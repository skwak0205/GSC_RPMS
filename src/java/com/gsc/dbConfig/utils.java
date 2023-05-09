package com.gsc.dbConfig;

import java.util.Properties;

public class utils {

    public static matrix.db.Context getContext() {
        matrix.db.Context context = null;
        try {
            Properties prop = dbConnectionUtil.getProperty();
            String internalUrl = prop.getProperty("3DSPACE_INTERNAL_URL");
            context = new matrix.db.Context(internalUrl);
            context.setUser("admin_platform");
            context.setPassword("Qwer1234");

            /**
             * Service Administrator
             * ctx::VPLMProjectLeader.Company Name.RPMS
             * ctx::VPLMProjectAdministrator.Company Name.RPMS
             * ctx::VPLMAdmin.Company Name.Default
             */
            //context.setRole("ctx::VPLMProjectLeader.Company Name.RPMS");
            //context.setRole("ctx::VPLMProjectAdministrator.Company Name.RPMS");
            context.setRole("ctx::VPLMAdmin.Company Name.Default");

            context.setVault("eService Production");
            context.connect(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return context;
    }
}
