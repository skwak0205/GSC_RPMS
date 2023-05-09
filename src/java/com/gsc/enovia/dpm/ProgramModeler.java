package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.service.ServiceResource;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiClassInfo;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiMethodInfo;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiMethodInfo.Visibility;
import com.dassault_systemes.platform.restServices.ModelerBase;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@ApiClassInfo(
   title = "Program Rest Services",
   schema = "program",
   version = "1.0.0",
   description = "Rest Services for managing the Program modeler.",
   tag = "Program"
)
@ApplicationPath("/resources/v1/modeler/gscprograms")
public class ProgramModeler extends ModelerBase {
   public static final String PGM_Service = "dpm.programs/";

   public Class[] getServices() {
      return new Class[]{ProgramModeler.class};
   }

   @GET
   @Path("")
   @Produces({"application/json"})
   @ApiMethodInfo(
      summary = "Get all users Programs.",
      schema = "programs",
      exposition = Visibility.Public
   )
   public static Response getPrograms(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
      String var2 = var1.getPath();
      return ServiceResource.processServiceRequest(var0, PGM_Service + var2, (MultivaluedMap)null, (String)null);
   }

   @POST
   @Path("")
   @Produces({"application/json"})
   @ApiMethodInfo(
      summary = "Create Programs.",
      schema = "programs",
      schemaBody = "programs",
      exposition = Visibility.Public
   )
   public static Response createPrograms(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
      String var3 = var1.getPath();
      return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap)null, var2);
   }

   @GET
   @Path("{programId}")
   @Produces({"application/json"})
   @ApiMethodInfo(
      summary = "Get a specific Program detail and related data.",
      schema = "programs",
      exposition = Visibility.Public
   )
   public static Response getProgramInfo(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
      String var2 = var1.getPath();
      return ServiceResource.processServiceRequest(var0, PGM_Service + var2, (MultivaluedMap)null, (String)null);
   }

   @PUT
   @Path("{programId}")
   @Produces({"application/json"})
   @ApiMethodInfo(
      summary = "Update a Program and its related data.",
      schema = "programs",
      schemaBody = "programs",
      exposition = Visibility.Public
   )
   public static Response updateProgramInfo(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
      String var3 = var1.getPath();
      return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap)null, var2);
   }

   @DELETE
   @Path("{programId}")
   @Produces({"application/json"})
   @ApiMethodInfo(
      summary = "Delete the given Program if it does not contain Projects.",
      exposition = Visibility.Public
   )
   public static Response deleteProgram(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
      String var3 = var1.getPath();
      return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap)null, var2);
   }

   @GET
   @Path("{programId}/projects")
   @Produces({"application/json"})
   @ApiMethodInfo(
      summary = "Get a list of Projects associated to specific Program.",
      schema = "projects",
      exposition = Visibility.Public
   )
   public static Response getProgramProjects(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
      String var2 = var1.getPath();
      return ServiceResource.processServiceRequest(var0, PGM_Service + var2, (MultivaluedMap)null, (String)null);
   }

   @PUT
   @Path("{programId}/projects")
   @Produces({"application/json"})
   @ApiMethodInfo(
      summary = "Add list of Projects associated to specific Program.",
      schema = "projects",
      schemaBody = "projects",
      exposition = Visibility.Public
   )
   public static Response addProgramProjects(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
      String var3 = var1.getPath();
      return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap)null, var2);
   }

   @DELETE
   @Path("{programId}/projects")
   @Produces({"application/json"})
   public static Response removeProgramProjects(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
      String var3 = var1.getPath();
      return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap)null, var2);
   }

   @GET
   @Path("help")
   @Produces({"application/json"})
   public static Response generateHelpDocumentation(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
      return ServiceResource.processServiceHelpRequest(var0, ProgramModeler.class, PGM_Service);
   }

   @GET
   @Path("search")
   @Produces({"application/json"})
   @ApiMethodInfo(
      summary = "Search Programs using the provided input string.",
      schema = "programs",
      schemaBody = "programs",
      exposition = Visibility.Public
   )
   public static Response searchPrograms(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
      String var3 = var1.getPath();
      return ServiceResource.processServiceRequest(var0, PGM_Service + var3, (MultivaluedMap)null, (String)null);
   }
}
