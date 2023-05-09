package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.dpm.ServiceUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollection;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Dataobject;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.QueryData;
import com.matrixone.apps.domain.DomainObject;
import matrix.db.Context;
import matrix.util.StringList;

import java.util.Iterator;
import java.util.List;

public class gscProfile  extends DomainObject {
    protected static Datacollection getProfiles(Context context, Datacollection datacollection1, List selects, String state, boolean isMyProfile, String businessUnitId, String gscYear) throws FoundationException {
        boolean var5 = false;
        String var6 = PropertyUtil.getSchemaName(context, TYPE_PERSON);
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

                // 230425 조회 조건 추가 - 해당 팀의 인력 및 년도
                if (businessUnitId != null && !businessUnitId.isEmpty()) {
                    if(whereClause != "") {
                        whereClause = String.format("%s AND (to[Business Unit Employee].from.physicalid == \"%s\")", whereClause, businessUnitId);
                    }else{
                        whereClause = String.format("%s (to[Business Unit Employee].from.physicalid == \"%s\")", whereClause, businessUnitId);
                    }
                }
                /*
                if (gscYear != null && !gscYear.isEmpty()) {
                    if(whereClause != "") {
                        whereClause = String.format("%s AND (attribute[gscYear] == \"%s\")", whereClause, gscYear);
                    }else{
                        whereClause = String.format("%s (attribute[gscYear] == \"%s\")", whereClause, gscYear);
                    }
                }
                */
                // 230425 조회 조건 추가 - 해당 팀의 인력 및 년도

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
