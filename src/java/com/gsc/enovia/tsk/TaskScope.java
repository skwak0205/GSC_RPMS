//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gsc.enovia.tsk;

import com.dassault_systemes.enovia.e6w.foundation.*;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.eMatrixDateFormat;
import matrix.db.Context;
import matrix.util.StringList;

import java.text.SimpleDateFormat;
import java.util.*;

public class TaskScope implements ServiceConstants {
    public static final String SELECT_TASK_ID = "from.id";
    public static final String SELECT_SCOPE_ID = "to.id";
    public static final String TYPE_PROJECT_SPACE = "Project Space";
    public static final String TYPE_PROJECT_ACCESS_LIST = "Project Access List";
    public static final String POLICY_PROJECT_SPACE = "Project Space";
    public static final String POLICY_PROJECT_ACCESS_LIST = "Project Access List";
    protected static final String RELATIONSHIP_CONTRIBUTES_TO = "Contributes To";
    protected static final String RELATIONSHIP_SUBTASK = "Subtask";
    public static final String RELATIONSHIP_PROJECT_ACCESS_LIST = "Project Access List";
    public static final String RELATIONSHIP_PROJECT_ACCESS_KEY = "Project Access Key";
    public static final String SELECT_PROJECT_ACCESS_LIST_REL_ID = "to[Project Access List].id";
    public static final String SELECT_PROJECT_ACCESS_LIST_ID = "to[Project Access List].from.id";
    public static final String SELECT_PROJECT_ACCESS_KEY_ID = "to[Project Access Key].from.id";

    public TaskScope() {
    }

    public static Map<String, Object> getTaskScopeRelInfo(Context var0, String var1, StringList var2) throws FoundationException {
        String[] var3 = new String[]{var1};
        MapList var4 = DBUtil.getRelInfo(var0, var3, var2);
        return (Map)var4.get(0);
    }

    public static MapList getTaskScopes(Context var0, String var1, StringList var2, StringList var3, String var4, String var5) throws FoundationException {
        MapList var6 = DBUtil.getRelatedObjects(var0, var1, "Contributes To", "*", var2, var3, true, false, 1, var4, var5, 0);
        return var6;
    }

    public static MapList getScopeTasks(Context var0, String var1, StringList var2, StringList var3, String var4, String var5, String var6) throws FoundationException {
        String var7 = var4;
        if (var4 == null) {
            var7 = "";
        }

        if (!var7.isEmpty()) {
            var7 = var7 + " && ";
        }

        var7 = var7 + Task.buildWhereClauseBasedOnFilter(var0, var6, (String)null);
        MapList var8 = DBUtil.getRelatedObjects(var0, var1, "Contributes To", "Task", var2, var3, false, true, 1, var7, var5, 0);
        return var8;
    }

    public static MapList getProjectScopeTasks(Context var0, String var1, boolean var2, StringList var3, String var4) throws FoundationException {
        Task.checkLicenseCollaborativeTasks(var0);
        boolean var5 = true;

        try {
            Task.checkLicenseProjectTasks(var0);
        } catch (FoundationException var8) {
            var5 = false;
        }

        String var6 = Task.buildWhereClauseBasedOnFilter(var0, var4, (String)null);
        if (!var2 || !var5) {
            var6 = var6 + " && to[Project Access Key] ~~ False";
        }

        MapList var7 = DBUtil.getRelatedObjects(var0, var1, "Subtask", "Task", var3, (StringList)null, false, true, -1, var6, (String)null, 0);
        return var7;
    }

    public static MapList getScopeTasks(Context var0, String var1, StringList var2, String var3) throws FoundationException {
        return getScopeTasks(var0, var1, var2, (StringList)null, (String)null, (String)null, var3);
    }

    public static String addTaskScope(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        String var4 = DBUtil.connect(var0, var2, "Contributes To", var1, (Map)null, false);
        modifyTaskScope(var0, var4, var3);
        return var4;
    }

    public static String addTaskToProjectScope(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        try {
            Calendar var4 = Calendar.getInstance();
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
            Task.modifyTask(var0, var1, var10);
            StringList var11 = new StringList();
            var11.add("id");
            var11.add("to[Project Access List].from.id");
            var11.add("to[Project Access List].id");
            Map var12 = DBUtil.getInfo(var0, var2, var11);
            String var13 = (String)var12.get("to[Project Access List].from.id");
            String var14 = (String)var12.get("id");
            String var15 = (String)var12.get("to[Project Access List].id");
            DBUtil.connect(var0, var13, "Project Access Key", var1, (Map)null, false);
            String[] var16 = new String[]{var1};
            com.matrixone.apps.common.Task var17 = new com.matrixone.apps.common.Task(var14);
            var17.addExisting(var0, var16, (String)null);
            return var15;
        } catch (Exception var18) {
            throw new FoundationException(var18);
        }
    }

    public static String addTaskScopeByName(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        DBUtil.startTransaction(var0, true);

        try {
            StringList var4 = new StringList(1);
            var4.addElement("id");
            MapList var5 = DBUtil.findObjects(var0, "*", var2, "*", "*", "*", "", (String)null, true, var4, 1);
            if (var5.isEmpty()) {
                String var10 = ServiceBase.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.ScopeInvalid", var0.getLocale());
                var10 = var10.replace("%SCOPE%", var2);
                throw new FoundationUserException(var10);
            } else {
                Map var6 = (Map)var5.get(0);
                String var7 = (String)var6.get("id");
                String var8 = addTaskScope(var0, var1, var7, var3);
                DBUtil.commitTransaction(var0);
                return var8;
            }
        } catch (FoundationException var9) {
            DBUtil.abortTransaction(var0);
            throw var9;
        }
    }

    public static void modifyTaskScope(Context var0, String var1, Map<String, String> var2) throws FoundationException {
        DBUtil.modifyConnection(var0, var1, var2, true);
    }

    public static void removeTaskScope(Context var0, String var1, String var2) throws FoundationException {
        DBUtil.disconnect(var0, var2, "Contributes To", var1);
    }

    public static void removeTaskScope(Context var0, String var1) throws FoundationException {
        DBUtil.disconnect(var0, var1);
    }
}
