package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.service.ServiceResource;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiClassInfo;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiMethodInfo;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiMethodInfo.Visibility;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiServiceParameter;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiServiceParameter.ParameterDataType;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiServiceParameter.ParameterType;
import com.dassault_systemes.platform.restServices.ModelerBase;
import com.dassault_systemes.platform.ven.apache.commons.net.util.Base64;
import com.matrixone.servlet.Framework;
import matrix.util.Mime64;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Locale;

@ApiClassInfo(
        title = "Project Rest Services",
        schema = "projects",
        version = "1.2.0",
        description = "Rest Services for managing the Project modeler.",
        tag = "Project"
)
@ApplicationPath("/resources/v1/modeler/gscprojects")
public class gscProjectModeler extends ModelerBase {
    public static final String DPM_Service = "dpm.gscprojects/";

    public Class[] getServices() {
        return new Class[]{gscProjectModeler.class};
    }

    @GET
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get all users Projects.",
            schema = "projects",
            exposition = Visibility.Public
    )
    public static Response getProjects(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        authenticate(httpServletRequest);
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + path, (MultivaluedMap)null, (String)null);
    }
    public static void authenticate(HttpServletRequest httpServletRequest) {
        try {
            String odtName = httpServletRequest.getHeader("authorization");
            if (odtName != null && odtName.length() > "Basic ".length()) {
                try {
                    odtName = odtName.substring("Basic ".length());
                    String[] var7 = (new String(Mime64.decode(odtName))).split(":");
                    String var8 = var7[0].replaceAll("%3A", ":");
                    String[] var9 = var8.split("@SC@");
                    String userId = var9[0];
                    String secrityContext = "";
                    if (var9.length > 1) {
                        secrityContext = var9[1];
                        secrityContext = secrityContext.startsWith("ctx::") ? secrityContext : "ctx::" + secrityContext;
                    }

                    String password = "";
                    if (var7.length > 1) {
                        password = var7[1];
                    }

                    matrix.db.Context context = new matrix.db.Context("");
                    context.setUser(userId);
                    context.setPassword(password);
                    context.setLocale(new Locale("en-US"));
                    context.setRole(secrityContext);
                    context.connect();
                    Framework.setContext(httpServletRequest.getSession(), context);
                }
                catch(Exception var13) {
                    var13.printStackTrace();
                }
            }
        } catch (Exception var14) {
            var14.printStackTrace();
        }
    }
    @POST
    @Path("/background")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Create projects and related data.",
            schema = "projects",
            schemaBody = "projects",
            exposition = Visibility.Public
    )
    public static Response createProjectInBackground(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String var3 = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + var3, (MultivaluedMap)null, var2);
    }

    @POST
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Create projects and related data.",
            schema = "projects",
            schemaBody = "projects",
            exposition = Visibility.Public
    )
    public static Response createProjects(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String var3 = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + var3, (MultivaluedMap)null, var2);
    }

    @PUT
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Update projects and related data.",
            schema = "projects",
            schemaBody = "projects",
            exposition = Visibility.Public
    )
    public static Response updateProjects(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String var3 = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + var3, (MultivaluedMap)null, var2);
    }

    /** @deprecated */
    @Deprecated
    @DELETE
    @Path("")
    @Produces({"application/json"})
    public static Response deleteProjects(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, var2);
    }

    @GET
    @Path("{projectId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get a specific Project detail and related data.",
            schema = "projects",
            exposition = Visibility.Public
    )
    public static Response getProjectInfo(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        authenticate(httpServletRequest);
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, (String)null);
    }

    @PUT
    @Path("{projectId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Update projects and related data.",
            schema = "projects",
            schemaBody = "projects",
            exposition = Visibility.Public
    )
    public static Response updateProjectInfo(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, var2);
    }

    @DELETE
    @Path("{projectId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Delete the given project.",
            exposition = Visibility.Public
    )
    public static Response deleteProject(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String var3 = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + var3, (MultivaluedMap)null, var2);
    }

    @GET
    @Path("{projectId}/issues")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get Project Issues for a given project.",
            schema = "issues",
            exposition = Visibility.Public
    )
    public static Response getProjectIssues(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String var2 = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + var2, (MultivaluedMap)null, (String)null);
    }

    @GET
    @Path("{projectId}/issues/{issueId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get the issue details for a given project & issue id.",
            schema = "issues",
            exposition = Visibility.Public
    )
    public static Response getProjectIssueInfo(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, (String)null);
    }

    @GET
    @Path("{projectId}/milestones")
    @Produces({"application/json"})
    public static Response getProjectMilestones(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, "dpm.gscprojects/" + var2, (MultivaluedMap)null, (String)null);
    }

    @POST
    @Path("{projectId}/milestones")
    @Produces({"application/json"})
    public static Response updateProjectMilestones(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, "dpm.gscprojects/" + var3, (MultivaluedMap)null, var2);
    }

    /** @deprecated */
    @Deprecated
    @GET
    @Path("{projectId}/issuesDetail/{issueId}")
    @Produces({"application/json"})
    public static Response getProjectIssuesDetail(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, "dpm.gscprojects/" + var2, (MultivaluedMap)null, (String)null);
    }

    /** @deprecated */
    @Deprecated
    @GET
    @Path("{projectId}/opportunities")
    @Produces({"application/json"})
    public static Response getProjectOpportunities(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, "dpm.gscprojects/" + var2, (MultivaluedMap)null, (String)null);
    }

    /** @deprecated */
    @Deprecated
    @GET
    @Path("{projectId}/allopportunities")
    @Produces({"application/json"})
    public static Response getProjectOpportunitiesAll(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, (String)null);
    }

    /** @deprecated */
    @Deprecated
    @GET
    @Path("{projectId}/opportunities/{opportunityId}")
    @Produces({"application/json"})
    public static Response getProjectOpportunityInfo(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, (String)null);
    }

    @GET
    @Path("{projectId}/risks")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get the risks associated directly to a given project.",
            schema = "risks",
            exposition = Visibility.Public,
            tags = {"Risks"}
    )
    public static Response getProjectRisks(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, (String)null);
    }

    /** @deprecated */
    @Deprecated
    @GET
    @Path("{projectId}/allrisks")
    @Produces({"application/json"})
    public static Response getProjectRisksAll(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, (String)null);
    }

    @GET
    @Path("{projectId}/risks/{riskId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get the risk details for a given project & risk id.",
            schema = "risks",
            exposition = Visibility.Public,
            tags = {"Risks"}
    )
    public static Response getProjectRiskInfo(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String var2 = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + var2, (MultivaluedMap)null, (String)null);
    }

    /** @deprecated */
    @Deprecated
    @GET
    @Path("{projectId}/risksDetail/{riskId}")
    @Produces({"application/json"})
    public static Response getProjectRisksDetail(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, (String)null);
    }

    @GET
    @Path("programId/{programId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get the projects for a given program id",
            schema = "projects",
            exposition = Visibility.Public
    )
    public static Response getProgramProjects(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, (String)null);
    }

    @GET
    @Path("{projectId}/folders")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get the folders for a given project id.",
            schema = "folders",
            exposition = Visibility.Public,
            tags = {"Folders"}
    )
    public static Response getWorkspaceVaults(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, (String)null);
    }

    @PUT
    @Path("{projectId}/folders")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Update the folders for a given project id.",
            schema = "folders",
            schemaBody = "folders",
            exposition = Visibility.Public,
            tags = {"Folders"}
    )
    public static Response updateProjectWorkspaceVaults(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, var2);
    }

    @POST
    @Path("{projectId}/folders")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Create the folders for a given project id.",
            schema = "folders",
            schemaBody = "folders",
            exposition = Visibility.Public,
            tags = {"Folders"}
    )
    public static Response addProjectWorkspaceVaults(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, var2);
    }

    @DELETE
    @Path("{projectId}/folders/{folderId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Delete a given folder based on the given project & folder id.",
            exposition = Visibility.Public,
            tags = {"Folders"}
    )
    public static Response deleteProjectWorkspaceVaults(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, var2);
    }

    @GET
    @Path("folderId/{folderId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get the information for a given folder object associated with any project.",
            schema = "folders",
            exposition = Visibility.Public,
            tags = {"Folders"}
    )
    public static Response getWorkspaceFolder(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, (String)null);
    }

    @GET
    @Path("{projectId}/assessments")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get the latest Assessment for a given Project.",
            schema = "assessments",
            exposition = Visibility.Public,
            tags = {"Assessments"},
            parameters = {@ApiServiceParameter(
                    name = "allAssessments",
                    description = "Optionally retrieve all Assessments for the Project. (Default: false)",
                    paramType = ParameterType.Query,
                    dataType = ParameterDataType.String,
                    required = false,
                    deprecated = false
            )}
    )
    public static Response getAssessments(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, (String)null, false);
    }

    @POST
    @Path("ids/assessments")
    @Produces({"application/json"})
    @Consumes({"application/x-www-form-urlencoded"})
    public static Response getRisks(@Context HttpServletRequest httpServletRequest, MultivaluedMap multivaluedMap) throws FoundationException {
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/", multivaluedMap, (String)null);
    }

    @PUT
    @Path("{projectId}/assessments")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Update the Assessments for a given Project.",
            schema = "assessments",
            schemaBody = "assessments",
            exposition = Visibility.Public,
            tags = {"Assessments"}
    )
    public static Response updateProjectAssessments(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, var2, false);
    }

    @POST
    @Path("{projectId}/assessments")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Create an Assessment for a given Project.",
            schema = "assessments",
            schemaBody = "assessments",
            exposition = Visibility.Public,
            tags = {"Assessments"}
    )
    public static Response addProjectAssessments(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String s) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, s, false);
    }

    @DELETE
    @Path("{projectId}/assessments/{assessmentId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Delete an Assessment for a given Project.",
            exposition = Visibility.Public,
            tags = {"Assessments"}
    )
    public static Response deleteProjectAssessments(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, var2, false);
    }

    @GET
    @Path("{projectId}/assessments/{assessmentId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get the information for a given Assessment.",
            schema = "assessments",
            exposition = Visibility.Public,
            tags = {"Assessments"}
    )
    public static Response getprojectAssessment(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, (String)null, false);
    }

    @PUT
    @Path("{projectId}/assessments/{assessmentId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Update an Assessment for a given Project.",
            schema = "assessments",
            schemaBody = "assessments",
            exposition = Visibility.Public,
            tags = {"Assessments"}
    )
    public static Response updateProjectAssessmentsById(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, var2, false);
    }
	
	@GET
    @Path("{projectId}/budget")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get the information for a given gscProjectBudgetIf.",
            schema = "gscProjectBudgetIf",
            exposition = Visibility.Public,
            tags = {"gscProjectBudgetIf"}
    )
    public static Response getgscProjectBudgetIf(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, (String)null, false);
    }

    @GET
    @Path("{projectId}/invest")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get the information for a given gscProjectInvestIf.",
            schema = "gscProjectInvestIf",
            exposition = Visibility.Public,
            tags = {"gscProjectInvestIf"}
    )
    public static Response getgscProjectInvestIf(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, (String)null, false);
    }

    @GET
    @Path("help")
    @Produces({"application/json"})
    public static Response generateHelpDocumentation(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        return ServiceResource.processServiceHelpRequest(httpServletRequest, com.dassault_systemes.enovia.dpm.ProjectModeler.class, "dpm.gscprojects/");
    }

    @GET
    @Path("search")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Search Projects using the provided input string.",
            schema = "projects",
            exposition = Visibility.Public,
            tags = {"Search"},
            parameters = {@ApiServiceParameter(
                    name = "searchStr",
                    description = "Search Criteria; 2 or more characters required.",
                    paramType = ParameterType.Query,
                    dataType = ParameterDataType.String,
                    required = true,
                    deprecated = false
            ), @ApiServiceParameter(
                    name = "$top",
                    description = "The number of objects to return; default 50; max 1000",
                    paramType = ParameterType.Query,
                    dataType = ParameterDataType.Integer,
                    required = false,
                    deprecated = false
            ), @ApiServiceParameter(
                    name = "$skip",
                    description = "The number of objects to skip in case of paginating through the serach results; default 0",
                    paramType = ParameterType.Query,
                    dataType = ParameterDataType.Integer,
                    required = false,
                    deprecated = false
            )}
    )
    public static Response searchProjects(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojects/" + path, (MultivaluedMap)null, (String)null);
    }
    public static String getSecurityContextForRestClient(String userId,String password,String securityContext)
    {
        String str = Base64.encodeBase64String((userId+"@SC@"+securityContext+":"+password).getBytes());
        return str;
    }

    public static void main(String[] args) throws Exception{
        String str = getSecurityContextForRestClient("admin_platform","Qwer1234","ctx::VPLMAdministrator.Company Name.RPMS");
        System.out.println("|"+str+"|");
        String authentication = com.matrixone.client.fcs.Mime64.encode("admin_platform@SC@VPLMProjectAdministrator.Company Name.RPMS:Qwer1234".getBytes());
        System.out.println("authentication = " + authentication);
        String odtName = "Basic " + authentication;
        odtName = odtName.substring("Basic ".length());
        String[] var7 = (new String(Mime64.decode(odtName))).split(":");
        String var8 = var7[0].replaceAll("%3A", ":");
        String[] var9 = var8.split("@SC@");
        String userId = var9[0];
        String securityContext = "";
        if (var9.length > 1) {
            securityContext = var9[1];
            securityContext = securityContext.startsWith("ctx::") ? securityContext : "ctx::" + securityContext;
        }

        String password = "";
        if (var7.length > 1) {
            password = var7[1];
        }
        System.out.println("userId = " + userId);
        System.out.println("securityContext = " + securityContext);
        System.out.println("password = " + password);
    }
}
