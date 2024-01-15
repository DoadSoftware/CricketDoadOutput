package com.cricket.captions;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.cricket.containers.LowerThird;
import com.cricket.model.BattingCard;
import com.cricket.model.BestStats;
import com.cricket.model.BowlingCard;
import com.cricket.model.Configuration;
import com.cricket.model.DuckWorthLewis;
import com.cricket.model.Fixture;
import com.cricket.model.Ground;
import com.cricket.model.Inning;
import com.cricket.model.MatchAllData;
import com.cricket.model.NameSuper;
import com.cricket.model.Partnership;
import com.cricket.model.Player;
import com.cricket.model.Statistics;
import com.cricket.model.StatsType;
import com.cricket.model.Team;
import com.cricket.model.Tournament;
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
	public List<MatchAllData> tournament_matches;
	public List<NameSuper> nameSupers;
	public List<Fixture> fixTures;
	public List<Team> Teams;
	public List<Partnership> partnershipList;
	public List<Ground> Grounds;
	
	public BattingCard battingCard;
	public Partnership partnership;
	public BowlingCard bowlingCard;
	public Inning inning;
	public Player player;
	public Statistics stat;
	public StatsType statsType;
	public Tournament tournament;
	public LowerThird lowerThird;
	
	public NameSuper namesuper;
	public Fixture fixture;
	public Ground ground;
	public Team team;
	
	public List<DuckWorthLewis> dls;
	public List<BattingCard> battingCardList = new ArrayList<BattingCard>();
	List<BestStats> top_batsman_beststats = new ArrayList<BestStats>();
	List<BestStats> top_bowler_beststats = new ArrayList<BestStats>();
	public List<String> this_data_str = new ArrayList<String>();
	
	public List<Tournament> addPastDataToCurr;
	
	String containerName = "",ltWhichContainer = "",surName = "";
	public LowerThirdGfx() {
		super();
	}
	
	public LowerThirdGfx(List<PrintWriter> print_writers, Configuration config, List<Statistics> statistics,
			List<StatsType> statsTypes, List<MatchAllData> tournament_matches, List<NameSuper> nameSupers,List<Fixture> fixTures, 
			List<Team> Teams, List<Ground> Grounds, List<Tournament> tournaments,List<DuckWorthLewis> dls) {
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
		this.tournaments = tournaments;
		this.dls = dls;
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
			setPositionOfLT(whatToProcess,WhichSide,config);
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
		
		lowerThird = new LowerThird("", inning.getBatting_team().getTeamName1(), "","PROJECTED SCORES", String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), 
				"",2,"",inning.getBatting_team().getTeamName4(),new String[]{"CURRENT (" + this_data_str.get(0) + ")",this_data_str.get(2) + "/OVER"
				,this_data_str.get(4) + "/OVER",this_data_str.get(6) + "/OVER"},new String[]{this_data_str.get(1),this_data_str.get(3),this_data_str.get(5),this_data_str.get(7)},
				new String[]{"RATE","SCORE"},null,new String[] {"-253.0","33.0","277.0","533.0"});
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			//setStatsPositionOfLT(4, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
		
	}
	
	public String populateBattingStyle(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
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
		
		lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,"", null, null, 1,"",
			inning.getBatting_team().getTeamName4(),null,null,new String[]{CricketFunctions.getbattingstyle(battingCard.getPlayer().getBattingStyle(),
				CricketUtil.SHORT, true, false).toUpperCase()},null,null);
		
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config);
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return status;
		}
		
	}
	public String populateBowlingStyle(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
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
		
		lowerThird = new LowerThird("", player.getFirstname(), surName,"", null, null,
				1,"",team.getTeamName4(),null,null,new String[]{CricketFunctions.getbowlingstyle(player.getBowlingStyle()).toUpperCase()},
				new String[]{whatToProcess.split(",")[3]},null);
		
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config);
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return status;
		}
		
	}
	
	public String populateBatThisMatch(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String outOrNot = "",striktRate = "";
		
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
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
		
		if(battingCard.getStrikeRate().equalsIgnoreCase("0.0")) {
			striktRate = "-";
		}else {
			striktRate = battingCard.getStrikeRate();
		}
		String[] Count = CricketFunctions.getScoreTypeData(CricketUtil.BATSMAN,matchAllData, inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2]),
				"-", matchAllData.getEventFile().getEvents()).split("-");
		
		if(battingCard.getPlayer().getSurname() == null) {
			surName = "";
		}else {
			surName = battingCard.getPlayer().getSurname();
		}
		
		lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,outOrNot, String.valueOf(battingCard.getRuns()),
				"", 2, "",inning.getBatting_team().getTeamName4(),new String[] {"BALLS","DOTS","FOURS","SIXES","STRIKE RATE"},new String[] {String.valueOf(battingCard.getBalls()),
				Count[0],String.valueOf(battingCard.getFours()),String.valueOf(battingCard.getSixes()),striktRate},null,null,
				new String[] {"-517.0","-267.0","0.0","250.0","516.0"});
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(4, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config);
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
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			if(inning == null) {
				return "populateBallThisMatch: Current Inning NOT found in this match";
			}
			bowlingCard = inning.getBowlingCard().stream().filter(boc -> boc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			if(bowlingCard == null) {
				return status;
			}
		}
		
		if(bowlingCard.getEconomyRate().equalsIgnoreCase("0.0")) {
			economy = "-";
		}else {
			economy = String.valueOf(bowlingCard.getEconomyRate());
		}
		
		if(bowlingCard.getPlayer().getSurname() == null) {
			surName = "";
		}else {
			surName = bowlingCard.getPlayer().getSurname();
		}
		
		lowerThird = new LowerThird("", bowlingCard.getPlayer().getFirstname(), surName,"", "", "", 2, "", inning.getBowling_team().getTeamName4(),
				new String[] {"OVERS", "MAIDENS", "RUNS", "WICKETS", "ECONOMY"},new String[]{CricketFunctions.OverBalls(bowlingCard.getOvers(), bowlingCard.getBalls()), 
				String.valueOf(bowlingCard.getMaidens()),String.valueOf(bowlingCard.getRuns()),String.valueOf(bowlingCard.getWickets()), economy}
				,null,null,new String[] {"-517.0","-267.0","0.0","250.0","516.0"});
		
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(5, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config);
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
			lowerThird = new LowerThird("FALL OF WICKET", inning.getBatting_team().getTeamName1(), "","", String.valueOf(inning.getTotalRuns()), String.valueOf(inning.getTotalWickets()),
				2,"",inning.getBatting_team().getTeamName4(),fowNumber,fowData,new String[]{"WICKET","SCORE"},null,
				new String[] {"-400.0","-285.0","-165.0","-45.0","70.0","170.0","270.0","370.0","470.0","570.0"});
			
			status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
			if(status == Constants.OK) {
				HideAndShowL3rdSubStrapContainers(WhichSide);
//				setStatsPositionOfLT(inning.getTotalWickets(), 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
				setPositionOfLT(whatToProcess,WhichSide,config);
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
		
		if (inning.getBowlingTeamId() == matchAllData.getSetup().getHomeTeamId() && Integer.valueOf(whatToProcess.split(",")[2]) == 50 || 
				inning.getBowlingTeamId() == matchAllData.getSetup().getHomeTeamId() && Integer.valueOf(whatToProcess.split(",")[2]) == 100) {
			
			for(int split_id=0;split_id<CricketFunctions.getSplit(Integer.valueOf(whatToProcess.split(",")[1]), Integer.valueOf(whatToProcess.split(",")[2]),
					matchAllData,matchAllData.getEventFile().getEvents()).size();split_id++) {
				splitData[split_id] = CricketFunctions.getSplit(Integer.valueOf(whatToProcess.split(",")[1]), Integer.valueOf(whatToProcess.split(",")[2])
						,matchAllData,matchAllData.getEventFile().getEvents()).get(split_id);
				splitNumber[split_id] = String.valueOf(split_id+1);
	    	}
			
			if(Integer.valueOf(whatToProcess.split(",")[2]) == 30) {
				whichSplit = "BALLS PER FIFTY";
				lowerThird = new LowerThird("", matchAllData.getSetup().getAwayTeam().getTeamName1(), "",whichSplit, String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), "",
						2,"",matchAllData.getSetup().getAwayTeam().getTeamName4(),splitNumber,splitData,new String[]{"FIFTIES","BALLS"},null,
						new String[] {"-400.0","-285.0","-165.0","-45.0","70.0","170.0","270.0","370.0","470.0","570.0"});
			}else {
				whichSplit = "BALLS PER HUNDRED";
				lowerThird = new LowerThird("", matchAllData.getSetup().getAwayTeam().getTeamName1(), "",whichSplit, String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), "",
						2,"",matchAllData.getSetup().getAwayTeam().getTeamName4(),splitNumber,splitData,new String[]{"HUNDREDS","BALLS"},null,
						new String[] {"-323.0","-178.0","-41.0","86.0","228.0","362.0","486.0","586.0","586.0","586.0"});
			}
		}else {
			
			for(int split_id=0;split_id<CricketFunctions.getSplit(Integer.valueOf(whatToProcess.split(",")[1]), Integer.valueOf(whatToProcess.split(",")[2]),
					matchAllData,matchAllData.getEventFile().getEvents()).size();split_id++) {
				splitData[split_id] = CricketFunctions.getSplit(Integer.valueOf(whatToProcess.split(",")[1]), Integer.valueOf(whatToProcess.split(",")[2])
						,matchAllData,matchAllData.getEventFile().getEvents()).get(split_id);
				splitNumber[split_id] = String.valueOf(split_id+1);
			}
			
			if(Integer.valueOf(whatToProcess.split(",")[2]) == 50) {
				whichSplit = "BALLS PER FIFTY";
				lowerThird = new LowerThird("", matchAllData.getSetup().getHomeTeam().getTeamName1(), "",whichSplit, String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), "",
						2,"",matchAllData.getSetup().getHomeTeam().getTeamName4(),splitNumber,splitData,new String[]{"FIFTIES","BALLS"},null,
						new String[] {"-400.0","-285.0","-165.0","-45.0","70.0","170.0","270.0","370.0","470.0","570.0"});
			}else {
				whichSplit = "BALLS PER HUNDRED";
				lowerThird = new LowerThird("", matchAllData.getSetup().getHomeTeam().getTeamName1(), "",whichSplit, String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets()), "",
						2,"",matchAllData.getSetup().getHomeTeam().getTeamName4(),splitNumber,splitData,new String[]{"HUNDREDS","BALLS"},null,
						new String[] {"-323.0","-178.0","-41.0","86.0","228.0","362.0","486.0","586.0","586.0","586.0"});
			}
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(splitSize, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	public String populateL3rdTarget(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES))
			.findAny().orElse(null);
		
		if(inning == null) {
			return "populateTarget: Current Inning NOT found in this match";
		}
			
		int requiredRuns = matchAllData.getMatch().getInning().get(0).getTotalRuns() + 1;
		String runRate = "";
		if(matchAllData.getSetup().getTargetRuns() != 0) {
			requiredRuns = matchAllData.getSetup().getTargetRuns();
		}
		
		if(requiredRuns <= 0) {
			requiredRuns = 0;
		}
		int requiredBalls = 0;
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
		
		String summary=inning.getBatting_team().getTeamName1()+" NEED "+(matchAllData.getMatch().getInning().get(0).getTotalRuns()+1)+" RUN"+
				CricketFunctions.Plural(matchAllData.getMatch().getInning().get(0).getTotalRuns()-inning.getTotalRuns()).toUpperCase()+" TO WIN ";
//			String summary=inning.getBatting_team().getTeamName1()+" NEED "+(matchAllData.getMatch().getInning().get(0).getTotalRuns()-inning.getTotalRuns())+" RUN"+CricketFunctions.Plural(matchAllData.getMatch().getInning().get(0).getTotalRuns()-inning.getTotalRuns()).toUpperCase()+" TO WIN ";
		runRate = " AT " + CricketFunctions.generateRunRate(requiredRuns, 0, requiredBalls, 2,matchAllData) + " RUNS PER OVER";
		
		if(matchAllData.getSetup().getTargetOvers() == "" || matchAllData.getSetup().getTargetOvers().trim().isEmpty() && 
				matchAllData.getSetup().getTargetRuns() == 0) {
			
			lowerThird = new LowerThird(summary, "", "","", "", "",1,"",inning.getBatting_team().getTeamName4(),null,null,
					new String[]{inning.getBatting_team().getTeamName1(),String.valueOf(CricketFunctions.getTargetRuns(matchAllData)),
							String.valueOf("FROM "+matchAllData.getSetup().getMaxOvers())+ " OVERS",runRate},null,null);
		}else {
			if(matchAllData.getSetup().getTargetOvers() != "") {
				
				lowerThird = new LowerThird(summary, "", "","", "", "",1,"",inning.getBatting_team().getTeamName4(),null,null,
						new String[]{inning.getBatting_team().getTeamName1(),String.valueOf(CricketFunctions.getTargetRuns(matchAllData)),
								String.valueOf("FROM "+matchAllData.getSetup().getTargetOvers())+ " OVERS",runRate},null,null);
			}
			if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("VJD")) {
				
				lowerThird = new LowerThird(summary, "", "","", "", "",1,"",inning.getBatting_team().getTeamName4(),null,null,
						new String[]{inning.getBatting_team().getTeamName1(),String.valueOf(CricketFunctions.getTargetRuns(matchAllData)),
								String.valueOf("FROM "+matchAllData.getSetup().getTargetOvers())+ " OVERS (VJD)",runRate},null,null);
				
			}else if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("DLS")) {

				lowerThird = new LowerThird(summary, "", "","", "", "",1,"",inning.getBatting_team().getTeamName4(),null,null,
						new String[]{inning.getBatting_team().getTeamName1(),String.valueOf(CricketFunctions.getTargetRuns(matchAllData)),
								String.valueOf("FROM "+matchAllData.getSetup().getTargetOvers())+ " OVERS (DLS)",runRate},null,null);
			}
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config);
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
		
		int requiredBalls = 0;
		requiredBalls = ((matchAllData.getSetup().getReducedOvers()) * 6) - (inning.getTotalOvers()*6 + inning.getTotalBalls());
		if(requiredBalls <= 0) {
			requiredBalls = 0;
		}
		String line_1 = "";
		if(requiredBalls < 100) {
			 line_1 = "FROM " + requiredBalls + " BALL" + CricketFunctions.Plural(requiredBalls).toUpperCase() + " AT " +
		CricketFunctions.generateRunRate((matchAllData.getMatch().getInning().get(0).getTotalRuns() - inning.getTotalRuns()), 0, requiredBalls, 2,matchAllData) + " RUNS PER OVER ";

		}else {
			if(requiredBalls%6 == 0) {
				 line_1 = "FROM " + requiredBalls/6 + " OVER" + CricketFunctions.Plural(requiredBalls/6).toUpperCase()
				 + " AT " + CricketFunctions.generateRunRate((matchAllData.getMatch().getInning().get(0).getTotalRuns() - inning.getTotalRuns()), 0, requiredBalls, 2,matchAllData) + " RUNS PER OVER ";

			}else {
				line_1 = "FROM " + requiredBalls/6 + "." + requiredBalls%6 + " OVER" + CricketFunctions.Plural(requiredBalls/6).toUpperCase()
				+ " AT " + CricketFunctions.generateRunRate((matchAllData.getMatch().getInning().get(0).getTotalRuns()-inning.getTotalRuns()), 0, requiredBalls, 2,matchAllData) + " RUNS PER OVER ";
			}
		}
		String summary=inning.getBatting_team().getTeamName1() + " NEED " + (matchAllData.getMatch().getInning().get(0).getTotalRuns()-inning.getTotalRuns()+1) + 
				" MORE " + "RUN" + CricketFunctions.Plural((matchAllData.getMatch().getInning().get(0).getTotalRuns()-inning.getTotalRuns()+1)).toUpperCase() + " TO WIN ";
//			String summary = inning.getBatting_team().getTeamName1() + " NEED " + (matchAllData.getMatch().getInning().get(0).getTotalRuns() - inning.getTotalRuns()) + " MORE RUNS TO WIN ";
		String line_2 = "WITH " + (10-inning.getTotalWickets()) + " WICKET" + CricketFunctions.Plural(10 - inning.getTotalWickets()).toUpperCase() + " REMAINING";

		lowerThird = new LowerThird(summary, "", "","", "", "",2,"",inning.getBatting_team().getTeamName4(),null,null,
				new String[]{line_1,line_2},null,null);
			
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config);
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
		
		lowerThird = new LowerThird("", player.getFirstname(), surName,team.getTeamName1(), "", "", 1, "",team.getTeamName4(),
				null,null,new String[]{whatToProcess.split(",")[3].toUpperCase()},null,null);
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
//			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config);
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
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
//			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config);
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
//			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config);
			return Constants.OK;
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
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
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
		
		if(battingCard.getStrikeRate().equalsIgnoreCase("0.0")) {
			striktRate = "-";
		}else {
			striktRate = battingCard.getStrikeRate();
		}
		
		if(battingCard.getPlayer().getSurname() == null) {
			surName = "";
		}else {
			surName = battingCard.getPlayer().getSurname();
		}
		
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
		
		lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,"", 
				String.valueOf(battingCard.getRuns()), String.valueOf(battingCard.getBalls()),2,"",inning.getBatting_team().getTeamName4(),
				null,null,new String[]{howOut,String.valueOf(battingCard.getFours()),String.valueOf(battingCard.getSixes()),Count[0],striktRate},
				null,null);
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config);
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
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
			if(inning == null) {
				return "populateHowOutWithOutFielder: Inning is Not Found";
			}
			battingCard = inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			if(battingCard == null) {
				return status;
			}
		}
		
		if(battingCard.getStrikeRate().equalsIgnoreCase("0.0")) {
			striktRate = "-";
		}else {
			striktRate = battingCard.getStrikeRate();
		}
		
		if(battingCard.getPlayer().getSurname() == null) {
			surName = "";
		}else {
			surName = battingCard.getPlayer().getSurname();
		}
		
		String[] Count = CricketFunctions.getScoreTypeData(CricketUtil.BATSMAN,matchAllData, inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2]),
				"-", matchAllData.getEventFile().getEvents()).split("-");
		
		lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,"", 
				String.valueOf(battingCard.getRuns()), String.valueOf(battingCard.getBalls() + 1),1,"",inning.getBatting_team().getTeamName4(),
				null,null,new String[]{String.valueOf(battingCard.getFours()),String.valueOf(battingCard.getSixes()),Count[0],striktRate},
				new String[]{striktRate},null);
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config);
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateQuickHowOut(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String striktRate = "",howOut = "";
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
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Last_Wicket_Wide$Top$"
				+ "txt_Score*GEOM*TEXT SET " + battingCardList.get(battingCardList.size()-1).getRuns() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Last_Wicket_Wide$Top$"
				+ "txt_Balls*GEOM*TEXT SET " + "(" + battingCardList.get(battingCardList.size()-1).getBalls() + ")" + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ "$Last_Wicket_Wide$txt_HowOut*GEOM*TEXT SET " + battingCardList.get(battingCardList.size()-1).getHowOutText() + "\0", print_writers);
		}
		
		if(battingCardList.get(battingCardList.size()-1).getStrikeRate().equalsIgnoreCase("0.0")) {
			striktRate = "-";
		}else {
			striktRate = battingCardList.get(battingCardList.size()-1).getStrikeRate();
		}
		
		if(battingCardList.get(battingCardList.size()-1).getPlayer().getSurname() == null) {
			surName = "";
		}else {
			surName = battingCardList.get(battingCardList.size()-1).getPlayer().getSurname();
		}
		
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
		
		String[] Count = CricketFunctions.getScoreTypeData(CricketUtil.BATSMAN,matchAllData, inning.getInningNumber(), battingCardList.get(battingCardList.size()-1).getPlayerId(),
				"-", matchAllData.getEventFile().getEvents()).split("-");
		
		
		lowerThird = new LowerThird("", battingCardList.get(battingCardList.size()-1).getPlayer().getFirstname(), surName,"", 
				String.valueOf(battingCardList.get(battingCardList.size()-1).getRuns()), 
				String.valueOf(battingCardList.get(battingCardList.size()-1).getBalls()),2,"",inning.getBatting_team().getTeamName4(),
				null,null,new String[]{howOut,String.valueOf(battingCardList.get(battingCardList.size()-1).getFours()),
				String.valueOf(battingCardList.get(battingCardList.size()-1).getSixes()),Count[0],striktRate},
				null,null);
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config);
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateL3rdThisSeries(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException, IOException
	{
		String teamName = "",best = "",economy = "",bowler_sr = "",bat_sr = "";
		int k =0;
		double economy_rate=0;
		
		addPastDataToCurr = CricketFunctions.extractTournamentStats("CURRENT_MATCH_DATA", false, tournament_matches, null, matchAllData, tournaments);
		
		if(addPastDataToCurr == null) {
			
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
							best = String.valueOf(top_batsman_beststats.get(j).getBestEquation()/2);
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
					tournament.getBallsFaced(), 1).equalsIgnoreCase("0.0")) {
				bat_sr = "-"; 
			}else {
				bat_sr = CricketFunctions.generateStrikeRate(tournament.getRuns(), 
						tournament.getBallsFaced(), 1);
			}
			
			lowerThird = new LowerThird("", tournament.getPlayer().getFirstname(), surName,"ICC U19 CWC 2024", "", "", 2,"",teamName,
					new String[]{"MATCHES", "RUNS", "FIFTIES", "HUNDREDS", "STRIKE RATE", "BEST"},new String[]{String.valueOf(tournament.getMatches()), 
					String.format("%,d\n", tournament.getRuns()), String.valueOf(tournament.getFifty()),String.valueOf(tournament.getHundreds()),bat_sr,best},null,null,
					new String[] {"-503.0","-315.0","-116.0","115.0","350.0","560.0"});
			
		}else if(whatToProcess.split(",")[0].equalsIgnoreCase("Control_f")) {
			
			for(int j=0;j<= top_bowler_beststats.size()-1;j++) {
				if(top_bowler_beststats.get(j).getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])) {
					if(k == 0) {
						k += 1;
						if(top_bowler_beststats.get(j).getBestEquation() % 1000 > 0) {
							best = ((top_bowler_beststats.get(j).getBestEquation() / 1000) +1) + "-" + (1000 - (top_bowler_beststats.get(j).getBestEquation() % 1000));
						}
						else if(top_bowler_beststats.get(j).getBestEquation() % 1000 < 0) {
							best = (top_bowler_beststats.get(j).getBestEquation() / 1000) + "-" + Math.abs(top_bowler_beststats.get(j).getBestEquation());
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
			
			DecimalFormat df_s = new DecimalFormat("0.00");
			if(tournament.getWickets() == 0 || tournament.getBallsBowled() == 0) {
				bowler_sr = "-";
			}else {
				bowler_sr = df_s.format(Double.valueOf(tournament.getBallsBowled())/tournament.getWickets());
			}
			
			lowerThird = new LowerThird("", tournament.getPlayer().getFirstname(), surName,"ICC U19 CWC 2024", "", "", 2,"",teamName,
					new String[]{"MATCHES", "RUNS", "WICKETS", "STRIKE RATE", "ECONOMY", "BEST"},new String[]{String.valueOf(tournament.getMatches()), String.format("%,d\n", tournament.getRuns()),
					String.valueOf(tournament.getWickets()),bowler_sr,economy,best},null,null,new String[] {"-503.0","-315.0","-116.0","115.0","350.0","560.0"});
			
		}
//		System.out.println(tournament.getPlayer().getFull_name() + " - " + tournament.getMatches() + " - " + tournament.getRuns());
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config);
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
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
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
			setPositionOfLT(whatToProcess,WhichSide,config);
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
			setPositionOfLT(whatToProcess,WhichSide,config);
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populateBatSummary(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return status;
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
			if(inning == null) {
				return "populateBatSummary: Inning is Not Found";
			}
			
			battingCard = inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			if(battingCard == null) {
				return status;
			}
		}
		
		String[] Count = CricketFunctions.getScoreTypeData(CricketUtil.BATSMAN,matchAllData, inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2]),
				"-", matchAllData.getEventFile().getEvents()).split("-");
		
		if(battingCard.getPlayer().getSurname() == null) {
			surName = "";
		}else {
			surName = battingCard.getPlayer().getSurname();
		}
		
		lowerThird = new LowerThird("", battingCard.getPlayer().getFirstname(), surName,"", String.valueOf(battingCard.getRuns()), 
				String.valueOf(battingCard.getBalls()), 2, "", inning.getBatting_team().getTeamName4(),new String[] {"DOTS", "ONES", "TWOS", "THREES", "FOURS", "SIXES"},
				new String[]{Count[0],Count[1],Count[2],Count[3],Count[4],Count[6]},null,null,new String[] {"-530.0","-328.0","-122.0","115.0","344.0","560.0"});
		
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(5, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config);
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
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
			if(inning == null) {
				return "populateBallSummary: Inning is Not Found";
			}
			
			bowlingCard = inning.getBowlingCard().stream().filter(boc -> boc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			if(bowlingCard == null) {
				return status;
			}
		}
		
		String[] Count = CricketFunctions.getScoreTypeData(CricketUtil.BOWLER,matchAllData, inning.getInningNumber(), Integer.valueOf(whatToProcess.split(",")[2]),
				"-", matchAllData.getEventFile().getEvents()).split("-");
		
		if(bowlingCard.getPlayer().getSurname() == null) {
			surName = "";
		}else {
			surName = bowlingCard.getPlayer().getSurname();
		}
		
		if(inning.getTotalOvers() == 0 || inning.getTotalOvers() > 1) {
			over_text = "OVER" + CricketFunctions.Plural(inning.getTotalOvers()).toUpperCase();
		}else {
			if(inning.getTotalBalls() == 1) {
				over_text = "OVERS";
			}
			else if(inning.getTotalBalls() > 0) {
				over_text = "OVER" + CricketFunctions.Plural(inning.getTotalBalls()).toUpperCase();
			}
			else {
				over_text = "OVER";
			}
		}
		
		lowerThird = new LowerThird("", bowlingCard.getPlayer().getFirstname(), surName,over_text, String.valueOf(bowlingCard.getWickets()) + "-" + String.valueOf(bowlingCard.getRuns()), 
				String.valueOf(CricketFunctions.OverBalls(bowlingCard.getOvers(), bowlingCard.getBalls())), 2, "", inning.getBowling_team().getTeamName4(),new String[] {"DOTS", "ONES", "TWOS", "THREES", "FOURS", "SIXES"},
				new String[]{Count[0],Count[1],Count[2],Count[3],Count[4],Count[6]},null,null,new String[] {"-530.0","-328.0","-122.0","115.0","344.0","560.0"});
		
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(5, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config);
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
		
		lowerThird = new LowerThird(inning.getBatting_team().getTeamName1(), "", "","", String.valueOf(inning.getTotalRuns()) + "-" + String.valueOf(inning.getTotalWickets()), 
				CricketFunctions.OverBalls(inning.getTotalOvers(), inning.getTotalBalls()), 2, "", inning.getBatting_team().getTeamName4(),
				new String[] {"DOTS", "ONES", "TWOS", "THREES", "FOURS", "SIXES"},new String[]{Count[0],Count[1],Count[2],Count[3],String.valueOf(inning.getTotalFours()),
				String.valueOf(inning.getTotalSixes())},null,null,new String[] {"-530.0","-328.0","-122.0","115.0","344.0","560.0"});
		
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(5, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config);
//			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 4,print_writers, config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
		}
	}
	
	public String populatePowerplay(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		String powerplay = "",Subheader = "", Subline = "";
		
		if(whatToProcess.split(",")[2].equalsIgnoreCase("p1")) {
			powerplay = "POWERPLAY 1";
			Subheader = "(OVERS 1 TO 10)";
			Subline = "NO MORE THAN 2 FIELDERS ALLOWED OUTSIDE 30-YARD CIRCLE";
		}else if(whatToProcess.split(",")[2].equalsIgnoreCase("p2")) {
			powerplay = "POWERPLAY 2";
			Subheader = "(OVERS 11 TO 40)";
			Subline = "NO MORE THAN 4 FIELDERS ALLOWED OUTSIDE 30-YARD CIRCLE";
		}else if(whatToProcess.split(",")[2].equalsIgnoreCase("p3")) {
			powerplay = "POWERPLAY 3";
			Subheader = "(OVERS 41 TO 50)";
			Subline = "NO MORE THAN 5 FIELDERS ALLOWED OUTSIDE 30-YARD CIRCLE";
		}
		lowerThird = new LowerThird(powerplay, matchAllData.getSetup().getHomeTeam().getTeamName4(), 
				matchAllData.getSetup().getAwayTeam().getTeamName4(),Subheader, "", "", 1, "FLAG" ,"",
				null,null,new String[]{Subline},null,null);
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(5, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config);
//			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 4,print_writers, config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return status;
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
			String pp_ovrs="";String pp="";
			
			if(whatToProcess.split(",")[2].equalsIgnoreCase("p1")) {
				powerplay = "POWERPLAY 1";
				pp_ovrs= CricketFunctions.PowerPlayMatchOvers(matchAllData,",").split(",")[0];
				pp=CricketFunctions.getFirstPowerPlayScore(matchAllData,inning.getInningNumber(), matchAllData.getEventFile().getEvents());

			}else if(whatToProcess.split(",")[2].equalsIgnoreCase("p2")) {
				powerplay = "POWERPLAY 2";
				pp_ovrs= CricketFunctions.PowerPlayMatchOvers(matchAllData,",").split(",")[1];
				pp=CricketFunctions.getSecPowerPlayScore(matchAllData, inning.getInningNumber(), matchAllData.getEventFile().getEvents());
			}else if(whatToProcess.split(",")[2].equalsIgnoreCase("p3")) {
				powerplay = "POWERPLAY 3";
				pp_ovrs= CricketFunctions.PowerPlayMatchOvers(matchAllData,",").split(",")[2];
				pp=CricketFunctions.getThirdPowerPlayScore(matchAllData, inning.getInningNumber(), matchAllData.getEventFile().getEvents());
			} 
			lowerThird = new LowerThird(powerplay,inning.getBatting_team().getTeamName1(), "",whatToProcess.split(",")[2], "","", 
					2, "" ,inning.getBatting_team().getTeamName4(),new String[]{"RUNS","WICKETS","RUN RATE","DOTS","FOURS","SIXES"},
					new String[]{pp.split(",")[0].split("-")[0],pp.split(",")[0].split("-")[1],
					CricketFunctions.generateRunRates(Integer.valueOf(pp.split(",")[0].split("-")[0]), Integer.valueOf(pp_ovrs.split("-")[0]), 
					Integer.valueOf(pp_ovrs.split("-")[1]), 2,matchAllData),pp.split(",")[3],pp.split(",")[1],pp.split(",")[2]},null,
					null,new String[] {"-517.0","-328.0","-122.0","115.0","344.0","560.0"});
			
				status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
				if(status == Constants.OK) {
					HideAndShowL3rdSubStrapContainers(WhichSide);
					setPositionOfLT(whatToProcess,WhichSide,config);
					return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
				} else {
					return status;
				}
			}
	}

	public String populateDlsTarget(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
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
		
		for(int i = 0; i<= dls.size() -1;i++) {
			if(dls.get(i).getOver_left().split("\\.")[0].equalsIgnoreCase(String.valueOf(inning.getTotalOvers()))) {
				for(int j=1;j<=6;j++) {
					if(inning.getTotalBalls() == j) {
//						this_data_str.add(CricketFunctions.populateDuckWorthLewis(matchAllData).get(i+j).getOver_left());
						this_data_str.add(CricketFunctions.populateDuckWorthLewis(matchAllData).get(i+j).getWkts_down());
						break;
					}
					
				}
				break;
			}
		}
		
		if(CricketFunctions.populateDls(matchAllData,dls).trim().isEmpty()) {
			return "error";
		}
		
		this_data_str.add(CricketFunctions.populateDls(matchAllData,dls));
		
		if(this_data_str == null) {
			return "error";
		}
		
		lowerThird = new LowerThird("DLS PAR SCORE", "", "","AFTER ", String.valueOf(CricketFunctions.OverBalls(inning.getTotalOvers(), inning.getTotalBalls())), 
				"",2,"","", null,null,new String[]{this_data_str.get(0),this_data_str.get(1)},null,null);
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(5, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config);
//			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 4,print_writers, config);
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
		summary=inning.getBatting_team().getTeamName1()+" NEED "+(matchAllData.getMatch().getInning().get(0).getTotalRuns()-inning.getTotalRuns())+" MORE RUNS TO WIN AT "+
				CricketFunctions.generateRunRate(requiredRuns, 0, requiredBalls, 2,matchAllData)+" RUNS PER OVER";
		if(inning != null) {
			
			lowerThird = new LowerThird(matchAllData.getSetup().getHomeTeam().getTeamGroup() + ", " + ground.getCity(), inning.getBowling_team().getTeamName4(),inning.getBatting_team().getTeamName4(),"", "", "",2,"Emirates","",null,null,
					new String[]{inning.getBowling_team().getTeamName1()+"  "+run_wicket+"  ("+
			CricketFunctions.OverBalls( matchAllData.getMatch().getInning().get(0).getTotalOvers(), matchAllData.getMatch().getInning().get(0).getTotalBalls())+")    ",inning.getBatting_team().getTeamName1()+"  ",
			String.valueOf(inning.getTotalRuns()+"-"+inning.getTotalWickets()+"  "+"("+
					CricketFunctions.OverBalls(inning.getTotalOvers(),inning.getTotalBalls())+") ")},new String[]{summary},null);
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(whatToProcess,WhichSide,config);
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
			
			lowerThird = new LowerThird("AFTER",  matchAllData.getSetup().getHomeTeam().getTeamName4(), 
					matchAllData.getSetup().getAwayTeam().getTeamName4(),"", "",CricketFunctions.OverBalls(inning.getTotalOvers(), inning.getTotalBalls()), 
					2, "FLAG" ,"",null,null,new String[]{inning.getBowling_team().getTeamName1(), 
					" WERE ",  String.valueOf(in_data.split(",")[0] + "-" + in_data.split(",")[1]),inning.getBatting_team().getTeamName1(), 
					"ARE", String.valueOf(inning.getTotalRuns() + "-" + inning.getTotalWickets())},new String[]{"FOURS",
					"SIXES ", String.valueOf(in_data.split(",")[3]),  String.valueOf(in_data.split(",")[2]),
					String.valueOf(inning.getTotalFours()), String.valueOf(inning.getTotalSixes())},new String[] {"-13.0"});
			
			status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
			if(status == Constants.OK) {
				HideAndShowL3rdSubStrapContainers(WhichSide);
				setPositionOfLT(whatToProcess,WhichSide,config);
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
		
		stat = statistics.stream().filter(st -> st.getPlayer_id() == FirstPlayerId).findAny().orElse(null);
		
		if(stat == null) {
			return "PopulateL3rdPlayerProfile: Stats not found for Player Id [" + FirstPlayerId + "]";
		}
		
		statsType = statsTypes.stream().filter(st -> st.getStats_short_name().equalsIgnoreCase(WhichProfile)).findAny().orElse(null);
		if(statsType == null) {
			return "PopulateL3rdPlayerProfile: Stats Type not found for profile [" + WhichProfile + "]";
		}
		
		player = CricketFunctions.getPlayerFromMatchData(stat.getPlayer_id(), matchAllData); 
		
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
		
		stat.setStats_type(statsType);
		
		stat = CricketFunctions.updateTournamentDataWithStats(stat, tournament_matches, matchAllData);
		stat = CricketFunctions.updateStatisticsWithMatchData(stat, matchAllData);
		
		double average = 0;
		String Data = "",hundred = "",fifty = "",strikeRate = "",batAverage = "",economy = "";
		
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
					stat.getBalls_faced(), 1).equalsIgnoreCase("0.0")) {
				strikeRate = "-";
			}else {
				strikeRate = CricketFunctions.generateStrikeRate(stat.getRuns(), 
						stat.getBalls_faced(), 1);
			}
			
			if(CricketFunctions.getAverage(stat.getMatches(), stat.getNot_out(), stat.getRuns(), 2, "-").equalsIgnoreCase("0.0")) {
				batAverage = "-";
			}else {
				batAverage = CricketFunctions.getAverage(stat.getMatches(), stat.getNot_out(), stat.getRuns(), 2, "-");
			}
			
			if(player.getSurname() == null) {
				surName = "";
			}else {
				surName = player.getSurname();
			}
			
			lowerThird = new LowerThird("", player.getFirstname(), surName,statsType.getStats_full_name(), "", "", 2,"",team.getTeamName4(),
					new String[]{"MATCHES", "RUNS", "AVERAGE", "FIFTIES", "HUNDREDS", "BEST", "STRIKE RATE"},
					new String[]{String.valueOf(stat.getMatches()), String.format("%,d\n", stat.getRuns()),
					batAverage ,fifty, hundred, stat.getBest_score(),strikeRate},null,null,
					new String[] {"-503.0","-335.0","-173.0","-3.0","173.0","340.0","516.0"});
		}
		else if(whatToProcess.split(",")[0].equalsIgnoreCase("F11")) {
			if(CricketFunctions.getEconomy(stat.getRuns_conceded(), stat.getBalls_bowled(),2,"-").equalsIgnoreCase("0.0")) {
				economy = "-";
			}else {
				economy = CricketFunctions.getEconomy(stat.getRuns_conceded(), stat.getBalls_bowled(),2,"-");
			}
			
			if(player.getSurname() == null) {
				surName = "";
			}else {
				surName = player.getSurname();
			}
			
			lowerThird = new LowerThird("", player.getFirstname(), surName,statsType.getStats_full_name(), "", "", 2,"",team.getTeamName4(),
					new String[]{"MATCHES", "WICKETS", "AVERAGE", "ECONOMY", "5WI", "BEST"},new String[]{String.valueOf(stat.getMatches()), 
					String.valueOf(stat.getWickets()),Data,economy, String.valueOf(stat.getPlus_5()), 
					stat.getBest_figures()},null,null,new String[] {"-503.0","-279.0","-35.0","206.0","407.0","560.0"});
		}
		
		status = PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide);
		if(status == Constants.OK) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
//			setStatsPositionOfLT(5, 2, WhichSide,whatToProcess.split(",")[0], print_writers, config);
			setPositionOfLT(whatToProcess,WhichSide,config);
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
		case Constants.ICC_U19_2023:
			switch(whatToProcess) {
			
			case "Shift_F3":
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
			case "s":
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
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
				break;
			case "d":  case "e":
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
			case "Control_a":
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
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				break;
				
			case "Control_F3":
				if(lowerThird.getWhichTeamFlag() != null) {
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
						+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + " " +lowerThird.getBallsFacedText() 
						+ " OVERS"+ "\0", print_writers);
			
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
				break;
			
			case "Control_p":
				
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
			case "Control_h":
				
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
				
				if(lowerThird.getSubTitle().equalsIgnoreCase("p1")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "(OVERS 1 TO 10)" + "\0", print_writers);
				}else if(lowerThird.getSubTitle().equalsIgnoreCase("p2")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "(OVERS 11 TO 40)" + "\0", print_writers);
				}else if(lowerThird.getSubTitle().equalsIgnoreCase("p3")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + "(OVERS 41 TO 50)" + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " +"" + "\0", print_writers);
				
				break;	
			case "Alt_k":
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
				
			case "Alt_d":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
						"$Select_Flags*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Name*GEOM*TEXT SET " + lowerThird.getHeaderText() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$txt_Designation*GEOM*TEXT SET " + lowerThird.getSubTitle() + " " + lowerThird.getScoreText() + " OVERS" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name" + containerName + "$Score*ACTIVE SET 0 \0", print_writers);
				break;
				
			case "Control_F5": case "Control_F9":
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
				
			case "F8":
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
				
			case "F5":
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
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
				break;
				
			case "F9":
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
				
			case "F10":
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
			
			case "Shift_F5": case "Shift_F9":
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
				
				switch(whatToProcess) {
				case "Shift_F5":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getBallsFacedText() + " BALL" + 
							CricketFunctions.Plural(Integer.valueOf(lowerThird.getBallsFacedText())).toUpperCase() + "\0", print_writers);
					break;
				case "Shift_F9":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getBallsFacedText() + " " + lowerThird.getSubTitle() + "\0", print_writers);
					break;	
				}
				break;
				
			case "Alt_F12":
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
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name" + containerName + "$Score$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getBallsFacedText() + " OVERS" + "\0", print_writers);
				
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
				
			case "F6": case "Control_F6": case "Shift_F6":
				
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
			}
			break;
		}
		return Constants.OK;
	}
	public void HideAndShowL3rdSubStrapContainers(int WhichSide)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			//Show number of sublines
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$1" + containerName + "$Divider*ACTIVE SET 0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$2" + containerName + "$Divider*ACTIVE SET 0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$3" + containerName + "$Divider*ACTIVE SET 0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$4" + containerName + "$Divider*ACTIVE SET 0 \0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_"+ WhichSide + 
					"$Select_Subline*FUNCTION*Omo*vis_con SET " + lowerThird.getNumberOfSubLines() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$1$Data$Stat*ACTIVE SET 0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0", print_writers);
			
			if(lowerThird.getTitlesText() != null) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
					"$Select_Subline$1$Data$Title*ACTIVE SET 1 \0", print_writers);
				for(int iTitle = 1; iTitle <= lowerThird.getTitlesText().length; iTitle++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1$Data$Title$txt_" + iTitle + "*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_" + WhichSide +
						"$Select_Subline$1" + containerName + "$Data$Title$txt_" + iTitle + "*TRANSFORMATION*POSITION*X SET " 
						+ lowerThird.getPosition()[iTitle-1] + "\0",print_writers);
				}
				//Hide number of Titles on each strap
				for(int iTitle = lowerThird.getTitlesText().length + 1; iTitle <= 10; iTitle++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1$Data$Title$txt_" + iTitle + "*ACTIVE SET 0 \0", print_writers);
				}
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1$Data$Title*ACTIVE SET 0 \0", print_writers);
			}
			
			if(lowerThird.getStatsText() != null) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Stat*ACTIVE SET 1 \0", print_writers);
				for(int iStats = 1; iStats <= lowerThird.getStatsText().length; iStats++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Stat$txt_" + iStats + "*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_" + WhichSide +
						"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + iStats + "*TRANSFORMATION*POSITION*X SET " + lowerThird.getPosition()[iStats-1] + "\0",print_writers);
				}
				//Hide number of Titles on each strap
				for(int iStats = lowerThird.getTitlesText().length + 1; iStats <= 10; iStats++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Stat$txt_" + iStats + "*ACTIVE SET 0 \0", print_writers);
				}
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Stat*ACTIVE SET 0 \0", print_writers);
			}
			
			for(int iSubLine = 1; iSubLine <= lowerThird.getNumberOfSubLines(); iSubLine++) {
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
								"$Select_Subline$" + iTitle + "" + containerName + "$Data$Right*TRANSFORMATION*POSITION*X SET " 
								+ lowerThird.getPosition()[0] + "\0",print_writers);
						}
					}
					
					
				} else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$"+ iSubLine + "$Data$Right*ACTIVE SET 0 \0", print_writers);
				}
			}
			
			break;
		}
	}
	
	public void setPositionOfLT(String whatToProcess,int WhichSide,Configuration config)
	{
		String LT_Position_1 = "",LT_Position_2 = "",LT_Position_3 = "",LT_Position_4 = "",
				LT_Flag_Keys="",LT_Flag_Object="",slave_Flag_Keys = "",slave_Flag_Object="";
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			switch (lowerThird.getNumberOfSubLines()) {
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
			}
			
			switch (whatToProcess.split(",")[0]) {
			case "F8": case "F10": case "j":
				
				ltWhichContainer = "$All_NameSupers";
				if(lowerThird.getWhichTeamFlag().isEmpty() && lowerThird.getWhichSponsor().isEmpty()) {
					LT_Flag_Keys = "50.0";
					LT_Flag_Object = "990";
					
					slave_Flag_Keys = "50.0";
					slave_Flag_Object = "990";
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
					default:
						if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
							slave_Flag_Keys = "50.0";
							slave_Flag_Object = "990";
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
			
			if(!whatToProcess.split(",")[0].equalsIgnoreCase("q") && !whatToProcess.split(",")[0].equalsIgnoreCase("Control_q")) {
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
				CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$RightPartSize*ANIMATION*KEY*$H1*VALUE SET "
					+ slave_Flag_Keys + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$RightPartSize*ANIMATION*KEY*$H2*VALUE SET "
					+ slave_Flag_Keys + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$RightPartSize*ANIMATION*KEY*$H_In*VALUE SET "
						+ slave_Flag_Keys + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$RightPartSize*ANIMATION*KEY*$H_Out*VALUE SET "
						+ slave_Flag_Keys + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$Side_" + WhichSide + "$obj_WidthX"
					+ "*FUNCTION*Maxsize*WIDTH_X SET " + slave_Flag_Object + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$Side_" + WhichSide + "$obj_WidthX"
						+ "*FUNCTION*Maxsize*WIDTH_X SET " + slave_Flag_Object + "\0", print_writers);
			}
			
			CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$RightPartSize*ANIMATION*KEY*$H1*VALUE SET "
				+ LT_Flag_Keys + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$RightPartSize*ANIMATION*KEY*$H_In*VALUE SET "
					+ LT_Flag_Keys + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$RightPartSize*ANIMATION*KEY*$H_Out*VALUE SET "
					+ LT_Flag_Keys + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$RightPartSize*ANIMATION*KEY*$H2*VALUE SET "
				+ LT_Flag_Keys + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$Side_" + WhichSide + "$obj_WidthX"
				+ "*FUNCTION*Maxsize*WIDTH_X SET " + LT_Flag_Object + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*" + ltWhichContainer + "$Top_Line$Side_" + WhichSide + "$obj_WidthX"
					+ "*FUNCTION*Maxsize*WIDTH_X SET " + LT_Flag_Object + "\0", print_writers);
			break;
		}
	}
	
	public String PopulateL3rdBody(int WhichSide, String whatToProcess) 
	{
		if(WhichSide == 1) {
			containerName = "$Change_Out";
		}else if(WhichSide == 2) {
			containerName = "$Change_In";
		}
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			switch (whatToProcess) {
			case "Shift_F3":
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
				
			case "s":
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
				
			case "Control_a":
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
				
			case "Control_F3":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + 
					lowerThird.getLeftText()[0]+" "+lowerThird.getLeftText()[1]+" "+lowerThird.getLeftText()[2]+ "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + lowerThird.getRightText()[0] + " " + lowerThird.getRightText()[2]
					+"        "+lowerThird.getRightText()[1]+lowerThird.getRightText()[3] + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " +lowerThird.getLeftText()[3]+" "+lowerThird.getLeftText()[4]+" "+lowerThird.getLeftText()[5]+ "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$2$Data$Right$txt_1*GEOM*TEXT SET " +lowerThird.getRightText()[0] + " " + lowerThird.getRightText()[4]
							+"        "+lowerThird.getRightText()[1]+lowerThird.getRightText()[5]+ "\0", print_writers);
				break;
				
			case "Control_p":
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
				
			case "Alt_k":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + " " + lowerThird.getLeftText()[1] + " OFF " + 
					lowerThird.getLeftText()[2]+ "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + lowerThird.getRightText()[0] + " " + lowerThird.getRightText()[1] + " OFF " + 
					lowerThird.getRightText()[2] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + "PARTNERSHIP " + lowerThird.getScoreText() + " RUNS FROM " + 
					lowerThird.getBallsFacedText() + " BALLS"  + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$2$Data$Right$txt_1*GEOM*TEXT SET " + "" + "\0", print_writers);
				break;
			
			case "Alt_d":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + "DLS PAR SCORE " + lowerThird.getLeftText()[0] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1].toUpperCase()  + "\0", print_writers);
				break;
				
			case "Control_F5": case "Control_F9":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
				
				switch (whatToProcess) {
				case "Control_F9":
					if(!lowerThird.getRightText()[0].equalsIgnoreCase("WITHOUTEND")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
								+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + lowerThird.getRightText()[0] + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
								+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + "" + "\0", print_writers);
					}
					break;
				}
				
				break;
				
			case "q": case "Control_q":
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
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + "FOURS  " + lowerThird.getLeftText()[1] + "                           SIXES  " + 
					lowerThird.getLeftText()[2] + "                           DOTS  " + lowerThird.getLeftText()[3] + "                           STRIKE RATE  " + 
					lowerThird.getLeftText()[4]  + "\0", print_writers);
				break;
				
				
			case "Shift_F6":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + "FOURS " + lowerThird.getLeftText()[0] + "      SIXES " + 
					lowerThird.getLeftText()[1] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + "STRIKE RATE " + lowerThird.getRightText()[0] + "\0", print_writers);
				break;	
				
			case "F5":
				for(int i=0; i<lowerThird.getTitlesText().length; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1" + containerName + "$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2" + containerName + "$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
				}
				break;
				
			case "F9":
				for(int i=0; i<lowerThird.getTitlesText().length; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1" + containerName + "$Data$Title$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2" + containerName + "$Data$Stat$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
				}
				break;
			case "Control_h":
				
				for(int i=0; i<lowerThird.getTitlesText().length; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1" + containerName + "$Data$Title$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2" + containerName + "$Data$Stat$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
				}
				break;	
			case "Shift_F5": case "Shift_F9": case "Alt_F12":
				for(int i=0; i<lowerThird.getTitlesText().length; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1" + containerName + "$Data$Title$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2" + containerName + "$Data$Stat$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0", print_writers);
				}
				break;
				
			case "F7": case "F11": case "Control_s": case "Control_f":
				for(int iStat = 0; iStat < lowerThird.getTitlesText().length; iStat++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$1$Data$Title$txt_" + (iStat + 1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[iStat] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$Select_Subline$2$Data$Stat$txt_" + (iStat + 1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[iStat] + "\0", print_writers);
				}
				break;
				
			case "F8":
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
				
			case "F10":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Position_With_Graphics$Sublines$Side_" + WhichSide 
						+ "$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
				break;
				
			case "Control_g":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0", print_writers);
				break;
				
			case "e":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " +lowerThird.getLeftText()[0]+ "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET "+ "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET "+lowerThird.getLeftText()[1]+ "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$2$Data$Right$txt_1*GEOM*TEXT SET " + "\0", print_writers);
				
				
				break;
				
			case "d":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
					+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + " " + lowerThird.getLeftText()[2] + 
					lowerThird.getLeftText()[3] + "\0", print_writers);
				
				break;	
			}
			break;
		}
		return Constants.OK;
	}
}