package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.e6wv2.foundation.*;
import com.dassault_systemes.enovia.e6wv2.foundation.db.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.*;
import com.dassault_systemes.enovia.e6wv2.foundation.util.CacheUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.util.FormatUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.util.StringUtil;
import com.dassault_systemes.enovia.tskv2.*;
import com.gsc.util.ServiceUtil;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.MapList;
import matrix.db.Context;
import matrix.util.StringList;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class gscEventService implements ServiceConstants {
    protected static final String BASE_SERVICE_NAME = "3dspace";
    protected static final List<String> ALLOWED_PROXY_SERVICES = new ArrayList(1);
    protected static final String PROJECTS_SERVICE_NAME = "dpm.projects";
    protected static final String SAMPLE_DATA_CREATED = "preference_SampleDataCreated";
    protected static final String SAMPLE_DATA_URL_PARAM = "createSampleData";
    private final Map<String, Dataobject> _persons = new HashMap();
    protected static final String CONSTANT_STATUS_NOT_STARTED = "NotStarted";
    protected static final String CONSTANT_STATUS_IN_PROGRESS = "InProgress";
    protected static final String CONSTANT_STATUS_DONE = "Done";
    protected static final String ASSIGN_TASK_NOTIFICATION = "preference_AssignedTaskNotificationOn";
    protected static final String SEND_EMAIL = "sendEmail";
    private static final Map<String, String> STATE_MAPPINGS = new HashMap(5);
    public static final String SCOPE_ID = "scopeId";
    public static final String SHOW_PROJECT_TASKS = "showProjectTasks";
    public static final String ESTIMATED_START_DATE = "estimatedStartDate";
    public static final String ESTIMATED_FINISH_DATE = "dueDate";
    public static final String ESTIMATED_DURATION = "estimatedDuration";
    private static final String FIELD_OWNER_NAME = "ownerName";
    private static final String FIELD_ORIGINATOR_NAME = "originatorName";
    private static final String FIELD_NAME_TRANSIENT = "name.transient";
    private static final String FIELD_ACTUAL_START_DATE = "actualStartDate";
    private static final String FIELD_ACTUAL_FINISH_DATE = "actualFinishDate";
    private static final String FIELD_STATE = "state";
    private static final String TYPE_ROUTE_TASK = "Inbox Task";
    private static final String RELATIONSHIP_ROUTE_CONTENT = "Object Route";
    private static final String ROUTE_TASK = "Route Task";
    private static final Select SELECT_ROUTE_ID;
    private static final Select SELECT_ROUTE_REQUIRES_ESIGN;
    private static final String IS_AN_EXPERIMENT_TASK = "to[Project Access Key].from.from[Project Access List].to.type.kindof[Experiment]";
    private static final Select SELECTABLE_IS_AN_EXPERIMENT_TASK;
    protected static final String RELATIONSHIP_ASSIGNED_TASKS = "Assigned Tasks";
    protected static final String RELATIONSHIP_ASSIGNED_EXPERIMENT_TASKS = "Assigned Experiment Tasks";
    protected static final String INTERFACE_DPMTASKWEIGHTAGE = "DPMTaskWeightage";
    protected static final String ATTRIBUTE_TASKWEIGHTAGE = "TaskWeightage";
    protected static final String PARAMETER_DELIVERABLE_ID = "deliverableId";
    protected static final String PARAMETER_REFERENCE_ID = "referenceId";

    public gscEventService() {
    }

    public static Datacollection updateEvents(Context var0, String[] var1) throws Exception {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        ArgMap var3 = var2.getServiceArgs();
        Datacollection var4 = var2.getDatacollection();

        String var5 = var2.getServiceName();
        List var6 = gscEvent.getTaskTypes(var0); // task type
        HashMap var7 = new HashMap();
        HashMap var8 = new HashMap();
        LinkedHashMap var9 = new LinkedHashMap(5);
        ArrayList var10 = new ArrayList();
        var10.add("Task Actual Start Date");
        var10.add("Task Actual Finish Date");
        var10.add("Percent Complete");

        ArrayList var11 = null;
        Object var12 = null;
        ArrayList var13 = null;
        String var14 = null;
        Object var15 = new HashMap();
        Map var16 = null;
        FoundationUtil.debug(".. creating tasks ...", var2.getStartTime());
        boolean var17 = false;
        int var18 = 1;

        Iterator var19 = var4.getDataobjects().iterator();
        while(true) {
            Dataobject var20;
            UpdateActions var21;
            String var23;
            Dataobject var25;
            String var26;
            String var30;
            String var31;
            String var33;
            String var60;
            String var67;
            String var71;
            String var73;
            do {
                if (!var19.hasNext()) {
                    if (var14 != null) {
                        FoundationUtil.debug(".. turning triggers on ...", var2.getStartTime());
                        ContextUtil.setTtriggersMode(var0, true);
                        FoundationUtil.debug(".. turning triggers on ... done", var2.getStartTime());
                        ContextUtil.setGlobalRPEValue(var0, "IGNORE_CREATE_TRIGGER", "true");
                        var19 = var13.iterator();

                        while(var19.hasNext()) {
                            var20 = (Dataobject)var19.next();
                            String var50 = var20.getId();
                            var9.clear();
                            ServiceDataFunctions.fillUpdates(var0, var20, var11, var9);
                            String var52 = "False";
                            if (var20.getDataelements() != null) {
                                var52 = (String)var20.getDataelements().get("mpiRequest");
                            }

                            if ("True".equalsIgnoreCase(var52) && UpdateActions.CREATE.equals(var20.getUpdateAction())) {
                                gscEvent.modifyTask(var0, var50, var9, false, true, false, (String)null);
                                var3.put("rollup", "live");
                            } else {
                                gscEvent.modifyTask(var0, var50, var9, false, false);
                            }

                            var23 = DataelementMapAdapter.getDataelementValue(var20, "state");
                            if (var23 != null && !var23.equals("Create")) {
                                gscEvent.setTaskState(var0, var50, var23, var20.getType(), var5);
                            }
                        }

                        ContextUtil.setGlobalRPEValue(var0, "IGNORE_CREATE_TRIGGER", "false");
                    }

                    FoundationUtil.debug(".. creating tasks ... done.", var2.getStartTime());
                    Datacollection var48 = new Datacollection();
                    Datacollection var49 = new Datacollection();
                    FoundationUtil.debug(".. reparenting & deleting tasks ...", var2.getStartTime());
                    HashMap var51 = new HashMap();
                    Iterator var53 = var4.getDataobjects().iterator();

                    while(true) {
                        Dataobject var54;
                        do {
                            do {
                                if (!var53.hasNext()) {
                                    FoundationUtil.debug(".. reparenting & deleting tasks ... done.", var2.getStartTime());
                                    ArrayList var55;
                                    if (!var48.getDataobjects().isEmpty()) {
                                        FoundationUtil.debug(".. retrieving business types for modified tasks ... ", var2.getStartTime());
                                        var55 = new ArrayList(1);
                                        var55.add(SELECTABLE_TYPE);
                                        ObjectUtil.print(var0, var48, (PrintData)null, var55, true);
                                        FoundationUtil.debug(".. retrieving business types for modified tasks ... done.", var2.getStartTime());
                                        Iterator var56 = var48.getDataobjects().iterator();

                                        while(var56.hasNext()) {
                                            Dataobject var64 = (Dataobject)var56.next();
                                            if ("".equals(var64.getType())) {
                                                throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.noModifyAccess", var0.getLocale()));
                                            }
                                        }
                                    }

                                    if (!var49.getDataobjects().isEmpty()) {
                                        FoundationUtil.debug(".. retrieving current actual start date for modified tasks ... ", var2.getStartTime());
                                        var55 = new ArrayList(1);
                                        var55.add(Task.SELECT_TASK_ACTUAL_START_DATE);
                                        ObjectUtil.print(var0, var49, (PrintData)null, var55, true);
                                        FoundationUtil.debug(".. retrieving current actual start date for modified tasks ... done.", var2.getStartTime());
                                    }

                                    Set var58 = var7.keySet();
                                    Set var59 = var8.keySet();
                                    Set var66 = var51.keySet();
                                    HashSet var63 = new HashSet();
                                    var63.addAll(var58);
                                    var63.addAll(var59);
                                    var63.addAll(var66);

                                    boolean var35;
                                    String var36;
                                    Iterator var70;
                                    ProjectSequence var82;
                                    for(var70 = var63.iterator(); var70.hasNext(); var82.finishUpdateSession(var0)) {
                                        var67 = (String)var70.next();
                                        var82 = new ProjectSequence(var0, var67);
                                        Map var80 = var82.getSequenceData(var0);
                                        var82.startUpdateSession(var0);
                                        List var76 = (List)var7.get(var67);
                                        if (var76 != null) {
                                            Iterator var85 = var76.iterator();

                                            label402:
                                            while(true) {
                                                Dataobject var88;
                                                do {
                                                    if (!var85.hasNext()) {
                                                        break label402;
                                                    }

                                                    var88 = (Dataobject)var85.next();
                                                } while(var80.containsKey(var88.getId()));

                                                var33 = RelelementMapAdapter.getRelelementValue(var88, "previousTaskId");
                                                String var34 = var88.getType();
                                                var35 = "Project Space".equalsIgnoreCase(var34) || "Project Concept".equalsIgnoreCase(var34);
                                                var36 = RelelementMapAdapter.getRelelementValue(var88, "nextTaskId");
                                                var82.assignSequence(var0, var88.getParent().getId(), var88.getId(), var33, var36, var35);
                                            }
                                        }

                                        List var87 = (List)var8.get(var67);
                                        if (var87 != null) {
                                            var82.unAssignSequence(var0, var87);
                                        }

                                        List var89 = (List)var51.get(var67);
                                        if (var89 != null) {
                                            Iterator var90 = var89.iterator();

                                            while(var90.hasNext()) {
                                                Dataobject var92 = (Dataobject)var90.next();
                                                String var95 = RelelementMapAdapter.getRelelementValue(var92, "previousTaskId");
                                                if (null != var95) {
                                                    var82.moveSequence(var0, var92.getParent().getId(), var92, var95, (String)null, (String)null);
                                                } else {
                                                    var36 = RelelementMapAdapter.getRelelementValue(var92, "nextTaskId");
                                                    var82.moveSequence(var0, var92.getParent().getId(), var92, (String)null, var36, (String)null);
                                                }
                                            }
                                        }
                                    }

                                    FoundationUtil.debug(".. performing task modifications & state changes ...", var2.getStartTime());
                                    var70 = var4.getDataobjects().iterator();

                                    while(true) {
                                        while(var70.hasNext()) {
                                            Dataobject var79 = (Dataobject)var70.next();
                                            UpdateActions var83 = var79.getUpdateAction();
                                            var73 = var79.getId();
                                            var30 = DataelementMapAdapter.getDataelementValue(var79, "subscribed");
                                            var31 = DataelementMapAdapter.getDataelementValue(var79, "emailAppId");
                                            if (var31 != null && !var31.isEmpty()) {
                                                ContextUtil.setCustomData(var0, "emailAppId", var31);
                                            }

                                            String var91;
                                            if (UpdateActions.MODIFY.equals(var83)) {
                                                var9.clear();
                                                //ServiceDataFunctions.fillUpdates(var0, var79, var2.getAutosaveFields(), var9);
                                                if (var9.containsKey("Task Constraint Type")) {
                                                    var91 = (String)var9.get("Task Constraint Type");
                                                    if ("".equals(var91) || null == var91) {
                                                        var9.remove("Task Constraint Type");
                                                    }
                                                }

                                                handleDuration(var0, var79, var9, var83);
                                                handleFormatPattern(var0, var79, var9, var83);
                                                var91 = getTitle(var0, var79);
                                                addToMap(var9, "name", var91);
                                                handleActuals(var79, var9, var5);
                                                if (!var48.getDataobjects().contains(var79)) {
                                                    var33 = var79.getType();
                                                    addToMap(var9, "type", var33);
                                                }

                                                boolean var93 = "true".equalsIgnoreCase((String)var3.get("isDPMTask"));
                                                boolean var94 = var79.getParent() == null || var93;
                                                var35 = ServiceSave.isObjectNew(var0, var73);

                                                addToMap(var9, "owner", DataelementMapAdapter.getDataelementValue(var79, "owner"));
                                                addToMap(var9, "gscTitle", DataelementMapAdapter.getDataelementValue(var79, "gscTitle"));
                                                addToMap(var9, "gscLocation", DataelementMapAdapter.getDataelementValue(var79, "gscLocation"));
                                                addToMap(var9, "Task Estimated Start Date", DataelementMapAdapter.getDataelementValue(var79, "estimatedStartDate"));
                                                addToMap(var9, "Task Estimated Finish Date", DataelementMapAdapter.getDataelementValue(var79, "estimatedFinishDate"));

                                                gscEvent.modifyTask(var0, var73, var9, var94, !var35, var93, var5);
                                                var36 = DataelementMapAdapter.getDataelementValue(var79, "state");
                                                String var37 = DataelementMapAdapter.getDataelementValue(var79, "routeTaskApprovalComments");
                                                String var38 = DataelementMapAdapter.getDataelementValue(var79, "routeTaskReviewerComments");
                                                String var39;
                                                if (var36 != null) {
                                                    var39 = "";
                                                    String var40 = "";
                                                    if (var79.getParent() == null && "Inbox Task".equals(var79.getType())) {
                                                        switch (var36) {
                                                            case "Assign":
                                                                var36 = "Assigned";
                                                            case "Assigned":
                                                            case "Review":
                                                            case "Complete":
                                                                break;
                                                            default:
                                                                throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.InvalidRouteTaskState", var0.getLocale()));
                                                        }

                                                        Dataobject var41 = getTaskInfo(var0, var73);
                                                        String var97 = DataelementMapAdapter.getDataelementValue(var41, SELECTABLE_CURRENT.getName());
                                                        String var43 = DataelementMapAdapter.getDataelementValue(var41, Task.SELECT_ROUTE_TASK_NEEDS_REVIEW.getName());
                                                        String var44 = DataelementMapAdapter.getDataelementValue(var41, Task.SELECT_ROUTE_TASK_APPROVAL_STATUS.getName());
                                                        String var45 = DataelementMapAdapter.getDataelementValue(var41, Task.SELECT_ROUTE_TASK_ACTION.getName());
                                                        var39 = DataelementMapAdapter.getDataelementValue(var41, SELECT_ROUTE_REQUIRES_ESIGN.getName());
                                                        var40 = var45;
                                                        String var46 = DataelementMapAdapter.getDataelementValue(var41, Task.SELECT_APPROVERS_COMMENTS.getName());
                                                        String var47 = DataelementMapAdapter.getDataelementValue(var41, Task.SELECT_REVIEWERS_COMMENTS.getName());
                                                        if ("Complete".equals(var97) && !var36.equals(var97)) {
                                                            throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.RouteTaskDemotionNotAllowedFromComplete", var0.getLocale()));
                                                        }

                                                        if ("Review".equals(var36) && "NO".equalsIgnoreCase(var43)) {
                                                            throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.ReviewStateNotRequired", var0.getLocale()));
                                                        }

                                                        if (("Complete".equals(var36) || "Review".equals(var36)) && "Approve".equals(var45) && ("".equals(var44) || "None".equals(var44))) {
                                                            throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.ApprovalDecisionRequired", var0.getLocale()));
                                                        }

                                                        if (("Review".equals(var36) || "Complete".equals(var36)) && ("".equals(var46) || null == var46) && ("".equals(var37) || null == var37)) {
                                                            throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.needsApprovalComments", var0.getLocale()));
                                                        }

                                                        if ("Complete".equals(var36) && "Review".equals(var97) && "YES".equalsIgnoreCase(var43) && ("".equals(var47) || null == var47) && ("".equals(var38) || null == var38)) {
                                                            throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.needsReviewerComments", var0.getLocale()));
                                                        }
                                                    }

                                                    Task.setTaskState(var0, var73, var36, var79.getType(), var5);
                                                    if ("TRUE".equalsIgnoreCase(var39)) {
                                                        String var96 = PropertyUtil.getTranslatedValue(var0, "Framework", "emxFramework.UserAuthentication.ReadAndUnderstand", var0.getLocale());
                                                        MqlUtil.mqlCommand(var0, "Modify bus $1 add history $2 comment $3", new String[]{var73, var40, var96});
                                                    }
                                                }

                                                var39 = DataelementMapAdapter.getDataelementValue(var79, "sendEmail");
                                                if (var30 != null && ("TRUE".equals(var30) || "FALSE".equals(var30))) {
                                                    Task.setSubscribed(var0, var73, "TRUE".equalsIgnoreCase(var30));
                                                } else if (var39 != null && "sendEmail".equals(var39)) {
                                                    NotificationUtil.sendEmailToSubscribers(var0, var73, var31, new HashMap());
                                                }
                                            } else if (UpdateActions.CREATE.equals(var83)) {
                                                if (var13 == null) {
                                                    var91 = DataelementMapAdapter.getDataelementValue(var79, "state");
                                                    if (var91 != null && !var91.equals("Create")) {
                                                        Task.setTaskState(var0, var73, var91, var79.getType(), var5);
                                                    }
                                                }

                                                if ("TRUE".equals(var30)) {
                                                    Task.setSubscribed(var0, var73, "TRUE".equalsIgnoreCase(var30));
                                                }
                                            }
                                        }

                                        FoundationUtil.debug(".. performing task modifications & state changes ... done.", var2.getStartTime());
                                        ServiceSave.getUpdatedObjects(var0, var2.getServiceReferenceName(), var2);
                                        return var4;
                                    }
                                }

                                var54 = (Dataobject)var53.next();
                                UpdateActions var62 = var54.getUpdateAction();
                                boolean var72;
                                if (!UpdateActions.CONNECT.equals(var62) && !UpdateActions.DISCONNECT.equals(var62)) {
                                    if (UpdateActions.MODIFY.equals(var62)) {
                                        captureSequenceOrder(var0, var54, var7, var8);
                                    } else if (UpdateActions.CREATE.equals(var62)) {
                                        captureSequenceOrder(var0, var54, var7, var8);
                                    } else if (UpdateActions.DELETE.equals(var62)) {
                                        captureSequenceOrder(var0, var54, var7, var8);
                                        var60 = var54.getId();
                                        boolean var68 = "true".equalsIgnoreCase((String)var3.get("isDPMTask"));
                                        var67 = "false";
                                        if (var54.getDataelements() != null) {
                                            var67 = (String)var54.getDataelements().get("mpiRequest");
                                        }

                                        var72 = false;
                                        if (var68 || "True".equalsIgnoreCase(var67)) {
                                            var72 = true;
                                        }

                                        Dataobject var77 = gscEvent.deleteTask(var0, var60, var54.getParent() == null, var72);
                                        if (var77 != null) {
                                            Object[] var75 = new Object[]{var0, var77};
                                            gscEvent var84 = new gscEvent();
                                            //ContextUtil.submitPostTransactionJob(var0, var84, "performSequenceAndRollup", var75);
                                        }

                                        var54.setId((String)null);
                                        var54.setRelId((String)null);
                                    }
                                } else {
                                    var25 = var54.getParent();
                                    var26 = var25.getId();
                                    var67 = var54.getId();
                                    var72 = false;
                                    if (var54.getDataelements() != null) {
                                        var72 = var54.getDataelements().containsValue("IndentationAndDragDrop");
                                    }

                                    if (UpdateActions.DISCONNECT.equals(var62)) {
                                        gscEvent.removeSubtask(var0, var26, var67, var72);
                                    } else {
                                        var54.setUpdateAction(UpdateActions.MODIFY);
                                        var67 = ServiceSave.getActualIdForTempId(var0, var67);
                                        var54.setId(var67);
                                        var73 = gscEvent.modifySubtask(var0, var26, var67, (Map)null, var72);
                                        var54.setRelId(var73);
                                    }

                                    boolean var74 = false;
                                    var30 = getParentPALId(var0, var25, var54);
                                    if (UpdateActions.CONNECT.equals(var62)) {
                                        ProjectSequence var78 = new ProjectSequence(var0, var30);
                                        Map var86 = var78.getSequenceData(var0);
                                        var74 = var86.containsKey(var67);
                                    }

                                    if (!var74) {
                                        if (!var72) {
                                            captureSequenceOrder(var0, var54, var7, var8);
                                        }
                                    } else {
                                        Object var81 = (List)var51.get(var30);
                                        if (var81 == null || ((List)var81).size() == 0) {
                                            var81 = new ArrayList();
                                            var51.put(var30, var81);
                                        }

                                        ((List)var81).add(var54);
                                    }
                                }
                            } while(!UpdateActions.MODIFY.equals(var54.getUpdateAction()));

                            var60 = var54.getId();
                            Dataobject var69 = getTaskInfo(var0, var60);
                            var67 = DataelementMapAdapter.getDataelementValue(var69, SELECTABLE_CURRENT.getName());
                            if (var67.equalsIgnoreCase("Review") && var54.getDataelements().containsKey("needsReview")) {
                                throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.needsApprovalUpdateFail", var0.getLocale()));
                            }

                            if (var54.getType() == null || var54.getType().isEmpty()) {
                                var48.getDataobjects().add(var54);
                            }

                            var71 = DataelementMapAdapter.getDataelementValue(var54, "actualStartDate");
                            var73 = DataelementMapAdapter.getDataelementValue(var54, "actualFinishDate");
                        } while((var71 == null || var71.isEmpty()) && (var73 == null || var73.isEmpty()));

                        var49.getDataobjects().add(var54);
                    }
                }

                var20 = (Dataobject)var19.next();
                var21 = var20.getUpdateAction();
            } while(!UpdateActions.CREATE.equals(var21));

            FoundationUtil.debug(".. creating task # " + var18++ + " ...", var2.getStartTime());
            var9.clear();
            Dataobject var22 = var20.getParent();
            var23 = var20.getDataelements().containsKey("sourceId") ? (String)var20.getDataelements().get("sourceId") : (String)var20.getDataelements().get("attribute[Source Id]");
            String var57;
            if (var22 != null) {
                if (var14 == null) {
                    ArrayList var24 = new ArrayList();
                    var24.add(Task.SELECT_TASK_ESTIMATED_START_DATE);
                    var24.add(SELECTABLE_TYPE);
                    var24.add(SELECTABLE_PROJECT);
                    var24.add(SELECTABLE_ORGANIZATION);
                    var24.add(SELECTABLE_VAULT);
                    var24.add(gscEvent.SELECTABLE_DEFAULT_CONSTRAINT_TYPE);
                    var24.add(gscEvent.SELECTABLE_TASK_CONSTRAINT_TYPE);
                    var25 = gscEvent.getTaskInfo(var0, var22.getId(), var24);
                    var14 = DataelementMapAdapter.getDataelementValue(var25, gscEvent.SELECT_TASK_ESTIMATED_START_DATE.getName());
                    var26 = var25.getType();
                    var22.setType(var26);
                    var15 = getPOVInfo(var0, var25);
                    var16 = ExtensionUtil.getAutomaticInterfacesByType(var0, "Project Management");
                    var13 = new ArrayList();
                    var11 = new ArrayList();
                    var12 = new ArrayList();
                    Iterator var27 = var2.getAutosaveFields().iterator();

                    while(true) {
                        while(var27.hasNext()) {
                            Field var28 = (Field)var27.next();
                            Selectable var29 = var28.getSelectable();
                            if (var29 != null && var10.indexOf(var29.getAttribute()) != -1) {
                                var11.add(var28);
                            } else {
                                ((List)var12).add(var28);
                            }
                        }

                        FoundationUtil.debug(".. turning triggers off ...", var2.getStartTime());
                        ContextUtil.setTtriggersMode(var0, false);
                        FoundationUtil.debug(".. turning triggers off ... done", var2.getStartTime());
                        break;
                    }
                }

                var13.add(0, var20);
                var9.put("Task Estimated Start Date", var14);
                var9.put("Task Estimated Finish Date", var14);
                var9.put("Originator", var0.getUser());
                if (var23 != null && !var23.isEmpty()) {
                    var9.put("Source Id", var23);
                }

                var9.putAll((Map)var15);
            } else if (!var17) {
                var57 = var0.getRole();
                if (var57 == null || var57.isEmpty() || var57.startsWith("ctx::VPLMExperimenter") || var57.startsWith("ctx::VPLMViewer")) {
                    throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.TaskCreationDeniedForReader", var0.getLocale()));
                }

                var9.put("Task Estimated Duration", "1");
                var17 = true;
                var12 = var2.getAutosaveFields();
            }

            //ServiceDataFunctions.fillUpdates(var0, var20, (List)var12, var9);
            handleDuration(var0, var20, var9, var21);
            var57 = var20.getType();
            if (var57 == null) {
                var60 = (String)var9.get("Task Estimated Duration");
                if (var20.getParent() != null && var60 != null) {
                    String[] var61 = var60.split(" ");
                    if (Double.valueOf(var61[0]) == 0.0) {
                        var57 = "Milestone";
                    }
                }

                if (var57 == null) {
                    var57 = "Task";
                }

                var20.setType(var57);
            }

            var60 = getTitle(var0, var20);
            if (var6.indexOf(var57) == -1) {
                createProject(var0, var2, var20);
                var26 = gscEvent.addSubtask(var0, (String)null, var22.getId(), var22.getType(), var20.getId(), (Map)null);
                var20.setRelId(var26);
            } else {
                handleActuals(var20, var9, var5);
                handleDefaultConstraintType(var0, var22, var20, var9);
                StringList var65 = var16 != null ? (StringList)var16.get(var57) : null;
                if (var65 != null) {
                    var67 = var65.join(",");
                    var9.put("interface", var67);
                }

                FoundationUtil.debug(".. calling create task API ...", var2.getStartTime());
                var67 = "";
                if (var20.getDataelements() != null) {
                    var67 = DataelementMapAdapter.getDataelementValue(var20, "policy");
                }

                var71 = null;
                if (var22 != null) {
                    var71 = var22.getId();
                }

                /*gscEvent type의 attribute 추가*/
                if (var57.equals("gscEvent")) {
                    var9.put("Task Estimated Start Date", DataelementMapAdapter.getDataelementValue(var20, "estimatedStartDate"));
                    var9.put("Task Estimated Finish Date", DataelementMapAdapter.getDataelementValue(var20, "estimatedFinishDate"));
                    var9.put("gscEventType", DataelementMapAdapter.getDataelementValue(var20, "gscEventType"));
                    var9.put("gscLocation", DataelementMapAdapter.getDataelementValue(var20, "gscLocation"));
                    var9.put("gscTitle", var60);
                    // 230502 - HJ 속성 추가 - constraintType, constraintDate
                    var9.put("Task Constraint Type", DataelementMapAdapter.getDataelementValue(var20, "constraintType"));
                    var9.put("Task Constraint Date", DataelementMapAdapter.getDataelementValue(var20, "constraintDate"));
                    // 230502 - HJ 속성 추가 - constraintType, constraintDate
                }

                var73 = gscEvent.createTask(var0, var57, var60, var9, var67, var71);

                System.out.println("new gscEvent oid >>> " + var73);

                /* gscEventProject */
                if (var57.equals("gscEvent")) {
                    String[] projectIds = new String[]{DataelementMapAdapter.getDataelementValue(var20, "projectIds")};
                    for (String prjOid : projectIds) {
                        System.out.println("prj oid >>> " + prjOid);
                        ServiceUtil.updateConnection(var0, UpdateActions.CREATE, var73, "gscEventProject", prjOid, true, true);
                    }
                }

                FoundationUtil.debug(".. calling create task API ... done", var2.getStartTime());
                ServiceSave.setActualIdForTempId(var0, var20, var73);

                if (var22 != null) {
                    var30 = getTaskPALIdToConnect(var0, var22, var20);
                    FoundationUtil.debug(".. calling create subtask API ...", var2.getStartTime());
                    var31 = (String)var20.getDataelements().get("taskWeightage");
                    HashMap var32 = null;
                    if (var31 != null && !"".equalsIgnoreCase(var31)) {
                        var32 = new HashMap();
                        var32.put("interface", "DPMTaskWeightage");
                        var32.put("TaskWeightage", var31);
                    }

                    var33 = gscEvent.addSubtask(var0, var30, var22.getId(), var22.getType(), var73, var32);
                    FoundationUtil.debug(".. calling create subtask API ... done", var2.getStartTime());
                    var20.setRelId(var33);
                }
            }
        }

    }

    private static Map<String, String> getPOVInfo(Context var0, Dataobject var1) throws FoundationException {
        Map var2 = ContextUtil.getContextPOVInfo(var0);
        String var3 = DataelementMapAdapter.getDataelementValue(var1, "organization");
        String var4 = DataelementMapAdapter.getDataelementValue(var1, "project");
        String var5 = DataelementMapAdapter.getDataelementValue(var1, "vault");
        HashMap var6 = new HashMap(3);
        if (!var3.equals(var2.get("organization"))) {
            var6.put("organization", var3);
        }

        if (!var4.equals(var2.get("project"))) {
            var6.put("project", var4);
        }

        if (!var5.equals(var2.get("vault"))) {
            var6.put("vault", var5);
        }

        return var6;
    }

    private static void handleDuration(Context var0, Dataobject var1, Map<String, String> var2, UpdateActions var3) throws FoundationException {
        String var4 = DataelementMapAdapter.getDataelementValue(var1, "estimatedDurationInputValue");
        String var5 = DataelementMapAdapter.getDataelementValue(var1, "estimatedDurationInputUnit");
        if (var4 != null && !var4.isEmpty() || var5 != null && !var5.isEmpty()) {
            String var6 = (String)var2.get("Task Estimated Duration");
            if (UpdateActions.MODIFY.equals(var3)) {
                ArrayList var7 = new ArrayList(2);
                if (var6 == null || var6.isEmpty()) {
                    var7.add(Task.SELECT_TASK_ESTIMATED_DURATION_INPUT_VALUE);
                }

                if (var5 == null || var5.isEmpty()) {
                    var7.add(Task.SELECT_TASK_ESTIMATED_DURATION_INPUT_UNIT);
                }

                if (!var7.isEmpty()) {
                    Dataobject var8 = gscEvent.getTaskInfo(var0, var1.getId(), var7);
                    String var9 = DataelementMapAdapter.getDataelementValue(var8, Task.SELECT_TASK_ESTIMATED_DURATION_INPUT_VALUE.getName());
                    String var10 = DataelementMapAdapter.getDataelementValue(var8, Task.SELECT_TASK_ESTIMATED_DURATION_INPUT_UNIT.getName());
                    var6 = var9 != null ? var9 : var6;
                    var5 = var10 != null ? var10 : var5;
                }
            }

            if (var6 != null && !var6.isEmpty() && var5 != null && !var5.isEmpty()) {
                if (!UpdateActions.CREATE.equals(var3)) {
                    String var11 = gscEvent.getPALId(var0, var1.getId());
                    if (var11.isEmpty()) {
                        ensureValidTaskDuration(var0, var6);
                    }
                }

                if (var4 != null && !var4.isEmpty() && var5 != null && !var5.isEmpty() && UpdateActions.CREATE.equals(var3)) {
                    var6 = var4;
                }

                var6 = var6 + " " + var5;
                var2.put("Task Estimated Duration", var6);
            }
        }

    }

    private static void ensureValidTaskDuration(Context var0, String var1) throws FoundationException {
        if (var1 != null && !var1.isEmpty()) {
            double var2 = Double.parseDouble(var1);
            if (var2 == 0.0) {
                throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.InvalidSimpleTaskDuration", var0.getLocale()));
            }
        }

    }

    private static void handleActuals(Dataobject var0, Map<String, String> var1, String var2) {
        if (var2 == null || !var2.contains("dpm.projecttemplates")) {
            removeEmptyFromMap(var1, "Task Actual Start Date");
            removeEmptyFromMap(var1, "Task Actual Finish Date");
            String var3 = (String)var1.get("Task Actual Start Date");
            String var4 = (String)var1.get("Task Actual Finish Date");
            String var5 = DataelementMapAdapter.getDataelementValue(var0, gscEvent.SELECT_TASK_ACTUAL_START_DATE.getName());
            if (var4 != null && !var4.isEmpty()) {
                if (var3 == null && (var5 == null || var5.isEmpty())) {
                    var1.put("Task Actual Start Date", var4);
                }

                var1.put("Percent Complete", "100");
                DataelementMapAdapter.removeDataelement(var0, "state");
            } else if (var3 != null && !var3.isEmpty() && (var5 == null || var5.isEmpty())) {
                String var6 = (String)var1.get("Percent Complete");
                if (var6 != null && Float.valueOf(var6) == 100.0F) {
                    DataelementMapAdapter.removeDataelement(var0, "state");
                } else if (!"Gate".equals(var0.getType()) && !"Milestone".equals(var0.getType())) {
                    DataelementMapAdapter.setDataelementValue(var0, "state", "Active");
                }
            }

        }
    }

    private static void captureSequenceOrder(Context var0, Dataobject var1, Map<String, List<Dataobject>> var2, Map<String, List<String>> var3) throws FoundationException {
        Dataobject var4 = var1.getParent();
        UpdateActions var5 = var1.getUpdateAction();
        String var6;
        if (var4 != null) {
            var6 = RelelementMapAdapter.getRelelementValue(var1, "sequenceOrder");
            String var7 = RelelementMapAdapter.getRelelementValue(var1, "previousTaskId");
            String var8 = RelelementMapAdapter.getRelelementValue(var1, "nextTaskId");
            if (var7 != null) {
                var7 = ServiceSave.getActualIdForTempId(var0, var7);
                RelelementMapAdapter.setRelelementValue(var1, "previousTaskId", var7);
            }

            if (var8 != null) {
                var8 = ServiceSave.getActualIdForTempId(var0, var8);
                RelelementMapAdapter.setRelelementValue(var1, "nextTaskId", var8);
            }

            if (!UpdateActions.MODIFY.equals(var5) || var6 != null || var7 != null || var8 != null) {
                String var9 = getParentPALId(var0, var4, var1);
                Object var10 = (List)var2.get(var9);
                if (var10 == null) {
                    var10 = new ArrayList();
                    var2.put(var9, (List<Dataobject>) var10);
                }

                Object var11 = (List)var3.get(var9);
                if (var11 == null) {
                    var11 = new ArrayList();
                    var3.put(var9, (List<String>) var11);
                }

                if (!UpdateActions.DELETE.equals(var5) && !UpdateActions.DISCONNECT.equals(var5)) {
                    ((List)var10).add(var1);
                } else {
                    ((List)var11).add(var1.getId());
                }
            }
        } else if (UpdateActions.DELETE.equals(var5)) {
            var6 = DataelementMapAdapter.getDataelementValue(var1, "PALId.transient");
            if (var6 == null) {
                var6 = gscEvent.getPALId(var0, var1.getId());
            }

            if (var6 != null && !var6.isEmpty()) {
                DataelementMapAdapter.setDataelementValue(var1, "PALId.transient", var6);
                Object var12 = (List)var3.get(var6);
                if (var12 == null) {
                    var12 = new ArrayList();
                    var3.put(var6, (List<String>) var12);
                }

                ((List)var12).add(var1.getId());
            }
        }

    }

    private static void createProject(Context var0, ServiceParameters var1, Dataobject var2) throws FoundationException {
        String var3 = ServiceJson.generateJsonStringfromJAXB(var2);
        Dataobject var4 = ServiceJson.readDataobjectfromJson(var3);
        Servicedata var5 = new Servicedata();
        var5.getData().add(var4);
        CSRFToken var6 = ContextUtil.getCSRFKey(var0, var1.getHttpRequest());
        var5.setCsrf(var6);
        ServiceParameters var7 = new ServiceParameters();
        var7.setServiceName("dpm.projects");
        var7.setHttpBaseUrl(var1.getHttpBaseUrl());
        var7.setHttpRequest(var1.getHttpRequest());
        var7.setStartTime(var1.getStartTime());
        ArgMap var8 = new ArgMap();
        var7.setServiceArgs(var8);
        var8.put("$include", "none");
        var8.put("$fields", "none");
        var8.put("TriggerStatus", "false");
        Servicedata var9 = ServiceBase.saveData(var0, var5, var7);
        Dataobject var10 = (Dataobject)var9.getData().get(0);
        var2.setId(var10.getId());
    }

    private static String getTaskPALIdToConnect(Context var0, Dataobject var1, Dataobject var2) throws FoundationException {
        String var3 = "PALId.transient";
        String var4 = DataelementMapAdapter.getDataelementValue(var1, var3);
        String var5 = var4 != null ? var4 : gscEvent.getPALId(var0, var1.getId());
        DataelementMapAdapter.setDataelementValue(var1, var3, var5);
        DataelementMapAdapter.setDataelementValue(var2, var3, var5);
        return var5;
    }

    private static String getParentPALId(Context var0, Dataobject var1, Dataobject var2) throws FoundationException {
        String var3 = "PALId.transient";
        String var4 = DataelementMapAdapter.getDataelementValue(var1, var3);
        String var5 = var4 != null ? var4 : gscEvent.getPALId(var0, var1.getId());
        DataelementMapAdapter.setDataelementValue(var1, var3, var5);
        return var5;
    }

    private static void handleDefaultConstraintType(Context var0, Dataobject var1, Dataobject var2, Map<String, String> var3) throws FoundationException {
        if (var1 != null) {
            String var4 = "defaultConstraintType";
            String var5 = DataelementMapAdapter.getDataelementValue(var1, var4);
            String var6 = var5 != null ? var5 : gscEvent.getDefaultConstraintType(var0, var1.getId());
            DataelementMapAdapter.setDataelementValue(var1, var4, var6);
            DataelementMapAdapter.setDataelementValue(var2, var4, var6);
            if (!var3.containsKey("Task Constraint Type")) {
                var3.put("Task Constraint Type", var6);
            }
        }

    }

    private static String getTitle(Context var0, Dataobject var1) throws FoundationUserException {
        String var2 = DataelementMapAdapter.getDataelementValue(var1, "title");
        if (var2 != null) {
            var2 = var2.trim();
        }

        if (var2 != null && var2.isEmpty()) {
            String var3 = PropertyUtil.getTranslatedValue(var0, "Foundation", "emxFoundation.Widget.Error.EmptyTitleNotAllowed", var0.getLocale());
            throw new FoundationUserException(var3);
        } else {
            return var2;
        }
    }

    public static Datacollection getRelatedTasks(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        String var3 = ((Dataobject)var2.getDatacollection().getDataobjects().get(0)).getId();
        String var4 = (String)var2.getJpoArgs().get("relatedItemType");
        String var5 = var4.equalsIgnoreCase("reference") ? "referenceId" : "deliverableId";
        String var6 = "tasks/";
        ArgMap var7 = var2.getServiceArgs();
        var7.put(var5, var3);
        var2.setServiceName(var6);
        Servicedata var8 = ServiceBase.getData(var0, var2);
        Datacollection var9 = new Datacollection();
        var9.getDataobjects().addAll(var8.getData());
        return var9;
    }

    public static Datacollection updatePredecessors(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        List var3 = var2.getDatacollection().getDataobjects();
        Datacollection var4 = new Datacollection();
        Iterator var6 = var3.iterator();

        Dataobject var8;
        String var15;
        while(var6.hasNext()) {
            Dataobject var7 = (Dataobject)var6.next();
            var8 = var7.getParent();
            String var9 = var7.getId();
            boolean var10 = ServiceSave.isObjectNew(var0, var9);
            if (var10) {
                var9 = ServiceSave.getActualIdForTempId(var0, var9);
            }

            ArrayList var11 = new ArrayList(2);
            var11.add(gscEvent.SELECTABLE_ISPROJECTSPACE);
            var11.add(gscEvent.SELECTABLE_ISPROJECTCONCEPT);
            Dataobject var12 = gscEvent.getTaskInfo(var0, var9, var11);
            boolean var13 = "TRUE".equalsIgnoreCase(DataelementMapAdapter.getDataelementValue(var12, gscEvent.SELECTABLE_ISPROJECTSPACE.getName())) || "TRUE".equalsIgnoreCase(DataelementMapAdapter.getDataelementValue(var12, gscEvent.SELECTABLE_ISPROJECTCONCEPT.getName()));
            if (var13) {
                throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.ProjectAsPredecessorAlert", var0.getLocale()));
            }

            boolean var14 = ServiceSave.isObjectNew(var0, var8.getId());
            if (!var14) {
                var15 = RelelementMapAdapter.getRelelementValue(var7, "dependencyType");
                if ("FS".equals(var15) || "SS".equals(var15)) {
                    String var16 = DataelementMapAdapter.getDataelementValue(var8, SELECTABLE_CURRENT.getName());
                    if (var16 == null) {
                        var4.getDataobjects().add(var8);
                    } else {
                        ensureTaskNotActive(var0, var8, "emxCollaborativeTasks.Error.CannotManageActiveTaskDependencies");
                    }
                }
            }
        }

        Dataobject var32;
        if (!var4.getDataobjects().isEmpty()) {
            ArrayList var25 = new ArrayList();
            var25.add(SELECTABLE_CURRENT);
            Datacollection var27 = ObjectUtil.print(var0, var4, (PrintData)null, var25, false);
            Iterator var30 = var27.getDataobjects().iterator();

            while(var30.hasNext()) {
                var32 = (Dataobject)var30.next();
                ensureTaskNotActive(var0, var32, "emxCollaborativeTasks.Error.CannotManageActiveTaskDependencies");
            }
        }

        HashMap var26 = new HashMap(2);
        Iterator var28 = var3.iterator();

        while(true) {
            while(true) {
                while(var28.hasNext()) {
                    var8 = (Dataobject)var28.next();
                    var32 = var8.getParent();
                    String var33 = var32.getId();
                    HashSet var34 = new HashSet();
                    RelateddataMap var35 = var32.getRelateddata();
                    Object var36 = new ArrayList();
                    if (var35 != null && var35.containsKey("predecessors")) {
                        var36 = (List)var35.get("predecessors");
                    }

                    ((List)var36).parallelStream().forEach((var1x) -> {
                        if (var1x != null) {
                            var34.add(((Dataobject)var1x).getId());
                        }

                    });
                    String var37 = RelelementMapAdapter.getRelelementValue(var8, "dependencyType");
                    var15 = RelelementMapAdapter.getRelelementValue(var8, "lagTime");
                    var26.clear();
                    addToMap(var26, "Dependency Type", var37);
                    addToMap(var26, "Lag Time", var15);
                    UpdateActions var38 = var8.getUpdateAction();
                    String var17;
                    String var18;
                    if (!UpdateActions.CREATE.equals(var38) && !UpdateActions.CONNECT.equals(var38)) {
                        if (UpdateActions.MODIFY.equals(var38)) {
                            var17 = var8.getRelId();
                            if (var17 != null) {
                                gscEvent.modifyPredecessorId(var0, var17, var26);
                            } else {
                                var18 = gscEvent.modifyPredecessor(var0, var33, var8.getId(), var26);
                                var8.setRelId(var18);
                            }
                        } else if (UpdateActions.DELETE.equals(var38) || UpdateActions.DISCONNECT.equals(var38)) {
                            var17 = var8.getRelId();
                            if (var17 != null) {
                                ObjectEditUtil.disconnect(var0, var17);
                            } else {
                                gscEvent.deletePredecessor(var0, var33, var8.getId());
                            }

                            var8.setId((String)null);
                        }
                    } else {
                        var17 = var8.getId() != null ? var8.getId() : var8.getTempId();
                        var17 = ServiceSave.getActualIdForTempId(var0, var17);
                        var18 = (String)var26.get("Dependency Type");
                        String var19 = "";
                        if (var34.contains(var17)) {
                            int var20 = 0;

                            for(int var21 = ((List)var36).size(); var20 < var21; ++var20) {
                                Dataobject var22 = (Dataobject)((List)var36).get(var20);
                                if (var22.getId().equalsIgnoreCase(var17)) {
                                    RelelementMap var23 = var22.getRelelements();
                                    String var24 = (String)var23.get("dependencyType");
                                    if (var18.equalsIgnoreCase(var24)) {
                                        var19 = var22.getRelId();
                                        break;
                                    }
                                }
                            }
                        }

                        if (var19 != null && !var19.isEmpty()) {
                            gscEvent.modifyPredecessorId(var0, var19, var26);
                        } else {
                            var8.setId(var17);
                            String var39 = gscEvent.addPredecessor(var0, var33, var17, var26);
                            var8.setRelId(var39);
                        }
                    }
                }

                String var29 = (String)var2.getServiceArgs().get("$fields");
                if (var29 != null) {
                    var29 = var29.replaceAll("predecessors.", "dpm.dependency.");
                    ArgMap var31 = var2.getServiceArgs();
                    var31.put("$fields", var29);
                    var2.setServiceArgs(var31);
                }

                ServiceSave.getUpdatedObjects(var0, var2.getServiceReferenceName(), var2);
                return var2.getDatacollection();
            }
        }
    }

    private static void ensureTaskNotActive(Context var0, Dataobject var1, String var2) throws FoundationException {
        String var3 = DataelementMapAdapter.getDataelementValue(var1, SELECTABLE_CURRENT.getName());
        if (!"Create".equals(var3) && !"Assign".equals(var3)) {
            throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", var2, var0.getLocale()));
        }
    }

    public static Dataobject updateTaskAssignees(Context context, String[] var1) throws Exception {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        Dataobject var3 = (Dataobject)var2.getDatacollection().getDataobjects().get(0);
        UpdateActions var4 = var3.getUpdateAction();
        Dataobject var5 = var3.getParent();
        String parentId = var3.getParent().getId();
        Dataobject var7 = getTaskInfo(context, parentId);
        String var8 = var7.getType();
        String var9 = (String)var7.getDataelements().get("to[Project Access Key].from.from[Project Access List].to.type.kindof[Experiment]");
        String var10 = "true".equalsIgnoreCase(var9) ? "Assigned Experiment Tasks" : "Assigned Tasks";
        String var11 = var3.getId();
        String var12 = var3.getRelId();
        String var13;
        Datacollection var15;
        if (var11 == null) {
            var13 = DataelementMapAdapter.getDataelementValue(var3, "name");
            if (var13 != null && !var13.isEmpty()) {
                ArrayList var14 = new ArrayList(1);
                var14.add(SELECTABLE_OBJECTID);
                var15 = gscEvent.getPersonInfo(context, var13, var14, 1);
                if (var15.getDataobjects().isEmpty()) {
                    String var21 = PropertyUtil.getTranslatedValue(context, "CollaborativeTasks", "emxCollaborativeTasks.Error.AssigneeInvalid", context.getLocale());
                    var21 = var21.replace("%ASSIGNEE%", var13);
                    throw new FoundationUserException(var21);
                }

                Dataobject var16 = (Dataobject)var15.getDataobjects().get(0);
                var11 = var16.getId();
                var3.setId(var11);
            }
        }

        List relatedAssignees = new ArrayList();
        MapList eventAssignees = gscEvent.findEventRelated(context, parentId, "Person", "Assigned Tasks", new StringList("physicalid"), null);
        Iterator eventAssigneeItr = eventAssignees.iterator();
        while (eventAssigneeItr.hasNext()) {
            Map eventAssigneeInfo = (Map) eventAssigneeItr.next();
            String eventAssigneeId = eventAssigneeInfo.get("physicalid").toString();
            relatedAssignees.add(eventAssigneeId);
        }

        if (UpdateActions.CONNECT.equals(var4) || UpdateActions.CREATE.equals(var4) || UpdateActions.MODIFY.equals(var4)) {
            String var18;
            if ("Inbox Task".equals(var8)) {
                gscEvent.setRouteTaskAssignee(context, parentId, var11);
            } else {
                HashMap var17 = new HashMap(1);
                var18 = RelelementMapAdapter.getRelelementValue(var3, "allocation");
                addToMap(var17, "Percent Allocation", var18);
                String var20;
                if (UpdateActions.MODIFY.equals(var4)) {
                    var15 = null;
                    if (var12 != null) {
                        var20 = ObjectEditUtil.modifyConnection(context, var12, var17, true, "id");
                    } else {
                        var20 = AssignedTasks.modifyTaskAssignee(context, parentId, var11, var17);
                    }

                    var3.setRelId(var20);
                } else {
                    if (!relatedAssignees.contains(var11)) {
                        var20 = AssignedTasks.addTaskAssignee(context, parentId, var11, var17, var10);
                        var3.setRelId(var20);
                    } else {
                        throw new FoundationException("Error: 이미 연결된 사용자 입니다.");
                    }
                }
            }

            var13 = (String)var2.getServiceArgs().get("$fields");
            if (var13 != null) {
                var13 = var13.replaceAll("assignees.", "tsk.assignees.");
                ArgMap var19 = var2.getServiceArgs();
                var19.put("$fields", var13);
                var2.setServiceArgs(var19);
            }

            ServiceSave.getUpdatedObjects(context, var2.getServiceReferenceName(), var2, var13);
            var3 = (Dataobject)var2.getDatacollection().getDataobjects().get(0);
            if ("Inbox Task".equals(var8)) {
                var18 = "ownerInfo";
                if (var5.getRelateddata() != null) {
                    var5.getRelateddata().remove(var18);
                }

                RelateddataMapAdapter.addRelatedData(var5, var18, var3);
                var3.setParent(var5);
            }
        }

        if (UpdateActions.DISCONNECT.equals(var4) || UpdateActions.DELETE.equals(var4)) {
            if ("Inbox Task".equals(var8)) {
                var3 = null;
            } else if (var12 != null) {
                ObjectEditUtil.disconnect(context, var12);
            } else {
                if (relatedAssignees.contains(var11)) {
                    AssignedTasks.removeTaskAssignee(context, parentId, var11, var10);
                } else {
                    throw new FoundationException("Error: 이미 연결되지 있지 않은 사용자 입니다.");
                }
            }
        }

        return var3;
    }

    private static Dataobject getTaskInfo(Context var0, String var1) throws FoundationException {
        ArrayList var2 = new ArrayList(5);
        var2.add(SELECTABLE_TYPE);
        var2.add(SELECTABLE_CURRENT);
        var2.add(Task.SELECT_ROUTE_TASK_NEEDS_REVIEW);
        var2.add(Task.SELECT_ROUTE_TASK_APPROVAL_STATUS);
        var2.add(Task.SELECT_ROUTE_TASK_ACTION);
        var2.add(Task.SELECT_APPROVERS_COMMENTS);
        var2.add(Task.SELECT_REVIEWERS_COMMENTS);
        var2.add(SELECT_ROUTE_REQUIRES_ESIGN);
        var2.add(SELECTABLE_IS_AN_EXPERIMENT_TASK);
        Dataobject var3 = gscEvent.getTaskInfo(var0, var1, var2);
        return var3;
    }

    public static Dataobject updateTaskScopes(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        Datacollection var3 = var2.getDatacollection();
        Dataobject var4 = (Dataobject)var3.getDataobjects().get(0);
        UpdateActions var5 = var4.getUpdateAction();
        String var6 = var4.getParent().getId();
        if (UpdateActions.CONNECT.equals(var5) || UpdateActions.CREATE.equals(var5)) {
            String var7 = var4.getId();
            if (var7 == null) {
                String var8 = DataelementMapAdapter.getDataelementValue(var4, "name");
                TaskScope.addTaskScopeByName(var0, var6, var8, (Map)null);
            } else {
                TaskScope.addTaskScope(var0, var6, var7, (Map)null);
            }

            var4 = getObject(var0, var2, var7, (String)null, (String)null);
        }

        if (UpdateActions.DISCONNECT.equals(var5) || UpdateActions.DELETE.equals(var5)) {
            TaskScope.removeTaskScope(var0, var6, var4.getId());
        }

        return var4;
    }

    public static Dataobject updateInboxTaskContents(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        Datacollection var3 = var2.getDatacollection();
        Dataobject var4 = (Dataobject)var3.getDataobjects().get(0);
        UpdateActions var5 = var4.getUpdateAction();
        String var6 = var4.getParent().getId();
        ArrayList var7 = new ArrayList();
        Select var8 = new Select("type", "type", ExpressionType.BUS, (Format)null, false);
        var7.add(var8);
        String var9;
        if (UpdateActions.CONNECT.equals(var5) || UpdateActions.CREATE.equals(var5)) {
            var9 = var4.getId();
            String var10 = getRouteId(var6, var0);
            connectInboxTaskContents(var0, var9, var10);
            var4 = getObject(var0, var2, var9, (String)null, (String)null);
        }

        if (UpdateActions.DISCONNECT.equals(var5) || UpdateActions.DELETE.equals(var5)) {
            var9 = getRouteId(var6, var0);
            disconnectInboxTaskContents(var0, var4.getId(), var9);
        }

        return var4;
    }

    public static String getRouteId(String var0, Context var1) throws FoundationException {
        ArrayList var2 = new ArrayList(1);
        var2.add(SELECT_ROUTE_ID);
        Dataobject var3 = ObjectUtil.print(var1, var0, (PrintData)null, var2);
        String var4 = DataelementMapAdapter.getDataelementValue(var3, SELECT_ROUTE_ID.getName());
        return var4;
    }

    public static void connectInboxTaskContents(Context var0, String var1, String var2) throws FoundationException {
        ObjectEditUtil.connect(var0, var1, "Object Route", var2, (Map)null, false);
    }

    public static void disconnectInboxTaskContents(Context var0, String var1, String var2) throws FoundationException {
        ObjectEditUtil.disconnect(var0, var1, "Object Route", var2);
    }

    public static Dataobject updateProjectScope(Context var0, String[] var1) throws FoundationException, FrameworkException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        Datacollection var3 = var2.getDatacollection();
        Dataobject var4 = (Dataobject)var3.getDataobjects().get(0);
        UpdateActions var5 = var4.getUpdateAction();
        Dataobject var6 = var4.getParent();
        String var7 = var6.getId(); // gscEvent object id

        if (UpdateActions.CONNECT.equals(var5) || UpdateActions.CREATE.equals(var5)) {
            ArrayList var8 = new ArrayList(2);
            var8.add(gscEvent.SELECT_TASK_ESTIMATED_DURATION_INPUT_VALUE);
            Dataobject var9 = gscEvent.getTaskInfo(var0, var7, var8);
            String var10 = DataelementMapAdapter.getDataelementValue(var9, gscEvent.SELECT_TASK_ESTIMATED_DURATION_INPUT_VALUE.getName());
            ensureValidTaskDuration(var0, var10);
            String var11 = var4.getId(); // project object id
            gscEvent.addTaskToPhase(var0, "gscEvent", var7, var11, (Map)null);
            var4 = getObject(var0, var2, var11, (String)null, (String)null);
            var2.setServiceName("tasks");
            var6 = getObject(var0, var2, var7, "none", (String)null);
            var4.setParent(var6);

            return var9;

        }

        return var4;
    }

    public static Dataobject updateEventProjects(Context context, String[] var1) throws Exception {
        ServiceParameters serviceParameters = (ServiceParameters)JPOUtil.unpackArgs(var1);
        Datacollection datacollection = serviceParameters.getDatacollection();

        // #########################################################################
        // 230428 HJ 연관 과제 gscGateResult 상태 업데이트 (연관 과졔 Rel gscGateResult 속성 및 과제 gscGateResult 속성 업데이트)
        List<Dataobject> dataobjectList = serviceParameters.getDatacollection().getDataobjects();
        Iterator<Dataobject> iterator = dataobjectList.iterator();

        // Dataobject 마다 실행
        while(iterator.hasNext()) {
            Dataobject dataobject = iterator.next();
            String projectId = dataobject.getId();
            String relId = dataobject.getRelId();

            HashMap hashMap = new HashMap(1);
            String gscGateResult = RelelementMapAdapter.getRelelementValue(dataobject, "gscGateResult");
            // String gscGateResult = DataelementMapAdapter.getDataelementValue(dataobject, "gscGateResult");
            if(gscGateResult != null)
                com.gsc.enovia.dpm.ServiceUtil.addToMap(hashMap, "gscGateResult", gscGateResult);
            try {
                ObjectEditUtil.modifyConnection(context, relId, hashMap, true, ""); // pushContext
                ObjectEditUtil.modify(context, projectId, hashMap, true, true); // applyLog
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 230428 HJ 연관 과제 gscGateResult 상태 업데이트 (연관 과졔 Rel gscGateResult 속성 및 과제 gscGateResult 속성 업데이트)
        // #########################################################################

        // sj.kwak Rel 처리
        Dataobject dataobject = (Dataobject)datacollection.getDataobjects().get(0);
        UpdateActions updateAction = dataobject.getUpdateAction();
        String prjId = dataobject.getId();
        Dataobject dataobjectParent = dataobject.getParent();
        String eventId = dataobjectParent.getId();

        List relatedProjects = new ArrayList();
        MapList eventProjects = gscEvent.findEventRelated(context, eventId, "Project Space", "gscEventProject", new StringList("physicalid"), null);
        Iterator eventProjectItr = eventProjects.iterator();
        while (eventProjectItr.hasNext()) {
            Map eventProjectInfo = (Map) eventProjectItr.next();
            String eventProjectId = eventProjectInfo.get("physicalid").toString();
            relatedProjects.add(eventProjectId);
        }

        if (UpdateActions.CONNECT.equals(updateAction) || UpdateActions.CREATE.equals(updateAction)) {
            if (!relatedProjects.contains(prjId)) {
                ServiceUtil.updateConnection(context, UpdateActions.CREATE, eventId, "gscEventProject", prjId, true, true);
            } else {
                throw new FoundationException("Error: 이미 연결된 과제 입니다.");
            }

        } else if (UpdateActions.DISCONNECT.equals(updateAction)) {
            if (relatedProjects.contains(prjId)) {
                com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, String.format("disconnect bus '%s' rel 'gscEventProject' to '%s'", eventId, prjId));
            } else {
                throw new FoundationException("Error: 이미 연결되지 않은 과제 입니다.");
            }
        }

        DomainObject relatedPrjObj = new DomainObject(prjId);
        Map prjAttr = relatedPrjObj.getAttributeMap(context, true);
        DataelementMap newDMap = new DataelementMap();
        newDMap.putAll(prjAttr);
        dataobject.setDataelements(newDMap);
        dataobject.setId(prjId);

        return dataobject;
        // sj.kwak Rel 처리
    }

    public static Dataobject updateEventTasks(Context context, String[] var1) throws Exception {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        Datacollection var3 = var2.getDatacollection();
        Dataobject var4 = (Dataobject)var3.getDataobjects().get(0);
        UpdateActions var5 = var4.getUpdateAction();
        Dataobject var6 = var4.getParent();
        String eventId = var6.getId();

        List projects = new ArrayList();
        MapList eventTasks = gscEvent.findEventRelated(context, eventId, "Milestone", "gscEventTask", new StringList("physicalid"), null);
        Iterator eventTaskItr = eventTasks.iterator();
        while (eventTaskItr.hasNext()) {
            Map eventTaskInfo = (Map) eventTaskItr.next();
            String eventTaskId = eventTaskInfo.get("physicalid").toString();
            String projectId = ServiceUtil.getObjectInfo(context, "Milestone", "physicalid", eventTaskId, new StringList("to[Subtask].from.physicalid")).get("to[Subtask].from.physicalid").toString();
            projects.add(projectId);
        }

        if (UpdateActions.CONNECT.equals(var5)) {
            StringList s = new StringList();
            s.add("name");
            s.add("attribute[Task Estimated Start Date].value");
            s.add("attribute[Task Estimated Finish Date].value");
            Map eventInfo = ServiceUtil.getObjectInfo(context, "gscEvent", "physicalid", eventId, s);
            String eventName = eventInfo.get("name").toString();
            String estimatedStartDate = eventInfo.get("attribute[Task Estimated Start Date].value").toString();
            String estimatedFinishDate = eventInfo.get("attribute[Task Estimated Finish Date].value").toString();

            eventInfo.clear();
            eventInfo.put("Task Estimated Start Date", estimatedStartDate);
            eventInfo.put("Task Estimated Finish Date", estimatedFinishDate);

            MapList relatedPrjs = gscEvent.findEventRelated(context, eventId, "Project Space", "gscEventProject", new StringList("physicalid"), null);
            Iterator relatedPrjItr = relatedPrjs.iterator();
            while(relatedPrjItr.hasNext()) {
                Map relatedPrj = (Map) relatedPrjItr.next();
                String relatedPrjId = relatedPrj.get("physicalid").toString();

                // HJ 230502 Milestone Assignee 연결 - 연동 과제의 PM(Owner)를 Assigneer 로 연결 - Owner 조회
                String PrjOwner = com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, String.format("print bus '%s' select owner dump;", relatedPrjId));
                String mqlPerson = com.matrixone.apps.domain.util.MqlUtil.mqlCommand(context, String.format("temp query bus 'Person' '%s' * select physicalid dump |;", PrjOwner));
                String PrjOwnerId = mqlPerson.split("\\|")[3];
                // HJ 230502 Milestone Assignee 연결 - 연동 과제의 PM(Owner)를 Assigneer 로 연결 - Owner 조회

                if (!projects.contains(relatedPrjId)) {
                    String milestoneId = gscEvent.createTask(context, "Milestone", eventName, eventInfo, null, null);
                    DomainObject milestoneObj = new DomainObject(milestoneId);
                    milestoneObj.promote(context);

                    ArrayList var8 = new ArrayList(2);
                    var8.add(gscEvent.SELECT_TASK_ESTIMATED_DURATION_INPUT_VALUE);
                    gscEvent.addTaskToPhase(context, "Milestone", milestoneId, relatedPrjId, (Map)null);
                    ServiceUtil.updateConnection(context, UpdateActions.CREATE, eventId, "gscEventTask", milestoneId, true, true);

                    // HJ 230502 Milestone Assignee 연결 - 연동 과제의 PM(Owner)를 Assigneer 로 연결
                    // ServiceUtil.updateConnection(context, UpdateActions.CREATE, PrjOwnerId, "Assigned Tasks", milestoneId, true, false);
                    AssignedTasks.addTaskAssignee(context, milestoneId, PrjOwnerId, (Map)null);
                    // HJ 230502 Milestone Assignee 연결 - 연동 과제의 PM(Owner)를 Assigneer 로 연결

                    Map milestoneAttr = milestoneObj.getAttributeMap(context, true);
                    DataelementMap newDMap = new DataelementMap();
                    newDMap.putAll(milestoneAttr);
                    var4.setDataelements(newDMap);
                    var4.setId(milestoneId);

                }
            }
        }

        return var4;
    }

    public static void updateEventState(Context var0, String[] var1) throws Exception {
        try {
            ContextUtil.startTransaction(var0, true);
            ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
            Datacollection var3 = var2.getDatacollection();
            Dataobject var4 = (Dataobject)var3.getDataobjects().get(0);
            UpdateActions var5 = var4.getUpdateAction();
            Dataobject var6 = var4.getParent();
            String eventId = var6.getId().split("/")[0];
            String type = var4.getType();

            System.out.println("state update");

            DomainObject eventObj = new DomainObject(eventId);
            if (type.equals("promote")) {
                eventObj.promote(var0);
            } else if (type.equals("demote")) {
                eventObj.demote(var0);
            }

            ContextUtil.commitTransaction(var0);
        } catch (Exception e) {
            ContextUtil.abortTransaction(var0);
            e.printStackTrace();
        }
    }

    public static Dataobject updateCalendar(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        Datacollection var3 = var2.getDatacollection();
        Dataobject var4 = (Dataobject)var3.getDataobjects().get(0);
        String var5 = null;
        UpdateActions var6 = UpdateActions.NONE;
        String var7 = "";
        DataelementMap var8 = var4.getDataelements();
        var7 = var4.getParent().getId();
        if (var8 != null && var8.containsKey("Default")) {
            var5 = (String)var8.get("Default");
        }

        String var9 = var4.getId();
        var6 = var4.getUpdateAction();
        if (UpdateActions.CONNECT.equals(var6) || UpdateActions.CREATE.equals(var6) || UpdateActions.MODIFY.equals(var6)) {
            gscEvent.setCalendar(var0, var7, var9, var5);
            String var10 = (String)var2.getServiceArgs().get("$fields");
            if (var10 != null) {
                var10 = var10.replaceAll("calendar.", "tsk.calendar.");
            }

            ServiceSave.getUpdatedObjects(var0, var2.getServiceReferenceName(), var2, var10);
            var4 = (Dataobject)var2.getDatacollection().getDataobjects().get(0);
        }

        if (UpdateActions.DISCONNECT.equals(var6) || UpdateActions.DELETE.equals(var6)) {
            gscEvent.removeCalendar(var0, var7, var9, var5);
        }

        return var4;
    }

    public static Datacollection autoRefreshTasks(Context var0, String[] var1) throws Throwable {
        gscEvent.checkLicenseCollaborativeTasks(var0);
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        ArgMap var3 = var2.getServiceArgs();
        String var5 = (String)var3.remove("refreshWidget");
        List var6 = null;
        if (var5 != null && !var5.isEmpty()) {
            var5 = URLDecoder.decode(var5, "UTF-8");
            Servicedata var7 = ServiceJson.readServicedatafromJson(var5);
            var6 = var7.getData();
        } else {
            var6 = var2.getDatacollection().getDataobjects();
            var2.setDatacollection((Datacollection)null);
        }

        HashMap var19 = new HashMap(10);
        String var10;
        if (var6 != null) {
            Iterator var8 = var6.iterator();

            while(var8.hasNext()) {
                Dataobject var9 = (Dataobject)var8.next();
                var10 = var9.getId();
                if (var10 != null && !var10.isEmpty() && var9.getCestamp() != null && !var9.getCestamp().isEmpty()) {
                    var19.put(var10, var9);
                    var9.setUpdateAction((UpdateActions)null);
                }
            }
        }

        var2.setServiceName("tasks");
        String var20 = (String)var3.get("$include");
        var10 = (String)var3.get("$fields");
        var3.put("$include", "none");
        var3.put("$fields", "none");
        Servicedata var11 = ServiceBase.getData(var0, var2);
        List var12 = var11.getData();
        String var13 = "";
        Iterator var14 = var12.iterator();

        Dataobject var15;
        String var16;
        while(var14.hasNext()) {
            var15 = (Dataobject)var14.next();
            var16 = var15.getId();
            String var17 = var15.getCestamp();
            Dataobject var18;
            if (var19.containsKey(var16)) {
                var18 = (Dataobject)var19.get(var16);
                if (var17.equals(var18.getCestamp())) {
                    var19.remove(var16);
                } else {
                    var18.setUpdateAction(UpdateActions.MODIFY);
                    var13 = var13 + var16 + ",";
                }
            } else {
                var18 = new Dataobject();
                var18.setId(var16);
                var18.setCestamp(var17);
                var18.setUpdateAction(UpdateActions.CREATE);
                var19.put(var16, var18);
                var13 = var13 + var16 + ",";
            }
        }

        if (!var13.isEmpty()) {
            var3.put("$ids", var13);
            var3.put("$include", var20);
            var3.put("$fields", var10);
            var2.setServiceName("tasks");
            var11 = ServiceBase.getData(var0, var2);
            var12 = var11.getData();
            var14 = var12.iterator();

            while(var14.hasNext()) {
                var15 = (Dataobject)var14.next();
                var16 = var15.getId();
                var15.setUpdateAction(((Dataobject)var19.get(var16)).getUpdateAction());
            }
        } else {
            var12.clear();
        }

        var14 = var19.values().iterator();

        while(var14.hasNext()) {
            var15 = (Dataobject)var14.next();
            if (var15.getUpdateAction() == null) {
                var15.setUpdateAction(UpdateActions.DELETE);
                var15.setTempId(var15.getId());
                var15.setCestamp((String)null);
                var15.setId((String)null);
                var12.add(var15);
            }
        }

        Datacollection var21 = new Datacollection();
        var21.getDataobjects().addAll(var12);
        return var21;
    }

    public static Datacollection getEvents(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        return getEvents(var0, var2);
    }

    public static Datacollection getEvents(Context var0, ServiceParameters var1) throws FoundationException {
        HttpServletRequest var2 = var1.getHttpRequest();
        gscEvent.checkLicenseCollaborativeTasks(var0, var2);
        Datacollection var3 = var1.getDatacollection();
        List var4 = var1.getSelects();
        ArgMap var5 = var1.getServiceArgs();

        List<String> eventTypes = null;
        if (var5.containsKey("eventType")) {
            eventTypes = new ArrayList<>();
            String eventTypeVal = var5.get("eventType");

            if (eventTypeVal.contains(",")) {
                eventTypes = List.of(eventTypeVal.split(","));
            } else {
                eventTypes.add(eventTypeVal);
            }
        }

        String var6 = (String)var5.get("showProjectTasks");
        String var7 = (String)var5.get("scopeId");
        String var8 = (String)var5.get("deliverableId");
        String var9 = (String)var5.get("referenceId");
        boolean var10 = Boolean.parseBoolean(var6);
        String var11 = (String)var5.get("currentTaskFilter");
        String var12 = (String)var5.get("fromCompletionDate");
        String var13 = (String)var1.getJpoArgs().get("includeRouteTasks");
        String var14 = (String)var5.get("includeRouteTasks");
        String var15 = null;
        if (var12 != null && !var12.equalsIgnoreCase("all")) {
            Date var16 = FormatUtil.parseISODate(var0, var12, (SimpleDateFormat)null);
            var15 = FormatUtil.formatMxDate(var0, var16, (SimpleDateFormat)null);
        }

        Datacollection var22 = var3;
        if (var3 != null && !var3.getDataobjects().isEmpty()) {
            // Task 하나 조회
            gscEvent.getTaskInfo(var0, var3, var4);
        } else if (var7 != null && !"".equals(var7)) {
            ArrayList var23 = new ArrayList();
            Select var24 = new Select("type", "type", ExpressionType.BUS, (Format)null, false);
            var23.add(var24);
            var23.add(gscEvent.SELECTABLE_ISPROJECTSPACE);
            Dataobject var25 = ObjectUtil.print(var0, var7, (PrintData)null, var23);
            String var27 = DataelementMapAdapter.getDataelementValue(var25, gscEvent.SELECTABLE_ISPROJECTSPACE.getName());
            if ("TRUE".equalsIgnoreCase(var27)) {
                var22 = TaskScope.getProjectScopeTasks(var0, var7, var4, var11, var15);
            } else {
                var22 = TaskScope.getScopeTasks(var0, var7, var4, var11, var15);
            }
        } else if (var8 != null && !var8.isEmpty()) {
            var22 = TaskReferences.getRelatedTasks(var0, "deliverable", var8, var4);
        } else if (var9 != null && !var9.isEmpty()) {
            var22 = TaskReferences.getRelatedTasks(var0, "reference", var9, var4);
        } else {
            String var17 = null;
            if (var10) {
                try {
                    gscEvent.checkLicenseProjectTasks(var0, var2);
                } catch (Exception var21) {
                    var10 = false;
                    var17 = "User: " + var0.getUser() + " has no access to product: DPM";
                    System.out.println(var17);
                }
            }

            String var18 = (String)var5.get("createSampleData");
            String var19 = null;
            if ("TRUE".equalsIgnoreCase(var18)) {
                boolean var20 = generateSampleData(var0);
                var19 = var20 ? null : "Unable to create sample data for user, likely due to permission issues.";
            }

//            var22 = gscEvent.getUserTasks(var0, var4, var11, var10, var15);
            var22 = gscEvent.getUserEvents(var0, var4, var11, var10, var15, eventTypes);
            if ("TRUE".equalsIgnoreCase(var13) || "TRUE".equalsIgnoreCase(var14)) {
                Datacollection var26 = gscEvent.getUserRouteInboxTasks(var0, var4, var15);
                var22.getDataobjects().addAll(var26.getDataobjects());
            }

            ServiceBase.appendInfoMessage(var22, var17);
            ServiceBase.appendInfoMessage(var22, var19);
        }

        return var22;
    }

    public static Datacollection getScopeTasks(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        String var3 = ((Dataobject)var2.getDatacollection().getDataobjects().get(0)).getId();
        ArgMap var4 = var2.getServiceArgs();
        var4.put("scopeId", var3);
        String var5 = var2.getServiceName();
        var5 = "tasks";
        var2.setServiceName(var5);
        Servicedata var6 = ServiceBase.getData(var0, var2);
        Datacollection var7 = new Datacollection();
        var7.getDataobjects().addAll(var6.getData());
        return var7;
    }

    public static Datacollections getCalendar(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        Datacollection var3 = var2.getDatacollection();
        List var4 = var2.getSelects();
        Datacollections var5 = gscEvent.getCalendar(var0, var3, var4);
        return var5;
    }

    public static Range getRangeTypeDerivatives(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        ArgMap var3 = var2.getJpoArgs();
        String var4 = (String)var3.get("relationshiptype");
        String var5 = (String)var3.get("searchtypes");
        if (var4 != null) {
            if (var4.equals("Object Route")) {
                var5 = MqlUtil.mqlCommand(var0, "print rel $1 select $2 dump $3", new String[]{var4, "fromtype", ","});
            } else {
                var5 = MqlUtil.mqlCommand(var0, "print rel $1 select $2 dump $3", new String[]{var4, "totype", ","});
            }
        }

        String var6 = "6W_TYPE_DERIVATIVES:" + var5;
        String var7 = null;
        Class var8 = com.dassault_systemes.enovia.tskv2.TaskService.class;
        synchronized(com.dassault_systemes.enovia.tskv2.TaskService.class) {
            var7 = (String)CacheUtil.getCacheObject(var0, var6);
            if (var7 == null) {
                List var9 = ObjectUtil.getTypeDerivatives(var0, var5);
                var7 = StringUtil.join(var9, ",");
                CacheUtil.setCacheObject(var0, var6, var7);
            }
        }

        Range var12 = new Range();
        Range.Item var13 = new Range.Item();
        var13.setDisplay(var7);
        var13.setValue(var5);
        var12.getItem().add(var13);
        return var12;
    }

    public static Range getRangeTaskTypes(Context var0, String[] var1) throws FoundationException {
        List var2 = gscEvent.getTaskTypes(var0);
        Range var3 = new Range();
        Iterator var4 = var2.iterator();

        while(var4.hasNext()) {
            String var5 = (String)var4.next();
            if (!"VPLM Task".equals(var5) && !"PQP Task".equals(var5)) {
                Range.Item var6 = new Range.Item();
                var3.getItem().add(var6);
                var6.setDisplay(PropertyUtil.getAdminI18NString(var0, "Type", var5, var0.getLocale().getLanguage()));
                var6.setValue(var5);
                var6.setHelpinfo("isatask");
            }
        }

        List var8 = ObjectUtil.getTypeDerivatives(var0, DomainConstants.TYPE_PROJECT_SPACE, true);
        Iterator var9 = var8.iterator();

        while(var9.hasNext()) {
            String var10 = (String)var9.next();
            if (!"Project Baseline".equals(var10) && !"Experiment".equals(var10) && !"Project Snapshot".equals(var10)) {
                Range.Item var7 = new Range.Item();
                var3.getItem().add(var7);
                var7.setDisplay(PropertyUtil.getAdminI18NString(var0, "Type", var10, var0.getLocale().getLanguage()));
                var7.setValue(var10);
                var7.setHelpinfo("isaproject");
            }
        }

        return var3;
    }

    public static Range getRouteTaskApprovalActions(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        ArgMap var3 = var2.getJpoArgs();
        String var4 = (String)var3.get("attribute");
        Range var5 = PropertyUtil.getAttributeRange(var0, var4);
        String var6 = PropertyUtil.getTranslatedValue(var0, "emxComponents.properties", "emxComponents.Routes.ShowAbstainForTaskApproval", var0.getLocale());

        for(int var7 = 0; var7 < var5.getItem().size(); ++var7) {
            Range.Item var8 = (Range.Item)var5.getItem().get(var7);
            if ((!"FALSE".equalsIgnoreCase(var6) || !"Abstain".equals(var8.getValue())) && !"Ignore".equals(var8.getValue()) && !"Signature Reset".equals(var8.getValue())) {
                if ("None".equals(var8.getValue())) {
                    var8.setDisplay("");
                }
            } else {
                var5.getItem().remove(var7--);
            }
        }

        return var5;
    }

    public Datacollection getPerson(Context var1, String[] var2) throws FoundationException {
        ServiceParameters var3 = (ServiceParameters)JPOUtil.unpackArgs(var2);
        ArgMap var4 = var3.getJpoArgs();
        List var5 = var3.getSelects();
        List var6 = var3.getDatacollection().getDataobjects();
        ArrayList var7 = new ArrayList(var6.size() / 2);
        Iterator var8 = var6.iterator();

        while(var8.hasNext()) {
            Dataobject var9 = (Dataobject)var8.next();
            String var10 = DataelementMapAdapter.getDataelementValue(var9, "ownerName");
            String var11 = DataelementMapAdapter.getDataelementValue(var9, "originatorName");
            if (var10 != null && !var10.isEmpty() && !this._persons.containsKey(var10) && var7.indexOf(var10) == -1) {
                var7.add(var10);
            }

            if (var11 != null && !var11.isEmpty() && !this._persons.containsKey(var11) && var7.indexOf(var11) == -1) {
                var7.add(var11);
            }
        }

        String var17 = StringUtil.join(var7, ",");
        List var18 = gscEvent.getPersonInfo(var1, var17, var5, 0).getDataobjects();

        for(int var19 = 0; var19 < var18.size(); ++var19) {
            Dataobject var21 = (Dataobject)var18.get(var19);
            String var12 = DataelementMapAdapter.getDataelementValue(var21, "name.transient");
            this._persons.put(var12, var21);
        }

        boolean var20 = "TRUE".equalsIgnoreCase((String)var4.get("owner"));
        Datacollection var22 = new Datacollection();
        List var23 = var22.getDataobjects();
        Iterator var13 = var6.iterator();

        while(var13.hasNext()) {
            Dataobject var14 = (Dataobject)var13.next();
            Dataobject var15 = null;
            String var16 = var20 ? DataelementMapAdapter.getDataelementValue(var14, "ownerName") : DataelementMapAdapter.getDataelementValue(var14, "originatorName");
            var15 = (Dataobject)this._persons.get(var16);
            var23.add(var15);
        }

        return var22;
    }

    private static void addToMap(Map<String, String> var0, String var1, String var2) {
        if (var2 != null) {
            var0.put(var1, var2);
        }

    }

    private static void removeEmptyFromMap(Map<String, String> var0, String var1) {
        if ("".equals(var0.get(var1))) {
            var0.remove(var1);
        }

    }

    protected static Dataobject getObject(Context var0, ServiceParameters var1, String var2, String var3, String var4) throws FoundationException {
        String var5 = var1.getServiceName();
        var1.setServiceName(var5 + "/" + var2);
        if (var3 != null) {
            var1.getServiceArgs().put("$include", var3);
        }

        if (var4 != null) {
            var1.getServiceArgs().put("$fields", var4);
        }

        Servicedata var6 = ServiceBase.getData(var0, var1);
        Dataobject var7 = var6.getData().isEmpty() ? null : (Dataobject)var6.getData().get(0);
        return var7;
    }

    public static Range getStates(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        ArgMap var3 = var2.getJpoArgs();
        String var4 = (String)var3.get("policy");
        String var5 = (String)var3.get("usecase");
        Range var6 = gscEvent.getStates(var0, var4);
        String var7 = (String)var3.get("projectState");
        if ("Route Task".equals(var5)) {
            for(int var8 = 0; var8 < var6.getItem().size(); ++var8) {
                String var9 = ((Range.Item)var6.getItem().get(var8)).getValue();
                if ("Create".equals(var9) || "Active".equals(var9)) {
                    var6.getItem().remove(var8--);
                }
            }
        }

        if ("Project Space Hold Cancel".equals(var7)) {
            Range var10 = gscEvent.getStates(var0, var7);
            var6.getItem().addAll(var10.getItem());
        }

        return var6;
    }

    public static Datacollection getTaskAssignNotificationPreference(Context var0, String[] var1) throws FoundationException {
        Datacollection var2 = new Datacollection();
        Dataobject var3 = new Dataobject();
        var2.getDataobjects().add(var3);
        boolean var4 = Task.getTaskAssignNotificationPreference(var0);
        DataelementMapAdapter.setDataelementValue(var3, "preference_AssignedTaskNotificationOn", var4 + "");
        return var2;
    }

    public static Dataobject updateTaskAssignNotificationPreference(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        Datacollection var3 = var2.getDatacollection();
        List var4 = var3.getDataobjects();
        Dataobject var5 = (Dataobject)var4.get(0);
        UpdateActions var6 = var5.getUpdateAction();
        if (UpdateActions.MODIFY.equals(var6)) {
            String var7 = DataelementMapAdapter.getDataelementValue(var5, "preference_AssignedTaskNotificationOn");
            gscEvent.setTaskAssignNotificationPreference(var0, Boolean.valueOf(var7));
        }

        return var5;
    }

    public static void getModifyAccess(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        String var3 = (String)var2.getJpoArgs().get("FieldName");
        String var4 = (String)var2.getJpoArgs().get("stateField");
        List var5 = var2.getDatacollection().getDataobjects();
        Iterator var6 = var5.iterator();

        while(var6.hasNext()) {
            Dataobject var7 = (Dataobject)var6.next();
            String var8 = DataelementMapAdapter.getDataelementValue(var7, var4);
            if ("Complete".equals(var8)) {
                DataelementMapAdapter.setDataelementValue(var7, var3, "FALSE");
            }
        }

    }

    public static void getDeleteAccess(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        String var3 = (String)var2.getJpoArgs().get("FieldName");
        String var4 = (String)var2.getJpoArgs().get("stateField");
        String var5 = (String)var2.getJpoArgs().get("palIdField");
        List var6 = var2.getDatacollection().getDataobjects();
        Iterator var7 = var6.iterator();

        while(var7.hasNext()) {
            Dataobject var8 = (Dataobject)var7.next();
            String var9 = DataelementMapAdapter.getDataelementValue(var8, var3);
            String var10 = DataelementMapAdapter.getDataelementValue(var8, var5);
            if (!"FALSE".equalsIgnoreCase(var9) && var10 != null && !var10.isEmpty()) {
                String var11 = DataelementMapAdapter.getDataelementValue(var8, var4);
                if (!"Create".equals(var11) && !"Assign".equals(var11)) {
                    DataelementMapAdapter.setDataelementValue(var8, var3, "FALSE");
                }
            }
        }

    }

    private static boolean generateSampleData(Context context) throws FoundationException {
        boolean var1 = true;
        String var2 = ContextUtil.getPersonProperty(context, "preference_SampleDataCreated");
        if ("TRUE".equalsIgnoreCase(var2)) {
            return var1;
        } else {
            ContextUtil.setSavePoint(context, "sample_data");

            try {
                ArrayList var3 = new ArrayList(1);
                Select var4 = new Select("physicalid", "physicalid", ExpressionType.BUS, (Format)null, false);
                var3.add(var4);
                Datacollection var5 = gscEvent.getPersonInfo(context, context.getUser(), var3, 1);
                String var6 = ((Dataobject)var5.getDataobjects().get(0)).getId();
                Date var7 = new Date();
                String taskname = "emxCollaborativeTasks.SampleData.Task1.name";
                String taskdescriptions = "emxCollaborativeTasks.SampleData.Task1.description";
                String var10 = "5";
                createSampleTask(context, taskname, taskdescriptions, var10, var7, var6);
                Date var11 = FormatUtil.computeDate(1);
                taskname = "emxCollaborativeTasks.SampleData.Task2.name";
                taskdescriptions = "emxCollaborativeTasks.SampleData.Task2.description";
                var10 = "2";
                createSampleTask(context, taskname, taskdescriptions, var10, var11, var6);
                Date var12 = FormatUtil.computeDate(3);
                taskname = "emxCollaborativeTasks.SampleData.Task3.name";
                taskdescriptions = "emxCollaborativeTasks.SampleData.Task3.description";
                var10 = "3";
                createSampleTask(context, taskname, taskdescriptions, var10, var12, var6);
                ContextUtil.setPersonProperty(context, "preference_SampleDataCreated", "TRUE");
            } catch (Exception var13) {
                ContextUtil.abortSavePoint(context, "sample_data");
                var1 = false;
                System.out.println("Error: unable to create sample tasks for TSK user: " + var13.getMessage());
            }

            return var1;
        }
    }

    private static void createSampleTask(Context context, String taskname, String taskdescriptions, String var3, Date var4, String var5) throws FoundationException {
        String var6 = PropertyUtil.getTranslatedValue(context, "CollaborativeTasks", taskname, context.getLocale());
        String var7 = PropertyUtil.getTranslatedValue(context, "CollaborativeTasks", taskdescriptions, context.getLocale());
        String var8 = FormatUtil.formatMxDate(context, var4, (SimpleDateFormat)null);
        HashMap var9 = new HashMap();
        var9.put("description", var7);
        var9.put("Task Estimated Duration", var3);
        var9.put("Task Estimated Finish Date", var8);
        String taskId = Task.createTask(context, var6, var9);
        AssignedTasks.addTaskAssignee(context, taskId, var5, (Map)null);
        String var11 = context.getUser() + "_PRJ";
        MqlUtil.mqlCommand(context, "modify bus $1 add ownership $2 $3 for $4 as $5", new String[]{taskId, "-", var11, "Multiple Ownership For Object", "delete"});
    }

    public static void getSubscribed(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        List var3 = var2.getDatacollection().getDataobjects();
        String var4 = (String)var2.getJpoArgs().get("FieldName");
        Task.getSubscribed(var0, var3, var4);
    }

    public static void overrideRouteTaskState(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        String var3 = (String)var2.getJpoArgs().get("FieldName");
        List var4 = var2.getDatacollection().getDataobjects();
        Iterator var5 = var4.iterator();

        while(var5.hasNext()) {
            Dataobject var6 = (Dataobject)var5.next();
            String var7 = DataelementMapAdapter.getDataelementValue(var6, "state");
            if ("Assigned".equals(var7)) {
                DataelementMapAdapter.setDataelementValue(var6, var3, "Assign");
            }
        }

    }

    private static void handleFormatPattern(Context var0, Dataobject var1, Map<String, String> var2, UpdateActions var3) throws FoundationException {
        String var4 = DataelementMapAdapter.getDataelementValue(var1, "pattern");
        String var5 = "";
        if (var4 != null && !var4.isEmpty()) {
            String var6 = var1.getId();
            String var7 = (String)var2.get("FormatPattern");
            if (UpdateActions.MODIFY.equals(var3)) {
                ArrayList var8 = new ArrayList(1);
                if (var7 != null) {
                    var8.add(new Select("hasInterface", "interface[DPMGanttChart]", ExpressionType.BUS, (Format)null, false));
                }

                Dataobject var9 = gscEvent.getTaskInfo(var0, var6, var8);
                var5 = DataelementMapAdapter.getDataelementValue(var9, "hasInterface");
            }

            if ("false".equalsIgnoreCase(var5)) {
                MqlUtil.mqlCommand(var0, "modify bus $1 add $2 $3", new String[]{var6, "interface", "DPMGanttChart"});
                var2.put("FormatPattern", var7);
            }
        }

    }

    public static Datacollections getCoOwners(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        Datacollection var3 = var2.getDatacollection();
        List var4 = var2.getSelects();
        Datacollections var5 = gscEvent.getCoOwners(var0, var3, var4);
        return var5;
    }

    public static Datacollection updateCoOwners(Context var0, String[] var1) throws FoundationException {
        ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
        Datacollection var3 = var2.getDatacollection();
        Iterator var4 = var3.getDataobjects().iterator();

        while(true) {
            Dataobject var5;
            UpdateActions var6;
            String var8;
            String var9;
            String var10;
            label26:
            do {
                while(var4.hasNext()) {
                    var5 = (Dataobject)var4.next();
                    var6 = var5.getUpdateAction();
                    Dataobject var7 = var5.getParent();
                    var8 = var7.getId();
                    var9 = var5.getId();
                    var10 = DataelementMapAdapter.getDataelementValue(var5, "name");
                    if (!UpdateActions.CREATE.equals(var6) && !UpdateActions.CONNECT.equals(var6)) {
                        continue label26;
                    }

                    Dataobject var11 = gscEvent.addCoOwner(var0, var8, var9, var10);
                    var5.setId(var11.getId());
                }

                ServiceSave.getUpdatedObjects(var0, var2.getServiceReferenceName(), var2);
                return var3;
            } while(!UpdateActions.DELETE.equals(var6) && !UpdateActions.DISCONNECT.equals(var6));

            gscEvent.removeCoOwner(var0, var8, var9, var10);
            var5.setId((String)null);
        }
    }

    static {
        STATE_MAPPINGS.put("Create", "Create");
        STATE_MAPPINGS.put("Assign", "Assign");
        STATE_MAPPINGS.put("Active", "Active");
        STATE_MAPPINGS.put("Review", "Review");
        STATE_MAPPINGS.put("Complete", "Complete");
        SELECT_ROUTE_ID = new Select((String)null, "from[Route Task].to.id", (ExpressionType)null, (Format)null, true);
        SELECT_ROUTE_REQUIRES_ESIGN = new Select((String)null, "from[Route Task].to.attribute[Requires ESign].value", (ExpressionType)null, (Format)null, true);
        SELECTABLE_IS_AN_EXPERIMENT_TASK = new Select((String)null, "to[Project Access Key].from.from[Project Access List].to.type.kindof[Experiment]", (ExpressionType)null, (Format)null, false);
    }
}
