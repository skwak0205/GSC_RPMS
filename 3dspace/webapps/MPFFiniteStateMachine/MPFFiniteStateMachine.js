define("DS/MPFFiniteStateMachine/FiniteStateMachine",["UWA/Core","UWA/Class"],function(t,i){"use strict";return i.extend({init:function(i){if(!t.is(i.states,"array"))throw new Error("states is required and must be an array");if(!t.is(i.state))throw new Error("state is required");if(!t.is(i.transitions,"array"))throw new Error("transitions must be an array");if(this.states=i.states.slice(),this.state=i.state,!this._checkTransitionDefinition(i.transitions))throw new Error("transitions definitions is not valid");this.transitions=this._parseTransitionDefinitions(i.transitions)},getState:function(){return this.state},changeState:function(t){let i=!1;return this._checkTransition(this.state,t)&&(this.state=t,i=!0),i},_hasState:function(t){return this.states.indexOf(t)>=0},_checkTransition:function(i,n){let s=!1;if(this._hasState(i)&&this._hasState(n)){const r=this.transitions[i];s=t.is(r,"array")&&r.indexOf(n)>=0}return s},_checkTransitionDefinition:function(i){const n=this;let s=!1;return t.is(i,"array")&&(s=i.reduce(function(t,i){return t&&n._hasState(i.from)&&n._hasState(i.to)},!0)),s},_parseTransitionDefinitions:function(i){const n={};return i.forEach(function(i){t.is(n[i.from],"array")?n[i.from].push(i.to):n[i.from]=[i.to]}),n}})});