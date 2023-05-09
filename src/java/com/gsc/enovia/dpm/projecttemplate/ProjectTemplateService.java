package com.gsc.enovia.dpm.projecttemplate;

import com.dassault_systemes.enovia.dpm.ProjectService;
import com.dassault_systemes.enovia.dpm.ServiceUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceConstants;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ContextUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.JPOUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.MqlUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectEditUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollection;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Dataobject;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.ServiceParameters;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.UpdateActions;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.ArgMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import matrix.db.Context;

public class ProjectTemplateService implements ServiceConstants {
    public static final String PARAMETER_STATE = "state";

    public static Datacollection getProjectTemplates(Context context, String[] args) throws FoundationException {
        ServiceParameters serviceParameters = (ServiceParameters)JPOUtil.unpackArgs(args);
        HttpServletRequest httpRequest = serviceParameters.getHttpRequest();
//        ServiceUtil.checkLicenseProject(context, httpRequest);
        Datacollection datacollection = serviceParameters.getDatacollection();
        List selects = serviceParameters.getSelects();
        ArgMap serviceArgs = serviceParameters.getServiceArgs();
        String state = (String)serviceArgs.get("state");
        Datacollection datacollection1;
        if (datacollection != null && !datacollection.getDataobjects().isEmpty()) {
            datacollection1 = com.gsc.enovia.dpm.projecttemplate.ProjectTemplate.getUserProjectTemplates(context, datacollection, selects, state);
        } else {
            datacollection1 = com.gsc.enovia.dpm.projecttemplate.ProjectTemplate.getUserProjectTemplates(context, (Datacollection)null, selects, state);
        }

        return datacollection1;
    }

    public static Dataobject updateProjectTemplate(Context context, String[] args) throws Exception {
        ServiceParameters serviceParameters = (ServiceParameters)JPOUtil.unpackArgs(args);
        Datacollection datacollection = serviceParameters.getDatacollection();
        Dataobject dataobject = (Dataobject)datacollection.getDataobjects().get(0);
        String dataobjectId = dataobject.getId();
        UpdateActions updateAction = dataobject.getUpdateAction();
        if (UpdateActions.CREATE.equals(updateAction)) {
            dataobject = ProjectService.updateProjectSpace(context, serviceParameters, dataobject, updateAction);
        } else {
            com.gsc.enovia.dpm.projecttemplate.ProjectTemplate projectTemplate;
            if (UpdateActions.MODIFY.equals(updateAction)) {
                projectTemplate = new com.gsc.enovia.dpm.projecttemplate.ProjectTemplate(dataobjectId);
                boolean isOwnerOrCoOwner = projectTemplate.isOwnerOrCoOwner(context, dataobjectId);
                if (!isOwnerOrCoOwner) {
                    dataobject = null;
                    throw new FoundationException("DPM110:Only Owner or co-owner can modify Project Template");
                }

                String current = MqlUtil.mqlCommand(context, "print bus $1 select current dump", new String[]{dataobjectId});
                if ("Released".equalsIgnoreCase(current) || "Obsolete".equalsIgnoreCase(current)) {
                    throw new FoundationException("DPM110: Project Template creation or modification is not allowed in 'Released' or 'Obsolete' state");
                }

                String objCurrent = (String)((Dataobject)datacollection.getDataobjects().get(0)).getDataelements().get("state");
                if (objCurrent != null && !objCurrent.isEmpty() && !current.equalsIgnoreCase(objCurrent)) {
                    ContextUtil.setGlobalRPEValue(context, "State", objCurrent);
                    ObjectEditUtil.setState(context, dataobjectId, objCurrent);
                }

                dataobject = ProjectService.updateProjectSpace(context, serviceParameters, dataobject, updateAction);
            } else if (UpdateActions.DELETE.equals(updateAction)) {
                projectTemplate = new ProjectTemplate(dataobjectId);
                projectTemplate.deleteObject(context);
                dataobject = null;
            }
        }

        return dataobject;
    }
}
