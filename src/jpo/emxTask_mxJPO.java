/* emxTask.java

   Copyright (c) 1992-2020 Dassault Systemes.
   All Rights Reserved.
   This program contains proprietary and trade secret information of MatrixOne,
   Inc.  Copyright notice is precautionary only
   and does not evidence any actual or intended publication of such program

   static const char RCSID[] = $Id: emxTask.java.rca 1.6 Wed Oct 22 16:21:23 2008 przemek Experimental przemek $
*/

import com.dassault_systemes.enovia.e6wv2.foundation.db.ObjectUtil;
import com.dassault_systemes.enovia.e6wv2.foundation.jaxb.*;
import com.gsc.enovia.tskv2.TaskReferences;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.framework.ui.UIUtil;
import com.matrixone.apps.program.ProgramCentralConstants;
import com.matrixone.apps.program.ProgramCentralUtil;
import com.matrixone.apps.program.ProjectSpace;
import com.matrixone.apps.program.Task;
import com.matrixone.apps.program.fiscal.Helper;
import matrix.db.*;
import matrix.util.MatrixException;
import matrix.util.StringList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The <code>emxTask</code> class represents the Task JPO
 * functionality for the AEF type.
 *
 * @version AEF 10.0.SP4 - Copyright (c) 2002, MatrixOne, Inc.
 */
public class emxTask_mxJPO extends emxTaskBase_mxJPO
{

    /**
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @throws Exception if the operation fails
     * @since AEF 10.0.SP4
     * @grade 0
     */
    public emxTask_mxJPO (Context context, String[] args)
        throws Exception
    {
      super(context, args);
    }
    /**
     * This method is used to show the status image.
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args    holds the following input arguments:
     *                0 - objectList MapList
     * @throws Exception if the operation fails
     * @returns Vector containing all the status image value as String.
     * @since PMC 11.0.0.0
     */
    public Vector getStatusIcon(Context context, String[] args) throws Exception {
        long start = System.currentTimeMillis();
        Map programMap = (Map) JPO.unpackArgs(args);
        Map paramList = (Map) programMap.get("paramList");
        String exportFormat = (String) paramList.get("exportFormat");
        MapList objectList = (MapList) programMap.get("objectList");
        String invokeFrom = (String) programMap.get("invokeFrom"); //Added for OTD

        int size = objectList.size();
        String[] objIdArr = new String[size];
        for (int i = 0; i < size; i++) {
            Map objectMap = (Map) objectList.get(i);
            objIdArr[i] = (String) objectMap.get(DomainObject.SELECT_ID);
        }

        Vector showIcon = new Vector(size);

        StringList busSelect = new StringList(4);
        busSelect.add(SELECT_PERCENT_COMPLETE);
        busSelect.add(SELECT_TASK_ACTUAL_START_DATE);
        busSelect.add(SELECT_TASK_ACTUAL_FINISH_DATE);
        busSelect.add(SELECT_TASK_ESTIMATED_FINISH_DATE);

        BusinessObjectWithSelectList objectWithSelectList = null;
        if ("TestCase".equalsIgnoreCase(invokeFrom)) { //Added for ODT
            objectWithSelectList = ProgramCentralUtil.getObjectWithSelectList(context, objIdArr, busSelect, true);
        } else {
            objectWithSelectList = ProgramCentralUtil.getObjectWithSelectList(context, objIdArr, busSelect);
        }

        String ctxLang = context.getLocale().getLanguage();
        SimpleDateFormat sdf = new SimpleDateFormat(eMatrixDateFormat.getEMatrixDateFormat(), Locale.US);

        Calendar calendar = Calendar.getInstance();
        Date systemDate = calendar.getTime();
        systemDate = Helper.cleanTime(systemDate);

        String onTime = EnoviaResourceBundle.getProperty(context, "ProgramCentral", "emxProgramCentral.Common.OnTime", ctxLang);
        String late = EnoviaResourceBundle.getProperty(context, "ProgramCentral", "emxProgramCentral.Common.Late", ctxLang);
        String behindSchedule = EnoviaResourceBundle.getProperty(context, "ProgramCentral", "emxProgramCentral.Common.Legend.BehindSchedule", ctxLang);
        int yellowRedThreshold = Integer.parseInt(EnoviaResourceBundle.getProperty(context, "eServiceApplicationProgramCentral.SlipThresholdYellowRed"));


        for (int i = 0; i < size; i++) {
            BusinessObjectWithSelect bws = objectWithSelectList.getElement(i);
            String percentComplete = bws.getSelectData(Task.SELECT_PERCENT_COMPLETE);
            String estimatedFinishDate = bws.getSelectData(ProgramCentralConstants.SELECT_ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE);
            String actualFinishDate = bws.getSelectData(ProgramCentralConstants.SELECT_ATTRIBUTE_TASK_ACTUAL_FINISH_DATE);
            String actualStartDate = bws.getSelectData(Task.SELECT_TASK_ACTUAL_START_DATE);

            String value = DomainObject.EMPTY_STRING;
            String display = DomainObject.EMPTY_STRING;
            try {
                Date estimatedFinish = sdf.parse(estimatedFinishDate);
                if ("100.0".equals(percentComplete)) {
                    if (UIUtil.isNotNullAndNotEmpty(actualFinishDate)) {
                        System.out.println("actualFinishDate = " + actualFinishDate);
                        Date actualFinish = sdf.parse(actualFinishDate);

                        if (actualFinish.before(estimatedFinish) || actualFinish.equals(estimatedFinish)) {
                            display = onTime;
                            value = ProgramCentralConstants.TASK_STATUS_ON_TIME;
                        } else {
                            display = late;
                            value = ProgramCentralConstants.TASK_STATUS_COMPLETED_LATE;
                        }
                    } else if (UIUtil.isNotNullAndNotEmpty(actualStartDate)) {
                        System.out.println("actualStartDate = " + actualStartDate);
                        Date actStartDate = sdf.parse(actualStartDate);
                        if (actStartDate.before(estimatedFinish)) {
                            display = onTime;
                            value = ProgramCentralConstants.TASK_STATUS_ON_TIME;
                        } else {
                            display = late;
                            value = ProgramCentralConstants.TASK_STATUS_COMPLETED_LATE;
                        }
                    }
                } else {
                    if (systemDate.after(estimatedFinish)) {
                        display = late;
                        value = ProgramCentralConstants.TASK_STATUS_LATE;
                    } else if (DateUtil.computeDuration(systemDate, estimatedFinish) <= yellowRedThreshold) {
                        display = behindSchedule;
                        value = ProgramCentralConstants.TASK_STATUS_BEHIND_SCHEDULE;
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (ProgramCentralUtil.isNotNullString(exportFormat)) {
                showIcon.add(display);
            } else if (ProgramCentralConstants.TASK_STATUS_ON_TIME.equals(value)) {
                showIcon.add("<div style=\"background:#6FBC4B;width:11px;height:11px;border-radius:50px;margin:auto;\" title=\"" + display + "\"> </div>");
            } else if (ProgramCentralConstants.TASK_STATUS_COMPLETED_LATE.equals(value)) {
                showIcon.add("<div style=\"background:#6FBC4B;width:11px;height:11px;border-radius:50px;margin:auto;\" title=\"" + display + "\"> </div>");
            } else if (ProgramCentralConstants.TASK_STATUS_BEHIND_SCHEDULE.equals(value)) {
                showIcon.add("<div style=\"background: #FEE000;width:11px;height:11px;border: none;-webkit-box-sizing: content-box;-moz-box-sizing: content-box;box-sizing: content-box;font: normal 100%/normal Arial, Helvetica, sans-serif;color: rgba(0, 0, 0, 1);-o-text-overflow: clip;text-overflow: clip;-webkit-transform: rotateZ(-45deg);transform: rotateZ(-45deg);-webkit-transform-origin: 0 100% 0deg;transform-origin: 0 100% 0deg;;margin:auto;\" title=\"" + display + "\"> </div>");
            } else if (ProgramCentralConstants.TASK_STATUS_LATE.equals(value)) {
                showIcon.add("<div style=\"background:#CC092F;width:11px;height:11px;margin:auto;\" title=\"" + display + "\"> </div>");
            } else {
                showIcon.add(" ");
            }
        }

        DebugUtil.debug("Total time taken by getStatusIcon(programHTMLOutPut)::" + (System.currentTimeMillis() - start));

        return showIcon;
    }
    public void releaseDocuments(Context context, String[] args)throws Exception {
        DebugUtil.debug("Entering releaseDocuments");

        // get values from args.
        String objectId = args[0];
        ExpandData expandData = new ExpandData();
        expandData.setGetFrom(true);
        expandData.setRelationshipPattern("Task Deliverable");
        Dataobject taskObject = new Dataobject();
        taskObject.setId(objectId);
        List<Selectable> selects = new ArrayList<Selectable>();
        Selectable selectable = new Selectable();
        selectable.setExpression("id");
        selectable.setName("id");
        selectable.setType(ExpressionType.BUS);
        Datacollection datacollection = ObjectUtil.expand(context, taskObject, expandData, selects);
        List<Dataobject> list = datacollection.getDataobjects();
        for (Dataobject dataobject : list) {
            String documentId = dataobject.getId();
            DomainObject document = DomainObject.newInstance(context, documentId);
            document.setState(context, "RELEASED");
            System.out.println("Document released successfully : " + documentId);
        }
    }

    /***
     * Check Event Result input data and Project State for review result
     * @param context
     * @param args
     * @return
     * @throws Exception
     */
    public int checkReviewResult(Context context, String[] args)
            throws Exception
    {
        // get values from args.
        String objectId = args[0];

        setId(objectId);

        //check if gscReviewResult attribute is empty

        //check if project state is changed by gscReviewResult
        // Approve : No change, Hold : Hold, Cancel : Cancel
        // if Event Type is TRS or PRS, skip(ET04, ET05)
        String eventType = getInfo(context, "attribute[gscEventType]");
        if("ET04".equals(eventType) || "ET05".equals(eventType)){
            return 0;
        }

        String strRelationshipName = "gscEventProject";
        String strType = "Project Space";
        StringList objectSelects = new StringList();
        objectSelects.add("name");
        objectSelects.add("current");
        objectSelects.add("attribute[gscGateResult]");
        StringList relSelects = new StringList();
        relSelects.add("attribute[gscReviewResult]");
        MapList projList = getRelatedObjects(context,strRelationshipName, strType, objectSelects, relSelects, false,true, (short)1, "", "");
        boolean checkPassed = false;
        int emptyCount = 0;
        //check if gscReviewResult attribute is empty
        for (int i = 0; i < projList.size(); i++) {
            Map map = (Map) projList.get(i);
            String reviewResult = (String)map.get("attribute[gscGateResult]");
            if(reviewResult == null || "".equals(reviewResult)){
                checkPassed = false;
                emptyCount++;
            }
            if(emptyCount > 0) {
                String sErrMsg  = String.format("Projects' reviewed result is not updated.");
                emxContextUtilBase_mxJPO.mqlNotice(context, sErrMsg);
                throw new MatrixException(sErrMsg);
            }
            //check if project state is changed by gscReviewResult
            String current = (String)map.get("current");
            String projectCode = (String)map.get("name");
            if("Cancel".equals(reviewResult) && !"Cancel".equals(current)){
                checkPassed = false;
                String sErrMsg  = String.format("Project(%s)'s state is not changed to Cancel.", projectCode);
                emxContextUtilBase_mxJPO.mqlNotice(context, sErrMsg);
                throw new MatrixException(sErrMsg);
            }
            if("Hold".equals(reviewResult) && !"Hold".equals(current)){
                checkPassed = false;
                String sErrMsg  = String.format("Project(%s)'s state is not changed to Hold.", projectCode);
                emxContextUtilBase_mxJPO.mqlNotice(context, sErrMsg);
                throw new MatrixException(sErrMsg);
            }
        }

        return 0;
    }
}
