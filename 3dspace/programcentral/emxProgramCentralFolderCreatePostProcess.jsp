<%--  emxProgramCentralFolderCreatePostProcess.jsp

   Copyright (c) 1999-2020 Dassault Systemes.
   All Rights Reserved.
   This program contains proprietary and trade secret information of MatrixOne,Inc.
   Copyright notice is precautionary only
   and does not evidence any actual or intended publication of such program

   static const char RCSID[] = $Id: emxProgramCentralFolderCreatePostProcess.jsp.rca 1.1.2.1.3.1 Wed Dec 24 12:59:09 2008 rdonahue Experimental $

 --%>

<%@include file="../emxUIFramesetUtil.inc"%>
<%@include file= "emxProgramCentralCommonUtilAppInclude.inc"%>

<%@page import="com.matrixone.apps.domain.util.MqlUtil"%>
<%@page import="com.matrixone.apps.domain.DomainObject"%>
<%@page import="matrix.db.Context"%>
<%@page import="com.matrixone.apps.domain.DomainConstants"%><html>
<head>
<title>Insert title here</title>
</head>
<body>
</body>
<script type="text/javascript" language="javascript">//![CDATA[
<!-- hide JavaScript from non-JavaScript browsers
<%
	String strNewObjectId    = emxGetParameter(request, "newObjectId");
	String strParentOID      = emxGetParameter(request, "parentOID");
	String strJSTreeID       = emxGetParameter(request, "jsTreeID");
	String strSuiteDirectory = emxGetParameter(request, "SuiteDirectory");
	
	String strParentType=DomainObject.EMPTY_STRING;
	String strTypeofContract = DomainObject.EMPTY_STRING;
  	if(null!=strParentOID && ""!=strParentOID)
	{
	   	DomainObject dobj=DomainObject.newInstance(context, strParentOID);
	   	strParentType=dobj.getInfo(context, DomainConstants.SELECT_TYPE);
	   	strTypeofContract = MqlUtil.mqlCommand(context, "print type $1 select $2 dump", true, strParentType, "kindof[Contract]");
	}
	//Added:21-Feb-2011:MS9:R211 PRG:IR-054191V6R2012 start
	String strIsClone = emxGetParameter(request, "IsClone");
	String sObjectType = emxGetParameter(request, "type");     //Added:PRG:RG6:R212:30-Jun-2011
	String strNwFolderName = emxGetParameter(request, "Name");  //Added:PRG:RG6:R212:30-Jun-2011
   String emxTableRowId = (String) emxGetParameter(request,"emxTableRowId");
   String pasteBelowToRow = "";
   String relId = ""; //new rel id generated by create page
   if(null != emxTableRowId){
       StringList slPasteBelowToRow = FrameworkUtil.split(emxTableRowId, "|");
       if(null != slPasteBelowToRow){
           pasteBelowToRow = slPasteBelowToRow.get(slPasteBelowToRow.size()-1).toString();
           if(slPasteBelowToRow.size() == 4){
        	   strParentOID = slPasteBelowToRow.get(1).toString();
        	 //[Deleted Line::May 24, 2011:RG6:2012x:IR-110474V6R2012x]      
           }
           else if(slPasteBelowToRow.size() == 3){
        	   strParentOID = slPasteBelowToRow.get(0).toString();
           }
       }
   }
   //Added:21-Feb-2011:MS9:R211 PRG:IR-054191V6R2012 end
	
   String strprojectObjectId = (String) emxGetParameter(request, "objectId");
   if(null == strParentOID)
   {
	   strParentOID=strprojectObjectId;
   }
//Added:21-Feb-2011:MS9:R211 PRG:IR-054191V6R2012 start
   Map programMap1 = new HashMap();
   Map requestMap1 = new HashMap();
   Map paramMap1= new HashMap();

   Enumeration enumParams=request.getParameterNames();
   while(enumParams.hasMoreElements())
   {
        String param=(String)enumParams.nextElement();
        String paramValue=request.getParameter(param);
        if(!"newObjectId".equalsIgnoreCase(param)){
            requestMap1.put(param,paramValue);
        }else{
        	paramMap1.put("objectId",paramValue);
        }
   }
   programMap1.put("paramMap",paramMap1);
   programMap1.put("requestMap",requestMap1);
      
    boolean isFromRMB = "true".equalsIgnoreCase(emxGetParameter(request, "isFromRMB"));
    String fromRMB = isFromRMB?"true":"";
    String xmlMessage=null;
    if(pasteBelowToRow.equals("") || pasteBelowToRow.equals(null))
    {
    	pasteBelowToRow="0";
    }
    //Added:PRG:RG6:R212:30-Jun-2011:check for unique workspace vault creation
    boolean isControlledFolderCreation = false;
    if(ProgramCentralUtil.isNotNullString(sObjectType))
    {
    	if(sObjectType.contains("_selectedType"))
    	{
    		isControlledFolderCreation = sObjectType.contains(DomainConstants.TYPE_CONTROLLED_FOLDER);
    	}
    	else if(sObjectType.equalsIgnoreCase("type_ControlledFolder"))
    	{
    		isControlledFolderCreation = true;
    	}
    }
    
    if(! isControlledFolderCreation )
    {
	    MapList mlFolders =PMCWorkspaceVault.getUniqueWorkspaceVaultInfo(context,strParentOID,strNwFolderName);
	    if(null != mlFolders && mlFolders.size() > 0)
	    {
	     throw new FrameworkException("type name revision not unique"); //PRG:RG6:R212:IR-113186V6R2012x:12-8-2011:modified
	    }
    }
    
  //End:PRG:RG6:R212:30-Jun-2011:check for unique workspace vault creation
    //PRG:RG6:R212:22-Jun-2011:110242V6R2012x/112931V6R2012x:Folder deletion Issue
    String strNewRelId    = emxGetParameter(request, "relId");
    if(ProgramCentralUtil.isNotNullString(strNewRelId))
    {
        relId = strNewRelId;
    }
    
    if("true".equals(strIsClone))
    {
	    xmlMessage = "<mxRoot>" +
	    "<action><![CDATA[add]]></action>" +
	    "<data status=\"committed\" fromRMB=\"" + fromRMB + "\" pasteBelowOrAbove=\"true\">" +
        "<item oid=\"" + strNewObjectId + "\" relId=\""+relId+ "\" pid=\"" + strParentOID + "\" direction=\"\"" +
	    " pasteBelowToRow=\"" + pasteBelowToRow + "\" />" + "</data></mxRoot>";
    }
    else
    {
	    xmlMessage = "<mxRoot>" +
		"<action><![CDATA[add]]></action>" +
		"<data status=\"committed\" fromRMB=\"" + fromRMB + "\"" + " >" +
		"<item oid=\"" + strNewObjectId + "\" relId=\"" + relId + "\" pid=\"" + strParentOID + "\" direction=\"\" pasteBelowToRow=\"" + pasteBelowToRow + "\" />" ;  //PRG:RG6:R212:11-May-2011:in order to remove arrow
		xmlMessage += "</data></mxRoot>";
    }
%>
        <%--XSSOK--%>
		var topFrame = findFrame(getTopWindow(), "PMCFolder");
		if(topFrame==null || topFrame =='undefined'){
			topFrame = findFrame(getTopWindow(), "detailsDisplay");
		}
    	if('<%=strParentType%>'=="Submission")
    	{
    		topFrame = findFrame(getTopWindow(), "LRASubmissionLocalFolder"); 
    		if(topFrame==null)
    			topFrame = findFrame(getTopWindow(), "LRASubmissionLocalFolder"); 
    		var leaderFrame=findFrame(getTopWindow(), "LRASubmissionLeaderFolder");
    		if(null!=leaderFrame)
    		{
	    		leaderFrame.emxEditableTable.addToSelected('<%=xmlMessage%>');
    			leaderFrame.refreshStructureWithOutSort();
    		}
    		
    	}
    	else if('<%=strParentType%>'=="Submission Master Record"){
    		topFrame = findFrame(getTopWindow(), "LRASubmissionMasterFolder");
    	}
    	else if('<%=strParentType%>'=="Contract Template" || '<%=strTypeofContract%>' == "TRUE"){
            var fancyTree = getTopWindow().objStructureFancyTree;
            if(fancyTree){
				fancyTree.addChild("<%=XSSUtil.encodeForJavaScript(context, strParentOID)%>", "<%=XSSUtil.encodeForJavaScript(context, strNewObjectId)%>");
            }   
	}
        topFrame.emxEditableTable.addToSelected('<%=xmlMessage%>');
        topFrame.refreshStructureWithOutSort();
        
	</script>
	</html>
	
	
