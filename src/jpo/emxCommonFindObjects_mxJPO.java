/*
 * emxCommonFindObjects.java program to get all document type Object Ids.
 *
 * Copyright (c) 1992-2020 Dassault Systemes.
 *
 * All Rights Reserved.
 * This program contains proprietary and trade secret information of
 * MatrixOne, Inc.  Copyright notice is precautionary only and does
 * not evidence any actual or intended publication of such program.
 *
 */

import matrix.db.*;
import java.lang.*;
/**
 * @version AEF 10.5 - Copyright (c) 2002, MatrixOne, Inc.
 */
public class emxCommonFindObjects_mxJPO extends emxCommonFindObjectsBase_mxJPO
{

    /**
     *
     * @param context the eMatrix <code>Context</code> object
     * @param args holds no arguments
     * @throws Exception if the operation fails
     * @since AEF Rossini
     * @grade 0
     */
    public emxCommonFindObjects_mxJPO (Context context, String[] args)
        throws Exception
    {
      super(context, args);
    }
}
