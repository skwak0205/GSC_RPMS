package com.ds.btcc.um5.widgets;

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
import com.ds.btcc.um5.Exception.UM5RestServiceException;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.MqlUtil;

import matrix.util.MatrixException;

@Path("/DSProducts")
public class UM5DSProducts extends RestService {

	public static final String TMP_DIR = System.getProperty("java.io.tmpdir");
	public static Map<String, String> mapRequestToFileName = new HashMap<>();

	@GET
	@Path("/list")
	public Response getList(@Context HttpServletRequest request, @QueryParam("appPattern") String appPattern,
							@QueryParam("where") String where) throws UM5RestServiceException {
		return list(request, appPattern, where);
	}

	@POST
	@Path("/list")
	public Response postList(@Context HttpServletRequest request, @FormParam("appPattern") String appPattern,
							 @FormParam("where") String where) throws UM5RestServiceException {
		return list(request, appPattern, where);
	}

	public Response list(HttpServletRequest request, String appPattern, String where) throws UM5RestServiceException {

		String outputStr = "";
		JsonObjectBuilder output = Json.createObjectBuilder();

		matrix.db.Context context = null;

		try {
			output.add("msg", "KO");

			// context = authenticate(request); //Deprecated in 17x
			boolean isSCMandatory = false;
			context = getAuthenticatedContext(request, isSCMandatory); // New way to get context in 17x

			// try to get Data from cache
			outputStr = getDataFromCache(appPattern + "$|$" + where);

			if (outputStr.isEmpty()) {
				ContextUtil.startTransaction(context, false);

				String strWhereClause = "";
				if (null != where && !where.isEmpty()) {
					strWhereClause = " where " + where;
				}
				JsonArrayBuilder outArr = Json.createArrayBuilder();


				String mqlRes = MqlUtil.mqlCommand(context, "list product " + appPattern + strWhereClause
								+ " select name title description app webclient richclient technical dynamic userexperience dump £|£",
						true, false);// includedin
				// derivative

				String[] mqlLines = mqlRes.split("\n");
				for (int i = 0; i < mqlLines.length; i++) {
					String line = mqlLines[i];
					if (line.isEmpty())
						break;
					String[] arrValues = line.split("£\\|£", -2);
					JsonObjectBuilder jsonObj = Json.createObjectBuilder();

					jsonObj.add("name", arrValues[0]);
					jsonObj.add("title", arrValues[1]);
					jsonObj.add("description", arrValues[2]);
					jsonObj.add("app", arrValues[3]);
					jsonObj.add("webclient", arrValues[4]);
					jsonObj.add("richclient", arrValues[5]);
					jsonObj.add("technical", arrValues[6]);
					jsonObj.add("dynamic", arrValues[7]);
					jsonObj.add("userexperience", arrValues[8]);

					String appName = arrValues[0];
					String strIsAPP = arrValues[3];
					if ("TRUE".equalsIgnoreCase(strIsAPP)) {
						addAppOrRoleInfos(context, jsonObj, appName);
					} else {
						jsonObj.add("appType", "Product");
					}

					outArr.add(jsonObj);
				}

				output.add("msg", "OK");
				output.add("data", outArr);

				ContextUtil.commitTransaction(context);
				outputStr = output.build().toString();
				addDataToCache(appPattern + "$|$" + where, outputStr);

			} else {
				System.out.println("Got Data from cache");
			}

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

		return Response.status(HttpServletResponse.SC_OK).entity(outputStr).build();
	}

	private void addAppOrRoleInfos(matrix.db.Context context, JsonObjectBuilder jsonObj, String appName)
			throws MatrixException {

		if (appName.startsWith("RAP-")) {
			try {
				String mqlResAppDef = MqlUtil.mqlCommand(context, "print bus RoleDefinition " + appName
								+ " - select attribute[Role Icon] attribute[Role Name] attribute[Role Display Name] attribute[Role Description] dump £|£",
						true, false);
				String[] arrValuesAppDef = mqlResAppDef.split("£\\|£", -2);

				jsonObj.add("title", arrValuesAppDef[2]);
				jsonObj.add("description", arrValuesAppDef[3]);
				jsonObj.add("appType", "Role");

				jsonObj.add("appIcon", arrValuesAppDef[0]);
			} catch (Exception ex) {
				// Do nothing
			}
		} else {
			try {
				String mqlResAppDef = MqlUtil.mqlCommand(context, "print bus AppDefinition " + appName
								+ " - select attribute[App Icon] attribute[App Name] attribute[App Display Name] attribute[App Type] dump £|£",
						true, false);
				String[] arrValuesAppDef = mqlResAppDef.split("£\\|£", -2);
				String appType = arrValuesAppDef[3];
				if (null == appType || appType.isEmpty()) {
					appType = "Product";
				}

				jsonObj.add("title", arrValuesAppDef[2]);
				jsonObj.add("description", arrValuesAppDef[1]);
				jsonObj.add("appType", appType);

				jsonObj.add("appIcon", arrValuesAppDef[0]);
			} catch (Exception ex) {
				// Do nothing
			}
		}

		if (appName.endsWith("_AP")) {
			jsonObj.add("appIcon", "/widget/images/MyApps/" + appName + "_AppIcon.png");
		}
	}

	@GET
	@Path("/{appName}/includedin")
	public Response getIncludedIn(@Context HttpServletRequest request, @PathParam("appName") String appName)
			throws UM5RestServiceException {
		return includedin(request, appName);
	}

	@POST
	@Path("/{appName}/includedin")
	public Response postIncludedIn(@Context HttpServletRequest request, @PathParam("appName") String appName)
			throws UM5RestServiceException {
		return includedin(request, appName);
	}

	public Response includedin(HttpServletRequest request, String appName) throws UM5RestServiceException {
		JsonObjectBuilder output = Json.createObjectBuilder();

		matrix.db.Context context = null;

		try {
			output.add("msg", "KO");

			// context = authenticate(request); //Deprecated in 17x
			boolean isSCMandatory = false;
			context = getAuthenticatedContext(request, isSCMandatory); // New way to get context in 17x

			ContextUtil.startTransaction(context, false);

			JsonArrayBuilder outArr = Json.createArrayBuilder();

			String mqlRes = MqlUtil.mqlCommand(context, "print product " + appName + " select includedin dump £|£",
					true, false);

			String[] mqlLines = mqlRes.split("\n");
			for (int i = 0; i < mqlLines.length; i++) {
				String line = mqlLines[i];
				String[] arrValues = line.split("£\\|£", -2);

				for (int j = 0; j < arrValues.length; j++) {
					String val = arrValues[j];
					if (val != null && !val.isEmpty()) {
						String mqlResProduct = MqlUtil.mqlCommand(context, "print product " + val
										+ " select name title description app webclient richclient technical dynamic userexperience dump £|£",
								true, false);

						String[] arrValuesProduct = mqlResProduct.split("£\\|£", -2);
						JsonObjectBuilder jsonObj = Json.createObjectBuilder();

						jsonObj.add("name", arrValuesProduct[0]);
						jsonObj.add("title", arrValuesProduct[1]);
						jsonObj.add("description", arrValuesProduct[2]);
						jsonObj.add("app", arrValuesProduct[3]);
						jsonObj.add("webclient", arrValuesProduct[4]);
						jsonObj.add("richclient", arrValuesProduct[5]);
						jsonObj.add("technical", arrValuesProduct[6]);
						jsonObj.add("dynamic", arrValuesProduct[7]);
						jsonObj.add("userexperience", arrValuesProduct[8]);

						String appNameChild = arrValuesProduct[0];
						String strIsAPP = arrValuesProduct[3];
						if ("TRUE".equalsIgnoreCase(strIsAPP)) {
							addAppOrRoleInfos(context, jsonObj, appNameChild);
						}

						outArr.add(jsonObj);
					}
				}
			}

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

	@GET
	@Path("/{appName}/dependency")
	public Response getDependency(@Context HttpServletRequest request, @PathParam("appName") String appName) throws UM5RestServiceException {
		return dependency(request, appName);
	}

	@POST
	@Path("/{appName}/dependency")
	public Response postDependency(@Context HttpServletRequest request, @PathParam("appName") String appName) throws UM5RestServiceException {
		return dependency(request, appName);
	}

	public Response dependency(HttpServletRequest request, String appName) throws UM5RestServiceException {
		JsonObjectBuilder output = Json.createObjectBuilder();

		matrix.db.Context context = null;

		try {
			output.add("msg", "KO");

			// context = authenticate(request); //Deprecated in 17x
			boolean isSCMandatory = false;
			context = getAuthenticatedContext(request, isSCMandatory); // New way to get context in 17x

			ContextUtil.startTransaction(context, false);

			JsonArrayBuilder outArr = Json.createArrayBuilder();

			String mqlRes = MqlUtil.mqlCommand(context, "print product " + appName + " select dependency dump £|£",
					true, false);

			String[] mqlLines = mqlRes.split("\n");
			for (int i = 0; i < mqlLines.length; i++) {
				String line = mqlLines[i];
				String[] arrValues = line.split("£\\|£", -2);

				for (int j = 0; j < arrValues.length; j++) {
					String val = arrValues[j];
					if (val != null && !val.isEmpty()) {
						String mqlResProduct = MqlUtil.mqlCommand(context, "print product " + val
										+ " select name title description app webclient richclient technical dynamic userexperience dump £|£",
								true, false);

						String[] arrValuesProduct = mqlResProduct.split("£\\|£", -2);
						JsonObjectBuilder jsonObj = Json.createObjectBuilder();

						jsonObj.add("name", arrValuesProduct[0]);
						jsonObj.add("title", arrValuesProduct[1]);
						jsonObj.add("description", arrValuesProduct[2]);
						jsonObj.add("app", arrValuesProduct[3]);
						jsonObj.add("webclient", arrValuesProduct[4]);
						jsonObj.add("richclient", arrValuesProduct[5]);
						jsonObj.add("technical", arrValuesProduct[6]);
						jsonObj.add("dynamic", arrValuesProduct[7]);
						jsonObj.add("userexperience", arrValuesProduct[8]);

						String appNameChild = arrValuesProduct[0];
						String strIsAPP = arrValuesProduct[3];
						if ("TRUE".equalsIgnoreCase(strIsAPP)) {
							addAppOrRoleInfos(context, jsonObj, appNameChild);
						}

						outArr.add(jsonObj);
					}
				}
			}

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

	@GET
	@Path("/{appName}/derivative")
	public Response getDerivative(@Context HttpServletRequest request, @PathParam("appName") String appName) throws UM5RestServiceException {
		return derivative(request, appName);
	}

	@POST
	@Path("/{appName}/derivative")
	public Response postDerivative(@Context HttpServletRequest request, @PathParam("appName") String appName) throws UM5RestServiceException {
		return derivative(request, appName);
	}

	public Response derivative(HttpServletRequest request, String appName) throws UM5RestServiceException {
		JsonObjectBuilder output = Json.createObjectBuilder();

		matrix.db.Context context = null;

		try {
			output.add("msg", "KO");

			// context = authenticate(request); //Deprecated in 17x
			boolean isSCMandatory = false;
			context = getAuthenticatedContext(request, isSCMandatory); // New way to get context in 17x

			ContextUtil.startTransaction(context, false);

			JsonArrayBuilder outArr = Json.createArrayBuilder();

			if (appName.startsWith("RAP-")) {
				String mqlResAppDef = MqlUtil.mqlCommand(context,
						"print bus RoleDefinition " + appName + " - select attribute[Role Apps] dump £|£", true, false);
				String[] arrValuesAppDef = mqlResAppDef.split(",", -2);
				for (int j = 0; j < arrValuesAppDef.length; j++) {
					String val = arrValuesAppDef[j];
					if (val != null && !val.isEmpty()) {
						String mqlResProduct = MqlUtil.mqlCommand(context, "print product " + val
										+ " select name title description app webclient richclient technical dynamic userexperience dump £|£",
								true, false);

						String[] arrValuesProduct = mqlResProduct.split("£\\|£", -2);
						JsonObjectBuilder jsonObj = Json.createObjectBuilder();

						jsonObj.add("name", arrValuesProduct[0]);
						jsonObj.add("title", arrValuesProduct[1]);
						jsonObj.add("description", arrValuesProduct[2]);
						jsonObj.add("app", arrValuesProduct[3]);
						jsonObj.add("webclient", arrValuesProduct[4]);
						jsonObj.add("richclient", arrValuesProduct[5]);
						jsonObj.add("technical", arrValuesProduct[6]);
						jsonObj.add("dynamic", arrValuesProduct[7]);
						jsonObj.add("userexperience", arrValuesProduct[8]);

						String appNameChild = arrValuesProduct[0];
						String strIsAPP = arrValuesProduct[3];
						if ("TRUE".equalsIgnoreCase(strIsAPP)) {
							addAppOrRoleInfos(context, jsonObj, appNameChild);
						}

						outArr.add(jsonObj);
					}
				}

			} else {
				String mqlRes = MqlUtil.mqlCommand(context, "print product " + appName + " select derivative dump £|£",
						true, false);

				String[] mqlLines = mqlRes.split("\n");
				for (int i = 0; i < mqlLines.length; i++) {
					String line = mqlLines[i];
					String[] arrValues = line.split("£\\|£", -2);

					for (int j = 0; j < arrValues.length; j++) {
						String val = arrValues[j];
						if (val != null && !val.isEmpty()) {
							String mqlResProduct = MqlUtil.mqlCommand(context, "print product " + val
											+ " select name title description app webclient richclient technical dynamic userexperience dump £|£",
									true, false);

							String[] arrValuesProduct = mqlResProduct.split("£\\|£", -2);
							JsonObjectBuilder jsonObj = Json.createObjectBuilder();

							jsonObj.add("name", arrValuesProduct[0]);
							jsonObj.add("title", arrValuesProduct[1]);
							jsonObj.add("description", arrValuesProduct[2]);
							jsonObj.add("app", arrValuesProduct[3]);
							jsonObj.add("webclient", arrValuesProduct[4]);
							jsonObj.add("richclient", arrValuesProduct[5]);
							jsonObj.add("technical", arrValuesProduct[6]);
							jsonObj.add("dynamic", arrValuesProduct[7]);
							jsonObj.add("userexperience", arrValuesProduct[8]);

							String appNameChild = arrValuesProduct[0];
							String strIsAPP = arrValuesProduct[3];
							if ("TRUE".equalsIgnoreCase(strIsAPP)) {
								addAppOrRoleInfos(context, jsonObj, appNameChild);
							}

							outArr.add(jsonObj);
						}
					}
				}
			}

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

	private void addDataToCache(String cacheName, String cachedData) {
		mapRequestToFileName.put(cacheName, cachedData);
	}

	private String getDataFromCache(String cacheName) {
		String res = "";
		res = mapRequestToFileName.get(cacheName);
		if (null == res) {
			res = "";
		}
		return res;
	}
}
