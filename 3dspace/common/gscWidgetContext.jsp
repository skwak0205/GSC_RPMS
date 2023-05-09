<%@ page import="com.matrixone.apps.domain.util.EnoviaResourceBundle" %>
<%--  emxFrameworkContextManager.jsp - Establish context and forward request.

   Copyright (c) 1992-2020 Dassault Systemes.
   All Rights Reserved.
   This program contains proprietary and trade secret information of MatrixOne,
   Inc.  Copyright notice is precautionary only
   and does not evidence any actual or intended publication of such program

--%>

<%@include file = "../emxRequestWrapperMethods.inc"%>

<%!
  //static variables.
  String SECURITY_CONTEXT = "SecurityContext";  //passed from 6W Foundation as part of every URL.
%>

<%
  try
  {
    Framework.isLoggedIn(request);
    Context context = Framework.getFrameContext(session);
    if (context != null) {
      session.setAttribute("emxSessionExist", Boolean.valueOf(true));  //required for downstream BPS code.
      session.setAttribute("timeZone", "9");  //required for SB.

      PersonUtil personUtil = new PersonUtil();
      String defaultSecurityContext = personUtil.getDefaultSecurityContext(context);

      if (defaultSecurityContext != null) {
        PersonUtil.setSecurityContext(session, defaultSecurityContext);
        //PersonUtil.setDefaultSecurityContext(context, defaultSecurityContext);
      }
      String widgetUrl = EnoviaResourceBundle.getProperty(context, "emxFramework.WIDGET_URL");
      String to = emxGetParameter(request, "to");
      if(to == null || "".equals(to)) {
        to = "";
      }
      String forwardUrl = widgetUrl +  to;
      out.clear();
%>
<script type="text/javascript" language="JavaScript" src="../common/scripts/emxUICore.js"></script>
<script>
  var forwardURL = encodeAllHREFParameters("<%=forwardUrl%>");
  window.location.href=forwardURL;
</script>
<%
  } else {
    //it should never reach here the way this JSP page is used.
    System.out.println("emxFrameworkContextManager - user is not logged in or system is not configured with DS Passport.");
    out.clear();
%>
Unexpected error has occurred.  Please ensure you are logged in and refresh the widget or browser.
<%
    }
  } catch (Exception e) {
    e.printStackTrace();
    }
%>
