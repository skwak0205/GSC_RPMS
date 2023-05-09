define("DS/RTVueUserPresenceAPI/RTVueUserPresenceAPI",["vuejs","vu-kit","DS/RTStore/RTStore","UWA/Data","DS/VueUserPresenceAPI/VueUserPresenceAPI"],function(e,r,s,t,n){"use strict";var u={name:"vu-rtuserpresence",components:{"vu-userpresence":n},props:{login:{type:String,required:!0},tenantId:{type:String,required:!0},swymUrl:{type:String,required:!1}},created(){this.swymUrl&&!s.store.getters.swymUrl&&s.store.dispatch("swymUrl",{swymUrl:this.swymUrl})},computed:{avatarUrl(){let e=s.store.getters.swymUrl+"/api/user/getpicture/login/"+this.login;return s.store.getters.isMobile?t.proxifyUrl(e,{proxy:"passport"}):e},presence(){var e=s.bridge.users(this.login,this.tenantId);return e&&(e.presence||e.status||"unknown").toLowerCase()}},template:'<vu-userpresence :presence="presence" :src="avatarUrl" />'},i=e.component("vu-rtuserpresence",u);return i.vue3cmp=u,i});