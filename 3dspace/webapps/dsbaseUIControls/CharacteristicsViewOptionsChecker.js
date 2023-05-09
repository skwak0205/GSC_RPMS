define('DS/dsbaseUIControls/CharacteristicsViewOptionsChecker', [
    'i18n!DS/dsbaseUIControls/assets/nls/CharacteristicsView',
    'DS/UWPClientCode/I18n'
], function (
    NLS,
    I18n
) {

    'use strict';

    /**
     * @summary Characteristics view options checker constructor
     * @class DS/dsbaseUIControls/CharacteristicsViewOptionsChecker
     * 
     * @param {Object} characteristicsViewOptions - Characteristics view options to check
     */
    function CharacteristicsViewOptionsCheckerTool(characteristicsViewOptions) {
        this.characteristicsViewOptions = characteristicsViewOptions;
    }


    /**
     * @summary Creation of the characteristics view checker tool
     */
    CharacteristicsViewOptionsCheckerTool.prototype = (function () {

        let getSupportedLanguages = function () {

            let languages = Object.keys(I18n.getSupportedLanguages());

            if (!languages.includes('en')) {
                languages.push('en');
            }

            return languages;

        };


        /**
         * Default customization values
         */
        let defaultCustomizationValues = {
            editMode: true,
            languages: {
                supported: getSupportedLanguages(),
                selected: 'en'
            },
            getInfos: undefined,
            display: {
                switchMode: true,
                switchLanguage: true,
                characteristics: {
                    constraints: true,
                    readOnly: true
                },
                scroller: true
            },
            constraintsPosition: 'top',
            filters: {
                whitelist: [],
                blacklist: [],
                orderlist: [
                    'http://www.3ds.com/RDF/Corpus/Kernel/nlsLabel',
                    'http://www.3ds.com/RDF/Corpus/Kernel/nlsComment',
                    'http://www.3ds.com/RDF/Corpus/Kernel/owner'
                ]
            },
            search: {},
            forceEdit: [],
            onChangeSensitive: true
        };


        /**
         * Default events functions
         */
        let defaultEvents = {
            onSwitchMode: function (mode) { },
            onValueChange: function (inputName, value) { },
            onFinishRender: function () { },
            onSelectLanguage: function (language) { },
            onSaveLanguage: function (original, targeted) {
                return new Promise(function (resolve, reject) { resolve(); });
            },
            onDiscardLanguage: function (original, targeted) {
                return new Promise(function (resolve, reject) { resolve(); });
            },
            onCancelLanguage: function (original, targeted) {
                return new Promise(function (resolve, reject) { resolve(); });
            },
            onChangeLanguageWithNoModification: function (original, targeted) {
                return new Promise(function (resolve, reject) { resolve(); });
            }
        };


        /*---- 3.1 ----*/
        let _checkCustomizationModeOptions = function () {

            if (this.characteristicsViewOptions.customization.editMode === undefined) {
                this.characteristicsViewOptions.customization.editMode = defaultCustomizationValues.editMode;
            }

        };


        /*---- 3.2 ----*/
        let _checkCustomizationLanguagesOptions = function() {

            if (this.characteristicsViewOptions.customization.languages === undefined) {
                this.characteristicsViewOptions.customization.languages = defaultCustomizationValues.languages;
            }

            /***** SUPPORTED *****/
            if (this.characteristicsViewOptions.customization.languages.supported === undefined) {
                this.characteristicsViewOptions.customization.languages.supported = defaultCustomizationValues.languages.supported;
            }

            if (!Array.isArray(this.characteristicsViewOptions.customization.languages.supported)) {
                throw new Error(NLS.get('CV.optionsChecker.languages.supported.error'));
            }


            /***** SELECTED *****/
            if (this.characteristicsViewOptions.customization.languages.selected === undefined) {
                this.characteristicsViewOptions.customization.languages.selected = this.characteristicsViewOptions.customization.languages.supported[0];
            }

            if (typeof this.characteristicsViewOptions.customization.languages.selected !== 'string') {
                throw new Error(NLS.get('CV.optionsChecker.languages.selected.error'));
            } 

            if (!this.characteristicsViewOptions.customization.languages.supported.includes(this.characteristicsViewOptions.customization.languages.selected)) {
                this.characteristicsViewOptions.customization.languages.supported.push(this.characteristicsViewOptions.customization.languages.selected);
            }

        };


        /*---- 3.2 ----*/
        let _checkCustomizationGetInfosOptions = function () {

            if (this.characteristicsViewOptions.customization.getInfos === undefined) {
                this.characteristicsViewOptions.customization.getInfos = defaultCustomizationValues.getInfos;
            }

        };


        /*---- 3.3 ----*/
        let _checkCustomizationDisplayOptions = function () {

            if (!this.characteristicsViewOptions.customization.display) {
                this.characteristicsViewOptions.customization.display = defaultCustomizationValues.display;
            }

            if (this.characteristicsViewOptions.customization.display.switchLanguage === undefined) {
                this.characteristicsViewOptions.customization.display.switchLanguage = defaultCustomizationValues.display.switchLanguage;
            }

            if (this.characteristicsViewOptions.customization.display.switchMode === undefined) {
                this.characteristicsViewOptions.customization.display.switchMode = defaultCustomizationValues.display.switchMode;
            }

            if (!this.characteristicsViewOptions.customization.display.characteristics) {
                this.characteristicsViewOptions.customization.display.characteristics = defaultCustomizationValues.display.characteristics;
            }

            if (this.characteristicsViewOptions.customization.display.characteristics.constraints === undefined) {
                this.characteristicsViewOptions.customization.display.characteristics.constraints = defaultCustomizationValues.display.characteristics.constraints;
            }

            if (this.characteristicsViewOptions.customization.display.characteristics.readOnly === undefined) {
                this.characteristicsViewOptions.customization.display.characteristics.readOnly = defaultCustomizationValues.display.characteristics.readOnly;
            }

            if (this.characteristicsViewOptions.customization.display.scroller === undefined) {
                this.characteristicsViewOptions.customization.display.scroller = defaultCustomizationValues.display.scroller;
            }

        };


        /*---- 3.5 ----*/
        let _checkCustomizationConstraintsPositionOptions = function () {

            if (!this.characteristicsViewOptions.customization.constraintsPosition) {
                this.characteristicsViewOptions.customization.constraintsPosition = defaultCustomizationValues.constraintsPosition;
            } else if (!['top', 'right', 'bottom', 'left'].includes(this.characteristicsViewOptions.customization.constraintsPosition)) {
                throw new Error(NLS.get('CV.optionsChecker.constraintsPosition.error'));
            }

        };


        /*---- 3.6 ----*/
        let _checkCustomizationSearchOptions = function () {

            if (!this.characteristicsViewOptions.customization.search) {
                this.characteristicsViewOptions.customization.search = defaultCustomizationValues.search;
            } else {
                let that = this;
                Object.keys(this.characteristicsViewOptions.customization.search).forEach(function (key) {
                    if (typeof that.characteristicsViewOptions.customization.search[key].autocomplete !== 'function') {
                        throw new Error(NLS.get('CV.optionsChecker.search.error', {
                            key: key
                        }));
                    }
                });
            }

        };


        /*---- 3.7 ----*/
        let _checkCustomizationFiltersOptions = function () {

            if (!this.characteristicsViewOptions.customization.filters) {
                this.characteristicsViewOptions.customization.filters = defaultCustomizationValues.filters;
            }

            if (!this.characteristicsViewOptions.customization.filters.whitelist) {
                this.characteristicsViewOptions.customization.filters.whitelist = defaultCustomizationValues.filters.whitelist;
            }

            if (!this.characteristicsViewOptions.customization.filters.blacklist) {
                this.characteristicsViewOptions.customization.filters.blacklist = defaultCustomizationValues.filters.blacklist;
            }

            if (!this.characteristicsViewOptions.customization.filters.orderlist) {
                this.characteristicsViewOptions.customization.filters.orderlist = defaultCustomizationValues.filters.orderlist;
            }

        };


        /*---- 3.8 ----*/
        let _checkCustomizationForceEditOptions = function () {

            if (this.characteristicsViewOptions.customization.forceEdit === undefined) {
                this.characteristicsViewOptions.customization.forceEdit = defaultCustomizationValues.forceEdit;
            }

        };


        /*---- 3.9 ----*/
        let _checkCustomizationOnChangeSensitiveOptions = function () {

            if (this.characteristicsViewOptions.customization.onChangeSensitive === undefined) {
                this.characteristicsViewOptions.customization.onChangeSensitive = defaultCustomizationValues.onChangeSensitive;
            }

        };


        /*---- 4.1 ----*/
        let _checkEventsOnValueChangeOptions = function () {

            if (!this.characteristicsViewOptions.events.onValueChange) {
                this.characteristicsViewOptions.events.onValueChange = defaultEvents.onValueChange;
            }

        };


        /*---- 4.2 ----*/
        let _checkEventsOnFinishRenderOptions = function() {

            if(!this.characteristicsViewOptions.events.onFinishRender) {
                this.characteristicsViewOptions.events.onFinishRender = defaultEvents.onFinishRender;
            }

        };


        /*---- 4.3 ----*/
        let _checkEventsLanguagesOptions = function() {

            let that = this;
            let isPromise = function (variable) {
                return (variable !== undefined && variable !== null && typeof variable.then === 'function');
            };

            let getPromiseFromCallback = function(callback) {
                return function() {
                    return new Promise(function (resolve, reject) {
                        let result = callback();
                        if (isPromise(result)) {
                            result.then(function () {
                                resolve();
                            });
                        } else {
                            resolve();
                        }
                    });
                }
            };

            if(!this.characteristicsViewOptions.events.onSaveLanguage) {
                this.characteristicsViewOptions.events.onSaveLanguage = defaultEvents.onSaveLanguage;
            } else {
               // this.characteristicsViewOptions.events.onSaveLanguage = getPromiseFromCallback(this.characteristicsViewOptions.events.onSaveLanguage);
            }

            if (!this.characteristicsViewOptions.events.onDiscardLanguage) {
                this.characteristicsViewOptions.events.onDiscardLanguage = defaultEvents.onDiscardLanguage;
            } else {
               // this.characteristicsViewOptions.events.onDiscardLanguage = getPromiseFromCallback(this.characteristicsViewOptions.events.onDiscardLanguage);
            }

            if (!this.characteristicsViewOptions.events.onCancelLanguage) {
                this.characteristicsViewOptions.events.onCancelLanguage = defaultEvents.onCancelLanguage;
            } else {
                // this.characteristicsViewOptions.events.onCancelLanguage = getPromiseFromCallback(this.characteristicsViewOptions.events.onCancelLanguage);
            }

            if (!this.characteristicsViewOptions.events.onChangeLanguageWithNoModification) {
                this.characteristicsViewOptions.events.onChangeLanguageWithNoModification = defaultEvents.onChangeLanguageWithNoModification;
            }

        };


        /*---- 4.4 ----*/
        let _checkEventsOnSwitchModeEvents = function () {

            if (!this.characteristicsViewOptions.events.onSwitchMode) {
                this.characteristicsViewOptions.events.onSwitchMode = defaultEvents.onSwitchMode;
            }

        };


        return {

            /*********************************************** 1 ***********************************************/
            /**
             * @summary Check all options passed to the characteristics view
             */
            checkAllOptions: function () {
                this.checkMandatoryOptions();       // 2
                this.checkCustomizationOptions();   // 3
                this.checkEventsOptions();          // 4
            },


            /*********************************************** 2 ***********************************************/
            /**
             * @summary Check mandatory options
             */
            checkMandatoryOptions: function () {
                if (!this.characteristicsViewOptions.rdfServerURL) throw new Error(NLS.get('CV.optionsChecker.rdfServerURL.notExisting.error'));
                if (!this.characteristicsViewOptions.resourceURI) throw new Error(NLS.get('CV.optionsChecker.resourceURI.notExisting.error'));
                if (typeof this.characteristicsViewOptions.rdfServerURL !== 'string') throw new Error(NLS.get('CV.optionsChecker.rdfServerURL.string.error'));
                if (typeof this.characteristicsViewOptions.resourceURI !== 'string') throw new Error(NLS.get('CV.optionsChecker.resourceURI.string.error'));
            },


            /*********************************************** 3 ***********************************************/
            /**
             * @summary Check customization/optional options
             */
            checkCustomizationOptions: function () {

                if (!this.characteristicsViewOptions.customization) {
                    this.characteristicsViewOptions.customization = defaultCustomizationValues;
                }

                _checkCustomizationModeOptions.bind(this)();                // 3.1
                _checkCustomizationLanguagesOptions.bind(this)();           // 3.2
                _checkCustomizationGetInfosOptions.bind(this)();            // 3.3
                _checkCustomizationDisplayOptions.bind(this)();             // 3.4
                _checkCustomizationConstraintsPositionOptions.bind(this)(); // 3.6
                _checkCustomizationSearchOptions.bind(this)();              // 3.7
                _checkCustomizationFiltersOptions.bind(this)();             // 3.8
                _checkCustomizationForceEditOptions.bind(this)();           // 3.9
                _checkCustomizationOnChangeSensitiveOptions.bind(this)();   // 3.10

            },


            /*********************************************** 4 ***********************************************/
            /**
             * @summary Check events options
             */
            checkEventsOptions: function () {

                if (!this.characteristicsViewOptions.events) {
                    this.characteristicsViewOptions.events = defaultEvents;
                }

                _checkEventsOnValueChangeOptions.bind(this)();              // 4.1
                _checkEventsOnFinishRenderOptions.bind(this)();             // 4.2
                _checkEventsLanguagesOptions.bind(this)();                  // 4.3
                _checkEventsOnSwitchModeEvents.bind(this)();                // 4.4

            }

        };

    })();

    return CharacteristicsViewOptionsCheckerTool.prototype.constructor = CharacteristicsViewOptionsCheckerTool;

});
