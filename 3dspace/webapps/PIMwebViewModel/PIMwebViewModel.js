/*!  Copyright 2018 Dassault Systemes. All rights reserved. */
define("DS/PIMwebViewModel/PIMwebUtils",["DS/WAFData/WAFData","DS/PlatformAPI/PlatformAPI"].concat([]),function(e,t){"use strict";let i={get3DSpaceUrl:()=>require("DS/PADUtils/PADUtilsServices").get3DSpaceUrl(),getPlatformId:()=>require("DS/PADUtils/PADUtilsServices").getPlatformId(),getSecurityContext:()=>require("DS/PADUtils/PADSettingsMgt").getSetting("pad_security_ctx"),getLang:()=>require("DS/PADServices/utils/GlobalUtils").getLang()};function n(e){var i=t.getUser();return e+"-"+(i?i.login:"")+"-"+Date.now()}function r(e,t,n,r){let s=Object.assign({},e);return s.method=t,s.url=i.get3DSpaceUrl()+n+"?tenant="+i.getPlatformId(),s.securityContext=i.getSecurityContext(),r&&(s.data=r),s}function s(t){return e.authenticatedRequest(t.url+"&SecurityContext="+t.securityContext,{method:t.method,headers:{Accept:"application/json","Content-Type":"application/json",SecurityContext:t.securityContext},data:JSON.stringify(t.data),timeout:t.timeout,onComplete:t.onComplete,onFailure:t.onFailure,onTimeout:t.onTimeout||t.onFailure})}var o=Object.create({}),a=Object.getPrototypeOf(o);function c(e){let t=new Blob([e.stream],{type:"application/json"}),i=new FormData;i.append("__fcs__jobTicket",e.preCheckIn.checkinTicket),i.append("file_0",t,"WebIsrScenarioContent.json");var n=new XMLHttpRequest;n.open("POST",e.preCheckIn.actionURL,!0),n.onload=function(){if(200===n.status){s(r({onComplete:e.onComplete,onFailure:e.onFailure},"POST","/resources/pimauthoring/service/checkinStreamInfo",{repRefId:e.repRefID,receipt:n.responseText,simuContent:e.stream}))}else e.onFailure()},n.send(i)}a._DEFAULT_PAGGING=500,a._navigateFromIsr=function(e){let t,i=[],a=t=>{i.forEach(e=>e.cancel()),e.onFailure(t)},c=i=>(i=JSON.parse("string"==typeof i?i:null))?t?(t.result&&t.result.length?i.result&&i.result.length&&(Object.assign(t.result[0].input_object,i.result[0].input_object),Object.assign(t.result[0].outputs,i.result[0].outputs)):t=i,delete t.version,void e.onComplete(JSON.stringify(t))):t=i:a(),l={onComplete:c,onFailure:a},u={onComplete:c,onFailure:a,isrID:e.isrID};return i.push(s(r(l,"POST","/cvservlet/navigate",{input_physical_ids:[e.isrID],label:n("PIMwebIsr"),primitives:[{navigate_to_rel:{id:1,filter:{bo_type:["dsc_Result_Category_Ref"]}}},{navigate_to_rel:{id:2}}],patterns:{itfCtxt:[{id:1},{id:2}]},fileAttributes:[],attributes:["ds6w:label","ds6wg:EnterpriseExtension.V_PartNumber","ds6w:responsible","owner","ds6w:type","ds6w:description"],version:3}))),i.push(o._navigateFromIsrToContext(u)),{cancel:()=>i.forEach(e=>e.cancel())}},a._navigateFromIsrToContext=function(e){return s(r({onComplete:t=>{if(!(t=JSON.parse("string"==typeof t?t:null))||!t.infos||"OK"!==t.infos.status||!t.result)return e.onFailure();e.onComplete(JSON.stringify({infos:t.infos,result:t.result.map(e=>({input_object:e.input,outputs:e.output}))}))},onFailure:e.onFailure},"POST","/resources/pimauthoring/service/readItfSimulationContent",Array.isArray(e.isrID)?e.isrID:[e.isrID]))},a._navigateFromContextualInterferences=function(e){return s(r(e,"POST","/cvservlet/navigate",{input_physical_ids:e.ctxIDs,label:n("PIMwebItfCtxt"),primitives:[{navigate_to_sr:{id:1,filter:{semantics:["6"],role:["198"]},mode:"last"}},{navigate_to_rel:{id:2}},{navigate_to_sr:{id:3,filter:{semantics:["5"],role:["201"]},mode:"path"}},{navigate_to_sr:{id:4,filter:{semantics:["5"],role:["197"]},mode:"path"}},{navigate_to_sr:{id:5,filter:{semantics:["6"],role:["200"]},mode:"path"}},{navigate_to_rel:{id:6,filter:{bo_type:["PLMPIMGeometricResultRepReference"]}}}],patterns:{ms:[{id:1}],r2sca:[{id:4}],sca2occ:[{id:1},{id:2},{id:3}],sca:[{id:1},{id:2},{id:5}],repRef:[{id:1},{id:6}]},attributes:["ds6w:label","ds6wg:EnterpriseExtension.V_PartNumber","ds6w:responsible","owner","ds6w:type","ds6w:status","ds6w:description","ds6w:project","ds6wg:revision","organization","current","bo.PLMPIMMetricReference.V_Itf_Analysis","bo.PLMPIMMetricReference.V_Itf_Previous_Analysis","bo.PLMPIMMetricReference.V_Itf_Type","bo.PLMPIMMetricReference.V_Itf_User_Type","bo.PLMPIMMetricReference.V_Itf_Real_Tolerance_Used_For_Computation","bo.PLMPIMMetricReference.V_Itf_Spec_Clearance_value","bo.PLMPIMMetricReferenceClashContact.V_Itf_Penetration_Value","bo.PLMPIMMetricReferenceClashContact.V_Itf_Clash_Volume","bo.PLMPIMMetricReferenceClashContact.V_Itf_FirstPointFoundClash","bo.PLMPIMMetricReferenceClashContact.V_Itf_SecondPointFoundClash","bo.PLMPIMMetricReferenceClearance.V_Itf_Distance_Min","bo.PLMPIMMetricReferenceClearance.V_Itf_FirstPointFoundClearance","bo.PLMPIMMetricReferenceClearance.V_Itf_SecondPointFoundClearance","bo.PLMPIMMetricReferenceFeature.V_Itf_Feature_UUID_1","bo.PLMPIMMetricReferenceFeature.V_Itf_Feature_UUID_2","bo.PLMPIMMetricReferenceFeature.V_Itf_Feature_Name_1","bo.PLMPIMMetricReferenceFeature.V_Itf_Feature_Name_2"],fileAttributes:[],version:3}))},a._setAttrValue=function(e){return e.data.attrValue=String(e.data.attrValue),s(r(e,"POST","/resources/PIMwebModel/services/setAttributeValue",e.data))},a._navigateFromMetricToIsr=function(e){return s(r(e,"POST","/cvservlet/navigate",{input_physical_ids:e.metricIDs,label:n("PIMwebItfMetric"),primitives:[{navigate_from_sr:{id:1,filter:{semantics:["6"],role:["198"]}}},{navigate_from_rel:{id:2}}],patterns:{itfCtxt:[{id:1}],isr:[{id:1},{id:2,iter:2}]},attributes:["ds6w:label","ds6w:responsible","owner","ds6w:type","ds6w:description"],fileAttributes:[],version:3}))},a._navigateOneLevel=function(e){return s(r(e,"POST","/cvservlet/navigate",{input_physical_ids:e.ids,label:n("PIMwebNavigateOneLevel"),primitives:[{navigate_to_rel:{id:1}}],patterns:{ref:[{id:1}]},attributes:["ds6w:label","ds6wg:EnterpriseExtension.V_PartNumber","ds6w:responsible","owner","ds6w:type","ds6w:project","ds6wg:revision","organization","current"],fileAttributes:[],version:3}))},a._getLabelAndIconAttr=function(e){return s(r(e,"POST","/cvservlet/fetch/v2",{physicalid:e.ids,label:n("PIMwebFetchForLabelandIcon"),select_predicate:["ds6w:label"],select_file:["icon"]}))},a._fetchForWelcomePanel=function(e){return s(r(e,"POST","/cvservlet/fetch/v2",{physicalid:e.ids,label:n("PIMwebFetchForWelcomePanel"),select_predicate:["ds6w:label","ds6w:type","ds6wg:revision","ds6w:status","owner","ds6w:responsible","ds6w:modified","ds6w:project","ds6w:identifier","ds6w:status"],select_file:["icon","thumbnail_2d"]}))},a._fetchValidationState=function(e){return s(r(e,"POST","/cvservlet/fetch/v2",{physicalid:e.ids,label:n("PIMwebFetchValidationState"),select_predicate:["bo.SIMItfSimulation.V_ValidationState"],select_file:[]}))},a._getInterferenceAnalysisProgress=function(e){return s(r(e,"POST","/resources/PIMwebModel/services/getInterferenceAnalysisProgress",e.ids))},a._updateIsrContextAndSpec=function(e){let t={onFailure:e.onFailure,onComplete:function(t){if(!(t=JSON.parse("string"==typeof t?t:null))||!t.infos||"OK"!==t.infos.status)return e.onFailure();c({stream:t.output.simuContent,repRefID:e.repRefID,preCheckIn:{checkinTicket:t.output.checkinTicket,actionURL:t.output.actionURL},onFailure:e.onFailure,onComplete:()=>e.onComplete({filter:t.output.filter})})}};return e.data.isrPid=e.isrID,s(r(t,"POST","/resources/pimauthoring/service/updateItfSimulationContent",e.data))},a._readIsrSpecStream=(()=>{let e,t=function(t,n){e||(e=n);try{e.Load({IRPC:!0,serverUrl:i.get3DSpaceUrl(),physicalid:t.repRefID,boType:"dsc_Scenario_CLASH_Rep",format:"1",persistancyType:"json",streamType:"text",onStreamLoaded:e=>t.onComplete({infos:{status:"OK"},stream:JSON.parse(e)}),onError:e=>t.onComplete({infos:{status:e}})})}catch(e){t.onComplete({infos:{status:"KO"}})}};return function(i){e?t(i,e):require(["DS/VisuDataAccess/Ox4StreamLoader"].concat([]),t.bind(null,i))}})(),a._createIsr=function(e){return s(r({onFailure:e.onFailure,onComplete:function(t){if(!(t=JSON.parse("string"==typeof t?t:null))||!t.infos||"OK"!==t.infos.status)return e.onFailure();c({stream:t.output.simuContent,repRefID:t.output.specID,preCheckIn:{checkinTicket:t.output.checkinTicket,actionURL:t.output.actionURL},onFailure:e.onFailure,onComplete:()=>e.onComplete({isrID:t.output.pId})})}},"POST","/resources/pimauthoring/service/createInterferenceSimulation",e.data))},a._readMetricGeomStream=(()=>{let e,t=function(t,n){e||(e=n);try{e.Load({IRPC:!0,serverUrl:i.get3DSpaceUrl(),physicalid:t.repRefID,boType:"PLMPIMGeometricResultRepReference",persistancyType:"json",streamType:"text",onStreamLoaded:e=>t.onComplete({infos:{status:"OK"},buffer:e.replace(/\0+$/,"")}),onError:e=>t.onComplete({infos:{status:e}})})}catch(e){t.onComplete({infos:{status:"KO"}})}};return function(i){e?t(i,e):require(["DS/VisuDataAccess/Ox4StreamLoader"].concat([]),t.bind(null,i))}})();var l,u,f=["SIMItfSimulationDS","ENOSTItfSimulationDS"];function p(e,t){return e?Promise.resolve(e):new Promise(function(i){s(r({onComplete:function(n){e?i(e):(-1===(e="string"==typeof n?Object.keys(JSON.parse(n)):[]).indexOf(t)&&e.push(t),Object.freeze(e=e.filter(e=>-1===f.indexOf(e))),i(e))},onFailure:function(){i([t])}},"POST","/resources/dictionary/getTypeInfo",[t]))})}a._getIsrTypes=function(){return p(l,"SIMItfSimulation").then(e=>l=e)},a._getBundleFasternerTypes=function(){return p(u,"BundleFastener").then(e=>u=e)};var d,I={};return a._getTypeNLS=function(e){var t={},n=e.filter(function(e){var i=I[e];return t[e]=i||e,!i});return n.length?new Promise(function(e){require(["DS/FedDictionaryAccess/FedDictionaryAccessAPI"],function(r){r.getNlsOfPropertiesValues(JSON.stringify({"ds6w:type":n}),{tenantId:i.getPlatformId(),lang:i.getLang(),apiVersion:"R2019x",onComplete:function(i){Object.keys((i||{})["ds6w:type"]||{}).forEach(function(e){t[e]=I[e]=i["ds6w:type"][e]}),e(t)},onFailure:function(){e(t)}})})}):Promise.resolve(t)},a._getCreationParams=function(e){return s(r({onFailure:e.onFailure,onComplete:function(t){return(t=JSON.parse("string"==typeof t?t:null))&&t&&t.infos&&"OK"===t.infos.status&&t.result?void a._getTypeNLS(l).then(function(i){t.result.types=i,e.onComplete(t.result)}):e.onFailure()}},"POST","/resources/pimauthoring/service/generateName",{type:e.type,lang:i.getLang()}))},a._sendUsageProbe=function(e,t,i){if(d){var n=d.getPADTracker({eventCategory:"usage",eventAction:e}),r={eventLabel:t};void 0!==i&&(r.eventValue=i),n.setEventData(r),n.trackPageEvent()}else require(["DS/PADServices/utils/PADTrackerManager"],function(e,t,i,n){d=n,this._sendUsageProbe(e,t,i)}.bind(this,e,t,i))},a._setRequestUtils=function(e){e.get3DSpaceUrl&&(i.get3DSpaceUrl=e.get3DSpaceUrl),e.getPlatformId&&(i.getPlatformId=e.getPlatformId),e.getSecurityContext&&(i.getSecurityContext=e.getSecurityContext),e.getLang&&(i.getLang=e.getLang)},a.getPlatformId=(()=>i.getPlatformId()),a.getLang=(()=>i.getLang()),o}),define("DS/PIMwebViewModel/PIMwebIsr",["UWA/Core","UWA/Class","DS/CoreEvents/ModelEvents","DS/PIMwebViewModel/PIMwebUtils"],function(e,t,i,n){"use strict";let r,s,o,a=1,c="ds6w:label",l="ds6w:type",u="ds6wg:EnterpriseExtension.V_PartNumber",f="ds6w:responsible",p="owner",d="ds6w:description",I="ds6wg:revision",h="ds6w:project",_="ds6w:status",m={1:"Clash",2:"Contact",3:"Clearance",4:"Contact",5:"NoInterference",6:"Undefined"},P={Clash:1,Contact:2,Clearance:3,NoInterference:5,Undefined:6},y={1:"OK",2:"KO",3:"NotAnalyzed",4:"NotAnalyzed",5:"NotAnalyzed"},g={1:"OK",2:"KO",3:"NotAnalyzed",4:"",5:"NotAnalyzed"},b={OK:1,KO:2,NotAnalyzed:3},M={"ds6w:description":"itfDescription","bo.PLMPIMMetricReference.V_Itf_Analysis":"itfAnalysis","bo.PLMPIMMetricReference.V_Itf_Previous_Analysis":"itfPrevAnalysis","bo.PLMPIMMetricReference.V_Itf_Type":"itfSystemType","bo.PLMPIMMetricReference.V_Itf_User_Type":"itfUserType","bo.PLMPIMMetricReference.V_Itf_Real_Tolerance_Used_For_Computation":"itfTolerance","bo.PLMPIMMetricReference.V_Itf_Spec_Clearance_value":"itfClearVal","bo.PLMPIMMetricReferenceClashContact.V_Itf_Penetration_Value":"itfPenVec","bo.PLMPIMMetricReferenceClashContact.V_Itf_Clash_Volume":"itfPenVol","bo.PLMPIMMetricReferenceClearance.V_Itf_Distance_Min":"itfMinDist","bo.PLMPIMMetricReferenceClashContact.V_Itf_FirstPointFoundClash":"itfQtfPt1","bo.PLMPIMMetricReferenceClashContact.V_Itf_SecondPointFoundClash":"itfQtfPt2","bo.PLMPIMMetricReferenceClearance.V_Itf_FirstPointFoundClearance":"itfQtfPt1","bo.PLMPIMMetricReferenceClearance.V_Itf_SecondPointFoundClearance":"itfQtfPt2","bo.PLMPIMMetricReferenceFeature.V_Itf_Feature_UUID_1":"itfFeat1","bo.PLMPIMMetricReferenceFeature.V_Itf_Feature_UUID_2":"itfFeat2","bo.PLMPIMMetricReferenceFeature.V_Itf_Feature_Name_1":"itfFeatName1","bo.PLMPIMMetricReferenceFeature.V_Itf_Feature_Name_2":"itfFeatName2","ds6w:responsible":"itfOwner",owner:"itfOwnerID","ds6w:status":"itfCurrent"},C=["physicalid","type","itfMetricName","itfOwner","itfOwnerID","itfCurrent","itfDescription","itfAnalysis","itfPrevAnalysis","itfSystemType","itfUserType","itfTolerance","itfClearVal","itfPenVec","itfPenVol","itfMinDist","itfQtfPt1","itfQtfPt2","itfFeat1","itfFeat2","itfFeatName1","itfFeatName2"],v=["physicalid","itfCtxName"];var D={};function F(e){var t=Number(e);return isNaN(t)||t<0?null:t}function w(e){var t=Number(e);return isNaN(t)?null:t}function S(e){return Object.keys(e).forEach(function(t){var i=M[t];i&&(e[i]=e[t],delete e[t])}),e.itfCurrent&&(e.itfCurrent=D[e.itfCurrent]||e.itfCurrent),e}function O(e){return e.itfCtxName=e[c],S(e),Object.keys(e).forEach(function(t){-1===v.indexOf(t)&&delete e[t]}),e}function x(e){return e.itfMetricName=e[c],e.type=e[l],S(e),Object.keys(e).forEach(function(t){-1===C.indexOf(t)&&delete e[t]}),e}function V(e){if(!e)return null;var t={physicalid:e.physicalid,label:e[c],owner:e[f],ownerID:e[p],type:e[l],revision:e[I],project:e[h],current:e.current?e.current:e[_],organization:e.organization};return e[u]&&(t.pn=e[u]),o&&o.indexOf(t.type)>-1&&(t.isBundleFst=!0),Object.freeze(t)}function L(e){var t=String(window.widget?window.widget.id+"_":"")+e;window.top.performance.measure(t,t)}function N(e){window.top.performance.mark(String(window.widget?window.widget.id+"_":"")+e)}var R,T,A,U,E=(R=[],T=0,A=0,U=function(e,t,i){L("PIM_QueryCtx_"+e),T--,E(),setTimeout(function(){i(t)},0)},function(t){t&&R.push(t),R.length&&T<5&&(T++,A++,function(t,i){N("PIM_QueryCtx_"+i);var r=e.clone(t,!1);r.onComplete=function(e){U(i,e,t.onComplete)},r.onFailure=function(e){U(i,e,t.onFailure)},r.onTimeout=function(e){U(i,e,t.onTimeout||t.onFailure)},n._navigateFromContextualInterferences(r)}(R.shift(),A))});return t.extend({init:function(e){let t=this,u={physicalid:e.id,isrContext:[],isrSpec:null,nbItfs:-2,itfs:{}},I=new i,h=function(e,i){return I.publish({event:e,data:i,context:t})};function _(){o=null,n._getBundleFasternerTypes().then(function(e){o=e.slice()})}function M(e){if(e&&e.contextualIDs&&e.onProgress&&e.onComplete&&e.onFailure)if(0!==e.contextualIDs.length){var t,i=Math.floor((e.contextualIDs.length-1)/n._DEFAULT_PAGGING)+1,o=0,a=[],c=!1;for(t=function(t){e.onProgress({itfIDs:t.itfIDs,isrID:u.physicalid,metrics:t.metricIDs});var i=t.itfIDs,o=[],a=u.isrContext&&u.isrContext.length?u.isrContext[0].prd.physicalid:null;a&&i.forEach(function(e){var t=u.itfs[e];t.r2sca&&t.r2sca.forEach(function(e){-1===o.indexOf(e.physicalid)&&o.push(e.physicalid)}),t.metricID&&r[t.metricID]&&r[t.metricID].sca2occ&&r[t.metricID].sca2occ.forEach(function(e){for(var t=0,i=e&&e.length>2?e.length-2:-1;t<i;++t)-1===o.indexOf(e[t].physicalid)&&o.push(e[t].physicalid)})}),o.length?n._navigateOneLevel({ids:o,onComplete:function(n){var o,c,f=JSON.parse(n),p={};f&&f.infos&&"OK"===f.infos.status&&f.result&&(f.result.forEach(function(e){o=e.input_object,(c=e.outputs&&e.outputs.ref&&e.outputs.ref.length?e.outputs.ref[0]:null)&&(s[c.physicalid]||(s[c.physicalid]=V(c)),p[o.physicalid]=c.physicalid)}),t.metricIDs.forEach(function(e){var t=r[e];t?!t.path&&t.sca&&t.sca2occ&&t.sca2occ.length&&(t.path=[null,null],t.sca2occ.forEach(function(e,t){if(e){var i;for(this[t]=[],i=0;i<e.length-2;++i){var n=e[i].physicalid;if(this[t].push(n),!(n=p[n])){this[t]=null;break}this[t].push(n)}this[t]&&(this[t].push(e[i].physicalid),this[t].push(e[i+1].physicalid))}},t.path),t.path[0]||t.path[1]||(t.path=null),delete t.sca2occ):window.console.log("No metric! At this point this is not possible")}),i.forEach(function(e){var t,i=u.itfs[e],n=r[i.metricID],s=[];a&&(n&&n.path&&(s.push(a),(i.r2sca||[]).some(function(e){var t=e.physicalid;if(s.push(t),!(t=p[t]))return!0;s.push(t)})||(i.path=[null,null],(t=s[s.length-1])&&n.path[0]&&t===n.sca&&(i.path[0]=s.concat(n.path[0])),t&&n.path[1]&&t===n.sca&&(i.path[1]=s.concat(n.path[1])))),delete i.r2sca)}),e.onProgress({interferingPathsReady:!0,itfIDs:i,isrID:u.physicalid,metrics:t.metricIDs}),l({itfIDs:i}))},onFailure:function(){l({itfIDs:i})}}):l({itfIDs:i})};o<i&&!c;)performance.mark("iteration"+o),E({ctxIDs:e.contextualIDs.slice(o*n._DEFAULT_PAGGING,(o+1)*n._DEFAULT_PAGGING),onComplete:function(e){if(!c){var i=JSON.parse(e);if(i&&i.infos&&"OK"===i.infos.status&&i.result){var n,o,a,l,f,p,d,I,h,_,m,P,y=(i=i.result).length,g=[],b=[];for(l=0;l<y;++l){if(o=O((n=i[l]).input_object),a=n.outputs.ms?n.outputs.ms[0]:null,g.push(o.physicalid),a&&-1===b.indexOf(a.physicalid)&&b.push(a.physicalid),a&&!r[a.physicalid]?r[a.physicalid]=x(a):a&&r[a.physicalid]&&(a=r[a.physicalid]),a&&!a.ctxt&&(a.ctxt=[]),a&&a.ctxt&&-1===a.ctxt.indexOf(o.physicalid)&&a.ctxt.push(o.physicalid),o.metricID=a?a.physicalid:null,u.itfs[o.physicalid]=o,n.outputs.r2sca&&n.outputs.r2sca.length)for(o.r2sca=[],f=0,_=n.outputs.r2sca[0].elements.length;f<_;++f)P=V(I=n.outputs.r2sca[0].elements[f]),s[I.physicalid]||(s[I.physicalid]=P),o.r2sca.push(P);if(n.outputs.sca&&n.outputs.sca.length>0&&a&&!a.sca&&(I=n.outputs.sca[0].elements[0],a.sca=I.physicalid,s[a.sca]||(s[a.sca]=V(I))),n.outputs.sca2occ&&a&&!a.sca2occ)for(a.sca2occ=[],f=0,_=n.outputs.sca2occ.length;f<_;++f){if(h=[],(d=n.outputs.sca2occ[f]).elements.length>1)for(p=0;p<d.elements.length;++p)P=V(I=d.elements[p]),s[I.physicalid]||(s[I.physicalid]=P),h.push(P);m="number"==typeof d.app_index&&d.app_index?d.app_index-1:"number"==typeof d.sr_id?d.sr_id-1:f,a.sca2occ[m]=h}n.outputs.repRef&&n.outputs.repRef.length&&a&&!a.repRef&&(a.repRef=n.outputs.repRef[0].physicalid)}t({itfIDs:g,metricIDs:b})}}},onFailure:function(t){c=!0,t||(t={}),t.step="Metrics",e.onFailure(t)}}),o++}else e.onComplete({itfIDs:[],metricIDs:[]});function l(t){c||(a.push.apply(a,t.itfIDs),t.error?(c=!0,t.error.step="Metrics",t.error.isrID=u.physicalid,e.onFailure(t.error)):a.length===e.contextualIDs.length&&e.onComplete({isrID:u.physicalid,itfIDs:a}))}}r={},s={},Object.keys(D).length||require(["DS/FedDictionaryAccess/FedDictionaryAccessAPI"],function(e){e.getNlsOfPropertiesValues(JSON.stringify({"ds6w:status":["VPLM_SMB_Evaluation.PRIVATE","VPLM_SMB_Evaluation.IN_WORK","VPLM_SMB_Evaluation.FROZEN","VPLM_SMB_Evaluation.RELEASED","VPLM_SMB_Evaluation.OBSOLETE"]}),{tenantId:n.getPlatformId(),lang:n.getLang(),apiVersion:"R2019x",onComplete:function(e){D=e["ds6w:status"]},onFailure:function(){}})}),this._fetch=function(e){u.physicalid&&e&&e.onProgress&&e.onComplete&&e.onFailure?e.contextualIDs?e.contextualIDs.length>0&&function(e){var t=a++;_(),N("PIM_QueryIsrToContext_"+t+"_"+u.physicalid),n._navigateFromIsrToContext({isrID:u.physicalid,onComplete:function(i){L("PIM_QueryIsrToContext_"+t+"_"+u.physicalid);var n,r,o,a=JSON.parse(i);a&&a.result&&1===a.result.length?(u.label=a.result[0].input_object[c],u.type=a.result[0].input_object[l],u.description=a.result[0].input_object[d],u.owner=a.result[0].input_object[f],u.ownerID=a.result[0].input_object[p],n=a.result[0].outputs&&a.result[0].outputs.prd,r=a.result[0].outputs&&a.result[0].outputs.filter,o=a.result[0].outputs&&a.result[0].outputs.spec,n=V(n),r=V(r),u.isrSpec=o,u.isrContext=n?[{prd:n,filter:r}]:[],u.nbItfs=-1,n&&!s[n.physicalid]&&(s[n.physicalid]=n),M({contextualIDs:e.contextualIDs,onProgress:e.onProgress,onComplete:e.onComplete,onFailure:e.onFailure})):e.onFailure()},onFailure:function(){L("PIM_QueryIsrToContext_"+t+"_"+u.physicalid),e.onFailure()}})}(e):function(e){if(!u.physicalid)return e.onFailure({error:"No valid Simulation ID parameter (iSimulationID)"}),void e.onFailure({step:"Simulation"});_();var i=a++;N("PIM_QueryIsr_"+i+"_"+u.physicalid),n._navigateFromIsr({isrID:u.physicalid,onComplete:function(r){L("PIM_QueryIsr_"+i+"_"+u.physicalid),n._sendUsageProbe("load","Isr");var o,a=JSON.parse(r);if(!a||!a.result||1!==a.result.length)return void e.onFailure({step:"Simulation"});if(!(o=a.result[0])||0===Object.keys(o.outputs).length)return void e.onFailure({step:"Simulation"});u.label=o.input_object[c],u.type=o.input_object[l],u.description=o.input_object[d],u.owner=o.input_object[f],u.ownerID=o.input_object[p],u.nbItfs=o.outputs.itfCtxt?o.outputs.itfCtxt.length:0,n._sendUsageProbe("load","IsrNbItfs",u.nbItfs);let I=V(o.outputs.prd),h=V(o.outputs.filter),_=(o.outputs.itfCtxt||[]).map(function(e){return e.physicalid});u.isrSpec=o.outputs.spec,u.isrContext=I?[{prd:I,filter:h}]:[],I&&!s[I.physicalid]&&(s[I.physicalid]=I),e.onProgress({context:t.getIsrContexts()}),n._sendUsageProbe("load","IsrWithFilter",h?"Yes":"No"),M({contextualIDs:_,onProgress:e.onProgress,onComplete:e.onComplete,onFailure:e.onFailure})},onFailure:function(t){L("PIM_QueryIsr_"+i+"_"+u.physicalid),n._sendUsageProbe("load","IsrFailed"),t||(t={}),t.step="Simulation",e.onFailure(t)}})}(e):e.onFailure()},this.getAttributes=function(){return{physicalid:u.physicalid,label:u.label,type:u.type,description:u.description,owner:u.owner,ownerID:u.ownerID}},this.getIsrContexts=function(){return JSON.parse(JSON.stringify(u.isrContext))},this.getItfIDs=function(){return Object.keys(u.itfs)},this.getInterferences=function(e){var t=[];return(e?Array.isArray(e)?e:[e]:Object.keys(u.itfs)).forEach(function(e){var i=u.itfs[e],n=r[i.metricID],o={itfuid:e,itfModelId:u.physicalid,itfCtxName:i.itfCtxName,itfPath1:i.path&&i.path.length&&i.path[0]?i.path[0].map(function(e){return s[e]}):null,itfPath2:i.path&&i.path.length>1&&i.path[1]?i.path[1].map(function(e){return s[e]}):null};n?(o.itfMetricName=n.itfMetricName,o.itfSystemType=m[n.itfSystemType],o.itfUserType=m[n.itfUserType],o.itfAnalysis=y[n.itfAnalysis],o.itfPrevAnalysis=g[n.itfPrevAnalysis],o.itfPenVec=F(n.itfPenVec),o.itfPenVol=F(n.itfPenVol),o.itfMinDist=F(n.itfMinDist),o.itfDescription=n.itfDescription,o.itfTolerance=F(n.itfTolerance),o.itfClearVal=F(n.itfClearVal),o.itfOwner=n.itfOwner,o.itfOwnerID=n.itfOwnerID,o.itfCurrent=n.itfCurrent,o.itfQtfPt1=(o.itfPenVec||o.itfPenVol||o.itfMinDist)&&n.itfQtfPt1&&3===n.itfQtfPt1.length?n.itfQtfPt1.map(w):null,o.itfQtfPt2=(o.itfPenVec||o.itfPenVol||o.itfMinDist)&&n.itfQtfPt2&&3===n.itfQtfPt2.length?n.itfQtfPt2.map(w):null,o.itfQtfPt1&&o.itfQtfPt2||(o.itfQtfPt1=o.itfQtfPt2=null),o.itfFeat1=n.itfFeat1,o.itfFeat2=n.itfFeat2,o.itfFeatName1=n.itfFeatName1,o.itfFeatName2=n.itfFeatName2,o.mID=n.physicalid,o.mType=n.type,o.mRepRef=n.repRef,o.msca=n.sca):(o.itfMetricName=o.itfSystemType=o.itfUserType=o.itfAnalysis=o.itfPrevAnalysis=o.itfPenVec=o.itfPenVol=o.itfMinDist=o.itfClearVal=void 0,o.itfDescription=o.itfTolerance=o.itfOwner=o.itfOwnerID=o.itfCurrent=o.itfQtfPt1=o.itfQtfPt2=void 0,o.itfFeat1=o.itfFeat2=o.itfFeatName1=o.itfFeatName2=o.mID=o.mType=o.mRepRef=o.msca=void 0),t.push(o)}),t},this.updateInterference=function(e){if(e.id&&u.itfs[e.id]&&"string"==typeof e.param){var t,i,s,o=u.itfs[e.id],a=r[o.metricID];switch(e.param){case"itfCtxName":t=o.physicalid,i="PLMEntity.V_Name",s=e.newValue;break;case"itfMetricName":t=a?a.physicalid:null,i="PLMEntity.V_Name",s=e.newValue;break;case"itfUserType":t=a?a.physicalid:null,i="PLMPIMMetricReference.V_Itf_User_Type",s=P[e.newValue];break;case"itfAnalysis":t=a?a.physicalid:null,i="PLMPIMMetricReference.V_Itf_Analysis",s=b[e.newValue];break;case"itfDescription":t=a?a.physicalid:null,i="PLMEntity.V_description",s=e.newValue}if(t){var c={data:{id:t,attrName:i,attrValue:s},onComplete:function(t){var i="string"==typeof t?JSON.parse(t):{},n=[];i&&"success"===i.status?"itfCtxName"===e.param?(o.itfCtxName=s,n.push(e.id)):(a[e.param]=s,Array.prototype.push.apply(n,a.ctxt)):n.push(e.id),h("itfUpdateSuccess",n.map(function(e){return{id:e}}))},onFailure:function(){h("itfUpdateFailed",[{id:e.id}])}};n._setAttrValue(c)}else setTimeout(function(){h("itfUpdateFailed",[{id:e.id}])},0)}else h("itfUpdateFailed",[{id:e.id}])},this.getLoadStatus=(()=>-2===u.nbItfs?"notLoaded":-1===u.nbItfs?"partiallyLoaded":"fullyLoaded"),this.getSpecAndContextInfo=function(e){if(!u.isrSpec||!u.isrSpec.physicalid)return window.console.error("isr without context and spec"),e.onFailure({error:"No specification available"});n._readIsrSpecStream({repRefID:u.isrSpec.physicalid,onComplete:function(i){if(!i||!i.infos||"OK"!==i.infos.status||!i.stream)return e.onFailure(i.infos.status);var n=i.stream;n.context=t.getIsrContexts()[0],n.isEditable=u.isrSpec.isEditable,e.onComplete(n)},onFailure:e.onFailure})},this.setSpecAndContextInfo=function(e){let t=u.isrSpec;if(!t||!t.physicalid||!t.isEditable)return window.console.error("isr without context and spec"),e.onFailure({error:"No specification available"});n._updateIsrContextAndSpec({isrID:u.physicalid,repRefID:t.physicalid,data:e.data,onFailure:e.onFailure,onComplete:t=>{u.isrContext[0].filter=V(t.filter),e.onComplete()}})},this.subscribe=function(e,t){return e&&t?I.subscribe({event:e},t):null},this.unsubscribe=function(e){null!=e&&I.unsubscribe(e)}}})}),define("DS/PIMwebViewModel/PIMwebIsrMgr",["DS/PIMwebViewModel/PIMwebIsr","DS/PIMwebViewModel/PIMwebUtils","DS/CoreEvents/ModelEvents"],function(e,t,i){"use strict";var n={},r={},s=new i;function o(e,t){return s.publish({event:e,data:t})}function a(t){var i=n[t];if(!i){var s=new e({id:t});n[t]=i={isr:s,lock:0},r[t]=[],r[t].push(s.subscribe("itfUpdateSuccess",function(e){e.isrID=t,o("itfUpdateSuccess",e)})),r[t].push(s.subscribe("itfUpdateFailed",function(e){e.isrID=t,o("itfUpdateFailed",e)}))}return i.isr}return Object.freeze({subscribe:function(e,t){return!e||!t||"itfUpdateSuccess"!==e&&"itfUpdateFailed"!==e?(window.console.error("Invalid event name"),null):s.subscribe({event:e},t)},unsubscribe:function(e){if(null!=e)return s.unsubscribe(e)},getLoadedIsr:function(){var e={};return Object.keys(n).forEach(function(t){e[t]=n[t].isr}),e},getCreationParams:e=>t._getCreationParams(e),createIsr:e=>t._createIsr(e),getIsr:function(e){var t=n[e];return t?t.isr:null},loadIsr:function(e){if(e&&e.isrID&&e.onProgress&&e.onComplete&&e.onFailure){var t=e.isrID,i=a(t);"fullyLoaded"!==i.getLoadStatus()?i._fetch({onProgress:function(n){e.onProgress({itfIDs:n.itfIDs,context:n.context,interferingPathsReady:n.interferingPathsReady,isrID:t,isr:i})},onComplete:function(){e.onComplete({isrID:t,isr:i,wasFullyLoaded:!1}),0===n[t].lock&&window.console.log("isr has not been locked!!!")},onFailure:function(r){delete n[t],i=null,e.onFailure(r)}}):(e.onProgress({itfIDs:i.getItfIDs(),context:i.getIsrContexts(),isrID:t,isr:i}),e.onComplete({isrID:t,isr:i,wasFullyLoaded:!0}))}else window.console.error("Invalid arguments")},loadIsrFromMetrics:function(e){e&&e.metrics&&e.onProgress&&e.onComplete&&e.onFailure?t._navigateFromMetricToIsr({metricIDs:e.metrics,onComplete:function(t){var i,r=JSON.parse(t),s={},o=[];r&&r.infos&&"OK"===r.infos.status&&r.result&&(r=r.result)&&Array.isArray(r)?(r.forEach(function(e){(i=e.outputs.isr?e.outputs.isr[0].physicalid:null)?(s[i]||(s[i]={itfC:[],itfM:[],label:e.outputs.isr[0]["ds6w:label"]}),s[i].itfC=s[i].itfC.concat(e.outputs.itfCtxt.map(function(e){return e.physicalid})),-1===s[i].itfM.indexOf(e.input_object.physicalid)&&s[i].itfM.push(e.input_object.physicalid)):o.push(e.input_object.physicalid)}),Object.freeze(s),e.onFilterIsr({isrIDs:s,metricsWithoutIsr:o,onComplete:function(t){var i=t.length,r=function(){0==--i&&e.onComplete()};if(0===t.length)return i=1,void r();t.forEach(function(t){if(s[t]){var i=a(t);i._fetch({contextualIDs:s[t].itfC,onProgress:e.onProgress,onComplete:function(){e.onComplete({isrID:t,isr:i,wasFullyLoaded:!1}),0===n[t].lock&&window.console.log("isr has not been locked!!!"),r()},onFailure:function(){e.onFailure({isrID:t,isr:i,wasFullyLoaded:!1})}})}})}})):e.onFailure()},onFailure:function(){e.onFailure()}}):e.onFailure()},lockIsr:function(e){var t=n[e];return t?(t.lock++,0):(window.console.log(e+" has not been loaded"),-1)},unlockIsr:function(e){var t=n[e];return t?(t.lock--,0===t.lock&&(t=n[e]=null,delete n[e]),0):(window.console.log(e+" has not been loaded"),-1)}})});