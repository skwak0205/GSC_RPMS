define("DS/DMUPlayMarkup/DMUMarkupImport",["UWA/Class"],function(e){"use strict";return e.extend({init:function(e){this.importData=function(t,r){t&&r&&e&&(e.isImporting(!0),r.fillObjectAttributes(e,t,e),r.registerForPostImport(e))},this.afterImport=function(){e&&(e.fireEvent("onDMUObjectInitialized",{dmuObject:e}),setTimeout(function(){e.isImporting(!1),e.fireEvent("onDMUMarkupLoaded",{dmuObject:e})},10))}}})}),define("DS/DMUPlayMarkup/DMUMarkup",["require","exports","DS/DMUBaseExperience/DMUContextManager","DS/DMUPlayMarkup/DMUMarkupImport","DS/DMUBaseReview/DMUOccurrenceOverloader","DS/Visualization/Node3D","UWA/Utils","DS/DMUBaseExperience/DMUObject"],function(e,t,r,n,i,l,o,s){"use strict";let a=[{name:"sID",type:"string",JSONname:"id"},{name:"sType",type:"string",JSONname:"type",defaultValue:"Markup"},{name:"sName",type:"string",JSONname:"Name",defaultValue:"Markup"},{name:"sDescription",type:"string",JSONname:"Description",defaultValue:""},{name:"sCurrentSlideId",type:"string",JSONname:"CurrentSlideId"},{name:"lSlides",type:"listObjects",JSONname:"Slides",defaultValue:[]},{name:"lComments",type:"listObjects",JSONname:"Comments",defaultValue:[]},{name:"lFormats",type:"listObjects",JSONname:"Formats",defaultValue:[]},{name:"lOccOverloaders",type:"listObjects",JSONname:"OccurenceOverloaders",defaultValue:[]},{name:"oLinksManager",type:"object",JSONname:"LinksManager",defaultValue:null}];return class extends s{constructor(e,t){super(e,t),this.addParameters(a);let s,u,c,d=this,g=null,m=null,f=!1,p=!1,h=new l;function O(e,t,r,n){e.getType()===t&&(d.set({attribute:r,value:e,mode:"addValue",index:n}),e.getNode()&&d.getNode().addChild(e.getNode()))}function D(e,t,r){if(e.getType()===t){let t=d.getAttribute(r);if(t){for(let r=0;r<t.length;r++)if(t[r]===e){t[r].getNode()&&d.getNode().removeChild(t[r].getNode()),t.splice(r,1);break}d.setAttribute(r,t),e.remove()}}}function M(e){let t=[],r=d.getAttribute(e);if(r)for(let e=0;e<r.length;e++)t.push(r[e]);return t}h.getDMUType=function(){return"DMUMarkup"},this.setNode(h);let b=this.setAttribute;this.setAttribute=function(e,t){let r=t;if(f)if(("lSlides"===e||"lFormats"===e)&&r&&Array.isArray(r))for(let e=r.length-1;e>=0;e--)r[e]&&r[e].getNode?r[e].getNode()&&d.getNode().addChild(r[e].getNode()):r.splice(e,1);else"oLinksManager"===e&&(s&&s.remove(),s=r);b(e,r)},this.addSlide=function(e,t){O(e,"DMUSlide","lSlides",t)},this.addFormat=function(e,t){O(e,"DMUFormat","lFormats",t)},this.addComment=function(e,t){O(e,"DMUComment","lComments",t)},this.addOccurrenceOverloader=function(e){O(e,"DMUOccurrenceOverloader","lOccOverloaders")},this.getSlides=function(){return M("lSlides")},this.getFormats=function(){return M("lFormats")},this.getComments=function(){return M("lComments")},this.getOccurrenceOverloaders=function(){return M("lOccOverloaders")},this.removeSlide=function(e){e===g&&(g=null),D(e,"DMUSlide","lSlides")},this.removeFormat=function(e){e===m&&(m=null);let t,r=e.getAttribute("sID")||"",n=d.getSlides();for(let e=0;e<n.length;e++)(t=n[e].getAttribute("sPointedFormats"))&&-1!==t.indexOf(r)&&n[e].setAttribute("sPointedFormats",[]);D(e,"DMUFormat","lFormats")},this.removeComment=function(e){D(e,"DMUComment","lComments")},this.removeOccurrenceOverloader=function(e){D(e,"DMUOccurrenceOverloader","lOccOverloaders")},this.setDirtyFlag=function(e){d.isImporting()||(p=e)},this.isDirty=function(){return p};let v=this.setVisibleFlag;this.setVisibleFlag=function(e,t){if(e!==d.isVisible()||t){let r=d.getCurrentSlide();r&&r.setVisibleFlag(e);let n=d.getOccurrenceOverloaders();for(let t=0;t<n.length;t++)n[t].setVisibleFlag(e);v(e,t)}};let S=this.setActiveFlag;this.setActiveFlag=function(e){if(e!==d.getActiveFlag()){let t,r=d.getOccurrenceOverloaders();for(t=0;t<r.length;t++)r[t].setActiveFlag(e);for(r=d.getSlides(),t=0;t<r.length;t++)r[t].setActiveFlag(e);for(r=d.getFormats(),t=0;t<r.length;t++)r[t].setActiveFlag(e);S(e)}};let C,A=0;this.computeNewID=function(){let e,t,r=[d],n=d.getSlides();for(r=r.concat(n),e=0;e<n.length;e++)r=(r=r.concat(n[e].getDMUObjects())).concat(n[e].getDMUObjects("DMUComment"));for(n=d.getFormats(),r=r.concat(n),e=0;e<n.length;e++)r=r.concat(n[e].getDMUObjects());for(r=r.concat(d.getOccurrenceOverloaders()),e=0;e<r.length;e++)if(r[e]){let n=r[e].getAttribute("sID");(t=parseFloat("string"==typeof n?n:""))>A&&(A=t)}if(s)for(;s.getChildWithID(A.toString());)A++;return(++A).toString()},this.isImporting=function(e){return"boolean"==typeof e&&f!==e&&((f=e)||d.refreshNode(),d.fireEvent(f?"onDMUMarkupImportStarts":"onDMUMarkupImportEnds",{dmuObject:d})),f},this.initAuthoringData=function(){if(-1!==r.getAuthoringFeatureTypes().indexOf("Markup")){let e="DS/DMUPersistence/DMUMarkupContextualBar";window.require([e],function(e){C=new e,d.getContextualMenuCommandList=C.getContextualMenuCommandList,d.getSharedContextualMenuCommandList=C.getSharedContextualMenuCommandList,C.register({frmWindow:d.getFrameWindow(),type:d.getType()})})}},this.removeAuthoringData=function(){d.getSharedContextualMenuCommandList=null,d.getContextualMenuCommandList=null,C&&C.unregister({frmWindow:d.getFrameWindow(),type:d.getType()})};let F=new n(d);this.importData=F.importData,this.afterImport=F.afterImport,this.initAuthoringData();let N=this.refreshNode;this.refreshNode=function(){if(d.getNode()&&!d.isImporting()){d.getNode().setName(d.getAttribute("sName"));let e,t=d.getSlides();for(e=0;e<t.length;e++)t[e].refreshNode();for(t=d.getFormats(),e=0;e<t.length;e++)t[e].refreshNode();for(t=d.getOccurrenceOverloaders(),e=0;e<t.length;e++)t[e].refreshNode();N()}},this.computeNewName=function(e){if(!e||!e.object)return"";let t="parenthesis"===e.separator?"(":".",r="parenthesis"===e.separator?")":"",n=1,i=e.prefix+(e.noSuffixIfPossible?"":t+n+r),l=function(l){let o,s,a=d.getAttribute(l)||[],u=[e.object.getAttribute("sName")];for(o=0;o<a.length;o++)if(e.object.getType()===a[o].getType())u.push(a[o].getAttribute("sName"));else if(a[o].getDMUObjects){let t=a[o].getDMUObjects(e.object.getType());for(s=0;s<t.length;s++)u.push(t[s].getAttribute("sName"))}for(;-1!==u.indexOf(i);)n++,i=e.prefix+t+n+r};return l("lSlides"),l("lFormats"),l("lOccOverloaders"),i},this.getDMUObjectFromPathElement=function(e){if(!e)return;let t,r=e.getLastElement(!0);if(d.getNode()===r)return d;let n=function(t){let n,i=d.getAttribute(t)||[];for(let t=0;t<i.length;t++){if(i[t].getNode&&i[t].getNode()===r)return i[t];if(i[t].getDMUObjectFromPathElement&&(n=i[t].getDMUObjectFromPathElement(e)))return n}};return(t=n("lSlides"))?t:(t=n("lFormats"))?t:(t=n("lComments"))?t:t=n("lOccOverloaders")};let U=this.setReadOnly;this.setReadOnly=function(e){let t=e||Boolean(u&&!u.isWebMarkup()),r=d.getAttribute("lSlides")||[];r=(r=(r=r.concat(d.getAttribute("lFormats")||[])).concat(d.getAttribute("lComments")||[])).concat(d.getAttribute("lOccOverloaders")||[]);for(let e=0;e<r.length;e++)r[e].setReadOnly(t);s&&s.setReadOnly(t),U(t)},this.getOccurence=function(e,t){let r=d.getAttribute("oLinksManager");if(r)return r.getOccurrence(e,t)},this.getOrCreateOccurrenceOverloader=function(t,r){let n,l=d.getOccurence(t,r);if(l){let s=d.getAttribute("lOccOverloaders")||[];for(let e=0;!n&&e<s.length;e++)if(s[e].isOverloading(t)){n=s[e];break}!n&&r&&((n=new i(e,d)).setAttributes([{name:"sName",value:d.computeNewName({object:n,prefix:"DMUOccurrenceOverloader"})},{name:"sID",value:d.computeNewID()},{name:"sUuid",value:o.getUUID()}]),n.setOccurrence(l),d.addOccurrenceOverloader(n))}return n},this.removeOccurrenceOverloaderIfNeeded=function(e){if(e){let t=!0,r=d.getAttribute("lSlides")||[];r=r.concat(d.getAttribute("lFormats")||[]);for(let n=0;n<r.length;n++)if(r[n].getOverloadForTarget(e)){t=!1;break}t&&d.removeOccurrenceOverloader(e)}};let y=this.remove;this.remove=function(){d.setActiveFlag(!1),s=g=u=null,y(),d.getApplicativeContext()&&d.getApplicativeContext().PIM&&d.fireEvent("onDMUApplicativeContextClean",{applicativeContext:d.getApplicativeContext().PIM}),c=null},this.setCurrentSlide=function(e){if(e){let t=g,r=m;t&&t.isDisplayed()&&t.setDisplayedFlag(!1),r&&r.isDisplayed()&&r.setDisplayedFlag(!1),"DMUSlide"===e.getType()?((g=e)&&g.setDisplayedFlag(!0),d.setAttribute("sCurrentSlideId",e.getAttribute("sID"))):(m=e)&&m.setDisplayedFlag(!0)}else g&&(g.setDisplayedFlag(!1),g=null,d.setAttribute("sCurrentSlideId","")),m&&(m.setDisplayedFlag(!1),m=null);d.fireEvent("onDMUCurrentSlideChanged",{dmuObject:d})},this.getCurrentSlide=function(){let e=d.getParent().getUIManager(),t=!!e&&e.getFormatEditionModeFlag();return!t&&g&&g.isDisplayed()?g:t&&m&&m.isDisplayed()?m:null},this.setMarkupOwner=function(e){u=e},this.getMarkupOwner=function(){return u},this.setApplicativeContext=(e=>{c=e}),this.getApplicativeContext=(()=>c)}}});