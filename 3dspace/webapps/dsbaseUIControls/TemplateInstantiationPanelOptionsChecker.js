define('DS/dsbaseUIControls/TemplateInstantiationPanelOptionsChecker', [
    'DS/dsbaseUIServices/TemplateServices',
    'i18n!DS/dsbaseUIControls/assets/nls/TemplateInstantiationPanel'
], function (
    TemplateServices,
    NLS
) {

    /**
         * Default values to use if some options are not set
         */
    let defaultValues = {
        title: 'Instantiation Panel',
        editMode: {
            templateChoice: true,
            characteristics: true
        },
        languages: {
            supported: ['en'],
            selected: 'en'
        },
        display: {
            title: true,
            characteristics: {
                readOnly: true,
                constraints: true
            },
            scroller: true,
            buttons: true
        },
        buttons: {
            cancel: {
                label: 'Cancel',
                type: 'default',
                defaultAction: function (rootURL, resourceURI, formValues) { }
            },
            ok: {
                label: 'Instantiate',
                type: 'primary',
                defaultAction: function (rootURL, templateURI, formValues, language) {

                    // Loop over form values to convert string numbers to numbers
                    let values = {};
                    Object.keys(formValues).forEach(function (key) {
                        values[key] = formValues[key].value;
                    });

                    // Create template instance according to template selected
                    console.log(language);
                    let TempServices = TemplateServices.getHandle(rootURL, language);
                    return TempServices.instantiate({
                        templateURI: templateURI,
                        inputs: values,
                        cbObject: {
                            onComplete: function () { },
                            onFailure: function (error) { }
                        }
                    });

                }
            }
        },
        constraintsPosition: 'top',
        buttonsPosition: 'right',
        filters: {}
    };


    /**
     * Default events functions
     */
    let defaultEvents = {
        onFinishRender: function () { }
    };


    /**
    * @summary PanelOptionsCheckerTool constructor
    * @class DS/dsbaseUIControls/TemplateInstantiationPanelOptionsChecker
	*
	* @param {Object} panelOptions - Panel options to check
	*/
    function PanelOptionsCheckerTool(panelOptions) {
        this.panelOptions = panelOptions;
    };


    PanelOptionsCheckerTool.prototype = (function () {

        /*---- 3.1 ----*/
        let _checkCustomizationTitleOptions = function () {

            if (!this.panelOptions.customization.title) {
                this.panelOptions.customization.title = defaultValues.title;
            }

        };


        /*---- 3.2 ----*/
        let _checkCustomizationModeOptions = function () {

            if (!this.panelOptions.customization.editMode) {
                this.panelOptions.customization.editMode = defaultValues.editMode;
            }

            if (this.panelOptions.customization.editMode.templateChoice === undefined) {
                this.panelOptions.customization.editMode.templateChoice = defaultValues.editMode.templateChoice;
            }

            if (this.panelOptions.customization.editMode.characteristics === undefined) {
                this.panelOptions.customization.editMode.characteristics = defaultValues.editMode.characteristics;
            }

        };


        /*---- 3.3 ----*/
        let _checkCustomizationLanguagesOptions = function () {

            if (this.panelOptions.customization.languages === undefined) {
                this.panelOptions.customization.languages = defaultValues.languages;
            }

            /***** SUPPORTED *****/
            if (this.panelOptions.customization.languages.supported === undefined) {
                this.panelOptions.customization.languages.supported = defaultValues.languages.supported;
            }

            if (!Array.isArray(this.panelOptions.customization.languages.supported)) {
                throw new Error('Supported languages should be given through an array!');
            }

            /***** SELECTED *****/
            if (this.panelOptions.customization.languages.selected === undefined) {
                this.panelOptions.customization.languages.selected = this.panelOptions.customization.languages.supported[0];
            }

            if (typeof this.panelOptions.customization.languages.selected !== 'string') {
                throw new Error('Selected language should be a string!');
            }

            if (!this.panelOptions.customization.languages.supported.includes(this.panelOptions.customization.languages.selected)) {
                this.panelOptions.customization.languages.supported.push(this.panelOptions.customization.languages.selected);
            }

        };


        /*---- 3.4 ----*/
        let _checkCustomizationDisplayOptions = function () {

            if (!this.panelOptions.customization.display) {
                this.panelOptions.customization.display = defaultValues.display;
            }

            if (this.panelOptions.customization.display.title === undefined) {
                this.panelOptions.customization.display.title = defaultValues.display.title;
            }

            if (this.panelOptions.customization.display.titleEditor === undefined) {
                this.panelOptions.customization.display.titleEditor = defaultValues.display.titleEditor;
            }

            if (!this.panelOptions.customization.display.characteristics) {
                this.panelOptions.customization.display.characteristics = defaultValues.display.characteristics;
            }

            if (this.panelOptions.customization.display.characteristics.constraints === undefined) {
                this.panelOptions.customization.display.characteristics.constraints = defaultValues.display.characteristics.constraints;
            }

            if (this.panelOptions.customization.display.characteristics.readOnly === undefined) {
                this.panelOptions.customization.display.characteristics.readOnly = defaultValues.display.characteristics.readOnly;
            }

            if (this.panelOptions.customization.display.scroller === undefined) {
                this.panelOptions.customization.display.sroller = defaultValues.display.scroller;
            }

            if (this.panelOptions.customization.display.buttons === undefined) {
                this.panelOptions.customization.display.buttons = defaultValues.display.buttons;
            }

        };

        /*---- 3.5 ----*/
        let _checkCustomizationConstraintsPositionOptions = function () {
            if (!this.panelOptions.customization.constraintsPosition) {
                this.panelOptions.customization.constraintsPosition = defaultValues.constraintsPosition;
            } else if (!['top', 'right', 'bottom', 'left'].includes(this.panelOptions.customization.constraintsPosition)) {
                throw new Error('Position for constraints popup should be "top", "right", "bottom" or "left"');
            }
        };


        /*---- 3.6 ----*/
        let _checkCustomizationButtonsOptions = function () {

            if (!this.panelOptions.customization.buttons) {
                this.panelOptions.customization.buttons = defaultValues.buttons;
            }

            // Cancel
            if (!this.panelOptions.customization.buttons.cancel) {
                this.panelOptions.customization.buttons.cancel = defaultValues.buttons.cancel;
            }

            if (!this.panelOptions.customization.buttons.cancel.label) {
                this.panelOptions.customization.buttons.cancel.label = defaultValues.buttons.cancel.label;
            }

            if (!this.panelOptions.customization.buttons.cancel.defaultAction) {
                this.panelOptions.customization.buttons.cancel.defaultAction = defaultValues.buttons.cancel.defaultAction;
            }

            if (!this.panelOptions.customization.buttons.cancel.type) {
                this.panelOptions.customization.buttons.cancel.type = defaultValues.buttons.cancel.type;
            } else if (!['primary', 'default', 'success', 'info', 'warning', 'error'].includes(this.panelOptions.customization.buttons.cancel.type)) {
                throw new Error('Type for button '+this.panelOptions.customization.buttons.cancel.label+' should be one of these : "primary", "default", "info", "success", "warning", "error"');
            }


            // Ok
            if (!this.panelOptions.customization.buttons.ok) {
                this.panelOptions.customization.buttons.ok = defaultValues.buttons.ok;
            }

            if (!this.panelOptions.customization.buttons.ok.label) {
                this.panelOptions.customization.buttons.ok.label = defaultValues.buttons.ok.label;
            }

            if (!this.panelOptions.customization.buttons.ok.defaultAction) {
                this.panelOptions.customization.buttons.ok.defaultAction = defaultValues.buttons.ok.defaultAction;
            }

            if (!this.panelOptions.customization.buttons.ok.type) {
                this.panelOptions.customization.buttons.ok.type = defaultValues.buttons.ok.type;
            } else if (!['primary', 'default', 'success', 'info', 'warning', 'error'].includes(this.panelOptions.customization.buttons.ok.type)) {
                throw new Error('Type for button '+this.panelOptions.customization.buttons.ok.label+' should be one of these : "primary", "default", "info", "success", "warning", "error"');
            }


            // Position
            if (!this.panelOptions.customization.buttons.position) {
                this.panelOptions.customization.buttons.position = defaultValues.buttons.position;
            }

        };


        /*---- 3.7 ----*/
        let _checkCustomizationFiltersOptions = function () {

            if (!this.panelOptions.customization.filters) {
                this.panelOptions.customization.filters = defaultValues.filters;
            }

            if (!this.panelOptions.customization.filters.whitelist) {
                this.panelOptions.customization.filters.whitelist = defaultValues.filters.whitelist;
            }

            if (!this.panelOptions.customization.filters.blacklist) {
                this.panelOptions.customization.filters.blacklist = defaultValues.filters.blacklist;
            }

            if (!this.panelOptions.customization.filters.orderlist) {
                this.panelOptions.customization.filters.orderlist = defaultValues.filters.orderlist;
            }

        };


        /*---- 4.1 ----*/
        let _checkEventsOnFinishRenderOptions = function () {

            if (!this.panelOptions.events.onFinishRender) {
                this.panelOptions.events.onFinishRender = defaultEvents.onFinishRender;
            }

        };



        return {

            /*********************************************** 1 ***********************************************/
            /**
             * @summary Check all options passed to the panel
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

                if (!this.panelOptions.rdfServerURL) {
                    throw new Error('RDF server URL must be defined!');
                }

                // Verify if some templates URIs or an option set are defined
                if (!this.panelOptions.templates || (!this.panelOptions.templates.list && !this.panelOptions.templates.fromOptionSet)) {
                    throw new Error('At least an array of templates URIs or an option set must be passed to the view!');
                }

                // Verify if fromOptionSet object is well constructed
                if (this.panelOptions.templates.fromOptionSet && (this.panelOptions.templates.fromOptionSet.uri === undefined /* || this.panelOptions.templates.fromOptionSet.taxonomy === undefined */)) {
                    throw new Error('"uri" & "taxonomies" parameters should be defined to retrieve templates from an option set!');
                }

                // Create a list of wanted templates at the end
                if (!this.panelOptions.templates.list) {
                    this.panelOptions.templatesFinalList = [];
                } else {
                    this.panelOptions.templatesFinalList = this.panelOptions.templates.list;
                }

            },


            /*********************************************** 3 ***********************************************/
            /**
             * @summary Check customization/optional options
             */
            checkCustomizationOptions: function () {

                if (!this.panelOptions.customization) {
                    this.panelOptions.customization = defaultValues;
                }

                _checkCustomizationTitleOptions.bind(this)();               // 3.1
                _checkCustomizationModeOptions.bind(this)();                // 3.2
                _checkCustomizationLanguagesOptions.bind(this)();           // 3.3
                _checkCustomizationDisplayOptions.bind(this)();             // 3.4
                _checkCustomizationConstraintsPositionOptions.bind(this)(); // 3.5
                _checkCustomizationButtonsOptions.bind(this)();             // 3.6
                _checkCustomizationFiltersOptions.bind(this)();             // 3.7

            },


            /*********************************************** 4 ***********************************************/
            /**
             * @summary Check customization/optional options
             */
            checkEventsOptions: function () {

                if (!this.panelOptions.events) {
                    this.panelOptions.events = defaultEvents;
                }

                _checkEventsOnFinishRenderOptions.bind(this)();             // 4.1

            }

        };

    })();

    return PanelOptionsCheckerTool.prototype.constructor = PanelOptionsCheckerTool;

});
