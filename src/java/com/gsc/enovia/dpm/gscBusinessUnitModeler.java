package com.gsc.enovia.dpm;

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
        title = "BusinessUnit Rest Services",
        schema = "businessunits",
        version = "1.0.0",
        description = "Rest Services for managing the BusinessUnit modeler.",
        tag = "BusinessGoal"
)
@ApplicationPath("/resources/v1/modeler/gscbusinessunits")
public class gscBusinessUnitModeler extends ModelerBase {
    public static final String DPM_Service = "dpm.gscbusinessunits/";

    public Class[] getServices() {
        return new Class[]{gscBusinessUnitModeler.class};
    }

    @GET
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get all users businessunits.",
            schema = "gscbusinessunits",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getBusinessUnits(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + path, (MultivaluedMap)null, (String)null);
    }

    @GET
    @Path("{businessunitId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get a businessunit from physicalId.",
            schema = "gscbusinessunits",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getBusinessUnitInfo(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + path, (MultivaluedMap)null, (String)null);
    }

    @GET
    @Path("mybusinessunit")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get a My businessunit.",
            schema = "gscbusinessunits",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getMyBusinessUnitInfo(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, DPM_Service + path, (MultivaluedMap)null, (String)null);
    }
}