package com.gsc.enovia.common;


import com.dassault_systemes.platform.restServices.RestService;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.program.ProgramCentralConstants;
import com.matrixone.json.JSONArray;
import com.matrixone.json.JSONObject;
import com.matrixone.vplm.posmodel.VPLMSecurityContext;
import java.io.InputStream;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import matrix.db.Context;
import matrix.util.StringList;

@Path("/project")
public class gscDashboard extends RestService {
    public gscDashboard() {
    }

    @GET
    @POST
    @Path("/getList")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response getProjectData(@QueryParam("programId") String programId) throws Exception {
        String outputStr = "";
        new JSONObject();
        Context context = null;

        try {
            Properties pro = this.getProperties("emxFrameworkCustom.properties");
            String ContextURL = pro.getProperty("emxFramework.dsk.Dashboard.ContextURL");
            String ContextUser = pro.getProperty("emxFramework.dsk.Dashboard.ContextUser");
            String ContextPassword = pro.getProperty("emxFramework.dsk.Dashboard.ContextPassword");
            String whereCurrent = pro.getProperty("emxFramework.dsk.Dashboard.whereCurrent");
            context = new Context(ContextURL);
            context.setUser(ContextUser);
            context.setPassword(ContextPassword);
            context.setVault("eService Production");
            context.connect();
            JSONArray array = new JSONArray();
            System.out.println("programId: " + programId);
            if (!programId.equals("")) {
                ContextUtil.pushContext(context);
                DomainObject program = new DomainObject(programId);
                StringList busSelects = new StringList();
                StringList relSelects = new StringList();
                busSelects.add("id");
                busSelects.add("type");
                busSelects.add("name");
                busSelects.add("policy");
                busSelects.add("current");
                busSelects.add("owner");
                busSelects.add("attribute[Task Estimated Start Date]");
                busSelects.add("attribute[Task Estimated Finish Date]");
                busSelects.add("attribute[dskItem]");
                busSelects.add("attribute[Percent Complete]");
                String where = "";
                if (whereCurrent != null && whereCurrent.length() > 0) {
                    where = "(current!=" + whereCurrent + ")";
                }

                MapList mapList = program.getRelatedObjects(context, ProgramCentralConstants.RELATIONSHIP_PROGRAM_PROJECT, ProgramCentralConstants.TYPE_PROJECT_SPACE, busSelects, relSelects, false, true, (short)1, where, (String)null);
                ContextUtil.popContext(context);
                System.out.println("mapList size: " + mapList.size());
                ListIterator itr = mapList.listIterator();

                while(itr.hasNext()) {
                    JSONObject json = new JSONObject();
                    array.put(json);
                    Map space = (Map)itr.next();
                    String oid = (String)space.get("id");
                    String sName = (String)space.get("name");
                    String sCurrent = (String)space.get("current");
                    String slItem = (String)space.get("attribute[dskItem]");
                    String sProgress = (String)space.get("attribute[Percent Complete]");
                    String sDate = (String)space.get("attribute[Task Estimated Start Date]");
                    String month = sDate.substring(0, sDate.indexOf("/"));
                    if (month == null) {
                        month = "1";
                    }

                    json.put("target", slItem);
                    json.put("objectId", oid);
                    json.put("month", month);
                    json.put("name", sName);
                    json.put("progress", sProgress);
                    json.put("current", sCurrent);
                    System.out.println("json: " + json.toString());
                }
            }

            outputStr = array.toString();
        } catch (Exception var26) {
            var26.printStackTrace();
        }

        System.out.println("JSONObject: " + outputStr);
        return Response.status(200).entity(outputStr).build();
    }

    @GET
    @POST
    @Path("/getProgramList")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response getProgramData(@QueryParam("programId") String programId) throws Exception {
        String outputStr = "";
        new JSONObject();
        Context context = null;

        try {
            Properties pro = this.getProperties("emxFrameworkCustom.properties");
            String ContextURL = pro.getProperty("emxFramework.dsk.Dashboard.ContextURL");
            String ContextUser = pro.getProperty("emxFramework.dsk.Dashboard.ContextUser");
            String ContextPassword = pro.getProperty("emxFramework.dsk.Dashboard.ContextPassword");
            context = new Context(ContextURL);
            context.setUser(ContextUser);
            context.setPassword(ContextPassword);
            context.connect();
            JSONArray array = new JSONArray();
            StringList objectSelects = new StringList(2);
            objectSelects.addElement("id");
            objectSelects.addElement("name");
            MapList mapList = DomainObject.findObjects(context, "Program", "*", (String)null, objectSelects);
            ListIterator itr = mapList.listIterator();

            while(itr.hasNext()) {
                JSONObject json = new JSONObject();
                array.put(json);
                Map space = (Map)itr.next();
                String sObjectId = (String)space.get("id");
                String sName = (String)space.get("name");
                json.put("name", sName);
                json.put("objectId", sObjectId);
            }

            outputStr = array.toString();
        } catch (Exception var17) {
            var17.printStackTrace();
        }

        System.out.println(outputStr);
        return Response.status(200).entity(outputStr).build();
    }

    private Context getLoginContext(HttpServletRequest request) throws Exception {
        Context context = this.authenticate(request);
        String sRole = context.getRole();
        if (sRole == null || sRole.isEmpty()) {
            String sPreferredCtx = VPLMSecurityContext.getPreferredSecurityContext(context, context.getUser());
            if (sPreferredCtx != null && !sPreferredCtx.isEmpty()) {
                sPreferredCtx = "ctx::" + sPreferredCtx;
                context.resetRole(sPreferredCtx);
            } else {
                System.out.println("No preferred security context defined for person " + context.getUser());
            }
        }

        return context;
    }

    public Properties getProperties(String fileName) {
        Properties pro = new Properties();
        String resource = fileName;

        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(resource);
            pro.load(is);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return pro;
    }
}
