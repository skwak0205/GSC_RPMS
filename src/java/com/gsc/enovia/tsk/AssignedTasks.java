//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gsc.enovia.tsk;

import com.dassault_systemes.enovia.e6w.foundation.*;
import com.matrixone.apps.domain.util.MapList;
import matrix.db.Context;
import matrix.util.StringList;

import java.util.Map;

public class AssignedTasks implements ServiceConstants {
    public static final String ATTRIBUTE_PERCENT_ALLOCATION = "Percent Allocation";
    public static final String SELECT_PERCENT_ALLOCATION = "attribute[Percent Allocation]";
    public static final String SELECT_TASK_ID = "to.id";
    public static final String SELECT_PERSON_ID = "from.id";
    private static final String RELATIONSHIP_ASSIGNED_TASKS = "Assigned Tasks";

    public AssignedTasks() {
    }

    public static Map<String, String> getTaskAssigneeInfo(Context var0, String var1, StringList var2) throws FoundationException {
        String[] var3 = new String[]{var1};
        MapList var4 = DBUtil.getRelInfo(var0, var3, var2);
        return (Map)var4.get(0);
    }

    public static MapList getTaskAssignees(Context var0, String var1, StringList var2, StringList var3, String var4, String var5) throws FoundationException {
        MapList var6 = DBUtil.getRelatedObjects(var0, var1, "Assigned Tasks", "*", var2, var3, true, false, 1, var4, var5, 0);
        return var6;
    }

    public static String addTaskAssignee(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        String var4 = DBUtil.connect(var0, var2, "Assigned Tasks", var1, (Map)null, false);
        DBUtil.createPersonOwnership(var0, var1, var2, "Project Lead");
        modifyTaskAssignee(var0, var4, var3);
        return var4;
    }

    public static String addTaskAssigneeByName(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        DBUtil.startTransaction(var0, true);

        try {
            StringList var4 = new StringList(1);
            var4.addElement("id");
            MapList var5 = Task.getPersonInfo(var0, var2, var4, 1);
            if (var5.isEmpty()) {
                String var10 = ServiceBase.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.AssigneeInvalid", var0.getLocale());
                var10 = var10.replace("%ASSIGNEE%", var2);
                throw new FoundationUserException(var10);
            } else {
                Map var6 = (Map)var5.get(0);
                String var7 = (String)var6.get("id");
                String var8 = addTaskAssignee(var0, var1, var7, var3);
                DBUtil.commitTransaction(var0);
                return var8;
            }
        } catch (FoundationException var9) {
            DBUtil.abortTransaction(var0);
            throw var9;
        }
    }

    public static void modifyTaskAssignee(Context var0, String var1, Map<String, String> var2) throws FoundationException {
        DBUtil.modifyConnection(var0, var1, var2, true);
    }

    public static void removeTaskAssignee(Context var0, String var1, String var2) throws FoundationException {
        DBUtil.disconnect(var0, var2, "Assigned Tasks", var1);
        DBUtil.deletePersonOwnership(var0, var1, var2);
    }

    public static void removeTaskAssignee(Context var0, String var1) throws FoundationException {
        StringList var2 = new StringList(2);
        var2.add("from.id");
        var2.add("to.id");
        Map var3 = getTaskAssigneeInfo(var0, var1, var2);
        DBUtil.disconnect(var0, var1);
        String var4 = (String)var3.get("to.id");
        String var5 = (String)var3.get("from.id");
        DBUtil.deletePersonOwnership(var0, var4, var5);
    }
}
