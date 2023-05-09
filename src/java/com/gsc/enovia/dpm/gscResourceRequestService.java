package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.e6wv2.foundation.*;
import com.dassault_systemes.enovia.e6wv2.foundation.db.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.ArgMap;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMapAdapter;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.Select;
import com.dassault_systemes.enovia.e6wv2.foundation.util.FormatUtil;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.program.ProgramCentralConstants;
import matrix.db.Context;
import matrix.util.StringList;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class gscResourceRequestService implements ServiceConstants {

    public static Datacollection getResourceRequests(Context context, String[] args) throws FoundationException {
        ServiceParameters serviceParameters = (ServiceParameters) JPOUtil.unpackArgs(args);
        HttpServletRequest var3 = serviceParameters.getHttpRequest();
        long var4 = System.currentTimeMillis();
//        com.dassault_systemes.enovia.dpm.ServiceUtil.checkLicenseDPM(context);
        Datacollection datacollection = serviceParameters.getDatacollection();
        List selects = serviceParameters.getSelects();
        ArgMap argMap = serviceParameters.getServiceArgs();
        String state = (String) argMap.get("state");
        String owned = (String) argMap.get("owned");
        String projectId = (String) argMap.get("projectId");
        boolean isOwned = false;
        if (owned != null && !owned.isEmpty()) {
            isOwned = Boolean.parseBoolean(owned);
        }

        Datacollection datacollection1;
        if (datacollection != null && !datacollection.getDataobjects().isEmpty()) {
            if(projectId != null && !"".equals(projectId)){
                datacollection1 = gscResourceRequest.getUserResourceRequests(context, datacollection, false, selects, (String) null, projectId);
            }else{
                datacollection1 = gscResourceRequest.getUserResourceRequests(context, datacollection, false, selects, (String) null, projectId);
            }
        } else {
            datacollection1 = gscResourceRequest.getUserResourceRequests(context, (Datacollection) null, isOwned, selects, state, projectId);
        }

        String var14 = String.format("%s%s (%d) ...\t", ">>> PROGRAM (SVC):  ", "getPrograms", datacollection1.getDataobjects().size());
        FoundationUtil.debug(var14, var4);
        return datacollection1;
    }

    private static String getCompleteFilterClause(Context var0, String var1, String var2) {
        String var3 = "";
        String var4 = "";
        var2 = var2 == null ? "" : var2.replaceAll("current != Complete AND", "");
        if (var1 != null && !var1.equalsIgnoreCase("all")) {
            if (!var1.equalsIgnoreCase("none")) {
                int var5 = Integer.valueOf(var1);
                LocalDate var6 = LocalDate.now();
                LocalDate var7 = var6.plusDays((long) (-var5));
                ZoneId var8 = ZoneId.systemDefault();
                Date var9 = Date.from(var7.atStartOfDay(var8).toInstant());
                var3 = FormatUtil.formatMxDate(var0, var9, (SimpleDateFormat) null);
            }

            if (var3 != null) {
                if (!var3.isEmpty()) {
                    var4 = "(attribute[Task Actual Finish Date]=='' || attribute[Task Actual Finish Date]>='" + var3 + "' )";
                } else {
                    var4 = String.format("current != %s ", "Complete");
                }

                if (var2 != null && !var2.isEmpty()) {
                    var2 = var2 + " AND " + var4;
                } else {
                    var2 = var4;
                }
            }
        }

        return var2;
    }

    static StringList _getResourceRequestState() {
        StringList var0 = new StringList(6);
        var0.add("Create");
        var0.add("Requested");
        var0.add("Proposed");
        var0.add("Committed");
        var0.add("Rejected");
        return var0;
    }

    public static void getResourceRequestInfo(Context var0, Datacollection var1, List var2) throws FoundationException {
//        com.dassault_systemes.enovia.dpm.ServiceUtil.checkLicenseProject(var0, (HttpServletRequest) null);
        if (var2 != null && !var2.isEmpty()) {
            if (var1.getDataobjects().size() > 0) {
                ObjectUtil.print(var0, var1, (PrintData) null, var2, true);
            }

        } else {
            throw new FoundationException("You must provide some selectables to ProjectSpace.getProjectInfo");
        }
    }

    public static Dataobject updateResourceRequests(Context context, String[] var1) throws Exception {
        ServiceParameters serviceParameters = (ServiceParameters) JPOUtil.unpackArgs(var1);
        Datacollection datacollection = serviceParameters.getDatacollection();
        Dataobject dataobject = (Dataobject) datacollection.getDataobjects().get(0);
        UpdateActions updateAction = dataobject.getUpdateAction();
        dataobject = updateResourceRequests(context, serviceParameters, dataobject, updateAction);
        return dataobject;
    }

    private static String getResourceRequestCode(Context context, Dataobject dataobject) throws FoundationUserException {
        String budgetCode = DataelementMapAdapter.getDataelementValue(dataobject, "projectCode");
        if (budgetCode != null) {
            budgetCode = budgetCode.trim();
        }

        UpdateActions updateAction = dataobject.getUpdateAction();
        if ((budgetCode == null || !budgetCode.isEmpty()) && (budgetCode != null || !UpdateActions.CREATE.equals(updateAction))) {
            return budgetCode;
        } else {
            String var4 = PropertyUtil.getTranslatedValue(context, "Foundation", "emxFoundation.Widget.Error.EmptyProjectCodeNotAllowed", context.getLocale());
            throw new FoundationUserException(var4);
        }
    }

    private static String getBusinessUnitId(Context context, Dataobject dataobject, String nameCode) throws FoundationUserException {
        // String nameCode = DataelementMapAdapter.getDataelementValue(dataobject, "businessUnitId");
        if (nameCode != null) {
            nameCode = nameCode.trim();
        }

        UpdateActions updateAction = dataobject.getUpdateAction();
        if ((nameCode == null || !nameCode.isEmpty()) && (nameCode != null || !UpdateActions.CREATE.equals(updateAction))) {
            return nameCode;
        } else {
            String var4 = PropertyUtil.getTranslatedValue(context, "Foundation", "emxFoundation.Widget.Error.EmptyBusinessUnitIdNotAllowed", context.getLocale());
            throw new FoundationUserException(var4);
        }
    }

    public static Dataobject updateResourceRequests(Context context, ServiceParameters serviceParameters, Dataobject dataobject, UpdateActions updateActions) throws Exception {
        if (!UpdateActions.CREATE.equals(updateActions) && !UpdateActions.MODIFY.equals(updateActions)) {
            if (UpdateActions.DELETE.equals(updateActions)) {
                DomainObject domBusGoal = new DomainObject(dataobject.getId());
                domBusGoal.deleteObject(context);
                // com.gsc.apps.program.Financials financialItem = new com.gsc.apps.program.FinancialItem(dataobject.getId());
                // financialItem.deleteObject(context);
                dataobject = null;
            }
        } else {
            HashMap<String, String> attrMap = new HashMap<>(5);
            // dpm-services.xml의 field-selectable autosave 설정이 true 인 항목을 조회함
            ServiceDataFunctions.fillUpdates(context, dataobject, serviceParameters.getAutosaveFields(), attrMap);

            String busResRqId;
            String interfaces;
            String type = "Resource Request";
            String policy = "Resource Request";
            String gscYear = DataelementMapAdapter.getDataelementValue(dataobject, "gscYear");
            String projectId = DataelementMapAdapter.getDataelementValue(dataobject, "projectId");

            // 시작일 종료일 (gscYear 1월 1일~ 12월 31일 자동 입력)
            //String startDate = "01/01/" + gscYear;
            //String endDate = "12/31/" + gscYear;

            //attrMap.put("Start Date",startDate);
            // attrMap.put("End Date",endDate);

            String businessUnitId = DataelementMapAdapter.getDataelementValue(dataobject, "businessUnitId");
            String busName = "RPMS";

            if(businessUnitId != null){
                businessUnitId = getBusinessUnitId(context, dataobject, businessUnitId);
                busName = MqlUtil.mqlCommand(context, "print bus $1 select name dump;",businessUnitId);
            }

            String name = gscYear + "-" + busName;

            if (UpdateActions.CREATE.equals(updateActions)) {
                type = type != null && !type.isEmpty() ? type : dataobject.getType();
                ArgMap serviceArgs = serviceParameters.getServiceArgs();
                String triggerStatus = (String) serviceArgs.get("TriggerStatus");

                if (triggerStatus != null && triggerStatus.equalsIgnoreCase("false")) {
                    Map<String, StringList> interfacesMap = ExtensionUtil.getAutomaticInterfacesByType(context, ProgramCentralConstants.TYPE_FINANCIAL_ITEM);
                    StringList projectSpaceList = interfacesMap != null ? interfacesMap.get(ProgramCentralConstants.TYPE_FINANCIAL_ITEM) : null;
                    if (projectSpaceList != null) {
                        interfaces = projectSpaceList.join(",");
                        attrMap.put("interface", interfaces);
                    }
                }

                busResRqId = gscResourceRequestService.createResourceRequest(context, type, name, attrMap, policy);

                // RelationShip 처리
                com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busResRqId, "Resource Plan", projectId, false, true);
                com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busResRqId, "Resource Pool", businessUnitId, true, true);

                dataobject.setId(busResRqId);

            } else if (UpdateActions.MODIFY.equals(updateActions)) {
                busResRqId = dataobject.getId();

                // RelationShip 처리
                com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busResRqId, "Resource Plan", projectId, false, true);
                com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busResRqId, "Resource Pool", businessUnitId, true, true);

                com.gsc.enovia.dpm.ServiceUtil.addToMap(attrMap, "name", name);
                gscResourceRequestService.modifyResourceRequest(context, busResRqId, attrMap);
            } else {
                busResRqId = dataobject.getId();

                com.gsc.enovia.dpm.ServiceUtil.addToMap(attrMap, "name", name);
                gscResourceRequestService.modifyResourceRequest(context, busResRqId, attrMap);
            }

            dataobject = getObject(context, serviceParameters, dataobject.getId(), (String) null, (String) null);
            Dataobject var18 = new Dataobject();
            var18.setUpdateAction(updateActions);
//            RelateddataMapAdapter.addRelatedData(dataobject, "performRollup", var18);
        }

        return dataobject;
    }

    protected static Dataobject getObject(Context var0, ServiceParameters var1, String var2, String var3, String var4) throws FoundationException {
        String var5 = var1.getServiceName();
        var1.setServiceName(var5 + "/" + var2);
        if (var3 != null) {
            var1.getServiceArgs().put("$include", var3);
        }

        if (var4 != null) {
            var1.getServiceArgs().put("$fields", var4);
        }

        Servicedata var6 = ServiceBase.getData(var0, var1);
        Dataobject var7 = null;
        if (var6.getData().size() > 0) {
            var7 = (Dataobject) var6.getData().get(0);
        } else {
            ArrayList<Selectable> var8 = new ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable>(6);
            var8.add(new Select("physicalid", "physicalid", ExpressionType.BUS, (Format) null, false));
            var8.add(new Select("type", "type", ExpressionType.BUS, (Format) null, false));
            var8.add(new Select("name", "name", ExpressionType.BUS, (Format) null, false));
            var8.add(new Select("revision", "revision", ExpressionType.BUS, (Format) null, false));
            var8.add(new Select("originated", "originated", ExpressionType.BUS, (Format) null, false));
            var8.add(new Select("description", "description", ExpressionType.BUS, (Format) null, false));
            if (var7 == null && var2 != null && !var2.isEmpty()) {
                var7 = ObjectUtil.print(var0, var2, (PrintData) null, var8);
            }
        }

        return var7;
    }

    public static String createResourceRequest(Context context, String type, String name, Map attrMap, String policy) throws FoundationException {
        try {
            //checkLicenseProjectLead(context, (HttpServletRequest)null);
            ObjectEditUtil.checkNameRules(context, name);
            String typeName = type != null ? type : "Budget";
            String policyName = policy != null && !policy.isEmpty() ? policy : "Budget";
            gscResourceRequest resourceRequest = new gscResourceRequest();
            resourceRequest.create(context, typeName, name, policyName, (String) null);
            String resourceRequestId = resourceRequest.getId(context);
            ObjectEditUtil.modify(context, resourceRequestId, attrMap, false);
            return resourceRequestId;
        } catch (Exception var9) {
            throw new FoundationException(var9);
        }
    }

    private static boolean isCompressed(byte[] var0) {
        return var0[0] == 31 && var0[1] == -117;
    }

    private static String unzip(String var0) {
        Base64.Decoder var1 = Base64.getDecoder();
        byte[] var2 = var1.decode(var0);
        if (!isCompressed(var2)) {
            return var0;
        } else {
            try {
                ByteArrayInputStream var3 = new ByteArrayInputStream(var2);
                Throwable var4 = null;

                try {
                    GZIPInputStream var5 = new GZIPInputStream(var3);
                    Throwable var6 = null;

                    try {
                        InputStreamReader var7 = new InputStreamReader(var5, StandardCharsets.UTF_8);
                        Throwable var8 = null;

                        try {
                            BufferedReader var9 = new BufferedReader(var7);
                            Throwable var10 = null;

                            try {
                                StringBuilder var11 = new StringBuilder();

                                String var12;
                                while ((var12 = var9.readLine()) != null) {
                                    var11.append(var12);
                                }

                                String var13 = var11.toString();
                                return var13;
                            } catch (Throwable var89) {
                                var10 = var89;
                                throw var89;
                            } finally {
                                if (var9 != null) {
                                    if (var10 != null) {
                                        try {
                                            var9.close();
                                        } catch (Throwable var88) {
                                            var10.addSuppressed(var88);
                                        }
                                    } else {
                                        var9.close();
                                    }
                                }

                            }
                        } catch (Throwable var91) {
                            var8 = var91;
                            throw var91;
                        } finally {
                            if (var7 != null) {
                                if (var8 != null) {
                                    try {
                                        var7.close();
                                    } catch (Throwable var87) {
                                        var8.addSuppressed(var87);
                                    }
                                } else {
                                    var7.close();
                                }
                            }

                        }
                    } catch (Throwable var93) {
                        var6 = var93;
                        throw var93;
                    } finally {
                        if (var5 != null) {
                            if (var6 != null) {
                                try {
                                    var5.close();
                                } catch (Throwable var86) {
                                    var6.addSuppressed(var86);
                                }
                            } else {
                                var5.close();
                            }
                        }

                    }
                } catch (Throwable var95) {
                    var4 = var95;
                    throw var95;
                } finally {
                    if (var3 != null) {
                        if (var4 != null) {
                            try {
                                var3.close();
                            } catch (Throwable var85) {
                                var4.addSuppressed(var85);
                            }
                        } else {
                            var3.close();
                        }
                    }

                }
            } catch (IOException var97) {
                throw new RuntimeException("Failed to unzip content", var97);
            }
        }
    }


    public static void modifyResourceRequest(Context context, String objectId, Map map) throws FoundationException {
        if (map != null && !map.isEmpty()) {
//            ServiceUtil.checkLicenseDPM(context);
            ObjectEditUtil.modify(context, objectId, map, false);
        }

    }

    public static void deleteResourceRequest(Context context, String var1) throws FoundationException {
//        ServiceUtil.checkLicenseDPM(context);
        ObjectEditUtil.delete(context, var1);
    }

}