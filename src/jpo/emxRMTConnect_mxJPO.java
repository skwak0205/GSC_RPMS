/*
** ${CLASSNAME}.java
** Copyright (c) 2003-2020 Dassault Systemes.
** All Rights Reserved
** This program contains proprietary and trade secret information of
** MatrixOne, Inc.  Copyright notice is precautionary only and does
** not evidence any actual or intended publication of such program.
** static const char RCSID[] = "$Id: /ENORequirementsManagementBase/CNext/Modules/ENORequirementsManagementBase/JPOsrc/custom/${CLASSNAME}.java 1.3.2.1.1.1 Wed Oct 29 22:20:01 2008 GMT przemek Experimental$";
*/

import matrix.db.Context;

import com.matrixone.apps.domain.util.EnoviaResourceBundle;


/**
 * This JPO class has methods pertaining to Requirement type
 * @version RequirementCentral 10.0.0.0 - Copyright (c) 2003, MatrixOne, Inc.
 */
public class emxRMTConnect_mxJPO extends emxRMTConnectBase_mxJPO
{

    /**
     * Create a new emxRMTConnect object from a given id
     *
     * @param context context for this request
     * @param arg[0] the object id
     * @exception Exception when unable to find object in the RequirementCentral
     * @since RequirementCentral 10.0.0.0
     * @grade 0
     */
    public emxRMTConnect_mxJPO (Context context, String[] args) throws Exception
    {
        super(context, args);
    }

    /**
     * Main entry point
     *
     * @param context context for this request
     * @param args holds no arguments
     * @return an integer status code (0 = success)
     * @exception Exception when problems occurred in the RequirementCentral
     * @since RequirementCentral 10.0.0.0
     * @grade 0
     */
    public int mxMain (Context context, String[] args) throws Exception
    {
        if (!context.isConnected())
        {
            String language = context.getSession().getLanguage();
            String strContentLabel = EnoviaResourceBundle.getProperty(context, "emxRequirementsStringResource", context.getLocale(), "emxRequirements.Alert.contextemxRMTConnectFailed");
            throw  new Exception(strContentLabel);
        }
        return  0;
    }
}

