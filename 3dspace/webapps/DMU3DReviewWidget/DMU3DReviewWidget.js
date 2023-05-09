/*!  Copyright 2015 Dassault Systemes. All rights reserved. */
define("DS/DMU3DReviewWidget/DMU3DReviewWidgetOptions",["UWA/Core","UWA/Class","UWA/Class/Options","UWA/Utils"],function(e,t,i,n){"use strict";return t.singleton(i,{init:function(){var t={};""!==window.location.search&&(e.extend(t,n.parseQuery(window.location.search)),t.widgetDomain&&e.extend(t,n.parseQuery(t.widgetDomain))),("debug_me"in t||window.debug)&&(t.debug=!0),("testWkb"in t||window.testWkb||window.widget&&window.widget.data&&window.widget.data.moduleUrl&&window.widget.data.moduleUrl.includes("testWkb"))&&(t.testWkb=!0),("resultFTAUI"in t||window.resultFTAUI)&&(t.resultFTAUI=!0),("pdfReview"in t||window.pdfReview)&&(t.pdfReview=!0),this.setOptions(t)}})}),
/*!  Copyright 2015 Dassault Systemes. All rights reserved. */
define("DS/DMU3DReviewWidget/DMU3DReviewAppInit",["DS/WAfrContainer/App","DS/DMUBaseExperience/DMUContextManager","DS/DMUMeasurable/DMUToolsSceneGraph"],function(e,t,i){"use strict";var n,o,a;return e.extend({setUp:function(e){var t=this;e.sourceApp?e.sourceApp.pad3DViewer?(a=e.sourceApp.pad3DViewer,this.pad3DViewer=e.sourceApp.pad3DViewer,this.appMainDiv=e.sourceApp.appMainDiv,new Promise(function(t){n?t():require(["DS/DMU3DReviewWidget/DMU3DReviewWidgetMain"],function(i){n=new i({widget:window.widget,pad3DViewer:e.sourceApp.pad3DViewer,appMainDiv:e.sourceApp.appMainDiv}),t()})}).then(function(){e.sourceApp&&!e.sourceApp.isDisposeCalled()&&e.sourceApp.dispose();var i=e.done;e.done=function(n){var a;t.appMainDiv||(t.appMainDiv=n.appMainDiv),e.done=i,"ENX3DIR_AP"===e.sourceApp.options.id&&((a=t).switchApp&&require(["DS/CATWebUXComponents/CATWebUXInfoFlag","DS/CATWebUXSlideShow/CATWebUXSlideShowController","i18n!DS/DMU3DReviewWidget/assets/nls/DMU3DReviewWidget"],function(e,t,i){var n=new e({frameWindow:a.pad3DViewer.getFrameWindow(),viewer:a.pad3DViewer.getViewer(),dockingSide:"left"});n.display({label:i.backToIssue,style:"webUXStyle",fontIcon:"wux-ui-3ds wux-ui-3ds-1x wux-ui-3ds-move-to-start",onClickCB:function(){o(),o=null,a.switchApp("ENX3DIR_AP")}});var r,s=t.getSlideShowController({context:a.pad3DViewer.getFrameWindow(),id:"DMU3DMarkupSlideShow"});s&&(r=s.subscribe("onActiveStateModified",function(e){n.setVisibleFlag(!e.state)})),o=function(){n.clean();var e=t.giveSlideShowController({context:a.pad3DViewer.getFrameWindow(),id:"DMU3DMarkupSlideShow"});e&&e.unsubscribe(r)}})),e.done()},n.setUp(e)})):e.done():require(["DS/DMU3DReviewWidget/DMU3DReviewWidgetMain"],function(i){n=new i({widget:window.widget,viewerAttachment:e.done,done:function(e){a=e.pad3DViewer,t.pad3DViewer=e.pad3DViewer,t.appMainDiv=e.appMainDiv}})})},dispose:function(e){var r=t.getReviewContext({context:this.pad3DViewer});if(e&&r)e.preferredType="Markup";else if(e&&this.pad3DViewer.getApplicativeData().getLoadedItfData&&this.pad3DViewer.getApplicativeData().getLoadedItfData().data.length)e.preferredType="SIMItfSimulation";else if(e){var s=i.getRoots(a);let t=1===s.length?i.getNodeType(s[0]):null;"Requirement"!==t&&"Requirement Specification"!==t&&"DesignSight"!==t||(e.preferredType=t)}o&&o(),o=null,n&&n.dispose&&n.dispose(e),this._parent(e)},onWillEnter:function(e){return n&&n.onWillEnter&&n.onWillEnter(e),require(["DS/DMUBaseExperience/EXPUtils","DS/DMU3DReviewController/DMU3DReviewController"],function(t,i){var n=i.get3DReviewController({context:e.pad3DViewer});("string"==typeof e&&"X3DPLAW_AP"===e||e.options&&"X3DPLAW_AP"===e.options.id)&&n.setappTransition(!0),t._sendUsageProbe("appTransitionFrom",e.options.id,"resolved")}),this._parent(e)},onWillLeave:function(e){var o=i.getRoots(a),r=["Document","DIFLayout","DIFSheet"];["X3DPLAW_AP","ENOR3R_AP","ENOR3V_AP","ENOR3D_AP"].indexOf(e.options.id)<0&&r.push("DesignSight");for(var s=0;s<o.length;s++)if(-1!==r.indexOf(i.getNodeType(o[s]))){var l=i.getNodeID(o[s]);t.getReviewContext({context:this.pad3DViewer})&&t.getReviewContext({context:this.pad3DViewer}).getCurrentSessionMarkup()||a.removeRoots({pids:[l]})}return require(["DS/CATWebUXPreferences/CATWebUXPreferencesManager"],function(i){var n=i.getPreferenceManager({context:a.getFrameWindow()});if(n&&("CATC3D_AP"===e.options.id||"CATA3I_AP"===e.options.id||"ENOI3D_AP"===e.options.id)){n.set2DSelectionPreferencesDisplayFlag(!1);let i=t.getReviewContext({context:a});"CATA3I_AP"===e.options.id&&i&&i.getCurrentSessionMarkup()&&i.getCurrentSessionMarkup().setActiveFlag(!1)}}),n&&n.onWillLeave&&n.onWillLeave(e),require(["DS/DMUBaseExperience/EXPUtils"],function(t){t._sendUsageProbe("appTransitionTo",e.options.id,"resolved")}),this._parent(e)}})}),define("DS/DMU3DReviewWidget/DMU3DReviewWidgetDefaultCmd",["DS/ApplicationFrame/Command","DS/DMU3DReviewController/DMU3DReviewController","DS/PADUtils/PADContext"],function(e,t,i){"use strict";return e.extend({init:function(e){var n=e||{};this._parent(n,{mode:"exclusive",isAsynchronous:!0}),this.setRepeatMode(!1),this.beginExecute=function(){var e=t.give3DReviewController({context:i.get()});e&&e.enableDMUContextualBar()},this.endExecute=function(){var e=t.give3DReviewController({context:i.get()});e&&e.disableDMUContextualBar()}}})}),define("DS/DMU3DReviewWidget/DMU3DReviewWidgetMain",["UWA/Core","DS/DMUControls/EXPNotify","DS/DMU3DReviewWidget/DMU3DReviewWidgetOptions","DS/PADUtils/PADSettingsMgt","DS/DMUMeasurable/DMUToolsSceneGraph","DS/ApplicationFrame/CommandsManager","DS/PADServices/views/LandingPage","DS/PADServices/utils/GlobalModelEvents","DS/EditPropWidget/constants/ConstantsMapping","DS/DMUReadPersistence/DMUWebServicesUtils","i18n!DS/DMU3DReviewWidget/assets/nls/DMU3DReviewWidget"],function(e,t,i,n,o,a,r,s,l,d,c){"use strict";let p;const u=[{key:"mkp",type:"Markup"},{key:"mku",type:"DMUMarkupRepReference"},{key:"isr",type:"SIMItfSimulation"},{key:"itf",type:"PLMPIMMetricReference"},{key:"difLayout",type:"DIFLayout"},{key:"difSheet",type:"DIFSheet"},{key:"difAnnotSet",type:"DIFAnnotationSet"},{key:"doc",type:"Document"},{key:"req",type:"Requirement"},{key:"reqSpec",type:"Requirement Specification"},{key:"msr",type:"DesignSight"},{key:"pointClouds",type:"R2V_FeatureState"}],w=["ds6w:label","ds6w:type","ds6wg:revision","ds6w:status","owner","ds6w:responsible","ds6w:modified","ds6w:project","ds6w:identifier"];function m(){var e=this.params.widget.getValue("xsceneStoredObjects");return e&&"string"==typeof e||(e='{"products":{},"filters":{}}'),JSON.parse(e)}function D(e){e&&e.length&&(this.showWelcomePanel(!1),this.pad3DViewer.addRootNodesFromContent({json3DXContent:{protocol:"3DXContent",version:"1.1",source:"X3DSEAR_AP",data:{items:e.map(function(e){return{envId:require("DS/PADUtils/PADUtilsServices").getPlatformId(),objectId:e.id,objectType:e.type,displayName:e.displayName}})}}}))}async function g(e){try{let t=await d.getParentTypes(e);return[e].concat(t)}catch{return[e]}}async function v(e){let i=!0;if(e.length>1)for(const t of e){let e=await g(t["ds6w:type"]);if(!e.includes("VPMReference")&&!e.includes("R2V_FeatureState")){i=!1;break}}var n,o,a;i?D.call(this,e.map(function(e){return{id:e.resourceid,type:e["ds6w:type_value"],displayName:e["ds6w:label"]}})):(n="warning",o=c.dropMultiNoAutorized,a=this.pad3DViewer,p&&(p.destroy(),p=null),p=new t({body:a.getFrameWindow().getUIFrame(),type:n,message:o}))}function f(e){if(e&&e.length){s.publish({event:"ENXViews/LandingPage/addOrRefresh",data:{objects:e.map(function(e){return{resourceid:e,openWithFilter:!1,loading:!0,fromCustom:!0}})}});var t=setInterval(function(){this.isAppReady&&this.welcomePanel&&(clearInterval(t),function(e,t){require("DS/WAFData/WAFData").authenticatedRequest(require("DS/PADUtils/PADUtilsServices").get3DSpaceUrl()+"/cvservlet/fetch/v2?tenant="+require("DS/PADUtils/PADUtilsServices").getPlatformId(),{method:"POST",headers:{Accept:"application/json","Content-Type":"application/json",SecurityContext:require("DS/PADUtils/PADSettingsMgt").getSetting("pad_security_ctx")},data:JSON.stringify({physicalid:e,label:"M3DFetchForWelcomePanel"+Date.now(),select_predicate:w,select_file:["icon","thumbnail_2d"]}),onComplete:t,onFailure:function(){},onTimeout:function(){}})}(e,function(e){if(this.welcomePanel){var t=((e?JSON.parse(e):{}).results||[]).map(function(e){var t={},i=require("DS/ENOPAD3DViewer/utils/PAD3DViewerModelServices"),n=i.getRoots(this.pad3DViewer).map(function(e){return i.getNodeID(e)}),o=require("DS/DMUBaseExperience/DMUContextManager").getReviewContext({context:this.pad3DViewer}),a=o?o.getReviewCtxInformation():{};a&&a.reviewId&&n.push(a.reviewId),this.pad3DViewer.getApplicativeData().getLoadedItfData&&this.pad3DViewer.getApplicativeData().getLoadedItfData().data.forEach(function(e){n=n.concat(e.metricID.length?e.metricID:[e.isrID])}),e.attributes.forEach(function(e){t[e.name]=e.value});var r=t["ds6w:type"],s={resourceid:t.resourceid,label:t["ds6w:label"],icons:[t.type_icon_url],thumbnail:t.preview_url,grid:t,subLabel:t["ds6w:responsible"]+" | "+new Date(t["ds6w:modified"]).toLocaleString(),description:t["ds6w:identifier"],openWithFilter:!1,fromCustom:!0,loading:void 0,opened:n.indexOf(t.resourceid)>-1,item2Load:{}};return s.item2Load[s.resourceid]={},"SIMItfSimulation"!==r&&"Markup"!==r&&"DMUMarkupRepReference"!==r&&"PLMPIMMetricReference"!==r&&"DIFAnnotationSet"!==r||(s.addRemoveFromViewCtx=!0),s}.bind(this));s.publish({event:"ENXViews/LandingPage/addOrRefresh",data:{objects:t}})}}.bind(this)))}.bind(this),100)}}function h(e){this.isAppReady=!1,this.params=e,this.welcomePanel=this.pad3DViewer=null,this.tokensWelcomePanel=[],this.tokensPAD=[],this.tokensMkp=[],this.tokensWelcomePanel.push(s.subscribe({event:"ENXViews/LandingPage/refreshCustom"},f.bind(this))),this.tokensWelcomePanel.push(s.subscribe({event:"ENXViews/LandingPage/openCustom"},function(e){var t=m.call(this),i=[];e.customitem2open.forEach(function(e){u.some(function(n){if(t[n.key]&&t[n.key][e])return i.push({id:e,type:n.type}),!0})}),D.call(this,i)}.bind(this))),this.tokensWelcomePanel.push(s.subscribe({event:"ENXViews/LandingPage/removeFromView"},function(e){if(e&&e.customitem2remove&&e.customitem2remove.length){var t=require("DS/ENOPAD3DViewer/utils/PAD3DViewerModelServices"),i=t.getRoots(this.pad3DViewer).map(function(e){return t.getNodeID(e)}),n=require("DS/DMUBaseExperience/DMUContextManager").getReviewContext({context:this.pad3DViewer}),o=n?n.getReviewCtxInformation():{},a=o?o.reviewId:null,r=[];this.pad3DViewer.getApplicativeData().getLoadedItfData&&(r=this.pad3DViewer.getApplicativeData().getLoadedItfData().data),this.pad3DViewer.getApplicativeData().removeDIFAnnotSetKeepPrd&&e.customitem2remove.forEach(e=>{this.pad3DViewer.getApplicativeData().removeDIFAnnotSetKeepPrd(e)}),e.customitem2remove.indexOf(a)>-1&&require("DS/DMUBaseExperience/DMUContextManager").removeReviewContext({context:this.pad3DViewer}),r.forEach(function(t){(t.metricID.length?t.metricID:[t.isrID]).some(function(i){if(e.customitem2remove.indexOf(i)>-1)return this.pad3DViewer.getApplicativeData().removeIsrKeepContext(t.isrID),!0},this)},this),i.forEach(function(t){e.customitem2remove.indexOf(t)>-1&&this.pad3DViewer.removeRoots({pids:[t]})},this)}}.bind(this))),this.tokensWelcomePanel.push(s.subscribe({event:"ENXViews/LandingPage/removeFromStore"},function(e){if(e&&e.customitem2remove&&e.customitem2remove.length){var t=m.call(this),i=[];e.customitem2remove.forEach(function(e){u.some(function(n){if(t[n.key]&&t[n.key][e]&&-1===i.indexOf(e))return i.push(e),delete t[n.key][e],!0})}),this.params.widget.setValue("xsceneStoredObjects",JSON.stringify(t)),s.publish({event:"ENXViews/LandingPage/removeFromView",data:{customitem2remove:i}})}}.bind(this))),this.tokensWelcomePanel.push(s.subscribe({event:"ENXViews/WelcomePanel/action/welcomePanelMkp"},function(){r.launchSearch({title:c.mkp,precondition:'flattenedtaxonomies:"types/Markup"OR flattenedtaxonomies:"types/DMUMarkupRepReference"',showApplyButton:!1,ok:v.bind(this)})}.bind(this))),this.tokensWelcomePanel.push(s.subscribe({event:"ENXViews/WelcomePanel/action/welcomePanelIsr"},function(){r.launchSearch({title:c.isr,precondition:'flattenedtaxonomies:"types/SIMItfSimulation"',showApplyButton:!1,ok:v.bind(this)})}.bind(this))),this.tokensWelcomePanel.push(s.subscribe({event:"ENXViews/WelcomePanel/action/welcomePanelItf"},function(){r.launchSearch({title:c.itf,precondition:'flattenedtaxonomies:"types/PLMPIMMetricReference"',showApplyButton:!1,ok:v.bind(this)})}.bind(this))),this.tokensWelcomePanel.push(s.subscribe({event:"ENXViews/WelcomePanel/action/welcomePanelDrw"},function(){r.launchSearch({title:c.drw,precondition:'flattenedtaxonomies:"types/Drawing"',showApplyButton:!1,ok:v.bind(this)})}.bind(this))),this.tokensWelcomePanel.push(s.subscribe({event:"ENXViews/WelcomePanel/action/welcomePanelDIFLayout"},function(){r.launchSearch({title:c.difLayout,precondition:'flattenedtaxonomies:"types/DIFLayout"',showApplyButton:!1,ok:v.bind(this)})}.bind(this))),this.tokensWelcomePanel.push(s.subscribe({event:"ENXViews/WelcomePanel/action/welcomePanelDIFSheet"},function(){r.launchSearch({title:c.difSheet,precondition:'flattenedtaxonomies:"types/DIFSheet"',showApplyButton:!1,ok:v.bind(this)})}.bind(this))),this.tokensWelcomePanel.push(s.subscribe({event:"ENXViews/WelcomePanel/action/welcomePanelDIFAnnotationSet"},function(){r.launchSearch({title:c.difAnnotSet,precondition:'flattenedtaxonomies:"types/DIFAnnotationSet"',showApplyButton:!1,ok:v.bind(this)})}.bind(this))),this.tokensWelcomePanel.push(s.subscribe({event:"ENXViews/WelcomePanel/action/welcomePanelDocument"},function(){r.launchSearch({title:c.doc,precondition:'flattenedtaxonomies:"types/Document"',showApplyButton:!1,ok:v.bind(this)})}.bind(this))),this.tokensWelcomePanel.push(s.subscribe({event:"ENXViews/WelcomePanel/action/welcomePanelRequirement"},function(){r.launchSearch({title:c.req,precondition:'flattenedtaxonomies:"types/Requirement"',showApplyButton:!1,ok:v.bind(this)})}.bind(this))),this.tokensWelcomePanel.push(s.subscribe({event:"ENXViews/WelcomePanel/action/welcomePanelRequirementSpec"},function(){r.launchSearch({title:c.reqSpec,precondition:'flattenedtaxonomies:"types/Requirement Specification"',showApplyButton:!1,ok:v.bind(this)})}.bind(this))),this.tokensWelcomePanel.push(s.subscribe({event:"ENXViews/WelcomePanel/action/welcomePanelMsr"},function(){r.launchSearch({title:c.msr,precondition:'flattenedtaxonomies:"types/DesignSight"',showApplyButton:!1,ok:v.bind(this)})}.bind(this))),this.tokensWelcomePanel.push(s.subscribe({event:"ENXViews/WelcomePanel/action/welcomPanelPrefCmd"},function(){require(["DS/CATWebUXPreferences/CATWebUXPreferencesManager"],function(e){var t=e.givePreferenceManager({context:this.pad3DViewer.getFrameWindow()});if(t){var i=t.subscribe("onPreferencesPanelDestroyed",function(){t.unsubscribe(i),this.showWelcomePanel(!0)}.bind(this));this.showWelcomePanel(!1),t.displayPreferencePanel(this.pad3DViewer.getFrameWindow())}}.bind(this))}.bind(this)));var t=r.getDefaultActivities();t.activities[0].section.actions.splice(2,0,{id:"welcomePanelMkp",title:c.mkp,icon:"app-3dmarkup"}),t.activities[0].section.actions.splice(5,0,{id:"welcomePanelDIFAnnotationSet",title:c.difAnnotSet,icon:"annotation"}),t.activities[0].section.actions.splice(6,0,{id:"welcomePanelIsr",title:c.isr,icon:"interference-simulation"}),t.activities[0].section.actions.splice(7,0,{id:"welcomePanelItf",title:c.itf,icon:"interference-metric"}),t.activities[0].section.actions.splice(8,0,{id:"welcomePanelMsr",title:c.msr,icon:"physics-simulation"}),t.activities[0].section.actions.splice(9,0,{id:"welcomePanelDrw",title:c.drw,icon:"drawing"}),t.activities[0].section.actions.splice(10,0,{id:"welcomePanelRequirementSpec",title:c.reqSpec,icon:"requirement-specification"}),t.activities[0].section.actions.splice(11,0,{id:"welcomePanelRequirement",title:c.req,icon:"requirement"}),t.activities[0].section.actions.splice(12,0,{id:"welcomePanelDIFLayout",title:c.difLayout,icon:"fl-diagrams"}),t.activities[0].section.actions.splice(13,0,{id:"welcomePanelDIFSheet",title:c.difSheet,icon:"fl-diagram"}),t.activities[0].section.actions.splice(14,0,{id:"welcomePanelDocument",title:c.doc,icon:"doc"}),t.activities.push({section:{id:"customizeAppSection",title:c.customApp,actions:[{id:"welcomPanelPrefCmd",title:c.prefs,icon:"cog"}]}}),this.welcomePanel=new r({widget:e.widget,readOnlyApp:!0,renderDiv:e.renderDiv,externalViewerMgt:!0,externalViewerMgtCB:function(e,t){!0!==t&&this.showWelcomePanel("viewer"!==e)}.bind(this),globalSearchPrecond:'flattenedtaxonomies:"types/Markup" OR flattenedtaxonomies:"types/SIMItfSimulation" OR flattenedtaxonomies:"types/PLMPIMMetricReference" OR flattenedtaxonomies:"types/DIFLayout" OR flattenedtaxonomies:"types/DIFSheet" OR flattenedtaxonomies:"types/DIFAnnotationSet" OR flattenedtaxonomies:"types/VPMReference" OR flattenedtaxonomies:"types/VPMRepReference" OR flattenedtaxonomies:"types/ENOStrRefinementSpecification" OR flattenedtaxonomies:"types/Document" OR flattenedtaxonomies:"types/Requirement" OR flattenedtaxonomies:"types/Requirement Specification" OR flattenedtaxonomies:"types/DesignSight"',defaultActionId:t.defaultActionId,activities:t.activities,onLandingPageReady:e.onLandingPageReady})}h.prototype={constructor:h,initialize:function(){var e=this.welcomePanel.initialize();return e.then(function(){var e=m.call(this),t=Array.prototype.concat.apply([],u.map(function(t){return Object.keys(e[t.key]||[])}));f.call(this,t)}.bind(this)),e},setViewer:function(e){this.pad3DViewer=e,!this.params.initApp&&this.showWelcomePanel(!1);var t=e.render().getContent(),i=t.parentNode;t.style.position="relative",!this.params.renderDiv.parentNode&&i&&i!==this.params.renderDiv&&i.insertBefore(this.params.renderDiv,i.firstChild),this.params.renderDiv.insertBefore(t,this.params.renderDiv.firstChild),this.welcomePanel&&(this.params.renderDiv.insertBefore(this.welcomePanel.elements.mainBody,this.params.renderDiv.firstChild),this.welcomePanel.setViewer(e,!this.params.initApp)),this.tokensPAD.push(e.subscribe({event:"onRootAdded"},function(t){var i=require("DS/ENOPAD3DViewer/utils/PAD3DViewerModelServices"),n=t.id,o=e.getController().getNode({id:n}),a=i.getNodeType(o);if("DesignSight"===a||"DIFLayout"===a||"DIFSheet"===a||"Document"===a||"Requirement"===a||"Requirement Specification"===a){"DesignSight"===a?this.params.widget.setValue("mkpSession",JSON.stringify({type:"msr",plmType:a,id:n})):this.params.widget.setValue("mkpSession",JSON.stringify({type:"Document"===a||"Requirement"===a||"Requirement Specification"===a?a:"2D",plmType:a,id:n}));var r=m.call(this);r[a="DIFLayout"===a?"difLayout":"DIFSheet"===a?"difSheet":"Requirement"===a?"req":"Requirement Specification"===a?"reqSpec":"DesignSight"===a?"msr":"doc"]||(r[a]={}),r[a][n]={},this.params.widget.setValue("xsceneStoredObjects",JSON.stringify(r)),this.showWelcomePanel(!1),f.call(this,[n])}}.bind(this))),this.tokensPAD.push(e.subscribe({event:"onRootRemoved"},function(e){var t=m.call(this);["difLayout","difSheet","doc","req","reqSpec","msr"].some(function(i){return t[i]&&t[i][e.id]})&&(f.call(this,[e.id]),this.params.widget.deleteValue("mkpSession"))}.bind(this))),require(["DS/DMUBaseExperience/DMUContextManager"],function(t){var i=t.getEventsController({viewer:e.getViewer()});this.tokensMkp.push(i.addEvent("onReviewCtxInformationChanged",function(e){var t=m.call(this);t.mkp||(t.mkp={}),t.mku||(t.mku={}),"Markup"===e.reviewType?t.mkp[e.reviewId]={}:"DMUMarkupRepReference"===e.reviewType&&(t.mku[e.reviewId]={}),this.params.widget.setValue("xsceneStoredObjects",JSON.stringify(t)),f.call(this,[e.reviewId])}.bind(this))),this.tokensMkp.push(i.addEvent("onReviewRemoved",function(e){var t=m.call(this);(e&&t.mkp&&t.mkp[e.reviewId]||e&&t.mku&&t.mku[e.reviewId])&&f.call(this,[e.reviewId]),this.params.widget.deleteValue("reviewIdInSession")}.bind(this)))}.bind(this))},appReady:function(e){if(this.isAppReady=!0,this.params.initApp){let r=this.params.widget.getValue("X3DContentId"),s=this.params.widget.id.indexOf("preview")>-1;if(this.params.widget.deleteValue("X3DContentId"),r)e&&e.appInitFromTransition({x3dContent:JSON.parse(r),onNoActionDone:function(){setTimeout(function(e){var t=JSON.parse(r);(t.data&&t.data.items||[]).forEach(function(e){("Drawing"===e.objectType||e.objectTaxonomies&&e.objectTaxonomies.indexOf("Drawing")>-1)&&delete e.path}),e.addRootNodesFromContent({json3DXContent:t,dispatch:!1})},0,this.pad3DViewer)}.bind(this)});else if(!0===this.params.widget.getValue("autoreload")||!0===this.params.widget.getValue("autoreloadPreviewApp")&&!s){var t=this.params.widget.getValue("reviewIdInSession");if(t)D.call(this,[{id:JSON.parse(t).reviewID,type:JSON.parse(t).reviewType}]);else{var i=this.params.widget.getValue("mkpSession");if((i=i?JSON.parse(i):null)&&"ITF"===i.type){var n=this.params.widget.getValue("ItfSimuInSession");if((n=n?JSON.parse(n):null)&&!Array.isArray(n)&&"object"==typeof n){var o=Object.keys(n);if(1===o.length){var a=o[0];Array.isArray(n[a])&&n[a].length?D.call(this,n[a].map(function(e){return{id:e,type:"PLMPIMMetricReference"}})):D.call(this,[{id:a,type:"SIMItfSimulation"}])}}}else i&&i.id&&("2D"===i.type||"Document"===i.type||"msr"===i.type)&&D.call(this,[{id:i.id,type:i.plmType}])}}s?this.params.widget.setValue("autoreloadPreviewApp",!0):this.params.widget.deleteValue("autoreloadPreviewApp")}var r=this.welcomePanel.appReady();return this.welcomePanel.redraw(),r},dispose:function(){if(this.showWelcomePanel(!1),this.welcomePanel){var e=require("DS/DMUBaseExperience/DMUContextManager").getEventsController({viewer:this.pad3DViewer.getViewer()});this.tokensMkp.forEach(e.removeEvent,e),this.tokensMkp=[],this.tokensPAD.forEach(this.pad3DViewer.unsubscribe,this.pad3DViewer),this.tokensPAD=[],this.pad3DViewer=this.params=null,this.tokensWelcomePanel.forEach(s.unsubscribe,s),this.tokensWelcomePanel.length=0;var t=this.welcomePanel.dispose();return this.welcomePanel=null,t}},showWelcomePanel:function(e){this.welcomePanel&&(this.welcomePanel.elements.mainBody.style.position="relative",this.welcomePanel.elements.mainBody.style.display=e?"":"none",e&&this.welcomePanel.redraw())}};var b,y,S,R,P={widget:null,widgetLink:null,viewer:null,viewerAttachment:null},k=!1,M="DMU3DMarkupDefaultWkb",A=i.getOption("testWkb"),C={wkb2D:{type:"2D",wkb:[{file:A?"DMU3DReviewWidget2D_TST.xml":"DMU3DReviewWidget2D.xml",module:A?"CAT3DReviewTestWidget":"DMU3DReviewWidget"}]},wkb2DFormats:{type:"2DFormats",wkb:[{file:"DMU2DFormats.xml",module:"DMU3DReviewWidget"}]},wkb3D:{type:"3D",wkb:[{file:A?"DMU3DReviewWidget_TST.xml":"DMU3DReviewWidget.xml",module:A?"CAT3DReviewTestWidget":"DMU3DReviewWidget"}]},wkb3DFormats:{type:"3DFormats",wkb:[{file:"DMU3DFormats.xml",module:"DMU3DReviewWidget"}]},wkbITF:{type:"ITF",wkb:[{file:A?"DMUReviewAndITFWidget_TST.xml":"DMUReviewAndITFWidget.xml",module:A?"CAT3DReviewTestWidget":"DMU3DReviewWidget"}]},wkbITFFormats:{type:"ITFFormats",wkb:[{file:"DMU3DFormats.xml",module:"DMU3DReviewWidget"}]},wkbDocument:{type:"Document",wkb:[{file:A?"DMU3DReviewWidgetDocument_TST.xml":"DMU3DReviewWidgetDocument.xml",module:A?"CAT3DReviewTestWidget":"DMU3DReviewWidget"}]},wkbRequirement:{type:"Requirement",wkb:[{file:A?"DMU3DReviewWidgetRequirement_TST.xml":"DMU3DReviewWidgetRequirement.xml",module:A?"CAT3DReviewTestWidget":"DMU3DReviewWidget"}]},"wkbRequirement Specification":{type:"Requirement Specification",wkb:[{file:A?"DMU3DReviewWidgetRequirement_TST.xml":"DMU3DReviewWidgetRequirement.xml",module:A?"CAT3DReviewTestWidget":"DMU3DReviewWidget"}]},wkb2DFL:{type:"2DFL",wkb:[{file:"DMU3DReviewWidget2DFL.xml",module:"DMU3DReviewWidget"}]},wkb2DFLFormats:{type:"2DFLFormats",wkb:[{file:"DMU2DFormats.xml",module:"DMU3DReviewWidget"}]},wkbMSR:{type:"MSR",wkb:[{file:"DMU3DMarkupWidgetMSR.xml",module:"DMU3DReviewWidget"}]},wkbR2V:{type:"R2V",wkb:[{file:"DMU3DMarkupWidgetR2V.xml",module:"DMU3DReviewWidget"}]},wkbMSRFormats:{type:"MSRFormats",wkb:[{file:"DMUMSRFormats.xml",module:"DMU3DReviewWidget"}]}};function U(){return P.widget.getValue(M)}function x(e){P.widget.setValue(M,e)}function W(){P.viewer.getContentName({callbacks:{onComplete:function(e){P.widget.setTitle(e.contentName)},onFailure:function(){}}})}var V=[],I=function(e){V.push(e),1===V.length&&function e(t){var i={lastCommand:[],currentCommand:null,defaultCommand:null},n=P.viewer.getCommandContext?P.viewer.getCommandContext():null;n?a._commandsState[n]=i:a._commandsState=i,a.resetDefaultCommand(n),a._isCommandEnding=!1,a._ignoreCommandBeginWhileEndingCommand=!1,a._commands&&a._cmdCheckHeader&&["_commands","_cmdCheckHeader"].forEach(function(e){var t=n?a[e][n]:a[e];Object.keys(t).forEach(function(e){var i=t[e];i&&i._destroy&&i._destroy(),delete t[e]})});var o=0,r=t.viewer.getFrameWindow().getActionBar(),s=r.onActionBarReady(function(){++o<t.afr.wkb.length&&t.merge?t.viewer.loadActionBar({merge:!0,file:t.afr.wkb[o].file,module:t.afr.wkb[o].module}):(r.removeCallback(s),t.viewer.getFrameWindow().getContextualUIManager().activateContextualBarOnLeftClicAfterCSOChange(),a.setDefaultCommand({ID:"DMU3ReviewWidgetDefaultHdr",className:"DS/DMU3DReviewWidget/DMU3DReviewWidgetDefaultCmd",context:n}),t.actionBarReadyCb&&t.actionBarReadyCb(),t.options&&t.options.done({appMainDiv:S,pad3DViewer:t.viewer})),t.merge&&o!==t.afr.wkb.length||(V.shift(),V.length&&e(V[0]))});t.viewer.loadActionBar({merge:!1,file:t.afr.wkb[0].file,module:t.afr.wkb[0].module})}(V[0])};async function F(e){e&&(e.subscribe("onReviewContextModified",function(e){var t=e.onABReady?e.onABReady:function(){},i=U();if("Data"===e.type){if("3D"===e.data.wkb){for(var n=!1,o=0;o<C.wkb3D.wkb.length;o++)C.wkb3D.wkb[o].file===e.data.file&&C.wkb3D.wkb[o].module===e.data.module&&(n=!0);n||(C.wkb3D.wkb.push({file:e.data.file,module:e.data.module}),"3D"===i&&I({merge:!0,afr:C.wkb3D,viewer:P.viewer,actionBarReadyCb:t}))}}else"RefreshAB"===e.type?I({merge:!0,afr:C["wkb"+i],viewer:P.viewer,actionBarReadyCb:t}):("ITF"===e.type||"MSR"===e.type||"R2V"===e.type||"Document"===e.type||"Requirement"===e.type||"Requirement Specification"===e.type||e.type!==i&&C["wkb"+e.type])&&(I({merge:!0,afr:C["wkb"+e.type],viewer:P.viewer,actionBarReadyCb:t}),e.type.contains("Formats")||x(e.type));"ITF"===e.type?P.widget.setValue("mkpSession",JSON.stringify({type:e.type})):"Data"===e.type&&P.widget.deleteValue("mkpSession")}),await e.setActiveState(!0))}var E,T,L,O=function(){require(["DS/CfgAuthoringContextUX/scripts/CfgAuthoringContext","DS/PADUtils/PADCommandProxy"],function(e,t){(E=e).renderDiv||E.initialize2(P.viewer.getFrameWindow().getViewerFrame(),t.appId(),P.widget.getValue("changeControlActivation")?"show":"hide","top-right",function(){E.addEvent("onAuthoringCtxVisible",function(e){e.showAuthoringContext&&P.widget.setValue("changeControlActivation",!0)})})})},N=function(e){require(["DS/PADUtils/PADCommandProxy","DS/PADUtils/PADSharedSettings","DS/DMUBaseExperience/DMUContextManager","i18n!DS/PADUtils/assets/nls/PADUtils"],function(t,o,a,r){var s,l=[],d=[];L=function(){P.widget.setPreferences([]),s.destroy(),l.forEach(function(e){e.cb&&n.removeEvent(e.cb,e.padFct)}),L=function(){}},T=function(){if(l.forEach(function(e){var t=e.preference.name,i=P.widget.getValue(t),o=n.getSetting(t);(i="boolean"===e.preference.type&&"boolean"!=typeof i?"true"===i:i)!==o&&n.setSetting(t,i),e.fct&&e.fct(i)}),n.updateWidgetPreferences(),!s&&l.length){let e=a.giveEventsController({viewer:P.viewer.getViewer()});s=t.create({events:{onPreferenceModification:function(e){l.some(function(t){if(e.name===t.preference.name){if("boolean"!=typeof e.value&&"string"!=typeof e.value)throw new Error("Value must be a string or a boolean");if(n.getSetting(e.name)!==e.value){if("boolean"===t.preference.type&&"boolean"!=typeof e.value)throw new Error("Preference is a boolean type so value must be a boolean");t.debounce=!0,P.widget.setValue(e.name,e.value),n.setSetting(e.name,e.value)}return t.fct&&t.fct(e.value),!0}})},onSelect:function(t){e&&e.fireEvent("onCrossWidgetSelection",t)}}}),P.widgetLink&&P.widgetLink.subscribe("DS/ExternalWdgtCom/Find",t=>{e&&e.fireEvent("onCrossWidgetFind",t)}),e&&(e.addEvent("onFireCrossWidgetSelection",e=>{s&&s.select(e)}),e.addEvent("onFireCrossWidgetFind",e=>{e&&e.paths&&e.paths.length&&P.widgetLink&&P.widgetLink.publish("DS/ExternalWdgtCom/Find",{paths:e.paths})})),l.forEach(function(e){e.cb&&n.addEvent(e.cb,e.padFct=function(t){e.debounce?e.debounce=!1:s.preferenceModification({name:e.preference.name,value:t})})})}},P.widget.setPreferences([]),n.setSetting("syncAutoReload",!0),o.destroy(),o.addSharedSettings(P.widget),l.push({preference:{name:"changeControlActivation",type:"boolean",label:r.changeControlActivation_label,defaultValue:!1},fct:function(e){E&&E[e?"show":"hide"]()},cb:"onChangeControlActivation",debounce:!1}),l.push({preference:{name:"changeControlPosition",type:"list",label:r.changeControlPosition_label,defaultValue:"top-right",options:[{label:r["top-right_label"],value:"top-right"},{label:r["top-left_label"],value:"top-left"},{label:r["bottom-left_label"],value:"bottom-left"},{label:r["bottom-right_label"],value:"bottom-right"}]},fct:function(e){E&&E.setPosition(e)}}),n.setSetting("authorizeChangeControl",!0),e&&O(),i.getOption("debug")?l.push({preference:{name:"webapi_timeout",type:"text",label:"Query timeout",defaultValue:"60000"}}):d=["webapi_timeout"],d.forEach(function(e){P.widget.deleteValue(e)}),d.length=0,l.forEach(function(e){P.widget.mergePreferences([e.preference])}),T(),P.widget.addEvent("endEdit",T)})},q=function(e){e.unsubscribe(y),y=null,W(),O(),require("DS/PADUtils/PADUtilsServices").isCloud()&&!window.__preventPrefPanel&&e.getViewer().displayIntroductoryControlPreferencePanel(),P.widget.getValue(M)||P.widget.addPreference({name:M,type:"hidden"});var t=!1,i=async function(){if(t){var i=b.get3DReviewController({context:e});await F(i),R.appReady(i)}else t=!0};require(["DS/DMU3DReviewController/DMU3DReviewController"],function(e){b=e,i()}),setTimeout(function(){"1"===P.widget.getValue("widgetForODTReplay")&&(P.widgetViewer3DReview._viewer3DReview=e),P.widget.dispatchEvent("onWidgetAndAppReady","1"===P.widget.getValue("widgetForODTReplay")?[{pad3DViewer:P.viewer}]:void 0),i()}),P.viewerAttachment&&P.viewerAttachment(S),P.viewerAttachment=null,e.getFrameWindow().getContextualUIManager().activateContextualBarOnLeftClicAfterCSOChange()};function _(e){"string"==typeof e.reviewID&&P.widget.setValue("reviewIdInSession",JSON.stringify(e))}function B(t){return S||(S=e.createElement("div",{class:"appMain",styles:{width:"auto",height:"100%"}})),(R=new h({widget:P.widget,readOnlyApp:!1,renderDiv:S,initApp:t&&t.initApp,onLandingPageReady:t&&t.onLandingPageReady})).initialize()}function X(){if(!P.viewer){var e=new Promise(function(e){B({initApp:!0,onLandingPageReady:function(){P.viewerAttachment(S),P.viewerAttachment=null}}).then(e)}),t=new Promise(function(e){require(["DS/ENOPAD3DViewer/PAD3DViewer","text!DS/DMU3DReviewWidget/assets/DMU3DReviewWidget.json"],function(t,i){e([t,i])})}),i=new Promise(e=>{require(["DS/ENOXInterWdgCom/LinkManager"],t=>{P.widgetLink=t,t.initializeWidgetLink(P.widget,{appIds:[],uses:["ENXContent","ENXEnrich"],implements:["ENXContent","ENXEnrich"],open:!0}).then(e)})});Promise.all([e,t,i]).then(function(e){var t=JSON.parse(e[1][1]);t.context="Default",t.mouseProfile=require("DS/PADUtils/PADUtilsServices").isCloud()?window.__mouseProfile:"CATIA";var i=U();t.workbenchModule="2D"===i||"ITF"===i||"R2V"===i||"MSR"===i||"Document"===i||"2DFL"===i?C["wkb"+i].wkb[0].module:C.wkb3D.wkb[0].module,t.workbench="2D"===i||"ITF"===i||"MSR"===i||"R2V"===i||"Document"===i||"2DFL"===i?C["wkb"+i].wkb[0].file:C.wkb3D.wkb[0].file;var o={widget:P.widget,windowOptions:t,enableFiltering:!0,binaryPrimitives:!0,enableSidePanel:!0,readyCB:N,visuProgressiveTileModel:!0},a=P.viewer=new e[1][0](o);t.mouseProfile&&a.getViewpoint().setDefaultController(!0,t.mouseProfile),R.setViewer(a);var r=n.getSetting("propertiesFacet"),s=l.getMapping();s&&(s.physicssimulation.alwaysVisible=!1,r.push(s.physicssimulation)),a.setOption("propertiesFacet",r),a.setAuthorizedRootTypes(n.getSetting("ups_authorized_root_types")),y=a.subscribe({event:"onReady"},q),a.subscribe({event:"onRootAdded"},W),a.subscribe({event:"onRootRemoved"},W),a.subscribe({event:"onRootAttached"},W),a.subscribe({event:"onRootDetached"},W),a.subscribe({event:"onContentNameModified"},W),a.setupWidgetLink(),n.setSetting("settypes",!0),"1"===P.widget.getValue("widgetForODTReplay")&&P.widget.dispatchEvent("onPAD3DViewerCreated",[{pad3DViewer:a}]),P.widgetInitDone&&P.widgetInitDone({appMainDiv:S,pad3DViewer:a})})}}function j(){if(!1===k&&P.viewer){var e=P.widget.getValue("reviewIdInSession");e=e?JSON.parse(e):void 0,P.viewer.refresh();var t,i,n=o.getRoots(P.viewer),a=!1;for(let e=0;e<n.length;e++)if(""!==o.getNodeID(n[e])&&(t=o.getNodeID(n[e]),"Document"===(i=o.getNodeType(n[e]))||"Requirement"===i||"Requirement Specification"===i||"DIFSheet"===i||"DIFLayout"===i)){a=!0;break}if(a)var r=P.viewer.subscribe({event:"onRefreshCompleted"},function(){P.viewer.unsubscribe(r);var e=P.viewer.options.visuProgressiveTileModel?[{rootID:t,validForServerCall:!0,structure:{rootStats:{infos:{encoding:"UTF-8",kind:"InstRef",version:"1.1"},results:[{resourceid:t,"ds6w:type":i,"ds6w:globaltype":"ds6w:Part","ds6w:globalType":"ds6w:Part",boundingbox:{max:[-1,-1,-1],min:[1,1,1]}},{synthesis:{name:"ProgressiveExpand_FlatSynthesis",value:1}},{synthesis:{name:"ProgressiveExpand_OccSynthesis",value:1}},{synthesis:{name:"ProgressiveExpand_LeafSynthesis",value:1}}]},infos:{encoding:"UTF-8",kind:"InstRef",version:"1.1"},results:[{resourceid:t,"ds6w:type":i,"ds6w:globaltype":"ds6w:Part","ds6w:globalType":"ds6w:Part",nbchildren:"0"}]}}]:[{rootID:t,validForServerCall:!0,structure:{rootId:i,results:[{attributes:[{name:"did",value:t},{name:"physicalid",value:t},{name:"ds6w:type",value:i}]},{path:[t]}]}}];P.viewer.loadJSON({data:e})});else"DesignSight"===i&&P.viewer.publish({event:"widgetReresh",data:{objectId:t,objectType:i}});if(e)var s=P.viewer.subscribe({event:"onLoadingComplete"},function(){P.viewer.unsubscribe(s),_(e);let i=P.widget.getValue("reviewIdInSession");(t=JSON.parse(i).reviewID)&&b&&b.give3DReviewController({context:P.viewer}).loadReview(t)})}}function J(){k=!0}function z(){if(b){var e=b.give3DReviewController({context:P.viewer});e&&e.refreshContext()}k=!1}function Q(){R&&R.dispose(),R=null,P.viewer&&(b&&b.unreference3DReviewController({context:P.viewer}),P.viewer.destroy()),P={widget:null,viewer:null,viewerAttachment:null},b=null}function G(e){P.viewer&&P.viewer.search({searchQuery:e})}var H=function(e){if(P.widget=e.widget,S=e.appMainDiv,P.widgetInitDone=e.done,e.pad3DViewer){P.viewer=e.pad3DViewer;var t=require("DS/DMUBaseExperience/DMUContextManager").getReviewContext({context:e.pad3DViewer}),i=t?t.getReviewCtxInformation():null;i&&i.reviewId&&_(i.reviewId)}else e.viewerAttachment?(P.viewerAttachment=e.viewerAttachment,X()):(P.viewerAttachment=function(e){P.widget.setBody(e)},P.widget.addEvent("onLoad",X)),P.widget.addEvent("onEdit",J),P.widget.addEvent("endEdit",z),P.widget.addEvent("onRefresh",j),P.widget.addEvent("onDestroy",Q),P.widget.addEvent("onSearch",G),"1"===P.widget.getValue("widgetForODTReplay")&&(P.widgetViewer3DReview=this),P.widget.addEvent("onDMUMarkupLoaded",_),P.widget.setMetas({helpPath:"DMU3DReviewWidget/assets/help"})};return H.prototype.setUp=function(e){function t(){require(["DS/DMU3DReviewController/DMU3DReviewController"],function(t){var i=(b=t).get3DReviewController({context:P.viewer});(async()=>{await F(i),e.sourceApp&&e.sourceApp.issues&&1===e.sourceApp.issues.length&&i.appInitFromTransition({issues:e.sourceApp.issues,onNoActionDone:function(){}})})()})}B().then(function(){R.setViewer(P.viewer),R.appReady(),P.widget.addEvent("onRefresh",j),P.widget.addEvent("onSearch",G),P.widget.addEvent("onDMUMarkupLoaded",_),P.widget.setMetas({helpPath:"DMU3DReviewWidget/assets/help"}),e.sourceApp&&e.sourceApp.pad3DViewer!==P.viewer&&(P.viewer=e.sourceApp.pad3DViewer);var i=n.getSetting("propertiesFacet"),a=l.getMapping();a&&a.physicssimulation&&a.physicssimulation.facet&&(i.find(function(e){return!(!e||!e.facet||e.facet.name!==a.physicssimulation.facet.name)})||(a.physicssimulation.alwaysVisible=!1,i.push(a.physicssimulation))),P.viewer.setOption("propertiesFacet",i),P.viewer.getBIController().toggleBetweenStandaloneAndSlaveMode("DUAL"),N(P.viewer),P.viewer.getViewer().updateViewPanelUIConfiguration&&P.viewer.getViewer().updateViewPanelUIConfiguration({useShadingIllustration:!1,useOutline:!1}),W(),"3D"===U()||o.getRoots(P.viewer).length>0?(I({options:e,merge:!1,afr:C.wkb3D,actionBarReadyCb:t,viewer:P.viewer}),x("3D")):"2DFL"===U()?(I({options:e,merge:!1,afr:C.wkb2DFL,actionBarReadyCb:t,viewer:P.viewer}),x("2DFL")):(I({options:e,merge:!1,afr:C.wkb2D,actionBarReadyCb:t,viewer:P.viewer}),x("2D")),n.setSetting("dynamic_authorized_types",!0)})},H.prototype.dispose=function(e){if(n.setSetting("dynamic_authorized_types",!1),P.widget.removeEvent("onRefresh",j),P.widget.removeEvent("onSearch",G),P.widget.removeEvent("endEdit",T),P.widget.removeEvent("onDMUMarkupLoaded",_),L&&L(),R&&R.dispose(),R=null,P.viewer){var t=n.getSetting("propertiesFacet"),i=l.getMapping();if(i){var o=t.findIndex(function(e){return!(!e||!e.facet||e.facet.name!==i.physicssimulation.facet.name)});o>=0&&t.splice(o,1)}P.viewer.setOption("propertiesFacet",t),b&&b.unreference3DReviewController({context:P.viewer,options:e})}C.wkb2D.wkb.length>1&&C.wkb2D.wkb.splice(1,C.wkb2D.wkb.length),C.wkb2DFL.wkb.length>1&&C.wkb2DFL.wkb.splice(1,C.wkb2DFL.wkb.length),C.wkb3D.wkb.length>1&&C.wkb3D.wkb.splice(1,C.wkb3D.wkb.length),C.wkb2DFormats.wkb.length>1&&C.wkb2DFormats.wkb.splice(1,C.wkb2DFormats.wkb.length),C.wkb2DFLFormats.wkb.length>1&&C.wkb2DFLFormats.wkb.splice(1,C.wkb2DFLFormats.wkb.length),C.wkb3DFormats.wkb.length>1&&C.wkb3DFormats.wkb.splice(1,C.wkb3DFormats.wkb.length),b=null},H.prototype.onWillLeave=function(e){if(P.viewer){var t=require("DS/DMUBaseExperience/DMUContextManager");if(t){var i=t.giveEventsController({viewer:P.viewer.getViewer()});i&&i.fireEvent("onLeavingR3DApp",{targetApp:e&&e.options?e.options.id:""})}}},H});