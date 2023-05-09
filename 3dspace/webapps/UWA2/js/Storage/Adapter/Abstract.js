define("UWA/Storage/Adapter/Abstract",["UWA/Core","UWA/Class","UWA/Class/Options","UWA/Storage"],function(t,s,e){"use strict";var r=s.extend(e,{type:"Abstract",limit:-1,init:function(t,s){this.storage=t||{},this.database=null,this.data={},this.includes=[],this.storage.isReady=!1,this.setOptions(s)},isAvailable:function(){return!1},interruptAccess:function(){if(!this.storage.isReady)throw new Error("Storage Adapter "+this.type+" is not ready.");return!0},get:function(t){return this.interruptAccess(),this.data[t]||null},set:function(t,s){return this.interruptAccess(),this.data[t]=s,s},rem:function(t){this.interruptAccess();var s=this.data[t];return this.data[t]=null,s}});return t.namespace("Storage/Adapter/Abstract",r,t)});