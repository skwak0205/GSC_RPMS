define("DS/M3DEEast/script/cockpit/cockpit-view",[["DS","M3DEUICore","script","lib","view","generic","swym-model-view"].join("/"),["DS","M3DEUICore","script","feature","core","context","context-model"].join("/")],function(e,t){"use strict";return e.extend({name:"cockpit-view",setup:function(){this._context=t.getInstance()},_createContent:function(){require(["DS/Dashboard/Views/My3DExperience/Cockpit"],function(e){new e({container:this.container})}.bind(this))}})}),define("DS/M3DEEast/script/cockpit/tabs-view",[["DS","M3DEUICore","script","lib","view","generic","swym-model-view"].join("/"),["DS","M3DEUICore","script","feature","core","context","context-model"].join("/")],function(e,t){"use strict";return e.extend({name:"cockpit-view",setup:function(){this._context=t.getInstance()},_createContent:function(){require(["DS/Dashboard/Views/My3DExperience/TabList"],function(e){new e({container:this.container})}.bind(this))}})});