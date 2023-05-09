define("UWA/Controls/Accordion",["UWA/Core","UWA/Controls/Segmented"],function(t,e){"use strict";var n=e.extend({name:"uwa-accordion",defaultOptions:{title:null,multiSelect:!1,constantItemWidth:!1,contentPosition:"bottom"},init:function(t){this._parent(t)},hasItem:function(t){return!!this.elements.items[t]},addItem:function(e,n){var o=this.elements,i=o.items=o.items||{},s=o.containers=o.containers||{};this._parent(e,n),s[e]=t.createElement("div",{class:this.getClassNames("-content")}).inject(i[e],"top"===this.options.contentPosition?"top":"bottom"),n.content&&this.setContent(e,n.content)},removeItem:function(t){var e=this.elements.items;e[t]&&(e[t].destroy(),e[t]=null)},updateContent:function(e,n,o){var i=this.elements.containers[e];return o&&i.empty(),t.is(n,"string")&&(n=t.createElement("div",{html:n})),i.addContent(n),i},buildSkeleton:function(){var e=this.elements;e.container=t.createElement("ul",{class:this.getClassNames()}),e.itemContainer=e.container},onClick:function(t){this.toggleItem(t)},onChange:function(t,e){e&&!this.options.multiSelect&&this.unselectItems(!1,t)},setContent:function(t,e){return this.updateContent(t,e,!0)},addContent:function(t,e){return this.updateContent(t,e,!1)}});return t.namespace("Controls/Accordion",n,t)});