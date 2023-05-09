define("DS/MPFBankAccountConstraintsModel/BankAccountFieldConstraint",["UWA/Core","UWA/Class"],function(n,t){"use strict";var e=t.extend({init:function(n,t){this.required=n||!0,this.pattern=t||""}});return e.EMPTY=new e,e}),define("DS/MPFBankAccountConstraintsModel/BankAccountConstraintsDataProxy",["UWA/Core","DS/MPFDataProxy/DataProxy"],function(n,t){"use strict";return t.extend({init:function(n){this._parent(n,"/formats/bank-accounts")}})}),define("DS/MPFBankAccountConstraintsModel/BankAccountConstraintsModel",["UWA/Core","UWA/String","DS/MPFModel/MPFModel","DS/MPFBankAccountModel/BankAccountModel","DS/MPFConstraintModel/ConstraintModel","DS/MPFDataProxy/NoDataProxy","i18n!DS/MPFBankAccountConstraintsModel/assets/nls/BankAccountConstraintsModel"],function(n,t,e,r,i,o,s){"use strict";var a={country:null};return a[r.Fields.IBAN]=null,a[r.Fields.SWIFT]=null,a[r.Fields.BSB_NUMBER]=null,a[r.Fields.ABA_NUMBER]=null,a[r.Fields.BANK_NUMBER]=null,a[r.Fields.ACCOUNT_NUMBER]=null,a[r.Fields.TRANSIT_NUMBER]=null,e.extend({idAttribute:"country",defaults:a,parse:function(n){var t,e,i={};for(t in n)if(n.hasOwnProperty(t)){i.country=t,e=n[t],this._addIfOwnProperty(e,i,"iban",r.Fields.IBAN),this._addIfOwnProperty(e,i,"swift",r.Fields.SWIFT),this._addIfOwnProperty(e,i,"bsb_number",r.Fields.BSB_NUMBER),this._addIfOwnProperty(e,i,"aba_number",r.Fields.ABA_NUMBER),this._addIfOwnProperty(e,i,"bank_number",r.Fields.BANK_NUMBER),this._addIfOwnProperty(e,i,"account_number",r.Fields.ACCOUNT_NUMBER),this._addIfOwnProperty(e,i,"transit_number",r.Fields.TRANSIT_NUMBER);break}return i},getConstraintKeyNames:function(){var t=this;return this.keys().filter(function(e){return"country"!==e&&n.is(t.get(e))})},_addIfOwnProperty:function(n,e,r,a){if(a=a||r,n.hasOwnProperty(r)){var d={};d[i.Fields.ATTRIBUTE]=a,d[i.Fields.PATTERN]=n[r].pattern,d[i.Fields.REQUIRED]=n[r].required,d[i.Fields.REQUIRED_ERROR]=t.ucfirst(t.format(s.get("isRequired"),s.get(a))),d[i.Fields.INVALID_ERROR]=t.ucfirst(t.format(s.get("isInvalid"),s.get(a))),e[a]=new i(d,{dataProxy:o.getInstance()})}}})}),define("DS/MPFBankAccountConstraintsModel/BankAccountConstraintsCollection",["UWA/Core","DS/MPFModel/MPFCollection","DS/MPFBankAccountConstraintsModel/BankAccountConstraintsModel"],function(n,t,e){"use strict";return t.extend({model:e})});