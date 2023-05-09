define("DS/MPFConnector/ConnectorBackend",[],function(){"use strict";return Object.freeze({MARKETPLACE:"marketplace",SWYM:"3DSwym"})}),define("DS/MPFConnector/Connector",["UWA/Core","UWA/Class"],function(e,r){"use strict";return r.extend({url:function(e){},request:function(e,r){}})}),define("DS/MPFConnector/MarketplaceConnector",["UWA/Core","UWA/Utils","UWA/Class","UWA/Promise","DS/MPFConnector/Connector","DS/MPFUtils/MPFUtils","DS/WAFData/WAFData","DS/i3DXCompassPlatformServices/i3DXCompassPlatformServices","DS/PlatformAPI/PlatformAPI","DS/MPFUrl/UrlUtils","DS/MPFServices/MarketplaceServices","DS/MPFConnector/ConnectorBackend"],(e,r,t,o,n,s,i,a,c,l,p,f)=>{"use strict";const u={},m=n.extend({init(e,r,t){if("string"!=typeof e)throw new Error("Marketplace base URL must be a string");if(t||(t={}),e.length>0){const r=e.slice(-1);this.rootUrl="/"===r?e.slice(0,-1):e}this.platformId=r,this.tenant=this.platformId,this.service=t.service||p.MAKE,this.app=t.app},url(e,t={}){const o=e.split("#"),n=o[0].split("?");let s;s=n.length>1?n[1].split("&"):[],this.service&&s.push("service="+this.service),this.platformId&&s.push("tenant="+this.platformId);const i=t.esid||r.getQueryString(window.location.href,"esid");i&&!1!==t.addESID&&s.push("project="+i);const a=s.join("&");return a&&(n[1]=a),o[0]=n.join("?"),e=o.join("#"),this.rootUrl+e},request(t,o){const n=o.method||"GET",s=e.merge({method:n,type:"json",timeout:18e4,retry:1},o);if("POST"===n||"PATCH"===n||"PUT"===n){o.data&&Object.prototype.isPrototypeOf.call(FormData.prototype,o.data)||(s.headers||(s.headers={}),s.headers["Content-Type"]="application/json")}e.is(r.Client)&&r.Client.Engine.ie&&"GET"===n&&(t=new l(t).addParameter("iecache",Date.now()).getUrl()),s.onFailure=function(){return s.retry-=1,s.retry>=0?i.authenticatedRequest(t,s):o.onFailure(...arguments)};const a=t.contains("service=clicknbuy");return a&&"POST"===n&&t.contains("/me/carts")?s.onPassportError=((e,r)=>{console.error(e);const t=window.location.pathname.contains("/cart")?"/welcome/compass-world/3dexperience-collaborative-innovation/collaborative-business-innovator/buy/order":window.location.pathname,o=new l(window.location.origin+t+window.location.hash).addParameter("goToPayment",!0).getUrl();window.location.href=r.replace("/logout","")+"/login?service="+encodeURIComponent(o)}):a&&"GET"===n&&t.contains("/mdcart/carts/")&&new l(window.location.href).hasParameter("cartId")?s.onPassportError=((e,r)=>{console.error(e),window.location.href=r.replace("/logout","")+"/login?service="+encodeURIComponent(window.location.href)}):t.contains("/me/cartsV2")&&(s.onPassportError=((e,r)=>{console.error(e)})),i.authenticatedRequest(t,s)}});return m.fetchPromise=function(e){e=e||{},e=m._sanitizeOptions(e);const r=s.objectHashCode(e),t=o.deferred();if(u[r])t.resolve(u[r]);else{const o=e.platformId||c.getApplicationConfiguration("app.mp.tenant")||window.$nuxt&&window.$nuxt.$env.PLATFORM_ID;o||console.warn("MarketplaceConnector - no platformId provided or found in configuration"),a.getServiceUrl({platformId:o,serviceName:e.backend,onComplete:function(n){if(n){const s=m._extractServiceDetails(n);let i;s.url?(s.platformId||(s.platformId=o),e.backend===f.MARKETPLACE&&(s.url+="/resources"),i=new m(s.url,s.platformId,e),u[r]=i,t.resolve(i)):m._fetchMyAppsUrl().then(m._fetchMarketplaceUrlFromMyApps).then(function(o){o.url?(i=new m(o.url+"/resources",o.platformId,e),u[r]=i,t.resolve(i)):t.reject(new Error("Unable to finc marketplace url from MyApps"))}).catch()}else t.reject(new Error("Unable to find marketplace service url for platformId "+o))},onFailure:function(){t.reject("Unable to get platform services from i3DXCompassPlatformServices")}})}return t.promise},m._fetchMyAppsUrl=function(){return new o(function(e,r){a.getServiceUrl({serviceName:"3DCompass",onComplete:function(t){Array.isArray(t)?e(m._extractFirstServiceWithAnUrlFromArray(t)):r(new Error("Unable to get MyApps url from i3DXCompassPlatformServices"))},onFailure:function(){r(new Error("Unable to get MyApps url from i3DXCompassPlatformServices"))}})})},m._fetchMarketplaceUrlFromMyApps=function(e){return new o(function(r,t){const o=e.url+"/resources/AppsMngt/api/v1/public/services";i.request(o,{type:"json",onComplete:function(e){const t=e&&e.platforms;if(t){const e=s.findInArray(t,e=>s.findInArray(e.services,e=>"marketplace"===e.id));if(e){const t=s.findInArray(e.services,function(e){return"marketplace"===e.id});r({platformId:e.id,url:t.url})}}},onFailure:function(){t(new Error("Unable to get marketplace url from MyApps"))}})})},m._sanitizeOptions=function(e){const r={};return(e=e||{}).service&&(r.service=e.service),e.platformId&&(r.platformId=e.platformId),r.backend=e.backend||"marketplace",r},m._extractServiceDetails=function(e){const r="string"==typeof e,t=Array.isArray(e);let o={};return r?o.url=e:t?o=m._extractFirstServiceWithAnUrlFromArray(e):o.url="",o},m._extractFirstServiceWithAnUrlFromArray=function(e){const r=s.findInArray(e,function(e){return e.url}),t={};return r&&(t.platformId=r.platformId,t.url=r.url),t},m.get3DPassportUrl=function(){const e=o.deferred();return a.getPlatformServices({onComplete:function(r){const t=r.filter(function(e){return!!e["3DPassport"]}).pop();if(t){const r=t["3DPassport"];e.resolve(r)}else e.reject("Unable to find a 3DPassport in platform services from i3DXCompassPlatformServices")},onFailure:function(){e.reject("Unable to get platform services from i3DXCompassPlatformServices")}}),e.promise},m}),define("DS/MPFConnector/SheerIdConnector",["DS/MPFConnector/Connector","DS/WAFData/WAFData"],function(e,r){"use strict";return e.extend({init(){this.rootUrl="https://services.sheerid.com/rest/v2"},request(){return this.fetch(...arguments)},async fetch(e,{method:r="GET",isJson:t=!0,contentType:o,data:n}={}){const s=this._createUrl(e),i={method:r};return t?(i.headers={"Content-Type":"application/json"},i.body=JSON.stringify(n)):o?(i.headers={"Content-Type":o},i.body=n):i.body=n,window.fetch(s,i).then(e=>e.json())},async fetchProxified(e,{method:t="GET",type:o="json",headers:n,data:s}={}){const i=this._createUrl(e);return new Promise((e,a)=>{r.proxifiedRequest(i,{method:t,type:o,headers:n,data:s,onComplete(r){e(JSON.parse(r))},onFailure(e){console.error(e),a(e)},onTimeout(e){console.error("Request timed out"),console.error(e),a(e)}})})},_createUrl(e){return new RegExp("^(?:[a-z]+:)?//","i").test(e)?e:this.rootUrl+e}})}),define("DS/MPFConnector/AppsConnector",["DS/MPFConnector/Connector","DS/MPFUrl/UrlUtils","DS/WAFData/WAFData","UWA/Promise","UWA/Core","DS/i3DXCompassPlatformServices/i3DXCompassPlatformServices"],(e,r,t,o,n,s)=>{"use strict";const i=e.extend({init(e,r={}){this.rootUrl=e,n.is(r.platformId)&&(this.platformId=r.platformId)},url(e){return new r(this.rootUrl+e).getUrl()},request(e,r){const o={method:r.method||"GET",type:"json",timeout:18e4,onComplete:r.onComplete,onFailure:r.onFailure,data:r.data};return t.authenticatedRequest(e,o)}});return i.fetchPromise=function(){var e;return e=o.deferred(),s.getPlatformServices({onComplete:function(r){var t,o,n;(t=r.filter(function(e){return!!e["3DCompass"]}).shift())?(o=t["3DCompass"]+"/resources/AppsMngt",n=new i(o),e.resolve(n)):e.reject("Unable to find a compass in platform services from i3DXCompassPlatformServices")},onFailure:function(){e.reject("Unable to get platform services from i3DXCompassPlatformServices")}}),e.promise},i.fetchPromiseSynchro=function(){var e;return e=o.deferred(),s.getPlatformServices({onComplete:function(r){var t,o,n;(t=r.filter(function(e){return!!e["3DCompass"]}).shift())?(o=t["3DCompass"]+"/resources/AppsMngtSynchro/api/v1",n=new i(o,{platformId:t.platformId}),e.resolve(n)):e.reject("Unable to find a compass in platform services from i3DXCompassPlatformServices")},onFailure:function(){e.reject("Unable to get platform services from i3DXCompassPlatformServices")}}),e.promise},i.getUser=function(e){return e||(e={}),s.getUser(e)},i}),define("DS/MPFConnector/NoConnector",["DS/MPFConnector/Connector","DS/MPFUtils/MPFUtils"],function(e,r){"use strict";let t;const o=e.extend({url:r.doNothing,request:r.doNothing});return o.getInstance=function(){return t||(t=new o),t},Object.freeze(o),o}),define("DS/MPFConnector/EchoConnector",["DS/MPFConnector/Connector","DS/MPFUtils/MPFUtils"],function(e,r){"use strict";let t;const o=e.extend({url:function(e){return e},request:r.doNothing});return o.getInstance=function(){return t||(t=new o),t},Object.freeze(o),o});