<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>

  <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
  <title>Output Screen</title>
	
  <script type="text/javascript" src="<c:url value="/webjars/jquery/3.6.0/jquery.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/webjars/bootstrap/5.1.3/js/bootstrap.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/resources/javascript/index.js"/>"></script>
  
  <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css"/>"/>  
  <link href="<c:url value="/webjars/font-awesome/6.0.0/css/all.css"/>" rel="stylesheet">
  
  <script type="text/javascript">
  
  var key_val = 0;
  
  $(document).on("keydown", function(e){
	  var event = document.all ? window.event : e;
	  switch (e.target.tagName.toLowerCase()) {
	    case "input":
	    case "textarea":
	      break;
	    default:
	      if(e.altKey && e.key === 's'){
	   		  e.preventDefault()
	   		  processUserSelectionData('LOGGER_FORM_KEYPRESS','SPEED');
	   	  }else if(e.altKey && e.key === 'r'){
	   		  e.preventDefault()
	   		  processUserSelectionData('LOGGER_FORM_KEYPRESS','RE_READ_DATA');
	   	  }else{
	   		  e.preventDefault();
	   		  key_val = e.which;
	   		  if(e.altKey) {
	   			key_val = key_val + 250;
	   		  }
			  if(e.ctrlKey) {
		   		key_val = key_val + 300;
	   		  }
			  if(e.shiftKey) {
		   		key_val = key_val + 350;
		   	  }
			  //DJ check upper case and lower case
	   		  processUserSelectionData('LOGGER_FORM_KEYPRESS',key_val);
	   	  }
	      break;
	  }
  }); 
  
  setInterval(() => {processCricketProcedures('READ-MATCH-AND-POPULATE');}, 1000);
 	
  </script>

</head>
<body>
<form:form name="output_form" autocomplete="off" action="POST">
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
			  <div id="lastxball_div" style="display:none;">
			  </div>
			  <div id="captions_div" class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label class="col-sm-4 col-form-label text-left">Match: ${session_match.match.matchFileName} </label>
			    <label class="col-sm-4 col-form-label text-left">Broadcaster: ${session_selected_broadcaster.replace("_"," ")} </label>
			    <label class="col-sm-4 col-form-label text-left">2nd Broadcaster: ${session_selected_second_broadcaster.replace("_"," ")} </label>
			    <label id="selected_inning" class="col-sm-4 col-form-label text-left">Current Inning: ${current_inning} </label>
			    <label id="inning1_totalruns_lbl" class="col-sm-4 col-form-label text-left">${curr_team_total}</label>
			    <label id="inning1_battingcard1_lbl" class="col-sm-4 col-form-label text-left">${curr_player}</label>
			    <label id="inning1_battingcard2_lbl" class="col-sm-4 col-form-label text-left">${curr_player2}</label>
			    <label id="inning1_bowlingcard_lbl" class="col-sm-4 col-form-label text-left">${curr_bowler}</label>
			    <label id="speed_lbl" class="col-sm-4 col-form-label text-left">Show Speed: YES</label>
				<!-- <label>SPEED </label>
				<input type = "text" name = "speedtext" id="speedtext"/>
				<button style="background-color:#ffeb2b;color:#000000;" class="btn btn-sm" type="button"
		  			name="animate_in_speed" id="animate_in_speed" onclick="processUserSelection(this)"> Animate-In Speed </button>  -->
				<button style="background-color:#f44336;color:#FEFEFE;" class="btn btn-sm" type="button"
			  		name="animateout_graphic_btn" id="animateout_graphic_btn" onclick="processUserSelection(this)"> AnimateOut (-) </button>
		  		<button style="background-color:#f44336;color:#FEFEFE;" class="btn btn-sm" type="button"
		  			name="clearall_graphic_btn" id="clearall_graphic_btn" onclick="processUserSelection(this)"> Clear All (SpaceBar) </button>
			  </div>
			  </div>
	       </div>
	    </div>
       </div>
    </div>
  </div>
<input type="hidden" id="which_keypress" name="which_keypress" value="${session_match.setup.which_key_press}"/>
<input type="hidden" name="selected_broadcaster" id="selected_broadcaster" value="${session_selected_broadcaster}"/>
<input type="hidden" name="selected_second_broadcaster" id="selected_second_broadcaster" value="${session_selected_second_broadcaster}"/>
<input type="hidden" id="matchFileTimeStamp" name="matchFileTimeStamp" value="${session_match.setup.matchFileTimeStamp}"></input>
</form:form>
</body>
</html>