package com.ds.utilities.widgets;

import java.io.StringReader;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.dassault_systemes.platform.restServices.RestService;
import com.ds.utilities.Exception.RestServiceException;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.FrameworkException;

import matrix.db.Page;

@Path("/WdgConfig")
public class WdgConfig extends RestService {

	@GET
	public Response getConfig(@Context HttpServletRequest request, @DefaultValue("") @QueryParam("action") String action, @QueryParam("wdg") String wdg, @QueryParam("configName") String configName, @QueryParam("configContent") String configContent) throws RestServiceException{
		return config(request, action, wdg, configName, configContent);
	}
	@POST
	public Response postConfig(@Context HttpServletRequest request, @DefaultValue("") @FormParam("action") String action, @FormParam("wdg") String wdg, @FormParam("configName") String configName, @FormParam("configContent") String configContent) throws RestServiceException{
		return config(request, action, wdg, configName, configContent);
	}
	
	public Response config(HttpServletRequest request, String action, String wdg, String configName, String configContent) throws RestServiceException{
		JsonObjectBuilder output = Json.createObjectBuilder();
		
		
		matrix.db.Context context = null;
		
		try {
			output.add("msg", "KO");
				boolean isSCMandatory = false;
				context = getAuthenticatedContext(request, isSCMandatory);
				
				if(null == wdg || wdg.isEmpty()){
					throw new Exception("Web Service WdgConfig : need a widget name (wdg)");
				}
				
				//Check page existence and create if needed
				String pageName=wdg+"WdgConf";
				Page pageConf=new Page(pageName);
				if(!pageConf.exists(context)){
					ContextUtil.pushContext(context);
					pageConf=Page.create(context, pageName, "Widget Configuration Page by DSIS.", "", "[]");
					ContextUtil.popContext(context);
				}
				
				pageConf.open(context);
				JsonReader jsonReaderConfig = Json.createReader(new StringReader(pageConf.getContents(context)));
				JsonArray configsArr = jsonReaderConfig.readArray();
				
				JsonArrayBuilder outArr = Json.createArrayBuilder();
				
				
				if(action.equals("getConfigsList")){
					for(int i=0; i<configsArr.size(); i++){
						JsonObject jsonObj = configsArr.getJsonObject(i);
						String confName = jsonObj.getString("name");
						outArr.add(confName);
					}
				}else if(action.equals("getConfig")){
					for(int i=0; i<configsArr.size(); i++){
						JsonObject jsonObj = configsArr.getJsonObject(i);
						String confName = jsonObj.getString("name");
						if(confName.equals(configName)){
							outArr.add(jsonObj);
							break;
						}
					}
					
				}else if(action.equals("saveConfig")){
					boolean saveDone=false;
					
					JsonArrayBuilder newConfigsArray = Json.createArrayBuilder();
					
					JsonObjectBuilder jsonConfigContent = Json.createObjectBuilder();
					
					Map<String, String[]> reqMap=request.getParameterMap();
					Set<String> keySet=reqMap.keySet();
					for (String keyParam : keySet) {
						if(keyParam.startsWith("configContent")){
							String jsonParam=keyParam.substring(keyParam.indexOf("[")+1, keyParam.lastIndexOf("]"));
							String jsonValue=reqMap.get(keyParam)[0];
							jsonConfigContent.add(jsonParam, jsonValue);
						}
					}
					
					for(int i=0; i<configsArr.size(); i++){
						JsonObject jsonObj = configsArr.getJsonObject(i);
						String confName = jsonObj.getString("name");
						if(confName.equals(configName)){
							JsonObjectBuilder newJsonObj = Json.createObjectBuilder();
							newJsonObj.add("name", configName);
							newJsonObj.add("prefs", jsonConfigContent);
							newConfigsArray.add(newJsonObj);
							saveDone=true;
						}else {
							newConfigsArray.add(jsonObj);
						}
					}
					if(!saveDone){
						JsonObjectBuilder jsonObj = Json.createObjectBuilder();
						jsonObj.add("name", configName);
						jsonObj.add("prefs", jsonConfigContent);
						newConfigsArray.add(jsonObj);
					}

					pageConf.setContents(context, newConfigsArray.build().toString());
					pageConf.update(context);
				}else{
					throw new Exception("Action not supported by Web Service WdgConfig");
				}
				pageConf.close(context);
				
				output.add("msg", "OK");
				output.add("data", outArr);
				
			
		} catch (Exception e) {
			try {
				if (ContextUtil.isTransactionActive(context)) {
					ContextUtil.abortTransaction(context);
				}
			} catch (FrameworkException e1) {
				e1.printStackTrace();
				throw new RestServiceException(e1);
			}
			e.printStackTrace();
			throw new RestServiceException(e);
		}
		
		return Response.status(HttpServletResponse.SC_OK).entity(output.build().toString()).build();
	}
}
