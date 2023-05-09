package com.gsc.apps.program;

import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.DecoratedOid;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.PropertyUtil;
import matrix.db.BusinessObject;
import matrix.db.Context;

public class IntervalItemData extends DomainObject {
    public static final String POLICY_INTERVAL_ITEM_DATA = PropertyUtil.getSchemaProperty("policy_IntervalItemData");
    public static final String TYPE_INTERVAL_ITEM_DATA = PropertyUtil.getSchemaProperty("type_IntervalItemData");

    public IntervalItemData() {
    }

    public IntervalItemData(String var1) throws Exception {
        super(var1);
    }

    public IntervalItemData(DecoratedOid var1) throws Exception {
        super(var1.getOID());
    }

    public IntervalItemData(BusinessObject var1) throws Exception {
        super(var1);
    }

    public void clear() throws FrameworkException {
        super.clear();
    }

    public DomainRelationship create(Context var1, String var2, String var3, String var4, DomainObject var5) throws FrameworkException {
        String var6 = this.getUniqueName("");
        if (var2 == null || var2.equals("")) {
            var2 = "IID-" + var6;
        }

        DomainRelationship var7 = this.createAndConnect(var1, TYPE_INTERVAL_ITEM_DATA, var2, var6, var3, (String)null, var4, var5, true);
        return var7;
    }

    public String getDefaultPolicy(Context var1, String var2) {
        return POLICY_INTERVAL_ITEM_DATA;
    }
}
