//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package matrix.db;

import com.dassault_systemes.platform.jdbc.WrappedJDBCConnection;
import com.matrixone.jdl.MatrixSession;
import com.matrixone.jdl.bosInterface;
import com.matrixone.jni.MatrixSerializable;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import matrix.util.JPOInvocationParams;
import matrix.util.MQLExecWithBindParams;
import matrix.util.MatrixException;
import matrix.util.StackTrace;
import matrix.util.ThreadMap;

public final class JPOSupport {
    static Map<Thread, ContextHolder> _activeThreads = new ConcurrentHashMap();
    private static boolean jpoExternal = false;
    private static boolean jpoExternalSet = false;
    static Properties aliases = new Properties();
    static Map<Object, Connection> activeJdbcObjects;
    private static final int PCT_NONE = 0;
    private static final int PCT_BUSINESSOBJECT_CREATE = 1;
    private static final int PCT_BUSINESSOBJECT_CREATE_COMPOSEE = 2;
    private static final int PCT_BUSINESSOBJECT_CONNECT = 3;
    private static final int PCT_BUSINESSOBJECT_CLONE = 4;
    private static final int PCT_BUSINESSOBJECT_REVISE = 5;
    private static final int PCT_CONNECTION_CREATE = 6;
    static List<String> JDBC_DRIVERS;

    public JPOSupport() {
    }

    public static Class<?> newClass(String var0) throws Exception {
        return newClass(var0, true);
    }

    public static Class<?> newClass(String var0, boolean var1) throws Exception {
        try {
            MatrixClassLoader var2 = MatrixClassLoader.getLoader(getContext());
            Class var3 = null;
            debug("Constructing class: " + var0);
            var0 = cmdLineHelper(var0);
            if (var0 == null) {
                return null;
            } else {
                int var4 = var0.indexOf("_mxJPO");
                String var5 = "";
                if (var4 > 0) {
                    int var6 = var4 + "_mxJPO".length();
                    var5 = var0.substring(0, var6);
                    String var7 = var0.substring(var6);
                    var4 = var7.indexOf("$");
                    if (var4 >= 0) {
                        var5 = var5 + var7;
                    }
                }

                boolean var13 = getExternal();
                if (var13 && !"".equals(var5)) {
                    try {
                        debug("Trying to load class: " + var5 + " from file system (debug mode)");
                        var3 = var2.loadClass(var5);
                        debug("Constructed class: " + var5 + " from file system (debug mode)");
                    } catch (Throwable var11) {
                        Throwable var14 = var11;
                        if (var5.endsWith("_mxJPO")) {
                            var5 = var5.substring(0, var5.length() - "_mxJPO".length());
                        }

                        try {
                            debug(" Got error loading: " + var0 + " trying to load (stripped _mxJPO): " + var0 + " " + var14);
                            var3 = var2.loadClass(var5);
                            debug("Constructed class: " + var5 + " from file system (debug mode)");
                        } catch (Throwable var10) {
                        }
                    }
                }

                if (var3 == null && (var1 || "".equals(var5))) {
                    try {
                        debug("Trying to load class - from db " + var0);
                        var3 = var2.loadClass(var0);
                        debug("Constructed class: " + var0 + " from the database");
                    } catch (Throwable var9) {
                        debug("Skipping loading class - from db " + var0 + " error: " + var9);
                    }
                }

                if (var3 == null && !var13 && !"".equals(var5)) {
                    debug(String.format("Trying to load from FS: ret = %s, isExternal = %s, classNameRaw", Boolean.toString(var3 == null), Boolean.toString(var13), var5));
                    var3 = var2.loadClass(var5);
                    debug("Constructed class: " + var5 + " from file system");
                }

                if (var3 == null) {
                    throw new ClassNotFoundException(var0);
                } else {
                    return var3;
                }
            }
        } catch (Throwable var12) {
            debug(var12);
            if (var12 instanceof Exception) {
                throw (Exception)var12;
            } else {
                throw new Exception(var12.getMessage() + " " + var0);
            }
        }
    }

    private static String cmdLineHelper(String var0) throws Exception {
        if (!var0.startsWith("-?") && !var0.startsWith("-help")) {
            String[] var5;
            if (var0.startsWith("+")) {
                var5 = var0.replace("+", "@").split("=");
                if (var5.length != 2) {
                    throw new Exception("Invalid alias add command, must be in form +alias=command");
                } else {
                    addAlias(var5[0], var5[1]);
                    return null;
                }
            } else if (var0.startsWith("&")) {
                Iterator var6 = aliases.keySet().iterator();

                while(var6.hasNext()) {
                    Object var8 = var6.next();
                    System.out.println(var8 + " ==> " + aliases.get(var8));
                }

                return null;
            } else if (var0.startsWith("-which=")) {
                var5 = var0.split("=");
                if (var5.length != 2) {
                    throw new Exception("Invalid which command, must be in form -which=FullyQualifedClassName");
                } else {
                    System.out.println(findClassLocation(var5[1]));
                    return null;
                }
            } else if (var0.startsWith("-")) {
                removeAlias(var0.replace("-", "@"));
                return null;
            } else {
                if (var0.startsWith("@")) {
                    String var1 = var0;
                    var0 = "";
                    String[] var2;
                    if (var1.contains(":")) {
                        var2 = var1.split(":");
                        if (var2.length != 2) {
                            throw new Exception("Alias format is @prefix:Name");
                        }

                        var1 = var2[0];
                        var0 = var2[1];
                    } else if (var1.contains(".")) {
                        var2 = var1.split("\\.");
                        if (var2.length < 2) {
                            throw new Exception("Alias format is @prefix.Name");
                        }

                        var1 = var2[0];
                        String var3 = var2[1];

                        for(int var4 = 2; var4 < var2.length; ++var4) {
                            var3 = var3 + "." + var2[var4];
                        }

                        var0 = var3;
                    }

                    String var7 = (String)aliases.get(var1);
                    if (!var0.isEmpty() && !var7.endsWith(".")) {
                        var7 = var7 + ".";
                    }

                    var0 = var7 + var0;
                    debug("Constructing class (after expansion): " + var0);
                }

                return var0;
            }
        } else {
            System.out.println("Usage: \tjava class [-method name [?]] ...\n\tjava +alias=pattern\n\tjava & - show all aliases\n\tjava -alias -delete the alias\n\tjava -which=[fully qualified class name] - show where the class is loaded from");
            return null;
        }
    }

    private static void addAlias(String var0, String var1) {
        aliases.put(var0, var1);
        flushAliases();
    }

    private static void removeAlias(String var0) throws MatrixException {
        aliases.remove(var0.replace("-", "@"));
        flushAliases();
    }

    private static void flushAliases() {
        try {
            FileOutputStream var0 = new FileOutputStream(getAliasStorageFile());
            aliases.store(var0, "Java Aliases");
        } catch (Exception var1) {
        }

    }

    private static File getAliasStorageFile() {
        return new File(System.getProperty("user.home"), ".mqlJavaAliases");
    }

    public static void destroyObject(Object var0) {
        if (activeJdbcObjects.containsKey(var0)) {
            Connection var1 = (Connection)activeJdbcObjects.get(var0);

            try {
                if (!var1.isClosed()) {
                    var1.close();
                }
            } catch (SQLException var3) {
            }
        }

    }

    public static byte[] getBytes(String var0) throws Exception {
        Context var1 = getContext();
        bosInterface var2 = var1.getSession().getInterface();
        byte[] var3 = null;
        if (var3 == null || var3.length == 0) {
            var3 = var2.classLoader(var1.getSession(), var0);
        }

        if (var3 != null && var3.length != 0) {
            return var3;
        } else {
            throw new ClassNotFoundException(var0);
        }
    }

    /** @deprecated */
    public static void registerThread(Context var0) {
        registerThread(var0, false);
    }

    private static void registerThread(Context var0, boolean var1) {
        findContextHolder(true).register(var0, var1);
    }

    public static void unregisterThread() {
        findContextHolder(true).unregister();
    }

    private static ContextHolder findContextHolder(boolean var0) {
        Thread var1 = Thread.currentThread();
        if (!_activeThreads.containsKey(var1)) {
            if (var0) {
                ContextHolder var2 = new ContextHolder();
                _activeThreads.put(var1, var2);
                return var2;
            } else {
                return null;
            }
        } else {
            return (ContextHolder)_activeThreads.get(var1);
        }
    }

    public static boolean isJPOThread() {
        Thread var0 = Thread.currentThread();
        if (!_activeThreads.containsKey(var0)) {
            return false;
        } else {
            return getContext() != null;
        }
    }

    public static Object invokeObject(Object serviceObject, String methodName, Context context, String[] var3, boolean var4) throws Throwable {
        if (serviceObject == null) {
            return null;
        } else {
            registerThread(context, var4);
            Class[] var5 = new Class[]{Context.class, String[].class};
            if ("?".equals(methodName)) {
                System.out.println(printMethodUsage(serviceObject));
                return null;
            } else {
                debug("Invoking: " + serviceObject.getClass() + ":" + methodName);
                ArrayList var6 = new ArrayList();
                boolean var34 = false;

                Object var44;
                try {
                    int var11;
                    int var12;
                    try {
                        var34 = true;
                        Class[] var7 = new Class[]{Context.class, String[].class};
                        ArrayList paramList = new ArrayList();
                        Method method = null;
                        Method[] var43 = serviceObject.getClass().getMethods();
                        var11 = var43.length;

                        for(var12 = 0; var12 < var11; ++var12) {
                            Method methods = var43[var12];
                            method = null;
                            if (methodName.equals(methods.getName())) {
                                int paramTypesLength = methods.getParameterTypes().length;
                                if (paramTypesLength >= var7.length) {
                                    Class[] parameterTypes = methods.getParameterTypes();
                                    paramList.clear();
                                    if (parameterTypes[0].equals(Context.class)) {
                                        --paramTypesLength;
                                        paramList.add(context);
                                        Annotation[][] var16 = methods.getParameterAnnotations();

                                        for(int var17 = 1; var17 < parameterTypes.length; ++var17) {
                                            Class var18 = parameterTypes[var17];
                                            if (var18.equals(String[].class)) {
                                                paramList.add(var3);
                                                --paramTypesLength;
                                                break;
                                            }

                                            Annotation[] var19 = var16[var17];
                                            Annotation[] var20 = var19;
                                            int var21 = var19.length;

                                            for(int var22 = 0; var22 < var21; ++var22) {
                                                Annotation var23 = var20[var22];
                                                Class var24 = var23.annotationType();
                                                if (var24.equals(JPOJDBCConnection.class)) {
                                                    --paramTypesLength;
                                                    Connection var25 = getJdbcConnection(context);
                                                    if (!((JPOJDBCConnection)var23).noRefCount()) {
                                                        var6.add(var25);
                                                    }

                                                    paramList.add(var25);
                                                }
                                            }
                                        }

                                        method = methods;
                                        if (paramTypesLength == 0) {
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        if (method == null) {
                            throw new NoSuchMethodException(serviceObject.getClass().getName() + "." + methodName + "\n\nAvailable Methods:\n\n" + printMethodUsage(serviceObject));
                        }

                        var44 = method.invoke(serviceObject, paramList.toArray(new Object[0]));
                        var34 = false;
                    } catch (NullPointerException var37) {
                        StackTraceElement[] var40 = Thread.currentThread().getStackTrace();
                        String var9 = "";
                        StackTraceElement[] var10 = var40;
                        var11 = var40.length;

                        for(var12 = 0; var12 < var11; ++var12) {
                            StackTraceElement var13 = var10[var12];
                            if (var9 == "") {
                                var9 = "\n\t";
                            } else {
                                var9 = var9 + var13.toString() + "\n\t";
                            }
                        }

                        debug(var9);
                        throw var37;
                    } catch (InvocationTargetException var38) {
                        Throwable var8 = var38.getTargetException();
                        debug(var8);
                        throw var8;
                    }
                } finally {
                    if (var34) {
                        try {
                            Iterator var27 = var6.iterator();

                            while(var27.hasNext()) {
                                Connection var28 = (Connection)var27.next();
                                if (var28 != null && !var28.isClosed()) {
                                    var28.close();
                                }
                            }
                        } catch (SQLException var35) {
                            var35.printStackTrace();
                        }

                        unregisterThread();
                    }
                }

                try {
                    Iterator var45 = var6.iterator();

                    while(var45.hasNext()) {
                        Connection var47 = (Connection)var45.next();
                        if (var47 != null && !var47.isClosed()) {
                            var47.close();
                        }
                    }
                } catch (SQLException var36) {
                    var36.printStackTrace();
                }

                unregisterThread();
                return var44;
            }
        }
    }

    private static String getPrecreateTriggerType(int var0) {
        switch (var0) {
            case 0:
            default:
                return "unknown trigger type (" + var0 + ")";
            case 1:
                return "businessobject create (" + var0 + ")";
            case 2:
                return "businessobject create composee (" + var0 + ")";
            case 3:
                return "businessobject connect (" + var0 + ")";
            case 4:
                return "businessobject clone (" + var0 + ")";
            case 5:
                return "businessobject revise (" + var0 + ")";
            case 6:
                return "relationship create (" + var0 + ")";
        }
    }

    public static byte[] invokePrecreateTrigger(Context var0, int var1, byte[] var2, String var3, String var4, boolean var5) throws Throwable {
        String var6 = getPrecreateTriggerType(var1);
        debug("Invoking Precreate Trigger: " + var3 + ":" + var4 + ", triggerType=" + var6);
        MatrixSerializable var7 = new MatrixSerializable();
        var7.reset(var2);
        boolean var8 = false;
        ConnectParameters var9;
        switch (var1) {
            case 1:
                CreateParameters var11 = (CreateParameters)JPO.invokeLocal(var0, JPOInvocationParams.getParams().setClassName(var3).setMethodName(var4).setMethodArgs(JPO.packArgs((new CreateParameters()).init(var7))));
                var7.clear();
                debug("Invoking Precreate Trigger(" + var6 + "): cleared" + (var11 == null ? ", null ret" : ""));
                if (var11 != null) {
                    var11.getBosCreateBusinessObjectSt().encode(var7);
                } else {
                    var8 = true;
                }
                break;
            case 2:
                ComposeeParameters var10 = (ComposeeParameters)JPO.invokeLocal(var0, JPOInvocationParams.getParams().setClassName(var3).setMethodName(var4).setMethodArgs(JPO.packArgs((new ComposeeParameters()).init(var7))));
                var7.clear();
                debug("Invoking Precreate Trigger(" + var6 + "): cleared" + (var10 == null ? ", null ret" : ""));
                if (var10 != null) {
                    var10.getBosCreateComposeeSt().encode(var7);
                } else {
                    var8 = true;
                }
                break;
            case 3:
                var9 = (ConnectParameters)JPO.invokeLocal(var0, JPOInvocationParams.getParams().setClassName(var3).setMethodName(var4).setMethodArgs(JPO.packArgs((new ConnectParameters()).init(var7))));
                var7.clear();
                debug("Invoking Precreate Trigger(" + var6 + "): cleared" + (var9 == null ? ", null ret" : ""));
                if (var9 != null) {
                    var9.getBosCreateConnectionSt().encode(var7);
                } else {
                    var8 = true;
                }
                break;
            case 4:
                CloneParameters var14 = (CloneParameters)JPO.invokeLocal(var0, JPOInvocationParams.getParams().setClassName(var3).setMethodName(var4).setMethodArgs(JPO.packArgs((new CloneParameters()).init(var7))));
                var7.clear();
                debug("Invoking Precreate Trigger(" + var6 + "): cleared" + (var14 == null ? ", null ret" : ""));
                if (var14 != null) {
                    var14.getBosObjectCloneInfoSt().encode(var7);
                } else {
                    var8 = true;
                }
                break;
            case 5:
                ReviseParameters var13 = (ReviseParameters)JPO.invokeLocal(var0, JPOInvocationParams.getParams().setClassName(var3).setMethodName(var4).setMethodArgs(JPO.packArgs((new ReviseParameters()).init(var7))));
                var7.clear();
                debug("Invoking Precreate Trigger(" + var6 + "): cleared" + (var13 == null ? ", null ret" : ""));
                if (var13 != null) {
                    var13.getBosObjectReviseInfoSt().encode(var7);
                } else {
                    var8 = true;
                }
                break;
            case 6:
                var9 = (ConnectParameters)JPO.invokeLocal(var0, JPOInvocationParams.getParams().setClassName(var3).setMethodName(var4).setMethodArgs(JPO.packArgs((new ConnectParameters()).init(var7))));
                var7.clear();
                debug("Invoking Precreate Trigger(" + var6 + "): cleared" + (var9 == null ? ", null ret" : ""));
                if (var9 != null) {
                    var9.getBosCreateConnectionSt().encode(var7);
                } else {
                    var8 = true;
                }
        }

        if (!var8) {
            byte[] var12 = var7.scan();
            if (var12 != null) {
                debug("Invoking Precreate Trigger(" + var6 + "): ret " + var12.length + " bytes");
            }

            return var12;
        } else {
            return null;
        }
    }

    private static String printMethodUsage(Object var0) {
        StringBuilder var1 = new StringBuilder();
        Method[] var2 = var0.getClass().getMethods();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Method var5 = var2[var4];
            Class[] var6 = var5.getParameterTypes();
            if (var6.length >= 2 && var6[0] == Context.class && var6[var6.length - 1] == String[].class) {
                var1.append(var5.getReturnType() + " " + var5.getName() + "(" + getArgs(var5) + ")\n");
            }
        }

        return var1.toString().length() > 0 ? var1.toString() : "None";
    }

    private static String getArgs(Method var0) {
        StringBuilder var1 = new StringBuilder();
        Annotation[][] var2 = var0.getParameterAnnotations();
        int var3 = 0;
        Class[] var4 = var0.getParameterTypes();
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Class var7 = var4[var6];
            Annotation[] var8 = var2[var3];
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
                Annotation var11 = var8[var10];
                var1.append(var11.toString() + " ");
            }

            var1.append(var7.getCanonicalName() + ",");
            ++var3;
        }

        return var1.toString().substring(0, var1.length() - 1);
    }

    private static void debug(Throwable var0) {
        if (isDebug()) {
            try {
                var0.printStackTrace();
                getContext().printTrace("JPO", "CLASS LOADER Exception: " + StackTrace.getStackTrace(var0));
            } catch (Exception var2) {
            }
        }

    }

    private static boolean isDebug() {
        ContextHolder var0 = findContextHolder(false);
        return var0 != null ? var0.isDebug() : false;
    }

    public static void debug(String var0) {
        if (isDebug()) {
            try {
                getContext().printTrace("JPO", "CLASS LOADER:" + var0);
            } catch (Exception var2) {
            }
        }

    }

    /** @deprecated */
    static InputStream getResourceAsStream(Context var0, String var1) {
        try {
            return (InputStream)doLegacyAction(var0, () -> {
                return getResourceAsStream(var1);
            });
        } catch (MatrixException var3) {
            return null;
        }
    }

    static InputStream getResourceAsStream(String var0) {
        try {
            Context var1 = getContext();
            boolean var2 = MatrixClassLoader.getLoader(getContext()).getCompile();
            debug("in JPOSupport.getResourceAsStream, bcompile=" + var2);
            if (var2) {
                return null;
            }

            debug("in JPOSupport - looking for page");
            Page var3 = new Page(var0);
            if (var3.exists(var1)) {
                var3.open(var1);
                byte[] var4 = var3.getContents(var1).getBytes();
                if (var4 != null) {
                    return new ByteArrayInputStream(var4);
                }
            }
        } catch (Exception var5) {
        }

        return null;
    }

    private static boolean getExternal() throws MatrixException {
        if (!jpoExternalSet) {
            jpoExternal = checkVariable(getContext(), "matrix.jpo.external");
            if (!jpoExternal) {
                jpoExternal = checkVariable(getContext(), "MX_JPO_EXTERNAL");
            }

            jpoExternalSet = true;
        }

        return jpoExternal;
    }

    public static boolean checkVariable(Context var0, String var1) {
        String var2 = System.getProperty(var1);
        if (var2 == null || var2.equals("")) {
            try {
                var2 = Environment.getValue(var0, var1);
            } catch (Exception var4) {
            }
        }

        return "true".compareToIgnoreCase(var2) == 0;
    }

    /** @deprecated */
    public static Object newInstance(String var0, Context var1, String[] var2, int var3, boolean var4, boolean var5) throws Throwable {
        return newInstance(var0, var1, var2, var4, var5);
    }

    public static Object newInstance(String var0, Context var1, String[] var2, boolean var3, boolean var4) throws Throwable {
        registerThread(var1, var4);

        Object var19;
        try {
            Class var5 = newClass(var0, var3);
            Object var6;
            if (var5 == null) {
                var6 = null;
                return var6;
            }

            if (var3) {
                debug("Loaded from DB");
            } else {
                debug("Loaded from: " + findClassLocation(var0));
            }

            var6 = construct(var2, var5);
            Connection var7 = null;
            Field[] var8 = var5.getDeclaredFields();
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
                Field var11 = var8[var10];
                if (var11.isAnnotationPresent(JPOJDBCConnection.class)) {
                    if (!var11.getType().isAssignableFrom(Connection.class)) {
                        throw new Exception("Field:" + var11.getName() + " is not appropriate for jdbc Connection");
                    }

                    var11.setAccessible(true);
                    if (var7 == null) {
                        var7 = getJdbcConnection(var1);
                    }

                    if (var7 == null) {
                        throw new Exception("JDBC connection information not available");
                    }

                    var11.set(var6, var7);
                    if (!activeJdbcObjects.containsKey(var6)) {
                        activeJdbcObjects.put(var6, var7);
                    }
                }
            }

            var19 = var6;
        } catch (InvocationTargetException var16) {
            throw var16.getTargetException();
        } catch (Exception var17) {
            debug("Unknown Exception: " + var17);
            throw var17;
        } finally {
            unregisterThread();
        }

        return var19;
    }

    public static Connection getJdbcConnection(Context var0) throws RemoteException, MatrixException, Exception {
        return getJdbcConnection(var0, (String)null, (String)null, (String)null);
    }

    public static Connection getJdbcConnection(Context var0, String var1, String var2, String var3) throws Exception {
        MatrixSession var4 = var0.getSession();
        var0.getInterface().getJdbcInfo(var4);
        List var5 = var4.getJdbcInfo();
        Properties var6 = new Properties();
        var6.put("user", var2 != null ? var2 : var5.get(0));
        var6.put("password", var3 != null ? var3 : var5.get(2));
        var1 = var1 != null ? var1 : (String)var5.get(1);
        if ("".equals(var1)) {
            return null;
        } else {
            Connection var7 = DriverManager.getConnection(var1, var6);
            if (var7.getMetaData().supportsTransactionIsolationLevel(2)) {
                var7.setTransactionIsolation(2);
            }

            return (Connection)(checkVariable(var0, "MX_DISABLE_JDBCWRAPPER") ? var7 : new WrappedJDBCConnection(var7));
        }
    }

    public static Object construct(String[] var0, Class<?> var1) throws Exception {
        Constructor var3;
        try {
            debug("Constructing using normal constructor, class : " + var1.getCanonicalName());
            Class[] var2 = new Class[]{Context.class, String[].class};
            var3 = var1.getDeclaredConstructor(var2);
            var3.setAccessible(true);
            Object[] var4 = new Object[]{getContext(), var0};
            return var3.newInstance(var4);
        } catch (NoSuchMethodException var5) {
            debug("Constructing using default constructor, class : " + var1.getCanonicalName());
            var3 = var1.getDeclaredConstructor();
            var3.setAccessible(true);
            return var3.newInstance();
        }
    }

    public static Object newInstance(Context var0, String var1) throws Throwable {
        String var2 = getClassName(var1);
        return newInstance(var2, var0, JPO.NULL_ARGS, true, false);
    }

    public static String getClassName(String var0) throws MatrixException {
        int var1 = var0.indexOf("$");
        if (var1 >= 0) {
            var0 = var0.substring(0, var1);
        }

        String var2 = "";
        if (Program.exists(getContext(), var0)) {
            var2 = (String)((MQLExecWithBindParams)((MQLExecWithBindParams)MQLExecWithBindParams.getParams().setCmd("print program $1")).setArgs(new String[]{var0})).setSelects(new String[]{"classname"}).exec(getContext()).get("classname");
        }

        return var2;
    }

    public static String getPropertyFileName(String var0) {
        int var1 = var0.indexOf(":");
        if (var1 > 0) {
            String var2 = var0.substring(2, var1);
            if (var2.equalsIgnoreCase("BUNDLE")) {
                var0 = var0.substring(var1 + 1);
                var1 = var0.indexOf("}");
                var0 = var0.substring(0, var1) + var0.substring(var1 + 1);
            }
        }

        return var0;
    }

    public static Context getContext() {
        ContextHolder var0 = findContextHolder(false);
        return var0 != null ? var0.getContext() : null;
    }

    public String verify(Context var1, String[] var2) throws Exception {
        registerThread(var1, false);
        String var3 = "";

        try {
            String var4 = getClassName(var2[0]);
            if ("".equals(var4)) {
                var4 = var2[0];
            }

            Class var5 = newClass(var4, true);
            Class var6 = newClass(var2[1], true);
            if (!var6.isAssignableFrom(var5)) {
                var3 = "Class: " + var5 + " does not implement the " + var6 + " interface";
            }
        } catch (Exception var7) {
            var3 = var7.toString();
        }

        unregisterThread();
        return var3;
    }

    public static <T> String setResult(T var0) {
        return setObject(JPOSupport.Bucket.INTERNAL, var0);
    }

    public static <T> T getResult(String var0) {
        return ThreadMap.remove(JPOSupport.Bucket.INTERNAL, var0);
    }

    public static <T> String setObject(Bucket var0, T var1) throws RuntimeException {
        return ThreadMap.setObject(var0, var1);
    }

    public static <T> T getReference(Bucket var0, String var1, boolean var2) throws MatrixException {
        return ThreadMap.getReference(var0, var1, var2);
    }

    public static void clearReference(Bucket var0, String var1) {
        ThreadMap.clearReference(var0, var1);
    }

    private static String findClassLocation(String var0) {
        try {
            Class var1 = Class.forName(var0);
            URL var2 = var1.getProtectionDomain().getCodeSource().getLocation();
            return var2.toString();
        } catch (Exception var3) {
            return var0 + " not found";
        }
    }

    public static int getNumActiveThreads() {
        return _activeThreads.size();
    }

    public static void printTrace(String var0, String var1) {
        try {
            Context var2 = getContext();
            if (var2 != null) {
                var2.printTrace(var0, var1);
            }
        } catch (Exception var3) {
        }

    }

    /** @deprecated */
    public static String getClassName(Context var0, String var1) throws MatrixException {
        return (String)doLegacyAction(var0, () -> {
            return getClassName(var1);
        });
    }

    private static <T> T doLegacyAction(Context var0, TransactionParameters.Task<T> var1) throws MatrixException {
        registerThread(var0, false);

        Object var2;
        try {
            var2 = var1.exec();
        } catch (Exception var6) {
            throw MatrixException.wrapIfNeeded(var6);
        } finally {
            unregisterThread();
        }

        return (T) var2;
    }

    /** @deprecated */
    public static Class<?> newClass(Context var0, String var1, int var2) throws MatrixException {
        return (Class)doLegacyAction(var0, () -> {
            return newClass(var1);
        });
    }

    /** @deprecated */
    public static Object construct(Context var0, String[] var1, Class<?> var2) throws MatrixException {
        return doLegacyAction(var0, () -> {
            return construct(var1, var2);
        });
    }

    static {
        File var0 = getAliasStorageFile();
        if (var0.exists()) {
            try {
                aliases.load(new FileInputStream(var0));
            } catch (Exception var2) {
            }
        }

        activeJdbcObjects = new HashMap();
        JDBC_DRIVERS = Arrays.asList("oracle.jdbc.OracleDriver", "com.nuodb.jdbc.Driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        JDBC_DRIVERS.stream().forEach((var0x) -> {
            try {
                Class.forName(var0x);
            } catch (Exception var2) {
            }

        });
    }

    public static enum Bucket {
        DEFAULT,
        INTERNAL;

        private Bucket() {
        }
    }
}
