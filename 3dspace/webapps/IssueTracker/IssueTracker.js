define("DS/IssueTracker/IssueValue",[],function(){"use strict";return{}}),define("DS/IssueTracker/IssueDimension",[],function(){"use strict";var e;return e={ISSUEMANAGEMENT_CREATE_ISSUE:{ISSUEMANAGEMENT_CREATEISSUE:"issueManagementCreateIssue",ISSUEMANAGEMENT_CREATEISSUE_APPROVAL_ACTIVATE:"issueManagementCreateIssueApprovalActivate",ISSUEMANAGEMENT_CREATEISSUE_CLASSIFICATION:"issueManagementCreateIssueClassification",ISSUEMANAGEMENT_CREATEISSUE_CONTAINS_CONTEXTS:"issueManagementCreateIssueContainsContexts",ISSUEMANAGEMENT_CREATEISSUE_CONTAINS_AFFECTEDITEMS:"issueManagementCreateIssueContainsAffectedItems",ISSUEMANAGEMENT_CREATEISSUE_CONTAINS_ATTACHMENTS:"issueManagementCreateIssueContainsAttachments",ISSUEMANAGEMENT_CREATEISSUE_CONTAINS_COOWNER:"issueManagementCreateIssueContainsCoowner",ISSUEMANAGEMENT_CREATEISSUE_CONTAINS_ASSIGNEE:"issueManagementCreateIssueContainsAssignee",ISSUE_CUSTOM_TYPE_OOTB:"issueCustomTypeOOTB",ISSUEMANAGEMENT_CREATEISSUE_CONTAINS_CONTRIBUTORS:"issueManagementCreateIssueContainsContributors",ISSUEMANAGEMENT_CREATION_ROLE:"issueManagementCreationRole",ISSUEMANAGEMENT_CREATEISSUE_STATUS_AT_CREATION:"issueManagementCreateIssueStatusAtCreation",ISSUEMANAGEMENT_MEDIA:"issueManagementMedia",ISSUEMANAGEMENT_CREATEISSUE_CONTAINS_INFORMEDUSERS:"issueManagementCreateIssueContainsInformedUsers"}},Object.freeze(e)}),define("DS/IssueTracker/IssueDimensionMapping",["DS/IssueTracker/IssueDimension"],function(e){"use strict";var t={};return t[e.ISSUEMANAGEMENT_CREATE_ISSUE.ISSUEMANAGEMENT_CREATEISSUE]={PersonalDimention:"pd1",allowedValues:["3D Toolbar","Geometry","Inline","New Issue","New Issue from Template","Duplicate","Advanced Duplicate"]},t[e.ISSUEMANAGEMENT_CREATE_ISSUE.ISSUEMANAGEMENT_CREATEISSUE_APPROVAL_ACTIVATE]={PersonalDimention:"pd2",allowedValues:["TRUE","FALSE"]},t[e.ISSUEMANAGEMENT_CREATE_ISSUE.ISSUEMANAGEMENT_CREATEISSUE_CLASSIFICATION]={PersonalDimention:"pd3",allowedValues:["TRUE","FALSE"]},t[e.ISSUEMANAGEMENT_CREATE_ISSUE.ISSUEMANAGEMENT_CREATEISSUE_CONTAINS_CONTEXTS]={PersonalDimention:"pd4",allowedValues:["TRUE","FALSE"]},t[e.ISSUEMANAGEMENT_CREATE_ISSUE.ISSUEMANAGEMENT_CREATEISSUE_CONTAINS_AFFECTEDITEMS]={PersonalDimention:"pd5",allowedValues:["TRUE","FALSE"]},t[e.ISSUEMANAGEMENT_CREATE_ISSUE.ISSUEMANAGEMENT_CREATEISSUE_CONTAINS_ATTACHMENTS]={PersonalDimention:"pd6",allowedValues:["TRUE","FALSE"]},t[e.ISSUEMANAGEMENT_CREATE_ISSUE.ISSUEMANAGEMENT_CREATEISSUE_CONTAINS_COOWNER]={PersonalDimention:"pd7",allowedValues:["TRUE","FALSE"]},t[e.ISSUEMANAGEMENT_CREATE_ISSUE.ISSUEMANAGEMENT_CREATEISSUE_CONTAINS_ASSIGNEE]={PersonalDimention:"pd8",allowedValues:["TRUE","FALSE"]},t[e.ISSUEMANAGEMENT_CREATE_ISSUE.ISSUE_CUSTOM_TYPE_OOTB]={PersonalDimention:"pd9",allowedValues:["TRUE","FALSE"]},t[e.ISSUEMANAGEMENT_CREATE_ISSUE.ISSUEMANAGEMENT_CREATEISSUE_CONTAINS_CONTRIBUTORS]={PersonalDimention:"pd10",allowedValues:["TRUE","FALSE"]},t[e.ISSUEMANAGEMENT_CREATE_ISSUE.ISSUEMANAGEMENT_CREATION_ROLE]={PersonalDimention:"pd11",allowedValues:["Leader","Author","Contributor","Reader"]},t[e.ISSUEMANAGEMENT_CREATE_ISSUE.ISSUEMANAGEMENT_CREATEISSUE_STATUS_AT_CREATION]={PersonalDimention:"pd12",allowedValues:["Save As Draft","Start"]},t[e.ISSUEMANAGEMENT_CREATE_ISSUE.ISSUEMANAGEMENT_MEDIA]={PersonalDimention:"pd13",allowedValues:["Desktop","Mobile","Tab"]},t[e.ISSUEMANAGEMENT_CREATE_ISSUE.ISSUEMANAGEMENT_CREATEISSUE_CONTAINS_INFORMEDUSERS]={PersonalDimention:"pd14",allowedValues:["TRUE","FALSE"]},Object.freeze(t)}),define("DS/IssueTracker/IssueDimensionBuilder",["DS/IssueTracker/IssueDimensionMapping"],function(e){"use strict";function t(e){return"string"==typeof e&&e.length>0}function n(){this.dimensions={}}return n.prototype.add=function(n,s){var E;return E=t(n)&&Object.keys(e).some(function(e){return e===n}),t(s)&&E&&(0==e[n].allowedValues.length||e[n].allowedValues.includes(s))&&(this.dimensions[e[n].PersonalDimention]=s),this},n.prototype.build=function(){return this.dimensions},{create:function(){return new n}}}),define("DS/IssueTracker/IssueValueMapping",["DS/IssueTracker/IssueValue"],function(e){"use strict";return{}}),define("DS/IssueTracker/IssueValueBuilder",["DS/IssueTracker/IssueValueMapping"],function(e){}),define("DS/IssueTracker/IssuePageBuilder",["DS/Usage/TrackerAPI","DS/IssueTracker/IssueDimensionBuilder"],function(e,t){"use strict";function n(e){this.url=e.url,this.title=void 0,this.language=void 0,this.appId=void 0,this.tenant=e.tenant,this.scope="Dashboard",this.dimensionsBuilder=t.create()}return n.prototype.setTitle=function(e){return this.title=e,this},n.prototype.setLanguage=function(e){return this.language=e,this},n.prototype.setAppId=function(e){return this.appId=e,this},n.prototype.setTenant=function(e){return this.tenant=e,this},n.prototype.addDimension=function(e,t){return this.dimensionsBuilder.add(e,t),this},n.prototype.addPersonalValue=function(e,t){return this},n.prototype.send=function(){const t=this.dimensionsBuilder.build();e.trackPageView({pageURL:this.url,pageTitle:this.title,pageLanguage:this.language,appID:this.appId,tenant:this.tenant,scope:this.scope,persDim:t})},{create:function(e){return new n(e)}}}),define("DS/IssueTracker/IssueEventBuilder",["DS/Usage/TrackerAPI","DS/IssueTracker/IssueDimensionBuilder"],function(e,t){"use strict";function n(e){this.category=e.category,this.action=e.action,this.tenant=e.tenant,this.scope="Dashboard",this.dimensionsBuilder=t.create()}return n.prototype.setLabel=function(e){return this.label=e,this},n.prototype.setAppId=function(e){return"string"==typeof e&&(this.appId=e),this},n.prototype.setTenant=function(e){return"string"==typeof e&&(this.tenant=e),this},n.prototype.addDimension=function(e,t){return this.dimensionsBuilder.add(e,t),this},n.prototype.getDimensions=function(){return this.dimensionsBuilder.build()},n.prototype.getValues=function(){},n.prototype.addPersonalValue=function(e,t){return this},n.prototype.send=function(){const t=this.dimensionsBuilder.build();e.trackPageEvent({eventCategory:this.category,eventAction:this.action,eventLabel:this.label,scope:this.scope,persDim:t,appID:this.appId}),console.log("Cloud Probes for "+this.label+" scenario. Personal Dimensions = "+JSON.stringify(t)+" Personal Values = ")},{create:function(e){return new n(e)}}}),define("DS/IssueTracker/IssueTracker",["DS/IssueTracker/IssueEventBuilder","DS/IssueTracker/IssuePageBuilder","DS/IssueTracker/IssueDimension","DS/IssueTracker/IssueDimensionMapping"],function(e,t,n,s){"use strict";return{createEventBuilder:function(t){return e.create(t)},createPageBuilder:function(e){return t.create(e)},createTracker:function(e){const t=this,n={category:t.Category.USAGE,action:t.Events.CLICK,tenant:""};if(Object.prototype.hasOwnProperty.call(e,"event")&&(Object.prototype.hasOwnProperty.call(e.event,"tenant")&&(n.tenant=e.event.tenant),Object.prototype.hasOwnProperty.call(e.event,"category")&&Object.prototype.hasOwnProperty.call(t.Category,e.event.category)&&(n.category=t.Category[e.event.Category]),Object.prototype.hasOwnProperty.call(e.event,"action")&&Object.prototype.hasOwnProperty.call(t.Events,e.event.action)&&(n.action=t.Events[e.event.Category])),console.error("Event information are not present"),""!=n.tenant){const s=t.createEventBuilder(n);Object.prototype.hasOwnProperty.call(e,"tracker")?(Object.prototype.hasOwnProperty.call(e.tracker,"label")&&(Object.prototype.hasOwnProperty.call(t.Labels,e.tracker.label)?s.setLabel(e.tracker.label):console.error(e.tracker.label||"Label is not set nor define in IssueTracker.Labels.")),Object.prototype.hasOwnProperty.call(e.tracker,"appID")?s.setAppId(e.tracker.appID):s.setAppId("NO_APP_ID"),s.addDimension("","")):console.error("Tracker information are not present")}else console.error("Tenant is missing")},gpasdidee:function(e){},getDeviceUsedForOperation:function(){var e=this.DeviceUsed.DESKTOP,t="ontouchstart"in document.documentElement&&/mobi/i.test(navigator.userAgent),n=window.matchMedia("(max-width: 767px)");return t?e=this.DeviceUsed.MOBILE:n.matches&&(e=this.DeviceUsed.TAB),e},Category:{USAGE:"ISU_CLOUD_PROBES_USAGE",PERFORMANCE:"ISU_CLOUD_PROBES_PERFORMANCE"},DeviceUsed:{DESKTOP:"Desktop",MOBILE:"Mobile",TAB:"Tab"},IssueCreation:{TOOLBAR:"3D Toolbar",GEOMETRY:"Geometry",INLINE:"Inline",NEWISSUE:"New Issue",NEWFROMTEMPLATE:"New Issue from Template",DUPLICATE:"Duplicate",ADVANCEDUPLICATE:"Advanced Duplicate"},IssueCreationStatus:{START:"Start",SAVEASDRAFT:"Save As Draft"},Events:{CLICK:"Click"},Availability:{TRUE:"TRUE",FALSE:"FALSE"},Labels:{ISSUEMANAGEMENT_CREATE_ISSUE:"IssueManagement Create Issue"}}});