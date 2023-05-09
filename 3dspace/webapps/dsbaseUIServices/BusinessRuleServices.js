define('DS/dsbaseUIServices/BusinessRuleServices', [
		"DS/dsbaseUIServices/OperationServices",
	"DS/dstoolsUIServices/dsbaseServices"
], function (operationToolbox, commonServices) {
		/**
		* @summary customization accelerators for client-side APIs
		* @module DS/dsbaseUIServices/BusinessRuleServices
		*/

	'use strict';
	function asArray(variable) {
		var toReturn = variable
		if (!Array.isArray(toReturn)) {
            toReturn = [variable];
		}
		return toReturn;
	}
	var businessRuleServices = {
		/**
		* @summary Returns an instance of BusinessRuleServices
		* @memberof module:DS/dsbaseUIServices/BusinessRuleServices
		* @returns {DS/dsbaseUIServices/BusinessRuleServicesTools}
		*
		* @param {String} rootServerURL - the root server URL
		*
		* @example
		*    define('DS/MyMwebModule/MyAMDFile',
		*           ['DS/dsbaseUIServices/BusinessRuleServices'], function (services) () {
		*         ...
		*         var myInstance = services.getHandle('root/server/url/');
		*         ...
		*    }
		*/
		getHandle: function(rootServerURL) {
			return new BusinessRuleServicesTools(rootServerURL);
		}
	};

	/**
	* @summary BusinessRuleServices constructor
	* @desc
	* <p> An instance  is returned by the
	* {@link module:DS/dsbaseUIServices/BusinessRuleServices#getHandle}   method. </p>
	* @class DS/dsbaseUIServices/BusinessRuleServicesTools
	* @global
	*
		* @param {String} rootServerURL - the root server URL
	*/
	function BusinessRuleServicesTools(rootServerURL) {
		this.rootServerURL = rootServerURL;
		this.WAFRequestFunction = commonServices.getRequestFunction();
		this.opServices = operationToolbox.getHandle(rootServerURL);
	}

	BusinessRuleServicesTools.prototype = {
		/**
		 * Executes a business rule
		 * @memberof DS/dsbaseUIServices/BusinessRuleServicesTools#
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 * 						- BusinessRuleURI : the URI of the DS business rule to invoke
		 * 						- ThisObjectURI : the URI of the resource on which the business rule is executed
		 * 						- InputParameters : a JSON object to pass input parameters
		 * @return {Promise} resolve/onComplete will be called with the retrieved values
		 */
	    execute: function (params) {
				var parametersOfOperation = {
					"BusinessRule"   : params.BusinessRuleURI,
					"ThisObject"     : params.ThisObjectURI,
					"InputParameters": params.InputParameters
				};

				return this.opServices.run({
					operationVersionURI: 'dsbusinessrule:executeBusinessRule_V1',
					inputs             : parametersOfOperation,
					urlParams          : params.urlParams,
					cbObject           : params.cbObject
				});
		},

		/**
		 * Retrieves an option set
		 * @memberof DS/dsbaseUIServices/BusinessRuleServicesTools#
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 *						- OptionSetURI : the URI of the DS option set to retrieve
		 * @return {Promise} resolve/onComplete will be called with the retrieved values
		 */
		retrieveOptionSet: function (params) {
			var parametersOfOperation = {
				OptionSet: params.OptionSetURI
			 };

			return this.opServices.run({
				operationVersionURI: 'dsbusinessrule:retrieveOptionSet_V1',
				inputs             : parametersOfOperation,
				urlParams          : params.urlParams,
				cbObject           : params.cbObject
			});
		},

		/**
		 * activates a customer extension
		 * @param  {Object}  params - an object containing the following fields:
		 *						- CustomerExtensionURI : the URI of the DS customer extension
		 * 						- userURI: the URI of the user whom to assign the customer extension
		 * @return {Promise} resolve/onComplete will be called with the retrieved values
		 */
		activateCustomerExtension: function (params) {

			var parametersOfOperation = {
				CustomerExtension : params.CustomerExtensionURI,
				PersonOrPlatform : params.userURI
			};

			console.log('activateCustomerExtension - parametersOfOperation = ' + JSON.stringify(parametersOfOperation));

			return this.opServices.run({
				operationVersionURI: 'dsbusinessrule:activateCustomerExtension_V1',
				inputs             : parametersOfOperation,
				urlParams          : params.urlParams,
				cbObject           : params.cbObject
			});
	   },

		/**
		 * deactivates a customer extension
		 * @param  {Object}  params - an object containing the following fields:
		 *          	- CustomerExtensionURI : the URI of the DS customer extension
		 * 						- userURI: the URI of the user whom to deactivate the customer extension
		 * @return {Promise} resolve/onComplete will be called with the retrieved values
		 */
		deactivateCustomerExtension: function (params) {

			var parametersOfOperation = {
				CustomerExtension :params.CustomerExtensionURI,
				PersonOrPlatform : params.userURI
			 };

			console.log('deactivateCustomerExtension - parametersOfOperation = ' + JSON.stringify(parametersOfOperation));

			return this.opServices.run({
				operationVersionURI: 'dsbusinessrule:deactivateCustomerExtension_V1',
				inputs             : parametersOfOperation,
				urlParams          : params.urlParams,
				cbObject           : params.cbObject
			});
	   },



		/**
		 * List the customer extensions
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 *						- cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 * @return {Array} an object the list of customer extensions - through the onComplete callback!
		 */
		listCustomerExtensions: function(params) {
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var promise = new Promise(function (resolve, reject) {
				WAFReq (commonServices.buildURL(rootServerURL,"dsbusinessrule:listCustomerExtensions", params.urlParams),{
					method: 'POST',
					type: 'json',
					headers: { 'Content-Type':'application/json'},
					data: "[\"\"]",
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
		 * List responsibilities from an option set
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 *						- optionSet : URI of the option set containing the list of responsibilities
		 *						- languagePref : language in which labels should be retrieved. Default : 'en'
		 *                      - cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 * @return {Array} an object containing the list of responsibilities retrieved from the specified option set- through the onComplete callback!
		 */
		listResponsibilities: function(params) {
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var promise = new Promise(function (resolve, reject) {
				WAFReq(commonServices.buildURL(rootServerURL, 'dsbusinessrule:listResponsibilities'), {
					method: 'POST',
					type: 'json',
					headers: { 'Content-Type':'application/json',
					'Accept-Language' : params.languagePref || 'en',
					//IR-770133-3DEXPERIENCER2021x AJY3 Add header to be compliant with CSRF.
					'X-Requested-With' : 'XMLHttpRequest'},
					data: JSON.stringify([params.optionSet, 'dsaccess:setOfResponsibilities']),
					onComplete: function(data, status) {
						if (params.cbObject !== undefined) {
							params.cbObject.onComplete(data, status);
						}
						resolve(data);
					},
					onFailure: function(data, status, error){
						if (params.cbObject !== undefined) {
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
		 * List services
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 *						- cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 * @return {Array} an object the list of customer extensions - through the onComplete callback!
		 */
		listServices: function(params) {
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var promise = new Promise(function (resolve, reject) {
				WAFReq (commonServices.buildURL(rootServerURL,"dsbusinessruleOMS:listServices", params.urlParams),{
					method: 'POST',
					type: 'json',
					headers: { 'Content-Type':'application/json'},
					data: "[\"\"]",
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
		 * List ontologies
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 *						- cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 * 						- services : array of services to filter on
		 * @return {Array} an object the list of customer extensions - through the onComplete callback!
		 */
		listOntologies: function(params) {
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var dataArray = JSON.stringify([JSON.stringify(params.services)]);
			var promise = new Promise(function (resolve, reject) {
				WAFReq (commonServices.buildURL(rootServerURL,"dsbusinessruleOMS:listOntologies", params.urlParams),{
					method: 'POST',
					type: 'json',
					headers: { 'Content-Type':'application/json'},
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
		 * Deploy ontologies
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 *						- cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 * 						- serviceName : array of services to filter on
		 * @return {Array} an object the list of customer extensions - through the onComplete callback!
		 */
		deployOntologies: function(params) {
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var dataArray = JSON.stringify("{\"serviceName\":\""+params.serviceName+"\",\"ontologyURI\":\""+params.ontologyURI+"\"}");
			var promise = new Promise(function (resolve, reject) {
				WAFReq (commonServices.buildURL(rootServerURL,"dsbusinessruleOMS:deployCusto", params.urlParams),{
					method: 'POST',
					type: 'json',
					headers: { 'Content-Type':'application/json'},
					data: dataArray,
					onComplete: function(data, status) {
						if (params.cbObject !== undefined) {
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
		 * Search users in DB
		 *
		 * @param  {Object}  params - an object containing the following fields:
		 *						- cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
		 * @return {Array} an object the list of customer extensions - through the onComplete callback!
		 */
		searchUsers: function(params) {
			var WAFReq = this.WAFRequestFunction;
			var rootServerURL = this.rootServerURL;
			var dataArray = JSON.stringify([params.text]);
			var promise = new Promise(function (resolve, reject) {
				WAFReq (commonServices.buildURL(rootServerURL,"dsbusinessrule:searchUsers", params.urlParams),{
					method: 'POST',
					type: 'json',
					headers: { 'Content-Type':'application/json'},
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
					timeout:10000
				});
			});
			return promise;
		}
	};
	BusinessRuleServicesTools.prototype.constructor = BusinessRuleServicesTools;
	return businessRuleServices;
});
