package com.ds.utilities.widgets;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
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
import com.matrixone.apps.domain.util.MailUtil;

import matrix.util.StringList;

@Path("/SendNotificationMail")
public class DSISMailUtil extends RestService {

	@GET
	public Response getMail(@Context HttpServletRequest request,
			@DefaultValue("") @QueryParam("toList") String strToList,
			@DefaultValue("") @QueryParam("ccList") String strCcList,
			@DefaultValue("") @QueryParam("bccList") String strBccList,
			@DefaultValue("") @QueryParam("subject") String strSubject,
			@DefaultValue("") @QueryParam("message") String strMessage,
			@DefaultValue("") @QueryParam("objectsIdsList") String strObjectsIdsList) throws RestServiceException {
		return mail(request, strToList, strCcList, strBccList, strSubject, strMessage, strObjectsIdsList);
	}

	@POST
	public Response postMail(@Context HttpServletRequest request,
			@DefaultValue("") @FormParam("toList") String strToList,
			@DefaultValue("") @FormParam("ccList") String strCcList,
			@DefaultValue("") @FormParam("bccList") String strBccList,
			@DefaultValue("") @FormParam("subject") String strSubject,
			@DefaultValue("") @FormParam("message") String strMessage,
			@DefaultValue("") @FormParam("objectsIdsList") String strObjectsIdsList) throws RestServiceException {
		return mail(request, strToList, strCcList, strBccList, strSubject, strMessage, strObjectsIdsList);
	}

	public Response mail(HttpServletRequest request, String strToList, String strCcList, String strBccList,
			String strSubject, String strMessage, String strObjectsIdsList) throws RestServiceException {

		
		JsonObjectBuilder output = Json.createObjectBuilder();

		matrix.db.Context context = null;

		try {
			output.add("msg", "KO");

			boolean isSCMandatory = false;
			context = getAuthenticatedContext(request, isSCMandatory);

			ContextUtil.startTransaction(context, true);

			StringList toList = new StringList(strToList.split(","));// name of users ...
			StringList ccList = strCcList.isEmpty() ? null : new StringList(strCcList.split(","));// name of users ...
			StringList bccList = strBccList.isEmpty() ? null : new StringList(strBccList.split(","));// name of users
																										// ...
			String subject = strSubject;
			String message = strMessage;
			StringList objectIdList = new StringList(strObjectsIdsList.split(","));// objectid list of attachments

			MailUtil.sendMessage(context, toList, ccList, bccList, subject, message, objectIdList);

			output.add("msg", "OK");

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
}
