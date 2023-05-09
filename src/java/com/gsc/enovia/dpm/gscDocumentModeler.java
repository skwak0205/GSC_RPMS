package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.service.ServiceResource;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiClassInfo;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiMethodInfo;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiServiceParameter;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiMethodInfo.Visibility;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiServiceParameter.ParameterDataType;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiServiceParameter.ParameterType;
import com.dassault_systemes.platform.restServices.ModelerBase;
import com.dassault_systemes.platform.ven.apache.commons.net.util.Base64;
import com.matrixone.servlet.Framework;
import matrix.util.MatrixException;
import matrix.util.Mime64;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Locale;

@ApiClassInfo(
        title = "Document Rest Services",
        schema = "documents",
        version = "1.2.0",
        description = "Rest Services for managing the Project modeler.",
        tag = "Project"
)
@ApplicationPath("/resources/v1/modeler/gscdocuments")
public class gscDocumentModeler extends ModelerBase {
    public static final String DPM_Service = "dpm.gscdocuments/";

    public Class[] getServices() {
        return new Class[]{gscDocumentModeler.class};
    }

    @GET
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get all users Documents.",
            schema = "projects",
            exposition = Visibility.Public
    )
    public static Response getDocuments(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
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
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + path, (MultivaluedMap)null, (String)null);
    }


    /** @deprecated */
    @Deprecated
    @DELETE
    @Path("")
    @Produces({"application/json"})
    public static Response deleteProjects(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscdocuments/" + path, (MultivaluedMap)null, var2);
    }

    @GET
    @Path("{documentId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get a specific Project detail and related data.",
            schema = "documents",
            exposition = Visibility.Public
    )
    public static Response getProjectInfo(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
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
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscdocuments/" + path, (MultivaluedMap)null, (String)null);
    }


    @DELETE
    @Path("{documentId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Delete the given project.",
            exposition = Visibility.Public
    )
    public static Response deleteDocument(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String var3 = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscdocuments/" + var3, (MultivaluedMap)null, var2);
    }


    @GET
    @Path("search")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Search Documents using the provided input string.",
            schema = "documents",
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
    public static Response searchDocuments(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscdocuments/" + path, (MultivaluedMap)null, (String)null);
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
