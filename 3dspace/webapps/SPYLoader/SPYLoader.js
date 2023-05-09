/*!  Copyright 2018 Dassault Systemes. All rights reserved. */
define("DS/SPYLoader/SPYStreamLoader",["UWA/Core","UWA/Class","DS/VisuDataAccess/Ox4PLMDataHelper","DS/VisuDataAccess/Ox4TicketGeneratorTask","DS/VisuDataAccess/Ox4XHRWithProxyAbstraction","DS/WAFData/WAFData"],function(e,t,n,i,r,s){"use strict";var o=null,a=null,d=function(e,t,n,i){var r={name:"",store:""};return r.name=e+"_'"+t+"'."+n,r.name+="="+i,r.format=n,r.persistencyName=i,r};return t.singleton({load:function(e){var t,u,l,c,h,f;null!==o&&null!==a&&a.url===e.serverUrl||(a={rawmode:!1,url:e.serverUrl},u=(t=a).url,t.xmqlServiceUri=function(){return u+"/i3dx/services/xmql"},t.threedexpServiceUri=function(){return u},r.useUWAProxy({proxymode:"passport"}),o=new n(a)),e.sd?(c=e.repId,h=e.sd,f=function(e){var t=/SDv2\((.*),'(.*)',(.*),.*,.*,'(.*)','(.*)'\)/.exec(e);return{format:t[1],role:t[2],lateType:t[3],persistencyType:t[4],persistencyName:t[5]}}(h),l=d(c,f.role,f.format,f.persistencyName)):l=d(e.repId,e.role,e.format,e.persistencyName),function(e,t,n,s){var d=o.transformSelectedFileIntoTicketRequest(e,t.format,t.store,t.name),u=new i(d,a);(new r).asyncTransformFCSUrlIntoRealUrl(u.generateRequest(),!1,function(e,t){n(t)})}(e.repId,l,function(t){!function(e,t,n,i,r,o){if("posthttp"==e.slice(0,8)){var a=e.indexOf(":postparams:"),d=e.slice(9,a),u=e.slice(a+12,e.length),l={cors:!0,timeout:isNaN(o)?0:o,method:"POST",data:u,responseType:t,async:!0,headers:{Accept:"text/plain"},onComplete:n,onFailure:i,onTimeout:r};s.authenticatedRequest(d,l)}}(t,e.streamType,function(t){e.onStreamLoaded(l.persistencyName,t)},e.onError,e.onTimeOut,e.timeout)},e.onError)}})}),define("DS/SPYLoader/SPYWSProxy",["UWA/Class","DS/WAFData/WAFData"],function(e,t){"use strict";var n,i=function(e){var t=12e4;return e>150&&(t*=e/150,t=Math.min(Math.round(t),24e4)),t},r=function(e,i,r,o){!function(e,n,i,r){var s=e.serverurl+"/cvservlet/expand?tenant="+e.tenant,o={timeout:2e4,onComplete:n,onCancel:i,onFailure:i,onTimeout:r,method:"POST",headers:{Accept:"application/json","Content-Type":"application/json","Accept-Language":"en"}},a={root_path_physicalid:[[e.physicalid]],select_bo:["physicalid","bo.streamdescriptors","bo.designsight.simulationorigin","type","label","v_description","revision","islastrevision","current"],type_filter_bo:["DesignSight","dsc_Result_Category_Ref","dsc_Result_rep"],label:"SimulationExperienceContent_Expand",tenant:e.tenant,locale:"en"};o.data=JSON.stringify(a),t.authenticatedRequest(s,o)}(e,function(e){var t,o,a=s.errorCodes.ERR_EXPANDCV_FAILED;if("string"==typeof e&&e.length>0){var d=JSON.parse(e).results;if(Array.isArray(d)&&d.length>=3){var u=d[0],l=d[2];if(l&&Array.isArray(l.attributes)&&u&&Array.isArray(u.attributes)){var c=(t=u.attributes,o="bo.designsight.simulationorigin",t.find(function(e){return e.name===o}));return n=c?c.value:void 0,void i(l.attributes,u.attributes)}}}r(a)},r,o)},s=e.singleton({errorCodes:{ERR_NOEXTDATA:"SPYWS_NOEXTDATA",ERR_UNEXPECTED:"SPYWS_UNEXPECTED",ERR_EXTDATA_PARSE:"SPYWS_EXTDATA_PARSE_FAILED",ERR_EXTDATA_NORUNS:"SPYWS_EXTDATA_NORUNS",ERR_EXTDATA_NOLW:"SPYWS_EXTDATA_NOLW",ERR_GETLWSTREAMS_NOLW:"SPYWS_GETLWSTREAMS_NOLW",ERR_EXPANDCV_FAILED:"SPYWS_EXPANDCV_FAILED"},getLatestSimulationOrigin:function(){return n},getResultExtendedData:function(e,t,n,i){Date.now();r(e,function(e,i){var r=e.find(function(e){return"bo.simobjsimulationv5repreferencegeneric.v_extendeddata"===e.name}),s=this.errorCodes.ERR_UNEXPECTED;if(void 0===r||"string"!=typeof r.value||r.value.length<=0)s=this.errorCodes.ERR_NOEXTDATA,n(s);else try{var o=JSON.parse(r.value);t(o,i)}catch(e){s=this.errorCodes.ERR_EXTDATA_PARSE,n(s)}}.bind(this),n,i)},_postRequestToSMAServices:function(e,n,i,r,s){var o={timeout:6e4,onComplete:function(e){var t=this.errorCodes.ERR_UNEXPECTED;if(void 0===e||"string"!=typeof e||e.length<=0)t=this.errorCodes.ERR_NOEXTDATA,r(t);else{try{var n=JSON.parse(e)}catch(e){t=this.errorCodes.ERR_EXTDATA_PARSE,r(t)}i(n)}}.bind(this),onCancel:r,onFailure:r,onTimeout:s,method:"POST",headers:{Accept:"application/json","Content-Type":"application/json","Accept-Language":"en"}},a={itemId:e.physicalid,clientContext:{}};a.clientContext[["e","k","a","r","i","p","m","u","i","l"].reverse().reduce(function(e,t){return e+t},"")]="7948163181467835",o.data=JSON.stringify(a),t.authenticatedRequest(n,o)},getAnalysisCases:function(e,t,n,i){var r=e.serverurl+"/resources/SMAPowerByServices/getAnalysisCases";this._postRequestToSMAServices(e,r,t,n,i)},getRuns:function(e,t,n,i){var r=e.serverurl+"/resources/SMAPowerByServices/getRuns";this._postRequestToSMAServices(e,r,t,n,i)},getLWDictionary:function(e,t,n,i,r){var s=r&&r.compareCVExpand;s&&this.getResultExtendedData(e,function(e,t){console.log(" ** sma getResultExtendedData onComplete => : ",e,t)}.bind(this),n,i);
//!!
//!!
var o=Date.now();this.getRuns(e,function(i){s&&console.log(" ** SPYLW - getRuns call took in ms: ",Date.now()-o);var r=this.errorCodes.ERR_UNEXPECTED,a=new Map,d={};if(i){d.boId=i.contentId,d.boCEstamp=i.cestamp,d.definitionProvider=e.serverurl,r=this.errorCodes.ERR_EXTDATA_NORUNS;var u=i.runs;if(u)this.errorCodes.ERR_EXTDATA_NOLW,Object.keys(u).forEach(function(e){var t=u[e];if(t&&t.lightweightViz){var n={name:t.name,lastRunID:e,lastUpdate:t.updateStamp,lw:t.lightweightViz};a.set(t.analysisCaseId,n)}})}a.size>0?t(a,d):n(r)}.bind(this),n,i)},loadContentStream:function(e,t,n,i){},getLWstreamUrl:function(e,n,i,s){r(e,function(r){!function(e,n,i,r,s){var o=e.serverurl+"/cvservlet/fetch/v2?tenant="+e.tenant,a={timeout:6e4,onComplete:i,onCancel:r,onFailure:r,onTimeout:s,method:"POST",headers:{Accept:"application/json","Content-Type":"application/json","Accept-Language":"en"}},d={physicalid:[n],fcs_url_mode:"REDIRECT",select_file:["xml"],select_predicate:["physicalid"],label:"SimulationExperienceContent_Fetch",tenant:e.tenant,locale:"en"};a.data=JSON.stringify(d),t.authenticatedRequest(o,a)}(e,r.id,n,i,s)},i,s)},getLWstreamsIds:function(e,t,s,o){n=void 0;var a=Date.now();r(e,function(e,n){var r=e.find(function(e){return"bo.streamdescriptors"===e.name});if(r){var o=r.value.split(":").filter(function(e){return e.contains("SimNavData_")});if(o.length>0){var d=Date.now()-a,u=i(d);t(e[1].value,o,u)}else s(this.errorCodes.ERR_GETLWSTREAMS_NOLW)}else s()}.bind(this),function(n){var r=e.serverurl+"/resources/SMAPowerByServices/getAttachedStreams";this._postRequestToSMAServices(e,r,function(e){if(e&&Array.isArray(e.streams)){var n=e.streams.filter(function(e){return"xml"===e.filetype&&e.name.contains("SimNavData_")});if(n.length>0){n.forEach(function(e,t,n){var i=e.locator.split(":");n[t]="SDv2(2,'EXTENDED',CATNonCATIADocument,'-1181347442','-1181347444','xml','"+i[4]+"')"});var r=Date.now()-a,o=2*i(r);t(e.contentId,n,o)}else s(this.errorCodes.ERR_GETLWSTREAMS_NOLW)}}.bind(this),s,o)}.bind(this),o)}});return s}),define("DS/SPYLoader/AbstractLoader",["UWA/Core","UWA/Class","DS/CoreEvents/Events","DS/WebUtils/ProbesManager","DS/SPYUtils/TrackingManager"],function(e,t,n,i,r){"use strict";return t.extend({_cleanCompleteCb:function(){this._sequenceCreatedCB&&(n.unsubscribe(this._sequenceCreatedCB),delete this._sequenceCreatedCB)},_cleanErrorCb:function(){this._sequenceErrorCB&&(n.unsubscribe(this._sequenceErrorCB),delete this._sequenceErrorCB)},_loadSequence:function(e,t){if(e&&this._listObjToLoad.push(e),!this._loading&&0!==this._listObjToLoad.length){var n=this._listObjToLoad.shift(),r=i.createProbe("SPY.SPYLoader.LOADSEQUENCE."+this._probesLoadSequence.length);this._probesLoadSequence.push(r),r.begin(),this._loading=!0,this._dmFactory.createSequence({src:n,spm:this._spm},t)}},_loadModel:function(){},init:function(){this._parent(),this._loading=!1,this._listObjToLoad=[],this._defaultAssetType=void 0,this._streamsInfo=[],this._typingInfo={},this._report={streamsInfo:[]},this._probesLoadSequence=[]},clean:function(){this._cleanCompleteCb(),this._cleanErrorCb(),this._spm=null,this._listObjToLoad=[],this._streamsInfo=[],delete this._dmFactory,this._report={streamsInfo:[]},this._probesLoadSequence.forEach(function(e){e&&e.end()})},onUnstreamComplete:function(e,t){this._cleanCompleteCb(),this._sequenceCreatedCB=n.subscribe({event:e+"/SPY/SEQUENCE/CREATED"},function(e){var n=this._probesLoadSequence[e.seqIdx];n&&n.end(),this._report&&e&&(this._report.filteredDDNb=e.filteredDDNb),t(e)}.bind(this))},onUnstreamError:function(e,t){this._cleanErrorCb(),this._sequenceErrorCB=n.subscribe({event:e+"/SPY/SEQUENCE/ERROR"},function(e){var n;(n=isNaN(e.seqIdx)?this._probesLoadSequence.pop():this._probesLoadSequence[e.seqIdx])&&n.end(),e&&"FILTEREDCONTENT"===e.errorCode?(this._report&&(this._report.filteredContent=!0),r.trackAllContentsFiltered(void 0,this._typingInfo)):(this._report&&(isNaN(this._report.invalidStreamNb)&&(this._report.invalidStreamNb=0),this._report.invalidStreamNb++),r.trackInvalidStreamContent(void 0,this._typingInfo)),this._loading=!1,t()}.bind(this))},onQueryStart:function(e,t){this._onQueryStart=t},onQueryProgress:function(e,t){this._onQueryProgress=t},onUnstreamStart:function(e,t){this._onUnstreamStart=t},onFileNotSupported:function(e,t){this._onFileNotSupported=t},onQueryError:function(e,t){this._onQueryError=t},onTimeOut:function(e,t){this._onTimeOut=t},load:function(e,t,n){this._spm=n,r.setTenant(e.tenant),this._spm.addAnalytics({providerType:e.provider,assetType:e.dtype||this._defaultAssetType,tenant:e.tenant,loadStart:Date.now()}),require([t],function(t){var n,s,o=function(){},a=this._onFileNotSupported?this._onFileNotSupported:o,d=this._onUnstreamStart?this._onUnstreamStart:o,u=this._onTimeOut?this._onTimeOut:o;this._dmFactory=new t;var l=this.getReport();if(l.streamNb=0,"FILE"===e.provider){var c="3dsim"===e.format||"simxml"===e.format,h=e.format===e.mimetype&&e.format===e.type;if(0===(s=e.filename&&e.filename.length>0?e.filename:e.serverurl).indexOf("https",0)&&s.indexOf(e.format,0)<0&&h){c?(l.streamNb=1,n={persistentId:s,index:0,provider:"FILE",filename:s,proxyurl:"none",withCredentials:!0},d(),this._loadSequence(n)):a()}else{var f=e.filename,p=new RegExp("[;]+","g"),m=f.split(p);l.streamNb=m.length;for(var _=0;_<m.length;_++)0!==(s=m[_]).indexOf("blob",0)&&s.indexOf("3dsim",0)<0&&s.indexOf("simxml",0)<0&&s.indexOf("nas",0)<0&&s.indexOf("inp",0)<0&&a(),n={persistentId:s,index:_,provider:"FILE",filename:s,serverurl:"",proxyurl:"none",requiredAuth:null,withCredentials:!0},d(),this._loadSequence(n)}}else if("BLOB"===e.provider)e.blob instanceof Blob&&(l.streamNb=1,n={provider:"BLOB",blob:e.blob,index:0,analytics:{dataSize:e.blob.size,streamingTime:0}},d(),this._loadSequence(n));else if("EV6"===e.provider||"3DSPACE"===e.provider){this._onQueryStart&&this._onQueryStart();var S=this._onQueryProgress?this._onQueryProgress:o,g=this._onQueryError?this._onQueryError:o,v=i.createProbe("SPY.SPYLoader.LOADMODEL");v.begin(),this._loadModel(e,S,function(e,t){v.end(),l&&(isNaN(l.streamTimeOut)||u()),t&&this._spm.setTypingInfo(t);var i=this;if(Array.isArray(e))e&&e.length>0&&(d(),e.forEach(function(e){n={persistentId:e.persistentId,index:e.index,provider:"BUFFER",buffer:e.data?e.data:e,analytics:{dataSize:e.dataSize,streamingTime:e.streamingTime}},i._loadSequence(n)}));else{var r=e;if(r.length>0){d();for(var s=new RegExp("[;]+","g"),o=r.split(s),c=0;c<o.length;c++){if(0!==(r=o[c]).indexOf("blob",0)&&r.indexOf("simxml",0)<0)return void a();n={provider:"FILE",filename:r,serverurl:"",proxyurl:"none",requiredAuth:null},this._loadSequence(n)}}}}.bind(this),function(e){switch(v.end(),e){case"NOACCESS":r.trackNoAccess(void 0,this._typingInfo);break;case"NOSTREAM":r.trackNoSimulationEC(void 0,this._typingInfo)}g(e)}.bind(this),function(e){r.trackECLoadingTimeOut(void 0,this._typingInfo,e)}.bind(this))}}.bind(this))},isLoadingComplete:function(){return 0===this._listObjToLoad.length&&!this._loading&&!this._spm.loading},continueLoading:function(){this._loading=!1,this._loadSequence()},getReport:function(){return this._report},resetReport:function(){this._report={streamsInfo:[]}},getDMFactory:function(){return this._dmFactory},extensionLoad:function(e,t,n,i){var r=function(){i&&i()},s=this.getDMFactory();if(s&&s.setExtendedMode&&this._spm&&this._streamsInfo){s.setExtendedMode(!0);var o=-1;e&&(o=this._spm.findSequenceByPersistentId(e)),o<0&&(o=this._spm.findSequenceByName(t));var a=this._streamsInfo[o];a?this._loadSequence({provider:"BUFFER",buffer:a.data,analytics:{dataSize:a.dataSize,streamingTime:a.streamingTime}},{onComplete:function(){n&&n()},onError:r}):r()}else r()}})}),define("DS/SPYLoader/SPYLoader",["UWA/Core","UWA/Class","DS/SPYLoader/AbstractLoader","DS/SPYLoader/SPYWSProxy","DS/SPYLoader/SPYStreamLoader"],function(e,t,n,i,r){"use strict";return n.singleton({_defaultAssetType:"DesignSight",_loadModel:function(e,t,n,s,o){this._typingInfo={assetType:e.dType||e.type,providerType:e.provider},this._streamsInfo=[],i.getAnalysisCases(e,function(e){if(e)for(var t in e.analysisCases){var n=e.analysisCases[t];this._typingInfo[t]={name:n.name,origin:n.origin,sequenceType:n.type,sequenceSubTypes:n.subType,featureId:n.featureId};var i=n.appData_NativeApp||n.appData_SimMgr;i&&(this._typingInfo[t].internalId=i.internalId,this._typingInfo[t].internalName=i.internalName)}}.bind(this),function(e){this._typingInfo.error=e}.bind(this),function(){this._typingInfo.timeout=!0}.bind(this)),i.getRuns(e,function(e){if(e)for(var t in e.runs){var n=e.runs[t];if(n.analysisCaseId){var i=this._typingInfo[n.analysisCaseId];i&&(i.resultSequenceId=t)}}}.bind(this),function(e){}.bind(this),function(){}.bind(this)),this._onFinish=n,i.getLWstreamsIds(e,function(n,a,d){if(this._typingInfo.simulationOrigin=i.getLatestSimulationOrigin(),r&&Array.isArray(a)){var u=0,l=a.length;this._report.streamNb=l,this._report.suggestedTimeout=d,t(0,{streamsLoaded:0,streamsTotal:l}),a.forEach(function(i,c){var h=Date.now(),f={repId:n,serverUrl:e.serverurl,sd:i,streamType:"arraybuffer",onStreamLoaded:function(e,n){l--;var i=a.length-l;t(100*i/a.length,{streamsLoaded:i,streamsTotal:a.length}),this._streamsInfo.push({persistentId:e,index:c,data:n,dataSize:n.byteLength,streamingTime:Date.now()-h}),0===l&&this._onFinish(this._streamsInfo,this._typingInfo)}.bind(this),timeout:d,onTimeOut:function(){u++,l--,this._report.streamTimeOut=u;var e=a.length-l;t(100*e/a.length),o&&o(Date.now()-h),0===l&&this._onFinish(this._streamsInfo,this._typingInfo)}.bind(this),onError:function(e){l--,s("NOACCESS")}};r.load(f)}.bind(this))}}.bind(this),function(e){this._typingInfo.simulationOrigin=i.getLatestSimulationOrigin(),e===i.errorCodes.ERR_GETLWSTREAMS_NOLW?s("NOSTREAM"):s("NOACCESS")}.bind(this),function(){this._typingInfo.simulationOrigin=i.getLatestSimulationOrigin(),s("NOACCESS")}.bind(this))},declareToPAD:function(e,t,n){if(e&&e.loadJSON){var i=[{rootID:t,validForServerCall:!0,structure:{rootStats:{infos:{encoding:"UTF-8",kind:"InstRef",version:"1.1"},results:[{resourceid:t,"ds6w:type":"DesignSight","ds6w:globaltype":"ds6w:Document","ds6w:label":n,boundingbox:{max:[-1,-1,-1],min:[1,1,1]}},{synthesis:{name:"ProgressiveExpand_FlatSynthesis",value:1}},{synthesis:{name:"ProgressiveExpand_OccSynthesis",value:1}},{synthesis:{name:"ProgressiveExpand_LeafSynthesis",value:1}}]},infos:{encoding:"UTF-8",kind:"InstRef",version:"1.1"},results:[{resourceid:t,"ds6w:type":"DesignSight","ds6w:globaltype":"ds6w:Document",boundingbox:{max:[-1,-1,-1],min:[1,1,1]}}]},refreshDelegationCB:function(){return new Promise(function(e){e()})}}];e.loadJSON({data:i})}},removeFromPAD:function(e,t){e&&t&&e.removeRoots({pids:[t]})}})});