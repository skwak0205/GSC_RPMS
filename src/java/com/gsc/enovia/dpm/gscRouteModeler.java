package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiMethodInfo;
import com.dassault_systemes.platform.restServices.ModelerBase;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@ApplicationPath("/resources/v1/modeler/gscRoute")
public class gscRouteModeler extends ModelerBase {
    public static final String DPM_Service = "dpm.gscRoutes/";

    @Override
    public Class<?>[] getServices()  {
        return new Class[]{gscRouteModeler.class};
    }

    @POST
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Update Route state.",
            schema = "route",
            schemaBody = "route",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response updateRouteState(@Context UriInfo uriInfo, String body) throws Exception {
        String var3 = uriInfo.getPath();
        return gscRouteService.updateRouteState(body);
    }

    @POST
    @Path("/approval")
    @Produces({"application/json"})
    public static Response sendApproval(@Context HttpServletRequest req, @Context UriInfo paramUriInfo, String body) throws Exception {
        String str = paramUriInfo.getPath();
        return gscRouteService.sendApproval(req, body);
    }
}
