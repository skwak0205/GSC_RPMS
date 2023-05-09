package com.gsc.apps.program;

import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.program.Currency;
import com.matrixone.apps.program.*;
import matrix.db.Context;
import matrix.db.JPO;
import matrix.util.MatrixException;
import matrix.util.StringList;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ResourceRequest extends DomainObject {
    public static final String STRING_COMMENT = "Comment";
    public static final double WORKING_HOURS_PER_DAY = 8.0;
    public static final String SELECT_ATTRIBUTE_START_DATE = "attribute[" + PropertyUtil.getSchemaProperty("attribute_StartDate") + "]";
    public static final String SELECT_ATTRIBUTE_END_DATE = "attribute[" + PropertyUtil.getSchemaProperty("attribute_EndDate") + "]";
    public static final String ATTRIBUTE_PHASE_START_DATE;
    public static final String ATTRIBUTE_PHASE_FINISH_DATE;
    public static final String SELECT_PROJECT_SPACE_ID;
    public static final String ATTRIBUTE_STANDARD_COST;
    public static final String ATTRIBUTE_RESOURCE_PLAN_PREFRENCE;
    public static final String RESOURCE_PLAN_PREFERENCE_PHASE = "Phase";
    public static final String RESOURCE_PLAN_PREFERENCE_TIMELINE = "Timeline";

    public ResourceRequest(String var1) throws Exception {
        super(var1);
    }

    public ResourceRequest() throws Exception {
    }

    public void create(Context var1, String var2, String var3, String var4, String var5, String var6, MapList var7, MapList var8) throws MatrixException {
        try {
            if (var1 == null) {
                throw new IllegalArgumentException("context");
            } else if (ProgramCentralUtil.isNullString(var2)) {
                throw new IllegalArgumentException("strProjectId");
            } else if (ProgramCentralUtil.isNullString(var5)) {
                throw new IllegalArgumentException("strResourcePoolId");
            } else {
                HashMap var9 = new HashMap();
                var9.put("projectObjectId", var2);
                var9.put("ProjectRole", var3);
                var9.put("Comment", var4);
                var9.put("ResourcePoolName", var5);
                var9.put("PreferredPersonDisplay", var6);
                var9.put("Skill", var7);
                var9.put("FTEValue", var8);
                String[] var10 = JPO.packArgs(var9);
                JPO.invoke(var1, "emxResourceRequest", (String[])null, "createResourcePlan", var10, String.class);
            }
        } catch (Exception var11) {
            var11.printStackTrace();
            throw new MatrixException(var11);
        }
    }

    public static MapList getFTE(Context var0, StringList var1) throws MatrixException {
        try {
            if (var0 == null) {
                throw new IllegalArgumentException("context");
            } else if (var1 != null && var1.size() != 0) {
                HashMap var2 = new HashMap();
                var2.put("ResourceRequestIds", var1);
                String[] var3 = JPO.packArgs(var2);
                return (MapList)JPO.invoke(var0, "emxResourceRequest", (String[])null, "getFTE", var3, MapList.class);
            } else {
                throw new IllegalArgumentException("slResourceRequestIds");
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            throw new MatrixException(var4);
        }
    }

    public FTE getFTE(Context var1) throws MatrixException {
        try {
            MapList var2 = getFTE(var1, new StringList(this.getId(var1)));
            if (var2 != null && var2.size() != 0) {
                Map var3 = (Map)var2.get(0);
                return (FTE)var3.get("RequestFTE");
            } else {
                throw new MatrixException("Failed to find FTE !");
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            throw new MatrixException(var4);
        }
    }

    public MapList getResourceInfo(Context var1, StringList var2, boolean var3, boolean var4) throws MatrixException {
        try {
            if (var1 == null) {
                throw new IllegalArgumentException("context");
            } else {
                HashMap var5 = new HashMap();
                var5.put("ResourceIds", var2);
                var5.put("getFTE", var3);
                var5.put("getResourceState", var4);
                String[] var6 = new String[]{this.getId()};
                String[] var7 = JPO.packArgs(var5);
                return (MapList)JPO.invoke(var1, "emxResourceRequest", var6, "getResourceInfo", var7, MapList.class);
            }
        } catch (Exception var8) {
            var8.printStackTrace();
            throw new MatrixException(var8);
        }
    }

    public void setResourceInfo(Context var1, String var2, FTE var3, String var4) throws MatrixException {
        try {
            if (var1 == null) {
                throw new IllegalArgumentException("context");
            } else if (var2 != null && !"".equals(var2) && !"null".equals(var2)) {
                if (var3 == null) {
                    var3 = FTE.getInstance(var1);
                }

                HashMap var5 = new HashMap();
                var5.put("strPersonId", var2);
                var5.put("fte", var3);
                var5.put("strResourceState", var4);
                String[] var6 = new String[]{this.getId()};
                String[] var7 = JPO.packArgs(var5);
                JPO.invoke(var1, "emxResourceRequest", var6, "setResourceInfo", var7);
            } else {
                throw new IllegalArgumentException("strPersonId");
            }
        } catch (Exception var8) {
            var8.printStackTrace();
            throw new MatrixException(var8);
        }
    }

    public void addResources(Context var1, MapList var2) throws MatrixException {
        try {
            Map var3 = (Map)var2.get(0);
            String var4 = (String)var3.get("resourcePlanPreference");
            if (var2 != null && var2.size() != 0) {
                String[] var5 = new String[]{this.getId()};
                String[] var6 = JPO.packArgs(var2);
                if (ProgramCentralUtil.isNotNullString(var4) && var4.equals(ProgramCentralConstants.TYPE_PHASE)) {
                    JPO.invoke(var1, "emxResourceRequest", var5, "addResourcesToRequestByPhase", var6);
                } else {
                    JPO.invoke(var1, "emxResourceRequest", var5, "addResourcesToRequest", var6);
                }

            } else {
                throw new IllegalArgumentException("mlResources");
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            throw new MatrixException(var7);
        }
    }

    public void removeResources(Context var1, StringList var2) throws MatrixException {
        try {
            if (var2 != null && var2.size() != 0) {
                String[] var3 = new String[]{this.getId()};
                String[] var4 = JPO.packArgs(var2);
                JPO.invoke(var1, "emxResourceRequest", var3, "removeResources", var4);
            } else {
                throw new IllegalArgumentException("slResourceIds");
            }
        } catch (Exception var5) {
            var5.printStackTrace();
            throw new MatrixException(var5);
        }
    }

    public static void approve(Context var0, StringList var1) throws MatrixException {
        try {
            if (var0 == null) {
                throw new IllegalArgumentException("context");
            } else if (var1 != null && var1.size() != 0) {
                HashMap var2 = new HashMap();
                var2.put("ResourceRequestIds", var1);
                String[] var3 = JPO.packArgs(var2);
                JPO.invoke(var0, "emxResourceRequest", (String[])null, "approve", var3);
            } else {
                throw new IllegalArgumentException("slRequestIds");
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            throw new MatrixException(var4);
        }
    }

    public void approve(Context var1) throws MatrixException {
        approve(var1, new StringList(this.getId()));
    }

    public Map deleteRequests(Context var1, String[] var2) throws MatrixException {
        try {
            String var3 = "";
            String var4 = "";
            String var5 = "";
            StringList var6 = new StringList();
            StringList var7 = new StringList();
            StringList var8 = new StringList();
            StringList var9 = new StringList();
            var5 = DomainConstants.STATE_RESOURCE_REQUEST_COMMITTED;

            for(int var10 = 0; var10 < var2.length; ++var10) {
                StringList var11 = FrameworkUtil.splitString(var2[var10], "|");
                int var12 = var11.size();

                for(int var13 = 0; var13 < var12; ++var13) {
                    var3 = (String)var11.get(var13);
                    if (var13 == 1 && var12 > 2) {
                        DomainObject var14 = DomainObject.newInstance(var1, var3);
                        boolean var15 = false;
                        var15 = var14.isKindOf(var1, DomainConstants.TYPE_RESOURCE_REQUEST);
                        if (!var15) {
                            var9.add(var3);
                        } else {
                            var4 = var14.getInfo(var1, "current");
                            if (!var4.equals(var5)) {
                                var6.add(var11.lastElement());
                                var8.add(var3);
                            } else {
                                var7.add(var3);
                            }
                        }
                    }
                }
            }

            String[] var21 = new String[var8.size()];
            var8.copyInto(var21);
            HashMap var22 = new HashMap();
            var22.put("PersonList", var9);
            var22.put("CommittedRequest", var7);
            var22.put("NonCommittedRequest", var6);
            HashMap var23 = new HashMap();
            var23.put("ResourceRequestIds", var21);
            String[] var24 = JPO.packArgs(var23);
            if (var4.equals(DomainConstants.STATE_RESOURCE_REQUEST_REQUESTED) || var4.equals(DomainConstants.STATE_RESOURCE_REQUEST_PROPOSED)) {
                JPO.invoke(var1, "emxResourceRequest", (String[])null, "notifyDeleteRequest", var24);
            }

            ContextUtil.pushContext(var1, PropertyUtil.getSchemaProperty(var1, "person_UserAgent"), "", "");

            try {
                DomainObject.deleteObjects(var1, var21);
            } finally {
                ContextUtil.popContext(var1);
            }

            return var22;
        } catch (Exception var20) {
            var20.printStackTrace();
            throw new MatrixException(var20);
        }
    }

    public Map deleteRequestsEditAction(Context var1, String[] var2) throws MatrixException {
        try {
            String var3 = "";
            String var4 = "";
            String var5 = "";
            StringList var6 = new StringList();
            StringList var7 = new StringList();
            StringList var8 = new StringList();
            var5 = DomainConstants.STATE_RESOURCE_REQUEST_COMMITTED;
            StringTokenizer var9 = new StringTokenizer(var2[0], "|");
            var3 = var9.nextToken();
            DomainObject var10 = DomainObject.newInstance(var1, var3);
            boolean var11 = false;
            var11 = var10.isKindOf(var1, DomainConstants.TYPE_PERSON);
            if (var11) {
                var8.add(var3);
            } else {
                var4 = var10.getInfo(var1, "current");
                if (!var4.equals(var5)) {
                    var6.add(var3);
                } else {
                    var7.add(var3);
                }
            }

            String[] var12 = new String[var6.size()];
            var6.copyInto(var12);
            HashMap var13 = new HashMap();
            var13.put("PersonList", var8);
            var13.put("CommittedRequest", var7);
            var13.put("NonCommittedRequest", var6);
            HashMap var14 = new HashMap();
            var14.put("ResourceRequestIds", var12);
            String[] var15 = JPO.packArgs(var14);
            if (var4.equals(DomainConstants.STATE_RESOURCE_REQUEST_REQUESTED) || var4.equals(DomainConstants.STATE_RESOURCE_REQUEST_PROPOSED)) {
                JPO.invoke(var1, "emxResourceRequest", (String[])null, "notifyDeleteRequest", var15);
            }

            ContextUtil.pushContext(var1, PropertyUtil.getSchemaProperty(var1, "person_UserAgent"), "", "");

            try {
                DomainObject.deleteObjects(var1, var12);
            } finally {
                ContextUtil.popContext(var1);
            }

            return var13;
        } catch (Exception var20) {
            var20.printStackTrace();
            throw new MatrixException(var20);
        }
    }

    public ResourceRequest createWBSRequest(Context var1, HashMap var2) throws MatrixException {
        try {
            String var3 = (String)var2.get("projectObjectId");
            if (var1 == null) {
                throw new IllegalArgumentException("context");
            } else if (var3 != null && !"".equals(var3) && !"null".equals(var3)) {
                String[] var4 = new String[1];
                var4 = JPO.packArgs(var2);
                return (ResourceRequest)JPO.invoke(var1, "emxResourceRequest", (String[])null, "createWBSRequest", var4, ResourceRequest.class);
            } else {
                throw new IllegalArgumentException("strProjectId");
            }
        } catch (Exception var5) {
            var5.printStackTrace();
            throw new MatrixException(var5);
        }
    }

    public MapList getResourceStateRanges(Context var1) throws MatrixException {
        try {
            if (var1 == null) {
                throw new IllegalArgumentException("context");
            } else {
                String[] var2 = new String[0];
                MapList var3 = (MapList)JPO.invoke(var1, "emxResourceRequest", (String[])null, "getResourceStateRanges", var2, MapList.class);
                return var3;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            throw new MatrixException(var4);
        }
    }

    public MapList getResourceRequestStates(Context var1) throws MatrixException {
        try {
            if (var1 == null) {
                throw new IllegalArgumentException("context");
            } else {
                String[] var2 = new String[0];
                MapList var3 = (MapList)JPO.invoke(var1, "emxResourceRequest", (String[])null, "getResourceRequestStates", var2, MapList.class);
                return var3;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            throw new MatrixException(var4);
        }
    }

    public void submitRequestByPL(Context var1) throws MatrixException {
        try {
            if (var1 == null) {
                throw new IllegalArgumentException("context");
            } else {
                String[] var2 = new String[0];
                String[] var3 = new String[]{this.getId()};
                JPO.invoke(var1, "emxResourceRequest", var3, "request", var2);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            throw new MatrixException(var4);
        }
    }

    public void reject(Context var1) throws MatrixException {
        try {
            if (var1 == null) {
                throw new IllegalArgumentException("context");
            } else {
                String[] var2 = new String[0];
                String[] var3 = new String[]{this.getId()};
                String var4 = "";
                JPO.invoke(var1, "emxResourceRequest", var3, "reject", var2);
            }
        } catch (Exception var5) {
            var5.printStackTrace();
            throw new MatrixException(var5);
        }
    }

    public void commit(Context var1) throws MatrixException {
        try {
            if (var1 == null) {
                throw new IllegalArgumentException("context");
            } else {
                String[] var2 = new String[0];
                String[] var3 = new String[]{this.getId()};
                JPO.invoke(var1, "emxResourceRequest", var3, "commit", var2);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            throw new MatrixException(var4);
        }
    }

    public String removeAssignment(Context var1, String var2, String var3) throws MatrixException {
        try {
            if (var1 == null) {
                throw new IllegalArgumentException("context");
            } else {
                String[] var4 = new String[1];
                HashMap var5 = new HashMap();
                var5.put("PersonId", var2);
                var5.put("RequestId", var3);
                var4 = JPO.packArgs(var5);
                String var6 = "";
                var6 = (String)JPO.invoke(var1, "emxResourceRequest", (String[])null, "removeAssignment", var4, String.class);
                return var6;
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            throw new MatrixException(var7);
        }
    }

    public void propose(Context var1) throws MatrixException {
        try {
            if (var1 == null) {
                throw new IllegalArgumentException("context");
            } else {
                String[] var2 = new String[0];
                String[] var3 = new String[]{this.getId()};
                JPO.invoke(var1, "emxResourceRequest", var3, "propose", var2);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            throw new MatrixException(var4);
        }
    }

    public static void propose(Context var0, StringList var1) throws MatrixException {
        new StringList();

        try {
            String var3 = var0.getSession().getLanguage();
            if (var0 == null) {
                throw new IllegalArgumentException("Invalid Argument context");
            } else if (var1 != null && var1.size() > 0) {
                HashMap var7 = new HashMap();
                var7.put("ResourceRequestIds", var1);
                String[] var5 = JPO.packArgs(var7);
                StringList var2 = (StringList)JPO.invoke(var0, "emxResourceRequest", (String[])null, "propose", var5, StringList.class);
            } else {
                String var4 = EnoviaResourceBundle.getProperty(var0, "ProgramCentral", "emxProgramCentral.ResourceRequest.SelectRequest", var3);
                throw new IllegalArgumentException(var4);
            }
        } catch (Exception var6) {
            throw new MatrixException(var6);
        }
    }

    public void rejectRequest(Context var1, String var2) throws MatrixException {
        try {
            if (var1 == null) {
                throw new IllegalArgumentException("context");
            } else {
                String[] var3 = new String[0];
                String[] var4 = new String[]{this.getId()};
                JPO.invoke(var1, "emxResourceRequest", var4, "notifyRejectRequest", var3);
            }
        } catch (Exception var5) {
            var5.printStackTrace();
            throw new MatrixException(var5);
        }
    }

    public void reuseRejectedRequestByPL(Context var1) throws MatrixException {
        try {
            if (var1 == null) {
                throw new IllegalArgumentException("context");
            } else {
                String[] var2 = new String[0];
                String[] var3 = new String[]{this.getId()};
                String var4 = "";
                JPO.invoke(var1, "emxResourceRequest", var3, "reuseRejectedRequestByPL", var2);
            }
        } catch (Exception var5) {
            var5.printStackTrace();
            throw new MatrixException(var5);
        }
    }

    public ResourceRequest assignResourcePoolStandardCost(Context var1, Map var2) throws MatrixException {
        try {
            String[] var3 = new String[1];
            var3 = JPO.packArgs(var2);
            return (ResourceRequest)JPO.invoke(var1, "emxResourceRequest", (String[])null, "assignResourcePoolStandardCost", var3, ResourceRequest.class);
        } catch (Exception var4) {
            throw new MatrixException(var4);
        }
    }

    public Map calculateFTE(Context var1, Date var2, Date var3, String var4) throws MatrixException {
        try {
            String var5 = EnoviaResourceBundle.getProperty(var1, "emxProgramCentral.ResourcePlan.NumberofPeopleUnit");
            MapList var6 = null;
            FTE var7 = FTE.getInstance(var1);
            if (null != var2 && null != var3) {
                var6 = var7.getTimeframes(var2, var3);
            }

            HashMap var8 = new HashMap();
            new HashMap();
            int var10 = var6.size();
            int var11 = 0;
            long var12 = 0L;
            long var14 = 0L;
            Calendar var16 = Calendar.getInstance();
            var16.setTime(var2);
            Calendar var17 = Calendar.getInstance();
            var17.setTime(var3);
            String var18 = var7.getTimeFrame(var2);
            String var19 = var7.getTimeFrame(var3);
            StringList var20 = FrameworkUtil.split(var18, "-");
            StringList var21 = FrameworkUtil.split(var19, "-");
            if ("Hours".equalsIgnoreCase(var5)) {
                var14 = ProgramCentralUtil.computeDuration(var2, var3);
            }

            for(Iterator var22 = var6.iterator(); var22.hasNext(); ++var11) {
                Map var9 = (Map)var22.next();
                int var23 = (Integer)var9.get("timeframe");
                int var24 = (Integer)var9.get("year");
                if (var11 == 0) {
                    if (Integer.parseInt(((String)var20.get(0)).toString()) == Integer.parseInt(((String)var21.get(0)).toString()) && Integer.parseInt(((String)var20.get(1)).toString()) == Integer.parseInt(((String)var21.get(1)).toString())) {
                        var12 = ProgramCentralUtil.computeDuration(var2, var3);
                    } else {
                        var12 = var7.getNumberofDaysSinceStartTimeFrame(var2);
                    }
                } else if (var11 == var10 - 1) {
                    var12 = var7.getNumberofDaysTillEndTimeFrame(var3);
                } else {
                    Date var25 = var7.getStartDate(var23 + "-" + var24);
                    Date var26 = var7.getEndDate(var23 + "-" + var24);
                    var12 = ProgramCentralUtil.computeDuration(var25, var26);
                }

                double var30 = 0.0;
                if ("Hours".equalsIgnoreCase(var5)) {
                    var30 = (double)var12 * Task.parseToDouble(var4) / (double)var14;
                } else {
                    Date var27 = var7.getStartDate(var23 + "-" + var24);
                    Date var28 = var7.getEndDate(var23 + "-" + var24);
                    var14 = ProgramCentralUtil.computeDuration(var27, var28);
                    var30 = (double)var12 * Task.parseToDouble(var4) / (double)var14;
                }

                String var31 = var23 + "-" + var24;
                var8.put(var31, var30);
            }

            return var8;
        } catch (Exception var29) {
            throw new MatrixException(var29);
        }
    }

    public double calculateTotalPhaseCost(Context var1, Date var2, Date var3, Map var4, Map var5, double var6) throws MatrixException {
        FTE var8 = FTE.getInstance(var1);
        DecimalFormat var9 = new DecimalFormat();
        var9.setMaximumFractionDigits(2);
        var9.setGroupingUsed(false);
        String var10 = EnoviaResourceBundle.getProperty(var1, "emxProgramCentral.ResourcePlan.NumberofPeopleUnit");
        Date var11 = eMatrixDateFormat.getJavaDate((String)var4.get(SELECT_ATTRIBUTE_START_DATE));
        Date var12 = eMatrixDateFormat.getJavaDate((String)var4.get(SELECT_ATTRIBUTE_END_DATE));
        String var13 = var8.getTimeFrame(var11);
        String var14 = var8.getTimeFrame(var12);
        StringList var15 = FrameworkUtil.split(var13, "-");
        StringList var16 = FrameworkUtil.split(var14, "-");
        var2 = getNormalizedDate(var2);
        var3 = getNormalizedDate(var3);
        String var17 = var8.getTimeFrame(var2);
        String var18 = var8.getTimeFrame(var3);
        StringList var19 = FrameworkUtil.split(var17, "-");
        StringList var20 = FrameworkUtil.split(var18, "-");
        int var21 = Integer.parseInt(((String)var19.get(0)).toString());
        int var22 = Integer.parseInt(((String)var19.get(1)).toString());
        int var23 = Integer.parseInt(((String)var20.get(0)).toString());
        int var24 = Integer.parseInt(((String)var20.get(1)).toString());
        Map var25 = this.calculateFTE(var1, var2, var3, "1");
        double var26 = 0.0;

        double var29;
        for(Iterator var28 = var25.keySet().iterator(); var28.hasNext(); var26 += var29) {
            var29 = 0.0;
            double var31 = 0.0;
            String var33 = (String)var28.next();
            StringList var34 = FrameworkUtil.split(var33, "-");
            Date var35 = getNormalizedDate(var8.getStartDate(var33));
            Date var36 = getNormalizedDate(var8.getEndDate(var33));
            int var37 = Integer.parseInt(((String)var34.get(0)).toString());
            int var38 = Integer.parseInt(((String)var34.get(1)).toString());
            Date var39 = var8.getStartDate(var33);
            if (var37 == var21 && var38 == var22) {
                var39 = var35;
            }

            Date var40 = var8.getEndDate(var33);
            if (var37 == var23 && var38 == var24) {
                var40 = var36;
            }

            if (var39.before(getNormalizedDate(var11))) {
                var39 = var11;
            }

            if (var40.after(getNormalizedDate(var12))) {
                var40 = var12;
            }

            var39 = getNormalizedDate(var39);
            var40 = getNormalizedDate(var40);
            long var42 = ProgramCentralUtil.computeDuration(var39, var40);
            if (var5.containsKey(var33)) {
                var31 = (Double)var5.get(var33);
            }

            if ("Hours".equalsIgnoreCase(var10)) {
                var29 = var31 * var6;
            } else {
                var29 = var31 * 8.0 * (double)var42 * var6;
            }

            var29 = Task.parseToDouble(var9.format(var29));
        }

        return var26;
    }

    public double calculateTotalPhaseCost(Context var1, Date var2, Date var3, Map var4, Map var5, String var6, double var7) throws MatrixException {
        DecimalFormat var9 = new DecimalFormat();
        var9.setMaximumFractionDigits(2);
        var9.setGroupingUsed(false);
        Date var10 = var2;
        Date var11 = var3;
        String var12 = EnoviaResourceBundle.getProperty(var1, "emxProgramCentral.ResourcePlan.NumberofPeopleUnit");
        double var13 = 0.0;
        double var15 = 0.0;
        double var17 = 0.0;
        FTE var19 = FTE.getInstance(var1);

        for(Iterator var20 = var4.keySet().iterator(); var20.hasNext(); var17 += var13) {
            String var21 = (String)var20.next();
            Date var22 = getNormalizedDate(var19.getStartDate(var21));
            Date var23 = getNormalizedDate(var19.getEndDate(var21));
            Date var24 = var22;
            Date var25 = var23;
            if (var10.after(var22)) {
                var24 = var10;
            }

            if (var11.before(var23)) {
                var25 = var11;
            }

            var24 = getNormalizedDate(var24);
            var25 = getNormalizedDate(var25);
            Map var26 = this.calculateFTE(var1, var24, var25, String.valueOf((Double)var5.get(var21)));
            long var27 = ProgramCentralUtil.computeDuration(var22, var23);
            var15 = (Double)var26.get(var21);
            if ("Hours".equalsIgnoreCase(var12)) {
                var13 = var15 * var7;
            } else {
                var13 = var15 * 8.0 * (double)var27 * var7;
            }

            var13 = Task.parseToDouble(var9.format(var13));
        }

        return var17;
    }

    public double calculateTotalPhaseFTE(Context var1, Date var2, Date var3, String var4) throws MatrixException {
        Map var5 = this.calculateFTE(var1, var2, var3, var4);
        double var6 = 0.0;

        String var9;
        for(Iterator var8 = var5.keySet().iterator(); var8.hasNext(); var6 += (Double)var5.get(var9)) {
            var9 = (String)var8.next();
        }

        return var6;
    }

    public static StringList tableValidateCurrency(Context var0, MapList var1) throws Exception {
        i18nNow var2 = new i18nNow();
        String var3 = var2.GetString("emxProgramCentralStringResource", var0.getSession().getLanguage(), "emxProgramCentral.CurrencyConversionForResourceRequest.NoExchangeRateAssuming1Rate");
        Map var4 = null;
        String var5 = "";
        String var6 = "";
        StringList var7 = new StringList();
        PersonUtil var8 = new PersonUtil();
        String var9 = PersonUtil.getUserCompanyId(var0);
        Map var10 = ProgramCentralUtil.getCurrencyConversionMap(var0, var9);
        String var11 = PersonUtil.getCurrency(var0);
        Iterator var12 = var1.iterator();

        while(var12.hasNext()) {
            var4 = (Map)var12.next();
            var5 = (String)var4.get("name");
            var6 = (String)var4.get("attribute[Standard Cost].inputunit");
            if (!var6.equalsIgnoreCase(var11) && var10.get(var6 + "," + var11) == null) {
                var7.add(var5);
            }
        }

        if (!var7.isEmpty()) {
            String var13 = "" + var3 + var7.toString();
            MqlUtil.mqlCommand(var0, "notice $1", new String[]{"\"" + var13 + "\" "});
        }

        return var7;
    }

    public void updateResourceRequestStartDate(Context var1, String var2, String var3, String var4) throws MatrixException {
        try {
            String[] var5 = new String[1];
            HashMap var6 = new HashMap();
            var6.put("phaseId", var2);
            var6.put("phaseStartDate", var3);
            var6.put("phaseEndDate", var4);
            var5 = JPO.packArgs(var6);
            String var7 = "";
            var7 = (String)JPO.invoke(var1, "emxResourceRequest", (String[])null, "updateResourceRequestStartDate", var5, String.class);
        } catch (Exception var8) {
            var8.printStackTrace();
            throw new MatrixException(var8);
        }
    }

    public static Date getNormalizedDate(Date var0) {
        Date var1 = null;
        Calendar var2 = Calendar.getInstance();
        var2.setTime(var0);
        var2.set(11, 0);
        var2.set(12, 0);
        var2.set(13, 0);
        var2.set(14, 0);
        var1 = var2.getTime();
        return var1;
    }

    public static String triggerCheckResourcePoolMessage(Context var0, String[] var1) throws Exception {
        String var2 = var1[0];
        String var3 = "from[" + RELATIONSHIP_RESOURCE_POOL + "].to.id";
        String var4 = "to[" + RELATIONSHIP_RESOURCE_PLAN + "].from.id";
        String var5 = "to[" + RELATIONSHIP_RESOURCE_PLAN + "].from.current";
        String var6 = "to[" + ResourcePlanTemplate.RELATIONSHIP_RESOURCE_REQUEST_PLAN_TEMPLATE + "].fromrel[" + ResourcePlanTemplate.RELATIONSHIP_RESOURCE_PLAN_TEMPLATE + "].from." + "id";
        StringList var7 = new StringList(4);
        var7.add("name");
        var7.add(var3);
        var7.add(var4);
        var7.add(var5);
        var7.add(var6);
        DomainObject var8 = DomainObject.newInstance(var0, var2);
        Map var9 = var8.getInfo(var0, var7);
        String var10 = (String)var9.get(var3);
        String var11 = (String)var9.get(var4);
        String var12 = (String)var9.get(var5);
        String var13 = (String)var9.get(var6);
        String var14 = null;
        if (STATE_PROJECT_SPACE_COMPLETE.equalsIgnoreCase(var12)) {
            var14 = "emxProgramCentral.Alert.ResourceRequest.ProjectCompleted";
            var14 = ProgramCentralUtil.getMessage(var0, var14, (String[])null, (String[])null, (String)null);
        }

        if (ProgramCentralUtil.isNotNullString(var13)) {
            var14 = "emxProgramCentral.Alert.ResourceRequest.ProjectTemplate";
            var14 = ProgramCentralUtil.getMessage(var0, var14, (String[])null, (String[])null, (String)null);
        } else if (ProgramCentralUtil.isNullString(var10)) {
            var14 = "emxProgramCentral.Alert.ResourceRequest.NoResourcePool";
            String[] var15 = new String[]{"ResourceRequest"};
            String var16 = (String)var9.get("name");
            String[] var17 = new String[]{var16};
            var14 = ProgramCentralUtil.getMessage(var0, var14, var15, var17, (String)null);
        }

        return var14;
    }

    public static MapList getPhaseList(Context var0, String var1) throws FrameworkException {
        DomainObject var2 = DomainObject.newInstance(var0, var1);
        StringList var3 = new StringList();
        var3.add("id");
        var3.add("name");
        var3.add(ATTRIBUTE_PHASE_START_DATE);
        var3.add(ATTRIBUTE_PHASE_FINISH_DATE);
        MapList var4 = ResourcePlanTemplate.getPhasesForResourceRequestView(var0, var2, var3);
        return var4;
    }

    public void createResourcePlan(Context var1, String[] var2) throws MatrixException {
        try {
            ContextUtil.startTransaction(var1, true);
            Map var3 = (Map)JPO.unpackArgs(var2);
            Map var4 = (Map)var3.get("requestMap");
            Map var5 = (Map)var3.get("paramMap");
            String var6 = (String)var5.get("newObjectId");
            this.processCreateRequestByTimeline(var1, var4, var6);
            ContextUtil.commitTransaction(var1);
        } catch (Exception var7) {
            ContextUtil.abortTransaction(var1);
            throw new MatrixException(var7);
        }
    }

    private void processCreateRequestByTimeline(Context var1, Map var2, String var3) throws FrameworkException, Exception, MatrixException {
        ContextUtil.startTransaction(var1, true);
        String var4 = "attribute[" + ATTRIBUTE_TASK_ESTIMATED_START_DATE + "]";
        String var5 = "attribute[" + ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE + "]";
        String var6 = (String)var2.get("timeZone");
        double var7 = Task.parseToDouble(var6);
        Locale var9 = ProgramCentralUtil.getLocale(var1);
        String var10 = var9.getLanguage();
        String var11 = (String)var2.get("objectId");
        String var12 = null != (String)var2.get("ProjectRole") ? (String)var2.get("ProjectRole") : "";
        String var13 = (String)var2.get("ResourcePoolDisplay");
        String var14 = (String)var2.get("PreferredPersonDisplay");
        String var15 = (String)var2.get("Business SkillDisplay");
        String var16 = (String)var2.get("Name");
        String var17 = (String)var2.get("Business Skill");
        String var18 = null;
        boolean var19 = ProgramCentralUtil.isNotNullString(var13);
        boolean var20 = ProgramCentralUtil.isNotNullString(var14);
        boolean var21 = ProgramCentralUtil.isNotNullString(var15);
        String[] var22 = null;
        DomainObject var23 = null;
        String[] var24 = null;
        if (var21) {
            var24 = var17.split(",");
        }

        String var25;
        if (var19) {
            var25 = (String)var2.get("ResourcePoolOID");
            var23 = DomainObject.newInstance(var1, var25);
        }

        if (!var19 && var20) {
            var25 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ResourceRequest.NoResourcePool.PreferredPerson", var10);
            throw new Exception(var25);
        } else {
            if (var20) {
                var18 = (String)var2.get("PreferredPersonOID");
                if (ProgramCentralUtil.isNullString(var18)) {
                    var18 = (String)var2.get("PreferredPerson");
                }

                var22 = var18.split(",");
            }

            this.validateResourcePoolPeople(var1, var13, var18, var23);
            DomainObject var37 = DomainObject.newInstance(var1, var11);
            StringList var26 = new StringList();
            var26.add(var4);
            var26.add(var5);
            String var27 = null;
            String var28 = null;
            Map var29 = var37.getInfo(var1, var26);
            var27 = (String)var29.get(var4);
            var28 = (String)var29.get(var5);
            String var30 = (String)var2.get("RequestStartDate");
            String var31 = (String)var2.get("RequestEndDate");
            var30 = eMatrixDateFormat.getFormattedInputDate(var1, var30, var7, var9);
            var31 = eMatrixDateFormat.getFormattedInputDate(var1, var31, var7, var9);
            this.validateRequestDates(var1, var27, var28, var30, var31);
            String var32 = (String)var2.get("NumberOfPeople");
            var32 = this.validateNumberOfPeople(var1, var10, var32);
            String var33 = (String)var2.get("StandardCost");
            BigDecimal var34 = validateStandardCost(var1, var10, var9, var33);
            FTE var35 = FTE.getInstance(var1);
            FTE var36 = FTE.getInstance(var1);
            this.processFTE(var1, var30, var31, var32, var35, var36);
            processResourceRequest(var1, var12, var24, var3, var22, var23, var30, var31, var37, var35, var36, var34);
            ContextUtil.commitTransaction(var1);
        }
    }

    public Map createAutomatedResourceRequest(Context var1, String[] var2) throws MatrixException {
        try {
            ContextUtil.startTransaction(var1, true);
            String var3 = var2[0];
            HashMap var4 = new HashMap();
            HashMap var5 = new HashMap();
            var4.put("objectId", var3);
            DomainObject var6 = DomainObject.newInstance(var1, var3);
            String var7 = FrameworkUtil.autoName(var1, "type_ResourceRequest", "policy_ResourceRequest");
            TimeZone var8 = TimeZone.getTimeZone(var1.getSession().getTimezone());
            double var9 = -1.0 * (double)var8.getRawOffset();
            double var11 = new Double(var9 / 3600000.0);
            var4.put("timeZone", var11 + "");
            Locale var13 = ProgramCentralUtil.getLocale(var1);
            String var14 = var6.getAttributeValue(var1, ATTRIBUTE_RESOURCE_PLAN_PREFRENCE);
            DomainObject var15;
            if (null != var14) {
                if ("Timeline".equals(var14)) {
                    var15 = DomainObject.newInstance(var1, var3);
                    StringList var16 = new StringList();
                    var16.add("attribute[" + ATTRIBUTE_TASK_ESTIMATED_START_DATE + "]");
                    var16.add("attribute[" + ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE + "]");
                    Map var17 = var15.getInfo(var1, var16);
                    String var18 = (String)var17.get("attribute[" + ATTRIBUTE_TASK_ESTIMATED_START_DATE + "]");
                    String var19 = (String)var17.get("attribute[" + ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE + "]");
                    var18 = eMatrixDateFormat.getFormattedDisplayDate(var18, var11, var13);
                    var19 = eMatrixDateFormat.getFormattedDisplayDate(var19, var11, var13);
                    var4.put("RequestStartDate", var18);
                    var4.put("RequestEndDate", var19);
                    this.processCreateRequestByTimeline(var1, var4, var7);
                } else if ("Phase".equals(var14)) {
                    Map var21 = ResourcePlanTemplate.getProjectPhaseInfo(var1, var3, false);
                    Iterator var22 = var21.keySet().iterator();

                    while(var22.hasNext()) {
                        String var23 = (String)var22.next();
                        var4.put("PhaseOID-" + var23, "0");
                    }

                    this.processCreateRequestByPhase(var1, var4, var21, var7);
                }
            }

            ContextUtil.commitTransaction(var1);
            var15 = DomainObject.newInstance(var1, var7);
            var5.put("id", var7);
            var5.put("planRelId", var15.getInfo(var1, "to[Resource Plan].id"));
            return var5;
        } catch (Exception var20) {
            ContextUtil.abortTransaction(var1);
            throw new MatrixException(var20);
        }
    }

    public static void processResourceRequest(Context var0, String var1, String[] var2, String var3, String[] var4, DomainObject var5, String var6, String var7, DomainObject var8, FTE var9, FTE var10, BigDecimal var11) throws FrameworkException, MatrixException, Exception {
        DomainObject var12 = null;
        String var13 = Currency.getBaseCurrency(var0, var8.getId(var0));
        double var14 = Currency.toBaseCurrency(var0, var8.getId(var0), var11.doubleValue(), false);
        HashMap var16 = new HashMap();
        var16.put(ProgramCentralConstants.ATTRIBUTE_PROJECT_ROLE, var1);
        var16.put(ProgramCentralConstants.ATTRIBUTE_START_DATE, var6);
        var16.put(ProgramCentralConstants.ATTRIBUTE_END_DATE, var7);
        var16.put(ATTRIBUTE_STANDARD_COST, var14 + " " + var13);
        var12 = DomainObject.newInstance(var0, var3);
        var12.setAttributeValues(var0, var16);
        String var17 = var9.getXML();
        DomainRelationship var18 = DomainRelationship.connect(var0, var8, RELATIONSHIP_RESOURCE_PLAN, var12);
        var18.setAttributeValue(var0, ATTRIBUTE_FTE, var17);
        String var20;
        if (null != var2) {
            for(int var19 = 0; var19 < var2.length; ++var19) {
                var20 = var2[var19];
                DomainObject var21 = DomainObject.newInstance(var0, var20);
                DomainRelationship.connect(var0, var12, RELATIONSHIP_RESOURCE_REQUEST_SKILL, var21);
            }
        }

        String var26;
        if (null != var5) {
            var26 = PropertyUtil.getSchemaProperty(var0, "relationship_ResourcePool");
            DomainRelationship.connect(var0, var12, var26, var5);
        }

        if (null != var4) {
            var26 = PropertyUtil.getSchemaProperty(var0, "relationship_Allocated");
            var20 = var10.getXML();

            for(int var25 = 0; var25 < var4.length; ++var25) {
                String var22 = var4[var25];
                DomainObject var23 = DomainObject.newInstance(var0, var22);
                DomainRelationship var24 = DomainRelationship.connect(var0, var12, var26, var23);
                var24.setAttributeValue(var0, ATTRIBUTE_FTE, var20);
            }
        }

    }

    private void validateResourcePoolPeople(Context var1, String var2, String var3, DomainObject var4) throws FrameworkException, Exception {
        this.checkPersonForSelectedPool(var1, var2, var4, var3);
    }

    private void processFTE(Context var1, String var2, String var3, String var4, FTE var5, FTE var6) throws Exception, MatrixException {
        boolean var7 = false;
        boolean var8 = false;
        Date var9 = eMatrixDateFormat.getJavaDate(var2);
        Date var10 = eMatrixDateFormat.getJavaDate(var3);
        var9 = getNormalizedDate(var9);
        var10 = getNormalizedDate(var10);
        MapList var11 = new MapList();
        ResourceRequest var12 = new ResourceRequest();
        Map var13 = var12.calculateFTE(var1, var9, var10, var4);
        var11.add(var13);
        if (var11 != null) {
            Map var14 = (Map)var11.get(0);
            String var15 = "";
            double var16 = 0.0;
            double var18 = 1.0;
            String[] var20 = null;

            int var22;
            int var23;
            for(Iterator var21 = var14.keySet().iterator(); var21.hasNext(); var6.setFTE(var23, var22, var18)) {
                var18 = 1.0;
                var15 = (String)var21.next();
                var16 = (Double)var13.get(var15);
                var20 = var15.split("-");
                var22 = Integer.parseInt(var20[0]);
                var23 = Integer.parseInt(var20[1]);
                var5.setFTE(var23, var22, var16);
                if (var16 < var18) {
                    var18 = var16;
                }
            }
        }

    }

    public static BigDecimal validateStandardCost(Context var0, String var1, Locale var2, String var3) throws MatrixException, Exception {
        BigDecimal var4 = new BigDecimal(0);
        if (ProgramCentralUtil.isNotNullString(var3)) {
            var4 = ProgramCentralUtil.getNormalizedCurrencyValue(var0, var2, var3.trim());
            if (var4.doubleValue() < 0.0) {
                String var5 = EnoviaResourceBundle.getProperty(var0, "ProgramCentral", "emxProgramCentral.ResourceRequest.CostMustBeGreaterThanZero", var1);
                throw new MatrixException(var5);
            }
        }

        return var4;
    }

    private String validateNumberOfPeople(Context var1, String var2, String var3) throws Exception {
        double var4 = 0.0;

        try {
            if (ProgramCentralUtil.isNullString(var3)) {
                var3 = "0";
            }

            var4 = Task.parseToDouble(var3);
        } catch (NumberFormatException var8) {
            String var7 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ResourceRequest.SelectNumericFTE", var2);
            throw new Exception(var7);
        }

        if (var4 < 0.0) {
            String var6 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ResourceRequest.RequestFTEGreaterThanZero", var2);
            throw new Exception(var6);
        } else {
            return var3;
        }
    }

    private void validateRequestDates(Context var1, String var2, String var3, String var4, String var5) throws Exception, MatrixException {
        String var6 = ProgramCentralUtil.getLocale(var1).getLanguage();
        Date var7 = eMatrixDateFormat.getJavaDate(var2);
        Date var8 = eMatrixDateFormat.getJavaDate(var3);
        Calendar var9 = Calendar.getInstance();
        Calendar var10 = Calendar.getInstance();
        var9.setTime(var7);
        var10.setTime(var8);
        var9.set(10, 0);
        var9.set(11, 0);
        var9.set(12, 0);
        var9.set(13, 0);
        var10.set(10, 0);
        var10.set(11, 0);
        var10.set(12, 0);
        var10.set(13, 0);
        Date var11 = eMatrixDateFormat.getJavaDate(var4);
        Date var12 = eMatrixDateFormat.getJavaDate(var5);
        Calendar var13 = Calendar.getInstance();
        Calendar var14 = Calendar.getInstance();
        var13.setTime(var11);
        var14.setTime(var12);
        var13.set(10, 0);
        var13.set(11, 0);
        var13.set(12, 0);
        var13.set(13, 0);
        var14.set(10, 0);
        var14.set(11, 0);
        var14.set(12, 0);
        var14.set(13, 0);
        int var15 = var13.get(2) + 1;
        int var16 = var13.get(1);
        int var17 = var14.get(2) + 1;
        int var18 = var14.get(1);
        String var19;
        if (var12.before(var11)) {
            var6 = var1.getSession().getLanguage();
            if (var16 <= var18 && (var16 != var18 || var15 <= var17)) {
                var19 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ResourceRequest.RequestEndDateGreaterThanRequestStartDate", var6);
                throw new MatrixException(var19);
            } else {
                var19 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ResourceRequest.RequestStartDateGreaterThanRequestEndDate", var6);
                throw new Exception(var19);
            }
        } else if (var13.before(var9) && var14.before(var9) || var13.after(var10) && var14.after(var10)) {
            var6 = var1.getSession().getLanguage();
            var19 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ResourceRequest.RequestStartDateOrEndDateShouldBeWithinProjectPeriod", var6);
            throw new MatrixException(var19);
        }
    }

    public void createResourcePlanByPhase(Context var1, String[] var2) throws Exception {
        Object var3 = null;
        boolean var4 = false;

        try {
            Map var5 = (Map)JPO.unpackArgs(var2);
            Map var6 = (Map)var5.get("requestMap");
            Map var7 = (Map)var5.get("paramMap");
            String var8 = (String)var7.get("newObjectId");
            String var9 = (String)var6.get("objectId");
            Map var10 = ResourcePlanTemplate.getProjectPhaseInfo(var1, var9, false);
            this.processCreateRequestByPhase(var1, var6, var10, var8);
        } catch (Exception var11) {
            var11.printStackTrace();
            throw new MatrixException(var11);
        }
    }

    private void processCreateRequestByPhase(Context var1, Map var2, Map var3, String var4) throws FrameworkException, Exception, MatrixException {
        Locale var5 = ProgramCentralUtil.getLocale(var1);
        String var6 = var5.getLanguage();
        String var7 = EnoviaResourceBundle.getProperty(var1, "emxProgramCentral.ResourcePlan.NumberofPeopleUnit");
        String var8 = (String)var2.get("objectId");
        String var9 = (String)var2.get("ProjectRole");
        String var10 = (String)var2.get("ResourcePoolDisplay");
        String var11 = null;
        DomainObject var12 = DomainObject.newInstance(var1, var8);
        NumberFormat var13 = ProgramCentralUtil.getNumberFormatInstance(2, false);
        String var14 = (String)var2.get("PreferredPersonDisplay");
        String var15 = (String)var2.get("Business SkillDisplay");
        String var16 = (String)var2.get("Business Skill");
        String var17 = null;
        DomainObject var18 = null;
        boolean var19 = ProgramCentralUtil.isNotNullString(var10);
        boolean var20 = ProgramCentralUtil.isNotNullString(var14);
        boolean var21 = ProgramCentralUtil.isNotNullString(var15);
        String[] var22 = null;
        if (var21) {
            var22 = var16.split(",");
        }

        if (var19) {
            var11 = (String)var2.get("ResourcePoolOID");
            var19 = true;
            var18 = DomainObject.newInstance(var1, var11);
        }

        String var23;
        if (!var19 && var20) {
            var23 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ResourceRequest.NoResourcePool.PreferredPerson", var6);
            throw new Exception(var23);
        } else {
            if (var20) {
                var17 = (String)var2.get("PreferredPersonOID");
                if (ProgramCentralUtil.isNullString(var17)) {
                    var17 = (String)var2.get("PreferredPerson");
                }
            }

            this.checkPersonForSelectedPool(var1, var10, var18, var17);
            var23 = (String)var2.get("Name");
            new StringList();
            String var25 = (String)var2.get("timeZone");
            Map var26 = this.checkAndExtractFTEForPhase(var2, var6);
            String var27 = (String)var2.get("StandardCost");
            BigDecimal var28 = validateStandardCost(var1, var6, var5, var27);
            Date var29 = null;
            Date var30 = null;
            SimpleDateFormat var31 = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);
            FTE var32 = FTE.getInstance(var1);
            DomainObject var33 = DomainObject.newInstance(var1, var4);
            StringList var34 = new StringList();
            String[] var35 = null;
            if (var17 != null && !"".equals(var17) && !"null".equals(var17)) {
                var34 = FrameworkUtil.split(var17, ",");
                var35 = var17.split(",");
            }

            new HashMap();
            Iterator var37 = var26.keySet().iterator();

            while(true) {
                Date var43;
                do {
                    String var38;
                    if (!var37.hasNext()) {
                        Calendar var63 = Calendar.getInstance();
                        var63.setTime(var30);
                        var63.set(11, 17);
                        var63.set(12, 0);
                        var63.set(13, 0);
                        var63.set(14, 0);
                        var30 = var63.getTime();
                        var63 = Calendar.getInstance();
                        var63.setTime(var29);
                        var63.set(11, 8);
                        var63.set(12, 0);
                        var63.set(13, 0);
                        var63.set(14, 0);
                        var29 = var63.getTime();
                        var38 = var31.format(var29);
                        String var64 = var31.format(var30);
                        HashMap var65 = new HashMap();
                        FTE var66 = FTE.getInstance(var1);
                        if (ProgramCentralUtil.isNotNullString(var17)) {
                            Map var67 = var32.getAllFTE();
                            double var68 = 0.0;
                            Iterator var69 = var67.keySet().iterator();

                            while(var69.hasNext()) {
                                String var70 = (String)var69.next();
                                Double var71 = (Double)var67.get(var70);
                                var68 = var71 / (double)var35.length;
                                var65.put(var70, new Double(var68));
                            }

                            var66.setAllFTE(var67);
                        }

                        processResourceRequest(var1, var9, var22, var4, var35, var18, var38, var64, var12, var32, var66, var28);
                        return;
                    }

                    var38 = (String)var37.next();
                    FTE var39 = FTE.getInstance(var1);
                    Double var40 = (Double)var26.get(var38);
                    Map var41 = (Map)var3.get(var38);
                    Date var42 = (Date)var41.get(ResourcePlanTemplate.ATTRIBUTE_PHASE_START_DATE);
                    var43 = (Date)var41.get(ResourcePlanTemplate.ATTRIBUTE_PHASE_FINISH_DATE);
                    DomainObject var44 = DomainObject.newInstance(var1, var38);
                    String var45 = PropertyUtil.getSchemaProperty(var1, "relationship_PhaseFTE");
                    DomainRelationship var46 = DomainRelationship.connect(var1, var33, var45, var44);
                    String var47 = var46.getName();
                    double var48 = var40;
                    ResourceRequest var50 = new ResourceRequest();
                    Map var51 = var50.calculateFTE(var1, var42, var43, var48 + "");
                    var39.setAllFTE(var51);
                    var32 = ResourcePlanTemplate.getCalculatedFTEMap(var39, var32);
                    if (var34.size() > 0) {
                        for(int var52 = 0; var52 < var34.size(); ++var52) {
                            double var53 = 0.0;
                            String var55 = (String)var34.get(var52);
                            String var56 = connectPersonToPhaseFTERel(var1, var55, var47);
                            DomainRelationship var57 = DomainRelationship.newInstance(var1, var56);
                            double var58 = 1.0;
                            var58 = var48 / (double)var34.size();
                            if (!"Hours".equalsIgnoreCase(var7) && var58 > 1.0) {
                                var58 = 1.0;
                            }

                            ContextUtil.pushContext(var1, PropertyUtil.getSchemaProperty(var1, "person_UserAgent"), "", "");

                            try {
                                var57.setAttributeValue(var1, ATTRIBUTE_FTE, String.valueOf(var58));
                            } finally {
                                ContextUtil.popContext(var1);
                            }
                        }
                    }

                    var46.setAttributeValue(var1, ATTRIBUTE_FTE, String.valueOf(var48));
                    if (null == var29 || null != var29 && var29.after(var42)) {
                        var29 = var42;
                    }
                } while(null != var30 && (null == var30 || !var30.before(var43)));

                var30 = var43;
            }
        }
    }

    public static String connectPersonToPhaseFTERel(Context var0, String var1, String var2) throws FrameworkException {
        MqlUtil.mqlCommand(var0, "verb on");
        String var3 = "add connection $1 from $2 torel $3";
        String var4 = MqlUtil.mqlCommand(var0, var3, new String[]{ResourcePlanTemplate.RELATIONSHIP_PERSON_PHASE_FTE, var1, var2});
        int var5 = var4.indexOf("'");
        int var6 = var4.lastIndexOf("'");
        String var7 = var4.substring(var5 + 1, var6);
        MqlUtil.mqlCommand(var0, "verb off");
        return var7;
    }

    private Map checkAndExtractFTEForPhase(Map var1, String var2) throws MatrixException {
        try {
            boolean var3 = false;
            HashMap var4 = new HashMap();
            Iterator var5 = var1.keySet().iterator();

            while(var5.hasNext()) {
                String var6 = (String)var5.next();
                if (var6.contains("PhaseOID-")) {
                    double var7 = 0.0;
                    String var9 = var6.substring(var6.indexOf("-") + 1, var6.length());
                    String var10 = (String)var1.get(var6);
                    if (ProgramCentralUtil.isNullString(var10)) {
                        var10 = "0";
                    }

                    try {
                        var7 = Task.parseToDouble(var10);
                    } catch (NumberFormatException var12) {
                    }

                    var4.put(var9, var7);
                }
            }

            return var4;
        } catch (Exception var13) {
            var13.printStackTrace();
            throw new MatrixException(var13);
        }
    }

    private void checkPersonForSelectedPool(Context var1, String var2, DomainObject var3, String var4) throws FrameworkException, Exception {
        new StringList();
        StringList var7 = new StringList();
        boolean var8 = false;
        if (var4 != null && !"".equals(var4) && !"null".equals(var4)) {
            StringList var6 = var3.getInfoList(var1, "from[" + DomainConstants.RELATIONSHIP_MEMBER + "].to.id");
            String[] var9 = var4.split(",");

            for(int var10 = 0; var10 < var9.length; ++var10) {
                String var11 = var9[var10];
                DomainObject var12 = DomainObject.newInstance(var1, var11);
                if (!var6.contains(var11)) {
                    String var13 = var12.getInfo(var1, "name");
                    var7.add(var13);
                    var8 = true;
                }
            }

            if (var8) {
                String var5 = var1.getSession().getLanguage();
                String var14 = EnoviaResourceBundle.getProperty(var1, "ProgramCentral", "emxProgramCentral.ResourceRequest.DoesNotBelongToResourcePool", var5);
                var14 = var14 + " " + var2 + ":";
                var14 = var14 + var7.toString();
                throw new Exception(var14);
            }
        }

    }

    public Map getOrganizationData(Context var1) throws FrameworkException {
        HashMap var2 = null;
        Object var11 = null;
        StringList var12 = new StringList();
        StringList var13 = new StringList();
        StringList var14 = new StringList();
        StringList var15 = new StringList();
        StringList var16 = new StringList();
        StringList var17 = new StringList();
        StringList var18 = new StringList();
        StringList var19 = new StringList();
        boolean var20 = false;
        String var3;
        String var4;
        String var5;
        String var6;
        String var7;
        String var8;
        String var9;
        String var10;
        if (this.isKindOf(var1, TYPE_PERSON)) {
            var20 = true;
            var3 = "to[" + RELATIONSHIP_MEMBER + "].from.type.kindof[" + TYPE_BUSINESS_UNIT + "]";
            var4 = "to[" + RELATIONSHIP_MEMBER + "].from.type.kindof[" + TYPE_DEPARTMENT + "]";
            var5 = "to[" + RELATIONSHIP_MEMBER + "].from.type.kindof[" + TYPE_COMPANY + "]";
            var6 = "to[" + RELATIONSHIP_MEMBER + "].from.type";
            var7 = "from[" + RELATIONSHIP_RESOURCE_POOL + "].to.type.kindof";
            var8 = "to[" + RELATIONSHIP_MEMBER + "].from.name";
            var9 = "to[" + RELATIONSHIP_MEMBER + "].from.id";
            var10 = "to[" + RELATIONSHIP_MEMBER + "].from.attribute[" + ATTRIBUTE_TITLE + "]";
        } else {
            var20 = true;
            var3 = "from[" + RELATIONSHIP_RESOURCE_POOL + "].to.type.kindof[" + TYPE_BUSINESS_UNIT + "]";
            var4 = "from[" + RELATIONSHIP_RESOURCE_POOL + "].to.type.kindof[" + TYPE_DEPARTMENT + "]";
            var5 = "from[" + RELATIONSHIP_RESOURCE_POOL + "].to.type.kindof[" + TYPE_COMPANY + "]";
            var6 = "from[" + RELATIONSHIP_RESOURCE_POOL + "].to.type";
            var7 = "from[" + RELATIONSHIP_RESOURCE_POOL + "].to.type.kindof";
            var8 = "from[" + RELATIONSHIP_RESOURCE_POOL + "].to.name";
            var9 = "from[" + RELATIONSHIP_RESOURCE_POOL + "].to.id";
            var10 = "from[" + RELATIONSHIP_RESOURCE_POOL + "].to.attribute[" + ATTRIBUTE_TITLE + "]";
        }

        if (var20) {
            var19.add(var9);
            var19.add(var8);
            var19.add(var6);
            var19.add(var7);
            var19.add(var3);
            var19.add(var4);
            var19.add(var5);
            var19.add(var10);
            Map var21 = this.getInfo(var1, var19);
            var11 = var21.get(var9);
            boolean var22 = false;
            if (null != var11) {
                if (var11 instanceof String) {
                    if (TYPE_ORGANIZATION.equals((String)var21.get(var7))) {
                        var22 = true;
                        var12.add((String)var11);
                        var13.add((String)var21.get(var8));
                        var14.add((String)var21.get(var6));
                        var15.add((String)var21.get(var3));
                        var16.add((String)var21.get(var4));
                        var17.add((String)var21.get(var5));
                        var18.add((String)var21.get(var10));
                    }
                } else if (var11 instanceof StringList) {
                    StringList var23 = (StringList)var21.get(var7);
                    int var24 = var23.size();

                    for(int var25 = 0; var25 < var24; ++var25) {
                        if (TYPE_ORGANIZATION.equals((String)var23.get(var25))) {
                            var22 = true;
                            var12.add((String)var11);
                            var13.add((String)((StringList)var21.get(var8)).get(var25));
                            var14.add((String)((StringList)var21.get(var6)).get(var25));
                            var15.add((String)((StringList)var21.get(var3)).get(var25));
                            var16.add((String)((StringList)var21.get(var4)).get(var25));
                            var17.add((String)((StringList)var21.get(var5)).get(var25));
                            var18.add((String)((StringList)var21.get(var10)).get(var25));
                        }
                    }
                }

                if (var22) {
                    var2 = new HashMap();
                    var2.put("OrganizationId", var12);
                    var2.put("OrganizationName", var13);
                    var2.put("OrganizationType", var14);
                    var2.put("OrganizationIsBU", var15);
                    var2.put("OrganizationIdIsDEP", var16);
                    var2.put("OrganizationIdIsCOM", var17);
                    var2.put("OrganizationTitle", var18);
                }
            }
        }

        return var2;
    }

    public Map getNormalizedFTEForPhase(Context var1, String var2, String var3, Map var4, Map var5) throws MatrixException {
        HashMap var6 = new HashMap();
        String var7 = EnoviaResourceBundle.getProperty(var1, "emxProgramCentral.ResourcePlan.NumberofPeopleUnit");
        if ("Hours".equalsIgnoreCase(var7)) {
            return var5;
        } else {
            try {
                Date var8 = getNormalizedDate(eMatrixDateFormat.getJavaDate((String)var4.get(SELECT_ATTRIBUTE_START_DATE)));
                Date var9 = getNormalizedDate(eMatrixDateFormat.getJavaDate((String)var4.get(SELECT_ATTRIBUTE_END_DATE)));
                Map var10 = this.getRequestTimeLineDays(var8, var9);
                HashMap var11 = new HashMap();
                Map var12 = this.getPhaseRequestTimeLineRatio(var1, var2, var8, var9, var10, var11);
                Map var13 = (Map)var12.get(var3);
                Map var14 = (Map)var13.get("PhaseRatioFactor");
                Double var15 = 0.0;

                String var17;
                for(Iterator var16 = var5.keySet().iterator(); var16.hasNext(); var6.put(var17, var15)) {
                    var15 = 0.0;
                    var17 = (String)var16.next();
                    Double var18 = (Double)var5.get(var17);
                    Double var19 = 0.0;
                    if (null != var11 && null != var11.get(var17)) {
                        var19 = (Double)var11.get(var17);
                    }

                    if (null != var14 && null != var14.get(var17)) {
                        var15 = new Double(var18 / var19) * (Double)var14.get(var17);
                    }
                }

                return var6;
            } catch (FrameworkException var20) {
                throw new MatrixException(var20);
            }
        }
    }

    private Map getPhaseRequestTimeLineRatio(Context var1, String var2, Date var3, Date var4, Map var5, Map var6) throws FrameworkException, MatrixException {
        HashMap var7 = null;
        HashMap var8 = new HashMap();
        MapList var9 = getPhaseList(var1, var2);
        FTE var10 = FTE.getInstance(var1);
        Iterator var11 = var9.iterator();

        while(var11.hasNext()) {
            HashMap var12 = new HashMap();
            Map var13 = (Map)var11.next();
            String var14 = (String)var13.get("id");
            Date var15 = getNormalizedDate(eMatrixDateFormat.getJavaDate((String)var13.get(ATTRIBUTE_PHASE_START_DATE)));
            Date var16 = getNormalizedDate(eMatrixDateFormat.getJavaDate((String)var13.get(ATTRIBUTE_PHASE_FINISH_DATE)));
            MapList var17 = var10.getTimeframes(var15, var16);
            String var18 = var10.getTimeFrame(var15);
            String var19 = var10.getTimeFrame(var16);
            StringList var20 = FrameworkUtil.split(var18, "-");
            StringList var21 = FrameworkUtil.split(var19, "-");
            int var22 = Integer.parseInt(((String)var20.get(0)).toString());
            int var23 = Integer.parseInt(((String)var20.get(1)).toString());
            int var24 = Integer.parseInt(((String)var21.get(0)).toString());
            int var25 = Integer.parseInt(((String)var21.get(1)).toString());
            new StringList();
            Double var27 = 0.0;
            Double var28 = 0.0;
            var7 = new HashMap();
            Iterator var29 = var17.iterator();

            while(var29.hasNext()) {
                Map var30 = (Map)var29.next();
                int var31 = (Integer)var30.get("timeframe");
                int var32 = (Integer)var30.get("year");
                String var33 = var31 + "-" + var32;
                var28 = 0.0;
                Date var34 = var10.getStartDate(var33);
                if (var31 == var22 && var32 == var23) {
                    var34 = var15;
                }

                Date var35 = var10.getEndDate(var33);
                if (var31 == var24 && var32 == var25) {
                    var35 = var16;
                }

                if (null != var5.get(var33)) {
                    if (var34.before(var3)) {
                        var34 = var3;
                    }

                    if (var35.after(var4)) {
                        var35 = var4;
                    }

                    var27 = (double)ProgramCentralUtil.computeDuration(var34, var35);
                    if (null != var5.get(var33)) {
                        Double var36 = (Double)var5.get(var33);
                        var28 = new Double(var27 / var36);
                    }

                    var7.put(var33, var28);
                    if (null != var6.get(var33)) {
                        var28 = var28 + (Double)var6.get(var33);
                    }

                    var6.put(var33, var28);
                }
            }

            var12.put("fromDate", var15);
            var12.put("toDate", var16);
            var12.put("PhaseRatioFactor", var7);
            var8.put(var14, var12);
        }

        return var8;
    }

    public Map getRequestTimeLineDays(Date var1, Date var2) throws MatrixException {
        FTE var3 = FTE.getInstance();
        MapList var4 = var3.getTimeframes(var1, var2);
        String var5 = var3.getTimeFrame(var1);
        String var6 = var3.getTimeFrame(var2);
        StringList var7 = FrameworkUtil.split(var5, "-");
        StringList var8 = FrameworkUtil.split(var6, "-");
        int var9 = Integer.parseInt(((String)var7.get(0)).toString());
        int var10 = Integer.parseInt(((String)var7.get(1)).toString());
        int var11 = Integer.parseInt(((String)var8.get(0)).toString());
        int var12 = Integer.parseInt(((String)var8.get(1)).toString());
        HashMap var13 = new HashMap();
        int var14 = 0;
        int var15 = var4.size();
        Double var16 = 0.0;
        Iterator var17 = var4.iterator();

        while(var17.hasNext()) {
            Map var18 = (Map)var17.next();
            int var19 = (Integer)var18.get("timeframe");
            int var20 = (Integer)var18.get("year");
            if (var14 == 0) {
                if (Integer.parseInt(((String)var7.get(0)).toString()) == Integer.parseInt(((String)var8.get(0)).toString()) && Integer.parseInt(((String)var7.get(1)).toString()) == Integer.parseInt(((String)var8.get(1)).toString())) {
                    var16 = (double)ProgramCentralUtil.computeDuration(var1, var2);
                } else {
                    var16 = (double)var3.getNumberofDaysSinceStartTimeFrame(var1);
                }
            } else if (var14 == var15 - 1) {
                var16 = (double)var3.getNumberofDaysTillEndTimeFrame(var2);
            } else {
                Date var21 = var3.getStartDate(var19 + "-" + var20);
                Date var22 = var3.getEndDate(var19 + "-" + var20);
                var16 = (double)ProgramCentralUtil.computeDuration(var21, var22);
            }

            String var23 = var19 + "-" + var20;
            ++var14;
            var13.put(var23, var16);
        }

        return var13;
    }

    public void updateStandardCostCurrency(Context var1, String var2, String var3, String var4) throws MatrixException {
        try {
            Double var5 = Task.parseToDouble(var2);
            var5 = Currency.convert(var1, var5, var3, var4, new Date(), false);
            this.setAttributeValue(var1, ProgramCentralConstants.ATTRIBUTE_STANDARD_COST, var5 + " " + var4);
        } catch (FrameworkException var6) {
            throw new MatrixException(var6);
        }
    }

    static {
        ATTRIBUTE_PHASE_START_DATE = "attribute[" + ATTRIBUTE_TASK_ESTIMATED_START_DATE + "]";
        ATTRIBUTE_PHASE_FINISH_DATE = "attribute[" + ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE + "]";
        SELECT_PROJECT_SPACE_ID = "to[" + RELATIONSHIP_RESOURCE_PLAN + "].from.id";
        ATTRIBUTE_STANDARD_COST = PropertyUtil.getSchemaProperty("attribute_StandardCost");
        ATTRIBUTE_RESOURCE_PLAN_PREFRENCE = PropertyUtil.getSchemaProperty("attribute_ResourcePlanPreference");
    }
}
