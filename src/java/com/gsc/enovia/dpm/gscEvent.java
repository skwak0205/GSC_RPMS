package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.e6w.foundation.DBUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationUserException;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceConstants;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceSave;
import com.dassault_systemes.enovia.e6wv2.foundation.db.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.*;
import com.dassault_systemes.enovia.e6wv2.foundation.util.FormatUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.util.LicenseUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.util.StringUtil;
import com.dassault_systemes.enovia.tskv2.SubscriptionUtil;
import com.gsc.util.ServiceUtil;
import com.matrixone.apps.common.InboxTask;
import com.matrixone.apps.common.ProjectManagement;
import com.matrixone.apps.common.Task;
import com.matrixone.apps.common.TaskDateRollup;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.EnoviaResourceBundle;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.framework.ui.UIUtil;
import com.matrixone.apps.program.ProgramCentralConstants;
import matrix.db.BusinessObject;
import matrix.db.BusinessObjectWithSelect;
import matrix.db.BusinessObjectWithSelectList;
import matrix.db.Context;
import matrix.util.StringList;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

public class gscEvent implements ServiceConstants {
    protected static final String REGISTERED_SUITE = "CollaborativeTasks";
    protected static final String CONSTANT_DECISION_APPROVED = "Approve";
    protected static final String TYPE_PERSON = "Person";
    protected static final String TYPE_TASK = "Task";
    protected static final String TYPE_GATE = "Gate";
    protected static final String TYPE_MILESTONE = "Milestone";
    public static final String TYPE_PHASE = "Phase";
    protected static final String TYPE_TASK_MANAGEMENT = "Task Management";
    protected static final String TYPE_DECISION = "Decision";
    protected static final String POLICY_PROJECT_TASK = "Project Task";
    protected static final String POLICY_PROJECT_REVIEW = "Project Review";
    protected static final String POLICY_DECISION = "Decision";
    protected static final String ATTRIBUTE_ORIGINATOR = "Originator";
    protected static final String ATTRIBUTE_PERCENT_COMPLETE = "Percent Complete";
    protected static final String ATTRIBUTE_TASK_ESTIMATED_START_DATE = "Task Estimated Start Date";
    protected static final String ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE = "Task Estimated Finish Date";
    protected static final String ATTRIBUTE_TASK_ESTIMATED_DURATION = "Task Estimated Duration";
    protected static final String ATTRIBUTE_TASK_ACTUAL_START_DATE = "Task Actual Start Date";
    protected static final String ATTRIBUTE_TASK_ACTUAL_FINISH_DATE = "Task Actual Finish Date";
    protected static final String ATTRIBUTE_DEFAULT_CONSTRAINT_TYPE = "Default Constraint Type";
    protected static final String ATTRIBUTE_TASK_CONSTRAINT_TYPE = "Task Constraint Type";
    protected static final String ATTRIBUTE_TASK_CONSTRAINT_DATE = "Task Constraint Date";
    protected static final String ATTRIBUTE_TASK_CONSTRAINT_TYPE_RANGE_ASAP = "As Soon As Possible";
    protected static final String ATTRIBUTE_TASK_CONSTRAINT_TYPE_RANGE_ALAP = "As Late As Possible";
    protected static final String ATTRIBUTE_TASK_CONSTRAINT_TYPE_RANGE_FNET = "Finish No Earlier Than";
    protected static final String ATTRIBUTE_TASK_CONSTRAINT_TYPE_RANGE_FNLT = "Finish No Later Than";
    protected static final String ATTRIBUTE_TASK_CONSTRAINT_TYPE_RANGE_MFON = "Must Finish On";
    protected static final String ATTRIBUTE_TASK_CONSTRAINT_TYPE_RANGE_MSON = "Must Start On";
    protected static final String ATTRIBUTE_TASK_CONSTRAINT_TYPE_RANGE_SNET = "Start No Earlier Than";
    protected static final String ATTRIBUTE_TASK_CONSTRAINT_TYPE_RANGE_SNLT = "Start No Later Than";
    protected static final String ATTRIBUTE_DEPENDENCY_TYPE = "Dependency Type";
    protected static final String ATTRIBUTE_DEPENDENCY_LAG_TIME = "Lag Time";
    protected static final String ATTRIBUTE_DEPENDENCY_TYPE_RANGE_FS = "FS";
    protected static final String ATTRIBUTE_DEPENDENCY_TYPE_RANGE_SF = "SF";
    protected static final String ATTRIBUTE_DEPENDENCY_TYPE_RANGE_FF = "FF";
    protected static final String ATTRIBUTE_DEPENDENCY_TYPE_RANGE_SS = "SS";
    protected static final String ATTRIBUTE_SCHEDULED_FROM_RANGE_START = "Project Start Date";
    protected static final String ATTRIBUTE_SCHEDULED_FROM_RANGE_FINISH = "Project Finish Date";
    protected static final String ATTRIBUTE_SOURCE_ID = "Source Id";
    protected static final String ATTRIBUTE_NEEDS_REVIEW = "Needs Review";
    protected static final String ATTRIBUTE_SEQUENCE_ORDER = "Sequence Order";
    protected static final String ATTRIBUTE_TASK_WBS = "Task WBS";
    protected static final String RELATIONSHIP_SUBTASK = "Subtask";
    protected static final String RELATIONSHIP_DEPENDENCY = "Dependency";
    protected static final String RELATIONSHIP_PROJECT_ACCESS_KEY = "Project Access Key";
    protected static final String RELATIONSHIP_CALENDAR = "Calendar";
    protected static final String RELATIONSHIP_DEFAULT_CALENDAR = "Default Calendar";
    protected static final String SELECT_PAL_ID = "to[Project Access List,Project Access Key].from.id";
    protected static final String RELATIONSHIP_DECISION = "Decision";
    protected static final String STATE_PROJECT_TASK_CREATE = "Create";
    protected static final String STATE_PROJECT_TASK_ASSIGN = "Assign";
    protected static final String STATE_PROJECT_TASK_ACTIVE = "Active";
    protected static final String STATE_PROJECT_TASK_REVIEW = "Review";
    protected static final String STATE_PROJECT_TASK_COMPLETE = "Complete";
    public static final String TASK_FILTER_ALL = "all";
    public static final String TASK_FILTER_OWNED = "owned";
    public static final String TASK_FILTER_ASSIGNED = "assigned";
    protected static final String ASSIGN_TASK_NOTIFICATION = "preference_AssignedTaskNotificationOn";
    public static final Select SELECT_TASK_ESTIMATED_DURATION = new Select("duration", DomainObject.getAttributeSelect("Task Estimated Duration"), (ExpressionType)null, (Format)null, false);
    public static final Select SELECT_TASK_ESTIMATED_FINISH_DATE = new Select("dueDate", DomainObject.getAttributeSelect("Task Estimated Finish Date"), (ExpressionType)null, (Format)null, false);
    public static final Select SELECT_TASK_ESTIMATED_START_DATE = new Select("start date", DomainObject.getAttributeSelect("Task Estimated Start Date"), (ExpressionType)null, (Format)null, false);
    public static final Select SELECT_TASK_ACTUAL_FINISH_DATE = new Select("actual Finish Date", DomainObject.getAttributeSelect("Task Actual Finish Date"), (ExpressionType)null, (Format)null, false);
    public static final Select SELECT_TASK_ACTUAL_START_DATE = new Select("actual Start Date", DomainObject.getAttributeSelect("Task Actual Start Date"), (ExpressionType)null, (Format)null, false);
    public static final Select SELECT_TASK_ESTIMATED_DURATION_INPUT_VALUE = new Select("durationInputValue", DomainObject.getAttributeSelect("Task Estimated Duration") + ".inputvalue", (ExpressionType)null, (Format)null, false);
    public static final Select SELECT_TASK_ESTIMATED_DURATION_INPUT_UNIT = new Select("durationInputUnit", DomainObject.getAttributeSelect("Task Estimated Duration") + ".inputunit", (ExpressionType)null, (Format)null, false);
    private static final Select SELECT_TASK_PAL_ID = new Select((String)null, "to[Project Access List,Project Access Key].from.id", (ExpressionType)null, (Format)null, false);
    private static final Select SELECT_TASK_PROJECT_ID = new Select((String)null, "to[Project Access Key].from.from[Project Access List].to.id", (ExpressionType)null, (Format)null, false);
    private static final Select SELECT_TASK_CONNECTION_IDS = new Select((String)null, "to[Subtask].id", (ExpressionType)null, (Format)null, true);
    private static final Select SELECT_TASK_CONNECTION_PAL_IDS = new Select((String)null, "to[Subtask].from.to[Project Access List,Project Access Key].from.id", (ExpressionType)null, (Format)null, true);
    private static final Select SELECT_TASK_OBJECT_ID = new Select("objectId", "id", (ExpressionType)null, (Format)null, false);
    public static final Select SELECT_NEEDS_REVIEW = new Select((String)null, DomainObject.getAttributeSelect("Needs Review"), (ExpressionType)null, (Format)null, false);
    public static final Select SELECT_ROUTE_TASK_NEEDS_REVIEW = new Select((String)null, DomainObject.getAttributeSelect("Review Task"), (ExpressionType)null, (Format)null, false);
    public static final Select SELECT_ROUTE_TASK_APPROVAL_STATUS = new Select((String)null, DomainObject.getAttributeSelect("Approval Status"), (ExpressionType)null, (Format)null, false);
    public static final Select SELECT_ROUTE_TASK_ACTION = new Select((String)null, DomainObject.getAttributeSelect("Route Action"), (ExpressionType)null, (Format)null, false);
    public static final Select SELECT_APPROVERS_COMMENTS = new Select((String)null, DomainObject.getAttributeSelect("Comments"), (ExpressionType)null, (Format)null, false);
    public static final Select SELECT_REVIEWERS_COMMENTS = new Select((String)null, DomainObject.getAttributeSelect("Reviewers Comments"), (ExpressionType)null, (Format)null, false);
    protected static final Select SELECTABLE_SEQUENCE_ORDER;
    protected static final Select SELECTABLE_TASK_WBS;
    protected static final Select SELECTABLE_IS_PROJECT_TASK;
    protected static final Select SELECTABLE_HAS_DELETE_ACCESS;
    protected static final Select SELECTABLE_IS_SUMMARY_TASK;
    protected static final Select SELECTABLE_ISTASK;
    protected static final Select SELECTABLE_ATTRIBUTE_TASKWEIGHTAGE_FROM_TASK;
    public static final Select ATTRIBUTE_TASKWEIGHTAGE;
    protected static final Select SELECTABLE_ISPROJECTSPACE;
    protected static final Select SELECTABLE_ISPROJECTCONCEPT;
    protected static final Map _relAttributesData;
    protected static final Select SELECTABLE_DEFAULT_CONSTRAINT_TYPE;
    protected static final Select SELECTABLE_TASK_CONSTRAINT_TYPE;
    protected static final String SELECTABLE_TASK_SOURCE_ID = "attribute[Source Id]";
    public static final String SUBSCRIPTION_EVENT_ALL = "TSKTaskTransaction";

    public static final String SELECT_PAL_PHYSICALID_FROM_PROJECT = "to[Project Access List].from.physicalid";

    public static final String SELECT_PAL_PHYSICALID_FROM_TASK = "to[Project Access Key].from.physicalid";

    public gscEvent() {
    }

    public static String createTask(Context var0, String var1, Map<String, String> var2) throws FoundationException {
        return createTask(var0, "Task", var1, var2);
    }

    public static String createTask(Context var0, String var1, String var2, Map<String, String> var3, String var4) throws FoundationException {
        return createTask(var0, var1, var2, var3, var4, (String)null);
    }

    public static String createTask(Context var0, String var1, String var2, Map<String, String> var3, String var4, String var5) throws FoundationException {
        String var6 = var1 == null ? "Task" : var1;
        if (("Gate".equalsIgnoreCase(var6) || "Milestone".equalsIgnoreCase(var6)) && var3 != null) {
            var3.put("Task Estimated Duration", "0");
        }

        if (var4 == null || var4.isEmpty()) {
            var4 = getDefaultPolicy(var0, var6);
            if (var4 == null || var4.isEmpty()) {
                var4 = "Project Task";
                if ("Gate".equalsIgnoreCase(var6) || "Milestone".equalsIgnoreCase(var6)) {
                    var4 = "Project Review";
                }
            }
        }

        String var7 = ObjectEditUtil.create(var0, var6, var2, ServiceUtil.getUniqueRevision(0, "", 0), var4, (String)null, (String)null, false, false, (Map)null);
        if (var5 != null && !var5.isEmpty()) {
            ObjectEditUtil.createObjectOwnership(var0, var7, var5, "Multiple Ownership For Object");
        }

        Object var8 = var3 != null ? var3 : new HashMap();
        if (!((Map)var8).containsKey("Source Id")) {
            ((Map)var8).put("Source Id", var7);
        }

        ObjectEditUtil.modify(var0, var7, (Map)var8, false);
        return var7;
    }

    public static String createTask(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        String var4 = "";
        return createTask(var0, var1, var2, var3, var4);
    }

    public static String getDefaultPolicy(Context var0, String var1) throws FoundationException {
        String var2 = null;

        try {
            Class var3 = Class.forName("com.matrixone.apps.program.ProgramCentralUtil");
            Constructor var4 = var3.getConstructor();
            Object var5 = var4.newInstance();
            Method var6 = var3.getDeclaredMethod("getDefaultPolicy", Context.class, String.class);
            Object var7 = var6.invoke(var5, var0, var1);
            if (var7 != null) {
                var2 = var7.toString();
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return var2;
    }

    public static Datacollection getUserTasks(Context var0, List<Selectable> var1) throws FoundationException {
        return getUserTasks(var0, var1, "all", false);
    }

    public static Datacollection getUserTasks(Context var0, List<Selectable> var1, String var2, boolean var3) throws FoundationException {
        return getUserTasks(var0, var1, var2, var3, (String)null);
    }

    public static Datacollection getUserTasks(Context var0, List<Selectable> var1, String var2, boolean var3, String var4) throws FoundationException {
        checkLicenseCollaborativeTasks(var0);
        if (var1 != null && !var1.isEmpty()) {
            boolean var5 = true;
            boolean var6 = true;
            if ("owned".equals(var2)) {
                var6 = false;
            } else if ("assigned".equals(var2)) {
                var5 = false;
            }

            String var7 = "((!{to[Project Access Key]})";
            if (var3) {
                checkLicenseProjectTasks(var0, (HttpServletRequest)null);
                var7 = var7 + " || ((!{from[Subtask]})  &&";
                var7 = var7 + " !(to[Project Access Key].from.from[Project Access List].to.type matchlist 'Project Template,Project Baseline,Project Snapshot,Experiment', ',' ||";
                var7 = var7 + "   to[Project Access Key].from.from[Project Access List].to.current matchlist 'Hold,Cancel', ',')))";
            } else {
                var7 = var7 + ")";
            }

            if (var4 != null && !var4.isEmpty()) {
                var7 = var7 + " && (attribute[Task Actual Finish Date]=='' || attribute[Task Actual Finish Date]>'" + var4 + "' )";
            }

            String var8 = "";

            try {
                var8 = EnoviaResourceBundle.getProperty(var0, "emxCollaborativeTasks.TaskExclusionList");
            } catch (FrameworkException var14) {
            }

            if (var8 != null && !var8.isEmpty()) {
                var7 = "!(type matchlist '" + var8 + "', ',') && " + var7;
            }

            String var9 = "(owner == context.user || ownership=='-|" + var0.getUser() + "_PRJ|co-owner')";
            Datacollection var10 = null;
            if (var5) {
                String var11 = var9 + " && " + var7;
                QueryData var12 = new QueryData();
                var12.setTypePattern("Task,Milestone,Gate,Phase");
                var12.setWhereExpression(var11);

                var10 = ObjectUtil.query(var0, var12, var1);
            }

            Datacollection var15 = null;
            if (var6) {
                Dataobject var16 = ContextUtil.getPersonObject(var0);
                var7 = "current != 'Create' && " + var7;
                if (var5) {
                    var7 = "(!" + var9 + ") && " + var7;
                }

                ExpandData var13 = new ExpandData();
                var13.setRelationshipPattern("Assigned Tasks");
                var13.setTypePattern("Task,Milestone,Gate,Phase");
                var13.setGetFrom(true);
                var13.setObjectWhere(var7);
                var15 = ObjectUtil.expand(var0, var16, var13, var1);
            }

            Datacollection var17 = new Datacollection();
            if (var10 != null) {
                var17.getDataobjects().addAll(var10.getDataobjects());
            }

            if (var15 != null) {
                var17.getDataobjects().addAll(var15.getDataobjects());
            }

            return var17;
        } else {
            throw new FoundationException("You must provide some selectables to Task.getUserTasks");
        }
    }

        public static Datacollection getUserEvents(Context var0, List<Selectable> var1, String var2, boolean var3, String var4, List<String> eventTypes) throws FoundationException {
        checkLicenseCollaborativeTasks(var0);
        if (var1 != null && !var1.isEmpty()) {
            boolean var5 = true;
            boolean var6 = true;
            if ("owned".equals(var2)) {
                var6 = false;
            } else if ("assigned".equals(var2)) {
                var5 = false;
            }

            String var7 = "((!{to[Project Access Key]})";
            if (var3) {
                checkLicenseProjectTasks(var0, (HttpServletRequest)null);
                var7 = var7 + " || ((!{from[Subtask]})  &&";
                var7 = var7 + " !(to[Project Access Key].from.from[Project Access List].to.type matchlist 'Project Template,Project Baseline,Project Snapshot,Experiment', ',' ||";
                var7 = var7 + "   to[Project Access Key].from.from[Project Access List].to.current matchlist 'Hold,Cancel', ',')))";
            } else {
                var7 = var7 + ")";
            }

            if (var4 != null && !var4.isEmpty()) {
                var7 = var7 + " && (attribute[Task Actual Finish Date]=='' || attribute[Task Actual Finish Date]>'" + var4 + "' )";
            }

            String var8 = "";

            try {
                var8 = EnoviaResourceBundle.getProperty(var0, "emxCollaborativeTasks.TaskExclusionList");
            } catch (FrameworkException var14) {
            }

            if (var8 != null && !var8.isEmpty()) {
                var7 = "!(type matchlist '" + var8 + "', ',') && " + var7;
            }

            String var9 = "(owner == context.user || ownership=='-|" + var0.getUser() + "_PRJ|co-owner')";
            Datacollection var10 = null;
            if (var5) {
                QueryData var12 = new QueryData();
                var12.setTypePattern("gscEvent");

                StringBuilder sb = new StringBuilder();
                sb.append(var9);
                if (eventTypes != null) {
                    sb.append(" && ");
                    for (int i = 0; i < eventTypes.size(); i++) {
                        if (i > 0) {
                            sb.append(" or");
                        }
                        sb.append(" attribute[gscEventType].value == " + eventTypes.get(i));
                    }
                }
                System.out.println("where >>> " + sb.toString());
                var12.setWhereExpression(sb.toString());
                var10 = ObjectUtil.query(var0, var12, var1);
            }

            Datacollection var15 = null;
            if (var6) {
                Dataobject var16 = ContextUtil.getPersonObject(var0);
                var7 = "current != 'Create' && " + var7;
                if (var5) {
                    var7 = "(!" + var9 + ") && " + var7;
                }

                ExpandData var13 = new ExpandData();
                var13.setRelationshipPattern("Assigned Tasks");
                var13.setTypePattern("Task,Milestone,Gate,Phase");
                var13.setGetFrom(true);
                var13.setObjectWhere(var7);
                var15 = ObjectUtil.expand(var0, var16, var13, var1);
            }

            Datacollection var17 = new Datacollection();
            if (var10 != null) {
                var17.getDataobjects().addAll(var10.getDataobjects());
            }

            if (var15 != null) {
                var17.getDataobjects().addAll(var15.getDataobjects());
            }

            return var17;
        } else {
            throw new FoundationException("You must provide some selectables to Task.getUserTasks");
        }
    }

    public static String addTaskToPhase(Context var0, String objectType, String var1, String var2, Map<String, String> var3) throws FoundationException {
        try {
            // var1 : gscEvent Object Id
            // var2 : Project Object Id
            /*Calendar var4 = Calendar.getInstance();
            var4.set(10, 8);
            var4.set(12, 0);
            var4.set(13, 0);
            var4.set(14, 0);
            var4.set(9, 0);
            Date var5 = var4.getTime();
            Date var6 = var4.getTime();
            SimpleDateFormat var7 = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
            String var8 = var7.format(var5);
            String var9 = var7.format(var6);
            HashMap var10 = new HashMap();
            var10.put("Task Estimated Start Date", var8);
            var10.put("Task Estimated Finish Date", var9);
            var10.put("Task Estimated Duration", "1 d");
            com.dassault_systemes.enovia.tsk.Task.modifyTask(var0, var1, var10);*/
            StringList var11 = new StringList();
            var11.add("id");
            var11.add("to[Project Access List].from.id");
            var11.add("to[Project Access List].id");
            Map var12 = DBUtil.getInfo(var0, var2, var11); // project 정보 조회
            String var13 = (String)var12.get("to[Project Access List].from.id");
            String var14 = (String)var12.get("id");
            String var15 = (String)var12.get("to[Project Access List].id");
            DBUtil.connect(var0, var13, "Project Access Key", var1, (Map)null, false);
            String[] var16 = new String[]{var1}; // gscEvent physical id 담음

            if (objectType.equals("Milestone")) {
                com.matrixone.apps.common.Task var17 = new com.matrixone.apps.common.Task(var14); // project object id 담음
                addExisting(var0, var17, var16, (String)null);
            } else if (objectType.equals("gscEvent")) {
                StringList select = new StringList();
                select.add("physicalid");
                select.add("name");
                select.add("current");

                Boolean connected = false;
                MapList phaseList = findEventRelated(var0, var14, "Phase", DomainConstants.RELATIONSHIP_SUBTASK, select, null);
                for (int i = 0; i < phaseList.size(); i++) {
                    Map list = (Map) phaseList.get(i);
                    if (list.get("current").equals("Active")) {
                        String phaseId = list.get("physicalid").toString();
                        Task var17 = new Task(phaseId);
                        addExisting(var0, var17, var16, (String)null); // phase에 gscEvent 연결
                        connected = true;
                    }
                }
                
                if (!connected) {
                    Task var17 = new Task(var14);
                    addExisting(var0, var17, var16, (String)null); // active한 phase가 존재하지 않으면 과제와 연결
                }
            }

            return var15;

        } catch (Exception var23) {
            throw new FoundationException(var23);
        }
    }

    public static Datacollection getUserRouteInboxTasks(Context var0, List<Selectable> var1) throws FoundationException {
        return getUserRouteInboxTasks(var0, var1, (String)null);
    }

    public static Datacollection getUserRouteInboxTasks(Context var0, List<Selectable> var1, String var2) throws FoundationException {
        try {
            Class var4 = Class.forName("com.dassault_systemes.enovia.route.Route");
            Class[] var5 = new Class[]{Context.class, List.class, String.class, String.class, Integer.TYPE};
            Method var6 = var4.getMethod("getUserRouteInboxTasks", var5);
            Datacollection var3 = (Datacollection)var6.invoke((Object)null, var0, var1, var2, "All", 0);
            InboxTask var7 = new InboxTask();
            MapList var8 = (MapList)var7.getTasksAssignedToContextUserGroup(var0);
            Datacollection var9 = new Datacollection();
            List var10 = var9.getDataobjects();
            Iterator var11 = var8.iterator();

            while(var11.hasNext()) {
                HashMap var12 = (HashMap)var11.next();
                Dataobject var13 = new Dataobject();
                var13.setId((String)var12.get("id"));
                var10.add(var13);
            }

            getTaskInfo(var0, var9, var1);
            var3.getDataobjects().addAll(var10);
            return var3;
        } catch (Throwable var14) {
            throw new FoundationException(var14.getMessage());
        }
    }

    public static Dataobject getTaskInfo(Context var0, String var1, List<Selectable> var2) throws FoundationException {
        Datacollection var3 = new Datacollection();
        Dataobject var4 = new Dataobject();
        var4.setId(var1);
        var3.getDataobjects().add(var4);
        getTaskInfo(var0, var3, var2);
        return var4;
    }

    public static void getTaskInfo(Context var0, Datacollection var1, List<Selectable> var2) throws FoundationException {
        checkLicenseCollaborativeTasks(var0);
        if (var2 != null && !var2.isEmpty()) {
            ObjectUtil.print(var0, var1, (PrintData)null, var2, true);
        } else {
            throw new FoundationException("You must provide some selectables to Task.getTaskInfo");
        }
    }

    public static void modifyTask(Context var0, String var1, Map<String, String> var2) throws FoundationException {
        modifyTask(var0, var1, var2, true, true);
    }

    public static void modifyTask(Context var0, String var1, Map<String, String> var2, boolean var3, boolean var4) throws FoundationException {
        modifyTask(var0, var1, var2, true, true, false);
    }

    protected static void modifyTask(Context var0, String var1, Map<String, String> var2, boolean var3, boolean var4, boolean var5) throws FoundationException {
        modifyTask(var0, var1, var2, true, true, false, (String)null);
    }

    protected static void modifyTask(Context var0, String var1, Map<String, String> var2, boolean var3, boolean var4, boolean var5, String var6) throws FoundationException {
        if (var6 != null && var6.contains("dpm.projecttemplates")) {
            var2.remove("Percent Complete");
        }

        if (var2 != null && !var2.isEmpty()) {
            checkLicenseCollaborativeTasks(var0);
            if (var2.containsKey("Percent Complete")) {
                String var7 = (String)var2.get("Percent Complete");
                double var8 = var7 != null ? Double.valueOf(var7) : -1.0;
                if (var8 < 0.0 || var8 > 100.0) {
                    throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.ModifyPercentCompleteError", var0.getLocale()));
                }

                ContextUtil.setGlobalRPEValue(var0, "PERCENTAGE_COMPLETE", "true");
            }

            if (var3 || var4) {
                ArrayList var20 = new ArrayList(5);
                var20.add(SELECTABLE_IS_SUMMARY_TASK);
                var20.add(SELECT_TASK_PAL_ID);
                if (var4) {
                    var20.add(ServiceConstants.SELECTABLE_NAME);
                    var20.add(ServiceConstants.SELECTABLE_CURRENT);
                    var20.add(ServiceConstants.SELECTABLE_TYPE);
                }

                boolean var21 = false;
                if (var3 && (var2.containsKey("Task Estimated Start Date") || var2.containsKey("Task Estimated Finish Date") || var2.containsKey("Task Estimated Duration") || var2.containsKey("Percent Complete"))) {
                    var20.add(SELECT_TASK_OBJECT_ID);
                    var21 = true;
                }

                Dataobject var9 = getTaskInfo(var0, (String)var1, var20);
                checkRestrictedSummaryFields(var0, var9, var2);
                String var10 = DataelementMapAdapter.getDataelementValue(var9, SELECT_TASK_PAL_ID.getName());
                String var11;
                String var26;
                if (var4) {
                    var11 = var0.getLocale().getLanguage();
                    String var12 = DataelementMapAdapter.getDataelementValue(var9, ServiceConstants.SELECT_CURRENT.getName());
                    if (var12.isEmpty()) {
                        throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.noModifyAccess", var0.getLocale()));
                    }

                    boolean var13 = "Review".equals(var12) && (var2.containsKey("Task Estimated Start Date") || var2.containsKey("Task Estimated Finish Date") || var2.containsKey("Task Estimated Duration")) && var10 != null && !var10.isEmpty();
                    boolean var14 = true;
                    if ("Complete".equals(var12)) {
                        boolean var15 = var2.containsKey("Task Actual Start Date");
                        boolean var16 = var2.containsKey("Task Actual Finish Date");
                        boolean var17 = var2.containsKey("Percent Complete");
                        int var18 = var2.size();
                        if (var18 == 1 && var15 || var18 == 2 && var17 && var16 || var18 == 3 && var15 && var16 && var17) {
                            var14 = false;
                        }
                    }

                    String var25;
                    if (var13 || "Complete".equals(var12) && var14) {
                        var25 = DataelementMapAdapter.getDataelementValue(var9, ServiceConstants.SELECTABLE_NAME.getName());
                        var26 = PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.ModifyCompletedTask", var0.getLocale());
                        String var28 = PropertyUtil.getStateI18NString(var0, "Project Task", "Active", var11);
                        var26 = var26.replace("%NLS_ACTIVE_STATE%", var28);
                        var26 = var26.replace("%TASK_NAME%", var25);
                        throw new FoundationUserException(var26);
                    }

                    if (!var2.containsKey("type")) {
                        if (!"Gate".equals(var9.getType()) && !"Milestone".equals(var9.getType())) {
                            ensureValidTaskDuration(var0, var2, var10);
                        } else {
                            checkRestrictedGateMilestobeFields(var0, var2);
                        }
                    } else if (((String)var2.get("type")).equals(var9.getType())) {
                        var2.remove("type");
                        if (!"Gate".equals(var9.getType()) && !"Milestone".equals(var9.getType())) {
                            ensureValidTaskDuration(var0, var2, var10);
                        } else {
                            checkRestrictedGateMilestobeFields(var0, var2);
                        }
                    } else {
                        if (!"Create".equals(var12) && !"Assign".equals(var12)) {
                            var25 = PropertyUtil.getStateI18NString(var0, "Project Task", "Assign", var11);
                            var26 = PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.typeChangeNotAllowedAtState", var0.getLocale());
                            var26 = var26.replace("%NLS_STATE%", var25);
                            throw new FoundationUserException(var26);
                        }

                        var25 = getDefaultPolicy(var0, (String)var2.get("type"));
                        if (!"Gate".equals(var2.get("type")) && !"Milestone".equals(var2.get("type"))) {
                            if ("Gate".equals(var9.getType()) || "Milestone".equals(var9.getType())) {
                                if (!var2.containsKey("Task Estimated Duration")) {
                                    var2.put("Task Estimated Duration", "1");
                                } else {
                                    ensureValidTaskDuration(var0, var2, var10);
                                }

                                if (var25 == null || var25.isEmpty()) {
                                    var25 = "Project Task";
                                }

                                var2.put("policy", var25);
                            }
                        } else {
                            var26 = DataelementMapAdapter.getDataelementValue(var9, SELECTABLE_IS_SUMMARY_TASK.getName());
                            if ("TRUE".equalsIgnoreCase(var26)) {
                                throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.InvalidSummaryTaskTypeChange", var0.getLocale()));
                            }

                            if (var25 == null || var25.isEmpty()) {
                                var25 = "Project Review";
                            }

                            var2.put("Task Estimated Duration", "0");
                            var2.put("policy", var25);
                        }
                    }
                }

                if (var21 && var10 != null && !var10.isEmpty()) {
                    var11 = DataelementMapAdapter.getDataelementValue(var9, SELECT_TASK_OBJECT_ID.getName());
                    HashMap var22 = new HashMap();
                    HashMap var23 = new HashMap();
                    var22.put(var11, var23);

                    try {
                        var26 = "";
                        if (var5) {
                            Date var24 = FormatUtil.parseMxDate(var0, (String)var2.remove("Task Estimated Start Date"), (SimpleDateFormat)null);
                            Date var27 = FormatUtil.parseMxDate(var0, (String)var2.remove("Task Estimated Finish Date"), (SimpleDateFormat)null);
                            var23.put("Task Estimated Start Date", var24);
                            var23.put("Task Estimated Finish Date", var27);
                            var23.put("Task Estimated Duration", var2.remove("Task Estimated Duration"));
                            var23.put("Percent Complete", var2.remove("Percent Complete"));

                            var26 = com.matrixone.apps.common.Task.updateDates(var0, var22, false, true);
                        } else {
                            //var26 = com.matrixone.apps.common.Task.updateDates(var0, var22, true, true);
                            String estimatedStartDate = var2.get("Task Estimated Start Date").toString();
                            String estimatedFinishDate = var2.get("Task Estimated Finish Date").toString();

                            com.matrixone.apps.domain.util.MqlUtil.mqlCommand(var0, String.format("mod bus %s 'Task Estimated Start Date' '%s'", var9.getId(), estimatedStartDate));
                            com.matrixone.apps.domain.util.MqlUtil.mqlCommand(var0, String.format("mod bus %s 'Task Estimated Finish Date' '%s'", var9.getId(), estimatedFinishDate));

                        }

                        /*if (!"FALSE".equalsIgnoreCase(var26)) {
                            throw new FoundationException("Error: unable to update project task estimated dates: " + var26);
                        }*/
                    } catch (Exception var19) {
                        throw new FoundationException(var19);
                    }
                }
            }

            ObjectEditUtil.modify(var0, var1, var2, false);
        }

    }

    private static void ensureValidTaskDuration(Context var0, Map<String, String> var1, String var2) throws FoundationException {
        if (var2 != null && !var2.isEmpty() && var1.containsKey("Task Estimated Duration")) {
            String var3 = (String)var1.get("Task Estimated Duration");
            List var4 = StringUtil.splitString(var3, " ");
            double var5 = Double.parseDouble((String)var4.get(0));
            if (var5 == 0.0) {
                throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.InvalidTaskDuration", var0.getLocale()));
            }
        }

    }

    private static void checkRestrictedGateMilestobeFields(Context var0, Map<String, String> var1) {
        var1.remove("Task Estimated Duration");
        String var2 = (String)var1.get("Percent Complete");
        double var3 = var2 != null ? Double.valueOf(var2) : -1.0;
        if (var3 != 0.0 && var3 != 100.0) {
            var1.remove("Percent Complete");
        }

    }

    private static void checkRestrictedSummaryFields(Context var0, Dataobject var1, Map<String, String> var2) {
        String var3 = DataelementMapAdapter.getDataelementValue(var1, SELECTABLE_IS_SUMMARY_TASK.getName());
        if ("TRUE".equalsIgnoreCase(var3)) {
            var2.remove("Task Actual Start Date");
            var2.remove("Task Actual Finish Date");
            var2.remove("Task Estimated Duration");
            var2.remove("Percent Complete");
        }

    }

    public static void deleteTask(Context var0, String var1) throws FoundationException {
        deleteTask(var0, var1, false);
    }

    public static void deleteTask(Context var0, String var1, boolean var2) throws FoundationException {
        deleteTask(var0, var1, var2, false);
    }

    protected static Dataobject deleteTask(Context var0, String var1, boolean var2, boolean var3) throws FoundationException {
        ArrayList var4 = new ArrayList(1);
        var4.add(SELECTABLE_CURRENT);
        var4.add(SELECTABLE_HAS_DELETE_ACCESS);
        var4.add(SELECTABLE_IS_PROJECT_TASK);
        var4.add(SELECT_TASK_PROJECT_ID);
        var4.add(SELECTABLE_TYPE);
        var4.add(SELECTABLE_NAME);
        Dataobject var5 = ObjectUtil.print(var0, var1, (PrintData)null, var4);
        String var6 = DataelementMapAdapter.getDataelementValue(var5, SELECT_CURRENT.getName());
        String var7 = DataelementMapAdapter.getDataelementValue(var5, SELECTABLE_IS_PROJECT_TASK.getName());
        if ("TRUE".equalsIgnoreCase(var7) && !"Create".equals(var6) && !"Assign".equals(var6)) {
            throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.CannotDeleteActiveTasks", var0.getLocale()));
        } else {
            String var8 = DataelementMapAdapter.getDataelementValue(var5, SELECTABLE_HAS_DELETE_ACCESS.getName());
            if ("FALSE".equalsIgnoreCase(var8)) {
                throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.noDeleteAccess", var0.getLocale()));
            } else {
                try {
                    ContextUtil.pushContext(var0);
                    if (var3) {
                        ArrayList var9 = new ArrayList();
                        var9.add(var1);
                        Class var10 = Class.forName("com.matrixone.apps.program.Task");
                        Class[] var11 = new Class[]{Context.class, List.class, Boolean.TYPE};
                        Method var12 = var10.getMethod("deleteTasks", var11);
                        var12.invoke((Object)null, var0, var9, false);
                    } else {
                        MqlUtil.mqlCommand(var0, "delete bus $1_id", new String[]{var1});
                    }
                } catch (Exception var16) {
                    var16.printStackTrace();
                    throw new FoundationException(var16);
                } finally {
                    ContextUtil.popContext(var0);
                }

                Dataobject var18 = null;
                if (var2 && "TRUE".equalsIgnoreCase(var7)) {
                    String var19 = DataelementMapAdapter.getDataelementValue(var5, SELECT_TASK_PROJECT_ID.getName());
                    var18 = new Dataobject();
                    var18.setId(var19);
                }

                return var18;
            }
        }
    }

    public static void performSequenceAndRollup(Context var0, Dataobject var1) throws FoundationException {
        ContextUtil.startTransaction(var0, true);

        try {
            ContextUtil.pushContext(var0);
            String var2 = var1.getId();
            com.matrixone.apps.common.Task var3 = new com.matrixone.apps.common.Task(var2);
            String var4 = var3.getInfo(var0, "id");
            ArrayList var5 = new ArrayList(0);
            modifyTaskSequenceOrders(var0, var2, var5);
            StringList var6 = new StringList();
            var6.add(var4);
            TaskDateRollup.validateTasks(var0, var6);
            ContextUtil.commitTransaction(var0);
        } catch (Exception var10) {
            ContextUtil.abortTransaction(var0);
            var10.printStackTrace();
            throw new FoundationException(var10);
        } finally {
            ContextUtil.popContext(var0);
        }

    }

    private static String createDecision(Context var0, String var1) throws FoundationException {
        MqlUtil.mqlCommand(var0, "set env $1 $2", new String[]{"decisionName", "Approve"});
        String var3 = "" + System.currentTimeMillis();
        String var4 = ObjectEditUtil.create(var0, "Decision", "Approve", var3, "Decision", (String)null, var1, true, true, (Map)null);
        ObjectEditUtil.connect(var0, var4, "Decision", var1, (Map)null, false);
        return var4;
    }

    public static void setTaskState(Context var0, String var1, String var2) throws FoundationException {
        setTaskState(var0, var1, var2, (String)null);
    }

    public static void setTaskState(Context var0, String var1, String var2, String var3) throws FoundationException {
        setTaskState(var0, var1, var2, (String)null, (String)null);
    }

    public static void setTaskState(Context var0, String var1, String var2, String var3, String var4) throws FoundationException {
        if (var4 == null || !var4.contains("dpm.projecttemplates")) {
            ArrayList var5;
            Dataobject var6;
            String var8;
            String var9;
            String var10;
            if ("Complete".equals(var2)) {
                var5 = new ArrayList(2);
                var5.add(SELECT_NEEDS_REVIEW);
                var5.add(SELECT_ROUTE_TASK_NEEDS_REVIEW);
                var5.add(SELECTABLE_POLICY);
                var5.add(SELECT_CURRENT);
                var6 = getTaskInfo(var0, (String)var1, var5);
                String var7 = DataelementMapAdapter.getDataelementValue(var6, SELECT_NEEDS_REVIEW.getName());
                var8 = DataelementMapAdapter.getDataelementValue(var6, SELECT_ROUTE_TASK_NEEDS_REVIEW.getName());
                var9 = DataelementMapAdapter.getDataelementValue(var6, SELECT_CURRENT.getName());
                if ("Complete".equals(var9)) {
                    return;
                }

                if (("YES".equalsIgnoreCase(var7) || "YES".equalsIgnoreCase(var8)) && !"Review".equals(var9)) {
                    throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.ReviewStateRequired", var0.getLocale()));
                }

                var10 = DataelementMapAdapter.getDataelementValue(var6, SELECTABLE_POLICY.getName());
                if ("Gate".equals(var3) && var10.equals("Project Review")) {
                    createDecision(var0, var1);
                }
            } else if (("Gate".equals(var3) || "Milestone".equals(var3)) && ("Assign".equals(var2) || "Active".equals(var2))) {
                var5 = new ArrayList(2);
                var5.add(SELECTABLE_NAME);
                var5.add(SELECTABLE_STATES);
                var6 = getTaskInfo(var0, (String)var1, var5);
                List var15 = DataelementMapAdapter.getDataelementValues(var6, SELECTABLE_STATES.getName());
                if (!var15.contains(var2)) {
                    var8 = var0.getLocale().getLanguage();
                    var9 = DataelementMapAdapter.getDataelementValue(var6, SELECTABLE_NAME.getName());
                    var10 = PropertyUtil.getStateI18NString(var0, "Project Task", var2, var8);
                    ArrayList var11 = new ArrayList();
                    Iterator var12 = var15.iterator();

                    while(var12.hasNext()) {
                        String var13 = (String)var12.next();
                        String var14 = PropertyUtil.getStateI18NString(var0, "Project Task", var13, var8);
                        var11.add(var14);
                    }

                    String var16 = PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.InvaldTargetState", var0.getLocale());
                    var16 = var16.replace("%NLS_STATE%", var10);
                    var16 = var16.replace("%NAME%", var9);
                    var16 = var16.replace("%NLS_STATES%", var11.toString());
                    throw new FoundationUserException(var16);
                }
            }

            ContextUtil.setGlobalRPEValue(var0, "State", var2);
            ObjectEditUtil.setState(var0, var1, var2);
        }
    }

    protected static Datacollection getPersonInfo(Context var0, String var1, List<Selectable> var2, int var3) throws FoundationException {
        QueryData var4 = new QueryData();
        var4.setTypePattern("Person");
        var4.setNamePattern(var1);
        var4.setLimit(BigInteger.valueOf((long)var3));
        Datacollection var5 = ObjectUtil.query(var0, var4, var2);
        return var5;
    }

    protected static void checkLicenseCollaborativeTasks(Context var0) throws FoundationException {
        checkLicenseCollaborativeTasks(var0, (HttpServletRequest)null);
    }

    protected static void checkLicenseCollaborativeTasks(Context var0, HttpServletRequest var1) throws FoundationException {
        LicenseUtil.checkLicenseReserved(var0, "ENOTASK_TP", var1);
    }

    protected static void checkLicenseProjectTasks(Context var0, HttpServletRequest var1) throws FoundationException {
        LicenseUtil.checkLicenseReserved(var0, "ENO_PGE_TP", var1);
    }

    protected static Range getStates(Context var0, String var1) throws FoundationException {
        Range var2 = new Range();
        String var3 = var0.getLocale().getLanguage();
        String var5 = MqlUtil.mqlCommand(var0, "print policy $1 select $2 dump $3", new String[]{var1, "state", ","});
        List var6 = StringUtil.splitString(var5, ",");
        Iterator var7 = var6.iterator();

        while(var7.hasNext()) {
            String var8 = (String)var7.next();
            String var9 = PropertyUtil.getStateI18NString(var0, var1, var8, var3);
            Range.Item var10 = new Range.Item();
            var10.setValue(var8);
            var10.setDisplay(var9);
            var2.getItem().add(var10);
        }

        return var2;
    }

    public static void setTaskAssignNotificationPreference(Context var0, boolean var1) throws FoundationException {
        ContextUtil.setPersonProperty(var0, "preference_AssignedTaskNotificationOn", var1 + "");
    }

    public static void setSubscribed(Context var0, String var1, boolean var2) throws FoundationException {
        SubscriptionUtil.setSubscribed(var0, var1, var2, "TSKTaskTransaction");
    }

    public static void getSubscribed(Context var0, List<Dataobject> var1, String var2) throws FoundationException {
        try {
            List var3 = SubscriptionUtil.getSubscribedTaskIds(var0);
            Iterator var4 = var1.iterator();

            while(var4.hasNext()) {
                Dataobject var5 = (Dataobject)var4.next();
                if (var3.contains(var5.getId())) {
                    DataelementMapAdapter.setDataelementValue(var5, var2, "TRUE");
                } else {
                    DataelementMapAdapter.setDataelementValue(var5, var2, "FALSE");
                }
            }

        } catch (Exception var6) {
            throw new FoundationException(var6);
        }
    }

    protected static String getPALId(Context var0, String var1) throws FoundationException {
        String var2 = MqlUtil.mqlCommand(var0, "print bus $1 select $2 dump", new String[]{var1, "to[Project Access List,Project Access Key].from.id"});
        return var2;
    }

    protected static String addSubtask(Context var0, String var1, String var2, String var3, String var4, Map<String, String> var5) throws FoundationException {
        if (!"Gate".equals(var3) && !"Milestone".equals(var3)) {
            String var6 = ObjectEditUtil.connect(var0, var2, "Subtask", var4, var5, false);
            if (var1 != null) {
                ObjectEditUtil.connect(var0, var1, "Project Access Key", var4, (Map)null, false);
            }

            return var6;
        } else {
            throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.InvalidSummaryTaskTypeDefinition", var0.getLocale()));
        }
    }

    protected static void removeSubtask(Context var0, String var1, String var2) throws FoundationException {
        removeSubtask(var0, var1, var2, false);
    }

    protected static void removeSubtask(Context var0, String var1, String var2, boolean var3) throws FoundationException {
        ArrayList var4 = new ArrayList(3);
        var4.add(SELECT_TASK_PAL_ID);
        var4.add(SELECTABLE_ISTASK);
        if (var3) {
            var4.add(SELECTABLE_ATTRIBUTE_TASKWEIGHTAGE_FROM_TASK);
        }

        Dataobject var5 = ObjectUtil.print(var0, var2, (PrintData)null, var4);
        String var6 = DataelementMapAdapter.getDataelementValue(var5, SELECTABLE_ISTASK.getName());
        String var7 = DataelementMapAdapter.getDataelementValue(var5, SELECT_TASK_PAL_ID.getName());
        String var8 = "";
        if (var3) {
            if ("TRUE".equalsIgnoreCase(var6)) {
                var8 = (String)DataelementMapAdapter.getDataelementValues(var5, SELECTABLE_ATTRIBUTE_TASKWEIGHTAGE_FROM_TASK.getName()).get(0);
            } else {
                var8 = MqlUtil.mqlCommand(var0, "print connection bus $1 $2 $3 relationship $4 select $5 dump $6", new String[]{var1, "to", var2, "Subtask", "attribute[TaskWeightage]", "|"});
            }
        }

        if ("TRUE".equalsIgnoreCase(var6) && !var3) {
            ObjectEditUtil.disconnect(var0, var7, "Project Access Key", var2);
        }

        if (var3 && var8 != null && !var8.isEmpty()) {
            _relAttributesData.put(var2, var8);
        }

        try {
            ObjectEditUtil.disconnect(var0, var1, "Subtask", var2);
        } catch (Exception var10) {
            var10.printStackTrace();
            throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.TaskDisconnectError", var0.getLocale()));
        }
    }

    protected static String modifySubtask(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        String var4 = modifySubtask(var0, var1, var2, var3, false);
        return var4;
    }

    protected static String modifySubtask(Context var0, String var1, String var2, Map<String, String> var3, boolean var4) throws FoundationException {
        ArrayList var5 = new ArrayList(3);
        var5.add(SELECT_TASK_PAL_ID);
        var5.add(SELECTABLE_TYPE);
        Dataobject var6 = ObjectUtil.print(var0, var1, (PrintData)null, var5);
        if (!"Gate".equals(var6.getType()) && !"Milestone".equals(var6.getType())) {
            String var7 = DataelementMapAdapter.getDataelementValue(var6, SELECT_TASK_PAL_ID.getName());
            var5.remove(SELECTABLE_TYPE);
            var5.add(SELECTABLE_ISTASK);
            var5.add(SELECT_TASK_CONNECTION_IDS);
            Dataobject var8 = ObjectUtil.print(var0, var2, (PrintData)null, var5);
            String var9 = DataelementMapAdapter.getDataelementValue(var8, SELECTABLE_ISTASK.getName());
            String var10;
            if ("TRUE".equalsIgnoreCase(var9) && !var4) {
                String var15 = DataelementMapAdapter.getDataelementValue(var8, SELECT_TASK_PAL_ID.getName());
                if (!var15.equals(var7)) {
                    throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.ReparentingIssue", var0.getLocale()));
                }

                var10 = (String)DataelementMapAdapter.getDataelementValues(var8, SELECT_TASK_CONNECTION_IDS.getName()).get(0);
                modifyFromConnection(var0, var10, var1, var3);
            } else {
                var5.clear();
                var5.add(SELECT_TASK_CONNECTION_PAL_IDS);
                ObjectUtil.print(var0, var8, (PrintData)null, var5, true);
                List var11 = DataelementMapAdapter.getDataelementValues(var8, SELECT_TASK_CONNECTION_IDS.getName());
                List var12 = DataelementMapAdapter.getDataelementValues(var8, SELECT_TASK_CONNECTION_PAL_IDS.getName());
                int var13 = var12.indexOf(var7);
                if (var13 != -1) {
                    var10 = (String)var11.get(var13);
                    modifyFromConnection(var0, var10, var1, var3);
                } else {
                    var10 = addSubtask(var0, (String)null, var1, var6.getType(), var2, var3);
                    if (var4 && _relAttributesData.containsKey(var2)) {
                        MqlUtil.mqlCommand(var0, "modify connection $1 add $2 $3", new String[]{var10, "interface", "DPMTaskWeightage"});
                        HashMap var14 = new HashMap();
                        var14.put("TaskWeightage", (String)_relAttributesData.get(var2));
                        ObjectEditUtil.modifyConnection(var0, var10, var14, false);
                    }
                }
            }

            return var10;
        } else {
            throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.InvalidSummaryTaskTypeDefinition", var0.getLocale()));
        }
    }

    private static void modifyFromConnection(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        HashMap var4 = var3 != null ? new HashMap(var3) : new HashMap();
        var4.put("from", var2);
        ObjectEditUtil.modifyConnection(var0, var1, var4, false);
    }

    protected static String addPredecessor(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        boolean var4 = false;
        String var5 = ObjectEditUtil.connect(var0, var1, "Dependency", var2, var3, false, var4, false);
        return var5;
    }

    protected static void deletePredecessor(Context var0, String var1, String var2) throws FoundationException {
        boolean var3 = !ServiceSave.isObjectNew(var0, var1);
        ObjectEditUtil.disconnect(var0, var1, "Dependency", var2, var3, false);
    }

    protected static String modifyPredecessor(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        String var4 = ObjectEditUtil.modifyConnection(var0, var1, "Dependency", var2, var3, false, SELECTABLE_REL_OBJECTID.getExpression());
        return var4;
    }

    protected static void modifyPredecessorId(Context var0, String var1, Map<String, String> var2) throws FoundationException {
        ObjectEditUtil.modifyConnection(var0, var1, var2, false, (String)null);
    }

    protected static void modifyTaskSequenceOrders(Context var0, String var1, List<Dataobject> var2) throws FoundationException {
        List var3 = getTaskTypes(var0);
        ArrayList var4 = new ArrayList(6);
        var4.add(SELECTABLE_ID);
        var4.add(SELECTABLE_TYPE);
        var4.add(SELECTABLE_OBJECTID_ELEMENT);
        var4.add(SELECT_TASK_PROJECT_ID);
        Dataobject var5 = ObjectUtil.print(var0, var1, (PrintData)null, var4);
        var4.remove(SELECT_TASK_PROJECT_ID);
        if (var3.indexOf(var5.getType()) != -1) {
            String var6 = DataelementMapAdapter.getDataelementValue(var5, SELECT_TASK_PROJECT_ID.getName());
            var5 = ObjectUtil.print(var0, var6, (PrintData)null, var4);
        }

        var4.add(SELECTABLE_REL_OBJECTID);
        var4.add(SELECTABLE_SEQUENCE_ORDER);
        var4.add(SELECTABLE_TASK_WBS);
        Datacollection var33 = new Datacollection();
        var33.getDataobjects().add(var5);
        ExpandData var7 = new ExpandData();
        var7.setRelationshipPattern("Subtask");
        var7.setGetFrom(true);
        var7.setRecurseToLevel(BigDecimal.ZERO);
        ObjectUtil.expandAsChildren(var0, var33, var7, var4);
        String var8 = "sequenceOrder";
        String var9 = "previousTaskId";
        String var10 = "nextTaskId";
        Sort var11 = Sortdata.newSortItem(SELECTABLE_SEQUENCE_ORDER.getName(), SortType.INTEGER, SortDirection.ASCENDING, ExpressionType.REL);
        ArrayList var12 = new ArrayList(1);
        var12.add(var11);
        Sortdata.sortDataobjects(var33.getDataobjects(), var12);
        HashMap var13 = new HashMap();
        indexParents(var5, var13);
        HashMap var14 = new HashMap();
        Iterator var15 = var2.iterator();

        label112:
        while(var15.hasNext()) {
            Dataobject var16 = (Dataobject)var15.next();
            String var17 = var16.getParent().getId();
            String var18 = var16.getId();
            String var19 = RelelementMapAdapter.getRelelementValue(var16, var8);
            String var20 = RelelementMapAdapter.getRelelementValue(var16, var9);
            String var21 = RelelementMapAdapter.getRelelementValue(var16, var10);
            String var22 = var20 != null ? var20 : var21;
            boolean var23 = var20 != null;
            Dataobject var24 = (Dataobject)var13.get(var17);
            List var25 = var24.getChildren();
            if ("".equals(var22)) {
                var19 = "0";
                var22 = null;
            } else if (var22 == null && var19 == null) {
                var22 = (String)var14.get(var17);
            }

            if (var19 != null && Integer.valueOf(var19) < var25.size()) {
                if (Integer.valueOf(var19) <= 1) {
                    var19 = "0";
                } else {
                    var19 = String.valueOf(Integer.valueOf(var19) - 1);
                }
            } else {
                var19 = String.valueOf(var25.size() - 1);
            }

            var14.put(var17, var18);
            int var26 = -2;
            int var27 = -2;
            int var28 = -1;
            Iterator var29 = var25.iterator();

            while(true) {
                String var31;
                String var32;
                do {
                    do {
                        do {
                            Dataobject var30;
                            if (!var29.hasNext()) {
                                if (var23 && var27 != -2) {
                                    ++var27;
                                }

                                int var38 = var27 != -2 ? var27 : Integer.valueOf(var19);
                                if (var38 != var26 && var26 != -2) {
                                    if (var26 < var38) {
                                        --var38;
                                    }

                                    var30 = (Dataobject)var25.remove(var26);
                                    var25.add(var38, var30);
                                }
                                continue label112;
                            }

                            var30 = (Dataobject)var29.next();
                            ++var28;
                            var31 = var30.getId();
                            var32 = DataelementMapAdapter.getDataelementValue(var30, SELECTABLE_OBJECTID_ELEMENT.getName());
                            if (var26 == -2 && (var31.equals(var18) || var32.equals(var18))) {
                                var26 = var28;
                                var16.setRelId(var30.getRelId());
                            }
                        } while(var22 == null);
                    } while(var27 != -2);
                } while(!var31.equals(var22) && !var32.equals(var22));

                var27 = var28;
            }
        }

        ArrayList var34 = new ArrayList();
        assignSequence(var5.getChildren(), 1, "", var34, var3);
        HashMap var35 = new HashMap(1);
        Iterator var36 = var34.iterator();

        while(var36.hasNext()) {
            Dataobject var37 = (Dataobject)var36.next();
            var35.clear();
            var35.put("Sequence Order", RelelementMapAdapter.getRelelementValue(var37, SELECTABLE_SEQUENCE_ORDER.getName()));
            var35.put("Task WBS", RelelementMapAdapter.getRelelementValue(var37, SELECTABLE_TASK_WBS.getName()));
            ObjectEditUtil.modifyConnection(var0, var37.getRelId(), var35, false);
        }

    }

    private static int assignSequence(List<Dataobject> var0, int var1, String var2, List<Dataobject> var3, List<String> var4) {
        int var5 = 0;
        Iterator var6 = var0.iterator();

        while(var6.hasNext()) {
            Dataobject var7 = (Dataobject)var6.next();
            String var8 = RelelementMapAdapter.getRelelementValue(var7, SELECTABLE_SEQUENCE_ORDER.getName());
            String var9 = RelelementMapAdapter.getRelelementValue(var7, SELECTABLE_TASK_WBS.getName());
            StringBuilder var10000 = (new StringBuilder()).append(var2);
            ++var5;
            String var10 = var10000.append(var5).toString();
            if (var1 != Integer.valueOf(var8) || !var10.equals(var9)) {
                RelelementMapAdapter.setRelelementValue(var7, SELECTABLE_SEQUENCE_ORDER.getName(), String.valueOf(var1));
                RelelementMapAdapter.setRelelementValue(var7, SELECTABLE_TASK_WBS.getName(), var10);
                var3.add(var7);
            }

            ++var1;
            String var11 = var10 + ".";
            if (var4.indexOf(var7.getType()) == -1) {
                var11 = "";
                assignSequence(var7.getChildren(), 1, var11, var3, var4);
            } else {
                var1 = assignSequence(var7.getChildren(), var1, var11, var3, var4);
            }
        }

        return var1;
    }

    private static void indexParents(Dataobject var0, Map<String, Dataobject> var1) {
        if (!var0.getChildren().isEmpty()) {
            String var2 = DataelementMapAdapter.getDataelementValue(var0, SELECTABLE_OBJECTID_ELEMENT.getName());
            var1.put(var0.getId(), var0);
            var1.put(var2, var0);
            Iterator var3 = var0.getChildren().iterator();

            while(var3.hasNext()) {
                Dataobject var4 = (Dataobject)var3.next();
                indexParents(var4, var1);
            }
        }

    }

    public static Datacollections getCalendar(Context var0, Datacollection var1, List<Selectable> var2) throws FoundationException {
        ArrayList var3 = new ArrayList(var2.size() + 1);
        var3.addAll(var2);
        var3.add(SELECTABLE_REL_TYPE);
        ExpandData var4 = new ExpandData();
        var4.setRelationshipPattern("Calendar");
        var4.setGetFrom(true);
        Datacollections var5 = ObjectUtil.expand(var0, var1, var4, var3);
        List var6 = getTaskTypes(var0);
        int var7 = 0;
        Iterator var8 = var5.getDatacollections().iterator();

        while(var8.hasNext()) {
            Datacollection var9 = (Datacollection)var8.next();
            Dataobject var10 = (Dataobject)var1.getDataobjects().get(var7++);
            String var11 = var10.getType();
            String var12 = "Calendar";
            if (var11 != null && !var6.contains(var11)) {
                var12 = "Default Calendar";
            }

            for(int var13 = 0; var13 < var9.getDataobjects().size(); ++var13) {
                Dataobject var14 = (Dataobject)var9.getDataobjects().get(var13);
                String var15 = (String)RelelementMapAdapter.removeRelelement(var14, SELECTABLE_REL_TYPE.getName());
                if (!var12.equals(var15)) {
                    var9.getDataobjects().remove(var13--);
                }
            }
        }

        return var5;
    }

    public static Dataobject getCalendar(Context var0, String var1) throws FoundationException {
        Dataobject var2 = new Dataobject();
        var2.setId(var1);
        ExpandData var3 = new ExpandData();
        var3.setRelationshipPattern("Calendar");
        var3.setGetFrom(true);
        var3.setRelationshipWhere("(type==Calendar AND from.type.kindof[Task Management] == TRUE) OR (type=='Default Calendar' AND from.type.kindof[Task Management] == FALSE)");
        ArrayList var4 = new ArrayList();
        var4.add(SELECTABLE_ID);
        var4.add(SELECTABLE_REL_OBJECTID);
        var4.add(SELECTABLE_OBJECTID_ELEMENT);
        var4.add(SELECTABLE_REL_TYPE);
        Datacollection var5 = ObjectUtil.expand(var0, var2, var3, var4);
        Dataobject var6 = var5.getDataobjects().isEmpty() ? null : (Dataobject)var5.getDataobjects().get(0);
        return var6;
    }

    public static void removeCalendar(Context var0, String var1, String var2) throws FoundationException {
        removeCalendar(var0, var1, var2, (String)null);
    }

    public static void removeCalendar(Context var0, String var1, String var2, String var3) throws FoundationException {
        Dataobject var4;
        if (null != var3 && var2 != null) {
            var4 = new Dataobject();
            var4.setId(var1);
            ArrayList var16 = new ArrayList();
            var16.add(SELECTABLE_ID);
            var16.add(SELECTABLE_OBJECTID_ELEMENT);
            var16.add(SELECTABLE_REL_TYPE);
            ExpandData var17 = new ExpandData();
            var17.setRelationshipPattern("Calendar");
            var17.setGetFrom(true);
            Dataobject var7 = new Dataobject();
            Datacollection var8 = ObjectUtil.expand(var0, var4, var17, var16);
            List var9 = var8.getDataobjects();
            Iterator var10 = var9.iterator();
            boolean var11 = false;

            while(var10.hasNext()) {
                var7 = (Dataobject)var10.next();
                DataelementMap var12 = var7.getDataelements();
                var11 = var12.containsValue(var2);
                if (var11) {
                    var10 = var9.iterator();
                    break;
                }
            }

            if (!var11) {
                throw new FoundationUserException("Error: the passed calendar id is not connected to task/project.");
            }

            if (var7 != null) {
                String var18 = var7.getId();
                String var13 = DataelementMapAdapter.getDataelementValue(var7, SELECTABLE_OBJECTID_ELEMENT.getName());
                if (var2.equals(var18) || var2.equals(var13)) {
                    String var14 = RelelementMapAdapter.getRelelementValue(var7, SELECTABLE_REL_TYPE.getName());
                    if ("Default Calendar".equals(var14)) {
                        HashMap var15 = new HashMap();
                        var15.put("type", "Calendar");
                        ObjectEditUtil.modifyConnection(var0, var7.getRelId(), var15, false);
                    } else {
                        ObjectEditUtil.disconnect(var0, var7.getRelId());
                    }
                }
            }
        } else {
            var4 = getCalendar(var0, var1);
            if (var4 != null) {
                String var5;
                if (var2 != null) {
                    var5 = DataelementMapAdapter.getDataelementValue(var4, SELECTABLE_OBJECTID_ELEMENT.getName());
                    if (!var2.equals(var4.getId()) && !var2.equals(var5)) {
                        throw new FoundationUserException("Error: the passed calendar id is not connected to task/project.");
                    }
                }

                var5 = RelelementMapAdapter.getRelelementValue(var4, SELECTABLE_REL_TYPE.getName());
                if ("Default Calendar".equals(var5)) {
                    HashMap var6 = new HashMap();
                    var6.put("type", "Calendar");
                    ObjectEditUtil.modifyConnection(var0, var4.getRelId(), var6, false);
                } else {
                    ObjectEditUtil.disconnect(var0, var4.getRelId());
                }
            }
        }

    }

    public static void setCalendar(Context var0, String var1, String var2) throws FoundationException {
        setCalendar(var0, var1, var2, (String)null);
    }

    public static void setCalendar(Context var0, String var1, String var2, String var3) throws FoundationException {
        ArrayList var4 = new ArrayList();
        var4.add(SELECTABLE_OBJECTID);
        var4.add(SELECTABLE_ISTASK);
        Dataobject var5 = ObjectUtil.print(var0, var1, (PrintData)null, var4);
        String var6 = DataelementMapAdapter.getDataelementValue(var5, SELECTABLE_ISTASK.getName());
        boolean var7 = !"FALSE".equalsIgnoreCase(var6);
        var4.clear();
        var4.add(SELECTABLE_ID);
        var4.add(SELECTABLE_REL_OBJECTID);
        var4.add(SELECTABLE_OBJECTID_ELEMENT);
        var4.add(SELECTABLE_REL_TYPE);
        ExpandData var8 = new ExpandData();
        var8.setRelationshipPattern("Calendar");
        var8.setGetFrom(true);
        Datacollection var9 = ObjectUtil.expand(var0, var5, var8, var4);
        if (var9.getDataobjects().isEmpty()) {
            if ("false".equalsIgnoreCase(var3)) {
                connectDefaultCalendar(var0, var1, var2, true);
            } else {
                connectDefaultCalendar(var0, var1, var2, var7);
            }
        } else {
            HashMap var10 = new HashMap();
            String var12;
            if (var7) {
                Dataobject var11 = (Dataobject)var9.getDataobjects().get(0);
                var12 = DataelementMapAdapter.getDataelementValue(var11, SELECTABLE_OBJECTID_ELEMENT.getName());
                if (!var2.equals(var11.getId()) && !var2.equals(var12)) {
                    var10.put("to", var2);
                    ObjectEditUtil.modifyConnection(var0, var11.getRelId(), var10, false);
                }
            } else {
                String var17 = null;
                var12 = null;
                Iterator var13 = var9.getDataobjects().iterator();

                while(true) {
                    Dataobject var14;
                    String var16;
                    do {
                        if (!var13.hasNext()) {
                            if (var17 != null) {
                                var10.put("type", "Calendar");
                                ObjectEditUtil.modifyConnection(var0, var17, var10, false);
                            }

                            if (var12 != null && (var3 == null || "true".equalsIgnoreCase(var3))) {
                                var10.put("type", "Default Calendar");
                                ObjectEditUtil.modifyConnection(var0, var12, var10, false);
                                return;
                            } else if (!"false".equalsIgnoreCase(var3) || var10.isEmpty() && var12 == null) {
                                if ("false".equalsIgnoreCase(var3)) {
                                    connectDefaultCalendar(var0, var1, var2, true);
                                } else {
                                    connectDefaultCalendar(var0, var1, var2, var7);
                                }

                                return;
                            } else {
                                ObjectEditUtil.modifyConnection(var0, var12, var10, false);
                                return;
                            }
                        }

                        var14 = (Dataobject)var13.next();
                        String var15 = RelelementMapAdapter.getRelelementValue(var14, SELECTABLE_REL_TYPE.getName());
                        var16 = DataelementMapAdapter.getDataelementValue(var14, SELECTABLE_OBJECTID_ELEMENT.getName());
                        if ("Default Calendar".equals(var15) && (var2.equals(var14.getId()) || var2.equals(var16) || "true".equalsIgnoreCase(var3) || var3 == null)) {
                            var17 = var14.getRelId();
                        }
                    } while(!var2.equals(var14.getId()) && !var2.equals(var16));

                    var12 = var14.getRelId();
                }
            }
        }

    }

    private static String connectDefaultCalendar(Context var0, String var1, String var2, boolean var3) throws FoundationException {
        String var4 = var3 ? "Calendar" : "Default Calendar";
        String var5 = ObjectEditUtil.connect(var0, var1, var4, var2, (Map)null, false);
        return var5;
    }

    protected static List<String> getTaskTypes(Context var0) throws FoundationException {
        List var1 = ObjectUtil.getTypeDerivatives(var0, "Task Management", true);
        return var1;
    }

    public static void setRouteTaskAssignee(Context var0, String var1, String var2) throws FoundationException {
        try {
            InboxTask var3 = new InboxTask(var1);
            if (var3.checkIfTaskIsAssignedToUserGroup(var0)) {
                var3.acceptTask(var0);
            } else {
                var3.delegateTask(var0, var2, true);
            }

        } catch (Exception var4) {
            throw new FoundationException(var4);
        }
    }

    public static String getDefaultConstraintType(Context var0, String var1) throws FoundationException {
        ArrayList var2 = new ArrayList(1);
        var2.add(SELECTABLE_DEFAULT_CONSTRAINT_TYPE);
        Dataobject var3 = ObjectUtil.print(var0, var1, (PrintData)null, var2);
        String var4 = DataelementMapAdapter.getDataelementValue(var3, SELECTABLE_DEFAULT_CONSTRAINT_TYPE.getName());
        return var4;
    }

    public static Dataobject addCoOwner(Context var0, String var1, String var2, String var3) throws FoundationException {
        String var4 = var3;
        ArrayList var5 = new ArrayList(5);
        Dataobject var6 = null;
        if (var3 != null) {
            var5.add(SELECTABLE_ID);
            List var7 = getPersonInfo(var0, var3, var5, 0).getDataobjects();
            var6 = var7.size() == 1 ? (Dataobject)var7.get(0) : null;
        } else if (var2 != null) {
            var5.add(SELECTABLE_ID);
            var5.add(SELECTABLE_NAME);
            var6 = ObjectUtil.print(var0, var2, (PrintData)null, var5);
            var4 = DataelementMapAdapter.getDataelementValue(var6, SELECTABLE_NAME.getName());
        }

        if (var6 == null) {
            throw new FoundationUserException("Invalid person id/name specified.");
        } else {
            Dataobject var8 = ObjectEditUtil.addCoOwnership(var0, var1, var4, "Project Lead");
            var8.setId(var6.getId());
            return var8;
        }
    }

    public static void removeCoOwner(Context var0, String var1, String var2, String var3) throws FoundationException {
        String var4 = var3;
        if (var3 == null) {
            ArrayList var5 = new ArrayList(1);
            var5.add(SELECTABLE_NAME);
            Dataobject var6 = ObjectUtil.print(var0, var2, (PrintData)null, var5);
            var4 = DataelementMapAdapter.getDataelementValue(var6, SELECTABLE_NAME.getName());
        }

        ObjectEditUtil.removeCoOwnership(var0, var1, var4);
    }

    public static Datacollections getCoOwners(Context var0, Datacollection var1, List<Selectable> var2) throws FoundationException {
        Datacollections var3 = ObjectUtil.getCoOwnersSOVs(var0, var1);
        fillInPersonIds(var0, var3, var2);
        return var3;
    }

    private static void fillInPersonIds(Context var0, Datacollections var1, List<Selectable> var2) throws FoundationException {
        ArrayList var3 = new ArrayList();
        Iterator var4 = var1.getDatacollections().iterator();

        while(var4.hasNext()) {
            Datacollection var5 = (Datacollection)var4.next();
            Iterator var6 = var5.getDataobjects().iterator();

            while(var6.hasNext()) {
                Dataobject var7 = (Dataobject)var6.next();
                String var8 = DataelementMapAdapter.getDataelementValue(var7, ObjectUtil.SELECTABLE_OWNERSHIP_PERSON.getName());
                if (!var3.contains(var8)) {
                    var3.add(var8);
                }
            }
        }

        if (!var3.isEmpty()) {
            ArrayList var15 = new ArrayList(var2);
            Select var16 = new Select("name.transient", "name", ExpressionType.BUS, (Format)null, false);
            var15.add(var16);
            String var17 = StringUtil.join(var3, ",");
            List var18 = getPersonInfo(var0, var17, var15, 0).getDataobjects();
            HashMap var19 = new HashMap(var18.size());

            for(int var9 = 0; var9 < var18.size(); ++var9) {
                Dataobject var10 = (Dataobject)var18.get(var9);
                String var11 = (String)DataelementMapAdapter.removeDataelement(var10, var16.getName());
                var19.put(var11, var10);
            }

            Iterator var20 = var1.getDatacollections().iterator();

            while(var20.hasNext()) {
                Datacollection var21 = (Datacollection)var20.next();

                for(int var22 = 0; var22 < var21.getDataobjects().size(); ++var22) {
                    Dataobject var12 = (Dataobject)var21.getDataobjects().get(var22);
                    String var13 = DataelementMapAdapter.getDataelementValue(var12, ObjectUtil.SELECTABLE_OWNERSHIP_PERSON.getName());
                    Dataobject var14 = (Dataobject)var19.get(var13);
                    if (var14 == null) {
                        var21.getDataobjects().remove(var22--);
                    } else {
                        var12.setId(var14.getId());
                        var12.setType(var14.getType());
                        var12.setDataelements(var14.getDataelements());
                    }
                }
            }

        }
    }

    public static MapList findEventRelated(Context context, String event, String findingType, String findingRelType, StringList findingObjSelect, StringList findingRelSelect) throws Exception {
        DomainObject eventObj = new DomainObject(event);
        MapList relatedList = eventObj.getRelatedObjects(context,
                findingRelType,  //String relPattern
                findingType, //String typePattern
                findingObjSelect,            //StringList objectSelects,
                findingRelSelect,                     //StringList relationshipSelects,
                true,                     //boolean getTo,
                true,                     //boolean getFrom,
                (short) 1,                 //short recurseToLevel,
                null,                     //String objectWhere,
                "",                       //String relationshipWhere,
                null,                     //Pattern includeType,
                null,                     //Pattern includeRelationship,
                null);                    //Map includeMap

        return relatedList;
    }

    private static void addExisting(Context var1, Task task, String[] var2, String var3) throws FrameworkException {
        com.matrixone.apps.domain.util.ContextUtil.startTransaction(var1, true);

        try {
            new HashMap(2);
            DomainObject var5 = new DomainObject();
            String var6 = "";
            StringList var7 = new StringList(2);
            var7.add(ProjectManagement.SELECT_PROJECT_SCHEDULE);
            var7.add(ProjectManagement.SELECT_PROJECT_SCHEDULE_ATTRIBUTE_FROM_TASK);
            var7.addElement(SELECT_PAL_PHYSICALID_FROM_PROJECT);
            var7.addElement(SELECT_PAL_PHYSICALID_FROM_TASK);
            Map var8 = task.getInfo(var1, var7);
            String var9 = (String)var8.get(ProjectManagement.SELECT_PROJECT_SCHEDULE); // project 정보 가짐
            if (var9 == null || var9.isEmpty()) {
                var9 = (String)var8.get(ProjectManagement.SELECT_PROJECT_SCHEDULE_ATTRIBUTE_FROM_TASK);
                if (var9 == null || var9.isEmpty()) {
                    var9 = "Auto";
                }
            }

            String var10 = (String)var8.get(SELECT_PAL_PHYSICALID_FROM_PROJECT);
            if (UIUtil.isNullOrEmpty(var10)) {
                var10 = (String)var8.get(SELECT_PAL_PHYSICALID_FROM_TASK);
            }

            if (var3 != null) {
                var6 = Task.getPhysicalId(var1, var3);
            }

            String var11 = Task.getPhysicalId(var1, task.getObjectId()); // gscEvent physical id
            var7.clear();
            var7.addElement(ProjectManagement.SELECT_IS_PROJECT_SPACE);
            var7.addElement("physicalid");
            var7.addElement(ProjectManagement.SELECT_IS_PROJECT_CONCEPT);
            var7.addElement(ProgramCentralConstants.SELECT_IS_PHASE);
            BusinessObjectWithSelectList var12 = BusinessObject.getSelectBusinessObjectData(var1, var2, var7);
            int var13 = 0;

            for(int var14 = var12.size(); var13 < var14; ++var13) {
                BusinessObjectWithSelect var15 = (BusinessObjectWithSelect)var12.getElement(var13);
                String var16 = var15.getSelectData("physicalid");
                var5.setId(var16);
                DomainRelationship.connect(var1, task, RELATIONSHIP_SUBTASK, var5);
                boolean var17 = "TRUE".equalsIgnoreCase(var15.getSelectData(ProjectManagement.SELECT_IS_PROJECT_SPACE)) || "TRUE".equalsIgnoreCase(var15.getSelectData(ProjectManagement.SELECT_IS_PROJECT_CONCEPT));
                Task.assignSequence(var1, var10, var11, var16, (String)null, var6, var17);
            }

            if ("Auto".equalsIgnoreCase(var9)) {
                com.matrixone.apps.domain.util.PropertyUtil.setGlobalRPEValue(var1, "PERCENTAGE_COMPLETE", "true");
                //task.rollupAndSave(var1);
            }

            com.matrixone.apps.domain.util.ContextUtil.commitTransaction(var1);
        } catch (Exception var21) {
            com.matrixone.apps.domain.util.ContextUtil.abortTransaction(var1);
            var21.printStackTrace();
            throw new FrameworkException(var21);
        } finally {
            com.matrixone.apps.domain.util.PropertyUtil.setGlobalRPEValue(var1, "PERCENTAGE_COMPLETE", "false");
        }

    }

    static {
        SELECTABLE_SEQUENCE_ORDER = new Select((String)null, DomainObject.getAttributeSelect("Sequence Order"), ExpressionType.REL, (Format)null, false);
        SELECTABLE_TASK_WBS = new Select((String)null, DomainObject.getAttributeSelect("Task WBS"), ExpressionType.REL, (Format)null, false);
        SELECTABLE_IS_PROJECT_TASK = new Select((String)null, "to[Project Access Key]", (ExpressionType)null, (Format)null, false);
        SELECTABLE_HAS_DELETE_ACCESS = new Select((String)null, "current.access[delete]", (ExpressionType)null, (Format)null, false);
        SELECTABLE_IS_SUMMARY_TASK = new Select((String)null, "from[Subtask]", ExpressionType.BUS, (Format)null, false);
        SELECTABLE_ISTASK = new Select((String)null, "type.kindof[Task Management]", ExpressionType.BUS, (Format)null, false);
        SELECTABLE_ATTRIBUTE_TASKWEIGHTAGE_FROM_TASK = new Select((String)null, "to[Subtask].attribute[TaskWeightage]", ExpressionType.BUS, (Format)null, false);
        ATTRIBUTE_TASKWEIGHTAGE = new Select((String)null, DomainObject.getAttributeSelect("TaskWeightage"), (ExpressionType)null, (Format)null, false);
        SELECTABLE_ISPROJECTSPACE = new Select((String)null, "type.kindof[Project Space]", ExpressionType.BUS, (Format)null, false);
        SELECTABLE_ISPROJECTCONCEPT = new Select((String)null, "type.kindof[Project Concept]", ExpressionType.BUS, (Format)null, false);
        _relAttributesData = new HashMap();
        SELECTABLE_DEFAULT_CONSTRAINT_TYPE = new Select("taskDefaultConstraintType", "evaluate[if type.kindof[Task Management] == 'TRUE' then to[Project Access Key].from.from[Project Access List].to.attribute[Default Constraint Type] else attribute[Default Constraint Type]]", ExpressionType.BUS, (Format)null, false);
        SELECTABLE_TASK_CONSTRAINT_TYPE = new Select((String)null, "attribute[Task Constraint Type]", ExpressionType.BUS, (Format)null, false);
    }
}

