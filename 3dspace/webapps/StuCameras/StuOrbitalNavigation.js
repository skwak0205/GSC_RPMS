define('DS/StuCameras/StuOrbitalNavigation', ['DS/StuCore/StuContext', 'DS/EP/EP', 'DS/StuCameras/StuNavigation', 'DS/MathematicsES/MathsDef', 'DS/StuRenderEngine/StuProductActor'], function (STU, EP, Navigation, DSMath) {
	'use strict';

    /**
	 * Describe an orbital camera navigation.
	 *
	 * @exports OrbitalNavigation
	 * @class 
	 * @constructor
	 * @noinstancector
	 * @public
	 * @extends {STU.Navigation}
	 * @memberOf STU
     * @alias STU.OrbitalNavigation
	 */
	var OrbitalNavigation = function () {

		Navigation.call(this);
		this.name = 'OrbitalNavigation';

		//////////////////////////////////////////////////////////////////////////
		// Properties that should NOT be visible in UI
		//////////////////////////////////////////////////////////////////////////

        /**
		 * Flag indicating if the camera viewpoint abide by the orbit constraints
		 *
		 * @member
		 * @private
		 * @type {boolean}
		 */
		this._isOrbitConstraintOK = false;

        /**
		 * Transparency manager
		 *
		 * @member
		 * @private
		 * @type {stu__ShadingManager}
		 */
		this._shadingManager = null;

        /**
		 * Transparency color in hexadecimal #RedGreenBlueAlpha
		 *
		 * @member
		 * @private
		 * @type {string}
		 */
		this._transparencyColor = '#aaaaaa10';

        /**
		 * Current transparency time amount
		 *
		 * @member
		 * @private
		 * @type {number}
		 */
		this._transparencyTime = 0;

        /**
		 * Easing variable for zooming
		 *
		 * @member
		 * @private
		 * @type {number}
		 */
		this._zoomBegin = 0;

        /**
		 * Easing variable for zooming
		 *
		 * @member
		 * @private
		 * @type {number}
		 */
		this._zoomChange = 0;

        /**
		 * Easing variable for zooming
		 *
		 * @member
		 * @private
		 * @type {number}
		 */
		this._zoomDuration = 0;

        /**
		 * Easing variable for zooming
		 *
		 * @member
		 * @private
		 * @type {number}
		 */
		this._zoomStart = 0;

        /**
		 * Easing variable for zooming
		 *
		 * @member
		 * @private
		 * @type {number}
		 */
		this._ZoomTime = 0;

        /**
		 * Moving vector between 2 frames
		 *
		 * @member
		 * @private
		 * @type {DSMath.Vector3D}
		 */
		this._targetPosLastFrame = null;

        /**
		 * Array holding parameters for transparency
		 *
		 * @member
		 * @private
		 * @type {Object}
		 */
		this._actorParam = {
			isProduct: false,
			shadingIft: null,
			startColor: {
				r: -1,
				g: -1,
				b: -1
			}
		};



        /**
		 * True when a focus transition is occurring
		 *
		 * @private
		 * @type {Boolean}
		 */
		this._isFocusingOn = false;

        /**
		 * True when a go to transition is occurring
		 *
		 * @private
		 * @type {Boolean}
		 */
		this._isGoingTo = false;

        /**
		 * True when moving around is activated by timer
		 *
		 * @private
		 * @type {Boolean}
		 */
		this._isMovingAroundByTimer = false;

		/**
		 * True when moving around is activated by the capacity
		 *
		 * @private
		 * @type {Boolean}
		 */
		this._isMovingAroundByCapacity = false;

        /**
		 * Time (in second) since the focus on transition was started
		 *
		 * @private
		 * @type {Number}
		 */
		this._elapsedTimeFocusOn = 0;

        /**
		 * Temporary variable to hold the next target during a focus on transition
		 *
		 * @private
		 * @type {STU.Actor3D}
		 */
		this._targetActorToFocus = null;

        /**
		 * Duration of the focus on transition
		 *
		 * @private
		 * @type {Number}
		 */
		this._timeToFocusOn = 2.0;

        /**
		 * Start position of the target during a focusOn transition
		 *
		 * @private
		 * @type {DSMath.Vector3D}
		 */
		this._startPosFocusOn = null;


        /**
		 * Time (in seconds) since the go to transition was started
		 *
		 * @private
		 * @type {Number}
		 */
		this._elapsedTimeGoTo = 0;

        /**
		 * Temporary variable to hold the target point during a go to transition
		 * @private
		 * @type {STU.Actor3D}
		 */
		this._targetToGoToget = null;

        /**
		 * Duration (in seconds) of the go to transition
		 *
		 * @private
		 * @type {Number}
		 */
		this._timeToGoTo = 2.0;

        /**
		 * Starting point of the go to transition
		 *
		 * @private
		 * @type {DSMath.Vector3D}
		 */
		this._startPositionGoTo = null;

        /**
		 * Variable that activate the automatic rotation of the camera
		 *
		 * @private
		 * @type {Number}
		 */
		this._elapsedTimeAtIdle = 0.0;

        /**
		 * Target's bounding sphere computed at start or each time we change target
		 *
		 * @private
		 * @type {Number}
		 */
		this._bSphere = 0.0;

		/**
		 * Target's bounding box computed at start or each time we change target
		 *
		 * @private
		 * @type {Number}
		 */
		 this._bBoxCenterZ = 0.0;

		/**
		 * Target's bounding box computed at start or each time we change target
		 *
		 * @private
		 * @type {Number}
		 */
		 this._bBoxLowZ = 0.0;

		 /**
		 * Target's bounding box computed at start or each time we change target
		 *
		 * @private
		 * @type {Number}
		 */
		  this._bBoxHighZ = 0.0;
		  
        /**
		 * Offset vector beetween the target origin and the bounding sphere center
		 *
		 * @private
		 * @type {DSMath.Vector3D}
		 */
		this._bsCenter2TargetOriginVector = null;

        /**
		 * Zoom factor
		 *
		 * @private
		 * @type {Number}
		 */
		this._zoomFactor = 1.5;

        /**
		 * Zoom easing duration
		 *
		 * @private
		 * @type {Number}
		 */
		this._zoomDefaultDuration = 0.5;

        /**
		 * Zoom easing variable for gamepad
		 *
		 * @private
		 * @type {Number}
		 */
		this._currentGamepadZoomSpeed = 0;
		this._zoomGamepadDefaultDuration = 0.3;
		this._zoomGamepadTime = 0;
		this._zoomGamepadHeading = 0;

        /**
		 * Movement easing variables
		 *
		 * @private
		 * @type {Number}
		 */
		this._inertiaON = false;
		this._movementEasingCurrentTime = 0;
		this._movementEasingDuration = 1.0;
		this._MemDeltaX = 0;
		this._MemDeltaY = 0;
		this._isDecelerating = false;

		this._smoothnessQueue = [];
		this._rotationSmoothness = 300; //400

		/**
		 * Flag indicating on "navigate on click" mode, the input is compute according to the position where the mouse has been pressed (not anymore based on the center of the screen).
		 *
		 * @member
		 * @private
		 * @type {boolean}
		 */
		this._useMousePosAsRefWithNavigateOnClick = true;

		/**
		 * DCN23 - experimental private/new feature - Offset to control the KeepOverTheGoundLimit (lower side => go below the limit at 0 it is the lowrst point of the bounding sphere, you can also rehaust the below limit to be a bit higher for instance) ; not under env variable yet
		 *
		 * @private
		 * @type {Number}
		 */
		this._GroundLimitBottomOffset = 0;

		/**
		 * DCN23 - experimental private/new feature - Offset to control the KeepOverTheGoundLimit (here it's to limit the upper side => prevent a too high point of view) ; not under env variable yet
		 *
		 * @private
		 * @type {Number}
		 */
		this._SkyLimitTopOffset = 0;

		/**
		 * DCN23 - experimental private/new feature - Flag to declare if we want to use the top ground to limit aerial view // !! actually you need to have keep off the ground to have keep off the sky (can be coded to not too but it is such a hassle rn as it is only a preview of a possible feature ...) ; not under env variable yet
		 *
		 * @private
		 * @type {boolean}
		 */
		this._KeepOffTheSky = false;
		 
		//////////////////////////////////////////////////////////////////////////
		// Properties that should be visible in UI
		//////////////////////////////////////////////////////////////////////////

		///
		/////////      General
		///
		
		///
		/////////      Parameters
		///

        /**
		 * Object around which the camera orbit
		 *
		 * @member
		 * @instance
		 * @name targetObject
		 * @public
		 * @type {STU.Actor}
		 * @memberOf STU.OrbitalNavigation
		 */
		this._targetObject = null;
		Object.defineProperty(this, 'targetObject', {
			enumerable: true,
			configurable: true,
			get: function () {
				return this._targetObject;
			},
			set: function (value) {
				this._targetObject = value;
				if (this.isActive())
					this.computeBoundingSphere();
			}
		});

        /**
		 * Force the camera position to stay over the floor
		 *
		 * @member
		 * @public
		 * @type {boolean}
		 */
		this.keepOverTheFloor = true;

		///
		/////////      Controls
		///

        /**
		 * Enable or disable zoom possibility
		 *
		 * @member
		 * @public
		 * @type {boolean}
		 */
		this.allowZoom = true;

        /**
		 * Enable or disable navigation with mouse
		 *
		 * @member
		 * @public
		 * @type {boolean}
		 */
		this.useMouse = true;

        /**
		 * Enable or disable navigation with keyboard
		 *
		 * @member
		 * @public
		 * @type {boolean}
		 */
		this.useKeyboard = false;

        /**
		 * Enable or disable navigation with gamepad
		 *
		 * @member
		 * @public
		 * @type {boolean}
		 */
		this.useGamepad = false;

		/**
		 *  Enable or disable touch
		 *
		 * @member
		 * @instance
		 * @name useTouch
		 * @public
		 * @type {Boolean}
		 * @default {false}
		 * @memberOf STU.OrbitalNavigation
		 */
		this._useTouch = false;
		Object.defineProperty(this, 'useTouch', {
			enumerable: true,
			configurable: true,
			get: function () {
				return this._useTouch;
			},
			set: function (value) {
				this._useTouch = value;
				if (this._inputManager !== undefined && this._inputManager !== null) {
					this._inputManager.useTouch = value;
				}
			}
		});

        /**
		 *  Mapped key for moving left
		 *
		 * @member
		 * @instance
		 * @name moveLeft
		 * @public
		 * @type {EP.Keyboard.EKey}
		 * @default {EP.Keyboard.EKey.eLeft}
		 * @memberOf STU.OrbitalNavigation
		 */
		this._moveLeft = EP.Keyboard.EKey.eLeft;
		Object.defineProperty(this, 'moveLeft', {
			enumerable: true,
			configurable: true,
			get: function () {
				return this._moveLeft;
			},
			set: function (value) {
				this._moveLeft = value;
				if (this._inputManager !== undefined && this._inputManager !== null) {
					this._inputManager.keyLeft = this._moveLeft;
				}
			}
		});

        /**
		 *  Mapped key for moving right
		 *
		 * @member
		 * @instance
		 * @name moveRight
		 * @public
		 * @type {EP.Keyboard.EKey}
		 * @default {EP.Keyboard.EKey.eRight}
		 * @memberOf STU.OrbitalNavigation
		 */
		this._moveRight = EP.Keyboard.EKey.eRight;
		Object.defineProperty(this, 'moveRight', {
			enumerable: true,
			configurable: true,
			get: function () {
				return this._moveRight;
			},
			set: function (value) {
				this._moveRight = value;
				if (this._inputManager !== undefined && this._inputManager !== null) {
					this._inputManager.keyRight = this._moveRight;
				}
			}
		});

        /**
		 *  Mapped key for moving up
		 *
		 * @member
		 * @instance
		 * @name moveUp
		 * @public
		 * @type {EP.Keyboard.EKey}
		 * @default {EP.Keyboard.EKey.eUp}
		 * @memberOf STU.OrbitalNavigation
		 */
		this._moveUp = EP.Keyboard.EKey.eUp;
		Object.defineProperty(this, 'moveUp', {
			enumerable: true,
			configurable: true,
			get: function () {
				return this._moveUp;
			},
			set: function (value) {
				this._moveUp = value;
				if (this._inputManager !== undefined && this._inputManager !== null) {
					this._inputManager.keyUp = this._moveUp;
				}
			}
		});

        /**
		 *  Mapped key for moving down
		 *
		 * @member
		 * @instance
		 * @name moveDown
		 * @public
		 * @type {EP.Keyboard.EKey}
		 * @default {EP.Keyboard.EKey.eDown}
		 * @memberOf STU.OrbitalNavigation
		 */
		this._moveDown = EP.Keyboard.EKey.eDown;
		Object.defineProperty(this, 'moveDown', {
			enumerable: true,
			configurable: true,
			get: function () {
				return this._moveDown;
			},
			set: function (value) {
				this._moveDown = value;
				if (this._inputManager !== undefined && this._inputManager !== null) {
					this._inputManager.keyDown = this._moveDown;
				}
			}
		});

        /**
		 *  Mapped key for moving zooming in
		 *
		 * @member
		 * @instance
		 * @name zoomIn
		 * @public
		 * @type {EP.Keyboard.EKey}
		 * @default {EP.Keyboard.EKey.ePageUp}
		 * @memberOf STU.OrbitalNavigation
		 */
		this._zoomIn = EP.Keyboard.EKey.ePageUp;
		Object.defineProperty(this, 'zoomIn', {
			enumerable: true,
			configurable: true,
			get: function () {
				return this._zoomIn;
			},
			set: function (value) {
				this._zoomIn = value;
				if (this._inputManager !== undefined && this._inputManager !== null) {
					this._inputManager.keyTrigger1 = this._zoomIn;
				}
			}
		});

        /**
		 *  Mapped key for moving zooming out
		 *
		 * @member
		 * @instance
		 * @name zoomOut
		 * @public
		 * @type {EP.Keyboard.EKey}
		 * @default {EP.Keyboard.EKey.ePageDown}
		 * @memberOf STU.OrbitalNavigation
		 */
		this._zoomOut = EP.Keyboard.EKey.ePageDown;
		Object.defineProperty(this, 'zoomOut', {
			enumerable: true,
			configurable: true,
			get: function () {
				return this._zoomOut;
			},
			set: function (value) {
				this._zoomOut = value;
				if (this._inputManager !== undefined && this._inputManager !== null) {
					this._inputManager.keyTrigger2 = this._zoomOut;
				}
			}
		});
		
        /**
		 * Set the speed of Zoom displacement
		 *
		 * @member
		 * @public
		 * @type {number}
		 */
		this.zoomSensitivity = 1.5;

		///
		/////////      Advanced
		///

        /**
		 * Set the speed of Yaw displacement (horizontal speed, rotation on Up axis) in degree / seconds
		 *
		 * @member
		 * @public
		 * @type {number}
		 */
		this.horizontalRotationSensitivity = 200.0;

        /**
		 * Set the speed of Pitch displacement (vertical speed, rotation on Right axis) in degree / seconds
		 *
		 * @member
		 * @public
		 * @type {number}
		 */
		this.verticalRotationSensitivity = 100.0;

        /**
		 * Maximum distance between the camera and its target
		 *
		 * @member
		 * @public
		 * @type {number}
		 */
		this.maximumDistance = 300000;

        /**
		 * Minimum distance between the camera and its target
		 *
		 * @member
		 * @public
		 * @type {number}
		 */
		this.minimumDistance = 200;

        /**
		 * Switch the X axis controls
		 *
		 * @member
		 * @public
		 * @type {number}
		 */
		this.invertXAxis = false;

        /**
		 * Switch the Y axis controls
		 *
		 * @member
		 * @public
		 * @type {number}
		 */
		this.invertYAxis = false;

        /**
		 * Enable the transparency objects between the camera and its target
		 *
		 * @member
		 * @public
		 * @type {number}
		 */
		this.hideObstacles = false;

        /**
		 * Prevent the camera from going throw objects
		 *
		 * @member
		 * @public
		 * @type {number}
		 */
		this.avoidCollision = false;

        /**
		 * Enable the camera automatic rotation when no input has been pressed since a while
		 *
		 * @member
		 * @public
		 * @type {Boolean}
		 */
		this.automaticRotationAtIdle = false;

        /**
		 * Elapsed time before activate the camera automatic rotation
		 *
		 * @member
		 * @public
		 * @type {number}
		 */
		this.automaticRotationTimer = 5.0;



        /**
		 * Damping Test - Under Variable CXP_ORBITAL_POC
		 *
		 * @member
		 * @private
		 * @type {Boolean}
		 */
		this.inertiaEnabled = undefined;
        /**
		 * Damping Test - Under Variable CXP_ORBITAL_POC
		 *
		 * @member
		 * @private
		 * @type {number}
		 */
		this.inertiaSensitivity = undefined;
        /**
		 * Navigation Mode Refactoring - Under Variable CXP_ORBITAL_POC
		 *
		 * @member
		 * @private
		 * @type {number}
		 */
		this.navigationMode = undefined;
	};

	OrbitalNavigation.prototype = new Navigation();
	OrbitalNavigation.prototype.constructor = OrbitalNavigation;
	OrbitalNavigation.prototype.protoId = '6E6DD25D-8A2F-437B-A533-2239CE2EDD32';
	OrbitalNavigation.prototype.pureRuntimeAttributes = [
		'this._actorParam',
		'this._camera',
		'this._elapsedTimeFocusOn',
		'this._elapsedTimeGoTo',
		'this._isFocusingOn',
		'this._isGoingTo',
		'this._isMovingAroundByTimer',
		'this._isMovingAroundByCapacity',
		'this._isOrbitConstraintOK',
		'this._moveDown',
		'this._moveLeft',
		'this._moveRight',
		'this._moveUp',
		'this._shadingManager',
		'this._startPosFocusOn',
		'this._startPositionGoTo',
		'this._targetActorToFocus',
		'this._targetToGoToget',
		'this._targetPosLastFrame',
		'this._timeToFocusOn',
		'this._timeToGoTo',
		'this._transparencyColor',
		'this._transparencyTime',
		'this._zoomBegin',
		'this._zoomChange',
		'this._zoomDuration',
		'this._zoomIn',
		'this._zoomOut',
		'this._zoomStart',
		'this._ZoomTime',
		'this._useTouch',
	].concat(Navigation.prototype.pureRuntimeAttributes);

    /**
	 * Replace Math.sign for IE 
	 * 
	 * @private
	 */
	OrbitalNavigation.prototype._sign = function (iNumber) {
		if (iNumber === 0) {
			return 0;
		} else if (iNumber > 0) {
			return 1;
		} else if (iNumber < 0) {
			return -1;
		}
	};

    /**
	 * Process executed when STU.OrbitalNavigation is activating.
	 *
	 * @method
	 * @private
	 */
	OrbitalNavigation.prototype.onActivate = function (oExceptions) {
		Navigation.prototype.onActivate.call(this, oExceptions);

		//we suppose that the first camera orbit doesn't comply with th camera constraints
		this._isOrbitConstraintOK = false;

		//Registering key-bindings
		this._inputManager.useMouse = this.useMouse;
		this._inputManager.useKeyboard = this.useKeyboard;
		this._inputManager.useGamepad = this.useGamepad;
		this._inputManager.mouseAxis = 2;
		this._inputManager.keyLeft = this.moveLeft;
		this._inputManager.keyRight = this.moveRight;
		this._inputManager.keyUp = this.moveUp;
		this._inputManager.keyDown = this.moveDown;
		this._inputManager.mouseInvertWheel = true;
		this._inputManager.mouseSensitivity = 2;
		this._inputManager.mouseInvertX = this.invertXAxis;
		this._inputManager.mouseInvertY = !this.invertYAxis;//true;
		this._inputManager.keyboardInvertX = this.invertXAxis;
		this._inputManager.keyboardInvertY = !this.invertYAxis;//true;
		this._inputManager.gamepadInvertX = this.invertXAxis;
		this._inputManager.gamepadInvertY = !this.invertYAxis;//true;
		this._inputManager.keyTrigger1 = this.zoomIn;
		this._inputManager.keyTrigger2 = this.zoomOut;
		this._inputManager.useGamepad = this.useGamepad;
		this._inputManager.useTouch = this._useTouch;
		this._inputManager.useDoubleTouch = false;
		this._useMousePosAsRefWithNavigateOnClick = true;

		// for the LookAt
		this.computeBoundingSphere();
		this.computeBoundingBox();

		// Set startup cam position above object if Keep Over the Floor is checked = fixing cam below at start when demanding keepOverTheFloor
		if ( this._camera !== undefined && this._camera !== null && this._targetObject !== undefined && this._targetObject !== null && (this.keepOverTheFloor === true || this._KeepOffTheSky === true) ) {
			// Get target position (so the bounding box (to compare its z)
			var lowerZBoundingBox = this._bBoxLowZ; //iBBox.low.z is negativ

			// for too upper position as well 
			var upperZBoundingBox = this._bBoxHighZ;

			// Get current camera position (to get its x and y)
			var newStartCameraPosition = this._camera.getPosition().clone();

			// Compare the z to see if we need to move up the cam
			if (newStartCameraPosition.z < lowerZBoundingBox && this.keepOverTheFloor === true) {
				// move up to the level of the ground of the cam ; Change only its z (keep x and y set up by user in experience)
				newStartCameraPosition.z = (lowerZBoundingBox + upperZBoundingBox) / 2; // put at the center to dodge being at the extrem limit = can cause to go below limits

				// Apply
				this._camera.setPosition(newStartCameraPosition);
				
			}
			else if (newStartCameraPosition.z > upperZBoundingBox && this._KeepOffTheSky) {
				// move up to the level of the ground of the cam ; Change only its z (keep x and y set up by user in experience)
				newStartCameraPosition.z = (lowerZBoundingBox + upperZBoundingBox) / 2; // put at the center to dodge being at the extrem limit = can cause to go below limits

				// Apply
				this._camera.setPosition(newStartCameraPosition);
            }
		}

		// Set startup viewpoint to look at the target
		if (this._camera !== undefined && this._camera !== null) {
			var targetPosition = new DSMath.Vector3D();
			this.getTargetPosition(targetPosition, "Location");
			this._camera.lookAt(targetPosition, "Location");
		}



		if (this.inertiaEnabled != null && this.inertiaEnabled != undefined) {
			this._inertiaON = this.inertiaEnabled;
			if (!this.navigateOnClick && !this.followMouse) {
				this._inertiaON = false;
			}
		}

		if (this.inertiaSensitivity != null && this.inertiaSensitivity != undefined) {
			this._rotationSmoothness = 1000 * this.inertiaSensitivity;
		}

        /*if (this.navigationMode != null && this.navigationMode != undefined) {
            console.log(this.navigationMode);
            switch (this.navigationMode) {
                case 0: // On Click
                    this.navigateOnClick = true;
                    this.followMouse = false;
                    console.log("Navigation Mode: On Click");
                    break;
                case 1: // Drag
                    this.navigateOnClick = true;
                    this.followMouse = true;
                    this._inputManager.mouseInvertX = !this._inputManager.mouseInvertX;
                    this._inputManager.mouseInvertY = !this._inputManager.mouseInvertY;

                    console.log("Navigation Mode: Drag");
                    break;
                case 2: // On Move
                    this.navigateOnClick = false;
                    this.followMouse = true;
                    console.log("Navigation Mode: On Move");
                    break;
                case 3: // Follow Mouse
                    this.navigateOnClick = false;
                    this.followMouse = false;
                    console.log("Navigation Mode: Follow Mouse");
                    break;
                default:
            }
        }*/

		if (!this.inertiaEnabled) {
			this._rotationSmoothness = 1;
		}

		for (var i = 0; i < this._rotationSmoothness; i++) {
			this._smoothnessQueue.push(new DSMath.Quaternion());
		}

		if (!this.navigateOnClick) {
			var rm = STU.RenderManager.getInstance();
			var viewerSize = rm.getViewerSize();

			var x = viewerSize.x * 0.5;
			var y = viewerSize.y * 0.5;
			var pos = new DSMath.Vector2D(x, y);

			rm.setMousePosition(pos);
		}
	};

    /**
	 * Process executed when STU.OrbitalNavigation is deactivating.
	 *
	 * @method
	 * @private
	 */
	OrbitalNavigation.prototype.onDeactivate = function () {
		if (this._shadingManager !== undefined && this._shadingManager !== null) {
			this._shadingManager.RemoveTransparency();
			this._shadingManager = null;
		}

		Navigation.prototype.onDeactivate.call(this);
	};

    /**
	 * Compute Bounding Sphere of the target actor
	 *
	 * @method
	 * @private
	 */
	OrbitalNavigation.prototype.computeBoundingSphere = function () {
		if (this._targetObject !== undefined && this._targetObject !== null) {
			var targetPos = this.targetObject.getPosition();
			this._bSphere = this.targetObject.getBoundingSphere();
			var BSCenter = new DSMath.Vector3D(this._bSphere.center.x, this._bSphere.center.y, this._bSphere.center.z);
			this._bsCenter2TargetOriginVector = DSMath.Vector3D.sub(BSCenter, targetPos);
		}
	}

	/**
	 * Compute Bounding Box of the target actor (used for beginning of xp and when changing actor)
	 *
	 * @method
	 * @private
	 */
	 OrbitalNavigation.prototype.computeBoundingBox = function () {
		// for optim, you should be able to not recompute bounding box every frame, but only when object is moving or changeing target objet.
		if (this._targetObject !== undefined && this._targetObject !== null) {
			// compute the lowest Z in the bounding box => set up the plan that limit the z of the cam ;
			//var MyParams = { excludeChildren: 1, orientation: this._targetObject.getTransform() };
			//var iBBox = this._targetObject.getOrientedBoundingBox(MyParams);
			var iBBox = this._targetObject.getBoundingBox();
			
			// the center if the object is only moving (the bbox size dont change)
			this._bBoxCenterZ = this._targetObject.getPosition().z; 

			this._bBoxLowZ = iBBox.low.z - this._GroundLimitBottomOffset; //iBBox.low.z is negativ
			// upper limit to reproduce the behavior of keep over the ground but for the top
			this._bBoxHighZ = iBBox.high.z + this._SkyLimitTopOffset;
		}
	}

    /**
	 * Returns true if the orbital is currently doing a focus transition towards iTarget.
	 *
	 * @method
	 * @param { STU.Actor3D} iTarget
	 * @public
	 * @return {Boolean}
	 */
	OrbitalNavigation.prototype.isFocusingOn = function (iTarget) {
		return this._isFocusingOn && this._targetActorToFocus === iTarget;
	};

    /**
	 * Returns true if the orbital is currently going to a new target.
	 *
	 * @method
	 * @public
     * @param  {STU.Actor3D}  iTarget the targeted position.
	 * @return {Boolean}
	 */
	OrbitalNavigation.prototype.isGoingTo = function (iTarget) {
		return this._isGoingTo && this._targetToGoToget === iTarget;
	};

    /**
	 * Returns true if the orbital is currently moving around.
	 *
	 * @method
	 * @public
	 * @return {Boolean}
	 */
	OrbitalNavigation.prototype.isMovingAround = function () {
		return (this._isMovingAroundByTimer || this._isMovingAroundByCapacity);
	};


	OrbitalNavigation.prototype._getBScenter = function (iActor) {
		var bsphere = iActor.getBoundingSphere();
		var center = new DSMath.Vector3D();
		center.set(bsphere.center.x, bsphere.center.y, bsphere.center.z);
		return center;
	};
			
    /**
	 * Sets a new orbiting target and makes a smooth transition towards it.
	 *
	 * @method
	 * @public
	 * @param  {STU.Actor3D} iNewTarget
	 */
	OrbitalNavigation.prototype.focusOn = function (iNewTarget) {
		if (!(iNewTarget instanceof STU.Actor3D)) {
			console.error('focusOn can only focus on an STU.Actor3D');

		} else { // everything is OK => start focusOn transition

			if (!(this._startPosFocusOn instanceof DSMath.Vector3D)) {
				this._startPosFocusOn = new DSMath.Vector3D();
			}
			if (this._isFocusingOn === true) {
				// there is already a focusOn transition going on 
				// we set the intermediate point as the new start point

				var startPosition = this._startPosFocusOn;
				var endPosition = this._getBScenter(iNewTarget);

				var easingAmount = this.easeOutCubic(this._elapsedTimeFocusOn, 0, 1, this._timeToFocusOn);
				this._startPosFocusOn = DSMath.Vector3D.lerp(startPosition, endPosition, easingAmount);

			} else {
				this.getTargetPosition(this._startPosFocusOn);
				this.computeBoundingBox();
				
			}

			this._isFocusingOn = true;
			this.targetObject = iNewTarget; // set the new target
			this._targetActorToFocus = iNewTarget;

			this._elapsedTimeFocusOn = 0;
			this._timeToFocusOn = 450 / (this.horizontalRotationSensitivity + this.verticalRotationSensitivity);
			// note that 450 / (100 + 200) = 1.5 so the transition will take 1.5s with default RotationSensitivity values
		}
	};

    /**
	 * To stop capacity "Focus On" throught NL keyword ("while", "during", etc...)
	 *
	 * @method
	 * @private
	 */
	OrbitalNavigation.prototype.stopFocusingOn = function (iNewTarget) {
		this._isFocusingOn = false;
		this._elapsedTimeFocusOn = 0;
	};

    /**
	 * Computes the intermediate orbiting target position during a focusing transition.
	 *
	 * @method
	 * @private
	 * @param  {DSMath.Vector3D} ioTargetPosition
	 */
	OrbitalNavigation.prototype.handleFocusOn = function (ioTargetPosition) {
		if (this._isFocusingOn === false) { // only makes sense when _isFocusingOn is true
			return;
		}

		if (this._elapsedTimeFocusOn > this._timeToFocusOn) {
			this._isFocusingOn = false;

			this.getTargetPosition(ioTargetPosition); //avoid flickering on last frame

			this.dispatchEvent(new STU.ServiceStoppedEvent("focusOn", this));

		} else if (this._elapsedTimeFocusOn <= this._timeToFocusOn) {

			var startPosition = this._startPosFocusOn;
			var endPosition = this._getBScenter(this._targetActorToFocus);

			var easingAmount = this.easeOutCubic(this._elapsedTimeFocusOn, 0, 1, this._timeToFocusOn);
			var intermediatePos = DSMath.Vector3D.lerp(startPosition, endPosition, easingAmount);

			ioTargetPosition.x = intermediatePos.x;
			ioTargetPosition.y = intermediatePos.y;
			ioTargetPosition.z = intermediatePos.z;
		}

		this._elapsedTimeFocusOn += this._deltaTime;
	};

    /**
	 * Sets a new position of the orbital navigation and makes a smooth transition towards it.
	 *
	 * @method
	 * @public
	 * @param  {STU.Actor3D}  iTargetToReach : position to reach.
	 */
	OrbitalNavigation.prototype.goTo = function (iTargetToReach) {
		if (!(iTargetToReach instanceof STU.Actor3D)) {
			console.error('goTo can only go to an STU.Actor3D');
		} else {
			// everything is OK => start goTo transition
			this._targetToGoToget = iTargetToReach;
			this._startPositionGoTo = this._camera.getPosition();
			this._isGoingTo = true;
			this._elapsedTimeGoTo = 0;
			this._timeToGoTo = 450 / (this.horizontalRotationSensitivity + this.verticalRotationSensitivity);
			// note that 450 / (100 + 200) = 1.5 so the transition will take 1.5s with default RotationSensitivity values
		}
	};


    /**
	 * Computes the intermediate camera position during a goTo transition.
	 *
	 * @method
	 * @private
	 * @param  {DSMath.Vector3D} oOrbitalCamPosition
	 */
	OrbitalNavigation.prototype.handleGoTo = function (oOrbitalCamPosition) {
		if (this._isGoingTo === false) {
			return false;
		}

		var targetPosition = this._targetToGoToget.getPosition();
		this._isOrbitConstraintOK = false;

		if (this._elapsedTimeGoTo > this._timeToGoTo) {
			this._isGoingTo = false;

			oOrbitalCamPosition.x = targetPosition.x;
			oOrbitalCamPosition.y = targetPosition.y;
			oOrbitalCamPosition.z = targetPosition.z;

			//Dispatch event
			var evt = new STU.NavHasReachedEvent(this._targetToGoToget, this);
			this._camera.dispatchEvent(evt);

			this.dispatchEvent(new STU.ServiceStoppedEvent("goTo", this));

		} else if (this._elapsedTimeGoTo <= this._timeToGoTo) {
			var startPosition = this._startPositionGoTo;
			var endPosition = targetPosition;

			var easingAmount = this.easeOutCubic(this._elapsedTimeGoTo, 0, 1, this._timeToGoTo);
			var intermediatePos = DSMath.Vector3D.lerp(startPosition, endPosition, easingAmount);

			oOrbitalCamPosition.x = intermediatePos.x;
			oOrbitalCamPosition.y = intermediatePos.y;
			oOrbitalCamPosition.z = intermediatePos.z;

		}
		this._elapsedTimeGoTo += this._deltaTime;

		return true;
	};

    /**
	 * To stop capacity "Go To" throught NL keyword ("while", "during", etc...) 
	 * @method
	 * @private
	 */
	OrbitalNavigation.prototype.stopGoingTo = function (iPoint) {
		this._isGoingTo = false;
		this._targetToGoToget = null;
		this._targetCoordinateToGoTo = null;
		this._targetVirtualPointToGoTo = null;
		this._elapsedTimeGoTo = 0;
		this._timeToGoTo = 0;
	};

    /**
	 * Make the navigation rotate around the target on with current distance/altitude, until stopped.
	 *
	 * @method
	 * @public
	 */
	OrbitalNavigation.prototype.startMoveAround = function () {
		this._isMovingAroundByCapacity = true;
	};

    /**
	 * Stops moving around.
	 *
	 * @method
	 * @public
	 */
	OrbitalNavigation.prototype.stopMoveAround = function () {
		//stoping the rotation using the capacity reset the timer
		this._isMovingAroundByCapacity = false;
		this._isMovingAroundByTimer = false;
		this._elapsedTimeAtIdle = 0;
	};


    /**
	 * [getCorrespondingTime description]
	 * @method  getCorrespondingTime
	 * @private
	 * @param   {number}             iValue
	 * @param   {function}           iEasing
	 * @return  {number}
	 */
	OrbitalNavigation.prototype.getCorrespondingTimeOLD = function (iValue, iEasing) {
		var maxLoop = 100;
		var eps = 0.001;

		var startTime = 0;
		var endTime = this._zoomDuration;

		var isIncreasing = this._zoomChange >= 0 ? true : false;

		var currentTime = 0;
		var res = -1;
		do {
			currentTime = (startTime + endTime) / 2;
			res = iEasing(currentTime, this._zoomBegin, this._zoomChange, this._zoomDuration);
			if (Math.abs(res - iValue) <= eps) {
				break;
			} else if (isIncreasing && res > iValue ||
				!isIncreasing && res < iValue) {
				endTime = currentTime;
			} else {
				startTime = currentTime;
			}
		} while (--maxLoop > 0);

		return currentTime;
	};

	OrbitalNavigation.prototype.getCorrespondingTime = function (iValue, iEasingFct, iBeginValue, iCurrentChange, iEasingDuration) {
		var maxLoop = 100;
		var eps = 0.001;

		var startTime = 0;
		var endTime = iEasingDuration;

		var isIncreasing = iCurrentChange >= 0 ? true : false;

		var currentTime = 0;
		var res = -1;
		do {
			currentTime = (startTime + endTime) / 2;
			res = iEasingFct(currentTime, iBeginValue, iCurrentChange, iEasingDuration);
			if (Math.abs(res - iValue) <= eps) {
				break;
			} else if (isIncreasing && res > iValue ||
				!isIncreasing && res < iValue) {
				endTime = currentTime;
			} else {
				startTime = currentTime;
			}
		} while (--maxLoop > 0);

		return currentTime;
	};


    /**
	 * Handle zooming behavior
	 *
	 * @method
	 * @private
	 * @param  {number} iDeltaTime Time elapsed since last frame (in seconds)
	 * @param  {STU.Camera} iCamera Camera component of the orbital navigation
	 * @param  {DSMath.Vector3D} iTargetPosition Position of the target
	 */
	OrbitalNavigation.prototype.handleZooming = function (iDeltaTime, iCamera, iTargetPosition) {
		//Camera zooming
		var cameraPosition = iCamera.getPosition();
		if (this.allowZoom === true) {
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			var targetToCam = DSMath.Vector3D.sub(cameraPosition, iTargetPosition);
			var targetToCamDst2 = targetToCam.squareNorm();
			var targetToCamDst = Math.sqrt(targetToCamDst2);

			var minDst = this.minimumDistance;
			var minDst2 = minDst * minDst;

			var maxDst = this.maximumDistance;
			var maxDst2 = maxDst * maxDst;

			//check that the camera respect the minimumDistance and maximumDistance
			if (targetToCamDst2 < minDst2) {
				targetToCam.normalize();
				targetToCam.multiplyScalar(minDst);
				targetToCam.add(iTargetPosition);

				//iCamera.lookAt(iTargetPosition);
				iCamera.setPosition(targetToCam);
			} else if (targetToCamDst2 > maxDst2) {
				targetToCam.normalize();
				targetToCam.multiplyScalar(maxDst);
				targetToCam.add(iTargetPosition);

				// iCamera.lookAt(iTargetPosition);
				iCamera.setPosition(targetToCam);
			} else { //smooth zooming 

				//The user is pushing a zoom key or turning the mouse wheel 
				if (//this._inputManager.buttonsState[0] === true ||
					//this._inputManager.buttonsState[1] === true ||
					//this._inputManager.axis5.x > 0 ||
					//this._inputManager.axis5.y < 0 ||
					this._inputManager.axis2.z !== 0 ||
					this._inputManager.axis6.z !== 0) {

					var targetToCamAndZoom = targetToCam.clone();

					//Is there already a zoom animation is going on 
					var isAnimating = false;
					if (this._zoomDuration !== 0) {
						isAnimating = true;

						targetToCamAndZoom.normalize();
						targetToCamAndZoom.multiplyScalar(this._zoomBegin + this._zoomChange);
					}

                    //Zooming in
                    /*if (this.useGamepad && this._inputManager.axis5.x > 0) {
                        targetToCamAndZoom.multiplyScalar(this._inputManager.axis5.x * this.zoomSensitivity);
                    }
                    else if (this.useKeyboard && this._inputManager.buttonsState[0] === true) {
                        targetToCamAndZoom.multiplyScalar(this._zoomFactor * 0.025 * this.zoomSensitivity);
                    }
                    else */if (this.useMouse && this._inputManager.axis2.z >= 1) {
						targetToCamAndZoom.multiplyScalar(this._zoomFactor * this.zoomSensitivity);
					}
					else if (this._inputManager.axis6.z === 1) {
						targetToCamAndZoom.multiplyScalar(this._zoomFactor * this.zoomSensitivity);
					}
                    //if (this._inputManager.buttonsState[1] === true || this._inputManager.gamepadButtonsState[4] === true || this._inputManager.axis2.z >= 1 || this._inputManager.axis6.z === 1) {
                    //    targetToCamAndZoom.multiplyScalar(this._zoomFactor * this.zoomSensitivity);
                    //}
                    //Zooming out
                    /*if (this.useGamepad && this._inputManager.axis5.y < 0) {
                        targetToCamAndZoom.divideScalar(this._inputManager.axis5.y * -1 * this.zoomSensitivity);
                    }
                    else if (this.useKeyboard && this._inputManager.buttonsState[1] === true) {
                        targetToCamAndZoom.divideScalar(this._zoomFactor * 0.025 * this.zoomSensitivity);
                    }
                    else */if (this.useMouse && this._inputManager.axis2.z <= -1) {
						targetToCamAndZoom.divideScalar(this._zoomFactor * this.zoomSensitivity);
					}
					else if (this._inputManager.axis6.z === -1) {
						targetToCamAndZoom.divideScalar(this._zoomFactor * this.zoomSensitivity);
					}
					//else if (this._inputManager.buttonsState[0] === true || this._inputManager.gamepadButtonsState[5] === true || this._inputManager.axis2.z <= -1 || this._inputManager.axis6.z === -1) {
					//    targetToCamAndZoom.divideScalar(this._zoomFactor * this.zoomSensitivity);
					//}

					var targetToCamAndZoomDst2 = targetToCamAndZoom.squareNorm();
					var targetToCamAndZoomDst = Math.sqrt(targetToCamAndZoomDst2);

					this._inputManager.axis1.z = 0;
					this._inputManager.axis2.z = 0;

					if (targetToCamAndZoomDst2 >= minDst2 && targetToCamAndZoomDst2 <= maxDst2) {
						if (isAnimating === false) {
							this._ZoomTime = 0;
							this._zoomBegin = targetToCamDst;
							this._zoomChange = targetToCamAndZoomDst - targetToCamDst;
							this._zoomDuration = this._zoomDefaultDuration;
						} else {
							var currentEase = this.easeInOutQuad(this._ZoomTime, this._zoomBegin, this._zoomChange, this._zoomDuration);
							// /!\ The order is important 

							var zoomAmount = targetToCamAndZoomDst - this._zoomBegin;

							// if zoomAmount has a different sign from zoomChange
							// then the zooming is switching direction
							if (zoomAmount * this._zoomChange < 0) {
								this._zoomBegin = 0;
							}

							this._zoomChange = zoomAmount;
							this._zoomDuration = this._zoomDefaultDuration;
							//this._ZoomTime = this.getCorrespondingTime(currentEase, this.easeInOutQuad);
							this._ZoomTime = this.getCorrespondingTime(currentEase, this.easeInOutQuad, this._zoomBegin, this._zoomChange, this._zoomDuration);
						}
					}
					else {
						this._inputManager.axis6.z = 0;
					}
				}


				if (this._zoomDuration !== 0) {
					this._ZoomTime += iDeltaTime;
					if (this._ZoomTime >= this._zoomDuration) {
						this._zoomDuration = 0;
					}

					var easeRes = this.easeInOutQuad(this._ZoomTime, this._zoomBegin, this._zoomChange, this._zoomDuration);
					var easeRes2 = easeRes * easeRes;

					var targetToCamAndZoom = targetToCam.clone();
					targetToCamAndZoom.normalize();
					targetToCamAndZoom.multiplyScalar(easeRes);
					var targetToCamAndZoomDst2 = targetToCamAndZoom.squareNorm();

					var isStickingToSky = false;
					//check ground level
					if (this.keepOverTheFloor === true) {
						var targetToCamNew = targetToCamAndZoom.clone();
						targetToCamNew.normalize();

						if (this.targetObject !== undefined && this.targetObject !== null) {
							// compute the bounding box (can optimize this to answer the [IR-636801])
							//this.computeBoundingBox();

							// lower limit keep over the floor
							var lowerZBoundingBox = this._bBoxLowZ; //iBBox.low.z is negativ

							// upper limit to reproduce the behavior of keep over the ground but for the top 
							var upperZBoundingBox = this._bBoxHighZ;

							// if camera is going below the floor (below the bounding spehre)
							if (targetToCamNew.z < -0.99 || cameraPosition.z < lowerZBoundingBox) { //zCamNorm < zBSNorm) { //before.dc23
								//targetToCamAndZoom.z -= (zCam - zAxisBS); //before.dc23
								targetToCamAndZoom.z = lowerZBoundingBox - this._bBoxCenterZ; // targetPosition.z =?= bSphere.center.z (true but idk why naming this way tho)
								targetToCamAndZoomDst2 = targetToCamAndZoom.squareNorm();
								isStickingToSky = false;
							}
							else if ( ( targetToCamNew.z > 0.99 || cameraPosition.z > upperZBoundingBox ) && this._KeepOffTheSky ) //  OR upper the top limit // need keep of the floor to enter here before ...
							{
								targetToCamAndZoom.z = upperZBoundingBox - this._bBoxCenterZ; 
								targetToCamAndZoomDst2 = targetToCamAndZoom.squareNorm();
								isStickingToSky = true;
                            }
						}
					}

					if (easeRes2 >= minDst2 && easeRes2 <= maxDst2 &&
						targetToCamAndZoomDst2 >= minDst2 && targetToCamAndZoomDst2 <= maxDst2) {
						isStickingToSky ?  targetToCamAndZoom.sub(iTargetPosition) : targetToCamAndZoom.add(iTargetPosition); // this is to know how we need to update targetToCamAndZoom
						iCamera.setPosition(targetToCamAndZoom);
						iCamera.lookAt(iTargetPosition);
					}
				}
			}
		}
	};

    /**
	 * Handle zooming behavior with gamepad
	 *
	 * @method
	 * @private
	 * @param  {number} iDeltaTime Time elapsed since last frame (in seconds)
	 * @param  {STU.Camera} iCamera Camera component of the orbital navigation
	 * @param  {DSMath.Vector3D} iTargetPosition Position of the target
	 */
	OrbitalNavigation.prototype.handleZoomingGamepad = function (iDeltaTime, iCamera, iTargetPosition) {
		var cameraPosition = iCamera.getPosition();
		if (this.allowZoom === true) {
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//distance to Camera
			var targetToCam = DSMath.Vector3D.sub(cameraPosition, iTargetPosition);
			var targetToCamDst2 = targetToCam.squareNorm();
			var targetToCamDst = Math.sqrt(targetToCamDst2);

			var minDst = this.minimumDistance;
			var minDst2 = minDst * minDst;
			var maxDst = this.maximumDistance;
			var maxDst2 = maxDst * maxDst;

			//distance from which we start decelerating
			var deltaMin = (minDst) * 2 //;(maxDst - minDst) * 0.015;
			var deltaMax = (maxDst - minDst);// * 0.55;

			var deltaMinDistToPos = targetToCamDst - minDst;
			deltaMinDistToPos = (deltaMinDistToPos < 0) ? 0 : deltaMinDistToPos;
			deltaMinDistToPos = (deltaMinDistToPos > deltaMin) ? deltaMin : deltaMinDistToPos;

			var deltaMaxDistToPos = maxDst - targetToCamDst;
			deltaMaxDistToPos = (deltaMaxDistToPos < 0) ? 0 : deltaMaxDistToPos;
			deltaMaxDistToPos = (deltaMaxDistToPos > deltaMax) ? deltaMax : deltaMaxDistToPos;

			//deceleration easing according to distance
			var t_min = this.easeOutQuad(deltaMinDistToPos, 0, 1, deltaMin);
			var t_max = this.easeOutQuad(deltaMaxDistToPos, 0, 1, deltaMax);

			//Input management 

			var inversionXFactor = this.invertXAxis ? -1 : 1;
			var inversionYFactor = this.invertYAxis ? -1 : 1;
			var gamepadAxisX = this._inputManager.axis5.x * inversionXFactor;
			var gamepadAxisY = this._inputManager.axis5.y * inversionYFactor;

			//var zoomOutPressed = (this._inputManager.axis5.x > 0 || this._inputManager.buttonsState[1] === true);
			//var zoomInPressed = (this._inputManager.axis5.y < 0 || this._inputManager.buttonsState[0] === true);            
			var zoomOutPressed = (gamepadAxisX > 0 || this._inputManager.buttonsState[1] === true);
			var zoomInPressed = (gamepadAxisY < 0 || this._inputManager.buttonsState[0] === true);
			var keyboardFactorOut = this._inputManager.buttonsState[1] === true ? 1 : 0;
			var keyboardFactorIn = this._inputManager.buttonsState[0] === true ? 1 : 0;

			//console.log(this._inputManager.axis5.x);

			var duration = this._zoomGamepadDefaultDuration;
			if (zoomOutPressed && this._zoomGamepadHeading >= 0) {//if (this._inputManager.axis5.x > 0 && this._zoomGamepadHeading >= 0) {
				//this._zoomGamepadTime += (iDeltaTime * this._inputManager.axis5.x) + (iDeltaTime * keyboardFactorOut);
				this._zoomGamepadTime += (iDeltaTime * gamepadAxisX) + (iDeltaTime * keyboardFactorOut);
				this._zoomGamepadTime = (this._zoomGamepadTime > duration) ? duration : this._zoomGamepadTime;
				this._zoomGamepadHeading = 1;
			}
			else if (zoomInPressed && this._zoomGamepadHeading <= 0) { //else if (this._inputManager.axis5.y < 0 && this._zoomGamepadHeading <= 0) {
				//this._zoomGamepadTime += (iDeltaTime * (this._inputManager.axis5.y * -1)) + (iDeltaTime * keyboardFactorIn);
				this._zoomGamepadTime += (iDeltaTime * (gamepadAxisY * -1)) + (iDeltaTime * keyboardFactorIn);
				this._zoomGamepadTime = (this._zoomGamepadTime > duration) ? duration : this._zoomGamepadTime;
				this._zoomGamepadHeading = -1;
			}
			else {
				if (this._zoomGamepadTime > 0) {
					//In case where the easing is not finish and pressed on the opposite zoom direction
					var o = (zoomOutPressed && this._zoomGamepadHeading > 0) ? 1 : 0; //(this._inputManager.axis5.y < 0 && this._zoomGamepadHeading > 0) ? 1 : 0;
					if (!o) o = (zoomInPressed && this._zoomGamepadHeading < 0) ? 1 : 0; //(this._inputManager.axis5.x > 0 && this._zoomGamepadHeading < 0) ? 1 : 0;
					var factor = (o == 1) ? 2 : 1;
					this._zoomGamepadTime -= iDeltaTime * factor;
					this._zoomGamepadTime = (this._zoomGamepadTime < 0) ? 0 : this._zoomGamepadTime;
				}
				else {
					this._zoomGamepadHeading = 0;
					//console.log(this._zoomGamepadTime);
				}
			}

			var easeRes = this.easeOutQuad(this._zoomGamepadTime, 0, 1, duration);

			var direction = targetToCam.clone();
			direction.normalize();

			var speed = (targetToCamDst * iDeltaTime) * easeRes * this.zoomSensitivity; //10000.0 * easeRes;
			if (this._zoomGamepadHeading > 0) {
				speed *= t_max;
			}
			else {
				speed *= t_min;
			}
			direction.multiplyScalar(speed * this._zoomGamepadHeading);

			var newPos = iCamera.getPosition().clone();
			newPos.add(direction);

			var targetToCamAndZoom = DSMath.Vector3D.sub(newPos, iTargetPosition);//newPos.clone();
			var targetToCamAndZoomDst2 = targetToCamAndZoom.squareNorm();

			//check ground level
			if (this.keepOverTheFloor === true) {
				var targetToCamNew = targetToCamAndZoom.clone();
				targetToCamNew.normalize();

				if (this.targetObject !== undefined && this.targetObject !== null) {
					// compute the bounding box (can optimize this to answer the [IR-636801])
					//this.computeBoundingBox();

					// lower limit keep over the floor
					var lowerZBoundingBox = this._bBoxLowZ; //iBBox.low.z is negativ

					// upper limit to reproduce the behavior of keep over the ground but for the top 
					var upperZBoundingBox = + this._bBoxHighZ;

					// if camera is going below the floor (below the bounding spehre)
					if (targetToCamNew.z < -0.99 || cameraPosition.z < lowerZBoundingBox) { //zCamNorm < zBSNorm) { //before.dc23
						//targetToCamAndZoom.z -= (zCam - zAxisBS); //before.dc23
						targetToCamAndZoom.z = lowerZBoundingBox - this._bBoxCenterZ; // targetPosition.z =?= bSphere.center.z (true but idk why naming this way tho)
						targetToCamAndZoomDst2 = targetToCamAndZoom.squareNorm();
					}
					else if (cameraPosition.z > upperZBoundingBox && this._KeepOffTheSky) //  OR upper the top limit // need keep of the floor to enter here before ...
					{
						targetToCamAndZoom.z = upperZBoundingBox - this._bBoxCenterZ; 
						targetToCamAndZoomDst2 = targetToCamAndZoom.squareNorm();
					}
				}
			}

			//iCamera.setPosition(newPos);
			//iCamera.lookAt(iTargetPosition);
			if (targetToCamAndZoomDst2 >= minDst2 && targetToCamAndZoomDst2 <= maxDst2) {
				iCamera.setPosition(newPos);
				iCamera.lookAt(iTargetPosition);
			}
		}
	}

    /**
	 * Convert an rgba hexadecimal color string  into an array
	 *
	 * @method
	 * @private
	 * @param  {string} iHex Hexadecimal color value #rrggbbaa
	 * @return {Array.<number>}    [r:redValue, g:greenValue, b:blueValue, a:alphaValue]
	 */
	OrbitalNavigation.prototype.hexToRgba = function (iHex) {
		var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(iHex);
		return result ? {
			r: parseInt(result[1], 16),
			g: parseInt(result[2], 16),
			b: parseInt(result[3], 16),
			a: parseInt(result[4], 16)
		} : null;
	};

    /**
	 * Handle transparency behavior
	 *
	 * @method
	 * @private
	 */
	OrbitalNavigation.prototype.handleTransparency = function () {
		var nbObjHit = 0;

		// instanciate the shading manager
		if (this._shadingManager === undefined || this._shadingManager === null) {
			this._shadingManager = this.buildShadingManager(); // jshint ignore:line
		}


		// there is no transparency if there is no target 
		if (this.targetObject !== undefined && this.targetObject !== null) {
			//get the position of the bounding sphere center of the target
			var bSphere = this.targetObject.getBoundingSphere();

			if (bSphere !== undefined && bSphere !== null) {
				var targetPosition = new DSMath.Vector3D();
				targetPosition.set(bSphere.center.x, bSphere.center.y, bSphere.center.z);

				var rayCamToTarget = new STU.Ray();
				rayCamToTarget.origin = this._camera.getPosition();
				var camToTarget = DSMath.Vector3D.sub(targetPosition, this._camera.getPosition());

				rayCamToTarget.direction = this._camera.getForward();
				rayCamToTarget.setLength(camToTarget.norm() - bSphere.radius - 1);

				var objBetweenCamAndTarget = this._renderMngr._pickFromRay(rayCamToTarget);
				nbObjHit = objBetweenCamAndTarget.length;

				//get the renderable of objects bewteen the camera and the target 
				for (var i = 0; i < objBetweenCamAndTarget.length; ++i) {
					var actor = objBetweenCamAndTarget[i].getActor();
					this._actorParam.shadingIft = actor.CATI3DXGraphicalProperties;
					this._actorParam.isProduct = false;
					if (actor instanceof STU.ProductActor) {
						this._actorParam.isProduct = true;
						this._actorParam.shadingIft = actor.CATI3DGeoVisu;

					} else if (this._actorParam.startColor.r === -1) {
						this._actorParam.startColor.r = actor.CATI3DXGraphicalProperties.GetRed();
						this._actorParam.startColor.g = actor.CATI3DXGraphicalProperties.GetGreen();
						this._actorParam.startColor.b = actor.CATI3DXGraphicalProperties.GetBlue();
					}
					break; //only the first obstacle is handled for now
				}
			}
		}

		//transparency animation
		if (this._actorParam.shadingIft !== null && this._actorParam.shadingIft !== undefined) {
			var animDuration = 0.2;
			var transpAmount = -1;

			if (animDuration > this._transparencyTime || nbObjHit === 0) {
				this._transparencyTime += this._deltaTime;
				transpAmount = this.easeInOutQuad(this._transparencyTime, 1, -1, animDuration);
			}

			if (this._transparencyTime > 2 * animDuration) {
				//cleaning
				if (this._actorParam.isProduct === true) {
					this._shadingManager.RemoveTransparency();
				} else {
					var c = this._actorParam.startColor;
					this._actorParam.shadingIft.SetColor(c.r, c.g, c.b);
					this._actorParam.shadingIft.SetOpacity(255);
					this._actorParam.startColor.r = -1;
				}

				this._actorParam.shadingIft = null;
				this._transparencyTime = 0;
			}

			if (this._actorParam.shadingIft !== null && transpAmount !== -1) {
				var color = this.hexToRgba(this._transparencyColor);
				this._shadingManager.RemoveTransparency();

				transpAmount += color.a / 255;
				transpAmount = Math.min(transpAmount, 1) * 255;
				transpAmount = Math.max(transpAmount, 0);


				if (this._actorParam.isProduct) {
					this._shadingManager.SetTransparencyColor(color.r, color.g, color.b, transpAmount);
					this._shadingManager.SetTransparent(this._actorParam.shadingIft);
				} else {
					this._actorParam.shadingIft.SetColor(color.r, color.g, color.b);
					this._actorParam.shadingIft.SetOpacity(transpAmount);
				}

			}

		}
	};

    /**
	 * Handle collision detection
	 *
	 * @method
	 * @private
	 * @param  {DSMath.Vector3D} iCameraPosition Position of the camera
	 * @param  {DSMath.Vector3D} iMotionVector Displacement vector of the camera
	 */
	OrbitalNavigation.prototype.handleCollision = function (iCameraPosition, iMotionVector) {
		var cameraSize = 300;
		var nbTry = 2;

		do {
			var speedVectorNorm = iMotionVector.norm();
			var collisionRay = new STU.Ray();

			collisionRay.origin.x = iCameraPosition.x;
			collisionRay.origin.y = iCameraPosition.y;
			collisionRay.origin.z = iCameraPosition.z;

			collisionRay.direction.x = iMotionVector.x / speedVectorNorm;
			collisionRay.direction.y = iMotionVector.y / speedVectorNorm;
			collisionRay.direction.z = iMotionVector.z / speedVectorNorm;

			collisionRay.setLength(speedVectorNorm + cameraSize);

			//var objHit = this._renderMngr._pickFromRay(collisionRay);
			var objHit;
			var myLocation = this._camera.getLocation();
			if (myLocation === null || myLocation === undefined) {
				objHit = this._renderMngr._pickFromRay(collisionRay);
			}
			else {
				objHit = this._renderMngr._pickFromRay(collisionRay, true, false, myLocation);
			}

			if (objHit.length > 0) {
				var n = objHit[0].getNormal();

				var v = iMotionVector.clone();
				v.x /= speedVectorNorm;
				v.y /= speedVectorNorm;
				v.z /= speedVectorNorm;

				var w = DSMath.cross(n, v);

				var d = DSMath.cross(n, w);
				var newSpeed = iMotionVector.dot(d);

				v.multiplyScalar(cameraSize);
				var bodySidePt = DSMath.Vector3D.add(v, iCameraPosition);
				var camBody2Obstacle = DSMath.Vector3D.sub(objHit[0].getPoint(), bodySidePt);

				iMotionVector.set(camBody2Obstacle.x + newSpeed * d.x, camBody2Obstacle.y + newSpeed * d.y, camBody2Obstacle.z + newSpeed * d.z);
				nbTry = nbTry - 1;
			} else {
				nbTry = 0;
			}
		} while (nbTry > 0);
	};

    /**
	 * Get the bounding sphere center of the target
	 *
	 * @method
	 * @private
	 * @param {DSMath.Vector3D} oTargetPosition Position of the target
	 */
	OrbitalNavigation.prototype.getTargetPosition = function (oTargetPosition, iRef) {
		// orbit around the bounding sphere center of the target
		//[IR-636801] We do not compute the bounding sphere at each frame. 
		//It is computed at start of the experience and each time the camera's target changes, 
		//Then we compute the vector bewteen its center and the target actor origin
		//To retrieve the BSphere center, we add this vector to the target actor position.
		if (this.targetObject !== undefined && this.targetObject !== null) {
			if (this._bsCenter2TargetOriginVector !== null && this._bsCenter2TargetOriginVector !== undefined) {
				var targetPos = this.targetObject.getPosition();
				var computedPos = DSMath.Vector3D.add(targetPos, this._bsCenter2TargetOriginVector);

				if (iRef instanceof DSMath.Transformation) {
					var invRefTransfo = iRef.getInverse();
					var posInWorldRef = new DSMath.Point(computedPos.x, computedPos.y, computedPos.z);
					var posInRef = posInWorldRef.clone();
					posInRef.applyTransformation(invRefTransfo);
					oTargetPosition.set(posInRef.x, posInRef.y, posInRef.z);
				}
				else {
					oTargetPosition.set(computedPos.x, computedPos.y, computedPos.z);
				}

			} else {
				var nodePos = this.targetObject.getPosition(iRef);
				oTargetPosition.set(nodePos.x, nodePos.y, nodePos.z);
			}
		}
	};

	//[IR-636801] We do not compute the bounding sphere at each frame. 
	//It is computed at start of the experience and each time the camera's target changes, 
	//Then we compute the vector bewteen its center and the target actor origin
	//To retrieve the BSphere center, we add this vector to the target actor position.
    /*OrbitalNavigation.prototype.getTargetPosition_old = function (oTargetPosition, iRef) {
        // orbit around the bounding sphere center of the target
        if (this.targetObject !== undefined && this.targetObject !== null) {

            // ca ca renvoit la bsphere dans le repere de son parent (?)
            var bSphere = this.targetObject.getBoundingSphere();
            if (bSphere !== undefined && bSphere !== null) {

                var parent = this.targetObject.getParent();
                if (parent instanceof STU.Actor3D) {
                    var parentTransformInRef = parent.getTransform(iRef);

                    var sphereCenterInParentRef = new DSMath.Point(bSphere.center.x, bSphere.center.y, bSphere.center.z);
                    var sphereCenterInRef = sphereCenterInParentRef.clone();
                    sphereCenterInRef.applyTransformation(parentTransformInRef);
                    oTargetPosition.set(sphereCenterInRef.x, sphereCenterInRef.y, sphereCenterInRef.z);
                }
                    // je n'ai pas de parent Actor3D => je suis directement sous le world
                else {
                    if (iRef === "World") { // ici rien a faire : bSphere est dans le Ref monde et iRef est le ref monde
                        oTargetPosition.set(bSphere.center.x, bSphere.center.y, bSphere.center.z);
                    }
                    else if (iRef === "Location") { // ici il y a un pb : on n'a pas de scene
                        oTargetPosition.set(bSphere.center.x, bSphere.center.y, bSphere.center.z);
                    }
                    else if (iRef instanceof DSMath.Transformation) {
                        // iRef * PosInRef = PosInWorld
                        // PosInRef = invRefTransfo*PosInWorld
                        var invRefTransfo = iRef.getInverse();

                        var sphereCenterInWorldRef = new DSMath.Point(bSphere.center.x, bSphere.center.y, bSphere.center.z);
                        var sphereCenterInRef = sphereCenterInWorldRef.clone();
                        sphereCenterInRef.applyTransformation(invRefTransfo);
                        oTargetPosition.set(sphereCenterInRef.x, sphereCenterInRef.y, sphereCenterInRef.z);
                    }
                }

            } else {
                var nodePos = this.targetObject.getPosition(iRef);
                oTargetPosition.set(nodePos.x, nodePos.y, nodePos.z);
            }
        }
    };*/

    /**
	 * Update method called each frames
	 *
	 * @method
	 * @private
	 */
	OrbitalNavigation.prototype.update = function () {
		
		var cameraPosition, targetPosition;

		var needToUpdateCamera = false;

		//check if the maximum distance is bigger than the minimum distance 
		if (this.minimumDistance > this.maximumDistance || this.minimumDistance < 0 || this.maximumDistance < 0) {
			return this;
		}

		this.handleReticule();

		targetPosition = new DSMath.Vector3D();
		this.getTargetPosition(targetPosition, "Location");
		this.handleFocusOn(targetPosition); // if there is a focus transition going on, targetPosition will be modified


		//if target has moved since last frame -> refresh of the cam position
		if (this._targetPosLastFrame instanceof DSMath.Vector3D) {
			var deltaTargetPosition = DSMath.Vector3D.sub(this._targetPosLastFrame, targetPosition);
			var deltaTargetNorm = deltaTargetPosition.squareNorm();

			if (deltaTargetNorm > 0.01) {
				cameraPosition = this._camera.getPosition("Location");
				cameraPosition.sub(deltaTargetPosition);
				this._camera.setPosition(cameraPosition, "Location");
				this._camera.lookAt(targetPosition);

				// can compute bbox only when moving on z axis
				if (deltaTargetPosition.z != 0)
				{
					//this.computeBoundingBox();

					// could also move only the bbox limits like this instead of recalculate it:
					this._bBoxCenterZ -= deltaTargetPosition.z; 
					this._bBoxLowZ -= deltaTargetPosition.z; 
					this._bBoxHighZ -= deltaTargetPosition.z; 
				}
			}
		}
		//save current target position
		this._targetPosLastFrame = targetPosition;

		//Camera zooming 
		var oldCameraPosition = this._camera.getPosition("Location");
		this.handleZooming(this._deltaTime, this._camera, targetPosition);
		if (this.useGamepad || this.useKeyboard) this.handleZoomingGamepad(this._deltaTime, this._camera, targetPosition);
		cameraPosition = this._camera.getPosition();

		//Goto Transition
		var lockCamRotation = this.handleGoTo(cameraPosition); // during a goTo Transition camera rotation is locked 
		needToUpdateCamera = needToUpdateCamera || lockCamRotation; // if there is a goTo transition going on, cameraPosition will be modified

		var deltaX = 0;
		var deltaY = 0;

		//Camera rotation
		if (!lockCamRotation) {

			//manage mouse inputs
			if ((this.navigateOnClick === false && this._isCursorInNeutralZone === false) ||
				(this.navigateOnClick === true && this._isMousePressed === true)) {
				var mouseXAxis = this._inputManager.axis2.x;
				var mouseYAxis = this._inputManager.axis2.y;

				var refX = 0;
				var refY = 0;
				if (this._useMousePosAsRefWithNavigateOnClick) {
					refX = this._lastMousePosition.x;
					refY = this._lastMousePosition.y;
				}

				deltaX = this.followMouse === true ? (mouseXAxis - this._lastMousePosition.x) / this._deltaTime : mouseXAxis - refX;
				deltaY = this.followMouse === true ? (mouseYAxis - this._lastMousePosition.y) / this._deltaTime : mouseYAxis - refY;

				//[IR-520057] : Input manager send inversed input values concerning mouse input however keyboard inputs are correct. 
				//Quick fix: inverse received value here.
				deltaY *= -1;
				deltaX *= -1;

				if (this.followMouse === true) {
					this._lastMousePosition.x = mouseXAxis;
					this._lastMousePosition.y = mouseYAxis;
				}
			}

			//manage keyboard inputs
			if (this.useKeyboard === true) {
				var keyboardXAxis = this._inputManager.axis1.x;
				var keyboardYAxis = this._inputManager.axis1.y;

				deltaX += keyboardXAxis;
				deltaY += keyboardYAxis;
			}

			//manage gamepad inputs
			if (this.useGamepad === true) {
				var gamepadXAxis = this._inputManager.axis3.x;
				var gamepadYAxis = this._inputManager.axis3.y;

				deltaX += gamepadXAxis;
				deltaY += gamepadYAxis;
			}

			//before.dcn23
			/*
			if (this.navigateOnClick === true && this._isTouchPressed === true) {
				var touchXAxis = this._inputManager.axis6.x * -1;
				var touchYAxis = this._inputManager.axis6.y * -1;

				deltaX = this.followMouse === true ? (touchXAxis - this._inputManager.axis6.lastTouchPosition.x) / this._deltaTime : touchXAxis;
				deltaY = this.followMouse === true ? (touchYAxis - this._inputManager.axis6.lastTouchPosition.y) / this._deltaTime : touchYAxis;

				this._inputManager.axis6.lastTouchPosition.x = touchXAxis;
				this._inputManager.axis6.lastTouchPosition.y = touchYAxis;
			}
			*/

			//manage touch input
			if (this.useTouch === true)
			{
				if (this.navigateOnClick === true && this._isTouchPressed === true) {
					var touchXAxis = this._inputManager.axis6.x;
					var touchYAxis = this._inputManager.axis6.y;
	
					// [DCN23] - Quick fix - add this three lines below to disable the teleoprtation (because the lastTouchPosition === 0,0);
					// => espacially in the case when using one finger then putting another and then let go of the first
					// => continuous use of screen but still detect the lift of one causing the event "lift finger" so it reset the value of lastTouchPostion to 0;0 but do not put it back to a clsoe position like when you first touch the screen (kinda firstTouchposition)
					// this situation happens because the flag for this._isTouchPressed didn't raise down BUT the onGestureDragEndEvent or onTouchReleaseEvent (for instance) is raised ! so the lastTouchPos is still reset !!
					// therefore i need to do the check myself here = like in the StuNavigation.js Line 308 to 321. I think this fix can be put inside the StuNavigation.js but because there can still be dependance build on it I prefer not to modify it.
					if (this._inputManager.axis6.lastTouchPosition.x == 0 && this._inputManager.axis6.lastTouchPosition.y == 0) {
						this._inputManager.axis6.lastTouchPosition.x = this._inputManager.axis6.x;
						this._inputManager.axis6.lastTouchPosition.y = this._inputManager.axis6.y;
					}
	
					//deltaX = this.followMouse === true ? (touchXAxis - this._inputManager.axis6.lastTouchPosition.x) / this._deltaTime : touchXAxis;
					//deltaY = this.followMouse === true ? (touchYAxis - this._inputManager.axis6.lastTouchPosition.y) / this._deltaTime : touchYAxis;
					deltaX += this.followMouse === true ? (touchXAxis - this._inputManager.axis6.lastTouchPosition.x) / this._deltaTime : touchXAxis - this._inputManager.axis6.lastTouchPosition.x;
					deltaY += this.followMouse === true ? (touchYAxis - this._inputManager.axis6.lastTouchPosition.y) / this._deltaTime : touchYAxis - this._inputManager.axis6.lastTouchPosition.y;
	
					//[03/10/2022 - DCN23] Quick fix: move the inverse value here instead of inside the defintion of touchXAxis,touchYAxis
					deltaX *= -1;
					deltaY *= -1;
	
					if (this.followMouse === true) {
						this._inputManager.axis6.lastTouchPosition.x = touchXAxis;
						this._inputManager.axis6.lastTouchPosition.y = touchYAxis;
					}
				}
			}
			
			
			//Automatic rotation
			if (this.automaticRotationAtIdle) {
				if (deltaX === 0 && deltaY === 0) {
					this._elapsedTimeAtIdle += this._deltaTime;
				}
				else {
					this._elapsedTimeAtIdle = 0;
					if (this._isMovingAroundByTimer) {
						this._isMovingAroundByTimer = false;
					}
				}

				if (this._elapsedTimeAtIdle >= this.automaticRotationTimer) {
					if (!this._isMovingAroundByTimer) {
						this._isMovingAroundByTimer = true;
					}
				}
			}
		}

		//Move around capacity
		if (this._isMovingAroundByTimer || this._isMovingAroundByCapacity) { // override  inputs when moving around
			deltaY = 0;
			deltaX = 0.3;
		}

		// Handle axis swap
		//deltaX *= (this.invertXAxis === true ? -1 : 1);
		//deltaY *= (this.invertYAxis === true ? -1 : 1);

		//We don't want inertia with touch
		//if (!this._useTouch && this._inputManager.axis6.y === 0 && this._inputManager.axis6.x === 0 && this._inertiaON) {
		if (false) {
			var isMoving = (deltaX !== 0 || deltaY !== 0);
			var isChangingDirectionX = (this._sign(deltaX) !== this._sign(this._MemDeltaX)) && this._MemDeltaX != 0 && deltaX != 0;
			var isChangingDirectionY = (this._sign(deltaY) !== this._sign(this._MemDeltaY)) && this._MemDeltaY != 0 && deltaY != 0;

			if (isMoving && !isChangingDirectionX /*&& !isChangingDirectionY*/) {
				if (this._MemDeltaX > 0 || this._MemDeltaY > 0) {
					if (this._isDecelerating) {
						var currentEase = this.easeInOutQuad(this._movementEasingCurrentTime, 0, 1, this._movementEasingDuration);
						this._movementEasingCurrentTime = this.getCorrespondingTime(currentEase, this.easeInOutSine, 0, 1, this._movementEasingDuration);
					}
				}

				var maxFactr = (this.followMouse) ? 10 : 1;
				this._movementEasingCurrentTime += this._deltaTime * maxFactr; // for follow mouse nav, we could have set directly currentTime = Duration, but we still want smoothing so we speed thing up
				this._movementEasingCurrentTime = (this._movementEasingCurrentTime > (this._movementEasingDuration)) ? (this._movementEasingDuration) : this._movementEasingCurrentTime;
				var currentEase = this.easeOutQuad(this._movementEasingCurrentTime, 0, 1, (this._movementEasingDuration));
				deltaX *= currentEase;
				deltaY *= currentEase;

				this._MemDeltaX = deltaX;
				this._MemDeltaY = deltaY;

				this._isDecelerating = false;
			}
			else if (this.followMouse && this._inputManager.buttonsState[8]) {
				this._MemDeltaX = 0;
				this._MemDeltaY = 0;
			}
			else {
				if (!this._isDecelerating) {
					var currentEase = this.easeOutQuad(this._movementEasingCurrentTime, 0, 1, (this._movementEasingDuration));
					this._movementEasingCurrentTime = this.getCorrespondingTime(currentEase, this.easeInOutQuad, 0, 1, this._movementEasingDuration);
					this._isDecelerating = true;
				}

				var maxFactr = (this.followMouse) ? 100 : 5;
				var factr = (isChangingDirectionX || isChangingDirectionY) ? maxFactr : 1;
				this._movementEasingCurrentTime -= this._deltaTime * factr;
				this._movementEasingCurrentTime = (this._movementEasingCurrentTime < 0) ? 0 : this._movementEasingCurrentTime;
				var currentEase = this.easeInOutQuad(this._movementEasingCurrentTime, 0, 1, this._movementEasingDuration);
				deltaX = this._MemDeltaX * currentEase;
				deltaY = this._MemDeltaY * currentEase;

				if (deltaX == 0) { this._MemDeltaX = 0; }
				if (deltaY == 0) { this._MemDeltaY = 0; }
			}
		}

		//no need to compute rotation if there is no input
		//if (deltaX !== 0 || deltaY !== 0) {
		if (deltaX !== 0 || deltaY !== 0 || this.inertiaEnabled) {
			var targetToCam = DSMath.Vector3D.sub(cameraPosition, targetPosition);
			var locationZ = new DSMath.Vector3D();

			locationZ.set(0, 0, 1);

			var rightQuat = new DSMath.Quaternion();
			rightQuat.makeRotation(this._camera.getRight("Location"), deltaY * STU.Math.DegreeToRad * this.verticalRotationSensitivity * this._deltaTime);

			var zAxisQuat = new DSMath.Quaternion();
			zAxisQuat.makeRotation(locationZ, deltaX * STU.Math.DegreeToRad * this.horizontalRotationSensitivity * this._deltaTime);

			var composedRotQuat = DSMath.Quaternion.multiply(rightQuat, zAxisQuat);

			///////////////////////////////=======================================================================================/////////////////////////////////
			if (this.inertiaEnabled) {
				this._smoothnessQueue.push(composedRotQuat);

				//Global variable which holds the amount of rotations which
				//need to be averaged.
				var addAmount = 0; // this._smoothnessQueue.length + 1;

				//Global variable which represents the additive quaternion
				var addedRotation = this._smoothnessQueue.shift(); //new DSMath.Quaternion(); //Quaternion.identity;

				//The averaged rotational value
				var averageRotation;

				//Loop through all the rotational values.
				for (var i = 0; i < this._smoothnessQueue.length; i++) {

					//Amount of separate rotational values so far
					addAmount++;

					addedRotation = DSMath.Quaternion.slerp(this._smoothnessQueue[i], addedRotation, addAmount / this._smoothnessQueue.length);
					averageRotation = addedRotation.clone();
					averageRotation.normalize();
				}
				composedRotQuat = averageRotation;
			}
			///////////////////////////////=======================================================================================/////////////////////////////////

			var targetToCamPoint = new DSMath.Point();
			targetToCamPoint.set(targetToCam.x, targetToCam.y, targetToCam.z);
			var rotatedTargetToCamPoint = targetToCamPoint.applyQuaternion(composedRotQuat);//var rotatedTargetToCamPoint = composedRotQuat.rotate(targetToCamPoint);

			cameraPosition.set(rotatedTargetToCamPoint.x, rotatedTargetToCamPoint.y, rotatedTargetToCamPoint.z);

			var targetToNewCamNormalized = cameraPosition.clone();

			targetToNewCamNormalized.normalize();

			var w = DSMath.Vector3D.cross(cameraPosition, locationZ);
			var dotCpWz = w.dot(this._camera.getRight("Location"));
			var dotWzT2C = locationZ.dot(targetToNewCamNormalized);

			if (this.keepOverTheFloor === true) {
				// compute the bounding box (can optimize this to answer the [IR-636801])
				//this.computeBoundingBox();

				// lower limit keep over the floor
				var lowerZBoundingBox = this._bBoxLowZ; //iBBox.low.z is negativ

				// upper limit to reproduce the behavior of keep over the ground but for the top
				var upperZBoundingBox = this._bBoxHighZ;

				// if camera is going below the floor (below the bounding spehre) (be careful we need to add + targetPosition.z to cameraPosition.z because for some reason it is not absolute but cameraPostion relative to the object ??) OR upper the top limit
				if (dotCpWz >= 0 || Math.abs(dotWzT2C) > 0.9998 || cameraPosition.z + targetPosition.z < lowerZBoundingBox || (cameraPosition.z + targetPosition.z > upperZBoundingBox && this._KeepOffTheSky) ) {//if (dotCpWz >= 0 || Math.abs(dotWzT2C) > 0.9998 || zCamNorm < zBSNorm) { //before.DCN23
					if (this._isOrbitConstraintOK === true) {
						targetToCamPoint.set(targetToCam.x, targetToCam.y, targetToCam.z);
						rotatedTargetToCamPoint = targetToCamPoint.applyQuaternion(zAxisQuat);//rotatedTargetToCamPoint = zAxisQuat.rotate(targetToCamPoint);
						cameraPosition.set(rotatedTargetToCamPoint.x, rotatedTargetToCamPoint.y, rotatedTargetToCamPoint.z);
					}
				} else {
					this._isOrbitConstraintOK = true;
				}
			}
			// Avoid rotation glitches when the camera is closed to the poles
			// dotCpWz >= 0 ensure that the rotation won't go beyond poles
			// 0.9998 is the result of cos(1°), it's the angle between world Z and targetToCam vectors
			else if (dotCpWz >= 0 || Math.abs(dotWzT2C) > 0.9998) {
				targetToCamPoint.set(targetToCam.x, targetToCam.y, targetToCam.z);
				rotatedTargetToCamPoint = targetToCamPoint.applyQuaternion(zAxisQuat);//rotatedTargetToCamPoint = zAxisQuat.rotate(targetToCamPoint);
				cameraPosition.set(rotatedTargetToCamPoint.x, rotatedTargetToCamPoint.y, rotatedTargetToCamPoint.z);
			}

			cameraPosition.add(targetPosition);
			needToUpdateCamera = true;

		}


		// End Use of STU API
		if (this.avoidCollision === true) {
			var speedVector = DSMath.Vector3D.sub(cameraPosition, oldCameraPosition);
			this.handleCollision(oldCameraPosition, speedVector);
			cameraPosition = DSMath.Vector3D.add(oldCameraPosition, speedVector);
		}

		if (needToUpdateCamera === true) {
			this._camera.setPosition(cameraPosition, "Location");
			this._camera.lookAt(targetPosition, "Location");
		}

		// its has to be after updating the camera
		if (this.hideObstacles === true) {
			this.handleTransparency();
		}

		return this;
	};

	// Expose in STU namespace.
	STU.OrbitalNavigation = OrbitalNavigation;

	return OrbitalNavigation;
});

define('StuCameras/StuOrbitalNavigation', ['DS/StuCameras/StuOrbitalNavigation'], function (OrbitalNavigation) {
	'use strict';

	return OrbitalNavigation;
});
