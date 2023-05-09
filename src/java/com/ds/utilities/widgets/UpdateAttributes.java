package com.ds.utilities.widgets;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
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
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.FrameworkException;

@Path("/UpdateAttributes")
public class UpdateAttributes extends RestService {

	@GET
	public Response getInfos(@Context HttpServletRequest request, @QueryParam("oids") String oids, @QueryParam("attrSelect") String attrSelect, @DefaultValue("") @QueryParam("newValue") String newValue) throws RestServiceException{
		return infos(request, oids, attrSelect, newValue);
	}
	
	@POST
	public Response postInfos(@Context HttpServletRequest request, @FormParam("oids") String oids, @FormParam("attrSelect") String attrSelect, @DefaultValue("") @FormParam("newValue") String newValue) throws RestServiceException{
		return infos(request, oids, attrSelect, newValue);
	}
	
	public Response infos(HttpServletRequest request, String oids, String attrSelect, String newValue) throws RestServiceException{

		JsonObjectBuilder output=Json.createObjectBuilder();

		matrix.db.Context context = null;

		try {
			output.add("msg", "KO");

				boolean isSCMandatory = false;
				context = getAuthenticatedContext(request, isSCMandatory);

				ContextUtil.startTransaction(context, true);
					
					JsonArrayBuilder outArr= Json.createArrayBuilder();
					
					String attribute = attrSelect.substring(attrSelect.indexOf("[")+1, attrSelect.indexOf("]"));
	
					String[] arrOids = oids.split(",");
					for (int i = 0; i < arrOids.length; i++) {
						String objId = arrOids[i];
						DomainObject domObj = new DomainObject(objId);
						domObj.setAttributeValue(context, attribute, newValue);
						outArr.add(objId);
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
				throw new RestServiceException(e1);
			}
			e.printStackTrace();
			throw new RestServiceException(e);
		}

		return Response.status(HttpServletResponse.SC_OK).entity(output.build().toString()).build();
	}
}
