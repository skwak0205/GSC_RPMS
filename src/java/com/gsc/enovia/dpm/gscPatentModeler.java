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
        title = "Patent Rest Services",
        schema = "patents",
        version = "1.0.0",
        description = "Rest Services for managing the Patent modeler.",
        tag = "Patent"
)
@ApplicationPath("/resources/v1/modeler/gscpatents")
public class gscPatentModeler extends ModelerBase {
    public static final String PGM_Service = "dpm.gscpatents/";

    public Class[] getServices() {
        return new Class[]{gscPatentModeler.class};
    }

    @GET
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get all users Patents.",
            schema = "patents",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getPatents(@Context HttpServletRequest context, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(context, PGM_Service + path, (MultivaluedMap) null, (String) null);
    }

    @POST
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Create Patents.",
            schema = "patents",
            schemaBody = "patents",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response createPatents(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap) null, var2);
    }

    @GET
    @Path("{businessgoalId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get a specific Patent detail and related data.",
            schema = "patents",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getPatentInfo(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var2, (MultivaluedMap) null, (String) null);
    }

    @PUT
    @Path("{businessgoalId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Update a Patent and its related data.",
            schema = "patents",
            schemaBody = "patents",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response updatePatentInfo(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap) null, var2);
    }

    @DELETE
    @Path("{businessgoalId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Delete the given Patent if it does not contain Projects.",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response deletePatent(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap) null, var2);
    }

    @GET
    @Path("{businessgoalId}/projects")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get a list of Projects associated to specific Patent.",
            schema = "projects",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getPatentProjects(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var2, (MultivaluedMap) null, (String) null);
    }

    @PUT
    @Path("{businessgoalId}/projects")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Add list of Projects associated to specific Patent.",
            schema = "projects",
            schemaBody = "projects",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response addPatentProjects(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap) null, var2);
    }

    @DELETE
    @Path("{businessgoalId}/projects")
    @Produces({"application/json"})
    public static Response removePatentProjects(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap) null, var2);
    }

    @GET
    @Path("help")
    @Produces({"application/json"})
    public static Response generateHelpDocumentation(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        return ServiceResource.processServiceHelpRequest(var0, gscPatentModeler.class, PGM_Service);
    }

    @GET
    @Path("search")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Search Patents using the provided input string.",
            schema = "patents",
            schemaBody = "patents",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response searchPatents(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap) null, (String) null);
    }
}
