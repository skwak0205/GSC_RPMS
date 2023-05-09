/*!  Copyright 2021 Dassault Systemes. All rights reserved. */
!function(){"use strict";let e,t,n,s,i,o,r,a={ctxt:0,spec:0};function l(n,s,i){let o;if(s){o=e.createElement("div",{class:"noPimData"}),new t({header:s,body:o,expandedFlag:!0,style:"divided"}).inject(n).elements.bodyContainer.setStyles({padding:"0 15px"})}else o=e.createElement("div",{class:"noPimData",style:{padding:"0 15px"}}).inject(n);o.textContent=i}let c=function(c){a[c]++;var u=this.elements.container;u.getChildren().forEach(e=>e.remove()),(n?Promise.resolve():new Promise(function(a){require(["UWA/Core","DS/Controls/Expander","DS/PIMwebViewModel/PIMwebUtils","DS/PIMwebViewModel/PIMwebIsrMgr","DS/PIMwebUX/PIMwebIsrPanelContextTab","DS/PIMwebUX/PIMwebIsrPanelSpecTab","i18n!DS/PIMwebPropertiesTabs/assets/nls/PIMwebPropertiesTabs.json","css!DS/PIMwebPropertiesTabs/assets/css/PIMwebPropertiesTabs"],function(l,c,u,d,p,f,h){e=l,t=c,s=d,i=p,o=f,r=h,(n=u)._setRequestUtils(this.options.requestUtils),a()}.bind(this))}.bind(this))).then(function(d,p){n._getIsrTypes().then(f=>{let h=this.getModel(),C=h&&h.getMergingModels&&h.getMergingModels()||h&&[h]||null,g=[];C&&(Object.keys(C).forEach(e=>{let t=C[e].get("_DS_ATTR_type");t="object"==typeof t?t.valueDB:t,f.indexOf(t)>-1&&g.push(C[e].get("objectId"))}),g.length&&g.forEach(f=>{let h=s.getIsr(f);(h&&"notLoaded"!==h.getLoadStatus()?Promise.resolve(h):new Promise(function(e){s.loadIsr({isrID:f,onProgress:()=>{},onComplete:t=>e(t.isr),onFailure:()=>e()})})).then(s=>{if(d===a[p])if(s)s.getSpecAndContextInfo({onComplete:function(r){if(d!==a[p])return;let l=u;if(g.length>1){let n=e.createElement("div",{class:s.getAttributes().label});new t({header:s.getAttributes().label,body:n,expandedFlag:!0,style:"divided"}).inject(u).elements.bodyContainer.setStyles({"padding-top":"0px","padding-right":"10px","padding-bottom":"10px","padding-left":"10px"}),l=n}if("ctxt"===p)(r.groups&&r.groups.length?new Promise(function(e){let t=[],s=r.groups.map(e=>({isFilter:"filter"===e.mode,Elements:e.content.map(e=>{let n=e[e.length-1];return t.push(n),{id:n}})}));n._getLabelAndIconAttr({ids:t,onComplete:function(t){if(d!==a[p])return;let n={};JSON.parse(t).results.forEach(e=>{let t={};e.attributes&&e.attributes.forEach(function(e){"resourceid"===e.name?n[e.value]=t:t[e.name]=e.value})}),s.forEach(e=>{e.Elements.forEach(e=>{e.label=n[e.id]["ds6w:label"],e.icons=[n[e.id].type_icon_url]})}),e(s)}})}):Promise.resolve([])).then(e=>{var t={Groups:e,Config:r.groupMode,ProductContext:{id:r.context.prd.physicalid,alias:r.context.prd.label,type:r.context.prd.type},FilterContext:r.context.filter?{id:r.context.filter.physicalid,alias:r.context.filter.label,type:r.context.filter.type}:null},n={type:"customFacetMode",parent:l,readOnly:!0};return n.contextInfo=t,new i(n)});else{var c={parent:l,type:"customFacetMode",readOnly:!0},f={ComputationMode:{},Fasteners:{},EngineeringCnx:{},KnowledgeRule:{},ReuseExistingItf:{},ComputeQuantifer:{},StoredResults:{}};f.ComputationMode.Clash=r.specStd.contains("Clash"),f.ComputationMode.Contact=r.specStd.contains("Contact"),f.ComputationMode.Clearance=r.specStd.contains("Clearance"),f.CheckInterRep=r.appendInterRep,f.CompBetweenIntermediateReps=r.computeInterRep,f.ComputationMode.ClearanceValue=(1e3*parseFloat(r.value_of_clearance)).toString()+"mm",f.Fasteners.FstChecked=r.checkFasteners,f.Fasteners.CheckItfWithFastenedParts=r.checkFasteneds,f.checkElec=Boolean(r.electricalItfs),f.checkPathways=Boolean(r.pathwayItfs);var h=[];h=r.specMCX.split("|"),f.EngineeringCnx.EnggCnxChecked=-1!==h.indexOf("on"),f.EngineeringCnx.CheckNoClash=-1!==h.indexOf("MCX_Check_NoClash"),f.EngineeringCnx.CheckContact=-1!==h.indexOf("MCX_Check_Contact"),f.EngineeringCnx.CheckClearance=-1!==h.indexOf("MCX_Check_Clearance"),f.EngineeringCnx.NoCheck=-1!==h.indexOf("MCX_Ignore"),f.ReuseExistingItf.DeleteOutOfScopeItf=r.destroyOutOfScope,f.ReuseExistingItf.RecomputeOnlyModItf=r.compare,r.clashQuantifier||r.volumeQuantifier?r.clashQuantifier&&!r.volumeQuantifier?f.StoredResults.ClashQuantifier=1:!r.clashQuantifier&&r.volumeQuantifier?f.StoredResults.ClashQuantifier=2:f.StoredResults.ClashQuantifier=3:f.StoredResults.ClashQuantifier=0,f.StoredResults.ClearanceQuantifier=r.clearQuantifier?1:0,f.StoredResults.ClashGeometry=r.clashGeometryStorage,f.StoredResults.ContactGeometry=r.contactGeometryStorage,f.StoredResults.ClearanceGeometry=r.clearanceGeometryStorage,f.StoredResults.Accuracy={},f.StoredResults.Accuracy.value=1e3*parseFloat(r.clearanceAreaGeometryAccuracy),f.StoredResults.Accuracy.units="mm",f.StoredResults.GeometryDetail=r.geometryDetail;var C=[];if(C=r.specRule.split("|"),f.KnowledgeRule.KnwRulesChecked=-1!==C.indexOf("on"),!(C.length>1))return c.specInfo=f,new o(c);f.KnowledgeRule.RuleSet={},n._getLabelAndIconAttr({ids:[C[1]],onComplete:function(e){if(d===a[p]){var t=JSON.parse(e).results;return f.KnowledgeRule.RuleSet.id=t[0].attributes[0].value,f.KnowledgeRule.RuleSet.alias=t[0].attributes[1].value,f.KnowledgeRule.RuleSet.icon=t[0].attributes[2].value,c.specInfo=f,new o(c)}}})}},onFailure:function(e){d===a[c]&&l(u,g.length>1&&s.getAttributes().label,"No stream retrieved"===e?r.privLevelSimulationError:r.isrInfoRetrievalFailed)}});else{if(d!==a[c])return;l(u,g.length>1&&s.getAttributes().label,r.isrInfoRetrievalFailed)}})}))})}.bind(this,a[c],c))};define("DS/PIMwebPropertiesTabs/PIMwebIsrPropContextTab",["UWA/Core","DS/EditPropWidget/facets/Common/FacetsBase"].concat([]),function(t,n){return e||(e=t),n.extend({init:function(t){this._parent(t),this.elements={},this.elements.container=e.createElement("div",{class:"infoIsrContextTab"}),this.elements.container.setStyles({overflow:"auto"}),this.updateFacet=c.bind(this,"ctxt"),this.destroyComponent=function(){},this.onResize=function(){},this.onRefresh=function(){this.updateFacet()}}})}),define("DS/PIMwebPropertiesTabs/PIMwebIsrPropSpecTab",["UWA/Core","DS/EditPropWidget/facets/Common/FacetsBase"].concat([]),function(t,n){return e||(e=t),n.extend({init:function(t){this._parent(t),this.elements={},this.elements.container=e.createElement("div",{class:"infoIsrSpecTab"}),this.elements.container.setStyles({overflow:"auto"}),this.updateFacet=c.bind(this,"spec"),this.destroyComponent=function(){},this.onResize=function(){},this.onRefresh=function(){this.updateFacet()}}})})}();