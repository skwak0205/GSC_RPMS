/*
 *  emxPerson.java
 *
 * Copyright (c) 1992-2020 Dassault Systemes.
 *
 * All Rights Reserved.
 * This program contains proprietary and trade secret information of
 * MatrixOne, Inc.  Copyright notice is precautionary only and does
 * not evidence any actual or intended publication of such program.
 *
 */
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.util.*;
import com.matrixone.apps.framework.ui.UIUtil;
import matrix.db.*;
import matrix.util.StringList;

import java.lang.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @version AEF Rossini - Copyright (c) 2002, MatrixOne, Inc.
 */
public class emxPerson_mxJPO extends emxPersonBase_mxJPO
{

    /**
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @throws Exception if the operation fails
     * @since AEF Rossini
     * @grade 0
     */
    public emxPerson_mxJPO (Context context, String[] args)
        throws Exception
    {
      super(context, args);
    }

    /***
     * fullname에 부서명 포함하도록 수정.
     * @param context
     * @param args
     * @return
     * @throws Exception
     */
    public String getFullName(Context context, String[] args) throws Exception
    {
        String result = "";

        String[] adminUsers = { "SLMInstallerAdmin", "3DIndexAdminUser", "ENOVIA_CLOUD", "User Agent" };
        List<String> lAdminUsers = Arrays.asList(adminUsers);
        StringList fullNames = new StringList();
        String fullName = DomainConstants.EMPTY_STRING;
        char VALUE_SEPARATOR_CHAR = 0x07;
        if(UIUtil.isNotNullAndNotEmpty(args[0]) && args[0].contains(String.valueOf(VALUE_SEPARATOR_CHAR))){
            StringList slUsernames = FrameworkUtil.split(args[0], String.valueOf(VALUE_SEPARATOR_CHAR));

            for (Iterator iterator = slUsernames.iterator(); iterator.hasNext();) {
                String username = (String) iterator.next();
                if(UIUtil.isNotNullAndNotEmpty(username) && !lAdminUsers.contains(username)) {
                    fullName = PersonUtil.getFullName(context, username);
                    String buName = MqlUtil.mqlCommand(context,"print bus Person "+username+" - select to[Business Unit Employee].from.attribute[Title] dump");
                    fullName += "["+buName+"]";
                } else {
                    fullName = "";
                }
                fullNames.add(fullName);
            }
            result = FrameworkUtil.join(fullNames, String.valueOf(VALUE_SEPARATOR_CHAR));

        } else if(UIUtil.isNotNullAndNotEmpty(args[0]) && !lAdminUsers.contains(args[0])) {
            result = PersonUtil.getFullName(context, args[0]);
        }
        return result;

    }


}
