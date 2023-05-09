package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.dpm.ServiceUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.MqlUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollection;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Dataobject;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.QueryData;
import com.matrixone.apps.common.Company;
import com.matrixone.apps.common.Person;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.FrameworkUtil;
import matrix.db.Context;
import matrix.util.StringList;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Program extends com.gsc.apps.program.Program {
   private static final String TAG = ">>> PROGRAM (OBJ):  ";
   static final String RELATIONSHIP_PROGRAM_PROJECT = "Program Project";
   static final String POLICY_PROGRAM = "policy_Program";

   private static StringList _getProgramStates() {
      StringList var0 = new StringList(2);
      var0.add(STATE_PROGRAM_ACTIVE);
      var0.add(STATE_PROGRAM_INACTIVE);
      return var0;
   }

   public String createProgram(Context context, String name) throws FoundationException {
      long var3 = System.currentTimeMillis();
      String var5 = "";
      String var6 = null;

      try {
         Company company = Person.getPerson(context).getCompany(context);
         StringList var8 = new StringList();
         var8.add("physicalid");
         Map var9 = company.getInfo(context, var8);
         String companyPhysicalId = (String)var9.get("physicalid");
         String mqlStr = " temp query bus $1 $2 $3 select $4 dump $5";
         String results = MqlUtil.mqlCommand(context, mqlStr, new String[]{TYPE_PROGRAM, name, companyPhysicalId, "physicalid", "|"});
         var5 = String.format("%s%s (%s) query ...\t", ">>> PROGRAM (OBJ):  ", "createProgram", name);
         FoundationUtil.debug(var5, var3);
         if (results != null && !results.isEmpty()) {
            var6 = (String)FrameworkUtil.split(results, "|").get(3);
         } else {
            this.createObject(context, TYPE_PROGRAM, name, companyPhysicalId, getDefaultPolicy(context), context.getVault().getName());
            var5 = String.format("%s%s (%s) create ...\t", ">>> PROGRAM (OBJ):  ", "createProgram", name);
            FoundationUtil.debug(var5, var3);
            var6 = this.getInfo(context, "physicalid");
            DomainRelationship.connect(context, company, DomainConstants.RELATIONSHIP_COMPANY_PROGRAM, this);
         }

         var5 = String.format("%s%s (%s) done ...\t", ">>> PROGRAM (OBJ):  ", "createProgram", name);
         FoundationUtil.debug(var5, var3);
         return var6;
      } catch (Exception var13) {
         throw new FoundationException(var13);
      }
   }

   protected static Datacollection getUserPrograms(Context var0, Datacollection var1, boolean var2, List var3, String var4) throws FoundationException {
      boolean var5 = false;
      String var6 = PropertyUtil.getSchemaName(var0, "type_Program");
      if (var1 != null && !var1.getDataobjects().isEmpty()) {
         var5 = true;
      }

      if (var5 && (var3 == null || var3.isEmpty())) {
         throw new FoundationException("You must provide some selectables to Program.getUserPrograms");
      } else {
         Datacollection var7 = new Datacollection();
         if (var5) {
            try {
               ServiceUtil.getObjectInfo(var0, var1, var3);
               List var8 = var1.getDataobjects();
               Iterator var9 = var8.iterator();

               while(var9.hasNext()) {
                  Dataobject var10 = (Dataobject)var9.next();
                  if (var10.getId().isEmpty()) {
                     throw new FoundationException("DPM100: Invalid Program ID. Business Object Does Not Exist");
                  }

                  var7.getDataobjects().add(var10);
               }
            } catch (Exception var11) {
               if (var11.getMessage().contains("Business Object Does Not Exist")) {
                  throw new FoundationException("DPM100: Invalid Program ID. Business Object Does Not Exist");
               }

               throw var11;
            }
         } else {
            String var12 = "";
            if (var4 != null && !var4.isEmpty()) {
               var12 = gscProjectSpace.getStateWhereClause(var0, "", var4, _getProgramStates());
            } else {
               var12 = String.format("current == %s", STATE_PROGRAM_ACTIVE);
            }

            if (var2) {
               var12 = String.format("%s AND owner == %s", var12, var0.getUser());
            }

            QueryData var13 = new QueryData();
            var13.setTypePattern(var6);
            var13.setWhereExpression(var12);
            var13.setLimit(gscProjectSpace.MAX_OBJECTS);
            var7 = ObjectUtil.query(var0, var13, var3);
         }

         return var7;
      }
   }
}
