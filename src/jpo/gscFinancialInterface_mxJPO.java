import com.gsc.dbConfig.dbSessionUtil;
import com.gsc.dbConfig.utils;
import com.gsc.util.ServiceUtil;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.MqlUtil;
import matrix.db.Context;
import matrix.util.StringList;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class gscFinancialInterface_mxJPO {
    private static final String _namespace = "com.gsc.gscInterface.financialIF";
    private static dbSessionUtil session = new dbSessionUtil(_namespace);

    public static void main(String[] args) throws Exception {
        Context context = utils.getContext();
        executeBudgetInterface(context, null);
        //executeInvestInterface(context, null);
        //connectProjectBudget(context, null);
        //getProjectTotalCost(context, new String[]{"invest"});
    }

    /**
     * <pre>
     *     ENOVIA_IF.BUDGET_IF 테이블의 Data를 gscBudgetIf Object로 생성 혹은 Attribute 업데이트
     * </pre>
     *
     * @param context
     * @param args
     */
    public static void executeBudgetInterface(Context context, String[] args) {
        int count = 0;
        String if_msg = null;
        String rowId = "";

        try {
            Map<String, Object> param = new HashMap<>();
            String today = session.getDate();
            param.put("date", today);
            System.out.println(ServiceUtil.printLog("[SQL] List Budget since updated >= " + today));

            MapList costList = (MapList) session.executeSql("list", "selectBudgetList", param);
            for (int i = 0; i < costList.size(); i++) {
                Map costInfo = (Map) costList.get(i);
                rowId = costInfo.get("ROWID").toString();
                String gscKostl = costInfo.get("KOSTL").toString().trim();
                String gscKostlTx = costInfo.get("KOSTL_TX").toString().trim();
                String gscSaknr = costInfo.get("SAKNR").toString().trim();
                String gscSaknrTx = costInfo.get("SAKNR_TX").toString().trim();
                String gscAmtPlanYear = costInfo.get("AMT_PLAN_YEAR") != null ? costInfo.get("AMT_PLAN_YEAR").toString().trim() : "0.0";
                String gscAmtAct = costInfo.get("AMT_ACT") != null ? costInfo.get("AMT_ACT").toString().trim() : "0.0";
                String gscAmtActM = costInfo.get("AMT_ACT_M") != null ? costInfo.get("AMT_ACT_M").toString().trim() : "0.0";
                String gscWaers = costInfo.get("WAERS").toString().trim();

                ContextUtil.startTransaction(context, true);

                String revision = getCurrentYear();
                Map attr = new HashMap();
                attr.put("gscKostl", gscKostl);
                attr.put("gscKostlTx", gscKostlTx);
                attr.put("gscSaknr", gscSaknr);
                attr.put("gscSaknrTx", gscSaknrTx);
                attr.put("gscAmtPlanYear", gscAmtPlanYear);
                attr.put("gscAmtAct", gscAmtAct);
                attr.put("gscAmtActM", gscAmtActM);
                attr.put("gscWaers", gscWaers);

                String objName = gscKostl + "-" + gscSaknr;
                MapList findBudget = DomainObject.findObjects(context, "gscBudgetIf", null, String.format("name=='%s' && revision=='%s'", objName, revision), new StringList("id"));
                String budgetOid = null;
                if (findBudget.isEmpty()) {
                    DomainObject newBudget = new DomainObject();
                    newBudget.createObject(context, "gscBudgetIf", objName, revision, "gscInterfaceDeliverable", context.getVault().getName());
                    newBudget.setAttributeValues(context, attr);
                    budgetOid = newBudget.getId(context);
                    if_msg = String.format("'%s'의 '%s' 비용 실적이 생성되었습니다.", gscKostlTx.trim(), gscSaknrTx.trim());
                    System.out.println(ServiceUtil.printLog("[CREATE] New gscBudgetIf Object -> " + budgetOid));

                } else {
                    Iterator budgetItr = findBudget.iterator();
                    while (budgetItr.hasNext()) {
                        Map budgetInfo = (Map) budgetItr.next();
                        budgetOid = budgetInfo.get("id").toString();
                    }

                    DomainObject budgetObj = DomainObject.newInstance(context, budgetOid);
                    budgetObj.setAttributeValues(context, attr);
                    if_msg = String.format("'%s'의 '%s' 비용 실적이 수정되었습니다.", gscKostlTx.trim(), gscSaknrTx.trim());
                    System.out.println(ServiceUtil.printLog("[UPDATE] Update 'gscBudgetIf' object's attribute ->" + budgetOid));
                }

                // 과제의 gscCostCenter attribute와 cost center가 같을 시, gscProjectBudgetIf로 연결
                String data = MqlUtil.mqlCommand(context, "temp query bus 'Project Space' * * where attribute[gscCostCenter]~='*" + gscKostl + "*' select id dump |");
                StringList list = FrameworkUtil.split(data, "\n");

                for (int k = 0; k < list.size(); k++) {
                    String row = list.get(k);
                    StringList items = FrameworkUtil.split(row, "|");
                    String projectOid = items.get(3);

                    String checkRel = MqlUtil.mqlCommand(context, String.format("query connection type gscProjectBudgetIf where 'from.id==%s && to.id==%s'", projectOid, budgetOid));
                    if (checkRel.isEmpty()) {
                        MqlUtil.mqlCommand(context, String.format("connect bus %s rel 'gscProjectBudgetIf' to %s", projectOid, budgetOid));
                        System.out.println(ServiceUtil.printLog("[CONNECT] connect gscProjectBudgetIf from '" + projectOid + "' to '" + budgetOid + "'"));
                    }
                }

                ContextUtil.commitTransaction(context);
                // 인터페이스 실행이 성공적으로 완료 될 시, ENOVIA_IF.BUDGET_IF의 IF_YN (Y), IF_MSG, IF_RESULT (Success) 컬럼 업데이트
                session.updateIF("updateBudgetIFColumn", "Y", "Success", if_msg, rowId);
                count++;
            }
            System.out.println(ServiceUtil.printLog("[TOTAL] Executed Count : " + count));
        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            e.printStackTrace();

            // 인터페이스 실행에 오류가 발생할 시, ENOVIA_IF.BUDGET_IF의 IF_YN (N), IF_MSG, IF_RESULT (Fail) 컬럼 업데이트
            session.updateIF("updateBudgetIFColumn", "N", "Fail", e.toString(), rowId);
        }
    }

    /**
     * <pre>ENOVIA_IF.INVEST_IF 테이블의 Data를 cost Item Object로 생성 혹은 Attribute 업데이트</pre>
     *
     * @param context
     * @param args
     */
    public static void executeInvestInterface(Context context, String[] args) {
        String rowId = null;
        String if_msg = null;
        int count = 0;

        try {
            Map<String, Object> param = new HashMap<>();
            String today = session.getDate();
            param.put("date", today);
            System.out.println(ServiceUtil.printLog("[SQL] List Invest since updated >= " + today));

            MapList investList = (MapList) session.executeSql("list", "selectInvestList", param);
            for (int i = 0; i < investList.size(); i++) {
                ContextUtil.startTransaction(context, true);

                Map investInfo = (Map) investList.get(i);
                rowId = investInfo.get("ROWID").toString();
                String gscBzCode = investInfo.get("BZ_CODE").toString();
                String revision = gscBzCode.split("-")[1];
                String gscBizNm = investInfo.get("BZ_NM").toString();
                String gscDeptNm = investInfo.get("DEPT_NM").toString();
                String gscLineNo = investInfo.get("LINE_NO").toString();
                String gscRemAmt = investInfo.get("REM_AMT").toString();
                String gscRsltAmt01 = investInfo.get("RSLT_AMT_01") != null ? investInfo.get("RSLT_AMT_01").toString() : "0.0";
                String gscRsltAmt02 = investInfo.get("RSLT_AMT_02") != null ? investInfo.get("RSLT_AMT_02").toString() : "0.0";
                String gscWbsCd = investInfo.get("WBS_CD") != null ? investInfo.get("WBS_CD").toString() : "";
                String gscMgmBgtAmt = investInfo.get("MGM_BGT_AMT") != null ? investInfo.get("MGM_BGT_AMT").toString() : "0.0";
                String gscPlnAmt = investInfo.get("PLN_AMT") != null ? investInfo.get("PLN_AMT").toString() : "0.0";
                String gscTrnsAmt = investInfo.get("TRNS_AMT") != null ? investInfo.get("TRNS_AMT").toString() : "0.0";

                Map attr = new HashMap();
                attr.put("gscBizNm", gscBizNm);
                attr.put("gscDeptNm", gscDeptNm);
                attr.put("gscLineNo", gscLineNo);
                attr.put("gscRemAmt", gscRemAmt);
                attr.put("gscRsltAmt01", gscRsltAmt01);
                attr.put("gscRsltAmt02", gscRsltAmt02);
                attr.put("gscWbsCd", gscWbsCd);
                attr.put("gscMgmBgtAmt", gscMgmBgtAmt);
                attr.put("gscPlnAmt", gscPlnAmt);
                attr.put("gscTrnsAmt", gscTrnsAmt);

                StringList select = new StringList();
                select.add("id");
                select.add("attribute[gscBizNm].value");
                select.add("attribute[gscDeptNm].value");
                select.add("attribute[gscLineNo].value");
                select.add("attribute[gscRemAmt].value");
                select.add("attribute[gscRsltAmt01].value");
                select.add("attribute[gscRsltAmt02].value");
                select.add("attribute[gscWbsCd].value");
                select.add("attribute[gscMgmBgtAmt].value");
                select.add("attribute[gscPlnAmt].value");
                select.add("attribute[gscTrnsAmt].value");

                MapList checkInvest = DomainObject.findObjects(context, "gscInvestIf", null, String.format("name=='%s' && revision=='%s'", gscBzCode, revision), select);
                if (checkInvest.isEmpty()) {
                    // Invest Object 생성
                    DomainObject newInvestObj = new DomainObject();
                    newInvestObj.createObject(context, "gscInvestIf", gscBzCode, revision, "gscInterfaceDeliverable", context.getVault().getName());
                    String newInvestOid = newInvestObj.getId(context);

                    newInvestObj.setAttributeValues(context, attr);
                    if_msg = "'" + gscBzCode + "'의 투자 실적이 생성되었습니다.";
                    System.out.println(ServiceUtil.printLog("[CREATE] New gscInvestIf Object -> " + newInvestOid));

                } else {
                    // Invest Object 업데이트
                    Iterator investItr = checkInvest.iterator();
                    while (investItr.hasNext()) {
                        Map investObjInfo = (Map) investItr.next();
                        String investOid = investObjInfo.get("id").toString();
                        DomainObject investObj = new DomainObject(investOid);

                        String oldGscBizNm = investObjInfo.get("attribute[gscBizNm].value").toString();
                        String oldGscDeptNm = investObjInfo.get("attribute[gscDeptNm].value").toString();
                        String oldGscLineNo = investObjInfo.get("attribute[gscLineNo].value").toString();
                        String oldGscRemAmt = investObjInfo.get("attribute[gscRemAmt].value").toString();
                        String oldGscRsltAmt01 = investObjInfo.get("attribute[gscRsltAmt01].value").toString();
                        String oldGscRsltAmt02 = investObjInfo.get("attribute[gscRsltAmt02].value").toString();
                        String oldGscWbsCd = investObjInfo.get("attribute[gscWbsCd].value").toString();
                        String oldGscMgmBgtAmt = investObjInfo.get("attribute[gscMgmBgtAmt].value").toString();
                        String oldGscPlnAmt = investObjInfo.get("attribute[gscPlnAmt].value").toString();
                        String oldGscTrnsAmt = investObjInfo.get("attribute[gscTrnsAmt].value").toString();

                        Map newAttr = new HashMap();
                        if (!oldGscBizNm.equals(gscBizNm)) newAttr.put("gscBizNm", gscBizNm);
                        if (!oldGscDeptNm.equals(gscDeptNm)) newAttr.put("gscDeptNm", gscDeptNm);
                        if (!oldGscLineNo.equals(gscLineNo)) newAttr.put("gscLineNo", gscLineNo);
                        if (!oldGscRemAmt.equals(gscRemAmt)) newAttr.put("gscRemAmt", gscRemAmt);
                        if (!oldGscRsltAmt01.equals(gscRsltAmt01)) newAttr.put("gscRsltAmt01", gscRsltAmt01);
                        if (!oldGscRsltAmt02.equals(gscRsltAmt02)) newAttr.put("gscRsltAmt02", gscRsltAmt02);
                        if (!oldGscWbsCd.equals(gscWbsCd)) newAttr.put("gscWbsCd", gscWbsCd);
                        if (!oldGscMgmBgtAmt.equals(gscMgmBgtAmt)) newAttr.put("gscMgmBgtAmt", gscMgmBgtAmt);
                        if (!oldGscPlnAmt.equals(gscPlnAmt)) newAttr.put("gscPlnAmt", gscPlnAmt);
                        if (!oldGscTrnsAmt.equals(gscTrnsAmt)) newAttr.put("gscTrnsAmt", gscTrnsAmt);

                        investObj.setAttributeValues(context, newAttr);
                        if_msg = "'" + gscBzCode + "'의 투자 실적이 업데이트 되었습니다.";
                        System.out.println(ServiceUtil.printLog("[UPDATE] Update 'gscInvestIf' object's attribute ->" + investOid));
                    }
                }
                ContextUtil.commitTransaction(context);

                // 인터페이스 실행이 성공적으로 완료 될 시, ENOVIA_IF.INVEST_IF의 IF_YN (Y), IF_MSG, IF_RESULT (Success) 컬럼 업데이트
                session.updateIF("updateInvestIFColumn", "Y", "Success", if_msg, rowId);
                count++;
            }

            System.out.println(ServiceUtil.printLog("[TOTAL] Executed Count : " + count));
        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            // 인터페이스 실행에 오류가 발생할 시, ENOVIA_IF.INVEST_IF의 IF_YN (N), IF_MSG, IF_RESULT (Fail) 컬럼 업데이트
            session.updateIF("updateInvestIFColumn", "N", "Fail", e.toString(), rowId);
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     *     과제 Object의 gscCostCenter attribute와 비교하여 gscBudgetIf object를 'gscProjectBudgetIf' relationship으로 연결
     * </pre>
     *
     * @param context
     * @param args
     */
    public static void connectProjectBudget(Context context, String[] args) {
        try {
            List<Map<String, String>> projectCostCenterList = new ArrayList<>();
            String data = MqlUtil.mqlCommand(context, "temp query bus 'Project Space' * * select id attribute[gscCostCenter].value dump |");
            StringList list = FrameworkUtil.split(data, "\n");
            for (int i = 0; i < list.size(); i++) {
                String row = list.get(i);
                StringList items = FrameworkUtil.split(row, "|");
                String projectOid = items.get(3);
                String projectCostCenter = items.get(4).trim();
                if (!projectCostCenter.equals("")) {
                    Map<String, String> projectInfo = null;
                    if (projectCostCenter.contains(",")) {
                        // 하나 이상의 cost center를 가지고 있는 과제 처리
                        for (String costCenter : projectCostCenter.split(",")) {
                            projectInfo = new HashMap<>();
                            projectInfo.put("costCenter", costCenter.trim());
                            projectInfo.put("projectOid", projectOid);
                            projectCostCenterList.add(projectInfo);
                        }
                    } else {
                        projectInfo = new HashMap<>();
                        projectInfo.put("costCenter", projectCostCenter.trim());
                        projectInfo.put("projectOid", projectOid);
                        projectCostCenterList.add(projectInfo);
                    }
                }
            }

            for (Map<String, String> info : projectCostCenterList) {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
                String costCenter = info.get("costCenter");
                String projectOid = info.get("projectOid");

                System.out.println("cost center >>> " + costCenter);
                System.out.println("project oid >>> " + projectOid);

                String checkBudget = MqlUtil.mqlCommand(context, String.format("temp query bus gscBudgetIf * * where 'attribute[gscKostl].value==\"%s\"' select id dump |", costCenter));
                StringList budgetList = FrameworkUtil.split(checkBudget, "\n");
                System.out.println(budgetList.size());
                for (int j = 0; j < budgetList.size(); j++) {
                    String budgetRow = budgetList.get(j);
                    StringList budgetItems = FrameworkUtil.split(budgetRow, "|");
                    String budgetOid = budgetItems.get(3);
                    System.out.println(budgetOid);

                    String checkRel = MqlUtil.mqlCommand(context, String.format("query connection type gscProjectBudgetIf where 'from.id==%s && to.id==%s'", projectOid, budgetOid));
                    if (checkRel.isEmpty()) {
                        System.out.println("연결");
                        ContextUtil.startTransaction(context, true);
                        MqlUtil.mqlCommand(context, String.format("connect bus %s rel 'gscProjectBudgetIf' to %s", projectOid, budgetOid));
                        ContextUtil.commitTransaction(context);
                    } else {
                        System.out.println("이미 연결");
                    }
                }
                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println();
            }

        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     *     과제와 연결된 투자, 비용 Object의 예산과 실적의 총 합계 구함
     * </pre>
     *
     * @param context
     * @param args
     */
    public static void getProjectTotalCost(Context context, String[] args) {
        try {
            String type = args[0];
            String relType = null;
            String objType = null;
            String attrPlan = null;
            String attrActual = null;
            String prjAttr = null;

            if (type.equals("invest")) {
                relType = "gscProjectInvestIf";
                objType = "gscInvestIf";
                attrPlan = "gscMgmBgtAmt";
                attrActual = "gscRsltAmt01";
                prjAttr = "gscInvestAmount";

            } else if (type.equals("budget")) {
                relType = "gscProjectBudgetIf";
                objType = "gscBudgetIf";
                attrPlan = "gscAmtPlanYear";
                attrActual = "gscAmtAct";
                prjAttr = "gscBudgetAmount";
            }

            DomainObject projectObj = new DomainObject();
            StringList select = new StringList();
            select.add("name");
            select.add("revision");
            select.add(String.format("attribute[%s].value", attrPlan));
            select.add(String.format("attribute[%s].value", attrActual));

            String data = MqlUtil.mqlCommand(context, String.format("temp query bus 'Project Space' * * where 'from[%s]==true' select id dump |", relType));
            StringList list = FrameworkUtil.split(data, "\n");

            for (int i = 0; i < list.size(); i++) {
                System.out.println("==========================================================");
                String row = list.get(i);
                StringList items = FrameworkUtil.split(row, "|");
                String projectOid = items.get(3);
                projectObj.setId(projectOid);
                System.out.println("project oid >>> " + projectOid);

                MapList relObj = projectObj.getRelatedObjects(context, relType, "*", select, (StringList) null, true, true, (short) 1, (String) null, "", 0);
                if (!relObj.isEmpty()) {
                    /*
                        {
                            "2022":
                                {
                                "actual": "23435",
                                "plan": "2334"
                                },
                            "2023":
                                {
                                "actual": "23435",
                                "plan": "2334"
                                }
                        }
                     */
                    JSONObject totalJson = new JSONObject();
                    JSONObject yearJson2022 = new JSONObject();
                    JSONObject yearJson2023 = new JSONObject();
                    Double totalPlan2022 = 0.0;
                    Double totalPlan2023 = 0.0;
                    Double totalActual2022 = 0.0;
                    Double totalActual2023 = 0.0;

                    System.out.println("rel obj size >>> " + relObj.size());
                    for (int j = 0; j < relObj.size(); j++) {
                        Map relObjInfo = (Map) relObj.get(j);
                        String name = relObjInfo.get("name").toString();
                        String revision = relObjInfo.get("revision").toString();
                        String plan = relObjInfo.get("attribute[" + attrPlan + "].value").toString();
                        String actual = relObjInfo.get("attribute[" + attrActual + "].value").toString();

                        System.out.println("+++++++++++" + name + "++++++++++++");

                        if (revision.equals("2022")) {
                            System.out.println("2022 plan >>> " + plan);
                            System.out.println("2022 actual >>> " + actual);

                            totalPlan2022 += Double.parseDouble(plan);
                            totalActual2022 += Double.parseDouble(actual);

                            BigDecimal bD = new BigDecimal(totalPlan2022);
                            BigDecimal bD2 = new BigDecimal(totalActual2022);

                            System.out.println("total plan 2022 >>> " + bD);
                            System.out.println("total Actual 2022 >>> " + bD2);

                            yearJson2022.put("plan", bD);
                            yearJson2022.put("actual", bD2);

                            totalJson.put(revision, yearJson2022);

                        } else if (revision.equals("2023")) {
                            System.out.println("2023 plan >>> " + plan);
                            System.out.println("2023 actual >>> " + actual);

                            totalPlan2023 += Double.parseDouble(plan);
                            totalActual2023 += Double.parseDouble(actual);

                            BigDecimal bD = new BigDecimal(totalPlan2023);
                            BigDecimal bD2 = new BigDecimal(totalActual2023);

                            System.out.println("total plan 2023 >>> " + bD);
                            System.out.println("total Actual 2023 >>> " + bD2);

                            yearJson2023.put("plan", bD);
                            yearJson2023.put("actual", bD2);

                            totalJson.put("2023", yearJson2023);
                        }
                    }

                    System.out.println(totalJson);

                    ContextUtil.startTransaction(context, true);
                    MqlUtil.mqlCommand(context, String.format("mod bus %s '%s' '%s'", projectOid, prjAttr, totalJson));
                    ContextUtil.commitTransaction(context);
                }
                System.out.println("==========================================================");
                System.out.println();
            }
        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     *     현재 연도 구함
     * </pre>
     * @return
     */
    private static String getCurrentYear() {
        LocalDate now = LocalDate.now();
        return String.valueOf(now.getYear());
    }
}
