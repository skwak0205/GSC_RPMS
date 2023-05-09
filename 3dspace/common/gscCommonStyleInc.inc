<%-----------------------------------------------------------------------------
* FILE    : emdCommonStyleInc.inc
* DESC    : Common Directory에서 사용할 style , emxUtil Tag 설정
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
* 2020-08-28     SeungYong,Lee   최초 생성
------------------------------------------------------------------------------%>
<%@include file = "../common/emxNavigatorInclude.inc"%>
<script language="javascript" src="../common/scripts/emxUIConstants.js"></script>
<script language="javascript" src="../common/scripts/emxUICore.js"></script>

<%
  //Use only if Not a Dialog Page
  String isDialogPage = emxGetParameter(request,"contentPageIsDialog");
  if (!"true".equalsIgnoreCase(isDialogPage)){
      
%>
<script type="text/javascript" language="javascript">
  addStyleSheet("emxUIProperties","../common/styles/");
</script>
<%
  }
%>
<script type="text/javascript" language="javascript">
<%
  String showPFPage = emxGetParameter(request,"PrinterFriendly");
  if ("true".equals(showPFPage)){
%>
            addStyleSheet("emxUIDefaultPF","../common/styles/");
            addStyleSheet("emxUIListPF","../common/styles/");
            addStyleSheet("emxUIPropertiesPF","../common/styles/");
<%
  } else {
%>
            addStyleSheet("emxUIDefault","../common/styles/");
            addStyleSheet("emxUIList","../common/styles/");
            addStyleSheet("emxUIForm","../common/styles/");          
<%
  }
%>

    addStyleSheet("emxUIMenu","../common/styles/");
</script>

<script type="text/javascript" language="javascript" src="../common/scripts/jquery-latest.js"></script>
<script language="javascript" src="../common/scripts/jquery-ui.js"></script>
<script language="javascript" src="../enterprisechangemgtapp/scripts/emdUISearchUtil.js"></script>
<script language="javascript" src="../common/scripts/emxUICoreMenu.js"></script>
<script language="javascript" src="../common/scripts/emxUICalendar.js"></script>
<%@include file="../common/emdCommonValidation.inc"%>

<%@page import="com.emd.apps.common.util.emdStringUtil"%>
<%@page import="com.emd.apps.common.util.emdConvertUtil"%>

<%@include file = "../emxTagLibInclude.inc"%>
<emxUtil:localize id="i18nId" bundle="emxComponentsStringResource" locale='<%= request.getHeader("Accept-Language") %>'/>
