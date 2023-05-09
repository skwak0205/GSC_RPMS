define('DS/dsbaseUIControls/UserCharacteristicsView', [
    'DS/dsbaseUIControls/UserCharacteristicsViewOptionsChecker',
    'DS/dsbaseUIControls/CharacteristicsView',
    'DS/dsbaseUIControls/CharacteristicValuesSearcher',
    'DS/dstoolsUIServices/dsbaseServices',
    'UWA/Core',
    'DS/UIKIT/Input/Text',
    'DS/UIKIT/Input/Number',
    'DS/UIKIT/Input/Date',
    'DS/UIKIT/Input/Toggle',
    'DS/UIKIT/Input/Select',
    'i18n!DS/dsbaseUIControls/assets/nls/UserCharacteristicsView',
    'css!DS/UIKIT/UIKIT.css',
    'css!DS/dsbaseUIControls/assets/style/UserCharacteristicsView.css'
], function (
    UserCharacteristicsViewOptionsChecker,
    CharacteristicsView,
    CharacteristicValuesSearcher,
    DSBaseServices,
    UWA,
    Text,
    Number,
    Date,
    Toggle,
    Select,
    NLS
) {

    'use strict';

    let UserCharacteristicsView = CharacteristicsView.extend({

        className: function () {

            let className = this._parent();
            className += ' user-characteristics-view';
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
         * @property {SearchEngine}     autocomplete                             - Autcomplete search function called when user starts writing in corresponding input (show suggestions)
         * @property {Function}         [options.customization.onSuggest=]       - Function called when an item is shown in the list of suggestions (has to return a UWA/Element to render the associated item in the list of suggestions)
         * @property {Function}         [options.customization.onSelect=]        - Function called when an item is selected in the list of suggestions (has to return a string to render the associated badge)
         */


        /*********************** CONSTRUCTOR ***********************/
        /**
         * @param {Object}                          options                                                             - Options to adapt behavior & customize the view
         * @param {String}                          options.rdfServerURL                                                - RDF server root URL (mandatory)
         * @param {String}                          options.resourceURI                                                 - URI of the resource for which we list user characteristics (mandatory)
         * @param {String}                          options.fromOptionSet                                               - Option set to use in order to retrieve some user characteristics
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
         * @param {Object}                          [options.customization.display]                                     - Object to define display settings (i.e. some components should be displayed or not ?)
         * @param {Boolean}                         [options.customization.display.switchMode=true]                     - Boolean to indicate if "switch mode" button should be displayed or not
         * @param {Boolean}                         [options.customization.display.switchLanguage=true]                 - Boolean to indicate if "switch language" button should be displayed or not
         * @param {Boolean}                         [options.customization.display.characteristics.constraints=true]    - Boolean to indicate if user characteristics constraints within a popover should be displayed or not
         * @param {Boolean}                         [options.customization.display.scroller=true]                       - Boolean to indicate if scroll view should be used
         *
         * @param {String}                          [options.customization.constraintsPosition="top"]                   - Position of the constraints popup relative to the 'information' icon (i). It can be one of these : 'top', 'right', 'bottom' or 'left'
         *
         * @param {Object.<string, SearchOption>}   [options.customization.search={}]                                   - Object to define autocomplete inputs
         *
         * @param {Object}                          options.customization.filters                                       - Object to define filters (filters are taken into account in this order: 1. whitelist  2. blacklist  3. order )
         * @param {String[]}                        [options.customization.filters.whitelist=[]]                        - Array of URIs to retrieve specific user characteristics
         * @param {String[]}                        [options.customization.filters.blacklist=[]]                        - Array of URIs to avoid retrieving some user characteristics
         * @param {String[]}                        [options.customization.filters.orderlist=["http://www.3ds.com/RDF/Corpus/Kernel/nlsLabel","http://www.3ds.com/RDF/Corpus/Kernel/nlsComment","http://www.3ds.com/RDF/Corpus/Kernel/owner']] - Array of URIs to order added user characteristics, according to the index in the array
         * 
         * @param {Object}                          [options.events]                                                    - Object to store events callbacks
         * @param {Function}                        [options.onValueChange]                                             - Callback called when a value is modified in an input
         * @param {Function}                        [options.onFinishRender]                                            - Callback called when the view is rendered
         *
         * @constructs UserCharacteristicsView
         * @summary Setup component
         * @memberof module:DS/dsbaseUIControls/UserCharacteristicsView
         * @override
         */
        setup: function (options) {

            // Checking options
            (new UserCharacteristicsViewOptionsChecker(options)).checkAllOptions();
            this.options = options;

            // Counters
            this.numberOfAvailableUserCharacteristics = 0;
            this.numberOfUserCharacteristicsInList = 0;
            this.numberOfShownSuggestions = 0;

            // Available user characteristics
            this.userCharacteristicsFromOptionSet = [];

            // Inputs
            this.alreadyAddedUserCharacteristics = [];
            this.modifiedUserCharacteristics = [];
            this.addedUserCharacteristics = [];
            this.removedUserCharacteristics = [];

            // Display
            this.dropdownIsHidden = true;

            // Generate the user characteristics view
            let that = this;
            this.launchSpinnerOnLoad();
            let thatParent = this._parent.bind(this);

            return this.getUserCharacteristicsFromOptionSet().then(function (userCharacteristics) {

                return that.getMetaDataForUserCharacteristics(userCharacteristics).then(function (metaData) {

                    // Add meta data
                    userCharacteristics.forEach(function (uc) {
                        Object.assign(uc.infos, metaData[uc.attr]);
                    });

                    that.numberOfAvailableUserCharacteristics = userCharacteristics.length;
                    that.userCharacteristicsFromOptionSet = userCharacteristics;

                    // Execute parent function
                    return thatParent(options).then(function () {

                        if (!that.elements.iconsBar) {
                            that.createIconsBar();
                        }

                        that.addRemoveButtonOnInputs();
                        that.createDefaultMessageUC();

                        if ((that.options.customization.display.scroller) && (!that.elements.scroller)) {
                            that.createScroller();
                        }

                        that.render({
                            fromUCV: true
                        });

                        new Promise(function() {
                            that.options.events.onFinishRenderBackup.bind(that)();
                        });

                    });

                });

            });

        },


        /**
         * @private
         * @param {Object} options - List of options to detect rendering
         * @override 
         */
        render: function (options) {

            if (options && options.fromUCV) {

                this.options.events.onFinishRenderBackup = this.options.events.onFinishRender;
                this.options.events.onFinishRender = undefined;
                this._parent();
                this.elements.defaultMessageUC.inject(this.container);

                // Check if "scroller" or "div" is displayed
                if ((!this.options.customization.display.scroller) && (this.elements.listCharacteristics.getFields().length === 0)) {
                    this.elements.listCharacteristics.inject(this.container);
                }

                // Check if "switchMode" icon should be displayed
                if (!this.options.customization.display.switchMode) {
                    let text = (this.options.customization.editMode) ? NLS.get('UCV.mode.icon.text.switchEdit') : NLS.get('UCV.mode.icon.text.switchReadOnly');
                    this.elements.iconsBar.getItem(text).elements.container.hide();
                }

                if (this.numberOfAvailableUserCharacteristics > 0) {

                    if (!this.isListEmpty()) {
                        this.elements.defaultMessageUC.hide();
                    } else if (this.options.customization.display.scroller) {
                        this.elements.scroller.getContent().hide();
                    }

                } else {

                    this.elements.defaultMessageUC.hide();
                    this.elements.scroller.getContent().hide();
                    (new UWA.Element('div', {
                        content: NLS.get('UCV.noUC.error'),
                        class: 'user-characteristics-view-not-available-default-message'
                    })).inject(this.container);

                }

            }

        },


        /**
         * @private
         * @summary Create a default message (specific to this view) with a big 'plus' icon
         */
        createDefaultMessageUC: function () {

            let that = this;

            // Create a big clickable icon being displayed in the view container
            // When the icon is clicked, it flickers the dropdown menu containing the available user characteristics
            let plusIcon = new UWA.Element('i', {
                class: 'fonticon fonticon-list-add',
                events: {
                    click: function () {

                        let plusIconDropdown = that.elements.iconsBar.getItem(NLS.get('UCV.add.icon.text')).elements.container;

                        if (that.dropdownIsHidden) {
                            plusIconDropdown.click();
                            that.dropdownIsHidden = false;
                        } else {
                            that.getContent().click();
                            that.dropdownIsHidden = true;
                        }

                    }
                }
            });

            this.elements.defaultMessageUC = new UWA.Element('div', {
                content: plusIcon,
                class: 'user-characteristics-view-default-message'
            });

        },


        /**
        * @private
        * @summary Create default message when no user characteristics retrieved
        * @override
        */
        createDefaultMessage: function () { },


        /**
         * @private
         * @summary Create icons bar options (with a menu for available user characteristics)
         * @override
         */
        createIconsBar: function () {

            this._parent();
            let that = this;

            if (that.numberOfAvailableUserCharacteristics > 0) {

                // Loop over all retrieved user characteristics
                let items = [];
                that.userCharacteristicsFromOptionSet.forEach(function (userCharac) {

                    // Create dropdown items for "plus" icon
                    // When clicking on an item, we create a new input and we hide the user characteristic in dropdown
                    items.push({
                        text: ((userCharac.label) ? userCharac.label : userCharac.uri),
                        name: userCharac.uri,
                        handler: function () {

                            let input = that.generateInput(userCharac);
                            input.getContent().addEvent('change', function (event) {
                                if (input.options.type === 'checkbox') {
                                    input.options.checked = !input.options.checked;
                                    event.target.value = input.options.checked;
                                }
                                that.getEventFunction()(input.getName(), event.target.value);
                            });
                            input.setDisabled(!that.options.customization.editMode);

                            // Add the new input in form
                            if (input) {
                                that.addANewInputInForm(input);
                                this.getItem(userCharac.uri).elements.container.hide();
                            }

                            // Disable "plus" dropdown if all user characteristics have been added
                            if (that.shouldPlusIconBeDisabled()) {
                                that.elements.iconsBar.disableItem(1);
                            } else {
                                that.elements.iconsBar.enableItem(1);
                            }

                            that.onAddUserCharacteristic(userCharac.uri);

                        }
                    });

                    that.numberOfShownSuggestions++;

                });

                // Create menu with a new item to select a user characteristic
                that.elements.iconsBar.addItem({
                    fonticon: 'plus',
                    text: NLS.get('UCV.add.icon.text'),
                    innerComponent: {
                        type: 'dropdownmenu',
                        options: {
                            items: items,
                            events: {
                                onShow: function (event) {

                                    // Create "subMenu" is it does not exist
                                    if (!that.elements.subMenu) {
                                        that.elements.subMenu = this;
                                        that.removeAlreadyAddedUserCharacteristicsSuggestions();
                                    }

                                    // Hide "subMenu" is no available suggestions
                                    if (that.shouldPlusIconBeDisabled()) {
                                        this.elements.container.hide();
                                        that.elements.iconsBar.disableItem(1);
                                    }

                                    // Hide "subMenu" if we load it on component creation
                                    if (that.options.fromStart === true) {
                                        this.elements.container.hide();
                                        that.options.fromStart = undefined;
                                    }

                                }
                            }
                        }
                    }
                });

                // Force loading once
                that.options.fromStart = true;
                that.elements.iconsBar.getItem(NLS.get('UCV.add.icon.text')).elements.container.click();

            }

        },


        /**
         * @private
         * @summary  Remove suggestions from 'plus' icon menu for user characteristics previously added
         */
        removeAlreadyAddedUserCharacteristicsSuggestions: function () {

            let that = this;

            this.userCharacteristicsFromOptionSet.forEach(function (userCharac) {
                if (that.isAlreadyAddedUC(userCharac.uri)) {
                    that.elements.subMenu.getItem(userCharac.uri).elements.container.hide();
                    that.numberOfShownSuggestions--;
                }
            });

        },


        /**
         * @private
         * @summary Get user characteristics previously added
         * @override
         */
        getCharacteristicsInfos: function () {

            let that = this;
            let thatParent = this._parent.bind(this);

            return new Promise(function (resolve, reject) {
                thatParent().then(function (characteristics) {
                    characteristics = characteristics.filter(function (c) {
                        return c.infos.User;
                    });
                    that.numberOfUserCharacteristicsInList += characteristics.length;
                    resolve(characteristics);
                });
            });

        },


        /**
        * @private
        * @summary Get user characteristics information from an option set
        * @returns {Function} Function to get user characteristics from the specified server
        */
        getUserCharacteristicsFromOptionSet() {

            let that = this;
            let url = this.options.rdfServerURL + 'v0/invoke/dsbase:characteristic.listUserCharacteristicsFromOptionSet';
            let optionSet = this.options.fromOptionSet;

            /* NEED TO BE REPLACED BY WRAPPER */
            /* WRAPPER NEED TO BE CREATED TO RETRIEVE USER CHARACTERISTICS FROM OPTION SET */
            return new Promise(function (resolve, reject) {

                DSBaseServices.getRequestFunction()(url, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept-Language': that.options.customization.languages.selected
                    },
                    type: 'json',
                    data: JSON.stringify([
                        optionSet,
                    ]),
                    onComplete: function (data, status) {

                        data.value = JSON.parse(data.value);

                        // Select only the wanted user characteristics
                        if (that.options.customization.filters.whitelist.length > 0) {
                            data.value = data.value.filter(function (uc) {
                                return (that.options.customization.filters.whitelist.includes(uc.uri));
                            });
                        }

                        // Remove the unwanted user characteristics
                        if (that.options.customization.filters.blacklist.length > 0) {
                            data.value = data.value.filter(function (uc) {
                                return (!that.options.customization.filters.blacklist.includes(uc.uri));
                            });
                        }

                        // Order user characteristics
                        if (that.options.customization.filters.orderlist.length > 0) {
                            data.value.forEach(function (uc) {
                                uc.attr = uc.uri;
                            });
                            data.value = that.orderCharacteristics(data.value);
                        }

                        resolve(data.value);

                    },
                    onFailure: function (data, status, error) {
                        reject(error);
                    },
                    timeout: 999999
                });

            });

        },


        getMetaDataForUserCharacteristics: function (userCharacs) {

            let that = this;
            let url = that.options.rdfServerURL + 'v0/invoke/dsbase:characteristic.getMetadata';
            let userCharacsURIs = [];

            userCharacs.forEach(function (uc) {
                userCharacsURIs.push(uc.attr);
            });

            /* NEED TO BE REPLACED BY WRAPPER */
            return new Promise(function (resolve, reject) {

                DSBaseServices.getRequestFunction()(url, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept-Language': that.options.customization.languages.selected
                    },
                    type: 'json',
                    data: JSON.stringify([
                        JSON.stringify(userCharacsURIs)
                    ]),
                    onComplete: function (data, status) {
                        data.value = JSON.parse(data.value);
                        resolve(data.value);
                    },
                    onFailure: function (data, status, error) {
                        reject(error);
                    },
                    timeout: 999999
                });

            });

        },


        /**
         * @private
         * @summary Get a remove button associated to a given input
         * @param {DS/UIKIT/Input} input - UIKIT Input 
         */
        getRemoveButton: function (input) {

            let that = this;

            return (new UWA.Element('i', {
                class: 'fonticon fonticon-close user-chracteristics-view-remove',
                events: {
                    click: function (event) {
                        that.getRemoveCallback()(input);
                        this.destroy();
                    }
                }
            }));

        },


        /**
         * @private
         * @summary Add a "remove" icon floating right for each input
         */
        addRemoveButtonOnInputs: function () {

            let that = this;

            // Loop over all created inputs for already added user characteristics
            // Add a remove icon for each of them
            this.elements.listCharacteristics.getFields().forEach(function (field) {

                let input = that.getInputFromFieldName(field.name);
                input.getContent().addClassName('user-characteristics-view-input');
                that.alreadyAddedUserCharacteristics.push(input);
                let inputParent = input.getContent().getParent();

                // When the "remove" icon is clicked, it destroys itself and the associated input
                that.getRemoveButton(input).inject(inputParent, 'bottom');

                // Move remove icon at the good position
                if (input.options.infos.LongString === true) {
                    input.elements.container.addEvent('resize', function () {
                        that.moveRemoveIcon(this);
                    });
                }

            });

        },


        /**
         * @private
         * @summary Create a textarea field with the expected options
         * @param   {Object} characteristicInfos - User characteristic information to take into account
         * @returns {Object}
         * @override
         */
        generateTextareaField: function (userCharacteristicsInfos) {
            let field = this._parent(userCharacteristicsInfos);
            field.rows = 4;
            field.multiline = true;
            return field;
        },


        /**
         * @private
         * @summary Generate input according to user characteristics infos
         * @param {Object} userCharac - User characteristic we want to generate an input for
         */
        generateInput: function (userCharac) {

            let that = this;

            let input;
            let infos = userCharac.infos;
            infos.Name = userCharac.uri;
            infos.Label = userCharac.label;
            let inputOptions = this.generateField(infos);
            inputOptions.className += ' user-characteristics-view-input';

            // Defined as autocomplete ?
            // Resource ?
            // List ?
            // Vector ?
            if (Object.keys(this.options.customization.search).includes(userCharac.uri)
                || ['resource', 'list', 'vector'].includes(infos.UIType)) {
                inputOptions.searchEngine = this.options.customization.search[inputOptions.name].autocomplete;
                inputOptions.customization = {
                    classNames: [ inputOptions.className ],
                    onSuggest: (this.options.customization.search[infos.Name]) ? this.options.customization.search[infos.Name].onSuggest : undefined,
                    onSelect: (this.options.customization.search[infos.Name]) ? this.options.customization.search[infos.Name].onSelect : undefined,
                };
                input = new CharacteristicValuesSearcher(inputOptions);

                // Has authorized values ?
            } else if (infos.AuthorizedValues) {
                inputOptions.className += ' input-number';
                inputOptions.placeholder = false;
                input = new Select(inputOptions);

                // Number ?
            } else if (infos.UIType === 'number') {
                input = new Number(inputOptions);
                input.setValue((infos.MinMax.min) ? infos.MinMax.min.value : 0);

                // Long string ?
            } else if (infos.UIType === 'string' && infos.LongString === true) {
                input = new Text(inputOptions);

                // Replace remove icon next to textarea (according to current height input)
                input.elements.container.addEvent('resize', function () {
                    that.moveRemoveIcon(this);
                });

                // Date ?
            } else if (infos.UIType === 'date') {
                input = new Date(inputOptions);

                // Boolean ?
            } else if (infos.UIType === 'boolean') {
                inputOptions.checked = false;
                input = new Toggle(inputOptions);

                // All other cases
            } else {
                inputOptions.className += ' form-control form-control-root';
                input = new Text(inputOptions);
            }

            // Unit ?
            if (((infos.Unit != undefined) && (infos.Unit.uri != undefined) && (infos.Unit.symbol != undefined)) || ((infos.Units != undefined) && (infos.Units.length > 0))) {
                this.createUnitButton(input);
            }

            return input;

        },


        /**
         * @private
         * @summary Move 'remove' icon next to input (according to its height)
         * @param {UWA/Element} element - Graphic element to consider to move the icon
         */
        moveRemoveIcon: function (element) {
            let removeIcon = element.getParent().getElement('.user-chracteristics-view-remove');
            let height = parseInt(element.getStyle('height'), 10) / 2 - 9.5;
            removeIcon.setStyle('top', height + 'px');
        },


        /**
         * @private
         * @summary Save a newly added user characteristic
         * @param {DS/UIKIT/Input} input - Input to save
         */
        addUserCharacteristic: function (input) {

            if (!this.isAlreadyAddedUC(input.options.name)) {
                this.addedUserCharacteristics.push(input);
            } else {

                this.onValueChange(input.getName(), input.getValue());

                // Add
                this.alreadyAddedUserCharacteristics = this.alreadyAddedUserCharacteristics.filter(function (alreadyAdded) {
                    return (alreadyAdded.options.infos.Name !== input.options.infos.Name);
                });
                this.alreadyAddedUserCharacteristics.push(input);

                // Remove
                if (this.isRemoved(input.getName())) {
                    let indexRemoved = this.removedUserCharacteristics.findIndex(function (removedCharac, index) {
                        if (removedCharac.getName() === input.getName()) {
                            return index;
                        }
                    });
                    this.removedUserCharacteristics.splice(indexRemoved, 1);
                }

            }

            this.numberOfUserCharacteristicsInList++;
            this.numberOfShownSuggestions--;

        },


        /**
         * @private
         * @summary Add a new DS/UIKIT/Input in the form element
         */
        addANewInputInForm: function (input) {

            // Create a new form input with label & remove icon
            let formGroup = new UWA.Element('div', {
                class: 'form-group'
            });

            // Label
            if (input.options.infos.UIType !== 'boolean') {
                (new UWA.Element('label', {
                    html: input.options.infos.Label
                })).inject(formGroup);
            }

            // Input
            input.inject(formGroup);

            // Remove button
            this.getRemoveButton(input).inject(formGroup);

            // Inject input in form
            formGroup.inject(this.elements.listCharacteristics.getContent());

            // Save newly added user characteristic
            this.addUserCharacteristic(input);

            // Adapt display
            if (!this.isListEmpty()) {

                this.elements.defaultMessageUC.hide();

                if (this.options.customization.display.scroller) {
                    this.elements.scroller.getContent().show();
                }

                if (this.options.customization.display.switchMode) {
                    let text = (!this.options.customization.editMode) ? NLS.get('UCV.mode.icon.text.switchEdit') : NLS.get('UCV.mode.icon.text.switchReadOnly');
                    this.elements.iconsBar.getItem(text).elements.container.show();
                }

            }

        },


        /**
         * @private
         * @summary Should the plus icon be disabled ?
         * @param {String} userCharacName - URI of the user characteristic
         */
        shouldPlusIconBeDisabled: function () {
            if (this.isListEmpty()) {
                return false;
            } else {
                return (this.numberOfShownSuggestions === 0);
            }
        },


        /**
         * @private
         * @summary Is a user characteristic previously added ?
         * @param {String} userCharacName - URI of the user characteristic
         */
        isAlreadyAddedUC: function (userCharacName) {
            return this.alreadyAddedUserCharacteristics.some(function (alreadyAdded) {
                return (alreadyAdded.getName() === userCharacName);
            });
        },


        /**
         * @private
         * @summary Has the user characteristic been removed ?
         * @param {String} userCharacName - URI of the user characteristic
         */
        isRemoved: function (userCharacName) {
            return this.removedUserCharacteristics.some(function (removedCharac) {
                return (removedCharac.getName() === userCharacName);
            });
        },


        /**
         * @private
         * @summary Are there new user characteristics added ?
         */
        areThereNewUC: function () {
            return (this.addedUserCharacteristics.length > 0);
        },


        /**
         * @private
         * @summary Is the user characteristics list empty ?
         */
        isListEmpty: function () {
            return (this.numberOfUserCharacteristicsInList === 0);
        },


        /**
         * @private
         * @summary Return the function callback used to remove a user characteristic from the list
         * @returns Function to remove a user characteristic
         */
        getRemoveCallback: function () {

            let that = this;

            return function (input) {

                let inputName = input.options.name;
                that.numberOfShownSuggestions++;
                that.numberOfUserCharacteristicsInList--;

                // Destroy label & input
                let formGroup = input.getContent().getClosest('.form-group');
                if ((formGroup !== undefined) && (formGroup !== null)) {
                    formGroup.destroy();
                }

                // Forget about the user characteristic
                if (that.isAlreadyAddedUC(input.options.name)) {
                    that.removedUserCharacteristics.push(input);
                } else {
                    that.addedUserCharacteristics = that.addedUserCharacteristics.filter(function (userCharacInput) {
                        return (userCharacInput.options.name !== inputName);
                    });
                }


                // Show the user characteristic in the available options in listing
                if (that.elements.iconsBar) {
                    let item = that.elements.iconsBar.overflowMenu.getItem(inputName);
                    if (item) {
                        item.elements.container.show();
                    }
                }

                // Hide "switchMode" button if there's no user characteristic added
                if (that.isListEmpty()) {

                    that.elements.scroller.getContent().hide();
                    that.elements.defaultMessageUC.show();

                    if (that.options.customization.display.switchMode) {
                        let text = (this.options.customization.editMode) ? NLS.get('UCV.mode.icon.text.switchEdit') : NLS.get('UCV.mode.icon.text.switchReadOnly');
                        that.elements.iconsBar.getItem(text).elements.container.hide();
                    }

                }

                // Enable user characteristics list if at least one
                if (!that.shouldPlusIconBeDisabled()) {
                    that.elements.iconsBar.enableItem(1);
                }

                // Raise event
                that.onRemoveUserCharacteristic(inputName);

            };

        },


        /**
         * @private
         * @summary Raise event when a value changed for a given input
         * @param {String} inputName    - Name of the input as stored in the form
         * @param {String} value        - Nexw value associated to that input
         */
        onValueChange: function (inputName, value) {
            this._parent(inputName, value);
            this.onModifyUserCharacteristic(inputName);
        },


        /**
         * @private
         * @summary Raise event when a user characteristic is added
         * @param {String} name - URI of the user characteristic
         */
        onAddUserCharacteristic: function (name) {
            this.options.events.onAddUserCharacteristic(name);
        },


        /**
         * @private
         * @summary Raise event when a user characteristic is modified
         * @param {String} name - URI of the user characteristic
         */
        onModifyUserCharacteristic: function (name) {
            this.options.events.onModifyUserCharacteristic(name);
        },


        /**
         * @private
         * @summary Raise event when a user characteristic is removed
         * @param {String} name - URI of the user characteristic
         */
        onRemoveUserCharacteristic: function (name) {
            this.options.events.onRemoveUserCharacteristic(name);
        },


        /******************************************* PUBLIC METHODS *******************************************/
        /**
        * @summary Get UIKit input from field name
        * @param {String} name - Field name
        * @return {DS/UIKIT/Input} UWA/Element corresponding to the input (if the field name exists)
        */
        getInputFromFieldName: function (name) {

            let input = this._parent(name);

            if(!input) {
                input = this.addedUserCharacteristics.find(function(added) {
                    return (added.options.infos.Name === name);
                });
            }

            return input;

        },

        
        /**
         * @summary Switch mode for the list of user characteristics
         * @param {Boolean} editMode - Indicate if component should be in edit mode or not
         * @override
         */
        switchMode: function (editMode) {

            if (editMode === undefined) {
                editMode = true;
            }

            this._parent(editMode);

            // Disable "on-the-fly" inputs previously added
            this.addedUserCharacteristics.forEach(function (userCharacInput) {
                userCharacInput.setDisabled(!editMode);
            });

            // Disable already added user characteristics
            this.alreadyAddedUserCharacteristics.forEach(function (alreadyAdded) {
                alreadyAdded.setDisabled(!editMode);
            });

        },


        /**
         * @typedef     {Object} UserCharac
         * @property    {String} id     - URI of the user characteristic
         * @property    {String} value  - Value entered by the user for that user characteristic
         */

        /**
         * @summary Get added user characteristics infos
         * @returns {UserCharac[]}
         */
        getAddedUserCharacteristics: function () {

            let addedUserCharacs = {};

            this.addedUserCharacteristics.forEach(function (input) {

                let value;
                if (input.options.type === 'select') {
                    value = input.getValue()[0];
                } else {
                    value = input.getValue();
                }

                addedUserCharacs[input.getName()] = {
                    type: input.options.infos.UIType,
                    value: value
                };

            });

            return addedUserCharacs;

        },


        /**
         * @summary Get all modified user characteristics
         *          Will only return preivously added user characteristics with a modified value
         * @returns {UserCharac[]}
         */
        getModifiedUserCharacteristics: function () {

            let that = this;
            let modifiedAlreadyAddedUserCharacteristics = {};

            this.modifiedValues.forEach(function (uri) {

                if (!that.getRemovedUserCharacteristics().includes(uri)) {

                    let input = that.alreadyAddedUserCharacteristics.find(function (alreadyAdded) {
                        return (uri === alreadyAdded.options.infos.Name);
                    });

                    if (input) {
                        modifiedAlreadyAddedUserCharacteristics[uri] = {
                            type: input.options.infos.UIType,
                            value: input.getValue()
                        };
                    }

                }

            });

            return modifiedAlreadyAddedUserCharacteristics;

        },


        /**
         * @summary Get all removed user characteristics
         * @returns {String[]} Array of URIs
         */
        getRemovedUserCharacteristics: function () {

            let removedUserCharacteristics = [];

            this.removedUserCharacteristics.forEach(function (input) {
                removedUserCharacteristics.push(input.getName());
            });

            return removedUserCharacteristics;

        }

    });

    return UserCharacteristicsView;

});
