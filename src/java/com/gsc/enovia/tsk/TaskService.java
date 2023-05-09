//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gsc.enovia.tsk;

import com.dassault_systemes.enovia.e6w.foundation.*;
import com.dassault_systemes.enovia.e6w.foundation.jaxb.*;
import com.dassault_systemes.enovia.e6w.foundation.jaxbext.RelateddataMap;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationUtil;
import com.matrixone.apps.domain.util.DateUtil;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.framework.ui.UIUtil;
import matrix.db.Context;
import matrix.util.StringList;

import java.net.URLDecoder;
import java.util.*;

public class TaskService implements ServiceConstants {
    private Map<String, Map<String, String>> _persons = null;
    protected static final String CONSTANT_STATUS_NOT_STARTED = "NotStarted";
    protected static final String CONSTANT_STATUS_IN_PROGRESS = "InProgress";
    protected static final String CONSTANT_STATUS_DONE = "Done";
    private static final Map<String, String> STATE_MAPPINGS = new HashMap(5);
    private static final Map<String, String> STATUS_MAPPINGS;
    private static final String KEY_TENANT_SCOPE_TYPES = "TASK SCOPE TYPES";
    private static final String KEY_TENANT_ALL_SCOPE_TYPES = "TASK SCOPE ALL TYPES";

    public TaskService() {
    }

    private static Map computeEstimates(Context var0, String[] var1) throws FoundationException {
        Map var2 = ServiceUtil.unpackArgs(var1);
        Map var3 = (Map)var2.get("JPO_WIDGET_ARGS");
        Datagroup var4 = (Datagroup)var2.get("JPO_OBJECT_MAP");
        UpdateActions var5 = var4.getUpdateAction();
        String var6 = var4.getObjectId();
        boolean var7 = UpdateActions.CREATE.equals(var5);
        String var8 = "";
        String var9 = "";
        boolean var10 = false;
        String var11 = "";
        String var12 = "0.0";
        String var13 = "";
        if (UIUtil.isNotNullAndNotEmpty(var6)) {
            StringList var14 = new StringList();
            var14.add("attribute[Task Estimated Duration]");
            var14.add("to[Project Access Key]");
            var14.add("to[Project Access Key].from.from[Project Access List].to.attribute[Schedule From]");
            Map var15 = DBUtil.getInfo(var0, var6, var14);
            var11 = (String)var15.get("to[Project Access Key]");
            var12 = (String)var15.get("attribute[Task Estimated Duration]");
            var13 = (String)var15.get("to[Project Access Key].from.from[Project Access List].to.attribute[Schedule From]");
        }

        HashMap var28 = new HashMap();
        new Double(0.0);
        long var16 = 0L;
        String var18 = ServiceSave.getUpdatedFieldValue(var4, "estimatedDuration", var7);
        String var19 = ServiceSave.getUpdatedFieldValue(var4, "estimatedStartDate", var7);
        String var20 = ServiceSave.getUpdatedFieldValue(var4, "estimatedFinishDate", var7);
        String var21 = ServiceSave.getUpdatedFieldValue(var4, "dueDate", var7);
        if ((var19 == null || var19.isEmpty()) && (var20 == null || var20.isEmpty()) && (var18 == null || var18.isEmpty()) && var21 != null) {
            if ("".equals(var21)) {
                if ("False".equalsIgnoreCase(var11)) {
                    addToMap(var28, "Task Estimated Duration", "0.0");
                    addToMap(var28, "Task Estimated Start Date", var21);
                    addToMap(var28, "Task Estimated Finish Date", var21);
                    return var28;
                }

                throw new FoundationUserException("");
            }

            var10 = true;
            var20 = var21;
            if ("0.0".equals(var12)) {
                var18 = "1";
            }
        }

        Double var29;
        if (var18 != null) {
            var29 = Double.parseDouble(var18);
            addToMap(var28, "Task Estimated Duration", var18);
        } else {
            var29 = Double.parseDouble(var12);
            addToMap(var28, "Task Estimated Duration", var12);
        }

        if (var29 == 0.0 && (var19 == null || var19.isEmpty()) && (var20 == null || var20.isEmpty())) {
            var28.clear();
            return var28;
        } else {
            Date var22;
            Date var23;
            if (var19 == null || var19.isEmpty() || var20 != null && !var20.isEmpty()) {
                if ((var19 == null || var19.isEmpty()) && var20 != null && !var20.isEmpty()) {
                    var10 = true;
                    var22 = ServiceUtil.getDate(var20);
                    var9 = ServiceUtil.formatMxDate(var22, var3);
                    if (var29 == 0.0) {
                        throw new FoundationUserException("");
                    }

                    var16 = var29.longValue() * 86400000L;
                    var23 = DateUtil.computeStartDate(var22, var16);
                    var8 = ServiceUtil.formatMxDate(var23, var3);
                    var28.put("Task Estimated Finish Date", var9);
                    var28.put("Task Estimated Start Date", var8);
                } else if ((var19 == null || var19.isEmpty()) && (var20 == null || var20.isEmpty())) {
                    var19 = ServiceSave.getUpdatedFieldValue(var4, "estimatedStartDate", true);
                    var22 = ServiceUtil.getDate(var19);
                    if (var22 != null) {
                        var8 = ServiceUtil.formatMxDate(var22, var3);
                        if (var29 != 0.0) {
                            var16 = var29.longValue() * 86400000L;
                            var23 = DateUtil.computeFinishDate(var22, var16);
                            var9 = ServiceUtil.formatMxDate(var23, var3);
                            var28.put("Task Estimated Start Date", var8);
                            var28.put("Task Estimated Finish Date", var9);
                        }
                    }
                } else if (var19 != null && !var19.isEmpty() && var20 != null && !var20.isEmpty()) {
                    long var30 = Long.parseLong(var19);
                    long var24 = Long.parseLong(var20);
                    if (var30 > var24) {
                        throw new FoundationUserException("");
                    }

                    Date var26 = ServiceUtil.getDate(var19);
                    var8 = ServiceUtil.formatMxDate(var26, var3);
                    var28.put("Task Estimated Start Date", var8);
                    Date var27 = ServiceUtil.getDate(var20);
                    var9 = ServiceUtil.formatMxDate(var27, var3);
                    var28.put("Task Estimated Finish Date", var9);
                    var16 = DateUtil.computeDuration(var26, var27);
                    var18 = var16 + "";
                    var28.put("Task Estimated Duration", var18);
                }
            } else {
                var22 = ServiceUtil.getDate(var19);
                var8 = ServiceUtil.formatMxDate(var22, var3);
                if (var29 == 0.0) {
                    throw new FoundationUserException("");
                }

                var16 = var29.longValue() * 86400000L;
                var23 = DateUtil.computeFinishDate(var22, var16);
                var9 = ServiceUtil.formatMxDate(var23, var3);
                var28.put("Task Estimated Start Date", var8);
                var28.put("Task Estimated Finish Date", var9);
            }

            if (UIUtil.isNotNullAndNotEmpty(var13)) {
                if (var10) {
                    if ("Project Start Date".equals(var13)) {
                        var28.put("Task Constraint Type", "Finish No Earlier Than");
                        var28.put("Task Constraint Date", var9);
                    } else {
                        var28.put("Task Constraint Type", "Finish No Later Than");
                        var28.put("Task Constraint Date", var9);
                    }
                } else if ("Project Start Date".equals(var13)) {
                    var28.put("Task Constraint Type", "Start No Earlier Than");
                    var28.put("Task Constraint Date", var8);
                } else {
                    var28.put("Task Constraint Type", "Start No Later Than");
                    var28.put("Task Constraint Date", var8);
                }
            }

            return var28;
        }
    }

    public static Datagroup updateTasks(Context var0, String[] var1) throws FoundationException {
        try {
            Map var2 = ServiceUtil.unpackArgs(var1);
            Map var3 = (Map)var2.get("JPO_WIDGET_ARGS");
            Datagroup var4 = (Datagroup)var2.get("JPO_OBJECT_MAP");
            UpdateActions var5 = var4.getUpdateAction();
            String var6 = var4.getObjectId();
            String var10;
            if (UpdateActions.CREATE.equals(var5) || UpdateActions.MODIFY.equals(var5)) {
                HashMap var7 = new HashMap(2);
                boolean var8 = UpdateActions.CREATE.equals(var5);
                String var9 = ServiceSave.getUpdatedFieldValue(var4, "description", var8);
                addToMap(var7, "description", var9);
                var10 = ServiceSave.getUpdatedFieldValue(var4, "state", var8);
                Map var11 = computeEstimates(var0, var1);
                var7.putAll(var11);
                String var12 = ServiceSave.getUpdatedFieldValue(var4, "percentComplete", var8);
                if (var12 != null) {
                    var7.put("Percent Complete", var12);
                }

                String var13 = ServiceSave.getUpdatedFieldValue(var4, "title", var8);
                if (var13 != null) {
                    var13 = var13.trim();
                }

                if (var13 != null && var13.isEmpty() || var13 == null && UpdateActions.CREATE.equals(var5)) {
                    String var23 = ServiceBase.getTranslatedValue(var0, "Foundation", "emxFoundation.Widget.Error.EmptyTitleNotAllowed", var0.getLocale());
                    throw new FoundationUserException(var23);
                }

                if (UpdateActions.CREATE.equals(var5)) {
                    Map var14 = Task.createTask(var0, var13, var7);
                    var6 = (String)var14.get("id");
                    if (var10 != null) {
                        Task.setTaskState(var0, var6, var10);
                    }

                    var4 = getTask(var0, var3, var6);
                }

                if (UpdateActions.MODIFY.equals(var5)) {
                    if (var10 != null) {
                        Task.setTaskState(var0, var6, var10);
                    }

                    addToMap(var7, "name", var13);
                    Task.modifyTask(var0, var6, var7);
                    StringList var22 = new StringList();
                    var22.addElement("to[Project Access Key].from.id");
                    Map var15 = DBUtil.getInfo(var0, var6, var22);
                    String var16 = (String)var15.get("to[Project Access Key].from.id");
                    if (UIUtil.isNotNullAndNotEmpty(var16)) {
                        com.matrixone.apps.common.Task var17 = new com.matrixone.apps.common.Task(var6);
                        var17.rollupAndSave(var0);
                    }

                    var4 = getTask(var0, var3, var6);
                    var4.setRelateddata((RelateddataMap)null);
                }
            }

            if (UpdateActions.DELETE.equals(var5)) {
                StringList var19 = new StringList();
                var19.addElement("to[Project Access Key].from.from[Project Access List].to.id");
                Map var20 = DBUtil.getInfo(var0, var6, var19);
                var10 = (String)var20.get("to[Project Access Key].from.from[Project Access List].to.id");
                Task.deleteTask(var0, var6);
                if (UIUtil.isNotNullAndNotEmpty(var10)) {
                    com.matrixone.apps.common.Task var21 = new com.matrixone.apps.common.Task(var10);
                    var21.reSequence(var0, var10);
                    var21.rollupAndSave(var0);
                }
            }

            return var4;
        } catch (Exception var18) {
            throw new FoundationException(var18);
        }
    }

    public static Datagroup updateTaskAssignees(Context var0, String[] var1) throws FoundationException {
        try {
            Map var2 = ServiceUtil.unpackArgs(var1);
            Map var3 = (Map)var2.get("JPO_WIDGET_ARGS");
            Datagroup var4 = (Datagroup)var2.get("JPO_OBJECT_MAP");
            UpdateActions var5 = var4.getUpdateAction();
            String var6 = var4.getParent().getObjectId();
            if (UpdateActions.CONNECT.equals(var5)) {
                String var7 = var4.getObjectId();
                String var8 = null;
                if (var7 == null) {
                    String var9 = ServiceSave.getUpdatedFieldValue(var4, "name", true);
                    var8 = AssignedTasks.addTaskAssigneeByName(var0, var6, var9, (Map)null);
                } else {
                    var8 = AssignedTasks.addTaskAssignee(var0, var6, var7, (Map)null);
                }

                var4 = getObject(var0, var3, var6, "assignees", var8);
            }

            if (UpdateActions.DISCONNECT.equals(var5)) {
                AssignedTasks.removeTaskAssignee(var0, var6, var4.getObjectId());
            }

            return var4;
        } catch (Exception var10) {
            var10.printStackTrace();
            throw var10;
        }
    }

    public static Datagroup updateTaskReferences(Context var0, String[] var1) throws FoundationException {
        try {
            Map var2 = ServiceUtil.unpackArgs(var1);
            Map var3 = (Map)var2.get("JPO_WIDGET_ARGS");
            Datagroup var4 = (Datagroup)var2.get("JPO_OBJECT_MAP");
            String var5 = var4.getObjectId();
            String var6 = var4.getParent().getObjectId();
            UpdateActions var7 = var4.getUpdateAction();
            if (UpdateActions.CONNECT.equals(var7)) {
                String var8 = TaskReferences.addTaskReference(var0, var6, var5, (Map)null);
                var4 = getObject(var0, var3, var6, "references", var8);
            }

            if (UpdateActions.DISCONNECT.equals(var7)) {
                TaskReferences.removeTaskReference(var0, var6, var5);
            }

            return var4;
        } catch (Exception var9) {
            var9.printStackTrace();
            throw var9;
        }
    }

    public static Datagroup updateTaskDeliverables(Context var0, String[] var1) throws FoundationException {
        try {
            Map var2 = ServiceUtil.unpackArgs(var1);
            Map var3 = (Map)var2.get("JPO_WIDGET_ARGS");
            Datagroup var4 = (Datagroup)var2.get("JPO_OBJECT_MAP");
            String var5 = var4.getObjectId();
            String var6 = var4.getParent().getObjectId();
            UpdateActions var7 = var4.getUpdateAction();
            if (UpdateActions.CONNECT.equals(var7)) {
                String var8 = TaskReferences.addTaskDeliverable(var0, var6, var5, (Map)null);
                var4 = getObject(var0, var3, var6, "deliverables", var8);
            }

            if (UpdateActions.DISCONNECT.equals(var7)) {
                TaskReferences.removeTaskDeliverable(var0, var6, var5);
            }

            return var4;
        } catch (Exception var9) {
            var9.printStackTrace();
            throw var9;
        }
    }

    public static Datagroup updateTaskScopes(Context var0, String[] var1) throws FoundationException {
        try {
            Map var2 = ServiceUtil.unpackArgs(var1);
            Map var3 = (Map)var2.get("JPO_WIDGET_ARGS");
            Datagroup var4 = (Datagroup)var2.get("JPO_OBJECT_MAP");
            UpdateActions var5 = var4.getUpdateAction();
            String var6 = var4.getParent().getObjectId();
            if (UpdateActions.CONNECT.equals(var5)) {
                String var7 = var4.getObjectId();
                String var8 = null;
                if (var7 == null) {
                    String var9 = ServiceSave.getUpdatedFieldValue(var4, "name", true);
                    var8 = TaskScope.addTaskScopeByName(var0, var6, var9, (Map)null);
                } else {
                    var8 = TaskScope.addTaskScope(var0, var6, var7, (Map)null);
                }

                var4 = getObject(var0, var3, var6, "scopes", var8);
            }

            if (UpdateActions.DISCONNECT.equals(var5)) {
                TaskScope.removeTaskScope(var0, var6, var4.getObjectId());
            }

            return var4;
        } catch (Exception var10) {
            var10.printStackTrace();
            throw var10;
        }
    }

    public static Datagroup updateProjectScope(Context var0, String[] var1) throws FoundationException {
        try {
            Map var2 = ServiceUtil.unpackArgs(var1);
            Map var3 = (Map)var2.get("JPO_WIDGET_ARGS");
            Datagroup var4 = (Datagroup)var2.get("JPO_OBJECT_MAP");
            UpdateActions var5 = var4.getUpdateAction();
            String var6 = var4.getParent().getObjectId();
            if (UpdateActions.CONNECT.equals(var5)) {
                String var7 = var4.getObjectId();
                TaskScope.addTaskToProjectScope(var0, var6, var7, (Map)null);
                Datagroup var9 = getTask(var0, var3, var6);
                var4.getParent().getDataelements().putAll(var9.getDataelements());
                var4 = (Datagroup)((Datarecord)var9.getRelateddata().get("DPMProject")).getDatagroups().get(0);
            }

            return var4;
        } catch (Exception var10) {
            throw var10;
        }
    }

    public static MapList refreshTasks(Context var0, String[] var1) throws Throwable {
        Task.checkLicenseCollaborativeTasks(var0);
        Map var2 = ServiceUtil.unpackArgs(var1);
        Map var3 = (Map)var2.get("JPO_WIDGET_ARGS");
        Datarecord var4 = null;
        String var5 = ServiceBase.getArgStr(var3, "refreshWidget");
        if (!var5.isEmpty()) {
            var5 = URLDecoder.decode(var5, "UTF-8");
            ServiceContainer var6 = (ServiceContainer)FoundationUtil.transformJSONStringtoJAXB(var5, ServiceContainer.class);
            var4 = var6.getDatarecords();
        }

        HashMap var15 = new HashMap(10);
        if (var4 != null) {
            Iterator var7 = var4.getDatagroups().iterator();

            while(var7.hasNext()) {
                Datagroup var8 = (Datagroup)var7.next();
                String var9 = var8.getObjectId();
                if (var9 != null && !var9.isEmpty()) {
                    var15.put(var9, var8);
                    var8.setUpdateAction((UpdateActions)null);
                }
            }
        }

        StringList var16 = new StringList(2);
        var16.add("id");
        var16.add("cestamp");
        String var17 = var0.getUser();
        MapList var18 = getTasks(var0, var3, var16);

        for(int var10 = 0; var10 < var18.size(); ++var10) {
            Map var11 = (Map)var18.get(var10);
            String var12 = (String)var11.get("id");
            String var13 = (String)var11.get("cestamp");
            Datagroup var14;
            if (var15.containsKey(var12)) {
                var14 = (Datagroup)var15.get(var12);
                if (var13.equals(var14.getCestamp())) {
                    var15.remove(var12);
                } else {
                    var14.setUpdateAction(UpdateActions.MODIFY);
                }
            } else {
                var14 = new Datagroup();
                var14.setObjectId(var12);
                var14.setCestamp(var13);
                var14.setUpdateAction(UpdateActions.CREATE);
                var15.put(var12, var14);
            }
        }

        MapList var19 = new MapList();
        Iterator var20 = var15.values().iterator();

        while(var20.hasNext()) {
            Datagroup var21 = (Datagroup)var20.next();
            HashMap var22 = new HashMap(2);
            var19.add(var22);
            if (var21.getUpdateAction() == null) {
                var22.put("updateAction", UpdateActions.DELETE);
                var22.put("tempid", var21.getPhysicalId());
            } else {
                var22.put("updateAction", var21.getUpdateAction());
                var22.put("id", var21.getObjectId());
            }
        }

        return var19;
    }

    public static MapList getTasks(Context var0, String[] var1) throws FoundationException {
        Task.checkLicenseCollaborativeTasks(var0);
        Map var2 = ServiceUtil.unpackArgs(var1);
        Map var3 = (Map)var2.get("JPO_WIDGET_ARGS");
        StringList var4 = (StringList)var2.get("JPO_BUS_SELECTS");
        MapList var5 = getTasks(var0, var3, var4);
        return var5;
    }

    private static MapList getTasks(Context var0, Map<String, Object> var1, StringList var2) throws FoundationException {
        String var3 = ServiceBase.getArgStr(var1, "objectId");
        String var4 = !"".equals(var3) ? var3 : ServiceBase.getArgStr(var1, "objectIds");
        String var5 = ServiceBase.getArgStr(var1, "scopeId");
        String var6 = ServiceBase.getArgStr(var1, "showProjectTasks");
        boolean var7 = Boolean.valueOf(var6);
        String var8 = ServiceBase.getArgStr(var1, "currentTaskFilter");
        MapList var9 = null;
        if (!"".equals(var4)) {
            String[] var10 = var4.split(" ");
            var9 = Task.getTaskInfo(var0, var10, var2);
        } else if (!"".equals(var5)) {
            StringList var13 = new StringList();
            var13.add("type");
            Map var11 = DBUtil.getInfo(var0, var5, var13);
            String var12 = (String)var11.get("type");
            if ("Project Space".equals(var12)) {
                var9 = TaskScope.getProjectScopeTasks(var0, var5, var7, var2, var8);
            } else {
                var9 = TaskScope.getScopeTasks(var0, var5, var2, var8);
            }
        } else {
            var9 = Task.getUserTasks(var0, "", var2, var8, var7);
        }

        restrictModifyOnCompleteTasks(var9);
        return var9;
    }

    private static void restrictModifyOnCompleteTasks(MapList var0) {
        Iterator var1 = var0.iterator();

        while(var1.hasNext()) {
            Map var2 = (Map)var1.next();
            String var3 = (String)var2.get("current");
            if ("Complete".equals(var3)) {
                var2.put("current.access[modify,changename]", "FALSE");
            }
        }

    }

    public static MapList getScopeRangeTypes(Context var0, String[] var1) throws FoundationException {
        Map var2 = ServiceUtil.unpackArgs(var1);
        Map var3 = (Map)var2.get("JPO_ARGS");
        String var4 = (String)var3.get("searchtypes");
        String var5 = null;
        Class var6 = TaskService.class;
        synchronized(TaskService.class) {
            String var7 = (String)DBUtil.getCacheObject(var0, "TASK SCOPE TYPES");
            if (!var4.equals(var7)) {
                List var8 = DBUtil.getTypeDerivatives(var0, var4);
                StringList var9 = new StringList(var8.size());
                var9.addAll(var8);
                var5 = FrameworkUtil.join(var9, ",");
                DBUtil.setCacheObject(var0, "TASK SCOPE TYPES", var4);
                DBUtil.setCacheObject(var0, "TASK SCOPE ALL TYPES", var5);
            } else {
                var5 = (String)DBUtil.getCacheObject(var0, "TASK SCOPE ALL TYPES");
            }
        }

        MapList var12 = new MapList(1);
        HashMap var13 = new HashMap(2);
        var12.add(var13);
        var13.put("value", var4);
        var13.put("display", var5);
        return var12;
    }

    public List<MapList> getPerson(Context var1, String[] var2) throws FoundationException {
        Map var3 = ServiceUtil.unpackArgs(var2);
        Map var4 = (Map)var3.get("JPO_WIDGET_ARGS");
        Map var5 = (Map)var3.get("JPO_ARGS");
        StringList var6 = (StringList)var3.get("JPO_BUS_SELECTS");
        List var7 = (List)var3.get("JPO_OBJECT_MAP");
        String var13;
        Map var22;
        if (this._persons == null) {
            StringList var8 = new StringList(var7.size() / 2);
            Iterator var9 = var7.iterator();

            while(var9.hasNext()) {
                Datagroup var10 = (Datagroup)var9.next();
                if (!((Dataelement)var10.getDataelements().get("ownerName")).getValue().isEmpty()) {
                    String var11 = ((FieldValue)((Dataelement)var10.getDataelements().get("ownerName")).getValue().get(0)).getValue();
                    String var12 = ((FieldValue)((Dataelement)var10.getDataelements().get("originatorName")).getValue().get(0)).getValue();
                    if (var8.indexOf(var11) == -1) {
                        var8.add(var11);
                    }

                    if (!"".equals(var12) && var8.indexOf(var12) == -1) {
                        var8.add(var12);
                    }
                }
            }

            String var16 = FrameworkUtil.join(var8, ",");
            MapList var18 = Task.getPersonInfo(var1, var16, var6, 0);
            this._persons = new HashMap(var18.size());

            for(int var20 = 0; var20 < var18.size(); ++var20) {
                var22 = (Map)var18.get(var20);
                var13 = (String)var22.get("name");
                this._persons.put(var13, var22);
            }
        }

        boolean var15 = "true".equals(var5.get("owner"));
        ArrayList var17 = new ArrayList(var7.size());

        MapList var23;
        for(Iterator var19 = var7.iterator(); var19.hasNext(); var17.add(var23)) {
            Datagroup var21 = (Datagroup)var19.next();
            var22 = null;

            try {
                var13 = var15 ? ((FieldValue)((Dataelement)var21.getDataelements().remove("ownerName")).getValue().get(0)).getValue() : ((FieldValue)((Dataelement)var21.getDataelements().remove("originatorName")).getValue().get(0)).getValue();
                var22 = (Map)this._persons.get(var13);
            } catch (Exception var14) {
            }

            var23 = new MapList(1);
            if (var22 != null) {
                var23.add(var22);
            }
        }

        return var17;
    }

    public static MapList getTaskStatus(Context var0, String[] var1) throws FoundationException {
        Map var2 = ServiceUtil.unpackArgs(var1);
        MapList var3 = (MapList)var2.get("JPO_WIDGET_DATA");
        String var4 = (String)var2.get("JPO_WIDGET_FIELD_KEY");

        for(int var5 = 0; var5 < var3.size(); ++var5) {
            Map var6 = (Map)var3.get(var5);
            String var7 = (String)var6.get(var4);
            var6.put(var4, var7);
        }

        return var3;
    }

    protected static String convertToState(String var0) {
        return (String)STATUS_MAPPINGS.get(var0);
    }

    protected static String convertToStatus(String var0) {
        return (String)STATE_MAPPINGS.get(var0);
    }

    private static void addToMap(Map<String, String> var0, String var1, String var2) {
        if (var2 != null) {
            var0.put(var1, var2);
        }

    }

    protected static Datagroup getTask(Context var0, String var1, String var2) throws FoundationException {
        HashMap var3 = new HashMap(2);
        var3.put("arg_widget_name", var1);
        return getTask(var0, (Map)var3, var2);
    }

    protected static Datagroup getTask(Context var0, Map<String, Object> var1, String var2) throws FoundationException {
        HashMap var3 = new HashMap(2);
        String var4 = ServiceBase.getArgStr(var1, "arg_widget_name");
        String var5 = ServiceBase.getArgStr(var1, "arg_uri");
        var3.put("objectId", var2);
        var3.put("arg_uri", var5);
        Service var6 = (Service)ServiceBase.getWidgetData(var0, var4, var3);
        Datagroup var7 = var6.getDatarecords().getDatagroups().size() > 0 ? (Datagroup)var6.getDatarecords().getDatagroups().get(0) : null;
        return var7;
    }

    protected static Datagroup getObject(Context var0, Map<String, Object> var1, String var2, String var3, String var4) throws FoundationException {
        Datagroup var5 = getTask(var0, var1, var2);
        List var6 = ((Datarecord)var5.getRelateddata().get(var3)).getDatagroups();
        Datagroup var7 = null;
        Iterator var8 = var6.iterator();

        while(var8.hasNext()) {
            Datagroup var9 = (Datagroup)var8.next();
            if (var4.equals(var9.getRelId())) {
                var7 = var9;
                break;
            }
        }

        return var7;
    }

    public static MapList getStates(Context var0, String[] var1) throws FoundationException {
        Map var2 = ServiceUtil.unpackArgs(var1);
        Map var3 = (Map)var2.get("JPO_ARGS");
        String var4 = (String)var3.get("policy");
        return Task.getStates(var0, var4);
    }

    static {
        STATE_MAPPINGS.put("Create", "Create");
        STATE_MAPPINGS.put("Assign", "Assign");
        STATE_MAPPINGS.put("Active", "Active");
        STATE_MAPPINGS.put("Review", "Review");
        STATE_MAPPINGS.put("Complete", "Complete");
        STATUS_MAPPINGS = new HashMap(3);
        STATUS_MAPPINGS.put("Create", "Create");
        STATUS_MAPPINGS.put("Assign", "Assign");
        STATUS_MAPPINGS.put("Active", "Active");
        STATUS_MAPPINGS.put("Review", "Review");
        STATUS_MAPPINGS.put("Complete", "Complete");
    }
}
