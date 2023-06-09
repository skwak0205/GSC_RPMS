/*
**   emxLocation
**
**   Copyright (c) 1992-2020 Dassault Systemes.
**   All Rights Reserved.
**   This program contains proprietary and trade secret information of MatrixOne,
**   Inc.  Copyright notice is precautionary only
**   and does not evidence any actual or intended publication of such program
**
*/

import com.matrixone.apps.common.util.ComponentsUtil;

import matrix.db.*;

/**
 * The <code>emxLocation</code> class contains methods for emxLocation.
 *
 * @version Common 10.5.1.2 - Copyright(c) 2004, MatrixOne, Inc.
 */

public class emxLocation_mxJPO extends emxLocationBase_mxJPO
{
      /**
       * Constructor.
       *
       * @param context the eMatrix <code>Context</code> object
       * @param args holds no arguments
       * @throws Exception if the operation fails
       * @since Common 10.5.1.2
       * @grade 0
       */
      public emxLocation_mxJPO (Context context, String[] args)
          throws Exception
      {
          super(context, args);
      }

      /**
       * This method is executed if a specific method is not specified.
       *
       * @param context the eMatrix <code>Context</code> object
       * @param args holds no arguments
       * @returns int
       * @throws Exception if the operation fails
       * @since Common 10.5.1.2
       */
      public int mxMain(Context context, String[] args)
          throws Exception
      {
          if (true)
          {
              throw new Exception(ComponentsUtil.i18nStringNow("emxComponents.Location.SpecifyMethodLocationInvocation", context.getLocale().getLanguage()));
          }
          return 0;
      }

}
