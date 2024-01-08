var session_match,qt_btn;
var selected_options = [];
function processWaitingButtonSpinner(whatToProcess) 
{
	switch (whatToProcess) {
	case 'START_WAIT_TIMER': 
		$('.spinner-border').show();
		$(':button').prop('disabled', true);
		break;
	case 'END_WAIT_TIMER': 
		$('.spinner-border').hide();
		$(':button').prop('disabled', false);
		break;
	}
}
function initialiseSelectedOptionsList()
{
	selected_options = [];
	for(var i = 1; i <= 4; i++) {
	    selected_options.push('');
	}
	
	document.getElementById('selected_inning').innerHTML = 'Selected Inning: ' + document.getElementById('which_inning').value;
}
function initialiseForm(whatToProcess,dataToProcess)
{
	switch (whatToProcess) {
	case 'UPDATE-CONFIG':
		
		document.getElementById('configuration_file_name').value = $('#select_configuration_file option:selected').val();
		document.getElementById('select_cricket_matches').value = dataToProcess.filename;
		document.getElementById('select_broadcaster').value = dataToProcess.broadcaster;
		document.getElementById('select_second_broadcaster').value = dataToProcess.secondaryBroadcaster;
		document.getElementById('qtIPAddress').value = dataToProcess.qtIpAddress;
		document.getElementById('qtPortNumber').value = dataToProcess.qtPortNumber;
		document.getElementById('vizIPAddress').value = dataToProcess.primaryIpAddress;
		document.getElementById('vizPortNumber').value = dataToProcess.primaryPortNumber;
		document.getElementById('vizSecondaryIPAddress').value = dataToProcess.secondaryIpAddress;
		document.getElementById('vizSecondaryPortNumber').value = dataToProcess.secondaryPortNumber;
		document.getElementById('vizTertiaryIPAddress').value = dataToProcess.tertiaryIpAddress;
		document.getElementById('vizTertiaryPortNumber').value = dataToProcess.tertiaryPortNumber;
		processUserSelection($('#select_second_broadcaster'));
		break;
	
	}
}
function processUserSelection(whichInput)
{
	switch ($(whichInput).attr('name')) {
	case 'load_scene_btn':
      	document.initialise_form.method = 'post';
      	document.initialise_form.action = 'output';
      	document.initialise_form.submit();
		break;
	case 'cancel_graphics_btn':
		$("#select_graphic_options_div").empty();
		document.getElementById('select_graphic_options_div').style.display = 'none';
		$("#captions_div").show();
		break;
	case 'populate_btn':
		processCricketProcedures("POPULATE-GRAPHICS", $('#which_keypress').val() + ',' + selected_options.toString());
		break;
	case 'f4_graphic_btn': case 'f6_graphic_btn': case 'f7_graphic_btn': case 'f8_graphic_btn': case 'f9_graphic_btn': 
	case 'f_graphic_btn': case 's_graphic_btn': case 'w_graphic_btn': case 'z_graphic_btn':
		switch ($(whichInput).attr('name')){
			case 'f4_graphic_btn':
				qt_btn = 'F4_BTN';
				break;
			case 'f6_graphic_btn':
				qt_btn = 'F6_BTN';
				break;
			case 'f7_graphic_btn':
				qt_btn = 'F7_BTN';
				break;
			case 'f8_graphic_btn':
				qt_btn = 'F8_BTN';
				break;	
			case 'f9_graphic_btn':
				qt_btn = 'F9_BTN';
				break;
			case 'f_graphic_btn':
				qt_btn = 'F_BTN';
				break;
			case 's_graphic_btn':
				qt_btn = 'S_BTN';
				break;
			case 'w_graphic_btn':
				qt_btn = 'W_BTN';
				break;
			case 'z_graphic_btn':
				qt_btn = 'Z_BTN';
				break;
		}
		processCricketProcedures("ANIMATE-QT", qt_btn + ",");
		break;
	}	
}

function processUserSelectionData(whatToProcess,dataToProcess)
{
	switch (whatToProcess) {
	case 'LOGGER_FORM_KEYPRESS':
		initialiseSelectedOptionsList();
		document.getElementById('which_keypress').value = dataToProcess;
		switch(dataToProcess) {
		case 'SPEED':
			processCricketProcedures('SHOW_SPEED');
			break;
		case 'RE_READ_DATA':
			processCricketProcedures('RE_READ_DATA');
			break;
		case 'Alt_ ':
			processCricketProcedures('CLEAR-ALL-WITH-INFOBAR');
			break;
		case ' ':
			processCricketProcedures('CLEAR-ALL');
			break;
		case '1': case '2': case '3': case '4':
			if(session_match.setup.maxOvers > 0){
				switch (dataToProcess) {
				case '3': case '4': // Key 1 to 4
					document.body.focus();
					keys = [];
					alert("3rd and 4th inning NOT available in a limited over match");
					return false;
				}				
			}
			document.getElementById('which_inning').value = dataToProcess;
			document.getElementById('selected_inning').innerHTML = 'Selected Inning: ' + dataToProcess;
			break;
			
		case '-':
			
			document.body.focus();
			keys = [];
			if(confirm('It will Also Delete Your Preview from Directory...\r\n \r\nAre You Sure To Animate Out? ') == true){
				processCricketProcedures('ANIMATE-OUT-GRAPHICS');
			}
			break;
		case 'Alt_-':
			
			document.body.focus();
			keys = [];
			if(confirm('Animate Out Infobar? ') == true){
				processCricketProcedures('ANIMATE-OUT-INFOBAR');
			}
			break;
		
		default:
			
			switch($('#which_inning').val()) {
			case 1: case 2: case 3: case 4:  
				document.body.focus();
				keys = [];
				alert('Selected inning must be between 1 to 4 [found ' 
					+ document.getElementById('which_inning').value + ']');
				return false;
			}
			
			switch(dataToProcess) {
			case 'F12': case 'Alt_1': case 'Alt_2': 
			case 'Control_F5': case 'Control_F8': case 'Control_F9': case 'F5': case 'F6' : case 'F7': 
			case 'F8': case 'F9': case 'F11': case 's':
			case 'Shift_O': case 'k': case 'g': case 'f':
				addItemsToList(dataToProcess,null);
				break;
			case 'Shift_F10': case 'Shift_F11': case 'm': case 'F1': case 'F2': case 'F4': case 'Control_a': 
			case 'Alt_k':  case 'Shift_F3': case 'd': case 'e': case 'Control_F7': case 'Shift_K':
			case 'Control_k': case 'Control_F10':
				dataToProcess = dataToProcess + ',' + document.getElementById('which_inning').value;
				processCricketProcedures("POPULATE-GRAPHICS", dataToProcess);
				break;
			default:
				processCricketProcedures("GRAPHICS-OPTIONS", dataToProcess);
				break;
			}
		}
		break;
	}
}
function processCricketProcedures(whatToProcess,dataToProcess)
{
	var valueToProcess = dataToProcess;
	switch(whatToProcess) {
	case 'GET-CONFIG-DATA':
		valueToProcess = $('#select_configuration_file option:selected').val();
		break;
	case 'READ-MATCH-AND-POPULATE':
		valueToProcess = $('#matchFileTimeStamp').val();
		break;
	}		
	$.ajax({    
        type : 'Get',     
        url : 'processCricketProcedures.html',     
        data : 'whatToProcess=' + whatToProcess + '&valueToProcess=' + valueToProcess, 
        dataType : 'json',
        success : function(data) {
			switch(whatToProcess) {
			case 'GET-CONFIG-DATA':
				initialiseForm('UPDATE-CONFIG',data);
				break;
			case 'READ-MATCH-AND-POPULATE': case "RE_READ_DATA":
				if(data){
					session_match = data;
					initialiseForm('UPDATE-MATCH-ON-OUTPUT-FORM',data);
				}
				break;
			case 'SHOW_SPEED':
				if(data == true){
					document.getElementById('speed_lbl').innerHTML = 'Show Speed: ' + 'YES';
				}else if(data == false){
					document.getElementById('speed_lbl').innerHTML = 'Show Speed: ' + 'NO';
				}
				break;
			default:
				if(whatToProcess == 'POPULATE-GRAPHICS') {
					if(data.status == 'OK') {
						document.body.focus();
						keys = [];
						if(confirm('Animate In?') == true){
							processCricketProcedures(whatToProcess.replace('POPULATE-', 'ANIMATE-IN-'),dataToProcess);
							$("#select_graphic_options_div").empty();
							document.getElementById('select_graphic_options_div').style.display = 'none';
							$("#captions_div").show();
						}
					} else {
						alert(data.status);
					}
				}else if(whatToProcess == 'GRAPHICS-OPTIONS'){
					addItemsToList(dataToProcess,data);
				}
				break;
			}
		}
	});
}
function setDropdownOptionToSelectOptionArray(whichInput, whichIndex)
{
	selected_options[0] = document.getElementById('which_inning').value;
	selected_options[whichIndex+1] = $('#' + $(whichInput).attr('id') + ' option:selected').val();
}
function addItemsToList(whatToProcess,dataToProcess)
{
	var select,option,header_text,div,table,tbody,row;
	var cellCount = 0;
	
	switch(whatToProcess) {
	case 'Control_m': case 'F5': case 'F6': case 'F7': case 'F8': case 'F9': case 'F10': case 'F11':
	case 'Control_F5': case 'Control_F9': case 'Control_F8': case 'Control_d': case 'Control_e': case 's':
	case 'Shift_O': case 'k': case 'g': case 'f':
	case 'F12': case 'Alt_1': case 'Alt_2': //InfoBar Left-Middle
	
		$("#captions_div").hide();
		$('#select_graphic_options_div').empty();
	
		header_text = document.createElement('h6');
		header_text.innerHTML = 'Select Graphic Options';
		document.getElementById('select_graphic_options_div').appendChild(header_text);
		
		table = document.createElement('table');
		table.setAttribute('class', 'table table-bordered');
				
		tbody = document.createElement('tbody');

		table.appendChild(tbody);
		document.getElementById('select_graphic_options_div').appendChild(table);
		
		row = tbody.insertRow(tbody.rows.length);
		
		switch(whatToProcess) {
		case 'F12':
			
			select = document.createElement('select');
			select.id = 'selectMiddleStat';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = 'IDENT_TEAM';
			option.text = 'Ident & Team';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'IDENT_TOURNAMENT';
			option.text = 'Ident & tournament';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'BATSMAN';
			option.text = 'Batsman/Bowler';
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			
			select = document.createElement('select');
			select.id = 'selectLeftBottom';
			select.name = select.id;

			option = document.createElement('option');
			option.value = 'CRR';
			option.text = 'Run Rate';
			select.appendChild(option);

			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.isCurrentInning == 'YES'){
					if(inn.inningNumber == 1){
						
						option = document.createElement('option');
						option.value = 'TOSS';
						option.text = 'Toss';
						select.appendChild(option);
						
					}
					else{
						
						option = document.createElement('option');
						option.value = 'TARGET';
						option.text = 'target';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = 'RRR';
						option.text = 'Required Rate';
						select.appendChild(option);
					}
				}
			});
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),1);
			cellCount = cellCount + 1;
			break;

		case 'Alt_1':

			select = document.createElement('select');
			select.id = 'selectLeftBottom';
			select.name = select.id;

			option = document.createElement('option');
			option.value = 'CRR';
			option.text = 'Run Rate';
			select.appendChild(option);

			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.isCurrentInning == 'YES'){
					if(inn.inningNumber == 1){
						option = document.createElement('option');
						option.value = 'TOSS';
						option.text = 'Toss';
						select.appendChild(option);
					}
					else{
						
						option = document.createElement('option');
						option.value = 'RRR';
						option.text = 'Required Rate';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = 'TARGET';
						option.text = 'Target';
						select.appendChild(option);
					}
				}
			});
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			break;
			
		case 'Alt_2':
			
			select = document.createElement('select');
			select.id = 'selectMiddleStat';
			select.name = select.id;

			option = document.createElement('option');
			option.value = 'BATSMAN';
			option.text = 'Batsman';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'IDENT_TEAM';
			option.text = 'Ident & team';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'IDENT_TOURNAMENT';
			option.text = 'Ident & Tournament';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'CURR_PARTNERSHIP';
			option.text = 'Current Partership';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'EXTRAS';
			option.text = 'Extras';
			select.appendChild(option);

			option = document.createElement('option');
			option.value = 'LAST_WICKET';
			option.text = 'Last Wicket';
			select.appendChild(option);

			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.isCurrentInning == 'YES'){
					if(inn.inningNumber == 1){
						
						option = document.createElement('option');
						option.value = 'PROJECTED';
						option.text = 'Projected Score';
						select.appendChild(option);
						
					}
					else{
						option = document.createElement('option');
						option.value = 'TARGET';
						option.text = 'Target';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = 'EQUATION';
						option.text = 'Equation';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = 'COMPARE';
						option.text = 'Compare';
						select.appendChild(option);
					}
				}
			});
			
			row.insertCell(cellCount).appendChild(select);
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			
			break;
			
		case 's':
			
			select = document.createElement('select');
			select.id = 'selectSplit';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = '50';
			option.text = '50-Split';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = '100';
			option.text = '100-Split';
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1
			break;
			
		case 'Control_F5'://Batsman Style
		
			select = document.createElement('select');
			select.id = 'selectPlayerName';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.isCurrentInning == 'YES'){
					if(inn.battingTeamId == session_match.setup.homeTeamId){
						session_match.setup.homeSquad.forEach(function(hs,index,arr){
							option = document.createElement('option');
							option.value = hs.playerId;
							option.text = hs.full_name;
							select.appendChild(option);
						});
						session_match.setup.homeOtherSquad.forEach(function(hos,index,arr){
							option = document.createElement('option');
							option.value = hos.playerId;
							option.text = hos.full_name  + ' (OTHER)';
							select.appendChild(option);
						});
					}else {
						session_match.setup.awaySquad.forEach(function(as,index,arr){
							option = document.createElement('option');
							option.value = as.playerId;
							option.text = as.full_name;
							select.appendChild(option);
						});
						session_match.setup.awayOtherSquad.forEach(function(aos,index,arr){
							option = document.createElement('option');
							option.value = aos.playerId;
							option.text = aos.full_name  + ' (OTHER)';
							select.appendChild(option);
						});
					}
				}
			});

			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			break;
			
		case 'Control_F8':
			
			select = document.createElement('select');
			select.id = 'selectTeams';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = session_match.setup.homeTeamId;
			option.text = session_match.setup.homeTeam.teamName1;
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = session_match.setup.awayTeamId;
			option.text = session_match.setup.awayTeam.teamName1;
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			break;
			
		case 'Control+F9': //BowlerStyle
		
			select = document.createElement('select');
			select.id = 'selectPlayerName';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.isCurrentInning == 'YES'){
					if(inn.bowlingTeamId == session_match.setup.homeTeamId){
						session_match.setup.homeSquad.forEach(function(hs,index,arr){
							option = document.createElement('option');
							option.value = hs.playerId;
							option.text = hs.full_name;
							select.appendChild(option);
						});
						session_match.setup.homeOtherSquad.forEach(function(hos,index,arr){
							option = document.createElement('option');
							option.value = hos.playerId;
							option.text = hos.full_name  + ' (OTHER)';
							select.appendChild(option);
						});
					}else {
						session_match.setup.awaySquad.forEach(function(as,index,arr){
							option = document.createElement('option');
							option.value = as.playerId;
							option.text = as.full_name;
							select.appendChild(option);
						});
						session_match.setup.awayOtherSquad.forEach(function(aos,index,arr){
							option = document.createElement('option');
							option.value = aos.playerId;
							option.text = aos.full_name  + ' (OTHER)';
							select.appendChild(option);
						});
					}
				}
			});

			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			break;
			
		case 'Control_m': //MATCH-PROMO
		
			select = document.createElement('select');
			select.id = 'selectMatchPromo';
			select.name = select.id;
			dataToProcess.forEach(function(oop,index,arr1){	
				option = document.createElement('option');
	            option.value = oop.matchnumber;
	            option.text = oop.matchnumber + ' - ' +oop.home_Team.teamName1 + ' Vs ' + oop.away_Team.teamName1 ;
	            select.appendChild(option);
	        });
	        
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			
			cellCount = cellCount + 1;
			break;
			
		case 'F5': case 'f': //BatThisMatch
		
			select = document.createElement('select');
			select.id = 'selectBatsmanThisMatch';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.isCurrentInning == 'YES'){
					inn.battingCard.forEach(function(bc,bc_index,bc_arr){
						option = document.createElement('option');
						option.value = bc.playerId;
						option.text = bc.player.full_name + " - " + bc.status;	
						select.appendChild(option);
					});
				}
			});
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			break;
			
		case 'F9': case 'g': //BallThisMatch
		
			select = document.createElement('select');
			select.id = 'selectBatamanThisMatch';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.isCurrentInning == 'YES'){
					inn.bowlingCard.forEach(function(boc,boc_index,bc_arr){
						option = document.createElement('option');
						option.value = boc.playerId;
						option.text = boc.player.full_name;	
						select.appendChild(option);
					});
				}
			});
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			break;
			
		case 'F6': case 'Shift_O': //HowOut
			select = document.createElement('select');
			select.id = 'selectHowoutPlayers';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.isCurrentInning == 'YES'){
					inn.battingCard.forEach(function(bc,bc_index,bc_arr){
						option = document.createElement('option');
						option.value = bc.playerId;
						option.text = bc.player.full_name + " - " + bc.status;	
						select.appendChild(option);
					});
				}
			});
			
			select.setAttribute('onchange','setDropdownOptionToSelectOptionArray(this, 0)');
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			break;
		case 'F7': case 'Control_d': //Lt Bat Profile 
			select = document.createElement('select');
			select.id = 'selectPlayerName';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.isCurrentInning == 'YES'){
					if(inn.battingTeamId == session_match.setup.homeTeamId){
						session_match.setup.homeSquad.forEach(function(hs,index,arr){
							option = document.createElement('option');
							option.value = hs.playerId;
							option.text = hs.full_name;
							select.appendChild(option);
						});
						session_match.setup.homeOtherSquad.forEach(function(hos,index,arr){
							option = document.createElement('option');
							option.value = hos.playerId;
							option.text = hos.full_name  + ' (OTHER)';
							select.appendChild(option);
						});
					}else {
						session_match.setup.awaySquad.forEach(function(as,index,arr){
							option = document.createElement('option');
							option.value = as.playerId;
							option.text = as.full_name;
							select.appendChild(option);
						});
						session_match.setup.awayOtherSquad.forEach(function(aos,index,arr){
							option = document.createElement('option');
							option.value = aos.playerId;
							option.text = aos.full_name  + ' (OTHER)';
							select.appendChild(option);
						});
					}
				}
			});

			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			
			select = document.createElement('select');
			select.id = 'selectProfile';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = 'DT20';
			option.text = 'T20';
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),1);
			cellCount = cellCount + 1
			break;
		case 'F8'://NameSuper Player
			select = document.createElement('select');
			select.style = 'width:100px';
			select.id = 'selectPlayer';
			select.name = select.id;
			
			session_match.setup.homeSquad.forEach(function(hs,index,arr){
				option = document.createElement('option');
				option.value = hs.playerId;
				option.text = hs.full_name + ' - ' + session_match.setup.homeTeam.teamName4;
				select.appendChild(option);
			});
			session_match.setup.homeOtherSquad.forEach(function(hos,index,arr){
				option = document.createElement('option');
				option.value = hos.playerId;
				option.text = hos.full_name + ' - ' + session_match.setup.homeTeam.teamName4 + ' (OTHER)';
				select.appendChild(option);
			});
			
			session_match.setup.awaySquad.forEach(function(as,index,arr){
				option = document.createElement('option');
				option.value = as.playerId;
				option.text = as.full_name + ' - ' + session_match.setup.awayTeam.teamName4;
				select.appendChild(option);
			});
			session_match.setup.awayOtherSquad.forEach(function(aos,index,arr){
				option = document.createElement('option');
				option.value = aos.playerId;
				option.text = aos.full_name + ' - ' + session_match.setup.awayTeam.teamName4 + ' (OTHER)';
				select.appendChild(option);
			});
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			
			select = document.createElement('select');
			select.style = 'width:100px';
			select.id = 'selectCaptainWicketKeeper';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = 'Player Of The Match';
			option.text = 'Player Of The Match';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'Captain';
			option.text = 'Captain';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'Captain-WicketKeeper';
			option.text = 'Captain-WicketKeeper';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'Wicket_Keeper';
			option.text = 'WicketKeeper';
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),1);
			cellCount = cellCount + 1;
			break;
		case 'F10'://NameSuperDB
			select = document.createElement('select');
			select.style = 'width:130px';
			select.id = 'selectNameSuper';
			select.name = select.id;
			
			dataToProcess.forEach(function(ns,index,arr1){
				option = document.createElement('option');
				option.value = ns.namesuperId;
				option.text = ns.prompt ;
				select.appendChild(option);
			});
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			
			cellCount = cellCount + 1;
			break;
		case 'F11': case 'Control_e': //Lt Ball Profile
			select = document.createElement('select');
			select.id = 'selectPlayerName';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.isCurrentInning == 'YES'){
					if(inn.bowlingTeamId == session_match.setup.homeTeamId){
						session_match.setup.homeSquad.forEach(function(hs,index,arr){
							option = document.createElement('option');
							option.value = hs.playerId;
							option.text = hs.full_name;
							select.appendChild(option);
						});
						session_match.setup.homeOtherSquad.forEach(function(hos,index,arr){
							option = document.createElement('option');
							option.value = hos.playerId;
							option.text = hos.full_name  + ' (OTHER)';
							select.appendChild(option);
						});
					}else {
						session_match.setup.awaySquad.forEach(function(as,index,arr){
							option = document.createElement('option');
							option.value = as.playerId;
							option.text = as.full_name;
							select.appendChild(option);
						});
						session_match.setup.awayOtherSquad.forEach(function(aos,index,arr){
							option = document.createElement('option');
							option.value = aos.playerId;
							option.text = aos.full_name  + ' (OTHER)';
							select.appendChild(option);
						});
					}
				}
			});

			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			
			select = document.createElement('select');
			select.id = 'selectProfile';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = 'DT20';
			option.text = 'T20';
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),1);
			cellCount = cellCount + 1
			break;
		case 'k':
			select = document.createElement('select');
			select.style = 'width:130px';
			select.id = 'selectBugdb';
			select.name = select.id;
			
			dataToProcess.forEach(function(bug,index,arr1){
				option = document.createElement('option');
				option.value = bug.bugId;
				option.text = bug.prompt;
				select.appendChild(option);
			});
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			break;
		}
		option = document.createElement('input');
		option.type = 'button';
		option.name = 'populate_btn';
		option.value = 'Populate Data';
	    option.id = option.name;
	    option.setAttribute('onclick',"processUserSelection(this)");
	    
	    div = document.createElement('div');
	    div.append(option);

		option = document.createElement('input');
		option.type = 'button';
		option.name = 'cancel_graphics_btn';
		option.id = option.name;
		option.value = 'Cancel';
		option.setAttribute('onclick','processUserSelection(this)');

	    div.append(option);
	    
	    row.insertCell(cellCount).appendChild(div);
	    cellCount = cellCount + 1;
		
		document.getElementById('select_graphic_options_div').style.display = '';
		break;
	}
}
