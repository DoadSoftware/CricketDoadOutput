var session_match;
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
	case 'populate_namesuper_btn':
		case 'populate_namesuper_btn':
		processCricketProcedures("POPULATE-GRAPHICS", 121 + ',' + $('#selectNameSuper option:selected').val());
		break;
	}	
}
function processUserSelectionData(whatToProcess,dataToProcess){
	switch (whatToProcess) {
	case 'LOGGER_FORM_KEYPRESS':
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
					alert("3rd and 4th inning NOT available in a limited over match");
					return false;
				}				
			}
			document.getElementById('which_keypress').value = parseInt(dataToProcess) - 48;
			document.getElementById('selected_inning').innerHTML = 'Selected Inning: ' + (parseInt(dataToProcess) - 48);
			break;
			
		case 189:
			if(confirm('It will Also Delete Your Preview from Directory...\r\n \r\nAre You Sure To Animate Out? ') == true){
				processCricketProcedures('ANIMATE-OUT');
			}
			break;
		case 77:
			processCricketProcedures("POPULATE-GRAPHICS", dataToProcess);
			break;
		case 112: case 113:
			dataToProcess = dataToProcess + ',' + document.getElementById('which_keypress').value;
			processCricketProcedures("POPULATE-GRAPHICS", dataToProcess);
			break;
		case 121: // NAME SUPER
			processCricketProcedures("NAMESUPER-GRAPHICS-OPTIONS", dataToProcess);
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
						if(confirm('Animate In?') == true){
							processCricketProcedures(whatToProcess.replace('POPULATE-', 'ANIMATE-IN-'),dataToProcess);
						}
					}else if(data == false){
						alert('HAVE A ISSUE IN DATA FILLING');
					}
				}else if(whatToProcess == 'NAMESUPER-GRAPHICS-OPTIONS'){
					addItemsToList('NAMESUPER-OPTION',data);
				}
				break;
			}
		}
	});
}
function addItemsToList(whatToProcess,dataToProcess)
{
	var select,option,header_text,div,table,tbody,row,max_cols;
	var cellCount = 0;
	
	
	switch(whatToProcess) {
	case 'NAMESUPER-OPTION':
		$('#captions_div').empty();
	
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
		case 'NAMESUPER-OPTION':
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
				
				row.insertCell(cellCount).appendChild(select);
				cellCount = cellCount + 1;
				
				option = document.createElement('input');
				option.type = 'button';
				option.name = 'populate_namesuper_btn';
			    option.value = 'Populate Namesuper';
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
		break;
	}
}
