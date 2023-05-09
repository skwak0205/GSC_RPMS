/**
 * @author DSIS
 */
define("DSISWidgetModules/DSISToolsWS", ["DSISWidgetModules/Connector3DExp"], function (Connector3DExp) {
    "use strict";

    var DSISToolsWS = {
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
                responseType: options.responseType,
                onComplete: function (dataResp, headerResp, callbackData) {
                    if (dataResp.msg === "OK") {
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

        //Objects Services

        find: function (options) {
            options.url = "/DSISTools/Find";
            DSISToolsWS._do3DSpaceCall(options);
        },
        expand: function (options) {
            options.url = "/DSISTools/ExpandObject";
            DSISToolsWS._do3DSpaceCall(options);
        },
        objInfo: function (options) {
            options.url = "/DSISTools/ObjectInfo";
            DSISToolsWS._do3DSpaceCall(options);
        },
        objUpdateAttributes: function (oid, mapAttrs, options) {
            options.url = "/DSISTools/UpdateAttributes/" + oid;
            options.data = JSON.stringify(mapAttrs);
            options.headers = {
                "Content-Type": "application/json"
            };
            options.method = "PUT";
            DSISToolsWS._do3DSpaceCall(options);
        },
        relInfo: function (options) {
            options.url = "/DSISTools/RelationshipInfo";
            DSISToolsWS._do3DSpaceCall(options);
        },

        //Types Services

        listTypes: function (typePattern, options) {
            options.url = "/UM5Tools/Type/list/" + typePattern;
            DSISToolsWS._do3DSpaceCall(options);
        },
        typeFromRel: function (type, options) {
            options.url = "/UM5Tools/Type/" + type + "/fromrel";
            DSISToolsWS._do3DSpaceCall(options);
        },
        typeToRel: function (type, options) {
            options.url = "/UM5Tools/Type/" + type + "/torel";
            DSISToolsWS._do3DSpaceCall(options);
        },
        typeIcon: function (type, options) {
            options.url = "/UM5Tools/Type/" + type + "/icon";
            DSISToolsWS._do3DSpaceCall(options);
        },
        typeAttributes: function (type, options) {
            options.url = "/UM5Tools/Type/" + type + "/attributes";
            DSISToolsWS._do3DSpaceCall(options);
        },
        typePolicies: function (type, options) {
            options.url = "/UM5Tools/Type/" + type + "/policies";
            DSISToolsWS._do3DSpaceCall(options);
        },
        typeCreateObject: function (type, name, revision, policy, options) {
            if (!name || name === "") {
                name = "_autoname";
            }
            if (!revision || revision === "") {
                revision = "_autonameRevision";
            }
            options.url = "/UM5Tools/Type/" + type + "/" + name + "/" + revision + "/" + policy;
            options.method = "PUT";
            DSISToolsWS._do3DSpaceCall(options);
        },

        //Rels Services

        addConnection: function (fromId, relType, toId, options) {
            options.url = "/UM5Tools/Relationship/connect/" + fromId + "/" + relType + "/" + toId;
            DSISToolsWS._do3DSpaceCall(options);
        },
        removeConnection: function (relId, options) {
            options.url = "/UM5Tools/Relationship/disconnect/" + relId;
            DSISToolsWS._do3DSpaceCall(options);
        },
        relFromType: function (relType, options) {
            options.url = "/UM5Tools/Relationship/" + relType + "/fromtype";
            DSISToolsWS._do3DSpaceCall(options);
        },
        relToType: function (relType, options) {
            options.url = "/UM5Tools/Relationship/" + relType + "/totype";
            DSISToolsWS._do3DSpaceCall(options);
        },

        //UI Conf Services

        uiConfList: function (confType, options) {
            options.url = "/UM5Tools/uiconf/v1/list/" + confType;
            options.method = "POST";
            DSISToolsWS._do3DSpaceCall(options);
        },
        uiConfCreate: function (confType, confName, confContent, options) {
            options.url = "/UM5Tools/uiconf/v1/create/" + confType + "/" + confName;
            options.data = JSON.stringify(confContent);
            options.headers = {
                "Content-Type": "application/json"
            };
            options.method = "POST";
            DSISToolsWS._do3DSpaceCall(options);
        },
        uiConfUpdate: function (confPID, confContent, options) {
            options.url = "/UM5Tools/uiconf/v1/update/" + confPID;
            options.data = JSON.stringify(confContent);
            options.headers = {
                "Content-Type": "application/json"
            };
            options.method = "POST";
            DSISToolsWS._do3DSpaceCall(options);
        },
        uiConfDelete: function (confPID, options) {
            options.url = "/UM5Tools/uiconf/v1/update/" + confPID;
            options.method = "DELETE";
            DSISToolsWS._do3DSpaceCall(options);
        },
        uiConfContent: function (confPID, options) {
            options.url = "/UM5Tools/uiconf/v1/getConf/" + confPID;
            options.method = "GET";
            DSISToolsWS._do3DSpaceCall(options);
        }
    };
    return DSISToolsWS;
});