import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.matrixone.apps.domain.DomainAccess;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.MqlUtil;
import com.matrixone.apps.requirements.RequirementsUtil;

import matrix.db.Context;
import matrix.util.StringList;

/**
 * @author QYG
 *
 */

 /**
 *	@quickreview QYG 2017:07:06 : Migrate SOV bits with new DomainAccess.xml patterns
 *	@quickreview GVC AGM1 2021:21:04 : IR-817979 -> Exception during ownership migration script
 */
@SuppressWarnings("unchecked")
public class emxRequirementOwnershipMigrationBase_mxJPO extends
		emxCommonMigrationBase_mxJPO {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8679068394307678964L;
	private static final String SELECT_OWNERSHIP = "ownership";
    private static final String SELECT_OWNERSHIP_ACCESS = "ownership.access";

   /* static
    {
        DomainObject.MULTI_VALUE_LIST.add(SELECT_OWNERSHIP);
        DomainObject.MULTI_VALUE_LIST.add(SELECT_OWNERSHIP_ACCESS);
    }*/

    protected String sReader = "";
    protected String sAuthor = "";
	/**
	 * @param context
	 * @param args
	 * @throws Exception
	 */
	public emxRequirementOwnershipMigrationBase_mxJPO(Context context,
			String[] args) throws Exception {
		super(context, args);
		
        String policy = RequirementsUtil.getRequirementPolicy(context);
        try{
	        StringList logicalNames = DomainAccess.getLogicalNamesForPolicy(context, policy);
	        sReader = DomainAccess.getPhysicalAccessMasksForPolicy(context, policy, (String)logicalNames.get(0));
	        sAuthor = DomainAccess.getPhysicalAccessMasksForPolicy(context, policy, (String)logicalNames.get(1));
        }catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
	}

	@SuppressWarnings({ "deprecation" })
    public void migrateObjects(Context context, StringList objectIdList) throws Exception
    {

        StringList objectSelects = new StringList(4);

        objectSelects.add(DomainConstants.SELECT_ID);
        objectSelects.add(DomainConstants.SELECT_NAME);
        //objectSelects.add(SELECT_OWNERSHIP);
        //objectSelects.add(SELECT_OWNERSHIP_ACCESS);
        
		Collection multiValueList = new HashSet(2);
		multiValueList.add(SELECT_OWNERSHIP);
		multiValueList.add(SELECT_OWNERSHIP_ACCESS);
		
        String[] oidsArray = (String[])objectIdList.toArray(new String[]{});
        MapList objectList = DomainObject.getInfo(context, oidsArray, objectSelects, multiValueList);

        String physicalMask, key, access, org , role, cmd;
        StringList tokens;
        Map<?, ?> m;
        Iterator<String> ownershipItr;

        boolean bChanged;
        Iterator<?> itr = objectList.iterator();
        while (itr.hasNext())
        {
            m = (Map<?, ?>)itr.next();

            String objectId = (String)m.get(DomainConstants.SELECT_ID);
            String objectName = (String)m.get(DomainConstants.SELECT_NAME);
            StringList ownership = (StringList)m.get(SELECT_OWNERSHIP);
            
        	bChanged = false;
        	physicalMask = null;

			//GVC:212104:IR-817979:attribute ownership does not always have a value.
            if (ownership != null)
			{
				ownershipItr = ownership.iterator();
				while (ownershipItr.hasNext())
				{
					key = ownershipItr.next();

					//
					// If not personal security context (i.e. xyz_PRJ) nothing to do
					//
					if (!key.contains("_PRJ")) {
						continue;
					}
                
					//
					// Get org and role (project) from ownership string
					// (needed for MQL add/remove ownership)
					//
					tokens = FrameworkUtil.splitString(key,"|");
					org = (String)tokens.remove(0);
					role = (String)tokens.remove(0);

					//
					// Get ownership.access from the map
					//
					access = (String)m.get(SELECT_OWNERSHIP + "[" + key + "].access");

					if(access.contains("revise")) {
                		if(!access.contains("majorrevise")) {
                    		physicalMask = sAuthor;
                		}
					}
					else if(!access.contains("toconnect") || !access.contains("checkout")) {
                		physicalMask = sReader;
					}
                
					if(physicalMask != null) {
						cmd = "modify bus $1 remove ownership $2 $3 for $4 as $5";
						MqlUtil.mqlCommand(context,cmd, objectId, org, role, DomainAccess.COMMENT_MULTIPLE_OWNERSHIP, "all");
                    
						cmd = "modify bus " + objectId + " add ownership '" + org + "' '" + role + "' for '" + DomainAccess.COMMENT_MULTIPLE_OWNERSHIP + "' as " + physicalMask;
						MqlUtil.mqlCommand(context,cmd);
                    
						bChanged = true;
					}
				}
			}
            if (bChanged) {
                loadMigratedOids(objectId);
            }else{
                String comment = "Skipping object <<" + objectName + ">> SOV_OK\n";
                writeUnconvertedOID(comment, objectId);
            }
        }
    }

}
