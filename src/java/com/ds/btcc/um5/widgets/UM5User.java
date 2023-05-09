package com.ds.btcc.um5.widgets;

import java.util.Vector;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.dassault_systemes.platform.restServices.RestService;
import com.ds.btcc.um5.Exception.UM5RestServiceException;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.PersonUtil;

@Path("/UserInfo")
public class UM5User extends RestService {

	@GET
	@Path("/getSecurityContexts")
	public Response getSecurityContexts(@Context HttpServletRequest request) throws UM5RestServiceException {

		// JSONObject output=new JSONObject();
		JsonObjectBuilder output = Json.createObjectBuilder();

		matrix.db.Context context = null;

		try {
			// output.put("msg", "KO");
			output.add("msg", "KO");

			// context = authenticate(request); //Deprecated in 17x
			boolean isSCMandatory = false;
			context = getAuthenticatedContext(request, isSCMandatory); // New way to get context in 17x

			//JSONArray outArr = new JSONArray();
			JsonArrayBuilder outArr = Json.createArrayBuilder();
			

			@SuppressWarnings("unchecked")
			Vector<String> resSCs = PersonUtil.getSecurityContextAssignments(context);
			// System.out.println("SC : "+resSCs);

			String defaultSC = PersonUtil.getDefaultSecurityContext(context);
			//outArr.put(defaultSC);// Put the default Security Context
			outArr.add(defaultSC);// Put the default Security Context

			for (String strSC : resSCs) {
				if (!strSC.equals(defaultSC)) {
					//outArr.put(strSC);// Then put the other Security Contexts
					outArr.add(strSC);// Then put the other Security Contexts
				}
			}

			//output.put("msg", "OK");
			//output.put("data", outArr);
			output.add("msg", "OK");
			output.add("data", outArr);

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
	@Path("/getLogin")
	public Response getLogin(@Context HttpServletRequest request) throws UM5RestServiceException {

		//JSONObject output = new JSONObject();
		JsonObjectBuilder output = Json.createObjectBuilder();

		matrix.db.Context context = null;

		try {
			//output.put("msg", "KO");
			output.add("msg", "KO");

			// context = authenticate(request); //Deprecated in 17x
			boolean isSCMandatory = false;
			context = getAuthenticatedContext(request, isSCMandatory); // New way to get context in 17x

			//JSONArray outArr = new JSONArray();
			JsonArrayBuilder outArr = Json.createArrayBuilder();

			String login = context.getUser();

			//outArr.put(login);
			outArr.add(login);

			//output.put("msg", "OK");
			//output.put("data", outArr);
			output.add("msg", "OK");
			output.add("data", outArr);

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
	@Path("/getFullName")
	public Response getName(@Context HttpServletRequest request) throws UM5RestServiceException {

		JsonObjectBuilder output = Json.createObjectBuilder();

		matrix.db.Context context = null;

		try {
			output.add("msg", "KO");

			// context = authenticate(request); //Deprecated in 17x
			boolean isSCMandatory = false;
			context = getAuthenticatedContext(request, isSCMandatory); // New way to get context in 17x

			JsonArrayBuilder outArr = Json.createArrayBuilder();

			String fullName = PersonUtil.getFullName(context);

			outArr.add(fullName);

			output.add("msg", "OK");
			output.add("data", outArr);

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
