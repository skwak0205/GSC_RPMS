/*!
  Script: IOT.js

    This file is part of UWA JS Runtime.

  About: License

    Copyright 2006-2014 Netvibes, a Dassault Systèmes company.
    All rights reserved.

*/
define("UWA/Services/IOT",["UWA/Core"],function(e){"use strict";var n=function(){},r={registerEventSender:function(r){if(!e.is(r,"function"))throw new Error("Event sender must be a function");n=r},sendEvent:function(e){n.call(null,e)}};return e.namespace("Services/IOT",r,e)});