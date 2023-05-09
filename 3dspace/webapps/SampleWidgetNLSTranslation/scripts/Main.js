function executeWidgetCode() {
	require(['i18n!SampleWidgetNLSTranslation/assets/AppliTranslation'],function (AppliTranslation) {
			'use strict';
			var myWidget = {
				onLoadWidget: function() {
					/*//Welcome User
					var BodyText1 = AppliTranslation.get("KeyBodyText1" , {"preftext1" : widget.getValue("UserName")}) ; 

					//Language Code of browser window
					var BodyText2 = AppliTranslation.get("KeyBodyText2", { "code" :  widget.lang } );
					
					//Get the user role
					var BodyText3 = AppliTranslation.get("KeyBodyText3" , {"preftext2" : widget.getValue("userRole")});
					
					
					var BodyContent = "<p>" + BodyText1 + "</p>"  ;
					BodyContent += "<p>" + BodyText2 + "</p>" ;
					BodyContent += "<p>" + BodyText3 + "</p>"  ;
				

					//Set custom title
					var TitleText = AppliTranslation.get("KeyTitle" , { "preftext" : widget.getValue("title") } ) ;
					widget.setTitle(TitleText) ;*/

					widget.body.innerHTML= BodyContent ;
				}
			};
			widget.addEvent("onLoad", myWidget.onLoadWidget);
		});
}