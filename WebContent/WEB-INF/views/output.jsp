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
   <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    
    <!-- Include Select2 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    
    <!-- Include Select2 JS -->
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
  
  <link rel="stylesheet" href="<c:url value="/resources/css/index.css"/>">
  <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css"/>"/>  
  <link href="<c:url value="/webjars/font-awesome/6.0.0/css/all.css"/>" rel="stylesheet">
  
  <script type="text/javascript">
    
  $(document).on("keydown", function(e){
	  
	  if($('#waiting_modal').hasClass('show')) {
		  e.cancelBubble = true;
		  e.stopImmediatePropagation();
    	  e.preventDefault();
		  return false;
	  }
	  
      var evtobj = window.event? event : e;
      
      switch(e.target.tagName.toLowerCase())
      {
      case "input": case "textarea":
    	 break;
      default:
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
			   console.log('whichKey = ' + whichKey);
			   processUserSelectionData('LOGGER_FORM_KEYPRESS',whichKey);
		   }
	      }
	  }); 
   	  setInterval(() => {processCricketProcedures('READ-MATCH-AND-POPULATE');}, 1000);
  </script> 
    
</head>
<body onload="onPageLoadEvent('OUTPUT')">
<div id="waiting_modal" class="modal my_waiting_modal fade bd-example-modal-lg" data-backdrop="static" 
	data-keyboard="false" data-bs-backdrop="static" tabindex="-1">
    <div id="waiting_modal_body" class="modal-dialog modal-sm">
        <div class="modal-content" style="width: 48px">
	    	<h5 style="color:white">Processing...</h5>
	    	<br>
            <span class="fa fa-spinner fa-spin fa-4x" style="color:white"></span>
        </div>
    </div>
</div>
<form:form name="output_form" method="POST" action="output" enctype="multipart/form-data">
<div class="content py-5" style="background-color: #EAE8FF; color: #2E008B;">
  <div class="container">
	<div class="row">
	 <div class="col-md-8 offset-md-2">
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
           <div class="card-header">
             <h3 class="mb-0">Output</h3>
             <h5 align="right">${expiryDate} Days Left</h5>
           </div>
          <div class="card-body">
			<div class="content py-5" style="background-color:#EAE8FF;color:#2E008B;">
				<div class="container">
				  <div id="select_graphic_options_div" style="display:none;">
				  </div>
				  <div id="captions_div" class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
				    <label class="col-sm-4 col-form-label text-left">Match: ${session_match.match.matchFileName} </label>
				    <label class="col-sm-4 col-form-label text-left">Broadcaster: ${session_configuration.broadcaster.replace("_"," ")} </label>
				    <label class="col-sm-4 col-form-label text-left">2nd Broadcaster: ${session_configuration.secondaryBroadcaster.replace("_"," ")} </label>
				     <label id="selected_inning" class="col-sm-4 col-form-label text-left">Selected Inning: </label>
				    <label id="inning1_teamScore_lbl" class="col-sm-4 col-form-label text-left">-</label>
				    <label id="inning2_teamScore_lbl" class="col-sm-4 col-form-label text-left">-</label>
			    	<label id="inning1_battingcard1_lbl" class="col-sm-4 col-form-label text-left">-</label>
			    	<label id="inning1_battingcard2_lbl" class="col-sm-4 col-form-label text-left">-</label>
			      	<label id="inning1_bowlingcard_lbl" class="col-sm-4 col-form-label text-left">-</label>
				  </div>
			  </div>
		  </div>
	     </div>
      </div>
    </div>
   </div>
  </div>
  </div>
<input type="hidden" id="which_keypress" name="which_keypress"/>
<input type="hidden" id="which_inning" name="which_inning" value="${which_inning}"/>
<input type="hidden" name="selected_broadcaster" id="selected_broadcaster" value="${session_configuration.broadcaster}"/>
<input type="hidden" name="selected_second_broadcaster" id="selected_second_broadcaster" value="${session_configuration.secondaryBroadcaster}"/>
<input type="hidden" name="selected_match_max_overs" id="selected_match_max_overs" value="${session_match.setup.maxOvers}"/>
<input type="hidden" id="matchFileTimeStamp" name="matchFileTimeStamp" value="${session_match.setup.matchFileTimeStamp}"></input>
</form:form>
 <script type="text/javascript">
    var helpPageOpened = false, helpWindow = null; 
    document.addEventListener('keydown', function(event) {
        if (event.ctrlKey && event.shiftKey && event.key === 'H') {
            event.preventDefault();           
            var helpPageUrl = '<c:url value="/Help"/>';
            if (!helpPageOpened || (helpWindow && helpWindow.closed)) {
                helpWindow = window.open(helpPageUrl, '_blank'); 
                helpPageOpened = true; 
                if (helpWindow) {
                    helpWindow.onbeforeunload = function() {
                        helpPageOpened = false; 
                    };
                }
            } else {
                helpWindow.focus();
                helpWindow.location.reload();
            }
        }
    });
</script>

</body>
</html>