/* eslint-disable no-empty */
/*
*/

define('DS/ModelLoader_3dwb/loader',
[
  'UWA/Core'
  , 'UWA/Promise'
  , 'DS/Plugins/UserRenderList'
  , 'DS/ZipJS/zip'
  , 'DS/Visualization/SceneGraphFactory'
  , 'DS/CAT3DWVisu/CAT3DWThreeJsExtender'
  , 'DS/Visualization/ThreeJS_DS'
  , 'DS/Visualization/Node3D'
  , 'DS/CAT3DSKExperience/CAT3DSKAnnotationAbbreviationsExperience'
  , 'DS/WAFData/WAFData'
  , 'DS/Visualization/ModelLoader'
  , 'DS/Usage/TrackerAPI'
  , 'DS/Visualization/MaterialApplication'
  , 'DS/CAT3DWVideosUI/CAT3DWVideosController'
  , 'DS/Core/PointerEvents'
  , 'DS/CoreEvents/Events'
  , 'DS/CAT3DWInfraUI/CAT3DWHandleErrors'
  , 'DS/SVGLoader/SVGLineMaterial'
  , 'DS/UIKIT/Mask'
  , 'DS/PlatformAPI/PlatformAPI'
  , 'DS/Mesh/MeshUtils'
  , 'DS/CAT3DWInfra/CAT3DW3DSwymMediaServices'
  , 'DS/CAT3DWInfra/CAT3DWUrlServices'
  , 'DS/WebappsUtils/WebappsUtils'
  , 'DS/CAT3DWInfra/CAT3DWPreferenceManager'
  , 'DS/CAT3DWInfra/CAT3DWFileServices'
  , 'DS/CAT3DWRichLinksUI/CAT3DWRichLinkPreviewGenerator'
  , 'DS/CAT3DWText/CAT3DWTextCommonConstants'
  , 'DS/CAT3DWText/CAT3DWTextRenderService'
  , 'css!DS/CAT3DWRichLinksUI/CAT3DWRichLinksUI'
  , 'DS/CAT3DWVisu/CAT3DWGeomFactory'
],
function (
  UWA
  , PromiseUWA
  , UserRenderList
  , zip
  , SceneGraphFactory
  , THREE_Extended
  , THREE
  , Node3D
  , CAT3DSKAnnotationAbbreviations
  , WAFData
  , ModelLoader
  , TrackerAPI
  , MaterialApplication
  , CAT3DWVideosController
  , PointerEvents
  , WUXEvents
  , CAT3DWHandleErrors
  // eslint-disable-next-line no-unused-vars
  , SVGLineMaterial // require needed to use THREE.SVGLineMaterial
  , Mask
  , PlatformAPI
  , Mesh
  , CAT3DW3DSwymMediaServices
  , CAT3DWUrlServices
  , WebappsUtils
  , CAT3DWPreferenceManager
  , CAT3DWFileServices
  , CAT3DWRichLinkPreviewGenerator
  , CAT3DWTextCommonConstants
  , CAT3DWTextRenderService
  , cssfile
  , CAT3DWGeomFactory
) {
  'use strict';
  return UWA.Class.extend({

    init: function (options) {
      this._parent(options);
      this._3dMediaCounter = 0;
      this._2dMediaCounter = 0;
      this._videoController = new CAT3DWVideosController();
      this._mapVid = new Map();
      this._mapText = new Map();

      const isCorrectReceiver = (cb, eventData) => {
        if (this._viewer.div && eventData.viewerDiv) {
          if (eventData.viewerDiv === this._viewer.div) {
            cb();
          }
        } else {
          cb();
        }
      };

      // Subscribing to events sent by CAT3DSketchExperience
      this._onStartExperience = WUXEvents.subscribe({ event: 'CAT3DSKExperienceStart' }, isCorrectReceiver.bind(this, this.startExperience.bind(this)));
      this._onStopExperience = WUXEvents.subscribe({ event: 'CAT3DSKExperienceStop' }, isCorrectReceiver.bind(this, this.stopExperience.bind(this)));
      this._onExitExperience = WUXEvents.subscribe({ event: 'CAT3DSKExperienceExit' }, isCorrectReceiver.bind(this, this.dispose.bind(this)));

      this._isMobileApp = false;
      if (window && window.ds && window.ds.env) {
        if (window.ds.env === 'MOBILE') {
          this._isMobileApp = true;
        }
      }
      this._defaultRectangleValue = 100;

      this._textRenderService = new CAT3DWTextRenderService();
    },

    dispose: function () {
      WUXEvents.unsubscribe(this._onExitExperience);
      WUXEvents.unsubscribe(this._onStartExperience);
      WUXEvents.unsubscribe(this._onStopExperience);

      this.stopExperience();
      this._3DModelsNode.removeChildren ();
      this._3DModelsNode = null;
      this._videoModelsNode.removeChildren ();
      this._videoModelsNode = null;
      this._annotContainer.removeChildren ();
      this._annotContainer = null;
      this._3DSKTContainer.removeChildren ();
      this._3DSKTContainer = null;

      this._mapText = null;
      this._textRenderService = null;
      this._swymMediaServices.dispose();
      this._swymMediaServices = null;
      this._beforeMainPass = null;
      this._viewer = null;
      this._initialCameraDistance = null;
      this._pickedObject = null;
      this._onZoomToken = null;
      this._lastRenderCameraDistance = null;

      // Remove videos
      this._mapVid.forEach((obj) => {
        this._videoController.removeVideo(obj.video);
      });
      this._videoController.dispose();
      this._videoController = null;
      this._mapVid = null;
    },

    startExperience: function () {
      this._normal = new THREE.Vector3(0, 0, -1);
      this._pickedObject = null;

      this._onDownCB = this._onDown.bind(this);
      this._onMoveCB = this._onMove.bind(this);
      this._onUpCB = this._onUp.bind(this);

      const viewerContainer = this._viewer.div;
      viewerContainer.addEventListener(PointerEvents.POINTERDOWN, this._onDownCB);
      viewerContainer.addEventListener(PointerEvents.POINTERMOVE, this._onMoveCB);
      viewerContainer.addEventListener(PointerEvents.POINTERUP, this._onUpCB);

      this._onZoomToken = this._viewer.currentViewpoint.control.onZoom(this._onZoom.bind(this));
    },

    stopExperience: function () {
      const viewerContainer = this._viewer.div;
      viewerContainer.removeEventListener(PointerEvents.POINTERDOWN, this._onDownCB);
      viewerContainer.removeEventListener(PointerEvents.POINTERMOVE, this._onMoveCB);
      viewerContainer.removeEventListener(PointerEvents.POINTERUP, this._onUpCB);

      this._viewer.currentViewpoint.control.unsubscribe(this._onZoomToken);

      this._pickedObject = null;
      this._normal = null;

      // Resetting the models to their original orientation
      if (this._3DModelsNode && this._3DModelsNode.children) {
        for (let i = 0; i < this._3DModelsNode.children.length; i++) {
          this._3DModelsNode.children[i].setMatrix(new THREE.Matrix4());
        }
      }
      // Stopping the videos and resetting them to the begining
      this._mapVid.forEach((obj) => {
        obj.video.pause();
        obj.video.reset();
      });
    },

    _onDown: function (iData) {
      this._pickedObject = this._viewer.pick(this._viewer.getMousePosition(new THREE.Vector2(iData.offsetX, iData.offsetY)), 'primHybrid', true);
      if (this._pickedObject) {
        this._pickedObject.modelObject = null;

        if (this._pickedObject.position) {
          this._lastMousePosition = new THREE.Vector2(iData.offsetX, iData.offsetY);
        }

        //const pathElement = this._pickedObject.path?.[0];
        const pathElement = (this._pickedObject.path && this._pickedObject.path[0]) ? this._pickedObject.path[0] : null;
        if (pathElement) {
          pathElement.externalPath.some((node, idx, externalPath) => {
            const isVideo = node.name === this._videoModelsNode.name;
            const is3DModel = node.name === this._3DModelsNode.name;
            let isLink = node.isURL;
            if (isVideo || is3DModel) {
              const mediaNode = externalPath[idx + 1];
              if (!mediaNode) {
                return false;
              } else if (isVideo) {
                this._videoClick(mediaNode.name);
              } else {
                this._pickedObject.modelObject = mediaNode;
              }

              return true;
            }
            if (isLink) {
              window.open(node.url);
            }
            return false;
          });
        }
      }
    },

    _onMove: function (iData) {
      if (this._pickedObject && this._pickedObject.modelObject) {
        var currentMousePos = new THREE.Vector2(iData.offsetX, iData.offsetY);
        this._lastMouseMoveDistance = this._lastMousePosition.distanceTo(currentMousePos);
        if (isNaN(this._lastMouseMoveDistance)) {
          this._lastMouseMoveDistance = 0;
          return;
        }

        var translationInScreenCoords = currentMousePos.clone().sub(this._lastMousePosition).normalize();
        var screenCenterOnPlane = this._pickOnPlaneOfObject(new THREE.Vector2(), this._pickedObject.position);
        var tranlatedCenterOnPlane = this._pickOnPlaneOfObject(translationInScreenCoords, this._pickedObject.position);
        this._lastMousePosition = currentMousePos;

        var translationInPlaneCoords = tranlatedCenterOnPlane.sub(screenCenterOnPlane).normalize();
        var rotationAxis = translationInPlaneCoords.clone().cross(this._normal).normalize();

        var rotation = 0.005 * this._lastMouseMoveDistance;

        if (rotation) {
          this._rotate3DModel(this._pickedObject.modelObject, rotationAxis, rotation);
        }
      }
    },

    _onUp: function () {
      this._pickedObject = null;
    },

    _pickOnPlaneOfObject: function (screenPoint, objPoint) {
      var plane = new THREE.Plane();
      plane.setFromNormalAndCoplanarPoint(this._normal, objPoint).normalize();
      // line from camera
      var ray = this._viewer.currentViewpoint.create3DRayFrom2DPoint(screenPoint);
      var intersectPoint = ray.intersectPlane(plane);

      return intersectPoint;
    },

    _rotate3DModel: function (iModel, iRotationAxis, iRotationValue, iRotationCenter) {
      var translation1, translation2, center;

      if (iRotationCenter) {
        translation1 = new THREE.Matrix4().makeTranslation(-iRotationCenter.x, -iRotationCenter.y, -iRotationCenter.z);
        translation2 = new THREE.Matrix4().makeTranslation(iRotationCenter.x, iRotationCenter.y, iRotationCenter.z);
      } else if (iModel.parents[0].name === 'bbox_3D') {
        center = iModel.parents[0].getBoundingBox().center();

        translation1 = new THREE.Matrix4().makeTranslation(-center.x, -center.y, -center.z);
        translation2 = new THREE.Matrix4().makeTranslation(center.x, center.y, center.z);
      } else if (iModel.name === 'bbox_3D') {
        center = iModel.getBoundingBox().center();

        translation1 = new THREE.Matrix4().makeTranslation(-center.x, -center.y, -center.z);
        translation2 = new THREE.Matrix4().makeTranslation(center.x, center.y, center.z);
      } else {
        center = iModel.getCenter();
        translation1 = new THREE.Matrix4().makeTranslation(-center.x, -center.y, -center.z);
        translation2 = new THREE.Matrix4().makeTranslation(center.x, center.y, center.z);
      }

      var iRotation = new THREE.Matrix4().makeRotationAxis(iRotationAxis, iRotationValue);

      iModel.applyMatrix(translation1);
      iModel.applyMatrix(iRotation);
      iModel.applyMatrix(translation2);

      this._viewer.render();
    },

    _videoClick: function (iNode3DName) {
      var obj = this._mapVid.get(iNode3DName);
      if (obj && obj.video) {
        if (obj.video.isPlaying()) {
          obj.video.pause();
        } else {
          obj.video.play();
        }
      }
    },

    _onZoom: function () {
      // adapting videos to current zoom
      var dataPos;
      var videoDimension;

      this._mapVid.forEach((value) => {
        dataPos = value;
        videoDimension = new THREE.Vector2(
          this._viewer.currentViewpoint.project(dataPos.topLeft).distanceTo(this._viewer.currentViewpoint.project(dataPos.topRight)),
          this._viewer.currentViewpoint.project(dataPos.topLeft).distanceTo(this._viewer.currentViewpoint.project(dataPos.bottomLeft))
        );

        dataPos.video.updateVideoControls(dataPos.position, videoDimension);
      });

      // adapting texts to current zoom
      let currentCameraDistance = this._viewer.currentViewpoint.getTargetDistance();
      let zoomChangeRatio = (this._lastRenderCameraDistance || this._initialCameraDistance) / currentCameraDistance;

      if (zoomChangeRatio > CAT3DWTextCommonConstants.zoomInThreshold || zoomChangeRatio < CAT3DWTextCommonConstants.zoomOutThreshold) {
        this._mapText.forEach((iTextObj) => {
          if (!iTextObj.textnode.isURL) {
            this._redrawText(iTextObj.textnode, iTextObj.textbox);
          }
        });
        this._lastRenderCameraDistance = currentCameraDistance;
      }
    },

    unzip: function (zipFile) {
      return new PromiseUWA(function (resolve /*, reject*/) {
        var blobReader = new zip.BlobReader(zipFile);
        zip.createReader(
          blobReader,
          function (reader) {
            var zipReader = reader;
            zipReader.getEntries(function (entries) {
              var entry = entries.find(function (element) {
                return element.filename === 'sketch.json';
              });
              if (entry) {
                entry.getData(new zip.TextWriter(), function (data) {
                  var jsonStr = data.target.result;
                  zipReader.close(function (/*blob*/) {
                    resolve(jsonStr);
                  });
                });
              }
            });
          },
          function () {
            console.log('zip error');
          }
        );
      });
    },

    readJsonFile: function (file) {
      return new PromiseUWA(function (resolve, reject) {
        // Getting the data using WAFData
        var reqOptions = {
          proxy: 'none'
        };
        reqOptions.type = 'arraybuffer';
        reqOptions.timeout = 10 * 60 * 1000;

        // We got the data
        reqOptions.onComplete = function (data) {
          resolve(new Blob([data], { type: 'application/zip' }));
        };

        reqOptions.onError = function (data) {
          reject(data);
        };

        WAFData.authenticatedRequest(file, reqOptions);
      });
    },

    getListPointFromCircle: function (annotJSON, normal) {
      var circlePoints = [];
      var circlePlaneNormal = new THREE.Vector3(normal.x, normal.y, normal.z);
      var startPoint = new THREE.Vector3(annotJSON.annotStartPoint.x, annotJSON.annotStartPoint.y, annotJSON.annotStartPoint.z);
      var center = new THREE.Vector3(annotJSON.geometry.center.x, annotJSON.geometry.center.y, annotJSON.geometry.center.z);
      var radius = annotJSON.geometry.radius;
      var dx = new THREE.Vector3().subVectors(startPoint, center).normalize();
      var dy = new THREE.Vector3().crossVectors(dx, circlePlaneNormal).normalize();
      var step = (2 * Math.PI) / annotJSON.pointsCount;
      for (var theta = 0; theta < 2 * Math.PI; theta += step) {
        var x = center.x + radius * Math.cos(theta) * dx.x + radius * Math.sin(theta) * dy.x;
        var y = center.y + radius * Math.cos(theta) * dx.y + radius * Math.sin(theta) * dy.y;
        var z = center.z + radius * Math.cos(theta) * dx.z + radius * Math.sin(theta) * dy.z;
        circlePoints.push(new THREE.Vector3(x, y, z));
      }
      return circlePoints;
    },
    getListPointFromEllipse: function (annotJSON) {
      var ellipsePoints = [];
      //var startPoint = new THREE.Vector3(annotJSON.annotStartPoint.x, annotJSON.annotStartPoint.y, annotJSON.annotStartPoint.z);
      var center = new THREE.Vector3(annotJSON.geometry.center.x, annotJSON.geometry.center.y, annotJSON.geometry.center.z);
      var dx = new THREE.Vector3(annotJSON.geometry.majorAxis.dir.x, annotJSON.geometry.majorAxis.dir.y, annotJSON.geometry.majorAxis.dir.z);
      var dy = new THREE.Vector3(annotJSON.geometry.minorAxis.dir.x, annotJSON.geometry.minorAxis.dir.y, annotJSON.geometry.minorAxis.dir.z);
      var majorAxisLength = annotJSON.geometry.majorAxis.length;
      var minorAxisLength = annotJSON.geometry.minorAxis.length;

      var step = (2 * Math.PI) / annotJSON.pointsCount;
      for (var theta = 0; theta < 2 * Math.PI; theta += step) {
        var x = center.x + majorAxisLength * Math.cos(theta) * dx.x + minorAxisLength * Math.sin(theta) * dy.x;
        var y = center.y + majorAxisLength * Math.cos(theta) * dx.y + minorAxisLength * Math.sin(theta) * dy.y;
        var z = center.z + majorAxisLength * Math.cos(theta) * dx.z + minorAxisLength * Math.sin(theta) * dy.z;
        ellipsePoints.push(new THREE.Vector3(x, y, z));
      }
      return ellipsePoints;
    },
    getListPointFromLine: function (annotJSON) {
      var linePoints = [];
      var startPoint = new THREE.Vector3(annotJSON.geometry.startPoint.x, annotJSON.geometry.startPoint.y, annotJSON.geometry.startPoint.z);
      var endPoint = new THREE.Vector3(annotJSON.geometry.endPoint.x, annotJSON.geometry.endPoint.y, annotJSON.geometry.endPoint.z);
      linePoints.push(startPoint, endPoint);
      return linePoints;
    },
    getListPointFromRectangle: function (annotJSON) {
      var trianglePoints = [];
      var vertexA = new THREE.Vector3(annotJSON.geometry.vertexA.x, annotJSON.geometry.vertexA.y, annotJSON.geometry.vertexA.z);
      var vertexB = new THREE.Vector3(annotJSON.geometry.vertexB.x, annotJSON.geometry.vertexB.y, annotJSON.geometry.vertexB.z);
      var vertexC = new THREE.Vector3(annotJSON.geometry.vertexC.x, annotJSON.geometry.vertexC.y, annotJSON.geometry.vertexC.z);
      var vertexD = new THREE.Vector3(annotJSON.geometry.vertexD.x, annotJSON.geometry.vertexD.y, annotJSON.geometry.vertexD.z);
      trianglePoints.push(vertexA, vertexB, vertexC, vertexD, vertexA);
      return trianglePoints;
    },
    getListPointFromTriangle: function (annotJSON) {
      var trianglePoints = [];
      var vertexA = new THREE.Vector3(annotJSON.geometry.vertexA.x, annotJSON.geometry.vertexA.y, annotJSON.geometry.vertexA.z);
      var vertexB = new THREE.Vector3(annotJSON.geometry.vertexB.x, annotJSON.geometry.vertexB.y, annotJSON.geometry.vertexB.z);
      var vertexC = new THREE.Vector3(annotJSON.geometry.vertexC.x, annotJSON.geometry.vertexC.y, annotJSON.geometry.vertexC.z);
      trianglePoints.push(vertexA, vertexB, vertexC, vertexA);
      return trianglePoints;
    },
    getListPointFromUnknown: function (annotJSON) {
      var points = [];
      for (var i = 0; i < annotJSON.geometry.points.length; i++) {
        var point = new THREE.Vector3(annotJSON.geometry.points[i].x, annotJSON.geometry.points[i].y, annotJSON.geometry.points[i].z);
        points.push(point);
      }
      return points;
    },

    _getLineMaterial: function (graphicalProp) {
      var params = {
        color: graphicalProp.color,
        lineWidth: graphicalProp.thickness * window.devicePixelRatio,
        opacity: graphicalProp.opacity,
        transparent: false,
        force: true,
        isCPUPattern: true, // Fixed dashes size with zoom
        //linejoin: 'round',
        linecap: 'round',
        needsUpdate: true
      };

      var lineMaterial = new THREE.LineBasicMaterial(params);
      return lineMaterial;
    },

    _getSphereMaterial: function (graphicalProp) {
      var sphereMaterial = new THREE.MeshBasicMaterial({
        color: graphicalProp.color,
        opacity: graphicalProp.opacity,
        transparent: false,
        force: true
      });

      return sphereMaterial;
    },

    _applyOpacityStencilOp: function (node, graphicalProp) {
      if (graphicalProp.opacity < 1) {
        var passManager = this._viewer.getUserPassManager();
        var stencilPass = new UserRenderList('StencilOpacityPass');
        var renderPass = passManager.createUserRenderPass('userRenderPass', stencilPass);
        renderPass.setEnableStencilTest(true);
        renderPass.setEnableStencilWrite(true);
        renderPass._passes[0].stencilRef = graphicalProp.opacity * 100;
        renderPass.setStencilFunc('NOTEQUAL');
        renderPass.setStencilOpsSeparate('REPLACE', 'REPLACE', 'KEEP', 'KEEP');
        var mainPass = passManager.getSystemPass('main');
        passManager.insertUserPassAfterPass(renderPass, mainPass);
        node.setRenderPasses([stencilPass]);
      }
    },

    draw: function (listPoint, type, graphicalProp) {
      var lineParams = {
        points: listPoint
        //drawMode: Mesh.ConnectivityTypeEnum.LINES
      };
      if ('point' === type) { // If point draw Point
        this.drawPoint(listPoint, graphicalProp);
        return;
      }

      lineParams.material = new THREE.SVGLineMaterial({
        color: graphicalProp.color,
        lineWidth: graphicalProp.thickness * window.devicePixelRatio,
        side: THREE.DoubleSide,
        opacity: graphicalProp.opacity,
        enableClipPlanes: false,
        linecap: 'round',
        force: true,
        transparent: false
      });
      var splineNode = SceneGraphFactory.createLineNode(lineParams);
      splineNode.setName('3DSketch_Annot');
      // splineNode.oldSetMaterial(lineMaterial);
      this._annotContainer.add(splineNode);
      // Opacity homogeneisation
      this._applyOpacityStencilOp(splineNode, graphicalProp);
    },

    drawPoint: function (listPoint, graphicalProp) {
      var circleMaterial = new THREE.MeshBasicMaterial({
        color: graphicalProp.color,
        transparent: false,
        force: true,
        opacity: graphicalProp.opacity,
        side: THREE.DoubleSide
      });
      var splineNode = SceneGraphFactory.createCircleNode({
        segments: 20,
        radius: graphicalProp.thickness * 0.5,
        fill: true,
        material: circleMaterial
      });
      splineNode.name = 'CAT3DSketch-PointNode';
      splineNode.setMatrix(new THREE.Matrix4().setPosition(listPoint[0]));
      this._annotContainer.addChild(splineNode);
      this._applyOpacityStencilOp(splineNode, graphicalProp);
    },

    load3DModel: function (modelFormat, modelPath, transfoMatrixArray /*, iCallback*/) {
      return new PromiseUWA( function (resolve) {
        // ByPass cors issue with parameter
        var urlService = new CAT3DWUrlServices();
        modelPath = urlService.generateUrlWithSuffix(modelPath);

        // proxify url for mobile app
        if (this._isMobileApp) {
          modelPath = UWA.Data.proxifyUrl(modelPath, { proxy: 'passport' });
        }

        var modelLoader = new ModelLoader();
        // parent Node
        var string = 'model3DNode_' + this._3dMediaCounter;
        this._3dMediaCounter++;

        var bboxNode = new Node3D('bbox_3D');
        var model3DNode = new Node3D(string);
        bboxNode.addChild(model3DNode);
        // affect
        this._3DModelsNode.addChild(bboxNode);

        modelLoader.setOnLoadedCallback(() => {
          // Moving the children to make the model position match the bbox center
          var center = model3DNode.getBoundingBox().center();
          var matrix = new THREE.Matrix4();
          matrix.makeTranslation(-center.x, -center.y, -center.z);
          for (var i = 0; i < model3DNode.children.length; i++) {
            model3DNode.children[i].setMatrix(matrix);
          }
          resolve();
        });

        modelLoader.setOnErrorCallback(function (err) {
          console.error(err);
        });

        modelLoader.loadModel(
          {
            filename: modelPath,
            proxyurl: 'none',
            format: modelFormat,
            withCredentials: true,
            viewer: this._viewer
          },
          model3DNode
        );

        var transfoMat = new THREE.Matrix4();

        transfoMat.setFromArray(transfoMatrixArray);

        model3DNode.setMatrix(transfoMat);
        urlService.dispose();
      }.bind(this));
    },

    onLoadImageError: function (/*iCallback*/) {
      //console.error(iCallback);
    },

    _onTextureLoaded: function (/*iCallback*/) {
      // console.log('texture loaded');
    },

    // Callback when the texture has been loaded
    apply2Dtexture: function (texture, transfoMat, renderDepth, videoData, position) {
      var model2DNode;
      var string = '';
      if (videoData) {
        string = 'model2DNode_' + this._2dMediaCounter;
        this._2dMediaCounter++;
        model2DNode = new Node3D(string);
        // affect
        //this._3DSKTContainer.addChild(model2DNode);
        this._videoModelsNode.addChild(model2DNode);
      } else {
        string = 'model2DNode_' + this._2dMediaCounter;
        this._2dMediaCounter++;
        model2DNode = new Node3D(string);
        // affect
        this._3DSKTContainer.addChild(model2DNode);
      }

      texture.flipY = false;

      // Rectangle dimensions
      var defaultRectangleValue = 100;

      // Create a quad with the geometry helper
      var geomParams = {
        width: -defaultRectangleValue,
        height: -defaultRectangleValue,
        fill: true,
        noEdge: true
      };
      var rectNode = SceneGraphFactory.createRectangleNode(geomParams);
      rectNode.setRenderPasses([this._beforeMainPass]);

      rectNode.setMatrix(new THREE.Matrix4().setPosition(new THREE.Vector3(defaultRectangleValue / 2, defaultRectangleValue / 2, 0)));
      var rotMatrix = new THREE.Matrix4().makeRotationX(Math.PI);

      model2DNode.addChild(rectNode);

      model2DNode.setMatrix(transfoMat);
      rectNode.applyMatrix(rotMatrix);

      if (texture.image.width <= 0 || texture.image.height <= 0) {
        return;
      }

      var material = new THREE.MeshBasicMaterial({
        map: texture,
        side: THREE.DoubleSide,
        force: true,
        transparent: true,
        depthTest: false
      });
      material.color = new THREE.Color(); // white

      // avoid z-fighting (if draw on the image)
      material.polygonOffset = true;
      material.polygonOffsetFactor = 0.7;

      if (videoData) {
        var matrix = model2DNode.getMatrixWorld();
        var positionArray = rectNode.mesh3js.geometry[0].vertexPositionArray;
        var bottomLeft = new THREE.Vector3(positionArray[0], positionArray[1], positionArray[2]).applyMatrix4(matrix);
        var topLeft = new THREE.Vector3(positionArray[9], positionArray[10], positionArray[11]).applyMatrix4(matrix);
        var topRight = new THREE.Vector3(positionArray[6], positionArray[7], positionArray[8]).applyMatrix4(matrix);

        this._mapVid.set(model2DNode.name, {
          video: videoData,
          position: position,
          bottomLeft: bottomLeft,
          topLeft: topLeft,
          topRight: topRight
        });
      }

      var materialApp = new MaterialApplication({
        //faceMaterial: mat
        faceMaterial: material
      });

      rectNode.setMaterialApplication(materialApp);
      rectNode.setRenderDepth(renderDepth);

      this.textureLoaded = true;
    },

    load2DModel: function (modelPath, transfoMatrixArray, renderDepth, mediaType, status /*, iCallback*/) {
      return new PromiseUWA( function (resolve) {
        if (status !== CAT3DWFileServices.STATUS_KO && status !== CAT3DWFileServices.STATUS_PROCESSING) {
          // ByPass cors issue with parameter
          var urlService = new CAT3DWUrlServices();
          modelPath = urlService.generateUrlWithSuffix(modelPath);
        }

        // proxify url for mobile app
        if (this._isMobileApp) {
          modelPath = UWA.Data.proxifyUrl(modelPath, { proxy: 'passport' });
        }

        var transfoMat = new THREE.Matrix4();
        transfoMat.setFromArray(transfoMatrixArray);

        var texture = null;

        // parent Node
        if (
          mediaType === CAT3DWFileServices.TYPE_IMAGE ||
          status === CAT3DWFileServices.STATUS_PROCESSING /*|| mediaType === CAT3DWFileServices.TYPE_DOCUMENT*/
        ) {
          // Create a texture
          var p1 = new PromiseUWA(
            function (resolveInternal /*, reject*/) {
              texture = THREE.ImageUtils.loadTexture3DWhiteboard(modelPath, undefined, this._onTextureLoaded.bind(this), this.onLoadImageError, 'use-credentials', 1);
              resolveInternal(texture);
            }.bind(this)
          );

          p1.then(
            function (iTexture) {
              this.apply2Dtexture(iTexture, transfoMat, renderDepth, null);
              resolve();
            }.bind(this)
          );
        } else if (mediaType === CAT3DWFileServices.TYPE_VIDEO) {
          // Rectangle dimensions
          var defaultRectangleValue = 100;

          var position = new THREE.Vector3(0, 0, 0);
          position.getPositionFromMatrix(transfoMat);

          var topLeft = new THREE.Vector3(position.x - defaultRectangleValue / 2, position.y + defaultRectangleValue / 2, position.z);
          var topRight = new THREE.Vector3(position.x + defaultRectangleValue / 2, position.y + defaultRectangleValue / 2, position.z);
          var bottomLeft = new THREE.Vector3(position.x - defaultRectangleValue / 2, position.y - defaultRectangleValue / 2, position.z);

          var _videoData;

          var p2 = new PromiseUWA(
            function (resolveInternal /*, reject*/) {
              this._videoController.viewSettings = {
                _beforeMainPass: this._beforeMainPass,
                getBeforeMainPass: function () {
                  return this._beforeMainPass;
                }
              };

              this._videoController.createVideo({
                videoNode: this._videoModelsNode,
                urlOrTexture: modelPath,
                viewer: this._viewer,
                controlPosition: position,
                renderDepth: renderDepth + 1,
                videoDimension: new THREE.Vector2(
                  this._viewer.currentViewpoint.project(topLeft).distanceTo(this._viewer.currentViewpoint.project(topRight)),
                  this._viewer.currentViewpoint.project(topLeft).distanceTo(this._viewer.currentViewpoint.project(bottomLeft))
                )
              }).then(function (videoData) {
                _videoData = videoData;
                resolveInternal(_videoData);
              });
            }.bind(this)
          );

          p2.then(
            function (iVideoData) {
              texture = iVideoData.getTexture();
              this.apply2Dtexture(texture, transfoMat, renderDepth, iVideoData, position);
              //setTimeout(this.apply2Dtexture.bind(this,texture, transfoMat, iVideoData),3000)
              resolve();
            }.bind(this)
          );
        }
      }.bind(this));
    },

    drawSketch: function (jsonStr) {
      var parsedJSON = JSON.parse(jsonStr);
      this._version = parsedJSON.ver;

      this._3DModelsNode = new Node3D('3DModels');
      // affect
      this._3DSKTContainer.addChild(this._3DModelsNode);

      this._videoModelsNode = new Node3D('VideoModels');
      this._3DSKTContainer.addChild(this._videoModelsNode);

      return new PromiseUWA(
        function (resolve /*, reject*/) {
          // var annotJSON = JSON.parse(jsonStr);
          if (parsedJSON.ver) {
            var annnotAbbreviation = new CAT3DSKAnnotationAbbreviations();
            var unObfuscatedJSON = annnotAbbreviation.processJSON(parsedJSON, 'expand');
            var explodeStateList = unObfuscatedJSON.explodeStates;
            for (var es = 0; es < explodeStateList.length; es++) {
              if (explodeStateList[es].id === 0) {
                var drawPlaneList = explodeStateList[es].drawPlanes;
                for (var i = 0; i < drawPlaneList.length; i++) {
                  var drawPlane = drawPlaneList[i];
                  //var annotList = drawPlane.annotList;
                  // Test if there is any visible annotation in the drawplane
                  // If not, just don't create it
                  var annotList = drawPlane.annotList.filter(function (annot) {
                    return annot.isVisible;
                  });
                  if (annotList.length) {
                    var listPoints = null;
                    for (var j = 0; j < annotList.length; j++) {
                      switch (annotList[j].type) {
                      case 'circle':
                        listPoints = this.getListPointFromCircle(annotList[j], drawPlane.drawPlaneInfo.normal);
                        break;
                      case 'ellipse':
                        listPoints = this.getListPointFromEllipse(annotList[j]);
                        break;
                      case 'line':
                        listPoints = this.getListPointFromLine(annotList[j]);
                        break;
                      case 'rectangle':
                        listPoints = this.getListPointFromRectangle(annotList[j]);
                        break;
                      case 'triangle':
                        listPoints = this.getListPointFromTriangle(annotList[j]);
                        break;
                      case 'unknownOpen':
                      case 'unknownClosed':
                        listPoints = this.getListPointFromUnknown(annotList[j]);
                        break;
                      case 'point':
                        listPoints = [new THREE.Vector3(annotList[j].annotStartPoint.x, annotList[j].annotStartPoint.y, annotList[j].annotStartPoint.z)];
                        break;
                      default:
                        listPoints = this.getListPointFromUnknown(annotList[j]);
                        break;
                      }
                      this.draw(listPoints, annotList[j].type, annotList[j].graphicalProp);
                    }
                  }
                }
              }
            }
            resolve(unObfuscatedJSON);
            return;
          }
          resolve(parsedJSON);
        }.bind(this)
      );
    },

    getMedia: function (parsedJSON) {
      var textboxes = parsedJSON.Textboxes;

      // Array medias data (mediaId + associatedData)
      var mediasData = [];

      // Process media 2D
      if (parsedJSON.ExternalRef && parsedJSON.ExternalRef.media2D && parsedJSON.ExternalRef.media2D.length) {
        for (let i = 0; i < parsedJSON.ExternalRef.media2D.length; i++) {
          mediasData.push({
            mediaId: parsedJSON.ExternalRef.media2D[i].mediaId,
            associatedData: {
              matrix: parsedJSON.ExternalRef.media2D[i].matrix,
              renderDepth: parsedJSON.ExternalRef.media2D[i].renderDepth
            }
          });
        }
      }

      // Process media 3D
      if (parsedJSON.ExternalRef && parsedJSON.ExternalRef.media3D && parsedJSON.ExternalRef.media3D.length) {
        for (let j = 0; j < parsedJSON.ExternalRef.media3D.length; j++) {
          mediasData.push({
            mediaId: parsedJSON.ExternalRef.media3D[j].mediaId,
            associatedData: {
              matrix: parsedJSON.ExternalRef.media3D[j].matrix
            }
          });
        }
      }

      // Process texts
      if (textboxes && textboxes.length > 0) {
        for (var index = 0; index < textboxes.length; index++) {
          this.loadText(textboxes[index]);
        }
      }

      if (!mediasData.length) {
        return new PromiseUWA(function (resolve) {
          resolve(parsedJSON);
        });
      }

      return this._swymMediaServices
        .getMedias(
          mediasData,
          function (iPercentValue) {
            Mask.mask(this._mask, 'Loading ' + iPercentValue + '%');
          }.bind(this)
        )
        .then(
          async function (iMediasData) {
            CAT3DWHandleErrors.initialize();
            var transfoMatrixArray = null;
            var renderDepth = 0;
            var mediaNotProcessedList = '';
            var nbMediaNotAvailable = 0;
            var nbMediaNotProcessed = 0;
            let promiseArray = [];
            for (let i = 0; i < iMediasData.length; i++) {
              if (iMediasData[i].status === CAT3DWFileServices.STATUS_PROCESSING && iMediasData[i].type) {
                mediaNotProcessedList += iMediasData[i].title + ' ';
                nbMediaNotProcessed++;
              }

              if (iMediasData[i].status === CAT3DWFileServices.STATUS_KO && iMediasData[i].type) {
                nbMediaNotAvailable++;
              }

              if (iMediasData[i].format === CAT3DWFileServices.FORMAT_2D || iMediasData[i].status === CAT3DWFileServices.STATUS_PROCESSING) {
                transfoMatrixArray = JSON.parse(iMediasData[i].associatedData.matrix);
                renderDepth = iMediasData[i].associatedData.renderDepth * 2;
                promiseArray.push(this.load2DModel(iMediasData[i].url, transfoMatrixArray['elements'], renderDepth, iMediasData[i].type, iMediasData[i].status));
              } else if (iMediasData[i].format === CAT3DWFileServices.FORMAT_3D) {
                transfoMatrixArray = JSON.parse(iMediasData[i].associatedData.matrix);
                promiseArray.push(this.load3DModel(iMediasData[i].extension, iMediasData[i].url, transfoMatrixArray['elements']));
              }
            }
            await Promise.all(promiseArray).then(() => {;
              //this._viewer.render();  
            });
            if (nbMediaNotProcessed)
                CAT3DWHandleErrors.setGeneralErrorMessage(
                  nbMediaNotProcessed + ' media not yet processed. Try reloading when all medias are processed',
                  '',
                  mediaNotProcessedList,
                  'warning'
                );
              if (nbMediaNotAvailable) {
                CAT3DWHandleErrors.setGeneralErrorMessage(nbMediaNotAvailable + ' media not available. Check your access rights', '', '', 'warning');
              }
            return parsedJSON;        
          }.bind(this)
        ).catch(error => {
              if (error) console.log(error);
            });
    },

    // Text services
    loadText: function (textbox) {
      var stringArray = textbox.matrix;
      var transfoMatrixArray = JSON.parse(stringArray);
      var transfoMat = new THREE.Matrix4();
      transfoMat.setFromArray(transfoMatrixArray['elements']);

      var model2DNode;
      var string = '';
      string = 'model2DNode_' + this._2dMediaCounter;
      this._2dMediaCounter++;
      model2DNode = new Node3D(string);
      this._3DSKTContainer.addChild(model2DNode);
      var textNode = this.makeTextNode(transfoMat, model2DNode, textbox.renderDepth * 2);
      textNode.isURL = false; // flag to know if text is a url

      // push textNode in Map
      this._mapText.set(string, { textnode: textNode, textbox: textbox });

      // Compute texture (render text to canvas and then apply texture from canvas)
      if (this._version === '1.0') {
        var canvas = this.makeTextCanvas(
          textbox.inputDimensions.height,
          textbox.inputDimensions.width,
          textbox.text,
          textbox.color,
          textbox.fontSize,
          textbox.lineHeight,
          textbox.ratio,
          textbox.backgroundColor
        );

        this.setTexture(canvas, textNode);
        this._viewer.render();
      } else {
        // current data model version is 2.0
        // text will be sharp when the font size displayed on the screen is 66px (default font size)
        this._redrawText(textNode, textbox);
      }

      // a sticky note cannot be a link
      if (textbox.backgroundColor) {
        return;
      }

      // is the text a URL?
      var urlService = new CAT3DWUrlServices();
      var isURL = urlService.isValidHttpUrl(textbox.text.trim());
      if (isURL) {
        // This code should be shared by authoring app
        // meanwhile, doing the same fix for trailing newlines
        var url = textbox.text.trim();
        if (!(url.startsWith('http://') || url.startsWith('https://'))) {
          url = 'https://' + url;
        }
        textNode.url = url;
        textNode.setVisibility(false);
        CAT3DWRichLinkPreviewGenerator.createPreview(url).then(
          function (view) {
            if (view) {
              // link is valid
              this._textRenderService.renderLinkToCanvas(view).then(
                function (richLinkCanvas) {
                  textNode.isURL = true;
                  this.setTexture(richLinkCanvas, textNode);
                  textNode.setVisibility(true);
                  this._viewer.render();
                }.bind(this)
              );
            } else {
              textNode.setVisibility(true);
            }
          }.bind(this)
        );
      }
    },

    drawConnections: function (parsedJSON) {
      return new PromiseUWA(function (resolve) {
        var connections = parsedJSON.connections;
        if (!connections || connections.length === 0) {
          resolve(parsedJSON);
        }
        var objects = parsedJSON.explodeStates[0].drawPlanes[0].annotList;
        var media2D = parsedJSON.ExternalRef.media2D;
        if (media2D)
          media2D.forEach(e => objects.push(e));
        var media3D = parsedJSON.ExternalRef.media3D;
        if (media3D)
          media3D.forEach(e => objects.push(e));
        var texts = parsedJSON.Textboxes;
        if (texts)
          texts.forEach(e => objects.push(e));

        var getObjectByID = function (objectID) {
          for (var i = 0; i < objects.length; i++) {
            if (objects[i].id === objectID)
              return objects[i];
          }
        };

        var getPortPositionByMatrix = function (matrix, portPosition) {

          var transfoMatrixArray = JSON.parse(matrix);
          var transfoMat = new THREE.Matrix4();
          transfoMat.setFromArray(transfoMatrixArray['elements']);

          switch (portPosition) {
          case 'top':
            var top = new THREE.Vector3(0, -50, 0);
            return top.applyMatrix4(transfoMat);
          case 'bottom':
            var bottom = new THREE.Vector3(0, 50, 0);
            return  bottom.applyMatrix4(transfoMat);
          case 'left':
            var left = new THREE.Vector3(50, 0, 0);
            return left.applyMatrix4(transfoMat);
          case 'right':
            var right = new THREE.Vector3(-50, 0, 0);
            return right.applyMatrix4(transfoMat);
          }
        };

        var getPortPositionByPoints = function (object, portPosition) {

          let pts = object.geometry;
          let offsetThickness = object.graphicalProp.thickness / 2;

          //cas Cercle
          if (object.type === 'circle') {
            switch (portPosition) {
            case 'top':
              return new THREE.Vector2(pts.center.x - pts.radius - offsetThickness, pts.center.y);
            case 'bottom':
              return new THREE.Vector2(pts.center.x + pts.radius + offsetThickness, pts.center.y);
            case 'left':
              return new THREE.Vector2(pts.center.x, pts.center.y - pts.radius - offsetThickness);
            case 'right':
              return new THREE.Vector2(pts.center.x, pts.center.y + pts.radius + offsetThickness);
            }
          }

          var position = {
            top: Number.POSITIVE_INFINITY,
            bottom: Number.NEGATIVE_INFINITY,
            left: Number.POSITIVE_INFINITY,
            right: Number.NEGATIVE_INFINITY
          };

          var points = [];

          switch (object.type) {
          case 'unknownOpen':
          case 'unknownClosed':
          case 'point':
            points = pts.points;
            break;
          case 'line':
            points.push(pts.startPoint);
            points.push(pts.endPoint);
            break;
          case 'rectangle':
            points.push(pts.vertexA);
            points.push(pts.vertexB);
            points.push(pts.vertexC);
            points.push(pts.vertexD);
            break;
          case 'triangle':
            points.push(pts.vertexA);
            points.push(pts.vertexB);
            points.push(pts.vertexC);
            break;
          case 'ellipse':
            var center = new THREE.Vector3(pts.center.x, pts.center.y, pts.center.z);
            var dx = new THREE.Vector3(pts.majorAxis.dir.x, pts.majorAxis.dir.y, pts.majorAxis.dir.z);
            var dy = new THREE.Vector3(pts.minorAxis.dir.x, pts.minorAxis.dir.y, pts.minorAxis.dir.z);
            var step = (2 * Math.PI) / object.pointsCount;
            for (var theta = 0; theta < 2 * Math.PI; theta += step) {
              var x = center.x + pts.majorAxis.length * Math.cos(theta) * dx.x + pts.minorAxis.length * Math.sin(theta) * dy.x;
              var y = center.y + pts.majorAxis.length * Math.cos(theta) * dx.y + pts.minorAxis.length * Math.sin(theta) * dy.y;
              var z = center.z + pts.majorAxis.length * Math.cos(theta) * dx.z + pts.minorAxis.length * Math.sin(theta) * dy.z;
              points.push(new THREE.Vector3(x, y, z));
            }
          }

          points.forEach(e => {
            if (e.x < position.top)
              position.top = e.x;
            if (e.x > position.bottom)
              position.bottom = e.x;
            if (e.y < position.left)
              position.left = e.y;
            if (e.y > position.right)
              position.right = e.y;
          });

          switch (portPosition) {
          case 'top':
            return new THREE.Vector2(position.top - offsetThickness, (position.left + position.right) / 2);
          case 'bottom':
            return new THREE.Vector2(position.bottom + offsetThickness, (position.left + position.right) / 2);
          case 'left':
            return new THREE.Vector2((position.top + position.bottom) / 2, position.left  - offsetThickness);
          case 'right':
            return new THREE.Vector2((position.top + position.bottom) / 2, position.right  + offsetThickness);
          }

        };

        for (var i = 0; i < connections.length; i++) {
          var objectA = getObjectByID(connections[i].connectionPointA.pointedObjectID);
          var objectB = getObjectByID(connections[i].connectionPointB.pointedObjectID);

          if (!objectA || !objectB)
            continue;

          let vPointA, vPointB;

          let vPortPosA = connections[i].connectionPointA.portPosition;
          let vPortPosB = connections[i].connectionPointB.portPosition;

          if (objectA.matrix)
            vPointA = getPortPositionByMatrix(objectA.matrix, vPortPosA);
          else if (objectA.geometry)
            vPointA = getPortPositionByPoints(objectA, vPortPosA);
          else
            continue;

          if (objectB.matrix)
            vPointB = getPortPositionByMatrix(objectB.matrix, vPortPosB);
          else if (objectB.geometry)
            vPointB = getPortPositionByPoints(objectB, vPortPosB);
          else
            continue;

          if (!vPointA || !vPointB)
            continue;

          var points = [];
          var controlPoint;

          if (connections[i].controlPoint)
            controlPoint = new THREE.Vector2(connections[i].controlPoint.x, connections[i].controlPoint.y);
          else {
            let iAControlPoint = CAT3DWGeomFactory.GetControlPoint(vPointA, vPortPosA, vPointB);
            let iBControlPoint = CAT3DWGeomFactory.GetControlPoint(vPointB, vPortPosB, vPointA);
            controlPoint = new THREE.Vector2((iAControlPoint.x + iBControlPoint.x) / 2, (iBControlPoint.y + iAControlPoint.y) / 2);
          }

          if (!controlPoint)
            continue;

          points.push(vPointA);
          points.push(controlPoint);
          points.push(vPointB);

          var vBezierPoints = CAT3DWGeomFactory.ComputeBezierCurve(points, 0.01, vPortPosA, vPortPosB);

          var graphicalProperties = {
            'color': '#000000',
            'opacity': 1,
            'thickness': 1
          };

          this.draw(vBezierPoints, 'unknownOpen', graphicalProperties);
        }

        resolve(parsedJSON);

      }.bind(this));
    },

    //--------------------------------------------------------
    // Text SERVICES
    //--------------------------------------------------------

    _redrawText: function (iTextNode, iTextBox) {
      if (this._version === '1.0')
        // in 1.0 version text texture is not updated
        return;

      var defaultFontSize = CAT3DWTextCommonConstants.defaultFontSize;
      var defaultLineHeight = CAT3DWTextCommonConstants.defaultLineHeight;
      // get default padding : if it is a sticky note margins are higher
      var defaultPadding = iTextBox.backgroundColor ? CAT3DWTextCommonConstants.defaultStickyNotePadding : CAT3DWTextCommonConstants.defaultTextPadding;

      // compute scaling factor (ratio between the real size in pixel of the text and the default input size of the text)
      var textMatrix = new THREE.Matrix4().copy(iTextNode.getMatrixWorld());
      var bottomLeft = new THREE.Vector3(50, 50, 0);
      var topLeft = new THREE.Vector3(50, -50, 0);
      bottomLeft.applyMatrix4(textMatrix);
      topLeft.applyMatrix4(textMatrix);
      var topLeft2D = this._viewer.currentViewpoint.project(topLeft);
      var bottomLeft2D = this._viewer.currentViewpoint.project(bottomLeft);
      var realHeightInPixel = topLeft2D.distanceTo(bottomLeft2D);
      var scalingFactor = realHeightInPixel / iTextBox.inputDimensions.height;

      var desiredWidth = iTextBox.inputDimensions.width * scalingFactor;
      var desiredHeight = iTextBox.inputDimensions.height * scalingFactor;
      var desiredFontSize = defaultFontSize * scalingFactor;
      var desiredLineHeight = defaultLineHeight * scalingFactor;

      var desiredPadding = {
        top: defaultPadding.top * scalingFactor,
        bottom: defaultPadding.bottom * scalingFactor,
        left: defaultPadding.left * scalingFactor,
        right: defaultPadding.right * scalingFactor
      };

      
      var canvas = this._textRenderService.renderTextToCanvas(
        desiredWidth,
        desiredHeight,
        iTextBox.text,
        desiredLineHeight,
        desiredFontSize,
        desiredPadding,
        iTextBox.color,
        iTextBox.backgroundColor
      );

      this.setTexture(canvas, iTextNode);
      this._viewer.render();
    },

    // compatibility with version 1.0
    getMaxCanvasWidth: function (height, width, name, fontSize, lineHeight, background) {
      var canvas = document.createElement('canvas');
      canvas.height = height;
      canvas.width = width;
      var maxCTXWidth = 0;
      var ctx = canvas.getContext('2d');
      ctx.textBaseline = 'top';
      ctx.font = fontSize ? `${fontSize} GochiHand` : '22px GochiHand';
      var lineheight = lineHeight ? lineHeight : 30;
      var lines = name.split('\n');
      for (var i = 0; i < lines.length; i++) {
        if (ctx.measureText(lines[i]).actualBoundingBoxRight > maxCTXWidth) {
          maxCTXWidth = ctx.measureText(lines[i]).actualBoundingBoxRight;
        }
      }
      var stickyNote = background && background !== '';
      var totalHeight = parseFloat(lineheight, 10) * lines.length;
      if (stickyNote) {
        //Canvas should be atleast sticky note minimum dimensions
        maxCTXWidth = maxCTXWidth > width ? maxCTXWidth : width;
        totalHeight = totalHeight > height ? totalHeight : height;
      }
      return { width: maxCTXWidth + 10, height: totalHeight };
    },

    // compatibility with version 1.0
    makeTextCanvas: function (height, width, name, color, fontSize, lineHeight, ratio, background) {
      var dimensions = this.getMaxCanvasWidth(height, width, name, fontSize, lineHeight, background);
      var textRatio = ratio ? ratio : 1;
      var canvas = document.createElement('canvas');
      canvas.height = dimensions.height;
      canvas.width = dimensions.width;
      var maxCTXWidth = 0;
      var ctx = canvas.getContext('2d');
      ctx.textBaseline = 'top';
      ctx.font = fontSize ? `${fontSize} GochiHand` : '22px GochiHand';

      var x = 5;
      // var y = 30;
      var lineheight = lineHeight ? lineHeight : 30;
      if (background) {
        ctx.fillStyle = background;
        ctx.fillRect(0, 0, canvas.width, canvas.height);
      }
      ctx.fillStyle = color ? color : 'black';
      var lines = name.split('\n');
      for (var i = 0; i < lines.length; i++) {
        if (ctx.measureText(lines[i]).actualBoundingBoxRight > maxCTXWidth) {
          maxCTXWidth = ctx.measureText(lines[i]).actualBoundingBoxRight;
        }
        if (i === 0) {
          ctx.fillText(lines[i], x, 5 * textRatio);
        } else {
          ctx.fillText(lines[i], x, 5 * textRatio + parseInt(lineheight, 10) * i);
        }
      }
      return ctx.canvas;
    },

    makeTextNode: function (transfoMat, model2DNode, renderDepth) {
      var rectangle = SceneGraphFactory.createRectangleNode({
        width: -100,
        height: -100,
        fill: true,
        color: new Mesh.Color(255, 255, 255, 0)
      });
      rectangle.setRenderPasses([this._beforeMainPass]);
      rectangle.setMatrix(new THREE.Matrix4().setPosition(new THREE.Vector3(100 / 2, 100 / 2, 0)));
      if (renderDepth !== undefined && renderDepth !== null) {
        rectangle.setRenderDepth(renderDepth);
      }
      model2DNode.addChild(rectangle);
      model2DNode.setMatrix(transfoMat);
      return rectangle;
    },

    setTexture: function (canvas, node) {
      var materialApp = new MaterialApplication({
        faceMaterial: this.getTextMaterial(canvas)
      });
      node.setMaterialApplication(materialApp);
    },

    getTextMaterial: function (canvas) {
      var texture = new THREE.Texture(
        canvas,
        undefined,
        THREE.ClampToEdgeWrapping,
        THREE.ClampToEdgeWrapping,
        THREE.LinearFilter,
        THREE.LinearMipMapLinearFilter
      );
      texture.anisotropy = 16;
      texture.needsUpdate = true;
      var material = new THREE.MeshBasicMaterial({
        map: texture,
        force: true,
        side: THREE.DoubleSide
      });
      // Transparence = true for new CAT3DWTextView without text setted
      material.transparent = true;
      material.color = new THREE.Color(); // white
      // avoid z-fighting (if draw on the text)
      material.polygonOffset = true;
      material.polygonOffsetFactor = 0.7;
      return material;
    },

    loadTextboxFont: function (parsedJSON) {
      return new PromiseUWA(function (resolve /*, reject*/) {
        var fontUrl = WebappsUtils.getWebappsAssetUrl('ModelLoader_3dwb', 'fonts/GochiHand-Regular.ttf');
        var myfont = null;
        if (CAT3DWPreferenceManager.getPreferenceValue('M3DEMobile')) {
          var proxifiedUrl = UWA.Data.proxifyUrl(fontUrl, {
            proxy: 'passport'
          });
          myfont = new FontFace('GochiHand', 'url(' + proxifiedUrl + ') format("woff2")');
        } else {
          myfont = new FontFace('GochiHand', 'url(' + fontUrl + ') format("woff2")');
        }

        myfont.load().then(function () {
          document.fonts.add(myfont);
          resolve(parsedJSON);
        });
      });
    },

    // eslint-disable-next-line no-unused-vars
    load: function (iData, onReady, onProgress, onLoaded) {
      this._3DSKTContainer = new Node3D('3DSKTContainer');
      this._annotContainer = new Node3D('annotContainer');
      this._3DSKTContainer.addChild(this._annotContainer);
      this._3DSKTContainer.setVisibility(false);
      const viewerContainer = iData.viewer.div;

      this._viewer = iData.viewer;
      this._maskContainer = UWA.createElement('div', {
        styles: {
          opacity: 0.7,
          pointerEvents: 'none',
          position: 'absolute',
          top: '0',
          width: '100%',
          height: '100%',
          zIndex: 10
        }
      }).inject(viewerContainer);
      this._mask = UWA.createElement('div', {
        styles: {
          position: 'absolute',
          top: '0',
          width: '100%',
          height: '100%'
        }
      }).inject(this._maskContainer);
      Mask.mask(this._mask, 'Loading'); // mask over the viewer

      var platformParam = {};
      if (iData.requestsOptions && iData.requestsOptions.serverurl) {
        platformParam.platformUrl = iData.requestsOptions.serverurl;
      } else {
        platformParam.platformId = PlatformAPI.getTenant();
        /* ## */
        // Deprecated : to be removed
        try {
          if (!platformParam.platformId && parent.ds && parent.ds.swymTenant) {
            platformParam.platformId = parent.ds.swymTenant;
          }
        } catch (error) {
          // console.error(error);
        }
        /* ## */
      }
      if (platformParam && platformParam.platformId) {
        CAT3DWPreferenceManager.setPreferenceValue('swymTenant', platformParam.platformId);
      }

      this._swymMediaServices = new CAT3DW3DSwymMediaServices(platformParam);
      this._initialCameraDistance = this._viewer.currentViewpoint.getTargetDistance(); // save initial position of the camera

      // Retrieve the pass manager
      var passManager = this._viewer.getUserPassManager();
      // Create the render list
      this._beforeMainPass = new UserRenderList('BeforeMain');
      // Create a clear pass with setClearDepth set to true
      var clearPass = passManager.createUserClearPass('userClearPass', this._beforeMainPass);
      clearPass.setClearDepth(true);
      // Create a render pass
      var renderPass = passManager.createUserRenderPass('userRenderPass', this._beforeMainPass);
      // Insert the passes before the "main" pass
      var mainPass = passManager.getSystemPass('main');
      passManager.insertUserPassBeforePass(clearPass, mainPass);
      passManager.insertUserPassBeforePass(renderPass, clearPass);

      this.readJsonFile(iData.filename)
        .then(this.unzip.bind(this))
        .then(this.drawSketch.bind(this))
        .then(this.loadTextboxFont.bind(this))
        .then(this.getMedia.bind(this))
        .then(this.drawConnections.bind(this))
        .then(
          function () {            
            Mask.unmask(this._mask);
            this._3DSKTContainer.setVisibility(true);
            this._viewer.render();
            viewerContainer.removeChild(this._maskContainer);
            this._mask = null;
            this._maskContainer = null;            
            onReady(this._3DSKTContainer);
            onLoaded(this._3DSKTContainer);

            TrackerAPI.trackPageEvent({
              eventCategory: '3DWhiteBoard_Loader',
              eventAction: '3DWhiteBoard_LoadVisu',
              eventLabel: 'Load visu of sketch in other apps',
              appID: 'CAT3DSKT_AP'
            });
          }.bind(this)
        )
        .catch(this._error);

      //set camera position!!

      //var normal = new THREE.Vector3(0, 0, -1)

      //this._viewpoint.moveTo({ sightDirection: normal });
    },

    _error: function (err) {
      console.error(err);
    }
  });
});
