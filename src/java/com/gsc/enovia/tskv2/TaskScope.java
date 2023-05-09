//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gsc.enovia.tskv2;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationUserException;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceConstants;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ContextUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectEditUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMapAdapter;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.Select;
import com.matrixone.apps.domain.util.EnoviaResourceBundle;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.eMatrixDateFormat;
import matrix.db.Context;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

public class TaskScope implements ServiceConstants {
    public static final String SELECT_TASK_ID = "from.id";
    public static final String SELECT_SCOPE_ID = "to.id";
    protected static final String RELATIONSHIP_CONTRIBUTES_TO = "Contributes To";
    protected static final String RELATIONSHIP_SUBTASK = "Subtask";
    public static final String RELATIONSHIP_PROJECT_ACCESS_LIST = "Project Access List";
    public static final String RELATIONSHIP_PROJECT_ACCESS_KEY = "Project Access Key";
    public static final String TYPE_PROJECT_SPACE = "Project Space";
    public static final String POLICY_PROJECT_SPACE = "Project Space";
    public static final String TYPE_PROJECT_ACCESS_LIST = "Project Access List";
    public static final String POLICY_PROJECT_ACCESS_LIST = "Project Access List";
    public static final String SELECT_PROJECT_ACCESS_LIST_REL_ID = "to[Project Access List].physicalid";
    public static final String SELECT_PROJECT_ACCESS_LIST_ID = "to[Project Access List].from.physicalid";
    public static final String SELECT_PROJECT_ACCESS_KEY_ID = "to[Project Access Key].from.id";
    public static final String TYPE_PROJECT_BASELINE = "Project Baseline";
    public static final String TYPE_EXPERIMENT = "Experiment";
    public static final String TYPE_PROJECT_SNAPSHORT = "Project Snapshot";
    public static final String SELECT_IS_KINDOF_PROJECT_BASELINE = "type.kindof[Project Baseline]";
    public static final String SELECT_IS_KINDOF_EXPERIMENT = "type.kindof[Experiment]";
    public static final String SELECT_IS_KINDOF_PROJECT_SNAPSHORT = "type.kindof[Project Snapshot]";

    public TaskScope() {
    }

    public static Dataobject getTaskScopeRelInfo(Context var0, String var1, List<Selectable> var2) throws FoundationException {
        Dataobject var3 = new Dataobject();
        var3.setRelId(var1);
        Datacollection var4 = new Datacollection();
        var4.getDataobjects().add(var3);
        return ObjectUtil.print(var0, var3, (PrintData)null, var2, false);
    }

    public static Datacollection getTaskScopes(Context var0, String var1, List<Selectable> var2, List<Selectable> var3, String var4, String var5) throws FoundationException {
        Dataobject var6 = new Dataobject();
        var6.setId(var1);
        ExpandData var7 = new ExpandData();
        var7.setTypePattern("*");
        var7.setRelationshipPattern("Contributes To");
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

    public static Datacollection getScopeTasks(Context var0, String var1, List<Selectable> var2, List<Selectable> var3, String var4, String var5, String var6) throws FoundationException {
        return getScopeTasks(var0, var1, var2, var3, var4, var5, var6, (String)null);
    }

    public static Datacollection getScopeTasks(Context var0, String var1, List<Selectable> var2, List<Selectable> var3, String var4, String var5, String var6, String var7) throws FoundationException {
        String var8 = var4;
        if (var4 == null) {
            var8 = "";
        }

        if (!var8.isEmpty()) {
            var8 = var8 + " && ";
        }

        var8 = var8 + buildWhereClauseBasedOnFilter(var6);
        var8 = var8 + buildWhereClauseBasedOnCompletionDate(var7, !var8.isEmpty());
        var8 = var8 + buildWhereClauseBasedOnTaskExclusionList(var0, !var8.isEmpty());
        Dataobject var9 = new Dataobject();
        var9.setId(var1);
        ExpandData var10 = new ExpandData();
        var10.setTypePattern("Task");
        var10.setRelationshipPattern("Contributes To");
        var10.setGetFrom(true);
        var10.setObjectWhere(var8);
        var10.setRelationshipWhere(var5);
        ArrayList var11 = new ArrayList();
        var11.addAll(var2);
        var11.addAll(var3);
        return ObjectUtil.expand(var0, var9, var10, var11);
    }

    public static Datacollection getScopeTasks(Context var0, String var1, List<Selectable> var2, String var3) throws FoundationException {
        return getScopeTasks(var0, var1, var2, new ArrayList(), (String)null, (String)null, var3, (String)null);
    }

    public static Datacollection getScopeTasks(Context var0, String var1, List<Selectable> var2, String var3, String var4) throws FoundationException {
        return getScopeTasks(var0, var1, var2, new ArrayList(), (String)null, (String)null, var3, var4);
    }

    public static Datacollection getProjectScopeTasks(Context var0, String var1, List<Selectable> var2, String var3) throws FoundationException {
        return getProjectScopeTasks(var0, var1, var2, var3, (String)null);
    }

    public static Datacollection getProjectScopeTasks(Context var0, String var1, List<Selectable> var2, String var3, String var4) throws FoundationException {
        Task.checkLicenseCollaborativeTasks(var0);
        Task.checkLicenseProjectTasks(var0, (HttpServletRequest)null);
        String var5 = buildWhereClauseBasedOnFilter(var3);
        var5 = var5 + buildWhereClauseBasedOnCompletionDate(var4, !var5.isEmpty());
        var5 = var5 + buildWhereClauseBasedOnTaskExclusionList(var0, !var5.isEmpty());
        Dataobject var6 = new Dataobject();
        var6.setId(var1);
        ExpandData var7 = new ExpandData();
        var7.setTypePattern("Task,Milestone,Gate,Phase");
        var7.setRelationshipPattern("Subtask");
        var7.setLimit(Short.valueOf((short)0));
        var7.setRecurseToLevel(BigDecimal.valueOf(-1L));
        var7.setGetFrom(true);
        var7.setObjectWhere(var5);
        ArrayList var8 = new ArrayList();
        var8.addAll(var2);
        return ObjectUtil.expand(var0, var6, var7, var8);
    }

    private static String buildWhereClauseBasedOnCompletionDate(String var0, boolean var1) {
        String var2 = "";
        if (var0 != null && !var0.isEmpty()) {
            if (var1) {
                var2 = var2 + " && ";
            }

            var2 = var2 + "(attribute[Task Actual Finish Date]=='' || attribute[Task Actual Finish Date]>='" + var0 + "' )";
        }

        return var2;
    }

    private static String buildWhereClauseBasedOnFilter(String var0) {
        boolean var1 = true;
        boolean var2 = true;
        String var3 = "";
        if ("all".equals(var0)) {
            return var3;
        } else {
            if ("owned".equals(var0)) {
                var2 = false;
            } else if ("assigned".equals(var0)) {
                var1 = false;
            }

            if (var1) {
                var3 = var3 + "owner == \"context.user\"";
            }

            if (var2) {
                if (!var3.isEmpty()) {
                    var3 = var3 + " || ";
                }

                var3 = var3 + "(current != \"Create\" && to[Assigned Tasks].from.name == \"context.user\")";
            }

            return var3;
        }
    }

    private static String buildWhereClauseBasedOnTaskExclusionList(Context var0, boolean var1) {
        String var2 = "";
        String var3 = "";

        try {
            var3 = EnoviaResourceBundle.getProperty(var0, "emxCollaborativeTasks.TaskExclusionList");
        } catch (FrameworkException var5) {
        }

        if (var3 != null && !"".equals(var3)) {
            if (var1) {
                var2 = var2 + " && ";
            }

            var2 = var2 + "!(type matchlist '" + var3 + "', ',')";
        }

        return var2;
    }

    public static String addTaskToProjectScope(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        try {
            Task.checkLicenseProjectTasks(var0, (HttpServletRequest)null);
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
            HashMap var10 = new HashMap(5);
            if (var3 != null) {
                var10.putAll(var3);
            }

            ArrayList var11 = new ArrayList(1);
            var11.add(SELECT_CURRENT);
            Dataobject var12 = Task.getTaskInfo(var0, var1, var11);
            String var13 = DataelementMapAdapter.getDataelementValue(var12, SELECT_CURRENT.getName());
            if (var13.equals("Review") || var13.equals("Complete")) {
                ContextUtil.setGlobalRPEValue(var0, "State", var13);
                var10.put("Percent Complete", "100");
            }

            var10.put("Task Estimated Start Date", var8);
            var10.put("Task Estimated Finish Date", var9);
            ArrayList var14 = new ArrayList(3);
            var14.add(new Select("id", "id", ExpressionType.BUS, (Format)null, false));
            var14.add(new Select("to[Project Access List].from.physicalid", "to[Project Access List].from.physicalid", ExpressionType.BUS, (Format)null, false));
            var14.add(new Select("to[Project Access List].physicalid", "to[Project Access List].physicalid", ExpressionType.BUS, (Format)null, false));
            var14.add(new Select("type.kindof[Project Baseline]", "type.kindof[Project Baseline]", ExpressionType.BUS, (Format)null, false));
            var14.add(new Select("type.kindof[Experiment]", "type.kindof[Experiment]", ExpressionType.BUS, (Format)null, false));
            var14.add(new Select("type.kindof[Project Snapshot]", "type.kindof[Project Snapshot]", ExpressionType.BUS, (Format)null, false));
            Dataobject var15 = ObjectUtil.print(var0, var2, (PrintData)null, var14);
            String var16 = DataelementMapAdapter.getDataelementValue(var15, "to[Project Access List].from.physicalid");
            String var17 = DataelementMapAdapter.getDataelementValue(var15, "id");
            String var18 = DataelementMapAdapter.getDataelementValue(var15, "to[Project Access List].physicalid");
            boolean var19 = Boolean.valueOf(DataelementMapAdapter.getDataelementValue(var15, "type.kindof[Project Baseline]")) || Boolean.valueOf(DataelementMapAdapter.getDataelementValue(var15, "type.kindof[Experiment]")) || Boolean.valueOf(DataelementMapAdapter.getDataelementValue(var15, "type.kindof[Project Snapshot]"));
            String var20;
            if (var19) {
                var20 = PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.ForbiddenOperation", var0.getLocale());
                throw new FoundationUserException(var20);
            } else {
                Task.modifyTask(var0, var1, var10, false, false);
                ObjectEditUtil.connect(var0, var16, "Project Access Key", var1, (Map)null, false);
                var20 = ObjectUtil.getOIDforPhysicalId(var0, var1);
                String[] var21 = new String[]{var20};
                com.matrixone.apps.common.Task var22 = new com.matrixone.apps.common.Task(var17);
                var22.addExisting(var0, var21, (String)null);
                return var18;
            }
        } catch (Exception var23) {
            throw new FoundationException(var23);
        }
    }

    public static String addTaskScope(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        String var4 = ObjectEditUtil.connect(var0, var2, "Contributes To", var1, (Map)null, false);
        modifyTaskScope(var0, var4, var3);
        return var4;
    }

    public static Dataobject addTaskScopeByName(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        ContextUtil.startTransaction(var0, true);

        try {
            ArrayList var4 = new ArrayList(1);
            Select var5 = new Select("physicalid", "physicalid", ExpressionType.BUS, (Format)null, false);
            var4.add(var5);
            QueryData var6 = new QueryData();
            var6.setNamePattern(var2);
            var6.setExpandType(true);
            var6.setLimit(BigInteger.ONE);
            Datacollection var7 = ObjectUtil.query(var0, var6, var4);
            if (var7.getDataobjects().isEmpty()) {
                String var12 = PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Error.ScopeInvalid", var0.getLocale());
                var12 = var12.replace("%SCOPE%", var2);
                throw new FoundationUserException(var12);
            } else {
                Dataobject var8 = (Dataobject)var7.getDataobjects().get(0);
                String var9 = var8.getId();
                String var10 = addTaskScope(var0, var1, var9, var3);
                var8.setRelId(var10);
                ContextUtil.commitTransaction(var0);
                return var8;
            }
        } catch (FoundationException var11) {
            ContextUtil.abortTransaction(var0);
            throw var11;
        }
    }

    public static void modifyTaskScope(Context var0, String var1, Map<String, String> var2) throws FoundationException {
        ObjectEditUtil.modifyConnection(var0, var1, var2, true);
    }

    public static void removeTaskScope(Context var0, String var1, String var2) throws FoundationException {
        ObjectEditUtil.disconnect(var0, var2, "Contributes To", var1);
    }

    public static void removeTaskScope(Context var0, String var1) throws FoundationException {
        ObjectEditUtil.disconnect(var0, var1);
    }
}
