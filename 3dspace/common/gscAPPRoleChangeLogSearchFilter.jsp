<%-----------------------------------------------------------------------------
* FILE    : gscAPPRoleChangeLogSearchFilter.jsp
* DESC    : 권한 변경 내역 검색 필터 페이지
* VER.    : 1.0
* AUTHOR  : SeungYong,Lee
* PROJECT : HiMSEM Engine Project
*
* Copyright 2018 by ENOVIA All rights reserved.
* -----------------------------------------------------------------
*                           Revision history
* -----------------------------------------------------------------
*
* Since          Author         Description
* -----------   ------------   -----------------------------------
* 2020-08-03     SeungYong,Lee   최초 생성
------------------------------------------------------------------------------%>
<%@page import="com.gsc.apps.common.constants.gscCustomConstants"%>
<%@include file ="emxNavigatorTopErrorInclude.inc" %>
<%@include file="../gscCommonTagLibInclude.inc"%>
<%@include file = "gscCommonStyleInc.inc"%>
<%@include file="../common/gscUICommonAutoComplete.inc"%>
<%@include file = "../emxUICommonHeaderEndInclude.inc"%>
<form name="gscSearchForm">
    <input type="hidden" id="pInitialSearch" name="pInitialSearch" value="false" />
    <table class="form">
    <colgroup>
            <col width="10%"/>
            <col width="15%"/>
            <col width="10%"/>
            <col width="15%"/>
            <col width="10%"/>
            <col width="15%"/>
            <col width="10%"/>
        </colgroup>
        <tbody>
            <tr>
                <td class="label"><emxUtil:i18n localize="i18nId">emxComponents.Column.gscAPPRoleChangeType</emxUtil:i18n></td>
                <td class="inputField">
                    <gscTagUtil:displayMultiSelectUtil selectBoxName="changeType" required="false"
                        showAll="true"
                        isSearchField="true" colName="emxComponents.RegisterSite.SharePointAppName" 
                        resourceName="emxComponentsStringResource" colSize="170"
                        program="gscCommonUtil:getCommonAttributeRangeList"><%=gscCustomConstants.ATTRIBUTE_GSCAPPROLECHANGETYPE %></gscTagUtil:displayMultiSelectUtil>
                </td>
                <td class="label"><emxUtil:i18n localize="i18nId">emxComponents.Label.OrganizationName</emxUtil:i18n></td>
                <td class="inputField">
                    <input type="text" id="Organization Name" name="Organization Name" 
                        colname="<emxUtil:i18nScript localize="i18nId">emxComponents.Label.OrganizationName</emxUtil:i18nScript>" 
                         />
                </td>
                <td class="field schBtn inputField" colspan="3">
                    <button type="button" class="btn-primary">
                        <emxUtil:i18n localize="i18nId">emxComponents.Common.Search</emxUtil:i18n>
                    </button>
                </td>
            </tr>
            <tr>
                <td class="label"><emxUtil:i18n localize="i18nId">emxComponents.Common.UserName</emxUtil:i18n></td>
                <td class="inputField">
                    <input type="text" id="nameDisplay" name="nameDisplay" value=""
                        colname="<emxUtil:i18nScript localize="i18nId">emxComponents.Common.UserName</emxUtil:i18nScript>"
                        onKeyDown="searchCommonPerson(this, parent, '', 'name');"/>
                    <input type="button" value="..." onClick="Javascript:showPerson('filterFrame', 'nameDisplay', '', 'name');" />
                    <a href="javascript:clear('nameDisplay');clear('name');">

                        <emxUtil:i18nScript localize="i18nId">emxComponents.Label.Search.Clear</emxUtil:i18nScript>
                    </a>
                    <input type="hidden" id="name" name="name" value="" />
                </td>
                <td class="label"><emxUtil:i18nScript localize="i18nId">emxComponents.Common.Originated</emxUtil:i18nScript></td>
                <td class="inputField" colspan="4">
                    <gscTagUtil:fieldDateRange formName="gscSearchForm"
                            colName="emxComponents.Common.Originated"
                            resourceName="emxComponentsStringResource"
                            selectDateRangeName="originated" showDefault="true" />
                </td>
            </tr>
        </tbody>
    </table>
</form>

<script type="text/javascript">
    $(function () {
        $("button.btn-primary").on("click", function () {
            parent.doFilter();
        });
        $("#tableFrame", parent.document).on("load", function(){
            $("#pInitialSearch").val("true");
        });
    });
</script>
<%@include file="../emxUICommonEndOfPageInclude.inc" %>