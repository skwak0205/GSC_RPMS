<%--  emxProgramCentraFolderUtil.jsp

   Copyright (c) 1999-2020 Dassault Systemes.
   All Rights Reserved.
   This program contains proprietary and trade secret information of MatrixOne,Inc.
   Copyright notice is precautionary only
   and does not evidence any actual or intended publication of such program

   static const char RCSID[] = $Id: emxProgramCentraFolderUtil.jsp.rca 1.1.2.1.3.1 Wed Dec 24 12:59:09 2008 rdonahue Experimental $

 --%>

<%@include file="../emxUIFramesetUtil.inc"%>
<%@include file= "emxProgramCentralCommonUtilAppInclude.inc"%>

<%@page import="com.matrixone.apps.domain.util.MqlUtil"%>
<%@page import="com.matrixone.apps.domain.DomainObject"%>
<%@page import="matrix.db.Context"%>
<%@page import="com.matrixone.apps.domain.DomainConstants"%>
<%@page import="com.matrixone.apps.program.ProgramCentralUtil"%>

<%@page import="java.util.Iterator"%>
<%@page import="com.matrixone.apps.domain.DomainRelationship"%>
<%@page import="com.matrixone.apps.common.CommonDocument"%>
<%@page import="com.matrixone.apps.program.ProgramCentralConstants"%>
<%@page import="matrix.util.StringList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="com.matrixone.apps.domain.util.EnoviaResourceBundle"%>
<%@page import="com.matrixone.apps.domain.util.XSSUtil"%>
<html>
<%!

private static  List<String> getDocumentIdList(Map requestMap,StringList documentTitleList ,StringList documentIdList) {
    List<String> addedDocumentIdList =   new ArrayList(5);
    
    for(int i=0;i<5;i++) {
        int fileIndex          = -1;
        String addedDocumentId = null;
        String fileName   =   (String)requestMap.get("fileName"+i);
        fileIndex         =   documentTitleList.indexOf(fileName);
        if (fileIndex>=0) {
            addedDocumentId  =   (String)documentIdList.get(fileIndex);
            addedDocumentIdList.add(addedDocumentId);
        }
    }
    return addedDocumentIdList;
}
%>

<%
String sMode =  emxGetParameter(request, "actionMode");
	if(ProgramCentralUtil.isNotNullString(sMode) && sMode.indexOf("?") > 0){
		sMode = sMode.substring(0, sMode.indexOf("?"));
	}
	Map mEmxCommonDocumentCheckinData = (Map) session.getAttribute("emxCommonDocumentCheckinData");
	String sParentOID      = emxGetParameter(request, "parentOID");
	String sObjectId      = emxGetParameter(request, "objectId");
 	sObjectId = XSSUtil.encodeURLForServer(context, sObjectId); 	
	String sDocumentAction = "";
 	String currentFrame =emxGetParameter(request, "portalCmdName");
 	currentFrame = XSSUtil.encodeURLForServer(context, currentFrame);
 %>

<script language="javascript" src="../common/scripts/emxUIConstants.js"></script>
<script src="../common/scripts/emxUICore.js" type="text/javascript"></script>
<script src="../common/scripts/emxUIModal.js" type="text/javascript"></script>

<script type="text/javascript" language="javascript">

<%
    if(ProgramCentralUtil.isNullString(sMode) && null != mEmxCommonDocumentCheckinData)
	{
		   sMode = (String)mEmxCommonDocumentCheckinData.get("actionMode");
           mEmxCommonDocumentCheckinData.remove("actionMode");
	}

if("addNewFile".equalsIgnoreCase(sMode)) {
    Map requestMap = (Map) session.getAttribute("emxCommonDocumentCheckinData");
    Map mObjectMap = (Map)request.getAttribute("objectMap");
   
    String documentObjectId = null;
    String pasteBelowToRow = null;
    if (requestMap != null) {
        documentObjectId   =   (String)requestMap.get("objectId");
    }
    documentObjectId = XSSUtil.encodeURLForServer(context, documentObjectId);
    if(ProgramCentralUtil.isNotNullString(documentObjectId)) {
        DomainObject dDocumentObj = DomainObject.newInstance(context);
        dDocumentObj.setId(documentObjectId);

        final String documentRelationshipIdKey = "from["+CommonDocument.RELATIONSHIP_ACTIVE_VERSION+"].id";
        final String documentIdKey = "from["+CommonDocument.RELATIONSHIP_ACTIVE_VERSION+"].to.id";
        final String documentTitleKey = "from["+CommonDocument.RELATIONSHIP_ACTIVE_VERSION+"].to."+ProgramCentralConstants.SELECT_ATTRIBUTE_TITLE;

        StringList slDocumentSelects = new StringList();
        slDocumentSelects.add(documentRelationshipIdKey);
        slDocumentSelects.add(documentIdKey);
        slDocumentSelects.add(documentTitleKey);
        Map documentInfo = dDocumentObj.getInfo(context,slDocumentSelects);

        if(documentInfo != null) {               
            String documentRelationshipId = (String)documentInfo.get(documentRelationshipIdKey);
            StringList documentIdList = (StringList)documentInfo.get(documentIdKey);
            StringList documentTitleList = (StringList)documentInfo.get(documentTitleKey);
            
            List<String> addedDocumentIdList  =   getDocumentIdList(requestMap,documentTitleList,documentIdList);
                            
            if(!addedDocumentIdList.isEmpty() && ProgramCentralUtil.isNotNullString(documentRelationshipId)) {
                if(ProgramCentralUtil.isNullString(pasteBelowToRow)) {
                	int size = documentIdList.size();
                	size = size-2;
                    pasteBelowToRow="0";
                }
                StringBuffer partialXML   =   new StringBuffer();
                for(int i=0;i<addedDocumentIdList.size();i++) {
                     
                    partialXML.append("<item oid=\"" + addedDocumentIdList.get(i)); 
                    partialXML.append("\" relId=\"" + documentRelationshipId);
                    partialXML.append("\" pid=\"" + documentObjectId); 
                    partialXML.append("\" direction=\"\" pasteBelowToRow=\"" + pasteBelowToRow + "\" />");
                }

                boolean isFromRMB = "true".equalsIgnoreCase((String)requestMap.get("isFromRMB"));
                String fromRMB = isFromRMB?"true":"";
                
                 String xmlMessage = "<mxRoot>" +"<action><![CDATA[add]]></action>" + "<data status=\"committed\" fromRMB=\"" + fromRMB + "\"" + "pasteBelowOrAbove=\"true\">";
                 xmlMessage += partialXML.toString();
                 xmlMessage += "</data></mxRoot>";
%>
               var topFrame = findFrame(getTopWindow().opener.getTopWindow(), "detailsDisplay");
			   if (null==topFrame || (null!=topFrame && typeof topFrame.emxEditableTable == 'undefined')){
                     	topFrame=this.parent.parent.opener;
               }
               var parentObjectId = '<%=documentObjectId%>';
               var xmlRef 	= topFrame.oXML;
   			   var allRows = emxUICore.selectNodes(xmlRef, "/mxRoot/rows//r");
				   var parentRowID = new Array();
			   for (var i = 0; i < allRows.length; i++) {
			       var levelId = allRows[i].getAttribute("id");
			       var oldRow = emxUICore.selectSingleNode(xmlRef, "/mxRoot/rows//r[@id = '" + levelId + "']");
			       var oId = oldRow.getAttribute("o");
				         
				   if(parentObjectId == oId){
					   parentRowID.push(levelId);
				   }
			   }
			    topFrame.emxEditableTable.expand(parentRowID, "1");
                //topFrame.refreshStructureWithOutSort();
               <%
            }
        }
    }        
}
else if("checkout".equalsIgnoreCase(sMode)) {
    	Map emxCommonDocumentCheckoutData = (Map) session.getAttribute("emxCommonDocumentCheckoutData");
    	String objectId = (String) emxCommonDocumentCheckoutData.get("objectId");
    	if(objectId == null){
    		objectId = (String) emxCommonDocumentCheckoutData.get("checkoutObjectId");
    	}
    	String oldOid      = emxGetParameter(request, "oid0");
    	 
 	    DomainObject document = DomainObject.newInstance(context, objectId);
 	    
 	    //LSA - getting the currentFrame from the appName parameter
 	    currentFrame = (String) emxCommonDocumentCheckoutData.get("appName");
 	    if(emxCommonDocumentCheckoutData.containsKey("portalCmdName")){
 	    	currentFrame = (String) emxCommonDocumentCheckoutData.get("portalCmdName");
 	    }
 	    %>
 	    
 	    var detailsDisplayFrame = findFrame(getTopWindow(), "detailsDisplay");
 	    if (!detailsDisplayFrame) {
	 	    if (getTopWindow().opener) {
	 	    	detailsDisplayFrame = findFrame(getTopWindow().opener.getTopWindow(), "detailsDisplay");
	 	    }
	 	    else {
	 	    	detailsDisplayFrame = null;
	 	    }
 	    }
 	    if (detailsDisplayFrame) {
	 	    if (typeof detailsDisplayFrame.emxEditableTable == 'undefined'){
	 	    	detailsDisplayFrame = findFrame(getTopWindow(), "<%=currentFrame%>");
	 	    }
	 	    var pid = "<%=objectId%>";
	 	    var nRow = new Array();
	 	    nRow = emxUICore.selectNodes(detailsDisplayFrame.oXML, "/mxRoot/rows//r[@o = '" + pid + "']");
	 	    for(var rw=0;rw<nRow.length;rw++){
	 	
	 	    nRow[rw].setAttribute("expand", false);
	 	    nRow[rw].setAttribute("expandedLevels", 0);
	 	
	 	    var pidRowId =nRow[rw].getAttribute("id");
	 	
	 	    var rowIdArr = new Array();
	 	    rowIdArr.push(pidRowId);
	 	
	 	    detailsDisplayFrame.emxEditableTable.expand(rowIdArr, "1");
	 	    detailsDisplayFrame.emxEditableTable.refreshRowByRowId(rowIdArr);
	 	    }
     	}
    <%
    }
    else if("documentLock".equalsIgnoreCase(sMode) || "documentUnlock".equalsIgnoreCase(sMode)) {
		
       //Should be file object.
        DomainObject documentFile   = DomainObject.newInstance(context, sObjectId);
        String docObjId          	= documentFile.getInfo(context, "to[Latest Version].from.id");

        %>
        var topFrame = findFrame(getTopWindow(), "detailsDisplay");
        if (typeof topFrame.emxEditableTable == 'undefined'){
            topFrame=this.parent;
        }
        var fileOid = "<%=sObjectId%>";
        var docOid = "<%=docObjId%>";
      	//var nRow = new Array();
      	//var fileRow = emxUICore.selectSingleNode(topFrame.oXML, "/mxRoot/rows//r[@o = '" + fileOid + "']");
      	//var docRow = emxUICore.selectSingleNode(topFrame.oXML, "/mxRoot/rows//r[@o = '" + docOid + "']");
		
		var fileRow = emxUICore.selectNodes(topFrame.oXML, "/mxRoot/rows//r[@o = '" + fileOid + "']");
      	var docRow = emxUICore.selectNodes(topFrame.oXML, "/mxRoot/rows//r[@o = '" + docOid + "']");
      
      	var rowIdArr = new Array();
        //rowIdArr.push(fileRow.getAttribute("id"));
        //rowIdArr.push(docRow.getAttribute("id"));
		
		for(var fileRowItr=0; fileRowItr<fileRow.length; fileRowItr++){
			rowIdArr.push(fileRow[fileRowItr].getAttribute("id"));
		}
		
		for(var docRowItr=0; docRowItr<docRow.length; docRowItr++){
			rowIdArr.push(docRow[docRowItr].getAttribute("id"));
		}
        
        topFrame.emxEditableTable.refreshRowByRowId(rowIdArr);
		
        <%
    }
    else if("createDocument".equalsIgnoreCase(sMode) || "uploadFiles".equalsIgnoreCase(sMode)) {
        String sNewDocumentObjectId = null;
        String pasteBelowToRow = null;
        String sParsedParentId = null;
        Map mRequestMap = (Map)request.getAttribute("requestMap");
        Map mObjectMap = (Map)request.getAttribute("objectMap");

        if(null != mRequestMap)
        {
        	String sEmxTableRowId = (String)mRequestMap.get("emxTableRowId");
        	if(ProgramCentralUtil.isNotNullString(sEmxTableRowId))
        	{
	        	Map mParsedRowIds =  ProgramCentralUtil.parseTableRowId(context,sEmxTableRowId);
	        	if(null != mParsedRowIds)
	        	{
	        		sParsedParentId = (String)mParsedRowIds.get("objectId");
	        	    pasteBelowToRow = (String) mParsedRowIds.get("rowId");
	        	}
        	}
        	else
        	{
        		sParsedParentId = (String)mRequestMap.get("parentId");
        	}
        }
        if(null != mObjectMap)
        {
        	Object obj = mObjectMap.get("objectId");
        	if(obj instanceof StringList)
        	{
        		   StringList slNewObjId = (StringList)obj;
        		   if(slNewObjId.size() > 0)
        		   {
        			   sNewDocumentObjectId = (String)slNewObjId.get(0);
        		   }
        	}
        	else if(obj instanceof String)
        	{
        		sNewDocumentObjectId = (String) obj;
        	}
        }

        if(ProgramCentralUtil.isNotNullString(sNewDocumentObjectId))
        {
        	DomainObject dDocumentObj = DomainObject.newInstance(context);
        	dDocumentObj.setId(sNewDocumentObjectId);

            final String saRelId = "to["+DomainConstants.RELATIONSHIP_VAULTED_OBJECTS_REV2+"].id";
            StringList slDocumentSelects = new StringList();
            slDocumentSelects.add(saRelId);
            Map mDocumentInfo = dDocumentObj.getInfo(context,slDocumentSelects);

            if(null != mDocumentInfo)
            {
            	String sNewRelId = (String)mDocumentInfo.get(saRelId);
            	if(ProgramCentralUtil.isNotNullString(sNewRelId) && ProgramCentralUtil.isNotNullString(sParsedParentId))
            	{
            		if(ProgramCentralUtil.isNullString(pasteBelowToRow))
            	    {
            	        pasteBelowToRow="0";
            	    }

            		boolean isFromRMB = "true".equalsIgnoreCase((String)mRequestMap.get("isFromRMB"));
            	    String fromRMB = isFromRMB?"true":"";

                    String xmlMessage = "<mxRoot>" +
            	        "<action><![CDATA[add]]></action>" +
            	        "<data status=\"committed\" fromRMB=\"" + fromRMB + "\"" + " >" +
            	        "<item oid=\"" + sNewDocumentObjectId + "\" relId=\"" + sNewRelId + "\" pid=\"" + sParsedParentId + "\" direction=\"\" pasteBelowToRow=\"" + pasteBelowToRow + "\" />" ;  //PRG:RG6:R212:11-May-2011:in order to remove arrow
            	        xmlMessage += "</data></mxRoot>";
%>
                    var topFrame = findFrame(getTopWindow().getWindowOpener().getTopWindow(), "detailsDisplay");
					if (topFrame==null || topFrame==undefined){
                    	topFrame = findFrame(getTopWindow().getWindowOpener().getTopWindow(), "portalDisplay");
                    }
					if (typeof topFrame.emxEditableTable == 'undefined'){
                     	topFrame=this.parent.parent.opener;
                     }
	                getTopWindow().window.closeWindow();
                    topFrame.emxEditableTable.addToSelected('<%= xmlMessage%>'); //XSSOK
<%
            	}
            }
        }
        else
        {
            String strError = (String)session.getValue("error.message");
%>
            alert("<%=strError%>");
            </script>
            <script type="text/javascript" language="javascript">
<%
			session.removeValue("error.message");
        }
    }
    else if("addExistingDocument".equalsIgnoreCase(sMode)) { 
        String[] tableRowIdArray   =   emxGetParameterValues(request, "emxTableRowId");
        DomainObject dObjNewDocument = DomainObject.newInstance(context);
        StringBuffer sbDiscardedDocumentNameList = new StringBuffer();
        Object objNewDocList = request.getAttribute("newDocListForAddExisting");
        StringList slNewDocumentList = null;
		String swarningMsg = "";
        
        //Map put into session by emxProgramCentalAutonomySearcResults
        Map connectionMap = (Map)session.getAttribute("connectionMap");
        session.removeAttribute("connectionMap");
        String noChangeOwnerAccess = (String)session.getAttribute("noChangeOwnerAccess");
        session.removeAttribute("noChangeOwnerAccess");
        String needFullPageRefresh = (String)session.getAttribute("needFullPageRefresh");
        session.removeAttribute("needFullPageRefresh");
        
        if("true".equalsIgnoreCase(noChangeOwnerAccess)) {
     	   
             String sError = EnoviaResourceBundle.getProperty(context, ProgramCentralConstants.PROGRAMCENTRAL,
            		               "emxProgramCentral.Content.ConnectErrorMessage", request.getHeader("Accept-Language"));
          	 %>
             alert("<%=sError%>");
             </script>
        <script type="text/javascript" language="javascript"> <%                     
        }else{
        
        if(null != objNewDocList)
        {
        	slNewDocumentList = (StringList)objNewDocList;
        }
           
        if(tableRowIdArray != null && tableRowIdArray.length > 0) {
            StringBuffer partialXML   =   new StringBuffer();
            for (int i=0; i<tableRowIdArray.length; i++) {
                
                if(ProgramCentralUtil.isNotNullString(tableRowIdArray[i])) {
                    
                    Map parsedRowIdMap =  ProgramCentralUtil.parseTableRowId(context,tableRowIdArray[i]);
                    
                    if(parsedRowIdMap != null) {
                        
                        String newDocumentObjectId = (String)parsedRowIdMap.get("objectId");
                        String pasteBelowToRow = (String) parsedRowIdMap.get("rowId");
                        
                        if(ProgramCentralUtil.isNotNullString(newDocumentObjectId) && (null != slNewDocumentList && slNewDocumentList.contains(newDocumentObjectId))) {
                            
                             
                            //String newRelationShipId = MqlUtil.mqlCommand(context,"print bus $1 select $2 dump",sParentOID,"from["+DomainConstants.RELATIONSHIP_VAULTED_OBJECTS_REV2+"|to.id=="+newDocumentObjectId+"].id");
                            String newRelationShipId = (String)connectionMap.get(newDocumentObjectId);

                            if(ProgramCentralUtil.isNotNullString(newRelationShipId) && ProgramCentralUtil.isNotNullString(sParentOID)) {
                                if(ProgramCentralUtil.isNullString(pasteBelowToRow)) {
                                    pasteBelowToRow="0";
                                }
                                 
                                partialXML.append("<item oid=\"" + newDocumentObjectId); 
                                partialXML.append("\" relId=\"" + newRelationShipId);
                                partialXML.append("\" pid=\"" + sParentOID); 
                                partialXML.append("\" direction=\"\" pasteBelowToRow=\"" + pasteBelowToRow + "\" />") ; //PRG:RG6:R212:11-May-2011:in order to remove arrow
                            }
                            else
                            {
                            	dObjNewDocument.setId(newDocumentObjectId);
                            	StringList slObjList = new StringList();
                            	slObjList.add(DomainConstants.SELECT_NAME);
                            	Map mDocumentObjInfo = dObjNewDocument.getInfo(context,slObjList);
                            	if(null != mDocumentObjInfo)
                            	{
                            		String sDocumentObjName = (String)mDocumentObjInfo.get(DomainConstants.SELECT_NAME);
                            		sbDiscardedDocumentNameList.append(sDocumentObjName);
                            		sbDiscardedDocumentNameList.append("  ");
                            	}      
                            }
                        }      
                    }
                } 
            }
            if(sbDiscardedDocumentNameList.length() > 0)
            {
            	 swarningMsg = EnoviaResourceBundle.getProperty(context, "ProgramCentral", 
          			  "emxProgramCentral.Content.ConnectErrorMessage", request.getHeader("Accept-Language")) +
            	sbDiscardedDocumentNameList.toString();
            %>
            	alert("<%=swarningMsg%>");
            <%	
            }
            if("TRUE".equalsIgnoreCase(needFullPageRefresh)){
            	%> 
            	<%
            	if(!ProgramCentralUtil.is3DSearchEnabled(context)){
              	%>
              	window.parent.getTopWindow().getWindowOpener().parent.location = window.parent.getTopWindow().getWindowOpener().parent.location.href; 
            	getTopWindow().closeWindow();
              	<%}else{%>
              	window.parent.getTopWindow().getWindowOpener().parent.location = window.parent.getTopWindow().getWindowOpener().parent.location.href;
              	if(window.parent.getTopWindow().getWindowOpener()){
              		getTopWindow().closeWindow();
              	}
            	<%}
            }
            boolean isFromRMB = "true".equalsIgnoreCase(emxGetParameter(request, "isFromRMB"));
			String relName = emxGetParameter(request, "relName");
            String fromRMB = isFromRMB?"true":"";
            
            String xmlMessage = "<mxRoot>";
            xmlMessage += "<action><![CDATA[add]]></action> <data status=\"committed\" fromRMB=\"" + fromRMB + "\"" + " >";
            xmlMessage += partialXML.toString();
            xmlMessage += "</data></mxRoot>";

if(!ProgramCentralUtil.is3DSearchEnabled(context)){
%>
	topFrame = findFrame(getTopWindow().opener.getTopWindow(), "detailsDisplay");
	if(typeof topFrame.emxEditableTable === "undefined"){
		topFrame = findFrame(getTopWindow().opener.getTopWindow(), "<%= currentFrame %>");
	}
if(topFrame)
{
	var swarningMsg = '<%=swarningMsg%>';

	<% if(swarningMsg == ""){%>
			
		<%if(DomainConstants.RELATIONSHIP_VAULTED_OBJECTS_REV2.equalsIgnoreCase(relName)){%>
		topFrame.emxEditableTable.addToSelected('<%= xmlMessage %>');
		<%}else{%>
		topFrame.location.href=topFrame.location.href;
		getTopWindow().RefreshHeader();
		<%}%> 
	<%} else { %>
		getTopWindow().window.location.href=getTopWindow().window.location.href;
 <%	}%> 
}
else
{
	topFrame = findFrame(window.parent.getTopWindow().getWindowOpener().getTopWindow(), "detailsDisplay");
	if(typeof topFrame.emxEditableTable === "undefined"){
		topFrame = findFrame(window.parent.getTopWindow().getWindowOpener().getTopWindow(), "<%= currentFrame %>");
	}
	if(topFrame){
			if (typeof topFrame.emxEditableTable == 'undefined'){
                      	topFrame=this.parent.parent.opener;
            }
            topFrame.emxEditableTable.addToSelected('<%= xmlMessage %>'); //XSSOK
		}
	else {
		getTopWindow().opener.location.href = getTopWindow().opener.location.href; 
		}
	} <%}else{%>
	topFrame = findFrame(getTopWindow(), "detailsDisplay");
	if(typeof topFrame.emxEditableTable === "undefined"){
		topFrame = findFrame(getTopWindow(), "<%= currentFrame %>");
	}
	if(topFrame)
	{	
		<%if(DomainConstants.RELATIONSHIP_VAULTED_OBJECTS_REV2.equalsIgnoreCase(relName)){%>
		topFrame.emxEditableTable.addToSelected('<%= xmlMessage %>');
		<%}else{%>
		topFrame.location.href=topFrame.location.href;
		getTopWindow().RefreshHeader();
		<%}%> 
	}else {
		topFrame = findFrame(window.parent.getTopWindow(), "detailsDisplay");
		if(typeof topFrame.emxEditableTable === "undefined"){
			topFrame = findFrame(window.parent.getTopWindow(), "<%= currentFrame %>");
		}
		if(topFrame){
				if (typeof topFrame.emxEditableTable == 'undefined'){
	                      	topFrame=this.parent;
} 
	            topFrame.emxEditableTable.addToSelected('<%= xmlMessage %>'); //XSSOK
			}
		else {
			topFrame = findFrame(getTopWindow().opener.getTopWindow(), "detailsDisplay");
			if(typeof topFrame.emxEditableTable === "undefined"){
				topFrame = findFrame(getTopWindow().opener.getTopWindow(), "<%= currentFrame %>");
			}
			if(topFrame){
				topFrame.location.href=topFrame.location.href;
				getTopWindow().window.close();
			}else{
			getTopWindow().location.href = getTopWindow().location.href; 
			}
		}
	} <%}
        }
    }
    }
    else if("preProcessCheckout".equalsIgnoreCase(sMode) || "preProcessDownload".equalsIgnoreCase(sMode))
    {
    	String sParsedObjectId = null;
    	String sEmxTableRowId = "";
    	String sSelectedTableRowIds ="";
    	sDocumentAction = "";
    	String sCategoryObjectId = emxGetParameter(request, "objectId");
    	sCategoryObjectId = XSSUtil.encodeURLForServer(context, sCategoryObjectId);    	
    	String[] tableRowIdArray   =   emxGetParameterValues(request, "emxTableRowId");
    	
    	if(null != tableRowIdArray)
    	{
    		String[] strFolders = tableRowIdArray;
    		int cnt = strFolders.length;
    		for(int i=0;i<cnt;i++)
    		{
    			String sObjId = strFolders[i];
    			Map mParsedObject = ProgramCentralUtil.parseTableRowId(context,sObjId);
    			String sTempParentObjId = (String)mParsedObject.get("parentOId");
    			String sTempObjId = (String)mParsedObject.get("objectId");
    			String sTempRelId = (String)mParsedObject.get("relId");
    			
    			String sActualFileName =  "";
    			String sObjectType =  "";    			
    			StringList slSelectable = new StringList();
    			slSelectable.addElement(ProgramCentralConstants.SELECT_NAME);
    			slSelectable.addElement(ProgramCentralConstants.SELECT_IS_DOCUMENTS);
                if(ProgramCentralUtil.isNullString(sActualFileName))
                {
                   DomainObject dObj = DomainObject.newInstance(context, sTempObjId);//Test Folder
                   Map mObjectInfo = dObj.getInfo(context, slSelectable);
                   sActualFileName =  (String)mObjectInfo.get(ProgramCentralConstants.SELECT_NAME);/// add thuis in one db call temp fix
                
                   sObjectType = (String)mObjectInfo.get(ProgramCentralConstants.SELECT_IS_DOCUMENTS);                   
                   
                   if(!"True".equalsIgnoreCase(sObjectType))
                   {
                	   String sErrMsg = EnoviaResourceBundle.getProperty(context, "ProgramCentral", "emxProgramCentral.Folders.AddToSelect.selectOnlyDocument",  request.getHeader("Accept-Language"));
                   %>
                   alert("<%=XSSUtil.encodeForJavaScript(context,sErrMsg)%>");
                   </script>
                   <%
                   return;
                   }             
                }
                
                StringList slBusSelect = new StringList(2);
                slBusSelect.addElement(DomainConstants.SELECT_ID);
                slBusSelect.addElement(DomainConstants.SELECT_NAME);
                String sWhereClause = "name == \""+sActualFileName+"\"";
                DomainObject dMasterObj = DomainObject.newInstance(context, sTempParentObjId);
                MapList mlSubFolderList = dMasterObj.getRelatedObjects(context,
                        CommonDocument.RELATIONSHIP_ACTIVE_VERSION, //pattern to match relationships
                        CommonDocument.TYPE_DOCUMENTS, //pattern to match types
                        slBusSelect, //the eMatrix StringList object that holds the list of select statement pertaining to Business Obejcts.
                        null, //the eMatrix StringList object that holds the list of select statement pertaining to Relationships.
                        false, //get To relationships
                        true, //get From relationships
                        (short)1, //the number of levels to expand, 0 equals expand all.
                        sWhereClause, //where clause to apply to objects, can be empty ""
                        null,
                        0);
                if(null != mlSubFolderList && mlSubFolderList.size() ==1)
                {
                    Map mNewVersionObjMapAfterCheckIn = (Map)mlSubFolderList.get(0);
                    sTempObjId = (String)mNewVersionObjMapAfterCheckIn.get(DomainConstants.SELECT_ID);
                }
    			
    			sObjId = sTempRelId + "|" +sTempObjId+ "|" + sTempParentObjId;
    			if(i == cnt-1)
    			{
    				sSelectedTableRowIds += sObjId;
    			}
    			else
    			{
    			 sSelectedTableRowIds += sObjId+",";
    			}
    		}
    	}
    	
        if("preProcessCheckout".equalsIgnoreCase(sMode))
        {
            sDocumentAction = "checkout";
  %>
  <script type="text/javascript" language="javascript">
	        function preCallcheckout()
	        {
	            if("" == "<%=sParsedObjectId%>" || "null" == "<%=sParsedObjectId%>")
	            {
	            	var contentStoreURL = "../components/emxComponentsStoreFormDataToSession.jsp" ;
	                
	                var queryString = "emxTableRowId=<%=XSSUtil.encodeForJavaScript(context,sSelectedTableRowIds)%>&objectId=<%=XSSUtil.encodeForJavaScript(context,sCategoryObjectId)%>&action=<%=XSSUtil.encodeForJavaScript(context,sDocumentAction)%>&refresh=false&appName=Table&appDir=programcentral&appProcessPage=emxProgramCentraFolderUtil.jsp&getCheckoutMapFromSession=true";
	                
	                var xmlResult = emxUICore.getXMLDataPost(contentStoreURL, queryString);
	                var root = xmlResult.documentElement;
	                var sessionKey = emxUICore.getText(emxUICore.selectSingleNode(root, "/mxFormData/sessionKey"));
	              
	              callCheckout("","","","","false","","","","","","","","","","","true",sessionKey,"","");
	            }
	            else
	            {
	              callCheckout("<%=XSSUtil.encodeForJavaScript(context,sParsedObjectId)%>","<%=XSSUtil.encodeForJavaScript(context,sDocumentAction)%>","","","false","","Table","programcentral","","","","","","","","","","","emxProgramCentraFolderUtil.jsp");
	            }
	        }
	        
        preCallcheckout();
  <%
        }
        else
        {
            sDocumentAction = "download";
  %>
            function preProcessDownload()
            {
                if("" == "<%=sParsedObjectId%>" || "null" == "<%=sParsedObjectId%>")
                {
                    var contentStoreURL = "../components/emxComponentsStoreFormDataToSession.jsp" ;
                    <%-- XSSOK --%> 
                    var queryString = "emxTableRowId=<%=sSelectedTableRowIds%>&objectId=<%=sCategoryObjectId%>&action=<%=sDocumentAction%>&appName=Table&getCheckoutMapFromSession=true";
                    
                    var xmlResult = emxUICore.getXMLDataPost(contentStoreURL, queryString);
                    var root = xmlResult.documentElement;
                    var sessionKey = emxUICore.getText(emxUICore.selectSingleNode(root, "/mxFormData/sessionKey"));
                    
                  callCheckout("","","","","","","","","","","","","","","","true",sessionKey,"","");
                }
                else
                {
                	<%-- XSSOK --%> 
                       callCheckout("<%=sParsedObjectId%>","<%=sDocumentAction%>","","","","","Table","","","","","","","","","","","","");
                }
            }
            
            preProcessDownload();
  <% 
        }
    }else if("checkin".equalsIgnoreCase(sMode)) {
    	String oldOid      = emxGetParameter(request, "oid0");
    	DomainObject document = DomainObject.newInstance(context, sObjectId);
    	%>
        var detailsDisplayFrame = findFrame(getTopWindow(), "detailsDisplay");
        if (!detailsDisplayFrame) {
            if (getTopWindow().opener) {
                detailsDisplayFrame = findFrame(getTopWindow().opener.getTopWindow(), "detailsDisplay");
            }
            else {
                detailsDisplayFrame = null;
            }
        }
        if (detailsDisplayFrame) {
        	 if (typeof detailsDisplayFrame.emxEditableTable == 'undefined'){
        		 detailsDisplayFrame=this.parent.parent.opener;
              }
        	var pid = "<%=sObjectId%>";
        	var nRow = new Array();
        	nRow = emxUICore.selectNodes(detailsDisplayFrame.oXML, "/mxRoot/rows//r[@o = '" + pid + "']");
        	for(var rw=0;rw<nRow.length;rw++){
    		       	
        	nRow[rw].setAttribute("expand", false);
        	nRow[rw].setAttribute("expandedLevels", 0);
	        
	        var pidRowId =nRow[rw].getAttribute("id");
	        
	        var rowIdArr = new Array();
	        rowIdArr.push(pidRowId);
	        
	        detailsDisplayFrame.emxEditableTable.expand(rowIdArr, "1");
	        detailsDisplayFrame.emxEditableTable.refreshRowByRowId(rowIdArr);
        	}
}       
        <%
      } else if("uploaddeliverable".equalsIgnoreCase(sMode)) {
    	  %>
    	   var frameCheckIn = findFrame(getTopWindow(),"checkinFrame");
    	   if(frameCheckIn) {
    		   var listDisplayFrame = findFrame(frameCheckIn.getTopWindow().getWindowOpener(), "listDisplay");
    		   
    		   if(!listDisplayFrame){
    			   var topFrame = findFrame(getTopWindow().opener.getTopWindow(), <%=currentFrame%>);
    		          if(!topFrame)
    		          {
    		          	topFrame = findFrame(getTopWindow().opener.getTopWindow(), "PMCDeliverable");
    		          }
    		          if(topFrame)
    		          {
    		          	topFrame.location.href=topFrame.location.href;
    		          }else{
    		          	topFrame = findFrame(getTopWindow().opener.getTopWindow(), "detailsDisplay");
    		          	{
    		          		getTopWindow().opener.location.href = getTopWindow().opener.location.href; 
    		          		}
    		          	} 

    		          getTopWindow().close();
    			   
    			   
    		   }else{
    		   listDisplayFrame.parent.location.href = listDisplayFrame.parent.location.href;
    		   //listDisplayFrame.getTopWindow().getWindowOpener().emxEditableTable.refreshStructureWithOutSort();  
    		   }
    		    
    	   }
		   else{
    		   var listDisplayFrame = findFrame(getTopWindow().getWindowOpener().getTopWindow(), "PMCDeliverable");
    		   listDisplayFrame.parent.location.href = listDisplayFrame.parent.location.href;
    		   //listDisplayFrame.getTopWindow().getWindowOpener().emxEditableTable.refreshStructureWithOutSort();  
    		    
    	   }
    	  <%
      } else if("EditDetails".equalsIgnoreCase(sMode)) {
    	  %>
    	  var detailsDisplayFrame = findFrame(getTopWindow(), "detailsDisplay");
          if (!detailsDisplayFrame) {
              if (getTopWindow().getWindowOpener()) {
                  detailsDisplayFrame = findFrame(getTopWindow().getWindowOpener().getTopWindow(), "detailsDisplay");
              }
              else {
                  detailsDisplayFrame = null;
              }
          }
          if (detailsDisplayFrame) {
              detailsDisplayFrame.emxEditableTable.refreshStructureWithOutSort();
          }
    	  <%
      } else {
      %>
      var detailsDisplayFrame = findFrame(getTopWindow(), "detailsDisplay");
      if (!detailsDisplayFrame) {
          if (getTopWindow().getWindowOpener()) {
              detailsDisplayFrame = findFrame(getTopWindow().getWindowOpener().getTopWindow(), "detailsDisplay");
          }
          else {
              detailsDisplayFrame = null;
          }
					}
      if (detailsDisplayFrame) {
		  if (typeof detailsDisplayFrame.emxEditableTable == 'undefined'){
     		 detailsDisplayFrame=this.parent.parent.opener;
           }
          detailsDisplayFrame.emxEditableTable.refreshStructureWithOutSort();
          detailsDisplayFrame.emxEditableTable.refreshStructureWithOutSort();
					}
<%
    }    
    
    
    //Window Refresh Logic 
    
    //If action is download don't refresh
    if(!"download".equals(sDocumentAction)){
		if("addExistingDocument".equalsIgnoreCase(sMode)){
		%> if(swarningMsg ==""){
					getTopWindow().window.close();
				}else {
					getTopWindow().window.location.href=getTopWindow().window.location.href;
				}
		<%} else{
%>
		if(getTopWindow().closeSlideInDialog) {
			getTopWindow().closeSlideInDialog();
		} else {
			getTopWindow().window.close();
		}
<%
		}
	}
%>
    
</script>
</html>
