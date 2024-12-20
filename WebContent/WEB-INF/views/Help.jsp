<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Help</title>

   <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css"/>"/>
    <!-- Font Awesome CSS -->
    <link href="<c:url value="/webjars/font-awesome/6.0.0/css/all.css"/>" rel="stylesheet">
    <!-- jQuery -->
    <script type="text/javascript" src="<c:url value="/webjars/jquery/3.6.0/jquery.min.js"/>"></script>
    <!-- Bootstrap JS -->
    <script type="text/javascript" src="<c:url value="/webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js"/>"></script>
    <!-- HTML2Canvas Library -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js"></script>
    <!-- Custom JavaScript -->
    <script type="text/javascript" src="<c:url value="/resources/javascript/index.js"/>"></script>
    <!-- Custom CSS -->
    <link rel="stylesheet" href="<c:url value="/resources/css/index.css"/>">
    <style type="text/css">
    	.nav-link {
            font-weight: bold;
        } .screenshot-section {
            padding: 20px;
            border: 1px solid #ccc;
            margin: 20px;
        }
    </style>
</head>
<body>
    <div class="content py-5" style="background-color: #EAE8FF; color: #2E008B">    
        <div class="container">
            <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
                 <li class="nav-item" role="presentation">
 <button class="nav-link active" id="pills-function-keys-tab" data-bs-toggle="pill" data-bs-target="#pills-function-keys" 
                            type="button" role="tab" aria-controls="pills-function-keys" aria-selected="true">INFOBAR</button>
                </li>
                <li class="nav-item" role="presentation">
					<button class="nav-link" id="pills-letters-tab" data-bs-toggle="pill" data-bs-target="#pills-letters" 
                            type="button" role="tab" aria-controls="pills-letters" aria-selected="false">FULL FRAMES</button>
                </li>
                <li class="nav-item" role="presentation">
					<button class="nav-link" id="pills-control-tab" data-bs-toggle="pill" data-bs-target="#pills-control" 
                            type="button" role="tab" aria-controls="pills-control" aria-selected="false">LOWER THIRD</button>
                </li>
                <li class="nav-item" role="presentation">
					<button class="nav-link" id="pills-mini-keys-tab" data-bs-toggle="pill" data-bs-target="#pills-mini-keys" 
                            type="button" role="tab" aria-controls="pills-mini-keys" aria-selected="false">MINI'S</button>
                </li>
                <li class="nav-item" role="presentation">
					<button class="nav-link" id="pills-bug-tab" data-bs-toggle="pill" data-bs-target="#pills-bug" 
                            type="button" role="tab" aria-controls="pills-bug" aria-selected="false">BUGS</button>
                </li>
                <li class="nav-item" role="presentation">
					<button class="nav-link" id="pills-lof-keys-tab" data-bs-toggle="pill" data-bs-target="#pills-lof-keys" 
                            type="button" role="tab" aria-controls="pills-lof-keys" aria-selected="false">LOF</button>
                </li>
                <li class="nav-item" role="presentation">
					<button class="nav-link" id="pills-popup-keys-tab" data-bs-toggle="pill" data-bs-target="#pills-popup-keys" 
                            type="button" role="tab" aria-controls="pills-popup-keys" aria-selected="false">POP UP'S</button>
                </li>
            </ul>
            <div class="tab-content" id="pills-tabContent">
                <div class="tab-pane fade show active" id="pills-function-keys" role="tabpanel" aria-labelledby="pills-function-keys-tab">
            		 <button class="screenshot-button" onclick="takeScreenshot('infoTable', 'INFOBAR')">Take Screenshot</button>
                  <br><br>
	             <div class="screenshot-section" id="infoTable">       
	            <table class="table table-bordered">
	            <thead >
	                <tr>
	                    <th class="table-header" colspan="12">INFOBAR</th>
	                </tr>
	            </thead>
	            <tbody>
	            	 <tr>
	                	<td class="table-cell-black">INFOBAR IN</td><td class="table-cell-blue">F12</td>
	                	<td class="table-cell-black">INFOBAR IDENT</td><td class="table-cell-blue">CTRL+F12</td>
	                	<td class="table-cell-black">INFOBAR OUT</td><td class="table-cell-blue">-</td>
	                	<td class="table-cell-black">INFOBAR IDENT OUT</td><td class="table-cell-blue">=</td>
	                	<td class="table-cell-black">INFOBAR IDENT DATA</td><td class="table-cell-blue">Shift+F12</td>
	                	<td class="table-cell-black">BOTTOM RIGHT SECTION</td><td class="table-cell-blue">ALT+7</td>
	                	<td class="table-cell-black">RIGHT SECTION, RIGHT TOP, TICKER TIMELINE</td><td class="table-cell-blue">ALT+8</td>
	                </tr>
	                <tr>
	                	<td class="table-cell-black">FREE TEXT INFO DB</td><td class="table-cell-blue">ALT+9</td>
	                	<td class="table-cell-black">INFOBAR_SPONSOR</td><td class="table-cell-blue">]</td>
	                	<td class="table-cell-black">HAT-TRICK DIRECTOR</td><td class="table-cell-blue">;</td>
	                	<td class="table-cell-black">TICKER PUSH OUT</td><td class="table-cell-blue">PageDown/ARROW DOWN</td>
	                	<td class="table-cell-black">TICKER PUSH IN</td><td class="table-cell-blue">PageUp/ARROW UP</td>
	                	<td class="table-cell-black">CHALLENGE RUNS</td><td class="table-cell-blue">ALT+C</td>
	                	<td class="table-cell-black">CHALLENGE RUNS OUT</td><td class="table-cell-blue">CTRL+=</td>
	                </tr>
	                <tr>
	                	<td class="table-cell-black">FOUR DIRECTOR</td><td class="table-cell-blue">F</td>
	                	<td class="table-cell-black">FREE HIT DIRECTOR</td><td class="table-cell-blue">I</td>
	                	<td class="table-cell-black">SIX DIRECTOR</td><td class="table-cell-blue">S</td>
	                	<td class="table-cell-black">POINTS TABLE</td><td class="table-cell-blue">CTRL+S</td>
	                	<td class="table-cell-black">WICKET DIRECTOR</td><td class="table-cell-blue">W</td>
	                	<td class="table-cell-black">NINE DIRECTOR</td><td class="table-cell-blue">0</td>
	                	<td class="table-cell-black">POWER PLAY IN/OUT</td><td class="table-cell-blue">ALT+E</td>
	                </tr>
	                <tr>
	                	<td class="table-cell-black">TICKER SHRINK</td><td class="table-cell-blue">ALT+F</td>
	                	<td class="table-cell-black">COMMENTATOR</td><td class="table-cell-blue">ALT+0</td>
	                	<td class="table-cell-black">BAT PROFILE</td><td class="table-cell-blue">ALT+3</td>
	                	<td class="table-cell-black">BOWL PROFILE</td><td class="table-cell-blue">ALT+4</td>
	                	<td class="table-cell-black">LAST X BALLS</td><td class="table-cell-blue">ALT+5</td>
	                	<td class="table-cell-black">BAT + SPONSOR</td><td class="table-cell-blue">ALT+6</td>
	                </tr>
	                <tr>
	                    <td class="table-cell-black">AnimateOut Infobar-Right</td><td class="table-cell-blue">CTRL+0</td>
	                	<td class="table-cell-black">BONUS</td><td class="table-cell-blue">CTRL+1</td>
	                	<td class="table-cell-black">POWERPLAY IN</td><td class="table-cell-blue">CTRL+2</td>
	                	<td class="table-cell-black">POWERPLAY OUT</td><td class="table-cell-blue">CTRL+3</td>
	                	<td class="table-cell-black">FREE TEXT USING INPUT BOX</td><td class="table-cell-blue">CTRL+4</td>
	                	<td class="table-cell-black">RIGHT TOP INFOBAR OUT</td><td class="table-cell-blue">CTRL+-</td>
	                	
	                </tr>
	                    </tbody>
	            	  </table>
	            	</div>
           		</div>
           		 <div class="tab-pane fade" id="pills-letters" role="tabpanel" aria-labelledby="pills-letters-tab">
				      <button class="screenshot-button" onclick="takeScreenshot('infoTable1', 'FULLFRAME')">Take Screenshot</button>				      
                  		<br><br> 
                  	<div class="screenshot-section" id="infoTable1">  	
		            <table class="table table-bordered">
		            <thead >
		                <tr>
		                    <th class="table-header" colspan="14">FULL FRAME</th>
		                </tr>
		            </thead>
		            <tbody>
		            	<tr>
		            		<td class="table-cell-black">MATCH IDENT</td><td class="table-cell-blue">M</td>
			                <td class="table-cell-black">MATCH PROMO</td><td class="table-cell-blue">CTRL+M</td>
			                <td class="table-cell-black">TEAMS WITH PHOTOS</td><td class="table-cell-blue">SHIFT+T</td>
			                <td class="table-cell-black">DOUBLE TEAMS</td><td class="table-cell-blue">CTRL+F7</td>
			                <td class="table-cell-black">PLAYER PROFILE BAT</td><td class="table-cell-blue">CTRL+D</td>
			                <td class="table-cell-black">PLAYER PROFILE BOWL</td><td class="table-cell-blue">CTRL+E</td>
			                <td class="table-cell-black">DOUBLE MATCH IDENT</td><td class="table-cell-blue">CTRL+SHIFT+D</td>
		               </tr>
		               <tr>
		               		<td class="table-cell-black">BAT THIS SERIES</td><td class="table-cell-blue">SHIFT+P</td>
		            		<td class="table-cell-black">BOWL THIS SERIES</td><td class="table-cell-blue">SHIFT+Q</td>
		            		<td class="table-cell-black">BATTING CARD</td><td class="table-cell-blue">F1</td>
		            		<td class="table-cell-black">PHOTO SCORECARD</td><td class="table-cell-blue">CTRL+F1</td>
			                <td class="table-cell-black">BOWLING CARD</td><td class="table-cell-blue">F2</td>
			                <td class="table-cell-black">MATCH SUMMARY</td><td class="table-cell-blue">CTRL+F11</td>
			                <td class="table-cell-black">ALL PARTNERSHIP</td><td class="table-cell-blue">F4</td>
		               </tr>
		                <tr>
		                	<td class="table-cell-black">CURR PARTNERSHIP</td><td class="table-cell-blue">SHIFT+K</td>
		                	<td class="table-cell-black">MANHATTAN</td><td class="table-cell-blue">CTRL+F10</td>
		            		<td class="table-cell-black">DOUBLE MANHATTAN</td><td class="table-cell-blue">ALT+F11</td>
		            		<td class="table-cell-black">WORMS</td><td class="table-cell-blue">SHIFT+F10</td>
		            		<td class="table-cell-black">TARGET</td><td class="table-cell-blue">SHIFT+D</td>
		            		<td class="table-cell-black">FOW</td><td class="table-cell-blue">TO BE DEVELOP</td>
		            		<td class="table-cell-black">POINTS TABLE</td><td class="table-cell-blue">P</td>
		               </tr>
		               <tr>
		               		<td class="table-cell-black">POINTS TABLE GROUP WISE</td><td class="table-cell-blue">CTRL+P</td>
		               		<td class="table-cell-black">PREV MATCH SUMMARY</td><td class="table-cell-blue">SHIFT+F11</td>
		               		<td class="table-cell-black">MOST RUNS</td><td class="table-cell-blue">Z</td>
		               		<td class="table-cell-black">MOST WICKETS</td><td class="table-cell-blue">X</td>
		               		<td class="table-cell-black">MOST FOURS</td><td class="table-cell-blue">F</td>
		               		<td class="table-cell-black">MOST SIXES</td><td class="table-cell-blue">V</td>
		               		<td class="table-cell-black">BAT MILESTONE</td><td class="table-cell-blue">ALT+M</td>
		               </tr>
		               <tr>
		               		<td class="table-cell-black">BOWL MILESTONE</td><td class="table-cell-blue">ALT+N</td>
		               		<td class="table-cell-black">NEXT TO BAT</td><td class="table-cell-blue">SHIFT+J</td>
		               		<td class="table-cell-black">BATSMAN v ALL BOWLERS</td><td class="table-cell-blue">CTRL+SHIFT+E</td>
			               	<td class="table-cell-black">BOWLER v ALL BATTERS</td><td class="table-cell-blue">CTRL+SHIFT+F</td>
			               	<td class="table-cell-black">POTT</td><td class="table-cell-blue">R</td>
			               	<td class="table-cell-black">BATTING BY POSITION</td><td class="table-cell-blue">ALT+G</td>
			                <td class="table-cell-black">BATTING BY YEARS</td><td class="table-cell-blue">ALT+H</td>
		               </tr>
		                <tr>
			               <td class="table-cell-black">TODAY'S MATCH</td><td class="table-cell-blue">ALT+J</td>
			               <td class="table-cell-black">BATTING BY COUNTRY</td><td class="table-cell-blue">ALT+V</td>
			               <td class="table-cell-black">SQUAD</td><td class="table-cell-blue">ALT+Z</td>
			               <td class="table-cell-black">FOW</td><td class="table-cell-blue">ALT+F3</td>
			               <td class="table-cell-black">RICHIE'S CAPTION</td><td class="table-cell-blue">ALT+F5</td>
			               <td class="table-cell-black">BATTING BY VENUE IN A COUNTRY</td><td class="table-cell-blue">ALT+L</td>
			               <td class="table-cell-black">SINGLE TEAM (CAREER)</td><td class="table-cell-blue">ALT+F9</td>
		               </tr>
		                <tr>
			               <td class="table-cell-black">SINGLE TEAM (THIS SERIES)</td><td class="table-cell-blue">ALT+F10</td>
			               <td class="table-cell-black">BOWLING BY YEARS</td><td class="table-cell-blue">SHIFT+M</td>
			               <td class="table-cell-black">WICKETS TAKEN IN AN INNINGS</td><td class="table-cell-blue">SHIFT+N</td>
			               <td class="table-cell-black">BAT PROFILE (THIS SERIES)</td><td class="table-cell-blue">SHIFT+P</td>
			               <td class="table-cell-black">BATTING BARS</td><td class="table-cell-blue">ALT+K</td>
			               <td class="table-cell-black">ECONOMY BARS</td><td class="table-cell-blue">SHIFT+G</td>
			               <td class="table-cell-black">BOWLING BARS</td><td class="table-cell-blue">SHIFT+H</td>
		               </tr>
		               
		                <tr>
			               <td class="table-cell-black">IMPACT PLAYER</td><td class="table-cell-blue">SHIFT+I</td>
			               <td class="table-cell-black">BOWLING BY COUNTRY</td><td class="table-cell-blue">SHIFT+V</td>
			               <td class="table-cell-black">BOWLER BEST FIGURES(THIS SERIES)</td><td class="table-cell-blue">SHIFT+X</td>
			               <td class="table-cell-black">BATSMAN HIGHEST SCORE(THIS SERIES)</td><td class="table-cell-blue">SHIFT+Z</td>
			               <td class="table-cell-black">TAPE BALL LEADERBOARD</td><td class="table-cell-blue">CTRL+C</td>
			               <td class="table-cell-black">BOWLING BY VENUE IN A COUNTRY</td><td class="table-cell-blue">CTRL+L</td>
			               <td class="table-cell-black">BEST BOWLING FIG</td><td class="table-cell-blue">CTRL+X</td>
		               </tr>
		               
		                <tr>
			               <td class="table-cell-black">HIGHEST SCORE</td><td class="table-cell-blue">CTRL+Z</td>
			               <td class="table-cell-black">BATSMAN CAREER MANHATTAN</td><td class="table-cell-blue">ALT + SHIFT+M</td>
			               <td class="table-cell-black">HALF FRAME WAGON WHEEL</td><td class="table-cell-blue">ALT + SHIFT+W</td>
			               <td class="table-cell-black">FULL FRAME WAGON WHEEL</td><td class="table-cell-blue">ALT + SHIFT+Y</td>
			             </tr>
		                </tbody>
		                </table>
		              </div>
		        </div>
		        <div class="tab-pane fade" id="pills-control" role="tabpanel" aria-labelledby="pills-control-tab">
		        	 <button class="screenshot-button" onclick="takeScreenshot('infoTable2', 'LOWERTHIRDS')">Take Screenshot</button>
                  		<br><br>
                  	<div class="screenshot-section" id="infoTable2">
				        	 <table class="table table-bordered">
				            <thead >
				                <tr>
				                    <th class="table-header" colspan="15">LOWER THIRDS</th>
				                </tr>
				            </thead>
				            <tbody>
				            	<tr>
				            		<td class="table-cell-black">HOME TEAM NAMESUPER</td><td class="table-cell-blue">F8</td>
				            		<td class="table-cell-black">AWAY TEAM NAMESUPER</td><td class="table-cell-blue">ALT+F8</td>	
				            		<td class="table-cell-black">NAMESUPER DB 2 LINE</td><td class="table-cell-blue">F10</td>
				            		<td class="table-cell-black">NAMESUPER DB 1 LINE</td><td class="table-cell-blue">J</td>
					                <td class="table-cell-black">BATING STYLE</td><td class="table-cell-blue">CTRL+F5</td>
					                <td class="table-cell-black">BOWLING STYLE</td><td class="table-cell-blue">CTRL+F9</td>
					                <td class="table-cell-black">BAT THIS MATCH</td><td class="table-cell-blue">F5</td>
				            	</tr>
				                <tr>
					               <td class="table-cell-black">BOWL THIS MATCH</td><td class="table-cell-blue">F9</td>
					               <td class="table-cell-black">BAT THIS SERIES</td><td class="table-cell-blue">CTRL+S</td>
					               <td class="table-cell-black">BOWL THIS SERIES</td><td class="table-cell-blue">CTRL+F</td>
					               <td class="table-cell-black">BAT PROFILE</td><td class="table-cell-blue">F7</td>
					               <td class="table-cell-black">BOWL PROFILE</td><td class="table-cell-blue">F11</td>
					               <td class="table-cell-black">HOW OUT</td><td class="table-cell-blue">F6</td>
					               <td class="table-cell-black">QUICK HOWOUT</td><td class="table-cell-blue">CTRL+F6</td>
				               </tr>
				                <tr>
					               <td class="table-cell-black">HOWOUT W/O FIELDER</td><td class="table-cell-blue">SHIFT+F6</td>
					               <td class="table-cell-black">BAT 0 1 2</td><td class="table-cell-blue">SHIFT+F5</td>
					               <td class="table-cell-black">TEAM 0,1,2</td><td class="table-cell-blue">ALT+F12</td>
					               <td class="table-cell-black">BOWLER 0,1,2</td><td class="table-cell-blue">SHIFT+F9</td>
					               <td class="table-cell-black">PHASE WISE</td><td class="table-cell-blue">CTRL+H</td>
					               <td class="table-cell-black">LINE UP</td><td class="table-cell-blue">CTRL+SHIFT+O</td>
				                   <td class="table-cell-black">NEXT TO BAT</td><td class="table-cell-blue">CTRL+SHIFT+B</td>
				               </tr>
				               <tr>
					               <td class="table-cell-black">FOW</td><td class="table-cell-blue">SHIFT+F3</td>
					               <td class="table-cell-black">LT PROJECTED SCORE</td><td class="table-cell-blue">CTRL+A</td>
					               <td class="table-cell-black">COMPARISON</td><td class="table-cell-blue">CTRL+F3</td>
				               	   <td class="table-cell-black">TARGET</td><td class="table-cell-blue">D</td>
				               	   <td class="table-cell-black">EQUATION</td><td class="table-cell-blue">E</td>
				               	   <td class="table-cell-black">Batter This match + Career LT</td><td class="table-cell-blue">SHIFT+Y</td>
					               <td class="table-cell-black">PHOTO BAT STYLE</td><td class="table-cell-blue">SHIFT+F7</td>
				               </tr>
				               <tr>
				               	   <td class="table-cell-black">SUMMARY DAY BY DAY</td><td class="table-cell-blue">ALT+I</td>
					               <td class="table-cell-black">CURR PARTNERSHIP(LT)</td><td class="table-cell-blue">ALT+O</td>
					               <td class="table-cell-black">POTT</td><td class="table-cell-blue">ALT+Q</td>
					               <td class="table-cell-black">STAFF HOME</td><td class="table-cell-blue">ALT+A</td>
					               <td class="table-cell-black">DLS PAR SCORE/td><td class="table-cell-blue">ALT+D</td>
					               <td class="table-cell-black">TODAY'S MATCH</td><td class="table-cell-blue">ALT+J</td>
					               <td class="table-cell-black">STAFF AWAY</td><td class="table-cell-blue">ALT+S</td>
				               </tr>
				                <tr>
					               <td class="table-cell-black">SESSION</td><td class="table-cell-blue">ALT+W</td>
					               <td class="table-cell-black">BOWLER MULTI CAREER</td><td class="table-cell-blue">ALT+X</td>
					               <td class="table-cell-black">BATTER MULTI CAREER</td><td class="table-cell-blue">ALT+Y</td>
					               <td class="table-cell-black">HOWOUT BOTH</td><td class="table-cell-blue">ALT+F6</td>
					               <td class="table-cell-black">POWERPLAY DESC</td><td class="table-cell-blue">CTRL+G</td>
				               </tr>
				                <tr>
					               <td class="table-cell-black">BAT THIS MATCH BOTH</td><td class="table-cell-blue">SHIFT+A</td>
					               <td class="table-cell-black">MATCH SUMMARY LT</td><td class="table-cell-blue">SHIFT+B</td>
					               <td class="table-cell-black">EXTRAS</td><td class="table-cell-blue">SHIFT+E</td>
					               <td class="table-cell-black">BOWL THIS MATCH BOTH</td><td class="table-cell-blue">SHIFT+R</td>
					               <td class="table-cell-black">THIS SESSION</td><td class="table-cell-blue">SHIFT+U</td>
					               <td class="table-cell-black">MATCHES AND CATCHES LT</td><td class="table-cell-blue">SHIFT+W</td>
					               <td class="table-cell-black">SPEEDS - FASTEST SLOWEST AVERAGE</td><td class="table-cell-blue">SHIFT+F8</td>
				               </tr>
				                <tr>
					               <td class="table-cell-black">INNINGS BUILDER LT</td><td class="table-cell-blue">CTRL+I</td>
					               <td class="table-cell-black">ALL SESSION</td><td class="table-cell-blue">CTRL+J</td>
					               <td class="table-cell-black">TALL ROUNDER LT - BATTING TEAM</td><td class="table-cell-blue">CTRL+N</td>
					               <td class="table-cell-black">PLAYER v PLAYER LT</td><td class="table-cell-blue">Control + Shift + X</td>
					               <td class="table-cell-black">TEAM BOUNDARIES</td><td class="table-cell-blue">CTRL+Q</td>
					               <td class="table-cell-black">ALL ROUNDER LT - BOWLING TEAM</td><td class="table-cell-blue">CTRL+R</td>
					               <td class="table-cell-black">ALL BOWLER'S SPEED COMPARISON LT</td><td class="table-cell-blue">CTRL+T</td>
				               </tr>
				                <tr>
				                   <td class="table-cell-black">POINTERS</td><td class="table-cell-blue">ALT+SHIFT+O</td>
					               <td class="table-cell-black">BOWLING CARD (MAX 3 BOWLERS)</td><td class="table-cell-blue">CTRL+F2</td>
					               <td class="table-cell-black">PHOTO BOWL STYLE</td><td class="table-cell-blue">CTRL+F4</td>
					               <td class="table-cell-black">SPEEDS THIS OVER LT</td><td class="table-cell-blue">CTRL+F8</td>
					               <td class="table-cell-black">BOWLER V LHB/RHB LT</td><td class="table-cell-blue">CTRL+U</td>
					               <td class="table-cell-black">LAST FEW OVERS LT</td><td class="table-cell-blue">Shift + G</td>
					               <td class="table-cell-black">COMMENTATOR LT</td><td class="table-cell-blue">CTRL + SHIFT+C</td>
				               </tr>
					             <tr>
					               <td class="table-cell-black">RUNS AND BALLS SPENT</td><td class="table-cell-blue">ALT + SHIFT+B</td>
					               <td class="table-cell-black">DUCKS/CAPTAIN in bengal</td><td class="table-cell-blue">ALT + SHIFT+C</td>
					               <td class="table-cell-black">TIME SINCE RUNS</td><td class="table-cell-blue">ALT + SHIFT+D</td>
					               <td class="table-cell-black">TIME SINCE BOUNDARIES</td><td class="table-cell-blue">ALT + SHIFT+E</td>
					               <td class="table-cell-black">TIME SINCE MAIDEN</td><td class="table-cell-blue">ALT + SHIFT+F</td>
					               <td class="table-cell-black">TIME SINCE CONSECUTIVE RUNS</td><td class="table-cell-blue">ALT + SHIFT+G</td>
					               <td class="table-cell-black">TIME SINCE FIRST RUN SCORED</td><td class="table-cell-blue">ALT + SHIFT+H</td>
					             </tr>
					             <tr>
					               <td class="table-cell-black">BEST BATTING CAREER</td><td class="table-cell-blue">ALT + SHIFT+I</td>
					               <td class="table-cell-black">BEST BOWLING CAREER</td><td class="table-cell-blue">ALT + SHIFT+J</td>
					               <td class="table-cell-black">OVER RATE PER HOUR OF DAY - WITH VARIATIONS LT</td><td class="table-cell-blue">ALT + SHIFT+K</td>
					               <td class="table-cell-black">PLAYER RESULT LT</td><td class="table-cell-blue">ALT + SHIFT+N</td>
					               <td class="table-cell-black">SINGLE SHOT ANALYSIS LT</td><td class="table-cell-blue">ALT + SHIFT+Q</td>
					               <td class="table-cell-black">CAREER'S 1st INNINGS v 2nd INNINGS - BATTER</td><td class="table-cell-blue">ALT + SHIFT+S</td>
					               <td class="table-cell-black">CAREER'S 1st INNINGS v 2nd INNINGS - BOWLER</td><td class="table-cell-blue">ALT + SHIFT+T</td>
					             </tr>
					             <tr>
					               <td class="table-cell-black">SETTING v CHASING LT</td><td class="table-cell-blue">ALT + SHIFT+U</td>
					               <td class="table-cell-black">BOTH INNING 0,1,2 COMPARISON</td><td class="table-cell-blue">ALT + SHIFT+F3</td>
					               <td class="table-cell-black">RICHIES CAPTION LT</td><td class="table-cell-blue">ALT + SHIFT+F5</td>
					               <td class="table-cell-black">BOWLING SPELLS LT</td><td class="table-cell-blue">ALT + SHIFT+F7</td>
					               <td class="table-cell-black">SPEEDS THIS OVER LT</td><td class="table-cell-blue">ALT + SHIFT+T</td>
					               <td class="table-cell-black">BOWLING STYLE</td><td class="table-cell-blue">ALT + SHIFT+T</td>
					             </tr>
					             <tr>
					               <td class="table-cell-black">BOWLER V BATSMAN LT - CURRENT INNINGS</td><td class="table-cell-blue">CTRL + SHIFT+E</td>
					               <td class="table-cell-black">BATSMAN v BOWLER LT - CURRENT INNINGS</td><td class="table-cell-blue">CTRL + SHIFT+F</td>
					               <td class="table-cell-black">BOWLING SPELLS LT</td><td class="table-cell-blue">CTRL + SHIFT+P</td>
					               <td class="table-cell-black">GENERIC</td><td class="table-cell-blue">CTRL + SHIFT+Q</td>
					             </tr>
				                </tbody>
				             </table>
				            </div>
				        </div>
				       <div class="tab-pane fade" id="pills-mini-keys" role="tabpanel" aria-labelledby="pills-mini-keys-tab">
				        	 <button class="screenshot-button" onclick="takeScreenshot('infoTable3', 'MINIS')">Take Screenshot</button>
		                  	<br><br>
		                  	<div class="screenshot-section" id="infoTable3">
				        	<table class="table table-bordered">
					            <thead>
					                <tr>
					                    <th class="table-header" colspan="4">MINI'S</th>
					                </tr>
					            </thead>
					            <tbody>
						            <tr>
						           		<td class="table-cell-black">MINI BATTING CARD</td><td class="table-cell-blue">SHIFT+F1</td>
					                    <td class="table-cell-black">MINI BOWLING CARD</td><td class="table-cell-blue">SHIFT+F2</td>
					                    <td class="table-cell-black">POINTS TABLE</td><td class="table-cell-blue">ALT+F7</td>
							            <td class="table-cell-black">BAT GRIFF</td><td class="table-cell-blue">ALT+F1</td>
							            <td class="table-cell-black">BOWL GRIFF</td><td class="table-cell-blue">ALT+F2</td>
						            </tr>
					            </tbody>
					          </table>
					          </div>
				        </div>
				     <div class="tab-pane fade" id="pills-bug" role="tabpanel" aria-labelledby="pills-bug-tab">
				        	 <button class="screenshot-button" onclick="takeScreenshot('infoTable4', 'BUGS')">Take Screenshot</button>
		                  	<br><br>
		                  	<div class="screenshot-section" id="infoTable4">
				        	<table class="table table-bordered">
		            <thead>
		                <tr>
		                    <th class="table-header" colspan="14">BUGS</th>
		                </tr>
		            </thead>
		            <tbody>
		                <tr><td class="table-cell-black">BOWL FIG</td><td class="table-cell-blue">G</td>
		                	<td class="table-cell-black">HIGHLIGHTS</td><td class="table-cell-blue">H</td>
		                	<td class="table-cell-black">PLAYER OF THE MATCH</td><td class="table-cell-blue">O</td>
		                	<td class="table-cell-black">THIRD UMPIRE</td><td class="table-cell-blue">T</td>
		                	<td class="table-cell-black">BAT SCORE</td><td class="table-cell-blue">Y</td>
		                	<td class="table-cell-black">TOURNAMENT SIX COUNTER</td><td class="table-cell-blue">6</td>
		                	<td class="table-cell-black">BUG 50-50</td><td class="table-cell-blue">.</td>
		                </tr>
		                <tr>
		                	<td class="table-cell-black">WICKET SEQUENCE BOWLER</td><td class="table-cell-blue">ALT+B</td>
			                <td class="table-cell-black">SIX DISTANCE</td><td class="table-cell-blue">SHIFT+C</td>
			                <td class="table-cell-black">WICKET SEQUENCE</td><td class="table-cell-blue">SHIFT+F</td>
			                <td class="table-cell-black">BAT DISMISSAL</td><td class="table-cell-blue">SHIFT+O</td>
			                <td class="table-cell-black">BUG MULTI PARTNERSHIP</td><td class="table-cell-blue">SHIFT+F4</td>
							<td class="table-cell-black">CURR PARTNERSHIP (BUGS)</td><td class="table-cell-blue">CTRL+K</td>
		                	<td class="table-cell-black">POWERPLAY</td><td class="table-cell-blue">CTRL+Y</td>	             
		                </tr>
		            </tbody>
		          </table>
		          </div>
		        </div>
		        <div class="tab-pane fade" id="pills-lof-keys" role="tabpanel" aria-labelledby="pills-lof-keys-tab">
		        	 <button class="screenshot-button" onclick="takeScreenshot('infoTable5', 'LOF')">Take Screenshot</button>
                 	 <br><br>
                 	 <div class="screenshot-section" id="infoTable5">
			        <table class="table table-bordered">
			            <thead>
			                <tr>
			                    <th class="table-header" colspan="4">LOF</th>
			                </tr>
			            </thead>
			            <tbody>
				            <tr>
				           		 <td class="table-cell-black">LOF BATTING CARD</td><td class="table-cell-blue">ALT + SHIFT+F1</td>
			                    <td class="table-cell-black">LOF BOWLING CARD</td><td class="table-cell-blue">ALT + SHIFT+F2</td>
				            </tr>
			            </tbody>
			          </table>
			          </div>
		        </div>
		        <div class="tab-pane fade" id="pills-popup-keys" role="tabpanel" aria-labelledby="pills-popup-keys-tab">
    			<button class="screenshot-button" onclick="takeScreenshot('infoTable6', 'POPUP')">Take Screenshot</button>
                       <br><br>
                 	 <div class="screenshot-section" id="infoTable6">
		        	<table class="table table-bordered">
			            <thead>
			                <tr>
			                    <th class="table-header" colspan="4">POP UP'S</th>
			                </tr>
			            </thead>
			            <tbody>
				            <tr>
				           		 <td class="table-cell-black">BAT POPUP</td><td class="table-cell-blue">CTRL + SHIFT+U</td>
				             	 <td class="table-cell-black">BOWL POPUP</td><td class="table-cell-blue">CTRL + SHIFT+V</td>
				            </tr>
			            </tbody>
			         </table>
			         </div>
		        </div>
            </div>
           </div>
           </div>
     <script>
     function takeScreenshot(tableId, headerText) {
    	    var element = document.getElementById(tableId);
    	    if (!element) {
    	        console.error(`Element with ID "${tableId}" not found.`);
    	        return;
    	    }

    	    html2canvas(element).then(function(canvas) {
    	        var imgData = canvas.toDataURL('image/png');
    	        var link = document.createElement('a');
    	        link.href = imgData;
    	        var fileName = headerText.trim() !== '' ? headerText + '.png' : 'screenshot.png';
                link.download = fileName;
    	        document.body.appendChild(link);
    	        link.click();
    	        document.body.removeChild(link);
    	    }).catch(function(error) {
    	        console.error('Error capturing screenshot:', error);
    	    });
    	}

 </script>

</body>
</html>
