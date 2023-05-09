/* eslint strict: "off" */

define('DS/dsbaseUIServices/TemplateServices', [
	"DS/dstoolsUIServices/dsbaseServices",
	"DS/dsbaseUIServices/OperationServices"
], function (commonServices,operationToolbox) {
		/**
		* @summary Template corpus accelerators for client-side APIs
		* @module DS/dsbaseUIServices/TemplateServices
		*/


	'use strict';
	function asArray(variable) {
		var toReturn = variable
		if (!Array.isArray(toReturn)) {
            toReturn = [variable];
		}
		return toReturn;
	}
	var templateServices = {
		/**
		* @summary Returns an instance of TemplateServices
		* @memberof module:DS/dsbaseUIServices/TemplateServices
		* @returns {DS/dsbaseUIServices/TemplateServicesTools}
		*
		* @param {String} rootServerURL - the root server URL
		*
		* @example
		*    define('DS/MyMwebModule/MyAMDFile',
		*           ['DS/dsbaseUIServices/TemplateServices'], function (services) () {
		*         ...
		*         var myInstance = services.getHandle('root/server/url/');
		*         ...
		*    }
		*/
		getHandle: function(rootServerURL, lang) {
			return new TemplateServicesTools(rootServerURL, lang);
		}
	};

	/**
	* @summary TemplateServices constructor
	* @desc
	* <p> An instance  is returned by the
	* {@link module:DS/dsbaseUIServices/TemplateServices#getHandle}   method. </p>
	* @class DS/dsbaseUIServices/TemplateServicesTools
	* @global
	*
	* @param {String} rootServerURL - the root server URL
	*/
	function TemplateServicesTools(rootServerURL, lang) {
		this.rootServerURL = rootServerURL;
		this.WAFRequestFunction = commonServices.getRequestFunction();
		this.operationServices = operationToolbox.getHandle(rootServerURL, lang);
		this.headers = {
			'Accept-Language': lang
		};
	}
	TemplateServicesTools.prototype =  {
		/**
		 * Instantiates a Template
		 * @memberof DS/dsbaseUIServices/TemplateServicesTools#
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 * 						- templateURI : the template to instantiate (mandatory)
		 * 						- inputs : the inputs values under the form {"inputName":"value", ...} (mandatory)
		 *            - cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 *                      - instanceURI : URI of the created instance (optional, will use a UUID if not provided)
		 * 						- securedContext : currently unused (optional)
		 * 						- noUpdate : currently unused (optional)
		 * @return {String} a URI for the created instance - through the onComplete callback!
		 */
		instantiate: function(params) {
			return this.operationServices.run({
				operationVersionURI:'dsop:new_V1',
				inputs: {
					template:       {'@id': params.templateURI},
					inputs:         JSON.stringify(params.inputs),
					instanceURI:    params.instanceURI,
					securedContext: ((params.securedContext !== undefined) ? {'@id': params.securedContext} : undefined),
					noUpdate:       params.noUpdate
				},
				urlParams : params.urlParams,
				cbObject:params.cbObject, timeout:100000
			});
		},
		/**
		 * Lists the Templates available to create a resource individual of a given set of classes
		 * @memberof DS/dsbaseUIServices/TemplateServicesTools#
		 *
		 * @param {Object} params - an object containing the following fields:
		 *						- classURI   : the class for which you want to create individuals - can be an array
		 *						- optionSetURI: The URI of the option set to consider when searching for instantiable templates
		 * @return {Array} a map giving a list of templates that can create the individuals of each class
		 */
		listTemplatesForClasses: function(params) {
			var dataArray = JSON.stringify([params.classURI]);
			if(params.optionSetURI !== undefined && params.optionSetURI !== null) {
				dataArray = JSON.stringify([params.classURI,params.optionSetURI]);
			}
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var headers = this.headers;
			console.log('listTemplatesForClasses - dataArray : ' + dataArray);
			var promise = new Promise(function (resolve, reject) {
				WAFReq (commonServices.buildURL(rootServerURL,"dsbase:listTemplatesForClass", params.urlParams),{
					method: 'POST',
					type: 'json',
					headers: Object.assign(headers, {
						'Content-Type': 'application/json'
					}),
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
		},
		/**
		 * Lists the Templates available to create a resource individual of a given set of classes
		 * @memberof DS/dsbaseUIServices/TemplateServicesTools#
		 *
		 * @param {Object} params - an object containing the following fields:
		 * 			- classURI   : the class for which you want to create individuals - Array
		 *      - options     : The options to consider (refer to i3DXRDFBaseOperator\dsoperatorCode.m\src\template-deploy.js for details)
		 * @return {Array} a map giving a list of templates that can create the individuals of each class
		 */
		listTemplates: function(params) {
			var dataArray = [];
			var classURI = params.classURI;
			dataArray.push(classURI);
			if(params.options !== undefined && params.options !== null) {
				var options = JSON.stringify(params.options);
				dataArray.push(options);
			}
			dataArray = JSON.stringify(dataArray);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var headers = this.headers;
			console.log('listTemplatesForClasses - dataArray : ' + dataArray);
			var promise = new Promise(function (resolve, reject) {
				WAFReq (commonServices.buildURL(rootServerURL,"dsbase:listTemplates", params.urlParams),{
					method: 'POST',
					type: 'json',
					headers: Object.assign(headers, {
						'Content-Type': 'application/json'
					}),
					data: dataArray,
					onComplete: function(data, status) {
						if(params.cbObject !== undefined) {
							params.cbObject.onComplete(data, status);
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
		},
		/**
		 * Lists the Templates available to create a resource individual of a given option set
		 * @memberof DS/dsbaseUIServices/TemplateServicesTools#
		 *
		 * @param {Object} params - an object containing the following fields:
		 *						- optionSetURI: The URI of the option set to consider when searching for instantiable templates
		 * @return {Array} a map giving a list of templates that can create the individuals of each class
		 */
		listTemplatesFromOptionSet: function(params) {
			var dataArray = JSON.stringify([params.optionSetURI]);

			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var headers = this.headers;
			//console.log('listTemplatesFromOptionSet - dataArray : ' + dataArray);
			var promise = new Promise(function (resolve, reject) {
				WAFReq (commonServices.buildURL(rootServerURL,"dsbase:listTemplatesFromOptionSet", params.urlParams),{
					method: 'POST',
					type: 'json',
					headers: Object.assign(headers, {
						'Content-Type': 'application/json'
					}),
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
		},
		/**
		 * Get inputs needed for instantiation
		 * @memberof DS/dsbaseUIServices/TemplateServicesTools#
		 *
		 * @param {Object} params - an object containing the following fields:
		 * 			- templateURI   : the template URI
		 *      	- toFetch     	: a list of the infos to retrieved from the list ('AuthorizedValues','AuthorizedValuesNls','MinMax','Value','ReadOnly','User','Type','Comment','Unit','LongString','IsList','IsComputed')
		 * @return {Array} an array containing the the characteristics and their infos - through the onComplete callback!
		 */
		getTemplateInputsForInstantiation: function (params) {
			var dataArray = JSON.stringify([
				params.templateURI,
				JSON.stringify(asArray(params.toFetch))
			]);
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var headers = this.headers;
			var promise = new Promise(function (resolve, reject) {
				WAFReq(commonServices.buildURL(rootServerURL, "dsbase:getTemplateInputsForInstantiation", params.urlParams), {
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
		 * Set a header value
		 * @param {String} key		- Entity header
		 * @param {String} value	- Value associated to the header
		 */
		setHeader: function (key, value) {
			this.headers[key] = value;
		}

	};
	TemplateServicesTools.prototype.constructor = TemplateServicesTools;
	return templateServices;
});
