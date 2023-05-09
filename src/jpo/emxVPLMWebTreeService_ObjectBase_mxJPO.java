import java.util.Map;
import java.util.Stack;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.dassault_systemes.enovia.webapps.richeditor.util.JsonUtil;
import com.dassault_systemes.requirements.ReqSchemaUtil;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.requirements.RequirementsCommon;
import com.matrixone.apps.requirements.RequirementsUtil;

import matrix.db.Context;
import matrix.db.JPO;
import matrix.util.MatrixException;
import matrix.util.StringList;


public class emxVPLMWebTreeService_ObjectBase_mxJPO extends emxDomainObject_mxJPO {
	/**
	 * 
	 * @param context
	 *            the eMatrix <code>Context</code> object
	 * @param args
	 *            holds no arguments
	 * @throws Exception
	 *             if the operation fails
	 * @since WhereUsed R212
	 * @grade 0
	 */
	public emxVPLMWebTreeService_ObjectBase_mxJPO (Context context, String[] args) throws Exception{
		super(context,args);
	}
	/**
	 * @param args
	 */
	public int mxMain(Context context, String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		return 0;
	}
	
	
	private MapList putVPLMObjectsOnJSONObject(Context context, JsonObject object) throws MatrixException{
		String[] strRowId = new String[1];
		String strTypeName = object.getString(SELECT_TYPE);
		MapList mapLstAllVPLMObjects = new MapList();
		JsonArrayBuilder JSONArrChildren = null;
		if(object.isNull("children")){
			JSONArrChildren = Json.createArrayBuilder();
		}else{
			JSONArrChildren = JsonUtil.jsonArrBuilder(object.getJsonArray("children"));
		}
		if(strTypeName.equalsIgnoreCase(ReqSchemaUtil.getRequirementSpecificationType(context))||strTypeName.equalsIgnoreCase(ReqSchemaUtil.getRequirementType(context))){
			strRowId[0] = object.getString(SELECT_ID);
			MapList functionalList = RequirementsCommon.getVPLMFRoots(context, "Functional", strRowId);
			for(int i = 0; i<functionalList.size();i++)
			{
				Map childMap = (Map)functionalList.get(i);
				JsonObjectBuilder JSONObjNewChild = Json.createObjectBuilder();
				JSONObjNewChild.add("id", (String)childMap.get("PLM_FunctionRoot"));
				JSONObjNewChild.add("name", (String)childMap.get("PLM_FunctionName"));
				JSONArrChildren.add(JSONObjNewChild.build());
			}
			MapList LogicalList = RequirementsCommon.getVPLMFRoots(context,"Logical", strRowId);
			for(int i = 0; i<LogicalList.size();i++)
			{
				Map childMap = (Map)LogicalList.get(i);
				JsonObjectBuilder JSONObjNewChild = Json.createObjectBuilder();
				JSONObjNewChild.add("id", (String)childMap.get("PLM_LogicalRoot"));
				JSONObjNewChild.add("name", (String)childMap.get("PLM_LogicalName"));
				JSONArrChildren.add(JSONObjNewChild.build());
			}
			MapList PhysicalList = RequirementsCommon.getVPLMFRoots(context, "Physical", strRowId);
			for(int i = 0; i<PhysicalList.size();i++)
			{
				Map childMap = (Map)PhysicalList.get(i);
				JsonObjectBuilder JSONObjNewChild = Json.createObjectBuilder();
				JSONObjNewChild.add("id", (String)childMap.get("PLM_PhysicalRoot"));
				JSONObjNewChild.add("name", (String)childMap.get("PLM_PhysicalName"));
				JSONArrChildren.add(JSONObjNewChild.build());
			}
			mapLstAllVPLMObjects.addAll(functionalList);
			mapLstAllVPLMObjects.addAll(LogicalList);
			mapLstAllVPLMObjects.addAll(PhysicalList);
		}else{
			mapLstAllVPLMObjects = null;
		}
		return mapLstAllVPLMObjects;
	}
	
	private Stack getChildrenStruct(Context context, Stack objectStack, MapList listOfMap, boolean boolGetFLObjects) throws MatrixException{
			int level = 0;
			for(int i = 0; i<listOfMap.size(); i++){
				JsonObjectBuilder JSONObjStackObject = (JsonObjectBuilder)objectStack.peek();
				String strStackLevel = JSONObjStackObject.build().getString(SELECT_LEVEL);
				int iStackLevel = Integer.parseInt(strStackLevel);
				
				Map MapObject = (Map)listOfMap.get(i);
				String strId       = (String)MapObject.get(SELECT_ID);
				String strName     = (String)MapObject.get(SELECT_NAME);
				String strType     = (String)MapObject.get(SELECT_TYPE);
				String strLevel     = (String)MapObject.get(SELECT_LEVEL);
				String strRelType    = (String)MapObject.get(SELECT_RELATIONSHIP_TYPE);
				
				JsonObjectBuilder JSONObject = Json.createObjectBuilder();
				JSONObject.add(SELECT_ID,strId);
				JSONObject.add(SELECT_NAME,strName);
				JSONObject.add(SELECT_TYPE,strType);
				JSONObject.add(SELECT_LEVEL,strLevel);
				JSONObject.add(SELECT_RELATIONSHIP_TYPE,strRelType);
				String strIconPath = RequirementsCommon.getIconForType(context, strType, strRelType);
				JSONObject.add("icon",strIconPath);
				int iActualLevel = Integer.parseInt(strLevel);
				
				if(iActualLevel>iStackLevel){
					//List<JSONObject> list = new List<JSONObject>();
					JsonArrayBuilder array = Json.createArrayBuilder();
					//list.add(JSONObject);
					array.add(JSONObject.build());
					objectStack.add(JSONObject.build());
					//stackObject.put("children", list);
					JSONObjStackObject.add("children", array.build());
					if(boolGetFLObjects == true){
						putVPLMObjectsOnJSONObject(context, JSONObjStackObject.build());
					}					
				}else{
					//boolean isLeaf=false;
					while( iStackLevel >=  iActualLevel){
						JsonObject temp2 = (JsonObject)objectStack.pop();
						//if(isLeaf==false)
						//{
							//isLeaf=true;
							//temp2.put("leaf", "true");
						//}
						JsonObject JSONOTemp = (JsonObject)objectStack.peek();
						iStackLevel = Integer.parseInt(JSONOTemp.getString(SELECT_LEVEL));
					}
					JsonObjectBuilder previousObjectStack =  (JsonObjectBuilder)objectStack.peek();
					//List tempList = (List)previousObjectStack.get("children");
					JsonArray tempList = (JsonArray)previousObjectStack.build().getJsonArray("children");
					//tempList.add(JSONObject);
					tempList.add(JSONObject.build());
					previousObjectStack.add("children", tempList);
					objectStack.push(JSONObject.build());
				}
			}
		JsonObject JSONObjLastObject =  (JsonObject)objectStack.peek();
		//lastObject.put("leaf", "true");
		if(boolGetFLObjects == true){
			putVPLMObjectsOnJSONObject(context, JSONObjLastObject);
		}
		return objectStack;
	}
	
	private JsonObject createJSONStruct(Context context, Map rootObject, MapList children, boolean boolGetFLObjects) throws MatrixException{
		JsonObjectBuilder JSONStruct = Json.createObjectBuilder();
		JSONStruct.add(SELECT_ID, (String)rootObject.get(SELECT_ID));
		JSONStruct.add(SELECT_NAME, (String)rootObject.get(SELECT_NAME));
		String strType = (String)rootObject.get(SELECT_TYPE);
		JSONStruct.add(SELECT_TYPE, strType);
		String strIconPath = RequirementsCommon.getIconForType(context, strType, null);
		JSONStruct.add("icon", strIconPath);
		JSONStruct.add(SELECT_LEVEL, "0");
		Stack objectStack = new Stack();
		objectStack.push(JSONStruct.build());
		if(children != null){
			getChildrenStruct(context,objectStack, children, boolGetFLObjects);
		}
		return JSONStruct.build();
	}

	
	private String getChildrenObjectTypesFromId(Context context, String objectId) throws FrameworkException{
		DomainObject domOject = DomainObject.newInstance(context, objectId);
		domOject.openObject(context);
		String strObjectType = domOject.getType(context);
		String strObjTypes = null;;
		if(ReqSchemaUtil.getRequirementSpecificationType(context).equalsIgnoreCase(strObjectType)||
			ReqSchemaUtil.getChapterType(context).equalsIgnoreCase(strObjectType)){
			strObjTypes = ReqSchemaUtil.getRequirementType(context) + "," +
            ReqSchemaUtil.getChapterType(context) + "," +
            ReqSchemaUtil.getCommentType(context);
		}else if(ReqSchemaUtil.getRequirementType(context).equalsIgnoreCase(strObjectType)){
			strObjTypes = ReqSchemaUtil.getRequirementType(context);
		}else if(ReqSchemaUtil.getCommentType(context).equalsIgnoreCase(strObjectType)){
			strObjTypes = "";
		}else{
			strObjTypes = null;
		}
		return strObjTypes;
	}
	
	private String getToRelTypesFromId(Context context, String objectId, int depth) throws FrameworkException{
		DomainObject domOject = DomainObject.newInstance(context, objectId);
		domOject.openObject(context);
		String strObjectType = domOject.getType(context);
		String strRelTypes = null;;
		if(ReqSchemaUtil.getRequirementSpecificationType(context).equalsIgnoreCase(strObjectType)||
			ReqSchemaUtil.getChapterType(context).equalsIgnoreCase(strObjectType)){
			if(depth == 1){
				strRelTypes = 	ReqSchemaUtil.getSpecStructureRelationship(context);
			}else{
				strRelTypes = 	ReqSchemaUtil.getSpecStructureRelationship(context) + "," + 
				ReqSchemaUtil.getSubRequirementRelationship(context)+ "," +
				ReqSchemaUtil.getDerivedRequirementRelationship(context);
			}
		}else if(ReqSchemaUtil.getRequirementType(context).equalsIgnoreCase(strObjectType)){
			strRelTypes = ReqSchemaUtil.getSubRequirementRelationship(context)+ "," +
							ReqSchemaUtil.getDerivedRequirementRelationship(context);;
		}else if(ReqSchemaUtil.getCommentType(context).equalsIgnoreCase(strObjectType)){
			strRelTypes = "";
		}else{
			strRelTypes = null;
		}
		return strRelTypes;
	}
	
	private String getParentObjectTypesFromId(Context context, String objectId) throws FrameworkException{
		DomainObject domOject = DomainObject.newInstance(context, objectId);
		domOject.openObject(context);
		String strObjectType = domOject.getType(context);
		String strObjTypes = null;;
		if(ReqSchemaUtil.getRequirementSpecificationType(context).equalsIgnoreCase(strObjectType)){
			strObjTypes = "";
		}else if(ReqSchemaUtil.getChapterType(context).equalsIgnoreCase(strObjectType)){
			strObjTypes =   ReqSchemaUtil.getChapterType(context) + "," +
							ReqSchemaUtil.getRequirementSpecificationType(context);
		}else if(ReqSchemaUtil.getRequirementType(context).equalsIgnoreCase(strObjectType)){
			strObjTypes =   ReqSchemaUtil.getRequirementType(context)+ "," +
							ReqSchemaUtil.getChapterType(context) + "," +
							ReqSchemaUtil.getRequirementSpecificationType(context);
		}else if(ReqSchemaUtil.getCommentType(context).equalsIgnoreCase(strObjectType)){
			strObjTypes = ReqSchemaUtil.getChapterType(context) + "," +
						  ReqSchemaUtil.getRequirementSpecificationType(context);
		}else{
			strObjTypes = null;
		}
		return strObjTypes;
	}
	
	private String getFromRelTypesFromId(Context context, String objectId) throws FrameworkException{
		DomainObject domOject = DomainObject.newInstance(context, objectId);
		domOject.openObject(context);
		String strObjectType = domOject.getType(context);
		String strRelTypes = null;;
		if(ReqSchemaUtil.getRequirementSpecificationType(context).equalsIgnoreCase(strObjectType)){
			strRelTypes = 	"";

		}else if(ReqSchemaUtil.getChapterType(context).equalsIgnoreCase(strObjectType)){
			strRelTypes = ReqSchemaUtil.getSpecStructureRelationship(context);
		}else if(ReqSchemaUtil.getRequirementType(context).equalsIgnoreCase(strObjectType)){
			strRelTypes =   ReqSchemaUtil.getSpecStructureRelationship(context) + "," + 
							ReqSchemaUtil.getSubRequirementRelationship(context)+ "," +
							ReqSchemaUtil.getDerivedRequirementRelationship(context);;
		}else if(ReqSchemaUtil.getCommentType(context).equalsIgnoreCase(strObjectType)){
			strRelTypes = ReqSchemaUtil.getSpecStructureRelationship(context);
		}else{
			strRelTypes = null;
		}
		return strRelTypes;
	}
	
	public JsonObject getChildren(Context context, String[] args)
	{
		JsonObject JSONOStruct = null;
		try {
			Map paramMap = (Map)JPO.unpackArgs(args);
			
			String strObjectId = ((String[])paramMap.get("id"))[0];
			String strDepth = ((String[])paramMap.get("depth"))[0];
			int iDepth = Integer.parseInt(strDepth);
			String strObjTypes = getChildrenObjectTypesFromId(context, strObjectId);
			String strRelTypes = getToRelTypesFromId(context, strObjectId, iDepth);
			
	        StringList strListSelectStmts = new StringList(5);
	        strListSelectStmts.addElement(SELECT_ID);
	        strListSelectStmts.addElement(SELECT_TYPE);
	        strListSelectStmts.addElement(SELECT_NAME);
	        strListSelectStmts.addElement(SELECT_REVISION);
	        
	        StringList strListRelSelect = new StringList(SELECT_RELATIONSHIP_ID);
	        strListRelSelect.addElement("attribute[" + RequirementsUtil.getSequenceOrderAttribute(context) + "]");
	        strListRelSelect.add(DomainRelationship.SELECT_FROM_ID);
	        strListRelSelect.add(DomainRelationship.SELECT_TO_ID);
	        strListRelSelect.addElement(SELECT_FROM_ID);
	        strListRelSelect.addElement(SELECT_RELATIONSHIP_TYPE);  
	        DomainObject domObject = DomainObject.newInstance(context, strObjectId);
	        
	        MapList childObjects = null;
	        if(iDepth != 0){
	        	if(iDepth == -1){
		        	iDepth = 0;
		        }
	        	iDepth=iDepth==-1?0:iDepth;
	        	childObjects = domObject.getRelatedObjects(context, strRelTypes, strObjTypes,
						strListSelectStmts, strListRelSelect, false, true, (short) iDepth, null, null);
	        }
			domObject.openObject(context);
			StringList strListAttibuteList = new StringList();
			strListAttibuteList.add(SELECT_ID);
			strListAttibuteList.add(SELECT_LEVEL);
			strListAttibuteList.add(SELECT_TYPE);
			strListAttibuteList.add(SELECT_NAME);
			strListAttibuteList.add(SELECT_REVISION);
			Map infos = domObject.getInfo(context, strListAttibuteList);
			domObject.close(context);
			JSONOStruct = createJSONStruct(context, infos, childObjects,true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSONOStruct;
	}
	
	private JsonObject getTestCaseUseCaseObjects(Context context, String strObjectId) throws Exception{
		MapList childObjects = null;
		JsonObject JSONOStruct = null;
		
		StringList strListSelectStmts = new StringList(5);
        strListSelectStmts.addElement(SELECT_ID);
        strListSelectStmts.addElement(SELECT_TYPE);
        strListSelectStmts.addElement(SELECT_NAME);
        strListSelectStmts.addElement(SELECT_REVISION);
        
        StringList strListRelSelect = new StringList(SELECT_RELATIONSHIP_ID);
        strListRelSelect.addElement("attribute[" + RequirementsUtil.getSequenceOrderAttribute(context) + "]");
        strListRelSelect.add(DomainRelationship.SELECT_FROM_ID);
        strListRelSelect.add(DomainRelationship.SELECT_TO_ID);
        strListRelSelect.addElement(SELECT_FROM_ID);
        strListRelSelect.addElement(SELECT_RELATIONSHIP_TYPE);  
        
        String strRelTypes = ReqSchemaUtil.getRequirementValidationRelationship(context)+ "," +
		  					 ReqSchemaUtil.getSubTestCaseRelationship(context)/* + "," +  
		  					 ReqSchemaUtil.getSubUseCaseRelationship(context) + "," +
        					 ReqSchemaUtil.getRequirementUseCaseRelationship(context)*/;
        					  
        
        String strObjTypes = ReqSchemaUtil.getTestCaseType(context)/* + "," +
        					 ReqSchemaUtil.getUseCaseType(context)*/;
        
        DomainObject  domObject = DomainObject.newInstance(context, strObjectId);
        childObjects = domObject.getRelatedObjects(context, strRelTypes, strObjTypes,
				strListSelectStmts, strListRelSelect, false, true, (short) 0, null, null);
        
        StringList strListAttibuteList = new StringList();
		strListAttibuteList.add(SELECT_ID);
		strListAttibuteList.add(SELECT_LEVEL);
		strListAttibuteList.add(SELECT_TYPE);
		strListAttibuteList.add(SELECT_NAME);
		strListAttibuteList.add(SELECT_REVISION);
		Map infos = domObject.getInfo(context, strListAttibuteList);
		domObject.close(context);
		JSONOStruct = createJSONStruct(context, infos, childObjects,false);
        return JSONOStruct;
	}
	
	private JsonObject getParentsObjects(Context context, String strObjectId) throws Exception{
		JsonObject JSONOParentStruct = null;
		String strObjTypes = getParentObjectTypesFromId(context, strObjectId);
		String strRelTypes = getFromRelTypesFromId(context, strObjectId);
		StringList strListSelectStmts = new StringList(5);
        strListSelectStmts.addElement(SELECT_ID);
        strListSelectStmts.addElement(SELECT_TYPE);
        strListSelectStmts.addElement(SELECT_NAME);
        strListSelectStmts.addElement(SELECT_REVISION);
        
        StringList strListRelSelect = new StringList(SELECT_RELATIONSHIP_ID);
        strListRelSelect.addElement("attribute[" + RequirementsUtil.getSequenceOrderAttribute(context) + "]");
        strListRelSelect.add(DomainRelationship.SELECT_FROM_ID);
        strListRelSelect.add(DomainRelationship.SELECT_TO_ID);
        strListRelSelect.addElement(SELECT_FROM_ID);
        strListRelSelect.addElement(SELECT_RELATIONSHIP_TYPE);  
        DomainObject domObject = DomainObject.newInstance(context, strObjectId);
        
        MapList childObjects = null;
    	childObjects = domObject.getRelatedObjects(context, strRelTypes, strObjTypes,
				strListSelectStmts, strListRelSelect, true, false, (short) 0, null, null);
		domObject.openObject(context);
		StringList strListAttibuteList = new StringList();
		strListAttibuteList.add(SELECT_ID);
		strListAttibuteList.add(SELECT_LEVEL);
		strListAttibuteList.add(SELECT_TYPE);
		strListAttibuteList.add(SELECT_NAME);
		strListAttibuteList.add(SELECT_REVISION);
		Map infos = domObject.getInfo(context, strListAttibuteList);
		domObject.close(context);
		JSONOParentStruct = createJSONStruct(context, infos, childObjects,true);
		return JSONOParentStruct;
	}
	private JsonArray concat2JSONArrays(JsonArray array1,JsonArray array2) throws Exception{
		for(int i = 0 ; i<array2.size();i++){
			JsonObject object = array2.getJsonObject(i);
			array1.add(object);
		}
		return array1;
	}
	
	public JsonObject getParents(Context context, String[] args)
	{
		Map paramMap;
		JsonObject JSONOTestCaseUseCaseStruct = null;
		JsonObject JSONOParentStruct = null;
		JsonObject JSONStruct = null;
		JsonArray Children = null;
		try {
			paramMap = (Map)JPO.unpackArgs(args);
			String strObjectId = ((String[])paramMap.get("id"))[0];
			String strDepth = ((String[])paramMap.get("depth"))[0];
			JsonArray testCaseUseCaseArray = null;
	
			JSONOTestCaseUseCaseStruct = getTestCaseUseCaseObjects(context, strObjectId);
			if(JSONOTestCaseUseCaseStruct.containsKey("children")){
				testCaseUseCaseArray =  JSONOTestCaseUseCaseStruct.getJsonArray("children");
			}	
			
			JSONOParentStruct = getParentsObjects(context, strObjectId);
			JsonArray parentArray = null;
			if(JSONOParentStruct.containsKey("children")){
				parentArray = JSONOParentStruct.getJsonArray("children");
			}	
			if(parentArray != null && testCaseUseCaseArray != null){
				Children = concat2JSONArrays(parentArray,testCaseUseCaseArray);
				JSONStruct = JsonUtil.jsonObjBuilder(JSONOParentStruct).add("children", Children).build();
			}else if(parentArray!=null){
				JSONStruct = JSONOParentStruct;
			}else if(testCaseUseCaseArray!=null){
				JSONStruct = JSONOTestCaseUseCaseStruct;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSONStruct;
	}
}
