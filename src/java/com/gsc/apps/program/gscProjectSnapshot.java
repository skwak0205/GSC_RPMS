package com.gsc.apps.program;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.Map;

import com.matrixone.apps.program.ProgramCentralConstants;
import com.matrixone.apps.program.ProgramCentralUtil;
import matrix.db.Context;
import matrix.util.MatrixException;
import matrix.util.StringList;

public class gscProjectSnapshot extends gscProjectSpace {
    public static final String TYPE_PROJECT_SNAPSHOT;
    private gscProjectSpace masterGscProjectSpaceObject = null;

    public gscProjectSnapshot(gscProjectSpace var1) {
        this.masterGscProjectSpaceObject = var1;
    }

    public gscProjectSnapshot() {
    }

    public gscProjectSnapshot(String var1) throws Exception {
        super(var1);
    }

    private gscProjectSpace getMasterProject(Context var1) throws MatrixException {
        if (this.masterGscProjectSpaceObject == null) {
            this.getId(var1);
            String var3 = this.getInfo(var1, "to[Related Projects].from.id");

            try {
                gscProjectSpace var4 = new gscProjectSpace(var3);
                return var4;
            } catch (Exception var5) {
                throw new MatrixException(var5);
            }
        } else {
            return this.masterGscProjectSpaceObject;
        }
    }

    public void setMasterProject(Context var1, gscProjectSpace var2) {
        if (this.masterGscProjectSpaceObject == null) {
            this.masterGscProjectSpaceObject = var2;
        }

    }

    public String getMasterProjectId(Context var1) throws MatrixException {
        return this.masterGscProjectSpaceObject.getId(var1);
    }

    public gscProjectSnapshot create(Context var1) throws MatrixException {
        gscProjectSpace var2 = this.getMasterProject(var1);

        assert var2 != null;

        gscProjectSnapshot var3 = null;
        boolean var4 = true;

        try {
            var3 = this.create(var1, var4);
            return var3;
        } catch (Exception var6) {
            throw new MatrixException(var6);
        }
    }

    private gscProjectSnapshot create(Context var1, boolean var2) throws MatrixException {
        gscProjectSpace var3 = this.getMasterProject(var1);
        gscProjectSnapshot var4 = var3.createSnapshot(var1, var2);
        return var4;
    }

    public void delete(Context var1) throws MatrixException {
        gscProjectSpace var2 = this.getMasterProject(var1);
        String var3 = this.getId(var1);
        var2.deleteSnapshot(var1, var3);
    }

    public void setEffectivity(Context var1) throws MatrixException {
        String var2 = this.getId(var1);
        gscProjectSpace var3 = this.getMasterProject(var1);
        if (!ProgramCentralUtil.isNullString(var2) && var3 != null) {
            String var4 = var3.getEffectivityStartDate(var1, false);
            String var5 = ProgramCentralUtil.getMatrixFormatedCurrentDate(var1);
            Map var6 = com.matrixone.apps.program.ProjectSpace.formatDateEffectivityExpression(var1, var4, var5);
            com.matrixone.apps.program.ProjectSpace.setDateEffectivity(var1, var2, var6, false);
        } else {
            throw new IllegalArgumentException("No Snapshot or master project business object");
        }
    }

    public String getEffectivityStartDate(Context var1) throws MatrixException {
        return this.getEffectivityDate(var1, false, true);
    }

    public String getEffectivityEndDate(Context var1) throws MatrixException {
        return this.getEffectivityDate(var1, false, false);
    }

    public String getEffectivityDisplayStartDate(Context var1) throws MatrixException {
        return this.getEffectivityDisplayDate(var1, false, true);
    }

    public String getEffectivityDisplayEndDate(Context var1) throws MatrixException {
        return this.getEffectivityDisplayDate(var1, false, false);
    }

    private String getEffectivityDate(Context var1, boolean var2, boolean var3) throws MatrixException {
        String var4 = this.getId(var1);
        if (ProgramCentralUtil.isNullString(var4)) {
            throw new IllegalArgumentException("No businees object");
        } else {
            Map var5 = null;

            try {
                var5 = this.getEffectivityExpression(var1, var2);
            } catch (Exception var7) {
                throw new MatrixException(var7);
            }

            String var6 = "";
            if (var3) {
                var6 = this.evaluateActualEffectivityStartDateFromExpression(var1, var5);
            } else {
                var6 = this.evaluateActualEffectivityEndDateFromExpression(var1, var5);
            }

            return ProgramCentralUtil.isNotNullString(var6) ? var6 : "";
        }
    }

    private String getEffectivityDisplayDate(Context var1, boolean var2, boolean var3) throws MatrixException {
        String var4 = this.getId(var1);
        if (ProgramCentralUtil.isNullString(var4)) {
            throw new IllegalArgumentException("No businees object");
        } else {
            Map var5 = null;

            try {
                var5 = this.getEffectivityExpression(var1, var2);
            } catch (Exception var7) {
                throw new MatrixException(var7);
            }

            String var6 = "";
            if (var3) {
                var6 = this.evaluateDisplayEffectivityStartDateFromExpression(var1, var5);
            } else {
                var6 = this.evaluateDisplayEffectivityEndDateFromExpression(var1, var5);
            }

            return ProgramCentralUtil.isNotNullString(var6) ? var6 : "";
        }
    }

    protected boolean isFirstSnapshot(Context var1) throws MatrixException {
        String var2 = this.getId(var1);
        if (ProgramCentralUtil.isNullString(var2)) {
            throw new IllegalArgumentException("No Business object");
        } else {
            gscProjectSpace var3 = this.getMasterProject(var1);
            if (var3 == null) {
                throw new IllegalArgumentException("Master project is not set");
            } else {
                StringList var4 = var3.getInfoList(var1, "from[Related Projects].to.id");
                int var5 = var4 != null ? var4.size() : 0;
                return var5 == 1 ? Boolean.TRUE : Boolean.FALSE;
            }
        }
    }

    static {
        TYPE_PROJECT_SNAPSHOT = ProgramCentralConstants.TYPE_PROJECT_SNAPSHOT;
    }
}
