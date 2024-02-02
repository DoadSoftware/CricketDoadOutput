var session_match, session_caption, session_animation;
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
function onPageLoadEvent(whichPage){
	switch(whichPage){
	case 'OUTPUT':
		$("#select_graphic_options_div").empty();
		document.getElementById('selected_inning').innerHTML = 'Selected Inning: ' + document.getElementById('which_inning').value;
		initialiseSelectedOptionsList();
		addItemsToList('HELP-FILE',session_match);
		break;
	}
}
function initialiseSelectedOptionsList()
{
	selected_options = [];
	for(var i = 1; i <= 5; i++) {
	    selected_options.push('');
	}
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
		document.getElementById('primaryVariousOptions').value = dataToProcess.primaryVariousOptions;
		document.getElementById('vizSecondaryIPAddress').value = dataToProcess.secondaryIpAddress;
		document.getElementById('vizSecondaryPortNumber').value = dataToProcess.secondaryPortNumber;
		document.getElementById('vizTertiaryIPAddress').value = dataToProcess.tertiaryIpAddress;
		document.getElementById('vizTertiaryPortNumber').value = dataToProcess.tertiaryPortNumber;
		processUserSelection($('#select_second_broadcaster'));
		break;
		
	case 'UPDATE-MATCH-ON-OUTPUT-FORM':
	
		dataToProcess.match.inning.forEach(function(inn,index,arr){
			document.getElementById('inning1_teamScore_lbl').innerHTML = dataToProcess.match.inning[0].batting_team.teamName4 + ' : ' + 
				parseInt(dataToProcess.match.inning[0].totalRuns) + '-' + parseInt(dataToProcess.match.inning[0].totalWickets) + ' (' + 
				parseInt(dataToProcess.match.inning[0].totalOvers) + '.' + parseInt(dataToProcess.match.inning[0].totalBalls) + ')';
				
			document.getElementById('inning2_teamScore_lbl').innerHTML = dataToProcess.match.inning[1].batting_team.teamName4 + ' : ' + 
				parseInt(dataToProcess.match.inning[1].totalRuns) + '-' + parseInt(dataToProcess.match.inning[1].totalWickets) + ' (' + 
				parseInt(dataToProcess.match.inning[1].totalOvers) + '.' + parseInt(dataToProcess.match.inning[1].totalBalls) + ')';
			
			if(inn.isCurrentInning == 'YES'){
				inn.battingCard.forEach(function(bc,index,arr){
					if(inn.partnerships != null && inn.partnerships.length > 0) {
						inn.partnerships.forEach(function(par,index,arr){
							if(bc.playerId == par.firstBatterNo) {
								if(bc.status == 'OUT'){
									document.getElementById('inning1_battingcard1_lbl').innerHTML = bc.player.surname + ' ' + bc.runs + '(' + bc.balls + ')' ;
									document.getElementById('inning1_battingcard1_lbl').style.color = 'red';
								}else{
									if(bc.onStrike == 'YES'){
										document.getElementById('inning1_battingcard1_lbl').innerHTML = bc.player.surname + '*' + ' ' + bc.runs + '(' + bc.balls + ')' ;
									}else{
										document.getElementById('inning1_battingcard1_lbl').innerHTML = bc.player.surname + ' ' + bc.runs + '(' + bc.balls + ')' ;
									}
									document.getElementById('inning1_battingcard1_lbl').style.color = 'green';
								}
							}
							else if(bc.playerId == par.secondBatterNo) {
								if(bc.status == 'OUT'){
									document.getElementById('inning1_battingcard2_lbl').innerHTML = bc.player.surname + ' ' + bc.runs + '(' + bc.balls + ')' ;
									document.getElementById('inning1_battingcard2_lbl').style.color = 'red';
								}else{
									if(bc.onStrike == 'YES'){
										document.getElementById('inning1_battingcard2_lbl').innerHTML = bc.player.surname + '*' + ' ' + bc.runs + '(' + bc.balls + ')' ;
									}else{
										document.getElementById('inning1_battingcard2_lbl').innerHTML = bc.player.surname + ' ' + bc.runs + '(' + bc.balls + ')' ;
									}
									document.getElementById('inning1_battingcard2_lbl').style.color = 'green';
								}
							}
						});
					}
				});
				inn.bowlingCard.forEach(function(boc,index,arr){
					if(boc.status == 'CURRENTBOWLER' || boc.status == 'LASTBOWLER'){
						document.getElementById('inning1_bowlingcard_lbl').innerHTML = boc.player.surname + ' ' + boc.wickets 
									+ '-' + boc.runs + '(' + boc.overs + '.' + boc.balls + ')';
					}
				});	
			}
		});
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
		//processCricketProcedures("POPULATE-GRAPHICS", $('#which_keypress').val() + ',' + selected_options.toString());
		if($(key_press_hidden_input)) {
			processCricketProcedures("POPULATE-GRAPHICS", $('#key_press_hidden_input').val() + ',' + selected_options.toString());
		} else {
			processCricketProcedures("POPULATE-GRAPHICS", $('#which_keypress').val() + ',' + selected_options.toString());
		}
		break;
	}	
}

function processUserSelectionData(whatToProcess,dataToProcess)
{
	switch (whatToProcess) {
	case 'LOGGER_FORM_KEYPRESS':

/*		if(!$('#select_graphic_options_div').is(':empty')) {
			alert('All keypress are disabled while SELECTION part is on screen');
			return false;
		}else{
			document.getElementById('which_keypress').value = dataToProcess;
		}*/
		
		document.getElementById('which_keypress').value = dataToProcess;
		
		switch(dataToProcess) {
		case 'SPEED':
			processCricketProcedures('SHOW_SPEED');
			break;
		case 'Alt_r':
			processCricketProcedures('RE_READ_DATA');
			break;
		case 'Control_ ':
			processCricketProcedures('CLEAR-ALL-WITH-INFOBAR');
			break;
		case ' ':
			processCricketProcedures('CLEAR-ALL');
			break;
		case '1': case '2': case '3': case '4':
			if(session_match.setup.maxOvers > 0){
				switch (dataToProcess) {
				case '3': case '4': // Key 1 to 4
					alert("3rd and 4th inning NOT available in a limited over match");
					return false;
				}				
			}
			document.getElementById('which_inning').value = dataToProcess;
			document.getElementById('selected_inning').innerHTML = 'Selected Inning: ' + dataToProcess;
			break;
			
		case '-':
			
			if(confirm('It will Also Delete Your Preview from Directory...\r\n \r\nAre You Sure To Animate Out? ') == true){
				processCricketProcedures('ANIMATE-OUT-GRAPHICS');
			}
			break;
			
		case 'Alt_-':
			
			if(confirm('Animate Out Infobar? ') == true){
				processCricketProcedures('ANIMATE-OUT-INFOBAR');
			}
			break;
		
		default:
			
			switch($('#which_inning').val()) {
			case 1: case 2: case 3: case 4:
				alert('Selected inning must be between 1 to 4 [found ' 
					+ document.getElementById('which_inning').value + ']');
				return false;
			}
			
			switch(dataToProcess) {
			case 'F12': case 'Alt_1': case 'Alt_2': case 'Alt_3': case 'Alt_4': case 'Alt_5': case 'Alt_7': case 'Alt_8': //case 'Alt_6':
			case 'Control_F5': case 'Control_F8': case 'Control_F9': case 'F4': case 'F5': case 'F6' : case 'F7': case 'Alt_F8':
			case 'F8': case 'F9': case 'F11': case 's': case 'q': case 'Shift_F5': case 'Shift_F9': case 'Shift_F6': case 'Control_y':
			case 'Shift_K': case 'Shift_O': case 'g': case 'f': case 'Control_g': case 'Control_s': case 'Control_f': //case 'Alt_F9':
			case 'Control_h': case 'Alt_F12': case 'l': case 'p': case 'Alt_m': case 'Alt_n': case 'Control_b': case 'Alt_F10': case 'Alt_d':
			case 'Control_p': case 'Shift_F4': case 'Alt_F1': case 'Alt_F2': case 'Shift_E': case 'Shift_P': case 'Shift_Q': case 'Alt_z':
				addItemsToList(dataToProcess,null);
				break;
			case 'Shift_F10': case 'Shift_F11': case 'm': case 'F1': case 'F2': case 'Control_F1': case 'Control_a':
			case 'Alt_k':  case 'Shift_F3': case 'd': case 'e': case 'Control_F6': //case 'Control_F7': 
			case 'Control_k': case 'Control_F10': case 'Control_F3': case 'n': case 'a': case 't': case 'h':
			case 'Shift_F1': case 'Shift_F2': case 'Shift_D': case 'Control_q': case 'Control_b': case 'o': case 'Control_F2': case 'b':
			case 'Alt_F11':
				dataToProcess = dataToProcess + ',' + document.getElementById('which_inning').value;
				processCricketProcedures("POPULATE-GRAPHICS", dataToProcess);
				break;
			//These buttons will animate in & animate out graphics
			case 'Alt_p':
				if(session_animation != null && session_animation.specialBugOnScreen == 'TOSS') {
					processCricketProcedures("ANIMATE-OUT-GRAPHICS", dataToProcess);
				} else {
					addItemsToList(dataToProcess,null);
					//processCricketProcedures("POPULATE-GRAPHICS", dataToProcess);
				}
				break;
			//All key presses which doesn't require graphics population will come here
			case '5': case '6': case '7': case '8': case '9':
				processCricketProcedures("QUIDICH-COMMANDS", dataToProcess);
				break;
			case 'Alt_f': case 'Alt_g': case 'ArrowDown': case 'ArrowUp': case 'w': case 'i': case 'y': case 'u':
				dataToProcess = dataToProcess + ',' + document.getElementById('which_inning').value;
				processCricketProcedures("ANIMATE-IN-GRAPHICS", dataToProcess);
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
	case 'QUIDICH-COMMANDS':
		valueToProcess = dataToProcess;
		break;
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
				if(whatToProcess == "RE_READ_DATA"){
					alert("Data is Loaded");
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
				switch(whatToProcess) {	
				case 'POPULATE-GRAPHICS':
					if(data.status == 'OK') {
						session_caption = data;
						if(confirm('Animate In?') == true) {
							$('.my_waiting_modal').modal('show');
							setTimeout(function(){$('.my_waiting_modal').modal('hide');},3000);
							processCricketProcedures(whatToProcess.replace('POPULATE-', 'ANIMATE-IN-'),dataToProcess);
							//alert(dataToProcess);
							if(dataToProcess.split(',')[0] != 'Control_F9' && 
							dataToProcess.split(',')[0] != 'Control_F5'){
								$("#select_graphic_options_div").empty();
								document.getElementById('select_graphic_options_div').style.display = 'none';
								$("#captions_div").show();
							}
						}
					} else {
						if(data.status != 'YES'){
							alert(data.status);
						}	
						$("#select_graphic_options_div").empty();
						document.getElementById('select_graphic_options_div').style.display = 'none';
						$("#captions_div").show();
					}
					break;
				case 'GRAPHICS-OPTIONS':
					addItemsToList(dataToProcess,data);
					break;
				default:
					if(whatToProcess.includes("ANIMATE-IN-") || whatToProcess.includes("ANIMATE-OUT-")) {
						session_animation = data;	
					}
					break;
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
function setTextBoxOptionToSelectOptionArray(whichIndex)
{
	selected_options[0] = document.getElementById('which_inning').value;
	selected_options[whichIndex+1] = $('#selectFreeText').val();
}
function addItemsToList(whatToProcess,dataToProcess)
{
	var select,option,header_text,div,table,tbody,row;
	var cellCount = 0;
	
	switch(whatToProcess) {	
/*	case 'HELP-FILE':
		
		$('#help_file_div').empty();

		table = document.createElement('table');
		table.setAttribute('class', 'table table-bordered');
				
		tbody = document.createElement('tbody');

		table.appendChild(tbody);
		document.getElementById('help_file_div').appendChild(table);
		
		for(var iRow=0; iRow<=1; iRow++){
			row = tbody.insertRow(tbody.rows.length);
			for(var iCol=0; iCol<=2; iCol++){
				header_text = document.createElement('h6');
				switch(iRow){
				case 0:
					switch(iCol){
					case 0:
						header_text.innerHTML = 'Ful Framers';
						break;
					case 1:
						header_text.innerHTML = 'Lower Thirds';
						break;
					case 2:
						header_text.innerHTML = 'Infobar';
						break;
					}
					break;
				case 1:
					switch(iCol){
					case 0:
						header_text.innerHTML = 'F1: Scorecard';
						break;
					case 1:
						header_text.innerHTML = 'F4: partnership';
						break;
					case 2:
						header_text.innerHTML = 'Alt+1: Bottom left options';
						break;
					}
					break;
				}
				row.insertCell(iCol).appendChild(header_text);
			}
		}
		break;*/
		
	case 'Control_m': case 'F4': case 'F5': case 'F6': case 'F8': case 'F9': case 'F10': //case 'F7': case 'F11':
	case 'Control_F5': case 'Control_F9': case 'Control_F8': case 's': case 'p': case 'Control_p': //case 'Control_d': case 'Control_e':
	case 'z': case 'x': case 'c': case 'v': case 'Control_F11': case 'Control_y': case 'Alt_F8': case 'Alt_F1': case 'Alt_F2':
	case 'Shift_K': case 'Shift_O': case 'k': case 'g': case 'f': case 'Shift_F5': case 'Shift_F9': case 'Control_h': case 'Control_g': case 'q':
	case 'j': case 'Shift_F6': case 'Control_s':  case 'Control_f': case 'Alt_F12': case 'l': case 'Shift_E': //case 'Alt_F9':
	case 'F12': case 'Alt_1': case 'Alt_2': case 'Alt_3': case 'Alt_4': case 'Alt_5': case 'Alt_6': case 'Alt_7': case 'Alt_8': case 'Alt_9': case 'Alt_0':
	case 'Alt_m': case 'Alt_n': case 'Control_b': case 'Alt_p': case 'Alt_F10': case 'Alt_d': case 'Shift_F4': case 'Alt_a': case 'Alt_s': 
	case 'Shift_P': case 'Shift_Q': case 'Alt_z':  case 'Control_z': case 'Control_x':
	 //InfoBar LeftBottom-Middle-BatPP-BallPP-LastXBalls-Batsman/Sponsor-RightBottom
		
		$("#captions_div").hide();
		$('#select_graphic_options_div').empty();
   		initialiseSelectedOptionsList();

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
		case 'Shift_F4':
			header_text.innerHTML = 'BUG MULTI PARTNERSHIP';
			select = document.createElement('select');
			select.id = 'selectPartnership';
			select.name = select.id;
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
					for(var i=1; i<=inn.partnerships.length; i++) {
			            option = document.createElement('option');
						option.value = i;
						option.text = i;
						select.appendChild(option);
					}
				}
			});
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
		break;
		case 'F12':
			header_text.innerHTML = 'MAIN INFOBAR';
			
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
			option.value = 'GROUP';
			option.text = 'Group';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'VENUE';
			option.text = 'Venue Name';
			select.appendChild(option);
			
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
						option.text = 'Target';
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
			header_text.innerHTML = 'LEFT BOTTON INFOBAR SECTION';
	
			select = document.createElement('select');
			select.id = 'selectLeftBottom';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = 'GROUP';
			option.text = 'Group';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'VENUE';
			option.text = 'Venue Name';
			select.appendChild(option);

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
						
						option = document.createElement('option');
						option.value = 'INNINGS_SCORE';
						option.text = '1st Inning Score';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = 'EQUATION';
						option.text = 'Equation';
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
			header_text.innerHTML = 'MIDDLE INFOBAR SECTION';
			
			select = document.createElement('select');
			select.id = 'selectMiddleStat';
			select.name = select.id;

			option = document.createElement('option');
			option.value = 'BATSMAN';
			option.text = 'Batsman/Bowler';
			select.appendChild(option);
			
			/*option = document.createElement('option');
			option.value = 'BATSMAN_TOURNAMENT';
			option.text = 'Batsman/Tournament';
			select.appendChild(option);*/
			
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
			option.text = 'Current Partnership';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'EXTRAS';
			option.text = 'Extras';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'FOW';
			option.text = 'Fall Of Wickets';
			select.appendChild(option);

			option = document.createElement('option');
			option.value = 'LAST_WICKET';
			option.text = 'Last Wicket';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'BALLS_SINCE_LAST_BOUNDARY';
			option.text = 'Balls Since Last Boundary';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'ARAMCO_POTD';
			option.text = 'Aramco POTD';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'CRICTOS';
			option.text = 'Crictos';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'THIS_MATCH_SIXES';
			option.text = 'This Match Sixes';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'TOURNAMENT_SIXES';
			option.text = 'Tournament Sixes';
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
						/*option = document.createElement('option');
						option.value = 'TARGET';
						option.text = 'Target';
						select.appendChild(option);*/
						
						option = document.createElement('option');
						option.value = 'EQUATION';
						option.text = 'Equation';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = 'RESULTS';
						option.text = 'Result';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = 'DLS_PAR_SCORE';
						option.text = 'D/L Par Score';
						select.appendChild(option);
					}
				}
			});
			
			row.insertCell(cellCount).appendChild(select);
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			
			break;
		
		case 'Alt_5':
			header_text.innerHTML = 'MIDDLE INFOBAR SECTION - LAST x BALLS';
		
			select = document.createElement('input');
			select.type = "text";
			select.id = 'selectFreeText';
			select.value = '10';
			
			select.setAttribute('onchange',"setTextBoxOptionToSelectOptionArray(0)");
			row.insertCell(cellCount).appendChild(select);
			setTextBoxOptionToSelectOptionArray(0);
			cellCount = cellCount + 1;
			
			break;
			
		case 'Alt_6':
			header_text.innerHTML = 'MIDDLE INFOBAR SECTION - BAT & SPONSOR';
		
			select = document.createElement('select');
			select.id = 'selectWhichSponsor';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = '1';
			option.text = 'DP World';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = '2';
			option.text = 'IndusInd Bank';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = '3';
			option.text = 'Emirates';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = '4';
			option.text = 'Coco Cola';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = '5';
			option.text = 'Aramco';
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1
			break;
			
		case 'Alt_7':
			header_text.innerHTML = 'RIGHT BOTTOM INFOBAR SECTION';
			
			select = document.createElement('select');
			select.id = 'selectRightBottom';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = 'OVER';
			option.text = 'This Over';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'BOWLING_END';
			option.text = 'Bowling End';
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1
			break;
			
		case 'Alt_8':
			header_text.innerHTML = 'RIGHT INFOBAR SECTION';
		
			select = document.createElement('select');
			select.id = 'selectRightSection';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = 'BOWLER';
			option.text = 'Bowler';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'BOUNDARY';
			option.text = 'Innings Boundaries';
			select.appendChild(option);
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.isCurrentInning == 'YES'){
					if(inn.inningNumber == 2){
						option = document.createElement('option');
						option.value = 'COMPARE';
						option.text = 'Compare';
						select.appendChild(option);
					}
				}
			});
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1
			break;
			
		case 'Alt_9':
			header_text.innerHTML = 'MIDDLE INFOBAR SECTION - FREE TEXT';
		
			select = document.createElement('select');
			select.id = 'selectInfoBarStats';
			select.name = select.id;
			
			dataToProcess.forEach(function(pro,index,arr1){
				option = document.createElement('option');
				option.value = pro.order;
				option.text = pro.order + '-' + pro.prompt ;
				select.appendChild(option);
			});
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1
			break;
			
		case 'Alt_0':
			header_text.innerHTML = 'MIDDLE INFOBAR SECTION - COMMANTATORS';
		
			select = document.createElement('select');
			select.id = 'selectInfoBarComm1';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = '0';
			option.text = "";
			select.appendChild(option);
			
			dataToProcess.forEach(function(comm,index,arr1){
				if(comm.useThis == 'Yes'){
					option = document.createElement('option');
					option.value = comm.commentatorId;
					option.text = comm.commentatorName;
					select.appendChild(option);
				}
			});
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1
			
			select = document.createElement('select');
			select.id = 'selectInfoBarComm2';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = '0';
			option.text = "";
			select.appendChild(option);
			
			dataToProcess.forEach(function(comm,index,arr1){
				if(comm.useThis == 'Yes'){
					option = document.createElement('option');
					option.value = comm.commentatorId;
					option.text = comm.commentatorName;
					select.appendChild(option);
				}
			});
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),1);
			cellCount = cellCount + 1
			
			select = document.createElement('select');
			select.id = 'selectInfoBarComm3';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = '0';
			option.text = "";
			select.appendChild(option);
			
			dataToProcess.forEach(function(comm,index,arr1){
				if(comm.useThis == 'Yes'){
					option = document.createElement('option');
					option.value = comm.commentatorId;
					option.text = comm.commentatorName;
					select.appendChild(option);
				}
			});
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 2)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),2);
			cellCount = cellCount + 1
			break;
		case 'Shift_E':
		 	header_text.innerHTML = 'LT - EXTRAS';
		
			select = document.createElement('select');
			select.id = 'selectExtras';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = '1';
			option.text = '1st Inning';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = '2';
			option.text = '2nd Inning';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'totalExtras';
			option.text = 'Total Extras';
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1
			break;
		case 'Alt_d':
			header_text.innerHTML = 'LT - DLS PAR SCORE';
		
			select = document.createElement('select');
			select.id = 'selectSponsor';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = 'currentOver';
			option.text = 'Current Over';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'nextBall';
			option.text = 'Next Ball';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'nextOver';
			option.text = 'Next Over';
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1
			break;
			
		case 'Shift_K': case 'F4':
			switch(whatToProcess) {
			case 'F4':
				header_text.innerHTML = 'FF ALL PARTNERSHIP';
				break;
			case 'Shift_K':
				header_text.innerHTML = 'FF CURRENT PARTNERSHIP';
				break;
			}
		
			select = document.createElement('select');
			select.id = 'selectSponsor';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = 'cocaCola';
			option.text = 'Coca Cola';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'noSponsor';
			option.text = 'withoutSponsor';
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1
			break;

		case 'Control_h': case 'Control_g': case 'Control_y':
			header_text.innerHTML = 'POWERPLAY';

			select = document.createElement('select');
			select.id = 'selectPowerplay';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = 'p1';
			option.text = 'Powerplay 1';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'p2';
			option.text = 'Powerplay 2';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'p3';
			option.text = 'Powerplay 3';
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1
			break;	
			
		case 's':
			
			header_text.innerHTML = '50-100 SPLIT';
			
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
		case 'Control_s':
			header_text.innerHTML = 'LT THIS SERIES BAT';
			
			select = document.createElement('select');
			select.id = 'selectPlayerName';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
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
			
		case 'Control_F5': case 'Control_b'://Batsman Style

			switch(whatToProcess) {
			case 'Control_F5':
				header_text.innerHTML = 'BAT STYLE';
				break;
			case 'Control_b':
				header_text.innerHTML = 'BATTER IN AT';
				break;
			}
			select = document.createElement('select');
			select.id = 'selectPlayerName';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
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
		 case 'Alt_m':  case 'Shift_P':
			switch(whatToProcess) {	
			case 'Alt_m':
				header_text.innerHTML = 'BATSMAN MILESTONE';
				break;
			case 'Shift_P':
				header_text.innerHTML = 'BATSMAN THIS SERIES';
				break;
			}
			select = document.createElement('select');
			select.id = 'selectPlayerName';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
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
			
			switch(whatToProcess){
			case 'Shift_P':
				select = document.createElement('select');
				select.id = 'selectStatType';
				select.name = select.id;
				
				option = document.createElement('option');
				option.value = '0';
				option.text = '';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '1';
				option.text = 'Matches';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '2';
				option.text = 'Runs';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '3';
				option.text = 'Fifties';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '4';
				option.text = 'Hundreds';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '5';
				option.text = 'Average';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '6';
				option.text = 'Strike Rate';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '7';
				option.text = 'Best';
				select.appendChild(option);
				
				select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 2)");
				row.insertCell(cellCount).appendChild(select);
				setDropdownOptionToSelectOptionArray($(select),2);
				cellCount = cellCount + 1
				break;
			}
			break;	
		case 'Alt_p':
			header_text.innerHTML = 'BUG TOSS';
			
			select = document.createElement('select');
			select.id = 'selectTeams';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = session_match.setup.homeTeam.teamName1 + '-' + 'BAT';
			option.text = session_match.setup.homeTeam.teamName1 + '-' + 'BAT';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = session_match.setup.homeTeam.teamName1 + '-' + 'FIELD';
			option.text = session_match.setup.homeTeam.teamName1 + '-' + 'FIELD';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = session_match.setup.awayTeam.teamName1 + '-' + 'BAT';
			option.text = session_match.setup.awayTeam.teamName1 + '-' + 'BAT';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = session_match.setup.awayTeam.teamName1 + '-' + 'FIELD';
			option.text = session_match.setup.awayTeam.teamName1 + '-' + 'FIELD';
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			break;
			
		case 'Alt_a': case 'Alt_s':
			switch(whatToProcess) {
			case 'Alt_a':
				header_text.innerHTML = 'LT HOME TEAM STAFF';
				break;
			case 'Alt_s':
				header_text.innerHTML = 'LT AWAY TEAM STAFF';
				break;
			}
			
			select = document.createElement('select');
			select.id = 'selectTeams';
			select.name = select.id;
			
			dataToProcess.forEach(function(st,index,arr1){
				option = document.createElement('option');
				option.value = st.staffId;
				option.text = st.staffName + ' - ' + st.role + ' (' + st.team.teamName1 + ')';
				select.appendChild(option);
			});
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			
			break;	
			
		case 'Control_F8': case 'Alt_F9': case 'Alt_F12': case 'Alt_F10': case 'Alt_z':
			switch(whatToProcess) {
			case 'Control_F8':
				header_text.innerHTML = 'FF PLAYING XI';
				break;
			case 'Alt_F12':
				header_text.innerHTML = 'TEAM 0,1,2';
				break;	
			case 'Alt_F10':
				header_text.innerHTML = 'SINGLE TEAM (THIS SERIES)';
				break;
			case 'Alt_z':
				header_text.innerHTML = 'SQUAD';
				break;
			}
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
			
			switch(whatToProcess){
			case 'Alt_z':
				select = document.createElement('select');
				select.id = 'selectType';
				select.name = select.id;
				
				option = document.createElement('option');
				option.value = 'role';
				option.text = 'Role';
				select.appendChild(option);
				
				/*option = document.createElement('option');
				option.value = 'matches';
				option.text = 'Matches';
				select.appendChild(option);*/
				
				option = document.createElement('option');
				option.value = 'runs';
				option.text = 'Runs';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'wickets';
				option.text = 'Wickets';
				select.appendChild(option);
				
				select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
				row.insertCell(cellCount).appendChild(select);
				setDropdownOptionToSelectOptionArray($(select),1);
				cellCount = cellCount + 1
				break;
			case 'Alt_F9': case 'Alt_F10':
				
				select = document.createElement('select');
				select.id = 'selectStyle';
				select.name = select.id;
				
				option = document.createElement('option');
				option.value = 'age';
				option.text = 'Age';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'batting';
				option.text = 'Batting';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'bowling';
				option.text = 'Bowling';
				select.appendChild(option);
				
				select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
				row.insertCell(cellCount).appendChild(select);
				setDropdownOptionToSelectOptionArray($(select),1);
				cellCount = cellCount + 1
				
				select = document.createElement('select');
				select.id = 'selectType';
				select.name = select.id;
				
				option = document.createElement('option');
				option.value = 'runs';
				option.text = 'Runs';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'average';
				option.text = 'Average';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'strikeRate';
				option.text = 'Strike Rate';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'wickets';
				option.text = 'Wickets';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'economy';
				option.text = 'Economy';
				select.appendChild(option);
				
				select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 2)");
				row.insertCell(cellCount).appendChild(select);
				setDropdownOptionToSelectOptionArray($(select),2);
				cellCount = cellCount + 1
			}
			break;
			
		case 'z': case 'x': case 'c': case 'v': case 'Control_z': case 'Control_x':
			switch(whatToProcess) {
			case 'z':
				header_text.innerHTML = 'LEADERBOARD - MOST RUNS';
				break;
			case 'x':
				header_text.innerHTML = 'LEADERBOARD - MOST WICKETS';
				break;	
			case 'c':
				header_text.innerHTML = 'LEADERBOARD - MOST FOURS';
				break;	
			case 'v':
				header_text.innerHTML = 'LEADERBOARD - MOST SIXES';
				break;	
			case 'Control_z':
			    header_text.innerHTML = 'LEADERBOARD - HIGHEST SCORES';
				break;		
			case 'Control_x':
			    header_text.innerHTML = 'LEADERBOARD - BEST FIGURES';
				break;	
			}
		
			select = document.createElement('select');
			select.id = 'selectPlayerName';
			select.name = select.id;
			for(i=0;i<dataToProcess.length;i++){
				if(i<5){
					option = document.createElement('option');
		            option.value = (i+1)+ "_" + dataToProcess[i].playerId;
		            option.text = dataToProcess[i].player.full_name;
		            select.appendChild(option);
				}
			}
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1
			break;
		
		case 'Control_f':
			
			switch(whatToProcess) {
			case 'Control_f':
				header_text.innerHTML = 'LT BALL THIS SERIES';
				break;		
			}
			
			select = document.createElement('select');
			select.id = 'selectPlayerName';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
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
				
		case 'Control_F9': //BowlerStyle
			
			switch(whatToProcess) {
			case 'Control_F9':
				header_text.innerHTML = 'BALL STYLE';
				break;		
			}
			
			select = document.createElement('select');
			select.id = 'selectPlayerName';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
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
			
			switch(whatToProcess) {
			case 'Control_F9':
				select = document.createElement('select');
				select.id = 'selectBowlingEnd';
				select.name = select.id;
				
				option = document.createElement('option');
				option.value = session_match.setup.ground.first_bowling_end;
				option.text = session_match.setup.ground.first_bowling_end;
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = session_match.setup.ground.second_bowling_end;
				option.text = session_match.setup.ground.second_bowling_end;
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'WITHOUTEND';
				option.text = 'WITHOUT END';
				select.appendChild(option);
				
				select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
				row.insertCell(cellCount).appendChild(select);
				setDropdownOptionToSelectOptionArray($(select),1);
				cellCount = cellCount + 1
				break;			
			}
			
			break;
		case 'Alt_n': case 'Shift_Q'://Bowler milestone
			
			switch(whatToProcess) {
			case 'Alt_n':
				header_text.innerHTML = 'BOWLER MILESTONE';
				break;	
			case 'Shift_Q':
				header_text.innerHTML = 'BOWLER THIS SERIES';
				break;		
			}
			
			select = document.createElement('select');
			select.id = 'selectPlayerName';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
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
			switch(whatToProcess){
			case 'Shift_Q':
				select = document.createElement('select');
				select.id = 'selectStatType';
				select.name = select.id;
				
				option = document.createElement('option');
				option.value = '0';
				option.text = '';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '1';
				option.text = 'Matches';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '2';
				option.text = 'Wickets';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '3';
				option.text = 'Economy';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '4';
				option.text = 'Average';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '5';
				option.text = 'Strike Rate';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '6';
				option.text = '5WI';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '7';
				option.text = 'Best Fig';
				select.appendChild(option);
				
				select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 2)");
				row.insertCell(cellCount).appendChild(select);
				setDropdownOptionToSelectOptionArray($(select),2);
				cellCount = cellCount + 1
				break;
			}
			
			break;
			
		case 'Control_m': case 'Control_F11': //MATCH-PROMO - PreviousMatchSummary 
			
			switch(whatToProcess) {
			case 'Control_m':
				header_text.innerHTML = 'FF MATCH PROMO';
				break;
			case 'Control_F11':
				header_text.innerHTML = 'FF PREVIOUS MATCH SUMMARY';
				break;
			}
			
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
		case 'f':
			select = document.createElement('select');
			select.id = 'selectBatsmanThisMatch';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
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
		case 'Alt_F1':
			switch(whatToProcess) {
			case 'Alt_F1':
				header_text.innerHTML = 'BAT GRIFF';
				break;						
			}
			select = document.createElement('select');
			select.id = 'selectBatsmanThisMatch';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
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
			
		case 'Alt_F2':
			switch(whatToProcess) {
			case 'Alt_F2':
				header_text.innerHTML = 'BALL GRIFF';
				break;						
			}
			select = document.createElement('select');
			select.id = 'selectBatsmanThisMatch';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
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
		case 'F5':
			switch(whatToProcess) {
			case 'F5':
				header_text.innerHTML = 'BAT THIS MATCH';
				break;
			}
			select = document.createElement('select');
			select.id = 'selectBatsmanThisMatch';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
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
		case 'Alt_F1':
			switch(whatToProcess) {
			case 'Alt_F1':
				header_text.innerHTML = 'BAT GRIFF';
				break;					
			}
			select = document.createElement('select');
			select.id = 'selectBatsmanThisMatch';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
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
		case 'Shift_F5': case 'q'://BatThisMatch
			switch(whatToProcess) {
			case 'Shift_F5':
				header_text.innerHTML = 'BAT 0,1,2';
				break;
			case 'Q':
				header_text.innerHTML = 'PLAYER BOUNDARY';
				break;				
			}
			select = document.createElement('select');
			select.id = 'selectBatsmanThisMatch';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
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
		
		case 'g':
			select = document.createElement('select');
			select.id = 'selectBatamanThisMatch';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
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
		
		case 'F9': 
			switch(whatToProcess) {
			case 'F9':
				header_text.innerHTML = 'BALL THIS MATCH';
				break;				
			}
			
			select = document.createElement('select');
			select.id = 'selectBatamanThisMatch';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
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
		case 'Shift_F9': //BallThisMatch
			
			switch(whatToProcess) {
			case 'Shift_F9':
				header_text.innerHTML = 'BALL 0,1,2';
				break;				
			}
			
			select = document.createElement('select');
			select.id = 'selectBatamanThisMatch';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
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
		case 'Control_p':
			select = document.createElement('select');
			select.id = 'selectGroups';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = 'Group1';
			option.text = 'Group 1';	
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'Group2';
			option.text = 'Group 2';	
			select.appendChild(option);
			
			select.setAttribute('onchange','setDropdownOptionToSelectOptionArray(this, 0)');
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			break;
		case 'p':
			select = document.createElement('select');
			select.id = 'selectGroups';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = 'GroupA';
			option.text = 'Group A';	
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'GroupB';
			option.text = 'Group B';	
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'GroupC';
			option.text = 'Group C';	
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'GroupD';
			option.text = 'Group D';	
			select.appendChild(option);
			
			select.setAttribute('onchange','setDropdownOptionToSelectOptionArray(this, 0)');
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			break;
		case 'Shift_O':
			select = document.createElement('select');
			select.id = 'selectHowoutPlayers';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
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
		case 'F6': case 'Shift_F6': //HowOut
			switch(whatToProcess) {
			case 'F6':
				header_text.innerHTML = 'HOW OUT';
				break;
			case 'Shift_F6':
				header_text.innerHTML = 'HOW OUT WITHOUT FIELDER';
				break;					
			}
			select = document.createElement('select');
			select.id = 'selectHowoutPlayers';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
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
		case 'l':
		
			select = document.createElement('select');
			select.id = 'selectType';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = 'Economy';
			option.text = 'This match - First (Economy)';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'Catches';
			option.text = 'This Match - Second (Catches)';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'Career';
			option.text = 'Career';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'Tournament';
			option.text = 'Tournament';
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			
			select = document.createElement('select');
			select.id = 'selectPlayerName';
			select.name = select.id;
			
			session_match.setup.homeSquad.forEach(function(hs,index,arr){
				if(hs.role == 'All-rounder'){
					option = document.createElement('option');
					option.value = hs.playerId;
					option.text = hs.full_name+' -('+session_match.setup.homeTeam.teamName4+')';
					select.appendChild(option);	
				}
			});

			session_match.setup.homeOtherSquad.forEach(function(hos,index,arr){
				if(hos.role == 'All-rounder'){
				option = document.createElement('option');
				option.value = hos.playerId;
				option.text = hos.full_name  + ' (OTHER)'+' -('+session_match.setup.homeTeam.teamName4+')';
				select.appendChild(option);
			}
			});
				session_match.setup.awaySquad.forEach(function(as,index,arr){
				if(as.role == 'All-rounder'){
				option = document.createElement('option');
				option.value = as.playerId;
				option.text = as.full_name+' -('+session_match.setup.awayTeam.teamName4+')';
				select.appendChild(option);
				}
			});
			session_match.setup.awayOtherSquad.forEach(function(aos,index,arr){
				if(aos.role == 'All-rounder'){
				option = document.createElement('option');
				option.value = aos.playerId;
				option.text = aos.full_name  + ' (OTHER)'+' -('+session_match.setup.awayTeam.teamName4+')';
				select.appendChild(option);
				}
			});
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),1);
			cellCount = cellCount + 1;
			break;
		case 'F7': case 'Control_d'://Lt Bat Profile
		
			switch(whatToProcess){
				case 'F7':
				header_text.innerHTML = 'BAT PLAYER PROFILE';
				break;
				case 'control_d':
				header_text.innerHTML = 'BAT PLAYER PROFILE';
				break;
			}
			
			select = document.createElement('select');
			select.id = 'selectPlayerName';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
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
			option.value = 'U19ODI';
			option.text = 'U19 ODI';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'LIST A';
			option.text = 'LIST A';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'ACCU19';
			option.text = 'ACC U19';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'SA TRI-NATION 2023-24';
			option.text = 'SA TRI-NATION';
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),1);
			cellCount = cellCount + 1
			
			switch(whatToProcess){
			case 'Control_d':
				select = document.createElement('select');
				select.id = 'selectStatType';
				select.name = select.id;
				
				option = document.createElement('option');
				option.value = '0';
				option.text = '';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '1';
				option.text = 'Matches';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '2';
				option.text = 'Runs';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '3';
				option.text = 'Fifties';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '4';
				option.text = 'Hundreds';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '5';
				option.text = 'Average';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '6';
				option.text = 'Strike Rate';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '7';
				option.text = 'Best';
				select.appendChild(option);
				
				select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 2)");
				row.insertCell(cellCount).appendChild(select);
				setDropdownOptionToSelectOptionArray($(select),2);
				cellCount = cellCount + 1
				break;
			}
			break;		
		case 'Alt_3':
		
			switch(whatToProcess){
				case 'Alt_3':
				header_text.innerHTML = 'MIDDLE INFOBAR SECTION - BAT PLAYER PROFILE';
				break;
			}
			
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
			option.value = 'U19ODI';
			option.text = 'U19 ODI';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'LIST A';
			option.text = 'LIST A';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'ACCU19';
			option.text = 'ACC U19';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'SA TRI-NATION 2023-24';
			option.text = 'SA TRI-NATION';
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),1);
			cellCount = cellCount + 1
			
			break;
			
		case 'F8': case 'Alt_F8'://NameSuper Player
			switch(whatToProcess){
			case 'F8':
				header_text.innerHTML = 'HOME TEAM NAMESUPER PLAYER';
				
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
				
				select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
				row.insertCell(cellCount).appendChild(select);
				setDropdownOptionToSelectOptionArray($(select),0);
				cellCount = cellCount + 1;
				break;
			case 'Alt_F8':
				header_text.innerHTML = 'AWAY TEAM NAMESUPER PLAYER';
				
				select = document.createElement('select');
				select.style = 'width:100px';
				select.id = 'selectPlayer';
				select.name = select.id;
				
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
				break;
				
			}
			
			select = document.createElement('select');
			select.style = 'width:100px';
			select.id = 'selectCaptainWicketKeeper';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = 'Team';
			option.text = 'Team';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'Player Of The Match';
			option.text = 'Player Of The Match';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'Captain';
			option.text = 'Captain';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'Captain Wicket-Keeper';
			option.text = 'Captain-WicketKeeper';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'Wicket-Keeper';
			option.text = 'WicketKeeper';
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),1);
			cellCount = cellCount + 1;
			break;
		case 'F10': case 'j': //NameSuperDB
			switch(whatToProcess){
			case 'F10':
				header_text.innerHTML = 'NAMESUPER DATABASE';
				break;
			case 'j':
				header_text.innerHTML = 'NAMESUPER SINGLE LINE';
				break;
			}
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
		case 'Alt_4': 
			switch(whatToProcess){
			case 'Alt_4':
			header_text.innerHTML = 'MIDDLE INFOBAR SECTION - BALL PLAYER PROFILE';
				break;
			}
		
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
			option.value = 'U19ODI';
			option.text = 'U19 ODI';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'LIST A';
			option.text = 'LIST A';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'ACCU19';
			option.text = 'ACC U19';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'SA TRI-NATION 2023-24';
			option.text = 'SA TRI-NATION';
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),1);
			cellCount = cellCount + 1
			
			break;
		case 'F11': case 'Control_e'://Lt Ball Profile
			switch(whatToProcess){
			case 'F11':
				header_text.innerHTML = 'BALL PLAYER PROFILE';
				break;
			case 'F11':
				header_text.innerHTML = 'BALL PLAYER PROFILE';
				break;
			}
		
			select = document.createElement('select');
			select.id = 'selectPlayerName';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
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
			option.value = 'U19ODI';
			option.text = 'U19 ODI';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'LIST A';
			option.text = 'LIST A';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'ACCU19';
			option.text = 'ACC U19';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'SA TRI-NATION 2023-24';
			option.text = 'SA TRI-NATION';
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),1);
			cellCount = cellCount + 1
			
			switch(whatToProcess){
			case 'Control_e':
				select = document.createElement('select');
				select.id = 'selectStatType';
				select.name = select.id;
				
				option = document.createElement('option');
				option.value = '0';
				option.text = '';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '1';
				option.text = 'Matches';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '2';
				option.text = 'Wickets';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '3';
				option.text = '3WI';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '4';
				option.text = '5WI';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '5';
				option.text = 'Average';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '6';
				option.text = 'Economy';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = '7';
				option.text = 'Best Fig';
				select.appendChild(option);
				
				select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 2)");
				row.insertCell(cellCount).appendChild(select);
				setDropdownOptionToSelectOptionArray($(select),2);
				cellCount = cellCount + 1
					break;
			}
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

		option = document.createElement('input');
		option.type = 'hidden';
		option.name = 'key_press_hidden_input';
		option.id = option.name;
		option.value = whatToProcess;

	    div.append(option);
	    
	    row.insertCell(cellCount).appendChild(div);
	    cellCount = cellCount + 1;
		
		document.getElementById('select_graphic_options_div').style.display = '';
		break;
	}
}
