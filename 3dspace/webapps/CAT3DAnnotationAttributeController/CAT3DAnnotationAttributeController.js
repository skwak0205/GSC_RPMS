/*!  Copyright 2016 Dassault Systemes. All rights reserved. */
/*!  Copyright 2016 Dassault Systemes. All rights reserved. */
define("DS/CAT3DAnnotationAttributeController/CAT3DAnnotationAttributeController",["UWA/Class","DS/Selection/CSOManager","DS/CoreEvents/ModelEvents","DS/CAT3DAnnotationPanels/CAT3DAnnotationAttrListTable","DS/CATWebUXComponents/CATWebUXPanel","DS/CAT3DAnnotationController/CAT3DAnnotationController","DS/CAT3DAnnotationModelLoader/CAT3DAnnotationModelLoader","DS/CAT3DAnnotationCommands/controllers/CAT3DAnnotationFilterController","DS/CAT3DAnnotationUtils/CAT3DAnnotationStorageController","DS/WebappsUtils/WebappsUtils","DS/CATWebUXPreferences/CATWebUXPreferencesUtils","DS/CAT3DAnnotationUtils/CAT3DAnnotationUtils","DS/CATWebUXPreferences/CATWebUXPreferencesManager","DS/ApplicationFrame/CommandsManager","DS/PADUtils/views/PADAlert","i18n!DS/CAT3DAnnotationAttributeController/assets/nls/CAT3DAnnotationAttributeController","i18n!DS/CAT3DAnnotationRsc/assets/nls/CAT3DAnnotationAttributesCategories","i18n!DS/CAT3DAnnotationRsc/assets/nls/CAT3DAnnotationAttributesSubTypes","i18n!DS/CAT3DAnnotationRsc/assets/nls/CAT3DAnnotationTypes"],function(e,t,n,i,r,a,o,l,s,c,u,p,d,h,g,f,A,m,b){"use strict";var y={},v={},D=e.extend({init:function(e){this._parent(e);var D,C,S,T,k,P,w,I=this,E=[],U={},F=[],x=!1,L=!1,R=!0,M=(e||{}).ctxViewer,V=!1,O={},B={},_={},W=0,N={},q=!1,X=150,j=0,H=0,J=0,z=!1,G=[],K=s.getPreference("3DAnnotAttributesOrderedList"),Z=new n,Q=[],Y=!1,$=!1,ee=!1,te={},ne=!0,ie={6:[{type:4,state:!1},{type:5,state:!0},{type:6,state:!1}]},re={6:5};function ae(e,t){Z.publish({event:e,data:t})}function oe(e){var t=e.split("&#xA;"),n=[];t.forEach(function(e){n=n.concat(e.split("&#10;"))});var i=[];return n.forEach(function(e){i=i.concat(e.split(/(\r\n|\n|\r)/gm))}),n}function le(e){var t,n,i,r=[];if(Array.isArray(e)){for(t=0;t<e.length;t++)if(i=e[t].Magnitude,(n=e[t].Value)&&e[t].Name){i&&i.toLowerCase&&(i=i.toLowerCase());var a=!1;u.isMagnitudeSupported(i)?(n=u.convertValueToString(e[t].Value,i,{displayUnit:!1}),a=!0):"boolean"===i?n=("1"===n).toString():"string"===i&&(n=oe(n)),r.push({colID:e[t].Name,title:e[t].Name+(a?" ("+u.getCurrentUnit(i).nls+")":""),value:n,hidden:e[t].Hidden})}}else if(e){var o=e.split(/(\r\n|\n|\r)/gm);for(t=0;t<o.length;t++)if(10!==o[t].charCodeAt(0)){var l=o[t].indexOf("=");-1===l&&(l=o[t].indexOf(":")),-1!==l?r.push({colID:o[t].substring(0,l),title:o[t].substring(0,l),value:o[t].substring(l+1,o[t].length)}):r.push({colID:"otherParams",title:f.otherParams,value:o[t]})}}return r}function se(e,t,n){var i,r,a,o,l,s=b[e.type];if(n){if(i={},e.hyperlink.hyperlinksAndComments?(i.hyperlinksAndComments=e.hyperlink.hyperlinksAndComments,r=!0):e.hyperlink&&e.hyperlink.requirements&&(i.navReq=e.hyperlink.requirements,r=!0),r)return{uuid:e.uuid,idPath:t.slice(),isAStructuralItem:e.isRootAttr,name:e.name,type:e.type,attrType:e.type,activeState:e.activeState,icon:e.icon,attributes:i}}else{var c;if(i=[],47===e.type){r=!0,s=e.attrType&&m[e.category]&&m[e.category][e.attrType]?m[e.category][e.attrType]:void 0;var p=le(e.param);p&&p.length&&(i=i.concat(p)),c=e.pointedAttrs}else if(47!==e.type){if(r=!0,e.noaText&&i.push({colID:"noaText",title:f.noaText,value:e.noaText}),e.noaType&&i.push({colID:"noaType",title:f.noaType,value:e.noaType}),e.text&&i.push({colID:"text",title:f.text,value:oe(e.text)}),e.generalTolerance||e.tabulatedLimit&&e.toleranceLimits){var d=37===e.type||13===e.type?"angle":"length",h=" ("+u.getCurrentUnit(d).nls+")",g="length"===d?"mm":"deg",A="length"===d?"m":"rad";if(e.generalTolerance){if(i.push({colID:"generalToleranceName",title:f.generalToleranceName,value:e.generalTolerance.name}),(o=e.generalTolerance.variation.split(" / ")).length){for(var y=0;y<o.length;y++)o[y]=o[y].replace("°","");a=u.convert(o[0],d,g,A),l="",isNaN(a)||(l+=u.convertValueToString(a,d,{displayUnit:!1}),l+=" / ",a=u.convert(o[1],d,g,A),l+=u.convertValueToString(a,d,{displayUnit:!1})),i.push({colID:"generalToleranceVariation",title:f.generalToleranceVariation+h,value:l})}}else i.push({colID:"tabulatedLimit",title:f.tabulatedLimit,value:e.tabulatedLimit}),a=u.convert(e.toleranceLimits.min,d,g,A),i.push({colID:"toleranceLimitMin",title:f.toleranceLimitMin+h,value:u.convertValueToString(a,d,{displayUnit:!1})}),a=u.convert(e.toleranceLimits.max,d,g,A),i.push({colID:"toleranceLimitMax",title:f.toleranceLimitMax+h,value:u.convertValueToString(a,d,{displayUnit:!1})})}e.hyperlink&&e.hyperlink.hyperlinksAndComments&&e.hyperlink.hyperlinksAndComments.hiddenText&&i.push({colID:"hiddenText",title:f.hiddenText,value:oe(e.hyperlink.hyperlinksAndComments.hiddenText)}),e.hyperlink&&e.hyperlink.failureModes&&e.hyperlink.failureModes.length&&i.push({colID:"failureModes",title:f.failureModes,value:e.hyperlink.failureModes}),e.hyperlink&&e.hyperlink.requirements&&i.push({colID:"navReq",title:f.navReq,value:e.hyperlink.requirements}),e.hyperlink&&e.hyperlink.relatedDocs&&i.push({colID:"relatedDocs",title:f.relatedDocs,value:e.hyperlink.relatedDocs}),e.hyperlink&&e.hyperlink.hyperlinksAndComments&&e.hyperlink.hyperlinksAndComments.urls&&e.hyperlink.hyperlinksAndComments.urls.length&&i.push({colID:"urls",title:f.urls,value:e.hyperlink.hyperlinksAndComments.urls})}if(r)return{uuid:e.uuid,attrType:e.type,attrSubtype:e.attrType,pointedUuids:c,idPath:t,isAStructuralItem:e.isRootAttr,name:e.name,type:s,activeState:e.activeState,icon:e.icon,attributes:i}}}function ce(e){var t=[];return e.hyperlink&&e.hyperlink.hyperlinksAndComments&&e.hyperlink.hyperlinksAndComments.hiddenText&&t.push({colID:"hiddenText",title:f.hiddenText,value:oe(e.hyperlink.hyperlinksAndComments.hiddenText)}),e.hyperlink&&e.hyperlink.failureModes&&e.hyperlink.failureModes.length&&t.push({colID:"failureModes",title:f.failureModes,value:e.hyperlink.failureModes}),e.hyperlink&&e.hyperlink.requirements&&t.push({colID:"navReq",title:f.navReq,value:e.hyperlink.requirements}),e.hyperlink&&e.hyperlink.relatedDocs&&t.push({colID:"relatedDocs",title:f.relatedDocs,value:e.hyperlink.relatedDocs}),e.hyperlink&&e.hyperlink.hyperlinksAndComments&&e.hyperlink.hyperlinksAndComments.urls&&e.hyperlink.hyperlinksAndComments.urls.length&&t.push({colID:"urls",title:f.urls,value:e.hyperlink.hyperlinksAndComments.urls}),t}function ue(e,t,n){var i,r,a,o,l,s,c=[],p=!1,d=b[e.type];if(47===e.type&&e.category===n){p=!0,d=e.attrType&&m[e.category]&&m[e.category][e.attrType]?m[e.category][e.attrType]:void 0;var h=le(e.param);h&&h.length&&(c=c.concat(h)),l=e.pointedAttrs,"6"===n&&1===e.attrType&&h&&h.length&&h.some(function(e){if("VisuMode"===e.colID)return re[6]=parseFloat(e.value),!0})}else if("3DAnnotations"===n&&47!==e.type){if(p=!0,e.noaText&&c.push({colID:"noaText",title:f.noaText,value:e.noaText}),e.noaType&&c.push({colID:"noaType",title:f.noaType,value:e.noaType}),e.text&&c.push({colID:"text",title:f.text,value:oe(e.text)}),e.generalTolerance||e.tabulatedLimit&&e.toleranceLimits){var g=37===e.type||13===e.type?"angle":"length",A=" ("+u.getCurrentUnit(g).nls+")",y="length"===g?"mm":"deg",v="length"===g?"m":"rad";if(e.generalTolerance){if(c.push({colID:"generalToleranceName",title:f.generalToleranceName,value:e.generalTolerance.name}),(a=e.generalTolerance.variation.split(" / ")).length){for(var D=0;D<a.length;D++)a[D]=a[D].replace("°","");r=u.convert(a[0],g,y,v),o="",isNaN(r)||(o+=u.convertValueToString(r,g,{displayUnit:!1}),o+=" / ",r=u.convert(a[1],g,y,v),o+=u.convertValueToString(r,g,{displayUnit:!1})),c.push({colID:"generalToleranceVariation",title:f.generalToleranceVariation+A,value:o})}}else c.push({colID:"tabulatedLimit",title:f.tabulatedLimit,value:e.tabulatedLimit}),r=u.convert(e.toleranceLimits.min,g,y,v),c.push({colID:"toleranceLimitMin",title:f.toleranceLimitMin+A,value:u.convertValueToString(r,g,{displayUnit:!1})}),r=u.convert(e.toleranceLimits.max,g,y,v),c.push({colID:"toleranceLimitMax",title:f.toleranceLimitMax+A,value:u.convertValueToString(r,g,{displayUnit:!1})})}c=c.concat(ce(e))}if(e.children&&e.children.length){var C;s=[];for(var S=0;S<e.children.length;S++)(C=ue(e.children[S],t.concat([S]),n))&&s.push(C)}return p&&(i={idPath:t,pointedUuids:l,attrType:e.type,attrSubtype:e.attrType,uuid:e.uuid,isAStructuralItem:e.isRootAttr,name:e.name,type:d,displayFlag:e.displayFlag,activeState:e.activeState,icon:e.icon,attributes:c,children:s}),i}function pe(e,t,n){var i=[];if("3DAnnotations"===n&&e.hyperlink){var r=[];(r=r.concat(ce(e))).length&&i.push({uuid:e.uuid,idPath:t.slice(),isAStructuralItem:e.isRootAttr,name:e.name,type:b[e.type],attrType:e.type,displayFlag:e.displayFlag,activeState:e.activeState,icon:e.icon,attributes:r})}for(var a=0;a<e.children.length;a++){var o=e.children[a];if("RootViews"!==o.type&&"RootCaptures"!==o.type)for(var l=0;l<o.children.length;l++){var s=ue(o.children[l],[t[0],t[1],a,l],n);s&&i.push(s)}}return i}function de(){var e,t,n=Object.keys(U);for(e=0;e<n.length;e++)U[n[e]].splice(0,U[n[e]].length);if(U={},Y){if(U.result=[],E&&E.length)for(e=0;e<E.length;e++){var i=E[e].getLastElement(!0);for(t=i;t&&t.getAnnotationClassType&&"tpsAnnotationSet"!==t.getAnnotationClassType();)t=t.parents[0];if(t.isVisible()){var r=se(i.toJSON(),[e],i===t);r&&(U.result[0]||U.result.push({elements:[]}),U.result[0].elements.push(r))}}}else if(E&&E.length)for(e=0;e<E.length;e++)for(var a=0;a<E[e].children.length;a++)if((t=E[e].children[a]).isVisible)for(var o=0;o<K.length;o++)if(null===G||G&&-1===G.indexOf(K[o])){var l={elements:pe(t,[e,a],K[o]),section:{idPath:[e,a],name:t.referenceLabel+" / "+t.name,icon:t.referenceIcon}};l.elements.length&&(U[K[o]]||(U[K[o]]=[]),U[K[o]].push(l))}}var he=function(){J&&(clearTimeout(J),J=0),F&&F.length&&(F.forEach(function(e){e.panel.clean();for(var t=Object.keys(e.tokensBrowserPanel),n=0;n<t.length;n++)e.attrUI.unsubscribe(e.tokensBrowserPanel[t[n]]);e.attrUI.destroy(),e.attrUI=null}),F.length=0)},ge=function(){!x||z?he():(J&&(clearTimeout(J),J=0),J=setTimeout(function(){x&&I&&(I._internalRefresh(),X=150,J=0)},X))},fe=function(e){x&&(j+=!0===e?1:-1,H&&(clearTimeout(H),H=0),F.length&&(F.forEach(function(e){e.panel.setEnableFlag(j>=0)}),j<0&&(H=setTimeout(function(){x&&F&&F.forEach(function(e){e.panel.setEnableFlag(!0)}),j=0},2e4))))},Ae=function(e){var t;if(E&&E.length){var n,i,r;t=[];for(var a=0;a<E.length;a++){if(Y){if(e.isEqual(E[a])){t=[a];break}}else if(E[a].path.isAParentOf(e)){t.push(a);for(var o=0;o<E[a].children.length;o++)if((r=E[a].children[o].path).isAParentOf(e))for(t.push(o);!r.isEqual(e);){i=!1,n=r.getChildrenPathes();for(var l=0;l<n.length;l++)if(n[l].isAParentOf(e)||n[l].isEqual(e)){t.push(l),r=n[l],i=!0;break}if(!i)return}}}}return t},me=function(e,t,n){e&&e.length&&e.forEach(function(e){var i=e.pathElement,r={path:i.clone(),attributes:[]},a=[],o=i.getLastElement(!0);if(o&&o.getAnnotationClassType){var l=Ae(i);l&&a.push({idPath:l,infos:t})}else{var c=C.getPontingAnnotations(i,!0);if(c&&c.length)for(var u=0;u<c.length;u++)"tpsAttribute"===c[u].getLastElement(!0).getAnnotationClassType()&&(a.push({idPath:Ae(c[u]),infos:t}),r.attributes.push(c[u].clone()))}a.length&&F.forEach(function(e){(n||!e.attrUI.getDiscipline()||"display"!==s.getPreference("CAT3DAnnotAttrRowSelection_"+e.attrUI.getDiscipline()))&&e.attrUI.setAdditionalInformations(a)})})},be=function(e){q||z||me(e,{activeState:!0,displayFlag:!0})},ye=function(e){q||z||me(e,{activeState:!1})},ve=function(){q||z||F&&F.length&&F.forEach(function(e){if(!e.attrUI.getDiscipline()||e.attrUI.getDiscipline()&&"display"!==s.getPreference("CAT3DAnnotAttrRowSelection_"+e.attrUI.getDiscipline())){var t=e.attrUI.getDisplayedIDPaths(!0);t&&t.length&&e.attrUI.setAdditionalInformations(t.map(function(e){return{idPath:e,infos:{activeState:!1}}}))}})},De=function(e){if(e&&E&&E.length&&E[e[0]]){if(Y)return E[e[0]];if(E[e[0]].children&&E[e[0]].children[e[1]]){if(2===e.length)return E[e[0]].children[e[1]].path;if(E[e[0]].children[e[1]].path){for(var t,n=E[e[0]].children[e[1]].path.getChildrenPathes(),i=2;i<e.length;i++)n&&n[e[i]]&&(n=(t=n[e[i]]).getChildrenPathes());return t}}}};function Ce(e){e&&ae("onCommentSelected",{path:De(e.idPath)})}var Se,Te=function(e){if(!q){var n;if(q=!0,Y)t.empty();else{var i=y[e.discipline];i&&i.length&&(t.remove(i),i.length=0),y[e.discipline]=[]}var r=[],a=[],o=e.currentSelection;for(n=0;n<o.length;n++){var l=De(o[n].idPath);l&&(o[n].activeState?(Y||y[e.discipline].push({pathElement:l}),r.push({pathElement:l,filterOpts:ie})):o[n].activeState||a.push({pathElement:l}),l.getLastElement(!0).setDisplayFlag(!!o[n].activeState||l.getLastElement(!0).getDisplayFlag()))}r.length&&t.add(r),a.length&&t.add(a),q=!1}};function ke(){Se&&clearTimeout(Se),Se=setTimeout(function(){if(Se=null,C)if(x){var e=[];Object.keys(v).forEach(function(t){e=e.concat(v[t]),Y||me(v[t],{activeState:!0,displayFlag:!0},!0)}),C.isolatePaths({paths:e.map(function(e){return e.pathElement}),opts:ie})}else C.isolatePaths({paths:[],opts:ie})})}var Pe=function(e){if(!q){q=!0;var t=e.currentSelection;v[e.discipline]=[];for(var n=0;n<t.length;n++){var i=De(t[n].idPath);i&&t[n].activeState&&(v[e.discipline].push({pathElement:i}),i.getLastElement(!0).setDisplayFlag(!0))}ke(),q=!1}},we=function(e){var t=De(e.idPath);t&&t.getLastElement(!0).getDisplayFlag&&t.getLastElement(!0).getDisplayFlag()!==e.flag&&t.getLastElement(!0).setDisplayFlag(e.flag)};function Ie(e){e.state,s.setPreference("CAT3DAnnotColumnSort_"+e.discipline,e.state)}function Ee(e){var n=[];"display"===e.state?(s.setPreference("CAT3DAnnotAttrRowSelection_"+e.discipline,e.state),n=y[e.discipline]?y[e.discipline].slice():null,v[e.discipline]=y[e.discipline]?y[e.discipline].slice():[],y[e.discipline]=[],n&&n.length&&(q=!0,t.remove(n),q=!1),ke()):(n=v[e.discipline]?v[e.discipline].slice():null,y[e.discipline]=v[e.discipline]?v[e.discipline].slice():[],v[e.discipline]=[],ke(),n&&n.length&&(n.forEach(function(e){e.filterOpts=ie}),q=!0,t.add(n),q=!1),s.setPreference("CAT3DAnnotAttrRowSelection_"+e.discipline,e.state))}function Ue(e){y[e.discipline]&&y[e.discipline].length?(q=!0,t.remove(y[e.discipline].slice()),q=!1,y[e.discipline].length=0):v[e.discipline]&&(v[e.discipline]=[],ke()),s.setPreference("CAT3DAnnotAttrAttrDisplay_"+(Y?"result":e.discipline),e.state)}function Fe(e){if(ie[e.discipline]=e.state,"display"===s.getPreference("CAT3DAnnotAttrRowSelection_"+e.discipline))ke();else{var n=y[e.discipline]?y[e.discipline].slice():null;n&&n.length&&(q=!0,n.forEach(function(e){e.filterOpts=ie}),t.remove(n),t.add(n),q=!1)}}function xe(e){var n,i;for(i=0;i<F.length;i++)if(F[i].discipline===e.discipline){n=F[i];break}var r=[];if(t.get().forEach(function(t){var n=t.pathElement.getLastElement(!0);n.getAnnotationClassType&&("3DAnnotations"===e.discipline&&47!==n.getAnnotationType()||n.getAttributeCategory&&e.discipline===n.getAttributeCategory())&&r.push(n)}),n){var a=function(e){if(e.setDisplayFlag(!1),e.children&&e.children.length&&e.children[0].setDisplayFlag)for(var t=0;t<e.children.length;t++)a(e.children[t])},o=S.getLoadedAnnotationSets();for(i=0;i<o.length;i++)for(var l=o[i].children,s=0;s<l.length;s++){var c=l[s];if("RootCaptures"!==c.getAnnotationType()&&"RootViews"!==c.getAnnotationType())for(var u,p=l[s].children,d=0;d<p.length;d++)u=p[d],("3DAnnotations"===e.discipline&&47!==u.getAnnotationType()||u.getAttributeCategory&&e.discipline===u.getAttributeCategory())&&-1===r.indexOf(u)&&a(u)}g.displayNotification({eventID:"info",msg:f.DisplayHistorySuccessfulLbl})}}function Le(e){var t,n;if(Y)t=F[0];else for(n=0;n<F.length;n++)if(F[n].discipline===e.discipline){t=F[n];break}if(t){var i,r=t.attrUI.getDisplayedIDPaths(!0);if(r.length){var a,o=[];for(n=0;n<r.length;n++)(a=De(r[n]))&&(o.push(a),(i=C.getAllRelatedPaths(a))&&i.length&&(o=o.concat(i)));o.length&&M.getViewer().currentViewpoint.reframeOn(400,o,{reframeSpace:"visibleSpace",withInternalSceneGraph:!0})}}F[0].panel&&F[0].panel.isSmallWidth()&&F[0].panel.collapsePanel()}function Re(e){if(ne&&e&&e.discipline&&F){var t=M.getRootBagPath().getChildrenPathes().map(function(e){return{node:e.getLastElement(!0),id:p.getNodeID(e.getLastElement(!0))}});if(t&&t.length){var n=function(n){for(var i="",r=0;r<t.length;r++)r>0&&(i+="_"),i+=n[t[r].id]["ds6w:label"];F.some(function(t){if(t.discipline===e.discipline){var n=t.attrUI.exportAsCSV(e);if(n){var r=new Blob([n],{type:"text/csv"}),a=window.URL.createObjectURL(r),o=document.createElement("a");o.style.display="none",o.download=i+"_"+A[t.discipline]+".csv",o.href=a,document.body.appendChild(o),window.__karma__&&window.__karma__.config?ae("onAttributeTableExported",{fileName:i+"_"+A[t.discipline]+".csv",content:n}):o.click(),window.URL.revokeObjectURL(a),document.body.removeChild(o),p._sendUsageProbe("command",e.onlySelectedRows?"ExportSelectedTechnologicalAttrToCSV":"ExportVisibleTechnologicalAttrToCSV")}return!0}})};if("3DXML"===p.getLoadedAssetType()){for(var i=0;i<t.length;i++){var r=p.getNodeInfos(t[i].node);(void 0)[t[i].id]={},(void 0)[t[i].id]["ds6w:label"]=r.name}n(void 0)}else M.getController().getAttributes({ids:t.map(function(e){return e.id}),attributes:["ds6w:label"],callbacks:{onComplete:n,onFailure:n}})}}}function Me(e){if(x&&F&&F.length){var t=[],n={};e&&e.length&&e.forEach(function(e){if(e&&e.getLastElement(!0).getAttributeCategory){var i=e.getLastElement(!0).getAttributeCategory();-1===t.indexOf(i)&&t.push(i),n[i]||(n[i]=[]),n[i].push({pathElement:e})}}),t.forEach(function(e){F.some(function(t){if(t.attrUI.getDiscipline()===e)return"display"===s.getPreference("CAT3DAnnotAttrRowSelection_"+e)?I.setDisplayedElementPaths(n[e]):I.setSelectedElementPaths(n[e]),!0})})}Q=e}function Ve(e){e.colIdx&&F[0].panel&&F[0].panel.isSmallWidth()&&F[0].panel.collapsePanel()}let Oe=["dragenter","dragover","drop"];function Be(e){e.stopPropagation()}function _e(){const e=M.getViewer().canvas,t=M.getFrameWindow().getImmersiveFrame();return Oe.forEach(n=>{e.addEventListener(n,Be),t.addEventListener(n,Be)}),!0}function We(){const e=M.getViewer().canvas,t=M.getFrameWindow().getImmersiveFrame();return Oe.forEach(n=>{e.removeEventListener(n,Be),t.addEventListener(n,Be)}),!0}var Ne=function(e,t){for(var n,a=0;a<F.length;a++)if(F[a].discipline===e){n=F[a];break}n||(n={discipline:e},F.push(n)),n&&n.attrUI&&n.attrUI.cleanPanel(),n.attrUI=new i,n.tokensBrowserPanel={},n.tokensBrowserPanel.onAttrActiveStateModified=n.attrUI.subscribe("onAttrActiveStateModified",Te),n.tokensBrowserPanel.onAttrDisplayStateModifiedCB=n.attrUI.subscribe("onAttrDisplayStateModified",Pe),n.tokensBrowserPanel.onAttrDisplayFlagModified=n.attrUI.subscribe("onAttrDisplayFlagModified",we),n.tokensBrowserPanel.onDeleteDisplayHistory=n.attrUI.subscribe("onDeleteDisplayHistory",xe),n.tokensBrowserPanel.onSwitchEnableColumnSort=n.attrUI.subscribe("onSwitchEnableColumnSort",Ie),n.tokensBrowserPanel.onSwitchRowSelectionMode=n.attrUI.subscribe("onSwitchRowSelectionMode",Ee),n.tokensBrowserPanel.onSwitchAttributeDisplayMode=n.attrUI.subscribe("onSwitchAttributeDisplayMode",Ue),n.tokensBrowserPanel.onSwitchMultiRepDisplayMode=n.attrUI.subscribe("onSwitchMultiRepDisplayMode",Fe),n.tokensBrowserPanel.onReframeOn=n.attrUI.subscribe("onReframeOn",Le),n.tokensBrowserPanel.onExportToCSVRequired=n.attrUI.subscribe("onExportToCSVRequired",Re),n.tokensBrowserPanel.onAttributeClicked=n.attrUI.subscribe("onAttributeClicked",function(e){C&&C.solveAttributeLink(e)}),n.tokensBrowserPanel.onCommentClicked=n.attrUI.subscribe("onCommentClicked",Ce),n.tokensBrowserPanel.onDSpointerhit=n.attrUI.subscribe("onDSpointerhit",Ve);var o=s.getPreference("CAT3DAnnotAttrRowSelection_"+e);o=o||"highlight";var l=s.getPreference("CAT3DAnnotAttrAttrDisplay_"+e);l=l||"columns";var u=s.getPreference("CAT3DAnnotColumnSort_"+e);u=u||"disabled";var p=n.attrUI.buildPanel({model:t,discipline:e,disciplineNls:A[e],rowSelectionCurrentState:o,columnSortCurrentState:u,attributeDisplayCurrentState:l,representationMode:ie[e],multiRepDefaultMode:re[e],allowExport:!1!==ne,onDragStartColumnHeader:_e,onDragEndColumnHeader:We});if(p)if(n.panel)n.panel.content=p;else{var d=c.getWebappsAssetUrl("CAT3DAnnotationRsc","icons/22/");"3DAnnotations"===e?d+="I_W3DAnnot_NodeAnnotationSet.png":"0"===e?d+="I_W3DAnnot_MechanicalAttribute.png":"1"===e?d+="I_W3DAnnot_ElectricalAttribute.png":"2"===e?d+="I_W3DAnnot_KnowledgeAttribute.png":"3"===e?d+="I_W3DAnnot_StructureAttribute.png":"4"===e?d+="I_W3DAnnot_CompositeAttribute.png":"5"===e?d+="I_W3DAnnot_GeometricalAttribute.png":"6"===e?d+="I_W3DAnnot_LayeredPrdAttribute.png":"7"===e&&(d+="I_W3DAnnot_PathwayAttribute.png"),n.panel=new r({id:"CAT3DAnnotAttrBrowserPanel"+e,className:"CAT3DAnnotAttrBrowserPanel",title:A[e],icon:d,immersiveFrame:M.getFrameWindow().getImmersiveFrame(),viewerFrame:M.getFrameWindow().getViewerFrame(),closeButtonFlag:!1,content:p,minHeight:50,minWidth:180,height:180,snappableFlag:!0,allowMaximizeFlag:!0,maximizeButtonFlag:!0,allowedDockAreas:WUXDockAreaEnum.RightDockArea|WUXDockAreaEnum.LeftDockArea|WUXDockAreaEnum.TopDockArea|WUXDockAreaEnum.BottomDockArea,currentDockArea:WUXDockAreaEnum.BottomDockArea})}n&&n.panel.setPanelVisibilityFlag(R&&0!==n.attrUI.getDisplayedIDPaths().length)};this.setResultRepresentationModeFlag=function(e){e!==$&&($=e,Y?I.hideAttributeResultTable():(he(),ge()))},this.getResultRepresentationModeFlag=function(){return $},this.displayAttributeResultTable=function(e){E.length=0;for(var t=0;t<e.length;t++)E.push(e[t].clone());Y=!0,ge()},this.hideAttributeResultTable=function(){Y&&(E.length=0,he(),Y=!1,ge())},this._internalRefresh=function(){if($&&!Y||!$&&Y)he();else if(x&&!z&&C.getActiveState()){var e,n,a,o=t.get().slice();if(Y){if(de(),F)for(e=F.length-1;e>=0;e--){for(F[e].panel&&(F[e].panel.clean(),F[e].panel=null),a=Object.keys(F[e].tokensBrowserPanel),n=0;n<a.length;n++)F[e].attrUI.unsubscribe(F[e].tokensBrowserPanel[a[n]]);F[e].attrUI.cleanPanel(),F.splice(e,1)}if(!U.result.length||!U.result[0].elements||!U.result[0].elements.length)return;!function(){F&&F.length&&F[0].attrUI&&F[0].attrUI.cleanPanel(),F&&F.length||(F=[{}]),F[0].attrUI=new i,F[0].tokensBrowserPanel={},F[0].tokensBrowserPanel.onAttrActiveStateModified=F[0].attrUI.subscribe("onAttrActiveStateModified",Te),F[0].tokensBrowserPanel.onReframeOn=F[0].attrUI.subscribe("onReframeOn",Le),F[0].tokensBrowserPanel.onDeleteDisplayHistory=F[0].attrUI.subscribe("onDeleteDisplayHistory",xe),F[0].tokensBrowserPanel.onSwitchAttributeDisplayMode=F[0].attrUI.subscribe("onSwitchAttributeDisplayMode",Ue),F[0].tokensBrowserPanel.onAttributeClicked=F[0].attrUI.subscribe("onAttributeClicked",function(e){C&&C.solveAttributeLink(e)}),F[0].tokensBrowserPanel.onDSpointerhit=F[0].attrUI.subscribe("onDSpointerhit",Ve);var e=s.getPreference("CAT3DAnnotAttrAttrDisplay_result");e=e||"columns";var t=F[0].attrUI.buildPanel({model:U.result,attributeDisplayCurrentState:e,onDragStartColumnHeader:_e,onDragEndColumnHeader:We});t&&(F[0].panel?F[0].panel.content=t:F[0].panel=new r({id:"CAT3DAnnotResultAttrBrowserPanel",className:"CAT3DAnnotAttrBrowserPanel CAT3DAnnotResultAttrBrowserPanel",title:f.resultAttrPanelTitle,icon:"wux-ui-3ds wux-ui-3ds-1x wux-ui-3ds-attributes",immersiveFrame:M.getFrameWindow().getImmersiveFrame(),viewerFrame:M.getFrameWindow().getViewerFrame(),closeButtonFlag:!1,content:t,minHeight:50,minWidth:180,height:180,snappableFlag:!0,allowMaximizeFlag:!0,maximizeButtonFlag:!0,allowedDockAreas:WUXDockAreaEnum.RightDockArea|WUXDockAreaEnum.LeftDockArea|WUXDockAreaEnum.TopDockArea|WUXDockAreaEnum.BottomDockArea,currentDockArea:WUXDockAreaEnum.BottomDockArea})),F&&F.length&&F[0].panel.setPanelVisibilityFlag(0!==F[0].attrUI.getDisplayedIDPaths().length)}(),F.length&&(F.forEach(function(e){e.panel.setEnableFlag(j>=0)}),me(o,{activeState:!0}),ke(),Q.length&&Me(Q),Q.length=0)}else k||(k=C.onAdditionalInfosRequired({controller:"AnnotAttrBrowser"}).getJSONModel),E.splice(0,E.length),k(function(t){E=t,G=D?D.getAttributeTypesFiltered():null,de();var i=Object.keys(U);for(e=F.length-1;e>=0;e--)if(-1===i.indexOf(F[e].discipline)){for(F[e].panel.clean(),a=Object.keys(F[e].tokensBrowserPanel),n=0;n<a.length;n++)F[e].attrUI.unsubscribe(F[e].tokensBrowserPanel[a[n]]);F[e].attrUI.cleanPanel(),F.splice(e,1)}for(e=i.length-1;e>=0;e--)Ne(i[e],U[i[e]]);F&&F.length&&(F.forEach(function(e){e.panel.setPanelVisibilityFlag(R),e.panel.setEnableFlag(j>=0),e.attrUI.forceCommentColumnVisibilityFlag(ee),e.attrUI.setCommentInformation(P)}),me(o,{activeState:!0})),ke(),Q.length&&Me(Q),Q.length=0})}else he()},this.setDisplayedElementPaths=function(e,t){if(x){v={},t&&(ie=t);var n=[],i={};e&&e.length&&e.forEach(function(e){if(e.pathElement&&e.pathElement.getLastElement(!0).getAttributeCategory){var t=e.pathElement.getLastElement(!0).getAttributeCategory();v[t]||(v[t]=[]),v[t].push(e),-1===n.indexOf(t)&&n.push(t),i[t]||(i[t]=[]),i[t].push(Ae(e.pathElement))}}),n.forEach(function(e){s.setPreference("CAT3DAnnotAttrRowSelection_"+e,"display"),F.some(function(t){if(t.attrUI.getDiscipline()===e){t.attrUI.setRowSelectionMode("display"),ie&&ie[e]&&t.attrUI.setMultiRepOption(ie[e]);for(var n=t.attrUI.getDisplayedIDPaths(!0)||[],r=i[e].slice(),a=r.length-1;a>=0;a--){for(var o=!1,l=n.length-1;l>=0;l--)if(JSON.stringify(r[a])===JSON.stringify(n[l])){n.splice(l,1),o=!0;break}o&&r.splice(a,1)}var s=n.map(function(e){return{idPath:e,infos:{activeState:!1}}});return(s=s.concat(r.map(function(e){return{idPath:e,infos:{activeState:!0,displayFlag:!0}}}))).length&&t.attrUI.setAdditionalInformations(s),!0}})}),ke()}},this.setSelectedElementPaths=function(e,n){var i=[],r=[];e&&e.length&&e.forEach(function(e){if(e.pathElement&&e.pathElement.getLastElement(!0).getAttributeCategory){var t=e.pathElement.getLastElement(!0).getAttributeCategory();-1===i.indexOf(t)&&i.push(t)}r.push({pathElement:e.pathElement.clone(),filterOpts:ie})}),x&&(n&&(ie=n),i.forEach(function(e){s.setPreference("CAT3DAnnotAttrRowSelection_"+e,"highlight"),F.some(function(t){if(t.attrUI.getDiscipline()===e)return t.attrUI.setRowSelectionMode("highlight"),!0})})),t.add(r)},this.getDisplayedElementPaths=function(){if(x){var e=[];return Object.keys(v).forEach(function(t){e=e.concat(v[t])}),e}},this.getDisplayedElementsFilteringOpts=function(){return ie},this.setActiveState=function(e){var n;if(x=e,C&&S){if(x)V||(N.onPostAdd=t.onPostAdd(be),N.onPostRemove=t.onPostRemove(ye),N.onEmpty=t.onEmpty(ve),D=l.give3DAnnotFilterController({context:M}),(w=d.getPreferenceManager({context:M.getFrameWindow()}))&&(te.onUnitChanged=w.subscribe("CATWebUXPreferences_UnitsAndDecimalPlace",ge),te.onTrailingZerosChanged=w.subscribe("CATWebUXPreferences_TrailingZeros",ge)),D&&(O.onAnnotationFilterActiveStateModified=D.subscribe("onAnnotationFilterActiveStateModified",function(){var e=D.getAttributeTypesFiltered(),t=!1;if((!G&&e&&e.length||!e&&G&&G.length||e&&G&&e.length!==G.length)&&(t=!0),!t)for(n=0;n<e.length;n++)if(e[n]!==G[n]){t=!0;break}t&&ge()}),O.onAttributesVisibilityModified=D.subscribe("onAttributesVisibilityModified",ge)),B.onAnnotControllerActiveStateChanged=C.subscribe("onAnnotControllerActiveStateChanged",function(e){e.state?ge():he()}),B.onFeatureFocusRequired=C.subscribe("onFeatureFocusRequired",function(e){e&&e.paths&&Me(e.paths)}),B.onAnnotationsLinkedDataSolved=C.subscribe("onAnnotationsLinkedDataSolved",function(e){!0===e.succeeded&&ge()}),B.onBeginDisplayViewOrCapture=C.subscribe("onBeginDisplayViewOrCapture",function(e){if(e&&(e.path||e.uuid)){let e=s.getPreference("3DAnnotKeepSelectedFeaturesPref");Object.keys(v).forEach(function(t){"3DAnnotations"!==t&&e||(me(v[t],{activeState:!1},!0),delete v[t])}),e||t.empty(),ke()}}),_.onAnnotModelLoaderActiveStateChanged=S.subscribe("onAnnotModelLoaderActiveStateChanged",function(e){e.state?ge():he()}),_.onAnnotationVisibilityModifiedToken=S.subscribe("onAnnotationVisibilityModified",function(e){e&&e.node&&e.node.getAnnotationClassType&&"tpsAnnotationSet"===e.node.getAnnotationClassType()&&(e.node.isVisible()||(Object.keys(v).forEach(function(t){if(v[t]&&v[t].length)for(var n=v[t].length-1;n>=0;n--)(!v[t][n]||v[t][n]&&e.path.isAParentOf(v[t][n]))&&v[t].splice(n,1)}),v={},ke()),ge())}),_.onAnnotationSetLoadedToken=S.subscribe("onAnnotationSetLoaded",function(){fe(!0),ge()}),_.onAnnotationSetPartiallyLoaded=S.subscribe("onAnnotationSetPartiallyLoaded",function(){fe(!0),ge()}),_.onAnnotationSetRemovedToken=S.subscribe("onAnnotationSetRemoved",function(){var e;X=0,(e=S.getOrderedAnnotationSetPaths())&&e.length&&(e=e.map(e=>e.annotSetPath)),Object.keys(v).forEach(function(t){for(var n=v[t].length-1;n>=0;n--){var i=!1;if(e&&e.length)for(var r=e.length-1;r>=0;r--)if(v[t][n]&&v[t][n].pathElement&&e[r].isAParentOf(v[t][n].pathElement)){i=!0;break}i||v[t].splice(n,1)}}),ge()}),_.onAnnotationSetBeginLoadToken=S.subscribe("onAnnotationSetBeginLoad",function(){fe(!1)}),_.onAnnotationSetLoadingFailedToken=S.subscribe("onAnnotationSetLoadingFailed",function(){fe(!0)}),_.onNoAnnotationSetLoadedToken=S.subscribe("onNoAnnotationSetLoaded",function(e){e&&e.loadingAlreadyStarted&&fe(!0)}),_.onAnnotationSetLoadingCanceledToken=S.subscribe("onAnnotationSetLoadingCanceled",function(){fe(!0)}),_.onVisuModificationToken=S.subscribe("onAnnotationParentVisibilityModified",function(){ge()}),W=M.getViewer().onSwapVisibleSpace?M.getViewer().onSwapVisibleSpace(function(){z=0===M.getViewer().getVisibleSpace(),1===M.getViewer().getVisibleSpace()?ge():he()}):null,z=0===M.getViewer().getVisibleSpace(),V=!0),ke(),ge();else if(V){t.unsubscribe(N.onPostAdd),t.unsubscribe(N.onPostRemove),t.unsubscribe(N.onEmpty);var i=Object.keys(O);if(D)for(n=0;n<i.length;n++)D.unsubscribe(O[i[n]]);if(w){for(i=Object.keys(te),n=0;n<i.length;n++)w.unsubscribe(te[i[n]]);d.unreferencePreferenceManager({context:M.getFrameWindow()}),w=null}for(i=Object.keys(B),n=0;n<i.length;n++)C.unsubscribe(B[i[n]]);for(i=Object.keys(_),n=0;n<i.length;n++)S.unsubscribe(_[i[n]]);W&&M.getViewer()&&M.getViewer().unsubscribe(W),ke(),W=0,O={},_={},B={},N={},he(),Q.length=0,V=!1}}else T||(T=setInterval(function(){S=o.giveAnnotationModelLoader({context:M}),(C=a.giveAnnotationController({context:M}))&&S&&(clearInterval(T),T=null,I.setActiveState(x))},100))},this.getActiveState=function(){return x},this.setControllerEnableFlag=function(e){var t=h.getCommandCheckHeader("CAT3DAnnotSemanticAttrBrowserHdr",M.getCommandContext());t&&(e?t.enable():t.disable()),L?(I.setActiveState(!0),L=!1):e||x&&(I.setActiveState(!1),L=!0,k=C=S=null)},this.setPanelVisibilityFlag=function(e){R=e,!Y&&F&&F.length&&F.forEach(function(e){e.panel.setPanelVisibilityFlag(R&&0!==e.attrUI.getDisplayedIDPaths().length)})},this.getPanelVisibilityFlag=function(){return R},this.forceCommentColumnVisibilityFlag=function(e){ee!==e&&(ee=e,F.forEach(function(e){e.attrUI.forceCommentColumnVisibilityFlag(ee)}))},this.updateCommentColumn=function(e){JSON.stringify(P)!==JSON.stringify(e)&&(P=e,F.forEach(function(e){e.attrUI.setCommentInformation(P)}))},this.clearController=function(){this.setActiveState(!1),T&&clearInterval(T),F=E=null,D=C=S=G=null,k=null,Z=M=I=null},this.allowTableExportInCSV=function(e){ne=e},this.subscribe=function(e,t){return e&&t?Z.subscribe({event:e},t):void 0},this.unsubscribe=function(e){Z&&e&&Z.unsubscribe(e)}}}),C=[];return e.singleton({init:function(){},give3DAnnotAttrBrowserController:function(e){if(e&&e.context)for(var t=0;t<C.length;t++)if(C[t].context===e.context)return C[t].attrCtrl},get3DAnnotAttrBrowserController:function(e){var t=this.give3DAnnotAttrBrowserController(e);if(e&&e.context&&!t){var n=new D({ctxViewer:e.context});t=Object.freeze({setActiveState:function(e){n&&n.setActiveState(e)},getActiveState:function(){if(n)return n.getActiveState()},setResultRepresentationModeFlag:function(e){n&&n.setResultRepresentationModeFlag(e)},getResultRepresentationModeFlag:function(){if(n)return n.getResultRepresentationModeFlag()},setDisplayedElementPaths:function(e,t){n&&n.setDisplayedElementPaths(e,t)},getDisplayedElementsFilteringOpts:function(){return n?n.getDisplayedElementsFilteringOpts():null},setSelectedElementPaths:function(e,t){n&&n.setSelectedElementPaths(e,t)},getDisplayedElementPaths:function(){if(n)return n.getDisplayedElementPaths()},setControllerEnableFlag:function(e){n&&n.setControllerEnableFlag(e)},displayAttributeResultTable:function(e){n&&n.displayAttributeResultTable(e)},hideAttributeResultTable:function(){n&&n.hideAttributeResultTable()},forceCommentColumnVisibilityFlag:function(e){n&&n.forceCommentColumnVisibilityFlag(e)},updateCommentColumn:function(e){n&&n.updateCommentColumn(e)},clearController:function(){n&&(n.clearController(),n=null)},setPanelVisibilityFlag:function(e){n&&n.setPanelVisibilityFlag(e)},getPanelVisibilityFlag:function(){return!!n&&n.getPanelVisibilityFlag()},allowTableExportInCSV:function(e){n&&n.allowTableExportInCSV(e)},subscribe:function(e,t){return n?n.subscribe(e,t):void 0},unsubscribe:function(e){n&&n.unsubscribe(e)}}),C.push({context:e.context,attrCtrl:t,counter:1})}else if(e&&e.context)for(var i=0;i<C.length;i++)if(C[i].context===e.context){C[i].counter++;break}return t},unreference3DAnnotAttrBrowserController:function(e){if(e&&e.context)for(var t=0;t<C.length;t++)if(C[t].context===e.context){C[t].counter--,C[t].counter<=0&&(C[t].attrCtrl.clearController(),C.splice(t,1));break}}})});