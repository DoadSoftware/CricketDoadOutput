package com.cricket.captions;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.cricket.containers.L3Griff;
import com.cricket.containers.LowerThird;
import com.cricket.model.BatBallGriff;
import com.cricket.model.BattingCard;
import com.cricket.model.BestStats;
import com.cricket.model.BowlingCard;
import com.cricket.model.Configuration;
import com.cricket.model.DuckWorthLewis;
import com.cricket.model.Event;
import com.cricket.model.Fixture;
import com.cricket.model.Ground;
import com.cricket.model.HeadToHead;
import com.cricket.model.Inning;
import com.cricket.model.MatchAllData;
import com.cricket.model.NameSuper;
import com.cricket.model.POTT;
import com.cricket.model.Partnership;
import com.cricket.model.Player;
import com.cricket.model.PowerPlays;
import com.cricket.model.Spell;
import com.cricket.model.Staff;
import com.cricket.model.Statistics;
import com.cricket.model.StatsType;
import com.cricket.model.Team;
import com.cricket.model.Tournament;
import com.cricket.model.VariousText;
import com.cricket.service.CricketService;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class LowerThirdGfx 
{
	public int FirstPlayerId;
	public int SecondPlayerId;
	public String WhichProfile, status = "";
	
	public List<PrintWriter> print_writers; 
	public Configuration config;
	public List<Statistics> statistics;
	public List<StatsType> statsTypes;
	public List<Tournament> tournaments;
	public List<BestStats> tapeballs;
	public List<MatchAllData> tournament_matches;
	public List<NameSuper> nameSupers;
	public List<Team> Teams;
	public List<Partnership> partnershipList;
	public List<Ground> Grounds;
	public List<Staff> Staff;
	public List<Player> Players;
	public List<POTT> Potts;
	public List<VariousText> VariousText;
	public List<HeadToHead> headToHead;
	public List<Tournament> past_tournament_stats;
	public List<Fixture> fixTures;
	
	public BattingCard battingCard;
	public Partnership partnership;
	public BowlingCard bowlingCard;
	public MatchAllData match;
	public Inning inning;
	public Player player;
	public Statistics stat;
	public StatsType statsType;
	public Tournament tournament;
	public BestStats tapeball;
	public LowerThird lowerThird;
	public Staff staff;
	public NameSuper namesuper;
	public Fixture fixture;
	public Ground ground;
	public Team team;
	public L3Griff l3griff;
	public VariousText variousText;
	public CricketService cricketService;
	
	public List<DuckWorthLewis> dls;
	public List<BattingCard> battingCardList = new ArrayList<BattingCard>();
	public List<BowlingCard> bowlingCardList = new ArrayList<BowlingCard>();
	public List<BestStats> top_tape = new ArrayList<BestStats>();
	public List<BestStats> top_batsman_beststats = new ArrayList<BestStats>();
	public List<BestStats> top_bowler_beststats = new ArrayList<BestStats>();
	public List<String> this_data_str = new ArrayList<String>();
	public List<BatBallGriff> griff = new ArrayList<BatBallGriff>();
	public List<Fixture> FixturesList = new ArrayList<Fixture>();
	
	public List<PowerPlays> powerplayData = new ArrayList<PowerPlays>();
	public List<BestStats> pastdata = new ArrayList<BestStats>();
	public List<Tournament> addPastDataToCurr = new ArrayList<Tournament>();
	
	public int impactOmo=0;
	
	String containerName = "",ltWhichContainer = "",surName = "", teamName = "", variousData = "",logo_name = "" , color_name = "",age = "", teamNameAsCity = "";
	int subline = 0;
	public LowerThirdGfx() {
		super();
	}
	
	public LowerThirdGfx(List<PrintWriter> print_writers, Configuration config, List<Statistics> statistics, List<StatsType> statsTypes, 
			List<MatchAllData> tournament_matches, List<NameSuper> nameSupers, List<Team> Teams, List<Ground> Grounds, 
			List<Tournament> tournaments,List<BestStats> tapeballs,List<DuckWorthLewis> dls, List<Staff> staff, List<Player> players, List<POTT> pott,
			List<VariousText> VariousText, List<HeadToHead> headToHead, List<Tournament> past_tournament_stats, CricketService cricketService,List<Fixture> fixTures) {
		super();
		this.print_writers = print_writers;
		this.config = config;
		this.statistics = statistics;
		this.statsTypes = statsTypes;
		this.tournament_matches = tournament_matches;
		this.nameSupers = nameSupers;
		this.Teams = Teams;
		this.Grounds = Grounds;
		this.tournaments = tournaments;
		this.tapeballs = tapeballs;
		this.dls = dls;
		this.Staff = staff;
		this.Players = players;
		this.Potts = pott;
		this.VariousText = VariousText;
		this.headToHead = headToHead;
		this.past_tournament_stats = past_tournament_stats;
		this.cricketService = cricketService;
		this.fixTures = fixTures;
	}
	
	public String populateInningComp(String whatToProcess, int whichSide, MatchAllData matchAllData) throws InterruptedException {
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)
				&& inn.getInningNumber() == 2).findAny().orElse(null);
		
		if(inning == null) {
			return "populateL3rdComparison: Inning is Not Found";
		}
		List<String> inning_comparison_stats = CricketFunctions.TeamStatsComparison(matchAllData);
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.BENGAL_T20:
			lowerThird = new LowerThird("BATTING SUMMARY COMPARISON",  "", "","", "",CricketFunctions.OverBalls(inning.getTotalOvers(), inning.getTotalBalls()), 3, "" ,"EVENT",
					new String[] {"0s", "1s", "2s", "3s", "4s", "6s"},
					new String[]{inning_comparison_stats.get(0).split(",")[0],inning_comparison_stats.get(0).split(",")[1],inning_comparison_stats.get(0).split(",")[2],
							inning_comparison_stats.get(0).split(",")[3],inning_comparison_stats.get(0).split(",")[4],inning_comparison_stats.get(0).split(",")[6]},
					new String[]{inning_comparison_stats.get(1).split(",")[0],inning_comparison_stats.get(1).split(",")[1],inning_comparison_stats.get(1).split(",")[2],
									inning_comparison_stats.get(1).split(",")[3],inning_comparison_stats.get(1).split(",")[4],inning_comparison_stats.get(1).split(",")[6]},
					new String[] {inning.getBowling_team().getTeamName4(),inning.getBatting_team().getTeamName4()},new String[] {"50.0","95.0","140.0","185.0","230.0","275.0"});
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],whichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(whichSide);
			setPositionOfLT(whatToProcess,whichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(whichSide, whatToProcess.split(",")[0]);
		}else {
			return status;
		}
	}
	
	public String populateNextToBat(String whatToProcess, int whichSide, MatchAllData matchAllData) throws InterruptedException {
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
		if(inning == null) {
			return "Select the current inning";
		}
		battingCardList = inning.getBattingCard().stream().filter(bc ->bc.getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)).collect(Collectors.toList());
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],whichSide);
		if(status == Constants.OK) {
			return PopulateL3rdBody(whichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateBatVsAllBowlers(String whatToProcess, int whichSide, MatchAllData matchAllData) throws InterruptedException {
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1]))
				.findAny().orElse(null);
		int batterId =Integer.valueOf(whatToProcess.split(",")[2]);
		int bowlerId = Integer.valueOf(whatToProcess.split(",")[3]), runs = 0, balls = 0;
		player = CricketFunctions.getPlayerFromMatchData(bowlerId, matchAllData);
		Player batter = CricketFunctions.getPlayerFromMatchData(batterId, matchAllData);
		team = Teams.stream().filter(tm -> tm.getTeamId() == batter.getTeamId()).findAny().orElse(null);
		if(team == null) {
			return "bat vs bowler: Team id [" + batter.getTeamId() + "] from database is returning NULL";
		}
		HashMap<Integer, int[]> map = new HashMap<>();
		int scoreType[] = new int[7];
		Arrays.fill(scoreType, 0);
		for(Event event : matchAllData.getEventFile().getEvents()) {
			if(event.getEventInningNumber() == inning.getInningNumber()) {
				if(event.getEventBowlerNo() == bowlerId && event.getEventBatterNo() == batterId) {
					switch (event.getEventType().toUpperCase()) {
					case CricketUtil.DOT:
						scoreType[0]++;
						balls++;
						 runs += event.getEventRuns();
						break;
					case CricketUtil.ONE:
						scoreType[1]++;
						balls++;
						runs += event.getEventRuns();
						break;
					case CricketUtil.TWO:
						scoreType[2]++;
						balls++;
						runs += event.getEventRuns();
						break;
					case CricketUtil.THREE:
						scoreType[3]++;
						balls++;
						runs += event.getEventRuns();
						break;
					case CricketUtil.FOUR:
						scoreType[4]++;
						balls++;
						runs += event.getEventRuns();
						break;
					case CricketUtil.SIX:
						scoreType[5]++;
						balls++;
						runs += event.getEventRuns();
						break;
	                case CricketUtil.FIVE:case CricketUtil.NINE:case CricketUtil.LOG_WICKET:
	                	balls++;
	                    runs += event.getEventRuns();
	                     break;
	      		    case CricketUtil.LEG_BYE:
	      		    	balls++;
	      		    	break;
	                case CricketUtil.LOG_ANY_BALL:
	                	 if(event.getEventRuns()>0) {
	                		 balls++;
	                		 runs += event.getEventRuns(); 
	                	 }   
	                	break;
					}
				}
			}
		}
		String[] data = new String[scoreType.length];
		for(int i = 0; i<scoreType.length-1; i++) {
			data[i] = String.valueOf(scoreType[i]);
		}
		data[6] = CricketFunctions.generateStrikeRate(runs, balls, 1);
		lowerThird = new LowerThird("", batter.getTicker_name(), player.getTicker_name(),"THIS INNING", "", "",
				2,"",team.getTeamName4(),new String[]{"0s","1s","2s","3s","4s","6s","S/R"},data,null,null,new String[] {"-400.0","-274.0","-139.0","7.0","145.0","281.0","427.0","560.0","600.0","650.0"});
				
				status = PopulateL3rdHeader(whatToProcess.split(",")[0],whichSide);
				if(status == Constants.OK) {
					HideAndShowL3rdSubStrapContainers(whichSide);
					setPositionOfLT(whatToProcess,whichSide,config,lowerThird.getNumberOfSubLines());
					return PopulateL3rdBody(whichSide, whatToProcess.split(",")[0]);
				} else {
					return status;
				}
	}
	
	public String populateInningBuilder(String whatToProcess, int whichSide, MatchAllData matchAllData) throws InterruptedException {
		int playerId = Integer.valueOf(whatToProcess.split(",")[2]);
		
		player = CricketFunctions.getPlayerFromMatchData(playerId, matchAllData);
		team = Teams.stream().filter(tm -> tm.getTeamId() == player.getTeamId()).findAny().orElse(null);
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
		battingCard = inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == playerId).findAny().orElse(null);
		String[] size = new String[CricketFunctions.getPlayerSplit(Integer.valueOf(whatToProcess.split(",")[1]), playerId, 20,battingCard.getBalls() , matchAllData, matchAllData.getEventFile().getEvents()).size()];
		List<String> data = CricketFunctions.getPlayerSplit(Integer.valueOf(whatToProcess.split(",")[1]), playerId, 20,battingCard.getBalls() , matchAllData, matchAllData.getEventFile().getEvents());
		for(int i=0; i<data.size(); i++) {
			size[i] = data.get(i);
		}
		
		lowerThird = new LowerThird("", player.getTicker_name(), "",String.valueOf(battingCard.getRuns()), String.valueOf(battingCard.getBalls()), "",
		2,"",team.getTeamName4(),size,size,new String[]{"RUNS","BALLS"},null,new String[] {"-400.0","-274.0","-139.0","7.0","145.0","281.0","427.0","560.0","600.0","650.0"});
		
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],whichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(whichSide);
			setPositionOfLT(whatToProcess,whichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(whichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
		
//		switch (config.getBroadcaster().toUpperCase()) {
//		case Constants.ICC_U19_2023:
//			lowerThird = new LowerThird("", matchAllData.getSetup().getAwayTeam().getTeamName1(), "",whichSplit, String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), "",
//					2,"",matchAllData.getSetup().getAwayTeam().getTeamName4(),splitNumber,splitData,new String[]{"FIFTIES","BALLS"},null,
//					new String[] {"-400.0","-274.0","-139.0","7.0","145.0","281.0","427.0","560.0","600.0","650.0"});
//			break;
//		case Constants.ISPL:
//			lowerThird = new LowerThird("", matchAllData.getSetup().getAwayTeam().getTeamName1(), "",whichSplit, String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), "",
//					2,"",matchAllData.getSetup().getAwayTeam().getTeamName4(),splitNumber,splitData,new String[]{"THIRTIES","BALLS"},null,
//					new String[] {"136.0","229.0","316.0","415.0","519.0","614.0","712.0","805.0","820.0","840.0"});
//			break;	
//		}
//		System.out.println("NAME : "+player.getFirstname());
//		System.out.println("RUNS : "+battingCard.getRuns());
//		System.out.println("BALLS : "+battingCard.getBalls());
//		System.out.println("DATA : "+CricketFunctions.getPlayerSplit(Integer.valueOf(whatToProcess.split(",")[1]), playerId, 20,battingCard.getBalls() , matchAllData, matchAllData.getEventFile().getEvents()).size());
		 
	}
	
	public String populateImpact(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
			if(inning == null) {
				return status;
			}
		}
		
		Player outPlayer =  cricketService.getAllPlayer().stream().filter(plyr -> plyr.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
		Player inPlayer =  cricketService.getAllPlayer().stream().filter(plyr -> plyr.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[3])).findAny().orElse(null);
		
		Team team = Teams.stream().filter(tm -> tm.getTeamId() == outPlayer.getTeamId()).findAny().orElse(null);
		
		lowerThird = new LowerThird(outPlayer.getFirstname(), outPlayer.getSurname(), outPlayer.getPhoto(), inPlayer.getFirstname(), inPlayer.getSurname(), inPlayer.getPhoto(), 2, "", team.getTeamName4(), null, null, null, null, null);
		
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateThisSession(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String dayovers = "-";
		
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
			if(inning == null) {
				return status;
			}
		}
		
		dayovers = CricketFunctions.OverBalls(0, matchAllData.getMatch().getDaysSessions().get(matchAllData.getMatch().getDaysSessions().size()-1).getTotalBalls());
		
		lowerThird = new LowerThird("THIS SESSION", "", "","", "","", 2, "",inning.getBatting_team().getTeamName4(),
				new String[] {"OVERS","RUNS","WICKETS", "OVER RATE","RUN RATE"},new String[] {dayovers,
				String.valueOf(matchAllData.getMatch().getDaysSessions().get(matchAllData.getMatch().getDaysSessions().size()-1).getTotalRuns()),
				String.valueOf(matchAllData.getMatch().getDaysSessions().get(matchAllData.getMatch().getDaysSessions().size()-1).getTotalWickets()),
				CricketFunctions.BetterOverRate(0, matchAllData.getMatch().getDaysSessions().get(matchAllData.getMatch().getDaysSessions().size()-1).getTotalBalls(), 
				(matchAllData.getMatch().getDaysSessions().get(matchAllData.getMatch().getDaysSessions().size()-1).getTotalSeconds()/60), "", false),
				CricketFunctions.generateRunRate(matchAllData.getMatch().getDaysSessions().get(matchAllData.getMatch().
				getDaysSessions().size()-1).getTotalRuns(), 0, matchAllData.getMatch().getDaysSessions().get(matchAllData.getMatch().
				getDaysSessions().size()-1).getTotalBalls(), 2, matchAllData)}, null,null,new String[] {"-517.0","-284.0","-17.0","255.0","533.0"});
		
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateSession(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String dayovers= "-";
		int runs = 0,wickets = 0;
		String header = "",run_rate = "",over_rate="";
		
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
			if(inning == null) {
				return status;
			}
		}
		
		for(int i =0; i <= matchAllData.getMatch().getDaysSessions().size() -1; i++ ) {
			
//			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + "TLogo" + CricketUtil.PNG_EXTENSION + ";");

			if(matchAllData.getMatch().getDaysSessions().get(i).getDayNumber() == Integer.valueOf(whatToProcess.split(",")[2]) && 
					matchAllData.getMatch().getDaysSessions().get(i).getSessionNumber() == Integer.valueOf(whatToProcess.split(",")[3])) {
				
				dayovers = CricketFunctions.OverBalls(0, matchAllData.getMatch().getDaysSessions().get(i).getTotalBalls());
				
				header = "DAY " + matchAllData.getMatch().getDaysSessions().get(i).getDayNumber() + " - SESSION " + 
						matchAllData.getMatch().getDaysSessions().get(i).getSessionNumber();
				
				runs = matchAllData.getMatch().getDaysSessions().get(i).getTotalRuns();
				wickets = matchAllData.getMatch().getDaysSessions().get(i).getTotalWickets();
				run_rate = CricketFunctions.generateRunRate(matchAllData.getMatch().getDaysSessions().get(i).getTotalRuns(), 0,
						matchAllData.getMatch().getDaysSessions().get(i).getTotalBalls(), 2, matchAllData);
				
				over_rate = CricketFunctions.BetterOverRate(0, matchAllData.getMatch().getDaysSessions().get(i).getTotalBalls(), 
						(matchAllData.getMatch().getDaysSessions().get(i).getTotalSeconds()/60), "", false);
				
			}
		}
		
		lowerThird = new LowerThird(header, "", "","", "","", 2, "","FLAG",
				new String[] {"OVERS","RUNS","WICKETS","OVER RATE","RUN RATE"},new String[] {dayovers,String.valueOf(runs),
				String.valueOf(wickets),over_rate,run_rate},null,null,new String[] {"-517.0","-284.0","-17.0","255.0","533.0"});
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateAllSession(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String session1_dayovers = "-",session2_dayovers = "-",session3_dayovers = "-";
		String session1_over_rate = "-",session2_over_rate = "-",session3_over_rate = "-";
		String session1_runs = "-",session2_runs = "-",session3_runs = "-",session1_wickets = "-",session2_wickets = "-",session3_wickets = "-";
		String header = "",session1_run_rate = "-",session2_run_rate = "-",session3_run_rate = "-";
		
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
			if(inning == null) {
				return status;
			}
		}
		
		for(int i =0; i <= matchAllData.getMatch().getDaysSessions().size() -1; i++) {
			
//			print_writer.println("LAYER2*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET lgTeamLogo " + logo_path + "TLogo" + CricketUtil.PNG_EXTENSION + ";");

				if(matchAllData.getMatch().getDaysSessions().get(i).getDayNumber() == Integer.valueOf(whatToProcess.split(",")[2])) {
				
				header = CricketFunctions.getnumeric(matchAllData.getMatch().getDaysSessions().get(i).getDayNumber()).toUpperCase()  + " DAY ";
				
				if(matchAllData.getMatch().getDaysSessions().get(i).getSessionNumber() == 1) {
					
					session1_dayovers = CricketFunctions.OverBalls(0, matchAllData.getMatch().getDaysSessions().get(i).getTotalBalls());
					
					session1_runs = String.valueOf(matchAllData.getMatch().getDaysSessions().get(i).getTotalRuns());
					session1_wickets = String.valueOf(matchAllData.getMatch().getDaysSessions().get(i).getTotalWickets());
					session1_run_rate = CricketFunctions.generateRunRate(matchAllData.getMatch().getDaysSessions().get(i).getTotalRuns(), 0, 
							matchAllData.getMatch().getDaysSessions().get(i).getTotalBalls(), 2, matchAllData);
					session1_over_rate = CricketFunctions.BetterOverRate(0, matchAllData.getMatch().getDaysSessions().get(i).getTotalBalls(), 
							(matchAllData.getMatch().getDaysSessions().get(i).getTotalSeconds()/60), "", false);
					
				}else if(matchAllData.getMatch().getDaysSessions().get(i).getSessionNumber() == 2) {
					
					session2_dayovers = CricketFunctions.OverBalls(0, matchAllData.getMatch().getDaysSessions().get(i).getTotalBalls());
					
					session2_runs = String.valueOf(matchAllData.getMatch().getDaysSessions().get(i).getTotalRuns());
					session2_wickets = String.valueOf(matchAllData.getMatch().getDaysSessions().get(i).getTotalWickets());
					session2_run_rate = CricketFunctions.generateRunRate(matchAllData.getMatch().getDaysSessions().get(i).getTotalRuns(), 0, 
							matchAllData.getMatch().getDaysSessions().get(i).getTotalBalls(), 2, matchAllData);
					session2_over_rate = CricketFunctions.BetterOverRate(0, matchAllData.getMatch().getDaysSessions().get(i).getTotalBalls(), 
							(matchAllData.getMatch().getDaysSessions().get(i).getTotalSeconds()/60), "", false);
					
				}else if(matchAllData.getMatch().getDaysSessions().get(i).getSessionNumber() == 3) {
					
					session3_dayovers = CricketFunctions.OverBalls(0, matchAllData.getMatch().getDaysSessions().get(i).getTotalBalls());
					
					session3_runs = String.valueOf(matchAllData.getMatch().getDaysSessions().get(i).getTotalRuns());
					session3_wickets = String.valueOf(matchAllData.getMatch().getDaysSessions().get(i).getTotalWickets());
					session3_run_rate = CricketFunctions.generateRunRate(matchAllData.getMatch().getDaysSessions().get(i).getTotalRuns(), 0, 
							matchAllData.getMatch().getDaysSessions().get(i).getTotalBalls(), 2, matchAllData);
					session3_over_rate = CricketFunctions.BetterOverRate(0, matchAllData.getMatch().getDaysSessions().get(i).getTotalBalls(), 
							(matchAllData.getMatch().getDaysSessions().get(i).getTotalSeconds()/60), "", false);
				}
			}
		}
		
		lowerThird = new LowerThird(header, "", "" , "", "", "", 4, "", "FLAG",
				new String[] {"OVERS","RUNS","WICKETS","OVER RATE","RUN RATE"},new String[] {
					String.valueOf(session1_dayovers + "/" + session2_dayovers + "/" + session3_dayovers),
					String.valueOf(session1_runs + "/" + session2_runs + "/" + session3_runs),
					String.valueOf(session1_wickets + "/" + session2_wickets + "/" + session3_wickets),
					String.valueOf(session1_over_rate+ "/" + session2_over_rate + "/" + session3_over_rate),
					String.valueOf(session1_run_rate + "/" + session2_run_rate + "/" + session3_run_rate)},
					new String[]{"1st SESSION","2nd SESSION","3rd SESSION"},null,new String[] {"-295.0","-125.0","60.0","300.0","533.0"}
		);
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateSummaryDaybyDay(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String day1_dayovers = "-",day2_dayovers = "-",day3_dayovers = "-",day4_dayovers = "-",day5_dayovers = "-";
		
		int day1_total_balls = 0,day2_total_balls = 0,day3_total_balls = 0,day4_total_balls = 0,day5_total_balls = 0,
				day1_runs = 0,day2_runs = 0,day3_runs = 0,day4_runs = 0,day5_runs = 0,day1_wickets = 0,day4_wickets = 0,
				day5_wickets = 0,day2_wickets = 0,day3_wickets = 0, row = 0;
		
		String day1_run_rate = "-",day2_run_rate = "-",day3_run_rate = "-",day4_run_rate = "-",day5_run_rate = "-";
		
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
			if(inning == null) {
				return status;
			}
		}
		
		for(int i =0; i <= matchAllData.getMatch().getDaysSessions().size() -1; i++) {
			
//			System.out.println("Day = " + matchAllData.getMatch().getDaysSessions().get(i).getDayNumber());
			if(matchAllData.getMatch().getDaysSessions().get(i).getDayNumber() == 1) {
				
				day1_runs = day1_runs + matchAllData.getMatch().getDaysSessions().get(i).getTotalRuns();
				day1_wickets = day1_wickets + matchAllData.getMatch().getDaysSessions().get(i).getTotalWickets();
				day1_total_balls = day1_total_balls + matchAllData.getMatch().getDaysSessions().get(i).getTotalBalls();
				
				day1_dayovers = CricketFunctions.OverBalls(0, day1_total_balls);
				day1_run_rate = CricketFunctions.generateRunRate(day1_runs, 0, day1_total_balls, 2, matchAllData);
				
			}else if(matchAllData.getMatch().getDaysSessions().get(i).getDayNumber() == 2) {
				
				day2_runs = day2_runs + matchAllData.getMatch().getDaysSessions().get(i).getTotalRuns();
				day2_wickets = day2_wickets + matchAllData.getMatch().getDaysSessions().get(i).getTotalWickets();
				day2_total_balls = day2_total_balls + matchAllData.getMatch().getDaysSessions().get(i).getTotalBalls();
				
				day2_dayovers = CricketFunctions.OverBalls(0, day2_total_balls);
				day2_run_rate = CricketFunctions.generateRunRate(day2_runs, 0, day2_total_balls, 2, matchAllData);
			}else if(matchAllData.getMatch().getDaysSessions().get(i).getDayNumber() == 3) {
				
				day3_runs = day3_runs + matchAllData.getMatch().getDaysSessions().get(i).getTotalRuns();
				day3_wickets = day3_wickets + matchAllData.getMatch().getDaysSessions().get(i).getTotalWickets();
				day3_total_balls = day3_total_balls + matchAllData.getMatch().getDaysSessions().get(i).getTotalBalls();
				
				day3_dayovers = CricketFunctions.OverBalls(0, day3_total_balls);
				day3_run_rate = CricketFunctions.generateRunRate(day3_runs, 0, day3_total_balls, 2, matchAllData);
			}else if(matchAllData.getMatch().getDaysSessions().get(i).getDayNumber() == 4) {
				
				day4_runs = day4_runs + matchAllData.getMatch().getDaysSessions().get(i).getTotalRuns();
				day4_wickets = day4_wickets + matchAllData.getMatch().getDaysSessions().get(i).getTotalWickets();
				day4_total_balls = day4_total_balls + matchAllData.getMatch().getDaysSessions().get(i).getTotalBalls();
				
				day4_dayovers = CricketFunctions.OverBalls(0, day4_total_balls);
				day4_run_rate = CricketFunctions.generateRunRate(day4_runs, 0, day4_total_balls, 2, matchAllData);
			}else if(matchAllData.getMatch().getDaysSessions().get(i).getDayNumber() == 5) {
				
				day5_runs = day5_runs + matchAllData.getMatch().getDaysSessions().get(i).getTotalRuns();
				day5_wickets = day5_wickets + matchAllData.getMatch().getDaysSessions().get(i).getTotalWickets();
				day5_total_balls = day5_total_balls + matchAllData.getMatch().getDaysSessions().get(i).getTotalBalls();
				
				day5_dayovers = CricketFunctions.OverBalls(0, day5_total_balls);
				day5_run_rate = CricketFunctions.generateRunRate(day5_runs, 0, day5_total_balls, 2, matchAllData);
			}
		}
		
		if(matchAllData.getMatch().getDaysSessions().size() <= 3) {
			row = 2;
		}else if(matchAllData.getMatch().getDaysSessions().size() > 3 && matchAllData.getMatch().getDaysSessions().size() <= 6) {
			row = 3;
		}else if(matchAllData.getMatch().getDaysSessions().size() > 6 && matchAllData.getMatch().getDaysSessions().size() <= 9) {
			row = 4;
		}else if(matchAllData.getMatch().getDaysSessions().size() > 9 && matchAllData.getMatch().getDaysSessions().size() <= 12) {
			row = 5;
		}else if(matchAllData.getMatch().getDaysSessions().size() > 12 && matchAllData.getMatch().getDaysSessions().size() <= 15) {
			row = 6;
		}
		  		
		
		lowerThird = new LowerThird("MATCH SUMMARY", "", "","DAY BY DAY", "","", row, "","FLAG",
				new String[] {"OVERS","RUNS","WICKETS","RUN RATE"},new String[] {String.valueOf(day1_dayovers + "/" + day2_dayovers + "/" + day3_dayovers + "/" + day4_dayovers + "/" + day5_dayovers),
					String.valueOf(day1_runs + "/" + day2_runs + "/" + day3_runs + "/" + day4_runs + "/" + day5_runs),
					String.valueOf(day1_wickets + "/" + day2_wickets + "/" + day3_wickets + "/" + day4_wickets + "/" + day5_wickets),
					String.valueOf(day1_run_rate + "/" + day2_run_rate + "/" + day3_run_rate + "/" + day4_run_rate + "/" + day5_run_rate)},
					new String[]{"DAY 1","DAY 2","DAY 3"},null,new String[] {"-194.0","51.0","305.0","536.0"});
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateMatchStatistics(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		DecimalFormat df = new DecimalFormat("0.00");
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) return status;
		
		int totalRuns = 0,totalWickets = 0,totalBalls = 0,rem_over = 0;
		double totalOvers = 0;
		String runRate = "-";
		
		for(int i = 0; i < matchAllData.getMatch().getInning().size(); i++) {
			totalRuns += matchAllData.getMatch().getInning().get(i).getTotalRuns();
			totalWickets += matchAllData.getMatch().getInning().get(i).getTotalWickets();
			totalOvers += matchAllData.getMatch().getInning().get(i).getTotalOvers();
			rem_over += matchAllData.getMatch().getInning().get(i).getTotalBalls();
			totalBalls += matchAllData.getMatch().getInning().get(i).getTotalBalls();
		}
		
		totalBalls = totalBalls%6;
		rem_over = rem_over/6;
		
		totalOvers = totalOvers + rem_over;
		if(totalBalls == 0) {
		}else {
			totalOvers = Double.valueOf(String.valueOf(String.valueOf((int)totalOvers) + "." + String.valueOf(totalBalls)));
		}
		
		runRate = df.format(totalRuns/totalOvers);
		
		lowerThird = new LowerThird("MATCH STATISTICS", "", "","", "","", 2, "","FLAG",
				new String[] {"RUNS","OVERS","WICKETS","RUN RATE"},new String[] {String.valueOf(totalRuns),String.valueOf(totalOvers),
				String.valueOf(totalWickets), String.valueOf(runRate)}
				,null,null,new String[] {"-517.0","-193.0","184.0","533.0"});
		
//		 CricketFunctions.getSessionPerformer(matchAllData, matchAllData.getEventFile().getEvents());

		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateTodaysMatch(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String runs = "-", overs = "-", wickets = "-", run_rate = "-";
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
			if(inning == null) {
				return status;
			}
		}
		
		overs = CricketFunctions.CurrentDayStats(matchAllData, "/", "CURRENT").split("/")[0];
		runs = CricketFunctions.CurrentDayStats(matchAllData, "/", "CURRENT").split("/")[1];
		wickets = CricketFunctions.CurrentDayStats(matchAllData, "/", "CURRENT").split("/")[2];
		
		run_rate = CricketFunctions.CurrentDayStats(matchAllData, "/", "CURRENT").split("/")[4];
		
		lowerThird = new LowerThird("TODAY'S MATCH", "", "","", "","", 2, "",inning.getBatting_team().getTeamName4(),
				new String[] {"OVERS","RUNS","WICKETS","RUN RATE"},new String[] {overs,runs,wickets,run_rate}
				,null,null,new String[] {"-517.0","-193.0","184.0","533.0"});
		
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateOverRate(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String runs = "-", overs = "-", wickets = "-", run_rate = "-";
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
			if(inning == null) {
				return status;
			}
		}
		
//		runs = CricketFunctions.CurrentDayStats(matchAllData, "/", "CURRENT").split("/")[0];
//		overs = CricketFunctions.CurrentDayStats(matchAllData, "/", "CURRENT").split("/")[1];
//		wickets = CricketFunctions.CurrentDayStats(matchAllData, "/", "CURRENT").split("/")[2];
//		run_rate = CricketFunctions.CurrentDayStats(matchAllData, "/", "CURRENT").split("/")[3];
		
		lowerThird = new LowerThird("OVER RATE", "", "","", "","", 2, "",inning.getBatting_team().getTeamName4(),
				new String[] {"RUNS","OVERS","WICKETS","RUN RATE"},new String[] {runs,overs,wickets,run_rate}
				,null,null,new String[] {"-517.0","-193.0","184.0","533.0"});
		
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateL3rdBowlerSpell(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			if(inning == null) {
				return status;
			}
		}
		
		player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(whatToProcess.split(",")[2]), matchAllData);
		
		if(player == null) {
			return status;
		}
		
		for(Spell sp : inning.getSpells()) {
			if(sp.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])) {
				this_data_str.add(CricketFunctions.OverBalls(0, sp.getBalls()) + "," + sp.getMaidens() + "," + sp.getRuns() + "," + sp.getWickets() + "," + 
						CricketFunctions.getEconomy(sp.getRuns(), sp.getBalls(), 2, "-"));
			}
		}
		
		if(this_data_str == null) {
			return status;
		}
		
		lowerThird = new LowerThird(player.getFull_name(), "ALL SPELLS", "","", "","", 2, "",inning.getBowling_team().getTeamName4(),
				new String[] {"OVERS","MAIDENS","RUNS","WICKETS","ECONOMY"},new String[] {"","","","",""}
				,new String[] {"","1st","2nd","3rd"},null,new String[] {"-410.0","-185.0","60.0","300.0","533.0"});
		
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateBallThisMatchBoth(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String run = "-", over = "-", dots = "-", wickets = "-", economy = "-",teamname = "",name = "";
		String[] stats = new String[5];
		
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			
			for(Inning inn : matchAllData.getMatch().getInning()) {
				for(BowlingCard boc : inn.getBowlingCard()) {
					if(boc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])) {
						teamname = inn.getBowling_team().getTeamName4();
						name = boc.getPlayer().getFirstname();
						run = run + "," + String.valueOf(boc.getRuns());
						over = over + "," + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls());
						dots = dots + "," + String.valueOf(boc.getDots());
						wickets = wickets + "," + String.valueOf(boc.getWickets());
						
						
						if(boc.getEconomyRate().equalsIgnoreCase("0.00")) {
							economy = economy + "," + "-";
						}else {
							economy = economy + "," + String.valueOf(boc.getEconomyRate());
						}
						
						if(boc.getPlayer().getSurname() == null) {
							surName = "";
						}else {
							surName = boc.getPlayer().getSurname();
						}
					}
				}
			}
		}
		
		stats [0] = run.replaceFirst("-,", "");
		stats [1] = over.replaceFirst("-,", "");
		stats [2] = dots.replaceFirst("-,", "");
		stats [3] = wickets.replaceFirst("-,", "");
		stats [4] = economy.replaceFirst("-,", "");
		
		lowerThird = new LowerThird("", name, surName,"", "",
				"", 3, "",teamname,new String[] {"OVERS", "MAIDEN", "RUNS", "WICKETS", "ECONOMY"},stats
				,new String[]{"1st Inning","2nd Inning"},null,new String[] {"-259.0","-77.0","109.0","312.0","533.0"});
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(5, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
//			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 4,print_writers, config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	public String populateBatThisMatchBoth(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String run = "-", ball = "-", four = "-", six = "-", strike = "-",teamname = "",name = "";
		String[] stats = new String[5];
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			for(Inning inn : matchAllData.getMatch().getInning()) {
				for(BattingCard bc : inn.getBattingCard()) {
					if(bc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])) {
						teamname = inn.getBatting_team().getTeamName4();
						name = bc.getPlayer().getFirstname();
						run = run + "," + String.valueOf(bc.getRuns());
						ball = ball + "," + String.valueOf(bc.getBalls());
						four = four + "," + String.valueOf(bc.getFours());
						six = six + "," + String.valueOf(bc.getSixes());
						
						
						if(bc.getStrikeRate().trim().isEmpty()) {
							strike = strike + "," + "-";
						}else {
							strike = strike + "," + bc.getStrikeRate();
						}
						
						if(bc.getPlayer().getSurname() == null) {
							surName = "";
						}else {
							surName = bc.getPlayer().getSurname();
						}
					}
				}
			}
		}
		
		stats [0] = run.replaceFirst("-,", "");
		stats [1] = ball.replaceFirst("-,", "");
		stats [2] = four.replaceFirst("-,", "");
		stats [3] = six.replaceFirst("-,", "");
		stats [4] = strike.replaceFirst("-,", "");
		
		lowerThird = new LowerThird("", name, surName,"", "",
				"", 3, "",teamname,new String[] {"RUNS","BALLS","FOURS","SIXES","STRIKE RATE"},stats
				,new String[]{"1st Inning","2nd Inning"},null,new String[] {"-259.0","-77.0","109.0","312.0","533.0"});
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(4, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
//			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 4,print_writers, config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateHowOutBoth(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String run = "-", ball = "-",teamname = "",name = "", howOut = "-", howOut_text = "";
		String[] stats = new String[3];
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			for(Inning inn : matchAllData.getMatch().getInning()) {
				for(BattingCard bc : inn.getBattingCard()) {
					if(bc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])) {
						teamname = inn.getBatting_team().getTeamName4();
						name = bc.getPlayer().getFirstname();
						run = run + "," + String.valueOf(bc.getRuns());
						ball = ball + "," + String.valueOf(bc.getBalls());

						if(bc.getPlayer().getSurname() == null) {
							surName = "";
						}else {
							surName = bc.getPlayer().getSurname();
						}
						
						if(bc.getHowOut()!=null) {
							if(bc.getHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
								if(bc.getWasHowOutFielderSubstitute() != null && 
										bc.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
									howOut_text = "run out " + "sub (" + bc.getHowOutFielder().getTicker_name() + ")";
								}else {
									howOut_text = "run out (" + bc.getHowOutFielder().getTicker_name() + ")";
								}
							}
							else if(bc.getHowOut().equalsIgnoreCase(CricketUtil.CAUGHT)) {
								if(bc.getWasHowOutFielderSubstitute() != null && 
										bc.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
									howOut_text = "c" +  " sub (" + bc.getHowOutFielder().getTicker_name() + ")  b " + 
											bc.getHowOutBowler().getTicker_name();
								} else {
									howOut_text = "c " + bc.getHowOutFielder().getTicker_name() + "  b " + 
											bc.getHowOutBowler().getTicker_name();
								}
							}else if(bc.getHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
								howOut_text = "RETIRED HURT";
							}else {
								if(!bc.getHowOutPartOne().isEmpty()) {
									howOut_text = bc.getHowOutPartOne();
								}
								
								if(!bc.getHowOutPartTwo().isEmpty()) {
									if(!howOut.trim().isEmpty()) {
										if(howOut_text.contains("-")) {
											howOut_text = "";
										}
										howOut_text = howOut_text + "  " + bc.getHowOutPartTwo();
									}else {
										howOut_text = bc.getHowOutPartTwo();
									}
								}
							}
							howOut = howOut + "," + howOut_text;
						}else {
							howOut_text = "-";
							howOut = howOut + "," + howOut_text;
						}
					}
				}
			}
		}
		
		stats [0] = howOut.replaceFirst("-,", "");
		stats [1] = run.replaceFirst("-,", "");
		stats [2] = ball.replaceFirst("-,", "");
		
		lowerThird = new LowerThird("", name, surName,"", "",
				"", 2, "",teamname,null,stats,new String[]{"1st INNINGS","2nd INNINGS"},null,new String[] {"0.0","470.0","516.0"});
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(4, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
//			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 4,print_writers, config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populatePOTT(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		}
		for(Player plyr : Players) {
			if(plyr.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])) {
				surName = plyr.getFull_name();
				for(Team team : Teams) {
					if(plyr.getTeamId() == team.getTeamId()) {
						teamName = team.getTeamName4();
						break;
					}
				}
			}
		}
		for(VariousText vt : VariousText) {
			if(vt.getVariousType().equalsIgnoreCase("POTT") && vt.getUseThis().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
				variousData = vt.getVariousText();
			}else if(vt.getVariousType().equalsIgnoreCase("POTT") && vt.getUseThis().toUpperCase().equalsIgnoreCase(CricketUtil.NO)) {
				variousData = "";
			}
		}
		
		lowerThird = new LowerThird("ARAMCO", surName, "",variousData, "","",2,"",teamName,null,null,null,null,null);
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateL3rdCurrentPartnership(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
			if(inning == null) {
				return status;
			}
			if(inning.getPartnerships() != null && inning.getPartnerships().size() <= 0) {
				return status;
			}
			partnership = inning.getPartnerships().stream().filter(pship -> pship.getPartnershipNumber() == 
					inning.getPartnerships().get(inning.getPartnerships().size()-1).getPartnershipNumber()).findAny().orElse(null);
			
			if(partnership == null) {
				return status;
			}
		}
		
		lowerThird = new LowerThird("", "CURRENT", "PARTNERSHIP","", String.valueOf(partnership.getTotalRuns()),String.valueOf(partnership.getTotalBalls()),2,"",
				inning.getBatting_team().getTeamName4(),null,null,new String[]{partnership.getFirstPlayer().getTicker_name(),String.valueOf(partnership.getFirstBatterRuns()),
						String.valueOf(partnership.getFirstBatterBalls())},new String[]{partnership.getSecondPlayer().getTicker_name(),
						String.valueOf(partnership.getSecondBatterRuns()),String.valueOf(partnership.getSecondBatterBalls())},null);
		
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateL3rdProjected(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == 1 &&
					inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
			if(inning == null) {
				return "Current Inning is Not 1";
			}
			this_data_str = CricketFunctions.projectedScore(matchAllData);
			if(this_data_str.size() <= 0) {
				return status;
			}
		}
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.BENGAL_T20:
			lowerThird = new LowerThird("", inning.getBatting_team().getTeamLogo(), "","PROJECTED SCORES", String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), 
					String.valueOf(CricketFunctions.OverBalls(inning.getTotalOvers(), inning.getTotalBalls())),2,"",inning.getBatting_team().getTeamName4(),new String[]{"CURRENT (" + this_data_str.get(0) + ")",this_data_str.get(2) + "/OVER"
					,this_data_str.get(4) + "/OVER",this_data_str.get(6) + "/OVER"},new String[]{this_data_str.get(1),this_data_str.get(3),this_data_str.get(5),this_data_str.get(7)},
					new String[]{"RATE","SCORE"},null,new String[] {"-253.0","33.0","277.0","533.0"});
			break;
		case Constants.ICC_U19_2023:
			lowerThird = new LowerThird("", inning.getBatting_team().getTeamName1(), "","PROJECTED SCORES", String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), 
					"",2,"",inning.getBatting_team().getTeamName4(),new String[]{"CURRENT (" + this_data_str.get(0) + ")",this_data_str.get(2) + "/OVER"
					,this_data_str.get(4) + "/OVER",this_data_str.get(6) + "/OVER"},new String[]{this_data_str.get(1),this_data_str.get(3),this_data_str.get(5),this_data_str.get(7)},
					new String[]{"RATE","SCORE"},null,new String[] {"-253.0","33.0","277.0","533.0"});
			break;
		case Constants.ISPL:
			lowerThird = new LowerThird("", inning.getBatting_team().getTeamName1(), "","PROJECTED SCORES", String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), 
					"",2,"",inning.getBatting_team().getTeamName4(),new String[]{"CURRENT (" + this_data_str.get(0) + ")",this_data_str.get(2) + "/OVER"
					,this_data_str.get(4) + "/OVER"},new String[]{this_data_str.get(1),this_data_str.get(3),this_data_str.get(5)},
					new String[]{"RATE","SCORE"},null,new String[] {"225.0","535.0","758.0"});
			break;	
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			//setStatsPositionOfLT(4, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
		
	}
	
	public String populateBattingStyleWithPhoto(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
			if(inning == null) {
				return "populateBattingStyle: Current Inning NOT found in this match";
			}
			battingCard = inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			if(battingCard == null) {
				return status;
			}
		}
		
		if(battingCard.getPlayer().getSurname() == null) {
			surName = "";
		}else {
			surName = battingCard.getPlayer().getSurname();
		}
		
		System.out.println("battingCard.getPlayer().getAge() = " + battingCard.getPlayer().getAge());
		if(battingCard.getPlayer().getAge() == 0) {
			age = "-";
		}else {
			age = String.valueOf(battingCard.getPlayer().getAge());
		}
		
		lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,"", null, null, 1,battingCard.getPlayer().getPhoto(),
			inning.getBatting_team().getTeamName4(),null,null,new String[]{CricketFunctions.getbattingstyle(battingCard.getPlayer().getBattingStyle(),
				CricketUtil.SHORT, true, false).toUpperCase(),age},null,null);
		
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return status;
		}
		
	}
	
	public String populateBowlingStyleWithPhoto(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		
		player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(whatToProcess.split(",")[2]), matchAllData);
		
		if(matchAllData.getSetup().getHomeTeamId() == player.getTeamId()) {
			team = matchAllData.getSetup().getHomeTeam();
		} else if(matchAllData.getSetup().getAwayTeamId() == player.getTeamId()) {
			team = matchAllData.getSetup().getAwayTeam();
		}
		
		if(player.getSurname() == null) {
			surName = "";
		}else {
			surName = player.getSurname();
		}
		
		if(player.getAge() == 0) {
			age = "-";
		}else {
			age = String.valueOf(player.getAge());
		}
		
		lowerThird = new LowerThird("", player.getFirstname(), surName,"", null, null,
				1,player.getPhoto(),team.getTeamName4(),null,null,new String[]{CricketFunctions.getbowlingstyle(player.getBowlingStyle()).toUpperCase(),age},
				null,null);
		
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return status;
		}
		
	}
	
	public String populateBattingStyle(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
			if(inning == null) {
				return "populateBattingStyle: Current Inning NOT found in this match";
			}
			battingCard = inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			if(battingCard == null) {
				return status;
			}
		}
		
		if (CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2])).equalsIgnoreCase(CricketUtil.YES)) {
			impactOmo = 1;
		} else if (CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(),
				inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2])).equalsIgnoreCase(CricketUtil.YES)) {
			impactOmo =1;
		} else {
			impactOmo =0;
		}
		
		if(battingCard.getPlayer().getSurname() == null) {
			surName = "";
		}else {
			surName = battingCard.getPlayer().getSurname();
		}
		
		lowerThird = new LowerThird(inning.getBatting_team().getTeamLogo(), battingCard.getPlayer().getFirstname(), surName,"", null, null, 1,"",
			inning.getBatting_team().getTeamName4(),null,null,new String[]{CricketFunctions.getbattingstyle(battingCard.getPlayer().getBattingStyle(),
				CricketUtil.SHORT, true, false).toUpperCase()},null,null);
		
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return status;
		}
		
	}
	public String populateBowlingStyle(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		
		player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(whatToProcess.split(",")[2]), matchAllData);
		
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
		if(inning == null) {
			return "populateBowlingStyle: Current Inning NOT found in this match";
		}
		
		if(matchAllData.getSetup().getHomeTeamId() == player.getTeamId()) {
			team = matchAllData.getSetup().getHomeTeam();
		} else if(matchAllData.getSetup().getAwayTeamId() == player.getTeamId()) {
			team = matchAllData.getSetup().getAwayTeam();
		}
		
		if(player.getSurname() == null) {
			surName = "";
		}else {
			surName = player.getSurname();
		}
		
		lowerThird = new LowerThird(team.getTeamLogo(), player.getFirstname(), surName,"", null, null,
				1,"",team.getTeamName4(),null,null,new String[]{CricketFunctions.getbowlingstyle(player.getBowlingStyle()).toUpperCase()},
				new String[]{whatToProcess.split(",")[3]},null);
		
		if (CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2])).equalsIgnoreCase(CricketUtil.YES)) {
			impactOmo = 1;
		} else if (CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(),
				inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2])).equalsIgnoreCase(CricketUtil.YES)) {
			impactOmo =1;
		} else {
			impactOmo =0;
		}
		
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return status;
		}
		
	}
	
	public String populateL3rdOversPerHour(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			if(inning == null) {
				return "populateL3rdOversPerHour: Current Inning NOT found in this match";
			}
			
			String[] overData = new String[inning.getInningStats().getBallsPerHour().size()];
			String[] overNumber = new String[inning.getInningStats().getBallsPerHour().size()];
			
			if(inning.getInningStats().getBallsPerHour() == null || inning.getInningStats().getBallsPerHour().size() <= 0) {
				lowerThird = new LowerThird("OVERS PER HOUR", inning.getBowling_team().getTeamName1(), "","", "", "",
					2,"",inning.getBowling_team().getTeamName4(),new String[]{" "},new String[]{" "},new String[]{"HOUR","OVER"},null,
					new String[] {"-400.0","-285.0","-165.0","-45.0","70.0","170.0","270.0","370.0","470.0","570.0"});
			}
			else if(inning.getInningStats().getBallsPerHour() != null || inning.getInningStats().getBallsPerHour().size() > 0) {
				for(int i=0;i<inning.getInningStats().getBallsPerHour().size();i++) {
					overData[i] = String.valueOf(inning.getInningStats().getBallsPerHour().get(i));
					overNumber[i] = String.valueOf(i+1);
				}
			}
			
			lowerThird = new LowerThird("OVERS PER HOUR", inning.getBowling_team().getTeamName1(), "","", "", "",
					2,"",inning.getBowling_team().getTeamName4(),overNumber,overData,new String[]{"HOUR","OVER"},null,
					new String[] {"-400.0","-285.0","-165.0","-45.0","70.0","170.0","270.0","370.0","470.0","570.0"});
			
			status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
			if(status == Constants.OK) {
				HideAndShowL3rdSubStrapContainers(WhichSide);
				setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
				return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
			} else {
				return status;
			}
		}
	}
	public String populateBatThisMatch(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException, JsonMappingException, JsonProcessingException
	{
		System.out.println(whatToProcess);
		
		String outOrNot = "",striktRate = "";
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
			if(inning == null) {
				return "populateBatThisMatch: Current Inning NOT found in this match";
			}
			battingCard = inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			if(battingCard == null) {
				return status;
			}
			if(battingCard.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
				outOrNot = "NOT OUT";
			}else {
				outOrNot = "";
			}
		}
		
		if(battingCard.getStrikeRate() !=null) {
			if(battingCard.getStrikeRate().trim().isEmpty()) {
				striktRate = "-";
			}else {
				striktRate = String.valueOf(Math.round(Float.valueOf(battingCard.getStrikeRate())));
			}
		}else {
			striktRate = "-";
		}
		
		String[] Count = CricketFunctions.getScoreTypeData(CricketUtil.BATSMAN,matchAllData, inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2]),
				"-", matchAllData.getEventFile().getEvents()).split("-");
		
		if(battingCard.getPlayer().getSurname() == null) {
			surName = "";
		}else {
			surName = battingCard.getPlayer().getSurname();
		}
		
		if (CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2])).equalsIgnoreCase(CricketUtil.YES)) {
			impactOmo = 1;
		} else if (CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(),
				inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2])).equalsIgnoreCase(CricketUtil.YES)) {
			impactOmo =1;
		} else {
			impactOmo =0;
		}
		
			if(!whatToProcess.split(",")[4].equalsIgnoreCase("NOSTATS")) {
				statsType = statsTypes.stream().filter(stype -> stype.getStats_short_name().equalsIgnoreCase(whatToProcess.split(",")[4])).findAny().orElse(null);
				if(statsTypes == null) {
					return "bat this match: Stats Type not found for profile [" + WhichProfile + "]";
				}
				stat = statistics.stream().filter(st -> st.getPlayer_id() == Integer.valueOf(whatToProcess.split(",")[2]) && statsType.getStats_id() == st.getStats_type_id()).findAny().orElse(null);
				if(stat == null) {
					return "bat this match : No stats found for player id [" + whatToProcess.split(",")[2] + "] from database is returning NULL";
				}
				stat.setStats_type(statsType);
				
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					switch (whatToProcess.split(",")[4].toUpperCase()) {
					case "DT20":
						stat = CricketFunctions.updateTournamentWithH2h(stat, headToHead, matchAllData);
						stat = CricketFunctions.updateStatisticsWithMatchData(stat, matchAllData);
						break;
					}
					break;
				}
			}
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.BENGAL_T20:
			String sponsorOrNot = whatToProcess.split(",")[3];
			if(stat!=null) {
				lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,outOrNot, String.valueOf(battingCard.getRuns()),
						"", 3, sponsorOrNot,inning.getBatting_team().getTeamName4(),new String[] {"BALLS","DOTS","FOURS","SIXES","STRIKE RATE"},new String[] {String.valueOf(battingCard.getBalls()),
						Count[0],String.valueOf(battingCard.getFours()),String.valueOf(battingCard.getSixes()),striktRate},
						new String[] {stat.getStats_type().getStats_short_name(),String.valueOf(stat.getMatches()),String.valueOf(stat.getRuns()),String.valueOf(stat.getBalls_faced())},null,
						new String[] {"-12.0","54.0","120.0","186.0","252.0"});
			}else {
				lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,outOrNot, String.valueOf(battingCard.getRuns()),
						"", 2, sponsorOrNot,inning.getBatting_team().getTeamName4(),new String[] {"BALLS","DOTS","FOURS","SIXES","STRIKE RATE"},new String[] {String.valueOf(battingCard.getBalls()),
						Count[0],String.valueOf(battingCard.getFours()),String.valueOf(battingCard.getSixes()),striktRate},null,null,new String[] {"-12.0","54.0","120.0","186.0","252.0"});
			}
			
			break;
		case Constants.ICC_U19_2023:
			lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,outOrNot, String.valueOf(battingCard.getRuns()),
					"", 2, "",inning.getBatting_team().getTeamName4(),new String[] {"BALLS","DOTS","FOURS","SIXES","STRIKE RATE"},new String[] {String.valueOf(battingCard.getBalls()),
					Count[0],String.valueOf(battingCard.getFours()),String.valueOf(battingCard.getSixes()),striktRate},null,null,
					new String[] {"-517.0","-267.0","0.0","250.0","516.0"});
			break;
		case Constants.ISPL:
			lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,outOrNot, String.valueOf(battingCard.getRuns()),
					"", 2, "",inning.getBatting_team().getTeamName4(),new String[] {"BALLS","DOTS","FOURS","SIXES","NINES","S/R"},new String[] {String.valueOf(battingCard.getBalls()),
					Count[0],String.valueOf(battingCard.getFours()),String.valueOf(battingCard.getSixes()),String.valueOf(battingCard.getNines()),striktRate},null,null,
					new String[] {"0.0","145.0","303.0","454.0","600.0","743.0"});
			break;	
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(4, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
//			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 4,print_writers, config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	public String populateBowlThisMatch(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String economy = "";
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
			if(inning == null) {
				return "populateBallThisMatch: Current Inning NOT found in this match";
			}
			bowlingCard = inning.getBowlingCard().stream().filter(boc -> boc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			if(bowlingCard == null) {
				return status;
			}
		}
		
		if(bowlingCard.getEconomyRate().equalsIgnoreCase("0.00")) {
			economy = "-";
		}else {
			economy = String.valueOf(bowlingCard.getEconomyRate());
		}
		
		if(bowlingCard.getPlayer().getSurname() == null) {
			surName = "";
		}else {
			surName = bowlingCard.getPlayer().getSurname();
		}
		if (CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2])).equalsIgnoreCase(CricketUtil.YES)) {
			impactOmo = 1;
		} else if (CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(),
				inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2])).equalsIgnoreCase(CricketUtil.YES)) {
			impactOmo =1;
		} else {
			impactOmo =0;
		}
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.BENGAL_T20:
			String sponsorOrNot = whatToProcess.split(",")[3];
			lowerThird = new LowerThird("", bowlingCard.getPlayer().getFirstname(), surName,"", "", "", 2, sponsorOrNot, inning.getBowling_team().getTeamName4(),
					new String[] {"OVERS", "MAIDENS", "RUNS", "WICKETS", "ECONOMY"},new String[]{CricketFunctions.OverBalls(bowlingCard.getOvers(), bowlingCard.getBalls()), 
					String.valueOf(bowlingCard.getMaidens()),String.valueOf(bowlingCard.getRuns()),String.valueOf(bowlingCard.getWickets()), economy}
					,null,null,new String[] {"-12.0","54.0","120.0","186.0","252.0"});
			break;
		case Constants.ICC_U19_2023:
			lowerThird = new LowerThird("", bowlingCard.getPlayer().getFirstname(), surName,"", "", "", 2, "", inning.getBowling_team().getTeamName4(),
					new String[] {"OVERS", "MAIDENS", "RUNS", "WICKETS", "ECONOMY"},new String[]{CricketFunctions.OverBalls(bowlingCard.getOvers(), bowlingCard.getBalls()), 
					String.valueOf(bowlingCard.getMaidens()),String.valueOf(bowlingCard.getRuns()),String.valueOf(bowlingCard.getWickets()), economy}
					,null,null,new String[] {"-517.0","-267.0","0.0","250.0","516.0"});
			break;
		case Constants.ISPL:
			lowerThird = new LowerThird("", bowlingCard.getPlayer().getFirstname(), surName,"", "", "", 2, "", inning.getBowling_team().getTeamName4(),
					new String[] {"OVERS", "DOTS", "RUNS", "WICKETS", "ECONOMY"},new String[]{CricketFunctions.OverBalls(bowlingCard.getOvers(), bowlingCard.getBalls()), 
					String.valueOf(bowlingCard.getDots()),String.valueOf(bowlingCard.getRuns()),String.valueOf(bowlingCard.getWickets()), economy}
					,null,null,new String[] {"0.0","185.0","368.0","539.0","755.0"});
			break;	
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(5, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
//			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 4,print_writers, config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String PopulateLTBowlingCard(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			match = matchAllData;
			if(match == null) {
				return "PopulateFfHeader: match is NULL";
			}
			
			ground = Grounds.stream().filter(grnd -> grnd.getFullname().contains(matchAllData.getSetup().getVenueName())).findAny().orElse(null);
			if(ground == null) {
				return "PopulateFfHeader: ground name is NULL";
			}
			
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
			if(inning == null) {
				return "populateBowlingCard: Current Inning NOT found in this match";
			}
		}
		
		bowlingCardList = new ArrayList<BowlingCard>();
		if(inning.getBowlingCard() != null && inning.getBowlingCard().size() > 0) {
			if(inning.getBowlingCard().size() == 1) {
				subline = 2;
			}else if(inning.getBowlingCard().size() == 2) {
				subline = 3;
			}else if(inning.getBowlingCard().size() == 3) {
				subline = 4;
			}else {
				subline = 4;
			}
			
			for(int iRow = 1; iRow <= inning.getBowlingCard().size(); iRow++) {
				if((inning.getBowlingCard().get(iRow-1).getRuns() > 0 || 
						((inning.getBowlingCard().get(iRow-1).getOvers()*6)
						+inning.getBowlingCard().get(iRow-1).getBalls()) > 0) && iRow < 4) {
					bowlingCardList.add(inning.getBowlingCard().get(iRow - 1));
				}
			}		
		}
		
		lowerThird = new LowerThird("", bowlingCardList.get(0).getPlayer().getFirstname(), "","", "", "", subline, "", inning.getBowling_team().getTeamName4(),
				new String[] {"OVERS", "MAIDENS", "RUNS", "WICKETS", "ECONOMY"},new String[]{"","","","",""}
				,new String[]{""},null,new String[] {"-264.0","-56.0","137.0","326.0","535.0"});
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(5, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
//			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 4,print_writers, config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateFOW(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1]))
				.findAny().orElse(null);
		if(inning == null) {
			return "populateFOW: Current Inning NOT found in this match";
		}else {
			String[] fowData = new String[inning.getTotalWickets()];
			String[] fowNumber = new String[inning.getTotalWickets()];
			
			if(inning.getFallsOfWickets() == null || inning.getFallsOfWickets().size() <= 0) {
				lowerThird = new LowerThird("", inning.getBatting_team().getTeamName1(), "","FALL OF WICKET", String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), "",
					2,"",inning.getBatting_team().getTeamName4(),new String[]{" "},new String[]{" "},new String[]{"WICKET","SCORE"},null,
					new String[] {"-400.0","-285.0","-165.0","-45.0","70.0","170.0","270.0","370.0","470.0","570.0"});
			}
			else if(inning.getFallsOfWickets() != null || inning.getFallsOfWickets().size() > 0) {
				for(int fow_id=0;fow_id<inning.getFallsOfWickets().size();fow_id++) {
					fowData[fow_id] = String.valueOf(inning.getFallsOfWickets().get(fow_id).getFowRuns());
					fowNumber[fow_id] = String.valueOf(fow_id+1);
				}
			}
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.BENGAL_T20:
				lowerThird = new LowerThird("FALL OF WICKET", inning.getBatting_team().getTeamLogo(), "","", String.valueOf(inning.getTotalRuns()), String.valueOf(inning.getTotalWickets()),
						2,"",inning.getBatting_team().getTeamName4(),fowNumber,fowData,new String[]{"WICKET","SCORE"},null,
						new String[] {"38.0","59.0","86.0","112.0","137.0","163.0","189.0","213.0","238.0","262.0"});
				break;
			case Constants.ICC_U19_2023:
				lowerThird = new LowerThird("FALL OF WICKET", inning.getBatting_team().getTeamName1(), "","", String.valueOf(inning.getTotalRuns()), String.valueOf(inning.getTotalWickets()),
						2,"",inning.getBatting_team().getTeamName4(),fowNumber,fowData,new String[]{"WICKET","SCORE"},null,
						new String[] {"-400.0","-285.0","-165.0","-45.0","70.0","170.0","270.0","370.0","470.0","570.0"});
				break;
			case Constants.ISPL:
				lowerThird = new LowerThird("FALL OF WICKET", inning.getBatting_team().getTeamName1(), "","", String.valueOf(inning.getTotalRuns()), String.valueOf(inning.getTotalWickets()),
						2,"",inning.getBatting_team().getTeamName4(),fowNumber,fowData,new String[]{"WICKET","SCORE"},null,
						new String[] {"85.0","162.0","241.0","321.0","400.0","478.0","556.0","632.0","710.0","789.0"});
				break;	
			}
			
			status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
			if(status == Constants.OK) {
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ISPL: case Constants.ICC_U19_2023:
					HideAndShowL3rdSubStrapContainers(WhichSide);
					setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
					break;
				}
//				setStatsPositionOfLT(inning.getTotalWickets(), 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
				
				return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
			} else {
				return status;
			}
		}
	}
	public String populate30_50Split(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String whichSplit = "";
		String[] splitData = new String[CricketFunctions.getSplit(Integer.valueOf(whatToProcess.split(",")[1]), Integer.valueOf(whatToProcess.split(",")[2]),
			matchAllData,matchAllData.getEventFile().getEvents()).size()];
		String[] splitNumber = new String[CricketFunctions.getSplit(Integer.valueOf(whatToProcess.split(",")[1]), Integer.valueOf(whatToProcess.split(",")[2]),
			matchAllData,matchAllData.getEventFile().getEvents()).size()];
		
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1]) && 
				inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES))
			.findAny().orElse(null);
		
		if(inning == null) {
			return "populate50-100: Current Inning NOT found in this match";
		}
		
		if (inning.getBowlingTeamId() == matchAllData.getSetup().getHomeTeamId() && Integer.valueOf(whatToProcess.split(",")[2]) == 30 || 
				inning.getBowlingTeamId() == matchAllData.getSetup().getHomeTeamId() && Integer.valueOf(whatToProcess.split(",")[2]) == 50) {
			
			for(int split_id=0;split_id<CricketFunctions.getSplit(Integer.valueOf(whatToProcess.split(",")[1]), Integer.valueOf(whatToProcess.split(",")[2]),
					matchAllData,matchAllData.getEventFile().getEvents()).size();split_id++) {
				splitData[split_id] = CricketFunctions.getSplit(Integer.valueOf(whatToProcess.split(",")[1]), Integer.valueOf(whatToProcess.split(",")[2])
						,matchAllData,matchAllData.getEventFile().getEvents()).get(split_id);
				splitNumber[split_id] = String.valueOf(split_id+1);
				System.out.println(splitData[split_id]);
	    	}
			
			if(Integer.valueOf(whatToProcess.split(",")[2]) == 30) {
				whichSplit = "BALLS PER THIRTY";
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					lowerThird = new LowerThird("", matchAllData.getSetup().getAwayTeam().getTeamName1(), "",whichSplit, String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), "",
							2,"",matchAllData.getSetup().getAwayTeam().getTeamName4(),splitNumber,splitData,new String[]{"THIRTIES","BALLS"},null,
							new String[] {"41.0","65.0","89.0","113.0","137.0","161.0","185.0","209.0","233.0","257.0"});
					break;
				case Constants.ICC_U19_2023:
					lowerThird = new LowerThird("", matchAllData.getSetup().getAwayTeam().getTeamName1(), "",whichSplit, String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), "",
							2,"",matchAllData.getSetup().getAwayTeam().getTeamName4(),splitNumber,splitData,new String[]{"FIFTIES","BALLS"},null,
							new String[] {"-400.0","-274.0","-139.0","7.0","145.0","281.0","427.0","560.0","600.0","650.0"});
					break;
				case Constants.ISPL:
					lowerThird = new LowerThird("", matchAllData.getSetup().getAwayTeam().getTeamName1(), "",whichSplit, String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), "",
							2,"",matchAllData.getSetup().getAwayTeam().getTeamName4(),splitNumber,splitData,new String[]{"THIRTIES","BALLS"},null,
							new String[] {"136.0","229.0","316.0","415.0","519.0","614.0","712.0","805.0","820.0","840.0"});
					break;	
				}
			}else {
				whichSplit = "BALLS PER FIFTY";
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					lowerThird = new LowerThird("", matchAllData.getSetup().getAwayTeam().getTeamName1(), "",whichSplit, String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), "",
							2,"",matchAllData.getSetup().getAwayTeam().getTeamName4(),splitNumber,splitData,new String[]{"FIFTIES","BALLS"},null,
							new String[] {"41.0","65.0","89.0","113.0","137.0","161.0","185.0","209.0","233.0","257.0"});
					break;
				case Constants.ICC_U19_2023:
					lowerThird = new LowerThird("", matchAllData.getSetup().getAwayTeam().getTeamName1(), "",whichSplit, String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), "",
							2,"",matchAllData.getSetup().getAwayTeam().getTeamName4(),splitNumber,splitData,new String[]{"HUNDREDS","BALLS"},null,
							new String[] {"-321.0","-175.0","-40.0","99.0","233.0","350.0","493.0","1016.0","1016.0","1016.0"});
					break;
				case Constants.ISPL:
					lowerThird = new LowerThird("", matchAllData.getSetup().getAwayTeam().getTeamName1(), "",whichSplit, String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), "",
							2,"",matchAllData.getSetup().getAwayTeam().getTeamName4(),splitNumber,splitData,new String[]{"FIFTIES","BALLS"},null,
							new String[] {"136.0","229.0","316.0","415.0","519.0","614.0","712.0","805.0","820.0","840.0"});
					break;	
				}
			}
		}else {
			
			for(int split_id=0;split_id<CricketFunctions.getSplit(Integer.valueOf(whatToProcess.split(",")[1]), Integer.valueOf(whatToProcess.split(",")[2]),
					matchAllData,matchAllData.getEventFile().getEvents()).size();split_id++) {
				splitData[split_id] = CricketFunctions.getSplit(Integer.valueOf(whatToProcess.split(",")[1]), Integer.valueOf(whatToProcess.split(",")[2])
						,matchAllData,matchAllData.getEventFile().getEvents()).get(split_id);
				splitNumber[split_id] = String.valueOf(split_id+1);
			}
			
			if(Integer.valueOf(whatToProcess.split(",")[2]) == 30) {
				whichSplit = "BALLS PER THIRTY";
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					lowerThird = new LowerThird("", matchAllData.getSetup().getHomeTeam().getTeamName1(), "",whichSplit, String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), "",
							2,"",matchAllData.getSetup().getHomeTeam().getTeamName4(),splitNumber,splitData,new String[]{"THIRTIES","BALLS"},null,
							new String[] {"41.0","65.0","89.0","113.0","137.0","161.0","185.0","209.0","233.0","257.0"});
					break;
				case Constants.ICC_U19_2023:
					lowerThird = new LowerThird("", matchAllData.getSetup().getHomeTeam().getTeamName1(), "",whichSplit, String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), "",
							2,"",matchAllData.getSetup().getHomeTeam().getTeamName4(),splitNumber,splitData,new String[]{"FIFTIES","BALLS"},null,
							new String[] {"-400.0","-274.0","-139.0","7.0","145.0","281.0","427.0","560.0","600.0","650.0"});
					break;
				case Constants.ISPL:
					lowerThird = new LowerThird("", matchAllData.getSetup().getHomeTeam().getTeamName1(), "",whichSplit, String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), "",
							2,"",matchAllData.getSetup().getHomeTeam().getTeamName4(),splitNumber,splitData,new String[]{"THIRTIES","BALLS"},null,
							new String[] {"136.0","229.0","316.0","415.0","519.0","614.0","712.0","805.0","820.0","840.0"});
					break;	
				}
			}else {
				whichSplit = "BALLS PER FIFTY";
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					lowerThird = new LowerThird("", matchAllData.getSetup().getHomeTeam().getTeamName1(), "",whichSplit, String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), "",
							2,"",matchAllData.getSetup().getHomeTeam().getTeamName4(),splitNumber,splitData,new String[]{"FIFTIES","BALLS"},null,
							new String[] {"41.0","65.0","89.0","113.0","137.0","161.0","185.0","209.0","233.0","257.0"});
					break;
				case Constants.ICC_U19_2023:
					lowerThird = new LowerThird("", matchAllData.getSetup().getHomeTeam().getTeamName1(), "",whichSplit, String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), "",
							2,"",matchAllData.getSetup().getHomeTeam().getTeamName4(),splitNumber,splitData,new String[]{"HUNDREDS","BALLS"},null,
							new String[] {"-321.0","-175.0","-40.0","99.0","233.0","350.0","493.0","1016.0","1016.0","1016.0"});
					break;
				case Constants.ISPL:
					lowerThird = new LowerThird("", matchAllData.getSetup().getHomeTeam().getTeamName1(), "",whichSplit, String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), "",
							2,"",matchAllData.getSetup().getHomeTeam().getTeamName4(),splitNumber,splitData,new String[]{"FIFTIES","BALLS"},null,
							new String[] {"136.0","229.0","316.0","415.0","519.0","614.0","712.0","805.0","820.0","840.0"});
					break;	
				}
			}
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(splitSize, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	public String populateL3rdExtras(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		
		boolean cr_found = false,cr1_found = false,cr2_found = false;
		String cr = "-",cr1 = "-",cr2 = "-";
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			if(whatToProcess.split(",")[2].equalsIgnoreCase("1")) {
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[2]))
						.findAny().orElse(null);
					
				if(inning == null) {
					return "populateExtras: Current Inning is Null";
				}
				
				if(matchAllData.getMatch().getInning().get(0).getTotalPenalties() > 0) {
					lowerThird = new LowerThird("INNINGS EXTRAS", "", "",whatToProcess.split(",")[2], "", "",2,"",inning.getBatting_team().getTeamName4(),new String[]{"WIDES","NO BALLS","BYES","LEG BYES","PENALTIES"},
							new String[]{String.valueOf(inning.getTotalWides()),String.valueOf(inning.getTotalNoBalls()),String.valueOf(inning.getTotalByes()),
							String.valueOf(inning.getTotalLegByes()),String.valueOf(inning.getTotalPenalties())},null,null,
							new String[] {"-516.0","-285.0","-26.0","227.0","506.0"});
				}else {
					lowerThird = new LowerThird("INNINGS EXTRAS", "", "",whatToProcess.split(",")[2], "", "",2,"",inning.getBatting_team().getTeamName4(),new String[]{"WIDES","NO BALLS","BYES","LEG BYES"},
							new String[]{String.valueOf(inning.getTotalWides()),String.valueOf(inning.getTotalNoBalls()),String.valueOf(inning.getTotalByes()),
									String.valueOf(inning.getTotalLegByes())},null,null,
							new String[] {"-516.0","-155.0","195.0","520.0"});
				}
			}else if(whatToProcess.split(",")[2].equalsIgnoreCase("2")) {
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[2]))
						.findAny().orElse(null);
					
				if(inning == null) {
					return "populateExtras: Current Inning is Null";
				}
				
				if(matchAllData.getMatch().getInning().get(1).getTotalPenalties() > 0) {
					lowerThird = new LowerThird("INNINGS EXTRAS", "", "",whatToProcess.split(",")[2], "", "",2,"",inning.getBatting_team().getTeamName4(),new String[]{"WIDES","NO BALLS","BYES","LEG BYES","PENALTIES"},
							new String[]{String.valueOf(inning.getTotalWides()),String.valueOf(inning.getTotalNoBalls()),String.valueOf(inning.getTotalByes()),
							String.valueOf(inning.getTotalLegByes()),String.valueOf(inning.getTotalPenalties())},null,null,
							new String[] {"-516.0","-285.0","-26.0","227.0","506.0"});
				}else {
					lowerThird = new LowerThird("INNINGS EXTRAS", "", "",whatToProcess.split(",")[2], "", "",2,"",inning.getBatting_team().getTeamName4(),new String[]{"WIDES","NO BALLS","BYES","LEG BYES"},
							new String[]{String.valueOf(inning.getTotalWides()),String.valueOf(inning.getTotalNoBalls()),String.valueOf(inning.getTotalByes()),
									String.valueOf(inning.getTotalLegByes())},null,null,
							new String[] {"-516.0","-155.0","195.0","520.0"});
				}
			}else {
				if(matchAllData.getMatch().getInning().get(0).getTotalPenalties() > 0 || matchAllData.getMatch().getInning().get(1).getTotalPenalties() > 0) {
					lowerThird = new LowerThird("MATCH EXTRAS", matchAllData.getSetup().getHomeTeam().getTeamName4(), matchAllData.getSetup().getAwayTeam().getTeamName4(),
							whatToProcess.split(",")[2], "", "",4,"FLAG","",new String[]{"WIDES","NO BALLS","BYES","LEG BYES","PENALTIES"},
							new String[]{String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalWides() + "," + matchAllData.getMatch().getInning().get(1).getTotalWides() + "," + 
							(matchAllData.getMatch().getInning().get(0).getTotalWides() + matchAllData.getMatch().getInning().get(1).getTotalWides())),
							String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalNoBalls() + "," + matchAllData.getMatch().getInning().get(1).getTotalNoBalls() + "," + 
							(matchAllData.getMatch().getInning().get(0).getTotalNoBalls() + matchAllData.getMatch().getInning().get(1).getTotalNoBalls())),
							String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalByes() + "," + matchAllData.getMatch().getInning().get(1).getTotalByes() + "," + 
							(matchAllData.getMatch().getInning().get(0).getTotalByes() + matchAllData.getMatch().getInning().get(1).getTotalByes())),
							String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalLegByes() + "," + matchAllData.getMatch().getInning().get(1).getTotalLegByes() + "," + 
							(matchAllData.getMatch().getInning().get(0).getTotalLegByes() + matchAllData.getMatch().getInning().get(1).getTotalLegByes())),
							String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalPenalties() + "," + matchAllData.getMatch().getInning().get(1).getTotalPenalties() + "," + 
							(matchAllData.getMatch().getInning().get(0).getTotalPenalties() + matchAllData.getMatch().getInning().get(1).getTotalPenalties()))},
							new String[]{matchAllData.getMatch().getInning().get(0).getBatting_team().getTeamName1(),
							matchAllData.getMatch().getInning().get(1).getBatting_team().getTeamName1()},null,
							new String[] {"-239.0","-58.0","135.0","321.0","525.0"});
				}else {
					lowerThird = new LowerThird("MATCH EXTRAS", matchAllData.getSetup().getHomeTeam().getTeamName4(), matchAllData.getSetup().getAwayTeam().getTeamName4(),
							whatToProcess.split(",")[2], "", "",4,"FLAG","",new String[]{"WIDES","NO BALLS","BYES","LEG BYES"},
							new String[]{String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalWides() + "," + matchAllData.getMatch().getInning().get(1).getTotalWides() + "," + 
									(matchAllData.getMatch().getInning().get(0).getTotalWides() + matchAllData.getMatch().getInning().get(1).getTotalWides())),
									String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalNoBalls() + "," + matchAllData.getMatch().getInning().get(1).getTotalNoBalls() + "," + 
									(matchAllData.getMatch().getInning().get(0).getTotalNoBalls() + matchAllData.getMatch().getInning().get(1).getTotalNoBalls())),
									String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalByes() + "," + matchAllData.getMatch().getInning().get(1).getTotalByes() + "," + 
									(matchAllData.getMatch().getInning().get(0).getTotalByes() + matchAllData.getMatch().getInning().get(1).getTotalByes())),
									String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalLegByes() + "," + matchAllData.getMatch().getInning().get(1).getTotalLegByes() + "," + 
									(matchAllData.getMatch().getInning().get(0).getTotalLegByes() + matchAllData.getMatch().getInning().get(1).getTotalLegByes()))},
							new String[]{matchAllData.getMatch().getInning().get(0).getBatting_team().getTeamName1(),
							matchAllData.getMatch().getInning().get(1).getBatting_team().getTeamName1()},null,
							new String[] {"-239.0","40.0","297.0","536.0"});
				}
			}
			break;
		case Constants.ISPL:
			if(whatToProcess.split(",")[2].equalsIgnoreCase("1")) {
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[2]))
						.findAny().orElse(null);
					
				if(inning == null) {
					return "populateExtras: Current Inning is Null";
				}
				
				if(matchAllData.getMatch().getInning().get(0).getTotalPenalties() > 0) {
					for(Event evnt : matchAllData.getEventFile().getEvents()) {
						if (evnt.getEventInningNumber() == Integer.valueOf(whatToProcess.split(",")[2])) {
							if(evnt.getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50)) {
								cr_found = true;
								cr = String.valueOf(evnt.getEventRuns());
							}
						}
					}
					
					if(cr_found == true) {
						lowerThird = new LowerThird("INNINGS EXTRAS", "", "",whatToProcess.split(",")[2], "", "",2,"",inning.getBatting_team().getTeamName4(),new String[]{"WD","NB","B","LB","P","CR"},
								new String[]{String.valueOf(inning.getTotalWides()),String.valueOf(inning.getTotalNoBalls()),String.valueOf(inning.getTotalByes()),
								String.valueOf(inning.getTotalLegByes()),String.valueOf(inning.getTotalPenalties()),cr},null,null,
								new String[] {"0.0","199.0","381.0","569.0","736.0"});
					}else {
						lowerThird = new LowerThird("INNINGS EXTRAS", "", "",whatToProcess.split(",")[2], "", "",2,"",inning.getBatting_team().getTeamName4(),new String[]{"WD","NB","B","LB","P"},
								new String[]{String.valueOf(inning.getTotalWides()),String.valueOf(inning.getTotalNoBalls()),String.valueOf(inning.getTotalByes()),
								String.valueOf(inning.getTotalLegByes()),String.valueOf(inning.getTotalPenalties())},null,null,
								new String[] {"0.0","199.0","381.0","569.0","736.0"});
					}
					
				}else {
					for(Event evnt : matchAllData.getEventFile().getEvents()) {
						if (evnt.getEventInningNumber() == Integer.valueOf(whatToProcess.split(",")[2])) {
							if(evnt.getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50)) {
								cr_found = true;
								cr = String.valueOf(evnt.getEventRuns());
							}
						}
					}
					
					if(cr_found == true) {
						lowerThird = new LowerThird("INNINGS EXTRAS", "", "",whatToProcess.split(",")[2], "", "",2,"",inning.getBatting_team().getTeamName4(),new String[]{"WD","NB","B","LB","CR"},
								new String[]{String.valueOf(inning.getTotalWides()),String.valueOf(inning.getTotalNoBalls()),String.valueOf(inning.getTotalByes()),
										String.valueOf(inning.getTotalLegByes()),cr},null,null,
								new String[] {"0.0","251.0","485.0","736.0"});
					}else {
						lowerThird = new LowerThird("INNINGS EXTRAS", "", "",whatToProcess.split(",")[2], "", "",2,"",inning.getBatting_team().getTeamName4(),new String[]{"WD","NB","B","LB"},
								new String[]{String.valueOf(inning.getTotalWides()),String.valueOf(inning.getTotalNoBalls()),String.valueOf(inning.getTotalByes()),
										String.valueOf(inning.getTotalLegByes())},null,null,
								new String[] {"0.0","251.0","485.0","736.0"});
					}
				}
			}else if(whatToProcess.split(",")[2].equalsIgnoreCase("2")) {
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[2]))
						.findAny().orElse(null);
					
				if(inning == null) {
					return "populateExtras: Current Inning is Null";
				}
				
				if(matchAllData.getMatch().getInning().get(1).getTotalPenalties() > 0) {
					for(Event evnt : matchAllData.getEventFile().getEvents()) {
						if (evnt.getEventInningNumber() == Integer.valueOf(whatToProcess.split(",")[2])) {
							if(evnt.getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50)) {
								cr_found = true;
								cr = String.valueOf(evnt.getEventRuns());
							}
						}
					}
					
					if(cr_found == true) {
						lowerThird = new LowerThird("INNINGS EXTRAS", "", "",whatToProcess.split(",")[2], "", "",2,"",inning.getBatting_team().getTeamName4(),new String[]{"WD","NB","B","LB","P","CR"},
								new String[]{String.valueOf(inning.getTotalWides()),String.valueOf(inning.getTotalNoBalls()),String.valueOf(inning.getTotalByes()),
								String.valueOf(inning.getTotalLegByes()),String.valueOf(inning.getTotalPenalties()),cr},null,null,
								new String[] {"0.0","199.0","381.0","569.0","736.0"});
					}else {
						lowerThird = new LowerThird("INNINGS EXTRAS", "", "",whatToProcess.split(",")[2], "", "",2,"",inning.getBatting_team().getTeamName4(),new String[]{"WD","NB","B","LB","P"},
								new String[]{String.valueOf(inning.getTotalWides()),String.valueOf(inning.getTotalNoBalls()),String.valueOf(inning.getTotalByes()),
								String.valueOf(inning.getTotalLegByes()),String.valueOf(inning.getTotalPenalties())},null,null,
								new String[] {"0.0","199.0","381.0","569.0","736.0"});
					}
				}else {
					for(Event evnt : matchAllData.getEventFile().getEvents()) {
						if (evnt.getEventInningNumber() == Integer.valueOf(whatToProcess.split(",")[2])) {
							if(evnt.getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50)) {
								cr_found = true;
								cr = String.valueOf(evnt.getEventRuns());
							}
						}
					}
					
					if(cr_found == true) {
						lowerThird = new LowerThird("INNINGS EXTRAS", "", "",whatToProcess.split(",")[2], "", "",2,"",inning.getBatting_team().getTeamName4(),new String[]{"WD","NB","B","LB","CR"},
								new String[]{String.valueOf(inning.getTotalWides()),String.valueOf(inning.getTotalNoBalls()),String.valueOf(inning.getTotalByes()),
										String.valueOf(inning.getTotalLegByes()),cr},null,null,
								new String[] {"0.0","251.0","485.0","736.0"});
					}else {
						lowerThird = new LowerThird("INNINGS EXTRAS", "", "",whatToProcess.split(",")[2], "", "",2,"",inning.getBatting_team().getTeamName4(),new String[]{"WD","NB","B","LB"},
								new String[]{String.valueOf(inning.getTotalWides()),String.valueOf(inning.getTotalNoBalls()),String.valueOf(inning.getTotalByes()),
										String.valueOf(inning.getTotalLegByes())},null,null,
								new String[] {"0.0","251.0","485.0","736.0"});
					}
				}
			}else {
				for(Event evnt : matchAllData.getEventFile().getEvents()) {
					if (evnt.getEventInningNumber() == 1) {
						if(evnt.getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50)) {
							cr1_found = true;
							cr1 = String.valueOf(evnt.getEventRuns());
						}
					}
					
					if (evnt.getEventInningNumber() == 2) {
						if(evnt.getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50)) {
							cr2_found = true;
							cr2 = String.valueOf(evnt.getEventRuns());
						}
					}
				}
				
				if(matchAllData.getMatch().getInning().get(0).getTotalPenalties() > 0 || matchAllData.getMatch().getInning().get(1).getTotalPenalties() > 0) {
					
					if(cr1_found == false && cr2_found == false) {
						lowerThird = new LowerThird("MATCH EXTRAS", matchAllData.getSetup().getHomeTeam().getTeamName4(), matchAllData.getSetup().getAwayTeam().getTeamName4(),
								whatToProcess.split(",")[2], "", "",4,"FLAG","",new String[]{"WD","NB","B","LB","P"},
								new String[]{String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalWides() + "," + matchAllData.getMatch().getInning().get(1).getTotalWides() + "," + 
								(matchAllData.getMatch().getInning().get(0).getTotalWides() + matchAllData.getMatch().getInning().get(1).getTotalWides())),
								String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalNoBalls() + "," + matchAllData.getMatch().getInning().get(1).getTotalNoBalls() + "," + 
								(matchAllData.getMatch().getInning().get(0).getTotalNoBalls() + matchAllData.getMatch().getInning().get(1).getTotalNoBalls())),
								String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalByes() + "," + matchAllData.getMatch().getInning().get(1).getTotalByes() + "," + 
								(matchAllData.getMatch().getInning().get(0).getTotalByes() + matchAllData.getMatch().getInning().get(1).getTotalByes())),
								String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalLegByes() + "," + matchAllData.getMatch().getInning().get(1).getTotalLegByes() + "," + 
								(matchAllData.getMatch().getInning().get(0).getTotalLegByes() + matchAllData.getMatch().getInning().get(1).getTotalLegByes())),
								String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalPenalties() + "," + matchAllData.getMatch().getInning().get(1).getTotalPenalties() + "," + 
								(matchAllData.getMatch().getInning().get(0).getTotalPenalties() + matchAllData.getMatch().getInning().get(1).getTotalPenalties()))},
								new String[]{matchAllData.getMatch().getInning().get(0).getBatting_team().getTeamName4(),
								matchAllData.getMatch().getInning().get(1).getBatting_team().getTeamName4()},null,
								new String[] {"226.0","369.0","505.0","639.0","774.0"});
					}else {
						lowerThird = new LowerThird("MATCH EXTRAS", matchAllData.getSetup().getHomeTeam().getTeamName4(), matchAllData.getSetup().getAwayTeam().getTeamName4(),
								whatToProcess.split(",")[2], "", "",4,"FLAG","",new String[]{"WD","NB","B","LB","P","CR"},
								new String[]{String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalWides() + "," + matchAllData.getMatch().getInning().get(1).getTotalWides() + "," + 
								(matchAllData.getMatch().getInning().get(0).getTotalWides() + matchAllData.getMatch().getInning().get(1).getTotalWides())),
								String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalNoBalls() + "," + matchAllData.getMatch().getInning().get(1).getTotalNoBalls() + "," + 
								(matchAllData.getMatch().getInning().get(0).getTotalNoBalls() + matchAllData.getMatch().getInning().get(1).getTotalNoBalls())),
								String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalByes() + "," + matchAllData.getMatch().getInning().get(1).getTotalByes() + "," + 
								(matchAllData.getMatch().getInning().get(0).getTotalByes() + matchAllData.getMatch().getInning().get(1).getTotalByes())),
								String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalLegByes() + "," + matchAllData.getMatch().getInning().get(1).getTotalLegByes() + "," + 
								(matchAllData.getMatch().getInning().get(0).getTotalLegByes() + matchAllData.getMatch().getInning().get(1).getTotalLegByes())),
								String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalPenalties() + "," + matchAllData.getMatch().getInning().get(1).getTotalPenalties() + "," + 
								(matchAllData.getMatch().getInning().get(0).getTotalPenalties() + matchAllData.getMatch().getInning().get(1).getTotalPenalties())),
								String.valueOf(cr1 + "," + cr2 + "," + "0")},
								new String[]{matchAllData.getMatch().getInning().get(0).getBatting_team().getTeamName4(),
								matchAllData.getMatch().getInning().get(1).getBatting_team().getTeamName4()},null,
								new String[] {"226.0","369.0","505.0","639.0","774.0"});
					}
				}else {
					if(cr1_found == false && cr2_found == false) {
						lowerThird = new LowerThird("MATCH EXTRAS", matchAllData.getSetup().getHomeTeam().getTeamName4(), matchAllData.getSetup().getAwayTeam().getTeamName4(),
								whatToProcess.split(",")[2], "", "",4,"FLAG","",new String[]{"WD","NB","B","LB"},
								new String[]{String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalWides() + "," + matchAllData.getMatch().getInning().get(1).getTotalWides() + "," + 
										(matchAllData.getMatch().getInning().get(0).getTotalWides() + matchAllData.getMatch().getInning().get(1).getTotalWides())),
										String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalNoBalls() + "," + matchAllData.getMatch().getInning().get(1).getTotalNoBalls() + "," + 
										(matchAllData.getMatch().getInning().get(0).getTotalNoBalls() + matchAllData.getMatch().getInning().get(1).getTotalNoBalls())),
										String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalByes() + "," + matchAllData.getMatch().getInning().get(1).getTotalByes() + "," + 
										(matchAllData.getMatch().getInning().get(0).getTotalByes() + matchAllData.getMatch().getInning().get(1).getTotalByes())),
										String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalLegByes() + "," + matchAllData.getMatch().getInning().get(1).getTotalLegByes() + "," + 
										(matchAllData.getMatch().getInning().get(0).getTotalLegByes() + matchAllData.getMatch().getInning().get(1).getTotalLegByes()))},
								new String[]{matchAllData.getMatch().getInning().get(0).getBatting_team().getTeamName4(),
								matchAllData.getMatch().getInning().get(1).getBatting_team().getTeamName4()},null,
								new String[] {"258.0","453.0","622.0","797.0"});
					}else {
						lowerThird = new LowerThird("MATCH EXTRAS", matchAllData.getSetup().getHomeTeam().getTeamName4(), matchAllData.getSetup().getAwayTeam().getTeamName4(),
								whatToProcess.split(",")[2], "", "",4,"FLAG","",new String[]{"WD","NB","B","LB","CR"},
								new String[]{String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalWides() + "," + matchAllData.getMatch().getInning().get(1).getTotalWides() + "," + 
										(matchAllData.getMatch().getInning().get(0).getTotalWides() + matchAllData.getMatch().getInning().get(1).getTotalWides())),
										String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalNoBalls() + "," + matchAllData.getMatch().getInning().get(1).getTotalNoBalls() + "," + 
										(matchAllData.getMatch().getInning().get(0).getTotalNoBalls() + matchAllData.getMatch().getInning().get(1).getTotalNoBalls())),
										String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalByes() + "," + matchAllData.getMatch().getInning().get(1).getTotalByes() + "," + 
										(matchAllData.getMatch().getInning().get(0).getTotalByes() + matchAllData.getMatch().getInning().get(1).getTotalByes())),
										String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalLegByes() + "," + matchAllData.getMatch().getInning().get(1).getTotalLegByes() + "," + 
										(matchAllData.getMatch().getInning().get(0).getTotalLegByes() + matchAllData.getMatch().getInning().get(1).getTotalLegByes()))},
								new String[]{matchAllData.getMatch().getInning().get(0).getBatting_team().getTeamName4(),
								matchAllData.getMatch().getInning().get(1).getBatting_team().getTeamName4()},null,
								new String[] {"258.0","453.0","622.0","797.0"});
					}
				}
			}
			break;	
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	public String populateLTMatchId(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		
		if(whatToProcess.split(",")[2].equalsIgnoreCase("VENUE")) {
			lowerThird = new LowerThird(matchAllData.getSetup().getHomeTeam().getTeamName4(), matchAllData.getSetup().getAwayTeam().getTeamName4(), 
					"LIVE FROM " + matchAllData.getSetup().getVenueName(),matchAllData.getSetup().getHomeTeam().getTeamName2(), matchAllData.getSetup().getHomeTeam().getTeamName3(),
					matchAllData.getSetup().getAwayTeam().getTeamName2(),1,matchAllData.getSetup().getAwayTeam().getTeamName3(),"",null,null,null,null,null);
			
		}else if(whatToProcess.split(",")[2].equalsIgnoreCase("TARGET")) {
			
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1]))
					.findAny().orElse(null);
				
			if(inning == null) {
				return "populateTarget: Current Inning NOT found in this match";
			}
			
			if(inning.getInningNumber() == 1) {
				return "populateTarget: Current Inning is 1";
			}
			
			String summary = "";
			
			teamNameAsCity = matchAllData.getMatch().getInning().get(1).getBatting_team().getTeamLogo();
			
			if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER) && matchAllData.getSetup().getMaxOvers() == 1) {
				summary = teamNameAsCity + " NEED " + CricketFunctions.getTargetRuns(matchAllData) + " RUNS TO WIN FROM " + (matchAllData.getSetup().getMaxOvers()*6) + " BALLS";
				
			}else {
				if(matchAllData.getSetup().getTargetOvers() == "" || matchAllData.getSetup().getTargetOvers().trim().isEmpty() && matchAllData.getSetup().getTargetRuns() == 0) {
					
					summary = teamNameAsCity + " NEED " + CricketFunctions.getTargetRuns(matchAllData) + " RUNS TO WIN FROM " + 
								CricketFunctions.getTargetOvers(matchAllData) + " OVERS";
				}else {
					if(matchAllData.getSetup().getTargetOvers() != "") {
						
						summary = teamNameAsCity + " NEED " + CricketFunctions.getTargetRuns(matchAllData) + " RUNS TO WIN FROM " + 
								CricketFunctions.getTargetOvers(matchAllData) + " OVERS";
					}
					if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("VJD")) {
						summary = teamNameAsCity + " NEED " + CricketFunctions.getTargetRuns(matchAllData) + " RUNS TO WIN FROM " + 
								CricketFunctions.getTargetOvers(matchAllData) + " OVERS (VJD)";
						
					}else if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("DLS")) {
						summary = teamNameAsCity + " NEED " + CricketFunctions.getTargetRuns(matchAllData) + " RUNS TO WIN FROM " + 
								CricketFunctions.getTargetOvers(matchAllData) + " OVERS (DLS)";
					}
				}
			}
			
			lowerThird = new LowerThird(matchAllData.getSetup().getHomeTeam().getTeamName4(), matchAllData.getSetup().getAwayTeam().getTeamName4(), 
					summary,matchAllData.getSetup().getHomeTeam().getTeamName2(), matchAllData.getSetup().getHomeTeam().getTeamName3(),
					matchAllData.getSetup().getAwayTeam().getTeamName2(),1,matchAllData.getSetup().getAwayTeam().getTeamName3(),"",null,null,null,null,null);
			
		}else if(whatToProcess.split(",")[2].equalsIgnoreCase("RESULT")) {
			
			lowerThird = new LowerThird(matchAllData.getSetup().getHomeTeam().getTeamName4(), matchAllData.getSetup().getAwayTeam().getTeamName4(), 
					CricketFunctions.generateMatchSummaryStatus(2, matchAllData, CricketUtil.FULL, "|", config.getBroadcaster()).toUpperCase()
					,matchAllData.getSetup().getHomeTeam().getTeamName2(), matchAllData.getSetup().getHomeTeam().getTeamName3(),
					matchAllData.getSetup().getAwayTeam().getTeamName2(),1,matchAllData.getSetup().getAwayTeam().getTeamName3(),"",null,null,null,null,null);
			
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	public String populateL3rdTarget(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1]))
			.findAny().orElse(null);
		
		if(inning == null) {
			return "populateTarget: Current Inning NOT found in this match";
		}
		
		if(inning.getInningNumber() == 1) {
			return "populateTarget: Current Inning is 1";
		}
		
		String runRate = "",summary = "";
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.BENGAL_T20:
			teamNameAsCity = inning.getBatting_team().getTeamLogo();
			break;

		default:
			teamNameAsCity = inning.getBatting_team().getTeamName4();
			break;
		}
		
		if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER) && matchAllData.getSetup().getMaxOvers() == 1) {
			summary = teamNameAsCity + " NEED " + CricketFunctions.getTargetRuns(matchAllData) + " RUNS" + " TO WIN ";
			
			lowerThird = new LowerThird(summary, "", "","", "", "",1,"",inning.getBatting_team().getTeamName4(),null,null,
					new String[]{String.valueOf("FROM " + matchAllData.getSetup().getMaxOvers()*6)+ " BALLS",""},null,null);
		}else {
			if(matchAllData.getSetup().getTargetOvers() == "" || matchAllData.getSetup().getTargetOvers().trim().isEmpty() && matchAllData.getSetup().getTargetRuns() == 0) {
				
				summary = teamNameAsCity + " NEED " + CricketFunctions.getTargetRuns(matchAllData) + " RUNS" + " TO WIN ";
				runRate = " AT " + CricketFunctions.generateRunRate(CricketFunctions.getTargetRuns(matchAllData), 
						Integer.valueOf(CricketFunctions.getTargetOvers(matchAllData)), 0, 2, matchAllData) + " RUNS PER OVER";
				
				lowerThird = new LowerThird(summary, "", "","", "", "",1,"",inning.getBatting_team().getTeamName4(),null,null,
						new String[]{String.valueOf("FROM " + CricketFunctions.getTargetOvers(matchAllData))+ " OVERS",runRate},null,null);

			}else {
				summary = teamNameAsCity + " NEED " + CricketFunctions.getTargetRuns(matchAllData) + " RUNS" + " TO WIN ";
				runRate = " AT " + CricketFunctions.generateRunRate(CricketFunctions.getTargetRuns(matchAllData), 
						Integer.valueOf(CricketFunctions.getTargetOvers(matchAllData)), 0, 2,matchAllData) + " RUNS PER OVER";
				
				if(matchAllData.getSetup().getTargetOvers() != "") {
					
					lowerThird = new LowerThird(summary, "", "","", "", "",1,"",inning.getBatting_team().getTeamName4(),null,null,
							new String[]{String.valueOf("FROM " + CricketFunctions.getTargetOvers(matchAllData))+ " OVERS",runRate},null,null);
				}
				if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("VJD")) {
					
					lowerThird = new LowerThird(summary, "", "","", "", "",1,"",inning.getBatting_team().getTeamName4(),null,null,
							new String[]{String.valueOf("FROM " + CricketFunctions.getTargetOvers(matchAllData))+ " OVERS",runRate + " (VJD)"},null,null);
					
				}else if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("DLS")) {
					
					lowerThird = new LowerThird(summary, "", "","", "", "",1,"",inning.getBatting_team().getTeamName4(),null,null,
							new String[]{String.valueOf("FROM " + CricketFunctions.getTargetOvers(matchAllData))+ " OVERS",runRate + " (DLS)"},null,null);
				}
			}
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}

	public String populateL3rdEquation(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == 2 && inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES))
				.findAny().orElse(null);
		
		if(inning == null) {
			return "populateEquation: current inning is 1";
		}
		
		String line_1 = "",summary="";
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.BENGAL_T20:
			teamNameAsCity = inning.getBatting_team().getTeamLogo();
			break;

		default:
			teamNameAsCity = inning.getBatting_team().getTeamName4();
			break;
		}
		
		if(CricketFunctions.getRequiredBalls(matchAllData) < 100) {
			line_1 = "FROM " + CricketFunctions.getRequiredBalls(matchAllData) + " BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase();

		}else {
			if(CricketFunctions.getRequiredBalls(matchAllData)%6 == 0) {
				 line_1 = "FROM " + CricketFunctions.getRequiredBalls(matchAllData)/6 + " OVERS";

			}else {
				line_1 = "FROM " + CricketFunctions.OverBalls(CricketFunctions.getRequiredBalls(matchAllData)/6, CricketFunctions.getRequiredBalls(matchAllData)%6) + " OVERS";
			}
		}
		
		summary = teamNameAsCity + " NEED " + CricketFunctions.getRequiredRuns(matchAllData);
		
		if(CricketFunctions.getRequiredRuns(matchAllData) <= 1) {
			summary = summary + " RUN" + CricketFunctions.Plural(CricketFunctions.getRequiredRuns(matchAllData)).toUpperCase() + " TO WIN ";
		}else {
			summary = summary + " MORE RUN" + CricketFunctions.Plural(CricketFunctions.getRequiredRuns(matchAllData)).toUpperCase() + " TO WIN ";
		}
		
		line_1 = line_1 + " AT " + CricketFunctions.generateRunRate(CricketFunctions.getRequiredRuns(matchAllData), 0, 
				CricketFunctions.getRequiredBalls(matchAllData), 2,matchAllData) + " RUNS PER OVER ";
		
		if(matchAllData.getSetup().getTargetType()!= null) {
			if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("VJD")) {
				line_1 = line_1 + " (" + CricketUtil.VJD + ")";
			}else if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("DLS")) {
				line_1 = line_1 + " (" + CricketUtil.DLS + ")";
			}
		}
		
		String line_2 = "WITH " + (10-inning.getTotalWickets()) + " WICKET" + CricketFunctions.Plural(10 - inning.getTotalWickets()).toUpperCase() + " REMAINING";
		
		lowerThird = new LowerThird(summary, "", "","", "", "",2,"",inning.getBatting_team().getTeamName4(),null,null,
				new String[]{line_1,line_2},null,null);
			
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}

	public String populateLTNameSuperPlayer(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		player =  CricketFunctions.getPlayerFromMatchData(Integer.valueOf(whatToProcess.split(",")[2]), matchAllData);
		team = Teams.stream().filter(tm -> tm.getTeamId() == player.getTeamId()).findAny().orElse(null);
		
		if(team == null) {
			return "populateNameSuper: Team From Database returning Null";
		}
		
		if(player.getSurname() == null) {
			surName = "";
		}else {
			surName = player.getSurname();
		}
		
		lowerThird = new LowerThird("", player.getFirstname(), surName,team.getTeamName1(), team.getTeamName4(), "", 1, "",team.getTeamName4(),
				null,null,new String[]{whatToProcess.split(",")[3].toUpperCase()},null,null);
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ISPL: case Constants.BENGAL_T20:
				HideAndShowL3rdSubStrapContainers(WhichSide);
				break;
			}
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	public String populateLTNameSuper(String whatToProcess,int WhichSide) throws InterruptedException
	{
		namesuper = this.nameSupers.stream().filter(ns -> ns.getNamesuperId() == Integer.valueOf(whatToProcess.split(",")[2]))
			.findAny().orElse(null);
		
		if(namesuper.getSurname() == null) {
			surName = "";
		}else {
			surName = namesuper.getSurname();
		}
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023: case Constants.ISPL:
			if(namesuper.getSponsor()!= null && namesuper.getFlag()!= null && namesuper.getSubLine() != null) {
				lowerThird = new LowerThird("", namesuper.getFirstname(), surName,"", "", "", 1, namesuper.getSponsor() ,namesuper.getFlag(),
						null,null,new String[]{namesuper.getSubLine()},null,null);
			}else if(namesuper.getSponsor()!= null && namesuper.getFlag()== null && namesuper.getSubLine() != null) {
				lowerThird = new LowerThird("", namesuper.getFirstname(), surName,"", "", "", 1, namesuper.getSponsor() ,"",
						null,null,new String[]{namesuper.getSubLine()},null,null);
			}else if(namesuper.getSponsor()== null && namesuper.getFlag()!= null && namesuper.getSubLine() != null) {
				lowerThird = new LowerThird("", namesuper.getFirstname(), surName,"", "", "", 1, "" ,namesuper.getFlag(),
						null,null,new String[]{namesuper.getSubLine()},null,null);
			}else if(namesuper.getSponsor()!= null && namesuper.getFlag()!= null && namesuper.getSubLine() == null) {
				lowerThird = new LowerThird("", namesuper.getFirstname(), surName,"", "", "", 0, namesuper.getSponsor() ,namesuper.getFlag(),
						null,null,new String[]{""},null,null);
			}else if(namesuper.getSponsor()!= null && namesuper.getFlag()== null && namesuper.getSubLine() == null) {
				lowerThird = new LowerThird("", namesuper.getFirstname(), surName,"", "", "", 0, namesuper.getSponsor() ,"",
						null,null,new String[]{""},null,null);
			}else if(namesuper.getSponsor()== null && namesuper.getFlag()!= null && namesuper.getSubLine() == null) {
				lowerThird = new LowerThird("", namesuper.getFirstname(), surName,"", "", "", 0, "" ,namesuper.getFlag(),
						null,null,new String[]{""},null,null);
			}else if(namesuper.getSponsor()== null && namesuper.getFlag()== null && namesuper.getSubLine() == null) {
				lowerThird = new LowerThird("", namesuper.getFirstname(), surName,"", "", "", 0, "" ,"",
						null,null,new String[]{""},null,null);
			}else {
				lowerThird = new LowerThird("", namesuper.getFirstname(), surName,"", "", "", 1, "" ,"",
						null,null,new String[]{namesuper.getSubLine()},null,null);
			}
			break;
		case Constants.BENGAL_T20:
			
			if(namesuper.getSponsor()!= null && namesuper.getSubLine() != null) {
				lowerThird = new LowerThird("", namesuper.getFirstname(), surName,"", "", "", 1, namesuper.getSponsor() ,"",
						null,null,new String[]{namesuper.getSubLine()},null,null);
			}else if(namesuper.getSponsor()== null && namesuper.getSubLine() != null) {
				lowerThird = new LowerThird("", namesuper.getFirstname(), surName,"", "", "", 1, "" ,"",
						null,null,new String[]{namesuper.getSubLine()},null,null);
			}else if(namesuper.getSponsor()!= null && namesuper.getSubLine() == null) {
				lowerThird = new LowerThird("", namesuper.getFirstname(), surName,"", "", "", 0, namesuper.getSponsor() ,"",
						null,null,new String[]{""},null,null);
			}else if(namesuper.getSponsor()== null && namesuper.getSubLine() == null) {
				lowerThird = new LowerThird("", namesuper.getFirstname(), surName,"", "", "", 0, "" ,"",
						null,null,new String[]{""},null,null);
			}else {
				lowerThird = new LowerThird("", namesuper.getFirstname(), surName,"", "", "", 1, "" ,"",
						null,null,new String[]{namesuper.getSubLine()},null,null);
			}
			break;
		}
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ISPL: case Constants.BENGAL_T20:
				HideAndShowL3rdSubStrapContainers(WhichSide);
				break;
			}
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateLTNameSuperSingle(String whatToProcess,int WhichSide) throws InterruptedException
	{
		namesuper = this.nameSupers.stream().filter(ns -> ns.getNamesuperId() == Integer.valueOf(whatToProcess.split(",")[2]))
				.findAny().orElse(null);
		
		if(namesuper.getSurname() == null) {
			surName = "";
		}else {
			surName = namesuper.getSurname();
		}
		
		lowerThird = new LowerThird("", namesuper.getFirstname(), surName,"", "", "", 0, "" ,"",
			null,null,null,null,null);
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ISPL:
				HideAndShowL3rdSubStrapContainers(WhichSide);
				break;
			}
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return Constants.OK;
//			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateLTStaff(String whatToProcess,int WhichSide) throws InterruptedException
	{
		staff =  Staff.stream().filter(tm -> tm.getStaffId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
		if(staff == null) {
			return "populateLTStaff: Staff From Database returning Null";
		}
		
		team = Teams.stream().filter(tm -> tm.getTeamId() == staff.getClubId()).findAny().orElse(null);
		if(team == null) {
			return "populateLTStaff: Team From Database returning Null";
		}
		
		lowerThird = new LowerThird("", staff.getStaffName(), "",team.getTeamName1(), "", "", 1, "",team.getTeamName4(),
				null,null,new String[]{staff.getRole()},null,null);
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
//			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateCaptain(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
			
			if(inning == null) {
				return "populateCaptain: Inning is Not Found";
			}
		}
		
		Player captain =  cricketService.getAllPlayer().stream().filter(plyr -> plyr.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
		
		Team team = Teams.stream().filter(tm -> tm.getTeamId() == captain.getTeamId()).findAny().orElse(null);
		
		if(captain.getSurname() != null) {
			surName = captain.getSurname();
		}else {
			surName = "";
		}
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.BENGAL_T20:
			lowerThird = new LowerThird("", captain.getFirstname(), surName,"", "", "",1,"",team.getTeamName4(),
					null,null,null,null,null);
			break;	
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return status;
//			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateHowOut(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String striktRate = "",howOut = "";
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
			
			if(inning == null) {
				return "populateHowOut: Inning is Not Found";
			}
			battingCard = inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			if(battingCard == null) {
				return status;
			}
		}
		
		String[] Count = CricketFunctions.getScoreTypeData(CricketUtil.BATSMAN,matchAllData, inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2]),
				"-", matchAllData.getEventFile().getEvents()).split("-");
		
		if(battingCard.getStrikeRate() != null) {
			if(battingCard.getStrikeRate().equalsIgnoreCase("0.0")) {
				striktRate = "-";
			}else {
				float sr = Math.round(Double.valueOf(battingCard.getStrikeRate()));
				
				striktRate = String.valueOf(sr);
				
				System.out.println("battingCard.getStrikeRate() = " + striktRate );
				
			}
		}else {
			striktRate = "-";
		}
		
		
		if(battingCard.getPlayer().getSurname() == null) {
			surName = "";
		}else {
			surName = battingCard.getPlayer().getSurname();
		}
		
		if(battingCard.getHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
			if(battingCard.getWasHowOutFielderSubstitute() != null && 
					battingCard.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
				howOut = "run out " + "(sub - " + battingCard.getHowOutFielder().getTicker_name() + ")";
			}else if(battingCard.getHowOutFielder() == null){
				howOut = "run out (sub)";
			}else {
				howOut = "run out (" + battingCard.getHowOutFielder().getTicker_name() + ")";
			}
		}
		else if(battingCard.getHowOut().equalsIgnoreCase(CricketUtil.CAUGHT)) {
			if(battingCard.getWasHowOutFielderSubstitute() != null && 
					battingCard.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
				howOut = "c " +  "(sub - " + battingCard.getHowOutFielder().getTicker_name() + ")  b " + 
						battingCard.getHowOutBowler().getTicker_name();
			} else if(battingCard.getHowOutFielder() == null){
				howOut = "c (sub)";
			}else {
				howOut = "c " + battingCard.getHowOutFielder().getTicker_name() + "  b " + 
						battingCard.getHowOutBowler().getTicker_name();
			}
		}else if(battingCard.getHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
			howOut = "RETIRED HURT";
		}else {
			if(!battingCard.getHowOutPartOne().isEmpty()) {
				howOut = battingCard.getHowOutPartOne();
			}
			
			if(!battingCard.getHowOutPartTwo().isEmpty()) {
				if(!howOut.trim().isEmpty()) {
					howOut = howOut + "  " + battingCard.getHowOutPartTwo();
				}else {
					howOut = battingCard.getHowOutPartTwo();
				}
			}
		}
		
		if (CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2])).equalsIgnoreCase(CricketUtil.YES)) {
			impactOmo = 1;
		} else if (CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(),
				inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2])).equalsIgnoreCase(CricketUtil.YES)) {
			impactOmo =1;
		} else {
			impactOmo =0;
		}
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.BENGAL_T20:
			lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,"", 
					String.valueOf(battingCard.getRuns()), String.valueOf(battingCard.getBalls()),2,"",inning.getBatting_team().getTeamName4(),
					null,null,new String[]{howOut,String.valueOf(battingCard.getFours()),String.valueOf(battingCard.getSixes()),Count[0]},
					new String[] {striktRate},null);
			break;
		case Constants.ICC_U19_2023:
			lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,"", 
					String.valueOf(battingCard.getRuns()), String.valueOf(battingCard.getBalls()),2,"",inning.getBatting_team().getTeamName4(),
					null,null,new String[]{howOut,String.valueOf(battingCard.getFours()),String.valueOf(battingCard.getSixes()),Count[0],striktRate},
					new String[]{String.valueOf((battingCard.getDuration()/60))},null);
			break;
		case Constants.ISPL:
			lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,"", 
					String.valueOf(battingCard.getRuns()), String.valueOf(battingCard.getBalls()),2,"",inning.getBatting_team().getTeamName4(),
					null,null,new String[]{howOut,String.valueOf(battingCard.getFours()),String.valueOf(battingCard.getSixes()), String.valueOf(battingCard.getNines()),Count[0],striktRate},
					null,null);
			break;	
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateHowOutWithOutFielder(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String striktRate = "";
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
			
			if(inning == null) {
				return "populateHowOutWithOutFielder: Inning is Not Found";
			}
			battingCard = inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			if(battingCard == null) {
				return status;
			}
		}
		
		if(battingCard.getStrikeRate() != null) {
			if(battingCard.getStrikeRate().equalsIgnoreCase("0.0")) {
				striktRate = "-";
			}else {
				striktRate = String.valueOf(Math.round(Float.valueOf(battingCard.getStrikeRate())));
			}
		}else {
			striktRate = "-";
		}
		
		
		if(battingCard.getPlayer().getSurname() == null) {
			surName = "";
		}else {
			surName = battingCard.getPlayer().getSurname();
		}
		
		if (CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2])).equalsIgnoreCase(CricketUtil.YES)) {
			impactOmo = 1;
		} else if (CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(),
				inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2])).equalsIgnoreCase(CricketUtil.YES)) {
			impactOmo =1;
		} else {
			impactOmo =0;
		}
		
		String[] Count = CricketFunctions.getScoreTypeData(CricketUtil.BATSMAN,matchAllData, inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2]),
				"-", matchAllData.getEventFile().getEvents()).split("-");
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.BENGAL_T20:
			lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,"", 
					String.valueOf(battingCard.getRuns()), String.valueOf(battingCard.getBalls() + 1),1,"",inning.getBatting_team().getTeamName4(),
					null,null,new String[]{String.valueOf(battingCard.getFours()),String.valueOf(battingCard.getSixes()),Count[0]},
					new String[]{striktRate},null);
			break;
		case Constants.ICC_U19_2023:
			lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,"", 
					String.valueOf(battingCard.getRuns()), String.valueOf(battingCard.getBalls() + 1),1,"",inning.getBatting_team().getTeamName4(),
					null,null,new String[]{String.valueOf(battingCard.getFours()),String.valueOf(battingCard.getSixes()),Count[0],striktRate},
					new String[]{striktRate},null);
			break;
		case Constants.ISPL:
			lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,"", 
					String.valueOf(battingCard.getRuns()), String.valueOf(battingCard.getBalls() + 1),1,"",inning.getBatting_team().getTeamName4(),
					null,null,new String[]{String.valueOf(battingCard.getFours()),String.valueOf(battingCard.getSixes()),String.valueOf(battingCard.getNines()),Count[0],striktRate},
					new String[]{striktRate},null);
			break;	
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateQuickHowOut(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String striktRate = "",howOut = "",duration ="";
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
			inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);

			if(inning == null) {
				return "populateVizInfobarMiddleSection: Inning returned is NULL";
			}
			
			if(inning.getFallsOfWickets() == null && inning.getFallsOfWickets().isEmpty()) {
				return "populateVizInfobarMiddleSection: FoW returned is EMPTY";
			}
			
			battingCardList.add(inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == 
				inning.getFallsOfWickets().get(inning.getFallsOfWickets().size() - 1).getFowPlayerID()).findAny().orElse(null));
	
			if(battingCardList.get(battingCardList.size()-1) == null) {
				return "populateVizInfobarLeftBottom: Last wicket returned is invalid";
			}
		}
		
		if(battingCardList.get(battingCardList.size()-1).getStrikeRate() != null) {
			if(battingCardList.get(battingCardList.size()-1).getStrikeRate().equalsIgnoreCase("0.0")) {
				striktRate = "-";
			}else {
				striktRate = String.valueOf(Math.round(Float.valueOf(battingCardList.get(battingCardList.size()-1).getStrikeRate())));
			}
		}else {
			striktRate = "-";
		}
		
		
		if(battingCardList.get(battingCardList.size()-1).getPlayer().getSurname() == null) {
			surName = "";
		}else {
			surName = battingCardList.get(battingCardList.size()-1).getPlayer().getSurname();
		}
		
		if(battingCardList.get(battingCardList.size()-1).getHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
			if(battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute() != null && 
					battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
				howOut = "run out " + "(sub - " + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + ")";
			} else if(battingCardList.get(battingCardList.size()-1).getHowOutFielder() == null){
				howOut = "run out (sub)";
			}else {
				howOut = "run out (" + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + ")";
			}
		}
		else if(battingCardList.get(battingCardList.size()-1).getHowOut().equalsIgnoreCase(CricketUtil.CAUGHT)) {
			if(battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute() != null && 
					battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
				howOut = "c " +  "(sub -  " + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + ")  b " + 
						battingCardList.get(battingCardList.size()-1).getHowOutBowler().getTicker_name();
			} else if(battingCardList.get(battingCardList.size()-1).getHowOutFielder() == null){
				howOut = "c (sub)";
			}else {
				howOut = "c " + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + "  b " + 
						battingCardList.get(battingCardList.size()-1).getHowOutBowler().getTicker_name();
			}
		}else {
			if(!battingCardList.get(battingCardList.size()-1).getHowOutPartOne().isEmpty()) {
				howOut = battingCardList.get(battingCardList.size()-1).getHowOutPartOne();
			}
			
			if(!battingCardList.get(battingCardList.size()-1).getHowOutPartTwo().isEmpty()) {
				if(!howOut.trim().isEmpty()) {
					howOut = howOut + "  " + battingCardList.get(battingCardList.size()-1).getHowOutPartTwo();
				}else {
					howOut = battingCardList.get(battingCardList.size()-1).getHowOutPartTwo();
				}
			}
		}
		
		if (CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), inning.getFallsOfWickets()
				.get(inning.getFallsOfWickets().size() - 1).getFowPlayerID()).equalsIgnoreCase(CricketUtil.YES)) {
			impactOmo = 1;
		} else if (CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(),
				inning.getInningNumber(), inning.getFallsOfWickets().get(inning.getFallsOfWickets().size() - 1)
				.getFowPlayerID()).equalsIgnoreCase(CricketUtil.YES)) {
			impactOmo =1;
		} else {
			impactOmo =0;
		}
		
		String[] Count = CricketFunctions.getScoreTypeData(CricketUtil.BATSMAN,matchAllData, inning.getInningNumber(), 
				battingCardList.get(battingCardList.size()-1).getPlayerId(),"-", matchAllData.getEventFile().getEvents()).split("-");
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.BENGAL_T20:
			lowerThird = new LowerThird("", battingCardList.get(battingCardList.size()-1).getPlayer().getFirstname(), surName,"", 
					String.valueOf(battingCardList.get(battingCardList.size()-1).getRuns()), 
					String.valueOf(battingCardList.get(battingCardList.size()-1).getBalls()),2,"",inning.getBatting_team().getTeamName4(),
					null,null,new String[]{howOut,String.valueOf(battingCardList.get(battingCardList.size()-1).getFours()),
					String.valueOf(battingCardList.get(battingCardList.size()-1).getSixes()),Count[0]},
					new String[]{striktRate},null);
			break;
		case Constants.ICC_U19_2023:
			lowerThird = new LowerThird("", battingCardList.get(battingCardList.size()-1).getPlayer().getFirstname(), surName,"", 
					String.valueOf(battingCardList.get(battingCardList.size()-1).getRuns()), 
					String.valueOf(battingCardList.get(battingCardList.size()-1).getBalls()),2,"",inning.getBatting_team().getTeamName4(),
					null,null,new String[]{howOut,String.valueOf(battingCardList.get(battingCardList.size()-1).getFours()),
					String.valueOf(battingCardList.get(battingCardList.size()-1).getSixes()),Count[0],striktRate},
					new String[]{String.valueOf((battingCardList.get(battingCardList.size()-1).getDuration()/60))},null);
			break;
		case Constants.ISPL:
			lowerThird = new LowerThird("", battingCardList.get(battingCardList.size()-1).getPlayer().getFirstname(), surName,"", 
					String.valueOf(battingCardList.get(battingCardList.size()-1).getRuns()), 
					String.valueOf(battingCardList.get(battingCardList.size()-1).getBalls()),2,"",inning.getBatting_team().getTeamName4(),
					null,null,new String[]{howOut,String.valueOf(battingCardList.get(battingCardList.size()-1).getFours()),
					String.valueOf(battingCardList.get(battingCardList.size()-1).getSixes()),
					String.valueOf(battingCardList.get(battingCardList.size()-1).getNines()),Count[0],striktRate},
					null,null);
			break;	
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	public String PopulateBatBallGriff(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
			if(inning == null) {
				return "populateBatThisMatch: Current Inning NOT found in this match";
			}
			
			player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(whatToProcess.split(",")[2]), matchAllData);
			
			if(whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F1")) {
				
				griff = CricketFunctions.getBatBallGriffData(CricketUtil.BATSMAN,Integer.valueOf(whatToProcess.split(",")[2]), Teams, tournament_matches, matchAllData);
				
				if(griff == null) {
					return "griff is Null";
				}
				
				if(griff.size() > 7) {
					return "griff size is greater than 7 we cannot display data";
				}
				
				setGriff(griff.size(), whatToProcess, WhichSide, config);
				
			}else if(whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F2")) {
				
				griff = CricketFunctions.getBatBallGriffData(CricketUtil.BOWLER,Integer.valueOf(whatToProcess.split(",")[2]), Teams, tournament_matches, matchAllData);
				
				if(griff == null) {
					return "griff is Null";
				}
				
//				System.out.println("SIZE : " + griff.size());
				
				if(griff.size() > 7) {
					return "griff size is greater than 7 we cannot display data";
				}
				
				setGriff(griff.size(), whatToProcess, WhichSide, config);
			}
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowGriffSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,l3griff.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateL3rdThisSeries(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException, IOException
	{
		String teamName = "",best = "-",economy = "",
				bat_sr = "",hundred = "", fifties = "",Data = "",thirty = "";
		int k =0;
		double average = 0,economy_rate=0;
		
		
		switch (config.getBroadcaster()) {
		case Constants.BENGAL_T20:
			addPastDataToCurr = CricketFunctions.extractTournamentData("CURRENT_MATCH_DATA", false, headToHead,cricketService, matchAllData, past_tournament_stats);
			break;

		default:
			if(whatToProcess.split(",")[3].equalsIgnoreCase("WITHOUT_CURRENT")) {
				addPastDataToCurr = CricketFunctions.extractTournamentStats("PAST_MATCHES_DATA", false, tournament_matches, null, matchAllData, null);
			}else if(whatToProcess.split(",")[3].equalsIgnoreCase("WITH_CURRENT")) {
				addPastDataToCurr = CricketFunctions.extractTournamentStats("CURRENT_MATCH_DATA", false, tournament_matches, null, matchAllData, tournaments);
			}
			break;
		}
		if(addPastDataToCurr == null) {
			return "addPastDataToCurr is Null";
		}
		
		for(Tournament tourn : addPastDataToCurr) {
			for(BestStats bs : tourn.getBatsman_best_Stats()) {
				top_batsman_beststats.add(bs);
			}
			for(BestStats bfig : tourn.getBowler_best_Stats()) {
				top_bowler_beststats.add(bfig);
			}
		}
		
		Collections.sort(top_batsman_beststats, new CricketFunctions.PlayerBestStatsComparator());
		Collections.sort(top_bowler_beststats, new CricketFunctions.PlayerBestStatsComparator());
		
		tournament = addPastDataToCurr.stream().filter(tourn -> tourn.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
		
		team = Teams.stream().filter(tm -> tm.getTeamId() == tournament.getPlayer().getTeamId()).findAny().orElse(null);
		teamName = team.getTeamName4();
		
		if(tournament.getPlayer().getSurname() == null) {
			surName = "";
		}else {
			surName = tournament.getPlayer().getSurname();
		}
		
		if(whatToProcess.split(",")[0].equalsIgnoreCase("Control_s")) {
			
			for(int j=0;j<= top_batsman_beststats.size()-1;j++) {
				if(top_batsman_beststats.get(j).getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])) {
					if(k == 0) {
						k += 1;
						if(top_batsman_beststats.get(j).getBestEquation() % 2 == 0) {
							if(top_batsman_beststats.get(j).getBestEquation()/2 == 0) {
								best = "-";
							}else {
								best = String.valueOf(top_batsman_beststats.get(j).getBestEquation()/2);
							}
						}else {
							best = (top_batsman_beststats.get(j).getBestEquation()-1) / 2 + "*";
						}
						break;
					}
				}else {
					best = "-";
				}
			}
			
			if(CricketFunctions.generateStrikeRate(tournament.getRuns(), 
					tournament.getBallsFaced(), 1).trim().isEmpty()) {
				bat_sr = "-"; 
			}else {
				bat_sr = String.valueOf(Math.round(Float.valueOf(CricketFunctions.generateStrikeRate(tournament.getRuns(), 
						tournament.getBallsFaced(), 1))));
//				bat_sr = CricketFunctions.generateStrikeRate(tournament.getRuns(), 
//						tournament.getBallsFaced(), 1);
			}
			
			if(tournament.getThirty() == 0) {
				thirty = "-";
			}else {
				thirty = String.valueOf(tournament.getThirty());
			}
			
			if(tournament.getFifty() == 0) {
				fifties = "-";
			}else {
				fifties = String.valueOf(tournament.getFifty());
			}
			
			if(tournament.getHundreds() == 0) {
				hundred = "-";
			}else {
				hundred = String.valueOf(tournament.getHundreds());
			}
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.BENGAL_T20:
				lowerThird = new LowerThird("", tournament.getPlayer().getFirstname(), surName,"BPTL - 2024", "", "", 2,"",teamName,
						new String[]{"MATCHES", "RUNS", "50s", "100s", "S/R", "BEST"},new String[]{String.valueOf(tournament.getMatches()), 
						String.format("%,d\n", tournament.getRuns()), fifties,hundred,bat_sr,best},null,null,
						new String[] {"-3.0","51.0","103.0","155.0","207.0","259.0"});
				break;
			case Constants.ICC_U19_2023:
				lowerThird = new LowerThird("", tournament.getPlayer().getFirstname(), surName,"ICC U19 MEN'S CWC 2024", "", "", 2,"",teamName,
						new String[]{"MATCHES", "RUNS", "FIFTIES", "HUNDREDS", "STRIKE RATE", "BEST"},new String[]{String.valueOf(tournament.getMatches()), 
						String.format("%,d\n", tournament.getRuns()), fifties,hundred,bat_sr,best},null,null,
						new String[] {"-503.0","-315.0","-116.0","115.0","350.0","560.0"});
				break;
			case Constants.ISPL:
				lowerThird = new LowerThird("", tournament.getPlayer().getFirstname(), surName,"ISPL 2024", "", "", 2,"",teamName,
						new String[]{"MATCHES", "RUNS", "THIRTIES", "FIFTIES", "S/R", "BEST"},new String[]{String.valueOf(tournament.getMatches()), 
						String.format("%,d\n", tournament.getRuns()), thirty,fifties,bat_sr,best},null,null,
						new String[] {"0.0","186.0","333.0","516.0","673.0","805.0"});
				break;	
			}
		}else if(whatToProcess.split(",")[0].equalsIgnoreCase("Control_f")) {
			
			for(int j=0;j<= top_bowler_beststats.size()-1;j++) {
				System.out.println("top_bowler_beststats = " + top_bowler_beststats.get(j));
				if(top_bowler_beststats.get(j).getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])) {
					if(k == 0) {
						k += 1;
						if(top_bowler_beststats.get(j).getBestEquation() % 1000 > 0) {
							best = ((top_bowler_beststats.get(j).getBestEquation() / 1000) +1) + "-" + (1000 - (top_bowler_beststats.get(j).getBestEquation() % 1000));
						}
						else if(top_bowler_beststats.get(j).getBestEquation() % 1000 < 0) {
							best = (top_bowler_beststats.get(j).getBestEquation() / 1000) + "-" + Math.abs(top_bowler_beststats.get(j).getBestEquation());
						}
						else if(top_bowler_beststats.get(j).getBestEquation() != 0) {
							if(top_bowler_beststats.get(j).getBestEquation() % 1000 == 0) {
								best = (top_bowler_beststats.get(j).getBestEquation() / 1000) + "-" + "0";
							}
						}
						break;
					}
				}else if(top_bowler_beststats.get(j).getPlayerId() != Integer.valueOf(whatToProcess.split(",")[2])) {
					best = "-";
				}
			}
			
			if(tournament.getRunsConceded() == 0 && tournament.getBallsBowled() == 0) {
				economy = "-";
			}else {
				economy_rate = (Double.valueOf(tournament.getRunsConceded())) / tournament.getBallsBowled();
				economy_rate = economy_rate * 6;
				DecimalFormat df_b = new DecimalFormat("0.00");
				economy = df_b.format(economy_rate);
			}
			
			if(tournament.getRunsConceded() == 0 || tournament.getWickets() == 0) {
				Data = "-";
			}else {
				average = tournament.getRunsConceded()/tournament.getWickets();
				DecimalFormat df_bo = new DecimalFormat("0.00");
				Data = df_bo.format(average);
			}
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.BENGAL_T20:
				lowerThird = new LowerThird("", tournament.getPlayer().getFirstname(), surName,"BPTL - 2024", "", "", 2,"",teamName,
						new String[]{"MATCHES", "WICKETS", "AVERAGE", "ECONOMY", "BEST"},new String[]{String.valueOf(tournament.getMatches()),
						String.valueOf(tournament.getWickets()),Data,economy,best},null,null,new String[] {"-3.0","63.0","129.0","195.0","261.0"});
				break;
			case Constants.ICC_U19_2023:
				lowerThird = new LowerThird("", tournament.getPlayer().getFirstname(), surName,"ICC U19 MEN'S CWC 2024", "", "", 2,"",teamName,
						new String[]{"MATCHES", "WICKETS", "AVERAGE", "ECONOMY", "BEST"},new String[]{String.valueOf(tournament.getMatches()),
						String.valueOf(tournament.getWickets()),Data,economy,best},null,null,new String[] {"-503.0","-231.0","56.0","319.0","563.0"});
				break;
			case Constants.ISPL:
				lowerThird = new LowerThird("", tournament.getPlayer().getFirstname(), surName,"ISPL 2024", "", "", 2,"",teamName,
						new String[]{"MATCHES", "WICKETS", "ECONOMY", "BEST"},new String[]{String.valueOf(tournament.getMatches()),
						String.valueOf(tournament.getWickets()),economy,best},null,null,new String[] {"0.0","246.0","514.0","763.0"});
				break;	
			}
		}
//		System.out.println(tournament.getPlayer().getFull_name() + " - " + tournament.getMatches() + " - " + tournament.getRuns());
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateBoundaries(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
			
			if(inning == null) {
				return "populateBoundaries: Inning is Not Found";
			}
			
			battingCard = inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			if(battingCard == null) {
				return status;
			}
		}
		
		if(battingCard.getPlayer().getSurname() == null) {
			surName = "";
		}else {
			surName = battingCard.getPlayer().getSurname();
		}
		
		lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,battingCard.getStatus(), 
				String.valueOf(battingCard.getRuns()), String.valueOf(battingCard.getBalls()),0,"Emirates",inning.getBatting_team().getTeamName4(),
				null,null,new String[]{String.valueOf(battingCard.getFours()),String.valueOf(battingCard.getSixes())},null,null);
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			//HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateTeamsBoundaries(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
			
			if(inning == null) {
				return "populateTeamsBoundaries: Inning is Not Found";
			}
		}
		
		lowerThird = new LowerThird("", inning.getBatting_team().getTeamName1(), "",String.valueOf(CricketFunctions.OverBalls(inning.getTotalOvers(), inning.getTotalBalls())), 
				String.valueOf(inning.getTotalRuns()), String.valueOf(inning.getTotalWickets()),0,"Emirates",inning.getBatting_team().getTeamName4(),
				null,null,new String[]{String.valueOf(inning.getTotalFours()),String.valueOf(inning.getTotalSixes())},null,null);
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			//HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateBatSummary(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String outOrNot = "";
		
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
			
			if(inning == null) {
				return "populateBatSummary: Inning is Not Found";
			}
			
			battingCard = inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			if(battingCard == null) {
				return status;
			}
			
			if(battingCard.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
				outOrNot = "NOT OUT";
			}else {
				outOrNot = "";
			}
			
		}
		
		String[] Count = CricketFunctions.getScoreTypeData(CricketUtil.BATSMAN,matchAllData, inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2]),
				"-", matchAllData.getEventFile().getEvents()).split("-");
		
		if(battingCard.getPlayer().getSurname() == null) {
			surName = "";
		}else {
			surName = battingCard.getPlayer().getSurname();
		}
		
		if (CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2])).equalsIgnoreCase(CricketUtil.YES)) {
			impactOmo = 1;
		} else if (CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(),
				inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2])).equalsIgnoreCase(CricketUtil.YES)) {
			impactOmo =1;
		} else {
			impactOmo =0;
		}
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.BENGAL_T20:
			lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,outOrNot, String.valueOf(battingCard.getRuns()), 
					String.valueOf(battingCard.getBalls()), 2, "", inning.getBatting_team().getTeamName4(),new String[] {"DOTS", "ONES", "TWOS", "THREES", "FOURS", "SIXES"},
					new String[]{Count[0],Count[1],Count[2],Count[3],Count[4],Count[6]},null,null,new String[] {"-12.0","42.0","94.0","146.0","198.0","250.0"});
			break;
		case Constants.ICC_U19_2023:
			lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,outOrNot, String.valueOf(battingCard.getRuns()), 
					String.valueOf(battingCard.getBalls()), 2, "", inning.getBatting_team().getTeamName4(),new String[] {"DOTS", "ONES", "TWOS", "THREES", "FOURS", "SIXES"},
					new String[]{Count[0],Count[1],Count[2],Count[3],Count[4],Count[6]},null,null,new String[] {"-530.0","-328.0","-122.0","115.0","344.0","560.0"});
			break;
		case Constants.ISPL:
			lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,outOrNot, String.valueOf(battingCard.getRuns()), 
					String.valueOf(battingCard.getBalls()), 2, "", inning.getBatting_team().getTeamName4(),new String[] {"0s", "1s", "2s", "3s", "4s", "6s","9s"},
					new String[]{Count[0],Count[1],Count[2],Count[3],Count[4],Count[6],Count[7]},null,null,new String[] {"-26.0","97.0","229.0","366.0","510.0","656.0","785.0"});
			break;	
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(5, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
//			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 4,print_writers, config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateBallSummary(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String over_text = "";
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
			
			if(inning == null) {
				return "populateBallSummary: Inning is Not Found";
			}
			
			bowlingCard = inning.getBowlingCard().stream().filter(boc -> boc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			if(bowlingCard == null) {
				return status;
			}
		}
		
		if (CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2])).equalsIgnoreCase(CricketUtil.YES)) {
			impactOmo = 1;
		} else if (CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(),
				inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2])).equalsIgnoreCase(CricketUtil.YES)) {
			impactOmo =1;
		} else {
			impactOmo =0;
		}
		
		String[] Count = CricketFunctions.getScoreTypeData(CricketUtil.BOWLER,matchAllData, inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2]),
				"-", matchAllData.getEventFile().getEvents()).split("-");
		
		if(bowlingCard.getPlayer().getSurname() == null) {
			surName = "";
		}else {
			surName = bowlingCard.getPlayer().getSurname();
		}
		
		if(bowlingCard.getOvers() == 0 || bowlingCard.getOvers() > 1) {
			over_text = "OVER" + CricketFunctions.Plural(bowlingCard.getOvers()).toUpperCase();
		}else {
			if(bowlingCard.getBalls() == 1) {
				over_text = "OVERS";
			}
			else if(bowlingCard.getBalls() > 0) {
				over_text = "OVER" + CricketFunctions.Plural(bowlingCard.getBalls()).toUpperCase();
			}
			else {
				over_text = "OVER";
			}
		}
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.BENGAL_T20:
			lowerThird = new LowerThird("", bowlingCard.getPlayer().getFirstname(), surName,over_text, String.valueOf(bowlingCard.getWickets()) + "-" + String.valueOf(bowlingCard.getRuns()), 
					String.valueOf(CricketFunctions.OverBalls(bowlingCard.getOvers(), bowlingCard.getBalls())), 2, "", inning.getBowling_team().getTeamName4(),new String[] {"DOTS", "ONES", "TWOS", "THREES", "FOURS", "SIXES"},
					new String[]{Count[0],Count[1],Count[2],Count[3],Count[4],Count[6]},null,null,new String[] {"-12.0","42.0","94.0","146.0","198.0","250.0"});
			break;
		case Constants.ICC_U19_2023:
			lowerThird = new LowerThird("", bowlingCard.getPlayer().getFirstname(), surName,over_text, String.valueOf(bowlingCard.getWickets()) + "-" + String.valueOf(bowlingCard.getRuns()), 
					String.valueOf(CricketFunctions.OverBalls(bowlingCard.getOvers(), bowlingCard.getBalls())), 2, "", inning.getBowling_team().getTeamName4(),new String[] {"DOTS", "ONES", "TWOS", "THREES", "FOURS", "SIXES"},
					new String[]{Count[0],Count[1],Count[2],Count[3],Count[4],Count[6]},null,null,new String[] {"-530.0","-328.0","-122.0","115.0","344.0","560.0"});
			break;
		case Constants.ISPL:
			lowerThird = new LowerThird("", bowlingCard.getPlayer().getFirstname(), surName,over_text, String.valueOf(bowlingCard.getWickets()) + "-" + String.valueOf(bowlingCard.getRuns()), 
					String.valueOf(CricketFunctions.OverBalls(bowlingCard.getOvers(), bowlingCard.getBalls())), 2, "", inning.getBowling_team().getTeamName4(),new String[] {"0s", "1s", "2s", "3s", "4s", "6s","9s"},
					new String[]{Count[0],Count[1],Count[2],Count[3],Count[4],Count[6],Count[7]},null,null,new String[] {"-26.0","97.0","229.0","366.0","510.0","656.0","785.0"});
			break;	
		}
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(5, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
//			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 4,print_writers, config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateTeamSummary(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getBattingTeamId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			
			if(inning == null) {
				return "populateTeamSummary: Inning is Not Found";
			}
		}
		
		String[] Count = CricketFunctions.getScoreTypeData(CricketUtil.TEAM,matchAllData, inning.getInningNumber(), 0,
				"-", matchAllData.getEventFile().getEvents()).split("-");
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.BENGAL_T20:
			lowerThird = new LowerThird(inning.getBatting_team().getTeamName1(), "", "","", String.valueOf(inning.getTotalRuns()) + "-" + String.valueOf(inning.getTotalWickets()), 
					CricketFunctions.OverBalls(inning.getTotalOvers(), inning.getTotalBalls()), 2, "", inning.getBatting_team().getTeamName4(),
					new String[] {"DOTS", "ONES", "TWOS", "THREES", "FOURS", "SIXES"},new String[]{Count[0],Count[1],Count[2],Count[3],String.valueOf(inning.getTotalFours()),
					String.valueOf(inning.getTotalSixes())},null,null,new String[] {"-12.0","42.0","94.0","146.0","198.0","250.0"});
			break;
		case Constants.ICC_U19_2023:
			lowerThird = new LowerThird(inning.getBatting_team().getTeamName1(), "", "","", String.valueOf(inning.getTotalRuns()) + "-" + String.valueOf(inning.getTotalWickets()), 
					CricketFunctions.OverBalls(inning.getTotalOvers(), inning.getTotalBalls()), 2, "", inning.getBatting_team().getTeamName4(),
					new String[] {"DOTS", "ONES", "TWOS", "THREES", "FOURS", "SIXES"},new String[]{Count[0],Count[1],Count[2],Count[3],String.valueOf(inning.getTotalFours()),
					String.valueOf(inning.getTotalSixes())},null,null,new String[] {"-530.0","-328.0","-122.0","115.0","344.0","560.0"});
			break;
		case Constants.ISPL:
			lowerThird = new LowerThird(inning.getBatting_team().getTeamName1(), "", "","", String.valueOf(inning.getTotalRuns()) + "-" + String.valueOf(inning.getTotalWickets()), 
					CricketFunctions.OverBalls(inning.getTotalOvers(), inning.getTotalBalls()), 2, "", inning.getBatting_team().getTeamName4(),
					new String[] {"0s", "1s", "2s", "3s", "4s", "6s","9s"},new String[]{Count[0],Count[1],Count[2],Count[3],String.valueOf(inning.getTotalFours()),
					String.valueOf(inning.getTotalSixes()),String.valueOf(inning.getTotalNines())},null,null,new String[] {"-26.0","97.0","229.0","366.0","510.0","656.0","785.0"});
			break;	
		}
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(5, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
//			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 4,print_writers, config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populatePowerplay(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String powerplay = "",Subheader = "", Subline = "";
		
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
		
		if(inning == null) {
			return "populateHowOut: Inning is Not Found";
		}
		
		if(whatToProcess.split(",")[2].equalsIgnoreCase("p1")) {
			powerplay = "POWERPLAY 1";
			Subheader = "(OVERS " + inning.getFirstPowerplayStartOver() + " TO " + inning.getFirstPowerplayEndOver() + ")";
			Subline = "NO MORE THAN 2 FIELDERS ALLOWED OUTSIDE 30-YARD CIRCLE";
		}else if(whatToProcess.split(",")[2].equalsIgnoreCase("p2")) {
			powerplay = "POWERPLAY 2";
			Subheader = "(OVERS " + inning.getSecondPowerplayStartOver() + " TO " + inning.getSecondPowerplayEndOver() + ")";
			Subline = "NO MORE THAN 4 FIELDERS ALLOWED OUTSIDE 30-YARD CIRCLE";
		}else if(whatToProcess.split(",")[2].equalsIgnoreCase("p3")) {
			powerplay = "POWERPLAY 3";
			Subheader = "(OVERS " + inning.getThirdPowerplayStartOver() + " TO " + inning.getThirdPowerplayEndOver() + ")";
			Subline = "NO MORE THAN 5 FIELDERS ALLOWED OUTSIDE 30-YARD CIRCLE";
		}
		lowerThird = new LowerThird(powerplay, matchAllData.getSetup().getHomeTeam().getTeamName4(), 
				matchAllData.getSetup().getAwayTeam().getTeamName4(),Subheader, "", "", 1, "FLAG" ,"",
				null,null,new String[]{Subline},null,null);
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(5, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
//			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 4,print_writers, config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateL3rdAllRounderStats(String whatToProcess, int WhichSide, MatchAllData matchAllData) throws InterruptedException, IOException {
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			if( matchAllData.getMatch().getInning() == null) {
				return status;
			}
			FirstPlayerId = Integer.valueOf(whatToProcess.split(",")[3]);
			WhichProfile = whatToProcess.split(",")[2];
			
			this_data_str = new ArrayList<String>();
			battingCard = null;
			bowlingCard = null;
			
			if(whatToProcess.split(",")[2].equalsIgnoreCase("Economy")||whatToProcess.split(",")[2].equalsIgnoreCase("Catches")) {
				
				for(Inning inn : matchAllData.getMatch().getInning()) {
					if(battingCard == null) {
						battingCard = inn.getBattingCard().stream().filter(bc->bc.getPlayerId() == 
								FirstPlayerId).findAny().orElse(null);
					}
					
					if(inn.getBowlingCard() != null && inn.getBowlingCard().size() > 0) {
						if(bowlingCard == null) {
							bowlingCard = inn.getBowlingCard().stream().filter(boc->boc.getPlayerId() == 
									FirstPlayerId).findAny().orElse(null);
						}
					}
				}
				
				team = Teams.stream().filter(team->team.getTeamId() == battingCard.getPlayer().getTeamId())
						.findAny().orElse(null);
				
				if(battingCard.getRuns() <= 0) {
					this_data_str.add("-");
				}else {
					this_data_str.add(CricketFunctions.generateStrikeRate(battingCard.getRuns(), 
							battingCard.getBalls(), 1));
				}
				
				if(bowlingCard == null) {
					this_data_str.add("-");
					this_data_str.add("-");
					this_data_str.add("-");
				}else {
					this_data_str.add(CricketFunctions.OverBalls(bowlingCard.getOvers(), bowlingCard.getBalls()));
					this_data_str.add(String.valueOf(bowlingCard.getWickets()));
					
					if(whatToProcess.split(",")[2].equalsIgnoreCase("Catches")) {
						this_data_str.add(String.valueOf(CricketFunctions.getAllRounderCatches(Integer.valueOf(
								whatToProcess.split(",")[3]), matchAllData, matchAllData.getEventFile().getEvents())));
						
					}else if(whatToProcess.split(",")[2].equalsIgnoreCase("Economy")) {
						if(bowlingCard.getEconomyRate() == null) {
							this_data_str.add("-");
						}else {
							this_data_str.add(bowlingCard.getEconomyRate());
						}
					}
				}
				
				lowerThird = new LowerThird(battingCard.getPlayer().getFull_name(),team.getTeamName1(), "","Economy", "","", 2, "",
						team.getTeamName4(), new String[]{"RUNS","BALLS","STRIKE RATE","OVERS","WICKETS",WhichProfile.toUpperCase()},
						new String[]{String.valueOf(battingCard.getRuns()), String.valueOf(battingCard.getBalls()), 
							this_data_str.get(0),this_data_str.get(1),this_data_str.get(2),this_data_str.get(3)},null,
						null,new String[] {"-530.0","-345.0","-122.0","102.0","310.0","530.0"});
					
			}
			else if(whatToProcess.split(",")[2].equalsIgnoreCase("Career")) {
				stat = statistics.stream().filter(st -> st.getPlayer_id() == FirstPlayerId).findAny().orElse(null);
				if(stat == null) {
					
				}
				statsType = statsTypes.stream().filter(st -> st.getStats_short_name().
						equalsIgnoreCase("U19ODI")).findAny().orElse(null);
				if(statsType==null) {
					return "Player stats not found";
				}
				
				stat.setStats_type(statsType);
				
				stat = CricketFunctions.updateTournamentDataWithStats(stat, tournament_matches, matchAllData);
				stat = CricketFunctions.updateStatisticsWithMatchData(stat, matchAllData);
				
				player = CricketFunctions.getPlayerFromMatchData(stat.getPlayer_id(), matchAllData); 
				
				if(player == null) {
					return "populateL3rdAllRounderStats: Player Id not found [" + FirstPlayerId + "]";
				}
				
				team = Teams.stream().filter(team->team.getTeamId() == player.getTeamId())
						.findAny().orElse(null);
				
				lowerThird = new LowerThird(player.getFull_name(),team.getTeamName1(), "","Career", "","", 
						2, "" ,team.getTeamName4(),new String[]{"MATCHES","RUNS","AVERAGE","WICKETS","ECONOMY"},
						new String[]{String.valueOf(stat.getMatches()), String.valueOf(stat.getRuns()),
						String.valueOf(CricketFunctions.getAverage(stat.getInnings(), stat.getNot_out(), 
						stat.getRuns(), 2, "-")),String.valueOf(stat.getWickets()), String.valueOf(
						CricketFunctions.getEconomy(stat.getRuns_conceded(), stat.getBalls_bowled(), 2, "-"))},null,
						null,new String[] {"-503.0","-262.0","-9.0","250.0","530.0"});
				
				
			}else if(whatToProcess.split(",")[2].equalsIgnoreCase("Tournament")) {
				addPastDataToCurr =  new ArrayList<Tournament>();
				addPastDataToCurr = CricketFunctions.extractTournamentStats("CURRENT_MATCH_DATA", false, 
						tournament_matches, null, matchAllData, tournaments);
				
				if(addPastDataToCurr == null) {
					return "populateL3rdAllRounderStats: Player PastDataToCurr not found ";

				}
				tournament = addPastDataToCurr.stream().filter(tourn -> tourn.getPlayerId() == 
						FirstPlayerId).findAny().orElse(null);
				
				team = Teams.stream().filter(tm -> tm.getTeamId() == 
						tournament.getPlayer().getTeamId()).findAny().orElse(null);
				
				lowerThird = new LowerThird(player.getFull_name(),team.getTeamName1(), "","Tournament", "","", 
						2, "" ,team.getTeamName4(),new String[]{"MATCHES","RUNS","AVERAGE","WICKETS","ECONOMY"},
						new String[]{String.valueOf(tournament.getMatches()), String.valueOf(tournament.getRuns()),
						
						String.valueOf(CricketFunctions.getAverage(tournament.getInnings(), tournament.getNot_out() ,
								tournament.getRuns(), 2, "-")),String.valueOf(tournament.getWickets()), 
						
						String.valueOf(CricketFunctions.getEconomy(tournament.getRunsConceded(), tournament.getBallsBowled(), 2, "-"))},null,
						null,new String[] {"-503.0","-262.0","-9.0","250.0","530.0"});
			}
			
			status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
			if(status == Constants.OK) {
				HideAndShowL3rdSubStrapContainers(WhichSide);
				setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
				return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
			} else {
				return status;
			}
		}
	}
	
	public String populateL3rdAllPowerPlay(String whatToProcess, int WhichSide, MatchAllData matchAllData) throws InterruptedException {
		
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES) 
					&& Integer.valueOf(whatToProcess.split(",")[1]) == 2).findAny().orElse(null);
			if(inning == null) {
				return "populateTeamSummary: Inning should be 2";
			}
			this_data_str = new ArrayList<String>();
			for(int i = 0; i <= 5; i++) {
					this_data_str.add("");
				
			}
			String pp_ovrs="";
			if(matchAllData.getMatch().getInning().get(0).getTotalOvers()>= matchAllData.getMatch().getInning().get(0).getFirstPowerplayStartOver()-1) {
				this_data_str.set(0, CricketFunctions.getFirstPowerPlayScore(matchAllData, 1, matchAllData.getEventFile().getEvents()).split(",")[0]);
			}else {
				this_data_str.set(0,"-");
			}
			if(matchAllData.getMatch().getInning().get(0).getTotalOvers() > matchAllData.getMatch().getInning().get(0).getSecondPowerplayStartOver()-1) {
				this_data_str.set(2, CricketFunctions.getSecPowerPlayScore(matchAllData, 1, matchAllData.getEventFile().getEvents()).split(",")[0]);
			}else if(matchAllData.getMatch().getInning().get(0).getTotalOvers() == matchAllData.getMatch().getInning().get(0).getSecondPowerplayStartOver()-1 && matchAllData.getMatch().getInning().get(0).getTotalBalls() > 0) {
				this_data_str.set(2, CricketFunctions.getSecPowerPlayScore(matchAllData,1, matchAllData.getEventFile().getEvents()).split(",")[0]);
			}else {
				this_data_str.set(2, "-");
			}
			if(matchAllData.getMatch().getInning().get(0).getTotalOvers() > matchAllData.getMatch().getInning().get(0).getThirdPowerplayStartOver()-1) {
				this_data_str.set(4, CricketFunctions.getThirdPowerPlayScore(matchAllData, 1, matchAllData.getEventFile().getEvents()).split(",")[0]);
			}else if(matchAllData.getMatch().getInning().get(0).getTotalOvers() == matchAllData.getMatch().getInning().get(0).getThirdPowerplayStartOver()-1 && matchAllData.getMatch().getInning().get(0).getTotalBalls() > 0) {
				this_data_str.set(4, CricketFunctions.getThirdPowerPlayScore(matchAllData,1, matchAllData.getEventFile().getEvents()).split(",")[0]);
			}else {
				this_data_str.set(4, "-");
			}
			
			if(inning.getTotalOvers() >= inning.getFirstPowerplayStartOver()-1) {
				this_data_str.set(1, CricketFunctions.getFirstPowerPlayScore(matchAllData, 2, matchAllData.getEventFile().getEvents()).split(",")[0]);

			}else {
				this_data_str.set(1,"-");	
			}
			
			if(inning.getTotalOvers() > inning.getSecondPowerplayStartOver()-1) {
				this_data_str.set(3, CricketFunctions.getSecPowerPlayScore(matchAllData,2, matchAllData.getEventFile().getEvents()).split(",")[0]);
				
			}else if(inning.getTotalOvers() == inning.getSecondPowerplayStartOver()-1 && inning.getTotalBalls() > 0) {
				this_data_str.set(3, CricketFunctions.getSecPowerPlayScore(matchAllData,2, matchAllData.getEventFile().getEvents()).split(",")[0]);
				
			}else {
				this_data_str.set(3,"-");	
			}
			
			if(inning.getTotalOvers() > inning.getThirdPowerplayStartOver()-1) {
				this_data_str.set(5, CricketFunctions.getThirdPowerPlayScore(matchAllData, 2, matchAllData.getEventFile().getEvents()).split(",")[0]);

			}else if(inning.getTotalOvers() == inning.getThirdPowerplayStartOver()-1 && inning.getTotalBalls() > 0) {
				this_data_str.set(5, CricketFunctions.getThirdPowerPlayScore(matchAllData,2, matchAllData.getEventFile().getEvents()).split(",")[0]);

			}else {
				this_data_str.set(5,"-");	
			}
			
			pp_ovrs= CricketFunctions.PowerPlayMatchOvers(inning.getInningNumber(),matchAllData,",");
		
			String pp1="P1"+","+ pp_ovrs.split(",")[0]+","+this_data_str.get(0)+","+this_data_str.get(1);
			String pp2="P2"+","+pp_ovrs.split(",")[1]+","+this_data_str.get(2)+","+this_data_str.get(3);
			String pp3="P3"+","+pp_ovrs.split(",")[2]+","+this_data_str.get(4)+","+this_data_str.get(5);
			
			lowerThird = new LowerThird("POWERPLAY COMPARISON","", "",matchAllData.getSetup().getHomeTeam().getTeamName4(), matchAllData.getSetup().getAwayTeam().getTeamName4(),"", 
				4, "FLAG","",new String[]{"   ","OVERS",inning.getBowling_team().getTeamName1(),inning.getBatting_team().getTeamName1()},
				new String[]{"",pp1,pp2,pp3},
				null,null,
				new String[] {"-542.0","-328.0","4.0","409.0"});
						
			status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
			if(status == Constants.OK) {
				HideAndShowL3rdSubStrapContainers(WhichSide);
				setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
				return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
			} else {
				return status;
			}
		}
	}

	public String populateL3rdPowerPlay(String whatToProcess, int WhichSide, MatchAllData matchAllData) throws InterruptedException {
		String powerplay = "";
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			if(inning == null) {
				return status;
			}
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				String pp_ovrs="";String pp="",Subheader = "",run_rate="";
				
				if(whatToProcess.split(",")[2].equalsIgnoreCase("p1")) {
					powerplay = "POWERPLAY 1";
					Subheader = "(OVERS " + inning.getFirstPowerplayStartOver() + " TO " + inning.getFirstPowerplayEndOver() + ")";
					pp_ovrs= CricketFunctions.PowerPlayMatchOvers(inning.getInningNumber(),matchAllData,",");
					pp=CricketFunctions.getFirstPowerPlayScore(matchAllData,inning.getInningNumber(), matchAllData.getEventFile().getEvents());
					run_rate = CricketFunctions.generateRunRates(Integer.valueOf(pp.split(",")[0].split("-")[0]),Integer.valueOf(pp_ovrs.split(",")[0].split("-")[0]),
							Integer.valueOf(pp_ovrs.split(",")[0].split("-")[1]),2,matchAllData);

				}else if(whatToProcess.split(",")[2].equalsIgnoreCase("p2")) {
					powerplay = "POWERPLAY 2";
					Subheader = "(OVERS " + inning.getSecondPowerplayStartOver() + " TO " + inning.getSecondPowerplayEndOver() + ")";
					pp_ovrs= CricketFunctions.PowerPlayMatchOvers(inning.getInningNumber(),matchAllData,",");
					pp=CricketFunctions.getSecPowerPlayScore(matchAllData, inning.getInningNumber(), matchAllData.getEventFile().getEvents());
//					System.out.println("pp_ovrs = " + pp_ovrs);
					run_rate = CricketFunctions.generateRunRates(Integer.valueOf(pp.split(",")[0].split("-")[0]),Integer.valueOf(pp_ovrs.split(",")[1].split("-")[0]),
							Integer.valueOf(pp_ovrs.split(",")[1].split("-")[1]),2,matchAllData);

				}else if(whatToProcess.split(",")[2].equalsIgnoreCase("p3")) {
					powerplay = "POWERPLAY 3";
					Subheader = "(OVERS " + inning.getThirdPowerplayStartOver() + " TO " + inning.getThirdPowerplayEndOver() + ")";
					pp_ovrs= CricketFunctions.PowerPlayMatchOvers(inning.getInningNumber(),matchAllData,",");
					pp=CricketFunctions.getThirdPowerPlayScore(matchAllData, inning.getInningNumber(), matchAllData.getEventFile().getEvents());
					run_rate = CricketFunctions.generateRunRates(Integer.valueOf(pp.split(",")[0].split("-")[0]),Integer.valueOf(pp_ovrs.split(",")[2].split("-")[0]),
							Integer.valueOf(pp_ovrs.split(",")[2].split("-")[1]),2,matchAllData);

				} 
				lowerThird = new LowerThird(powerplay,inning.getBatting_team().getTeamName1(), "",whatToProcess.split(",")[2], Subheader,"", 
						2, "" ,inning.getBatting_team().getTeamName4(),new String[]{"RUNS","WICKETS","RUN RATE","DOTS","FOURS","SIXES"},
						new String[]{pp.split(",")[0].split("-")[0],pp.split(",")[0].split("-")[1],
						run_rate,pp.split(",")[3],pp.split(",")[1],pp.split(",")[2]},null,
						null,new String[] {"-530.0","-336.0","-122.0","115.0","344.0","560.0"});
				break;
			case Constants.ISPL:
				String pp1 = "",pp2 = "",tName = "",tName4 = "";
				powerplayData = CricketFunctions.AllpowerplayScores(tournament_matches, matchAllData, null, "PAST_CURRENT");
				for (int i = 0; i<= powerplayData.size() -1; i++) {
					if(powerplayData.get(i).getTeam().getTeamId() == Integer.valueOf(whatToProcess.split(",")[2])) {
						pp1 = powerplayData.get(i).getTotal_runs().get(0) + "-" + powerplayData.get(i).getTotal_wickets().get(0);
						pp2 = powerplayData.get(i).getTotal_runs().get(1) + "-" + powerplayData.get(i).getTotal_wickets().get(1);
						tName = powerplayData.get(i).getTeam().getTeamName1();
						tName4 = powerplayData.get(i).getTeam().getTeamName4();
					}
				}
				
				lowerThird = new LowerThird("",tName, "POWERPLAY ANALYSIS",whatToProcess.split(",")[2], "","", 
						3, "" ,tName4,new String[]{"","MANDATORY POWERPLAY","BATTING POWERPLAY"},
						new String[]{"",pp1,pp2},new String[]{"RUNS","WICKETS"},
						null,new String[] {"0.0","252.0","701.0"});
				break;
			}
				status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
				if(status == Constants.OK) {
					HideAndShowL3rdSubStrapContainers(WhichSide);
					setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
					return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
				} else {
					return status;
				}
			}
	}
	
	public String populateL3rdInningPowerPlay(String whatToProcess, int WhichSide, MatchAllData matchAllData) throws InterruptedException {
		
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber()==Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
			if(inning == null) {
				return "populateTeamSummary: Inning is Not Found";
			}
			
			String p1 = "-",p2 = "-",p3 = "-",p4 = "-",log_50_type = "",log_50_runs = "",challengeRuns = "";
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				String pp1="",pp2="",pp3="",run_rate="";
				
				String pp_ovrs="";
				if(inning.getInningNumber()==1) {
					
					pp_ovrs=CricketFunctions.PowerPlayMatchOvers(inning.getInningNumber(),matchAllData,",");
					
					if(inning.getTotalOvers() >= inning.getFirstPowerplayStartOver()-1) {
						pp1=CricketFunctions.getFirstPowerPlayScore(matchAllData,inning.getInningNumber(), matchAllData.getEventFile().getEvents()).replace("-", ",");
						
						run_rate = CricketFunctions.generateRunRates(Integer.valueOf(pp1.split(",")[0].split("-")[0]),Integer.valueOf(pp_ovrs.split(",")[0].split("-")[0]),
								Integer.valueOf(pp_ovrs.split(",")[0].split("-")[1]),2,matchAllData) + ",";
						
					}else {
						pp1 = "-,-,-,-" ;
						run_rate = "-" + ",";
					}
					
					if(inning.getTotalOvers() > inning.getSecondPowerplayStartOver()-1) {
						pp2=CricketFunctions.getSecPowerPlayScore(matchAllData, inning.getInningNumber(), matchAllData.getEventFile().getEvents()).replace("-", ",");
						run_rate = run_rate + CricketFunctions.generateRunRates(Integer.valueOf(pp2.split(",")[0].split("-")[0]),Integer.valueOf(pp_ovrs.split(",")[1].split("-")[0]),
								Integer.valueOf(pp_ovrs.split(",")[1].split("-")[1]),2,matchAllData) + ",";
						
					}else if(inning.getTotalOvers() == inning.getSecondPowerplayStartOver()-1 && inning.getTotalBalls() > 0) {
						pp2=CricketFunctions.getSecPowerPlayScore(matchAllData, inning.getInningNumber(), matchAllData.getEventFile().getEvents()).replace("-", ",");
						run_rate = run_rate + CricketFunctions.generateRunRates(Integer.valueOf(pp2.split(",")[0].split("-")[0]),Integer.valueOf(pp_ovrs.split(",")[1].split("-")[0]),
								Integer.valueOf(pp_ovrs.split(",")[1].split("-")[1]),2,matchAllData) + ",";
						
					}else {
						pp2 = "-,-,-,-" ;
						run_rate = run_rate + "-" + ",";
					}
					
					if(inning.getTotalOvers() > inning.getThirdPowerplayStartOver()-1) {
						pp3=CricketFunctions.getThirdPowerPlayScore(matchAllData,inning.getInningNumber(), matchAllData.getEventFile().getEvents()).replace("-", ",");
						run_rate = run_rate + CricketFunctions.generateRunRates(Integer.valueOf(pp3.split(",")[0].split("-")[0]),
								Integer.valueOf(pp_ovrs.split(",")[2].split("-")[0]),Integer.valueOf(pp_ovrs.split(",")[2].split("-")[1]),2,matchAllData);
						
					}else if(inning.getTotalOvers() == inning.getThirdPowerplayStartOver()-1 && inning.getTotalBalls() > 0) {
						pp3=CricketFunctions.getThirdPowerPlayScore(matchAllData,inning.getInningNumber(), matchAllData.getEventFile().getEvents()).replace("-", ",");
						run_rate = run_rate + CricketFunctions.generateRunRates(Integer.valueOf(pp3.split(",")[0].split("-")[0]),
								Integer.valueOf(pp_ovrs.split(",")[2].split("-")[0]),Integer.valueOf(pp_ovrs.split(",")[2].split("-")[1]),2,matchAllData);
						
					}else {
						pp3 = "-,-,-,-" ;
						run_rate = run_rate + "-";
					}
					
				}else if(inning.getInningNumber()==2) {
					
					pp_ovrs=CricketFunctions.PowerPlayMatchOvers(inning.getInningNumber(),matchAllData,",");
					
					if(inning.getTotalOvers() >= inning.getFirstPowerplayStartOver()-1) {
						pp1=CricketFunctions.getFirstPowerPlayScore(matchAllData,inning.getInningNumber(), matchAllData.getEventFile().getEvents()).replace("-", ",");
						
						run_rate = CricketFunctions.generateRunRates(Integer.valueOf(pp1.split(",")[0].split("-")[0]),Integer.valueOf(pp_ovrs.split(",")[0].split("-")[0]),
								Integer.valueOf(pp_ovrs.split(",")[0].split("-")[1]),2,matchAllData) + ",";
						
					}else {
						pp1 = "-,-,-,-" ;
						run_rate = "-" + ",";
					}
					
					if(inning.getTotalOvers() >= inning.getSecondPowerplayStartOver()-1) {
						pp2=CricketFunctions.getSecPowerPlayScore(matchAllData, inning.getInningNumber(), matchAllData.getEventFile().getEvents()).replace("-", ",");
						
						run_rate = run_rate + CricketFunctions.generateRunRates(Integer.valueOf(pp2.split(",")[0].split("-")[0]),Integer.valueOf(pp_ovrs.split(",")[1].split("-")[0]),
								Integer.valueOf(pp_ovrs.split(",")[1].split("-")[1]),2,matchAllData) + ",";
						
					}else {
						pp2 = "-,-,-,-" ;
						run_rate = run_rate + "-" + ",";
					}
					
					if(inning.getTotalOvers() >= inning.getThirdPowerplayStartOver()-1) {
						pp3=CricketFunctions.getThirdPowerPlayScore(matchAllData,inning.getInningNumber(), matchAllData.getEventFile().getEvents()).replace("-", ",");
						
						run_rate = run_rate + CricketFunctions.generateRunRates(Integer.valueOf(pp3.split(",")[0].split("-")[0]),
								Integer.valueOf(pp_ovrs.split(",")[2].split("-")[0]),Integer.valueOf(pp_ovrs.split(",")[2].split("-")[1]),2,matchAllData);
						
					}else {
						pp3 = "-,-,-,-" ;
						run_rate = run_rate + "-";
					}
				}
				
				lowerThird = new LowerThird(inning.getBatting_team().getTeamName1(),"", "","POWERPLAY SUMMARY", "","", 
						4,"",inning.getBatting_team().getTeamName4(),new String[]{"OVERS","RUNS","WICKETS","RUN RATE","FOURS","SIXES"},
						new String[]{"",pp_ovrs,pp1,pp2,pp3,run_rate},null,null,new String[] {"-517.0","-328.0","-122.0","115.0","344.0","560.0"});
					
				break;
			case Constants.ISPL:
				if(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).size() <= 0) {
					lowerThird = new LowerThird(inning.getBatting_team().getTeamName2(),"", "",inning.getBatting_team().getTeamName3(), "","", 
							2,"",inning.getBatting_team().getTeamName4(),new String[]{"POWERPLAY","OVERS 3-5","OVERS 6-8","OVERS 9-10"},
							new String[]{"-","-","-","-"},null,null,new String[] {"67.0","317.0","536.0","761.0"});
				}else {
					if(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).size()) == 1) {
						
						p1 = Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[0])+ "-" + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[1]);
						
						lowerThird = new LowerThird("INNINGS PROGRESSION","", "",inning.getBatting_team().getTeamName1(), "","", 
								2,"",inning.getBatting_team().getTeamName4(),new String[]{"POWERPLAY","OVERS 3-5","OVERS 6-8","OVERS 9-10"},
								new String[]{p1,p2,p3,p4},null,null,new String[] {"67.0","317.0","536.0","761.0"});
					}else if(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).size()) == 2) {
						
						p1 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(1).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[1]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(1).split("-")[1]));
						
						lowerThird = new LowerThird("INNINGS PROGRESSION","", "",inning.getBatting_team().getTeamName1(), "","", 
								2,"",inning.getBatting_team().getTeamName4(),new String[]{"POWERPLAY","OVERS 3-5","OVERS 6-8","OVERS 9-10"},
								new String[]{p1,p2,p3,p4},null,null,new String[] {"67.0","317.0","536.0","761.0"});
					}else if(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).size()) == 3) {
						
						p1 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(1).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[1]) + 
										Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(1).split("-")[1]));
						
						p2 = Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(2).split("-")[0]) + "-" + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(2).split("-")[1]);
						
						lowerThird = new LowerThird("INNINGS PROGRESSION","", "",inning.getBatting_team().getTeamName1(), "","", 
								2,"",inning.getBatting_team().getTeamName4(),new String[]{"POWERPLAY","OVERS 3-5","OVERS 6-8","OVERS 9-10"},
								new String[]{p1,p2,p3,p4},null,null,new String[] {"67.0","317.0","536.0","761.0"});
					}else if(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).size()) == 4) {
						
						p1 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(1).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[1]) + 
										Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(1).split("-")[1]));
						
						p2 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(2).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(3).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(2).split("-")[1]) + 
										Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(3).split("-")[1]));
						
						lowerThird = new LowerThird("INNINGS PROGRESSION","", "",inning.getBatting_team().getTeamName1(), "","", 
								2,"",inning.getBatting_team().getTeamName4(),new String[]{"POWERPLAY","OVERS 3-5","OVERS 6-8","OVERS 9-10"},
								new String[]{p1,p2,p3,p4},null,null,new String[] {"67.0","317.0","536.0","761.0"});
					}else if(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).size()) == 5) {
						
						p1 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(1).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[1]) + 
										Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(1).split("-")[1]));
						
						p2 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(2).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(3).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(4).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(2).split("-")[1]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(3).split("-")[1]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(4).split("-")[1]));
						
						
						lowerThird = new LowerThird("INNINGS PROGRESSION","", "",inning.getBatting_team().getTeamName1(), "","", 
								2,"",inning.getBatting_team().getTeamName4(),new String[]{"POWERPLAY","OVERS 3-5","OVERS 6-8","OVERS 9-10"},
								new String[]{p1,p2,p3,p4},null,null,new String[] {"67.0","317.0","536.0","761.0"});
					}else if(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).size()) == 6) {
						
						p1 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(1).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[1]) + 
							Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(1).split("-")[1]));
						
						p2 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(2).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(3).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(4).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(2).split("-")[1]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(3).split("-")[1]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(4).split("-")[1]));
						
						p3 = Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(5).split("-")[0]) + "-" + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(5).split("-")[1]);
						
						if ((matchAllData.getEventFile().getEvents() != null) && (matchAllData.getEventFile().getEvents().size() > 0)) {
							for(Event evnt: matchAllData.getEventFile().getEvents()) {
								if(evnt.getEventInningNumber() == inning.getInningNumber()) {
									if(evnt.getEventExtra() != null) {
										if(evnt.getEventType().equalsIgnoreCase("LOG_50_50")) {
											log_50_type = evnt.getEventExtra();
											log_50_runs = String.valueOf(evnt.getEventExtraRuns());
											challengeRuns = String.valueOf(evnt.getEventTotalRunsInAnOver());
										}
									}
								}
							}
						}
						
						if(log_50_type.trim().isEmpty() && log_50_runs.trim().isEmpty()) {
							lowerThird = new LowerThird("INNINGS PROGRESSION","", "",inning.getBatting_team().getTeamName1(), "","", 
									2,"",inning.getBatting_team().getTeamName4(),new String[]{"POWERPLAY","OVERS 3-5","OVERS 6-8","OVERS 9-10"},
									new String[]{p1,p2,p3,p4},null,null,new String[] {"67.0","317.0","536.0","761.0"});
						}else {
							int cr = 0;
							if(log_50_type.equalsIgnoreCase("-")) {
								cr = (Integer.valueOf(p3.split("-")[0]) - Integer.valueOf(log_50_runs));
							}else if(log_50_type.equalsIgnoreCase("+")){
								cr = (Integer.valueOf(p3.split("-")[0]) + Integer.valueOf(log_50_runs));
							}
							System.out.println("cr = " + cr + "   p3 = " + p3);
							lowerThird = new LowerThird("INNINGS PROGRESSION", challengeRuns + " (" + log_50_type + log_50_runs + ")", "",inning.getBatting_team().getTeamName1(), "","", 
									3,"",inning.getBatting_team().getTeamName4(),new String[]{"POWERPLAY","OVERS 3-5","OVERS 6-8","OVERS 9-10"},
									new String[]{p1,p2,cr + "-" + p3.split("-")[1],p4},null,null,new String[] {"67.0","317.0","536.0","761.0"});
						}
					}else if(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).size()) == 7) {
						
						p1 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(1).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[1]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(1).split("-")[1]));
						
						p2 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(2).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(3).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(4).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(2).split("-")[1]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(3).split("-")[1]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(4).split("-")[1]));
						
						p3 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(5).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(6).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(5).split("-")[1]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(6).split("-")[1]));
						
						if ((matchAllData.getEventFile().getEvents() != null) && (matchAllData.getEventFile().getEvents().size() > 0)) {
							for(Event evnt: matchAllData.getEventFile().getEvents()) {
								if(evnt.getEventInningNumber() == inning.getInningNumber()) {
									if(evnt.getEventExtra() != null) {
										if(evnt.getEventType().equalsIgnoreCase("LOG_50_50")) {
											log_50_type = evnt.getEventExtra();
											log_50_runs = String.valueOf(evnt.getEventExtraRuns());
											challengeRuns = String.valueOf(evnt.getEventTotalRunsInAnOver());
										}
									}
								}
							}
						}
						
						if(log_50_type.trim().isEmpty() && log_50_runs.trim().isEmpty()) {
							lowerThird = new LowerThird("INNINGS PROGRESSION","", "",inning.getBatting_team().getTeamName1(), "","", 
									2,"",inning.getBatting_team().getTeamName4(),new String[]{"POWERPLAY","OVERS 3-5","OVERS 6-8","OVERS 9-10"},
									new String[]{p1,p2,p3,p4},null,null,new String[] {"67.0","317.0","536.0","761.0"});
						}else {
							int cr = 0;
							if(log_50_type.equalsIgnoreCase("-")) {
								cr = (Integer.valueOf(p3.split("-")[0]) - Integer.valueOf(log_50_runs));
							}else if(log_50_type.equalsIgnoreCase("+")){
								cr = (Integer.valueOf(p3.split("-")[0]) + Integer.valueOf(log_50_runs));
							}
							System.out.println("cr = " + cr + "   p3 = " + p3);
							lowerThird = new LowerThird("INNINGS PROGRESSION", challengeRuns + " (" + log_50_type + log_50_runs + ")", "",inning.getBatting_team().getTeamName1(), "","", 
									3,"",inning.getBatting_team().getTeamName4(),new String[]{"POWERPLAY","OVERS 3-5","OVERS 6-8","OVERS 9-10"},
									new String[]{p1,p2,cr + "-" + p3.split("-")[1],p4},null,null,new String[] {"67.0","317.0","536.0","761.0"});
						}
					}else if(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).size()) == 8) {
						
						p1 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(1).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[1]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(1).split("-")[1]));
						
						p2 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(2).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(3).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(4).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(2).split("-")[1]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(3).split("-")[1]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(4).split("-")[1]));
						
						p3 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(5).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(6).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(7).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(5).split("-")[1]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(6).split("-")[1]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(7).split("-")[1]));
						
						if ((matchAllData.getEventFile().getEvents() != null) && (matchAllData.getEventFile().getEvents().size() > 0)) {
							for(Event evnt: matchAllData.getEventFile().getEvents()) {
								if(evnt.getEventInningNumber() == inning.getInningNumber()) {
									if(evnt.getEventExtra() != null) {
										if(evnt.getEventType().equalsIgnoreCase("LOG_50_50")) {
											log_50_type = evnt.getEventExtra();
											log_50_runs = String.valueOf(evnt.getEventExtraRuns());
											challengeRuns = String.valueOf(evnt.getEventTotalRunsInAnOver());
										}
									}
								}
							}
						}
						
						if(log_50_type.trim().isEmpty() && log_50_runs.trim().isEmpty()) {
							lowerThird = new LowerThird("INNINGS PROGRESSION","", "",inning.getBatting_team().getTeamName1(), "","", 
									2,"",inning.getBatting_team().getTeamName4(),new String[]{"POWERPLAY","OVERS 3-5","OVERS 6-8","OVERS 9-10"},
									new String[]{p1,p2,p3,p4},null,null,new String[] {"67.0","317.0","536.0","761.0"});
						}else {
							int cr = 0;
							if(log_50_type.equalsIgnoreCase("-")) {
								cr = (Integer.valueOf(p3.split("-")[0]) - Integer.valueOf(log_50_runs));
							}else if(log_50_type.equalsIgnoreCase("+")){
								cr = (Integer.valueOf(p3.split("-")[0]) + Integer.valueOf(log_50_runs));
							}
							System.out.println("cr = " + cr + "   p3 = " + p3);
							lowerThird = new LowerThird("INNINGS PROGRESSION", challengeRuns + " (" + log_50_type + log_50_runs + ")", "",inning.getBatting_team().getTeamName1(), "","", 
									3,"",inning.getBatting_team().getTeamName4(),new String[]{"POWERPLAY","OVERS 3-5","OVERS 6-8","OVERS 9-10"},
									new String[]{p1,p2,cr + "-" + p3.split("-")[1],p4},null,null,new String[] {"67.0","317.0","536.0","761.0"});
						}
					}else if(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).size()) == 9) {
						
						p1 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(1).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[1]) + 
										Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(1).split("-")[1]));
						
						p2 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(2).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(3).split("-")[0]) + 
										Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(4).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(2).split("-")[1]) + 
										Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(3).split("-")[1]) + 
												Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(4).split("-")[1]));
						
						p3 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(5).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(6).split("-")[0]) + 
										Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(7).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(5).split("-")[1]) + 
										Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(6).split("-")[1]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(7).split("-")[1]));
						
						
						p4 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(8).split("-")[0])+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(8).split("-")[1])));
						
						
						if ((matchAllData.getEventFile().getEvents() != null) && (matchAllData.getEventFile().getEvents().size() > 0)) {
							for(Event evnt: matchAllData.getEventFile().getEvents()) {
								if(evnt.getEventInningNumber() == inning.getInningNumber()) {
									if(evnt.getEventExtra() != null) {
										if(evnt.getEventType().equalsIgnoreCase("LOG_50_50")) {
											log_50_type = evnt.getEventExtra();
											log_50_runs = String.valueOf(evnt.getEventExtraRuns());
											challengeRuns = String.valueOf(evnt.getEventTotalRunsInAnOver());
										}
									}
								}
							}
						}
						
						if(log_50_type.trim().isEmpty() && log_50_runs.trim().isEmpty()) {
							lowerThird = new LowerThird("INNINGS PROGRESSION","", "",inning.getBatting_team().getTeamName1(), "","", 
									2,"",inning.getBatting_team().getTeamName4(),new String[]{"POWERPLAY","OVERS 3-5","OVERS 6-8","OVERS 9-10"},
									new String[]{p1,p2,p3,p4},null,null,new String[] {"67.0","317.0","536.0","761.0"});
						}else {
							int cr = 0;
							if(log_50_type.equalsIgnoreCase("-")) {
								cr = (Integer.valueOf(p3.split("-")[0]) - Integer.valueOf(log_50_runs));
							}else if(log_50_type.equalsIgnoreCase("+")){
								cr = (Integer.valueOf(p3.split("-")[0]) + Integer.valueOf(log_50_runs));
							}
							System.out.println("cr = " + cr + "   p3 = " + p3);
							lowerThird = new LowerThird("INNINGS PROGRESSION", challengeRuns + " (" + log_50_type + log_50_runs + ")", "",inning.getBatting_team().getTeamName1(), "","", 
									3,"",inning.getBatting_team().getTeamName4(),new String[]{"POWERPLAY","OVERS 3-5","OVERS 6-8","OVERS 9-10"},
									new String[]{p1,p2,cr + "-" + p3.split("-")[1],p4},null,null,new String[] {"67.0","317.0","536.0","761.0"});
						}
					}else if(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).size()) == 10) {
						
						p1 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(1).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(0).split("-")[1]) + 
										Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(1).split("-")[1]));
						
						p2 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(2).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(3).split("-")[0]) + 
										Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(4).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(2).split("-")[1]) + 
										Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(3).split("-")[1]) + 
												Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(4).split("-")[1]));
						
						p3 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(5).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(6).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(7).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(5).split("-")[1]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(6).split("-")[1]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(7).split("-")[1]));
						
						
						p4 = (Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(8).split("-")[0]) + 
								Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(9).split("-")[0]))+ "-" + 
								(Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(8).split("-")[1]) + 
										Integer.valueOf(getAllOverData(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(9).split("-")[1]));
						
						if ((matchAllData.getEventFile().getEvents() != null) && (matchAllData.getEventFile().getEvents().size() > 0)) {
							for(Event evnt: matchAllData.getEventFile().getEvents()) {
								if(evnt.getEventInningNumber() == inning.getInningNumber()) {
									if(evnt.getEventExtra() != null) {
										if(evnt.getEventType().equalsIgnoreCase("LOG_50_50")) {
											log_50_type = evnt.getEventExtra();
											log_50_runs = String.valueOf(evnt.getEventExtraRuns());
											challengeRuns = String.valueOf(evnt.getEventTotalRunsInAnOver());
										}
									}
								}
							}
						}
						
						if(log_50_type.trim().isEmpty() && log_50_runs.trim().isEmpty()) {
							lowerThird = new LowerThird("INNINGS PROGRESSION","", "",inning.getBatting_team().getTeamName1(), "","", 
									2,"",inning.getBatting_team().getTeamName4(),new String[]{"POWERPLAY","OVERS 3-5","OVERS 6-8","OVERS 9-10"},
									new String[]{p1,p2,p3,p4},null,null,new String[] {"67.0","317.0","536.0","761.0"});
						}else {
							int cr = 0;
							if(log_50_type.equalsIgnoreCase("-")) {
								cr = (Integer.valueOf(p3.split("-")[0]) - Integer.valueOf(log_50_runs));
							}else if(log_50_type.equalsIgnoreCase("+")){
								cr = (Integer.valueOf(p3.split("-")[0]) + Integer.valueOf(log_50_runs));
							}
							
							System.out.println("cr = " + cr + "   p3 = " + p3);
							lowerThird = new LowerThird("INNINGS PROGRESSION", challengeRuns + " (" + log_50_type + log_50_runs + ")", "",inning.getBatting_team().getTeamName1(), "","", 
									3,"",inning.getBatting_team().getTeamName4(),new String[]{"POWERPLAY","OVERS 3-5","OVERS 6-8","OVERS 9-10"},
									new String[]{p1,p2,cr + "-" + p3.split("-")[1],p4},null,null,new String[] {"67.0","317.0","536.0","761.0"});
						}
					}
				}
				
				
					
				break;	
			}	
			status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
			if(status == Constants.OK) {
				HideAndShowL3rdSubStrapContainers(WhichSide);
				setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
				return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
			} else {
				return status;
			}
		}
	}
	
	public String populateDlsTarget(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		int balls = 0, overs = 0;
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
			if(inning == null) {
				return "populateTeamSummary: Inning is Not Found";
			}
		}
		
		this_data_str = new ArrayList<String>();
		if(dls == null) {
			return "populateTeamSummary: dls is Null";
		}
		
		if(whatToProcess.split(",")[2].equalsIgnoreCase("currentOver")) {
			overs = inning.getTotalOvers();
			balls = inning.getTotalBalls();
		}else if(whatToProcess.split(",")[2].equalsIgnoreCase("nextBall")) {
			overs = inning.getTotalOvers();
			balls = inning.getTotalBalls() + 1;
		}else if(whatToProcess.split(",")[2].equalsIgnoreCase("nextOver")) {
			overs = inning.getTotalOvers() + 1;
			balls = 0;
		}
		
		
		for(int i = 0; i<= dls.size() -1;i++) {
			if(dls.get(i).getOver_left().split("\\.")[0].equalsIgnoreCase(String.valueOf(overs))) {
				for(int j=0;j<6;j++) {
					if(balls == j) {
						this_data_str.add(CricketFunctions.populateDuckWorthLewis(matchAllData).get(i+j).getWkts_down());
						break;
					}
				}
				break;
			}
		}
		
		if(CricketFunctions.populateDls(matchAllData, CricketUtil.FULL, Integer.valueOf(this_data_str.get(0))).trim().isEmpty()) {
			return "error";
		}
		
		this_data_str.add(CricketFunctions.populateDls(matchAllData, CricketUtil.FULL, Integer.valueOf(this_data_str.get(0))));
		
		if(this_data_str == null) {
			return "error";
		}
		
		lowerThird = new LowerThird("DUCKWORTH LEWIS STERN", matchAllData.getSetup().getHomeTeam().getTeamName4(), 
				matchAllData.getSetup().getAwayTeam().getTeamName4(),"", String.valueOf(CricketFunctions.OverBalls(overs, balls)), 
				"",2,"FLAG","", null,null,new String[]{this_data_str.get(0),this_data_str.get(1)},null,null);
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(5, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
//			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 4,print_writers, config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	public String populateLTMatchPromo(String whatToProcess, int WhichSide, MatchAllData matchAllData) throws InterruptedException {
		
		fixture = fixTures.stream().filter(fix -> fix.getMatchnumber() == 
				Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
		
		if(fixture == null) {
			return "Fixture id [" + Integer.valueOf(whatToProcess.split(",")[2]) + "] from database is returning NULL";
		}
		
		String match_name="",newDate = "",date_data = "",text = "";
		
		String[] dateSuffix = {
				"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
				
				"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
				
				"th", "st", "nd", "rd", "th", "th", "th", "th", "th","th",
				
				"th", "st"
		};
		
		if(fixture.getMatchnumber() > 9) {
			match_name = fixture.getMatchfilename();
		}else {
			match_name = "MATCH " + fixture.getMatchnumber();
		}
		
		Calendar cal_bengal = Calendar.getInstance();
		cal_bengal.add(Calendar.DATE, +1);
		if(fixture.getDate().equalsIgnoreCase(new SimpleDateFormat("dd-MM-yyyy").format(cal_bengal.getTime()))) {
			text = "TOMORROW - " + fixture.getLocalTime() + " IST - " + fixture.getVenue();
		}else {
			cal_bengal.add(Calendar.DATE, -1);
			if(fixture.getDate().equalsIgnoreCase(new SimpleDateFormat("dd-MM-yyyy").format(cal_bengal.getTime()))) {
				text = "UP NEXT - " + fixture.getLocalTime() + " IST - " + fixture.getVenue();
			}else {
				newDate = fixture.getDate().split("-")[0];
				if(Integer.valueOf(newDate) < 10) {
					newDate = newDate.replaceFirst("0", "");
				}
				date_data = newDate + dateSuffix[Integer.valueOf(newDate)] + " " + 
						Month.of(Integer.valueOf(fixture.getDate().split("-")[1])) + " " + fixture.getDate().split("-")[2];
				
				text = date_data + " - " + fixture.getLocalTime() + " IST - " + fixture.getVenue();
			}
		}
		
		lowerThird = new LowerThird(fixture.getHome_Team().getTeamName2() + fixture.getHome_Team().getTeamName3(), fixture.getAway_Team().getTeamName2(),
				fixture.getAway_Team().getTeamName3(),fixture.getHome_Team().getTeamName4(), fixture.getAway_Team().getTeamName4(), "",2,"Emirates","",null,null,
				null,null,null);
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	public String populateL3rdMatchSummary(String whatToProcess, int WhichSide, MatchAllData matchAllData) throws InterruptedException {
		ground = Grounds.stream().filter(grnd -> grnd.getFullname().contains(matchAllData.getSetup().getVenueName())).findAny().orElse(null);
		
		if(ground == null) {
			return "populateL3rdMatchSummary: ground name is NULL";
		}
		
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == 2 && inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES))
				.findAny().orElse(null);

		if(inning == null) {
			return "populateL3rdMatchSummary: Inning is Not Found";
		}
		
		String run_wicket="",summary="";int requiredBalls = 0;

		if(matchAllData.getMatch().getInning().get(0).getTotalWickets()==10) {
			run_wicket=String.valueOf(matchAllData.getMatch().getInning().get(0).getTotalRuns());
		}else {
			run_wicket=matchAllData.getMatch().getInning().get(0).getTotalRuns()+"-"+matchAllData.getMatch().getInning().get(0).getTotalWickets();
		}
		int requiredRuns = matchAllData.getMatch().getInning().get(0).getTotalRuns() + 1;
		if(matchAllData.getSetup().getTargetRuns() != 0) {
			requiredRuns = matchAllData.getSetup().getTargetRuns();
		}
		
		if(requiredRuns <= 0) {
			requiredRuns = 0;
		}
		if(matchAllData.getSetup().getTargetOvers() != null && !matchAllData.getSetup().getTargetOvers().trim().isEmpty()) {
			if(matchAllData.getSetup().getTargetOvers().contains(".")) {
				requiredBalls = ((Integer.valueOf(matchAllData.getSetup().getTargetOvers().split(".")[0]) * 6) + 
						Integer.valueOf(matchAllData.getSetup().getTargetOvers().split(".")[1]));
			} else {
				requiredBalls = ((Integer.valueOf(matchAllData.getSetup().getTargetOvers()) * 6));
			}
		}else {
			requiredBalls = ((matchAllData.getSetup().getMaxOvers()) * 6);
		}
		if(requiredBalls <= 0) {
			requiredBalls = 0;
		}
		
		
		summary = inning.getBatting_team().getTeamName1() + " NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " MORE RUN" + 
				CricketFunctions.Plural(CricketFunctions.getRequiredRuns(matchAllData)).toUpperCase() + " TO WIN AT " +
				CricketFunctions.generateRunRate(CricketFunctions.getRequiredRuns(matchAllData),
						0,CricketFunctions.getRequiredBalls(matchAllData),2,matchAllData) + " RUNS PER OVER";
		if(inning != null) {
			
			lowerThird = new LowerThird("MATCH SUMMARY", inning.getBowling_team().getTeamName4(),inning.getBatting_team().getTeamName4(),"", "", "",2,"Emirates","",null,null,
					new String[]{inning.getBowling_team().getTeamName1()+"  "+run_wicket+"  ("+
			CricketFunctions.OverBalls( matchAllData.getMatch().getInning().get(0).getTotalOvers(), matchAllData.getMatch().getInning().get(0).getTotalBalls())+")    ",inning.getBatting_team().getTeamName1()+"  ",
			String.valueOf(inning.getTotalRuns()+"-"+inning.getTotalWickets()+"  "+"("+
					CricketFunctions.OverBalls(inning.getTotalOvers(),inning.getTotalBalls())+") ")},new String[]{summary},null);
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateL3rdComparison (String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)
					&& inn.getInningNumber() == 2).findAny().orElse(null);
			
			if(inning == null) {
				return "populateL3rdComparison: Inning is Not Found";
			}
			
			String in_data= CricketFunctions.compareInning_Data(matchAllData, ",", 1, matchAllData.getEventFile().getEvents());
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.BENGAL_T20:
				lowerThird = new LowerThird("AFTER",  matchAllData.getSetup().getHomeTeam().getTeamName4(), 
						matchAllData.getSetup().getAwayTeam().getTeamName4(),"", "",CricketFunctions.OverBalls(inning.getTotalOvers(), inning.getTotalBalls()), 
						2, "" ,"EVENT",new String[]{"FOURS" + "," + String.valueOf(in_data.split(",")[3]), String.valueOf(inning.getTotalFours()),
						"SIXES" + "," + String.valueOf(in_data.split(",")[2]) , String.valueOf(inning.getTotalSixes())},null,new String[]{inning.getBowling_team().getTeamName1(), 
						" WERE ",  String.valueOf(in_data.split(",")[0] + "-" + in_data.split(",")[1]),inning.getBatting_team().getTeamName1(), 
						"ARE", String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets())},null,new String[] {"328.0","406.0","515.0","585.0"});
				break;
			case Constants.ICC_U19_2023:
				lowerThird = new LowerThird("AFTER",  matchAllData.getSetup().getHomeTeam().getTeamName4(), 
						matchAllData.getSetup().getAwayTeam().getTeamName4(),"", "",CricketFunctions.OverBalls(inning.getTotalOvers(), inning.getTotalBalls()), 
						2, "FLAG" ,"",new String[]{"FOURS" + "," + String.valueOf(in_data.split(",")[3]), String.valueOf(inning.getTotalFours()),
						"SIXES" + "," + String.valueOf(in_data.split(",")[2]) , String.valueOf(inning.getTotalSixes())},null,new String[]{inning.getBowling_team().getTeamName1(), 
						" WERE ",  String.valueOf(in_data.split(",")[0] + "-" + in_data.split(",")[1]),inning.getBatting_team().getTeamName1(), 
						"ARE", String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets())},null,new String[] {"328.0","406.0","515.0","585.0"});
				break;
			case Constants.ISPL:
				lowerThird = new LowerThird("AFTER",  matchAllData.getSetup().getHomeTeam().getTeamName4(), 
						matchAllData.getSetup().getAwayTeam().getTeamName4(),"", "",CricketFunctions.OverBalls(inning.getTotalOvers(), inning.getTotalBalls()), 
						2, "FLAG" ,"ISPL",new String[]{"FOURS" + "," + String.valueOf(in_data.split(",")[3]), String.valueOf(inning.getTotalFours()),
						"SIXES" + "," + String.valueOf(in_data.split(",")[2]) , String.valueOf(inning.getTotalSixes()),
						"NINES" + "," + String.valueOf(in_data.split(",")[4]) , String.valueOf(inning.getTotalNines())},null,new String[]{inning.getBowling_team().getTeamName4(), 
						" ",  String.valueOf(in_data.split(",")[0] + "-" + in_data.split(",")[1]),inning.getBatting_team().getTeamName4(), 
						"", String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets())},null,new String[] {"384.0","464.0","564.0","631.0","741.0","819.0"});
				break;	
			}
			
			status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
			if(status == Constants.OK) {
				HideAndShowL3rdSubStrapContainers(WhichSide);
				setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
				return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
			}else {
				return status;
			}
		}				
	}

	public String PopulateL3rdPlayerProfile(String whatToProcess, int WhichSide, MatchAllData matchAllData) 
		throws JsonMappingException, JsonProcessingException, InterruptedException
	{
		
		
		if(!whatToProcess.contains(",") && whatToProcess.split(",").length >= 4) {
			return "PopulateL3rdPlayerProfile: error occured in what to process";
		}

		FirstPlayerId = Integer.valueOf(whatToProcess.split(",")[2]);
		WhichProfile = whatToProcess.split(",")[3];
		
		if(FirstPlayerId <= 0 || WhichProfile == null) {
			return "PopulateL3rdPlayerProfile: Player Id NOT found [" + FirstPlayerId + "]";
		}
		
		player = CricketFunctions.getPlayerFromMatchData(FirstPlayerId, matchAllData); 
		
		if(player == null) {
			return "PopulateL3rdPlayerProfile: Player Id not found [" + FirstPlayerId + "]";
		}
		
		if(matchAllData.getSetup().getHomeTeamId() == player.getTeamId()) {
			team = matchAllData.getSetup().getHomeTeam();
		} else if(matchAllData.getSetup().getAwayTeamId() == player.getTeamId()) {
			team = matchAllData.getSetup().getAwayTeam();
		} 
		
		if(team == null) {
			return "PopulateL3rdPlayerProfile: Team Id not found [" + player.getTeamId() + "]";
		}
		
		statsType = statsTypes.stream().filter(stype -> stype.getStats_short_name().equalsIgnoreCase(WhichProfile)).findAny().orElse(null);
		if(statsTypes == null) {
			return "PopulateL3rdPlayerProfile: Stats Type not found for profile [" + WhichProfile + "]";
		}
		stat = statistics.stream().filter(st -> st.getPlayer_id() == player.getPlayerId() && statsType.getStats_id() == st.getStats_type_id()).findAny().orElse(null);
		if(stat == null) {
			return "populatePlayerProfile: No stats found for player id [" + player.getFull_name() + "] from database is returning NULL";
		}
		stat.setStats_type(statsType);
		
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.BENGAL_T20:
			switch (WhichProfile.toUpperCase()) {
			case "DT20":
				stat = CricketFunctions.updateTournamentWithH2h(stat, headToHead, matchAllData);
				stat = CricketFunctions.updateStatisticsWithMatchData(stat, matchAllData);
				break;
			}
			break;
		case Constants.ICC_U19_2023:
			stat = CricketFunctions.updateTournamentDataWithStats(stat, tournament_matches, matchAllData);
			stat = CricketFunctions.updateStatisticsWithMatchData(stat, matchAllData);
			break;
		}
		
		
		double average = 0;
		String Data = "",hundred = "",fifty = "",strikeRate = "",
			batAverage = "",economy = "",best = "",runs = "" ;
		
		if(stat.getRuns_conceded() == 0 || stat.getWickets() == 0) {
			Data = "-";
		}else {
			average = stat.getRuns_conceded()/stat.getWickets();
			DecimalFormat df_bo = new DecimalFormat("0.00");
			Data = df_bo.format(average);
		}
		
		if(whatToProcess.split(",")[0].equalsIgnoreCase("F7")) {
			if(stat.getFifties() == 0) {
				fifty = "-";
			}else {
				fifty = String.valueOf(stat.getFifties());
			}
			
			if(stat.getHundreds() == 0) {
				hundred = "-";
			}else {
				hundred = String.valueOf(stat.getHundreds());
			}
			
			if(CricketFunctions.generateStrikeRate(stat.getRuns(), 
					stat.getBalls_faced(), 1).trim().isEmpty()) {
				strikeRate = "-";
			}else {
				strikeRate = String.valueOf(Math.round(Float.valueOf(CricketFunctions.generateStrikeRate(stat.getRuns(), 
						stat.getBalls_faced(), 1))));
			}
			
			if(stat.getRuns() == 0) {
				runs = "-";
			}else {
				runs = String.format("%,d\n", stat.getRuns());
			}
			
			if(CricketFunctions.getAverage(stat.getInnings(), stat.getNot_out(), stat.getRuns(), 2, "-").equalsIgnoreCase("0.00")) {
				batAverage = "-";
			}else {
				batAverage = CricketFunctions.getAverage(stat.getInnings(), stat.getNot_out(), stat.getRuns(), 2, "-");
			}
			
			if(player.getSurname() == null) {
				surName = "";
			}else {
				surName = player.getSurname();
			}
			
			if(stat.getBest_score().equalsIgnoreCase("0")) {
				best = "-";
			}else {
				best = stat.getBest_score();
			}
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.BENGAL_T20:
				if(WhichProfile.equalsIgnoreCase("DT20")) {
					statsType.setStats_short_name("T20 CAREER");
				}else if(WhichProfile.equalsIgnoreCase("IPL")) {
					statsType.setStats_short_name("IPL CAREER");
				}
				lowerThird = new LowerThird("", player.getFirstname(), surName,statsType.getStats_short_name(), "", "", 2,"",team.getTeamName4(),
						new String[]{"MATCHES", "RUNS", "50s", "100s", "BEST", "S/R"},
						new String[]{String.valueOf(stat.getMatches()), runs ,fifty, hundred, best,strikeRate},null,null,
						new String[] {"-3.0","51.0","103.0","155.0","207.0","259.0"});
				break;

			default:
				lowerThird = new LowerThird("", player.getFirstname(), surName,statsType.getStats_full_name(), "", "", 2,"",team.getTeamName4(),
						new String[]{"MATCHES", "RUNS", "AVERAGE", "FIFTIES", "HUNDREDS", "BEST", "STRIKE RATE"},
						new String[]{String.valueOf(stat.getMatches()), runs,batAverage ,fifty, hundred, best,strikeRate},null,null,
						new String[] {"-503.0","-335.0","-173.0","-3.0","173.0","340.0","516.0"});
				break;
			}
			
			
		}	
		else if(whatToProcess.split(",")[0].equalsIgnoreCase("F11")) {
			if(CricketFunctions.getEconomy(stat.getRuns_conceded(), stat.getBalls_bowled(),2,"-").equalsIgnoreCase("0.00")) {
				economy = "-";
			}else {
				economy = CricketFunctions.getEconomy(stat.getRuns_conceded(), stat.getBalls_bowled(),2,"-");
			}
			
			if(player.getSurname() == null) {
				surName = "";
			}else {
				surName = player.getSurname();
			}
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.BENGAL_T20:
				if(WhichProfile.equalsIgnoreCase("DT20")) {
					statsType.setStats_short_name("T20 CAREER");
				}else if(WhichProfile.equalsIgnoreCase("IPL")) {
					statsType.setStats_short_name("IPL CAREER");
				}
				lowerThird = new LowerThird("", player.getFirstname(), surName,statsType.getStats_short_name(), "", "", 2,"",team.getTeamName4(),
						new String[]{"MATCHES", "WKTS", "AVG", "ECON", "5WI", "BEST"},new String[]{String.valueOf(stat.getMatches()), 
						String.valueOf(stat.getWickets()),Data,economy, String.valueOf(stat.getPlus_5()), stat.getBest_figures()},null,null,
						new String[] {"-3.0","51.0","103.0","155.0","207.0","259.0"});
				break;

			default:
				lowerThird = new LowerThird("", player.getFirstname(), surName,statsType.getStats_full_name(), "", "", 2,"",team.getTeamName4(),
						new String[]{"MATCHES", "WICKETS", "AVERAGE", "ECONOMY", "5WI", "BEST"},new String[]{String.valueOf(stat.getMatches()), 
						String.valueOf(stat.getWickets()),Data,economy, String.valueOf(stat.getPlus_5()), 
						stat.getBest_figures()},null,null,new String[] {"-503.0","-279.0","-35.0","206.0","407.0","560.0"});
				break;
			}
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(5, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config,lowerThird.getNumberOfSubLines());
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String PopulateL3rdHeader(String whatToProcess,int WhichSide) throws InterruptedException 
	{
		if(WhichSide == 1) {
			containerName = "$Change_Out";
		}else if(WhichSide == 2) {
			containerName = "$Change_In";
		}
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.BENGAL_T20:
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side"+ WhichSide + 
					"$Select_Impact*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
			break;
		case Constants.ISPL:
			
			if(lowerThird.getWhichTeamFlag().contains("KHILADI XI") || lowerThird.getWhichTeamFlag().contains("MASTER 11")) {
				if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("KHILADI XI")) {
					logo_name = "KHILADI_XI";
				}else if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("MASTER 11")) {
					logo_name = "MASTER_XI";
				}
			}else {
				logo_name = lowerThird.getWhichTeamFlag();
			}
			
			if(!whatToProcess.equalsIgnoreCase("F10")) {
				if(lowerThird.getWhichTeamFlag().contains("KHILADI XI") || lowerThird.getWhichTeamFlag().contains("MASTER 11")) {
					if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("KHILADI XI")) {
						color_name = "KHILADI_XI";
					}else if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("MASTER 11")) {
						color_name = "MASTER_XI";
					}
				}else {
					color_name = lowerThird.getWhichTeamFlag();
				}
				
				switch(whatToProcess) {
				case "Shift_F7": case "Control_F4":
					ltWhichContainer =  "$Image_LowerThirds";
					break;
				default:
					ltWhichContainer =  "$All_LowerThirds";
					break;
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2" + 
						"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + color_name + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base1" + 
						"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + color_name + "\0", print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$TopLine$Side" + WhichSide + "$In$ScoreGrp" + 
//						"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + color_name + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$SubLineBaseGrp$Side" + WhichSide + "$img_Base2" + 
						"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + color_name + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$1$img_Text2" + 
						"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + color_name + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$2$img_Text2" + 
						"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + color_name + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$3$img_Text2" + 
						"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + color_name + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$4$img_Text2" + 
						"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + color_name + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$1$img_Base2" + 
						"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + color_name + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$2$img_Base2" + 
						"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + color_name + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$3$img_Base2" + 
						"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + color_name + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$4$img_Base2" + 
						"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + color_name + "\0", print_writers);
			}
			break;
		}
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023: case Constants.ISPL: case Constants.BENGAL_T20:
			switch(whatToProcess) {
			case "Control_i":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
						"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
						"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 1 \0", print_writers);
				

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getSubTitle()+"("+lowerThird.getScoreText()+")" + "\0", print_writers);
				
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
				break;
			case "Alt_F6":
				if(lowerThird.getWhichTeamFlag() != null) {
					CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					
					if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				}
				
				if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
				break;
			case "Shift_A": case "Shift_R":
				if(lowerThird.getWhichTeamFlag() != null) {
					CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					
					if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				}
				
				if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
				break;
			case "Shift_U":
				if(lowerThird.getWhichTeamFlag() != null) {
					CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				}
				
				if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
				break;
			case "Alt_j":
				if(lowerThird.getWhichTeamFlag() != null) {
					CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				}
				
				if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
				break;	
			case "b":
				if(lowerThird.getWhichTeamFlag() != null) {
					CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				}
				
				if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
				break;	
			case "Alt_w":
				if(lowerThird.getWhichTeamFlag() != null) {
					CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				}
				
				if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
				break;
			case "Control_j":
				if(lowerThird.getWhichTeamFlag() != null) {
					CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				}
				
				if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
				break;
			case "Alt_i":
				if(lowerThird.getWhichTeamFlag() != null) {
					CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				}
				
				if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
				break;	
			case "Alt_q":
				
				CricketFunctions.DoadWriteCommandToSelectedViz(1,"-1 RENDERER*FRONT_LAYER*TREE*$All_POTT_Aramco$Overall_Position_Y$Top_Line$HeaderBand$Select_Sponsor"
						+ "*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToSelectedViz(1,"-1 RENDERER*FRONT_LAYER*TREE*$All_POTT_Aramco$Overall_Position_Y$Top_Line$Data$Sponsor$Select_Sponsor"
						+ "*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2,"-1 RENDERER*FRONT_LAYER*TREE*$All_POTT_Aramco$Overall_Position_Y$Top_Line$HeaderBand$Select_Sponsor"
							+ "*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToSelectedViz(2,"-1 RENDERER*FRONT_LAYER*TREE*$All_POTT_Aramco$Overall_Position_Y$Top_Line$Data$Sponsor$Select_Sponsor"
							+ "*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				}
				if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_POTT_Aramco$Position_With_Graphics$Sublines$Side_1$1$Main$Data$Flag$img_Shadow*ACTIVE SET 0 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_POTT_Aramco$Position_With_Graphics$Sublines$Side_1$1$Main$Data$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_POTT_Aramco$Position_With_Graphics$Sublines$Side_1$1$Main$Data$Flag$img_Flag"
						+ "*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_POTT_Aramco$Position_With_Graphics$Top_Line$Data$TopTexts$Name$"
						+ "txt_Header1*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_POTT_Aramco$Position_With_Graphics$Top_Line$Data$TopTexts$Name$"
						+ "txt_Header2*GEOM*TEXT SET " + "ICC" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_POTT_Aramco$Position_With_Graphics$Top_Line$Data$TopTexts$Name$"
						+ "txt_Header3*GEOM*TEXT SET " + "PLAYER OF THE TOURNAMENT" + "\0", print_writers);
				break;
				
			case "Shift_E":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					if(lowerThird.getSubTitle().equalsIgnoreCase("1") || lowerThird.getSubTitle().equalsIgnoreCase("2")) {
						if(lowerThird.getWhichTeamFlag() != null) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
									"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
										"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
							}else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
										"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
							}
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
									"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						}
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 5 \0", print_writers);
						
						if(lowerThird.getFirstName().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow2*ACTIVE SET 0 \0", print_writers);
						}else if(lowerThird.getSurName().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow2*ACTIVE SET 1 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow2*ACTIVE SET 1 \0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
								"$Select_Flags$Flag_Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getFirstName() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
								"$Select_Flags$Flag_Flag" + containerName + "$img_Flag2*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getSurName() + "\0", print_writers);
						
					}
					
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
					
					break;
				case Constants.ISPL:
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + logo_name + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + logo_name + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 0 \0", print_writers);
					
//					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
//							+ "$Change$ScoreGrp*ACTIVE SET 0 \0", print_writers);
					
					break;	
				}
				break;
			case "Alt_F1": case "Alt_F2":
				if(l3griff.getWhichTeamFlag() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					if(l3griff.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + l3griff.getWhichTeamFlag() + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + l3griff.getFirstName() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "ICC U19 MEN'S CWC 2024" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
				
				break;
			case "Control_F2":
				if(lowerThird.getWhichTeamFlag() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + inning.getBowling_team().getTeamName1() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide + "$Name" + containerName + 
						"$txt_Designation*GEOM*TEXT SET " + match.getSetup().getMatchIdent() + " - " + ground.getCity() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 1 \0", print_writers);
				
				if(inning.getTotalWickets() >= 10) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + inning.getTotalRuns() + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + inning.getTotalRuns() + "-" + inning.getTotalWickets() + "\0", print_writers);
				}
				
				if((inning.getTotalOvers() == 1 && inning.getTotalBalls() == 0) || (inning.getTotalOvers() == 0 && inning.getTotalBalls() > 0)) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + CricketFunctions.OverBalls(inning.getTotalOvers(), inning.getTotalBalls()) 
							+ " OVER" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + CricketFunctions.OverBalls(inning.getTotalOvers(), inning.getTotalBalls()) 
							+ " OVERS" + "\0", print_writers);
				}
				
				
				break;
			case "a":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					if(lowerThird.getWhichTeamFlag() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
								"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " +lowerThird.getSubTitle() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
					break;
				case Constants.ISPL:
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + logo_name + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + logo_name + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 0 \0", print_writers);
					
//					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
//							+ "$Change$ScoreGrp*ACTIVE SET 0 \0", print_writers);
					break;
				}
				
				break;
				
			case "Shift_F3":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" 
							+ "$Select_Subline$1$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET 0.0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" 
							+ "$Select_Subline$2$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET 0.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Team_BadgeAll$Side1$TeamBadgeGrp1" + 
							"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Team_BadgeAll$Side1$TeamBadgeGrp1" + 
							"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Team_BadgeAll$Side1$TeamBadgeGrp2" + 
							"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Team_BadgeAll$Side1$TeamBadgeGrp2" + 
							"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Top_Line" 
							+ "$txt_FirstName*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Top_Line" 
							+ "$txt_LastName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Top_Line" 
							+ "$txt_Designation*GEOM*TEXT SET " + lowerThird.getHeaderText() +CricketFunctions.Plural(Integer.valueOf(lowerThird.getBallsFacedText())).toUpperCase()+ "\0", print_writers);
					
					if(Integer.valueOf(lowerThird.getBallsFacedText()) >= 10) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Top_Line" 
								 + "$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Top_Line"
								 + "$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "-" + lowerThird.getBallsFacedText() + "\0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Top_Line" 
							 + "$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
					break;
				case Constants.ICC_U19_2023:
					if(lowerThird.getWhichTeamFlag() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
								"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + lowerThird.getHeaderText() + 
							CricketFunctions.Plural(Integer.valueOf(lowerThird.getBallsFacedText())).toUpperCase() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 1 \0", print_writers);
					
					if(Integer.valueOf(lowerThird.getBallsFacedText()) >= 10) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
								+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
								+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "-" + lowerThird.getBallsFacedText() + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + logo_name + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + logo_name + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getHeaderText() + 
							CricketFunctions.Plural(Integer.valueOf(lowerThird.getBallsFacedText())).toUpperCase() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$ScoreGrp*ACTIVE SET 1 \0", print_writers);
					
					if(Integer.valueOf(lowerThird.getBallsFacedText()) >= 10) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
								+ "$Change$geom_ScorePositionX$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
								+ "$Change$geom_ScorePositionX$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "-" + lowerThird.getBallsFacedText() + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
					break;
				}
				break;
			case "Alt_Shift_L": case "Control_Shift_P":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					if(lowerThird.getWhichTeamFlag() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
								"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + logo_name + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + logo_name + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getHeaderText() + 
							CricketFunctions.Plural(Integer.valueOf(lowerThird.getBallsFacedText())).toUpperCase() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$ScoreGrp*ACTIVE SET 1 \0", print_writers);
					
					if(Integer.valueOf(lowerThird.getBallsFacedText()) >= 10) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
								+ "$Change$geom_ScorePositionX$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
								+ "$Change$geom_ScorePositionX$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "-" + lowerThird.getBallsFacedText() + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
					break;
				}
				break;	
			case "u":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET 0.0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$2$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET 0.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_FirstName*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_LastName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_Designation*GEOM*TEXT SET " + lowerThird.getSubTitle()+ "\0", print_writers);
					
					System.out.println("lowerThird.getScoreText() = " + lowerThird.getScoreText());
					if(Integer.valueOf(lowerThird.getScoreText().split("-")[1]) >= 10) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
								 + "$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText().split("-")[0] + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
								 + "$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
					break;
				case Constants.ICC_U19_2023:
					if(lowerThird.getWhichTeamFlag() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
								"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 1 \0", print_writers);
					
					if(Integer.valueOf(lowerThird.getScoreText().split("-")[1])>=10) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
								+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText().split("-")[0] + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
								+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					}
					
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$TopLine$Side" + WhichSide +
							"$geom_ScorePositionX*TRANSFORMATION*POSITION*X SET 215.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + logo_name + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + logo_name + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 1 \0", print_writers);
					
					if(Integer.valueOf(lowerThird.getScoreText().split("-")[1])>=10) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
								+ "$Change$geom_ScorePositionX$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText().split("-")[0] + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
								+ "$Change$geom_ScorePositionX$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
					break;
				}
				
				break;
			case "Control_Shift_M":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Ident$Main$Left$In$Mask1" + 
							"$img_Base1*TEXTURE*IMAGE SET " + Constants.BENGAL_BASE_PATH + "1/" + lowerThird.getHeaderText() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Ident$Main$Left$In$Mask2" + 
							"$img_Base1*TEXTURE*IMAGE SET " + Constants.BENGAL_BASE_PATH + "1/" + lowerThird.getHeaderText() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Ident$Team_Badges_Grp$TeamBadgeGrp1" + 
							"$TeamBadgeGrp1$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getHeaderText() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Ident$Team_Badges_Grp$TeamBadgeGrp1" + 
							"$TeamBadgeGrp1$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getHeaderText() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Ident$Team_Badges_Grp$TeamBadgeGrp1" + 
							"$TeamBadgeGrp2$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getHeaderText() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Ident$Team_Badges_Grp$TeamBadgeGrp1" + 
							"$TeamBadgeGrp2$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getHeaderText() + " \0", print_writers);
					
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Ident$Main$Right$In$Mask1" + 
							"$img_Base1*TEXTURE*IMAGE SET " + Constants.BENGAL_BASE_PATH + "1/" + lowerThird.getFirstName() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Ident$Main$Right$In$Mask2" + 
							"$img_Base1*TEXTURE*IMAGE SET " + Constants.BENGAL_BASE_PATH + "1/" + lowerThird.getFirstName() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Ident$Team_Badges_Grp$TeamBadgeGrp2" + 
							"$TeamBadgeGrp1$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getFirstName() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Ident$Team_Badges_Grp$TeamBadgeGrp2" + 
							"$TeamBadgeGrp1$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getFirstName() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Ident$Team_Badges_Grp$TeamBadgeGrp2" + 
							"$TeamBadgeGrp2$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getFirstName() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Ident$Team_Badges_Grp$TeamBadgeGrp2" + 
							"$TeamBadgeGrp2$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getFirstName() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Ident$Main$Left$In$TextIn" + 
							"$img_Text1*TEXTURE*IMAGE SET " + Constants.BENGAL_TEXT_PATH + "1/" + lowerThird.getHeaderText() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Ident$Main$Right$In" + 
							"$img_Text1*TEXTURE*IMAGE SET " + Constants.BENGAL_TEXT_PATH + "1/" + lowerThird.getFirstName() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Ident$Main$Left$In$TextIn" + 
							"$txt_TeamFirstName*GEOM*TEXT SET " + lowerThird.getSubTitle() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Ident$Main$Left$In$TextIn" + 
							"$txt_TeamLastName*GEOM*TEXT SET " + lowerThird.getScoreText() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Ident$Main$Right$In$img_Text1" + 
							"$txt_TeamFirstName*GEOM*TEXT SET " + lowerThird.getBallsFacedText() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Ident$Main$Right$In$img_Text1" + 
							"$txt_TeamLastName*GEOM*TEXT SET " + lowerThird.getWhichSponsor() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Ident$Main$Middle$Bottom" + 
							"$txt_Title*GEOM*TEXT SET " + lowerThird.getSurName() + " \0", print_writers);
					break;
				}	
				
				break;	
			case "d":  case "e":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_FirstName*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_LastName*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_Designation*GEOM*TEXT SET " + ""+ "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Score*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
					break;
				case Constants.ICC_U19_2023:
					if(lowerThird.getWhichTeamFlag() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
								"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + logo_name + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + logo_name + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 0 \0", print_writers);
					break;
				}	
				
				break;
			
			case "88":  //case "n":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
						"$Select_Flags*FUNCTION*Omo*vis_con SET 5 \0", print_writers);
				
				if(lowerThird.getSubTitle().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow2*ACTIVE SET 0 \0", print_writers);
				}else if(lowerThird.getScoreText().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow2*ACTIVE SET 1 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow2*ACTIVE SET 1 \0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
						"$Select_Flags$Flag_Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getSubTitle() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
						"$Select_Flags$Flag_Flag" + containerName + "$img_Flag2*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getScoreText() + "\0", print_writers);
				
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
					+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText()+ "\0", print_writers);
			
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
				break;
				
			case "Control_a":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Projected_LT$Team_BadgeAll" + 
							"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Projected_LT$Team_BadgeAll" + 
							"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Projected_LT$Team_BadgeAll" + 
							"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Projected_LT$Team_BadgeAll" + 
							"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Projected_LT$Top_Line"	
							+ "$txt_tName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Projected_LT$Top_Line"
							+ "$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Projected_LT$Top_Line"
							+ "$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getBallsFacedText()+ "\0", print_writers);
					break;
				case Constants.ICC_U19_2023:
					if(lowerThird.getWhichTeamFlag() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
								"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
					}

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 1 \0", print_writers);
					if(Integer.valueOf(lowerThird.getScoreText().split("-")[1])>=10) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
								+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText().split("-")[0] + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
								+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
					
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$TopLine$Side" + WhichSide +
							"$geom_ScorePositionX*TRANSFORMATION*POSITION*X SET 215.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + logo_name + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + logo_name + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 1 \0", print_writers);
					
					if(Integer.valueOf(lowerThird.getScoreText().split("-")[1])>=10) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
								+ "$Change$geom_ScorePositionX$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText().split("-")[0] + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
								+ "$Change$geom_ScorePositionX$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
					
					break;
				}
				break;
			
			case "l":
				if(lowerThird.getWhichTeamFlag() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				}

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
				
				if(lowerThird.getSubTitle().equalsIgnoreCase("Economy")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				}else if(lowerThird.getSubTitle().equalsIgnoreCase("Career")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "U19 ODI CAREER" + "\0", print_writers);
				}else if(lowerThird.getSubTitle().equalsIgnoreCase("Tournament")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET ICC U19 MEN’S CWC 2024 \0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 1 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " +"" + "\0", print_writers);
				
				break;
				
			case "Control_F3":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_FirstName*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_LastName*GEOM*TEXT SET " + lowerThird.getHeaderText() + " " +lowerThird.getBallsFacedText() 
							+ " OVERS" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_Designation*GEOM*TEXT SET " + ""+ "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Score*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
					break;
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 5 \0", print_writers);
					
					if(lowerThird.getFirstName().equalsIgnoreCase("NEP")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow2*ACTIVE SET 0 \0", print_writers);
					}else if(lowerThird.getSurName().equalsIgnoreCase("NEP")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow2*ACTIVE SET 1 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow2*ACTIVE SET 1 \0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag_Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag_Flag" + containerName + "$img_Flag2*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getSurName() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + " " +lowerThird.getBallsFacedText() 
							+ " OVERS"+ "\0", print_writers);
				
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + logo_name + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + logo_name + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getBallsFacedText() 
							+ " OVERS" + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 0 \0", print_writers);
					break;
				}
				break;
			
			case "Shift_B":
				
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 5 \0", print_writers);
					
					if(lowerThird.getFirstName().equalsIgnoreCase("NEP")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow2*ACTIVE SET 0 \0", print_writers);
					}else if(lowerThird.getSurName().equalsIgnoreCase("NEP")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow2*ACTIVE SET 1 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow2*ACTIVE SET 1 \0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag_Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag_Flag" + containerName + "$img_Flag2*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getSurName() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
				
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
					break;
				case Constants.BENGAL_T20:
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET -3.0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$2$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET -3.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + "EVENT" + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + "EVENT" + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + "EVENT" + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + "EVENT" + " \0", print_writers);
					
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
				
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
					
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_FirstName*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_LastName*GEOM*TEXT SET \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_Designation*GEOM*TEXT SET " + ""+ "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Score*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
					break;	
				}
				
				break;
			case "Control_h":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					if(lowerThird.getWhichTeamFlag() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
								"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
					}

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " +"" + "\0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + logo_name + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + logo_name + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 0 \0", print_writers);
					break;
				}
				break;	
			case "Alt_o":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET 0.0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$2$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET 0.0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_FirstName*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_LastName*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_Designation*GEOM*TEXT SET " + ""+ "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Score*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
					break;
				case Constants.ICC_U19_2023:
					if(lowerThird.getWhichTeamFlag() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
								"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + logo_name + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + logo_name + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 0 \0", print_writers);
					break;
				}
				
				break;
				
			case "Alt_d":
				if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 5 \0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
						"$Select_Flags*FUNCTION*Omo*vis_con SET 5 \0", print_writers);

				if(lowerThird.getFirstName().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow2*ACTIVE SET 0 \0", print_writers);
				}else if(lowerThird.getSurName().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow2*ACTIVE SET 1 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow2*ACTIVE SET 1 \0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
						"$Select_Flags$Flag_Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getFirstName() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
						"$Select_Flags$Flag_Flag" + containerName + "$img_Flag2*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getSurName() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
				break;
			case "Shift_F7": case "Control_F4":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Image_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + logo_name + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Image_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + logo_name + "\0", print_writers);
					if(config.getPrimaryIpAddress().equalsIgnoreCase(Constants.LOCALHOST)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Image_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$img_Player" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_PHOTO_PATH + logo_name + "\\\\" + lowerThird.getWhichSponsor() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
					}else {
						if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.Local_ISPL_PHOTOS_PATH + logo_name + "\\\\" + lowerThird.getWhichSponsor() + CricketUtil.PNG_EXTENSION ).exists()) {
							return "Photo not found "+lowerThird.getWhichSponsor();
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Image_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$img_Player" + 
								"*TEXTURE*IMAGE SET " + "\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.Local_ISPL_PHOTOS_PATH + logo_name + "\\\\" + lowerThird.getWhichSponsor() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
					}
					
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Image_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Image_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Image_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 0 \0", print_writers);
					break;
				}	
				
				break;	
			case "Control_F5": case "Control_F9":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side"+ WhichSide + 
							"$Select_Impact*FUNCTION*Omo*vis_con SET "+impactOmo+" \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							
							+ "$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_Designation*GEOM*TEXT SET " + lowerThird.getHeaderText()+ "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Score*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
					break;
				case Constants.ICC_U19_2023:
					if(lowerThird.getWhichTeamFlag() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide + 
								"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide + "$obj_WidthX"
								+ "*FUNCTION*Maxsize*WIDTH_X SET 1020\0", print_writers);
					}

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + logo_name + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + logo_name + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 0 \0", print_writers);
					break;
				}	
				
				break;
				
			case "F8": case "Alt_F8":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Overall_Position_Y$Sublines$Side_" + WhichSide + "$Select_Subline"
							+ "*FUNCTION*Omo*vis_con SET 1  \0", print_writers);
					if(lowerThird.getWhichTeamFlag() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
								"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
					}

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Overall_Position_Y$Sublines$Side_" + WhichSide + "$Select_Subline"
							+ "*FUNCTION*Omo*vis_con SET 1  \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + logo_name + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + logo_name + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 0 \0", print_writers);
					break;
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Score*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
					break;
				}
				
				break;
			case "Control_Shift_F":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					if(lowerThird.getWhichTeamFlag() != null) {
						CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						
						if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
								"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
					}
					
					if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
						CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " V " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + "" + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
					break;
				}
				break;
				
			case "F5":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					String containerForSponsor = "";
					if(lowerThird.getWhichSponsor().toUpperCase().equalsIgnoreCase("SPONSOR")) {
						WhichSide = 1;
						containerForSponsor = "$All_LowerThirds$gfx_BatsmanScore_LT";
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + containerForSponsor + "$Sublines$Side1"
								+ "$Data1$txt_Title*TRANSFORMATION*POSITION*X SET 14.254 \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+containerForSponsor+"$Top_Line$Side" + WhichSide 
								 + "$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getStatsText()[0] + "\0", print_writers);
						if(lowerThird.getSubTitle().equalsIgnoreCase("NOT OUT")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+containerForSponsor+"$Top_Line$Side" + WhichSide 
									 + "$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText()+"*" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+containerForSponsor+"$Top_Line$Side" + WhichSide 
									 + "$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
						}
						
					}else {
						containerForSponsor = "$All_LowerThirds";
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+containerForSponsor+"$Top_Line$Side" + WhichSide 
								 + "$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+containerForSponsor+"$Top_Line$Side" + WhichSide 
								 + "$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+containerForSponsor+"$Top_Line$Side"+ WhichSide + 
							"$Select_Impact*FUNCTION*Omo*vis_con SET "+impactOmo+" \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+containerForSponsor+"$Team_BadgeAll$Side"+ WhichSide + 
							"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+containerForSponsor+"$Team_BadgeAll$Side"+ WhichSide + 
							"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+containerForSponsor+"$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+containerForSponsor+"$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+containerForSponsor+"$Top_Line$Side" + WhichSide 
							+ "$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+containerForSponsor+"$Top_Line$Side" + WhichSide 
							+ "$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+containerForSponsor+"$Top_Line$Side" + WhichSide 
							+ "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					
//					if(lowerThird.getWhichSponsor().toUpperCase().equalsIgnoreCase("SPONSOR")) {
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_BatsmanScore_LT$Top_Line" + 
//								"$Select_Impact*FUNCTION*Omo*vis_con SET "+impactOmo+" \0", print_writers);
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_BatsmanScore_LT" + 
//								"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_BatsmanScore_LT" + 
//								"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_BatsmanScore_LT" + 
//								"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_BatsmanScore_LT" + 
//								"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
//						
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_BatsmanScore_LT$Top_Line"
//								+ "$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_BatsmanScore_LT$Top_Line"
//								+ "$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_BatsmanScore_LT$Top_Line" 
//								+ "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_BatsmanScore_LT$Top_Line" 
//								 + "$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
//
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
//								 + "$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
//					}else {
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side"+ WhichSide + 
//								"$Select_Impact*FUNCTION*Omo*vis_con SET "+impactOmo+" \0", print_writers);
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
//								"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
//								"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
//								"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
//								"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
//						
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
//								+ "$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
//								+ "$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
//								+ "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
//								 + "$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
//
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
//								 + "$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
//					}
					break;
				case Constants.ICC_U19_2023:
					if(lowerThird.getWhichTeamFlag() != null) {
						CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						
						if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
								"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
					}
					
					if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
						CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 1 \0", print_writers);
					
					if(lowerThird.getSubTitle().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
								+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
								+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$TopLine$Side" + WhichSide +
							"$geom_ScorePositionX*TRANSFORMATION*POSITION*X SET 215.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + logo_name + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + logo_name + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 1 \0", print_writers);
					
					
					if(lowerThird.getSubTitle().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
								+ "$Change$geom_ScorePositionX$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
								+ "$Change$geom_ScorePositionX$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
					break;	
				}
				break;
				
			case "F9":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					String containerForSponsor = "";
					if(lowerThird.getWhichSponsor().toUpperCase().equalsIgnoreCase("SPONSOR")) {
						WhichSide = 1;
						containerForSponsor = "$All_LowerThirds$gfx_BowlerFigure_LT";
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+ containerForSponsor + "$Top_Line$Side" + WhichSide 
								 + "$txt_Score*GEOM*TEXT SET " + lowerThird.getStatsText()[3] +"-"+ lowerThird.getStatsText()[2] + "\0", print_writers);

						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+ containerForSponsor + "$Top_Line$Side" + WhichSide 
								 + "$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getStatsText()[0]+ " OVERS" + "\0", print_writers);
					}else {
						containerForSponsor = "$All_LowerThirds";
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+ containerForSponsor + "$Top_Line$Side" + WhichSide 
								 + "$txt_Score*GEOM*TEXT SET " + "" + "\0", print_writers);

						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+ containerForSponsor + "$Top_Line$Side" + WhichSide 
								 + "$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + containerForSponsor + "$Top_Line$Side"+ WhichSide + 
							"$Select_Impact*FUNCTION*Omo*vis_con SET "+impactOmo+" \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+ containerForSponsor + "$Team_BadgeAll$Side"+ WhichSide + 
							"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+ containerForSponsor + "$Team_BadgeAll$Side"+ WhichSide + 
							"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+ containerForSponsor + "$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+ containerForSponsor + "$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+ containerForSponsor + "$Top_Line$Side" + WhichSide 	
							+ "$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+ containerForSponsor + "$Top_Line$Side" + WhichSide 
							+ "$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*"+ containerForSponsor + "$Top_Line$Side" + WhichSide 
							+ "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					
					
					break;
				case Constants.ICC_U19_2023:
					if(lowerThird.getWhichTeamFlag() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						
						if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
								"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + logo_name + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + logo_name + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 0 \0", print_writers);
					break;	
				}
				
				break;
				
			case "Alt_a": case "Alt_s":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Overall_Position_Y$Sublines$Side_" + WhichSide + "$Select_Subline"
						+ "*FUNCTION*Omo*vis_con SET 1  \0", print_writers);
				if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToSelectedViz(1,"-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Side_"+ WhichSide + 
						"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				
				if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Side_" + WhichSide +
							"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Side_" + WhichSide +
							"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Side_" + WhichSide + "$Select_Flags$Flag" 
						+ containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
				
				break;
				
			case "F10":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Overall_Position_Y$Sublines$Side_" + WhichSide + "$Select_Subline"
							+ "*FUNCTION*Omo*vis_con SET 1  \0", print_writers);
					if(!lowerThird.getWhichSponsor().isEmpty()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
								"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichSponsor().toUpperCase() + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
								"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichSponsor().toUpperCase() + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
								"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichSponsor().toUpperCase() + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
								"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichSponsor().toUpperCase() + " \0", print_writers);
					}else if(lowerThird.getWhichSponsor().isEmpty()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
								"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + "EVENT" + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
								"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + "EVENT" + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
								"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + "EVENT" + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
								"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + "EVENT" + " \0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Score*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
					
					break;
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Overall_Position_Y$Sublines$Side_" + WhichSide + "$Select_Subline"
							+ "*FUNCTION*Omo*vis_con SET 1  \0", print_writers);
					
					if(!lowerThird.getWhichSponsor().isEmpty() && !lowerThird.getWhichTeamFlag().isEmpty()) {
						if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
							CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Data$Side_"+ WhichSide + 
									"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							
							if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
								CricketFunctions.DoadWriteCommandToSelectedViz(2,"-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
										"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
							}else {
								CricketFunctions.DoadWriteCommandToSelectedViz(2,"-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
										"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
							}
							
							CricketFunctions.DoadWriteCommandToSelectedViz(2,"-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
									"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToSelectedViz(1,"-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
						
						if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToSelectedViz(1,"-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag_Sponsor" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToSelectedViz(1,"-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag_Sponsor" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToSelectedViz(1,"-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
								"$Select_Flags$Flag_Sponsor" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
					
					}else if(!lowerThird.getWhichSponsor().isEmpty() && lowerThird.getWhichTeamFlag().isEmpty()) {
						if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
							CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Data$Side_"+ WhichSide + 
									"$Select_Flags*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToSelectedViz(1,"-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
						
					}else if(lowerThird.getWhichSponsor().isEmpty() && !lowerThird.getWhichTeamFlag().isEmpty()) {
						if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
							CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Data$Side_"+ WhichSide + 
									"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToSelectedViz(1,"-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						
						if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
								"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						
					}else {
						if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
							CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Data$Side_"+ WhichSide + 
									"$Select_Flags*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToSelectedViz(1,"-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Data$Side_"+ WhichSide + 
							"$Select_Flags$Sponsor*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
					
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side"+ WhichSide + 
							"$Select_Subline*FUNCTION*Omo*vis_con SET 1\0", print_writers);
					
					if(!lowerThird.getWhichSponsor().isEmpty() && !lowerThird.getWhichTeamFlag().isEmpty()) {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base1" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$TopLine$Side" + WhichSide + "$In$ScoreGrp" + 
//								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$1$img_Text2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$2$img_Text2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$3$img_Text2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$4$img_Text2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$SubLineBaseGrp$Side" + WhichSide + "$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide + "$1$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide + "$2$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide + "$3$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide + "$4$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						
					}else if(!lowerThird.getWhichSponsor().isEmpty() && lowerThird.getWhichTeamFlag().isEmpty()) {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + "ISPL" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + "ISPL" + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + "ISPL" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base1" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + "ISPL" + "\0", print_writers);
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$TopLine$Side" + WhichSide + "$In$ScoreGrp" + 
//								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + "ISPL" + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$1$img_Text2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + "ISPL" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$2$img_Text2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + "ISPL" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$3$img_Text2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + "ISPL" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$4$img_Text2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + "ISPL" + "\0", print_writers);
						
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$SubLineBaseGrp$Side" + WhichSide + "$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + "ISPL" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide + "$1$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + "ISPL" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide + "$2$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + "ISPL" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide + "$3$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + "ISPL" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide + "$4$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + "ISPL" + "\0", print_writers);
						
					}else if(lowerThird.getWhichSponsor().isEmpty() && !lowerThird.getWhichTeamFlag().isEmpty()) {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base1" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$TopLine$Side" + WhichSide + "$In$ScoreGrp" + 
//								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$1$img_Text2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$2$img_Text2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$3$img_Text2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$4$img_Text2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$SubLineBaseGrp$Side" + WhichSide + "$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide + "$1$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide + "$2$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide + "$3$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide + "$4$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + lowerThird.getWhichTeamFlag() + "\0", print_writers);
						
					}else {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + "ISPL" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + "ISPL" + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + "ISPL" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base1" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + "ISPL" + "\0", print_writers);
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$TopLine$Side" + WhichSide + "$In$ScoreGrp" + 
//								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + "ISPL" + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$1$img_Text2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + "ISPL" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$2$img_Text2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + "ISPL" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$3$img_Text2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + "ISPL" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$MoveForShrink$SubLines$Side" + WhichSide + "$4$img_Text2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + "ISPL" + "\0", print_writers);
						
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$SubLineBaseGrp$Side" + WhichSide + "$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + "ISPL" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide + "$1$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + "ISPL" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide + "$2$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + "ISPL" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide + "$3$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + "ISPL" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide + "$4$img_Base2" + 
								"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + "ISPL" + "\0", print_writers);
						
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 0 \0", print_writers);
					
					break;
				}
				break;
				
			case "Control_g":
				
				if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 5 \0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
						"$Select_Flags*FUNCTION*Omo*vis_con SET 5 \0", print_writers);

				if(lowerThird.getFirstName().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow2*ACTIVE SET 0 \0", print_writers);
				}else if(lowerThird.getSurName().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow2*ACTIVE SET 1 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Select_Flags$Flag_Flag" + containerName + "$img_Shadow2*ACTIVE SET 1 \0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
						"$Select_Flags$Flag_Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getFirstName() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
						"$Select_Flags$Flag_Flag" + containerName + "$img_Flag2*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getSurName() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
				
				break;	
			case "Alt_Shift_F3":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side"+ WhichSide + 
						"$Select_Impact*FUNCTION*Omo*vis_con SET "+impactOmo+" \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
						"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
						"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
						"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
						"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
						+ "$txt_FirstName*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
						+ "$txt_LastName*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
						+ "$txt_Designation*GEOM*TEXT SET " + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
						 + "$txt_Score*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
						 + "$txt_Not_Out*GEOM*TEXT SET " + "AFTER " +lowerThird.getBallsFacedText() +" OVERS" + "\0", print_writers);
				break;
			
			case "Shift_F5": case "Shift_F9":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side"+ WhichSide + 
							"$Select_Impact*FUNCTION*Omo*vis_con SET "+impactOmo+" \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					
					break;
				case Constants.ICC_U19_2023:
					if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
						CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					
					if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 1 \0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$TopLine$Side" + WhichSide +
							"$geom_ScorePositionX*TRANSFORMATION*POSITION*X SET 215.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + logo_name + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + logo_name + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 1 \0", print_writers);
					
					break;
				}
				
				switch(whatToProcess) {
				case "Alt_Shift_F3":
				case Constants.BENGAL_T20:
//					if(lowerThird.getSubTitle().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
//								 + "$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText()+"*" + "\0", print_writers);
//					}else {
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
//								 + "$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
//					}
//					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
//							 + "$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getBallsFacedText() + " BALL" + 
//							CricketFunctions.Plural(Integer.valueOf(lowerThird.getBallsFacedText())).toUpperCase() + "\0", print_writers);
					break;
				case "Shift_F5":
					switch (config.getBroadcaster().toUpperCase()) {
					case Constants.BENGAL_T20:
						if(lowerThird.getSubTitle().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
									 + "$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText()+"*" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
									 + "$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
								 + "$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getBallsFacedText() + " BALL" + 
								CricketFunctions.Plural(Integer.valueOf(lowerThird.getBallsFacedText())).toUpperCase() + "\0", print_writers);
						break;
					case Constants.ICC_U19_2023:
						if(lowerThird.getSubTitle().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
									+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "*" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
									+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
								+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getBallsFacedText() + " BALL" + 
								CricketFunctions.Plural(Integer.valueOf(lowerThird.getBallsFacedText())).toUpperCase() + "\0", print_writers);
						break;
					case Constants.ISPL:
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
								+ "$Change$geom_ScorePositionX$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
								+ "$Change$geom_ScorePositionX$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getBallsFacedText() + " BALL" + 
								CricketFunctions.Plural(Integer.valueOf(lowerThird.getBallsFacedText())).toUpperCase() + "\0", print_writers);
						break;
					}
					
					break;
				case "Shift_F9":
					switch (config.getBroadcaster().toUpperCase()) {
					case Constants.BENGAL_T20:
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
								 + "$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
								 + "$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getBallsFacedText() + " " + lowerThird.getSubTitle() + "\0", print_writers);
						break;
					case Constants.ICC_U19_2023:
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
								+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
								+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getBallsFacedText() + " " + lowerThird.getSubTitle() + "\0", print_writers);
						break;
					case Constants.ISPL:
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
								+ "$Change$geom_ScorePositionX$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
								+ "$Change$geom_ScorePositionX$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getBallsFacedText() + " " + lowerThird.getSubTitle() + "\0", print_writers);
						break;
					}
					break;	
				}
				break;
				
			case "Alt_F12":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_FirstName*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_LastName*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_Designation*GEOM*TEXT SET " + ""+ "\0", print_writers);
					if(Integer.valueOf(lowerThird.getScoreText().split("-")[1])>=10){
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
								 + "$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText().split("-")[0] + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
								 + "$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getBallsFacedText() + " OVERS" + "\0", print_writers);
					break;
				case Constants.ICC_U19_2023:
					if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
						CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					
					if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 1 \0", print_writers);
					
					if(Integer.valueOf(lowerThird.getScoreText().split("-")[1])>=10){
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
								+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText().split("-")[0] + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
								+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getBallsFacedText() + " OVERS" + "\0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$TopLine$Side" + WhichSide +
							"$geom_ScorePositionX*TRANSFORMATION*POSITION*X SET 215.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + logo_name + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + logo_name + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 1 \0", print_writers);
					
					if(Integer.valueOf(lowerThird.getScoreText().split("-")[1])>=10){
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
								+ "$Change$geom_ScorePositionX$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText().split("-")[0] + "\0", print_writers);
						
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
								+ "$Change$geom_ScorePositionX$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getBallsFacedText() + " OVERS" + "\0", print_writers);
					
					break;
				}
				
				break;	
			
			case "j":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Overall_Position_Y$Sublines$Side_" + WhichSide + "$Select_Subline"
						+ "*FUNCTION*Omo*vis_con SET 0  \0", print_writers);
				
				if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Data$Side_"+ WhichSide + 
						"$Select_Flags*FUNCTION*Omo*vis_con SET 0 \0", print_writers);

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
				
				break;
			
			case "Alt_Shift_C":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Captain_LT$Team_BadgeAll$Side1$TeamBadgeGrp1" + 
							"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Captain_LT$Team_BadgeAll$Side1$TeamBadgeGrp1" + 
							"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Captain_LT$Team_BadgeAll$Side1$TeamBadgeGrp2" + 
							"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Captain_LT$Team_BadgeAll$Side1$TeamBadgeGrp2" + 
							"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Captain_LT$Top_Line" 
							+ "$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Captain_LT$Top_Line" 
							+ "$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Captain_LT$Sublines" 
							+ "$txt_1*GEOM*TEXT SET " + "CAPTAIN" + "\0", print_writers);
					
					break;	
				}
				
				break;
				
			case "F6": case "Control_F6": case "Shift_F6":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side"+ WhichSide + 
							"$Select_Impact*FUNCTION*Omo*vis_con SET "+impactOmo+" \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getBallsFacedText() + " BALL" + 
							CricketFunctions.Plural(Integer.valueOf(lowerThird.getBallsFacedText())).toUpperCase() + "\0", print_writers);
					
					break;
				case Constants.ICC_U19_2023:
					if(lowerThird.getWhichTeamFlag() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						
						if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
								"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
					}

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getBallsFacedText() + " BALL" + 
							CricketFunctions.Plural(Integer.valueOf(lowerThird.getBallsFacedText())).toUpperCase() + "\0", print_writers);
					
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$TopLine$Side" + WhichSide +
							"$geom_ScorePositionX*TRANSFORMATION*POSITION*X SET 215.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + logo_name + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + logo_name + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getBallsFacedText() + " BALL" + 
							CricketFunctions.Plural(Integer.valueOf(lowerThird.getBallsFacedText())).toUpperCase() + "\0", print_writers);
					
					break;	
				}
				
				break;
				
			case "q": case "Control_q":
				
				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Top_Line$Data$Side_"+ WhichSide + 
						"$Select_Flags*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
				TimeUnit.MILLISECONDS.sleep(200);
				if(config.getSecondaryIpAddress()!= null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Top_Line$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					
					if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
								"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Top_Line$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
					TimeUnit.MILLISECONDS.sleep(200);
				}
				
				
				switch(whatToProcess) {
				case "q":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 1 \0", print_writers);
					
					if(lowerThird.getSubTitle().equalsIgnoreCase(CricketUtil.OUT)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
								+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					}else if(lowerThird.getSubTitle().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
								+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "*" + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getBallsFacedText() + " BALL" + 
							CricketFunctions.Plural(Integer.valueOf(lowerThird.getBallsFacedText())).toUpperCase() + "\0", print_writers);
					break;
				case "Control_q":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 1 \0", print_writers);
					
					if(Integer.valueOf(lowerThird.getBallsFacedText()) >= 10) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
								+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
								+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "-" + lowerThird.getBallsFacedText() + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getSubTitle() + " OVERS" + "\0", print_writers);
					break;	
				}
				break;	
				
			case "F7": case "F11": case "Control_s": case "Control_f":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_BadgeShadow*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$img_Badge*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1R*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Team_BadgeAll$Side"+ WhichSide + 
							"$TeamBadge1*TEXTURE*IMAGE SET " + Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							+ "$txt_Designation*GEOM*TEXT SET " + lowerThird.getSubTitle()+ "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Score*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side" + WhichSide 
							 + "$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
					break;
				case Constants.ICC_U19_2023:
					if(lowerThird.getWhichTeamFlag() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
								"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						
						if(lowerThird.getWhichTeamFlag().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
									"$Select_Flags$Flag" + containerName + "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
								"$Select_Flags$Flag" + containerName + "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
					}

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
							+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$BaseGrp$img_Base2$img_TeamLogoBW" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + logo_name + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side" + WhichSide + "$LogoScale$img_TeamLogo" + 
							"*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + logo_name + "\0", print_writers);
					
//					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$TopHeaderGrp$MaskForTopHeader" + 
//							"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + color_name + "\0", print_writers);
//					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$TopHeaderGrp$TopHeaderBaseBase" + 
//							"*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + color_name + "\0", print_writers);
					
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$TopTextGrp$txt_FirstName*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$BottomTextGrp$txt_LastName*GEOM*TEXT SET " + lowerThird.getSurName() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$TopHeaderGrp$Side1" 
							+ "$txt_TopHeader*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX$txt_Score*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y$Side" + WhichSide 
							+ "$Change$geom_ScorePositionX$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
					break;
				}
				break;
			}
			break;
		}
		return Constants.OK;
	}
	
	public String PopulateL3rdBody(int WhichSide, String whatToProcess) 
	{
		if(WhichSide == 1) {
			containerName = "$Change_Out";
		}else if(WhichSide == 2) {
			containerName = "$Change_In";
		}
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023: case Constants.ISPL: case Constants.BENGAL_T20:
			switch (whatToProcess) {
			case "Control_Shift_B":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Next_To_Bat_LT$TeamBadge$img_BadgeShadow*TEXTURE*IMAGE SET " 
							+ Constants.BENGAL_ICONS_PATH + inning.getBatting_team().getTeamName4().toUpperCase() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Next_To_Bat_LT$TeamBadge$img_Badge*TEXTURE*IMAGE SET " 
							+ Constants.BENGAL_ICONS_PATH + inning.getBatting_team().getTeamName4().toUpperCase() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Next_To_Bat_LT$TeamBadge$TeamBadge1R*TEXTURE*IMAGE SET " 
							+ Constants.BENGAL_ICONS_PATH + inning.getBatting_team().getTeamName4().toUpperCase() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Next_To_Bat_LT$TeamBadge$TeamBadge1*TEXTURE*IMAGE SET " 
							+ Constants.BENGAL_ICONS_PATH + inning.getBatting_team().getTeamName4().toUpperCase() + " \0", print_writers);
					
					int row = 1;
					boolean foundPlayerStats = false;
					for(BattingCard bc : battingCardList) {
						if(row>=4) {
							break;
						}
						if(bc.getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
							if(bc.getHowOut() != null && !bc.getHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
								continue;
							}
							if(config.getPrimaryIpAddress().equalsIgnoreCase(Constants.LOCALHOST)) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Next_To_Bat_LT$"+row+"$img_1*TEXTURE*IMAGE SET " 
										+ Constants.BENGAL_LOCAL_PHOTO_PATH + inning.getBatting_team().getTeamName4() + "\\\\" + bc.getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION + " \0", print_writers);
							}else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Next_To_Bat_LT$"+row+"$img_1*TEXTURE*IMAGE SET " 
										+ "\\\\" + config.getPrimaryIpAddress() + Constants.BENGAL_PHOTO_PATH  + inning.getBatting_team().getTeamName4() + "\\" + bc.getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION+ " \0", print_writers);
							}
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Next_To_Bat_LT$"+row+"$txt_3*ACTIVE SET " 
									+ "0" + " \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Next_To_Bat_LT$"+row+"$txt_4*ACTIVE SET " 
									+ "0" + " \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Next_To_Bat_LT$"+row+"$txt_1*GEOM*TEXT SET " 
									+ "IN AT "+ (inning.getTotalWickets() + 2 + row)+ " \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Next_To_Bat_LT$"+row+"$txt_2*GEOM*TEXT SET " 
									+bc.getPlayer().getFull_name() + " \0", print_writers);
							row++;
						}
					}
					break;
				}
				break;
			case "Shift_I":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Substitute$TeamBadge$img_BadgeShadow*TEXTURE*IMAGE SET " 
						+ Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Substitute$TeamBadge$img_Badge*TEXTURE*IMAGE SET " 
						+ Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Substitute$TeamBadge$TeamBadge1R*TEXTURE*IMAGE SET " 
						+ Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Substitute$TeamBadge$TeamBadge1*TEXTURE*IMAGE SET " 
						+ Constants.BENGAL_ICONS_PATH + lowerThird.getWhichTeamFlag() + " \0", print_writers);
				
				//OUT player
				if(config.getPrimaryIpAddress().equalsIgnoreCase(Constants.LOCALHOST)) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Substitute$SubGrp$img_Image*TEXTURE*IMAGE SET " 
							+ Constants.BENGAL_LOCAL_PHOTO_PATH + lowerThird.getWhichTeamFlag() + "\\\\" + lowerThird.getSurName() + CricketUtil.PNG_EXTENSION + " \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Substitute$SubGrp$img_Image*TEXTURE*IMAGE SET " 
							+ "\\\\" + config.getPrimaryIpAddress() + Constants.BENGAL_PHOTO_PATH  + lowerThird.getWhichTeamFlag() + "\\" + lowerThird.getSurName() + CricketUtil.PNG_EXTENSION+ " \0", print_writers);
				}
				
				if(lowerThird.getFirstName() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Substitute$SubGrp$txt_FirstName*GEOM*TEXT SET " 
							+ lowerThird.getHeaderText() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Substitute$SubGrp$txt_LastName*GEOM*TEXT SET " 
							+ lowerThird.getFirstName() + " \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Substitute$SubGrp$txt_FirstName*GEOM*TEXT SET " 
							+ "" + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Substitute$SubGrp$txt_LastName*GEOM*TEXT SET " 
							+ lowerThird.getHeaderText() + " \0", print_writers);
				}
				
				
				//IN player
				if(config.getPrimaryIpAddress().equalsIgnoreCase(Constants.LOCALHOST)) {
					//photoname
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Substitute$ImpactGrp$img_Image*TEXTURE*IMAGE SET " 
							+ Constants.BENGAL_LOCAL_PHOTO_PATH + lowerThird.getWhichTeamFlag() + "\\\\" + lowerThird.getBallsFacedText() + CricketUtil.PNG_EXTENSION + " \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Substitute$ImpactGrp$img_Image*TEXTURE*IMAGE SET " 
							+ "\\\\" + config.getPrimaryIpAddress() + Constants.BENGAL_PHOTO_PATH  + lowerThird.getWhichTeamFlag() + "\\" + lowerThird.getBallsFacedText() + CricketUtil.PNG_EXTENSION+ " \0", print_writers);
				}
				if(lowerThird.getScoreText() != null) {
					//firstname
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Substitute$ImpactGrp$txt_FirstName*GEOM*TEXT SET " 
							+ lowerThird.getSubTitle() + " \0", print_writers);
					
					//surname
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Substitute$ImpactGrp$txt_LastName*GEOM*TEXT SET " 
							+ lowerThird.getScoreText() + " \0", print_writers);
				}else {
					//firstname
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Substitute$ImpactGrp$txt_FirstName*GEOM*TEXT SET " 
							+ "" + " \0", print_writers);
					
					//surname
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Substitute$ImpactGrp$txt_LastName*GEOM*TEXT SET " 
							+ lowerThird.getSubTitle() + " \0", print_writers);
				}
				
				
				break;
			case "Control_i":
				int runs = Integer.valueOf(lowerThird.getSubTitle());
				int pRuns = 0, fRuns = 20;
				
				for(int i=1; i<=10; i++) {
					if(runs>=20) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$1" + containerName + "$Data$Title$txt_"+i+"*GEOM*TEXT SET " + pRuns +"-"+ fRuns + "\0", print_writers);
						pRuns = fRuns + 1;
						fRuns = fRuns+20;
						runs = runs-20;
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$1" + containerName + "$Data$Title$txt_"+i+"*GEOM*TEXT SET " + pRuns +"-"+ lowerThird.getSubTitle() + "\0", print_writers);
					}
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
				
				for(int i=0; i<lowerThird.getTitlesText().length; i++) {
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
				}
				System.out.println(lowerThird.getBallsFacedText().length());
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
				
				break;
			case "Alt_F6": 
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1$Data$Stat*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
				
				for(int i=0; i<lowerThird.getStatsText().length; i++) {
					if(i==2) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$1$Data$Stat$fig_2*ACTIVE SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2$Data$Stat$fig_2*ACTIVE SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$1" + containerName + "$Data$Stat$fig_2*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split(",")[0] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2" + containerName + "$Data$Stat$fig_2*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split(",")[1] + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$1$Data$Stat$txt_3*ACTIVE SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2$Data$Stat$txt_3*ACTIVE SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$1" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split(",")[0] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split(",")[1] + "\0", print_writers);
					}
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
				
				
				break;
			case "Shift_A":  case "Shift_R":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
				
				for(int i=0; i<lowerThird.getTitlesText().length; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split(",")[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split(",")[1] + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$3$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
				
				
				break;
			case "Shift_U":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
				
				for(int i=0; i<lowerThird.getTitlesText().length; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
				}
				break;
			case "Alt_j":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
				
				for(int i=0; i<lowerThird.getTitlesText().length; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
				}
				break;	
			case "b":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
				
				for(int i=0; i<lowerThird.getTitlesText().length; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
				}
				break;	
			case "Alt_w":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
				
				for(int i=0; i<lowerThird.getTitlesText().length; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
				}
				break;
			case "Control_j":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1" + containerName + "$Data$Left*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
				
				for(int i=0; i<lowerThird.getTitlesText().length; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1" + containerName + "$Data$Title$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
				}
				
				for(int i=0; i<lowerThird.getStatsText().length; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2" + containerName + "$Data$Stat$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split("/")[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3" + containerName + "$Data$Stat$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split("/")[1] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4" + containerName + "$Data$Stat$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split("/")[2] + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$3$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$4$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[2] + "\0", print_writers);
				break;	
				
			case "Alt_i":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1" + containerName + "$Data$Left*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
				
				for(int i=0; i<lowerThird.getTitlesText().length; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1" + containerName + "$Data$Title$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
				}
				
				for(int i=0; i<lowerThird.getStatsText().length; i++) {
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2" + containerName + "$Data$Stat$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split("/")[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3" + containerName + "$Data$Stat$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split("/")[1] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4" + containerName + "$Data$Stat$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split("/")[2] + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$3$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$4$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[2] + "\0", print_writers);
				break;		
			
			case "Alt_q":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_POTT_Aramco$Sublines$Side_1"
						+ "$1$Main$Data$Set_Material$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_POTT_Aramco$Sublines$Side_1"
						+ "$2$Main$Data$txt_Info*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
				break;
				
			case "Shift_E":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$2$Data$Right$txt_1*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$3$Data$Right$txt_1*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$4$Data$Right$txt_1*GEOM*TEXT SET " + "" + "\0", print_writers);
					
					if(lowerThird.getSubTitle().equalsIgnoreCase("1") || lowerThird.getSubTitle().equalsIgnoreCase("2")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
						
						for(int i=0; i<lowerThird.getTitlesText().length; i++) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$1" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
						}
						
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
								+ "$Select_Subline$4$Data$Left$txt_1*GEOM*TEXT SET " + "TOTAL" + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
						
						for(int i=0; i<lowerThird.getTitlesText().length; i++) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$1" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split(",")[0] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$3" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split(",")[1] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$4" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split(",")[2] + "\0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$3$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
						
					}
					
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET  \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET  \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Right$txt_1*GEOM*TEXT SET  \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Right$txt_1*GEOM*TEXT SET  \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Right$txt_1*GEOM*TEXT SET  \0", print_writers);
						
					if(lowerThird.getSubTitle().equalsIgnoreCase("1") || lowerThird.getSubTitle().equalsIgnoreCase("2")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$2$Data$Title*ACTIVE SET 0\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$3$Data$Title*ACTIVE SET 0\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$4$Data$Title*ACTIVE SET 0\0", print_writers);
						
						for(int i=0; i<lowerThird.getTitlesText().length; i++) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$1$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$2$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
						}
						
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Left$txt_1*GEOM*TEXT SET " + "TOTAL" + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
						
						for(int i=0; i<lowerThird.getTitlesText().length; i++) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$1$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$2$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split(",")[0] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$3$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split(",")[1] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$4$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split(",")[2] + "\0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
						
					}
					
					break;
				}
				break;
			case "Alt_F1": case "Alt_F2":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Title*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Stat*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1$Data$Left*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Left*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Left*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Left*ACTIVE SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1$Data$Right*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Right*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Right*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Right*ACTIVE SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1" + containerName + "$Data$Set_Material*MATERIAL SET /Default/Essentials/TextColor/LightBlue \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3" + containerName + "$Data$Set_Material*MATERIAL SET /Default/Essentials/TextColor/LightBlue \0", print_writers);
					
				if(griff.size() <= 3) {
					for(int i=0; i<l3griff.getTopTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + "v " + l3griff.getTopTitlesText()[i] + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2" + containerName + "$Data$Stat$fig_" + (i+1) + "*ACTIVE SET 1 \0", print_writers);
						
						if(l3griff.getTopStatsText()[i].contains(",")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + l3griff.getTopStatsText()[i].split(",")[0] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2" + containerName + "$Data$Stat$fig_" + (i+1) + "*GEOM*TEXT SET " + l3griff.getTopStatsText()[i].split(",")[1] + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + l3griff.getTopStatsText()[i] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2" + containerName + "$Data$Stat$fig_" + (i+1) + "*GEOM*TEXT SET " + "" + "\0", print_writers);
						}
					}
				}else {
					for(int i=0; i<l3griff.getTopTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + "v " + l3griff.getTopTitlesText()[i] + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2" + containerName + "$Data$Stat$fig_" + (i+1) + "*ACTIVE SET 1 \0", print_writers);
						if(l3griff.getTopStatsText()[i].contains(",")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + l3griff.getTopStatsText()[i].split(",")[0] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2" + containerName + "$Data$Stat$fig_" + (i+1) + "*GEOM*TEXT SET " + l3griff.getTopStatsText()[i].split(",")[1] + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + l3griff.getTopStatsText()[i] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2" + containerName + "$Data$Stat$fig_" + (i+1) + "*GEOM*TEXT SET " + "" + "\0", print_writers);
						}
					}
					
					for(int i=0; i<l3griff.getBottomTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + "v " + l3griff.getBottomTitlesText()[i] + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$4" + containerName + "$Data$Stat$fig_" + (i+1) + "*ACTIVE SET 1 \0", print_writers);
						if(l3griff.getBottomStatsText()[i].contains(",")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$4" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + l3griff.getBottomStatsText()[i].split(",")[0] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$4" + containerName + "$Data$Stat$fig_" + (i+1) + "*GEOM*TEXT SET " + l3griff.getBottomStatsText()[i].split(",")[1] + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$4" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + l3griff.getBottomStatsText()[i] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$4" + containerName + "$Data$Stat$fig_" + (i+1) + "*GEOM*TEXT SET " + "" + "\0", print_writers);
						}
					}
				}
				break;
			case "Control_F2":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
				
				for(int i=0; i<lowerThird.getTitlesText().length; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				for(int j =0; j <= subline - 2; j++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$" + (j+2) + "$Data$Left$txt_1*GEOM*TEXT SET " + bowlingCardList.get(j).getPlayer().getTicker_name() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$" + (j+2) + "" + containerName + "$Data$Stat$txt_1*GEOM*TEXT SET " + 
						CricketFunctions.OverBalls(bowlingCardList.get(j).getOvers(), bowlingCardList.get(j).getBalls()) + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$" + (j+2) + "" + containerName + "$Data$Stat$txt_2*GEOM*TEXT SET " + bowlingCardList.get(j).getMaidens() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$" + (j+2) + "" + containerName + "$Data$Stat$txt_3*GEOM*TEXT SET " + bowlingCardList.get(j).getRuns() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$" + (j+2) + "" + containerName + "$Data$Stat$txt_4*GEOM*TEXT SET " + bowlingCardList.get(j).getWickets() + "\0", print_writers);
					
					if(bowlingCardList.get(j).getEconomyRate() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$" + (j+2) + "" + containerName + "$Data$Stat$txt_5*GEOM*TEXT SET " + bowlingCardList.get(j).getEconomyRate() + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$" + (j+2) + "" + containerName + "$Data$Stat$txt_5*GEOM*TEXT SET " + "-" + "\0", print_writers);
					}
				}
				break;
				
			case "a":
				
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_"+ WhichSide + 
							"$Select_Subline$1$Select_Base*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_" + WhichSide +
							"$Select_Subline$1$Select_Base$Blue*ALPHA*ALPHA SET 30.0 \0",print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
					
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$1" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
							
						if(i==0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
									"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[1].split(",")[0] + "\0", print_writers);
								
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$3" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[1].split(",")[1] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$4" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[1].split(",")[2] + "\0", print_writers);	
						}if(i==3) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
									"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[5].split(",")[0] + "\0", print_writers);
								
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$3" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[5].split(",")[1] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$4" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[5].split(",")[2] + "\0", print_writers);
						}if(i==1||i==2) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
									"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[2].split(",")[i-1] + "\0", print_writers);
								
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$3" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[3].split(",")[i-1] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$4" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[4].split(",")[i-1] + "\0", print_writers);
						}if(i==4||i==5) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
									"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[2].split(",")[i-2] + "\0", print_writers);
								
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$3" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[3].split(",")[i-2] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$4" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[4].split(",")[i-2] + "\0", print_writers);
						}
					}
					break;
				case Constants.ISPL:
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$TopLine$TopData_Position_Y$Side" + WhichSide +
							"$BottomTextGrp$geom_MaxSize*FUNCTION*Maxsize*WIDTH_X SET 900.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					if(lowerThird.getNumberOfSubLines() == 2) {
						for(int i=0; i<lowerThird.getTitlesText().length; i++) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$1$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$2$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
						}
					}else {
						for(int i=0; i<lowerThird.getTitlesText().length; i++) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$1$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$2$Data$Stat$txt_1*GEOM*TEXT SET \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$2$Data$Stat$txt_2*GEOM*TEXT SET \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$2$Data$Stat$txt_3*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$2$Data$Stat$txt_4*GEOM*TEXT SET \0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$3$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
						}
					}
					
					break;
				}
				break;
			case "Shift_F3":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					for(int i=1; i<=10; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
								"$Select_Subline$1$Data$Stat$txt_" + i + "*ACTIVE SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
								"$Select_Subline$2$Data$Stat$txt_" + i + "*ACTIVE SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
								"$Select_Subline$3$Data$Stat$txt_" + i + "*ACTIVE SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
								"$Select_Subline$4$Data$Stat$txt_" + i + "*ACTIVE SET 0 \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
							"$Select_Subline$2$Data$Stat$txt_" + i + "*FUNCTION*Maxsize*DEFAULT_SCALE_X SET 0.28\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
							"$Select_Subline$2$Data$Stat*ACTIVE SET 1 \0", print_writers);
					
					
					for(int iSubLine = 1; iSubLine <= lowerThird.getNumberOfSubLines(); iSubLine++) {
						
						//HIDE AND SHOW TITLE
						if(lowerThird.getTitlesText() != null) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
								"$Select_Subline$"+ iSubLine + "$Data$Title*ACTIVE SET 1 \0", print_writers);
							
							for(int iTitle = 1; iTitle <= lowerThird.getTitlesText().length; iTitle++) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
									"$Select_Subline$"+ iSubLine + "$Data$Title$txt_" + iTitle + "*ACTIVE SET 1 \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
									"$Select_Subline$"+ iSubLine + "$Data$Title$txt_" + iTitle + "*TRANSFORMATION*POSITION*X SET " 
									+ lowerThird.getPosition()[iTitle-1] + "\0",print_writers);
							}
							//Hide number of Titles on each strap
							for(int iTitle = lowerThird.getTitlesText().length + 1; iTitle <= 10; iTitle++) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
										"$Select_Subline$"+ iSubLine + "$Data$Title$txt_" + iTitle + "*ACTIVE SET 0 \0", print_writers);
							}
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
									"$Select_Subline$"+ iSubLine + "$Data$Title*ACTIVE SET 0 \0", print_writers);
						}
						
						// HIDE AND SHOW STATS
						if(lowerThird.getStatsText() != null) {
							
							for(int iStats = 1; iStats <= lowerThird.getStatsText().length; iStats++) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
									"$Select_Subline$"+ iSubLine + "$Data$Stat$txt_" + iStats + "*ACTIVE SET 1 \0", print_writers);
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
									"$Select_Subline$" + iSubLine + "$Data$Stat$txt_" + iStats + "*TRANSFORMATION*POSITION*X SET " + lowerThird.getPosition()[iStats-1] + "\0",print_writers);
							}
							//Hide number of Titles on each strap
							for(int iStats = lowerThird.getStatsText().length + 1; iStats <= 10; iStats++) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Position_With_Graphics$Sublines" +
										"$Select_Subline$"+ iSubLine + "$Data$Stat$txt_" + iStats + "*ACTIVE SET 0 \0", print_writers);
							}
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
									"$Select_Subline$"+ iSubLine + "$Data$Stat*ACTIVE SET 0 \0", print_writers);
						}
						
						//Show Left on each strap
						if(lowerThird.getLeftText() != null &&  lowerThird.getLeftText().length > 0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
									"$Select_Subline$"+ iSubLine + "$Data$Left*ACTIVE SET 1 \0", print_writers);
						} else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
									"$Select_Subline$"+ iSubLine + "$Data$Left*ACTIVE SET 0 \0", print_writers);
						}
						//Show Right on each strap
						if(lowerThird.getRightText() != null && lowerThird.getRightText().length > 0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
									"$Select_Subline$"+ iSubLine + "$Data$Right*ACTIVE SET 1 \0", print_writers);
							if(lowerThird.getPosition() != null && lowerThird.getPosition().length > 0) {
								for(int iTitle = 1; iTitle <= lowerThird.getRightText().length; iTitle++) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
										"$Select_Subline$" + iTitle + "$Data$Right*ACTIVE SET 1 \0", print_writers);
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
										"$Select_Subline$" + iTitle + "$Data$Right*TRANSFORMATION*POSITION*X SET " 
										+ lowerThird.getPosition()[0] + "\0",print_writers);
								}
							}
							
							
						} else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
									"$Select_Subline$"+ iSubLine + "$Data$Right*ACTIVE SET 0 \0", print_writers);
						}
					}
					
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
								"$Select_Subline$1" + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
								"$Select_Subline$2" + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" +
								"$Select_Subline$2$Data$Stat$txt_" + (i+1) + "*FUNCTION*Maxsize*DEFAULT_SCALE_X SET 0.20\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" 
							+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_Fall_Of_Wickets$Sublines" 
						+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
					break;
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
					
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Stat$txt_" + (i+1) + "*FUNCTION*Maxsize*DEFAULT_SCALE_X SET 0.75\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
					
					break;
				}
				
				break;
			case "Control_Shift_P":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					for(int j=0;j<=this_data_str.size()-1;j++) {
						for(int i=0; i<lowerThird.getTitlesText().length; i++) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$1" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
									"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + this_data_str.get(j).split(",")[i] + "\0", print_writers);
						}
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$3$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[2] + "\0", print_writers);
					
					break;
				}
				break;
			case "Alt_Shift_L":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
						
						if(i == 0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
									"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + CricketFunctions.OverBalls(0, Integer.valueOf(lowerThird.getStatsText()[i])) + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
									"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + CricketFunctions.OverBalls(0, Integer.valueOf((Integer.valueOf(lowerThird.getStatsText()[i]) - Integer.valueOf(lowerThird.getStatsText()[i-1])))) + "\0", print_writers);
						}
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
					
					break;
				}
				break;	
				
			case "u":
				
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							containerName+"$Select_Subline$1" + "$Data$Title$txt_1*GEOM*TEXT SET " + "1" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							containerName+"$Select_Subline$1" + "$Data$Title$txt_2*GEOM*TEXT SET " + "2" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							containerName+"$Select_Subline$1" + "$Data$Title$txt_3*GEOM*TEXT SET " + "3" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							containerName+"$Select_Subline$1" + "$Data$Title$txt_4*GEOM*TEXT SET " + "4" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							containerName+"$Select_Subline$1" + "$Data$Title$txt_5*GEOM*TEXT SET " + "5" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							containerName+"$Select_Subline$1"  + "$Data$Title$txt_6*GEOM*TEXT SET " + "6" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							containerName+"$Select_Subline$1"  + "$Data$Title$txt_7*GEOM*TEXT SET " + "7" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							containerName+"$Select_Subline$1"  + "$Data$Title$txt_8*GEOM*TEXT SET " + "8" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							containerName+"$Select_Subline$1"  + "$Data$Title$txt_9*GEOM*TEXT SET " + "9" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							containerName+"$Select_Subline$1"  + "$Data$Title$txt_10*GEOM*TEXT SET " + "10" + "\0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
								containerName+"$Select_Subline$2" + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
						+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
						+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
					break;
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_1*GEOM*TEXT SET " + "FIRST" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_2*GEOM*TEXT SET " + "SECOND" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_3*GEOM*TEXT SET " + "THIRD" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_4*GEOM*TEXT SET " + "FOURTH" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_5*GEOM*TEXT SET " + "FIFTH" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_6*GEOM*TEXT SET " + "SIXTH" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_7*GEOM*TEXT SET " + "SEVENTH" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_8*GEOM*TEXT SET " + "EIGHTH" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_9*GEOM*TEXT SET " + "NINTH" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_10*GEOM*TEXT SET " + "TENTH" + "\0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Title$txt_1*GEOM*TEXT SET 1\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Title$txt_2*GEOM*TEXT SET 2\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Title$txt_3*GEOM*TEXT SET 3\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Title$txt_4*GEOM*TEXT SET 4\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Title$txt_5*GEOM*TEXT SET 5\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Title$txt_6*GEOM*TEXT SET 6\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Title$txt_7*GEOM*TEXT SET 7\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Title$txt_8*GEOM*TEXT SET 8\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Title$txt_9*GEOM*TEXT SET 9\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Title$txt_10*GEOM*TEXT SET 10\0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$2$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
					break;
				}
				
				break;
				
			case "Control_a":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Projected_LT$Sublines" +
								"$1$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Projected_LT$Sublines" +
								"$2$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
					}
					
					break;
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
					
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
					
					break;	
				}
				
				break;
			
			case "l":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
				
				for(int i=0; i<lowerThird.getTitlesText().length; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1" + containerName + "$Data$Title$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2" + containerName + "$Data$Stat$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
				}
				break;
			
			case "n":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_"+ WhichSide + 
						"$Select_Subline$1$Select_Base*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_" + WhichSide +
						"$Select_Subline$1$Select_Base$Blue*ALPHA*ALPHA SET 30.0 \0",print_writers);
				
				for(int i=0; i<lowerThird.getTitlesText().length; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[1].split(",")[i] + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[2].split(",")[i] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[3].split(",")[i] + "\0", print_writers);
				}
				
				for(int i=1; i<=4; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$" + i + "" + containerName + "$Divider*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_" + WhichSide +
							"$Select_Subline$" + i + "$Divider$obj_Divider1*TRANSFORMATION*POSITION*X SET -482.0 \0",print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_" + WhichSide +
							"$Select_Subline$" + i + "$Divider$obj_Divider2*TRANSFORMATION*POSITION*X SET -202.0 \0",print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_" + WhichSide +
							"$Select_Subline$" + i + "$Divider$obj_Divider3*TRANSFORMATION*POSITION*X SET 200.0 \0",print_writers);
				}
				break;
				
			case "Control_F3":
				
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET 0.0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$2$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET 0.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$1$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					if(Integer.valueOf(lowerThird.getLeftText()[2].split("-")[1]) >= 10) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
								+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + lowerThird.getLeftText()[1] 
								+ lowerThird.getLeftText()[2].split("-")[0] + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
								+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + lowerThird.getLeftText()[1] 
								+ lowerThird.getLeftText()[2] + "\0", print_writers);
					}
					
					if(Integer.valueOf(lowerThird.getLeftText()[5].split("-")[1]) >= 10) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
								+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " +lowerThird.getLeftText()[3] + " " + lowerThird.getLeftText()[4]
								+ " " + lowerThird.getLeftText()[5].split("-")[0] + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
								+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " +lowerThird.getLeftText()[3] + " " + lowerThird.getLeftText()[4]
								+ " " + lowerThird.getLeftText()[5] + "\0", print_writers);
					}
					
					
					break;
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
//					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
//							"$Select_Subline$2$Data$Title*ACTIVE SET 1 \0", print_writers);
					
					if(Integer.valueOf(lowerThird.getLeftText()[2].split("-")[1]) >= 10) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
								+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + lowerThird.getLeftText()[1] 
								+ lowerThird.getLeftText()[2].split("-")[0] + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
								+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + lowerThird.getLeftText()[1] 
								+ lowerThird.getLeftText()[2] + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_1*GEOM*TEXT SET " + lowerThird.getTitlesText()[0].split(",")[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_2*GEOM*TEXT SET " + lowerThird.getTitlesText()[0].split(",")[1] + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_3*GEOM*TEXT SET " + lowerThird.getTitlesText()[2].split(",")[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_4*GEOM*TEXT SET " + lowerThird.getTitlesText()[2].split(",")[1] + "\0", print_writers);
					
					
					if(Integer.valueOf(lowerThird.getLeftText()[5].split("-")[1]) >= 10) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
								+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " +lowerThird.getLeftText()[3] + " " + lowerThird.getLeftText()[4]
								+ " " + lowerThird.getLeftText()[5].split("-")[0] + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
								+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " +lowerThird.getLeftText()[3] + " " + lowerThird.getLeftText()[4]
								+ " " + lowerThird.getLeftText()[5] + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2" + containerName + "$Data$Title$txt_1*GEOM*TEXT SET " + lowerThird.getTitlesText()[0].split(",")[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2" + containerName + "$Data$Title$txt_2*GEOM*TEXT SET " + lowerThird.getTitlesText()[1] + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2" + containerName + "$Data$Title$txt_3*GEOM*TEXT SET " + lowerThird.getTitlesText()[2].split(",")[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2" + containerName + "$Data$Title$txt_4*GEOM*TEXT SET " + lowerThird.getTitlesText()[3] + "\0", print_writers);
					break;
				case Constants.ISPL:
//					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
//							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					if(Integer.valueOf(lowerThird.getLeftText()[2].split("-")[1]) >= 10) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + lowerThird.getLeftText()[1] 
										+ lowerThird.getLeftText()[2].split("-")[0] + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + lowerThird.getLeftText()[1] 
										+ lowerThird.getLeftText()[2] + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Title$txt_1*GEOM*TEXT SET " + lowerThird.getTitlesText()[0].split(",")[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Title$txt_2*GEOM*TEXT SET " + lowerThird.getTitlesText()[0].split(",")[1] + "\0", print_writers);
						
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Title$txt_3*GEOM*TEXT SET " + lowerThird.getTitlesText()[2].split(",")[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Title$txt_4*GEOM*TEXT SET " + lowerThird.getTitlesText()[2].split(",")[1] + "\0", print_writers);
					
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Title$txt_5*GEOM*TEXT SET " + lowerThird.getTitlesText()[4].split(",")[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Title$txt_6*GEOM*TEXT SET " + lowerThird.getTitlesText()[4].split(",")[1] + "\0", print_writers);
					
					if(Integer.valueOf(lowerThird.getLeftText()[5].split("-")[1]) >= 10) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[3] + " " + lowerThird.getLeftText()[4]
									+ " " + lowerThird.getLeftText()[5].split("-")[0] + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[3] + " " + lowerThird.getLeftText()[4]
									+ " " + lowerThird.getLeftText()[5] + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$2$Data$Title$txt_1*GEOM*TEXT SET " + lowerThird.getTitlesText()[0].split(",")[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$2$Data$Title$txt_2*GEOM*TEXT SET " + lowerThird.getTitlesText()[1] + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$2$Data$Title$txt_3*GEOM*TEXT SET " + lowerThird.getTitlesText()[2].split(",")[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$2$Data$Title$txt_4*GEOM*TEXT SET " + lowerThird.getTitlesText()[3] + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title$txt_5*GEOM*TEXT SET " + lowerThird.getTitlesText()[4].split(",")[0] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title$txt_6*GEOM*TEXT SET " + lowerThird.getTitlesText()[5] + "\0", print_writers);
					
					break;
				}
				break;
				
			case "Shift_B":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + 
						lowerThird.getLeftText()[0]+"    "+lowerThird.getLeftText()[1] + "     " + lowerThird.getLeftText()[2] + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getRightText()[0] + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$2$Data$Right$txt_1*GEOM*TEXT SET " + "\0", print_writers);
					break;
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + 
							lowerThird.getLeftText()[0]+"    "+lowerThird.getLeftText()[1] + "     " + lowerThird.getLeftText()[2] + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getRightText()[0]  + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getRightText()[0]  + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$2$Data$Right$txt_1*GEOM*TEXT SET " + "\0", print_writers);
					break;
				}
				break;
				
			case "Alt_o":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + " " + lowerThird.getLeftText()[1] + " OFF " + 
							lowerThird.getLeftText()[2] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " +lowerThird.getRightText()[0] + " " + lowerThird.getRightText()[1] + " OFF " + 
							lowerThird.getRightText()[2] +"\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
						+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + "PARTNERSHIP - " + lowerThird.getScoreText() + " RUNS FROM " + 
						lowerThird.getBallsFacedText() + " BALL" + CricketFunctions.Plural(Integer.valueOf(lowerThird.getBallsFacedText())).toUpperCase() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$2$Data$Right$txt_1*GEOM*TEXT SET " +"" +"\0", print_writers);
					break;
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + " " + lowerThird.getLeftText()[1] + " OFF " + 
						lowerThird.getLeftText()[2]+ "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + lowerThird.getRightText()[0] + " " + lowerThird.getRightText()[1] + " OFF " + 
						lowerThird.getRightText()[2] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + "PARTNERSHIP - " + lowerThird.getScoreText() + " RUNS FROM " + 
						lowerThird.getBallsFacedText() + " BALL" + CricketFunctions.Plural(Integer.valueOf(lowerThird.getBallsFacedText())).toUpperCase()  + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$2$Data$Right$txt_1*GEOM*TEXT SET " + "" + "\0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET 0.0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Right$txt_1*TRANSFORMATION*POSITION*X SET 275.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + " " + lowerThird.getLeftText()[1] + " OFF " + 
						lowerThird.getLeftText()[2] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + lowerThird.getRightText()[0] + " " + lowerThird.getRightText()[1] + " OFF " + 
						lowerThird.getRightText()[2] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + "PARTNERSHIP - " + lowerThird.getScoreText() + " RUNS FROM " + 
						lowerThird.getBallsFacedText() + " BALL" + CricketFunctions.Plural(Integer.valueOf(lowerThird.getBallsFacedText())).toUpperCase() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$2$Data$Right$txt_1*GEOM*TEXT SET " + "" + "\0", print_writers);
					break;
				}
				
				break;
			
			case "Alt_d":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + "DLS PAR SCORE AFTER " + lowerThird.getScoreText() + " OVERS - "
					+ lowerThird.getLeftText()[0] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1].toUpperCase() + " PAR SCORE"  + "\0", print_writers);
				break;
				
			case "Control_F5": case "Control_F9":
				
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET -15.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
						+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
					break;
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
					
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
					
					break;
				}
				
				switch (whatToProcess) {
				case "Control_F9":
					switch (config.getBroadcaster().toUpperCase()) {
					case Constants.BENGAL_T20:
						if(!lowerThird.getRightText()[0].equalsIgnoreCase("WITHOUTEND")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
									+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + lowerThird.getRightText()[0] + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
									+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + "" + "\0", print_writers);
						}
						break;
						case Constants.ICC_U19_2023:
							if(!lowerThird.getRightText()[0].equalsIgnoreCase("WITHOUTEND")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
										+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + lowerThird.getRightText()[0] + "\0", print_writers);
							}else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
										+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + "" + "\0", print_writers);
							}
							break;
						case Constants.ISPL:
							if(!lowerThird.getRightText()[0].equalsIgnoreCase("WITHOUTEND")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
										"$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + lowerThird.getRightText()[0] + "\0", print_writers);
							}else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
										"$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + "" + "\0", print_writers);
							}
							break;
					}
					break;
				}
				
				break;
			 
			case "Shift_F7": case "Control_F4":
				
				switch (config.getBroadcaster().toUpperCase()) {
				
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Image_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Image_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Image_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Image_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Left$txt_1*FUNCTION*Maxsize*WIDTH_X SET 500.0 \0", print_writers);
					
					if(lowerThird.getLeftText()[1].equalsIgnoreCase("-")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Image_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
							
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Image_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "            AGE " + lowerThird.getLeftText()[1] + "\0", print_writers);
							
					}
					
					break;
				}
				
//				switch (whatToProcess) {
//				case "Shift_F7":
//					switch (config.getBroadcaster().toUpperCase()) {
//						case Constants.ISPL:
//							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
//									"$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + "AGE " + lowerThird.getRightText()[0] + "\0", print_writers);
//							break;
//					}
//					break;
//				case "n":
//					switch (config.getBroadcaster().toUpperCase()) {
//						case Constants.ISPL:
//							if(!lowerThird.getRightText()[0].equalsIgnoreCase("WITHOUTEND")) {
//								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
//										"$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + lowerThird.getRightText()[0] + "       AGE " + lowerThird.getRightText()[1] + "\0", print_writers);
//							}else {
//								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
//										"$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + " AGE " + lowerThird.getRightText()[1] + "\0", print_writers);
//							}
//							break;
//					}
//					break;
//				}
				break;	
				
			case "q": case "Control_q":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$StatGrp1$txt_1*GEOM*TEXT SET " + "FOURS" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$StatGrp1$fig_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$StatGrp2$txt_1*GEOM*TEXT SET " + "SIXES" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$StatGrp2$fig_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
				break;
				
			case "F6": case "Control_F6":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET -3.0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$2$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET -3.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
						+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + "FOURS  " + lowerThird.getLeftText()[1] + "    SIXES  " + 
						lowerThird.getLeftText()[2] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + "STRIKE RATE " +lowerThird.getRightText()[0] +"\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$2$Data$Right$txt_1*GEOM*TEXT SET " +"" +"\0", print_writers);
					break;
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + "FOURS  " + lowerThird.getLeftText()[1] + "                           SIXES  " + 
						lowerThird.getLeftText()[2] + "                           DOTS  " + lowerThird.getLeftText()[3] + "                           STRIKE RATE  " + 
						lowerThird.getLeftText()[4]  + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + lowerThird.getRightText()[0] + " Minutes" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$2$Data$Right$txt_1*GEOM*TEXT SET " + "" + "\0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + "FOURS  " + lowerThird.getLeftText()[1] + "   SIXES  " + 
							lowerThird.getLeftText()[2] + "   NINES  " + lowerThird.getLeftText()[3] + "   DOTS  " + lowerThird.getLeftText()[4] + "   S/R  " + 
							lowerThird.getLeftText()[5].replace(".0", "") + "\0", print_writers);
					
					break;
				}
				
				break;
				
				
			case "Shift_F6":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET -15.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
						+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + "FOURS " + lowerThird.getLeftText()[0] + "      SIXES " + 
						lowerThird.getLeftText()[1] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
						+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + "STRIKE RATE " + lowerThird.getRightText()[0] + "\0", print_writers);
					break;
				
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + "FOURS " + lowerThird.getLeftText()[0] + "      SIXES " + 
						lowerThird.getLeftText()[1] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + "STRIKE RATE " + lowerThird.getRightText()[0] + "\0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET 0.0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Right$txt_1*TRANSFORMATION*POSITION*X SET 275.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + "FOURS " + lowerThird.getLeftText()[0] + "  SIXES " + 
							lowerThird.getLeftText()[1] + "  NINES " + lowerThird.getLeftText()[2] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + "S/R " + lowerThird.getRightText()[0] + "\0", print_writers);
					
					break;
				}
				
				break;	
				
			case "Control_Shift_F":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						System.out.println(lowerThird.getStatsText()[i]);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
					}
					break;
				}
				break;
			case "F5":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
					}
					break;
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
					}
					break;
				case Constants.BENGAL_T20:
					
					if(lowerThird.getWhichSponsor().toUpperCase().equalsIgnoreCase("SPONSOR")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_BatsmanScore_LT$Sublines"+
								"$Data1$txt_Title" + "*GEOM*TEXT SET " + "FOURS" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_BatsmanScore_LT$Sublines"+
								"$Data2$txt_Title" + "*GEOM*TEXT SET " + "SIXES" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_BatsmanScore_LT$Sublines"+
								"$Data1$txt_Fif" + "*GEOM*TEXT SET " + lowerThird.getStatsText()[2] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$gfx_BatsmanScore_LT$Sublines"+
								"$Data2$txt_Fif" + "*GEOM*TEXT SET " + lowerThird.getStatsText()[3] + "\0", print_writers);
					}else {
						String statName = "";
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
								"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
								"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
								"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
								+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET "+ "" + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
								+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET "+ ""+ "\0", print_writers);
						
						if(stat != null) {
							if(stat.getStats_type().getStats_short_name().equalsIgnoreCase("DT20")) {
								statName = "T20";
							}else {
								statName = stat.getStats_type().getStats_short_name();
							}
							
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
									+ "$Select_Subline$3$Data$Left$txt_1*GEOM*TEXT SET "+ statName +" CAREER - "+ "   MATCHES  "+ lowerThird.getLeftText()[1] +"  | "
											+" RUNS  "+ lowerThird.getLeftText()[2] + "  | " +" S/R  "+ CricketFunctions.generateStrikeRate(stat.getRuns(), stat.getBalls_faced(), 1) + "\0", print_writers);
						}
						for(int i=0; i<lowerThird.getTitlesText().length; i++) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
									containerName+ "$Select_Subline$1" + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
									containerName+ "$Select_Subline$2" + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
									containerName+ "$Select_Subline$3" + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + "" + "\0", print_writers);
						}
					}
					break;	
				}
				
				break;
				
			case "F9":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					if(!lowerThird.getWhichSponsor().equalsIgnoreCase("SPONSOR")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
								"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
								"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
								"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
						for(int i=0; i<lowerThird.getTitlesText().length; i++) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
									containerName+ "$Select_Subline$1" + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
									containerName+ "$Select_Subline$2" + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
						}
					}
					break;
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2" + containerName + "$Data$Stat$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
					}
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
					}
					break;
				}
				
				break;
			case "Control_h":
				
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2" + containerName + "$Data$Stat$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
					}
					break;
				case Constants.ISPL:
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$2$Data$Stat$txt_1*GEOM*TEXT SET \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$3$Data$Stat$txt_1*GEOM*TEXT SET \0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
							
					}
					
					for(int i=1; i<lowerThird.getStatsText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split("-")[0] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i].split("-")[1] + "\0", print_writers);
							
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$3$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
					
					break;
				}
				break;	
			case "Alt_Shift_F3":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET "+ "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET "+ "" + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET "+ lowerThird.getRightText()[0]+ "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$2$Data$Right$txt_1*GEOM*TEXT SET "+ "" + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$3$Data$Left$txt_1*GEOM*TEXT SET "+ lowerThird.getRightText()[1] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$3$Data$Right$txt_1*GEOM*TEXT SET "+ "" + "\0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
								containerName+"$Select_Subline$1" + "$Data$Title$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
									containerName+"$Select_Subline$2" + "$Data$Stat$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
									containerName+"$Select_Subline$3" + "$Data$Stat$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getLeftText()[i] + "\0", print_writers);
					}
					break;
				}
				break;
			case "Shift_F5": case "Shift_F9": case "Alt_F12":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
								containerName+"$Select_Subline$1" + "$Data$Title$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
									containerName+"$Select_Subline$2" + "$Data$Stat$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
					}
					break;
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1" + containerName + "$Data$Title$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2" + containerName + "$Data$Stat$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
					}
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					for(int i=0; i<lowerThird.getTitlesText().length; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$1$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$2$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
					}
					break;
				}
				
				break;
				
			case "F7": case "F11": case "Control_s": case "Control_f":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					for(int iStat = 0; iStat < lowerThird.getTitlesText().length; iStat++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Title$txt_" + (iStat + 1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[iStat] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$2$Data$Stat$txt_" + (iStat + 1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[iStat] + "\0", print_writers);
					}
					break;
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					for(int iStat = 0; iStat < lowerThird.getTitlesText().length; iStat++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$1$Data$Title$txt_" + (iStat + 1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[iStat] + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$2$Data$Stat$txt_" + (iStat + 1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[iStat] + "\0", print_writers);
					}
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					for(int iStat = 0; iStat < lowerThird.getTitlesText().length; iStat++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$1$Data$Title$txt_" + (iStat + 1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[iStat] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$2$Data$Stat$txt_" + (iStat + 1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[iStat] + "\0", print_writers);
					}
					break;
				}
				break;
				
			case "F8": case "Alt_F8":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET -15.0 \0", print_writers);
					
					if(lowerThird.getLeftText()[0].equalsIgnoreCase("Captain") || lowerThird.getLeftText()[0].equalsIgnoreCase("Captain Wicket-Keeper") ||
							lowerThird.getLeftText()[0].equalsIgnoreCase("Wicket-Keeper")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
								+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getSubTitle() + " " + lowerThird.getLeftText()[0].toUpperCase() + "\0", print_writers);
						
					}else if(lowerThird.getLeftText()[0].equalsIgnoreCase("Team")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
								+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
						
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
								+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0].toUpperCase() + "\0", print_writers);
					}
					
					break;
				case Constants.ICC_U19_2023:
					if(lowerThird.getLeftText()[0].equalsIgnoreCase("Captain") || lowerThird.getLeftText()[0].equalsIgnoreCase("Captain Wicket-Keeper") ||
							lowerThird.getLeftText()[0].equalsIgnoreCase("Wicket-Keeper")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Sublines$Side_" + WhichSide 
								+ "$txt_1*GEOM*TEXT SET " + lowerThird.getSubTitle() + " " + lowerThird.getLeftText()[0].toUpperCase() + "\0", print_writers);
					}else if(lowerThird.getLeftText()[0].equalsIgnoreCase("Team")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Sublines$Side_" + WhichSide 
								+ "$txt_1*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Sublines$Side_" + WhichSide 
								+ "$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0].toUpperCase() + "\0", print_writers);
					}
					break;
				case Constants.ISPL:
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Left$txt_1*FUNCTION*Maxsize*WIDTH_X SET 650.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					if(lowerThird.getLeftText()[0].equalsIgnoreCase("Captain") || lowerThird.getLeftText()[0].equalsIgnoreCase("Wicket-Keeper")) {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getSubTitle() + " " + lowerThird.getLeftText()[0].toUpperCase() + "\0", print_writers);
					}else if(lowerThird.getLeftText()[0].equalsIgnoreCase("Captain Wicket-Keeper")) {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getScoreText() + " " + lowerThird.getLeftText()[0].toUpperCase() + "\0", print_writers);
					}else if(lowerThird.getLeftText()[0].equalsIgnoreCase("Team")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
								"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0].toUpperCase() + "\0", print_writers);
					}
					break;
				}
				break;
				
			case "F10":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET -15.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0].toUpperCase() + "\0", print_writers);
					break;
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
							"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0].toUpperCase() + "\0", print_writers);
					break;
				}	
				
				break;
			case "Alt_a": case "Alt_s":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$txt_1*GEOM*TEXT SET " + lowerThird.getSubTitle() + " " + lowerThird.getLeftText()[0].toUpperCase() + "\0", print_writers);
				break;
				
			case "Control_g":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
				break;
				
			case "e":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET -3.0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$2$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET 0.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
						+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + " " + lowerThird.getLeftText()[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET "+lowerThird.getLeftText()[1]+ "\0", print_writers);
					break;
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " +lowerThird.getLeftText()[0]+ "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET "+ "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET "+lowerThird.getLeftText()[1]+ "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$2$Data$Right$txt_1*GEOM*TEXT SET " + "\0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$Side" + WhichSide +
							"$In$BottomTextGrp$geom_MaxSize*FUNCTION*Maxsize*DEFAULT_SCALE_X SET 1.7\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$2$Data$Right$txt_1*GEOM*TEXT SET \0", print_writers);
					break;
				}
				break;
				
			case "d":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET -13.0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide 
						+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + " " + lowerThird.getLeftText()[0] + lowerThird.getLeftText()[1] + "\0", print_writers);
					break;
				case Constants.ICC_U19_2023:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + " " + lowerThird.getLeftText()[0] + 
						lowerThird.getLeftText()[1] + "\0", print_writers);
					break;
				case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$Side" + WhichSide +
							"$In$BottomTextGrp$geom_MaxSize*FUNCTION*Maxsize*DEFAULT_SCALE_X SET 1.7\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$4$Data$Title*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + " " + lowerThird.getLeftText()[0] + 
						lowerThird.getLeftText()[1] + "\0", print_writers);
					break;
				}
				break;	
			}
			break;
		}
		return Constants.OK;
	}
	public void HideAndShowL3rdSubStrapContainers(int WhichSide)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.BENGAL_T20:
			//Show number of sublines
			for(int i=1; i<=10; i++) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
						"$Select_Subline$1$Data$Stat$txt_" + i + "*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
						"$Select_Subline$2$Data$Stat$txt_" + i + "*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
						"$Select_Subline$3$Data$Stat$txt_" + i + "*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
						"$Select_Subline$4$Data$Stat$txt_" + i + "*ACTIVE SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
						containerName + "$Select_Subline$2$Data$Stat$txt_" + i + "*FUNCTION*Maxsize*DEFAULT_SCALE_X SET 0.28\0", print_writers);
			}
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side"+ WhichSide + 
					"$Select_Subline*FUNCTION*Omo*vis_con SET " + lowerThird.getNumberOfSubLines() + "\0", print_writers);
			
			if(lowerThird.getNumberOfSubLines() == 4) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
						"$Select_Subline$2$Data$Stat*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
						"$Select_Subline$3$Data$Stat*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
						"$Select_Subline$4$Data$Stat*ACTIVE SET 1 \0", print_writers);
			}else if(lowerThird.getNumberOfSubLines() == 3) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
						"$Select_Subline$2$Data$Stat*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
						"$Select_Subline$3$Data$Stat*ACTIVE SET 1 \0", print_writers);
			}else if(lowerThird.getNumberOfSubLines() == 2) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
						"$Select_Subline$2$Data$Stat*ACTIVE SET 1 \0", print_writers);
			}
			
			for(int iSubLine = 1; iSubLine <= lowerThird.getNumberOfSubLines(); iSubLine++) {
				
				//HIDE AND SHOW TITLE
				if(lowerThird.getTitlesText() != null) {
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
						"$Select_Subline$"+ iSubLine + "$Data$Title*ACTIVE SET 1 \0", print_writers);
					
					for(int iTitle = 1; iTitle <= lowerThird.getTitlesText().length; iTitle++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Title$txt_" + iTitle + "*ACTIVE SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Title$txt_" + iTitle + "*TRANSFORMATION*POSITION*X SET " 
							+ lowerThird.getPosition()[iTitle-1] + "\0",print_writers);
					}
					//Hide number of Titles on each strap
					for(int iTitle = lowerThird.getTitlesText().length + 1; iTitle <= 10; iTitle++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
								"$Select_Subline$"+ iSubLine + "$Data$Title$txt_" + iTitle + "*ACTIVE SET 0 \0", print_writers);
					}
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Title*ACTIVE SET 0 \0", print_writers);
				}
				
				// HIDE AND SHOW STATS
				if(lowerThird.getStatsText() != null) {
					
					for(int iStats = 1; iStats <= lowerThird.getStatsText().length; iStats++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Stat$txt_" + iStats + "*ACTIVE SET 1 \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$" + iSubLine + "$Data$Stat$txt_" + iStats + "*TRANSFORMATION*POSITION*X SET " + lowerThird.getPosition()[iStats-1] + "\0",print_writers);
					}
					//Hide number of Titles on each strap
					for(int iStats = lowerThird.getStatsText().length + 1; iStats <= 10; iStats++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side" + WhichSide +
								"$Select_Subline$"+ iSubLine + "$Data$Stat$txt_" + iStats + "*ACTIVE SET 0 \0", print_writers);
					}
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Stat*ACTIVE SET 0 \0", print_writers);
				}
				
				//Show Left on each strap
				if(lowerThird.getLeftText() != null &&  lowerThird.getLeftText().length > 0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Left*ACTIVE SET 1 \0", print_writers);
				} else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Left*ACTIVE SET 0 \0", print_writers);
				}
				//Show Right on each strap
				if(lowerThird.getRightText() != null && lowerThird.getRightText().length > 0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Right*ACTIVE SET 1 \0", print_writers);
					if(lowerThird.getPosition() != null && lowerThird.getPosition().length > 0) {
						for(int iTitle = 1; iTitle <= lowerThird.getRightText().length; iTitle++) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
								"$Select_Subline$" + iTitle + "$Data$Right*ACTIVE SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
								"$Select_Subline$" + iTitle + "$Data$Right*TRANSFORMATION*POSITION*X SET " 
								+ lowerThird.getPosition()[0] + "\0",print_writers);
						}
					}
					
					
				} else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Right*ACTIVE SET 0 \0", print_writers);
				}
			}
			
			break;
		
		case Constants.ICC_U19_2023:
			//Show number of sublines
			for(int i=1; i<=10; i++) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1$Data$Stat$fig_" + i + "*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Stat$fig_" + i + "*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Stat$fig_" + i + "*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Stat$fig_" + i + "*ACTIVE SET 0 \0", print_writers);
			}
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_"+ WhichSide + 
					"$Select_Subline$1$Select_Base*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$1" + containerName + "$Divider*ACTIVE SET 0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$2" + containerName + "$Divider*ACTIVE SET 0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$3" + containerName + "$Divider*ACTIVE SET 0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$4" + containerName + "$Divider*ACTIVE SET 0 \0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$1" + containerName + "$Data$Set_Material*MATERIAL SET /Default/Essentials/TextColor/DarkBlue \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$3" + containerName + "$Data$Set_Material*MATERIAL SET /Default/Essentials/TextColor/DarkBlue \0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_"+ WhichSide + 
					"$Select_Subline*FUNCTION*Omo*vis_con SET " + lowerThird.getNumberOfSubLines() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$1$Data$Stat*ACTIVE SET 0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
			
			if(lowerThird.getNumberOfSubLines() == 4) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Stat*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Stat*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Stat*ACTIVE SET 1 \0", print_writers);
			}else if(lowerThird.getNumberOfSubLines() == 3) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Stat*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Stat*ACTIVE SET 1 \0", print_writers);
			}else if(lowerThird.getNumberOfSubLines() == 2) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Stat*ACTIVE SET 1 \0", print_writers);
			}
			
			for(int iSubLine = 1; iSubLine <= lowerThird.getNumberOfSubLines(); iSubLine++) {
				
				//HIDE AND SHOW TITLE
				if(lowerThird.getTitlesText() != null) {
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$"+ iSubLine + "$Data$Title*ACTIVE SET 1 \0", print_writers);
					
					for(int iTitle = 1; iTitle <= lowerThird.getTitlesText().length; iTitle++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Title$txt_" + iTitle + "*ACTIVE SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Title$txt_" + iTitle + "*TRANSFORMATION*POSITION*X SET " 
							+ lowerThird.getPosition()[iTitle-1] + "\0",print_writers);
					}
					//Hide number of Titles on each strap
					for(int iTitle = lowerThird.getTitlesText().length + 1; iTitle <= 10; iTitle++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$"+ iSubLine + "$Data$Title$txt_" + iTitle + "*ACTIVE SET 0 \0", print_writers);
					}
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Title*ACTIVE SET 0 \0", print_writers);
				}
				
				// HIDE AND SHOW STATS
				if(lowerThird.getStatsText() != null) {
					
					for(int iStats = 1; iStats <= lowerThird.getStatsText().length; iStats++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Stat$txt_" + iStats + "*ACTIVE SET 1 \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_" + WhichSide +
							"$Select_Subline$" + iSubLine + "$Data$Stat$txt_" + iStats + "*TRANSFORMATION*POSITION*X SET " + lowerThird.getPosition()[iStats-1] + "\0",print_writers);
					}
					//Hide number of Titles on each strap
					for(int iStats = lowerThird.getStatsText().length + 1; iStats <= 10; iStats++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$"+ iSubLine + "$Data$Stat$txt_" + iStats + "*ACTIVE SET 0 \0", print_writers);
					}
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Stat*ACTIVE SET 0 \0", print_writers);
				}
				
				//Show Left on each strap
				if(lowerThird.getLeftText() != null &&  lowerThird.getLeftText().length > 0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Left*ACTIVE SET 1 \0", print_writers);
				} else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Left*ACTIVE SET 0 \0", print_writers);
				}
				//Show Right on each strap
				if(lowerThird.getRightText() != null && lowerThird.getRightText().length > 0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Right*ACTIVE SET 1 \0", print_writers);
					if(lowerThird.getPosition() != null && lowerThird.getPosition().length > 0) {
						for(int iTitle = 1; iTitle <= lowerThird.getRightText().length; iTitle++) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$" + iTitle + "$Data$Right*ACTIVE SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_" + WhichSide +
								"$Select_Subline$" + iTitle + "$Data$Right*TRANSFORMATION*POSITION*X SET " 
								+ lowerThird.getPosition()[0] + "\0",print_writers);
						}
					}
					
					
				} else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Right*ACTIVE SET 0 \0", print_writers);
				}
			}
			
			break;
		case Constants.ISPL:
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$TopLine$TopData_Position_Y$Side" + WhichSide +
					"$BottomTextGrp$geom_MaxSize*FUNCTION*Maxsize*WIDTH_X SET 500.0\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
					"$Select_Subline$1$Left$txt_1*FUNCTION*Maxsize*WIDTH_X SET 1000.0 \0", print_writers);
			
			//Show number of sublines
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
					"$Select_Subline$1$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET 0.0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
					"$Select_Subline$2$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET 0.0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
					"$Select_Subline$3$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET 0.0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
					"$Select_Subline$4$Data$Left$txt_1*TRANSFORMATION*POSITION*X SET 0.0 \0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
					"$Select_Subline$1$Data$Right$txt_1*TRANSFORMATION*POSITION*X SET 0.0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
					"$Select_Subline$2$Data$Right$txt_1*TRANSFORMATION*POSITION*X SET 0.0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
					"$Select_Subline$3$Data$Right$txt_1*TRANSFORMATION*POSITION*X SET 0.0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
					"$Select_Subline$4$Data$Right$txt_1*TRANSFORMATION*POSITION*X SET 0.0 \0", print_writers);
			
			for(int i=1; i<=10; i++) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
						"$Select_Subline$1$Data$Stat$fig_" + i + "*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
						"$Select_Subline$2$Data$Stat$fig_" + i + "*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
						"$Select_Subline$3$Data$Stat$fig_" + i + "*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
						"$Select_Subline$4$Data$Stat$fig_" + i + "*ACTIVE SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide +
						"$Select_Subline$2$Data$Stat$txt_" + (i) + "*FUNCTION*Maxsize*DEFAULT_SCALE_X SET 1.05\0", print_writers);
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$Side" + WhichSide +
					"$In$BottomTextGrp$geom_MaxSize*FUNCTION*Maxsize*DEFAULT_SCALE_X SET 1.0\0", print_writers);
			
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_"+ WhichSide + 
//					"$Select_Subline$1$Select_Base*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
//					"$Select_Subline$1" + containerName + "$Divider*ACTIVE SET 0 \0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
//					"$Select_Subline$2" + containerName + "$Divider*ACTIVE SET 0 \0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
//					"$Select_Subline$3" + containerName + "$Divider*ACTIVE SET 0 \0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
//					"$Select_Subline$4" + containerName + "$Divider*ACTIVE SET 0 \0", print_writers);
			
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
//					"$Select_Subline$1" + containerName + "$Data$Set_Material*MATERIAL SET /Default/Essentials/TextColor/DarkBlue \0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
//					"$Select_Subline$3" + containerName + "$Data$Set_Material*MATERIAL SET /Default/Essentials/TextColor/DarkBlue \0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side"+ WhichSide + 
					"$Select_Subline*FUNCTION*Omo*vis_con SET " + lowerThird.getNumberOfSubLines() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
					"$Select_Subline$1$Data$Stat*ACTIVE SET 0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
					"$Select_Subline$2$Data$Stat*ACTIVE SET 0 \0", print_writers);
			
			if(lowerThird.getNumberOfSubLines() == 4) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
						"$Select_Subline$2$Data$Stat*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
						"$Select_Subline$3$Data$Stat*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
						"$Select_Subline$4$Data$Stat*ACTIVE SET 1 \0", print_writers);
			}else if(lowerThird.getNumberOfSubLines() == 3) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
						"$Select_Subline$2$Data$Stat*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
						"$Select_Subline$3$Data$Stat*ACTIVE SET 1 \0", print_writers);
			}else if(lowerThird.getNumberOfSubLines() == 2) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
						"$Select_Subline$2$Data$Stat*ACTIVE SET 1 \0", print_writers);
			}
			
			for(int iSubLine = 1; iSubLine <= lowerThird.getNumberOfSubLines(); iSubLine++) {
				
				//HIDE AND SHOW TITLE
				if(lowerThird.getTitlesText() != null) {
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Title*ACTIVE SET 1 \0", print_writers);
					
					for(int iTitle = 1; iTitle <= lowerThird.getTitlesText().length; iTitle++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Title$txt_" + iTitle + "*ACTIVE SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Title$txt_" + iTitle + "*TRANSFORMATION*POSITION*X SET "
							+ lowerThird.getPosition()[iTitle-1] + " \0", print_writers);
					}
					//Hide number of Titles on each strap
					for(int iTitle = lowerThird.getTitlesText().length + 1; iTitle <= 10; iTitle++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
								"$Select_Subline$"+ iSubLine + "$Data$Title$txt_" + iTitle + "*ACTIVE SET 0 \0", print_writers);
					}
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Title*ACTIVE SET 0 \0", print_writers);
				}
				
				// HIDE AND SHOW STATS
				if(lowerThird.getStatsText() != null) {
					
					for(int iStats = 1; iStats <= lowerThird.getStatsText().length; iStats++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Stat$txt_" + iStats + "*ACTIVE SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Stat$txt_" + iStats + "*TRANSFORMATION*POSITION*X SET "
							+ lowerThird.getPosition()[iStats-1] + " \0", print_writers);
					}
					//Hide number of Titles on each strap
					for(int iStats = lowerThird.getStatsText().length + 1; iStats <= 10; iStats++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
								"$Select_Subline$"+ iSubLine + "$Data$Stat$txt_" + iStats + "*ACTIVE SET 0 \0", print_writers);
					}
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Stat*ACTIVE SET 0 \0", print_writers);
				}
				
				//Show Left on each strap
				if(lowerThird.getLeftText() != null &&  lowerThird.getLeftText().length > 0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Left*ACTIVE SET 1 \0", print_writers);
				} else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Left*ACTIVE SET 0 \0", print_writers);
				}
				//Show Right on each strap
				if(lowerThird.getRightText() != null && lowerThird.getRightText().length > 0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Right*ACTIVE SET 1 \0", print_writers);
					if(lowerThird.getPosition() != null && lowerThird.getPosition().length > 0) {
						for(int iTitle = 1; iTitle <= lowerThird.getRightText().length; iTitle++) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
								"$Select_Subline$"+ iTitle + "$Data$Right*ACTIVE SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
									"$Select_Subline$"+ iTitle + "$Data$Right*TRANSFORMATION*POSITION*X SET "
									+ lowerThird.getPosition()[0] + " \0", print_writers);
						}
					}
					
					
				} else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$SubLines$Side" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Right*ACTIVE SET 0 \0", print_writers);
				}
			}
			
			break;	
		}
	}
	
	public void HideAndShowGriffSubStrapContainers(int WhichSide)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023: case Constants.ISPL:
			//Show number of sublines
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_"+ WhichSide + 
					"$Select_Subline$1$Select_Base*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$1" + containerName + "$Divider*ACTIVE SET 0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$2" + containerName + "$Divider*ACTIVE SET 0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$3" + containerName + "$Divider*ACTIVE SET 0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$4" + containerName + "$Divider*ACTIVE SET 0 \0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_"+ WhichSide + 
					"$Select_Subline*FUNCTION*Omo*vis_con SET " + l3griff.getNumberOfSubLines() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$1$Data$Stat*ACTIVE SET 0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
			
			if(l3griff.getNumberOfSubLines() == 4) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Stat*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Stat*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Stat*ACTIVE SET 1 \0", print_writers);
			}else if(l3griff.getNumberOfSubLines() == 3) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Stat*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Stat*ACTIVE SET 1 \0", print_writers);
			}else if(l3griff.getNumberOfSubLines() == 2) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Stat*ACTIVE SET 1 \0", print_writers);
			}
			
			//HIDE AND SHOW TITLE
			if(l3griff.getTopTitlesText() != null) {
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$1$Data$Title*ACTIVE SET 1 \0", print_writers);
				
				for(int iTitle = 1; iTitle <= l3griff.getTopTitlesText().length; iTitle++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1$Data$Title$txt_" + iTitle + "*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_" + WhichSide +
						"$Select_Subline$1$Data$Title$txt_" + iTitle + "*TRANSFORMATION*POSITION*X SET " 
						+ l3griff.getPosition()[iTitle-1] + "\0",print_writers);
				}
				//Hide number of Titles on each strap
				for(int iTitle = l3griff.getTopTitlesText().length + 1; iTitle <= 10; iTitle++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1$Data$Title$txt_" + iTitle + "*ACTIVE SET 0 \0", print_writers);
				}
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1$Data$Title*ACTIVE SET 0 \0", print_writers);
			}
			
			// HIDE AND SHOW STATS
			if(l3griff.getTopStatsText() != null) {
				
				for(int iStats = 1; iStats <= l3griff.getTopStatsText().length; iStats++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Stat$txt_" + iStats + "*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Stat$txt_" + iStats + "*TRANSFORMATION*POSITION*X SET " + l3griff.getPosition()[iStats-1] + "\0",print_writers);
				}
				//Hide number of Titles on each strap
				for(int iStats = l3griff.getTopStatsText().length + 1; iStats <= 10; iStats++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Stat$txt_" + iStats + "*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Stat$fig_" + iStats + "*ACTIVE SET 0 \0", print_writers);
				}
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Stat*ACTIVE SET 0 \0", print_writers);
			}
			
			//HIDE AND SHOW TITLE
			if(l3griff.getBottomTitlesText() != null) {
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$3$Data$Title*ACTIVE SET 1 \0", print_writers);
				
				for(int iTitle = 1; iTitle <= l3griff.getBottomTitlesText().length; iTitle++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Title$txt_" + iTitle + "*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Title$txt_" + iTitle + "*TRANSFORMATION*POSITION*X SET " 
						+ l3griff.getPosition()[l3griff.getTopTitlesText().length + (iTitle-1)] + "\0",print_writers);
				}
				//Hide number of Titles on each strap
				for(int iTitle = l3griff.getBottomTitlesText().length + 1; iTitle <= 10; iTitle++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$3$Data$Title$txt_" + iTitle + "*ACTIVE SET 0 \0", print_writers);
				}
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$3$Data$Title*ACTIVE SET 0 \0", print_writers);
			}
			
			// HIDE AND SHOW STATS
			if(l3griff.getBottomStatsText() != null) {
				
				for(int iStats = 1; iStats <= l3griff.getBottomStatsText().length; iStats++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Stat$txt_" + iStats + "*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Stat$txt_" + iStats + "*TRANSFORMATION*POSITION*X SET " + 
							l3griff.getPosition()[l3griff.getTopStatsText().length + (iStats-1)] + "\0",print_writers);
				}
				//Hide number of Titles on each strap
				for(int iStats = l3griff.getBottomStatsText().length + 1; iStats <= 10; iStats++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Stat$txt_" + iStats + "*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$4$Data$Stat$fig_" + iStats + "*ACTIVE SET 0 \0", print_writers);
				}
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$4$Data$Stat*ACTIVE SET 0 \0", print_writers);
			}
			
			
			break;
		}
	}
	
	public void setPositionOfLT(String whatToProcess,int WhichSide,Configuration config,int subline)
	{
		String LT_Position_1 = "",LT_Position_2 = "",LT_Position_3 = "",LT_Position_4 = "",LT_Position_5 = "",LT_Position_6 = "",LT_Position_7 = "",
				LT_Flag_Keys="",LT_Flag_Object="",LT_Flag_ObjectX="",LT_Flag_ObjectY="",LT_Flag_ObjectZ="",slave_Flag_Keys = "",slave_Flag_Object="";
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ISPL:
			switch (subline) {
			case 0:
				LT_Position_1 = "156.0";
				LT_Position_2 = "-36.0 -52.5 0";
				LT_Position_3 = "0.0 -52.5 0.0";
				LT_Position_4 = "159.0";
				LT_Position_5 = "1.0 1.0 1.0";
				LT_Position_6 = "-52.5";
				LT_Position_7 = "156.0";
				break;
			case 1:
				LT_Position_1 = "208.5";
				LT_Position_2 = "0.0 0.0 0.0";
				LT_Position_3 = "0.0 0.0 0.0";
				LT_Position_4 = "211.5";
				LT_Position_5 = "1.0 1.0 1.0";
				LT_Position_6 = "0.0";
				LT_Position_7 = "208.5";
				break;
			case 2:
				LT_Position_1 = "261.0";
				LT_Position_2 = "33.25 52.5 0.0";
				LT_Position_3 = "33.25 52.5 0.0";
				LT_Position_4 = "264.0";
				LT_Position_5 = "1.35 1.35 1.35";
				LT_Position_6 = "52.5";
				LT_Position_7 = "261.0";
				break;
			case 3:
				LT_Position_1 = "313.0";
				LT_Position_2 = "67.65 105.0 0.0";
				LT_Position_3 = "67.65 105.0 0.0";
				LT_Position_4 = "316.5";
				LT_Position_5 = "1.35 1.35 1.35";
				LT_Position_6 = "105.0";
				LT_Position_7 = "313.5";
				break;
			case 4:
				LT_Position_1 = "366.0";
				LT_Position_2 = "104.0 157.5 0.0";
				LT_Position_3 = "104.0 157.5 0.0";
				LT_Position_4 = "369.0";
				LT_Position_5 = "1.35 1.35 1.35";
				LT_Position_6 = "152.5";
				LT_Position_7 = "366.0";
				break;	
			}
			
			if(WhichSide == 1) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$WhiteBigBase*ANIMATION*KEY*$White_Base_Y1*VALUE SET "
						+ LT_Position_1 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$TopHeaderGrp$TopPositionForSublines*ANIMATION*KEY*$TopHeader_Y1*VALUE SET "
						+ LT_Position_2 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y*ANIMATION*KEY*$TopData_Y1*VALUE SET "
						+ LT_Position_3 + "\0",print_writers);
				
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side1$BaseGrp$img_Base2$GrungeBase*ANIMATION*KEY*$GrungeBase_Side1_Y1*VALUE SET "
						+ LT_Position_4 + "\0",print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$Mask_2_3*ANIMATION*KEY*$Mask_2_3_Y1*VALUE SET "
//						+ LT_Position_4 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side1$BaseGrp$img_Base2$img_TeamLogoBW*ANIMATION*KEY*$Logo_BW_Side1_Y1*VALUE SET "
						+ LT_Position_4 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side1$BaseGrp$x*ANIMATION*KEY*$X_Side1_Y1*VALUE SET "
						+ LT_Position_4 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side1$LogoScale*ANIMATION*KEY*$LogoScale_Side1_1*VALUE SET "
						+ LT_Position_5 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side1$LogoScale*ANIMATION*KEY*$LogoScale_Side1_2*VALUE SET "
						+ LT_Position_5 + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side1$BaseGrp$img_Base2$GrungeBase*ANIMATION*KEY*$GrungeBase_Side1_Y2*VALUE SET "
						+ LT_Position_4 + "\0",print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$Mask_2_3*ANIMATION*KEY*$Mask_2_3_Y1*VALUE SET "
//						+ LT_Position_4 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side1$BaseGrp$img_Base2$img_TeamLogoBW*ANIMATION*KEY*$Logo_BW_Side1_Y2*VALUE SET "
						+ LT_Position_4 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side1$BaseGrp$x*ANIMATION*KEY*$X_Side1_Y2*VALUE SET "
						+ LT_Position_4 + "\0",print_writers);
				
				
			}else if(WhichSide == 2) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$WhiteBigBase*ANIMATION*KEY*$White_Base_Y2*VALUE SET "
						+ LT_Position_1 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$TopHeaderGrp$TopPositionForSublines*ANIMATION*KEY*$TopHeader_Y2*VALUE SET "
						+ LT_Position_2 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y*ANIMATION*KEY*$TopData_Y2*VALUE SET "
						+ LT_Position_3 + "\0",print_writers);
				
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side2$BaseGrp$img_Base2$GrungeBase*ANIMATION*KEY*$GrungeBase_Side2_Y1*VALUE SET "
						+ LT_Position_4 + "\0",print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$Mask_2_3*ANIMATION*KEY*$Mask_2_3_Y1*VALUE SET "
//						+ LT_Position_4 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side2$BaseGrp$img_Base2$img_TeamLogoBW*ANIMATION*KEY*$Logo_BW_Side2_Y1*VALUE SET "
						+ LT_Position_4 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side2$BaseGrp$x*ANIMATION*KEY*$X_Side2_Y1*VALUE SET "
						+ LT_Position_4 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side2$LogoScale*ANIMATION*KEY*$LogoScale_Side2_1*VALUE SET "
						+ LT_Position_5 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side2$LogoScale*ANIMATION*KEY*$LogoScale_Side2_2*VALUE SET "
						+ LT_Position_5 + "\0",print_writers);
				
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side2$BaseGrp$img_Base2$GrungeBase*ANIMATION*KEY*$GrungeBase_Side2_Y2*VALUE SET "
						+ LT_Position_4 + "\0",print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$Mask_2_3*ANIMATION*KEY*$Mask_2_3_Y2*VALUE SET "
//						+ LT_Position_4 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side2$BaseGrp$img_Base2$img_TeamLogoBW*ANIMATION*KEY*$Logo_BW_Side2_Y2*VALUE SET "
						+ LT_Position_4 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side2$BaseGrp$x*ANIMATION*KEY*$X_Side2_Y2*VALUE SET "
						+ LT_Position_4 + "\0",print_writers);
				
				
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side2$BaseGrp$img_Base2$GrungeBase*ANIMATION*KEY*$GrungeBase_Y_Side2_IN1*VALUE SET "
						+ LT_Position_4 + "\0",print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$Mask_2_3*ANIMATION*KEY*$Mask_2_3_Y_IN1*VALUE SET "
//						+ LT_Position_4 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side2$BaseGrp$img_Base2$img_TeamLogoBW*ANIMATION*KEY*$Logo_BW_Y_Side2_IN1*VALUE SET "
						+ LT_Position_4 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side2$BaseGrp$x*ANIMATION*KEY*$X_Y_Side2_IN1*VALUE SET "
						+ LT_Position_4 + "\0",print_writers);
				
			}
			
			if(WhichSide == 1) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$WhiteBigBase*ANIMATION*KEY*$White_Base_In*VALUE SET "
						+ LT_Position_1 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$TopHeaderGrp$TopPositionForSublines*ANIMATION*KEY*$TopHeader_Y_IN1*VALUE SET "
						+ LT_Position_2 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$TopData_Position_Y*ANIMATION*KEY*$TopData_Y_IN1*VALUE SET "
						+ LT_Position_3 + "\0",print_writers);
				
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side1$BaseGrp$img_Base2$GrungeBase*ANIMATION*KEY*$GrungeBase_Y_Side1_IN1*VALUE SET "
						+ LT_Position_4 + "\0",print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$Mask_2_3*ANIMATION*KEY*$Mask_2_3_Y_IN1*VALUE SET "
//						+ LT_Position_4 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side1$BaseGrp$img_Base2$img_TeamLogoBW*ANIMATION*KEY*$Logo_BW_Y_Side1_IN1*VALUE SET "
						+ LT_Position_4 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side1$BaseGrp$x*ANIMATION*KEY*$X_Y_Side1_IN1*VALUE SET "
						+ LT_Position_4 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side1$LogoScale*ANIMATION*KEY*$LogoScale_Side1_1*VALUE SET "
						+ LT_Position_5 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$TeamLogoGrp$LogoGrpOut$Side1$LogoScale*ANIMATION*KEY*$LogoScale_Side1_2*VALUE SET "
						+ LT_Position_5 + "\0",print_writers);
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$SubLines$Side" + WhichSide + 
					"$Set_Rows_Side_"+ WhichSide  +"_Position_Y*TRANSFORMATION*POSITION*Y SET " + LT_Position_6 + "\0",print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$MoveForShrink$Out$TopLine$geom_MaskTopLine_8"
//					+ "*GEOM*height SET " + LT_Position_7 + "\0",print_writers);
			break;
		case Constants.ICC_U19_2023:
			switch (subline) {
			case 0:
				LT_Position_1 = "0.0";
				LT_Position_2 = "0.0 -33.0 0.0";
				LT_Position_3 = "0.0 -8.0 0.0";
				LT_Position_4 = "16.0";
				break;
			case 1:
				LT_Position_1 = "25.0";
				LT_Position_2 = "0.0 0.0 0.0";
				LT_Position_3 = "0.0 -25.0 0.0";
				LT_Position_4 = "50.0";
				break;
			case 2:
				LT_Position_1 = "50.0";
				LT_Position_2 = "0.0 50.0 0.0";
				LT_Position_3 = "0.0 -50.0 0.0";
				LT_Position_4 = "100.0";
				break;
			case 3:
				LT_Position_1 = "75.0";
				LT_Position_2 = "0.0 100.0 0.0";
				LT_Position_3 = "0.0 -75.0 0.0";
				LT_Position_4 = "150.0";
				break;
			case 4:
				LT_Position_1 = "100.0";
				LT_Position_2 = "0.0 150.0 0.0";
				LT_Position_3 = "0.0 -100.0 0.0";
				LT_Position_4 = "200.0";
				break;	
			}
			
			switch (whatToProcess.split(",")[0]) {
			case "Alt_F1": case "Alt_F2":
				ltWhichContainer = "$All_LowerThirds";
				
				if(l3griff.getWhichTeamFlag().trim().isEmpty() && l3griff.getWhichSponsor().trim().isEmpty()) {
					LT_Flag_Keys = "57.0";
					LT_Flag_Object = "1110";
					
					slave_Flag_Keys = "57.0";
					slave_Flag_Object = "1110";
				} else if(!l3griff.getWhichTeamFlag().trim().isEmpty() && !l3griff.getWhichSponsor().trim().isEmpty() && 
						!l3griff.getWhichSponsor().equalsIgnoreCase("HASHTAG")) {
					LT_Flag_Keys = "36.0";
					LT_Flag_Object = "840";
					if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
						slave_Flag_Keys = "47.0";
						slave_Flag_Object = "1020";
					}
				} else if(!l3griff.getWhichTeamFlag().trim().isEmpty() &&
						!l3griff.getWhichSponsor().equalsIgnoreCase("HASHTAG")) {
					LT_Flag_Keys = "47.0";
					LT_Flag_Object = "1020";
					
					slave_Flag_Keys = "47.0";
					slave_Flag_Object = "1020";
				} else if(!l3griff.getWhichSponsor().trim().isEmpty()) {
					switch (l3griff.getWhichSponsor()) {
					case "HASHTAG":
						LT_Flag_Keys = "25.0";
						LT_Flag_Object = "810";
						
						slave_Flag_Keys = "47.0";
						slave_Flag_Object = "1020";
						break;
					case "FLAG":
						LT_Flag_Keys = "37.0";
						LT_Flag_Object = "940";
						
						slave_Flag_Keys = "37.0";
						slave_Flag_Object = "940";
						break;	
					default:
						if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
							slave_Flag_Keys = "57.0";
							slave_Flag_Object = "1110";
						}
						LT_Flag_Keys = "37.0";
						LT_Flag_Object = "940";
						break;
					}
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Sublines$Side_" + WhichSide + "$Set_Rows_Side_" + WhichSide + 
						"_Position_Y*TRANSFORMATION*POSITION*Y SET " + LT_Position_1 + "\0",print_writers);
				break;
			case "Alt_F8": case "F8": case "F10": case "j": case "Alt_a": case "Alt_s":
				
				ltWhichContainer = "$All_NameSupers";
				if(lowerThird.getWhichTeamFlag().isEmpty() && lowerThird.getWhichSponsor().isEmpty()) {
					LT_Flag_Keys = "58.0";
					LT_Flag_Object = "1040";
					
					slave_Flag_Keys = "58.0";
					slave_Flag_Object = "1040";
				} else if(!lowerThird.getWhichTeamFlag().isEmpty() && !lowerThird.getWhichSponsor().isEmpty() && 
						!lowerThird.getWhichSponsor().equalsIgnoreCase("HASHTAG")) {
					LT_Flag_Keys = "20.0";
					LT_Flag_Object = "700";
					if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
						slave_Flag_Keys = "40.0";
						slave_Flag_Object = "900";
					}
				} else if(!lowerThird.getWhichTeamFlag().isEmpty() &&
						!lowerThird.getWhichSponsor().equalsIgnoreCase("HASHTAG")) {
					LT_Flag_Keys = "40.0";
					LT_Flag_Object = "900";
					
					slave_Flag_Keys = "40.0";
					slave_Flag_Object = "900";
				} else if(!lowerThird.getWhichSponsor().isEmpty()) {
					switch (lowerThird.getWhichSponsor()) {
					case "HASHTAG":
						LT_Flag_Keys = "19.0";
						LT_Flag_Object = "680";
						
						slave_Flag_Keys = "40.0";
						slave_Flag_Object = "900";
						break;
					case "FLAG":
						LT_Flag_Keys = "37.0";
						LT_Flag_Object = "940";
						
						slave_Flag_Keys = "37.0";
						slave_Flag_Object = "940";
						break;
					default:
						if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
							slave_Flag_Keys = "58.0";
							slave_Flag_Object = "1040";
						}
						LT_Flag_Keys = "31.0";
						LT_Flag_Object = "810";
						break;
					}
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Sublines$Side_" + WhichSide + "$Set_Rows_Side_" + WhichSide + 
						"_Position_Y*TRANSFORMATION*POSITION*Y SET 2.0 \0",print_writers);
				break;

			default:
				if(lowerThird.getWhichTeamFlag().trim().isEmpty() && lowerThird.getWhichSponsor().trim().isEmpty()) {
					LT_Flag_Keys = "57.0";
					LT_Flag_Object = "1110";
					
					slave_Flag_Keys = "57.0";
					slave_Flag_Object = "1110";
				} else if(!lowerThird.getWhichTeamFlag().trim().isEmpty() && !lowerThird.getWhichSponsor().trim().isEmpty() && 
						!lowerThird.getWhichSponsor().equalsIgnoreCase("HASHTAG")) {
					LT_Flag_Keys = "36.0";
					LT_Flag_Object = "840";
					if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
						slave_Flag_Keys = "47.0";
						slave_Flag_Object = "1020";
					}
				} else if(!lowerThird.getWhichTeamFlag().trim().isEmpty() &&
						!lowerThird.getWhichSponsor().equalsIgnoreCase("HASHTAG")) {
					LT_Flag_Keys = "47.0";
					LT_Flag_Object = "1020";
					
					slave_Flag_Keys = "47.0";
					slave_Flag_Object = "1020";
				} else if(!lowerThird.getWhichSponsor().trim().isEmpty()) {
					switch (lowerThird.getWhichSponsor()) {
					case "HASHTAG":
						LT_Flag_Keys = "25.0";
						LT_Flag_Object = "810";
						
						slave_Flag_Keys = "47.0";
						slave_Flag_Object = "1020";
						break;
					case "FLAG":
						LT_Flag_Keys = "37.0";
						LT_Flag_Object = "940";
						
						slave_Flag_Keys = "37.0";
						slave_Flag_Object = "940";
						break;	
					default:
						if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
							slave_Flag_Keys = "57.0";
							slave_Flag_Object = "1110";
						}
						LT_Flag_Keys = "37.0";
						LT_Flag_Object = "940";
						break;
					}
				}
				
				switch (whatToProcess.split(",")[0]) {
				case "q": case "Control_q":
					if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
						slave_Flag_Keys = "47.0";
						slave_Flag_Object = "1020";
					}
					LT_Flag_Keys = "36.0";
					LT_Flag_Object = "840";
					ltWhichContainer = "$BoundaryLowerthird";
					break;
				default :
					ltWhichContainer = "$All_LowerThirds";
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Sublines$Side_" + WhichSide + "$Set_Rows_Side_" + WhichSide + 
							"_Position_Y*TRANSFORMATION*POSITION*Y SET " + LT_Position_1 + "\0",print_writers);
					break;
				}
				break;
			}
			
			if(!whatToProcess.split(",")[0].equalsIgnoreCase("q") && !whatToProcess.split(",")[0].equalsIgnoreCase("Control_q") && !whatToProcess.split(",")[0].equalsIgnoreCase("Alt_q")) {
				if(!whatToProcess.split(",")[0].equalsIgnoreCase("F8") && !whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F8")
						&& !whatToProcess.split(",")[0].equalsIgnoreCase("F10") && !whatToProcess.split(",")[0].equalsIgnoreCase("j")
						&& !whatToProcess.split(",")[0].equalsIgnoreCase("Alt_a") && !whatToProcess.split(",")[0].equalsIgnoreCase("Alt_s")) {
					if(WhichSide == 1) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Sublines$obj_SublineBase*ANIMATION*KEY*$Change_In_1*VALUE SET "
								+ LT_Position_4 + "\0",print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Sublines$obj__Mask_6_*ANIMATION*KEY*$Change_In_1*VALUE SET "
								+ LT_Position_4 + "\0",print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Overall_Position_Y_In_Out*ANIMATION*KEY*$Change_In_1*VALUE SET " 
								+ LT_Position_2 + "\0",print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Sublines$Sublines_Position_Y*ANIMATION*KEY*$Change_In_1*VALUE SET "
								+ LT_Position_3 + "\0",print_writers);
						
					}else if(WhichSide == 2) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Sublines$obj_SublineBase*ANIMATION*KEY*$Change_In_2*VALUE SET "
								+ LT_Position_4 + "\0",print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Sublines$obj__Mask_6_*ANIMATION*KEY*$Change_In_2*VALUE SET "
								+ LT_Position_4 + "\0",print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Overall_Position_Y_In_Out*ANIMATION*KEY*$Change_In_2*VALUE SET " 
								+ LT_Position_2 + "\0",print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Sublines$Sublines_Position_Y*ANIMATION*KEY*$Change_In_2*VALUE SET "
								+ LT_Position_3 + "\0",print_writers);
					}
				}
			}
			
			if(WhichSide == 1 && !whatToProcess.split(",")[0].equalsIgnoreCase("q") && !whatToProcess.split(",")[0].equalsIgnoreCase("Control_q") && !whatToProcess.split(",")[0].equalsIgnoreCase("Alt_q") ) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Sublines$obj_SublineBase*ANIMATION*KEY*$In_2*VALUE SET "
						+ LT_Position_4 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Sublines$obj_SublineBase*ANIMATION*KEY*$Out_1*VALUE SET "
					+ LT_Position_4 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Sublines$obj__Mask_6_*ANIMATION*KEY*$In_2*VALUE SET "
					+ LT_Position_4 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Sublines$obj__Mask_6_*ANIMATION*KEY*$Out_1*VALUE SET "
					+ LT_Position_4 + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Overall_Position_Y_In_Out*ANIMATION*KEY*$In_2*VALUE SET " 
					+ LT_Position_2 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Overall_Position_Y_In_Out*ANIMATION*KEY*$Out_1*VALUE SET "
					+ LT_Position_2 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Sublines$Sublines_Position_Y*ANIMATION*KEY*$In_2*VALUE SET "
					+ LT_Position_3 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Sublines$Sublines_Position_Y*ANIMATION*KEY*$Out_1*VALUE SET "
					+ LT_Position_3 + "\0",print_writers);
			}
			
			if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
				
				if(WhichSide == 1) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$RightPartSize*ANIMATION*KEY*$H1*VALUE SET "
							+ slave_Flag_Keys + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$RightPartSize*ANIMATION*KEY*$H_In*VALUE SET "
							+ slave_Flag_Keys + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$RightPartSize*ANIMATION*KEY*$H_Out*VALUE SET "
							+ slave_Flag_Keys + "\0", print_writers);
//					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$RightPartSize*ANIMATION*KEY*$H2*VALUE SET "
//							+ slave_Flag_Keys + "\0", print_writers);
				}else if(WhichSide == 2) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$RightPartSize*ANIMATION*KEY*$H2*VALUE SET "
							+ slave_Flag_Keys + "\0", print_writers);
					
				}
				
				CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$Side_" + WhichSide + "$obj_WidthX"
					+ "*FUNCTION*Maxsize*WIDTH_X SET " + slave_Flag_Object + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$Side_" + WhichSide + "$obj_WidthX"
						+ "*FUNCTION*Maxsize*WIDTH_X SET " + slave_Flag_Object + "\0", print_writers);
			}
			
			if(WhichSide == 1) {
				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$RightPartSize*ANIMATION*KEY*$H1*VALUE SET "
						+ LT_Flag_Keys + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$RightPartSize*ANIMATION*KEY*$H_In*VALUE SET "
						+ LT_Flag_Keys + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$RightPartSize*ANIMATION*KEY*$H_Out*VALUE SET "
						+ LT_Flag_Keys + "\0", print_writers);
//				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$RightPartSize*ANIMATION*KEY*$H2*VALUE SET "
//						+ LT_Flag_Keys + "\0", print_writers);
			}else if(WhichSide == 2) {
				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$RightPartSize*ANIMATION*KEY*$H2*VALUE SET "
						+ LT_Flag_Keys + "\0", print_writers);
				
			}
			CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$Side_" + WhichSide + "$obj_WidthX"
				+ "*FUNCTION*Maxsize*WIDTH_X SET " + LT_Flag_Object + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$Side_" + WhichSide + "$obj_WidthX"
					+ "*FUNCTION*Maxsize*WIDTH_X SET " + LT_Flag_Object + "\0", print_writers);
			
			break;
			
		case Constants.BENGAL_T20:
			switch (subline) {
			case 0:
				LT_Position_1 = "0.375"; //Obj_MaskScale Y
				LT_Position_2 = "-64.0"; //Set_Side1_Top_Position_Y/Set_Side2_Top_Position_Y
				LT_Position_3 = "-50.0"; //Set_Side1_Top_Position_X/Set_Side2_Top_Position_X
				LT_Position_4 = "-64.0"; //Set_Rows_Side_1_Position_Y/Set_Rows_Side_2_Position_Y
				LT_Flag_Keys = "0.65"; //SCALE
				LT_Flag_ObjectX = "-40.0";
				LT_Flag_ObjectY = "-20.0";
				LT_Flag_ObjectZ = "0.0";
				break;
			case 1:
				LT_Position_1 = "0.44";
				LT_Position_2 = "-54.0";
				LT_Position_3 = "-40.0";
				LT_Position_4 = "-50.0";
				LT_Flag_Keys = "0.8"; //SCALE
				LT_Flag_ObjectX = "-20.0";//POSITION
				LT_Flag_ObjectY = "-20.0";//POSITION
				LT_Flag_ObjectZ = "0.0";//POSITION
				break;
			case 2:
				LT_Position_1 = "0.64";
				LT_Position_2 = "-36.0";
				LT_Position_3 = "-30.0";
				LT_Position_4 = "-34.0";
				LT_Flag_Keys = "0.8"; //SCALE
				LT_Flag_ObjectX = "-10.0";//POSITION
				LT_Flag_ObjectY = "-10.0";//POSITION
				LT_Flag_ObjectZ = "0.0";//POSITION
				break;
			case 3:
				LT_Position_1 = "0.8";
				LT_Position_2 = "-18.0";
				LT_Position_3 = "-20.0";
				LT_Position_4 = "-18.0";
				LT_Flag_Keys = "0.8"; //SCALE
				LT_Flag_ObjectX = "-10.0";//POSITION
				LT_Flag_ObjectY = "0.0";//POSITION
				LT_Flag_ObjectZ = "0.0";//POSITION
				break;
			case 4:
				LT_Position_1 = "1.0";
				LT_Position_2 = "0.0";
				LT_Position_3 = "0.0";
				LT_Position_4 = "0.0";
				LT_Flag_Keys = "1.0"; //SCALE
				LT_Flag_ObjectX = "0.0";//POSITION
				LT_Flag_ObjectY = "0.0";//POSITION
				LT_Flag_ObjectZ = "0.0";//POSITION
				
				break;	
			}
			ltWhichContainer = "$All_LowerThirds";
			if(WhichSide == 1) {
				//DYNAMIC
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Obj_Mask*ANIMATION*KEY*$Scale3*VALUE*Y SET "
						+ LT_Position_1 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Obj_Mask*ANIMATION*KEY*$Scale1*VALUE*Y SET "
						+ LT_Position_1 + "\0",print_writers);
				
				//ANIM IN
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Obj_Mask*ANIMATION*KEY*$Scale_In1*VALUE*Y SET "
						+ LT_Position_1 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Obj_Mask*ANIMATION*KEY*$Scale__Out1*VALUE*Y SET "
						+ LT_Position_1 + "\0",print_writers);
				
				//X
				//DYNAMIC
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos1*VALUE*X SET "
						+ LT_Flag_ObjectX + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale1*VALUE*X SET "
						+ LT_Flag_Keys + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos3*VALUE*X SET "
						+ LT_Flag_ObjectX + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale3*VALUE*X SET "
						+ LT_Flag_Keys + "\0",print_writers);
				
				//ANIM IN
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos_In1*VALUE*X SET "
						+ LT_Flag_ObjectX + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale_In1*VALUE*X SET "
						+ LT_Flag_Keys + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos_Out1*VALUE*X SET "
						+ LT_Flag_ObjectX + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale_Out1*VALUE*X SET "
						+ LT_Flag_Keys + "\0",print_writers);
				
				//Y
				//DYNAMIC
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos1*VALUE*Y SET "
						+ LT_Flag_ObjectY + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale1*VALUE*Y SET "
						+ LT_Flag_Keys + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos3*VALUE*Y SET "
						+ LT_Flag_ObjectY + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale3*VALUE*Y SET "
						+ LT_Flag_Keys + "\0",print_writers);
				
				//ANIM IN
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos_In1*VALUE*Y SET "
						+ LT_Flag_ObjectY + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale_Out1*VALUE*Y SET "
						+ LT_Flag_Keys + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos_In1*VALUE*Y SET "
						+ LT_Flag_ObjectY + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale_Out1*VALUE*Y SET "
						+ LT_Flag_Keys + "\0",print_writers);
			
				//Z
				//DYNAMIC
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos1*VALUE*Z SET "
						+ LT_Flag_ObjectZ + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale1*VALUE*Z SET "
						+ LT_Flag_Keys + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos3*VALUE*Z SET "
						+ LT_Flag_ObjectZ + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale3*VALUE*Z SET "
						+ LT_Flag_Keys + "\0",print_writers);
				
				
				//ANIM IN
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos_In1*VALUE*Z SET "
						+ LT_Flag_ObjectZ + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale_Out1*VALUE*Z SET "
						+ LT_Flag_Keys + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos_In1*VALUE*Z SET "
						+ LT_Flag_ObjectZ + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale_Out1*VALUE*Z SET "
						+ LT_Flag_Keys + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$Side" + WhichSide + "$Set_Side" + WhichSide + 
						"_Top_Position_Y*TRANSFORMATION*POSITION*Y SET " + LT_Position_2 + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$Side" + WhichSide + "$Set_Side" + WhichSide + 
						"_Top_Position_X*TRANSFORMATION*POSITION*X SET " + LT_Position_3 + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Sublines$Side" + WhichSide + "$Set_Rows_Side_" + WhichSide + 
						"_Position_Y*TRANSFORMATION*POSITION*Y SET " + LT_Position_4 + "\0",print_writers);
				
			}else if(WhichSide == 2) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Obj_Mask*ANIMATION*KEY*$Scale4*VALUE*Y SET "
						+ LT_Position_1 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Obj_Mask*ANIMATION*KEY*$Scale2*VALUE*Y SET "
						+ LT_Position_1 + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Obj_Mask*ANIMATION*KEY*$Scale_In1*VALUE*Y SET "
						+ LT_Position_1 + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Obj_Mask*ANIMATION*KEY*$Scale__Out1*VALUE*Y SET "
						+ LT_Position_1 + "\0",print_writers);
				
				//X
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos2*VALUE*X SET "
						+ LT_Flag_ObjectX + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale2*VALUE*X SET "
						+ LT_Flag_Keys + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos4*VALUE*X SET "
						+ LT_Flag_ObjectX + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale4*VALUE*X SET "
						+ LT_Flag_Keys + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos_In1*VALUE*X SET "
						+ LT_Flag_ObjectX + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale_In1*VALUE*X SET "
						+ LT_Flag_Keys + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos_Out1*VALUE*X SET "
						+ LT_Flag_ObjectX + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale_Out1*VALUE*X SET "
						+ LT_Flag_Keys + "\0",print_writers);
				
				//Y
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos2*VALUE*Y SET "
						+ LT_Flag_ObjectY + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale2*VALUE*Y SET "
						+ LT_Flag_Keys + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos4*VALUE*Y SET "
						+ LT_Flag_ObjectY + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale4*VALUE*Y SET "
						+ LT_Flag_Keys + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos_In1*VALUE*Y SET "
						+ LT_Flag_ObjectY + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale_Out1*VALUE*Y SET "
						+ LT_Flag_Keys + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos_In1*VALUE*Y SET "
						+ LT_Flag_ObjectY + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale_Out1*VALUE*Y SET "
						+ LT_Flag_Keys + "\0",print_writers);
				
				//Z
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos2*VALUE*Z SET "
						+ LT_Flag_ObjectZ + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale2*VALUE*Z SET "
						+ LT_Flag_Keys + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos4*VALUE*Z SET "
						+ LT_Flag_ObjectZ + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale4*VALUE*Z SET "
						+ LT_Flag_Keys + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos_In1*VALUE*Z SET "
						+ LT_Flag_ObjectZ + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale_Out1*VALUE*Z SET "
						+ LT_Flag_Keys + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Pos_In1*VALUE*Z SET "
						+ LT_Flag_ObjectZ + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Team_BadgeAll$LogoScale*ANIMATION*KEY*$Logo_Scale_Out1*VALUE*Z SET "
						+ LT_Flag_Keys + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$Side" + WhichSide + "$Set_Side" + WhichSide + 
						"_Top_Position_Y*TRANSFORMATION*POSITION*Y SET " + LT_Position_2 + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$Side" + WhichSide + "$Set_Side" + WhichSide + 
						"_Top_Position_X*TRANSFORMATION*POSITION*X SET " + LT_Position_3 + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Sublines$Side" + WhichSide + "$Set_Rows_Side_" + WhichSide + 
						"_Position_Y*TRANSFORMATION*POSITION*Y SET " + LT_Position_4 + "\0",print_writers);
			}
			break;
		}
		
	}
	
	public void setGriff(int griffSize,String whatToProcess,int WhichSide,Configuration config)
	{
		String [] TopTitle = null,TopStats = null,BottomTitle = null,BottomStats = null,Position=null;
		int numberOfSubLines = 0;
		
		if(griffSize <= 3) {
			numberOfSubLines = 2;
		}else {
			numberOfSubLines = 4;
		}
		
		switch (griffSize) {
		case 1:
			TopTitle = new String[1];
			TopStats = new String[1];
			Position = new String[] {"0.0"}; 
			if(whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F1")) {
				for(int i=0;i<=griffSize-1;i++) {
					TopTitle[i] = griff.get(i).getOpponentTeam().getTeamName1();
					if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
						if(griff.get(i).getHow_out() != null && !griff.get(i).getHow_out().trim().isEmpty() && 
								griff.get(i).getHow_out().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
							TopStats[i] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
						}else {
							TopStats[i] = "DNB";
						}
					}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
						TopStats[i] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
					}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						TopStats[i] = griff.get(i).getRuns() + "*," + griff.get(i).getBallsFaced();
					}else {
						TopStats[i] = "DNP";
					}
				}
			}else if(whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F2")){
				for(int i=0;i<=griffSize-1;i++) {
					TopTitle[i] = griff.get(i).getOpponentTeam().getTeamName1();
					if(griff.get(i).getStatus().equalsIgnoreCase("DNB")) {
						TopStats[i] = "DNB";
					}else if(griff.get(i).getStatus().equalsIgnoreCase("BALL")) {
						TopStats[i] = griff.get(i).getWickets() + "-" + griff.get(i).getRunsConceded() + "," + griff.get(i).getOversBowled();
					}else {
						TopStats[i] = "DNP";
					}
				}
			}
			break;
		case 2:
			TopTitle = new String[2];
			TopStats = new String[2];
			Position = new String[] {"-245.0","256.0"}; 
			
			if(whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F1")) {
				for(int i=0;i<=griffSize-1;i++) {
					TopTitle[i] = griff.get(i).getOpponentTeam().getTeamName1();
					if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
						if(griff.get(i).getHow_out() != null && !griff.get(i).getHow_out().trim().isEmpty() && griff.get(i).getHow_out().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
							TopStats[i] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
						}else {
							TopStats[i] = "DNB";
						}
					}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
						TopStats[i] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
					}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						TopStats[i] = griff.get(i).getRuns() + "*," + griff.get(i).getBallsFaced();
					}else {
						TopStats[i] = "DNP";
					}
				}
			}else if(whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F2")){
				for(int i=0;i<=griffSize-1;i++) {
					TopTitle[i] = griff.get(i).getOpponentTeam().getTeamName1();
					if(griff.get(i).getStatus().equalsIgnoreCase("DNB")) {
						TopStats[i] = "DNB";
					}else if(griff.get(i).getStatus().equalsIgnoreCase("BALL")) {
						TopStats[i] = griff.get(i).getWickets() + "-" + griff.get(i).getRunsConceded() + "," + griff.get(i).getOversBowled();
					}else {
						TopStats[i] = "DNP";
					}
				}
			}
			break;
		case 3:
			TopTitle = new String[3];
			TopStats = new String[3];
			Position = new String[] {"-365.0","20.0","400.0"}; 
			
			if(whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F1")) {
				for(int i=0;i<=griffSize-1;i++) {
					TopTitle[i] = griff.get(i).getOpponentTeam().getTeamName1();
					if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
						if(griff.get(i).getHow_out() != null && !griff.get(i).getHow_out().trim().isEmpty() && griff.get(i).getHow_out().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
							TopStats[i] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
						}else {
							TopStats[i] = "DNB";
						}
					}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
						TopStats[i] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
					}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						TopStats[i] = griff.get(i).getRuns() + "*," + griff.get(i).getBallsFaced();
					}else {
						TopStats[i] = "DNP";
					}
				}
			}else if(whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F2")){
				for(int i=0;i<=griffSize-1;i++) {
					TopTitle[i] = griff.get(i).getOpponentTeam().getTeamName1();
					if(griff.get(i).getStatus().equalsIgnoreCase("DNB")) {
						TopStats[i] = "DNB";
					}else if(griff.get(i).getStatus().equalsIgnoreCase("BALL")) {
						TopStats[i] = griff.get(i).getWickets() + "-" + griff.get(i).getRunsConceded() + "," + griff.get(i).getOversBowled();
					}else {
						TopStats[i] = "DNP";
					}
				}
			}
			break;
		case 4:
			TopTitle = new String[2];
			TopStats = new String[2];
			BottomTitle = new String[2];
			BottomStats = new String[2];
			Position = new String[] {"-245.0","256.0","-245.0","256.0"};
			
			if(whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F1")) {
				for(int i=0;i<=griffSize-1;i++) {
					if(i<2) {
						TopTitle[i] = griff.get(i).getOpponentTeam().getTeamName1();
						if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
							if(griff.get(i).getHow_out() != null && !griff.get(i).getHow_out().trim().isEmpty() && griff.get(i).getHow_out().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
								TopStats[i] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
							}else {
								TopStats[i] = "DNB";
							}
						}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
							TopStats[i] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
						}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
							TopStats[i] = griff.get(i).getRuns() + "*," + griff.get(i).getBallsFaced();
						}else {
							TopStats[i] = "DNP";
						}
					}else {
						BottomTitle[i-2] = griff.get(i).getOpponentTeam().getTeamName1();
						if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
							if(griff.get(i).getHow_out() != null && !griff.get(i).getHow_out().trim().isEmpty() && griff.get(i).getHow_out().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
								BottomStats[i-2] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
							}else {
								BottomStats[i-2] = "DNB";
							}
						}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
							BottomStats[i-2] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
						}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
							BottomStats[i-2] = griff.get(i).getRuns() + "*," + griff.get(i).getBallsFaced();
						}else {
							BottomStats[i-2] = "DNP";
						}
					}
				}
			}else if(whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F2")){
				for(int i=0;i<=griffSize-1;i++) {
					if(i<2) {
						TopTitle[i] = griff.get(i).getOpponentTeam().getTeamName1();
						if(griff.get(i).getStatus().equalsIgnoreCase("DNB")) {
							TopStats[i] = "DNB";
						}else if(griff.get(i).getStatus().equalsIgnoreCase("BALL")) {
							TopStats[i] = griff.get(i).getWickets() + "-" + griff.get(i).getRunsConceded() + "," + griff.get(i).getOversBowled();
						}else {
							TopStats[i] = "DNP";
						}
					}else {
						BottomTitle[i-2] = griff.get(i).getOpponentTeam().getTeamName1();
						if(griff.get(i).getStatus().equalsIgnoreCase("DNB")) {
							BottomStats[i-2] = "DNB";
						}else if(griff.get(i).getStatus().equalsIgnoreCase("BALL")) {
							BottomStats[i-2] = griff.get(i).getWickets() + "-" + griff.get(i).getRunsConceded() + "," + griff.get(i).getOversBowled();
						}else {
							BottomStats[i-2] = "DNP";
						}
					}
				}
			}
			break;
		case 5:
			TopTitle = new String[3];
			TopStats = new String[3];
			BottomTitle = new String[2];
			BottomStats = new String[2];
			Position = new String[] {"-365.0","20.0","400.0","-172.0","210.0"};
			
			if(whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F1")) {
				for(int i=0;i<=griffSize-1;i++) {
					if(i<3) {
						TopTitle[i] = griff.get(i).getOpponentTeam().getTeamName1();
						if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
							if(griff.get(i).getHow_out() != null && !griff.get(i).getHow_out().trim().isEmpty() && griff.get(i).getHow_out().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
								TopStats[i] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
							}else {
								TopStats[i] = "DNB";
							}
						}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
							TopStats[i] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
						}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
							TopStats[i] = griff.get(i).getRuns() + "*," + griff.get(i).getBallsFaced();
						}else {
							TopStats[i] = "DNP";
						}
					}else {
						BottomTitle[i-3] = griff.get(i).getOpponentTeam().getTeamName1();
						if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
							if(griff.get(i).getHow_out() != null && !griff.get(i).getHow_out().trim().isEmpty() && griff.get(i).getHow_out().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
								BottomStats[i-3] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
							}else {
								BottomStats[i-3] = "DNB";
							}
						}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
							BottomStats[i-3] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
						}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
							BottomStats[i-3] = griff.get(i).getRuns() + "*," + griff.get(i).getBallsFaced();
						}else {
							BottomStats[i-3] = "DNP";
						}
					}
				}
			}else if(whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F2")){
				for(int i=0;i<=griffSize-1;i++) {
					if(i<3) {
						TopTitle[i] = griff.get(i).getOpponentTeam().getTeamName1();
						if(griff.get(i).getStatus().equalsIgnoreCase("DNB")) {
							TopStats[i] = "DNB";
						}else if(griff.get(i).getStatus().equalsIgnoreCase("BALL")) {
							TopStats[i] = griff.get(i).getWickets() + "-" + griff.get(i).getRunsConceded() + "," + griff.get(i).getOversBowled();
						}else {
							TopStats[i] = "DNP";
						}
					}else {
						BottomTitle[i-3] = griff.get(i).getOpponentTeam().getTeamName1();
						if(griff.get(i).getStatus().equalsIgnoreCase("DNB")) {
							BottomStats[i-3] = "DNB";
						}else if(griff.get(i).getStatus().equalsIgnoreCase("BALL")) {
							BottomStats[i-3] = griff.get(i).getWickets() + "-" + griff.get(i).getRunsConceded() + "," + griff.get(i).getOversBowled();
						}else {
							BottomStats[i-3] = "DNP";
						}
					}
				}
			}
			break;
		case 6:
			TopTitle = new String[3];
			TopStats = new String[3];
			BottomTitle = new String[3];
			BottomStats = new String[3];
			Position = new String[] {"-365.0","20.0","400.0","-365.0","20.0","400.0"};
			
			if(whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F1")) {
				for(int i=0;i<=griffSize-1;i++) {
					if(i<3) {
						TopTitle[i] = griff.get(i).getOpponentTeam().getTeamName1();
						if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
							if(griff.get(i).getHow_out() != null && !griff.get(i).getHow_out().trim().isEmpty() && griff.get(i).getHow_out().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
								TopStats[i] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
							}else {
								TopStats[i] = "DNB";
							}
						}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
							TopStats[i] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
						}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
							TopStats[i] = griff.get(i).getRuns() + "*," + griff.get(i).getBallsFaced();
						}else {
							TopStats[i] = "DNP";
						}
					}else {
						BottomTitle[i-3] = griff.get(i).getOpponentTeam().getTeamName1();
						if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
							if(griff.get(i).getHow_out() != null && !griff.get(i).getHow_out().trim().isEmpty() && griff.get(i).getHow_out().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
								BottomStats[i-3] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
							}else {
								BottomStats[i-3] = "DNB";
							}
						}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
							BottomStats[i-3] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
						}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
							BottomStats[i-3] = griff.get(i).getRuns() + "*," + griff.get(i).getBallsFaced();
						}else {
							BottomStats[i-3] = "DNP";
						}
					}
				}
			}else if(whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F2")){
				for(int i=0;i<=griffSize-1;i++) {
					if(i<3) {
						TopTitle[i] = griff.get(i).getOpponentTeam().getTeamName1();
						if(griff.get(i).getStatus().equalsIgnoreCase("DNB")) {
							TopStats[i] = "DNB";
						}else if(griff.get(i).getStatus().equalsIgnoreCase("BALL")) {
							TopStats[i] = griff.get(i).getWickets() + "-" + griff.get(i).getRunsConceded() + "," + griff.get(i).getOversBowled();
						}else {
							TopStats[i] = "DNP";
						}
					}else {
						BottomTitle[i-3] = griff.get(i).getOpponentTeam().getTeamName1();
						if(griff.get(i).getStatus().equalsIgnoreCase("DNB")) {
							BottomStats[i-3] = "DNB";
						}else if(griff.get(i).getStatus().equalsIgnoreCase("BALL")) {
							BottomStats[i-3] = griff.get(i).getWickets() + "-" + griff.get(i).getRunsConceded() + "," + griff.get(i).getOversBowled();
						}else {
							BottomStats[i-3] = "DNP";
						}
					}
				}
			}
			break;
		case 7:
			TopTitle = new String[4];
			TopStats = new String[4];
			BottomTitle = new String[3];
			BottomStats = new String[3];
			Position = new String[] {"-428","-130","167.0","460.0","-283.0","22.0","323.0"};
			
			if(whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F1")) {
				for(int i=0;i<=griffSize-1;i++) {
					if(i<4) {
						TopTitle[i] = griff.get(i).getOpponentTeam().getTeamName1();
						if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
							if(griff.get(i).getHow_out() != null && !griff.get(i).getHow_out().trim().isEmpty() && griff.get(i).getHow_out().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
								TopStats[i] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
							}else {
								TopStats[i] = "DNB";
							}
						}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
							TopStats[i] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
						}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
							TopStats[i] = griff.get(i).getRuns() + "*," + griff.get(i).getBallsFaced();
						}else {
							TopStats[i] = "DNP";
						}
					}else {
						BottomTitle[i-4] = griff.get(i).getOpponentTeam().getTeamName1();
						if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
							if(griff.get(i).getHow_out() != null && !griff.get(i).getHow_out().trim().isEmpty() && griff.get(i).getHow_out().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
								BottomStats[i-4] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
							}else {
								BottomStats[i-4] = "DNB";
							}
						}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
							BottomStats[i-4] = griff.get(i).getRuns() + "," + griff.get(i).getBallsFaced();
						}else if(griff.get(i).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
							BottomStats[i-4] = griff.get(i).getRuns() + "*," + griff.get(i).getBallsFaced();
						}else {
							BottomStats[i-4] = "DNP";
						}
					}
				}
			}else if(whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F2")){
				for(int i=0;i<=griffSize-1;i++) {
					if(i<4) {
						TopTitle[i] = griff.get(i).getOpponentTeam().getTeamName1();
						if(griff.get(i).getStatus().equalsIgnoreCase("DNB")) {
							TopStats[i] = "DNB";
						}else if(griff.get(i).getStatus().equalsIgnoreCase("BALL")) {
							TopStats[i] = griff.get(i).getWickets() + "-" + griff.get(i).getRunsConceded() + "," + griff.get(i).getOversBowled();
						}else {
							TopStats[i] = "DNP";
						}
					}else {
						BottomTitle[i-4] = griff.get(i).getOpponentTeam().getTeamName1();
						if(griff.get(i).getStatus().equalsIgnoreCase("DNB")) {
							BottomStats[i-4] = "DNB";
						}else if(griff.get(i).getStatus().equalsIgnoreCase("BALL")) {
							BottomStats[i-4] = griff.get(i).getWickets() + "-" + griff.get(i).getRunsConceded() + "," + griff.get(i).getOversBowled();
						}else {
							BottomStats[i-4] = "DNP";
						}
					}
				}
			}
			break;
		}
		
		if(whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F1")){
			l3griff = new L3Griff("", player.getFull_name(), "", "", "", inning.getBatting_team().getTeamName4(), numberOfSubLines, 
					TopTitle, TopStats, BottomTitle, BottomStats, Position);
		}else {
			l3griff = new L3Griff("", player.getFull_name(), "", "", "", inning.getBowling_team().getTeamName4(), numberOfSubLines, 
					TopTitle, TopStats, BottomTitle, BottomStats, Position);
		}
	}

	public List<String> getAllOverData(int inning,List<Event> event, MatchAllData matchAllData) {
		
		String to = "";
		int runs = 0,wicket = 0,bowlerId = 0;
		boolean over_start = false,is_this_current_over = false;
		ArrayList<String> allData = new ArrayList<String>();
		if ((matchAllData.getEventFile().getEvents() != null) && (matchAllData.getEventFile().getEvents().size() > 0)) {
			for(Event evnt: matchAllData.getEventFile().getEvents()) {
				if(evnt.getEventInningNumber() == inning) {
					if(evnt.getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50)) {
						String lastElement = allData.get(allData.size() - 1);
				        lastElement = lastElement.concat("-CR");
				        allData.set(allData.size() - 1, lastElement);
					}
					
					if(evnt.getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
						to = "";
						if(evnt.getEventExtra().equalsIgnoreCase("TAPE")) {
							to = "-TO";
						}
						for(BowlingCard boc : matchAllData.getMatch().getInning().get(inning - 1).getBowlingCard()) {
							if(boc.getStatus().equalsIgnoreCase(CricketUtil.CURRENT + CricketUtil.BOWLER)) {
								bowlerId = boc.getPlayerId();
							}
						}
					}
					
					switch(evnt.getEventType()) {
					case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
	            	case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
	            		runs += evnt.getEventRuns();
	                    break;
	            	case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
	            		runs += evnt.getEventRuns();
	                    break;
	
	            	case CricketUtil.LOG_WICKET:
	                    if (evnt.getEventRuns() > 0)
	                    {
	                    	runs += evnt.getEventRuns();
	                    }
	                    wicket += 1;
	                    break;
	
	            	case CricketUtil.LOG_ANY_BALL:
	            		runs += evnt.getEventRuns();
	                    if (evnt.getEventExtra() != null)
	                    {
	                    	runs += evnt.getEventExtraRuns();
	                    }
	                    if (evnt.getEventSubExtra() != null)
	                    {
	                    	runs += evnt.getEventSubExtraRuns();
	                    }
	                    if(evnt.getEventHowOut() != null && !evnt.getEventHowOut().trim().isEmpty()) {
	                    	wicket += 1;
	                    }
	                    break;										
					}
					
					if(evnt.getEventType().equalsIgnoreCase(CricketUtil.END_OVER)) {
						if(is_this_current_over == true) {
							allData.set(allData.size()-1, runs + "-" + wicket + "-EO" + to);
						}else {
							allData.add(runs + "-" + wicket + "-EO" + to);
						}
						runs = 0;
						wicket = 0;
						over_start = false;
					}else if(evnt.getEventOverNo() == (matchAllData.getMatch().getInning().get(inning - 1).getTotalOvers())){
						if(!evnt.getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
							if(evnt.getEventBowlerNo() == bowlerId) {
//								System.out.println(evnt.getEventType());
								if(over_start == false) {
									allData.add(runs + "-" + wicket + "-CO" + to);
									over_start = true;
									is_this_current_over = true;
								}
								
								if(over_start == true) {
									String rw = runs + "-" + wicket;
									allData.set(allData.size()-1, runs + "-" + wicket + "-CO" + to);
								}
							}
						}
					}
				}
			}
		}
		System.out.println("allData = " + allData);
		return allData;
		
	}
}