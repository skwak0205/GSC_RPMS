define("DS/MPFBuyerAddressComponent/BuyerAddressPanelFooter",["UWA/Class/View","DS/UIKIT/Input/Button","i18n!DS/MPFBuyerAddressComponent/assets/nls/BuyerAddressComponent"],function(e,t,s){"use strict";const r={ON_SAVE:"onSave",ON_CANCEL:"onCancel"},i=e.extend({className:"mpf-buyer-address-panel-footer mpf-footer",setup(e){this.readOnly=!0===e.readOnly,this.showCancelButton=!1!==e.showCancelButton,this.saveButton=this._createSaveButton(),this.cancelButton=this._createCancelButton()},render(){const e=[];return this.readOnly||(e.push(this.saveButton),this.showCancelButton&&e.push(this.cancelButton)),this.container.setContent(e),this},destroy(){this._parent(),this.container=null},setDisabled(e){e!==this.readOnly&&(this.readOnly=!1!==e,this.render())},_createSaveButton(){return new t({className:"primary",value:s.get("save"),events:{onClick:this.dispatchEvent.bind(this,r.ON_SAVE)}})},_createCancelButton(){return new t({value:s.get("cancel"),events:{onClick:this.dispatchEvent.bind(this,r.ON_CANCEL)}})}});return i.Events=Object.freeze(r),i}),define("DS/MPFBuyerAddressComponent/BuyerInformationBox",["UWA/Core","UWA/Class/View","i18n!DS/MPFBuyerAddressComponent/assets/nls/BuyerAddressComponent"],function(e,t,s){"use strict";return t.extend({className:"mpf-info-box",setup:function(e){this._parent(e)},render:function(){var e;return e="CompanyModel"===this.model._type?this._createCompanyInfo():this._createPersonInfo(),this.container.setContent(e),this},_createPersonInfo:function(){var e;return e=this.model.getFirstName()+" "+this.model.getLastName(),this._createEntry(e)},_createCompanyInfo:function(){var e,t;return e=this.model.getName(),t=this.model.getRegistrationId(),[this._createEntry(e),this._createEntry(s.get("taxRegistrationId"),void 0),this._createEntry(s.get("companyRegistrationId"),t)]},_createEntry:function(t,s){var r,i;return r=[],t&&(i=e.createElement("span",{class:"mpf-label",text:t}),r.push(i)),s&&(i=e.createElement("span",{class:"mpf-value",text:s}),r.push(i)),e.createElement("p",{className:"mpf-info-box-item",html:r})}})}),define("DS/MPFBuyerAddressComponent/BuyerAddressCard",["UWA/Core","UWA/Class/View","DS/MPFAddressModel/AddressModel","css!DS/MPFUI/MPFUI"],function(e,t,s){"use strict";var r={ON_SELECTED:"onSelected"},i=t.extend({className:"mpf-address-card",domEvents:{click:"cardClicked"},setup:function(e){this._parent(e),this.buyer=e.buyer,this.showPhoneNumber=!0===e.showPhoneNumber,this.isSelectable=!1!==e.isSelectable,this.selected=!0===e.selected,this.model.get("addressLine0")||this.model.set("addressLine0",this._getAddressTitle()),this.isSelectable&&this.container.addClassName("mpf-selectable")},render:function(){var t,r=this.model;return t=[s.Fields.LINE1,s.Fields.LINE2,s.Fields.LINE3,s.Fields.LINE4,s.Fields.CITY,s.Fields.COUNTY,s.Fields.STATE,s.Fields.POSTAL_CODE,s.Fields.COUNTRY],this.showPhoneNumber&&t.push(s.Fields.PHONE_NUMBER),(t=t.map(function(e){return r.get(e)}).filter(function(e){return e&&e.length>0}).map(function(t){return e.createElement("p",{class:"mpf-address-card-line",text:t})})).unshift(e.createElement("p",{class:"mpf-address-card-line title",text:this.model.get("addressLine0")})),this.container.setContent(t),this.selected?this.container.addClassName("selected"):this.container.removeClassName("selected"),this},destroy:function(){this.model=null,this._parent(),this.container=null},cardClicked:function(){this.isSelectable&&!this.selected&&(this.selected=!0,this.render(),this.dispatchEvent(r.ON_SELECTED,this.model))},setSelected:function(e){e!==this.selected&&(this.selected=e,this.render())},_getAddressTitle:function(){return void 0!==this.buyer.defaults.registrationId?this.buyer.get("name"):this.buyer.get("firstName")+" "+this.buyer.get("lastName")}});return i.Events=Object.freeze(r),i}),define("DS/MPFBuyerAddressComponent/BuyerAddressFormV2",["UWA/Class/View","UWA/Class/Promise","DS/MPFServices/ObjectService","DS/MPFAddressModel/AddressModel","DS/MPFPersonModel/PersonModel","DS/MPFCountryModel/CountryCollection","DS/MPFAddressModel/AddressFactory","DS/MPFCompanyModel/CompanyFactory","DS/MPFBuyer/BuyerType","DS/MPFView/FieldTextInputV2","DS/MPFCompanyComponent/CompanyNameInput","DS/MPFCompanyComponent/CompanyRegistrationIdInput","DS/MPFCompanyComponent/CompanyTaxRegistrationIdInput","DS/MPFAddressFormComponent/AddressFormComponentV2","i18n!DS/MPFBuyerAddressComponent/assets/nls/BuyerAddressComponent"],function(e,t,s,r,i,n,o,d,a,h,l,c,u,y,p){"use strict";var m,C={};return C.BILLING="billing",C.SHIPPING="shipping",(m=e.extend({className:"mpf-buyer-address-form-v2",setup:function(e){s.requiredOfPrototype(e.address,r,"options.address must be a AddressModel"),s.requiredOfPrototype(e.addressFactory,o,"options.addressFactory must be an AddressFactory"),s.requiredOfPrototype(e.companyFactory,d,"options.companyFactory must be a CompanyFactory"),s.requiredOfPrototype(e.countries,n,"options.countries must be a CountryCollection"),s.requiredOfPrototype(e.person,i,"options.person must be a PersonModel"),this.address=e.address,this.addressFactory=e.addressFactory,this.companyFactory=e.companyFactory,this.countries=e.countries,this.person=e.person,this.company=e.company,this.type=e.type||C.SHIPPING,this.buyerType=this.company?a.COMPANY:a.INDIVIDUAL,this.buyerTypeLocked=!0===e.buyerTypeLocked,this.readOnly=!0===e.readOnly,this.countryReadOnly=e.countryReadOnly||this._getCountryReadOnlyDefaultValue(),this.listenTo(this.address,"onChange:"+r.Fields.COUNTRY,this._onChangeCountry.bind(this))},render:function(){return this.buyerInputs=this._createBuyerInputs(),this.addressForm=this._createAddressForm(),this.container.setContent([this.buyerInputs.map(function(e){return e.render()}),this.addressForm.inputs.map(function(e){return e.render()})]),this.addressForm._applyCountryFormatAddress(),this},validate:function(){var e=!0;return this.buyerInputs.forEach(function(t){t.validate().isValid||(e=!1)}),this.addressForm.validate()&&e},save:function(){return this.buyerType===a.COMPANY?this._saveCompany().then(this._saveAddress.bind(this)):this.person.savePromise().then(this._saveAddress.bind(this))},setDisabled:function(e){e=!1!==e,this.readOnly=e,this.render()},setBuyerType:function(e){e!==this.buyerType&&(this.buyerType=e,this.buyerType!==a.COMPANY||this.company||(this.company=this.companyFactory.createModel(d.Types.COMPANY,null,{parentResourceId:this.person.get("id"),addressFactory:this.addressFactory})),this.render())},destroy:function(){this.stopListening(),this.buyerTypeSelector&&this.buyerTypeSelector.destroy(),this.buyerForm&&this.buyerForm.destroy(),this.addressForm&&this.addressForm.destroy(),this.address=null,this.addressFactory=null,this.companyFactory=null,this.countries=null,this.person=null,this.company=null,this._parent(),this.container=null},_saveCompany:function(){return this.company.isNew()?this.company.savePromise():this.company.hasPendingChanges()?this.company.savePromise(this.company.getPendingChanges(),{patch:!0}):t.resolve()},_getCountryReadOnlyDefaultValue:function(){return this.type===C.BILLING&&(!!this.company&&!this.company.isNew()&&!!this.company.getCountry())},_onChangeCountry:function(){this.type===C.BILLING&&this.company&&"DEFAULT"!==this.address.getCountry()&&this.company.setCountry(this.address.getCountry()),this.addressForm._onCountryChange()},_saveAddress:function(){if(this.address.isNew()){var e=this.buyerType===a.COMPANY?o.Types.COMPANY:o.Types.ME,t=this.buyerType===a.COMPANY?this.company.get("id"):this.person.get("id");this.address.dataProxy=this.addressFactory.getDataProxy(e),this.address.setParentResourceId(t)}return this.address.set(r.Fields.IS_BILL_TO,"TRUE"),this.address.set(r.Fields.IS_SHIP_TO,"TRUE"),this.address.savePromise().catch(this._saveRetryForInvalidBillingAddress.bind(this))},_saveRetryForInvalidBillingAddress:function(e){var s;return this.type===C.BILLING?s=t.reject(e):(this.address.set(r.Fields.IS_BILL_TO,"FALSE"),s=this.address.savePromise()),s},_createBuyerInputs:function(){var e=[];return this.buyerType===a.COMPANY?(e.push(this._createCompanyNameInput()),this.type===C.BILLING&&(e.push(this._createCompanyTaxRegistrationIdInput()),e.push(this._createCompanyRegistrationIdInput()))):(e.push(this._createFirstNameInput()),e.push(this._createLastNameInput())),e},_createCompanyNameInput:function(){return new l({model:this.company,readOnly:this.readOnly||!this.company.isNew()&&!!this.company.getName(),required:!0,dynamicValidation:!0})},_createCompanyTaxRegistrationIdInput:function(){return new u({model:this.company,readOnly:this.readOnly,required:!0,dynamicValidation:!0})},_createCompanyRegistrationIdInput:function(){return new c({model:this.company,readOnly:this.readOnly,required:!0,dynamicValidation:!0})},_createFirstNameInput:function(){return new h({model:this.person,fieldName:"firstName",fieldLabel:p.get("firstName"),required:!0,readOnly:!0,dynamicValidation:!0})},_createLastNameInput:function(){return new h({model:this.person,fieldName:"lastName",fieldLabel:p.get("lastName"),required:!0,readOnly:!0,dynamicValidation:!0})},_createAddressForm:function(){return new y({addressModel:this.address,addressConstraints:this.addressFactory.getConstraints(),countries:this.countries,preSetCountry:this.preSetCountry,readOnly:this.readOnly,countryReadOnly:this.readOnly||this.countryReadOnly,showPhoneNumber:!0,required:!0})}})).Types=Object.freeze(C),m}),define("DS/MPFBuyerAddressComponent/BuyerTypeSelectorInput",["UWA/Core","UWA/Class/View","DS/UIKIT/Input/Toggle","DS/MPFUtils/MPFUtils","i18n!DS/MPFBuyerAddressComponent/assets/nls/BuyerAddressComponent"],function(e,t,s,r,i){"use strict";var n={ON_CHANGE:"onChange"},o={COMPANY:"company",PERSON:"person"},d=t.extend({className:"mpf-buyer-type",setup:function(e){this._parent(e),this.readOnly=!0===e.readOnly,this.inputs=[],this.isCompany=void 0!==this.model.defaults.registrationId,this.selected=this.isCompany?o.COMPANY:o.PERSON,this.toggleName="buyerType_"+r.generateUUID()},domEvents:{click:"onClick"},render:function(){var t,r;return this.inputs=[new s({name:this.toggleName,value:o.PERSON,className:"primary",label:i.get("personAddress"),checked:!this.isCompany,disabled:this.readOnly}),new s({name:this.toggleName,value:o.COMPANY,className:"primary",label:i.get("companyAddress"),checked:this.isCompany,disabled:this.readOnly})],t=e.createElement("label",{class:"mpf-person",html:[this.inputs[0]]}),r=e.createElement("label",{class:"mpf-company",html:[this.inputs[1]]}),this.container.setContent([t,r]),this},onClick:function(){var e;(e=this.getSelectedType())!==this.selected&&(this.isCompany=e===o.COMPANY,this.selected=e,this.dispatchEvent(n.ON_CHANGE,[this.selected]))},getSelectedType:function(){return this.inputs.filter(function(e){return e.isChecked()}).shift().getValue()},setReadOnly:function(e){this.readOnly=!0===e,this.render()},destroy:function(){this.model=null,this._parent(),this.container=null}});return d.Types=Object.freeze(o),d.Events=Object.freeze(n),d}),define("DS/MPFBuyerAddressComponent/BuyerAddressPanel",["UWA/Core","UWA/Class/View","DS/UIKIT/Input/Button","DS/UIKIT/Mask","DS/UIKIT/Alert","DS/MPFServices/ObjectService","DS/MPFAddressModel/AddressModel","DS/MPFPersonModel/PersonModel","DS/MPFCountryModel/CountryCollection","DS/MPFAddressModel/AddressFactory","DS/MPFCompanyModel/CompanyFactory","DS/MPFBuyer/BuyerType","DS/MPFView/EmptyView","DS/MPFBuyerAddressComponent/BuyerTypeSelectorInput","DS/MPFBuyerAddressComponent/BuyerAddressFormV2","DS/MPFBuyerAddressComponent/BuyerAddressPanelFooter","i18n!DS/MPFBuyerAddressComponent/assets/nls/BuyerAddressComponent","css!DS/MPFBuyerAddressComponent/MPFBuyerAddressComponent"],function(e,t,s,r,i,n,o,d,a,h,l,c,u,y,p,m,C){"use strict";var A,f=p.Types,g={};return g.ON_CANCEL="onCancel",g.ON_SAVE="onSave",g.ON_ERROR="onError",(A=t.extend({className:"mpf-buyer-address-panel",setup:function(e){n.requiredOfPrototype(e.address,o,"options.address must be a AddressModel"),n.requiredOfPrototype(e.addressFactory,h,"options.addressFactory must be an AddressFactory"),n.requiredOfPrototype(e.companyFactory,l,"options.companyFactory must be a CompanyFactory"),n.requiredOfPrototype(e.countries,a,"options.countries must be a CountryCollection"),n.requiredOfPrototype(e.person,d,"options.person must be a PersonModel"),this.address=e.address,this.addressFactory=e.addressFactory,this.companyFactory=e.companyFactory,this.countries=e.countries,this.person=e.person,this.buyerType=this._getBuyerType(this.person),this.company=e.company||this.buyerType===c.COMPANY&&this._createCompany(),this.type=e.type||f.SHIPPING,this.buyerTypeLocked=!0===e.buyerTypeLocked,this.readOnly=!0===e.readOnly,this.showMyAddressesLink=!1!==e.showMyAddressesLink,this.showCancelButton=!1!==e.showCancelButton,this.countryReadOnly=e.countryReadOnly,this.title=e.title,this.serviceView=e.serviceView||u.getInstance(),this.alert=this._createAlert(),this.header=this._createHeader(),this.footer=this._createFooter()},render:function(){var e=[this.alert,this.header];return this.buyerTypeLocked||(this.buyerTypeSelectorInput=this._createBuyerTypeSelector(),e.push(this.buyerTypeSelectorInput.render())),this.form=this._createForm(),e.push(this.form.render()),e.push(this.serviceView.render()),e.push(this.footer.render()),this.container.setContent(e),this},validate:function(){return this.form.validate()},save:function(){if(this.validate())return r.mask(this.container),this.form.save().then(()=>{r.unmask(this.container),this.dispatchEvent(g.ON_SAVE,{address:this.address,buyerType:this.buyerType})}).catch(e=>{r.unmask(this.container),console.error(e),this.alert.add({className:"error",message:C.get("saveAddressError")}).show(),this.dispatchEvent(g.ON_ERROR,e)})},setDisabled:function(e){this.readOnly=!1!==e,this.buyerTypeSelectorInput&&this.buyerTypeSelectorInput.setReadOnly(this.readOnly),this.form&&this.form.setDisabled(this.readOnly),this.footer.setDisabled(this.readOnly)},destroy:function(){this.stopListening(),this.form&&this.form.destroy(),this.footer.destroy(),this.address=null,this.addressFactory=null,this.companyFactory=null,this.countries=null,this.person=null,this.company=null,this._parent(),this.container=null},_onBuyerTypeChange:function(e){this.buyerType=e===y.Types.COMPANY?c.COMPANY:c.INDIVIDUAL,this.form.setBuyerType(this.buyerType)},_createAlert:function(){return new i({visible:!1,autoHide:!0})},_createHeader:function(){return e.createElement("div",{class:"mpf-address-panel-header",html:[{tag:"h4",class:"mpf-title",text:this.title||this._getTitle()},this.showMyAddressesLink&&this._createMyAddressesLink()]})},_getTitle:function(){return C.get((this.address.isNew()?"create":"edit")+(this.type===f.SHIPPING?"ShippingAddress":"BillingAddress"))},_createMyAddressesLink:function(){return e.createElement("div",{class:"mpf-link btn-link-icon",html:[{tag:"span",class:"fonticon fonticon-back fonticon-clickable"},{tag:"a",class:"btn-link",text:C.get("addressList")}],events:{click:this.dispatchEvent.bind(this,g.ON_CANCEL)}})},_createBuyerTypeSelector:function(){var e=new y({model:this.buyerType===c.COMPANY?this.company:this.person});return this.listenTo(e,y.Events.ON_CHANGE,this._onBuyerTypeChange.bind(this)),e},_createForm:function(){return new p({address:this.address,person:this.person,company:this.company,addressFactory:this.addressFactory,companyFactory:this.companyFactory,countries:this.countries,readOnly:this.readOnly,type:this.type,buyerTypeLocked:this.buyerTypeLocked,countryReadOnly:this.countryReadOnly})},_createFooter:function(){const e=new m({readOnly:this.readOnly,showCancelButton:this.showCancelButton});return this.listenTo(e,m.Events.ON_SAVE,this.save.bind(this)),this.listenTo(e,m.Events.ON_CANCEL,this.dispatchEvent.bind(this,g.ON_CANCEL)),e},_createCompany(){return this.companyFactory.createModel(l.Types.COMPANY,null,{parentResourceId:this.person.get("id"),addressFactory:this.addressFactory})},_getBuyerType:e=>e.get(d.Fields.BUYER_TYPE)===c.INDIVIDUAL.name?c.INDIVIDUAL:c.COMPANY})).Events=Object.freeze(g),A.Types=Object.freeze(f),A}),define("DS/MPFBuyerAddressComponent/BuyerAddressForm",["UWA/Core","UWA/Class/View","UWA/Promise","DS/UIKIT/Mask","DS/MPFServices/ObjectService","DS/MPFAddressModel/AddressModel","DS/MPFCountryModel/CountryCollection","DS/MPFBuyerAddressComponent/BuyerTypeSelectorInput","DS/MPFCompanyComponent/CompanyForm","DS/MPFCompanyComponent/CompanyNameForm","DS/MPFPersonComponent/PersonNameForm","DS/MPFAddressFormComponent/AddressFormComponentV2","DS/MPFBuyer/BuyerType","DS/MPFError/ValidationError"],function(e,t,s,r,i,n,o,d,a,h,l,c,u,y){"use strict";return t.extend({className:"mpf-buyer-address-form",setup:function(e){var t;this._parent(e),i.requiredOfPrototype(e.countries,o,"options.countries must be a CountryCollection"),this.address=e.address,this.buyer=e.buyer||null,this.buyerType=u.fromModel(this.buyer),this.addressFactory=e.addressFactory,this.buyerTypeLocked=!0===e.buyerTypeLocked,this.companyFactory=e.companyFactory,this.countryReadOnly=!0===e.countryReadOnly,this.isBillingAddress=!0===e.isBillingAddress,this.isCompany=this.buyerType===u.COMPANY,this.authorized=!0===e.authorized,this.countries=e.countries,this.showPhoneNumber=e.showPhoneNumber,(t=this._isBuyerTypeDetermined())||this.isCompany||this._changeBuyerType("individual"),this.buyerTypeLocked||(this.buyerTypeSelector=new d({model:this.buyer,readOnly:t}),this.listenTo(this.buyerTypeSelector,"onChange",this.onBuyerTypeChange)),this.buyerForm=null,this.addressForm=null,this._initCountryListener()},render:function(){var e=[];return this.isCompany?this.buyerForm=this._createCompanyForm(this.buyer,this.isBillingAddress):this.buyerForm=new l({model:this.buyer,readOnly:!0,lockSave:!0,required:!0}),this.addressForm=new c({addressModel:this.address,addressConstraints:this.address.constraints,countries:this.countries,preSetCountry:this.preSetCountry,readOnly:!this.address.isNew(),countryReadOnly:this.countryReadOnly,showPhoneNumber:this.showPhoneNumber}),this.buyerTypeLocked||e.push(this.buyerTypeSelector.render()),e.push(this.buyerForm.render()),e.push(this.addressForm.render()),this.container.setContent(e),this},save:function(){var e,t,i=this;return r.mask(this.container),e=this.buyerForm.save.bind(this.buyerForm),t=this._saveBillingAddressPromise.bind(this),s.all([this._validateBuyerPromise(),this._validateAddressPromise()]).then(e).then(t).catch(function(e){var t;return t=e instanceof Error?e:new Error("Unable to save buyer or address form"),s.reject(t)}).finally(function(){r.unmask(i.container)})},validate:function(){return this.buyerForm.validate()&&this.addressForm.validate()},onBuyerTypeChange:function(e){this._changeBuyerType(e),this.render()},destroy:function(){this.stopListening(),this.buyerTypeSelector.destroy(),this.address=null,this.buyer=null,this.countries=null,this._parent(),this.container=null},addClassName:function(e){this.container.addClassName(e)},removeClassName:function(e){this.container.removeClassName(e)},clear:function(){this.addressForm&&this.addressForm.clear()},_isBuyerTypeDetermined:function(){var e;return e=this.buyer.addresses.filter(function(e){return!e.isNew()}).length>0,this.isCompany||e},_changeBuyerType:function(e){this.isCompany="company"===e,this._initCountryListener(),"company"===e?(this.userBuyer=this.buyer,this.isBillingAddress?(this.countryReadOnly=!this.address.isNew()&&this.address.getCountry(),this.buyer=this.companyFactory.createModel("me",{country:this.address.getCountry()})):(this.countryReadOnly=!1,this.buyer=this.companyFactory.createModel("me"))):this.userBuyer&&(this.buyer=this.userBuyer,this.countryReadOnly=!1)},_initCountryListener:function(){this.stopListening(this.address,"onChange:"+n.Fields.COUNTRY),this.isCompany&&this.isBillingAddress&&this.listenTo(this.address,"onChange:"+n.Fields.COUNTRY,this._onChangeCountry.bind(this))},_createCompanyForm:function(e,t){return t&&this.authorized?new a({model:e,countries:this.countries,hideCountry:!0}):new h({model:e,readOnly:!e.isNew(),required:!0})},_onChangeCountry:function(){this.isCompany&&"DEFAULT"!==this.address.getCountry()&&this.buyer.set("country",this.address.getCountry())},_validateBuyerPromise:function(){var e=this;return new s(function(t,s){e.buyerForm.validate()?t():s(new y("Buyer validation failed"))})},_validateAddressPromise:function(){var e=this;return new s(function(t,s){e.addressForm.validate()?t():s(new y("Unable to save address"))})},_saveBillingAddressPromise:function(){var e;if(this.addressForm.addressModel.isNew())return this.isCompany?(e=this.buyer.get("name"),this.addressForm.addressModel.dataProxy=this.addressFactory.getDataProxy("companyAddress")):e=this.buyer.get("firstName")+" "+this.buyer.get("lastName"),this.addressForm.addressModel.set("isBillTo","TRUE"),this.addressForm.addressModel.set("isShipTo","TRUE"),this.addressForm.addressModel.parentResourceId=this.buyer.id,this.addressForm.addressModel.set("addressLine0",e),this.addressForm.savePromise().catch(this._saveRetryForShippingAddress.bind(this))},_saveRetryForShippingAddress:function(e){var t;return this.isBillingAddress?t=s.reject(e):(this.addressForm.addressModel.set("isBillTo","FALSE"),t=this.addressForm.savePromise()),t}})}),define("DS/MPFBuyerAddressComponent/BuyerAddressList",["UWA/Core","UWA/Class/View","DS/UIKIT/Scroller","DS/MPFBuyer/BuyerType","DS/MPFBuyerAddressComponent/BuyerAddressCard"],function(e,t,s,r,i){"use strict";var n,o={};return o.ADDRESS_SELECTED="AddressSelected",(n=t.extend({className:"mpf-buyer-address-list",setup:function(e){this._parent(e),this.addressCards=[],this.buyer=e.buyer,this.buyerType=e.buyerType,this.showPhoneNumber=e.showPhoneNumber,this.isSelectable=!1!==e.isSelectable,this.isBillingAddress=!0===e.isBillingAddress,this.selectedAddress=this.isSelectable?e.selectedAddress||this._getDefaultSelection():null,this.scroller=null,this.listenTo(this.model,"onChange",this.render.bind(this))},render:function(){var t,r;return this._createAddressCards(),t=this.addressCards.map(function(e){return e.render()}),r=e.createElement("div",{class:"mpf-address-cards",html:t}),this.scroller=new s({element:r}),this.container.setContent(this.scroller),this},getSelectedAddress:function(){return this.selectedAddress},setSelectedAddress:function(e){var t=this.model.get(e);t&&(this.selectedAddress=t,this.addressCards.forEach(function(e){e.setSelected(e.model.id===t.id)}),this.dispatchEvent(o.ADDRESS_SELECTED,t))},scrollToSelectedAddress:function(){this.scroller&&this.scroller.scrollToElement(".selected")},removeClassName:function(e){this.container.removeClassName(e)},addClassName:function(e){this.container.addClassName(e)},size:function(){return this.addressCards.length},_isAddressesPersistedAndOfExpectedType:function(e){var t,s;return this.isBillingAddress?(t=e.isBillTo(),s=this.buyerType!==r.COMPANY||!this.buyer.getCountry()||this.buyer.getCountry()===e.getCountry()):(t=e.isShipTo(),s=!0),!e.isNew()&&t&&s},_createAddressCard:function(e){return new i({model:e,buyer:this.buyer,showPhoneNumber:this.showPhoneNumber,isSelectable:this.isSelectable,selected:!!this.selectedAddress&&this.selectedAddress.get("id")===e.get("id")})},_listenCardSeleted:function(e){this.isSelectable&&this.listenTo(e,i.Events.ON_SELECTED,this.setSelectedAddress.bind(this))},_stopListeningCardSelected:function(e){this.stopListening(e,i.Events.ON_SELECTED)},_createAddressCards:function(){this.addressCards.forEach(this._stopListeningCardSelected.bind(this)),this.addressCards=this._filterAddresses().map(this._createAddressCard.bind(this)),this.addressCards.forEach(this._listenCardSeleted.bind(this))},_filterAddresses:function(){return this.model.filter(this._isAddressesPersistedAndOfExpectedType.bind(this))},_getDefaultSelection:function(){var e=this._filterAddresses();return e.length>0?e[0]:null}})).Events=Object.freeze(o),n}),define("DS/MPFBuyerAddressComponent/BuyerAddressBook",["UWA/Core","UWA/Class/View","DS/UIKIT/Modal","DS/UIKIT/Input/Button","DS/UIKIT/Alert","DS/MPFServices/ObjectService","DS/MPFCountryModel/CountryCollection","DS/MPFAddressModel/AddressFactory","DS/MPFAddressModel/AddressModel","DS/MPFBuyerAddressComponent/BuyerAddressList","DS/MPFBuyerAddressComponent/BuyerAddressForm","DS/MPFBuyer/BuyerType","DS/MPFError/ValidationError","DS/MPFFiniteStateMachine/FiniteStateMachine","DS/MPFUrl/UrlUtils","i18n!DS/MPFBuyerAddressComponent/assets/nls/BuyerAddressComponent","css!DS/MPFBuyerAddressComponent/MPFBuyerAddressComponent"],function(e,t,s,r,i,n,o,d,a,h,l,c,u,y,p,m){"use strict";var C={CREATION:"creation",LIST:"list",SAVING:"saving"};return t.extend({className:"buyer-address-book",setup:function(e){e||(e={}),this._parent(e),n.requiredOfPrototype(e.countries,o,"options.countries must be a CountryCollection"),this.me=e.me,this.countries=e.countries,this.companyFactory=e.companyFactory,this.cartFactory=e.cartFactory,this.cartItemFactory=e.cartItemFactory,this.addressFactory=e.addressFactory,this.buyer=e.buyer||null,this.selectedAddress=e.selectedAddress||null,this.preSetCountry=!1!==e.preSetCountry,this.buyerTypeLocked=!0===e.buyerTypeLocked,this.countryReadOnly=!0===e.countryReadOnly,this.isBillingAddress=!0===e.isBillingAddress,this.showSelectButton=!1!==e.showSelectButton,this.showPhoneNumber=!0===e.showPhoneNumber&&!this.isBillingAddress,this.isCreationAllowed=this._isCreationAllowed(e),this.buyerType=c.fromModel(this.buyer),this.authorized=this._isAuthorizedToPatchCompany(),this.stateMachine=this._createStateMachine(),this.alertMessages=this._createAlertMessagesContainer(),this.listPageLink=this._createAddressListLink(),this.creationPageLink=this._createAddressCreationLink(),this.footer=this._createFooter(),e.className&&this.container.addClassName(e.className)},render:function(){return this.buyerAddressForm=this._createBuyerAddressForm(),this.addressList=this._createAddressList(),this.pages={list:this._renderAddressListPage(),creation:this._renderAddressCreationPage()},this.modal=this._createModal(this.pages,this.footer),this.stateMachine.getState()===C.CREATION?(this.footer[0].setValue(m.get("save")),this.footer[0].show(),this.pages.creation.removeClassName("hidden"),this.pages.list.addClassName("hidden")):(this.footer[0].setValue(m.get("select")),this._adaptSelectButtonState(),this.pages.creation.addClassName("hidden"),this.pages.list.removeClassName("hidden"),this.showSelectButton||this.footer[0].hide()),this.container.setContent(this.modal),this},show:function(){if(this.modal){var e=this.model.filter(function(e){return!e.isNew()}).length>0,t=this.isCreationAllowed&&!e?C.CREATION:C.LIST;this._changeActivePage(t),this.modal.show()}},hide:function(){this.stateMachine.getState()!==C.SAVING&&this.modal&&this.modal.hide()},saveAndOrSelect:function(){var e,t=this;this.stateMachine.getState()===C.CREATION?(this.footer[0].load(),this.footer[1].setDisabled(!0),this.stateMachine.changeState(C.SAVING),this.buyerAddressForm.save().then(function(){t.errorMessageContainer&&t.errorMessageContainer.empty(),t.buyer.get("addressLine0")||(e=void 0!==t.buyer.defaults.registrationId?t.buyer.get("name"):t.buyer.get("firstName")+" "+t.buyer.get("lastName"),t.buyerAddressForm.address.set("addressLine0",e)),t.selectedAddress=t.buyerAddressForm.address,t.model.add(t.buyerAddressForm.address),t.hide(),t.stateMachine.changeState(C.CREATION),t.render(),t.dispatchEvent("onSelected",{ok:!0,selectedAddress:t.selectedAddress})}).catch(function(e){var s;(t.stateMachine.changeState(C.CREATION),t.dispatchEvent("onSelected",{ok:!1}),Object.prototype.isPrototypeOf.call(u.prototype,e))||(s=e.toString()||m.get("saveAddressError"),t.alertMessages.add({className:"error",message:s}))}).finally(function(){t.footer[0].load(!1),t.footer[1].setDisabled(!1)})):(t.selectedAddress=t.addressList.getSelectedAddress(),t.dispatchEvent("onSelected",{ok:!0,selectedAddress:t.addressList.getSelectedAddress()}),t.hide())},getSelectedAddress:function(){return this.selectedAddress},destroy:function(){this.buyerAddressForm&&this.buyerAddressForm.destroy(),this.addressList&&this.addressList.destroy(),this.model=null,this.me=null,this.buyer=null,this.countries=null,this.addressFactory=null,this.companyFactory=null,this.cartFactory=null,this.cartItemFactory=null,this._parent(),this.container=null},_createFooter:function(){var e=this;return[new r({className:"primary",events:{onClick:function(){e.saveAndOrSelect()}},value:m.get("select")}),new r({value:m.get("cancel"),events:{onClick:function(){e.hide()}}})]},_renderAddressListPage:function(){var t;return(t=[]).push(this.creationPageLink),t.push(this.addressList.render()),e.createElement("div",{class:"mpf-page",html:t})},_renderAddressCreationPage:function(){var t;return(t=[]).push(this.listPageLink),t.push(this.buyerAddressForm.render()),e.createElement("div",{class:"mpf-page",html:t})},_createAddressCreationLink:function(){var t,s=this;return this.isCreationAllowed&&(t=e.createElement("a",{class:"mpf-link",html:[{tag:"span",class:"fonticon fonticon-plus"},{tag:"span",text:m.get("addAddress")}],href:"#",events:{click:function(){s._changeActivePage(C.CREATION)}}})),t},_createAddressListLink:function(){var t=this;return e.createElement("a",{class:"mpf-link",html:[{tag:"span",class:"fonticon fonticon-back"},{tag:"span",text:m.get("addressList")}],href:"#",events:{click:function(){t._changeActivePage(C.LIST)}}})},_changeActivePage:function(e){var t;e!==this.stateMachine.getState()&&Object.prototype.hasOwnProperty.call(this.pages,e)&&(this.stateMachine.changeState(e),this.stateMachine.getState()===C.CREATION?(t=this.isBillingAddress?m.get("enterBillingAddress"):m.get("enterShippingAddress"),this.footer[0].setValue(m.get("save")),this.footer[0].show(),this.pages.creation.removeClassName("hidden"),this.pages.list.addClassName("hidden")):(t=this.isBillingAddress?m.get("selectBillingAddress"):m.get("selectShippingAddress"),this.footer[0].setValue(m.get("select")),this.pages.creation.addClassName("hidden"),this.pages.list.removeClassName("hidden"),this.showSelectButton||this.footer[0].hide()),this.modal.getHeader().getElement("h4").setText(t))},_createBuyerAddressForm:function(){var e=this._getBuyerCountry(),t=this._createAddress();return this.preSetCountry&&e&&t.set(a.Fields.COUNTRY,e),new l({address:t,buyer:this.buyer,addressFactory:this.addressFactory,buyerTypeLocked:this.buyerTypeLocked,companyFactory:this.companyFactory,cartFactory:this.cartFactory,cartItemFactory:this.cartItemFactory,countryReadOnly:this.countryReadOnly,isBillingAddress:this.isBillingAddress,preSetCountry:this.preSetCountry,authorized:this.authorized,countries:this.countries,showPhoneNumber:this.showPhoneNumber})},_createAddress:function(){var e;return this.buyerType===c.INDIVIDUAL?e=this.addressFactory.createModel(d.Types.ME):this.buyerType===c.COMPANY&&(e=this.addressFactory.createModel(d.Types.COMPANY),this.buyer.getCountry()&&e.set(a.Fields.COUNTRY,this.buyer.getCountry())),e.set(a.Fields.IS_SHIP_TO,"TRUE"),e.set(a.Fields.IS_BILL_TO,"TRUE"),e},_getBuyerCountry:function(){var e=this.buyer.get("country");if(e)return e;var t=this.model.last();return t?t.getCountry():null},_createModal:function(t,r){var i,n,o,d=this.stateMachine.getState();return n=[this.alertMessages,t.creation,t.list],o=d===C.CREATION?this.isBillingAddress?m.get("enterBillingAddress"):m.get("enterShippingAddress"):this.showSelectButton?this.isBillingAddress?m.get("selectBillingAddress"):m.get("selectShippingAddress"):this.isBillingAddress?m.get("billingAddresses"):m.get("shippingAddresses"),i=new s({header:e.createElement("h4",{text:o}),body:n,footer:this.footer,closable:!1,events:{onHide:this._onModalHide.bind(this)}}),this.alertMessages.getContent().setStyle("visibility","visible"),i},_onModalHide:function(){this.buyerAddressForm.clear()},_createStateMachine:function(){var e;return e=this.isCreationAllowed&&this.model.isEmpty()?C.CREATION:C.LIST,new y({state:e,states:[C.CREATION,C.LIST,C.SAVING],transitions:[{from:C.LIST,to:C.CREATION},{from:C.CREATION,to:C.LIST},{from:C.CREATION,to:C.SAVING},{from:C.SAVING,to:C.CREATION}]})},_createAlertMessagesContainer:function(){return new i({closable:!0,visible:!0,autoHide:!1})},_createAddressList:function(){this.addressList&&this.stopListening(this.addressList);var e=new h({model:this.model,buyer:this.buyer,buyerType:this.buyerType,isBillingAddress:this.isBillingAddress,showPhoneNumber:this.showPhoneNumber,selectedAddress:this.selectedAddress,isSelectable:this.showSelectButton});return this.listenTo(e,h.Events.ADDRESS_SELECTED,this._adaptSelectButtonState.bind(this)),e},_isAuthorizedToPatchCompany:function(){var t=this,s=!0;if(this.buyerType===c.COMPANY){this.buyerTypeLocked=!0;var r=this.buyer.getCountry();this.isBillingAddress&&r&&(this.countryReadOnly=!0,this.preSetCountry=r),s=""===this.buyer.getEmpProjectName()||!e.is(this.me)||this.me.getRoles().some(function(e){return"Admin"===e.Role&&e.Project===t.buyer.getEmpProjectName()})}return s},_isCreationAllowed:function(e){return!(p.from(window.location.href).isEmp()&&this.isBillingAddress)||!0===e.isCreationAllowed},_adaptSelectButtonState:function(){this.stateMachine.getState()===C.LIST&&this.showSelectButton&&this.addressList&&this.footer[0].setDisabled(!this.addressList.getSelectedAddress())}})});