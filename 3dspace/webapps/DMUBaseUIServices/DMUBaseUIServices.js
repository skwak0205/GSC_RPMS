define("DS/DMUBaseUIServices/DMUSlidePreferences",["DS/DMUBaseExperience/DMUContextManager"],function(e){"use strict";let t=[];return Object.freeze({getSlidePreferences:e=>{if(e&&e.context)for(let n=0;n<t.length;n++)if(t[n].context===e.context)return t[n].preferences},setSlidePreferences:n=>{if(n&&n.context&&n.preferences){let r=!0;for(let e=0;e<t.length;e++)if(t[e].context===n.context){let i=Object.keys(n.preferences);for(let r=0;r<i.length;r++)t[e].preferences[i[r]]=n.preferences[i[r]];r=!1}r&&t.push({context:n.context,preferences:n.preferences});let i=e.giveEventsController({viewer:n.context&&n.context.getViewer?n.context.getViewer():null});i&&i.fireEvent("onDMUSlidePreferencesChanged")}},removeSlidePreferences:e=>{if(e&&e.context)for(let n=0;n<t.length;n++)if(t[n].context===e.context){t.splice(n,1);break}}})}),define("DS/DMUBaseUIServices/DMUObjectsServices",["require","exports","DS/PADUtils/PADContext","DS/Visualization/ThreeJS_DS","DS/DMUBaseExperience/EXPManagerSet","DS/DMUBaseReview/DMULinksManager"],function(e,t,n,r,i,a){"use strict";const o={0:{px:1,mm:.13},1:{px:2,mm:.35},2:{px:3,mm:.7},3:{px:4,mm:1},4:{px:5,mm:1.4},5:{px:6,mm:2},6:{px:7,mm:2.3},7:{px:8,mm:2.6},8:{px:16,mm:4},9:{px:32,mm:9},10:{px:64,mm:12}};class s{static getAbsolutePosition(e,t,r,o,s){let l="string"==typeof s?s:e.getAttribute("fPointedPageNumber"),c="number"==typeof s?s:e.getAttribute("sPointedElementID");if(l||c){e.setAlertFlag(!1);let t=i.getManager({name:"DMU2DSheetManager",context:n.get().getFrameWindow()});if(t){let e=t.getElementMatrix(l||c);if(e)return r.clone().applyMatrix4(e)}return e.setAlertFlag(!0),r}if(void 0!==o&&t){let n=e.getParent();if(n){let e=n.getAttribute("oLinksManager");if(e instanceof a){let n=e.getOccurrencePathElement(o);if(n)return t.clone().applyMatrix4(n.getWorldMatrix())}}}return r}static getRelativePosition(e,t){let a=e.getAttribute("fPointedPageNumber"),o=e.getAttribute("sPointedElementID");if(!a&&!o)return t;e.setAlertFlag(!1);let s=i.getManager({name:"DMU2DSheetManager",context:n.get().getFrameWindow()});if(!s||!s.isADocumentDisplayed())return t;let l=s.getElementMatrix(a||o);return l?t.clone().applyMatrix4((new r.Matrix4).getInverse(l)):(e.setAlertFlag(!0),t)}static getAbsoluteRotation(e,t){let r=e.getAttribute("fPointedPageNumber");if(!r)return t;let a=i.getManager({name:"DMU2DSheetManager",context:n.get().getFrameWindow()});if(!a||!a.isADocumentDisplayed())return t;let o=a.getPageRotation(r);return o?o+t:t}static getRelativeRotation(e,t){let r=e.getAttribute("fPointedPageNumber");if(!r)return t;let a=i.getManager({name:"DMU2DSheetManager",context:n.get().getFrameWindow()});if(!a||!a.isADocumentDisplayed())return t;let o=a.getPageRotation(r);return o?t-o:t}static getAbsolutePositionMatrix(e,t,n){if(void 0===n||!t)return;let r=e.getParent();if(r){let e=r.getAttribute("oLinksManager");if(e){let r=e.getOccurrencePathElement(n);if(r)return r.getWorldMatrix().clone().multiply(t)}}}static getPOIInformationFromPathElement(e){const t=n.get().getController().getPOIPosition(e);return{position:t?new r.Vector3(t.x,t.y,t.z):void 0}}static getLineThickness(e,t="px"){return e in o?o[e][t]:void 0}static convertLineThickness(e,t){let n=s.getLineThicknessIndex(e,"mm"===t?"px":"mm");return n&&n in o?o[n][t]:void 0}static getLineThicknessIndex(e,t="px"){let n=Object.keys(o).find(n=>{if(o[parseInt(n)][t]===e)return!0});return n?parseInt(n):void 0}}return s});