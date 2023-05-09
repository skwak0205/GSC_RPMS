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
        title = "BusinessGoal Rest Services",
        schema = "businessgoals",
        version = "1.0.0",
        description = "Rest Services for managing the BusinessGoal modeler.",
        tag = "BusinessGoal"
)
@ApplicationPath("/resources/v1/modeler/gscbusinessgoals")
public class gscBusinessGoalModeler extends ModelerBase {
    public static final String PGM_Service = "dpm.gscbusinessgoals/";

    public Class[] getServices() {
        return new Class[]{gscBusinessGoalModeler.class};
    }

    @GET
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get all users BusinessGoals.",
            schema = "businessgoals",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getBusinessGoals(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var2, (MultivaluedMap)null, (String)null);
    }

    @POST
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Create BusinessGoals.",
            schema = "businessgoals",
            schemaBody = "businessgoals",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response createBusinessGoals(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap)null, var2);
    }

    @PUT
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Update businessgoals and related data.",
            schema = "businessgoals",
            schemaBody = "businessgoals",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response updateBusinessGoals(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String var3 = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, PGM_Service + var3, (MultivaluedMap)null, var2);
    }

    @GET
    @Path("{businessgoalId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get a specific BusinessGoal detail and related data.",
            schema = "businessgoals",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getBusinessGoalInfo(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var2, (MultivaluedMap)null, (String)null);
    }

    @PUT
    @Path("{businessgoalId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Update a BusinessGoal and its related data.",
            schema = "businessgoals",
            schemaBody = "businessgoals",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response updateBusinessGoalInfo(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap)null, var2);
    }

    @DELETE
    @Path("{businessgoalId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Delete the given BusinessGoal if it does not contain Projects.",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response deleteBusinessGoal(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap)null, var2);
    }

    @GET
    @Path("{businessgoalId}/projects")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get a list of Projects associated to specific BusinessGoal.",
            schema = "projects",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getBusinessGoalProjects(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var2, (MultivaluedMap)null, (String)null);
    }

    @PUT
    @Path("{businessgoalId}/projects")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Add list of Projects associated to specific BusinessGoal.",
            schema = "projects",
            schemaBody = "projects",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response addBusinessGoalProjects(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap)null, var2);
    }

    @DELETE
    @Path("{businessgoalId}/projects")
    @Produces({"application/json"})
    public static Response removeBusinessGoalProjects(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap)null, var2);
    }

    @GET
    @Path("help")
    @Produces({"application/json"})
    public static Response generateHelpDocumentation(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        return ServiceResource.processServiceHelpRequest(var0, gscBusinessGoalModeler.class, PGM_Service);
    }

    @GET
    @Path("search")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Search BusinessGoals using the provided input string.",
            schema = "businessgoals",
            schemaBody = "businessgoals",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response searchBusinessGoals(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap)null, (String)null);
    }
}
