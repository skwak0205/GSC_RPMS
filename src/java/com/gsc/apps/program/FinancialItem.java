//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gsc.apps.program;

import com.matrixone.apps.common.DocumentHolder;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.program.Currency;
import com.matrixone.apps.program.*;
import matrix.db.BusinessObject;
import matrix.db.Context;
import matrix.db.JPO;
import matrix.util.MatrixException;
import matrix.util.StringList;

import java.util.*;

public class FinancialItem extends Financials implements DocumentHolder {
    public FinancialItem() {
    }

    public FinancialItem(String var1) throws Exception {
        super(var1);
    }

    public FinancialItem(DecoratedOid var1) throws Exception {
        super(var1.getOID());
    }

    public FinancialItem(BusinessObject var1) throws Exception {
        super(var1);
    }

    public void clear() throws FrameworkException {
        super.clear();
    }

    public void create(Context context, String var2, String var3, ProjectSpace var4, String var5, Map var6) throws FrameworkException {
        String var7 = this.getUniqueName("");
        if (var2 == null || var2.equals("")) {
            var2 = "FI-" + var7.substring(var7.length() - 6);
        }

        this.create(context, DomainConstants.TYPE_FINANCIAL_ITEM, var2, var7, var3, var4, RELATIONSHIP_PROJECT_FINANCIAL_ITEM, var5, var6);
    }

    public String getDefaultPolicy(Context var1, String var2) {
        return POLICY_FINANCIAL_ITEMS;
    }

    public String getDocumentRelationshipType() {
        return RELATIONSHIP_REFERENCE_DOCUMENT;
    }

    public static MapList getFinancialItems(Context var0, ProjectSpace var1, StringList var2) throws FrameworkException {
        MapList var3 = var1.getRelatedObjects(var0, RELATIONSHIP_PROJECT_FINANCIAL_ITEM, "*", var2, (StringList)null, false, true, (short)1, (String)null, (String)null);
        return var3;
    }

    public MapList getParentInfo(Context var1, StringList var2) throws FrameworkException {
        MapList var3 = this.getParentInfo(var1, 1, var2, RELATIONSHIP_PROJECT_FINANCIAL_ITEM);
        return var3;
    }

    public static MapList getTypeAttributes(Context var0) throws FrameworkException {
        return getTypeAttributes(var0, DomainConstants.TYPE_FINANCIAL_ITEM);
    }

    private void setFinancialItem(Context var1, String var2, StringList var3, StringList var4) throws FrameworkException {
        MapList var5 = getFinancialItems(var1, this, var2, var3, (String)null);
        Iterator var6 = var5.iterator();
        double var7 = 0.0;
        double var9 = 0.0;
        double var11 = 0.0;

        while(var6.hasNext()) {
            Map var13 = (Map)var6.next();
            String var14 = (String)var13.get(var3.get(0));
            String var15 = (String)var13.get(var3.get(1));
            String var16 = (String)var13.get(var3.get(2));

            try {
                var7 += Task.parseToDouble(var14);
                var9 += Task.parseToDouble(var15);
                var11 += Task.parseToDouble(var16);
            } catch (NumberFormatException var18) {
                throw new FrameworkException(var18.getMessage());
            }
        }

        HashMap var19 = new HashMap();
        var19.put(var4.get(0), Double.toString(var7));
        var19.put(var4.get(1), Double.toString(var9));
        var19.put(var4.get(2), Double.toString(var11));
        this.setAttributeValues(var1, var19);
    }

    public void rollup(Context var1, boolean var2, boolean var3) throws FrameworkException {
        StringList var4 = new StringList();
        StringList var5 = new StringList();
        String var6 = "";
        if (var2) {
            var6 = TYPE_COST_ITEM;
            var4.add(SELECT_PLANNED_COST);
            var4.add(SELECT_ESTIMATED_COST);
            var4.add(SELECT_ACTUAL_COST);
            var5.add(ATTRIBUTE_PLANNED_COST);
            var5.add(ATTRIBUTE_ESTIMATED_COST);
            var5.add(ATTRIBUTE_ACTUAL_COST);
        }

        if (var3) {
            var4.clear();
            var6 = TYPE_BENEFIT_ITEM;
            var4.add(SELECT_PLANNED_BENEFIT);
            var4.add(SELECT_ESTIMATED_BENEFIT);
            var4.add(SELECT_ACTUAL_BENEFIT);
            var5.add(ATTRIBUTE_PLANNED_BENEFIT);
            var5.add(ATTRIBUTE_ESTIMATED_BENEFIT);
            var5.add(ATTRIBUTE_ACTUAL_BENEFIT);
        }

        this.setFinancialItem(var1, var6, var4, var5);
        var4.clear();
        var4.add(SELECT_PLANNED_COST);
        var4.add(SELECT_ESTIMATED_COST);
        var4.add(SELECT_ACTUAL_COST);
        var4.add(SELECT_PLANNED_BENEFIT);
        var4.add(SELECT_ESTIMATED_BENEFIT);
        var4.add(SELECT_ACTUAL_BENEFIT);
        Map var7 = this.getInfo(var1, var4);
        String var8 = (String)var7.get(SELECT_PLANNED_COST);
        String var9 = (String)var7.get(SELECT_ESTIMATED_COST);
        String var10 = (String)var7.get(SELECT_ACTUAL_COST);
        String var11 = (String)var7.get(SELECT_PLANNED_BENEFIT);
        String var12 = (String)var7.get(SELECT_ESTIMATED_BENEFIT);
        String var13 = (String)var7.get(SELECT_ACTUAL_BENEFIT);
        double var14 = Task.parseToDouble(var11) - Task.parseToDouble(var8);
        double var16 = Task.parseToDouble(var12) - Task.parseToDouble(var9);
        double var18 = Task.parseToDouble(var13) - Task.parseToDouble(var10);
        HashMap var20 = new HashMap();
        var20.put(ATTRIBUTE_PLANNED_NET_BENEFIT, Double.toString(var14));
        var20.put(ATTRIBUTE_ESTIMATED_NET_BENEFIT, Double.toString(var16));
        var20.put(ATTRIBUTE_ACTUAL_NET_BENEFIT, Double.toString(var18));
        this.setAttributeValues(var1, var20);
    }

    public void delete(Context var1, String var2) throws FrameworkException {
        ContextUtil.startTransaction(var1, true);

        try {
            if (var2 != null && !"".equals(var2) && !"null".equals(var2)) {
                ContextUtil.pushContext(var1);
                DomainRelationship.disconnect(var1, var2);
                ContextUtil.popContext(var1);
                this.deleteObject(var1, true);
                ContextUtil.commitTransaction(var1);
            }

        } catch (Exception var4) {
            ContextUtil.abortTransaction(var1);
            throw new FrameworkException(var4);
        }
    }

    public static MapList getFinancialBudgetOrBenefit(Context var0, ProjectSpace var1, StringList var2, String var3) throws FrameworkException {
        MapList var4 = var1.getRelatedObjects(var0, RELATIONSHIP_PROJECT_FINANCIAL_ITEM, var3, var2, (StringList)null, false, true, (short)1, (String)null, (String)null);
        return var4;
    }

    public void updateCostInterval(Context var1, String var2, String var3) throws MatrixException {
        DomainObject var4 = null;
        StringList var5 = null;

        try {
            if (ProgramCentralUtil.isNotNullString(var3) && ProgramCentralUtil.isNotNullString(var2)) {
                var4 = DomainObject.newInstance(var1, var2);
                var5 = new StringList();
                var5.add("type");
                var5.add(Financials.SELECT_FINANCIAL_PROJECT_ID);
                Map var6 = var4.getInfo(var1, var5);
                String var7 = (String)var6.get("type");
                if (var7.equalsIgnoreCase(TYPE_BUDGET)) {
                    String var8 = (String)var6.get(Financials.SELECT_FINANCIAL_PROJECT_ID);
                    ProjectSpace var9 = new ProjectSpace(var8);
                    if (!var9.hasPhase(var1) && var3.equals("Project Phase")) {
                        String var10 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ProjectBenefitOrBudget.CostIntervalUpdateFailure", var1.getSession().getLanguage());
                        MqlUtil.mqlCommand(var1, "Error $1", new String[]{var10});
                    } else {
                        var4.setAttributeValue(var1, Financials.ATTRIBUTE_COST_INTERVAL, var3);
                    }
                } else {
                    var4.setAttributeValue(var1, Financials.ATTRIBUTE_COST_INTERVAL, var3);
                }

            } else {
                throw new MatrixException("Null argument(s)");
            }
        } catch (Exception var11) {
            throw new MatrixException(var11);
        }
    }

    public static boolean isProjectVisible(Context var0, String var1) throws MatrixException {
        boolean var2 = false;

        try {
            if (ProgramCentralUtil.isNullString(var1)) {
                throw new IllegalArgumentException("Project id argument is null.");
            } else {
                ProjectSpace var3 = (ProjectSpace)DomainObject.newInstance(var0, DomainConstants.TYPE_PROJECT_SPACE, "PROGRAM");
                var3.setId(var1);
                var2 = var3.checkAccess(var0, (short)0);
                return var2;
            }
        } catch (Exception var4) {
            throw new MatrixException(var4);
        }
    }

    public static boolean isContextAllowedForFinancialOperations(Context var0, String[] var1) throws MatrixException {
        boolean var2 = false;

        try {
            Map var3 = (Map)JPO.unpackArgs(var1);
            String var4 = (String)var3.get("objectId");
            ProjectSpace var5 = (ProjectSpace)DomainObject.newInstance(var0, TYPE_PROJECT_SPACE, "PROGRAM");
            var5.setId(var4);
            var2 = var5.checkAccess(var0, (short)1);
            return var2;
        } catch (Exception var6) {
            throw new MatrixException(var6);
        }
    }

    public static boolean isBackgroundTaskActive(Context var0, String var1) throws MatrixException {
        return isCurrencyModifyBackgroundTaskActive(var0, var1);
    }

    private static boolean isCurrencyModifyBackgroundTaskActive(Context var0, String var1) throws MatrixException {
        try {
            return ProgramCentralUtil.isBackgroundTaskActive(var0, var1, "Update Base Currency");
        } catch (Exception var3) {
            throw new MatrixException(var3);
        }
    }

    public void updateBaseCurrency(Context var1, String var2, String var3) throws MatrixException {
        try {
            String var4 = this.getId(var1);
            if (!ProgramCentralUtil.isNullString(var4) && !ProgramCentralUtil.isNullString(var2) && !ProgramCentralUtil.isNullString(var3)) {
                DomainObject var5 = DomainObject.newInstance(var1, var4);
                String var6 = "";
                String var7 = "";
                StringList var8 = new StringList();
                StringList var9 = new StringList();
                new HashMap();
                String var11 = "";
                String var12 = "";
                String var13 = "";
                boolean var14 = false;
                if (var5.isKindOf(var1, ProgramCentralConstants.TYPE_BUDGET)) {
                    var14 = true;
                    var6 = Financials.TYPE_COST_ITEM;
                    var7 = ProgramCentralConstants.RELATIONSHIP_COST_ITEM_INTERVAL + "," + ProgramCentralConstants.RELATIONSHIP_ACTUAL_TRANSACTION_ITEM;
                    var11 = ProgramCentralConstants.SELECT_PLANNED_COST;
                    var12 = ProgramCentralConstants.SELECT_ESTIMATED_COST;
                    var13 = ProgramCentralConstants.SELECT_ACTUAL_COST;
                } else if (var5.isKindOf(var1, ProgramCentralConstants.TYPE_BENEFIT)) {
                    var14 = false;
                    var6 = Financials.TYPE_BENEFIT_ITEM;
                    var7 = ProgramCentralConstants.RELATIONSHIP_BENEFIT_ITEM_INTERVAL;
                    var11 = ProgramCentralConstants.SELECT_PLANNED_BENEFIT;
                    var12 = ProgramCentralConstants.SELECT_ESTIMATED_BENEFIT;
                    var13 = ProgramCentralConstants.SELECT_ACTUAL_BENEFIT;
                }

                var8.add(var11);
                var8.add(var12);
                var8.add(var13);
                var8.add("type");
                var8.add("id");
                var8.add("name");
                Map var15 = var5.getInfo(var1, var8);
                this.updateBaseCurrency(var1, var15, var2, var3, var14);
                String var16 = Financials.RELATIONSHIP_FINANCIAL_ITEMS;
                MapList var17 = var5.getRelatedObjects(var1, var16, var6, var8, var9, false, true, (short)2, (String)null, (String)null, 0);
                Iterator var18 = var17.iterator();

                while(var18.hasNext()) {
                    Map var19 = (Map)var18.next();
                    this.updateBaseCurrency(var1, var19, var2, var3, var14);
                    String var20 = (String)var19.get("id");
                    DomainObject var21 = DomainObject.newInstance(var1, var20);
                    var9.add("id[connection]");
                    var9.add("name[connection]");
                    var9.add("type[connection]");
                    var9.add(var11);
                    var9.add(var12);
                    var9.add(var13);
                    var16 = var7;
                    if (var14) {
                        var6 = ProgramCentralConstants.TYPE_INTERVAL_ITEM_DATA + "," + ProgramCentralConstants.TYPE_ACTUAL_TRANSACTION + "," + ProgramCentralConstants.TYPE_PHASE;
                        var8.add(SELECT_TRANSACTION_AMOUNT);
                    } else {
                        var6 = ProgramCentralConstants.TYPE_INTERVAL_ITEM_DATA;
                    }

                    MapList var22 = null;

                    try {
                        ContextUtil.pushContext(var1, PropertyUtil.getSchemaProperty(var1, "person_UserAgent"), "", "");
                        var22 = var21.getRelatedObjects(var1, var16, var6, var8, var9, true, true, (short)2, (String)null, (String)null, 0);
                    } finally {
                        ContextUtil.popContext(var1);
                    }

                    Iterator var23 = var22.iterator();

                    while(var23.hasNext()) {
                        Map var24 = (Map)var23.next();
                        this.updateBaseCurrency(var1, var24, var2, var3, var14);
                    }
                }

            }
        } catch (Exception var28) {
            throw new MatrixException(var28);
        }
    }

    private void updateBaseCurrency(Context var1, Map var2, String var3, String var4, boolean var5) throws MatrixException {
        try {
            String var6 = (String)var2.get("type");
            if (ProgramCentralUtil.isNullString(var6)) {
                var6 = (String)var2.get("type[connection]");
            }

            double var7 = 0.0;
            double var9 = 0.0;
            double var11 = 0.0;
            double var13 = 0.0;
            String var15 = "";
            new Date();
            new HashMap();
            Map var17 = this.convertAttributeSelectsToAttributes(var2);
            var17 = this.updateAttributeMapBaseCurrency(var1, var17, var3, var4);
            DomainObject var18;
            if (!ProgramCentralConstants.TYPE_BUDGET.equals(var6) && !ProgramCentralConstants.TYPE_COST_ITEM.equals(var6) && !ProgramCentralConstants.TYPE_BENEFIT.equals(var6) && !ProgramCentralConstants.TYPE_BENEFIT_ITEM.equals(var6)) {
                if (ProgramCentralConstants.TYPE_ACTUAL_TRANSACTION.equals(var6)) {
                    var15 = (String)var2.get("id");
                    var18 = null;

                    try {
                        ContextUtil.pushContext(var1, PropertyUtil.getSchemaProperty(var1, "person_UserAgent"), "", "");
                        if (!var17.isEmpty()) {
                            var18 = DomainObject.newInstance(var1, var15);
                            var18.setAttributeValues(var1, var17);
                        }
                    } finally {
                        ContextUtil.popContext(var1);
                    }
                } else if (ProgramCentralConstants.TYPE_INTERVAL_ITEM_DATA.equals(var6) || ProgramCentralConstants.TYPE_PHASE.equals(var6)) {
                    var15 = (String)var2.get("id[connection]");
                    var18 = null;
                    if (!var17.isEmpty()) {
                        DomainRelationship var24 = DomainRelationship.newInstance(var1, var15);
                        var24.setAttributeValues(var1, var17);
                    }
                }
            } else {
                var15 = (String)var2.get("id");
                var18 = null;
                if (!var17.isEmpty()) {
                    var18 = DomainObject.newInstance(var1, var15);
                    var18.setAttributeValues(var1, var17);
                }
            }

        } catch (Exception var23) {
            throw new MatrixException(var23);
        }
    }

    private Map updateAttributeMapBaseCurrency(Context var1, Map var2, String var3, String var4) throws MatrixException {
        try {
            HashMap var5 = new HashMap(var2.size());
            if (!var2.isEmpty()) {
                Set var6 = var2.keySet();
                Date var7 = new Date();
                Iterator var8 = var6.iterator();

                while(var8.hasNext()) {
                    String var9 = (String)var8.next();
                    if (this.isValidAttribute(var9)) {
                        String var10 = (String)var2.get(var9);
                        if (ProgramCentralUtil.isNotNullString(var10)) {
                            double var11 = Task.parseToDouble(var10);
                            var11 = Currency.convert(var1, var11, var3, var4, var7, false);
                            var10 = String.valueOf(var11);
                            var5.put(var9, var10 + " " + var4);
                        }
                    }
                }
            }

            return var5;
        } catch (Exception var13) {
            throw new MatrixException(var13);
        }
    }

    private boolean isValidAttribute(String var1) {
        return ProgramCentralConstants.ATTRIBUTE_PLANNED_BENEFIT.equals(var1) || ProgramCentralConstants.ATTRIBUTE_ESTIMATED_BENEFIT.equals(var1) || ProgramCentralConstants.ATTRIBUTE_ACTUAL_BENEFIT.equals(var1) || ProgramCentralConstants.ATTRIBUTE_ACTUAL_COST.equals(var1) || ProgramCentralConstants.ATTRIBUTE_ESTIMATED_COST.equals(var1) || ProgramCentralConstants.ATTRIBUTE_PLANNED_COST.equals(var1) || ProgramCentralConstants.ATTRIBUTE_TRANSACTION_AMOUNT.equals(var1);
    }

    private Map convertAttributeSelectsToAttributes(Map var1) throws MatrixException {
        try {
            Set var2 = var1.keySet();
            HashMap var3 = new HashMap(var1.size());
            Iterator var4 = var2.iterator();

            while(var4.hasNext()) {
                String var5 = (String)var4.next();
                if (var5.contains("attribute[")) {
                    int var6 = var5.lastIndexOf("[");
                    ++var6;
                    int var7 = var5.lastIndexOf("]");
                    String var8 = var5.substring(var6, var7);
                    var3.put(var8, (String)var1.get(var5));
                }
            }

            return var3;
        } catch (Exception var9) {
            throw new MatrixException(var9);
        }
    }

    public static String getCurrencyModifyBackgroundTaskMessage(Context var0, boolean var1) throws MatrixException {
        try {
            String var2 = var0.getSession().getLanguage();
            String var3 = ProgramCentralUtil.getPMCI18nString(var0, "emxProgramCentral.ProjectBenefitOrBudget.BaseCurrencyModifyMessage", var2);
            if (var1) {
                MqlUtil.mqlCommand(var0, "notice $1", new String[]{var3});
            }

            return var3;
        } catch (Exception var4) {
            throw new MatrixException(var4);
        }
    }
}
