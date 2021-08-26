var dropDownCountry;
var dataListState;
	$(document).ready(function(){
		dropDownCountry = $("#country");
		dataListState = $("#listStates");
		dropDownCountry.on("change",function(){
			loadStatesForCountry();
			$("#state").val("").focus();
		});
	});

	function loadStatesForCountry(){
		selectedCountry = $("#country option:selected");
		countryId = selectedCountry.val();
		url = contextPath + "states/list_by_country/" + countryId;
		
		$.get(url, function(responseJSON) {
			dataListState.empty();
			
			$.each(responseJSON, function(index, state) {
				$("<option>").val(state.name).text(state.name).appendTo(dataListState);
			});
			
		}).fail(function() {
			showErrorModal("Error loading state/provinces for the selected country");
		});	
	}