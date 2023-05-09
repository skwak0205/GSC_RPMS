define('DS/EPInputs/EPMouseReleaseEvent', [
	'DS/EP/EP',
	'DS/EPEventServices/EPEventServices',
	'DS/EPInputs/EPMouseEvent',
	'DS/MathematicsES/MathVector2DJSImpl'
], function (EP, EventServices, MouseEvent, Vector2D) {
	'use strict';

	/**
	 * @class
	 * @classdesc
	 * <p>Describe an event generated from a mouse.</br>
	 * It contains information about the mouse device.</br>
	 * It occurs when the user releases a mouse button.</p>
	 *
	 * <p>This event is dispatched globally on the EP.EventServices.</br>
	 * In order to get notified, you need to add a listener as EP.MouseReleaseEvent type on the EP.EventServices.</p>
	 * @alias EP.MouseReleaseEvent
	 * @noinstancector
	 * @public
	 * @extends EP.MouseEvent
	 * @example
	 * var objectListener = {};
	 * objectListener.onMouseReleaseEvent = function (iMouseReleaseEvent) {
	 *	// user released a mouse button
	 * };
	 * // Add Listener to get notified
	 * EP.EventServices.addObjectListener(EP.MouseReleaseEvent, objectListener, 'onMouseReleaseEvent');
	 * // Remove Listener when you don't need it anymore
	 * EP.EventServices.removeObjectListener(EP.MouseReleaseEvent, objectListener, 'onMouseReleaseEvent');
	 */
	var MouseReleaseEvent = function () {

		MouseEvent.call(this);

		/**
         * Mouse Button ID.
         *
		 * @private
         * @type {EP.Mouse.EButton}
         */
		this.button = undefined;

		/**
         * The position in pixel of the cursor on the viewer.
         *
		 * @private
         * @type {DSMath.Vector2D}
         */
	    this.position = new Vector2D();

	};

	MouseReleaseEvent.prototype = Object.create(MouseEvent.prototype);
	MouseReleaseEvent.prototype.constructor = MouseReleaseEvent;
	MouseReleaseEvent.prototype.type = 'MouseReleaseEvent';

	/**
	 * Return the Mouse Button ID.
	 *
	 * @public
	 * @return {EP.Mouse.EButton}
	 * @example
	 * var objectListener = {};
	 * objectListener.onMouseReleaseEvent = function (iMouseReleaseEvent) {
	 *	if(iMouseReleaseEvent.getButton() === EP.Mouse.EButton.eLeft) {
	 *		// left button has been released
	 *	}
	 * };
	 */
	MouseReleaseEvent.prototype.getButton = function () {
		return this.button;
	};

	/**
	 * Return the position in pixel of the cursor on the viewer.
	 *
	 * @public
	 * @return {DSMath.Vector2D}
	 * @example
	 * var objectListener = {};
	 * objectListener.onMouseReleaseEvent = function (iMouseReleaseEvent) {
	 *	var mousePosition = iMouseReleaseEvent.getPosition();
	 *	var mouseWidth = mousePosition.x;
	 *	var mouseHeight = mousePosition.y;
	 * };
	 */
	MouseReleaseEvent.prototype.getPosition = function () {
		return this.position.clone();
	};

	EventServices.registerEvent(MouseReleaseEvent);

	// Expose in EP namespace.
	EP.MouseReleaseEvent = MouseReleaseEvent;

	return MouseReleaseEvent;
});
