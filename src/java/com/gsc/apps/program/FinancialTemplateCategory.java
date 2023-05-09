package com.gsc.apps.program;

import com.matrixone.apps.common.Company;
import com.matrixone.apps.common.Person;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.DecoratedOid;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.PropertyUtil;
import matrix.db.BusinessObject;
import matrix.db.Context;
import matrix.util.StringList;

public class FinancialTemplateCategory extends DomainObject {
    public static final String RELATIONSHIP_COMPANY_FINANCIAL_CATEGORIES = PropertyUtil.getSchemaProperty("relationship_CompanyFinancialCategories");
    public static final String RELATIONSHIP_FINANCIAL_SUB_CATEGORIES = PropertyUtil.getSchemaProperty("relationship_FinancialSubCategories");
    public static final String POLICY_FINANCIAL_CATEGORIES = PropertyUtil.getSchemaProperty("policy_FinancialCategories");
    public static final String TYPE_FINANCIAL_BENEFIT_CATEGORY = PropertyUtil.getSchemaProperty("type_FinancialBenefitCategory");
    public static final String TYPE_FINANCIAL_COST_CATEGORY = PropertyUtil.getSchemaProperty("type_FinancialCostCategory");

    public FinancialTemplateCategory() {
    }

    public FinancialTemplateCategory(String var1) throws Exception {
        super(var1);
    }

    public FinancialTemplateCategory(BusinessObject var1) throws Exception {
        super(var1);
    }

    public FinancialTemplateCategory(DecoratedOid var1) throws Exception {
        super(var1.getOID());
    }

    public FinancialTemplateCategory createSubCategory(Context context, String var2, String var3) throws FrameworkException {
        String type = this.getInfo(context, "type");
        FinancialTemplateCategory financialTemplateCategory = new FinancialTemplateCategory();
        String uniqueName = financialTemplateCategory.getUniqueName("");
        financialTemplateCategory.createAndConnect(context, type, var2, uniqueName, var3, (String) null, RELATIONSHIP_FINANCIAL_SUB_CATEGORIES, this, true);
        return financialTemplateCategory;
    }

    public void create(Context context, String var2, String var3, String var4) throws FrameworkException {
        String uniqueName = this.getUniqueName("");
        Company company = Person.getPerson(context).getCompany(context);
        this.createAndConnect(context, var2, var3, uniqueName, var4, (String) null, RELATIONSHIP_COMPANY_FINANCIAL_CATEGORIES, company, true);
    }

    public static MapList getBenefitCategories(Context var0, int var1, StringList var2, String var3) throws FrameworkException {
        return getCategories(var0, TYPE_FINANCIAL_BENEFIT_CATEGORY, var1, var2, var3);
    }

    public static MapList getCategories(Context var0, String var1, int var2, StringList var3, String var4) throws FrameworkException {
        Company var5 = Person.getPerson(var0).getCompany(var0);
        MapList var6 = getCategories(var0, var5, var1, var2, var3, var4);
        if (var6.size() == 0) {
            String var7 = Company.getHostCompany(var0);
            var5.setId(var7);
            var6 = getCategories(var0, var5, var1, var2, var3, var4);
        }

        return var6;
    }

    protected static MapList getCategories(Context var0, DomainObject var1, String var2, int var3, StringList var4, String var5) throws FrameworkException {
        String var6 = RELATIONSHIP_COMPANY_FINANCIAL_CATEGORIES;
        if (var3 != 1) {
            var6 = var6 + "," + RELATIONSHIP_FINANCIAL_SUB_CATEGORIES;
        }

        if (var2 == null || "".equals(var2)) {
            var2 = "*";
        }

        MapList var7 = var1.getRelatedObjects(var0, var6, var2, var4, (StringList) null, false, true, (short) var3, var5, (String) null);
        return var7;
    }

    public static MapList getCostCategories(Context var0, int var1, StringList var2, String var3) throws FrameworkException {
        return getCategories(var0, TYPE_FINANCIAL_COST_CATEGORY, var1, var2, var3);
    }

    public MapList getSubCategories(Context var1, int var2, StringList var3, String var4) throws FrameworkException {
        MapList var5 = this.getRelatedObjects(var1, RELATIONSHIP_FINANCIAL_SUB_CATEGORIES, "*", var3, (StringList) null, false, true, (short) var2, var4, (String) null);
        return var5;
    }

    public String getDefaultPolicy(Context var1, String var2) {
        return POLICY_FINANCIAL_CATEGORIES;
    }
}
