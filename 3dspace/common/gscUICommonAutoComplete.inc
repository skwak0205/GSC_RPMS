<%-----------------------------------------------------------------------------
* FILE    : emdUICommonAutoComplete.inc
* DESC    : 자동완성 기능 구현
* VER.    : 1.0
* AUTHOR  : GeonHwan,Bae
* PROJECT : HiMSEM Engine Project
*
* Copyright 2018 by ENOVIA All rights reserved.
* -----------------------------------------------------------------
*                           Revision history
* -----------------------------------------------------------------
*
* Since          Author         Description
* -----------   ------------   -----------------------------------
* 2020-05-25     GeonHwan,Bae   최초 생성
* 2002-06-02     BongJun,Park   테이블 height 적용, tableName 미 입력시 TableHeader hidden, function 추가 적용
------------------------------------------------------------------------------%>
<script type="text/javascript">
    var links = document.getElementsByTagName("link");

    // [B] jquery-ui.css Add
    var addedJQueryCss = false;
    for (var idx = 0; idx < links.length; idx++) {
        if (links[idx].href.indexOf("jquery-ui.css") > -1) {
            addedJQueryCss = true;
            break;
        }
    }
    
    if (!addedJQueryCss) {
        var link = document.createElement("link");
        link.href = "../plugins/libs/jqueryui/1.10.3/css/jquery-ui.css";
        link.rel = "stylesheet";
        $(document.head).append(link);
    }
    // [E] jquery-ui.css Add

</script>
<script type="text/javascript">
var UIAutoCompleteMap = function () {
    this._container = {};
}

UIAutoCompleteMap.prototype =  {
    Get : function (key) {
        return this.Contains(key) ? this._container[key] : null;
    }
    ,
    Put : function(key,value) {
        this._container[key] = value;
    }
    ,
    Contains : function(key) {
        return key in this._container;
    }
    ,
    Remove : function(key) {
        return this.Contains(key) ? delete this._container[key] : false;
    }
    ,
    ToString : function(sep) {
        var tostring = [];
        sep = sep || ",";
        for(var key in this._container) {
            tostring.push("[ " + key + " = " + this._container[key] + " ]");
        }
        return tostring.join(sep);
    }
    ,
    IsValid : function(key) {
        return typeof this.Get(key) != "function";
    }
    ,
    ToArray : function() {
        var arry = [];

        for(var key in this._container) {
            if(this.IsValid(key)) {
                arry.push(this.Get(key));
            }
        }
        return arry;
    }
}
var UIAutoCompleteObject = new UIAutoCompleteMap();

var UIAutoCompleteConstants = new Object();
UIAutoCompleteConstants.HEADER_SPACE=11; 
UIAutoCompleteConstants.SCROLL_SPACE_WIDTH=18;
UIAutoCompleteConstants.WIDGET_SPACE_WIDTH=25;
UIAutoCompleteConstants.HEADER_HEIGHT=26;
UIAutoCompleteConstants.DEFAULT_ALIGN = "center";
UIAutoCompleteConstants.SELECT_MODE_TOP="TOP";
UIAutoCompleteConstants.SELECT_MODE_POPUP="POPUP";

function TableColumnVO() {
    this.columnName   = "";
    this.columnTitle  = "";
    this.columnAlign  = UIAutoCompleteConstants.DEFAULT_ALIGN;
    this.columnSelect = "";
    this.columnWidth = 100;
}

function UICommonAutoComplete(frmFieldActual) { 
    this.autoCompletePage = "../common/emdAPPCommonAJAXProcess.jsp?";
    this.bleSelected = false;
    this.bleShowPopup = false;
    this.positionX=0;
    this.positionY=0;
    this.frmFieldActual = $("#"+ frmFieldActual);
    this.frmFieldHidden = null;
    this.frmFieldDisplay = null;
    this.custSelectValue = null;
    this.custDisplayValue = null;
    this.custCodeValue    = null;
    this.bleDisplayValue = false;
    this.hiddenLabel     = false;
    this.customParameter = null;
    this.jpoName = null;
    this.methodName= null;
    this.required = true;
    this.useCallbackFN = null;
    this.widgetWidth = "auto";
    this.widgetHeight = "auto";
    this.minLength = 1;
    this.queryLimit = 0;
    this.selectList = [];
    this.targetFrame = null;
    this.rangeHref   = null;
    this.tempSaveFieldCode="";
    this.tempSaveFieldCodeID="";
    this.tempSaveFieldCodeDisplay="";
    this.autoCompleteObject = null;
    this._popupSize   = "800x700";
    this._isTableList = false;
    this._rangeHref = "";
    this._rangeFunction = "";
    this._showPopup = false;
    this._selectMode = UIAutoCompleteConstants.SELECT_MODE_TOP;
    this._tableHeight = 100;
    this._tableName = null;
    this._tableHeader = [];
    this._selectdata = false;
    $('#menu-container').width(200);
}

UICommonAutoComplete.prototype = new Object;

function extractLast( term ) {

    return split( term ).pop();

}
function split( val ) {
    return val.split( /,\s*/ );
}

UICommonAutoComplete.prototype = {
    setRangeHref : function(strRangeHref) {
        this._rangeHref = strRangeHref;
    },
    setRangeFunction : function(strRangeFunction) {
        this._rangeFunction = strRangeFunction;
    },
    setShowPopup : function(bleShowPopup) {
        this._showPopup = bleShowPopup;
    },
    setSelectMode : function(strSelectMode) {
        if (strSelectMode != "")
            this._selectMode = strSelectMode.toUpperCase(); 
        else 
            this._selectMode = UIAutoCompleteConstants.SELECT_MODE_POPUP;
    },
    setPopupSize : function(strPopupSize) {
        if (strPopupSize != "")
            this._popupSize = strPopupSize; 
    },
    setTableColumn : function(tableColumns) {
    	if(tableColumns.length <= 0){
            this._isTableList = false;
        }else{
            var tableWidth = 0;
            this._tableHeader = [];
            this._isTableList = true;
            for(var i=0; i < tableColumns.length;++i) {
                var tableColumn = new TableColumnVO();
                tableColumn.columnName   = tableColumns[i].COLUMN_NAME;
                tableColumn.columnTitle  = tableColumns[i].COLUMN_TITLE;
                tableColumn.columnAlign  = tableColumns[i].COLUMN_ALIGH;
                tableColumn.columnSelect = tableColumns[i].COLUMN_SELECT;
                tableColumn.columnWidth  = tableColumns[i].COLUMN_WIDTH;
                tableWidth += parseInt(tableColumn.columnWidth);
                this._tableHeader.push(tableColumn);
            }

            this.widgetWidth = tableWidth + UIAutoCompleteConstants.SCROLL_SPACE_WIDTH;
        }
    },
    getTableHeader : function(tableName) {
        var parentObj = this;
        $.ajax({
            url : parentObj.autoCompletePage + "jpoName=emdCommonAutoComplete&methodName=getTableForAutoComplete&table=" + tableName,
            contentType : "application/json; charset=utf-8",
            dataType : "json",
            cache : false,
            success : function (data) {
            	parentObj.setTableColumn(data);
            },
            error:function(request,status,error){
                alert("Get Table Object Error : " + error);
            }
        });
    },
    setTableHeader : function(tableName, intHeight) {
        // Table Header 정의
        this.getTableHeader(tableName);
        
        if (intHeight !== undefined && intHeight != null) {
            this._tableHeight = intHeight;
            this.widgetHeight = intHeight + UIAutoCompleteConstants.HEADER_HEIGHT;
        }
        
        this._tableHeight=0;
    },
    init : function(){
        this.tempSaveFieldCode="";
        this.tempSaveFieldCodeID="";
        this.tempSaveFieldCodeDisplay="";

        if (this.custDisplayValue != null ) {
            this.bleDisplayValue = true;
        }
        
        this.bleSelected = false;
        this.bleShowPopup = false;
        
        var parentObj = this;
        this.frmFieldActual.on("keydown", function(event){
            var code = event.keycode || event.which;
            
            if(code == 13){
            	
                event.preventDefault();
                $(this).unbind("keydown", parentObj.autoCompleteKeyEvent);
                
                return false;
            }
        });

        if (this._tableHeader.length >= 1) {
            this._isTableList = true;
        }
        
        this.frmFieldActual.on('blur', function(e) {
            if (parentObj.required) {
                if ($(this).attr("selectdata") == "false") {
                    $(this).val("").trigger('change');  
                    
                    if (parentObj.frmFieldHidden!=null)
                        $("#" + parentObj.frmFieldHidden).val("");   
                                    
                    if (parentObj.frmFieldDisplay != null) {
                        var tagName = $("#" + parentObj.frmFieldDisplay).prop("tagName");
                        if (tagName == "SPAN" || tagName == "DIV" || tagName == "TD")
                            $("#" + parentObj.frmFieldDisplay).text("");
                        else 
                            $("#" + parentObj.frmFieldDisplay).val("").trigger('change');
                    }
                    
                    
                }
            }
        });
    },
    autoCompleteKeyEvent : function(event) {
        var code = event.keycode || event.which;
        var parentObj = $(this).data("parentObject");
        var targetObject = parentObj.frmFieldActual;
        var isTable = parentObj._isTableList;
        if(code == 13){
            event.preventDefault();

            this._rangeHref = "";
            this._rangeFunction = "";
            this._showPopup = false;
            var dataCount = 0;
            if (isTable)
                dataCount = targetObject.autocomplete("widget").find( "table#autoCompleteTable tr" ).length;
            else
                dataCount = targetObject.autocomplete("widget").find( "li" ).length;
            

            var inputValue = targetObject.val();
            // Search 팝업시 함수 호출시 호출되는 인자 정의(필요한 경우 추가하여 사용)
            // inputValue : text 박스에 입력한 문자열
            // fieldActual : 실제 Text박스의 Tag ID
            var args = [{"inputValue":inputValue,fieldActual:targetObject.attr("id")}];

            if (dataCount == 1) {
                if (targetObject.data('ui-autocomplete').selectedItem === null) {
                    if (isTable)
                        targetObject.data('ui-autocomplete').menu.element.find('table#autoCompleteTable tr:first').trigger('click');
                    else
                        targetObject.data('ui-autocomplete').menu.element.children('li:first').children('a').trigger('click');
                    
                    targetObject.attr("selectdata", "true");   
                }
            } else if (dataCount > 1) {
                if (targetObject.data('ui-autocomplete').selectedItem === null) {
                    if (parentObj._selectMode == UIAutoCompleteConstants.SELECT_MODE_TOP) {
                        if (isTable)
                            targetObject.data('ui-autocomplete').menu.element.find('table#autoCompleteTable tr:first').trigger('click');
                        else
                            targetObject.data('ui-autocomplete').menu.element.children('li:first').children('a').trigger('click');
                        
                        return false;
                    } 
                    if (parentObj._showPopup) {
                        if (parentObj._rangeFunction!= "") {
                            window[parentObj._rangeFunction].apply(null, args);
                        } else if (parentObj._rangeHref != "") {
                            showModalDialog(parentObj._rangeHref, 800, 700, false, parentObj._popupSize);
                        }
                        targetObject.attr("selectdata", "true");         
                   }       
                    
                }
            } else {
                if (parentObj._showPopup) {
                    if (parentObj._rangeFunction != "") {
                        window[parentObj._rangeFunction].apply(null, args);
                    } else if (parentObj._rangeHref != "") {
                        showModalDialog(parentObj._rangeHref, 800, 700, false, parentObj._popupSize);
                    }
                    targetObject.attr("selectdata", "true");       
                }
            }
                
            return false;
        }
    }, 
    setAutoCompletePage : function(strAutoCompletePage) {
        if (strAutoCompletePage != null && strAutoCompletePage != "") {
            this.autoCompletePage = strAutoCompletePage;
        }
    },
    selectTable : function(event) {
        var selectItem = $(this).data("ui-autocomplete-item");
        var parentObj = $(this).data("parentObject");
        parentObj.select(selectItem);
    },
    select : function(item) {
        this.frmFieldActual.attr("selectdata", "true");
        this.tempSaveFieldCode = item.label;
        
        if (this.custCodeValue != null) {
            this.tempSaveFieldCode = item.selectValue[this.custCodeValue];
        }
        
        this.tempSaveFieldCodeID = item.objectId;
        this.tempSaveFieldCodeDisplay = item.objectTitle;

        if (this.frmFieldActual != null)
            this.frmFieldActual.val(this.tempSaveFieldCode).trigger('change');

        if (this.frmFieldHidden != null) {
            if (this.custSelectValue == null)   
                $("#" + this.frmFieldHidden).val(this.tempSaveFieldCodeID).trigger('change');
            else 
                $("#" + this.frmFieldHidden).val(item.selectValue[this.custSelectValue]).trigger('change');
        }
        
        if (this.frmFieldDisplay != null) {
            var tagName = $("#" + this.frmFieldDisplay).prop("tagName");
            if (tagName == "SPAN" || tagName == "DIV" || tagName == "TD")
                $("#" + this.frmFieldDisplay).text(tthis.empSaveFieldCodeDisplay);
            else 
                $("#" + this.frmFieldDisplay).val(this.tempSaveFieldCodeDisplay).trigger('change');
        }
  
        this.frmFieldActual.autocomplete( "destroy" );
        
        if(this.useCallbackFN != null){
            try
            {   
                var args = [{targetName:this.frmFieldActual,objectId:item.objectId,label:item.label,objectTitle:item.objectTitle,selectValue:item.selectValue}];
                
                window[this.useCallbackFN].apply(null, args);
                           
            }catch(e){}
        }
    },
    show : function() {
        var parentObj = this;

        this.init();
        this.frmFieldActual.data("parentObject", parentObj);
        this.frmFieldActual.attr("selectdata", "false");
        this.autoCompleteObject = this.frmFieldActual.autocomplete({
            source : function(request, response) {
                var searchCode = extractLast(parentObj.frmFieldActual.val());

                if (parentObj.required) {
	                if (parentObj.frmFieldHidden!=null)
	                    $("#" + parentObj.frmFieldHidden).val("").trigger('change');  
	                
	                if (parentObj.frmFieldDisplay != null) {
	                    var tagName = $(parentObj.frmFieldDisplay).prop("tagName");
	                    if (tagName == "SPAN" || tagName == "DIV" || tagName == "TD")
	                        $("#" + parentObj.frmFieldDisplay).text("");
	                    else 
	                        $("#" + parentObj.frmFieldDisplay).val("").trigger('change');
	                }
                }

                $.ajax({
                    url : parentObj.autoCompletePage + "jpoName=" + parentObj.jpoName + "&methodName=" + parentObj.methodName + "&"+ parentObj.customParameter,
                    contentType : "application/json; charset=utf-8",
                    dataType : "json",
                    cache : false,
                    data : {
                        searchCode :   searchCode,
                        DisplayValue : parentObj.custDisplayValue,
                        SelectValue  : parentObj.custSelectValue,
                        selectLists  : parentObj.selectList,
                        queryLimit   : parentObj.queryLimit,
                        tableName   : parentObj._tableName
                    },
                    success : function (data) {
                        parentObj.frmFieldActual.bind("keydown", parentObj.autoCompleteKeyEvent);

                        parentObj.frmFieldActual.removeClass("ui-autocomplete-loading");

                        if(data==""){
                            bleNoResults = true;
                            
                            if (parentObj.required) {       
                                if (parentObj.frmFieldHidden!=null)
                                    $("#" + parentObj.frmFieldHidden).val("");   

                                if (parentObj.frmFieldDisplay != null) {
                                    var tagName = $("#" + parentObj.frmFieldDisplay).prop("tagName");
                                    if (tagName == "SPAN" || tagName == "DIV" || tagName == "TD")
                                        $("#" + parentObj.frmFieldDisplay).text("");
                                    else 
                                        $("#" + parentObj.frmFieldDisplay).val("").trigger('change');
                                }
                                
                            }
                            response(null);
                        }else{
                            response( $.map(data, function (item) {
                            var labelValue = item.name;
                            var labelSelect = item.id;
                            
                            if (parentObj.custCodeValue != "" && parentObj.custCodeValue!=null)
                                labelValue = item[parentObj.custCodeValue];

                            if (parentObj.custSelectValue != "" && parentObj.custSelectValue!=null)
                                labelSelect = item[parentObj.custSelectValue];
                            
                                return {
                                    label                : labelValue,
                                    desc                 : item.description,
                                    objectId             : labelSelect,
                                    objectTitle          : item[parentObj.custDisplayValue],
                                    selectValue          : item,
                                    val                  : 1
                                }
                            }));
                        }
                    },
                    error:function(request,status,error){
                        alert("code:"+request.status+"\n"+" "+"error:"+error);
                    }
                });
            }, 
            position:{
                my: "left top",
                at: "left bottom",
                collision:"fit flip"  
            }, 
            change : function() {
                if (!parentObj.bleSelected) {
                    if (parentObj.required) {         
                        if (parentObj.frmFieldHidden!=null)
                            $("#" + parentObj.frmFieldHidden).val("");   
                                            
                        if (parentObj.frmFieldDisplay != null) {
                            var tagName = $("#" + parentObj.frmFieldDisplay).prop("tagName");
                            if (tagName == "SPAN" || tagName == "DIV" || tagName == "TD")
                                $("#" + parentObj.frmFieldDisplay).text("");
                            else 
                                $("#" + parentObj.frmFieldDisplay).val("").trigger('change');
                        }
                    }
                }
            }, 
            minLength: parentObj.minLength, 
            autoFocus : false, 
            open: function() { 
                parentObj.bleSelected = false;
                parentObj.frmFieldActual.attr("selectdata", "false");
                if(parentObj.widgetWidth == "auto"){
                    parentObj.frmFieldActual.autocomplete("widget").css({"width": (parentObj.frmFieldActual.width() + UIAutoCompleteConstants.WIDGET_SPACE_WIDTH + "px")});
                } else {
                    parentObj.frmFieldActual.autocomplete("widget").width(parentObj.widgetWidth);
                }
                
                if(parentObj.widgetHeight != "auto"){
                    var intHeight = parentObj.widgetHeight;
                    parentObj.frmFieldActual.autocomplete("widget").css("height", (intHeight + "px"));  
                } else {
                	var $input = $(parentObj.frmFieldActual),
                    inputTop = $input.position().top,
                    inputHeight = $input.height(),
                    autocompleteHeight = $input.autocomplete("widget").height(),
                    windowHeight = $(window).height();

                	if (parentObj.targetFrame)
                		windowHeight = $(parentObj.targetFrame).height();
                	
                	if ((inputHeight + inputTop + autocompleteHeight) > windowHeight) {
                		parentObj.frmFieldActual.autocomplete("widget").css('height', (windowHeight - inputHeight - inputTop - 100) + 'px');
                		parentObj.frmFieldActual.autocomplete("widget").css("overflow-y", "auto");
                	}
                }
                
                if (parentObj._isTableList)
                    parentObj.frmFieldActual.autocomplete("widget").css("overflow-y", "hidden");
                
            }, 
            close : function() {    
                var bleSelected = parentObj.frmFieldActual.attr("selectdata");
                if (bleSelected == "true") {
                    if (parentObj.required) {
                        if (parentObj.frmFieldHidden!=null)
                            $("#" + parentObj.frmFieldHidden).val(parentObj.tempSaveFieldCodeID);    

                        if (parentObj.frmFieldDisplay != null) {
                            var tagName = $("#" + parentObj.frmFieldDisplay).prop("tagName");
                            if (tagName == "SPAN" || tagName == "DIV" || tagName == "TD")
                                $("#" + parentObj.frmFieldDisplay).text(parentObj.tempSaveFieldCodeDisplay);
                            else 
                                $("#" + parentObj.frmFieldDisplay).val(parentObj.tempSaveFieldCodeDisplay).trigger('change');
                        }
                    }               
                } else {
                    parentObj.frmFieldActual.attr("selectdata", "false");
                }
                parentObj.frmFieldActual.autocomplete( "destroy" );
            }, 
            select : function (event, ui) {
                parentObj.bleSelected =true;
                parentObj.select(ui.item);
            }

        }); 

        if (this.autoCompleteObject.data("ui-autocomplete") !== undefined){
            var autoComplete = this.autoCompleteObject.data("ui-autocomplete");
            this.autoCompleteObject.data("ui-autocomplete")._renderMenu = function( ul, items ) {
                if (parentObj._isTableList) {
                      //table definitions
                    if (parentObj._tableHeader.length > 0) {
                        var colGroup = $("<colgroup>");
                        var tableTr = $("<tr>");
                        var totalWidth = 0;
                        var headerSpaceSize = 0;
                        for(var i=0; i< parentObj._tableHeader.length; ++i) {
                            var columnHeader = $("<td>", {id:parentObj._tableHeader[i].columnName, style: "text-align:center;width:" + (parentObj._tableHeader[i].columnWidth-UIAutoCompleteConstants.HEADER_SPACE) + "px !important;" });
                            columnHeader.text(parentObj._tableHeader[i].columnTitle);
                            columnHeader.attr("select-data-name", parentObj._tableHeader[i].columnSelect);  
                            
                            tableTr.append(columnHeader);   
                            var col = $("<col width='" + parentObj._tableHeader[i].columnWidth + "px'/>");
                            colGroup.append(col);
                            
                            totalWidth += parseInt(parentObj._tableHeader[i].columnWidth);
                            headerSpaceSize += UIAutoCompleteConstants.HEADER_SPACE;
                        }

                        var tableHeader = $("<table>", {'id':"autoCompleteTableHeader", style:"width:" + (totalWidth-headerSpaceSize) + "px !important;"});
                        var intWidth = totalWidth + UIAutoCompleteConstants.SCROLL_SPACE_WIDTH;
                        var divHeader = $("<div/>", {style:'position:block;width:' + totalWidth + 'px;overflow:none;'});
                        tableHeader.append(colGroup);
                        tableHeader.append(tableTr);
                        divHeader.append(tableHeader);
                        
                        var divBody = $("<div/>", {style:'display:block !important;min-height:40px !important;max-height:200px !important;width:' + intWidth + 'px;overflow-x:hidden;overflow-y:auto;'});
                        var tableBody = $("<table>", {id:"autoCompleteTable",style:"width:" + totalWidth + "px !important;"});
                        tableBody.append(colGroup);
                        divBody.append(tableBody);
                        ul.append(divHeader);
                        ul.append(divBody);
                                                
                        $.each( items, function( index, item ) {
                            autoComplete._renderItem(ul.find("#autoCompleteTable"), item );
                        });
                    }
                } else {
                    $.each( items, function( index, item ) {
                        autoComplete._renderItem(ul, item );
                    });
                }
            };      
            this.autoCompleteObject.bleSelected = false;
            
            this.autoCompleteObject.data("ui-autocomplete")._renderItem = function( ul, item ) {
                if (parentObj._isTableList) {
                    if (parentObj._tableHeader.length > 0) {
                        var tableTr = $("<tr>", {'class':'ui-menu-item emdTableRow','role':'presentation'})
                        .data( "ui-autocomplete-item", item )
                        .data( "parentObject", parentObj )
                        .bind("click", parentObj.selectTable);
                
                        
                        for(var i=0; i< parentObj._tableHeader.length; ++i) {
                            var columnHeader = $("<td>", {id:parentObj._tableHeader[i].columnName, style: "text-align:" + parentObj._tableHeader[i].columnAlign + ";width:" + parentObj._tableHeader[i].columnWidth + "px !important;" });
                            columnHeader.text(item.selectValue[parentObj._tableHeader[i].columnSelect]);    
                            tableTr.append(columnHeader);   
                        }
                        tableTr.appendTo(ul);
                    }
                } else {

                    if (parentObj.bleDisplayValue) {
                        if (parentObj.hiddenLabel) {
                            return $( "<li>" ).data( "ui-autocomplete-item", item )
                            .data( "parentObject", parentObj ).append( "<a>" + item.objectTitle + "</a>" ).appendTo( ul );
                        } else {
                            var objectTitleValue=item.objectTitle==undefined?"":item.objectTitle;
                            return $( "<li>" ).data( "ui-autocomplete-item", item )
                            .data( "parentObject", parentObj ).append( "<a>" + item.label + "[" + item.objectTitle + "]</a>" ).appendTo( ul );
                        }
                    } else {
                        return $( "<li>" ).data( "ui-autocomplete-item", item )
                        .data( "parentObject", parentObj ).append( "<a>" + item.label + "  </a>" ).appendTo( ul );
                    }
                }
            };
            
            this.autoCompleteObject.data("ui-autocomplete")._appendTo = function() {
                var element = this.options.appendTo;
                
                if (element) {
                    element = element.jquery || element.nodeType?$(element):this.document.find(element).eq(0); 
                }
                
                if (!element) {
                    element = this.element.closest(".ui-front");
                }
                
                if (!element.length) {
                    element = this.document[0].body;
                }
    
                return element;
            }
            
            this.autoCompleteObject.data("ui-autocomplete")._suggest = function(items) {
            
                var ul = this.menu.element.empty();
                var frame = null;
                if (parentObj.targetFrame) {
                    if((typeof parentObj.targetFrame)== 'object'){
                        frame = parentObj.targetFrame;
                    }else{
                        frame = findFrame(top, parentObj.targetFrame);
                    }
                }
                
                if (frame) {
                    if ("undefined" == frame.isAddedCSS || undefined == frame.isAddedCSS) {
                        frame.isAddedCSS = false;
                    }
                    if (!frame.isAddedCSS) {
                        var links = frame.document.getElementsByTagName("link");
                    
                        for(var idx =0; idx < links.length; idx++) {
                            if (links[idx].href.indexOf("jquery-ui.css") > -1) {
                                frame.isAddedCSS = true;
                                break;
                            }  
                        }
    
                        if (!frame.isAddedCSS) {
                            var link = frame.document.createElement("link");
                            link.href = "../plugins/libs/jqueryui/1.10.3/css/jquery-ui.css";
                            link.rel = "stylesheet";
                            $(frame.document.head).append(link);

                            frame.isAddedCSS = true;
                        }
                    }
                    $(frame.document.body).append(ul);
                }
    
                this._renderMenu(ul, items);
                this.isNewMenu = true;
                this.menu.refresh();
            
                ul.show();
                this._resizeMenu();
                           debugger;
                if (frame) {
                    var field = $(parentObj.frmFieldActual)[0];
                    var x = emxUICore.getActualLeft(field) + frame.document.body.scrollLeft;
                    var y = (emxUICore.getActualTop(field) + field.offsetParent.offsetHeight) + field.offsetHeight;
                    //var y = parentObj.positionY + field.offsetParent.offsetHeight + field.offsetHeight;
                    if (document.body.scrollTop > 0) {
                        var positionY = document.body.scrollTop;
                        y -= positionY;
                    }
                    
                    emxUICore.moveTo(ul[0], x, y);
                } else {
                    ul.position($.extend({
                        of:this.element
                    }, this.options.position));
                }
                
                if (this.options.autoFocus) {
                    this.menu.next();
                }
            }
        }
    }
};

function isNotNull(value) {
    try {
        if (value != null && value != "") {
            return true;
        } else {
            return false;
        }
    } catch(e) {
        return false;
    }
}

function isExist(objectName) {
    if ($("#" + objectName).length > 0) {
        return true;
    } else {
        return false;
    }
}

/*
Statement

@param obj - Field Obj
@param frmFieldHidden - Hidden Field Id
@param custDisplayValue - Custom Display Value
@param custSelectValue - Custom Select Value
@param useCallbackFN - Callback Javascript Name
@param required - Boolean Required 
@param selectList - Search Select Value List
@return
@throws Exception
*/
function makeAutoComplete(obj, frmFieldDisplay, frmFieldHidden, custDisplayValue, custSelectValue, custCodeValue, useCallbackFN, required, customParameter, selectList, jpoName, methodName, minLength, queryLimit, widgetWidth, targetFrame, tableName) {
  var targetName = $(obj).attr("id");

  var objAutoCompleteFind;
  
  if (UIAutoCompleteObject.Contains(targetName)) {
      objAutoCompleteFind = UIAutoCompleteObject.Get(targetName);
      
      objAutoCompleteFind.show();
  } else {

      objAutoCompleteFind = new UICommonAutoComplete(targetName);
      
      if (isExist(frmFieldHidden))
          objAutoCompleteFind.frmFieldHidden =  frmFieldHidden; 
      
      if (isExist(frmFieldDisplay))
          objAutoCompleteFind.frmFieldDisplay = frmFieldDisplay;
      
      if (isNotNull(custDisplayValue))
          objAutoCompleteFind.custDisplayValue = custDisplayValue;

      if (isNotNull(tableName)){
          objAutoCompleteFind._tableName = tableName;
          //objAutoCompleteFind.setTableHeader(tableName, 200);
          objAutoCompleteFind.setTableHeader(tableName);
      }


      if (isNotNull(custSelectValue))
          objAutoCompleteFind.custSelectValue = custSelectValue;
      
      if (isNotNull(custCodeValue))
        objAutoCompleteFind.custCodeValue=custCodeValue;
      
      if (isNotNull(customParameter))
          objAutoCompleteFind.customParameter = customParameter;
      
      if (isNotNull(useCallbackFN))
          objAutoCompleteFind.useCallbackFN = useCallbackFN;
      
      if (required !==  undefined)
          objAutoCompleteFind.required = required;
      
      if (isNotNull(selectList))
          objAutoCompleteFind.selectList = selectList;
      
      if (!isNotNull(jpoName)) {
          alert("JPO Name Required");
          return false;
      }

      if (!isNotNull(methodName)) {
          alert("Method Name Required");
          return false;
      }
      
      objAutoCompleteFind.jpoName = jpoName;
      objAutoCompleteFind.methodName = methodName;
      
      if (isNotNull(minLength))
          objAutoCompleteFind.minLength = minLength;
      
      if (isNotNull(queryLimit))
          objAutoCompleteFind.queryLimit = queryLimit;
      
      if (isNotNull(widgetWidth))
          objAutoCompleteFind.widgetWidth = widgetWidth;

      if (isNotNull(targetFrame))
          objAutoCompleteFind.targetFrame = targetFrame;
      
      objAutoCompleteFind.setRangeFunction("showRangeFunction");
      objAutoCompleteFind.setShowPopup(true);
      objAutoCompleteFind.setSelectMode("");
      objAutoCompleteFind.show();
      UIAutoCompleteObject.Put(targetName, objAutoCompleteFind);
  }
  
}

/*
Statement

@param obj - Field Obj
@param frmFieldHidden - Hidden Field Id
@param custDisplayValue - Custom Display Value
@param custSelectValue - Custom Select Value
@param useCallbackFN - Callback Javascript Name
@param required - Boolean Required 
@param selectList - Search Select Value List
@return
@throws Exception
*/
function showAutocomplete(obj) {
  var targetName = $(obj).attr("id");
  var objAutoCompleteFind;
  
  if (UIAutoCompleteObject.Contains(targetName)) {
      objAutoCompleteFind = UIAutoCompleteObject.Get(targetName);
      
      objAutoCompleteFind.show();
  }  
}
</script>


<script language="javascript" src="../common/scripts/emdUICommonAutoCompleteFunction.js"></script>
