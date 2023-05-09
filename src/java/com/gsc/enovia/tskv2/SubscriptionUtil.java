//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gsc.enovia.tskv2;

import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceConstants;
import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.Select;
import com.matrixone.apps.domain.util.PersonUtil;
import matrix.db.Context;
import matrix.util.StringList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SubscriptionUtil implements ServiceConstants {
    private static final String RELATIONSHIP_SUBSCRIBED_PERSON = "Subscribed Person";
    private static final String RELATIONSHIP_PUBLISH = "Publish";
    private static final String RELATIONSHIP_PUBLISH_SUBSCRIBE = "Publish Subscribe";
    private static final String TYPE_EVENT = "Event";
    private static final String TYPE_PUBLISH_SUBSCRIBE = "Publish Subscribe";
    private static final String EMAIL = "Email";

    public SubscriptionUtil() {
    }

    public static List<String> getSubscribedTaskIds(Context var0) throws FoundationException {
        ArrayList var2 = new ArrayList();

        String var1;
        try {
            var1 = PersonUtil.getPersonObjectID(var0);
        } catch (Exception var9) {
            throw new FoundationException(var9);
        }

        Dataobject var3 = new Dataobject();
        var3.setId(var1);
        ExpandData var4 = new ExpandData();
        var4.setGetFrom(true);
        var4.setGetTo(true);
        var4.setLimit(Short.valueOf("0"));
        var4.setRelationshipPattern("Subscribed Person,Publish,Publish Subscribe");
        var4.setTypePattern("Event,Publish Subscribe,Task");
        var4.setPreserveLevel(false);
        var4.setRecurseToLevel(new BigDecimal(3));
        ArrayList var5 = new ArrayList();
        var5.add(new Select("physicalid", "physicalid", ExpressionType.BUS, (Format)null, false));
        var5.add(new Select("type", "type", ExpressionType.BUS, (Format)null, false));
        Datacollection var6 = ObjectUtil.expand(var0, var3, var4, var5);
        Iterator var7 = var6.getDataobjects().iterator();

        while(var7.hasNext()) {
            Dataobject var8 = (Dataobject)var7.next();
            if ("Task".equals(var8.getType())) {
                var2.add(var8.getId());
            }
        }

        return var2;
    }

    public static List<String> getSubscribersList(Context var0, String var1, String var2) throws FoundationException {
        ArrayList var3 = new ArrayList();

        try {
            StringList var4 = com.matrixone.apps.common.util.SubscriptionUtil.getSubscribersList(var0, var1, var2, false);
            Iterator var5 = var4.iterator();

            while(var5.hasNext()) {
                Object var6 = var5.next();
                var3.add(((String)var6).split("\\|")[0]);
            }

            return var3;
        } catch (Exception var7) {
            throw new FoundationException(var7);
        }
    }

    public static void setSubscribed(Context var0, String var1, boolean var2, String var3) throws FoundationException {
        try {
            if (var2) {
                com.matrixone.apps.common.util.SubscriptionUtil.createSubscription(var0, false, "type", var1, var3, false, "Email");
            } else {
                com.matrixone.apps.common.util.SubscriptionUtil.deleteSubscriptions(var0, false, "type", var1, var3, "Email");
            }

        } catch (Exception var5) {
            throw new FoundationException(var5);
        }
    }
}
