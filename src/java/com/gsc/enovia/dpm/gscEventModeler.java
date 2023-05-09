package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.service.ServiceResource;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiClassInfo;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiMethodInfo;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiServiceParameter;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiMethodInfo.Visibility;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiServiceParameter.ParameterDataType;
import com.dassault_systemes.enovia.openapi.documentation.annotation.ApiServiceParameter.ParameterType;
import com.dassault_systemes.platform.restServices.ModelerBase;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
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
        title = "gscEvent Rest Services",
        schema = "gscEvent",
        version = "1.0.0",
        description = "Rest Services for managing the gscEvent modeler.",
        tag = "Task"
)
@ApplicationPath("/resources/v1/modeler/gscEvent")
@Path("/")
public class gscEventModeler extends ModelerBase {
    public static final String E6W_Service = "gscevent/";

    public gscEventModeler() {
    }

    public Class<?>[] getServices() {
        return new Class[]{com.gsc.enovia.dpm.gscEventModeler.class};
    }

    @POST
    @Path("")
    @Produces({"application/json"})
    @Consumes({"application/x-www-form-urlencoded"})
    public static Response getTasks(@Context HttpServletRequest var0, MultivaluedMap<String, String> var1) throws FoundationException {
        return ServiceResource.processServiceRequest(var0, E6W_Service, var1, (String)null);
    }

    @POST
    @Path("ids")
    @Produces({"application/json"})
    @Consumes({"application/x-www-form-urlencoded"})
    @ApiMethodInfo(
            summary = "Get the user assigned tasks for the specified task objects.",
            schema = "tasks",
            exposition = Visibility.Public
    )
    public static Response getTasks2(@Context HttpServletRequest var0, MultivaluedMap<String, String> var1) throws FoundationException {
        return ServiceResource.processServiceRequest(var0, E6W_Service, var1, (String)null);
    }

    @GET
    @Path("")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Get the user assigned/owned tasks.",
            schema = "tasks",
            exposition = Visibility.Public,
            parameters = {@ApiServiceParameter(
                    name = "showProjectTasks",
                    description = "whether to include project tasks",
                    paramType = ParameterType.Query,
                    dataType = ParameterDataType.Boolean,
                    required = false,
                    deprecated = false
            )}
    )
    public static Response getUserTasks(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var2, (MultivaluedMap)null, (String)null);
    }

    @PUT
    @Path("")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Modify existing task(s).",
            schema = "tasks",
            schemaBody = "tasks",
            exposition = Visibility.Public
    )
    public static Response updateTasks(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var3, (MultivaluedMap)null, var2);
    }

    @POST
    @Path("")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Create new task(s).",
            schema = "tasks",
            schemaBody = "tasks",
            exposition = Visibility.Public
    )
    public static Response createTasks(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var3, (MultivaluedMap)null, var2);
    }

    @DELETE
    @Path("")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    public static Response deleteTasks(@Context HttpServletRequest var0, @Context UriInfo var1, String  var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var3, (MultivaluedMap)null, var2);
    }

    @GET
    @Path("{taskId}")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Retrive an existing task information.",
            schema = "tasks",
            exposition = Visibility.Public
    )
    public static Response getTask(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var2, (MultivaluedMap)null, (String)null);
    }

    @PUT
    @Path("{taskId}")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Update an existing task information.",
            schema = "tasks",
            schemaBody = "tasks",
            exposition = Visibility.Public
    )
    public static Response updateTask(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var3, (MultivaluedMap)null, var2);
    }

    @POST
    @Path("{taskId}")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    public static Response createTask(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var3, (MultivaluedMap)null, var2);
    }

    @DELETE
    @Path("{taskId}")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Delete an existing task.",
            schema = "",
            exposition = Visibility.Public
    )
    public static Response deleteTask(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var3, (MultivaluedMap)null, var2);
    }

    @GET
    @Path("{taskId}/assignees")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Retrieve assignees for an existing task.",
            schema = "assignees",
            exposition = Visibility.Public
    )
    public static Response getTaskAssignees(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var2, (MultivaluedMap)null, (String)null);
    }

    @PUT
    @Path("{taskId}/assignees")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Add new assignees for an existing task.",
            schema = "assignees",
            schemaBody = "assignees",
            exposition = Visibility.Public
    )
    public static Response addTaskAssignees(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var3, (MultivaluedMap)null, var2);
    }

    @DELETE
    @Path("{taskId}/assignees")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    public static Response removeTaskAssignees(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var3, (MultivaluedMap)null, var2);
    }

    @GET
    @Path("scopeId/{scopeId}")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Retrieve tha tasks for a given context/scope object.",
            schema = "tasks",
            exposition = Visibility.Public
    )
    public static Response getScopeTasks(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var2, (MultivaluedMap)null, (String)null);
    }

    @GET
    @Path("{taskId}/scopes")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Retrieve tha tasks context/scope object(s).",
            schema = "scopes",
            exposition = Visibility.Public
    )
    public static Response getTaskScopes(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var2, (MultivaluedMap)null, (String)null);
    }

    @POST
    @Path("{taskId}/scopes")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Add specified Scope object(s) to the specific Task object.",
            schema = "scopes",
            schemaBody = "scopes",
            exposition = Visibility.Public
    )
    public static Response addTaskScopes(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var3, (MultivaluedMap)null, var2);
    }

    @DELETE
    @Path("{taskId}/scopes")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    public static Response removeTaskScopes(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var3, (MultivaluedMap)null, var2);
    }

    @GET
    @Path("{taskId}/references")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Get the task references.",
            schema = "references",
            exposition = Visibility.Public
    )
    public static Response getTaskReferences(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var2, (MultivaluedMap)null, (String)null);
    }

    @POST
    @Path("{taskId}/references")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Add new task references.",
            schema = "references",
            schemaBody = "references",
            exposition = Visibility.Public
    )
    public static Response addTaskReferences(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var3, (MultivaluedMap)null, var2);
    }

    @DELETE
    @Path("{taskId}/references")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    public static Response removeTaskReferences(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var3, (MultivaluedMap)null, var2);
    }

    @GET
    @Path("referenceId/{referenceId}")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    public static Response getTasksForReference(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var2, (MultivaluedMap)null, (String)null);
    }

    @GET
    @Path("{taskId}/deliverables")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Get the task deliverables.",
            schema = "deliverables",
            exposition = Visibility.Public
    )
    public static Response getTaskDeliverables(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var2, (MultivaluedMap)null, (String)null);
    }

    @POST
    @Path("{taskId}/deliverables")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Add new task deliverables.",
            schema = "deliverables",
            schemaBody = "deliverables",
            exposition = Visibility.Public
    )
    public static Response addTaskDeliverables(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var3, (MultivaluedMap)null, var2);
    }

    @DELETE
    @Path("{taskId}/deliverables")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    public static Response removeTaskDeliverables(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var3, (MultivaluedMap)null, var2);
    }

    @GET
    @Path("deliverableId/{deliverableId}")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    public static Response getTasksForDeliverable(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var2, (MultivaluedMap)null, (String)null);
    }

    @POST
    @Path("autoRefresh")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @Consumes({"application/x-www-form-urlencoded"})
    public static Response autoRefresh(@Context HttpServletRequest var0, MultivaluedMap<String, String> var1) throws FoundationException {
        return ServiceResource.processServiceRequest(var0, "gsctasks/autoRefresh", var1, (String)null);
    }

    @POST
    @Path("autoRefresh")
    @Produces({"application/json"})
    public static Response autoRefresh2(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var3, (MultivaluedMap)null, var2, true, false);
    }

    @GET
    @Path("tasksPreference")
    @Produces({"application/json", "application/ds-json"})
    public static Response getTaskAssignNotificationPreference(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, var2, (MultivaluedMap)null, (String)null);
    }

    @PUT
    @Path("tasksPreference")
    @Produces({"application/json", "application/ds-json"})
    public static Response updateTaskAssignNotificationPreference(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, var3, (MultivaluedMap)null, var2);
    }

    @GET
    @Path("help")
    @Produces({"application/json"})
    public static Response generateHelpDocumentation(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        return ServiceResource.processServiceHelpRequest(var0, com.dassault_systemes.enovia.tskv2.TasksModeler.class, E6W_Service);
    }

    @GET
    @Path("{taskId}/projects")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Retrieve assignees for an existing task.",
            schema = "gscEventProject",
            exposition = Visibility.Public
    )
    public static Response getRelatedProjects(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var2, (MultivaluedMap)null, (String)null);
    }

    @PUT
    @Path("{taskId}/projects")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Add new assignees for an existing task.",
            schema = "gscEventProject",
            schemaBody = "gscEventProject",
            exposition = Visibility.Public
    )
    public static Response updateRelatedProjects(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var3, (MultivaluedMap)null, var2);
    }

    @GET
    @Path("{taskId}/tasks")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Retrieve assignees for an existing task.",
            schema = "gscEventTask",
            exposition = Visibility.Public
    )
    public static Response getRelatedTasks(@Context HttpServletRequest var0, @Context UriInfo var1) throws FoundationException {
        String var2 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var2, (MultivaluedMap)null, (String)null);
    }

    @PUT
    @Path("{taskId}/tasks")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Add new assignees for an existing task.",
            schema = "gscEventTask",
            schemaBody = "gscEventTask",
            exposition = Visibility.Public
    )
    public static Response updateRelatedTasks(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var3, (MultivaluedMap)null, var2);
    }

    @PUT
    @Path("{taskId}/state")
    @Produces({"application/json", "application/xml", "application/ds-json"})
    @ApiMethodInfo(
            summary = "Add new assignees for an existing task.",
            schema = "gscEventTask",
            schemaBody = "gscEventTask",
            exposition = Visibility.Public
    )
    public static Response updateTaskState(@Context HttpServletRequest var0, @Context UriInfo var1, String var2) throws FoundationException {
        String var3 = var1.getPath();
        return ServiceResource.processServiceRequest(var0, E6W_Service + var3, (MultivaluedMap)null, var2);
    }
}

