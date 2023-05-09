<%-----------------------------------------------------------------------------
* FILE    : emdAPPRoleUpdateTable.jsp
* DESC    : 사용자 Security Context, Role 권한 Update 페이지
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
* 2020-08-27     SeungYong,Lee   최초 생성
------------------------------------------------------------------------------%>
<%@page import="com.emd.apps.common.util.emdListUtil"%>
<%@page import="com.emd.apps.common.constants.emdCustomConstants"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.matrixone.vplm.posws.stubsaccess.ClientWithoutWS" %>
<%@ page import="com.dassault_systemes.vplmposadministrationui.uiutil.AdminUtilities" %>
<%@ page import="com.matrixone.vplm.posmodel.license.LicenseInfo" %>
<%@include file = "emxPLMOnlineAdminAttributesCalculation.jsp"%>
<%@include file = "emxPLMOnlineAdminLicensesUtil.inc"%>
<%@include file = "../common/emxPLMOnlineAdminAuthorize.jsp"%>
<%@include file="../emdCommonTagLibInclude.inc"%>
<%@include file ="emxNavigatorTopErrorInclude.inc" %>
<%@include file = "emdCommonStyleInc.inc"%>
<%@include file = "../common/emdUICommonAutoComplete.inc"%>



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

    String ResultString[]= new String[2];
    ResultString[0] = getNLS("YourPersonHasBeenUpdated");
    ResultString[1] = getNLSMessageWithParameter("ERR_CreationRight","UserID");
    String sPersonId=emxGetParameter(request,"PLM_ExternalID");

//[S] Add by SeungYong, Lee  [From/To User 구별하는 Title]
    String strUserTypeHeader = "";
    String strUserType = emdStringUtil.NVL(emxGetParameter(request,"userType"));
    String isLicense = emdStringUtil.NVL(emxGetParameter(request,"isLicense"));
    String strFromUserName = emdStringUtil.NVL(emxGetParameter(request,"fromUserName"));
    if (strUserType.equals("fromUser")) {
        strUserTypeHeader = "emxComponents.Common.Header.FromUser";
    } else {
        strUserTypeHeader = "emxComponents.Common.Header.ToUser";
    }
//[E] Add by SeungYong, Lee  [From/To User 구별하는 Title]

//[S] Add by SeungYong, Lee  [Person emd가 붙은 Enovia Role 리스트를 출력 (Enovia Role check 여부를 위함)]
    Vector vec =  PersonUtil.getUserRoles(context, sPersonId);

    StringList slEnoRoleList = new StringList();
    for (int i = 0, size = vec.size(); size > i ; i++) {
        String role = (String)vec.get(i);
        if(role.startsWith("emd")) {
           slEnoRoleList.add(role);
        }
    }
//[E] Add by SeungYong, Lee  [Person emd가 붙은 Enovia Role 리스트를 출력 (Enovia Role check 여부를 위함)]

    String CannotRemLic = getNLS("ERR_CannotRemoveLicenses");
    String casualLicences = "";
    try{
        casualLicences = getCasualLicencesAssigned(mainContext,sPersonId,"40");
    } catch (Exception e) {
        e.printStackTrace();
    }

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
                                             emdCustomConstants.TYPE_COMPANY,
                                             strCompanyEngine,
                                             "-",
                                             "Division",
                                             "all",
                                             emdCustomConstants.SELECT_NAME,
                                             emdCustomConstants.SYMB_SEP);
    slResult.add(strCompanyEngine);
    slResult.addAll(emdListUtil.getQueryResultLastName(strMqlResult, emdCustomConstants.SYMB_SEP));
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

    /**
     * Update Submit Function
     */
    function sendToUpdatePage() {
        getTopWindow().document.querySelector('#imgProgressDiv').style.visibility = "";
        var personFrame = parent.document.getElementById('iPersonFrame').contentWindow.document;
        var fromUser    = personFrame.getElementById('fromName').value;
        var toUser      = personFrame.getElementById('toName').value;

        if(fromUser == '' || fromUser == null) {
            if( toUser =='' || toUser == null){
                return alert('Please search Person');
            }
        }

        /* DisplayLoading(); */
        var URLToSend = "";
        var personAttributes = document.getElementsByTagName("input");

        for(var i = 0 ; i < personAttributes.length ; i++) {
            if ((personAttributes[i].name != "") && (personAttributes[i].type == "text")) {
                URLToSend=URLToSend+personAttributes[i].name+"="+encodeURIComponent(personAttributes[i].value)+"&";
            }
        }
        URLToSend=URLToSend+"Country=";
        var country="";
        if(document.getElementById("CountryId").selectedIndex != -1){
            if (document.getElementById("CountryId").options[document.getElementById("CountryId").selectedIndex].value == "```manualEntryOptionDisplay```") {
                country=document.getElementById("CountryIdtxt").value;
            } else {
                country=document.getElementById("CountryId").options[document.getElementById("CountryId").selectedIndex].value;
            }
        }
        URLToSend=URLToSend+encodeURIComponent(country)+"&";

        var Licences = getSelectedLicencesCheckbox();

        var org = document.getElementById("FilterArea3").options[document.getElementById("FilterArea3").options.selectedIndex].value;
        URLToSend=URLToSend+"context="+encodeURIComponent(document.getElementById("removeSelect").innerHTML.htmlDecode());
        URLToSend=URLToSend+"&Assignment="+encodeURIComponent(document.getElementById("HiddenElement").innerHTML.htmlDecode());
        URLToSend=URLToSend+"&currentOrganization="+encodeURIComponent(org);

        URLToSend=URLToSend+"&Active="+document.getElementsByName("Active")[0].checked;
        // JIC 2014:10:23 Added Contractor support
        if (document.getElementById("Contractor") != null) {
            URLToSend=URLToSend+"&Contractor="+document.getElementById("Contractor").checked;
        }
        if ((document.getElementsByName("VPLMAdmin") != null) && (document.getElementsByName("VPLMAdmin").length>0)) {
            URLToSend=URLToSend+"&VPLMAdmin="+document.getElementsByName("VPLMAdmin")[0].checked;
        } else {
            URLToSend=URLToSend+"&VPLMAdmin=rien";
        }
        // JIC 2015:03:17 Added Casual Hour support
        if (document.getElementById("Casual") != null) {
            URLToSend=URLToSend+"&CasualHour="+(document.getElementById("Casual").checked==true?"40":"0");
        }
        if(isLicense )
        URLToSend=URLToSend+"&Licences="+Licences;
        URLToSend=URLToSend+"&ActualLicences="+"<%=casualLicences%>";

        URLToSend=URLToSend+"&addRole="+encodeURIComponent(document.getElementById("addRole").innerHTML.htmlDecode());
        URLToSend=URLToSend+"&removeRole="+encodeURIComponent(document.getElementById("removeRole").innerHTML.htmlDecode());

        if (document.getElementById("PrjAdmin") != null) {
            URLToSend=URLToSend+"&PrjAdmin="+PrjAdmin;
        }

        var xmlhttp;
        if (window.XMLHttpRequest) {// Mozilla/Safari
            xmlhttp = new XMLHttpRequest();
        } else if (window.ActiveXObject) {// IE
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        } else {
            alert('Sorry, your browser does not support XML HTTP Request!');
        }
        xmlreqs[3] = xmlhttp;
        xmlhttp.open("POST", "emdAPPRoleUpdateProcess.jsp", true);
        xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xmlhttp.onreadystatechange = updatePersonDB;
        xmlhttp.send(URLToSend);
    }

    /**
     * 유저 권한 Update 결과를 반환
     */
    function updatePersonDB() {
        var xmlhttp=xmlreqs[3];

        xmlhttp.onreadystatechange=function() {
            if(xmlhttp.readyState==4) {
               var createResult = xmlhttp.responseXML.getElementsByTagName("updateResult");
               if(createResult[0].firstChild.data==0) {
                    var dbView = document.getElementById("contexts").innerHTML;
                    var removeSelect = document.getElementById("removeSelect").innerHTML;
                    var boxCombin = removeSelect.split("!!");
                    for (var i=1; i<boxCombin.length; i++) {
                        dbView = ReplaceAllOccurence(dbView,"!!"+boxCombin[i],"");
                    }
                    document.getElementById("removeSelect").innerHTML = "";
                    document.getElementById("contexts").innerHTML = dbView + document.getElementById("HiddenElement").innerHTML;
                    document.getElementById("HiddenElement").innerHTML = "";
               }

                var createLicenseResult = xmlhttp.responseXML.getElementsByTagName("LicMessage");
                <% for (int i = 0 ; i <2 ; i++) {%>
                    if (<%=i%> == createResult[0].firstChild.data){
                        document.getElementById("messageError").innerHTML = "<%=ResultString[i]%>";
                    }
                <%}%>
                if (document.getElementById("messageError").innerHTML == "") document.getElementById("messageError").innerHTML = "<%=ResultString[1]%>";
                if (createLicenseResult[0].firstChild.data != "Rien") {
                    if (createLicenseResult[0].firstChild.data.length > 2){
                        document.getElementById("messageError").innerHTML = document.getElementById("messageError").innerHTML +"\n" +createLicenseResult[0].firstChild.data;
                    } else{
                        document.getElementById("messageError").innerHTML = document.getElementById("messageError").innerHTML +"\n" + "<%=CannotRemLic%>";
                    }
                }
                var LicencesString = "source=Licences&Filter="+encodeURIComponent("<%=sPersonId%>");

                /* HideLoading(); */
                document.getElementById("hidethisone").style.display="block";
                getTopWindow().document.querySelector('#imgProgressDiv').style.visibility = "hidden";
            }
        }
    }

    /**
     * 검색된 사용자의 적용된 Security Context 체크
     */
    function CheckContext() {
        var txt =document.getElementById("contexts").innerHTML;
        if (txt!=null && txt!="") {
            var boxCombin = txt.split("!!");
            for (var i=1; i<boxCombin.length; i++) {
                var nameCheckBox = boxCombin[i].htmlDecode();
                if (document.getElementById(nameCheckBox)!=null) {
                    document.getElementById(nameCheckBox).checked = true;
                }
            }
         }
    }

    /**
     * 조직 리스트 선택 시 새로고침
     */
    function refreshTable() {
        var orga = $("FilterArea3").options[$("FilterArea3").options.selectedIndex].value;
            window.location.replace("emdAPPRoleUpdateTable.jsp?PLM_ExternalID="+encodeURIComponent("<%=sPersonId%>")+"&source=Admin&currentOrganization="+encodeURIComponent(orga));
    }

    /**
     * Security Context 삭제 할 권한을 구별함.
     */
    function RemoveCheckBoxBis(boxName){
            var found = false;
            var txt = document.getElementById("contexts").innerHTML;
            if(txt!=null && txt!="" && txt.indexOf(boxName)>-1) {
                found = true;
                var removeSelect = document.getElementById("removeSelect").innerHTML;
                if (document.getElementById(boxName).checked==false && removeSelect.indexOf(boxName)==-1) {
                    removeSelect = removeSelect + "!!"+boxName;
                } else if(document.getElementById(boxName).checked==true && removeSelect.indexOf(boxName)>-1) {
                    removeSelect = ReplaceAllOccurence(removeSelect,"!!"+boxName,"");
                }
                document.getElementById("removeSelect").innerHTML = removeSelect;
            }
            if (found==false) {
                var HiddenElement = document.getElementById("HiddenElement").innerHTML;
                if (document.getElementById(boxName).checked==true && HiddenElement.indexOf(boxName)==-1) {
                    HiddenElement = HiddenElement + "!!"+boxName;
                } else if(document.getElementById(boxName).checked==false && HiddenElement.indexOf(boxName)>-1) {
                    HiddenElement = ReplaceAllOccurence(HiddenElement,"!!"+boxName,"");
                }
                document.getElementById("HiddenElement").innerHTML = HiddenElement;
            }
      }

    var xmlreqs = new Array();
    var prev_filter_lic = "";
    var filter_lic = false;

    var host ="";
    var projects="";
    var roles = "";


    /**
     * View를 구성 할 Data를 불러옴
     */
    function displayRoleMatrix() {
        xmlhttpRole = xmlreqs[0];
        if(xmlhttpRole.readyState==4) {
            roles =xmlhttpRole.responseXML.getElementsByTagName("Role");
            xmlreq("emxPLMOnlineAdminAjaxResponse.jsp","source=Organization&Destination=VPLMAdmin&filterOrg=*&responseOrg=org_Type",displayRoleMatrixWithOrganization,6);
        }
    }

     /*
      * Filter a table, with a letter specified in an area which id = FilterArea.
      * and the Table ID = TableToFilter
      */
     function localfilterStrings(){
        var doc = document.getElementById("FilterArea").value;
        var regex = /\*/g;
        doc = doc.replace(regex,".*");
        var row= document.getElementById("TableToFilter").rows;
        for (var i = 1 ; i < row.length ; i++) {
             row[i].style.display = "";
         }
        for (var i = 1 ; i < row.length ; i++) {
             /*var nom = row[i].cells[1].innerHTML;*/
            var nom = row[i].cells[1].textContent;
            //TODO: case sensitivity check
              if (nom.match(doc) == null) {
                 row[i].style.display = "none";
             }
        }
    }


    /**
     * Security Context Table View 구성
     */
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
    /**
     * Security Context Table CheckBox 구성
     */
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
                    <%for (int i = 0 ; i < 7 ; i ++) {%>
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
               CheckContext();
        }
    }

    /**
     * Person 정보
     */
    function displayPerson() {
        var xmlhttpPerson = xmlreqs[2];
        if(xmlhttpPerson.readyState==4){
            var destRole =  "source=Role&Destination=XP&Method=Create";

            var person =xmlhttpPerson.responseXML.getElementsByTagName("Person");


            if(xmlhttpPerson.responseXML.getElementsByTagName("FirstName")[0].firstChild != null ){

             document.getElementById("V_first_name").value = xmlhttpPerson.responseXML.getElementsByTagName("FirstName")[0].firstChild.data;
        }
        if(xmlhttpPerson.responseXML.getElementsByTagName("LastName")[0].firstChild != null)
            document.getElementById("V_last_name").value = xmlhttpPerson.responseXML.getElementsByTagName("LastName")[0].firstChild.data;
            if(xmlhttpPerson.responseXML.getElementsByTagName("Email")[0].firstChild != null) {
                var childNodes = xmlhttpPerson.responseXML.getElementsByTagName("Email")[0].childNodes;
                var email = "";
                for(var k = 0; k < childNodes.length; k++) {
                    email = email + childNodes[k].data ;
                }
                document.getElementById("V_email").value = email;
            }
           if(xmlhttpPerson.responseXML.getElementsByTagName("Street")[0].firstChild != null)
           document.getElementById("Street").value = xmlhttpPerson.responseXML.getElementsByTagName("Street")[0].firstChild.data;
           if(xmlhttpPerson.responseXML.getElementsByTagName("City")[0].firstChild != null)
           document.getElementById("City").value = xmlhttpPerson.responseXML.getElementsByTagName("City")[0].firstChild.data;
           if(xmlhttpPerson.responseXML.getElementsByTagName("State")[0].firstChild != null)
           document.getElementById("State").value = xmlhttpPerson.responseXML.getElementsByTagName("State")[0].firstChild.data;
           if(xmlhttpPerson.responseXML.getElementsByTagName("PostalCode")[0].firstChild != null)
           document.getElementById("PostalCode").value = xmlhttpPerson.responseXML.getElementsByTagName("PostalCode")[0].firstChild.data;
           if(xmlhttpPerson.responseXML.getElementsByTagName("Country")[0].firstChild != null) {
           document.getElementById("CountryId").value = xmlhttpPerson.responseXML.getElementsByTagName("Country")[0].firstChild.data;
           if(document.getElementById("CountryId").value == ""){
           document.getElementById("CountryIdtxt").style = "";
           document.getElementById("CountryIdtxt").disabled = false;
           document.getElementById("CountryIdtxt").value = xmlhttpPerson.responseXML.getElementsByTagName("Country")[0].firstChild.data;
           document.getElementById("CountryId").value = "```manualEntryOptionDisplay```";
                }
            }
           if(xmlhttpPerson.responseXML.getElementsByTagName("Alias")[0].firstChild != null)
           document.getElementById("Alias").value = xmlhttpPerson.responseXML.getElementsByTagName("Alias")[0].firstChild.data;
           if(xmlhttpPerson.responseXML.getElementsByTagName("Phone")[0].firstChild != null)
           document.getElementById("V_phone").value = xmlhttpPerson.responseXML.getElementsByTagName("Phone")[0].firstChild.data;
           if(xmlhttpPerson.responseXML.getElementsByTagName("WorkPhone")[0].firstChild != null)
           document.getElementById("Work_Phone_Number").value = xmlhttpPerson.responseXML.getElementsByTagName("WorkPhone")[0].firstChild.data;

           if(xmlhttpPerson.responseXML.getElementsByTagName("IsActive")[0].firstChild != null) {

               if(xmlhttpPerson.responseXML.getElementsByTagName("IsActive")[0].firstChild.data == "Active"){
                   document.getElementsByName("Active")[0].checked = true;
               } else {
                   document.getElementsByName("Active")[0].checked = false;
               }
           }
               var OrgString="";
               if(xmlhttpPerson.responseXML.getElementsByTagName("Emp").length > 0 &&
                  xmlhttpPerson.responseXML.getElementsByTagName("Emp")[0].firstChild != null)
               {
                   // JIC 17:03:13 IR IR-508813-3DEXPERIENCER2018x: Replaced "firstChild.data" with loop on all nodes as Internet Explorer 10 splits text containins hyphens into several nodes (and "textContext" is not supported prior to Internet Explorer 9...)
                   // document.getElementById("OrgEmp").innerHTML = "<b>"+xmlhttpPerson.responseXML.getElementsByTagName("Emp")[0].firstChild.data.htmlEncode()+"</b>";
                   var childNodes = xmlhttpPerson.responseXML.getElementsByTagName("Emp")[0].childNodes;
                   var employeeOrgName = ""
                   for (var j = 0; j < childNodes.length; j++)
                   {
                       employeeOrgName = employeeOrgName + childNodes[j].data.htmlEncode();
                   }
                   document.getElementById("OrgEmp").innerHTML = "<b>"+employeeOrgName+"</b>";
               }
               if(xmlhttpPerson.responseXML.getElementsByTagName("Org").length > 0 &&
                  xmlhttpPerson.responseXML.getElementsByTagName("Org")[0].firstChild != null)
               {
                   if(xmlhttpPerson.responseXML.getElementsByTagName("Org").length == 1){
                       // JIC 17:03:13 IR IR-508813-3DEXPERIENCER2018x: Replaced "firstChild.data" with loop on all nodes as Internet Explorer 10 splits text containins hyphens into several nodes (and "textContext" is not supported prior to Internet Explorer 9...)
                       //document.getElementById("OrgMem").innerHTML = "<b>"+xmlhttpPerson.responseXML.getElementsByTagName("Org")[0].firstChild.data.htmlEncode()+"</b>";
                       var childNodes = xmlhttpPerson.responseXML.getElementsByTagName("Org")[0].childNodes;
                       var memberOrgName = ""
                       for (var j = 0; j < childNodes.length; j++)
                       {
                           memberOrgName = memberOrgName + childNodes[j].data.htmlEncode();
                       }
                       document.getElementById("OrgMem").innerHTML = "<b>"+memberOrgName+"</b>";
                   }else{
                       for (var j = 0 ; j < xmlhttpPerson.responseXML.getElementsByTagName("Org").length-1 ; j++){
                           // JIC 17:03:13 IR IR-508813-3DEXPERIENCER2018x: Replaced "firstChild.data" with loop on all nodes as Internet Explorer 10 splits text containins hyphens into several nodes (and "textContext" is not supported prior to Internet Explorer 9...)
                           //OrgString += xmlhttpPerson.responseXML.getElementsByTagName("Org")[j].firstChild.data+ ",";
                           var childNodes = xmlhttpPerson.responseXML.getElementsByTagName("Org")[j].childNodes;
                           var memberOrgName = ""
                           for (var k = 0; k < childNodes.length; k++)
                           {
                               memberOrgName = memberOrgName + childNodes[k].data.htmlEncode();
                           }
                           OrgString += memberOrgName + ",";
                       }
                       // JIC 17:03:13 IR IR-441308-3DEXPERIENCER2016x: Replaced "firstChild.data" with loop on all nodes as Internet Explorer 10 splits text containins hyphens into several nodes (and "textContext" is not supported prior to Internet Explorer 9...)
                       //OrgString += xmlhttpPerson.responseXML.getElementsByTagName("Org")[j].firstChild.data;
                       var childNodes = xmlhttpPerson.responseXML.getElementsByTagName("Org")[j].childNodes;
                       var memberOrgName = ""
                       for (var k = 0; k < childNodes.length; k++)
                       {
                           memberOrgName = memberOrgName + childNodes[k].data.htmlEncode();
                       }
                       OrgString += memberOrgName;

                       document.getElementById("OrgMem").innerHTML = "<b>"+OrgString+"</b>";
                   }
               }

                xmlreq("emxPLMOnlineAdminAjaxResponse.jsp",destRole,displayRoleMatrix,0);
            }
       }

    /**
     * 사용자 검색 시 검색된 사용자 정보 setting
     */
    function initXPPerson(hostCompany){
        host = hostCompany;
        var destPerson =  "source=Person&User="+encodeURIComponent("<%=sPersonId%>");
        xmlreq("emxPLMOnlineAdminAjaxResponse.jsp",destPerson,displayPerson,2);
        // JIC 15:04:21 Added parameters "CasualHour" and "User"
        <%--var destCasualHour = "User="+encodeURIComponent("<%=sPersonId%>");--%>

        // if (document.getElementById("Casual") != null) {
        //     destCasualHour += +"&CasualHour="+(document.getElementById("Casual").checked==true?"40":"0")
        // }
        if("<%=isLicense%>" == "true") {
            var destCasualHour = "User="+encodeURIComponent("<%=strFromUserName%>");
            xmlreq("emxPLMOnlineAdminXHRLicenseGet.jsp",destCasualHour,getLicensesResponse,1);
            var licsTable = document.getElementById("lics");
            licsTable.style.display = "block";
        }

        getTopWindow().document.querySelector('#imgProgressDiv').style.visibility = "hidden";
    }

       /**
        * 조직리스트 select box 구현
        */
    function addOrgColumn(cellOrg) {
        var xmlhttpOrg = xmlreqs[6];
        var response = xmlhttpOrg.responseXML.getElementsByTagName("org_Type");
        var responsePLMID = xmlhttpOrg.responseXML.getElementsByTagName("PLM_ExternalID");

        cellOrg.style.fontSize = "10pt";
        cellOrg.style.fontWeight = "bold";
        cellOrg.style.width = "2em";
        cellOrg.innerHTML= "<select id='FilterArea3' onchange='refreshTable()'>";

// [S] Add by SeungYong,Lee [조직리스트 HHI Engine 하위 리스트만 출력] 2020-09-14
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
// [E] Add by SeungYong,Lee [조직리스트 HHI Engine 하위 리스트만 출력] 2020-09-14
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

    // JIC 2014:10:23 Added Contractor support
    isContractor = pers.get("Contractor") != null && ((String)pers.get("Contractor")).equals("true") ? true : false;

    // JIC 2015:03:18 Added Casual Hour support
    casualHour = pers.get("CasualHour") != null ? new Integer((String)pers.get("CasualHour")).intValue() : 0;

    StringList contx = (StringList)pers.get("ctx");
    String ctxtiti = "";

    for (int j = 0; j < contx.size() ; j ++){
        String contextName = (String)contx.get(j);
        if(contextName.indexOf("VPLMAdmin."+HostCompanyName+".Default")>=0) isAdmin = true;
        if(contextName.indexOf("."+orgid+".")<0) continue;
        ctxtiti = ctxtiti + "!!" + contextName.replace("."+orgid+".",";").replaceAll(" ",",");
    }
    ctxtiti = EncodeUtil.escape(ctxtiti);
    String targ = "emdAPPRoleUpdateProcess.jsp";
    %>

         <form action="<%=targ%>" id="submitForm" name="submitForm"  method="GET">
             <textarea name="contexts" id="contexts" style="display: none;"><%=ctxtiti%></textarea>
             <textarea name="removeSelect" id="removeSelect"  style="display:none"></textarea>
             <textarea name="roleList" id="roleList" style="display: none;"></textarea>
             <div style="display:none"><input type="text" name="Source" id="Source" style="display:none" value="UpdateXP">UpdateXP</input>
             <input type="text" name="PLM_ExternalID" id="PLM_ExternalID" style="display:none" value="<%=sPersonId%>"><%=sPersonId%></input>
             <input type="text" name="fromUser" id="fromUser" style="display:none" value="">
             <input type="text" name="userType" id="userType" style="display: none;" value="<%=strUserType %>">
             <input type="text" name="isLicense" id="isLicense" style="display:none" value="<%=isLicense%>">
             </div>
            <table class="form">
            <tr>
                <td class="heading1" colspan="4" >
                <emxUtil:i18n localize="i18nId"><%=strUserTypeHeader %></emxUtil:i18n>
                </td>
            </tr>
            </table>
            <table class="form">
                      <script>
                            // JIC 2014:10:21 Added functions "refreshContractor" and "refreshVPLMAdmin" for contractor support
                            function refreshContractor() {
                                var isVPLMAdmin = document.getElementById("VPLMAdmin").checked;
                                if (isVPLMAdmin == true) {
                                    document.getElementById("Contractor").checked = false;
                                }
                            }
                            function refreshVPLMAdmin() {
                                var isContractor = document.getElementById("Contractor").checked;
                                if (isContractor == true) {
                                    document.getElementById("VPLMAdmin").checked = false;
                                }
                            }
                        </script>
                        <!-- JIC 2014:10:21 Added checkbox "Contractor" for contractor support-->
                        <tr>
                            <td class="label"> <%=Contractor%>
                                <td class="title field">
                                    <%if (isContractor){%>
                                        <input type="checkbox" id="Contractor" name="Contractor" onchange="refreshVPLMAdmin()" checked>
                                    <%}else{%>
                                        <input type="checkbox" id="Contractor" name="Contractor" onchange="refreshVPLMAdmin()">
                                    <%}%>
                                </td>
                            </td>
                        </tr>
                        <tr>
                            <td class="label"> <%=Administrator%>
                                <td class="title field">
                                    <%if (isAdmin) {%>
                                        <input type="checkbox" id="VPLMAdmin" name="VPLMAdmin" onchange="refreshContractor()" checked>
                                    <%} else {%>
                                        <input type="checkbox" id="VPLMAdmin" name="VPLMAdmin" onchange="refreshContractor()">
                                    <%}%>
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
                <emdTagUtil:displayMultiSelectUtil selectBoxName="enoviaRole" required="true"
                        inputType="checkbox"
                        showAll="false"
                        isSearchField="true" colName="emxComponents.Common.Role"
                        resourceName="emxComponentsStringResource"
                        program="emdCommonUtil:getCommonAttributeRangeList">emdEnoviaRoleCode</emdTagUtil:displayMultiSelectUtil>
                </td>
            </tr>
            </table>

<%--             License Table --%>

             <div id="lics"  class="scroll-cont" style="display: none" >
                 <div id="lics_section1_container">
                     <!-- JIC 15:03:03 Added License type -->
                     <div id="lics_section1_casual">
                         <table class="titleLic" id="lics_section1_casual_table" border="0" cellspacing="0" cellpadding="0" width="100%">
                             <colgroup>
                                 <col class="tableLicMargin" />
                                 <col class="tableLicCheckbox" />
                                 <col class="tableLicTitle" />
                             </colgroup>
                         </table>
                     </div>
                     <div id="lics_section1_w_filter" class="licHeader">
                         <table  border="0" cellspacing="0" cellpadding="0" width="100%" colspan="4*,*">
                             <tr>
                                 <td>
                                     <%
                                         addFirstLicenseSection(out);%>
                                 </td>
                                 <td >
                                     <div>
                                         <input id="lic_filter" type="text" title="<%=nlsLicSectionFilter%>" value="" onkeyup="license_filter(this,['lics_section1_table','lics_Available_table','lics_UnavailRich_table','lics_UnavailServer_table']);" />
                                     </div>
                                 </td>
                             </tr>
                         </table>
                     </div>
                     <div id="lics_section1_body" width="100%">
                         <table class="titleLic" id="lics_section1_table" border="0" cellspacing="0" cellpadding="0" width="100%">
                             <colgroup>
                                 <col class="tableLicMargin" />
                                 <col class="tableLicCheckbox" />
                                 <col class="tableLicCasual" />
                                 <col class="tableLicAvailability" />
                                 <col class="tableLicTitle" />
                             </colgroup>
                         </table>
                     </div>
                 </div>
                 <div id="lics_section0_container">
                     <div id="lics_section0" class="licHeader">
                         <table class="titleLic" border="0" cellspacing="0" cellpadding="0" width="100%">
                             <colgroup>
                                 <col class="tableLicCheckbox" />
                                 <col class="tableLicCheckbox" />
                                 <col class="tableLicTitle" />
                             </colgroup>
                             <tr>
                                 <td>
                                     <img src="images/iconSectionCollapse.gif" id="lics_section0_img">
                                 </td>
                                 <td>
                                 </td>
                                 <td>
                                     <%=nlsLicSection2%>
                                 </td>
                             </tr>
                         </table>
                     </div>
                     <div id="lics_section0_body" width="100%">
                         <table class="titleLic" id="lics_section0_table" border="0" cellspacing="0" cellpadding="0" width="100%">
                             <colgroup>
                                 <col class="tableLicMargin" />
                                 <col class="tableLicCheckbox" />
                                 <col class="tableLicCasual" />
                                 <col class="tableLicAvailability" />
                                 <col class="tableLicTitle" />
                             </colgroup>
                             <tr>
                                 <td> </td>
                                 <td> </td>
                                 <td> </td>
                                 <td>
                                     <img src="images/iconParamProgress.gif">
                                 </td>
                             </tr>
                         </table>
                     </div>
                 </div>
                 <div id="lics_section3_container" style="display:none;">
                 </div>
             </div>
<%--             License Table --%>

            <textarea  name="HiddenElement" id="HiddenElement" style="display:none"></textarea>

            <textarea  name="removeRole" id="removeRole" style="display: none;"></textarea>
            <textarea  name="addRole" id="addRole" style="display: none;"></textarea>
        </form>
</body>
</html>

<script type="text/javascript">

    /*******************************************
     * Enovia Role  추가, 권한 항목을 저장함.
     *******************************************/
    jQuery("input[name='enoviaRole']").on('click', function (){
        var target = event.target;
        var found = false;
        var txt = document.getElementById("roleList").innerHTML;
        if(txt!=null && txt!="" && txt.indexOf(target.value)>-1) {
            found = true;
            var removeRole = document.getElementById("removeRole").innerHTML;
            if (target.checked==false && removeRole.indexOf(target.value)==-1) {
                removeRole = removeRole + "!!"+target.value;
            } else if(target.checked==true && removeRole.indexOf(target.value)>-1) {
                removeRole = ReplaceAllOccurence(removeRole,"!!"+target.value,"");
            }
            document.getElementById("removeRole").innerHTML = removeRole;
        }
        if (found==false) {
            var addRole = document.getElementById("addRole").innerHTML;
            if (target.checked==true && addRole.indexOf(target.value)==-1) {
                addRole = addRole + "!!"+target.value;
            } else if(target.checked==false && addRole.indexOf(target.value)>-1) {
                addRole = ReplaceAllOccurence(addRole,"!!"+target.value,"");
            }
            document.getElementById("addRole").innerHTML = addRole;
        }
    });

    /*******************************************
     * Enovia Role이 Person에 존재하면 Checked
     *******************************************/
    function selectedRole() {
        var vRoleList = new Array();
        var roleArea = document.getElementById('roleList');
        var roles = "";
        <%for (int i = 0 , size = slEnoRoleList.size(); size > i; i++){%>
            vRoleList[<%=i%>]= '<%=slEnoRoleList.get(i)%>';
            roles += '<%=slEnoRoleList.get(i) + ","%>';
        <%}%>

        roleArea.innerHTML = roles.substring(0, roles.length -1);

        var roleTable = document.getElementById('RoleTable');
        var roleCheckBoxList = roleTable.getElementsByTagName('input');
        for(var i = 0; roleCheckBoxList.length > i; i ++) {
            var roleCheckbox = roleCheckBoxList[i];
            for(var j = 0; vRoleList.length > j ; j++){
                var role = vRoleList[j];
                if(role == roleCheckbox.value) {
                    roleCheckbox.checked = true;
                }
            }
        }
    }
selectedRole();

</script>