<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>

  <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
  
  <title>Output</title>
	
  <script type="text/javascript" src="<c:url value="/webjars/jquery/3.6.0/jquery.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/webjars/bootstrap/5.1.3/js/bootstrap.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/resources/javascript/index.js"/>"></script>
  
  <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css"/>"/>  
  <link href="<c:url value="/webjars/font-awesome/6.0.0/css/all.css"/>" rel="stylesheet">
  
  <script type="text/javascript">
    
  	function KeyPress(e) {
  		
      var evtobj = window.event? event : e;
      
      e.preventDefault();
      
      var whichKey = '';
	  var validKeyFound = false;
    
      if(evtobj.ctrlKey) {
    	  whichKey = 'Control';
      }
      if(evtobj.altKey) {
    	  if(whichKey) {
        	  whichKey = whichKey + '_Alt';
    	  } else {
        	  whichKey = 'Alt';
    	  }
      }
      if(evtobj.shiftKey) {
    	  if(whichKey) {
        	  whichKey = whichKey + '_Shift';
    	  } else {
        	  whichKey = 'Shift';
    	  }
      }
      
	  if(evtobj.keyCode) {
    	  if(whichKey) {
    		  if(!whichKey.includes(evtobj.key)) {
            	  whichKey = whichKey + '_' + evtobj.key;
    		  }
    	  } else {
        	  whichKey = evtobj.key;
    	  }
	  }

	  validKeyFound = false;
	  if (whichKey.includes('_')) {
		  whichKey.split("_").forEach(function (this_key) {
			  switch (this_key) {
			  case 'Control': case 'Shift': case 'Alt':
				break;
			  default:
				validKeyFound = true;
				break;
			  }
		  });
	   } else {
		  if(whichKey != 'Control' && whichKey != 'Alt' && whichKey != 'Shift') {
			  validKeyFound = true;
		  }
	   }
	   if(validKeyFound == true) {
		  processUserSelectionData('LOGGER_FORM_KEYPRESS',whichKey);
	   }
	}

	document.onkeydown = KeyPress;  
  	setInterval(() => {processCricketProcedures('READ-MATCH-AND-POPULATE');}, 1000);
	
  </script>
    
</head>
<body onload="initialiseSelectedOptionsList()">
<form:form name="output_form" method="POST" action="output" enctype="multipart/form-data">
<div class="content py-5" style="background-color: #EAE8FF; color: #2E008B">
  <div class="container">
	<div class="row">
	 <div class="col-md-8 offset-md-2">
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
           <div class="card-header">
             <h3 class="mb-0">Output</h3>
            <!--   <h3 class="mb-0">${licence_expiry_message}</h3>  -->
           </div>
          <div class="card-body">
          
			  <div id="select_graphic_options_div" style="display:none;">
			  </div>
			  <div id="captions_div" class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label class="col-sm-4 col-form-label text-left">Match: ${session_match.match.matchFileName} </label>
			    <label class="col-sm-4 col-form-label text-left">Broadcaster: ${session_configuration.broadcaster.replace("_"," ")} </label>
			    <label class="col-sm-4 col-form-label text-left">2nd Broadcaster: ${session_configuration.secondaryBroadcaster.replace("_"," ")} </label>
			    <label id="selected_inning" class="col-sm-4 col-form-label text-left">Selected Inning: </label>
			    <label id="speed_lbl" class="col-sm-4 col-form-label text-left">Show Speed: YES</label>
			    <c:if test="${(session_configuration.secondaryBroadcaster == 'quidich')}">
					<div class="left" style = "margin-top: 5px; font-size: 21px;">
				
					<button style="background-color:#ffeb2b;color:#000000;margin:3px;" class="btn btn-sm" type="button"
				  		name="f4_graphic_btn" id="f4_graphic_btn" onclick="processUserSelection(this)"> RESET</button>
				  	<button style="background-color:#ffeb2b;color:#000000;margin:3px;" class="btn btn-sm" type="button"
				  		name="f6_graphic_btn" id="f6_graphic_btn" onclick="processUserSelection(this)"> STAND-BY</button>
				  	<button style="background-color:#ffeb2b;color:#000000;margin:3px;" class="btn btn-sm" type="button"
				  		name="f7_graphic_btn" id="f7_graphic_btn" onclick="processUserSelection(this)"> ANIMATE IN</button>
				  	<button style="background-color:#ffeb2b;color:#000000;margin:3px;" class="btn btn-sm" type="button"
				  		name="f8_graphic_btn" id="f8_graphic_btn" onclick="processUserSelection(this)"> STADIUM ANIMATE OUT</button>	
				  	<button style="background-color:#ffeb2b;color:#000000;margin:3px;" class="btn btn-sm" type="button"
				  		name="f9_graphic_btn" id="f9_graphic_btn" onclick="processUserSelection(this)"> ANIMATE OUT</button>
				  		
				  	<button style="background-color:#ffeb2b;color:#000000;margin:3px;" class="btn btn-sm" type="button"
				  		name="f_graphic_btn" id="f_graphic_btn" onclick="processUserSelection(this)"> SHOW PITCH DIMENSIONS</button>
				  	<button style="background-color:#ffeb2b;color:#000000;margin:3px;" class="btn btn-sm" type="button"
				  		name="s_graphic_btn" id="s_graphic_btn" onclick="processUserSelection(this)"> SHOW STADIUM</button>
				  	<button style="background-color:#ffeb2b;color:#000000;margin:3px;" class="btn btn-sm" type="button"
				  		name="w_graphic_btn" id="w_graphic_btn" onclick="processUserSelection(this)"> SHOW WIND</button>
				  	<button style="background-color:#ffeb2b;color:#000000;margin:3px;" class="btn btn-sm" type="button"
				  		name="z_graphic_btn" id="z_graphic_btn" onclick="processUserSelection(this)"> ZOOM PITCH</button>			
				  			
				</div>	
				</c:if>	
			  </div>
			  </div>
	       </div>
	    </div>
       </div>
    </div>
  </div>
<input type="hidden" id="which_keypress" name="which_keypress"/>
<input type="hidden" id="which_inning" name="which_inning"/>
<input type="hidden" name="selected_broadcaster" id="selected_broadcaster" value="${session_configuration.broadcaster}"/>
<input type="hidden" name="selected_second_broadcaster" id="selected_second_broadcaster" value="${session_configuration.secondaryBroadcaster}"/>
<input type="hidden" name="selected_match_max_overs" id="selected_match_max_overs" value="${session_match.setup.maxOvers}"/>
<input type="hidden" id="matchFileTimeStamp" name="matchFileTimeStamp" value="${session_match.setup.matchFileTimeStamp}"></input>
</form:form>
</body>
</html>