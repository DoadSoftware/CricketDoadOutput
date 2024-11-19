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
		if(document.getElementById('which_inning').value == 1){
			document.getElementById('inning1_teamScore_lbl').style.backgroundColor ='#990000';
			document.getElementById('inning2_teamScore_lbl').style.backgroundColor ='';
			document.getElementById('inning1_teamScore_lbl').style.color ='white';
			document.getElementById('inning2_teamScore_lbl').style.color ='';
				
		}else if(document.getElementById('which_inning').value == 2){
			document.getElementById('inning2_teamScore_lbl').style.backgroundColor = '#990000';
			document.getElementById('inning1_teamScore_lbl').style.backgroundColor = '';
			document.getElementById('inning2_teamScore_lbl').style.color = 'white';
			document.getElementById('inning1_teamScore_lbl').style.color = '';
		}
		//addItemsToList('HELP-FILE',session_match);
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
		processCricketProcedures("CANCLE-GRAPHICS");
		$("#select_graphic_options_div").empty();
		document.getElementById('select_graphic_options_div').style.display = 'none';
		$("#captions_div").show();
		break;
	case 'populate_btn': 
		if($(key_press_hidden_input)) {
			processCricketProcedures("POPULATE-GRAPHICS", $('#key_press_hidden_input').val() + ',' + selected_options.toString());
		} else {
			processCricketProcedures("POPULATE-GRAPHICS", $('#which_keypress').val() + ',' + selected_options.toString());
		}
		break;
	case 'change_on':
		processUserSelectionData('IMPACT-CHANGE-ON', 'Shift_I');
		break;
	}	
}

function processUserSelectionData(whatToProcess,dataToProcess)
{
	

	switch (whatToProcess) {
	case 'IMPACT-CHANGE-ON':
		processCricketProcedures('IMPACT-CHANGE-ON');
		break;
	case 'LOGGER_FORM_KEYPRESS':
		document.getElementById('which_keypress').value = dataToProcess;
		//alert('dataToProcess = ' + dataToProcess);
		switch(dataToProcess) {
		case '7':
			processCricketProcedures('HEAD_TO_HEAD_FILE');
			break;
			
		case 'SPEED':
			processCricketProcedures('SHOW_SPEED');
			break;
		case 'Escape':
			$("#select_graphic_options_div").empty();
			document.getElementById('select_graphic_options_div').style.display = 'none';
			$("#captions_div").show();
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
		case '=':
			processCricketProcedures('ANIMATE-OUT-SECOND_PLAYING');
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
			
			if(dataToProcess == 1){
				document.getElementById('inning1_teamScore_lbl').style.backgroundColor ='#990000';
				document.getElementById('inning2_teamScore_lbl').style.backgroundColor ='';
				document.getElementById('inning1_teamScore_lbl').style.color ='white';
				document.getElementById('inning2_teamScore_lbl').style.color ='';
				
			}else if(dataToProcess == 2){
				document.getElementById('inning2_teamScore_lbl').style.backgroundColor = '#990000';
				document.getElementById('inning1_teamScore_lbl').style.backgroundColor = '';
				document.getElementById('inning2_teamScore_lbl').style.color = 'white';
				document.getElementById('inning1_teamScore_lbl').style.color = '';
			}
			
			break;
			
		case '-':
			
			if(confirm('It will Also Delete Your Preview from Directory...\r\n \r\nAre You Sure To Animate Out? ') == true){
				processCricketProcedures('ANIMATE-OUT-GRAPHICS');
			}
			break;
		
		case 'Control_-':
			if(confirm('Animate Out Top Section? ') == true){
				processCricketProcedures('ANIMATE-OUT-TAPE');
			}
			break;
		case 'Alt_u':
			if(confirm('Animate Out Target? ') == true){
				processCricketProcedures('ANIMATE-OUT-TARGET');
			}
			break;
		case 'Control_=':
			if(confirm('Animate Out Tape or Challenge Runs? ') == true){
				processCricketProcedures('ANIMATE-OUT-CR');
			}
			break;
			
		case 'Alt_-':
			
			if(confirm('Animate Out Infobar? ') == true){
				processCricketProcedures('ANIMATE-OUT-INFOBAR');
			}
			break;
		case 'Alt_=':
			
			if(confirm('Animate Out Infobar? ') == true){
				processCricketProcedures('ANIMATE-OUT-IDENT');
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
			case 'F1':
				switch($('#selected_broadcaster').val().toUpperCase()){
				case 'ISPL':
					addItemsToList(dataToProcess,null);
					break;
				case 'ICC-U19-2023': case 'BENGAL-T20': case 'NPL':
					dataToProcess = dataToProcess + ',' + document.getElementById('which_inning').value;
					processCricketProcedures("POPULATE-GRAPHICS", dataToProcess);
					break;
				}
				break;	
			case 'F4': case 'Shift_K':
				switch($('#selected_broadcaster').val().toUpperCase()){
				case 'ISPL': case 'BENGAL-T20': case 'NPL':
					dataToProcess = dataToProcess + ',' + document.getElementById('which_inning').value;
					processCricketProcedures("POPULATE-GRAPHICS", dataToProcess);
					break;
				case 'ICC-U19-2023':
					addItemsToList(dataToProcess,null);
					break;
				}
				break;
			case 'Control_F11':
				switch($('#selected_broadcaster').val().toUpperCase()){
				case 'ISPL': case 'ICC-U19-2023':
					dataToProcess = dataToProcess + ',' + document.getElementById('which_inning').value;
					processCricketProcedures("POPULATE-GRAPHICS", dataToProcess);
					break;
				case 'BENGAL-T20': case 'NPL':
					addItemsToList(dataToProcess,null);
					break;
				}
				break;
			case 'Control_p': case 'Alt_F7':
				switch($('#selected_broadcaster').val().toUpperCase()){
				case 'ISPL': case 'BENGAL-T20':
					dataToProcess = dataToProcess + ',' + document.getElementById('which_inning').value;
					processCricketProcedures("POPULATE-GRAPHICS", dataToProcess);
					break;
				case 'ICC-U19-2023': 
					addItemsToList(dataToProcess,null);
					break;
				}
				break;
			case 'Control_h':
				switch($('#selected_broadcaster').val().toUpperCase()){
				case 'NPL':
					dataToProcess = dataToProcess + ',' + document.getElementById('which_inning').value
					processCricketProcedures("POPULATE-GRAPHICS", dataToProcess);
					break;
				case 'ICC-U19-2023': case 'ISPL': 
					addItemsToList(dataToProcess,null); 
					break;
				}
				break;
			case 'F7':
				switch($('#selected_broadcaster').val().toUpperCase()){
					case 'BENGAL-T20': case 'NPL':
					addItemsToList(dataToProcess,null); 
					break;
				}
			break;		
			case 'F11':
				switch($('#selected_broadcaster').val().toUpperCase()){
					case 'BENGAL-T20': case 'NPL':
					addItemsToList(dataToProcess,null); 
					break;
				}
			break;	
			case 'Shift_C':
			case 'Control_4': case 'F12': case 'Alt_1': case 'Alt_2': case 'Alt_7':  case 'Alt_5': //case 'Alt_6': case 'Alt_8': case 'Alt_3': case 'Alt_4': case 'F7': case 'F11':
			case 'Control_F5': case 'Shift_T': case 'Control_F9': case 'F5': case 'F6': case 'Alt_w':  case 'Control_j': case 'Alt_F8':
			case 'F8': case 'F9':  case 'u': case 'q': case 'Shift_F5': case 'Shift_F9': case 'Shift_F6': case 'Control_y': case 'Shift_F8':
			case 'Shift_O': case 'g': case 'y': case 'Control_g': case 'Control_s': case 'Control_f': //case 'Alt_F9': case 'Control_h':
			case 'Alt_F12': case 'l': case 'p': case 'Alt_m': case 'Alt_n': case 'Control_b': case 'Alt_F10': case 'Alt_d':
			case 'Control_p': case 'Shift_F4': case 'Alt_F1': case 'Alt_F2': case 'Shift_E': case 'Shift_P': case 'Shift_Q': case 'Alt_z': case 'Shift_F':
			case 'Alt_F6': case 'Shift_R': case 'Shift_A': case 'Alt_c': case 'Control_F12': case 'Shift_F12': case 'Shift_F7': case 'Control_Shift_F9':
			case 'Shift_Z': case 'Shift_X': case 'Control_i': case 'Control_Shift_E': case 'Control_Shift_F': case 'Control_Shift_P': case 'Control_Shift_F1': case 'Control_Shift_D':
			case 'Alt_Shift_Z': case 'Control_Shift_F7': case 'Shift_I': case 'Alt_Shift_C': case 'Control_Shift_F2': case 'Control_Shift_M': case 'Control_Shift_F4':
			case 'Control_Shift_U': case 'Control_Shift_V':
				addItemsToList(dataToProcess,null); 
				break;
			//changed shift_f11 to control_f11
			case 'Shift_F10': case 'm': case 'F2': case 'Control_F1': case 'Control_a': case "Control_Shift_F10":
			case 'Alt_o':  case 'Shift_F3': case 'd': case 'e': case 'Control_F6': case 'Control_F7': 
			case 'Control_k': case 'Control_F10': case 'Control_F3':  case 'a': case 't': case 'h': case 'n':
			case 'Shift_F1': case 'Shift_F2': case 'Shift_D': case 'Control_q': case 'Control_b': case 'o': case 'Control_F2': case 'b':
			case 'Alt_F11': case 'r': case 'Shift_U': case 'Alt_j': case 'Alt_h': case 'Alt_Shift_L': //case 'Shift_F':
			case 'Control_F8': case 'Alt_y': case '.': case '/': case 'Shift_V': case 'Alt_i': case 'b': case 'Shift_B': case 'Control_Shift_B':
			case '6': case 'Alt_Shift_F3': case 'Control_Shift_R': case 'Control_Shift_J':
				/*switch(dataToProcess){
				case 'Shift_F':
					count++;
					dataToProcess = dataToProcess + ',' + document.getElementById('which_inning').value + ',' + count;
				break;
				default:
					dataToProcess = dataToProcess + ',' + document.getElementById('which_inning').value;
				}*/
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
				switch($('#selected_broadcaster').val().toUpperCase()){
				case 'ISPL': case 'NPL':
					dataToProcess = dataToProcess + ',' + document.getElementById('which_inning').value;
					processCricketProcedures("ANIMATE-IN-GRAPHICS", dataToProcess);
					break;
				case 'ICC-U19-2023':
					processCricketProcedures("QUIDICH-COMMANDS", dataToProcess);
					break;
				}
				
				break;
			case 'Alt_f': case 'Alt_g': case 'ArrowDown': case 'ArrowUp': case 'w': case 'i': case 'f': case 's': case '0': case ';': 
			case 'Control_2': case 'Control_3': case 'ArrowLeft': case 'ArrowRight':
				dataToProcess = dataToProcess + ',' + document.getElementById('which_inning').value;
				processCricketProcedures("ANIMATE-IN-GRAPHICS", dataToProcess);
				break;
			case 'Control_1':
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
			case 'HEAD_TO_HEAD_FILE':
				alert(data.match.matchFileName + ' H2H FILE IS CREATED');
				break;
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
							$('.my_waiting_modal').modal('hide');
							//setTimeout(function(){$('.my_waiting_modal').modal('hide');},3000);
							processCricketProcedures(whatToProcess.replace('POPULATE-', 'ANIMATE-IN-'),dataToProcess);
							if(dataToProcess.split(',')[0] == 'Shift_F') {
									if($('#selectWicketSequence option:last').val()){
										
									}
									$('#selectWicketSequence option:selected').next().prop('selected', true);
									document.getElementById('selectWicketSequence').setAttribute('onchange','setDropdownOptionForWicketSequence(0)');
									setDropdownOptionForWicketSequence(0);
								}
						}else {
							processUserSelection($('#cancel_graphics_btn').attr('value','cancel_graphics_btn'));
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
function removeSelectDuplicates(select_id)
{
	var this_list = {};
	$("select[id='" + select_id + "'] > option").each(function () {
	    if(this_list[this.text]) {
	        $(this).remove();
	    } else {
	        this_list[this.text] = this.value;
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
function setDropdownOptionForWicketSequence(whichIndex)
{
	selected_options[0] = document.getElementById('which_inning').value;
	selected_options[whichIndex+1] = $('#selectWicketSequence option:selected').val();
}
function getStrikeRate(totalRunsScored, totalBallsFaced, numberOfDecimals, defaultValue) {
    if (totalBallsFaced <= 0) {
        return defaultValue;
    } else {
        if (numberOfDecimals > 0) {
            return ((totalRunsScored*100) / totalBallsFaced).toFixed(numberOfDecimals);
        } else {
            return defaultValue;
        }
    }
}
function getEconomy(totalRunsConceded, totalBallsBowled, numberOfDecimals, defaultValue) {
    if (totalBallsBowled <= 0) {
        return defaultValue;
    } else {	
        if (numberOfDecimals > 0) {
            return ((totalRunsConceded / totalBallsBowled) * 6).toFixed(numberOfDecimals);
        } else {
            return defaultValue;
		}
 	}
 }
function addItemsToList(whatToProcess,dataToProcess)
{
	var select,option,header_text,div,table,table_data,tbody,row;
	var cellCount = 0,row_count=0;
	
	switch(whatToProcess) {
	case 'HELP-FILE':
		$("#captions_div").hide();
		$('#select_graphic_options_div').empty();
		
		let sections = [
		    { title: 'INFOBAR', captions: [
		        ['FOUR DIRECTOR', 'F'],
		        ['FREE HIT DIRECTOR', 'I'],
		        ['SIX DIRECTOR', 'S'],
		        ['POINTS TABLE', 'CTRL+S'],
		        ['WICKET DIRECTOR', 'W'],
		        ['NINE DIRECTOR', '0'],
		        ['INFOBAR IN', 'F12'],
		        ['INFOBAR_SPONSOR', ']'],
		        ['HAT-TRICK DIRECTOR', ';'],
		        ['TICKER PUSH OUT', 'PageDown/ARROW DOWN'],
		        ['TICKER PUSH IN', 'PageUp/ARROW UP'],
		        ['CHALLENGE RUNS', 'ALT+C'],
		        ['POWER PLAY IN/OUT', 'ALT+E'],
		        ['TICKER SHRINK', 'ALT+F'],
		        ['COMMENTATOR', 'ALT+0'],
		        ['BOTTOM LEFT SECTION', 'ALT+1'],
		        ['MIDDLE SECTION', 'ALT+2'],
		        ['BAT PROFILE', 'ALT+3'],
		        ['BOWL PROFILE', 'ALT+4'],
		        ['LAST X BALLS', 'ALT+5'],
		        ['BAT + SPONSOR', 'ALT+6'],
		        ['BOTTOM RIGHT SECTION', 'ALT+7'],		        
		        ['RIGHT SECTION, RIGHT TOP, TICKER TIMELINE', 'ALT+8'],
		        ['FREE TEXT INFO DB', 'ALT+9'],
		        ['INFOBAR IDENT DATA', 'SHIFT+F12'],
		        ['INFOBAR IDENT', 'CTRL+F12'],
		        ['AnimateOut Infobar-Right', 'CTRL+0'],
		        ['BONUS', 'CTRL+1'],
		        ['POWERPLAY IN', 'CTRL+2'],
		        ['POWERPLAY OUT', 'CTRL+3'],
		        ['FREE TEXT USING INPUT BOX', 'CTRL+4'],
		        ['RIGHT TOP INFOBAR OUT', 'CTRL+-'],
		        ['CHALLENGE RUNS OUT', 'CTRL+='],
		        ['INFOBAR OUT', '-'],
		        ['INFOBAR IDENT OUT', '='],
		        
		    ]},
		    { title: 'FULL FRAME', captions: [
		        ['MOST FOURS', 'F'],
		        ['POTT ', 'R'],
		        ['MOST SIXES', 'V'],
		        ['MOST WICKETS', 'X'],
		        ['MOST RUNS', 'Z'],
		        ['BATTING CARD', 'F1'],
		        ['BOWLING CARD', 'F2'],
		        ['ALL PARTNERSHIP', 'F4'],
		        ['BATTING BY POSITION ', 'ALT+G'],
		        ['BATTING BY YEARS ', 'ALT+H'],
		        ["TODAY'S MATCH", 'ALT+J'],
		        ['BATTING BARS ', 'ALT+K'],
		        ['BATTING BY VENUE IN A COUNTRY ', 'ALT+L'],
		        ['BAT MILESTONE', 'ALT+M'],
		        ['BOWL MILESTONE', 'ALT+N'],
		        ['BATSMAN v ALL BOWLERS', 'ALT+T'],
		        ['BOWLER v ALL BATTERS', 'ALT+U'],
		        ['BATTING BY COUNTRY ', 'ALT+V'],
		        ['SQUAD ', 'ALT+Z'],
		        ['FOW ', 'ALT+F3'],
		        ["RICHIE'S CAPTION", 'ALT+F5'],
		        ['SINGLE TEAM (CAREER)', 'ALT+F9'],
		        ['SINGLE TEAM (THIS SERIES)', 'ALT+F10'],
		        ['DOUBLE MANHATTAN', 'ALT+F11'],
		        ['TARGET', 'SHIFT+D'],
		        ['ECONOMY BARS ', 'SHIFT+G'],
		        ['BOWLING BARS ', 'SHIFT+H'],
		        ['IMPACT PLAYER ', 'SHIFT+I'],
		        ['NEXT TO BAT ', 'SHIFT+J'],
		        ['CURR PARTNERHIP ', 'SHIFT+K'],
		        ['BOWLING BY YEARS ', 'M'],
		        ['WICKETS TAKEN IN AN INNINGS ', 'SHIFT+N'],
		        ['BAT PROFILE (THIS SERIES)', 'SHIFT+P'],
		        ['BOWL PROFILE (THIS SERIES)', 'SHIFT+Q'],
		        ['TEAMS WITH PHOTOS', 'SHIFT+T'],
		        ['BOWLING BY COUNTRY', 'SHIFT+V'],
		        ['Bowler Best Figures FF(THIS SERIES)', 'SHIFT+X'],
		        ['Bowler Best Figures FF(THIS SERIES)', 'SHIFT+Z'],
		        ['Bowler Best Figures FF(THIS SERIES)', 'SHIFT+D'],
		        ['PREV MATCH SUMMARY', 'SHIFT+F10'],
		        ['PREV MATCH SUMMARY', 'SHIFT+F11'],
		        ['TAPE BALL LEADERBOARD', 'CTRL+C'],
		        ['BAT PROFILE (DB)', 'CTRL+D'],
		        ['BOWL PROFILE (DB)', 'CTRL+E'],
		        ['BOWLING BY VENUE IN A COUNTRY ', 'CTRL+L'],
		        ['MATCH PROMO', 'CTRL+M'],
		        ['POINTS TABLE (6 TEAMS)', 'CTRL+P'],
		        ['BEST BOWLING FIG', 'CTRL+X'],
		        ['HIGHEST SCORE', 'CTRL+Z'],
		        ['PHOTO SCORECARD', 'CTRL+F1'],
		        ['DOUBLE TEAMS', 'CTRL+F7'],
		        ['MANHATTAN', 'CTRL+F10'],
		        ['MATCH SUMMARY', 'CTRL+F11'],
		        ['BATSMAN CAREER MANHATTAN ', 'ALT + SHIFT+M'],
		        ['HALF FRAME WAGON WHEEL', 'ALT + SHIFT+W'],
		        ['FULL FRAME WAGON WHEEL', 'ALT + SHIFT+Y'],
		    ]},
		    { title: 'LOWER THIRDS', captions: [
		        ['LT ALL POWERPLAY SUMM', 'A'],
		        ['MATCH STATISTICS ', 'B'],
		        ['LT TARGET', 'D'],
		        ['EQUATION', 'E'],
		        ['MOST RUNS', 'Z'],
		        ['BATTING CARD', 'F1'],
		        ['BOWLING CARD', 'F2'],
		        ['ALL PARTNERSHIP', 'F4'],
		        ['BATTING BY POSITION ', 'ALT+G'],
		        ['BATTING BY YEARS ', 'ALT+H'],
		        ["TODAY'S MATCH", 'ALT+J'],
		        ['BATTING BARS ', 'ALT+K'],
		        ['BATTING BY VENUE IN A COUNTRY ', 'ALT+L'],
		        ['BAT MILESTONE', 'ALT+M'],
		        ['BOWL MILESTONE', 'ALT+N'],
		        ['BATSMAN v ALL BOWLERS', 'ALT+T'],
		        ['BOWLER v ALL BATTERS', 'ALT+U'],
		        ['BATTING BY COUNTRY ', 'ALT+V'],
		        ['SQUAD ', 'ALT+Z'],
		        ['FOW ', 'ALT+F3'],
		        ["RICHIE'S CAPTION", 'ALT+F5'],
		        ['SINGLE TEAM (CAREER)', 'ALT+F9'],
		        ['SINGLE TEAM (THIS SERIES)', 'ALT+F10'],
		        ['DOUBLE MANHATTAN', 'ALT+F11'],
		        ['TARGET', 'SHIFT+D'],
		        ['ECONOMY BARS ', 'SHIFT+G'],
		        ['BOWLING BARS ', 'SHIFT+H'],
		        ['IMPACT PLAYER ', 'SHIFT+I'],
		        ['NEXT TO BAT ', 'SHIFT+J'],
		        ['CURR PARTNERHIP ', 'SHIFT+K'],
		        ['BOWLING BY YEARS ', 'M'],
		        ['WICKETS TAKEN IN AN INNINGS ', 'SHIFT+N'],
		        ['BAT PROFILE (THIS SERIES)', 'SHIFT+P'],
		        ['BOWL PROFILE (THIS SERIES)', 'SHIFT+Q'],
		        ['TEAMS WITH PHOTOS', 'SHIFT+T'],
		        ['BOWLING BY COUNTRY', 'SHIFT+V'],
		        ['Bowler Best Figures FF(THIS SERIES)', 'SHIFT+X'],
		        ['Bowler Best Figures FF(THIS SERIES)', 'SHIFT+Z'],
		        ['Bowler Best Figures FF(THIS SERIES)', 'SHIFT+D'],
		        ['PREV MATCH SUMMARY', 'SHIFT+F10'],
		        ['PREV MATCH SUMMARY', 'SHIFT+F11'],
		        ['TAPE BALL LEADERBOARD', 'CTRL+C'],
		        ['BAT PROFILE (DB)', 'CTRL+D'],
		        ['BOWL PROFILE (DB)', 'CTRL+E'],
		        ['BOWLING BY VENUE IN A COUNTRY ', 'CTRL+L'],
		        ['MATCH PROMO', 'CTRL+M'],
		        ['POINTS TABLE (6 TEAMS)', 'CTRL+P'],
		        ['BEST BOWLING FIG', 'CTRL+X'],
		        ['HIGHEST SCORE', 'CTRL+Z'],
		        ['PHOTO SCORECARD', 'CTRL+F1'],
		        ['DOUBLE TEAMS', 'CTRL+F7'],
		        ['MANHATTAN', 'CTRL+F10'],
		        ['MATCH SUMMARY', 'CTRL+F11'],
		        ['BATSMAN CAREER MANHATTAN ', 'ALT + SHIFT+M'],
		        ['HALF FRAME WAGON WHEEL', 'ALT + SHIFT+W'],
		        ['FULL FRAME WAGON WHEEL', 'ALT + SHIFT+Y'],
		    ]},
		    { title: "MINI'S AND BUGS", captions: [
		        ['BOWL FIG', 'G'],
		        ['HIGHLIGHTS', 'H'],
		        ['PLAYER OF THE MATCH', 'O'],
		        ['THIRD UMPIRE', 'T'],
		        ['BAT SCORE', 'Y'],
		        ['TOURNAMENT SIX COUNTER', '6'],
		        ['BUG 50-50', '.'],
		        ['WICKET SEQUENCE BOWLER', 'ALT+B'],
		        ['SIX DISTANCE', 'SHIFT+C'],
		        ['WICKET SEQUENCE', 'SHIFT+F'],
		        ['BAT DISMISSAL', 'SHIFT+O'],
		        ['BUG MULTI PARTNERSHIP', 'SHIFT+F4'],
		        ['MINI BATTING CARD', 'SHIFT+F1'],
		        ['MINI BOWLING CARD', 'SHIFT+F2'],
		        ['CURR PARTNERSHIP (BUGS)', 'CTRL+K'],
		        ['POWERPLAY', 'CTRL+Y'],
		    ]},
		    { title: "POP UP", captions: [
		        ['BAT POPUP', 'CTRL + SHIFT+U'],
		        ['BOWL POPUP', 'CTRL + SHIFT+V'],
		    ]}
		];
		
    		sections.forEach(section => {
		    table = document.createElement('table');
		    table.setAttribute('class', 'table table-bordered');
		
		    tbody = document.createElement('tbody');
		    table.appendChild(tbody);
		
		    headerRow = document.createElement('tr');
		    headerText = document.createElement('th');
		    headerText.setAttribute('class', 'thead-dark');
		    headerText.setAttribute('colspan', '2');
		    headerText.setAttribute('style', 'color: red;');
		    headerText.innerHTML = `<b>${section.title}</b>`;
		    headerRow.appendChild(headerText);
		    tbody.appendChild(headerRow);
		
        for (let i = 0; i < Math.ceil(section.captions.length / 3); i++) {
		        let row = document.createElement('tr');
		
		        for (let j = 0; j < 3; j++) {
		            let index = i * 3 + j;
		            if (index < section.captions.length) {
		                cell1 = document.createElement('td');
		                cell2 = document.createElement('td');
		                cell1.innerHTML = `<b>${section.captions[index][0]}</b>`;
		                if (section.captions[index][1].includes('/')) {
        					cell2.innerHTML = `<b>${section.captions[index][1].replace('/', '/<br>')}</b>`;
					    } else {
					        cell2.innerHTML = `<b>${section.captions[index][1]}</b>`;
					    }

		                cell2.setAttribute('style', 'color: blue;');
		                row.appendChild(cell1);
		                row.appendChild(cell2);
		            } else {
		                cell1 = document.createElement('td');
		                cell2 = document.createElement('td');
		                row.appendChild(cell1);
		                row.appendChild(cell2);
		            }
		        }
		
		        tbody.appendChild(row);
		    }
		
		    $('#select_graphic_options_div').append(table);
		    $('#select_graphic_options_div').append("<br>");
		});
		
		$("#select_graphic_options_div").show();
	break;
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
	/*case '8':
		$("#captions_div").hide();
		$('#select_graphic_options_div').empty();
		
		header_text = document.createElement('h6');
		header_text.innerHTML = 'Select Graphic Options';
		document.getElementById('select_graphic_options_div').appendChild(header_text);
		
		table_data = document.createElement('table');
		table_data.setAttribute('class', 'table table-bordered');
		tbody = document.createElement('tbody');
		table_data.appendChild(tbody);
		
		dataToProcess.forEach(function(playerstats,index,arr1){
			row = tbody.insertRow(tbody.rows.length);
			for(var j = 1; j <= 2; j++){
				text = document.createElement('text');
				switch(j){
					case 1:
						alert(playerstats.playerId);
						text.innerHTML = playerstats.playerId ;
						break;
				}
				row.insertCell(j-1).appendChild(text);
			}
			//alert(playerstats.playerId);
		});
		
		document.getElementById('select_graphic_options_div').appendChild(table_data);
		break;*/
		
	case 'Shift_C':
	case 'Control_m': case 'F4': case 'F5': case 'F6': case 'Alt_w': case 'Control_j': case 'F8': case 'F9': case 'F10': case 'F7': case 'F11':
	case 'Control_F5': case 'Control_F9': case 'Shift_T': case 'u': case 'p': case 'Control_p': case 'Control_d': case 'Control_e': case 'Shift_F8':
	case 'z': case 'x': case 'c': case 'v': case 'Shift_F11': case 'Control_y': case 'Alt_F8': case 'Alt_F1': case 'Alt_F2':
	case 'Shift_K': case 'Shift_O': case 'k': case 'g': case 'y': case 'Shift_F5': case 'Shift_F9': case 'Control_h': case 'Control_g': case 'q':
	case 'j': case 'Shift_F6': case 'Control_s':  case 'Control_f': case 'Alt_F12': case 'l': case 'Shift_E': //case 'Alt_F9':
	case 'F12': case 'Alt_1': case 'Alt_2': case 'Alt_3': case 'Alt_4': case 'Alt_5': case 'Alt_6': case 'Alt_7': case 'Alt_8': case 'Alt_9': case 'Alt_0':
	case 'Alt_m': case 'Alt_n': case 'Control_b': case 'Alt_p': case 'Alt_F10': case 'Alt_d': case 'Shift_F4': case 'Alt_a': case 'Alt_s': 
	case 'Shift_P': case 'Shift_Q': case 'Alt_z': case 'Control_c': case 'Control_v': case 'Control_z': case 'Control_x': case 'Alt_q': case 'Shift_F': 
	case 'Alt_F6': case 'Shift_A': case 'Shift_R': case 'Control_Shift_F1': case 'Control_Shift_D': case 'Alt_Shift_Z': case 'Control_Shift_F7': case 'Control_Shift_F2':
	case 'Alt_c': case 'Control_F12': case 'Shift_F12': case 'F1': case 'Shift_F7': case 'Control_Shift_F9': case 'Alt_Shift_C': case 'Control_Shift_L':
	case 'Shift_Z': case 'Shift_X': case 'Control_i': case 'Control_Shift_E': case 'Control_Shift_F': case 'Control_Shift_P': case 'Shift_I': case 'Control_F11': case 'Control_Shift_M':
	case 'Alt_Shift_R': case 'Control_Shift_U': case 'Control_Shift_V': case 'Control_4': case 'Shift_~': case 'Shift_!': case 'Control_Shift_F4':
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
		case 'Shift_!':
			header_text.innerHTML = 'PLAYER CAREER STATS';
			
			thead = document.createElement('thead');
			//tbody = document.createElement('tbody');
			tr = document.createElement('tr');
			for (var j = 0; j <= 4; j++) {
			    th = document.createElement('th'); // Column
				th.scope = 'col';
			    switch (j) {
				case 0:
				    th.innerHTML = 'TEAM/PLAYER';
					break;
				case 1:
					th.innerHTML = 'BAT T20I STATS (M|R|50/100|SR)';
					break;
				case 2:
					th.innerHTML = 'BALL T20I STATS (M|W|R|Econ.)';
					break;
				case 3:
					th.innerHTML = 'BAT ODI STATS (M|R|50/100|SR)';
					break;
				case 4:
					th.innerHTML = 'BALL ODI STATS (M|W|R|Econ.)';
					break;
				}	
				tr.appendChild(th);
			}
			thead.appendChild(tr);
			table.appendChild(thead);
			alert(dataToProcess.length);
			for(var i = 0; i <= dataToProcess.length - 1; i++){
				row = tbody.insertRow(tbody.rows.length);
				for(var j = 0; j <= 4; j++){
					text = document.createElement('text');
					switch(j){
					case 0:
						text.innerHTML = dataToProcess[i].playerId + ' - ' + dataToProcess[i].player.full_name + ' (' + dataToProcess[i].team.teamName4 + ')';
						break;
					case 1: case 2: case 3: case 4:
						text.innerHTML = 'NO DATA IN DB' ;
						break;
					}
					row.insertCell(j).appendChild(text);
				}
			}
			break;
		case 'Shift_~':
			header_text.innerHTML = 'PLAYER STATS';
			
			thead = document.createElement('thead');
			//tbody = document.createElement('tbody');
			tr = document.createElement('tr');
			for (var j = 0; j <= 4; j++) {
			    th = document.createElement('th'); // Column
				th.scope = 'col';
			    switch (j) {
				case 0:
				    th.innerHTML = 'TEAM/PLAYER';
					break;
				case 1:
					th.innerHTML = 'BAT CAREER STATS (M|R|50/100|SR)';
					break;
				case 2:
					th.innerHTML = 'BALL CAREER STATS (M|W|R|Econ.)';
					break;
				case 3:
					th.innerHTML = 'BAT THIS SERIES (M|R|50/100|SR)';
					break;
				case 4:
					th.innerHTML = 'BALL THIS SERIES (M|W|R|Econ.)';
					break;
				}	
				tr.appendChild(th);
			}
			thead.appendChild(tr);
			table.appendChild(thead);
			
			for(var i = 0; i <= dataToProcess.length - 1; i++){
				row = tbody.insertRow(tbody.rows.length);
				for(var j = 0; j <= 4; j++){
					text = document.createElement('text');
					switch(j){
					case 0:
						text.innerHTML = dataToProcess[i].playerId + ' - ' + dataToProcess[i].player.full_name + ' (' + dataToProcess[i].team.teamName4 + ')';
						break;
					case 1:
						if(dataToProcess[i].stats != null) {
							text.innerHTML = dataToProcess[i].stats.matches + ' | ' + dataToProcess[i].stats.runs + ' | ' + dataToProcess[i].stats.fifties + '/' +
								dataToProcess[i].stats.hundreds + ' | ' + getStrikeRate(dataToProcess[i].stats.runs,dataToProcess[i].stats.balls_faced,1,'-') ;
						}else {
							text.innerHTML = 'NO DATA IN DB' ;
						}
						break;
					case 2:
						if(dataToProcess[i].stats != null) {
							text.innerHTML = dataToProcess[i].stats.matches + ' | ' + dataToProcess[i].stats.wickets + ' | ' + dataToProcess[i].stats.runs_conceded + 
								' | ' + getEconomy(dataToProcess[i].stats.runs_conceded,dataToProcess[i].stats.balls_bowled,2,'-');
						}else {
							text.innerHTML = 'NO DATA IN DB' ;
						}
						break;
					case 3:
						if(dataToProcess[i].tournament != null) {
							text.innerHTML = dataToProcess[i].tournament.matches + ' | ' + dataToProcess[i].tournament.runs + ' | ' + dataToProcess[i].tournament.fifty + '/' 
								+ dataToProcess[i].tournament.hundreds + ' | ' + getStrikeRate(dataToProcess[i].tournament.runs,dataToProcess[i].tournament.ballsFaced,1,'-');
						}else {
							text.innerHTML = 'NO DATA' ;
						}
						break;
					case 4:
						if(dataToProcess[i].tournament != null) {
							text.innerHTML = dataToProcess[i].tournament.matches + ' | ' + dataToProcess[i].tournament.wickets + ' | ' + dataToProcess[i].tournament.runsConceded + 
								' | ' + getEconomy(dataToProcess[i].tournament.runsConceded,dataToProcess[i].tournament.ballsBowled,2,'-');
						}else {
							text.innerHTML = 'NO DATA' ;
						}
						break;
					}
					row.insertCell(j).appendChild(text);
				}
			}
			break;
		case 'Alt_Shift_R':
			header_text.innerHTML = 'TEAM FIXTURES/RESULTS';
			
			select = document.createElement('select');
			select.id = 'selectTeams';
			select.name = select.id;

			dataToProcess.forEach(function(teams,index,arr1){
				option = document.createElement('option');
				option.value = teams.teamId;
				option.text = teams.teamName1;
				select.appendChild(option);
			});
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			break;
		case 'Control_Shift_M':
			header_text.innerHTML = 'LT MATCH IDENT';
			
			select = document.createElement('select');
			select.id = 'selectMatchData';
			select.name = select.id;

			option = document.createElement('option');
			option.value = 'Venue';
			option.text = 'Venue' ;
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'Target';
			option.text = 'Target' ;
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'Result';
			option.text = 'Result' ;
			select.appendChild(option);
			
			row.insertCell(cellCount).appendChild(select);
			cellCount = cellCount + 1;
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			break;
		case 'Alt_Shift_Z':
			header_text.innerHTML = 'TEAMS LOGOS/CAPTAINS';
			
			select = document.createElement('select');
			select.id = 'selectData';
			select.name = select.id;

			option = document.createElement('option');
			option.value = 'logo';
			option.text = 'Logo' ;
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'captain';
			option.text = 'Captain' ;
			select.appendChild(option);
			
			row.insertCell(cellCount).appendChild(select);
			cellCount = cellCount + 1;
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			break;
		case 'Shift_I':
			header_text.innerHTML = 'IMPACT PLAYER';
				select = document.createElement('select');
				select.id = 'selectOutPlayer';
				select.name = select.id;
				
				option = document.createElement('option');
				option.value = 'out';
				option.text = '-- SELECT OUT PLAYER --';
				select.appendChild(option);
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
				select.id = 'selectInPlayer';
				select.name = select.id;
				
				option = document.createElement('option');
				option.value = 'in';
				option.text = '-- SELECT IN PLAYER --';
				select.appendChild(option);
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
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),1);
			cellCount = cellCount + 1;
			break;
		case 'Control_Shift_D':
			header_text.innerHTML = 'DOUBLE MATCH IDENT/PROMO';
			
			select = document.createElement('select');
			select.id = 'selectTieID';
			select.name = select.id;

			option = document.createElement('option');
			option.value = 'today';
			option.text = 'Today' ;
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'tomorrow';
			option.text = 'Tomorrow' ;
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'day_after_tomorrow';
			option.text = 'Day After Tomorrow' ;
			select.appendChild(option);
			
			row.insertCell(cellCount).appendChild(select);
			cellCount = cellCount + 1;
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			break;
		case 'Control_Shift_F4':
			select = document.createElement('select');
			select.id = 'selectPlayer';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
					inn.partnerships.sort(function(a, b) {
				      if (b.totalRuns === a.totalRuns) {
						 return a.totalBalls - b.totalBalls; 
				      }
				      return b.totalRuns - a.totalRuns;
				    });
					
					inn.partnerships.forEach(function(p,p_index,p_arr){
						option = document.createElement('option');
						option.value = p.partnershipNumber;
						option.text = p.partnershipNumber+" "+p.firstPlayer.ticker_name+"-"+p.secondPlayer.ticker_name;	
						select.appendChild(option);
					});
				}
			});
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			break;
		case 'Control_Shift_F2': case 'Control_Shift_V':
			

			select = document.createElement('select');
			select.id = 'selectPlayer';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
					inn.bowlingCard.sort(function(a, b) {
				      if (b.wickets === a.wickets) {
						if(a.economyRate === b.economyRate){
							return b.dots - a.dots;
					  	}
				        return a.economyRate - b.economyRate;
				      }
				      return b.wickets - a.wickets;
				    });
					
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
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			
			select = document.createElement('select');
			select.id = 'selectStatsType';
			select.name = select.id;
			switch(whatToProcess){
				case 'Control_Shift_V':
				header_text.innerHTML = 'BALL POP UP';
				option = document.createElement('option');
				option.value = 'figure';
				option.text = 'Bowler Figure';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'economy';
				option.text = 'Economy';
				select.appendChild(option);
				break;
				case 'Control_Shift_F2':
				header_text.innerHTML = 'BALL PERFORMER';
				option = document.createElement('option');
				option.value = 'performer';
				option.text = 'Performer';
				select.appendChild(option);
				break;
			}
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),1);
			cellCount = cellCount + 1;
			break;
		case 'Control_Shift_F1': case 'Control_Shift_U':
			select = document.createElement('select');
			select.id = 'selectPlayer';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
					inn.battingCard.sort(function(a, b) {
				      if (b.runs === a.runs) {
						if(a.balls === b.balls){
							return b.fours - a.fours
					  	}
				        // If totalRuns are equal, sort by totalBalls (ascending)
				        return a.balls - b.balls;
				      }
				      // Otherwise, sort by totalRuns (descending)
				      return b.runs - a.runs;
				    });
					
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
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			
			select = document.createElement('select');
			select.id = 'selectStatsType';
			select.name = select.id;
			switch(whatToProcess){
				case 'Control_Shift_U':
				header_text.innerHTML = 'BAT POP UP';
				option = document.createElement('option');
				option.value = 'score';
				option.text = 'Score';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'strikeRate';
				option.text = 'Strike Rate';
				select.appendChild(option);
				break;
				case 'Control_Shift_F1':
				header_text.innerHTML = 'BAT PERFORMER/PARTNERSHIP';
				option = document.createElement('option');
				option.value = 'performer';
				option.text = 'Performer';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'partnership';
				option.text = 'Partnership';
				select.appendChild(option);
				break;
			}
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),1);
			cellCount = cellCount + 1;
			break;
		case 'Control_Shift_E':
			header_text.innerHTML = 'BOWLER VS ALL BATSMAN';
			select = document.createElement('select');
			select.id = 'selectPlayer';
			select.name = select.id;
				select = document.createElement('select');
				select.id = 'selectBowler';
				select.name = select.id;
				
				session_match.match.inning.forEach(function(inn,index,arr){
					if(inn.inningNumber == document.getElementById('which_inning').value){
						inn.bowlingCard.forEach(function(boc,index,arr){
							if(boc.status == 'CURRENTBOWLER'){
								option = document.createElement('option');
								option.value = boc.player.playerId;
								option.text = boc.player.full_name;
								select.appendChild(option);
							}
						});
						
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
				removeSelectDuplicates(select.id);
				cellCount = cellCount + 1;
		break;
		case 'Control_Shift_F':
			header_text.innerHTML = 'BATSMAN VS ALL BOWLERS';
			select = document.createElement('select');
			select.id = 'selectPlayer';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
					inn.battingCard.forEach(function(bc,index,arr){
						if(bc.status == 'NOT OUT'){
							if(bc.onStrike == 'YES'){
								option = document.createElement('option');
								option.value = bc.playerId;
								option.text = bc.player.full_name + " - " + bc.status;
								select.appendChild(option);
							}else{
								option = document.createElement('option');
								option.value = bc.playerId;
								option.text = bc.player.full_name + " - " + bc.status;
								select.appendChild(option);
							}
						}
					});
					
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
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			
			switch($('#selected_broadcaster').val().toUpperCase()){
				case 'BENGAL-T20':
				break;
				default:
				select = document.createElement('select');
				select.id = 'selectBowler';
				select.name = select.id;
				
				session_match.match.inning.forEach(function(inn,index,arr){
					if(inn.inningNumber == document.getElementById('which_inning').value){
						inn.bowlingCard.forEach(function(boc,index,arr){
							if(boc.status == 'CURRENTBOWLER'){
								option = document.createElement('option');
								option.value = boc.player.playerId;
								option.text = boc.player.full_name;
								select.appendChild(option);
							}
						});
						
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
				setDropdownOptionToSelectOptionArray($(select),1);
				removeSelectDuplicates(select.id);
				cellCount = cellCount + 1;
			}
		break;
		case 'Control_i':
			header_text.innerHTML = 'BATSMAN SCORE SPLIT';
			select = document.createElement('select');
			select.id = 'selectPlayer';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
					inn.battingCard.forEach(function(bc,index,arr){
						if(bc.status == 'NOT OUT'){
							if(bc.onStrike == 'YES'){
								option = document.createElement('option');
								option.value = bc.playerId;
								option.text = bc.player.full_name + " - " + bc.status;
								select.appendChild(option);
							}else{
								option = document.createElement('option');
								option.value = bc.playerId;
								option.text = bc.player.full_name + " - " + bc.status;
								select.appendChild(option);
							}
						}
					});
					
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
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
		break;	
		case 'Control_F11':
			header_text.innerHTML = 'SUMMARY';
			select = document.createElement('select');
			select.id = 'selectSummary';
			select.name = select.id;
			
			option = document.createElement('option');
            option.value = 'captain';
            option.text = 'Captain';
            select.appendChild(option);
            
			option = document.createElement('option');
            option.value = 'logo';
            option.text = 'logo';
            select.appendChild(option);
            
            switch($('#selected_broadcaster').val().toUpperCase()){
				case 'BENGAL-T20':
				option = document.createElement('option');
	            option.value = 'trophy';
	            option.text = 'trophy';
	            select.appendChild(option);
				break;
			}
            
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			break;
		case 'F1':
			header_text.innerHTML = 'SCORECARD';
			select = document.createElement('select');
			select.id = 'selectScoreCard';
			select.name = select.id;
			
			option = document.createElement('option');
            option.value = 'SPLIT';
            option.text = 'SPLIT';
            select.appendChild(option);
            
			option = document.createElement('option');
            option.value = 'NORMAL';
            option.text = 'NORMAL';
            select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			break;	
		case 'Alt_c':
			header_text.innerHTML = 'CHALLENGED RUNS';
		
			select = document.createElement('input');
			select.type = "text";
			select.id = 'selectFreeText';
			select.value = '16';
			
			select.setAttribute('onchange',"setTextBoxOptionToSelectOptionArray(0)");
			row.insertCell(cellCount).appendChild(select);
			setTextBoxOptionToSelectOptionArray(0);
			cellCount = cellCount + 1;
		break;
		case 'Shift_F':
			header_text.innerHTML = 'WICKET SEQUENCE';
			select = document.createElement('select');
			select.id = 'selectWicketSequence';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
					inn.fallsOfWickets.forEach(function(fow,fow_index,fow_arr){
						inn.battingCard.forEach(function(bc,bc_index,bc_arr){
							if(fow.fowPlayerID == bc.playerId){
								option = document.createElement('option');
								option.value = bc.playerId;
								option.text = bc.player.full_name + " - " + bc.status;	
								select.appendChild(option);
							}
						});
					});
				}
			});
			
			select.setAttribute('onchange',"setDropdownOptionForWicketSequence(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionForWicketSequence(0);
			cellCount = cellCount + 1;
		break;
		case 'Alt_q':
			header_text.innerHTML = 'POTT';
			select = document.createElement('select');
			select.id = 'selectPott';
			select.name = select.id;
			dataToProcess.forEach(function(pott,index,arr1){	
				option = document.createElement('option');
	            option.value = pott.playerId1;
	            option.text = pott.player1.full_name;
	            select.appendChild(option);
	            
	            option = document.createElement('option');
	            option.value = pott.playerId2;
	            option.text = pott.player2.full_name;
	            select.appendChild(option);
	            
	            option = document.createElement('option');
	            option.value = pott.playerId3;
	            option.text = pott.player3.full_name;
	            select.appendChild(option);
	            
	            option = document.createElement('option');
	            option.value = pott.playerId4;
	            option.text = pott.player4.full_name;
	            select.appendChild(option);
	        });
	        
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			
			cellCount = cellCount + 1;
			break;
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
		case 'Control_F12': case 'Shift_F12':
			header_text.innerHTML = 'INFOBAR IDENT';
			select = document.createElement('select');
			select.id = 'selectIdentInfo';
			select.name = select.id;
			
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
						option.value = 'RESULT';
						option.text = 'Result';
						select.appendChild(option);
					}
				}
			});
			
			option = document.createElement('option');
			option.value = 'SUPEROVER';
			option.text = 'Super Over';
			select.appendChild(option);
						
			option = document.createElement('option');
			option.value = 'TOURNAMENT';
			option.text = 'Tournament';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = 'VENUE';
			option.text = 'Venue';
			select.appendChild(option);
			
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
		break;
		case 'F12':
			switch($('#selected_broadcaster').val().toUpperCase()){
			case 'NPL':
				
				select = document.createElement('select');
				select.id = 'selectMiddleStat';
				select.name = select.id;
				
				option = document.createElement('option');
				option.value = 'BATSMAN';
				option.text = 'Batsman/Bowler';
				select.appendChild(option);
				
				select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
				row.insertCell(cellCount).appendChild(select);
				setDropdownOptionToSelectOptionArray($(select),0);
				cellCount = cellCount + 1;
			
				break;
				
			case 'ISPL':
				
				select = document.createElement('select');
				select.id = 'selectMiddleStat';
				select.name = select.id;
				
				option = document.createElement('option');
				option.value = 'BATSMAN';
				option.text = 'Batsman/Bowler';
				select.appendChild(option);
				
				select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
				row.insertCell(cellCount).appendChild(select);
				setDropdownOptionToSelectOptionArray($(select),0);
				cellCount = cellCount + 1;
			
				break;
			
			case 'BENGAL-T20':
				select = document.createElement('select');
				select.id = 'selectMiddleStat';
				select.name = select.id;
				
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
	
				session_match.match.inning.forEach(function(inn,index,arr){
					if(inn.isCurrentInning == 'YES'){
						if(inn.inningNumber == 1){
							option = document.createElement('option');
							option.value = 'TOSS';
							option.text = 'Toss';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'CRR';
							option.text = 'Run Rate';
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
				
			case 'ICC-U19-2023':
				
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
			}
			
			
			header_text.innerHTML = 'MAIN INFOBAR';
			
			
			break;

		case 'Alt_1':
			switch($('#selected_broadcaster').val().toUpperCase()){
				case 'ISPL':
				header_text.innerHTML = 'INFOBAR MIDDLE';
				select = document.createElement('select');
				select.id = 'selectMiddleStat';
				select.name = select.id;
				
				/*option = document.createElement('option');
				option.value = 'CURR_PARTNERSHIP';
				option.text = 'Current Partnership';
				select.appendChild(option);*/
				if(session_match.setup.matchType == 'SUPER_OVER'){
					option = document.createElement('option');
					option.value = 'THIS_OVER';
					option.text = 'This Over';
					select.appendChild(option);
				}
				
				option = document.createElement('option');
				option.value = 'BOUNDARY';
				option.text = 'Inning Boundaries';
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
							/*option = document.createElement('option');
							option.value = 'TARGET';
							option.text = 'Target';
							select.appendChild(option);*/
							
							option = document.createElement('option');
							option.value = 'EQUATION';
							option.text = 'Equation';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'RESULT';
							option.text = 'Result';
							select.appendChild(option);
						}
					}
				});
				break;
				case 'ICC-U19-2023':
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
				
				if(session_match.setup.matchType == 'TEST'){
					
					option = document.createElement('option');
					option.value = 'REMAINING_OVERS';
					option.text = 'Remaining Overs';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'BOWLING_TEAM_TARGET';
					option.text = 'BOWLING TEAM TARGET';
					select.appendChild(option);
		
					option = document.createElement('option');
					option.value = 'FOLLOW_ON';
					option.text = 'Follow On';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'DAY_SESSION';
					option.text = 'Day Session';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'WHICH_INNING';
					option.text = 'Which inning';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'CURRENT_INNING_OVER';
					option.text = 'current inning Over';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'FIRST_INNING_SCORE';
					option.text = 'First Inning Score';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'LOCAL-TIME';
					option.text = 'Local Time';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'CURRENT_SESSION';
					option.text = 'Current Session Run Rate';
					select.appendChild(option);
					
				}
					break;
				
				case 'BENGAL-T20':
					header_text.innerHTML = 'LEFT BOTTON INFOBAR SECTION';
					select = document.createElement('select');
					select.id = 'selectLeftBottom';
					select.name = select.id;
		
					session_match.match.inning.forEach(function(inn,index,arr){
						if(inn.isCurrentInning == 'YES'){
							if(inn.inningNumber == 1){
								option = document.createElement('option');
								option.value = 'TOSS';
								option.text = 'Toss';
								select.appendChild(option);
								
								option = document.createElement('option');
								option.value = 'CRR';
								option.text = 'Run Rate';
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
					break;	
				}
				select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
				row.insertCell(cellCount).appendChild(select);
				setDropdownOptionToSelectOptionArray($(select),0);
				cellCount = cellCount + 1;
			break;
			
		case 'Alt_2':
			switch($('#selected_broadcaster').val().toUpperCase()){
				case 'ISPL':
				header_text.innerHTML = 'MIDDLE INFOBAR SECTION';
			
				select = document.createElement('select');
				select.id = 'selectMiddleStat';
				select.name = select.id;
	
				option = document.createElement('option');
				option.value = 'BATSMAN';
				option.text = 'Batsman/Bowler';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'CURR_PARTNERSHIP';
				option.text = 'Current Partnership';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'CRR';
				option.text = 'Run Rate';
				select.appendChild(option);
				
				session_match.match.inning.forEach(function(inn,index,arr){
					if(inn.isCurrentInning == 'YES'){
						if(inn.inningNumber == 1){
							
							/*option = document.createElement('option');
							option.value = 'PROJECTED';
							option.text = 'Projected Score';
							select.appendChild(option);*/
							
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
							option.value = 'EQUATION';
							option.text = 'Equation';
							select.appendChild(option);
						}
					}
				});
				break;
				
				case 'NPL':
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
				option.value = 'THIS_MATCH_FOURS';
				option.text = 'This Match Fours';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'THIS_MATCH_SIXES';
				option.text = 'This Match Sixes';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'TOURNAMENT_SIXES';
				option.text = 'Tournament Sixes';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'REVIEWS_REMAINING';
				option.text = 'REVIEWS REMAINING';
				select.appendChild(option);
				
				session_match.match.inning.forEach(function(inn,index,arr){
					if(inn.isCurrentInning == 'YES' && session_match.setup.matchType != 'TEST'){
						if(inn.inningNumber == 1){
							
							option = document.createElement('option');
							option.value = 'CRR';
							option.text = 'Current Run Rate';
							select.appendChild(option);
				
							option = document.createElement('option');
							option.value = 'TOSS';
							option.text = 'Toss';
							select.appendChild(option);
				
							option = document.createElement('option');
							option.value = 'PROJECTED';
							option.text = 'Projected Score';
							select.appendChild(option);
							
						}
						else{
							option = document.createElement('option');
							option.value = 'RRR';
							option.text = 'Required Run Rate';
							select.appendChild(option);
				
							option = document.createElement('option');
							option.value = 'TARGET';
							option.text = 'Target';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'EQUATION';
							option.text = 'Equation';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'RESULT';
							option.text = 'Result';
							select.appendChild(option);
							
							option = document.createElement('option');
							option.value = 'DLS_PAR_SCORE';
							option.text = 'D/L Par Score';
							select.appendChild(option);
						}
					}
				});
				break;
				
				case 'ICC-U19-2023':
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
					if(inn.isCurrentInning == 'YES' && session_match.setup.matchType != 'TEST'){
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
				if(session_match.setup.matchType == 'TEST'){
					
					option = document.createElement('option');
					option.value = 'LEAD_TRAIL_EQUATION';
					option.text = 'Lead Trail Equation';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'EQUATION';
					option.text = 'Equation';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'CURRENT_SESSION';
					option.text = 'This Session';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'DAY_PLAY';
					option.text = 'ToDay Play';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'OVER_RATE';
					option.text = 'Over Rate';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'BOWLING_TEAM_TARGET';
					option.text = 'BOWLING TEAM TARGET';
					select.appendChild(option);
		
				}
				break;
			case 'BENGAL-T20':
				header_text.innerHTML = 'MIDDLE INFOBAR SECTION';
			
				select = document.createElement('select');
				select.id = 'selectMiddleStat';
				select.name = select.id;
	
				option = document.createElement('option');
				option.value = 'BATSMAN';
				option.text = 'Batsman/Bowler';
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
				option.text = 'Current Partnership';
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
					if(inn.isCurrentInning == 'YES' && session_match.setup.matchType != 'TEST'){
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
						}
					}
				});
				break;	
			}
			
			
			row.insertCell(cellCount).appendChild(select);
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			
			break;
		case 'Control_4':
			header_text.innerHTML = 'INFOBAR FREE TEXT';
		
			select = document.createElement('input');
			select.type = "text";
			select.id = 'selectFreeText';
			select.value = '';
			select.style.width = '500px';
			
			select.setAttribute('onchange',"setTextBoxOptionToSelectOptionArray(0)");
			row.insertCell(cellCount).appendChild(select);
			setTextBoxOptionToSelectOptionArray(0);
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
		case 'Shift_C':
			header_text.innerHTML = 'SIX DISTANCE';
		
			select = document.createElement('input');
			select.type = "text";
			select.id = 'selectFreeText';
			
			select.setAttribute('onchange',"setTextBoxOptionToSelectOptionArray(0)");
			row.insertCell(cellCount).appendChild(select);
			setTextBoxOptionToSelectOptionArray(0);
			cellCount = cellCount + 1;
		break;
			
		case 'Alt_6':
			switch($('#selected_broadcaster').val().toUpperCase()){
				case 'ICC-U19-2023':
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
			}
			
			break;
			
		case 'Alt_7':
			switch($('#selected_broadcaster').val().toUpperCase()){
				case 'ISPL':
				
				select = document.createElement('select');
				select.id = 'selectRightBottom';
				select.name = select.id;
				
				option = document.createElement('option');
				option.value = 'BOWLER';
				option.text = 'Bowler';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'OVER';
				option.text = 'This Over';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'BOWLING_END';
				option.text = 'Bowling End';
				select.appendChild(option);
				break;
			case 'NPL':
				
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
				
				option = document.createElement('option');
				option.value = 'BOWLER_REPLACE';
				option.text = 'Bowler Replace';
				select.appendChild(option);
				break;	
			case 'ICC-U19-2023':
				
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
				
				option = document.createElement('option');
				option.value = 'BOWLER_REPLACE';
				option.text = 'Bowler Replace';
				select.appendChild(option);
				break;
			case 'BENGAL-T20':
				
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
				
				option = document.createElement('option');
				option.value = 'BOWLING_ECONOMY';
				option.text = 'Bowling Economy';
				select.appendChild(option);
				
				/*option = document.createElement('option');
				option.value = 'BOWLER_REPLACE';
				option.text = 'Bowler Replace';
				select.appendChild(option);*/
				break;	
			}
			header_text.innerHTML = 'RIGHT BOTTOM INFOBAR SECTION';
			
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1
			break;
			
		case 'Alt_8':
			switch($('#selected_broadcaster').val().toUpperCase()){
				case 'ISPL':
					header_text.innerHTML = 'RIGHT TOP INFOBAR SECTION';
		
					select = document.createElement('select');
					select.id = 'selectRightSection';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'TAPED_BALL';
					option.text = 'Tape Ball';
					select.appendChild(option);
					
					session_match.match.inning.forEach(function(inn,index,arr){
						if(inn.isCurrentInning == 'YES'){
							if(inn.inningNumber == 2){
								option = document.createElement('option');
								option.value = 'TARGET';
								option.text = 'Target';
								select.appendChild(option);
								
								option = document.createElement('option');
								option.value = 'EQUATION';
								option.text = 'Equation';
								select.appendChild(option);
							}
						}
					});
					
					option = document.createElement('option');
					option.value = 'TIMELINE';
					option.text = 'Timeline';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'SUPER_OVER';
					option.text = 'Super Over';
					select.appendChild(option);
					
					break;
					
				case 'ICC-U19-2023': case 'NPL':
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
					break;
				case 'BENGAL-T20':
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
					
					option = document.createElement('option');
					option.value = 'THIS_MATCH_SIXES';
					option.text = 'Inning Sixes';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'THIS_MATCH_FOURS';
					option.text = 'Inning Fours';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'TOURNAMENT_SIXES';
					option.text = 'Tournament Sixes';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'TOURNAMENT_FOURS';
					option.text = 'Tournament Fours';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'BALLS_SINCE_LAST_BOUNDARY';
					option.text = 'Ball Since Last Boundary';
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
					break;	
			}	
			
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
			switch($('#selected_broadcaster').val().toUpperCase()){
				case 'ICC-U19-2023':
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
				case 'ISPL':
					header_text.innerHTML = 'POWERPLAY';
	
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectTeam';
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
			}
			
			break;	
			
		case 'u':
			
			switch($('#selected_broadcaster').val().toUpperCase()){
				case 'ISPL': case 'BENGAL-T20': case 'NPL':
					header_text.innerHTML = '30-50 SPLIT';
			
					select = document.createElement('select');
					select.id = 'selectSplit';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = '30';
					option.text = '30-Split';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = '50';
					option.text = '50-Split';
					select.appendChild(option);
					
					select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
					row.insertCell(cellCount).appendChild(select);
					setDropdownOptionToSelectOptionArray($(select),0);
					cellCount = cellCount + 1
					break;
				case 'ICC-U19-2023':
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
			}
			break;
			
		case 'Control_F5': case 'Control_b': case 'Control_s': case 'Shift_F7'://Batsman Style

			switch(whatToProcess) {
			case 'Control_F5':
				header_text.innerHTML = 'BAT STYLE';
				break;
			case 'Control_b':
				header_text.innerHTML = 'BATTER IN AT';
				break;
			case 'Control_s':
				header_text.innerHTML = 'LT THIS SERIES BAT';
				break;
			case 'Shift_F7':
				header_text.innerHTML = 'BAT STYLE WITH PHOTO';
				break;	
			}
			select = document.createElement('select');
			select.id = 'selectPlayerName';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
					inn.battingCard.forEach(function(bc,index,arr){
						if(bc.status == 'NOT OUT'){
							if(bc.onStrike == 'YES'){
								option = document.createElement('option');
								option.value = bc.player.playerId;
								option.text = bc.player.full_name;
								select.appendChild(option);
							}else{
								option = document.createElement('option');
								option.value = bc.player.playerId;
								option.text = bc.player.full_name;
								select.appendChild(option);
							}
						}
					});
					
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
			removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			
			switch(whatToProcess) {
			case 'Control_s':
				switch($('#selected_broadcaster').val().toUpperCase()){
					case 'BENGAL-T20': case 'NPL':
					break;
					default:
						select = document.createElement('select');
						select.id = 'selectType';
						select.name = select.id;
						
						option = document.createElement('option');
						option.value = 'WITH_CURRENT';
						option.text = 'With Current Match';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = 'WITHOUT_CURRENT';
						option.text = 'Without Current Match';
						select.appendChild(option);
						
						select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
						row.insertCell(cellCount).appendChild(select);
						setDropdownOptionToSelectOptionArray($(select),1);
						cellCount = cellCount + 1;
				}
				break;
			}
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
					inn.battingCard.forEach(function(bc,index,arr){
						if(bc.status == 'NOT OUT'){
							if(bc.onStrike == 'YES'){
								option = document.createElement('option');
								option.value = bc.player.playerId;
								option.text = bc.player.full_name;
								select.appendChild(option);
							}else{
								option = document.createElement('option');
								option.value = bc.player.playerId;
								option.text = bc.player.full_name;
								select.appendChild(option);
							}
						}
					});
					
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
			removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			switch(whatToProcess){
				case 'Shift_P':
				switch($('#selected_broadcaster').val().toUpperCase()){
					case 'ICC-U19-2023': case 'NPL':
						
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
					
					switch($('#selected_broadcaster').val().toUpperCase()){
						case 'ICC-U19-2023':
						option = document.createElement('option');
						option.value = '3';
						option.text = 'Fifties';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = '4';
						option.text = 'Hundreds';
						select.appendChild(option);
						break;
						case 'NPL':
						option = document.createElement('option');
						option.value = '3';
						option.text = 'Thirties';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = '4';
						option.text = 'Fifties';
						select.appendChild(option);
						break;
					}
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
			}
			break;	
		case 'Alt_p':
			header_text.innerHTML = 'BUG TOSS';
			
			select = document.createElement('select');
			select.id = 'selectTeams';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = session_match.setup.homeTeam.teamName4 + '-' + 'BAT';
			option.text = session_match.setup.homeTeam.teamName4 + '-' + 'BAT';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = session_match.setup.homeTeam.teamName4 + '-' + 'FIELD';
			option.text = session_match.setup.homeTeam.teamName4 + '-' + 'FIELD';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = session_match.setup.awayTeam.teamName4 + '-' + 'BAT';
			option.text = session_match.setup.awayTeam.teamName4 + '-' + 'BAT';
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = session_match.setup.awayTeam.teamName4 + '-' + 'FIELD';
			option.text = session_match.setup.awayTeam.teamName4 + '-' + 'FIELD';
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
			
		case 'Shift_T': case 'Alt_F9': case 'Alt_F12': case 'Alt_F10': case 'Alt_z': case 'Shift_F8':
		case 'Control_Shift_F7':
			switch(whatToProcess) {
			case 'Shift_T': case 'Control_Shift_F7':
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
				
				/*select = document.createElement('select');
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
				cellCount = cellCount + 1*/
				
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
		
		case 'z': case 'x': case 'c': case 'v': case 'Control_z': case 'Control_x': case 'Control_c': case 'Control_v':
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
			case 'Control_c':
				header_text.innerHTML = 'LEADERBOARD - TAPE BALL';
				break;
			case 'Control_v':
				header_text.innerHTML = 'LEADERBOARD - MOST ECONOMICAL 50-50 OVER';
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
		
		case 'Control_Shift_P':
			switch(whatToProcess) {
			case 'Control_Shift_P':
				header_text.innerHTML = 'LT BOWLER SPELL';
				break;		
			}
			
			select = document.createElement('select');
			select.id = 'selectPlayerName';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
					inn.bowlingCard.forEach(function(boc,index,arr){
						option = document.createElement('option');
						option.value = boc.player.playerId;
						option.text = boc.player.full_name;
						select.appendChild(option);
					});
				}
			});

			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			
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
					inn.bowlingCard.forEach(function(boc,index,arr){
						if(boc.status == 'CURRENTBOWLER'){
							option = document.createElement('option');
							option.value = boc.player.playerId;
							option.text = boc.player.full_name;
							select.appendChild(option);
						}
					});
					
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
			removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			
			switch($('#selected_broadcaster').val().toUpperCase()){
					case 'BENGAL-T20': case 'NPL':
					break;
					default:
						select = document.createElement('select');
						select.id = 'selectType';
						select.name = select.id;
						
						option = document.createElement('option');
						option.value = 'WITH_CURRENT';
						option.text = 'With Current Match';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = 'WITHOUT_CURRENT';
						option.text = 'Without Current Match';
						select.appendChild(option);
						
						select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
						row.insertCell(cellCount).appendChild(select);
						setDropdownOptionToSelectOptionArray($(select),1);
						cellCount = cellCount + 1;
				}
			break;
				
		case 'Control_F9':  case 'Control_Shift_F9'://BowlerStyle
			switch(whatToProcess) {
			case 'Control_F9':
				header_text.innerHTML = 'BALL STYLE';
				break;	
			case 'Control_Shift_F9':
				header_text.innerHTML = 'BALL STYLE With Photo';
				break;		
			}
			
			select = document.createElement('select');
			select.id = 'selectPlayerName';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
					inn.bowlingCard.forEach(function(boc,index,arr){
						if(boc.status == 'CURRENTBOWLER'){
							option = document.createElement('option');
							option.value = boc.player.playerId;
							option.text = boc.player.full_name;
							select.appendChild(option);
						}
					});
					
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
			removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			
			switch(whatToProcess) {
			case 'Control_F9': case 'Control_Shift_F9':
				select = document.createElement('select');
				select.id = 'selectBowlingEnd';
				select.name = select.id;
				
				option = document.createElement('option');
				option.value = 'WITHOUTEND';
				option.text = 'WITHOUT END';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = session_match.setup.ground.first_bowling_end;
				option.text = session_match.setup.ground.first_bowling_end;
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = session_match.setup.ground.second_bowling_end;
				option.text = session_match.setup.ground.second_bowling_end;
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
					inn.bowlingCard.forEach(function(boc,index,arr){
						if(boc.status == 'CURRENTBOWLER'){
							option = document.createElement('option');
							option.value = boc.player.playerId;
							option.text = boc.player.full_name;
							select.appendChild(option);
						}
					});
					
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
			removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			cellCount = cellCount + 1;
			switch(whatToProcess){
			case 'Shift_Q':
				switch($('#selected_broadcaster').val().toUpperCase()){
					case 'ICC-U19-2023': case 'NPL':
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
					
					switch($('#selected_broadcaster').val().toUpperCase()){
						case 'ICC-U19-2023':
						option = document.createElement('option');
						option.value = '6';
						option.text = '5WI';
						select.appendChild(option);
						break;
						case 'NPL':
						option = document.createElement('option');
						option.value = '6';
						option.text = '3WI';
						select.appendChild(option);
						break;
					}
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
			}
			
			break;
			
		case 'Control_m': case 'Shift_F11': case 'Control_Shift_L'://MATCH-PROMO - PreviousMatchSummary 
			
			switch(whatToProcess) {
			case 'Control_Shift_L':
				header_text.innerHTML = 'MATCH PROMO';
				break;	
			case 'Control_m':
				header_text.innerHTML = 'FF MATCH PROMO';
				break;
			case 'Shift_F11':
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
		case 'y':
			select = document.createElement('select');
			select.id = 'selectBatsmanThisMatch';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
					inn.battingCard.forEach(function(bc,index,arr){
						if(bc.status == 'NOT OUT'){
							if(bc.onStrike == 'YES'){
								option = document.createElement('option');
								option.value = bc.player.playerId;
								option.text = bc.player.full_name + " - " + bc.status;
								select.appendChild(option);
							}else{
								option = document.createElement('option');
								option.value = bc.player.playerId;
								option.text = bc.player.full_name + " - " + bc.status;
								select.appendChild(option);
							}
						}
					});
					
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
			//removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
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
					inn.battingCard.forEach(function(bc,index,arr){
						if(bc.status == 'NOT OUT'){
							if(bc.onStrike == 'YES'){
								option = document.createElement('option');
								option.value = bc.player.playerId;
								option.text = bc.player.full_name;
								select.appendChild(option);
							}else{
								option = document.createElement('option');
								option.value = bc.player.playerId;
								option.text = bc.player.full_name;
								select.appendChild(option);
							}
						}
					});
					
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
			//removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
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
					inn.bowlingCard.forEach(function(boc,index,arr){
						if(boc.status == 'CURRENTBOWLER'){
							option = document.createElement('option');
							option.value = boc.player.playerId;
							option.text = boc.player.full_name;
							select.appendChild(option);
						}
					});
					
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
			//removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			break;	
		case 'F5': case 'Shift_A':
			switch(whatToProcess) {
			case 'F5': case 'Shift_A':
				header_text.innerHTML = 'BAT THIS MATCH';
				break;
			}
			select = document.createElement('select');
			select.id = 'selectBatsmanThisMatch';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
					inn.battingCard.forEach(function(bc,index,arr){
						if(bc.status == 'NOT OUT'){
							if(bc.onStrike == 'YES'){
								option = document.createElement('option');
								option.value = bc.playerId;
								option.text = bc.player.full_name + " - " + bc.status;
								select.appendChild(option);
							}else{
								option = document.createElement('option');
								option.value = bc.playerId;
								option.text = bc.player.full_name + " - " + bc.status;
								select.appendChild(option);
							}
						}
					});
					
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
			//removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			
			switch($('#selected_broadcaster').val().toUpperCase()){
				case 'BENGAL-T20':
					select = document.createElement('select');
					select.id = 'sponsorOrNot';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'sponsor';
					option.text = 'With Sponsor';
					select.appendChild(option);
				
					option = document.createElement('option');
					option.value = 'nosponsor';
					option.text = 'Without Sponsor';
					select.appendChild(option);
					
					select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
					row.insertCell(cellCount).appendChild(select);
					setDropdownOptionToSelectOptionArray($(select),1);
					cellCount = cellCount + 1
					
					select = document.createElement('select');
					select.id = 'statType';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'noStats';
					option.text = '';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'DT20';
					option.text = 'DT20';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'THISSERIES';
					option.text = 'THIS SERIES';
					select.appendChild(option);
					
					select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 2)");
					row.insertCell(cellCount).appendChild(select);
					setDropdownOptionToSelectOptionArray($(select),2);
					cellCount = cellCount + 1
				break;
			}
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
					inn.battingCard.forEach(function(bc,index,arr){
						if(bc.status == 'NOT OUT'){
							if(bc.onStrike == 'YES'){
								option = document.createElement('option');
								option.value = bc.playerId;
								option.text = bc.player.full_name + " - " + bc.status;
								select.appendChild(option);
							}else{
								option = document.createElement('option');
								option.value = bc.playerId;
								option.text = bc.player.full_name + " - " + bc.status;
								select.appendChild(option);
							}
						}
					});
					
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
			//removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			break;
		
		case 'g':
			select = document.createElement('select');
			select.id = 'selectBatamanThisMatch';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
					inn.bowlingCard.forEach(function(boc,index,arr){
						if(boc.status == 'CURRENTBOWLER'){
							option = document.createElement('option');
							option.value = boc.player.playerId;
							option.text = boc.player.full_name;
							select.appendChild(option);
						}
					});
					
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
			//removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			break;
		
		case 'F9': case 'Shift_R':
			switch(whatToProcess) {
			case 'F9': case 'Shift_R':
				header_text.innerHTML = 'BALL THIS MATCH';
				break;				
			}
			
			select = document.createElement('select');
			select.id = 'selectBatamanThisMatch';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
					inn.bowlingCard.forEach(function(boc,index,arr){
						if(boc.status == 'CURRENTBOWLER'){
							option = document.createElement('option');
							option.value = boc.player.playerId;
							option.text = boc.player.full_name;
							select.appendChild(option);
						}
					});
					
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
			//removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			
			switch($('#selected_broadcaster').val().toUpperCase()){
				case 'BENGAL-T20':
					select = document.createElement('select');
					select.id = 'sponsorOrNot';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'sponsor';
					option.text = 'With Sponsor';
					select.appendChild(option);
				
					option = document.createElement('option');
					option.value = 'nosponsor';
					option.text = 'Without Sponsor';
					select.appendChild(option);
					
					select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
					row.insertCell(cellCount).appendChild(select);
					setDropdownOptionToSelectOptionArray($(select),1);
					cellCount = cellCount + 1
					
					select = document.createElement('select');
					select.id = 'statType';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'noStats';
					option.text = '';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'DT20';
					option.text = 'DT20';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'THISSERIES';
					option.text = 'THIS SERIES';
					select.appendChild(option);
					
					select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 2)");
					row.insertCell(cellCount).appendChild(select);
					setDropdownOptionToSelectOptionArray($(select),2);
					cellCount = cellCount + 1
				break;
			}
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
					inn.bowlingCard.forEach(function(boc,index,arr){
						if(boc.status == 'CURRENTBOWLER'){
							option = document.createElement('option');
							option.value = boc.player.playerId;
							option.text = boc.player.full_name;
							select.appendChild(option);
						}
					});
					
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
			//removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
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
		case 'Control_j':
			header_text.innerHTML = 'SESSION';
			
			select = document.createElement('select');
			select.id = 'selectDays';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = '1';
			option.text = 'Day 1' ;
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = '2';
			option.text = 'Day 2' ;
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = '3';
			option.text = 'Day 3' ;
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = '4';
			option.text = 'Day 4' ;
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = '5';
			option.text = 'Day 5' ;
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			//removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			break;	
		case 'Alt_w':
			header_text.innerHTML = 'SESSION';
			
			select = document.createElement('select');
			select.id = 'selectday';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = '1';
			option.text = 'Day 1' ;
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = '2';
			option.text = 'Day 2' ;
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = '3';
			option.text = 'Day 3' ;
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = '4';
			option.text = 'Day 4' ;
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = '5';
			option.text = 'Day 5' ;
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
			row.insertCell(cellCount).appendChild(select);
			//removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			
			select = document.createElement('select');
			select.id = 'selectsession';
			select.name = select.id;
			
			option = document.createElement('option');
			option.value = '1';
			option.text = 'Session 1' ;
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = '2';
			option.text = 'Session 2' ;
			select.appendChild(option);
			
			option = document.createElement('option');
			option.value = '3';
			option.text = 'Session 3' ;
			select.appendChild(option);
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),1);
			cellCount = cellCount + 1;
			break;
		case 'Shift_Z':
			switch(whatToProcess) {
			case 'Shift_Z':
				header_text.innerHTML = 'BEST STATS - THIS SERIES';
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
						option.text = bc.player.full_name;	
						select.appendChild(option);
					});
				}
			});
			
			select.setAttribute('onchange','setDropdownOptionToSelectOptionArray(this, 0)');
			row.insertCell(cellCount).appendChild(select);
			//removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			break;
			
		case 'Shift_X':
			switch(whatToProcess) {
			case 'Shift_X':
				header_text.innerHTML = 'BEST FIGURE - THIS SERIES';
				break;			
			}
			
			select = document.createElement('select');
			select.id = 'selectHowoutPlayers';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == (3 - document.getElementById('which_inning').value)){
					inn.battingCard.forEach(function(bc,bc_index,bc_arr){
						option = document.createElement('option');
						option.value = bc.playerId;
						option.text = bc.player.full_name;	
						select.appendChild(option);
					});
				}
			});
			
			select.setAttribute('onchange','setDropdownOptionToSelectOptionArray(this, 0)');
			row.insertCell(cellCount).appendChild(select);
			//removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			break;
			
		case 'Alt_Shift_C'://HowOut //how out w/o fielder // how out both
			switch(whatToProcess) {
			case 'Alt_Shift_C':
				header_text.innerHTML = 'CAPTAIN';
				break;				
			}
			select = document.createElement('select');
			select.id = 'selectCaptainPlayers';
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
			
			select.setAttribute('onchange','setDropdownOptionToSelectOptionArray(this, 0)');
			row.insertCell(cellCount).appendChild(select);
			//removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			break;
				
		case 'F6': case 'Shift_F6': case 'Alt_F6'://HowOut //how out w/o fielder // how out both
			switch(whatToProcess) {
			case 'F6':
				header_text.innerHTML = 'HOW OUT';
				break;
			case 'Shift_F6':
				header_text.innerHTML = 'HOW OUT WITHOUT FIELDER';
				break;	
			case 'Alt_F6':
				header_text.innerHTML = 'HOW OUT BOTH';
				break;				
			}
			select = document.createElement('select');
			select.id = 'selectHowoutPlayers';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
					inn.battingCard.forEach(function(bc,bc_index,bc_arr){
						if(inn.fallsOfWickets.length > 0){
							if(bc.playerId == inn.fallsOfWickets[inn.fallsOfWickets.length-1].fowPlayerID){
								option = document.createElement('option');
								option.value = bc.playerId;
								option.text = bc.player.full_name + " - " + bc.status;	
								select.appendChild(option);
							}
						}
						
					});
					
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
			//removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
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
				option.text = aos.full_name  + ' (OTHER)'+' -(' + session_match.setup.awayTeam.teamName4 + ')';
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
					inn.battingCard.forEach(function(bc,index,arr){
						if(bc.status == 'NOT OUT'){
							if(bc.onStrike == 'YES'){
								option = document.createElement('option');
								option.value = bc.player.playerId;
								option.text = bc.player.full_name;
								select.appendChild(option);
							}else{
								option = document.createElement('option');
								option.value = bc.player.playerId;
								option.text = bc.player.full_name;
								select.appendChild(option);
							}
						}
					});
					
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
			//removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			
			select = document.createElement('select');
			select.id = 'selectProfile';
			select.name = select.id;
			
			switch($('#selected_broadcaster').val().toUpperCase()){
				case 'NPL':
					option = document.createElement('option');
					option.value = 'DT20';
					option.text = 'DT20';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'IT20';
					option.text = 'IT20';
					select.appendChild(option);
					break;
				case 'BENGAL-T20':
					option = document.createElement('option');
					option.value = 'BPTL2024';
					option.text = 'BPTL2024';
					select.appendChild(option);
									
					option = document.createElement('option');
					option.value = 'DT20';
					option.text = 'DT20';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'IPL';
					option.text = 'IPL';
					select.appendChild(option);
				break;
				default:
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
			}
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),1);
			cellCount = cellCount + 1
			
			switch(whatToProcess){
			case 'Control_d':
				switch($('#selected_broadcaster').val().toUpperCase()){
					case 'ICC-U19-2023': case 'NPL':
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
					
					switch($('#selected_broadcaster').val().toUpperCase()){
						case 'ICC-U19-2023':
						option = document.createElement('option');
						option.value = '3';
						option.text = 'Fifties';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = '4';
						option.text = 'Hundreds';
						select.appendChild(option);
						break;
						case 'NPL':
						option = document.createElement('option');
						option.value = '3';
						option.text = 'Thirties';
						select.appendChild(option);
						
						option = document.createElement('option');
						option.value = '4';
						option.text = 'Fifties';
						select.appendChild(option);
						break;
					}
					
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
			}
			break;		
		case 'Alt_3':
			switch($('#selected_broadcaster').val().toUpperCase()){
				case 'ICC-U19-2023': case 'NPL':
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
							inn.battingCard.forEach(function(bc,index,arr){
								if(bc.status == 'NOT OUT'){
									if(bc.onStrike == 'YES'){
										option = document.createElement('option');
										option.value = bc.player.playerId;
										option.text = bc.player.full_name;
										select.appendChild(option);
									}else{
										option = document.createElement('option');
										option.value = bc.player.playerId;
										option.text = bc.player.full_name;
										select.appendChild(option);
									}
								}
							});
							
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
					removeSelectDuplicates(select.id);
					setDropdownOptionToSelectOptionArray($(select),0);
					cellCount = cellCount + 1;
					
					select = document.createElement('select');
					select.id = 'selectProfile';
					select.name = select.id;
					
					switch($('#selected_broadcaster').val().toUpperCase()){
						case 'ICC-U19-2023':
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
						break;
						case 'NPL':
							option = document.createElement('option');
							option.value = 'DT20';
							option.text = 'DT20';
							select.appendChild(option);
						break;
					}
					
					select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
					row.insertCell(cellCount).appendChild(select);
					setDropdownOptionToSelectOptionArray($(select),1);
					cellCount = cellCount + 1
					break;	
			}

			
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
			switch($('#selected_broadcaster').val().toUpperCase()){
				case 'ICC-U19-2023': case 'NPL':
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
							inn.bowlingCard.forEach(function(boc,index,arr){
								if(boc.status == 'CURRENTBOWLER'){
									option = document.createElement('option');
									option.value = boc.player.playerId;
									option.text = boc.player.full_name;
									select.appendChild(option);
								}
							});
							
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
					removeSelectDuplicates(select.id);
					setDropdownOptionToSelectOptionArray($(select),0);
					cellCount = cellCount + 1;
					
					select = document.createElement('select');
					select.id = 'selectProfile';
					select.name = select.id;
					
					switch($('#selected_broadcaster').val().toUpperCase()){
						case 'ICC-U19-2023':
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
						break;
						case 'NPL':
							option = document.createElement('option');
							option.value = 'DT20';
							option.text = 'DT20';
							select.appendChild(option);
						break;
					}
					
					select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
					row.insertCell(cellCount).appendChild(select);
					setDropdownOptionToSelectOptionArray($(select),1);
					cellCount = cellCount + 1;
					break;
			}			
			break;
		case 'F11': case 'Control_e'://Lt Ball Profile
			switch(whatToProcess){
			case 'F11':
				header_text.innerHTML = 'BALL PLAYER PROFILE';
				break;
			}
		
			select = document.createElement('select');
			select.id = 'selectPlayerName';
			select.name = select.id;
			
			session_match.match.inning.forEach(function(inn,index,arr){
				if(inn.inningNumber == document.getElementById('which_inning').value){
					inn.bowlingCard.forEach(function(boc,index,arr){
						if(boc.status == 'CURRENTBOWLER'){
							option = document.createElement('option');
							option.value = boc.player.playerId;
							option.text = boc.player.full_name;
							select.appendChild(option);
						}
					});
					
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
			//removeSelectDuplicates(select.id);
			setDropdownOptionToSelectOptionArray($(select),0);
			removeSelectDuplicates(select.id);
			cellCount = cellCount + 1;
			
			select = document.createElement('select');
			select.id = 'selectProfile';
			select.name = select.id;
			
			switch($('#selected_broadcaster').val().toUpperCase()){
				case 'NPL':
					option = document.createElement('option');
					option.value = 'DT20';
					option.text = 'DT20';
					select.appendChild(option);
					option = document.createElement('option');
					option.value = 'IT20';
					option.text = 'IT20';
					select.appendChild(option);
				break;
				case 'BENGAL-T20':
					option = document.createElement('option');
					option.value = 'BPTL2024';
					option.text = 'BPTL2024';
					select.appendChild(option);
					option = document.createElement('option');
					option.value = 'DT20';
					option.text = 'DT20';
					select.appendChild(option);
					option = document.createElement('option');
					option.value = 'IPL';
					option.text = 'IPL';
					select.appendChild(option);
				break;
				default:
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
			}
			
			select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 1)");
			row.insertCell(cellCount).appendChild(select);
			setDropdownOptionToSelectOptionArray($(select),1);
			cellCount = cellCount + 1
			
			switch(whatToProcess){
			case 'Control_e':
				switch($('#selected_broadcaster').val().toUpperCase()){
					case 'ICC-U19-2023': case 'NPL':
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
			$('#selectBugdb').select2();
			break;
		}
		
		if(whatToProcess == 'Shift_I'){
			option = document.createElement('input');
			option.type = 'button';
			option.name = 'change_on';
			option.value = 'Change On';
		    option.id = option.name;
		    option.setAttribute('onclick','processUserSelection(this)');
		    
		    div = document.createElement('div');
		    div.append(option);
		    row.insertCell(cellCount).appendChild(div);
	    	cellCount = cellCount + 1;
		}
		
		switch(whatToProcess){
			case 'Shift_C':
			case 'Control_m': case 'F4': case 'F5': case 'F6': case 'Alt_w': case 'Control_j': case 'F8': case 'F9': case 'F10': case 'F7': case 'F11':
			case 'Control_F5': case 'Control_F9': case 'Shift_T': case 'u': case 'p': case 'Control_p': case 'Control_d': case 'Control_e': case 'z': 
			case 'x': case 'c': case 'v': case 'Shift_F11': case 'Control_y': case 'Alt_F8': case 'Alt_F1': case 'Alt_F2': case 'Shift_K': case 'Shift_O': 
			case 'k': case 'g': case 'y': case 'Shift_F5': case 'Shift_F9': case 'Control_h': case 'Control_g': case 'q': case 'j': case 'Shift_F6': case 'Shift_F8':
			case 'Control_s':  case 'Control_f': case 'Alt_F12': case 'l': case 'Shift_E': //case 'Alt_F9': 
			case 'F12': case 'Alt_1': case 'Alt_2': case 'Alt_3': case 'Alt_4': case 'Alt_5': case 'Alt_6': case 'Alt_7': case 'Alt_8': case 'Alt_9': case 'Alt_0':
			case 'Alt_m': case 'Alt_n': case 'Control_b': case 'Alt_p': case 'Alt_F10': case 'Alt_d': case 'Shift_F4': case 'Alt_a': case 'Alt_s': case 'Shift_P': 
			case 'Shift_Q': case 'Alt_z': case 'Control_c': case 'Control_v': case 'Control_z': case 'Control_x': case 'Alt_q': case 'Shift_F': case 'Alt_F6': 
			case 'Shift_A': case 'Shift_R': case 'Control_Shift_F1': case 'Control_Shift_D': case 'Alt_Shift_Z': case 'Control_Shift_F7': case 'Control_Shift_F2':
			case 'Alt_c': case 'Control_F12': case 'Shift_F12': case 'F1': case 'Shift_F7': case 'Control_Shift_F9': case 'Alt_Shift_C': case 'Control_Shift_L':
			case 'Shift_Z': case 'Shift_X': case 'Control_i': case 'Control_Shift_E': case 'Control_Shift_F': case 'Control_Shift_P': case 'Shift_I': 
			case 'Control_F11': case 'Control_Shift_M': case 'Alt_Shift_R': case 'Control_Shift_U': case 'Control_Shift_V': case 'Control_4': case 'Control_Shift_F4':
				
				option = document.createElement('input');
				option.type = 'button';
				option.name = 'populate_btn';
				option.value = 'Populate Data';
			    option.id = option.name;
			    option.setAttribute('onclick','processUserSelection(this)');
			    
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
			    break;
		}
		
		document.getElementById('select_graphic_options_div').style.display = '';
		break;
	}
}
