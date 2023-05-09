  <%--  emxRouteWizardFinalProcess.jsp  --  Creates a Route object through CreateRoute
  Wizard.
  Copyright (c) 1992-2020 Dassault Systemes.
  All Rights Reserved.
  This program contains proprietary and trade secret information of MatrixOne,Inc.
  Copyright notice is precautionary only and does not evidence any actual or intended publication of such program

  static const char RCSID[] = $Id: emxRouteWizardFinalProcess.jsp.rca 1.19 Wed Oct 22 16:18:24 2008 przemek Experimental przemek $
 --%>
<%@ page import="matrix.db.*,matrix.util.*,com.matrixone.apps.domain.*,com.matrixone.apps.domain.util.*,java.util.*,com.matrixone.apps.framework.ui.UIUtil"%>
<%@include file = "../emxUICommonAppInclude.inc"%>
<%@include file = "emxRouteInclude.inc" %>
<%@include file = "../common/enoviaCSRFTokenValidation.inc"%>
<jsp:useBean id="formBean" scope="session" class="com.matrixone.apps.common.util.FormBean"/>

<script language="JavaScript" src="../common/scripts/emxUICore.js" type="text/javascript"></script>
<script language="JavaScript" src="../common/scripts/emxUIConstants.js" type="text/javascript"></script>
<%

// Start: Create New Tasks at Current Level
    // If there are some tasks which are edited to be on the current level, then they should be started 
     String strFromPage = (String)formBean.getElementValue("FROM_PAGE");
     if ("EditAllTasks".equals(strFromPage)) {
         formBean.removeElement("FROM_PAGE");//Added for bug 374256 to remove the FROM_PAGE from formElementValues
         // Get route object id
         String strRouteId = emxGetParameter(request, "objectId");
         
         // Get what is the route status
         String strRouteStatus = (String)formBean.getElementValue("routeStatus");
         
         // Get what is the current route execution level
         String strCurrentRouteLevel = (String)formBean.getElementValue("currentRouteLevel");
         if (strCurrentRouteLevel == null) {
             throw new Exception("Current Route Level is null");
         }
         
         // Get the route orders for all tasks
         String strIsTaskStarted = (String)formBean.getElementValue("isTaskStarted");
         
         // Get the taks' order
         String strTaskOrders = (String)formBean.getElementValue("routeOrder");
         
         // If Route is Started then only we shall start the task on current level
         if ("Started".equals(strRouteStatus)) {
             StringList slIsTaskStartedFlags = FrameworkUtil.split(strIsTaskStarted, "~");
             StringList slTaskOrders = FrameworkUtil.split(strTaskOrders, "~");
             
             // For each task order, find if that is on the current level and is not yet started, 
             // if this is the case then only start the task on current level
             int nSize = slTaskOrders.size();
             String strTaskOrder = null;
             String strIsTaskStartedFlag = null;
             Route objRoute = (Route)DomainObject.newInstance(context, DomainObject.TYPE_ROUTE);
             
             for (int i = 0; i < nSize; i++) {
                 strTaskOrder = (String)slTaskOrders.get(i);
                 strIsTaskStartedFlag = (String)slIsTaskStartedFlags.get(i);
                 
                 if (strCurrentRouteLevel.equals(strTaskOrder)) {// Current Level?
                     if ("false".equalsIgnoreCase(strIsTaskStartedFlag)) {//Not Started yet?
                         
                         objRoute.setId(strRouteId);
                         objRoute.startTasksOnCurrentLevel(context);
                             
                         break;
                     }
                 }
             }
             
			 // If the task is already started, but then its Due Date is yet to be calculated from
             // offset then process it.
             strTaskOrder = null;
             strIsTaskStartedFlag = null;
             
             for (int i = 0; i < nSize; i++) {
                 strTaskOrder = (String)slTaskOrders.get(i);
                 strIsTaskStartedFlag = (String)slIsTaskStartedFlags.get(i);
                 
                 if (strCurrentRouteLevel.equals(strTaskOrder)) {// Current Level?
                     if (!"false".equalsIgnoreCase(strIsTaskStartedFlag)) {//Started?
                         
                         objRoute.setId(strRouteId);
                         objRoute.setDueDateFromOffsetForGivenLevelTasks(context, Integer.parseInt(strCurrentRouteLevel));
                             
                         break;
                     }
                 }
             }
             objRoute.updateRouteActivityStatus(context, strRouteId);
         }
%>
            <script language="javascript">
			  parent.window.getWindowOpener().location.href=parent.window.getWindowOpener().location.href;
			  window.closeWindow();
            </script>
<%
            return;
     }
// Start: Create New Tasks at Current Level

    final String AEF_WORKSPACE_LEAD_GRANTOR_USERNAME = "Workspace Lead Grantor";
    final String AEF_WORKSPACE_MEMBER_GRANTOR_USERNAME = "Workspace Member Grantor";
    final String AEF_WORKSPACE_ACCESS_GRANTOR_USERNAME = "Workspace Access Grantor";
	final String ROUTE_ACCESS_GRANTOR = "Route Access Grantor";

    String keyValue=emxGetParameter(request,"keyValue");
    String sState = (String)formBean.getElementValue("Stage");
    
    String sAttrRouteCompletionAction      = PropertyUtil.getSchemaProperty(context, "attribute_RouteCompletionAction" );
    String relProjectRoute                 = PropertyUtil.getSchemaProperty(context, "relationship_ProjectRoute");
    String sAttrAutoStopOnRejection        = PropertyUtil.getSchemaProperty(context, "attribute_AutoStopOnRejection" );
    String sAttrPreserveTaskOwner          = PropertyUtil.getSchemaProperty(context, "attribute_PreserveTaskOwner" );
    String sAttrTitle			           = PropertyUtil.getSchemaProperty(context, "attribute_Title" );

    BusinessObject memberObject         = null;
    BusinessObject routeTemplateObj     = null;
    boolean boolSubRoute                = false;
	String routeRequiresESign="False";
    String sProjectId                = "";
    String objectId                  = null;
    String documentID                = null;
    String sTemplateId               = null;
    String routeName                 = null;
    String routeDescription          = null;
    String routeAutoName             = null;
    String strTaskNeedsReview        = null;
    String strTemplateTaskVal        = null;
    String routeCompletionAction     = null;
    String routeBasePurpose          = null;
    String checkPreserveOwner        = null;
    String restrictMembers           = null;
    String selscopeId                = null;
    String routeId                   = "";
    String sVisblToParent = null;
    String strAutoStopOnRejection    = null;

    AttributeList attrList                      = null;


    Hashtable routeDetails    = (Hashtable)formBean.getElementValue("hashRouteWizFirst");
    MapList taskDetails       = (MapList)formBean.getElementValue("taskMapList");
    HashMap parallelNodeMap   = (HashMap)formBean.getElementValue("parallelNodeMap");
    MapList routeMemberList   = (MapList)formBean.getElementValue("routeMemberMapList");

    HashMap hashStateMap=(HashMap)formBean.getElementValue("hashStateMap");
    
    if( (hashStateMap == null) || (hashStateMap.size() <= 0) )
              hashStateMap= new HashMap();

    //String newFolderId     = (String)routeDetails.get("newFolderIds");
    HashMap map1           = new HashMap();
    String sUser           = context.getUser();

    if (routeDetails != null)
    {
      documentID            = (String)routeDetails.get("documentID");
      sTemplateId           = (String)routeDetails.get("templateId");
      routeName             = (String)routeDetails.get("routeName");
      routeDescription      = (String)routeDetails.get("routeDescription");
      routeAutoName         = (String)routeDetails.get("routeAutoName");
      routeCompletionAction = (String)routeDetails.get("routeCompletionAction");
      routeBasePurpose      = (String)routeDetails.get("routeBasePurpose");
      checkPreserveOwner      = (String)routeDetails.get("checkPreserveOwner");
      restrictMembers       = (String)routeDetails.get("selscope");
      sVisblToParent = (String)routeDetails.get("visblToParent");
      objectId  = (String)routeDetails.get("objectId");
      strAutoStopOnRejection = (String)routeDetails.get("routeAutoStop");
      if(restrictMembers.equals("ScopeName")){
        selscopeId = (String)routeDetails.get("selscopeId");
        restrictMembers   = (String)routeDetails.get("selscopeName");
      }
      else if(restrictMembers.equals("Organization"))
      {
       selscopeId = restrictMembers;
       }
      routeDetails.putAll(hashStateMap);
    }
    
    boolean rtSelected = (sTemplateId != null && !"null".equals(sTemplateId) && !sTemplateId.equals(""));
    if(rtSelected)
  	  new com.matrixone.apps.common.RouteTemplate(sTemplateId).checksToUseRouteTemplateInRoute(context);
    
          String sAttrRouteRequiresESignature   = PropertyUtil.getSchemaProperty(context, "attribute_RequiresESign" );
		  if(routeBasePurpose != null&&("Approval".equalsIgnoreCase(routeBasePurpose)||"Standard".equalsIgnoreCase(routeBasePurpose)))
			 {
				 if(rtSelected)
					{  
					  String SELECT_ATTRIBUTE_ROUTE_REQUIRES_ESIGN = "attribute[" + sAttrRouteRequiresESignature + "]";
					  DomainObject dmoRouteTemplate = new DomainObject(sTemplateId);
					  String rTemplateRequiresESign = dmoRouteTemplate.getInfo(context, SELECT_ATTRIBUTE_ROUTE_REQUIRES_ESIGN);
					  if("True".equalsIgnoreCase(rTemplateRequiresESign))
						{
							routeRequiresESign="True";
						}
					}
				 else
				   {
					 	String esignConfigSetting ="None";
						try{
								esignConfigSetting = MqlUtil.mqlCommand(context, "list expression $1 select $2 dump","ENXESignRequiresESign", "value");
								if(UIUtil.isNullOrEmpty(esignConfigSetting))
										esignConfigSetting="None";
							}
						catch (Exception e){
								esignConfigSetting="None";
						}
						if("All".equalsIgnoreCase(esignConfigSetting))
							routeRequiresESign="True";
				 }
			 
				
		   }
    if(sVisblToParent == null) 
        sVisblToParent = "";
     //person Object Creation 
     com.matrixone.apps.common.Person person = (com.matrixone.apps.common.Person)DomainObject.newInstance(context,DomainConstants.TYPE_PERSON);
     BusinessObject personObject = (BusinessObject)person.getPerson(context);

     String revisionSequence = "";
     String strProjectVault  = "";
     Route route  = null;
     DomainObject sProjectObject = null;
     String sPrjType = null;

    try
    {
      // Start the transaction
     ContextUtil.startTransaction(context, true);
     
      if(objectId != null && !"".equals(objectId) && !"null".equals(objectId))
      {
         
                DomainObject boObject = new DomainObject(objectId);
                boObject.open(context);
                String sType  = boObject.getTypeName();
                boolean isProjId = false;
                try
                {
                        if ((routeAutoName != null) && ("checked".equals(routeAutoName)) && !"null".equals(routeAutoName))
                        {
                            map1 = Route.createRouteWithScope(context , objectId , routeName , routeDescription , true,routeDetails);
                            
                        }
                        else
                        {
                        
                            map1 = Route.createRouteWithScope(context , objectId , routeName , routeDescription , false,routeDetails);
			
                        }
                        
                        
                        routeId = (String)map1.get("routeId");
                        if(sProjectId != null && !sProjectId.equals(""))
                        {
                          sProjectId = (String)map1.get("workspaceId");
                          sProjectObject = DomainObject.newInstance(context, sProjectId);
                          sPrjType = sProjectObject.getInfo(context, DomainObject.SELECT_TYPE);
                        }
                      
                }catch(Exception ranc){
                        
                        throw new FrameworkException(ranc.getMessage());
                }
        }//eof if(objectId != null && !"".equals(objectId) && !"null".equals(objectId))
      else 
      {
         
         //if no objectid exists
         if(restrictMembers.equals("All") || restrictMembers.equals("Organization"))
         {
                if(routeName == null || "null".equals(routeName) || "".equals(routeName))
                {                   
                	 	route = (Route)DomainObject.newInstance(context,DomainConstants.TYPE_ROUTE);
                        routeName = FrameworkUtil.autoName(context,"type_Route", new Policy(DomainObject.POLICY_ROUTE).getFirstInSequence(context),	"policy_Route",
         						null,
         						null,
         						true,
         						true);
                         route.createObject(context, DomainConstants.TYPE_ROUTE, routeName, null, DomainObject.POLICY_ROUTE, null);
                         routeId = route.getId(context);  
                }
                else
                {
                        BusinessObject tempObj = new BusinessObject(DomainObject.TYPE_ROUTE,routeName,revisionSequence,strProjectVault);
                        tempObj.create(context,DomainObject.POLICY_ROUTE);
                        route = (Route)DomainObject.newInstance(context,tempObj);
                        routeId = route.getObjectId();
                }
                // connect person creating route to the route via project route
                personObject.open(context);
                route.connect(context,new RelationshipType(DomainObject.RELATIONSHIP_PROJECT_ROUTE),true,personObject);
        }
        else
        {

			try {
                        if ((routeAutoName != null) && ("checked".equals(routeAutoName)) && !"null".equals(routeAutoName))
                        {
                                map1 = Route.createRouteWithScope(context , selscopeId , routeName , routeDescription , true,routeDetails);
                        }
                        else
                        {
                                map1 = Route.createRouteWithScope(context , selscopeId , routeName , routeDescription , false,routeDetails);
                        }
                        routeId = (String)map1.get("routeId");
                        sProjectId = (String)map1.get("workspaceId");
                        sProjectObject = DomainObject.newInstance(context, sProjectId);
                        sPrjType = sProjectObject.getInfo(context, DomainObject.SELECT_TYPE);
                }catch(Exception ranc){
                        
                        throw new FrameworkException(ranc.getMessage());
                }
         }// eof else 
     }// eof else if no object Id Exists

     // for setting attributes on the Route Object 
     if("true".equalsIgnoreCase(checkPreserveOwner)){
    	 checkPreserveOwner = "True";
     }else {
    	 checkPreserveOwner = "False";
     }
     route = (Route)DomainObject.newInstance(context,routeId);
     route.open(context);
     AttributeList routeAttrList = new AttributeList();
     routeAttrList.addElement(new Attribute(new AttributeType(DomainObject.ATTRIBUTE_ORIGINATOR),sUser));
     routeAttrList.addElement(new Attribute(new AttributeType(sAttrRouteCompletionAction),routeCompletionAction));
     routeAttrList.addElement(new Attribute(new AttributeType(DomainObject.ATTRIBUTE_ROUTE_BASE_PURPOSE),routeBasePurpose));
     routeAttrList.addElement(new Attribute(new AttributeType(sAttrAutoStopOnRejection),strAutoStopOnRejection));
     routeAttrList.addElement(new Attribute(new AttributeType(sAttrPreserveTaskOwner),checkPreserveOwner));
	  routeAttrList.addElement(new Attribute(new AttributeType(sAttrRouteRequiresESignature),routeRequiresESign));
	  routeAttrList.addElement(new Attribute(new AttributeType(sAttrTitle),routeName));
 	
     
     if( (selscopeId != null) && (!selscopeId.equals("")) ){
    	 if(FrameworkUtil.isObjectId(context, selscopeId)){
    	 	DomainObject boscope = new DomainObject(selscopeId);
         	selscopeId  =  boscope.getInfo(context, "physicalid");
  	   }
        routeAttrList.addElement(new Attribute(new AttributeType(DomainObject.ATTRIBUTE_RESTRICT_MEMBERS),selscopeId));
     }        
    
     route.setAttributes(context,routeAttrList);
     route.setDescription(routeDescription);
     route.update(context);
     // eof  setting attributes on the Route Object 
/*
      // for adding Project member to Route
     if(DomainObject.TYPE_WORKSPACE.equals(sPrjType) || DomainObject.TYPE_PROJECT.equals(sPrjType))
     {
        try
        {
           com.matrixone.apps.common.Person pr1 = com.matrixone.apps.common.Person.getPerson(context);
           // get the project Member.
           memberObject = JSPUtil.getProjectMember( context, session , sProjectId, pr1);
           // connect Project Member to Route
           route.connect(context,new RelationshipType(DomainObject.RELATIONSHIP_MEMBER_ROUTE),true,memberObject);
        }catch(Exception exp){
           throw new FrameworkException(exp.getMessage());
        }
     }
*/
     if(rtSelected)
     {
        try
        {
           routeTemplateObj = new BusinessObject(sTemplateId);
           route.connectTemplate(context, sTemplateId);
           
           //
           // Copy the value of Auto Stop On Rejection attribute from Route Template to Route object
           //
           final String ATTRIBUTE_AUTO_STOP_ON_REJECTION   = PropertyUtil.getSchemaProperty(context, "attribute_AutoStopOnRejection" );
           final String SELECT_ATTRIBUTE_AUTO_STOP_ON_REJECTION = "attribute[" + ATTRIBUTE_AUTO_STOP_ON_REJECTION + "]";
           // Get value from template object
           DomainObject dmoRouteTemplate = new DomainObject(routeTemplateObj);
           strAutoStopOnRejection = dmoRouteTemplate.getInfo(context, SELECT_ATTRIBUTE_AUTO_STOP_ON_REJECTION);
           if (strAutoStopOnRejection != null && !"".equals(strAutoStopOnRejection) && !"null".equalsIgnoreCase(strAutoStopOnRejection)) {
               // Set value on route object
               route.setAttributeValue(context, ATTRIBUTE_AUTO_STOP_ON_REJECTION, strAutoStopOnRejection);
           }
           
        }catch(Exception e){
           throw new FrameworkException(e.getMessage());
        }
     }
     if(objectId != null && !"null".equals(objectId) && !"".equals(objectId))
     {
        // construct the parent object for the Route
        DomainObject objectGeneral = DomainObject.newInstance(context, objectId);
		StringList slTaskSelects = new StringList();
		slTaskSelects.add(DomainConstants.SELECT_ORIGINATOR);
		slTaskSelects.add(DomainConstants.SELECT_OWNER);    
		slTaskSelects.add(DomainConstants.SELECT_TYPE);    
		Map mpTaskObject = objectGeneral.getInfo(context,slTaskSelects);	
		String strObjType = (String) mpTaskObject.get(DomainConstants.SELECT_TYPE); 
        //if(objectGeneral.getType(context).equals(DomainObject.TYPE_INBOX_TASK))
        if(DomainObject.TYPE_INBOX_TASK.equals(strObjType))	
        {
           boolSubRoute = true;
        }
        // to set Sub Route visibility attribute only when this is task Sub Route and user chooses Yes radio in Options page
        if(boolSubRoute && sVisblToParent != null && !"null".equals(sVisblToParent) && !"".equals(sVisblToParent) && "Yes".equalsIgnoreCase(sVisblToParent))
        {
          route.setAttributeValue(context, DomainObject.ATTRIBUTE_SUBROUTE_VISIBILITY , "Yes");
		  // Bug No : 304201 Begin
          //DomainObject taskObj=new DomainObject(objectId);
          //String originator=taskObj.getInfo(context,DomainConstants.SELECT_ORIGINATOR);
          String userAgent = PropertyUtil.getSchemaProperty(context, "person_UserAgent");		 		  
		  String originator = (String) mpTaskObject.get(DomainConstants.SELECT_ORIGINATOR);
		  String owner = (String) mpTaskObject.get(DomainConstants.SELECT_OWNER);		  
		  originator = userAgent.equalsIgnoreCase(originator) ? owner :originator;
		  
          
		  try{
    		 ContextUtil.pushContext(context);
		  	 StringList accessNames = DomainAccess.getLogicalNames(context, routeId);	
             DomainAccess.createObjectOwnership(context, routeId, null, originator +"_PRJ", (String)accessNames.get(0),  DomainAccess.COMMENT_MULTIPLE_OWNERSHIP, false);
          } catch(Exception ex) {
    		 throw new FrameworkException(ex);
          }finally {
              ContextUtil.popContext(context);
          }

// Bug No : 304201 End
        }
     }//if condition
        StringList contentIdList = new StringList();
        //<Fix 372839>
        StringList docIdList = new StringList();
		//</Fix 372839>
     // add any files to the Route if they were sent in
     if (documentID != null && !"".equals(documentID) && !"null".equals(documentID))
     {
        StringTokenizer stToken = new StringTokenizer(documentID, "~");
        while (stToken.hasMoreTokens()) 
        {
           String contentToken = stToken.nextToken();
           if (!contentToken.equals(objectId))
           {
              contentIdList.addElement(contentToken);
           }
           //<Fix 372839>
           else
           {
               docIdList.addElement(contentToken);
           }
           //</Fix 372839>
        }
        if(contentIdList.size() > 0)
        {
           String[] contentIdArray = (String[])contentIdList.toArray(new String []{});
           route.AddContent(context, contentIdArray,hashStateMap);
         
        }
        //Subscription for Document
        if (routeDetails != null)
        {
           MapList upLoadedFilesMapList      = (MapList)routeDetails.get("upLoadedFiles");
           if (upLoadedFilesMapList != null && upLoadedFilesMapList.size() > 0)
           {
              Workspace workspace = (Workspace)DomainObject.newInstance(context,sProjectId,DomainConstants.TEAM);
              Iterator upLoadedFilesMapListItr = upLoadedFilesMapList.iterator();
              WorkspaceVault wsVault = (WorkspaceVault)DomainObject.newInstance(context,DomainConstants.TYPE_WORKSPACE_VAULT,DomainConstants.TEAM);
              while(upLoadedFilesMapListItr.hasNext())
              {
                 Map upLoadedMap = (Map)upLoadedFilesMapListItr.next();
                 String docId = (String)upLoadedMap.get("docID");
                 String folderId = (String)upLoadedMap.get("folderId");
                 if (documentID.indexOf(docId) != -1)
                 {
                    String treeMenu = JSPUtil.getApplicationProperty(context,application,"eServiceSuiteTeamCentral.emxTreeAlternateMenuName.type_Document");
                    boolean validTreeMenu=false;
                    if(  treeMenu  != null && !"null".equals( treeMenu  ) && !"".equals( treeMenu ))
                    {
                       validTreeMenu=true;
                    }
                    //Subscription at WorkspaceVault Level
                    wsVault.setId(folderId);
                    if(validTreeMenu) 
                    {
                       MailUtil.setTreeMenuName(context, treeMenu );
                    }
                    //Subscription at workspace Level
                    validTreeMenu = false;
                    treeMenu = JSPUtil.getApplicationProperty(context,application,"eServiceSuiteTeamCentral.emxTreeAlternateMenuName.type_ProjectVault");
                    if(treeMenu  != null && !"null".equals( treeMenu  ) && !"".equals( treeMenu ))
                    {
                       validTreeMenu = true;
                    }
                    if(validTreeMenu)
                    {
                       MailUtil.setTreeMenuName(context, treeMenu);
                    }
                 }
              }
           }//UploadedFilesMapList
        }//RouteDetails (Subscription for Document)
     }// if (documentID != null && !"".equals(documentID) && !"null".equals(documentID))

        if(taskDetails != null && taskDetails.size() > 0){
                route.addRouteMembers(context, taskDetails,parallelNodeMap);
        }

        if(routeMemberList != null && routeMemberList.size() > 0){
                route.addRouteMemberAccess(context, routeMemberList);
        }
		ContextUtil.commitTransaction(context);
    }
    catch (Exception ex )
    {
        ContextUtil.abortTransaction(context);
        throw new FrameworkException(ex.getMessage());
    }

%>
<html>
	<head>
	
	</head>
	<body>
	<script language="javascript">
		submitWithCSRF("emxRouteWizardStartProcess.jsp?routeId=<%=XSSUtil.encodeForURL(context, routeId)%>&objectId=<%=XSSUtil.encodeForURL(context, objectId)%>&Stage=<%=XSSUtil.encodeForURL(context, sState)%>&keyValue=<%=XSSUtil.encodeForURL(context, keyValue)%>", window);
	</script>
	</body>
</html>
