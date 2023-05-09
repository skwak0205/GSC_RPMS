package com.ds.utilities.widgets;

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
import com.ds.utilities.Exception.RestServiceException;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.PersonUtil;

@Path("/UserInfo")
public class User extends RestService {

	@GET
	@Path("/getSecurityContexts")
	public Response getSecurityContexts(@Context HttpServletRequest request) throws RestServiceException {

		JsonObjectBuilder output = Json.createObjectBuilder();

		matrix.db.Context context = null;

		try {
			output.add("msg", "KO");

			boolean isSCMandatory = false;
			context = getAuthenticatedContext(request, isSCMandatory);

			JsonArrayBuilder outArr = Json.createArrayBuilder();
			

			@SuppressWarnings("unchecked")
			Vector<String> resSCs = PersonUtil.getSecurityContextAssignments(context);
			
			String defaultSC = PersonUtil.getDefaultSecurityContext(context);
			outArr.add(defaultSC);// Put the default Security Context

			for (String strSC : resSCs) {
				if (!strSC.equals(defaultSC)) {
					outArr.add(strSC);// Then put the other Security Contexts
				}
			}

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

	@GET
	@Path("/getLogin")
	public Response getLogin(@Context HttpServletRequest request) throws RestServiceException {

		JsonObjectBuilder output = Json.createObjectBuilder();

		matrix.db.Context context = null;

		try {
			output.add("msg", "KO");

			boolean isSCMandatory = false;
			context = getAuthenticatedContext(request, isSCMandatory);

			JsonArrayBuilder outArr = Json.createArrayBuilder();

			String login = context.getUser();

			outArr.add(login);

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

	@GET
	@Path("/getFullName")
	public Response getName(@Context HttpServletRequest request) throws RestServiceException {

		JsonObjectBuilder output = Json.createObjectBuilder();

		matrix.db.Context context = null;

		try {
			output.add("msg", "KO");

			boolean isSCMandatory = false;
			context = getAuthenticatedContext(request, isSCMandatory);

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
				throw new RestServiceException(e1);
			}
			e.printStackTrace();
			throw new RestServiceException(e);
		}

		return Response.status(HttpServletResponse.SC_OK).entity(output.build().toString()).build();
	}
}
