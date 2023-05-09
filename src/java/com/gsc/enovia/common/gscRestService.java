package com.gsc.enovia.common;


import com.dassault_systemes.platform.restServices.ModelerBase;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/rest")
public class gscRestService extends ModelerBase {
    public gscRestService() {
    }

    public Class<?>[] getServices() {
        return new Class[]{gscCommon.class, gscDashboard.class};
    }
}
