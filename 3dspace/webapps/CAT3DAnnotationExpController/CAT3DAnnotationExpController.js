/*!  Copyright 2016 Dassault Systemes. All rights reserved. */
define("DS/CAT3DAnnotationExpController/CAT3DAnnotExpControllerInstance",["DS/CAT3DAnnotationController/CAT3DAnnotBaseControllerInstance","DS/Selection/CSOManager","DS/Selection/HSOManager","DS/CAT3DAnnotationCommands/controllers/CAT3DAnnotationFilterController","DS/CATWebUXSlideShow/CATWebUXSlideShowController","DS/CAT3DAnnotationAttributeController/CAT3DAnnotationAttributeController","DS/CAT3DAnnotationUtils/CAT3DAnnotationUtils","DS/Visualization/SceneGraphUtils","DS/SceneGraphOverrides/SceneGraphOverrideSet","text!DS/CAT3DAnnotationExpController/assets/json/CAT3DAnnotationExpController.json"],function(t,e,n,o,i,a,r,l,s,g){"use strict";return t.extend({init:function(t){var h=t||{};this._parent(h);var u,A,f,d,c,p,m=h.workbenchName||"CAT3DAnnotationWidgetWkb",C=h.moduleName||"CAT3DAnnotationWidget",v=h.ctxViewer,y=this,b=!0,E={},T=null,P=JSON.parse(g),S=[],L={visible:[],hidden:[]},D=[],R=function(t){for(var e,n=t.children,o=0;o<n.length&&!e;o++)if(!n[o].getAnnotationClassType)for(var i=n[o].children,a=0;a<i.length;a++)if(i[a].getPathElement&&i[a].setInternalSceneGraph){e=i[a];break}return e};function O(t){if(t){var e=t.getLastElement(!0);if(e.getAnnotationUuid&&e.getAnnotationUuid()&&"tpsAnnotationSet"!==e.getAnnotationClassType()&&"tpsRoot"!==e.getAnnotationClassType()){var n=e.getAttributeCategory(),o=e.getAnnotationUuid();E[n]||(E[e.getAttributeCategory()]={}),E[n][o]||(E[n][o]=[t].concat(y.getPointedPaths(t)))}t.getChildrenPathes().forEach(O)}}var x=[];function V(){p&&(clearTimeout(p),p=null),v&&b&&0!==Object.keys(E).length&&(p=setTimeout(function(){var t=[];x&&x.length&&x.forEach(t=>{t&&t.parents&&t.parents.length&&t.parents[0].removeChild(t)}),x=[];var o=n.get().map(function(t){if(!0===t.annotXHighlight)return t});o&&o.length&&n.remove(o),e.get().forEach(function(e){let n=[],o=e.pathElement.getLastElement(!0);o.getAnnotationUuid?(n=y.getAllRelatedPaths(e.pathElement,e.filterOpts),o.getAnnotationSurfaceNormalRep()&&(x.push(o.getAnnotationSurfaceNormalRep()),e.pathElement.getParentPath().addChild(o.getAnnotationSurfaceNormalRep()))):v.getRootBagPath().isAParentOf(e.pathElement)&&(n=y.getPointedPaths(e.pathElement)).push(e.pathElement),t=t.concat(n.map(function(t){return{pathElement:t.clone(),annotXHighlight:!0}}))}),t&&t.length&&n.add(t),v.getViewer().render()}))}function I(t){T&&S&&T.deleteOverrides(S),S.length=0;var e=n.get().map(function(t){if(!0===t.displayMode)return t});e&&e.length&&n.remove(e),t||(l.applyVisibilityToPaths(L.visible,0),L.visible.length=0,l.applyVisibilityToPaths(L.hidden,1),L.hidden.length=0),v.getViewer().render()}var w=this.onAnnotationSetLoaded;this.onAnnotationSetLoaded=function(t){w(t),t&&t.path&&t.path.getLastElement(!0)&&(O(t.path),t.path.getLastElement(!0).getRootAnnotationNode&&t.path.getLastElement(!0).getRootAnnotationNode("RootAttributes").setVisibility(!0))};var U=this.onAnnotationSetRemoved;this.onAnnotationSetRemoved=function(t){var e,n;U(t),t&&t.nodes&&t.nodes.length&&(e=t.nodes,n=function(t){if(t&&t.getAnnotationUuid&&t.getAnnotationUuid()&&"tpsAnnotationSet"!==t.getAnnotationClassType()&&"tpsRoot"!==t.getAnnotationClassType()){var e=t.getAttributeCategory(),o=t.getAnnotationUuid();E[e]&&E[e][o]&&delete E[e][o]}t&&t.children.forEach(n)},e&&e.length&&e.forEach(n))},this._getParentAnnotationSetPath=function(t){for(var e=t.clone();e&&(!e.getLastElement(!0).getAnnotationClassType||"tpsAnnotationSet"!==e.getLastElement(!0).getAnnotationClassType());)e=e.getParentPath();return e};var F=this.setActiveState;this.setActiveState=function(t){if(F(t),t){(f=v.isDefaultContextualBarVisible())&&v.setDefaultContextualBarVisibility(!1),(d=v.isOpenWithMenuAvailable())||v.setOpenWithMenuAvailability(!0),(c=v.isRobotVisible())&&v.setRobotVisibility(!1),v.getFrameWindow().getContextualUIManager().register({onContextualBarReady:function(){var t=y.getContextualCommandList();return t.length?{cmdList:t}:void 0},context:v.getCommandContext?v.getCommandContext():null,workbenchName:m,module:C,cmdPrefix:"",subscriberId:C,merge:!0});var n=i.giveSlideShowController({context:v.getFrameWindow(),id:"CAT3DAnnotationSlideShow"});n&&n.setListOfCommandsAllowed(["CAT3DAnnotationWidgetDefaultHdr"]),A||((A={}).onPostAdd=e.onPostAdd(V),A.onPostRemove=e.onPostRemove(V)),T||(T=new s(v.getRootBagPath().getLastElement(!0)),v.getViewer().getSceneGraphOverrideSetManager().pushSceneGraphOverrideSet(T))}else t||(I(),v&&(v.getViewer().getSceneGraphOverrideSetManager().removeSceneGraphOverrideSet(T),T=null,v.getFrameWindow().getContextualUIManager().unregister(C)),void 0!==f&&v.setDefaultContextualBarVisibility(f),f=void 0,void 0!==d&&v.setOpenWithMenuAvailability(d),d=void 0,void 0!==c&&v.setRobotVisibility(c),c=void 0,A&&(e.unsubscribe(A.onPostAdd),e.unsubscribe(A.onPostRemove),A=null),E={})};var G=this.clearController;function H(t,e){if(!t)return!1;if(!(e&&t.getLastElement(!0).getAttributeCategory&&e[t.getLastElement(!0).getAttributeCategory()]))return!0;if(e&&t.getLastElement(!0).getAttributeCategory&&e[t.getLastElement(!0).getAttributeCategory()])for(var n=0;n<e[t.getLastElement(!0).getAttributeCategory()].length;n++)if(e[t.getLastElement(!0).getAttributeCategory()][n].type===t.getLastElement(!0).getAttributeType())return e[t.getLastElement(!0).getAttributeCategory()][n].state;return!0}function _(t,e){for(var n=t.getChildrenPathes(),o=0;o<n.length;o++)n[o].getLastElement(!0).getAttributeCategory&&e.push(n[o].clone()),_(n[o],e)}function B(t){var e,n=[];if("rep"===t.getType()){var o=t.getPrimitives();for(e=0;e<o.length;e++)n=n.concat(THREEDS.SubElement.getFromSGNode(o[e]))}for(e=0;e<n.length;e++)if(2===n[e].getCellDimension())return!1;return!0}function N(t,e,n,o,i){if(t&&n&&e){var a=t.getLastElement(!0),r=[];if(a.getPointedFeatureIds){var l=a.getPointedFeatureIds();if(l&&l.length){var s=[];l.forEach(function(t){o&&o!==t.name||(s=s.concat(t.uuids))}),s&&s.length&&e.getChildrenPathes().some(function(t){if("RootAttributes"===t.getLastElement(!0).getAnnotationType()){var e=[];return function t(e,n,o){e&&(e.getLastElement(!0).getAnnotationUuid&&-1!==n.indexOf(e.getLastElement(!0).getAnnotationUuid())&&o.push(e.clone()),e.getChildrenPathes().forEach(function(e){t(e,n,o)}))}(t,s,e),r=r.concat(e),!0}})}}if(i){var g=[];r.forEach(function(t){if(t.getLastElement(!0).getAttributeCategory&&i[t.getLastElement(!0).getAttributeCategory()])for(var e=t.getChildrenPathes(),n=0;n<e.length;n++)for(var o=0;o<i[t.getLastElement(!0).getAttributeCategory()].length;o++){i[t.getLastElement(!0).getAttributeCategory()][o].type===e[n].getLastElement(!0).getAttributeType()&&g.push(e[n].clone());break}}),r=r.concat(g)}r.forEach(function(t){-1===n.indexOf(t)&&n.push(t)})}}function k(t){if(t){var e,o=t.paths;if(I(),o&&o.length){var i,a,r,s,g,h=[],u=[],A=[],f=[],d=[],c=[],p=o.slice(),m=[];for(o.forEach(function(e){for(var n=e.clone();n&&"tpsAnnotationSet"!==n.getLastElement(!0).getAnnotationClassType();)n=n.getParentPath();var o=[];_(e,o),o.forEach(function(t){-1===p.indexOf(t)&&p.push(t)}),o.forEach(function(e){N(e,n,p,null,t.opts)}),s=e.getLastElement(!0);var i=P.displayParentObjTypes[s.getAttributeCategory()]?P.displayParentObjTypes[s.getAttributeCategory()][s.getAttributeType()]:null,a=[];if(N(e,n,a,null,t.opts),i&&i.parentType){for(var r=e.clone();r&&r.getLastElement(!0).getAttributeType;){if(r.getLastElement(!0).getAttributeType()===parseFloat(i.parentType)){c.push(r);break}r=r.getParentPath()}d=d.concat(E[s.getAttributeCategory()][s.getAnnotationUuid()]),o.forEach(function(e){if(H(e,t.opts)){var n=e.getLastElement(!0);n.getAttributeCategory&&(d=d.concat(E[n.getAttributeCategory()][n.getAnnotationUuid()]))}}),a.forEach(function(e){if(e.getLastElement(!0).getAttributeCategory&&e.getLastElement(!0).getAttributeCategory()===s.getAttributeCategory()&&H(e,t.opts)){var n=E[e.getLastElement(!0).getAttributeCategory()][e.getLastElement(!0).getAnnotationUuid()];if("true"===i.highlightOnlyLineicReps)for(var o=0;o<n.length;o++)!n[o].getLastElement(!0).getAnnotationClassType&&B(n[o].getLastElement())&&d.push(n[o]);else d=d.concat(n)}})}else a.forEach(function(t){-1===p.indexOf(t)&&p.push(t)});var l=[];N(e,n,l,"Highlight"),l&&l.length&&l.forEach(function(e){if(e&&e.getLastElement(!0)&&e.getLastElement(!0).getAttributeCategory){var n=[];_(e,n),n.forEach(function(e){H(e,t.opts)&&(d=d.concat(E[e.getLastElement(!0).getAttributeCategory()][e.getLastElement(!0).getAnnotationUuid()]))})}})}),i=0;i<p.length;i++)(e=p[i]).getLastElement(!0).getAttributeType&&(m[e.getLastElement(!0).getAttributeCategory()]||(m[e.getLastElement(!0).getAttributeCategory()]=[]),-1===m[e.getLastElement(!0).getAttributeCategory()].indexOf(e.getLastElement(!0).getAttributeType())&&m[e.getLastElement(!0).getAttributeCategory()].push(e.getLastElement(!0).getAttributeType())),s=p[i].getLastElement(!0),h.push(s.getAnnotationUuid()),u.includes(s.getAttributeCategory())||u.push(s.getAttributeCategory());if(u.forEach(function(e){var n=E[e];if(n){var l,s,g=Object.keys(n);for(i=0;i<g.length;i++){l=-1!==h.indexOf(g[i]);var u=!0;if((s=n[g[i]])[0].getLastElement(!0).getAttributeType){var C=!1;if(t.opts&&t.opts[e]&&t.opts[e].length){var v=p.slice().concat(c);for(a=0;a<v.length;a++)if(v[a].isAParentOf(s[0])||v[a].isEqual(s[0])){for(r=0;r<t.opts[e].length;r++)if(t.opts[e][r].type===s[0].getLastElement(!0).getAttributeType()){l=t.opts[e][r].isolateElts&&m[e]&&-1!==m[e].indexOf(t.opts[e][r].type)?t.opts[e][r].state&&-1!==h.indexOf(s[0].getLastElement(!0).getAnnotationUuid()):t.opts[e][r].state,C=!0;break}break}}if(P.forceVisiblityTypes[e]&&"true"===P.forceVisiblityTypes[e][s[0].getLastElement(!0).getAttributeType()]&&(l=!0),!l&&!C)for(a=0;a<c.length;a++)if(c[a].isAParentOf(s[0])||c[a].isEqual(s[0])){u=!1;break}}if(u)for(a=0;a<s.length;a++){const t=s[a];if(t){let n=t.getLastElement(!0);if(n.getAttributeCategory&&n.getAttributeType){let e=P.filterRepresentationVisibility[n.getAttributeCategory()]?P.filterRepresentationVisibility[n.getAttributeCategory()][n.getAttributeType()]:null;if(l&&e&&n.getAnnotationAttributeNode){let i=n.getAnnotationAttributeNode();i&&i.setVisibility("true"!==e.hideOnParentSelection||o.some(e=>e.isEqual(t)))}}t.internalPath.length?"3DAnnotations"===e?l&&-1===d.indexOf(t)&&d.push(t):-1===L[l?"visible":"hidden"].indexOf(t)&&L[l?"visible":"hidden"].push(t):t.externalPath.length&&(l&&-1===A.indexOf(t)?A.push(t):l||-1!==f.indexOf(t)||f.push(t))}}}}}),f.length){var C=f.filter(function(t){return!A.some(function(e){return t.isAParentOf(e)})});if(C.length){if(!(g=T.createOverride({pathes:C})))return void window.console.error("override not correct");S.push(g),g.setVisibility(!1)}}if(A.length){if(!(g=T.createOverride({pathes:A})))return void window.console.error("override not correct");S.push(g),g.setVisibility(!0)}for(i=L.hidden.length-1;i>=0;i--)l.getVisibilityFromPath(L.hidden[i])||L.hidden.splice(i,1);for(L.hidden.length&&l.applyVisibilityToPaths(L.hidden,0),i=L.visible.length-1;i>=0;i--)l.getVisibilityFromPath(L.visible[i])&&L.visible.splice(i,1);L.visible.length&&l.applyVisibilityToPaths(L.visible,1),d.length&&n.add(d.map(function(t){return{pathElement:t,displayMode:!0}})),y._publish("onDisplayedAnnotationsOrFeaturesChanged",{paths:o})}}}this.clearController=function(){G(),v=y=null},this.getPointedPaths=function(t){var e=[];if(v.getRootBagPath().isAParentOf(t)){var n=t.getLastElement(!0);n.getAnnotationClassType&&"tpsAttribute"===n.getAnnotationClassType()&&(e=e.concat(y.getRelatedGeometryPaths(t)))}return e},this.getAllRelatedPaths=function(t,e){var n=[];if(t&&t.getLastElement(!0).getAnnotationUuid){var o,i,a=[t.clone()];"tpsAttribute"===t.getLastElement(!0).getAnnotationClassType()&&_(t.clone(),a);for(var r=0;r<a.length;r++)H(a[r],e)&&(i=a[r].getLastElement(!0),(o=E[i.getAttributeCategory()]?E[i.getAttributeCategory()][i.getAnnotationUuid()]:null)&&o.length&&(n=n.concat(o)))}return n};var M=this.onAdditionalInfosRequired;this.onAdditionalInfosRequired=function(t){return"AnnotFilter"===t.controller||"AnnotAttrBrowser"===t.controller?{getJSONModel:y._getJSONModel}:M(t)},this.disableCtxCommandsAvailability=function(t){D=!0===t?t:t.slice()};var W=this.getAnnotControllerPrototype;this.getAnnotControllerPrototype=function(){var t=W();return t.getPointedPaths=function(t){if(y)return y.getPointedPaths(t)},t.getAllRelatedPaths=function(t,e){if(y)return y.getAllRelatedPaths(t,e)},t.getRelatedGeometryPaths=function(t){if(y)return y.getRelatedGeometryPaths(t)},t.getPontingAnnotations=function(t,e){if(y)return y.getPontingAnnotations(t,e)},t.getAssociatedInformations=function(t,e){if(y)return y.getAssociatedInformations(t,null,e)},t.isolatePaths=k,t.disableCtxCommandsAvailability=y.disableCtxCommandsAvailability,t},this.getPontingAnnotations=function(t,e){var n,o,i,a,l,s=[],g=[],h=t.internalPath;for(i=0;i<h.length;i++)null!=(n=h[i].getCGMID?h[i].getCGMID():null)&&(o=h[i].getType(),s.push(n),g.push(o));for(var u=t.clone();u&&!r.getNodeID(u.getLastElement(!0));)u=u.getParentPath();if(!u)return[];var A,f=u.getChildrenPathes();for(i=0;i<f.length;i++)f[i].getLastElement(!0).getAnnotationClassType&&"tpsAnnotationSet"===f[i].getLastElement(!0).getAnnotationClassType()&&(A=f[i]);if(A){var d=A.getLastElement(!0).getTTRSs();if(d){var c=[];for(i=0;i<d.length;i++)if(d[i].geoP&&d[i].geoP.length)for(var p=0;p<d[i].geoP.length;p++){var m=d[i].geoP[p];if(e){var C=!0;for(a=0;a<m.length;a++)if(m[a]!==s[a]){C=!1;break}C&&"primitive"===g[g.length-1]&&-1===c.indexOf(d[i].tpsID)&&c.push(d[i].tpsID)}else m.equals(s)&&"primitive"===g[g.length-1]&&-1===c.indexOf(d[i].tpsID)&&c.push(d[i].tpsID)}if(c.length){var v=[],y=function(t,e){var n=t?t.getLastElement(!0):null;if(n&&n.getAnnotationClassType){var o=void 0!==n.getAnnotationTTRS?n.getAnnotationTTRS():null;if(o&&-1!==o.indexOf(e)){for(var i=!1,a=v.length-1;a>=0;a--){if(v[a].isEqual(t)){i=!0;break}v[a].isAParentOf(t)&&v.splice(a,1)}i||v.push(t)}for(var r=t.getChildrenPathes(),l=0;l<r.length;l++)y(r[l],e)}};for(l=0;l<c.length;l++){var b=A.getChildrenPathes();for(i=0;i<b.length;i++)if((o=b[i].getLastElement(!0).getAnnotationType())&&"RootViews"!==o&&"RootCaptures"!==o){var E=b[i].getChildrenPathes();for(a=0;a<E.length;a++)y(E[a],c[l])}}return v}}}return[]},this.getRelatedGeometryPaths=function(t){var e,n,o,i=[],a=t&&t.length?t:[t];for(e=0;e<a.length;e++)if(o=a[e]){var r=o.getLastElement(!0);if(!r.getAnnotationClassType)return i;var l=y._getParentAnnotationSetPath(o),s=void 0!==r.getAnnotationTTRS&&r.getAnnotationTTRS()?r.getAnnotationTTRS():[],g=void 0!==r.getAnnotationTTRSRep?r.getAnnotationTTRSRep():null;if(g&&g.length&&(s=s.concat(g)),s&&s.length){var h=l.getLastElement(!0).getTTRSs(),u=[],A=[];for(e=0;e<s.length;e++)for(n=0;n<h.length;n++)if(s[e]===h[n].tpsID){h[n].geoP&&h[n].geoP.length&&(u=u.concat(h[n].geoP)),h[n].referencedCGs&&h[n].referencedCGs.length&&(A=A.concat(h[n].referencedCGs));break}if(u.length){var f=y.getCGRPath(l);if(f){var d=f.getLastElement(!0),c=d.getPathElement(u,f);if(!c||c&&!c.length){let t=l.getLastElement(!0).getRootAnnotationNode("RootGeometries"),n=t.children;for(e=0;e<n.length;e++){if(R(n[e])&&(c=d.getPathElement(u,f))&&c.length){var p=l.clone();p.addElement(t),p.addElement(n[e]),i.push(p),i=i.concat(y.getRelatedGeometryPaths(p));break}}}else i=i.concat(c)}}if(A.length){let t=l.getChildrenPathes();for(let n=0;n<t.length;n++)if(t[n].getLastElement(!0).getAnnotationType&&"RootGeometries"===t[n].getLastElement(!0).getAnnotationType()){let o=t[n].getChildrenPathes();for(e=0;e<o.length;e++)o[e].getLastElement(!0).getAnnotationID&&-1!==A.indexOf(o[e].getLastElement(!0).getAnnotationID())&&i.push(o[e].clone())}}}}return i},this.getAnnotationControllerType=function(){return"3DAnnotationExperience"};var q=this._disableAnnotUI,j=!1;this._disableAnnotUI=function(){q();var t=o.give3DAnnotFilterController({context:v});t&&t.getActiveState()&&(j=t.getPanelVisibilityFlag(),t.setPanelVisibilityFlag(!1)),(t=a.give3DAnnotAttrBrowserController({context:v}))&&t.getActiveState()&&(u=t.getPanelVisibilityFlag(),t.setPanelVisibilityFlag(!1))};var X=this._enableAnnotUI;this._enableAnnotUI=function(){X();var t=o.give3DAnnotFilterController({context:v});t&&t.setPanelVisibilityFlag(j),void 0!==u&&(t=a.give3DAnnotAttrBrowserController({context:v}))&&t.setPanelVisibilityFlag(u)};var J=this._onCommandStartCB;this._onCommandStartCB=function(t){"CAT3DAnnotationLevelSelectorHdr"===t._id&&(b=!1,t.onEnd(function(){b=!0})),J(t)};var z=this.getAssociatedInformations;this.getAssociatedInformations=function(t,e,n){var i=e;return i||(i=o.give3DAnnotFilterController({context:v})),z(t,i,n)},this.getContextualCommandList=function(){var t,n=[];if(!v||!0===D)return n;var o=v.getRootBagPath();const i=e.get().map(t=>{if(o.isAParentOf(t.pathElement)||t.pathElement.getLastElement(!0).getAnnotationClassType)return t.pathElement}).filter(t=>{if(t)return t});if(0===i.length)return n;var a=!1,r=!1,l=!1;for(t=0;t<i.length;t++){if(!a){var s,g=y._getSelectedAnnotationsVisibilityFlag();if(!0===g||!1===g||void 0===g)s=1===i.length&&(i[0].getLastElement(!0).getAnnotationClassType&&"tpsAnnotationSet"===i[0].getLastElement(!0).getAnnotationClassType()||!i[0].getLastElement(!0).getAnnotationClassType)?g?"CAT3DAnnotationSetHideHdr":"CAT3DAnnotationSetShowHdr":g?"CAT3DAnnotationHideHdr":"CAT3DAnnotationShowHdr",n.push({line:1,name:g?"CAT3DAnnotationHideStr":"CAT3DAnnotationShowStr",hdr_list:[s]}),a=!0}var h=i[t].getParentPath(),u=i[t].getLastElement(!0);u.getAttributeCategory&&P.typesWithAdditionalInformation[u.getAttributeCategory()]&&P.typesWithAdditionalInformation[u.getAttributeCategory()][u.getAttributeType()]&&(l=!0),(u.getAnnotationClassType&&"tpsAnnotationSet"!==u.getAnnotationClassType()&&"tpsAttribute"!==u.getAnnotationClassType()||!u.getAnnotationClassType)&&1===i.length&&(r||h&&"RootBag"!==h.getLastElement(!0).name&&((u.getAnnotationClassType&&"tpsAnnotationSet"!==u.getAnnotationClassType()||-1===D.indexOf("CAT3DAnnotationLevelSelectorHdr"))&&n.push({line:1,name:"CAT3DAnnotationLevelSelectorStr",hdr_list:["CAT3DAnnotationLevelSelectorHdr"]}),r=!0)),1===i.length&&"RootBag"===h.getLastElement(!0).name&&-1===D.indexOf("RemoveRootHdr")&&n.push({name:"CAT3DAnnotationRemoveRootStr",line:1,hdr_list:["RemoveRootHdr"]})}return l&&n.push({name:"CAT3DAnnotShowAttrRelatedInfosStr",line:1,hdr_list:["CAT3DAnnotShowAttrRelatedInfosHdr"]}),n}}})}),define("DS/CAT3DAnnotationExpController/CAT3DAnnotationExpController",[],function(){});