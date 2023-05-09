//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gsc.enovia.tskv2;

import com.dassault_systemes.enovia.e6wproxy.ProxyObject;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceConstants;
import com.dassault_systemes.enovia.e6wv2.foundation.db.JPOUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectEditUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.ArgMap;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMap;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.Select;
import matrix.db.Context;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TaskReferences implements ServiceConstants {
    private static final String POLICY_PROXYITEM = "ProxyItem";
    private static final String TYPE_DOCUMENT_PROXY = "Document Proxy";
    private static final String SERVICE_3DSPACE = "3DSpace";
    private static final String SERVICE_3DRIVE = "3DDrive";
    private static final String SERVICE_ONEDRIVE = "onedrive";
    private static final String FOLDERTYPE_3DDRIVE = "DriveFolder";
    private static final String FOLDERTYPE_ONEDRIVE = "text/directory";
    private static final String RELATIONSHIP_DELIVERABLE = "Task Deliverable";
    private static final String RELATIONSHIP_REFERENCE = "Reference Document";
    private static final String RELATIONSHIP_REFERENCED_SIMULATIONS = "Referenced Simulations";
    private static final String PROXY_TENANT = "attribute[Proxy_Tenant].value";
    private static final String PROXY_SERVICE = "attribute[Proxy_Service].value";
    private static final String PROXY_ID = "attribute[Proxy_Id].value";
    private static final String PROXY_TITLE = "attribute[Proxy_Title].value";
    private static final String PROXY_URL = "attribute[Proxy_URL].value";
    private static final String SELECT_PHYSICALID = "physicalid";

    public TaskReferences() {
    }

    private static String createTaskProxyObject(Context var0, ServiceParameters var1, Dataobject var2) throws FoundationException {
        String var3 = var2.getType();
        String var4 = "Document Proxy";
        ProxyObject var5 = new ProxyObject();
        var5.setProxyId(var2.getId());
        var5.setProxyService(var2.getService());
        var5.setProxyTenant(var2.getTenant());
        DataelementMap var6 = var2.getDataelements();
        if (var6 != null && var6.size() > 0) {
            var5.setProxyTitle((String)var6.get("title"));
            var5.setProxyURL((String)var6.get("URL"));
        } else {
            var5.setProxyTitle(var2.getId());
            var5.setProxyURL("");
        }

        var5.createProxy(var0, var4);
        String var7 = var5.getNativeId();
        return var7;
    }

    private static String addTaskRelatedFile(Context var0, String var1, String var2, String var3, Map<String, String> var4) throws FoundationException {
        String var5 = ObjectEditUtil.connect(var0, var1, var3, var2, var4, false);
        return var5;
    }

    private static String updateTaskRelatedFile(Context var0, String var1, String var2, String var3, Map<String, String> var4) throws FoundationException {
        String var5 = ObjectEditUtil.connect(var0, var1, var3, var2, var4, false);
        return var5;
    }

    private static void removeTaskRelatedFile(Context var0, String var1, String var2, String var3) throws FoundationException {
        ObjectEditUtil.disconnect(var0, var1, var3, var2);
    }

    private static Dataobject getRelatedObject(Context var0, String var1, String var2, String var3, String var4) throws FoundationException {
        Dataobject var5 = new Dataobject();
        var5.setId(var1);
        ArrayList var6 = new ArrayList(6);
        var6.add(new Select("physicalid", "physicalid", ExpressionType.BUS, (Format)null, false));
        var6.add(new Select("attribute[Proxy_Tenant].value", "attribute[Proxy_Tenant].value", ExpressionType.BUS, (Format)null, false));
        var6.add(new Select("attribute[Proxy_Service].value", "attribute[Proxy_Service].value", ExpressionType.BUS, (Format)null, false));
        var6.add(new Select("attribute[Proxy_Id].value", "attribute[Proxy_Id].value", ExpressionType.BUS, (Format)null, false));
        var6.add(new Select("attribute[Proxy_Title].value", "attribute[Proxy_Title].value", ExpressionType.BUS, (Format)null, false));
        var6.add(new Select("attribute[Proxy_URL].value", "attribute[Proxy_URL].value", ExpressionType.BUS, (Format)null, false));
        ExpandData var7 = new ExpandData();
        var7.setGetTo(false);
        var7.setGetFrom(true);
        var7.setRelationshipPattern(var4);
        var7.setRecurseLevels("1");
        StringBuffer var8 = new StringBuffer();
        if (var3 != null && !var3.isEmpty() && !"3DSpace".equalsIgnoreCase(var3)) {
            var8.append("to[");
            var8.append(var4);
            var8.append("].to.");
            var8.append("attribute[Proxy_Service].value");
            var8.append("=='");
            var8.append(var3);
            var8.append("' && ");
            var8.append("to[");
            var8.append(var4);
            var8.append("].to.");
            var8.append("attribute[Proxy_Id].value");
            var8.append("=='");
            var8.append(var2);
            var8.append("'");
        } else {
            var8.append("to[");
            var8.append(var4);
            var8.append("].to.physicalid=='");
            var8.append(var2);
            var8.append("'");
        }

        var7.setObjectWhere(var8.toString());
        Datacollection var9 = ObjectUtil.expand(var0, var5, var7, var6);
        Dataobject var10 = null;
        if (var9 != null && !var9.getDataobjects().isEmpty()) {
            var10 = (Dataobject)var9.getDataobjects().get(0);
        }

        return var10;
    }

    public static Datacollections getRelatedFiles(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        ArgMap var3 = var2.getJpoArgs();
        String var4 = (String)var3.get("relationshiptype");
        Datacollection var5 = var2.getDatacollection();
        switch (var4) {
            case "Reference Document":
            case "Task Deliverable":
            default:
                List var6 = var2.getSelects();
                ArrayList var20 = new ArrayList(6);
                var20.add(new Select("attribute[Proxy_Tenant].value", "attribute[Proxy_Tenant].value", ExpressionType.BUS, (Format)null, false));
                var20.add(new Select("attribute[Proxy_Service].value", "attribute[Proxy_Service].value", ExpressionType.BUS, (Format)null, false));
                var20.add(new Select("attribute[Proxy_Id].value", "attribute[Proxy_Id].value", ExpressionType.BUS, (Format)null, false));
                var20.add(new Select("attribute[Proxy_Title].value", "attribute[Proxy_Title].value", ExpressionType.BUS, (Format)null, false));
                var20.add(new Select("attribute[Proxy_URL].value", "attribute[Proxy_URL].value", ExpressionType.BUS, (Format)null, false));
                var20.addAll(var6);
                ExpandData var8 = new ExpandData();
                var8.setGetTo(false);
                var8.setGetFrom(true);
                var8.setRelationshipPattern(var4);
                var8.setRecurseLevels("1");
                StringBuffer var9 = new StringBuffer();
                var8.setObjectWhere(var9.toString());
                Datacollections var10 = ObjectUtil.expand(var0, var5, var8, var20);
                Iterator var11 = var10.getDatacollections().iterator();

                while(var11.hasNext()) {
                    Datacollection var12 = (Datacollection)var11.next();

                    for(int var13 = 0; var13 < var12.getDataobjects().size(); ++var13) {
                        Dataobject var14 = (Dataobject)var12.getDataobjects().get(var13);
                        String var15 = (String)var14.getDataelements().get("attribute[Proxy_Tenant].value");
                        String var16 = (String)var14.getDataelements().get("attribute[Proxy_Service].value");
                        String var17 = (String)var14.getDataelements().get("attribute[Proxy_Id].value");
                        String var18 = (String)var14.getDataelements().get("attribute[Proxy_Title].value");
                        String var19 = (String)var14.getDataelements().get("attribute[Proxy_URL].value");
                        if (var17 != null && !var17.isEmpty()) {
                            var14.setId(var17);
                        }

                        if (var15 != null && !var15.isEmpty()) {
                            var14.setTenant(var15);
                        }

                        if (var16 != null && !var16.isEmpty()) {
                            var14.setService(var16);
                            if ("3DDrive".equalsIgnoreCase(var16)) {
                                var14.setType("DriveFile");
                                var14.getDataelements().put("hasfiles", "TRUE");
                            }
                        } else {
                            var14.setService("3DSpace");
                        }

                        var14.getDataelements().remove("attribute[Proxy_Tenant].value");
                        var14.getDataelements().remove("attribute[Proxy_Service].value");
                        var14.getDataelements().remove("attribute[Proxy_Id].value");
                        var14.getDataelements().remove("attribute[Proxy_Title].value");
                        var14.getDataelements().remove("attribute[Proxy_URL].value");
                        if (var17 != null && !var17.isEmpty()) {
                            var14.getDataelements().remove("revision");
                            var14.getDataelements().remove("policy");
                            var14.getDataelements().remove("stateNLS");
                            var14.setCestamp("");
                            var14.setRelId("");
                            var14.getDataelements().put("URL", var19);
                        }
                    }
                }

                return var10;
        }
    }

    public static Dataobject updateRelatedFiles(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        ArgMap var3 = var2.getJpoArgs();
        String var4 = (String)var3.get("relationshiptype");
        Datacollection var5 = var2.getDatacollection();
        boolean var6 = true;
        boolean var7 = false;
        boolean var8 = false;
        boolean var9 = false;
        boolean var10 = false;
        boolean var11 = false;
        Dataobject var12 = new Dataobject();
        String var13 = null;
        switch (var4) {
            case "Reference Document":
            case "Task Deliverable":
            case "Referenced Simulations":
                var12 = (Dataobject)var5.getDataobjects().get(0);
                String var14 = var12.getParent().getId();
                UpdateActions var18 = var12.getUpdateAction();
                String var16 = var12.getService();
                if (var16 != null) {
                    if ("3DDrive".equalsIgnoreCase(var16)) {
                        var8 = true;
                    }

                    if ("onedrive".equalsIgnoreCase(var16)) {
                        var7 = true;
                    }

                    if ("dropbox".equalsIgnoreCase(var16)) {
                        var10 = true;
                    }

                    if ("google2".equalsIgnoreCase(var16)) {
                        var9 = false;
                    }

                    var6 = false;
                }

                var13 = var12.getId();
                Dataobject var17 = getRelatedObject(var0, var14, var12.getId(), var12.getService(), var4);
                if (var17 != null) {
                    var13 = (String)var17.getDataelements().get("physicalid");
                    if (var13 != null && !var13.isEmpty()) {
                        var11 = true;
                    }
                }

                if (var11 && UpdateActions.MODIFY.equals(var18)) {
                    updateTaskRelatedFile(var0, var14, var13, var4, (Map)null);
                }

                if ((UpdateActions.CONNECT.equals(var18) || UpdateActions.CREATE.equals(var18)) && !var11) {
                    if (!var6) {
                        var13 = createTaskProxyObject(var0, var2, var12);
                    }

                    addTaskRelatedFile(var0, var14, var13, var4, (Map)null);
                }

                if (UpdateActions.DISCONNECT.equals(var18) || UpdateActions.DELETE.equals(var18)) {
                    removeTaskRelatedFile(var0, var14, var13, var4);
                }

                return var12;
            default:
                return var12;
        }
    }

    public static Datacollection getRelatedTasks(Context var0, String var1, String var2, List<Selectable> var3) throws FoundationException {
        String var4 = "reference".equalsIgnoreCase(var1) ? "Reference Document" : "Task Deliverable";
        ExpandData var5 = new ExpandData();
        var5.setGetTo(true);
        var5.setRelationshipPattern(var4);
        Dataobject var6 = new Dataobject();
        var6.setId(var2);
        Datacollection var7 = ObjectUtil.expand(var0, var6, var5, var3);
        return var7;
    }

    /** @deprecated */
    @Deprecated
    public static Datacollection getTaskReferences(Context var0, String var1, List<Selectable> var2, List<Selectable> var3, String var4, String var5) throws FoundationException {
        Dataobject var6 = new Dataobject();
        var6.setId(var1);
        ExpandData var7 = new ExpandData();
        var7.setTypePattern("*");
        var7.setRelationshipPattern("Reference Document");
        var7.setLimit(Short.valueOf((short)0));
        var7.setRecurseToLevel(BigDecimal.ONE);
        var7.setGetTo(false);
        var7.setGetFrom(true);
        var7.setObjectWhere(var4);
        var7.setRelationshipWhere(var5);
        ArrayList var8 = new ArrayList();
        var8.addAll(var2);
        var8.addAll(var3);
        return ObjectUtil.expand(var0, var6, var7, var8);
    }

    /** @deprecated */
    @Deprecated
    public static Datacollection getTaskDeliverables(Context var0, String var1, List<Selectable> var2, List<Selectable> var3, String var4, String var5) throws FoundationException {
        Dataobject var6 = new Dataobject();
        var6.setId(var1);
        ExpandData var7 = new ExpandData();
        var7.setTypePattern("*");
        var7.setRelationshipPattern("Task Deliverable");
        var7.setLimit(Short.valueOf((short)0));
        var7.setRecurseToLevel(BigDecimal.ONE);
        var7.setGetTo(false);
        var7.setGetFrom(true);
        var7.setObjectWhere(var4);
        var7.setRelationshipWhere(var5);
        ArrayList var8 = new ArrayList();
        var8.addAll(var2);
        var8.addAll(var3);
        return ObjectUtil.expand(var0, var6, var7, var8);
    }

    /** @deprecated */
    @Deprecated
    public static String addTaskReference(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        String var4 = ObjectEditUtil.connect(var0, var1, "Reference Document", var2, var3, false);
        return var4;
    }

    /** @deprecated */
    @Deprecated
    public static Dataobject addTaskReference(Context var0, String var1, Dataobject var2, Map<String, String> var3) throws FoundationException {
        Dataobject var4 = ObjectEditUtil.connect(var0, var1, "Reference Document", var2, var3, false, "3dspace", TaskService.ALLOWED_PROXY_SERVICES);
        return var4;
    }

    /** @deprecated */
    @Deprecated
    public static String addTaskDeliverable(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        String var4 = ObjectEditUtil.connect(var0, var1, "Task Deliverable", var2, var3, false);
        return var4;
    }

    /** @deprecated */
    @Deprecated
    public static void removeTaskReference(Context var0, String var1, String var2) throws FoundationException {
        ObjectEditUtil.disconnect(var0, var1, "Reference Document", var2);
    }

    /** @deprecated */
    @Deprecated
    public static void removeTaskReference(Context var0, String var1, Dataobject var2) throws FoundationException {
        ObjectEditUtil.disconnect(var0, var1, "Reference Document", var2, "3dspace");
    }

    /** @deprecated */
    @Deprecated
    public static void removeTaskDeliverable(Context var0, String var1, String var2) throws FoundationException {
        ObjectEditUtil.disconnect(var0, var1, "Task Deliverable", var2);
    }

    /** @deprecated */
    @Deprecated
    public static void removeTaskReference(Context var0, String var1) throws FoundationException {
        ObjectEditUtil.disconnect(var0, var1);
    }
}
