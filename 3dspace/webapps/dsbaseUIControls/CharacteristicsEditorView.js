define('DS/dsbaseUIControls/CharacteristicsEditorView', [
    'DS/dsbaseUIControls/CharacteristicsEditorViewOptionsChecker',
    'DS/dsbaseUIControls/CharacteristicsView',
    'DS/dsbaseUIControls/UserCharacteristicsView',
    'DS/dsbaseUIServices/OperationServices',
    'DS/dsbaseUIServices/CharacteristicServices',
    'UWA/Core',
    'UWA/Class/View',
    'DS/UIKIT/Spinner',
    'DS/UIKIT/Input/Button',
    'DS/UIKIT/Accordion',
    'i18n!DS/dsbaseUIControls/assets/nls/CharacteristicsEditorView',
    'css!DS/UIKIT/UIKIT.css',
    'css!DS/dsbaseUIControls/assets/style/CharacteristicsEditorView.css'
], function (
    CharacteristicsEditorViewOptionsChecker,
    CharacteristicsView,
    UserCharacteristicsView,
    OperationServices,
    CharacteristicServices,
    UWA,
    View,
    Spinner,
    Button,
    Accordion,
    NLS
) {

    'use strict';


    /**
    * @summary Generic view to edit characteristics of a given resource
    * @extends UWA/Class/View
    */
    let CharacteristicsEditorView = View.extend({

        tagName: 'div',


        /**
         * @private
         * @summary Add complementary class names to the characteristics editor view component
         */
        className: function () {

            let className = 'characteristics-editor-view';

            if (this.options.customization) {

                // Verify if other CSS class names are defined
                if (this.options.customization.classNames !== undefined) {
                    this.options.customization.classNames.forEach(function (cn) {
                        className += ' ' + cn;
                    });
                }

            }

            return className;

        },


        /****** BUTTON CLICK ******/
        /**
         * Modified characteristic returned by the view on button click
         * @typedef     {Object} ModifiedCharacteristic
         * @property    {String|Number|String[]|Number[]}  value       - New value of the characteristic (after modification)
         * @property    {Boolean}                          readOnly    - Boolean to indicate if the characteristic is read-only or not
         */

        /**
         * Callback for a button click
         * @callback    Click
         * @param       {String}                                  rootURL           - Root server URL (as given through the view constructor)
         * @param       {String}                                  resourceURI       - Resource URI (as given through the view constructor)
         * @param       {Object.<String, ModifiedCharacteristic>} formValues        - Object with all modified characteristics (that have non-specific function)
         * @param       {String}                                  formValues.*      - URI of the modified characteristic
         */


        /****** SPECIFIC ACTION FOR A GIVEN CHARACTERISTIC ******/
        /**
         * Callback for a specific action linked to a characteristic on button click
         * @callback    SpecificAction
         * @param       {String}    value - Characteristic value
         */


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
         * @property {SearchEngine}     autocomplete                             - Autcomplete search function called when user starts writing in corresponding input (show suggestions)
         * @property {Function}         [options.customization.onSuggest=]       - Function called when an item is shown in the list of suggestions (has to return a UWA/Element to render the associated item in the list of suggestions)
         * @property {Function}         [options.customization.onSelect=]        - Function called when an item is selected in the list of suggestions (has to return a string to render the associated badge)
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
         * @property {String}   [infos.Type]                - Long URI of the RDF characteristic type
         *
         * @property {Object}   [infos.Unit]                - Information about the unit associated to the characteristic
         * @property {String}   infos.Unit.uri              - Short URI of the unit
         * @property {String}   infos.Unit.symbol           - Symbol associated to the unit (examples : 'kg', 'm3', 'cm' ...)
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
         * @param {Object}                          [options.customization]                                             - Object to define customization parameters to render the view
         * @param {String}                          [options.customization.preferenceId]                                - Editor id (used to retrieve preferences - units - from previous utilization)
         * @param {String[]}                        [options.customization.classNames=[]]                               - Array of additionnal CSS class names
         * @param {String}                          [options.customization.title="Characteristics Editor"]              - Title displayed at the top of the view
         * @param {Boolean}                         [options.customization.editMode=true]                               - Boolean to indicate if inputs should be disabled or not (true => edit, false => read)
         *
         * @param {Object}                          [options.customization.languages]                                   - Languages options
         * @param {Array.<String>}                  [options.customization.languages.supported=<platform_languages>]    - Array of languages proposed to the user (i.e. array of RDF languages codes)
         * @param {String}                          [options.customization.languages.selected="en"]                     - Language selected on view construction
         *
         * @param {GetInfos}                        [options.customization.getInfos]                                    - Function to use to retrieve characteristic infos (instead of default 'dsbase:characteristic.getInfos' webservice)
         *
         * @param {Object}                          options.customization.display                                       - Object to define display settings (i.e. some components should be displayed or not ?)
         * @param {Boolean}                         [options.customization.display.title=true]                          - Boolean to indicate if title should be displayed at the top of the view
         * @param {Boolean}                         [options.customization.display.switchMode=true]                     - Boolean to indicate if "switch mode" button should be displayed or not
         * @param {Boolean}                         [options.customization.display.switchLanguage=true]                 - Boolean to indicate if "switch language" button should be displayed or not
         * @param {Boolean}                         [options.customization.display.characteristics.readOnly=true]       - Boolean to indicate if read-only characteristics should be displayed or not
         * @param {Boolean}                         [options.customization.display.characteristics.constraints=true]    - Boolean to indicate if characteristics constraints within a popover should be displayed or not
         * @param {Boolean}                         [options.customization.display.characteristics.user=true]           - Boolean to indicate if User Characteristics View should be displayed or not
         * @param {Boolean}                         [options.customization.display.scroller=true]                       - Boolean to indicate if scroll view should be used
         * @param {Boolean}                         [options.customization.display.buttons=true]                        - Boolean to indicate if buttons should be displayed at the bottom of the view
         *
         * @param {String}                          [options.customization.constraintsPosition="top"]                   - Position of the constraints popup relative to the 'information' icon (i). It can be one of these : 'top', 'right', 'bottom' or 'left'
         *
         * @param {Object}                          options.customization.buttons                                       - Object to define the wanted buttons at the bottom of the view. By default there are two buttons with predefined actions :
         *                                                                                                                      'Ok' button       : Set characteristics of the resource with the specified values
         *                                                                                                                      'Cancel' button
         * @param {Object}                          options.customization.buttons.cancel                                - Object to define/override 'Cancel' button, displayed in the footer
         * @param {String}                          [options.customization.buttons.cancel.label="Cancel"]               - Label of the 'Cancel' button
         * @param {String}                          [options.customization.buttons.cancel.type="default"]               - UIKit type of the 'Cancel' button. It can be one of these : 'primary', 'default', 'success', 'info', 'warning' or 'error'
         * @param {Click}                           options.customization.buttons.cancel.defaultAction                  - Function that handles the click on 'Cancel' button
         * @param {Object}                          options.customization.buttons.ok                                    - Object to define/override 'Ok' button, displayed in the footer
         * @param {String}                          [options.customization.buttons.ok.label="Ok"]                       - Label of the 'Ok' button
         * @param {String}                          [options.customization.buttons.ok.type="primary"]                   - UIKit type of the 'Ok' button. It can be one of these : 'primary', 'default', 'success', 'info', 'warning' or 'error'
         * @param {Click}                           [options.customization.buttons.ok.defaultAction=<Set>]              - Function that handles the click on 'Ok' button
         * @param {Object.<string, SpecificAction>} [options.customization.buttons.ok.specificActions={}]               - Object to define specific actions for some characteristics when clicking on 'Ok' button (only available for Characteristics View)
         *                                                                                                                      Key     : long URI of the characteristic
         *                                                                                                                      Value   : action function defined for the characteristic given as key
         * @param {String}                          [options.customization.buttons.position="right"]                    - Position of the two buttons in the footer. It can be one of these : 'left' or 'right'
         *
         * @param {Object.<string, Object>}         [options.customization.search={}]                                   - Object to define wanted autocomplete inputs (characteristic URI as key, object with funcitons as value)
         * @param {SearchOption}                    options.customization.search[URI].autocomplete                      - Autcomplete search function
         * @param {Function}                        [options.customization.search[URI].onSuggest]                       - Function to construct UI when an item is suggested (according to user entry)
         * @param {Function}                        [options.customization.search[URI].onSelect]                        - Function to construct UI when an item is selected
         * @param {Function}                        [options.customization.search[URI].onCleanDataset]                  - Callback called when the input is cleaned
         *
         * @param {Object}                          options.customization.filters                                       - Object to define filters (filters are taken into account in this order: 1. whitelist  2. blacklist  3. order )
         * @param {String[]}                        [options.customization.filters.whitelist=[]]                        - Array of URIs to retrieve specific characteristics
         * @param {String[]}                        [options.customization.filters.blacklist=[]]                        - Array of URIs to avoid retrieving some characteristics
         * @param {String[]}                        [options.customization.filters.orderlist=["http://www.3ds.com/RDF/Corpus/Kernel/nlsLabel","http://www.3ds.com/RDF/Corpus/Kernel/nlsComment","http://www.3ds.com/RDF/Corpus/Kernel/owner']] - Array of URIs to order retrieved characteristics, according to the index in the array
         *
         * @param {Boolean}                         [options.customization.onChangeSensitive=true]                      - Boolean to indicate if only modified characteristics should be taken into account
         *
         * @param {Object}                          options.events                                                      - Object for events callbacks
         * @param {Function}                        [options.events.onFinishRender]                                     - Function called when the characteristics editor view is rendered
         * @param {Function}                        [options.events.onSelectLanguage]                                   - Callback called when a language is selected in the dropdown
         *
         * @constructs CharacteristicsEditorView
         * @summary Setup component
         * @memberof module:DS/dsbaseUIControls/CharacteristicsEditorView
         */
        setup: function (options) {

            // Checking
            (new CharacteristicsEditorViewOptionsChecker(options)).checkAllOptions();
            this.options = options;

            // Services
            if (this.options.customization.languages) {
                this.characteristicServices = CharacteristicServices.getHandle(this.options.rdfServerURL, this.options.customization.languages.selected);
                this.operationServices = OperationServices.getHandle(this.options.rdfServerURL, this.options.customization.languages.selected);
            } else {
                this.characteristicServices = CharacteristicServices.getHandle(this.options.rdfServerURL);
                this.operationServices = OperationServices.getHandle(this.options.rdfServerURL);
            }

            // Data
            this.data = {
                characteristicsView: {
                    loaded: [],
                    changed: {}
                },
                userCharacteristicsView: {
                    loaded: [],
                    added: {},
                    removed: [],
                    modified: {}
                }
            };
            this.backupCV = {};
            this.backupUCV = {};
            this.finishRenderCV = false;
            this.finishRenderUCV = false;

            // Load characteristics and user characteristics at the same time
            // Differenciate them afterwards
            let that = this;
            //console.log(this);
            this.launchSpinnerOnLoad();
            this.getCharacteristicsInfos().then(function (allCharacteristics) {

                that.differenciateCharacteristics(allCharacteristics);

                // Building top part (with characteristics)
                that.createTopTitle();
                that.createCharacteristicsView(false);

                // Building bottom part (with user characteristics)
                if (that.options.customization.display.characteristics.user) {
                    that.createSeparator();
                    that.createBottomTitle();
                    that.createUserCharacteristicsView();
                }

                // Building footer with buttons
                that.createFooter();

                that.render();

            });

        },


        /**
         * @private
         * @summary Render the complete view
         */
        render: function () {

            // Hide spinner after everything is loaded
            this.elements.spinner.hide();

            // Create an accordion to distinguish characteristics from user characteristics
            this.elements.accordion = new Accordion({
                className: 'characteristics-editor-view-accordion',
                exclusive: false,
                animated: true,
                items: []
            });

            this.renderTopPart();
            this.elements.accordion.inject(this.container);
            this.renderBottomPart();
            this.renderFooter();

            //console.log(this);
            return this;

        },


        /**
         * @private
         * @summary Render the characteristics view
         */
        renderTopPart: function () {

            // Inject title
            if (this.elements.topTitle) {
                this.elements.topTitle.inject(this.container);
            }

            this.renderCharacteristicsView();

        },


        /**
         * Render the Characteristics View
         * @param {Boolean} recreate - Is the Characteristics View reloaded ?
         */
        renderCharacteristicsView: function (recreate) {

            if (this.elements.characteristicsView) {

                // From language popup ?
                if (recreate) {

                    if (this.options.customization.display.characteristics.user) {
                        this.visibleCV = true;
                        let accordionItemContainer = this.elements.accordion.getItem('characteristicsViewItem').elements.container;
                        this.elements.characteristicsView.inject(accordionItemContainer);
                    } else {
                        this.elements.characteristicsView.inject(this.elements.footer, 'before');
                    }

                } else {

                    if (this.options.customization.display.characteristics.user) {
                        this.visibleCV = true;
                        this.elements.accordion.addItem([
                            {
                                title: '<span class="fonticon fonticon-list"></span>' + NLS.get('CEV.characteristics.title'),
                                name: 'characteristicsViewItem',
                                content: this.elements.characteristicsView,
                                selected: this.visibleCV,
                                arrows: false,
                                handler: function () {
                                    /*that.visibleCV = !that.visibleCV;
                                    if (that.visibleCV) {
                                        that.elements.characteristicsView.show();
                                    } else {
                                        that.elements.characteristicsView.hide();
                                    }*/
                                }
                            }
                        ]);
                    } else {
                        this.elements.characteristicsView.inject(this.container);
                    }

                }

            }

        },


        /**
         * @private
         * @summary Render the user characteristics view
         */
        renderBottomPart: function () {

            // Inject user-characteristics view
            if (this.elements.userCharacteristicsView) {
                this.renderUserCharacteristicsView();
            }

        },


        /**
         * Render the User Characteristics View
         * @param {Boolean} recreate - Is the Characteristics View reloaded ?
         */
        renderUserCharacteristicsView: function (recreate) {

            if (recreate) {
                let accordionItemContainer = this.elements.accordion.getItem('userCharacteristicsViewItem').elements.container;
                this.elements.userCharacteristicsView.inject(accordionItemContainer);
            } else {

                this.elements.accordion.addItem([
                    {
                        title: '<span class="fonticon fonticon-list-add"></span>' + NLS.get('CEV.userCharacteristics.title'),
                        name: 'userCharacteristicsViewItem',
                        content: this.elements.userCharacteristicsView
                    }
                ]);

            }

        },


        /**
         * @private
         * @summary Render the footer with buttons
         */
        renderFooter: function () {

            // Inject all buttons
            if (this.elements.divButtons) {

                this.elements.divButtons.inject(this.elements.footer);
                this.elements.footer.inject(this.container);

                if (!this.options.customization.display.buttons) {
                    this.elements.footer.hide();
                    let scrollerContent = this.getElement('.scroller-content');
                    if (scrollerContent) {
                        scrollerContent.addClassName('characteristics-editor-view-scroller-content-nofooter');
                    }
                }

            }

        },


        /**
         * @private
         * @summary Launch spinner loader while all characteristics are being retrieved
         */
        launchSpinnerOnLoad: function () {

            this.elements.spinner = new Spinner({
                className: 'characteristics-editor-view-spinner',
                animate: false
            }).inject(this.container)
                .show();

        },


        /**
        * @private
        * @summary Create default message error occured to retrieve all characteristics
        */
        injectDefaultMessage: function () {

            this.elements.defaultMessage = new UWA.Element('div', {
                content: NLS.get('CEV.defaultMessage'),
                class: 'characteristics-editor-view-default-message'
            });
            this.elements.defaultMessage.inject(this.container);

        },


        /**
         * @private
         * @summary Create title being displayed at the top of the view
         */
        createTopTitle: function () {

            this.elements.topTitle = new UWA.Element('h3', {
                html: this.options.customization.title,
                class: 'characteristics-editor-view-title',
                styles: {
                    'display': (this.options.customization.display.title) ? 'block' : 'none'
                }
            });

        },


        /**
         * @private
         * @summary Give the button status (Should the button be disabled ?)
         */
        shouldButtonBeDisabled: function () {
            return !((Object.keys(this.data.characteristicsView.changed).length > 0)
                || (Object.keys(this.data.userCharacteristicsView.added).length > 0)
                || (this.data.userCharacteristicsView.removed.length > 0)
                || (Object.keys(this.data.userCharacteristicsView.modified).length > 0)
                || !this.options.customization.onChangeSensitive);
        },


        /**
         * @private
         * @summary In-the-middle function to be sure to synchronize "onFinishRender" event for both subviews
         */
        executeCommonOnFinishEvent: function () {

            if ((!this.options.customization.display.characteristics.user) || ((this.finishRenderCV === true) && (this.finishRenderUCV === true))) {
                this.options.events.onFinishRender.bind(this)();
            }

        },


        /**
         * @private
         * @summary Create the list of characteristics by reusing 'CharacteristicsView' component
         */
        createCharacteristicsView: function (recreate) {

            let that = this;
            this.finishRenderCV = false;

            // Construct force edit array for specific characteristics
            let tmp = Object.keys(this.options.customization.buttons.ok.specificActions).concat(Object.keys(this.options.customization.search));
            let forceEditArray = [];
            tmp.forEach(function (item) {
                if (forceEditArray.indexOf(item) === -1) {
                    forceEditArray.push(item);
                }
            });

            let classNames = [];
            if (!that.options.customization.display.characteristics.user) {
                classNames.push('characteristics-editor-embedded-view');
            }

            let optionsCustomization = Object.assign({}, this.options.customization);
            this.elements.characteristicsView = new CharacteristicsView({
                rdfServerURL: this.options.rdfServerURL,
                resourceURI: this.options.resourceURI,
                customization: Object.assign(optionsCustomization, {
                    classNames : classNames,
                    getInfos: function () {

                        return new Promise(function (resolve, reject) {

                            // Check if there is a distinction between characteristics and user characteristics
                            if (that.options.customization.display.characteristics.user) {
                                resolve(that.data.characteristicsView.loaded);
                            } else {
                                resolve(that.data.characteristicsView.loaded.concat(that.data.userCharacteristicsView.loaded));
                            }

                        });
                    },
                    forceEdit: forceEditArray,
                    checkNLSOnConstruction: !recreate
                }),
                events: {
                    onSwitchMode: function (mode) {
                        that.options.customization.editMode = mode;
                    },
                    onValueChange: function (inputName, value) {
                        let formValues = that.elements.characteristicsView.getFormValues();
                        that.data.characteristicsView.changed = formValues;
                        that.buttons[1].setDisabled(that.shouldButtonBeDisabled());
                    },
                    onFinishRender: function () {
                        if (recreate) {
                            that.elements.characteristicsView.updateNLSInputs();
                        }
                        that.backupCharacteristicsView();
                        that.finishRenderCV = true;
                        that.executeCommonOnFinishEvent();
                    },
                    onSelectLanguage: function (language) {
                        that.options.events.onSelectLanguage(language);
                    },
                    onSaveLanguage: function (original, targeted) {
                        that.fromLanguage = original;
                        that.recreate = true;
                        return new Promise(function (resolve, reject) {
                            that.characteristicServices.setHeader('Accept-Language', original);
                            that.operationServices.setHeader('Accept-Language', original);
                            return that.executeAction(true, false).then(function () {
                                that.heightIconsbar = that.elements.characteristicsView.elements.iconsBar.elements.container.offsetHeight;
                                if (that.options.customization.display.scroller) {
                                    that.heightScroller = that.elements.characteristicsView.elements.scroller.elements.container.offsetHeight;
                                }
                                that.elements.characteristicsView.destroy();

                                let cont;
                                if (that.options.customization.display.characteristics.user) {
                                    cont = that.elements.accordion.getItem('characteristicsViewItem').elements.container;
                                    that.launchSpinnerOnViewRecreation(cont);
                                } else {
                                    cont = that.elements.topTitle;
                                    that.launchSpinnerOnViewRecreation(cont, 'after');
                                }

                                that.characteristicServices.setHeader('Accept-Language', targeted);
                                that.operationServices.setHeader('Accept-Language', targeted);
                                if (!that.options.customization.languages) {
                                    that.options.customization.languages= {
                                        selected: targeted
                                    };
                                } else {
                                    that.options.customization.languages.selected = targeted;
                                }

                                return that.getCharacteristicsInfos(['AuthorizedValues', 'AuthorizedValuesNLS', 'Type', 'Value', 'Language', 'MinMax']).then(function (characteristics) {
                                    that.updateCharacteristics(characteristics);
                                    that.createCharacteristicsView(true);
                                    that.renderCharacteristicsView(true);
                                    that.recreate = false;
                                    that.elements.spinnerReload.destroy();
                                    resolve();
                                });
                            });
                        });
                    },
                    onDiscardLanguage: function (original, targeted) {
                        that.fromLanguage = original;
                        that.recreate = true;
                        return new Promise(function (resolve, reject) {
                            that.heightIconsbar = that.elements.characteristicsView.elements.iconsBar.elements.container.offsetHeight;
                            if (that.options.customization.display.scroller) {
                                that.heightScroller = that.elements.characteristicsView.elements.scroller.elements.container.offsetHeight;
                            }
                            that.elements.characteristicsView.destroy();

                            let cont;
                            if (that.options.customization.display.characteristics.user) {
                                cont = that.elements.accordion.getItem('characteristicsViewItem').elements.container;
                                that.launchSpinnerOnViewRecreation(cont);
                            } else {
                                cont = that.elements.topTitle;
                                that.launchSpinnerOnViewRecreation(cont, 'after');
                            }

                            that.characteristicServices.setHeader('Accept-Language', targeted);
                            that.operationServices.setHeader('Accept-Language', targeted);
                            if (!that.options.customization.languages) {
                                that.options.customization.languages = {
                                    selected: targeted
                                };
                            } else {
                                that.options.customization.languages.selected = targeted;
                            }

                            return that.getCharacteristicsInfos(['AuthorizedValues', 'AuthorizedValuesNLS', 'Type', 'Value', 'Language', 'MinMax']).then(function (characteristics) {
                                that.updateCharacteristics(characteristics);
                                that.createCharacteristicsView(true);
                                that.renderCharacteristicsView(true);
                                that.recreate = false;
                                that.elements.spinnerReload.destroy();
                                resolve();
                            });
                        });
                    },
                    onChangeLanguageWithNoModification: function (original, targeted) {
                        that.fromLanguage = original;
                        that.recreate = true;
                        return new Promise(function (resolve, reject) {
                            that.heightIconsbar = that.elements.characteristicsView.elements.iconsBar.elements.container.offsetHeight;
                            if (that.options.customization.display.scroller) {
                                that.heightScroller = that.elements.characteristicsView.elements.scroller.elements.container.offsetHeight;
                            }
                            that.elements.characteristicsView.destroy();

                            let cont;
                            if (that.options.customization.display.characteristics.user) {
                                cont = that.elements.accordion.getItem('characteristicsViewItem').elements.container;
                                that.launchSpinnerOnViewRecreation(cont);
                            } else {
                                cont = that.elements.topTitle;
                                that.launchSpinnerOnViewRecreation(cont, 'after');
                            }

                            that.characteristicServices.setHeader('Accept-Language', targeted);
                            that.operationServices.setHeader('Accept-Language', targeted);
                            if (!that.options.customization.languages) {
                                that.options.customization.languages = {
                                    selected: targeted
                                };
                            } else {
                                that.options.customization.languages.selected = targeted;
                            }

                            return that.getCharacteristicsInfos(['AuthorizedValues', 'AuthorizedValuesNLS', 'Type', 'Value', 'Language', 'MinMax']).then(function (characteristics) {
                                that.updateCharacteristics(characteristics);
                                that.createCharacteristicsView(true);
                                that.renderCharacteristicsView(true);
                                that.recreate = false;
                                that.characteristicServices.setHeader('Accept-Language', targeted);
                                that.operationServices.setHeader('Accept-Language', targeted);
                                that.elements.spinnerReload.destroy();
                                resolve();
                            });
                        });
                    }
                }
            });

        },


        /**
         * @private
         * @summary Create a separator
         */
        createSeparator: function () {

            this.elements.separator = new UWA.Element('hr', {
                class: 'charactersitics-editor-view-separator'
            });

        },


        /**
         * @private
         * @summary Create the "User Characteristics" title
         */
        createBottomTitle: function () {

            this.elements.bottomTitle = new UWA.Element('h3', {
                html: NLS.get('CEV.userCharacteristics.title'),
                class: 'characteristics-editor-view-title',
                styles: {
                    'display': (this.options.customization.display.title) ? 'block' : 'none'
                }
            });

        },


        /**
         * @private
         * @summary Create the user characteristics view at the bottom of the editor
         */
        createUserCharacteristicsView: function (recreate) {

            let that = this;
            this.finishRenderUCV = false;

            let classNames = [];
            if (!that.options.customization.display.characteristics.user) {
                classNames.push('characteristics-editor-embedded-view');
            // } else {
            //     classNames.push('characteristics-editor-embedded-view-half');
            }

            let optionsCustomization = Object.assign({}, this.options.customization);
            this.elements.userCharacteristicsView = new UserCharacteristicsView({
                rdfServerURL: this.options.rdfServerURL,
                resourceURI: this.options.resourceURI,
                fromOptionSet: 'dscust:ListOfUserCharacteristics',
                customization: Object.assign(optionsCustomization, {
                    classNames : classNames,
                    getInfos: function () {
                        return new Promise(function (resolve, reject) {

                            // Check if there is a distinction between characteristics and user characteristics
                            if (that.options.customization.display.characteristics.user) {
                                resolve(that.data.userCharacteristicsView.loaded);
                            }

                        });
                    }
                }),
                events: {
                    onAddUserCharacteristic: function (name) {
                        that.data.userCharacteristicsView.added = that.elements.userCharacteristicsView.getAddedUserCharacteristics();
                        that.data.userCharacteristicsView.modified = that.elements.userCharacteristicsView.getModifiedUserCharacteristics();
                        that.data.userCharacteristicsView.removed = that.elements.userCharacteristicsView.getRemovedUserCharacteristics();
                        that.buttons[1].setDisabled(that.shouldButtonBeDisabled());
                    },
                    onRemoveUserCharacteristic: function (name) {
                        that.data.userCharacteristicsView.added = that.elements.userCharacteristicsView.getAddedUserCharacteristics();
                        that.data.userCharacteristicsView.modified = that.elements.userCharacteristicsView.getModifiedUserCharacteristics();
                        that.data.userCharacteristicsView.removed = that.elements.userCharacteristicsView.getRemovedUserCharacteristics();
                        that.buttons[1].setDisabled(that.shouldButtonBeDisabled());
                    },
                    onModifyUserCharacteristic: function (name) {
                        that.data.userCharacteristicsView.added = that.elements.userCharacteristicsView.getAddedUserCharacteristics();
                        that.data.userCharacteristicsView.modified = that.elements.userCharacteristicsView.getModifiedUserCharacteristics();
                        that.data.userCharacteristicsView.removed = that.elements.userCharacteristicsView.getRemovedUserCharacteristics();
                        that.buttons[1].setDisabled(that.shouldButtonBeDisabled());
                    },
                    onFinishRender: function () {
                        that.backupUserCharacteristicsView();
                        that.finishRenderUCV = true;
                        that.executeCommonOnFinishEvent();
                    },
                    onSaveLanguage: function (original, targeted) {
                        that.fromLanguage = original;
                        that.recreate = true;
                        return new Promise(function (resolve, reject) {
                            return that.executeAction(false, true).then(function () {
                                that.elements.userCharacteristicsView.destroy();
                                that.characteristicServices.setHeader('Accept-Language', original);
                                that.operationServices.setHeader('Accept-Language', original);
                                that.options.customization.languages.selected = targeted;

                                return that.getCharacteristicsInfos(['AuthorizedValues', 'AuthorizedValuesNLS', 'Type', 'Value', 'Language', 'MinMax']).then(function (characteristics) {
                                    that.updateCharacteristics(characteristics);
                                    that.createUserCharacteristicsView(true);
                                    that.renderUserCharacteristicsView(true);
                                    that.recreate = false;
                                    that.characteristicServices.setHeader('Accept-Language', targeted);
                                    that.operationServices.setHeader('Accept-Language', targeted);
                                    resolve();
                                });
                            });
                        });
                    },
                    onDiscardLanguage: function (original, targeted) {
                        that.fromLanguage = original;
                        that.recreate = true;
                        return new Promise(function (resolve, reject) {
                            that.elements.userCharacteristicsView.destroy();
                            that.characteristicServices.setHeader('Accept-Language', original);
                            that.operationServices.setHeader('Accept-Language', original);
                            that.options.customization.languages.selected = targeted;

                            return that.getCharacteristicsInfos(['AuthorizedValues', 'AuthorizedValuesNLS', 'Type', 'Value', 'Language', 'MinMax']).then(function (characteristics) {
                                that.updateCharacteristics(characteristics);
                                that.createUserCharacteristicsView(true);
                                that.renderUserCharacteristicsView(true);
                                that.recreate = false;
                                that.characteristicServices.setHeader('Accept-Language', targeted);
                                that.operationServices.setHeader('Accept-Language', targeted);
                                resolve();
                            });
                        });
                    },
                    onChangeLanguageWithNoModification: function (original, targeted) {
                        that.fromLanguage = original;
                        that.recreate = true;
                        return new Promise(function (resolve, reject) {
                            that.elements.userCharacteristicsView.destroy();
                            that.characteristicServices.setHeader('Accept-Language', original);
                            that.operationServices.setHeader('Accept-Language', original);
                            that.options.customization.languages.selected = targeted;

                            return that.getCharacteristicsInfos(['AuthorizedValues', 'AuthorizedValuesNLS', 'Type', 'Value', 'Language', 'MinMax']).then(function (characteristics) {
                                that.updateCharacteristics(characteristics);
                                that.createUserCharacteristicsView(true);
                                that.renderUserCharacteristicsView(true);
                                that.recreate = false;
                                that.characteristicServices.setHeader('Accept-Language', targeted);
                                that.operationServices.setHeader('Accept-Language', targeted);
                                resolve();
                            });
                        });
                    }
                }
            });

        },


        /**
         * Create a spinner when the view is reloaded (after choosing a new language)
         * @param {UWA/Element} container   - UWA/Element where the Characteristics View is stored
         * @param {String}      where       - Position of the spinner in the speicified container
         */
        launchSpinnerOnViewRecreation: function (container, where) {

            this.elements.spinnerReload = new Spinner({
                className: 'characteristics-editor-view-spinner',
                animate: false
            });

            if (where !== undefined) {
                this.elements.spinnerReload.inject(container, where).show();
            } else {
                this.elements.spinnerReload.inject(container).show();
            }

            let heightIconsbar = parseInt(this.heightIconsbar, 10);
            let heightScroller = parseInt(this.heightScroller, 10);
            let heightSpinner = parseInt(this.elements.spinnerReload.getContent().offsetHeight, 10) / 2;
            let margin = (heightIconsbar + heightScroller) / 2 - heightSpinner / 2;
            this.elements.spinnerReload.getContent().setStyles({
                'margin-top': margin + 'px',
                'margin-bottom': margin + 'px'
            });

        },


        /**
         * @private
         * @summary Generate footer
         */
        createFooter: function () {

            this.elements.footer = new UWA.Element('div', {
                class: 'modal-footer characteristics-editor-view-footer'
            });

            this.elements.divButtons = this.createButtons();

        },


        /**
         * @private
         * @summary Create 'Cancel' button
         * @param {String} rootURL      - RDF Server root URL
         * @param {String} resourceURI  - URI of the given resource
         */
        getCancelButton: function (rootURL, resourceURI) {

            let that = this;
            let cancelButton = this.options.customization.buttons.cancel;

            return new Button({
                value: cancelButton.label,
                className: cancelButton.type + ' characteristics-editor-view-button ' + this.options.customization.buttons.position,
                events: {
                    onClick: function () {

                        let thatButton = this;
                        let CVFormValues = that.elements.characteristicsView.getFormValues();
                        thatButton.setDisabled(true);

                        let formValues = {
                            characteristicsView: CVFormValues
                        };

                        if (that.options.customization.display.characteristics.user) {
                            formValues.userCharacteristicsView = {
                                added: that.elements.userCharacteristicsView.getAddedUserCharacteristics(),
                                modified: that.elements.userCharacteristicsView.getModifiedUserCharacteristics(),
                                removed: that.elements.userCharacteristicsView.getRemovedUserCharacteristics()
                            };
                        }
                        try {
                            cancelButton.defaultAction(rootURL, resourceURI, formValues);
                        } catch (error) {
                            thatButton.setDisabled(false);
                            that.raiseError(error, NLS.get('CEV.cancelButton.error'), false);
                        }

                        thatButton.setDisabled(false);

                    }
                }
            });

        },


        /**
         * @private
         * @summary Create 'Ok' button
         * @param {String} rootURL      - RDF Server root URL
         * @param {String} resourceURI  - URI of the given resource
         */
        getOkButton: function (rootURL, resourceURI) {

            let that = this;
            let okButton = this.options.customization.buttons.ok;

            return new Button({
                value: okButton.label,
                disabled: this.options.customization.onChangeSensitive,
                className: okButton.type + ' characteristics-editor-view-button ' + this.options.customization.buttons.position,
                events: {
                    onClick: function () {
                        that.executeAction();
                    }
                }
            });

        },


        /**
         * @private
         * @summary Get all added user characteristics
         */
        getUserCharacteristics: function () {
            return this.elements.userCharacteristicsView.getAddedUserCharacteristics();
        },


        /**
         * @private
         * @summary Get all modified user characteristics
         */
        getModifiedUserCharacteristics: function () {
            return this.elements.userCharacteristicsView.getModifiedUserCharacteristics();
        },


        /**
         * @private
         * @summary Get all removed user characteristics
         */
        getRemovedUserCharacteristics: function () {
            return this.elements.userCharacteristicsView.getRemovedUserCharacteristics();
        },


        /**
         * @private
         * @summary Create buttons to be injected in the footer
         */
        createButtons: function () {

            let that = this;
            let buttons = [];
            let divButtons = new UWA.Element('div', {
                class: 'characteristics-editor-view-buttons'
            });

            // Create the two buttons
            let rootURL = that.options.rdfServerURL;
            let resourceURI = that.options.resourceURI;
            let cancelButton = this.getCancelButton(rootURL, resourceURI);
            let okButton = this.getOkButton(rootURL, resourceURI);

            // Inject the two buttons
            buttons.push(cancelButton);
            buttons.push(okButton);
            if (this.options.customization.buttons.position === 'right') {
                cancelButton.inject(divButtons);
                okButton.inject(divButtons);
            } else {
                okButton.inject(divButtons);
                cancelButton.inject(divButtons);
            }

            this.buttons = buttons;
            return divButtons;

        },


        /**
         * @private
         * @summary Distinguish characteristics from user characteristics
         * @param {Object[]} allCharacteristics - All characteristics (characteristics + user characteristics)
         */
        differenciateCharacteristics: function (allCharacteristics) {

            let that = this;

            allCharacteristics.forEach(function (c) {
                if ((c.infos.User) && (that.options.customization.display.characteristics.user)) {
                    that.data.userCharacteristicsView.loaded.push(c);
                } else {
                    that.data.characteristicsView.loaded.push(c);
                }
            });

        },


        updateCharacteristics: function (characteristics) {

            let that = this;

            characteristics.forEach(function (c) {

                if ((c.infos.User) && (that.options.customization.display.characteristics.user)) {

                    let findIndex = that.data.userCharacteristicsView.loaded.findIndex(function (d) {
                        return (d.attr === c.attr);
                    });

                    that.data.userCharacteristicsView.loaded[findIndex].infos = Object.assign(that.data.userCharacteristicsView.loaded[findIndex].infos, c.infos);

                } else {

                    let findIndex = that.data.characteristicsView.loaded.findIndex(function (d) {
                        return (d.attr === c.attr);
                    });

                    that.data.characteristicsView.loaded[findIndex].infos = Object.assign(that.data.characteristicsView.loaded[findIndex].infos, c.infos);

                }

            });

        },


        /**
         * @private
         * @summary Get all characteristics at once
         */
        getCharacteristicsInfos: function (toFetch) {

            let that = this;

            if (!toFetch) {
                toFetch = ['Name', 'ReadOnly', 'Comment', 'Value', 'AuthorizedValues', 'AuthorizedValuesNLS', 'IsList', 'Type', 'Unit', 'User', 'MinMax', 'Label', 'LongString', 'IsNLS', 'Language' ];
            }

            // Prepare data to send through POST request
            let rscURI = this.options.resourceURI;
            let charNames = (this.options.customization.filters.whitelist.length > 0) ? this.options.customization.filters.whitelist : ['*'];
            let blacklist = this.options.customization.filters.blacklist;
            let format = 'DISPLAY';

            var optionsForGetInfos = {
                rscURIs: rscURI,
                charNames: charNames,
                toFetch: toFetch,
                blacklist: blacklist,
                format: format,
                lang: (this.options.customization.languages) ? this.options.customization.languages.selected : undefined,
                cbObject: {
                    onComplete: function (data, status) { },
                    onFailure: function (data, status, error) {
                        that.injectDefaultMessage();
                    }
                }
            };

            if (this.options.customization.getInfos !== undefined) {
                return this.options.customization.getInfos.call(this, optionsForGetInfos);
            } else {
                return this.characteristicServices.getInfos(optionsForGetInfos);
            }

        },


        /******************************************* PUBLIC METHODS *******************************************/
        /**
         * @summary Show footer
         */
        showFooter: function () {
            this.options.customization.display.buttons = true;
            this.elements.footer.show();
        },


        /**
         * @summary Hide footer
         */
        hideFooter: function () {
            this.options.customization.display.buttons = false;
            this.elements.footer.hide();
        },


        /**
         * @summary Backup form values for characteristics view
         */
        backupCharacteristicsView: function () {
            this.backupCV = this.elements.characteristicsView.backup();
            return this.backupCV;
        },


        /**
         * @summary Backup form values for user characteristics view
         */
        backupUserCharacteristicsView: function () {
            this.backupUCV = {};
            if (this.options.customization.display.characteristics.user) {
                this.backupUCV = this.elements.userCharacteristicsView.backup();
            }
            return this.backupUCV;
        },


        /**
         * @summary Save the current state of the form (values) on client side.
	     * This method is automatically called when the component is created.
         * Thus, the first state is saved until this function is called.
         */
        backup: function () {

            this.backupAll = {};

            let backupCV = this.backupCharacteristicsView();
            let backupUCV = this.backupUserCharacteristicsView();
            let backupCVClone = Object.assign({}, backupCV);
            let backupUCVClone = Object.assign({}, backupUCV);
            this.backupAll = Object.assign(backupCVClone, backupUCVClone);

            return this.backupAll;
        },


        /**
         * @summary Backup form values for characteristics view
         */
        rollbackCharacteristicsView: function () {
            this.elements.characteristicsView.rollback();
            return this.backupCharacteristicsView();
        },


        /**
         * @summary Backup form values for user characteristics view
         */
        rollbackUserCharacteristicsView: function () {
            if (this.options.customization.display.characteristics.user) {
                this.elements.userCharacteristicsView.rollback();
                return this.backupUserCharacteristicsView();
            }
            return {};
        },


        /**
        * @summary Go back to previous state.
        * The previous state corresponds to the form state when the function backup was called.
        */
        rollback: function () {
            this.rollbackCharacteristicsView();
            this.rollbackUserCharacteristicsView();
            this.backup();
        },


        /**
         * @summary Switch mode for the list of characteristics and/or user characteristics.
         * Render the component according to the specified mode.
	     * In that way, the form mode (edit or read-only) can be changed at any time.
         * @param {Boolean}     editModeCV   - Indicate if Characteristics View component should be in edit mode or not
         * @param {Boolean}     editModeUCV  - Indicate if User Characteristics View component should be in edit mode or not
         */
        switchMode: function (editModeCV, editModeUCV) {

            if (editModeCV === undefined) {
                editModeCV = true;
            }

            if (editModeUCV === undefined) {
                editModeUCV = true;
            }

            this.elements.characteristicsView.switchMode(editModeCV);

            if (this.options.customization.display.characteristics.user) {
                this.elements.userCharacteristicsView.switchMode(editModeUCV);
            }

        },


        /**
        * @summary Get UIKit input from field name
        * @param {String} name - Field name
        * @return {DS/UIKIT/Input} UWA/Element corresponding to the input (if the field name exists)
        */
        getInputFromFieldName: function (name) {

            let input = this.elements.characteristicsView.getInputFromFieldName(name);
            if ((!input) && (this.options.customization.display.characteristics.user)) {
                input = this.elements.userCharacteristicsView.getInputFromFieldName(name);
            }

            return input;
        },


        /**
         * @summary Get values for each input in the editor
         * @return {Object} - Map as uri/value
         */
        getFormValues: function () {

            let formValues = this.elements.characteristicsView.getFormValues();

            if (this.options.customization.display.characteristics.user) {
                formValues = Object.assign(formValues, this.elements.userCharacteristicsView.getFormValues());
            }

            return formValues;

        },


        /**
         * @summary Execute action associated to 'Ok' button
         * @returns {Promise}
         */
        executeAction: function (forCharacteristics, forUserCharacteristics) {

            if (forCharacteristics === undefined) {
                forCharacteristics = true;
            }

            if (forUserCharacteristics === undefined) {
                forUserCharacteristics = true;
            }

            let that = this;
            let okButton = this.options.customization.buttons.ok;
            let rootURL = this.options.rdfServerURL;
            let resourceURI = this.options.resourceURI;
            let thatButton;
            if (this.options.customization.display.buttons) {
                thatButton = this.buttons[1];
                thatButton.setDisabled(true);
            }

            return new Promise(function (resolve, reject) {

                let formValues = that.elements.characteristicsView.getFormValues();

                let copyFV = {};
                if (forCharacteristics) {
                    Object.keys(formValues).forEach(function (key) {
                        if (!Object.keys(that.options.customization.buttons.ok.specificActions).includes(key)) {
                            copyFV[key] = formValues[key];
                        }
                    });
                }

                if ((that.options.customization.display.characteristics.user) && (forUserCharacteristics)) {
                    let modifiedUserCharacteristics = that.getModifiedUserCharacteristics();
                    copyFV = Object.assign(modifiedUserCharacteristics, copyFV);
                }

                try {

                    // Verify at least one characteristic modified
                    if (Object.keys(copyFV).length > 0) {
                        // Default action (set by default)
                        if (that.recreate) {
                            resolve(okButton.defaultAction(rootURL, resourceURI, copyFV, that.fromLanguage));
                        } else {
                            resolve(okButton.defaultAction(rootURL, resourceURI, copyFV, that.elements.characteristicsView.getCurrentLanguage()));
                        }

                    }

                } catch (error) {
                    if (thatButton) {
                        thatButton.setDisabled(false);
                    }
                    console.log(error);
                }

                resolve();

            }).then(function () {

                // Overriding functions for some characteristics
                Object.keys(that.options.customization.buttons.ok.specificActions).forEach(function (o) {
                    if (that.elements.characteristicsView.modifiedValues.includes(o) || !that.options.customization.onChangeSensitive) {
                        let value = that.elements.characteristicsView.elements.listCharacteristics.getValue(o);
                        try {
                            let input = that.elements.characteristicsView.getInputFromFieldName(o);
                            let infos = {};
                            if (that.elements.characteristicsView.isPerson(input.options.infos)) {
                                infos.FullName = input.getLabel();
                            }
                            that.options.customization.buttons.ok.specificActions[o](value, infos);
                        } catch (error) {
                            if (thatButton) {
                                thatButton.setDisabled(false);
                            }
                            console.log(error);
                        }
                    }
                });

                if ((that.options.customization.display.characteristics.user) && (forUserCharacteristics)) {

                    let ucLang;
                    if (that.recreate) {
                        ucLang = that.fromLanguage;
                    } else {
                        ucLang = that.elements.userCharacteristicsView.getCurrentLanguage();
                    }

                    // Remove user characteristics
                    let removedUserCharacteristics = that.getRemovedUserCharacteristics();
                    if (removedUserCharacteristics.length > 0) {
                        that.operationServices.setHeader('Accept-Language', ucLang);
                        that.operationServices.run({
                            operationVersionURI: 'dsop:removeUserCharacteristics_V1',
                            inputs: {
                                resource: { '@id': that.options.resourceURI },
                                characteristicURIs: removedUserCharacteristics
                            },
                            cbObject: {
                                onComplete: function () {
                                    // Set internal variable in user characteristics view
                                    that.elements.userCharacteristicsView.alreadyAddedUserCharacteristics = that.elements.userCharacteristicsView.alreadyAddedUserCharacteristics.filter(function (alreadyAdded) {
                                        return (!removedUserCharacteristics.includes(alreadyAdded.options.infos.Name));
                                    });
                                    that.elements.userCharacteristicsView.removedUserCharacteristics = [];
                                },
                                onFailure: function () { }
                            },
                            timeout: 100000
                        });
                    }

                    // ------

                    // Add user characteristics
                    let addedUserCharacteristics = that.getUserCharacteristics();
                    let addedUserCharacs = Object.keys(addedUserCharacteristics).map(function (addedUC) {
                        return {
                            id: addedUC,
                            value: addedUserCharacteristics[addedUC].value
                        };
                    });

                    if (addedUserCharacs.length > 0) {
                        that.operationServices.setHeader('Accept-Language', ucLang);
                        that.operationServices.run({
                            operationVersionURI: 'dsop:addUserCharacteristics_V1',
                            inputs: {
                                resource: { '@id': that.options.resourceURI },
                                userCharacteristics: addedUserCharacs
                            },
                            cbObject: {
                                onComplete: function () {
                                    that.elements.userCharacteristicsView.addedUserCharacteristics = [];
                                },
                                onFailure: function () { }
                            },
                            timeout: 100000
                        });
                    }

                    that.data.userCharacteristicsView.added = [];
                    that.elements.userCharacteristicsView.backup();

                }

                that.elements.characteristicsView.modifiedValues = [];
                that.data.characteristicsView.changed = [];
                that.elements.characteristicsView.backup();
                that.elements.characteristicsView.backupFormValuesOnChange = that.elements.characteristicsView.backupFormValues;

                if (thatButton) {
                    thatButton.setDisabled(that.options.customization.onChangeSensitive && !that.getContent().hasClassName('editorViaInstantiationPanel'));
                }

            }).catch(function () {
                if (thatButton) {
                    thatButton.setDisabled(false);
                }
            });

        }

    });

    return CharacteristicsEditorView;

});
