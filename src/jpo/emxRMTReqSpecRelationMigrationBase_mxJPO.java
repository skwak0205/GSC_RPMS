/*
** emxRMTReqSpecRelationMigrationBase
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
import java.util.List;

import matrix.db.Context;

import com.dassault_systemes.requirements.ReqSchemaUtil;
import com.matrixone.apps.domain.util.EnoviaResourceBundle;
import com.matrixone.apps.domain.util.MqlUtil;
import com.matrixone.apps.productline.ProductLineUtil;
import com.matrixone.apps.requirements.RequirementsUtil;

/**
 * The <code>emxRMTReqSpecRelationMigrationBase</code> class contains migration script
 * @author 
 * @version RequirementsManagement V6R2008-2 - Copyright (c) 2007, MatrixOne, Inc.
 *
 */
public class emxRMTReqSpecRelationMigrationBase_mxJPO
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
    * Create a new emxRMTReqSpecRelationMigrationBase object from a given id.
    *
    * @param context the eMatrix <code>Context</code> object
    * @param args holds no arguments.
    * @return a emxPMCSequenceOrderMigrationBase Object
    * @throws Exception if the operation fails
    * @since RequirementsManagement V6R2008-2
    */
    public emxRMTReqSpecRelationMigrationBase_mxJPO (Context context, String[] args) throws Exception
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
     * @since RequirementsManagement V6R2008-2
     */
    public int mxMain (Context context, String[] args)
    throws Exception
    {
        if (!context.isConnected())
        {
            String strContentLabel = EnoviaResourceBundle.getProperty(context, "emxRequirementsStringResource", context.getLocale(), "emxRequirements.Error.UnsupportedClient"); 
            throw  new Exception(strContentLabel);
        }
        migrate(context,args);
        return  0;
     }

    /**
     * this method migartes the data.
     * @param context the eMatrix <code>Context</code> object
     * @param args  - is a string array
     * @throws Exception if the operation fails
     * @since RequirementsManagement V6R2008-2
     */

    public void migrate(Context context,String[] args)
    throws Exception
    {
        //set the trigger off
        MqlUtil.mqlCommand(context,"trigger off");
   try {
        try
        {
            foLogFileInfo = new FileOutputStream(getString(context,MIGRATION_LOG_FILE),false);
            psLogWriter = new PrintStream(foLogFileInfo);
        }
        catch(Exception e)
        {
            Exception ex=new Exception("Migration Log File Entry Not Present in the Properties File");
            throw e;
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

       // Migrate all the Image objects to checkin them to appropriate formats
        migrateRequirementSpecificationRels(context, args);
		migrateOriginatingRequirementRels(context, args);
   } finally {
        //set the trigger on
        MqlUtil.mqlCommand(context,"trigger on");
   }
    }

    /**
     * this method migrates all the 'Requirement Specification' relationships to the 'Specification Structure' relationship
     * @param context the eMatrix <code>Context</code> object
     * @param args  - is a string array
     * @throws Exception if the operation fails
     * @since RequirementsManagement V6R2008-2
     */
    protected void migrateRequirementSpecificationRels(Context context,String[] args) throws Exception
    {
        try
        {
			
            String strRelType = RequirementsUtil.getRequirementSpecificationRelationship(context);
			String strNewRelType = ReqSchemaUtil.getSpecStructureRelationship(context);
			
			List lstReqSpecTypes = ProductLineUtil.getChildrenTypes(context, ReqSchemaUtil.getRequirementSpecificationType(context));
			lstReqSpecTypes.add(ReqSchemaUtil.getRequirementSpecificationType(context));
			
			String strCommand = "query connection type $1 select $2 dump $3";
            String strResults = MqlUtil.mqlCommand(context, strCommand, strRelType, "id from.id to.id to.type from.name to.name", "|");
			
			String[] resultsArray = strResults.split("\n");
			for(int i=0;i<resultsArray.length;i++)
			{
				String[] parsedResult = resultsArray[i].split("[|]");
				if (parsedResult.length == 7 )
				{
					String relOID = parsedResult[1];
					String fromOID = parsedResult[2];
					String toOID = parsedResult[3];
					String toType = parsedResult[4];
					String fromName = parsedResult[5];
					String toName = parsedResult[6];
					
					if (lstReqSpecTypes.contains(toType))
					{					
						String createCommand = "add connection $1 to $2 from $3"; 
						MqlUtil.mqlCommand(context, createCommand, strNewRelType, fromOID, toOID);
						
						String deleteCommand = "disconnect connection $1";
						MqlUtil.mqlCommand(context, deleteCommand, relOID);
						
						//migrationInfoWriter("Migrated Relationship " + relOID);
					}
					else
					{
						migrationInfoWriter("Could not migrate Relationship of type " + ReqSchemaUtil.getRequirementSpecificationType(context) + " between " + fromName + " and " + toName + " because of invalid To Type.");
					}
				}				
			}
        }
        catch(Exception ex)
        {
            migrationInfoWriter(getString(context,MIGRATE_FAILURE));
            throw ex;
        }
    }

	/**
     * this method migrates all the 'Originating Requirement' relationships to the 'Specification Structure' relationship
     * @param context the eMatrix <code>Context</code> object
     * @param args  - is a string array
     * @throws Exception if the operation fails
     * @since RequirementsManagement V6R2008-2
     */
	protected void migrateOriginatingRequirementRels(Context context,String[] args) throws Exception
    {
        try
        {
			
            String strRelType = ReqSchemaUtil.getOriginatingRequirementRelationship(context);
			String strNewRelType = ReqSchemaUtil.getSpecStructureRelationship(context);
					
			String strCommand = "query connection type $1 select $2 dump $3";
            String strResults = MqlUtil.mqlCommand(context, strCommand, strRelType, "id from.id to.id", "|");
			
			String[] resultsArray = strResults.split("\n");
			for(int i=0;i<resultsArray.length;i++)
			{
				String[] parsedResult = resultsArray[i].split("[|]");
				if (parsedResult.length == 4 )
				{
					String relOID = parsedResult[1];
					String fromOID = parsedResult[2];
					String toOID = parsedResult[3];				
					
					String createCommand = "add connection $1 to $2 from $3"; 
					MqlUtil.mqlCommand(context, createCommand, strRelType, toOID, fromOID);
					
					String deleteCommand = "disconnect connection $1";
					MqlUtil.mqlCommand(context, deleteCommand, relOID);
									
					//migrationInfoWriter("Migrated Relationship " + relOID);
				}				
			}
           
            migrationInfoWriter(getString(context,MIGRATE_SUCCESS));
        }
        catch(Exception ex)
        {
            migrationInfoWriter(getString(context,MIGRATE_FAILURE));
            throw ex;
        }
    }

    /**
    * This method writes to the log file using the Printwriter object
    * @param strLogEnrty - Message to write
    * @throws Exception if the operation fails
    * @since RequirementsManagement V6R2008-2
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
    * @since RequirementsManagement V6R2008-2
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
    * ProductCentral string resouce file.
    * @param context - The eMatrix <code>Context</code> object
    * @param strKey - Property file entry
    * @since RequirementsManagement V6R2008-2
    * @grade 0
    */
    private String getString(Context context,String strKey) throws Exception
    {
        return EnoviaResourceBundle.getProperty(context, BUNDLE_STR, context.getLocale(), strKey); 
    }

}

