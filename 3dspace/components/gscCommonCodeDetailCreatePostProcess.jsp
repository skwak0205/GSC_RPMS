<%-----------------------------------------------------------------------------
* FILE    : gscCommonCodeDetailCreatePostProcess.jsp
* DESC    : 공통코드 Detail 생성후 Table 새로 고침 JSP
* VER.    : 1.0
* AUTHOR  : BongJun,Park
* PROJECT : HiMSEN Engine Project
*
* Copyright 2018 by ENOVIA All rights reserved.
* -----------------------------------------------------------------
*                         Revision history
* -----------------------------------------------------------------
* Since          Author         Description
* ------------   ------------   -----------------------------------
* 2020-06-10     BongJun,Park    최초 생성
------------------------------------------------------------------------------%>

<%@ page import="com.matrixone.apps.domain.DomainObject" %>

<%@include file="../common/emxNavigatorInclude.inc" %>
<%@include file="../common/emxNavigatorTopErrorInclude.inc" %>
<%@include file="../emxUICommonHeaderBeginInclude.inc" %>

<script language="Javascript">
    top.opener.location.href = top.opener.location.href;
    window.top.close();
</script>

<%@include file="../common/emxNavigatorBottomErrorInclude.inc" %>
