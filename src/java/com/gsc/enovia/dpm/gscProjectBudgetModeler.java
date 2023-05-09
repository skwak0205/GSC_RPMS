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
        title = "Project Budget Rest Services",
        schema = "projectbudgets",
        version = "1.0.0",
        description = "Rest Services for managing the Project Budget modeler.",
        tag = "Project"
)
@ApplicationPath("/resources/v1/modeler/gscprojectbudgets")
public class gscProjectBudgetModeler extends ModelerBase {
    public static final String DPM_Service = "dpm.gscprojectbudgets/";

    public Class[] getServices() {
        return new Class[]{gscProjectBudgetModeler.class};
    }
    @GET
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get all users Project Budgets.",
            schema = "projectbudgets",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getAllBudgets(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + path, (MultivaluedMap)null, (String)null);
    }
    @GET
    @Path("{physicalid}")
    @Produces({"application/json"})
    @Consumes({"application/x-www-form-urlencoded"})
    public static Response getProjectBudgets(@Context HttpServletRequest var0, MultivaluedMap multivaluedMap) throws FoundationException {
        return ServiceResource.processServiceRequest(var0, DPM_Service, multivaluedMap, (String)null);
    }

    @POST
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Create project budgets and related data.",
            schema = "projectbudgets",
            schemaBody = "projectbudgets",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response createProjectBudgets(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String var3 = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + var3, (MultivaluedMap)null, var2);
    }

    @PUT
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Update project Budgets and related data.",
            schema = "projectbudgets",
            schemaBody = "projectbudgets",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response updateProjectBudgets(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String var3 = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojectbudgets/" + var3, (MultivaluedMap)null, var2);
    }

    @PUT
    @Path("/state")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Update project Budgets and related data.",
            schema = "projectbudgets",
            schemaBody = "projectbudgets",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response updateProjectBudgetState(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String var3 = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojectbudgets/" + var3, (MultivaluedMap)null, var2);
    }

    @DELETE
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Delete project Budgets and related data.",
            schema = "projectbudgets",
            schemaBody = "projectbudgets",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response deleteProjectBudgets(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String var3 = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, "dpm.gscprojectbudgets/" + var3, (MultivaluedMap)null, var2);
    }

}
