
define('DS/StuCameras/StuInputManager', ['DS/StuCore/StuContext', 'DS/StuModel/StuInstance',
	'DS/EPInputs/EPMousePressEvent', 'DS/EPInputs/EPMouseReleaseEvent', 'DS/EPInputs/EPMouseMoveEvent', 'DS/EPInputs/EPMouseMoveEvent', 'DS/EPInputs/EPMouseWheelEvent',
	'DS/EPInputs/EPKeyboardPressEvent', 'DS/EPInputs/EPKeyboardReleaseEvent', 'DS/EPInputs/EPGamepadPressEvent', 'DS/EPInputs/EPGamepadReleaseEvent', 'DS/EPInputs/EPGamepadAxisEvent',
	'DS/EPInputs/EPGestureDragBeginEvent', 'DS/EPInputs/EPGestureDragEndEvent', 'DS/EPInputs/EPGestureDragEvent', 'DS/EPInputs/EPGesturePinchBeginEvent', 'DS/EPInputs/EPGesturePinchEvent',
	'DS/EPInputs/EPGesturePinchEndEvent', 'DS/EPInputs/EPGestureDoubleTapEvent', 'DS/EPInputs/EPGestureTwoFingersTapEvent', 'DS/EPInputs/EPGestureHoldBeginEvent', 'DS/EPInputs/EPGestureHoldEndEvent'],
	function (STU, Instance) {
		'use strict';

		STU.eInputTrigger = {
			eTrigger1: 0, eTrigger2: 1, eTrigger3: 2, eTrigger4: 3,
			eTrigger5: 4, eTrigger6: 5, eTrigger7: 6, eTrigger8: 7
		};

		var InputManager = function () {
			var self = this;

			Instance.call(this);
			this.componentInterface = this.protoId;

			this.name = "InputManager";

			//////////////////////////////////////////////////////////////////////////
			// Properties that should NOT be visible in UI
			//////////////////////////////////////////////////////////////////////////

			// First axis used to store keyboard or mouse displacement.
			// 
			// The keyboard modifies those value when up/down/right/left
			// keys are called.
			// The mouse modifies those value when mouse is moved, but only
			// if mouseAxis is set to 1
			this.axis1 = {
				"x": 0,
				"y": 0,
				"z": 0
			};

			// Second axis used to store mouse displacement, if mousAxis is set to 2
			this.axis2 = {
				"x": 0,
				"y": 0,
				"z": 0
			};

			// Third axis used to store Gamepad Left joystick input, if mousAxis is set to 2
			this.axis3 = {
				"x": 0,
				"y": 0,
				"z": 0
			};

			// Fourth axis used to store Gamepad Right joystick input, if mousAxis is set to 2
			this.axis4 = {
				"x": 0,
				"y": 0,
				"z": 0
			};

			// Fifth axis used to store Gamepad Trigger input, if mousAxis is set to 2
			this.axis5 = {
				"x": 0,
				"y": 0,
				"z": 0
			};

			// Sixth axis used to store Touch input
			this.axis6 = {
				"x": 0,
				"y": 0,
				"z": 0,
				"lastTouchPosition": new DSMath.Point()
			};

			// Seventh axis used to store Touch input
			this.axis7 = {
				"x": 0,
				"y": 0,
				"z": 0,
				"lastTouchPosition": new DSMath.Point(),
				"firstTouchPosition": new DSMath.Point()
			};

			// Heigth axis used to store Touch input
			this.axis8 = {
				"x": 0, //-0.5,
				"y": 0, //0.5,
				"z": 0,
				"lastTouchPosition": new DSMath.Point()
			};
			// this structure keeps the state of each button
			// and can be accessed to know if the button has been 
			// activated or not
            /*
            this.buttonsState = {
                "trigger1": false,
                "trigger2": false,
                "trigger3": false,
                "trigger4": false,
                "trigger5": false,
                "trigger6": false,
                "trigger7": false,
                "trigger8": false,	
                "mouse":false,		
            };
            */

			this.buttonsState = [false, false, false, false,
				false, false, false, false, false];

			this.gamepad = null;

			this.gamepadButtonsState = [false, false, false,
				false, false, false];

			this.touchProperties = {
				"lastPinchDistance": 0,
				"isHold": false,
				"doubleTap": false,
				"idLeft": -1,
				"idRight": -1
			};

			this.useDoubleTouch = false;

			//////////////////////////////////////////////////////////////////////////
			// Properties that should be visible in UI
			//////////////////////////////////////////////////////////////////////////

			// set to TRUE if mouse is used as input
			this.useMouse = true; // true;

			// set to TRUE if keyboard is used as input
			this.useKeyboard = true; //true;

			// set to TRUE if Gamepad is used as input
			this.useGamepad = true; //true;

			// set to TRUE if Touch is used as input
			this.useTouch = true;

			// set to TRUE if Touch is used (to desactivate mouse in StuNavigation)
			this.touchPending = false

			// set the axis to be used to store mouse displacement
			this.mouseAxis = 1; // true

			// set to TRUE if Y mouse displacement should be inverted
			this.mouseInvertY = false;  // false

			// set to TRUE if X mouse displacement should be inverted
			this.mouseInvertX = false;  // false

			// set to TRUE if mouse wheel displacement should be inverted
			this.mouseInvertWheel = true;  // true

			// set the mouse sensitivity
			this.mouseSensitivity = 1;

			// set the key code that generate the Up event
			this.keyUp = EP.Keyboard.EKey.eUp;

			// set the key code that generate the Down event
			this.keyDown = EP.Keyboard.EKey.eDown;

			// set the key code that generate the Right event
			this.keyRight = EP.Keyboard.EKey.eRight;

			// set the key code that generate the Left event
			this.keyLeft = EP.Keyboard.EKey.eLeft;

			// set the key code that generate the Front event
			this.keyFront = EP.Keyboard.EKey.ePageUp;

			// set the key code that generate the Back event
			this.keyBack = EP.Keyboard.EKey.ePageDown;

			// set the key code that generate the Trigger 1 event
			this.keyTrigger1 = EP.Keyboard.EKey.eSpace;

			// set the key code that generate the Trigger 2 event
			this.keyTrigger2 = EP.Keyboard.EKey.eShift;

			// set the key code that generate the Trigger 3 event
			this.keyTrigger3 = EP.Keyboard.EKey.eControl;

			// set the key code that generate the Trigger 4 event
			this.keyTrigger4 = EP.Keyboard.EKey.eNone;

			// set the key code that generate the Trigger 5 event
			this.keyTrigger5 = EP.Keyboard.EKey.eNone;

			// set the key code that generate the Trigger 6 event
			this.keyTrigger6 = EP.Keyboard.EKey.eNone;

			// set the key code that generate the Trigger 7 event
			this.keyTrigger7 = EP.Keyboard.EKey.eNone;

			// set the key code that generate the Trigger 8 event
			this.keyTrigger8 = EP.Keyboard.EKey.eNone;

			// set to TRUE if X mouse displacement should be inverted
			this.keyboardInvertX = false;  // false

			// set to TRUE if Y mouse displacement should be inverted
			this.keyboardInvertY = false;  // false

			// set to TRUE if Z mouse displacement should be inverted
			this.keyboardInvertZ = false;  // false

			// set to TRUE if X Gamepad's joystick input should be inverted
			this.gamepadInvertX = false;

			// set to TRUE if Y Gamepad's joystick input should be inverted
			this.gamepadInvertY = false;


			this.onMouseDown = function (event) {
				if (!self.useMouse) {
					return;
				}
				self.buttonsState[8] = true;
				self.touchPending = false; // bc in webExperience a mouseDown event triggers a touch Drag event as well => need to shutdown touch when using mouse
			}; // onMouseDown.

			this.onMouseUp = function (event) {
				if (!self.useMouse) {
					return;
				}
				self.buttonsState[8] = false;
			}; // onMouseUp.

			this.onMouseMove = function (event) {
				var axis;

				if (!self.useMouse) {
					return;
				}

				var viewerSize = STU.RenderManager.getInstance().getViewerSize();

				axis = self.mouseAxis === 1 ? self.axis1 : self.axis2;
				axis.x = -(event.getPosition().x / viewerSize.x * 2 - 1) * self.mouseSensitivity;
				axis.y = -(event.getPosition().y / viewerSize.y * -2 + 1) * self.mouseSensitivity;
				if (self.mouseInvertY) {
					axis.y *= -1;
				}
				if (self.mouseInvertX) {
					axis.x *= -1;
				}

			}; // onMouseMove.

			this.onMouseWheel = function (event) {
				var axis;

				if (!self.useMouse) {
					return;
				}

				axis = self.mouseAxis === 1 ? self.axis1 : self.axis2;
				axis.z = event.getWheelDeltaValue() * self.mouseSensitivity;

				if (self.mouseInvertWheel) {
					axis.z *= -1;
				}
			}; // onMouseWheel.

			this.onKeyDown = function (event) {
				if (!self.useKeyboard) {
					return;
				}

				var axisKeyChanged = false;

				switch (event.getKey()) {
					case self.keyTrigger1: self.buttonsState[0] = true; break;
					case self.keyTrigger2: self.buttonsState[1] = true; break;
					case self.keyTrigger3: self.buttonsState[2] = true; break;
					case self.keyTrigger4: self.buttonsState[3] = true; break;
					case self.keyTrigger5: self.buttonsState[4] = true; break;
					case self.keyTrigger6: self.buttonsState[5] = true; break;
					case self.keyTrigger7: self.buttonsState[6] = true; break;
					case self.keyTrigger8: self.buttonsState[7] = true; break;

					case self.keyUp: axisKeyChanged = true; break;
					case self.keyRight: axisKeyChanged = true; break;
					case self.keyDown: axisKeyChanged = true; break;
					case self.keyLeft: axisKeyChanged = true; break;
					case self.keyBack: axisKeyChanged = true; break;
					case self.keyFront: axisKeyChanged = true; break;
				}

				if (axisKeyChanged) {
					self.updateKbdAxis();
				}
			}; // onKeyDown.

			this.onKeyUp = function (event) {
				if (!self.useKeyboard) {
					return;
				}

				var axisKeyChanged = false;

				switch (event.getKey()) {
					case self.keyTrigger1: self.buttonsState[0] = false; break;
					case self.keyTrigger2: self.buttonsState[1] = false; break;
					case self.keyTrigger3: self.buttonsState[2] = false; break;
					case self.keyTrigger4: self.buttonsState[3] = false; break;
					case self.keyTrigger5: self.buttonsState[4] = false; break;
					case self.keyTrigger6: self.buttonsState[5] = false; break;
					case self.keyTrigger7: self.buttonsState[6] = false; break;
					case self.keyTrigger8: self.buttonsState[7] = false; break;

					case self.keyUp: axisKeyChanged = true; break;
					case self.keyRight: axisKeyChanged = true; break;
					case self.keyDown: axisKeyChanged = true; break;
					case self.keyLeft: axisKeyChanged = true; break;
					case self.keyBack: axisKeyChanged = true; break;
					case self.keyFront: axisKeyChanged = true; break;
				}

				if (axisKeyChanged) {
					self.updateKbdAxis();
				}
			}; // onKeyUp.

			this.updateKbdAxis = function () {
				var devMng = EP.Devices;

				if (undefined !== devMng && null !== devMng) {
					self.axis1.x = 0;
					self.axis1.y = 0;
					self.axis1.z = 0;

					if (devMng.getKeyboard().isKeyPressed(self.keyUp)) self.axis1.y += 1;
					if (devMng.getKeyboard().isKeyPressed(self.keyRight)) self.axis1.x += 1;
					if (devMng.getKeyboard().isKeyPressed(self.keyDown)) self.axis1.y -= 1;
					if (devMng.getKeyboard().isKeyPressed(self.keyLeft)) self.axis1.x -= 1;
					if (devMng.getKeyboard().isKeyPressed(self.keyFront)) self.axis1.z += 1;
					if (devMng.getKeyboard().isKeyPressed(self.keyBack)) self.axis1.z -= 1;

					if (self.keyboardInvertX) {
						self.axis1.x *= -1;
					}
					if (self.keyboardInvertY) {
						self.axis1.y *= -1;
					}
					if (self.keyboardInvertZ) {
						self.axis1.z *= -1;
					}
				}
			};

			this.onGamepad = function (event) {
				//console.log(this.event);
			};

			this.onGamepadPressEvent = function (event) {
				if (!self.useGamepad) {
					return;
				}
				switch (event.getButton()) {
					case EP.Gamepad.EButton.e1: self.gamepadButtonsState[0] = true; break; //Button A
					case EP.Gamepad.EButton.e2: self.gamepadButtonsState[1] = true; break; //Button B
					case EP.Gamepad.EButton.e3: self.gamepadButtonsState[2] = true; break; //Button X
					case EP.Gamepad.EButton.e4: self.gamepadButtonsState[3] = true; break; //Button Y
					case EP.Gamepad.EButton.e5: self.gamepadButtonsState[4] = true; break; //Button LB
					case EP.Gamepad.EButton.e6: self.gamepadButtonsState[5] = true; break; //Button RB
				}
			};

			this.onGamepadReleaseEvent = function (event) {
				if (!self.useGamepad) {
					return;
				}
				switch (event.getButton()) {
					case EP.Gamepad.EButton.e1: self.gamepadButtonsState[0] = false; break; //Button A
					case EP.Gamepad.EButton.e2: self.gamepadButtonsState[1] = false; break; //Button B
					case EP.Gamepad.EButton.e3: self.gamepadButtonsState[2] = false; break; //Button X
					case EP.Gamepad.EButton.e4: self.gamepadButtonsState[3] = false; break; //Button Y
					case EP.Gamepad.EButton.e5: self.gamepadButtonsState[4] = false; break; //Button LB
					case EP.Gamepad.EButton.e6: self.gamepadButtonsState[5] = false; break; //Button RB
				}
			};

			this.onGamepadAxisEvent = function (event) {
				if (!self.useGamepad) {
					return;
				}

				self.axis3.x = 0;
				self.axis3.y = 0;
				self.axis4.x = 0;
				self.axis4.y = 0;
				self.axis5.x = 0;
				self.axis5.y = 0;

				var gamepad = event.getGamepad();
				self.axis3.x = gamepad.getAxisValue(EP.Gamepad.EAxis.eLSX);
				self.axis3.y = gamepad.getAxisValue(EP.Gamepad.EAxis.eLSY);
				self.axis4.x = gamepad.getAxisValue(EP.Gamepad.EAxis.eRSX);
				self.axis4.y = gamepad.getAxisValue(EP.Gamepad.EAxis.eRSY);
				self.axis5.x = gamepad.getAxisValue(EP.Gamepad.EAxis.eLT);
				self.axis5.y = gamepad.getAxisValue(EP.Gamepad.EAxis.eRT);

				if (self.gamepadInvertX) {
					self.axis3.x *= -1;
					self.axis4.x *= -1;
					self.axis5.x *= -1;
				}
				if (self.gamepadInvertY) {
					self.axis3.y *= -1;
					self.axis4.y *= -1;
					self.axis5.y *= -1;
				}
				//console.log(self.axis3);
			};

			this.onGestureDragBeginEvent = function (e) {
				if (!self.useTouch) {
					return;
				}

				self.touchPending = true;

			};

			this.onGestureDragEndEvent = function (e) {
				if (!self.useTouch) {
					return;
				}

				self.axis6.x = 0;
				self.axis6.y = 0;

				self.touchPending = false;
				self.firstPositionDrag = null;
			};

			this.onGestureDragEvent = function (e) {
				if (!self.useTouch) {
					return;
				}

				var viewerSize = STU.RenderManager.getInstance().getViewerSize();

				self.axis6.x = -(e.position.x / viewerSize.x * 2 - 1);
				self.axis6.y = -(e.position.y / viewerSize.y * -2 + 1);
				if (self.mouseInvertY) {
					self.axis6.y *= -1;
				}
				if (self.mouseInvertX) {
					self.axis6.x *= -1;
				}
			};

			this.onGesturePinchBeginEvent = function (e) {
				if (!self.useTouch) {
					return;
				}

				self.touchPending = true;

				self.touchProperties.lastPinchDistance = e.distance;
			};

			this.onGesturePinchEvent = function (e) {
				if (!self.useTouch) {
					return;
				}

				var distance = self.touchProperties.lastPinchDistance - e.distance;

				if (Math.abs(distance) < 40) {
					return;
				}

				if ((distance) * -1 < 0) { // if spreading between fingers > last spreading 
					self.axis2.z = 1;
				}
				else if ((distance) * -1 > 0) { // if spreading between fingers < last spreading 
					self.axis2.z = -1;
				}

				self.touchProperties.lastPinchDistance = e.distance;
			};

			this.onGesturePinchEndEvent = function (e) {
				if (!self.useTouch) {
					return;
				}

				self.touchPending = false;

				self.touchProperties.lastPinchDistance = 0;
			};

			this.onGestureDoubleTapEvent = function (e) {
				if (!self.useTouch) {
					return;
				}

				self.axis6.z = 1;

				self.touchProperties.doubleTap = true;
			};

			this.onGestureTwoFingersTapEvent = function (e) {
				if (!self.useTouch) {
					return;
				}

				self.axis6.z = -1;
			};

			this.onGestureHoldBeginEvent = function (e) {
				if (!self.useTouch) {
					return;
				}

				self.touchProperties.isHold = true;
			}

			this.onGestureHoldEndEvent = function (e) {
				if (!self.useTouch) {
					return;
				}

				self.touchProperties.isHold = false;
			}

			this.onTouchMoveEvent = function (e) {
				if (!self.useTouch) {
					return;
				}

				var viewerSize = STU.RenderManager.getInstance().getViewerSize();
				var posTempX = -(e.position.x / viewerSize.x * 2 - 1);
				var posTempY = -(e.position.y / viewerSize.y * -2 + 1);

				if (Math.sign(posTempX) == 1) { // Left
					if (self.touchProperties.idLeft == -1 && self.touchProperties.idRight != e.getId()) {
						self.touchProperties.idLeft = e.getId();
					}

					if (self.touchProperties.idLeft == e.getId()) {
						self.axis7.x = posTempX;
						self.axis7.y = posTempY;
						if (self.mouseInvertY) {
							self.axis7.y *= -1;
						}
						if (self.mouseInvertX) {
							self.axis7.x *= -1;
						}
					}
				}
				else if (Math.sign(posTempX) == -1) { // right
					if (self.touchProperties.idRight == -1 && self.touchProperties.idLeft != e.getId()) {
						self.touchProperties.idRight = e.getId();
					}
					if (self.touchProperties.idRight == e.getId()) {
						self.axis8.x = posTempX;
						self.axis8.y = posTempY;
						if (self.mouseInvertY) {
							self.axis8.y *= -1;
						}
						if (self.mouseInvertX) {
							self.axis8.x *= -1;
						}
					}
				}

				if (self.touchProperties.idLeft != -1 || self.touchProperties.idRight != -1) {
					self.touchPending = true;
				}
			}

			this.onTouchReleaseEvent = function (e) {
				if (e.getId() == self.touchProperties.idLeft) {
					self.touchProperties.idLeft = -1;
					self.axis7.x = 0;
					self.axis7.y = 0;
					self.axis7.firstTouchPosition.x = 0;
					self.axis7.firstTouchPosition.y = 0;
					self.axis7.lastTouchPosition.x = 0;
					self.axis7.lastTouchPosition.y = 0;
				}

				if (e.getId() == self.touchProperties.idRight) {
					self.touchProperties.idRight = -1;
					self.axis8.x = 0;
					self.axis8.y = 0;
					self.axis8.lastTouchPosition.x = 0;
					self.axis8.lastTouchPosition.y = 0;
				}

				if (self.touchProperties.idLeft == -1 && self.touchProperties.idRight == -1) {
					self.touchPending = false;
				}
			}

		}; // InputManager.

		InputManager.prototype = new Instance();
		InputManager.prototype.constructor = InputManager;
		InputManager.prototype.protoId = "82749348-8272-4A2E-AB8C-A3AA7EC4491F";
		InputManager.prototype.pureRuntimeAttributes = ["buttonsState"].concat(Instance.prototype.pureRuntimeAttributes);

		InputManager.prototype.onActivate = function (oExceptions) {
			Instance.prototype.onActivate.call(this, oExceptions);

			var em = EP.EventServices;
			if (em !== undefined && em !== null) {				
				em.addListener(EP.MousePressEvent, this.onMouseDown);
				em.addListener(EP.MouseReleaseEvent, this.onMouseUp);
				em.addListener(EP.MouseMoveEvent, this.onMouseMove);
				em.addListener(EP.MouseWheelEvent, this.onMouseWheel);

				em.addListener(EP.KeyboardPressEvent, this.onKeyDown);
				em.addListener(EP.KeyboardReleaseEvent, this.onKeyUp);

				//em.addListener(EP.GamepadEvent, this.onGamepad);
				// Add Listener to get notified
				em.addListener(EP.GamepadPressEvent, this.onGamepadPressEvent);
				em.addListener(EP.GamepadReleaseEvent, this.onGamepadReleaseEvent);
				em.addListener(EP.GamepadAxisEvent, this.onGamepadAxisEvent);

				// LBN9 - Add touch listener
				em.addListener(EP.GestureDragEvent, this.onGestureDragEvent);
				em.addListener(EP.GestureDragBeginEvent, this.onGestureDragBeginEvent);
				em.addListener(EP.GestureDragEndEvent, this.onGestureDragEndEvent);

				em.addListener(EP.GesturePinchBeginEvent, this.onGesturePinchBeginEvent);
				em.addListener(EP.GesturePinchEvent, this.onGesturePinchEvent);
				em.addListener(EP.GesturePinchEndEvent, this.onGesturePinchEndEvent);

				em.addListener(EP.GestureDoubleTapEvent, this.onGestureDoubleTapEvent);
				em.addListener(EP.GestureTwoFingersTapEvent, this.onGestureTwoFingersTapEvent);

				em.addListener(EP.GestureHoldBeginEvent, this.onGestureHoldBeginEvent);
				em.addListener(EP.GestureHoldEndEvent, this.onGestureHoldEndEvent);

				em.addListener(EP.TouchMoveEvent, this.onTouchMoveEvent);
				em.addListener(EP.TouchReleaseEvent, this.onTouchReleaseEvent);
			}
		};

		InputManager.prototype.onDeactivate = function () {
			var em = EP.EventServices;
			if (em !== undefined && em !== null) {				
				em.removeListener(EP.MousePressEvent, this.onMouseDown);
				em.removeListener(EP.MouseReleaseEvent, this.onMouseUp);
				em.removeListener(EP.MouseMoveEvent, this.onMouseMove);
				em.removeListener(EP.MouseWheelEvent, this.onMouseWheel);

				em.removeListener(EP.KeyboardPressEvent, this.onKeyDown);
				em.removeListener(EP.KeyboardReleaseEvent, this.onKeyUp);

				//em.removeListener(EP.GamepadEvent, this.onGamepad);
				em.removeListener(EP.GamepadPressEvent, this.onGamepadPressEvent);
				em.removeListener(EP.GamepadReleaseEvent, this.onGamepadReleaseEvent);
				em.removeListener(EP.GamepadAxisEvent, this.onGamepadAxisEvent);

				// LBN9 - Remove touch listener
				em.removeListener(EP.GestureDragBeginEvent, this.onGestureDragBeginEvent);
				em.removeListener(EP.GestureDragEndEvent, this.onGestureDragEndEvent);
				em.removeListener(EP.GestureDragEvent, this.onGestureDragEvent);

				em.removeListener(EP.GesturePinchBeginEvent, this.onGesturePinchBeginEvent);
				em.removeListener(EP.GesturePinchEvent, this.onGesturePinchEvent);
				em.removeListener(EP.GesturePinchEndEvent, this.onGesturePinchEndEvent);

				em.removeListener(EP.GestureDoubleTapEvent, this.onGestureDoubleTapEvent);
				em.removeListener(EP.GestureTwoFingersTapEvent, this.onGestureTwoFingersTapEvent);

				em.removeListener(EP.GestureHoldBeginEvent, this.onGestureHoldBeginEvent);
				em.removeListener(EP.GestureHoldEndEvent, this.onGestureHoldEndEvent);

				em.removeListener(EP.TouchMoveEvent, this.onTouchMoveEvent);
				em.removeListener(EP.TouchReleaseEvent, this.onTouchReleaseEvent);
			}

			Instance.prototype.onDeactivate.call(this);
		};

		InputManager.prototype.replacer = function () {
			var mod = Instance.prototype.replacer.apply(this);

			for (var propName in mod) {
				// removing function (only useful at runtime)
				// TODO: I don't know what it is not done directly by the default replacer      
				if (mod.hasOwnProperty(propName) && typeof mod[propName] === 'function') {
					delete mod[propName];
				}
			}
			return mod;
		};

		// Expose in STU namespace.
		STU.InputManager = InputManager;

		return InputManager;
	});

define('StuCameras/StuInputManager', ['DS/StuCameras/StuInputManager'], function (InputManager) {
	'use strict';

	return InputManager;
});

