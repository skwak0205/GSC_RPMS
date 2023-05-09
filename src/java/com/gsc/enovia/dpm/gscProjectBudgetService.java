package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.e6wv2.foundation.*;
import com.dassault_systemes.enovia.e6wv2.foundation.db.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.ArgMap;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMapAdapter;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.Select;
import com.dassault_systemes.enovia.e6wv2.foundation.util.FormatUtil;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.program.Currency;
import com.matrixone.apps.program.Financials;
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

public class gscProjectBudgetService {

    public static Datacollection getProjectBudgets(Context context, String[] args) throws FoundationException {
        ServiceParameters serviceParameters = (ServiceParameters) JPOUtil.unpackArgs(args);
        HttpServletRequest var3 = serviceParameters.getHttpRequest();
        long var4 = System.currentTimeMillis();
        com.dassault_systemes.enovia.dpm.ServiceUtil.checkLicenseDPM(context);
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
            datacollection1 = gscProjectBudget.getUserProjectBudgets(context, datacollection, false, selects, (String) null);
        } else if (projectId != null && !"".equals(projectId)) {
            datacollection1 = gscProjectBudget.getUserProjectBudgets(context, datacollection, false, selects, (String) null);
        } else {
            datacollection1 = gscProjectBudget.getUserProjectBudgets(context, (Datacollection) null, isOwned, selects, state);
        }

        String var14 = String.format("%s%s (%d) ...\t", ">>> PROGRAM (SVC):  ", "getProjectBudgets", datacollection1.getDataobjects().size());
        FoundationUtil.debug(var14, var4);
        return datacollection1;
    }

    public static Dataobject updateProjectBudgets(Context context, String[] args) throws Exception {
        ServiceParameters serviceParameters = (ServiceParameters) JPOUtil.unpackArgs(args);
        Datacollection datacollection = serviceParameters.getDatacollection();
        Dataobject dataobject = (Dataobject) datacollection.getDataobjects().get(0);
        UpdateActions updateAction = dataobject.getUpdateAction();
        dataobject = updateProjectBudgets(context, serviceParameters, dataobject, updateAction);
        return dataobject;
    }

    public static Dataobject updateProjectBudgets(Context context, ServiceParameters serviceParameters, Dataobject dataobject, UpdateActions updateActions) throws Exception {
        if (UpdateActions.DELETE.equals(updateActions)) {
            String budgetPhysicId = dataobject.getId();
            gscProjectBudget.delRelatedObject(context, budgetPhysicId);
            deleteProjectBudget(context, budgetPhysicId);
            dataobject = null;
        } else {
            HashMap<String, String> hashMap = new HashMap<>(5);
            ServiceDataFunctions.fillUpdates(context, dataobject, serviceParameters.getAutosaveFields(), hashMap);
            String constraintType;
            String policy;
            String projectStartDate;
            String budgetId;
            String interfaces;
            String type = DataelementMapAdapter.getDataelementValue(dataobject, "type");
            policy = DataelementMapAdapter.getDataelementValue(dataobject, "policy");
            String revision = com.gsc.util.ServiceUtil.getUniqueRevision(0, "", 0);
            String projectPhysicId = DataelementMapAdapter.getDataelementValue(dataobject, "projectId");
            String buPhysicId = DataelementMapAdapter.getDataelementValue(dataobject, "businessUnitId");
            String gscYear = DataelementMapAdapter.getDataelementValue(dataobject, "gscYear");
            String costInterval = "Monthly";

            String timeLineIntervalFrom = "1/1/"+gscYear;
            String timeLineIntervalTo = "12/1/"+gscYear;

            hashMap.put("gscYear", gscYear);
            String baseCurrency = Currency.getBaseCurrency(context, projectPhysicId);
            hashMap.put(Financials.ATTRIBUTE_PLANNED_COST, "0.0" + " " + baseCurrency);
            hashMap.put(Financials.ATTRIBUTE_COST_INTERVAL, costInterval);
            hashMap.put(Financials.ATTRIBUTE_COST_INTERVAL_START_DATE, timeLineIntervalFrom);
            hashMap.put(Financials.ATTRIBUTE_COST_INTERVAL_END_DATE, timeLineIntervalTo);

            if (UpdateActions.CREATE.equals(updateActions)) {
                type = type != null && !type.isEmpty() ? type : dataobject.getType();
                ArgMap serviceArgs = serviceParameters.getServiceArgs();
                String triggerStatus = (String) serviceArgs.get("TriggerStatus");
                if (triggerStatus != null && triggerStatus.equalsIgnoreCase("false")) {
                    Map<String, StringList> interfacesMap = ExtensionUtil.getAutomaticInterfacesByType(context, ProgramCentralConstants.TYPE_FINANCIAL_ITEM);
                    StringList projectSpaceList = interfacesMap != null ? interfacesMap.get(ProgramCentralConstants.TYPE_FINANCIAL_ITEM) : null;
                    if (projectSpaceList != null) {
                        interfaces = projectSpaceList.join(",");
                        hashMap.put("interface", interfaces);
                    }
                }
                budgetId = createProjectBudget(context, type, gscYear, revision, hashMap, policy);
                gscProjectBudgetService.connectBudget(context, budgetId, projectPhysicId, buPhysicId);
                dataobject.setId(budgetId);

            } else {
                String budgetPhysicId = dataobject.getId();
                dataobject.setId(budgetPhysicId);
                hashMap.remove("description");
                gscProjectBudgetService.modifyProjectBudget(context, dataobject.getId(), hashMap, projectPhysicId, buPhysicId);
            }

            dataobject = getObject(context, serviceParameters, dataobject.getId(), (String) null, (String) null);
            Dataobject var18 = new Dataobject();
            var18.setUpdateAction(updateActions);
//            RelateddataMapAdapter.addRelatedData(dataobject, "performRollup", var18);
        }

        return dataobject;
    }

    public static String createProjectBudget(Context context, String type, String name, String revision, Map attrMap, String policy) throws FoundationException {
        try {
            ContextUtil.startTransaction(context, true);

            //checkLicenseProjectLead(context, (HttpServletRequest)null);
            ObjectEditUtil.checkNameRules(context, name);
            String typeName = type != null ? com.matrixone.apps.domain.util.PropertyUtil.getSchemaProperty(type) : "Budget";
            String policyName = policy != null && !policy.isEmpty() ? com.matrixone.apps.domain.util.PropertyUtil.getSchemaProperty(policy) : "Financial Items";
            com.gsc.apps.program.ProjectBudget projectBudget = new com.gsc.apps.program.ProjectBudget();
            projectBudget.create(context, typeName, name, policyName, (String) null);
            String projectBudgetId = projectBudget.getPhysicalId(context);
            //projectBudget.promote(context);

            // Revision 업데이트
            String mql = " modify bus $1 name $2 revision $3";
            com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, mql, projectBudgetId, name, revision);

            // Attribute 업데이트
            projectBudget.setAttributeValues(context, attrMap);
            //ObjectEditUtil.modify(context, projectBudgetId, attrMap, false);

            ContextUtil.commitTransaction(context);
            return projectBudgetId;
        } catch (Exception var9) {
            ContextUtil.abortTransaction(context);
            throw new FoundationException(var9);
        }
    }

    public static void modifyProjectBudget(Context context, String objectId, Map map, String prjPhysciId, String buPhysicId) throws Exception {
        if (map != null && !map.isEmpty()) {
            ServiceUtil.checkLicenseDPM(context);

            // Update Attribute
            DomainObject budgetObj = new DomainObject(objectId);
            budgetObj.setAttributeValues(context, map);

            MapList relatedPrj = budgetObj.getRelatedObjects(context,
                    "Project Financial Item",  //String relPattern
                    "Project Space", //String typePattern
                    new StringList("physicalid"),  //StringList objectSelects,
                    null,                     //StringList relationshipSelects,
                    true,                     //boolean getTo,
                    true,                     //boolean getFrom,
                    (short) 1,                 //short recurseToLevel,
                    null,                     //String objectWhere,
                    "",                       //String relationshipWhere,
                    null,                     //Pattern includeType,
                    null,                     //Pattern includeRelationship,
                    null);                    //Map includeMap

            MapList relatedBU = budgetObj.getRelatedObjects(context,
                    "gscFinancialBusinessUnit",  //String relPattern
                    "Business Unit", //String typePattern
                    new StringList("physicalid"),  //StringList objectSelects,
                    null,                     //StringList relationshipSelects,
                    true,                     //boolean getTo,
                    true,                     //boolean getFrom,
                    (short) 1,                 //short recurseToLevel,
                    null,                     //String objectWhere,
                    "",                       //String relationshipWhere,
                    null,                     //Pattern includeType,
                    null,                     //Pattern includeRelationship,
                    null);                    //Map includeMap

            String relatedPrjPhysciId = null;
            String relatedBUPhysicId = null;
            for (int i = 0; i < relatedPrj.size(); i++) {
                Map list = (Map) relatedPrj.get(i);
                relatedPrjPhysciId = list.get("physicalid").toString();
                System.out.println("related prj physic id >>> " + relatedPrjPhysciId);
            }

            for (int i = 0; i < relatedBU.size(); i++) {
                Map list = (Map) relatedBU.get(i);
                relatedBUPhysicId = list.get("physicalid").toString();
                System.out.println("related bu physic id >>> " + relatedBUPhysicId);
            }

            if(!relatedPrjPhysciId.equals(prjPhysciId)) {
                com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, String.format("disconnect bus '%s' rel 'Project Financial Item' to '%s'", relatedPrjPhysciId, objectId));
                com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, String.format("connect bus '%s' rel 'Project Financial Item' to '%s'", prjPhysciId, objectId));
            }

            if(!relatedBUPhysicId.equals(buPhysicId)) {
                com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, String.format("disconnect bus '%s' rel 'gscFinancialBusinessUnit' to '%s'", objectId, relatedBUPhysicId));
                com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, String.format("connect bus '%s' rel 'gscFinancialBusinessUnit' to '%s'", objectId, buPhysicId));
            }
        }
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

    static StringList _getProjectBudgetStates() {
        StringList var0 = new StringList(6);
        var0.add("Create");
        var0.add("Assign");
        var0.add("Active");
        var0.add("Review");
        var0.add("Complete");
        var0.add("Archive");
        return var0;
    }

    public static void getProjectBudgetInfo(Context var0, Datacollection var1, List var2) throws FoundationException {
        com.dassault_systemes.enovia.dpm.ServiceUtil.checkLicenseProject(var0, (HttpServletRequest) null);
        if (var2 != null && !var2.isEmpty()) {
            if (var1.getDataobjects().size() > 0) {
                ObjectUtil.print(var0, var1, (PrintData) null, var2, true);
            }

        } else {
            throw new FoundationException("You must provide some selectables to ProjectSpace.getProjectInfo");
        }
    }

    private static String getBudgetCode(Context context, Dataobject dataobject) throws FoundationUserException {
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

    public static Boolean connectBudget(Context context, String budgetOid, String prjOid, String businessUnitOid) throws FoundationException {
        Boolean connected;
        try {
            ContextUtil.startTransaction(context, true);
            DomainObject budgetObj = new DomainObject(budgetOid);

            if (prjOid != null) {
                // Project와 Relationship 연결
                DomainObject prjObj = DomainObject.newInstance(context, prjOid);
                DomainRelationship.connect(context, prjObj, "Project Financial Item", budgetObj);

                // Project Access List Relationship 연결
                String SELECT_ACCESS_OBJECT = "to[" + DomainConstants.RELATIONSHIP_PROJECT_ACCESS_LIST + "].from.id";
                String strProjectAccessObject = (String) prjObj.getInfo(context, SELECT_ACCESS_OBJECT);

                DomainObject accessObj = DomainObject.newInstance(context, strProjectAccessObject);
                String strRelationship = DomainConstants.RELATIONSHIP_PROJECT_ACCESS_KEY;
                DomainRelationship.connect(context, accessObj, strRelationship, budgetObj);
            }

            if (businessUnitOid != null) {
                // gscFinancialBusinessUnit Relationship 연결
                DomainObject businessUnitObj = DomainObject.newInstance(context, businessUnitOid);
                DomainRelationship.connect(context, budgetObj, "gscFinancialBusinessUnit", businessUnitObj);
            }

            ContextUtil.commitTransaction(context);
            connected = true;
        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            e.printStackTrace();
            connected = false;
            throw new FoundationException(e);
        }

        return connected;
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
            ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable> var8 = new ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable>(6);
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

    public static void deleteProjectBudget(Context context, String var1) throws FoundationException {
        ServiceUtil.checkLicenseDPM(context);
        ObjectEditUtil.delete(context, var1);
    }
}
