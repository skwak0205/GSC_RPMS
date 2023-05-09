<%-- emxComponentSearchRolesDialog.jsp --

  Copyright (c) 1992-2020 Dassault Systemes.
  All Rights Reserved.
  This program contains proprietary and trade secret information of MatrixOne, Inc.
  Copyright notice is precautionary only and does not evidence any actual or intended publication of such program

   static const char RCSID[] = $Id: emxComponentsSearchRolesDialog.jsp.rca 1.9 Wed Oct 22 16:18:42 2008 przemek Experimental przemek $ 
--%>

<%@include file = "emxComponentsDesignTopInclude.inc"%>
<%@include file = "emxComponentsVisiblePageInclude.inc"%>

<%@include file = "../emxUICommonHeaderBeginInclude.inc" %>

<script language="javascript">

  function doSearch() {
    if(jsIsClicked()) {
        alert("<emxUtil:i18nScript  localize="i18nId">emxComponents.Search.RequestProcessMessage</emxUtil:i18nScript>");
        return;
    }
    if ( document.searchtemplate.txtName.value == "" || document.searchtemplate.txtName.value == null ) {
		alert("<emxUtil:i18nScript  localize="i18nId">emxComponents.SearchRole.EnterName</emxUtil:i18nScript>");
	    document.searchtemplate.txtName.focus();
	    return;
    } else {
     	//var strQueryLimit = parent.frames[3].document.bottomCommonForm.QueryLimit.value;
     	var strQueryLimit = parent.document.bottomCommonForm.QueryLimit.value;
     	document.searchtemplate.queryLimit.value = strQueryLimit;
      
      	if(jsDblClick()) 	{
        	startSearchProgressBar();
        	document.searchtemplate.submit();
      	}
      	return;
     }
  }

  function closeWindow() {
    window.closeWindow();
    return;
  }
  
</script>

<%@include file = "../emxUICommonHeaderEndInclude.inc" %>

<%
  // ------------------------ Page content below here  ---------------------
  String objectId         = emxGetParameter(request, "objectId");
  String loginName        = emxGetParameter(request,"loginName");
  String password         = emxGetParameter(request,"password");
  String confirmpassword  = emxGetParameter(request,"confirmpassword");
  String firstName        = emxGetParameter(request,"firstName");
  String middleName       = emxGetParameter(request,"middleName");
  String lastName         = emxGetParameter(request,"lastName");
  String companyName      = emxGetParameter(request,"companyName");
  String location         = emxGetParameter(request,"location");
  String workPhoneNumber  = emxGetParameter(request,"workPhoneNumber");
  String homePhoneNumber  = emxGetParameter(request,"homePhoneNumber");
  String pagerNumber      = emxGetParameter(request,"pagerNumber");
  String emailAddress     = emxGetParameter(request,"emailAddress");
  String faxNumber        = emxGetParameter(request,"faxNumber");
  String webSite          = emxGetParameter(request,"webSite");
  String mainSearchPage   = emxGetParameter(request,"mainSearchPage");
  String targetSearchPage = emxGetParameter(request,"targetSearchPage");
  
  String strOrgId         = emxGetParameter(request,"orgId");
  if(strOrgId == null || "null".equals(strOrgId)) {
    strOrgId = "";
  }

  // Only one line added - Nishchal
  String keyValue = emxGetParameter(request,"keyValue");

  String sName = emxGetParameter(request,"txtName");
  String sTopChecked = emxGetParameter(request,"chkTopLevel");
  String sSubChecked = emxGetParameter(request,"chkSubLevel");

  String sRoleList     = emxGetParameter(request, "roleList");

  if (sRoleList == null){
    sRoleList="";
  }

  if (sName==null){
    sName = "*";
  }
%>

<form name="searchtemplate" id="searchtemplate" method="post" onSubmit="doSearch(); return false;" target=_parent action="emxComponentsSearchRolesResultsFS.jsp">
  <input type="hidden" name="loginName" value="<xss:encodeForHTMLAttribute><%= emxGetParameter(request,"loginName") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="password"  value="<xss:encodeForHTMLAttribute><%= emxGetParameter(request,"password") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="confirmpassword" value="<xss:encodeForHTMLAttribute><%= emxGetParameter(request,"confirmpassword") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="firstName" value="<xss:encodeForHTMLAttribute><%= emxGetParameter(request,"firstName") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="middleName"value="<xss:encodeForHTMLAttribute><%= emxGetParameter(request,"middleName") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="lastName"  value="<xss:encodeForHTMLAttribute><%= emxGetParameter(request,"lastName") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="companyName" value="<xss:encodeForHTMLAttribute><%= emxGetParameter(request,"companyName") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="location" value="<xss:encodeForHTMLAttribute><%= emxGetParameter(request,"location") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="workPhoneNumber" value="<xss:encodeForHTMLAttribute><%= emxGetParameter(request,"workPhoneNumber") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="homePhoneNumber" value="<xss:encodeForHTMLAttribute><%= emxGetParameter(request,"homePhoneNumber") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="pagerNumber" value="<xss:encodeForHTMLAttribute><%= emxGetParameter(request,"pagerNumber") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="emailAddress" value="<xss:encodeForHTMLAttribute><%= emxGetParameter(request,"emailAddress") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="faxNumber" value="<xss:encodeForHTMLAttribute><%= emxGetParameter(request,"faxNumber") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="webSite" value="<xss:encodeForHTMLAttribute><%= emxGetParameter(request,"webSite") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="businessUnit" value="<xss:encodeForHTMLAttribute><%= emxGetParameter(request,"businessUnit") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="callPage" value="<xss:encodeForHTMLAttribute><%=emxGetParameter(request, "callPage") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="form" value="<xss:encodeForHTMLAttribute><%=emxGetParameter(request, "form") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="field" value="<xss:encodeForHTMLAttribute><%=emxGetParameter(request, "field") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="fieldDisp" value="<xss:encodeForHTMLAttribute><%=emxGetParameter(request, "fieldDisp") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="multiSelect" value="<xss:encodeForHTMLAttribute><%=emxGetParameter(request, "multiSelect") %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="objectId" value="<xss:encodeForHTMLAttribute><%= objectId %></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="roleList" value="<xss:encodeForHTMLAttribute><%=sRoleList%></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="mainSearchPage" value="<xss:encodeForHTMLAttribute><%=mainSearchPage%></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="targetSearchPage" value="<xss:encodeForHTMLAttribute><%=targetSearchPage%></xss:encodeForHTMLAttribute>" />
  <input type="hidden" name="orgId" value="<xss:encodeForHTMLAttribute><%=strOrgId%></xss:encodeForHTMLAttribute>" />

  <!-- Nishchal  Added KeyValue -->
  <input type="hidden" name="keyValue" value="<xss:encodeForHTMLAttribute><%= keyValue %></xss:encodeForHTMLAttribute>" />
  
  <input type=hidden name="queryLimit" value="">

  <table border="0" cellpadding="5" cellspacing="2" width="530">
    <tr>
      <td class="label">
        <emxUtil:i18n localize="i18nId">emxComponents.SearchRole.Type</emxUtil:i18n>
      </td>
      <td class="inputField"><img src="../common/images/iconSmallRole.gif" alt="" name="role" id="role" border="0"/>
        &nbsp;<emxUtil:i18n localize="i18nId">emxComponents.SearchRole.Role</emxUtil:i18n>
      </td>
    </tr>
    <tr>
      <td class="label">
        <emxUtil:i18n localize="i18nId">emxComponents.Common.Name</emxUtil:i18n>
      </td>
      <td class="inputField">
        <input type="text" name="txtName" id="txtName" value="<xss:encodeForHTMLAttribute><%=sName%></xss:encodeForHTMLAttribute>"/>
      </td>
    </tr>
    <tr>
      <td class="label">
        <emxUtil:i18n localize="i18nId">emxComponents.SearchRole.Level</emxUtil:i18n>
      </td>
      <td class="inputField">
        <table>
          <tr><td>
            <input type="checkbox" name="chkTopLevel" id="chkTopLevel" value="Checked" <%=XSSUtil.encodeForHTMLAttribute(context, sTopChecked)%>/><emxUtil:i18n localize="i18nId">emxComponents.SearchRole.TopLevel</emxUtil:i18n>
          </td></tr>
          <tr><td>
            <input type="checkbox" name="chkSubLevel" id="chkSubLevel" value="Checked" <%=XSSUtil.encodeForHTMLAttribute(context, sSubChecked)%>/><emxUtil:i18n localize="i18nId">emxComponents.SearchRole.SubLevel</emxUtil:i18n>
          </td></tr>
        </table>
      </td>
    </tr>
  </table>
</form>
<%@include file = "../emxUICommonEndOfPageInclude.inc" %>
