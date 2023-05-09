package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceConstants;
import com.dassault_systemes.enovia.e6wv2.foundation.db.JPOUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollection;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Dataobject;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Selectable;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.ServiceParameters;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.ArgMap;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMap;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMapAdapter;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.RelateddataMap;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.program.ProgramCentralConstants;
import com.matrixone.apps.program.ProgramCentralUtil;
import matrix.db.Context;
import matrix.util.StringList;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class gscDocumentService implements ServiceConstants {
    public static Datacollection getDocuments(Context context, String[] args) throws FoundationException {
        ServiceParameters serviceParameters = (ServiceParameters) JPOUtil.unpackArgs(args);
        HttpServletRequest httpRequest = serviceParameters.getHttpRequest();
//        ServiceUtil.checkLicenseProject(context, httpRequest);
        Datacollection datacollection = serviceParameters.getDatacollection();
        List<Selectable> parametersSelects = serviceParameters.getSelects();
        ArgMap serviceArgs = serviceParameters.getServiceArgs();
        String state = (String)serviceArgs.get("state");
        String owned = (String)serviceArgs.get("owned");
        String structured = (String)serviceArgs.get("structured");
        String programId = (String)serviceArgs.get("programId");
        String excludeSubtypes = (String)serviceArgs.get("excludeSubtypes");
        Datacollection datacollection1 = datacollection;

        boolean isOwned = false;
        boolean isStructured = false;
        if (owned != null && !owned.isEmpty()) {
            isOwned = Boolean.parseBoolean(owned);
        }

        if (structured != null && !structured.isEmpty()) {
            isStructured = Boolean.parseBoolean(structured);
        }

        String mode;
        if (serviceParameters.getServiceArgs().containsKey("mode")) {
            mode = (String)serviceParameters.getServiceArgs().get("mode");
            if ("getUpdatedData".equalsIgnoreCase(mode)) {
                String title = (String)serviceParameters.getServiceArgs().get("title");
                List<Dataobject> dataobjectList = datacollection.getDataobjects();
                Dataobject dataobject = dataobjectList.get(0);
                String dataobjectId = dataobject.getId();
                Map getupdatedData = com.dassault_systemes.enovia.dpm.ProjectSpace.getupdatedData(context, dataobjectId, title);
                DataelementMap dataelementMap = dataobject.getDataelements();
                if (dataelementMap == null) {
                    dataelementMap = new DataelementMap();
                }

                if (getupdatedData.containsKey("physicalId")) {
                    dataelementMap.put("physicalId", (String)getupdatedData.get("physicalId"));
                } else if (getupdatedData.containsKey("Error")) {
                    dataelementMap.put("Error", getupdatedData.get("Error"));
                }

                dataobject.setDataelements(dataelementMap);
                return datacollection;
            }
        } else if (serviceParameters.getServiceArgs().containsKey("query")) {
            mode = (String)serviceParameters.getServiceArgs().get("query");
            Datacollection var23 = loadResources(context, mode);
            RelateddataMap var24 = new RelateddataMap();
            var24.put("Resources", (ArrayList<Dataobject>)var23.getDataobjects());
            List<Dataobject> var25 = datacollection.getDataobjects();
            Dataobject var26 = var25.get(0);
            var26.setRelateddata(var24);
        } else if (datacollection != null && !datacollection.getDataobjects().isEmpty()) {
            datacollection1 = gscDocument.getUserDocuments(context, datacollection, false, isStructured, parametersSelects, (String)null);
        } else if (programId != null && !"".equals(programId)) {
            datacollection1 = gscProjectSpace.getProgramProjects(context, programId, parametersSelects);
        } else {
            datacollection1 = gscDocument.getUserDocuments(context, (Datacollection)null, isOwned, isStructured, parametersSelects, state, excludeSubtypes);
        }

        return datacollection1;
    }
    private static Datacollection loadResources(Context var0, String var1) {
        System.out.println("=============Load Resource : START===============");
        Datacollection var2 = new Datacollection();

        try {
            String var3 = "";
            String var4 = "";
            String var5 = "";
            String var6 = "";
            StringList var7 = new StringList();
            var7.add("physicalid");
            var7.add("name");
            var7.add(ProgramCentralConstants.SELECT_ATTRIBUTE_FIRSTNAME);
            var7.add(ProgramCentralConstants.SELECT_ATTRIBUTE_LASTNAME);
            StringBuffer var8 = new StringBuffer();
            StringList var9 = tokenizeResourceName(var1);
            int var10 = var9.size();
            if (var10 == 1) {
                var3 = var4 = var6 = (String)var9.get(0);
                var8.append("(");
                var8.append("name");
                var8.append(" ~~ '");
                var8.append(var3);
                var8.append("*'");
                var8.append(" || ");
                var8.append(ProgramCentralConstants.SELECT_ATTRIBUTE_FIRSTNAME);
                var8.append(" ~~ '");
                var8.append(var4);
                var8.append("*'");
                var8.append(" || ");
                var8.append(ProgramCentralConstants.SELECT_ATTRIBUTE_LASTNAME);
                var8.append(" ~~ '");
                var8.append(var6);
                var8.append("*'");
                var8.append(") && ");
                var8.append("current");
                var8.append(" !~~ '");
                var8.append("Inactive'");
            } else if (var10 == 2) {
                var4 = (String)var9.get(0);
                var6 = (String)var9.get(1);
                var8.append("(");
                var8.append(ProgramCentralConstants.SELECT_ATTRIBUTE_FIRSTNAME);
                var8.append(" ~~ '");
                var8.append(var4);
                var8.append("'");
                var8.append(" && ");
                var8.append(ProgramCentralConstants.SELECT_ATTRIBUTE_LASTNAME);
                var8.append(" ~~ '");
                var8.append(var6);
                var8.append("*'");
                var8.append(") && ");
                var8.append("current");
                var8.append(" !~~ '");
                var8.append("Inactive'");
            }

            MapList var11 = DomainObject.findObjects(var0, ProgramCentralConstants.TYPE_PERSON, (String)null, "-", (String)null, "*", var8.toString(), (String)null, true, var7, (short)0, (String)null, (String)null, new StringList());
            Iterator var12 = var11.iterator();

            while(var12.hasNext()) {
                Map var13 = (Map)var12.next();
                String var14 = (String)var13.get(DomainConstants.SELECT_ATTRIBUTE_FIRSTNAME);
                String var15 = (String)var13.get(DomainConstants.SELECT_ATTRIBUTE_LASTNAME);
                String var16 = (String)var13.get("physicalid");
                String var17 = (String)var13.get("name");
                Dataobject var18 = new Dataobject();
                var18.setId(var16);
                DataelementMapAdapter.setDataelementValue(var18, "Id", var16);
                DataelementMapAdapter.setDataelementValue(var18, "Name", var14 + ' ' + var15);
                DataelementMapAdapter.setDataelementValue(var18, "Username", var17);
                DataelementMapAdapter.setDataelementValue(var18, "icon", getSwymURL(var0, var17));
                var2.getDataobjects().add(var18);
            }

            System.out.println("=============Load Resource : END===============");
            System.out.println("Added res::" + var2.getDataobjects().size());
            return var2;
        } catch (FrameworkException var19) {
            var19.printStackTrace();
            return var2;
        }
    }
    private static String getSwymURL(Context var0, String var1) throws FrameworkException {
        String var2 = "";
        String var3 = var0.getTenant().toLowerCase();
        if (var1 != null && !var1.isEmpty()) {
            String var4 = "";
            String var5 = com.matrixone.apps.domain.util.PropertyUtil.getEnvironmentProperty(var0, "SWYM_URL");
            if (ProgramCentralUtil.isNotNullString(var5)) {
                var4 = var3 + var5;
                if (!var4.startsWith("https://")) {
                    var4 = "https://" + var4;
                }

                if (!var4.endsWith("/")) {
                    var4 = var4 + "/";
                }

                String var6 = "/api/user/getpicture/login/";
                String var7 = "/format/mini";
                var2 = var4 + var6 + var1 + var7;
            } else {
                var2 = "../../common/images/iconSmallPerson.png";
            }
        }

        return var2;
    }
    private static StringList tokenizeResourceName(String var0) {
        new StringList();
        var0 = var0.trim();
        StringList var1 = FrameworkUtil.split(var0, " ");
        return var1;
    }

}
