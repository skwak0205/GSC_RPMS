define('DS/dsbaseUIControls/TemplateInstantiationPanel', [
    'DS/dsbaseUIControls/TemplateInstantiationPanelOptionsChecker',
    'DS/dsbaseUIControls/CharacteristicsEditorView',
    'DS/dsbaseUIServices/TemplateServices',
    'UWA/Core',
    'UWA/Class/View',
    'DS/UIKIT/Form',
    'DS/UIKIT/Spinner',
    'i18n!DS/dsbaseUIControls/assets/nls/TemplateInstantiationPanel',
    'css!DS/UIKIT/UIKIT.css',
    'css!DS/dsbaseUIControls/assets/style/TemplateInstantiationPanel.css'
], function (
    TemplateInstantiationPanelOptionsChecker,
    CharacteristicsEditorView,
    TemplateServices,
    UWA,
    View,
    Form,
    Spinner,
    NLS
) {

    'use strict';


    /**
     * @summary Generic panel to instantiate templates
     * @extends UWA/Class/View
     */
    let InstantiationPanel = View.extend({

        tagName: 'div',


        /**
         * @private
         * @summary Add complementary class names to the panel component
         */
        className: function () {

            let className = 'template-instantiation-panel';

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


        /****** SEARCH ******/
        /**
         * @typedef     {Object} Item
         * @property    {String} label  - Label to display when searching for a text
         * @property    {String} value  - Value being set
         */

        /**
         * @typedef {Function} searchInLocal
         * @param   {String} text       - Text to search
         * @param   {Item[]} localItems - Local items used for searching
         */

        /**
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
        * @returns     {Item[]|Promise.<Item[]>}
        */

        /**
         * @constructor
         * @alias TemplateInstantiationPanel
         * @summary Setup component
         *
         * @param {Object}                          options                                                         - Options to adapt behavior & customize the panel
         * @param {String}                          options.rdfServerURL                                            - RDF Server URL to use to get infos, to validate values & to instantiate a template
         *
         * @param {Object}                          options.templates                                               - Object to indicate which templates to retrieve
         * @param {Object[]}                        [options.templates.list=[]]                                     - Array of templates URIs to propose to the end-user
         * @param {Object}                          [options.templates.fromOptionSet={}]                            - Object to define an option set to use in order to retrieve some additionnal templates
         * @param {String}                          options.templates.fromOptionSet.uri                             - Option set URI
         * @param {String}                          options.templates.fromOptionSet.taxonomy                        - Taxonomy URI to search for specific templates types in the option set
         *
         * @param {Object}                          [options.customization]                                         - Object to define customization parameters to render the panel
         * @param {String}                          [options.customization.preferenceId]                            - Instantiation panel id (used to retrieve preferences - units - from previous utilization)
         * @param {String[]}                        [options.customization.classNames=[]]                           - Array of additionnal CSS class names
         * @param {String}                          [options.customization.title="Instantiation Panel"]             - Title displayed at the top of the panel
         *
         * @param {Object}                          options.customization.editMode                                  - Object to specify if "template choice" or "characteristics" are in edit mode or not
         * @param {Boolean}                         [options.customization.editMode.templateChoice=true]            - Boolean to indicate if "template choice" component is editable
         * @param {Boolean}                         [options.customization.editMode.characteristics=true]           - Boolean to indicate if "characteristics" inputs form are editable
         *
         * @param {Object}                          [options.customization.languages]                               - Languages options
         * @param {Array.<String>}                  options.customization.languages.supported                       - Array of languages proposed to the user (i.e. array of RDF languages codes)
         * @param {String}                          options.customization.languages.selected                        - Language selected on panel construction
         *
         * @param {Object}                          options.customization.display                                   - Object to define display settings
         * @param {Boolean}                         [options.customization.display.title=true]                      - Boolean to indicate if the title should be displayed at the top of the panel
         * @param {Boolean}                         [options.customization.display.switchLanguage=true]             - Boolean to indicate if "switch language" button should be displayed or not
         * @param {Boolean}                         [options.customization.display.characteristics.readOnly=true]   - Boolean to indicate if read-only characteristics should be displayed or not
         * @param {Boolean}                         [options.customization.display.characteristics.constraints=true] - Boolean to indicate if characteristics constraints within a popover should be displayed or not
         * @param {Boolean}                         [options.customization.display.scroller=true]                   - Boolean to indicate if scroll view should be used
         * @param {Boolean}                         [options.customization.display.buttons=true]                    - Boolean to indicate if buttons should be displayed at the bottom of the panel
         *
         * @param {String}                          [options.customization.constraintsPosition="top"]               - Position of the constraints popup according to the 'information' icon (i). It can be one of these : 'top', 'right', 'bottom' or 'left'
         *
         * @param {Object}                          options.customization.buttons                                   - Object to define the wanted buttons at the bottom of the panel. By default there are two buttons with predefined actions :
         *                                                                                                                  'Instantiate' button       : Instantiate selected template with corresponding characteristics
         *                                                                                                                  'Cancel' button
         * @param {Object}                          options.customization.buttons.cancel                            - Object to define/override 'Cancel' button, displayed in the footer
         * @param {String}                          [options.customization.buttons.cancel.label="Cancel"]           - Label of the 'Cancel' button
         * @param {String}                          [options.customization.buttons.cancel.type="default"]           - UIKit type of the 'Cancel' button. It can be one of these : 'primary', 'default', 'success', 'info', 'warning' or 'error'
         * @param {Click}                           options.customization.buttons.cancel.defaultAction              - Function that handles the click on 'Cancel' button
         * @param {Object}                          options.customization.buttons.ok                                - Object to define/override 'Ok' button, displayed in the footer
         * @param {String}                          [options.customization.buttons.ok.label="Instantiate"]          - Label of the 'Ok' button
         * @param {String}                          [options.customization.buttons.ok.type="primary"]               - UIKit type of the 'Ok' button. It can be one of these : 'primary', 'default', 'success', 'info', 'warning' or 'error'
         * @param {Click}                           [options.customization.buttons.ok.defaultAction=<Instantiate>]  - Function that handles the click on 'Ok' button
         *
         * @param {Object.<string, SearchEngine>}   [options.customization.search={}]                               - Object to define wanted autocomplete inputs (characteristic URI as key, autocomplete search function as value)
         *
         * @param {Object}                          options.customization.filters                                   - Object to define filters (filters are taken into account in this order: 1. whitelist  2. blacklist  3. order )
         * @param {String[]}                        [options.customization.filters.whitelist=[]]                    - Array of URIs to retrieve specific characteristics
         * @param {String[]}                        [options.customization.filters.blacklist=[]]                    - Array of URIs to avoid retrieving some characteristics
         * @param {String[]}                        [options.customization.filters.orderlist=["http://www.3ds.com/RDF/Corpus/Kernel/nlsLabel","http://www.3ds.com/RDF/Corpus/Kernel/nlsComment","http://www.3ds.com/RDF/Corpus/Kernel/owner']] - Array of URIs to order retrieved characteristics, according to the index in the array
         *
         * @param {Object}                          options.events                                                  - Object for events callbacks
         * @param {Function}                        [options.events.onFinishRender]                                - Function called when the template instantiation panel is rendered
         * @param {Function}                        [options.events.onRetrieveTemplateInputs]                      - Function called when characteristics for the selected template are retrieved
         */
        setup: function (options) {

            // Checking
            (new TemplateInstantiationPanelOptionsChecker(options)).checkAllOptions();
            this.options = options;


            // Building
            let that = this;
            this.launchSpinnerOnLoad();
            this.createTitle();

            if (this.options.templates.fromOptionSet) {
                this.addTemplatesFromOptionSet().then(function () {
                    that.createPanel();
                });
            } else {
                this.createPanel();
            }

        },


        /**
         * @private
         * @summary Render the panel with created components
         */
        render: function () {

            let that = this;

            // Hide spinner after everything is loaded
            this.elements.spinner.hide();

            // Title ?
            if ((this.options.customization.display.title) && (this.elements.title)) {

                this.elements.title.inject(this.container);

                // Render CharacteristicsEditorView
                this.elements.listCharacteristics.inject(this.container);

                // Render template choice view
                this.elements.templateChoice.inject(this.container);

                // Render an horizontal line separator
                new UWA.Element('hr', {
                    class: 'template-instantiation-panel-hr'
                }).inject(that.container);

            } else if (this.elements.title) {

                this.elements.title.inject(this.container);

                // Render an horizontal line separator
                new UWA.Element('hr', {
                    class: 'template-instantiation-panel-hr'
                }).inject(that.container);

                // Render template choice view
                this.elements.templateChoice.inject(this.container);

                // Render CharacteristicsEditorView
                this.elements.listCharacteristics.inject(this.container);

            }

            return this;

        },


        /**
         * @private
         * @summary Launch spinner loader while template choices & characteristics are being retrieved
         */
        launchSpinnerOnLoad: function () {

            this.elements.spinner = new Spinner({
                className: 'template-instantiation-panel-spinner',
                animate: false
            })
                .inject(this.container)
                .show();

        },


        /**
         * @private
         * @summary Add templates retrieved from an option set
         */
        addTemplatesFromOptionSet: function () {

            let that = this;

            // Prepare data to send through POST request
            let classURI = this.options.templates.fromOptionSet.taxonomy;
            let optionSetURI = this.options.templates.fromOptionSet.uri;

            let templateServices = TemplateServices.getHandle(this.options.rdfServerURL);

            function onComplete(data) {
                //console.log('TIP - addTemplatesFromOptionSet : '+JSON.stringify(data));

                let templates = data.member;
                if (templates) {
                    templates.forEach(function (t) {
                        that.options.templatesFinalList.push({
                            uri: t['@id'],
                            label: t.label
                        });
                    });
                }
            }

            if (classURI === undefined) {
                return templateServices.listTemplatesFromOptionSet({
                    // classURI: classURI,
                    optionSetURI: optionSetURI,
                    cbObject: {
                        onComplete: onComplete,
                        onFailure: function () { }
                    }
                });
            } else {
                return templateServices.listTemplatesForClasses({
                    classURI: classURI,
                    optionSetURI: optionSetURI,
                    cbObject: {
                        onComplete: onComplete,
                        onFailure: function () { }
                    }
                });
            }

        },


        /**
         * @private
         * @summary Create title being displayed at the top of the panel
         */
        createTitle: function () {

            this.elements.title = new UWA.Element('h3', {
                html: this.options.customization.title,
                class: 'template-instantiation-panel-title',
                styles: {
                    'display': (this.options.customization.display.title) ? 'table-caption' : 'none'
                }
            });

        },


        /**
         * @private
         * @summary Create the panel component
         */
        createPanel: function () {

            // Generate the view for template choices
            this.elements.templateChoice = this.createTemplateChoices();

            // Generate the view with characteristics
            this.elements.listCharacteristics = this.createCharacteristicsList(this.options.templatesFinalList[0].uri);

            // Render the panel
            this.render();

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
                    class: 'template-instantiation-panel-default-message'
                }).inject(this.container);
            }

            console.log(error);
            throw new Error(message);

        },


        /**
         * @private
         * @summary Create the characteristics list by using the CharacteristicsEditorView component
         */
        createCharacteristicsList: function (templateURI) {

            let that = this;

            return new CharacteristicsEditorView({
                rdfServerURL: this.options.rdfServerURL,
                resourceURI: templateURI,
                customization: {
                    preferenceId: this.options.customization.preferenceId,
                    classNames: ['editorViaInstantiationPanel'],
                    editMode: this.options.customization.editMode.characteristics,
                    languages: this.options.customization.languages,
                    getInfos: function () {

                        let TempServices = TemplateServices.getHandle(that.options.rdfServerURL, that.options.customization.languages.selected);

                        return TempServices.getTemplateInputsForInstantiation({
                            templateURI: templateURI,
                            toFetch: ['Name', 'ReadOnly', 'Comment', 'Value', 'AuthorizedValues', 'IsList', 'Type', 'UIType', 'Unit', 'MinMax', 'Label', 'LongString'],
                            cbObject: {
                                onComplete: function (data, status) {
                                    that.options.events.onRetrieveTemplateInputs && that.options.events.onRetrieveTemplateInputs(data, status);
                                },
                                onFailure: function (data, status, error) {
                                    throw new Error(error);
                                }
                            }
                        });

                    },
                    display: {
                        title: false,
                        switchMode: false,
                        characteristics: {
                            readOnly: this.options.customization.display.characteristics.readOnly,
                            constraints: this.options.customization.display.characteristics.constraints,
                            user: false
                        },
                        switchLanguage: this.options.customization.display.switchLanguage,
                        scroller: this.options.customization.display.scroller,
                        buttons: this.options.customization.display.buttons
                    },
                    search: this.options.customization.search,
                    constraintsPosition: this.options.customization.constraintsPosition,
                    buttons: this.options.customization.buttons,
                    filters: this.options.customization.filters,
                    onChangeSensitive: false
                },
                events: {
                    onFinishRender: function() {
                        if (!that.rendered) {
                            that.rendered = true;
                            that.options.events.onFinishRender.bind(that)();
                        }
                    },
                    onSelectLanguage: function (language) {
                        that.options.customization.languages.selected = language;
                    }
                }
            });

        },


        /**
         * @private
         * @summary Create the top part of the panel (select with template choices)
         */
        createTemplateChoices: function () {

            // Loop over all the templates to respect UIKit format
            let fieldOptions = [];
            this.options.templatesFinalList.forEach(function (t) {
                fieldOptions.push({
                    value: t.uri,
                    label: t.label || t.uri
                });
            });


            // Return the form with the 'select' field
            let that = this;
            let form = new Form({
                className: 'template-instantiation-panel-form-template',
                fields: [
                    {
                        type: 'select',
                        name: 'template',
                        label: NLS.get('TIP.template'),
                        options: fieldOptions,
                        events: {
                            onChange: function () {
                                this.setDisabled(true);
                                let template = this.getValue()[0];
                                if (template) {
                                    that.elements.listCharacteristics.remove();
                                    that.elements.listCharacteristics = that.createCharacteristicsList(template);
                                    that.elements.listCharacteristics.inject(that.container);
                                }
                                this.setDisabled(false);
                            }
                        }
                    }
                ]
            });

            // Verify if we wanna disable the input
            if (fieldOptions.length > 1) {
                form.setDisabled(!this.options.customization.editMode.templateChoice);
            } else {
                form.setDisabled(true);
            }

            return form;

        },


        /******************************************* PUBLIC METHODS *******************************************/
        /**
         * @summary Show title
         */
        showTitle: function () {
            this.elements.title.show();
        },


        /**
         * @summary Hide title
         */
        hideTitle: function () {
            this.elements.title.hide();
        },


        /**
         * @summary Set title
         * @param {String} title - String to display as the panel title
         */
        setTitle: function (title) {
            this.elements.title.setContent(title);
        },


        /**
         * @summary Show footer
         */
        showFooter: function () {
            this.options.customization.display.buttons = true;
            this.elements.listCharacteristics.showFooter();
        },


        /**
         * @summary Hide footer
         */
        hideFooter: function () {
            this.options.customization.display.buttons = false;
            this.elements.listCharacteristics.hideFooter();
        },


        /**
         * @summary Switch mode for the template choices and/or characteristics list
         * @param {Boolean} editMode            - Indicate we have to apply edit mode or not
         * @param {Boolean} onTemplateChoice    - Indicate if we apply the 'editMode' boolean on template choices
         * @param {Boolean} onCharacteristics   - Indicate if we apply the 'editMode' boolean on characteristics list
         */
        switchMode: function (editMode, onTemplateChoice, onCharacteristics) {

            // Verify if we wanna change mode on template choice input
            if (onTemplateChoice) {
                this.options.customization.editMode.templateChoice = editMode;
                this.elements.templateChoice.setDisabled(!editMode);
            }

            // Verify if we wanna change mode on form inputs
            if (onCharacteristics) {
                this.options.customization.editMode.characteristics = editMode;
                this.elements.listCharacteristics.switchMode(editMode);
            }

        },

    });

    return InstantiationPanel;

});
