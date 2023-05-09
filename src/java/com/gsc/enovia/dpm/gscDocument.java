package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.dpm.ServiceUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollection;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Dataobject;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.ExpandData;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.PrintData;
import com.dassault_systemes.enovia.e6wv2.foundation.util.FormatUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.util.StringUtil;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.PersonUtil;
import matrix.db.Context;
import matrix.util.StringList;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class gscDocument extends com.matrixone.apps.document.GenericDocument{

    protected static Datacollection getUserDocuments(Context context, Datacollection datacollection, boolean isOwned, boolean isStructured, List selects, String state) throws FoundationException {
        return getUserDocuments(context, datacollection, isOwned, isStructured, selects, state, "");
    }

    protected static Datacollection getUserDocuments(Context context, Datacollection datacollection, boolean isOwned, boolean isStructured, List list, String state, String excludedTypes) throws FoundationException {
        boolean hasData = false;
        String typeProjectSpace = PropertyUtil.getSchemaName(context, "type_Document");
        if (datacollection != null && !datacollection.getDataobjects().isEmpty()) {
            hasData = true;
        }

        if (!hasData || list != null && !list.isEmpty()) {
            Datacollection datacollection1 = new Datacollection();
            List dataObjList;
            Dataobject dataobject;
            ExpandData expandData;
            if (hasData) {
                try {
                    getDocumentInfo(context, datacollection, list);
                    dataObjList = datacollection.getDataobjects();
                    Iterator objItr = dataObjList.iterator();

                    while(objItr.hasNext()) {
                        dataobject = (Dataobject)objItr.next();
                        if (dataobject.getId().isEmpty()) {
                            throw new FoundationException("DPM100: Invalid Project Space ID. Business Object Does Not Exist");
                        }

                        datacollection1.getDataobjects().add(dataobject);
                    }
                } catch (Exception ex) {
                    if (ex.getMessage().contains("Business Object Does Not Exist")) {
                        throw new FoundationException("DPM100: Invalid Project Space ID. Business Object Does Not Exist");
                    }

                    throw ex;
                }
            } else {
                String objectWhere = "";
                if (state != null && !state.isEmpty()) {
                    objectWhere = gscProjectSpace.getStateWhereClause(context, "", state, _getDocumentStates());
                } else {
                    objectWhere = String.format("current != %s", "OBSOLETE");
                }

                if (excludedTypes != null && !excludedTypes.isEmpty()) {
                    List var23 = StringUtil.splitString(excludedTypes, ",");
                    String var14;
                    if (!var23.isEmpty()) {
                        for(Iterator var25 = var23.iterator(); var25.hasNext(); objectWhere = objectWhere + " AND type != '" + var14 + "'") {
                            var14 = (String)var25.next();
                        }
                    }
                }

                if (isOwned) {
                    objectWhere = String.format("%s AND owner == %s", objectWhere, context.getUser());
                }

                expandData = new ExpandData();
                expandData.setRelationshipPattern("Task Deliverable");
                expandData.setLimit(Short.valueOf((short)0));
                expandData.setRecurseToLevel(BigDecimal.valueOf(1L));
                expandData.setGetFrom(false);
                expandData.setGetTo(true);
                expandData.setTypePattern("Task Management");
                expandData.setObjectWhere(objectWhere);
                dataobject = new Dataobject();

                try {
                    DomainObject personObject = PersonUtil.getPersonObject(context);
                    String personObj = personObject.getInfo(context, "id");
                    dataobject.setId(personObj);
                } catch (Exception var20) {
                    var20.printStackTrace();
                    throw new FoundationException(var20.getMessage());
                }

                datacollection1 = ObjectUtil.expand(context, dataobject, expandData, list);
            }

            return datacollection1;

        } else {
            throw new FoundationException("You must provide some selectables to ProjectSpace.getUserProjects");
        }
    }
    static StringList _getDocumentStates() {
        StringList var0 = new StringList(6);
        var0.add("PRIVATE");
        var0.add("IN_WORK");
        var0.add("FROZEN");
        var0.add("RELEASED");
        return var0;
    }
    public static Dataobject getDocumentInfo(Context var0, String var1, List var2) throws FoundationException {
        Dataobject var3 = new Dataobject();
        var3.setId(var1);
        Datacollection var4 = new Datacollection();
        var4.getDataobjects().add(var3);
        getDocumentInfo(var0, var4, var2);
        return (Dataobject)var4.getDataobjects().get(0);
    }

    public static void getDocumentInfo(Context var0, Datacollection var1, List var2) throws FoundationException {
        ServiceUtil.checkLicenseProject(var0, (HttpServletRequest)null);
        if (var2 != null && !var2.isEmpty()) {
            if (var1.getDataobjects().size() > 0) {
                ObjectUtil.print(var0, var1, (PrintData)null, var2, true);
            }

        } else {
            throw new FoundationException("You must provide some selectables to ProjectSpace.getProjectInfo");
        }
    }
}
