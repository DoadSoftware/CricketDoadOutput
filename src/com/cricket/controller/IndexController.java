package com.cricket.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
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
import com.cricket.captions.BugsAndMiniGfx;
import com.cricket.captions.Caption;
import com.cricket.captions.Constants;
import com.cricket.captions.FullFramesGfx;
import com.cricket.captions.InfobarGfx;
import com.cricket.captions.LowerThirdGfx;
import com.cricket.captions.Scene;
import com.cricket.containers.Infobar;
import com.cricket.containers.Stats;
import com.cricket.model.BestStats;
import com.cricket.model.Bugs;
import com.cricket.model.Commentator;
import com.cricket.model.Configuration;
import com.cricket.model.DuckWorthLewis;
import com.cricket.model.EventFile;
import com.cricket.model.FieldersData;
import com.cricket.model.Fixture;
import com.cricket.model.Ground;
import com.cricket.model.HeadToHead;
import com.cricket.model.InfobarStats;
import com.cricket.model.Match;
import com.cricket.model.MatchAllData;
import com.cricket.model.NameSuper;
import com.cricket.model.POTT;
import com.cricket.model.PerformanceBug;
import com.cricket.model.Player;
import com.cricket.model.Setup;
import com.cricket.model.Staff;
import com.cricket.model.Statistics;
import com.cricket.model.Team;
import com.cricket.model.Tournament;
import com.cricket.model.VariousText;
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
@SessionAttributes(value = {"session_configuration","expiryDate"})
public class IndexController 
{
	@Autowired
	CricketService cricketService;
	
	public static MatchAllData session_match;
	public static String expiry_date = "2024-12-31";
	public static String current_date;
	public static long last_match_time_stamp = 0;
	public static Scene this_scene;
	public static Caption this_caption;
	public static Animation this_animation;
	public static List<PrintWriter> print_writers;
	public static boolean show_speed = true;
	
	public static List<MatchAllData> cricket_matches = new ArrayList<MatchAllData>();
	public static List<Tournament> past_tournament_stats = new ArrayList<Tournament>();
	public static List<HeadToHead> headToHead = new ArrayList<HeadToHead>();
	public static List<BestStats> past_tape = new ArrayList<BestStats>();
	public static List<Statistics> session_statistics = new ArrayList<Statistics>();
	public static Statistics session_statistics_past_matches = new Statistics();
	public static List<NameSuper> session_name_super = new ArrayList<NameSuper>();
	public static List<Bugs> session_bugs = new ArrayList<Bugs>();
	public static List<InfobarStats> session_infoBarStats = new ArrayList<InfobarStats>();
	public static List<Fixture> session_fixture = new ArrayList<Fixture>(); 
	public static List<Team> session_team = new ArrayList<Team>(); 
	public static List<Ground> session_ground = new ArrayList<Ground>();
	public static List<VariousText> session_variousText = new ArrayList<VariousText>();
	public static List<Commentator> session_commentator = new ArrayList<Commentator>();
	public static List<Staff> session_staff = new ArrayList<Staff>();
	public static List<Player> session_players = new ArrayList<Player>();
	public static List<POTT> session_pott = new ArrayList<POTT>();
	public static List<String> session_teamChanges = new ArrayList<String>();
	public static List<PerformanceBug> session_performance_bug = new ArrayList<PerformanceBug>();
	
	public static long plotter_match_time_stamp1=0,plotter_match_time_stamp2=0,
			plotter_match_time_stamp3=0,plotter_match_time_stamp4=0,plotter_match_time_stamp=0,speed_match_time_stamp=0;
	public static String plotterData;
	public boolean Plotter_file_change = false;
	
	FieldersData fielderFormation = new FieldersData();
	
	BugsAndMiniGfx this_bugs_mini = new BugsAndMiniGfx();
	
	List<DuckWorthLewis> session_dls = new ArrayList<DuckWorthLewis>();
	
	@RequestMapping(value = {"/","/initialise"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String initialisePage(ModelMap model, 
		@ModelAttribute("session_configuration") Configuration session_configuration,
		@ModelAttribute("expiryDate") String expiryDate) 
		throws JAXBException, MalformedURLException, IOException, IllegalAccessException, InvocationTargetException 
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
		
//		if(cricket_matches == null || cricket_matches.size()<=0) {
//			cricket_matches = CricketFunctions.getTournamentMatches(new File(CricketUtil.CRICKET_SERVER_DIRECTORY + 
//					CricketUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
//				@Override
//			    public boolean accept(File pathname) {
//			        String name = pathname.getName().toLowerCase();
//			        return name.endsWith(".json") && pathname.isFile();
//			    }
//			}), cricketService);
//		}
		
//		headToHead = CricketFunctions.extractHeadToHead(new File(CricketUtil.CRICKET_SERVER_DIRECTORY + 
//				CricketUtil.HEADTOHEAD_DIRECTORY).listFiles(), cricketService);

		return "initialise";
	}
	@RequestMapping(value = {"/Help"}, method={RequestMethod.GET,RequestMethod.POST}) 
		public String HelpPage()  
		{
			return "Help";
		}
		
	@RequestMapping(value = {"/output"}, method={RequestMethod.GET,RequestMethod.POST},
		consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}) 
	public String outputPage(ModelMap model,
			@ModelAttribute("session_configuration") Configuration session_configuration,
			@ModelAttribute("expiryDate") String expiryDate,
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
			@RequestParam(value = "vizTertiaryLanguage", required = false, defaultValue = "") String vizTertiaryLanguage,
			@RequestParam(value = "previewOnOrOff", required = false, defaultValue = "") String previewOnOrOff) 
				throws StreamWriteException, DatabindException, IllegalAccessException, InvocationTargetException, 
				JAXBException, IOException, URISyntaxException, ParseException, InterruptedException, CloneNotSupportedException 
	{
		if(current_date == null || current_date.isEmpty()) {
			
			model.addAttribute("error_message","You must be connected to the internet online");
			return "error";
		
		} else if(new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date).before(new SimpleDateFormat("yyyy-MM-dd").parse(current_date))) {
			
			model.addAttribute("error_message","This software has expired");
			return "error";
			
		}else {

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			
			LocalDate date1 = LocalDate.parse(current_date, dtf);
			LocalDate date2 = LocalDate.parse(expiry_date, dtf);
			
			long daysBetween = ChronoUnit.DAYS.between(date1, date2);
			
			expiryDate = String.valueOf(daysBetween);
			
			last_match_time_stamp = new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.MATCHES_DIRECTORY 
				+ selectedMatch).lastModified();
			
			session_configuration = new Configuration(selectedMatch, select_broadcaster, select_second_broadcaster,
				vizIPAddress, vizPortNumber, vizLanguage, qtIPAddress, qtPortNumber, primaryVariousOptions, vizSecondaryIPAddress,
				vizSecondaryPortNumber, vizSecondaryLanguage, previewOnOrOff);
			
			JAXBContext.newInstance(Configuration.class).createMarshaller().marshal(session_configuration, 
					new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.CONFIGURATIONS_DIRECTORY + configuration_file_name));
			
			print_writers = CricketFunctions.processPrintWriter(session_configuration);
			
			this_scene = new Scene();
			this_animation = new Animation(new Infobar());
			
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
			session_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ, 
					CricketUtil.SETUP + "," + CricketUtil.MATCH + "," + CricketUtil.EVENT, session_match));			
			session_match.getSetup().setMatchFileTimeStamp(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
			
			headToHead = CricketFunctions.extractHeadToHead(session_match, cricketService);
			//past_tournament_stats = CricketFunctions.extractTournamentData("PAST_MATCHES_DATA", false, headToHead, cricketService, session_match, null);
			
			GetVariousDBData("NEW", session_configuration);
			
			show_speed = true;
			
			switch (select_broadcaster) {
			case Constants.ISPL:
				if(session_configuration.getPrimaryVariousOptions().contains(Constants.FULL_FRAMER)) {
					this_scene.LoadScene("FULL-FRAMERS", print_writers, session_configuration);
				}
				if(session_match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
					this_scene.LoadScene("SO_OVERLAYS", print_writers, session_configuration);
				}else {
					this_scene.LoadScene("OVERLAYS", print_writers, session_configuration);
				}
				
				this_animation.ResetAnimation("CLEAR-ALL", print_writers, session_configuration);
				break;
			case Constants.ICC_U19_2023: case Constants.BENGAL_T20: case Constants.NPL:
				if(session_configuration.getPrimaryVariousOptions().contains(Constants.FULL_FRAMER)) {
					this_scene.LoadScene("FULL-FRAMERS", print_writers, session_configuration);
				}
				this_scene.LoadScene("OVERLAYS", print_writers, session_configuration);
				break;
			}
			
			if(session_match.getMatch().getInning() != null) {
				model.addAttribute("which_inning", session_match.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning()
						.equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null).getInningNumber());
			} else {
				model.addAttribute("which_inning", "1");
			}
			model.addAttribute("session_match", session_match);
			model.addAttribute("expiryDate", expiryDate);
			model.addAttribute("session_configuration", session_configuration);
			model.addAttribute("select_second_broadcaster", select_second_broadcaster);
			model.addAttribute("select_broadcaster", select_broadcaster);
			
			return "output";
		}
	}

	@RequestMapping(value = {"/processCricketProcedures"}, method={RequestMethod.GET,RequestMethod.POST})    
	public @ResponseBody String processCricketProcedures(
		@ModelAttribute("session_configuration") Configuration session_configuration,
		@RequestParam(value = "whatToProcess", required = false, defaultValue = "") String whatToProcess,
		@RequestParam(value = "valueToProcess", required = false, defaultValue = "") String valueToProcess) 
			throws Exception 
	{
		switch (whatToProcess.toUpperCase()) {
		case "HEAD_TO_HEAD_FILE":
			CricketFunctions.exportMatchData(session_match);
			
			return JSONObject.fromObject(session_match).toString();
		case "GET-CONFIG-DATA":

			session_configuration = (Configuration)JAXBContext.newInstance(Configuration.class).createUnmarshaller().unmarshal(
				new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.CONFIGURATIONS_DIRECTORY + valueToProcess));
			
			return JSONObject.fromObject(session_configuration).toString();
		
		case "RE_READ_DATA":
			
			headToHead = CricketFunctions.extractHeadToHead(session_match, cricketService);
//			past_tournament_stats = CricketFunctions.extractTournamentData("PAST_MATCHES_DATA", false, headToHead, cricketService, session_match, null);
			
			GetVariousDBData("UPDATE", session_configuration);
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
			
			
			if(new File("C:\\Sports\\Cricket\\Fielder\\Fielder_Text\\" + 
		            "FieldPlotter.txt").exists()) {
				
				String data = new String(Files.readAllBytes(Paths.get("C:\\Sports\\Cricket\\Fielder\\Fielder_Text\\" + 
			            "FieldPlotter.txt")));
		        // Split the content by lines and print each line separately
		        String[] lines = data.split("\n");
		        
		        plotterData = lines[0].trim();
		        
		        if(lines.length > 0) {
					if(lines[1].trim().equalsIgnoreCase("true")) {
						fielderFormation = CricketFunctions.getFielderFormation(CricketUtil.CRICKET_DIRECTORY + "Fielder/" + lines[0].trim());
						if(fielderFormation.isCheckbox() == true) {
							if(lines[0].trim().equalsIgnoreCase("FielderFormation.JSON")) {
								if(plotter_match_time_stamp != new File(CricketUtil.CRICKET_DIRECTORY + "Fielder/" + lines[0].trim()).lastModified()) {
									plotter_match_time_stamp = new File(CricketUtil.CRICKET_DIRECTORY + "Fielder/" + lines[0].trim()).lastModified();
									//System.out.println("AfterCheckBox = " + fielderFormation.isCheckbox());
									Plotter_file_change = true;
								}
							}else if(lines[0].trim().equalsIgnoreCase("FielderFormation_1.JSON")) {
								if(plotter_match_time_stamp1 != new File(CricketUtil.CRICKET_DIRECTORY + "Fielder/" + lines[0].trim()).lastModified()) {
									plotter_match_time_stamp1 = new File(CricketUtil.CRICKET_DIRECTORY + "Fielder/" + lines[0].trim()).lastModified();
									//System.out.println("AfterCheckBox = " + fielderFormation.isCheckbox());
									Plotter_file_change = true;
								}
							}else if(lines[0].trim().equalsIgnoreCase("FielderFormation_2.JSON")) {
								if(plotter_match_time_stamp2 != new File(CricketUtil.CRICKET_DIRECTORY + "Fielder/" + lines[0].trim()).lastModified()) {
									plotter_match_time_stamp2 = new File(CricketUtil.CRICKET_DIRECTORY + "Fielder/" + lines[0].trim()).lastModified();
									//System.out.println("AfterCheckBox = " + fielderFormation.isCheckbox());
									Plotter_file_change = true;
								}
							}else if(lines[0].trim().equalsIgnoreCase("FielderFormation_3.JSON")) {
								if(plotter_match_time_stamp3 != new File(CricketUtil.CRICKET_DIRECTORY + "Fielder/" + lines[0].trim()).lastModified()) {
									plotter_match_time_stamp3 = new File(CricketUtil.CRICKET_DIRECTORY + "Fielder/" + lines[0].trim()).lastModified();
									//System.out.println("AfterCheckBox = " + fielderFormation.isCheckbox());
									Plotter_file_change = true;
								}
							}else if(lines[0].trim().equalsIgnoreCase("FielderFormation_4.JSON")) {
								if(plotter_match_time_stamp4 != new File(CricketUtil.CRICKET_DIRECTORY + "Fielder/" + lines[0].trim()).lastModified()) {
									plotter_match_time_stamp4 = new File(CricketUtil.CRICKET_DIRECTORY + "Fielder/" + lines[0].trim()).lastModified();
									//System.out.println("AfterCheckBox = " + fielderFormation.isCheckbox());
									Plotter_file_change = true;
								}
							}
						}
					}else if(lines[1].trim().equalsIgnoreCase("false")) {
						
					}
				}
			}
			
			if(Plotter_file_change == true) {
				
				this_caption.this_infobarGfx.updateFieldPlotter(print_writers, session_match);
				Plotter_file_change = false;
			}
			
			return JSONObject.fromObject(session_match).toString();
		
		default:
			System.out.println("whatToProcess = " + whatToProcess);
			if(session_configuration.getBroadcaster().equalsIgnoreCase(Constants.NPL)) {
				if(whatToProcess.split(",")[0].toUpperCase().equalsIgnoreCase("highlightProfile") || whatToProcess.split(",")[0].toUpperCase().equalsIgnoreCase("highlightLeader")) {
					
					this_animation.ChangeOn(whatToProcess, print_writers, session_configuration);
				}
			}
			if(whatToProcess.toUpperCase().equalsIgnoreCase("IMPACT-CHANGE-ON")) {
				this_animation.AnimateIn("Shift_I", print_writers, session_configuration);
			}
			if(whatToProcess.contains("GRAPHICS-OPTIONS")||whatToProcess.contains("GRAPHICS-OPTIONS_DATA")) {
				return JSONArray.fromObject(GetGraphicOption(valueToProcess)).toString();
			}else if(whatToProcess.contains("POPULATE-GRAPHICS")) {
				switch(this_animation.getTypeOfGraphicsOnScreen(session_configuration,valueToProcess)){
				case Constants.INFO_BAR:
					if(valueToProcess.split(",")[0].equalsIgnoreCase("Control_F12")) {
						if(this_animation.infobar.isInfobar_on_screen()) {
							this_caption.whichSide = 2;
						} else {
							this_caption.whichSide = 1;
						}
						this_caption.PopulateGraphics(valueToProcess, session_match);
//						this_animation.caption = this_caption;
//						if(this_caption.status.equalsIgnoreCase(Constants.OK)) {
//							processAnimations("ANIMATE-IN-GRAPHICS", session_configuration, valueToProcess, print_writers);
//							return JSONObject.fromObject(this_caption).toString();
//						} else {
//							return JSONObject.fromObject(this_caption).toString();
//						}
					}else {
						if(this_animation.infobar.isInfobar_on_screen()) {
							this_caption.whichSide = 2;
						} else {
							this_caption.whichSide = 1;
						}
						this_caption.PopulateGraphics(valueToProcess, session_match);
						this_animation.caption = this_caption;
						if(this_caption.status.equalsIgnoreCase(Constants.OK)) {
							processAnimations("ANIMATE-IN-GRAPHICS", session_configuration, valueToProcess, print_writers);
							this_caption.status = CricketUtil.YES;
							return JSONObject.fromObject(this_caption).toString();
						} else {
							return JSONObject.fromObject(this_caption).toString();
						}
					}
					
				default:
					switch (session_configuration.getBroadcaster()) {
					case Constants.ICC_U19_2023: case Constants.ISPL: case Constants.BENGAL_T20: case Constants.NPL:
						if(!session_configuration.getPrimaryVariousOptions().contains(Constants.FULL_FRAMER)
							&& this_animation.getTypeOfGraphicsOnScreen(session_configuration, valueToProcess).contains(Constants.FULL_FRAMER)) {
							this_caption.setStatus("Error: Full framers captions NOT selected on start-up");
							return JSONObject.fromObject(this_caption).toString();
						}
						break;
					}
					if(this_animation.whichGraphicOnScreen.isEmpty()) {
						if(!this_animation.specialBugOnScreen.equalsIgnoreCase(CricketUtil.TOSS)) {
							if(this_animation.infobar.isInfobar_on_screen() == false) {
								this_animation.ResetAnimation("CLEAR-ALL", print_writers, session_configuration);
							}else {
								this_animation.ResetAnimation("", print_writers, session_configuration);
							}
						}
						this_caption.whichSide = 1;
					} else {
						//Don't allow L3rds change-on while FFs are on screen
						switch (this_animation.getTypeOfGraphicsOnScreen(session_configuration, this_animation.whichGraphicOnScreen)) {
						case Constants.FULL_FRAMER: case Constants.LOWER_THIRD: 
						case Constants.NAME_SUPERS + Constants.LOWER_THIRD:
						case Constants.BOUNDARIES + Constants.LOWER_THIRD:
							
							if(this_animation.getTypeOfGraphicsOnScreen(session_configuration,valueToProcess) 
								!= this_animation.getTypeOfGraphicsOnScreen(session_configuration,this_animation.whichGraphicOnScreen)) {

								//Make a preview of lowerThird when FullFrames is on Screen and vice-verca
								switch (this_animation.getTypeOfGraphicsOnScreen(session_configuration, this_animation.whichGraphicOnScreen)) {
								case Constants.FULL_FRAMER: 
									switch (this_animation.getTypeOfGraphicsOnScreen(session_configuration, valueToProcess)) {
									case Constants.LOWER_THIRD: case Constants.NAME_SUPERS + Constants.LOWER_THIRD: 
									case Constants.BOUNDARIES + Constants.LOWER_THIRD:
										this_caption.whichSide = 1;
										this_caption.PopulateGraphics(valueToProcess, session_match);
										this_animation.processL3Preview(valueToProcess, print_writers, this_caption.whichSide, session_configuration,session_match);
										break;
									}
									break;
								case Constants.LOWER_THIRD: case Constants.NAME_SUPERS + Constants.LOWER_THIRD: 
								case Constants.BOUNDARIES + Constants.LOWER_THIRD:
									switch (this_animation.getTypeOfGraphicsOnScreen(session_configuration, valueToProcess)) {
									case Constants.FULL_FRAMER:
										this_caption.whichSide = 1;
										this_caption.PopulateGraphics(valueToProcess, session_match);
										this_animation.processFullFramesPreview(valueToProcess, print_writers, this_caption.whichSide, 
											session_configuration, this_animation.whichGraphicOnScreen);
										break;
									}
									break;
								}
								
								this_caption.setStatus(this_animation.getTypeOfGraphicsOnScreen(session_configuration,
									this_animation.whichGraphicOnScreen).replace("_", " ") + " is on screen. "
									+ this_animation.getTypeOfGraphicsOnScreen(session_configuration,valueToProcess).replace(
									"_", " ") + " not allowed" );
								
								return JSONObject.fromObject(this_caption).toString();

							}
							break;
						}
						this_caption.whichSide = 2;
					}
					this_caption.PopulateGraphics(valueToProcess, session_match);
					this_animation.caption = this_caption;
					//Previews
					switch (this_animation.getTypeOfGraphicsOnScreen(session_configuration, valueToProcess)) {
					case Constants.FULL_FRAMER:
						this_animation.processFullFramesPreview(valueToProcess, print_writers, this_caption.whichSide, 
							session_configuration, this_animation.whichGraphicOnScreen);
						break;
					case Constants.LOWER_THIRD: 
					case Constants.NAME_SUPERS + Constants.LOWER_THIRD:
					case Constants.BOUNDARIES + Constants.LOWER_THIRD:
						this_animation.processL3Preview(valueToProcess, print_writers, this_caption.whichSide, session_configuration,session_match);
						break;
					case Constants.BUGS:
						this_animation.processBugsPreview(valueToProcess, print_writers, this_caption.whichSide, 
							session_configuration, this_animation.whichGraphicOnScreen);
						break;
					case Constants.MINIS:
						this_animation.processMiniPreview(valueToProcess, print_writers, this_caption.whichSide, 
							session_configuration, this_animation.whichGraphicOnScreen);
						break;
					}
					break;
				}
				return JSONObject.fromObject(this_caption).toString();
			}
			else if(whatToProcess.contains("ANIMATE-IN-GRAPHICS") || whatToProcess.contains("ANIMATE-OUT-GRAPHICS")
				|| whatToProcess.contains("ANIMATE-OUT-INFOBAR") || whatToProcess.contains("QUIDICH-COMMANDS") || 
				whatToProcess.contains("ANIMATE-OUT-TAPE") || whatToProcess.contains("ANIMATE-OUT-IDENT") || 
				whatToProcess.contains("ANIMATE-OUT-CR") || whatToProcess.contains("ANIMATE-OUT-TARGET")) {

				if(whatToProcess.contains("ANIMATE-OUT-GRAPHICS")) {
					switch (valueToProcess.split(",")[0]) {
					case "Alt_p":
						if(!this_animation.whichGraphicOnScreen.isEmpty()) {
							this_animation.status = "Cannot animate out bugs while " + 
								this_animation.whichGraphicOnScreen + " is on screen";
							return JSONObject.fromObject(this_animation).toString();
						}
						break;
					}
				}
				
				processAnimations(whatToProcess, session_configuration, valueToProcess, print_writers);
			}else if(whatToProcess.contains("ANIMATE-OUT-SECOND_PLAYING")) {
				this_animation.processAnimation(Constants.BACK, print_writers, "Anim_Lineup_Image_Big", "CONTINUE");
				this_animation.whichGraphicOnScreen = "";
			} 
			else if(whatToProcess.contains("CLEAR-ALL") || whatToProcess.contains("CLEAR-ALL-WITH-INFOBAR")) {
				this_animation.ResetAnimation(whatToProcess, print_writers, session_configuration);
			}else if(whatToProcess.contains("CANCLE-GRAPHICS")) {
				this_caption.whichSide = 1;
				
			}
			return JSONObject.fromObject(this_animation).toString();
		}
	}
	public void processAnimations(String whatToProcess, Configuration session_configuration, String valueToProcess, 
		List<PrintWriter> print_writers) throws InterruptedException, NumberFormatException, ParseException, 
		CloneNotSupportedException, IOException, JAXBException, UnsupportedAudioFileException, LineUnavailableException, IllegalAccessException, InvocationTargetException, URISyntaxException
	{
		if(whatToProcess.contains("ANIMATE-IN-GRAPHICS")) {
			switch(this_animation.getTypeOfGraphicsOnScreen(session_configuration,valueToProcess)){
			case Constants.INFO_BAR:
				if(valueToProcess.split(",")[0].equalsIgnoreCase("Control_F8")){
					this_animation.AnimateIn(valueToProcess, print_writers, session_configuration);
				}else if(valueToProcess.split(",")[0].equalsIgnoreCase("Control_F12")) {
					this_animation.AnimateIn(valueToProcess, print_writers, session_configuration);
				}else if(valueToProcess.split(",")[0].equalsIgnoreCase("alt_c")) {
					this_animation.AnimateIn(valueToProcess, print_writers, session_configuration);
				}else if(valueToProcess.split(",")[0].equalsIgnoreCase("Alt_y")){
					this_animation.AnimateIn(valueToProcess, print_writers, session_configuration);
				}else {
					this_animation.ChangeOn(valueToProcess, print_writers, session_configuration);
					TimeUnit.MILLISECONDS.sleep(2000);
					this_caption.whichSide = 1;
					this_caption.PopulateGraphics(valueToProcess, session_match);
					this_animation.CutBack(valueToProcess, print_writers, session_configuration);
				}
				break;
			default:
				if(this_animation.whichGraphicOnScreen.isEmpty()) {
					this_animation.AnimateIn(valueToProcess, print_writers, session_configuration);
				} else { // Change on
					this_animation.ChangeOn(valueToProcess, print_writers, session_configuration);
					TimeUnit.MILLISECONDS.sleep(2500);
					this_caption.whichSide = 1;
					this_caption.PopulateGraphics(valueToProcess, session_match);
					TimeUnit.MILLISECONDS.sleep(300);
					this_animation.CutBack(valueToProcess, print_writers, session_configuration);
					
				}
				break;
			}
		} else if(whatToProcess.contains("ANIMATE-OUT-GRAPHICS")) {
			switch (valueToProcess.split(",")[0]) {
			case "Alt_p":
				this_animation.AnimateOut(valueToProcess, print_writers, session_configuration);
				break;
			default:
				this_animation.AnimateOut(this_animation.whichGraphicOnScreen, print_writers, session_configuration);
				break;
			}
		}else if(whatToProcess.contains("ANIMATE-OUT-INFOBAR")) {
			this_animation.AnimateOut("F12,", print_writers, session_configuration);
		}else if(whatToProcess.contains("ANIMATE-OUT-IDENT")) {
			this_animation.AnimateOut("Control_F12,", print_writers, session_configuration);
		}else if(whatToProcess.contains("QUIDICH-COMMANDS")) {
			this_animation.processQuidichCommands(valueToProcess, print_writers, session_configuration);
		}else if(whatToProcess.contains("ANIMATE-OUT-TAPE")) {
			this_animation.AnimateOut("Control_F8,", print_writers, session_configuration);
		}else if(whatToProcess.contains("ANIMATE-OUT-TARGET")) {
			this_animation.AnimateOut("Alt_y,", print_writers, session_configuration);
		}else if(whatToProcess.contains("ANIMATE-OUT-CR")) {
			this_animation.AnimateOut("Alt_c,", print_writers, session_configuration);
		}
	}
	@ModelAttribute("session_configuration")
	public Configuration session_configuration(){
		return new Configuration();
	}
	@ModelAttribute("expiryDate")
	public String expiryDate(){
		return new String();
	}
	
	public static List<Stats> getPlayerFromMatchData(String whatToProcess, MatchAllData match)
	{
		List<Stats> stats = new ArrayList<Stats>();
		
		switch(whatToProcess) {
		case "Shift_!":
			for(Player plyr : match.getSetup().getHomeSquad()) {
				stats.add(new Stats(plyr.getPlayerId(), match.getSetup().getHomeTeam(), plyr, new ArrayList<Statistics>()));
			}
			for(Player plyr : match.getSetup().getHomeSubstitutes()) {
				stats.add(new Stats(plyr.getPlayerId(), match.getSetup().getHomeTeam(), plyr, new ArrayList<Statistics>()));
			}
			for(Player plyr : match.getSetup().getAwaySquad()) {
				stats.add(new Stats(plyr.getPlayerId(), match.getSetup().getAwayTeam(), plyr, new ArrayList<Statistics>()));
			}
			for(Player plyr : match.getSetup().getAwaySubstitutes()) {
				stats.add(new Stats(plyr.getPlayerId(), match.getSetup().getAwayTeam(), plyr, new ArrayList<Statistics>()));
			}
			break;
		case "Shift_~":
			for(Player plyr : match.getSetup().getHomeSquad()) {
				stats.add(new Stats(plyr.getPlayerId(), match.getSetup().getHomeTeam(), plyr, null, null));
			}
			for(Player plyr : match.getSetup().getHomeSubstitutes()) {
				stats.add(new Stats(plyr.getPlayerId(), match.getSetup().getHomeTeam(), plyr, null, null));
			}
			for(Player plyr : match.getSetup().getAwaySquad()) {
				stats.add(new Stats(plyr.getPlayerId(), match.getSetup().getAwayTeam(), plyr, null, null));
			}
			for(Player plyr : match.getSetup().getAwaySubstitutes()) {
				stats.add(new Stats(plyr.getPlayerId(), match.getSetup().getAwayTeam(), plyr, null, null));
			}
			break;
		}
		return stats;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> GetGraphicOption(String whatToProcess) throws IOException, NumberFormatException, IllegalAccessException, 
	InvocationTargetException, InterruptedException, ParseException, CloneNotSupportedException, JAXBException, UnsupportedAudioFileException, 
	LineUnavailableException, URISyntaxException {
		switch ((whatToProcess.contains(",")? whatToProcess.split(",")[0]:whatToProcess)) {
		case "Alt_e":
			this_caption.whichSide = 1;
			this_caption.PopulateGraphics("Alt_e,", session_match);
			break;
		case "Control_Shift_J":
			return (List<T>) session_performance_bug;
		case "F10": case "j":
		    return (List<T>) session_name_super;
		case "k":
			return (List<T>) session_bugs;
		case "Control_m": case "Shift_F11": case "Control_Shift_L":
			return (List<T>) CricketFunctions.processAllFixtures(cricketService);
		case "Alt_9":
			return (List<T>) session_infoBarStats;
		case "Alt_0":
			return (List<T>) session_commentator;
		case "Alt_a":
			return (List<T>) CricketFunctions.processAllStaff(cricketService, session_match.getSetup().getHomeTeamId());
		case "Alt_s":
			return (List<T>) CricketFunctions.processAllStaff(cricketService, session_match.getSetup().getAwayTeamId());
		case "Alt_q":
			return (List<T>) CricketFunctions.processAllPott(cricketService);
		case "Alt_Shift_R": case "Alt_z": case "Alt_Shift_W": //case "Control_Shift_F8":
			return (List<T>) session_team;
		case "Shift_!":
			List<Stats> database_statistics = new ArrayList<Stats>();
			database_statistics = getPlayerFromMatchData(whatToProcess ,session_match);
			
			for(Statistics stats : session_statistics) {
				for(int i=0;i<=database_statistics.size()-1;i++) {
					if(database_statistics.get(i).getPlayerId() == stats.getPlayer_id()) {
						stats.setStats_type(cricketService.getStatsType(stats.getStats_type_id()));
						database_statistics.get(i).getStatsList().add(stats);
					}
				}
			}
			
			return (List<T>) database_statistics;
		case "Shift_~":
			List<Stats> statistics = new ArrayList<Stats>();
			statistics = getPlayerFromMatchData(whatToProcess ,session_match);
			
			for(Statistics stats : session_statistics) {
				for(int i=0;i<=statistics.size()-1;i++) {
					if(statistics.get(i).getPlayerId() == stats.getPlayer_id()) {
						stats.setStats_type(cricketService.getStatsType(stats.getStats_type_id()));
						stats = CricketFunctions.updateTournamentWithH2h(stats, headToHead, session_match);
						stats = CricketFunctions.updateStatisticsWithMatchData(stats, session_match);
						statistics.get(i).setStats(stats);
					}
				}
			}
			for(Tournament tour : CricketFunctions.extractTournamentData("CURRENT_MATCH_DATA", false, headToHead,cricketService, session_match, past_tournament_stats)) {
				for(int i=0;i<=statistics.size()-1;i++) {
					if(tour.getPlayerId() == statistics.get(i).getPlayerId()) {
						statistics.get(i).setTournament(tour);
					}
				}
			}
			
			return (List<T>) statistics;
		case "Control_Shift_F8":
			if(whatToProcess.contains(",")) {
				FullFramesGfx.stats_past_tournament = CricketFunctions.extractTournamentData("CURRENT_MATCH_DATA", false, headToHead, cricketService, 
						session_match, past_tournament_stats);
				FullFramesGfx.stats_past_tournament.removeIf(tournament -> tournament.getPlayer().getTeamId() != Integer.valueOf(whatToProcess.split(",")[1])); 
				switch(whatToProcess.split(",")[2]) {
					case "MOST RUNS":
						Collections.sort(FullFramesGfx.stats_past_tournament,new CricketFunctions.BatsmenMostRunComparator());
						break;
					case "MOST WICKETS":
						Collections.sort(FullFramesGfx.stats_past_tournament,new CricketFunctions.BowlerWicketsComparator());
						break;
					case "MOST FOURS":
						Collections.sort(FullFramesGfx.stats_past_tournament,new CricketFunctions.BatsmanFoursComparator());
						break;
					case "MOST SIXES":
						Collections.sort(FullFramesGfx.stats_past_tournament,new CricketFunctions.BatsmanSixesComparator());
						break;
					}
		        return FullFramesGfx.stats_past_tournament.size() > 5 ? (List<T>) FullFramesGfx.stats_past_tournament.subList(0, 5) : (List<T>) FullFramesGfx.stats_past_tournament;
			}else {
				return (List<T>) cricketService.getTeams();
			}
		case "z": case "x": case "c": case "v": case "Control_Shift_Z": case "Control_Shift_Y":
			List<Tournament> tournament_stats = CricketFunctions.extractTournamentData("CURRENT_MATCH_DATA", false, headToHead, cricketService, 
					session_match, past_tournament_stats);
			switch (whatToProcess) {
			case "z": 
				Collections.sort(tournament_stats,new CricketFunctions.BatsmenMostRunComparator());
				break;
			case "x": 
				Collections.sort(tournament_stats,new CricketFunctions.BowlerWicketsComparator());
				break;
			case "c": 
				Collections.sort(tournament_stats,new CricketFunctions.BatsmanFoursComparator());
				break;
			case "v":
				Collections.sort(tournament_stats,new CricketFunctions.BatsmanSixesComparator());
				break;
			case "Control_Shift_Z":
				Collections.sort(tournament_stats,new CricketFunctions.BestBatsmanStrikeRateComparator());
				break;
			case "Control_Shift_Y":
				Collections.sort(tournament_stats,new CricketFunctions.BestBowlerEconomyComparator());
				break;
			}
			return (List<T>) tournament_stats;
		case "Control_c":
			List<BestStats> tapeball = new ArrayList<BestStats>();
			tapeball = CricketFunctions.extractTapeData("COMBINED_PAST_CURRENT_MATCH_DATA", cricketService, cricket_matches, session_match, null);
			Collections.sort(tapeball,new CricketFunctions.TapeBowlerWicketsComparator());
			return (List<T>) tapeball;
		case "Control_v":
			List<BestStats> log_fifty = new ArrayList<BestStats>();
			log_fifty = CricketFunctions.extractLogFifty("COMBINED_PAST_CURRENT_MATCH_DATA",CricketUtil.BOWLER,cricketService, cricket_matches, session_match, null);
			Collections.sort(log_fifty,new CricketFunctions.LogFiftyWicketsComparator());
			return (List<T>) log_fifty;
		case "Control_z": case "Control_x":
			switch (whatToProcess) {
			case "Control_z":
				FullFramesGfx.top_batsman_beststat.clear();
		        
		        List<Tournament> tournaments = CricketFunctions.extractTournamentData("CURRENT_MATCH_DATA", false, headToHead, cricketService, 
		                session_match, past_tournament_stats);
		        
		        for(Tournament tour : tournaments) {
		        	for(BestStats bs : tour.getBatsman_best_Stats()) {	
		        		
		        		if(bs.getPlayerId()== tour.getPlayerId() && FullFramesGfx.top_batsman_beststat.stream().noneMatch(ply -> ply.getMatchNumber() != null &&
		        			  Integer.parseInt(ply.getMatchNumber().replaceAll("\\D+", "")) == Integer.parseInt(session_match.getMatch()
		        			  .getMatchFileName().replaceAll("\\D+", "")) && ply.getPlayerId() == tour.getPlayerId())) {
	        			
		        			FullFramesGfx.top_batsman_beststat.add(CricketFunctions.getProcessedBatsmanBestStats(bs));
		        		}
	        		}
		        }
		        Collections.sort(FullFramesGfx.top_batsman_beststat, new CricketFunctions.BatsmanBestStatsComparator());
			 return (List<T>) FullFramesGfx.top_batsman_beststat;
				
			case "Control_x":
				List<BestStats> top_ten_beststat = new ArrayList<BestStats>();
		        for(Tournament tourn : CricketFunctions.extractTournamentData("CURRENT_MATCH_DATA", false, headToHead, cricketService, 
		                session_match, past_tournament_stats)) {
		            for(BestStats bs : tourn.getBowler_best_Stats()) {
		            	top_ten_beststat.add(CricketFunctions.getProcessedBowlerBestStats(bs));
		            }
		        }
				Collections.sort(top_ten_beststat,new CricketFunctions.BowlerBestStatsComparator());
				return (List<T>) top_ten_beststat;
			}
			return null;
		}
		return null;
	}
	
	public void GetVariousDBData(String typeOfUpdate, Configuration config) throws StreamReadException, DatabindException, 
		IllegalAccessException, InvocationTargetException, JAXBException, IOException, CloneNotSupportedException, InterruptedException, URISyntaxException
	{
		switch (config.getBroadcaster()) {
		case Constants.ICC_U19_2023: case Constants.ISPL: case Constants.BENGAL_T20: case Constants.NPL:
			
			session_statistics = cricketService.getAllStats();
			if(config.getBroadcaster().equalsIgnoreCase(Constants.ISPL)) {
				past_tape = CricketFunctions.extractTapeData("PAST_MATCHES_DATA", cricketService, cricket_matches, session_match, null);
				past_tournament_stats = CricketFunctions.extractTournamentStats("PAST_MATCHES_DATA",false, cricket_matches, cricketService, session_match, null);
			}else {
				past_tournament_stats = CricketFunctions.extractTournamentData("PAST_MATCHES_DATA", false, headToHead, cricketService, session_match, null);
			}
			
//			headToHead = CricketFunctions.extractHeadToHead(new File(CricketUtil.CRICKET_SERVER_DIRECTORY + 
//					CricketUtil.HEADTOHEAD_DIRECTORY).listFiles(), cricketService);

			session_performance_bug = cricketService.getPerformanceBugs();
			session_name_super =  cricketService.getNameSupers();
			session_team =  cricketService.getTeams();
			session_ground =  cricketService.getGrounds();
			session_bugs = cricketService.getBugs();
			session_infoBarStats = cricketService.getInfobarStats();
			session_variousText = cricketService.getVariousTexts();
			session_commentator = cricketService.getCommentator();
			session_staff = cricketService.getStaff();
			session_fixture =  CricketFunctions.processAllFixtures(cricketService);
			session_players = cricketService.getAllPlayer();
			session_pott = cricketService.getPott();
			
			if(new File(CricketUtil.CRICKET_DIRECTORY + "ParScores BB.html").exists()) {
				session_dls = CricketFunctions.populateDuckWorthLewis(session_match);
			}
			
			if(new File(CricketUtil.CRICKET_DIRECTORY + "TeamChanges.txt").exists()) {
				String text_to_return = "";
				try (BufferedReader br = new BufferedReader(new FileReader(CricketUtil.CRICKET_DIRECTORY + "TeamChanges.txt"))) {
					while((text_to_return = br.readLine()) != null) {
						if(text_to_return.contains("|")) {
							
						}else {
							if(text_to_return.contains("H") || text_to_return.contains("A")) {
								session_teamChanges.add(text_to_return);
							}
						}
					}
				}
			}
			
			switch (typeOfUpdate) {
			case "NEW":
				this_caption = new Caption(print_writers, config, session_statistics,cricketService.getAllStatsType(), cricket_matches, session_name_super,
					session_bugs,session_infoBarStats,session_fixture, session_team, session_ground,session_variousText, session_commentator, session_staff, 
					session_players, session_pott, session_teamChanges, session_performance_bug, new FullFramesGfx(),new LowerThirdGfx(), new InfobarGfx(), new BugsAndMiniGfx(), 1, "", "-", 
					past_tournament_stats,past_tape,session_dls, headToHead, past_tournament_stats, cricketService);
//				this_caption.this_infobarGfx.previous_sixes = String.valueOf(CricketFunctions.extracttournamentFoursAndSixes("COMBINED_PAST_CURRENT_MATCH_DATA", 
//					cricket_matches, session_match, null).getTournament_sixes());
				
				this_caption.this_infobarGfx.previous_sixes = String.valueOf(CricketFunctions.extracttournamentFoursAndSixesData("COMBINED_PAST_CURRENT_MATCH_DATA", 
						headToHead, session_match, null).getTournament_sixes());
						
				this_caption.this_bugsAndMiniGfx.previous_sixes =  String.valueOf(CricketFunctions.extracttournamentFoursAndSixesData("PAST_MATCHES_DATA", 
						headToHead, session_match, null).getTournament_sixes());
				
				this_caption.this_bugsAndMiniGfx.previous_fours =  String.valueOf(CricketFunctions.extracttournamentFoursAndSixesData("PAST_MATCHES_DATA", 
						headToHead, session_match, null).getTournament_fours());
				break;
			case "UPDATE":
				
				session_match = CricketFunctions.populateMatchVariables(cricketService, CricketFunctions.readOrSaveMatchFile(CricketUtil.READ, 
						CricketUtil.SETUP + "," + CricketUtil.MATCH + "," + CricketUtil.EVENT, session_match));
				
				cricket_matches = CricketFunctions.getTournamentMatches(new File(CricketUtil.CRICKET_SERVER_DIRECTORY + 
						CricketUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
					@Override
				    public boolean accept(File pathname) {
				        String name = pathname.getName().toLowerCase();
				        return name.endsWith(".json") && pathname.isFile();
				    }
				}), cricketService);

				//Bug and Mini
				this_caption.this_bugsAndMiniGfx.bugs = session_bugs;
				this_caption.this_bugsAndMiniGfx.Teams = session_team;
				this_caption.this_bugsAndMiniGfx.VariousText = session_variousText;
				this_caption.this_bugsAndMiniGfx.performanceBugs = session_performance_bug;
				
				//InfoBar
				this_caption.this_infobarGfx.statistics = session_statistics;
				this_caption.this_infobarGfx.statsTypes = cricketService.getAllStatsType();
				this_caption.this_infobarGfx.infobarStats  = session_infoBarStats;
				this_caption.this_infobarGfx.Grounds = session_ground;
				this_caption.this_infobarGfx.tournament_matches = cricket_matches;
				this_caption.this_infobarGfx.dls  = session_dls;
				this_caption.this_infobarGfx.Commentators = session_commentator;
				
				//LowerThird
				this_caption.this_lowerThirdGfx.statistics = session_statistics;
				this_caption.this_lowerThirdGfx.statsTypes = cricketService.getAllStatsType();
				this_caption.this_lowerThirdGfx.tournament_matches = cricket_matches;
				this_caption.this_lowerThirdGfx.nameSupers = session_name_super;
				this_caption.this_lowerThirdGfx.Teams = session_team;
				this_caption.this_lowerThirdGfx.Grounds = session_ground;
				this_caption.this_lowerThirdGfx.tournaments = past_tournament_stats;
				this_caption.this_lowerThirdGfx.tapeballs = past_tape;
				this_caption.this_lowerThirdGfx.dls = session_dls;
				this_caption.this_lowerThirdGfx.Staff = session_staff;
				this_caption.this_lowerThirdGfx.VariousText = session_variousText;
				this_caption.this_lowerThirdGfx.Potts = session_pott;
				
				
				//FullFrames
				this_caption.this_fullFramesGfx.statistics = session_statistics;
				this_caption.this_fullFramesGfx.statsTypes = cricketService.getAllStatsType();
				this_caption.this_fullFramesGfx.tournament_matches = cricket_matches;
				this_caption.this_fullFramesGfx.fixTures = session_fixture;
				this_caption.this_fullFramesGfx.Teams = session_team;
				this_caption.this_fullFramesGfx.Grounds = session_ground;
				this_caption.this_fullFramesGfx.tournaments = past_tournament_stats;
				this_caption.this_fullFramesGfx.VariousText = session_variousText;
				this_caption.this_fullFramesGfx.Potts = session_pott;
				if(new File(CricketUtil.CRICKET_DIRECTORY + "TeamChanges.txt").exists()) {
					String text_to_return = "";
					this_caption.this_fullFramesGfx.TeamChanges.clear();
					try (BufferedReader br = new BufferedReader(new FileReader(CricketUtil.CRICKET_DIRECTORY + "TeamChanges.txt"))) {
						while((text_to_return = br.readLine()) != null) {
							if(text_to_return.contains("|")) {
								
							}else {
								if(text_to_return.contains("H") || text_to_return.contains("A")) {
									this_caption.this_fullFramesGfx.TeamChanges.add(text_to_return);
								}
							}
						}
					}
				}
				
				break;
			}
			break;
		}
	}
	
}