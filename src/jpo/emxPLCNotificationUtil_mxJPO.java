/*
 ** emxPLCNotificationUtil.java
 **
 ** Copyright (c) 1992-2020 Dassault Systemes.
 **
 ** All Rights Reserved.
 ** This program contains proprietary and trade secret information of MatrixOne, Inc.
 ** Copyright notice is precautionary only and does not evidence any actual or intended
 ** publication of such program.
 **
 */

import matrix.db.Context;

/**
 * The <code>emxPLCNotificationUtil</code> class contains common notification utility methods for PLC
 * @version Variant Configuration R207 - Copyright (c) 2008-2020 Dassault Systemes.
 * @since PLC R207
 */

public class emxPLCNotificationUtil_mxJPO extends emxPLCNotificationUtilBase_mxJPO
{
   /**
    * Constructor.
    *
    * @param context the eMatrix <code>Context</code> object
    * @param args holds no arguments
    * @throws Exception if the operation fails
    */
   public emxPLCNotificationUtil_mxJPO (Context context, String[] args) throws Exception
   {
      super(context, args);
   }
}
