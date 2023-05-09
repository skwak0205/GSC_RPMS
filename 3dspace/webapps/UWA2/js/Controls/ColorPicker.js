define("UWA/Controls/ColorPicker",["UWA/Core","UWA/Event","UWA/Color","UWA/Controls/Picker","UWA/Controls/Drag","UWA/Controls/Input","UWA/String"],function(e,t,s,n,i,l,a){"use strict";var o=i.extend({defaultOptions:{touchSnap:0,mouseSnap:0},start:function(e){this.move(e,{distance:0,x:0,y:0})},move:function(t,s){var n=this.element.getOffsets(),i=this.element.getSize(),l={x:t.x-n.x,y:t.y-n.y};l.x=Math.max(0,Math.min(i.width,l.x)),l.y=Math.max(0,Math.min(i.height,l.y));var a={x:l.x/i.width,y:l.y/i.height};this.options.pickCallback(a,l),this.options.hideCursorWhileDragging&&s.distance>0&&!this.injectedStylesheet&&(this.injectedStylesheet=e.createElement("style",{html:"* { cursor: none !important; }"}).inject(document.body))},reset:function(){this.injectedStylesheet&&(this.injectedStylesheet.remove(),this.injectedStylesheet=null),this._parent()}}),r=n.extend({name:"uwa-colorpicker",currentView:null,previouslyDisplayedHue:null,options:{button:{tag:"div",class:"uwa-colorpicker-well"},dropdownOptions:{},defaultValue:null,defaultView:"picker"},panton:["#9E080E","#637235","#942157","#50299A","#1277DB","#101010","#E51A60","#159A2B","#C41F88","#A159CE","#188D92","#444444","#FC7E29","#85BB23","#E868AE","#7970D0","#37C2DB","#888888","#FEB82C","#C6E12F","#E6A6AB","#A98EE5","#7EBBF5","#AAAAAA","#FFD631","#E6FB4D","#F2C99F","#B9B8E0","#C3E1FE","#CCCCCC"],_items:[],init:function(e){this._parent(e),/\bdark\b/.test(this.options.className)&&!this.options.dropdownOptions.className&&(this.options.dropdownOptions.className="dark"),this.elements.well=this.elements.button.getElement(".uwa-colorpicker-well"),this._updateWell(),this.syncInput()},_getColor:function(){var t=this.getValue()||this.options.defaultValue||"ffffff";if(this._color){var n=s.parse(t),i=this._color;if(e.equals(i.toRGBString(),n.toRGBString()))return this._color;this._color=null}return s.parse(t)},_setColor:function(e){this.setValue(e.toHSLString())},_setView:function(e){var t=e||this.options.defaultView;return this.previouslyDisplayedHue=void 0,t!==this.currentView&&(this.elements.viewContainer.setContent(this["build"+a.ucfirst(t)+"View"]()),this.currentView=t,["panton","picker"].forEach(function(e){this.elements.colorpickerContainer.toggleClassName(e+"-view",e===t)}.bind(this))),this},setValue:function(e){"string"==typeof e&&(e=s.parse(e)),e&&(this._color=e,this._parent(e.toHexString().substring(1)))},buildContent:function(){return this.elements.colorpickerContainer=e.createElement("div",{class:this.getClassNames("-container")}),this.elements.viewContainer=e.createElement("div",{class:this.getClassNames("-view-container")}),this.elements.colorpickerContainer.addContent(this.elements.viewContainer),this.elements.colorpickerContainer.addContent(this._buildControls()),this._setView(this.options.defaultView),this.elements.colorpickerContainer},buildPickerView:function(){var s=this,n=e.createElement("div",{html:[this.elements.slPicker=e.createElement("div",{class:this.getClassNames("-sl-picker"),html:[this.elements.slPickerCanvas=e.createElement("canvas",{class:this.getClassNames("-sl-picker-canvas"),width:220,height:220}),this.elements.slPickerCursor=e.createElement("div",{class:this.getClassNames("-sl-picker-cursor")})]}),this.elements.huePicker=e.createElement("div",{class:this.getClassNames("-hue-picker"),html:[this.elements.huePickerArrows=e.createElement("div",{class:this.getClassNames("-hue-picker-arrows")}),this.elements.huePickerCanvas=e.createElement("canvas",{class:this.getClassNames("-hue-picker-canvas"),width:26,height:220})]})]});return new o({element:this.elements.slPicker,hideCursorWhileDragging:!0,pickCallback:function(e){var t,n=e.x,i=1-e.y,l=(2-n)*i;i>0?(t=n*i,t=(t/=l<=1?l:2-l)||0,l/=2):t=e.x,l*=100,t*=100,s._setColor(s._getColor().cloneWith({l:l,s:t}))}}),new o({element:this.elements.huePicker,pickCallback:function(e){s._setColor(s._getColor().cloneWith({h:360*(1-e.y)}))}}),this.elements.hexField.getElement(".uwa-input-hex-field input").addEvents({keyup:function(){s._readInputFieldValue()},input:function(){s._readInputFieldValue()},change:function(){s._readInputFieldValue()}}),this.elements.dropdown.getInnerElement().addEvent("mousedown",function(e){e.target!==s.elements.hexFieldInput.getInputElement()&&t.preventDefault(e)}),this._renderHuePicker(),this.syncInput(),n},buildPantonView:function(){var n=this;return n.elements.pantonContainer=e.createElement("div",{class:n.getClassNames("-panton-container")}),n._items=[],this.panton.forEach(function(t){var i=e.createElement("span",{class:n.getClassNames("-item-content"),styles:{background:t},events:{click:function(){n.dispatchEvent("resetPantonSelection"),this.addClassName("active"),n._setColor(s.parse(t))}}});e.createElement("div",{class:n.getClassNames("-item"),html:[i]}).inject(n.elements.pantonContainer),n._items.push(i)}),this.elements.dropdown.getInnerElement().addEvent("mousedown",function(e){e.target!==n.elements.hexFieldInput.getInputElement()&&t.preventDefault(e)}),n.elements.pantonContainer},_buildControls:function(){var s=this,n=/\bdark\b/.test(this.options.className);return e.createElement("div",{class:this.getClassNames("-controls"),html:[e.createElement("div",{class:this.getClassNames("-panton-button"),events:{mousedown:function(e){t.preventDefault(e)},click:function(e){t.preventDefault(e),s._setView("panton")}}}),e.createElement("div",{class:this.getClassNames("-picker-button"),events:{mousedown:function(e){t.preventDefault(e)},click:function(e){t.preventDefault(e),s._setView("picker")}}}),this.elements.hexField=e.createElement("div",{class:this.getClassNames("-hex-field"),html:[this.elements.hexFieldInput=new l.Text({_root:!1,className:this.getClassNames("-hex-field-input")}),e.createElement("span",{class:this.getClassNames("-hex-field-icon"),text:"#"})]}),this.elements.clearButton=e.createElement("div",{class:this.getClassNames("-clear-button")+" uwa-icon","data-icon":"p",events:{mousedown:function(e){t.preventDefault(e)},click:function(e){t.preventDefault(e),s.setValue(s.options.defaultValue)}}}),new l.Button({_root:!1,value:"Done",className:this.getClassNames("-done-button")+(n?" extra-dark-grey":" dark-grey"),events:{onMouseDown:function(e){t.preventDefault(e)},onClick:function(){s.toggle(!1)}}})]})},resetPantonSelection:function(){for(var e=this._items,t=0;t<e.length;t++)e[t].removeClassName("active")},_renderHuePicker:function(){for(var e=this.elements.huePickerCanvas,t=this.elements.huePickerCanvas.getContext("2d"),n=t.createLinearGradient(0,0,0,e.height),i=0;i<37;i++){var l=i/36,a=s.parse("hsl("+360*(1-l)+", 100%, 50%)");n.addColorStop(l,a.toHSLString())}t.fillStyle=n,t.fillRect(0,0,e.width,e.height)},_updateWell:function(){this.elements.well.setStyle("background",this._getColor().toHSLString())},_updateSlPicker:function(){var e=this.elements.slPickerCanvas,t=this.elements.slPickerCanvas.getContext("2d"),n=this._getColor(),i=n.toObject();if(this.previouslyDisplayedHue!==i.h){this.previouslyDisplayedHue=i.h;var l=t.createLinearGradient(0,0,e.width,0),a=s.parse("hsl("+i.h+", 100%, 50%)");l.addColorStop(0,"white"),l.addColorStop(1,a.toHSLString()),t.fillStyle=l,t.fillRect(0,0,e.width,e.height);var o=t.createLinearGradient(0,0,0,e.height);o.addColorStop(0,"transparent"),o.addColorStop(1,"black"),t.fillStyle=o,t.fillRect(0,0,e.width,e.height)}var r,h,c,u,d=this.elements.slPickerCursor;u=((r=i.l/100*2)+(h=i.s/100*(r<=1?r:2-r)))/2,c=r>0?2*h/(r+h):i.s/100,c=Math.round(c*this.elements.slPickerCanvas.width),u=Math.round((1-u)*this.elements.slPickerCanvas.height),d.setStyle("left",c+"px"),d.setStyle("top",u+"px"),d.setStyle("background",n.toHexString())},_updateHuePicker:function(){var e=this.elements.huePickerArrows,t=(360-this._getColor().toObject().h)/360*this.elements.huePickerCanvas.height;e.setStyle("top",Math.round(t)+"px")},_updateHexField:function(){var t=this.elements.hexFieldInput;if(this.getValue()){var n=this._getColor(),i=s.parse(t.getValue());i&&e.equals(n.toRGBString(),i.toRGBString())||t.setValue(n.toHexString().substring(1))}else t.setValue("")},_updateDefaultButton:function(){this.elements.clearButton.toggle(e.is(this.options.defaultValue)&&this.options.defaultValue!==this.getValue())},syncInput:function(){if(this.elements.well&&this._updateWell(),this.elements.slPicker&&(this._updateSlPicker(),this._updateHuePicker(),this._updateHexField(),this._updateDefaultButton()),this.elements.value){var e=this.getValue();e=e?e.toLowerCase():"",this.elements.value.setText(e)}},_readInputFieldValue:function(){var e,t,n,i=this.elements.hexFieldInput.getValue(),l=(t=!0,(n=(e=i)&&e.match(t?/^\s*#?([0-9a-f]{0,6})/i:/^\s*#?(|[0-9a-f]{3}|[0-9a-f]{6})\s*$/i))?n[1]:"");if(l!==i&&this.elements.hexFieldInput.setValue(l),3===l.length||6===l.length){var a=l.replace(/-/g,"Z"),o=s.parse(a);this.setValue(o)}},toggle:function(e){this._parent.apply(this,arguments),!1===e&&this._dispatchOnChange()}});return e.namespace("Controls/ColorPicker",r,e)});