package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.service.ServiceResource;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiClassInfo;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiMethodInfo;
import com.dassault_systemes.platform.restServices.ModelerBase;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@ApiClassInfo(
        title = "ResourceRequest Rest Services",
        schema = "resourcerequests",
        version = "1.0.0",
        description = "Rest Services for managing the ResourceRequest modeler.",
        tag = "ResourceRequest"
)
@ApplicationPath("/resources/v1/modeler/gscresourcerequests")
public class gscResourceRequestModeler extends ModelerBase {
    public static final String PGM_Service = "dpm.gscresourcerequests/";

    public Class[] getServices() {
        return new Class[]{gscResourceRequestModeler.class};
    }

    @GET
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get all users ResourceRequests.",
            schema = "resourcerequests",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getResourceRequests(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var2, (MultivaluedMap) null, (String) null);
    }

    @POST
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Create ResourceRequests.",
            schema = "resourcerequests",
            schemaBody = "resourcerequests",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response createResourceRequests(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap) null, var2);
    }

    @GET
    @Path("{businessgoalId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get a specific ResourceRequest detail and related data.",
            schema = "resourcerequests",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getResourceRequestInfo(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var2, (MultivaluedMap) null, (String) null);
    }

    @PUT
    @Path("{businessgoalId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Update a ResourceRequest and its related data.",
            schema = "resourcerequests",
            schemaBody = "resourcerequests",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response updateResourceRequestInfo(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap) null, var2);
    }

    @DELETE
    @Path("{businessgoalId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Delete the given ResourceRequest if it does not contain Projects.",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response deleteResourceRequest(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap) null, var2);
    }

    @GET
    @Path("{businessgoalId}/projects")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get a list of Projects associated to specific ResourceRequest.",
            schema = "projects",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getResourceRequestProjects(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var2, (MultivaluedMap) null, (String) null);
    }

    @PUT
    @Path("{businessgoalId}/projects")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Add list of Projects associated to specific ResourceRequest.",
            schema = "projects",
            schemaBody = "projects",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response addResourceRequestProjects(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap) null, var2);
    }

    @DELETE
    @Path("{businessgoalId}/projects")
    @Produces({"application/json"})
    public static Response removeResourceRequestProjects(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap) null, var2);
    }

    @GET
    @Path("help")
    @Produces({"application/json"})
    public static Response generateHelpDocumentation(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        return ServiceResource.processServiceHelpRequest(var0, gscResourceRequestModeler.class, PGM_Service);
    }

    @GET
    @Path("search")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Search ResourceRequests using the provided input string.",
            schema = "resourcerequests",
            schemaBody = "resourcerequests",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response searchResourceRequests(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap) null, (String) null);
    }
}
