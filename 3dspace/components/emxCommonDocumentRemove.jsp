<%--  emxComponentUnlockDocument.jsp   -

   Copyright (c) 1992-2020 Dassault Systemes.
   All Rights Reserved.
   This program contains proprietary and trade secret information of MatrixOne,
   Inc.  Copyright notice is precautionary only
   and does not evidence any actual or intended publication of such program

   static const char RCSID[] = $Id: emxCommonDocumentRemove.jsp.rca 1.14 Wed Oct 22 16:18:22 2008 przemek Experimental przemek $
--%>

<%@include file = "../emxUICommonAppInclude.inc"%>
<%@include file = "emxComponentsUtil.inc"%>
<%@include file = "../common/enoviaCSRFTokenValidation.inc"%>

<%
  String[] oids = emxGetParameterValues(request, "emxTableRowId");
try
{
  //get document Id
  Map objectMap = UIUtil.parseRelAndObjectIds(context, oids, false);
  oids = (String[])objectMap.get("objectIds");
  String[] relIds = (String[])objectMap.get("relIds");
  String action = emxGetParameter(request, "action");
  String objectId = emxGetParameter(request, "objectId");
  String errorMessage = ComponentsUtil.i18nStringNow("emxCompoments.Document.CannotDelete", sLanguage);

  if ( "disconnect".equals(action) )
  {
      //Added for the Bug 314495 Begin
      boolean contextPushed = false;
      try
      {
          CommonDocument.removeDocuments(context, relIds, false);
      }
      catch (Exception exp)
      {
        try
        {
            //Added for the Bug 314495 Begin
            DomainObject commonRt2 = DomainObject.newInstance(context,objectId);
            Access contextAccess = commonRt2.getAccessMask(context);
            if(contextAccess.hasToConnectAccess())
            {
                ContextUtil.pushContext(context);
                contextPushed = true;
                CommonDocument.removeDocuments(context, relIds, false);
            } else {
                 throw new FrameworkException(exp);
            }
         } catch(Exception ex) {
                throw new FrameworkException(ex);
         }
         finally
         {
            if( contextPushed)
              ContextUtil.popContext(context);
            }
          }

  } else if ("delete".equalsIgnoreCase(action) )
  {
      CommonDocument.deleteDocuments(context, oids);
  } else if ("deleteVersion".equalsIgnoreCase(action) )
  {
      CommonDocument commonDocument = (CommonDocument)DomainObject.newInstance(context,objectId);
      commonDocument.deleteVersion(context, oids, false);
  } else if ("deleteFile".equalsIgnoreCase(action) )
  {
      CommonDocument commonDocument = (CommonDocument)DomainObject.newInstance(context,objectId);
      commonDocument.deleteVersion(context, oids, true);
  } else if ("deleteNonVersionableFile".equalsIgnoreCase(action) ) {
		ContextUtil.startTransaction(context, true);
      for (int i=0;i<oids.length;i++){
          String oid = oids[i];
          StringList oidFileFormat = FrameworkUtil.split(oid, "~");
          if ( oidFileFormat.size() == 3 )
          {
              oid = (String)oidFileFormat.get(0);
              String fileName =(String) oidFileFormat.get(1);
              String format = (String) oidFileFormat.get(2);
              if(format.contains("|")){
            	  StringList formatArr =  FrameworkUtil.split(format,"\\|");
                  format = (String)formatArr.get(0);
                  }
              CommonDocument commonDocument = (CommonDocument)DomainObject.newInstance(context,oid);
              if(commonDocument.isLocked(context))
            	  throw new Exception(errorMessage);
              commonDocument.deleteFile(context, fileName, format);
          }
      }
      ContextUtil.commitTransaction(context);
  }

} catch (Exception ex)
{
    String message = "";
    
    if (  ex.getMessage().indexOf("delete")>0){
		message=EnoviaResourceBundle.getProperty(context, "emxComponentsStringResource", context.getLocale(), "emxCompoments.Document.CannotDelete");
	}  else if(ex.getMessage().indexOf("fromdisconnect") >0 || ex.getMessage().indexOf("todisconnect") >0) {
		message = EnoviaResourceBundle.getProperty(context, "emxComponentsStringResource", context.getLocale(), "emxComponents.Common.DisconnectObjectFailed");
	} else {
		message = ex.getMessage();
	}
    session.setAttribute("error.message" , message);
}

%>
<html>
<body>
<script language="JavaScript" src="../common/scripts/emxUIConstants.js" type="text/javascript"></script>
<script language="JavaScript" src="../common/scripts/emxUICore.js" type="text/javascript"></script>
<script language="Javascript" >
if(getTopWindow().getWindowOpener() && getTopWindow().getWindowOpener().getTopWindow().RefreshHeader){
    getTopWindow().getWindowOpener().getTopWindow().RefreshHeader();
}else if(getTopWindow().RefreshHeader){
  	 getTopWindow().RefreshHeader();
	}
    var frameContent = openerFindFrame(getTopWindow(),"detailsDisplay");
	 if(frameContent !=null){
		 var url = frameContent.document.location.href;
		 var replaceURL=url.replace("persist=true", "persist=false");
		frameContent.document.location.href= replaceURL;
	 }else{
		  var url = parent.document.location.href;
		  var replaceURL=url.replace("persist=true", "persist=false");
		  parent.document.location.href = replaceURL;
	 }
	
</script>

</body>
</html>
