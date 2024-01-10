package com.cricket.captions;

import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;

import com.cricket.containers.LowerThird;
import com.cricket.model.BattingCard;
import com.cricket.model.Bugs;
import com.cricket.model.Configuration;
import com.cricket.model.Fixture;
import com.cricket.model.Ground;
import com.cricket.model.Inning;
import com.cricket.model.MatchAllData;
import com.cricket.model.NameSuper;
import com.cricket.model.Player;
import com.cricket.model.Statistics;
import com.cricket.model.StatsType;
import com.cricket.model.Team;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class Caption 
{
	public InfobarGfx this_infobarGfx = new InfobarGfx();
	public BugsAndMiniGfx this_bugsAndMiniGfx = new BugsAndMiniGfx();
	public LowerThirdGfx this_lowerThirdGfx;
	public FullFramesGfx this_fullFramesGfx;
	
	public List<PrintWriter> print_writers; 
	public Configuration config;
	public List<Statistics> statistics;
	public List<StatsType> statsTypes;
	public List<MatchAllData> tournament_matches;
	public List<NameSuper> nameSupers;
	public List<Fixture> fixTures;
	public List<Team> Teams;
	public List<Ground> Grounds;
	public List<Bugs> bugs;
	
	public BattingCard battingCard;
	public Inning inning;
	public Player player;
	public Statistics stat;
	public StatsType statsType;
	public LowerThird lowerThird;
	public NameSuper namesuper;
	public Fixture fixture;
	public Team team;

	public int FirstPlayerId, SecondPlayerId, whichSide;
	public String WhichProfile, typeOfGraphics = "";
	
	private String status;
	
	public Caption() {
		super();
	}
	
	public Caption(List<PrintWriter> print_writers, Configuration config, List<Statistics> statistics,
		List<StatsType> statsTypes, List<MatchAllData> tournament_matches, List<NameSuper> nameSupers,List<Bugs> bugs,
		List<Fixture> fixTures, List<Team> Teams, List<Ground> Grounds,FullFramesGfx this_fullFramesGfx,
		LowerThirdGfx this_lowerThirdGfx, int whichSide, String whichGraphhicsOnScreen, String slashOrDash) {
	
		super();
		this.print_writers = print_writers;
		this.config = config;
		this.statistics = statistics;
		this.statsTypes = statsTypes;
		this.tournament_matches = tournament_matches;
		this.nameSupers = nameSupers;
		this.fixTures = fixTures;
		this.Teams = Teams;
		this.Grounds = Grounds;
		this.this_fullFramesGfx = new FullFramesGfx(print_writers, config, statistics, statsTypes, tournament_matches, 
				nameSupers, fixTures, Teams, Grounds);
		this.this_lowerThirdGfx = new LowerThirdGfx(print_writers, config, statistics, statsTypes, tournament_matches, 
				nameSupers, fixTures, Teams, Grounds);
		this.whichSide = whichSide;
		this.this_infobarGfx = new InfobarGfx(config, slashOrDash, print_writers);
		this.this_bugsAndMiniGfx = new BugsAndMiniGfx(print_writers, config, tournament_matches, bugs, Teams, Grounds);
		this.status = "";
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public void PopulateGraphics(String whatToProcess, MatchAllData matchAllData) 
		throws InterruptedException, JsonMappingException, JsonProcessingException, 
		NumberFormatException, ParseException
	{
		if(whatToProcess.contains(",")) {
			switch (whatToProcess.split(",")[0]) {
			case "F1": // Scorecard FF
				status = this_fullFramesGfx.PopulateScorecardFF(whichSide, whatToProcess.split(",")[0], matchAllData, 
					Integer.valueOf(whatToProcess.split(",")[1]));
				break;
			case "F2": // Bowling FF
				status = this_fullFramesGfx.PopulateBowlingCardFF(whichSide, whatToProcess.split(",")[0], matchAllData, 
					Integer.valueOf(whatToProcess.split(",")[1]));
				break;
			case "F4": //All Partnership
				status = this_fullFramesGfx.populatePartnership(whichSide, whatToProcess.split(",")[0], matchAllData, 
					Integer.valueOf(whatToProcess.split(",")[1]));
				break;
			case "F5": //BAT THIS MATCH
				status = this_lowerThirdGfx.populateBatThisMatch(whatToProcess, whichSide, matchAllData);
				break;
			case "F6"://HowOut
				status = this_lowerThirdGfx.populateHowOut(whatToProcess,whichSide,matchAllData);
				break;
			case "F7": case "F11": // L3rd BAT and BALL Profile
				status = this_lowerThirdGfx.PopulateL3rdPlayerProfile(whatToProcess,whichSide, matchAllData);
				break;
			case "F8": //NAMESUPER PLAYER
				status = this_lowerThirdGfx.populateLTNameSuperPlayer(whatToProcess,whichSide,matchAllData);
				break;
			case "F9": //BOWL THIS MATCH
				status = this_lowerThirdGfx.populateBowlThisMatch(whatToProcess, whichSide, matchAllData);
				break;
			case "F10": //NameSuper DB
				status = this_lowerThirdGfx.populateLTNameSuper(whatToProcess,whichSide);
				break;
			case "F12":// InfoBar
				status = this_infobarGfx.populateInfobar(print_writers,whatToProcess,matchAllData);
				break;
			case "Control_F5"://Batsman Style
				status = this_lowerThirdGfx.populateBattingStyle(whatToProcess,whichSide,matchAllData);
				break;
			case "Control_F7": // Double Teams
				status = this_fullFramesGfx.PopulateDoubleTeams(whichSide, whatToProcess.split(",")[0], matchAllData);
				break;
			case "Control_F8": //Playing XI
				status = this_fullFramesGfx.populatePlayingXI(whichSide, whatToProcess.split(",")[0],
					Integer.valueOf(whatToProcess.split(",")[2]), matchAllData, 0);
				break;
			case "Control_F9"://Bowler Style
				status = this_lowerThirdGfx.populateBowlingStyle(whatToProcess,whichSide,matchAllData);
				break;
			case "Control_F10":
				status = this_fullFramesGfx.populateManhattan(whichSide, whatToProcess.split(",")[0],matchAllData,Integer.valueOf(whatToProcess.split(",")[1]));
				break;
			case "Shift_F3": //Fall of Wicket
				status = this_lowerThirdGfx.populateFOW(whatToProcess, whichSide, matchAllData);
				break;
			case "Shift_F10": //MATCH SUMMARY
				status = this_fullFramesGfx.populateWorms(whichSide, whatToProcess.split(",")[0], matchAllData, 
					Integer.valueOf(whatToProcess.split(",")[1]));
				break;
			case "Shift_F11": //MATCH SUMMARY
				status = this_fullFramesGfx.populateMatchSummary(whichSide, whatToProcess.split(",")[0], matchAllData, 
					Integer.valueOf(whatToProcess.split(",")[1]));
				break;
			case "d": //Target
				status = this_lowerThirdGfx.populateL3rdTarget(whatToProcess, whichSide, matchAllData);
				break;
			case "e": //Equation
				status = this_lowerThirdGfx.populateL3rdEquation(whatToProcess, whichSide, matchAllData);	
				break;
			case "f": // Bug Batsman Score
				status = this_bugsAndMiniGfx.populateBatScore(whatToProcess, matchAllData, whichSide);
				break;
			case "g": //Bug Bowler Score
				status = this_bugsAndMiniGfx.populateBowlScore(whatToProcess, matchAllData, whichSide);
				break;
			case "k": //DataBase
				status = this_bugsAndMiniGfx.bugsDB(whatToProcess,whichSide);
				break;
			case "m": //Match id
				status = this_fullFramesGfx.populateFFMatchId(whichSide,whatToProcess.split(",")[0], matchAllData);
				break;
			case "s": //30-50
				status = this_lowerThirdGfx.populate30_50Split(whatToProcess, whichSide, matchAllData);
				break;
			case "q"://Boundaries
				status = this_lowerThirdGfx.populateBoundaries(whatToProcess,whichSide,matchAllData);
				break;
			case "v"://Bat 012
				status = this_lowerThirdGfx.populateBatSummary(whatToProcess,whichSide,matchAllData);
				break;
			case "b"://Ball 012
				status = this_lowerThirdGfx.populateBallSummary(whatToProcess,whichSide,matchAllData);
				break;
			case "h"://Teams 012
				status = this_lowerThirdGfx.populateTeamSummary(whatToProcess,whichSide,matchAllData);
				break;
			case "p"://powerplay
				status = this_lowerThirdGfx.populatePowerplay(whatToProcess,whichSide,matchAllData);
				break;	
			case "Control_a"://Projected
				status = this_lowerThirdGfx.populateL3rdProjected(whatToProcess,whichSide,matchAllData);
				break;
			case "Control_c"://Comparison
				status = this_lowerThirdGfx.populateL3rdComparison(whatToProcess,whichSide,matchAllData);
				break;	
			case "Control_d": case "Control_e":
				status = this_fullFramesGfx.populatePlayerProfile(whichSide, whatToProcess, matchAllData, 0);
				break;
			case "Control_k": //Curr Partnership
				status = this_bugsAndMiniGfx.bugsCurrPartnership(whatToProcess,matchAllData,whichSide);
				break;
			case "Control_m": //MATCH PROMO
				status = this_fullFramesGfx.populateFFMatchPromo(whichSide, whatToProcess,matchAllData);
				break;
			case "Shift_K"://FF curr part
				status = this_fullFramesGfx.populateCurrPartnership(whichSide, whatToProcess.split(",")[0], 
					matchAllData, whichSide);
				break;
			case "Shift_O":
				status = this_bugsAndMiniGfx.bugsDismissal(whatToProcess,matchAllData,whichSide);
				break;
			case "Alt_k"://Curr Part
				status = this_lowerThirdGfx.populateL3rdCurrentPartnership(whatToProcess,whichSide,matchAllData);
				break;
			case "Alt_1": // Infobar Left Bottom
				this_infobarGfx.infobar.setLeft_bottom(whatToProcess.split(",")[2]);
				status = this_infobarGfx.populateVizInfobarLeftBottom(print_writers, matchAllData, whichSide);
				break;
			case "Alt_2": // Infobar Middle
				this_infobarGfx.infobar.setMiddle_section(whatToProcess.split(",")[2]);
				status = this_infobarGfx.populateVizInfobarMiddleSection(print_writers, matchAllData, whichSide);
				break;
			}
		}
		status = Constants.OK;
	}
}