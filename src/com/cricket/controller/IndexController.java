package com.cricket.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.cricket.captions.Animation;
import com.cricket.captions.Caption;
import com.cricket.captions.Scene;
import com.cricket.model.Configuration;
import com.cricket.model.EventFile;
import com.cricket.model.Match;
import com.cricket.model.MatchAllData;
import com.cricket.model.Setup;
import com.cricket.model.Statistics;
import com.cricket.model.Tournament;
import com.cricket.service.CricketService;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@SessionAttributes(value = {"session_configuration"})
public class IndexController 
{
	@Autowired
	CricketService cricketService;
	
	public static MatchAllData session_match;
	public static String expiry_date = "2024-01-31";
	public static String current_date;
	public static long last_match_time_stamp = 0;
	public static Scene this_scene;
	public static Caption this_caption;
	public static Animation this_animation;
	public static List<PrintWriter> print_writers;
	public static boolean show_speed = true;
	public static int whichSide = 1;
	public static int graphicOnScreen = 0;
	
	public static List<MatchAllData> cricket_matches = new ArrayList<MatchAllData>();
	public static List<Tournament> past_tournament_stats = new ArrayList<Tournament>();
	public static List<Statistics> session_statistics = new ArrayList<Statistics>();
	
	@RequestMapping(value = {"/","/initialise"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String initialisePage(ModelMap model, 
		@ModelAttribute("session_configuration") Configuration session_configuration) 
		throws JAXBException, MalformedURLException, IOException 
	{
		
		if(current_date == null || current_date.isEmpty()) {
			current_date = CricketFunctions.getOnlineCurrentDate();
		}

		model.addAttribute("match_files", new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        String name = pathname.getName().toLowerCase();
		        return name.endsWith(".json") && pathname.isFile();
		    }
		}));
		
		model.addAttribute("configuration_files", new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.CONFIGURATIONS_DIRECTORY).listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        String name = pathname.getName().toLowerCase();
		        return name.endsWith(".xml") && pathname.isFile();
		    }
		}));

		return "initialise";
	}

	@RequestMapping(value = {"/output"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String outputPage(ModelMap model,
			@ModelAttribute("session_configuration") Configuration session_configuration,
			@RequestParam(value = "configuration_file_name", required = false, defaultValue = "") String configuration_file_name,
			@RequestParam(value = "select_broadcaster", required = false, defaultValue = "") String select_broadcaster,
			@RequestParam(value = "select_second_broadcaster", required = false, defaultValue = "") String select_second_broadcaster,
			@RequestParam(value = "which_layer", required = false, defaultValue = "") String which_layer,
			@RequestParam(value = "which_scene", required = false, defaultValue = "") String which_scene,
			@RequestParam(value = "select_cricket_matches", required = false, defaultValue = "") String selectedMatch,
			@RequestParam(value = "qtIPAddress", required = false, defaultValue = "") String qtIPAddress,
			@RequestParam(value = "qtPortNumber", required = false, defaultValue = "") int qtPortNumber,
			@RequestParam(value = "qtSceneName", required = false, defaultValue = "") String qtScene,
			@RequestParam(value = "qtLanguage", required = false, defaultValue = "") String qtLanguage,
			@RequestParam(value = "vizIPAddress", required = false, defaultValue = "") String vizIPAddress,
			@RequestParam(value = "vizPortNumber", required = false, defaultValue = "") int vizPortNumber,
			@RequestParam(value = "vizSceneName", required = false, defaultValue = "") String vizScene,
			@RequestParam(value = "vizLanguage", required = false, defaultValue = "") String vizLanguage,
			@RequestParam(value = "vizSecondaryIPAddress", required = false, defaultValue = "") String vizSecondaryIPAddress,
			@RequestParam(value = "vizSecondaryPortNumber", required = false, defaultValue = "") int vizSecondaryPortNumber,
			@RequestParam(value = "vizSecondaryScene", required = false, defaultValue = "") String vizSecondaryScene,
			@RequestParam(value = "vizSecondaryLanguage", required = false, defaultValue = "") String vizSecondaryLanguage,
			@RequestParam(value = "vizTertiaryIPAddress", required = false, defaultValue = "") String vizTertiaryIPAddress,
			@RequestParam(value = "vizTertiaryPortNumber", required = false, defaultValue = "") int vizTertiaryPortNumber,
			@RequestParam(value = "vizTertiaryScene", required = false, defaultValue = "") String vizTertiaryScene,
			@RequestParam(value = "vizTertiaryLanguage", required = false, defaultValue = "") String vizTertiaryLanguage) 
				throws StreamWriteException, DatabindException, IllegalAccessException, InvocationTargetException, 
				JAXBException, IOException, URISyntaxException, ParseException, InterruptedException 
	{
		if(current_date == null || current_date.isEmpty()) {
			
			model.addAttribute("error_message","You must be connected to the internet online");
			return "error";
		
		} else if(new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date).before(new SimpleDateFormat("yyyy-MM-dd").parse(current_date))) {
			
			model.addAttribute("error_message","This software has expired");
			return "error";
			
		}else {

			last_match_time_stamp = new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY + selectedMatch).lastModified();
			whichSide = 1;
			graphicOnScreen = 0;
			
			session_configuration = new Configuration(selectedMatch, select_broadcaster,qtIPAddress, qtPortNumber,qtScene, qtLanguage, vizIPAddress, vizPortNumber, vizScene, 
				vizLanguage,vizSecondaryIPAddress, vizSecondaryPortNumber, vizSecondaryScene, vizSecondaryLanguage,vizTertiaryIPAddress, vizTertiaryPortNumber,
				vizTertiaryScene, vizTertiaryLanguage); 
			
			JAXBContext.newInstance(Configuration.class).createMarshaller().marshal(session_configuration, 
					new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.CONFIGURATIONS_DIRECTORY + configuration_file_name));
			
			print_writers = CricketFunctions.processPrintWriter(session_configuration);
			
			this_scene = new Scene();
			this_animation = new Animation();
			
			switch (select_broadcaster) {
//			case "IPL_2024":
			default:
				this_scene.LoadScene("FULL-FRAMERS", print_writers, session_configuration);
				this_scene.LoadScene("OVERLAYS", print_writers, session_configuration);
				break;
			}
			
			session_match = new MatchAllData();
			
			if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.SETUP_DIRECTORY + selectedMatch).exists()) {
				session_match.setSetup(new ObjectMapper().readValue(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.SETUP_DIRECTORY + 
						selectedMatch), Setup.class));
				session_match.setMatch(new ObjectMapper().readValue(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY + 
						selectedMatch), Match.class));
			}
			if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.EVENT_DIRECTORY + selectedMatch).exists()) {
				session_match.setEventFile(new ObjectMapper().readValue(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.EVENT_DIRECTORY + 
						selectedMatch), EventFile.class));
			}
			
			session_match.getMatch().setMatchFileName(selectedMatch);
			session_match = CricketFunctions.populateMatchVariables(cricketService, 
				CricketFunctions.readOrSaveMatchFile(CricketUtil.READ,CricketUtil.SETUP + "," + 
				CricketUtil.MATCH + "," + CricketUtil.EVENT, session_match));			
			session_match.getSetup().setMatchFileTimeStamp(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
			
			switch (select_broadcaster) {
			case "ICC-U19-2023":

				cricket_matches = CricketFunctions.getTournamentMatches(new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
					@Override
				    public boolean accept(File pathname) {
				        String name = pathname.getName().toLowerCase();
				        return name.endsWith(".json") && pathname.isFile();
				    }
				}), cricketService);
				session_statistics = cricketService.getAllStats();
				past_tournament_stats = CricketFunctions.extractTournamentStats(
					"PAST_MATCHES_DATA",false, cricket_matches, cricketService, session_match, null);
				this_caption = new Caption(print_writers, session_configuration, session_statistics,cricketService.getAllStatsType(), cricket_matches,cricketService);
				break;
			}
			show_speed = true;
			
			model.addAttribute("session_match", session_match);
			model.addAttribute("session_configuration", session_configuration);
			return "output";
		}
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> GetGraphicOption(String whatToProcess) {
		switch (whatToProcess) {
		case "NAMESUPER-GRAPHICS-OPTIONS":
		    return (List<T>) cricketService.getNameSupers();
		case "MATCH_PROMO-GRAPHICS-OPTIONS":
			return (List<T>) CricketFunctions.processAllFixtures(cricketService);
		}
		return null;
	}
	
	@RequestMapping(value = {"/processCricketProcedures"}, method={RequestMethod.GET,RequestMethod.POST})    
	public @ResponseBody String processCricketProcedures(
		@ModelAttribute("session_configuration") Configuration session_configuration,
		@RequestParam(value = "whatToProcess", required = false, defaultValue = "") String whatToProcess,
		@RequestParam(value = "valueToProcess", required = false, defaultValue = "") String valueToProcess) 
					throws Exception 
	{
		boolean all_ok_status = false;
		
		switch (whatToProcess.toUpperCase()) {
		case "GET-CONFIG-DATA":

			session_configuration = (Configuration)JAXBContext.newInstance(Configuration.class).createUnmarshaller().unmarshal(
				new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.CONFIGURATIONS_DIRECTORY + valueToProcess));
			
			return JSONObject.fromObject(session_configuration).toString();
			
		case "RE_READ_DATA":

			cricket_matches = CricketFunctions.getTournamentMatches(new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".json") && pathname.isFile();
			    }
			}), cricketService);
			
			session_statistics = cricketService.getAllStats();
			past_tournament_stats = CricketFunctions.extractTournamentStats("PAST_MATCHES_DATA",false, cricket_matches, cricketService, session_match, null);
			
			this_caption = new Caption(print_writers, session_configuration, session_statistics, cricketService.getAllStatsType(), cricket_matches,cricketService);
			
			session_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(
				CricketUtil.READ,CricketUtil.SETUP, session_match));
			
			return JSONObject.fromObject(session_match).toString();
			
		case "SHOW_SPEED":
			
			if(show_speed == true) {
				show_speed = false;
			}else {
				show_speed = true;
			}
			
			return String.valueOf(show_speed);

		case "READ-MATCH-AND-POPULATE":
			
			if(last_match_time_stamp != new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY 
				+ session_match.getMatch().getMatchFileName()).lastModified()) {
				session_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ,
					CricketUtil.MATCH + "," + CricketUtil.EVENT, session_match));
				last_match_time_stamp = new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY 
						+ session_match.getMatch().getMatchFileName()).lastModified();
			}
			return JSONObject.fromObject(session_match).toString();
			
		default:
			
			if(whatToProcess.contains("GRAPHICS-OPTIONS")) {
				return JSONArray.fromObject(GetGraphicOption(whatToProcess)).toString();
			}
			else if(whatToProcess.contains("POPULATE-GRAPHICS")) {
				//System.out.println("valueToProcess : " + valueToProcess);
				all_ok_status = this_caption.PopulateGraphics(valueToProcess, whichSide, session_match);
				
				return String.valueOf(all_ok_status);
			}
			else if(whatToProcess.contains("ANIMATE-IN-GRAPHICS")) {
				if(whichSide == 1) {
					this_animation.AnimateIn(valueToProcess, print_writers, session_configuration);
					whichSide = 3 - whichSide;
				} else {
					this_animation.ChangeOn(valueToProcess,graphicOnScreen,print_writers, session_configuration);
					TimeUnit.MILLISECONDS.sleep(2000);
					all_ok_status = this_caption.PopulateGraphics(valueToProcess, 1, session_match);
					this_animation.CutBack(valueToProcess, print_writers, session_configuration);
				}
				graphicOnScreen = Integer.valueOf(valueToProcess.split(",")[0]);
			}
			else if(whatToProcess.contains("ANIMATE-OUT")) {
				this_animation.AnimateOut(String.valueOf(graphicOnScreen), print_writers, session_configuration);
				graphicOnScreen = 0;
				whichSide = 1;
			}
			else if(whatToProcess.contains("CLEAR-ALL")) {
				whichSide = 1;
				graphicOnScreen = 0;
				this_animation.ClearAll(print_writers);
			}
			return JSONObject.fromObject(session_match).toString();
		}
	}
	@ModelAttribute("session_configuration")
	public Configuration session_configuration(){
		return new Configuration();
	} 
}