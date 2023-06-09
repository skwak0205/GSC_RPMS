<%--  emxComponentSetCompanyKeyInRPE.inc  -- set company key in rpe

    Copyright (c) 1992-2020 Dassault Systemes.All Rights Reserved.
    This program contains proprietary and trade secret information of MatrixOne,Inc.
    Copyright notice is precautionary only and does not evidence any actual or intended publication of such program

    static const char RCSID[] = $Id: emxComponentsSetCompanyKeyInRPE.inc.rca 1.7 Wed Oct 22 16:18:52 2008 przemek Experimental przemek $
--%>

<%
  // Getting Company Key from session
  String companyKey = (String)session.getAttribute(com.matrixone.apps.common.Person.RPE_COMPANY_KEY);

  // Check Company key is null then get comapny key from admin person
  // and set in session for fututre
  if ( companyKey == null )
  {
      companyKey = com.matrixone.apps.common.Person.getCompanyKey(context);
      session.setAttribute(com.matrixone.apps.common.Person.RPE_COMPANY_KEY, companyKey);
  }

  // setting Comapny key as RPE value for triggers
  PropertyUtil.setGlobalRPEValue(context, com.matrixone.apps.common.Person.RPE_COMPANY_KEY, companyKey);
%>
