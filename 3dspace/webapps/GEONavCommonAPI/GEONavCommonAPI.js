define("DS/GEONavCommonAPI/GEONavCommonServices",["UWA/Core","DS/i3DXCompassPlatformServices/i3DXCompassPlatformServices","DS/WAFData/WAFData"],function(e,r,o){"use strict";return{_csrf:"",callService:function(e,o,t){var n=this,s=e.envId?e.envId:widget.getValue("x3dPlatformId");r.getServiceUrl({serviceName:e.serviceName,platformId:s,onComplete:function(r){var s;Array.isArray(r)?r.length&&(s=r[0].url):r.constructor===String&&(s=r),n.requestService({url:s+e.URI,requestOptions:e.requestOptions},o,t)},onFailure:function(r){console.log(r.message);var o={message:"The "+e.serviceName+" Service has not been found in the platform"};t(o)}})},callFCSService:function(e,r,t){var n={method:"GET",type:"text",onComplete:function(e){r(e)},onFailure:function(e){console.log(e.message);t({message:"GEONavCommonServices: error calling FCS service"})}},s=UWA.extend(UWA.clone(n),e.requestOptions);o.authenticatedRequest(e.actionurl,s)},requestService:function(e,r,t){var n=this,s={method:"GET",type:"json",onComplete:function(e,o,t){var s=n.getCSRFTokenFromHeader(o);s&&(n._csrf=s),r(e)},onFailure:function(e){console.log(e.message);t({message:"error calling web service"})}},a=UWA.extend(UWA.clone(s),e.requestOptions);a.headers||(a.headers={}),a.headers["X-DS-CSRFTOKEN"]=n._csrf||"",o.authenticatedRequest(e.url,a)},getCSRFTokenFromHeader:function(e){return void 0===e?"":e["X-DS-CSRFTOKEN"]||e["X-DS-CSRFTOKEN".toLowerCase()]}}});