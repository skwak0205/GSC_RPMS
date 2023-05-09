package com.gsc.enovia.common;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceConstants;
import com.dassault_systemes.enovia.e6wv2.foundation.db.JPOUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Datacollection;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.ServiceParameters;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.ArgMap;
import matrix.db.Context;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class gscCommonCodeService implements ServiceConstants {

    public static Datacollection getCommonCodes(Context context, String[] args) throws FoundationException {
        ServiceParameters serviceParameters = (ServiceParameters) JPOUtil.unpackArgs(args);
        HttpServletRequest var3 = serviceParameters.getHttpRequest();
        long var4 = System.currentTimeMillis();
        com.dassault_systemes.enovia.dpm.ServiceUtil.checkLicenseDPM(context);
        Datacollection datacollection = serviceParameters.getDatacollection();
        List selects = serviceParameters.getSelects();
        ArgMap argMap = serviceParameters.getServiceArgs();
        String state = (String) argMap.get("state");
        String code = (String) argMap.get("code");

        Datacollection datacollection1;
        if (datacollection != null && !datacollection.getDataobjects().isEmpty()) {
            datacollection1 = gscCommonCode.getAllCommonCodes(context, datacollection, selects, (String) null);
        } else if (code != null && code.isEmpty()) {
            datacollection1 = gscCommonCode.getCommonCodes(context, datacollection, selects, (String) null);
        } else {
            datacollection1 = gscCommonCode.getAllCommonCodes(context, (Datacollection) null, selects, state);
        }

        String var14 = String.format("%s%s (%d) ...\t", ">>> PROGRAM (SVC):  ", "getCommonCodes", datacollection1.getDataobjects().size());
        FoundationUtil.debug(var14, var4);
        return datacollection1;
    }

}
