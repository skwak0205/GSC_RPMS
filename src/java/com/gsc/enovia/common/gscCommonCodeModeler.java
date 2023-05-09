package com.gsc.enovia.common;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.service.ServiceResource;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiMethodInfo;
import com.dassault_systemes.platform.restServices.ModelerBase;
import com.gsc.enovia.dpm.gscBusinessGoalModeler;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
@ApplicationPath("/resources/v1/modeler/gsccommoncodes")
public class gscCommonCodeModeler  extends ModelerBase {
    public static final String PGM_Service = "dpm.gsccommoncodes/";

    @Override
    public Class<?>[] getServices() {
        return new Class[]{gscCommonCodeModeler.class};
    }
    @GET
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get all common codes.",
            schema = "commoncodes",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getCommonCodes(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) throws FoundationException {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(httpServletRequest, PGM_Service + path, (MultivaluedMap)null, (String)null);
    }
}
