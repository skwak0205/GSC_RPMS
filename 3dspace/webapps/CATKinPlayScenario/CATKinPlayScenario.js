define("DS/CATKinPlayScenario/utilsWS/WSUtils",["DS/WAFData/WAFData"],function(e){"use strict";var t={ExpandForAnimDiscipline:function(e,n,i,a){if(e.args.input.asset.serverurl&&e.args.input.asset.tenant){var r=e.args.input.asset.serverurl+"/cvservlet/expand?tenant="+e.args.input.asset.tenant,s={timeout:6e4,onComplete:i,onCancel:a,onFailure:a,onTimeout:a,method:"POST",headers:{Accept:"application/json","Content-Type":"application/json","Accept-Language":"en"}},o={root_path_physicalid:[[n]],expand_iter:"1",select_bo:["physicalid","pathsr","bo.streamdescriptors","type"],type_filter_bo:["CATANIMDISCIPLINE"],label:"AnimationPlayer",tenant:e.args.input.asset.tenant,locale:"en"};return s.data=JSON.stringify(o),t.executeExpandRequest(r,s)}},Expand3d:function(e,n,i,a){if(e.args.input.asset.serverurl&&e.args.input.asset.tenant){var r=e.args.input.asset.serverurl+"/cvservlet/expand3d?tenant="+e.args.input.asset.tenant,s={timeout:6e4,onComplete:i,onCancel:a,onFailure:a,onTimeout:a,method:"POST",headers:{Accept:"application/json","Content-Type":"application/json","Accept-Language":"en"}},o={root_path_physicalid:[[n]],expand_iter:"-1",select_bo:["physicalid","pathsr","bo.streamdescriptors","type"],label:"AnimationPlayer",tenant:e.args.input.asset.tenant,locale:"en"};return s.data=JSON.stringify(o),t.executeExpand3dRequest(r,s)}},Fetch:function(e,n,i,a){if(e.args.input.asset.serverurl&&e.args.input.asset.tenant){var r=e.args.input.asset.serverurl+"/cvservlet/fetch/v2?tenant="+e.args.input.asset.tenant,s={timeout:6e4,onComplete:i,onCancel:a,onFailure:a,onTimeout:a,method:"POST",headers:{Accept:"application/json","Content-Type":"application/json","Accept-Language":"en"}},o={physicalid:n,fcs_url_mode:"REDIRECT",select_file:["anm"],select_predicate:["physicalid"],label:"AnimationPlayer",tenant:e.args.input.asset.tenant,locale:"en"};return s.data=JSON.stringify(o),t.executeFetchRequest(r,s)}},executeExpandRequest:function(t,n){e.authenticatedRequest(t,n)},executeExpand3dRequest:function(t,n){e.authenticatedRequest(t,n)},executeFetchRequest:function(t,n){e.authenticatedRequest(t,n)}};return t}),define("DS/CATKinPlayScenario/CATKinPlayScenario",["DS/3DPlay/PlayScenario3D","DS/CoreEvents/Events","DS/SIMAnimationWebMdl/SIMAnimationServices","DS/WebSystem/App"],function(e,t,n,i){"use strict";return e.extend({init:function(e){this._parent(e)},launch:function(){i.is3DPlay()?this.loadWorkbench({module:"CATKinPlayScenario",name:"CATKinPlayScenarioWithSidePanel"},!0,{sections:[{id:"PlayAnimation",nls:"CATKinPlayScenario/CATKinPlayScenario",rsc:"CATKinPlayScenario/CATKinPlayScenario"},{id:"ChooseAnimation",nls:"CATKinPlayScenario/CATKinPlayScenario",rsc:"CATKinPlayScenario/CATKinPlayScenario"},{id:"RightSidePanel",nls:"PADUtils/PADUtils",rsc:"PADUtils/PADUtils"}]}):this.loadWorkbench({module:"CATKinPlayScenario",name:"CATKinPlayScenario"},!0,{sections:[{id:"PlayAnimation",nls:"CATKinPlayScenario/CATKinPlayScenario",rsc:"CATKinPlayScenario/CATKinPlayScenario"},{id:"ChooseAnimation",nls:"CATKinPlayScenario/CATKinPlayScenario",rsc:"CATKinPlayScenario/CATKinPlayScenario"}]}),require(["DS/CoreEvents/Events"],function(e){e.publish({event:"CATKINPLAYSCENARIO/KINSCENARIOSELECTED",data:{}})})},isMobileCompatible:function(){return!0}})}),define("DS/CATKinPlayScenario/CATKinPlayRegister",["DS/3DPlay/3DPlayRegister","DS/CATKinPlayScenario/CATKinPlayScenario","DS/SIMAnimationWebMdl/SIMAnimationServices","DS/Visualization/PathElement","UWA/Promise","DS/PADUtils/utilsWS/WSAccessV1","DS/Visualization/Utils","DS/WAFData/WAFData","DS/SIMAnimationWebMdl/Ox4FeatureModeler","DS/WebSystem/Environment","DS/PADUtils/PADUtilsServices","DS/CoreEvents/Events","DS/ENOPAD3DViewer/utils/PAD3DViewerModelServices","DS/Visualization/Node3D","DS/Visualization/IdPath","DS/CATKinPlayScenario/utilsWS/WSUtils"],function(e,t,n,i,a,r,s,o,l,c,u,p,S,A,d,I){"use strict";var h=[];return e.extend({init:function(e){var t=this,r=c.isSet("3DPlay.KinDebug");this._parent(e),this.hasCATANIMDISCIPLINE=0,this.CATANIMDISCIPLINEarray={},this._Experience=this.scenarioManager.getExperience(),this._OOCActive=!1,this._Experience.RuntimeMode.OOC&&(this._OOCActive=this._Experience.RuntimeMode.OOC),this._largeDataStructure=null,this._initScenarios(),this._Experience&&this._Experience.EventDisposer.add(this._Experience.subscribe("ASSET/LOADINGFINISHED",function(){var e=function(e){t.scenarioManager.setAvailability(e,{iRegisterID:t.registerID,iScenarioID:h[0]})},s=function(){var i=t._Experience.getRootBag(),a=new n,r=a.getAnimations(i),s=a.getKeyFramer(i);self.keyFramer=s,e(r&&r.length>0),p.publish({event:"ANIMATION_SEARCH_COMPLETE",data:r})};e(!1);var u,d,C,D=!c.isBoolActive("3DPlay.Debug.SkipKinIndex");if(t._largeDataStructure=t._Experience.pad3DViewer.getController().isLargeDataStructure(),t._Experience.args.input.asset&&t._Experience.args.input.asset.physicalid&&"EV6"===t._Experience.args.input.asset.provider&&D){u=t._Experience.args.input.asset.physicalid,d=function(n){var c,u=["pathsr","bo.streamdescriptors"];try{c=JSON.parse(n)}catch(e){c={results:[]}}if(c.results.forEach(function(e){var n=e.attributes;if(n&&n.length>0){for(var i,a,r={},s=n.length;s--;){var o=n[s];u.indexOf(o.name)>-1?r[o.name]=o.value:"physicalid"===o.name?a=o.value:"type"===o.name&&"CATANIMDISCIPLINE"===o.value&&(i=!0)}i&&(t.CATANIMDISCIPLINEarray[a]=r,t.hasCATANIMDISCIPLINE++)}}),e(t.hasCATANIMDISCIPLINE>0),0!=t.hasCATANIMDISCIPLINE){var p=function(){for(var e in t.CATANIMDISCIPLINEarray)if(t.CATANIMDISCIPLINEarray[e].pathsr)for(var n=t.CATANIMDISCIPLINEarray[e].pathsr.split(" Sep "),i=0;i<n.length;i++){var a=n[i].split(" "),r=a[2],s="rootBag",o=r.split("_"),l=(parseInt(o[1]),a.indexOf("PS")),c=a.indexOf("PE");if(-1!==l&&-1!==c)for(var u=l+1;u<c;u++){s=a[u];var p=t._Experience.pad3DViewer.getController().getInstanceParentReferenceId(s);if(!p)return!1}}return!0}(),d=[],h=t._Experience.getRootBag().children[0];for(var C in h&&h.traverse(function(e){var n;(n=!0===t._OOCActive?S.getNodeID(e)?S.getNodeID(e):e.id:e.getID?e.getID():e.id)&&(0,d[n]=e),e.id=n}),t.CATANIMDISCIPLINEarray)if(t.CATANIMDISCIPLINEarray[C].pathsr)for(var D=t.CATANIMDISCIPLINEarray[C].pathsr.split(" Sep "),v=D.length,f=0;f<v;f++){var g=D[f].split(" "),y=g[2],P=d[C];if(p&&!P&&!0===t._OOCActive&&(P=new A(C),d[C]=P),P)if(!0===t._OOCActive){var m=[],E="rootBag";m.push(E),P.SemanticRelations=P.SemanticRelations||{};var T=y.split("_"),_=parseInt(T[1]),N=g.indexOf("PS"),x=g.indexOf("PE");if(-1!==N&&-1!==x){for(var O=N+1;O<x;O++){E=g[O];var R=t._Experience.pad3DViewer.getController().getInstanceParentReferenceId(E);if(R)-1===(y=m.indexOf(R))&&m.push(R);m.push(E)}r&&console.log("PATH ",K.getKey())}else r&&console.log("Error parsing SemanticRelations ",D[f]);P.SemanticRelations[_]=m}else{var K=new i;K.addElement(h),P.SemanticRelations=P.SemanticRelations||{};T=y.split("_"),_=parseInt(T[1]),N=g.indexOf("PS"),x=g.indexOf("PE");if(-1!==N&&-1!==x){for(O=N+1;O<x;O++){var b=d[g[O]];b&&(K.addElement(b),O<x-1&&1==b.children.length&&K.addElement(b.children[0]))}r&&console.log("PATH ",K.getKey())}else r&&console.log("Error parsing SemanticRelations ",D[f]);P.SemanticRelations[_]=K}}h.traverse(function(e){e.SemanticRelations&&r&&console.log("DEBUG",e.SemanticRelations)});var M=[];for(var C in t.CATANIMDISCIPLINEarray)t.CATANIMDISCIPLINEarray[C]["bo.streamdescriptors"]&&M.push(C);I.Fetch(t._Experience,M,function(e){var n,i={};try{n=JSON.parse(e).results}catch(e){}for(var c=n.length;c--;){var u=n[c];if(u){for(var p,A,I=u.attributes.length;I--;){var C=u.attributes[I];"anm"===C.name?p=C.value:"physicalid"===C.name&&(A=C.value)}i[A]=p}}l.init();var D=[],v=function(e,n,i,a){var r;void 0!==n&&(r=!0===t._OOCActive?S.getNodeID(n)||n.name:n.getID?n.getID():n.id,l.loadRepresentation(n,r,new Blob([e],{type:"application/octet-stream"}),function(e){!function(e,n){var i;i=!0===t._OOCActive?S.getNodeID(e)||e.name:e.getID?e.getID():e.id;var a=l.listRootFeatures(i);e.AnimFeatures=a,n.AnimFeaturesList=n.AnimFeaturesList||[],Array.prototype.push.apply(n.AnimFeaturesList,e.AnimFeatures)}(n,h),i()}))};Object.keys(i).forEach(function(e){D.push(new a(function(t,n){var a,s,l;a=i[e],s=function(n){r&&console.log(n),v(n,d[e],t)},l=function e(){r&&console.log(e),n()},o.handleRequest(a,{authentication:"passport",method:"GET",type:"arraybuffer",timeout:5e4,onComplete:s,onTimeout:l,onFailure:l,onCancel:l})}))}),a.all(D).then(function(){i=null,d=null,D=[],s()})},function(){r&&console.error(arguments)})}},!0===t._OOCActive?I.ExpandForAnimDiscipline(t._Experience,u,d,C):I.Expand3d(t._Experience,u,d,C)}else s()}))},onClick:function(e){},_initScenarios:function(){var e=this;h.push(this.scenarioManager.addScenario({registerID:this.registerID,scenario:t.extend({launch:function(){e.onClick(this._parent.bind(this)),this._parent()}}),available:!1,name:"Animation"}))}})});