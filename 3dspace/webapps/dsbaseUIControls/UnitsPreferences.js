define('DS/dsbaseUIControls/UnitsPreferences', [
    'UWA/Class',
    'UWA/Utils/Cookie'
], function (
    Class,
    Cookie
) {

    'use strict';

    /**
     * @summary Units preferences manager
     * @extends UWA/Class
     */
    let UnitsPreferences = Class.extend({


        /**
         * Initialize variable to have an operational units preferences manager
         * @param {Object}  options     - Options needed to initialize the manager
         * @param {Object}  options.id  - Id of the component we want to retrieve the preferences for
         * @override 
         */
        init: function(options) {

            this.viewId = options.id + '-units';

            if (!this.hasViewPreferences()) {
                this.setCookieValue({});
            }

        },

        /**
         * Describe a characteristic URI
         * @typedef {CharacteristicURI}
         * @type {String}
         */

        /**
         * Describe the units preferences
         * @typedef {UnitsPreferences}
         * @type {Object}
         * @param {String} unitURI      - Unit URI (for example: 'http://www.3ds.com/RDF/Corpus/dsqt/meter')
         * @param {String} prefixURI    - Prefix URI (for example: 'http://www.3ds.com/RDF/Corpus/dsqt/milli')
         */

        /**
         * Describe the type of object stored in a cookie
         * @typedef Preferences
         * @type {Object.<CharacteristicURI,UnitsPreferences}
         */

        /**
         * @summary Get the complete cookie value(i.e. the dictionary with characteristics URIs as keys & units preferences as values)
         * @returns {Preferences} - Units preferences for all characteristics
         */
        getCookieValue: function() {
            return JSON.parse(Cookie.get(this.viewId));
        },


        /**
         * @summary Set the complete cookie value(i.e. the dictionary with characteristics URIs as keys & units preferences as values)
         * @param {Preferences} value - New dictionary to use for setting cookie value
         */
        setCookieValue: function(value) {

            let expirationDate = new Date();
            expirationDate.setFullYear(expirationDate.getFullYear() + 1);

            Cookie.set(
                this.viewId,
                JSON.stringify(value),
                {
                    expires: expirationDate
                }
            );

        },


        /**
         * @summary Indicate if some preferences exist for the given view id
         * @returns {Boolean} - Flag to indicate if cookie exists
         */
        hasViewPreferences: function() {
            let preferences = Cookie.get(this.viewId);
            return (preferences !== undefined);
        },


        /**
         * Indicate if a given characteristic has a preference previously stored
         * @param {String} characteristicURI - Characteristic URI
         * @returns {Boolean} - Flag to indicate if preferences exist for given characteristic
         */
        hasPreference: function(characteristicURI) {
            let preferences = this.getCookieValue();
            return (preferences[characteristicURI] !== undefined);
        },


        /**
         * Get the unit preference for a given characteristic
         * @param {String} characteristicURI - Characteristic URI
         * @returns {String|undefined} - Unit URI if exists. Otherwise, it returns 'undefined'.
         */
        getUnitPreference: function(characteristicURI) {

            if (this.hasPreference(characteristicURI)) {
                let preferences = this.getCookieValue();
                return preferences[characteristicURI].unitURI;
            }

            return undefined;
        },


        /**
         * Get the prefix unit preference for a given characteristic
         * @param {String} characteristicURI - Characteristic URI
         * @returns {String|undefined} - Prefix unit URI if exists. Otherwise, it returns 'undefined';
         */
        getPrefixPreference: function(characteristicURI) {

            if (this.hasPreference(characteristicURI)) {
                let preferences = this.getCookieValue();
                return preferences[characteristicURI].prefixURI;
            }

            return undefined;
        },


        /**
         * Get all preferences for a given characteristic
         * @param {String} characteristicURI - Characteristic URI
         * @returns {UnitsPreferences|undefined} - Object with all preferences if exists. Otherwise, it returns 'undefined'.
         */
        getPreferences: function(characteristicURI) {

            if (this.hasPreference(characteristicURI)) {
                let preferences = this.getCookieValue();
                return preferences[characteristicURI];
            }

            return undefined;
        },


        /**
         * Set the unit preference for a given characteristic
         * @param {String} characteristicURI    - Characteristic URI
         * @param {Stirng} unitURI              - Unit URI 
         */
        setUnitPreference: function (characteristicURI, unitURI) {

            let preferences = this.getCookieValue();

            if (this.hasPreference(characteristicURI)) {
                preferences[characteristicURI].unitURI = unitURI;
            } else {
                preferences[characteristicURI] = {
                    unitURI: unitURI
                };
            }

            this.setCookieValue(preferences);

        },


        /**
         * Set the prefix unit preference for a given characteristic
         * @param {String} characteristicURI    - Characteristic URI
         * @param {String} prefixURI            - Prefix unit URI 
         */
        setPrefixPreference: function (characteristicURI, prefixURI) {

            let preferences = this.getCookieValue();

            if (this.hasPreference(characteristicURI)) {
                preferences[characteristicURI].prefixURI = prefixURI;
            } else {
                preferences[characteristicURI] = {
                    prefixURI: prefixURI
                };
            }

            this.setCookieValue(preferences);

        },


        /**
         * Set all preferences for a given characteristic
         * @param {String} characteristicURI    - Characteristic URI
         * @param {String} unitURI              - Unit URI
         * @param {String} prefixURI            - Prefix unit URI
         */
        setPreferences: function (characteristicURI, unitURI, prefixURI) {
            this.setUnitPreference(characteristicURI, unitURI);
            this.setPrefixPreference(characteristicURI, prefixURI);
        }

    });

    return UnitsPreferences;

});
