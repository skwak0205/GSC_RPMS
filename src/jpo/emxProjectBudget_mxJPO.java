/*
 *  emxProjectTemplateBudget.java
 *
 * Copyright (c) 1992-2020 Dassault Systemes.
 *
 * All Rights Reserved.
 * This program contains proprietary and trade secret information of
 * MatrixOne, Inc.  Copyright notice is precautionary only and does
 * not evidence any actual or intended publication of such program.
 *
 * static const char RCSID[] = $Id: ${CLASSNAME}.java.rca 1.7.2.2 Thu Dec  4 07:56:10 2008 ds-ss Experimental ${CLASSNAME}.java.rca 1.7.2.1 Thu Dec  4 01:55:03 2008 ds-ss Experimental ${CLASSNAME}.java.rca 1.7 Wed Oct 22 15:50:28 2008 przemek Experimental przemek $
 */
import com.matrixone.apps.common.Task;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.EnoviaResourceBundle;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.eMatrixDateFormat;
import com.matrixone.apps.program.CostItem;
import com.matrixone.apps.program.Financials;
import com.matrixone.apps.program.ProgramCentralConstants;
import com.matrixone.apps.program.ProgramCentralUtil;
import matrix.db.*;
import matrix.util.StringList;

import java.lang.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @version PMC 10-6 - Copyright (c) 2002, MatrixOne, Inc.
 */
public class emxProjectBudget_mxJPO extends emxProjectBudgetBase_mxJPO
{
    /**
     * Constructor.
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @throws Exception if the operation fails
     * @since PMC 10-6
     */
    public emxProjectBudget_mxJPO (Context context, String[] args)
        throws Exception
    {
        super(context, args);
    }

    /**
     * Generates required Cost Interval columns dynamically
     *
     * @param context The matrix context object
     * @param args The arguments, it contains requestMap
     * @return The MapList object containing definitions about new columns for showing Benefit Intervals
     * @throws Exception if operation fails
     */
    public MapList getDynamicCostIntervalColumn (Context context, String[] args) throws Exception
    {
        final String strDateFormat=eMatrixDateFormat.getEMatrixDateFormat();
        Map mapProgram = (Map) JPO.unpackArgs(args);
        Map requestMap = (Map)mapProgram.get("requestMap");

        // Following code gets business objects information
        String strObjectId = (String) requestMap.get("objectId");
        String selectedOption = (String) requestMap.get("selectedOption");
        selectedOption = ProgramCentralUtil.isNullString(selectedOption)?"option2":selectedOption;


        // check if budget is present.
        DomainObject dmoProject = DomainObject.newInstance(context, strObjectId);
        String SELECT_PROJECT_COST_INTERVAL = "attribute[" + ATTRIBUTE_COST_INTERVAL  + "]";
        StringList busSelect1 = new StringList(2);
        busSelect1.add(SELECT_PROJECT_COST_INTERVAL);
        busSelect1.add(SELECT_CURRENT);
        StringList relSelect1 = new StringList();
        MapList mlBudget = dmoProject.getRelatedObjects(
                context,
                RELATIONSHIP_PROJECT_FINANCIAL_ITEM,
                TYPE_BUDGET,
                busSelect1,
                relSelect1,
                false,
                true,
                (short)1,
                "",
                DomainConstants.EMPTY_STRING);

        if(mlBudget == null || mlBudget.size()<1)
            return mlBudget;

        String strLanguage=context.getSession().getLanguage();
        String strFY= EnoviaResourceBundle.getProperty(context, "ProgramCentral",
                "emxProgramCentral.Financials.FiscalYear", strLanguage);
        String strM= EnoviaResourceBundle.getProperty(context, "ProgramCentral",
                "emxProgramCentral.Financials.Month", strLanguage);
        String strQ= EnoviaResourceBundle.getProperty(context, "ProgramCentral",
                "emxProgramCentral.Financials.Quarter", strLanguage);
        String strWK= EnoviaResourceBundle.getProperty(context, "ProgramCentral",
                "emxProgramCentral.Financials.Week", strLanguage);
        String strTotal= EnoviaResourceBundle.getProperty(context, "ProgramCentral",
                "emxProgramCentral.Financials.Total", strLanguage);
        Date dtFiscalStartDate = null;
        Date dtFiscalEndDate = null;
        Map fiscalYearMap = null;
        Map mapColumn = null;
        Map mapSettings = null;
        Date dtStartDate = null;
        Date dtFinishDate = null;

        String tableName = (String)requestMap.get("selectedTable");
        String displayView = (String)requestMap.get(BUDGETDISPLAYVIEWFILTER);
        if("PMCProjectBudgetReportTable".equals(tableName)){
            displayView = (String)requestMap.get("PMCProjectBudgetReportDisplayViewFilter");
        }
        String strBudgetCostInterval = "";
        String strTimeLineInterval = "";
        String strMonth = "";
        String strBudgetState = "";
        String  selecteView= "";
        String strInterval = "";
        if("PMCProjectBudgetReportTable".equals(tableName)){
            selecteView = actualView;
            strInterval =  (String) requestMap.get("PMCProjectBudgetReportIntervalFilter");
        }else {
            selecteView = (String)requestMap.get(BUDGETVIEWFILTER);
            strInterval = (String)requestMap.get(BUDGETINTERVALFILTER);
        }
        MapList mlColumns = new MapList();
        int interval = 0;
        int index = 0;
        //This is default year interval
        if(ProgramCentralUtil.isNullString(strInterval)){
            Map intervalMap = getBenefitOrBudgetYearRange(context, args);	// to get the list of year intervals.
            StringList slIntervals = (StringList)intervalMap.get("field_choices");
            if(slIntervals.size() != 0){
                strInterval = (String)slIntervals.get(0);	// The very first interval in the list will be taken as default interval.
                index = strInterval.lastIndexOf(" Year");
                strInterval = strInterval.substring(0,index);
                interval = Integer.parseInt(strInterval);
            }
        }
        //User's choice interval
        else{
            index = strInterval.lastIndexOf(" Year");
            strInterval = strInterval.substring(0,index);
            interval = Integer.parseInt(strInterval);
        }
        for (Iterator itrTableRows = mlBudget.iterator(); itrTableRows.hasNext();){
            Map mapDataColumn = new HashMap();
            mapDataColumn= (Map) itrTableRows.next();
            strTimeLineInterval = (String)mapDataColumn.get(SELECT_PROJECT_COST_INTERVAL);
            strBudgetCostInterval= (String)mapDataColumn.get(SELECT_PROJECT_COST_INTERVAL);
            strBudgetState = (String)mapDataColumn.get(SELECT_CURRENT);
        }

        Map mapDate = getMinMaxCostIntervalDates(context,strObjectId);
        String selecteReportView = (String)requestMap.get(BUDGETTIMELINEREPORTFILTER);
        if(null != selecteReportView){
            strTimeLineInterval = selecteReportView;
        }else{
            //Set the interval to weekly only when everything is null.
            if(null==strTimeLineInterval){
                strTimeLineInterval = WEEKLY;
            }
        }

        if(null != mapDate){
            dtStartDate = (Date)mapDate.get("TimeLineIntervalFrom");
            dtFinishDate = (Date)mapDate.get("TimeLineIntervalTo");
        }
        if("FiscalYear".equals(displayView)){
            String strSelectedYear = "";
            strSelectedYear = (String)requestMap.get(BUDGETFISCALYEARFILTER);

            if(null == strSelectedYear || "null".equals(strSelectedYear) || "".equals(strSelectedYear)){
                strSelectedYear = (String)requestMap.get(BUDGETREPORTFISCALYEARFILTER);
            }
            //Added:PRG:MS9:R212:26-Aug-2011:IR-088614V6R2012x
            if(null != strSelectedYear && !"".equals(strSelectedYear) && !"null".equals(strSelectedYear)){
                int year = Integer.parseInt(strSelectedYear);
                fiscalYearMap = Financials.getFiscalInterval(interval, year);
                dtFiscalStartDate = (Date)fiscalYearMap.get("fStartDate");
                dtFiscalEndDate = (Date)fiscalYearMap.get("fEndDate");

            }else{
                Calendar calendar1 = Calendar.getInstance();
                if(null!= dtStartDate){
                    calendar1.setTime(dtStartDate);
                    int nYear = calendar1.get(Calendar.YEAR);
                    fiscalYearMap = Financials.getFiscalInterval(interval, nYear);
                    dtFiscalStartDate = (Date)fiscalYearMap.get("fStartDate");
                    dtFiscalEndDate = (Date)fiscalYearMap.get("fEndDate");
                }
            }
        }else{
            dtFiscalStartDate = (Date)mapDate.get("TimeLineIntervalFrom");
            dtFiscalEndDate = (Date)mapDate.get("TimeLineIntervalTo");
        }

        try{
            int nTimeframe  = 0 ;
            int nYear = 0;
            //Calculation of Fiscal Start Month  and year to display in the groupheader
            int intFiscalYearStartYear = 0;
            int intFiscalYearEndYear = 0;
            String strFiscalStartMonth = "";
            if(null != dtFiscalStartDate){
                Calendar calFiscStartYear =  Calendar.getInstance();
                calFiscStartYear.setTime(dtFiscalStartDate);
                Calendar calFiscEndYear =  Calendar.getInstance();
                calFiscEndYear.setTime(dtFiscalEndDate);
                int intFiscalYearStartMonth = calFiscStartYear.get(Calendar.MONTH)+1;
                strFiscalStartMonth =  getMonthName(context,intFiscalYearStartMonth);
                intFiscalYearStartYear = calFiscStartYear.get(Calendar.YEAR);
                intFiscalYearEndYear = calFiscEndYear.get(Calendar.YEAR);
            }
            mapColumn = new HashMap();
            if(!PROJECT_PHASE.equals(strBudgetCostInterval)){
                if(null != dtStartDate && null != dtFinishDate){

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dtStartDate);

                    ArrayList dateList =  getIntervalDateList(dtStartDate,dtFinishDate,strTimeLineInterval);

                    if(null != dtFiscalStartDate && null != dtFiscalEndDate){
                        dateList =  getIntervalDateList(dtFiscalStartDate,dtFiscalEndDate,strTimeLineInterval);
                    }
                    String strIntervalDate = null;
                    java.util.Set<Integer> intervals = new HashSet<Integer>();
                    for (Iterator itrTableRows = dateList.iterator(); itrTableRows.hasNext();)
                    {
                        Date dtIntervalDate=(Date)itrTableRows.next();
                        Calendar intervalCalendar=Calendar.getInstance();
                        intervalCalendar.setTime(dtIntervalDate);
                        int day = intervalCalendar.get(Calendar.DAY_OF_MONTH);
                        int month=intervalCalendar.get(Calendar.MONTH)+1;
                        int nFiscalDisplayYear = Financials.getFiscalYear(dtIntervalDate);
                        intervals.add(nFiscalDisplayYear);
                        nYear = intervalCalendar.get(Calendar.YEAR);
                        if(intervals.size() > interval)
                            break;
                        mapColumn = new HashMap();
                        nTimeframe = Financials.getFiscalMonthNumber(dtIntervalDate);
                        strMonth = getMonthName(context,(dtIntervalDate.getMonth()+1));
                        String strLabelDate = ""+dtIntervalDate.getDate()+"-"+strMonth+"-"+(dtIntervalDate.getYear()+1900);

                        if(strTimeLineInterval.equals(CostItem.MONTHLY)){
                            mapColumn.put("label", strM+"-"+nTimeframe +" ("+strLabelDate+")");
                        }
                        else if ((CostItem.WEEKLY).equals(strTimeLineInterval)){
                            nTimeframe = Financials.getFiscalWeekNumber(dtIntervalDate);
                            mapColumn.put("label", strWK+"-"+nTimeframe +" ("+strLabelDate+")");
                        }
                        else if ((CostItem.QUARTERLY).equals(strTimeLineInterval)){
                            nTimeframe = Financials.getFiscalQuarterNumber(dtIntervalDate);
                            mapColumn.put("label", strQ+"-"+nTimeframe +" ("+strLabelDate+")");
                        }
                        else{
                            nTimeframe= month;
                            mapColumn.put("label", strFY+"-"+nFiscalDisplayYear);
                        }
                        String columnName = ProgramCentralConstants.EMPTY_STRING;
                        String dateFormat = strDateFormat.toLowerCase();
                        if(dateFormat.contains("yyyy/mm/dd"))
                        {
                            columnName = nYear + "/" + month + "/" + day;
                        }
                        else if(dateFormat.contains("dd/mm/yyyy"))
                        {
                            columnName = day + "/" + month + "/" + nYear;
                        }
                        else
                        {
                            columnName = month + "/" + day + "/" + nYear;
                        }
                        mapColumn.put("name", columnName);
                        mapSettings = new HashMap();
                        mapSettings.put("Registered Suite","ProgramCentral");
                        mapSettings.put("program","emxProjectBudget");
                        mapSettings.put("function","getColumnBudgetData");
                        mapSettings.put("Column Type","program");
                        mapSettings.put("Editable","true");
                        mapSettings.put("Export","true");
                        mapSettings.put("Field Type","attribute");
                        mapSettings.put("Sortable","false");
                        if(!"PMCProjectBudgetReportTable".equals(tableName)){
                            mapSettings.put("Update Program","emxProjectBudget");
                            mapSettings.put("Update Function","updateDynamicColumnData");
                            mapSettings.put("Edit Access Program","emxProjectBudget");
                            mapSettings.put("Edit Access Function","isColumnEditable");
                        }
                        mapSettings.put("Style Program","emxFinancialItem");
                        mapSettings.put("Style Function","getBudgetRightAllignedStyleInfo");
                        if("FiscalYear".equals(displayView) || strTimeLineInterval.equals(CostItem.MONTHLY))
                        {
                            mapSettings.put("Group Header",strFY+" "+nFiscalDisplayYear+"");
                        }else{
                            mapSettings.put("Group Header",strFY+" "+nFiscalDisplayYear+"");
                        }
                        mapSettings.put("Validate","validateCost");
                        mapColumn.put("settings", mapSettings);

                        // HJ 23-02-13 월 데이터 mapColumn 삭제 처리
                        //mlColumns.add(mapColumn);
                    }//Added:PRG:MS9:R212:26-Aug-2011:IR-088614V6R2012x end
                    mapColumn = new HashMap();
                    mapColumn.put("name", "Total");
                    String strTotalLabel= EnoviaResourceBundle.getProperty(context, "ProgramCentral",
                            "emxProgramCentral.Budget.Total", strLanguage);
                    mapColumn.put("label",strTotalLabel );
                    mapSettings = new HashMap();

                    mapSettings = new HashMap();
                    mapSettings.put("Registered Suite","ProgramCentral");
                    mapSettings.put("program","emxProjectBudget");
                    mapSettings.put("function","getColumnBudgetData");
                    mapSettings.put("Column Type","program");
                    mapSettings.put("Editable","false");
                    mapSettings.put("Export","true");
                    mapSettings.put("Field Type","string");
                    mapSettings.put("Sortable","false");
                    mapSettings.put("Style Program","emxFinancialItem");
                    mapSettings.put("Style Function","getBudgetRightAllignedStyleInfo");
                    mapColumn.put("settings", mapSettings);
                    mlColumns.add(mapColumn);
                }
            }else{
                StringList busSelect = new StringList();
                StringList relSelect = new StringList();
                String whereClause = "";
                String SELECT_PROJECT_PHASE_ID =SELECT_ID;
                String SELECT_PROJECT_PHASE_NAME =SELECT_NAME;
                String SELECT_PROJECT_PHASE_START_DATE = SELECT_ATTRIBUTE_TASK_ESTIMATED_START_DATE;
                String SELECT_PROJECT_PHASE_END_DATE = SELECT_ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE;
                String attrTaskWBSId =  "to["+DomainConstants.RELATIONSHIP_SUBTASK+"].attribute["+DomainConstants.ATTRIBUTE_TASK_WBS+"]";
                busSelect.add(SELECT_PROJECT_PHASE_ID);
                busSelect.add(SELECT_PROJECT_PHASE_NAME);
                busSelect.add(SELECT_PROJECT_PHASE_START_DATE);
                busSelect.add(SELECT_PROJECT_PHASE_END_DATE);
                busSelect.addElement(attrTaskWBSId);
                MapList mlProjectPhase = new MapList();
                mlProjectPhase = getProjectPhases(context,strObjectId,busSelect,relSelect,whereClause);
                if("option2".equals(selectedOption)){
                    mlProjectPhase.sortStructure(context, attrTaskWBSId, "ascending", "emxWBSColumnComparator");
                }else{
                    mlProjectPhase.sortStructure(context, attrTaskWBSId, "ascending", "emxWBSIDComparator");
                }
                String PhaseStartDate ="";
                String PhaseFinishDate ="";
                Map mapDataColumn1 = new HashMap();
                for (Iterator itrTableRows = mlProjectPhase.iterator(); itrTableRows.hasNext();)
                {
                    mapDataColumn1= (Map) itrTableRows.next();
                    PhaseStartDate = (String)mapDataColumn1.get(SELECT_PROJECT_PHASE_START_DATE);
                    PhaseFinishDate = (String)mapDataColumn1.get(SELECT_PROJECT_PHASE_END_DATE);
                }
                //if(null == selecteView && tableName.equals("PMCProjectBudgetReportTable")){
                if("PMCProjectBudgetReportTable".equals(tableName)){
                    mlProjectPhase.clear();
                    ArrayList dateList = new ArrayList();
                    if(ProgramCentralUtil.isNotNullString(strTimeLineInterval) && strTimeLineInterval.contains("Phase")){
                        strTimeLineInterval = WEEKLY;
                    }
                    dateList =  getIntervalDateList(dtStartDate,dtFinishDate,strTimeLineInterval);
                    for(int i=0;i<dateList.size();i++){
                        mlProjectPhase.add(dateList.get(i));
                    }
                }
                String strIntervalDate = null;
                Map mapDataColumn = new HashMap();
                int nFiscalDisplayYear = 0;
                for (Iterator itrTableRows = mlProjectPhase.iterator(); itrTableRows.hasNext();)
                {
                    if( "PMCProjectBudgetReportTable".equals(tableName)){
                        Date dtIntervalDate=(Date)itrTableRows.next();
                        Calendar intervalCalendar=Calendar.getInstance();
                        intervalCalendar.setTime(dtIntervalDate);

                        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                        strIntervalDate = sdf.format(dtIntervalDate);
                        dtIntervalDate = sdf.parse (strIntervalDate);
                        int date = intervalCalendar.get(Calendar.DATE);
                        nYear=intervalCalendar.get(Calendar.YEAR);
                        int month = intervalCalendar.get(Calendar.MONTH);
                        month++;
                        strMonth = getMonthName(context, month);
                        nFiscalDisplayYear = Financials.getFiscalYear(dtIntervalDate);
                        String strLabelDate = "" + date + "-" + strMonth + "-" + nYear;
                        mapColumn = new HashMap();
                        if(strTimeLineInterval.equals(CostItem.MONTHLY)){
                            mapColumn.put("label", strM + "-" + month + " (" + strLabelDate + ")");
                        }
                        else if ((CostItem.WEEKLY).equals(strTimeLineInterval)){
                            nTimeframe = Financials.getFiscalWeekNumber(dtIntervalDate);
                            mapColumn.put("label", strWK + nTimeframe + " (" + strLabelDate + ")");
                        }
                        else if ((CostItem.QUARTERLY).equals(strTimeLineInterval)){
                            nTimeframe = Financials.getFiscalQuarterNumber(dtIntervalDate);
                            mapColumn.put("label", strQ + nTimeframe + " (" + strLabelDate + ")");
                        }else if ((CostItem.ANNUALLY).equals(strTimeLineInterval)){
                            nTimeframe = Financials.getFiscalYear(dtIntervalDate);
                            mapColumn.put("label", strFY + nTimeframe + " (" + strLabelDate + ")");
                        }else{
                            nTimeframe= intervalCalendar.get(Calendar.WEEK_OF_YEAR);
                            mapColumn.put("label", strWK + nTimeframe + " (" + strLabelDate + ")");
                        }
                        mapColumn.put("name", month + "/" + date + "/" + nYear);
                    }
                    else{
                        mapDataColumn= (Map) itrTableRows.next();
                        String PhaseName = (String)mapDataColumn.get(SELECT_NAME);
                        String PhaseId = (String)mapDataColumn.get(SELECT_ID);
                        PhaseStartDate = (String)mapDataColumn.get(SELECT_PROJECT_PHASE_START_DATE);
                        PhaseFinishDate = (String)mapDataColumn.get(SELECT_PROJECT_PHASE_END_DATE);
                        mapColumn = new HashMap();
                        mapColumn.put("isGrid","true");
                        mapColumn.put("label",PhaseName);
                        mapColumn.put("name", PhaseId);

                    }
                    Locale locale = (Locale)requestMap.get("localeObj");
                    String strTimeZone = (String)requestMap.get("timeZone");
                    double clientTZOffset = Task.parseToDouble(strTimeZone);
                    String strPhaseStartDate = eMatrixDateFormat.getFormattedDisplayDate(PhaseStartDate, clientTZOffset, locale);
                    String strPhaseFinishDate = eMatrixDateFormat.getFormattedDisplayDate(PhaseFinishDate, clientTZOffset, locale);
                    mapSettings = new HashMap();
                    mapSettings.put("Registered Suite","ProgramCentral");
                    mapSettings.put("program","emxProjectBudget");
                    mapSettings.put("function","getColumnBudgetData");
                    mapSettings.put("Column Type","program");
                    mapSettings.put("Editable","true");
                    mapSettings.put("Export","true");
                    mapSettings.put("Field Type","attribute");
                    mapSettings.put("Sortable","false");
                    if("PMCProjectBudgetReportTable".equals(tableName)){
                        mapSettings.put("Group Header",strFY+" "+nFiscalDisplayYear+"");
                    }else{
                        mapSettings.put("Edit Access Function","getEditAccessToBudgetRows");
                        mapSettings.put("Edit Access Program","emxProjectBudget");
                        mapSettings.put("Update Program","emxProjectBudget");
                        mapSettings.put("Update Function","updateDynamicColumnPhaseData");
                        mapSettings.put("Group Header",strPhaseStartDate+" - "+strPhaseFinishDate);
                    }
                    mapSettings.put("Style Program","emxFinancialItem");
                    mapSettings.put("Style Function","getBudgetRightAllignedStyleInfo");
                    mapSettings.put("Validate","validateCost");
                    mapColumn.put("settings", mapSettings);

                    // HJ 23-02-13 월 데이터 mapColumn 삭제 처리
                    // mlColumns.add(mapColumn);
                }
                mapColumn = new HashMap();
                mapColumn.put("name", "Total");
                mapColumn.put("label", strTotal);
                //Added:PRG:MS9:R212:26-Aug-2011:IR-088614V6R2012x end
                mapSettings = new HashMap();

                mapSettings = new HashMap();
                mapSettings.put("Registered Suite","ProgramCentral");
                mapSettings.put("program","emxProjectBudget");
                mapSettings.put("function","getColumnBudgetData");
                mapSettings.put("Column Type","program");
                mapSettings.put("Editable","false");
                mapSettings.put("Export","true");
                mapSettings.put("Field Type","string");
                mapSettings.put("Sortable","false");
                mapSettings.put("Style Program","emxFinancialItem");
                mapSettings.put("Style Function","getBudgetRightAllignedStyleInfo");
                mapColumn.put("settings", mapSettings);
                mlColumns.add(mapColumn);

            }

            if((strBudgetState.equals(STATE_BUDGET_PLAN_FROZEN) && !planView.equalsIgnoreCase(selecteView)) && !"PMCProjectBudgetReportTable".equals(tableName))
            {
                mapColumn=new HashMap();
                String planORactual="";
                if(actualView.equalsIgnoreCase(selecteView))
                    planORactual="emxProgramCentral.Financial.Plan";
                else
                    planORactual="emxProgramCentral.Financial.Actual";

                mapColumn.put("name","PlanOrActual");
                mapColumn.put("label",planORactual);
                mapColumn.put("settings", addEstimateTableColumns(context,planORactual));
                mlColumns.add(mapColumn);

                mapColumn=new HashMap();
                mapColumn.put("name","VarianceAmount");
                mapColumn.put("label","emxProgramCentral.Financial.VarianceAmount");
                mapColumn.put("settings", addEstimateTableColumns(context,"Amount"));
                mlColumns.add(mapColumn);
                mapColumn=new HashMap();
                mapColumn.put("name","VariancePercent");
                mapColumn.put("label","emxProgramCentral.Financial.VariancePercent");
                mapColumn.put("settings", addEstimateTableColumns(context,"%"));
                mlColumns.add(mapColumn);
            }
        }catch(Exception exp){
            exp.printStackTrace();
            throw exp;
        }
        return mlColumns;
    }

    /**
     * Generates required Cost Interval columns dynamically
     *
     * @param context The matrix context object
     * @param args The arguments, it contains requestMap
     * @return The MapList object containing definitions about new columns for showing Benefit Intervals
     * @throws Exception if operation fails
     */
    public MapList TESTgetDynamicCostIntervalColumn (Context context, String[] args) throws Exception
    {
        final String strDateFormat= eMatrixDateFormat.getEMatrixDateFormat();
        Map mapProgram = (Map) JPO.unpackArgs(args);
        Map requestMap = (Map)mapProgram.get("requestMap");

        // Following code gets business objects information
        String strObjectId = (String) requestMap.get("objectId");
        String selectedOption = (String) requestMap.get("selectedOption");
        selectedOption = ProgramCentralUtil.isNullString(selectedOption)?"option2":selectedOption;


        // check if budget is present.
        DomainObject dmoProject = DomainObject.newInstance(context, strObjectId);
        // attribute[Cost Interval];
        String SELECT_PROJECT_COST_INTERVAL = "attribute[" + ATTRIBUTE_COST_INTERVAL  + "]";
        StringList busSelect1 = new StringList(2);
        busSelect1.add(SELECT_PROJECT_COST_INTERVAL);
        busSelect1.add(SELECT_CURRENT);
        StringList relSelect1 = new StringList();
        MapList mlBudget = dmoProject.getRelatedObjects(
                context,
                RELATIONSHIP_PROJECT_FINANCIAL_ITEM,
                TYPE_BUDGET,
                busSelect1,
                relSelect1,
                false,
                true,
                (short)1,
                "",
                DomainConstants.EMPTY_STRING);

        if(mlBudget == null || mlBudget.size()<1)
            return mlBudget;

        String strLanguage=context.getSession().getLanguage();
        String strFY= EnoviaResourceBundle.getProperty(context, "ProgramCentral",
                "emxProgramCentral.Financials.FiscalYear", strLanguage);
        String strM= EnoviaResourceBundle.getProperty(context, "ProgramCentral",
                "emxProgramCentral.Financials.Month", strLanguage);
        String strQ= EnoviaResourceBundle.getProperty(context, "ProgramCentral",
                "emxProgramCentral.Financials.Quarter", strLanguage);
        String strWK= EnoviaResourceBundle.getProperty(context, "ProgramCentral",
                "emxProgramCentral.Financials.Week", strLanguage);
        String strTotal= EnoviaResourceBundle.getProperty(context, "ProgramCentral",
                "emxProgramCentral.Financials.Total", strLanguage);
        Date dtFiscalStartDate = null;
        Date dtFiscalEndDate = null;
        Map fiscalYearMap = null;
        Map mapColumn = null;
        Map mapSettings = null;
        Date dtStartDate = null;
        Date dtFinishDate = null;

        String tableName = (String)requestMap.get("selectedTable");
        String displayView = (String)requestMap.get(BUDGETDISPLAYVIEWFILTER);
        if("PMCProjectBudgetReportTable".equals(tableName)){
            displayView = (String)requestMap.get("PMCProjectBudgetReportDisplayViewFilter");
        }
        String strBudgetCostInterval = "";
        String strTimeLineInterval = "";
        String strMonth = "";
        String strBudgetState = "";
        String  selecteView= "";
        String strInterval = "";
        if("PMCProjectBudgetReportTable".equals(tableName)){
            selecteView = actualView;
            strInterval =  (String) requestMap.get("PMCProjectBudgetReportIntervalFilter");
        }else {
            selecteView = (String)requestMap.get(BUDGETVIEWFILTER);
            strInterval = (String)requestMap.get(BUDGETINTERVALFILTER);
        }
        MapList mlColumns = new MapList();
        int interval = 0;
        int index = 0;
        //This is default year interval
        if(ProgramCentralUtil.isNullString(strInterval)){
            Map intervalMap = getBenefitOrBudgetYearRange(context, args);	// to get the list of year intervals.
            StringList slIntervals = (StringList)intervalMap.get("field_choices");
            if(slIntervals.size() != 0){
                strInterval = (String)slIntervals.get(0);	// The very first interval in the list will be taken as default interval.
                index = strInterval.lastIndexOf(" Year");
                strInterval = strInterval.substring(0,index);
                interval = Integer.parseInt(strInterval);
            }
        }
        //User's choice interval
        else{
            index = strInterval.lastIndexOf(" Year");
            strInterval = strInterval.substring(0,index);
            interval = Integer.parseInt(strInterval);
        }
        for (Iterator itrTableRows = mlBudget.iterator(); itrTableRows.hasNext();){
            Map mapDataColumn = new HashMap();
            mapDataColumn= (Map) itrTableRows.next();
            strTimeLineInterval = (String)mapDataColumn.get(SELECT_PROJECT_COST_INTERVAL);
            strBudgetCostInterval= (String)mapDataColumn.get(SELECT_PROJECT_COST_INTERVAL);
            strBudgetState = (String)mapDataColumn.get(SELECT_CURRENT);
        }

        Map mapDate = getMinMaxCostIntervalDates(context,strObjectId);
        String selecteReportView = (String)requestMap.get(BUDGETTIMELINEREPORTFILTER);
        if(null != selecteReportView){
            strTimeLineInterval = selecteReportView;
        }else{
            //Set the interval to weekly only when everything is null.
            if(null==strTimeLineInterval){
                strTimeLineInterval = WEEKLY;
            }
        }

        if(null != mapDate){
            dtStartDate = (Date)mapDate.get("TimeLineIntervalFrom");
            dtFinishDate = (Date)mapDate.get("TimeLineIntervalTo");
        }
        if("FiscalYear".equals(displayView)){
            String strSelectedYear = "";
            strSelectedYear = (String)requestMap.get(BUDGETFISCALYEARFILTER);

            if(null == strSelectedYear || "null".equals(strSelectedYear) || "".equals(strSelectedYear)){
                strSelectedYear = (String)requestMap.get(BUDGETREPORTFISCALYEARFILTER);
            }
            //Added:PRG:MS9:R212:26-Aug-2011:IR-088614V6R2012x
            if(null != strSelectedYear && !"".equals(strSelectedYear) && !"null".equals(strSelectedYear)){
                int year = Integer.parseInt(strSelectedYear);
                fiscalYearMap = Financials.getFiscalInterval(interval, year);
                dtFiscalStartDate = (Date)fiscalYearMap.get("fStartDate");
                dtFiscalEndDate = (Date)fiscalYearMap.get("fEndDate");

            }else{
                Calendar calendar1 = Calendar.getInstance();
                if(null!= dtStartDate){
                    calendar1.setTime(dtStartDate);
                    int nYear = calendar1.get(Calendar.YEAR);
                    fiscalYearMap = Financials.getFiscalInterval(interval, nYear);
                    dtFiscalStartDate = (Date)fiscalYearMap.get("fStartDate");
                    dtFiscalEndDate = (Date)fiscalYearMap.get("fEndDate");
                }
            }
        }else{
            dtFiscalStartDate = (Date)mapDate.get("TimeLineIntervalFrom");
            dtFiscalEndDate = (Date)mapDate.get("TimeLineIntervalTo");
        }

        try{
            int nTimeframe  = 0 ;
            int nYear = 0;
            //Calculation of Fiscal Start Month  and year to display in the groupheader
            int intFiscalYearStartYear = 0;
            int intFiscalYearEndYear = 0;
            String strFiscalStartMonth = "";
            if(null != dtFiscalStartDate){
                Calendar calFiscStartYear =  Calendar.getInstance();
                calFiscStartYear.setTime(dtFiscalStartDate);
                Calendar calFiscEndYear =  Calendar.getInstance();
                calFiscEndYear.setTime(dtFiscalEndDate);
                int intFiscalYearStartMonth = calFiscStartYear.get(Calendar.MONTH)+1;
                strFiscalStartMonth =  getMonthName(context,intFiscalYearStartMonth);
                intFiscalYearStartYear = calFiscStartYear.get(Calendar.YEAR);
                intFiscalYearEndYear = calFiscEndYear.get(Calendar.YEAR);
            }
            mapColumn = new HashMap();
            if(!PROJECT_PHASE.equals(strBudgetCostInterval)){
                if(null != dtStartDate && null != dtFinishDate){

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dtStartDate);

                    ArrayList dateList =  getIntervalDateList(dtStartDate,dtFinishDate,strTimeLineInterval);

                    if(null != dtFiscalStartDate && null != dtFiscalEndDate){
                        dateList =  getIntervalDateList(dtFiscalStartDate,dtFiscalEndDate,strTimeLineInterval);
                    }
                    String strIntervalDate = null;
                    java.util.Set<Integer> intervals = new HashSet<Integer>();
                    for (Iterator itrTableRows = dateList.iterator(); itrTableRows.hasNext();)
                    {
                        Date dtIntervalDate=(Date)itrTableRows.next();
                        Calendar intervalCalendar=Calendar.getInstance();
                        intervalCalendar.setTime(dtIntervalDate);
                        // 날짜 및 년도
                        int day = intervalCalendar.get(Calendar.DAY_OF_MONTH);
                        int month=intervalCalendar.get(Calendar.MONTH)+1;
                        int nFiscalDisplayYear = Financials.getFiscalYear(dtIntervalDate);
                        intervals.add(nFiscalDisplayYear);
                        nYear = intervalCalendar.get(Calendar.YEAR);
                        if(intervals.size() > interval)
                            break;
                        mapColumn = new HashMap();
                        // 4~12, 1~4
                        nTimeframe = Financials.getFiscalMonthNumber(dtIntervalDate);
                        strMonth = getMonthName(context,(dtIntervalDate.getMonth()+1));
                        // HJ 수정 (기존: 일-월-년, 년-월-일)
                        /* String strLabelDate = ""+dtIntervalDate.getDate()+"-"+strMonth+"-"+(dtIntervalDate.getYear()+1900); */
                        String strLabelDate = ""+(dtIntervalDate.getYear()+1900)+"-"+strMonth+"-"+dtIntervalDate.getDate();

                        if(strTimeLineInterval.equals(CostItem.MONTHLY)){
                            // HJ TEST
                            /* mapColumn.put("label", strM+"-"+nTimeframe +" ("+strLabelDate+")"); */
                            if(nTimeframe == 4 || nTimeframe == 1){
                                // HJ TEST
                                mapColumn.put("label", "HJ TEST -" +"day:"+ day +"/month:"+month+"/datalist.size:"+ dateList.size() +"/"+ dtIntervalDate +" ("+strLabelDate+")");
                            }else{
                                mapColumn.put("label", strM+"-"+nTimeframe +" ("+strLabelDate+")");
                            }
                        }
                        else if ((CostItem.WEEKLY).equals(strTimeLineInterval)){
                            nTimeframe = Financials.getFiscalWeekNumber(dtIntervalDate);
                            mapColumn.put("label", strWK+"-"+nTimeframe +" ("+strLabelDate+")");
                        }
                        else if ((CostItem.QUARTERLY).equals(strTimeLineInterval)){
                            nTimeframe = Financials.getFiscalQuarterNumber(dtIntervalDate);
                            mapColumn.put("label", strQ+"-"+nTimeframe +" ("+strLabelDate+")");
                        }
                        else{
                            nTimeframe= month;
                            mapColumn.put("label", strFY+"-"+nFiscalDisplayYear);
                        }
                        String columnName = ProgramCentralConstants.EMPTY_STRING;
                        String dateFormat = strDateFormat.toLowerCase();
                        if(dateFormat.contains("yyyy/mm/dd"))
                        {
                            columnName = nYear + "/" + month + "/" + day;
                        }
                        else if(dateFormat.contains("dd/mm/yyyy"))
                        {
                            columnName = day + "/" + month + "/" + nYear;
                        }
                        else
                        {
                            columnName = month + "/" + day + "/" + nYear;
                        }
                        mapColumn.put("name", columnName);
                        mapSettings = new HashMap();
                        mapSettings.put("Registered Suite","ProgramCentral");
                        mapSettings.put("program","emxProjectBudget");
                        mapSettings.put("function","getColumnBudgetData");
                        mapSettings.put("Column Type","program");
                        mapSettings.put("Editable","true");
                        mapSettings.put("Export","true");
                        mapSettings.put("Field Type","attribute");
                        mapSettings.put("Sortable","false");
                        if(!"PMCProjectBudgetReportTable".equals(tableName)){
                            mapSettings.put("Update Program","emxProjectBudget");
                            mapSettings.put("Update Function","updateDynamicColumnData");
                            mapSettings.put("Edit Access Program","emxProjectBudget");
                            mapSettings.put("Edit Access Function","isColumnEditable");
                        }
                        mapSettings.put("Style Program","emxFinancialItem");
                        mapSettings.put("Style Function","getBudgetRightAllignedStyleInfo");
//                        if("FiscalYear".equals(displayView) || strTimeLineInterval.equals(CostItem.MONTHLY))
//                        {
//                            mapSettings.put("Group Header",strFY+" "+nFiscalDisplayYear+"");
//                        }else{
//                            mapSettings.put("Group Header",strFY+" "+nFiscalDisplayYear+"");
//                        }
                        mapSettings.put("Validate","validateCost");
                        mapColumn.put("settings", mapSettings);
                        mlColumns.add(mapColumn);
                    }//Added:PRG:MS9:R212:26-Aug-2011:IR-088614V6R2012x end
                    mapColumn = new HashMap();
                    mapColumn.put("name", "Total");
                    String strTotalLabel= EnoviaResourceBundle.getProperty(context, "ProgramCentral",
                            "emxProgramCentral.Budget.Total", strLanguage);
                    mapColumn.put("label",strTotalLabel );
                    mapSettings = new HashMap();

                    mapSettings = new HashMap();
                    mapSettings.put("Registered Suite","ProgramCentral");
                    mapSettings.put("program","emxProjectBudget");
                    mapSettings.put("function","getColumnBudgetData");
                    mapSettings.put("Column Type","program");
                    mapSettings.put("Editable","false");
                    mapSettings.put("Export","true");
                    mapSettings.put("Field Type","string");
                    mapSettings.put("Sortable","false");
                    mapSettings.put("Style Program","emxFinancialItem");
                    mapSettings.put("Style Function","getBudgetRightAllignedStyleInfo");
                    mapColumn.put("settings", mapSettings);
                    mlColumns.add(mapColumn);
                }
            }else{
                StringList busSelect = new StringList();
                StringList relSelect = new StringList();
                String whereClause = "";
                String SELECT_PROJECT_PHASE_ID =SELECT_ID;
                String SELECT_PROJECT_PHASE_NAME =SELECT_NAME;
                String SELECT_PROJECT_PHASE_START_DATE = SELECT_ATTRIBUTE_TASK_ESTIMATED_START_DATE;
                String SELECT_PROJECT_PHASE_END_DATE = SELECT_ATTRIBUTE_TASK_ESTIMATED_FINISH_DATE;
                String attrTaskWBSId =  "to["+DomainConstants.RELATIONSHIP_SUBTASK+"].attribute["+DomainConstants.ATTRIBUTE_TASK_WBS+"]";
                busSelect.add(SELECT_PROJECT_PHASE_ID);
                busSelect.add(SELECT_PROJECT_PHASE_NAME);
                busSelect.add(SELECT_PROJECT_PHASE_START_DATE);
                busSelect.add(SELECT_PROJECT_PHASE_END_DATE);
                busSelect.addElement(attrTaskWBSId);
                MapList mlProjectPhase = new MapList();
                mlProjectPhase = getProjectPhases(context,strObjectId,busSelect,relSelect,whereClause);
                if("option2".equals(selectedOption)){
                    mlProjectPhase.sortStructure(context, attrTaskWBSId, "ascending", "emxWBSColumnComparator");
                }else{
                    mlProjectPhase.sortStructure(context, attrTaskWBSId, "ascending", "emxWBSIDComparator");
                }
                String PhaseStartDate ="";
                String PhaseFinishDate ="";
                Map mapDataColumn1 = new HashMap();
                for (Iterator itrTableRows = mlProjectPhase.iterator(); itrTableRows.hasNext();)
                {
                    mapDataColumn1= (Map) itrTableRows.next();
                    PhaseStartDate = (String)mapDataColumn1.get(SELECT_PROJECT_PHASE_START_DATE);
                    PhaseFinishDate = (String)mapDataColumn1.get(SELECT_PROJECT_PHASE_END_DATE);
                }
                //if(null == selecteView && tableName.equals("PMCProjectBudgetReportTable")){
                if("PMCProjectBudgetReportTable".equals(tableName)){
                    mlProjectPhase.clear();
                    ArrayList dateList = new ArrayList();
                    if(ProgramCentralUtil.isNotNullString(strTimeLineInterval) && strTimeLineInterval.contains("Phase")){
                        strTimeLineInterval = WEEKLY;
                    }
                    dateList =  getIntervalDateList(dtStartDate,dtFinishDate,strTimeLineInterval);
                    for(int i=0;i<dateList.size();i++){
                        mlProjectPhase.add(dateList.get(i));
                    }
                }
                String strIntervalDate = null;
                Map mapDataColumn = new HashMap();
                int nFiscalDisplayYear = 0;
                for (Iterator itrTableRows = mlProjectPhase.iterator(); itrTableRows.hasNext();)
                {
                    if( "PMCProjectBudgetReportTable".equals(tableName)){
                        Date dtIntervalDate=(Date)itrTableRows.next();
                        Calendar intervalCalendar=Calendar.getInstance();
                        intervalCalendar.setTime(dtIntervalDate);

                        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                        strIntervalDate = sdf.format(dtIntervalDate);
                        dtIntervalDate = sdf.parse (strIntervalDate);
                        int date = intervalCalendar.get(Calendar.DATE);
                        nYear=intervalCalendar.get(Calendar.YEAR);
                        int month = intervalCalendar.get(Calendar.MONTH);
                        month++;
                        strMonth = getMonthName(context, month);
                        nFiscalDisplayYear = Financials.getFiscalYear(dtIntervalDate);
                        String strLabelDate = "" + date + "-" + strMonth + "-" + nYear;
                        mapColumn = new HashMap();
                        if(strTimeLineInterval.equals(CostItem.MONTHLY)){
                            mapColumn.put("label", strM + "-" + month + " (" + strLabelDate + ")");
                        }
                        else if ((CostItem.WEEKLY).equals(strTimeLineInterval)){
                            nTimeframe = Financials.getFiscalWeekNumber(dtIntervalDate);
                            mapColumn.put("label", strWK + nTimeframe + " (" + strLabelDate + ")");
                        }
                        else if ((CostItem.QUARTERLY).equals(strTimeLineInterval)){
                            nTimeframe = Financials.getFiscalQuarterNumber(dtIntervalDate);
                            mapColumn.put("label", strQ + nTimeframe + " (" + strLabelDate + ")");
                        }else if ((CostItem.ANNUALLY).equals(strTimeLineInterval)){
                            nTimeframe = Financials.getFiscalYear(dtIntervalDate);
                            mapColumn.put("label", strFY + nTimeframe + " (" + strLabelDate + ")");
                        }else{
                            nTimeframe= intervalCalendar.get(Calendar.WEEK_OF_YEAR);
                            mapColumn.put("label", strWK + nTimeframe + " (" + strLabelDate + ")");
                        }
                        mapColumn.put("name", month + "/" + date + "/" + nYear);
                    }
                    else{
                        mapDataColumn= (Map) itrTableRows.next();
                        String PhaseName = (String)mapDataColumn.get(SELECT_NAME);
                        String PhaseId = (String)mapDataColumn.get(SELECT_ID);
                        PhaseStartDate = (String)mapDataColumn.get(SELECT_PROJECT_PHASE_START_DATE);
                        PhaseFinishDate = (String)mapDataColumn.get(SELECT_PROJECT_PHASE_END_DATE);
                        mapColumn = new HashMap();
                        mapColumn.put("isGrid","true");
                        mapColumn.put("label",PhaseName);
                        mapColumn.put("name", PhaseId);

                    }
                    Locale locale = (Locale)requestMap.get("localeObj");
                    String strTimeZone = (String)requestMap.get("timeZone");
                    double clientTZOffset = Task.parseToDouble(strTimeZone);
                    String strPhaseStartDate = eMatrixDateFormat.getFormattedDisplayDate(PhaseStartDate, clientTZOffset, locale);
                    String strPhaseFinishDate = eMatrixDateFormat.getFormattedDisplayDate(PhaseFinishDate, clientTZOffset, locale);
                    mapSettings = new HashMap();
                    mapSettings.put("Registered Suite","ProgramCentral");
                    mapSettings.put("program","emxProjectBudget");
                    mapSettings.put("function","getColumnBudgetData");
                    mapSettings.put("Column Type","program");
                    mapSettings.put("Editable","true");
                    mapSettings.put("Export","true");
                    mapSettings.put("Field Type","attribute");
                    mapSettings.put("Sortable","false");
                    if("PMCProjectBudgetReportTable".equals(tableName)){
                        mapSettings.put("Group Header",strFY+" "+nFiscalDisplayYear+"");
                    }else{
                        mapSettings.put("Edit Access Function","getEditAccessToBudgetRows");
                        mapSettings.put("Edit Access Program","emxProjectBudget");
                        mapSettings.put("Update Program","emxProjectBudget");
                        mapSettings.put("Update Function","updateDynamicColumnPhaseData");
                        mapSettings.put("Group Header",strPhaseStartDate+" - "+strPhaseFinishDate);
                    }
                    mapSettings.put("Style Program","emxFinancialItem");
                    mapSettings.put("Style Function","getBudgetRightAllignedStyleInfo");
                    mapSettings.put("Validate","validateCost");
                    mapColumn.put("settings", mapSettings);
                    mlColumns.add(mapColumn);
                }
                mapColumn = new HashMap();
                mapColumn.put("name", "Total");
                mapColumn.put("label", strTotal);
                //Added:PRG:MS9:R212:26-Aug-2011:IR-088614V6R2012x end
                mapSettings = new HashMap();

                mapSettings = new HashMap();
                mapSettings.put("Registered Suite","ProgramCentral");
                mapSettings.put("program","emxProjectBudget");
                mapSettings.put("function","getColumnBudgetData");
                mapSettings.put("Column Type","program");
                mapSettings.put("Editable","false");
                mapSettings.put("Export","true");
                mapSettings.put("Field Type","string");
                mapSettings.put("Sortable","false");
                mapSettings.put("Style Program","emxFinancialItem");
                mapSettings.put("Style Function","getBudgetRightAllignedStyleInfo");
                mapColumn.put("settings", mapSettings);
                mlColumns.add(mapColumn);

            }

            if((strBudgetState.equals(STATE_BUDGET_PLAN_FROZEN) && !planView.equalsIgnoreCase(selecteView)) && !"PMCProjectBudgetReportTable".equals(tableName))
            {
                mapColumn=new HashMap();
                String planORactual="";
                if(actualView.equalsIgnoreCase(selecteView))
                    planORactual="emxProgramCentral.Financial.Plan";
                else
                    planORactual="emxProgramCentral.Financial.Actual";

                mapColumn.put("name","PlanOrActual");
                mapColumn.put("label",planORactual);
                mapColumn.put("settings", addEstimateTableColumns(context,planORactual));
                mlColumns.add(mapColumn);

                mapColumn=new HashMap();
                mapColumn.put("name","VarianceAmount");
                mapColumn.put("label","emxProgramCentral.Financial.VarianceAmount");
                mapColumn.put("settings", addEstimateTableColumns(context,"Amount"));
                mlColumns.add(mapColumn);
                mapColumn=new HashMap();
                mapColumn.put("name","VariancePercent");
                mapColumn.put("label","emxProgramCentral.Financial.VariancePercent");
                mapColumn.put("settings", addEstimateTableColumns(context,"%"));
                mlColumns.add(mapColumn);
            }
        }catch(Exception exp){
            exp.printStackTrace();
            throw exp;
        }
        return mlColumns;
    }
}
