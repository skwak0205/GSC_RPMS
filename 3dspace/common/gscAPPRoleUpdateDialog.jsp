<%-----------------------------------------------------------------------------
* FILE    : gscAPPRoleUpdateDialog.jsp
* DESC    : Role Update Dialog Page
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
<%@include file="../emxContentTypeInclude.inc"%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
    <div style="width: 100%; height: 45px;">
        <iframe name="iPersonFrame" id="iPersonFrame" scrolling="no" src="gscAPPRoleSearchPerson.jsp" frameborder="0" align="rigth" style="width: 100%;"></iframe>
    </div>
    <div style="width: 100%; height: 550px;">
    <iframe name="iDataFrame" id="iDataFrame" scrolling="no" src="gscAPPRoleUpdateDefaultTable.jsp" frameborder="0" align="rigth" style="width: 100%; height: 550px;"></iframe>
    </div>
</body>
</html>
