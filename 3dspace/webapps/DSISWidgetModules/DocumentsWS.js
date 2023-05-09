/**
 * @author DSIS
 */
define("DSISWidgetModules/DocumentsWS", ["DSISWidgetModules/Connector3DExp"], function (Connector3DExp) {
    "use strict";

    var DocumentsWS = {

        _do3DSpaceCall: function (optionsin) {
            let options = optionsin;
            Connector3DExp.call3DSpace({
                url: options.url,
                method: options.method || "POST",
                type: options.type || "json",
                headers: options.headers,
                data: options.data,
                callbackData: options.callbackData,
                forceReload: options.forceReload,
                contentType: options.contentType,
                responseType: options.responseType,
                onComplete: function (dataResp, headerResp, callbackData) {
                    //Save the CSRF Token if it's there
                    if (dataResp && dataResp.csrf && dataResp.csrf.name === "ENO_CSRF_TOKEN") {
                        DocumentsWS._currentCSRF = dataResp.csrf;
                    }
                    if (dataResp.success) {
                        options.onOk(dataResp.data, callbackData);
                    } else {
                        var errorType = "Error in Web Service Response " + options.url;
                        var errorMsg = JSON.stringify(dataResp);
                        options.onError(errorType, errorMsg);
                    }

                },
                onFailure: function (error) {
                    var errorType = "WebService Call Faillure " + options.url;
                    var errorMsg = JSON.stringify(error);
                    options.onError(errorType, errorMsg);
                }
            });
        },
        get3DSpaceURL: function () {
            return Connector3DExp.getCurrentTenantInfo()["3DSpace"];
        },

        //OOTB Documents Web Services

        //Manage the CSRF Token
        _currentCSRF: null,
        _doCallWithCSRF: function (options) {
            if (!options.data) {
                options.data = {};
            }
            if ((!DocumentsWS._currentCSRF || DocumentsWS._currentCSRF === null) && options.method !== "GET") {
                // If it's a GET request CSRF is not needed and we will have a CSRF token available in return
                // In 18x the call to bellow Web Service "/resources/v1/application/CSRF" is KO so you need to first do a GET call to a Web Service that will then give you a CSRF token
                // Due to the if condition above, if it's a GET call then it will do directly do your GET call
                // Once you have a CSRF token stored you can then do any kind of call using this method, the if condition will see that their is a CSRF token stored.

                // Else in 19x you don't need to do a previous GET call, this "/resources/v1/application/CSRF" call should work to Get the CSRF before doing the final targetted call
                DocumentsWS._do3DSpaceCall({
                    url: "/resources/v1/application/CSRF", //KO in 18x !
                    method: "GET",
                    forceReload: true,
                    onOk: (data) => {
                        //We have the CSRF Token here
                        DocumentsWS._currentCSRF = data.csrf;
                        //Now do the call
                        DocumentsWS._doCallWithCSRF(options);
                    },
                    onError: (errorType, errorMsg) => {
                        options.onError("Impossible to get CSRF Token due to " + errorType, errorMsg);
                    }
                });
            } else {
                //Do the call directly with the last CSRF Token
                options.data.csrf = DocumentsWS._currentCSRF;
                if (options.method !== "GET") {
                    options.data = JSON.stringify(options.data);
                    options.contentType = "application/json";
                    options.headers = options.headers || {};
                    options.headers["Content-Type"] = "application/json";
                }
                DocumentsWS._do3DSpaceCall(options);
            }
        },

        //Helpers for the Web Services
        getDocInfos: function (docId, options) {
            options.url = "/resources/v1/modeler/documents/" + docId;
            options.method = "GET";
            options.forceReload = true;
            DocumentsWS._doCallWithCSRF(options);
        },

        getDocInfosFiles: function (docId, options) {
            options.url = "/resources/v1/modeler/documents/" + docId + "/files";
            options.method = "GET";
            options.forceReload = true;
            DocumentsWS._doCallWithCSRF(options);
        },

        getDocFileDownloadTicket: function (docId, fileId, options) {
            options.url = `/resources/v1/modeler/documents/${docId}/files/${fileId}/DownloadTicket`;
            options.method = "PUT";
            options.forceReload = true;
            DocumentsWS._doCallWithCSRF(options);
        },

        uploadFileToNewDoc: function (file, onOkWithDocInfos, onError) {
            //Process is :
            // 1- Get a Checkin Ticket for the FCS
            // 2- Upload the file in the FCS and get the FCS Receipt
            // 3- Create the Document with the FCS Receipt to attach the file to it at creation

            DocumentsWS._getCheckinTicketInfos((fcsDataTicket) => {
                //Step 1 done
                let fcsURL = fcsDataTicket.ticketURL;
                let fcsTicketParam = fcsDataTicket.ticketparamname;
                let fcsTicket = fcsDataTicket.ticket;
                DocumentsWS._uploadFileToFCS(file, fcsURL, fcsTicketParam, fcsTicket, (fcsReceipt) => {
                    //Step 2 done
                    let fileName = file.name;
                    let docTitle = fileName.substring(0, fileName.lastIndexOf("."));
                    DocumentsWS._createNewDocWithFCSFileReceipt(fcsReceipt, docTitle, fileName, onOkWithDocInfos, onError);
                }, onError);
            }, onError);

        },

        _getCheckinTicketInfos: function (onOkFCSTicket, onError) {
            let options = {
                url: "/resources/v1/modeler/documents/files/CheckinTicket",
                method: "PUT",
                data: "{}", //Empty Json as a String
                contentType: "application/json",
                forceReload: true,
                onOk: (dataFCSTicket) => {
                    let fcsData = dataFCSTicket && dataFCSTicket[0] && dataFCSTicket[0].dataelements;
                    onOkFCSTicket(fcsData);
                },
                onError: (errorType, errorMsg) => {
                    onError(errorType, errorMsg);
                }
            };
            //No CSRF Token needed for this PUT call to get a CheckinTicket and the bonus is that it will give us one for the next calls
            DocumentsWS._do3DSpaceCall(options);
        },

        _uploadFileToFCS: function (file, fcsURL, fcsTicketParam, fcsTicket, onOkFCSReceipt, onError) {
            let formData = new FormData();
            formData.append(fcsTicketParam, fcsTicket);
            formData.append("file_0", file, file.name);

            let req = new XMLHttpRequest();
            req.open("POST", fcsURL, true);

            req.onload = function (e) {
                if (this.status === 200) {
                    var dataFCSReceipt = req.response;

                    onOkFCSReceipt(dataFCSReceipt);

                } else {
                    console.error("Error will uploading the file to FCS", e);
                    onError("WebService Call Faillure " + fcsURL, req.response);
                }
            };

            req.onerror = function () {
                onError("WebService Call Faillure " + fcsURL, arguments);
            };

            req.send(formData); //Send the XHR
        },

        _createNewDocWithFCSFileReceipt: function (fcsFileReceipt, docTitle, fileName, onOkDocCreated, onError) {
            var strCtx = widget.getValue(Connector3DExp._widgetPref4Ctx);
            let currentCS = strCtx.split(".")[2];

            let tempId = "temp_" + Date.now();

            let options = {
                url: "/resources/v1/modeler/documents",
                method: "POST",
                data: { //CSRF token will be added by the _doCallWithCSRF function call
                    data: [{
                        dataelements: {
                            title: docTitle,
                            collabSpace: currentCS
                        },
                        relateddata: {
                            files: [{
                                dataelements: {
                                    title: fileName,
                                    receipt: fcsFileReceipt
                                }
                            }]
                        },
                        tempId: tempId
                    }]
                },
                contentType: "application/json",
                type: "json",
                forceReload: true,
                onOk: (dataDoc) => {
                    onOkDocCreated(dataDoc, tempId);
                },
                onError: (errorType, errorMsg) => {
                    onError(errorType, errorMsg);
                }
            };

            DocumentsWS._doCallWithCSRF(options);

        }

    };
    return DocumentsWS;
});