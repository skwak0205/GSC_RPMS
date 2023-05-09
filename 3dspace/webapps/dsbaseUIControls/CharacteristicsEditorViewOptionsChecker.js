define('DS/dsbaseUIControls/CharacteristicsEditorViewOptionsChecker', [
    'DS/dsbaseUIServices/CharacteristicServices',
    'i18n!DS/dsbaseUIControls/assets/nls/CharacteristicsEditorView'
], function (
    CharacteristicServices,
    NLS
) {

    /**
    * @summary Characteristics editor view options checker constructor
    * @class DS/dsbaseUIControls/CharacteristicsEditorViewOptionsChecker
	*
	* @param {Object} characteristicsEditorViewOptions - Characteristics editor view options to check
	*/
    function CharacteristicsEditorViewOptionsCheckerTool(characteristicsEditorViewOptions) {
        this.characteristicsEditorViewOptions = characteristicsEditorViewOptions;
    };


    /**
     * @summary Creation of the characteristics editor view checker tool
     */
    CharacteristicsEditorViewOptionsCheckerTool.prototype = (function () {

        /**
         * Default customization values
         */
        let defaultCustomizationValues = {
            title: NLS.get('CEV.title'),
            editMode: true,
            display: {
                title: true,
                subtitle: true,
                switchMode: true,
                characteristics: {
                    constraints: true,
                    readOnly: true,
                    user: false
                },
                scroller: true,
                buttons: true
            },
            constraintsPosition: 'top',
            buttons: {
                cancel: {
                    label: NLS.get('CEV.cancelButton.content'),
                    type: 'default',
                    defaultAction: function (rootURL, resourceURI, formValues) {
                        console.log(formValues);
                    }
                },
                ok: {
                    label: NLS.get('CEV.okButton.content'),
                    type: 'primary',
                    defaultAction: function (rootURL, resourceURI, formValues, language) {

                        if (Object.keys(formValues).length > 0) {

                            // Prepare data to send through POST request
                            // Create names & values arrays for characteristics
                            let charNames = [];
                            let charValues = [];

                            Object.keys(formValues).forEach(function (key) {
                                if (!formValues[key].readOnly) {
                                    charNames.push(key);
                                    charValues.push(formValues[key].value);
                                }
                            });

                            // Deal with special case, when one characteristic value is array
                            if ((charValues.length === 1) && (Array.isArray(charValues[0]))) {
                                charValues = charValues[0];
                            }

                            let rscURI = [resourceURI];
                            let hasUnit = false;

                            let characteristicServices = CharacteristicServices.getHandle(rootURL, language);
                            return characteristicServices.setValue({
                                rscURIs: rscURI,
                                charNames: charNames,
                                charValues: charValues,
                                hasUnit: hasUnit,
                                cbObject: {
                                    onComplete: function (data, status) { },
                                    onFailure: function (data, status, error) { }
                                }
                            });

                        }

                    },
                    specificActions: {}
                },
                position: 'right'
            },
            search: {},
            filters: {
                whitelist: [],
                blacklist: [],
                orderlist: [
                    'http://www.3ds.com/RDF/Corpus/Kernel/nlsLabel',
                    'http://www.3ds.com/RDF/Corpus/Kernel/nlsComment',
                    'http://www.3ds.com/RDF/Corpus/Kernel/owner'
                ]
            },
            onChangeSensitive: true,
            events: {
                onFinishRender: function () { }
            }
        };


        /**
         * Default events functions
         */
        let defaultEvents = {
            onFinishRender: function () { },
            onSelectLanguage: function (language) { } 
        };


        /*---- 3.1 ----*/
        let _checkCustomizationTitlesOptions = function () {
            if (!this.characteristicsEditorViewOptions.customization.title) this.characteristicsEditorViewOptions.customization.title = defaultCustomizationValues.title;
            if (!this.characteristicsEditorViewOptions.customization.subtitle) this.characteristicsEditorViewOptions.customization.subtitle = defaultCustomizationValues.subtitle;
        };


        /*---- 3.2 ----*/
        let _checkCustomizationModeOptions = function () {
            if (this.characteristicsEditorViewOptions.customization.editMode === undefined) {
                this.characteristicsEditorViewOptions.customization.editMode = defaultCustomizationValues.editMode;
            }
        };


        /*---- 3.3 ----*/
        let _checkCustomizationDisplayOptions = function () {

            if (!this.characteristicsEditorViewOptions.customization.display) {
                this.characteristicsEditorViewOptions.customization.display = defaultCustomizationValues.display;
            }

            if (this.characteristicsEditorViewOptions.customization.display.title === undefined) {
                this.characteristicsEditorViewOptions.customization.display.title = defaultCustomizationValues.display.title;
            }

            if (this.characteristicsEditorViewOptions.customization.display.subtitle === undefined) {
                this.characteristicsEditorViewOptions.customization.display.subtitle = defaultCustomizationValues.display.subtitle;
            }

            if (this.characteristicsEditorViewOptions.customization.display.switchMode === undefined) {
                this.characteristicsEditorViewOptions.customization.display.switchMode = defaultCustomizationValues.display.switchMode;
            }

            if (!this.characteristicsEditorViewOptions.customization.display.characteristics) {
                this.characteristicsEditorViewOptions.customization.display.characteristics = defaultCustomizationValues.display.characteristics;
            }

            if (this.characteristicsEditorViewOptions.customization.display.characteristics.constraints === undefined) {
                this.characteristicsEditorViewOptions.customization.display.characteristics.constraints = defaultCustomizationValues.display.characteristics.constraints;
            }

            if (this.characteristicsEditorViewOptions.customization.display.characteristics.readOnly === undefined) {
                this.characteristicsEditorViewOptions.customization.display.characteristics.readOnly = defaultCustomizationValues.display.characteristics.readOnly;
            }

            if (this.characteristicsEditorViewOptions.customization.display.characteristics.user === undefined) {
                this.characteristicsEditorViewOptions.customization.display.characteristics.user = defaultCustomizationValues.display.characteristics.user;
            }

            if (this.characteristicsEditorViewOptions.customization.display.scroller === undefined) {
                this.characteristicsEditorViewOptions.customization.display.scroller = defaultCustomizationValues.display.scroller;
            }

            if (this.characteristicsEditorViewOptions.customization.display.buttons === undefined) {
                this.characteristicsEditorViewOptions.customization.display.buttons = defaultCustomizationValues.display.buttons;
            }

        };


        /*---- 3.5 ----*/
        let _checkCustomizationConstraintsPositionOptions = function () {
            if (!this.characteristicsEditorViewOptions.customization.constraintsPosition) {
                this.characteristicsEditorViewOptions.customization.constraintsPosition = defaultCustomizationValues.constraintsPosition;
            } else if (!['top', 'right', 'bottom', 'left'].includes(this.characteristicsEditorViewOptions.customization.constraintsPosition)) {
                throw new Error(NLS.get('CEV.optionsChecker.constraintsPosition.error'));
            }
        };


        /*---- 3.6 ----*/
        let _checkCustomizationButtonsOptions = function () {

            if (!this.characteristicsEditorViewOptions.customization.buttons) {
                this.characteristicsEditorViewOptions.customization.buttons = defaultCustomizationValues.buttons;
            }

            // Cancel
            if (!this.characteristicsEditorViewOptions.customization.buttons.cancel) {
                this.characteristicsEditorViewOptions.customization.buttons.cancel = defaultCustomizationValues.buttons.cancel;
            }

            if (!this.characteristicsEditorViewOptions.customization.buttons.cancel.label) {
                this.characteristicsEditorViewOptions.customization.buttons.cancel.label = defaultCustomizationValues.buttons.cancel.label;
            }

            if (!this.characteristicsEditorViewOptions.customization.buttons.cancel.defaultAction) {
                this.characteristicsEditorViewOptions.customization.buttons.cancel.defaultAction = defaultCustomizationValues.buttons.cancel.defaultAction;
            }

            if (!this.characteristicsEditorViewOptions.customization.buttons.cancel.type) {
                this.characteristicsEditorViewOptions.customization.buttons.cancel.type = defaultCustomizationValues.buttons.cancel.type;
            } else if (!['primary', 'default', 'success', 'info', 'warning', 'error'].includes(this.characteristicsEditorViewOptions.customization.buttons.cancel.type)) {
                throw new Error(NLS.get('CEV.optionsChecker.buttons.types.error', {
                    label: this.characteristicsEditorViewOptions.customization.buttons.cancel.label
                }));
            }


            // Ok
            if (!this.characteristicsEditorViewOptions.customization.buttons.ok) {
                this.characteristicsEditorViewOptions.customization.buttons.ok = defaultCustomizationValues.buttons.ok;
            }

            if (!this.characteristicsEditorViewOptions.customization.buttons.ok.label) {
                this.characteristicsEditorViewOptions.customization.buttons.ok.label = defaultCustomizationValues.buttons.ok.label;
            }

            if (!this.characteristicsEditorViewOptions.customization.buttons.ok.defaultAction) {
                this.characteristicsEditorViewOptions.customization.buttons.ok.defaultAction = defaultCustomizationValues.buttons.ok.defaultAction;
            }

            if (!this.characteristicsEditorViewOptions.customization.buttons.ok.type) {
                this.characteristicsEditorViewOptions.customization.buttons.ok.type = defaultCustomizationValues.buttons.ok.type;
            } else if (!['primary', 'default', 'success', 'info', 'warning', 'error'].includes(this.characteristicsEditorViewOptions.customization.buttons.ok.type)) {
                throw new Error(NLS.get('CEV.optionsChecker.buttons.types.error', {
                    label: this.characteristicsEditorViewOptions.customization.buttons.ok.label
                }));
            }

            if (!this.characteristicsEditorViewOptions.customization.buttons.ok.specificActions) {
                this.characteristicsEditorViewOptions.customization.buttons.ok.specificActions = defaultCustomizationValues.buttons.ok.specificActions;
            } else {
                let that = this;
                Object.keys(this.characteristicsEditorViewOptions.customization.buttons.ok.specificActions).forEach(function (key) {
                    if (typeof that.characteristicsEditorViewOptions.customization.buttons.ok.specificActions[key] !== 'function') {
                        throw new Error(NLS.get('CEV.optionsChecker.buttons.specificActions.error', {
                            key: key
                        }));
                    }
                });
            }


            // Position
            if (!this.characteristicsEditorViewOptions.customization.buttons.position) {
                this.characteristicsEditorViewOptions.customization.buttons.position = defaultCustomizationValues.buttons.position;
            }

        };


        /*---- 3.7 ----*/
        let _checkCustomizationSearchOptions = function () {

            if (!this.characteristicsEditorViewOptions.customization.search) {
                this.characteristicsEditorViewOptions.customization.search = defaultCustomizationValues.search;
            } else {
                let that = this;
                Object.keys(this.characteristicsEditorViewOptions.customization.search).forEach(function (key) {
                    if (typeof that.characteristicsEditorViewOptions.customization.search[key].autocomplete !== 'function') {
                        throw new Error(NLS.get('CEV.optionsChecker.search.error', {
                            key: key
                        }));
                    }
                });
            }

        };


        /*---- 3.8 ----*/
        let _checkCustomizationFiltersOptions = function () {

            if (!this.characteristicsEditorViewOptions.customization.filters) {
                this.characteristicsEditorViewOptions.customization.filters = defaultCustomizationValues.filters;
            }

            if (!this.characteristicsEditorViewOptions.customization.filters.whitelist) {
                this.characteristicsEditorViewOptions.customization.filters.whitelist = defaultCustomizationValues.filters.whitelist;
            }

            if (!this.characteristicsEditorViewOptions.customization.filters.blacklist) {
                this.characteristicsEditorViewOptions.customization.filters.blacklist = defaultCustomizationValues.filters.blacklist;
            }

            if (!this.characteristicsEditorViewOptions.customization.filters.orderlist) {
                this.characteristicsEditorViewOptions.customization.filters.orderlist = defaultCustomizationValues.filters.orderlist;
            }

        };


        /*---- 3.9 ----*/
        let _checkCustomizationOnChangeSensitiveOptions = function () {

            if (this.characteristicsEditorViewOptions.customization.onChangeSensitive === undefined) {
                this.characteristicsEditorViewOptions.customization.onChangeSensitive = defaultCustomizationValues.onChangeSensitive;
            }

        };


        /*---- 4.1 ----*/
        let _checkEventsOnFinishRenderOptions = function () {

            if (!this.characteristicsEditorViewOptions.events.onFinishRender) {
                this.characteristicsEditorViewOptions.events.onFinishRender = defaultEvents.onFinishRender;
            }

        };


        //---- 4.2 ----*/
        let _checkEventsOnSelectLanguageOptions = function () {

            if (!this.characteristicsEditorViewOptions.events.onSelectLanguage) {
                this.characteristicsEditorViewOptions.events.onSelectLanguage = defaultEvents.onSelectLanguage;
            }

        };


        return {

            /*********************************************** 1 ***********************************************/
            /**
             * @summary Check all options passed to the view
             */
            checkAllOptions: function () {
                this.checkMandatoryOptions();      // 2
                this.checkCustomizationOptions();  // 3
                this.checkEventsOptions();         // 4
            },


            /*********************************************** 2 ***********************************************/
            /**
             * @summary Check mandatory options
             */
            checkMandatoryOptions: function () {
                if (!this.characteristicsEditorViewOptions.rdfServerURL) throw new Error(NLS.get('CEV.optionsChecker.rdfServerURL.error'));
                if (!this.characteristicsEditorViewOptions.resourceURI) throw new Error(NLS.get('CEV.optionsChecker.resourceURI.error'));
            },


            /*********************************************** 3 ***********************************************/
            /**
             * @summary Check customization/optional options
             */
            checkCustomizationOptions: function () {

                if (!this.characteristicsEditorViewOptions.customization) {
                    this.characteristicsEditorViewOptions.customization = defaultCustomizationValues;
                }

                _checkCustomizationTitlesOptions.bind(this)();              // 3.1
                _checkCustomizationModeOptions.bind(this)();                // 3.2
                _checkCustomizationDisplayOptions.bind(this)();             // 3.3
                _checkCustomizationConstraintsPositionOptions.bind(this)(); // 3.5
                _checkCustomizationButtonsOptions.bind(this)();             // 3.6
                _checkCustomizationSearchOptions.bind(this)();              // 3.7
                _checkCustomizationFiltersOptions.bind(this)();             // 3.8
                _checkCustomizationOnChangeSensitiveOptions.bind(this)();   // 3.9

            },


            /*********************************************** 4 ***********************************************/
            /**
             * @summary Check events options
             */
            checkEventsOptions: function () {

                if (!this.characteristicsEditorViewOptions.events) {
                    this.characteristicsEditorViewOptions.events = defaultEvents;
                }

                _checkEventsOnFinishRenderOptions.bind(this)();             // 4.1
                _checkEventsOnSelectLanguageOptions.bind(this)();           // 4.2

            }

        };

    })();

    return CharacteristicsEditorViewOptionsCheckerTool.prototype.constructor = CharacteristicsEditorViewOptionsCheckerTool;

});
