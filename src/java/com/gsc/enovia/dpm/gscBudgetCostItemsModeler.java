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
        title = "Project Cost Item Rest Services",
        schema = "projectcostitems",
        version = "1.0.0",
        description = "Rest Services for managing the Project Budget modeler.",
        tag = "Project"
)
@ApplicationPath("/resources/v1/modeler/gscbudgetcostitems")
public class gscBudgetCostItemsModeler extends ModelerBase {
    public static final String DPM_Service = "dpm.gscbudgetcostitems/";

    @Override
    public Class<?>[] getServices()  {
        return new Class[]{gscBudgetCostItemsModeler.class};
    }

    @GET
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get all users Budget Cost Items.",
            schema = "projectcostitems",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getAllCostItems(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + path, (MultivaluedMap)null, (String)null);
    }

    @GET
    @Path("{physicalid}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get a Budget's Cost Items.",
            schema = "projectcostitems",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getBudgetCostItems(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + path, (MultivaluedMap)null, (String)null);
    }

    @GET
    @Path("/project/{physicalid}/expense")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get a Project's Expense Cost Items.",
            schema = "projectcostitems",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getProjectExpense(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + path, (MultivaluedMap)null, (String)null);
    }

    @GET
    @Path("/project/{physicalid}/investment")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get a Project's Investment Cost Items.",
            schema = "projectcostitems",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getProjectInvestment(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + path, (MultivaluedMap)null, (String)null);
    }


    @GET
    @Path("{physicalid}/expense")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get all users Budget Cost Items of Expense.",
            schema = "projectcostitems",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getBudgetExpense(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + path, (MultivaluedMap)null, (String)null);
    }

    @GET
    @Path("/investment")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get all users Budget Cost  of Investment.",
            schema = "projectcostitems",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getAllInvestment(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + path, (MultivaluedMap)null, (String)null);
    }

    @GET
    @Path("{physicalid}/investment")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get all users Budget Cost  of Investment.",
            schema = "projectcostitems",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getBudgetInvestment(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + path, (MultivaluedMap)null, (String)null);
    }

    @POST
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Create cost items and related data.",
            schema = "projectcostitems",
            schemaBody = "projectcostitems",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response createBudgetCostItems(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String var3 = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + var3, (MultivaluedMap)null, var2);
    }

    @PUT
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Update cost items and related data.",
            schema = "projectcostitems",
            schemaBody = "projectcostitems",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response updateBudgetCostItems(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String var3 = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + var3, (MultivaluedMap)null, var2);
    }

    @PUT
    @Path("/project/investment")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Connect project with cost item of investment.",
            schema = "projectcostitems",
            schemaBody = "projectcostitems",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response connectProjectInvestment(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String var3 = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + var3, (MultivaluedMap)null, var2);
    }

    @DELETE
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Delete cost items and related data.",
            schema = "projectcostitems",
            schemaBody = "projectcostitems",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response deleteBudgetCostItems(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String var3 = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + var3, (MultivaluedMap)null, var2);
    }
}
