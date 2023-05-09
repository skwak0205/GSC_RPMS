//
// $Id: emxProjectConcept.java.rca 1.6 Wed Oct 22 16:21:23 2008 przemek Experimental przemek $ 
//
// emxProjectConcept.java
//
// Copyright (c) 2002-2020 Dassault Systemes.
// All Rights Reserved
// This program contains proprietary and trade secret information of
// MatrixOne, Inc.  Copyright notice is precautionary only and does
// not evidence any actual or intended publication of such program.
//

import com.exalead.enovia.api.client.command.impl.GetBusinessObjectCommand;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.program.ProgramCentralConstants;
import com.matrixone.apps.program.ProgramCentralUtil;
import com.matrixone.apps.program.ProjectConcept;
import matrix.db.*;
import matrix.util.MatrixException;
import matrix.util.StringList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 * The <code>emxProjectConcept</code> class represents the Project Concept JPO
 * functionality for the AEF type.
 *
 * @version AEF 10.0.SP4 - Copyright (c) 2002, MatrixOne, Inc.
 */
public class emxProjectConcept_mxJPO extends emxProjectConceptBase_mxJPO
{

    /**
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @throws Exception if the operation fails
     * @since AEF 10.0.SP4
     * @grade 0
     */
    public emxProjectConcept_mxJPO (Context context, String[] args)
        throws Exception
    {
      super(context, args);
    }

    private StringList getAllProjectSubTypeNames(Context context, String type) throws FrameworkException {
        StringList subTypeList = new StringList();
        //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:start
        String sCommandStatement = "print type $1 select $2 dump $3";
        String subTypes = MqlUtil.mqlCommand(context, sCommandStatement, type, "derivative", "|");
        //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:End
        if ("".equalsIgnoreCase(subTypes)) {
            subTypeList.addElement(type);
            return subTypeList;
        } else {
            subTypes = subTypes + "|" + type;
        }
        subTypeList = FrameworkUtil.split(subTypes, "|");
        return subTypeList;
    }

    // HJ
    @com.matrixone.apps.framework.ui.PostProcessCallable
    public void gscperformPostProcessActionsEdit(Context context, String[] args)
            throws Exception {
        try {
            com.matrixone.apps.program.ProjectConcept projectConcept =
                    (com.matrixone.apps.program.ProjectConcept) DomainObject.newInstance(context,
                            DomainConstants.TYPE_PROJECT_CONCEPT, DomainConstants.PROGRAM);

            com.matrixone.apps.common.Person person =
                    (com.matrixone.apps.common.Person) DomainObject.newInstance(context,
                            DomainConstants.TYPE_PERSON);

            final String ATTRIBUTE_SCHEDULE_FROM = PropertyUtil.getSchemaProperty(context, "attribute_ScheduleFrom");
            final String SELECT_ATTRIBUTE_SCHEDULE_FROM = "attribute[" + ATTRIBUTE_SCHEDULE_FROM + "]";

            final String RELATIONSHIP_CONTRIBUTES_TO = PropertyUtil.getSchemaProperty("relationship_ContributesTo");
            final String SELECT_CONTRIBUTES_TO_RELATIONSHIP_ID = "from[" + RELATIONSHIP_CONTRIBUTES_TO + "].id";

            StringList busSelects = new StringList(2);
            busSelects.add(ProjectConcept.SELECT_OWNER);
            busSelects.add(SELECT_PROJECT_ACCESS_LIST_ID);

            busSelects.add(SELECT_TYPE);
            busSelects.add(SELECT_ATTRIBUTE_SCHEDULE_FROM);
            busSelects.add(SELECT_CONTRIBUTES_TO_RELATIONSHIP_ID);

            HashMap programMap = (HashMap) JPO.unpackArgs(args);
            HashMap requestMap = (HashMap) programMap.get("requestMap");
            HashMap paramMap = (HashMap) programMap.get("paramMap");
            String objectId = (String) paramMap.get("objectId");
            String businessUnitId = (String) requestMap.get("BusinessUnitId");

            String programId = (String) requestMap.get("ProgramId");
            String deliverableId = (String) requestMap.get("DeliverableEditable");
            //Modified for Bug # 340636 on 9/7/2007 - Start
            String ProjectName = (String) requestMap.get("ProjectName");

            projectConcept.setId(objectId);
            Map projectInfo = projectConcept.getInfo(context, busSelects);

            //
            // Following code is written for coping with TaskDateRollUp algorithm. This code should be moved to Schedule From attribute
            // modify Action trigger. Its late in X+4 to change the schema, hence the code is accomodated here.
            // When a project lead changes Schedule From attribute value for the existing project, following code
            // resets the time part of the project's estimated start/finish date appropriately, so that roll up process
            // algorithm still functions correctly.
            //
            String strScheduleFrom = (String) projectInfo.get(SELECT_ATTRIBUTE_SCHEDULE_FROM);
            String existingProjectScheduleFromVal = (String) requestMap.get("Schedule FromfieldValue");

            boolean isScheduledChanged = true;
            if (ProgramCentralUtil.isNullString(existingProjectScheduleFromVal) || strScheduleFrom.equalsIgnoreCase(existingProjectScheduleFromVal)) {
                isScheduledChanged = false;
            }

            //Added:NZF:22-Dec-2011:IR-091218V6R2012
            if (isScheduledChanged) {
                com.matrixone.apps.program.Task task = (com.matrixone.apps.program.Task) DomainObject
                        .newInstance(context, DomainConstants.TYPE_TASK, "PROGRAM");
                task.setId(objectId);
                task.rollupAndSave(context);
            }

            //End:NZF:22-Dec-2011:IR-091218V6R2012

            String strType = (String) projectInfo.get(SELECT_TYPE);
            StringList prjSpaceSubTypes = getAllProjectSubTypeNames(context, DomainConstants.TYPE_PROJECT_SPACE);
            StringList prjConceptSubTypes = getAllProjectSubTypeNames(context, DomainConstants.TYPE_PROJECT_CONCEPT);
            String strProjType = "";
            // [MODIFIED::Jan 20, 2011:S4E:R211:TypeAhead::Start]
            String strFieldName = "";
            if (prjSpaceSubTypes.contains(strType)) {
                strProjType = DomainConstants.TYPE_PROJECT_SPACE;
                strFieldName = "ProjectSpaceOwner";
            } else if (prjConceptSubTypes.contains(strType)) {
                strProjType = DomainConstants.TYPE_PROJECT_CONCEPT;
                strFieldName = "ProjectConceptOwner";
            }
            String ownerName = (String) requestMap.get(strFieldName);
            // [MODIFIED::Jan 20, 2011:S4E:R211:TypeAhead::End]
            BusinessType busType = new BusinessType(strProjType, context.getVault());
            if (busType.hasChildren(context)) {
                String ActualType = (String) requestMap.get("ActualType");
                boolean isContextPushed = false;
                if (!(projectConcept.checkAccess(context, (short) AccessConstants.cModify))) {
                    ContextUtil.pushContext(context);
                    isContextPushed = true;
                }
                if (ActualType != null && !ActualType.isEmpty()) {
                    projectConcept.setType(context, ActualType, objectId, projectConcept.getPolicy(context).toString());
                }
                if (isContextPushed)
                    ContextUtil.popContext(context);
            }

            //Added for Change Discipline
            boolean isECHInstalled = com.matrixone.apps.domain.util.FrameworkUtil.isSuiteRegistered(context, "appVersionEnterpriseChange", false, null, null);
            if (isECHInstalled) {
                if (projectConcept.isKindOf(context, PropertyUtil.getSchemaProperty(context, "type_ChangeProject"))) {
                    String strInterfaceName = PropertyUtil.getSchemaProperty(context, "interface_ChangeDiscipline");
                    //Check if an the change discipline interface has been already connected
                    //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:start
                    String sCommandStatement = "print bus $1 select $2 dump";
                    String sIsInterFacePresent = MqlUtil.mqlCommand(context, sCommandStatement, objectId, "interface[" + strInterfaceName + "]");
                    //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:End

                    //If no interface --> add one
                    if ("false".equalsIgnoreCase(sIsInterFacePresent)) {
                        //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:start
                        sCommandStatement = "modify bus $1 add interface $2";
                        MqlUtil.mqlCommand(context, sCommandStatement, objectId, strInterfaceName);
                        //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:End
                    }

                    BusinessInterface busInterface = new BusinessInterface(strInterfaceName, context.getVault());
                    AttributeTypeList listInterfaceAttributes = busInterface.getAttributeTypes(context);

                    Iterator listInterfaceAttributesItr = listInterfaceAttributes.iterator();
                    while (listInterfaceAttributesItr.hasNext()) {
                        String attrName = ((AttributeType) listInterfaceAttributesItr.next()).getName();
                        String attrNameSmall = attrName.replaceAll(" ", "");
                        String attrNameSmallHidden = attrNameSmall + "Hidden";
                        String attrNameValue = (String) requestMap.get(attrNameSmallHidden);

                        if (attrNameValue != null && !attrNameValue.equalsIgnoreCase("")) {
                            projectConcept.setAttributeValue(context, attrName, attrNameValue);
                        } else {
                            projectConcept.setAttributeValue(context, attrName, "No");
                        }
                    }
                }
            }
            //End Added for Change Discipline

            //Modified for Bug # 340636 on 9/7/2007 - End
            /*Map projectInfo = projectConcept.getInfo(context,busSelects);*/
            String oldOwner = (String) projectInfo.get(ProjectConcept.SELECT_OWNER);
            String accessObjectId = (String) projectInfo.get(SELECT_PROJECT_ACCESS_LIST_ID);
            DomainObject accessObject = DomainObject.newInstance(context, accessObjectId);
            if (ownerName != null && !ownerName.equals("") && !ownerName.equals("null")) {
                projectConcept.setOwner(context, ownerName);
                accessObject.setOwner(context, ownerName);
            }

            person = com.matrixone.apps.common.Person.getPerson(context);

            // get the company id for this context
            com.matrixone.apps.common.Company company = person.getCompany(context);

            // HJ - RelatedObjects (objectId = projectConceptId)
            String sCommandStatement = "print bus $1 select $2 dump;";
            String strRelBusUnitPro = MqlUtil.mqlCommand(context, sCommandStatement, objectId, "relationship[" + RELATIONSHIP_BUSINESS_UNIT_PROJECT + "].fromall");
            strRelBusUnitPro = strRelBusUnitPro.replace("B","");
            StringList strlistRelBusUnitPro = FrameworkUtil.split(strRelBusUnitPro,",");

            // Old Relation 전부 삭제
            if (strRelBusUnitPro != null) {
                if(strlistRelBusUnitPro.size() > 1){
                    for (int i = 0; i < strlistRelBusUnitPro.size(); i++) {
                        projectConcept.setRelatedObject(context, RELATIONSHIP_BUSINESS_UNIT_PROJECT, false, null);
                    }
                }else{
                    projectConcept.setRelatedObject(context, RELATIONSHIP_BUSINESS_UNIT_PROJECT, false, null);
                }
            }

            if (businessUnitId != null && !businessUnitId.equals("") && !businessUnitId.equals("null")) {
                // HJ TEST (원본)
                /* projectConcept.setBusinessUnit(context, businessUnitId); */
                if (businessUnitId.contains(",")) {
                    // request businessUnitId
                    StringList listbusUnit = FrameworkUtil.split(businessUnitId,",");
                    for(int nCount=0;nCount<listbusUnit.size();nCount++) {
                        if(nCount==0){
                            projectConcept.setBusinessUnit(context, listbusUnit.get(nCount));
                        }else{
                            projectConcept.addRelatedObject(context, new RelationshipType(DomainConstants.RELATIONSHIP_BUSINESS_UNIT_PROJECT), true, listbusUnit.get(nCount));
                        }
                    }
                } else {
                    projectConcept.setBusinessUnit(context, businessUnitId);
                }
            } else {
                // HJ - Multi Relation Setting
                projectConcept.setBusinessUnit(context, null);
                // Multi Relation 제거
//                if (strRelBusUnitPro != null) {
//                    if(strlistRelBusUnitPro.size() > 1){
//                        for (int i = 0; i < strlistRelBusUnitPro.size(); i++) {
//                            projectConcept.setRelatedObject(context, RELATIONSHIP_BUSINESS_UNIT_PROJECT, false, null);
//                        }
//                    }else{
//                        projectConcept.setRelatedObject(context, RELATIONSHIP_BUSINESS_UNIT_PROJECT, false, null);
//                    }
//                }
            }

            // Connect Program
            if (programId != null && !programId.equals("") && !programId.equals("null")) {
                projectConcept.setProgram(context, programId);
            } else {
                projectConcept.setProgram(context, null);
            }

            // Connect Deliverable
            String strContributesToId = (String) projectInfo.get(SELECT_CONTRIBUTES_TO_RELATIONSHIP_ID);
            if (ProgramCentralUtil.isNotNullString(deliverableId)) {
                DomainObject deliverable = DomainObject.newInstance(context, deliverableId);

                if (ProgramCentralUtil.isNotNullString(strContributesToId))
                    DomainRelationship.setToObject(context, strContributesToId, deliverable);
                else
                    DomainRelationship.connect(context, projectConcept, RELATIONSHIP_CONTRIBUTES_TO, deliverable);
            } else {
                if (ProgramCentralUtil.isNotNullString(strContributesToId))
                    DomainRelationship.disconnect(context, strContributesToId);
            }

            if (ownerName != null && !ownerName.equals("") && !ownerName.equals("null") && (!ownerName.equals(oldOwner))) {
                Access accessMask = new Access();
                //accessMask.setReadAccess(true);
                //accessMask.setShowAccess(true);
                //Added 337605_1
                accessMask.setModifyAccess(true);
                accessMask.setExecuteAccess(true);
                accessMask.setUser(oldOwner);

                StringList userList = new StringList(1);
                userList.add(oldOwner);

                BusinessObjectList objects = new BusinessObjectList(1);
                objects.add(accessObject);

                boolean isContextPushed = false;

                ContextUtil.pushContext(context);
                isContextPushed = true;
                //revoke all access on the PAL object for the Old Owner
                BusinessObject.revokeAccessRights(context, objects, userList);
                //Grant the required access on the PAL object to the Old Owner
                BusinessObject.grantAccessRights(context, objects, accessMask);
                if (isContextPushed)
                    ContextUtil.popContext(context);
            }
        } catch (Exception ee) {
            String strMsg = EnoviaResourceBundle.getProperty(context, ProgramCentralConstants.PROGRAMCENTRAL,
                    "emxProgramCentral.Experiment.ModifyAsscess", context.getSession().getLanguage());

            MqlUtil.mqlCommand(context, "warning $1", strMsg);
            ee.printStackTrace();
        }


    }

    @com.matrixone.apps.framework.ui.PostProcessCallable
    public void TESTperformPostProcessActionsEdit(Context context, String[] args)
            throws Exception {
        try {
            com.matrixone.apps.program.ProjectConcept projectConcept =
                    (com.matrixone.apps.program.ProjectConcept) DomainObject.newInstance(context,
                            DomainConstants.TYPE_PROJECT_CONCEPT, DomainConstants.PROGRAM);

            com.matrixone.apps.common.Person person =
                    (com.matrixone.apps.common.Person) DomainObject.newInstance(context,
                            DomainConstants.TYPE_PERSON);

            final String ATTRIBUTE_SCHEDULE_FROM = PropertyUtil.getSchemaProperty(context, "attribute_ScheduleFrom");
            final String SELECT_ATTRIBUTE_SCHEDULE_FROM = "attribute[" + ATTRIBUTE_SCHEDULE_FROM + "]";

            final String RELATIONSHIP_CONTRIBUTES_TO = PropertyUtil.getSchemaProperty("relationship_ContributesTo");
            final String SELECT_CONTRIBUTES_TO_RELATIONSHIP_ID = "from[" + RELATIONSHIP_CONTRIBUTES_TO + "].id";

            StringList busSelects = new StringList(2);
            busSelects.add(ProjectConcept.SELECT_OWNER);
            busSelects.add(SELECT_PROJECT_ACCESS_LIST_ID);

            busSelects.add(SELECT_TYPE);
            busSelects.add(SELECT_ATTRIBUTE_SCHEDULE_FROM);
            busSelects.add(SELECT_CONTRIBUTES_TO_RELATIONSHIP_ID);

            HashMap programMap = (HashMap) JPO.unpackArgs(args);
            HashMap requestMap = (HashMap) programMap.get("requestMap");
            HashMap paramMap = (HashMap) programMap.get("paramMap");
            String objectId = (String) paramMap.get("objectId");
            String businessUnitId = (String) requestMap.get("BusinessUnitId");

            String programId = (String) requestMap.get("ProgramId");
            String deliverableId = (String) requestMap.get("DeliverableEditable");
            //Modified for Bug # 340636 on 9/7/2007 - Start
            String ProjectName = (String) requestMap.get("ProjectName");

            projectConcept.setId(objectId);
            Map projectInfo = projectConcept.getInfo(context, busSelects);

            //
            // Following code is written for coping with TaskDateRollUp algorithm. This code should be moved to Schedule From attribute
            // modify Action trigger. Its late in X+4 to change the schema, hence the code is accomodated here.
            // When a project lead changes Schedule From attribute value for the existing project, following code
            // resets the time part of the project's estimated start/finish date appropriately, so that roll up process
            // algorithm still functions correctly.
            //
            String strScheduleFrom = (String) projectInfo.get(SELECT_ATTRIBUTE_SCHEDULE_FROM);
            String existingProjectScheduleFromVal = (String) requestMap.get("Schedule FromfieldValue");

            boolean isScheduledChanged = true;
            if (ProgramCentralUtil.isNullString(existingProjectScheduleFromVal) || strScheduleFrom.equalsIgnoreCase(existingProjectScheduleFromVal)) {
                isScheduledChanged = false;
            }

            //Added:NZF:22-Dec-2011:IR-091218V6R2012
            if (isScheduledChanged) {
                com.matrixone.apps.program.Task task = (com.matrixone.apps.program.Task) DomainObject
                        .newInstance(context, DomainConstants.TYPE_TASK, "PROGRAM");
                task.setId(objectId);
                task.rollupAndSave(context);
            }

            //End:NZF:22-Dec-2011:IR-091218V6R2012

            String strType = (String) projectInfo.get(SELECT_TYPE);
            StringList prjSpaceSubTypes = getAllProjectSubTypeNames(context, DomainConstants.TYPE_PROJECT_SPACE);
            StringList prjConceptSubTypes = getAllProjectSubTypeNames(context, DomainConstants.TYPE_PROJECT_CONCEPT);
            String strProjType = "";
            // [MODIFIED::Jan 20, 2011:S4E:R211:TypeAhead::Start]
            String strFieldName = "";
            if (prjSpaceSubTypes.contains(strType)) {
                strProjType = DomainConstants.TYPE_PROJECT_SPACE;
                strFieldName = "ProjectSpaceOwner";
            } else if (prjConceptSubTypes.contains(strType)) {
                strProjType = DomainConstants.TYPE_PROJECT_CONCEPT;
                strFieldName = "ProjectConceptOwner";
            }
            String ownerName = (String) requestMap.get(strFieldName);
            // [MODIFIED::Jan 20, 2011:S4E:R211:TypeAhead::End]
            BusinessType busType = new BusinessType(strProjType, context.getVault());
            if (busType.hasChildren(context)) {
                String ActualType = (String) requestMap.get("ActualType");
                boolean isContextPushed = false;
                if (!(projectConcept.checkAccess(context, (short) AccessConstants.cModify))) {
                    ContextUtil.pushContext(context);
                    isContextPushed = true;
                }
                if (ActualType != null && !ActualType.isEmpty()) {
                    projectConcept.setType(context, ActualType, objectId, projectConcept.getPolicy(context).toString());
                }
                if (isContextPushed)
                    ContextUtil.popContext(context);
            }

            //Added for Change Discipline
            boolean isECHInstalled = com.matrixone.apps.domain.util.FrameworkUtil.isSuiteRegistered(context, "appVersionEnterpriseChange", false, null, null);
            if (isECHInstalled) {
                if (projectConcept.isKindOf(context, PropertyUtil.getSchemaProperty(context, "type_ChangeProject"))) {
                    String strInterfaceName = PropertyUtil.getSchemaProperty(context, "interface_ChangeDiscipline");
                    //Check if an the change discipline interface has been already connected
                    //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:start
                    String sCommandStatement = "print bus $1 select $2 dump";
                    String sIsInterFacePresent = MqlUtil.mqlCommand(context, sCommandStatement, objectId, "interface[" + strInterfaceName + "]");
                    //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:End

                    //If no interface --> add one
                    if ("false".equalsIgnoreCase(sIsInterFacePresent)) {
                        //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:start
                        sCommandStatement = "modify bus $1 add interface $2";
                        MqlUtil.mqlCommand(context, sCommandStatement, objectId, strInterfaceName);
                        //PRG:RG6:R213:Mql Injection:parameterized Mql:20-Oct-2011:End
                    }

                    BusinessInterface busInterface = new BusinessInterface(strInterfaceName, context.getVault());
                    AttributeTypeList listInterfaceAttributes = busInterface.getAttributeTypes(context);

                    Iterator listInterfaceAttributesItr = listInterfaceAttributes.iterator();
                    while (listInterfaceAttributesItr.hasNext()) {
                        String attrName = ((AttributeType) listInterfaceAttributesItr.next()).getName();
                        String attrNameSmall = attrName.replaceAll(" ", "");
                        String attrNameSmallHidden = attrNameSmall + "Hidden";
                        String attrNameValue = (String) requestMap.get(attrNameSmallHidden);

                        if (attrNameValue != null && !attrNameValue.equalsIgnoreCase("")) {
                            projectConcept.setAttributeValue(context, attrName, attrNameValue);
                        } else {
                            projectConcept.setAttributeValue(context, attrName, "No");
                        }
                    }
                }
            }
            //End Added for Change Discipline

            //Modified for Bug # 340636 on 9/7/2007 - End
            /*Map projectInfo = projectConcept.getInfo(context,busSelects);*/
            String oldOwner = (String) projectInfo.get(ProjectConcept.SELECT_OWNER);
            String accessObjectId = (String) projectInfo.get(SELECT_PROJECT_ACCESS_LIST_ID);
            DomainObject accessObject = DomainObject.newInstance(context, accessObjectId);
            if (ownerName != null && !ownerName.equals("") && !ownerName.equals("null")) {
                projectConcept.setOwner(context, ownerName);
                accessObject.setOwner(context, ownerName);
            }

            person = com.matrixone.apps.common.Person.getPerson(context);

            // get the company id for this context
            com.matrixone.apps.common.Company company = person.getCompany(context);

            // HJ - RelatedObjects (objectId = projectConceptId)
            String sCommandStatement = "print bus $1 select $2 dump;";
            String strGetBusUnitId = MqlUtil.mqlCommand(context, sCommandStatement, objectId, "relationship[" + RELATIONSHIP_BUSINESS_UNIT_PROJECT + "].fromall");
            strGetBusUnitId = strGetBusUnitId.replace("B","");
            StringList listGetBusUnitId = FrameworkUtil.split(strGetBusUnitId,",");

            // request businessUnitId 가 존재할 경우
            if (businessUnitId != null && !businessUnitId.equals("") && !businessUnitId.equals("null")) {
                // HJ TEST (원본)
                /* projectConcept.setBusinessUnit(context, businessUnitId); */

                StringList listReqBusUnitId = FrameworkUtil.split(businessUnitId,",");
                int sizelistReqBusUnitId = listReqBusUnitId.size();
                int CountDelRelBusUnit = 0;
                StringList DelBusUnitId = new StringList();

                // (선) 삭제 (Old Relation 존재 시) - Old Relation 중 Req Relation 비교를 통해 삭제

                if(strGetBusUnitId != null && !strGetBusUnitId.equals("") && !strGetBusUnitId.equals("null")) {
                    for(int nGet=0;nGet<listGetBusUnitId.size();nGet++) {
//                        Boolean ChkDelete = true;
//                        int NumDelete = 0;
//
//                        for(int nReq=0;nReq<listReqBusUnitId.size();nReq++) {
//                            // 조회 UnitId 중 요청 UnitId 가 존재할 경우, ChkDelete False
//                            if (listGetBusUnitId.contains(listReqBusUnitId.get(nReq))){
//                                ChkDelete = false;
//                                NumDelete = nReq;
//                            }
//                        }
//
//                        // ChkDelete Bus Relation 삭제
//                        if(ChkDelete){
//                            DomainRelationship BusRelation = new DomainRelationship(listReqBusUnitId.get(NumDelete));
//                            BusRelation.remove(context);
//                        }

//                        DomainRelationship BusRelation = new DomainRelationship(listGetBusUnitId.get(nGet));
//                        BusRelation.remove(context, true);
//                        DomainRelationship var5 = null;
//                        StringList var6 = new StringList(1);
//                        var6.add("id");
//                        StringList var7 = new StringList(1);
//                        var7.add("id[connection]");
//                        Map var8 = this.getRelatedObject(context, RELATIONSHIP_BUSINESS_UNIT_PROJECT, false, var6, var7);
//                        DomainRelationship.disconnect(context, listGetBusUnitId.get(nGet));
                        // disconnect bus 15741.36097.25371.55281 relationship 'Business Unit Project' from 15741.36097.34677.19650;
                        MqlUtil.mqlCommand(context, "disconnect businessobject $1 relationship $2 from $3",objectId,RELATIONSHIP_BUSINESS_UNIT_PROJECT,listGetBusUnitId.get(nGet));

//                        try
//                        {
//                            ContextUtil.startTransaction(context,true);
//                            DomainObject dObject = new DomainObject(objId);
//                            String strSelect = "to["+relName+"]."+DomainConstants.SELECT_FROM_ID+"";
//                            StringList slPersonIds = dObject.getInfoList(context, strSelect);
//                            //Added to check whether person is already connected, if yes then don't connect and if no then connect
//                            if(!slPersonIds.contains(txtPersonTo)) {
//                                MqlUtil.mqlCommand(context, "connect businessobject $1 relationship $2 from $3",true,objId,relName,txtPersonTo);
//                            }
//                            MqlUtil.mqlCommand(context, "disconnect businessobject $1 relationship $2 from $3",true,objId,relName,txtPersonFrom);
//                            ContextUtil.commitTransaction(context);
//                        }
//                        catch(MatrixException e)
//                        {e.printStackTrace();
//                            ContextUtil.abortTransaction(context);
//                            isError = true;
//                            slErrors.add( mainObjType + " " + taskName );
//                        }


                    }
                }

                // Req businessUnitId 가 복수 일 경우
                if (businessUnitId.contains(",")) {
                    // 생성 - Req Relation 중 Old Relation 비교를 통해 생성 (Old Relation 이 없을 경우, 처음은 Set)
                    for(int nReq=0;nReq<listReqBusUnitId.size();nReq++) {
                        // GetRelation 존재 할 경우
                        if(strGetBusUnitId != null && !strGetBusUnitId.equals("") && !strGetBusUnitId.equals("null")) {
                            for(int nGet=0;nGet<listGetBusUnitId.size();nGet++) {
                                // 존재 하지 않을 경우, 추가
                                if(!listGetBusUnitId.contains(listReqBusUnitId.get(nReq))){
                                    projectConcept.addRelatedObject(context, new RelationshipType(DomainConstants.RELATIONSHIP_BUSINESS_UNIT_PROJECT), true, listReqBusUnitId.get(nReq));
                                }
                            }
                        // GetRelation 존재 하지 않을 경우
                        }else{
                            // 처음 Relation Set
                            if(nReq == 0){
                                projectConcept.setBusinessUnit(context, listReqBusUnitId.get(nReq));
                                // 2
                            }else{
                                projectConcept.addRelatedObject(context, new RelationshipType(DomainConstants.RELATIONSHIP_BUSINESS_UNIT_PROJECT), true, listReqBusUnitId.get(nReq));
                            }
                        }
                    }
                // Req businessUnitId 가 하나 일 경우
                } else {
                    projectConcept.setBusinessUnit(context, businessUnitId);
                }
            // request businessUnitId 가 존재하지 않을 경우
            } else {
                // HJ - Multi Relation Setting
                /* projectConcept.setBusinessUnit(context, null); */
                // 조회한 Multi Relation 제거
                if (strGetBusUnitId != null) {
                    if(listGetBusUnitId.size() > 1){
                        for (int i = 0; i < listGetBusUnitId.size(); i++) {
                            projectConcept.setRelatedObject(context, RELATIONSHIP_BUSINESS_UNIT_PROJECT, false, null);
                        }
                    }else{
                        projectConcept.setRelatedObject(context, RELATIONSHIP_BUSINESS_UNIT_PROJECT, false, null);
                    }
                }
            }

            // Connect Program
            if (programId != null && !programId.equals("") && !programId.equals("null")) {
                projectConcept.setProgram(context, programId);
            } else {
                projectConcept.setProgram(context, null);
            }

            // Connect Deliverable
            String strContributesToId = (String) projectInfo.get(SELECT_CONTRIBUTES_TO_RELATIONSHIP_ID);
            if (ProgramCentralUtil.isNotNullString(deliverableId)) {
                DomainObject deliverable = DomainObject.newInstance(context, deliverableId);

                if (ProgramCentralUtil.isNotNullString(strContributesToId))
                    DomainRelationship.setToObject(context, strContributesToId, deliverable);
                else
                    DomainRelationship.connect(context, projectConcept, RELATIONSHIP_CONTRIBUTES_TO, deliverable);
            } else {
                if (ProgramCentralUtil.isNotNullString(strContributesToId))
                    DomainRelationship.disconnect(context, strContributesToId);
            }

            if (ownerName != null && !ownerName.equals("") && !ownerName.equals("null") && (!ownerName.equals(oldOwner))) {
                Access accessMask = new Access();
                //accessMask.setReadAccess(true);
                //accessMask.setShowAccess(true);
                //Added 337605_1
                accessMask.setModifyAccess(true);
                accessMask.setExecuteAccess(true);
                accessMask.setUser(oldOwner);

                StringList userList = new StringList(1);
                userList.add(oldOwner);

                BusinessObjectList objects = new BusinessObjectList(1);
                objects.add(accessObject);

                boolean isContextPushed = false;

                ContextUtil.pushContext(context);
                isContextPushed = true;
                //revoke all access on the PAL object for the Old Owner
                BusinessObject.revokeAccessRights(context, objects, userList);
                //Grant the required access on the PAL object to the Old Owner
                BusinessObject.grantAccessRights(context, objects, accessMask);
                if (isContextPushed)
                    ContextUtil.popContext(context);
            }
        } catch (Exception ee) {
            String strMsg = EnoviaResourceBundle.getProperty(context, ProgramCentralConstants.PROGRAMCENTRAL,
                    "emxProgramCentral.Experiment.ModifyAsscess", context.getSession().getLanguage());

            MqlUtil.mqlCommand(context, "warning $1", strMsg);
            ee.printStackTrace();
        }


    }

    // HJ - Table List BusinessUnitProject 조회 (Column Type - programHTMLOutput)
    public Vector getBusUnit(Context context, String[] args) throws Exception {
        try {
            long start = System.currentTimeMillis();
            Map programMap = (Map) JPO.unpackArgs(args);
            Map paramList = (Map) programMap.get("paramList");
            MapList objectList = (MapList) programMap.get("objectList");

            int size = objectList.size();
            String[] objIdArr = new String[size];
            for (int i = 0; i < size; i++) {
                Map objectMap = (Map) objectList.get(i);
                objIdArr[i] = (String) objectMap.get(DomainObject.SELECT_ID);
            }

            Vector BusList = new Vector(size);

            for(int index = 0; index < size; index++){
                String projectConceptId = objIdArr[index];
                DomainObject domProjectConcept = DomainObject.newInstance(context);
                StringBuffer busUnitName = new StringBuffer();
                StringBuffer busUnitId = new StringBuffer();

                if (ProgramCentralUtil.isNotNullString(projectConceptId)) {
                    domProjectConcept = DomainObject.newInstance(context, projectConceptId);
                    StringList selectStmts = new StringList(2);
                    selectStmts.addElement(SELECT_ID);
                    selectStmts.addElement(SELECT_NAME);
                    selectStmts.addElement(SELECT_ATTRIBUTE_TITLE); // To display Title instead of Name of Business Unit

                    MapList mapList = domProjectConcept.getRelatedObjects(context,
                            DomainConstants.RELATIONSHIP_BUSINESS_UNIT_PROJECT,    // relationship pattern
                            DomainConstants.TYPE_BUSINESS_UNIT,                        // object pattern
                            selectStmts,                                            // object selects
                            null,                                                    // relationship selects
                            true,                                                // to direction
                            false,                                                    // from direction
                            (short) 1,                                            // recursion level
                            null,                                                    // object where clause
                            null);                       // relationship

                    int mapListSize = mapList.size();

                    if (mapList != null && mapListSize > 0) {
                        // construct array of ids

                        for (int i = 0; i < mapListSize; i++) {
                            Map dataMap = (Map) mapList.get(i);
                            String type = (String) dataMap.get(SELECT_TYPE);
                            String name = (String) dataMap.get(SELECT_NAME);
                            String objectId = (String) dataMap.get(SELECT_ID);
                            String title = (String) dataMap.get(SELECT_ATTRIBUTE_TITLE);

                            if (ProgramCentralUtil.isNotNullString(title)) {
                                name = title;
                            }

                            busUnitName.append(name + ",");
                            busUnitId.append(objectId + ",");
                        }
                    }
                }
                String strBusUnitName = "";
                String strBusUnitId = "";


                if (busUnitName.length() > 0) {
                    strBusUnitName = busUnitName.toString();
                    strBusUnitName = strBusUnitName.substring(0, strBusUnitName.length() - 1);
                    strBusUnitId = busUnitId.toString();
                    strBusUnitId = strBusUnitId.substring(0, strBusUnitId.length() - 1);
                }
                com.matrixone.apps.common.Company company =
                        (com.matrixone.apps.common.Company) DomainObject.newInstance(context,
                                DomainConstants.TYPE_COMPANY);

                com.matrixone.apps.common.Person person =
                        (com.matrixone.apps.common.Person) DomainObject.newInstance(context,
                                DomainConstants.TYPE_PERSON);

                String personId = null;
                if (personId == null || personId.equals("") || personId.equals("null")) {
                    personId = person.getPerson(context).getId();
                }
                person.setId(personId);

                if (ProgramCentralUtil.isNotNullString(strBusUnitId)) {
                    BusList.add(XSSUtil.encodeForHTML(context, strBusUnitName));
                }else{
                    BusList.add("");
                }
            }

            DebugUtil.debug("getBusUnit time..............:"+(System.currentTimeMillis()-start));

            return BusList;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public Vector getoriginatedDate(Context context, String[] args) throws Exception {
        try {
            long start = System.currentTimeMillis();
            Map programMap = (Map) JPO.unpackArgs(args);
            MapList objectList = (MapList) programMap.get("objectList");

            int size = objectList.size();
            String[] objIdArr = new String[size];
            for (int i = 0; i < size; i++) {
                Map objectMap = (Map) objectList.get(i);
                objIdArr[i] = (String) objectMap.get(DomainObject.SELECT_ID);
            }

            Vector returnVector = new Vector(size);

            for(int index = 0; index < size; index++){
                String busId = objIdArr[index];
                String selects = "originated";

                // MQL
                String strmql = MqlUtil.mqlCommand(context,"print bus $1 select $2 dump;",busId,selects);
                String Date_MMDDYYYY = strmql.split(" ")[0];
                String Date_HHmmss = strmql.split(" ")[1];
                String[] DateList_DDMMYYYY = Date_MMDDYYYY.split("/");
                String Date_YYYY = DateList_DDMMYYYY[2];
                String Date_MM = DateList_DDMMYYYY[0];
                String Date_DD = DateList_DDMMYYYY[1];
                String returnDate = Date_YYYY+"-"+Date_MM+"-"+Date_DD+" "+Date_HHmmss;
                returnVector.add(returnDate);
            }

            DebugUtil.debug("getoriginatedDate time..............:"+(System.currentTimeMillis()-start));

            return returnVector;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public String selectBusUnit(Context context, String[] args) throws Exception {
        StringBuffer output = new StringBuffer();
        try {
            HashMap programMap = (HashMap) JPO.unpackArgs(args);
            HashMap requestMap = (HashMap) programMap.get("requestMap");
            String strMode = (String) requestMap.get("mode");
            String projectConceptId = (String) requestMap.get("objectId");
            String languageStr = (String) requestMap.get("languageStr");
            DomainObject domProjectConcept = DomainObject.newInstance(context);
            String strOuput = "";
            StringBuffer busUnitName = new StringBuffer();
            StringBuffer busUnitId = new StringBuffer();

            if (ProgramCentralUtil.isNotNullString(projectConceptId)) {
                domProjectConcept = DomainObject.newInstance(context, projectConceptId);
                StringList selectStmts = new StringList(2);
                selectStmts.addElement(SELECT_ID);
                selectStmts.addElement(SELECT_NAME);
                selectStmts.addElement(SELECT_ATTRIBUTE_TITLE); // To display Title instead of Name of Business Unit

                MapList mapList = domProjectConcept.getRelatedObjects(context,
                        DomainConstants.RELATIONSHIP_BUSINESS_UNIT_PROJECT,    // relationship pattern
                        DomainConstants.TYPE_BUSINESS_UNIT,                        // object pattern
                        selectStmts,                                            // object selects
                        null,                                                    // relationship selects
                        true,                                                // to direction
                        false,                                                    // from direction
                        (short) 1,                                            // recursion level
                        null,                                                    // object where clause
                        null);                       // relationship

                if (mapList != null && mapList.size() > 0) {
                    // construct array of ids
                    int mapListSize = mapList.size();
                    for (int i = 0; i < mapListSize; i++) {
                        Map dataMap = (Map) mapList.get(i);
                        String type = (String) dataMap.get(SELECT_TYPE);
                        String name = (String) dataMap.get(SELECT_NAME);
                        String objectId = (String) dataMap.get(SELECT_ID);
                        String title = (String) dataMap.get(SELECT_ATTRIBUTE_TITLE);

                        if (ProgramCentralUtil.isNotNullString(title)) {
                            name = title;
                        }

                        busUnitName.append(name + ",");
                        busUnitId.append(objectId + ",");

                    }
                }
            }
            String strBusUnitName = "";
            String strBusUnitId = "";


            if (busUnitName.length() > 0) {
                strBusUnitName = busUnitName.toString();
                strBusUnitName = strBusUnitName.substring(0, strBusUnitName.length() - 1);
                strBusUnitId = busUnitId.toString();
                strBusUnitId = strBusUnitId.substring(0, strBusUnitId.length() - 1);
            }
            com.matrixone.apps.common.Company company =
                    (com.matrixone.apps.common.Company) DomainObject.newInstance(context,
                            DomainConstants.TYPE_COMPANY);

            com.matrixone.apps.common.Person person =
                    (com.matrixone.apps.common.Person) DomainObject.newInstance(context,
                            DomainConstants.TYPE_PERSON);

            String personId = null;
            if (personId == null || personId.equals("") || personId.equals("null")) {
                personId = person.getPerson(context).getId();
            }
            person.setId(personId);

            if (strMode.equalsIgnoreCase("edit") || strMode.equalsIgnoreCase("create")) {
                String clearLabel = EnoviaResourceBundle.getProperty(context, "ProgramCentral",
                        "emxProgramCentral.Common.Clear", languageStr);
                // find the person's company
                StringList busSelects = new StringList(1);
                busSelects.add(company.SELECT_ID);
                busSelects.add(company.SELECT_NAME);
                company = person.getCompany(context);
                String companyId = company.getInfo(context, company.SELECT_ID);
                output.append("<input type='text' name='BusinessUnitName' value='" + XSSUtil.encodeForHTML(context, strBusUnitName) + "' readonly=\"readonly\"/>");
                output.append("<input type='hidden' name='BusinessUnitId' value='" + strBusUnitId + "'/>");
                output.append("<input type='button' name='business' value='...' onClick=\"");
                //output.append("javascript:showChooser('../programcentral/emxProgramCentralOrganizationSelectDialogFS.jsp?form=emxCreateForm&amp;fieldName=BusinessUnitName&amp;fieldId=BusinessUnitId&amp;");
                //output.append("objectId="+companyId+"&amp;suiteKey=ProgramCentral','600','600')\"/>");
                output.append("javascript:showChooser('");
                String strURL = ProgramCentralConstants.EMPTY_STRING;
                // HJ - emxFullSearch.jsp >> selection=single 에서 multiple 수정
                // strURL="../common/emxFullSearch.jsp?field=TYPES=type_BusinessUnit&amp;table=PMCOrganizationSummary&amp;selection=single&amp;submitURL=../programcentral/emxProgramCentralResourceRequestAutonomySearchSelect.jsp&amp;fieldNameActual=BusinessUnitId&amp;fieldNameDisplay=BusinessUnitName&amp;suiteKey=ProgramCentral";
                strURL = "../common/emxFullSearch.jsp?field=TYPES=type_BusinessUnit&amp;table=PMCOrganizationSummary&amp;selection=multiple&amp;submitURL=../programcentral/emxProgramCentralResourceRequestAutonomySearchSelect.jsp&amp;fieldNameActual=BusinessUnitId&amp;fieldNameDisplay=BusinessUnitName&amp;suiteKey=ProgramCentral";
                // HJ - gscCodeMaster Test
                //strURL = "../common/emxFullSearch.jsp?field=TYPES=type_gscCodeMaster&amp;selection=single&amp;table=gscCodeMasterTable";
                output.append(strURL);
                output.append("','600','600')\"/>");
                output.append("<a href=\"javascript:removeBusinessUnit()\">" + clearLabel + "</a>");

                strOuput = output.toString();
            } else if (ProgramCentralUtil.isNotNullString(strBusUnitId)) {
                // strBusUnitId 가 "," 를 포함한 복수개일 경우 output Html
                if(strBusUnitId.contains(",")){
                    StringList strlistBusUnitId = FrameworkUtil.split(strBusUnitId,",");
                    StringList strlistBusUnitName = FrameworkUtil.split(strBusUnitName,",");
                    for(int index=0; index < strlistBusUnitId.size(); index++){
                        strBusUnitId = XSSUtil.encodeForURL(context, strBusUnitId);
                        output.append("<a href='../common/emxTree.jsp?objectId=").append(strlistBusUnitId.get(index));
                        output.append("&amp;relId=null&amp;suiteKey=ProgramCentral'>");
                        output.append("<img src='../common/images/iconSmallBusinessUnit.gif' border='0' valign='absmiddle'/>");
                        output.append(XSSUtil.encodeForHTML(context, strlistBusUnitName.get(index)));
                        output.append("</a>&emsp;");
                    }
                // strBusUnitId 가 한개일 경우 output Html
                }else{
                    strBusUnitId = XSSUtil.encodeForURL(context, strBusUnitId);
                    output.append("<a href='../common/emxTree.jsp?objectId=").append(strBusUnitId);
                    output.append("&amp;relId=null&amp;suiteKey=ProgramCentral'>");
                    output.append("<img src='../common/images/iconSmallBusinessUnit.gif' border='0' valign='absmiddle'/>");
                    output.append(XSSUtil.encodeForHTML(context, strBusUnitName));
                    output.append("</a>");
                }
                strOuput = output.toString();
            }
            return strOuput;
        } catch (Exception ex) {
            throw ex;
        }
    }
}

