define("DS/i3DXCompassServices/i3DXCompassServices",[],function(){"use strict";var e="DS/i3DXCompassPlatformServices/i3DXCompassPlatformServices",n=function(){var e={};return function(n,i){if(e[n])e[n]&&i(e[n]);else if(function(){var e;try{top.document,e=!0}catch(n){e=!1}return e}())try{top.require([n],function(t){e[n]=t,i(t)})}catch(t){try{require([n],function(t){e[n]=t,i(t)})}catch(e){i(null)}}else try{require([n],function(t){e[n]=t,i(t)})}catch(e){i(null)}}}();return{getPlatformServices:function(i){n(e,function(e){e.getPlatformServices({public:!0,platformId:i.platformId,onComplete:function(e){e?(i.platformId,i.onComplete(e)):i.onComplete()},onFailure:i.onFailure})})},getServiceUrl:function(i){n(e,function(e){e.getServiceUrl({public:!0,platformId:i.platformId,serviceName:i.serviceName,onComplete:function(e){e?(i.platformId,i.onComplete(e)):i.onComplete()},onFailure:i.onFailure})})}}}),define("DS/i3DXCompassServices/i3DXCompassPubSub",["UWA/Utils","UWA/Core","UWA/Class/Model","UWA/Class/Collection"],function(e,n,i,t){"use strict";var o="com.ds.compass",s=function(){return"undefined"!=typeof dscef},c=function(){var e={};return function(n,i){if(!e[n]&&function(){var e;try{top.document,e=!0}catch(n){e=!1}return e}())try{top.require([n],function(t){e[n]=t,i(t)})}catch(e){}else e[n]&&i(e[n])}}();return{subscribe:function(e,n){c("DS/MessageBus/MessageBus",function(i){i&&i.subscribe({channel:o,topic:e,callback:n})})},publish:function(r,u,a){var l=this;c("DS/MessageBus/MessageBus",function(c){var f=e.getUUID(),p={},b=n.clone(r);if(u){for(var m in u)u.hasOwnProperty(m)&&u[m]&&(u[m]instanceof i||u[m]instanceof t)&&u[m].toJSON&&(u[m]=u[m].toJSON());p=u?n.clone(u):{}}s()&&setTimeout(function(){dscef.sendString(b,JSON.stringify(p))},500),c&&(u||(u={}),a&&(l.subscribe(r+"Callback",function(e){e._PUB_SUB_ID===f&&(delete e._PUB_SUB_ID,l.unsubscribe(r+"Callback"),a.apply(l,arguments))}),-1===r.indexOf("Callback")&&(u._PUB_SUB_ID=f)),c.publish({channel:o,topic:r,data:u}))})},unsubscribe:function(e){c("DS/MessageBus/MessageBus",function(n){n&&n.unsubscribe({channel:o,topic:e})})},_subscribe:function(){throw new Error("use the subscribe method instead of _subscribe")},_publish:function(){throw new Error("use the publish method instead of _publish")},_initCefSubscriberIfNeeded:function(){s()&&require(["DS/PlatformAPI/PlatformAPI"],function(e){e.subscribe("webinwin:win:notification",function(e){if(console.log("Received a webinwin:win:notification"),console.log(e),e="string"==typeof e?JSON.parse(e):e,console.log(e),e&&e.topic){var n={channel:o,topic:e.topic,data:e.data};console.log(n),c("DS/MessageBus/MessageBus",function(e){e&&e.publish(n)})}})})}}});