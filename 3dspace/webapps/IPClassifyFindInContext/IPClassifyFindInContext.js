define("DS/IPClassifyFindInContext/service/FindInTreeServices",["i18n!DS/IPClassifyFindInContext/assets/nls/FindInTree","DS/Core/PointerEvents","DS/IPClassifyUtil/IPClassifyAlert"],function(e,t,n){"use strict";return{_findsInTree:function(i){var s=!0,o=i.treeLoader;o.loader.addEventListener(t.POINTERHIT,function(e){e.stopPropagation(),s=!1,i.highLightNodes()}),new Promise(function(t,r){var a=[];i.dataProvider.findTreeNodes({pid:i.nodeToFindIn,searchFTSquery:i.searchFTSquery,rscIdToSearch:i.rscIdToSearch,type:i.type,onComplete:function(d){if(s){if(o.setMessage(e.loader.retrievingPaths),d.results&&d.results.length>0)for(var l=0;l<d.results.length;l++)for(var h=d.results[l],c=0;c<h.attributes.length;c++)if("resourceid"==h.attributes[c].name){a.push(h.attributes[c].value);break}a.length?(100==a.length&&n.displayNotification({message:i.dataProvider.NlsTree&&i.dataProvider.NlsTree.msg_info_limit_FindInTree?i.dataProvider.NlsTree.msg_info_limit_FindInTree:e.msg_info_limit_FindInTree,className:"info"}),i.setNodesIdTobeHighLighted(a),t(a)):(n.displayNotification({message:i.dataProvider.NlsTree&&i.dataProvider.NlsTree.msg_info_No_Occurrence?i.dataProvider.NlsTree.msg_info_No_Occurrence:e.msg_info_No_Occurrence,className:"info"}),i.highLightNodes(),i.setNodesIdTobeHighLighted([]),r())}},onFailure:function(e){r(e)}})}).then(function(e){i.dataProvider.expandTreeAndSelectNode&&s&&(o.loader.removeEventListener("click",function(e){s=!1,i.highLightNodes()}),i.dataProvider.expandTreeAndSelectNode({folderToFindAndSelect:{ids:e},findInTree:!0,findInTreeLoader:o,doNotSelectInTree:!0,partiallyExpandPath:!0,onComplete:function(e){var t=e.folderToFindAndSelect.ids;i.highLightNodes(t)},onGiveUp:function(e){console.log(" expandTreeAndSelectFolder:onGiveUp"),i.highLightNodes()},onFailure:function(e){i.highLightNodes()}}))}).catch(function(e){e&&console.log(e),i.highLightNodes()})}}}),define("DS/IPClassifyFindInContext/views/FindInCTxUI",["css!DS/IPClassifyFindInContext/IPClassifyFindInContext.css","DS/Controls/Abstract","UWA/Core","DS/Utilities/Utils","DS/Core/PointerEvents","i18n!DS/IPClassifyFindInContext/assets/nls/FindInContent","DS/Controls/Tab","DS/Controls/Button","DS/Core/ModelEvents","DS/Controls/TooltipModel","DS/WUXAutoComplete/AutoComplete","DS/Tree/TreeDocument","DS/Tree/TreeNodeModel","DS/UIKIT/Spinner","DS/WidgetServices/WidgetServices"],function(e,t,n,i,s,o,r,a,d,l,h,c,u,g,f){"use strict";return t.extend({init:function(e){this._parent(e),this.placeholder=e.placeholder,this.onDrop=e.onDrop,this.keyLocalStorage=e.keyLocalStorage,this._queryLimit=e.queryLimit?e.queryLimit:10,this.savedQueries=localStorage.getItem(this.keyLocalStorage)?JSON.parse(localStorage.getItem(this.keyLocalStorage)):[],this.launchFindInCtx=e.launchFindInCtx,this.onSelectAll=e.onSelectAll,this.onFindPreviousResult=e.onFindPreviousResult,this.onFindNextResult=e.onFindNextResult,this.errorMsgonNoFind=e.errorMsgonNoFind,this.close=e.close,this.explicitCheckforFind=e.explicitCheckforFind,this.buildSkeleton()},buildSkeleton:function(){var e=this;this.elements.container.addClassName("bookmark-controls-search-container"),this.elements.findEditor=new h({multiSearchMode:!1,placeholder:this.placeholder,allowFreeInput:!0}).inject(this.elements.container),this.elements.findEditor.getContent().addClassName("bookmark-controls-search-editor");var t=this.savedQueries;null!==t&t.length>0&&(this.elements.findEditor.elementsTree=this._createModel(t)),this.elements.textInfo=n.createElement("div",{class:"bookmark-findin-label"}).inject(this.elements.findEditor.getContent().getChildren()[0]),f.isTouchDevice()&&this.elements.textInfo.setAttributeNode(document.createAttribute("touch-mode")),this.elements.spinner=new g({visible:!1,animate:!0,spin:!0}),this.elements.spinner=new g({visible:!1,animate:!0,spin:!0}).inject(this.elements.findEditor.getContent().getChildren()[0]),this.elements.spinner.elements.container.addClassName("bookmark-findin-spinner"),this.elements.findEditor.getContent().ondrop=function(t){e.onDrop(t)};var i=n.createElement("div",{class:"bookmark-findin-action-div"}).inject(this.elements.container);this.elements.searchBtn=new a({emphasize:"secondary",icon:{iconName:"check",fontIconFamily:WUXManagedFontIcons.Font3DS,fontIconSize:"1x"}}).inject(i),this.elements.searchBtn.tooltipInfos=new l({longHelp:o.findInCtxSearchView.ftssearch}),this.elements.chevronUpBtn=new a({emphasize:"secondary",icon:{iconName:"chevron-up",fontIconFamily:WUXManagedFontIcons.Font3DS,fontIconSize:"1x"}}).inject(i),this.elements.chevronUpBtn.tooltipInfos=new l({longHelp:o.findInCtxSearchView.chevronUpBtn}),this.elements.chevronDwnBtn=new a({emphasize:"secondary",icon:{iconName:"chevron-down",fontIconFamily:WUXManagedFontIcons.Font3DS,fontIconSize:"1x"}}).inject(i),this.elements.chevronDwnBtn.tooltipInfos=new l({longHelp:o.findInCtxSearchView.chevronDwnBtn}),this.onSelectAll&&(this.elements.selectOnBtn=new a({emphasize:"secondary",icon:{iconName:"select-on",fontIconFamily:WUXManagedFontIcons.Font3DS,fontIconSize:"1x"}}).inject(i),this.elements.selectOnBtn.tooltipInfos=new l({longHelp:o.findInCtxSearchView.selectAll})),this._addEventListenersOnPanelElements()},_createNode:function(e){return new u({label:e,value:e,icons:[{iconName:"clock"}]})},_createModel:function(e){var t=this._treemodel=new c;t.prepareUpdate();for(var n=0;n<e.length;n++){var i=this._createNode(e[n]);t.addRoot(i)}return t.pushUpdate(),t},addQuery:function(e){var t=this.savedQueries;-1==t.indexOf(e)&&e&&e.length&&(t.length<this._queryLimit?(t.unshift(e),localStorage.setItem(this.keyLocalStorage,JSON.stringify(t)),this._treemodel?this._treemodel.addRoot(new u({label:e,value:e,icons:[{iconName:"clock"}]}),0):this._treemodel=this.elements.findEditor.elementsTree=this._createModel(t)):e&&e.length&&(t.pop(),t.unshift(e),localStorage.setItem(this.keyLocalStorage,JSON.stringify(t)),this._treemodel.removeRoot(this._treemodel.getRoots().length-1),this._treemodel.addRoot(new u({label:e,value:e,icons:[{iconName:"clock"}]}),0)))},_addEventListenersOnPanelElements:function(){var e=this;this.elements.findEditor.getContent().addEventListener("keydown",function(t){var i=n.Event.whichKey(event);"return"===i?e.elements.searchBtn.disabled||(e.launchFindInCtx(e.findStr),e.setLoadingState(!0),t.stopPropagation()):"esc"===i&&(e.close(),t.stopPropagation())}),this.elements.searchBtn.addEventListener(s.POINTERHIT,function(t){e.elements.searchBtn.disabled||(e.launchFindInCtx(e.findStr),e.setLoadingState(!0),t.stopPropagation())}),this.elements.findEditor.addEventListener("change",e.checkforDisplayofFindButton.bind(e)),this.elements.chevronUpBtn.addEventListener(s.POINTERHIT,function(){e.elements.chevronUpBtn.disabled||e._goToPreviousFindResult()}),this.elements.chevronDwnBtn.addEventListener(s.POINTERHIT,function(){e.elements.chevronDwnBtn.disabled||e._goToNextFindResult()}),this.onSelectAll&&this.elements.selectOnBtn.addEventListener(s.POINTERHIT,function(){e.elements.selectOnBtn.disabled||e.onSelectAll()})},setLoadingState:function(e){this._setLoadingState(e)},show:function(){this._parent.apply(this,arguments),this.elements.findEditor.focus()},_setLoadingState:function(e){e?(this.elements.spinner.show(),this.elements.textInfo.hide(),this.elements.searchBtn.disabled=!0,this.elements.findEditor.disabled=!0,this.elements.findEditor._editor.disabled=!0):(this.elements.spinner.hide(),this.elements.textInfo.show(),this.checkforDisplayofFindButton(),this.elements.findEditor.disabled=!1,this.elements.findEditor._editor.disabled=!1)},setMatchingStringCount:function(e){this.count=e,this.currentValue=0,this.count>0&&(this.currentValue=1),this.setLoadingState(!1),this._displayCurrentValue()},setCurrentValue:function(e){switch(e){case"previous":this.currentValue>1?this.currentValue=this.currentValue-1:this.currentValue=this.count;break;case"next":this.currentValue<this.count?this.currentValue=this.currentValue+1:this.currentValue=1}this._displayCurrentValue()},_displayCurrentValue:function(){var e=void 0!==this.currentValue&&void 0!==this.count&&0!==this.count?this.currentValue+"/"+this.count:"";""==e?this.removeLabel():this.setLabel(e);var t=!(this.count>1);this.elements.chevronUpBtn&&this.elements.chevronDwnBtn&&(this.elements.chevronUpBtn.disabled=t,this.elements.chevronDwnBtn.disabled=t),0===this.count?(this.onSelectAll&&(this.elements.selectOnBtn.disabled=!0),this.errorMsgonNoFind&&this.errorMsgonNoFind()):this.onSelectAll&&(this.elements.selectOnBtn.disabled=!1)},_goToPreviousFindResult:function(){var e=this.currentValue;this.count&&0!==this.count&&(this.currentValue>1?this.currentValue--:this.currentValue=this.count,this._displayCurrentValue()),this.currentValue&&e!==this.currentValue&&this.onFindPreviousResult&&this.onFindPreviousResult()},_goToNextFindResult:function(){var e=this.currentValue;this.count&&0!==this.count&&(this.currentValue<this.count?this.currentValue++:this.currentValue=1,this._displayCurrentValue()),this.currentValue&&e!==this.currentValue&&this.onFindNextResult&&this.onFindNextResult()},checkforDisplayofFindButton:function(){this.findStr=this.elements.findEditor.value?this.elements.findEditor.value.trim():this.elements.findEditor.value;var e=!this.explicitCheckforFind||this.explicitCheckforFind();this.findStr&&this.findStr.length&&!0!==this.loadingInProgress&&e?this.elements.searchBtn.disabled=!1:this.elements.searchBtn.disabled=!0},setLabel:function(e){this.elements.textInfo&&(this.elements.textInfo.innerHTML=e);var t="padding-right:"+(this.elements.textInfo.getSize().width+30+"px")+" !important; width: 100%";this.getLineEditor()&&this.getLineEditor()._myInput&&this.getLineEditor()._myInput.setAttribute("style",t)},removeLabel:function(){this.elements.textInfo&&(this.elements.textInfo.innerHTML="");this.getLineEditor()&&this.getLineEditor()._myInput&&this.getLineEditor()._myInput.setAttribute("style","padding-right: 20px !important; width: 100%")},getLineEditor:function(){return this.elements.findEditor._editor},getTextInfo:function(){return this.elements.textInfo},getFindEditor:function(){return this.elements.findEditor},getSearchBtn:function(){return this.elements.searchBtn},getPreviousBtn:function(){return this.elements.chevronUpBtn},getNextButton:function(){return this.elements.chevronDwnBtn},getSelectAllBtn:function(){return this.elements.selectOnBtn}})}),define("DS/IPClassifyFindInContext/FindInContent",["css!DS/IPClassifyFindInContext/IPClassifyFindInContext.css","DS/Controls/Abstract","UWA/Core","DS/Utilities/Utils","DS/IPClassifyFindInContext/views/FindInCTxUI","i18n!DS/IPClassifyFindInContext/assets/nls/FindInContent","DS/PADUtils/PADTypesMgt","DS/Controls/Toggle","DS/Controls/ButtonGroup","DS/IPClassifyUtil/IPClassifyAlert","DS/Controls/Accordeon","DS/Controls/ComboBox"],function(e,t,n,i,s,o,r,a,d,l,h,c){"use strict";return t.extend({name:"FindInCtx",searchWithFTS:!0,markForRecordFlag:!0,buttonFindInBookmark:!1,buttonAutoSelect:!1,init:function(){this._parent()},buidUI:function(e){var t=this;this.dataProvider=e.dataProvider,this.findInBookmark=e.findInBookmark,this.target=e.target,this.position=e.position,this.placeholder=o.placeholder_findInCtx,this.title=e.title,this._nodesForFindInCtx=e.nodesForFindInCtx,this.visibleFlag=!0,this._biFilter=this.dataProvider.getContentFilter(),this.dgv=this.dataProvider.getContentGridView(),this.elements.container.addClassName("bookmark-controls-search"),this.commonFindInCtxUI=new s({keyLocalStorage:"BM_FindInCtx_savedQueries",queryLimit:10,onDrop:this._onDrop.bind(this),placeholder:this.placeholder,launchFindInCtx:this._launchFindInCtxService.bind(this),onSelectAll:!1!==e.multiselection?this._onSelectAll.bind(this):void 0,onFindPreviousResult:this._goToPreviousMatchingCell.bind(this),onFindNextResult:this._goToNextMatchingCell.bind(this),errorMsgonNoFind:this.displayErrorNotification.bind(this),explicitCheckforFind:this.explicitCheckforFind.bind(this),close:function(){t.dataProvider.onContentFindClose()}}).inject(this.elements.container),this.findEditor=this.commonFindInCtxUI.getFindEditor(),this.searchBtn=this.commonFindInCtxUI.getSearchBtn(),this.previousBtn=this.commonFindInCtxUI.getPreviousBtn(),this.nextBtn=this.commonFindInCtxUI.getNextButton(),this.selectAllBtn=this.commonFindInCtxUI.getSelectAllBtn();var i=n.createElement("div",{class:"bookmark-findinCTx-options-expander"}),r=this.elements.checkButtonGroup=new d({type:"checkbox"});r.addChild(new a({type:"checkbox",label:o.findInCtxAutoSelect,checkFlag:t.buttonAutoSelect})),r.inject(i),r.getContent().addClassName("bookmark-findInCtx-checkbtnGrp"),this.findInBookmark&&widget.getPreference("allow-products-expand").value&&r.addChild(new a({type:"checkbox",label:o.findInCtxSearchView.checkButtonGroupLabel,checkFlag:t.buttonFindInBookmark})),r.addEventListener("change",function(e){1==e.dsModel.getButtonCount()?t.buttonAutoSelect=e.dsModel.getButton(0).checkFlag:(t.buttonAutoSelect=e.dsModel.getButton(0).checkFlag,t.buttonFindInBookmark=e.dsModel.getButton(1).checkFlag)}),n.createElement("div",{class:"bookmark-findInCTx-selection-name",text:o.FindIn+" :"}).inject(i),this._getColumnSelectionPanel().inject(i);new h({items:[{header:o.Options,content:i}],style:"simple"}).inject(this.elements.container);this.findEditor.addEventListener("change",function(){t.searchWithFTS=!0}),this.searchBtn.disabled=!0,this.previousBtn.disabled=!0,this.nextBtn.disabled=!0,this.selectAllBtn&&(this.selectAllBtn.disabled=!0),this._createPanel(),this._subscribeToTheExternalEvents()},_createPanel:function(){var e=this,t=require("DS/Windows/Dialog"),i=new n.Element("div",{styles:{"vertical-align":"top","min-height":"286px",position:"relative"}}),s=new n.Element("div",{class:"wux-control-inline-container"});i.inject(s);var r=this.dataProvider.getTreeWidth();this._panel=new t({title:o.findInCtxSearchView.title+this.title,content:e.elements.container,position:e.position?{of:e.target,my:"top left",at:"top left",offsetX:r+e.position.x,offsetY:e.position.y+30}:{of:e.target,my:"center",at:"center"},width:320,minWidth:320,minHeight:40,allowedDockAreas:0,collapsibleFlag:!1}),e._panel.getContent().querySelector(".wux-windows-window-content").setStyle("padding","3px"),this._panel.addEventListener("close",function(t){e.dataProvider.onContentFindClose()}),this._panel.getContent().addClassName("bookmark-findInCtx-panel")},_launchSearchFTS:function(e){var t=this._isFindInBookmarkFlat(),n={biFilter:this._biFilter,query:t?e:this._constructQuery(e),nodes:this._nodesForFindInCtx,findFlatInBookmark:t,highLightNodes:this.highlightElements.bind(this)},i=this._stringTobeHighlighted=e;this.dataProvider.findContentNodes(n),n.findFlatInBookmark||0==n.nodes.length&&l.displayNotification({message:this.dataProvider.NlsContent.msg_error_FindInCtx_noValidPhysicalObject,className:"error"}),this.commonFindInCtxUI.addQuery(i)},_launchSearchID:function(e){var t={biFilter:this._biFilter,nodes:this._nodesForFindInCtx,rscIdToSearch:this.rscIdToSearch,findFlatInBookmark:this._isFindInBookmarkFlat(),highLightNodes:this.highlightElements.bind(this)};this._stringTobeHighlighted=e;this.dataProvider.findContentNodes(t)},_launchFindInCtxService:function(e){this.searchBtn.disabled||(this.searchWithFTS?this._launchSearchFTS(e):this._launchSearchID(e))},_isFindInBookmarkFlat:function(){var e=this.elements.checkButtonGroup;if(e&&2==e.getButtonCount())var t=e.getButton(1).checkFlag;return!!this.findInBookmark&&(!widget.getPreference("allow-products-expand").value||t)},_isMatchingNodeTobeSelected:function(){var e=this.elements.checkButtonGroup;return!!e&&e.getButton(0).checkFlag},closeViewafterRefresh:function(){this.dataProvider.onContentFindClose()},_onDrop:function(e){var t=this;if(e.preventDefault(),"{}"!==e.dataTransfer.getData("text/plain")){var n=JSON.parse(e.dataTransfer.getData("text/plain"));if(1===n.data.items.length){var i=[],s=n.data.items[0],a=s.objectId,d=s.objectType,l=s.displayName;d&&l?function(e,n,s){i.push({type:n}),r.isTypesAuthorized(i,function(n){n.length>0&&(t.findEditor.value=s,setTimeout(function(){t.searchWithFTS=!1,t.rscIdToSearch=e},300))}),"Workspace"===n&&"Workspace Vault"===n||(!widget.getPreference("allow-products-expand").value||t.elements.checkButtonGroup&&t.elements.checkButtonGroup.value[0])&&(t.findEditor.value=s,setTimeout(function(){t.searchWithFTS=!1,t.rscIdToSearch=e},300))}(a,d,l):(this.findEditor&&(this.findEditor.value=o.findInCtxSearchView.loadingLabel),t.loadingInProgress=!0,require(["DS/PADServices/ws/WSAccess","DS/PADServices/utils/PlatformURL","DS/PADServices/utils/ParserUtils","DS/PADServices/utils/GlobalUtils"],function(e,n,i,s){(e.fetch?Promise.resolve():n.retrievePlatformUrl({addDefaults:!0,initWSAcess:!0})).then(function(){var n={pids:[a],data:{select_predicate:["ds6w:label","ds6w:type"],select_file:[]}};e.fetch(n).then(function(e){var n="string"==typeof e?JSON.parse(e):e;if(n.results&&1===n.results.length){var r=n.results[0],a=i.parseAttributes(r.attributes);l=a.label,delete t.loadingInProgress,t.findEditor.value=l}else s.displayNotif({message:o.findInCtxSearchView.cannotRetrieveLabel,level:"error"})}).catch(function(){s.displayNotif({message:o.findInCtxSearchView.cannotRetrieveLabel,level:"error"})})})}))}}},_subscribeToTheExternalEvents:function(){var e=this;this.dataProvider.onCurrentSelectionChange(function(t){e&&e.dataProvider.onContentFindClose()}),this.dataProvider.onContentSearchChange(function(){e&&e.dataProvider.onContentFindClose()})},_onSelectAll:function(){if(this.dgv){var e=this.dgv.getMatchingFindNodeModelArray();if(e&&e.length){var t=this.dataProvider.getContentTreeDocument();t.prepareUpdate();for(var n=0;n<e.length;n++)e[n].isSelected()||e[n].select();t.pushUpdate()}}},displayErrorNotification:function(){l.displayNotification({message:o.msg_success_FindInCtx_NoObjectFound,className:"info"})},highlightElements:function(e,t,n){var i=this,s=[];if(t.length>0&&e.forEach(function(e){for(var n=0;n<t.length;n++){var i=e.getNodeByPath(t[n]);i&&i.isVisible()&&s.push(i)}}),n.length>0&&(s=s.concat(n)),this.dgv)if(this.dgv.closeFind(),s.length>0){var o=this.dgv;o.findWidget=i.commonFindInCtxUI,o.findNavigationMode="row",o.getNodeModelToProcessForFind=function(e,t){return s},this._selectionPanel&&this._selectionPanel.currentValue&&(o.getColumnsToProcessForFind=function(e,t){return i._getSelectedColumnDataIndex()}),o.setFindStr(this._stringTobeHighlighted,void 0,void 0,void 0,!0),this._isMatchingNodeTobeSelected()&&this._selectCurrentMatchingRow(),this.commonFindInCtxUI.setMatchingStringCount(o.matchingStringCount)}else this.commonFindInCtxUI.setMatchingStringCount(0)},_getColumnSelectionPanel:function(){var e=this,t=[];e.dataProvider.getContentColumnsToIgnore&&(t=e.dataProvider.getContentColumnsToIgnore());var n=function(){e._selectionPanel._getMainElement().getChildren()[1].innerText=o.Allcolumns};if(!this._selectionPanel){var i=function(e){var n=[];n.push({label:o.Allcolumns,value:"All columns"});for(var i=0;i<e.length;i++){var s=e[i];let o=!1;if(s.convertInfos&&s.convertInfos.manipulationUnit&&s.convertInfos.manipulationUnit!==s.convertInfos.unit&&(o=!0),0!=s.visibleFlag&&-1==t.indexOf(s.dataIndex)&&"dateTime"!==s.kind&&"timestamp"!==s.kind&&!o){var r={label:s.text,value:s.dataIndex};n.push(r)}}return n}(this.dgv.layout.columns);this._selectionPanel=new c({placeholder:"None",autocompleteFlag:!1,enableSearchFlag:!1,multiSelFlag:!0,elementsList:i}),this._selectionPanel.selectedIndexes=Array.from(Array(i.length).keys());var s=e._selectionPanel.currentValue;n();var r=[];i.forEach(function(e){r.push(e.value)}),this._selectionPanel.addEventListener("change",function(t){var i=e._selectionPanel.elementsList.length,o=e._selectionPanel.selectedIndexes.length,a=e._selectionPanel.currentValue.indexOf("All columns")>-1,d=function(e,t){for(var n=[],i=[],s=0;s<e.length;s++)n[e[s]]=!0;for(s=0;s<t.length;s++)n[t[s]]?delete n[t[s]]:n[t[s]]=!0;for(var o in n)i.push(o);return i}(s,e._selectionPanel.currentValue);if(d.indexOf("All columns")>-1&&1==d.length||0==e._selectionPanel.preselectedIndex)a?(s=r,e._selectionPanel.selectedIndexes=Array.from(Array(i).keys()),n()):(s=[],e._selectionPanel.selectedIndexes=[]);else if(a||o!=i-1)if(a&&o==i-1){var l=[];e._selectionPanel.selectedIndexes.forEach(function(e){l.push(e)}),l.shift(),(s=JSON.parse(JSON.stringify(r))).shift();var h=s.indexOf(d[0]);s.splice(h,1),e._selectionPanel.selectedIndexes=l}else s=e._selectionPanel.currentValue;else s=r,e._selectionPanel.selectedIndexes=Array.from(Array(i).keys()),n()}),this._selectionPanel.addEventListener("hide",function(){e._selectionPanel.currentValue.indexOf("All columns")>-1&&n(),0==e._selectionPanel.currentValue.length?e.searchBtn.tooltipInfos.longHelp=o.error_tooltip_NoColumnSelected:e.searchBtn.tooltipInfos.longHelp=o.findInCtxSearchView.ftssearch,e.findEditor.fire("change")})}return this._selectionPanel},_getSelectedColumnDataIndex:function(){var e=JSON.parse(JSON.stringify(this._selectionPanel.currentValue)),t=e.indexOf("All columns");return t>-1?(e.splice(t,1),e):e},_constructQuery:function(e){for(var t=this.dgv.layout.columns,n=this._getSelectedColumnDataIndex(),i="",s=!parseInt(e),o=!parseFloat(e),r={},a=0;a<t.length;a++)r[t[a].dataIndex]=t[a];for(var d=0;d<n.length;d++){var l=null,h=!1;let t="*",a="*",c=r[n[d]];c&&(l=c.ds6w,o&&("real"===c.kind||c.convertInfos)&&(h=!0),s&&"integer"===c.kind&&(h=!0),("real"===c.kind||c.convertInfos||"integer"===c.kind)&&(t="",a=""),"string"!==c.kind&&"string"!==c.typeRepresentation&&"tag"!==c.typeRepresentation||(t='"*',a='*"')),h||l&&(i=i+"["+l+"]:"+t+e+a,d!==n.length-1&&(i+=" OR "))}return i.lastIndexOf(" OR ")===i.length-" OR ".length&&(i=i.substring(0,i.length-" OR ".length)),i},explicitCheckforFind:function(){return!this._selectionPanel||0!=this._selectionPanel.currentValue},_goToPreviousMatchingCell:function(){this.dgv.goToPreviousMatchingCell(),this._isMatchingNodeTobeSelected()&&this._selectCurrentMatchingRow()},_goToNextMatchingCell:function(){this.dgv.goToNextMatchingCell(),this._isMatchingNodeTobeSelected()&&this._selectCurrentMatchingRow()},_selectCurrentMatchingRow:function(){var e=this.dgv._rowModel._currentMatchingNodeModel;e&&!e.isSelected()&&e.select(!0)},close:function(){this.dgv&&this.dgv.closeFind(),this._selectionPanel.destroy(),this._selectionPanel=null,this.dgv.getNodeModelToProcessForFind=void 0,this.visibleFlag=!1,this._panel.destroy(),this._panel=null}})}),define("DS/IPClassifyFindInContext/FindInTree",["css!DS/IPClassifyFindInContext/IPClassifyFindInContext.css","DS/Controls/Abstract","UWA/Core","DS/Controls/TooltipModel","DS/IPClassifyFindInContext/views/FindInCTxUI","DS/IPClassifyFindInContext/service/FindInTreeServices","DS/Controls/Loader","i18n!DS/IPClassifyFindInContext/assets/nls/FindInTree","DS/IPClassifyUtil/IPClassifyAlert"],function(e,t,n,i,s,o,r,a,d){"use strict";return t.extend({_name:"folder_FindInTree",searchWithFTS:!0,_nodesIdToFindIn:null,_isComponentVisible:!1,_isTreeTaggerApplied:!1,_currentFolder:null,init:function(e){this.dataProvider=e.dataProvider,this.findInContainerDiv=e.findInContainerDiv,this.isBookMarkWidget=e.isBookMarkWidget,this._parent(e),this.isBookMarkWidget?this._buildSkeletonBookmark():this._buildSkeleton(),this.disableClickEvent="boolean"==typeof e.disableClickEvent&&e.disableClickEvent},_buildSkeletonBookmark:function(){var e=this;this.elements.container.addClassName("folder_FindInTree"),this._createFindUI(),e.dataProvider.onTreeFilterChange(function(t){if(e._isTreeTaggerApplied=t,e._isComponentVisible)if(e._findInTreeloader.loader&&e._findInTreeloader.loader.visibleFlag){var n=e._findInTreeloader.loader.elements.stopButton;n&&n.getContent().click(),e.hideUI(),d.displayNotification({title:a.msg_warning_TreeTagger_ClosingThefind.Title,message:a.msg_warning_TreeTagger_ClosingThefind.Details,className:"warning"})}else{var i=e._getNodesIdTobeHighlighted();i&&i.length?e._highlightNodes(i):e._highlightNodes()}})},_buildSkeleton:function(){var e=this;this.elements.container.addClassName("folder_FindInTree"),this._findInTreeIcon=n.createElement("i",{class:"wux-ui-3ds wux-ui-3ds-2x wux-ui-3ds-find"}).inject(this.elements.container),this._findInTreeIcon.addEventListener("click",this.clickEvent.bind(this)),this._findInTreeIcon.tooltipInfos=new i({shortHelp:a.toolTip}),this._createFindUI(),e.dataProvider.onTreeFilterChange(function(t){if(e._isTreeTaggerApplied=t,e._isComponentVisible)if(e._findInTreeloader.loader&&e._findInTreeloader.loader.visibleFlag){var n=e._findInTreeloader.loader.elements.stopButton;n&&n.getContent().click(),e.hideUI(),d.displayNotification({title:a.msg_warning_TreeTagger_ClosingThefind.Title,message:a.msg_warning_TreeTagger_ClosingThefind.Details,className:"warning"})}else{var i=e._getNodesIdTobeHighlighted();i&&i.length?e._highlightNodes(i):e._highlightNodes()}})},clickEvent:function(e){this.disableClickEvent||(this.commonFindinTreeUI.visibleFlag?this.hideUI():this.showUI())},_createFindUI:function(){var e=this;this.commonFindinTreeUI=new s({keyLocalStorage:e.dataProvider.autoCompleteKey,queryLimit:10,onDrop:this._onDrop.bind(this),launchFindInCtx:this._launchFindInCtxService.bind(this),onFindPreviousResult:this._goToPreviousMatchingCell.bind(this),onFindNextResult:this._goToNextMatchingCell.bind(this),close:function(){e.hideUI()}}),this.commonFindinTreeUI.elements.container.addClassName("findinTree_findcontainer"),this.commonFindinTreeUI.hide(),this.findEditor=this.commonFindinTreeUI.getFindEditor(),this.searchBtn=this.commonFindinTreeUI.getSearchBtn(),this.previousBtn=this.commonFindinTreeUI.getPreviousBtn(),this.nextBtn=this.commonFindinTreeUI.getNextButton(),this.dataProvider.onCurrentSelectionChange(function(){if(e._isComponentVisible)if(e.dataProvider.getTreeSelectedNode()){var t=e.dataProvider.getTreeSelectedNode().getLabel();e._setNameTothePlaceholder(t)}else e._setNameTothePlaceholder(" ");else e.isBookMarkWidget||(e.isCurrentFolderValidForFindIn()?e._findInTreeIcon.show():e._findInTreeIcon.hide())}),this.searchBtn.disabled=!0,this.previousBtn.disabled=!0,this.nextBtn.disabled=!0,this.findEditor.addEventListener("change",function(){e.searchWithFTS=!0})},hideUI:function(){this.elements.container.getParent().getParent().removeAttribute("findInTree"),this.elements.container.removeClassName("active"),this.commonFindinTreeUI.hide(),this.findInContainerDiv&&this.findInContainerDiv.hide(),this.dgv&&(this.dgv.closeFind(),this.commonFindinTreeUI.setMatchingStringCount(0)),this._isComponentVisible=!1,this.isCurrentFolderValidForFindIn()||this.isBookMarkWidget||this._findInTreeIcon.hide()},showUI:function(){var e=this.dataProvider.getTreeSelectedNode();if(e){var t=e.getLabel();this._setNameTothePlaceholder(t),this.dgv=this.dataProvider.getTreeGridView(),this.elements.container.getParent().getParent().setAttribute("findInTree",!0),this.elements.container.addClassName("active"),this.findInContainerDiv&&this.findInContainerDiv.show(),this.commonFindinTreeUI.show(),this._IdsTobeHighLighted=null,this._isComponentVisible=!0}else this.dataProvider.NlsTree.msg_warning_NoObjectSelected.Title&&d.displayNotification({title:this.dataProvider.NlsTree.msg_warning_NoObjectSelected.Title,subtitle:this.dataProvider.NlsTree.msg_warning_NoObjectSelected.Advice,message:this.dataProvider.NlsTree.msg_warning_NoObjectSelected.Details,className:"warning"})},_onDrop:function(e){var t=this;if(e.preventDefault(),"{}"!==e.dataTransfer.getData("text/plain")){var n=JSON.parse(e.dataTransfer.getData("text/plain"));if(1===n.data.items.length){var i=n.data.items[0],s=i.objectId,o=i.objectType,r=i.displayName;-1!==t.dataProvider.getSupportedTypesForDrop().indexOf(o)?(t.findEditor.value=r,setTimeout(function(){t.searchWithFTS=!1,t.rscIdToSearch=s},300)):d.displayNotification({title:t.dataProvider.NlsTree.msg_warning_FindInTree_InvalidDrop.Title,subtitle:t.dataProvider.NlsTree.msg_warning_FindInTree_InvalidDrop.Advice,className:"warning"})}}},_launchFindInCtxService:function(e){var t=this;if(this._IdsTobeHighLighted=null,this.isCurrentFolderValidForFindIn()){this._currentFolder=this.dataProvider.getTreeSelectedNode(),this._nodesIdToFindIn=this._setNodesIdToFindIn(this._currentFolder);var n=!!this._currentFolder&&this._currentFolder.isVisible();this._nodesIdToFindIn&&n?(this._findInTreeloader.createLoader(this.dataProvider),this.searchWithFTS?this._launchSearchFTS(e):this._launchSearchID(e)):(n||d.displayNotification({title:t.dataProvider.NlsTree.msg_warning_expandTotheFindInNode.Title,subtitle:t.dataProvider.NlsTree.msg_warning_expandTotheFindInNode.Advice,message:t.dataProvider.NlsTree.msg_warning_expandTotheFindInNode.Details,className:"warning"}),setTimeout(function(){t._highlightNodes()},300))}else d.displayNotification({message:t.dataProvider.NlsTree.msg_warning_FindInTree_NoLaunchOnFavourite,className:"warning"}),setTimeout(function(){t._highlightNodes()},300)},_launchSearchFTS:function(e){var t={nodeToFindIn:this._nodesIdToFindIn,searchFTSquery:e,type:this.currentNodePlmType,highLightNodes:this._highlightNodes.bind(this),setNodesIdTobeHighLighted:this._setNodesIdTobeHighLighted.bind(this),treeLoader:this._findInTreeloader,dataProvider:this.dataProvider};o._findsInTree(t);var n=this._stringTobeHighlighted=e;this.commonFindinTreeUI.addQuery(n)},_launchSearchID:function(e){var t={nodeToFindIn:this._nodesIdToFindIn,rscIdToSearch:this.rscIdToSearch,type:this.currentNodePlmType,highLightNodes:this._highlightNodes.bind(this),setNodesIdTobeHighLighted:this._setNodesIdTobeHighLighted.bind(this),treeLoader:this._findInTreeloader,dataProvider:this.dataProvider};this._stringTobeHighlighted=e,o._findsInTree(t)},_goToPreviousMatchingCell:function(){this.dgv.goToPreviousMatchingCell()},_goToNextMatchingCell:function(){this.dgv.goToNextMatchingCell()},_setNameTothePlaceholder:function(e){e&&this.commonFindinTreeUI&&(this.commonFindinTreeUI.getFindEditor().placeholder=a.placeholder_findInTree+" : "+e)},_highlightNodes:function(e){var t=this,n=[];if(e&&e.length>0&&e.forEach(function(e){var i;(i=t.dataProvider.getTreeVisibleNodesyId(e))&&n.push(i)}),n.length>0){var i=this._currentFolder.getAllDescendants(),s=[];if(i.forEach(function(e){for(var t=0;t<n.length;t++)if(n[t].id==e.id&&n[t].isVisible()){s.push(e);break}}),this.dgv){this.dgv.closeFind();var o=this.dgv;o.findNavigationMode="row",o.findWidget=this.commonFindinTreeUI,o.findNavigationMode="row",o.getNodeModelToProcessForFind=function(e,t){return s},o.setFindStr(this._stringTobeHighlighted,void 0,void 0,void 0,!0),this.commonFindinTreeUI.setMatchingStringCount(o.matchingStringCount)}}else this.dgv.closeFind(),this.commonFindinTreeUI.setMatchingStringCount(0);this._findInTreeloader.removeLoader()},_setNodesIdToFindIn:function(e){if((this.currentNodePlmType=e.plmType)==this.dataProvider.supportedSection){var t=this.dataProvider.getTreeSectionNodeForFind(),n=t.getChildren();if(n.length>0&&t.isExpanded()){for(var i=[],s=0;s<n.length;s++){if(!(i.length<200)){d.displayNotification({message:this.dataProvider.NlsTree&&this.dataProvider.NlsTree.msg_info_limit_FindInTree?this.dataProvider.NlsTree.msg_info_limit_FindInTree:a.msg_info_limit_FindInTree,className:"info"});break}n[s].isVisible()&&i.push(n[s].options.id)}return i}return this.dataProvider.NlsTree.msg_warning_NoObjectsUnderSection&&d.displayNotification({title:this.dataProvider.NlsTree.msg_warning_NoObjectsUnderSection.Title,subtitle:this.dataProvider.NlsTree.msg_warning_NoObjectsUnderSection.Advice,message:this.dataProvider.NlsTree.msg_warning_NoObjectsUnderSection.Details,className:"warning"}),null}return e.options.id},isCurrentFolderValidForFindIn:function(){var e=this.dataProvider.getTreeSelectedNode();return!(!e||e.options.plmType===this.dataProvider.unSupportedSection||e.isFavorite)},getUI:function(){return this.commonFindinTreeUI},isTreeTaggerApplied:function(){return this._isTreeTaggerApplied},_setNodesIdTobeHighLighted:function(e){this._IdsTobeHighLighted=e},_getNodesIdTobeHighlighted:function(){return this._IdsTobeHighLighted?this._IdsTobeHighLighted:null},_findInTreeloader:{createLoader:function(e){var t=e.getTreeElementToAddCommand();this.container=n.createElement("div",{class:"FolderEditor_findInTree_loader"}).inject(t),this.loader=new r({height:40,showButtonFlag:!0,fadeDuration:200}).inject(this.container),this.loader.on(e.NlsTree.loader.findingObjects)},updateLoader:function(e){this.loader&&this.loader.update(e)},removeLoader:function(){this.loader&&(this.loader.off(),delete this.loader,this.container.destroy())},setMessage:function(e){this.loader.on(e)}}})});