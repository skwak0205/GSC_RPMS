/*!
Script: DSSocialGlobeUWA.js

This file is part of UWA JS Runtime.

About: License

Copyright 2006-2013 Dassault Systèmes company.
All rights reserved.
*/
define("DS/SocialGlobeAPI/GlobeAPI",["UWA/Controls/Abstract","DS/SocialGlobe/GlobeItf"],function(e,t){"use strict";return e.extend({init:function(e,i,o,a,l){this._globe=t.create(document.getElementById(e),!0,!0),this._map=this._globe.globe,this._map.initialTileMapValue=i+"{z}/{x}/{y}."+o,this._map.initialMaxLevel=a,this._map.initialZoomLevel=l.initialZoomLevel?l.initialZoomLevel:5,this._map.initialGeoloc=l.initialCoordinates?l.initialCoordinates.join(","):void 0,this._map.start()},addGeoJson:function(e){this._map.clearGeoJSONData(),this._map.loadGeoJSON(e)},moveCamera:function(e,t,i){var o=this._map.map.getZoom();i&&(o=i.zoomLevel?i.zoomLevel:o),this._map.flyToLatLon(e,t,o)},addMarker:function(e,t,i,o,a){var l=void 0;a&&(l=a.color?a.color:l);this._map.addMarkerFromAPI(e,t,i,o,l)},reset:function(){this._map.clearGeoJSONData()},select:function(e){if(e.length&&e.length>0){var t=e[0];this._map.selectMarkerFromAPI(t)}},unselect:function(e){this._map.unselectAll(),e.length&&e.length},setOnSelectCallback:function(e){this._map._onSelectCallback=e},removeMarker:function(e){this._map.removeMarkerFromAPI(e)}})});