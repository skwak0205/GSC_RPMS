<%-----------------------------------------------------------------------------
* FILE    : gscAPPRoleUpdateDefaultTable.jsp
* DESC    : 사용자 권한 Update 기본 테이블 페이지
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
* 2020-09-15     SeungYong,Lee   최초 생성
------------------------------------------------------------------------------%>
<%@page import="com.gsc.apps.common.util.gscListUtil"%>
<%@page import="com.gsc.apps.common.constants.gscCustomConstants"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.matrixone.vplm.posws.stubsaccess.ClientWithoutWS" %>
<%@ page import="com.matrixone.apps.common.Person" %>
<%@ page import="com.dassault_systemes.vplmposadministrationui.uiutil.AdminUtilities" %>
<%@include file = "emxPLMOnlineAdminAttributesCalculation.jsp"%>
<%@include file = "emxPLMOnlineAdminLicensesUtil.inc"%>
<%@include file = "../common/emxPLMOnlineAdminAuthorize.jsp"%>
<%@include file="../gscCommonTagLibInclude.inc"%>
<%@include file ="emxNavigatorTopErrorInclude.inc" %>
<%@include file = "gscCommonStyleInc.inc"%>
<%@include file = "../common/gscUICommonAutoComplete.inc"%>



<%--
    Document   : emxPLMOnlineAdminXPUpdatePerson.jsp
    Author     : LXM
    Modified :   26/05/2011 -> Replace Post by GEt for AIX
--%>
<%

    String Roles[][]= new String[7][2];
    Roles[0][0] = "VPLMProjectAdministrator";
    Roles[0][1] = getNLS("VPLMProjectAdministrator");
    Roles[1][0] = "VPLMCreator";
    Roles[1][1] = getNLS("VPLMCreator");
    Roles[2][0] = "VPLMExperimenter";
    Roles[2][1] = getNLS("VPLMExperimenter");
    Roles[3][0] = "VPLMProjectLeader";
    Roles[3][1] = getNLS("VPLMProjectLeader");
    Roles[4][0] = "VPLMViewer";
    Roles[4][1] = getNLS("VPLMViewer");
    Roles[5][0] = "VPLMSecuredCrossAccess";
    Roles[5][1] = getNLS("VPLMSecuredCrossAccess");
    // JIC 13:10:08 IR-260240V6R2014x: Added role "Domain Expert"
    Roles[6][0] = "Domain Expert";
    Roles[6][1] = getNLS("DomainExpert");
    String Current = getNLS("CurrentOrganization");

    String sPersonId=emxGetParameter(request,"PLM_ExternalID");
    
//[S] Add by SeungYong, Lee  [From/To User 구별하는 Title]
    String strUserTypeHeader = "";
    String strUserType = gscStringUtil.NVL(emxGetParameter(request,"userType"));
    if (strUserType.equals("fromUser")) {
        strUserTypeHeader = "emxComponents.Common.Header.FromUser";
    } else {
        strUserTypeHeader = "emxComponents.Common.Header.ToUser";
    }
//[E] Add by SeungYong, Lee  [From/To User 구별하는 Title]
            
//[S] Add by SeungYong, Lee  [Person gsc가 붙은 Enovia Role 리스트를 출력 (Enovia Role check 여부를 위함)]
    Vector vec =  PersonUtil.getUserRoles(context, sPersonId);
    
    StringList slEnoRoleList = new StringList();
    for (int i = 0, size = vec.size(); size > i ; i++) {
        String role = (String)vec.get(i);
        if(role.startsWith("gsc")) {
           slEnoRoleList.add(role); 
        }
    }
//[E] Add by SeungYong, Lee  [Person gsc가 붙은 Enovia Role 리스트를 출력 (Enovia Role check 여부를 위함)]

    String CannotRemLic = getNLS("ERR_CannotRemoveLicenses");
    String casualLicences = "";
    
    String TabInteger[] = new String[3];
    TabInteger[0]=getNLS("0");
    TabInteger[1]=getNLS("40");
    TabInteger[2]=getNLS("40");

    String nlsINFO_FullCasual = myNLS.getMessage("LicenSectionFullOrCasual",TabInteger);


    String HostCompanyName= getHostCompanyName(mainContext);

    String CasualLicenseTable[] = casualLicences.split(",");
    StringList casualStringList = new StringList(CasualLicenseTable);
    String target = "";
    String source = (String)emxGetParameter(request,"source");
    String message = (String)emxGetParameter(request,"message");
    String FilterProject = myNLS.getMessage("FilterProject");
    String orgid = (String)emxGetParameter(request,"currentOrganization");
    
    if (orgid==null || orgid.isEmpty()) {
        orgid = HostCompanyName;
    }
//[S] Add by SeungYong, Lee  [조직 리스트는 오직 HHI Engine 하위 리스트]
    
    String strCompanyEngine = UINavigatorUtil.getI18nString("emxComponents.Company.ENGINE", "emxComponents", strLanguage);
    StringList slResult = new StringList();
    slResult.add(HostCompanyName);
    String strMql = "expand bus $1 $2 $3 from rel $4 recurse to $5 select bus $6 dump $7";
    String strMqlResult = MqlUtil.mqlCommand(context,
                                             strMql, 
                                             gscCustomConstants.TYPE_COMPANY,
                                             strCompanyEngine,
                                             "-",
                                             "Division",
                                             "all",
                                             gscCustomConstants.SELECT_NAME,
                                             gscCustomConstants.SYMB_SEP);
    slResult.add(strCompanyEngine);
    slResult.addAll(gscListUtil.getQueryResultLastName(strMqlResult, gscCustomConstants.SYMB_SEP));
    int orgSize = slResult.size();
//[E] Add by SeungYong, Lee  [조직 리스트는 오직 HHI Engine 하위 리스트]

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script language="javascript" src="../emxUIPageUtility.js"></script>
        <script src="scripts/emxUIAdminConsoleUtil.js" type="text/javascript"></script>
        <style>
            div.horiz      { width:100%; position:relative; }
            div.divHauts   { height:42%; }
            div.divMilieus { height:53%; margin-left:-1px; overflow:auto; }
            div.divBass    { height:6%; }
            div.scroll-cont{
                            float:left; overflow:auto;
                            height:100%;
            }
            div.scroll-contOrg{
                            float:left; overflow:auto;
                            width:22%;   /* a cause des marges (et de leur gestion differente par IE et FF, il  */
                                         /* faut prendre un % de longueur inferieur a 50%                       */
                            height:100%; /* il est capital de fixer height pour que scroll-bloc ne deborde pas! */
            }
            div.licHeader {border:0px; background:#dfdfdf;}
            .tableLicMargin       {width:32; background:white; }
            .tableLicCheckbox     {width:32;}
            .tableLicAvailability {width:32;}
            .tableLicTitle        {}

            #tableHeaderColumn{
                    min-height:26px;
                    padding:10px 5px 10px 5px;
                    background: #f5f6f7; /* Old browsers */
                    background: -moz-linear-gradient(top, #f5f6f7 0%, #e2e4e3 100%); /* FF3.6+ */
                    background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#f5f6f7), color-stop(100%,#e2e4e3)); /* Chrome,Safari4+ */
                    background: -webkit-linear-gradient(top, #f5f6f7 0%,#e2e4e3 100%); /* Chrome10+,Safari5.1+ */
                    background: -o-linear-gradient(top, #f5f6f7 0%,#e2e4e3 100%); /* Opera 11.10+ */
                    background: -ms-linear-gradient(top, #f5f6f7 0%,#e2e4e3 100%); /* IE10+ */
                    background: linear-gradient(to bottom, #f5f6f7 0%,#e2e4e3 100%); /* W3C */
                    filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f5f6f7', endColorstr='#e2e4e3',GradientType=0 ); /* IE6-9 */
                    font-weight:bold;
                    border-top:1px solid #b4b6ba;
                    border-left:1px solid #b4b6ba;
                    border-bottom:1px solid #b4b6ba;
        }
        </style>
        <script>
        
var xmlreqs = new Array();
var prev_filter_lic = "";
var filter_lic = false;

    var host ="";
    var projects="";
    var roles = "";


    function displayRoleMatrix() {
        xmlhttpRole = xmlreqs[0];
        if(xmlhttpRole.readyState==4) {
            roles =xmlhttpRole.responseXML.getElementsByTagName("Role");
            xmlreq("emxPLMOnlineAdminAjaxResponse.jsp","source=Organization&Destination=VPLMAdmin&filterOrg=*&responseOrg=org_Type",displayRoleMatrixWithOrganization,6);
        }
    }
        


    function displayRoleMatrixWithOrganization() {
        var xmlhttpOrg = xmlreqs[6];
        if(xmlhttpOrg.readyState==4) {
            var table=document.getElementById('TableToFilter');
            var row=table.insertRow(-1);
            var cell=row.insertCell(-1);
            cell.style.fontSize = "10pt";
            cell.style.fontWeight = "bold";
            cell.style.height = "30px";
                
//[S] Add by SeungYong,Lee 필터컬럼
            cell.id = 'tableHeaderColumn';
//[E] Add by SeungYong,Lee 필터컬럼
                
            addOrgColumn(cell);
            var table1=document.getElementById('TableOrg');

            var response = xmlhttpOrg.responseXML.getElementsByTagName("org_Type");
            var responsePLMID = xmlhttpOrg.responseXML.getElementsByTagName("PLM_ExternalID");
            
            
            var row3=table1.insertRow(-1);
            var cell5=row3.insertCell(-1);
            cell5.className="title";
            cell5.innerHTML="<%=Current%>"+":<b style=\"color:red\" >*";


            for (var i = 0 ; i <roles.length ; i++){
                var cell=row.insertCell(-1);
                cell.className = "matrixCell";
                cell.style.fontWeight = "bold";
                cell.id = "tableHeaderColumn";
                var roleName = roles[i].getElementsByTagName("PLM_ExternalID").item(0).firstChild.data;
                var roleNameNLS = roleName;
                <%for (int i = 0 ; i < 7 ; i ++){%>
                if ("<%=Roles[i][0]%>" == roleName)roleNameNLS="<%=Roles[i][1]%>";
                <%}%>
                cell.innerHTML=roleNameNLS;
                cell.title=roleNameNLS;
            }
            xmlreq("emxPLMOnlineAdminAjaxResponse.jsp","source=Project&Solution=VPM&Destination=XP&Method=Create",displayProjectMatrix,2);
        }
    }

    function displayProjectMatrix() {
        var xmlhttp = xmlreqs[2];
        if(xmlhttp.readyState==4) {
            projects =xmlhttp.responseXML.getElementsByTagName("Project");
            var table = document.getElementById('TableToFilter');
            for (var i = 0 ; i <projects.length ; i++){
                var row=table.insertRow(-1);
                var cell=row.insertCell(-1);
                cell.style.fontWeight = "bold";
                cell.style.fontSize = "12pt";
                cell.style.color = "white";
                cell.style.display = "none";
                    
                var cellTitle =row.insertCell(-1);
                cellTitle.style.fontWeight = "bold";
                cellTitle.style.fontSize = "12pt";
                cellTitle.style.width = "auto";
                
                var prjName =projects[i].getElementsByTagName("PLM_ExternalID").item(0).textContent;
                cell.innerHTML = projects[i].getElementsByTagName("PLM_ExternalID").item(0).textContent.htmlEncode();
                
                var prjNameTitle =projects[i].getElementsByTagName("V_Name").item(0).textContent; 
                cellTitle.id=prjName
                cellTitle.setAttribute("class","label");
                
                var titleTextNode = document.createElement("span");
                titleTextNode.setAttribute("title","Name:"+prjName);
                titleTextNode.innerHTML = projects[i].getElementsByTagName("V_Name").item(0).textContent.htmlEncode();
                cellTitle.appendChild(titleTextNode);
                for (var j = 0 ; j <roles.length ; j++){
                    var cell=row.insertCell(-1);
                    var roleName = roles[j].getElementsByTagName("PLM_ExternalID").item(0).firstChild.data;
                    var roleNameNLS = roleName;
                    <%for (int i = 0 ; i < 7 ; i ++){%>
                    if ("<%=Roles[i][0]%>" == roleName)roleNameNLS="<%=Roles[i][1]%>";
                    <%}%>

                    var string = roleName+";"+prjName;
                    var secCtx = roleNameNLS+" / "+prjName;
//[S] Add by SeungYong,Lee
                    cell.setAttribute('class', 'field');
//[E] Add by SeungYong,Lee
                    string = ReplaceAllOccurence(string," ",",");
                    if((prjName=="Default") && (roleName=="VPLMAdmin")){
                         cell.innerHTML="<input type='checkbox' align='center' class='field' disabled id="+string+" title='"+secCtx+"'>";
                    }else{
                         cell.innerHTML="<input type='checkbox' align='center' class='field' onclick='javascript:RemoveCheckBoxBis(\""+string+"\")' id="+string+" title='"+secCtx+"' >";
                    }
                }
            }
        }
    }



        function initXPPerson(hostCompany){
            var destRole =  "source=Role&Destination=XP&Method=Create";
            host = hostCompany;
            xmlreq("emxPLMOnlineAdminAjaxResponse.jsp",destRole,displayRoleMatrix,0);
            
            getTopWindow().document.querySelector('#imgProgressDiv').style.visibility = "hidden";
        }

        function addOrgColumn(cellOrg) {
            //[S] Add by SeungYong, Lee
              var xmlhttpOrg = xmlreqs[6]; 
              var response = xmlhttpOrg.responseXML.getElementsByTagName("org_Type");
              var responsePLMID = xmlhttpOrg.responseXML.getElementsByTagName("PLM_ExternalID");

              
              cellOrg.style.fontSize = "10pt";
              cellOrg.style.fontWeight = "bold";
              cellOrg.style.width = "2em";
              cellOrg.innerHTML=" <select id='FilterArea3' onchange='refreshTable()'>";
              var nameElement = "";
              var selectedName  = "<%=orgid%>" 
              <%for(int i = 0; orgSize > i; i++) { %>
                     nameElement = "<%=slResult.get(i)%>";
                     if (nameElement == selectedName){
                         var nouvel_element = new Option(nameElement,nameElement,true,true);
                     } else {
                         var nouvel_element = new Option(nameElement,nameElement,false,false);
                     }
                     
                     document.getElementById("FilterArea3").options[document.getElementById("FilterArea3").length] = nouvel_element;
              <%}%>
          }
        </script>

    </head>
<%
    String UserId = myNLS.getMessage("UserID");
    String Email = myNLS.getMessage("Email");
    String FirstName = myNLS.getMessage("FirstName");
    String LastName = myNLS.getMessage("LastName");
    String Phone = myNLS.getMessage("HomePhone");
    String WorkPhone = myNLS.getMessage("WorkPhone");
    String Street = myNLS.getMessage("Street");
    String City = myNLS.getMessage("City");
    String State = myNLS.getMessage("State");
    String PostalCode = myNLS.getMessage("PostalCode");
    String Country = myNLS.getMessage("Country");
    String Update = myNLS.getMessage("Update");
    String Active = myNLS.getMessage("Active");
    String Contractor = myNLS.getMessage("Contractor");
    String Administrator = myNLS.getMessage("Administrator");
%>
    <body id="bodyContent" onload="javascript:initXPPerson('<%=XSSUtil.encodeForJavaScript(context,(String)orgid)%>');">
          <script>addReturnMessage();</script>
    <%

    boolean isAdmin = false;
    boolean isContractor = false;
    // JIC 15:03:18 Added person Casual Hours
    int casualHour = 0;

    Map person = new HashMap();
    Map listperson = new HashMap();

    person.put("PLM_ExternalID",sPersonId);
    listperson.put("person0" ,person);

    Map fin = new HashMap();
    fin.put("method","queryPerson");
    String[] slec = new String[] {""};
    fin.put("iPerson",listperson);
    // JIC 18:07:03 FUN076129: Removed use of PLM key/added context
    ClientWithoutWS client = new ClientWithoutWS(mainContext);
    Map per = client.serviceCall(fin);
    Map pers = (Map)per.get("person0");


    %>

         <form action="" id="submitForm" name="submitForm"  method="GET">
             <textarea name="contexts" id="contexts" style="display: none;"></textarea>
             <textarea name="removeSelect" id="removeSelect"  style="display:none"></textarea>
             <textarea name="roleList" id="roleList" style="display: none;"></textarea>
             <div style="display:none"><input type="text" name="Source" id="Source" style="display:none" value="UpdateXP">UpdateXP</input>
             <input type="text" name="PLM_ExternalID" id="PLM_ExternalID" style="display:none" value=""></input>
             <input type="text" name="userType" id="userType" style="display: none;" value="">
             <!-- <input type="text" name="PLM_ExternalID" id="PLM_ExternalID" style="display:none" value=""></input> -->
             </div>
            <table class="form">
            </table>
            <table class="form">
                      <script>
                            // JIC 2014:10:21 Added functions "refreshContractor" and "refreshVPLMAdmin" for contractor support
                           
                        </script>
                        <!-- JIC 2014:10:21 Added checkbox "Contractor" for contractor support-->
                        <tr>
                            <td class="label"> <%=Contractor%>
                                <td class="title field">
                                        <input type="checkbox" id="Contractor" name="Contractor" onchange="refreshVPLMAdmin()">
                                </td>
                            </td>
                        </tr>
                        <tr>
                            <td class="label"> <%=Administrator%>
                                <td class="title field">
                                        <input type="checkbox" id="VPLMAdmin" name="VPLMAdmin" onchange="refreshContractor()">
                                </td>
                            </td>
                        </tr>
             </table>
             <%if (message != null) {%>
               <script type="text/javascript">setTime("<%=message%>");</script><%}%>
            <div class="divHauts horiz" id="divHauts" style="display: none;">
                     <div class="scroll-cont" style="width : 38%" >
                        <table class="big">
                       <%if ( !AdminUtilities.isProjectAdmin(mainContext)){%>
                       <tr><td class="pic"></td>
                          <td class="title"><img src="images/iconSmallPeople.gif"/><%=UserId%> : <%=emxGetParameter(request,"PLM_ExternalID")%></td><td class="title"><%=myNLS.getMessage("UserAlias")%></td><td><input type="text" name="Alias" id="Alias" value=""></td></tr>
                         <tr><td class="title"><%=FirstName%></td><td><input type="text" id="V_first_name" name="V_first_name" value=""></td><td class="title"><%=Street%></td><td><input type="text" name="Street" id="Street" value=""></td></tr>
                        <tr><td class="title"><%=LastName%></td><td><input type="text" name="V_last_name" id="V_last_name" value=""></td><td class="title"><%=City%></td><td><input type="text" name="City"  id="City" value=""></td></tr>
                        <tr><td class="title"><%=Email%></td><td><input type="text" name="V_email" id="V_email" value=""></td><td class="title"><%=State%></td><td><input type="text" name="State" id="State" value=""></td></tr>
                        <tr><td class="title"><%=Phone%></td><td><input type="text" name="V_phone" id="V_phone" value=""></td><td class="title"><%=PostalCode%></td><td><input type="text" name="PostalCode"  id="PostalCode" value=""></td></tr>
                        <tr><td class="title"><%=WorkPhone%></td><td><input type="text" name="Work_Phone_Number" id="Work_Phone_Number" value=""></td>
                        <td class="title"><%=Country%></td>
                                    <%
                                        java.util.Map countryChoiceDetails = (java.util.Map) matrix.db.JPO.invoke(context, "emxOrganization", null, "getCountryChooserDetailsForHTMLDisplay", new String[]{request.getHeader("Accept-Language")}, java.util.HashMap.class);
                                        java.util.List optionList =  (java.util.List)countryChoiceDetails.get("optionList");
                                        java.util.List valueList =  (java.util.List)countryChoiceDetails.get("valueList");
                                        java.util.List manualEntryList =  (java.util.List)countryChoiceDetails.get("manualEntryList");
                                        String countryDefaultValue = (String)countryChoiceDetails.get("default");
                                    %>
                        <td>
         <framework:editOptionList disabled="false" name="Country" optionList="<%=optionList%>" valueList="<%=valueList%>" sortDirection="ascending" selected="<%=countryDefaultValue%>" manualEntryList="<%=manualEntryList%>"/>                                   
                        </td></tr>
                        <tr>
                            <td class="title" ><%=Active%></td>
                            <td>
                                <input type="checkbox" name="Active">
                            </td>
                        </tr>
                            <%} else {%>
                          <td class="pic"><img src="images/iconSmallPeople.gif"><td class="title"><%=UserId%> : <%=emxGetParameter(request,"PLM_ExternalID")%></td>
                        <tr><td class="title"><%=FirstName%></td><td><input type="text" id="V_first_name"  name="V_first_name" value="" readonly></td><td class="title"><%=Street%></td><td><input type="text" name="Street" id="Street" value="" readonly></td></tr>
                        <tr><td class="title"><%=LastName%></td><td><input type="text" name="V_last_name" id="V_last_name" value="" readonly></td><td class="title"><%=City%></td><td><input type="text" name="City" id="City" value="" readonly></td></tr>
                        <tr><td class="title"><%=Email%></td><td><input type="text" name="V_email" id="V_email" value="" readonly></td><td class="title"><%=State%></td><td><input type="text" name="State" id="State" value="" readonly></td></tr>
                        <tr><td class="title"><%=Phone%></td><td><input type="text" name="V_phone"  id="V_phone" value="" readonly></td><td class="title"><%=PostalCode%></td><td><input type="text" name="PostalCode" id="PostalCode" value="" readonly></td></tr>
                        <tr><td class="title"><%=WorkPhone%></td><td><input type="text" name="Work_Phone_Number" id="Work_Phone_Number" value="" readonly></td>
                        <td class="title"><%=Country%></td>
                                    <%
                                        java.util.Map countryChoiceDetails = (java.util.Map) matrix.db.JPO.invoke(context, "emxOrganization", null, "getCountryChooserDetailsForHTMLDisplay", new String[]{request.getHeader("Accept-Language")}, java.util.HashMap.class);
                                        java.util.List optionList          = (java.util.List)countryChoiceDetails.get("optionList");
                                        java.util.List valueList           = (java.util.List)countryChoiceDetails.get("valueList");
                                        java.util.List manualEntryList     = (java.util.List)countryChoiceDetails.get("manualEntryList");
                                        String countryDefaultValue         = (String)countryChoiceDetails.get("default");
                                    %>
                        <td>
         <framework:editOptionList disabled="true" name="Country" optionList="<%=optionList%>" valueList="<%=valueList%>" sortDirection="ascending" selected="<%=countryDefaultValue%>" manualEntryList="<%=manualEntryList%>"/>                                    
                        </td></tr>
                        <tr><td  class="title" >Active</td><td >
                        <input type="checkbox" name="Active"  disabled>
                               </td><input name="PrjAdmin" value="PrjAdmin" style="display:none"></tr>

                        <%}%>

                        </table>
                </div>

                <div class="scroll-contOrg" style="border-left: 1px white solid;border-right: 1px white solid">
                     <!-- <img  align="middle" id="imageWaitingTable"  src="images/iconParamProgress.gif"> -->
                        <table width="100%"  id="TableOrg" style="height : 90%">
                        <img src="images/iconSmallXPOrganization.gif">
                        <tr style="width:100%">
                            <td class="title" width="32%"><%=getNLS("Employee")%>: <td style="color:#50596F" id="OrgEmp"></td>


                        </tr>
                        <tr style="width:100%">
                            <td class="title"><%=getNLS("Member")%>: <td style="color:#50596F" id="OrgMem"></td>


                        </tr>
                     </table>
                 </div>
            </div>

            <!-- <hr> -->
            <table class="form">
            <tr>
                <td class="heading1" colspan="4" >
                <emxUtil:i18n localize="i18nId">emxComponents.Common.Header.SecurityContext</emxUtil:i18n>
                </td>
            </tr>
            </table>
            <div class="divMilieus horiz" >
                    <table id="TableToFilter" width="100%" style="height:40% ; background-color : white;  border-color: white"  >
                    </table>
            </div>
            <table class="form">
            <tr>
                <td class="heading1" colspan="4" >
                <emxUtil:i18n localize="i18nId">emxComponents.Common.Role</emxUtil:i18n>
                </td>
            </tr>
            </table>
            
            
            <table class="form" id="RoleTable">
            <tr>
                <td class="field">
                <gscTagUtil:displayMultiSelectUtil selectBoxName="enoviaRole" required="true"
                        inputType="checkbox" 
                        showAll="false"
                        isSearchField="true" colName="emxComponents.Common.Role" 
                        resourceName="emxComponentsStringResource" 
                        program="gscCommonUtil:getCommonAttributeRangeList">gscEnoviaRoleCode</gscTagUtil:displayMultiSelectUtil>
                </td>
            </tr>
            </table>
                
            <textarea  name="HiddenElement" id="HiddenElement" style="display:none"></textarea>
            
            <textarea  name="removeRole" id="removeRole" style="display:none"></textarea>
            <textarea  name="addRole" id="addRole" style="display:none"></textarea>
        </form>
</body>
</html>
