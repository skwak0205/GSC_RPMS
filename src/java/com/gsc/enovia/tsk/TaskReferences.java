//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gsc.enovia.tsk;

import com.dassault_systemes.enovia.e6w.foundation.DBUtil;
import com.dassault_systemes.enovia.e6w.foundation.FoundationException;
import com.dassault_systemes.enovia.e6w.foundation.ServiceConstants;
import com.matrixone.apps.domain.util.MapList;
import matrix.db.Context;
import matrix.util.StringList;

import java.util.Map;

public class TaskReferences implements ServiceConstants {
    private static final String RELATIONSHIP_REFERENCE_DOCUMENT = "Reference Document";
    private static final String RELATIONSHIP_TASK_DELIVERABLE = "Task Deliverable";

    public TaskReferences() {
    }

    public static String addTaskReference(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        String var4 = DBUtil.connect(var0, var1, "Reference Document", var2, (Map)null, false);
        return var4;
    }

    public static String addTaskDeliverable(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        String var4 = DBUtil.connect(var0, var1, "Task Deliverable", var2, (Map)null, false);
        return var4;
    }

    public static MapList getTaskReferences(Context var0, String var1, StringList var2, StringList var3, String var4, String var5) throws FoundationException {
        MapList var6 = DBUtil.getRelatedObjects(var0, var1, "Reference Document", "*", var2, var3, false, true, 1, var4, var5, 0);
        return var6;
    }

    public static MapList getTaskDeliverables(Context var0, String var1, StringList var2, StringList var3, String var4, String var5) throws FoundationException {
        MapList var6 = DBUtil.getRelatedObjects(var0, var1, "Task Deliverable", "*", var2, var3, false, true, 1, var4, var5, 0);
        return var6;
    }

    public static void removeTaskReference(Context var0, String var1, String var2) throws FoundationException {
        DBUtil.disconnect(var0, var1, "Reference Document", var2);
    }

    public static void removeTaskDeliverable(Context var0, String var1, String var2) throws FoundationException {
        DBUtil.disconnect(var0, var1, "Task Deliverable", var2);
    }

    public static void removeTaskReference(Context var0, String var1) throws FoundationException {
        DBUtil.disconnect(var0, var1);
    }
}
