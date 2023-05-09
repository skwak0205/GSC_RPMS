package com.gsc.util;

import com.dassault_systemes.enovia.e6wv2.foundation.db.ContextUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.UpdateActions;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.MqlUtil;
import matrix.db.Context;
import matrix.util.StringList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class ServiceUtil {
    public static void updateConnection(Context context, UpdateActions updateAction, String physicalId, String relName, String targetIds, boolean isFrom, boolean isOneToOne) throws Exception {
        try {
            ContextUtil.startTransaction(context,true);
            StringList targetList = FrameworkUtil.split(targetIds, ",");
            if (UpdateActions.CREATE.equals(updateAction)) {
                for (int i = 0; i < targetList.size(); i++) {
                    String targetId = targetList.get(i);
                    if (isFrom)
                        MqlUtil.mqlCommand(context, "add connection '$1' from $2 to $3", relName, physicalId, targetId);
                    else
                        MqlUtil.mqlCommand(context, "add connection '$1' from $2 to $3", relName, targetId, physicalId);

                }
            } else {
                if (isFrom) {
                    String asIsIds = MqlUtil.mqlCommand(context, String.format("print bus '%s' select from[%s].to.physicalid dump", physicalId, relName));
                    StringList asIsList = FrameworkUtil.split(asIsIds, ",");
                    if(isOneToOne) {
                        String relId = MqlUtil.mqlCommand(context,String.format("print bus '%s' select from[%s].physicalid dump", physicalId, relName));
                        String asIdTargetId = MqlUtil.mqlCommand(context,String.format("print bus '%s' select from[%s].to.physicalid dump", physicalId, relName));
                        if(!"".equals(relId) && targetList.size() > 0 && !targetList.get(0).equals(asIdTargetId)) {
                            MqlUtil.mqlCommand(context, "modify connection '$1' to $2", relId, targetList.get(0));
                        }
                        else if("".equals(relId) && "".equals(asIdTargetId) && targetList.size() > 0){
                            MqlUtil.mqlCommand(context, String.format("add connection '%s' from %s to %s", relName, physicalId, targetList.get(0)));
                        }
                        else if(!"".equals(relId) && targetList.size() == 0) {
                            MqlUtil.mqlCommand(context, "delete connection '$1'", relId);
                        }
                    }
                    else {
                        for (int i = 0; i < targetList.size(); i++) {
                            String targetId = targetList.get(i);
                            if (!asIsList.contains(targetId))
                                MqlUtil.mqlCommand(context, String.format("add connection '%s' from %s to %s", relName, physicalId, targetId));

                        }
                        for (int i = 0; i < asIsList.size(); i++) {
                            String asIsId = asIsList.get(i);
                            if (!targetList.contains(asIsId)) {
                                String relId = MqlUtil.mqlCommand(context, String.format("query connection type '%s' where from.physicalid==%s&&to.physicalid==%s select physicalid dump", relName, physicalId, asIsId));
                                MqlUtil.mqlCommand(context, "delete connection '$1'", relId.substring(relId.indexOf(",")+1));
                            }
                        }
                    }

                } else {
                    String asIsIds = MqlUtil.mqlCommand(context, String.format("print bus '%s' select to[%s].from.physicalid dump", physicalId, relName));
                    StringList asIsList = FrameworkUtil.split(asIsIds, ",");
                    if(isOneToOne) {
                        String relId = MqlUtil.mqlCommand(context,String.format("print bus '%s' select to[%s].physicalid dump", physicalId, relName));
                        String asIdTargetId = MqlUtil.mqlCommand(context,String.format("print bus '%s' select to[%s].from.physicalid dump", physicalId, relName));
                        if(!"".equals(relId) && targetList.size() > 0 && !targetList.get(0).equals(asIdTargetId)) {
                            MqlUtil.mqlCommand(context, "modify connection '$1' from $2", relId, targetList.get(0));
                        }
                        else if("".equals(relId) && "".equals(asIdTargetId) && targetList.size() > 0){
                            MqlUtil.mqlCommand(context, String.format("add connection '%s' from %s to %s", relName, targetList.get(0), physicalId));
                        }
                        else if(!"".equals(relId) && targetList.size() == 0) {
                            MqlUtil.mqlCommand(context, "delete connection '$1'", relId);
                        }
                    } else {
                        for (int i = 0; i < targetList.size(); i++) {
                            String targetId = targetList.get(i);
                            if (!asIsList.contains(targetId))
                                MqlUtil.mqlCommand(context, String.format("add connection '%s' from %s to %s", relName, targetId, physicalId));

                        }
                        for (int i = 0; i < asIsList.size(); i++) {
                            String asId = asIsList.get(i);
                            if (!targetList.contains(asId)) {
                                String relId = MqlUtil.mqlCommand(context, String.format("query connection type '%s' where from.physicalid==%s&&to.physicalid==%s select id dump", relName, asId, physicalId));
                                MqlUtil.mqlCommand(context, "delete connection '$1'", relId.substring(relId.indexOf(",")+1));
                            }
                        }
                    }

                }
            }
            ContextUtil.commitTransaction(context);
        }
        catch (Exception e){
            ContextUtil.abortTransaction(context);
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     *     Object 정보 가져오기
     * </pre>
     *
     * @param context context
     * @param type  조회할 Object의 타입
     * @param condition 검색할 key
     * @param code 검색할 value
     * @param select 조회할 정보 (null일 시 Object ID만 조회)
     * @return 조회된 정보
     * @throws FrameworkException
     */
    public static Map getObjectInfo(Context context, String type, String condition, String code, StringList select) throws FrameworkException {
        Map info = null;
        StringBuilder sb = new StringBuilder();
        sb.append(condition + "=='");
        sb.append(code);
        sb.append("'");

        if(select == null) {
            select = new StringList();
            select.add("id");
        }

        MapList m = DomainObject.findObjects(context, type, null, sb.toString(), select);
        if (m.size() == 1) {
            info = (Map) m.get(0);
        }

        return info;
    }

    /**
     * <pre>
     * 입력 String InputDate 데이터가 Null이 아닌지 체크 - Null 이 아닐 경우, True(boolean)를 Return. Null 일 경우, False(boolean) Return
     * </pre>
     *
     * @param InputData
     * @return Outputboolean
     * @throws Exception
     */
    public static boolean chkNotNull(String InputData){
        try {
            boolean Outputboolean = false;
            if(InputData != null && !InputData.isEmpty() && InputData != ""){
                Outputboolean = true;
            }else{
                Outputboolean = false;
            }

            return Outputboolean;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static String whereClause(String InputData){
        try {
            String outputWhereClause = InputData;
            return outputWhereClause;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * <pre> Revision 생성</pre>
     * @param counter
     * @param var1
     * @param var2
     * @return
     */
    public static String getUniqueRevision(int counter, String var1, int var2) {
        ++counter;
        if (counter < 0) {
            counter = 0;
        }

        StringBuffer var3 = new StringBuffer(35);
        if (var2 == 0) {
            var3.append(var1);
            var3.append(counter);
            var3.append((new Date()).getTime());
        } else {
            String var4 = (new Long((new Date()).getTime())).toString();
            if (var2 < var4.length()) {
                var4 = var4.substring(var4.length() - var2, var4.length());
            }

            var3.append(var1);
            var3.append(counter);
            var3.append(var4);
        }

        return var3.toString();
    }

    /**
     * <pre>인터페이스 배치 실행 시 로그 출력</pre>
     * @param msg
     * @return
     */
    public static String printLog(String msg) {
        LocalDateTime now = LocalDateTime.now();
        String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String log = String.format("[%s] >>> %s", formatedNow, msg);
        return log;
    }

    public static void main(String[] args) throws  Exception {
        String objectId = "29614DCF00003B3863B28A0B000002E6";
        String targetIds = "29523ABE00004538636066F0000058EC";
        String relName = "Resource Pool";
        Locale locale = new Locale("en-US");
        Context context = new matrix.db.Context("");
        context.setUser("admin_platform");
        context.setPassword("Qwer1234");
        context.setLocale(locale);
        context.setRole("ctx::VPLMProjectLeader.Company Name.RPMS");
        context.connect();
        updateConnection(context,UpdateActions.MODIFY,objectId,relName,targetIds,true, true);
    }
}
