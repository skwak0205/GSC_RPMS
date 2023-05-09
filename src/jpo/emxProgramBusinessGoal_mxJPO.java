/*
 *  emxProgramBusinessGoal.java
 *
 * Copyright (c) 2003-2020 Dassault Systemes.
 *
 * All Rights Reserved.
 * This program contains proprietary and trade secret information of
 * MatrixOne, Inc.  Copyright notice is precautionary only and does
 * not evidence any actual or intended publication of such program.
 *
 * static const char RCSID[] = $Id: ${CLASSNAME}.java.rca 1.8.2.1 Thu Dec  4 07:56:09 2008 ds-ss Experimental ${CLASSNAME}.java.rca 1.8 Wed Oct 22 15:49:52 2008 przemek Experimental przemek $
 */

import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.program.BusinessGoal;
import com.matrixone.apps.program.ProgramCentralConstants;
import matrix.db.*;
import matrix.util.StringList;

import java.lang.*;
import java.util.HashMap;
import java.util.Map;
/**
 * @version PMC 10.0.0.0 - Copyright (c) 2003, MatrixOne, Inc.
 */
public class emxProgramBusinessGoal_mxJPO extends emxProgramBusinessGoalBase_mxJPO
{

    /**
     * Constructor
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @throws Exception if the operation fails
     * @since PMC 10.0.0.0
     */
    public emxProgramBusinessGoal_mxJPO (Context context, String[] args)
            throws Exception
    {
        super(context, args);
    }

    // HJ - Project Management 비즈니스 목표
    @com.matrixone.apps.framework.ui.ProgramCallable
    public MapList getTopandMyBusinessGoals(Context context, String[] args)
            throws Exception
    {
        try
        {
            DomainObject dmObj = DomainObject.newInstance(context);

            String ownerExpression = "(owner ~= \"" + context.getUser() + "\")";
            String stateExpression = "(current != \"" + STATE_BUSINESS_GOALS_COMPLETE + "\")";
            String tosubGoalExpressionFalse = "(to[Sub Goal]==false)";
            String FromsubGoalExpressiontrue = "(from[Sub Goal]==true)";
            MapList businessGoalList = new MapList();
            // Add selectables
            StringList busSelects = new StringList(1);
            busSelects.add(BusinessGoal.SELECT_ID);
            //need to have a filter
            //this is the filter for displaying user's BusinessGoal objects

            String whereExpression = ownerExpression + " && " + stateExpression + "&&" + tosubGoalExpressionFalse + "&&" + FromsubGoalExpressiontrue;
            businessGoalList = DomainObject.findObjects(context,
                    BusinessGoal.TYPE_BUSINESS_GOAL,
                    null,
                    whereExpression,
                    busSelects);


            return businessGoalList;
        }
        catch (Exception ex)
        {
            throw ex;
        }

    }

    // HJ
    @com.matrixone.apps.framework.ui.ExcludeOIDProgramCallable
    public StringList excludeBusinessGoalforAddExisting(Context context, String[] args)
            throws Exception
    {
        MapList mlBusinessGoalList = new MapList();
        StringList slBusinessGoal = new StringList();
        Map mpObjectMap= new HashMap();

        HashMap programMap = (HashMap) JPO.unpackArgs(args);
        String strProjectId=(String) programMap.get("parentOID");
        DomainObject dmObj = DomainObject.newInstance(context);

        StringList slSelectables = new StringList(1);
        slSelectables.add(DomainObject.SELECT_ID);

        String busWhere =("current=='" + STATE_BUSINESS_GOALS_COMPLETE + "'");
        busWhere += (" || from["+ ProgramCentralConstants.RELATIONSHIP_SUBGOAL+"].to.type =='" + DomainObject.TYPE_BUSINESS_GOAL + "'");
        busWhere += (" || from[Business Goal Project Space].to.id=='" + strProjectId + "'");

        mlBusinessGoalList=	findObjects( context,
                DomainObject.TYPE_BUSINESS_GOAL,
                null,
                busWhere,
                slSelectables);

        for (int i = 0; i <  mlBusinessGoalList.size(); i++){
            mpObjectMap = (Map) mlBusinessGoalList.get(i);
            String strBusinessGoal = (String)mpObjectMap.get(DomainObject.SELECT_ID);
            slBusinessGoal.add(strBusinessGoal);
        }

        return slBusinessGoal;

    }
}
