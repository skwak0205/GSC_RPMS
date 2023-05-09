/*
**  emxDomainAccess
**
**  Copyright (c) 1992-2020 Dassault Systemes.
**  All Rights Reserved.
**  This program contains proprietary and trade secret information of MatrixOne,
**  Inc.  Copyright notice is precautionary only
**  and does not evidence any actual or intended publication of such program.
**
*/

import java.util.*;
import java.util.Map.Entry;

import com.matrixone.apps.domain.DomainAccess;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.framework.ui.UIUtil;
import com.matrixone.apps.program.ProgramCentralUtil;

import matrix.db.*;
import matrix.util.StringList;


/**

 */
public class emxDomainAccess_mxJPO extends emxDomainAccessBase_mxJPO {


    /**
     * Constructor.
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @throws Exception if the operation
     * @since V6R2011x
     * @grade 0
     */
    public emxDomainAccess_mxJPO (Context context, String[] args)
        throws Exception
    {
        super(context, args);
    }

    @com.matrixone.apps.framework.ui.ProgramCallable
    static public MapList getObjectAccessList(Context context, String[] args) throws Exception {
        Map<?, ?> programMap = (Map<?, ?>) JPO.unpackArgs(args);
        String id = (String) programMap.get("objectId");
        if (id == null) {
            Map<?, ?> paramList = (Map<?, ?>) programMap.get("paramList");
            id = (String) paramList.get("objectId");
        }
        if (id.indexOf(':') != -1) { //if id is one of the access masks, then ignore.
            return new MapList();
        }
        MapList results = DomainAccess.getAccessSummaryList(context, id);
        DomainObject domainObject = new DomainObject(id);
        if(domainObject.isKindOf(context, DomainConstants.TYPE_WORKSPACE_VAULT)){
            String wVaultOwner = domainObject.getInfo(context, DomainConstants.SELECT_OWNER);
            String[] param = {id};
            String workspaceId ="";
            workspaceId= (String)JPO.invoke(context, "emxWorkspaceFolder", null, "getProjectId", JPO.packArgs(param), Object.class);
            if(UIUtil.isNotNullAndNotEmpty(workspaceId)){
                DomainObject workspaceObj = new DomainObject(workspaceId);
                String wsOwner = workspaceObj.getInfo(context, DomainConstants.SELECT_OWNER);
                wsOwner += "_PRJ";
                wVaultOwner += "_PRJ";
                Iterator resultsItr = results.iterator();
                while(resultsItr.hasNext()){
                    Map mapObjects = (Map) resultsItr.next();
                    String member = (String)mapObjects.get(DomainConstants.SELECT_NAME);
                    if(UIUtil.isNotNullAndNotEmpty(member)&& (wsOwner.equals(member) || wVaultOwner.equals(member))){
                        mapObjects.put("disableSelection","true");
                    }else{
                        mapObjects.put("disableSelection","false");
                    }
                }
            }
        }
        //MapList results = getAccessSummaryList(context, id);
        return results;
    }

    static private StringList getTableData(Context context, String[] args, String key) throws Exception {
        Map<?, ?> programMap = (Map<?, ?>) JPO.unpackArgs(args);
        MapList objectListFromArgs = (MapList) programMap.get("objectList");

        Map<?, ?> paramList = (Map<?, ?>) programMap.get("paramList");
        String ctxObjId = (String) paramList.get("objectId");

        MapList objectListFromDB = null;
        if(DomainAccess.KEY_ACCESS_GRANTED.equalsIgnoreCase(key)) {
            objectListFromDB = DomainAccess.getAccessSummaryList(context, ctxObjId);
        }else {
            objectListFromDB = objectListFromArgs;
        }

        int size = objectListFromDB.size();

        HashMap<String, String> colValuesFromDB = new HashMap<String, String>();
        for(int i = 0; i < size; i++) {
            Map<?, ?> mapFromDB = (Map<?, ?>) objectListFromDB.get(i);
            String uniqueRowIdFromDB = (String) mapFromDB.get(DomainConstants.SELECT_ID);
            String colValueFromDB = (String) mapFromDB.get(key);
            if(DomainAccess.KEY_ACCESS_PROJECT.equalsIgnoreCase(key)) {
                colValueFromDB=(String) mapFromDB.get("projectTitle");
            }
            colValuesFromDB.put(uniqueRowIdFromDB, colValueFromDB);
        }

        StringList results = new StringList(objectListFromArgs.size());
        for (int i=0; i < objectListFromArgs.size(); i++) {
            Map<?, ?> mapFromArgs = (Map<?, ?>) objectListFromArgs.get(i);
            String uniqueRowIdFromArgs = (String) mapFromArgs.get(DomainConstants.SELECT_ID);

            results.addElement(colValuesFromDB.get(uniqueRowIdFromArgs));
        }
        return results;
    }

    // HJ - getgscRatio
    static public Vector getgscRatio(Context context, String[] args) throws Exception {
        Map programMap = (Map) JPO.unpackArgs(args);
        MapList objectList = (MapList) programMap.get("objectList");

        Vector returnVector = new Vector();

        int objsize = objectList.size();
        String[] objIdArr = new String[objsize];
        String[] objUserNameArr= new String[objsize];
        for (int i = 0; i < objsize; i++) {
            Map objectMap = (Map) objectList.get(i);
            objIdArr[i] = (String) objectMap.get(DomainObject.SELECT_ID);
            objUserNameArr[i] = (String) objectMap.get("username");
        }

        // �θ� Object ���� Relation Member ID ��ȸ
        String mqlret = MqlUtil.mqlCommand(context,"print bus $1 select $2 dump",objIdArr[0],"relationship[Member].id");

        StringList relIdList = new StringList();
        if(mqlret.contains(",")) {
            relIdList = FrameworkUtil.split(mqlret,",");
            if(relIdList.size() == (objsize-1))
                relIdList.add(relIdList.size(),"-");
        }else{
            relIdList.add(0,mqlret);
            relIdList.add(1,"-");
        }

        for(int index = 0; index < objsize; index++) {
            HashMap cellMap = new HashMap();
            String gscvalue = "";
            String gscvalueDisp = "";
            if(relIdList.get(index).equalsIgnoreCase("-")){
                // returnVector.add("-");
                gscvalueDisp = "-";
            }else{
                String mqlgscRatioValue = MqlUtil.mqlCommand(context,"print connection $1 select $2 dump",relIdList.get(index),"attribute[gscRatio].value");
                // returnVector.add(mqlgscRatioValue);
                gscvalueDisp = mqlgscRatioValue;
            }
            cellMap.put("ActualValue",relIdList.get(index)+"_"+gscvalueDisp);
            cellMap.put("DisplayValue",gscvalueDisp);
            returnVector.add(cellMap);
        }

        // bus Member ��
//        if(relIdList.size() == objsize){
//            for(int index = 0; index < objsize; index++) {
//                String mqlbusValue = MqlUtil.mqlCommand(context,"print connection $1 select $2 dump",relIdList.get(index),"businessobject");
//                if(objUserNameArr[index].equalsIgnoreCase(mqlbusValue)){
//                    if(relIdList.get(index).equalsIgnoreCase("-")){
//                        returnVector.add("-");
//                    }else{
//                        String mqlgscRatioValue = MqlUtil.mqlCommand(context,"print connection $1 select $2 dump",relIdList.get(index),"attribute[gscRatio].value");
//                        returnVector.add(mqlgscRatioValue);
//                    }
//                }
//            }
//        }

        return returnVector;
    }

    // HJ - updategscRatioValue (updateAccessValue ����)
    public void updategscRatioValue(Context context, String[] args)throws Exception {
        Map<?, ?> programMap        = (Map<?, ?>) JPO.unpackArgs(args);
        HashMap<?, ?> requestMap    = (HashMap<?, ?>) programMap.get("requestMap");
        HashMap<?, ?> columnMap    = (HashMap<?, ?>) programMap.get("columnMap");
        HashMap<?, ?> paramMap  = (HashMap<?, ?>) programMap.get("paramMap"); // paramMap - [{languageStr},{ko-KR}]
        String objectId     = (String) requestMap.get("objectId"); // 15741.36097.2221.65017 - Project Space
        String parentOID     = (String) requestMap.get("parentOID");
        String ActualValue = (String) requestMap.get("selectedTable");

        String updateValue     = (String)paramMap.get("New Value"); // Actual Row Value
        String dataDetails    = (String)paramMap.get("objectId"); // 15741.36097.2221.65017::project_user2_PRJ:Multiple Ownership For Object
        StringList dataDetailsList = StringUtil.split(dataDetails, ":");
        String exp_rel = (String)  columnMap.get("expression_relationship");

        String TestreqValue = (String) requestMap.get("RequestValuesMap"); // TestreqValue = null

        String userName = dataDetailsList.get(2).replace("_PRJ","");
        Map<String, String> relMember = new HashMap<>();

        String TESTValue = "";

        // for loop (entrySet())
        for (Entry<?, ?> entrySet : requestMap.entrySet()) {
//            TESTValue += entrySet.getKey() + " : " + entrySet.getValue();
            TESTValue += entrySet.getKey() + "/";
//            TESTValue += entrySet.getValue() + "/";
        }

        TESTValue += programMap.size()+"/"+exp_rel+"/"+parentOID+"/"+objectId+"/"+ActualValue;

        emxUtil_mxJPO utilityClass = new emxUtil_mxJPO(context, null);
        String[] command = new String[1];

        // �θ� Object ���� Relation Member ID ��ȸ
        if(updateValue != "-"){
            String mqlrelMemberConId = MqlUtil.mqlCommand(context,"print bus $1 select $2 dump",objectId,"relationship[Member].id");
            String mqlrelMemberName = MqlUtil.mqlCommand(context,"print bus $1 select $2 dump",objectId,"relationship[Member].businessobject");

            StringList relMemberIdList = new StringList();
            StringList relMemberNameList = new StringList();
            if(mqlrelMemberConId.contains(",")) {
                relMemberIdList = FrameworkUtil.split(mqlrelMemberConId,",");
                relMemberNameList = FrameworkUtil.split(mqlrelMemberName,",");
                for(int index=0; index < relMemberIdList.size(); index++) {
                    relMember.put(relMemberNameList.get(index), relMemberIdList.get(index));
                }
                relMember.put("-","-");
            }else{
                relMember.put(mqlrelMemberName,mqlrelMemberConId);
                relMember.put("-","-");
            }

            String relConId = relMember.get(userName);

            command[0] = "mod connection " + relConId + "gscRatio "+ TESTValue;
            utilityClass.executeMQLCommands(context, command);
        }
    }

    // HJ - TESTgetgscRatio
    public StringList TESTgetgscRatio(Context context, String[] args) throws Exception {
        try {
            Map programMap = (Map) JPO.unpackArgs(args);
            MapList objectList = (MapList) programMap.get("objectList");

            Vector returnVector = new Vector();

            int objsize = objectList.size();
            String[] objIdArr = new String[objsize];
            String[] objUserNameArr= new String[objsize];
            for (int i = 0; i < objsize; i++) {
                Map objectMap = (Map) objectList.get(i);
                objIdArr[i] = (String) objectMap.get(DomainObject.SELECT_ID);
                objUserNameArr[i] = (String) objectMap.get("username");
            }

            // �θ� Object ���� Relation Member ID ��ȸ
            String mqlret = MqlUtil.mqlCommand(context,"print bus $1 select $2 dump",objIdArr[0],"relationship[Member].id");

            StringList relIdList = new StringList();
            if(mqlret.contains(",")) {
                relIdList = FrameworkUtil.split(mqlret,",");
                if(relIdList.size() == (objsize-1))
                    relIdList.add(relIdList.size(),"-");
            }else{
                relIdList.add(0,mqlret);
                relIdList.add(1,"-");
            }

            for(int index = 0; index < objsize; index++) {
                if(relIdList.get(index).equalsIgnoreCase("-")){
                    returnVector.add("-");
                }else{
                    String mqlgscRatioValue = MqlUtil.mqlCommand(context,"print connection $1 select $2 dump",relIdList.get(index),"attribute[gscRatio].value");
                    returnVector.add(mqlgscRatioValue);
                }
            }
//            String key = DomainAccess.KET_ACCESS_ORGTITLE;

            String key = "orgTitle";

            Map<?, ?> paramList = (Map<?, ?>) programMap.get("paramList");
            String ctxObjId = (String) paramList.get("objectId");

            MapList objectListFromDB = null;
            if(DomainAccess.KEY_ACCESS_GRANTED.equalsIgnoreCase(key)) {
                objectListFromDB = DomainAccess.getAccessSummaryList(context, ctxObjId);
            }else {
                objectListFromDB = objectList;
            }

            int size = objectListFromDB.size();
            StringList results = new StringList(objectList.size());

            HashMap<String, String> colValuesFromDB = new HashMap<String, String>();
            for(int i = 0; i < size; i++) {
                Map<?, ?> mapFromDB = (Map<?, ?>) objectListFromDB.get(i);
                // uniqueRowIdFromDB == id (ex: 		15741.36097.2221.65017:Company Name:RPMS:Primary)
                String uniqueRowIdFromDB = (String) mapFromDB.get(DomainConstants.SELECT_ID);
                String colValueFromDB = (String) mapFromDB.get(key);
                // String colValueFromDB = (String) returnVector.get(i);

                colValuesFromDB.put(uniqueRowIdFromDB, colValueFromDB);
                results.addElement(uniqueRowIdFromDB);
            }

            for (int i=0; i < objectList.size(); i++) {
                Map<?, ?> mapFromArgs = (Map<?, ?>) objectList.get(i);
                // uniqueRowIdFromArgs == id
                String uniqueRowIdFromArgs = (String) mapFromArgs.get(DomainConstants.SELECT_ID);

                // results.addElement(colValuesFromDB.get(uniqueRowIdFromArgs));
            }

            return results;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
