/*
** emxRMTNormalizeRequirementStructureBase
**
** Copyright (c) 1992-2020 Dassault Systemes.
**
** All Rights Reserved.
** This program contains proprietary and trade secret information of
** MatrixOne, Inc.  Copyright notice is precautionary only and does
** not evidence any actual or intended publication of such program.
**
*
*/

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Map;

import matrix.db.Context;
import matrix.util.StringList;

import com.dassault_systemes.requirements.ReqSchemaUtil;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.EnoviaResourceBundle;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.requirements.SpecificationStructure;

/**
 * The <code>emxRMTNormalizeRequirementStructureBase</code> class contains migration script
 * @author
 * @version RequirementsManagement V6R2010x - Copyright (c) 2011, MatrixOne, Inc.
 *
 */
public class emxRMTNormalizeRequirementStructureBase_mxJPO
{
    private static final String BUNDLE_STR = "emxRequirementsStringResource";
    private static final String MIGRATION_LOG_FILE = "emxRequirements.Migration.LogInformation";
    private static final String MIGRATION_ERR_FILE = "emxRequirements.Migration.ERRInformation";
    private static final String END_LINE = "\n";
    private static final String MIGRATE_SUCCESS = "emxRequirements.Migration.Log.MigrateSuccess";
    private static final String MIGRATE_FAILURE = "emxRequirements.Migration.Log.MigrateFailure";
    private static FileOutputStream foLogFileInfo;
    private static FileOutputStream foErrFile;
    private static PrintStream psLogWriter;
    private static PrintStream psErrWriter;


    /**
    * Create a new emxRMTContentDataMigrationBase object from a given id.
    *
    * @param context the eMatrix <code>Context</code> object
    * @param args holds no arguments.
    * @return a emxRMTContentDataMigrationBase Object
    * @throws Exception if the operation fails
    * @since RequirementsManagement V6R2012x
    */
    public emxRMTNormalizeRequirementStructureBase_mxJPO (Context context, String[] args) throws Exception
    {
        //super(context, args);
    }

    /**
     * Main entry point.
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @return an integer status code (0 = success)
     * @throws Exception if the operation fails
     * @since RequirementsManagement V6R2012x
     */
    public int mxMain (Context context, String[] args)
    throws Exception
    {
        if (!context.isConnected())
        {
            String strContentLabel = EnoviaResourceBundle.getProperty(context, "emxRequirementsStringResource", context.getLocale(), "emxRequirements.Error.UnsupportedClient"); 
            throw  new Exception(strContentLabel);
        }
        normalize(context,args);
        return  0;
     }

    /**
     * this method migrates the data.
     * @param context the eMatrix <code>Context</code> object
     * @param args  - is a string array
     * @throws Exception if the operation fails
     * @since RequirementsManagement V6R2012x
     */
    public void normalize(Context context,String[] args)
    throws Exception
    {
        //MQLCommand mqlCommand = new MQLCommand();
        //set the trigger off
        //MqlUtil.mqlCommand(context,"trigger off");
        try
        {
            foLogFileInfo = new FileOutputStream(getString(context,MIGRATION_LOG_FILE),false);
            psLogWriter = new PrintStream(foLogFileInfo);
        }
        catch(Exception e)
        {
            Exception ex=new Exception("Migration Log File Entry Not Present in the Properties File");
            throw ex;
        }
        //open file for error logging
        try
        {
            foErrFile = new FileOutputStream(getString(context,MIGRATION_ERR_FILE),false);
            psErrWriter = new PrintStream(foErrFile);
        }
        catch(Exception e)
        {
            Exception ex=new Exception("Migration Error File Entry Not Present in the Properties File");
            throw ex;
        }

        normalizeRequirementStructure(context, args);
        //set the trigger on
        //MqlUtil.mqlCommand(context,"trigger on");
    }


    /**
     * this method normalizes the Sequence Order of Requirement Structure
     * @param context the eMatrix <code>Context</code> object
     * @param args  - is a string array
     * @throws Exception if the operation fails
     * @since RequirementsManagement V6R2012x
     */
	protected void normalizeRequirementStructure(Context context,String[] args) throws Exception
    {
        try
        {
            String strType=ReqSchemaUtil.getRequirementType(context);
            String strWhereExp = null; 
            StringList objSelect = new StringList(2);
            objSelect.addElement(DomainConstants.SELECT_ID);

            // fetching all the Requirement objects in the database
            MapList mapList = DomainObject.findObjects(context,strType,DomainConstants.QUERY_WILDCARD,strWhereExp,objSelect);
            DomainObject reqObj = DomainObject.newInstance(context);

            for(int i=0;i<mapList.size();i++)
            {
                String strId = (String)( (Map)mapList.get(i) ).get(DomainConstants.SELECT_ID);
                
                migrationInfoWriter("Normalizing " + strId);
                reqObj.setId(strId);
				SpecificationStructure.normalizeSequenceOrder(context, reqObj, ReqSchemaUtil.getSubRequirementRelationship(context), false);
				SpecificationStructure.normalizeSequenceOrder(context, reqObj, ReqSchemaUtil.getDerivedRequirementRelationship(context), false);
}
        }
        catch(Exception ex)
        {
			migrationErrWriter(getString(context,MIGRATE_FAILURE));
            throw ex;
        }
    }

    /**
    * This method writes to the log file using the Printwriter object
    * @param strLogEnrty - Message to write
    * @throws Exception if the operation fails
    * @since RequirementsManagement V6R2012x
    * @grade 0
    */
    protected void migrationInfoWriter(String strLogEnrty)
    throws Exception
    {
        if (strLogEnrty!=null && strLogEnrty.length()>0)
        {
            psLogWriter.println(strLogEnrty);

        }
    }
    /**
    * This method writes to the ERR file using the Printwriter object
    * @param strLogEnrty - Message to write
    * @throws Exception if the operation fails
    * @since RequirementsManagement V6R2012x
    * @grade 0
    */
    protected void migrationErrWriter(String strLogEnrty)
    throws Exception
    {
        if (strLogEnrty!=null && strLogEnrty.length()>0)
        {
            psErrWriter.println(strLogEnrty);
        }
    }

    /**
    * This method returns internalized value for the passed key in
    * ProductCentral string resource file.
    * @param context - The eMatrix <code>Context</code> object
    * @param strKey - Property file entry
    * @since RequirementsManagement V6R2012x
    * @grade 0
    */
    private String getString(Context context,String strKey) throws Exception
    {
        return EnoviaResourceBundle.getProperty(context, BUNDLE_STR, context.getLocale(), strKey); 
    }

}

