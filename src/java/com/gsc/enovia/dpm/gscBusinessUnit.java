package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.dpm.ProjectSpace;
import com.dassault_systemes.enovia.dpm.ServiceUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollection;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Dataobject;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.QueryData;
import com.matrixone.apps.common.util.ComponentsUtil;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.PersonUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.Set;

import com.matrixone.apps.program.ProgramCentralConstants;
import com.matrixone.apps.program.ProgramCentralUtil;
import matrix.db.*;
import matrix.util.MatrixException;
import matrix.util.StringList;

public class gscBusinessUnit extends DomainObject {
    protected static Datacollection getBusinessUnits(Context context, Datacollection datacollection1, List selects, String state, boolean isMyProfile) throws FoundationException {
        boolean var5 = false;
        String var6 = PropertyUtil.getSchemaName(context, TYPE_BUSINESS_UNIT);
        if (datacollection1 != null && !datacollection1.getDataobjects().isEmpty()) {
            var5 = true;
        }

        if (var5 && (selects == null || selects.isEmpty())) {
            throw new FoundationException("You must provide some selectables to Program.getUserPrograms");
        } else {
            Datacollection datacollection = new Datacollection();
            if (var5) {
                try {
                    ServiceUtil.getObjectInfo(context, datacollection1, selects);
                    List var8 = datacollection1.getDataobjects();
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
                if (state != null && !state.isEmpty()) {
                    whereClause = ServiceUtil.getStateWhereClause(context, "", state, _getPersonStates());
                } else {
                    whereClause = String.format("current == %s", STATE_PERSON_ACTIVE);
                }

                if (isMyProfile) {
                    whereClause = String.format("%s AND owner == %s", whereClause, context.getUser());
                }

                QueryData queryData = new QueryData();
                queryData.setTypePattern(var6);
                System.out.println("whereClause = " + whereClause);
                queryData.setWhereExpression(whereClause);
                datacollection = ObjectUtil.query(context, queryData, selects);
            }

            return datacollection;
        }
    }
    private static StringList _getPersonStates() {
        StringList var0 = new StringList(4);
        var0.add(STATE_PERSON_ACTIVE);
        return var0;
    }
}
