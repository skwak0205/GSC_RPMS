/**
 * @quickReview GVC ZUD 21:03:09 IR-829869 :  Error mgnt when creation/deletion of derivation link fails
 */
/* 
** emxRMTConnectBase
**
** Copyright (c) 2007 MatrixOne, Inc.
**
** All Rights Reserved.
** This program contains proprietary and trade secret information of
** MatrixOne, Inc.  Copyright notice is precautionary only and does
** not evidence any actual or intended publication of such program.
**
*/

/*
Change History:
Date       Change By  Release   Bug/Functionality        Details
-----------------------------------------------------------------------------------------------------------------------------
17-May-23  HAT1 ZUD   V6R2018x  TSK3278161              ENOVIA GOV TRM Deprecation of functionalities to clean up
17-Jun-06  HAT1 ZUD   V6R2018x  IR-526839-3DEXPERIENCER2018x: ENOVIA GOV TRM Deprecation of functionalities to clean up 

*/
import java.util.Hashtable;

import com.dassault_systemes.requirements.ReqSchemaUtil;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.EnoviaResourceBundle;
import com.matrixone.apps.domain.util.TreeOrderUtil;
import com.matrixone.apps.domain.util.FrameworkException;

import matrix.db.Context;
import matrix.db.JPO;
import matrix.util.MatrixException;
import matrix.util.StringList;

public class emxRMTConnectBase_mxJPO extends emxDomainObject_mxJPO
{	
	public emxRMTConnectBase_mxJPO(Context context, String[] args)
			throws Exception {
		super(context, args);
		// TODO Auto-generated constructor stub
	}

	public static StringList createDerivationLinks(Context context, String[] args) throws Exception
	{
		String[] IDs = (String[])JPO.unpackArgs(args);
		String objIdFrom = IDs[0];
		String objIdTo   = IDs[1];
		
		//++ OSLC Traceability Authoring Cmd ++
		DomainObject obj = new DomainObject();
		obj.setId(objIdFrom);
		String type = obj.getInfo(context, DomainConstants.SELECT_TYPE);
		
		if("Requirement Proxy".equalsIgnoreCase(type))
    	{
			String errorMsg = EnoviaResourceBundle.getProperty(context, "emxRequirementsStringResource", 
					context.getLocale(), "emxRequirements.Alert.TraceabilityAuthoring.RequirementProxy");
    		
			throw new Exception(errorMsg);
    	}
    	//-- OSLC Traceability Authoring Cmd --
		
		DomainObject objFrom = DomainObject.newInstance(context, objIdFrom);
		DomainObject objTo   = DomainObject.newInstance(context, objIdTo);
		
		String isUserAgent = EnoviaResourceBundle.getProperty(context, "emxRequirement.contextUser.UserAgent");
		if("true".equalsIgnoreCase(isUserAgent))
		{
			ContextUtil.pushContext(context);	
		}
		String fromConnect = "FALSE";					   
		String fromObjectAccessFrom = objFrom.getInfo(context, "current.access[modify]");
		String toobjectAccessFrom = objTo.getInfo(context, "current.access[modify]");
		
		if("true".equalsIgnoreCase(fromObjectAccessFrom) && "true".equalsIgnoreCase(toobjectAccessFrom))																				 
		{
			fromConnect = "TRUE";
		}															
		
		// Check fromconnect access 
		 if (!fromConnect.equalsIgnoreCase("TRUE"))
         {
            String defLang = context.getSession().getLanguage();
            String noAccess = EnoviaResourceBundle.getProperty(context, "emxRequirementsStringResource", context.getLocale(), "emxRequirements.Alert.ConnectAccessRestricted");
            throw (new MatrixException(noAccess));
         }
		 
		StringList relselect = new StringList();
		relselect.add(DomainRelationship.SELECT_ID);
		
		//Create Derived Requirement relationship.
		DomainRelationship newRel = DomainRelationship.connect(context, objFrom, ReqSchemaUtil.getDerivedRequirementRelationship(context), objTo);
	    Hashtable RelInfo			= newRel.getRelationshipData(context, relselect);
	    StringList objectRelId	= (StringList)RelInfo.get(DomainRelationship.SELECT_ID);

	    //Setting TreeOrder attribute.
	    newRel.setAttributeValue(context, ReqSchemaUtil.getTreeOrderAttribute(context),""+TreeOrderUtil.getNextTreeOrderValue());
	    
		return objectRelId;
	}

	  //HAT1 ZUD: IR-526839-3DEXPERIENCER2018x: ENOVIA GOV TRM Deprecation of functionalities to clean up 
	  /**
	   * Method
	   * @param context           the eMatrix <code>Context</code> object
	   * @param args              Derived Requirement relationship id.
	   * @return Message if the relationship object is deleted.
	   * @throws Throwable 
	   * @throws Exception if the operation fails
	   * @since RequirementsManagement V6R2018x
	   */
	public static String deleteDerivationLinks(Context context, String[] args) throws Exception
	{
		String[] relID = (String[])JPO.unpackArgs(args);
		String relObjId = relID[0];
		
		try 
		{
			String isUserAgent = EnoviaResourceBundle.getProperty(context, "emxRequirement.contextUser.UserAgent");
			if("true".equalsIgnoreCase(isUserAgent))
			{
				ContextUtil.pushContext(context);	
			}
			
			DomainRelationship.disconnect(context, relObjId, true);
			
			if("true".equalsIgnoreCase(isUserAgent))
			{
				ContextUtil.popContext(context);	
			}
			return "Derivation link is removed.";
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new FrameworkException(e);
		}
	}
	/**
	   * Method
	   * @param context           the eMatrix <code>Context</code> object
	   * @param args              Derived Requirement relationship id, AttributeName, New value of attribute.
	   * @return void.
	   * @throws Throwable 
	   * @throws Exception if the operation fails
	   * @since RequirementsManagement V6R2018x
	   */
	
    //  HAT1 ZUD: IR-620577-3DEXPERIENCER2019x fix 
	public static void modifyLinkStatusValue(Context context, String[] args) throws Exception
	{
		String[] strInfo = (String[])JPO.unpackArgs(args); 
		if(strInfo.length < 3)
			return;
		
		String strRelId = strInfo[0];
		String attributeName = strInfo[1];
		String newValue = strInfo[2];
		
		DomainRelationship.newInstance(context, strRelId).setAttributeValue(context, attributeName, newValue);		
	}
	
}
