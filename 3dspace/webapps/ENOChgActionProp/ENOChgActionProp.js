define("DS/ENOChgActionProp/scripts/Models/CAPropTileViewModel",["UWA/Class/Model","UWA/Date","DS/TreeModel/TreeDocument","DS/TreeModel/TreeNodeModel","DS/TreeModel/DataModelSet","DS/ENOChgServices/scripts/services/ChgPropertiesComponentServices","DS/ENOChgServices/scripts/services/ChgIDCardCommonServices","DS/ENOChgServices/scripts/services/ChgMembersServices","DS/ENOChgServices/scripts/services/ChgServiceGlobalVariable","DS/PlatformAPI/PlatformAPI","i18n!DS/ENOChgActionProp/assets/nls/ChangePropertiesFacet","DS/ENOChgServices/scripts/services/ChgDataProcess","DS/ENOChgServices/scripts/services/ChgDataService"],function(e,t,n,i,a,r,o,s,l,c,p,u,d){"use strict";return e.extend({init:function(){var e=new a;this.treeDocument=new n({dataModelSet:e})},fetch:function(e){return e.method="GET",e.ajax=function(t,n){var i=[e.caId.replace("pid:","")],a=[],l=r.getCADetails(e.caId),c=d.getObjectInfo(i,["icon","image"]);a.push(l,c),Promise.all(a).then(function(e){var t;e[0].changeaction?((t=e[0]).changeaction.icon=e[1][0].dataelements.icon,t.changeaction.image=e[1][0].dataelements.image):((t=e[1]).changeaction.icon=e[0][0].dataelements.icon,t.changeaction.image=e[0][0].dataelements.image);var i=[];if(t&&t.changeaction){var a=t.changeaction.people;a.length&&(a.forEach(function(e){"owner"==e.role&&i.push(e.name)}),i.length&&o.getUserFullNameFromIDs(i).then(function(e){s._addFullNameToPersonObject(t,e),n.onComplete(t)}))}})},e.url="dummyURL",this._parent(e)},parse:function(e){var n,r,o,s,l,d,C=e.changeaction;new a;this.treeDocument.prepareUpdate(),C.people.forEach(function(e){"owner"==e.role&&(n=e)});var h=C.attributes;UWA.is(h)&&h.forEach(function(e){"currentNLS"===e.name?r=e.value:"attribute_Synopsis"===e.name?o=u.escapeHTML(e.value):"attribute_EstimatedCompletionDate"===e.name?s=UWA.is(e.value)&&""!==e.value?t.strftime(new Date(e.value),"%Y-%m-%d"):"":"attribute_Severity"===e.name?l=e.value:"interface[Change On Hold]"===e.name&&(d="TRUE"==e.value)});var m=C.name+" | "+n.fullName,g=r,v=p.CHG_Prop_Tile_Header_Title+": "+o+"\n"+p.CHG_Prop_Tile_Header_Name+": "+C.name+"\n"+p.CHG_Prop_Tile_Header_Owner+": "+n.fullName+"\n"+p.CHG_Prop_Tile_Maturity_State+": "+r;""!=s&&(v=v+"\n"+p.CHG_Prop_Tile_DueDate+": "+s,g=g+" | "+s);var S=["level-low"],f=[p.CHG_Prop_Tile_Severity_Low];"Medium"==l?(S=["level-medium"],f=[p.CHG_Prop_Tile_Severity_Medium]):"High"==l&&(S=["level-high"],f=[p.CHG_Prop_Tile_Severity_High]),d&&(S.push("pause"),f.push(p.CHG_Prop_Tile_State_On_Hold)),C.referentials.length>0&&(S.push("attach"),f.push(p.CHG_Prop_Tile_Referential_label));var A=new i({label:o,subLabel:m,description:g,grid:C,icons:[C.icon],thumbnail:C.image,customTooltip:v,contextualMenu:["dummy value to show icon"],statusbarIcons:S,statusbarIconsTooltips:f});this.treeDocument.addChild(A),c.publish("ca:tilemodel:ready"),c.unsubscribe("ca:tilemodel:ready")}})}),define("DS/ENOChgActionProp/scripts/Models/CAApprovalSumModel",["UWA/Class/Model"],function(e){"use strict";return e.extend({idAttribute:"id",setup:function(){},fetch:function(e){e.method="GET";return e.ajax=function(e,t){},e.url="dummyURL",this._parent(e)},parse:function(e){if(UWA.is(e)&&UWA.is(e)){this.set("id",e.id),this.set("order",e.order),this.set("taskName",e.name),this.set("taskTitle",e.title),this.set("taskApprover",e.approver),this.set("taskMaturity",e.maturity),this.set("approvalComments",e.comments),this.set("approvalStatus",e.action),this.set("actualComplDate",e.actualComplDate)}}})}),define("DS/ENOChgActionProp/facets/ChangePropertiesFacet",["UWA/Core","DS/EditPropWidget/facets/Common/FacetsBase","DS/UIKIT/Mask","css!DS/ENOChangeActionUX/ENOChangeActionUX","DS/WidgetServices/securityContextServices/SecurityContextServices","DS/ENOChgServices/scripts/services/ChgInfraService","DS/ENOChgServices/scripts/services/ChgServiceGlobalVariable"],function(e,t,n,i,a,r,o){"use strict";return t.extend({init:function(t){this._parent.apply(this,arguments);var n=o.getGlobalVariable();"Native"===this.checkEnvironment()?n.isNative=!0:"3DSpace"===this.checkEnvironment()&&(n.is3DSpace=!0),this.elements.container||(this.elements.container=e.createElement("div",{styles:{height:"100%"}}))},checkEnvironment:function(){var e=window.top.location.pathname;return e.indexOf("emxNavigator.jsp")>=0?"3DSpace":"undefined"!=typeof dscef&&e.indexOf("ENOChangeAction.html")>=0?"Native":void 0},updateFacet:function(){if(this.modid&&!this.getModel())return!1;var e=this;n.mask(this.elements.container),require(["DS/ENOChgActionProp/scripts/ChangePropertiesView","DS/ENOChgActionProp/scripts/Models/CAApprovalSumModel"],function(t,i){if(e.elements&&e.elements.container){var s=e.getModel();if(!s)return!1;var l={isReference:!0,prodid:"relationship"===s.get("metatype")?s.get("selReferenceID"):s.get("objectId")};e.modid=l.prodid;var c=o.getGlobalVariable();a.getInstance({tenant:c.tenant}).getSecurityContext({onSuccess:function(i){c.securityContext=i.SecurityContext,c.mySecurityContext=i.SecurityContext,r.populate3DSpaceURL().then(function(i){r.getExpressionValue().then(function(i){var a=c.CRView=(new t).displayChangeReq(l,e);n.unmask(e.elements.container),e.elements.container.setContent(a)})})},onFailure:function(e){console.log("Failed to GetSecurityContext")}})}})},destroyComponent:function(){e.is(this.destroy,"function")&&this.destroy()}})}),define("DS/ENOChgActionProp/scripts/ChangePropertiesApprovalSumView",["UWA/Core","UWA/Class","UWA/Class/Collection","DS/W3DXComponents/Views/Layout/TableScrollView","i18n!DS/ENOChgActionProp/assets/nls/ChangePropertiesFacet"],function(e,t,n,i,a){"use strict";return{_createView_ApprovalSummaryTable:function(e){var t=new n(e);return new i({collection:t,useInfiniteScroll:!1,usePullToRefresh:!1,headers:[{property:"order",label:a.Order},{property:"taskTitle",label:a.Title},{property:"taskApprover",label:a.Approver},{property:"approvalStatus",label:a.Action},{property:"approvalComments",label:a.Comments},{property:"actualComplDate",label:a.Completion_Date}]})}}}),define("DS/ENOChgActionProp/scripts/ChangePropertiesDataGridView",["UWA/Core","UWA/Class","DS/DataGridView/DataGridView","i18n!DS/ENOChgActionProp/assets/nls/ChangePropertiesFacet"],function(e,t,n,i){"use strict";return{_createView_ApprovalSummaryTable:function(e){var t=new n({columns:[{text:i.Order,dataIndex:"order",typeRepresentation:"string",width:"47px"},{text:i.Title,dataIndex:"title",typeRepresentation:"string"},{text:i.Approver,dataIndex:"approver",typeRepresentation:"string"},{text:i.Action,dataIndex:"status",typeRepresentation:"string"},{text:i.Comments,dataIndex:"comments",typeRepresentation:"string"},{text:i.Completion_Date,dataIndex:"approvalDate",typeRepresentation:"string"}],treeDocument:e,showRowIndexFlag:!1,showSelectionCheckBoxesFlag:!1});return t&&(t.layout.rowsHeader=!1),t}}}),define("DS/ENOChgActionProp/facets/iChangePropertiesFacet",["UWA/Core","DS/EditPropWidget/facets/Common/FacetsBase","DS/EditPropWidget/constants/EditPropConstants","DS/UIKIT/Mask","css!DS/ENOChangeActionUX/ENOChangeActionUX","DS/WidgetServices/securityContextServices/SecurityContextServices","DS/ENOChgServices/scripts/services/ChgInfraService","DS/ENOChgServices/scripts/services/ChgServiceGlobalVariable"],function(e,t,n,i,a,r,o,s){"use strict";return t.extend({metatype:n.METATYPE_REL,init:function(t){this._parent.apply(this,arguments);var n=s.getGlobalVariable();"Native"===this.checkEnvironment()?n.isNative=!0:"3DSpace"===this.checkEnvironment()&&(n.is3DSpace=!0),this.elements.container||(this.elements.container=e.createElement("div",{styles:{height:"100%"}}))},checkEnvironment:function(){var e=window.top.location.pathname;return e.indexOf("emxNavigator.jsp")>=0?"3DSpace":"undefined"!=typeof dscef&&e.indexOf("ENOChangeAction.html")>=0?"Native":void 0},updateFacet:function(){if(this.modid&&!this.getModel())return!1;var e=this;i.mask(this.elements.container),require(["DS/EditPropWidget/constants/EditPropConstants","DS/ENOChgActionProp/scripts/ChangePropertiesView","DS/ENOChgActionProp/scripts/Models/CAApprovalSumModel"],function(t,n,a){if(e.elements&&e.elements.container){if(!e.getModel())return!1;var l={isReference:!1,prodid:e.getOccurrence().getModel(t.MODEL_INIT).get("objectId")};e.modid=l.prodid;var c=s.getGlobalVariable();r.getInstance({tenant:c.tenant}).getSecurityContext({onSuccess:function(t){c.securityContext=t.SecurityContext,c.mySecurityContext=t.SecurityContext,o.populate3DSpaceURL().then(function(t){o.getExpressionValue().then(function(t){var a=c.CRView=(new n).displayChangeReq(l,e);i.unmask(e.elements.container),e.elements.container.setContent(a)})})},onFailure:function(e){console.log("Failed to GetSecurityContext")}})}})},destroyComponent:function(){e.is(this.destroy,"function")&&this.destroy()}})}),define("DS/ENOChgActionProp/scripts/ChgGropCADialog",["DS/ENOChgServices/scripts/services/ChgPropertiesComponentServices","DS/Controls/TooltipModel","i18n!DS/ENOChgActionProp/assets/nls/ChangePropertiesFacet"],function(e,t,n){"use strict";return{setGroupCATooltop:function(i){var a=this;a.target=i.target,a.title=i.title,a.groupPid=i.groupPid;var r=n.Group_CA_Tooltip_Info;r+="\n";e.getChangeActionForGrop(a.groupPid).then(function(e){for(var n=e.changeactions,i=0;i<n.length;i++){var o=n[i].title,s=n[i].name;r+=o+" ("+s+")",i!=n.length-1&&(r+="\n")}var l=new t({title:a.title,shortHelp:r});a.target.tooltipInfos=l})}}}),define("DS/ENOChgActionProp/scripts/ChangePropertiesCATileView",["UWA/Core","UWA/Class","DS/Handlebars/Handlebars4","DS/W3DXComponents/Views/Item/TileView","DS/WebappsUtils/WebappsUtils","DS/TransientWidget/TransientWidget","DS/ENOChgServices/scripts/services/ChgServiceGlobalVariable","DS/ENOChgServices/scripts/services/ChgNativeInitService","DS/CollectionView/ResponsiveTilesCollectionView","DS/ENOChgServices/scripts/services/ChgPropertiesComponentServices","DS/Core/PointerEvents","i18n!DS/ENOChgActionProp/assets/nls/ChangePropertiesFacet","DS/ENOChgServices/scripts/services/ChgInfraService","require"],function(e,t,n,i,a,r,o,s,l,c,p,u,d,C){"use strict";var h={_createNewView_CATile:function(e){var t=this,n=new l({model:e,height:"inherit",displayedOptionalCellProperties:["contextualMenu","description","propertiesList","statusbarIcons"],allowUnsafeHTMLContent:!0,useDragAndDrop:!0,grid:{selectionBehavior:{toggle:!0,unselectAllOnEmptyArea:!0,canMultiSelect:!0,canInteractiveMultiselectWithCheckboxClick:!0}},onDragStartCell:function(e,t){d.readyDragContent(e,t)},onDragEnterBlankDefault:function(e){},onDragEnterBlank:function(e){}}),i={callback:function(e){return e&&e.cellInfos&&e.cellInfos.cellModel?t.showRMBMenuOnTile(t,e).then(function(e){return e}):[]}};return n.onContextualEvent=i,n},showRMBMenuOnTile:function(e,t){return new Promise(function(n,i){var a=[],r=t.cellInfos.nodeModel.options.grid.id;r.contains("pid:")&&(r=r.replace("pid:",""));var o={objectId:r,objectType:t.cellInfos.nodeModel.options.grid.type,displayName:t.cellInfos.nodeModel.options.grid.name,displayType:t.cellInfos.nodeModel.options.grid.displayType,facetName:"dependency"},s=C("DS/ENOChgServices/scripts/services/ChgIDCardCommonServices").openWith(o);a.push(h.getRelationalExplorerMenu(e,t)),s.then(e=>{e&&a.push(e),n(a)})})},getRelationalExplorerMenu:function(e,t){return{type:"PushItem",title:u.CHG_Prop_Tile_Menu_Relational_Explorer,fonticon:{family:1,content:"wux-ui-3ds wux-ui-3ds-1x wux-ui-3ds-object-related"},action:{callback:function(e){require(["DS/ENOChgServices/scripts/services/ChgIDCardCommonServices"],function(e){var n=t.cellInfos.nodeModel.options.grid.id,i=[];i.push(n.replace("pid:","")),e.openRelationalExplorer(i)})}}}}};return h}),define("DS/ENOChgActionProp/scripts/ChangePropertiesView",["UWA/Core","UWA/Class","DS/UIKIT/Alert","DS/UIKIT/Input/Toggle","DS/WebappsUtils/WebappsUtils","DS/ENOChgActionProp/scripts/Models/CAPropTileViewModel","DS/ENOChgActionProp/scripts/Models/CAApprovalSumModel","css!DS/ENOChgActionProp/ENOChgActionProp","DS/ENOChgActionProp/scripts/ChangePropertiesCATileView","DS/ENOChgActionProp/scripts/ChangePropertiesDataGridView","DS/UIKIT/Mask","DS/PlatformAPI/PlatformAPI","UWA/Class/Promise","DS/ENOChgServices/scripts/services/ChgPropertiesComponentServices","DS/ENOChgServices/scripts/services/ChgIDCardHeaderServices","DS/ENOChgActionProp/scripts/ChgGropCADialog","DS/ENOChgServices/scripts/services/ChgServiceGlobalVariable","i18n!DS/ENOChgActionProp/assets/nls/ChangePropertiesFacet"],function(e,t,n,i,a,r,o,s,l,c,p,u,d,C,h,m,g,v){"use strict";return t.extend({displayChangeReq:function(e,t){this.options=t.options,this.chgMod=e;var n=this.CreateElements(t),i=this;return this.ChangeData=C.GetChangeRequired(this.chgMod.prodid).then(function(e){i.UpdateUIForCC(e),i.onResize(t)},function(e){n.setContent("Error while getting Change Required value"),console.log("Failed to Get Change Required")}),n},onResize:function(e){var t=e.elements.container.getStyle("width");t&&(t=parseInt(t));var n=document.getElementsByClassName("CR_MainContainer")[0].offsetWidth,i=document.getElementsByClassName("ChangeReq_CheckBox")[0].offsetWidth,a=document.getElementsByClassName("CAGroupNameContainer")[0],r=document.querySelectorAll(".CA_NameContainer .CANewTile-container")[0],o=0,s=!1;a?o=a.offsetWidth+70:(r||document.getElementsByClassName("ChangeReq_CheckBox")[0].hasClassName("hidden"))&&(o=320,s=!0);var l=document.getElementsByClassName("CR_Container")[0],c=l.offsetWidth,p=l.currentStyle||window.getComputedStyle(l);c+=2*parseInt(p.marginRight);var u=l,d=document.getElementsByClassName("CR_MainContainer")[0];c+i+o>n?(u.setStyle("max-width","200px"),d.setStyle("display","inline-block")):(u.setStyle("max-width","200px"),s?d.setStyle("display","inline-block"):d.setStyle("display","flex"))},CreateElements:function(e){var t=this,n=UWA.Element("div",{class:"GroupsContainer"});this.ChgReqGroup=UWA.Element("div",{class:"CR_MainContainer"}),this.ChgReqContainer=UWA.Element("div",{class:"CR_Container",title:v.Change_Required,html:v.Change_Required});var a;e.elements&&e.elements.container&&e.elements.container.getStyle("width"),t=this;return e.elements&&e.elements.container&&e.elements.container.addEvent("resize",function(){setTimeout(function(){t.onResize(e)},50)}),this.checkbox=new i({type:"switch",label:"",className:"ChangeReq_CheckBox",checked:!1,disabled:this.options.readOnly,events:{onChange:function(){var e=this,n=this.isChecked(),i=1==n?"Set":"UnSet";this.ChangeData=C.SetUnsetChangeRequired(t.chgMod.prodid,i).then(function(e){t.UpdateUIForCC(e)},function(t){return 1==n?e.setCheck(!1):e.setCheck(!0),d.reject(t)})}}}),this.CAContainer=UWA.Element("div",{class:"CA_NameContainer hidden"}),this.ChgReqGroup.addContent(this.ChgReqContainer),this.ChgReqGroup.addContent(this.CAContainer),this.ChgReqGroup.addContent(this.checkbox),n.addContent(this.ChgReqGroup),this.ApprovalSummaryGroup=UWA.Element("div",{class:"ApprovalSummaryGroup"}),this.ApprovalSummaryGroupName=UWA.Element("div",{class:"ApprovalSummaryGroupName",html:v.Release_Auth}),this.ApprovalSummaryCATileMain=UWA.Element("div",{class:"ApprovalSummaryCATileMain"}),this.ApprovalSummaryCATile=UWA.Element("div",{class:"ApprovalSummaryCATile"}),this.ApprovalSummaryTable=UWA.Element("div",{class:"ApprovalSummaryTable"}),!0===(t=this).chgMod.isReference&&(p.mask(n),C.getImpactedCA(this.chgMod.prodid).then(function(e){if(!e){p.unmask(n);var i=UWA.Element("div",{class:"error-msg-div",html:[{tag:"span",class:"fonticon fonticon-attention"},{tag:"span",html:v.ECM_OBJECT_NOT_RELEASED_ERR}]});return t.ApprovalSummaryGroup.addContent(t.ApprovalSummaryGroupName),t.ApprovalSummaryGroup.addContent(i),n.addContent(t.ApprovalSummaryGroup),n}C.getConfiguredObjectInfo(t.chgMod.prodid).then(function(i){if(i.error&&""!=i.error){var r=UWA.Element("div",{class:"error-msg-div",html:[{tag:"span",class:"fonticon fonticon-attention"},{tag:"span",html:i.error}]});return t.ApprovalSummaryGroup.addContent(t.ApprovalSummaryGroupName),t.ApprovalSummaryGroup.addContent(r),n.addContent(t.ApprovalSummaryGroup),p.unmask(n),n}a=e;var o=t.createModelForTile(a);u.subscribe("ca:tilemodel:ready",function(){var e=l._createNewView_CATile(o.treeDocument),i=[],r={};i=[];if(e){var s=UWA.Element("div",{class:"CANewReleaseAuthTile-container"});s.addContent(e.getContent()),t.ApprovalSummaryCATile.addContent(s)}var u={};u.caId=a;var m=[];C.getRouteFromChangeAction(u.caId).then(function(e){var a=e.lastApproveRoute;a&&C.getRouteDetails(a).then(function(e){h.getCompletedTaskDetails(u.caId).then(function(a){if(e&&e.routeInfo&&e.routeInfo.success){e.routeInfo.data.forEach(function(e){for(var n=e.id.replace("pid:",""),o=a.completedTaskDetail.inboxTasks,s=0;s<o.length;s++){var l=o[s];if(l)if(l.id.replace("pid:","")===n){r[n]={id:n,name:e.dataelements.name,title:e.dataelements.title,maturity:e.dataelements.current,comments:e.dataelements.comments,order:e.dataelements.taskOrder,approver:e.dataelements.taskAssignee,action:e.dataelements.taskAction,actualComplDate:l.actualCompletionDate},r[n].action==l.approvalStatus&&(r[n].action=l.approvalStatusNLS),m.push(r);var c=t.createCAApprovalSumModel(r[n]);i.push(c)}}});var o=C.buildApprovalSummaryDataGridModel(i),s=c._createView_ApprovalSummaryTable(o);s&&t.ApprovalSummaryTable.addContent(s),p.unmask(n),t.ApprovalSummaryCATileMain.addContent(t.ApprovalSummaryCATile),t.ApprovalSummaryGroup.addContent(t.ApprovalSummaryGroupName),t.ApprovalSummaryGroup.addContent(t.ApprovalSummaryCATileMain),t.ApprovalSummaryGroup.addContent(t.ApprovalSummaryTable),n.addContent(t.ApprovalSummaryGroup)}},function(e){return p.unmask(n),d.reject(e)})},function(e){return p.unmask(n),d.reject(e)})},function(e){return p.unmask(n),d.reject(e)})})},function(e){p.unmask(n),d.reject(e)})},function(e){return p.unmask(t.ApprovalSummaryGroup),d.reject(e)})),n},createModelForTile:function(e){var t={};return t.caId=e,this.caTileModel=new r,this.caTileModel.fetch(t),this.caTileModel},createCAApprovalSumModel:function(e){return this.tableModel=new o,this.tableModel.parse(e),this.tableModel},UpdateUIForCC:function(e){var t=this;if("none"==e.CCValue||"any"==e.CCValue)t.checkbox.setClassName("ChangeReq_CheckBox"),t.CAContainer.hasClassName("hidden")||t.CAContainer.addClassName("hidden"),"any"==e.CCValue?t.checkbox.setCheck(!0):t.checkbox.setCheck(!1);else if(null!=e.ChangeData){t.checkbox.elements.container.addClassName("hidden"),t.CAContainer.removeClassName("hidden");a.getWebappsAssetUrl("ENOChgActionProp","icons/iconSmallChangeAction.png");var n=t.createModelForTile(e.ChangeData.id);u.subscribe("ca:tilemodel:ready",function(){var e=l._createNewView_CATile(n.treeDocument),i=UWA.Element("div",{class:"CANewTile-container"});i.addContent(e.getContent()),t.CAContainer.setContent(i)})}else if(null!=e.GroupChangeData&&"true"==g.getGlobalVariable().CHG_CHANGE_GROUP){t.checkbox.elements.container.addClassName("hidden"),t.CAContainer.removeClassName("hidden"),t.CAContainer.setContent({tag:"span",html:e.GroupChangeData.title,class:"CAGroupNameContainer"},{tag:"span",html:"&nbsp;&nbsp;"},{tag:"span",class:"fonticon fonticon-group  CAIconContainer"});var i={title:v.Change_Required_by_Group+" : "+e.GroupChangeData.title,target:t.CAContainer,groupPid:e.GroupChangeData.id};m.setGroupCATooltop(i)}}})});