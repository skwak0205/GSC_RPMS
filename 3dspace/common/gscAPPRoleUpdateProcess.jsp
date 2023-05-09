<%-----------------------------------------------------------------------------
* FILE    : gscAPPRoleUpdateProcess.jsp
* DESC    : APP Role Update Process
* VER.    : 1.0
* AUTHOR  : SeungYong,Lee
* PROJECT : HiMSEM Engine Project
*
* Copyright 2018 by ENOVIA All rights reserved.
* -----------------------------------------------------------------
*                           Revision history
* -----------------------------------------------------------------
*
* Since          Author         Description
* -----------   ------------   -----------------------------------
* 2020-09-04     SeungYong,Lee   최초 생성
------------------------------------------------------------------------------%>
<%@page import="com.gsc.apps.ecm.util.gscECMUtil"%>
<%@page import="com.gsc.apps.common.util.gscStringUtil"%>
<%
    response.setContentType("text/xml");
    response.setContentType("charset=UTF-8");
    response.setHeader("Content-Type", "text/xml");
    response.setHeader("Cache-Control", "no-cache");
    response.getWriter().write("<?xml version='1.0' encoding='UTF-8'?>");
%>
<%@ page import="com.matrixone.vplm.posws.stubsaccess.ClientWithoutWS"%>
<%@ page import="com.matrixone.vplm.posbusinessmodel.SecurityContext"%>
<%@ page import="com.matrixone.vplm.posmodel.license.LicenseInfo"%>
<%@ page import="com.matrixone.vplm.posmodel.license.LicenseUserAssignment"%>
<%@include file="../common/emxNavigatorNoDocTypeInclude.inc"%>
<%@include file="emxPLMOnlineAdminAttributesCalculation.jsp"%>
<%@ page import="com.dassault_systemes.vplmposadministrationui.uiutil.EncodeUtil"%>
<%@ page import="com.dassault_systemes.vplmposadministrationui.uiutil.AdminUtilities"%>
<%@include file="../common/emxPLMOnlineAdminAuthorize.jsp"%>
<%
    String responseUpdatePerson = "<root><updateResult>";
    String HostCompanyName      = getHostCompanyName(mainContext);
    String source               = (String) emxGetParameter(request, "Source");
    String street               = emxGetParameter(request, "Street");
    String city                 = emxGetParameter(request, "City");
    String postalCode           = emxGetParameter(request, "PostalCode");
    String country              = emxGetParameter(request, "Country");
    String state                = emxGetParameter(request, "State");
    String sPersonID            = emxGetParameter(request, "PLM_ExternalID");
    String Accreditation        = emxGetParameter(request, "Accreditation");
    String Alias                = emxGetParameter(request, "Alias");
    String Activate             = emxGetParameter(request, "Active");
    // JIC 2014:10:23 Added Contractor support
    String Contractor           = emxGetParameter(request, "Contractor");
    // JIC 2015:04:01 Added Casual Hour support
    String CasualHour           = emxGetParameter(request, "CasualHour");
    String fromUser             = emxGetParameter(request, "fromUser");
    String isLicense             = emxGetParameter(request, "isLicense");

    Map logMap = new HashMap();
    StringList slRemList = new StringList();
    StringList slAddList = new StringList();

    String MessageLic = "Rien";
    if (source == null)
        source = "";
    Map person      = new HashMap();
    Map contextadd1 = new HashMap();
    Map contextrem1 = new HashMap();
    Map ctx2Add     = new HashMap();
    Map ctx2Rem     = new HashMap();
    Map orga        = new HashMap();
    Map listorga    = new HashMap();

    Map update = new HashMap();
    update.put("method", "updatePerson");
    Boolean IsActive = false;
    if (Activate != null) {
        person.put("IsActive", Activate);
    }

    if (Contractor != null) {
        person.put("Contractor", Contractor);
    }

    if (CasualHour != null) {
        person.put("CasualHour", CasualHour);
    }

    if (street == null)
        street = "";

    /********************************************************
     * From User의 License 값을 가져와 To User에게 그대로 부여
     ********************************************************/
    if (isLicense.equals("true")) {
        //license S
        // JIC 15:04:02 Moved person update before licensing
        update.put("iPersonInfo", person);


        if (source.equals("UpdateXP")){
            String ActualLicences = emxGetParameter(request,"ActualLicences");
            String[] ActualLicencesTable = ActualLicences.split(",");
            StringList ActualLicencesStringList = new StringList();
            for (int h = 0 ; h < ActualLicencesTable.length ; h++){
                ActualLicencesStringList.addElement(ActualLicencesTable[h]);
            }

            try{
                LicenseUserAssignment lua = new LicenseUserAssignment(sPersonID);
                // retrieve licenses list from request parameters
                String Licences = emxGetParameter(request,"Licences");

                if(Licences != null){
                    if (Licences.equals("")){
                        lua.addLicenseParameterIfValid("","");
                    }else{
                        String sParamValue[] = Licences.split(",,");
                        for (int a = 0 ; a < sParamValue.length ; a++){
                            String sParamValueOneByOne[] =  sParamValue[a].split("!!");
                            // JIC 15:04:02 Removed Casual-specific code (both Full and Casual licenses are now managed by class LicenseUserAssignment)
                            lua.addLicenseParameterIfValid(sParamValueOneByOne[0],sParamValueOneByOne[1]);
                        }
                    }
                }

                // JIC 15:04:20 Removed explicit license removal

                //person licenses assignments modifications (add/remove)
                lua.update(context,myNLS);
                // JIC 15:04:03 Remove Casual licenses assignment
            }
            catch(Exception e){
                System.out.println();
                MessageLic = e.getMessage();
                if (MessageLic.contains("java.")){
                    String MessageJava = MessageLic.substring(MessageLic.indexOf("java."), MessageLic.length() );
                    MessageLic = MessageLic.substring(0, MessageLic.indexOf("java."));
                    if (MessageJava.contains("Message:")){
                        MessageJava = MessageJava.substring(MessageJava.indexOf("Message")+8, MessageJava.length());
                    }
                    MessageLic = MessageLic + MessageJava;
                    if (MessageLic.contains(".")){
                        MessageLic = MessageLic.substring(0,MessageLic.indexOf("."));
                    }
                }
            }
        }
        // JIC 15:04:23 Moved licensing after person update
        else if (source.equals("Admin")){
            String licences = emxGetParameter(request,"licences");
            String[] licencesTable = licences.split(",,");
            try{
                LicenseUserAssignment lua = new LicenseUserAssignment(sPersonID);
                if (!licences.equals("")){
                    // retrieve licenses list from request parameters
                    for ( int l = 0 ; l < licencesTable.length ; l++ ) {
                        String value = licencesTable[l];
                        String val = value.substring(4,value.length()-4);
                        lua.addLicenseParameterIfValid(value,val);
                    }
                } else {
                    lua.addLicenseParameterIfValid("","");
                }
                // person licenses assignments modifications (add/remove)
                lua.update(mainContext,myNLS);
            }catch(Exception e){
                MessageLic = e.getMessage();
                if (MessageLic.contains("java.")){
                    String MessageJava = MessageLic.substring(MessageLic.indexOf("java."), MessageLic.length() );
                    MessageLic = MessageLic.substring(0, MessageLic.indexOf("java."));
                    if (MessageJava.contains("Message:")){
                        MessageJava = MessageJava.substring(MessageJava.indexOf("Message")+8, MessageJava.length());
                    }
                    MessageLic = MessageLic + MessageJava;
                    if (MessageLic.contains(".")){
                        MessageLic = MessageLic.substring(0,MessageLic.indexOf("."));
                    }
                }
                responseUpdatePerson = responseUpdatePerson + 2 + "</updateResult><LicMessage>" + MessageLic + "</LicMessage></root>";
                response.getWriter().write(responseUpdatePerson);
                return;
            }
        }
        responseUpdatePerson = responseUpdatePerson + 0 + "</updateResult><LicMessage>" + MessageLic + "</LicMessage></root>";
        response.getWriter().write(responseUpdatePerson);
        return;
    }

    //[S] Add By SeungYong,Lee [Enovia Role 권한 추가 및 삭제]
    StringList slRoleList = PersonUtil.getSecurityContextsNames(context, sPersonID);
    String removeRole = emxGetParameter(request, "removeRole");
    String addRole    = emxGetParameter(request, "addRole");
    String[] addRoleList    = addRole.split("!!");
    String[] removeRoleList = removeRole.split("!!");

    /**************************************************
     * Copy Role 기능
     **************************************************/
    // Copy Role Start
    try {
        if (gscStringUtil.isNotEmpty(fromUser)) {
            StringList slFromUserRoleList = PersonUtil.getSecurityContextsNames(context, fromUser);
            StringList personNames = new StringList();
            personNames.add(sPersonID);
            SecurityContext secCtx = new SecurityContext();

            for (int i = 0; slRoleList.size() > i; i++) {
                String contextName = slRoleList.get(i);
                if (!slFromUserRoleList.contains(contextName)) {
                    secCtx = SecurityContext.getSecurityContext(mainContext, contextName);
                    secCtx.unassignPersons(mainContext, personNames);
                }
            }

            for(int j = 0; slFromUserRoleList.size() > j; j++) {
                String contextName = slFromUserRoleList.get(j);
                if (!slRoleList.contains(contextName)) {
                    secCtx = SecurityContext.getSecurityContext(mainContext, contextName);
                    secCtx.assignPersons(mainContext, personNames);
                }
            }

            String roleMql   = "list role $1 where $2 ";
            String mqlFromResult = MqlUtil.mqlCommand(context, roleMql, "gsc*", "person == '" + fromUser + "'");
            String mqlToResult = MqlUtil.mqlCommand(context, roleMql, "gsc*", "person == '" + sPersonID + "'");
            StringList slFromRole = FrameworkUtil.split(mqlFromResult, "\n");
            StringList slToRole   = FrameworkUtil.split(mqlToResult, "\n");

            StringBuffer sbRem = new StringBuffer();
            for (int i = 0, size = slToRole.size(); size > i; i++) {
                String strEnoRole = slToRole.get(i);
                if(!slFromRole.contains(strEnoRole))  {
                    sbRem.append("mod role ");
                    sbRem.append(strEnoRole);
                    sbRem.append(" remove assign person ");
                    sbRem.append(sPersonID);
                    sbRem.append(";");
                }
            }
            gscECMUtil.mqlMultipleCommand(context, sbRem.toString());

            StringBuffer sbAdd = new StringBuffer();
            for (int i = 0, size = slFromRole.size(); size > i; i++) {
                String strEnoRole = slFromRole.get(i);
                if (!slToRole.contains(strEnoRole)) {
                    sbAdd.append("mod role ");
                    sbAdd.append(strEnoRole);
                    sbAdd.append(" add assign person ");
                    sbAdd.append(sPersonID);
                    sbAdd.append(";");
                }
            }
            gscECMUtil.mqlMultipleCommand(context, sbAdd.toString());

            responseUpdatePerson = responseUpdatePerson + 0 + "</updateResult><LicMessage>" + MessageLic + "</LicMessage></root>";
            response.getWriter().write(responseUpdatePerson);
            return;
        }
    } catch (Exception e) {
        e.printStackTrace();
        responseUpdatePerson = responseUpdatePerson + 2 + "</updateResult><LicMessage>" + MessageLic + "</LicMessage></root>";
        response.getWriter().write(responseUpdatePerson);
        return;
    }
    // Copy Role End

    String roleMql   = "list role $1 where $2 ";
    String mqlResult = MqlUtil.mqlCommand(context, roleMql, "gsc*", "person == '" + sPersonID + "'");

    if (addRoleList.length > 0) {
        StringBuffer sbAdd = new StringBuffer();
        for (int i = 1, size = addRoleList.length; size > i; i++) {
            sbAdd.append("mod role ");
            sbAdd.append(addRoleList[i]);
            sbAdd.append(" add assign person ");
            sbAdd.append(sPersonID);
            sbAdd.append(";");
        }
        gscECMUtil.mqlMultipleCommand(context, sbAdd.toString());
    }

    if (removeRoleList.length > 0) {
        StringBuffer sbRem = new StringBuffer();
        for (int i = 1, size = removeRoleList.length; size > i; i++) {
            sbRem.append("mod role ");
            sbRem.append(removeRoleList[i]);
            sbRem.append(" remove assign person ");
            sbRem.append(sPersonID);
            sbRem.append(";");
        }
        gscECMUtil.mqlMultipleCommand(context, sbRem.toString());
    }



    logMap.put("userName", sPersonID);
    logMap.put("previous", slRoleList);
    logMap.put("previousEnovia", mqlResult);
    //[E] Add By SeungYong,Lee [Enovia Role 권한 추가 및 삭제]

    person.put("PLM_ExternalID", sPersonID);
    person.put("v_distinguished_name", Alias);
    person.put("postalCode", postalCode);
    person.put("state", state);
    person.put("Preferences", "");

    String userType = gscStringUtil.NVL(emxGetParameter(request, "userType"));
    if (userType.equals("")) {
        person.put("city", city);
        person.put("v_first_name", emxGetParameter(request, "V_first_name"));
        person.put("v_last_name", emxGetParameter(request, "V_last_name"));
        person.put("street", street);
        person.put("v_email", emxGetParameter(request, "V_email"));
        person.put("v_phone", emxGetParameter(request, "V_phone"));
        person.put("Work_Phone_Number", emxGetParameter(request, "Work_Phone_Number"));
        person.put("country", country);
    }

    String[] ctx2R = new String[0];
    int j = 0;
    int k = 0;

    // JIC 15:04:23 Split "Admin" case into 2 parts
    if (source.equals("Admin")) {
        person.put("Accreditation", Accreditation);

        String ctx2Adds = (String) emxGetParameter(request, "Ctx2Add");

        String[] ctxAddd = ctx2Adds.split(",");
        for (int i = 0; i < ctxAddd.length; i++) {
            contextadd1 = new HashMap();
            if (!ctxAddd[i].equals("")) {
                contextadd1.put("PLM_ExternalID", ctxAddd[i]);
                ctx2Add.put("context" + j, contextadd1);
                j++;
            }
        }

        String ctx2Remove = emxGetParameter(request, "Ctx2Remove");
        String[] ctxRemove = ctx2Remove.split(",");

        for (int i = 0; i < ctxRemove.length; i++) {
            contextrem1 = new HashMap();
            if (!ctxRemove[i].equals("")) {
                contextrem1.put("PLM_ExternalID", ctxRemove[i]);
                ctx2Rem.put("context" + k, contextrem1);
                k++;
            }
        }
        update.put("iCtx2Rem", ctx2Rem);
        update.put("iCtx2Add", ctx2Add);
    }

    if (source.equals("UpdateXP")) {
        String ctx2Remo = emxGetParameter(request, "context");
        String Assignment = emxGetParameter(request, "Assignment");
        String Orga = emxGetParameter(request, "currentOrganization");

        String assTab[] = Assignment.split("!!");
        String Active = (String) emxGetParameter(request, "Active");
        String VPLMAdmin = (String) emxGetParameter(request, "VPLMAdmin");
        String Prj = (String) emxGetParameter(request, "PrjAdmin");
        StringList ctxIds = new StringList();

        if (Active == null) {
            Active = "";
        }
        if (Active.equals("true")) {
            person.put("IsActive", Active);
        } else {
            if (Prj == null) {
                person.put("IsActive", "false");
            }
        }

        StringList persNames = new StringList();
        persNames.addElement(sPersonID);
        /*************************************************************
         * 관리자 권한 설정
         *************************************************************/
        if (VPLMAdmin.equals("true")) {
            SecurityContext secCtx1 = SecurityContext.getSecurityContext(mainContext, "VPLMAdmin." + HostCompanyName + ".Default");
            secCtx1.assignPersons(mainContext, persNames);
        }
        if (VPLMAdmin.equals("false")) {
            SecurityContext secCtx2 = SecurityContext.getSecurityContext(mainContext, "VPLMAdmin." + HostCompanyName + ".Default");
            secCtx2.unassignPersons(mainContext, persNames);
        }

        if (ctx2Remo.contains("!!")) {
            String[] remoTab = ctx2Remo.split("!!");

            for (int i = 1; i < remoTab.length; i++) {
                if (/*!(remoTab[i].contains("VPLMAdmin")) &&*/ remoTab[i].contains(";")) {
                    String[] PersContextOne = remoTab[i].split(";");
                    String RoleName = PersContextOne[0].replaceAll(",", " ");
                    String ProjectName = PersContextOne[1].replaceAll(",", " ");
                    String contextName = RoleName + "." + Orga + "." + ProjectName;

                    SecurityContext secCtx = SecurityContext.getSecurityContext(mainContext, contextName);
                    secCtx.unassignPersons(mainContext, persNames);
                }
            }
        }

        for (int i = 1; i < assTab.length; i++) {
            if (assTab[i].contains(";")) {
                String[] PersContextOne = assTab[i].split(";");
                String RoleName = PersContextOne[0].replaceAll(",", " ");
                String ProjectName = PersContextOne[1].replaceAll(",", " ");
                String contextName = RoleName + "." + Orga + "." + ProjectName;
                SecurityContext cont = new SecurityContext();

                boolean exist = SecurityContext.doesSecurityContextExists(context, contextName);

                if (!exist) {
                    cont.create(context, RoleName, Orga, ProjectName, "Security Context", "vplm", new HashMap());
                }

                SecurityContext secCtx = new SecurityContext();
                secCtx = SecurityContext.getSecurityContext(mainContext, contextName);
                secCtx.assignPersons(mainContext, persNames);
            }
        }
    }

    // JIC 18:07:03 FUN076129: Removed use of PLM key/added context
    update.put("iPersonInfo", person);
    ClientWithoutWS client = new ClientWithoutWS(mainContext);
    Map result = client.serviceCall(update);
    int res = ((Integer) result.get("resultat")).intValue();

//    //[S] Add By SeungYong,Lee [VPLM Role 변경 기록 , Enovia Role 변경 기록]
//    try {
//        JPO.invoke(context, "gscAPPInterfaceRoleChangeLog", null, "sendRoleChangeLog", JPO.packArgs(logMap), Map.class);
//        JPO.invoke(context, "gscAPPInterfaceRoleChangeLog", null, "sendRoleChangeEnoviaLog", JPO.packArgs(logMap), Map.class);
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
    //[E] Add By SeungYong,Lee [VPLM Role 변경 기록 , Enovia Role 변경 기록]
    responseUpdatePerson = responseUpdatePerson + res + "</updateResult><LicMessage>" + MessageLic + "</LicMessage></root>";
    response.getWriter().write(responseUpdatePerson);
%>
