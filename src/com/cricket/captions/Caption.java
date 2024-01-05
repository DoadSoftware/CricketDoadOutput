package com.cricket.captions;

import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;

import com.cricket.containers.LowerThird;
import com.cricket.model.BattingCard;
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
	public int FirstPlayerId;
	public int SecondPlayerId;
	public String WhichProfile;
	
	public static InfobarGfx this_infobarGfx = new InfobarGfx();
	
	public List<PrintWriter> print_writers; 
	public Configuration config;
	public List<Statistics> statistics;
	public List<StatsType> statsTypes;
	public List<MatchAllData> tournament_matches;
	public List<NameSuper> nameSupers;
	public List<Fixture> fixTures;
	public List<Team> Teams;
	public List<Ground> Grounds;
	
	public BattingCard battingCard;
	public Inning inning;
	public Player player;
	public Statistics stat;
	public StatsType statsType;
	public LowerThird lowerThird;
	
	public NameSuper namesuper;
	public Fixture fixture;
	public Team team;
	
	LowerThirdGfx this_lowerThirdGfx;
	FullFramesGfx this_fullFramesGfx;
	
	public Caption() {
		super();
	}
	
	public Caption(List<PrintWriter> print_writers, Configuration config, List<Statistics> statistics,
			List<StatsType> statsTypes, List<MatchAllData> tournament_matches, List<NameSuper> nameSupers,List<Fixture> fixTures, 
			List<Team> Teams, List<Ground> Grounds,FullFramesGfx this_fullFramesGfx,LowerThirdGfx this_lowerThirdGfx) {
	
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
	}

	public boolean PopulateGraphics(String whatToProcess, int WhichSide, MatchAllData matchAllData) 
		throws InterruptedException, JsonMappingException, JsonProcessingException, NumberFormatException, ParseException
	{
		if(whatToProcess.contains(",")) {
			switch (whatToProcess.split(",")[0]) {
			case "F1": // Scorecard FF
				return this_fullFramesGfx.PopulateScorecardFF(WhichSide, whatToProcess.split(",")[0], matchAllData, Integer.valueOf(whatToProcess.split(",")[1]));
			case "F2": // Bowling FF
				return this_fullFramesGfx.PopulateBowlingCardFF(WhichSide, whatToProcess.split(",")[0], matchAllData, Integer.valueOf(whatToProcess.split(",")[1]));
			case "F4": //All Partnership
				return this_fullFramesGfx.populatePartnership(WhichSide, whatToProcess, matchAllData, Integer.valueOf(whatToProcess.split(",")[1]));
			case "F5": //BAT THIS MATCH
				return this_lowerThirdGfx.populateBatThisMatch(whatToProcess, WhichSide, matchAllData);
			case "F6"://HowOut
				return this_lowerThirdGfx.populateHowOut(whatToProcess,WhichSide,matchAllData);
			case "F7": case "F11": // L3rd BAT and BALL Profile
				return this_lowerThirdGfx.PopulateL3rdPlayerProfile(whatToProcess,WhichSide, matchAllData);
			case "F8": //NAMESUPER PLAYER
				return this_lowerThirdGfx.populateLTNameSuperPlayer(whatToProcess,WhichSide,matchAllData);
			case "F9": //BOWL THIS MATCH
				return this_lowerThirdGfx.populateBowlThisMatch(whatToProcess, WhichSide, matchAllData);
			case "F10": //NameSuper DB
				return this_lowerThirdGfx.populateLTNameSuper(whatToProcess,WhichSide);
			case "F12":// InfoBar
				return this_infobarGfx.populateInfobar(print_writers,config,whatToProcess,matchAllData);
			case "Control F5"://Batsman Style
				return this_lowerThirdGfx.populateBattingStyle(whatToProcess,WhichSide,matchAllData);
			case "Control F8": //Playing XI
				return this_fullFramesGfx.populatePlayingXI(WhichSide, whatToProcess.split(",")[0],Integer.valueOf(whatToProcess.split(",")[2]), matchAllData, 0);
			case "Control F9"://Bowler Style
				return this_lowerThirdGfx.populateBowlingStyle(whatToProcess,WhichSide,matchAllData);
			case "Shift F3": //Fall of Wicket
				return this_lowerThirdGfx.populateFOW(whatToProcess, WhichSide, matchAllData);
			case "Shift F11": //MATCH SUMMARY
				return this_fullFramesGfx.populateMatchSummary(WhichSide, whatToProcess.split(",")[0], matchAllData, Integer.valueOf(whatToProcess.split(",")[1]));
			case "d": //Target
				return this_lowerThirdGfx.populateL3rdTarget(whatToProcess, WhichSide, matchAllData);
			case "e": //Equation
				return this_lowerThirdGfx.populateL3rdEquation(whatToProcess, WhichSide, matchAllData);	
			case "m": //Match id
				return this_fullFramesGfx.populateFFMatchId(WhichSide,whatToProcess.split(",")[0], matchAllData);
			case "s": //30-50
				return this_lowerThirdGfx.populate30_50Split(whatToProcess, WhichSide, matchAllData);
			case "Control a"://Projected
				return this_lowerThirdGfx.populateL3rdProjected(whatToProcess,WhichSide,matchAllData);
			case "Control d": case "Control e":
				return this_fullFramesGfx.populatePlayerProfile(WhichSide, whatToProcess, matchAllData, 0);
			case "Control m": //MATCH PROMO
				return this_fullFramesGfx.populateFFMatchPromo(WhichSide, whatToProcess,matchAllData);
			case "Alt k"://Curr Part
				return this_lowerThirdGfx.populateL3rdCurrentPartnership(whatToProcess,WhichSide,matchAllData);
			}
		}
		return true;
	}
}