import com.gsc.dbConfig.dbSessionUtil;
import com.gsc.dbConfig.utils;
import com.gsc.util.ServiceUtil;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.*;
import matrix.db.AttributeList;
import matrix.db.BusinessObject;
import matrix.db.Context;
import matrix.util.StringList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class gscBusinessUnitInterface_mxJPO {
    private static final String _namespace = "com.gsc.gscInterface.deptIF";
    private static dbSessionUtil session = new dbSessionUtil(_namespace);
    private static StringList select = new StringList();

    public static void main(String[] args) throws Exception {
        Context context = utils.getContext();
        executeDeptInterface(context, null);
    }

    /**
     * <pre>
     *     부서 인터페이스 실행
     * </pre>
     *
     * @param context context
     * @param args    arguments
     */
    public static void executeDeptInterface(Context context, String args[]) {
        select.add("id");
        select.add("attribute[Title].value");
        select.add("attribute[gscManager].value");
        select.add("to[Division].from.name");

        try {
            Map<String, Object> param = new HashMap<>();
            String today = session.getDate();
            param.put("date", today);
            System.out.println(ServiceUtil.printLog("[SQL] List Budget since updated >= " + today));

            MapList deptList = (MapList) session.executeSql("list", "selectDeptList", param);
            int count = 0;

            if (deptList.size() > 0) {
                for (int i = 0; i < deptList.size(); i++) {
                    Map deptInfo = (Map) deptList.get(i);
                    String deptYn = deptInfo.get("DEPT_USEYN").toString();

                    if (deptYn.equals("Y")) {
                        syncBU(context, deptInfo);
                    } else if (deptYn.equals("N")) {
                        demoteBU(context, deptInfo);
                    }

                    count++;
                }
            }
            System.out.println(ServiceUtil.printLog("[TOTAL] Executed Count : " + count));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     *     Business Unit 생성 및 업데이트
     * </pre>
     *
     * @param context context
     * @param buInfo  Business Unit 정보
     */
    public static void syncBU(Context context, Map buInfo) {
        String deptId = null;
        String if_msg = null;
        try {
            ContextUtil.startTransaction(context, true);
            deptId = buInfo.get("DEPT_ID").toString().trim();
            MapList checkExist = DomainObject.findObjects(context, DomainConstants.TYPE_BUSINESS_UNIT, null, "name==" + deptId, select);
            if (checkExist.isEmpty()) {
                String buId = FrameworkUtil.autoRevision(context, DomainConstants.TYPE_BUSINESS_UNIT, deptId, DomainConstants.POLICY_ORGANIZATION, null);
                BusinessObject buObj = new BusinessObject(buId);

                Map attr = new HashMap();
                attr.put("Title", buInfo.get("DEPT_NAME").toString().trim());
                attr.put("Organization ID", deptId);
                attr.put("gscManager", buInfo.get("CAP_EMPL_NO").toString().trim());
                buObj.setAttributeValues(context, AttributeList.create(attr));
                buObj.promote(context);

                if (deptId.equals("F00050")) { // 부서가 '기술연구소'인 경우, 최상위 부서로 Company Object와 연결
                    String companyId = ServiceUtil.getObjectInfo(context, "Company", "name", "Company Name", null).get("id").toString();
                    MqlUtil.mqlCommand(context, String.format("connect bus '%s' Relationship 'Division' to '%s'", companyId, buId));

                } else {
                    // 상위 부서와 연결
                    String parentOid = ServiceUtil.getObjectInfo(context, "Company", "name", buInfo.get("PARENT_CODE").toString(), null).get("id").toString();
                    MqlUtil.mqlCommand(context, String.format("connect bus '%s' Relationship 'Division' to '%s'", parentOid, buId));
                }
                if_msg = buInfo.get("DEPT_NAME").toString().trim() + " 부서를 새로 생성하였습니다.";
                System.out.println(ServiceUtil.printLog("[CREATE] New Business Unit Object for " + deptId + " -> " + buId));

            } else {
                //코드가 같은 Business Unit이 이미 존재할 경우 업데이트
                Iterator checkExistItr = checkExist.iterator();
                while (checkExistItr.hasNext()) {
                    Map buObjInfo = (Map) checkExistItr.next();
                    String buId = buObjInfo.get("id").toString();
                    BusinessObject oriObj = new BusinessObject(buId);
                    String originalName = buObjInfo.get("attribute[Title].value").toString();
                    String newName = buInfo.get("DEPT_NAME").toString().trim();
                    String originalManager = buObjInfo.get("attribute[gscManager].value").toString();
                    String newManager = buInfo.get("CAP_EMPL_NO").toString().trim();

                    if (!originalName.equals(newName)) { // 부서 이름이 변경된 경우
                        Map attr = new HashMap();
                        attr.put("Title", newName);
                        oriObj.setAttributeValues(context, AttributeList.create(attr));
                        if_msg = "'" + originalName + "' 부서의 이름이 '" + newName + "'으로 변경되었습니다.";
                    }

                    if (!originalManager.equals(newManager)) { // 부서 팀장이 변경된 경우
                        Map attr = new HashMap();
                        attr.put("gscManager", newManager);
                        oriObj.setAttributeValues(context, AttributeList.create(attr));
                        if_msg = "부서의 팀장이 '" + newManager + "'으로 변경되었습니다.";
                    }

                    if (!buInfo.get("DEPT_ID").toString().equals("F00050")) {
                        String originParent = buObjInfo.get("to[Division].from.name") != null ? buObjInfo.get("to[Division].from.name").toString() : null;
                        String newParent = buInfo.get("PARENT_CODE").toString();
                        String newParentOid = ServiceUtil.getObjectInfo(context, DomainConstants.TYPE_BUSINESS_UNIT, "name", newParent, null).get("id").toString();

                        if (originParent != null) {
                            String type = originParent.contains("Company") ? DomainConstants.TYPE_COMPANY : DomainConstants.TYPE_BUSINESS_UNIT;
                            String originParentOid = ServiceUtil.getObjectInfo(context, type, "name", originParent, null).get("id").toString();

                            if (!originParent.equals(newParent)) { // 상위 부서 변경 시 relationship 재설정
                                MqlUtil.mqlCommand(context, String.format("disconnect bus '%s' Relationship 'Division' to '%s';", originParentOid, buId));
                                MqlUtil.mqlCommand(context, String.format("connect bus '%s' Relationship 'Division' to '%s';", newParentOid, buId));

                                String msg = "부서의 상위 부서가 변경되었습니다.";
                                if_msg = if_msg == null ? msg : if_msg + "\n" + msg;
                            }
                        } else {
                            MqlUtil.mqlCommand(context, String.format("connect bus '%s' Relationship 'Division' to '%s';", newParentOid, buId));
                            String msg = "부서의 상위 부서를 추가하였습니다.";
                            if_msg = if_msg == null ? msg : if_msg + "\n" + msg;
                        }
                    }

                    System.out.println(ServiceUtil.printLog("[UPDATE] update Business Unit object's attribute ->" + buId));
                }
            }

            ContextUtil.commitTransaction(context);

            // 인터페이스 실행이 성공적으로 완료 될 시, ENOVIA_IF.DEPT_IF의 IF_YN (Y), IF_MSG, IF_RESULT (Success) 컬럼 업데이트
            session.updateIF(null, "Y", "Success", if_msg, deptId);
        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            e.printStackTrace();

            // 인터페이스 실행에 오류가 발생할 시, ENOVIA_IF.DEPT_IF의 IF_YN (N), IF_MSG, IF_RESULT (Fail) 컬럼 업데이트
            session.updateIF(null, "N", "Fail", e.toString(), deptId);
        }
    }

    /**
     * <pre>
     *     Platform에 존재하는 Business Unit이나 DB상에 존재하지 않는 경우 Business Unit을 demote함
     * </pre>
     *
     * @param context context
     */
    public static void demoteBU(Context context, Map deptInfo) {
        String deptId = deptInfo.get("DEPT_ID").toString();
        StringList s = new StringList();
        s.add("id");
        s.add("name");
        try {
            MapList buList = DomainObject.findObjects(context, "Business Unit", null, "name==" + deptId, s);
            for (int i = 0; i < buList.size(); i++) {
                Map bu = (Map) buList.get(i);
                String buId = bu.get("id").toString();
                String buCode = bu.get("name").toString();

                ContextUtil.startTransaction(context, true);
                DomainObject dObj = new DomainObject(buId);
                dObj.demote(context);
                ContextUtil.commitTransaction(context);
                String if_msg = "'" + buCode + "'는 더이상 존재하지 않는 부서 입니다.";

                // 인터페이스 실행이 성공적으로 완료 될 시, ENOVIA_IF.DEPT_IF의 IF_YN (Y), IF_MSG, IF_RESULT (Success) 컬럼 업데이트
                session.updateIF(null, "Y", "Success", if_msg, deptId);
                System.out.println(ServiceUtil.printLog(String.format("[INACTIVE] Change '%s' status -> Inactive", buId)));
            }
        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            e.printStackTrace();

            // 인터페이스 실행에 오류가 발생할 시, ENOVIA_IF.DEPT_IF의 IF_YN (N), IF_MSG, IF_RESULT (Fail) 컬럼 업데이트
            session.updateIF(null, "N", "Fail", e.toString(), deptId);
        }
    }
}
