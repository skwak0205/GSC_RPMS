define("DS/CfgDictionary/CfgDictionaryMapperUtils",["DS/CfgDictionary/CfgDictionaryTypeEnum"],function(e){"use strict";var t={},r=function(t,r,i){"reference"===i.kind?t[i.rel_lid]={type:e.OPTION,parent:r,name:i.attributes._title,description:i.attributes._description,image:i.attributes._image,seqNumber:i.attributes._sequenceNumber,kind:"reference"}:i.kind},i=function(t,r,i,a,n){var l=0;if("reference"===i.kind)for(t[i.lid]={type:e.VARIANT,parent:r,name:i.attributes._title,image:i.attributes._image,description:i.attributes._description,mandatory:a,seqNumber:n,values:[],kind:"reference"},l=0;l<i.values.member.length;l+=1)t[i.lid].values.push(i.values.member[l].rel_lid),t[i.values.member[l].rel_lid]={type:e.VALUE,parent:i.lid,name:i.values.member[l].attributes._title,description:i.values.member[l].attributes._description,image:i.values.member[l].attributes._image,kind:"reference",seqNumber:i.values.member[l].attributes._sequenceNumber},t[i.values.member[l].id]={type:e.VALUE,parent:i.lid,name:i.values.member[l].attributes._title,image:i.values.member[l].attributes._image,description:i.values.member[l].attributes._description,kind:"reference",seqNumber:i.values.member[l].attributes._sequenceNumber}},a=function(t,n,l){var s=0;t[l.lid]={parent:n,name:l.attributes._title,image:l.attributes._image,type:e.VARIABILITY_GROUP,seqNumber:l.attributes._sequenceNumber,content:[]};var m=l.variants?l.variants.member:[];for(s=0;s<m.length;s+=1)i(t,l.lid,m[s]),t[l.lid].content.push(m[s].lid);var u=l.options?l.options.member:[];for(s=0;s<u.length;s+=1)r(t,l.lid,u[s]),u[s].rel_lid&&t[l.lid].content.push(u[s].rel_lid);var o=l.variabilityGroups?l.variabilityGroups.member:[];for(s=0;s<o.length;s+=1)a(t,l.lid,o[s]),t[l.lid].content.push(o[s].lid)};return t.dicoMinifiedV3ToMap=function(e){var t={},n=void 0,l=void 0;if(null!=e&&null!=e){if(e.member)return this.formatDicoFromGetRuleCriteria(e);if("3.0"!==e._version)return null;var s,m,u=0,o=e.portfolioClasses?e.portfolioClasses.member[0].portfolioComponents.member[0]:e,b=o.options?o.options.member:[],p=o.variants?o.variants.member:[],c=o.variabilityGroups?o.variabilityGroups.member:[],d=o.rules&&o.rules.member?o.rules.member:[];for(u=0;u<b.length;u+=1)r(t,null,b[u]);for(u=0;u<p.length;u+=1)"instance"===p[u].kind&&p[u].attributes&&(n=p[u].attributes._mandatory,l=p[u].attributes._sequenceNumber),i(t,null,p[u],n,l);for(u=0;u<c.length;u+=1)a(t,null,c[u]);for(u=0;u<d.length;u+=1)s=t,m=d[u],s[m.id]={name:m.attributes._title,type:m.attributes._type};return t}console.log("CfgDictionaryMapperUtils.dicoMinifiedV3ToMap : The dictionary wasn't define")},t.formatDicoFromGetRuleCriteria=function(e){var t={};if(e.member){for(var r=0;r<e.member.length;r++){var i=e.member[r];switch(i.type){case"Variant":t[i.busLogicalId]={name:i.title,type:i.type,parent:i.parent,image:i.image,values:[]};break;case"Variant Value":t[i.relLogicalId]={name:i.title,type:i.type,parent:i.parent,image:i.image},t[i.parent]?t[i.parent].values.push(i.relLogicalId):t[i.parent]={values:[i.relLogicalId]};break;case"Option Group":case"Option":t[i.relLogicalId]={name:i.title,type:i.type,parent:i.parent,image:i.image};break;default:console.error("Issue CfgDictionaryMapperUtils.formatDicoFromGetRuleCriteria : Variability no type")}}return t}return null},t});