var session_match;
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
function initialiseOutput()
{
	selected_options = [];
	for(var i = 1; i <= 4; i++) {
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
		document.getElementById('vizSecondaryIPAddress').value = dataToProcess.secondaryIpAddress;
		document.getElementById('vizSecondaryPortNumber').value = dataToProcess.secondaryPortNumber;
		document.getElementById('vizTertiaryIPAddress').value = dataToProcess.tertiaryIpAddress;
		document.getElementById('vizTertiaryPortNumber').value = dataToProcess.tertiaryPortNumber;
		break;
	
	case 'UPDATE-MATCH-ON-OUTPUT-FORM':
	
		dataToProcess.match.inning.forEach(function(inn,index,arr){
			if(inn.isCurrentInning == 'YES'){
			inn.battingCard.forEach(function(bc,index,arr){
					if(inn.partnerships != null && inn.partnerships.length > 0) {
						
						inn.partnerships.forEach(function(par,index,arr){
								if(bc.playerId == par.firstBatterNo) {
								if(bc.onStrike == 'YES'){
									document.getElementById('inning1_battingcard1_lbl').innerHTML = bc.player.surname + '*' + ' ' + bc.runs + '(' + bc.balls + ')' ;
								}
							}
						else if(bc.playerId == par.secondBatterNo) {
							if(bc.onStrike == 'NO'){
								document.getElementById('inning1_battingcard2_lbl').innerHTML = bc.player.surname + ' ' + bc.runs + '(' + bc.balls + ')';
							}
						}
							});
						
					}
					document.getElementById('inning1_totalruns_lbl').innerHTML = inn.batting_team.teamName4 + '-' + parseInt(inn.totalRuns) + '-' 
										+ parseInt(inn.totalWickets) + '('+ parseInt(inn.totalOvers) + '.' + parseInt(inn.totalBalls) + ')' ;
				
			});
			}
			if(inn.isCurrentInning == 'YES'){
			inn.bowlingCard.forEach(function(boc,index,arr){
						if(boc.status == 'CURRENTBOWLER'){
							document.getElementById('inning1_bowlingcard_lbl').innerHTML = boc.player.surname + ' ' + boc.wickets 
										+ '-' + boc.runs + '(' + boc.overs + '.' + boc.balls + ')';
						}else if(boc.status == 'LASTBOWLER'){
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
	case 'select_second_broadcaster':
		if($('#select_second_broadcaster').val() == 'Quidich'){
			$("#qtIPAddress").show();
			$("#qtPortNumber").show();
			$('label[for=qtIPAddress], input#qtIPAddress').show();
			$('label[for=qtPortNumber], input#qtPortNumber').show();
		}else{
			$("#qtIPAddress").hide();
			$("#qtPortNumber").hide();
			$('label[for=qtIPAddress], input#qtIPAddress').hide();
			$('label[for=qtPortNumber], input#qtPortNumber').hide();
		}
		break;
	case 'load_scene_btn':
      	document.initialise_form.submit();
		break;
	case 'cancel_graphics_btn':
		$("#select_graphic_options_div").empty();
		document.getElementById('select_graphic_options_div').style.display = 'none';
		$("#captions_div").show();
		break;
		
	case 'populate_btn':
		//alert(selected_options.toString());
		//alert('KEY ' + $('#which_keypress').val());
		processCricketProcedures("POPULATE-GRAPHICS", $('#which_keypress').val() + ',' + selected_options.toString());
		break;
	}	
}

function processUserSelectionData(whatToProcess,dataToProcess)
{
	switch (whatToProcess) {
	case 'LOGGER_FORM_KEYPRESS':
		if(dataToProcess=='SPEED' || dataToProcess == 'RE_READ_DATA'){
			
		}else{
			dataToProcess = parseInt(dataToProcess);
		}
		document.getElementById('which_keypress').value = parseInt(dataToProcess);
		switch(dataToProcess) {
		case 'SPEED':
			processCricketProcedures('SHOW_SPEED');
			break;
		case 'RE_READ_DATA':
			processCricketProcedures('RE_READ_DATA');
			break;
		case 32:
			processCricketProcedures('CLEAR-ALL');
			break;
		case 49: case 50: case 51: case 52:
			if(session_match.setup.maxOvers > 0){
				switch (dataToProcess) {
				case 51: case 52: // Key 1 to 4
					document.body.focus();
					keys = [];
					alert("3rd and 4th inning NOT available in a limited over match");
					return false;
				}				
			}
			document.getElementById('which_inning').value = parseInt(dataToProcess) - 48;
			document.getElementById('selected_inning').innerHTML = 'Selected Inning: ' + (parseInt(dataToProcess) - 48);
			break;
			
		case 189:
			document.body.focus();
			keys = [];
			if(confirm('It will Also Delete Your Preview from Directory...\r\n \r\nAre You Sure To Animate Out? ') == true){
				processCricketProcedures('ANIMATE-OUT');
			}
			break;
		case 16122: case 77: case 112: case 113:
			if(document.getElementById('selected_inning').innerHtml < 1 && document.getElementById('selected_inning').innerHtml > 4) {
				document.body.focus();
				keys = [];
				alert(document.getElementById('selected_inning').innerHtml + ' must be between 1 to 4');
				return false;
			}
			dataToProcess = dataToProcess + ',' + document.getElementById('which_inning').value;
			processCricketProcedures("POPULATE-GRAPHICS", dataToProcess);
			break;
		
		case 1777: // MATCH PROMO
		case 118: case 122: //Lt-Bat-Ball-Profile
		case 121: // NAMESUPER
		case 116: case 117: case 119: case 120: //BatThisMatch, HowOut, NameSuper-Player, BallThisMatch
		 	switch(dataToProcess) {
			case 116: case 117 : case 118: case 119: case 120: case 122:
				addItemsToList(dataToProcess,null);
				break;
			default:
				processCricketProcedures("GRAPHICS-OPTIONS", dataToProcess);
				break;
			}
			break;
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
					if(data == true) {
						document.body.focus();
						keys = [];
						if(confirm('Animate In?') == true){
							processCricketProcedures(whatToProcess.replace('POPULATE-', 'ANIMATE-IN-'),dataToProcess);
						}
					}else if(data == false){
						document.body.focus();
						keys = [];
						alert('HAVE A ISSUE IN DATA FILLING');
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
	selected_options[whichIndex] = $('#' + $(whichInput).attr('id') + ' option:selected').val();
}
function addItemsToList(whatToProcess,dataToProcess)
{
	var select,option,header_text,div,table,tbody,row;
	var cellCount = 0;
	
	switch(whatToProcess) {
	case 1777: case 116: case 117: case 118: case 119: case 120: case 121: case 122:
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
			case 1777: //MATCH-PROMO
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
			case 116://BatThisMatch
				select = document.createElement('select');
				select.id = 'selectBatamanThisMatch';
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
			case 120://BallThisMatch
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
			case 117://HowOut
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
				
				select.setAttribute('onchange',"setDropdownOptionToSelectOptionArray(this, 0)");
				row.insertCell(cellCount).appendChild(select);
				setDropdownOptionToSelectOptionArray($(select),0);
				cellCount = cellCount + 1;
				break;
			case 118://Lt Bat Profile
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
			case 119://NameSuper Player
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
			case 121://NameSuperDB
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
			case 122://Lt Ball Profile
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
