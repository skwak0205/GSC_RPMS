"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = exports.ClickOutside = void 0;

function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function defaultConditional() {
  return true;
}

function directive(e, el, binding) {
  var handler = typeof binding.value === 'function' ? binding.value : binding.value.handler;
  var isActive = _typeof(binding.value) === 'object' && binding.value.closeConditional || defaultConditional; // The include element callbacks below can be expensive
  // so we should avoid calling them when we're not active.
  // Explicitly check for false to allow fallback compatibility
  // with non-toggleable components

  if (!e || isActive(e) === false) return; // Check if additional elements were passed to be included in check
  // (click must be outside all included elements, if any)

  var elements = (_typeof(binding.value) === 'object' && binding.value.include || function () {
    return [];
  })(); // Add the root element for the component this directive was defined on


  elements.push(el); // Check if it's a click outside our elements, and then if our callback returns true.
  // Non-toggleable components should take action in their callback and return falsy.
  // Toggleable can return true if it wants to deactivate.
  // Note that, because we're in the capture phase, this callback will occur before
  // the bubbling click event on any outside elements.

  !elements.some(function (el) {
    return el.contains(e.target);
  }) && setTimeout(function () {
    isActive(e) && handler && handler(e);
  }, 0);
}

var ClickOutside = {
  // [data-app] may not be found
  // if using bind, inserted makes
  // sure that the root element is
  // available, iOS does not support
  // clicks on body
  inserted: function inserted(el, binding) {
    var onClick = function onClick(e) {
      return directive(e, el, binding);
    }; // iOS does not recognize click events on document
    // or body, this is the entire purpose of the v-app
    // component and [data-app], stop removing this


    var app = document.querySelector('[data-app]') || document.body; // This is only for unit tests

    app.addEventListener('click', onClick, true);
    el._clickOutside = onClick;
  },
  unbind: function unbind(el) {
    if (!el._clickOutside) return;
    var app = document.querySelector('[data-app]') || document.body; // This is only for unit tests

    app && app.removeEventListener('click', el._clickOutside, true);
    delete el._clickOutside;
  }
};
exports.ClickOutside = ClickOutside;
var _default = ClickOutside;
exports.default = _default;
//# sourceMappingURL=index.js.map
