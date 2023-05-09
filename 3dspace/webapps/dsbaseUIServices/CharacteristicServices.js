define('DS/dsbaseUIServices/CharacteristicServices', [
	'DS/dstoolsUIServices/dsbaseServices'
], function (commonServices) {
		/**
		* @summary characteristics accelerators for client-side APIs
		* @module DS/dsbaseUIServices/CharacteristicServices
		*/

	'use strict';
	function asArray(variable) {
		var toReturn = variable
		if (!Array.isArray(toReturn)) {
            toReturn = [variable];
		}
		return toReturn;
	}
	var charServices = {
		/**
		* @summary Returns an instance of CharacteristicServices
		* @memberof module:DS/dsbaseUIServices/CharacteristicServices
		* @returns {DS/dsbaseUIServices/CharacteristicServicesTools}
		*
		* @param {String} rootServerURL - the root server URL
		*
		* @example
		*    define('DS/MyMwebModule/MyAMDFile',
		*           ['DS/dsbaseUIServices/CharacteristicServices'], function (services) () {
		*         ...
		*         var myInstance = services.getHandle('root/server/url/');
		*         ...
		*    }
		*/
		getHandle: function(rootServerURL, lang) {
			return new CharacteristicServicesTools(rootServerURL, lang);
		}
	};

	/**
	* @summary CharacteristicServices constructor
	* @desc
	* <p> An instance  is returned by the
	* {@link module:DS/dsbaseUIServices/CharacteristicServices#getHandle}   method. </p>
	* @class DS/dsbaseUIServices/CharacteristicServicesTools
	* @global
	*
		* @param {String} rootServerURL - the root server URL
	*/
	function CharacteristicServicesTools(rootServerURL, lang) {
		this.rootServerURL = rootServerURL;
		this.headers = {
			'Accept-Language': lang
		};
		this.WAFRequestFunction = commonServices.getRequestFunction();
	}

	CharacteristicServicesTools.prototype =  {
		/**
		 * Gets the value of one (or several) characteristic on a resource (or several)
		 * @memberof DS/dsbaseUIServices/CharacteristicServicesTools#
		 *
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 * 						- rscURIs : the resource(s) holding the characteristic(s) (mandatory)
		 * 						- charNames : the characteristic(s) to retrieve can be  (mandatory)
		 *						- format : the format of the value to retrieve: default is "Display", possible values are "Primtive" (returns a value in JS language is appliable), "Raw" (returns a RDF Literal value as a string)
		 *						- cbObject : and object containing the onComplete and onFailure callbacks (optional)
		 *						- withNLS : for translated values, returns also the translated value
		 * @return {Promise} resolve/onComplete will be called with the retrieved values
		 */
		getValue: function(params) {
			var options = {
				format:"Display"
			};
			if(params.format !== undefined) {
				options.format =  params.format;
			}

			if(params.withNLS === true) {
				options.nlsTranslation = true;
			}
			var dataArray = JSON.stringify([JSON.stringify(asArray(params.rscURIs)),JSON.stringify(asArray(params.charNames)),JSON.stringify(options)]);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var headers = this.headers;
			var promise = new Promise(function (resolve, reject) {
				WAFReq (commonServices.buildURL(rootServerURL,"dsbase:characteristic.getValue", params.urlParams),{
					method: 'POST',
					type: 'json',
					headers: Object.assign(headers, {
						'Content-Type': 'application/json'
					}),
					data: dataArray,
					onComplete: function(data, status) {
						data.value = JSON.parse(data.value);
						if(params.cbObject !== undefined) {
							params.cbObject.onComplete(data,status);
						}
						resolve(data.value);
					},
					onFailure: function(data, status, error){
						if(params.cbObject !== undefined) {
							params.cbObject.onFailure(data, status, error);
						}
						reject(error);
					},
					timeout:300000
				});
			});
			return promise;
		},
		/**
		 * Sets the value of one (or several) characteristic on a resource (or several)
		 * @memberof DS/dsbaseUIServices/CharacteristicServicesTools#
		 *
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 *						- rscURIs : the resource(s) holding the characteristic(s) (mandatory)
		 * 						- charNames : the characteristic(s) to retrieve can be  (mandatory)
		 * 						- charValues : the value(s) to set  (mandatory)
		 *						- hasUnit : indicates if the value is given as a string with unit, in which case it will be parsed (mandatory)
		 *						- cbObject : and object containing the onComplete and onFailure callbacks (optional)
		 * @return {Promise} resolve/onComplete will be called with the retrieved values
		 */
		setValue: function(params) {
			var dataArray = JSON.stringify([JSON.stringify(asArray(params.rscURIs)),JSON.stringify(asArray(params.charNames)),JSON.stringify(asArray(params.charValues)),JSON.stringify(params.hasUnit)]);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var headers = this.headers;
			var promise = new Promise(function (resolve, reject) {
				var request = WAFReq(commonServices.buildURL(rootServerURL,"dsbase:characteristic.setValue", params.urlParams),{
					method: 'POST',
					type: 'json',
					headers: Object.assign(headers, {
						'Content-Type': 'application/json'
					}),
					data: dataArray,
					onComplete: function(data, status) {
						// data.value = JSON.parse(data.value);
						if(params.cbObject !== undefined) {
							params.cbObject.onComplete(data,status);
						}
						resolve(data);
					},
					onFailure: function(data, status, error){
						if(params.cbObject !== undefined) {
							//AJY3 3/9/2020 Send request in response so that http code is accessible to calling function.
							params.cbObject.onFailure({data : data, request : request}, status, error);
						}
						reject(error);
					},
					timeout:300000
				});
			});
			return promise;
		},
		/**
		 * Patches the value of one characteristic on a resource
		 * @memberof DS/dsbaseUIServices/CharacteristicServicesTools#
		 *
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 * 						- {String} resourceURI   : the resource holding the characteristic (mandatory)
		 * 						- {String} attributeName : the characteristic to patch (mandatory)
		 * 						- {Array}  actions       : the actions to do (mandatory)
		 * Refer to dsbase:characteristic.patchValue for more details on the supported actions
		 *
		 * @return {Promise} resolve/onComplete will be called with the retrieved values
		 *
		 * @example
     * 		var actions = [];
     * 		actions.push({"value": 4, "action": "APPEND"});
     * 		actions.push({"value": ["ten", "eleven"], "action": "APPEND"});
		 * 		actions.push({"value": ["nine", {"@id": "dscust:elem2"}], "action": "REMOVE"});
		 * 		actions.push({"value": [{"@id": "dscust:elem5"}], "action": "REMOVE_ALL"});
		 * 		charServices.patchValue('dscust:myResource', 'dscust:myListProperty', actions);
		 */
		patchValue: function(params) {
			var dataArray = JSON.stringify([
				params.resourceURI,
				params.attributeName,
				JSON.stringify(params.actions)
			]);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var headers = this.headers;
			var promise = new Promise(function (resolve, reject) {
				WAFReq (commonServices.buildURL(rootServerURL,"dsbase:characteristic.patchValue", params.urlParams),{
					method: 'POST',
					type: 'json',
					headers: Object.assign(headers, {
						'Content-Type': 'application/json'
					}),
					data: dataArray,
					onComplete: function(data, status) {
						data.value = JSON.parse(data.value);
						if(params.cbObject !== undefined) {
							params.cbObject.onComplete(data,status);
						}
						resolve(data.value);
					},
					onFailure: function(data, status, error){
						if(params.cbObject !== undefined) {
							params.cbObject.onFailure(data, status, error);
						}
						reject(error);
					},
					timeout:300000
				});
			});
			return promise;
		},
		/**
		 * Lists all characteristics for a resource (or several)
		 * @memberof DS/dsbaseUIServices/CharacteristicServicesTools#
		 *
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 * 						- rscURIs : the resource(s) to introspect (mandatory)
		 *                      - cbObject : and object containing the onComplete and onFailure callbacks (optional)
		 * @return {Promise} resolve/onComplete will be called with the retrieved values
		 */
		list: function(params) {
			var dataArray = JSON.stringify([JSON.stringify(asArray(params.rscURIs))]);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var headers = this.headers;
			var promise = new Promise(function (resolve, reject) {
				WAFReq (commonServices.buildURL(rootServerURL,"dsbase:characteristic.list", params.urlParams), {
					method: 'POST',
					headers: Object.assign(headers, {
						'Content-Type': 'application/json'
					}),
					type: 'json',
					data: dataArray,
					onComplete: function(data, status) {
						data.value = JSON.parse(data.value);
						if(params.cbObject !== undefined) {
							params.cbObject.onComplete(data,status);
						}
						resolve(data.value);
					},
					onFailure: function(data, status, error){
						if(params.cbObject !== undefined) {
							params.cbObject.onFailure(data, status, error);
						}
						reject(error);
					},
					timeout:300000
				});
			});
			return promise;
		},
		/**
		 * Returns the informations indicated for a given characteristic
		 * @memberof DS/dsbaseUIServices/CharacteristicServicesTools#
		 *
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 * 						- rscURIs : the resource(s) to introspect (mandatory)
		 * 						- charNames : the characteristic(s) to retrieve  (mandatory)
		 * 						- toFetch : a list of the infos to retrieved from the list ('AuthorizedValues','AuthorizedValuesNls','MinMax','Value','ReadOnly','User','Type','Comment','Unit','LongString','IsList') (mandatory)
		 *                      - blacklist : a list of characteristics to not retrieve
		 * 						- format : format to use to get characteristics value (optional)
		 * 						- cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 * @return {Array} an array containing the association between the characteristics and their infos - through the onComplete callback!
		 */
		getInfos: function(params) {
			var dataArray = JSON.stringify([
				JSON.stringify(asArray(params.rscURIs)),
				JSON.stringify(asArray(params.charNames)),
				JSON.stringify(asArray(params.toFetch)),
				JSON.stringify(asArray(params.blacklist)),
				params.format
			]);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var headers = this.headers;
			var promise = new Promise(function (resolve, reject) {
				WAFReq (commonServices.buildURL(rootServerURL,"dsbase:characteristic.getInfos", params.urlParams), {
					method: 'POST',
					headers: Object.assign(headers, {
						'Content-Type': 'application/json'
					}),
					type: 'json',
					data: dataArray,
					onComplete: function(data, status) {
						data.value = JSON.parse(data.value);
						if(params.cbObject !== undefined) {
							params.cbObject.onComplete(data,status);
						}
						resolve(data.value);
					},
					onFailure: function(data, status, error){
						if(params.cbObject !== undefined) {
							params.cbObject.onFailure(data, status, error);
						}
						reject(error);
					},
					timeout:300000
				});
			});
			return promise;
		},
		/**
		 * Returns the UI types for a list of RDF types
		 * @memberof DS/dsbaseUIServices/CharacteristicServicesTools#
		 *
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 * 						- types : a list of types (mandatory)
		 * 						- cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 * @return {Array} an array containing the UI Types associated to the given types
		 */
		getUITypes: function (params) {
			var dataArray = JSON.stringify([
				JSON.stringify(asArray(params.types))
			]);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var headers = this.headers;
			var promise = new Promise(function (resolve, reject) {
				WAFReq(commonServices.buildURL(rootServerURL, "dsbase:characteristic.getUITypes", params.urlParams), {
					method: 'POST',
					headers: Object.assign(headers, {
						'Content-Type': 'application/json'
					}),
					type: 'json',
					data: dataArray,
					onComplete: function (data, status) {
						data.value = JSON.parse(data.value);
						if (params.cbObject !== undefined) {
							params.cbObject.onComplete(data, status);
						}
						resolve(data.value);
					},
					onFailure: function (data, status, error) {
						if (params.cbObject !== undefined) {
							params.cbObject.onFailure(data, status, error);
						}
						reject(error);
					},
					timeout: 300000
				});
			});
			return promise;
		},
		/**
		 * Deletes a characteristic on a resource
		 * @memberof DS/dsbaseUIServices/CharacteristicServicesTools#
		 *
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 * 						- rscURIs : the resource(s) to handle (mandatory)
		 * 						- charNames : the characteristic(s) to delete (mandatory)
		 *                      - cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 */
        /* modification FRH 18-9-2019 suppression of the delete method
		delete: function( params) {
			var dataArray = JSON.stringify([JSON.stringify(asArray(params.rscURIs)),JSON.stringify(asArray(params.charNames))]);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var headers = this.headers;
			var promise = new Promise(function (resolve, reject) {
				WAFReq (commonServices.buildURL(rootServerURL,"dsbase:characteristic.delete"), {
					method: 'POST',
					headers: Object.assign(headers, {
						'Content-Type': 'application/json'
					}),
					type: 'json',
					data: dataArray,
					onComplete: function(data, status) {
						if(params.cbObject !== undefined) {
							params.cbObject.onComplete(data,status);
						}
						resolve(data);
					},
					onFailure: function(data, status, error){
						if(params.cbObject !== undefined) {
							params.cbObject.onFailure(data, status, error);
						}
						reject(error);
					},
					timeout:300000
				});
			});
			return promise;
		},*/
		/**
		 * Checks the validity of a characteristic value for a resource
		 * @memberof DS/dsbaseUIServices/CharacteristicServicesTools#
		 *
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 * 						- rscURIs : the resource(s) to check on (mandatory)
		 * 						- charNames : the characteristic(s) to test for (mandatory)
		 * 						- charValues : the value(s) to test for
		 *						- cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 */
		isValueValid: function(params) {
			// FIXME: no unit handling here...
			var dataArray = JSON.stringify([JSON.stringify(asArray(params.rscURIs)),JSON.stringify(asArray(params.charNames)),JSON.stringify(asArray(params.charValues))]);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var headers = this.headers;
			var promise = new Promise(function (resolve, reject) {
				WAFReq (commonServices.buildURL(rootServerURL,"dsbase:characteristic.isValueValid", params.urlParams),{
					method: 'POST',
					type: 'json',
					headers: Object.assign(headers, {
						'Content-Type': 'application/json'
					}),
					data: dataArray,
					onComplete: function(data, status) {
						data.value = JSON.parse(data.value);
						if(params.cbObject !== undefined) {
							params.cbObject.onComplete(data,status);
						}
						resolve(data.value);
					},
					onFailure: function(data, status, error){
						if(params.cbObject !== undefined) {
							params.cbObject.onFailure(data, status, error);
						}
						reject(error);
					},
					timeout:300000
				});
			});
			return promise;
		},


		/**
		 * Set a header value
		 * @param {String} key		- Entity header
		 * @param {String} value	- Value associated to the header
		 */
		setHeader: function(key, value) {
			this.headers[key] = value;
		}

	};
	CharacteristicServicesTools.prototype.constructor = CharacteristicServicesTools;
	return charServices;
});
