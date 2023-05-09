package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.MqlUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectEditUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollection;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Dataobject;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.QueryData;
import com.matrixone.apps.common.Company;
import com.matrixone.apps.common.Person;
import com.matrixone.apps.common.util.ComponentsUtil;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.program.ProgramCentralConstants;
import matrix.db.*;
import matrix.util.MatrixException;
import matrix.util.StringList;

import java.util.Set;
import java.util.*;

public class gscProjectBudget  extends DomainObject {
    public static final String SELECT_COMPANY_ID;
    public static final String SELECT_COMPANY_NAME;
    public static final String SELECT_HAS_PROJECTS;
    private static final String POLICY_BUDGET = "policy_FinancialItems";
    private static final String TYPE_BUDGET = "type_Budget";

    public static final String STATE_BUDGET_PLAN;
    public static final String STATE_BUDGET_REVIEW;
    public static final String STATE_BUDGET_PLANREVIEW;
    public static final String STATE_BUDGET_PLANFROZEN;

    public String createProjectBudget(Context context, String var2) throws FoundationException {
        long var3 = System.currentTimeMillis();
        String strFormat = "";
        String physicalId = null;

        try {
            Company var7 = Person.getPerson(context).getCompany(context);
            StringList var8 = new StringList();
            var8.add("physicalid");
            Map var9 = var7.getInfo(context, var8);
            String physicalid = (String)var9.get("physicalid");
            String mqlStr = " temp query bus $1 $2 $3 select $4 dump $5";
            String results = MqlUtil.mqlCommand(context, mqlStr, new String[]{TYPE_BUDGET, var2, physicalid, "physicalid", "|"});
            strFormat = String.format("%s%s (%s) query ...\t", ">>> PROGRAM (OBJ):  ", "createProgram", var2);
            FoundationUtil.debug(strFormat, var3);
            if (results != null && !results.isEmpty()) {
                physicalId = (String) FrameworkUtil.split(results, "|").get(3);
            } else {
                this.createObject(context, TYPE_BUDGET, var2, physicalid, getDefaultPolicy(context), context.getVault().getName());
                strFormat = String.format("%s%s (%s) create ...\t", ">>> BUDGET (OBJ):  ", "createBudget", var2);
                FoundationUtil.debug(strFormat, var3);
                physicalId = this.getInfo(context, "physicalid");
                DomainRelationship.connect(context, var7, DomainConstants.RELATIONSHIP_COMPANY_PROGRAM, this);
            }

            strFormat = String.format("%s%s (%s) done ...\t", ">>> PROGRAM (OBJ):  ", "createProgram", var2);
            FoundationUtil.debug(strFormat, var3);
            return physicalId;
        } catch (Exception var13) {
            throw new FoundationException(var13);
        }
    }
    public static String getDefaultPolicy(Context var0) {
        return com.matrixone.apps.domain.util.PropertyUtil.getSchemaProperty("policy_Budget");
    }

    protected static Datacollection getUserProjectBudgets(Context context, Datacollection datacollection1, boolean isOwner, List selects, String var4) throws FoundationException {
        boolean hasData = false;
        String typeName = PropertyUtil.getSchemaName(context, "type_Budget");
        if (datacollection1 != null && !datacollection1.getDataobjects().isEmpty()) {
            hasData = true;
        }

        if (hasData && (selects == null || selects.isEmpty())) {
            throw new FoundationException("You must provide some selectables to getUserProjectBudgets");
        } else {
            Datacollection datacollection = new Datacollection();
            if (hasData) {
                try {
                    ServiceUtil.getObjectInfo(context, datacollection1, selects);
                    List var8 = datacollection1.getDataobjects();
                    Iterator var9 = var8.iterator();

                    while(var9.hasNext()) {
                        Dataobject dataobject = (Dataobject)var9.next();
                        if (dataobject.getId().isEmpty()) {
                            throw new FoundationException("DPM100: Invalid Program ID. Business Object Does Not Exist");
                        }

                        datacollection.getDataobjects().add(dataobject);
                    }
                } catch (Exception var11) {
                    if (var11.getMessage().contains("Business Object Does Not Exist")) {
                        throw new FoundationException("DPM100: Invalid Program ID. Business Object Does Not Exist");
                    }

                    throw var11;
                }
            } else {
                String whereClause = "";
                if (var4 != null && !var4.isEmpty()) {
                    whereClause = gscProjectSpace.getStateWhereClause(context, "", var4, _getBudgetStates());
                } else {
                    //whereClause = String.format("current == '%s'", "Plan Frozen");
                }

                if (isOwner) {
                    whereClause = String.format("%s AND owner == %s", whereClause, context.getUser());
                }

                QueryData queryData = new QueryData();
                queryData.setTypePattern(typeName);
                System.out.println("whereClause = " + whereClause);
                queryData.setWhereExpression(whereClause);
                queryData.setLimit(gscProjectSpace.MAX_OBJECTS);
                datacollection = ObjectUtil.query(context, queryData, selects);
                for(int i=0; i< datacollection.getDataobjects().size(); i++) {
                    Dataobject d = datacollection.getDataobjects().get(i);
                    d.getDataelements().put("gscYear", "2023");
                }
            }
            return datacollection;
        }
    }

    protected static Datacollection getBusinesUnitProjectBudgets(Context context, Datacollection var1, boolean isOwner, List var3, String var4) throws FoundationException {
        boolean var5 = false;
        String var6 = PropertyUtil.getSchemaName(context, "type_Budget");
        if (var1 != null && !var1.getDataobjects().isEmpty()) {
            var5 = true;
        }

        if (var5 && (var3 == null || var3.isEmpty())) {
            throw new FoundationException("You must provide some selectables to getUserProjectBudgets");
        } else {
            Datacollection datacollection = new Datacollection();
            if (var5) {
                try {
                    ServiceUtil.getObjectInfo(context, var1, var3);
                    List var8 = var1.getDataobjects();
                    Iterator var9 = var8.iterator();

                    while(var9.hasNext()) {
                        Dataobject var10 = (Dataobject)var9.next();
                        if (var10.getId().isEmpty()) {
                            throw new FoundationException("DPM100: Invalid Program ID. Business Object Does Not Exist");
                        }

                        datacollection.getDataobjects().add(var10);
                    }
                } catch (Exception var11) {
                    if (var11.getMessage().contains("Business Object Does Not Exist")) {
                        throw new FoundationException("DPM100: Invalid Program ID. Business Object Does Not Exist");
                    }

                    throw var11;
                }
            } else {
                String whereClause = "";
                if (var4 != null && !var4.isEmpty()) {
                    whereClause = gscProjectSpace.getStateWhereClause(context, "", var4, _getBudgetStates());
                } else {
                    whereClause = String.format("current == '%s'", "Plan Frozen");
                }

                if (isOwner) {
                    whereClause = String.format("%s AND owner == %s", whereClause, context.getUser());
                }

                QueryData queryData = new QueryData();
                queryData.setTypePattern(var6);
                System.out.println("whereClause = " + whereClause);
                queryData.setWhereExpression(whereClause);
                queryData.setLimit(gscProjectSpace.MAX_OBJECTS);
                datacollection = ObjectUtil.query(context, queryData, var3);
            }

            return datacollection;
        }
    }
    private static StringList _getBudgetStates() {
        StringList var0 = new StringList(4);
        var0.add(STATE_BUDGET_PLAN);
        var0.add(STATE_BUDGET_REVIEW);
        var0.add(STATE_BUDGET_PLANREVIEW);
        var0.add(STATE_BUDGET_PLANFROZEN);
        return var0;
    }
    public void create(Context context, String var2, String var3, String var4) throws FrameworkException {
        this.create(context, "Budget", var2, var3, var4);
    }

    public void create(Context var1, String var2, String var3, String var4, String var5) throws FrameworkException {
        this.create(var1, var2, var3, var4, var5, (Map)null, (String)null);
    }

    public void create(Context var1, String var2, String var3, String var4, String var5, Map var6, String var7) throws FrameworkException {
        this.create(var1, var2, var3, var4, var5, var6, var7, false);
    }

    public void create(Context var1, String var2, String var3, String var4, String var5, Map var6, String var7, boolean var8) throws FrameworkException {
        this.create(var1, var2, var3, var4, var5, var6, var7, var8, new BusinessInterfaceList());
    }

    public void create(Context context, String type, String name, String policy, String var5, Map attrMap, String descriptioin, boolean var8, BusinessInterfaceList businessInterfaceList) throws FrameworkException {
        try {
            ComponentsUtil.checkLicenseReserved(context, ProgramCentralConstants.PGE_PRG_LICENSE_ARRAY);
        } catch (MatrixException var21) {
            throw new FrameworkException(var21);
        }

        String revision = this.getUniqueName("");
        this.createObject(context, type, name, revision, policy, context.getVault().getName());
        if (attrMap == null) {
            attrMap = new HashMap(3);
        }

        ((Map)attrMap).put(ATTRIBUTE_ORIGINATOR, context.getUser());
        Set var23 = ((Map)attrMap).keySet();
        AttributeList var12 = new AttributeList();
        Iterator var13 = var23.iterator();

        while(var13.hasNext()) {
            String var14 = (String)var13.next();
            AttributeType var15 = new AttributeType(var14);

            try {
                var15.open(context);
                boolean var16 = var15.isMultiVal();
                if (var16) {
                    StringList var17 = (StringList)((Map)attrMap).get(var14);
                    var12.addElement(new Attribute(var15, var17));
                    var13.remove();
                }
            } catch (MatrixException var20) {
                var20.printStackTrace();
            }
        }

        String str;
        if (!var8) {
            str = this.getInfo(context, "physicalid");
            ((Map)attrMap).put(ProgramCentralConstants.ATTRIBUTE_SOURCE_ID, str);
        }

        if (businessInterfaceList != null) {
            try {
                BusinessInterfaceList businessInterfaces = this.getBusinessInterfaces(context, true);
                int i = 0;

                for(int j = businessInterfaceList.size(); i < j; ++i) {
                    BusinessInterface businessInterface = (BusinessInterface)businessInterfaceList.get(i);
                    if (!businessInterfaces.contains(businessInterface)) {
                        try {
                            this.addBusinessInterface(context, businessInterface);
                        } catch (Exception var19) {
                            System.out.println(businessInterface.getName() + " is not applicable to the type " + type);
                        }
                    }
                }
            } catch (MatrixException var22) {
                var22.printStackTrace();
            }
        }

        this.setAttributeValues(context, (Map)attrMap);
        if (var12 != null && !var12.isEmpty()) {
            try {
                this.setAttributeValues(context, var12);
            } catch (MatrixException var18) {
                var18.printStackTrace();
            }
        }

        if (descriptioin != null) {
            this.setDescription(context, descriptioin);
        }

    }

    public static void delRelatedObject(Context context, String budgetId) throws FoundationException {
        try {
            DomainObject budgetObj = new DomainObject();
            budgetObj.setId(budgetId);
            MapList relatedCostItem = budgetObj.getRelatedObjects(context, "Financial Items", "*", new StringList("physicalid"), (StringList) null, true, true, (short) 1, (String) null, "", 0);
            Iterator relatedCostItemItr = relatedCostItem.iterator();
            while(relatedCostItemItr.hasNext()) {
                ContextUtil.startTransaction(context, true);
                com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, "trigger off", true);

                Map info = (Map) relatedCostItemItr.next();
                String costItemId = info.get("physicalid").toString();
                System.out.println("cost item id >>> " + costItemId);
                gscCostItem.delRelActualTrans(context, costItemId);
                ObjectEditUtil.delete(context, costItemId);

                ContextUtil.commitTransaction(context);
                com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, "trigger on", true);
            }

        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            throw new FoundationException(e.getMessage());
        }
    }

    static {
        SELECT_COMPANY_ID = "to[" + RELATIONSHIP_COMPANY_PROGRAM + "].from.id";
        SELECT_COMPANY_NAME = "to[" + RELATIONSHIP_COMPANY_PROGRAM + "].from.name";
        SELECT_HAS_PROJECTS = "from[" + RELATIONSHIP_PROGRAM_PROJECT + "]";
        STATE_BUDGET_PLAN = com.matrixone.apps.domain.util.PropertyUtil.getSchemaProperty("policy", POLICY_BUDGET, "state_Plan");
        STATE_BUDGET_REVIEW = com.matrixone.apps.domain.util.PropertyUtil.getSchemaProperty("policy", POLICY_BUDGET, "state_Review");
        STATE_BUDGET_PLANREVIEW = com.matrixone.apps.domain.util.PropertyUtil.getSchemaProperty("policy", POLICY_BUDGET, "state_PlanReview");
        STATE_BUDGET_PLANFROZEN = com.matrixone.apps.domain.util.PropertyUtil.getSchemaProperty("policy", POLICY_BUDGET, "state_PlanFrozen");
    }
}
