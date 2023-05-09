package com.gsc.apps.program;

import com.matrixone.apps.common.Company;
import com.matrixone.apps.common.Organization;
import com.matrixone.apps.common.Person;
import com.matrixone.apps.common.VaultHolder;
import com.matrixone.apps.domain.DomainAccess;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.framework.ui.UINavigatorUtil;
import com.matrixone.apps.program.ProgramCentralConstants;
import com.matrixone.apps.program.ProgramCentralUtil;
import com.matrixone.apps.program.Question;
import com.matrixone.apps.program.QuestionHolder;
import matrix.db.BusinessObject;
import matrix.db.BusinessObjectList;
import matrix.db.Context;
import matrix.util.MatrixException;
import matrix.util.StringList;

import java.util.Iterator;
import java.util.Map;

public class ProjectTemplate extends com.matrixone.apps.program.ProjectSpace implements QuestionHolder, VaultHolder {
    public static final String SELECT_COMPANY_ID;
    public static final String SELECT_COMPANY_NAME;
    public static final String SELECT_HAS_QUESTIONS;
    public static final String STATE_PROJECT_TEMPLATE_ACTIVE;
    public static final String STATE_PROJECT_TEMPLATE_INACTIVE;
    public static final String STATE_PROJECT_TEMPLATE_DRAFT;
    public static final String STATE_PROJECT_TEMPLATE_IN_APPROVAL;

    public ProjectTemplate() {
    }

    public ProjectTemplate(String var1) throws Exception {
        super(var1);
    }

    public ProjectTemplate(BusinessObject var1) {
        super(var1);
    }

    public ProjectTemplate(DecoratedOid var1) throws Exception {
        super(var1.getOID());
    }

    /** @deprecated */
    public Question addQuestion(Context var1, String var2, String var3) throws FrameworkException {
        Question var4 = new Question();
        var4.create(var1, var2, var3, this);
        return var4;
    }

    public void clear() throws FrameworkException {
        super.clear();
    }

    public void confirmDelete(Context var1, boolean var2, BusinessObjectList var3) throws FrameworkException {
        DebugUtil.debug("Project Template Confirm delete started");
        boolean var4 = this.hasRelatedObjects(var1, RELATIONSHIP_QUESTION, true);
        if (!var2 && var4) {
            throw new FrameworkException("Error: delete request denied; Project Template has references");
        } else {
            super.confirmDelete(var1, var2, var3);
            if (var4) {
                MapList var5 = this.getQuestions(var1, (StringList)null, (String)null);
                new Question();

                String var9;
                for(Iterator var7 = var5.iterator(); var7.hasNext(); DebugUtil.debug("To delete " + var9)) {
                    Map var8 = (Map)var7.next();
                    var9 = (String)var8.get("id");

                    try {
                        var3.add(new Question(var9));
                    } catch (Exception var11) {
                        throw new FrameworkException(var11.getMessage());
                    }
                }
            }

        }
    }

    public void create(Context var1, String var2, String var3, String var4) throws FrameworkException {
        this.create(var1, TYPE_PROJECT_TEMPLATE, var2, var3, var4);
    }

    public static String getDefaultPolicy(Context var0) {
        return POLICY_PROJECT_TEMPLATE;
    }

    public static MapList getProjectTemplates(Context var0, StringList var1, String var2) throws FrameworkException {
        Company var3 = Person.getPerson(var0).getCompany(var0);
        return var3.getRelatedObjects(var0, RELATIONSHIP_COMPANY_PROJECT_TEMPLATES, TYPE_PROJECT_TEMPLATE, var1, (StringList)null, false, true, (short)1, var2, "");
    }

    /** @deprecated */
    public MapList getQuestions(Context var1, StringList var2, String var3) throws FrameworkException {
        return this.getRelatedObjects(var1, RELATIONSHIP_PROJECT_QUESTION, "*", var2, (StringList)null, false, true, (short)1, var3, "");
    }

    public static MapList getTypeAttributes(Context var0) throws FrameworkException {
        return getTypeAttributes(var0, TYPE_PROJECT_TEMPLATE);
    }

    public boolean hasAccess(Context var1, String[] var2) throws FrameworkException {
        boolean var3 = false;

        boolean var7;
        try {
            Object var4 = null;
            String var5 = Person.getPerson(var1).getCompanyId(var1);
            ContextUtil.pushContext(var1);
            var3 = true;
            String var6 = this.getInfo(var1, SELECT_COMPANY_ID);
            var7 = var5.equals(var6);
        } catch (Exception var11) {
            throw new FrameworkException(var11);
        } finally {
            if (var3) {
                ContextUtil.popContext(var1);
            }

        }

        return var7;
    }

    public void setOwner(Context var1, String var2) throws FrameworkException {
        try {
            StringList var3 = new StringList();
            var3.add("id");
            Map var4 = this.getCompany(var1, var3);
            Person var5 = new Person(PersonUtil.getPersonObjectID(var1, var2));
            Company var6 = var5.getCompany(var1);
            String var7;
            String var8;
            if (!var6.getId().equals((String)var4.get("id"))) {
                var7 = var1.getSession().getLanguage();
                var8 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ProjectTemplate.UCannotBeOwnerOfThisProjectTemplate", var7);
                throw new Exception(var8);
            } else {
                var7 = this.getInfo(var1, "owner");
                this.updatePALOwnership(var1, var7, var2);
                this.changeRelatedTypeOwnership(var1, var7, var2);
                var8 = this.getId(var1);
                DomainObject.newInstance(var1, var8).setOwner(var1, var2);
                String var9 = PersonUtil.getPersonObjectID(var1, var2);
                DomainAccess.createObjectOwnership(var1, var8, var9, "Project Lead", "Multiple Ownership For Object");
                DomainAccess.deleteObjectOwnership(var1, var8, (String)null, var7 + "_PRJ", "Multiple Ownership For Object");
            }
        } catch (Exception var10) {
            throw new FrameworkException(var10);
        }
    }

    private void updatePALOwnership(Context var1, String var2, String var3) throws Exception {
        DomainObject var4 = this.getAccessListObject(var1);
        if (!var3.equals(var2)) {
            try {
                ContextUtil.pushContext(var1);
                var4.setOwner(var1, var3);
            } finally {
                ContextUtil.popContext(var1);
            }
        }

    }

    protected void changeRelatedTypeOwnership(Context var1, String var2, String var3) throws MatrixException {
        try {
            gscProjectSpace var4 = new gscProjectSpace(this.getId(var1));
            var4.changeRelatedTypeOwnership(var1, var2, var3);
        } catch (Exception var5) {
            throw new MatrixException(var5);
        }
    }

    public Map getCompany(Context var1, StringList var2) throws FrameworkException {
        return this.getRelatedObject(var1, RELATIONSHIP_COMPANY_PROJECT_TEMPLATES, false, var2, (StringList)null);
    }

    public boolean isOwnerOrCoOwner(Context var1, String var2) {
        boolean var3 = false;
        String var4 = var1.getUser();
        String var5 = "from[" + RELATIONSHIP_MEMBER + "].to";
        StringList var6 = new StringList(1);
        var6.add(var5);
        StringList var7 = new StringList();
        var7.add("owner");
        var7.add(var5);

        try {
            MapList var8 = DomainObject.getInfo(var1, new String[]{var2}, var7, var6);
            Map var9 = (Map)var8.get(0);
            String var10 = (String)var9.get("owner");
            StringList var11 = (StringList)var9.get(var5);
            if (var4.equalsIgnoreCase(var10)) {
                var3 = true;
            } else if (var11 != null && var11.contains(var4)) {
                var3 = true;
            }
        } catch (FrameworkException var12) {
            var12.printStackTrace();
        }

        return var3;
    }

    public MapList getAllAdminFromCompany(Context var1) throws Exception {
        MapList var2 = new MapList();
        String var3 = PropertyUtil.getSchemaProperty(var1, "role_ProjectAdministrator");
        String var4 = PropertyUtil.getSchemaProperty(var1, "role_ExternalProjectAdministrator");
        String var5 = PropertyUtil.getSchemaProperty(var1, "role_VPLMProjectLeader");
        String var6 = PropertyUtil.getSchemaProperty(var1, "role_3DSRestrictedLeader");
        String var7 = "to[" + DomainRelationship.RELATIONSHIP_COMPANY_PROJECT_TEMPLATES + "].from.id";

        try {
            StringList var8 = new StringList(2);
            var8.add(var7);
            var8.add("owner");
            Map var9 = this.getInfo(var1, var8);
            String var10 = (String)var9.get(var7);
            String var11 = (String)var9.get("owner");
            StringList var12 = new StringList("id");
            Organization var13 = (Organization)DomainObject.newInstance(var1, var10);
            MapList var14 = var13.getMemberPersons(var1, var12, (String)null, (String)null);
            int var15 = var14.size();
            if (var15 > 0) {
                String var16 = "print role $1 select $2 dump $3";
                String var17 = ProgramCentralUtil.isNotNullString(var4) ? MqlUtil.mqlCommand(var1, var16, new String[]{var4, DomainConstants.TYPE_PERSON, ","}) : "";
                String var18 = ProgramCentralUtil.isNotNullString(var3) ? MqlUtil.mqlCommand(var1, var16, new String[]{var3, DomainConstants.TYPE_PERSON, ","}) : "";
                String var19 = ProgramCentralUtil.isNotNullString(var5) ? MqlUtil.mqlCommand(var1, var16, new String[]{var5, DomainConstants.TYPE_PERSON, ","}) : "";
                String var20 = ProgramCentralUtil.isNotNullString(var6) ? MqlUtil.mqlCommand(var1, var16, new String[]{var6, DomainConstants.TYPE_PERSON, ","}) : "";
                StringList var21 = FrameworkUtil.split(var17, ",");
                StringList var22 = FrameworkUtil.split(var18, ",");
                StringList var23 = FrameworkUtil.split(var19, ",");
                StringList var24 = FrameworkUtil.split(var20, ",");
                StringList var25 = new StringList();
                var25.addAll(var21);
                var25.addAll(var22);
                var25.addAll(var23);
                var25.addAll(var24);
                MapList var26 = this.getCoOwners(var1, new StringList("name"), (StringList)null);
                int var27 = var26.size();
                StringList var28 = new StringList(var27 + 1);
                var28.add(var11);

                for(int var29 = 0; var29 < var27; ++var29) {
                    Map var30 = (Map)var26.get(var29);
                    Object var31 = var30.get("name");
                    var28.add(String.valueOf(var31));
                }

                String var38 = UINavigatorUtil.isCloud(var1) ? "onlineinstance.product.derivative.derivative" : "product.derivative.derivative";
                String var39 = "print person $1 select $2 dump $3";

                for(int var40 = 0; var40 < var15; ++var40) {
                    Map var32 = (Map)var14.get(var40);
                    String var33 = (String)var32.get("name");
                    if (!var28.contains(var33) && var25.contains(var33)) {
                        String var34 = MqlUtil.mqlCommand(var1, var39, new String[]{var33, var38, "|"});
                        StringList var35 = FrameworkUtil.split(var34, "|");
                        boolean var36 = var35.contains(ProgramCentralConstants.PRG_LICENSE_ARRAY[0]);
                        if (var36) {
                            var2.add(var32);
                        }
                    }
                }
            }

            return var2;
        } catch (Exception var37) {
            var37.printStackTrace();
            throw var37;
        }
    }

    public MapList getCoOwners(Context var1, StringList var2, StringList var3) throws Exception {
        MapList var4 = new MapList();

        try {
            var4 = this.getRelatedObjects(var1, DomainRelationship.RELATIONSHIP_MEMBER, DomainRelationship.TYPE_PERSON, var2, var3, false, true, (short)0, (String)null, (String)null, 0);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return var4;
    }

    public void addCoOwners(Context var1, String[] var2) throws Exception {
        try {
            String[] var3 = ProgramCentralUtil.parseTableRowId(var1, var2);
            DomainRelationship.connect(var1, this, DomainRelationship.RELATIONSHIP_MEMBER, true, var3);
        } catch (Exception var4) {
            var4.printStackTrace();
            throw var4;
        }
    }

    public void removeCoOwners(Context var1, String[] var2) throws Exception {
        try {
            int var3 = var2.length;
            String[] var4 = new String[var3];

            for(int var5 = 0; var5 < var3; ++var5) {
                String var6 = var2[var5];
                Map var7 = ProgramCentralUtil.parseTableRowId(var1, var6);
                var4[var5] = (String)var7.get("relId");
            }

            DomainRelationship.disconnect(var1, var4);
        } catch (Exception var8) {
            var8.printStackTrace();
            throw var8;
        }
    }

    public StringList getOwnerInclusionList(Context var1) throws Exception {
        StringList var2 = new StringList();

        try {
            MapList var3 = this.getAllAdminFromCompany(var1);
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
                Map var5 = (Map)var4.next();
                Object var6 = var5.get("id");
                var2.add(String.valueOf(var6));
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return var2;
    }

    static {
        SELECT_COMPANY_ID = "to[" + RELATIONSHIP_COMPANY_PROJECT_TEMPLATES + "].from.id";
        SELECT_COMPANY_NAME = "to[" + RELATIONSHIP_COMPANY_PROJECT_TEMPLATES + "].from.name";
        SELECT_HAS_QUESTIONS = "from[" + RELATIONSHIP_PROJECT_QUESTION + "]";
        STATE_PROJECT_TEMPLATE_ACTIVE = PropertyUtil.getSchemaProperty("policy", POLICY_PROJECT_TEMPLATE, "state_Active");
        STATE_PROJECT_TEMPLATE_INACTIVE = PropertyUtil.getSchemaProperty("policy", POLICY_PROJECT_TEMPLATE, "state_Inactive");
        STATE_PROJECT_TEMPLATE_DRAFT = PropertyUtil.getSchemaProperty("policy", POLICY_PROJECT_TEMPLATE, "state_Draft");
        STATE_PROJECT_TEMPLATE_IN_APPROVAL = PropertyUtil.getSchemaProperty("policy", POLICY_PROJECT_TEMPLATE, "state_InApproval");
    }
}
