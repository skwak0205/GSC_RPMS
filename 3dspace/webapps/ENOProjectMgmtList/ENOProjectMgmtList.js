define("DS/ENOProjectMgmtList/config/ContextSelectionConfig",["DS/ENOProjectMgmtCommonComponents/config/ConfigBase","DS/ENOProjectMgmtModels/Collections/SearchObjects","DS/ENOProjectMgmtModels/Utils/FederatedSearch","DS/ENOProjectMgmtCommonComponents/views/ProjectMgmtObjectCardView","DS/ENOProjectMgmtCommonComponents/utils/CommonUtils","i18n!DS/ENOProjectMgmtList/assets/nls/ENOProjectMgmtList"],function(e,t,n,o,i,a){"use strict";let s=e.extend({_uwaClassName:"ContextSelectionConfig",columnInfo:{},setup:function(){this.setColumnInfo()},type:"properties",values:[{readWrite:"true",name:"context",attributeToDisplay:"name",imageAttribute:"image",singleObjectOnly:!0,type:"object",typeAheadCallback:t.typeAheadCallback,customView:o,searchSource:n.filterSearchSources(["3dspace"]),typesForSearchFieldName:"typesForSearch",linkCallback:i.launchProperties,placeHolder:a.CONTEXT_SEARCH_TEXT,validateSameTenant:!0,showRemoveMenuItem:!0,excludeSearchTypes:{"3dspace":["Inbox Task","Experiment","Project Snapshot"]}}]});return s._instance=s._instance||new s,s._instance}),define("DS/ENOProjectMgmtList/config/ProjectCreateConfig",["DS/ENOProjectMgmtCommonComponents/config/ConfigBase","DS/ENOProjectMgmtModels/Collections/SearchObjects","DS/ENOProjectMgmtCommonComponents/views/ProjectMgmtObjectCardView","DS/ENOProjectMgmtCommonComponents/views/ProjectMgmtHeatMapCustomView","DS/ENOProjectMgmtCommonComponents/utils/CommonUtils","i18n!DS/ENOProjectMgmtCommonComponents/assets/nls/ENOProjectMgmtCommonComponents","i18n!DS/ENOProjectMgmtList/assets/nls/ENOProjectMgmtList"],function(e,t,n,o,i,a,s){"use strict";let r=e.extend({_uwaClassName:"ProjectCreateConfig",columnInfo:{},setup:function(){this.setColumnInfo()},type:"basic",values:[{label:s.NAME,name:"title",type:"text",readWrite:!0,required:!0,numberOfDisplayLines:1,numberOfEditableLines:1,removeCarriageReturns:!0,maxLength:128,validate:!0},{label:s.TYPE,name:"projectType",readWrite:!0,type:"select",defaultValue:void 0,values:void 0},{label:s.POLICY,name:"policy",readWrite:!0,type:"select"},{name:"calendar",label:s.CALENDAR_CONTEXT,readWrite:!0,attributeToDisplay:"title",imageAttribute:"image",singleObjectOnly:!0,type:"object",typeAheadCallback:t.typeAheadCallback,customView:n,linkCallback:i.launchProperties,placeHolder:s.SEARCH_PLACEHOLDER_TEXT_CALENDAR,validateSameTenant:!0},{label:s.PROJECT_DATE,name:"constraintDate",type:"date",readWrite:!0,defaultValue:new Date,required:!1,validate:!0},{label:s.SCHEDULE_FROM,name:"scheduleFrom",type:"select",readWrite:!0,defaultValue:void 0,values:void 0},{label:s.CONSTRAINT_TYPE,name:"defaultConstraintType",type:"select",readWrite:!0,defaultValue:void 0,values:void 0},{label:s.BASE_CURRENCY,name:"currency",type:"select",readWrite:!0,defaultValue:void 0,values:void 0},{label:s.DESCRIPTION,name:"description",type:"textarea",readWrite:!0,numberOfDisplayLines:2,numberOfEditableLines:2,maxLength:150},{name:"type"},{name:"parentId"}],getDefaultImpactConfig:Function.prototype,getDefaultProbabilityConfig:Function.prototype});return r._instance=r._instance||new r,r._instance}),define("DS/ENOProjectMgmtList/config/ProjectPreferencesConfig",["DS/ENOProjectMgmtCommonComponents/config/ConfigBase","i18n!DS/ENOProjectMgmtList/assets/nls/ENOProjectMgmtList"],function(e,t){"use strict";let n=e.extend({_uwaClassName:"ProjectPreferencesConfig",columnInfo:{},setup:function(){this.setColumnInfo()},type:"basic",values:[{label:t.PERCENT_COMPLETE_BASED_ON,name:"percentCompleteBasedOn",type:"select",readWrite:!0,values:void 0},{label:t.SCHEDULE_BASED_ON,name:"scheduleBasedOn",type:"select",readWrite:!0,values:void 0},{label:t.SCHEDULE,name:"projectSchedule",type:"select",readWrite:!0,values:void 0},{label:t.LAG_CALENDAR,name:"lagCalendar",type:"select",readWrite:!0,values:void 0},{label:t.ENABLE_APPROVAL,name:"enableApproval",type:"select",readWrite:!0,values:void 0},{label:t.RESOURCE_PLAN_MODE,name:"resourcePlanPreference",type:"select",readWrite:!0,values:void 0}],getDefaultImpactConfig:Function.prototype,getDefaultProbabilityConfig:Function.prototype});return n._instance=n._instance||new n,n._instance}),define("DS/ENOProjectMgmtList/config/ExperimentPropertiesConfig",["DS/ENOProjectMgmtCommonComponents/config/ConfigBase","DS/ENOProjectMgmtModels/Collections/SearchObjects","DS/ENOProjectMgmtCommonComponents/views/ProjectMgmtObjectCardView","DS/ENOProjectMgmtCommonComponents/utils/CommonUtils","DS/ENOProjectMgmtCommonComponents/views/ProjectMgmtHeatMapCustomView","i18n!DS/ENOProjectMgmtCommonComponents/assets/nls/ENOProjectMgmtCommonComponents","i18n!DS/ENOProjectMgmtList/assets/nls/ENOProjectMgmtList"],function(e,t,n,o,i,a,s){"use strict";a.MORE;let r=e.extend({_uwaClassName:"ExperimentPropertiesConfig",columnInfo:{},setup:function(){this.setColumnInfo()},type:"basic",values:[{label:s.NAME,name:"title",type:"text",readWrite:"true",required:!0,numberOfDisplayLines:1,numberOfEditableLines:1,removeCarriageReturns:!0,maxLength:128,validate:!0},{label:s.DESCRIPTION,name:"description",type:"textarea",readWrite:"true",numberOfDisplayLines:2,numberOfEditableLines:2,maxLength:150},{label:s.ESTIMATED_DATES,name:"estimatedStartDate",type:"startStop",startDateLabel:s.START,endDateAttribute:"estimatedFinishDate",endDateLabel:s.END,validate:!0},{label:s.SCHEDULE_FROM,name:"scheduleFrom",type:"select",defaultValue:void 0,values:void 0},{label:s.CONSTRAINT_TYPE,name:"defaultConstraintType",type:"select",defaultValue:void 0,values:void 0}],getDefaultImpactConfig:Function.prototype,getDefaultProbabilityConfig:Function.prototype});return r._instance=r._instance||new r,r._instance}),define("DS/ENOProjectMgmtList/config/ProjectAppConfig",["UWA/Core","DS/ENOProjectMgmtCommonComponents/config/AppConfig","i18n!DS/ENOProjectMgmtCommonComponents/assets/nls/ENOProjectMgmtCommonComponents","i18n!DS/ENOProjectMgmtList/assets/nls/ENOProjectMgmtList"],function(e,t,n,o){"use strict";return e.extend(t,{_uwaClassName:"AppConfig",searchResultsMap:{objectId:"id",objectType:"type",displayName:"title","ds6w:status":"state"},supportedTypesAsParamsForContext:["Project Space","Project Concept","Project Baseline"],supportedTypesAsParamsInAppListing:["Project Space","Project Concept","Project Baseline","Experiment"],completeState:"Complete",inWorkState:"Active"})}),define("DS/ENOProjectMgmtList/config/ProjectPropertiesConfig",["UWA/Core","DS/ENOProjectMgmtCommonComponents/config/ConfigBase","DS/ENOProjectMgmtCommonComponents/utils/CommonUtils","DS/ENOProjectMgmtCommonComponents/views/CalendarPopup","DS/ENOProjectMgmtCommonComponents/views/ProjectMgmtObjectCardView","DS/ENOProjectMgmtCommonComponents/views/ProjectMgmtHeatMapCustomView","DS/ENOProjectMgmtModels/Collections/SearchObjects","i18n!DS/ENOProjectMgmtCommonComponents/assets/nls/ENOProjectMgmtCommonComponents","i18n!DS/ENOProjectMgmtList/assets/nls/ENOProjectMgmtList"],function(e,t,n,o,i,a,s,r,l){"use strict";let c=r.MORE,m=t.extend({_uwaClassName:"ProjectPropertiesConfig",columnInfo:{},setup:function(){this.setColumnInfo()},type:"basic",values:[{label:r.TITLE,name:"title",type:"text",readWrite:"true",required:!0,numberOfDisplayLines:1,numberOfEditableLines:1,removeCarriageReturns:!0,maxLength:128,validate:!0},{label:l.NAME,name:"name",type:"text"},{label:l.TYPE,name:"type",type:"text"},{label:l.DESCRIPTION,name:"description",type:"textarea",readWrite:"true",numberOfDisplayLines:2,numberOfEditableLines:2,maxLength:150},{label:l.POLICY,name:"policy",type:"text"},{label:l.ESTIMATED_DATES,name:"estimatedStartDate",type:"startStop",startDateLabel:l.START,endDateAttribute:"estimatedFinishDate",endDateLabel:l.END,validate:!0},{label:l.COLLABORATIVE_SPACE,name:"project",type:"text",group:c},{label:l.ACTUAL_DATES,startDateLabel:l.START,name:"actualStartDate",type:"startStop",endDateAttribute:"actualFinishDate",endDateLabel:l.END,group:c},{label:l.ORIGINATED,name:"originated",type:"date",group:c},{label:l.MODIFIED,name:"modified",type:"date",group:c},{label:l.PROJECT_DATE,name:"constraintDate",type:"date",readWrite:!0,defaultValue:void 0,required:!0,validate:!0},{label:l.SCHEDULE_FROM,name:"scheduleFrom",type:"select",readWrite:!0,defaultValue:void 0,values:void 0},{label:l.CONSTRAINT_TYPE,name:"defaultConstraintType",type:"select",readWrite:!0,defaultValue:void 0,values:void 0},{label:l.BASE_CURRENCY,name:"currency",type:"select",readWrite:!0,defaultValue:"Project Space",values:void 0},{label:r.CALENDAR,name:"calendar",attributeToDisplay:"title",imageAttribute:"image",singleObjectOnly:!0,type:"object",customView:i,linkCallback:n.launchProperties,getDropDownMenuItems:function(t,i){let a=e.clone(i||{},!1);a.handler=function(e){new o(this,{model:e}).render()}.bind(void 0,a.model);let s=this.getDropDownMenuItems(t,i);return s.push(n.getPreviewButton(a)),s}}]});return m._instance=m._instance||new m,m._instance}),define("DS/ENOProjectMgmtList/utils/Constants",["DS/WebappsUtils/WebappsUtils"],function(e){"use strict";let t=e.getWebappsBaseUrl()+"ENOProjectMgmtList/assets/";return{BASE_URL:t,ADD_NODE:"add node",REMOVE:"Remove",ENTER_KEYCODE:13,MOVE_PROJECT:"move project",LATE:t+"icons/late.png",BEHIND_SCHEDULE:t+"icons/behindSchedule.png",COMPLETED_ON_TIME:t+"icons/completedOnTime.png",ALLOWED_IMAGE_FORMATS:["png","jpg","jpeg","gif","tiff","bpg"]}}),define("DS/ENOProjectMgmtList/views/ProjectGanttView",["UWA/Core","UWA/Class/View","DS/UIKIT/Scroller","DS/Foundation/WidgetUwaUtils","DS/ENOProjectMgmtModels/Models/AppSettings","DS/ENOGantt/View/GanttView","text!DS/ENOProjectMgmtList/assets/templates/GanttViewTemplate.html.handlebars","i18n!DS/ENOProjectMgmtCommonComponents/assets/nls/ENOProjectMgmtCommonComponents","css!DS/ENOProjectMgmtCommonComponents/ENOProjectMgmtCommonComponents.css"],function(e,t,n,o,i,a,s,r){"use strict";return t.extend({_uwaClassName:"ProjectGanttView",init:function(e,t){this.parentApp=e,o.setupEnoviaServer(),window.enoviaServer.securityContext=encodeURIComponent(i.get("collabspace")),window.isIFWE=!0},reset:function(){this.ganttView&&(this.ganttView.destroy(),this.ganttView=void 0)},render:function(){return this.reset(),this._appContainer=e.createElement("div"),this._appContainer.setHTML(s),this.ganttViewContent=this._appContainer.getElement("#ganttViewContent"),this.ganttViewContent.inject(this._appContainer.getElement("#ganttViewContainer")),this.ganttViewContent},loadData:function(t){let n=this,o=e.clone(t||{},!1);this._collection=o._parentView._collection,n.ganttView?n.launchGanttView(o):require(["DS/ENOGantt/View/GanttPlugins"],function(e){n.ganttView=new a({container:n.ganttViewContent,ganttPlugins:e}),n.launchGanttView(o)})},launchGanttView:function(e){let t=i.getContextInfo(),n=e.onFailureDataModels;if(n&&delete e.onFailureDataModels,t||n)this._loadGanttView(t);else{let e=this.ganttView.getSavedProjectPhysicalId();e?this.parentView.setContextWithId(e):this._loadGanttView(t)}},_loadGanttView:function(e){this.ganttView.loadGanttView({contextModel:this._collection.getCurrentDataModel(),collabspace:i.get("collabspace"),AppSettings:i,parentView:this.parentApp})},destroy:function(){this.ganttView&&(this.ganttView.destroy(),delete this.ganttView),this._collection&&(this._collection.destroy(),delete this._collection),this._appContainer&&(this._appContainer.destroy(),delete this._appContainer)},getHeight:function(){return this.height=this.height||this.parentApp.getHeight(),this.height},setHeight:function(e){if(this.height=e,this._appContainer){let e=this.ganttViewContent;e&&(e.setStyle("height",this.height+"px"),this.ganttView.mainPanel.setHeight(this.height),this.ganttView.mainPanel.setWidth(this.ganttViewContent.clientWidth))}},show:function(){this._appContainer&&this._appContainer.show()},hide:function(){this._appContainer&&this._appContainer.hide()}})}),define("DS/ENOProjectMgmtList/utils/ProjectCloneModel",["UWA/Core","DS/ENOProjectMgmtModels/Models/FieldsDefinition","DS/ENOProjectMgmtCommonComponents/utils/CommonUtils","DS/ENOProjectMgmtCommonComponents/utils/ProjectMgmtCloneModel"],function(e,t,n,o){"use strict";return o.extend({_uwaClassName:"ProjectCloneModel",init:function(e,t){this._parent.apply(this,arguments)},add:function(t,n,o){var i=e.clone(o||{},!1);i.sourceType="project",this._parent(t,n,i)},dataForRendering:function(){let e=this._parent.apply(this,arguments),t=this.get("actualStartDate"),o=this.get("actualFinishDate"),i=this.get("estimatedStartDate"),a=this.get("estimatedFinishDate"),s=this.get("constraintDate");return e.estimatedStartDate=i&&n.getIntlDateFormat(i),e.estimatedFinishDate=a&&n.getIntlDateFormat(a),e.actualStartDate=t&&n.getIntlDateFormat(t),e.actualFinishDate=o&&n.getIntlDateFormat(o),e.constraintDate=s&&n.getIntlDateFormat(s),e},validate:function(e,t){var n=!0,o={fields:[],messages:{}};let i=this._attributesGet(e,"title"),a=i&&i.trim().length;return n&&!a&&(n=!1,o.fields.push("title")),n=n&&this._validateEstimatedDates(e,o,t),this.dispatchEvent("disableSaveButton",!n),n?void 0:o},_validateEstimatedDates:function(t,n,o){let i=!0,a=e.clone(o||{},!1),s=this._attributesGet(t,"estimatedStartDate"),r=this._attributesGet(t,"estimatedFinishDate");return s=s&&new Date(s.actualValue||s.getTime&&s.getTime()).setHours(0,0,0,0),r=r&&new Date(r.actualValue||r.getTime&&r.getTime()).setHours(0,0,0,0),s&&r&&s>r&&(i=!1,CustomAlert.buildNotification({subtitle:CommonNLS.DATE_ERROR_MSG},"error"),n.fields.push(a.name)),i},set:function(t,n,o){let i;return"scheduleFrom"===t&&(n&&e.is(n,"object")&&(n=n.actualValue),arguments[1]=n,i=this._parent.apply(this,arguments),this.dispatchEvent("onChange:"+t,[n])),"projectType"===t&&(n&&e.is(n,"object")&&(n=n.actualValue),arguments[1]=n,i=this._parent.apply(this,arguments),this.dispatchEvent("onChange:"+t,[n])),i||this._parent.apply(this,arguments)}})}),define("DS/ENOProjectMgmtList/config/ExperimentPreferencesConfig",["DS/ENOProjectMgmtCommonComponents/config/ConfigBase","i18n!DS/ENOProjectMgmtList/assets/nls/ENOProjectMgmtList"],function(e,t){"use strict";let n=e.extend({_uwaClassName:"ExperimentPreferencesConfig",columnInfo:{},setup:function(){this.setColumnInfo()},type:"basic",values:[{label:t.PERCENT_COMPLETE_BASED_ON,name:"percentCompleteBasedOn",type:"select",readWrite:!1,values:void 0},{label:t.SCHEDULE_BASED_ON,name:"scheduleBasedOn",type:"select",readWrite:!1,values:void 0},{label:t.SCHEDULE,name:"projectSchedule",type:"select",readWrite:!1,values:void 0},{label:t.LAG_CALENDAR,name:"lagCalendar",type:"select",readWrite:!1,values:void 0}]});return n._instance=n._instance||new n,n._instance}),define("DS/ENOProjectMgmtList/config/TabConfigForCreate",["DS/ENOProjectMgmtCommonComponents/config/ConfigBase","DS/ENOProjectMgmtList/config/ProjectCreateConfig","i18n!DS/ENOProjectMgmtCommonComponents/assets/nls/ENOProjectMgmtCommonComponents","i18n!DS/ENOProjectMgmtList/assets/nls/ENOProjectMgmtList"],function(e,t,n,o){"use strict";let i=e.extend({_uwaClassName:"TabConfigForCreate",columnInfo:{},setup:function(){this.setColumnInfo()},name:"create",type:"tab",values:[{name:o.PROPERTIES,key:"properties",selected:!0,include:"ownerInfo",font:"doc-text",config:t,checkFlag:!0}],getDefaultTab:function(){for(let e in this.values)if(this.values[e].checkFlag)return this.values[e]}});return i._instance=i._instance||new i,i._instance}),define("DS/ENOProjectMgmtList/config/ProjectListConfig",["UWA/Core","DS/ENOProjectMgmtCommonComponents/config/ConfigBase","DS/ENOProjectMgmtCommonComponents/config/DefaultGridConfig","DS/ENOProjectMgmtList/utils/Constants","DS/ENOProjectMgmtModels/Utils/SearchUtil","DS/ENOProjectMgmtModels/Models/FieldsDefinition","i18n!DS/ENOProjectMgmtCommonComponents/assets/nls/ENOProjectMgmtCommonComponents","i18n!DS/ENOProjectMgmtList/assets/nls/ENOProjectMgmtList","css!DS/ENOProjectMgmtCommonComponents/ENOProjectMgmtCommonComponents.css","css!DS/ENOProjectMgmtList/ENOProjectMgmtList.css"],function(e,t,n,o,i,a,s,r){"use strict";let l=t.extend({_uwaClassName:"ProjectListConfig",columnInfo:{},ignoreCheckBoxSelection:!0,customContentColumnInfo:{},setup:function(){this.setColumnInfo(),this.gridConfig=this.getGridConfig()},type:"list",getGridConfig:function(){let t=e.clone(n);return t.cellDragEnabledFlag=!0,t.identifier="projectListGridView",t.showContextualMenuColumnFlag=!1,t.customViews=[{identifier:"ProjectList View",layoutDensity:"comfortable",columnsVisibleFlag:{typeicon:!1},columnsWidth:{tree:"auto",name:125,description:175,modified:200,ownerInfo:200,state:225},columnsSortModel:[{dataIndex:"modified",sort:"desc"}],isDefault:!0}],t.statusBarOptions=this.getStatusBarOptions(),t},registerReusableCellContent:{id:"programsContent",buildContent:function(){return e.createElement("div",{class:"grid-users-list"})}},getContextFields:function(){return"none,name,title,image,project,thumbnail,typeicon"},getContextIncludeFields:function(){return"none"},values:[{text:r.NAME,fields:"name,title,typeicon,image,modifyAccess",initialFields:"name,image,hasExperimentOrBaseline,modifyAccess",expandFields:"experiments.name,experiments.title,experiments.typeicon,baselines.name,baselines.typeicon",expandInclude:"experiments,baselines",key:"name",dataIndex:"tree",minWidth:"150",icon:""},{text:r.TYPE,fields:"type",initialFields:"type",dataIndex:"type"},{text:r.MATURITY_STATE,fields:"state",initialFields:"state",dataIndex:"state",mapFromRangeValue:!0,getCellClassName:function(e){let t=e.cellView,n=t&&t.elements.container.getElement(".wux-tweakers");if(n&&n.setAttribute("has-badge",!0),e.nodeModel){let t=e.nodeModel.options.data.model;if(t)return"object-maturity "+(t.get("state")?t.get("state").toLowerCase():"")}}},{text:r.OWNER,include:"ownerInfo",fields:"owner,ownerInfo.name,ownerInfo.firstname,ownerInfo.lastname",dataIndex:"ownerInfo",typeRepresentation:"string",getCellValue:function(e){let t=e.nodeModel.options.data.model,n=t&&t.get("ownerInfo");return n&&n.length&&n.length>0?n.at(0).get("fullName"):""},getCellClassName:function(e){if(e.nodeModel)return"object-owner"},getCellSemantics:function(e){let t=e.nodeModel.options.data.model,n=t&&t.get("ownerInfo");return t&&{icon:n&&n&&n.length&&n.length>0?n.at(0).get("image"):"",label:n&&n.length&&n.length>0?n.at(0).get("fullName"):""}}},{text:r.ESTIMATED_FINISH_DATE,fields:"estimatedFinishDate",dataIndex:"estimatedFinishDate",alignmentValue:"center",typeRepresentation:"datetime"},{text:r.PERCENT_COMPLETE,fields:"percentComplete",dataIndex:"percentComplete",typeRepresentation:"progress",width:"150"},{text:r.PROGRAM_NAME,include:"programList",fields:"programList.typeicon,programList.name",dataIndex:"programList",minWidth:"200",typeRepresentation:"string",customContentId:"programsContent",onCellRequest:function(t){var n=this.reuseCellContent("programsContent");n.empty();let o=t.nodeModel.options.data.model,i=o&&o.get("programList"),a=i&&i.length;i&&i.forEach(function(t){let o=t.id,i=e.createElement("div",{class:"grid-user-image"});e.createElement("img",{draggable:"true",class:"grid-user-image-img",src:t.get("typeicon"),title:t.get("name"),"data-id":o}).inject(i),i.inject(n),1===a&&e.createElement("span",{"data-id":o,html:t.get("name"),title:t.get("name"),class:"wux-tweakers-string-label"}).inject(n)}),t.cellView._setReusableContent(n)},singleObjectOnly:!1,typeRepresentation:"string",getCellValue:function(e){},getCellSemantics:function(e){return{allowMultipleValuesFlag:!0}}},{text:r.DESCRIPTION,fields:"description",dataIndex:"description"}],updateImpactProbabiltyConfig:Function.prototype});return l._instance=l._instance||new l,l._instance}),define("DS/ENOProjectMgmtList/views/ProjectDetailsAndRelationships",["UWA/Core","DS/ENOProjectMgmtCommonComponents/utils/Constants","DS/ENOProjectMgmtCommonComponents/utils/CommonUtils","DS/ENOProjectMgmtCommonComponents/components/CommonCalendar","DS/ENOProjectMgmtCommonComponents/views/ProjectMgmtDetailsAndRelationships","DS/ENOProjectMgmtModels/Models/AppSettings","DS/ENOProjectMgmtModels/Models/FieldsDefinition","DS/ENOProjectMgmtList/config/ExperimentPropertiesConfig","i18n!DS/ENOProjectMgmtList/assets/nls/ENOProjectMgmtList"],function(e,t,n,o,i,a,s,r,l){"use strict";return i.extend({_uwaClassName:"ProjectDetailsAndRelationships",getCustomFields:function(){let e=this._parent.apply(this,arguments);return e.push("projectType"),e},render:function(){let e=this._parent.apply(this,arguments),t=this.detailsContainer.getElement(".object-properties-list");return t&&t.remove(),e},showDetailsDisplay:function(e){if(!(this.currentModel&&this.currentModel.isNew)){this.currentModel=this._collection.getCurrentDataModel();let t,n=this.currentModel.get("type");switch(this.currentTab.key){case"properties":t=this._tabConfig.getPropertiesConfigForType(n);break;case"preferences":t=this._tabConfig.getPreferencesConfigForType(n,e);break;default:t=e.config}e.config=t}this._parent(e),"properties"===this.currentTab.key&&(this.fetchProjectPolicyDetails(this.currentModel.model.get("projectType")||this.currentModel.model.get("type")),this.currentModelEvents={"onChange:projectType":this.fetchProjectPolicyDetails,"onChange:scheduleFrom":this.fetchProjectConstaintDetails},this.listenTo(this.currentModel,this.currentModelEvents))},fetchProjectPolicyDetails:function(e){let t=this.relatedView.properties._config,o=n.findIndex(t.values,"name","policy");var i=s.getFieldDefinitionColumn("projects","policy"),a=[];i.range.item.forEach(t=>{var n=t.helpinfo;e==n&&a.push(t)});let r=n.formatRangeValuesForSelectView(a,"value","display",void 0);o>0&&(t.values[o].values=r,t.values[o].defaultValues=r,t.values[o].defaultValue=r[0]?r[0].displayValue:"",this.currentModel.set("policy",t.values[o].defaultValue),this.currentModel.dispatchEvent("resetDetailsDisplay"))},fetchProjectConstaintDetails:function(e){if("Project Finish Date"==e||"Project Start Date"==e){let o=this._propertiesTab._config,i=n.findIndex(o.values,"name","defaultConstraintType");var t="";o.values[i].values.forEach(n=>{"Project Finish Date"==e&&"As Late As Possible"==n.actualValue&&(t=n.displayValue),"Project Start Date"==e&&"As Soon As Possible"==n.actualValue&&(t=n.displayValue)}),this.currentModel.set("defaultConstraintType",t),o.values[i].defaultValue=t}this.currentModel.dispatchEvent("resetDetailsDisplay")},_validateActionButtons:function(){let e=this;if(!this.currentModel)return;let o=this.currentModel.model.getActionsAttrsAccessMap(),i=this.currentModel.model.type,a=this.currentModel.get("ownerInfo").at(0),s=n.isLoggedInUser(a.get("name")),r=o.canDelete;this._actionButtonConfig.values.forEach(function(n){let o=e.actionButtons[n.key];if(n.key===t.DELETE){let e=l.NO_DELETE_ACCESS_MSG;"Experiment"!==i&&"Project Baseline"!==i||(e=l.EXPERIMENT_NO_DELETE_ACCESS_MSG),o.disabled=Boolean(!r),o.visibleFlag=s,!r&&(o.tooltipInfos.shortHelp=e)}})},save:function(t){let n=e.clone(t||{},!1);n.doNotUpdateTaggerData=!0,this._parent(n)},setContextWithId:function(e,t){let n={homeView:e,detailedObjects:[{id:t}],config:e._contextSelectionConfig.values[0]};e.contextScope.add("context",t,n)},getCustomView:function(e,t){if("calendar"===t)return new o(e,{model:this.currentModel})},handleDeleteOnComplete:function(e){this._parent.apply(this,arguments),this.parentApp.showHideLeftPanel(!0)},buildTabBarContainer:function(){let e=a.get("context");if(e){let t=e.type;this._tabConfig.values=this._tabConfig.getValues("Experiment"===t||"Project Baseline"===t)}this._parent.apply(this,arguments)}})}),define("DS/ENOProjectMgmtList/config/ActionButtonConfig",["DS/ENOProjectMgmtCommonComponents/utils/Constants","DS/ENOProjectMgmtCommonComponents/config/ConfigBase","DS/ENOProjectMgmtList/config/ProjectAppConfig","i18n!DS/ENOProjectMgmtCommonComponents/assets/nls/ENOProjectMgmtCommonComponents"],function(e,t,n,o){"use strict";let i=t.extend({_uwaClassName:"ActionButtonConfig",type:"actionButtons",columnInfo:{},setup:function(){this.setColumnInfo()},values:[{name:o.DELETE,key:"delete",selected:!1,showOnCreate:!1,font:"trash",checkFlag:!1}]});return i._instance=i._instance||new i,i._instance}),define("DS/ENOProjectMgmtList/config/TabConfig",["DS/ENOProjectMgmtCommonComponents/config/ConfigBase","DS/ENOProjectMgmtList/config/ProjectPropertiesConfig","DS/ENOProjectMgmtList/config/ProjectPreferencesConfig","DS/ENOProjectMgmtCommonComponents/config/ClassificationConfig","DS/ENOProjectMgmtList/config/ActionButtonConfig","DS/ENOProjectMgmtList/config/ExperimentPropertiesConfig","DS/ENOProjectMgmtList/config/ExperimentPreferencesConfig","i18n!DS/ENOProjectMgmtCommonComponents/assets/nls/ENOProjectMgmtCommonComponents","i18n!DS/ENOProjectMgmtList/assets/nls/ENOProjectMgmtList"],function(e,t,n,o,i,a,s,r,l){"use strict";let c=e.extend({_uwaClassName:"TabConfig",columnInfo:{},setup:function(){this.setColumnInfo()},name:"edit",type:"tab",actionButtonConfig:i,values:[{name:l.PROPERTIES,key:"properties",selected:!0,fields:"title,name,typeicon,image,owner,canDelete,modifyAccess,promoteAccess,demoteAccess,fromConnectAccess,toConnectAccess,description,policy,constraintDate,estimatedStartDate,estimatedFinishDate,state,actualStartDate,actualFinishDate,originated,modified,project,scheduleFrom,defaultConstraintType,percentComplete,ownerInfo.name,ownerInfo.firstname,ownerInfo.lastname,estimatedFinishDate,calendar.title,calendar.image",include:"none,ownerInfo,calendar",font:"doc-text",checkFlag:!0},{name:r.PREFERENCES,key:"preferences",fields:"scheduleBasedOn,percentCompleteBasedOn,projectSchedule,lagCalendar,enableApproval,resourcePlanPreference",font:"tools",config:n,checkFlag:!0},{name:r.CLASSIFICATION,key:"classification",selected:!1,font:"library-class",config:o,checkFlag:!1},{name:r.RELATIONS_LABEL,key:"relations",selected:!1,showOnCreate:!1,font:"object-related",checkFlag:!1},{name:r.COMMENT,key:"comments",selected:!1,showOnCreate:!1,font:"comment",checkFlag:!1}],getDefaultTab:function(){for(let e in this.values)if(this.values[e].checkFlag)return this.values[e]},getValues:function(e){let t=this.__proto__.values;return e?t.filter(e=>"classification"!==e.key&&"relations"!==e.key):t},getPreferencesConfigForType:function(e,t){return"Experiment"===e||"Project Baseline"===e?s:n},getPropertiesConfigForType:function(e){return"Experiment"===e||"Project Baseline"===e?a:t}});return c._instance=c._instance||new c,c._instance}),define("DS/ENOProjectMgmtList/collection/ProjectCollection",["DS/ENOProjectMgmtCommonComponents/collection/ProjectMgmtViewCollection","DS/ENOProjectMgmtModels/Collections/Projects","DS/ENOProjectMgmtModels/Models/Project","DS/ENOProjectMgmtList/config/ProjectAppConfig","DS/ENOProjectMgmtList/config/ProjectPropertiesConfig","DS/ENOProjectMgmtList/config/TabConfig","DS/ENOProjectMgmtList/config/TabConfigForCreate","DS/ENOProjectMgmtList/config/ProjectListConfig","DS/ENOProjectMgmtList/utils/ProjectCloneModel","DS/ENOProjectMgmtModels/Constants/Constants","i18n!DS/ENOProjectMgmtList/assets/nls/ENOProjectMgmtList"],function(e,t,n,o,i,a,s,r,l,c,m){"use strict";return e.singleton({_uwaClassName:"ProjectCollection",setup:function(){let e={};e.DataModels=t,e.DataModel=n,e.AppConfig=o,e.PropertiesConfig=i,e.TabConfig=a,e.TabConfigForCreate=s,e.ListConfig=r,e.CloneModel=l,e.governingType=c.TYPENAMES.PROJECT,e.governingTypeNLS=m.PROJECT,e.NLS=m,this._parent(e)},destroy:function(){this._parent()}})}),define("DS/ENOProjectMgmtList/config/ViewConfig",["DS/ENOProjectMgmtCommonComponents/config/ViewConfigBase","DS/ENOProjectMgmtModels/Constants/Constants","DS/ENOProjectMgmtModels/Models/FieldsDefinition","DS/ENOProjectMgmtList/config/ProjectCreateConfig","DS/ENOProjectMgmtList/config/ProjectPropertiesConfig","DS/ENOProjectMgmtList/config/ProjectPreferencesConfig"],function(e,t,n,o,i,a){"use strict";let s=e.extend({_uwaClassName:"ViewConfig",setup:function(){this._parent(t.APPLICATIONS.PROJECT),n.setFieldTaggerMap()},_addRangeValuesToConfigs:function(e,t,n){this._parent.call(this,e,t,n,[o,i,a])}});return s._instance=s._instance||new s,s._instance}),define("DS/ENOProjectMgmtList/config/HomeTabConfig",["DS/ENOProjectMgmtCommonComponents/utils/CommonUtils","DS/ENOProjectMgmtCommonComponents/config/ConfigBase","DS/ENOProjectMgmtCommonComponents/views/MassUpdateView","DS/ENOProjectMgmtCommonComponents/views/ProjectMgmtGridView","DS/ENOProjectMgmtModels/Models/AppSettings","DS/ENOProjectMgmtList/views/ProjectGanttView","DS/ENOProjectMgmtList/views/ProjectDetailsAndRelationships","DS/ENOProjectMgmtList/config/TabConfig","DS/ENOProjectMgmtList/config/ProjectAppConfig","DS/ENOProjectMgmtList/config/ProjectListConfig","DS/ENOProjectMgmtList/config/TabConfigForCreate","DS/ENOProjectMgmtList/collection/ProjectCollection","i18n!DS/ENOProjectMgmtCommonComponents/assets/nls/ENOProjectMgmtCommonComponents","i18n!DS/ENOProjectMgmtList/assets/nls/ENOProjectMgmtList"],function(e,t,n,o,i,a,s,r,l,c,m,d,g,p){"use strict";let u=t.extend({_uwaClassName:"HomeTabConfig",columnInfo:{},skeletonOptions:{rightContainerContent:void 0,centerContainerContent:void 0,leftContainerCollapsedFlag:!1,rightContainerCollapsedFlag:!0,leftContainerWidth:0,leftContainerMinWidth:0,leftContainerMaxWidth:0,rightContainerWidth:400,rightContainerMinWidth:200,rightContainerMaxWidth:500,navigationContainerVisibleFlag:!1,idCardContainerVisibleFlag:!0,statusContainerVisibleFlag:!1,identifier:"SkeletonLayout_projectMgmentRiskHomeView"},leftPanel:{type:"tab",toolbarConfig:{group1:[{name:"gridView",label:g.GRID_VIEW,type:"tab",bodyContainer:".ds-app-panel-body",selected:!0,checkFlag:!0,content:o,options:{leftPanelOnly:!0,tabConfig:r,appConfig:l,nlsTrend:p.PROJECT_TREND,collection:d,listConfig:c,doNotShowContextualMenu:!0,tabConfigForCreate:m,detailsAndRelationships:s},include:!0,font:"view-list",tabViewToolbarConfig:{createMenuItems:[{name:"createProject",value:"createProject",text:p.PROJECT,label:p.PROJECT,fonticon:"plus"},{name:"createExperiment",value:"createExperiment",text:p.EXPERIMENT,label:p.EXPERIMENT,fonticon:"plus"},{name:"createBaseline",value:"createBaseline",text:p.BASELINE,label:p.BASELINE,fonticon:"plus"}],group2:[{name:"projectCreateButtonContainer",type:"button",displayStyle:"lite",domId:"createNewButton",emphasize:"secondary",iconName:"plus",fontIconSize:"1x",shortHelp:g.CREATE_NEW_LABEL,separator:!0,buttonClick:{functionName:"createDropdownMenu"}},{name:"projectMaturityContainer",type:"button",displayStyle:"lite",domId:"maturityButton",emphasize:"secondary",iconName:"collaborative-lifecycle-management",fontIconSize:"1x",shortHelp:g.MATURITY,customViewKey:"massUpdateView",buttonClick:{functionName:"handleMassUpdateButtonEvents",params:"maturity",validateModificationsPending:!0}},{name:"projectRelationsContainer",type:"button",displayStyle:"lite",domId:"relationsButton",emphasize:"secondary",iconName:"object-related",fontIconSize:"1x",shortHelp:g.RELATIONS_LABEL,separator:!0,customViewKey:"massUpdateView",buttonClick:{functionName:"handleMassUpdateButtonEvents",params:"relations",validateModificationsPending:!0}}]}}]},defaultTab:"gridView"},mainPanel:{type:"facet",toolbarConfig:{group1:[{name:"ganttView",label:p.GANTT_VIEW,type:"tab",bodyContainer:".ds-app-content.ds-app-facet",selected:!0,checkFlag:!0,content:a,options:{collection:d,listConfig:c,appConfig:l,tabConfig:r,tabConfigForCreate:m,nlsTrend:p.PROJECT_TREND,detailsAndRelationships:s},include:!0,font:"view-tag-cloud"}]},options:{collection:d,listConfig:c,appConfig:l,tabConfig:r,tabConfigForCreate:m,detailsAndRelationships:s},defaultTab:"ganttView",noRenderOnTabClickEvent:!1},getCreateMenuItems:function(e){let t,n,o,a=[],s=this.leftPanel.toolbarConfig.group1[0].tabViewToolbarConfig.createMenuItems,r=e&&"TRUE"===e.get("modifyAccess")&&-1===["Review","Complete","Archive","Cancel"].indexOf(e.get("state")),l=i.get("collabspace"),c=l&&l.substring(l.lastIndexOf(":")+1,l.indexOf(".")),m="VPLMProjectLeader"===c||"3DSRestrictedLeader"===c;return s.forEach(i=>{switch(i.name){case"createProject":n=!m,o=p.NO_CREATE_ACCESS_TT_MSG;break;case"createExperiment":case"createBaseline":n=!m||!e||!r,o=p.NO_CREATE_EXPERIMENT_ACCESS_TT_MSG}(t=UWA.clone(i||{},!1)).className=n?"disabled":"",t.description=n?o:i.text,a.push(t)}),a}});return u._instance=u._instance||new u,u._instance}),define("DS/ENOProjectMgmtList/views/ProjectHomeView",["DS/ENOProjectMgmtCommonComponents/utils/CommonUtils","DS/ENOProjectMgmtCommonComponents/views/HomeViewTabBase","DS/ENOProjectMgmtCommonComponents/views/ProjectMgmtHomeView","DS/ENOProjectMgmtModels/Models/Project","DS/ENOProjectMgmtModels/Models/AppSettings","DS/ENOProjectMgmtModels/Models/FieldsDefinition","DS/ENOProjectMgmtModels/Constants/Constants","DS/ENOProjectMgmtList/config/ViewConfig","DS/ENOProjectMgmtList/config/HomeTabConfig","DS/ENOProjectMgmtList/config/ProjectAppConfig","DS/ENOProjectMgmtList/config/TabConfigForCreate","DS/ENOProjectMgmtList/collection/ProjectCollection","DS/ENOProjectMgmtList/config/ContextSelectionConfig","DS/ENOProjectMgmtList/views/ProjectDetailsAndRelationships","DS/UIKIT/DropdownMenu","DS/ENOProjectMgmtCommonComponents/components/CustomAlert","i18n!DS/ENOProjectMgmtList/assets/nls/ENOProjectMgmtList","css!DS/ENOProjectMgmtList/ENOProjectMgmtList.css"],function(e,t,n,o,i,a,s,r,l,c,m,d,g,p,u,C,f){"use strict";let E=["Experiment","Project Baseline"],h=["Create","Assign","Active"],P=n.extend({_uwaClassName:"ProjectHomeView",init:function(e){let t=UWA.clone(e||{},!1);UWA.extend(t,{nls:f,initialCall:!0,viewConfig:r,baseContextModel:o,appConfig:c,homeTabConfig:l,collection:d,tabConfigForCreate:m,container:t.container||t,contextSelectionConfig:g,detailsAndRelationships:p,contextSourceInfo:{fetchSource:s.APPLICATIONS.PROJECT,fieldDefinitionSource:s.APPLICATIONS.PROJECT}}),this._parent(t)},render:function(e){let t=UWA.clone(e||{},!1);UWA.extend(t,{hideSettings:!0,hideMaturity:!0,hide6WTagger:!0,ignoreMatrixConfiguration:!0,processPostCompleteDataModels:!0}),this._parent(t)},handleContextWithDroppedObj:function(e){this._paramData=e,this.render()},setContextWithId:function(e){let t={homeView:this,detailedObjects:[{id:e}],config:this._contextSelectionConfig.values[0]};this.contextScope.add("context",e,t)},onFailureDataModels:function(e){e.onFailureDataModels=!0,this._parent(e)},handleRemoveContext:function(e){let t=this.homeBodyView.ganttView;e&&!e.onFailureDataModels&&(t&&t.ganttView.removeProjectPhysicalId(),e&&delete e.onFailureDataModels),this._parent.apply(this,arguments)},setHeight:function(){let e=i.getContextInfo()?this._homeTabConfig.mainPanel:this._homeTabConfig.leftPanel;this._parent(e)},fetchAdditionalDataForModels:function(e){let t=i.get("context"),n=UWA.clone(e||{},!1);t&&(n.paramData=[t]),this._parent(n)},createDropdownMenu:function(){let e=this;this.projectCreateButtonContainer=this.getToolbarButton("projectCreateButtonContainer");let t=l.getCreateMenuItems(this.getValidSelectionModel());this.dropdownMenu&&this.dropdownMenu.destroy(),e.projectCreateButtonContainer&&(this.dropdownMenu=new u({target:e.projectCreateButtonContainer.getContent(),items:t,events:{onClick:function(t,n){n.name;switch(n.name){case"createProject":e.createNew();break;case"createExperiment":case"createBaseline":e.createExperimentOrBaseLine("createExperiment"===n.name)}}}}),this.dropdownMenu.show())},getValidSelectionModel:function(){let e=this._collection.treeDocument.getSelectedNodes(),t=1===(e&&e.length)?e[0].model:void 0;return Boolean(t&&-1!==h.indexOf(t.get("state"))&&-1===E.indexOf(t.get("type")))?t:void 0},createExperimentOrBaseLine:function(e){let t=this.getValidSelectionModel();if(!t)return void C.buildNotification({message:f.EXPERIMENT_CREATE_INCORRECT_SELECTION},"error");C.buildNotification({message:e?f.EXPERIMENT_CREATE_MESSAGE:f.BASELINE_CREATE_MESSAGE},"info");let n=this._collection.getNewDataModel({config:m.values[0]});n.set({parentId:t.id,title:t.title,type:e?s.TYPENAMES.EXPERIMENT:s.TYPENAMES.PROJECTBASELINE});let o=this._collection.getDocument().getTreeNodeModelById(t.id),i=t.get(e?"experiments":"baselines");!i&&(i=this._collection.DataModels()),this._collection.saveDataModel(n,{isNew:!0,isExpand:!o.isExpanded(),collection:i,treeDocument:o})},updateStateOfActionsButtons:Function.prototype});return t.prototype.updateStateOfActionsButtons=P.prototype.updateStateOfActionsButtons,P}),define("DS/ENOProjectMgmtList/components/ProjectInitializer",["DS/ENOProjectMgmtList/views/ProjectHomeView","DS/ENOProjectMgmtCommonComponents/components/CommonInitializer"],function(e,t){"use strict";return t.extend({init:function(t){let n=t||{};n.container||(n.container=document.querySelector("#ProjectListAppMgmtView")),n.homeView=e,this._parent(n)}})});