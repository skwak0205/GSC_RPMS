function executeWidgetCode() {
    require(["UWA/Drivers/jQuery", "DS/PlatformAPI/PlatformAPI"], function($, PlatformAPI) {
        var myWidget = {
            dataFull: [
                { firstName: "John", lastName: "Doe", userId: "JD1" },
                { firstName: "Jane", lastName: "Doe", userId: "JD2" },
                { firstName: "David", lastName: "Doe", userId: "DD1" },
                { firstName: "David", lastName: "Black", userId: "DB1" },
                { firstName: "David", lastName: "White", userId: "DW1" },
                { firstName: "Walter", lastName: "White", userId: "WW1" }
            ],

            topicName: "clickUserId",

            displayData: function(arrData) {

                var $wdgBody = $(widget.body);
                $wdgBody.empty();

                var $table = $("<table id = 'data'></table>");

                $table.append("<thead><tr><th>First Name</th><th>Last Name</th><th>userId</th></tr></thead>");

                var $tBody = $("<tbody></tbody>");

                for (var i = 0; i < arrData.length; i++) {
                    var $tr = $(
                        "<tr id='" +
                            arrData[i].firstName + "&nbsp " + arrData[i].lastName + "&nbsp "+ arrData[i].userId +
                            "' rowSelected='false'>" + 
							"<td>" +
                            arrData[i].firstName +
                            "</td><td>" +
                            arrData[i].lastName +
                            "</td><td>" +
                            arrData[i].userId +
                            "</td></tr>"
                    );
                    $tr.on("click", myWidget.clickOnRow);
                    $tBody.append($tr);
                }
                $table.append($tBody);
                $wdgBody.append($table);
            },
            onLoadWidget: function() {
                myWidget.displayData(myWidget.dataFull);
            },
			
			//clickOnRow function parses the data of the row selected by user. It applies color coding to the selected row and invoke the publish method of API module
            /*clickOnRow: function(ev) {
                var elem = this;
				var userIdClicked = elem.id;
				var array1 = [];
				var array2=[];
				array1=userIdClicked.split(" ");
                array2.push({
					firstName: array1[0],
					lastName: array1[1],
					userId: array1[2]
				});
				
				
				selected= $(elem).attr("rowSelected");
				var wasSelected = selected === "true" ? true : false;
				if (wasSelected) {
                    //remove the color;
                    $(elem).css("background-color", "");
                    $(elem).attr("rowSelected", "false");
					PlatformAPI.publish(myWidget.topicName, {FullName: array2, secondClick: wasSelected});
                } else {
                    //set the color
					$("table > tbody > tr").each(function () {
						var currentRow = this;
						$(currentRow).css("background-color", "");		
						$(currentRow).attr("rowSelected", "false");		
					});
                    $(elem).css("background-color", "#79D1FB");
                    $(elem).attr("rowSelected", "true");
					PlatformAPI.publish(myWidget.topicName, {FullName: array2, secondClick: wasSelected});
                }
            }*/
		};

        widget.myWidget = myWidget;

        widget.addEvent("onLoad", myWidget.onLoadWidget);
    });
}
