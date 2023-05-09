package com.gsc.enovia.common;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollection;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Dataobject;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.QueryData;
import com.gsc.enovia.dpm.ServiceUtil;
import com.gsc.enovia.dpm.gscProjectSpace;
import com.matrixone.apps.domain.DomainObject;
import matrix.db.Context;
import matrix.util.StringList;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

public class gscCommonCode extends DomainObject {
    protected static final BigInteger MAX_OBJECTS = new BigInteger("5000");
    private static final String POLICY_GSCCODEMASTER = "policy_gscCodeMaster";
    private static final String TYPE_GSCCODEMASTER = "type_gscCodeMaster";
    protected static Datacollection getAllCommonCodes(Context context, Datacollection datacollection1, List selects, String current) throws FoundationException {
        boolean hasData = false;
        String typeName = PropertyUtil.getSchemaName(context, TYPE_GSCCODEMASTER);
        if (datacollection1 != null && !datacollection1.getDataobjects().isEmpty()) {
            hasData = true;
        }

        if (hasData && (selects == null || selects.isEmpty())) {
            throw new FoundationException("You must provide some selectables to gscCommonCode.getCommonCodes");
        } else {
            Datacollection datacollection = new Datacollection();
            if (hasData) {
                try {
                    ServiceUtil.getObjectInfo(context, datacollection1, selects);
                    List dataobjects = datacollection1.getDataobjects();
                    Iterator itr = dataobjects.iterator();

                    while(itr.hasNext()) {
                        Dataobject dataobject = (Dataobject)itr.next();
                        if (dataobject.getId().isEmpty()) {
                            throw new FoundationException("DPM100: Invalid Program ID. Business Object Does Not Exist");
                        }

                        datacollection.getDataobjects().add(dataobject);
                    }
                } catch (Exception var11) {
                    if (var11.getMessage().contains("Business Object Does Not Exist")) {
                        throw new FoundationException("DPM100: Invalid gscCodeMaster ID. Business Object Does Not Exist");
                    }

                    throw var11;
                }
            } else {
                String whereClause = "";
                if (current != null && !current.isEmpty()) {
                    whereClause = gscProjectSpace.getStateWhereClause(context, "", current, _getCommonCodeStates());
                } else {
                    //whereClause = String.format("current == %s", "Plan Frozen");
                }

                QueryData queryData = new QueryData();
                queryData.setTypePattern(typeName);
                System.out.println("whereClause = " + whereClause);
                queryData.setWhereExpression(whereClause);
                queryData.setLimit(MAX_OBJECTS);
                datacollection = ObjectUtil.query(context, queryData, selects);
            }

            return datacollection;
        }
    }
    protected static Datacollection getCommonCodes(Context context, Datacollection datacollection1, List selects, String code) throws FoundationException {
        boolean hasData = false;
        String typeName = PropertyUtil.getSchemaName(context, TYPE_GSCCODEMASTER);
        if (datacollection1 != null && !datacollection1.getDataobjects().isEmpty()) {
            hasData = true;
        }

        if (hasData && (selects == null || selects.isEmpty())) {
            throw new FoundationException("You must provide some selectables to gscCommonCode.getCommonCodes");
        } else {
            Datacollection datacollection = new Datacollection();

                String whereClause = "";
                if (code != null && !code.isEmpty()) {
                    whereClause = "revision=="+code;
                }

                QueryData queryData = new QueryData();
                queryData.setTypePattern(typeName);
                System.out.println("whereClause = " + whereClause);
                queryData.setWhereExpression(whereClause);
                queryData.setLimit(MAX_OBJECTS);
                datacollection = ObjectUtil.query(context, queryData, selects);

            return datacollection;
        }
    }
    static StringList _getCommonCodeStates() {
        StringList var0 = new StringList(6);
        var0.add("Active");
        var0.add("Inactive");
        return var0;
    }
}
