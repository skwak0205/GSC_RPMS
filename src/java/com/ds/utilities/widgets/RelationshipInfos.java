package com.ds.utilities.widgets;

import java.util.Map;

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
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.MapList;

import matrix.util.StringList;

@Path("/RelationshipInfo")
public class RelationshipInfos extends RestService {

	@GET
	public Response getInfos(@Context HttpServletRequest request, @QueryParam("relIds") String relIds,
			@DefaultValue("") @QueryParam("selects") String selects) throws RestServiceException {
		return infos(request, relIds, selects);
	}

	@POST
	public Response postInfos(@Context HttpServletRequest request, @FormParam("relIds") String relIds,
			@DefaultValue("") @FormParam("selects") String selects) throws RestServiceException {
		return infos(request, relIds, selects);
	}

	public Response infos(HttpServletRequest request, String relIds, String selects) throws RestServiceException {

		JsonObjectBuilder output = Json.createObjectBuilder();

		matrix.db.Context context = null;

		try {
			output.add("msg", "KO");

			boolean isSCMandatory = false;
			context = getAuthenticatedContext(request, isSCMandatory);

			ContextUtil.startTransaction(context, false);

			JsonArrayBuilder outArr = Json.createArrayBuilder();
			if (null != relIds && !relIds.isEmpty()) {
				String[] arrRelIds = relIds.split(",");

				StringList relSl = new StringList();
				relSl.add("id[connection]");
				relSl.add("physicalid[connection]");
				relSl.add("name[connection]");
				relSl.add("from.id");
				relSl.add("to.id");

				String[] arrSelects = selects.split(",");
				for (int i = 0; i < arrSelects.length; i++) {
					String select = arrSelects[i];
					if (select.startsWith("JPO-")) {
						// jpoSelects.add(select);
					} else {
						relSl.add(select);
					}
				}

				MapList mlRes = DomainRelationship.getInfo(context, arrRelIds, relSl);

				for (int i = 0; i < mlRes.size(); i++) {
					@SuppressWarnings("unchecked")
					Map<String, Object> mapRel = (Map<String, Object>) mlRes.get(i);

					JsonObjectBuilder jsonObj = Json.createObjectBuilder();
					Tools.map2JsonBuilder(jsonObj, mapRel);

					outArr.add(jsonObj);
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
				throw new RestServiceException(e1);
			}
			e.printStackTrace();
			throw new RestServiceException(e);
		}

		return Response.status(HttpServletResponse.SC_OK).entity(output.build().toString()).build();
	}
}
