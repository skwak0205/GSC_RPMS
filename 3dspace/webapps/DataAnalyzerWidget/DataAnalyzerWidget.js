define("DS/DataAnalyzerWidget/DataAnalyzerWidget",["css!DS/DataAnalyzerWidget/DataAnalyzerWidget","UWA/Controls/Abstract","i18n!DS/LifecycleWidget/assets/nls/LifecycleWidgetNls","i18n!DS/DataAnalyzerWidget/assets/nls/DataAnalyzerWidgetNls","DS/LifecycleServices/LifecycleServices","DS/LifecycleServices/LifecycleServicesSettings","DS/WAFData/WAFData","DS/LifecycleCmd/MessageMediator","DS/WebToWinInfra/WebToWinCom","DS/WebInWinHelper/WebInWinHelper","DS/LifecycleControls/CommandDialog","DS/Controls/Accordeon","DS/Controls/Button","DS/Controls/Tab","DS/Tree/FileTreeView","DS/Tree/Tree","DS/Layouts/GridEngine"],function(e,t,n,i,a,r,o,s,l,c,u,p,m,h,d,y,_){"use strict";return t.extend({init:function(e,t,n){var o=this;if(this.widget=e,this.parentWidget=t,this.options=n,this.wintop=!1,this.runmyapp=!1,this.odtmode=!1,this.tenant=null,n&&n.odtmode&&(this.odtmode=n.odtmode),this._parent(n),n&&n.wintop){if(this.wintop=!0,this.container=UWA.createElement("div",{name:"data_analyzis",class:"dataanalyze_popup","data-rec-id":"dataanalyze_popup"}),this._computingTxt=new UWA.Element("text",{text:i.computing}),this._computingTxt.inject(this.container),this._computingTxt.hide(),c.initTenant(this),c.getServices(this),this.webToWinCom_Socket=l.createSocket({socket_id:"WebInWin_DataAnalyzerWidget_Socket_"+Math.floor(1e5*Math.random()+1)}),void 0!==this.options.serverUrl&&this.options.serverUrl.length>0&&void 0!==this.options.serverUrl[0].platformId){var s=this.options.serverUrl[0].platformId;this.tenant=s}this.addEvent("onComplete",()=>{this.webToWinCom_Socket.dispatchEvent("onDispatchToWin",{notif_name:"ClosePanel",notif_parameters:"ClosePanel"},"lf_web_in_win")})}else n&&n.runmyapp&&(this.runmyapp=!0,this.container=UWA.createElement("div",{name:"data_analyzis",class:"dataanalyze_popup","data-rec-id":"dataanalyze_popup"}),r.app_initialization(function(){o.tenant=r.getTenant();var e=r.getOption("platform_services",null);null!=e&&e.length>0&&Array.isArray(e)&&e.forEach(function(e){null!=e&&e.hasOwnProperty("platformId")&&a.getSecurityContextList(e.platformId,function(){o.getSecurityContext=a.getSecurityContextForCollaborativeSharingCommands})})}))},initDatafromWin:function(e){var t=[];e.forEach(function(e){var n=e.physicalId,i=e.name,a={physicalid:n,name:i,displayName:i};e.hasOwnProperty("config_filter")&&(a.config_filter=e.config_filter),t.push(a)}),this.executeCmd(t)},initDatafromDrop:function(e){var t=[];e.forEach(function(e){var n=e.physicalId,i=e.name,a={physicalid:n,name:i,displayName:i};e.hasOwnProperty("config_filter")&&(a.config_filter=e.config_filter),t.push(a)}),this.executeCmd(t)},executeCmd:function(e){var t=this;if(this.physicalIds=[],0===e.length)throw n.noObject;e.forEach(function(e){if(e.hasOwnProperty("physicalid")&&""===e.physicalid)throw n.epmtyPhysicalId;t.physicalIds.push(e)}),this._executeAnalyzis(function(n){t._showCommandUI(e,n)},function(n){t._showCommandFailed(e,n)})},_logToConsole:function(e){this.wintop?(this.webToWinCom_Socket.dispatchEvent("onDispatchToWin",{notif_name:"onLogToCNEXTOUTPUT",notif_parameters:e},"lf_web_in_win"),console.log(e)):console.log(e)},_setWaitingResponse:function(){this.widget?this.widget.body.style.cursor="wait":document.body.style.cursor="wait",this._computingTxt&&this._computingTxt.show()},_setEndWaitingResponse:function(){this.widget?this.widget.body.style.cursor="default":document.body.style.cursor="default",this._computingTxt&&this._computingTxt.hide()},_showCommandFailed:function(e,t){this._logToConsole(t),this._logToConsole("\n");var n=null;if(t&&t.status&&t.status,t&&t.report&&t.report.length>=1){n=i.error_message_report;for(var a=0;a<t.report.length;a++){var r=t.report[a];null!==r&&r.hasOwnProperty("message")&&(n=n+"\n"+r.message)}}if(t&&t.error&&t.error.length>=1){n="";var o=t.error[0];null!==o&&o.hasOwnProperty("message")&&(n=o.message);for(a=1;a<t.error.length;a++)null!==(o=t.error[a])&&o.hasOwnProperty("message")&&(n=n+"\n"+o.message)}null===n&&(n=i.error_message_invalid_server_response),this.wintop||this.odtmode?this.webToWinCom_Socket.dispatchEvent("onDispatchToWin",{notif_name:"onDisplayErrorMessage",notif_parameters:{severity:"warning",message:n}},"lf_web_in_win"):alert("computation failed:\n"+n),this.dispatchEvent("onComplete",{})},_showCommandUI:function(e,t){var n=null;if(this.wintop||this.runmyapp)(n=this.container).innerHTML="";else{n=new UWA.Element("div",{name:"data_analyzis",class:"dataanalyze_popup","data-rec-id":"dataanalyze_popup"});var a=this,r=new u,o=r.create(n,{title:i.CommandDialogTitle,closeButton:!0,resizable:!0,buttonStyle:r.buttonStyle.none,onClose:function(){a.dispatchEvent("onComplete",{})}});void 0!==this.widget&&null!==this.widget?o.inject(this.widget.body):o.inject(document.body)}if(1!=e.length||this.wintop?1==e.length?new UWA.Element("h5",{text:i.H5Title1+":"}).inject(n):new UWA.Element("h5",{text:i.H5TitleS+":"}).inject(n):new UWA.Element("h5",{text:i.H5Title+e[0].displayName+":"}).inject(n),null!=t&&t.hasOwnProperty("summary")){var s=[],l=t.summary,c=!1,m=!1,h=this._itemSummaryCount(l);null!=h&&(s.push(h),c=!0);var d=this._itemSummaryDepth(l);null!=d&&(s.push(d),m=!0);var y=new UWA.Element("div",{"data-rec-id":"advanced_div",class:"dataanalyze_advanced"}),_=this._itemSummaryTypes(l,this.tenant);null!=_&&_.inject(y);var f=e[0].displayName,v=this._DumpList(f,l,t,this.tenant);null!=v&&v.inject(y);var w={header:i.AdvancedSection,content:y};s.push(w);var g=new p({items:s,exclusive:!1,style:"simple",recId:"displayedSummaryInt"});g.getContent().setAttribute("data-rec-id","displayedSummaryExt"),g.inject(n),g.items.length>0&&(c||m)&&g.expandItem(0),g.items.length>1&&m&&g.expandItem(1),t.hasOwnProperty("elapse_time")&&t.hasOwnProperty("cloudview_elapse_time")&&new UWA.Element("text",{class:"dataanalyze_elapses_time",text:i.elapse_time+t.elapse_time+"(ms) , "+i.cloudview_elapse_time+t.cloudview_elapse_time+"(ms)"}).inject(n)}},_executeAnalyzis:function(e,t){var n=this,i=this.physicalIds,r=[];r.push({expand_tool:"cloudview"});var o=function(n){null!=n&&n.hasOwnProperty("status")&&"failure"!==n.status&&"error"!==n.status&&"success"===n.status&&n.hasOwnProperty("results")?e(n.results):t(n)};a.getSecurityContextPromise(this.tenant).then(function(e){n._analyzisServiceRequest(i,r,o,e)}).catch(function(e){t({status:"failure",error:{message:e.message}})})},_analyzisServiceRequest:function(e,t,n,a){var s=this;1==e.length&&void 0!==e[0]&&null!==e[0]&&e[0].hasOwnProperty("physicalid")?this._logToConsole("DATA ANALYZIS: "+e[0].physicalid+"\n"):e.length>1?this._logToConsole("DATA ANALYZIS: "+e.length+" objects\n"):this._logToConsole("DATA ANALYZIS: ???\n"),this._logToConsole(e),this._logToConsole("\n");var l=JSON.stringify({data:e,options:t}),c=r.get3DSpaceWSUrl(this.tenant,"/resources/lifecycle/dataanalyze/structure","SecurityContext="+a);this._setWaitingResponse(),o.authenticatedRequest(c,{method:"POST",headers:r.getHeaders(encodeURIComponent(a)),timeout:864e5,onComplete:function(e){s._setEndWaitingResponse(),s._logToConsole("DATA ANALYZIS: COMPLETED\n"),n(e)},data:l,onTimeout:function(e){s._setEndWaitingResponse(),s._logToConsole("DATA ANALYZIS: Timeout error\n"),n({status:"failure",error:{message:i.error_message_timeout}})},onFailure:function(e,t){s._setEndWaitingResponse(),s._logToConsole("DATA ANALYZIS: Failed to retrieve data from the server\n"),null!=t&&s._logToConsole(t),n({status:"failure",error:{message:i.error_message_request_failed}})},type:"json"})},_itemSummaryCount:function(e){if(null!==e){var t=0;e.hasOwnProperty("nbObjects")&&(t=e.nbObjects);var n=0;e.hasOwnProperty("nbEntities")&&(n=e.nbEntities);var a=0;e.hasOwnProperty("nbRelations")&&(a=e.nbRelations);var r=new UWA.Element("div",{"data-rec-id":"itemSummaryCount_div"}),o=new UWA.Element("ul").inject(r);return n>1?new UWA.Element("li",{text:n+i.itemSummaryCount_entities}).inject(o):new UWA.Element("li",{text:n+i.itemSummaryCount_entity}).inject(o),a>=1&&new UWA.Element("li",{text:a+i.itemSummaryCount_relations}).inject(o),{header:i.itemSummaryCount_summaryItem+t,content:r}}},_itemSummaryDepth:function(e){if(null!==e&&e.hasOwnProperty("max_depth")&&e.max_depth>0){var t=new UWA.Element("div",{"data-rec-id":"itemSummaryDepth_div"}),n=new UWA.Element("ul").inject(t);return new UWA.Element("li",{text:i.replace(i.itemSummaryDepth_one_structure,{max_depth:e.max_depth})}).inject(n),{header:i.itemSummaryDepth_header,content:t}}if(null!==e&&e.hasOwnProperty("structures")){var a=e.structures;t=new UWA.Element("div",{"data-rec-id":"itemSummaryDepth_div"});new UWA.Element("text",{text:i.replace(i.itemSummaryDepth_several_structures,{nbStructures:a.length})}).inject(t);n=new UWA.Element("ul").inject(t);return a.forEach(function(e){if(e.hasOwnProperty("nbObjects")&&e.hasOwnProperty("max_depth")){var t=e.nbObjects,a=e.max_depth;new UWA.Element("li",{text:i.replace(i.itemSummaryDepth_composed_of,{structure_nb_objects:t,max_depth:a})}).inject(n)}}),{header:i.itemSummaryDepth_header,content:t}}},_itemSummaryTypes_ListContent:function(e,t,n){var i=new UWA.Element("div",{class:"itemSummaryTypes_ListContent","data-rec-id":"itemSummaryTypes_ListContent_div"}),a=new UWA.Element("table").inject(i);return t.forEach(function(t){if(null!==t&&t.hasOwnProperty("name")&&t.hasOwnProperty("count")){var i=t.name,o=parseInt(t.count);if(e.hasOwnProperty(i)){var s=e[i];if(null!==s&&s.hasOwnProperty("icon")){var l="";l=s.hasOwnProperty("nls")&&null!==s.nls&&""!==s.nls?s.nls:"`"+s.name+"`";var c="";s.hasOwnProperty("icon")&&null!==s.icon&&""!==s.icon&&(c=s.icon);var u=new UWA.Element("tr").inject(a),p=r.get3DSpaceWSUrl(n,c),m=new UWA.Element("td").inject(u);new UWA.Element("img",{src:p}).inject(m);var h=new UWA.Element("td",{class:"dataanalyze_typelist_count_td"}).inject(u);new UWA.Element("text",{text:o+" "}).inject(h);var d=new UWA.Element("td").inject(u);new UWA.Element("text",{text:l+"(s)"}).inject(d)}}else console.log("_itemSummaryTypes_ListContent / not found "+i)}}),i},_typeGoThru:function(e,t,n,i,a){if(i.hasOwnProperty(e)){var r=i[e],o=null;a.hasOwnProperty(e)?o=a[e]:(o={name:e,nls:"",icon:"",count:0,isroot:!1,nodechildren:[],irpc:""},a[e]=o,null!==r&&r.hasOwnProperty("nls")&&(o.nls=r.nls),null!==r&&r.hasOwnProperty("icon")&&(o.icon=r.icon),null!==r&&r.hasOwnProperty("irpc")&&(o.irpc=r.irpc),null!==r&&r.hasOwnProperty("parent")&&null!==r.parent?o.isroot=!1:(o.isroot=!0,a.push(o))),o.count=o.count+n,null===t||o.nodechildren.hasOwnProperty(t.name)||(o.nodechildren[t.name]=t,o.nodechildren.push(t)),null!==r&&r.hasOwnProperty("parent")&&null!==r.parent&&this._typeGoThru(r.parent,o,n,i,a)}else console.log("_typeGoThru / not found "+e)},_convertTypesListAsTreeNode:function(e,t,n){var i=this,a=!1,o="";t.hasOwnProperty("nls")&&null!==t.nls&&""!==t.nls?(o=t.nls+" ("+t.count+")",a=!0):o="`"+t.name+"` ("+t.count+")",t.label=o;var s=r.get3DSpaceWSUrl(n,t.icon);t.icons=[s],t.data=t;var l=[];if(t.nodechildren.forEach(function(t){var a=i._convertTypesListAsTreeNode(e,t,n);for(var r in a)l.push(a[r])}),a&&(1!=l.length||t.isroot||1==l.length&&l[0].count!=t.count)){if(0!=l.length)for(var c in t.children=[],l)t.children.push(l[c]);return[t]}return a?1==l.length&&console.log("_convertTypesListAsTreeNode / skipping only 1 child: "+o):console.log("_convertTypesListAsTreeNode / skipping not displayable: "+o),l},_itemSummaryTypes_TreeContent:function(e,t,n){var i=[],a=this;t.forEach(function(t){if(null!==t&&t.hasOwnProperty("name")&&t.hasOwnProperty("count")){var n=t.name,r=parseInt(t.count);a._typeGoThru(n,null,r,e,i)}});var r=null,o=null,s=null,l=null,c=null,u=null,p=[],m=[],h=[];i.forEach(function(e){"PLMCoreInstance"==e.name?r=e:"PLMCoreRepInstance"==e.name?s=e:"PLMCoreReference"==e.name?o=e:"PLMCoreRepReference"==e.name?l=e:"PLMPort"==e.name?c=e:"PLMConnection"==e.name?u=e:e.isroot&&"PLMInstance"==e.irpc?r.push(e):e.isroot&&"PLMRepInstance"==e.irpc?s.push(e):e.isroot&&"PLMReference"==e.irpc?o.push(e):e.isroot&&"PLMRepReference"==e.irpc?l.push(e):e.isroot&&"PLMConnection"==e.irpc?u.push(e):e.isroot&&"PLMPort"==e.irpc?c.push(e):e.isroot&&"entity"==e.irpc?p.push(e):e.isroot&&"relation"==e.irpc?m.push(e):h.push(e)}),i=[],null!==o&&i.push(o),null!==r&&i.push(r),null!==l&&i.push(l),null!==s&&i.push(s),null!==u&&i.push(u),null!==c&&i.push(c),i=(i=(i=i.concat(p)).concat(m)).concat(h);var y=new UWA.Element("div",{"data-rec-id":"itemSummaryTypes_TreeContent_div",class:"dataanalyze_itemSummaryTypes_TreeContent"});return i.forEach(function(e){a._convertTypesListAsTreeNode(y,e,n)}),new d({apiVersion:2,roots:i}).inject(y),y},_itemSummaryTypes:function(e,t){var n=new UWA.Element("div",{"data-rec-id":"itemSummaryTypes_div"});if(null!==e&&e.hasOwnProperty("typesinfo")&&e.hasOwnProperty("typescount")){var a=e.typesinfo,r=e.typescount;if(null!==a&&0!==a.length&&null!==r&&0!==r.length){var o=new h({displayStyle:"panel",multiSelFlag:!0,reorderFlag:!1,showComboBoxFlag:!0,recId:"typeSummaryTab"});o.tabBar.closeButtonFlag=!1,o.tabBar.centeredFlag=!1;var s=this._itemSummaryTypes_ListContent(a,r,t),l=this._itemSummaryTypes_TreeContent(a,r,t);o.add({label:i.Types_as_list,content:s,index:0,value:0}),o.add({label:i.Types_as_tree,content:l,index:1,value:1}),o.value=[0],o.inject(n)}}return n},_DumpList:function(e,t,n,a){var r=this,o=new UWA.Element("div");if(o.setStyle("padding-top","10px"),null!=n&&n.hasOwnProperty("structure")&&null!=t&&t.hasOwnProperty("typesinfo")){var s=n.structure,l=t.typesinfo,c=["id","type","irpc","primarytype","interface","name","revision","physicalid","policy","owner","project","organization","PLM_ExternalID","V_Name","current","V_discipline","V_Owner","V_InstanceOf"],u=new m({label:i.Dump_objects,recId:"dumpButton"});u.inject(o),u.addEventListener("buttonclick",function(t){var n="",i=0;for(i=0;i<c.length;i++)n+=c[i],i!==c.length-1&&(n+=",");n+="\n";var a={};if(null!=l)for(var o in l)if(l.hasOwnProperty(o)){var u=l[o];a.hasOwnProperty(u.name)||(a[u.name]=[]),a[u.name].push(u)}s.forEach(function(e){var t=e.type;if(a.hasOwnProperty(t)){var r=a[t],o=null;null!=r&&r.length>=1&&(o=r[0]),null!=o&&o.hasOwnProperty("irpc")&&(e.irpc=o.irpc)}for(i=0;i<c.length;i++){var s=c[i];null!==e&&e.hasOwnProperty(s)&&(n+=e[s]),i!==c.length-1&&(n+=",")}n+="\n"});var p=new Blob([n],{type:"data:text/csv;charset=utf-8"}),m=window.document.createElementNS("http://www.w3.org/1999/xhtml","a");m.href=URL.createObjectURL(p),m.download=e+".csv";var h=new MouseEvent("click");r.odtmode||m.dispatchEvent(h)})}return o}})});