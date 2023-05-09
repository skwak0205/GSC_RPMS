/*! Copyright 2014 , Dassault Systemes. All rights reserved. */
/* global define */
/*-------------------------------------------------------------------------------
  
--------------------------------------------------------------------------------*/
define('DS/CAAWebAppsRetrievingXML/CAAWebAppsRetrievingXMLTools',  
       [ 'DS/WAFData/WAFData'] , function (WAFData) {
    	
	'use strict';

	var _DOMContainer  ; 
	
	// Method to parse XML structure
	// -----------------------------
	var myLoop = function (x) {
		var i, y, xLen, txt;
		txt = "";
		x = x.childNodes;
		xLen = x.length;
		for (i = 0; i < xLen ;i++) {
			y = x[i];	
			if (y.nodeName == "parsererror") {
				txt += y.childNodes[0].childNodes[0].nodeValue + "<br>";
				txt += y.childNodes[1].childNodes[0].nodeValue + "<br>";
				txt += y.childNodes[2].childNodes[0].nodeValue + "<br>";
			} else {
				if (y.nodeName != "#text") {
					txt += "<br>" + y.nodeName;
					if (y.childNodes[0] != undefined) {
						txt += ": " + y.childNodes[0].nodeValue + myLoop(y) //+ "<br>";
					}
				}
			}			
       }
	   return txt ;
    };

	//In case of successful response
	// wsreponse is a DOM object
	//  since responseType='document'
	var displayComplete = function (wsreponse) {
	     
		var x = wsreponse.documentElement;
        var txt = myLoop(x);
	    _DOMContainer.innerHTML = txt ;
		
	};
	
	//In case of ws failure - proxified case
	//  error           : a JS ERROR object  
	//  backendresponse : may be undefined if the error is not 502 
	//
	var displayFailure = function (error, backendresponse) {
		var text = "<b>HTTP Request failure</b><br><br>"  ;
		text += "<b>WAFData Error Message</b>:<br>"
		text += error.message ;

		if ( backendresponse ) {
			if ( typeof backendresponse === 'string' ) {
				text += "<br><br><b>Proxification Response</b>: <br>" ;
				text += backendresponse ;
			}
			//only if authenticatedRequest and backendresponse is an xml as 404
			if ( typeof backendresponse === 'object' ) {
				text += "<br><br><b>Backend response as xml</b>: <br>" ;
				text += myLoop( backendresponse );
			}
		}
		_DOMContainer.innerHTML = text ;
	};
	
	// data exported by the AMD Module
	//
    var exports;
	
	exports = {
	    
		DisplayXML : function ( container, URLResource ) {
		    _DOMContainer = container ;
			if ( _DOMContainer && URLResource ) {	
			
                    // HTTP Request on non 3DEXPERIENCE servers  
					// so we use the proxified API with default (javax) proxy
					WAFData.proxifiedRequest(URLResource + '.xml', {
						'method'       :'GET',					
						'type'         : 'xml' ,						
						'onComplete'   : displayComplete ,	 					
						'onFailure'    : displayFailure
					});						
			} else {
				console.log("Internal issue, no DOM");
			}
		}
	};
	
    return exports;
});
