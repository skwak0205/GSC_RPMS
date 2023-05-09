package com.gsc.enovia.dpm.projecttemplate;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.service.ServiceResource;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiClassInfo;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiMethodInfo;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiMethodInfo.Visibility;
import com.dassault_systemes.platform.restServices.ModelerBase;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ApplicationPath;
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

@ApiClassInfo(
        title = "Project Template Rest Services",
        schema = "projecttemplates",
        version = "1.2.0",
        description = "Rest Services for managing the Project Template modeler.",
        tag = "Project Template"
)
@ApplicationPath("/resources/v1/modeler/gscprojecttemplates")
public class ProjectTemplateModeler extends ModelerBase {
    public static final String DPM_Service = "dpm.gscprojecttemplates/";

    public Class[] getServices() {
        return new Class[]{ProjectTemplateModeler.class};
    }

    @GET
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get all users Project Templates.",
            schema = "projecttemplates",
            exposition = Visibility.Public
    )
    public static Response getProjectTemplates(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + path, (MultivaluedMap)null, (String)null);
    }

    @GET
    @Path("{projecttemplateId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get a specific Project Template detail and related data.",
            schema = "projecttemplates",
            exposition = Visibility.Public
    )
    public static Response getProjectTemplateInfo(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + path, (MultivaluedMap)null, (String)null);
    }

    @POST
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Create Project Templates and related data.",
            schema = "projecttemplates",
            schemaBody = "projecttemplates",
            exposition = Visibility.Public
    )
    public static Response createProjectTemplates(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + path, (MultivaluedMap)null, var2);
    }

    @PUT
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Update Project Templates and related data.",
            schema = "projecttemplates",
            schemaBody = "projecttemplates",
            exposition = Visibility.Public
    )
    public static Response updateProjectTemplates(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String var3 = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + var3, (MultivaluedMap)null, var2);
    }

    @PUT
    @Path("{projecttemplateId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Update Project Templates and related data.",
            schema = "projecttemplates",
            schemaBody = "projecttemplates",
            exposition = Visibility.Public
    )
    public static Response updateProjectTemplateInfo(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, DPM_Service + var3, (MultivaluedMap)null, var2);
    }

    @DELETE
    @Path("{projecttemplateId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Delete the given Project Template.",
            exposition = Visibility.Public
    )
    public static Response deleteProjectTemplate(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, DPM_Service + var3, (MultivaluedMap)null, var2);
    }
}
