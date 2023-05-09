package com.ds.utilities.widgets;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.dassault_systemes.platform.restServices.RestService;
import com.ds.utilities.Exception.RestServiceException;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.MqlUtil;

@Path("/Policy")
public class Policy extends RestService {

	public static final String TMP_DIR = System.getProperty("java.io.tmpdir");
	public static Map<String, String> mapRequestToFileName = new HashMap<>();

	@GET
	@Path("/list")
	public Response getList(@Context HttpServletRequest request, @QueryParam("namePattern") String namePattern,
			@QueryParam("where") String where) throws RestServiceException {
		return list(request, namePattern, where);
	}

	@POST
	@Path("/list")
	public Response postList(@Context HttpServletRequest request, @FormParam("namePattern") String namePattern,
			@FormParam("where") String where) throws RestServiceException {
		return list(request, namePattern, where);
	}

	public Response list(HttpServletRequest request, String namePattern, String where) throws RestServiceException {

		String outputStr = "";
		JsonObjectBuilder output = Json.createObjectBuilder();

		matrix.db.Context context = null;

		try {
			output.add("msg", "KO");

				boolean isSCMandatory = false;
				context = getAuthenticatedContext(request, isSCMandatory);

				// try to get Data from cache
				outputStr = getDataFromCache(namePattern+"$|$"+where);

				if (outputStr.isEmpty()) {
					ContextUtil.startTransaction(context, false);

						String strWhereClause = "";
						if (null != where && !where.isEmpty()) {
							strWhereClause = " where " + where;
						}
						JsonArrayBuilder outArr = Json.createArrayBuilder();

						String mqlRes = MqlUtil.mqlCommand(context, "list policy '" + namePattern + "'" + strWhereClause
								+ " select name minorsequence majorsequence delimiter dump |", true, false);

						String[] mqlLines = mqlRes.split("\n");
						for (int i = 0; i < mqlLines.length; i++) {
							String line = mqlLines[i];

							if (line.isEmpty())
								break;

							String[] arrValues = line.split("\\|", -2);
							JsonObjectBuilder jsonObj = Json.createObjectBuilder();

							jsonObj.add("name", arrValues[0]);
							jsonObj.add("minorsequence", arrValues[1]);
							jsonObj.add("majorsequence", arrValues[2]);
							jsonObj.add("delimiter", arrValues[3]);

							outArr.add(jsonObj);
						}

						output.add("msg", "OK");
						output.add("data", outArr);

						ContextUtil.commitTransaction(context);
						outputStr = output.build().toString();
						
						addDataToCache(namePattern+"$|$"+where, outputStr);
					
				}
			
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

		return Response.status(HttpServletResponse.SC_OK).entity(outputStr).build();
	}

	private void addDataToCache(String cacheName, String cachedData) {
		mapRequestToFileName.put(cacheName, cachedData);
	}

	private String getDataFromCache(String cacheName) {
		String res = "";
		res = mapRequestToFileName.get(cacheName);
		if(null == res) {
			res="";
		}
		return res;
	}

	@GET
	@Path("/{policyName}/infos")
	public Response getPolicyInfos(@Context HttpServletRequest request, @PathParam("policyName") String policyName) throws RestServiceException {
		return policyInfos(request, policyName);
	}

	@POST
	@Path("/{policyName}/infos")
	public Response postPolicyInfos(@Context HttpServletRequest request, @PathParam("policyName") String policyName) throws RestServiceException {
		return policyInfos(request, policyName);
	}

	public Response policyInfos(HttpServletRequest request, String policyName) throws RestServiceException {
		JsonObjectBuilder output = Json.createObjectBuilder();

		matrix.db.Context context = null;

		try {
			output.add("msg", "KO");

				boolean isSCMandatory = false;
				context = getAuthenticatedContext(request, isSCMandatory);

				ContextUtil.startTransaction(context, false);
					JsonObjectBuilder jsonPolicy = Json.createObjectBuilder();

					jsonPolicy.add("types", policyTypes(context, policyName));
					jsonPolicy.add("allstate", policyAllState(context, policyName));
					jsonPolicy.add("states", policyStates(context, policyName));

					output.add("msg", "OK");
					output.add("data", jsonPolicy);

					ContextUtil.commitTransaction(context);
				
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

	private JsonArrayBuilder policyTypes(matrix.db.Context context, String policyName) throws Exception {
		JsonArrayBuilder jsonArray = Json.createArrayBuilder();

		String mqlRes = MqlUtil.mqlCommand(context, "print policy '" + policyName + "' select type dump |", true,
				false);

		String[] mqlLines = mqlRes.split("\n");
		for (int i = 0; i < mqlLines.length; i++) {
			String line = mqlLines[i];

			if (line.isEmpty())
				break;

			String[] arrValues = line.split("\\|", -2);

			for (int j = 0; j < arrValues.length; j++) {
				String type = arrValues[j];

				jsonArray.add(type);
			}
		}
		return jsonArray;
	}

	private JsonArrayBuilder policyAllState(matrix.db.Context context, String policyName) throws Exception {
		return getStateInfos(context, policyName, "allstate");
	}

	private JsonArrayBuilder policyStates(matrix.db.Context context, String policyName) throws Exception {
		JsonArrayBuilder jsonArray = Json.createArrayBuilder();

		String mqlRes = MqlUtil.mqlCommand(context, "print policy '" + policyName + "' select state dump |", true,
				false);

		String[] mqlLines = mqlRes.split("\n");
		for (int i = 0; i < mqlLines.length; i++) {
			String line = mqlLines[i];

			if (line.isEmpty())
				break;

			String[] arrValues = line.split("\\|", -2);

			for (int j = 0; j < arrValues.length; j++) {
				String val = arrValues[j];
				JsonObjectBuilder jsonObj = Json.createObjectBuilder();
				jsonObj.add("name", val);
				jsonObj.add("data", getStateInfos(context, policyName, "state[" + val + "]"));
				jsonArray.add(jsonObj);
			}
		}
		return jsonArray;
	}

	private JsonArrayBuilder getStateInfos(matrix.db.Context context, String policyName, String stateName) throws Exception {
		JsonArrayBuilder jsonArray = Json.createArrayBuilder();

		String mqlRes = MqlUtil.mqlCommand(context,
				"print policy '" + policyName + "' select " + stateName + ".owner dump |", true, false);

		String[] mqlLines = mqlRes.split("\n");
		for (int i = 0; i < mqlLines.length; i++) {
			String line = mqlLines[i];

			if (line.isEmpty())
				break;

			String[] arrValues = line.split("\\|", -2);

			for (int j = 0; j < arrValues.length; j++) {
				String val = arrValues[j];
				if (val.equalsIgnoreCase("TRUE")) {
					JsonObjectBuilder jsonObj = getStateInfos(context, policyName, stateName, "owner");
					jsonObj.add("owner", true);
					jsonArray.add(jsonObj);
				} else {
					JsonObjectBuilder jsonObj = getStateInfos(context, policyName, stateName, "owner[" + val + "]");
					jsonObj.add("owner", true);
					jsonArray.add(jsonObj);
				}
			}
		}

		mqlRes = MqlUtil.mqlCommand(context,
				"print policy '" + policyName + "' select " + stateName + ".public dump |", true, false);
		mqlLines = mqlRes.split("\n");
		for (int i = 0; i < mqlLines.length; i++) {
			String line = mqlLines[i];

			if (line.isEmpty())
				break;

			String[] arrValues = line.split("|", -2);

			for (int j = 0; j < arrValues.length; j++) {
				String val = arrValues[j];
				if (val.equalsIgnoreCase("TRUE")) {
					JsonObjectBuilder jsonObj = getStateInfos(context, policyName, stateName, "public");
					jsonObj.add("public", true);
					jsonArray.add(jsonObj);
				} else {
					JsonObjectBuilder jsonObj = getStateInfos(context, policyName, stateName, "public[" + val + "]");
					jsonArray.add(jsonObj);
				}
			}
		}

		mqlRes = MqlUtil.mqlCommand(context, "print policy '" + policyName + "' select " + stateName + ".user dump |",
				true, false);
		mqlLines = mqlRes.split("\n");
		for (int i = 0; i < mqlLines.length; i++) {
			String line = mqlLines[i];

			if (line.isEmpty())
				break;

			String[] arrValues = line.split("\\|", -2);

			for (int j = 0; j < arrValues.length; j++) {
				String val = arrValues[j];
				if (val.equalsIgnoreCase("TRUE")) {
					jsonArray.add(getStateInfos(context, policyName, stateName, "user"));
				} else {
					if (!val.contains("|")) {
						val += "|";// In case were there is a user without key AND with key
					}
					jsonArray.add(getStateInfos(context, policyName, stateName, "user[" + val + "]"));
				}
			}
		}

		return jsonArray;
	}

	private JsonObjectBuilder getStateInfos(matrix.db.Context context, String policyName, String stateName, String keySelect)
			throws Exception {

		String composedSelectPrefix = stateName + "." + keySelect;

		String[] selects = new String[] { "organization", "project", "access", "filter", "revoke", "user", "key",
				"login" };

		String selectStr = "";
		for (int i = 0; i < selects.length; i++) {
			selectStr += composedSelectPrefix + "." + selects[i] + " ";
		}

		String mqlRes = MqlUtil.mqlCommand(context,
				"print policy '" + policyName + "' select " + selectStr + " dump |", true, false);

		JsonObjectBuilder jsonPolicy = Json.createObjectBuilder();
		jsonPolicy.add("stateSelect", composedSelectPrefix);

		String[] mqlLines = mqlRes.split("\n");
		for (int i = 0; i < mqlLines.length; i++) {
			String line = mqlLines[i];

			if (line.isEmpty())
				break;

			String[] arrValues = line.split("\\|", -2);

			for (int j = 0; j < arrValues.length; j++) {
				String val = arrValues[j];
				String key = selects[j];

				jsonPolicy.add(key, val);
			}
		}

		return jsonPolicy;
	}

}
