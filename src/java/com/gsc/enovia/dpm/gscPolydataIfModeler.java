package com.gsc.enovia.dpm;

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
        title = "PolydataIf Rest Services",
        schema = "PolydataIfs",
        version = "1.0.0",
        description = "Rest Services for managing the PolydataIf modeler.",
        tag = "PolydataIf"
)
@ApplicationPath("/resources/v1/modeler/gscpolydataIf")
public class gscPolydataIfModeler extends ModelerBase {
    public static final String PGM_Service = "dpm.gscpolydataIf/";

    public Class[] getServices() {
        return new Class[]{gscPolydataIfModeler.class};
    }

    @GET
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get all users PolydataIfs.",
            schema = "PolydataIfs",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getPolydataIfs(@Context HttpServletRequest context, @Context UriInfo uriInfo) throws Exception {
        String path = uriInfo.getPath();
        return ServiceResource.processServiceRequest(context, PGM_Service + path, (MultivaluedMap) null, (String) null);
    }

    @POST
    @Path("")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Create PolydataIfs.",
            schema = "PolydataIfs",
            schemaBody = "PolydataIfs",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response createPolydataIfs(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws Exception {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap) null, var2);
    }

    @GET
    @Path("{gscpolydataIfId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Get a specific PolydataIf detail and related data.",
            schema = "PolydataIfs",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response getPolydataIfInfo(@Context HttpServletRequest var0, @Context UriInfo var1) throws Exception {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var2, (MultivaluedMap) null, (String) null);
    }

    @PUT
    @Path("{gscpolydataIfId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Update a PolydataIf and its related data.",
            schema = "PolydataIfs",
            schemaBody = "PolydataIfs",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response updatePolydataIfInfo(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws Exception {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap) null, var2);
    }

    @DELETE
    @Path("{gscpolydataIfId}")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Delete the given PolydataIf if it does not contain Projects.",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response deletePolydataIf(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws Exception {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap) null, var2);
    }

    @GET
    @Path("help")
    @Produces({"application/json"})
    public static Response generateHelpDocumentation(@Context HttpServletRequest var0, @Context UriInfo var1) throws Exception {
        return ServiceResource.processServiceHelpRequest(var0, gscPolydataIfModeler.class, PGM_Service);
    }

    @GET
    @Path("search")
    @Produces({"application/json"})
    @ApiMethodInfo(
            summary = "Search PolydataIfs using the provided input string.",
            schema = "PolydataIfs",
            schemaBody = "PolydataIfs",
            exposition = ApiMethodInfo.Visibility.Public
    )
    public static Response searchPolydataIfs(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws Exception {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap) null, (String) null);
    }
}
