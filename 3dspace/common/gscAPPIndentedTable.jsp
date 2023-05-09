<%-----------------------------------------------------------------------------
* FILE    : gscAPPIndentedTable.jsp
* DESC    : Filter Frame 적용 Indented Table
* VER.    : 1.0
* AUTHOR  : GeonHwan,Bae
* PROJECT : HiMSEN Engine Project
*
* Copyright 2018 by ENOVIA All rights reserved.
* -----------------------------------------------------------------
*                         Revision history
* -----------------------------------------------------------------
* Since          Author         Description
* ------------   ------------   -----------------------------------
* 2020-05-22     GeonHwan,Bae   최초 생성
------------------------------------------------------------------------------%>
<%@page import="com.gsc.apps.common.util.gscConvertUtil"%>
<%@page import="com.gsc.apps.common.util.gscStringUtil"%>
<%@include file = "../emxContentTypeInclude.inc"%>
<%@include file = "./emxNavigatorBaseInclude.inc"%>
<%@include file="./emxUIConstantsInclude.inc"%>
<%@ page import="com.matrixone.apps.domain.util.*" %>
<%@ page import="java.util.Enumeration" %>

<%
try{
    String program = emxGetParameter(request,"program");
    String table = emxGetParameter(request,"table");
    String filterFramePage = emxGetParameter(request,"FilterFramePage");
    int intRowHeight = 29;
    String filterFrameCount = gscStringUtil.NVL(emxGetParameter(request,"FilterFrameSize"), "-1");

    String filterFrameSize = gscConvertUtil.castString(gscConvertUtil.castint(filterFrameCount) * intRowHeight);

    String filterFrameProcess = emxGetParameter(request,"FilterFrameProcess");
    String suiteKey = emxGetParameter(request,"suiteKey");
    String header = emxGetParameter(request,"header");
    String hideOuterHeader = gscStringUtil.NVL(emxGetParameter(request, "hideOuterHeader"));
    String strFields = gscStringUtil.NVL(emxGetParameter(request, "fields"));
    String strReadOnlyFields = gscStringUtil.NVL(emxGetParameter(request, "disabledFields"));
    String topPixel = "35px";

    String stringResourceFile = UINavigatorUtil.getStringResourceFileId(context, suiteKey);
    String languageStr = request.getHeader("Accept-Language");
    String pageHeading = "";
    if(header!=null && !"".equals(header)){
        pageHeading = i18nNow.getI18nString(header,stringResourceFile,languageStr);
    }else{
        pageHeading = "Indented View";
    }

    if(gscStringUtil.isEmpty(filterFrameProcess)) {
        filterFrameProcess = "../common/gscAPPIndentedTableFilterProcess.jsp";
    }
    Enumeration eNumParameters = emxGetParameterNames(request);
    String urlParameter = "";
    while( eNumParameters.hasMoreElements() ) {
        String paramName = (String)eNumParameters.nextElement();
        String paramValue = (String)emxGetParameter(request,paramName);

        //url encode 추가
        if(gscStringUtil.isNotEmpty(paramValue)) {
            paramValue = FrameworkUtil.encodeURL(paramValue);
        }

        if(urlParameter==null || "".equals(urlParameter)){
            urlParameter = "?" + paramName + "=" + paramValue;
        }else{
            urlParameter += "&" + paramName + "=" + paramValue;
        }
    }
    String targetLocation = gscStringUtil.NVL(emxGetParameter(request,"targetLocation"));
    String indentedTableUrl = "../common/emxIndentedTable.jsp";

    filterFramePage += urlParameter;
    filterFrameProcess += urlParameter;
    indentedTableUrl += urlParameter;

//    filterFramePage = XSSUtil.encodeURLwithParsing(context, filterFramePage);
//    filterFrameProcess = XSSUtil.encodeURLwithParsing(context, filterFrameProcess);
//    indentedTableUrl = XSSUtil.encodeURLwithParsing(context, indentedTableUrl);

    String sPrevious = EnoviaResourceBundle.getProperty(context, "Framework", "emxFramework.BackToolbarMenu.label",languageStr);
    String sNext = EnoviaResourceBundle.getProperty(context, "Framework", "emxFramework.ForwardToolbarMenu.label",languageStr);
    String appMenuLabel = EnoviaResourceBundle.getProperty(context, "Framework", "emxNavigator.UIMenu.ApplicationMenu",languageStr);

%>
<%@include file="../emxUICommonHeaderBeginInclude.inc"%>
<title><%=EnoviaResourceBundle.getProperty(null, "emxFramework.Common.defaultPageTitle")%></title>
<link rel="stylesheet" href="./styles/emxUICalendar.css" ></link>
<script type="text/javascript" src="./scripts/emxUICoreMenu.js"></script>
<script type="text/javascript" src="./scripts/emxUICalendar.js"></script>
<script language="JavaScript" src="./scripts/jquery-latest.js"></script>
<script language="javascript" src="./scripts/jquery-ui.min-xparam.js"></script>
<script language="javascript" src="../common/scripts/emxUIUtility.js"></script>
<link rel="stylesheet" type="text/css" href="./styles/gscUICustomStyle.css" />
<style>
input[type="checkbox"][type="radio"][readonly] {
  pointer-events: none;
}
</style>
<script language="Javascript">
    addStyleSheet("emxUIDefault");
    addStyleSheet("emxUIToolbar");
    addStyleSheet("emxUIMenu");
    addStyleSheet("emxUIDOMLayout");
    addStyleSheet("emxUIDialog");
    addStyleSheet("emxUIChannelDefault");
    addStyleSheet("emxUIList");
    addStyleSheet("emxUIForm");

    if(getTopWindow().isMobile){
        addStyleSheet("emxUIMobile", "mobile/styles/");
    }

    var filterFrameCount = <%= filterFrameCount%>;
    var filterFrameSize = 0;
    var totalFilterFrameHeight = 0;
    var topPageHeight = 2;
    
    /**
     * 조회입력 부분과 Table의 크기 및 위치를 재조정하는 함수 
     *
     * @returns
     */
    function resizeFilter(){
        var firstTrObject = $("#filterFrame").contents().find("table").eq(0).find("tr:first");
        var trFilterObject = firstTrObject.siblings();
        var bleHasFilter= false;
        var intTempFilterCount = 0;
        var intFilterFrameRowIndex = -1;
        var intFilterRowCount = 0;
        filterFrameSize = 0;
        totalFilterFrameHeight = 0;
        if (firstTrObject) {
            if (filterFrameCount >= 1) {
                filterFrameSize += $(firstTrObject).height();
                intTempFilterCount = filterFrameCount;
            }
        
            bleHasFilter = true;
            
            totalFilterFrameHeight+= $(firstTrObject).height();
            intFilterRowCount++;
        }
        
        $.each(trFilterObject, function(idx,element) {
            if (bleHasFilter && (intTempFilterCount > (idx+1))) {
                filterFrameSize += $(element).height();
            }

            totalFilterFrameHeight+= $(element).height();
            intFilterRowCount++;
        });

        if (filterFrameCount <= 0) {
            filterFrameSize = 0;
        }
        if ((intFilterRowCount <= filterFrameCount) || filterFrameCount < 0){
            $("#divFilterDivision").hide();
        } else {
            $("#divFilterDivision").show();
        }
        
        $("#filterFrame").height(filterFrameSize);

        
        if($("#divFilterDivision").is(':visible')) {
        	var obj = $('.schClose button');
            if ($(obj).hasClass('on')) {
                if (totalFilterFrameHeight >= 0) {
                    document.getElementById("filterFrame").style.height = totalFilterFrameHeight + "px";
                }
            } else {
                document.getElementById("filterFrame").style.height = filterFrameSize + "px";
            }
        }        
        reloadSize();
    }
    
    /**
     * 화면의 width,heigh를 조정한다.
     *
     * @returns
     */
    function reloadSize(){
        var divPageBodyHeight = document.getElementById("divPageBody").clientHeight;
        
        var divFilterFrameHeight = $("#filterFrame").height();
        var divFilterDivisionTop = 0;
        
        if (divFilterFrameHeight!== undefined)
        	divFilterDivisionTop = divFilterFrameHeight+1;
        
        if($("#divFilterDivision").is(':visible')) {
            $("#divFilterDivision").css("top", divFilterDivisionTop + "px");

            var divFilterDivisionHeight = $("#divFilterDivision").height();
            divPageBodyHeight = divPageBodyHeight - divFilterFrameHeight - divFilterDivisionHeight;
            $("#tableFrame").css("top", (divFilterFrameHeight + divFilterDivisionHeight) + "px");
            
            document.getElementById("tableFrame").style.height = divPageBodyHeight + "px";
        } else {
            divPageBodyHeight = divPageBodyHeight - divFilterFrameHeight;
            $("#tableFrame").css("top", (divFilterDivisionTop ) + "px");
            document.getElementById("tableFrame").style.height = (divPageBodyHeight - topPageHeight) + "px";
        }
    }

    /**
     * 검색을 실행한다.
     *
     * @returns
     */
    function doFilter(){
        if(!getTopWindow().findFrame(window, "tableFrame").oXML) return;

        var filterFrame = findFrame(window, "filterFrame");
        var formObject = filterFrame.document.forms[0];
        // validationFormValue 실행 ( Validation 체크 )
        if (typeof filterFrame.validationFormValue == "function" && !(filterFrame.validationFormValue(formObject))) {
            return;
        }

        //Filter Frame에 정의되어 있는 Parameter 값 초기화
        for(var i=0; i<formObject.elements.length; i++){
            var elementsName = formObject.elements[i].name;
            if(elementsName!=null && elementsName!=undefined && elementsName!="") {
                removeParameter(elementsName);
            }
        }
        
        // pageNumber 초기화
        removeParameter("pageNumber");

        //searchProcess();
        getTopWindow().showMask();

        var filterTimeStamp = (findFrame(window, "tableFrame").document.getElementsByName("timeStamp")[0] !=null ) ? findFrame(window, "tableFrame").document.getElementsByName("timeStamp")[0].value : "";
        formObject.method="post";
        formObject.target="pagehidden";
        formObject.action="<%=filterFrameProcess%>&filterTimeStamp=" + filterTimeStamp;
        formObject.submit();
    }

    /**
     * 조회 Progress Bar 실행
     *
     * @returns
     */
    function searchProcess() {
        document.getElementById('imgLoadingProgressDiv').style.visibility = 'visible';
        document.getElementById('imgLoadingProgressDiv').innerHTML = "Searching...";
    }

    /**
     * 조회 Progress Bar 실행 중지
     *
     * @returns
     */
    function searchProcessOff() {
        document.getElementById('imgLoadingProgressDiv').style.visibility = 'hidden';
    }

    /**
     * Page 로드 후 기본 사이즈 및 버튼에 대한 초기화 하는 함수. 
     *
     * @returns
     */
    function onLoad(){
        initTableFrame();
        resizeFilter();
        
        //search Area
        $('.schClose button').on("click", function() {
        	toggleFilterFrame(this);
        });

        $('.home button').on("click", function() {
            getTopWindow().launchHomePage();
        });

        $('.previous button').on("click", function() {
            getTopWindow().bclist.goBack();
            return false;
        });

        $('.next button').on("click", function() {
            getTopWindow().bclist.goForward();
            return false;
        });
        
        toggleFilterFrame($('.schClose button'));
    }
    
    /**
     * Filter Frame size 조절클릭시 실행되는 함수.
     *
     * @param obj Size 조절 버튼
     * @returns
     */
    function toggleFilterFrame(obj) {
        if (!$(obj).hasClass('on')) {
            if (totalFilterFrameHeight >= 0) {
                document.getElementById("filterFrame").style.height = totalFilterFrameHeight + "px";
            }

            $(obj).addClass('on');
        } else {
            document.getElementById("filterFrame").style.height = filterFrameSize + "px";
            $(obj).removeClass('on');
        }
        
        reloadSize();
        
        return false;
    }

    function initTableFrame(){
        findFrame(window, "filterFrame").document.forms[0].method="post";
        findFrame(window, "filterFrame").document.forms[0].target="tableFrame";
        findFrame(window, "filterFrame").document.forms[0].action="<%=indentedTableUrl%>";
        findFrame(window, "filterFrame").document.forms[0].submit();
    }

    /**
     * 조회시에 기존의 파라미터를 제거한 후 조회필드에 정의한 파라미터로 url을 만드는 함수 
     *
     * @param param
     * @returns
     */
    function removeParameter(parm){
        var urlParameters = findFrame(window, "tableFrame").urlParameters || "";

        if(urlParameters.indexOf("amp;") >= 0){
            while(urlParameters.indexOf("amp;") >= 0){
                urlParameters = urlParameters.replace("amp;", "");
            }
        }

        var arrURLparms = urlParameters.split("&");
        var len = arrURLparms.length;
        var count = 0;
        var newArrayUrlparams = new Array();
        for(var i = 0; i < len; i++){
            arrURLparms[i] = arrURLparms[i].split("=");
            if (arrURLparms[i][0] != parm){
                newArrayUrlparams[count] = arrURLparms[i].join("=");
                count++;
            }
        }

        findFrame(window, "tableFrame").urlParameters = newArrayUrlparams.join("&");
   }

</script>
</header>
<body class="no-footer" onload="javascript:onLoad();" style="overflow: hidden;">
<%
if (hideOuterHeader.equals("true")) {
    topPixel = "0px";
} else {
%>
<div id="pageHeadDiv">
<form name="mx_filterselect_hidden_form">
    <table>
        <tr>
            <td class="page-title">
                <h2 id="ph"><%=XSSUtil.encodeForHTML(context, pageHeading)%></h2>
            </td>
            <td class="functions">
                <table id="functionstable">
                    <tbody>
                    <tr>
                        <td class="progress-indicator"><div id="imgLoadingProgressDiv" class="progress-indicator-text" style="height: 19px !important;" ><p id="loading1">Loading...</p></div><div id="imgProgressDivPortal"><div id="count"></div></div></td>
                        <% if (!targetLocation.equalsIgnoreCase("POPUP"))  { %>
                        <td><div class="field home button enabled"><a title="<%= appMenuLabel %>"><button class="home enabled" title="<%= appMenuLabel %>"></button></a></div></td>
                        <td><div class="field previous button"><a ><button class="previous" title="<%= sPrevious %>"></button></a></div></td>
                        <td><div class="field next button enabled"><a ><button class="next disabled" title="<%= sNext %>"></button></a></div></td>
                        <% } %>
                    </tr>
                    </tbody>
                </table>
            </td>
        </tr>
    </table>
</form>
</div>

<%
}
/* 2018-03-22 sme.park [E] -  헤더 감추는 옵션 추가 */
%>
<div id="divPageBody" style="top:<%=XSSUtil.encodeForHTMLAttribute(context, topPixel)%>;">

    <iframe id="filterFrame" name="filterFrame" onload="loadComplete()" style="height:0px; min-height: 0px; border:0;border-top:1px solid #D8D8D8;" src="<%=filterFramePage%>" scrolling="no"></iframe>
    <div id="divFilterDivision" class="schClose" style="height:14px; display:none;">
        <button type="button" id="btnSearch" ></button>
    </div>
    <iframe id="tableFrame" name="tableFrame" style="top:0px; min-height:0px; height:100%; border:0;" src=""></iframe>
    <iframe class='hidden-frame' name='pagehidden' style="height:0px; width:0px;"></iframe>
</div>
</body>
</html>
<script>
	$(window).resize(function() {
	    clearTimeout(window.resizedFinished);
	    window.resizedFinished = setTimeout(function(){
	    	resizeFilter();
	    }, 100);
	});
	
	function loadComplete(){
		var objHeaders = $("#filterFrame").contents().find("td.label,td.labelRequired,td.createLabel,td.createLabelRequired")
		var inputObject = $("#filterFrame").contents().find("input,select")
	
	    $(inputObject).each(function(e) {
	    	var obj = $(inputObject).eq(e);
	        $(obj).on("keyup", function(event) {
	            if (event.keyCode==13) {
	            	doFilter();
	            }
	        });
	    });
	    
	    $(objHeaders).each(function(e) {
	        var obj = $(objHeaders).eq(e);
	        var strTitle = $(obj).text();
	        $(obj).attr("title", strTitle);
	    });
		
	    var fields="<%= strFields %>";
	    
	    if (fields != ""){
	        initFilterFrameFields(fields);
	    }
	    
	    var readonlyFields="<%= strReadOnlyFields %>";
	    if (readonlyFields != ""){
	    	readonlyFilterFrameFields(readonlyFields);
	    }
	    
	}
	
	/**
	 * FilterFrame Page의 fields의 Default 설정 
	 *
	 * @param fields
	 * @returns
	 */
	function initFilterFrameFields(fields) {
	    var exprObjects = $("#filterFrame").contents().find("td.inputField");
	    var aryFields = fields.split(":");
	    
	    for(var i=0; i< aryFields.length; ++i) {
	        var aryDetailField = aryFields[i].split("=");
	        var initValue = aryDetailField[1];
	        var aryInitValue = initValue.split(",");
	        
	        var tdObjects = exprObjects.filter("[select_expression='" + aryDetailField[0] + "']");
	        
	        var childrenObject = tdObjects.find('input,select');
	        
	        childrenObject.each(function(){
	        	switch(this.type) {
	        	   case "text" :
	        	   case "select-one" :
	                   $(this).val(initValue);
	                   break;
	        	   case "radio" :
	        	   case "checkbox" :
	        		   if ($.inArray($(this).val(), aryInitValue)> -1) {
	        			   $(this).prop("checked", true);
	        		   } else {
	        			   $(this).prop("checked", false);
	        		   }
	        		   
	        	   default:
	        		   
	        		   break;
	        	}
	
	        });
	    }
	}
	
	/**
	 * FilterFrame Page의 Readonly Fields의 ReadOnly 설정 
	 *
	 * @param fields
	 * @returns
	 */
	function readonlyFilterFrameFields(readonlyFields) {
	    var exprObjects = $("#filterFrame").contents().find("td.inputField");
	    var aryFields = readonlyFields.split(",");
	    
	    for(var i=0; i< aryFields.length; ++i) {
	        var aryDisableDetailValue = aryFields[i].split("=");
	        var aryContainsDisableValues= [];
	        var bleExcludeDisable = false;
	        var searchExpressionName = aryFields[i];
	        if (aryDisableDetailValue.length >= 2) {
	            bleExcludeDisable= true;
	            var aryTempContainsDisableValues = aryDisableDetailValue[1].split(",");
	            
	            for(var idx=0; idx < aryTempContainsDisableValues.length; ++idx) {
	            	aryContainsDisableValues.push(aryTempContainsDisableValues[idx]);
	            }
	            
	            searchExpressionName = aryDisableDetailValue[0];
	        }
	        
	        var tdObjects = exprObjects.filter("[select_expression='" + searchExpressionName + "']");
	
	        var childrenObject = tdObjects.find('input,select');
	        
	        childrenObject.each(function(){
	            switch(this.type) {
	               case "text" :
	                   $(this).prop("readonly", true);
	                   break;
	               case "radio" :
	               case "checkbox" :
	                   $(this).click(function(e){
	                	   e.preventDefault();
	                	   return false;
	                   });
	                   break;
	               case "select-one" :
	            	   if (!bleExcludeDisable) {
		                   var initValue = $(this).val();
		                   
		                   var options = $(this).children('option');
		                   
		                   options.each(function() {
		                       if ($(this).val() != initValue)
		                           $(this).prop("disabled", true);
		                   });
	            	   } else {
	                       var options = $(this).children('option');
	                       
	                       options.each(function() {
	                           if (aryContainsDisableValues.indexOf($(this).val())>= 0) {
	                               $(this).prop("disabled", true);
	                           }
	                       });
	            	   }
	                   break;
	            }
	
	        });
	    }
	}
</script>
<%}catch(Exception e){
    e.printStackTrace();
}%>