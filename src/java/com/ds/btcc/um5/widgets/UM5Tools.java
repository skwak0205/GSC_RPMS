package com.ds.btcc.um5.widgets;

import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.ApplicationPath;

import com.dassault_systemes.platform.restServices.ModelerBase;

import matrix.util.StringList;

@ApplicationPath("/UM5Tools")
public class UM5Tools extends ModelerBase {

	@Override
	public Class<?>[] getServices() {
		return new Class<?>[] {
			UM5FindObjects.class,
			UM5User.class,
			UM5ExpandObject.class,
			UM5ObjectInfos.class,
			UM5WdgConfig.class,
			UM5RelationshipInfos.class,
			UM5MailUtil.class,
			UM5DSProducts.class,
			UM5UpdateAttributes.class,
			UM5Policy.class
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
				System.out.println("UM5Tools - map2JsonBuilder : Warning Unsuported Type of Object :"+objValue.getClass().getName());
				jsonObj.add(key, objValue.toString());
			}
		}
	}

}
