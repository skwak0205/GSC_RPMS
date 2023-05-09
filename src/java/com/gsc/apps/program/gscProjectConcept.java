//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gsc.apps.program;

import com.matrixone.apps.common.VaultHolder;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.DecoratedOid;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.MailUtil;
import com.matrixone.apps.domain.util.MapList;
import matrix.db.BusinessObject;
import matrix.db.Context;
import matrix.util.StringList;

public class gscProjectConcept extends gscProjectSpace implements VaultHolder {
    public gscProjectConcept() {
    }

    public gscProjectConcept(String var1) throws Exception {
        super(var1);
    }

    public gscProjectConcept(BusinessObject var1) throws Exception {
        super(var1);
    }

    public gscProjectConcept(DecoratedOid var1) throws Exception {
        super(var1.getOID());
    }

    public void create(Context var1, String var2, String var3, String var4) throws FrameworkException {
        this.create(var1, TYPE_PROJECT_CONCEPT, var2, var3, var4);
    }

    public static String getDefaultPolicy(Context var0) {
        return POLICY_PROJECT_CONCEPT;
    }

    public static MapList getTypeAttributes(Context var0) throws FrameworkException {
        return getTypeAttributes(var0, TYPE_PROJECT_CONCEPT);
    }

    public static void sendNotification(Context var0, String var1, StringList var2, String var3, String[] var4, String[] var5, String var6, String[] var7, String[] var8) throws FrameworkException {
        DomainObject var9 = DomainObject.newInstance(var0, var1, "PROGRAM");

        try {
            var9.startTransaction(var0, true);
            StringList var10 = new StringList();
            var10.addElement(var1);
            MailUtil.sendNotification(var0, var2, (StringList)null, (StringList)null, var3, var4, var5, var6, var7, var8, var10, (String)null, "emxProgramCentralStringResource");
            ContextUtil.commitTransaction(var0);
        } catch (Exception var11) {
            ContextUtil.abortTransaction(var0);
            throw new FrameworkException(var11);
        }
    }
}
