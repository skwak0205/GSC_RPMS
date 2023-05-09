import java.io.FileWriter;
import java.util.Map;

import matrix.db.MatrixWriter;
import matrix.db.Context;
import matrix.util.SelectList;
import matrix.util.StringList;

import java.io.BufferedWriter;

import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.MqlUtil;
import com.matrixone.apps.domain.util.PropertyUtil;
import com.matrixone.apps.library.LibraryCentralConstants;


public class emxLibraryCentralGenericDocumentAccessMigrationBase_mxJPO extends emxCommonMigration_mxJPO
{
	String genericDocType = "";
	
	String migrationRunNoProperty        	= "LibraryCentalGrantObjectsMigrationRunNo";
	String program_SystemInformation     	= "eServiceSystemInformation.tcl";
	String migrationStatusProperty       	= "LibraryCentalGrantObjectsMigration";

    String state_PreMigrationInProcess   	= "PreMigrationInProcess";
    String state_PreMigrationComplete    	= "PreMigrationComplete";
    String state_MigrationInProcess      	= "MigrationInProcess";
    String state_MigrationComplete       	= "MigrationComplete";
    String state_PostMigrationInProcess  	= "PostMigrationInProcess";
    String state_PostMigrationComplete   	= "PostMigrationComplete";

    String migrationlogName              	= "migration.log";
    String migratedObjectIdsLogName      	= "convertedIds.txt";
    String fileStatusLogName             	= "fileStatus.csv";
    String failedObjectIdsLogNamePrefix  	= "unConvertedObjectIds_";
    String failedObjectIdsLogNameSuffix  	= ".csv";

    int flatFileNoUnderProcess		 		= 1;


	public emxLibraryCentralGenericDocumentAccessMigrationBase_mxJPO(Context context, String[] args) throws Exception
	{
		super(context, args);
		genericDocType = PropertyUtil.getSchemaProperty(context,"type_GenericDocument");
	}


    /**
     * This method is executed if a specific method is not specified
     * @param context the eMatrix <code>Context</code> object
     * @param args holds three arguments
     *        args[0] - Directory where files containing ObjectIds are to be read
     *        args[1] - minimum Range (ie. Flat file to start migration from)
     *        args[2] - maximum Range (ie. Flat file till which migration is to be run)
     *
     * @returns 0 always
     * @throws Exception if the migration fails
     */
    public int mxMain(Context context,String[] args)throws Exception
    {
    	try
    	{
    		if (args.length<3 || args.length>3)
    		{
    			error = "Wrong number of arguments";
    			throw new IllegalArgumentException(error);
    		}
    		else
    		{
    			setAdminMigrationStatus(context,migrationRunNoProperty,"increment");
    			// write Run No. to log Files
    			writeRunNoToLogs(context);

    			preMigration(args);

    			setAdminMigrationStatus(context,migrationStatusProperty, state_MigrationInProcess);

    			// call Main Migration
    			writer = new BufferedWriter(new MatrixWriter(context));
    			super.mxMain(context, args);

    			libraryCentralCreateLogs(context);
    			
    			//This command will give a General Library count who all are not connected with relationship Library Reference Feature.
    			String  noOfGenericDocWithAccess = MqlUtil.mqlCommand(context, "eval expr $1 on temp query bus $2 $3 $4 where $5", "count TRUE", genericDocType, "*", "*", "access!='' && policy!=Version && to[Classified Item]==TRUE && attribute[Access Type]==Inherited");
    			String  noOfGenericDocCount = MqlUtil.mqlCommand(context, "eval expr $1 on temp query bus $2 $3 $4 where $5", "count TRUE", genericDocType, "*", "*", "policy!=Version && to[Classified Item]==TRUE && attribute[Access Type]==Inherited");

    			try{
    				if(noOfGenericDocWithAccess.equals(noOfGenericDocCount))
    				{
    					mqlLogRequiredInformationWriter("=============================================================================\n");
    					mqlLogRequiredInformationWriter("There are "+noOfGenericDocCount+" '"+genericDocType+"' objects which are Migrated\n");
    					mqlLogRequiredInformationWriter(" \n");
    					mqlLogRequiredInformationWriter("Step 3 of Migration :     COMPLETE \n");
    					mqlLogRequiredInformationWriter("\nLogging of this Migration will be written to: "+migrationlogName+"\n\n");
    					mqlLogRequiredInformationWriter("==============================================================================\n");
    					setAdminMigrationStatus(context,migrationStatusProperty, state_MigrationComplete);
    				}
    				else
    				{
    					mqlLogRequiredInformationWriter("================================================================================\n");
        				mqlLogRequiredInformationWriter("Migrating  type "+genericDocType+"  successful with Difference in Compare COUNT\n");
        				mqlLogRequiredInformationWriter(" \n");
        				mqlLogRequiredInformationWriter("Step 3 of Migration :     COMPLETED with Difference in Compare COUNT \n");
        				mqlLogRequiredInformationWriter(" \n");
        				int count = Integer.parseInt(noOfGenericDocCount) - Integer.parseInt(noOfGenericDocWithAccess);
        				mqlLogRequiredInformationWriter("There are "+count+" '"+genericDocType+"' objects which are not Migrated\n");
        				mqlLogRequiredInformationWriter("\nLogging of this Migration will be written to: "+migrationlogName+"\n\n");
        				mqlLogRequiredInformationWriter("================================================================================\n");
    				}
    			}
    			catch(Exception e)
    			{
    				e.printStackTrace();
    			}
    			finally
    			{
    				libraryCentralCloseLogs();
    			}
    		}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	finally
    	{
    		if(convertedOidsLog != null)
    		{
    			convertedOidsLog.close();
    		}
    		if(unconvertedOidsLog != null)
    		{
    			unconvertedOidsLog.close();
    		}
    		if(writer != null)
    		{
    			writer.close();
    		}
    	}
        return 0;
    }


    /**
     * This method is executed for pre-migration changes required before starting actual Migration
     * pre migration is executed only once
     */
    public void preMigration(String[] args)
    {

    }


    public void migrateObjects(Context context, StringList objectList) throws Exception
    {
    	try
    	{
    		int lengthObjectList = objectList.size();

    		for(int objItr = 0; objItr < lengthObjectList; objItr++)
    		{
    			String objectIdUnderProcess  = objectList.get(objItr);
    			try{
    				//migration logic
    				DomainObject genDocObj = DomainObject.newInstance(context, objectIdUnderProcess);
    				String sAccessType = genDocObj.getInfo(context, "attribute["+DomainObject.ATTRIBUTE_ACCESS_TYPE+"].value");
    				if("Inherited".equalsIgnoreCase(sAccessType)){
    					SelectList selectStmts = new SelectList(1);
    			    	selectStmts.addElement(DomainObject.SELECT_ID);
    			    	 MapList parentList = genDocObj.getRelatedObjects(context,
    			    				LibraryCentralConstants.RELATIONSHIP_CLASSIFIED_ITEM,
    			    				LibraryCentralConstants.TYPE_CLASSIFICATION,
    			    				selectStmts,
    			    				new StringList(),
    			    				true, 
    			    				false, 
    			    				(short)1, 
    			    				null, 
    			    				null,
    			    				0);
    			    	 for (int k=0; k<parentList.size(); k++ ){
    			     		Map tempMap = (Map)parentList.get(k);
    			     		String objIdparent =(String)tempMap.get(DomainObject.SELECT_ID);
    			     		MqlUtil.mqlCommand(context, "modify bus $1 add access bus $2 for $3 as $4,$5", true, objectIdUnderProcess, objIdparent, "Classification Access Inheritance", "read","show");
    			    	 }
    				}
    				//log Success
    				loadMigratedOids(objectIdUnderProcess);
    			}
    			catch(Exception e)
    			{
    				writeUnconvertedOID(context,objItr,objectIdUnderProcess,e.getMessage());
    			}
    		}
    	}
    	catch(Exception e)
    	{
    		mqlLogRequiredInformationWriter(e.getMessage());
    		throw e;
    	}
    }


    
    /**
     * this method writes Error Log
     * @param context the eMatrix <code>Context</code> object
     * @param objectId object Id of the object that failed
     * @param reason specifies the Reason for failure
     * @return nothing
     * @throws Exception if the operation fails
     */
    public void writeUnconvertedOID(Context context,int rowNo, String objectId, String reason) throws Exception
    {
        int currentRunNo     = getAdminMigrationStatus(context, migrationRunNoProperty);
        StringBuffer command = new StringBuffer();
        command.append(currentRunNo+",");
        command.append(objectId+",");
        command.append("objectids_"+flatFileNoUnderProcess+".txt,");
        command.append((rowNo+1)+",");

        reason = reason.replaceAll(",", ".");
        reason = reason.replaceAll("\\n", "|");
        reason = reason.replaceAll("\\r", "");
        command.append(reason);
        command.append("\n");
        failureId = objectId;

        if(unconvertedObjectCount < unconvertedChunkSize)
        {
            errorLog.write(command.toString());
            errorLog.flush();
            unconvertedObjectCount++;
        } else {
            errorLog.close();
            unconvertedObjectCount = 1;
            unconvertedFileCount ++;
            errorLog   = new FileWriter(documentDirectory + failedObjectIdsLogNamePrefix+ unconvertedFileCount +failedObjectIdsLogNameSuffix, true);
            errorLog.write("RUN NO,OBJECTID,FILE NAME,ROW NO,REASON \n");
            errorLog.write(command.toString());
            errorLog.flush();
        }
    }
    
    
    
    /**
     * Sets the migration status as a property setting.
     * @param context the eMatrix <code>Context</code> object
     * @param strStatus String containing the status setting
     * @throws Exception
     */
    public void setAdminMigrationStatus(Context context,String name, String strStatus) throws Exception
    {
        if(name.equals(migrationRunNoProperty))
        {
            if(strStatus.equalsIgnoreCase("increment"))
            {
                // If name is LibraryCentalDocumentSheetMigrationR215RunNo and strStatus increment then value of LibraryCentalDocumentSheetMigrationR215RunNo is incremented
                int lastRunNo = getAdminMigrationStatus(context, name);
                MqlUtil.mqlCommand(context, "modify program $1 property $2 value $3", program_SystemInformation, name, (lastRunNo+1)+"");
            }
            else
            {
                MqlUtil.mqlCommand(context, "modify program $1 property $2 value $3", program_SystemInformation, name, "0");
            }
        }
        else
        {
            MqlUtil.mqlCommand(context, "modify program $1 property $2 value $3", program_SystemInformation, name, strStatus);
        }
    }


    /**
     * Gets the migration status as an integer value.  Used to enforce an order of migration.
     * @param context the eMatrix <code>Context</code> object
     * @return integer representing the status
     * @throws Exception
     */
    public int getAdminMigrationStatus(Context context,String name) throws Exception
    {
        if(name.equals(migrationRunNoProperty))
        {
            String result = MqlUtil.mqlCommand(context, "print program $1 select $2 dump", program_SystemInformation, "property["+name+"].value");
            try
            {
                return Integer.parseInt(result);
            }
            catch(NumberFormatException nfe)
            {
                return 0;
            }
        }

        String result   = MqlUtil.mqlCommand(context, "print program $1 select $2 dump", program_SystemInformation, "property["+name+"].value");

        if(result.equalsIgnoreCase(state_PreMigrationInProcess))
        {
            return 1;
        }
        else if(result.equalsIgnoreCase(state_PreMigrationComplete))
        {
            return 2;
        }
        else if(result.equalsIgnoreCase(state_MigrationInProcess))
        {
            return 3;
        }
        else if(result.equalsIgnoreCase(state_MigrationComplete))
        {
            return 4;
        }
        else if(result.equalsIgnoreCase(state_PostMigrationInProcess))
        {
            return 4;
        }
        else if(result.equalsIgnoreCase(state_PostMigrationComplete))
        {
            return 4;
        }
        return 0;
    }


    public void writeRunNoToLogs(Context context)throws Exception
    {
        libraryCentralCreateLogs(context);
        int currentRunNo = getAdminMigrationStatus(context, migrationRunNoProperty);

        mqlLogRequiredInformationWriter("==================================================================\n");
        mqlLogRequiredInformationWriter("Run No: "+currentRunNo+"\n");
        mqlLogRequiredInformationWriter("==================================================================\n");

        if(currentRunNo == 1)
        {
            errorLog.write("RUN NO,OBJECTID,FILE NAME,ROW NO,REASON \n");
        }

        statusLog.write("\nRun No: "+currentRunNo+"\n");
        convertedOidsLog.write("\nRun No: "+currentRunNo+"\n");

        libraryCentralCloseLogs();
    }



    public void libraryCentralCreateLogs(Context context)throws Exception
    {
        writer            = new BufferedWriter(new MatrixWriter(context));
        warningLog        = new FileWriter(documentDirectory + migrationlogName, true);
        statusLog         = new FileWriter(documentDirectory + fileStatusLogName, true);
        convertedOidsLog  = new FileWriter(documentDirectory + migratedObjectIdsLogName, true);
        errorLog          = new FileWriter(documentDirectory + failedObjectIdsLogNamePrefix+ unconvertedFileCount +failedObjectIdsLogNameSuffix, true);
    }


    /**
     * This method sets flatFileNoUnderProcess and
     * Calls super method which Reads the contents of the file and puts in Arraylist.
     *
     * @param args i holds the suffux of filename to identify the file.
     * @returns ArrayList of objectIds present in the file
     * @throws Exception if the operation fails
     */
    public StringList readFiles(int i)throws Exception
    {
        flatFileNoUnderProcess = i;
        return super.readFiles(i);
    }


    /**
     * this method Closes all the logs created
     * @return nothing
     * @throws Exception if the operation fails
     */
    public void libraryCentralCloseLogs()throws Exception
    {
        convertedOidsLog.flush();
        convertedOidsLog.close();

        statusLog.flush();
        statusLog.close();

        warningLog.flush();
        warningLog.close();

        writer.flush();
        writer.close();

        errorLog.flush();
        errorLog.close();
    }
}
