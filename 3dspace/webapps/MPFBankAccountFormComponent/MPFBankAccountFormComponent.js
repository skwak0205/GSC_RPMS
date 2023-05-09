define("DS/MPFBankAccountFormComponent/BankAccountFormComponent",["UWA/Core","UWA/Element","UWA/Class/View","DS/UIKIT/Input/Text","DS/UIKIT/Input/Button","DS/UIKIT/Alert","DS/MPFCountrySelectComponent/CountrySelectComponent","i18n!DS/MPFBankAccountFormComponent/assets/nls/BankAccountFormComponent","css!DS/MPFUI/MPFUI"],function(t,e,n,s,i,a,o,r){"use strict";return n.extend({setup:function(e){if(e||(e={}),!t.is(e.constraints))throw new Error("option.constraints parameter is required");this.container.addClassName("mpf-bank-account-form"),this.constraints=e.constraints,this.countrySelectComponent=new o({model:this.model}),void 0!==e.showCountry?this.showCountry=e.showCountry:this.showCountry=!0,this.inputs=[],this.validated=!1,this.saveButton=null,this.editable=!1!==e.editable,void 0!==e.readOnly&&(this.editable=!0!==e.readOnly),this.showSaveSuccess=!1,this.listenTo(this.model,"onChange:country",this.render),this.listenTo(this.model,"onSync",this.render)},domEvents:{"click .mpf-commands .btn-primary":"save"},render:function(){var e,n,s,i,a;return e=[],n=this._createMessageContainer(),e.push(this._createValidationIcon()),s=this._createEntries(this.model,this.constraints),Array.prototype.push.apply(e,s),this.showCountry&&e.push(this.countrySelectComponent.render()),i=this._createCommandsContainer(),e.push(i),a=t.createElement("form",{class:"mpf-form",html:e}),this.validated&&a.addClassName("validated"),this.container.setContent([n,a]),this.showSaveSuccess&&(this.showSaveSuccess=!1,t.createElement("p",{class:"mpf-success",html:[{tag:"span",class:"fonticon fonticon-check"},{tag:"span",text:r.get("bankAccountSaved")}]}).inject(n)),this},destroy:function(){this.countrySelectComponent.destroy(),this.countrySelectComponent=null,this.stopListening(this.model),this.model=null,this._parent(),this.container=null},save:function(){var t,e,n,s,i,a=this;if((t=this.container.getElement(".mpf-messages")).removeClassName("mpf-error"),t.empty(),this.validated=!0,this.container.getElement(".mpf-form").addClassName("validated"),e=this.container.getElements("input").reduce(function(t,e){return t[e.name]=e.value.trim(),t},{}),this.model.set(e))if(this.saveButton.load(!0),this._setFieldsEnabled(!1),(n=this.model.validate(this.model.toJSON()))&&n.length>0){for(s=n.length,i=0;i<s;i++)this._addErrorMessage(n[i]);this.saveButton.load(!1),this._setFieldsEnabled(!0)}else this.model.save(null,{onComplete:function(){a.showSaveSuccess=!0,a.saveButton.load(!1),a.saveButton.disable()},onFailure:function(){a._addErrorMessage(r.get("saveFailure")),a.saveButton.load(!1),a._setFieldsEnabled(!0)}})},isReadOnly:function(){return!this.editable||!this.model.isNew()},setReadOnly:function(t){this.editable=!t,this._setFieldsEnabled(this.editable),this.render()},_createValidationIcon:function(){var e;return e=t.createElement("div",{class:"mpf-kyc-validation-icon"}),this.model.isNew()||e.addClassName("validated"),e},_createMessageContainer:function(){return t.createElement("div",{class:"mpf-messages"})},_addErrorMessage:function(e){var n;n=this.container.getElement(".mpf-messages"),this.saveButton.load(!1),n.addClassName("mpf-error"),t.createElement("p",{text:r.get(e)}).inject(n)},_createEntries:function(){var t,e,n=this,s=[];return e=this.model.get("country"),t=this.constraints.get(e)||this.constraints.get("DEFAULT"),this.inputs=[],t&&t.keys().filter(function(t){return"country"!==t&&void 0!==n.model.get(t)}).forEach(function(e){var i,a;null!==(i=t.get(e))&&(a=n.model.get(e),s.push(n._createEntry(r.get(e),e,a,i)))}),s},_createEntry:function(e,n,i,a){var o;return a.required&&(e+='&nbsp;<em class="mpf-required">*</em>'),o=new s({name:n,value:i,pattern:a.pattern,required:a.required}),this.isReadOnly()&&o.disable(),this.inputs.push(o),t.createElement("div",{class:"mpf-entry",html:[{tag:"label",html:[{tag:"span",class:"mpf-label",html:e},o]}]})},_createCommandsContainer:function(){var e=[];return this.isReadOnly()||(this.saveButton=new i({value:r.get("Save"),className:"primary"}),e.push(this.saveButton)),t.createElement("div",{class:"mpf-commands",html:e})},_setFieldsEnabled:function(t){this.inputs.map(function(e){e.setDisabled(!t)})}})}),define("DS/MPFBankAccountFormComponent/BankAccountFormComponentV2",["UWA/Core","UWA/Class/View","DS/UIKIT/Alert","DS/UIKIT/Input/Button","DS/MPFView/FieldTextInput","DS/MPFServices/ObjectService","DS/MPFBankAccountModel/BankAccountModel","DS/MPFError/BadArgumentError","DS/MPFError/ValidationError","i18n!DS/MPFBankAccountFormComponent/assets/nls/BankAccountFormComponent","css!DS/MPFUI/MPFUI"],function(t,e,n,s,i,a,o,r,c,u){"use strict";return e.extend({className:"mpf-bank-account-form",setup:function(e){if(e=e||{},a.requiredOfPrototype(e.bankAccount,o,"BankAccountFormComponent expects options.bankAccount to be a BankAccountModel"),this.bankAccount=e.bankAccount,t.is(e.constraints,"object"))this.bankAccountConstraints=e.constraints;else{if(!t.is(this.bankAccount.constraints,"object"))throw new r("Constraints were not found in BankAccountModel");this.bankAccountConstraints=this.bankAccount.constraints}if(this.country=e.bankAccount.get("country"),!t.is(this.country,"string"))throw new r("The country attribute in the passed BankAccountModel has to be set before creating the BankAccountFormComponentV2","BankAccountFormComponentV2",42);this.readOnly=e.readOnly||!1,this.alert=this._createAlert(),this.inputs=this._createInputs(),this.buttonContainer=this._createButtonContainer()},render:function(){for(var t=[],e=0;e<this.inputs.length;e++)t.push(this.inputs[e].render());return this.container.setContent([this.alert,{tag:"div",class:"mpf-bank-account-form-input-container",html:t},this.buttonContainer]),this},destroy:function(){this.stopListening(),this.bankAccount=null,this.bankAccountConstraints=null,this.country=null;for(var t=0;t<this.inputs.length;t++)this.inputs[t].destroy(),this.inputs[t]=null;this._parent(),this.container=null},savePromise:function(){var e=this;return this._validateInputs()?(this.saveButton.load(!0),this.bankAccount.savePromise(this._getCurrentValues()).then(function(){e.alert.add({className:"success",message:u.get("bankAccountSaved")}),e.saveButton.load(!1),e.saveButton.disable(),e.setReadOnly(!0)}).catch(function(t){var n=t.response&&t.response.errorMsg?t.response.errorMsg:t;return console.error(n),e.alert.add({className:"error",message:u.get("saveFailure")}),e.saveButton.load(!1),n})):(this.alert.add({className:"error",message:u.get("saveFailure")}),this.saveButton.load(!1),t.Promise.reject(new c(u.get("validationFailure"))))},setReadOnly:function(t){this.readOnly=t,this.buttonContainer=this._createButtonContainer(),this.inputs.forEach(function(e){e.setDisabled(t)}),this.render()},_onChange:function(){this.readOnly||(this._validateFilledInputs(),this._validateModel()?this.saveButton.enable():this.saveButton.disable())},_createAlert:function(){return new n({visible:!0,autoHide:!0,hideDelay:3e3})},_createButtonContainer:function(){var e=t.createElement("div",{class:"mpf-commands"});return this.readOnly||(this.saveButton=new s({className:"primary",value:u.get("Save"),disabled:!0,events:{onClick:this.savePromise.bind(this)}}).inject(e)),e},_createInputs:function(){var e=[],n=this,s=this._getBankAccountConstraints();if(t.is(s)&&0!==s.keys().length){for(var a=s.getConstraintKeyNames(),o=0;o<a.length;o++){var r=a[o];if(void 0!==n.bankAccount.get(r)){var c=new i({model:n.bankAccount,fieldName:r,fieldLabel:u.get(r),required:!0,readOnly:n.readOnly,dynamicValidation:!0,force:!0,saveOnChange:!1,silent:!1});n.listenTo(c,"update",function(){n._onChange()}),e.push(c)}}return e}},_getBankAccountConstraints:function(){return this.bankAccountConstraints.get(this.country)||this.bankAccountConstraints.get("DEFAULT")},_validateInputs:function(){for(var e=0;e<this.inputs.length;e++)if(t.is(this.inputs[e].validate()))return!1;return!0},_validateFilledInputs:function(){for(var e=!0,n=0;n<this.inputs.length;n++){var s=this.inputs[n].fieldName,i=this.bankAccount.get(s);t.is(i,"string")&&i.length>0&&t.is(this.inputs[n].validate())&&(e=!1)}return e},_validateModel:function(){var e=this.bankAccount.validate(this._getCurrentValues());return!(t.is(e)&&e.length>0)},_getCurrentValues:function(){for(var t={},e=0;e<this.inputs.length;e++){var n=this.inputs[e].fieldName,s=this.bankAccount.get(n);t[n]=s}return t}})});