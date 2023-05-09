define('DS/dsbaseUIControls/UserCharacteristicsViewOptionsChecker', [
    'DS/dsbaseUIControls/CharacteristicsViewOptionsChecker'
], function (CharacteristicsViewOptionsChecker) {

    'use strict';

    /**
     * @summary User characteristics view options checker constructor
     * @class DS/dsbaseUIControls/UserCharacteristicsViewOptionsChecker
     *
     * @param {Object} userCharacteristicsViewOptions - User characteristics view options to check
     */
    function UserCharacteristicsViewOptionsCheckerTool(userCharacteristicsViewOptions) {
        this.characteristicsViewOptions = userCharacteristicsViewOptions;
    }

    // Extend 'DS/dsbaseUIControls/CharacteristicsViewOptionsChecker'
    UserCharacteristicsViewOptionsCheckerTool.prototype = Object.create(CharacteristicsViewOptionsChecker.prototype);


    let defaultEvents = {
        events: {
            onAddUserCharacteristic: function (name) { },
            onRemoveUserCharacteristic: function (name) { },
            onModifyUserCharacteristic: function (name) { }
        }
    };


    /*---- 4.2 ----*/
    let _checkEventsOnAddUserCharacteristicOptions = function () {

        if (!this.characteristicsViewOptions.events.onAddUserCharacteristic) {
            this.characteristicsViewOptions.events.onAddUserCharacteristic = defaultEvents.events.onAddUserCharacteristic;
        }

    };


    /*---- 4.3 ----*/
    let _checkEventsOnRemoveUserCharacteristicOptions = function () {

        if (!this.characteristicsViewOptions.events.onRemoveUserCharacteristic) {
            this.characteristicsViewOptions.events.onRemoveUserCharacteristic = defaultEvents.events.onRemoveUserCharacteristic;
        }

    };


    /*---- 4.3 ----*/
    let _checkEventsOnModifyUserCharacteristicOptions = function () {

        if (!this.characteristicsViewOptions.events.onModifyUserCharacteristic) {
            this.characteristicsViewOptions.events.onModifyUserCharacteristic = defaultEvents.events.onModifyUserCharacteristic;
        }

    };


    /*********************************************** 2 ***********************************************/
    /**
     * @summary Check mandatory options
     * @override
     */
    UserCharacteristicsViewOptionsCheckerTool.prototype.checkMandatoryOptions = function () {

        CharacteristicsViewOptionsChecker.prototype.checkMandatoryOptions.call(this);

        if (!this.characteristicsViewOptions.fromOptionSet) {
            throw new Error(NLS.get('UCV.optionsChecker.optionSet.notExisting.error'));
        }

        if (typeof this.characteristicsViewOptions.fromOptionSet !== 'string') {
            throw new Error(NLS.get('UCV.optionsChecker.optionSet.type.error'));
        }
        
    };


    /*********************************************** 4 ***********************************************/
    UserCharacteristicsViewOptionsCheckerTool.prototype.checkEventsOptions = function () {
        CharacteristicsViewOptionsChecker.prototype.checkEventsOptions.call(this);
        _checkEventsOnAddUserCharacteristicOptions.bind(this)();        // 4.2
        _checkEventsOnRemoveUserCharacteristicOptions.bind(this)();     // 4.3
        _checkEventsOnModifyUserCharacteristicOptions.bind(this)();     // 4.4

    };


    return UserCharacteristicsViewOptionsCheckerTool.prototype.constructor = UserCharacteristicsViewOptionsCheckerTool;

});
