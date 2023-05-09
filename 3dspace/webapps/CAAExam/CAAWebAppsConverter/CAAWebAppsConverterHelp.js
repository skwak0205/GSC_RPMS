/*! Copyright 2014 , Dassault Systemes. All rights reserved. */
/* global define */
/*-------------------------------------------------------------------------------
  
--------------------------------------------------------------------------------*/
define('DS/CAAWebAppsConverter/CAAWebAppsConverterHelp',  
       ['DS/WAFData/WAFData'] , function (WAFData) {
    	
	'use strict';

	var _Container;
	
    var exports;

	exports = {
	    
		onConvertClick: function( options ) {
			var pathWS="http://query.yahooapis.com/v1/public/yql?" ;
			var selectClause= "q=select" + encodeURIComponent(" * from xml ");					 
			var URLConverter= " url='http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?FromCurrency=EUR&ToCurrency=USD'" ;
			var whereClause="where" + encodeURIComponent(URLConverter);
			pathWS += selectClause + whereClause + "&format=json&diagnostics=true";
			
			console.log("PATH" + pathWS);
			_Container = options.container ;
			
			WAFData.proxifiedRequest(pathWS, {
				method:'GET',
				onComplete: exports._processOnComplete ,
				onFailure: exports._processOnFailure 
			});	 
		},
		
		_processOnFailure : function (error, responseAsString) {
			var textToDisplay="Internal Error";
			if ( responseAsString ) {
			    //The error created by WAFData due to the proxification
				var respAsJSON= JSON.parse(responseAsString);
				if ( respAsJSON && respAsJSON.error) {
					textToDisplay=respAsJSON.error.message;
				}
			}
			_Container.innerHTML=textToDisplay ;
		},
		
		_processOnComplete : function (responseAsString) {  
			//Retrieve the amount									
			var inputText=_Container.querySelector('.amountIpt');
			var amountT=inputText.value ;
			var labelResult=_Container.querySelector('.amountResultLbl');
			
			//Create a JSON object
			var respAsJSON = JSON.parse(responseAsString);
			var result=null ;
			if ( respAsJSON && respAsJSON.query && respAsJSON.query.results ) {
				result=respAsJSON.query.results;
			}
			 
			if ( result && result["double"] && result["double"].content ) {
				//Retrieve the rate
				var rateCurrency = result["double"].content; 

				//Compute the conversion
				var amountResult = amountT * rateCurrency;	
			 
				//Modify the Label 												   
				labelResult.innerHTML= amountResult ;
			 }else {
				labelResult.innerHTML= "No response, issue with the Yahoo service" ;
			}
		}
	};
	
    return exports;
});
