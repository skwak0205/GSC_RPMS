//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gsc.enovia.tsk;

import com.dassault_systemes.enovia.e6w.foundation.*;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.MqlUtil;
import com.matrixone.apps.domain.util.i18nNow;
import matrix.db.Context;
import matrix.util.MatrixException;
import matrix.util.StringList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Task implements ServiceConstants {
    protected static final String REGISTERED_SUITE = "CollaborativeTasks";
    public static final String TYPE_PERSON = "Person";
    protected static final String TYPE_TASK = "Task";
    private static final String POLICY_PROJECT_TASK = "Project Task";
    protected static final String ATTRIBUTE_PERCENT_COMPLETE = "Percent Complete";
    protected static final String ATTRIBUTE_TASK_ESTIMATED_START_DATE = "Task Estimated Start Date";
    protected static final String ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE = "Task Estimated Finish Date";
    protected static final String ATTRIBUTE_TASK_ESTIMATED_DURATION = "Task Estimated Duration";
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
    protected static final String ATTRIBUTE_SCHEDULED_FROM_RANGE_START = "Project Start Date";
    protected static final String ATTRIBUTE_SCHEDULED_FROM_RANGE_FINISH = "Project Finish Date";
    protected static final String STATE_PROJECT_TASK_CREATE = "Create";
    protected static final String STATE_PROJECT_TASK_ASSIGN = "Assign";
    protected static final String STATE_PROJECT_TASK_ACTIVE = "Active";
    protected static final String STATE_PROJECT_TASK_REVIEW = "Review";
    protected static final String STATE_PROJECT_TASK_COMPLETE = "Complete";
    protected static final String WILDCARD = "*";
    public static final String TASK_FILTER_ALL = "all";
    public static final String TASK_FILTER_OWNED = "owned";
    public static final String TASK_FILTER_ASSIGNED = "assigned";

    public Task() {
    }

    public static Map<String, String> createTask(Context var0, String var1, Map<String, String> var2) throws FoundationException {
        return createTask(var0, var1, (String)null, -1, var2);
    }

    private static Map<String, String> createTask(Context var0, String var1, String var2, int var3, Map<String, String> var4) throws FoundationException {
        try {
            checkLicenseCollaborativeTasks(var0);
            DBUtil.startTransaction(var0, true);
            Map var5 = DBUtil.create(var0, "Task", var1, (String)null, "Project Task", (String)null, var2, false, false, var4);
            DBUtil.commitTransaction(var0);
            return var5;
        } catch (FoundationException var6) {
            var6.printStackTrace();
            DBUtil.abortTransaction(var0);
            throw var6;
        }
    }

    public static MapList getUserTasks(Context var0, String var1, StringList var2) throws FoundationException {
        return getUserTasks(var0, var1, var2, "all", false);
    }

    public static MapList getUserTasks(Context var0, String var1, StringList var2, String var3, boolean var4) throws FoundationException {
        checkLicenseCollaborativeTasks(var0);
        boolean var5 = true;

        try {
            checkLicenseProjectTasks(var0);
        } catch (FoundationException var8) {
            var5 = false;
        }

        if (var2 != null && !var2.isEmpty()) {
            String var6 = buildWhereClauseBasedOnFilter(var0, var3, var1);
            if (!var4 || !var5) {
                if (!var6.isEmpty()) {
                    var6 = var6 + " && ";
                }

                var6 = var6 + "to[Project Access Key] ~~ False";
            }

            MapList var7 = DBUtil.findObjects(var0, "Task", "*", "*", "*", "*", var6, (String)null, true, var2, 0);
            return var7;
        } else {
            throw new FoundationException("You must provide some selectables to Task.getUserTasks");
        }
    }

    protected static String buildWhereClauseBasedOnFilter(Context var0, String var1, String var2) {
        boolean var3 = false;
        boolean var4 = false;
        if ("owned".equals(var1)) {
            var3 = true;
        } else if ("assigned".equals(var1)) {
            var4 = true;
        } else {
            var3 = true;
            var4 = true;
        }

        String var5 = var2;
        if (var2 == null || var2.isEmpty()) {
            var5 = var0.getUser();
        }

        String var6 = "";
        if (var3) {
            var6 = var6 + "owner == \"" + var5 + "\"";
        }

        if (var4) {
            if (!var6.isEmpty()) {
                var6 = var6 + " || ";
            }

            var6 = var6 + "(current != \"Create\" && to[Assigned Tasks].from.name == \"" + var5 + "\")";
        }

        if (!var6.isEmpty()) {
            var6 = "(" + var6 + ") && ";
        }

        var6 = var6 + "from[Subtask] ~~ False";
        return var6;
    }

    public static MapList getTaskInfo(Context var0, String[] var1, StringList var2) throws FoundationException {
        checkLicenseCollaborativeTasks(var0);
        if (var2 != null && !var2.isEmpty()) {
            MapList var3 = DBUtil.getInfo(var0, var1, var2);
            return var3;
        } else {
            throw new FoundationException("You must provide some selectables to Task.getTaskInfo");
        }
    }

    public static void modifyTask(Context var0, String var1, Map<String, String> var2) throws FoundationException {
        checkLicenseCollaborativeTasks(var0);
        DBUtil.modify(var0, var1, var2, false);
    }

    public static void deleteTask(Context var0, String var1) throws FoundationException {
        DBUtil.startTransaction(var0, true);

        try {
            DBUtil.mqlCommand(var0, "delete bus $1_id", new String[]{var1});
            DBUtil.commitTransaction(var0);
        } catch (FoundationException var3) {
            DBUtil.abortTransaction(var0);
            throw new FoundationUserException(ServiceBase.getTranslatedValue(var0, "Framework", "emxCollaborativeTasks.Error.noDeleteAccess", var0.getLocale()));
        }
    }

    public static void setTaskState(Context var0, String var1, String var2) throws FoundationException {
        DBUtil.setState(var0, var1, var2);
    }

    protected static MapList getPersonInfo(Context var0, String var1, StringList var2, int var3) throws FoundationException {
        MapList var4 = DBUtil.findObjects(var0, "Person", var1, "*", "*", "*", "", (String)null, true, var2, var3);
        return var4;
    }

    protected static void checkLicenseCollaborativeTasks(Context var0) throws FoundationException {
        ServiceUtil.checkLicenseReserved(var0, "ENO6WGV_TP");
    }

    protected static void checkLicenseProjectTasks(Context var0) throws FoundationException {
        ServiceUtil.checkLicenseReserved(var0, "ENO_PGE_TP");
    }

    protected static MapList getStates(Context var0, String var1) throws FoundationException {
        MapList var2 = new MapList(1);
        String var3 = var0.getLocale().getLanguage();

        try {
            String var5 = MqlUtil.mqlCommand(var0, "print policy $1 select $2 dump $3", new String[]{var1, "state", ","});
            StringList var6 = FrameworkUtil.split(var5, ",");
            Iterator var7 = var6.iterator();

            while(var7.hasNext()) {
                String var8 = (String)var7.next();
                String var9 = i18nNow.getStateI18NString(var1, var8, var3);
                HashMap var10 = new HashMap(2);
                var2.add(var10);
                var10.put("value", var8);
                var10.put("display", var9);
            }

            return var2;
        } catch (MatrixException var11) {
            throw new FoundationException(var11);
        }
    }
}
