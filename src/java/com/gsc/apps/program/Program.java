package com.gsc.apps.program;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.matrixone.apps.common.Company;
import com.matrixone.apps.common.Person;
import com.matrixone.apps.common.util.ComponentsUtil;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.DecoratedOid;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.MqlUtil;
import com.matrixone.apps.domain.util.PropertyUtil;
import com.matrixone.apps.program.ProgramCentralConstants;
import matrix.db.BusinessObject;
import matrix.db.Context;
import matrix.util.StringList;

public class Program extends DomainObject {
    public static final String SELECT_COMPANY_ID;
    public static final String SELECT_COMPANY_NAME;
    public static final String SELECT_HAS_PROJECTS;
    public static final String STATE_PROGRAM_ACTIVE;
    public static final String STATE_PROGRAM_INACTIVE;

    public Program() {
    }

    public Program(String var1) throws Exception {
        super(var1);
    }

    public Program(BusinessObject var1) throws Exception {
        super(var1);
    }

    public Program(DecoratedOid var1) throws Exception {
        super(var1.getOID());
    }

    public void clear() throws FrameworkException {
        super.clear();
    }

    public void create(Context var1, String var2, String var3, String var4) throws FrameworkException {
        try {
            ComponentsUtil.checkLicenseReserved(var1, ProgramCentralConstants.PGE_PRG_LICENSE_ARRAY);
            this.clear();
            String[] var5 = new String[]{var2};
            this.triggerCreateCheck(var1, var5);
            ContextUtil.startTransaction(var1, true);
            this.createObject(var1, TYPE_PROGRAM, var2, this.getUniqueName(""), var3, var1.getVault().getName());
            this.triggerCreateAction(var1, (String[])null);
            ContextUtil.commitTransaction(var1);
        } catch (Exception var6) {
            ContextUtil.abortTransaction(var1);
            throw new FrameworkException(var6);
        }
    }

    public void delete(Context var1) throws FrameworkException {
        try {
            this.triggerDeleteCheck(var1, (String[])null);
            super.deleteObject(var1);
        } catch (Exception var3) {
            throw new FrameworkException(var3);
        }
    }

    public static String getDefaultPolicy(Context var0) {
        return POLICY_PROGRAM;
    }

    public static MapList getPrograms(Context var0, StringList var1, String var2) throws FrameworkException {
        return getPrograms(var0, var1, "*", var2, 0);
    }

    public static MapList getPrograms(Context var0, StringList var1, String var2, String var3, int var4) throws FrameworkException {
        try {
            String var5 = Person.getPerson(var0).getCompanyId(var0);
            Company var6 = (Company)DomainObject.newInstance(var0, var5);
            return var6.getRelatedObjects(var0, RELATIONSHIP_COMPANY_PROGRAM, TYPE_PROGRAM, var1, (StringList)null, false, true, (short)1, var3, (String)null);
        } catch (Exception var7) {
            throw new FrameworkException(var7);
        }
    }

    public MapList getProjects(Context var1, StringList var2, String var3) throws FrameworkException {
        MapList var4 = this.getRelatedObjects(var1, RELATIONSHIP_PROGRAM_PROJECT, "*", var2, (StringList)null, false, true, (short)1, var3, (String)null);
        return var4;
    }

    public static MapList getTypeAttributes(Context var0) throws FrameworkException {
        return getTypeAttributes(var0, TYPE_PROGRAM);
    }

    protected boolean isNameUnique(Context var1, String var2) throws FrameworkException {
        Company var3 = null;
        String var4;
        if (this.getObjectId() != null) {
            if (this.getName() == var2) {
                return true;
            }

            var4 = this.getInfo(var1, SELECT_COMPANY_ID);
            var3 = new Company();
            var3.setId(var4);
        } else {
            var3 = Person.getPerson(var1).getCompany(var1);
        }

        var4 = "temp query bus $1 $2 $3 where $4 limit $5 dump $6";
        String var5 = SELECT_COMPANY_ID + " == " + var3.getObjectId();
        String var6 = MqlUtil.mqlCommand(var1, var4, new String[]{TYPE_PROGRAM, var2, "*", var5, "1", "|"});
        return "".equals(var6);
    }

    public void setName(Context var1, String var2) throws FrameworkException {
        String[] var3 = new String[]{var2};

        try {
            this.triggerChangeNameCheck(var1, var3);
        } catch (Exception var5) {
            throw new FrameworkException(var5);
        }

        super.setName(var1, var2);
    }

    protected int triggerCreateCheck(Context var1, String[] var2) throws Exception {
        String var3 = var2[0];
        if (!this.isNameUnique(var1, var3)) {
            throw new Exception("Error: Program name is not unique; please supply a new name.");
        } else {
            return 0;
        }
    }

    protected void triggerCreateAction(Context var1, String[] var2) throws Exception {
        Company var3 = Person.getPerson(var1).getCompany(var1);
        DomainRelationship.connect(var1, var3, RELATIONSHIP_COMPANY_PROGRAM, this);
    }

    protected int triggerChangeNameCheck(Context var1, String[] var2) throws Exception {
        String var3 = var2[0];
        if (!this.isNameUnique(var1, var3)) {
            throw new Exception("Error: Program name is not unique; please supply a new name.");
        } else {
            return 0;
        }
    }

    protected int triggerDeleteCheck(Context var1, String[] var2) throws Exception {
        String var3 = this.getInfo(var1, SELECT_HAS_PROJECTS);
        if ("TRUE".equalsIgnoreCase(var3)) {
            throw new Exception("Error: Program can not be deleted as it contains project references.");
        } else {
            return 0;
        }
    }

    static {
        SELECT_COMPANY_ID = "to[" + RELATIONSHIP_COMPANY_PROGRAM + "].from.id";
        SELECT_COMPANY_NAME = "to[" + RELATIONSHIP_COMPANY_PROGRAM + "].from.name";
        SELECT_HAS_PROJECTS = "from[" + RELATIONSHIP_PROGRAM_PROJECT + "]";
        STATE_PROGRAM_ACTIVE = PropertyUtil.getSchemaProperty("policy", POLICY_PROGRAM, "state_Active");
        STATE_PROGRAM_INACTIVE = PropertyUtil.getSchemaProperty("policy", POLICY_PROGRAM, "state_Inactive");
    }
}
