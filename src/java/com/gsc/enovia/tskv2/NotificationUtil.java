//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gsc.enovia.tskv2;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.MqlUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.util.mail.MailNotification;
import com.matrixone.apps.domain.util.BackgroundProcess;
import com.matrixone.apps.framework.ui.UINavigatorUtil;
import matrix.db.Context;

import java.util.*;

public class NotificationUtil {
    private static final String PERSON_ADMIN_TYPE = "person";
    private static final String PREFERENCE_ICON_MAIL_LANGUAGE = "IconMailLanguagePreference";
    private static final String PAGE_BREAK = "<br>";
    private static final String DASHBOARDURL_STRING_RESOURCE = "emxCollaborativeTasks.Email.3DDashboardURL";
    private static final String APP_NAME = "ENOTASK_AP";
    private static final String REGISTERED_SUITE = "CollaborativeTasks";

    public NotificationUtil() {
    }

    public static void sendEmailToSubscribers(Context var0, String var1, String var2, Map<String, String> var3) {
        try {
            Context var4 = var0.getFrameContext("NotificationUtil");
            Object[] var5 = new Object[]{var0, var1, var2, var3};
            Class[] var6 = new Class[]{Context.class, String.class, String.class, Map.class};
            BackgroundProcess var7 = new BackgroundProcess();
            var7.submitJob(var4, new NotificationUtil(), "sendEmailInBackground", var5, var6);
        } catch (Exception var8) {
            System.out.println("Error launching background process for update email.");
            var8.printStackTrace();
        }

    }

    public static void sendEmailInBackground(Context var0, String var1, String var2, Map<String, String> var3) throws Exception {
        HashMap var4 = new HashMap();
        HashMap var5 = new HashMap();

        try {
            List var6 = SubscriptionUtil.getSubscribersList(var0, var1, "TSKTaskTransaction");
            String var7 = MqlUtil.mqlCommand(var0, "print bus $1 select $2 $3 dump $4", new String[]{var1, "name", "to[Project Access Key].from.from[Project Access List].to.id", "@@"});
            String[] var8 = var7.split("@@");
            String var9 = var8[0];
            if (var8.length == 2) {
                String var10 = var8[1];
                if (var10 != null && !"".equals(var10)) {
                    return;
                }
            }

            Iterator var17 = var6.iterator();

            while(var17.hasNext()) {
                String var11 = (String)var17.next();
                Locale var14 = getLocale(var0, var11);
                String var12;
                String var13;
                if (!var4.containsKey(var14)) {
                    var12 = PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Label.NotificationEmailBody", var14).replaceAll("%TASK_NAME%", "<strong>" + var9 + "</strong>");
                    var12 = var12 + "<br><br>";
                    var12 = var12 + PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Label.FollowingOccurred", var14);
                    var12 = var12 + "<br><br>";
                    var12 = var12 + PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Label.EmailTaskModified", var14);
                    var12 = var12 + "<br>";
                    var12 = var12 + "<br>";
                    var12 = var12 + get3DDashboardURL(var0, var1, var2, var14);
                    var4.put(var14, var12);
                    var13 = PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Label.MailSubjectTaskUpdate", var14);
                    var5.put(var14, var13);
                } else {
                    var12 = (String)var4.get(var14);
                    var13 = (String)var5.get(var14);
                }

                MailNotification var15 = new MailNotification(var0, var11, var13, var12);
                var15.sendMail();
            }
        } catch (Exception var16) {
            System.out.println("Error sending task update email.");
            var16.printStackTrace();
        }

    }

    private static String get3DDashboardURL(Context var0, String var1, String var2, Locale var3) throws Exception {
        String var4 = "";

        try {
            String var5 = "ENOTASK_AP";
            if (!UINavigatorUtil.isCloud(var0)) {
                var5 = "ENOTASP_AP";
            }

            if (var2 != null && var2.length() > 0) {
                var5 = var2;
            }

            var4 = FoundationUtil.generate3DDashboardURL(var0, var5, var1);
            if (var4 != null && !var4.isEmpty()) {
                var4 = PropertyUtil.getTranslatedValue(var0, "CollaborativeTasks", "emxCollaborativeTasks.Email.3DDashboardURL", var3).replaceAll("<url>", var4);
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return var4;
    }

    private static Locale getLocale(Context var0, String var1) throws FoundationException {
        try {
            String var3 = "print $1 $2 select $3 dump";
            String var2 = MqlUtil.mqlCommand(var0, var3, new String[]{"person", var1, "property[IconMailLanguagePreference].value"});
            return new Locale(var2);
        } catch (Exception var4) {
            throw new FoundationException("Error getting locale for user " + var1);
        }
    }
}
