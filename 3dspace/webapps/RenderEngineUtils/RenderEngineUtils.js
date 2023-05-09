define("DS/RenderEngineUtils/StateSortingCacheItem",["DS/Mesh/ThreeJS_Base"],function(e){"use strict";var i=function({dlIndex:e,resetFrameIndex:i,material:t,object:r,geometry:s,dg:a,dgInterval:n,renderMode:o,renderVolumesOnly:l,renderLineMode:d,geomDisposeFunc:p,materialToUse:c,matApp:y,gasMaterial:u,cpInfos:h=null}){this._args={material:t,object:r,geometry:s,dg:a,dgInterval:n,renderMode:o,renderVolumesOnly:l,geomDisposeFunc:p,materialToUse:c,matApp:y,gasMaterial:u,cpInfos:h},this.dlIndex=e,this.resetFrameIndex=i,this.renderLineMode=d};return i.prototype.addDrawable=function(e,i){var t=e[this.dlIndex],r={qaAutomationMode:i};Object.assign(r,this._args),t.addDrawable(r)},i}),define("DS/RenderEngineUtils/InheritanceSolver",["DS/Mesh/ThreeJS_Base"],function(e){"use strict";const i=e._VisuFilterType;let t=null,r=0,s=0,a=0,n=0,o=null,l=null,d=null;function p(e,i){let t=e;return i.glType!=a&&i.glType!=n||!e.line||(t=e.line),i.glType==s&&e.point&&(t=e.point),t._deferredMaterialsInit||t.updateDeferredMaterials(),t}function c(e){if("true"===localStorage.getItem("forceCPUOutlines"))return e.isOrtho;return e._viewpoint&&e._viewpoint._2DMode&&e.isOrtho}function y(e,i){return e._viewpoint&&e._viewpoint.isNative2D()&&e._viewpoint._depthMode?i._depthPriority:-1}let u=null;return class{constructor(e){this.renderer=e,this.gl=null,this.reset(),this._objectsWithOutlines=null,this.camera=null}_useCPUOutlines(e){return c(e)}enableOutlineHanding(i,t){if(!this.camera)throw"Globals are not set, camera not found";if(this.outlineStruct=null,i){const i=this.camera;this.outlineStruct=t;let r=null;c(i)&&(r=new e.Vector4(0,0,1,0).applyMatrix4(i.matrixWorld)),t.outlinesCameraDirection=r;const s=this._objectsWithOutlines&&this._objectsWithOutlines.cameraDir,a=!!r!=!!s||r&&!r.equals(s);this._objectsWithOutlines&&a&&(this._objectsWithOutlines.cameraDir=r,this._objectsWithOutlines.objects.forEach(e=>e._flagAsGSSOInvalidated()),this._objectsWithOutlines.objects.clear())}}setContext(i,t,p){this.gl=i,r=e.GeomTypeEnum.FACE,s=i.POINTS,a=i.LINES,n=i.LINE_STRIP,o=t,l=p,d||(d=new e.MeshBasicMaterial)}setGlobals(i,t,r,s,a,n,o,l){this.getDLFromObject=i,this.onGeometryDisposeDS=t,this.camera=r,this.defaultAppearanceType=s,this.QAAutomationMode=s===e.DefaultAppearanceMode.__QA_AUTOMATION__,this.stateSortingResultID=n,this.enableGSSOCache=a,this.zOnlyPass=o,this.pickingPass=l}reset(){this.getDLFromObject=null,this.onGeometryDisposeDS=null,this.QAAutomationMode=!1,this.defaultAppearanceType=-1,this.stateSortingResultID=-1,this.enableGSSOCache=!1,this.outlineStruct=null,this.object=null,this.cappingMaterial=null,this.zOnlyPass=!1,this.pickingPass=!1}_registerObjectWithOutlines(){this._objectsWithOutlines||(this._objectsWithOutlines={cameraDir:this.outlineStruct.outlinesCameraDirection,objects:new Set}),this._objectsWithOutlines.objects.add(this.object)}_handleOutlines(i){if(!this.outlineStruct)return;const t=this.object,r=this.renderer,s=this.outlineStruct,a=(this.getDLFromObject,this.onGeometryDisposeDS);if(t.fromLoadedModel&&i&&i.getMask()&e.OutlineMask)if(t.geometry&&t.geometry.length&&t.geometry[0]&&t.geometry[0].vertexIndexArray){let n=null;const l=s.outlinesCameraDirection;l&&((n=l.clone()).applyMatrix4((new e.Matrix4).getInverse(t.matrixWorld)),n=new e.Vector3(n.x,n.y,n.z).normalize());let{outlineBG:d,created:p,invalidateGSSO:c}=o.prepareOutlines({object:t,width:i._outlineWidth,viewDirection:n,_sortCount:s.sortCount,_gl:this.gl,visibleSpace:r.visibleSpace,showHiddenOutlines:1===i._hiddenEdges});d.length>0&&(this._registerObjectWithOutlines(),c&&s.objectWithTransientGSSO.add(t),p&&(s.outlinesCreated=!0));for(let e=0;e<d.length;e++){const t=d[e];p&&t.addEventListener("dispose",a);const r=t.drawingGroups[0];this._addDGToDL({geometry:t,dg:r,dgInterval:r,material:r.material,originalMaterial:r.material,renderMode:i})}}else r._NoMeshInRamWarningCount<10&&(console.warn("OutlineBuilding unavailable, make sure noMeshInRAM is inactive"),r._NoMeshInRamWarningCount++)}_handleSectionLines(i){let t=null;if(i&&i._maskWithDefault&e.SectionLineMask){const e=this.object,r=this.renderer,s=(this.getDLFromObject,this.onGeometryDisposeDS);if(e.geometry&&e.geometry.length&&e.geometry[0]&&e.geometry[0].vertexIndexArray){let a=e.sectionLinesBuilder&&e.sectionLinesBuilder.sectionLineGeometry;const n=e.occurrenceRenderingOverride&&e.occurrenceRenderingOverride.sectionLinesProperties,o=n?n.color:null,d=n?n.width:1,p=e.occurrenceRenderingOverride&&e.occurrenceRenderingOverride.clipPlanes;let c=null;e.occurrenceRenderingOverride&&e.occurrenceRenderingOverride.clippingContext&&(c=e.occurrenceRenderingOverride.clippingContext._getSectionLinesArray());const y=a&&e.layer&&!e.layer.upToDate,u=!y&&e.sectionLinesBuilder&&e.sectionLinesBuilder.update({width:d,color:o,clipPlanes:p,sectionLines:c});if(!y&&u||(e.sectionLinesBuilder&&e.sectionLinesBuilder.dispose(),e.sectionLinesBuilder=null,a=null,e.layer&&(e.layer.upToDate=!0)),!a&&!e.sectionLinesBuilder){const i=new l({renderable:e,renderer:r,lineWidth:d,color:o,clipPlanes:p,sectionLines:c});i.computeSectionLineGeometry(),(a=i.sectionLineGeometry)&&a.addEventListener("dispose",s),e.sectionLinesBuilder=i}if(a){const e=a.drawingGroups[0];null===o&&(t=e.material),this._addDGToDL({geometry:a,dg:e,dgInterval:e,material:e.material,originalMaterial:e.material,renderMode:i})}}else r._NoMeshInRamWarningCount<10&&(console.warn("SectionLineBuilding unavailable, make sure noMeshInRAM is inactive"),r._NoMeshInRamWarningCount++)}return t}_handleObjectSectionCapping(e,i,t){const s=this.renderer,a=this.object,n=a.renderPointsOnly;let o=null;if(s.cuttingScene||i){for(let i=0;i<e.length;i++){const a=e[i].drawingGroups;let l=0;for(let e=0;e<a.length;e++){const i=a[e];i._geomType!==r||n||i.count>l&&(o=t&&i.material?i.material:i.gas.isGAS?i.gas.getMaterial(null,4,i.gas,s):i.gas,l=i.count)}}i&&!s.cuttingScene&&o&&o.color&&(a.sectionLinesBuilder.setColor(o.color),o=null)}return o}_getModifiedOriginalMaterial(e,i,t,s,a,n){const o=this.renderer,l=this.object._occurrence;let d=i;!s&&l&&l.__solvedSubstituteMaterials&&l.__solvedSubstituteMaterials.has(i)&&(d=l.__solvedSubstituteMaterials.get(i));let p=a;return t||(d=d.getGeomTypeDeferredMaterial(e._geomType,{boundaryEdge:o.boundaryEdgeMaterialInfo,smoothEdge:n._halfVisibleSmoothEdges>0})),(s&&e._geomType===r||this.QAAutomationMode)&&(d=d.getPredefinedMaterial(o.noiseTexture3D,this.defaultAppearanceType,o._customMaterialModeParams),p=null),d._deferredMaterialsInit||d.updateDeferredMaterials(),[d,p]}_getBackgroundViewModeMaterial(i,t){return this.renderer._backgroundViewMode===e.BackgroundViewMode.GHOST&&i.applyBackgroundViewMode?u||((u=new e.MeshBasicMaterial({force:!0,side:e.Constants.DoubleSide,color:new e.Color(16711680),transparent:!0})).activatePDSFX(),u.setPDSFXOverridableFunctions({},{ProcessFinalColor:"void ProcessFinalColor(inout vec4 color) { color = vec4(1.0, 1.0, 1.0, 0.1); }"}),u):t}_handleOldWorkFlowCommon(e,s,a,n,o,l,c,y){const u=this.object,h=this.renderer;let m=null,g=!1,P=!1;if(l&&c)m=p(c,s);else{const e=u.material||d,i=o||!u.fromLoadedModel,t=e.force&&"forceGeomTypeFACE"!==e.force,a=e.force&&"forceGeomTypeFACE"===e.force,n=i&&t;m=p(e,s),n||(o&&s.material&&s.material.updateDeferredMaterials?m=p(s.material,s):(m=p(s.gas,s),g=!0),a&&s._geomType===r&&(m=e,g=!1,P=!0))}m=this._getBackgroundViewModeMaterial(u,m),!l&&y&&s._geomType===r&&(m=y);const f=u._getFilter(i.LOOK);if(f){const e=f.material;e&&(P=!0,g=!1,m=p(e,s))}[m,t]=this._getModifiedOriginalMaterial(s,m,P,g,null,e);const L=h.materialToUse;let D=m._deferredMaterials[L];if(D){if(l&&h.cuttingScene&&s.glType>=4&&(null!==this.cappingMaterial?D=this.cappingMaterial:this.cappingMaterial=D),m.backMaterial){const i=m.backMaterial._deferredMaterials[L];i&&this._addDGToDL({geometry:n,dg:s,dgInterval:a,material:i,originalMaterial:m,renderMode:e,materialMode:o})}this._addDGToDL({geometry:n,dg:s,dgInterval:a,material:D,originalMaterial:m,renderMode:e,materialMode:o})}}_handleLayerOldWorkflow(e,i,t,r){const s=e.drawableGroup,a=e.drawableInterval,n=e.geometry;this._handleOldWorkFlowCommon(t,s,a,n,r,!0,i,null)}_handleGeometryOldWorkflow(e,i,t,r){const a=this.object,n=this.renderer,o=a.renderPointsOnly,l=a.material||d,p=t||!a.fromLoadedModel,c=l.force&&"forceGeomTypeFACE"!==l.force;let y=null;p&&c||(y=this._handleObjectSectionCapping(e,r,t)),y&&!n.cuttingScene&&("forceGeomTypeFACE"!==l.force||this.QAAutomationMode||(y=l));for(let r=0;r<e.length;r++){const a=e[r],n=a.drawingGroups;for(let e=0;e<n.length;e++){const r=n[e],l=r.glType;o&&l!=s||this._handleOldWorkFlowCommon(i,r,r,a,t,!1,null,y)}}}_handleNewWorkFlowCommon(t,r,s,a,n,o,l,d,c){const u=this.object,h=this.renderer,m=r.glType;let g=!1,P=null,f=!1;const L=u.material;n&&l||!L||!L.force||(l=p(L,r));const D=u.gas;let _=null;if(D?(P=D.getMaterial(o,m,r.gas.isGAS?r.gas:null,h,y(this.camera,u)),_=o.isGAS?o.clone().merge(D):D):(P=o.isGAS?o.getMaterial(null,m,r.gas,h,y(this.camera,u)):o,_=o.isGAS?o:null),_){if(this.pickingPass&&_._noPick)return;const t=u._getFilter(i.ZMODE)||h._GlobalZModeFilter;t&&(_._type===e.GASTypeEnum.NOZ&&t.enableZ?(_=_.clone())._type=e.GASTypeEnum.SKIN:_._type===e.GASTypeEnum.NOZ||t.enableZ||((_=_.clone())._type=e.GASTypeEnum.NOZ))}P&&!l&&(l=P,g=!0);const S=u._getFilter(i.LOOK);if(S){const e=S.material;e&&(f=!0,g=!1,l=p(e,r))}l=this._getBackgroundViewModeMaterial(u,l),[l,d]=this._getModifiedOriginalMaterial(r,l,f,g,d,t);const M=h.materialToUse;let E=l._deferredMaterials[M];if(E){if(this.getDLFromObject,this.onGeometryDisposeDS,l.backMaterial){const e=l.backMaterial._deferredMaterials[M];e&&this._addDGToDL({geometry:a,dg:r,dgInterval:s,material:e,originalMaterial:l,renderMode:t,matApp:d,gasMaterial:P,gasStruct:_,materialMode:n})}c&&h.cuttingScene&&m>=4&&(null!==this.cappingMaterial?l=E=this.cappingMaterial:this.cappingMaterial=E),this._addDGToDL({geometry:a,dg:r,dgInterval:s,material:E,originalMaterial:l,renderMode:t,matApp:d,gasMaterial:P,gasStruct:_,materialMode:n})}}_handleLayerNewWorkflow(e,i,t,r,s){const a=this.object,n=e.drawableGroup,o=n.glType;let l=null,d=null;if(s){const e=a.matApp;let t=e&&e._getMaterialFromGLType(o,n._geomType);i&&(i.isMatApp&&(d=(l=i.solveInheritance(e,a._occurrence.depth))===e?t:i._getMaterialFromGLType(o,n._geomType)),d||i.isMatApp||(l=t?e:null,d=t||i)),d||(l=e,d=t)}this._handleNewWorkFlowCommon(r,n,e.drawableInterval,e.geometry,s,t,d,l,!0)}_handleGeometryNewWorkflow(i,t,a,n){const o=this.object,l=o.renderPointsOnly,d=function(e){return e&&e.materialMode<2}(o);let p=this._handleObjectSectionCapping(i,n,a);const c=d?a:a||!o.fromLoadedModel;for(let n=0;n<i.length;n++){const d=i[n],y=d.drawingGroups;for(let i=0;i<y.length;i++){const n=y[i],u=n.glType;if(l&&u!=s)continue;let h=null,m=null;if(c){const i=o.matApp;let t=i&&i._getMaterialFromGLType(u,n._geomType);n.material&&(n.material.isMatApp&&(m=(h=n.material.solveInheritance(i,o._occurrence.depth))===i?t:n.material._getMaterialFromGLType(u,n._geomType)),m||n.material.isMatApp||(h=t?i:null,(m=t)||(m=n.material instanceof e.Material?n.material:null))),m||(h=i,m=t),!p||i&&h===i||n._geomType!==r||(m=p)}this._handleNewWorkFlowCommon(t,n,n,d,a,n.gas,m,h,!1)}}}_handleObjectWithLayers(e,i,t){const r=this.object,a=r.layer,n=this.renderer,o=r.renderPointsOnly,l=a.layers.length;this.cappingMaterial=null;for(let d=0;d<l;d++){const l=a.layers[d];let p=l.drawableInterval.getMaterial();if(l.drawableInterval.skip(r,n.visibleSpace))continue;const c=l.drawableGroup,y=c.glType;if(!o||y==s)if(e||p&&p.isMatApp){p||(p=c.material);let e=l.drawableInterval.gas;e||(e=c.gas),this._handleLayerNewWorkflow(l,p,e,i,t)}else this._handleLayerOldWorkflow(l,p,i,t)}}_handleObject(e,i,t,r){let s=this.object.geometry;s.length||0===s.length||(s=[s]),e?this._handleGeometryNewWorkflow(s,i,t,r):this._handleGeometryOldWorkflow(s,i,t,r)}_addDGToDL({material:e,originalMaterial:i,dg:t=null,dgInterval:r=null,geometry:s,renderMode:a=null,matApp:n=null,gasMaterial:o=null,gasStruct:l=null,materialMode:d=!1}){this.renderer.addDGToDL({object:this.object,geometry:s||this.object.geometry,dg:t,dgInterval:r,material:e,originalMaterial:i,renderMode:a,matApp:n,gasMaterial:o,gasStruct:l,getDLFromObjectFunc:this.getDLFromObject,geomDisposeFunc:this.onGeometryDisposeDS,enableGSSOCache:this.enableGSSOCache,camera:this.camera,materialMode:d})}solve(t){this.object=t;const r=this.object._occurrence,s=this.renderer,a=s.materialToUse,n=t.material;let o=null;const l=t.matApp,d=s.newMaterialInheritance||!!function(e){if(!e.geometry.length)return!1;const i=e.geometry[0].drawingGroups;return!(!i||!i.length)&&i[0].gas&&i[0].gas.isGAS}(t)||!!l;var p=t._getFilter(i._MATERIAL_TO_USE);if(p&&p.materialToUse!==a)return;if(r&&r._isFurtiveFiltered())return;if(!d&&n&&(n._deferredMaterialsInit||n.updateDeferredMaterials(),n.line&&!n.line._deferredMaterialsInit&&n.line.updateDeferredMaterials(),n.point&&!n.point._deferredMaterialsInit&&n.point.updateDeferredMaterials(),o=n._deferredMaterials[a]),void 0===o&&(o=n),null===t._gsso_cache&&(t._gsso_cache={}),t._gsso_cache[this.stateSortingResultID]||(t._gsso_cache[this.stateSortingResultID]=[]),t._updateBackgroundViewMode(this.renderer._invertBackgroundNodes),s._backgroundViewMode===e.BackgroundViewMode.NO_DISPLAY&&t.applyBackgroundViewMode)return;if(s._planeViewMode&&s._planeViewMode.mode!==e.PlaneViewMode.NORMAL&&(t._updatePlaneViewMode(s._planeViewMode.plane),t._applyPlaneViewMode)){const i=s._planeViewMode.mode;if(i===e.PlaneViewMode.NO_DISPLAY)return;if(this.pickingPass&&i===e.PlaneViewMode.LOWLIGHT_NO_PICK)return}this.getDLFromObject,this.onGeometryDisposeDS,this.camera;const c=s.getRenderMode(t);if(t.renderLineMode=c.getMasks()[1],2===t.geometry._type||t.geometry.length>=0){const e=t.layer,i=s.getMaterialMode(t,this.zOnlyPass);this._handleOutlines(c);const r=this._handleSectionLines(c);e?this._handleObjectWithLayers(d,c,i):this._handleObject(d,c,i,r)}else d&&n?(n._deferredMaterialsInit||n.updateDeferredMaterials(),n.backMaterial&&this._addDGToDL({material:n.backMaterial._deferredMaterials[a],originalMaterial:n}),this._addDGToDL({material:n._deferredMaterials[a],originalMaterial:n})):(o.backMaterial&&this._addDGToDL({material:o.backMaterial._deferredMaterials[a],originalMaterial:n}),this._addDGToDL({material:o,originalMaterial:n}))}}}),define("DS/RenderEngineUtils/DisplayRangeList",["DS/Mesh/ThreeJS_Base","DS/Visualization/FixedSizeArrayPool"],function(e,i){"use strict";var t=null;const r=e._VisuFilterType;var s=function(e,i,t,r,s,a){const n=i.geometry.indexBufferGPU;e.drawElements(e.LINE_STRIP,t,n.glFormat,i.start*n.bytesPerIndex)},a=function(e,i,t,r,s,a){e.drawArrays(i.dg.glType,i.geometry.vertexIndexArray[i.start],t)},n=function(e,i,t,r,s,a){e.drawArrays(i.dg.glType,i.start,t)},o=function(e,i,t,r,s,a){const n=i.geometry,o=n.indexBufferGPU;e.drawElements(i.dg.glType,t,o.glFormat,(n.indexOffsetGPU+i.start)*o.bytesPerIndex)},l=function(e,i,t,r,s,a){const n=i.geometry.indexBufferGPU;r._setVertexAttribDivisors(1,s,a),r.drawElementsInstanced(e.LINE_STRIP,t,n.glFormat,i.start*n.bytesPerIndex,i.object.nbInstances),r._setVertexAttribDivisors(0,s,a)},d=function(e,i,t,r,s,a){const n=i.geometry.indexBufferGPU,o=i.dg&&i.dg._instancingInfos?i.dg._instancingInfos.nbInstances:i.object.nbInstances;r._setVertexAttribDivisors(1,s,a),r.drawElementsInstanced(i.dg.glType,t,n.glFormat,i.start*n.bytesPerIndex,o),r._setVertexAttribDivisors(0,s,a)},p=function(e,i,t,r,s,a){if(a.specialMeshIds>=0){const s=i.geometry.indexBufferGPU;var n=i.autoInstancingData.updateData;i.autoInstancingData.updateData=!1,e.bindBuffer(e.ARRAY_BUFFER,i.autoInstancingData.idsBuffer),e.vertexAttribPointer(a.specialMeshIds,2,e.FLOAT,!1,0,0),n&&e.bufferData(e.ARRAY_BUFFER,i.autoInstancingData.idsArray,e.DYNAMIC_DRAW),r.vertexAttribDivisor(a.specialMeshIds,1),a.specialMeshPicking>=0&&(e.bindBuffer(e.ARRAY_BUFFER,i.autoInstancingData.pickingColorBuffer),e.vertexAttribPointer(a.specialMeshPicking,3,e.FLOAT,!1,0,0),n&&e.bufferData(e.ARRAY_BUFFER,i.autoInstancingData.pickingColorArray,e.DYNAMIC_DRAW),r.vertexAttribDivisor(a.specialMeshPicking,1));const o=i.autoInstancingData.lastId+1;r.drawElementsInstanced(i.dg.glType,t,s.glFormat,i.start*s.bytesPerIndex,o),r.vertexAttribDivisor(a.specialMeshIds,0),a.specialMeshPicking>=0&&r.vertexAttribDivisor(a.specialMeshPicking,0)}},c=function(i,r,s,a,n){this.material=i,this.material._displayRangeLists.set(a,this),this.occurrenceRenderingOverride=r,this.materialOverride=null,this.tree=new Map,this.ssrId=s,this.dl=n,null===t&&(t=e.Constants)};return c.prototype.getSortId=function(e,i){return e.isCurvedPipe?i.id:i.vertexBufferGPU?i.vertexBufferGPU.id:-1},c.prototype.insert=function(i,c,y,u,h,m,g,P){var f=this.getSortId(c,y),L=this.tree,D=L.get(f);if(D){var _=!0,S=D.next;if(u.cullingData||1!==u.glType||S.object.id===c.id&&S.geometry===y&&S.dg.glType===u.glType&&S.start+S.count===h&&(S.count+=m,_=!1),_){var M=D.next;D.next=i,i.attachment=D,i.next=M,null!==M&&(M.attachment=i)}}else D={tree:L,sortId:f,next:i},i.attachment=D,L.set(f,D);if(i.attachment){var E=!1,T=u?u.glType:-1;if(u){E=4===T||5===T;var O=c._getFilter(r.POLYGON_OFFSET);O?(i.doRenderLineModePolygonOffset.doIt=!0,i.doRenderLineModePolygonOffset.enable=O.enabled(),i.doRenderLineModePolygonOffset.unit=O.unit,i.doRenderLineModePolygonOffset.factor=O.factor):E&&!this.material.polygonOffset&&(i.doRenderLineModePolygonOffset.doIt=!0,i.doRenderLineModePolygonOffset.enable=0!==c.renderLineMode&&!(this.material.isWideLine||this.material.is2DLine),i.doRenderLineModePolygonOffset.unit=1,i.doRenderLineModePolygonOffset.factor=1);var v=null!==(u._instancingInfos?u._instancingInfos.instanceAttribArrays:c._instancingInfos?c._instancingInfos.instanceAttribArrays:null);c.isCurvedPipe?i.draw=p:this.material.wireframe&&E?i.draw=v?l:s:v?i.draw=d:0===T&&y.vertexPositionArray&&y.vertexPositionArray.nonindexedPoints?i.draw=a:u.nonIndexed?i.draw=n:i.draw=o}var I=P||(u?u.gas:null),b=!(!I||this.material.forceSide)?I:this.material,A=b.side===t.BackSide;return c._isDecal()&&(A=A||g!==e.MaterialToUse.highlightMaterial&&g!==e.MaterialToUse.pickingMaterial),c._bodyDim>0?i.doSetMaterialFaces.doubleSided=2===c._bodyDim:i.doSetMaterialFaces.doubleSided=b.side!==t.BackSide&&b.side!==t.FrontSide,i.doSetMaterialFaces.forceSide=b.forceSide,i.doSetMaterialFaces.flipSided=A,(c._occurrence&&c._occurrence.isInNozPass()||this.dl.noz)&&(i._programID|=e.ProgramIDs.NOZ_OBJECT),i.updateLineInfos=this.material.isWideLine||this.material.is2DLine||this.material.isDashedLine&&!this.material.is2DLine||u&&u._updateLineWithShapes,!0}return!1},c.prototype._setRenderingInfos=function(i,t){var r=this.material,s=(i.ssr.overridableMaterial||r.deferrable)&&r.overridable&&this.occurrenceRenderingOverride&&this.occurrenceRenderingOverride.materialOverride?this.occurrenceRenderingOverride.materialOverride:null;return s&&s.internalMaterialOverride&&s.internalMaterialOverride.material===r&&(s=null),this.materialOverride=s,!(r.overridable&&s&&0===s.getOpacity()||!s&&0===r.getOpacity())||e._isSelectionMaterial(t)},c.prototype.flagObjectsAsGSSOInvalidated=function(){this.tree.forEach(function(e,i){for(var t=e.next;null!==t;t=t.next)t.object._flagAsGSSOInvalidated()})},c.prototype.add=function({object:r,geometry:s,dg:a,dgInterval:n,renderMode:o,renderVolumesOnly:l,materialToUse:d,matApp:p,gasMaterial:c,cpInfos:y=null,qaAutomationMode:u}){var h=!0,m=o?o._maskWithDefault:e.RenderMode.defaultMask,g=d===e.MaterialToUse.pickingMaterial||d===e.MaterialToUse.highlightMaterial;if(g||(m&=~t.SurfacicPointRenderPointMask),a){var P=r&&r.getFilterGeomType(),f=P||a._geomType;if(f||(f=0===a.glType?e.GeomTypeEnum.UNKNOWN_0D:e.GeomTypeEnum.UNKNOWN),h=(f&m)>0,l){var L=c?c.side:a.gas?a.gas.side:-1;0===a.glType||1===a.glType||f&e.RenderMode.pointsMask||f&e.RenderMode.linesMask?h=!1:0===r._bodyDim&&L===e.DoubleSide?h=!1:r&&0!==r._bodyDim&&3!==r._bodyDim&&(h=!1)}if(u){var D=e.__QA_AUTOMATION_MASK__;h&&(h=(f&D)>0),g&&(h=(f&(D|=e.GeomTypeEnum.SMOOTH_EDGE|e.GeomTypeEnum.SHARP_EDGE|e.GeomTypeEnum.BOUNDARY_EDGE))>0)}}if(h){var _=n?n.start:0,S=n?n.count:0;if(r.isCurvedPipe){var M=e.InstancedCurvedPipeManager,E=M._perRenderableData.get(y.dgIndex);if(!E)return;var T=E[y.index],O=this.tree.get(s.id),v=O?O.next:null,I=M._geometryToInstancingGroup.get(s.id);if(!I)return;var b=2,A=d===e.MaterialToUse.pickingMaterial,R=!1;if(v)if(v.autoInstancingData){if(v.autoInstancingData.lastId+1>=v.autoInstancingData._maxNbInstances){b=2*v.autoInstancingData._maxNbInstances;var w=i.prototype.allocate_autoInstData(b,A,T.bin);v.autoInstancingData.copyDataTo(w),v.releaseAutoInstData(),v.autoInstancingData=w}}else v.autoInstancingData=i.prototype.allocate_autoInstData(b,A,T.bin);else R=!0,(v=i.prototype.allocateInstancedCurvedPipeSection(r,s,a,_,S,b,A,T.bin)).next=null,this.insert(v,r,s,a,_,S,d);var k=null;if(A){var F=r.pickingID>>16&255,G=(r.pickingID>>8&255)/255,x=(255&r.pickingID)/255;F|=128,k=(new e.Color).setRGB(F/255,G,x)}v.autoInstancingData.addInstance(r,T,y,k,I._nbCurvePointsPlus2),r._tokens[this.ssrId]||(r._tokens[this.ssrId]=[]);var B=r._tokens[this.ssrId];R=!0;for(var C=0;C<B.length;C++)if(B[C]===v){R=!1;break}R&&B.push(v)}else{var N=i.prototype.allocate(r,s,a,_,S,p,this.material.useGASColor||this.material.useGASOpacity?c:null);if(N.next=null,this.insert(N,r,s,a,_,S,d,c))r._tokens[this.ssrId]||(r._tokens[this.ssrId]=[]),(B=r._tokens[this.ssrId]).push(N)}}},c.prototype.getFirstDrawable=function(){if(this.tree.values)var e=this.tree.values().next().value;else{e=null;this.tree.forEach(function(i,t){null===e&&(e=i)})}return e?e.next:null},c}),define("DS/RenderEngineUtils/DisplayRangeListBackToFront",["DS/RenderEngineUtils/DisplayRangeList"],function(e){"use strict";var i=function(i,t,r,s,a){e.call(this,i,t,r,s,a)};return(i.prototype=Object.create(e.prototype)).getSortId=function(e,i){return 0},i.prototype.sortDrawablesBackToFront=function(e){var i=e.matrixWorldInverse.elements,t=this.tree.get(0),r=t?t.next:null;if(r)for(var s=null;null!==r;){if(null!==(s=r.object).renderDepth)s.z=s.renderDepth;else{var a=s.worldRadius,n=s.worldCenter,o=i[2]*n.x+i[6]*n.y+i[10]*n.z+i[14];s.z=o+a}for(var l=r.attachment,d=!1;l!==t&&s.z<l.object.z;)l=l.attachment,d=!0;var p=r.next;if(d){var c=r.attachment;c.next=p,p&&(p.attachment=c);var y=l.next;l.next=r,r.next=y,r.attachment=l,y&&(y.attachment=r)}r=p}},i}),define("DS/RenderEngineUtils/DisplayList",["DS/Mesh/ThreeJS_Base","DS/Visualization/WidelinesHelper","DS/RenderEngineUtils/DisplayRangeList","DS/RenderEngineUtils/DisplayRangeListBackToFront"],function(e,i,t,r){"use strict";var s=0,a=0,n=function(i){this.name="",this.string_id=s.toString(),s++,this.active=!0,this.priority=0,this.zSort=e.FrontToBack,this.side=e.DoubleSide,this.clearDepth=!1,this.clearStencil=!1,this.useStencil=!1,this.pickingPriority=!1,this.depthTest=!0,this.depthWrite=!0,this.depthFunc=null,this.polygonOffset=!1,this.polygonOffsetFactor=1,this.polygonOffsetUnits=1,this.canOIT=!1,this.canForceSideOnSolids=!1,this.hasTranspar=!1,this.hasDecal=!1,this.neverSort=!1,this.hasPoliteDepth=!0,this.hasOcclusionDepth=!0,this.noz=!1,this.sortByGLType=!1,this.setValues(i),this._materialList=[],this._displayRangeLists=new Map,this.index=-1,this.ssrId=-1,this.mustZSort=!1,this._sortByProgramNextFrame=!0,this._fatherDL=null},o=function(e,i,t,r,s,a,n,o){this.material=e,this.occurrenceRenderingOverride=i,this.id=t,this.occurrenceRenderingOverrideId=r,this.decalSortID=s,this.glType=a,this.materialToUse=n,this._objectsTransparentOnGPU=o};return n.prototype.addDrawable=function({material:s,object:n,geometry:l,dg:d,dgInterval:p,renderMode:c,renderVolumesOnly:y,geomDisposeFunc:u,materialToUse:h,matApp:m,gasMaterial:g,cpInfos:P=null,qaAutomationMode:f}){var L=!1,D=null,_=h+"-"+s.id;d&&this.canForceSideOnSolids&&0===h&&s._autoForceSide&&(5===d._dimension||g&&0===g.side)&&(n._forceBackFacesOnSolid=!0);var S=!1,M=null;n&&n.occurrenceRenderingOverride&&(S=(D=n.occurrenceRenderingOverride).isPurePGP(),_=_+"#"+(M=D.getGSSOId(S)));var E=-1;this.hasDecal&&n&&n._isInDecalPass()&&(_=_+"!"+n._decalData.stencilID+"?"+n._decalData.key,E=n._decalData.key);var T=!1;if(n._vertexColors&&s._allowObjectColor){_=_+"VC"+n._vertexColors;var O=n._vertexColors;T=(O&e.VertexColorType.A)>0||O<=e.VertexColorType.RGBA}this.neverSort&&(_+=a++);var v=this._displayRangeLists.get(_);if(S&&v){let e=v.occurrenceRenderingOverride;e&&e.getGSSOId(e.isPurePGP())!==M&&(v=null)}if(!v){var I=_.toString()+"_"+this.string_id;v=this.mustZSort?new r(s,D,this.ssr.id,I,this):new t(s,D,this.ssr.id,I,this),this._displayRangeLists.set(_,v),(L=v._setRenderingInfos(this,h))||this._displayRangeLists.delete(_)}if(s.isWideLine||s.is2DLine){if(!y){var b=l.computeWideLine(d,s);if(!b)return!1;if(l.wideLinesLinker&&(l.wideLinesLinker.linkedGeometry.addEventListener("dispose",u),s.isDashedLine&&(s.isCPUPattern||!l.wideLinesLinker.linkedGeometry.cpuPatternMode&&l.wideLinesLinker.linkedGeometry.vertexLineDistancesArray||i._computeWideLineDistances(l.wideLinesLinker.linkedGeometry))),p===d)p=b;else{var A=b.getPrimitiveStart(p.primitiveStart),R=b.getPrimitiveStart(p.primitiveStart+p.primitiveCount-1),w=b.getPrimitiveCount(p.primitiveStart+p.primitiveCount-1);A>=0&&R>=0&&(p={start:A,count:R-A+w},b.getPrimitiveStart(0)!==b.start&&(p.start+=b.start))}v.add({object:n,geometry:l.wideLinesLinker.linkedGeometry,dg:b,dgInterval:p,renderMode:c,renderVolumesOnly:y,materialToUse:h,matApp:m,gasMaterial:g,qaAutomationMode:f})}}else{if(s.isDashedLine&&!s.is2DLine)if(s.isCPUPattern);else if(l.cpuPatternMode||!l.vertexLineDistancesArray||l.isAttributeStale("position")){if(l&&!l.vertexIndexArray)return!1;i._computeLineDistances(l)}v.add({object:n,geometry:l,dg:d,dgInterval:p,renderMode:c,renderVolumesOnly:y,materialToUse:h,matApp:m,gasMaterial:g,cpInfos:P,qaAutomationMode:f})}if(L){var k=this._materialList,F=new o(s,D,_,D?D.id:-1,E,d?d.glType:-1,h,T);this._sortByProgramNextFrame=!0,k.push(F)}},n.prototype.empty=function(){for(var e=this._materialList,i=e.length,t=this._displayRangeLists,r=0;r<i;r++){var s=t.get(e[r].id);s&&s.material._displayRangeLists.delete(e[r].id.toString()+"_"+this.string_id)}e.length=0,t.clear()},n.prototype.cleanMaterialListAndDRLs=function(){for(var e=this._displayRangeLists,i=0,t=0,r=0,s=-1,a=this._materialList;i<a.length;){s=a[i].id;var n=e.get(s);n&&n.getFirstDrawable()?r>0?(a.splice(t,r),i=++t,r=0):t=++i:(r++,e.delete(s),n&&n.material._displayRangeLists.delete(s.toString()+"_"+this.string_id),i++)}r>0&&a.splice(t,r)},n.prototype.init=function(){},n.prototype.finalize=function(){},n.prototype.setValues=function(e){if(void 0!==e)for(var i in e){var t=e[i];void 0!==t?i in this&&(this[i]=t):console.warn("DisplayList: '"+i+"' parameter is undefined.")}},n}),define("DS/RenderEngineUtils/StateSortingResult",["DS/Mesh/ThreeJS_Base","DS/RenderEngineUtils/DisplayList"],function(e,i){"use strict";var t=null,r=null;e.displayListPriorityEnum={},e.displayListPriorityEnum.dlSolids=71,e.displayListPriorityEnum.dlSurfaces=70,e.displayListPriorityEnum.dlSurfacesCurvedPipes=69,e.displayListPriorityEnum.dlSurfacesCurvedPipesTranspar=68,e.displayListPriorityEnum.dlEdges=67,e.displayListPriorityEnum.dlBack=66,e.displayListPriorityEnum.dlPoints=65,e.displayListPriorityEnum.dlDecals=64,e.displayListPriorityEnum.dlTranspar1D=63,e.displayListPriorityEnum.dlTranspar=62,e.displayListPriorityEnum.dlEdgeAdjacenceSelection=61,e.displayListPriorityEnum.dlEdgePrimitiveSelection=60,e.displayListPriorityEnum.dlPointPrimitiveSelection=59,e.displayListPriorityEnum.dlNoZ=58,e.displayListPriorityEnum.dlNoZTranspar=57,e.displayListPriorityEnum.dlDepthValue=56,e.displayListPriorityEnum.dlDepthValue2=55,e.displayListPriorityEnum.OTHER3D_2DMODE=54,e.displayListPriorityEnum.OTHER3D=36,e.displayListPriorityEnum.priority0=53,e.displayListPriorityEnum.priority1=51,e.displayListPriorityEnum.priority2=49,e.displayListPriorityEnum.priority3=47,e.displayListPriorityEnum.priority4=45,e.displayListPriorityEnum.priority5=43,e.displayListPriorityEnum.priority6=41,e.displayListPriorityEnum.priority7=39,e.displayListPriorityEnum.priority8=37,e.displayListPriorityEnum.priority9=34,e.displayListPriorityEnum.priority10=32,e.displayListPriorityEnum.priority11=30,e.displayListPriorityEnum.priority12=28,e.displayListPriorityEnum.priority13=26,e.displayListPriorityEnum.priority14=24,e.displayListPriorityEnum.priority15=22,e.displayListPriorityEnum.priorityP0=52,e.displayListPriorityEnum.priorityP1=50,e.displayListPriorityEnum.priorityP2=48,e.displayListPriorityEnum.priorityP3=46,e.displayListPriorityEnum.priorityP4=44,e.displayListPriorityEnum.priorityP5=42,e.displayListPriorityEnum.priorityP6=40,e.displayListPriorityEnum.priorityP7=38,e.displayListPriorityEnum.priorityP8=35,e.displayListPriorityEnum.priorityP9=33,e.displayListPriorityEnum.priorityP10=31,e.displayListPriorityEnum.priorityP11=29,e.displayListPriorityEnum.priorityP12=27,e.displayListPriorityEnum.priorityP13=25,e.displayListPriorityEnum.priorityP14=23,e.displayListPriorityEnum.priorityP15=21,e.displayListPriorityEnum.HEDGE=20,e.displayListPriorityEnum.HOTHER=19,e.displayListPriorityEnum.FURTIVE_EDGE=18,e.displayListPriorityEnum.FURTIVE=17,e.displayListPriorityEnum.OTHER2D=16,e.displayListPriorityEnum.TRANSPAR2DP15=15,e.displayListPriorityEnum.TRANSPAR2DP14=14,e.displayListPriorityEnum.TRANSPAR2DP13=13,e.displayListPriorityEnum.TRANSPAR2DP12=12,e.displayListPriorityEnum.TRANSPAR2DP11=11,e.displayListPriorityEnum.TRANSPAR2DP10=10,e.displayListPriorityEnum.TRANSPAR2DP9=9,e.displayListPriorityEnum.TRANSPAR2DP8=8,e.displayListPriorityEnum.TRANSPAR2DP7=7,e.displayListPriorityEnum.TRANSPAR2DP6=6,e.displayListPriorityEnum.TRANSPAR2DP5=5,e.displayListPriorityEnum.TRANSPAR2DP4=4,e.displayListPriorityEnum.TRANSPAR2DP3=3,e.displayListPriorityEnum.TRANSPAR2DP2=2,e.displayListPriorityEnum.TRANSPAR2DP1=1,e.displayListPriorityEnum.TRANSPAR2DP0=0,e.displayListPriorityEnum.priority=[e.displayListPriorityEnum.priority0,e.displayListPriorityEnum.priority1,e.displayListPriorityEnum.priority2,e.displayListPriorityEnum.priority3,e.displayListPriorityEnum.priority4,e.displayListPriorityEnum.priority5,e.displayListPriorityEnum.priority6,e.displayListPriorityEnum.priority7,e.displayListPriorityEnum.priority8,e.displayListPriorityEnum.priority9,e.displayListPriorityEnum.priority10,e.displayListPriorityEnum.priority11,e.displayListPriorityEnum.priority12,e.displayListPriorityEnum.priority13,e.displayListPriorityEnum.priority14,e.displayListPriorityEnum.priority15],e.displayListPriorityEnum.priorityP=[e.displayListPriorityEnum.priorityP0,e.displayListPriorityEnum.priorityP1,e.displayListPriorityEnum.priorityP2,e.displayListPriorityEnum.priorityP3,e.displayListPriorityEnum.priorityP4,e.displayListPriorityEnum.priorityP5,e.displayListPriorityEnum.priorityP6,e.displayListPriorityEnum.priorityP7,e.displayListPriorityEnum.priorityP8,e.displayListPriorityEnum.priorityP9,e.displayListPriorityEnum.priorityP10,e.displayListPriorityEnum.priorityP11,e.displayListPriorityEnum.priorityP12,e.displayListPriorityEnum.priorityP13,e.displayListPriorityEnum.priorityP14,e.displayListPriorityEnum.priorityP15];var s=0,a=function(i,a){null===t&&(t=e.Constants,r=e.MaterialToUse),this.id=s++,this._displayLists=[],this._resetGSSOCacheIndex=-1,this._pickingPriorityMapping=null,this.pass=i,this.materialToUse=a,this.overridableMaterial=a===r.originalMaterial||a===r.oitAccumMaterial||a===r.oitRevealMaterial||a===r.transparentShadowMaterial||a===r.ZOnlyMaterial};return a.prototype.init=function(){for(var r=new i({name:"SOLID",priority:e.displayListPriorityEnum.dlSolids,zSort:e.FrontToBack,side:t.FrontSide,polygonOffset:!0}),s=new i({name:"SURFACE",priority:e.displayListPriorityEnum.dlSurfaces,zSort:e.FrontToBack,side:t.DoubleSide,polygonOffset:!0}),a=new i({name:"EDGE",priority:e.displayListPriorityEnum.dlEdges,zSort:e.FrontToBack,side:t.DoubleSide,polygonOffset:!1,hasOcclusionDepth:!1}),n=new i({name:"SURFACE_CURVED_PIPE",priority:e.displayListPriorityEnum.dlSurfacesCurvedPipes,zSort:e.FrontToBack,side:e.DoubleSide,polygonOffset:!0}),o=new i({name:"SURFACE_CURVED_PIPE_TRANSPAR",priority:e.displayListPriorityEnum.dlSurfacesCurvedPipesTranspar,zSort:e.BackToFront,side:t.DoubleSide,polygonOffset:!0,depthWrite:!1,hasTranspar:!0,canOIT:!0,hasOcclusionDepth:!1}),l=new i({name:"BACK",priority:e.displayListPriorityEnum.dlBack,zSort:e.FrontToBack,side:t.DoubleSide,polygonOffset:!0,depthWrite:!1,depthFunc:"GREATER",canOIT:!0,hasPoliteDepth:!1,hasOcclusionDepth:!1}),d=new i({name:"POINT",priority:e.displayListPriorityEnum.dlPoints,zSort:e.FrontToBack,side:t.DoubleSide,polygonOffset:!1,hasOcclusionDepth:!1}),p=new i({name:"DECALS",priority:e.displayListPriorityEnum.dlDecals,zSort:e.FrontToBack,side:t.DoubleSide,polygonOffset:!0,hasTranspar:!0,hasDecal:!0,hasPoliteDepth:!1,hasOcclusionDepth:!1,canOIT:!0}),c=new i({name:"TRANSPAR1D",priority:e.displayListPriorityEnum.dlTranspar1D,zSort:e.BackToFront,side:t.DoubleSide,polygonOffset:!0,depthWrite:!1,hasTranspar:!0,canOIT:!0,hasOcclusionDepth:!1}),y=new i({name:"TRANSPAR",priority:e.displayListPriorityEnum.dlTranspar,zSort:e.BackToFront,side:t.DoubleSide,polygonOffset:!0,depthWrite:!1,hasTranspar:!0,canOIT:!0,hasOcclusionDepth:!1,canForceSideOnSolids:!0}),u=new i({name:"EDGEADJACENCESELECTION",priority:e.displayListPriorityEnum.dlEdgeAdjacenceSelection,zSort:e.FrontToBack,side:t.DoubleSide,polygonOffset:!1,hasOcclusionDepth:!1,depthTest:!1}),h=new i({name:"EDGEPRIMITIVESELECTION",priority:e.displayListPriorityEnum.dlEdgePrimitiveSelection,zSort:e.FrontToBack,side:t.DoubleSide,polygonOffset:!1,hasOcclusionDepth:!1,depthTest:!1}),m=new i({name:"POINTPRIMITIVESELECTION",priority:e.displayListPriorityEnum.dlPointPrimitiveSelection,zSort:e.FrontToBack,side:t.DoubleSide,polygonOffset:!1,hasOcclusionDepth:!1,depthTest:!1}),g=new i({name:"NOZ",priority:e.displayListPriorityEnum.dlNoZ,zSort:e.FrontToBack,side:t.DoubleSide,polygonOffset:!0,clearDepth:!0,hasPoliteDepth:!1,noz:!0,hasOcclusionDepth:!1}),P=new i({name:"DEPTHVALUE2",priority:e.displayListPriorityEnum.dlDepthValue2,zSort:e.BackToFront,side:t.DoubleSide,hasPoliteDepth:!1,depthTest:!1,noz:!0,neverSort:!0,hasOcclusionDepth:!1}),f=new i({name:"DEPTHVALUE",priority:e.displayListPriorityEnum.dlDepthValue,zSort:e.FrontToBack,side:t.DoubleSide}),L=new i({name:"NOZTRANSPAR",priority:e.displayListPriorityEnum.dlNoZTranspar,zSort:e.BackToFront,side:t.DoubleSide,polygonOffset:!0,hasPoliteDepth:!1,noz:!0,hasOcclusionDepth:!1}),D=new i({name:"OTHER2D",priority:e.displayListPriorityEnum.OTHER2D,zSort:e.FrontToBack,side:t.DoubleSide}),_=0;_<16;_++){var S=new i({name:"NOZPRIORITY"+_,priority:e.displayListPriorityEnum.priority[_],zSort:e.FrontToBack,side:t.DoubleSide,polygonOffset:!0,depthTest:!1,hasOcclusionDepth:!0,hasPoliteDepth:!1,sortByGLType:!0,noz:!0});this.addDisplayList(S)}for(_=0;_<16;_++){S=new i({name:"NOZPRIORITYP"+_,priority:e.displayListPriorityEnum.priorityP[_],zSort:e.FrontToBack,side:t.DoubleSide,polygonOffset:!0,depthTest:!0,depthFunc:"ALWAYS",hasOcclusionDepth:!0,hasPoliteDepth:!1,sortByGLType:!0,noz:!0});this.addDisplayList(S)}for(_=0;_<16;_++){S=new i({name:"TRANSPAR2DP"+_,priority:e.displayListPriorityEnum.TRANSPAR2DP0+1,zSort:e.BackToFront,side:t.DoubleSide,polygonOffset:!0,depthTest:!0,depthFunc:"ALWAYS",hasTranspar:!0,canOIT:!0,hasOcclusionDepth:!1,canForceSideOnSolids:!0});this.addDisplayList(S)}this.addDisplayList(r),this.addDisplayList(s),this.addDisplayList(n),this.addDisplayList(o),this.addDisplayList(a),this.addDisplayList(l),this.addDisplayList(d),this.addDisplayList(p),this.addDisplayList(y),this.addDisplayList(c),this.addDisplayList(u),this.addDisplayList(h),this.addDisplayList(m),this.addDisplayList(g),this.addDisplayList(L),this.addDisplayList(D),this.addDisplayList(f),this.addDisplayList(P)},a.prototype.addDisplayList=function(i){var t,r=!1;if(this._displayLists.length&&void 0!==i.priority)for(t=0;t<this._displayLists.length&&!r;t++){var s=this._displayLists[t].priority;i.priority>s&&(this.insertDisplayList(i,t),r=!0)}for(r||this._displayLists.push(i),t=0;t<this._displayLists.length;t++)this._displayLists[t].index=t;return i.ssrId=this.id,i.ssr=this,i.zSort===e.BackToFront&&(i.mustZSort=!0),i},a.prototype.insertDisplayList=function(e,i){this._displayLists.splice(i,0,e)},a.prototype.getDisplayListByPriority=function(e){for(var i=0;i<this._displayLists.length;i++)if(this._displayLists[i].priority===e)return this._displayLists[i];return null},a.prototype.getDisplayListByName=function(e){for(var i=0;i<this._displayLists.length;i++)if(this._displayLists[i].name===e)return this._displayLists[i];return null},a}),define("DS/RenderEngineUtils/RenderEngineUtils",["DS/RenderEngineUtils/StateSortingResult","DS/RenderEngineUtils/DisplayList","DS/RenderEngineUtils/InheritanceSolver","DS/RenderEngineUtils/StateSortingCacheItem"],function(e,i,t,r){"use strict";return{StateSortingResult:e,DisplayList:i,InheritanceSolver:t,StateSortingCacheItem:r}});