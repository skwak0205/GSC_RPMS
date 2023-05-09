define("UWA/Class",["UWA/Core"],function(t){"use strict";var n=!1,i=/xyz/.test(function(){t.xyz()}),e=i?/\b_parent\b/:/^/,r=i?/\b_previous\b/:/^/,o=function(){},s=function(){};function a(t,n,i,s){i._original&&(i=i._original);var a,p=e.test(i),u=r.test(i);return p||u?("function"!=typeof n&&(n=s?o:t||o),"function"!=typeof t&&(t=u?o:n),(a=function(){var e,r,o;return null!=this?(p&&(r=this._parent,this._parent=t),u&&(o=this._previous,this._previous=n),e=i.apply(this,arguments),p&&(this._parent=r),u&&(this._previous=o)):e=i.apply(this,arguments),e}).displayName="(method wrapper)",a._original=i,a):i}function p(t){return function(){this._previous.apply(this,arguments),t.apply(this,arguments)}}return s.extend=function(){var i,e;return n=!0,i=new this,n=!1,e=function(){!n&&this.__uwaPrivateConstructor&&this.__uwaPrivateConstructor.apply(this,arguments)},i.constructor=e,t.extend(e,{prototype:i,parent:this,extend:s.extend,singleton:s.singleton,implement:s.implement}),e.implement.apply(e,arguments),e},s.implement=function(){var n,i,e,r,o,s,u,l=this.prototype||this;for(n=0,i=arguments.length;n<i;n+=1){var c;for(r in e=arguments[n],"class"===(o=t.typeOf(e))||"function"===o?("function"===o&&(e.prototype.__uwaPrivateConstructor=p(e)),e=e.prototype):e.init&&(e.__uwaPrivateConstructor=e.init,delete e.init),c=e.name?e.name:l.name?t.owns(l,"name")?l.name:"("+l.name+" inheritor)":"(anonymous class)",e)"constructor"!==r&&(s=e[r],t.is(s,"function")?(u=("class"===o?arguments[n]:this).parent,s.displayName=c+"#"+("__uwaPrivateConstructor"===r?"init":r),l[r]=a(u&&u.prototype&&u.prototype[r],t.owns(l,r)&&l[r],s,"class"===o)):t.is(s,"plain")?(t.owns(l,r)||(l[r]=t.is(l[r],"plain")?t.clone(l[r]):{}),t.extend(l[r],s,!0)):l[r]=s)}return this},s.singleton=function(){var t,i=this.extend.apply(this,arguments),e=i.prototype,r=!1,o=[];function s(n){t[n]=function(){if(!r){if("throw"===t.uninitializedCalls)throw new Error("Singleton called before being initialized");if("ignore"===t.uninitializedCalls)return;t.init()}return t[n].apply(this,arguments)}}for(var a in n=!0,t=new i,n=!1,e)"function"==typeof e[a]&&"init"!==a&&"constructor"!==a&&(o.push(a),s(a));return t.init=function(){if(r)throw new Error("Singleton is already initialized");var n,e;for(r=!0,n=0,e=o.length;n<e;n++)delete t[o[n]];return i.apply(this,arguments),this},t},t.namespace("Class",s,t)});