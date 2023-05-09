/*
*/

define('DS/ModelLoader_3dskt/loader',
[
  'UWA/Core'
  , 'UWA/Promise'
  , 'DS/SceneGraphNodes/FixedSizeNode3D'
  , 'DS/ZipJS/zip'
  , 'DS/Visualization/SceneGraphFactory'
  , 'DS/CAT3DWVisu/CAT3DWThreeJsExtender'
  , 'DS/Visualization/ThreeJS_DS'
  , 'DS/Visualization/Node3D'
  , 'DS/CAT3DSKExperience/CAT3DSKAnnotationAbbreviationsExperience'
  , 'DS/WAFData/WAFData'
  , 'DS/Visualization/ModelLoader'
  , 'DS/Usage/TrackerAPI'
  , 'DS/Plugins/UserRenderList'
  , 'DS/UIKIT/Mask'
  , 'DS/PlatformAPI/PlatformAPI'
  , 'DS/CAT3DWInfra/CAT3DW3DSwymMediaServices'
  , 'DS/CAT3DWInfraUI/CAT3DWHandleErrors'
  , 'DS/Visualization/MaterialApplication'
  , 'DS/CAT3DWInfra/CAT3DWUrlServices'
  , 'DS/Visualization/Ambience'
  , 'DS/CAT3DWInfra/CAT3DWPreferenceManager'
  , 'DS/CAT3DWInfra/CAT3DWFileServices'
],
function (
  UWA
  , PromiseUWA
  , FixedSizeNode3D
  , zip
  , SceneGraphFactory
  , THREE_Extended
  , THREE
  , Node3D
  , CAT3DSKAnnotationAbbreviations
  , WAFData
  , ModelLoader
  , TrackerAPI
  , UserRenderList
  , Mask
  , PlatformAPI
  , CAT3DW3DSwymMediaServices
  , CAT3DWHandleErrors
  , MaterialApplication
  , CAT3DWUrlServices
  , Ambience
  , CAT3DWPreferenceManager
  , CAT3DWFileServices
) {
  'use strict';
  return UWA.Class.extend({

    init: function (options) {
      this._parent(options);
      this._3dMediaCounter = 0;
      this._2dMediaCounter = 0;

      this.isR2V = null;
      this.texture = [];

      this._isMobileApp = false;
      if (window && window.ds && window.ds.env) {
        if (window.ds.env === 'MOBILE') {
          this._isMobileApp = true;
        }
      }
    },

    unzip: function (zipFile) {
      return new PromiseUWA(function (resolve/*, reject*/) {
        var blobReader = new zip.BlobReader(zipFile);
        zip.createReader(blobReader, function (reader) {
          var zipReader = reader;
          zipReader.getEntries(function (entries) {
            var entry = entries.find(function (element) { return element.filename === 'sketch.json'; });
            if (entry) {
              entry.getData(new zip.TextWriter(), function (data) {
                var jsonStr = data.target.result;
                zipReader.close(function (/*blob*/) {
                  resolve(jsonStr);
                });
              });
            }
          });
        }, function () {
          console.log('zip error');
        });
      });
    },

    readJsonFile: function (file) {
      return new PromiseUWA(function (resolve, reject) {
        // Getting the data using WAFData
        var reqOptions = {
          'proxy': 'none'
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
      var dx = ((new THREE.Vector3).subVectors(startPoint, center)).normalize();
      var dy = ((new THREE.Vector3).crossVectors(dx, circlePlaneNormal)).normalize();
      var step = (2 * Math.PI) / annotJSON.pointsCount;
      for (var theta = 0; theta < 2 * Math.PI; theta += step) {
        var x = center.x + (radius * Math.cos(theta) * dx.x) + (radius * Math.sin(theta) * dy.x);
        var y = center.y + (radius * Math.cos(theta) * dx.y) + (radius * Math.sin(theta) * dy.y);
        var z = center.z + (radius * Math.cos(theta) * dx.z) + (radius * Math.sin(theta) * dy.z);
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
        var x = center.x + (majorAxisLength * Math.cos(theta) * dx.x) + (minorAxisLength * Math.sin(theta) * dy.x);
        var y = center.y + (majorAxisLength * Math.cos(theta) * dx.y) + (minorAxisLength * Math.sin(theta) * dy.y);
        var z = center.z + (majorAxisLength * Math.cos(theta) * dx.z) + (minorAxisLength * Math.sin(theta) * dy.z);
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
      var lineMaterial = this._getLineMaterial(graphicalProp);
      if ('point' === type) { // If point draw Point
        this.drawPoint(listPoint, graphicalProp);
        return;
      }

      var lineParams = {
        points: listPoint
        //drawMode: Mesh.ConnectivityTypeEnum.LINES
      };

      var splineNode = SceneGraphFactory.createLineNode(lineParams);
      splineNode.setName('3DSketch_Annot');
      splineNode.oldSetMaterial(lineMaterial);
      this._annotContainer.add(splineNode);

      if (type === 'line') {
        var fixedSizeNode = new FixedSizeNode3D({ name: '3DSketch_Linecap', ratio: 1.0 });
        this._annotContainer.addChild(fixedSizeNode);

        fixedSizeNode.setMatrix(new THREE.Matrix4().setPosition(listPoint[0].clone()));

        var sphereMaterial = this._getSphereMaterial(graphicalProp);

        var cap = SceneGraphFactory.createSphereNode({ sag: 3, radius: graphicalProp.thickness / 2, fill: true, material: sphereMaterial });
        this._applyOpacityStencilOp(cap, graphicalProp);
        fixedSizeNode.add(cap);
      }
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

      var fixedSizeNode = new FixedSizeNode3D({ name: 'CAT3DSketch-PointNode', ratio: 1.0 });
      this._annotContainer.addChild(fixedSizeNode);
      fixedSizeNode.setMatrix(new THREE.Matrix4().setPosition(listPoint[0]));

      var splineNode = SceneGraphFactory.createSphereNode({ sag: 20, radius: graphicalProp.thickness * 0.5, material: circleMaterial });
      fixedSizeNode.add(splineNode);

      this._applyOpacityStencilOp(splineNode, graphicalProp);
    },

    load3DModel: function (modelPath, transfoMatrixArray) {
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
      var model3DNode = new Node3D(string);
      // affect
      this._3DSKTContainer.addChild(model3DNode);

      modelLoader.setOnLoadedCallback(() => {

        // Moving the children to make the model position match the bbox center
        var center = model3DNode.getBoundingBox().center();
        var matrix = new THREE.Matrix4();
        matrix.makeTranslation(-center.x, -center.y, -center.z);
        for (var i = 0; i < model3DNode.children.length; i++) {
          model3DNode.children[i].setMatrix(matrix);
        }
        this._viewer.render();
      });

      modelLoader.setOnErrorCallback(function (err) {
        console.error(err);
      });

      modelLoader.loadModel({
        filename: modelPath,
        proxyurl: 'none',
        withCredentials: true
      }, model3DNode);

      if (transfoMatrixArray) {

        var transfoMat = new THREE.Matrix4();

        transfoMat.setFromArray(transfoMatrixArray);

        model3DNode.setMatrix(transfoMat);
      }
    },

    _onTextureLoaded: function (i, transfoMat) {
      this.apply2Dtexture(this.texture[i], transfoMat, null);
    },

    onLoadImageError: function () {
    },

    drawSketch: function (jsonStr) {
      var annotJSON = JSON.parse(jsonStr);

      //check if file is a R2V project --> disabled until we fix misalignement with background issue
      this.isR2V = annotJSON.isR2V;

      CAT3DWPreferenceManager.setPreferenceValue('isR2V', this.isR2V);

      return new PromiseUWA(function (resolve /*, reject*/) {
        // var annotJSON = JSON.parse(jsonStr);
        if (annotJSON.ver) {
          var annnotAbbreviation = new CAT3DSKAnnotationAbbreviations();
          var unObfuscatedJSON = annnotAbbreviation.processJSON(annotJSON, 'expand');
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
        }
        resolve(annotJSON.ExternalRef);
      }.bind(this));
    },

    // Callback when the texture has been loaded
    apply2Dtexture: function (texture, transfoMat, videoData) {

      var model2DNode;
      var string = '';
      if (videoData) {
        string = 'model2DNode_' + this._2dMediaCounter;
        this._2dMediaCounter++;
        model2DNode = new Node3D(string);
        // affect
        //this._3DSKTContainer.addChild(model2DNode);
        this.VideoModelsNode.addChild(model2DNode);
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

      if (texture.image.width <= 0 || texture.image.height <= 0)
        return;

      var mat = THREE.MaterialUtils.createMatteFaceMaterial({
        map: texture,
        side: THREE.DoubleSide,
        force: true,
        transparent: true,
        depthTest: false
      });

      if (videoData) {
        this.mapVid.set(model2DNode.name, videoData);
      }

      var materialApp = new MaterialApplication({
        faceMaterial: mat
      });

      rectNode.setMaterialApplication(materialApp);

      this.textureLoaded = true;

    },

    load2DModel: function (modelPath, transfoMatrixArray, mediaType, status /*, iCallback*/) {

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

      this.texture.push(null);

      var i = this.texture.length - 1;

      // parent Node
      if (mediaType === CAT3DWFileServices.TYPE_IMAGE || status === CAT3DWFileServices.STATUS_PROCESSING/*|| mediaType === CAT3DWFileServices.TYPE_DOCUMENT*/) {

        // Create a texture
        //var p1 = new PromiseUWA(function (resolve/*, reject*/) {


        this.texture[i] = THREE.ImageUtils.loadTexture3DWhiteboard(modelPath, undefined, this._onTextureLoaded.bind(this, i, transfoMat), this.onLoadImageError, 'use-credentials', 1);

        //}.bind(this));

      }

    },

    getMedias: function (iMedias) {

      // Array medias data (media id + associated data)
      var mediasData = [];

      // Process media 3D
      if (iMedias && iMedias.media3D && iMedias.media3D.length) {
        for (var j = 0; j < iMedias.media3D.length; j++) {
          mediasData.push({
            mediaId: iMedias.media3D[j].mediaId,
            associatedData: {
              id: iMedias.media3D[j].id,
              matrix: iMedias.media3D[j].matrix
            }
          });
        }
      }

      // Process media (old data model)
      if (iMedias && iMedias.mediaId) {
        mediasData.push({
          mediaId: iMedias.mediaId,
          associatedData: {
            id: iMedias.id,
            matrix: null
          }
        });
      }

      // Process media (R2V mode
      if (iMedias && iMedias.media2D && iMedias.media2D.length && this.isR2V === true) {
        for (var j = 0; j < iMedias.media2D.length; j++) {
          mediasData.push({
            mediaId: iMedias.media2D[j].mediaId,
            associatedData: {
              id: iMedias.media2D[j].id,
              matrix: iMedias.media2D[j].matrix,
              background: iMedias.media2D[j].background
            }
          });
          if (iMedias.media2D[j].background === true) {
            CAT3DWPreferenceManager.setPreferenceValue('camPosR2V', iMedias.media2D[j].R2VCamera.camPos);
            CAT3DWPreferenceManager.setPreferenceValue('quatR2V', iMedias.media2D[j].R2VCamera.quat);
            CAT3DWPreferenceManager.setPreferenceValue('fovR2V', iMedias.media2D[j].R2VCamera.fov);
            CAT3DWPreferenceManager.setPreferenceValue('focalR2V', iMedias.media2D[j].R2VCamera.focal);
            CAT3DWPreferenceManager.setPreferenceValue('imWidthR2V', iMedias.media2D[j].R2VCamera.imWidth);
            CAT3DWPreferenceManager.setPreferenceValue('imHeightR2V', iMedias.media2D[j].R2VCamera.imHeight);
          }
        }
      }



      if (!mediasData.length) {
        return new PromiseUWA(function (resolve) { resolve(); });
      }

      return this._swymMediaServices.getMedias(mediasData,
                                               function (iPercentValue) {
                                                 Mask.mask(this._mask, 'Loading ' + iPercentValue + '%');
                                               }.bind(this))
        .then(function (iMediasData) {
          CAT3DWHandleErrors.initialize();
          var transfoMatrixArray = null;
          var transfoMatrixArrayElem = null;
          var mediaNotProcessedList = '';
          var nbMediaNotAvailable = 0;
          var nbMediaNotProcessed = 0;
          for (var i = 0; i < iMediasData.length; i++) {
            if (iMediasData[i].status === CAT3DWFileServices.STATUS_PROCESSING && iMediasData[i].type) {
              mediaNotProcessedList += iMediasData[i].title + ' ';
              nbMediaNotProcessed++;
            }

            if (iMediasData[i].status === CAT3DWFileServices.STATUS_KO && iMediasData[i].type) {
              nbMediaNotAvailable++;
            }

            if (iMediasData[i].format === CAT3DWFileServices.FORMAT_3D && iMediasData[i].status !== CAT3DWFileServices.STATUS_PROCESSING) {
              if (iMediasData[i].associatedData.matrix) {
                transfoMatrixArray = JSON.parse(iMediasData[i].associatedData.matrix);
                transfoMatrixArrayElem = transfoMatrixArray['elements'];
              } else
                transfoMatrixArrayElem = null;
              this.load3DModel(iMediasData[i].url, transfoMatrixArrayElem);
            }

            if (iMediasData[i].format === CAT3DWFileServices.FORMAT_2D || iMediasData[i].status === CAT3DWFileServices.STATUS_PROCESSING) {
              if (iMediasData[i].associatedData.background && iMediasData[i].associatedData.background === true) {

                var ambience = new Ambience({ viewer: this._viewer });

                this._viewer.setAmbience(ambience);
                var backgroundUrl = iMediasData[i].url;

                // ByPass cors issue with parameter
                var urlService = new CAT3DWUrlServices();
                backgroundUrl = urlService.generateUrlWithSuffix(backgroundUrl);

                // proxify url for mobile app
                if (this._isMobileApp) {
                  backgroundUrl = UWA.Data.proxifyUrl(backgroundUrl, { proxy: 'passport' });
                }
                ambience.setBackGroundMap({ url: backgroundUrl, mapping: new THREE.SimpleEnvMappingFit(), hdr: false });

              } else {
                transfoMatrixArray = JSON.parse((iMediasData[i].associatedData.matrix));
                this.load2DModel(iMediasData[i].url, transfoMatrixArray['elements'], iMediasData[i].type, iMediasData[i].status);
              }
            }
          }
          if (nbMediaNotProcessed)
            CAT3DWHandleErrors.setGeneralErrorMessage(nbMediaNotProcessed + ' media not yet processed. Try reloading when all medias are processed', '', mediaNotProcessedList, 'warning');
          if (nbMediaNotAvailable)
            CAT3DWHandleErrors.setGeneralErrorMessage(nbMediaNotAvailable + ' media not available. Check your access rights', '', '', 'warning');

        }.bind(this))
        .catch(function (err) { if (err) console.log(err); });

    },

    load: function (iData, onReady, onProgress, onLoaded) {
      this._3DSKTContainer = new Node3D('3DSKTContainer');
      this._annotContainer = new Node3D('annotContainer');
      this._3DSKTContainer.addChild(this._annotContainer);
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
      }).inject(document.body);
      this._mask = UWA.createElement('div', {
        styles: {
          position: 'absolute',
          top: '0',
          width: '100%',
          height: '100%'
        }
      }).inject(this._maskContainer);
      Mask.mask(this._mask, 'Loading'); // mask over the viewer

      // init swym media services from platformId or platformUrl
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

      this._swymMediaServices = new CAT3DW3DSwymMediaServices(platformParam);

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
        .then(this.getMedias.bind(this))
        .then(function () {
          Mask.unmask(this._mask);
          document.body.removeChild(this._maskContainer);
          onReady(this._3DSKTContainer);
          onLoaded(this._3DSKTContainer);

          TrackerAPI.trackPageEvent({
            eventCategory: '3DSketch_Loader',
            eventAction: '3DSketch_LoadVisu',
            eventLabel: 'Load visu of sketch in other apps',
            appID: 'CAT3DSKT_AP'
          });
        }.bind(this))
        .catch(this._error);
    },

    _error: function (err) {
      console.error(err);
    }

  });
});
