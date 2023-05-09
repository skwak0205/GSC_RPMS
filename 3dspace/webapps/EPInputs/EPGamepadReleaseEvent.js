define('DS/EPInputs/EPGamepadReleaseEvent', ['DS/EP/EP', 'DS/EPEventServices/EPEventServices', 'DS/EPInputs/EPGamepadEvent'], function (EP, EventServices, GamepadEvent) {
	'use strict';

	/**
	 * @class
	 * @classdesc
	 * <p>Describe an event generated from a gamepad.</br>
	 * It contains information about the gamepad device.</br>
	 * It occurs when the user releases a gamepad button.</p>
	 *
	 * <p>This event is dispatched globally on the EP.EventServices.</br>
	 * In order to get notified, you need to add a listener as EP.GamepadReleaseEvent type on the EP.EventServices.</p>
	 * @alias EP.GamepadReleaseEvent
	 * @noinstancector
	 * @public
	 * @extends EP.GamepadEvent
     * @example
	 * var objectListener = {};
	 * objectListener.onGamepadReleaseEvent = function (iGamepadReleaseEvent) {
	 *	// user released a gamepad button
	 * };
	 * // Add Listener to get notified
	 * EP.EventServices.addObjectListener(EP.GamepadReleaseEvent, objectListener, 'onGamepadReleaseEvent');
	 * // Remove Listener when you don't need it anymore
	 * EP.EventServices.removeObjectListener(EP.GamepadReleaseEvent, objectListener, 'onGamepadReleaseEvent');
	 */
	var GamepadReleaseEvent = function () {

		GamepadEvent.call(this);

		/**
         * Gamepad Button ID.
         *
		 * @private
         * @type {EP.Gamepad.EButton}
         */
		this.button = undefined;
	};

	GamepadReleaseEvent.prototype = Object.create(GamepadEvent.prototype);
	GamepadReleaseEvent.prototype.constructor = GamepadReleaseEvent;
	GamepadReleaseEvent.prototype.type = 'GamepadReleaseEvent';

	/**
	 * Return the Gamepad Button ID.
	 *
	 * @public
	 * @return {EP.Gamepad.EButton}
	 * @example
	 * var objectListener = {};
	 * objectListener.onGamepadReleaseEvent = function (iGamepadReleaseEvent) {
	 *	if(iGamepadReleaseEvent.getButton() === EP.Gamepad.EButton.e1) {
	 *		// button 1 has been released
	 *	}
	 * };
	 */
	GamepadReleaseEvent.prototype.getButton = function () {
		return this.button;
	};

	EventServices.registerEvent(GamepadReleaseEvent);

	// Expose in EP namespace.
	EP.GamepadReleaseEvent = GamepadReleaseEvent;

	return GamepadReleaseEvent;
});
