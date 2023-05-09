define("DS/DMUPlaySlide/DMUSlideImport",["UWA/Class","UWA/Utils","DS/DMUBaseExperience/EXPUtils"],function(e,t,i){"use strict";return e.extend({init:function(e){let n,a,l,r;function s(e,t,n,a){let l;if(e&&t){let r=e.getParameters();if(r){l={};for(let s=0;s<r.length;s++){let o=r[s];if(o.JSONname&&(!n||n&&o.isOverloadable)){let n=t,r=o.JSONname.split(".");for(;void 0!==n;){0===r.length&&(l[o.name]=i.strToType(n,o.type),a&&e.setNominalAttribute(o.name,l[o.name])),n=n[r.shift()]}}}}}return l}this.importData=function(t,i,s,o,g){if(l=o,r=g,t&&i&&e){if(i.fillObjectAttributes(e,t,s),n=t.EnvironmentOverload||{},a=t.ObjectOverloads||[],t.ApplicativeContext){let i=Object.keys(t.ApplicativeContext);for(let n=0;n<i.length;n++)e.setSlideApplicativeContext(i[n],t.ApplicativeContext[i[n]])}i.registerForPostImport(e)}},this.afterImport=function(i){if(e){let o=e.getAttribute("lDMUObjects")||[],g=!1;for(let e=o.length-1;e>=0;e--)o[e]&&o[e].getType||(o.splice(e,1),g=!0);g&&e.setAttribute("lDMUObjects",o);let d=function(e,t){if(e&&"object"==typeof e){if(e.hasOwnProperty(t))return!0;let i=Object.keys(e);for(let n=0;n<i.length;n++)if(d(e[i[n]],t))return!0}};if(n){let t=e.getParent().getParent();e.updateEnvironmentOverload({target:t.getEnvironment(),state:s(t.getEnvironment(),n.state,!0)})}let u=e.getDMUObjects(),D=[],c=[];if(!r&&a){let t,i=e.getParent().getOccurrenceOverloaders();a.forEach(function(n){let a,r=!1;for(a=0;a<i.length;a++)if(i[a].getAttribute("sID")===n.targetId){e.addObjectOverload({target:i[a],state:s(i[a],n.state,!0)}),r=!0;break}if(l&&!r)for(a=0;a<u.length;a++)if(u[a].getAttribute("sID")===n.targetId){D.push(n.targetId),c.push(n),void 0===(t=s(u[a],n.state,!1,!0)).bVisible&&u[a].setNominalAttribute(!1);break}})}if(r)e.setAttributes([{name:"sID",value:e.getParent().computeNewID()},{name:"sUuid",value:t.getUUID()}]);else if(l)for(let t=0;t<u.length;t++){let i=D.indexOf(u[t].getAttribute("sID"));-1!==i&&(d(c[i],"visible")||d(c[i],"Visible"))||u[t].setNominalAttribute("bVisible",!1),u[t].setNominalAttribute("sID",e.getParent().computeNewID())}e.fireEvent("onDMUObjectInitialized",{dmuObject:e,dmuCont:e.getParent(),mkpRepRef:i})}}}})}),define("DS/DMUPlaySlide/controllers/DMUSlideShowController",["UWA/Class","DS/CATWebUXSlideShow/CATWebUXSlideShowController","DS/DMUBaseUIControllers/DMUSlidesController","DS/DMUBaseUIControllers/DMUCommentsController","DS/DMUBaseUIControllers/DMUReviewController","DS/DMUBaseExperience/DMUContextManager","DS/DMUBaseUIControllers/DMUApplicativeContextManager","DS/DMUBaseExperience/EXPManagerSet","DS/Selection/CSOManager","DS/Selection/HSOManager","DS/Selection/PSOManager","DS/WebappsUtils/WebappsUtils","i18n!DS/DMUPlaySlide/assets/nls/DMUPlaySlide"],function(e,t,i,n,a,l,r,s,o,g,d,u,D){"use strict";return e.extend({init:function(e){this._parent(e);var c=(e||{}).context,m=s.getManager({name:"DMU2DSheetManager",context:c.getFrameWindow()}),f=u.getWebappsAssetUrl("DMUPlaySlide","icons/32/")+"I_DMU_Slide.png",p=t.getSlideShowController({context:c.getFrameWindow(),id:"DMU3DMarkupSlideShow"});p.setListOfCommandsAllowed(["LockUnlockSectionHdr","DMUPreviousCommentCmdHdr","DMUNextCommentCmdHdr","WebDocumentFitAllInCmdHdr","WebDocumentFitWidthCmdHdr","WebDocumentPreviousPageCmdHdr","WebDocumentNextPageCmdHdr"]);var h,b,M,v,S=[],U=l.giveEventsController({viewer:c.getViewer()});function O(e){var t=l.getReviewContext({context:c}),n=t?t.getValidation():null;if(e.slideData){var a=i.giveDMUSlidesController({context:c.getFrameWindow()});if(a){if(n){var r=e.slideData.slide?e.slideData.slide.getParent().getMarkupOwner():null,s=a.getActiveSlide(),o=s?s.getParent().getMarkupOwner():null;r&&o&&r.getPlmID()!==o.getPlmID()&&n.setCurrentMarkup(r);var g=t.getCurrentSessionMarkup().getSlides(),d=g[0].getAttribute("sID"),u=g[g.length-1].getAttribute("sID"),m=e.slideData.slide?e.slideData.slide.getAttribute("sID"):"";if(r)if(m===d){var h=n.getPreviousRepRef(r.getPlmID());h.isLoaded()||(U.fireEvent("onReplyExpandRequest",{data:{id:h.getPlmID()}}),n.setCurrentMarkup(r))}else if(m===u){var b=n.getNextRepRef(r.getPlmID());b.isLoaded()||(U.fireEvent("onReplyExpandRequest",{data:{id:b.getPlmID()}}),n.setCurrentMarkup(r))}}a.setActiveSlide(e.slideData.slide),p.displaySlideInformation({icon:f,message:e.slideData.slide?e.slideData.slide.getAttribute("sName")+" ("+e.slideData.index+" / "+e.slideData.nbSlides+") ":D.nominalSlide,secondary:n?n.getCurrentMarkup().getName():t?t.getCurrentSessionMarkup().getAttribute("sName"):void 0})}}else if(e.elementData){var M,v;if("page"===e.elementData.type)v=e.elementData.visibleElements.length>1?e.elementData.visibleElements[0].number+", "+e.elementData.visibleElements[1].number:e.elementData.visibleElements[0].number,M=D.page+" "+v+" "+D.of+" "+e.elementData.elementLength;else"requirement"===e.elementData.type&&(M=e.elementData.visibleElements[0].title);p.displaySlideInformation({fontIcon:"wux-ui-3ds-docs",message:M,secondary:n&&n.getCurrentMarkup()?n.getCurrentMarkup().getName():""})}}function y(){var e=null,t=l.getReviewContext({context:c});if(t){var i=t.getValidation();if(i){e=[];for(var n=i.getLoadedRepRefs(),a=0;a<n.length;a++)e=e.concat(n[a].getMarkupContent().getSlides())}else e=t.getCurrentSessionMarkup().getSlides()}return e}function C(e){if(m&&m.isADocumentDisplayed()&&("Document"===m.getDocumentType()||"Requirement"===m.getDocumentType()))m.centerViewpointAtNextOrPreviousElement("next"===e),O({elementData:m.getElementsVisualizationData()});else{var t=i.giveDMUSlidesController({context:c.getFrameWindow()});if(t){for(var n=y(),a=t.getActiveSlide(),l=0;l<n.length;l++){var r=n[l].getParent().getMarkupOwner(),s=null,o=!0;if(a&&(s=a.getParent().getMarkupOwner()),r&&s&&(o=r.getPlmID()===s.getPlmID()),a&&n[l].getAttribute("sID")===a.getAttribute("sID")&&o)return void("next"===e?l<n.length-1?O({slideData:{slide:n[l+1],index:l+2,nbSlides:n.length}}):O({slideData:{slide:n[0],index:1,nbSlides:n.length}}):O(l>0?{slideData:{slide:n[l-1],index:l,nbSlides:n.length}}:{slideData:{slide:n[n.length-1],index:n.length,nbSlides:n.length}}))}O("next"===e?{slideData:{slide:n[0],index:1,nbSlides:n.length}}:{slideData:{slide:n[n.length-1],index:n.length,nbSlides:n.length}})}}}p.subscribe("onPreviousSlideCB",function(){C("previous")}),p.subscribe("onNextSlideCB",function(){C("next")}),p.subscribe("onFirstSlideCB",function(){if(!m||!m.isADocumentDisplayed()||"Document"!==m.getDocumentType()&&"Requirement"!==m.getDocumentType()){var e=y();O({slideData:{slide:e[0],index:1,nbSlides:e.length}})}}),p.subscribe("onLastSlideCB",function(){if(!m||!m.isADocumentDisplayed()||"Document"!==m.getDocumentType()&&"Requirement"!==m.getDocumentType()){var e=y();O({slideData:{slide:e[e.length-1],index:e.length,nbSlides:e.length}})}}),p.subscribe("onActiveStateModified",function(e){var t,s,u,D,f=l.getReviewContext({context:c}),C=m&&m.isADocumentDisplayed()&&("Document"===m.getDocumentType()||"Requirement"===m.getDocumentType());if(C){var F=n.giveDMUCommentsController({context:c.getFrameWindow()});u=a.giveDMUReviewController({context:c.getFrameWindow()}),F&&(F.setVisibleFlag(!e.state),F.setPresentationModeFlag(e.state),u&&u.setVisibleFlag(!e.state)),m&&(e.state||(h&&U.removeEvent(h),h=null),m.setCurrentPageWidgetVisibleFlag(!e.state),m.setMultiPageVisibility(e.state),m.setBookmarksVisibility(!e.state),"Document"===m.getDocumentType()&&m.enableTextSelection(!e.state))}else{if(t=y(),u=a.giveDMUReviewController({context:c.getFrameWindow()}),!(s=i.giveDMUSlidesController({context:c.getFrameWindow()})))return void p.setActiveState(!1);if(s.setVisibleFlag(!e.state),!u)return void p.setActiveState(!1);u.setVisibleFlag(!e.state)}if(e.state){var A=C||null!=t&&t.length>0;if(e.onBeginSlideShow(A),!A)return;o.empty(),g.empty(),d.empty(),f&&(b=f.isReadOnly(),f.setReadOnly(!0),f.getCurrentSessionMarkup()&&!f.getCurrentSessionMarkup().isVisible()&&f.getCurrentSessionMarkup().setVisibleFlag(!0)),M=!!m&&m.getReviewPlayMode(),m&&(m.setReviewPlayMode(!0),m.setNavBarVisibleFlag(!1));var x=r.giveDMUApplicativeContextManager({context:c}),P=x?x.getApplicativeControllers():[];for(D=0;D<P.length;D++)S.push({ctx:P[D],minimalFlag:P[D].getMinimalUIFlag?P[D].getMinimalUIFlag():void 0}),P[D].setMinimalUIFlag&&(P[D].setMinimalUIFlag(!0),P[D].setAnnotationUIVisibleFlag&&P[D].setAnnotationUIVisibleFlag(!1));var N=c.getViewer();if(v={activated:N.getManipulators(),preActivate:N.manipulatorManager.preActivation},N.setManipulators({activated:!1,preActivate:!0}),C)O({elementData:m.getElementsVisualizationData()}),h=U.addEvent("onCurrentElementChanged",function(){O({elementData:m.getElementsVisualizationData()})});else if(s){var I=s.getActiveSlide();if(I){for(D=0;D<t.length;D++)if(t[D].getAttribute("sID")===I.getAttribute("sID")){O({slideData:{slide:I,index:D+1,nbSlides:t.length}});break}}else O({slideData:{slide:t[0],index:1,nbSlides:t.length}})}}else{for(D=0;D<S.length;D++)void 0!==S[D].minimalFlag&&(S[D].ctx.setMinimalUIFlag(S[D].minimalFlag),S[D].ctx.setAnnotationUIVisibleFlag&&S[D].ctx.setAnnotationUIVisibleFlag(!S[D].minimalFlag));S.length=0,m&&(m.setReviewPlayMode(M),m.setNavBarVisibleFlag(!0)),f&&(f.setReadOnly(b),!b&&f.getCurrentSessionMarkup()&&f.getCurrentSessionMarkup().getCurrentSlide()&&f.getCurrentSessionMarkup().getCurrentSlide().getPointedFormats().length&&f.getCurrentSessionMarkup().getCurrentSlide().getPointedFormats()[0].setReadOnly(!0)),c.getViewer().setManipulators(v)}}),this.clean=function(){t.unreferenceSlideShowController({context:c.getFrameWindow(),id:"DMU3DMarkupSlideShow"}),p=null},this.getActiveState=function(){return p?p.getActiveState():null},this.setActiveState=function(e){p&&p.setActiveState(e)},this.subscribe=function(e,t){return p?p.subscribe(e,t):null},this.unsubscribe=function(e){p&&p.unsubscribe(e)}}})}),define("DS/DMUPlaySlide/DMUFormat",["require","exports","DS/Visualization/Node3D","DS/DMUPlaySlide/DMUSlideImport","DS/DMUBaseExperience/DMUContextManager","DS/DMUBaseExperience/DMUObject","DS/DMUBaseExperience/EXPUtils"],function(e,t,i,n,a,l,r){"use strict";let s=[{name:"sType",type:"string",JSONname:"type",defaultValue:"Format"},{name:"sID",type:"string",JSONname:"id"},{name:"sUuid",type:"string",JSONname:"uuid"},{name:"sName",type:"string",JSONname:"Name"},{name:"sDescription",type:"string",JSONname:"Description"},{name:"sImageType",type:"string",JSONname:"imgData.type"},{name:"sImageData",type:"string",JSONname:"imgData.data"},{name:"bInWork",type:"boolean",JSONname:"InWork"},{name:"lDMUObjects",type:"listObjects",JSONname:"DMUObjects",defaultValue:[]},{name:"mRelativePosMatrix",type:"Matrix4",JSONname:"RelativePosMatrix"},{name:"sPointedReference",type:"string",JSONname:"PointedReference"},{name:"sImageURL",type:"ImageURL",JSONname:"ImageURL"}];return class extends l{constructor(e,t){super(e,t),this.addParameters(s);let l,o=this,g={},d=[],u="DS/DMUFormatEdition/DMUFormatContextualBar";this.initAuthoringData=function(){-1!==a.getAuthoringFeatureTypes().indexOf("Slide")&&(-1===a.getAuthoringFeatureTypes().indexOf("Slide")||o.getContextualMenuCommandList||window.require([u],function(e){l=new e,o.getContextualMenuCommandList=l.getContextualMenuCommandList,o.getSharedContextualMenuCommandList=l.getSharedContextualMenuCommandList,l.register({frmWindow:o.getFrameWindow(),type:o.getType()})}))},this.removeAuthoringData=function(){-1!==a.getAuthoringFeatureTypes().indexOf("Slide")&&(o.getSharedContextualMenuCommandList=null,o.getContextualMenuCommandList=null,l&&l.unregister({frmWindow:o.getFrameWindow(),type:o.getType()}))};let D=new n(o);this.importData=D.importData,this.afterImport=D.afterImport,this.initAuthoringData();let c=new i(this);c.getDMUType=this.getType,this.setNode(c),this.getPreview=function(){let e=o.getAttribute("sImageURL");if(e)return e;let t=o.getAttribute("sImageType"),i=o.getAttribute("sImageData");return t&&i?r.getImageDataUrl(t,i):void 0};let m=this.remove;this.remove=function(){let e,t=o.getAttribute("lDMUObjects")||[];for(e=t.length-1;e>=0;e--)t[e].getNode()&&o.getNode().removeChild(t[e].getNode());if(m(),d)for(e=0;e<d.length;e++)d[e].state={},d[e].target.refreshNode();D=d=g=null},this.areSectionPlanesDisplayed=function(){let e;if(o.getActiveFlag()&&o.isVisible()&&o.isDisplayed()){let t=o.getDMUObjects("DMUSection");for(e=0;e<t.length;e++)if(t[e].isSectionDisplayed())return!0}return!1};let f,p=this.refreshNode;this.refreshNode=function(){let e;if(o.getNode()&&!t.isImporting()){o.getNode().setName(o.getAttribute("sName"));let t=o.getActiveFlag()&&o.isDisplayed()&&o.isVisible();if(o.getNode().setVisibility(t),o.getNode().setVisibilityFree(!t),d){for(e=0;e<d.length;e++)d[e].target.refreshNode();o.fireEvent("onDMUSlideOrFormatOverloadsRefreshRequired",{dmuObject:o})}let i=o.getAttribute("lDMUObjects")||[];for(e=0;e<i.length;e++)(t||"DMUSection"===i[e].getType()||-1!==i[e].getType().indexOf("DMUCompare"))&&i[e].refreshNode();o.fireEvent("onDMUSlideOrFormatRefreshRequired",{dmuObject:o}),p()}},this.addDMUObject=function(e){o.set({attribute:"lDMUObjects",value:e,mode:"addValue"}),e.getNode()&&o.getNode().addChild(e.getNode()),"DMUMarker3DSpline"===e.getType()&&g&&g.state&&e.updateProjectionPlaneDefinition({up:g.state.vViewpointUp,right:g.state.vViewpointRight}),e.setDisplayedFlag(o.isDisplayed()),o.fireEvent("onDMUObjectAddedInObject",{dmuObject:e,parentObject:o})},this.getDMUObjects=function(e){let t=[],i=o.getAttribute("lDMUObjects")||[];for(let n=0;n<i.length;n++)!e||e&&i[n]&&i[n].getType()===e?t.push(i[n]):i[n].getDMUComments&&"DMUComment"===e&&(t=t.concat(i[n].getDMUComments()));return t},this.removeDMUObject=function(e){let t=o.getAttribute("lDMUObjects")||[];for(let i=0;i<t.length;i++)if(t[i]===e)return e.getNode()&&o.getNode().removeChild(e.getNode()),t.splice(i,1),o.setAttribute("lDMUObjects",t),e.remove(),void o.fireEvent("onDMUObjectRemovedFromObject",{dmuObject:e,parentObject:o})},this.get3DMarkers=function(){let e=o.getDMUObjects();for(let t=e.length-1;t>=0;t--)-1===e[t].getType().indexOf("Marker3D")&&-1===e[t].getType().indexOf("Measure")&&e.splice(t,1);return e},this.get2DMarkers=function(){let e=o.getDMUObjects();for(let t=e.length-1;t>=0;t--)-1===e[t].getType().indexOf("Marker2D")&&e.splice(t,1);return e},this.setSlideApplicativeContext=function(e,t){e&&(f||(f={}),f[e]=t,o.makeDirty(),o.fireEvent("onDMUObjectAttributeModified",{dmuObject:o,attrName:"oApplicativeContext",value:f}))},this.getSlideApplicativeContext=function(e){if(f)return e?f[e]:f};let h=this.setReadOnly;this.setReadOnly=function(e,t){let i,n=o.getDMUObjects();for(i=0;i<n.length;i++)n[i].setReadOnly(e,t);h(e)},this.addObjectOverload=function(e){if(e&&e.target){let t=!1;if(d){for(let i=0;i<d.length;i++)if(d[i].target===e.target){d[i].state=e.state,t=!0;break}t||d.push({target:e.target,state:e.state})}o.makeDirty(),o.fireEvent("onDMUObjectAttributeModified",{dmuObject:o,attrName:"oOccurrenceOverload",value:d})}},this.getObjectOverloads=function(){return d||[]},this.removeObjectOverload=function(e){if(d){let t=d.indexOf(e);if(t>=0){let e=d[t].target;d.splice(t,1),o.fireEvent("onDMUObjectAttributeModified",{dmuObject:o,attrName:"oOccurrenceOverload",value:d}),o.getParent().removeOccurrenceOverloaderIfNeeded(e)}}},this.updateEnvironmentOverload=function(e){g=e,o.getDMUObjects("DMUMarker3DSpline").forEach(t=>{t.updateProjectionPlaneDefinition({up:e.state.vViewpointUp,right:e.state.vViewpointRight})}),o.makeDirty(),o.fireEvent("onDMUSlideEnvironmentOverloadChanged",{dmuObject:o,value:e})},this.getEnvironmentOverload=function(){return g||{}};let b=this.setActiveFlag;this.setActiveFlag=function(e){if(e===o.getActiveFlag())return;let t=o.getDMUObjects();for(let i=0;i<t.length;i++)t[i].setActiveFlag(e);b(e)};let M=this.setVisibleFlag;this.setVisibleFlag=function(e){if(e===o.isVisible())return;let t=o.getDMUObjects();for(let i=0;i<t.length;i++)t[i].setVisibleFlag(e);M(e)};let v=this.setDisplayedFlag;this.setDisplayedFlag=function(e){if(e===o.isDisplayed())return;let t=o.getDMUObjects();for(let i=0;i<t.length;i++)t[i].setDisplayedFlag(e);v(e)},this.getDMUObjectFromPathElement=function(e){if(!e)return;let t=e.getLastElement(!0);if(o.getNode()===t)return o;let i,n=o.getAttribute("lDMUObjects")||[];for(let a=0;a<n.length;a++){if(n[a].getNode&&n[a].getNode()===t)return n[a];if(n[a].getDMUObjectFromPathElement&&(i=n[a].getDMUObjectFromPathElement(e)))return i}},this.getOverloadForTarget=function(e){if(g&&g.target===e)return g;if(d&&d.length)for(let t=0;t<d.length;t++)if(d[t].target===e)return d[t]};let S=this.setAttribute;this.setAttribute=function(e,t){"sImageData"===e&&S("sImageURL",null),S(e,t)}}}}),define("DS/DMUPlaySlide/DMUSlide",["require","exports","DS/DMUPlaySlide/DMUFormat"],function(e,t,i){"use strict";let n=[{name:"sPointedFormats",type:"listStrings",JSONname:"PointedFormats"},{name:"sOwner",type:"string",JSONname:"Owner"},{name:"fCreationDate",type:"float",JSONname:"CreationDate"},{name:"sParentSlideID",type:"string",JSONname:"ParentSlide"}];return class extends i{constructor(e,t){super(e,t),this.addParameters(n),this.setAttribute("sType","Slide");let i=this,a=[],l=this.remove;this.remove=function(){if(i.setDisplayedFlag(!1),a){for(let e=0;e<a.length;e++)a[e].setDisplayedFlag(!1),a[e].setReadOnly(!1,!0),a[e].refreshNode();a.length=0}l()};let r,s=this.setAttribute;this.setAttribute=function(e,t){if("sPointedFormats"===e){let n=i.getPointedFormats().slice();a=null;for(let e=0;e<n.length;e++)n[e].setDisplayedFlag(!1),n[e].setReadOnly(!1,!0),n[e].refreshNode();a=[],s(e,t),i.fireEvent("onDMUSlideFormatListModified",{dmuObject:i})}else s(e,t)},this.getPointedFormats=function(){if(!a)return[];let e=i.getAttribute("sPointedFormats");if(!a.length&&e&&e.length){let i=t.getFormats();for(let t=0;t<e.length;t++)for(let n=0;n<i.length;n++)if(i[n].getAttribute("sID")===e[t]){a.push(i[n]);break}}return a},this.setAssociatedHighlightData=function(e){r=e,i.fireEvent("onDMUSlideAssociatedToHighlight",{dmuObject:i,data:r})},this.getAssociatedHighlightData=function(){return r};let o=this.refreshNode;this.refreshNode=function(){if(i.getNode()&&!t.isImporting()){let e=i.getPointedFormats();if(e&&e.length){for(let t=0;t<e.length;t++)e[t].setDisplayedFlag(i.isDisplayed()),e[t].setVisibleFlag(i.isVisible()),e[t].setReadOnly(i.isVisible()&&i.isDisplayed(),!0),e[t].refreshNode();i.update3DFormatDisplayed()}o()}},this.areSectionPlanesDisplayed=function(){let e;if(i.getActiveFlag()&&i.isVisible()&&i.isDisplayed()){let t=i.getDMUObjects("DMUSection");for(e=0;e<t.length;e++)if(t[e].isSectionDisplayed())return!0;let n=i.getPointedFormats();if(n&&n.length)for(let t=0;t<n.length;t++){let i=n[t].getDMUObjects("DMUSection");for(e=0;e<i.length;e++)if(i[e].isSectionDisplayed())return!0}}return!1},this.update3DFormatDisplayed=function(){let e=i.getActiveFlag()&&i.isVisible()&&i.isDisplayed(),t=i.getPointedFormats();if(t&&t.length)for(let n=0;n<t.length;n++){let a,l=i.getDMUObjects("DMUSection"),r=t[n].getDMUObjects("DMUSection"),s=i.getDMUObjects("DMUCompare3D").concat(i.getDMUObjects("DMUCompare2D")),o=t[n].getDMUObjects("DMUCompare3D").concat(t[n].getDMUObjects("DMUCompare2D")),g=!1,d=!1;if(r.length){if(e&&l.length)for(a=0;a<l.length;a++)if(l[a].isSectionDisplayed()){g=!0;break}for(a=0;a<r.length;a++)r[a].setDisplayedFlag(!g&&t[n].isDisplayed()),r[a].setVisibleFlag(!g&&t[n].isVisible()),r[a].refreshNode()}if(o.length){if(e&&s.length)for(a=0;a<s.length;a++){d=!0;break}for(a=0;a<o.length;a++)o[a].setDisplayedFlag(!d&&t[n].isDisplayed()),o[a].setVisibleFlag(!d&&t[n].isVisible()),o[a].refreshNode()}}};let g=this.setDisplayedFlag;this.setDisplayedFlag=function(e){if(e===i.isDisplayed())return;g(e);let t=i.getPointedFormats();if(t&&t.length)for(let i=0;i<t.length;i++)t[i].setDisplayedFlag(e);i.update3DFormatDisplayed()};let d=this.setVisibleFlag;this.setVisibleFlag=function(e){if(e===i.isVisible())return;d(e);let t=i.getPointedFormats();if(t&&t.length)for(let i=0;i<t.length;i++)t[i].setVisibleFlag(e);i.update3DFormatDisplayed()}}}});