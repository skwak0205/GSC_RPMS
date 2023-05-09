package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceBase;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceDataFunctions;
import com.dassault_systemes.enovia.e6wv2.foundation.db.JPOUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.ArgMap;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMapAdapter;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.Select;
import com.gsc.util.ServiceUtil;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.MqlUtil;
import com.matrixone.apps.program.CostItem;
import com.matrixone.apps.program.Currency;
import com.matrixone.apps.program.Financials;
import matrix.db.Context;
import matrix.util.StringList;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;

public class gscBudgetCostItemService {

    public static Datacollection getBudgetCostItems(Context context, String[] args) throws FoundationException, FrameworkException {
        Datacollection res = null;
        List budgetIds = new ArrayList();
        ServiceParameters serviceParameters = (ServiceParameters) JPOUtil.unpackArgs(args);
        HttpServletRequest httpRequest = serviceParameters.getHttpRequest();
        long var4 = System.currentTimeMillis();
        com.dassault_systemes.enovia.dpm.ServiceUtil.checkLicenseDPM(context);
        Datacollection datacollection = serviceParameters.getDatacollection();
        List parametersSelects = serviceParameters.getSelects();
        ArgMap argMap = serviceParameters.getServiceArgs();
        String state = (String) argMap.get("state");
        String owned = (String) argMap.get("owned");
        boolean isOwned = false;
        if (owned != null && !owned.isEmpty()) {
            isOwned = Boolean.parseBoolean(owned);
        }

        if (datacollection != null && !datacollection.getDataobjects().isEmpty()) {
            // Cost Item Type 별 조회
            Dataobject dataobject = (Dataobject) datacollection.getDataobjects().get(0);
            String param = dataobject.getId();

            if (param.contains("/")) {
                String[] params = param.split("/");
                String physicId = params[0];
                String costItemType = params[1];
                budgetIds.add(physicId);

                System.out.println(physicId + " >>> " + costItemType);

                if (params.length == 3 && physicId.equals("project")) {
                    budgetIds.clear();
                    physicId = costItemType;
                    costItemType = params[2];

                    DomainObject prjObj = new DomainObject();
                    prjObj.setId(physicId);
                    MapList relatedBudget = prjObj.getRelatedObjects(context, "Project Financial Item", "*", new StringList("physicalid"), (StringList) null, true, true, (short) 1, (String) null, "", 0);
                    if (!relatedBudget.isEmpty()) {
                        Iterator budgetItr = relatedBudget.iterator();
                        while(budgetItr.hasNext()) {
                            Map budgetInfo = (Map) budgetItr.next();
                            String budgetOid = budgetInfo.get("physicalid").toString();

                            System.out.println("budget oid >>> " + budgetOid);
                            budgetIds.add(budgetOid);
                        }
                    }
                }
                res = gscCostItem.getBudgetCostItems(context, parametersSelects, budgetIds, costItemType);
            } else if (param.equals("investment")) {
                // 투자 실적 조회
                res = gscCostItem.getBudgetCostItems(context, parametersSelects, null, param);
            }
        } else {
            // Cost Items 전체 조회
            res = gscCostItem.getUserCostItems(context, parametersSelects);
        }

        return res;
    }

    public static Dataobject updateBudgetCostItems(Context context, String[] args) throws Exception {
        ServiceParameters serviceParameters = (ServiceParameters) JPOUtil.unpackArgs(args);
        Datacollection datacollection = serviceParameters.getDatacollection();
        Dataobject dataobject = (Dataobject) datacollection.getDataobjects().get(0);
        UpdateActions updateAction = dataobject.getUpdateAction();
        dataobject = updateBudgetCostItems(context, serviceParameters, dataobject, updateAction);
        return dataobject;
    }

    public static Dataobject updateBudgetCostItems(Context context, ServiceParameters serviceParameters, Dataobject dataobject, UpdateActions updateActions) throws Exception {
        if (UpdateActions.DELETE.equals(updateActions)) {
            String objectId = dataobject.getId();
            String deletedCostItem = gscCostItem.deleteCostItem(context, objectId);
            dataobject.setId(deletedCostItem);
        } 
        else {
            if (UpdateActions.CREATE.equals(updateActions) || UpdateActions.MODIFY.equals(updateActions)) {
                HashMap<String, String> hashMap = new HashMap<>(5);
                ServiceDataFunctions.fillUpdates(context, dataobject, serviceParameters.getAutosaveFields(), hashMap);

                String name = DataelementMapAdapter.getDataelementValue(dataobject, "name");
                String costCenter = DataelementMapAdapter.getDataelementValue(dataobject, "ledgerAccountNumber");
                String plannedCost = DataelementMapAdapter.getDataelementValue(dataobject, "plannedCost");
                String notes = DataelementMapAdapter.getDataelementValue(dataobject, "notes");
                String parentId = DataelementMapAdapter.getDataelementValue(dataobject, "parentId");

                if (UpdateActions.CREATE.equals(updateActions)) {
                    String newCostItemOid = gscCostItem.createNewCostItem(context, parentId, name, costCenter, plannedCost, notes);
                    dataobject.setId(newCostItemOid);
                } else if (UpdateActions.MODIFY.equals(updateActions)) {
                    String costItemId = dataobject.getId();
                    String updatedCostItemOid = gscCostItem.updateCostItem(context, parentId, costItemId, name, plannedCost, notes);
                    dataobject.setId(updatedCostItemOid);
                }

            } else {
                String costItemId = DataelementMapAdapter.getDataelementValue(dataobject, "costItemId");
                String projectId = DataelementMapAdapter.getDataelementValue(dataobject, "projectId");
                String budgetId = null;
                LocalDate now = LocalDate.now();
                String currentYear = String.valueOf(now.getYear());

                CostItem costItem = new CostItem(costItemId);
                Double actualCost = Double.parseDouble(costItem.getAttributeValue(context, "Actual Cost"));

                DomainObject prjObj = new DomainObject();
                prjObj.setId(projectId);

                StringList select = new StringList();
                select.add("physicalid");
                select.add("name");
                MapList relatedBudget = prjObj.getRelatedObjects(context, "Project Financial Item", "*", select, (StringList) null, true, true, (short) 1, (String) null, "", 0);
                if (!relatedBudget.isEmpty()) {
                    Iterator budgetItr = relatedBudget.iterator();
                    while(budgetItr.hasNext()) {
                        Map budgetInfo = (Map) budgetItr.next();
                        String budgetName = budgetInfo.get("name").toString();
                        String budgetOid = budgetInfo.get("physicalid").toString();

                        /*if (budgetName.equals(currentYear)) {
                            System.out.println("budget oid >>> " + budgetOid);
                            budgetId = budgetOid;
                        }*/
                        budgetId = budgetOid;
                    }
                } else {
                    // Budget 생성
                    Map attr = new HashMap();
                    String costInterval = "Monthly";

                    String timeLineIntervalFrom = "1/1/"+currentYear;
                    String timeLineIntervalTo = "12/1/"+currentYear;
                    attr.put("gscYear", currentYear);
                    String baseCurrency = Currency.getBaseCurrency(context, projectId);
                    attr.put(Financials.ATTRIBUTE_PLANNED_COST, "0.0" + " " + baseCurrency);
                    attr.put(Financials.ATTRIBUTE_COST_INTERVAL, costInterval);
                    attr.put(Financials.ATTRIBUTE_COST_INTERVAL_START_DATE, timeLineIntervalFrom);
                    attr.put(Financials.ATTRIBUTE_COST_INTERVAL_END_DATE, timeLineIntervalTo);

                    budgetId = gscProjectBudgetService.createProjectBudget(context, null, currentYear, ServiceUtil.getUniqueRevision(0, "", 0), attr, null);
                    gscProjectBudgetService.connectBudget(context, budgetId, projectId, null);
                }

                System.out.println("budget id >>> " + budgetId);
                if (UpdateActions.CONNECT.equals(updateActions)) {
                    // 과제와 투자 실적 연결
                    ServiceUtil.updateConnection(context, UpdateActions.CREATE, costItemId, "Financial Items", budgetId, false, true);

                    // Budget의 Actual Planned 업데이트
                    gscCostItem.updateBudgetCost(context, budgetId, "Actual Cost", "add", actualCost, 0.0);
                }  else {
                    MqlUtil.mqlCommand(context, String.format("disconnect bus %s rel 'Financial Items' to %s", budgetId, costItemId));
                    gscCostItem.updateBudgetCost(context, budgetId, "Actual Cost", "subtract", actualCost, 0.0);
                }

                dataobject = getObject(context, serviceParameters, costItemId, (String) null, (String) null);
            }
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
}
