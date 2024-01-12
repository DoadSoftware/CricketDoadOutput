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
import com.cricket.util.CricketUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class Caption 
{
	private boolean specialBug_on_screen = false;
	
	public InfobarGfx this_infobarGfx = new InfobarGfx();
	public BugsAndMiniGfx this_bugsAndMiniGfx = new BugsAndMiniGfx();
	public LowerThirdGfx this_lowerThirdGfx;
	public FullFramesGfx this_fullFramesGfx;
	public Animation this_anim = new Animation();
	
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
	
	public boolean isSpecialBug_on_screen() {
		return specialBug_on_screen;
	}

	public void setSpecialBug_on_screen(boolean specialBug_on_screen) {
		this.specialBug_on_screen = specialBug_on_screen;
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
		this.this_infobarGfx = new InfobarGfx(config, slashOrDash, print_writers, statistics, statsTypes, tournament_matches);
		this.this_bugsAndMiniGfx = new BugsAndMiniGfx(print_writers, config, tournament_matches, bugs, Teams, Grounds);
		this.status = "";
		this.specialBug_on_screen = false;
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
				this_fullFramesGfx.whichSponsor = whatToProcess.split(",")[2];
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
				this_infobarGfx.infobar.setPowerplay_on_screen(false);
				status = this_infobarGfx.populateInfobar(print_writers,whatToProcess,matchAllData);
				break;
			case "Control_F1":// Photo ScoreCard
				status = this_fullFramesGfx.PopulatePhotoScorecardFF(whichSide, whatToProcess.split(",")[0], matchAllData, 
						Integer.valueOf(whatToProcess.split(",")[1]));
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
			case "j": //NameSuper DB
				status = this_lowerThirdGfx.populateLTNameSuperSingle(whatToProcess,whichSide);
				break;
			case "k": //DataBase
				status = this_bugsAndMiniGfx.bugsDB(whatToProcess,whichSide,matchAllData);
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
			case "Shift_F5"://Bat 012
				status = this_lowerThirdGfx.populateBatSummary(whatToProcess,whichSide,matchAllData);
				break;
			case "Shift_F9"://Ball 012
				status = this_lowerThirdGfx.populateBallSummary(whatToProcess,whichSide,matchAllData);
				break;
			case "Alt_F12"://Teams 012
				status = this_lowerThirdGfx.populateTeamSummary(whatToProcess,whichSide,matchAllData);
				break;
			case "p"://powerplay
				status = this_lowerThirdGfx.populatePowerplay(whatToProcess,whichSide,matchAllData);
				break;	
			case "Control_a"://Projected
				status = this_lowerThirdGfx.populateL3rdProjected(whatToProcess,whichSide,matchAllData);
				break;
			case "Control_F3"://Comparison
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
			case "Control_p": //Lt MATCH SUMMARY
				status = this_lowerThirdGfx.populateL3rdMatchSummary(whatToProcess,whichSide,matchAllData);
				break;
			case "Shift_K"://FF curr part
				this_fullFramesGfx.whichSponsor = whatToProcess.split(",")[2];
				status = this_fullFramesGfx.populateCurrPartnership(whichSide, whatToProcess.split(",")[0], 
					matchAllData, whichSide);
				break;
			case "Shift_O":
				status = this_bugsAndMiniGfx.bugsDismissal(whatToProcess,matchAllData,whichSide);
				break;
			case "Alt_F9": // Single Teams
				status = this_fullFramesGfx.populateSingleTeams(whichSide, whatToProcess, matchAllData, 0);
				break;
			case "Alt_k"://Curr Part
				status = this_lowerThirdGfx.populateL3rdCurrentPartnership(whatToProcess,whichSide,matchAllData);
				break;
			case "Alt_p":
				status = this_bugsAndMiniGfx.bugsToss(whatToProcess,matchAllData,whichSide);
				break;
			case "Alt_1": // Infobar Left Bottom
				this_infobarGfx.infobar.setLeft_bottom(whatToProcess.split(",")[2]);
				status = this_infobarGfx.populateVizInfobarLeftBottom(print_writers, matchAllData, whichSide);
				break;
			case "Alt_2": // Infobar Middle
				this_infobarGfx.infobar.setMiddle_section(whatToProcess.split(",")[2]);
				status = this_infobarGfx.populateVizInfobarMiddleSection(print_writers,matchAllData, whichSide);
				break;
			case "Alt_3": 
				this_infobarGfx.infobar.setMiddle_section("BAT_PROFILE_CAREER");
				this_infobarGfx.FirstPlayerId = Integer.valueOf(whatToProcess.split(",")[2]);
				this_infobarGfx.WhichProfile = whatToProcess.split(",")[3];
				status = this_infobarGfx.populateVizInfobarMiddleSection(print_writers, matchAllData, whichSide);
				break;
			case "Alt_4":
				this_infobarGfx.infobar.setMiddle_section("BALL_PROFILE_CAREER");
				this_infobarGfx.FirstPlayerId = Integer.valueOf(whatToProcess.split(",")[2]);
				this_infobarGfx.WhichProfile = whatToProcess.split(",")[3];
				status = this_infobarGfx.populateVizInfobarMiddleSection(print_writers, matchAllData, whichSide);
				break;
			case "Alt_5":
				this_infobarGfx.infobar.setMiddle_section("LAST_X_BALLS");
				this_infobarGfx.lastXballs = Integer.valueOf(whatToProcess.split(",")[2]);
				status = this_infobarGfx.populateVizInfobarMiddleSection(print_writers, matchAllData, whichSide);
				break;
			case "Alt_6":
				this_infobarGfx.infobar.setMiddle_section("BATSMAN_SPONSOR");
				this_infobarGfx.sponsor_omo = Integer.valueOf(whatToProcess.split(",")[2]);
				status = this_infobarGfx.populateVizInfobarMiddleSection(print_writers, matchAllData, whichSide);
				break;
			case "Alt_7":
				if(this_infobarGfx.infobar.getMiddle_section().equalsIgnoreCase(CricketUtil.BATSMAN)) {
					this_infobarGfx.infobar.setRight_bottom(whatToProcess.split(",")[2]);
					status = this_infobarGfx.populateVizInfobarRightBottom(print_writers, matchAllData, 1, whichSide);
				}else {
					status = "IN Alt+2 Section BASTMAN/BOWLER NOT SELECTED";
				}
				break;
			case "Alt_8":
				if(this_infobarGfx.infobar.getMiddle_section().equalsIgnoreCase(CricketUtil.BATSMAN)) {
					if(this_infobarGfx.infobar.getRight_section() != null && !this_infobarGfx.infobar.getRight_section().isEmpty()) {
						if(whatToProcess.split(",")[2].equalsIgnoreCase(CricketUtil.BOWLER)) {
							this_infobarGfx.infobar.setRight_top(whatToProcess.split(",")[2]);
							this_infobarGfx.infobar.setRight_bottom("BOWLING_END");
							
							status = this_infobarGfx.populateVizInfobarBowler(print_writers, matchAllData, 1);
							
							this_infobarGfx.infobar.setRight_section("");this_infobarGfx.infobar.setLast_right_section("");
						}else {
							this_infobarGfx.infobar.setRight_section(whatToProcess.split(",")[2]);
							status = this_infobarGfx.populateVizInfobarRightSection(print_writers, matchAllData, 1, whichSide);
						}
					}else {
						this_infobarGfx.infobar.setRight_section(whatToProcess.split(",")[2]);
						status = this_infobarGfx.populateVizInfobarRightSection(print_writers, matchAllData, 1, 1);
						
						this_infobarGfx.infobar.setRight_top("");this_infobarGfx.infobar.setRight_bottom("");
						this_infobarGfx.infobar.setLast_right_top("");;this_infobarGfx.infobar.setLast_right_bottom("");
					}
				}else {
					status = "IN Alt+2 Section BASTMAN/BOWLER NOT SELECTED";
				}
				break;
			}
		}
		
		switch (whatToProcess.split(",")[0]) {
		case "F1": case "F2": case "F4": case "Control_F1": case "Control_F7": case "Control_F8": case "Control_F10": 
		case "Shift_F10": case "Shift_F11": case "m": case "Control_d": case "Control_e": case "Control_m": 
		case "Shift_K": case "Alt_F9":
			if(status.equalsIgnoreCase(Constants.OK)) {
				status = this_anim.processFullFramesPreview(whatToProcess, print_writers, whichSide);
			}
			break;
		case "F5": case "F6": case "F7": case "F9": case "F11":
		case "Control_F5": case "Control_F9": case "Control_a":  case "Control_c":
		case "Shift_F3": case "s": case "d": case "e": case "v": case "b": case "h":
		case "p": case "Control_p": case "j":case "Alt_k":case "F8": case "F10":
			if(status.equalsIgnoreCase(Constants.OK)) {
				status = this_anim.processL3Preview(whatToProcess, print_writers, whichSide);
			}
			break;
		}
		status = Constants.OK;
	}
}