package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.e6wv2.foundation.*;
import com.dassault_systemes.enovia.e6wv2.foundation.db.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.ArgMap;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMapAdapter;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.Select;
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
import java.util.*;
import java.util.zip.GZIPInputStream;

public class gscPolydataIfService implements ServiceConstants {

    public static Datacollection getPolydataIfs(Context context, String[] args) throws Exception {
        ServiceParameters serviceParameters = (ServiceParameters) JPOUtil.unpackArgs(args);
        HttpServletRequest var3 = serviceParameters.getHttpRequest();
        long var4 = System.currentTimeMillis();
//        com.dassault_systemes.enovia.dpm.ServiceUtil.checkLicenseDPM(context);
        Datacollection datacollection = serviceParameters.getDatacollection();
        List selects = serviceParameters.getSelects();
        ArgMap argMap = serviceParameters.getServiceArgs();
        String state = (String) argMap.get("state");
        String owned = (String) argMap.get("owned");
        String taskId = (String) argMap.get("taskId");
        boolean isOwned = false;
        if (owned != null && !owned.isEmpty()) {
            isOwned = Boolean.parseBoolean(owned);
        }

        Datacollection datacollection1;
        if (datacollection != null && !datacollection.getDataobjects().isEmpty()) {
            if(taskId != null && !"".equals(taskId)){
                datacollection1 = gscPolydataIf.getUserPolydataIfs(context, datacollection, false, selects, (String) null, taskId);
            }else{
                datacollection1 = gscPolydataIf.getUserPolydataIfs(context, datacollection, false, selects, (String) null, (String) null);
            }
        } else {
            datacollection1 = gscPolydataIf.getUserPolydataIfs(context, (Datacollection) null, isOwned, selects, state, taskId);
        }

        String var14 = String.format("%s%s (%d) ...\t", ">>> PROGRAM (SVC):  ", "getPolydataIfs", datacollection1.getDataobjects().size());
        FoundationUtil.debug(var14, var4);
        return datacollection1;
    }


    static StringList _getPolydataIfState() {
        StringList var0 = new StringList(6);
        var0.add("Create");
        var0.add("Requested");
        var0.add("Proposed");
        var0.add("Committed");
        var0.add("Rejected");
        return var0;
    }

    public static void getPolydataIfInfo(Context var0, Datacollection var1, List var2) throws Exception {
//        com.dassault_systemes.enovia.dpm.ServiceUtil.checkLicenseProject(var0, (HttpServletRequest) null);
        if (var2 != null && !var2.isEmpty()) {
            if (var1.getDataobjects().size() > 0) {
                ObjectUtil.print(var0, var1, (PrintData) null, var2, true);
            }

        } else {
            throw new Exception("You must provide some selectables to ProjectSpace.getProjectInfo");
        }
    }

    public static Dataobject updatePolydataIfs(Context context, String[] var1) throws Exception {
        ServiceParameters serviceParameters = (ServiceParameters) JPOUtil.unpackArgs(var1);
        Datacollection datacollection = serviceParameters.getDatacollection();
        Dataobject dataobject = (Dataobject) datacollection.getDataobjects().get(0);
        UpdateActions updateAction = dataobject.getUpdateAction();
        dataobject = updatePolydataIfs(context, serviceParameters, dataobject, updateAction);
        return dataobject;
    }

    public static Dataobject updatePolydataIfs(Context context, ServiceParameters serviceParameters, Dataobject dataobject, UpdateActions updateActions) throws Exception {
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
            // type 및 policy 정의
            String type = "gscPolydataIf";
            String policy = "gscInterfaceDeliverable";

            String taskId = DataelementMapAdapter.getDataelementValue(dataobject, "taskId");
            String name = DataelementMapAdapter.getDataelementValue(dataobject, "gscNo");

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

                // busResRqId = com.gsc.enovia.dpm.gscBusinessGoalService.createBusinessGoal(context, type, name, attrMap, policy);
                // BUS 생성 부분
                busResRqId = com.gsc.enovia.dpm.gscPolydataIfService.createPolydataIf(context, type, name, attrMap, policy);

                // RelationShip 처리
                com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busResRqId, "gscInterfaceDeliverable", taskId, false, false);

                dataobject.setId(busResRqId);

            } else if (UpdateActions.MODIFY.equals(updateActions)) {
                busResRqId = dataobject.getId();

                // RelationShip 처리
                com.gsc.util.ServiceUtil.updateConnection(context, updateActions, busResRqId, "gscInterfaceDeliverable", taskId, false, false);

                // 수정 처리
                com.gsc.enovia.dpm.ServiceUtil.addToMap(attrMap, "name", name);
                gscPolydataIfService.modifyPolydataIf(context, busResRqId, attrMap);

            } else {
                busResRqId = dataobject.getId();

                com.gsc.enovia.dpm.ServiceUtil.addToMap(attrMap, "name", name);
                gscPolydataIfService.modifyPolydataIf(context, busResRqId, attrMap);
            }

            dataobject = getObject(context, serviceParameters, dataobject.getId(), (String) null, (String) null);
            Dataobject var18 = new Dataobject();
            var18.setUpdateAction(updateActions);
        }

        return dataobject;
    }

    protected static Dataobject getObject(Context var0, ServiceParameters var1, String var2, String var3, String var4) throws Exception {
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

    public static String createPolydataIf(Context context, String type, String name, Map attrMap, String policy) throws Exception {
        try {
            //checkLicenseProjectLead(context, (HttpServletRequest)null);
            ObjectEditUtil.checkNameRules(context, name);
            String typeName = type != null ? type : "gscPolydataIf";
            String policyName = policy != null && !policy.isEmpty() ? policy : "gscInterfaceDeliverable";
            gscPolydataIf resourceRequest = new gscPolydataIf();
            resourceRequest.create(context, typeName, name, policyName, (String) null);
            String resourceRequestId = resourceRequest.getId(context);
            ObjectEditUtil.modify(context, resourceRequestId, attrMap, false);
            return resourceRequestId;
        } catch (Exception var9) {
            throw new Exception(var9);
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


    public static void modifyPolydataIf(Context context, String objectId, Map map) throws Exception {
        if (map != null && !map.isEmpty()) {
//            ServiceUtil.checkLicenseDPM(context);
            ObjectEditUtil.modify(context, objectId, map, false);
        }

    }

    public static void deletePolydataIf(Context context, String var1) throws Exception {
//        ServiceUtil.checkLicenseDPM(context);
        ObjectEditUtil.delete(context, var1);
    }

}
