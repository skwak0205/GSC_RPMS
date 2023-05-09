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
        schema = "projectInvestIf",
        version = "1.0.0",
        description = "Rest Services for managing the Project Invest modeler.",
        tag = "Project"
)
@ApplicationPath("/resources/v1/modeler/gscprojectinvest")
public class gscProjectInvestModeler extends ModelerBase {
    public static final String DPM_Service = "gscProjectInvestIf/";
    public Class[] getServices() {
        return new Class[]{gscProjectInvestModeler.class};
    }

    @GET
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get all gscInvestIf.",
            schema = "gscInvestIf",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getAllInvest(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + path, (MultivaluedMap)null, (String)null);
    }

    @PUT
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Connect gscInvestIf with Project.",
            schema = "gscInvestIf",
            schemaBody = "gscInvestIf",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response connectProjectInvest(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, String var2) throws FoundationException {
        String var3 = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + var3, (MultivaluedMap)null, var2);
    }
}
