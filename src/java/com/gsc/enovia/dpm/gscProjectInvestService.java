package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceBase;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ContextUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.JPOUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.ArgMap;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMapAdapter;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.Select;
import com.gsc.util.ServiceUtil;
import com.matrixone.apps.domain.util.MqlUtil;
import matrix.db.Context;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class gscProjectInvestService {

    public static Datacollection getProjectInvest(Context context, String[] args) throws FoundationException {
        ServiceParameters serviceParameters = (ServiceParameters) JPOUtil.unpackArgs(args);
        HttpServletRequest var3 = serviceParameters.getHttpRequest();
        long var4 = System.currentTimeMillis();
        com.dassault_systemes.enovia.dpm.ServiceUtil.checkLicenseDPM(context);
        Datacollection datacollection = serviceParameters.getDatacollection();
        List selects = serviceParameters.getSelects();
        ArgMap argMap = serviceParameters.getServiceArgs();
        String state = (String) argMap.get("state");
        String owned = (String) argMap.get("owned");
        String projectId = (String) argMap.get("projectId");
        boolean isOwned = false;
        if (owned != null && !owned.isEmpty()) {
            isOwned = Boolean.parseBoolean(owned);
        }

        Datacollection datacollection1;
        if (datacollection != null && !datacollection.getDataobjects().isEmpty()) {
            datacollection1 = gscProjectBudget.getUserProjectBudgets(context, datacollection, false, selects, (String) null);
        } else if (projectId != null && !"".equals(projectId)) {
            datacollection1 = gscProjectBudget.getUserProjectBudgets(context, datacollection, false, selects, (String) null);
        } else {
            datacollection1 = gscProjectInvest.getAllInvests(context, (Datacollection) null, isOwned, selects, state);
        }

        String var14 = String.format("%s%s (%d) ...\t", ">>> PROGRAM (SVC):  ", "getProjectBudgets", datacollection1.getDataobjects().size());
        FoundationUtil.debug(var14, var4);
        return datacollection1;
    }

    public static Dataobject updateProjectInvestIf(Context context, String[] args) throws Exception {
        ServiceParameters serviceParameters = (ServiceParameters) JPOUtil.unpackArgs(args);
        Datacollection datacollection = serviceParameters.getDatacollection();
        Dataobject dataobject = (Dataobject) datacollection.getDataobjects().get(0);
        UpdateActions updateAction = dataobject.getUpdateAction();
        dataobject = updateProjectInvestIf(context, serviceParameters, dataobject, updateAction);
        return dataobject;
    }

    public static Dataobject updateProjectInvestIf(Context context, ServiceParameters serviceParameters, Dataobject dataobject, UpdateActions updateActions) throws FoundationException {
        Dataobject res = null;
        try {
            String investId = DataelementMapAdapter.getDataelementValue(dataobject, "investId");
            String projectId = DataelementMapAdapter.getDataelementValue(dataobject, "projectId");

            System.out.println("invest id >>> " + investId);
            System.out.println("project id >>> " + projectId);

            ContextUtil.startTransaction(context, true);
            if (UpdateActions.CONNECT.equals(updateActions)) {
                String checkRel = MqlUtil.mqlCommand(context, String.format("query connection type gscProjectInvestIf where 'from.physicalid==%s && to.physicalid==%s'", projectId, investId));
                System.out.println("check rel >>> " + checkRel);
                if (checkRel.isEmpty()) {
                    ServiceUtil.updateConnection(context, UpdateActions.CREATE, investId, "gscProjectInvestIf", projectId, false, true);
                } else {
                    throw new Exception("Error : Already Connected to the project");
                }

            } else if (UpdateActions.DISCONNECT.equals(updateActions)) {
                String checkRel = MqlUtil.mqlCommand(context, String.format("query connection type gscProjectInvestIf where 'from.physicalid==%s && to.physicalid==%s'", projectId, investId));
                if (!checkRel.isEmpty()) {
                    MqlUtil.mqlCommand(context, String.format("disconnect bus %s rel 'gscProjectInvestIf' to %s", projectId, investId));
                } else {
                    throw new Exception("Error : No Relationship Connected Between the project and invest object");
                }

            }

            ContextUtil.commitTransaction(context);
            res = getObject(context, serviceParameters, investId, null, null);
        } catch (Exception e) {
            ContextUtil.abortTransaction(context);
            e.printStackTrace();
            throw new FoundationException(e);
        }

        return res;
    }

    protected static Dataobject getObject(Context var0, ServiceParameters var1, String var2, String var3, String var4) throws FoundationException {
        String var5 = var1.getServiceName();
        var1.setServiceName(var5 + "/" + var2);
        if (var3 != null) {
            var1.getServiceArgs().put("$include", var3);
        }

        if (var4 != null) {
            var1.getServiceArgs().put("$fields", var4);
        }

        Servicedata var6 = ServiceBase.getData(var0, var1);
        Dataobject var7 = null;
        if (var6.getData().size() > 0) {
            var7 = (Dataobject) var6.getData().get(0);
        } else {
            ArrayList<Selectable> var8 = new ArrayList<com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable>(6);
            var8.add(new Select("physicalid", "physicalid", ExpressionType.BUS, (Format) null, false));
            var8.add(new Select("type", "type", ExpressionType.BUS, (Format) null, false));
            var8.add(new Select("name", "name", ExpressionType.BUS, (Format) null, false));
            var8.add(new Select("revision", "revision", ExpressionType.BUS, (Format) null, false));
            var8.add(new Select("originated", "originated", ExpressionType.BUS, (Format) null, false));
            var8.add(new Select("description", "description", ExpressionType.BUS, (Format) null, false));
            if (var7 == null && var2 != null && !var2.isEmpty()) {
                var7 = ObjectUtil.print(var0, var2, (PrintData) null, var8);
            }
        }

        return var7;
    }
}
