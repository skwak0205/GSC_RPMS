/*
 ** 05:10:2017 KIE1 ZUD IR-522320-3DEXPERIENCER2018x: R419-STP: Requirement Validation option is KO from Quick chart.
 ** 20:03:2018 KIE1 ZUD 	:IR-580732-3DEXPERIENCER2018x: validation column of the Test Case wrongly at failed
*/

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.dassault_systemes.enovia.webapps.richeditor.util.JsonUtil;
import com.dassault_systemes.requirements.ReqSchemaUtil;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.EnoviaResourceBundle;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.requirements.RequirementsUtil;

import matrix.db.Context;
import matrix.db.JPO;
import matrix.util.MatrixException;
import matrix.util.Pattern;
import matrix.util.StringList;

public class emxRMTDashBoardBase_mxJPO{  
	public emxRMTDashBoardBase_mxJPO (Context context, String[] args) throws Exception{
	}
    
    // Requirements Dashboard
	@com.matrixone.apps.framework.ui.ProgramCallable
    public MapList retrieveRequirementsDashboardItems(Context context, String[] args, StringList busSelects, String sRelationships, String sTypes, String sFilter, Map mFilter) throws Exception {    
        HashMap paramMap        = (HashMap) JPO.unpackArgs(args);
        String sOID             = (String) paramMap.get("objectId"); 
        MapList mlResults       = new MapList();
        if(!sFilter.equals("")) { sFilter = " && " + sFilter; }
        sFilter = "(revision == last) && (current != 'Obsolete')" + sFilter;
        if(sOID.equals("")) {
            mlResults = DomainObject.findObjects(context, "Requirement", null, "(owner == '" + context.getUser() + "') && " + sFilter, busSelects);            
            com.matrixone.apps.common.Person pUser = com.matrixone.apps.common.Person.getPerson( context );
            MapList mlRequirementsAssigned = pUser.getRelatedObjects(context, "Assigned Requirement", "Requirement", busSelects, null, false, true, (short)1, "(owner != '" + context.getUser() + "') && " + sFilter, "", 0);            
            mlResults.addAll(mlRequirementsAssigned);
        } else {
            Pattern pTypes = new Pattern("Requirement");
            busSelects.add("type");
            DomainObject dObject = new DomainObject(sOID);
            mlResults = dObject.getRelatedObjects(context, sRelationships, sTypes, busSelects, null, false, true, (short)0, "", "", 0, pTypes, null, mFilter);                
        }
        return mlResults;
    }
	
	@com.matrixone.apps.framework.ui.ProgramCallable
    public MapList getRequirementsDashboardItems(Context context, String[] args) throws Exception {  
        HashMap paramMap                = (HashMap)JPO.unpackArgs(args);
        String sOID                     = (String) paramMap.get("objectId"); 
        String sTypes                   = (String) paramMap.get("types");   
        String sRelationships           = (String) paramMap.get("relationships");       
        String filterEC                 = (String) paramMap.get("filterEC"); 
        String sFilterResponsibility    = (String) paramMap.get("filterResponsibility");  
        if(sTypes.equals(""))           { sTypes = "*"; }
        if(sRelationships.equals(""))   { sRelationships = "Design Responsibility,Sub Requirement,Specification Structure"; }   
        if(null == filterEC) { filterEC = ""; }
        StringBuilder sbWhere   = new StringBuilder();
        Map mFilter             = new HashMap();
        StringList busSelects   = new StringList();
        busSelects.add("id");                  
        if(!filterEC.equals("")) {
            busSelects.add("to[EC Affected Item]");
            busSelects.add("to[EC Affected Item].from.current");
        }
        if(!sFilterResponsibility.equals("undefined")) {
            if(!sFilterResponsibility.equals("-1")) {
                if(sFilterResponsibility.equals("-")) {
                    busSelects.add("to[Design Responsibility]");
                    sbWhere.append("(to[Design Responsibility] == False) && ");
                    mFilter.put("to[Design Responsibility]", "FALSE");
                } else {                
                    busSelects.add("to[Design Responsibility]");
                    busSelects.add("to[Design Responsibility].from.name");
                    sbWhere.append("(to[Design Responsibility] == TRUE && to[Design Responsibility].from.name == '").append(sFilterResponsibility).append("') && ");
                    mFilter.put("to[Design Responsibility]", "TRUE");
                    mFilter.put("to[Design Responsibility].from.name", sFilterResponsibility);
                }
            }     
        }           
        if (sbWhere.length() > 4 ) { sbWhere.setLength(sbWhere.length() - 4); }        
        MapList mlResults = retrieveRequirementsDashboardItems(context, args, busSelects, sRelationships, sTypes, sbWhere.toString(), mFilter);        
        if(!filterEC.equals("")) { 
            String sStatesECComplete = "Complete,Close,Reject";
            for(int i = mlResults.size() - 1; i >= 0; i--) {
                Map mResult             = (Map)mlResults.get(i);
                String sHasEC           = (String)mResult.get("to[EC Affected Item]");
                Boolean bRemove         = false;                
                Boolean bHasPendingEC   = false;
                if(sHasEC.equalsIgnoreCase("TRUE")) {
                    if (mResult.get("to[EC Affected Item].from.current") instanceof StringList) {
                        StringList slData = (StringList)mResult.get("to[EC Affected Item].from.current");
                       for(int j = 0; j < slData.size(); j++) {
                           String sStatus = (String)slData.get(j);
                           if(!sStatesECComplete.contains(sStatus)) {
                               bHasPendingEC = true;
                               break;
                           }
                       }                  
                    } else {
                        String sStatus = (String)mResult.get("to[EC Affected Item].from.current");
                        if(!sStatesECComplete.contains(sStatus)) {
                            bHasPendingEC = true;
                            break;
                        }                  
                    }
                }
                if(filterEC.equals("0")) { // No pending EC
                    if(bHasPendingEC) { bRemove = true; }
                } else if (filterEC.equals("1")) { // WITH pending EC
                    if(!bHasPendingEC) { bRemove = true; }
                }  
                if(bRemove) { mlResults.remove(i); }
                
            }           
        }
        if(!sOID.equals("")) {
            for(int i = mlResults.size() - 1; i >= 0; i--) {
                Map mResult = (Map)mlResults.get(i);
                mResult.put("level", "1");
            }            
        } 
        return mlResults;		
    }
    
	private JsonArray getTestCaseInformation(Context context, String objectId) throws FrameworkException{
		String sTypes           = "*";   
        String sRelationships   = ReqSchemaUtil.getSubRequirementRelationship(context) + ","
        						+ ReqSchemaUtil.getSpecStructureRelationship(context) + ","
        						+ ReqSchemaUtil.getRequirementValidationRelationship(context);  
        StringList busSelects = new StringList();
        StringList relSelects = new StringList();
        busSelects.add(DomainConstants.SELECT_ID);
        busSelects.add(DomainConstants.SELECT_TYPE);  
        busSelects.add(DomainConstants.SELECT_CURRENT);
        busSelects.add("from[Requirement Validation].to.current");        
        busSelects.add("from[Requirement Validation].to.attribute[Validation Status]");
        busSelects.add("from[Requirement Validation]");
        busSelects.add("attribute[Validation Status]");
        JsonArrayBuilder response = Json.createArrayBuilder();
        relSelects.add("from.id[connection]");
        DomainObject dObject = DomainObject.newInstance(context, objectId);
        MapList mlRequirements = dObject.getRelatedObjects(context, sRelationships, sTypes, busSelects, relSelects, false, true, (short)0, "", "", 0, null, null, null);
		
        for(int i=0; i<mlRequirements.size();i++){
        	Map object = (Map)mlRequirements.get(i);
			String testCaseId = (String)object.get(DomainConstants.SELECT_ID);
        	String type = (String)object.get(DomainConstants.SELECT_TYPE);
			DomainObject domObj = null;
			try{
				if(!"null".equalsIgnoreCase(testCaseId) && !"".equalsIgnoreCase(testCaseId))
				{
					domObj = new DomainObject(testCaseId);
				}
				if(domObj.isKindOf(context,ReqSchemaUtil.getTestCaseType(context))){
				// ++KIE1 IR-522320-3DEXPERIENCER2018x
					Map map = new HashMap();
					map.put("objectId", testCaseId);
					map.put("relId", "");
					map.put("languageStr", "");
					
					Map mapForArgs1 = new HashMap();
					mapForArgs1.put("paramMap", map);
					String[] args1 = JPO.packArgs(mapForArgs1);
					
					String validationStatus = "";
					try {
						validationStatus = (String) JPO.invoke(context,
								"emxPLCCommonBase", null, "lastCompletedTestExecutionStatus", JPO.packArgs(mapForArgs1),
								 String.class);
					} catch (MatrixException e) {
						
						e.printStackTrace();
					}
					validationStatus = (String) validationStatus.subSequence(3, validationStatus.length()-4);
					if(validationStatus.equalsIgnoreCase(EnoviaResourceBundle.getProperty(context, "emxProductLineStringResource", context.getLocale(), "emxProductLine.TestCase.LastReplayedTestExecution.NoTEReplayed")))
					{
						validationStatus = EnoviaResourceBundle.getProperty(context, "emxRequirementsStringResource", context.getLocale(), "emxRequirements.QuickChart.Validation_Status.NotReplayed");
					}
					object.put("attribute[Validation Status]", validationStatus);
					// --KIE1 IR-522320-3DEXPERIENCER2018x     			
					response.add(JsonUtil.buildJsonObj(object));
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
        }
		return response.build();
	}
	
	private JsonArray getECInformation(Context context, String objectId) throws FrameworkException{
		String sTypes           = "*";   
        String sRelationships   = ReqSchemaUtil.getSubRequirementRelationship(context) + ","
        						+ ReqSchemaUtil.getSpecStructureRelationship(context);
        StringList busSelects = new StringList();
        StringList relSelects = new StringList();
        busSelects.add(DomainConstants.SELECT_ID);
        busSelects.add(DomainConstants.SELECT_TYPE);  
        busSelects.add(DomainConstants.SELECT_CURRENT);
        busSelects.add("to[EC Affected Item]");
        busSelects.add("to[EC Affected Item].from.current");
        JsonArrayBuilder response = Json.createArrayBuilder();
        relSelects.add("from.id[connection]");
        DomainObject dObject = DomainObject.newInstance(context, objectId);
        MapList mlRequirements = dObject.getRelatedObjects(context, sRelationships, sTypes, busSelects, relSelects, false, true, (short)0, "", "", 0, null, null, null);
        for(int i=0; i<mlRequirements.size();i++){
        	Map object = (Map)mlRequirements.get(i);
        	response.add(JsonUtil.buildJsonObj(object));
        }
		return response.build();
	}
	
	private JsonArray getDesignResponsibilityInformation(Context context, String objectId) throws FrameworkException{
		String sTypes           = "*";   
        String sRelationships   = RequirementsUtil.getDesignResponsibilityRelationship(context)+"," 
        						+ ReqSchemaUtil.getSubRequirementRelationship(context) + ","
        						+ ReqSchemaUtil.getSpecStructureRelationship(context);
        StringList busSelects = new StringList();
        StringList relSelects = new StringList();
        busSelects.add(DomainConstants.SELECT_ID);
        busSelects.add(DomainConstants.SELECT_TYPE);  
        busSelects.add(DomainConstants.SELECT_CURRENT);
        busSelects.add("to[Design Responsibility].from.name");
        busSelects.add("to[Design Responsibility]");
        JsonArrayBuilder response =  Json.createArrayBuilder();
        relSelects.add("from.id[connection]");
        DomainObject dObject = DomainObject.newInstance(context, objectId);
        MapList mlRequirements = dObject.getRelatedObjects(context, sRelationships, sTypes, busSelects, relSelects, false, true, (short)0, "", "", 0, null, null, null);
        for(int i=0; i<mlRequirements.size();i++){
        	Map object = (Map)mlRequirements.get(i);
        	response.add(JsonUtil.buildJsonObj(object));
        }
		return response.build();
	}
	
	private JsonArray getTimeLineInformation(Context context, String objectId) throws ParseException, MatrixException{
		StringBuilder sbCategoriesTimeline          = new StringBuilder();
        java.util.List<String> lTimeline            = new ArrayList<String>();
        java.util.List<Integer> lWeeksMonths        = new ArrayList<Integer>();
        java.util.List<Integer> lYears              = new ArrayList<Integer>();  
        StringBuilder sbDataTimeline0               = new StringBuilder();
        StringBuilder sbDataTimeline1               = new StringBuilder();
        StringBuilder sbDataTimeline2               = new StringBuilder();
        StringBuilder sbDataTimeline3               = new StringBuilder();
        StringBuilder sbDataTimeline4               = new StringBuilder();
		String sTypes           = "*"; 
        String sRelationships   = ReqSchemaUtil.getSubRequirementRelationship(context) + ","
        						+ ReqSchemaUtil.getSpecStructureRelationship(context);
        StringList busSelects = new StringList();
        StringList relSelects = new StringList();
        busSelects.add(DomainConstants.SELECT_ID);
        busSelects.add(DomainConstants.SELECT_TYPE);  
        busSelects.add(DomainConstants.SELECT_CURRENT);
        busSelects.add("state[Private].start");
        busSelects.add("state[InWork].start");
        busSelects.add("state[Validate].start");
        busSelects.add("state[Frozen].start");
        busSelects.add("state[Release].start");
        busSelects.add("originated");
        MapList   outList = new MapList();
        JsonArrayBuilder response =  Json.createArrayBuilder();
        relSelects.add("from.id[connection]");
        DomainObject dObject = DomainObject.newInstance(context, objectId);
        MapList mlRequirements = dObject.getRelatedObjects(context, sRelationships, sTypes, busSelects, relSelects, false, true, (short)0, "", "", 0, null, null, null);
        if(mlRequirements.size() > 0) {
            mlRequirements.sort("originated", "ascending", "date");
            Map mFirst              = (Map)mlRequirements.get(0);  
            Boolean bUseMonths      = false;
            String sDateStart       = (String)mFirst.get("originated");
            SimpleDateFormat sdf    = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aaa");

            Calendar cNow           = Calendar.getInstance();
            Calendar cStart         = Calendar.getInstance();
            Calendar cDate          = Calendar.getInstance(); 

            cDate.setTime(sdf.parse(sDateStart));
            cStart.setTime(sdf.parse(sDateStart));     
            do  {
                int iWeek = cDate.get(Calendar.WEEK_OF_YEAR);
                int iYear = cDate.get(Calendar.YEAR);
                lWeeksMonths.add(iWeek);
                lYears.add(iYear);//          
                String sTemp = iWeek + "/" + iYear;
                lTimeline.add(sTemp);
                cDate.add(Calendar.DATE, 7);
            } while (cDate.before(cNow));                
            if(lTimeline.size() > 18) {
                bUseMonths      = true;
                lTimeline       = new ArrayList<String>();
                lWeeksMonths    = new ArrayList<Integer>();
                lYears          = new ArrayList<Integer>();
                cDate.setTime(sdf.parse(sDateStart));            
                do  {           
                    int iMonth = cDate.get(Calendar.MONTH) + 1;
                    int iYear = cDate.get(Calendar.YEAR);         
                    String sTemp = iMonth + "/" + iYear;
                    lTimeline.add(sTemp);
                    lWeeksMonths.add(iMonth);
                    lYears.add(iYear);
                    cDate.add(Calendar.MONTH, 1);
                } while (cDate.before(cNow));            
            }  
            for(int i=0; i<mlRequirements.size();i++){
            	Map mlRequirement = (Map)mlRequirements.get(i);
            	String sStart0              = (String) mlRequirement.get("state[Private].start");
                String sStart1              = (String)mlRequirement.get("state[InWork].start");
                String sStart2              = (String) mlRequirement.get("state[Validate].start");
                String sStart3              = (String) mlRequirement.get("state[Frozen].start");
                String sStart4              = (String) mlRequirement.get("state[Release].start");  
            	String sTimeline0 = getWeekOfTimeline(lTimeline, lYears, lWeeksMonths, sStart0, sdf, bUseMonths);
                String sTimeline1 = getWeekOfTimeline(lTimeline, lYears, lWeeksMonths, sStart1, sdf, bUseMonths);
                String sTimeline2 = getWeekOfTimeline(lTimeline, lYears, lWeeksMonths, sStart2, sdf, bUseMonths);
                String sTimeline3 = getWeekOfTimeline(lTimeline, lYears, lWeeksMonths, sStart3, sdf, bUseMonths);
                String sTimeline4 = getWeekOfTimeline(lTimeline, lYears, lWeeksMonths, sStart4, sdf, bUseMonths);

                sbDataTimeline0.append(sTimeline0).append(",");
                sbDataTimeline1.append(sTimeline1).append(",");
                sbDataTimeline2.append(sTimeline2).append(",");
                sbDataTimeline3.append(sTimeline3).append(",");
                sbDataTimeline4.append(sTimeline4).append(",");
            }
        }
        for(int i = 0; i < lTimeline.size(); i++) {           
            sbCategoriesTimeline.append("'").append(lTimeline.get(i)).append("',");
        }
        if (sbCategoriesTimeline.length() > 0 ) { 
        	sbCategoriesTimeline.setLength(sbCategoriesTimeline.length() - 1); 
        }
        Map data = new HashMap<String,String>();
        data.put("CategoriesTimeline", sbCategoriesTimeline.toString());
        data.put("Timeline0", sbDataTimeline0.toString());
        data.put("Timeline1", sbDataTimeline1.toString());
        data.put("Timeline2", sbDataTimeline2.toString());
        data.put("Timeline3", sbDataTimeline3.toString());
        data.put("Timeline4", sbDataTimeline4.toString());
        response.add(JsonUtil.buildJsonObj(data));
		return response.build();
	}
	
	private JsonArray getDerivedRequirementsStatus(Context context, String objectIdList,String direction) throws NumberFormatException, MatrixException{
		int[] result = new int[2]; 
		String has="";
		String hasNot="";
		
		result[0] = 0;
		result[1] = 0;
		String sTypes           = "*";   
        String sRelationships   = ReqSchemaUtil.getDerivedRequirementRelationship(context);  
        StringList busSelects = new StringList();
        StringList relSelects = new StringList();
        busSelects.add(DomainConstants.SELECT_ID);
        busSelects.add(DomainConstants.SELECT_TYPE);  
        busSelects.add(DomainConstants.SELECT_CURRENT);
        //JSONArray response = new JSONArray();
        String[] idList = objectIdList.split(",");
        boolean toDirection;
        boolean fromDirection;
        if(direction.equalsIgnoreCase("to")){
        	has = EnoviaResourceBundle.getProperty(context, "emxRequirementsStringResource", context.getLocale(), "emxRequirements.dashboard.refinedRequirement"); 
        	hasNot = EnoviaResourceBundle.getProperty(context, "emxRequirementsStringResource", context.getLocale(), "emxRequirements.dashboard.notrefinedRequirement"); 
        	toDirection = true;
            fromDirection = false;
        }else{
        	//covered Requirements
        	has = EnoviaResourceBundle.getProperty(context, "emxRequirementsStringResource", context.getLocale(), "emxRequirements.dashboard.coveredRequirement");
        	hasNot = EnoviaResourceBundle.getProperty(context, "emxRequirementsStringResource", context.getLocale(), "emxRequirements.dashboard.notCoveredRequirement"); 
        	toDirection = false;
            fromDirection = true;
        }
        JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        //JSONArray reqWithDerived = new JSONArray();
        JsonArrayBuilder returnedValues = Json.createArrayBuilder();
        JsonObjectBuilder objectInfo = Json.createObjectBuilder();
        for(int i=0;i<idList.length;i++){
        	String objectId = idList[i];
        	DomainObject dObject = DomainObject.newInstance(context, objectId);
            MapList mlRequirements = dObject.getRelatedObjects(context, sRelationships, sTypes, busSelects, relSelects, fromDirection, toDirection, (short)0, "", "", 0, null, null, null);
            if(mlRequirements.size()>0){
            	result[0]+=1;
            	//reqWithDerived.put(objectId);
            	objectInfo.add(objectId,has);
            }else{
            	result[1]+=1;
            	objectInfo.add(objectId,hasNot);
            }
            
            /*for(int j=0; j<mlRequirements.size();j++){
            	Map object = (Map)mlRequirements.get(j);
            	String state = (String)object.get(DomainConstants.SELECT_CURRENT);
            	if(jsonObject.contains(state)){
            		int value = (Integer) jsonObject.get(state);
            		value++;
            		jsonObject.put(state, value);
            	}else{
            		jsonObject.put(state, 1);
            	}
            }*/
        }
        //response.put(jsonObject);
        jsonObject.add("0", result[0]);
        jsonObject.add("1", result[1]);
        returnedValues.add(jsonObject.build());
        //returnedValues.put(reqWithDerived);
        returnedValues.add(objectInfo.build());
		return returnedValues.build();
	}
	
	
	private JsonObject getSubRequirements(Context context, String objectIdList) throws NumberFormatException, MatrixException{
		int[] result = new int[2]; 
		String habSub = EnoviaResourceBundle.getProperty(context, "emxRequirementsStringResource", context.getLocale(), "emxRequirements.dashboard.hasSubReq");
		String habNoSub = EnoviaResourceBundle.getProperty(context, "emxRequirementsStringResource", context.getLocale(), "emxRequirements.dashboard.hasNoSubReq"	);

		String sTypes           = ReqSchemaUtil.getRequirementType(context);   
		String sRelationships   = ReqSchemaUtil.getSubRequirementRelationship(context);  
        StringList busSelects = new StringList();
        StringList relSelects = new StringList();
        busSelects.add(DomainConstants.SELECT_ID);
        busSelects.add(DomainConstants.SELECT_TYPE);  
        busSelects.add(DomainConstants.SELECT_CURRENT);
        String[] idList = objectIdList.split(",");
        JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        for(int i=0;i<idList.length;i++){
        	String objectId = idList[i];
        	DomainObject dObject = DomainObject.newInstance(context, objectId);
            MapList mlRequirements = dObject.getRelatedObjects(context, sRelationships, sTypes, busSelects, relSelects, false, true, (short)1, "", "", 0, null, null, null);
            if(mlRequirements.size()>0){
            	jsonObject.add(objectId, habSub);
            }else{
            	jsonObject.add(objectId, habNoSub);
            }
        }
		return jsonObject.build();
	}
	
	private JsonArray getTestCaseValues(Context context, String objectList) throws MatrixException{
		int[] result = new int[2]; 
		String withTestCases = EnoviaResourceBundle.getProperty(context, "emxRequirementsStringResource", context.getLocale(), "emxRequirements.dashboard.hasTestCases"); 
		String noTestCases = EnoviaResourceBundle.getProperty(context, "emxRequirementsStringResource", context.getLocale(), "emxRequirements.dashboard.hasNoTestCases"); 
		String sTypes           = "*";   
        String sRelationships   = ReqSchemaUtil.getRequirementValidationRelationship(context);  
        StringList busSelects = new StringList();
        StringList relSelects = new StringList();
        busSelects.add(DomainConstants.SELECT_ID);
        busSelects.add(DomainConstants.SELECT_TYPE);  
        busSelects.add(DomainConstants.SELECT_CURRENT);
        busSelects.add("from[Requirement Validation].to.current");        
        busSelects.add("from[Requirement Validation].to.attribute[Validation Status]");
        busSelects.add("from[Requirement Validation]");
        busSelects.add("attribute[Validation Status]");
        JsonArrayBuilder response = Json.createArrayBuilder();
        relSelects.add("from.id[connection]");
        
        String[] idList = objectList.split(",");
        JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        JsonObjectBuilder jsonReqWithTestCases = Json.createObjectBuilder();
        JsonArrayBuilder returnedValues = Json.createArrayBuilder();
        for(int i=0;i<idList.length;i++){
        	String objectId = idList[i];
        	DomainObject dObject = DomainObject.newInstance(context, objectId);
            MapList mlRequirements = dObject.getRelatedObjects(context, sRelationships, sTypes, busSelects, relSelects, false, true, (short)1, "", "", 0, null, null, null);
            if(mlRequirements.size()>0){
            	result[0]+=1;
            	jsonReqWithTestCases.add(objectId,withTestCases);
            }else{
            	result[1]+=1;
            	jsonReqWithTestCases.add(objectId,noTestCases);
            }
        }
        jsonObject.add("0", result[0]);
        jsonObject.add("1", result[1]);
        returnedValues.add(jsonObject.build());
        returnedValues.add(jsonReqWithTestCases.build());
        
		return returnedValues.build();
        //return jsonReqWithTestCases;
	}
	
	
	
	private JsonArray getPLMParameterValues(Context context, String objectList) throws MatrixException{
		int[] result = new int[2]; 
		String withParams = EnoviaResourceBundle.getProperty(context, "emxRequirementsStringResource", context.getLocale(), "emxRequirements.dashboard.hasParameters");  
		String noParams = EnoviaResourceBundle.getProperty(context, "emxRequirementsStringResource", context.getLocale(), "emxRequirements.dashboard.hasNoParameters");
		String sTypes           = "*";   
        String sRelationships   = ReqSchemaUtil.getParameterUsageRelationship(context)+',';
        sRelationships   = ReqSchemaUtil.getParameterAggregationRelationship(context);
        StringList busSelects = new StringList();
        StringList relSelects = new StringList();
        busSelects.add(DomainConstants.SELECT_ID);
        //busSelects.add(DomainConstants.SELECT_TYPE);  
        //busSelects.add(DomainConstants.SELECT_CURRENT);
        relSelects.add("from.id[connection]");
        
        String[] idList = objectList.split(",");
        JsonObjectBuilder jsonObject =  Json.createObjectBuilder();
        JsonObjectBuilder jsonReqWithParam = Json.createObjectBuilder();
        JsonArrayBuilder jsonReturnedValues = Json.createArrayBuilder();
        for(int i=0;i<idList.length;i++){
        	String objectId = idList[i];
        	DomainObject dObject = DomainObject.newInstance(context, objectId);
            MapList mlRequirements = dObject.getRelatedObjects(context, sRelationships, sTypes, busSelects, relSelects, false, true, (short)1, "", "", 0, null, null, null);
            if(mlRequirements.size()>0){
            	result[0]+=1;
            	jsonReqWithParam.add(objectId,withParams);
            }else{
            	result[1]+=1;
            	jsonReqWithParam.add(objectId,noParams);
            }
        }
        jsonObject.add("0", result[0]);
        jsonObject.add("1", result[1]);
        jsonReturnedValues.add(jsonObject.build());
        jsonReturnedValues.add(jsonReqWithParam.build());
        
		//return jsonObject;
        return jsonReturnedValues.build();
	}
	
	
	@com.matrixone.apps.framework.ui.ProgramCallable
	public JsonArray getRequirementsDashboardData(Context context, String[] args) throws Exception {
		JsonArrayBuilder response = Json.createArrayBuilder();
		HashMap paramMap        = (HashMap) JPO.unpackArgs(args);
        String dashboardRequest = (String) paramMap.get("requestType");
        String sObjectId = (String) paramMap.get("objectId");
        JsonObjectBuilder filter = Json.createObjectBuilder();
        filter.add("filter", dashboardRequest);
        response.add(filter.build());
        if(dashboardRequest.equalsIgnoreCase("divChartTestCase")){
        	//response.put( getTestCaseValues(context, sObjectId));
        	JsonArray result = getTestCaseValues(context, sObjectId) ;
        	response.add(result.get(0));
        	response.add(result.get(1));
        }else if(dashboardRequest.equalsIgnoreCase("divChartValidation")){
        	response.add(getTestCaseInformation(context, sObjectId));
        }else if(dashboardRequest.equalsIgnoreCase("divChartEC")){
        	response.add(getECInformation(context, sObjectId));
        }else if(dashboardRequest.equalsIgnoreCase("divChartResponsibility")){
        	response.add(getDesignResponsibilityInformation(context, sObjectId));
        }else if(dashboardRequest.equalsIgnoreCase("divChartTimeline")){
        	response.add(getTimeLineInformation(context, sObjectId));
        }else if(dashboardRequest.equalsIgnoreCase("coveredRequirements")){
        	JsonArray result = getDerivedRequirementsStatus(context, sObjectId,"from") ;
        	response.add(result.get(0));
        	response.add(result.get(1));
        	//response.put(getDerivedRequirementsStatus(context, sObjectId,"from"));
        }else if(dashboardRequest.equalsIgnoreCase("refinedRequirements")){
        	JsonArray result = getDerivedRequirementsStatus(context, sObjectId,"to") ;
        	response.add(result.get(0));
        	response.add(result.get(1));
        	//response.put(getDerivedRequirementsStatus(context, sObjectId,"to"));
        }else if(dashboardRequest.equalsIgnoreCase("subRequirements")){
        	response.add(getSubRequirements(context, sObjectId));
        }else if(dashboardRequest.equalsIgnoreCase("divChartPLMParameter")){
        	JsonArray result = getPLMParameterValues(context, sObjectId) ;
        	response.add(result.get(0));
        	response.add(result.get(1));
        	//response.put(getPLMParameterValues(context, sObjectId));
        }
		return response.build();
	} 
	
    @com.matrixone.apps.framework.ui.ProgramCallable
    public String getWeekOfTimeline(java.util.List<String> lTimeline, java.util.List<Integer> lYears, java.util.List<Integer> lWeeksMonths, String sStart, SimpleDateFormat sdf, Boolean bUseMonths) throws ParseException {
        String sResult = "0";
        if(null != sStart) {
            if(!"".equals(sStart)) {

                Calendar cStart        = Calendar.getInstance();
                cStart.setTime(sdf.parse(sStart));

                for(int j = 0; j < lTimeline.size(); j++) {

                    int iYearReference          = lYears.get(j);
                    int iWeekMonthReference     = lWeeksMonths.get(j);
                    int iYearStart              = cStart.get(Calendar.YEAR);
                    int iMonthStart             = cStart.get(Calendar.WEEK_OF_YEAR);
                    if(bUseMonths) { iMonthStart = cStart.get(Calendar.MONTH) + 1; }

                    if(iYearStart == iYearReference) {
                        if(iMonthStart <= iWeekMonthReference) {
                            sResult = String.valueOf(j+1);
                            break;
                        }
                    } else if(iYearStart <= iYearReference) {
                            sResult = String.valueOf(j+1); 
                            break;                        
                    }
                }
            }
        }
        return sResult;
    }
}


