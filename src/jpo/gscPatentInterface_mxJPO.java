import com.gsc.dbConfig.dbSessionUtil;
import com.gsc.dbConfig.utils;
import com.gsc.util.ServiceUtil;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.MapList;
import matrix.db.Context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class gscPatentInterface_mxJPO {
    private static final String _namespace = "com.gsc.gscInterface.patentIF";
    private static dbSessionUtil session = new dbSessionUtil(_namespace);

    public static void main(String[] args) {
        Context context = utils.getContext();
        executePatentInterface(context, null);
    }

    public static void executePatentInterface(Context context, String args[]) {
        String if_msg = null;
        String mstNo = "";
        int count = 0;

        try {
            Map<String, Object> param = new HashMap<>();
            MapList patentList = (MapList) session.executeSql("list", "selectPatentList", param);
            System.out.println(ServiceUtil.printLog("[SQL] List Patent Where IF_YN = 'N'"));

            Iterator patentItr = patentList.iterator();
            while (patentItr.hasNext()) {
                ContextUtil.startTransaction(context, true);

                Map patentInfo = (Map) patentItr.next();
                mstNo = patentInfo.get("MST_NO").toString().trim();
                String koName = patentInfo.get("KO_NAME") != null ? patentInfo.get("KO_NAME").toString() : "";
                String koAppTitle = patentInfo.get("KO_APP_TITLE") != null ? patentInfo.get("KO_APP_TITLE").toString() : "";
                String priorityDate = patentInfo.get("PRIORITY_DATE") != null ? patentInfo.get("PRIORITY_DATE").toString() : "";
                String priorityMstNo = patentInfo.get("PRIORITY_MST_NO") != null ? patentInfo.get("PRIORITY_MST_NO").toString() : "";
                String regDate = patentInfo.get("REG_DATE") != null ? patentInfo.get("REG_DATE").toString() : "";
                String regNo = patentInfo.get("REG_NO") != null ? patentInfo.get("REG_NO").toString() : "";
                String countryNameTitle = patentInfo.get("COUNTRY_NAME_TITLE") != null ? patentInfo.get("COUNTRY_NAME_TITLE").toString() : "";
                String wfStatusName = patentInfo.get("WF_STATUS_NAME") != null ? patentInfo.get("WF_STATUS_NAME").toString() : "";
                String updDatetime = patentInfo.get("UPD_DATETIME") != null ? patentInfo.get("UPD_DATETIME").toString() : "";

                Map checkPatent = ServiceUtil.getObjectInfo(context, "gscPatent", "name", mstNo, null);
                if (checkPatent == null) {
                    // Patent Object 생성
                    DomainObject patentObj = DomainObject.newInstance(context);
                    patentObj.createObject(context, "gscPatent", mstNo, "-", "gscInterfaceDeliverable", context.getVault().getName());
                    String newPatentOid = patentObj.getId();

                    Map attr = new HashMap();
                    attr.put("gscMstNo", mstNo);
                    attr.put("gscKoName", koName);
                    attr.put("gscKoAppTitle", koAppTitle);
                    attr.put("gscPriorityDate", priorityDate);
                    attr.put("gscPriorityMstNo", priorityMstNo);
                    attr.put("gscRegDate", regDate);
                    attr.put("gscRegNo", regNo);
                    attr.put("gscCountryNameTitle", countryNameTitle);
                    attr.put("gscWfStatusName", wfStatusName);
                    attr.put("gscUpdDatetime", updDatetime);
                    patentObj.setAttributeValues(context, attr);

                    if_msg = "'" + mstNo + "' 의 특허가 생성되었습니다.";
                    System.out.println(ServiceUtil.printLog("[CREATE] New gscPatentIf Object -> " + newPatentOid));

                } else {
                    // Patent Object 업데이트
                    String patentOid = checkPatent.get("id").toString();
                    DomainObject patentObj = new DomainObject(patentOid);
                    Map attr = patentObj.getAttributeMap(context, true);

                    if (!attr.get("gscKoName").equals(koName)) attr.put("gscKoName", koName);
                    if (!attr.get("gscKoAppTitle").equals(koAppTitle)) attr.put("gscKoAppTitle", koAppTitle);
                    if (!attr.get("gscPriorityDate").equals(priorityDate)) attr.put("gscPriorityDate", priorityDate);
                    if (!attr.get("gscPriorityMstNo").equals(priorityMstNo))
                        attr.put("gscPriorityMstNo", priorityMstNo);
                    if (!attr.get("gscRegDate").equals(regDate)) attr.put("gscRegDate", regDate);
                    if (!attr.get("gscRegNo").equals(regNo)) attr.put("gscRegNo", regNo);
                    if (!attr.get("gscCountryNameTitle").equals(countryNameTitle))
                        attr.put("gscCountryNameTitle", countryNameTitle);
                    if (!attr.get("gscWfStatusName").equals(wfStatusName)) attr.put("gscWfStatusName", wfStatusName);
                    if (!attr.get("gscUpdDatetime").equals(updDatetime)) attr.put("gscUpdDatetime", updDatetime);

                    patentObj.setAttributeValues(context, attr);
                    if_msg = "'" + mstNo + "' 의 특허의 정보가 수정되었습니다.";
                    System.out.println(ServiceUtil.printLog("[UPDATE] Update 'gscPatentIf' object's attribute ->" + patentOid));
                }

                ContextUtil.commitTransaction(context);
                // 인터페이스 실행이 성공적으로 완료 될 시, ENOVIA_IF.PATENT_IF IF_YN (Y), IF_MSG, IF_RESULT (Success) 컬럼 업데이트
                session.updateIF(null, "Y", "Success", if_msg, mstNo);
                count++;
            }

            System.out.println(ServiceUtil.printLog("[TOTAL] Executed Count : " + count));
        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            e.printStackTrace();

            // 인터페이스 실행에 오류가 발생할 시, ENOVIA_IF.PATENT_IF IF_YN (N), IF_MSG, IF_RESULT (Fail) 컬럼 업데이트
            session.updateIF(null, "N", "Fail", e.toString(), mstNo);
        }
    }
}
