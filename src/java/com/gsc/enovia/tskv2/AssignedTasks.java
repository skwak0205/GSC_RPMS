//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gsc.enovia.tskv2;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationUserException;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceConstants;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceSave;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ContextUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectEditUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.Select;
import matrix.db.Context;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssignedTasks implements ServiceConstants {
    public static final String ATTRIBUTE_PERCENT_ALLOCATION = "Percent Allocation";
    public static final String SELECT_PERCENT_ALLOCATION = "attribute[Percent Allocation]";
    public static final String SELECT_TASK_ID = "to.physicalid";
    public static final String SELECT_PERSON_ID = "from.physicalid";
    protected static final String RELATIONSHIP_ASSIGNED_TASKS = "Assigned Tasks";

    public AssignedTasks() {
    }

    public static Dataobject getTaskAssigneeRelInfo(Context var0, String var1, List<Selectable> var2) throws FoundationException {
        Dataobject var3 = new Dataobject();
        var3.setRelId(var1);
        Datacollection var4 = new Datacollection();
        var4.getDataobjects().add(var3);
        PrintData var5 = new PrintData();
        return ObjectUtil.print(var0, var3, var5, var2, false);
    }

    public static Datacollection getTaskAssignees(Context var0, String var1, List<Selectable> var2, List<Selectable> var3, String var4, String var5) throws FoundationException {
        Dataobject var6 = new Dataobject();
        var6.setId(var1);
        ExpandData var7 = new ExpandData();
        var7.setTypePattern("*");
        var7.setRelationshipPattern("Assigned Tasks");
        var7.setLimit(Short.valueOf((short)0));
        var7.setRecurseToLevel(BigDecimal.ONE);
        var7.setGetTo(true);
        var7.setGetFrom(false);
        var7.setObjectWhere(var4);
        var7.setRelationshipWhere(var5);
        ArrayList var8 = new ArrayList();
        var8.addAll(var2);
        var8.addAll(var3);
        return ObjectUtil.expand(var0, var6, var7, var8);
    }

    public static String addTaskAssignee(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        return addTaskAssignee(var0, var1, var2, var3, "Assigned Tasks");
    }

    public static String addTaskAssignee(Context var0, String var1, String var2, Map<String, String> var3, String var4) throws FoundationException {
        boolean var5 = !ServiceSave.isObjectNew(var0, var1);
        String var6 = ObjectEditUtil.connect(var0, var2, var4, var1, (Map)null, false, false, var5);
        modifyTaskAssignee(var0, var6, var3);
        return var6;
    }

    public static Dataobject addTaskAssigneeByName(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        ContextUtil.startTransaction(var0, true);

        try {
            ArrayList var4 = new ArrayList(1);
            Select var5 = new Select("physicalid", "physicalid", ExpressionType.BUS, (Format)null, false);
            var4.add(var5);
            Datacollection var6 = Task.getPersonInfo(var0, var2, var4, 1);
            if (var6.getDataobjects().isEmpty()) {
                String var11 = PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.AssigneeInvalid", var0.getLocale());
                var11 = var11.replace("%ASSIGNEE%", var2);
                throw new FoundationUserException(var11);
            } else {
                Dataobject var7 = (Dataobject)var6.getDataobjects().get(0);
                String var8 = var7.getId();
                String var9 = addTaskAssignee(var0, var1, var8, var3);
                var7.setRelId(var9);
                ContextUtil.commitTransaction(var0);
                return var7;
            }
        } catch (FoundationException var10) {
            ContextUtil.abortTransaction(var0);
            throw var10;
        }
    }

    public static void modifyTaskAssignee(Context var0, String var1, Map<String, String> var2) throws FoundationException {
        ObjectEditUtil.modifyConnection(var0, var1, var2, true);
    }

    public static String modifyTaskAssignee(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        String var4 = ObjectEditUtil.modifyConnection(var0, var2, "Assigned Tasks", var1, var3, true, "id");
        return var4;
    }

    public static void removeTaskAssignee(Context var0, String var1, String var2) throws FoundationException {
        removeTaskAssignee(var0, var1, var2, "Assigned Tasks");
    }

    public static void removeTaskAssignee(Context var0, String var1, String var2, String var3) throws FoundationException {
        boolean var4 = !ServiceSave.isObjectNew(var0, var1);
        ObjectEditUtil.disconnect(var0, var2, var3, var1, false, var4);
    }

    public static void removeTaskAssignee(Context var0, String var1) throws FoundationException {
        ArrayList var2 = new ArrayList(1);
        Select var3 = new Select("from.physicalid", "from.physicalid", ExpressionType.REL, (Format)null, false);
        Select var4 = new Select("to.physicalid", "to.physicalid", ExpressionType.REL, (Format)null, false);
        var2.add(var3);
        var2.add(var4);
        ObjectEditUtil.disconnect(var0, var1);
    }
}
