define('DS/EPInputs/EPGamepadPressEvent', ['DS/EP/EP', 'DS/EPEventServices/EPEventServices', 'DS/EPInputs/EPGamepadEvent'], function (EP, EventServices, GamepadEvent) {
	'use strict';

	/**
	 * @class
	 * @classdesc
	 * <p>Describe an event generated from a gamepad.</br>
	 * It contains information about the gamepad device.</br>
	 * It occurs when the user presses a gamepad button.</p>
	 *
	 * <p>This event is dispatched globally on the EP.EventServices.</br>
	 * In order to get notified, you need to add a listener as EP.GamepadPressEvent type on the EP.EventServices.</p>
	 * @alias EP.GamepadPressEvent
	 * @noinstancector
	 * @public
	 * @extends EP.GamepadEvent
     * @example
     * var objectListener = {};
	 * objectListener.onGamepadPressEvent = function (iGamepadPressEvent) {
	 *	// user pressed a gamepad button
	 * };
	 * // Add Listener to get notified
	 * EP.EventServices.addObjectListener(EP.GamepadPressEvent, objectListener, 'onGamepadPressEvent');
	 * // Remove Listener when you don't need it anymore
	 * EP.EventServices.removeObjectListener(EP.GamepadPressEvent, objectListener, 'onGamepadPressEvent');
	 */
	var GamepadPressEvent = function () {

		GamepadEvent.call(this);

		/**
         * Gamepad Button ID.
         *
		 * @private
         * @type {EP.Gamepad.EButton}
         */
		this.button = undefined;
	};

	GamepadPressEvent.prototype = Object.create(GamepadEvent.prototype);
	GamepadPressEvent.prototype.constructor = GamepadPressEvent;
	GamepadPressEvent.prototype.type = 'GamepadPressEvent';

	/**
	 * Return the Gamepad Button ID.
	 *
	 * @public
	 * @return {EP.Gamepad.EButton}
	 * @example
	 * var objectListener = {};
	 * objectListener.onGamepadPressEvent = function (iGamepadPressEvent) {
	 *	if(iGamepadPressEvent.getButton() === EP.Gamepad.EButton.e1) {
	 *		// button 1 has been pressed
	 *	}
	 * };
	 */
	GamepadPressEvent.prototype.getButton = function () {
		return this.button;
	};

	EventServices.registerEvent(GamepadPressEvent);

	// Expose in EP namespace.
	EP.GamepadPressEvent = GamepadPressEvent;

	return GamepadPressEvent;
});
