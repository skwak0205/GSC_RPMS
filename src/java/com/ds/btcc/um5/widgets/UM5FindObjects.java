package com.ds.btcc.um5.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.dassault_systemes.platform.restServices.RestService;
import com.ds.btcc.um5.Exception.UM5RestServiceException;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.EnoviaResourceBundle;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.framework.ui.UINavigatorUtil;

import matrix.db.JPO;
import matrix.util.StringList;

@Path("/Find")
public class UM5FindObjects extends RestService {

	@GET
	public Response getFind(@Context HttpServletRequest request, @QueryParam("type") String typeObj,
			@QueryParam("selects") String selects, @QueryParam("where") String whereExpr,
			@QueryParam("findProgram") String findProgram, @QueryParam("findFunction") String findFunction,
			@QueryParam("findParams") String findParams) throws UM5RestServiceException {
		return find(request, typeObj, selects, whereExpr, findProgram, findFunction, findParams);
	}

	@POST
	public Response postFind(@Context HttpServletRequest request, @FormParam("type") String typeObj,
			@FormParam("selects") String selects, @FormParam("where") String whereExpr,
			@FormParam("findProgram") String findProgram, @FormParam("findFunction") String findFunction,
			@FormParam("findParams") String findParams) throws UM5RestServiceException {
		return find(request, typeObj, selects, whereExpr, findProgram, findFunction, findParams);
	}

	//@SuppressWarnings("unchecked")
	@SuppressWarnings("unchecked")
	public Response find(HttpServletRequest request, String typeObj, String selects, String whereExpr,
			String findProgram, String findFunction, String findParams) throws UM5RestServiceException {

		// Test sample :
		// https://3dexp.16xfd04.ds/3DSpace/UM5Tools/Find?type=Task*&selects=name,revision,current&where=name%20~=%20Ph*

		JsonObjectBuilder output = Json.createObjectBuilder();

		matrix.db.Context context = null;
		
		try {
			//output.put("msg", "KO");
			output.add("msg", "KO");

			// context = authenticate(request); //Deprecated in 17x
			boolean isSCMandatory = false;
			context = getAuthenticatedContext(request, isSCMandatory); // New way to get context in 17x

			ContextUtil.startTransaction(context, false);
			StringList slObj = new StringList();
			slObj.addElement("id");
			slObj.addElement("physicalid");
			slObj.addElement("type");
			slObj.addElement("current");
			slObj.addElement("policy");
			/*
			 * slObj.add("physicalid"); slObj.add("type"); slObj.add("type.kindof");
			 * slObj.add("name"); slObj.add("revision");
			 */

			// System.out.println("selects="+selects);

			ArrayList<String> jpoSelects = new ArrayList<String>();
			if (null != selects && !selects.isEmpty()) {
				String[] arrSelects = selects.split(",");
				for (int i = 0; i < arrSelects.length; i++) {
					String select = arrSelects[i];
					if (select.startsWith("JPO-")) {
						jpoSelects.add(select);
					} else {
						slObj.add(select);
					}
				}
			}

			// System.out.println("slObj="+slObj);

			if (null == whereExpr || whereExpr.isEmpty()) {
				whereExpr = "";
			}

			MapList mlObjs = null;

			if (null != findProgram && null != findFunction && !findProgram.isEmpty() && !findFunction.isEmpty()) {
				HashMap<String, String> mapArgs = new HashMap<String, String>();

				// System.out.println("expandParams="+expandParams);
				if (!findParams.isEmpty()) {
					String[] pairsParams = findParams.split("&");
					for (int i = 0; i < pairsParams.length; i++) {
						String[] pair = pairsParams[i].split("=");
						if (pair.length >= 2) {
							mapArgs.put(pair[0], pair[1]);
						}
					}
				}

				String[] args = JPO.packArgs(mapArgs);
				mlObjs = (MapList) JPO.invoke(context, findProgram, args, findFunction, args, MapList.class);

				MapList mlRes = new MapList();

				for (Object obj : mlObjs) {
					Map<String, Object> mapObj = (Map<String, Object>) obj;
					String oidTest = (String) mapObj.get("id");
					// Make sure that there is an object id, filter objects for some expand program
					// where last or first map is not an object
					if (null != oidTest && !oidTest.isEmpty()) {
						DomainObject dom = new DomainObject(oidTest);

						@SuppressWarnings("rawtypes")
						Map mapRes = dom.getInfo(context, slObj);

						mlRes.add(mapRes);
					}
				}
				
				mlObjs = mlRes;
			} else {
				// findObjects(Context context, String typePattern, String vaultPattern, String
				// whereExpression, StringList objectSelects)
				mlObjs = DomainObject.findObjects(context, typeObj, "*", whereExpr, slObj);
			}

			Map<String, Vector<String>> mapJPORes = new HashMap<String, Vector<String>>();
			for (String selectJPO : jpoSelects) {
				String[] selElems = selectJPO.split("-");
				if (selElems.length >= 3) {
					String prog = selElems[1];
					String funct = selElems[2];

					HashMap<String, Object> mapArgs = new HashMap<String, Object>();
					mapArgs.put("objectList", mlObjs);

					String[] args = JPO.packArgs(mapArgs);

					Vector<String> vRes = (Vector<String>) JPO.invoke(context, prog, args, funct, args, Vector.class);
					mapJPORes.put(selectJPO, vRes);
				}
			}

			//JSONArray outArr = new JSONArray();
			JsonArrayBuilder outArr = Json.createArrayBuilder();

			for (int i = 0; i < mlObjs.size(); i++) {
				Map<String, Object> mapObj = (Map<String, Object>) mlObjs.get(i);

				// Add Specific JPO selects
				for (String selectJPO : jpoSelects) {
					String val = mapJPORes.get(selectJPO).get(i);
					mapObj.put(selectJPO, val);
				}

				// Add NLS
				for (Object keySelect : slObj) {
					try {
						String strKey = (String) keySelect;
						String value = (String) mapObj.get(strKey);
						if (null != value) {
							String nlsType = "";
							String nlsInfo = "";
							if (strKey.equals("type")) {
								nlsType = "Type";
								nlsInfo = value.trim().replaceAll(" ", "_");
							} else if (strKey.equals("current")) {
								nlsType = "State";

								String strPolicy = (String) mapObj.get("policy");
								strPolicy = strPolicy.replaceAll(" ", "_");

								nlsInfo = strPolicy + "." + value.replaceAll(" ", "_");
							}

							if (!nlsType.isEmpty()) {
								String strValueNLS = EnoviaResourceBundle.getFrameworkStringResourceProperty(context,
										"emxFramework." + nlsType + "." + nlsInfo, request.getLocale());

								if (null != strValueNLS && !strValueNLS.equals("")
										&& !strValueNLS.startsWith("emxFramework.")) {
									mapObj.put("nls!" + strKey, strValueNLS);
								}
							}
						}
					} catch (Exception ex) {
						// Silent catch
					}
				}

				//JSONObject jsonObj = new JSONObject(mapObj);
				JsonObjectBuilder jsonObj = Json.createObjectBuilder();
				UM5Tools.map2JsonBuilder(jsonObj, mapObj);
				
				String type = (String) mapObj.get("type");
				if (null != type && !type.isEmpty()) {
					String icon = UINavigatorUtil.getTypeIconProperty(context, type);
					if (icon != null && !icon.isEmpty()) {
						//jsonObj.put("iconType", "/common/images/" + icon);
						jsonObj.add("iconType", "/common/images/" + icon);
					}
				}
				//outArr.put(jsonObj);
				outArr.add(jsonObj);
			}

			//output.put("msg", "OK");
			//output.put("data", outArr);
			output.add("msg", "OK");
			output.add("data", outArr);

			ContextUtil.commitTransaction(context);

		} catch (Exception e) {
			try {
				if (ContextUtil.isTransactionActive(context)) {
					ContextUtil.abortTransaction(context);
				}
			} catch (FrameworkException e1) {
				e1.printStackTrace();
				throw new UM5RestServiceException(e1);
			}
			e.printStackTrace();
			throw new UM5RestServiceException(e);
		}

		return Response.status(HttpServletResponse.SC_OK).entity(output.build().toString()).build();
	}
}
