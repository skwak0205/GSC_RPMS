define("DS/WidgetLink/WidgetLinkData",[],function(){"use strict";if(window!==top&&top.require)return top.require("DS/WidgetLink/WidgetLinkData");return function t(i,e){if(this.constructor===t)throw new Error("Cannot instanciate abstract class "+t.name);var n;Object.defineProperty(this,"id",{value:i,enumerable:!0}),n=function(t,i){Object.defineProperty(this,t,{get:function(){return i(e())},enumerable:!0})}.bind(this),["appId","x3dSharedId","x3dAppId","x3dPlatformId"].forEach(function(t){n(t,function(i){return i?i.getValue(t):void 0})}),n("alive",function(t){return!!t})}}),define("DS/WidgetLink/ExternalWidgetLink",["DS/WidgetLink/WidgetLinkData","DS/MessageBus/MessageBus"],function(t,i){"use strict";if(window!==top&&top.require)return top.require("DS/WidgetLink/ExternalWidgetLink");var e=new WeakMap,n={};function r(t){return require("DS/Dashboard/Utils").getRegisteredWidgetInstance(t)}function o(t,i){var e=r(i),n=r(t.id);return e&&n&&e.environment.wp.parentId===n.environment.wp.parentId?e:void 0}function s(i,e,r){if(r!==n)throw new Error("Cannot use "+s.name+"#constructor directly");t.call(this,e,function(){return o(i,e)}),this._widgetLink=i}return s.prototype=Object.create(t.prototype),s.prototype.constructor=s,s.get=function(t,i){var r,o,u,a=function(t,i){var n=e.get(t);return n&&n.get(i)}(t,i);return a||(r=new s(t,i,n),o=r,(u=e.get(o._widgetLink)||new Map).set(o.id,o),e.set(o._widgetLink,u),r)},s.prototype.isLinked=function(){return this.alive&&!!this._widgetLink.getLinked().find(function(t){return t.id===this.id},this)},s.prototype.unlink=function t(e){if(!e||"string"!=typeof e)throw new Error(s.name+"#"+t.name+" requires a reason as its parameter");this.isLinked()&&i.publish({channel:"widget-link",topic:"force-unlink",data:{source:this._widgetLink.id,target:this.id,reason:e}})},s.prototype.matchLinkability=function(){return this.isLinked()?require("DS/Dashboard/WidgetLinkManager").matchLinkability(o(this._widgetLink,this.id).data,Object.assign({},this._widgetLink,{x3dLinkability:this._widgetLink.getLinkability()})):null},s}),define("DS/WidgetLink/AbstractWidgetLink",["DS/MessageBus/MessageBus","DS/WidgetLink/WidgetLinkData","DS/WidgetLink/ExternalWidgetLink"],function(t,i,e){"use strict";if(window!==top&&top.require)return top.require("DS/WidgetLink/AbstractWidgetLink");var n=new Map,r="widget-link";function o(t){return n.get(t)||new Set}function s(t,i){var n,r=i.indexOf(t.id);if(-1!==r)return n=1===r?i[0]:i[1],e.get(function(t){return require("DS/WidgetLink/WidgetLink").get(t._widget)}(t),n)}function u(t,i){return function(e){var n=s(t,e.ids),r=Object.freeze({type:i,target:t,link:n});n&&t._listeners[i].forEach(function(t){try{t(r)}catch(t){setTimeout(function(){throw t})}})}}function a(i,e,n){i._defaultSubscriptions[e]=t.subscribe({channel:r,topic:e,callback:n})}function c(e){if(this.constructor===c)throw new Error("Cannot instanciate abstract class "+c.name);var s;i.call(this,e.id,function(){return e}),this._widget=e,this._channelId=[r,(s=this).id,o(s.id).size].join(":"),this._listeners={link:[],unlink:[]},this._defaultSubscriptions={},a(this,"link",u(this,"link")),a(this,"unlink",u(this,"unlink")),a(this,"destroy",function(i){i.ids.includes(this.id)&&(n.delete(this.id),this.unsubscribe(),Object.values(this._defaultSubscriptions).forEach(function(i){t.unsubscribe(i)}))}.bind(this)),function(t){var i=o(t.id);i.add(t),n.set(t.id,i)}(this)}return c.prototype=Object.create(i.prototype),c.prototype.constructor=c.prototype,c.prototype.getLinked=function t(){throw new Error("Cannot invoke abstract function "+c.name+"#"+t.name)},c.prototype.createView=function(t){return new(require("DS/WidgetLink/WidgetLinkView"))(this,t)},c.prototype.addEventListener=function(t,i){Object.keys(this._listeners).includes(t)&&this._listeners[t].push(i)},c.prototype.removeEventListener=function(t,i){if(Object.keys(this._listeners).includes(t)){var e=this._listeners[t].indexOf(i);-1!==e&&this._listeners[t].splice(e,1)}},c.prototype.publish=function(i,e){var n=this.id;this.getLinked().forEach(function(r){Array.from(o(r.id)).filter(function(t){return!!t.getLinked().find(function(t){return t.id===n})}).forEach(function(n){t.publish({channel:n._channelId,topic:i,data:e})})})},c.prototype.subscribe=function(i,e){return t.subscribe({channel:this._channelId,topic:i,callback:e})},c.prototype.unsubscribe=function(i){"string"==typeof i?t.unsubscribe({channel:this._channelId,topic:i}):i?t.unsubscribe(i):t.unsubscribe({channel:this._channelId})},c}),define("DS/WidgetLink/WidgetLink",["DS/WidgetLink/AbstractWidgetLink","DS/WidgetLink/ExternalWidgetLink"],function(t,i){"use strict";if(window!==top&&top.require)return top.require("DS/WidgetLink/WidgetLink");var e=new WeakMap,n={};function r(t){return require("DS/Dashboard/WidgetLinkManager").fixLinkability(t)}function o(i,e){if(e!==n)throw new Error("Cannot use "+o.name+"#constructor directly");t.call(this,i)}return o.prototype=Object.create(t.prototype),o.prototype.constructor=o,o.get=function(t){if(e.has(t))return e.get(t);var i=new o(t,n);return e.set(t,i),i},o.prototype.getLinked=function(){return require("DS/Dashboard/WidgetLinkManager").getLinked(this.id).map(function(t){return i.get(this,t)},this).filter(function(t){return t.alive})},o.prototype.getLinkability=function(){return r(this._widget.getValue("x3dLinkability"))},o.prototype.setLinkability=function(t){this._widget.setValue("x3dLinkability",t?r(t):void 0)},o}),define("DS/WidgetLink/WidgetLinkView",["DS/WidgetLink/AbstractWidgetLink"],function(t){"use strict";if(window!==top&&top.require)return top.require("DS/WidgetLink/WidgetLinkView");function i(i,e){t.call(this,i._widget),this._underlying=i,this._predicate=e}return i.prototype=Object.create(t.prototype),i.prototype.constructor=i,i.prototype.getLinked=function(){return this._underlying.getLinked().filter(this._predicate)},i});