'use strict';

define('DS/dsbaseUIServices/OperationServices', [
  'DS/dstoolsUIServices/dsbaseServices'], function (commonServices) {
		/**
		* @summary Operation corpus accelerators for client-side APIs
		* @module DS/dsbaseUIServices/OperationServices
		*/

    'use strict';

    var opServices = {
      /**
      * @summary Returns an instance of OperationServices
      * @memberof module:DS/dsbaseUIServices/OperationServices
      * @returns {DS/dsbaseUIServices/OperationServicesTools}
      *
		* @param {String} rootServerURL - the root server URL
      *
      * @example
      *    define('DS/MyMwebModule/MyAMDFile',
      *           ['DS/dsbaseUIServices/OperationServices'], function (services) () {
      *         ...
      *         var myInstance = services.getHandle('root/server/url/');
      *         ...
      *    }
      */
      getHandle: function (rootServerURL, lang) {
        return new OperationServicesTools(rootServerURL, lang);
      }
    };

    /**
    * @summary OperationServices constructor
    * @desc
    * <p> An instance  is returned by the
    * {@link module:DS/dsbaseUIServices/OperationServices#getHandle}   method. </p>
    * @class DS/dsbaseUIServices/OperationServicesTools
    * @global
    *
		* @param {String} rootServerURL - the root server URL
    */
    function OperationServicesTools(rootServerURL, lang) {
      this.rootServerURL = rootServerURL;
      this.headers =  {
        'Accept-Language': lang
      };
      this.WAFRequestFunction = commonServices.getRequestFunction();
    }

    OperationServicesTools.prototype = {
      /**
       * Remotely executes an Operation Version on the provided inputs and returns the execution result
		   * @memberof DS/dsbaseUIServices/OperationServicesTools#
       *
       *
       * @param  {Object}  params - an object containing the following fields:
       * 						- operationVersionURI : the operation version to execute (mandatory)
       * 						- inputs   : the inputs values under the form {"inputName":"value", ...} (mandatory)
       * 						- options  : options for the run (optional)
       *            - cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
       *            - timeout  : the maximum time in ms for the operation to be executed (optional, 30s by default)
       *            - urlParams: query parameters to add at the web service invocation (optional)
       *                         $mask: anApplicativeMask
       * @return {Object} a map containing the output values if the operation was successful - through the onComplete callback!
       */
      run: function (params) {
        if (params.timeout === undefined) {
          params.timeout = 30000;
        }
        var payload = [JSON.stringify(params.inputs)];
        if (params.options !== undefined)
          payload.push(JSON.stringify(params.options));

        var urlParams = (typeof params.urlParams === 'object') ? params.urlParams : {};
        urlParams["$this"] = params.operationVersionURI;

        var dataArray = JSON.stringify(payload);
        var wafReq = this.WAFRequestFunction;
        var rootServerURL = this.rootServerURL;
        var headers = this.headers;
        var promise = new Promise(function (resolve, reject) {
          var request = wafReq(commonServices.buildURL(rootServerURL, "dsbase:OperationVersion.run", urlParams), {
            method: 'POST',
            type: 'json',
            headers: Object.assign(headers, {
              'Content-Type': 'application/json'
            }),
            data: dataArray,
            onComplete: function (data, status) {
              if (data.value !== undefined) // value may be undefined if the Operation does not produce any outputs !
                data.value = JSON.parse(data.value);

              if (params.cbObject !== undefined)
                params.cbObject.onComplete(data, status);

              resolve(data);
            },
            onFailure: function (data, status, error) {
              if (params.cbObject !== undefined) {
                //AJY3 9/12/2019 Send request in response so that http code is accessible to calling function.
                params.cbObject.onFailure({data : data, request : request}, status, error);
              }
              reject(error);
            },
            timeout: params.timeout
          });
        });
        return promise;
      },

      /**
       * Posts an Operation Version task execution request with the provided inputs and returns the task identifier.
		   * @memberof DS/dsbaseUIServices/OperationServicesTools#
       *
       * @see OperationServices.pollTask
       * @see OperationServices.cancelTask
       *
       * WARNING: This function (and its associates: pollTask and cancelTask) only works with a RDF-persistency backend. It does not properly work with RDF-InMemory backend due transaction management.
       *
       * @param  {Object}  params - an object containing the following fields:
       *  - operationVersionURI : the operation version to execute (mandatory)
       *  - inputs : the inputs values under the form {"inputName":"value", ...} (mandatory)
       *  - [options] : options for the run (optional)
       *      - outputFormat: "mask" to enable mask usage (in conjunction $mask with query parameter to specify a particular mask.)
       *  - cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
       *  - [timeout] : The amount of time (in seconds) to wait before the task is considered (by the server) to be timed out. The default is (currently) 1800 seconds. (optional)
       *  - [polltimeout] : The maximum amount of time (in seconds) that the server will wait between client-initiated checks for status. Current default is 300 seconds. (optional)
       *
       * @return {String} a string corresponding to the taskid - through the onComplete callback!
       */
      postTask: function (params) {
        var payload = [JSON.stringify(params.inputs)];
        if (params.options !== undefined)
          payload.push(JSON.stringify(params.options));

        var urlParams = (typeof params.urlParams === 'object') ? params.urlParams : {};
        urlParams["$this"] = params.operationVersionURI;
        urlParams["$timeout"] = params.timeout;
        urlParams["$polltimout"] = params.polltimeout;

        var dataArray = JSON.stringify(payload);
        var wafReq = this.WAFRequestFunction;
        var rootServerURL = this.rootServerURL;
        var headers = this.headers;

        var promise = new Promise(function (resolve, reject) {
          var url = commonServices.buildTaskURL(rootServerURL, 'dsbase:OperationVersion.run', urlParams);

          wafReq(url, {
            method: 'POST',
            type: 'json',
            headers: Object.assign(headers, {
              'Content-Type': 'application/json'
            }),
            data: dataArray,
            onComplete: function (data, status) {
              if (params.cbObject !== undefined)
                params.cbObject.onComplete(data['@id'], status);

              resolve(data['@id']);
            },
            onFailure: function (data, status, error) {
              if (params.cbObject !== undefined) {
                params.cbObject.onFailure(data, status, error);
              }
              reject(error);
            },
            timeout: 1000
          });
        });

        return promise;
      },

      /**
       * Gets the task execution status given the provided task identifier and returns the following:<br>
       * - if the tasks is still running, the http code will be 202 (Accepted).
       * - if the tasks is successfully finished, the http code will be 200 (OK) and the @value field will contain the stringified execution result (if any; depending on the operation signature)
       * - if the task execution throwed an exception, the http code will be 500 (Internal Server Error).
       * - if the task execution duration exceeds the timeout or if the poll request exceeds the polltimeout, the http code will be 404 (Not found).
		   * @memberof DS/dsbaseUIServices/OperationServicesTools#
       *
       * @param  {Object}  params - an object containing the following fields:
       *  - taskId : the task identifier (mandatory)
       *  - cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
       *
       * @return {Object} through the onComplete callback!
       */
      pollTask: function (params) {
        var wafReq = this.WAFRequestFunction;
        var rootServerURL = this.rootServerURL;
        var promise = new Promise(function (resolve, reject) {
          var url = commonServices.buildURL(rootServerURL, params.taskId, params.urlParams);

          wafReq(url, {
            method: 'GET',
            onComplete: function (data, status) {
              if (params.cbObject !== undefined)
                params.cbObject.onComplete(data, status);

              resolve(data);
            },
            onFailure: function (data, status, error) {
              if (params.cbObject !== undefined) {
                params.cbObject.onFailure(data, status, error);
              }
              reject(error);
            },
            timeout: 30000
          });
        });

        return promise;
      },

      /**
       * Cancels a task execution given the provided task identifier.
		   * @memberof DS/dsbaseUIServices/OperationServicesTools#
       *
       *
       * @param  {Object}  params - an object containing the following fields:
       *  - taskId : the task identifier (mandatory)
       *  - cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
       */
      cancelTask: function (params) {
        var wafReq = this.WAFRequestFunction;
        var rootServerURL = this.rootServerURL;
        var headers = this.headers;
        var promise = new Promise(function (resolve, reject) {
          var url = commonServices.buildURL(rootServerURL, params.taskId, params.urlParams);

          wafReq(url, {
            method: 'DELETE',
            headers: Object.assign(headers, {
              'Content-Type': 'application/json'
            }),
            onComplete: function (data, status) {
              if (params.cbObject !== undefined)
                params.cbObject.onComplete(data, status);

              resolve(data);
            },
            onFailure: function (data, status, error) {
              if (params.cbObject !== undefined) {
                params.cbObject.onFailure(data, status, error);
              }
              reject(error);
            },
            timeout: 30000
          });
        });

        return promise;
      },
      /**
       * Returns the signature operation of a given OperationVersion
		   * @memberof DS/dsbaseUIServices/OperationServicesTools#
       *
       *
       * @param  {Object}  params - an object containing the following fields:
       * 						- operationVersionURI : the Operation version from which the definition must be retrieved (mandatory)
       *            - cbObject : and object containing the onComplete and onFailure callbacks (mandatory)
       * @return {Object} an object containing the operation definition - through the onComplete callback!
       */
      getVersionDefinition: function (params) {
        var definitionContent = {
          signature: true,
          implementation: true
        };
        var dataArray = JSON.stringify([params.operationVersionURI, definitionContent]);
        var wafReq = this.WAFRequestFunction;
        var rootServerURL = this.rootServerURL;
        var headers = this.headers;
        var promise = new Promise(function (resolve, reject) {
          wafReq(commonServices.buildURL(rootServerURL, "dsbase:getOperationVersionDefinition", params.urlParams), {
            method: 'POST',
            type: 'json',
            headers: Object.assign(headers, {
              'Content-Type': 'application/json'
            }),
            data: dataArray,
            onComplete: function (data, status) {
              data.value = JSON.parse(data.value);
              if (params.cbObject !== undefined) {
                params.cbObject.onComplete(data, status);
              }
              resolve(data);
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
       * Builds an Operation and its version(s) from the provided proxy definition.
		   * @memberof DS/dsbaseUIServices/OperationServicesTools#
       *
       *
       * @param  {Object}  params - an object containing the following fields:
       * 						- proxy : the (composite or atomic) Operation proxy (mandatory) - see ODL definition.
       *            - cbObject : an object containing the onComplete and onFailure callbacks (mandatory).
       * @return {Object} an object containing the operation definition - through the onComplete callback!
       */
      buildOperation: function (params) {
        if (params.timeout === undefined) {
          params.timeout = 30000;
        }
        var payload = [JSON.stringify(params.proxy)];
        var dataArray = JSON.stringify(payload);
        var wafReq = this.WAFRequestFunction;
        var rootServerURL = this.rootServerURL;
        var headers = this.headers;
        var promise = new Promise(function (resolve, reject) {
          wafReq(commonServices.buildURL(rootServerURL, 'dsbase:buildOperation', params.urlParams), {
            method: 'POST',
            type: 'json',
            headers: Object.assign(headers, {
              'Content-Type': 'application/json'
            }),
            data: dataArray,
            onComplete: function (data, status) {
              if (params.cbObject !== undefined)
                params.cbObject.onComplete(data, status);

              resolve(data);
            },
            onFailure: function (data, status, error) {
              if (params.cbObject !== undefined) {
                params.cbObject.onFailure(data, status, error);
              }
              reject(error);
            },
            timeout: params.timeout
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

    OperationServicesTools.prototype.constructor = OperationServicesTools;

    return opServices;
  });
