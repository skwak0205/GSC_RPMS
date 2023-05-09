define('DS/EPPrivateTools/EventListenerExtensions', [], function () {
    'use strict';

    function overwrite() {
        if (! this.overwritten) {
            this.overwritten = true;

            EventTarget.prototype._addEventListener = EventTarget.prototype.addEventListener;
            EventTarget.prototype.addEventListener = function (a, b, c) {
                if (c === undefined) c = false;
                this._addEventListener(a, b, c);
                if (! this.eventListenerList) this.eventListenerList = {};
                if (! this.eventListenerList[a]) this.eventListenerList[a] = [];
                this.eventListenerList[a].push({ listener: b, options: c });
            };

            EventTarget.prototype._removeEventListener = EventTarget.prototype.removeEventListener;
            EventTarget.prototype.removeEventListener = function (a, b, c) {
                if (c === undefined) c = false;
                this._removeEventListener(a, b, c);
                if (!this.eventListenerList) this.eventListenerList = {};
                if (!this.eventListenerList[a]) this.eventListenerList[a] = [];

                for (let i = 0; i < this.eventListenerList[a].length; i++) {
                    if (this.eventListenerList[a][i].listener === b, this.eventListenerList[a][i].options === c) {
                        this.eventListenerList[a].splice(i, 1);
                        break;
                    }
                }
                if (this.eventListenerList[a].length === 0) delete this.eventListenerList[a];
            };

            EventTarget.prototype._getEventListeners = function (a) {
                if (!this.eventListenerList) this.eventListenerList = {};
                if (a === undefined) { return this.eventListenerList; }
                return this.eventListenerList[a];
            };

            EventTarget.prototype._listAllEventListeners = function () {
                const allElements = Array.prototype.slice.call(document.querySelectorAll('*'));
                allElements.push(document);
                allElements.push(window);

                const types = [];

                for (let ev in window) {
                    if (/^on/.test(ev)) types[types.length] = ev;
                }

                let elements = [];
                for (let i = 0; i < allElements.length; i++) {
                    const currentElement = allElements[i];

                    // Events defined in attributes
                    for (let j = 0; j < types.length; j++) {

                        if (typeof currentElement[types[j]] === 'function') {
                            elements.push({
                                "node": currentElement,
                                "type": types[j],
                                "func": currentElement[types[j]].toString()
                            });
                        }
                    }

                    // Events defined with addEventListener
                    if (typeof currentElement._getEventListeners === 'function') {
                        var evts = currentElement._getEventListeners();
                        if (Object.keys(evts).length > 0) {
                            for (let evt of Object.keys(evts)) {
                                for (var k = 0; k < evts[evt].length; k++) {
                                    elements.push({
                                        "node": currentElement,
                                        "type": evt,
                                        "func": evts[evt][k].listener ? evts[evt][k].listener.toString() : 'null'
                                    });
                                }
                            }
                        }
                    }
                }

                return elements.sort();
            };

            EventTarget.prototype._removeEventListeners = function() {
                var keys = Object.keys(this.eventListenerList);
                for (var i = 0; i < keys.length; i++) {
                    var event = keys[i];
                    var eventArr = this.eventListenerList[event];

                    while (eventArr.length !== 0) {
                        this.removeEventListener(event, eventArr[0].listener, eventArr[0].options);
                    }
                }
            };

        }
    }

    return {
        'overwrite': overwrite
    };

});
