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
import org.springframework.http.MediaType;
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
import com.cricket.captions.Constants;
import com.cricket.captions.FullFramesGfx;
import com.cricket.captions.LowerThirdGfx;
import com.cricket.captions.Scene;
import com.cricket.containers.Infobar;
import com.cricket.model.Bugs;
import com.cricket.model.Configuration;
import com.cricket.model.EventFile;
import com.cricket.model.Fixture;
import com.cricket.model.Ground;
import com.cricket.model.Match;
import com.cricket.model.MatchAllData;
import com.cricket.model.NameSuper;
import com.cricket.model.Setup;
import com.cricket.model.Statistics;
import com.cricket.model.Team;
import com.cricket.model.Tournament;
import com.cricket.service.CricketService;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;
import com.fasterxml.jackson.core.exc.StreamReadException;
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
	
	public static List<MatchAllData> cricket_matches = new ArrayList<MatchAllData>();
	public static List<Tournament> past_tournament_stats = new ArrayList<Tournament>();
	public static List<Statistics> session_statistics = new ArrayList<Statistics>();
	public static List<NameSuper> session_name_super = new ArrayList<NameSuper>();
	public static List<Bugs> session_bugs = new ArrayList<Bugs>();
	public static List<Fixture> session_fixture = new ArrayList<Fixture>(); 
	public static List<Team> session_team = new ArrayList<Team>(); 
	public static List<Ground> session_ground = new ArrayList<Ground>();
	
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

	@RequestMapping(value = {"/output"}, method={RequestMethod.GET,RequestMethod.POST},
		consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}) 
	public String outputPage(ModelMap model,
			@ModelAttribute("session_configuration") Configuration session_configuration,
			@RequestParam(value = "configuration_file_name", required = false, defaultValue = "") String configuration_file_name,
			@RequestParam(value = "select_cricket_matches", required = false, defaultValue = "") String selectedMatch,
			@RequestParam(value = "select_broadcaster", required = false, defaultValue = "") String select_broadcaster,
			@RequestParam(value = "select_second_broadcaster", required = false, defaultValue = "") String select_second_broadcaster,
			@RequestParam(value = "qtIPAddress", required = false, defaultValue = "") String qtIPAddress,
			@RequestParam(value = "qtPortNumber", required = false, defaultValue = "") int qtPortNumber,
			@RequestParam(value = "vizIPAddress", required = false, defaultValue = "") String vizIPAddress,
			@RequestParam(value = "vizPortNumber", required = false, defaultValue = "") int vizPortNumber,
			@RequestParam(value = "vizSceneName", required = false, defaultValue = "") String vizScene,
			@RequestParam(value = "vizLanguage", required = false, defaultValue = "") String vizLanguage,
			@RequestParam(value = "primaryVariousOptions", required = false, defaultValue = "") String primaryVariousOptions,
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
//		if(current_date == null || current_date.isEmpty()) {
//			
//			model.addAttribute("error_message","You must be connected to the internet online");
//			return "error";
//		
//		} else if(new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date).before(new SimpleDateFormat("yyyy-MM-dd").parse(current_date))) {
//			
//			model.addAttribute("error_message","This software has expired");
//			return "error";
//			
//		}else {

			last_match_time_stamp = new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.MATCHES_DIRECTORY 
				+ selectedMatch).lastModified();
			
			session_configuration = new Configuration(selectedMatch, select_broadcaster, select_second_broadcaster,
				vizIPAddress, vizPortNumber, vizLanguage, qtIPAddress, qtPortNumber, primaryVariousOptions, vizSecondaryIPAddress,
				vizSecondaryPortNumber, vizSecondaryLanguage);
			
			JAXBContext.newInstance(Configuration.class).createMarshaller().marshal(session_configuration, 
					new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.CONFIGURATIONS_DIRECTORY + configuration_file_name));
			
			print_writers = CricketFunctions.processPrintWriter(session_configuration);
			
			this_scene = new Scene();
			this_animation = new Animation(new Infobar());
			
			switch (select_broadcaster) {
			case Constants.ICC_U19_2023:
				if(session_configuration.getPrimaryVariousOptions().contains(Constants.FULL_FRAMER)) {
					this_scene.LoadScene("FULL-FRAMERS", print_writers, session_configuration);
				}
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
			
			GetVariousDBData(session_configuration);
			
			show_speed = true;
			
			if(session_match.getMatch().getInning() != null) {
				model.addAttribute("which_inning", session_match.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning()
						.equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null).getInningNumber());
			} else {
				model.addAttribute("which_inning", "1");
			}
			model.addAttribute("session_match", session_match);
			model.addAttribute("session_configuration", session_configuration);
			model.addAttribute("select_second_broadcaster", select_second_broadcaster);
			model.addAttribute("select_broadcaster", select_broadcaster);
			return "output";
//		}
	}

	@RequestMapping(value = {"/processCricketProcedures"}, method={RequestMethod.GET,RequestMethod.POST})    
	public @ResponseBody String processCricketProcedures(
		@ModelAttribute("session_configuration") Configuration session_configuration,
		@RequestParam(value = "whatToProcess", required = false, defaultValue = "") String whatToProcess,
		@RequestParam(value = "valueToProcess", required = false, defaultValue = "") String valueToProcess) 
			throws Exception 
	{
		switch (whatToProcess.toUpperCase()) {
		case "GET-CONFIG-DATA":

			session_configuration = (Configuration)JAXBContext.newInstance(Configuration.class).createUnmarshaller().unmarshal(
				new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.CONFIGURATIONS_DIRECTORY + valueToProcess));
			
			return JSONObject.fromObject(session_configuration).toString();
			
		case "RE_READ_DATA":

			GetVariousDBData(session_configuration);
			return JSONObject.fromObject(session_match).toString();
			
		case "SHOW_SPEED":
			
			if(show_speed == true) {
				show_speed = false;
			}else {
				show_speed = true;
			}
			
			return String.valueOf(show_speed);

		case "READ-MATCH-AND-POPULATE":
			
			if(session_match == null) {
				return JSONObject.fromObject(null).toString();
			}
			
			if(last_match_time_stamp != new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY 
				+ session_match.getMatch().getMatchFileName()).lastModified()) {
				session_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ,
					CricketUtil.MATCH + "," + CricketUtil.EVENT, session_match));
				last_match_time_stamp = new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY 
						+ session_match.getMatch().getMatchFileName()).lastModified();
				
				this_caption.this_infobarGfx.updateInfobar(print_writers, session_match);
			}
			return JSONObject.fromObject(session_match).toString();
		
		default:
			
			if(whatToProcess.contains("GRAPHICS-OPTIONS")) {
				return JSONArray.fromObject(GetGraphicOption(valueToProcess)).toString();
			} else if(whatToProcess.contains("POPULATE-GRAPHICS")) {
				switch(this_animation.getTypeOfGraphicsOnScreen(valueToProcess)){
				case Constants.INFO_BAR:
					if(this_animation.infobar.isInfobar_on_screen()) {
						this_caption.whichSide = 2;
					} else {
						this_caption.whichSide = 1;
					}
					break;
				default:
					switch (session_configuration.getBroadcaster()) {
					case Constants.ICC_U19_2023:
						if(!session_configuration.getPrimaryVariousOptions().contains(Constants.FULL_FRAMER)
							&& this_animation.getTypeOfGraphicsOnScreen(valueToProcess).contains(Constants.FULL_FRAMER)) {
							this_caption.setStatus("Error: Full framers captions NOT selected on start-up");
							return JSONObject.fromObject(this_caption).toString();
						}
						break;
					}
					if(this_animation.whichGraphicOnScreen.isEmpty()) {
						this_caption.whichSide = 1;
					} else {
						//Don't allow L3rds change-on while FFs are on screen
						switch (this_animation.getTypeOfGraphicsOnScreen(this_animation.whichGraphicOnScreen)) {
						case Constants.FULL_FRAMER: case Constants.LOWER_THIRD:
							if(this_animation.getTypeOfGraphicsOnScreen(valueToProcess) 
								!= this_animation.getTypeOfGraphicsOnScreen(this_animation.whichGraphicOnScreen)) {
								this_caption.setStatus(this_animation.getTypeOfGraphicsOnScreen(
									this_animation.whichGraphicOnScreen).replace("_", " ") + " is on screen. "
									+ this_animation.getTypeOfGraphicsOnScreen(valueToProcess).replace("_", " ")
									+ " not allowed" );
								return JSONObject.fromObject(this_caption).toString();
							}
							break;
						}
						this_caption.whichSide = 2;
					}
					break;
				}
				this_caption.PopulateGraphics(valueToProcess, session_match);
				return JSONObject.fromObject(this_caption).toString();
			}
			else if(whatToProcess.contains("ANIMATE-IN-GRAPHICS") || whatToProcess.contains("ANIMATE-OUT-GRAPHICS")
				|| whatToProcess.contains("ANIMATE-OUT-INFOBAR") || whatToProcess.contains("QUIDICH-COMMANDS")) {
				
				if(whatToProcess.contains("ANIMATE-IN-GRAPHICS")) {
					switch(this_animation.getTypeOfGraphicsOnScreen(valueToProcess)){
					case Constants.INFO_BAR:
						this_animation.ChangeOn(valueToProcess, print_writers, session_configuration);
						TimeUnit.MILLISECONDS.sleep(2000);
						this_caption.whichSide = 1;
						this_caption.PopulateGraphics(valueToProcess, session_match);
						this_animation.CutBack(valueToProcess, print_writers, session_configuration);
						break;
					default:
						if(this_animation.whichGraphicOnScreen.isEmpty()) {
							this_animation.AnimateIn(valueToProcess, print_writers, session_configuration);
						} else { // Change on
							this_animation.ChangeOn(valueToProcess, print_writers, session_configuration);
							TimeUnit.MILLISECONDS.sleep(2000);
							this_caption.whichSide = 1;
							this_caption.PopulateGraphics(valueToProcess, session_match);
							this_animation.CutBack(valueToProcess, print_writers, session_configuration);
						}
						break;
					}
				} else if(whatToProcess.contains("ANIMATE-OUT-GRAPHICS")) {
					switch (valueToProcess.split(",")[0]) {
					case "Alt_p":
						if(!this_animation.whichGraphicOnScreen.isEmpty()) {
							this_animation.status = "Cannot animate out bugs while " + 
								this_animation.whichGraphicOnScreen + " is on screen";
							return JSONObject.fromObject(this_animation).toString();
						}
						this_animation.AnimateOut(valueToProcess, print_writers, session_configuration);
						break;
					default:
						this_animation.AnimateOut(this_animation.whichGraphicOnScreen, print_writers, session_configuration);
						break;
					}
				}else if(whatToProcess.contains("ANIMATE-OUT-INFOBAR")) {
					this_animation.AnimateOut("F12,", print_writers, session_configuration);
				}else if(whatToProcess.contains("QUIDICH-COMMANDS")) {
					this_animation.processQuidichCommands(valueToProcess, print_writers, session_configuration);
				}
			} else if(whatToProcess.contains("CLEAR-ALL") || whatToProcess.contains("CLEAR-ALL-WITH-INFOBAR")) {
				this_animation.ResetAnimation(whatToProcess, print_writers, session_configuration);
			}
			System.out.println("this_animation = " + this_animation);
			return JSONObject.fromObject(this_animation).toString();
		}
	}
	@ModelAttribute("session_configuration")
	public Configuration session_configuration(){
		return new Configuration();
	}
	@SuppressWarnings("unchecked")
	public <T> List<T> GetGraphicOption(String whatToProcess) {
		switch (whatToProcess) {
		case "F10": case "j":
		    return (List<T>) session_name_super;
		case "k":
			return (List<T>) session_bugs;
		case "Control_m":
			return (List<T>) CricketFunctions.processAllFixtures(cricketService);
		}
		return null;
	}
	
	public void GetVariousDBData(Configuration config) throws StreamReadException, DatabindException, 
		IllegalAccessException, InvocationTargetException, JAXBException, IOException
	{
		switch (config.getBroadcaster()) {
		case Constants.ICC_U19_2023:
			
			cricket_matches = CricketFunctions.getTournamentMatches(new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".json") && pathname.isFile();
			    }
			}), cricketService);
			session_statistics = cricketService.getAllStats();
			past_tournament_stats = CricketFunctions.extractTournamentStats(
				"COMBINED_PAST_CURRENT_MATCH_DATA",false, cricket_matches, cricketService, session_match, null);
			session_name_super =  cricketService.getNameSupers();
			session_fixture =  CricketFunctions.processAllFixtures(cricketService);
			session_team =  cricketService.getTeams();
			session_ground =  cricketService.getGrounds();

			switch (config.getBroadcaster()) {
			case Constants.ICC_U19_2023:
				this_caption = new Caption(print_writers, config, session_statistics,cricketService.getAllStatsType(), 
					cricket_matches, session_name_super,session_bugs,session_fixture, session_team, session_ground,
					new FullFramesGfx(),new LowerThirdGfx(), 1, "", "-",past_tournament_stats);
				break;
			}
			
			break;
		}
	}
	
}