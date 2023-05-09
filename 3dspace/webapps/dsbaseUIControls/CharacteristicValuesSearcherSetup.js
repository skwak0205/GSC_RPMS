define('DS/dsbaseUIControls/CharacteristicValuesSearcherSetup', [
    'DS/i3DXCompassPlatformServices/i3DXCompassPlatformServices'
], function (
    i3DXCompassPlatformServices
) {

    'use strict';

    let Singleton = (function () {

        // Constructor
        let constructor = function (platformId) {
            this.platformId = platformId;

            // Method to get FedSearch URL
            let getFedSearchURL = function () {
                return new Promise(function (resolve, reject) {

                    // Get current user
                    i3DXCompassPlatformServices.getUser({
                        onComplete: function (data) {
                            let currentUserID = data.id;

                            // Get all services associated to the current platform & user
                            i3DXCompassPlatformServices.getPlatformServices({
                                platformId: platformId,
                                onComplete: function (data) {
                                    // console.log('--- getPlatformServices ---');
                                    // console.log(data);

                                    var fedSearchURL;
                                    if (platformId) {
                                        fedSearchURL = data['3DSearch'];
                                    } else {
                                        fedSearchURL = data[0]['3DSearch']; // several platformIds returned
                                    }

                                    resolve({
                                        userID: currentUserID,
                                        fedSearchURL: fedSearchURL
                                    });
                                },
                                onFailure: function (error) {
                                    console.log(error);
                                }
                            });
                        },
                        onFailure: function (error) {
                            console.log(error);
                        }
                    });
                }).catch(function (error) {
                    console.log(error);
                });
            }

            this.fedSearchPromise = getFedSearchURL();

        }

        let instance = null;

        // Method for the Singleton pattern (get instance)
        return new function () {
            this.getInstance = function (platformId) {
                if (instance == null) {
                    instance = new constructor(platformId);
                    instance.constructor = null;
                }
                return instance;
            }
        };

    })();

    return Singleton;

});
