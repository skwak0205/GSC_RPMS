package com.ds.utilities.Exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.owasp.esapi.ESAPI;

@Provider
public class RestServiceExceptionMapper implements ExceptionMapper<RestServiceException> {

	@Override
	public Response toResponse(RestServiceException exc) {
		exc.printStackTrace();
		String msg = exc.getMessage();
		if (null != msg && !msg.isEmpty())
			msg = ESAPI.encoder().encodeForHTML(msg);
		return Response.status(200).entity("{\"msg\":\"Service Side Exception : " + msg + "\"}").build();
	}

}
