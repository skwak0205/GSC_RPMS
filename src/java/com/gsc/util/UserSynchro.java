package com.gsc.util;

import com.dassault_systemes.i3dx.appsmodel.collections.Assignation;
import com.dassault_systemes.i3dx.appsmodel.licenses.License;
import com.dassault_systemes.i3dx.appsmodel.licenses.LicenseUtil;
import com.dassault_systemes.i3dx.appsmodel.matrix.MQLCommand;
import com.dassault_systemes.i3dx.appsmodel.matrix.MQLException;
import com.dassault_systemes.i3dx.appsmodel.matrix.Matrix;
import com.dassault_systemes.i3dx.appsmodel.matrix.mql.factory.BusinessObjectFactory;
import com.dassault_systemes.i3dx.appsmodel.matrix.mql.factory.MQLFactory;
import com.dassault_systemes.i3dx.appsmodel.matrix.mql.factory.MQLStatement;
import com.dassault_systemes.i3dx.appsmodel.model.*;
import com.dassault_systemes.i3dx.appsmodel.model.util.AffinityUtil;
import com.dassault_systemes.i3dx.appsmodel.model.util.ComputingEnvironmentUtil;
import com.dassault_systemes.i3dx.appsmodel.model.util.ProductUtil;
import com.dassault_systemes.i3dx.appsmodel.model.util.PushUtil;
import com.dassault_systemes.i3dx.appsmodel.repository.Affinity;
import com.dassault_systemes.i3dx.appsmodel.util.EnvPropertyUtil;
import com.dassault_systemes.i3dx.appsmodel.util.UserRoleUtil;
import com.dassault_systemes.i3dx.appsmodel.util.Util;
import com.dassault_systemes.i3dx.appsservices.DSPassportServicesREST;
import com.dassault_systemes.i3dx.appsservices.PersonServices;
import com.dassault_systemes.i3dx.appsservices.dsls.PackagingServices;
import com.dassault_systemes.i3dx.appsservices.platform.PlatformServices;
import com.dassault_systemes.i3dx.appsservices.push.UserPushRequestSender;
import com.dassault_systemes.i3dx.appsservices.push.data.PushCallbackData;
import com.dassault_systemes.i3dx.appsservices.push.data.PushData;
import com.dassault_systemes.i3dx.appsservices.push.data.PushDataUser;
import com.dassault_systemes.i3dx.appsservices.roles.UserRoleServices;
import com.dassault_systemes.i3dx.appsservices.util.CASUtil;
import com.dassault_systemes.i3dx.appsservices.util.JsonServices;
import com.dassault_systemes.i3dx.appsservices.util.StringUtil;
import matrix.db.Context;
import matrix.util.MatrixException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import java.util.*;
import java.util.stream.Collectors;

public class UserSynchro {
    public UserSynchro() {
    }

    public static boolean synchronize(Context context, String username) {
        Logger var2 = LoggerFactory.getLogger(com.dassault_systemes.i3dx.appsservices.synchro.UserSynchro.class);

        try {
            boolean var3 = EnvPropertyUtil.isOnPremisse(context);
            if (!var3) {
                return true;
            } else {
                System.out.println("Synchronize user {} on CE OnPremises "+ username);
                if (StringUtil.isValidEmail(username)) {
                    System.out.println("User {} is an email address, do not synchronize "+ username);
                    return true;
                } else {
                    Matrix matrix = new Matrix(context);
                    SynchroUtils synchroUtils = new SynchroUtils();
                    if (!synchroUtils.isAnonymized(matrix, username) && !synchroUtils.isRemoved(matrix, username)) {
                        ComputingEnvironment computingEnvironment = new ComputingEnvironment("OnPremise");
                        Person person = PersonServices.fetchByEmailAndByPlatform(matrix, username, computingEnvironment);
                        System.out.println("Fetch user {} "+ username);
                        if (person == null) {
                            System.out.println("User {} does not exist"+ username);
                            return false;
                        } else {
                            Map userDetails = getUserDetails(matrix, person, computingEnvironment);
                            var2.trace("userDetails for user {} = {}", username, userDetails);
                            if (userDetails != null && !userDetails.isEmpty()) {
                                String var9 = PersonServices.getEmailFromPassportWithGoodCase(matrix, userDetails);
                                var2.trace("email {}", var9);
                                if (!var9.isEmpty() && var9.contains("@")) {
                                    if (!var9.equalsIgnoreCase(person.getEmailAddress()) || !((String)userDetails.get(CASUtil.Attribute.firstname)).equalsIgnoreCase(person.getFirstName()) || !((String)userDetails.get(CASUtil.Attribute.lastname)).equalsIgnoreCase(person.getLastName())) {
                                        person.setFirstName((String)userDetails.get(CASUtil.Attribute.firstname));
                                        person.setLastName((String)userDetails.get(CASUtil.Attribute.lastname));
                                        person.setEmailAddress(var9);
                                        matrix.update(person);
                                    }

                                    System.out.println("update {} user infos with 3DPassport "+ username);
                                    ComputingEnvironmentUtil.fetchComputingEnvironmentCached(matrix, computingEnvironment);
                                    System.out.println("Fetch CE");
                                    HasMembers hasMembers = new HasMembers(person, computingEnvironment);
                                    if (matrix.exists(hasMembers)) {
                                        matrix.fetch(hasMembers);
                                        System.out.println("Fetch relation HasMembers");
                                        if (hasMembers.isPending() && hasMembers.isGranted()) {
                                            System.out.println("User {} is PENDING, do not synchronize "+ username);
                                            return true;
                                        }
                                    }

                                    System.out.println("Done fetch");
                                    Map var10 = getVisibilityCasualAndState(matrix, person);
                                    HasMembers.Visibility var11 = (HasMembers.Visibility)var10.get("Visibility");
                                    int var12 = (Integer)var10.get("CasualHour");
                                    String var13 = (String)var10.get("State");
                                    if (var13.equalsIgnoreCase("Inactive")) {
                                        System.out.println("User {} deactivation "+username);
                                        PlatformServices.grantPlatform(matrix, person, computingEnvironment, HasMembers.Role.DENIED, HasMembers.State.ACTIVE, var11, var12, (String)null);
                                        System.out.println("User {} is now denied on environment {}" +" "+ username + " " +  computingEnvironment.getName());
                                        deniedUser(matrix, person, computingEnvironment, userDetails);
                                        System.out.println("Done synchro");
                                        return true;
                                    } else {
                                        HasMembers.Role var14 = getRole(matrix, person);
                                        PlatformServices.grantPlatform(matrix, person, computingEnvironment, var14, HasMembers.State.ACTIVE, var11, var12, (String)null);
                                        System.out.println("Grant user {} on environment {}" + " " + username+ " " + computingEnvironment.getName());
                                        Assignation var15 = UserRoleUtil.getUserProducts(matrix, person.getName(), var3);
                                        Set<License> var16 = var15.getLicenses(person.getName(), computingEnvironment.getName());
                                        PackagingServices var17 = new PackagingServices();
                                        LicenseUtil.hydrate(var16, var17.prerequisite());
                                        Set var18 = UserRoleServices.synchronizable(matrix, var16, computingEnvironment, person.getName(), var3);
                                        System.out.println("Fetch onPremises products for user {}"+ " " +  username);
                                        Affinity var19 = AffinityUtil.fetch(matrix, person);
                                        PersonServices.refreshCompassID(matrix, var19, true);
                                        PushData var20 = new PushData("OnPremise");
                                        PushDataUser var21 = new PushDataUser(matrix, person, "OnPremise");
                                        var21.setActive(1);
                                        var21.setState(1);
                                        var21.setRole(var14.equals(HasMembers.Role.ADMIN) ? 1 : 0);
                                        var21.setStatus(var11.equals(HasMembers.Visibility.CONTRACTOR) ? "contractor" : "employee");
                                        var21.addLicense(var18, "OnPremise");
                                        Set var22 = (Set)var16.stream().filter((var0x) -> {
                                            return ProductUtil.isCustomRole(var0x.getTrigram());
                                        }).collect(Collectors.toSet());
                                        System.out.println("Custom roles to synchronize are [{}]"+ " " +  LicenseUtil.dumpSet(var22));
                                        if (!var22.isEmpty()) {
                                            var21.addCustomRoles(var22);
                                        }

                                        var21.setLoginAndUIDAndEmailAndFirstNameAndLastName(person, userDetails);
                                        var21.setCompany(var19.getAffinity("company_" + computingEnvironment.getName()));
                                        var21.setUserInformation(userDetails);
                                        var20.add(var21);
                                        UserPushRequestSender var23 = new UserPushRequestSender(matrix, var20, true);
                                        System.out.println("onPremises synchro");
                                        System.out.println("START");
                                        var23.start();
                                        var23.join();
                                        System.out.println("DONE");
                                        Map var24 = var23.getResult();
                                        if (var24.containsKey(person.getName())) {
                                            if (((UserPushRequestSender.RESULT)var24.get(person.getName())).equals(UserPushRequestSender.RESULT.SUCCESS)) {
                                                return true;
                                            } else {
                                                var2.error("Error during onPremises synchro {}", var24.get(person.getName()));
                                                return false;
                                            }
                                        } else {
                                            return false;
                                        }
                                    }
                                } else {
                                    var2.warn("Email address \"{}\" from user details is empty or not valid for user {} -> do not synchronize", var9, person.getName());
                                    return true;
                                }
                            } else if (person.getEmailAddress() == null || person.getEmailAddress() != null && (person.getEmailAddress().isEmpty() || !person.getEmailAddress().contains("@"))) {
                                var2.warn("Email address \"{}\" is empty or not valid for user {} -> do not synchronize", person.getEmailAddress(), username);
                                return true;
                            } else {
                                var2.error("The user {} does not have a Passport account", username);
                                return false;
                            }
                        }
                    } else {
                        return true;
                    }
                }
            }
        } catch (Exception var25) {
            var25.printStackTrace();
            var2.error("Exception during onPremises synchro", var25);
            return false;
        }
    }

    private static void deniedUser(Matrix var0, Person var1, ComputingEnvironment var2, Map<CASUtil.Attribute, String> var3) throws Exception {
        Logger var4 = LoggerFactory.getLogger(com.dassault_systemes.i3dx.appsservices.synchro.UserSynchro.class);
        PushData var5 = new PushData(var2.getName());
        PushDataUser var6 = new PushDataUser(var0, var1, var2.getName());
        var6.setActive(1);
        var6.setState(2);
        var6.setLoginAndUIDAndEmailAndFirstNameAndLastName(var1, var3);
        var5.add(var6);
        UserPushRequestSender var7 = new UserPushRequestSender(var0, var5, new HashSet(), (PushCallbackData)null);
        var4.debug("START");
        var7.start();
        var7.join();
        var4.debug("DONE");
    }

    private static Map<CASUtil.Attribute, String> getUserDetails(Matrix var0, Person var1, ComputingEnvironment var2) throws Exception {
        Logger var3 = LoggerFactory.getLogger(com.dassault_systemes.i3dx.appsservices.synchro.UserSynchro.class);
        DSPassportServicesREST var4 = new DSPassportServicesREST(var0, var2);
        var3.trace("getUserDetails -> passport is up");
        return var4.getUserDetails(var1.getName(), DSPassportServicesREST.SearchType.username);
    }

    public static HasMembers.Role getRole(Matrix var0, Person var1) throws MatrixException {
        Logger var2 = LoggerFactory.getLogger(com.dassault_systemes.i3dx.appsservices.synchro.UserSynchro.class);
        MQLFactory var3 = new MQLFactory();
        MQLStatement var4 = var3.tempQuery(var3.businessobject("Person", var1.getName())).select(var3.select().add("from[Assigned Security Context].to.from[Security Context Role].to.attribute[PnO Family]")).dump().build();
        String var5 = MQLCommand.execute(var0.getContext(), var4);
        if (var5 != null && !var5.isEmpty() && Util.csvToSet(var5).contains("Admin")) {
            System.out.println("Role Family of {} contains Admin -> user is admin"+ " " + var1);
            return HasMembers.Role.ADMIN;
        } else {
            return HasMembers.Role.MEMBRE;
        }
    }

    private static Map<String, Object> getVisibilityCasualAndState(Matrix var0, Person var1) throws MatrixException {
        Logger var2 = LoggerFactory.getLogger(com.dassault_systemes.i3dx.appsservices.synchro.UserSynchro.class);
        int var3 = 0;
        MQLFactory var4 = new MQLFactory();
        HashMap var5 = new HashMap();
        MQLStatement var6 = var4.tempQuery(var4.businessobject(var1)).select(var4.select().add("to[Employee].attribute[Employment Type].value").add("attribute[Licensed Hours].value").add("current")).dump().build();
        String var7 = MQLCommand.execute(var0.getContext(), var6);
        System.out.println("value = {}"+ " " +  var7);
        if (var7 != null && !var7.isEmpty()) {
            String[] var8 = var7.split(",");
            if (var8.length == 6) {
                var3 = Integer.parseInt((var8[4] != null ? var8[4] : "").trim());
                String var9 = (var8[3] != null ? var8[3] : "").trim();
                System.out.println("Agreement {}"+ " " +  var9);
                switch (var9) {
                    case "Employee":
                        var5.put("Visibility", HasMembers.Visibility.EMPLOYEE);
                        break;
                    case "Contractor":
                        var5.put("Visibility", HasMembers.Visibility.CONTRACTOR);
                        break;
                    default:
                        System.out.println("User {} relation is {}, defaults to Employee"+ " " +  var1+ " " +  var9);
                        var5.put("Visibility", HasMembers.Visibility.EMPLOYEE);
                }

                String var10 = (var8[5] != null ? var8[5] : "").trim();
                System.out.println("User {} is {}"+ " " +  var1.getName()+ " " +  var10);
                var5.put("State", var10);
            } else {
                System.out.println("Expected 6 CSV values in {} found {}, using default Employee"+ " " +  var7+ " " +  var8.length);
            }
        } else {
            System.out.println("User {} has no Employee relation, defaults to Employee"+ " " +  var1);
        }

        var5.put("CasualHour", var3);
        return var5;
    }

    public static class SynchroUtils {
        public SynchroUtils() {
        }

        public boolean isAnonymized(Matrix var1, String var2) {
            Logger var3 = LoggerFactory.getLogger(com.dassault_systemes.i3dx.appsservices.synchro.UserSynchro.class);
            JsonServices var4 = new JsonServices();
            Push var5 = new Push(var2, "OnPremise");

            try {
                if (!PushUtil.fetch(var1, var5)) {
                    return false;
                }
            } catch (Exception var7) {
                var3.error("Fetch Push encounter an error : {}", var7);
                return false;
            }

            var3.debug(" json : {}", var5.getData());
            JsonObject var6 = var4.readObject(var5.getData());
            return var6.containsKey("anonymised") && var6.getString("anonymised").equals("1");
        }

        public boolean isRemoved(Matrix var1, String var2) {
            Logger var3 = LoggerFactory.getLogger(com.dassault_systemes.i3dx.appsservices.synchro.UserSynchro.class);
            IsRemoved var4 = new IsRemoved(BusinessObjectFactory.getInstance().getPerson(var2), new ComputingEnvironment("OnPremise"));

            try {
                if (var1.exists(var4)) {
                    return true;
                }
            } catch (MQLException var6) {
                var3.error("isRemoved test failed for user {} : {}", var2, var6);
            }

            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        Context context = new Context("https://rpmsdev.gscaltex.co.kr/internal");
        context.setUser("admin_platform");
        context.setPassword("Qwer1234");
        context.setLocale(new Locale("en-US"));
        context.setRole("");
        context.connect();
        UserSynchro.synchronize(context, "admin_platform");
    }
}
