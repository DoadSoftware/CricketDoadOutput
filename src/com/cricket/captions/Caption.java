package com.cricket.captions;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.bind.JAXBException;

import com.cricket.containers.LowerThird;
import com.cricket.model.BattingCard;
import com.cricket.model.Bugs;
import com.cricket.model.Commentator;
import com.cricket.model.Configuration;
import com.cricket.model.DuckWorthLewis;
import com.cricket.model.Fixture;
import com.cricket.model.Ground;
import com.cricket.model.InfobarStats;
import com.cricket.model.Inning;
import com.cricket.model.MatchAllData;
import com.cricket.model.NameSuper;
import com.cricket.model.POTT;
import com.cricket.model.Player;
import com.cricket.model.Statistics;
import com.cricket.model.StatsType;
import com.cricket.model.Team;
import com.cricket.model.Tournament;
import com.cricket.model.VariousText;
import com.cricket.model.Staff;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;

public class Caption 
{
	public InfobarGfx this_infobarGfx;
	public BugsAndMiniGfx this_bugsAndMiniGfx;
	public LowerThirdGfx this_lowerThirdGfx;
	public FullFramesGfx this_fullFramesGfx;
	public Animation this_anim = new Animation();
	
	public List<PrintWriter> print_writers; 
	public Configuration config;
	public List<Statistics> statistics;
	public List<StatsType> statsTypes;
	public List<MatchAllData> tournament_matches;
	public List<Tournament> tournament;
	public List<NameSuper> nameSupers;
	public List<Fixture> fixTures;
	public List<Team> Teams;
	public List<Ground> Grounds;
	public List<Bugs> bugs;
	public List<InfobarStats> infobarStats;
	public List<VariousText> VariousText;
	public List<DuckWorthLewis> dls;
	public List<Commentator> Commentators;
	public List<Staff> Staff;
	public List<Player> Players;
	public List<POTT> Pott;
	public List<String> TeamChanges;
	
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
	public String WhichProfile, status;
	
	public Caption() {
		super();
	}
	
	public Caption(List<PrintWriter> print_writers, Configuration config, List<Statistics> statistics, List<StatsType> statsTypes, 
		List<MatchAllData> tournament_matches, List<NameSuper> nameSupers,List<Bugs> bugs, List<InfobarStats> infobarStats, List<Fixture> fixTures,
		List<Team> Teams, List<Ground> Grounds, List<VariousText> varioustText, List<Commentator> commentators, List<Staff> staff, List<Player> players, 
		List<POTT> pott, List<String> teamChanges, FullFramesGfx this_fullFramesGfx,LowerThirdGfx this_lowerThirdGfx, InfobarGfx this_infobarGfx, 
		BugsAndMiniGfx this_bugsAndMiniGfx, int whichSide, String whichGraphhicsOnScreen, String slashOrDash, List<Tournament> tournament,List<DuckWorthLewis> dls) {
	
		super();
		this.print_writers = print_writers;
		this.config = config;
		this.statistics = statistics;
		this.statsTypes = statsTypes;
		this.tournament_matches = tournament_matches;
		this.nameSupers = nameSupers;
		this.bugs = bugs;
		this.infobarStats = infobarStats;
		this.fixTures = fixTures;
		this.Teams = Teams;
		this.Grounds = Grounds;
		this.tournament = tournament;
		this.VariousText = varioustText;
		this.Commentators = commentators;
		this.Staff = staff;
		this.Players = players;
		this.Pott = pott;
		this.TeamChanges = teamChanges;
		
		this.dls = dls;
		this.this_fullFramesGfx = new FullFramesGfx(print_writers, config, statistics, statsTypes, tournament_matches, 
				fixTures, Teams, Grounds,tournament, VariousText, players, pott, teamChanges);
		this.this_lowerThirdGfx = new LowerThirdGfx(print_writers, config, statistics, statsTypes, tournament_matches, 
				nameSupers, Teams, Grounds, tournament, dls, staff, players, pott, varioustText);
		this.whichSide = whichSide;
		this.this_infobarGfx = new InfobarGfx(config, slashOrDash, print_writers, statistics, statsTypes, infobarStats, 
				Grounds, Commentators, tournament_matches, dls);
		this.this_bugsAndMiniGfx = new BugsAndMiniGfx(print_writers, config, bugs, Teams, VariousText);
		this.status = "";
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public void PopulateGraphics(String whatToProcess, MatchAllData matchAllData) throws InterruptedException, NumberFormatException, ParseException, CloneNotSupportedException, IOException, JAXBException, UnsupportedAudioFileException, LineUnavailableException
	{
		if(whatToProcess.contains(",")) {
			switch (whatToProcess.split(",")[0]) {
			case "Control_1":
				status = this_infobarGfx.populatebonus(whatToProcess,whichSide, matchAllData);
				break;
			case "Alt_q":
				status = this_lowerThirdGfx.populatePOTT(whatToProcess,whichSide, matchAllData);
				break;
			case "r":
				status = this_fullFramesGfx.populatePOTT(whichSide, whatToProcess.split(",")[0], matchAllData, 0);
				break;
			case "F1": // Scorecard FF
				if(config.getBroadcaster().toUpperCase().equalsIgnoreCase(Constants.ISPL)) {
					this_fullFramesGfx.WhichScoreCard = whatToProcess.split(",")[2];
				}
				status = this_fullFramesGfx.PopulateScorecardFF(whichSide, whatToProcess.split(",")[0], matchAllData, 
					Integer.valueOf(whatToProcess.split(",")[1]));
				break;
			case "F2": // Bowling FF
				status = this_fullFramesGfx.PopulateBowlingCardFF(whichSide, whatToProcess.split(",")[0], matchAllData, 
					Integer.valueOf(whatToProcess.split(",")[1]));
				break;
			case "Control_F2": // Bowling FF
				status = this_lowerThirdGfx.PopulateLTBowlingCard(whatToProcess,whichSide, matchAllData);
				break;	
			case "F4": //All Partnership
				if(config.getBroadcaster().toUpperCase().equalsIgnoreCase(Constants.ICC_U19_2023)) {
					this_fullFramesGfx.whichSponsor = whatToProcess.split(",")[2];
				}
				status = this_fullFramesGfx.populatePartnership(whichSide, whatToProcess.split(",")[0], matchAllData, 
					Integer.valueOf(whatToProcess.split(",")[1]));
				break;
			case "Alt_F11":
				status = this_fullFramesGfx.populateDoubleManhattan(whichSide, whatToProcess.split(",")[0],matchAllData,Integer.valueOf(whatToProcess.split(",")[1]));
				break;
			case "Alt_F1": // BatGriff
				status = this_lowerThirdGfx.PopulateBatBallGriff(whatToProcess,whichSide, matchAllData);
				break;
			case "Alt_F2": // BallGriff
				status = this_lowerThirdGfx.PopulateBatBallGriff(whatToProcess,whichSide, matchAllData);
				break;	
			case "F5": //BAT THIS MATCH
				status = this_lowerThirdGfx.populateBatThisMatch(whatToProcess, whichSide, matchAllData);
				break;
			case "Shift_A": //BAT THIS MATCH BOTH INNING
				status = this_lowerThirdGfx.populateBatThisMatchBoth(whatToProcess, whichSide, matchAllData);
				break;
			case "Shift_L": //BALL THIS MATCH BOTH INNING
				status = this_lowerThirdGfx.populateBallThisMatchBoth(whatToProcess, whichSide, matchAllData);
				break;
			case "Shift_J": //THIS SESSION
				status = this_lowerThirdGfx.populateThisSession(whatToProcess, whichSide, matchAllData);
				break;
			case "F6"://HowOut
				status = this_lowerThirdGfx.populateHowOut(whatToProcess,whichSide,matchAllData);
				break;
			case "Alt_F6"://HowOut both
				status = this_lowerThirdGfx.populateHowOutBoth(whatToProcess,whichSide,matchAllData);
				break;
			case "F7": case "F11": // L3rd BAT and BALL Profile
				status = this_lowerThirdGfx.PopulateL3rdPlayerProfile(whatToProcess,whichSide, matchAllData);
				break;
			case "F8": case "Alt_F8": //HOME NAMESUPER PLAYER
				status = this_lowerThirdGfx.populateLTNameSuperPlayer(whatToProcess,whichSide,matchAllData);
				break;	
			case "F9": //BOWL THIS MATCH
				status = this_lowerThirdGfx.populateBowlThisMatch(whatToProcess, whichSide, matchAllData);
				break;
			case "F10": //NameSuper DB
				status = this_lowerThirdGfx.populateLTNameSuper(whatToProcess,whichSide);
				break;
			case "Alt_a": case "Alt_s":
				status = this_lowerThirdGfx.populateLTStaff(whatToProcess,whichSide);
				break;
			case "Shift_F12":
				this_infobarGfx.infobar.setInfobar_ident_section(whatToProcess.split(",")[2]);
				status = this_infobarGfx.infoIdentSection(print_writers, whatToProcess, matchAllData, whichSide);
				break;
			case "Control_F12":
				status = this_infobarGfx.populateInfobarIdent(print_writers,whatToProcess,matchAllData,1);
				break;
			case "F12":// InfoBar
				status = this_infobarGfx.populateInfobar(print_writers,whatToProcess,matchAllData);
				break;
			case "Control_F1":// Photo ScoreCard
				status = this_fullFramesGfx.PopulatePhotoScorecardFF(whichSide, whatToProcess.split(",")[0], matchAllData, 
					Integer.valueOf(whatToProcess.split(",")[1]));
				break;
			case "Control_F5"://Batsman Style
				status = this_lowerThirdGfx.populateBattingStyle(whatToProcess,whichSide,matchAllData);
				break;
			case "Shift_F7"://Batsman Style
				status = this_lowerThirdGfx.populateBattingStyleWithPhoto(whatToProcess,whichSide,matchAllData);
				break;	
			case "Control_F7": // Double Teams
				status = this_fullFramesGfx.PopulateDoubleTeams(whichSide, whatToProcess.split(",")[0], matchAllData);
				break;
			case "Control_F8": case "Shift_F8"://Playing XI
				status = this_fullFramesGfx.populatePlayingXI(whichSide, whatToProcess.split(",")[0],
					Integer.valueOf(whatToProcess.split(",")[2]), matchAllData, 0);
				break;

			case "Alt_z": //Squad
				this_fullFramesGfx.WhichType = whatToProcess.split(",")[3];
				status = this_fullFramesGfx.populateSquad(whichSide, whatToProcess.split(",")[0],
					Integer.valueOf(whatToProcess.split(",")[2]), matchAllData, 0);
				break;
			case "Control_F9"://Bowler Style
				status = this_lowerThirdGfx.populateBowlingStyle(whatToProcess,whichSide,matchAllData);
				break;
			case "Control_F10":
				status = this_fullFramesGfx.populateManhattan(whichSide, whatToProcess.split(",")[0],matchAllData,Integer.valueOf(whatToProcess.split(",")[1]));
				break;
			case "Control_s":
				status = this_lowerThirdGfx.populateL3rdThisSeries(whatToProcess,whichSide,matchAllData);
				break;
			case "Control_f":
				status = this_lowerThirdGfx.populateL3rdThisSeries(whatToProcess,whichSide,matchAllData);
				break;
			case "Control_y":
				status = this_bugsAndMiniGfx.populatebugPowerplay(whatToProcess,whichSide ,matchAllData);
				break;	
			case "Shift_F1":
				status = this_bugsAndMiniGfx.populateMiniScorecard(whichSide, whatToProcess,matchAllData);
				break;
			case "Shift_F2":
				status = this_bugsAndMiniGfx.populateMiniBowlingcard(whichSide, whatToProcess,matchAllData);
				break;
			case "Shift_F3": //Fall of Wicket
				status = this_lowerThirdGfx.populateFOW(whatToProcess, whichSide, matchAllData);
				break;
			case "Shift_D":
				status = this_fullFramesGfx.populateTarget(whichSide, whatToProcess.split(",")[0], matchAllData, Integer.valueOf(whatToProcess.split(",")[1]));
				break;
			case "Control_b":
				status = this_fullFramesGfx.populateInAt(whichSide, whatToProcess.split(",")[0],Integer.valueOf( whatToProcess.split(",")[2]), matchAllData);
				break;
			case "Alt_m":
				status = this_fullFramesGfx.populateBatMileStone(whichSide, whatToProcess, matchAllData);
				break;
			case "Alt_n":
				status = this_fullFramesGfx.populateBowlMileStone(whichSide, whatToProcess, matchAllData);
				break;
			case "Shift_F10": //MATCH SUMMARY
				status = this_fullFramesGfx.populateWorms(whichSide, whatToProcess.split(",")[0], matchAllData, 
					Integer.valueOf(whatToProcess.split(",")[1]));
				break;
			case "Control_F11":
				status = this_fullFramesGfx.populatePreviousMatchSummary(whichSide, whatToProcess, matchAllData, 0);
				break;
			case "Shift_F11": //MATCH SUMMARY
				status = this_fullFramesGfx.populateMatchSummary(whichSide, whatToProcess.split(",")[0], matchAllData, 
					Integer.valueOf(whatToProcess.split(",")[1]));
				break;
			case "n": //MATCH SUMMARY
				status = this_lowerThirdGfx.populateBowlingStyleWithPhoto(whatToProcess,whichSide,matchAllData);
//				status = this_fullFramesGfx.populateMatchSummary(whichSide, whatToProcess.split(",")[0], matchAllData, 
//					Integer.valueOf(whatToProcess.split(",")[1]));
				break;	
			case "d": //Target
				status = this_lowerThirdGfx.populateL3rdTarget(whatToProcess, whichSide, matchAllData);
				break;
			case "e": //Equation
				status = this_lowerThirdGfx.populateL3rdEquation(whatToProcess, whichSide, matchAllData);	
				break;
			case "Shift_E": //Extras
				status = this_lowerThirdGfx.populateL3rdExtras(whatToProcess, whichSide, matchAllData);
				break;
			case "Shift_F": //wicket sequencing
				status = this_bugsAndMiniGfx.populateWicketSequencing(whatToProcess, matchAllData, whichSide);
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
			case "p": case "Control_p":// Points Table
				this_fullFramesGfx.WhichGroup = whatToProcess.split(",")[2];
				status = this_fullFramesGfx.populateFFPointsTable(whichSide,whatToProcess.split(",")[0], matchAllData, 0);
				break;
			case "z": case "x": case "c": case "v": case "Control_z": case "Control_x":
				this_fullFramesGfx.FirstPlayerId = Integer.valueOf((whatToProcess.split(",")[2]).split("_")[1]);
				status = this_fullFramesGfx.populateLeaderBoard(whichSide, whatToProcess.split(",")[0], matchAllData, 0);
				break;
			case "s": //30-50
				status = this_lowerThirdGfx.populate30_50Split(whatToProcess, whichSide, matchAllData);
				break;
			case "q"://Boundaries
				status = this_lowerThirdGfx.populateBoundaries(whatToProcess,whichSide,matchAllData);
				break;
			case "Control_q"://Boundaries
				status = this_lowerThirdGfx.populateTeamsBoundaries(whatToProcess,whichSide,matchAllData);
				break;
			case "l"://All-rounderStats
				status = this_lowerThirdGfx.populateL3rdAllRounderStats(whatToProcess,whichSide,matchAllData);
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
			case "Alt_d":// DLS Target
				status = this_lowerThirdGfx.populateDlsTarget(whatToProcess,whichSide,matchAllData);
				break;	
			case "Control_g"://powerplay Description
				status = this_lowerThirdGfx.populatePowerplay(whatToProcess,whichSide,matchAllData);
				break;
			case "Control_h"://powerplay Summary
				status = this_lowerThirdGfx.populateL3rdPowerPlay(whatToProcess,whichSide,matchAllData);
				break;
			case "a": // All Powerplay Summary 
				status = this_lowerThirdGfx.populateL3rdInningPowerPlay(whatToProcess,whichSide,matchAllData);
				break;	
//			case "n": // POWERPLAY COMPARISON 
//				status = this_lowerThirdGfx.populateL3rdAllPowerPlay(whatToProcess,whichSide,matchAllData);
//				break;	
			case "Control_a"://Projected
				status = this_lowerThirdGfx.populateL3rdProjected(whatToProcess,whichSide,matchAllData);
				break;
			case "Control_F3"://Comparison
				status = this_lowerThirdGfx.populateL3rdComparison(whatToProcess,whichSide,matchAllData);
				break;	
			case "Control_d": case "Control_e":
				status = this_fullFramesGfx.populatePlayerProfile(whichSide, whatToProcess, matchAllData, 0);
				break;
			case "Shift_P": case "Shift_Q":
				status = this_fullFramesGfx.populateThisSeries(whichSide, whatToProcess, matchAllData, 0);
				break;
			case "Control_k": //Curr Partnership
				status = this_bugsAndMiniGfx.bugsCurrPartnership(whatToProcess,matchAllData,whichSide);
				break;
			case "Shift_F4": //Curr Partnership
				status = this_bugsAndMiniGfx.bugMultiPartnership(whatToProcess,matchAllData,whichSide);
				break;
			case "Control_m": //MATCH PROMO
				status = this_fullFramesGfx.populateFFMatchPromo(whichSide, whatToProcess,matchAllData);
				break;
//			case "Control_p": //Lt MATCH SUMMARY
//				status = this_lowerThirdGfx.populateL3rdMatchSummary(whatToProcess,whichSide,matchAllData);
//				break;
			case "Shift_K"://FF curr part
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					this_fullFramesGfx.whichSponsor = whatToProcess.split(",")[2];
					break;
				}
				
				status = this_fullFramesGfx.populateCurrPartnership(whichSide, whatToProcess.split(",")[0], 
					matchAllData, whichSide);
				break;
			case "Shift_O":
				status = this_bugsAndMiniGfx.bugsDismissal(whatToProcess,matchAllData,whichSide);
				break;
			case "o":
				status = this_bugsAndMiniGfx.bugsPlayerOfMatch(whatToProcess,matchAllData,whichSide);
				break;
			case "t":
				status = this_bugsAndMiniGfx.bugsThirdUmpire(whatToProcess,matchAllData,whichSide);
				break;	
			case "Control_F6":
				status = this_lowerThirdGfx.populateQuickHowOut(whatToProcess,whichSide,matchAllData);
				break;
			case "Shift_F6":
				status = this_lowerThirdGfx.populateHowOutWithOutFielder(whatToProcess,whichSide,matchAllData);
				break;	
			case "Alt_F9": // Single Teams Career
				status = this_fullFramesGfx.populateSingleTeamsCareer(whichSide, whatToProcess, matchAllData, 0);
				break;
			case "Alt_F10"://Single Teams This Series
				status = this_fullFramesGfx.populateSingleTeamsThisSeries(whichSide, whatToProcess, matchAllData, 0);
				break;
			case "Alt_k"://Curr Part
				status = this_lowerThirdGfx.populateL3rdCurrentPartnership(whatToProcess,whichSide,matchAllData);
				break;
			case "Alt_p":
				status = this_bugsAndMiniGfx.bugsToss(whatToProcess,matchAllData,whichSide);
				break;
			case "h":
				status = this_bugsAndMiniGfx.populateBugHighlight(whatToProcess,matchAllData,whichSide, Integer.valueOf(whatToProcess.split(",")[1]));
				break;
			case "Alt_1": // Infobar Left Bottom
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ISPL:
					if(this_infobarGfx.infobar.getFull_section() != null && !this_infobarGfx.infobar.getFull_section().isEmpty()) {
						if(!this_infobarGfx.infobar.getFull_section().equalsIgnoreCase(whatToProcess.split(",")[2])) {
							whichSide = 2;
						}else {
							whichSide = 1;
						}
					}else {
						whichSide = 1;
					}
					this_infobarGfx.infobar.setFull_section(whatToProcess.split(",")[2]);
					status = this_infobarGfx.populateFullSection(print_writers, matchAllData, whichSide);
					break;

				case Constants.ICC_U19_2023:
					this_infobarGfx.infobar.setLeft_bottom(whatToProcess.split(",")[2]);
					status = this_infobarGfx.populateVizInfobarLeftBottom(print_writers, matchAllData, whichSide);
					break;
				}
				break;
			case "Shift_T":
				status = this_infobarGfx.populateTapeBall(print_writers,matchAllData);
				break;
			case "Alt_2": // Infobar Middle
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					this_infobarGfx.infobar.setMiddle_section(whatToProcess.split(",")[2]);
					status = this_infobarGfx.populateVizInfobarMiddleSection(print_writers,matchAllData, whichSide);
					break;

				case Constants.ISPL:
					if(this_infobarGfx.infobar.getMiddle_section().equalsIgnoreCase(CricketUtil.BATSMAN)) {
						this_infobarGfx.infobar.setMiddle_section(whatToProcess.split(",")[2]);
						status = this_infobarGfx.populateVizInfobarMiddleSection(print_writers,matchAllData, 1);
					}else {
						if(whatToProcess.split(",")[2].equalsIgnoreCase(CricketUtil.BATSMAN)) {
							this_infobarGfx.infobar.setMiddle_section(whatToProcess.split(",")[2]);
							status = this_infobarGfx.populateVizInfobarMiddleSection(print_writers,matchAllData, 1);
						}else {
							this_infobarGfx.infobar.setMiddle_section(whatToProcess.split(",")[2]);
							status = this_infobarGfx.populateVizInfobarMiddleSection(print_writers,matchAllData, whichSide);
						}
					}
					
					this_infobarGfx.infobar.setFull_section("");
					break;
				}
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
			case "Alt_c":
				this_infobarGfx.challengedRuns = Integer.valueOf(whatToProcess.split(",")[2]);
				status = this_infobarGfx.populateChallengedSection(false,print_writers, matchAllData, 1);
				break;
			case "Alt_6":
				this_infobarGfx.infobar.setMiddle_section("BATSMAN_SPONSOR");
				this_infobarGfx.sponsor_omo = Integer.valueOf(whatToProcess.split(",")[2]);
				status = this_infobarGfx.populateVizInfobarMiddleSection(print_writers, matchAllData, whichSide);
				break;
			case "Alt_7":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					if(this_infobarGfx.infobar.getMiddle_section().equalsIgnoreCase(CricketUtil.BATSMAN)) {
						this_infobarGfx.infobar.setRight_bottom(whatToProcess.split(",")[2]);
						status = this_infobarGfx.populateVizInfobarRightBottom(print_writers, matchAllData, 1, whichSide);
					}else {
						status = "IN Alt+2 Section BASTMAN/BOWLER NOT SELECTED";
					}
					break;
				case Constants.ISPL:
					if(this_infobarGfx.infobar.getRight_bottom().equalsIgnoreCase(CricketUtil.BOWLER)) {
						this_infobarGfx.infobar.setRight_bottom(whatToProcess.split(",")[2]);
						status = this_infobarGfx.populateVizInfobarRightBottom(print_writers, matchAllData, 1, 1);
					}else {
						if(whatToProcess.split(",")[2].equalsIgnoreCase(CricketUtil.BOWLER)) {
							this_infobarGfx.infobar.setRight_bottom(whatToProcess.split(",")[2]);
							status = this_infobarGfx.populateVizInfobarRightBottom(print_writers, matchAllData, 1, 1);
						}else {
							this_infobarGfx.infobar.setRight_bottom(whatToProcess.split(",")[2]);
							status = this_infobarGfx.populateVizInfobarRightBottom(print_writers, matchAllData, whichSide, 1);
						}
					}
					
					this_infobarGfx.infobar.setFull_section("");
//					this_infobarGfx.infobar.setRight_bottom(whatToProcess.split(",")[2]);
//					status = this_infobarGfx.populateVizInfobarRightBottom(print_writers, matchAllData, 1, whichSide);
					break;
				}
				
				break;
			case "Alt_8":
				if(this_infobarGfx.infobar.getMiddle_section().equalsIgnoreCase(CricketUtil.BATSMAN)) {
					if(this_infobarGfx.infobar.getRight_section().equalsIgnoreCase(CricketUtil.BOWLER) && 
							whatToProcess.split(",")[2].equalsIgnoreCase(CricketUtil.BOWLER)) {
						
						status = "IN Alt+8 Section BOWLER IS ALREADY SELECTED";
					}else {
						if(whatToProcess.split(",")[2].equalsIgnoreCase(CricketUtil.BOWLER)) {
							this_infobarGfx.infobar.setRight_section(CricketUtil.BOWLER);
							this_infobarGfx.infobar.setRight_bottom("BOWLING_END");
							
							status = this_infobarGfx.populateVizInfobarBowler(print_writers, matchAllData, 1);
						}else {
							if(this_infobarGfx.infobar.getRight_section().equalsIgnoreCase(CricketUtil.BOWLER)) { 
								// When Goes Bowler to Boundary/Compare Section
								this_infobarGfx.infobar.setRight_section(whatToProcess.split(",")[2]);
								status = this_infobarGfx.populateVizInfobarRightSection(print_writers, matchAllData, 1, 1);
							}else {
								if(!this_infobarGfx.infobar.getRight_section().equalsIgnoreCase(whatToProcess.split(",")[2])) {
									// Add Data in Main Side1 -> SubSide2 between Boundary and Comparison and vice-versa
									this_infobarGfx.infobar.setRight_section(whatToProcess.split(",")[2]);
									status = this_infobarGfx.populateVizInfobarRightSection(print_writers, matchAllData, 1, 2);
								}else {
									// Add Data in Main Side1 -> SubSide1  between Boundary and Comparison and vice-versa
									status = this_infobarGfx.populateVizInfobarRightSection(print_writers, matchAllData, 1, 1);
								}
							}
						}
					}
				}else {
					status = "IN Alt+2 Section BASTMAN/BOWLER NOT SELECTED";
				}
				break;
			case "Alt_9":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					this_infobarGfx.infobar.setMiddle_section("FREE_TEXT");
					this_infobarGfx.infobarStatsId = Integer.valueOf(whatToProcess.split(",")[2]);
					status = this_infobarGfx.populateVizInfobarMiddleSection(print_writers, matchAllData, whichSide);
					break;
				case Constants.ISPL:
					this_infobarGfx.infobar.setFull_section("FREE_TEXT");
					this_infobarGfx.infobarStatsId = Integer.valueOf(whatToProcess.split(",")[2]);
					status = this_infobarGfx.populateFullSection(print_writers, matchAllData, whichSide);
					break;	
				}
				
				break;
			case "Alt_0":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					this_infobarGfx.infobar.setMiddle_section("COMMENTATORS");
					this_infobarGfx.Comms_Name = whatToProcess;
					status = this_infobarGfx.populateVizInfobarMiddleSection(print_writers, matchAllData, whichSide);
					break;
				case Constants.ISPL:
					this_infobarGfx.infobar.setFull_section("COMMENTATORS");
					this_infobarGfx.Comms_Name = whatToProcess;
					status = this_infobarGfx.populateFullSection(print_writers, matchAllData, whichSide);
					break;	
				}
				
				break;
			}
		}
		
		/*switch (whatToProcess.split(",")[0]) {
		case "F1": case "F2": case "F4": case "Control_F1": case "Control_F7": case "Control_F8": case "Control_F10": 
		case "Shift_F10": case "Shift_F11": case "m": case "Control_d": case "Control_e": case "Control_m": 
		case "Shift_K": case "Alt_F9":
			if(status.equalsIgnoreCase(Constants.OK)) {
				this_anim.processFullFramesPreview(whatToProcess, print_writers, whichSide);
			}
			break;
		case "F5": case "F6": case "F7": case "F9": case "F11":
		case "Control_F5": case "Control_F9": case "Control_a":  case "Control_c":
		case "Shift_F3": case "s": case "d": case "e": case "v": case "b": case "h":
		case "p": case "Control_p": case "j":case "Alt_k":case "F8": case "F10":
			if(status.equalsIgnoreCase(Constants.OK)) {
				this_anim.processL3Preview(whatToProcess, print_writers, whichSide);
			}
			break;
		}*/
	}

	@Override
	public String toString() {
		return "Caption [this_anim=" + this_anim + "]";
	}
	
}