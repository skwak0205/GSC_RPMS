define('DS/dsbaseUIControls/CharacteristicsView', [
    'DS/dsbaseUIControls/CharacteristicsViewOptionsChecker',
    'DS/dsbaseUIServices/CharacteristicServices',
    'DS/dsbaseUIControls/UnitConversionView',
    'DS/dsbaseUIControls/UnitsCacheInitialization',
    'DS/dsbaseUIControls/UnitsPreferences',
    'DS/dsbaseUIControls/GenericComponentsForm',
    'DS/dsbaseUIControls/CharacteristicValuesSearcherSuggestionItemWithText',
    'DS/dsbaseUIServices/BaseServices',
    'UWA/Core',
    'UWA/Class/View',
    'DS/UIKIT/Spinner',
    'DS/UIKIT/Iconbar',
    'DS/UIKIT/Scroller',
    'DS/UIKIT/Popover',
    'DS/UIKIT/Input/Text',
    'DS/UIKIT/Modal',
    'DS/UIKIT/SuperModal',
    'i18n!DS/dsbaseUIControls/assets/nls/CharacteristicsView',
    'css!DS/UIKIT/UIKIT.css',
    'css!DS/dsbaseUIControls/assets/style/CharacteristicsView.css'
], function (
    CharacteristicsViewOptionsChecker,
    CharacteristicServices,
    UnitConversionView,
    UnitsCacheInitialization,
    UnitsPreferences,
    GCForm,
    CharacteristicValuesSearcherSuggestionItemWithText,
    BaseServices,
    UWA,
    View,
    Spinner,
    Iconbar,
    Scroller,
    Popover,
    TextInput,
    Modal,
    SuperModal,
    NLS
) {

    'use strict';

    /**
    * @summary Generic view to list characteristics of a given resource
    * @extends UWA/Class/View
    */
    let CharacteristicsView = View.extend({

        tagName: 'div',


        /**
         * @private
         * @summary Add complementary class names to the characteristics view component
         * @returns {String} - Class name(s)
         * @override
          */
        className: function () {

            let className = 'characteristics-view';

            if (this.options.customization) {

                // Verify if other CSS class names are defined
                if (this.options.customization.classNames !== undefined) {
                    this.options.customization.classNames.forEach(function (cn) {
                        className += ' ' + cn;
                    });
                }

                // Verify if scroll is used (add a specific class if not)
                if (this.options.customization.display && this.options.customization.display.scroller === false) {
                    className += ' characteristics-view-no-scroll';
                }

            }

            return className;

        },


        /*********************** SEARCH ***********************/
        /**
         * Suggestion according to text entered by the user
         * @typedef     {Object} Suggestion
         * @property    {String} label  - Label to display when searching for a text
         * @property    {String} value  - Value being set
         */

        /**
         * Callback function to search in a JSON list
         * @typedef {Function} searchInLocal
         * @param   {String} text       - Text to search
         * @param   {Item[]} localItems - Local items used for searching
         */

        /**
         * Callback function to search in FedSearch
         * @typedef {Function} searchInFedSearch
         * @param   {String}    query   - Query to execute through FedSearch service
         * @param   {String}    tenant  - Tenant to use
         * @param   {String[]}  sources - FedSearch sources
         */

        /**
         * Callback used to search resources for a given text
         * @callback    SearchEngine
         * @param       {String} text - Text to search
         * @param       {Object.<String, searchInLocal|searchInFedSearch>}  searchServices  - OOTB services that can be used to search resources matching the given text
         * @returns     {Suggestion[]|Promise.<Suggestion[]>}
         */

        /**
         * Describe the expected type of object for search options
         * @typedef SearchOption
         * @type {Object}
         * @property {SearchEngine}     [autocomplete=]                          - Autcomplete search function called when user starts writing in corresponding input (show suggestions)
         * @property {Function}         [options.customization.onSuggest=]       - Function called when an item is shown in the list of suggestions (has to return a UWA/Element to render the associated item in the list of suggestions)
         * @property {Function}         [options.customization.onSelect=]        - Function called when an item is selected in the list of suggestions (has to return a string to render the associated badge)
         */


        /*********************** UNITS ***********************/
        /**
         * Unit
         * @typedef Unit
         * @type {Object}
         * @property {String} uri       - Short URI of the unit
         * @property {String} symbol    - Symbol associated to the unit (examples : 'kg', 'm3', 'cm' ...)
         */


        /*********************** GET INFOS ***********************/
        /**
         * Object with characteristic infos
         * @typedef  {Object}   CharacteristicInfos
         * @property {String}   attr                        - Long URI of the characteristic
         *
         * @property {Object}   infos                       - Object containing all the infos related to the characteristic
         * @property {String[]} [infos.AuthorizedValues]    - List of authorized values
         * @property {Boolean}  infos.IsList                - Boolean to indicate if the characteristic value is a list of values
         * @property {String}   infos.Label                 - Label associated to the characteristic
         * @property {Boolean}  infos.LongString            - Boolean to indicate if the characteristic contains a lot of text (textarea)
         * @property {Boolean}  infos.ReadOnly              - Boolean to indicate if the characteristic should be rendered in read-only mode
         * @property {Boolean}  infos.User                  - Boolean to indicate if the characteristics is a user characteristic (i.e. added by the user on the resource)
         *
         * @property {Object}   [infos.MinMax]              - Object to specify min & max values for a number input
         * @property {Object}   infos.MinMax.min            - Information about the minimum value
         * @property {String}   infos.MinMax.min.value      - Minimum value for a number input
         * @property {Boolean}  infos.MinMax.min.inclusive  - Is the minimum value inclusive ?
         * @property {Object}   infos.MinMax.max            - Information about the maximum value
         * @property {String}   infos.MinMax.max.value      - Maximum value for a number input
         * @property {Boolean}  infos.MinMax.max.inclusive  - Is the maximum value inclusive ?
         *
         * @property {String}   infos.Name                  - Long URI of the characteristic
         * @property {String}   infos.Type                  - Long URI of the RDF characteristic type
         *
         * @property {Unit}     [infos.Unit]                - Information about the unit associated to the characteristic
         * @property {Unit[]}   [infos.Units]               - Information about the units associated to the characteristic
         *
         * @property {String|Number|Boolean|Array<String>|Array<Number>|Array<Item>} [infos.Value]  - Default value of the characteristic when the component is created
         */

        /**
         * Callback function to get characteristics infos
         * @callback GetInfos
         * @returns {Promise.<CharacteristicInfos[]>}
         */


        /*********************** CONSTRUCTOR ***********************/
        /**
         * @param {Object}                          options                                                             - Options to adapt behavior & customize the view
         * @param {String}                          options.rdfServerURL                                                - RDF server root URL (mandatory)
         * @param {String}                          options.resourceURI                                                 - URI of the resource for which we list characteristics (mandatory)
         *
         * @param {String}                          [options.platformId]                                                - platform Id used to call MyApps services (i3DXCompassPlatformServices.getPlatformServices). Will retrieve the first result returned otherwise
         *
         * @param {Object}                          [options.customization]                                             - Object to define customization parameters to render the view
         * @param {String}                          [options.customization.preferenceId]                                - View id (used to retrieve preferences - units - from previous utilization)
         * @param {String[]}                        [options.customization.classNames=[]]                               - Array of additionnal CSS class names
         * @param {Boolean}                         [options.customization.editMode=true]                               - Boolean to indicate if inputs should be disabled or not (true => edit, false => read)
         *
         * @param {Object}                          [options.customization.languages]                                   - Languages options
         * @param {Array.<String>}                  [options.customization.languages.supported=<platform_languages>]    - Array of languages proposed to the user (i.e. array of RDF languages codes)
         * @param {String}                          [options.customization.languages.selected="en"]                     - Language selected on view construction
         *
         * @param {GetInfos}                        [options.customization.getInfos]                                    - Function to use to retrieve characteristic infos (instead of default 'dsbase:characteristic.getInfos' webservice)
         *
         * @param {Object}                          [options.customization.display]                                     - Object to define display settings (i.e. some components should be displayed or not ?)
         * @param {Boolean}                         [options.customization.display.switchMode=true]                     - Boolean to indicate if "switch mode" button should be displayed or not
         * @param {Boolean}                         [options.customization.display.switchLanguage=true]                 - Boolean to indicate if "switch language" button should be displayed or not
         * @param {Boolean}                         [options.customization.display.characteristics.readOnly=true]       - Boolean to indicate if read-only characteristics should be displayed or not
         * @param {Boolean}                         [options.customization.display.characteristics.constraints=true]    - Boolean to indicate if characteristics constraints within a popover should be displayed or not
         * @param {Boolean}                         [options.customization.display.scroller=true]                       - Boolean to indicate if scroll view should be used
         *
         * @param {String}                          [options.customization.constraintsPosition="top"]                   - Position of the constraints popup relative to the 'information' icon (i). It can be one of these : 'top', 'right', 'bottom' or 'left'
         *
         * @param {Object.<string, Object>}         [options.customization.search={}]                                   - Object to define autocomplete inputs
         * @param {SearchOption}                    options.customization.search[URI].autocomplete                      - Autcomplete search function
         * @param {Function}                        [options.customization.search[URI].onSuggest]                       - Function to construct UI when an item is suggested (according to user entry)
         * @param {Function}                        [options.customization.search[URI].onSelect]                        - Function to construct UI when an item is selected
         * @param {Function}                        [options.customization.search[URI].onCleanDataset]                  - Callback called when the input is cleaned
         *
         * @param {Object}                          [options.customization.filters]                                     - Object to define filters (filters are taken into account in this order: 1. whitelist  2. blacklist  3. order )
         * @param {String[]}                        [options.customization.filters.whitelist=[]]                        - Array of URIs to retrieve specific characteristics
         * @param {String[]}                        [options.customization.filters.blacklist=[]]                        - Array of URIs to avoid retrieving some characteristics
         * @param {String[]}                        [options.customization.filters.orderlist=["http://www.3ds.com/RDF/Corpus/Kernel/nlsLabel","http://www.3ds.com/RDF/Corpus/Kernel/nlsComment","http://www.3ds.com/RDF/Corpus/Kernel/owner']] - Array of URIs to order retrieved characteristics, according to the index in the array
         *
         * @param {String[]}                        [options.customization.forceEdit=[]]                                - Array of URIs to force wanted characteristics to be editable
         *
         * @param {Boolean}                         [options.customization.onChangeSensitive=true]                      - Boolean to indicate if only modified characteristics should be taken into account
         *
         * @param {Object}                          [options.events]                                                    - Object to store events callbacks
         * @param {Function}                        [options.events.onSwitchMode]                                       - Callback called when a new mode is selected
         * @param {Function}                        [options.events.onValueChange]                                      - Callback called when a value is modified in an input
         * @param {Function}                        [options.events.onFinishRender]                                     - Callback called when the view is rendered
         * @param {Function}                        [options.events.onSelectLanguage]                                   - Callback called when a language is selected in the dropdown
         * @param {Function}                        [options.events.onSaveLanguage]                                     - Callback called when the user clicks on the "Save" button in the warning popup (after modifying language)
         * @param {Function}                        [options.events.onDiscardLanguage]                                  - Callback called when the user clicks on the "Discard" button in the warning popup (after modifying language)
         * @param {Function}                        [options.events.onCancelLanguage]                                   - Callback called when the user clicks on the "Cancel" buttons in the warning ppopup (after modifying language)
         *
         * @constructs CharacteristicsView
         * @summary Setup component
         * @memberof module:DS/dsbaseUIControls/CharacteristicsView
         * @override
         */
        setup: function (options) {

            // Checking options
            (new CharacteristicsViewOptionsChecker(options)).checkAllOptions();
            this.options = options;

            // Characteristic services
            this.characteristicServices = CharacteristicServices.getHandle(this.options.rdfServerURL, this.options.customization.languages.selected);

            // Data
            this.originalUnits = {};
            this.originalPrefixes = {};
            this.currentUnits = {};
            this.currentPrefixes = {};
            this.currentValues = {};
            this.originalLanguage = this.options.customization.languages.selected;
            this.currentLanguage = this.options.customization.languages.selected;

            // Units preferences
            if (options.customization.preferenceId) {
                this.unitsPreferences = new UnitsPreferences({
                    id: options.customization.preferenceId
                });
                this.hasPreferences = true;
            }

            // Building characteristics view
            let that = this;
            //console.log(this);
            this.launchSpinnerOnLoad();
            return that.createView();

        },


        /**
         * @private
         * @summary Create the complete view from characteristics list (given by the server)
         */
        createView: function() {

            let that = this;

            return this.generateCharacteristicsFields().then(function (fields) {

                // Save list on 'this'
                that.elements.listCharacteristics = new GCForm({
                    eventFunction: that.getEventFunction(),
                    className: 'characteristics-view-form-characteristics',
                    fields: fields
                });

                if (fields.length > 0) {

                    if (that.options.customization.display.scroller) {
                        that.createScroller();
                    }

                    that.addEventsToFields();

                    that.createIconsBar();

                    if (that.options.customization.display.characteristics.constraints) {
                        that.createPopovers();
                    }

                    that.completeSpecialFields();
                    that.updateValuesFromUnitsPreferences();
                    //that.updateValuesForNLSInputs();

                    // Check if no NLS characteristic has a value in the current language
                    if ((that.options.customization.checkNLSOnConstruction) && (!that.hasNLSValuesForCurrentLanguage(fields))) {
                        that.currentLanguage = 'en';
                        if ((that.elements.iconsBar) && (that.options.customization.display.switchLanguage)) {
                            that.elements.iconsBar.getItem(NLS.get('CV.languages.icon.text')).elements.icon.setHTML('EN');
                        }
                    }

                } else {
                    that.createDefaultMessage();
                }

                that.render();
            });
        },


        /**
        * @private
        * @summary Render the view with created components
        * @override
        */
        render: function () {

            // Hide spinner after everything is loaded
            if (this.elements.spinner) {
                this.elements.spinner.hide();
            }

            // Inject switch button
            if (this.elements.iconsBar) {
                this.elements.iconsBar.inject(this.container);
            }

            // Verify if scroller has been previously created and inject it
            if (this.elements.scroller) {
                this.elements.scroller.inject(this.container);

            // Or inject directly the list of characteristics
            } else if (this.elements.listCharacteristics.getFields().length > 0) {
                this.elements.listCharacteristics.inject(this.container);

            // Otherwise, inject a default message to say that there's no characteristic
            } else if (this.elements.defaultMessage) {
                this.elements.defaultMessage.inject(this.container);
            }

            // Back-up form values
            this.backup();

            // Set up variables to detect modified characteristics
            this.backupFormValuesOnChange = this.backupFormValues;
            this.modifiedValues = [];

            if (this.options.events.onFinishRender !== undefined) {
                this.options.events.onFinishRender.bind(this)();
            }

            return this;

        },


        /**
        * @private
        * @summary Raise error when the function is called
        * @param {Object}  error           - Error
        * @param {String}  message         - Message to display when error is raised
        * @param {Boolean} isInjectable    - Boolean to indicate if error should be integrated in the user interface (view)
        */
        raiseError: function (error, message, isInjectable) {

            this.elements.spinner.hide();

            if (isInjectable) {
                new UWA.Element('div', {
                    content: message,
                    class: 'characteristics-view-default-message'
                }).inject(this.container);
            }

            console.log(error);
            throw new Error(message);

        },


        /**
        * @private
        * @summary Create default message when no characteristics retrieved
        */
        createDefaultMessage: function () {

            this.elements.defaultMessage = new UWA.Element('div', {
                content: NLS.get('CV.noCharacteristics.error'),
                class: 'characteristics-view-default-message'
            });

        },


        /**
         * @private
         * @summary Callback to modify icon for "switchMode" option
         */
        iconFunction: function () {

            let text = (this.options.customization.editMode) ? NLS.get('CV.mode.icon.text.switchReadOnly') : NLS.get('CV.mode.icon.text.switchEdit');
            let item = this.elements.iconsBar.getItem(text);
            let icon = item.elements.icon;
            let isLocked = icon.hasClassName('fonticon-pencil-locked');

            if (isLocked) {
                icon.removeClassName('fonticon-pencil-locked');
                icon.addClassName('fonticon-pencil');
            } else {
                icon.removeClassName('fonticon-pencil');
                icon.addClassName('fonticon-pencil-locked');
            }

        },


        createSwitchModeButtonItem: function() {

            let that = this;

            let fonticon = (this.options.customization.editMode) ? 'pencil' : 'pencil-locked';
            let text = (this.options.customization.editMode) ? NLS.get('CV.mode.icon.text.switchReadOnly') : NLS.get('CV.mode.icon.text.switchEdit');

            return {
                fonticon: fonticon,
                text: text,
                handler: function () {
                    // Switch mode for characteristics listing
                    that.options.fromClick = true;
                    that.switchMode(!that.options.customization.editMode);
                }
            };

        },


        createMultiLanguageButtonItem: function() {

            let that = this;
            let languageOptions = [];

            this.options.customization.languages.supported.forEach(function(language) {
                let languageUC = language.toUpperCase();
                languageOptions.push({
                    text: languageUC,
                    name: language,
                    handler: function () {

                        let currentLanguageUC = that.currentLanguage.toUpperCase();

                        let applyChangesIconsBar = function() {
                            if (that.elements.iconsBar) {
                                let item = that.elements.iconsBar.getItem(NLS.get('CV.languages.icon.text')).elements.container;
                                item.getElement('.fonticon').setContent(languageUC);
                            }
                        };

                        let applyChangesNLSInputs = function () {
                            if ((that.elements.listCharacteristics) && (!that.switchedLanguage)) {
                                that.updateNLSInputs();
                                that.switchedLanguage = true;
                            }
                        };

                        if (language !== that.currentLanguage) {

                            // Ask user confirmation
                            if (that.modifiedValues.length > 0) {
                                let superModal = new SuperModal({
                                    className: 'characteristics-view-languages-modal'
                                });
                                superModal.dialog({
                                    body:  '<p>\
                                                <br> \
                                                ' + NLS.get('CV.languages.modal.first', { currentLanguage: currentLanguageUC }) + ' \
                                                <br> \
                                                ' + NLS.get('CV.languages.modal.second', { newLanguage: languageUC }) + '\
                                            </p>',
                                    title: NLS.get('CV.languages.modal.title'),
                                    buttons: [
                                        {
                                            value: NLS.get('CV.languages.modal.buttons.save'),
                                            className: 'primary',
                                            action: function (modal) {
                                                let originalLanguage = that.currentLanguage;
                                                applyChangesIconsBar();
                                                that.options.events.onSaveLanguage(originalLanguage, language).then(function() {
                                                    that.options.events.onSelectLanguage(language);
                                                    applyChangesNLSInputs();
                                                    modal.destroy();
                                                });
                                            }
                                        },
                                        {
                                            value: NLS.get('CV.languages.modal.buttons.discard'),
                                            className: 'primary',
                                            action: function (modal) {
                                                let originalLanguage = that.currentLanguage;
                                                applyChangesIconsBar();
                                                that.options.events.onDiscardLanguage(originalLanguage, language).then(function () {
                                                    that.options.events.onSelectLanguage(language);
                                                    applyChangesNLSInputs();
                                                    modal.destroy();
                                                });
                                            }
                                        },
                                        {
                                            value: NLS.get('CV.languages.modal.buttons.cancel'),
                                            action: function (modal) {
                                                that.options.events.onCancelLanguage(that.currentLanguage, language).then(function () {
                                                    modal.destroy();
                                                });
                                            }
                                        }
                                    ]
                                });
                            } else {
                                that.options.events.onChangeLanguageWithNoModification(that.currentLanguage, language).then(function() {
                                    that.options.events.onSelectLanguage(language);
                                    applyChangesIconsBar();
                                    applyChangesNLSInputs();
                                });
                            }

                        }

                    }
                });
            });

            return {
                fonticon: 'lang',
                text: NLS.get('CV.languages.icon.text'),
                innerComponent: {
                    type: 'dropdownmenu',
                    options: {
                        className: 'characteristics-view-languages-dropdown',
                        items: languageOptions,
                        maxItemsBeforeScroll: 4
                    }
                }
            };

        },


        /**
        * @private
        * @summary Create icons ibar with "language" & "switch mode" buttons
        * Language button: be able to set values in a specific language
        * Switch button: be able to switch between read-only and edit-mode
        */
        createIconsBar: function () {

            let hasSwitchMode = this.options.customization.display.switchMode;
            let hasSwitchLanguage = this.options.customization.display.switchLanguage;
            let items = [];

            // Multi language ?
            if (hasSwitchLanguage) {
                items.push(this.createMultiLanguageButtonItem());
            }

            // Switch mode ?
            if (hasSwitchMode) {
                items.push(this.createSwitchModeButtonItem());
            }

            // Create icons bar
            if (items.length > 0) {

                this.elements.iconsBar = new Iconbar({
                    className: 'characteristics-view-icons-bar',
                    items: items
                });

                // Text for switch language
                if (hasSwitchLanguage) {

                    let contentLanguageIcon = this.elements.iconsBar.getItem(NLS.get('CV.languages.icon.text')).elements.icon;
                    contentLanguageIcon.setHTML('<span>' + contentLanguageIcon.getText() + '</span>');

                    let item = this.elements.iconsBar.getItem(NLS.get('CV.languages.icon.text')).elements.container;
                    item.addClassName('big-item');
                    item.addContent('<span class="fonticon fonticon-chevron-down"></span>');

                    let content = this.originalLanguage.toUpperCase();
                    item.getElement('.fonticon').addContent(content);

                }

                // Mouse events for switch mode
                if (hasSwitchMode) {
                    let text = (this.options.customization.editMode) ? NLS.get('CV.mode.icon.text.switchReadOnly') : NLS.get('CV.mode.icon.text.switchEdit');
                    let item = this.elements.iconsBar.getItem(text).elements.container;
                    item.addEvent('mouseenter', this.iconFunction.bind(this));
                    item.addEvent('mouseleave', this.iconFunction.bind(this));
                }

            }

        },


        /**
        * @private
        * @summary Create scroller component
        */
        createScroller: function () {

            // Define a 'div' containing the list of characteristics
            let scrollerDiv = new UWA.Element('div', {
                class: 'characteristics-view-scroller'
            });
            this.elements.listCharacteristics.inject(scrollerDiv);

            // Create the scroller
            this.elements.scroller = new Scroller({
                element: scrollerDiv
            });

        },


        /**
        * @private
        * @summary Create popup information for constrainted characteristics
        */
        createPopovers: function () {

            let that = this;
            this.elements.listCharacteristics.getFields().forEach(function (field) {
                let input = that.getInputFromFieldName(field.name);

                if (input !== undefined) {

                    let infos = input.options.infos;
                    let formGroup = field.getClosest('.form-group');
                    let listConstraints = that.createConstraints(infos);

                    // Verify if label exists
                    // If there is a label, let's display an icon at the right (float)
                    if ((listConstraints.numberOfConstraints > 0) && (formGroup !== null)) {

                        // Create UWA Elements to display a badge
                        let badge = new UWA.Element('span', {
                            class: 'badge badge-default characteristics-view-badge'
                        });

                        let badgeContent = new UWA.Element('span', {
                            class: 'badge-content'
                        });

                        let icon = new UWA.Element('i', {
                            class: 'fonticon fonticon-info characteristics-view-icon'
                        });

                        // Inject elements at the right of input label
                        badge.inject(formGroup, 'top');
                        badgeContent.inject(badge);
                        icon.inject(badgeContent);

                        // Create the popover to list constraints
                        new Popover({
                            target: icon,
                            className: 'characteristics-view-popup',
                            position: that.options.customization.constraintsPosition,
                            body: listConstraints,
                            title: NLS.get('CV.constraints.popup.title'),
                            trigger: 'hover'
                        });

                    }
                }

            });

        },


        /**
        * @private
        * @summary Create a list of constraints for a Popover
        */
        createConstraints: function (infos) {

            let list = new UWA.Element('div', {
                className: 'characteristics-view-constraints-list'
            });
            list.numberOfConstraints = 0;

            // Read-Only constraint
            if (this.isCharacteristicReadOnly(infos) && infos.ReadOnly) {
                (new UWA.Element('span', {
                    content: '<b>' + NLS.get('CV.constraints.popup.readOnly') + '</b>' + '<br>'
                })).inject(list);
                list.numberOfConstraints++;
            }

            // Min constraint
            if (infos.MinMax && infos.MinMax.min) {
                (new UWA.Element('span', {
                    content: '<b>' + NLS.get('CV.constraints.popup.minValue') + '</b> : ' + infos.MinMax.min.value + '<br>'
                })).inject(list);
                list.numberOfConstraints++;
            }

            // Max constraint
            if (infos.MinMax && infos.MinMax.max) {
                (new UWA.Element('span', {
                    content: '<b>' + NLS.get('CV.constraints.popup.maxValue') + '</b> : ' + infos.MinMax.max.value + '<br>'
                })).inject(list);
                list.numberOfConstraints++;
            }

            return list;

        },


        /**
         * @private
         * @summary Launch spinner loader while characteristics are being retrieved
         */
        launchSpinnerOnLoad: function () {

            if (!this.elements.spinner) {

                this.elements.spinner = new Spinner({
                    className: 'characteristics-view-spinner',
                    animate: false
                }).inject(this.container)
                  .show();

            }

        },


        /**
         * @private
         * @summary Generate all fields according to characteristics information
         */
        generateCharacteristicsFields: function () {

            let that = this;

            return new Promise(function (resolve, reject) {
                that.getCharacteristicsInfos().then(function (characteristics) {

                    characteristics = that.orderCharacteristics(characteristics);

                    let fields = [];
                    characteristics.forEach(function (c) {
                        if (that.isCharacteristicDisplayable(c.infos)) {
                            let field = that.generateField(c.infos);
                            fields.push(field);
                        }
                    });

                    resolve(fields);

                });
            });

        },


        /**
         * @private
         * @summary Check if a characteristic expect person(s) as value(s)
         * @param {Object} characteristicInfos - Characteristic information to take into account
         * @returns {Boolean} Indicate if person(s) is (are) expected
         */
        isPerson: function (characteristicInfos) {

            if (characteristicInfos.Name === 'http://www.3ds.com/RDF/Corpus/Kernel/owner') {
                return true;
            }

            let hasMultiplePersons = false;
            let hasPerson = false;
            if (characteristicInfos.Type === 'http://xmlns.com/foaf/0.1/Agent') {

                // Multiple persons ?
                if (characteristicInfos.IsList) {
                    hasMultiplePersons = characteristicInfos.Value.some(function (v) {
                        return ((v.misc.givenName !== undefined) || (v.misc.familyName !== undefined) || (v.misc.fullName != undefined));
                    });
                } else {
                    let v = characteristicInfos.Value;
                    hasPerson = ((v.misc.givenName !== undefined) || (v.misc.familyName !== undefined) || (v.misc.fullName != undefined));
                }

            }

            return (hasMultiplePersons || hasPerson);

        },


        getLabelForPerson: function (value) {

            let label = '';

            // Label differente from URI ?
            if (value.uri !== value.label) {
                label += value.label;
            } else {

                // Given name ?
                if (value.misc.givenName) {
                    label += value.misc.givenName;
                }

                // Family name ?
                if (value.misc.familyName) {
                    if (label !== '') {
                        label += ' ';
                    }
                    label += value.misc.familyName;
                }

            }

            if (label === '') {
                label = value.uri;
            }

            return label;

        },


        /**
         * @private
         * @summary Generate an autocomplete field according to characteristic information
         * @param   {Object} characteristicInfos - Characteristic information to take into account
         * @returns {Object}
         */
        generateAutocompleteField: function (characteristicInfos) {

            let that = this;
            let eventFunction = this.getEventFunction();
            let isPerson = this.isPerson(characteristicInfos);

            // Look for an eventual search function, onSelect, onSuggest
            let searchKey = Object.keys(this.options.customization.search).find(function (key) {
                return (key === characteristicInfos.Name);
            });

            // If events are overrided, we override them again to give characteristic infos
            let searchFunction, onSelect, onSuggest, onCleanDataset;
            if (searchKey !== undefined) {
                searchFunction = this.options.customization.search[searchKey].autocomplete;

                if (that.options.customization.search[searchKey].onSelect !== undefined) {
                    onSelect = function(item) {
                        item.infos = characteristicInfos;
                        that.options.customization.search[searchKey].onSelect(item);
                    };
                }

                if (that.options.customization.search[searchKey].onSuggest !== undefined) {
                    onSuggest = function (item) {
                        item.infos = characteristicInfos;
                        that.options.customization.search[searchKey].onSuggest(item);
                    };
                }

                if (that.options.customization.search[searchKey].onCleanDataset !== undefined) {
                    onCleanDataset = function (item) {
                        item.infos = characteristicInfos;
                        that.options.customization.search[searchKey].onCleanDataset(item);
                    };
                }
            }

            // Check if value expect person(s)
            if (isPerson) {

                if (!onSelect) {
                    onSelect = function (item) {
                        return item.label;
                    };
                }

                if (!onSuggest) {
                    onSuggest = function (item) {
                        return new CharacteristicValuesSearcherSuggestionItemWithText({
                            label: item.label,
                            sublabel: ''
                        });
                    };
                }
            }

            // Add a search function by default (if no one defined)
            if (searchFunction === undefined) {
                if (isPerson) {
                    searchFunction = function (text, searchServices) {
                        return searchServices.searchInFedSearch(
                            '[ds6w:type]:"pno:Person" AND ([ds6w:label]:(' + text.trim() + '*) OR ' + text.trim() + ')',
                            that.options.platformId,
                            ['swym']
                        );
                    };
                } else {
                    searchFunction = function(text, searchServices) {
                        return new Promise(function (resolve, reject) {
                            let baseServices = BaseServices.getHandle(that.options.rdfServerURL);
                            baseServices.getSearchSuggestionsFromType({
                                rdfType: characteristicInfos.Type,
                                cbObject: {
                                    onComplete: function (data, status) {
                                        searchServices.searchInLocal(text, data).then(function(suggestions) {
                                            resolve(suggestions);
                                        });
                                    },
                                    onFailure: function (data, status, error) {
                                        resolve([]);
                                    }
                                }
                            });
                        });
                    };
                }
            }

            // Define field
            let field = {
                type: 'autocomplete',
                searchFunction: searchFunction,
                onSelect: onSelect,
                onSuggest: onSuggest,
                onCleanDataset: onCleanDataset,
                events: {
                    onSelect: function (event) {
                        if (that.elements.listCharacteristics) {
                            let value = that.elements.listCharacteristics.getValue(this.options.infos.Name);
                            eventFunction(this.options.infos.Name, value);
                        }
                    },
                    onUnselect: function (event) {
                        if (that.elements.listCharacteristics) {
                            let value = that.elements.listCharacteristics.getValue(this.options.infos.Name);
                            eventFunction(this.options.infos.Name, value);
                        }
                    }
                }
            };

            // Add a default value on input if it is defined
            if (characteristicInfos.Value) {
                let items = [];

                // Multiple values ?
                if (Array.isArray(characteristicInfos.Value)) {

                    // Array of persons ?
                    if (isPerson) {
                        characteristicInfos.Value.forEach(function (v) {
                            console.log(v.uri);
                            let label = that.getLabelForPerson(v);
                            items.push({
                                value: v.uri,
                                label: label
                            });
                        });

                    // Array of other resources ?
                    } else {
                        characteristicInfos.Value.forEach(function (v) {
                            let val = (v.value) ? v.value : v;
                            items.push({
                                value: String(val),
                                label: String((v.label) ? v.label : val)
                            });
                        });
                    }

                // Other cases
                } else {

                    let value;
                    let label = '';

                    // Person ?
                    if (isPerson) {
                        value = characteristicInfos.Value.uri;
                        label = this.getLabelForPerson(characteristicInfos.Value);

                    // Other cases
                    } else {
                        value = String(characteristicInfos.Value);
                        label = String(characteristicInfos.Value);
                    }

                    items.push({
                        value: value,
                        label: label
                    });
                }
                field.defaultItems = items;
            }

            return field;

        },


        /**
         * @private
         * @summary Generate a select field according to characteristic information
         * @param   {Object} characteristicInfos - Characteristic information to take into account
         * @returns {Object}
         */
        generateSelectField: function (characteristicInfos) {

            let field = {
                className: 'input-number',
                type: 'select',
                selectType: 'text',
                placeholder: null,
                options: []
            };

            // Check if authorized values list contains numbers
            if (characteristicInfos.UIType === 'number') {
                field.selectType = 'number';
            }

            if ((characteristicInfos.AuthorizedValuesNLS) && (characteristicInfos.AuthorizedValues) && (characteristicInfos.AuthorizedValues.length === characteristicInfos.AuthorizedValuesNLS.length)) {

                for (let i = 0; i < characteristicInfos.AuthorizedValuesNLS.length; i++) {
                    field.options.push({
                        value: characteristicInfos.AuthorizedValues[i],
                        label: characteristicInfos.AuthorizedValuesNLS[i]
                    });
                }

            } else {
                characteristicInfos.AuthorizedValues.forEach(function (av) {
                    field.options.push({
                        value: av,
                        label: av
                    });
                });
            }

            // Put default value if it exists
            if (characteristicInfos.Value) {
                field.value = String(characteristicInfos.Value);
            }

            return field;

        },


        /**
         * @private
         * @summary Generate a number field according to characteristic information
         * @param   {Object} characteristicInfos - Characteristic information to take into account
         * @returns {Object}
         */
        generateNumberField: function (characteristicInfos) {

            let field = {
                type: 'number',
                step: 1,
                min: -999999999999999999999999999999999999999999999999999999999999999999999999,
                max: 999999999999999999999999999999999999999999999999999999999999999999999999
            };


            if (characteristicInfos.MinMax) {

                // Deal with min value, if defined
                if (characteristicInfos.MinMax.min) {
                    field.min = (characteristicInfos.MinMax.min.inclusive) ? parseFloat(characteristicInfos.MinMax.min.value) : parseFloat(characteristicInfos.MinMax.min.value) + 1;
                }

                // Deal with max value, if defined
                if (characteristicInfos.MinMax.max) {
                    field.max = (characteristicInfos.MinMax.max.inclusive) ? parseFloat(characteristicInfos.MinMax.max.value) : parseFloat(characteristicInfos.MinMax.max.value) - 1;
                }

            }

            // Put default value, if it exists
            if (characteristicInfos.Value) {
                field.value = String(characteristicInfos.Value);
            }

            return field;

        },


        /**
        * @private
        * @summary Generate a textarea field according to characteristic information
        * @param    {Object} characteristicInfos - Characteristic information to take into account
        * @returns  {Object}
        */
        generateTextareaField: function (characteristicInfos) {

            let field = {
                type: 'textarea',
                value: (characteristicInfos.Value) ? characteristicInfos.Value : ''
            };

            return field;

        },


        /**
        * @private
        * @summary Generate a text field according to characteristic information
        * @param    {Object}    characteristicInfos - Characteristic information to take into account
        * @returns  {Object}
        */
        generateTextField: function (characteristicInfos) {

            /******** NEED TO BE OPTIMISED WITH "generateAutocompleteField" function ********/
            // If owner
            let value = '';
            if ((characteristicInfos.Name === 'http://www.3ds.com/RDF/Corpus/Kernel/owner') && ((characteristicInfos.GivenName) || (characteristicInfos.FamilyName))) {
                let hasGivenName = false;
                if (characteristicInfos.GivenName) {
                    hasGivenName = true;
                    value += characteristicInfos.GivenName
                }
                if (characteristicInfos.FamilyName) {
                    if (hasGivenName) {
                        value += ' ';
                    }
                    value += characteristicInfos.FamilyName;
                }

            // Other cases
            } else {
                value = String((characteristicInfos.Value) ? characteristicInfos.Value : '')
            }

            let field = {
                type: 'text',
                value: value
            };

            return field;

        },


        /**
         * @private
         * @summary Generate a date picker field according to characteristic information
         * @param   {Object} characteristicInfos - Characteristic information to take into account
         * @returns {Object}
         */
        generateDateField: function (characteristicInfos) {

            // let valueDate = (characteristicInfos.Value) ? (String(new Date(characteristicInfos.Value)).replace('Z', '').replace('T', '')) : '';
            let valueDate = new Date(characteristicInfos.Value);

            let field = {
                type: 'date',
                value: valueDate
            };

            return field;

        },


        /**
         * @private
         * @summary Generate a time field according to characteristic information
         * @param   {Object} characteristicInfos - Characteristic information to take into account
         * @returns {Object}
         */
        generateTimeField: function (characteristicInfos) {

            let value = String((characteristicInfos.Value) ? characteristicInfos.Value : '');
            if (value.startsWith('-T'))
                value = value.slice(2, value.length);
            if (value.endsWith('Z'))
                value = value.slice(0, -1);

            let field = {
                type       : 'text',
                placeholder: 'HH:MM:SS',
                pattern    : '^$|(([0-1]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9])',
                value      : value
            };

            return field;

        },


        /**
         * @private
         * @summary Generate a checkbox field according to characteristic information
         * @param   {Object} characteristicInfos - Characteristic information to take into account
         * @returns {Object}
         */
        generateCheckboxField: function (characteristicInfos) {

            let field = {
                type: 'checkbox',
                checked: characteristicInfos.Value
            };

            return field;

        },


        /**
         * @private
         * @summary Add key events (keyup, onselect, ...) on associated inputs (to detect changes)
         */
        addEventsToFields: function () {

            let that = this;
            let keyUpElements = ['textarea', 'text'];
            let onChangeElements = ['select', 'number', 'checkbox', 'date'];
            let onSelectElements = ['autocomplete'];

            Object.keys(this.elements.listCharacteristics.getValues()).forEach(function (key) {

                let input = that.getInputFromFieldName(key);

                if (input) {
                    if (onChangeElements.includes(input.options.type)) {
                        input.getContent().addEvent('change', function (event) {
                            if (input.options.type === 'checkbox') {
                                input.options.checked = !input.options.checked;
                                event.target.value = input.options.checked;
                            }
                            that.getOnChangeEventCallback().bind(input)(event);
                        });
                    } else if (!onSelectElements.includes(input.options.type)) {
                        input.getContent().addEvent('keyup', function (event) {
                            that.getOnChangeEventCallback().bind(input)(event);
                        });
                    }
                }

            });

        },


        /**
         * @private
         * @summary Get event function, used to detect modified values
         * @return {Function} - Function used to detect modified values when events on inputs are detected
         */
        getEventFunction: function () {

            let that = this;
            return function (name, value) {

                // Check if a backup exists & array is initialized
                if (that.backupFormValuesOnChange && that.modifiedValues) {

                    // Check if previous value & actual value are different
                    if (that.backupFormValuesOnChange[name] !== value) {
                        if (!that.modifiedValues.includes(name)) {
                            that.modifiedValues.push(name);
                        }
                    } else {
                        that.modifiedValues = that.modifiedValues.filter(function (longURI) {
                            return (longURI !== name);
                        });
                    }

                    if (that.modifiedValues.length === 0 && that.options.customization.onChangeSensitive) {
                        // that.elements.buttons[1].setDisabled(true);
                    } else {
                        // that.elements.buttons[1].setDisabled(false);
                    }

                    //console.log(that.modifiedValues);

                }

                that.onValueChange(name, value);

            };

        },


        /**
        * @private
        * @summary Get onChange event for all inputs (except autocomplete)
        */
        getOnChangeEventCallback: function () {

            let eventFunction = this.getEventFunction();

            return function (event) {
                eventFunction(this.options.infos.Name, event.target.value);
            };

        },


        /**
        * Field created for a characteristic
        * @private
        * @typedef {Object} Field
        * @property {String}   name        - HTML attribute 'name' on input
        * @property {String}   value       - Input value
        * @property {String}   label       - Input label
        * @property {String}   placeholder - Input placeholder
        * @property {Boolean}  disabled    - Indicate if the input has to be in read-only mode or not
        * @property {String}   type        - Input type (according to UIKit input types)
        * @property {Object}   events      - Events on the created input
        */

        /**
         * @private
         * @summarize Return a field according to characteristic information
         * @param   {Object} infos - Information of a given characteristic
         * @returns {Field}
         */
        generateField: function (infos) {

            let field = {};

            // Keep characteristic infos to display constraints later on
            field.infos = infos;

            field.className = 'characteristics-view-input';
            field.disabled = this.isCharacteristicReadOnly(infos);
            field.name = infos.Name;
            field.label = (infos.Label) ? infos.Label : infos.Name;
            field.placeholder = field.label;

            // Defined as autocomplete ?
            // Resource ?
            // List ?
            // Vector ?
            if (Object.keys(this.options.customization.search).includes(infos.Name)
                || ['resource', 'list', 'vector', 'searcher'].includes(infos.UIType)
                || infos.IsList
            ) {
                field.placeholder = '';
                Object.assign(field, this.generateAutocompleteField(infos));

            // Has authorized values ?
            } else if (infos.AuthorizedValues || infos.AuthorizedValuesNLS) {
                Object.assign(field, this.generateSelectField(infos));

            // Number ?
            } else if (infos.UIType === 'number') {
                Object.assign(field, this.generateNumberField(infos));

            // Long string ?
            } else if (infos.UIType === 'string' && infos.LongString === true) {
                Object.assign(field, this.generateTextareaField(infos));

            // Date ?
            } else if (infos.UIType === 'date' || infos.UIType === 'dateTime') {
                Object.assign(field, this.generateDateField(infos));
                if (infos.UIType === 'dateTime')
                    field.className += ' dateTime-date-field';

            // Time ?
            } else if (infos.UIType === 'time') {
                Object.assign(field, this.generateTimeField(infos));

            // Boolean ?
            } else if (infos.UIType === 'boolean') {
                Object.assign(field, this.generateCheckboxField(infos));

            // All other cases
            } else {
                Object.assign(field, this.generateTextField(infos));
            }

            return field;

        },


        /**
         * Characteristic infos needed to create a unit button
         * @private
         * @typedef {Object} UnitButtonInfos
         * @param {String}  Name             - URI of the characteristic
         * @param {Number}  Value            - Number value
         * @param {String}  Type             - Quantity associated to the current unit
         * @param {Object}  Unit             - Unit oject
         * @param {String}  Unit.uri         - URI of the unit
         * @param {String}  Unit.symbol      - Symbol of the unit
         */

        /**
         * @private
         * @summary Create a button for a specific input to :
         *      - View the current unit
         *      - Be able to change the current unit by displaying a modal (to convert the value in a new unit)
         * @param {DS/UIKIT/Input}  input               - UIKIT input
         * @param {UnitButtonInfos} characteristicInfos - Characteristic information needed to create the button
         * @param {String}          symbol              - Symbol to display in the button
         */
        createUWAUnitButton: function (input, characteristicInfos, symbol) {

            let that = this;
            let symbolBeforeHover = symbol;

            return new UWA.Element('button', {
                class: 'characteristics-view-units-button btn btn-default',
                type: 'button',
                content: symbol,
                events: {
                    mouseenter: function (event) {
                        if (!input.isDisabled()) {
                            symbolBeforeHover = this.getText();
                            this.setHTML('<i class="fonticon fonticon-pencil"></i>');
                        }
                    },
                    mouseleave: function (event) {
                        if (!input.isDisabled()) {
                            this.setHTML(symbolBeforeHover);
                        }
                    },
                    click: function (event) {
                        let thatButton = this;
                        that.currentValues[characteristicInfos.Name] = input.getValue();

                        let footer = new UWA.Element('div');

                        let unitConversionView = new UnitConversionView({
                            rdfServerURL: that.options.rdfServerURL,
                            originalValue: Number(that.currentValues[characteristicInfos.Name]),
                            quantityURI: characteristicInfos.Type,
                            unitURI: that.currentUnits[characteristicInfos.Name],
                            prefixURI: that.currentPrefixes[characteristicInfos.Name]
                        });
                        that.elements.unitConversionView = unitConversionView;

                        let modal = new Modal({
                            renderTo: that.container,
                            header: '<h4>' + NLS.get('CV.units.modal.title') + ' - ' + input.options.label + '</h4>',
                            footer: footer,
                            body: unitConversionView
                        }).show();

                        (new UWA.Element('button', {
                            type: 'button',
                            class: 'btn btn-primary',
                            content: NLS.get('CV.units.modal.buttons.ok'),
                            events: {
                                click: function () {

                                    // Keep current information
                                    let convertedValue = unitConversionView.getValues().converted;
                                    that.currentUnits[characteristicInfos.Name] = convertedValue.unitURI;
                                    that.currentPrefixes[characteristicInfos.Name] = convertedValue.prefixURI;
                                    that.currentValues[characteristicInfos.Name] = convertedValue.value;
                                    thatButton.setContent(convertedValue.symbol);

                                    // Save preferences
                                    if (that.hasPreferences) {
                                        that.unitsPreferences.setPreferences(characteristicInfos.Name, convertedValue.unitURI, convertedValue.prefixURI);
                                    }

                                    // Calculate value to original unit
                                    console.log(convertedValue);
                                    UnitsCacheInitialization.getUnitsCache(that.options.rdfServerURL).convertValue(characteristicInfos.Type, that.currentUnits[characteristicInfos.Name], that.currentPrefixes[characteristicInfos.Name], that.originalUnits[characteristicInfos.Name], that.originalPrefixes[characteristicInfos.Name], convertedValue.value).then(function (valueToOriginalUnit) {
                                        input.valueToOriginalUnit = valueToOriginalUnit;
                                        input.setValue(convertedValue.value);
                                        delete that.elements.unitConversionView;
                                        modal.destroy();
                                    });
                                }
                            }
                        })).inject(footer);

                        (new UWA.Element('button', {
                            type: 'button',
                            class: 'btn btn-default',
                            content: NLS.get('CV.units.modal.buttons.cancel'),
                            events: {
                                click: function () {
                                    delete that.elements.unitConversionView;
                                    modal.destroy();
                                }
                            }
                        })).inject(footer);

                    }
                }
            });

        },

        updateNLSInputs: function () {
            this.updatePictogramsforNLSInputs();
            this.updateValuesForNLSInputs();
        },

        updatePictogramsforNLSInputs: function () {

            let that = this;

            this.elements.listCharacteristics.getFields().forEach(function (field) {

                let input = that.getInputFromFieldName(field.name);

                if (input) {
                    let infos = input.options.infos;
                    let inputContent = input.getContent();
                    let parentDiv = inputContent.getParent();

                    if (infos.IsNLS) {

                        // Add pictogram
                        parentDiv.addClassName('form-group-nls');
                        inputContent.addClassName('characteristics-view-input-nls');
                        new UWA.Element('div', {
                            class: 'input-group input-group-nls',
                            html: [
                                input,
                                {
                                    tag: 'span',
                                    class: 'input-group-addon',
                                    html: [
                                        {
                                            tag: 'span',
                                            class: 'fonticon fonticon-language'
                                        }
                                    ],
                                    events: {
                                        click: function () {
                                            input.getContent().focus();
                                        }
                                    }
                                },
                            ]
                        }).inject(parentDiv);

                    }

                }
            });

        },

        updateValuesForNLSInputs: function () {

            let that = this;

            this.elements.listCharacteristics.getFields().forEach(function (field) {

                let input = that.getInputFromFieldName(field.name);

                if (input) {
                    let infos = input.options.infos;
                    let inputContent = input.getContent();

                    if (infos.IsNLS) {
                        // Check if placeholder is needed (no value for the current language)
                        if (infos.Language !== that.currentLanguage) {
                            input.setValue('');
                            inputContent.setAttribute('placeholder', infos.Value);
                        }
                    }

                }

            });

        },


        hasNLSValuesForCurrentLanguage: function (fields) {

            let that = this;

            return fields.some(function (f) {
                return ((f.infos.IsNLS) && (f.infos.Language === that.currentLanguage));
            });

        },


        /**
         * @private
         * @summary Create unit button for a characteristic having a defined unit
         *          If no unit is defined on the characteristic, no button is created
         * @param {DS/UIKIT/Input} input - UIKIT input
         */
        createUnitButton: function (input) {

            let that = this;

            let infos = input.options.infos;
            let hasUniqueUnit = false;
            let hasMultipleUnits = false;
            let unitURI, prefixURI, symbol;

            // Preferences from cookie ?
            if ((this.hasPreferences) && (this.unitsPreferences.hasPreference(infos.Name))) {
                let prefs = that.unitsPreferences.getPreferences(infos.Name);
                unitURI = prefs.unitURI;
                prefixURI = prefs.prefixURI;
                hasUniqueUnit = true;
            } else {

                hasUniqueUnit = ((infos.Unit != undefined) && (infos.Unit.uri != undefined) && (infos.Unit.symbol != undefined));
                hasMultipleUnits = ((infos.Units != undefined) && (infos.Units.length > 0));

                if (hasUniqueUnit) {
                    unitURI = infos.Unit.uri;
                    symbol = infos.Unit.symbol;
                } else if (hasMultipleUnits) {
                    unitURI = infos.Units[0].uri;
                    symbol = infos.Units[0].symbol;
                }

            }


            if (hasUniqueUnit || hasMultipleUnits) {

                return UnitsCacheInitialization.getUnitsCache(that.options.rdfServerURL).getUnitSymbol(infos.Type, unitURI).then(function (unitSymbol) {
                    return UnitsCacheInitialization.getUnitsCache(that.options.rdfServerURL).getPrefixSymbol(prefixURI).then(function(prefixSymbol) {
                        that.originalUnits[infos.Name] = unitURI;
                        that.currentUnits[infos.Name] = unitURI;
                        that.originalPrefixes[infos.Name] = prefixURI;
                        that.currentPrefixes[infos.Name] = prefixURI;
                        input.valueToOriginalUnit = input.options.infos.Value;

                        let symbolForButton = '';
                        if (prefixSymbol) {
                            symbolForButton += prefixSymbol + unitSymbol;
                        } else {
                            symbolForButton += unitSymbol;
                        }

                        let unitButton = that.createUWAUnitButton(input, infos, symbolForButton);
                        unitButton.inject(input.elements.input, 'after');
                    });
                });

            }

        },


        /**
         * @private
         * @summary Add a time field to the existing date field for all 'dateTime' characteristics
         *
         * @param {DS/UIKIT/Input}  input - UIKIT input
         */
        completeDateTimeField: function (input) {

            let that = this;
            let infos = input.options.infos;

            if (infos.UIType === 'dateTime') {
                var timeValue;
                if (infos.Value !== undefined && infos.Value !== null) {
                    timeValue = infos.Value.split('T')[1];
                }

                let options = that.generateTimeField({Value: timeValue});
                options.className = 'characteristics-view-input dateTime-time-field';
                options.disabled  = that.isCharacteristicReadOnly(infos);

                let timeField = new TextInput(options);
                timeField.inject(input.elements.input, 'after');

                // keep a reference to the Time field here
                input.options.timeField = timeField;
            }

        },


        /**
         * @private
         * @summary Create unit buttons for characteristics having a defined unit
         */
        completeSpecialFields: function () {

            let that = this;
            let promises = [];

            // Loop over all the inputs
            this.elements.listCharacteristics.getFields().forEach(function (field) {
                let input = that.getInputFromFieldName(field.name);
                if (input) {
                    that.createUnitButton(input);
                    that.completeDateTimeField(input);
                }
            });

        },


        /**
         * @private
         * @summary Modify inputs values for those having units preferences
         */
        updateValuesFromUnitsPreferences: function() {

            let that = this;

            // Loop over all the inputs
            this.elements.listCharacteristics.getFields().forEach(function(field) {

                if ((that.hasPreferences) && (that.unitsPreferences.hasPreference(field.name))) {

                    let targetUnitURI = that.unitsPreferences.getUnitPreference(field.name);
                    let targetPrefixURI = that.unitsPreferences.getPrefixPreference(field.name);
                    let input = that.getInputFromFieldName(field.name);

                    if ((input) && ((targetUnitURI !== input.options.infos.Unit.uri) || (targetPrefixURI !== undefined))) {
                        UnitsCacheInitialization.getUnitsCache(that.options.rdfServerURL).convertValue(
                            input.options.infos.Type,
                            input.options.infos.Unit.uri,
                            undefined,
                            targetUnitURI,
                            targetPrefixURI,
                            input.options.infos.Value
                        ).then(function (convertedValue) {
                            input.setValue(String(convertedValue));
                        });
                    }

                }
            });

        },


        /**
        * @private
        * @summary Order characteristics according to 'orderlist' customization parameter
        * @param
        */
        orderCharacteristics: function (characteristics) {

            let orderList = this.options.customization.filters.orderlist;
            let i = 0;

            orderList.forEach(function (uri) {

                // Get original index
                let originalIndex = characteristics.findIndex(function (c) {
                    return (c.attr === uri);
                });

                // Swap positions
                if ((originalIndex !== -1) && (i <= characteristics.length - 1)) {
                    let tmp = characteristics[originalIndex];
                    characteristics[originalIndex] = characteristics[i];
                    characteristics[i] = tmp;
                    i++;
                }

            });

            return characteristics;

        },


        /**
        * @private
        * @summary Return a boolean to indicate if a characteristic has to be displayed or not
        * @param {Object}  characteristicInfos - Characteristic information to take into account
        */
        isCharacteristicDisplayable: function (characteristicInfos) {
            return (this.options.customization.display.characteristics.readOnly || !this.isCharacteristicReadOnly(characteristicInfos));
        },


        /**
        * @private
        * @summary Return a boolean to indicate if a characteristic is read-only or not
        * @param {Object}  characteristicInfos - Characeristic information to take into account
        */
        isCharacteristicReadOnly: function (characteristicInfos) {
            return ((characteristicInfos.ReadOnly && !Object.keys(this.options.customization.search).includes(characteristicInfos.Name) && !this.options.customization.forceEdit.includes(characteristicInfos.Name))
                || !this.options.customization.editMode);
        },


        /**
        * @private
        * @summary Get characteristics information for the given resource
        */
        getCharacteristicsInfos: function (toFetch) {

            if (!toFetch) {
                toFetch = ['Name', 'ReadOnly', 'Comment', 'Value', 'AuthorizedValues', 'AuthorizedValuesNLS', 'IsList', 'Type', 'Unit', 'User', 'MinMax', 'Label', 'LongString', 'IsNLS', 'Language' ];
            }

            if (this.options.customization.getInfos !== undefined) {
                return this.options.customization.getInfos(this.currentLanguage);
            } else {

                // Prepare data to send through POST request
                let rscURI = this.options.resourceURI;
                let charNames = (this.options.customization.filters.whitelist.length > 0) ? this.options.customization.filters.whitelist : ['*'];
                let blacklist = this.options.customization.filters.blacklist;
                let format = 'DISPLAY';

                return this.characteristicServices.getInfos({
                    rscURIs: rscURI,
                    charNames: charNames,
                    toFetch: toFetch,
                    blacklist: blacklist,
                    format: format,
                    cbObject: {
                        onComplete: function (data, status) {
                            console.log(data);
                        },
                        onFailure: function (data, status, error) { }
                    }
                });

            }

        },


        /**
         * @private
         * @summary Get a characteristic value thanks to the associated input
         * @param {DS/UIKIT/Input}  input       - Input associated to the characteristic
         * @param {String}          key         - Characteristic name
         * @param {Object}          formValues  - Form values (Map name => value)
         */
        getFormValue: function (input, key, formValues) {

            let that = this;
            let value;

            // Apply some specific transformations according to characteristic infos
            // List ?
            if (input.options.infos.IsList === true) {

                value = formValues[key].split(";");
                if ((value.length === 1) && (value[0] === '')) {
                    value = undefined;
                } else {
                    for (let i = 0; i < value.length; i++) {
                        value[i] = (!isNaN(value[i])) ? Number(value[i]) : value[i];
                    }
                }

                return value;

            // Date ?
            } else if (input.options.infos.UIType === 'date') {
                var date = input.getDate(); // backup not correctly returned for 'date' fields

                // OB4 : issues with time zones ! from now on, consider date as it is, without UTC transformation
                var year  = date.getFullYear();
                var month = date.getMonth()+1; // january = 0
                if (month < 10) { month = '0'+month; }
                var day   = date.getDate();
                if (day < 10) { day = '0'+day; }
                value = year+'-'+month+'-'+day;

                // value = date.toISOString();
                // value = value.slice(0, value.indexOf('T')) + 'Z'; // remove time

                return value;

            // Time ?
            } else if (input.options.infos.UIType === 'time') {

                value = formValues[key] + 'Z';

                return value;

            // DateTime ?
            } else if (input.options.infos.UIType === 'dateTime') {

                let timeField = input.options.timeField;

                var date = input.getDate(); // backup not correctly returned for 'date' fields
                value = date.toISOString();
                value = value.slice(0, value.indexOf('T')); // remove time retrieved from calendar Date
                value += 'T' + timeField.getValue() + 'Z'; // add time retrieved from Time field

                return value;

            // Number ?
            } else if (input.options.infos.UIType === 'number') {
                value = Number(formValues[key]);

                // Convert value if unit has been changed
                if ((that.originalUnits[key] !== that.currentUnits[key]) || (that.originalPrefixes[key] !== that.currentPrefixes[key])) {
                    return input.valueToOriginalUnit;
                } else {
                    return value;
                }

            } else {

                value = formValues[key];

                /**** TO FIX IN VALUE.JS !! ***/
                /**/ if (value === '') {    /**/
                /**/    value += ' ';       /**/
                /**/ }                      /**/
                /******************************/

                return value;
            }

        },


        /******************************************* PUBLIC METHODS *******************************************/
        getCurrentLanguage: function() {

            if (this.options.customization.display.switchLanguage && this.elements.iconsBar) {
                return this.elements.iconsBar.getItem(NLS.get('CV.languages.icon.text')).elements.icon.getText().toLowerCase();
            }

            return widget.lang;

        },


        /**
         * @summary Get modified form values for non-overrided characteristics
         * @return {Promise} Promise with all the values
         */
        getFormValues: function () {

            let that = this;
            let formValues = this.elements.listCharacteristics.getValues();
            let values = {};

            Object.keys(formValues).forEach(function (key) {

                // Check if characteristic has been modified -OR- check if we ignore 'modify'
                if ((that.modifiedValues.includes(key)) || (!that.options.customization.onChangeSensitive)) {

                    let input = that.getInputFromFieldName(key);
                    let value = that.getFormValue(input, key, formValues)

                    values[key] = {
                        value: value,
                        readOnly: that.isCharacteristicReadOnly(input.options.infos)
                    };

                }

            });

            return values;

        },


        /**
         * @summary Backup characteristics values
         */
        backup: function () {

            let that = this;

            if (this.elements.listCharacteristics) {
                this.backupFormValues = this.elements.listCharacteristics.getValues();

                Object.keys(this.backupFormValues).forEach(function (key) {

                    let input = that.getInputFromFieldName(key);
                    // backup not correctly returned for 'date' fields
                    if (input.options.type === 'date') {
                        let date = input.getDate();

                        // OB4 : issues with time zones ! from now on, consider date as it is, without UTC transformation
                        var year  = date.getFullYear();
                        var month = date.getMonth()+1; // january = 0
                        if (month < 10) { month = '0'+month; }
                        var day   = date.getDate();
                        if (day < 10) { day = '0'+day; }
                        let newValue = year+'-'+month+'-'+day;

                        that.backupFormValues[key] = newValue;
                    }
                    // for 'dateTime' fields, also backup the time value
                    if (input.options.infos.UIType === 'dateTime') {

                        let timeField = input.options.timeField;
                        var time = timeField.getValue().split(':');

                        let newValue = new Date(that.backupFormValues[key]);
                        newValue.setHours(time[0], time[1], time[2]);

                        that.backupFormValues[key] = newValue.toISOString();
                    }

                });
            }

            return this.backupFormValues;

        },


        /**
         * @summary Rollback to a previous state (when backup has been called for the last time)
         */
        rollback: function() {

            let that = this;
            Object.keys(this.backupFormValues).forEach(function (key) {

                // Get field & input for each characteristic
                let field = that.elements.listCharacteristics.getField(key);
                let input = that.getInputFromFieldName(key);

                // Check if input is disabled (if true, value not changed)
                if (!input.isDisabled()) {

                    if (field.className === 'autocomplete-input') {

                        // Save previous dataset to keep labels
                        let previousDataset = input.getDataset('dataset');

                        // Reset
                        input.reset();

                        // Regenerate list as it was previously
                        let options = {
                            name: 'list',
                            items: []
                        };
                        that.backupFormValues[key].split(';').forEach(function (v) {
                            options.items.push({
                                value: v,
                                label: previousDataset.items.find(function (item) {
                                    return (item.value === v);
                                }).label,
                                selected: true
                            });
                        });
                        input.addDataset(options);

                    } else if (field.type === 'select-one') {
                        input.setValue(that.backupFormValues[key]);
                    } else if (input.options.infos.UIType === 'dateTime') {
                        let timeField = input.options.timeField;

                        var dateValue = new Date(that.backupFormValues[key]);
                        // restore Date field
                        input.setDate(dateValue);

                        // restore Time field
                        let timeValue = dateValue.getHours()+':'+dateValue.getMinutes()+':'+dateValue.getSeconds();
                        timeField.setValue(timeValue);

                    } else if (input.options.type === 'date') {
                        input.setDate(that.backupFormValues[key]);
                    } else if (input.options.type === 'autocomplete') {
                        that.backupFormValues[key].split(';').forEach(function(s) {
                            let item = input.getDataset('dataset').items.find(function(i) {
                                return (i.value === s);
                            });
                            if (item) {
                                input.onSelect(item);
                            }
                        });
                    } else {
                        input.setValue(that.backupFormValues[key]);
                    }

                }
            });

            this.backupFormValues = this.backup();
            this.modifiedValues = [];
            return this.backupFormValues;

        },


        /**
        * @summary Get UIKit input from field name
        * @param {String} name - Field name
        * @return {DS/UIKIT/Input} UWA/Element corresponding to the input (if the field name exists)
        */
        getInputFromFieldName: function (name) {

            let field = this.elements.listCharacteristics.getField(name);
            if (field) {

                let input = this.elements.listCharacteristics.getInput(field.name);

                if (!input) {
                    input = this.elements.listCharacteristics.getNumberInput(field.name);
                }

                return input;

            }

            return undefined;

        },


        /**
        * @summary Get UIKit input from field name
        * @param {String} name - Field name
        * @return {DS/UIKIT/Input} UWA/Element corresponding to the input (if the field name exists)
        */
        getInputFromFieldName: function (name) {

            let field = this.elements.listCharacteristics.getField(name);
            if (field) {

                let input = this.elements.listCharacteristics.getInput(field.name);

                if (!input) {
                    input = this.elements.listCharacteristics.getNumberInput(field.name);
                }

                return input;

            }

            return undefined;

        },


        /**
         * @summary Raise event when a value changed for a given input
         * @param {String} inputName    - Name of the input as stored in the form
         * @param {String} value        - Nexw value associated to that input
         */
        onValueChange: function (inputName, value) {
            this.options.events.onValueChange(inputName, value);
        },


        /**
         * @summary Switch mode for the list of characteristics
         * @param {Boolean} editMode - Indicate if component should be in edit mode or not
         */
        switchMode: function (editMode) {

            if (editMode === undefined) {
                editMode = true;
            }

            let that = this;

            // Check if "switchMode" icon exists
            if ((this.options.customization.display.switchMode) && (this.options.customization.editMode !== editMode)) {

                // Get "switchMode" menu icon
                let currentText = (!editMode) ? NLS.get('CV.mode.icon.text.switchReadOnly') : NLS.get('CV.mode.icon.text.switchEdit');
                let item = this.elements.iconsBar.getItem(currentText);
                let tooltip = item.tooltip;
                tooltip.hide();

                // Modify icon
                this.iconFunction();

                // Modify popup text
                let tooltipBody = tooltip.elements.body;
                let newText = (editMode) ? NLS.get('CV.mode.icon.text.switchReadOnly') : NLS.get('CV.mode.icon.text.switchEdit');
                tooltipBody.textContent = newText;
                item.name = newText;
                item.text = newText;

                if (that.options.fromClick) {
                    tooltip.show();
                }
                that.options.fromClick = false;

            }

            // Save current mode
            this.options.customization.editMode = editMode;

            // Loop over all inputs to change their edit mode
            this.elements.listCharacteristics.getFields().forEach(function (field) {
                let input = that.getInputFromFieldName(field.name);
                if (input) {
                    input.setDisabled(that.isCharacteristicReadOnly(input.options.infos));

                    if (input.options.infos.UIType === 'dateTime') { // also modify disable state for time field
                        input.options.timeField.setDisabled(that.isCharacteristicReadOnly(input.options.infos));
                    }
                }
            });

            // Raise "onSwitchMode" event
            this.options.events.onSwitchMode(editMode);

        },

        destroy: function() {
            this._parent();
        }

    });

    return CharacteristicsView;

});
