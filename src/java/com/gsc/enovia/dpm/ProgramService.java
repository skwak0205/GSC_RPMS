package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.dpm.ProjectSpace;
import com.dassault_systemes.enovia.dpm.ServiceUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.*;
import com.dassault_systemes.enovia.e6wv2.foundation.db.JPOUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectEditUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollection;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Dataobject;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.ServiceParameters;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.UpdateActions;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.ArgMap;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMapAdapter;
import com.matrixone.apps.domain.util.FrameworkUtil;
import matrix.db.Context;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramService implements ServiceConstants {
   private static final String TAG = ">>> PROGRAM (SVC):  ";
   protected static final String REGISTERED_SUITE = "ProgramCentral";

   public static Datacollection getPrograms(Context context, String[] args) throws FoundationException {
      ServiceParameters serviceParameters = (ServiceParameters)JPOUtil.unpackArgs(args);
      HttpServletRequest var3 = serviceParameters.getHttpRequest();
      long var4 = System.currentTimeMillis();
      com.dassault_systemes.enovia.dpm.ServiceUtil.checkLicenseDPM(context);
      Datacollection datacollection = serviceParameters.getDatacollection();
      List selects = serviceParameters.getSelects();
      ArgMap argMap = serviceParameters.getServiceArgs();
      String state = (String)argMap.get("state");
      String owned = (String)argMap.get("owned");
      String projectId = (String)argMap.get("projectId");
      boolean isOwned = false;
      if (owned != null && !owned.isEmpty()) {
         isOwned = Boolean.parseBoolean(owned);
      }

      Datacollection datacollection1;
      if (datacollection != null && !datacollection.getDataobjects().isEmpty()) {
         datacollection1 = Program.getUserPrograms(context, datacollection, false, selects, (String)null);
      } else if (projectId != null && !"".equals(projectId)) {
         datacollection1 = ProjectSpace.getProgramProjects(context, projectId, selects);
      } else {
         datacollection1 = Program.getUserPrograms(context, (Datacollection)null, isOwned, selects, state);
      }

      String var14 = String.format("%s%s (%d) ...\t", ">>> PROGRAM (SVC):  ", "getPrograms", datacollection1.getDataobjects().size());
      FoundationUtil.debug(var14, var4);
      return datacollection1;
   }

   private static String createProgram(Context context, String name, Map map) throws FoundationException {
      long var3 = System.currentTimeMillis();

      try {
         com.dassault_systemes.enovia.dpm.ServiceUtil.checkLicenseDPM(context);
         ObjectEditUtil.checkNameRules(context, name);
         Program var5 = new Program();
         String var6 = var5.createProgram(context, name);
         String var7 = String.format("%s%s (%s) ...\t", ">>> PROGRAM (SVC):  ", "Create Program", name);
         FoundationUtil.debug(var7, var3);
         if (var6 != null) {
            ObjectEditUtil.modify(context, var6, map, false);
            FoundationUtil.debug(">>> PROGRAM (SVC):  Modify Program (createProgram)...\t", var3);
         }

         return var6;
      } catch (Exception var8) {
         throw new FoundationException(var8);
      }
   }

   public static Dataobject updatePrograms(Context var0, String[] var1) throws Exception {
      ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
      Datacollection var3 = var2.getDatacollection();
      Dataobject var4 = (Dataobject)var3.getDataobjects().get(0);
      long var5 = System.currentTimeMillis();
      com.dassault_systemes.enovia.dpm.ServiceUtil.checkLicenseDPM(var0);
      UpdateActions var7 = var4.getUpdateAction();
      String name;
      if (!UpdateActions.CREATE.equals(var7) && !UpdateActions.MODIFY.equals(var7)) {
         if (UpdateActions.DELETE.equals(var7)) {
            deleteProgram(var0, var4.getId());
            var4 = null;
         }
      } else {
         name = DataelementMapAdapter.getDataelementValue(var4, "name");
         if (name != null) {
            name = name.trim();
            ObjectEditUtil.checkNameRules(var0, name);
         }

         HashMap var9 = new HashMap(5);
         ServiceDataFunctions.fillUpdates(var0, var4, var2.getAutosaveFields(), var9);
         if (UpdateActions.CREATE.equals(var7)) {
            if (name == null || name.isEmpty() || name.toLowerCase().contains("auto")) {
               name = FrameworkUtil.autoName(var0, "type_Program", (String)null, "policy_Program", (String)null, (String)null, true, true);
               com.gsc.enovia.dpm.ServiceUtil.addToMap(var9, "name", name);
            }

            DataelementMapAdapter.setDataelementValue(var4, "name", name);
            String var10 = createProgram(var0, name, var9);
            var4.setId(var10);
         } else {
            com.gsc.enovia.dpm.ServiceUtil.addToMap(var9, "name", name);
            modifyProgram(var0, var4.getId(), var9);
         }
      }

      name = String.format("%s%s (%s)...\t", ">>> PROGRAM (SVC):  ", "updatePrograms", var7);
      FoundationUtil.debug(name, var5);
      return var4;
   }

   private static void modifyProgram(Context var0, String var1, Map var2) throws FoundationException {
      long var3 = System.currentTimeMillis();
      if (var2 != null && !var2.isEmpty()) {
         ObjectEditUtil.modify(var0, var1, var2, false);
      }

      String var5 = String.format("%s%s ...\t", ">>> PROGRAM (SVC):  ", "modifyProgram");
      FoundationUtil.debug(var5, var3);
   }

   public static void deleteProgram(Context var0, String var1) throws FoundationException {
      long var2 = System.currentTimeMillis();

      try {
         com.matrixone.apps.program.Program var4 = new com.matrixone.apps.program.Program(var1);
         var4.delete(var0);
         String var5 = String.format("%s%s ...\t", ">>> PROGRAM (SVC):  ", "deleteProgram");
         FoundationUtil.debug(var5, var2);
      } catch (Exception var6) {
         throw new FoundationException(var6);
      }
   }

   public static Dataobject updateProgramProjects(Context var0, String[] var1) throws FoundationException {
      ServiceParameters var2 = (ServiceParameters)JPOUtil.unpackArgs(var1);
      Dataobject var3 = (Dataobject)var2.getDatacollection().getDataobjects().get(0);
      long var4 = System.currentTimeMillis();
      UpdateActions var6 = var3.getUpdateAction();
      Dataobject var7 = var3.getParent();
      String var8 = var3.getParent().getId();
      String var9 = var3.getRelId();
      String var10 = var3.getId();
      String var11;
      ArrayList var12;
      if (var10 == null) {
         var11 = DataelementMapAdapter.getDataelementValue(var3, "name");
         if (var11 != null && !var11.isEmpty()) {
            var12 = new ArrayList(1);
            var12.add(SELECTABLE_OBJECTID);
            Datacollection var13 = com.gsc.enovia.dpm.ServiceUtil.getProjectInfo(var0, var11, var12, 10);
            if (var13.getDataobjects().isEmpty()) {
               String var18 = PropertyUtil.getTranslatedValue(var0, "ProgramCentral", "emxProjectService.Error.ProgramProjectInvalid", var0.getLocale());
               throw new FoundationUserException(var18);
            }

            Dataobject var14 = (Dataobject)var13.getDataobjects().get(0);
            var10 = var14.getId();
            var3.setId(var10);
         }
      }

      if (UpdateActions.CONNECT.equals(var6) || UpdateActions.CREATE.equals(var6) || UpdateActions.MODIFY.equals(var6)) {
         HashMap var15 = new HashMap(1);
         String var16;
         if (UpdateActions.MODIFY.equals(var6)) {
            var12 = null;
            if (var9 != null) {
               var16 = ObjectEditUtil.modifyConnection(var0, var9, var15, true, "id");
            } else {
               var16 = com.dassault_systemes.enovia.dpm.ServiceUtil.modifyConnection(var0, var8, var10, var15, "Program Project");
            }

            var3.setRelId(var16);
         } else {
            var16 = ServiceUtil.addConnection(var0, var8, var10, var15, "Program Project");
            var3.setRelId(var16);
         }

         var16 = (String)var2.getServiceArgs().get("$fields");
         if (var16 != null) {
            var16 = var16.replaceAll("projects.", "pgm.projects.");
            ArgMap var17 = var2.getServiceArgs();
            var17.put("$fields", var16);
            var2.setServiceArgs(var17);
         }

         ServiceSave.getUpdatedObjects(var0, var2.getServiceReferenceName(), var2, var16);
         var3 = (Dataobject)var2.getDatacollection().getDataobjects().get(0);
      }

      if (UpdateActions.DISCONNECT.equals(var6) || UpdateActions.DELETE.equals(var6)) {
         if (var9 != null) {
            ObjectEditUtil.disconnect(var0, var9);
         } else {
            ObjectEditUtil.disconnect(var0, var8, "Program Project", var10);
         }
      }

      var11 = String.format("%s%s (%s) ...\t", ">>> PROGRAM (SVC):  ", "updateProgramProjects", var6);
      FoundationUtil.debug(var11, var4);
      return var3;
   }
}
