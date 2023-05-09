//
// $Id: emxProgram.java.rca 1.6 Wed Oct 22 16:21:22 2008 przemek Experimental przemek $ 
//
// emxProgram.java
//
// Copyright (c) 2002-2020 Dassault Systemes.
// All Rights Reserved
// This program contains proprietary and trade secret information of
// MatrixOne, Inc.  Copyright notice is precautionary only and does
// not evidence any actual or intended publication of such program.
//

import com.matrixone.apps.common.util.ComponentsUtil;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.PersonUtil;
import com.matrixone.apps.program.ProgramCentralConstants;
import matrix.db.*;
import matrix.util.StringList;

import java.util.HashMap;

/**
 * The <code>emxProgram</code> class represents the Program JPO
 * functionality for the AEF type.
 *
 * @version AEF 10.0.SP4 - Copyright (c) 2002, MatrixOne, Inc.
 */
public class emxProgram_mxJPO extends emxProgramBase_mxJPO
{

    /**
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @throws Exception if the operation fails
     * @since AEF 10.0.SP4
     * @grade 0
     */
    public emxProgram_mxJPO (Context context, String[] args)
        throws Exception
    {
      super(context, args);
    }

    // HJ - gscCodeMaster
    @com.matrixone.apps.framework.ui.ProgramCallable
    public MapList getAllPrograms(Context context, String[] args)
            throws Exception
    {
        return getMyPrograms(context, args, null);
    }


    /**
     * This method gets the list of All Programs the user has access
     * @param context the eMatrix <code>Context</code> object
     * @param args holds the following input arguments:
     *        0 - objectList MapList
     * @return MapList containing the ids of program objects
     * @throws Exception if the operation fails
     * @since PMC 10-6
     */
    public MapList getMyPrograms(Context context, String[] args,String showSel)
            throws Exception
    {
        // Check license while listing Programs, if license check fails here
        // the programs will not be listed.
        //
        ComponentsUtil.checkLicenseReserved(context, ProgramCentralConstants.PGE_LICENSE_ARRAY);

        // Retrieve the program's program list.
        MapList programList = null;
        try
        {
            com.matrixone.apps.program.Program program = (com.matrixone.apps.program.Program) DomainObject.newInstance(context, DomainConstants.TYPE_PROGRAM,DomainConstants.PROGRAM);
            com.matrixone.apps.common.Person person = (com.matrixone.apps.common.Person) DomainObject.newInstance(context, DomainConstants.TYPE_PERSON);
            HashMap programMap = (HashMap) JPO.unpackArgs(args);
            String objectId = (String) programMap.get("objectId");
            String busWhere = null;

            // Add selectables
            StringList busSelects = new StringList(2);
            busSelects.add(program.SELECT_ID);
            busSelects.add(program.SELECT_VAULT);
            if(showSel == null)
            {
                busWhere = null;
            }
            else if((STATE_PROGRAM_INACTIVE).equals(showSel))
            {
                // modified for the bug 338045
                busWhere = program.SELECT_CURRENT + "== const '"+STATE_PROGRAM_INACTIVE+"'";
            }
            else
            {
                // modified for the bug 338045
                busWhere = program.SELECT_CURRENT + "!= const '"+STATE_PROGRAM_INACTIVE+"'";
            }


            String vaultPattern = "";
            String vaultOption = PersonUtil.getSearchDefaultSelection(context);
            vaultPattern = PersonUtil.getSearchVaults(context, false ,vaultOption);

            //use the matchlist keyword to filter by vaults, need this if option is not "All Vaults"
            if (!vaultOption.equals(PersonUtil.SEARCH_ALL_VAULTS) && vaultPattern.length() > 0)
            {
                if ((busWhere == null) || "".equals(busWhere))
                {
                    busWhere = "vault matchlist '" + vaultPattern + "' ','";
                }
                else
                {
                    busWhere += "&& vault matchlist '" + vaultPattern + "' ','";
                }
            }

            // pagination change
            if ((objectId == null) || objectId.equals("") || objectId.equals("null"))
            {
                programList = program.getPrograms(context, busSelects, busWhere);
            }
            else
            {
                programList = program.findObjects(context,
                        DomainConstants.TYPE_PROGRAM,
                        null,
                        null,
                        null,
                        null,//vaultPattern
                        busWhere,//busWhere
                        false,
                        busSelects);
            }
        }
        catch (Exception ex)
        {
            throw ex;
        }
        finally
        {
            return programList;
        }
    }
}
