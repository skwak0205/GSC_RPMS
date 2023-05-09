define("DS/OnePart3DSearch/facets/Properties",["DS/W3DXComponents/Skeleton","i18n!DS/OnePart3DSearch/assets/nls/onepart"],function(e,t){"use strict";return{facet_name:"onepart-properties",is_facet_avalailable:function(){return!1},facet_idcard:function(){return{text:t.facet.properties.title,icon:"vcard",handler:e.getRendererHandler("DS/OnePart3DSearch/views/PropertiesView")}}}}),define("DS/OnePart3DSearch/core/Services",["UWA/Class/Promise","DS/i3DXCompassPlatformServices/i3DXCompassPlatformServices","DS/WAFData/WAFData"],function(e,t,n){"use strict";function r(t){return new e(function(e,r){n.authenticatedRequest(t+"/3dxp-api/hello",{onComplete:e,onFailure:r})})}var i={host:null,ready:!1,init:function(){var n=this;return new e(function(e,n){t.getServiceUrl({serviceName:"OnePart",platformId:"OnPremise",onComplete:function(t){return t?e(t):(UWA.log("Got invalid OnePart backend url from Compass, check backend configuration."),n())},onFailure:function(e){return UWA.log("Failed to retrieve backend url from Compass: "+e.error),n()}})}).then(i.setHost.bind(this)).then(r).then(function(){n.ready=!0}).catch(function(){n.ready=!1})},setHost:function(e){return this.host=e,this.host},getHost:function(){return this.host},isReady:function(){return this.ready}};return i}),define("DS/OnePart3DSearch/views/PropertiesView",["DS/W3DXComponents/Views/Temp/TempItemView","DS/W3DXComponents/Views/Layout/ScrollView"],function(e,t){"use strict";var n=["preview_url","actions","type_icon_url","icon","icon_source"];return e.extend({name:"onepart-properties",tagName:"div",template:function(){return'<div class="'+this.getClassNames("-sub-container").trim()+' container"><table class="table table-striped"><colgroup><col class="col-xs-3"/><col class="col-xs-5"/></colgroup><thead></thead><tbody>{{#each filteredProperties}}<tr><td>{{[0]}}</td><td>{{[1]}}</td>{{/each}}</tbody></table></div>'},setup:function(){return(this.templateHelpers||(this.templateHelpers={})).filteredProperties=this.model.pairs().filter(function(e){return e[0]&&-1===e[0].indexOf("_value")&&-1===n.indexOf(e[0])}),this._parent.apply(this,arguments)},onRender:function(){var e=this.getElement("."+this.name+"-sub-container");e.render=function(){},e.container=e,new t({view:e,useInfiniteScroll:!1,usePullToRefresh:!1}).render().inject(this.container)}})}),define("DS/OnePart3DSearch/types/Drawing",["UWA/Controls/Abstract","DS/OnePart3DSearch/facets/Properties"],function(e,t){"use strict";return e.extend({names:["drawing"],init:function(){},getAvailableFacetsIdAndOrder:function(){return[t.facet_idcard()]},image:function(){return(this.model||this).get("preview_url")},icon:function(){return(this.model||this).get("type_icon_url")}})}),define("DS/OnePart3DSearch/actions/Component",["UWA/Controls/Abstract","DS/OnePart3DSearch/core/Services","i18n!DS/OnePart3DSearch/assets/nls/onepart"],function(e,t,n){"use strict";return e.extend({init:function(){},availableForTypes:function(){return["dgn","stp","step","dcm","igs","iges","inp","sldprt","sldasm","slddrw","dwg","dxf","dxb","dxx","dwf","dws","dwt","set","shp","shx","slb","sld","ipt","iam","catanalysis","catdrawing","catfct","catknowledge","catmaterial","catpart","catprocess","catproduct","catraster","catresource","catsettings","catshape","catswl","catsystem","cgr","model","v6part","v6product","prt","asm","drw","par","psm","solidworks","sldblk","sldbombt","sldclr","slddrt","slddwg","sldgtolfvt","sldholtbt","sldlfp","sldmat","sldreg","sldrevtbt","sldsffvt","sldstd","sldtbt","sldweldfvt","sldwldtbt","3dxml"]},getActionForObject:function(){return[{title:n.action.openInOnePart.title,icon:"eye",id:"OpenInOnePart",multiSel:!0,callback:this.openInWidgetApp.bind(this)}]},openInWidgetApp:function(e,n){var r={appName:t.isReady()?"EXA_S1X_AP":null,appData:{selection:function(e){return(e=Array.isArray(e)?e:[e]).map(function(e){return e.model?{id:e.model.get("resourceid"),url:e.model.get("ds6w:where/ds6w:source")}:null}).filter(function(e){return e})}(e)}};r.appName&&require(["DS/TransientWidget/TransientWidget"],function(e){!function(e){var t=e?e.getContent():null,n=t?t.getDocument():null,r=n?n.getElementsByClassName("tooltip"):[];for(var i in r)r.hasOwnProperty(i)&&r[i].remove()}(n[1]),e.showWidget(r.appName,"",r.appData)},function(){UWA.log("Unable to load transient widget.")})}})}),define("DS/OnePart3DSearch/types/Component",["UWA/Controls/Abstract","DS/OnePart3DSearch/core/Services","DS/OnePart3DSearch/facets/Properties"],function(e,t,n){"use strict";return e.extend({names:[],init:function(){t.init()},getAvailableFacetsIdAndOrder:function(){return[n.facet_idcard()]},image:function(){return(this.model||this).get("preview_url")},icon:function(){return(this.model||this).get("type_icon_url")}})});