package com.gsc.apps.program;

import com.matrixone.apps.domain.DomainAccess;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.program.*;
import com.matrixone.apps.program.fiscal.CalendarType;
import com.matrixone.apps.program.fiscal.Helper;
import com.matrixone.apps.program.fiscal.Interval;
import com.matrixone.apps.program.fiscal.IntervalType;
import matrix.db.BusinessObject;
import matrix.db.Context;
import matrix.util.MatrixException;
import matrix.util.StringList;

import java.text.DateFormat;
import java.util.*;

public abstract class Financials extends DomainObject implements ProgramCentralConstants {
    public static final String SELECT_ACTUAL_COST;
    public static final String AUTONAME_TYPE_BUDGET = "type_Budget";
    public static final String AUTONAME_POLICY_FINANCIALITEM = "policy_FinancialItems";
    public static final String SELECT_ATTRIBUTE_TASK_ESTIMATED_START_DATE;
    public static final String SELECT_ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE;
    public static final String SELECT_BUDGET_ID;
    public static final String SELECT_FINANCIAL_PROJECT_ID;

    public Financials() {
    }

    public Financials(String var1) throws Exception {
        super(var1);
    }

    public Financials(DecoratedOid var1) throws Exception {
        super(var1.getOID());
    }

    public Financials(BusinessObject var1) throws Exception {
        super(var1);
    }

    protected void create(Context context, String type, String name, String rev, String policy, DomainObject domainObject, String var7, String var8, Map var9) throws FrameworkException {
        try {
            ContextUtil.startTransaction(context, true);
            String projectAccessListSelectStr = null;
            if (domainObject instanceof ProjectSpace) {
                projectAccessListSelectStr = gscProjectSpace.SELECT_PROJECT_ACCESS_LIST_ID;
            } else {
                projectAccessListSelectStr = FinancialItem.SELECT_PROJECT_ACCESS_KEY_ID;
            }

            StringList var11 = new StringList(2);
            var11.add("vault");
            var11.add(projectAccessListSelectStr);
            Map var12 = domainObject.getInfo(context, var11);
            String vault = (String)var12.get("vault");
            String projectAccessListStr = (String)var12.get(projectAccessListSelectStr);
            if (domainObject.checkAccess(context, (short)22)) {
                this.createAndConnect(context, type, name, rev, policy, context.getVault().getName(), var7, domainObject, true);
            } else {
                this.createObject(context, type, name, rev, policy, context.getVault().getName());
                ContextUtil.pushContext(context);

                try {
                    String[] var15 = new String[]{this.getObjectId()};
                    DomainRelationship.connect(context, domainObject, var7, true, var15, true);
                } finally {
                    ContextUtil.popContext(context);
                }
            }

            if (projectAccessListStr != null && !projectAccessListStr.equals("")) {
                DomainRelationship.connect(context, DomainObject.newInstance(context, projectAccessListStr), RELATIONSHIP_PROJECT_ACCESS_KEY, this);
            }

            this.setDescription(context, var8);
            this.setAttributeValues(context, var9);
            ContextUtil.commitTransaction(context);
        } catch (Exception var20) {
            ContextUtil.abortTransaction(context);
            throw new FrameworkException(var20);
        }
    }

    public static ArrayList getMonthlyDateList(Date var0, Date var1) throws Exception {
        return getDateList(IntervalType.MONTHLY, var0, var1);
    }

    public static int getFiscalWeekNumber(Date var0) {
        return getFiscal(IntervalType.WEEKLY, var0);
    }

    public static int getFiscalMonthNumber(Date var0) {
        return getFiscal(IntervalType.MONTHLY, var0);
    }

    public static int getFiscalQuarterNumber(Date var0) {
        return getFiscal(IntervalType.QUARTERLY, var0);
    }

    public static int getFiscalYear(Date var0) {
        return getFiscal(IntervalType.YEARLY, var0);
    }

    public static ArrayList getWeeklyDateList(Date var0, Date var1) throws Exception {
        return getDateList(IntervalType.WEEKLY, var0, var1);
    }

    public static ArrayList getQuarterlyDateList(Date var0, Date var1) throws Exception {
        return getDateList(IntervalType.QUARTERLY, var0, var1);
    }

    public static ArrayList getAnnualDateList(Date var0, Date var1) throws Exception {
        return getDateList(IntervalType.YEARLY, var0, var1);
    }

    public static Date getFiscalMonthIntervalStartDate(Date var0) throws Exception {
        return getFiscalIntervalStartDate(IntervalType.MONTHLY, var0);
    }

    public static Date getFiscalQuarterIntervalStartDate(Date var0) throws Exception {
        return getFiscalIntervalStartDate(IntervalType.QUARTERLY, var0);
    }

    public static Date getFiscalWeekIntervalStartDate(Date var0) throws Exception {
        return getFiscalIntervalStartDate(IntervalType.WEEKLY, var0);
    }

    public static Date getFiscalAnnualIntervalStartDate(Date var0) throws Exception {
        return getFiscalIntervalStartDate(IntervalType.YEARLY, var0);
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

    public static void cloneStructure(Context var0, DomainObject var1, DomainObject var2) throws Exception {
        String var3 = "from[" + RELATIONSHIP_FINANCIAL_ITEMS + "].to.id";
        String var4 = "from[" + RELATIONSHIP_FINANCIAL_ITEMS + "].to.name";
        String var5 = "to[" + RELATIONSHIP_PROJECT_ACCESS_LIST + "].from.id";
        String var6 = "attribute[" + ATTRIBUTE_COST_INTERVAL + "]";
        StringList var7 = new StringList();
        var7.add("id");
        var7.add("name");
        var7.add(var6);
        var7.add(var3);
        var7.add(var4);
        StringList var8 = new StringList();
        String var9 = "";
        String var10 = RELATIONSHIP_PROJECT_FINANCIAL_ITEM;
        String var11 = TYPE_BUDGET;
        MapList var12 = var2.getRelatedObjects(var0, var10, var11, var7, var8, false, true, (short)1, var9, "", 0);
        if (var12.size() == 0 || null == var12) {
            MapList var13 = var1.getRelatedObjects(var0, var10, var11, var7, var8, false, true, (short)1, var9, "", 0);
            Map var14 = null;
            String var15 = null;
            String var16 = null;
            StringList var17 = new StringList();
            var17.add(SELECT_ATTRIBUTE_TASK_ESTIMATED_START_DATE);
            var17.add(SELECT_ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE);
            Map var18 = var2.getInfo(var0, var17);
            Iterator var19 = var13.iterator();

            while(var19.hasNext()) {
                var14 = (Map)var19.next();
                String var20 = (String)var14.get("id");
                String var21 = (String)var14.get("name");
                String var22 = (String)var14.get(var6);
                String var23 = null;
                if ("Project Phase".equals(var22)) {
                    var23 = TYPE_PHASE;
                } else {
                    var23 = TYPE_TASK_MANAGEMENT;
                }

                StringList var24 = new StringList();
                var24.add(SELECT_ATTRIBUTE_TASK_ESTIMATED_START_DATE);
                var24.add(SELECT_ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE);
                String var25 = "id";
                var24.add(var25);
                MapList var26 = var2.getRelatedObjects(var0, RELATIONSHIP_SUBTASK, var23, var24, (StringList)null, false, true, (short)1, (String)null, "", 0);
                Map var27 = getPhaseMinMaxDates(var26);
                new Date();
                new Date();
                Date var28 = (Date)var27.get("calPhaseStartDate");
                Date var29 = (Date)var27.get("calPhaseEndDate");
                if (null == var28 || "null".equals(var28)) {
                    var28 = new Date((String)var18.get(SELECT_ATTRIBUTE_TASK_ESTIMATED_START_DATE));
                }

                if (null == var29 || "null".equals(var29)) {
                    var29 = new Date((String)var18.get(SELECT_ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE));
                }

                Locale var30 = var0.getLocale();
                DateFormat var31 = DateFormat.getDateInstance(eMatrixDateFormat.getEMatrixDisplayDateFormat(), var30);
                var15 = var31.format(var28);
                var16 = var31.format(var29);
                var15 = eMatrixDateFormat.getFormattedInputDate(var0, var15, -5.5, var30);
                var16 = eMatrixDateFormat.getFormattedInputDate(var0, var16, -5.5, var30);
                DomainObject var32 = DomainObject.newInstance(var0, var20);
                String var33 = "";
                String var34 = "";
                var34 = var32.getUniqueName("");
                if (null != var21 && !"null".equals(var21) && !"".equals(var21)) {
                    var33 = var21;
                } else {
                    var33 = FrameworkUtil.autoName(var0, "type_Budget", (String)null, "policy_FinancialItems", (String)null, (String)null, true, false);
                }

                BusinessObject var35 = var32.clone(var0, var33, var34, var0.getVault().getName());
                DomainObject var36 = DomainObject.newInstance(var0, var35);
                String var37 = var36.getObjectId(var0);
                String var38 = PersonUtil.getPersonObjectID(var0);
                String var39 = DomainObject.newInstance(var0, var38).getInfo(var0, "name") + "_PRJ";
                String var40 = "";
                String var41 = "Multiple Ownership For Object";
                DomainAccess.clearMultipleOwnership(var0, var37);
                if (!DomainAccess.hasObjectOwnership(var0, var37, var40, var39, var41)) {
                    DomainAccess.createObjectOwnership(var0, var37, var38, "Project Lead", "Multiple Ownership For Object");
                }

                HashMap var42 = new HashMap();
                var42.put(ATTRIBUTE_COST_INTERVAL_START_DATE, var15);
                var42.put(ATTRIBUTE_COST_INTERVAL_END_DATE, var16);
                var42.put(ATTRIBUTE_PLANNED_COST, "0");
                var42.put(ATTRIBUTE_ESTIMATED_COST, "0");
                var42.put(ATTRIBUTE_ACTUAL_COST, "0");
                var36.setAttributeValues(var0, var42);
                String var43 = var2.getInfo(var0, var5);
                DomainRelationship.connect(var0, var2, RELATIONSHIP_PROJECT_FINANCIAL_ITEM, var36);
                DomainObject var44 = DomainObject.newInstance(var0, var43);
                String var45 = RELATIONSHIP_PROJECT_ACCESS_KEY;
                DomainRelationship.connect(var0, var44, var45, var36);
                connectClonedCostItem(var0, var13, var36, var3, var4, var43);
            }
        }

    }

    public static void connectClonedCostItem(Context var0, MapList var1, DomainObject var2, String var3, String var4, String var5) throws Exception {
        try {
            Map var6 = null;
            StringList var7 = new StringList();
            StringList var8 = new StringList();
            Iterator var9 = var1.iterator();

            while(var9.hasNext()) {
                var6 = (Map)var9.next();
                Object var10 = var6.get(var3);
                Object var11 = var6.get(var4);
                if (var10 != null) {
                    if (var10 instanceof String) {
                        var7.add((String)var10);
                        var8.add((String)var11);
                    } else {
                        if (!(var10 instanceof StringList)) {
                            throw new MatrixException("Invalid data for processing");
                        }

                        var7 = (StringList)var10;
                        var8 = (StringList)var11;
                    }
                }
            }

            for(int var22 = 0; var22 < var7.size(); ++var22) {
                String var23 = (String)var7.get(var22);
                String var24 = (String)var8.get(var22);
                DomainObject var12 = DomainObject.newInstance(var0, var23);
                String var13 = var12.getRevision(var0);
                if (!var13.isEmpty()) {
                    int var14 = var13.indexOf("-");
                    var13 = var13.substring(var14, var13.length());
                }

                String var25 = FrameworkUtil.autoRevision(var0, TYPE_COST_ITEM, var24, var12.getPolicy(var0).getName());
                var25 = var25 + var13;
                BusinessObject var15 = var12.clone(var0, var24, var25, var0.getVault().getName());
                DomainObject var16 = DomainObject.newInstance(var0, var15);
                HashMap var17 = new HashMap();
                var17.put(ATTRIBUTE_PLANNED_COST, "0");
                var17.put(ATTRIBUTE_ESTIMATED_COST, "0");
                var17.put(ATTRIBUTE_ACTUAL_COST, "0");
                var16.setAttributeValues(var0, var17);
                DomainRelationship.connect(var0, var2, RELATIONSHIP_FINANCIAL_ITEMS, var16);
                DomainObject var19 = DomainObject.newInstance(var0, var5);
                String var20 = RELATIONSHIP_PROJECT_ACCESS_KEY;
                DomainRelationship.connect(var0, var19, var20, var16);
                connectClonedCostItemInterval(var0, var12, var16, var2);
            }
        } catch (Exception var21) {
            var21.printStackTrace();
        }

    }

    public static void connectClonedCostItemInterval(Context var0, DomainObject var1, DomainObject var2, DomainObject var3) throws Exception {
        StringList var4 = new StringList();
        var4.add("id");
        var4.add("name");
        StringList var5 = new StringList();
        String var6 = "";
        StringList var7 = new StringList();
        var7.add(SELECT_COST_INTERVAL_START_DATE);
        var7.add(SELECT_COST_INTERVAL_END_DATE);
        var7.add(SELECT_COST_INTERVAL);
        Map var8 = var3.getInfo(var0, var7);
        String var9 = (String)var8.get(SELECT_COST_INTERVAL_START_DATE);
        Date var10 = new Date();
        Date var11 = new Date();
        if (null != var9 && !"null".equals(var9) && !"".equals(var9)) {
            var10 = MATRIX_DATE_FORMAT.parse(var9);
        }

        String var12 = (String)var8.get(SELECT_COST_INTERVAL_END_DATE);
        if (null != var12 && !"null".equals(var12) && !"".equals(var12)) {
            var11 = MATRIX_DATE_FORMAT.parse(var12);
        }

        String var13 = (String)var8.get(SELECT_COST_INTERVAL);
        if (var13 == null) {
            var13 = INTERVAL_MONTHLY;
        }

        ArrayList var14 = null;
        if (var13.equals("Monthly")) {
            var14 = getMonthlyDateList(var10, var11);
        } else if (var13.equals("Weekly")) {
            var14 = getWeeklyDateList(var10, var11);
        } else {
            var14 = getQuarterlyDateList(var10, var11);
        }

        HashMap var15 = new HashMap();
        MapList var16 = var1.getRelatedObjects(var0, RELATIONSHIP_COST_ITEM_INTERVAL, TYPE_INTERVAL_ITEM_DATA, var4, var5, false, true, (short)1, var6, "", 0);
        StringList var17 = new StringList();
        StringList var18 = new StringList();
        Map var19 = null;
        Iterator var20 = var16.iterator();

        while(var20.hasNext()) {
            var19 = (Map)var20.next();
            Object var21 = var19.get("id");
            Object var22 = var19.get("name");
            if (var21 != null) {
                if (var21 instanceof String) {
                    var17.add((String)var21);
                    var18.add((String)var22);
                } else {
                    if (!(var21 instanceof StringList)) {
                        throw new MatrixException("Invalid data for processing");
                    }

                    var17 = (StringList)var21;
                    var18 = (StringList)var22;
                }
            }
        }

        for(int var30 = 0; var30 < var14.size(); ++var30) {
            Date var31 = (Date)var14.get(var30);
            String var32 = MATRIX_DATE_FORMAT.format(var31);
            if (null != var32 && !"null".equals(var32) && !"".equals(var32)) {
                String var23 = (String)var17.get(0);
                String var24 = (String)var18.get(0);
                DomainObject var25 = DomainObject.newInstance(var0, var23);
                String var26 = FrameworkUtil.autoRevision(var0, TYPE_INTERVAL_ITEM_DATA, var24, var25.getPolicy(var0).getName());
                BusinessObject var27 = var25.clone(var0, var24, var26, var0.getVault().getName());
                DomainObject var28 = DomainObject.newInstance(var0, var27);
                DomainRelationship var29 = DomainRelationship.connect(var0, var2, RELATIONSHIP_COST_ITEM_INTERVAL, var28);
                var15.put(CostItemIntervalRelationship.ATTRIBUTE_INTERVAL_DATE, var32);
                var29.setAttributeValues(var0, var15);
            }
        }

    }

    public String getBudgetorBenefitCreated(Context context, String strBudgetType, DomainObject projObj) throws MatrixException {
        String budgeetId = "";

        try {
            String relName = RELATIONSHIP_PROJECT_FINANCIAL_ITEM;
            String var6 = "";
            StringList busSelects = new StringList();
            busSelects.add("id");
            busSelects.add("name");
            StringList relSelect = new StringList();
            MapList var9 = projObj.getRelatedObjects(context, relName, strBudgetType, busSelects, relSelect, false, true, (short)1, var6, "");
            Map var10 = null;

            for(Iterator var11 = var9.iterator(); var11.hasNext(); budgeetId = (String)var10.get("id")) {
                var10 = (Map)var11.next();
            }
        } catch (Exception var12) {
            var12.printStackTrace();
        }

        return budgeetId;
    }

    public static Map getPhaseMinMaxDates(MapList var0) {
        HashMap var1 = new HashMap();
        Map var2 = null;
        ArrayList var3 = new ArrayList();
        ArrayList var4 = new ArrayList();
        Iterator var5 = var0.iterator();

        while(var5.hasNext()) {
            var2 = (Map)var5.next();
            String var6 = (String)var2.get(SELECT_ATTRIBUTE_TASK_ESTIMATED_START_DATE);
            String var7 = (String)var2.get(SELECT_ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE);
            Date var8 = eMatrixDateFormat.getJavaDate(var6);
            Date var9 = eMatrixDateFormat.getJavaDate(var7);
            Calendar var10 = Calendar.getInstance();
            Calendar var11 = Calendar.getInstance();
            var10.setTime(var8);
            var11.setTime(var9);
            var10.set(10, 0);
            var10.set(12, 0);
            var10.set(13, 0);
            var10.set(14, 0);
            var11.set(10, 0);
            var11.set(12, 0);
            var11.set(13, 0);
            var11.set(14, 0);
            if (var3.size() == 0) {
                var3.add(var10);
                var4.add(var11);
            } else {
                int var12;
                Calendar var13;
                for(var12 = 0; var12 < var3.size(); ++var12) {
                    var13 = (Calendar)var3.get(var12);
                    var13.set(10, 0);
                    var13.set(12, 0);
                    var13.set(13, 0);
                    var13.set(14, 0);
                    if (var10.after(var13)) {
                        var8 = var13.getTime();
                    } else {
                        var8 = var10.getTime();
                    }
                }

                for(var12 = 0; var12 < var4.size(); ++var12) {
                    var13 = (Calendar)var4.get(var12);
                    var13.set(10, 0);
                    var13.set(12, 0);
                    var13.set(13, 0);
                    var13.set(14, 0);
                    if (var11.after(var13)) {
                        var9 = var11.getTime();
                    } else {
                        var9 = var13.getTime();
                    }
                }
            }

            var1.put("calPhaseStartDate", var8);
            var1.put("calPhaseEndDate", var9);
        }

        return var1;
    }

    public static MapList getProjectPhases(Context var0, String var1, StringList var2, StringList var3, String var4) throws MatrixException {
        ProjectSpace var5 = (ProjectSpace)DomainObject.newInstance(var0, var1, "Program");
        return var5.getRelatedObjects(var0, RELATIONSHIP_SUBTASK, ProjectSpace.TYPE_PHASE, var2, var3, false, true, (short)1, var4, "", 0);
    }

    public static String updateBudgetStartandEndDates(Context var0, String var1, String var2, DomainObject var3) throws Exception {
        String var4 = "attribute[" + ATTRIBUTE_COST_INTERVAL_START_DATE + "]";
        String var5 = "attribute[" + ATTRIBUTE_COST_INTERVAL_END_DATE + "]";
        StringList var6 = new StringList();
        var6.add("id");
        var6.add(var4);
        var6.add(var5);
        if (null != var3) {
            MapList var7 = var3.getRelatedObjects(var0, RELATIONSHIP_PROJECT_FINANCIAL_ITEM, TYPE_BUDGET, var6, (StringList)null, false, true, (short)1, (String)null, "", 0);
            String var8 = "";
            String var9 = "";
            String var10 = "";
            Iterator var11 = var7.iterator();

            while(var11.hasNext()) {
                Map var12 = (Map)var11.next();
                var8 = (String)var12.get("id");
                var9 = (String)var12.get(var4);
                var10 = (String)var12.get(var5);
                Date var13 = eMatrixDateFormat.getJavaDate(var9);
                Date var14 = eMatrixDateFormat.getJavaDate(var10);
                Date var15 = eMatrixDateFormat.getJavaDate(var1);
                Date var16 = eMatrixDateFormat.getJavaDate(var2);
                DomainObject var17 = DomainObject.newInstance(var0, var8);
                if (var13.after(var15)) {
                    var17.setAttributeValue(var0, ATTRIBUTE_COST_INTERVAL_START_DATE, var1);
                }

                if (var14.before(var16)) {
                    var17.setAttributeValue(var0, ATTRIBUTE_COST_INTERVAL_END_DATE, var2);
                }
            }
        }

        return null;
    }

    public static Map getFiscalInterval(int var0, int var1) {
        HashMap var2 = new HashMap();
        Interval var3 = null;
        Date var4 = null;
        Date var5 = null;

        for(int var6 = 0; var6 < var0; ++var6) {
            var3 = Helper.yearInterval(CalendarType.FISCAL, var1);
            if (var6 == 0) {
                var4 = var3.getStartDate();
            }

            ++var1;
        }

        var5 = var3.getEndDate();
        var2.put("fStartDate", var4);
        var2.put("fEndDate", var5);
        return var2;
    }

    public static Interval getNextFiscalInterval(Date var0, IntervalType var1) {
        return getFiscalIntervalByDirection(var0, var1, Traverse.FORWARD);
    }

    public static Interval getPreviousFiscalInterval(Date var0, IntervalType var1) {
        return getFiscalIntervalByDirection(var0, var1, Traverse.BACKWARD);
    }

    private static Interval getFiscalIntervalByDirection(Date var0, IntervalType var1, Traverse var2) {
        com.matrixone.apps.program.fiscal.Calendar var3 = Helper.getCalendar(CalendarType.FISCAL);
        com.matrixone.apps.program.fiscal.Iterator var4 = null;
        Interval[] var5 = null;
        var4 = var3.getIterator(var1);
        var5 = var4.range(var0, var0);
        Interval var6 = var5[0];
        Date var7 = null;
        Calendar var8 = Calendar.getInstance();
        if (var2.equals(Traverse.FORWARD)) {
            var7 = var6.getEndDate();
            var8.setTime(var7);
            var8.add(5, 1);
        } else if (var2.equals(Traverse.BACKWARD)) {
            var7 = var6.getStartDate();
            var8.setTime(var7);
            var8.add(5, -1);
        }

        var0 = var8.getTime();
        var5 = var4.range(var0, var0);
        return var5[0];
    }

    public static int getDays(Calendar var0, Calendar var1) throws MatrixException {
        return getDays(var0.getTime(), var1.getTime());
    }

    public static int getDays(Date var0, Date var1) throws MatrixException {
        if (null != var0 && null != var1) {
            long var2 = var0.getTime();
            long var4 = var1.getTime();
            long var6 = Math.abs(var4 - var2);
            var6 /= 1000L;
            var6 /= 60L;
            var6 /= 60L;
            var6 /= 24L;
            return Integer.parseInt(String.valueOf(var6));
        } else {
            throw new MatrixException("Date param(s) are null");
        }
    }

    private static ArrayList getDateList(IntervalType var0, Date var1, Date var2) throws Exception {
        if (var1 != null && var2 != null) {
            if (var2.before(var1)) {
                throw new Exception("Start date beyond end date");
            } else {
                ArrayList var3 = new ArrayList();
                com.matrixone.apps.program.fiscal.Calendar var4 = Helper.getCalendar(CalendarType.FISCAL);
                com.matrixone.apps.program.fiscal.Iterator var5 = var4.getIterator(var0);
                var1 = Helper.cleanTime(var1);
                var2 = Helper.cleanTime(var2);
                Interval[] var6 = var5.range(var1, var2);
                Interval[] var7 = var6;
                int var8 = var6.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    Interval var10 = var7[var9];
                    var3.add(var10.getStartDate());
                }

                return var3;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static Date getFiscalIntervalStartDate(IntervalType var0, Date var1) throws Exception {
        com.matrixone.apps.program.fiscal.Calendar var2 = Helper.getCalendar(CalendarType.FISCAL);
        com.matrixone.apps.program.fiscal.Iterator var3 = var2.getIterator(var0);
        var1 = Helper.cleanTime(var1);
        Interval[] var4 = var3.range(var1, var1);
        Date var5 = new Date();
        Interval[] var6 = var4;
        int var7 = var4.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            Interval var9 = var6[var8];
            var5 = var9.getStartDate();
        }

        return var5;
    }

    private static int getFiscal(IntervalType var0, Date var1) {
        com.matrixone.apps.program.fiscal.Calendar var2 = Helper.getCalendar(CalendarType.FISCAL);
        com.matrixone.apps.program.fiscal.Iterator var3 = var2.getIterator(var0);
        Interval[] var4 = var3.range(var1, var1);
        int var5 = 0;
        Interval[] var6 = var4;
        int var7 = var4.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            Interval var9 = var6[var8];
            if (IntervalType.YEARLY.equals(var0)) {
                var5 = var9.getYear();
            } else {
                var5 = var9.getIntervalNumber();
            }
        }

        return var5;
    }

    public static void setFinancialItemData(Context var0, Map var1, String var2, String var3) throws FrameworkException {
        if (!var1.isEmpty()) {
            Iterator var4 = var1.keySet().iterator();

            while(var4.hasNext()) {
                String var5 = (String)var4.next();
                Map var6 = (Map)var1.get(var5);
                String var7 = "print connection $1 select $2 dump";
                String var8 = MqlUtil.mqlCommand(var0, var7, new String[]{var5, "from.id"});
                if (!var8.equals(var2)) {
                    throw new FrameworkException(var3);
                }

                DomainRelationship var9 = new DomainRelationship(var5);
                if (var6 != null && var6.size() > 0) {
                    var9.setAttributeValues(var0, var6);
                }
            }
        }

    }

    protected Map getParentInfoMap(Context var1, StringList var2) throws FrameworkException {
        MapList var3 = this.getParentInfo(var1, 1, var2, RELATIONSHIP_FINANCIAL_ITEMS);
        Map var4 = null;
        if (var3.size() > 0) {
            var4 = (Map)var3.get(0);
        }

        return var4;
    }

    protected DomainRelationship addFinancialItemData(Context var1, Map var2, String var3) throws FrameworkException {
        DomainRelationship var4 = null;
        IntervalItemData var5 = new IntervalItemData();
        String var6 = this.getInfo(var1, SELECT_INTERVAL_ITEM_DATA_ID);
        if (var6 == null) {
            var4 = var5.create(var1, (String)null, var5.getDefaultPolicy(var1, (String)null), var3, this);
        } else {
            var5.setId(var6);
            var4 = DomainRelationship.connect(var1, this, var3, var5);
        }

        Object var7 = null;
        if (RELATIONSHIP_COST_ITEM_INTERVAL.equalsIgnoreCase(var3)) {
            var7 = new CostItemIntervalRelationship(var4);
        } else {
            var7 = new BenefitItemIntervalRelationship(var4);
        }

        if (var2 != null && var2.size() > 0) {
            ((DomainRelationship)var7).setAttributeValues(var1, var2);
        }

        return (DomainRelationship)var7;
    }

    protected MapList getFinancialItemData(Context var1, String var2, StringList var3, String var4) throws FrameworkException {
        MapList var5 = this.getRelatedObjects(var1, var2, "*", (StringList)null, var3, false, true, (short)1, (String)null, var4);
        return var5;
    }

    protected static MapList getFinancialItems(Context var0, FinancialItem var1, String var2, StringList var3, String var4) throws FrameworkException {
        MapList var5 = var1.getRelatedObjects(var0, RELATIONSHIP_FINANCIAL_ITEMS, var2, var3, (StringList)null, false, true, (short)1, var4, (String)null);
        return var5;
    }

    protected void rollup(Context var1, String var2) throws FrameworkException {
        boolean var3 = false;
        boolean var4 = false;
        StringList var5 = new StringList();
        String var6 = ATTRIBUTE_PLANNED_BENEFIT;
        String var7 = ATTRIBUTE_ESTIMATED_BENEFIT;
        String var8 = ATTRIBUTE_ACTUAL_BENEFIT;
        if (RELATIONSHIP_COST_ITEM_INTERVAL.equalsIgnoreCase(var2)) {
            var3 = true;
            var5.add(SELECT_PLANNED_COST);
            var5.add(SELECT_ESTIMATED_COST);
            var5.add(SELECT_ACTUAL_COST);
            var6 = ATTRIBUTE_PLANNED_COST;
            var7 = ATTRIBUTE_ESTIMATED_COST;
            var8 = ATTRIBUTE_ACTUAL_COST;
        } else {
            var4 = true;
            var5.add(SELECT_PLANNED_BENEFIT);
            var5.add(SELECT_ESTIMATED_BENEFIT);
            var5.add(SELECT_ACTUAL_BENEFIT);
        }

        MapList var9 = this.getFinancialItemData(var1, var2, var5, (String)null);
        Iterator var10 = var9.iterator();
        double var11 = 0.0;
        double var13 = 0.0;
        double var15 = 0.0;

        String var19;
        while(var10.hasNext()) {
            Map var17 = (Map)var10.next();
            String var18 = "";
            var19 = "";
            String var20 = "";
            if (RELATIONSHIP_COST_ITEM_INTERVAL.equalsIgnoreCase(var2)) {
                var18 = (String)var17.get(SELECT_PLANNED_COST);
                var19 = (String)var17.get(SELECT_ESTIMATED_COST);
                var20 = (String)var17.get(SELECT_ACTUAL_COST);
            } else {
                var18 = (String)var17.get(SELECT_PLANNED_BENEFIT);
                var19 = (String)var17.get(SELECT_ESTIMATED_BENEFIT);
                var20 = (String)var17.get(SELECT_ACTUAL_BENEFIT);
            }

            try {
                var11 += Task.parseToDouble(var18);
                var13 += Task.parseToDouble(var19);
                var15 += Task.parseToDouble(var20);
            } catch (NumberFormatException var23) {
                throw new FrameworkException(var23.getMessage());
            }
        }

        HashMap var25 = new HashMap();
        var25.put(var6, Double.toString(var11));
        var25.put(var7, Double.toString(var13));
        var25.put(var8, Double.toString(var15));
        this.setAttributeValues(var1, var25);
        var5.clear();
        var5.add("id");
        Map var26 = this.getParentInfoMap(var1, var5);
        var19 = (String)var26.get("id");

        try {
            FinancialItem var24 = new FinancialItem(var19);
            var24.rollup(var1, var3, var4);
        } catch (Exception var22) {
            throw new FrameworkException(var22);
        }
    }

    protected void setupDefaultIntervals(Context var1, Map var2, String var3) throws FrameworkException {
        StringList var4 = new StringList();
        if (RELATIONSHIP_COST_ITEM_INTERVAL.equalsIgnoreCase(var3)) {
            var4.add(SELECT_COST_INTERVAL_START_DATE);
            var4.add(FinancialItem.SELECT_COST_INTERVAL_END_DATE);
            var4.add(FinancialItem.SELECT_COST_INTERVAL);
        } else {
            var4.add(FinancialItem.SELECT_BENEFIT_INTERVAL_START_DATE);
            var4.add(FinancialItem.SELECT_BENEFIT_INTERVAL_END_DATE);
            var4.add(FinancialItem.SELECT_BENEFIT_INTERVAL);
        }

        try {
            Map var5 = this.getParentInfoMap(var1, var4);
            String var6 = "";
            String var7 = "";
            String var8 = "";
            if (RELATIONSHIP_COST_ITEM_INTERVAL.equalsIgnoreCase(var3)) {
                var6 = (String)var5.get(SELECT_COST_INTERVAL_START_DATE);
                var7 = (String)var5.get(SELECT_COST_INTERVAL_END_DATE);
                var8 = (String)var5.get(SELECT_COST_INTERVAL);
            } else {
                var6 = (String)var5.get(SELECT_BENEFIT_INTERVAL_START_DATE);
                var7 = (String)var5.get(SELECT_BENEFIT_INTERVAL_END_DATE);
                var8 = (String)var5.get(SELECT_BENEFIT_INTERVAL);
            }

            Date var9 = MATRIX_DATE_FORMAT.parse(var6);
            Date var10 = MATRIX_DATE_FORMAT.parse(var7);
            if (var8 == null) {
                var8 = INTERVAL_MONTHLY;
            }

            ArrayList var11 = null;
            if (var8.equals("Monthly")) {
                var11 = getMonthlyDateList(var9, var10);
            } else if (var8.equals("Weekly")) {
                var11 = getWeeklyDateList(var9, var10);
            } else {
                var11 = getQuarterlyDateList(var9, var10);
            }

            HashMap var12 = new HashMap();

            for(int var13 = 0; var13 < var11.size(); ++var13) {
                Date var14 = (Date)var11.get(var13);
                String var15 = MATRIX_DATE_FORMAT.format(var14);
                var12.put(ATTRIBUTE_INTERVAL_DATE, var15);
                this.addFinancialItemData(var1, var12, var3);
            }

            if (var2 != null) {
                this.rollup(var1, var3);
            }

        } catch (Exception var16) {
            throw new FrameworkException(var16);
        }
    }

    static {
        SELECT_ACTUAL_COST = getAttributeSelect(ATTRIBUTE_ACTUAL_COST);
        SELECT_ATTRIBUTE_TASK_ESTIMATED_START_DATE = "attribute[" + ATTRIBUTE_TASK_ESTIMATED_START_DATE + "]";
        SELECT_ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE = "attribute[" + ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE + "]";
        SELECT_BUDGET_ID = "from[" + RELATIONSHIP_PROJECT_FINANCIAL_ITEM + "].to.id";
        SELECT_FINANCIAL_PROJECT_ID = "to[" + RELATIONSHIP_PROJECT_FINANCIAL_ITEM + "].from.id";
    }

    public static enum Traverse {
        FORWARD,
        BACKWARD;

        private Traverse() {
        }
    }
}
