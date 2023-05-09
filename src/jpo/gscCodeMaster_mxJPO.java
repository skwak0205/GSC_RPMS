import com.gsc.apps.app.util.gscMQLConstants;
import com.gsc.apps.common.constants.gscTypeConstants;
import com.gsc.apps.common.util.gscCommonCodeUtil;
import com.gsc.apps.common.util.gscListUtil;
import com.gsc.apps.common.util.gscStringUtil;
import com.matrixone.apps.common.util.ComponentsUtil;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.framework.ui.UIUtil;
import com.matrixone.apps.library.LibraryCentralConstants;
import com.matrixone.apps.program.ProgramCentralConstants;
import com.matrixone.apps.program.ProgramCentralUtil;
import com.matrixone.apps.program.ProjectSpace;
import matrix.db.Context;
import matrix.db.JPO;
import matrix.util.MatrixException;
import matrix.util.StringList;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class gscCodeMaster_mxJPO {

    //@com.matrixone.apps.framework.ui.ProgramCallable
    public MapList getAllgscCodeMaster(Context context, String[] args)
            throws MatrixException
    {
        return getgscCodeMasterList(context, args, "All");
    }

    private MapList getgscCodeMasterList(Context context, String[] args, String Mode) throws MatrixException
    {
        // Check license while listing Project Concepts, Project Space, if license check fails here
        // the projects will not be listed. This is mainly done to avoid Project Concepts from being listed
        // but as this is the common method, the project space objects will also not be listed.
        //
        ComponentsUtil.checkLicenseReserved(context,ProgramCentralConstants.PGE_LICENSE_ARRAY);

        StringList busSelects = new StringList(2);
        busSelects.add(DomainConstants.SELECT_ID);
        busSelects.add(DomainConstants.SELECT_NAME);
        busSelects.add("type");
        //busSelects.add("attribute[" + DomainConstants.ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE  + "]");
        busSelects.add(DomainConstants.SELECT_VAULT);

        String busWhere = "";
        try{
            if (ProgramCentralUtil.isNullString(busWhere)) {
                busWhere +=" type!=" + ProgramCentralConstants.TYPE_EXPERIMENT+ " && type!='" + ProgramCentralConstants.TYPE_PROJECT_BASELINE+"'";
            } else {
                busWhere +=" && type!=" + ProgramCentralConstants.TYPE_EXPERIMENT+ " && type!='" + ProgramCentralConstants.TYPE_PROJECT_BASELINE+"'";
            }

            // String TYPE_PROJECT_SPACE = PropertyUtil.getSchemaProperty("type_ProjectSpace");
            // String TYPE_PROJECT_CONCEPT = PropertyUtil.getSchemaProperty("type_ProjectConcept");
            String typePattern = gscTypeConstants.TYPE_GSCCODEMASTER;
            MapList gscCodeMasterList = DomainObject.findObjects(
                    context,
                    typePattern, "*", "*", "*", "*", busWhere, true, busSelects);

            return gscCodeMasterList;

        }
        catch (Exception ex) {
            throw new MatrixException(ex);
        }
    }

}
