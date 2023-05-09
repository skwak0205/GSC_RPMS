package com.gsc.apps.program;

import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.program.FinancialItem;
import com.matrixone.apps.program.Financials;
import com.matrixone.apps.program.*;
import matrix.db.BusinessObject;
import matrix.db.Context;
import matrix.util.StringList;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CostItem extends Financials {
    public CostItem() {
    }

    public CostItem(String var1) throws Exception {
        super(var1);
    }

    public CostItem(DecoratedOid var1) throws Exception {
        super(var1.getOID());
    }

    public CostItem(BusinessObject var1) throws Exception {
        super(var1);
    }

    public CostItemIntervalRelationship addCostItemData(Context var1, Map var2) throws FrameworkException {
        return (CostItemIntervalRelationship)this.addFinancialItemData(var1, var2, RELATIONSHIP_COST_ITEM_INTERVAL);
    }

    public void clear() throws FrameworkException {
        super.clear();
    }

    public void create(Context var1, String var2, String var3, com.matrixone.apps.program.FinancialItem var4, String var5, Map var6, boolean var7) throws FrameworkException {
        HashMap var8 = var7 ? new HashMap() : null;
        this.create(var1, var2, var3, var4, var5, var6, var8);
    }

    public void create(Context context, String var2, String var3, com.matrixone.apps.program.FinancialItem financialItem, String var5, Map var6, Map var7) throws FrameworkException {
        this.create(context, var2, (String)null, var3, financialItem, var5, var6, var7);
    }
//create(context, costItemNameArry[0], costCategory, costItemPolicy, financialItem, "", attrMap, hashMap);
    public void create(Context context, String name, String expanseOrInvestment, String costItemPolicy, com.matrixone.apps.program.FinancialItem financialItem, String var6, Map attrMap, Map hashMap) throws FrameworkException {
        try {
            ContextUtil.startTransaction(context, true);
            String uniqueName = this.getUniqueName("");
            if (ProgramCentralUtil.isNotNullString(uniqueName) && !"null".equalsIgnoreCase(expanseOrInvestment)) {
                uniqueName = uniqueName + "-" + expanseOrInvestment;
            }

            if (name == null || name.equals("")) {
                name = "CI-" + uniqueName;
            }

            this.create(context, DomainConstants.TYPE_COST_ITEM, name, uniqueName, costItemPolicy, financialItem, RELATIONSHIP_FINANCIAL_ITEMS, var6, attrMap);
            if (hashMap != null) {
                this.setupDefaultIntervals(context, hashMap);
            }

            ContextUtil.commitTransaction(context);
        } catch (Exception var10) {
            ContextUtil.abortTransaction(context);
            throw new FrameworkException(var10);
        }
    }

    public MapList getCostItemData(Context var1, StringList var2, String var3) throws FrameworkException {
        return this.getFinancialItemData(var1, RELATIONSHIP_COST_ITEM_INTERVAL, var2, var3);
    }

    public static MapList getCostItems(Context var0, com.matrixone.apps.program.FinancialItem var1, StringList var2, String var3) throws FrameworkException {
        return getFinancialItems(var0, var1, TYPE_COST_ITEM, var2, var3);
    }

    public String getDefaultPolicy(Context var1, String var2) {
        return POLICY_COST_ITEM;
    }

    public Map getParentInfo(Context var1, StringList var2) throws FrameworkException {
        return this.getParentInfoMap(var1, var2);
    }

    public static MapList getTypeAttributes(Context var0) throws FrameworkException {
        return getTypeAttributes(var0, TYPE_COST_ITEM);
    }

    public void removeCostItemData(Context var1, String var2) throws FrameworkException {
        String var3 = "print connection $1 select $2 dump";
        String var4 = MqlUtil.mqlCommand(var1, var3, new String[]{var2, "from.id"});
        if (!var4.equals(this.getId(var1))) {
            throw new FrameworkException("Error: person does not belong to this risk");
        } else {
            DomainRelationship.disconnect(var1, var2);
        }
    }

    public void rollup(Context context) throws FrameworkException {
        this.rollup(context, RELATIONSHIP_COST_ITEM_INTERVAL);
    }

    public void setCostItemData(Context context, String objectId, Map attrMap) throws FrameworkException {
        String mqlStr = "print connection $1 select $2 dump";
        String fromId = MqlUtil.mqlCommand(context, mqlStr, new String[]{objectId, "from.id"});
        if (!fromId.equals(this.getId(context))) {
            throw new FrameworkException("Error: Item Data do not belong to this Cost Item");
        } else {
            CostItemIntervalRelationship var6 = new CostItemIntervalRelationship(objectId);
            if (attrMap != null && attrMap.size() > 0) {
                var6.setAttributeValues(context, attrMap);
            }

            this.rollup(context);
        }
    }

    public void setCostItemData(Context var1, Map var2) throws FrameworkException {
        setFinancialItemData(var1, var2, this.getId(), "Error: Item Data do not belong to this Cost Item");
        this.rollup(var1);
    }

    public void setupDefaultIntervals(Context var1) throws FrameworkException {
        this.setupDefaultIntervals(var1, (Map)null);
    }

    public void setupDefaultIntervals(Context var1, Map var2) throws FrameworkException {
        this.setupDefaultIntervals(var1, var2, RELATIONSHIP_COST_ITEM_INTERVAL);
    }
//    costItem.connectCostItem(context, strProjectId,slSelectedCostItem,slSelectedCostItemName,slSelectedCostCategory);
    public void connectCostItem(Context context, String strProjectId, StringList slSelectedCostItem, StringList slSelectedCostItemName, StringList slSelectedCostCategory) throws FrameworkException {
        try {
            DomainObject projectObj = DomainObject.newInstance(context, strProjectId);
            String budgetId = this.getBudgetorBenefitCreated(context, TYPE_BUDGET, projectObj);
            com.matrixone.apps.program.FinancialItem financialItem = new FinancialItem();
            financialItem.setId(budgetId);
            String[] costItemNameArry = new String[1];
            String costItemPolicy = this.getDefaultPolicy(context, DomainObject.TYPE_COST_ITEM);
            HashMap attrMap = new HashMap();
            String baseCurrency = Currency.getBaseCurrency(context, strProjectId);
            double zero = 0.0;
            attrMap.put(ATTRIBUTE_PLANNED_COST, zero + " " + baseCurrency);
            attrMap.put(ATTRIBUTE_ESTIMATED_COST, zero + " " + baseCurrency);
            attrMap.put(ATTRIBUTE_ACTUAL_COST, zero + " " + baseCurrency);
            HashMap hashMap = new HashMap();
            StringList var16 = new StringList();
            var16.add("id");
            var16.add("name");
            MapList itemList = financialItem.getRelatedObjects(context, RELATIONSHIP_FINANCIAL_ITEMS, TYPE_COST_ITEM, var16, (StringList)null, false, true, (short)1, (String)null, "", 0);
            Map var18 = null;
            StringList costItemNames = new StringList();
            Iterator var20 = itemList.iterator();

            String costItemName;
            while(var20.hasNext()) {
                var18 = (Map)var20.next();
                costItemName = "";
                costItemName = (String)var18.get("name");
                costItemNames.add(costItemName);
            }

            for(int i = 0; i < slSelectedCostItem.size(); ++i) {
                costItemName = (String)slSelectedCostItemName.get(i);
                costItemNameArry[0] = costItemName;
                String costCategory = (String)slSelectedCostCategory.get(i);
                if (!costItemNames.contains(costItemName)) {
                    this.create(context, costItemNameArry[0], costCategory, costItemPolicy, financialItem, "", attrMap, hashMap);
                }
            }

        } catch (Exception var23) {
            throw new FrameworkException(var23);
        }
    }

    public static int maxValidTimeframe(int var0, String var1) {
        if (var1.equals("Monthly")) {
            return 12;
        } else if (var1.equals("Quarterly")) {
            return 4;
        } else {
            Calendar var2 = Calendar.getInstance();
            var2.set(var0, 0, 1);
            return var2.getMaximum(3);
        }
    }

    public static int minValidTimeframe() {
        return 1;
    }

    public static int getQuarters(int var0) {
        switch (var0) {
            case 1:
            case 2:
            case 3:
                return 1;
            case 4:
            case 5:
            case 6:
                return 2;
            case 7:
            case 8:
            case 9:
                return 3;
            case 10:
            case 11:
            case 12:
                return 4;
            default:
                throw new IllegalArgumentException("month=" + var0);
        }
    }
}
