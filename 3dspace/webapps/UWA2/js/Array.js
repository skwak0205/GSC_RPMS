define("UWA/Array",["UWA/Core","UWA/Internal/Deprecate"],function(r,a){"use strict";var e={};function t(){return a.warn("UWA/Array as a constructor","Use the native Array instead."),Array.apply(null,arguments)}return e.equals=function(r,e){a.warn("UWA.Array.equals","Use UWA.equals instead");var t,n=r.length;if(!e||n!==e.length)return!1;for(t=0;t<n;t++)if(r[t]!==e[t])return!1;return!0},e.detect=function(r,a,e){var t,n;for(t=0,n=r.length;t<n;t++)if(a.call(e,r[t],t,r))return r[t]},e.invoke=function(r,a){var e=Array.prototype.slice.call(arguments,2);return r.map(function(r){return r[a].apply(r,e)})},e.erase=function(r,a){for(var e=r.length;e--;e)r[e]===a&&r.splice(e,1);return r},a.uncurryAlias(e,Array.prototype,"UWA.Array","Array"),t.isArray=a.alias("UWA/Array.isArray","Use the native Array.isArray instead",Array.isArray),["splice","slice"].forEach(function(r){t.prototype[r]=a.alias("UWA/Array#"+r,"Use the native Array#"+r+" instead",Array.prototype[r])}),Object.assign(t,e),r.namespace("Array",t,r)});