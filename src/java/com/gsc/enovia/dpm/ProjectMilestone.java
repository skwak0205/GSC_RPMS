package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.dpm.ServiceUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceConstants;
import com.dassault_systemes.enovia.e6wv2.foundation.db.JPOUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectEditUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollection;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollections;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Dataobject;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.ExpandData;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.ExpressionType;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Format;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.PrintData;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.ServiceParameters;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.UpdateActions;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.dassault_systemes.enovia.e6wv2.foundation.util.FormatUtil;
import matrix.db.Context;

public class ProjectMilestone implements ServiceConstants {
    private static final String TAG = ">>> MILESTONE (OBJ):  ";
    private static final String POLICY_PROXYITEM = "ProxyItem";
    private static final String SELECT_PROXY_TENANT = "attribute[Proxy_Tenant].value";
    private static final String SELECT_PROXY_SERVICE = "attribute[Proxy_Service].value";
    private static final String SELECT_PROXY_MODIFIED = "attribute[Proxy_Modified].value";
    private static final String SELECT_PROXY_STATE = "attribute[Proxy_State].value";
    private static final String SELECT_PROXY_ID = "attribute[Proxy_Id].value";
    private static final String SELECT_PROXY_TITLE = "attribute[Proxy_Title].value";
    private static final String SELECT_PROXY_URL = "attribute[Proxy_URL].value";
    private static final String SELECT_PHYSICALID = "physicalid";
    private static final String SELECT_PROXYITEM = "interface[ProxyItem]";
    private static final String SELECT_XPLANID = "3DPlanId";
    private static final String SERVICE_XPLAN = "3DPlan";
    private static final String SERVICE_LABEL = "serviceId";
    private static final String ATTRIBUTE_PROXY_TENANT = "ProxyItem.Proxy_Tenant";
    private static final String ATTRIBUTE_PROXY_SERVICE = "ProxyItem.Proxy_Service";
    private static final String ATTRIBUTE_PROXY_MODIFIED = "ProxyItem.Proxy_Modified";
    private static final String ATTRIBUTE_PROXY_STATE = "ProxyItem.Proxy_State";
    private static final String ATTRIBUTE_PROXY_ID = "ProxyItem.Proxy_Id";
    private static final String ATTRIBUTE_PROXY_TITLE = "ProxyItem.Proxy_Title";
    private static final String ATTRIBUTE_PROXY_URL = "ProxyItem.Proxy_URL";

    protected static Datacollections getProjectMilestones(Context var0, Datacollection var1, String var2, boolean var3, List var4, String var5) throws FoundationException {
        String var6 = PropertyUtil.getSchemaName(var0, "type_ProjectAccessList");
        String var7 = PropertyUtil.getSchemaName(var0, "relationship_ProjectAccessList");
        String var8 = PropertyUtil.getSchemaName(var0, "relationship_ProjectAccessKey");
        String var9 = PropertyUtil.getSchemaName(var0, "type_Milestone");
        String var10 = PropertyUtil.getSchemaName(var0, "type_Gate");
        gscProjectSpace.checkProjectAccess(var0, ((Dataobject)var1.getDataobjects().get(0)).getId());
        String var12 = "";
        boolean var13 = true;
        String var14 = "(type==\"" + var9 + "\" || type==\"" + var10 + "\")";
        String var15 = "";
        if (var5 != null && !var5.isEmpty()) {
            var15 = gscProjectSpace.getStateWhereClause(var0, "", var5, gscProjectSpace._getMilestoneStates());
        } else {
            var15 = "\"true\"";
        }

        var15 = String.format("%s AND %s", var14, var15);
        var4.add(new Select("attribute[Proxy_Tenant].value", "attribute[Proxy_Tenant].value", ExpressionType.BUS, (Format)null, false));
        var4.add(new Select("attribute[Proxy_Service].value", "attribute[Proxy_Service].value", ExpressionType.BUS, (Format)null, false));
        var4.add(new Select("attribute[Proxy_Id].value", "attribute[Proxy_Id].value", ExpressionType.BUS, (Format)null, false));
        var4.add(new Select("attribute[Proxy_Modified].value", "attribute[Proxy_Modified].value", ExpressionType.BUS, (Format)null, false));
        var4.add(new Select("attribute[Proxy_Title].value", "attribute[Proxy_Title].value", ExpressionType.BUS, (Format)null, false));
        var4.add(new Select("attribute[Proxy_URL].value", "attribute[Proxy_URL].value", ExpressionType.BUS, (Format)null, false));
        Datacollections var16 = new Datacollections();

        try {
            if (var2 != null && !var2.isEmpty()) {
                Dataobject var21 = ObjectUtil.print(var0, var2, (PrintData)null, var4);
                Datacollection var22 = new Datacollection();
                var22.getDataobjects().add(var21);
                var16.getDatacollections().add(var22);
            } else {
                ExpandData var17 = new ExpandData();
                var17.setGetTo(true);
                var17.setGetFrom(false);
                var17.setRecurseLevels("1");
                var17.setTypePattern(var6);
                var17.setRelationshipPattern(var7);
                Datacollections var18 = ObjectUtil.expand(var0, var1, var17, var4);
                Datacollection var19 = (Datacollection)var18.getDatacollections().get(0);
                var17.setGetTo(false);
                var17.setGetFrom(true);
                var17.setTypePattern("*");
                var17.setRelationshipPattern(var8);
                var17.setObjectWhere(var15);
                var16 = ObjectUtil.expand(var0, var19, var17, var4);
            }

            var16 = cleanProxyData(var0, var16);
            return var16;
        } catch (Exception var20) {
            var20.printStackTrace();
            throw new FoundationException(var20.getMessage());
        }
    }

    public static Dataobject updateProjectMilestones(Context var0, String[] var1) throws Exception {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        Datacollection var3 = var2.getDatacollection();
        ServiceUtil.checkLicenseDPM(var0);
        Dataobject var4 = (Dataobject)var3.getDataobjects().get(0);
        String var5 = var4.getId();
        if (var5 != null && !"".equalsIgnoreCase(var5)) {
            UpdateActions var6 = var4.getUpdateAction();
            ArrayList var7 = new ArrayList(10);
            var7.add(new Select("physicalid", "physicalid", ExpressionType.BUS, (Format)null, false));
            var7.add(new Select("interface[ProxyItem]", "interface[ProxyItem]", ExpressionType.BUS, (Format)null, false));
            var7.add(new Select("attribute[Proxy_Tenant].value", "attribute[Proxy_Tenant].value", ExpressionType.BUS, (Format)null, false));
            var7.add(new Select("attribute[Proxy_Service].value", "attribute[Proxy_Service].value", ExpressionType.BUS, (Format)null, false));
            var7.add(new Select("attribute[Proxy_Id].value", "attribute[Proxy_Id].value", ExpressionType.BUS, (Format)null, false));
            var7.add(new Select("attribute[Proxy_Title].value", "attribute[Proxy_Title].value", ExpressionType.BUS, (Format)null, false));
            var7.add(new Select("attribute[Proxy_URL].value", "attribute[Proxy_URL].value", ExpressionType.BUS, (Format)null, false));
            var7.add(new Select("attribute[Proxy_Modified].value", "attribute[Proxy_Modified].value", ExpressionType.BUS, (Format)null, false));
            var7.add(new Select("attribute[Proxy_State].value", "attribute[Proxy_State].value", ExpressionType.BUS, (Format)null, false));
            Dataobject var8 = ObjectUtil.print(var0, var5, (PrintData)null, var7);
            boolean var9 = false;
            if ("TRUE".equalsIgnoreCase((String)var8.getDataelements().get("interface[ProxyItem]"))) {
                var9 = true;
            }

            String var10 = var4.getService();
            if (var10 == null) {
                var10 = (String)var4.getDataelements().get("serviceId");
            }

            String var11 = var4.getTenant();
            String var12 = (String)var4.getDataelements().get("3DPlanId");
            String var13 = (String)var4.getDataelements().get("title");
            String var14 = (String)var4.getDataelements().get("image");
            String var15 = System.getenv("DASHBOARD_URL");
            if (var11 == null || "".equalsIgnoreCase(var11)) {
                var11 = "OnPremise";
            }

            boolean var16 = false;
            if (var10 != null && "3DPlan".equalsIgnoreCase(var10) && var12 != null && !"".equalsIgnoreCase(var12)) {
                var16 = true;
            }

            StringBuffer var17 = new StringBuffer();
            if (var16) {
                var17.append(var15);
                var17.append("#app:ENXPLAN_AP/content:ids=");
                var17.append(var12);
                var17.append("&3dPlatformId=");
                var17.append(var11);
            }

            String var18 = var17.toString();
            HashMap var19 = new HashMap(5);
            String var20 = FormatUtil.formatMxDate(var0, new Date(), (SimpleDateFormat)null);
            var19.put("ProxyItem.Proxy_Tenant", var11);
            var19.put("ProxyItem.Proxy_Service", var10);
            var19.put("ProxyItem.Proxy_Id", var12);
            var19.put("ProxyItem.Proxy_Title", var13);
            var19.put("ProxyItem.Proxy_Modified", var20);
            var19.put("ProxyItem.Proxy_URL", var18);
            switch (var6) {
                case CREATE:
                case MODIFY:
                case CONNECT:
                    if (var16) {
                        if (!var9) {
                            gscProjectService.addProxyInterface(var0, var5);
                        }

                        ObjectEditUtil.modify(var0, var5, var19, false);
                    }
                    break;
                case DISCONNECT:
                case DELETE:
                    if (var9) {
                        gscProjectService.removeProxyInterface(var0, var5);
                    }
            }

            return var4;
        } else {
            throw new FoundationException("Milestone Object ID required");
        }
    }

    private static Datacollections cleanProxyData(Context var0, Datacollections var1) {
        Iterator var2 = var1.getDatacollections().iterator();

        while(var2.hasNext()) {
            Datacollection var3 = (Datacollection)var2.next();

            for(int var4 = 0; var4 < var3.getDataobjects().size(); ++var4) {
                Dataobject var5 = (Dataobject)var3.getDataobjects().get(var4);
                String var6 = (String)var5.getDataelements().get("attribute[Proxy_Tenant].value");
                String var7 = (String)var5.getDataelements().get("attribute[Proxy_Service].value");
                String var8 = (String)var5.getDataelements().get("attribute[Proxy_Id].value");
                String var9 = (String)var5.getDataelements().get("attribute[Proxy_Modified].value");
                String var10 = (String)var5.getDataelements().get("attribute[Proxy_Title].value");
                String var11 = (String)var5.getDataelements().get("attribute[Proxy_URL].value");
                if (var8 != null && !var8.isEmpty() && var7 != null && !var7.isEmpty() && "3DPlan".equalsIgnoreCase(var7)) {
                    var5.getDataelements().put("3DPlanId", var8);
                    if (var6 != null && !var6.isEmpty()) {
                        var5.getDataelements().put("tenant", var6);
                    }

                    if (var7 != null && !var7.isEmpty()) {
                        var5.getDataelements().put("service", var7);
                    }
                }

                var5.getDataelements().remove("attribute[Proxy_Tenant].value");
                var5.getDataelements().remove("attribute[Proxy_Service].value");
                var5.getDataelements().remove("attribute[Proxy_Id].value");
                var5.getDataelements().remove("attribute[Proxy_Title].value");
                var5.getDataelements().remove("attribute[Proxy_URL].value");
                var5.getDataelements().remove("attribute[Proxy_Modified].value");
            }
        }

        return var1;
    }
}
