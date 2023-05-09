package com.gsc.apps.program;

import com.dassault_systemes.enovia.e6w.foundation.jaxb.FieldValue;
import com.dassault_systemes.enovia.e6w.foundation.jaxb.Status;
import com.matrixone.apps.common.Person;
import com.matrixone.apps.common.SubtaskRelationship;
import com.matrixone.apps.common.TaskHolder;
import com.matrixone.apps.common.util.ComponentsUtil;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.program.ProgramCentralConstants;
import com.matrixone.apps.program.ProgramCentralUtil;
import matrix.db.Context;
import matrix.db.JPO;
import matrix.util.Pattern;
import matrix.util.StringList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class Widgets {
   static final String SELECT_PROJECT_ID_FROM_TASK;
   static final String SELECT_PROJECT_NAME_FROM_TASK;
   static final String SELECT_PROJECT_STATE_FROM_TASK;

   public Widgets(Context var1, String[] var2) throws Exception {
   }

   public static MapList getUserProjects(Context context, String[] args) throws Exception {
      Map map = (Map)JPO.unpackArgs(args);
      StringList busSelects = (StringList)map.get("JPO_BUS_SELECTS");
      StringList relSelects = (StringList)map.get("JPO_REL_SELECTS");
      busSelects.addElement(com.matrixone.apps.common.Task.SELECT_TASK_ESTIMATED_FINISH_DATE);
      busSelects.addElement(com.matrixone.apps.common.Task.SELECT_TASK_ESTIMATED_DURATION);
      busSelects.addElement("current");
      String whereClause = "current != " + ProgramCentralConstants.STATE_PROJECT_SPACE_COMPLETE;
      whereClause = whereClause + " && current != " + ProgramCentralConstants.STATE_PROJECT_SPACE_ARCHIVE;
      whereClause = whereClause + " && current != " + ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_HOLD;
      whereClause = whereClause + " && current != " + ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_CANCEL;
      whereClause = whereClause + " && type != " + ProgramCentralConstants.TYPE_EXPERIMENT;
      whereClause = whereClause + " && type != '" + ProgramCentralConstants.TYPE_PROJECT_BASELINE + "'";
      Object var6 = null;
      Person var7 = Person.getPerson(context);
      MapList mapList = var7.getRelatedObjects(context, DomainConstants.RELATIONSHIP_MEMBER, DomainConstants.TYPE_PROJECT_MANAGEMENT, busSelects, (StringList)null, true, false, (short)1, whereClause, (String)var6, 0);
      return mapList;
   }

   public static MapList getUserTasks(Context var0, String[] var1) throws Exception {
      try {
         ComponentsUtil.checkLicenseReserved(var0, ProgramCentralConstants.PGE_LICENSE_ARRAY);
      } catch (Exception var8) {
         return new MapList();
      }

      Map var2 = (Map)JPO.unpackArgs(var1);
      StringList var3 = (StringList)var2.get("JPO_BUS_SELECTS");
      var3.addElement(com.matrixone.apps.common.Task.SELECT_TASK_ESTIMATED_FINISH_DATE);
      var3.addElement(com.matrixone.apps.common.Task.SELECT_TASK_ESTIMATED_DURATION);
      var3.addElement("current");
      Person var4 = Person.getPerson(var0);
      String var5 = "current != Complete";
      var5 = var5 + " && " + ProgramCentralConstants.SELECT_PROJECT_TYPE + ".kindof[" + ProgramCentralConstants.TYPE_EXPERIMENT + "] != TRUE";
      var5 = var5 + " && " + ProgramCentralConstants.SELECT_PROJECT_STATE + " != " + ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_HOLD;
      var5 = var5 + " && " + ProgramCentralConstants.SELECT_PROJECT_STATE + " != " + ProgramCentralConstants.STATE_PROJECT_SPACE_HOLD_CANCEL_CANCEL;
      String var6 = "to[" + DomainConstants.RELATIONSHIP_PROJECT_ACCESS_KEY + "]==True";
      if (ProgramCentralUtil.isNotNullString(var5)) {
         var5 = var5 + "  && " + var6;
      } else {
         var5 = "  && " + var6;
      }

      MapList var7 = var4.getAssignments(var0, var3, var5);
      return var7;
   }

   public static MapList getProjectPhase(Context var0, String[] var1) throws Exception {
      Map var2 = (Map)JPO.unpackArgs(var1);
      String var3 = (String)var2.get("JPO_WIDGET_FIELD_KEY");
      MapList var4 = (MapList)var2.get("JPO_WIDGET_DATA");

      for(int var5 = 0; var5 < var4.size(); ++var5) {
         Map var6 = (Map)var4.get(var5);
         String var7 = (String)var6.get("id");
         Map var8 = getProjectPhase(var0, var7);
         if (var8 != null) {
            var6.put("project_id", var7);
            var6.put(var3, var8.get("name"));
            var6.put("prg_phase_info", var8);
         }
      }

      return var4;
   }

   public static MapList getProjectPhaseActiveTaskCount(Context var0, String[] var1) throws Exception {
      Map var2 = (Map)JPO.unpackArgs(var1);
      String var3 = (String)var2.get("JPO_WIDGET_FIELD_KEY");
      MapList var4 = (MapList)var2.get("JPO_WIDGET_DATA");
      MapList var5 = getMyTasksInfo(var0);

      for(int var6 = 0; var6 < var4.size(); ++var6) {
         Map var7 = (Map)var4.get(var6);
         String var8 = (String)var7.get("id");
         int var9 = getTasksInProject(var5, var8);
         var7.put(var3, String.valueOf(var9));
      }

      return var4;
   }

   public static MapList getProjectProgressStatus(Context var0, String[] var1) throws Exception {
      Map var2 = (Map)JPO.unpackArgs(var1);
      String var3 = (String)var2.get("JPO_WIDGET_FIELD_KEY");
      MapList var4 = (MapList)var2.get("JPO_WIDGET_DATA");

      for(int var5 = 0; var5 < var4.size(); ++var5) {
         Map var6 = (Map)var4.get(var5);
         getProgressStatus(var0, var6, var3);
      }

      return var4;
   }

   public static MapList getTaskProgressStatus(Context var0, String[] var1) throws Exception {
      Map var2 = (Map)JPO.unpackArgs(var1);
      String var3 = (String)var2.get("JPO_WIDGET_FIELD_KEY");
      MapList var4 = (MapList)var2.get("JPO_WIDGET_DATA");

      for(int var5 = 0; var5 < var4.size(); ++var5) {
         Map var6 = (Map)var4.get(var5);
         getProgressStatus(var0, var6, var3);
      }

      return var4;
   }

   public static void getProgressStatus(Context var0, Map var1, String var2) throws Exception {
      SimpleDateFormat var3 = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
      Date var4 = new Date();
      Date var5 = var3.parse((String)var1.get(com.matrixone.apps.common.Task.SELECT_TASK_ESTIMATED_FINISH_DATE));
      String var6 = (String)var1.get("current");
      String var7 = (String)var1.get(com.matrixone.apps.common.Task.SELECT_TASK_ESTIMATED_DURATION);
      FieldValue var8 = new FieldValue();
      var8.setValue((String)var1.get(var2));
      var1.put(var2, var8);
      if (var4.after(var5)) {
         var8.getStatus();
         var8.setStatus(Status.WARNING);
      } else {
         int var9 = Integer.parseInt(EnoviaResourceBundle.getProperty(var0, "eServiceApplicationProgramCentral.SlipThresholdYellowRed"));
         long var10 = DateUtil.computeDuration(var4, var5);
         if (var10 <= (long)var9 && com.matrixone.apps.common.Task.parseToDouble(var7) > (double)var10) {
            var8.getStatus();
            var8.setStatus(Status.INFO);
         }
      }

   }

   private static Map getProjectPhase(Context var0, String var1) throws Exception {
      com.matrixone.apps.common.Task var2 = new com.matrixone.apps.common.Task(var1);
      byte var3 = 1;
      StringList var4 = new StringList();
      Object var5 = null;
      var4.addElement("id");
      var4.addElement("name");
      String var6 = com.matrixone.apps.common.Task.SELECT_TASK_ESTIMATED_DURATION + " != 0 && " + "current" + " != 'Complete'";
      Map var7 = null;
      MapList var8 = getTasks(var0, var2, var3, var4, (StringList)var5, var6);
      if (var8.size() > 0) {
         var7 = (Map)var8.get(0);
      }

      return var7;
   }

   private static MapList getTasks(Context var0, TaskHolder var1, int var2, StringList var3, StringList var4, String var5) throws Exception {
      if (var4 == null) {
         var4 = new StringList(1);
      }

      var4.addElement(SubtaskRelationship.SELECT_SEQUENCE_ORDER);
      String var6 = "*";
      String var7 = com.matrixone.apps.common.Task.RELATIONSHIP_SUBTASK;
      boolean var8 = false;
      boolean var9 = true;
      Object var10 = null;
      byte var11 = 0;
      Object var12 = null;
      Object var13 = null;
      Object var14 = null;
      String var15 = "to[" + DomainConstants.RELATIONSHIP_PROJECT_ACCESS_KEY + "]==True";
      if (ProgramCentralUtil.isNotNullString(var5)) {
         var5 = var5 + "  && " + var15;
      } else {
         var5 = var15;
      }

      MapList var16 = ((DomainObject)var1).getRelatedObjects(var0, var7, var6, var3, var4, var8, var9, (short)var2, var5, (String)var10, var11, (Pattern)var12, (Pattern)var13, (Map)var14);
      if (var2 >= 0) {
         var16.sortStructure(SubtaskRelationship.SELECT_SEQUENCE_ORDER, "ascending", "integer");
      }

      return var16;
   }

   private static MapList getMyTasksInfo(Context var0) throws FrameworkException {
      StringList var1 = new StringList(6);
      var1.add(SELECT_PROJECT_ID_FROM_TASK);
      var1.add(SELECT_PROJECT_NAME_FROM_TASK);
      var1.add(SELECT_PROJECT_STATE_FROM_TASK);
      var1.add("id");
      var1.add("name");
      var1.add("current");
      DomainObject var2 = PersonUtil.getPersonObject(var0);
      StringList var3 = var2.getInfoList(var0, "from[" + ProgramCentralConstants.RELATIONSHIP_ASSIGNED_TASKS + "].to.id");
      Object[] var4 = var3.toArray();
      String[] var5 = new String[var4.length];
      int var6 = 0;
      Object[] var7 = var4;
      int var8 = var4.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         Object var10 = var7[var9];
         var5[var6] = var10.toString();
         ++var6;
      }

      MapList var11 = DomainObject.getInfo(var0, var5, var1);
      return var11;
   }

   private static int getTasksInProject(MapList var0, String var1) {
      int var2 = 0;
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         Map var5 = (Map)var4;
         String var6 = var5.get("current").toString();
         String var7 = var5.get(SELECT_PROJECT_ID_FROM_TASK).toString();
         if (var7.equals(var1) && !var6.equals("Complete") && !var6.equals("Create")) {
            ++var2;
         }
      }

      return var2;
   }

   static {
      SELECT_PROJECT_ID_FROM_TASK = "to[" + DomainConstants.RELATIONSHIP_PROJECT_ACCESS_KEY + "].from.from[" + DomainConstants.RELATIONSHIP_PROJECT_ACCESS_LIST + "].to.id";
      SELECT_PROJECT_NAME_FROM_TASK = "to[" + DomainConstants.RELATIONSHIP_PROJECT_ACCESS_KEY + "].from.from[" + DomainConstants.RELATIONSHIP_PROJECT_ACCESS_LIST + "].to.name";
      SELECT_PROJECT_STATE_FROM_TASK = "to[" + DomainConstants.RELATIONSHIP_PROJECT_ACCESS_KEY + "].from.from[" + DomainConstants.RELATIONSHIP_PROJECT_ACCESS_LIST + "].to.current";
   }
}
