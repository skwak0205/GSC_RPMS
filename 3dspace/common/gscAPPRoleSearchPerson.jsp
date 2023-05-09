<%-----------------------------------------------------------------------------
* FILE    : gscAPPRoleSearchPerson.jsp
* DESC    : Role Update Page Search Person
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
* 2020-09-04     SeungYong,Lee   최초 생성
------------------------------------------------------------------------------%>
<%@page import="com.gsc.apps.common.constants.gscCustomConstants"%>
<%@include file ="emxNavigatorTopErrorInclude.inc" %>
<%@include file="../gscCommonTagLibInclude.inc"%>
<%@include file = "gscCommonStyleInc.inc"%>
<%@include file="../common/gscUICommonAutoComplete.inc"%>
<%@include file = "../emxUICommonHeaderEndInclude.inc"%>
<form name="gscSearchForm">
    <table class="form">
    <colgroup>
                <col width="10%"/>
                <col width="30%"/>
                <col width="10%"/>
                <col width="30%"/>
        </colgroup>
        <tbody>
            <tr>
                <td class="label"><emxUtil:i18n localize="i18nId">emxComponents.Common.Header.FromUser</emxUtil:i18n></td>
                <td class="inputField">
                    <input type="text" id="fromNameDisplay" name="fromNameDisplay" value=""
                        colname="<emxUtil:i18nScript localize="i18nId">emxComponents.Common.Header.FromUser</emxUtil:i18nScript>"
                        notnull onkeydown="searchFromPerson(this);"  />
                    <a href="javascript:clear('fromNameDisplay');clear('fromName');">
                        <emxUtil:i18nScript localize="i18nId">emxComponents.Label.Search.Clear</emxUtil:i18nScript>
                    </a>
                    <input type="hidden" id="fromName" name="fromName" value="" />
                    <a class="footericon" id="fromRefresh" href="javascript:fromRefresh()" style="margin-left: 4px;"><img src="images/iconActionRefresh.gif" border="0"  /></a>
                </td>
                <td class="label"><emxUtil:i18nScript localize="i18nId">emxComponents.Common.Header.ToUser</emxUtil:i18nScript></td>
                <td class="inputField">
                    <input type="text" id="toNameDisplay" name="toNameDisplay" value=""
                        colname="<emxUtil:i18nScript localize="i18nId">emxComponents.Common.Header.ToUser</emxUtil:i18nScript>"
                        notnull onkeydown="searchToPerson(this);"/>
                    <a href="javascript:clear('toNameDisplay');clear('toName');">
                        <emxUtil:i18nScript localize="i18nId">emxComponents.Label.Search.Clear</emxUtil:i18nScript>
                    </a>
                    <input type="hidden" id="toName" name="toName" value="" />
                    <a class="footericon" id="toRefresh" href="javascript:toRefresh()" style="margin-left: 4px;"><img src="images/iconActionRefresh.gif" border="0"  /></a>
                </td>
                <td class="inputField"></td>
                <td class="field schBtn inputField" colspan="2">
                    <button type="button" class="btn-primary" onclick="checkCopyLicenseToUser();">
                        <emxUtil:i18n localize="i18nId">emxComponents.Common.Button.CopyLicense</emxUtil:i18n>
                    </button>
                </td>
                <td class="inputField"></td>
                <td class="field schBtn inputField" colspan="2">
                    <button type="button" class="btn-primary" onclick="checkCopyRoleToUser();">
                        <emxUtil:i18n localize="i18nId">emxComponents.Common.Button.CopyRole</emxUtil:i18n>
                    </button>
                </td>
            </tr>
        </tbody>
    </table>
</form>
<script type="text/javascript">

var iDataFrame = parent.document.getElementById('iDataFrame');
var progress   = getTopWindow().document.querySelector('#imgProgressDiv');

// 사용자 이름   First Name + [사번]으로 출력
$("#fromNameDisplay").off('change').on('change',fromChangeDisplayName);
function fromChangeDisplayName(){
    var nameValue = $('#fromName').val();
    if(nameValue != null && nameValue != "") {
        var name = $('#fromNameDisplay').val() + " [" + $('#fromName').val() + "]";
        $('#fromNameDisplay').val(name);
    }
}
$("#toNameDisplay").off('change').on('change',toChangeDisplayName);
function toChangeDisplayName(){
    var nameValue = $('#toName').val();
    if(nameValue != null && nameValue != "") {
        var name = $('#toNameDisplay').val() + " [" + $('#toName').val() + "]";
        $('#toNameDisplay').val(name);
    }
}



// 자동완성 기능 (엔터나 탭 입력시 Display value 사라지는것 방지)
function searchFromPerson(obj) {
    if(event.keyCode != 13 && event.keyCode != 9){
        searchRoleUpdatePerson(obj, parent, '', 'fromName');
    }
}
function searchToPerson(obj) {
    if(event.keyCode != 13 && event.keyCode != 9){
        searchRoleUpdatePerson(obj, parent, '', 'toName');
    }
}

// 자동완성 기능에서 Enter 입력시 새로고침
$('#fromNameDisplay').keyup(function(e) {
    if(e.keyCode == 13) fromRefresh();
});
$('#toNameDisplay').keyup(function(e) {
    if(e.keyCode == 13) toRefresh();
});

function fromRefresh(){
    $('#fromRefresh').focus();
    $('#fromNameDisplay').focus();
    var fromName   =  $('#fromName').val();
    if(fromName != null && fromName != "") {
        $("#PLM_ExternalID").val(fromName);
        progress.style.visibility = "";
        iDataFrame.src = "gscAPPRoleUpdateTable.jsp?source=Admin&userType=fromUser&toUser=false&PLM_ExternalID=" + fromName;
    }
}
function toRefresh() {
    $('#toRefresh').focus();
    $('#toNameDisplay').focus();
    var toName   = $('#toName').val();
    if(toName != null && toName != "") {
        $("#PLM_ExternalID").val(toName);
        progress.style.visibility = "";
        iDataFrame.src = "gscAPPRoleUpdateTable.jsp?source=Admin&userType=toUser&toUser=true&PLM_ExternalID=" + toName;
    }
}

// Copy Role 기능 적용 시 From User와 To User가 모두 입력되야하기 위한 방지 기능
function checkCopyRoleToUser(){
    var fromName = document.getElementById('fromName').value;
    var toName = document.getElementById('toName').value;
    if (fromName == "" || fromName == null) {
        alert("<emxUtil:i18n localize="i18nId">emxComponents.Common.Alert.SearhFrom</emxUtil:i18n>");
    } else if (toName == "" || toName == null) {
        alert("<emxUtil:i18n localize="i18nId">emxComponents.Common.Alert.SearhTo</emxUtil:i18n>");
    } else {
        copyRoleToUser(fromName,toName);
    }
}

// Copy Role 기능이며 gscAPPRoleUpdateTable.jsp의 sendToUpdatePage() 기능을 실행
function copyRoleToUser(fromName,toName) {
    var externalId = iDataFrame.contentDocument.getElementById('PLM_ExternalID');
    var fromUserVal = iDataFrame.contentDocument.getElementById('fromUser');
    externalId.value = toName;
    fromUserVal.value = fromName;
    iDataFrame.contentWindow.sendToUpdatePage();
}

// Copy License 기능 적용 시 From User와 To User가 모두 입력되야하기 위한 방지 기능
function checkCopyLicenseToUser(){
    var fromName = document.getElementById('fromName').value;
    var toName = document.getElementById('toName').value;
    if (fromName == "" || fromName == null) {
        alert("<emxUtil:i18n localize="i18nId">emxComponents.Common.Alert.SearhFrom</emxUtil:i18n>");
    } else if (toName == "" || toName == null) {
        alert("<emxUtil:i18n localize="i18nId">emxComponents.Common.Alert.SearhTo</emxUtil:i18n>");
    } else {
        copyLicenseToUser(fromName,toName);
    }
}

// Copy License 기능를 위한 기능으로 gscAPPRoleUpdateTable.jsp에서 From User의 License 정보를 불러옴.
function copyLicenseToUser(fromName,toName) {
    var externalId = iDataFrame.contentDocument.getElementById('PLM_ExternalID');
    var fromUserVal = iDataFrame.contentDocument.getElementById('fromUser');
    var isLicense = iDataFrame.contentDocument.getElementById('isLicense');
    externalId.value = toName;
    fromUserVal.value = fromName;
    isLicense.value = "true";
    iDataFrame.src = "gscAPPRoleUpdateTable.jsp?source=Admin&userType=toUser&toUser=true&isLicense=true&fromUserName=" + fromName + "&PLM_ExternalID=" + toName;
}


</script>
<%@include file="../emxUICommonEndOfPageInclude.inc" %>