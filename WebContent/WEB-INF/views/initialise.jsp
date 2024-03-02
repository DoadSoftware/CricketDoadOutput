<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>

  <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
  <title>Initialise Screen</title>

  <script type="text/javascript" src="<c:url value="/webjars/jquery/3.6.0/jquery.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/webjars/bootstrap/5.1.3/js/bootstrap.min.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/resources/javascript/index.js"/>"></script>
  
  <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css"/>"/>  
  <link href="<c:url value="/webjars/font-awesome/6.0.0/css/all.css"/>" rel="stylesheet">
		
</head>
<body>
<form:form name="initialise_form" method="POST" action="output" enctype="multipart/form-data">
<div class="content py-5" style="background-color: #EAE8FF; color: #2E008B">
  <div class="container">
	<div class="row">
	 <div class="col-md-8 offset-md-2">
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
           <div class="card-header">
             <h3 class="mb-0">Initialise</h3>
           </div>
          <div class="card-body">
            <div id="initialise_div" style="display:none;">
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="select_configuration_file" class="col-sm-4 col-form-label text-left">Select Configuration </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="select_configuration_file" name="select_configuration_file" 
			      		class="brower-default custom-select custom-select-sm" 
			      		onchange="processCricketProcedures('GET-CONFIG-DATA')">
			          	<option value=""></option>
						<c:forEach items = "${configuration_files}" var = "config">
				          	<option value="${config.name}">${config.name}</option>
						</c:forEach>
			      </select>
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="configuration_file_name" class="col-sm-4 col-form-label text-left">Configuration File Name </label>
			    <div class="col-sm-6 col-md-6">
		             <input type="text" id="configuration_file_name" name="configuration_file_name"
		             	class="form-control form-control-sm floatlabel"></input>
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="select_cricket_matches" class="col-sm-4 col-form-label text-left">Select Cricket Match </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="select_cricket_matches" name="select_cricket_matches" 
			      		class="brower-default custom-select custom-select-sm">
						<c:forEach items = "${match_files}" var = "match">
				          	<option value="${match.name}">${match.name}</option>
						</c:forEach>
			      </select>
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="select_broadcaster" class="col-sm-4 col-form-label text-left">Select Broadcaster </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="select_broadcaster" name="select_broadcaster" class="browser-default custom-select custom-select-sm">
			      		<option value="ISPL">ISPL</option>
			      		<option value="ICC-U19-2023">ICC Under-19 WC 2023</option>
			      </select>
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			    <label for="select_second_broadcaster" class="col-sm-4 col-form-label text-left">Select 2nd Broadcaster </label>
			    <div class="col-sm-6 col-md-6">
			      <select id="select_second_broadcaster" name="select_second_broadcaster" 
			      		class="browser-default custom-select custom-select-sm" onchange="processUserSelection(this)">
			      		<option value="quidich">Quidich</option>
			      </select>
			    </div>
			  </div>
			  <div class="form-group row row-bottom-margin ml-2" style="margin-bottom:5px;">
			  	<label for="previewOnOrOff" class="col-sm-4 col-form-label text-left">PREVIEW</label>	
			  	<div class="col-sm-6 col-md-6">
				  	<select id="previewOnOrOff" name="previewOnOrOff" 
				      		class="browser-default custom-select custom-select-sm">
			          <option value="WITH_PREVIEW">PREVIEW</option>
			          <option value="WITHOUT_PREVIEW">NO PREVIEW</option>
				     </select>
			  	</div>	
			  </div>
			  
			<div class="row">
			<table class="table table-bordered table-responsive">
			  <tbody>
			  	<tr>
			      <td>
				    <label for="qtIPAddress" class="col-sm-4 col-form-label text-left">QT IP</label>				    
		             <input type="text" id="qtIPAddress" name="qtIPAddress" value="${session_configuration.qtIpAddress}"
		             	class="form-control form-control-sm floatlabel"></input>
			      </td>
			      <td>
				    <label for="qtPortNumber" class="col-sm-4 col-form-label text-left">QT Port</label>				    
		             <input type="text" id="qtPortNumber" name="qtPortNumber" value="${session_configuration.qtPortNumber}"
		             	class="form-control form-control-sm floatlabel"></input>
			      </td>
			    </tr>
			    <tr>
			      <td>
				    <label for="vizIPAddress" class="col-sm-4 col-form-label text-left">1st IP</label>				    
		             <input type="text" id="vizIPAddress" name="vizIPAddress" value="${session_configuration.primaryIpAddress}"
		             	class="form-control form-control-sm floatlabel"></input>
			      </td>
			      <td>
				    <label for="vizPortNumber" class="col-sm-4 col-form-label text-left">1st Port</label>				    
		             <input type="text" id="vizPortNumber" name="vizPortNumber" value="${session_configuration.primaryPortNumber}"
		             	class="form-control form-control-sm floatlabel"></input>
			      </td>
			      <td>
				    <label for="primaryVariousOptions" class="col-sm-4 col-form-label text-left">Options</label>			    
				      <select id="primaryVariousOptions" name="primaryVariousOptions" 
				      		class="browser-default custom-select custom-select-sm">
				          <option value="FULL_FRAMER_OVERLAYS">FFs & Overlays both</option>
				          <option value="OVERLAYS">Overlays Only</option>
				      </select>
			      </td>
			      <td>
				    <label for="vizLanguage" class="col-sm-4 col-form-label text-left">1st Language</label>			    
				      <select id="vizLanguage" name="vizLanguage" 
				      		class="browser-default custom-select custom-select-sm">
				          <option value="english">English</option>
				          <option value="hindi">Hindi</option>
				          <option value="telugu">Telugu</option>
				          <option value="tamil">Tamil</option>
				      </select>
			      </td>
			    </tr>
			    <tr>
			      <td>
				    <label for="vizSecondaryIPAddress" class="col-sm-4 col-form-label text-left">2nd IP</label>				    
		             <input type="text" id="vizSecondaryIPAddress" name="vizSecondaryIPAddress" value="${session_configuration.secondaryIpAddress}"
		             	class="form-control form-control-sm floatlabel"></input>
			      </td>
			      <td>
				    <label for="vizSecondaryPortNumber" class="col-sm-4 col-form-label text-left">2nd Port</label>				    
		             <input type="text" id="vizSecondaryPortNumber" name="vizSecondaryPortNumber" value="${session_configuration.secondaryPortNumber}"
		             	class="form-control form-control-sm floatlabel"></input>
			      </td>
			      <td>
				    <label for="vizSecondaryScene" class="col-sm-4 col-form-label text-left">2nd Scene</label>				    
		             <input type="text" id="vizSecondaryScene" name="vizSecondaryScene" value="${session_configuration.secondaryScene}"
		             	class="form-control form-control-sm floatlabel"></input>
			      </td>
			      <td>
				    <label for="vizSecondaryLanguage" class="col-sm-4 col-form-label text-left">2nd Language</label>			    
				      <select id="vizSecondaryLanguage" name="vizSecondaryLanguage" 
				      		class="browser-default custom-select custom-select-sm">
				          <option value="english">English</option>
				          <option value="hindi">Hindi</option>
				          <option value="telugu">Telugu</option>
				          <option value="tamil">Tamil</option>
				      </select>
			      </td>
			    </tr>
			    <tr>
			      <td>
				    <label for="vizTertiaryIPAddress" class="col-sm-4 col-form-label text-left">3rd IP</label>				    
		             <input type="text" id="vizTertiaryIPAddress" name="vizTertiaryIPAddress" value="${session_configuration.tertiaryIpAddress}"
		             	class="form-control form-control-sm floatlabel"></input>
			      </td>
			      <td>
				    <label for="vizTertiaryPortNumber" class="col-sm-4 col-form-label text-left">3rd Port</label>				    
		             <input type="text" id="vizTertiaryPortNumber" name="vizTertiaryPortNumber" value="${session_configuration.tertiaryPortNumber}"
		             	class="form-control form-control-sm floatlabel"></input>
			      </td>
			      <td>
				    <label for="vizTertiaryScene" class="col-sm-4 col-form-label text-left">3rd Scene</label>				    
		             <input type="text" id="vizTertiaryScene" name="vizTertiaryScene" value="${session_configuration.tertiaryScene}"
		             	class="form-control form-control-sm floatlabel"></input>
			      </td>
			      <td>
				    <label for="vizTertiaryLanguage" class="col-sm-4 col-form-label text-left">3rd Language</label>				    
				      <select id="vizTertiaryLanguage" name="vizTertiaryLanguage" 
				      		class="browser-default custom-select custom-select-sm">
				          <option value="english">English</option>
				          <option value="hindi">Hindi</option>
				          <option value="telugu">Telugu</option>
				          <option value="tamil">Tamil</option>
				      </select>
			      </td>
			    </tr>
			  </tbody>
		    </table>
		    </div>		  
		    <button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
		  		name="load_scene_btn" id="load_scene_btn" onclick="processUserSelection(this)"> Load Scene</button>
	       </div>
	    </div>
       </div>
    </div>
  </div>
</div>
</form:form>
</body>
</html>