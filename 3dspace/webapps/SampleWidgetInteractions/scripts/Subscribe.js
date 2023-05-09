function executeWidgetCode() {
    require(["UWA/Drivers/jQuery", "DS/PlatformAPI/PlatformAPI"], function($, PlatformAPI) {
        var myWidget = {
            dataFull: [],

            topicName: "clickUserId",

            displayData: function(arrData,secondClick) {
				
                var html = "<table >" + "<tr>" +
				"<td><label class='myLblTypeJSON' >First Name</label> </td>"  +
				"<td ><input class='firstname' type='text' /></td>" +
				"</tr>" + "<tr>" +
				"<td><label class='myLblTypeText' >Last Name</label> </td>"  +
				"<td ><input class='lastname' type='text' /></td>" +
				"</tr>" + "<tr>" +
				"<td><label class='myLblType' >User Id</label> </td>"  +
				"<td ><input class='userid' type='text' /></td>" + "</tr>" + "</table>"; 
				
				widget.body.innerHTML=html ;
				let data = JSON.parse(secondClick);
				if(!JSON.parse(secondClick)) {
					var FirstName = widget.body.querySelector('.firstname');
					var LastName = widget.body.querySelector('.lastname');
					var UserId    = widget.body.querySelector('.userid');
					for(i=0;i<arrData.length;i++) {
						FirstName.value=JSON.parse(JSON.stringify(arrData[i].firstName));
						LastName.value=JSON.parse(JSON.stringify(arrData[i].lastName));
						UserId.value=JSON.parse(JSON.stringify(arrData[i].userId));
					}
				} else {
					html+="<br><br><div style=\"font-size:160%;color:blue;font-weight: 500;text-align:center;\">Highlighted row deselected!</div>";
					widget.body.innerHTML=html ;
				}
            },
            onLoadWidget: function() {
                //PlatformAPI.subscribe(myWidget.topicName, myWidget.selectUserRow);

                myWidget.displayData(myWidget.dataFull,false);
            },

			//selectUserRow is the callback function invoked after subscribing to the publish event. It parses the data.
            selectUserRow: function(data) {
				var secondClick=data.secondClick;
				if(secondClick) {
					myWidget.dataFull=[];					
				} else {
					myWidget.dataFull=JSON.parse(JSON.stringify(data.FullName));
				}
				myWidget.displayData(myWidget.dataFull,secondClick);
            }
        };

        widget.myWidget = myWidget;

        widget.addEvent("onLoad", myWidget.onLoadWidget);
    });
}