package com.gsc.enovia.dpm;

import com.dassault_systemes.enovia.document.parameterization.ParamDocmConfiguration;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationException;
import com.dassault_systemes.enovia.e6wv2.foundation.FoundationUserException;
import com.dassault_systemes.enovia.e6wv2.foundation.ServiceConstants;
import com.dassault_systemes.enovia.e6wv2.foundation.db.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.Format;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.*;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMap;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.DataelementMapAdapter;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.RelateddataMapAdapter;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxbext.Select;
import com.dassault_systemes.iPLMDictionaryPublicItf.IPLMDictionaryPublicClassItf;
import com.dassault_systemes.iPLMDictionaryPublicItf.IPLMDictionaryPublicFactory;
import com.dassault_systemes.iPLMDictionaryPublicItf.IPLMDictionaryPublicItf;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.PersonUtil;
import com.matrixone.fcs.mcs.*;
import matrix.db.*;
import matrix.util.StringList;

import javax.json.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;

public class gscEventDeliverable implements ServiceConstants {
    protected static final String REGISTERED_SUITE = "DocumentManagement";
    public static final String STATE_PRIVATE = "PRIVATE";
    public static final String STATE_IN_WORK = "IN_WORK";
    public static final String STATE_INFROZEN = "FROZEN";
    public static final String STATE_RELEASED = "RELEASED";
    public static final String STATE_OBSOLETE = "OBSOLETE";
    private static int MAX_TIME = 30000;
    private static final Map<String, Boolean> _versionableTypes = new HashMap(10);
    private static final String PROPERTY_DISALLOW_VERSIONING = "DISALLOW_VERSIONING";
    private static final String SELECTABLE_TYPE_PROPERTY_DISALLOW_VERSIONING = "property[DISALLOW_VERSIONING]";
    private static String SELECTABLE_TYPE_KINDOF_DOCUMENTS;
    private static String SELECTABLE_KINDOF_DOCUMENT;
    private static final String DEFAULT_ZIP_NAME = "multiple_files.zip";
    private static final String INVALID_FILE_CHARACERS = "[\\\\/:*?\"<>|]";
    private static final String FILE_CHARACTER_REPLACEMENT = "_";
    private static final String FOLDER_NAME_SEPARATOR = ".";
    public static final String KEY_FORMAT = "format";
    public static final String KEY_KEEP_LOCKED = "keepLocked";
    protected static final String KEY_TEMP_FILEID = "temp-fileid";
    private static final String KEY_TEMP_TITLE = "temp-title";
    public static String ATTRIBUTE_TITLE;
    public static String ATTRIBUTE_ORIGINATOR;
    public static String ATTRIBUTE_CHECKIN_REASON;
    private static String ATTRIBUTE_IS_VERSION_OBJECT;
    private static String ATTRIBUTE_FILE_STORE_SYMBOLIC_NAME;
    public static final String RELATIONSHIP_PLMDOCCONNECTION = "PLMDocConnection";
    public static final String RELATIONSHIP_SPECIFICATIONDOCUMENT = "SpecificationDocument";
    private static String RELATIONSHIP_ACTIVE_VERSION;
    private static String RELATIONSHIP_LATEST_VERSION;
    public static String POLICY_DOCUMENT;
    private static String POLICY_VERSION;
    public static String FORMAT_GENERIC;
    private static String FORMAT_INVALID_FORMAT = "mxMedium Image";
    public static String TYPE_DOCUMENT;
    private static String TYPE_DOCUMENTS;
    public static Select SELECT_ID = new Select((String)null, "physicalid", (ExpressionType)null, (Format)null, false);
    public static Select SELECT_TYPE = new Select((String)null, "type", (ExpressionType)null, (Format)null, false);
    public static Select SELECT_NAME = new Select((String)null, "name", (ExpressionType)null, (Format)null, false);
    public static Select SELECT_REVISION = new Select((String)null, "revision", (ExpressionType)null, (Format)null, false);
    public static Select SELECT_DESCRIPTION = new Select((String)null, "description", (ExpressionType)null, (Format)null, false);
    public static Select SELECT_ORIGINATED = new Select((String)null, "originated", (ExpressionType)null, (Format)null, false);
    public static Select SELECT_MODIFIED = new Select((String)null, "modified", (ExpressionType)null, (Format)null, false);
    public static Select SELECT_ORIGINATED_UTC;
    public static Select SELECT_MODIFIED_UTC;
    public static Select SELECT_POLICY;
    public static Select SELECT_POLICY_NLS;
    public static Select SELECT_HAS_CHECKOUT_ACCESS;
    public static Select SELECT_HAS_CHECKIN_ACCESS;
    public static Select SELECT_HAS_LOCK_ACCESS;
    public static Select SELECT_HAS_UNLOCK_ACCESS;
    public static Select SELECT_HAS_REVISE_ACCESS;
    protected static Select SELECT_DOCUMENT_FILES;
    protected static Select SELECT_DOCUMENT_FORMATS;
    protected static Select SELECT_DOCUMENT_FILEIDS;
    protected static Select SELECT_DOCUMENT_FILE_SIZES;
    protected static Select SELECT_DOCUMENT_FILE_CHECKSUMS;
    protected static Select SELECT_DOCUMENT_HAS_LOCK_ACCESS;
    public static Select SELECT_TITLE;
    private static Select SELECT_TEMP_TITLE;
    public static Select SELECT_FILE_TITLE;
    public static Select SELECT_FILE_CHECKIN_REASON;
    public static Select SELECT_FILE_IS_VERSION_OBJECT;
    public static Select SELECT_FILE_LOCKED;
    public static Select SELECT_FILE_LOCKER;
    public static Select SELECT_FILE_FORMAT;
    public static Select SELECT_FILE_SIZE;
    public static Select SELECT_FILE_CHECKSUM;
    public static Select SELECT_MASTER_FILE_IDS;
    private static Select SELECT_MASTER_FILE_OBJECTIDS;
    public static Select SELECT_MASTER_FILE_NAMES;
    public static Select SELECT_MASTER_FILE_LOCKERS;
    protected static Select SELECT_FILE_MASTER_DOCUMENT_ID;
    private static Select SELECT_FILE_MASTER_FILES;
    private static Select SELECT_FILE_MASTER_FORMATS;
    private static Select SELECT_FILE_MASTER_DOC_ID;
    private static Select SELECT_FILE_ACTIVE_VERSION_ID;
    private static Select SELECT_FILE_LATEST_VERSION_ID;
    private static Select SELECT_FILE_PREVIOUS_ID;
    private static Select SELECT_FILE_PREVIOUS_FILE_NAME;
    private static Select SELECT_FILE_PREVIOUS_FILE_FORMAT;
    private static String SELECT_FILE_STORE_SYMBOLIC_NAME;
    private static Select SELECT_FILE_MASTER_DOCUMENT_NAME;
    private static Select SELECT_FILE_MASTER_DOCUMENT_REV;
    private static Select SELECT_LAST;
    private static Method _AttachDocuments;
    private static Method _DetachDocuments;
    private static Method _GETDocuments;
    private static Method _detachDOCConnections;
    private static Object _DocumentServices;

    public gscEventDeliverable() {
    }

    private static void init(Context var0) {
        ATTRIBUTE_TITLE = PropertyUtil.getSchemaName(var0, "attribute_Title");
        ATTRIBUTE_ORIGINATOR = PropertyUtil.getSchemaName(var0, "attribute_Originator");
        ATTRIBUTE_CHECKIN_REASON = PropertyUtil.getSchemaName(var0, "attribute_CheckinReason");
        ATTRIBUTE_IS_VERSION_OBJECT = PropertyUtil.getSchemaName(var0, "attribute_IsVersionObject");
        ATTRIBUTE_FILE_STORE_SYMBOLIC_NAME = PropertyUtil.getSchemaName(var0, "attribute_FileStoreSymbolicName");
        RELATIONSHIP_ACTIVE_VERSION = PropertyUtil.getSchemaName(var0, "relationship_ActiveVersion");
        RELATIONSHIP_LATEST_VERSION = PropertyUtil.getSchemaName(var0, "relationship_LatestVersion");
        POLICY_DOCUMENT = PropertyUtil.getSchemaName(var0, "policy_Document");
        POLICY_VERSION = PropertyUtil.getSchemaName(var0, "policy_Version");
        FORMAT_GENERIC = PropertyUtil.getSchemaName(var0, "format_generic");
        TYPE_DOCUMENT = PropertyUtil.getSchemaName(var0, "type_Document");
        TYPE_DOCUMENTS = PropertyUtil.getSchemaName(var0, "type_DOCUMENTS");
        SELECTABLE_TYPE_KINDOF_DOCUMENTS = "kindof[" + TYPE_DOCUMENTS + "]";
        SELECTABLE_KINDOF_DOCUMENT = "type.kindof[" + TYPE_DOCUMENT + "]";
        SELECT_TITLE = new Select("title", DomainObject.getAttributeSelect(ATTRIBUTE_TITLE), (ExpressionType)null, (Format)null, false);
        SELECT_TEMP_TITLE = new Select("temptitle", DomainObject.getAttributeSelect(ATTRIBUTE_TITLE), (ExpressionType)null, (Format)null, false);
        SELECT_FILE_TITLE = SELECT_TITLE;
        SELECT_FILE_CHECKIN_REASON = new Select((String)null, DomainObject.getAttributeSelect(ATTRIBUTE_CHECKIN_REASON), (ExpressionType)null, (Format)null, false);
        SELECT_FILE_IS_VERSION_OBJECT = new Select((String)null, DomainObject.getAttributeSelect(ATTRIBUTE_IS_VERSION_OBJECT), (ExpressionType)null, (Format)null, false);
        SELECT_FILE_STORE_SYMBOLIC_NAME = DomainObject.getAttributeSelect(ATTRIBUTE_FILE_STORE_SYMBOLIC_NAME);
        SELECT_MASTER_FILE_IDS = new Select("masterFileIds", "from[" + RELATIONSHIP_ACTIVE_VERSION + "].to.physicalid", (ExpressionType)null, (Format)null, true);
        SELECT_MASTER_FILE_OBJECTIDS = new Select("masterFileObjectIds", "from[" + RELATIONSHIP_ACTIVE_VERSION + "].to.id", (ExpressionType)null, (Format)null, true);
        SELECT_MASTER_FILE_NAMES = new Select("masterFileNames", "from[" + RELATIONSHIP_ACTIVE_VERSION + "].to.attribute[" + ATTRIBUTE_TITLE + "]", (ExpressionType)null, (Format)null, true);
        SELECT_MASTER_FILE_LOCKERS = new Select("masterFileLockers", "from[" + RELATIONSHIP_ACTIVE_VERSION + "].to.locker", (ExpressionType)null, (Format)null, true);
        SELECT_FILE_MASTER_DOCUMENT_ID = new Select((String)null, "last.to[" + RELATIONSHIP_LATEST_VERSION + "].from.physicalid", (ExpressionType)null, (Format)null, false);
        SELECT_FILE_MASTER_DOCUMENT_NAME = new Select((String)null, "last.to[" + RELATIONSHIP_LATEST_VERSION + "].from.name", (ExpressionType)null, (Format)null, false);
        SELECT_FILE_MASTER_DOCUMENT_REV = new Select((String)null, "last.to[" + RELATIONSHIP_LATEST_VERSION + "].from.revision", (ExpressionType)null, (Format)null, false);
        SELECT_FILE_MASTER_FILES = new Select((String)null, "to[" + RELATIONSHIP_LATEST_VERSION + "].from.format.file.name", (ExpressionType)null, (Format)null, true);
        SELECT_FILE_MASTER_FORMATS = new Select((String)null, "to[" + RELATIONSHIP_LATEST_VERSION + "].from.format.file.format", (ExpressionType)null, (Format)null, true);
        SELECT_FILE_MASTER_DOC_ID = new Select((String)null, "to[" + RELATIONSHIP_ACTIVE_VERSION + "].from.id", (ExpressionType)null, (Format)null, false);
        SELECT_FILE_ACTIVE_VERSION_ID = new Select((String)null, "to[" + RELATIONSHIP_ACTIVE_VERSION + "].id", (ExpressionType)null, (Format)null, false);
        SELECT_FILE_LATEST_VERSION_ID = new Select((String)null, "to[" + RELATIONSHIP_LATEST_VERSION + "].id", (ExpressionType)null, (Format)null, false);
        SELECT_FILE_PREVIOUS_ID = new Select((String)null, "previous.id", (ExpressionType)null, (Format)null, false);
        SELECT_FILE_PREVIOUS_FILE_NAME = new Select((String)null, "previous." + SELECT_TITLE.getExpression(), (ExpressionType)null, (Format)null, false);
        SELECT_FILE_PREVIOUS_FILE_FORMAT = new Select((String)null, "previous.format", (ExpressionType)null, (Format)null, false);
    }

    public static Map<String, Object> getInfo(Context var0, String var1, List<Selectable> var2) throws FoundationException {
        Dataobject var3 = new Dataobject();
        var3.setId(var1);
        ObjectUtil.print(var0, var3, (PrintData)null, var2, true);
        DataelementMap var4 = var3.getDataelements();
        return var4;
    }

    protected static void getDocumentInfo(Context var0, Datacollection var1, List<Selectable> var2) throws FoundationException {
        ObjectUtil.print(var0, var1, (PrintData)null, var2, true);
    }

    protected static Datacollections getDocumentFilesInfo(Context var0, Datacollection var1, String var2, List<Selectable> var3) throws FoundationException {
        ExpandData var4 = new ExpandData();
        var4.setRelationshipPattern(RELATIONSHIP_ACTIVE_VERSION);
        var4.setGetFrom(true);
        if (var2 != null) {
            var4.setObjectWhere("physicalid == " + var2 + " || id == " + var2);
        }

        Datacollections var5 = ObjectUtil.expand(var0, var1, var4, var3);
        int var6 = 0;
        Iterator var7 = var5.getDatacollections().iterator();

        while(true) {
            while(var7.hasNext()) {
                Datacollection var8 = (Datacollection)var7.next();
                Dataobject var9 = (Dataobject)var1.getDataobjects().get(var6++);
                List var10 = DataelementMapAdapter.getDataelementValues(var9, SELECT_DOCUMENT_FILEIDS.getName());
                List var11 = DataelementMapAdapter.getDataelementValues(var9, SELECT_DOCUMENT_FILES.getName());
                List var12 = DataelementMapAdapter.getDataelementValues(var9, SELECT_DOCUMENT_FORMATS.getName());
                List var13 = DataelementMapAdapter.getDataelementValues(var9, SELECT_DOCUMENT_FILE_SIZES.getName());
                List var14 = DataelementMapAdapter.getDataelementValues(var9, SELECT_DOCUMENT_FILE_CHECKSUMS.getName());
                boolean var15 = hasSelect(var3, SELECT_FILE_FORMAT);
                removeThumbnailFiles(var9.getId(), var10, var12, var11, var13, var14, (List)null);
                String var18;
                if (var8.getDataobjects().isEmpty() && !var11.isEmpty()) {
                    int var24 = -1;
                    Iterator var25 = var10.iterator();

                    while(var25.hasNext()) {
                        var18 = (String)var25.next();
                        ++var24;
                        Dataobject var26 = new Dataobject();
                        var26.setId(var18);
                        DataelementMapAdapter.setDataelementValue(var26, "temp-fileid", var18);
                        var8.getDataobjects().add(var26);
                        String var20 = (String)var13.get(var24);
                        DataelementMapAdapter.setDataelementValue(var26, SELECT_FILE_SIZE.getName(), var20);
                        String var21 = (String)var14.get(var24);
                        DataelementMapAdapter.setDataelementValue(var26, SELECT_FILE_CHECKSUM.getName(), var21);
                        boolean var22 = hasSelect(var3, SELECT_FILE_TITLE);
                        String var23;
                        if (var22) {
                            var23 = (String)var11.get(var24);
                            DataelementMapAdapter.setDataelementValue(var26, SELECT_FILE_TITLE.getName(), var23);
                        }

                        if (var15) {
                            var23 = (String)var12.get(var24);
                            DataelementMapAdapter.setDataelementValue(var26, SELECT_FILE_FORMAT.getName(), var23);
                        }
                    }
                } else {
                    Iterator var16 = var8.getDataobjects().iterator();

                    while(var16.hasNext()) {
                        Dataobject var17 = (Dataobject)var16.next();
                        var18 = DataelementMapAdapter.getDataelementValue(var17, "temp-title");
                        int var19 = var11.indexOf(var18);
                        if (var19 != -1) {
                            DataelementMapAdapter.setDataelementValue(var17, "temp-fileid", (String)var10.get(var19));
                            DataelementMapAdapter.setDataelementValue(var17, SELECT_FILE_SIZE.getName(), (String)var13.get(var19));
                            DataelementMapAdapter.setDataelementValue(var17, SELECT_FILE_CHECKSUM.getName(), (String)var14.get(var19));
                            if (var15) {
                                DataelementMapAdapter.setDataelementValue(var17, SELECT_FILE_FORMAT.getName(), (String)var12.get(var19));
                            }
                        }
                    }
                }
            }

            return var5;
        }
    }

    private static boolean hasSelect(List<Selectable> var0, Selectable var1) {
        boolean var2 = false;
        Iterator var3 = var0.iterator();

        while(var3.hasNext()) {
            Selectable var4 = (Selectable)var3.next();
            if (var4.getName().equalsIgnoreCase(var1.getName())) {
                var2 = true;
                break;
            }
        }

        return var2;
    }

    protected static Datacollections getFileVersions(Context var0, Datacollection var1, String var2, List<Selectable> var3) throws FoundationException {
        ArrayList var4 = new ArrayList(2);
        var4.add(new Select((String)null, "revisions.id", (ExpressionType)null, (Format)null, true));
        var4.add(SELECTABLE_OBJECTID);
        Datacollection var5 = ObjectUtil.print(var0, var1, (PrintData)null, var4, false);
        Datacollections var6 = new Datacollections();
        Iterator var7 = var5.getDataobjects().iterator();

        while(var7.hasNext()) {
            Dataobject var8 = (Dataobject)var7.next();
            Datacollection var9 = new Datacollection();
            var6.getDatacollections().add(var9);
            List var10 = DataelementMapAdapter.getDataelementValues(var8, "revisions.id");
            DataelementMapAdapter.removeDataelement(var8, "revisions.id");
            String var11 = var8.getId();
            Iterator var12 = var10.iterator();

            while(var12.hasNext()) {
                String var13 = (String)var12.next();
                if (!var11.equals(var13)) {
                    Dataobject var14 = new Dataobject();
                    var9.getDataobjects().add(var14);
                    var14.setId(var13);
                }
            }
        }

        ObjectUtil.print(var0, var6, (PrintData)null, var3, true);
        return var6;
    }

    public static void setState(Context var0, String var1, String var2) throws FoundationException {
        ObjectEditUtil.setState(var0, var1, var2, false);
    }

    public static String createDocument(Context var0, String var1, String var2, String var3, String var4, Map<String, String> var5) throws FoundationException {
        try {
            if (var2 == null) {
                var2 = TYPE_DOCUMENT;
            }

            if (var4 == null) {
                var4 = POLICY_DOCUMENT;
            }

            if (var3 == null) {
                var3 = getDefaultRevision(var0, var4);
            }

            String var6 = var0.getVault().getName();
            String var7;
            if (var1 == null) {
                var7 = FrameworkUtil.getAliasForAdmin(var0, "type", var2, true);
                String var8 = FrameworkUtil.getAliasForAdmin(var0, "policy", var4, true);
                var1 = FrameworkUtil.autoName(var0, var7, (String)null, var8, var6, var3, true, false);
            }

            var7 = ObjectEditUtil.create(var0, var2, var1, var3, var4, (String)null, (String)null, false, false, false, var5, false);
            return var7;
        } catch (Exception var9) {
            throw new FoundationException(var9);
        }
    }

    private static void removeThumbnailFiles(String var0, List<String> var1, List<String> var2, List<String> var3, List<String> var4, List<String> var5, List<String> var6) {
        for(int var7 = 0; var7 < var3.size(); ++var7) {
            String var8 = (String)var3.get(var7);
            String var9 = (String)var2.get(var7);
            if (!var8.isEmpty() && !FORMAT_INVALID_FORMAT.equals(var9)) {
                if (var6 != null) {
                    var6.add(var9 + "::" + var8);
                }

                if (var0 != null) {
                    String var10 = (String)var1.get(var7);
                    if (var10 == null || var10.isEmpty()) {
                        var10 = var0 + "|" + var9 + "|" + var8;
                        var1.set(var7, var10);
                    }
                }
            } else {
                if (var1 != null) {
                    var1.remove(var7);
                }

                var2.remove(var7);
                var3.remove(var7);
                if (var4 != null) {
                    var4.remove(var7);
                    var5.remove(var7);
                }

                --var7;
            }
        }

    }

    protected static Dataobject requestDownloadTicket(Context var0, Datacollection var1, String var2, boolean var3, String var4, boolean var5, boolean var6, boolean var7) throws FoundationException {
        ContextUtil.startTransaction(var0, true);

        try {
            ArrayList var8 = new ArrayList(var1.getDataobjects().size());
            ArrayList var9 = generateDownloadBOPs(var0, var1, var3, var8);
            if (var9.isEmpty()) {
                throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "DocumentManagement", "emxDocumentManagement.Error.NoFilesAttached", var0.getLocale()), "");
            } else {
                TicketWrapper var10 = null;
                Dataobject var11 = null;
                if (var9.size() == 1 && var4 == null) {
                    if (var5) {
                        FileURLDurationPolicy var15 = new FileURLDurationPolicy((long)MAX_TIME);
                        String[] var13 = FileURL.getFileURLs(var0, var2, var9, var15);
                        var11 = new Dataobject();
                        DataelementMapAdapter.setDataelementValue(var11, "ticketURL", var13[0]);
                        DataelementMapAdapter.setDataelementValue(var11, "fileName", (String)var8.get(0));
                    } else {
                        var10 = Checkout.doIt(var0, false, "", var2, "", var9, var7);
                    }
                } else {
                    if (var5) {
                        throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "DocumentManagement", "emxDocumentManagement.Error.InvalidFileStreamOption", var0.getLocale()), "emxDocumentManagement.Error.InvalidFileStreamOption");
                    }

                    String var12 = generateProposedZipName(var9, var6, var4);
                    var10 = Checkout.doIt(var0, true, var12, var2, "", var9, var7);
                }

                ContextUtil.commitTransaction(var0);
                if (var10 != null) {
                    var11 = generateDownloadTicketData(var10, var8);
                }

                return var11;
            }
        } catch (Exception var14) {
            ContextUtil.abortTransaction(var0);
            throw new FoundationException(var14.getMessage());
        }
    }

    protected static String generateProposedZipName(List<BusinessObjectProxy> var0, boolean var1, String var2) throws FoundationException {
        if (var2 != null && !var2.isEmpty()) {
            var2 = filterFileBadCharacters(var2);
            return var2;
        } else {
            String var3 = null;
            if (var1) {
                for(int var4 = 0; var4 < var0.size(); ++var4) {
                    String var5 = ((BusinessObjectProxy)var0.get(var4)).getPath();
                    if (var3 == null) {
                        var3 = var5;
                    } else if (!var3.equalsIgnoreCase(var5)) {
                        var3 = null;
                        break;
                    }
                }
            }

            if (var3 != null) {
                var2 = var3 + ".zip";
            } else if (var2 == null) {
                var2 = "multiple_files.zip";
            }

            return var2;
        }
    }

    protected static ArrayList<BusinessObjectProxy> generateDownloadBOPs(Context var0, Datacollection var1, boolean var2, List<String> var3) throws FoundationException {
        ArrayList var4 = new ArrayList(18);
        var4.add(SELECT_ID);
        var4.add(SELECT_TEMP_TITLE);
        var4.add(SELECT_FILE_MASTER_DOCUMENT_ID);
        var4.add(SELECT_DOCUMENT_FILES);
        var4.add(SELECT_DOCUMENT_FILEIDS);
        var4.add(SELECT_DOCUMENT_FORMATS);
        var4.add(SELECT_FILE_MASTER_FILES);
        var4.add(SELECT_FILE_MASTER_FORMATS);
        var4.add(SELECT_MASTER_FILE_IDS);
        var4.add(SELECT_MASTER_FILE_OBJECTIDS);
        var4.add(SELECT_MASTER_FILE_NAMES);
        if (var2) {
            var4.add(SELECT_FILE_LOCKER);
            var4.add(SELECT_MASTER_FILE_LOCKERS);
            var4.add(SELECT_DOCUMENT_HAS_LOCK_ACCESS);
        }

        var4.add(SELECT_NAME);
        var4.add(SELECT_REVISION);
        var4.add(SELECT_POLICY);
        var4.add(SELECT_FILE_MASTER_DOCUMENT_NAME);
        var4.add(SELECT_FILE_MASTER_DOCUMENT_REV);
        var4.add(SELECT_LAST);
        ObjectUtil.print(var0, var1, (PrintData)null, var4, true);
        String var5 = "";
        ArrayList var6 = new ArrayList(var1.getDataobjects().size());
        ArrayList var7 = new ArrayList(var1.getDataobjects().size());
        String var8 = "";

        try {
            var8 = ParamDocmConfiguration.getDOCMDownloadRule(var0);
        } catch (Exception var47) {
            var47.printStackTrace();
        }

        new HashMap();
        ArrayList var10 = new ArrayList();
        Iterator var11 = var1.getDataobjects().iterator();

        label205:
        while(var11.hasNext()) {
            Dataobject var12 = (Dataobject)var11.next();
            String var13 = DataelementMapAdapter.getDataelementValue(var12, SELECT_ID.getName());
            if ("".equals(var13)) {
                throw new FoundationUserException(PropertyUtil.getTranslatedValue(var0, "DocumentManagement", "emxDocumentManagement.Error.InvalidObjectId", var0.getLocale()), "emxDocumentManagement.Error.InvalidObjectId");
            }

            String var14 = DataelementMapAdapter.getDataelementValue(var12, SELECT_TEMP_TITLE.getName());
            String var15 = DataelementMapAdapter.getDataelementValue(var12, SELECT_NAME.getName());
            String var16 = DataelementMapAdapter.getDataelementValue(var12, SELECT_REVISION.getName());
            String var17 = DataelementMapAdapter.getDataelementValue(var12, SELECT_POLICY.getName());
            String var18 = DataelementMapAdapter.getDataelementValue(var12, SELECT_FILE_LOCKER.getName());
            if ("".equals(var18)) {
                var18 = null;
            }

            boolean var19 = "TRUE".equalsIgnoreCase(DataelementMapAdapter.getDataelementValue(var12, SELECT_DOCUMENT_HAS_LOCK_ACCESS.getName()));
            String var20 = DataelementMapAdapter.getDataelementValue(var12, SELECT_LAST.getName());
            List var21 = DataelementMapAdapter.getDataelementValues(var12, SELECT_DOCUMENT_FILEIDS.getName());
            List var22 = DataelementMapAdapter.getDataelementValues(var12, SELECT_DOCUMENT_FILES.getName());
            List var23 = DataelementMapAdapter.getDataelementValues(var12, SELECT_DOCUMENT_FORMATS.getName());
            List var24 = DataelementMapAdapter.getDataelementValues(var12, SELECT_MASTER_FILE_IDS.getName());
            List var25 = DataelementMapAdapter.getDataelementValues(var12, SELECT_MASTER_FILE_OBJECTIDS.getName());
            List var26 = DataelementMapAdapter.getDataelementValues(var12, SELECT_MASTER_FILE_NAMES.getName());
            List var27 = DataelementMapAdapter.getDataelementValues(var12, SELECT_MASTER_FILE_LOCKERS.getName());
            String var28 = DataelementMapAdapter.getDataelementValue(var12, SELECT_FILE_MASTER_DOCUMENT_ID.getName());
            String var29 = DataelementMapAdapter.getDataelementValue(var12, SELECT_FILE_MASTER_DOCUMENT_NAME.getName());
            String var30 = DataelementMapAdapter.getDataelementValue(var12, SELECT_FILE_MASTER_DOCUMENT_REV.getName());
            List var31 = DataelementMapAdapter.getDataelementValues(var12, SELECT_FILE_MASTER_FILES.getName());
            List var32 = DataelementMapAdapter.getDataelementValues(var12, SELECT_FILE_MASTER_FORMATS.getName());
            ArrayList var33 = new ArrayList(var31.size());
            removeThumbnailFiles(var12.getId(), var21, var23, var22, (List)null, (List)null, var33);
            removeThumbnailFiles((String)null, (List)null, var32, var31, (List)null, (List)null, (List)null);
            String var37;
            String var53;
            if (var28 != null) {
                if (var16.equals(var20)) {
                    if (!var31.isEmpty()) {
                        var5 = var29 + "." + var30;
                        var5 = filterFileBadCharacters(var5);
                        int var49 = var31.indexOf(var14);
                        if (var49 == -1) {
                            var49 = 0;
                            var14 = (String)var31.get(0);
                        }

                        var53 = (String)var32.get(var49);
                        BusinessObjectProxy var54 = new BusinessObjectProxy(var28, var53, var14, var5, true, false);
                        var6.add(var54);
                        var3.add(var14);
                        if (var2) {
                            if (var18 == null) {
                                addToLockList(var0, var7, var13, var19, var15);
                            } else if (!var0.getUser().equals(var18)) {
                                var37 = PropertyUtil.getTranslatedValue(var0, "DocumentManagement", "emxDocumentManagement.Error.AlreadyLockedByAnotherUser", var0.getLocale());
                                var37 = replaceToken(var37, "%FILE%", var14);
                                throw new FoundationUserException(var37, "emxDocumentManagement.Error.AlreadyLockedByAnotherUser");
                            }
                        }
                    }
                } else {
                    var5 = var29 + "." + var30 + "." + var16;
                    var5 = filterFileBadCharacters(var5);
                    BusinessObjectProxy var50 = new BusinessObjectProxy(var13, (String)var23.get(0), (String)var22.get(0), var5, true, false);
                    var6.add(var50);
                    var3.add((String) var22.get(0));
                    if (var2) {
                        var53 = PropertyUtil.getTranslatedValue(var0, "DocumentManagement", "emxDocumentManagement.Error.CheckoutNonActiveFile", var0.getLocale());
                        var53 = replaceToken(var53, "%FILE%", var14);
                        throw new FoundationUserException(var53, "emxDocumentManagement.Error.CheckoutNonActiveFile");
                    }
                }

                var5 = filterFileBadCharacters(var5);
            } else {
                if (var8.equals("Flatlist")) {
                    var5 = "";
                } else if (var8.equals("OneFileOneDirectory")) {
                    var5 = var15 + "." + var16;
                }

                var5 = filterFileBadCharacters(var5);
                List var34 = RelateddataMapAdapter.getRelatedData(var12, "files");
                Iterator var35 = var34.iterator();

                String var38;
                String var39;
                String var43;
                while(var35.hasNext()) {
                    Dataobject var36 = (Dataobject)var35.next();
                    var37 = var36.getId();
                    var38 = DataelementMapAdapter.getDataelementValue(var36, SELECT_FILE_TITLE.getName());
                    var39 = DataelementMapAdapter.getDataelementValue(var36, SELECT_FILE_FORMAT.getName());
                    String var40 = null;
                    String var41;
                    int var42;
                    if (var24.isEmpty()) {
                        var42 = var21.indexOf(var37);
                        if (var42 == -1 && var38 != null) {
                            var39 = var39 == null ? getDefaultFormat(var0, var17) : var39;
                            var42 = var33.indexOf(var39 + "::" + var38);
                        }

                        if (var42 == -1) {
                            var43 = PropertyUtil.getTranslatedValue(var0, "DocumentManagement", "emxDocumentManagement.Error.InvalidFileSpecified", var0.getLocale());
                            var43 = replaceToken(var43, "%FILE%", var38);
                            throw new FoundationUserException(var43, "emxDocumentManagement.Error.InvalidFileSpecified");
                        }

                        var40 = (String)var22.get(var42);
                        var41 = (String)var23.get(var42);
                    } else {
                        var42 = var24.indexOf(var37);
                        if (var42 == -1) {
                            var42 = var25.indexOf(var37);
                        }

                        if (var42 == -1 && var38 != null) {
                            var42 = var26.indexOf(var38);
                        }

                        if (var42 == -1) {
                            var43 = PropertyUtil.getTranslatedValue(var0, "DocumentManagement", "emxDocumentManagement.Error.InvalidFileSpecified", var0.getLocale());
                            var43 = replaceToken(var43, "%FILE%", var38);
                            throw new FoundationUserException(var43, "emxDocumentManagement.Error.InvalidFileSpecified");
                        }

                        var43 = (String)var24.get(var42);
                        var36.setId(var43);
                        var40 = (String)var26.get(var42);
                        var42 = var22.indexOf(var40);
                        if (var42 == -1) {
                            var42 = 0;
                            var40 = (String)var22.get(var42);
                        }

                        var41 = (String)var23.get(var42);
                        if (var2) {
                            if (var18 == null) {
                                addToLockList(var0, var7, var36.getId(), var19, var15);
                            } else if (!var0.getUser().equals(var18)) {
                                var43 = PropertyUtil.getTranslatedValue(var0, "DocumentManagement", "emxDocumentManagement.Error.AlreadyLockedByAnotherUser", var0.getLocale());
                                var43 = replaceToken(var43, "%FILE%", var40);
                                throw new FoundationUserException(var43, "emxDocumentManagement.Error.AlreadyLockedByAnotherUser");
                            }
                        }
                    }

                    BusinessObjectProxy var58 = new BusinessObjectProxy(var13, var41, var40, var5, true, false);
                    var6.add(var58);
                    var3.add(var40);
                }

                int var51;
                Iterator var52;
                if (var34.isEmpty()) {
                    var51 = -1;
                    var52 = var22.iterator();

                    while(var52.hasNext()) {
                        var37 = (String)var52.next();
                        ++var51;
                        var38 = (String)var23.get(var51);
                        BusinessObjectProxy var55 = new BusinessObjectProxy(var13, var38, var37, var5, true, false);
                        if (var8.equals("Flatlist")) {
                            boolean var56 = false;
                            boolean var57 = false;
                            String var59 = var37;
                            String var44 = "";
                            int var45 = 1;
                            int var46 = var37.lastIndexOf(46);
                            if (var46 > 0) {
                                var43 = var37.substring(0, var46);
                                var44 = var37.substring(var46, var37.length());
                            } else if (var46 == 0) {
                                var43 = "";
                                var44 = var37.substring(var46, var37.length());
                            } else {
                                var43 = var37;
                                var44 = "";
                            }

                            while(!var56) {
                                if (var10.contains(var59)) {
                                    var59 = var43 + '(' + Integer.toString(var45) + ')' + var44;
                                    var57 = true;
                                    ++var45;
                                } else {
                                    var56 = true;
                                }
                            }

                            if (var57) {
                                var55.setFilenameOverride(var59);
                            }

                            var10.add(var59);
                        }

                        var6.add(var55);
                        var3.add(var37);
                    }
                }

                if (var2) {
                    if (var24.isEmpty()) {
                        if (var18 == null) {
                            if (var7.indexOf(var13) == -1) {
                                addToLockList(var0, var7, var13, var19, var15);
                            }
                        } else if (!var0.getUser().equals(var18)) {
                            var53 = PropertyUtil.getTranslatedValue(var0, "DocumentManagement", "emxDocumentManagement.Error.DocumentLockedByAnotherUser", var0.getLocale());
                            var53 = replaceToken(var53, "%DOCNAME%", var15);
                            throw new FoundationUserException(var53, "emxDocumentManagement.Error.DocumentLockedByAnotherUser");
                        }
                    } else if (var7.isEmpty()) {
                        var51 = -1;
                        var52 = var24.iterator();

                        do {
                            while(true) {
                                if (!var52.hasNext()) {
                                    continue label205;
                                }

                                var37 = (String)var52.next();
                                ++var51;
                                var38 = (String)var27.get(var51);
                                if (var38 != null && !var38.isEmpty()) {
                                    break;
                                }

                                addToLockList(var0, var7, var37, var19, var15);
                            }
                        } while(var0.getUser().equals(var38));

                        var39 = PropertyUtil.getTranslatedValue(var0, "DocumentManagement", "emxDocumentManagement.Error.AlreadyLockedByAnotherUser", var0.getLocale());
                        var39 = replaceToken(var39, "%FILE%", (String)var26.get(var51));
                        throw new FoundationUserException(var39, "emxDocumentManagement.Error.AlreadyLockedByAnotherUser");
                    }
                }
            }
        }

        var11 = var7.iterator();

        while(var11.hasNext()) {
            String var48 = (String)var11.next();
            lockFile(var0, var48);
        }

        return var6;
    }

    private static void addToLockList(Context var0, List<String> var1, String var2, boolean var3, String var4) throws FoundationException {
        if (!var3) {
            String var5 = PropertyUtil.getTranslatedValue(var0, "DocumentManagement", "emxDocumentManagement.Error.InsufficientLockAccess", var0.getLocale());
            var5 = replaceToken(var5, "%DOCNAME%", var4);
            throw new FoundationUserException(var5, "emxDocumentManagement.Error.InsufficientLockAccess");
        } else {
            var1.add(var2);
        }
    }

    private static String filterFileBadCharacters(String var0) {
        String var1 = var0.replaceAll("[\\\\/:*?\"<>|]", "_");
        return var1;
    }

    protected static Dataobject requestCheckinTicket(Context var0, String var1, String var2, String var3, String var4, int var5) throws FoundationException {
        return requestCheckinTickets(var0, var1, var2, var3, var4, var5, 1);
    }

    protected static Dataobject requestCheckinTickets(Context var0, String var1, String var2, String var3, String var4, int var5) throws FoundationException {
        return requestCheckinTickets(var0, var1, var2, var3, var4, 1, var5);
    }

    private static Dataobject requestCheckinTickets(Context var0, String var1, String var2, String var3, String var4, int var5, int var6) throws FoundationException {
        var3 = validateCheckinStore(var0, var2, var3, var4);
        List var7 = getFCSPreCheckinTickets(var0, var1, var3, var5, var6);
        Dataobject var8 = generateCheckinTicketData(var7);
        return var8;
    }

    private static String validateCheckinStore(Context var0, String var1, String var2, String var3) throws FoundationException {
        System.out.println("validateCheckinStore : Enter");
        String var4 = "";

        try {
            IPLMDictionaryPublicItf var5 = (new IPLMDictionaryPublicFactory()).getDictionary();
            String var6 = "Document";
            IPLMDictionaryPublicClassItf var7 = var5.getClass(var0, var6);
            var4 = var5.retrieveStorename(var0, var7);
            System.out.println("validateCheckinStore : Store From BL = " + var4);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        if (!"".equals(var4) && var4 != null) {
            var2 = var4;
        } else if (var2 == null || "".equals(var2)) {
            if (var1 != null) {
                var2 = MqlUtil.mqlCommand(var0, "print bus $1 select $2 dump", new String[]{var1, "policy.store"});
            } else if (var3 != null && !var3.isEmpty()) {
                var2 = MqlUtil.mqlCommand(var0, "print policy $1 select $2 dump", new String[]{var3, "store"});
            } else {
                var2 = getDefaultStore(var0);
            }
        }

        System.out.println("validateCheckinStore : Store @ Exit = " + var2);
        return var2;
    }

    private static List<TicketWrapper> getFCSPreCheckinTickets(Context var0, String var1, String var2, int var3, int var4) throws FoundationException {
        try {
            ArrayList var5 = new ArrayList(var4);

            for(int var6 = 0; var6 < var4; ++var6) {
                TicketWrapper var7 = PreCheckin.doIt(var0, var2, var1, var3);
                var5.add(var7);
            }

            return var5;
        } catch (Exception var8) {
            throw new FoundationException(var8);
        }
    }

    private static String getDefaultStore(Context var0) throws FoundationException {
        try {
            String var1 = PropertyUtil.getSchemaName(var0, "store_STORE");
            DomainObject var2 = PersonUtil.getPersonObject(var0);
            StringList var3 = new StringList(3);
            var3.add(SELECT_FILE_STORE_SYMBOLIC_NAME);
            String var4 = "to[" + PropertyUtil.getSchemaName(var0, "relationship_Employee") + "].from." + SELECT_FILE_STORE_SYMBOLIC_NAME;
            var3.add(var4);
            Map var5 = var2.getInfo(var0, var3);
            String var6 = (String)var5.get(SELECT_FILE_STORE_SYMBOLIC_NAME);
            if (var6 == null || "".equals(var6)) {
                var6 = (String)var5.get(var4);
            }

            if (var6 != null && !"".equals(var6)) {
                var1 = PropertyUtil.getSchemaName(var0, var6);
            }

            return var1;
        } catch (Exception var7) {
            throw new FoundationException(var7);
        }
    }

    private static Dataobject generateDownloadTicketData(TicketWrapper var0, List<String> var1) {
        String var2 = var0.getActionURL();
        String var4 = var0.getExportString();
        String var5 = null;

        try {
            var5 = "__fcs__jobTicket=" + URLEncoder.encode(var4, "UTF-8");
        } catch (UnsupportedEncodingException var8) {
            var8.printStackTrace();
        }

        String var6 = var2 + "?" + var5;
        Dataobject var7 = new Dataobject();
        DataelementMapAdapter.setDataelementValue(var7, "ticketURL", var6);
        if (var1.size() == 1) {
            DataelementMapAdapter.setDataelementValue(var7, "fileName", (String)var1.get(0));
        } else {
            DataelementMapAdapter.setDataelementValues(var7, "fileNames", var1);
        }

        return var7;
    }

    private static Dataobject generateCheckinTicketData(List<TicketWrapper> var0) {
        String var1 = ((TicketWrapper)var0.get(0)).getActionURL();
        Dataobject var3 = new Dataobject();
        DataelementMapAdapter.setDataelementValue(var3, "ticketURL", var1);
        DataelementMapAdapter.setDataelementValue(var3, "ticketparamname", "__fcs__jobTicket");
        if (var0.size() == 1) {
            String var4 = ((TicketWrapper)var0.get(0)).getExportString();
            DataelementMapAdapter.setDataelementValue(var3, "ticket", var4);
        } else {
            ArrayList var7 = new ArrayList(var0.size());
            Iterator var5 = var0.iterator();

            while(var5.hasNext()) {
                TicketWrapper var6 = (TicketWrapper)var5.next();
                var7.add(var6.getExportString());
            }

            DataelementMapAdapter.setDataelementValues(var3, "tickets", var7);
        }

        return var3;
    }

    public static void modifyDocumentInfo(Context var0, String var1, Map<String, String> var2) throws FoundationException {
        ObjectEditUtil.modify(var0, var1, var2, false, false);
    }

    public static void modifyFileInfo(Context var0, String var1, Map<String, String> var2) throws FoundationException {
        var2.remove(ATTRIBUTE_TITLE);
        ObjectEditUtil.modify(var0, var1, var2, false);
    }

    protected static List<String> addFiles(Context var0, String var1, String var2, List<Map<String, String>> var3) throws FoundationException {
        if (var1 != null && var2 != null && var3 != null && var3.size() != 0) {
            return processFileUpdates(var0, var1, var2, var3);
        } else {
            throw new FoundationUserException("Error: invalid arguments passed to add files.");
        }
    }

    private static List<String> processFileUpdates(Context var0, String var1, String var2, List<Map<String, String>> var3) throws FoundationException {
        ContextUtil.startTransaction(var0, true);

        try {
            Dataobject var4 = new Dataobject();
            var4.setId(var1);
            ArrayList var5 = new ArrayList(10);
            var5.add(SELECT_FILE_LOCKER);
            var5.add(SELECT_TYPE);
            var5.add(SELECT_POLICY);
            var5.add(SELECT_MASTER_FILE_IDS);
            var5.add(SELECT_MASTER_FILE_OBJECTIDS);
            var5.add(SELECT_MASTER_FILE_NAMES);
            var5.add(SELECT_MASTER_FILE_LOCKERS);
            var5.add(SELECT_DOCUMENT_FILES);
            var5.add(SELECT_DOCUMENT_FILEIDS);
            var5.add(SELECT_DOCUMENT_FORMATS);
            ObjectUtil.print(var0, var4, (PrintData)null, var5, true);
            String var6 = DataelementMapAdapter.getDataelementValue(var4, SELECT_TYPE.getName());
            String var7 = DataelementMapAdapter.getDataelementValue(var4, SELECT_FILE_LOCKER.getName());
            String var8 = DataelementMapAdapter.getDataelementValue(var4, SELECT_POLICY.getName());
            List var9 = DataelementMapAdapter.getDataelementValues(var4, SELECT_MASTER_FILE_NAMES.getName());
            List var10 = DataelementMapAdapter.getDataelementValues(var4, SELECT_MASTER_FILE_IDS.getName());
            List var11 = DataelementMapAdapter.getDataelementValues(var4, SELECT_MASTER_FILE_OBJECTIDS.getName());
            List var12 = DataelementMapAdapter.getDataelementValues(var4, SELECT_MASTER_FILE_LOCKERS.getName());
            List var13 = DataelementMapAdapter.getDataelementValues(var4, SELECT_DOCUMENT_FILEIDS.getName());
            List var14 = DataelementMapAdapter.getDataelementValues(var4, SELECT_DOCUMENT_FILES.getName());
            List var15 = DataelementMapAdapter.getDataelementValues(var4, SELECT_DOCUMENT_FORMATS.getName());
            ArrayList var16 = new ArrayList(var13.size());
            removeThumbnailFiles(var1, var13, var15, var14, (List)null, (List)null, var16);
            ArrayList var17 = new ArrayList(var3.size());
            StringList var18 = new StringList(var3.size());
            StringList var19 = new StringList(var3.size());
            String var20 = null;
            boolean var21 = false;
            boolean var22 = isTypeVersionable(var0, var6);
            Iterator var23 = var3.iterator();

            String var27;
            String var28;
            String var40;
            while(var23.hasNext()) {
                Map var24 = (Map)var23.next();
                String var25 = (String)var24.get(ATTRIBUTE_TITLE);
                if (var25 == null) {
                    throw new FoundationUserException("Error: Attribute title is required for file checkin.");
                }

                String var26 = (String)var24.remove(SELECT_ID.getExpression());
                var27 = (String)var24.remove("format");
                if (var27 == null) {
                    var20 = var20 == null ? getDefaultFormat(var0, var8) : var20;
                    var27 = var20;
                }

                var28 = (String)var24.remove("keepLocked");
                String var29;
                int var30;
                String var31;
                String var32;
                if (var22) {
                    if (var26 != null) {
                        var30 = var10.indexOf(var26);
                        var30 = var30 == -1 ? var11.indexOf(var26) : var30;
                        if (var30 == -1) {
                            var31 = PropertyUtil.getTranslatedValue(var0, "DocumentManagement", "emxDocumentManagement.Error.VersionNonActiveFile", var0.getLocale());
                            throw new FoundationUserException(var31, "emxDocumentManagement.Error.VersionNonActiveFile");
                        }

                        var31 = (String)var9.get(var30);
                        if (!var31.equals(var25) && var9.contains(var25)) {
                            var32 = PropertyUtil.getTranslatedValue(var0, "DocumentManagement", "emxDocumentManagement.Error.UpdateNotAllowedDueToConflict", var0.getLocale());
                            var32 = replaceToken(var32, "%FILE%", var25);
                            throw new FoundationUserException(var32, "emxDocumentManagement.Error.UpdateNotAllowedDueToConflict");
                        }

                        var32 = (String)var12.get(var30);
                        if (!var32.isEmpty() && !var0.getUser().equals(var32)) {
                            String var41 = PropertyUtil.getTranslatedValue(var0, "DocumentManagement", "emxDocumentManagement.Error.UpdateNotAllowedDueToLock", var0.getLocale());
                            var41 = replaceToken(var41, "%FILE%", var25);
                            throw new FoundationUserException(var41, "emxDocumentManagement.Error.UpdateNotAllowedDueToLock");
                        }

                        int var33 = var14.indexOf(var31);
                        var27 = (String)var15.get(var33);
                        var29 = reviseFile(var0, var1, var26, var31, var27, var24);
                        var9.set(var30, var25);
                        var10.set(var30, var29);
                    } else {
                        if (var9.contains(var25)) {
                            var40 = PropertyUtil.getTranslatedValue(var0, "DocumentManagement", "emxDocumentManagement.Error.UpdateNotAllowedDueToConflict", var0.getLocale());
                            var40 = replaceToken(var40, "%FILE%", var25);
                            throw new FoundationUserException(var40, "emxDocumentManagement.Error.UpdateNotAllowedDueToConflict");
                        }

                        var29 = createFile(var0, var1, var24);
                        var9.add(var25);
                        var10.add(var29);
                    }

                    if ("TRUE".equalsIgnoreCase(var28)) {
                        lockFile(var0, var29);
                    }
                } else {
                    var29 = null;
                    if (var26 != null) {
                        var30 = var13.indexOf(var26);
                        if (var30 == -1) {
                            var31 = PropertyUtil.getTranslatedValue(var0, "DocumentManagement", "emxDocumentManagement.Error.VersionNonActiveFile", var0.getLocale());
                            throw new FoundationUserException(var31, "emxDocumentManagement.Error.VersionNonActiveFile");
                        }

                        var31 = (String)var14.get(var30);
                        if (!var31.equals(var25) && var14.contains(var25)) {
                            var32 = PropertyUtil.getTranslatedValue(var0, "DocumentManagement", "emxDocumentManagement.Error.UpdateNotAllowedDueToConflict", var0.getLocale());
                            var32 = replaceToken(var32, "%FILE%", var25);
                            throw new FoundationUserException(var32, "emxDocumentManagement.Error.UpdateNotAllowedDueToConflict");
                        }

                        var27 = (String)var15.get(var30);
                        var14.set(var30, var25);
                        MqlUtil.mqlCommand(var0, "delete bus $1 format $2 file $3", new String[]{var1, var27, var31});
                    } else if (var16.contains(var27 + "::" + var25)) {
                        var40 = PropertyUtil.getTranslatedValue(var0, "DocumentManagement", "emxDocumentManagement.Error.UpdateNotAllowedDueToConflict", var0.getLocale());
                        var40 = replaceToken(var40, "%FILE%", var25);
                        throw new FoundationUserException(var40, "emxDocumentManagement.Error.UpdateNotAllowedDueToConflict");
                    }

                    if ("TRUE".equalsIgnoreCase(var28)) {
                        var21 = true;
                    }

                    if (var7 != null && !"".equals(var7) && !var0.getUser().equals(var7)) {
                        var40 = PropertyUtil.getTranslatedValue(var0, "DocumentManagement", "emxDocumentManagement.Error.AlreadyLockedByAnotherUser", var0.getLocale());
                        var40 = replaceToken(var40, "%FILE%", var25);
                        throw new FoundationUserException(var40, "emxDocumentManagement.Error.AlreadyLockedByAnotherUser");
                    }

                    var13.add("");
                    var14.add(var25);
                    var15.add(var27);
                    var16.add(var27 + "::" + var25);
                }

                if (var29 != null) {
                    var17.add(var29);
                }

                var18.add(var25);
                var19.add(var27);
            }

            checkinMasterFile(var0, var1, var2, var18, var19);
            if (var17.isEmpty()) {
                var5.clear();
                var5.add(SELECT_DOCUMENT_FILEIDS);
                var5.add(SELECT_DOCUMENT_FORMATS);
                var5.add(SELECT_DOCUMENT_FILES);
                ObjectUtil.print(var0, var4, (PrintData)null, var5, true);
                List var35 = DataelementMapAdapter.getDataelementValues(var4, SELECT_DOCUMENT_FILEIDS.getName());
                List var36 = DataelementMapAdapter.getDataelementValues(var4, SELECT_DOCUMENT_FORMATS.getName());
                List var37 = DataelementMapAdapter.getDataelementValues(var4, SELECT_DOCUMENT_FILES.getName());
                var16 = new ArrayList(var37.size());
                removeThumbnailFiles(var1, var35, var36, var37, (List)null, (List)null, var16);

                for(int var38 = 0; var38 < var18.size(); ++var38) {
                    var27 = (String)var18.get(var38);
                    var28 = (String)var19.get(var38);
                    int var39 = var16.indexOf(var28 + "::" + var27);
                    var40 = (String)var35.get(var39);
                    var17.add(var40);
                }
            }

            if (var21 && var7 == null) {
                lockFile(var0, var1);
            } else if (!var21 && var7 != null && !var7.isEmpty()) {
                unlockFile(var0, var1);
            }

            ContextUtil.commitTransaction(var0);
            return var17;
        } catch (Exception var34) {
            ContextUtil.abortTransaction(var0);
            throw new FoundationException(var34);
        }
    }

    protected static String updateDocumentFile(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        ArrayList var4 = new ArrayList(4);
        var4.add(SELECT_MASTER_FILE_IDS);
        var4.add(SELECT_DOCUMENT_FILEIDS);
        var4.add(SELECT_DOCUMENT_FORMATS);
        var4.add(SELECT_DOCUMENT_FILES);
        Dataobject var5 = new Dataobject();
        var5.setId(var1);
        ObjectUtil.print(var0, var5, (PrintData)null, var4, true);
        List var6 = DataelementMapAdapter.getDataelementValues(var5, SELECT_MASTER_FILE_IDS.getName());
        List var7 = DataelementMapAdapter.getDataelementValues(var5, SELECT_DOCUMENT_FILEIDS.getName());
        List var8 = DataelementMapAdapter.getDataelementValues(var5, SELECT_DOCUMENT_FORMATS.getName());
        List var9 = DataelementMapAdapter.getDataelementValues(var5, SELECT_DOCUMENT_FILES.getName());
        ArrayList var10 = new ArrayList(var9.size());
        removeThumbnailFiles(var1, var7, var8, var9, (List)null, (List)null, var10);
        if (var7.size() != 1) {
            throw new FoundationUserException("Error: in order to modify/revise the document file, it must contain just one file; otherwise, file id must be specified.");
        } else {
            String var11;
            if (!var6.isEmpty()) {
                var11 = (String)var6.get(0);
            } else {
                var11 = (String)var7.get(0);
            }

            var3.put(SELECT_ID.getExpression(), var11);
            return updateFile(var0, var1, var2, var3);
        }
    }

    protected static String updateFile(Context var0, String var1, String var2, Map<String, String> var3) throws FoundationException {
        ArrayList var4 = new ArrayList(1);
        var4.add(var3);
        List var5 = addFiles(var0, var1, var2, var4);
        String var6 = (String)var5.get(0);
        return var6;
    }

    public static String updateFile(Context var0, String var1, String var2, String var3, String var4) throws FoundationException {
        HashMap var5 = new HashMap(2);
        var5.put(ATTRIBUTE_CHECKIN_REASON, var4);
        var5.put(ATTRIBUTE_TITLE, var3);
        var5.put(SELECT_ID.getExpression(), var2);
        ArrayList var6 = new ArrayList(1);
        var6.add(var5);
        List var7 = processFileUpdates(var0, var1, (String)null, var6);
        String var8 = (String)var7.get(0);
        return var8;
    }

    private static String reviseFile(Context var0, String var1, String var2, String var3, String var4, Map<String, String> var5) throws FoundationException {
        unlockFile(var0, var2);
        String var6 = ObjectEditUtil.revise(var0, var2, (String)null, false);
        MqlUtil.mqlCommand(var0, "modify bus $1 move from $2 format $3 file $4", new String[]{var2, var1, var4, var3});
        ObjectEditUtil.modify(var0, var6, var5, false);
        return var6;
    }

    protected static void unlockFile(Context var0, String var1) throws FoundationException {
        MqlUtil.mqlCommand(var0, "unlock bus $1", new String[]{var1});
    }

    protected static void lockFile(Context var0, String var1) throws FoundationException {
        MqlUtil.mqlCommand(var0, "lock bus $1", new String[]{var1});
    }

    private static void checkinMasterFile(Context var0, String var1, String var2, StringList var3, StringList var4) throws FoundationException {
        try {
            String var9;
            if (var2 != null) {
                ArrayList var7 = new ArrayList(var3.size());

                for(int var8 = 0; var8 < var3.size(); ++var8) {
                    var9 = (String)var3.get(var8);
                    String var10 = (String)var4.get(var8);
                    BusinessObjectProxy var11 = new BusinessObjectProxy(var1, var10, var9, true, false);
                    var7.add(var11);
                }

                Checkin.doIt(var0, var2, (String)null, var7);
            } else {
                String var13 = getDefaultStore(var0);
                DomainObject var14 = new DomainObject(var1);
                var9 = (String)var4.get(0);
                var14.checkinFromServer(var0, false, true, var9, var13, var3);
            }

        } catch (Exception var12) {
            throw new FoundationException(var12);
        }
    }

    private static String createFile(Context var0, String var1, Map<String, String> var2) throws FoundationException {
        try {
            String var3 = DomainObject.newInstance(var0, var1).getInfo(var0, "type");
            String var4 = POLICY_VERSION;
            String var5 = "" + UUID.randomUUID();
            String var6 = getDefaultRevision(var0, var4);
            var2.put(ATTRIBUTE_IS_VERSION_OBJECT, "True");
            String var7 = ObjectEditUtil.create(var0, var3, var5, var6, var4, (String)null, var1, false, false, var2);
            ObjectEditUtil.connect(var0, var1, RELATIONSHIP_ACTIVE_VERSION, var7, (Map)null, false);
            ObjectEditUtil.connect(var0, var1, RELATIONSHIP_LATEST_VERSION, var7, (Map)null, false);
            return var7;
        } catch (Exception var8) {
            throw new FoundationException(var8);
        }
    }

    public static Datacollection getRelatedDocuments(Context var0, String var1, String var2, String var3, List<Selectable> var4) throws FoundationException {
        if (var2 != null && !var2.isEmpty()) {
            Datacollection var5;
            Dataobject var6;
            if (var2.equals("PLMDocConnection")) {
                if (_DocumentServices == null || _GETDocuments == null) {
                    throw new FoundationException("VPLMJDocumentServices class or getDocuments method not found.");
                }

                var6 = null;

                MapList var12;
                try {
                    var0.setApplication("VPLM");
                    HashMap var13 = new HashMap();
                    var13.put("objectId", var1);
                    var13.put("parentRelName", var2);
                    var13.put("parentDirection", var3);
                    var12 = (MapList)_GETDocuments.invoke(_DocumentServices, var0, var13);
                } catch (Exception var11) {
                    throw new FoundationException(var11);
                }

                var5 = new Datacollection();

                for(int var14 = 0; var14 < var12.size(); ++var14) {
                    Map var8 = (Map)var12.get(var14);
                    String var9 = (String)var8.get("id");
                    if (var9 != null) {
                        Dataobject var10 = new Dataobject();
                        var10.setId(var9);
                        var5.getDataobjects().add(var10);
                    }
                }

                ObjectUtil.print(var0, var5, (PrintData)null, var4, true);
            } else {
                var6 = new Dataobject();
                var6.setId(var1);
                ExpandData var7 = new ExpandData();
                var7.setRelationshipPattern(var2);
                if ("to".equals(var3)) {
                    var7.setGetTo(true);
                } else {
                    var7.setGetFrom(true);
                }

                var5 = ObjectUtil.expand(var0, var6, var7, var4);
            }

            return var5;
        } else {
            throw new FoundationUserException("Error: relationship for related parent id is required.");
        }
    }

    public static String connectToParent(Context var0, String var1, String var2, String var3, String var4) throws FoundationException {
        String var5 = null;
        if (var2 != null && var3 != null) {
            if (var3.equals("PLMDocConnection")) {
                if (_DocumentServices == null || _AttachDocuments == null) {
                    throw new FoundationException("VPLMJDocumentServices class or attachDocuments method not found");
                }

                try {
                    HashMap var9 = new HashMap();
                    var9.put("objectId", var2);
                    String[] var7 = new String[]{var1};
                    var9.put("documentIds", var7);
                    var0.setApplication("VPLM");
                    _AttachDocuments.invoke(_DocumentServices, var0, var9);
                    var5 = "";
                } catch (Exception var8) {
                    throw new FoundationException(var8);
                }
            } else {
                boolean var6 = false;
                if (var3.equals("PLMDocConnection") || var3.equals("SpecificationDocument")) {
                    var6 = true;
                }

                if ("to".equalsIgnoreCase(var4)) {
                    var5 = ObjectEditUtil.connect(var0, var1, var3, var2, (Map)null, false, false, false, var6);
                } else {
                    var5 = ObjectEditUtil.connect(var0, var2, var3, var1, (Map)null, false, false, false, var6);
                }
            }
        }

        return var5;
    }

    public static void disconnectFromParent(Context var0, String var1, String var2, String var3, String var4) throws FoundationException {
        try {
            if (var2 != null && var3 != null) {
                if (var3.equals("PLMDocConnection")) {
                    if (_DocumentServices == null || _DetachDocuments == null) {
                        throw new FoundationException("VPLMJDocumentServices class or detachDocuments method not found");
                    }

                    try {
                        HashMap var9 = new HashMap();
                        var9.put("objectId", var2);
                        String[] var6 = new String[]{var1};
                        var9.put("documentIds", var6);
                        var0.setApplication("VPLM");
                        _DetachDocuments.invoke(_DocumentServices, var0, var9);
                    } catch (Exception var7) {
                        throw new FoundationException(var7);
                    }
                } else {
                    boolean var5 = false;
                    if (var3.equals("PLMDocConnection") || var3.equals("SpecificationDocument")) {
                        var5 = true;
                    }

                    if ("to".equalsIgnoreCase(var4)) {
                        ObjectEditUtil.disconnect(var0, var1, var3, var2, false, false, var5);
                    } else {
                        ObjectEditUtil.disconnect(var0, var2, var3, var1, false, false, var5);
                    }
                }
            }

        } catch (Exception var8) {
            throw new FoundationException(var8);
        }
    }

    public static Datacollections getParents(Context var0, Datacollection var1, List<Selectable> var2, String var3, String var4) throws FoundationException {
        Datacollections var5;
        if ("PLMDocConnection".equals(var3)) {
            var5 = new Datacollections();
            var5.setInfo("Notice: parents are not supported for relationship: PLMDocConnection");
        } else {
            ExpandData var6 = new ExpandData();
            var6.setRelationshipPattern(var3);
            var6.setGetFrom("to".equalsIgnoreCase(var4));
            var6.setGetTo(!"to".equalsIgnoreCase(var4));
            var5 = ObjectUtil.expand(var0, var1, var6, var2);
        }

        return var5;
    }

    public static void getParentIds(Context var0, Datacollection var1, String var2, String var3, String var4) throws FoundationException {
        String var5;
        if ("to".equalsIgnoreCase(var4)) {
            var5 = "from[" + var3 + "].to.physicalid";
        } else {
            var5 = "to[" + var3 + "].from.physicalid";
        }

        ArrayList var6 = new ArrayList(1);
        var6.add(new Select(var2, var5, (ExpressionType)null, (Format)null, true));
        ObjectUtil.print(var0, var1, (PrintData)null, var6, true);
        Iterator var7 = var1.getDataobjects().iterator();

        while(var7.hasNext()) {
            Dataobject var8 = (Dataobject)var7.next();
            String var9 = DataelementMapAdapter.getDataelementValue(var8, var2);
            DataelementMapAdapter.setDataelementValue(var8, var2, var9);
        }

    }

    public static String getDefaultRevision(Context var0, String var1) throws FoundationException {
        String var2 = null;
        if (POLICY_DOCUMENT.equals(var1)) {
            if (var2 == null) {
                var2 = getPolicyFirstSequence(var0, POLICY_DOCUMENT);
            }
        } else if (POLICY_VERSION.equals(var1)) {
            if (var2 == null) {
                var2 = getPolicyFirstSequence(var0, POLICY_VERSION);
            }
        } else {
            var2 = getPolicyFirstSequence(var0, var1);
        }

        return var2;
    }

    public static String getPolicyFirstSequence(Context var0, String var1) throws FoundationException {
        try {
            Policy var2 = new Policy(var1);
            var2.open(var0);
            String var3 = var2.getFirstInMinorSequence();
            var2.close(var0);
            return var3;
        } catch (Exception var4) {
            throw new FoundationException(var4);
        }
    }

    public static void deleteDocument(Context var0, String var1) throws FoundationException {
        Dataobject var2 = new Dataobject();
        var2.setId(var1);
        ArrayList var3 = new ArrayList();
        String var4 = "from[" + RELATIONSHIP_ACTIVE_VERSION + "].to.revisions.id";
        var3.add(new Select(var4, var4, (ExpressionType)null, (Format)null, true));
        var3.add(new Select((String)null, SELECTABLE_KINDOF_DOCUMENT, ExpressionType.BUS, (Format)null, false));
        var2 = ObjectUtil.print(var0, var2, (PrintData)null, var3, true);
        String var5 = DataelementMapAdapter.getDataelementValue(var2, SELECTABLE_KINDOF_DOCUMENT);
        if (_detachDOCConnections != null && "TRUE".equalsIgnoreCase(var5)) {
            try {
                HashMap var6 = new HashMap();
                String[] var7 = new String[]{var1};
                var6.put("documentIds", var7);
                Context var8 = var0.getFrameContext(gscEventDeliverable.class.getName());

                try {
                    var8.start(true);
                    _detachDOCConnections.invoke(_DocumentServices, var8, var6);
                } catch (Exception var14) {
                    throw new FoundationException(var14);
                } finally {
                    var8.shutdown();
                }
            } catch (Exception var16) {
                throw new FoundationException(var16);
            }
        }

        List var17 = DataelementMapAdapter.getDataelementValues(var2, var4);
        Iterator var18 = var17.iterator();

        while(var18.hasNext()) {
            String var19 = (String)var18.next();
            MqlUtil.mqlCommand(var0, false, "delete bus $1", true, new String[]{var19});
        }

        ObjectEditUtil.delete(var0, var1, true);
    }

    public static void deleteFile(Context var0, String var1, Dataobject var2) throws FoundationException {
        String var3 = var2.getId();
        String var4 = DataelementMapAdapter.getDataelementValue(var2, SELECT_FILE_FORMAT.getName());
        String var5 = DataelementMapAdapter.getDataelementValue(var2, SELECT_FILE_TITLE.getName());
        Dataobject var6 = new Dataobject();
        var6.setId(var1);
        ArrayList var7 = new ArrayList(6);
        var7.add(SELECT_DOCUMENT_FILEIDS);
        var7.add(SELECT_DOCUMENT_FORMATS);
        var7.add(SELECT_DOCUMENT_FILES);
        var7.add(SELECT_MASTER_FILE_IDS);
        var7.add(SELECT_MASTER_FILE_OBJECTIDS);
        var7.add(SELECT_MASTER_FILE_NAMES);
        ObjectUtil.print(var0, var6, (PrintData)null, var7, true);
        List var8 = DataelementMapAdapter.getDataelementValues(var6, SELECT_DOCUMENT_FILEIDS.getName());
        List var9 = DataelementMapAdapter.getDataelementValues(var6, SELECT_DOCUMENT_FORMATS.getName());
        List var10 = DataelementMapAdapter.getDataelementValues(var6, SELECT_DOCUMENT_FILES.getName());
        ArrayList var11 = new ArrayList(var10.size());
        removeThumbnailFiles(var1, var8, var9, var10, (List)null, (List)null, var11);
        List var12 = DataelementMapAdapter.getDataelementValues(var6, SELECT_MASTER_FILE_IDS.getName());
        List var13 = DataelementMapAdapter.getDataelementValues(var6, SELECT_MASTER_FILE_OBJECTIDS.getName());
        List var14 = DataelementMapAdapter.getDataelementValues(var6, SELECT_MASTER_FILE_NAMES.getName());
        String var15 = null;
        String var16 = null;
        int var17;
        if (!var12.isEmpty()) {
            var17 = var12.indexOf(var3);
            if (var17 == -1) {
                var17 = var13.indexOf(var3);
            }

            if (var17 != -1) {
                String var18 = (String)var14.get(var17);
                var17 = var10.indexOf(var18);
            }
        } else {
            var17 = var8.indexOf(var3);
            if (var17 == -1) {
                var17 = var11.indexOf(var4 + "::" + var5);
            }
        }

        if (var17 == -1) {
            throw new FoundationUserException("Error: invalid file id passed.");
        } else {
            var16 = (String)var10.get(var17);
            var15 = (String)var9.get(var17);
            if (!var12.isEmpty()) {
                Dataobject var23 = new Dataobject();
                var23.setId(var3);
                var7.clear();
                var7.add(new Select("revisions.id", "revisions.id", (ExpressionType)null, (Format)null, true));
                ObjectUtil.print(var0, var23, (PrintData)null, var7, true);
                List var20 = DataelementMapAdapter.getDataelementValues(var23, "revisions.id");
                Iterator var21 = var20.iterator();

                while(var21.hasNext()) {
                    String var22 = (String)var21.next();
                    MqlUtil.mqlCommand(var0, false, "delete bus $1", true, new String[]{var22});
                }
            }

            MqlUtil.mqlCommand(var0, "delete bus $1 format $2 file $3", new String[]{var1, var15, var16});
        }
    }

    public static void deleteVersion(Context var0, Dataobject var1) throws FoundationException {
        if (var1.getId() != null && !"".equals(var1.getId())) {
            ArrayList var2 = new ArrayList();
            var2.add(SELECT_TITLE);
            var2.add(SELECT_FILE_MASTER_DOC_ID);
            var2.add(SELECT_FILE_ACTIVE_VERSION_ID);
            var2.add(SELECT_FILE_LATEST_VERSION_ID);
            var2.add(SELECT_FILE_PREVIOUS_ID);
            var2.add(SELECT_FILE_PREVIOUS_FILE_NAME);
            var2.add(SELECT_FILE_PREVIOUS_FILE_FORMAT);
            var2.add(SELECT_FILE_MASTER_FILES);
            var2.add(SELECT_FILE_MASTER_FORMATS);
            ObjectUtil.print(var0, var1, (PrintData)null, var2, true);
            String var3 = DataelementMapAdapter.getDataelementValue(var1, SELECT_FILE_MASTER_DOC_ID.getName());
            if (var3 != null && !var3.isEmpty()) {
                List var4 = DataelementMapAdapter.getDataelementValues(var1, SELECT_FILE_MASTER_FILES.getName());
                List var5 = DataelementMapAdapter.getDataelementValues(var1, SELECT_FILE_MASTER_FORMATS.getName());
                removeThumbnailFiles((String)null, (List)null, var5, var4, (List)null, (List)null, (List)null);
                String var6 = DataelementMapAdapter.getDataelementValue(var1, SELECT_TITLE.getName());
                int var7 = var4.indexOf(var6);
                String var8 = (String)var5.get(var7);
                MqlUtil.mqlCommand(var0, "delete bus $1 format $2 file $3", new String[]{var3, var8, var6});
                String var9 = DataelementMapAdapter.getDataelementValue(var1, SELECT_FILE_PREVIOUS_ID.getName());
                if (var9 != null && !var9.isEmpty()) {
                    String var10 = DataelementMapAdapter.getDataelementValue(var1, SELECT_FILE_ACTIVE_VERSION_ID.getName());
                    String var11 = DataelementMapAdapter.getDataelementValue(var1, SELECT_FILE_LATEST_VERSION_ID.getName());
                    HashMap var12 = new HashMap();
                    var12.put("to", var9);
                    ObjectEditUtil.modifyConnection(var0, var10, var12, false);
                    ObjectEditUtil.modifyConnection(var0, var11, var12, false);
                    String var13 = DataelementMapAdapter.getDataelementValue(var1, SELECT_FILE_PREVIOUS_FILE_NAME.getName());
                    String var14 = DataelementMapAdapter.getDataelementValue(var1, SELECT_FILE_PREVIOUS_FILE_FORMAT.getName());
                    MqlUtil.mqlCommand(var0, "modify bus $1 move from $2 format $3 file $4", new String[]{var3, var9, var14, var13});
                }
            }

            MqlUtil.mqlCommand(var0, "delete bus $1_id", new String[]{var1.getId()});
        } else {
            throw new FoundationException("File Object Id is missing");
        }
    }

    public static File getFile(Context var0, String var1, String var2) throws FoundationException {
        ContextUtil.startTransaction(var0, true);

        try {
            Datacollection var3 = new Datacollection();
            Dataobject var4 = new Dataobject();
            var4.setId(var1);
            if (var2 != null) {
                Dataobject var5 = new Dataobject();
                var5.setId(var2);
                RelateddataMapAdapter.addRelatedData(var4, "files", var5);
            }

            var3.getDataobjects().add(var4);
            ArrayList var14 = new ArrayList(1);
            ArrayList var6 = generateDownloadBOPs(var0, var3, false, var14);
            BusinessObjectProxy var7 = (BusinessObjectProxy)var6.get(0);
            FileList var8 = new FileList();
            matrix.db.File var9 = new matrix.db.File(var7.getFileName(), var7.getFormat());
            var8.add(var9);
            String var10 = var0.createWorkspace();
            BusinessObject var11 = new BusinessObject(var7.getOID());
            var11.checkoutFiles(var0, false, var7.getFormat(), var8, var10);
            ContextUtil.commitTransaction(var0);
            File var12 = new File(var10, var7.getFileName());
            return var12;
        } catch (Exception var13) {
            ContextUtil.abortTransaction(var0);
            throw new FoundationException(var13);
        }
    }

    private static String getDefaultFormat(Context var0, String var1) throws FoundationException {
        String var2 = MqlUtil.mqlCommand(var0, "print policy $1 select $2 dump", new String[]{var1, "defaultformat"});
        if (var2.isEmpty() || FORMAT_INVALID_FORMAT.equals(var2)) {
            var2 = "";
            String var3 = MqlUtil.mqlCommand(var0, "print policy $1 select $2 dump $3", new String[]{var1, "format", "|"});
            List var4 = Arrays.asList(var3.split("|"));
            if (var4.contains(FORMAT_GENERIC)) {
                var2 = FORMAT_GENERIC;
            } else {
                var4.remove(FORMAT_INVALID_FORMAT);
                if (!var4.isEmpty()) {
                    var2 = (String)var4.get(0);
                }
            }
        }

        if (var2.isEmpty()) {
            throw new FoundationUserException("Error: no format available for checkin in policy: " + var1);
        } else {
            return var2;
        }
    }

    private static boolean isTypeVersionable(Context var0, String var1) throws FoundationException {
        Boolean var2 = (Boolean)_versionableTypes.get(var1);
        if (var2 == null) {
            synchronized(_versionableTypes) {
                var2 = (Boolean)_versionableTypes.get(var1);
                if (var2 == null) {
                    String var4 = MqlUtil.mqlCommand(var0, "print type $1 select $2 $3 dump", new String[]{var1, SELECTABLE_TYPE_KINDOF_DOCUMENTS, "property[DISALLOW_VERSIONING]"});
                    var4 = var4.toUpperCase();
                    var2 = "TRUE".equals(var4) || var4.startsWith("TRUE") && var4.endsWith("FALSE");
                    _versionableTypes.put(var1, var2);
                }
            }
        }

        return var2;
    }

    private static String replaceToken(String var0, String var1, String var2) {
        String var3 = var2 != null ? var2 : "";
        String var4 = var0.replace(var1, var3);
        return var4;
    }

    public static void reserveDocument(Context var0, String var1, String var2) throws FoundationException {
        String var3 = var2 != null ? var2 : "";
        MqlUtil.mqlCommand(var0, "modify bus $1 $2 comment $3", new String[]{var1, "reserve", var3});
    }

    public static void unreserveDocument(Context var0, String var1) throws FoundationException {
        MqlUtil.mqlCommand(var0, "modify bus $1 $2", new String[]{var1, "unreserve"});
    }

    public static void unlockDocument(Context var0, String var1) throws FoundationException {
        Dataobject var2 = new Dataobject();
        var2.setId(var1);
        ArrayList var3 = new ArrayList();
        String var4 = "from[" + RELATIONSHIP_ACTIVE_VERSION + "].to.id";
        var3.add(new Select(var4, var4, (ExpressionType)null, (Format)null, true));
        var2 = ObjectUtil.print(var0, var2, (PrintData)null, var3, true);
        List var5 = DataelementMapAdapter.getDataelementValues(var2, var4);
        Iterator var6 = var5.iterator();

        while(var6.hasNext()) {
            String var7 = (String)var6.next();
            unlockFile(var0, var7);
        }

    }

    public static void lockDocument(Context var0, String var1) throws FoundationException {
        Dataobject var2 = new Dataobject();
        var2.setId(var1);
        ArrayList var3 = new ArrayList();
        String var4 = "from[" + RELATIONSHIP_ACTIVE_VERSION + "].to.id";
        var3.add(new Select(var4, var4, (ExpressionType)null, (Format)null, true));
        var2 = ObjectUtil.print(var0, var2, (PrintData)null, var3, true);
        List var5 = DataelementMapAdapter.getDataelementValues(var2, var4);
        Iterator var6 = var5.iterator();

        while(var6.hasNext()) {
            String var7 = (String)var6.next();
            lockFile(var0, var7);
        }

    }

    public static JsonObject getStreamInfo(Context var0, String[] var1, String var2, boolean var3, boolean var4) throws FoundationException {
        ArrayList var5 = new ArrayList(1);
        JsonObjectBuilder var6 = Json.createObjectBuilder();
        JsonArrayBuilder var7 = Json.createArrayBuilder();

        String var9;
        for(int var8 = 0; var8 < var1.length; ++var8) {
            var9 = var1[var8];
            Dataobject var10 = new Dataobject();
            var10.setId(var9);
            ArrayList var11 = new ArrayList();
            String var12 = "from[" + RELATIONSHIP_ACTIVE_VERSION + "].to.id";
            var11.add(new Select(var12, var12, (ExpressionType)null, (Format)null, true));
            var11.add(new Select("format.file.name", "format.file.name", (ExpressionType)null, (Format)null, true));
            var11.add(new Select("format.file.fileid", "format.file.fileid", (ExpressionType)null, (Format)null, true));
            var11.add(new Select("format.file.checksum", "format.file.checksum", (ExpressionType)null, (Format)null, true));
            var11.add(new Select("format.file.format", "format.file.format", (ExpressionType)null, (Format)null, true));
            var11.add(new Select("cestamp", "cestamp", (ExpressionType)null, (Format)null, true));
            var11.add(SELECT_NAME);
            var11.add(SELECT_REVISION);
            JsonObjectBuilder var13 = Json.createObjectBuilder();
            var13.add("physicalid", var9);
            JsonArrayBuilder var14 = Json.createArrayBuilder();
            JsonArrayBuilder var15 = Json.createArrayBuilder();

            try {
                if (var4) {
                    try {
                        System.out.println("waiting for.... " + var9 + " ...count...0 ");
                        Thread.sleep(3000L);
                    } catch (InterruptedException var31) {
                        var31.printStackTrace();
                    }
                }

                var10 = ObjectUtil.print(var0, var10, (PrintData)null, var11, true);
                List var16 = DataelementMapAdapter.getDataelementValues(var10, var12);
                List var17 = DataelementMapAdapter.getDataelementValues(var10, "format.file.name");
                List var18 = DataelementMapAdapter.getDataelementValues(var10, "format.file.fileid");
                List var19 = DataelementMapAdapter.getDataelementValues(var10, "format.file.checksum");
                List var20 = DataelementMapAdapter.getDataelementValues(var10, "format.file.format");
                List var21 = DataelementMapAdapter.getDataelementValues(var10, "cestamp");
                String var22 = DataelementMapAdapter.getDataelementValue(var10, SELECT_REVISION.getName());
                String var23 = DataelementMapAdapter.getDataelementValue(var10, SELECT_NAME.getName());
                var13.add("cestamp", (String)var21.get(0));
                if (var16.size() != 1) {
                    var13.add("status", "authoring not found");
                } else {
                    int var24 = 1;
                    if (var17.get(0) == null || ((String)var17.get(0)).isEmpty()) {
                        boolean var25 = true;

                        while(var25) {
                            try {
                                System.out.println("waiting for.... " + var9 + " ...count... " + var24);
                                Thread.sleep(5000L);
                                var10 = ObjectUtil.print(var0, var10, (PrintData)null, var11, true);
                                var17 = DataelementMapAdapter.getDataelementValues(var10, "format.file.name");
                                var18 = DataelementMapAdapter.getDataelementValues(var10, "format.file.fileid");
                                var19 = DataelementMapAdapter.getDataelementValues(var10, "format.file.checksum");
                                var20 = DataelementMapAdapter.getDataelementValues(var10, "format.file.format");
                                if (var17.get(0) != null && !((String)var17.get(0)).isEmpty() || var24 == 3) {
                                    break;
                                }

                                ++var24;
                            } catch (InterruptedException var32) {
                                var32.printStackTrace();
                            }
                        }
                    }

                    var13.add("status", "authoring found");
                    JsonObjectBuilder var38 = Json.createObjectBuilder();
                    var38.add("key", "filename");
                    var38.add("value", (String)var17.get(0));
                    var15.add(var38);
                    JsonObjectBuilder var26 = Json.createObjectBuilder();
                    var26.add("key", "filetype");
                    var26.add("value", ((String)var17.get(0)).substring(((String)var17.get(0)).lastIndexOf(".") + 1));
                    var15.add(var26);
                    JsonObjectBuilder var27 = Json.createObjectBuilder();
                    var27.add("key", "fileid");
                    var27.add("value", (String)var18.get(0));
                    var15.add(var27);
                    JsonObjectBuilder var28 = Json.createObjectBuilder();
                    var28.add("key", "filechecksum");
                    var28.add("value", (String)var19.get(0));
                    var15.add(var28);
                    JsonObjectBuilder var29 = Json.createObjectBuilder();
                    var29.add("key", "filewatermarkstamp");
                    var29.add("value", (String)var19.get(0));
                    var15.add(var29);
                }

                if (var3) {
                    String var37 = var23 + "." + var22;
                    var37 = filterFileBadCharacters(var37);
                    BusinessObjectProxy var39 = new BusinessObjectProxy(var9, (String)var20.get(0), (String)var17.get(0), var37, true, false);
                    var5.add(var39);
                }
            } catch (Exception var33) {
                var13.add("status", "Invalid Object Id");
            }

            JsonObjectBuilder var36 = Json.createObjectBuilder();
            var36.add("streamId", "authoring");
            var36.add("attributes", var15);
            var14.add(var36);
            var13.add("streaminfo", var14);
            var7.add(var13);
        }

        var6.add("results", var7);
        if (var3) {
            try {
                TicketWrapper var34 = Checkout.doIt(var0, false, "", var2, "", var5, true);
                var9 = var34.getActionURL();
                String var35 = var34.getExportString();
                var6.add("ticketUrl", var9);
                var6.add("ticket", var35);
            } catch (Exception var30) {
            }
        }

        System.out.println("###@@@ jsonConverterAttrArray  " + var6.build());
        return var6.build();
    }

    public static JsonArray notifyMissingDO(Context var0, JsonArray var1) throws FoundationException {
        JsonArrayBuilder var2 = Json.createArrayBuilder();
        Iterator var3 = var1.getValuesAs(JsonObject.class).iterator();

        while(true) {
            JsonObject var4;
            do {
                if (!var3.hasNext()) {
                    System.out.println("### Document class jsonResponseArray.build():  " + var2.build());
                    return var2.build();
                }

                var4 = (JsonObject)var3.next();
            } while(var4 == null);

            String var5 = var4.getString("id");
            String var6 = var4.getString("format");
            Dataobject var7 = new Dataobject();
            var7.setId(var5);
            ArrayList var8 = new ArrayList();
            String var9 = "from[" + RELATIONSHIP_ACTIVE_VERSION + "].to.id";
            var8.add(new Select(var9, var9, (ExpressionType)null, (Format)null, true));
            var8.add(new Select((String)null, SELECTABLE_KINDOF_DOCUMENT, ExpressionType.BUS, (Format)null, false));
            var8.add(SELECT_TYPE);
            JsonObjectBuilder var10 = Json.createObjectBuilder();
            var10.add("id", var5);
            var10.add("format", var6);

            try {
                var7 = ObjectUtil.print(var0, var7, (PrintData)null, var8, true);
                String var11 = DataelementMapAdapter.getDataelementValue(var7, SELECTABLE_KINDOF_DOCUMENT);
                String var12 = DataelementMapAdapter.getDataelementValue(var7, SELECT_TYPE.getName());
                List var13 = DataelementMapAdapter.getDataelementValues(var7, var9);
                if ("TRUE".equalsIgnoreCase(var11) && var13.size() == 1) {
                    String[] var14 = new String[4];
                    var14[0] = var5;
                    if (var12 != null && !var12.isEmpty()) {
                        var14[1] = var12;
                    } else {
                        var14[1] = "Document";
                    }

                    var14[2] = "fileOnDemand";
                    var14[3] = (String)var13.get(0);
                    JPO.invoke(var0, "emxCommonDocument", (String[])null, "publishCheckinEvent", var14);
                    var10.add("status", "DORequested");

                    try {
                        ContextUtil.pushContext(var0);
                        MqlUtil.mqlCommand(var0, "log bus $1 eventmonitor  $2 event bus custom", new String[]{var5, "AdvancedSearch"});
                    } catch (Exception var20) {
                        var20.printStackTrace();
                    } finally {
                        ContextUtil.popContext(var0);
                    }
                } else {
                    var10.add("status", "Ignored");
                }
            } catch (Exception var22) {
                System.out.println("notifyMissingDO exception:  " + var22.getLocalizedMessage());
                var10.add("status", "AccessDenied");
            }

            var2.add(var10);
        }
    }

    static {
        SELECT_ORIGINATED_UTC = new Select((String)null, "originated", (ExpressionType)null, Format.UTC, false);
        SELECT_MODIFIED_UTC = new Select((String)null, "modified", (ExpressionType)null, Format.UTC, false);
        SELECT_POLICY = new Select((String)null, "policy", (ExpressionType)null, (Format)null, false);
        SELECT_POLICY_NLS = new Select((String)null, "policy", (ExpressionType)null, Format.POLICY, false);
        SELECT_HAS_CHECKOUT_ACCESS = new Select("checkout", "current.access[checkout]", (ExpressionType)null, (Format)null, false);
        SELECT_HAS_CHECKIN_ACCESS = new Select("checkin", "current.access[checkin]", (ExpressionType)null, (Format)null, false);
        SELECT_HAS_LOCK_ACCESS = new Select("lock", "current.access[lock]", (ExpressionType)null, (Format)null, false);
        SELECT_HAS_UNLOCK_ACCESS = new Select("unlock", "current.access[unlock]", (ExpressionType)null, (Format)null, false);
        SELECT_HAS_REVISE_ACCESS = new Select("revise", "current.access[revise]", (ExpressionType)null, (Format)null, false);
        SELECT_DOCUMENT_FILES = new Select("fileNames", "format.file.name", (ExpressionType)null, (Format)null, true);
        SELECT_DOCUMENT_FORMATS = new Select("fileFormats", "format.file.format", (ExpressionType)null, (Format)null, true);
        SELECT_DOCUMENT_FILEIDS = new Select("fileIds", "format.file.fileid", (ExpressionType)null, (Format)null, true);
        SELECT_DOCUMENT_FILE_SIZES = new Select("fileSizes", "format.file.size", (ExpressionType)null, (Format)null, true);
        SELECT_DOCUMENT_FILE_CHECKSUMS = new Select("fileChecksums", "format.file.checksum", (ExpressionType)null, (Format)null, true);
        SELECT_DOCUMENT_HAS_LOCK_ACCESS = new Select((String)null, "current.access[lock]", (ExpressionType)null, (Format)null, false);
        SELECT_FILE_LOCKED = new Select((String)null, "locked", (ExpressionType)null, (Format)null, false);
        SELECT_FILE_LOCKER = new Select((String)null, "locker", (ExpressionType)null, (Format)null, false);
        SELECT_FILE_FORMAT = new Select((String)null, "format", ExpressionType.KEY, (Format)null, false);
        SELECT_FILE_SIZE = new Select((String)null, "fileSize", ExpressionType.KEY, (Format)null, false);
        SELECT_FILE_CHECKSUM = new Select((String)null, "fileChecksum", ExpressionType.KEY, (Format)null, false);
        SELECT_MASTER_FILE_IDS = null;
        SELECT_MASTER_FILE_OBJECTIDS = null;
        SELECT_MASTER_FILE_NAMES = null;
        SELECT_MASTER_FILE_LOCKERS = null;
        SELECT_FILE_MASTER_DOCUMENT_ID = null;
        SELECT_FILE_MASTER_FILES = null;
        SELECT_FILE_MASTER_FORMATS = null;
        SELECT_FILE_MASTER_DOC_ID = null;
        SELECT_FILE_ACTIVE_VERSION_ID = null;
        SELECT_FILE_LATEST_VERSION_ID = null;
        SELECT_FILE_PREVIOUS_ID = null;
        SELECT_FILE_PREVIOUS_FILE_NAME = null;
        SELECT_FILE_PREVIOUS_FILE_FORMAT = null;
        SELECT_LAST = new Select((String)null, "last", (ExpressionType)null, (Format)null, false);
        com.matrixone.apps.domain.util.ContextUtil.Callable var0 = new com.matrixone.apps.domain.util.ContextUtil.Callable() {
            public Object call(Context var1) throws FoundationException {
                gscEventDeliverable.init(var1);
                return null;
            }
        };

        try {
            com.matrixone.apps.domain.util.ContextUtil.runInAnonymousContext(var0);
        } catch (Exception var5) {
            System.out.println(var5.getMessage());
        }

        _AttachDocuments = null;
        _DetachDocuments = null;
        _GETDocuments = null;
        _detachDOCConnections = null;
        _DocumentServices = null;

        try {
            Class var6 = Class.forName("com.dassault_systemes.VPLMJDocumentServices.VPLMJDocumentServices");
            Class[] var1 = new Class[0];
            Method var2 = var6.getMethod("getInstance", var1);
            Class[] var3 = new Class[]{Context.class, HashMap.class};
            _AttachDocuments = var6.getMethod("attachDocuments", var3);
            _DetachDocuments = var6.getMethod("detachDocuments", var3);
            _GETDocuments = var6.getMethod("getDocuments", var3);
            _DocumentServices = var2.invoke((Object)null);
            _detachDOCConnections = var6.getMethod("detachDOCConnections", var3);
        } catch (Exception var4) {
        }

    }
}

