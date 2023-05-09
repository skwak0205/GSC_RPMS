package com.ds.utilities.widgets;

import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.ApplicationPath;

import com.dassault_systemes.platform.restServices.ModelerBase;

import matrix.util.StringList;
import common.*;

@ApplicationPath("/DSISTools")
public class Tools extends ModelerBase {

	@Override
	public Class<?>[] getServices() {
		return new Class<?>[] {
			FindObjects.class,
			User.class,
			ExpandObject.class,
			ObjectInfos.class,
			WdgConfig.class,
			RelationshipInfos.class,
			DSISMailUtil.class,
			DSProducts.class,
			UpdateAttributes.class,
			Policy.class,
			gscFindObjects.class
			};
	}

	public static void map2JsonBuilder(JsonObjectBuilder jsonObj, Map<String, Object> mapObj) {
		for (String key : mapObj.keySet()) {
			Object objValue = mapObj.get(key);
			if(objValue instanceof String) {
				jsonObj.add(key, (String) objValue);
			}else if(objValue instanceof StringList) {
				StringList objValueList = (StringList) objValue;
				JsonArrayBuilder jsonArr = Json.createArrayBuilder();
				for (String stringVal : objValueList) {
					jsonArr.add(stringVal);
				}
				jsonObj.add(key, jsonArr);
			}else{
				System.out.println("DSIS Tools - map2JsonBuilder : Warning Unsuported Type of Object :"+objValue.getClass().getName());
				jsonObj.add(key, objValue.toString());
			}
		}
	}

	public static void gscmap2JsonBuilder(JsonObjectBuilder jsonObj, Map<String, Object> mapObj) {

		for (String key : mapObj.keySet()) {

			// HJ - Custom Remove Attr & gscCheck & ToCamelCaseExcludeFirst
			String gsckey = key;

			if(gscCommon.gscCheckParamAttr(gsckey)) {
				gsckey = gscSetParamAttr.gscRemoveParamAttr(key);

				if(!gscCommon.gscCheckgsc(gsckey)){
					if(gscCommon.gscCheckUpperCase(gsckey) > 0){
						gsckey = gscCommon.gscToCamelCase(gsckey).get("ToCamelText");
					}
				}
			}

			Object objValue = mapObj.get(key);
			if(objValue instanceof String) {
				jsonObj.add(gsckey, (String) objValue);
			}else if(objValue instanceof StringList) {
				StringList objValueList = (StringList) objValue;
				JsonArrayBuilder jsonArr = Json.createArrayBuilder();
				for (String stringVal : objValueList) {
					jsonArr.add(stringVal);
				}
				jsonObj.add(gsckey, jsonArr);
			}else{
				System.out.println("DSIS Tools - map2JsonBuilder : Warning Unsuported Type of Object :"+objValue.getClass().getName());
				jsonObj.add(gsckey, objValue.toString());
			}
		}
	}

}
