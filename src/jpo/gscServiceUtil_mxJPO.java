import com.dassault_systemes.enovia.e6w.foundation.ServiceBase;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceDefinition;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceDocGenerator;
import com.dassault_systemes.enovia.e6wv2.foundation.db.PropertyUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.*;
import com.dassault_systemes.enovia.e6wv2.foundation.util.CacheUtil;
import com.dassault_systemes.pprRestServices.WorkInstruction.SignOffUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gsc.StringUtil;
import com.gsc.util.UserSynchro;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MqlUtil;
import com.matrixone.apps.domain.util.XSSUtil;
import matrix.db.*;
import matrix.util.StringList;

import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class gscServiceUtil_mxJPO {
    public gscServiceUtil_mxJPO(Context context, String[] args)
            throws Exception {
    }

    public void code(Context context, String[] args) throws Exception {
        String types = "Project Space,Task,Issue,Risk";
        StringList list = FrameworkUtil.splitString(types, ",");
        Locale locale = context.getLocale();
        ArrayList<Locale> localeList = new ArrayList<>();
        localeList.add(Locale.US);
        localeList.add(Locale.KOREA);
        for(Locale l: localeList) {
            context.setLocale(l);
            System.out.println(l.getLanguage());

            BusinessTypeList typeList = BusinessType.getBusinessTypes(context);
            JsonObject jsonObj = new JsonObject();

            typeList.stream().filter(type -> list.contains(type.getName())).forEach(type -> {
                try {
                    AttributeTypeList attrList = type.getAttributeTypes(context);
                    attrList.stream().filter(attr -> {
                        System.out.println(jsonObj.keySet().contains(attr.getName()));
                        try {
                            return !jsonObj.keySet().contains(attr.getName()) && attr.getDataType(context).equals("string") && attr.getChoices() != null && attr.getChoices().size() > 0;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }).forEach(attr -> {
                        try {
                            JsonArray rangeArray = new JsonArray();
                            Range range = PropertyUtil.getAttributeRange(context, attr.getName());
                            for (Range.Item item : range.getItem()) {
                                JsonObject attrObj = new JsonObject();
                                attrObj.addProperty("value", item.getValue());
                                attrObj.addProperty("display", item.getDisplay());
                                rangeArray.add(attrObj);
                                System.out.println(attr.getName() + "value : " + item.getValue());
                                System.out.println(attr.getName() + "dilay : " + item.getDisplay());
                            }
                            jsonObj.add(StringUtil.lowerCamel(attr.getName()), rangeArray);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            System.out.println(jsonObj.toString());
            FileWriter f = new FileWriter("Codes." + context.getLocale().getLanguage() + ".json");
            f.write(jsonObj.toString());
            f.close();
        }

    }
    public void state(Context context, String[] args) throws Exception {
        String policies = "Project Space,Project Task,Issue,Risk";
        StringList list = FrameworkUtil.splitString(policies, ",");
        Locale locale = context.getLocale();
        ArrayList<String> localeList = new ArrayList<>();
        localeList.add("en");
        localeList.add("ko");
        for(String l: localeList) {

            PolicyList policyList = Policy.getPolicies(context);
            JsonObject jsonObj = new JsonObject();
            System.out.println(policyList.size());
            policyList.stream().filter(p -> list.contains(p.getName())).forEach(p -> {
                try {
                    String states = MqlUtil.mqlCommand(context,String.format("print policy '%s' select state dump |",p.getName()));
                    StringList stateList = FrameworkUtil.split(states,"|");
                    JsonArray stateArray = new JsonArray();

                    stateList.stream().forEach(s -> {
                        try {
                            String i18nState = PropertyUtil.getStateI18NString(context,s,p.getName(),l);
                            System.out.println(p.getName() + " : " + i18nState);
                            JsonObject stateObj = new JsonObject();
                            stateObj.addProperty("value", s);
                            stateObj.addProperty("display", i18nState);
                            stateArray.add(stateObj);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    jsonObj.add(p.getName(),stateArray);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            System.out.println(jsonObj.toString());
            FileWriter f = new FileWriter("States." + l + ".json");
            f.write(jsonObj.toString());
            f.close();
        }

    }
    public void parsePage(Context context, String[] args) throws Exception {
        try {
            String pageName = args[0];
            System.out.println("page : " + pageName);
//
//            Service service = ServiceDefinition.getServiceDefinition(context, pageName);
//            if (service == null) {
//                System.out.println(service);
//                return;
//            }
//
//            List<ServiceItem> items = service.getItems();
//            System.out.println(items.size());
            String path = "C:\\workspace\\3dspace\\src\\mql\\15_page\\dpm-services.xml";
            File f = new File(path);
            List<File> list = new ArrayList<>();
            list.add(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test(Context context, String[] args) {
        String compass = XSSUtil.encodeForJavaScript(context, com.matrixone.apps.domain.util.PropertyUtil.getRegistryClientService(context, "3DCOMPASS"));
        System.out.println("compass : " + compass);
    }

    public void syncUser(Context context, String[] args) {
        UserSynchro.synchronize(context, "admin_platform");
    }
}
