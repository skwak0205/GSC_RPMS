package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectEditUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollection;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Dataobject;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.QueryData;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMap;
import com.gsc.util.ServiceUtil;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.program.CostItem;
import com.matrixone.apps.program.FinancialItem;
import com.matrixone.apps.program.FinancialTemplateCategory;
import matrix.db.Context;
import matrix.util.StringList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class gscCostItem {
    public static String TYPE_BUDGET = PropertyUtil.getSchemaProperty("type_Budget");

    public static Datacollection getUserCostItems(Context context, List<Selectable> selectables) {
        Datacollection res = null;

        try {
            QueryData qData = new QueryData();
            qData.setTypePattern(DomainConstants.TYPE_COST_ITEM);
            res = ObjectUtil.query(context, qData, selectables);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public static Datacollection getBudgetCostItems(Context context, List<Selectable> selectables, List<String> budgetPhysicIds, String costItemType) {
        Datacollection res = null;
        Datacollection newResult = new Datacollection();
        try {
            if (budgetPhysicIds != null) { // 특정 Budget에 연결된 Cost Item 조회
                for (String budgetId : budgetPhysicIds) {
                    QueryData qData = new QueryData();
                    qData.setTypePattern(DomainConstants.TYPE_COST_ITEM);
                    StringBuffer sb = new StringBuffer();
                    sb.append(String.format("to[Financial Items].from.physicalid == %s", budgetId));
                    qData.setWhereExpression(sb.toString());
                    res = ObjectUtil.query(context, qData, selectables);

                    if (costItemType != null) {
                        List<Dataobject> costItemList = res.getDataobjects();

                        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
                        System.out.println("total item >>> " + costItemList.size());
                        System.out.println("cost Item type >>> " + costItemType);

                        for (int i = 0; i < costItemList.size(); i++) {
                            Dataobject costItem = costItemList.get(i);
                            DataelementMap costItemInfo = costItem.getDataelements();

                            String parentCategory = costItemInfo.get("revision").toString().split("-")[1].trim();
                            System.out.println("parentCategory >>> " + parentCategory);
                            String topCategoryName = "";

                            if (parentCategory.equals("Investment") || parentCategory.equals("Expense")) {
                                topCategoryName = parentCategory.toLowerCase();

                            } else {
                                if (parentCategory.substring(0, 1).matches("^[A-Z]*$")) {
                                    parentCategory = parentCategory.substring(0, 1);
                                    System.out.println("new parent category >>> " + parentCategory);
                                }

                                String mql = String.format("temp query bus 'Financial Cost Category' * * where 'name==\"%s\"' select id dump |", parentCategory);
                                System.out.println("mql >>> " + mql);
                                String data = MqlUtil.mqlCommand(context, mql);
                                StringList list = FrameworkUtil.split(data, "\n");
                                String categoryOid = "";
                                for (int x = 0; x < list.size(); x++) {
                                    String row = list.get(x);
                                    StringList items = FrameworkUtil.split(row, "|");
                                    categoryOid = items.get(3);
                                }
                                topCategoryName = MqlUtil.mqlCommand(context, String.format("print bus %s select to[Financial Sub Categories].from.name dump |", categoryOid)).toLowerCase();
                            }

                            if (topCategoryName.equals(costItemType)) {
                                System.out.println("num >>> " + i);
                                System.out.println("top category name >>>> " + topCategoryName.toLowerCase());
                                System.out.println("cost item >>>> " + costItem.getId());
                                System.out.println();

                                costItem.setDataelements(costItemInfo);
                                newResult.getDataobjects().add(costItem);

                            } else {
                                System.out.println("num >>> " + i);
                                System.out.println("remove category name >>>> " + topCategoryName.toLowerCase());
                                System.out.println("remove cost item >>>> " + costItem.getId());
                                System.out.println();
                            }
                        }
                        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
                        res = newResult;
                    }
                }

            } else { // investment 하위의 모든 cost item 조회 (이미 Budget과 연결된 Cost Item 제외)
                QueryData qData = new QueryData();
                qData.setTypePattern(DomainConstants.TYPE_COST_ITEM);
                qData.setWhereExpression("to[Financial Items] == false");
                res = ObjectUtil.query(context, qData, selectables);
                List<Dataobject> costItemList = res.getDataobjects();
                for (Dataobject costItem : costItemList) {
                    DataelementMap costItemData = costItem.getDataelements();
                    String revision = costItemData.get("revision").toString().split("-")[1];
                    if (revision.equals("Investment")) {
                        newResult.getDataobjects().add(costItem);
                    }
                }
                res = newResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public static String createNewCostItem(Context context, String parentId, String name,  String costCenter, String plannedCost, String notes) {
        String costItemPhysicId = null;

        try {
            ContextUtil.startTransaction(context, true);
            CostItem newItem = new CostItem();

            String[] costItemNameArry = new String[1];
            String costItemPolicy = newItem.getDefaultPolicy(context, DomainObject.TYPE_COST_ITEM);
            HashMap attrMap = new HashMap();
            double zero = 0.0;
            attrMap.put(CostItem.ATTRIBUTE_PLANNED_COST, zero + " " + "Won");
            attrMap.put(CostItem.ATTRIBUTE_ESTIMATED_COST, zero + " " + "Won");
            attrMap.put(CostItem.ATTRIBUTE_ACTUAL_COST, zero + " " + "Won");
            attrMap.put(CostItem.ATTRIBUTE_LEDGER_ACCOUNT_NUMBER, costCenter);
            attrMap.put(CostItem.ATTRIBUTE_NOTES, notes);

            HashMap hashMap = new HashMap();
            Map info = null;
            StringList costItemNames = new StringList();

            String costItemName;
            Iterator itemItr = getCostItemInfos(context, parentId);
            while (itemItr.hasNext()) {
                info = (Map) itemItr.next();
                costItemName = (String) info.get("name");
                costItemNames.add(costItemName);
            }

            costItemName = name;
            costItemNameArry[0] = name;

            String parentCategoryName = getParentCategoryName(context, name);
            FinancialItem financialItem = new FinancialItem(parentId);
            if (parentCategoryName != null && !costItemNames.contains(costItemName)) {
                newItem.create(context, costItemNameArry[0], parentCategoryName, costItemPolicy, financialItem, "", attrMap, hashMap);

                costItemPhysicId = newItem.getPhysicalId(context);
                attrMap.put(CostItem.ATTRIBUTE_PLANNED_COST, plannedCost + " " + "Won");
                ObjectEditUtil.modify(context, costItemPhysicId, attrMap, false);
                
                //Budget Planned Cost 업데이트
                updateBudgetCost(context, parentId, "Planned Cost", "add", Double.parseDouble(plannedCost), 0.0);

                ContextUtil.commitTransaction(context);
            } else if (costItemNames.contains(costItemName)) {
                throw new FoundationException("Error: 이미 추가된 category 입니다.");
            }

        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            e.printStackTrace();
        }

        return costItemPhysicId;
    }

    public static String updateCostItem(Context context, String parentId, String costItemId, String name, String plannedCost, String notes) throws FoundationException {
        String costItemOid = null;
        try {
            ContextUtil.startTransaction(context, true);

            CostItem costItem = new CostItem();
            Iterator itemItr = getCostItemInfos(context, parentId);

            while (itemItr.hasNext()) {
                Map costItemInfo = (Map) itemItr.next();
                costItemOid = costItemInfo.get("physicalid").toString();
                String costItemName = costItemInfo.get("name").toString();

                if (costItemOid.equals(costItemId)) {
                    costItem.setId(costItemId);

                    if (!costItemName.equals(name)) {
                        String newRevision = ServiceUtil.getUniqueRevision(0, "", 0) + "-" + getParentCategoryName(context, name);
                        MqlUtil.mqlCommand(context, String.format("mod bus %s name '%s' revision '%s'", costItemId, name, newRevision));
                    }

                    Map attrMap = costItem.getAttributeMap(context);
                    if (!attrMap.get("Planned Cost").equals(plannedCost)) {
                        Double oldCostItemPlannedCost = Double.parseDouble(attrMap.get("Planned Cost").toString());
                        attrMap.put(CostItem.ATTRIBUTE_PLANNED_COST, plannedCost + " " + "Won");

                        // Budget의 Planned cost 업데이트
                        updateBudgetCost(context, parentId, "Planned Cost", "update", Double.parseDouble(plannedCost), oldCostItemPlannedCost);
                    }

                    if (!attrMap.get("Notes").equals(notes)) {
                        attrMap.put(CostItem.ATTRIBUTE_NOTES, notes);
                    }

                    costItem.setAttributeValues(context, attrMap);
                }
            }

            //ObjectEditUtil.modify(context, costItemOid, attrMap, false);
            ContextUtil.commitTransaction(context);
        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            e.printStackTrace();
            throw new FoundationException(e.getMessage());
        }

        return costItemOid;
    }

    public static String deleteCostItem(Context context, String costItemOid) throws FoundationException {
        String deletedCostItemOid = null;

        try {
            ContextUtil.startTransaction(context, true);
            System.out.println("cost Item oid >>> " + costItemOid);
            CostItem costItem = new CostItem(costItemOid);
            String plannedCost = costItem.getInfo(context, "attribute[Planned Cost].value");

            StringList select = new StringList();
            select.add("id");
            select.add("attribute[Planned Cost].value");

            MapList budgetList = costItem.getRelatedObjects(context, "Financial Items", "*", select, (StringList) null, true, true, (short) 1, (String) null, "", 0);
            if (budgetList.size() > 0) {
                Iterator budgetItr = budgetList.iterator();
                while(budgetItr.hasNext()) {
                    Map budget = (Map) budgetItr.next();
                    String budgetOid = budget.get("id").toString();
                    updateBudgetCost(context, budgetOid, "Planned Cost", "subtract", Double.parseDouble(plannedCost), 0.0);
                }
            }
            DomainObject.deleteObjects(context, new String[]{costItemOid});
            ContextUtil.commitTransaction(context);
            deletedCostItemOid = costItemOid;

        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            e.printStackTrace();
            throw new FoundationException(e.getMessage());
        }

        return deletedCostItemOid;
    }

    public static void updateBudgetCost(Context context, String budgetId, String attrType, String action, Double costItemCost, Double costItemOldCost) {
        try {
            ContextUtil.startTransaction(context, true);

            FinancialItem budgetObj = new FinancialItem(budgetId);
            Double oldCost = Double.parseDouble(budgetObj.getAttributeValue(context, attrType));

            Double newCost = 0.0;
            switch (action) {
                case "add":
                    newCost = oldCost + costItemCost;
                    break;

                case "update":
                    newCost = (oldCost - costItemOldCost) + costItemCost;
                    break;

                case "subtract":
                    newCost = oldCost - costItemCost;
                    break;
            }

            budgetObj.setAttributeValue(context, attrType, newCost + " " + "Won");
            ContextUtil.commitTransaction(context);
        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            e.printStackTrace();
        }
    }

    public static void delRelActualTrans(Context context, String costItemId) throws FoundationException {
        try {

            CostItem costItem = new CostItem(costItemId);
            MapList relatedActualTrans = costItem.getRelatedObjects(context, "Actual Transaction Item", "*", new StringList("physicalid"), (StringList) null, true, true, (short) 1, (String) null, "", 0);
            Iterator relatedActualTranItr = relatedActualTrans.iterator();
            while(relatedActualTranItr.hasNext()) {
                ContextUtil.startTransaction(context, true);
                MqlUtil.mqlCommand(context, "trigger off", true);

                Map info = (Map) relatedActualTranItr.next();
                String actualTranId = info.get("physicalid").toString();
                System.out.println("actual trans id >>> " + actualTranId);
                ObjectEditUtil.delete(context, actualTranId);
                //com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, String.format("disconnect bus '%s' rel 'Financial Items' to '%s'", budgetId, costItemId));

                ContextUtil.commitTransaction(context);
                MqlUtil.mqlCommand(context, "trigger on", true);
            }
        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            throw new FoundationException(e.getMessage());
        }
    }

    private static Iterator getCostItemInfos(Context context, String budgetId) throws Exception {
        //FinancialItem financialItem = setFinancialItem(context, prjId, costItem);
        FinancialItem financialItem = new FinancialItem(budgetId);
        StringList select = new StringList();
        select.add("physicalid");
        select.add("name");
        MapList itemList = financialItem.getRelatedObjects(context, CostItem.RELATIONSHIP_FINANCIAL_ITEMS, CostItem.TYPE_COST_ITEM, select, (StringList) null, false, true, (short) 1, (String) null, "", 0);
        Iterator itemItr = itemList.iterator();

        return itemItr;
    }

    private static String getParentCategoryName(Context context, String childName) throws Exception {
        String name = null;

        String childOid = ServiceUtil.getObjectInfo(context, "Financial Cost Category", "name", childName, null).get("id").toString();
        DomainObject childObj = new DomainObject(childOid);
        StringList select = new StringList();
        select.add("id");
        select.add("name");

        MapList childRelList = childObj.getRelatedObjects(context, FinancialTemplateCategory.RELATIONSHIP_FINANCIAL_SUB_CATEGORIES, "*", select, (StringList) null, true, true, (short) 1, (String) null, "", 0);

        Iterator childRelItr = childRelList.iterator();
        while (childRelItr.hasNext()) {
            Map childRelInfo = (Map) childRelItr.next();
            name = childRelInfo.get("name").toString();
        }

        return name;
    }
}
