/*!  Copyright 2016 Dassault Systemes. All rights reserved. */
define("DS/CAT3DAnnotationModelLoader/CAT3DAnnotationLoader",["UWA/Class","UWA/Core","DS/Visualization/Node3D","DS/Visualization/LoaderUtils","DS/Visualization/ThreeJS_DS","DS/SceneGraphOverrides/GenericOverride","DS/Visualization/PathElement","DS/CATWebUXPreferences/CATWebUXPreferencesUtils","DS/CAT3DAnnotationModel/CAT3DAnnotationNode","DS/CAT3DAnnotationModel/CAT3DAnnotationViewNode","DS/CAT3DAnnotationModel/CAT3DAnnotationSetNode","DS/CAT3DAnnotationModel/CAT3DAnnotationRootNode","DS/CAT3DAnnotationModel/CAT3DAnnotationCaptureNode","DS/CAT3DAnnotationModel/CAT3DAnnotationDIFCaptureNode","DS/CAT3DAnnotationModel/CAT3DAnnotationAttributeNode"],function(e,t,n,o,i,a,r,d,l,s,f,h,g,c,u){"use strict";return e.extend({init:function(e){var p=e||{};this._parent(p);var A=this,v=function(e){if(e.getNodeType&&"Mesh3D"===e.getNodeType())return e;for(var t=e.children,n=0;n<t.length;n++){var o=v(t[n]);if(o)return o}},D=function(e){e.setPickParent&&e.setPickParent(!0);for(var t=e.children,n=0;n<t.length;n++)D(t[n])},S=function(e,t,n){t&&8===t.getAnnotationSupertype()||e.disableClipping(!0),n&&n.textureInBlack&&e.whiteVectorInBlack&&e.whiteVectorInBlack();var o=v(e);if(o&&(o.setShadow(!1,!1),o.setSSAO(!1),o.setVisibilityInTree(!1)),D(e),t){var i=e.children[0].isVisible();e.children[0].setVisibility(!0);var a=e.getBoundingSphere();(a.radius>0||a.pixelRadius>0)&&(t.addChild(e),t.setVisibility(i))}};function m(e){if(!e)return;let t=e.createOverrideSetManager(),n=t.createSceneGraphOverrideSet();t.pushSceneGraphOverrideSet(n);let o=n.createOverride(new r([e]));o&&(o.setColor(a.NO_COLOR,!0),o.setOpacity(a.NO_OPACITY,!0),o.resetMaterial())}function L(e,t,i){if(t[e]&&t[e].reps){var a=new n(t.name+"_"+e);i&&a.disableClipping(!0),o.processData([a],t[e],void 0,!0);var r=v(a);return r&&(r.setVisibilityInTree(!1),r.setShadow(!1,!1),r.excludeFromHighlight(!0,!0),r.setNoZ(!1)),m(a),a}}this.createAnnotationSet=function(e,n){for(var o=new f(t.extend(n,{onAnnotationAttrModifiedCB:e},!0)),i=0;i<12;i++){var a=new h({rootType:i+1});a.setVisibility(i+1!==11),o.addChild(a)}return m(o),o},this.postProcessViewsAndCaptures=function(e){var t,n,o,i=e.getRootAnnotationNode("RootCaptures"),a=e.getRootAnnotationNode("RootViews");if(a&&a.children.length)for(t=0;t<a.children.length;t++){var r=(o=a.children[t]).getMultiSectionData?o.getMultiSectionData():null;if(r&&r.views)for(n=0;n<r.views.length;n++)a.children[r.views[n]-1].setIsSubView(!0)}if(i&&i.children.length&&a&&a.children.length)for(t=0;t<i.children.length;t++){var d=i.children[t],l=d.getPointedView();if(l)for(n=0;n<a.children.length;n++)(o=a.children[n]).getAnnotationID()===l&&o.setAssociatedCapture(d.getAnnotationID())}},this.buildAnnotVisualization=function(e){if(e&&e.buildVisualization){var t=e.getAttributeParam();"4"===e.getAttributeCategory()?14!==e.getAttributeType()&&32!==e.getAttributeType()||t&&t.length&&t.forEach(function(t){"ANGLE"===t.Magnitude&&(t.Nls=d.convertValueToString(t.Value,"Angle",{displayUnit:14!==e.getAttributeType()})),"LENGTH"===t.Magnitude&&(t.Nls=d.convertValueToString(t.Value,"Length",{displayUnit:!0,displaySpace:!0}))}):"6"===e.getAttributeCategory()&&(4!==e.getAttributeType()&&5!==e.getAttributeType()&&6!==e.getAttributeType()||t&&t.length&&t.forEach(function(e){"LENGTH"===e.Magnitude&&(e.Nls=d.convertValueToString(e.Value,"Length",{displayUnit:!0,displaySpace:!0}))})),e.buildVisualization(t)}},this.loadAnnotation=function(e){var t,a,r,d=function(t,n,o){var i=Object.keys(t);if(i.length){for(var a,r={category:o,onAnnotationAttrModifiedCB:"TPSAttribute"===t.nodeXMLtype?e.cb:function(){}},l=0;l<i.length;l++)"childrenAttr"===i[l]?a=t[i[l]]:r[i[l]]=t[i[l]];var s=new u(r);n.addChild(s);var f=[];if("6"===s.getAttributeCategory()&&31===s.getAttributeType()){var h=s.getAttributeParam();h&&h.length&&(f=h.slice()).push({Name:"CSName",Value:s.getAnnotationName()})}if(a&&a.length)for(var g=0;g<a.length;g++)a[g].params=a[g].params?a[g].params.concat(f):f,d(a[g],s,o);A.buildAnnotVisualization(s)}};if(6!==e.nodeInfo.type){if("AnnotationSet"===e.nodeInfo.nodeXMLtype){if((e.nodeInfo.standards||e.nodeInfo.standardNorms)&&(e.annotationSetNode.getSemanticStandards=function(){return{standards:e.nodeInfo.standards,defaultSpecs:e.nodeInfo.standardNorms,stdName:e.nodeInfo.standard}}),e.nodeInfo.hyperlinksData&&e.nodeInfo.hyperlinksData.length){var f=[];for(t=0;t<e.nodeInfo.hyperlinksData.length;t++)e.nodeInfo.hyperlinksData[t].type>0&&2&e.nodeInfo.hyperlinksData[t].type&&f.push(e.nodeInfo.hyperlinksData[t]);f.length&&(e.annotationSetNode.getLinkedDataRelatedInfos=function(){return f})}}else if("TPSAttribute"===e.nodeInfo.nodeXMLtype||"TPSAttributes"===e.nodeInfo.nodeXMLtype){if(!e.nodeInfo.tpsID){var h=e.annotationSetNode.getRootAnnotationNode("RootAttributes").children,c=0;for(t=0;t<h.length;t++)e.annotationSetNode.getRootAnnotationNode("RootAttributes").children[t].isRootAttribute()&&c++;e.nodeInfo.isRootAttribute=!0,e.nodeInfo.type=47,e.nodeInfo.tpsID="root_"+c}d(e.nodeInfo,e.annotationSetNode.getRootAnnotationNode("RootAttributes"),e.nodeInfo.category)}else{var p=Object.keys(e.nodeInfo);if(p.length){for(var v={onAnnotationAttrModifiedCB:e.cb,reps:[]},D=0;D<p.length;D++)if("cappingRep"===p[D])v.cappingRep=L("cappingRep",e.nodeInfo,!0);else if("surfaceNormalRep"===p[D])v.surfaceNormalRep=L("surfaceNormalRep",e.nodeInfo);else if("planeInfos"===p[D]){var m=e.nodeInfo.planeInfos.direction;v.viewDirection=new i.Vector3(m.x,m.y,m.z);var b=e.nodeInfo.planeInfos.normal;v.viewNormal=new i.Vector3(b.x,b.y,b.z);var I=e.nodeInfo.planeInfos.origin;v.viewOrigin=new i.Vector3(I.x,I.y,I.z)}else v[p[D]]=e.nodeInfo[p[D]];if(r=-1,"TPS"===e.nodeInfo.nodeXMLtype||"TPSCG"===e.nodeInfo.nodeXMLtype?r=(a=new l(v)).getAnnotationSupertype()+1:"TPSView"===e.nodeInfo.nodeXMLtype?(r=0,a=new s(v)):"TPSCap"===e.nodeInfo.nodeXMLtype&&(r=1,a=new g(v)),-1===r||!e.annotationSetNode||!e.annotationSetNode.children[r]||(!(O=a)||null===O.getAnnotationType()||void 0===O.getAnnotationType()||isNaN(O.getAnnotationType())||"tps"===O.getAnnotationClassType()&&(O.getAnnotationType()>55||O.getAnnotationType()<=0))||(e.annotationSetNode.children[r].addChild(a),e.nodeInfo.rootIndex=r),-1===e.nodeInfo.nodeXMLtype.indexOf("TPSAttribute")&&(void 0!==e.nodeInfo.toleranceZoneValue&&(a.getAnnotationToleranceZoneValue=function(){return e.nodeInfo.toleranceZoneValue}),void 0!==e.nodeInfo.dimensionLimits&&(a.getAnnotationDimensionLimits=function(){return e.nodeInfo.dimensionLimits}),void 0!==e.nodeInfo.genTol&&(a.getGeneralTolerance=function(){return e.nodeInfo.genTol}),void 0!==e.nodeInfo.tabLimit&&(a.getTabulatedLimit=function(){return e.nodeInfo.tabLimit}),e.nodeInfo.hyperlinksData&&e.nodeInfo.hyperlinksData.length)){var C=[];for(t=0;t<e.nodeInfo.hyperlinksData.length;t++)e.nodeInfo.hyperlinksData[t].type>0&&2&e.nodeInfo.hyperlinksData[t].type&&C.push(e.nodeInfo.hyperlinksData[t]);C.length&&(a.getLinkedDataRelatedInfos=function(){return C})}e.nodeInfo.reps&&(T=e.nodeInfo,y=e.processText,x=e.annotationSetNode.getContextType(),P=a,M={textureInBlack:"false"!==localStorage.getItem("3DAnnotWhiteVectorAsBlack"),edgeTransparency:1!==x&&2!==x&&3!==x,processText:y},T.reps[0]&&T.reps[0].reps&&(N=new n,o.processData([N],T.reps[0],void 0,!0,M,function(){S(N,P,M)})),T.reps[1]&&T.reps[1].reps&&(R=new n,o.processData([R],T.reps[1],void 0,!0,M,function(){S(R,P,M)})))}}var T,y,x,P,N,R,M,O;if("TTRS"===e.nodeInfo.nodeXMLtype)e.annotationSetNode.addTTRS(e.nodeInfo);else if("RefFrame"===e.nodeInfo.nodeXMLtype)e.annotationSetNode.addReferenceFrame(e.nodeInfo);else if(a){if(void 0!==e.nodeInfo.val){var E=a.getAnnotationSupertype();1!==E&&2!==E&&8!==E&&9!==E&&(a.getAnnotationToleranceValue=function(){return e.nodeInfo.val})}void 0!==e.nodeInfo.trep&&(a.getAnnotationTTRSRep=function(){return e.nodeInfo.trep}),void 0!==e.nodeInfo.ref&&(a.getAnnotationReferenceFrame=function(){return e.nodeInfo.ref}),void 0!==e.nodeInfo.def&&(a.isDefaultAnnotation=function(){return 0!==e.nodeInfo.def}),void 0!==e.nodeInfo.grou&&(a.getGeometricalAttachmentList=function(){return e.nodeInfo.grou}),void 0!==e.nodeInfo.datumTargets&&(a.getDatumTargetList=function(){return e.nodeInfo.datumTargets}),void 0!==e.nodeInfo.associatedBasicDims&&(a.getAssociatedBasicDims=function(){return e.nodeInfo.associatedBasicDims}),void 0!==e.nodeInfo.auxiliaryFeatures&&(a.getAuxiliaryFeatures=function(){return e.nodeInfo.auxiliaryFeatures})}}},this.loadDIFAnnotation=function(e){if(e&&e.infos&&e.type&&e.annotSet&&"DIFCapture"===e.type){let t=e.annotSet.getRootAnnotationNode("RootCaptures");t&&(S(e.infos.visuinfo.node3D),t.addChild(new c({name:e.infos.Name,uuid:e.infos.ID||e.infos.iD,representation:e.infos.visuinfo.node3D,plane:e.infos.visuinfo.plane,up:e.infos.visuinfo.up})))}}}})}),define("DS/CAT3DAnnotationModelLoader/CAT3DAnnotationModelLoader",["UWA/Class","UWA/Utils","DS/Visualization/ThreeJS_DS","DS/Visualization/AnnotationServices","DS/WebappsUtils/WebappsUtils","DS/Visualization/Utils","DS/CoreEvents/ModelEvents","DS/PADUtils/views/PADAlert","DS/PADUtils/views/PADProgressBar","DS/PADUtils/PADSettingsMgt","DS/Visualization/PathElement","DS/CAT3DAnnotationUtils/CAT3DAnnotationStorageController","DS/CAT3DAnnotationUtils/CAT3DAnnotationUtils","DS/CAT3DAnnotationModelLoader/CAT3DAnnotationLoader","DS/CATWebUXPreferences/CATWebUXPreferencesManager","DS/DibUDLWebLoader/CATDibUDLWebLoaderForAnnotationSet","DS/PADUtils/PADUtilsServices","i18n!DS/CAT3DAnnotationModelLoader/assets/nls/CAT3DAnnotationModelLoader"],function(e,t,n,o,i,a,r,d,l,s,f,h,g,c,u,p,A,v){"use strict";var D=e.extend({init:function(e){this._parent(e);var D,S=this,m=new r,L=0,b=e.ctxViewer,I=e.favoriteCtxManager,C=null,T=new o,y=[],x=null,P=0,N=!1;let R=0;var M=[],O=[],E={},w=[],V=[],F=[],k=[],B=[],U=[],W=[];let X=[],G=[];var _,z,q=[],j={},Z=0,H=0,J=!1,Y=!1,K=[],Q={};function $(e,t){if(!t.length)return e;if(e)for(var n=e.getChildrenPathes(),o=0;o<n.length;o++)if(g.getNodeID(n[o].getLastElement(!0))===t[0])return $(n[o],t.splice(1,t.length-1))}function ee(e,t){if(e&&e.length){for(var n=[],o=e.length-1;o>=0;o--)-1!==U.indexOf(e[o])&&(n.push(e[o]),U.splice(U.indexOf(e[o]),1));if(n.length){var i=b.getController();n.forEach(function(e){i.unlockRepresentationMeshInRAM(e)}),i.unlockNodes({ids:e}),i.setForceReferencesLoad({data:e.map(function(e){return{id:e,state:!1}})})}}t&&t()}function te(e,t,n){return new Promise(o=>{let i=0;const a=function(t){(i+=t)===e.length&&o()},r=function(e){if(e&&e.ignoredIds&&e.ignoredIds.length){let t=[];e.ignoredIds.forEach(function(e){let n=O.indexOf(e);-1!==n&&O.splice(n,1),t.push(e)}),ee(t),a(t.length)}},d=function(e){if(L&&e&&e.node){let t=g.getNodeID(e.node);if(t){let o=O.indexOf(t);-1!==o&&O.splice(o,1),e.xml?(!E[t]||E[t]&&!E[t].embeddedSetLoaded)&&(E[t]||(E[t]={embeddedSetLoaded:!0}),Te(e.node,e.xml,n).then(function(){a(1)})):(ee([t]),a(1))}}};"refresh"===t?b.getController().refreshGeometry({ids:e,applicativeContainers:{V4V5_FDT:{errorCallBack:d,doneCallback:d}},onComplete:r,onFailure:r,onTimeout:r}):b.getController().switchNodeVisuStreamOnDemand({data:e.map(function(e){return{node:b.getController().getNode({id:e}),stream:"cgr"}}),applicativeContainers:{V4V5_FDT:{errorCallBack:d,doneCallback:d}},callbacks:{onURLLoad:function(){},onComplete:r,onFailure:r,onTimeout:r}})})}function ne(e,t){var n=[];if(e.externalPath.forEach(function(e){e.treeOrder||!g.isInstance(e)&&!g.isRepInstance(e)||n.push(g.getNodeID(e))}),n.length&&"3DXML"!==g.getLoadedAssetType()){var o=function(){R--,t&&t()},i=["TreeOrder","ro.plminstance.V_treeorder"];R++,b.getController().getAttributes({ids:n,attributes:i,callbacks:{onComplete:function(e){if(L){for(var t,a=0;a<n.length;a++)(t=e[n[a]][i[0]]?e[n[a]][i[0]]:e[n[a]][i[1]])&&(b.getController().getNode({id:n[a]}).treeOrder=parseFloat(t));o()}else R--},onFailure:o,onTimeout:o}})}else t&&t()}this.setActiveState=function(e){if(T&&e!==L&&(0===L||1===L||2===L)){if(L=e,Y=!0,Ce(),1===e||2===e){C||(C=new c),Ie(),j.onRemoveRoot||(j.onRemoveRoot=b.subscribe({event:"onRootRemoved"},ge),j.on3DPlayAssetChanged=b.subscribe({event:"on3DPlayAssetChanged"},function(){ge({path:b.getRootBagPath()})}),j.onRootAttached=b.subscribe({event:"onRootAttached"},ce),j.onRootDetached=b.subscribe({event:"onRootDetached"},ue),j.onHide=b.subscribe({event:"onHide"},ve),j.onShow=b.subscribe({event:"onShow"},De),j.onRefreshBegin=b.subscribe({event:"onRefreshBegin"},Se),j.onRefreshCompleted=b.subscribe({event:"onRefreshCompleted"},me)),1!==e||j.onAddRoot||(I&&(H=I.subscribe({event:"onFavoriteContextLoading"},le)),j.onAddRoot=b.subscribe({event:"onRootAdded"},he),j.onLoadingComplete=b.subscribe({event:"onLoadingComplete"},Ae),Z=h.subscribe("onPreferenceModified",de),ae());var t=b.getController().getDefaultLoadModelOptions();(_=t?t.CGRWithSG:void 0)||(t?t.CGRWithSG=!0:t={CGRWithSG:!0},b.getController().setDefaultLoadModelOptions(t)),fe(),(z=u.getPreferenceManager({context:b.getFrameWindow()}))&&(Q.onUnitChanged=z.subscribe("CATWebUXPreferences_UnitsAndDecimalPlace",xe),Q.onTrailingZerosChanged=z.subscribe("CATWebUXPreferences_TrailingZeros",xe)),D=b.getViewer().antiAliasing,b.getViewer().setAntiAliasing("MSAA")}else{if(D&&D!==b.getViewer().antiAliasing&&"MSAA"===b.getViewer().antiAliasing&&b.getViewer().setAntiAliasing(D),z){for(i=Object.keys(Q),o=0;o<i.length;o++)z.unsubscribe(Q[i[o]]);u.unreferencePreferenceManager({context:b.getFrameWindow()}),z=null}if(!_){var n=b.getController().getDefaultLoadModelOptions();n||(n={}),n.CGRWithSG=void 0,b.getController().setDefaultLoadModelOptions(n)}var o;P>0&&(P=1,we());var i=Object.keys(j);for(o=0;o<i.length;o++)b.unsubscribe(j[i[o]]);if(j={},Z&&(h.unsubscribe(Z),Z=0),H&&I&&(I.unsubscribe(H),H=0),V.splice(0,V.length),w.length){for(o=w.length-1;o>=0;o--)w[o].annotationSetNode.parents[0].removeChild(w[o].annotationSetNode);var a=w.map(e=>e.annotationSetNode);w.length=0,Oe("onAnnotationSetRemoved",{nodes:a})}M.forEach(e=>{e&&e.resolve&&e.resolve()}),M.length=0,ee(U),U.length=0,k.length=0,B.length=0,G.length=0,X.length=0,E={},O.length=0,K.length=0,R=0,b.getViewer().render()}Oe("onAnnotModelLoaderActiveStateChanged",{state:e}),Y=!1}},this.clear=function(){S.setActiveState(0),b=C=T=S=m=j=null,y=V=M=null,O=E=G=X=w=B=W=null},this.getActiveState=function(){return L},this.removeDIFAnnotationSet=function({id:e}){if(w.length){let t=[];for(let n=w.length-1;n>=0;n--)if(w[n].annotationSetNode.getAnnotationUuid()===e){w[n].annotationSetNode.parents[0].removeChild(w[n].annotationSetNode),t.push(w[n].annotationSetNode);let e=g.getNodeID(w[n].fatherNode);-1!==X.indexOf(e)&&X.splice(X.indexOf(e),1),w.splice(n,1);break}t.length&&(V.length=0,Oe("onAnnotationSetRemoved",{nodes:t}))}},this.getOrderedAnnotationSetPaths=function(){var e=[];if(!L)return e;var t=b.getRootBagPath();if(0===V.length){var n=[],o=[];w.forEach(function(e){let t=e.annotationSetNode;var n,i;F.some(function(o){if(o.getLastElement(!0)===e.fatherNode)return i=o.clone(),t.parents[0]===b.getDecorationBag()?n=se(t):(n=o.clone()).addElement(t),!0}),n||(n=se(t))&&(i=t.parents[0]!==b.getDecorationBag()?n.getParentPath():se(e.fatherNode)),n&&i&&o.push({annotSetPath:n,referencePath:i})});for(var i=0;i<o.length;i++){var a=!0;for(let e=0;e<B.length;e++)if(B[e].path.isAParentOf(o[i].referencePath)){a=!1;break}a&&n.push(o[i])}var r=function(e){var t=e.getLastElement(!0);if(t.getTreeOrder?t.getTreeOrder():t.treeOrder)return!0;for(var n=e.getChildrenPathes(),o=0;o<n.length;o++){if(!0===r(n[o]))return!0}},d=function(e){for(var n=[],o=e;!o.isEqual(t);){var i=o.getLastElement(!0),a=i.getTreeOrder?i.getTreeOrder():i.treeOrder;a=a||0,n.splice(0,0,a),o=o.getParentPath()}return n},l=function(e,t){if(0===e.length)return!0;if(0===t.length)return!1;for(var n=Math.max(e.length,t.length),o=0;o<n;o++)if(e[o]!==t[o])return e[o]>t[o];return!1};o=[];for(var s=function(e){var t=!0;for(let i=0;i<n.length;i++)if(e.isEqual(n[i].referencePath)){for(let e=0;e<o.length;e++)if(o[e].annotSetPath.isEqual(n[i].annotSetPath)){t=!1;break}n[i]&&t&&o.push(n[i]);break}let i=e.getChildrenPathes();for(let e=0;e<i.length;e++)s(i[e])},f=t.getChildrenPathes(),h=0;h<f.length;h++){var g;if(o=[],r(f[h])){var c=[];for(g=0;g<n.length;g++)f[h].isAParentOf(n[g].referencePath)&&c.push({index:d(n[g].referencePath.getParentPath()),data:n[g]});var u=[];for(g=0;g<c.length;g++){var p=c[g].index,A=c[g].data;if(0===o.length)o.push(A),u.push(p);else{var v=!1;for(let e=0;e<u.length&&l(p,u[e]);e++){if(e===u.length-1){o.push(A),u.push(p),v=!0;break}if(l(u[e+1],p)){o.splice(e+1,0,A),u.splice(e+1,0,p),v=!0;break}}v||(o.splice(0,0,A),u.splice(0,0,p))}}}else s(f[h]);V=V.concat(o)}}if(V&&V.length)for(var D=0;D<V.length;D++)e.push(V[D]);return e},this.getLoadedAnnotationSets=function(){return w.map(e=>e.annotationSetNode)},this.subscribe=function(e,t){if(e&&t)return m.subscribe({event:e},t)},this.unsubscribe=function(e){m&&e&&m.unsubscribe(e)},this.isRunning=function(){return R>0},this.hasAlreadyBeenLoaded=function(e,t){return!!e&&(t?-1!==W.indexOf(e):-1!==G.indexOf(e)||-1!==X.indexOf(e))},this.setAnnotationSetFavoriteCtxPath=function(e){if(e){var t,n=!1;for(t=0;t<F.length;t++)F[t].getLastElement(!0)===e.getLastElement(!0)&&(h.removeA3EWidgetPref({path:F[t],pref:"A3EAnnotSetsLoaded"}),F[t]=e.clone(),n=!0);n||(F.push(e.clone()),ne(e)),V.length=0,S.getOrderedAnnotationSetPaths().forEach(function(t){if(e.isAParentOf(t.referencePath)){var n=t.annotSetPath.getLastElement(!0);h.saveA3EWidgetPref({path:t.annotSetPath,pref:"A3EAnnotSetsLoaded",data:{visibility:n.isVisible(),ctxType:n.getContextType()}})}})}};var oe=function(e,t,n){let o,i=!1,a=e.children;for(o=0;o<a.length;o++)a[o].getAnnotationClassType&&"tpsAnnotationSet"===a[o].getAnnotationClassType()&&(i=!0);if(!i){let o=g.getNodeID(e),i=g.isRepRef(o);if(o&&(i&&-1===G.indexOf(o)||!i&&-1===X.indexOf(o))&&g.getNodeType(e))if(g.is3DLeaf(e)){let n=!0;for(let o=0;o<t.length;o++)if(t[o]===e){n=!1;break}n&&(i?G.push(o):X.push(o),t.push(e))}else if(g.isReference(e)){let t=!0;for(let o=0;o<n.length;o++)if(n[o]===e){t=!1;break}t&&(i?G.push(o):X.push(o),n.push(e))}}if(a&&a.length)for(o=0;o<a.length;o++)oe(a[o],t,n)};function ie(e){if(!e||!L||!e.productIds)return[];let t=[];return e.productIds.forEach(e=>{t.push(new Promise(function(t){let o=function(){let n=O.indexOf(e);-1!==n&&O.splice(n,1),t()};const i=new p;i.getAnnotationSetIDAssociatedToProductID({physicalid:e,serverUrl:A.get3DSpaceUrl(),securityContext:s.getSetting("pad_security_ctx"),onComplete:t=>{if(t&&t.length){const a=[];t.forEach(t=>{a.push(new Promise(o=>{t?i.loadModel({data:{physicalid:t,dataType:"DIFAnnotationSet"},serverUrl:A.get3DSpaceUrl(),securityContext:s.getSetting("pad_security_ctx"),onComplete:()=>{let a=b.getController().getNode({id:e});if(a){let r=function(r){const d=r&&r[t]&&r[t]["ds6w:label"]?r[t]["ds6w:label"]:v.DefaultAnnotationSetName;let l,s=C.createAnnotationSet(re,{name:d,uuid:t});for(let e=0;e<F.length;e++)if(F[e].getLastElement(!0)===a){l=F[e].clone();break}if(l){for(;l&&!g.isInstance(l.getLastElement(!0));)l=l.getParentPath();l&&s.setMatrix((new n.Matrix4).copy(l.getWorldMatrix(b.getViewer())))}b.getDecorationBag().addChild(s),w.push({annotationSetNode:s,fatherNode:a}),E[e]||(E[e]={}),E[e].difSetLoaded=!0,i.getAnnotationSetMBDCaptures({onSuccess:e=>{e&&e.length&&e.forEach(e=>{C.loadDIFAnnotation({infos:e.Childs&&e.Childs.length?e.Childs[0]:e,annotSet:s,type:"DIFCapture"})}),Oe("onAnnotationSetLoaded",{partNode:a,node:s}),b.getViewer().render(),o()}})};b.getController().getAttributes({ids:[t],attributes:["ds6w:label"],callbacks:{onComplete:r,onFailure:r}})}else o()}}):o()}))}),Promise.all(a).then(o)}else o()},onFailure:o})}))}),t}this.loadFTALightModel=function(e){if(!e||!L||!e.path&&!e.idPath)return;let t,n,o,i,a,r,d,l=e.ctxTypes&&1===e.ctxTypes.length&&2===e.ctxTypes[0],f=[],h=[],c=[],u=function(e,t){return-1===G.indexOf(e)&&-1===X.indexOf(e)&&(c.push(e),t?G.push(e):X.push(e),!0)},p=function(e,t,n){if(e){let o=e.getReferenceId();u(o)&&e.isRepRef()?-1===t.indexOf(o)&&-1===O.indexOf(o)&&(O.push(o),t.push(o)):(-1===n.indexOf(o)&&-1===O.indexOf(o)&&(O.push(o),n.push(o)),e.getChildren().forEach(function(e){p(e.getReferenceIterator(),t,n)}))}},A=e.idPath?e.idPath[e.idPath.length-1]:e.path?g.getNodeID(e.path.getLastElement(!0)):null;if(!A)return;if(l&&-1!==W.indexOf(A))return;let D=g.getLoadedAssetType(),m=e.path?e.path.clone():null,I=function(){if(!m&&e.idPath){let t=$(b.getRootBagPath(),e.idPath);t&&(m=t.clone(),S.setAnnotationSetFavoriteCtxPath(m))}},C=w.length,T=[];if(!0!==s.getSetting("enopad3dviewer_lightNodeModel")||"3DXML"===D){let s=[],c=[];if(I(),m){if(t=m.getLastElement(!0),e.loadChildren)oe(t,s,c);else if(l){if(g.is3DLeaf(t))u(A)&&s.push(t);else if(g.isReference(t))for(a=t.children,i=0;i<a.length;i++)1===(r=a[i].children).length&&g.isReference(r[0])&&1===(d=r[0].children).length&&g.isRepInstance(d[0])&&g.is3DLeaf(d[0].children[0])&&u(g.getNodeID(d[0].children[0]))&&s.push(d[0].children[0])}else if(u(A))if(g.is3DLeaf(t))s.push(t);else if(g.isReference(t))for(h.push(A),O.push(A),a=t.children,i=0;i<a.length;i++)if(g.isRepInstance(a[i])){let e=a[i].children[0];u(g.getNodeID(e))&&g.is3DLeaf(e)&&s.push(e)}for(o=0;o<s.length;o++)t=s[o],n=g.getNodeID(t),"3DXML"===D?t.xml&&(T.push(Te(t,t.xml,e.ctxTypes,!0)),t.xml=null):-1===O.indexOf(n)&&(O.push(n),f.push(n));if("3DXML"!==D)for(o=0;o<c.length;o++)n=g.getNodeID(c[o]),-1===O.indexOf(n)&&(O.push(n),f.push(n))}}else{let t=b.getController().createReferenceIterator(A);if(!t)return;if(e.loadChildren)p(t,f,h);else if(l)if(t.isRepRef())u(A)&&-1===O.indexOf(A)&&(O.push(A),f.push(A));else for(a=t.getChildren(),i=0;i<a.length;i++){let e=a[i].getReferenceIterator().getChildren();1===e.length&&e[0].getReferenceIterator().isRepRef()&&(n=e[0].getReferenceIterator().getReferenceId(),u(n)&&-1===O.indexOf(n)&&(O.push(n),f.push(n)))}else if(u(A))if(t.isRepRef())-1===O.indexOf(A)&&(O.push(A),f.push(A));else for(h.push(A),O.push(A),a=t.getChildren(),i=0;i<a.length;i++)(t=a[i].getReferenceIterator()).isRepRef()&&(n=t.getReferenceId(),u(n)&&-1===O.indexOf(n)&&(O.push(n),f.push(n)))}l&&W.push(A);let y=!1;R++;let x=function(){y&&we(),R--;let e=setInterval(()=>{S?S.isRunning()||(clearInterval(e),w.length>C?Me({eventID:"success",msg:v.AnnotationSetLoadedSuccessfullyLbl}):c&&c.length&&Me({eventID:"info",msg:v.NoAnnotationSetLoadedLbl})):clearInterval(e)},100)};if(f.length)(P=f,new Promise(function(e){if(P&&P.length){for(var t=[],n=0;n<P.length;n++)-1===U.indexOf(P[n])&&t.push(P[n]);if(t.length){var o=b.getController();t.forEach(function(e){o.lockRepresentationMeshInRAM(e)}),U=U.concat(t),o.lockNodes({ids:P}),o.setForceReferencesLoad({data:P.map(function(e){return{id:e,state:!0}}),callback:e})}}})).then(()=>{I();let n="3DXML"!==D?ie({productIds:f.concat(h)}):[];n&&n.length&&(T=T.concat(n));let o=h,i=[],a=[];f.forEach(function(e){t=b.getController().getNode({id:e});let n=g.getLoadedStream(t,b);t&&g.is3DLeaf(t)&&n?"thumbnail"===n?a.push(e):i.push(e):o.push(e)}),ee(o),(i.length||a.length)&&(Ee(),y=!0,T.push(function(e,t,n){return new Promise(o=>{if(!e||!t||!e.length&&!t.length)return void o();const i=[];e.length&&i.push(te(e,"refresh",n)),t.length&&i.push(te(t,"switch",n)),Promise.all(i).then(o)})}(i,a,e.ctxTypes))),T.length?Promise.all(T).then(x):x()});else if(h.length){let e="3DXML"!==D?ie({productIds:f.concat(h)}):[];e&&e.length?Promise.all(e).then(x):x()}else x();var P},this.getDirectChildrenAnnotSets=function(e){var t,n=[],o=e.getLastElement(!0),i=S.getOrderedAnnotationSetPaths();if(g.is3DLeaf(o))for(t=0;t<i.length;t++)e.getLastElement(!0)===i[t].referencePath.getLastElement(!0)&&n.push(i[t]);else if(g.isReference(o))for(t=0;t<i.length;t++){var a=be(i[t]);a&&a.getLastElement(!0)===e.getLastElement(!0)&&n.push(i[t])}return n};var ae=function(){for(var e=b.getRoots(),t=0;t<e.length;t++)K.push({id:g.getNodeID(e[t]),counter:0});pe()},re=function(e,t){e&&e.getAnnotationClassType&&t&&"visibility"===t&&Oe("onAnnotationVisibilityModified",{node:e})},de=function(e){if("3DAnnotLoadARMPartsPref"===e.preference&&!0===e.value)for(var t=b.getRootBagPath().getChildrenPathes(),n=0;n<t.length;n++)S.loadFTALightModel({path:t[n],ctxTypes:[2]})},le=function(e){q=q.concat(e)};function se(e){let t,n=b.getDecorationBag(),o=b.getController(),i=b.getRootBagPath().getLastElement(!0);if(e){let r=e.parents[0];if(r===n)return new f([r,e]);if(F.some(function(n){if(e.getAnnotationClassType&&n.getLastElement(!0)===e.parents[0]||!e.getAnnotationClassType&&n.getLastElement(!0)===e)return t=n.clone(),e.getAnnotationClassType&&t.addElement(e),!0}),!t){let r=e,d=[],l=!1;for(;!l&&r;){if(-1!==d.indexOf(r))return;if(d.unshift(r),"3DXML"===g.getLoadedAssetType()){if(1===r.parents.length&&r.parents[0]===i){l=!0;break}r=r.parents[0]}else{let e=r.parents.filter(e=>{if(e&&(e===i||e===n||e.getAnnotationClassType||g.getNodeID(e)&&o.getNode({id:g.getNodeID(e)})))return e});for(var a=0;a<e.length;a++){if(1===e.length&&e[a]===i){l=!0;break}if(e[a].getAnnotationClassType||g.getNodeID(e[a])&&o.getNode({id:g.getNodeID(e[a])})){r=e[a];break}}}}l&&d.length&&d.unshift(i),t=new f(d)}}return t}function fe(){b.getRootBagPath().getChildrenPathes().some(function(e){return b.getController().isLargeDataStructure(g.getNodeID(e.getLastElement(!0)))&&d.displayNotification({eventID:"info",msg:v.ConsiderRefiningYourDataLbl}),!0})}var he=function(e){V.length=0,K.push({id:g.getNodeID(e.path.getLastElement(!0)),counter:0}),fe(),pe()},ge=function(e){var t,n,o=[];for(t=F.length-1;t>=0;t--)e.path.isAParentOf(F[t])&&F.splice(t,1);for(t=w.length-1;t>=0;t--)se(w[t].fatherNode)||(o.push(w[t].annotationSetNode),w[t].annotationSetNode.parents[0]===b.getDecorationBag()&&b.getDecorationBag().removeChild(w[t].annotationSetNode),w.splice(t,1));for(t=M.length-1;t>=0;t--)se(M[t].node)||(M[t].resolve&&M[t].resolve(),M.splice(t,1));for(t=y.length-1;t>=0;t--)y[t].loadedData&&!se(y[t].loadedData.node)&&(Oe("onNoAnnotationSetLoaded",{partNode:y[t].loadedData.node,loadingAlreadyStarted:!0}),y[t].loadedData.resolve(),y[t].loadedData=null,y[t].terminate(),Ie(t));if(g.getLoadedAssetType())e.path.externalPath.forEach(function(e){var t=g.getNodeID(e);-1!==(n=G.indexOf(t))&&G.splice(n,1),-1!==(n=X.indexOf(t))&&X.splice(n,1),E[t]&&delete E[t],-1!==(n=W.indexOf(t))&&W.splice(n,1),-1!==(n=k.indexOf(t))&&k.splice(n,1)});else{var i=[];for(t=G.length-1;t>=0;t--)b.getController().getNode({id:G[t]})||(-1!==(n=U.indexOf(G[t]))&&i.push(G[t]),-1!==(n=W.indexOf(G[t]))&&W.splice(n,1),E[G[t]]&&delete E[G[t]],G.splice(t,1));for(t=X.length-1;t>=0;t--)b.getController().getNode({id:X[t]})||(-1!==(n=U.indexOf(X[t]))&&i.push(X[t]),-1!==(n=W.indexOf(X[t]))&&W.splice(n,1),E[X[t]]&&delete E[X[t]],X.splice(t,1));for(t=k.length-1;t>=0;t--)b.getController().getNode({id:k[t]})||k.splice(t,1);ee(i)}V.length=0,o.length&&Oe("onAnnotationSetRemoved",{nodes:o})},ce=function(e){for(var t=0;t<B.length;t++)if(B[t].detached&&e.path.isEqual(B[t].path)){B[t].hidden?B[t].detached=!1:B.splice(t,1);break}Oe("onAnnotationParentVisibilityModified")},ue=function(e){for(var t=!1,n=0;n<B.length;n++)if(e.path.isEqual(B[n].path)){B[n].detached=!0,t=!0;break}t||B.push({path:e.path.clone(),hidden:!1,detached:!0}),Oe("onAnnotationParentVisibilityModified")};function pe(){if(K.length&&!J&&L){for(var e=b.getRootBagPath().getChildrenPathes(),t=!0===s.getSetting("enopad3dviewer_lightNodeModel"),n=K.length-1;n>=0;n--){var o,i,a=K[n],r=!t||"3DXML"===g.getLoadedAssetType()||a.counter>=10;for(o=0;o<e.length;o++)if(g.getNodeID(e[o].getLastElement(!0))===a.id){i=e[o];break}if(i){if(!r){var d=b.getController().createReferenceIterator(a.id);d&&(d.isRepRef()||d.getChildren().length)&&(r=!0)}if(r){K.splice(n,1);var l=[],f=[];for(o=0;o<q.length;o++)if(q[o].root===g.getNodeID(i.getLastElement(!0))){var c=q[o].instances&&q[o].instances.length?q[o].instances:[q[o].origin];if(t)f.push(c);else{for(var u=i.clone(),p=0;p<c.length;p++)u=Le(u,c[p]);u&&!u.isEqual(i)&&l.push(u.clone())}q.splice(o,1);break}if(l.length||f.length){for(o=0;o<l.length;o++)S.loadFTALightModel({path:l[o]});for(o=0;o<f.length;o++)S.loadFTALightModel({idPath:f[o]})}else{var A=h.getA3EWidgetAnnotSetsLoadedPref({path:i});if(A&&A.length)for(var v=0;v<A.length;v++)!1===A[v].visibility&&k.push(A[v].idPath[A[v].idPath.length-1]),S.loadFTALightModel(A[v]);else S.loadFTALightModel({path:i}),h.getPreference("3DAnnotLoadARMPartsPref")&&S.loadFTALightModel({path:i,ctxTypes:[2]})}}else a.counter++}}K.length||(q.length=0)}}var Ae=function(){L&&(K.forEach(function(e){e.counter++}),pe())},ve=function(e){for(var t,n=0;n<e.paths.length;n++)if((t=e.paths[n]).getLastElement||(t=$(b.getRootBagPath(),e.paths[n])),t){for(var o=!1,i=0;i<B.length;i++)if(t.isEqual(B[i].path)){B[i].hidden=!0,o=!0;break}o||B.push({path:t.clone(),hidden:!0,detached:!1})}Oe("onAnnotationParentVisibilityModified")},De=function(e){for(var t,n=0;n<e.paths.length;n++)if((t=e.paths[n]).getLastElement||(t=$(b.getRootBagPath(),e.paths[n])),t)for(var o=0;o<B.length;o++)B[o].hidden&&t.isEqual(B[o].path)&&(B[o].detached?B[o].hidden=!1:B.splice(o,1));Oe("onAnnotationParentVisibilityModified")},Se=function(){J=!0,ee(U),U.length=0,V.splice(0,V.length)},me=function(){J=!1,P>0&&(P=1,we()),Ce(),Ie(),pe()},Le=function(e,t){if(e){var n=e.getLastElement(!0);if(g.getNodeID(n)===t)return e;for(var o=e.getChildrenPathes(),i=0;i<o.length;i++){var a=Le(o[i],t);if(a)return a}}},be=function(e){for(var t,n=e.referencePath.clone(),o=b.getRootBagPath(),i=!1;n&&!n.isEqual(o);){if(t=n.getLastElement(!0),g.is3DLeaf(t)&&n.getParentPath().isEqual(o))return n;if(g.isReference(t)){if(2!==e.annotSetPath.getLastElement(!0).getContextType())return n;if(i)return n;i=!0}n=n.getParentPath()}},Ie=function(e){var n=i.getWebappsBaseUrl(),o=t.matchUrl(n,window.location)?null:"Passport";void 0===e&&y.splice(0,y.length),a.getWorkerBlobFromSpecs([{provider:"FILE",filename:"AmdLoader.js",serverurl:n+"AmdLoader/",requiredAuth:o},{provider:"FILE",filename:"ThreeJS_Base.js",serverurl:n+"Mesh/",requiredAuth:o},{provider:"FILE",filename:"Mesh.js",serverurl:n+"Mesh/",requiredAuth:o},{provider:"FILE",filename:"CGRFile.js",serverurl:n+"Formats/",requiredAuth:o},{provider:"FILE",filename:"EasySax.js",serverurl:n+"EasySax/",requiredAuth:o},{provider:"FILE",filename:"CAT3DAnnotationLoadWorker.js",serverurl:n+"CAT3DAnnotationWorkers/",requiredAuth:o}],function(t){if(y)if(void 0===e)for(var n=0;n<4;n++){var o=new Worker(t);o.onmessage=Re,y.push(o)}else y[e]=new Worker(t),y[e].onmessage=Re})},Ce=function(){for(var e=0;e<y.length;e++)y[e].loadedData&&y[e].loadedData.resolve(),y[e].terminate();y.splice(0,y.length),M.forEach(e=>{e&&e.resolve&&e.resolve()}),M.splice(0,M.length),O.splice(0,O.length)},Te=function(e,t,n){return new Promise(o=>{var i=setInterval(function(){if(4===y.length){clearInterval(i);for(let i=0;i<y.length;i++)if(!y[i].loadedData)return Oe("onAnnotationSetBeginLoad",{partNode:e}),y[i].loadedData={annotationSets:{},node:e,xml:t,ctxTypes:n,resolve:o},void y[i].postMessage({xmlData:t,loadingIndex:i,ctxTypes:n});M.push({annotationSets:{},node:e,xml:t,ctxTypes:n,resolve:o})}},50)})},ye=function(e){if(12!==e.children.length)return!1;for(var t=0;t<e.children.length;t++)if(0!==e.children[t].children.length)return!0;return!1};function xe(){if(C){var e=function(t){t&&t.getAnnotationClassType&&(C.buildAnnotVisualization(t),t.children.forEach(e))};w.forEach(function(t){var n=t.annotationSetNode.getRootAnnotationNode("RootAttributes");if(n)for(var o=0;o<n.children.length;o++)e(n.children[o])}),b.getViewer().render()}}var Pe,Ne,Re=function(e){if(L){var n,o,i=e.data.loadingIndex;if(void 0!==i){var a=y[i].loadedData.node;if(!a)return y[n].loadedData&&y[n].loadedData.resolve&&y[n].loadedData.resolve(),y[n].loadedData=null,y[i].terminate(),void Ie(i);var r=g.getNodeID(a);if(!0===e.data.done){var d=Object.keys(y[i].loadedData.annotationSets),l=g.getNodeID(a);if(!0===e.data.loadingCancelled){for(n=0;n<d.length;n++)o=y[i].loadedData.annotationSets[d[n]],a.removeChild(o),o.splice(o.indexOf(o),1),-1!==k.indexOf(r)&&k.splice(k.indexOf(r),1),-1!==G.indexOf(l)&&G.splice(G.indexOf(l),1),E[l]&&(E[l].difSetLoaded?E[l].embeddedSetLoaded=!1:delete E[l]);!0===e.data.error?Oe("onAnnotationSetLoadingFailed",{partNode:a}):Oe("onNoAnnotationSetLoaded",{partNode:a,loadingAlreadyStarted:!0})}else{var s=function(e){if(L){var t=v.TesselatedTextRepsMissing;e[l]["ds6w:label"]&&(t+=e[l]["ds6w:label"]+" / "),t+=o.getAnnotationName(),Oe("onAnnotationSetPartiallyLoaded",{partNode:a,node:o,message:t})}},f=function(){L&&Oe("onAnnotationSetPartiallyLoaded",{partNode:a,node:o,message:v.TesselatedTextRepsMissing+o.getAnnotationName()})};for(n=0;n<d.length;n++)if(o=y[i].loadedData.annotationSets[d[n]],C.postProcessViewsAndCaptures(y[i].loadedData.annotationSets[d[n]]),e.data.loadingPartiallyFailed)if(!y[i].loadedData.hasFailedOnce&&y[i].loadedData.xml){a.removeChild(o),w.splice(w.map(e=>e.annotationSetNode).indexOf(o),1),-1!==k.indexOf(r)&&k.splice(k.indexOf(r),1);var h=y[i].loadedData;h.hasFailedOnce=!0;var c=t.loadXml(h.xml);h.xml=t.xmlToString(c),M.push(h),window&&window.console&&window.console.warn("Error while loading annotations of : "+g.getNodeID(a)+". FTA light model is converted and reloaded.")}else Oe("onAnnotationSetPartiallyLoaded",{partNode:a,node:o});else ye(o)?e.data.hasTesseletedTextReps?Oe("onAnnotationSetLoaded",{partNode:a,node:o}):g.getLoadedAssetType()?Oe("onAnnotationSetPartiallyLoaded",{partNode:a,node:o,message:v.TesselatedTextRepsMissing+o.getAnnotationName()}):b.getController().getAttributes({ids:[l],attributes:["ds6w:label"],callbacks:{onComplete:s,onFailure:f}}):(a.removeChild(o),o.splice(o.indexOf(o),1),-1!==k.indexOf(r)&&k.splice(k.indexOf(r),1),Oe("onNoAnnotationSetLoaded",{partNode:a,loadingAlreadyStarted:!0}))}if(y[i].loadedData.resolve(),y[i].loadedData=null,M.length){var u;for(n=M.length-1;n>=0;n--)M[n]&&!M[n].node?(M[n]&&M[n].resolve&&M[n].resolve(),M.splice(n,1)):M[n]&&M[n].node&&(u=n);y[i].loadedData={node:M[u].node,annotationSets:{},hasFailedOnce:M[u].hasFailedOnce,xml:M[u].xml,resolve:M[u].resolve},y[i].postMessage({xmlData:M[u].xml,loadingIndex:i,ctxTypes:M[u].ctxTypes}),M.splice(u,1)}}else{var p=e.data.node;if(y[i].loadedData.annotationSets[e.data.annotSetID])o=y[i].loadedData.annotationSets[e.data.annotSetID];else{var A=!0;-1!==k.indexOf(r)&&(A=!1,k.splice(k.indexOf(r),1)),o=C.createAnnotationSet(re,p),A||o.setVisibility(!1),a.addChild(o),w.push({annotationSetNode:o,fatherNode:a}),y[i].loadedData.annotationSets[e.data.annotSetID]=o}p&&C.loadAnnotation({nodeInfo:p,annotationSetNode:o,processText:e.data.processText,cb:re})}b.getViewer().render()}}};function Me(e){e&&("error"===e.eventID||"warning"===e.eventID?d.displayNotification(e):(Pe&&clearTimeout(Pe),(!Ne||e&&"success"===e.eventID)&&(Ne=e),Pe=setTimeout(function(){Pe=null,S&&(S.isRunning()?Me(Ne):(d.displayNotification(Ne),Ne=null))},1e3)))}var Oe=function(e,t){if(b){var n,o=t||{};o.path=se(o.node);var i=!g.getLoadedAssetType();if("onAnnotationSetLoaded"===e||"onAnnotationSetPartiallyLoaded"===e)o.path&&o.node&&(i&&h.saveA3EWidgetPref({path:o.partNode?se(o.partNode):o.path,pref:"A3EAnnotSetsLoaded",data:{visibility:o.node.isVisible(),ctxType:o.node.getContextType()}}),V.length=0,"onAnnotationSetPartiallyLoaded"===e&&Me({eventID:"warning",msg:o.message?o.message:v.AnnotationSetPartiallyLoadedLbl}));else if("onAnnotationSetRemoved"===e){if(!J&&!Y&&i&&o.nodes&&o.nodes.length)for(n=0;n<o.nodes.length;n++)h.removeA3EWidgetPref({nodeID:g.getNodeID(o.nodes[n]),pref:"A3EAnnotSetsLoaded"});V.splice(0,V.length)}else"onAnnotationSetLoadingFailed"===e?Me({eventID:"error",msg:v.AnnotationSetLoadedFailedLbl}):"onAnnotationSetLoadingCanceled"===e?Me({eventID:"info",msg:v.AnnotationSetLoadedCanceledLbl}):"onAnnotationVisibilityModified"===e?o.path&&o.node&&"tpsAnnotationSet"===o.node.getAnnotationClassType()&&i&&(h.removeA3EWidgetPref({path:o.path,pref:"A3EAnnotSetsLoaded"}),h.saveA3EWidgetPref({path:o.path,pref:"A3EAnnotSetsLoaded",data:{visibility:o.node.isVisible(),ctxType:o.node.getContextType()}})):"onAnnotationParentVisibilityModified"===e&&(V.length=0);o&&o.path&&("onAnnotationSetLoaded"===e||"onAnnotationSetPartiallyLoaded"===e)?ne(o.path,function(){m.publish({event:e,data:o,context:S})}):m.publish({event:e,data:o,context:S})}},Ee=function(){x&&(clearTimeout(x),x=null),x=setTimeout(function(){P=1,we()},2e4),1===++P&&setTimeout(()=>{!S||P<=0||!L||N||(N=!0,l.on(v.LoadingAnnotationsLbl,function(){var e,t=[],n=[];for(e=y.length-1;e>=0;e--)if(y[e].loadedData){var o=y[e].loadedData.node,i=g.getNodeID(o);t.push(o),n.push(g.getNodeID(o));for(var a=Object.keys(y[e].loadedData.annotationSets),r=0;r<a.length;r++)o.removeChild(y[e].loadedData.annotationSets[a[r]]),-1!==k.indexOf(i)&&k.splice(k.indexOf(i),1);y[e].loadedData.resolve(),y[e].loadedData=null}for(e=M.length-1;e>=0;e--)t.push(M[e].node),n.push(g.getNodeID(M[e].node)),M[e].resolve&&M[e].resolve();for(M.length=0,Ce(),Ie(),ee(n=n.concat(O)),e=n.length;e>=0;e--)-1!==G.indexOf(n[e])&&G.splice(G.indexOf(n[e]),1),E[n[e]]&&(E[n[e]].difSetLoaded?E[n[e]].embeddedSetLoaded=!1:delete E[n[e]]);O.length=0,t.forEach(function(e){Oe("onAnnotationSetLoadingCanceled",{partNode:e})})}))},3e3)},we=function(){x&&(clearTimeout(x),x=null),--P<0&&(P=0),0===P&&N&&(l.off(),N=!1)}}}),S=[];return e.singleton({init:function(){},getAnnotationModelLoader:function(e){if(e&&e.context){for(var t=!1,n=0;n<S.length;n++)if(S[n].context===e.context){t=!0;break}return t||S.push({context:e.context,modelLoader:new D({ctxViewer:e.context,favoriteCtxManager:e.favoriteCtxManager})}),this.giveAnnotationModelLoader(e)}},giveAnnotationModelLoader:function(e){if(e&&e.context){for(var t,n,o=0;o<S.length;o++)if(S[o].context===e.context){n=S[o],t=n.modelLoader;break}if(t)return Object.freeze({setActiveState:function(e){t&&t.setActiveState(e)},getActiveState:function(){if(t)return t.getActiveState()},loadFTAModel:function(e){t&&t.loadFTALightModel(e)},removeDIFAnnotationSet:function(e){t&&t.removeDIFAnnotationSet(e)},setAnnotationSetFavoriteCtxPath:function(e){t&&t.setAnnotationSetFavoriteCtxPath(e)},hasAlreadyBeenLoaded:function(e,n){if(t)return t.hasAlreadyBeenLoaded(e,n)},getLoadedAnnotationSets:function(){if(t)return t.getLoadedAnnotationSets()},getOrderedAnnotationSetPaths:function(){if(t)return t.getOrderedAnnotationSetPaths()},getDirectChildrenAnnotSets:function(e){if(t)return t.getDirectChildrenAnnotSets(e)},isRunning:function(){if(t)return t.isRunning()},subscribe:function(e,n){if(t)return t.subscribe(e,n)},unsubscribe:function(e){t&&t.unsubscribe(e)},loadFTALightModel:function(e,n,o,i){window.console.warn("DEPRECATED API. Use loadFTAModel method instead"),t&&t.loadFTALightModel({path:e,loadChildren:o,ctxTypes:i})}})}},unreferenceAnnotationModelLoader:function(e){if(e&&e.context)for(var t=0;t<S.length;t++)if(S[t].context===e.context)return S[t].modelLoader&&S[t].modelLoader.clear(),void S.splice(t,1)}})});