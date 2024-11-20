package com.cricket.captions;

import java.io.File;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.poi.ss.formula.atp.Switch;

import com.cricket.containers.LowerThird;
import com.cricket.model.BatBallGriff;
import com.cricket.model.BattingCard;
import com.cricket.model.BestStats;
import com.cricket.model.BowlingCard;
import com.cricket.model.Bugs;
import com.cricket.model.Configuration;
import com.cricket.model.Event;
import com.cricket.model.HeadToHead;
import com.cricket.model.Inning;
import com.cricket.model.LeagueTable;
import com.cricket.model.MatchAllData;
import com.cricket.model.Partnership;
import com.cricket.model.Player;
import com.cricket.model.Team;
import com.cricket.model.VariousText;
import com.cricket.service.CricketService;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;

import io.netty.util.Constant;

public class BugsAndMiniGfx 
{
	public String status = "",homecolor = "", awaycolor = "",WhichGroup = "",containerName = "", previous_sixes = "",today_sixes="";
	int fallOfWickets;
	
	int rowId=0, omo_num=0;
	String cont_name = "",text_name = "";
	public List<PrintWriter> print_writers; 
	public Configuration config;
	public List<Bugs> bugs;
	public List<Team> Teams;
	public List<VariousText> VariousText;
	public List<HeadToHead> headToHead;
	public List<MatchAllData> tournament_matches;
	public List<BatBallGriff> griff = new ArrayList<BatBallGriff>();
	public List<String> this_data_str = new ArrayList<String>();
	ArrayList<BestStats> bowler_data = new ArrayList<BestStats>();
	ArrayList<BestStats> batter_data = new ArrayList<BestStats>();
	
	//public FallOfWicket fallOfWickets;
	public BattingCard battingCard;
	public BowlingCard bowlingCard;
	public Partnership partnership;
	public VariousText variousText;
	public Inning inning;
	public Player player;
	public LeagueTable leagueTable;
	public CricketService cricketService;
	
	public Team team;
	public Bugs bug;

	public BugsAndMiniGfx() {
		super();
	}
	
	public BugsAndMiniGfx(List<PrintWriter> print_writers, Configuration config, List<Bugs> bugs, List<Team> teams, List<VariousText> VariousText,CricketService cricketService,List<HeadToHead> headToHead, List<MatchAllData> tournament_matches) {
		super();
		this.print_writers = print_writers;
		this.config = config;
		this.bugs = bugs;
		this.Teams = teams;
		this.VariousText = VariousText;
		this.cricketService = cricketService;
		this.headToHead = headToHead;
		this.tournament_matches = tournament_matches;
	}
	
	public String populateBowlerVsAllBatsman(String whatToProcess,int WhichSide,MatchAllData matchAllData) {
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
		if(inning == null) {
			return "Current Inning NOT found in this match";
		}
		player = cricketService.getAllPlayer().stream().filter(plyr->plyr.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
		if(player == null) {
			return "Player not found in db";
		}
		team = Teams.stream().filter(tm->tm.getTeamId() == player.getTeamId()).findAny().orElse(null);
		if(team == null) {
			return "Can't find team of the player";
		}
//		batter_data = CricketFunctions.getBowlerVsAllBat(Integer.valueOf(whatToProcess.split(",")[2]), inning.getInningNumber(), 
//				cricketService.getAllPlayer(), matchAllData);
		for(BestStats bs : batter_data) {
			System.out.println("NAME : "+bs.getPlayer().getFull_name()+" : BALLS : "+bs.getBalls()+" : RUNS : "+bs.getRuns());
		}
		if(populateMiniBody(WhichSide, whatToProcess.split(",")[0], matchAllData, inning.getInningNumber()) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	
	public String populateBatStatsVsAllBowlers(String whatToProcess,int WhichSide,MatchAllData matchAllData) {
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
		if(inning == null) {
			return "Current Inning NOT found in this match";
		}
		player = cricketService.getAllPlayer().stream().filter(plyr->plyr.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
		if(player == null) {
			return "Player not found in db";
		}
		team = Teams.stream().filter(tm->tm.getTeamId() == player.getTeamId()).findAny().orElse(null);
		if(team == null) {
			return "Can't find team of the player";
		}
		bowler_data = CricketFunctions.getBatsmanRunsVsAllBowlers(Integer.valueOf(whatToProcess.split(",")[2]), inning.getInningNumber(), cricketService.getAllPlayer(), matchAllData);
		for(BestStats stat : bowler_data) {
			System.out.println(stat.getPlayer().getFull_name()+" BALLS : "+stat.getBalls() +" RUNS : "+stat.getRuns());
		}
		if(populateMiniBody(WhichSide, whatToProcess.split(",")[0], matchAllData, inning.getInningNumber()) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	
	public String populateCounter(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws CloneNotSupportedException {
		
		this_data_str = new ArrayList<String>();
		today_sixes = String.valueOf(CricketFunctions.extracttournamentFoursAndSixesData("CURRENT_MATCH_DATA", headToHead, matchAllData, null).getTournament_sixes());
		
		if(Integer.valueOf(today_sixes) > 0 && matchAllData.getEventFile().getEvents().get(matchAllData.getEventFile().getEvents().size()-1).getEventType().equalsIgnoreCase(CricketUtil.SIX)) {
			if(matchAllData.getEventFile().getEvents().get(matchAllData.getEventFile().getEvents().size()-1).getEventWasABoundary() != null && 
					matchAllData.getEventFile().getEvents().get(matchAllData.getEventFile().getEvents().size()-1).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
				
				today_sixes = String.valueOf(Integer.valueOf(today_sixes)-1);
			}
		}
		
		this_data_str.add(CricketFunctions.hundredsTensUnits(String.valueOf(Integer.valueOf(previous_sixes) + Integer.valueOf(today_sixes))));
		if(WhichSide == 1) {
			String new_six_value = String.valueOf((Integer.valueOf(previous_sixes) + Integer.valueOf(today_sixes) + 1));
			this_data_str.add(CricketFunctions.hundredsTensUnits(new_six_value));
		}
		if(PopulateBugBody(WhichSide, whatToProcess,matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	
	public String populateGriff(String whatToProcess,int WhichSide,MatchAllData matchAllData) {
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
		if(inning == null) {
			return "populateBatThisMatch: Current Inning NOT found in this match";
		}
		
		player = cricketService.getAllPlayer().stream().filter(plyr ->plyr.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
		if(player == null) {
			return "player not found";
		}
		
		team = Teams.stream().filter(tm->tm.getTeamId() == player.getTeamId()).findAny().orElse(null);
		if(team == null) {
			return "Can't find team of the player";
		}
//		int count = 0;
//		String MatchName = "";
//		
//		for(HeadToHead h2h : headToHead) {
//			if(h2h.getPlayerId() == player.getPlayerId() && h2h.getTeam().getTeamName4().equalsIgnoreCase(team.getTeamName4())) {
//				MatchName = h2h.getMatchFileName();
//				if(h2h.getInningStarted().contains("Y")) {
//					System.out.println(h2h.getMatchFileName() + " RUNS : " + h2h.getRuns());
//				}else {
//					System.out.println(h2h.getMatchFileName() +  " - DNB");
//				}
//				count = 0;
//			}else if(h2h.getTeam().getTeamName4().equalsIgnoreCase(team.getTeamName4())) {
//				if(count == 11) {
//					System.out.println(h2h.getMatchFileName() +  " - DNP");
//					count = 0;
//				}else if(!MatchName.equalsIgnoreCase(h2h.getMatchFileName()) && count < 11) {
//					MatchName = h2h.getMatchFileName();
//					count = 1;
//				}else {
//					count++;
//				}
//			}
//		}
		
		switch (whatToProcess.split(",")[0]) {
		case Constants.NPL:
			break;

		default:
			if(whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F1")) {
				griff = CricketFunctions.getBatBallGriffData(CricketUtil.BATSMAN,Integer.valueOf(whatToProcess.split(",")[2]), Teams, tournament_matches, matchAllData);
			}else if(whatToProcess.split(",")[0].equalsIgnoreCase("Alt_F2")) {
				griff = CricketFunctions.getBatBallGriffData(CricketUtil.BOWLER,Integer.valueOf(whatToProcess.split(",")[2]), Teams, tournament_matches, matchAllData);
			}
			if(griff == null) {
				return "Griff is null";
			}
			break;
		}
		
		
		
		if(populateMiniBody(WhichSide, whatToProcess.split(",")[0], matchAllData, inning.getInningNumber()) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	
	public String populatePopup(String whatToProcess, int whichSide, MatchAllData matchAllData){
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
		if(inning == null) {
			return "inning is null";
		}
		switch (whatToProcess.split(",")[0]) {
		case "Control_Shift_U":
			battingCard = inning.getBattingCard().stream().filter(bc->bc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			if(battingCard == null) {
				return ": player [" + battingCard.getPlayer().getFull_name() + "] is not present in batting card";
			}
			break;

		case "Control_Shift_V":
			bowlingCard = inning.getBowlingCard().stream().filter(boc->boc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			if(bowlingCard == null) {
				return ": player [" + bowlingCard.getPlayer().getFull_name() + "] is not present in bowlingCard";
			}
			break;
		}
		if(PopulateBugBody(whichSide, whatToProcess,matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	
	public String populatePointsTable(String whatToProcess, MatchAllData matchAllData,int WhichSide) throws ParseException, JAXBException, InterruptedException
	{
		switch(config.getBroadcaster().toUpperCase()){
		case Constants.BENGAL_T20:
			if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.LEAGUE_TABLE_DIRECTORY + CricketUtil.LEAGUETABLE_XML).exists()) {
				leagueTable = (LeagueTable)JAXBContext.newInstance(LeagueTable.class).createUnmarshaller().unmarshal(
						new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.LEAGUE_TABLE_DIRECTORY + CricketUtil.LEAGUETABLE_XML));
			}
			break;
		default:
			if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.LEAGUE_TABLE_DIRECTORY + WhichGroup + CricketUtil.XML_EXTENSION).exists()) {
				leagueTable = (LeagueTable)JAXBContext.newInstance(LeagueTable.class).createUnmarshaller().unmarshal(
						new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.LEAGUE_TABLE_DIRECTORY + WhichGroup + CricketUtil.XML_EXTENSION));
			}
			break;
		}
		
		if(leagueTable == null) {
			return "populatePointsTable : League Table is null";
		}
		
		if(populateMiniBody(WhichSide, whatToProcess.split(",")[0],matchAllData, 0) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	
	public String populateWicketSequencing(String whatToProcess,MatchAllData matchAllData,int WhichSide)
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			status = "populateWicketSequencing match is null Or Inning is null";
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
			if(inning == null) {
				return "populateWicketSequencing Inning is null";
			}
			
			player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(whatToProcess.split(",")[2]), matchAllData);
			battingCard = inning.getBattingCard().stream().filter(bc->bc.getPlayerId() == player.getPlayerId()).findAny().orElse(null);
			team = Teams.stream().filter(tm -> tm.getTeamId() == player.getTeamId()).findAny().orElse(null);
			if(team == null) {
				return "populateWicketSequencing: Team id [" + battingCard.getPlayer().getTeamId() + "] from database is returning NULL";
			}
		}
		if(PopulateBugBody(WhichSide, whatToProcess,matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	
	public String bugsDismissal(String whatToProcess,MatchAllData matchAllData,int WhichSide) {
		
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return "bugsDismissal match is null Or Inning is null";
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
			if(inning == null) {
				return "bugsDismissal Inning is null";
			}
			battingCard = inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			if(battingCard == null) {
				return "bugsDismissal Batting Card is null";
			}
			team = Teams.stream().filter(tm -> tm.getTeamId() == battingCard.getPlayer().getTeamId()).findAny().orElse(null);
			if(team == null) {
				return "bugsDismissal: Team id [" + battingCard.getPlayer().getTeamId() + "] from database is returning NULL";
			}
		}
		if(PopulateBugBody(WhichSide, whatToProcess,matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	public String bugsover(String whatToProcess,MatchAllData matchAllData,int WhichSide) {
		
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
		if(inning == null) {
			return "populateBatScore Inning is null";
		}
		if(PopulateBugBody(WhichSide, whatToProcess,matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	public String bugstape(String whatToProcess,MatchAllData matchAllData,int WhichSide) {
		
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
		
		if(inning == null) {
			return "populateBatScore Inning is null";
		}
		if(PopulateBugBody(WhichSide, whatToProcess,matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	public String bugsPlayerOfMatch(String whatToProcess,MatchAllData matchAllData,int WhichSide) {
		
		if(PopulateBugBody(WhichSide, whatToProcess,matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	public String bugsThirdUmpire(String whatToProcess,MatchAllData matchAllData,int WhichSide) {
		
		if(PopulateBugBody(WhichSide, whatToProcess,matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	public String bugsCurrPartnership(String whatToProcess,MatchAllData matchAllData,int WhichSide) {
		
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return "bugsCurrPartnership match is null Or Inning is null";
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			if(inning == null) {
				return "bugsCurrPartnership Inning is null";
			}
			partnership = inning.getPartnerships().stream().filter(pship -> pship.getPartnershipNumber() == 
					inning.getPartnerships().get(inning.getPartnerships().size()-1).getPartnershipNumber()).findAny().orElse(null);
			if(partnership == null) {
				return "bugsCurrPartnership Partnership is null";
			}
			team = Teams.stream().filter(tm -> tm.getTeamId() == inning.getBattingTeamId()).findAny().orElse(null);
			if(team == null) {
				return "bugsCurrPartnership: Team id [" + inning.getBattingTeamId() + "] from database is returning NULL";
			}
		}
		if(PopulateBugBody(WhichSide, whatToProcess,matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	public String bugMultiPartnership(String whatToProcess,MatchAllData matchAllData,int WhichSide) {
		
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return "bugMultiPartnership match is null Or Inning is null";
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
			if(inning == null) {
				return "bugMultiPartnership Inning is null";
			}
			
			partnership = inning.getPartnerships().stream().filter(pship -> pship.getPartnershipNumber() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			if(partnership == null) {
				return "bugMultiPartnership Partnership is null";
			}
			team = Teams.stream().filter(tm -> tm.getTeamId() == inning.getBattingTeamId()).findAny().orElse(null);
			if(team == null) {
				return "bugMultiPartnership: Team id [" + inning.getBattingTeamId() + "] from database is returning NULL";
			}
		}
		if(PopulateBugBody(WhichSide, whatToProcess,matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	public String bugsDB(String whatToProcess,int WhichSide,MatchAllData matchAllData) {
		
		bug = this.bugs.stream().filter(bug -> bug.getBugId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
		if(bug == null) {
			return "bugsDB: bug is returning NULL";
		}
		
		if(bug.getFlag() != null) {
			team = Teams.stream().filter(tm -> tm.getTeamName4().equalsIgnoreCase(bug.getFlag())).findAny().orElse(null);
			if(team == null) {
				return "bugsDB: Flag in database is returning NULL";
			}
		}
		
		if(PopulateBugBody(WhichSide, whatToProcess,matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	public String populateBowlScore(String whatToProcess,MatchAllData matchAllData,int WhichSide)
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			status = "populateBowlScore match is null Or Inning is null";
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
			if(inning == null) {
				return "populateBowlScore Inning is null";
			}
			bowlingCard = inning.getBowlingCard().stream().filter(boc -> boc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			if(bowlingCard == null) {
				return "populateBowlScore Bowling is null";
			}
			team = Teams.stream().filter(tm -> tm.getTeamId() == bowlingCard.getPlayer().getTeamId()).findAny().orElse(null);
			if(team == null) {
				return "populateBowlScore: Team id [" + bowlingCard.getPlayer().getTeamId() + "] from database is returning NULL";
			}
		}
		if(PopulateBugBody(WhichSide, whatToProcess,matchAllData) == Constants.OK) {
			status = Constants.OK;;
		}
		return status;		
	}
	
	public String populateBugTarget(String whatToProcess,MatchAllData matchAllData,int WhichSide)
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			status = "populateBugTarget match is null Or Inning is null";
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1]))
					.findAny().orElse(null);
				
			if(inning == null) {
				return "populateTarget: Current Inning NOT found in this match";
			}
			
			if(inning.getInningNumber() == 1) {
				return "populateTarget: Current Inning is 1";
			}
		}
		if(PopulateBugBody(WhichSide, whatToProcess,matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	
	public String populateBatScore(String whatToProcess,MatchAllData matchAllData,int WhichSide)
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			status = "populateBatScore match is null Or Inning is null";
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
			if(inning == null) {
				return "populateBatScore Inning is null";
			}
			battingCard = inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
			if(battingCard == null) {
				return "populateBatScore Batting is null";
			}
			team = Teams.stream().filter(tm -> tm.getTeamId() == battingCard.getPlayer().getTeamId()).findAny().orElse(null);
			if(team == null) {
				return "populateBatScore: Team id [" + battingCard.getPlayer().getTeamId() + "] from database is returning NULL";
			}
		}
		if(PopulateBugBody(WhichSide, whatToProcess,matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	public String populateBugHighlight(String whatToProcess,MatchAllData matchAllData,int WhichSide, int whichInning)
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			status = "populateBatScore match is null Or Inning is null";
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == whichInning).findAny().orElse(null);
			if(inning == null) {
				return "populateBatScore Inning is null";
			}
		}
		if(PopulateBugBody(WhichSide, whatToProcess,matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	public String bugsToss(String whatToProcess, MatchAllData matchAllData, int WhichSide) {
		if (matchAllData == null || matchAllData.getMatch() == null ||
			matchAllData.getMatch().getInning() == null|| matchAllData.getSetup() == null) {
			status = "BugsToss match is null Or Inning is null";
		} else {
			inning = matchAllData.getMatch().getInning().stream().
					filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).
					findAny().orElse(null);
		}
		if(PopulateBugBody(WhichSide, whatToProcess,matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	public String populatebugPowerplay(String whatToProcess,int WhichSide, MatchAllData matchAllData) {
		if (matchAllData == null || matchAllData.getMatch() == null ||
			matchAllData.getMatch().getInning() == null|| matchAllData.getSetup() == null) {
			status = "BugPowerplay match is null Or Inning is null";
		} else {
			inning = matchAllData.getMatch().getInning().stream().
					filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).
					findAny().orElse(null);
		}
		
		if(PopulateBugBody(WhichSide, whatToProcess,matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}

	public String populateBugResult(String whatToProcess, MatchAllData matchAllData, int whichSide) {
		if (matchAllData == null || matchAllData.getMatch() == null ||
				matchAllData.getMatch().getInning() == null|| matchAllData.getSetup() == null) {
				status = "populateBugResult match is null Or Inning is null";
			} else {
				inning = matchAllData.getMatch().getInning().stream().
						filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).
						findAny().orElse(null);
			}
			if(PopulateBugBody(whichSide, whatToProcess,matchAllData) == Constants.OK) {
				status = Constants.OK;
			}
			return status;
	}
	public String populateBugSixDistance(String whatToProcess, MatchAllData matchAllData, int whichSide) {
		if(PopulateBugBody(whichSide, whatToProcess,matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	
	public String PopulateBugBody(int WhichSide, String whatToProcess,MatchAllData matchAllData) {
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.NPL:
			switch (whatToProcess.split(",")[0]) {
			case "Control_Shift_J":
				
				String summary = "";
				
				if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER) && matchAllData.getSetup().getMaxOvers() == 1) {
					
					summary = inning.getBatting_team().getTeamName1() + " NEED " + CricketFunctions.getTargetRuns(matchAllData) + " RUNS" + " TO WIN " + 
							"FROM " + (matchAllData.getSetup().getMaxOvers()*6) + " BALLS";
					
				}else {
					if(matchAllData.getSetup().getTargetOvers() == "" || matchAllData.getSetup().getTargetOvers().trim().isEmpty() && matchAllData.getSetup().getTargetRuns() == 0) {
						
						summary = inning.getBatting_team().getTeamName1() + " NEED " + CricketFunctions.getTargetRuns(matchAllData) + " RUNS" + " TO WIN " + 
								"FROM " + CricketFunctions.getTargetOvers(matchAllData) + " OVERS";
						

					}else {
						
						if(matchAllData.getSetup().getTargetOvers() != "") {
							
							summary = inning.getBatting_team().getTeamName1() + " NEED " + CricketFunctions.getTargetRuns(matchAllData) + " RUNS" + " TO WIN " + 
									"FROM " + CricketFunctions.getTargetOvers(matchAllData) + " OVERS";
						}
						if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("VJD")) {
							
							summary = inning.getBatting_team().getTeamName1() + " NEED " + CricketFunctions.getTargetRuns(matchAllData) + " RUNS" + " TO WIN " + 
									"FROM " + CricketFunctions.getTargetOvers(matchAllData) + " OVERS (VJD)";
							
						}else if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("DLS")) {
							
							summary = inning.getBatting_team().getTeamName1() + " NEED " + CricketFunctions.getTargetRuns(matchAllData) + " RUNS" + " TO WIN " + 
									"FROM " + CricketFunctions.getTargetOvers(matchAllData) + " OVERS (DLS)";
						}
					}
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Name*GEOM*TEXT SET " + summary + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET \0", print_writers);
				
				break;
			case "g":
				
				if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
					if(inning.getInningNumber() == 1 || inning.getInningNumber() == 2) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
								+ "$txt_Name*GEOM*TEXT SET " + bowlingCard.getPlayer().getTicker_name() + " - 1st INNS" + "\0", print_writers);
					}else if(inning.getInningNumber() == 3 || inning.getInningNumber() == 4) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
								+ "$txt_Name*GEOM*TEXT SET " + bowlingCard.getPlayer().getTicker_name() + " - 2nd INNS" + "\0", print_writers);
					}
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + bowlingCard.getPlayer().getTicker_name() + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$SubText$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + CricketFunctions.OverBalls(bowlingCard.getOvers(), bowlingCard.getBalls()) + " - " + bowlingCard.getMaidens() 
							+ " - " + bowlingCard.getRuns() + " - " + bowlingCard.getWickets() + "\0", print_writers);
				break;
				
			case "y":
				if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
					if(inning.getInningNumber() == 1 || inning.getInningNumber() == 2) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
								+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + " - 1st INNS" + "\0", print_writers);
					}else if(inning.getInningNumber() == 3 || inning.getInningNumber() == 4) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
								+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + " - 2nd INNS" + "\0", print_writers);
					}
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + "\0", print_writers);
				}
				
				if(battingCard.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET "  + battingCard.getRuns() + "*" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET "  + battingCard.getRuns()+ "\0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + battingCard.getBalls() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$SubText$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + battingCard.getFours() +" FOURS    " + battingCard.getSixes() + " SIXES"+ "\0", print_writers);
				break;
			case "Shift_F":
				if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
					if(inning.getInningNumber() == 1 || inning.getInningNumber() == 2) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
								+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + " - 1st INNS" + "\0", print_writers);
					}else if(inning.getInningNumber() == 3 || inning.getInningNumber() == 4) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
								+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + " - 2nd INNS" + "\0", print_writers);
					}
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET " + battingCard.getRuns() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + battingCard.getBalls() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + battingCard.getHowOutText() + "\0", print_writers);
				
				break;
			case "Shift_O":
				if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
					if(inning.getInningNumber() == 1 || inning.getInningNumber() == 2) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
								+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + " - 1st INNS" + "\0", print_writers);
					}else if(inning.getInningNumber() == 3 || inning.getInningNumber() == 4) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
								+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + " - 2nd INNS" + "\0", print_writers);
					}
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET " + battingCard.getRuns() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + battingCard.getBalls() + "\0", print_writers);
				
				if(battingCard.getHowOut().equalsIgnoreCase(CricketUtil.CAUGHT)) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
							+ "$txt_Sub*GEOM*TEXT SET " + battingCard.getHowOutText().split(" b ")[0] + "  b " + 
							battingCard.getHowOutText().split(" b ")[1] + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
							+ "$txt_Sub*GEOM*TEXT SET " + battingCard.getHowOutText() + "\0", print_writers);
				}
				
				break;
			case "Control_k":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Name*GEOM*TEXT SET " + "CURRENT PARTNERSHIP" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET " + partnership.getTotalRuns() + "*" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + partnership.getTotalBalls() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + "" + "\0", print_writers);
				break;
			case "Shift_F4":
				if(partnership.getPartnershipNumber() == 1) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET "+ partnership.getPartnershipNumber() + "st WICKET PARTNERSHIP" + "\0", print_writers);
					
				}else if(partnership.getPartnershipNumber() == 2) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + partnership.getPartnershipNumber() +"nd WICKET PARTNERSHIP" + "\0", print_writers);
					
				}else if(partnership.getPartnershipNumber() == 3) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET "+ partnership.getPartnershipNumber() + "rd WICKET PARTNERSHIP" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + partnership.getPartnershipNumber() +"th WICKET PARTNERSHIP" + "\0", print_writers);
				}
				
				if(partnership.getPartnershipNumber() == inning.getPartnerships().size()) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET " + partnership.getTotalRuns() + "*" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET " + partnership.getTotalRuns() + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + partnership.getTotalBalls() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + "" + "\0", print_writers);
				break;
			case "Alt_p": 
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Name*GEOM*TEXT SET " + whatToProcess.split(",")[2].split("-")[0] + " WON THE TOSS & ELECTED TO " + 
						whatToProcess.split(",")[2].split("-")[1] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET \0", print_writers);
				break;
			case "h":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Name*GEOM*TEXT SET " + inning.getBatting_team().getTeamName1() + "\0", print_writers);
				if (inning.getTotalWickets() >= 10) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET " + inning.getTotalRuns() + "\0", print_writers);
				} else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET " + inning.getTotalRuns()+ " - "+ inning.getTotalWickets() + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " +" "+ CricketFunctions.OverBalls(inning.getTotalOvers(), inning.getTotalBalls()) + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + "HIGHLIGHTS" + "\0", print_writers);
				break;	
			case "k":
				if(bug.getText4() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Balls*GEOM*TEXT SET " + "(" + bug.getText4() + ")" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Balls*GEOM*TEXT SET " + "" + "\0", print_writers);
				}
				
				if(bug.getText3() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET " + bug.getText3() + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET " + "" + "\0", print_writers);
				}
				
				if(bug.getText2() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
							+ "$txt_Sub*GEOM*TEXT SET " + bug.getText2() + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
							+ "$txt_Sub*GEOM*TEXT SET " + "" + "\0", print_writers);
				}
				
				if(bug.getText1() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + bug.getText1() + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + "" + "\0", print_writers);
				}
				
				
				break;
			case "Control_Shift_R":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET \0", print_writers);
				
				String matchResult = CricketFunctions.generateMatchSummaryStatus(inning.getInningNumber(), matchAllData, CricketUtil.FULL, "", config.getBroadcaster()).toUpperCase();
				
				if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
					if(matchResult.contains("tied")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
								+ "$txt_Name*GEOM*TEXT SET SUPER OVER TIED\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
								+ "$txt_Sub*GEOM*TEXT SET WINNER WILL BE DECIDED BY ANOTHER SUPER OVER\0", print_writers);
					}else {
						if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
							if(CricketFunctions.getRequiredRuns(matchAllData) <= 0) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
										+ "$txt_Name*GEOM*TEXT SET "+matchAllData.getMatch().getInning().get(1).getBatting_team().getTeamName1()+" WIN THE SUPER OVER"+"\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
										+ "$txt_Sub*GEOM*TEXT SET\0", print_writers);
							}else if(matchAllData.getMatch().getInning().get(1).getTotalWickets() >= 10 || 
									matchAllData.getMatch().getInning().get(1).getTotalOvers() >= matchAllData.getSetup().getMaxOvers()) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
										+ "$txt_Name*GEOM*TEXT SET "+matchAllData.getMatch().getInning().get(1).getBowling_team().getTeamName1()+" WIN THE SUPER OVER"+"\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
										+ "$txt_Sub*GEOM*TEXT SET\0", print_writers);
							}
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
									+ "$txt_Name*GEOM*TEXT SET "+matchResult+"\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
									+ "$txt_Sub*GEOM*TEXT SET\0", print_writers);
						}
					}
				}else {
					if(matchResult.contains("tied")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
								+ "$txt_Name*GEOM*TEXT SET MATCH TIED\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
								+ "$txt_Sub*GEOM*TEXT SET WINNER WILL BE DECIDED BY SUPER OVER\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
								+ "$txt_Name*GEOM*TEXT SET "+matchResult+"\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
								+ "$txt_Sub*GEOM*TEXT SET\0", print_writers);
					}
				}
				break;
			case "Shift_C":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Name*GEOM*TEXT SET SIX DISTANCE\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET "+whatToProcess.split(",")[2]+" METRES\0", print_writers);
				break;
			}
			break;
		case Constants.ICC_U19_2023:
			switch (whatToProcess.split(",")[0]) {
			case "Control_y":
				if(inning.getBatting_team().getTeamName4().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$img_Shadow*ACTIVE SET 0 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$img_Shadow*ACTIVE SET 1 \0", print_writers);
				}
				break;
			case "g": case "y": case "Shift_O": case "Shift_F": case "Control_k": case "Shift_F4":
				if(team.getTeamName4().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$img_Shadow*ACTIVE SET 0 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$img_Shadow*ACTIVE SET 1 \0", print_writers);
				}
				break;	
			case "o":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Select"
						+ "*FUNCTION*Omo*vis_con SET 0  \0", print_writers);
				CricketFunctions.DoadWriteCommandToSelectedViz(1,"-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Select$PlayerOftheMatch$Data$Select_Sponsor"
						+ "*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToSelectedViz(2,"-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Select$PlayerOftheMatch$Data$Select_Sponsor"
						+ "*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				break;
			case "t":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Select"
						+ "*FUNCTION*Omo*vis_con SET 2  \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToSelectedViz(2,"-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Select$Third_Umpire$1$Data$Side_1$SubText$Side1"
						+ "$group*ACTIVE SET 0 \0", print_writers);
				
				for(VariousText vt : VariousText) {
					if(vt.getVariousType().equalsIgnoreCase("THIRDUMPIRE") && vt.getUseThis().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Third_Umpire"
								+ "$1$Data$Side_1$SubText$txt_Sub*GEOM*TEXT SET " + vt.getVariousText() + "\0", print_writers);
					}else if(vt.getVariousType().equalsIgnoreCase("THIRDUMPIRE") && vt.getUseThis().toUpperCase().equalsIgnoreCase(CricketUtil.NO)) {
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Top_Align$Side" + WhichSide + "$Select_FooterType"
//							+ "$Info_Text$Data$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.
//							generateMatchSummaryStatus(WhichInning, matchAllData, CricketUtil.FULL, CricketUtil.BEAT).toUpperCase() + "\0", print_writers);
					}
				}
				break;	
			}
			switch(whatToProcess.split(",")[0]) {
			case "Alt_p": 
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Select"
						+ "*FUNCTION*Omo*vis_con SET 1  \0", print_writers);
				if(matchAllData.getSetup().getHomeTeam().getTeamName4().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Toss$img_Flag1$img_Shadow*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Toss$img_Flag2$img_Shadow*ACTIVE SET 1 \0", print_writers);
				}else if(matchAllData.getSetup().getAwayTeam().getTeamName4().equalsIgnoreCase("NEP")){
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Toss$img_Flag1$img_Shadow*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Toss$img_Flag2$img_Shadow*ACTIVE SET 0 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Toss$img_Flag1$img_Shadow*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Toss$img_Flag2$img_Shadow*ACTIVE SET 1 \0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Toss$img_Flag1*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + matchAllData.getSetup().getHomeTeam().getTeamName4() + "\0", print_writers);
						
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Toss$img_Flag2*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + matchAllData.getSetup().getAwayTeam().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Toss"
						+ "$txt_Info*GEOM*TEXT SET " + whatToProcess.split(",")[2].split("-")[0] + " WON THE TOSS & ELECTED TO " + 
						whatToProcess.split(",")[2].split("-")[1]+ "\0", print_writers);
				
				break;
			case "h":
				if(inning.getBatting_team().getTeamName4().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$img_Shadow*ACTIVE SET 0 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$img_Shadow*ACTIVE SET 1 \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Flag*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + 
						"$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Name*GEOM*TEXT SET " + inning.getBatting_team().getTeamName1() + "\0", print_writers);
				if (inning.getTotalWickets() >= 10) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET " + inning.getTotalRuns() + "\0", print_writers);
				} else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET " + inning.getTotalRuns()+ " - "+ inning.getTotalWickets() + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " +" "+ CricketFunctions.OverBalls(inning.getTotalOvers(), inning.getTotalBalls()) + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + "HIGHLIGHTS" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
				break;
			case "Control_y":
				String pp = "";
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Flag*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Name*GEOM*TEXT SET " + inning.getBatting_team().getTeamName1() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				if(whatToProcess.split(",")[2].equalsIgnoreCase("p1")) {
					pp = CricketFunctions.getFirstPowerPlayScore(matchAllData,inning.getInningNumber(), matchAllData.getEventFile().getEvents());
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$SubText$Side" + WhichSide 
							+ "$txt_Sub*GEOM*TEXT SET " + "POWERPLAY 1 " +  "(OVERS " + inning.getFirstPowerplayStartOver() + " TO " + 
							inning.getFirstPowerplayEndOver() + ") " + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET "  + pp.split(",")[0] + "\0", print_writers);
					
				}else if(whatToProcess.split(",")[2].equalsIgnoreCase("p2")) {
					pp=CricketFunctions.getSecPowerPlayScore(matchAllData, inning.getInningNumber(), matchAllData.getEventFile().getEvents());
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$SubText$Side" + WhichSide 
							+ "$txt_Sub*GEOM*TEXT SET " + "POWERPLAY 2 " +  "(OVERS " + inning.getSecondPowerplayStartOver() + " TO " + 
							inning.getSecondPowerplayEndOver() + ") " + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET "  + pp.split(",")[0] + "\0", print_writers);
					
				}else if(whatToProcess.split(",")[2].equalsIgnoreCase("p3")) {
					pp=CricketFunctions.getThirdPowerPlayScore(matchAllData, inning.getInningNumber(), matchAllData.getEventFile().getEvents());
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$SubText$Side" + WhichSide 
							+ "$txt_Sub*GEOM*TEXT SET " + "POWERPLAY 3 " +  "(OVERS " + inning.getThirdPowerplayStartOver() + " TO " + 
							inning.getThirdPowerplayEndOver() + ") " + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET "  + pp.split(",")[0] + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$SubText$Side" + WhichSide 
						+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
				break;
			case "g":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Flag*ACTIVE SET 1 \0", print_writers);
			
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
				
				if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
					if(inning.getInningNumber() == 1 || inning.getInningNumber() == 2) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
								+ "$txt_Name*GEOM*TEXT SET " + bowlingCard.getPlayer().getTicker_name() + " - 1st INNS" + "\0", print_writers);
					}else if(inning.getInningNumber() == 3 || inning.getInningNumber() == 4) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
								+ "$txt_Name*GEOM*TEXT SET " + bowlingCard.getPlayer().getTicker_name() + " - 2nd INNS" + "\0", print_writers);
					}
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + bowlingCard.getPlayer().getTicker_name() + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$SubText$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + CricketFunctions.OverBalls(bowlingCard.getOvers(), bowlingCard.getBalls()) + " - " + bowlingCard.getMaidens() 
							+ " - " + bowlingCard.getRuns() + " - " + bowlingCard.getWickets() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$SubText$Side" + WhichSide 
						+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
				break;
				
			case "y":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Flag*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
				
				if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
					if(inning.getInningNumber() == 1 || inning.getInningNumber() == 2) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
								+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + " - 1st INNS" + "\0", print_writers);
					}else if(inning.getInningNumber() == 3 || inning.getInningNumber() == 4) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
								+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + " - 2nd INNS" + "\0", print_writers);
					}
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + "\0", print_writers);
				}
				
				if(battingCard.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET "  + battingCard.getRuns() + "*" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET "  + battingCard.getRuns()+ "\0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + battingCard.getBalls() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$SubText$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + battingCard.getFours() +" FOURS    " + battingCard.getSixes() + " SIXES"+ "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$SubText$Side" + WhichSide 
						+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
				break;
			case "Shift_F":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Flag*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + 
						"$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
				
				if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
					if(inning.getInningNumber() == 1 || inning.getInningNumber() == 2) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
								+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + " - 1st INNS" + "\0", print_writers);
					}else if(inning.getInningNumber() == 3 || inning.getInningNumber() == 4) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
								+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + " - 2nd INNS" + "\0", print_writers);
					}
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET " + battingCard.getRuns() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + battingCard.getBalls() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + battingCard.getHowOutText() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
				
				break;
			case "Shift_O":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Flag*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + 
						"$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
				
				if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
					if(inning.getInningNumber() == 1 || inning.getInningNumber() == 2) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
								+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + " - 1st INNS" + "\0", print_writers);
					}else if(inning.getInningNumber() == 3 || inning.getInningNumber() == 4) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
								+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + " - 2nd INNS" + "\0", print_writers);
					}
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET " + battingCard.getRuns() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + battingCard.getBalls() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + battingCard.getHowOutText() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
				
				break;
			case "Control_k":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Flag*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + 
						"$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Name*GEOM*TEXT SET " + "CURRENT PARTNERSHIP" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET " + partnership.getTotalRuns() + "*" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + partnership.getTotalBalls() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
				break;
			case "Shift_F4":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Flag*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + 
						"$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
				
				if(partnership.getPartnershipNumber() == 1) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET "+ partnership.getPartnershipNumber() + "st WICKET PARTNERSHIP" + "\0", print_writers);
					
				}else if(partnership.getPartnershipNumber() == 2) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + partnership.getPartnershipNumber() +"nd WICKET PARTNERSHIP" + "\0", print_writers);
					
				}else if(partnership.getPartnershipNumber() == 3) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET "+ partnership.getPartnershipNumber() + "rd WICKET PARTNERSHIP" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + partnership.getPartnershipNumber() +"th WICKET PARTNERSHIP" + "\0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET " + partnership.getTotalRuns() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + partnership.getTotalBalls() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
				break;
			case "k":
				if(bug.getSponsor() != null) {
					if(config.getSecondaryIpAddress()!= null) {
						CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
								+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
							+ "$img_Sponsor*ACTIVE SET 1 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
							+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
				}
				
				if(bug.getFlag() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$img_Flag*ACTIVE SET 1 \0", print_writers);
					if(team.getTeamName4().equalsIgnoreCase("NEP")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
								+ "$img_Shadow*ACTIVE SET 0 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
								+ "$img_Shadow*ACTIVE SET 1 \0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + 
							"$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$img_Flag*ACTIVE SET 0 \0", print_writers);
				}
				
				if(bug.getText4() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Balls*GEOM*TEXT SET " + "(" + bug.getText4() + ")" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Balls*GEOM*TEXT SET " + "" + "\0", print_writers);
				}
				
				if(bug.getText3() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET " + bug.getText3() + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET " + "" + "\0", print_writers);
				}
				
				if(bug.getText2() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
							+ "$txt_Sub*GEOM*TEXT SET " + bug.getText2() + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
							+ "$txt_Sub*GEOM*TEXT SET " + "" + "\0", print_writers);
				}
				
				if(bug.getText1() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + bug.getText1() + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + "" + "\0", print_writers);
				}
				
				
				break;
			}
			break;
			
		case Constants.ISPL:
			switch (whatToProcess.split(",")[0]) {
			case "o":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Select"
						+ "*FUNCTION*Omo*vis_con SET 0  \0", print_writers);
				CricketFunctions.DoadWriteCommandToSelectedViz(1,"-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Select$PlayerOftheMatch$Data$Select_Sponsor"
						+ "*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToSelectedViz(2,"-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Select$PlayerOftheMatch$Data$Select_Sponsor"
						+ "*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				break;
			case "t":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Select"
						+ "*FUNCTION*Omo*vis_con SET 2  \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToSelectedViz(2,"-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Select$Third_Umpire$1$Data$Side_1$SubText$Side1"
						+ "$group*ACTIVE SET 0 \0", print_writers);
				
				for(VariousText vt : VariousText) {
					if(vt.getVariousType().equalsIgnoreCase("THIRDUMPIRE") && vt.getUseThis().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Third_Umpire"
								+ "$1$Data$Side_1$SubText$txt_Sub*GEOM*TEXT SET " + vt.getVariousText() + "\0", print_writers);
					}else if(vt.getVariousType().equalsIgnoreCase("THIRDUMPIRE") && vt.getUseThis().toUpperCase().equalsIgnoreCase(CricketUtil.NO)) {
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Top_Align$Side" + WhichSide + "$Select_FooterType"
//							+ "$Info_Text$Data$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.
//							generateMatchSummaryStatus(WhichInning, matchAllData, CricketUtil.FULL, CricketUtil.BEAT).toUpperCase() + "\0", print_writers);
					}
				}
				break;	
			}
			switch(whatToProcess.split(",")[0]) {
			case "Alt_p":
				
				if(matchAllData.getSetup().getHomeTeam().getTeamName4().contains("KHILADI XI") || matchAllData.getSetup().getHomeTeam().getTeamName4().contains("MASTER 11")) {
					if(matchAllData.getSetup().getHomeTeam().getTeamName4().equalsIgnoreCase("KHILADI XI")) {
						homecolor = "KHILADI_XI";
					}else if(matchAllData.getSetup().getHomeTeam().getTeamName4().equalsIgnoreCase("MASTER 11")) {
						homecolor = "MASTER_XI";
					}
				}else {
					homecolor = matchAllData.getSetup().getHomeTeam().getTeamName4();
				}
				
				if(matchAllData.getSetup().getAwayTeam().getTeamName4().contains("KHILADI XI") || matchAllData.getSetup().getAwayTeam().getTeamName4().contains("MASTER 11")) {
					if(matchAllData.getSetup().getAwayTeam().getTeamName4().equalsIgnoreCase("KHILADI XI")) {
						awaycolor = "KHILADI_XI";
					}else if(matchAllData.getSetup().getAwayTeam().getTeamName4().equalsIgnoreCase("MASTER 11")) {
						awaycolor = "MASTER_XI";
					}
				}else {
					awaycolor = matchAllData.getSetup().getAwayTeam().getTeamName4();
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Toss$Home$img_Base2*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE2 + homecolor + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Toss$Away$img_Base2*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE2 + awaycolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Toss$Home$img_Logo*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_LOGOS_PATH + homecolor + "\0", print_writers);
						
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Toss$Away$img_Logo*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_LOGOS_PATH + awaycolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Toss"
						+ "$txt_Info*GEOM*TEXT SET " + whatToProcess.split(",")[2].split("-")[0] + " WON THE TIP-TOP & ELECTED TO " + 
						whatToProcess.split(",")[2].split("-")[1]+ "\0", print_writers);
				
				break;
			case "h":
				
				if(team.getTeamName4().contains("KHILADI XI") || team.getTeamName4().contains("MASTER 11")) {
					if(team.getTeamName4().equalsIgnoreCase("KHILADI XI")) {
						homecolor = "KHILADI_XI";
					}else if(team.getTeamName4().equalsIgnoreCase("MASTER 11")) {
						homecolor = "MASTER_XI";
					}
				}else {
					homecolor = team.getTeamName4();
				}
				
				if(inning.getBatting_team().getTeamName4().contains("KHILADI XI") || inning.getBatting_team().getTeamName4().contains("MASTER 11")) {
					if(inning.getBatting_team().getTeamName4().equalsIgnoreCase("KHILADI XI")) {
						awaycolor = "KHILADI_XI";
					}else if(inning.getBatting_team().getTeamName4().equalsIgnoreCase("MASTER 11")) {
						awaycolor = "MASTER_XI";
					}
				}else {
					awaycolor = inning.getBatting_team().getTeamName4();
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Right_Section$img_Base1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE1 + homecolor + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$HeaderBand$img_Base2*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE2 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1$txt_Sub*"
						+ "TEXTURE*IMAGE SET " + Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Logo*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + 
						"$img_Logo*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + awaycolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Name*GEOM*TEXT SET " + inning.getBatting_team().getTeamName1() + "\0", print_writers);
				if (inning.getTotalWickets() >= 10) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET " + inning.getTotalRuns() + "\0", print_writers);
				} else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET " + inning.getTotalRuns()+ " - "+ inning.getTotalWickets() + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " +" "+ CricketFunctions.OverBalls(inning.getTotalOvers(), inning.getTotalBalls()) + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + "HIGHLIGHTS" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
				break;
			case "Control_y":
				String pp = "";
				
				if(team.getTeamName4().contains("KHILADI XI") || team.getTeamName4().contains("MASTER 11")) {
					if(team.getTeamName4().equalsIgnoreCase("KHILADI XI")) {
						homecolor = "KHILADI_XI";
					}else if(team.getTeamName4().equalsIgnoreCase("MASTER 11")) {
						homecolor = "MASTER_XI";
					}
				}else {
					homecolor = team.getTeamName4();
				}
				
				if(inning.getBatting_team().getTeamName4().contains("KHILADI XI") || inning.getBatting_team().getTeamName4().contains("MASTER 11")) {
					if(inning.getBatting_team().getTeamName4().equalsIgnoreCase("KHILADI XI")) {
						awaycolor = "KHILADI_XI";
					}else if(inning.getBatting_team().getTeamName4().equalsIgnoreCase("MASTER 11")) {
						awaycolor = "MASTER_XI";
					}
				}else {
					awaycolor = inning.getBatting_team().getTeamName4();
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Right_Section$img_Base1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE1 + homecolor + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$HeaderBand$img_Base2*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE2 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1$txt_Sub*"
						+ "TEXTURE*IMAGE SET " + Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Logo*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Logo*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + awaycolor + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Name*GEOM*TEXT SET " + inning.getBatting_team().getTeamName1() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				if(whatToProcess.split(",")[2].equalsIgnoreCase("p1")) {
					pp = CricketFunctions.getFirstPowerPlayScore(matchAllData,inning.getInningNumber(), matchAllData.getEventFile().getEvents());
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Sub*GEOM*TEXT SET " + "POWERPLAY 1 " +  "(OVERS " + inning.getFirstPowerplayStartOver() + " & " + 
							inning.getFirstPowerplayEndOver() + ") " + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET "  + pp.split(",")[0] + "\0", print_writers);
					
				}else if(whatToProcess.split(",")[2].equalsIgnoreCase("p2")) {
					pp=CricketFunctions.getSecPowerPlayScore(matchAllData, inning.getInningNumber(), matchAllData.getEventFile().getEvents());
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Sub*GEOM*TEXT SET " + "POWERPLAY 2 " +  "(OVER " + inning.getSecondPowerplayStartOver() + ") " + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET "  + pp.split(",")[0] + "\0", print_writers);
					
				}else if(whatToProcess.split(",")[2].equalsIgnoreCase("p3")) {
					pp=CricketFunctions.getThirdPowerPlayScore(matchAllData, inning.getInningNumber(), matchAllData.getEventFile().getEvents());
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Sub*GEOM*TEXT SET " + "POWERPLAY 3 " +  "(OVERS " + inning.getThirdPowerplayStartOver() + " TO " + 
							inning.getThirdPowerplayEndOver() + ") " + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET "  + pp.split(",")[0] + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$SubText$Side" + WhichSide 
						+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
				break;
			case "g":
				
				if(team.getTeamName4().contains("KHILADI XI") || team.getTeamName4().contains("MASTER 11")) {
					if(team.getTeamName4().equalsIgnoreCase("KHILADI XI")) {
						homecolor = "KHILADI_XI";
					}else if(team.getTeamName4().equalsIgnoreCase("MASTER 11")) {
						homecolor = "MASTER_XI";
					}
				}else {
					homecolor = team.getTeamName4();
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Right_Section$img_Base1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE1 + homecolor + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$HeaderBand$img_Base2*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE2 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1$txt_Sub*"
						+ "TEXTURE*IMAGE SET " + Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Logo*ACTIVE SET 1 \0", print_writers);
			
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Logo*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + homecolor + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Name*GEOM*TEXT SET " + bowlingCard.getPlayer().getTicker_name() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + CricketFunctions.OverBalls(bowlingCard.getOvers(), bowlingCard.getBalls()) + " - " + bowlingCard.getDots() 
						+ " - " + bowlingCard.getRuns() + " - " + bowlingCard.getWickets() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$SubText$Side" + WhichSide 
						+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
				break;
				
			case "y":
				
				if(team.getTeamName4().contains("KHILADI XI") || team.getTeamName4().contains("MASTER 11")) {
					if(team.getTeamName4().equalsIgnoreCase("KHILADI XI")) {
						homecolor = "KHILADI_XI";
					}else if(team.getTeamName4().equalsIgnoreCase("MASTER 11")) {
						homecolor = "MASTER_XI";
					}
				}else {
					homecolor = team.getTeamName4();
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Right_Section$img_Base1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE1 + homecolor+ "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$HeaderBand$img_Base2*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE2 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1$txt_Sub*"
						+ "TEXTURE*IMAGE SET " + Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Logo*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Logo*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + homecolor + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + "\0", print_writers);
				
				if(battingCard.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET "  + battingCard.getRuns() + "*" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET "  + battingCard.getRuns()+ "\0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + "(" + battingCard.getBalls() + ")" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + battingCard.getFours() +"  FOURS   " + battingCard.getSixes() + "  SIXES   " + 
						battingCard.getNines() + "  NINES" + "\0", print_writers);
				
				
				break;
			case ".":
				
				homecolor = "ISPL";
				String posOrNeg = "";
				//String tapeData = getchallenge(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Right_Section$img_Base1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE1 + homecolor+ "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$HeaderBand$img_Base2*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE2 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1$txt_Sub*"
						+ "TEXTURE*IMAGE SET " + Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Logo*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Logo*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + homecolor + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Name*GEOM*TEXT SET " + "50-50 OVER : " + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				for(Event evnt: matchAllData.getEventFile().getEvents()) {
					if(evnt.getEventInningNumber() == inning.getInningNumber()) {
						if(evnt.getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50)) {
							int bonus = 0;
							int challengeRuns = 0;
							challengeRuns = evnt.getEventTotalRunsInAnOver();
							bonus = evnt.getEventExtraRuns();
							posOrNeg = evnt.getEventExtra();
									
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
									+ "$txt_Runs*GEOM*TEXT SET "  + challengeRuns + " RUNS" + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
									+ "$txt_Balls*GEOM*TEXT SET " + "(" + posOrNeg + bonus + ")" + "\0", print_writers);
//							if((bonus*2) >= challengeRuns) {
//								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
//										+ "$txt_Balls*GEOM*TEXT SET " + "(+" + bonus + ")" + "\0", print_writers);
//							}else {
//								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
//										+ "$txt_Balls*GEOM*TEXT SET " + "(-" + bonus + ")" + "\0", print_writers);
//							}
						}
					}
				}
				break;
			case "/":
				
				String tapeData = getBowlerRunsOverbyOver(inning.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData);
				
				homecolor = "ISPL";
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Right_Section$img_Base1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE1 + homecolor+ "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$HeaderBand$img_Base2*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE2 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1$txt_Sub*"
						+ "TEXTURE*IMAGE SET " + Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Logo*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Logo*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + homecolor + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Name*GEOM*TEXT SET " + tapeData.split(",")[0] + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET "  + " TAPE BALL OVER : " + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + tapeData.split(",")[1] + " RUN" + CricketFunctions.Plural(Integer.valueOf(tapeData.split(",")[1])).toUpperCase() + " & " + tapeData.split(",")[2] + " WICKET" + 
						CricketFunctions.Plural(Integer.valueOf(tapeData.split(",")[2])).toUpperCase() + "\0", print_writers);
				break;	
			case "Shift_F":
				
				if(team.getTeamName4().contains("KHILADI XI") || team.getTeamName4().contains("MASTER 11")) {
					if(team.getTeamName4().equalsIgnoreCase("KHILADI XI")) {
						homecolor = "KHILADI_XI";
					}else if(team.getTeamName4().equalsIgnoreCase("MASTER 11")) {
						homecolor = "MASTER_XI";
					}
				}else {
					homecolor = team.getTeamName4();
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Right_Section$img_Base1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE1 + homecolor + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$HeaderBand$img_Base2*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE2 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1$txt_Sub*"
						+ "TEXTURE*IMAGE SET " + Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Logo*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + 
						"$img_Logo*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET " + battingCard.getRuns() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + battingCard.getBalls() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + battingCard.getHowOutText() + "\0", print_writers);
				
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
				
				break;
			case "Shift_O":
				
				if(team.getTeamName4().contains("KHILADI XI") || team.getTeamName4().contains("MASTER 11")) {
					if(team.getTeamName4().equalsIgnoreCase("KHILADI XI")) {
						homecolor = "KHILADI_XI";
					}else if(team.getTeamName4().equalsIgnoreCase("MASTER 11")) {
						homecolor = "MASTER_XI";
					}
				}else {
					homecolor = team.getTeamName4();
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Right_Section$img_Base1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE1 + homecolor + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$HeaderBand$img_Base2*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE2 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1$txt_Sub*"
						+ "TEXTURE*IMAGE SET " + Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Logo*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + 
						"$img_Logo*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET " + battingCard.getRuns() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + "(" + battingCard.getBalls() + ")" + "\0", print_writers);
				
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + battingCard.getHowOutText() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
				
				break;
			case "Control_k":
				
				if(team.getTeamName4().contains("KHILADI XI") || team.getTeamName4().contains("MASTER 11")) {
					if(team.getTeamName4().equalsIgnoreCase("KHILADI XI")) {
						homecolor = "KHILADI_XI";
					}else if(team.getTeamName4().equalsIgnoreCase("MASTER 11")) {
						homecolor = "MASTER_XI";
					}
				}else {
					homecolor = team.getTeamName4();
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Right_Section$img_Base1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE1 + homecolor + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$HeaderBand$img_Base2*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE2 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1$txt_Sub*"
						+ "TEXTURE*IMAGE SET " + Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Logo*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + 
						"$img_Logo*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Name*GEOM*TEXT SET " + "CURRENT PARTNERSHIP" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET " + partnership.getTotalRuns() + "*" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + partnership.getTotalBalls() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
				break;
			case "Shift_F4":
				
				if(team.getTeamName4().contains("KHILADI XI") || team.getTeamName4().contains("MASTER 11")) {
					if(team.getTeamName4().equalsIgnoreCase("KHILADI XI")) {
						homecolor = "KHILADI_XI";
					}else if(team.getTeamName4().equalsIgnoreCase("MASTER 11")) {
						homecolor = "MASTER_XI";
					}
				}else {
					homecolor = team.getTeamName4();
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Right_Section$img_Base1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE1 + homecolor + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$HeaderBand$img_Base2*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE2 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1$txt_Sub*"
						+ "TEXTURE*IMAGE SET " + Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Logo*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + 
						"$img_Logo*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + homecolor + "\0", print_writers);
				
				if(partnership.getPartnershipNumber() == 1) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET "+ partnership.getPartnershipNumber() + "st WICKET PARTNERSHIP" + "\0", print_writers);
					
				}else if(partnership.getPartnershipNumber() == 2) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + partnership.getPartnershipNumber() +"nd WICKET PARTNERSHIP" + "\0", print_writers);
					
				}else if(partnership.getPartnershipNumber() == 3) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET "+ partnership.getPartnershipNumber() + "rd WICKET PARTNERSHIP" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + partnership.getPartnershipNumber() +"th WICKET PARTNERSHIP" + "\0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET " + partnership.getTotalRuns() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + partnership.getTotalBalls() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
						+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
				break;
			case "k":
				
				if(team.getTeamName4().contains("KHILADI XI") || team.getTeamName4().contains("MASTER 11")) {
					if(team.getTeamName4().equalsIgnoreCase("KHILADI XI")) {
						homecolor = "KHILADI_XI";
					}else if(team.getTeamName4().equalsIgnoreCase("MASTER 11")) {
						homecolor = "MASTER_XI";
					}
				}else {
					homecolor = team.getTeamName4();
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Right_Section$img_Base1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE1 + homecolor + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$HeaderBand$img_Base2*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_BASE2 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1*TEXTURE*IMAGE SET " 
						+ Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + "$Data$img_text1$txt_Sub*"
						+ "TEXTURE*IMAGE SET " + Constants.ISPL_TEXT1 + homecolor + "\0", print_writers);
				
				if(bug.getSponsor() != null) {
					if(config.getSecondaryIpAddress()!= null) {
						CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
								+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
							+ "$img_Sponsor*ACTIVE SET 1 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$SubText$Side" + WhichSide 
							+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
				}
				
				if(bug.getFlag() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$img_Logo*ACTIVE SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + 
							"$img_Logo*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + homecolor + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$img_Flag*ACTIVE SET 0 \0", print_writers);
				}
				
				if(bug.getText4() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Balls*GEOM*TEXT SET " + "(" + bug.getText4() + ")" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Balls*GEOM*TEXT SET " + "" + "\0", print_writers);
				}
				
				if(bug.getText3() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET " + bug.getText3() + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Runs*GEOM*TEXT SET " + "" + "\0", print_writers);
				}
				
				if(bug.getText2() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Sub*GEOM*TEXT SET " + bug.getText2() + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Sub*GEOM*TEXT SET " + "" + "\0", print_writers);
				}
				
				if(bug.getText1() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + bug.getText1() + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
							+ "$txt_Name*GEOM*TEXT SET " + "" + "\0", print_writers);
				}
				
				
				break;
			}
			break;	
		case Constants.BENGAL_T20:
			switch(whatToProcess.split(",")[0]) {
				case "6":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$All_LowerThirds$gfx_Counter$DataGrp"
							+ "$Units$Side1$fig_UnitOutline*GEOM*TEXT SET "+this_data_str.get(0).split(",")[2] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$All_LowerThirds$gfx_Counter$DataGrp"
							+ "$Units$Side1$fig_Unit*GEOM*TEXT SET " +this_data_str.get(0).split(",")[2]+ "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$All_LowerThirds$gfx_Counter$DataGrp"
							+ "$Tenths$Side1$fig_TenthsOutline*GEOM*TEXT SET "+this_data_str.get(0).split(",")[1] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$All_LowerThirds$gfx_Counter$DataGrp"
							+ "$Tenths$Side1$fig_Tenths*GEOM*TEXT SET "+this_data_str.get(0).split(",")[1] + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$All_LowerThirds$gfx_Counter$DataGrp"
							+ "$Hundreths$Side1$fig_HundrethsOutline*GEOM*TEXT SET "+this_data_str.get(0).split(",")[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$All_LowerThirds$gfx_Counter$DataGrp"
							+ "$Hundreths$Side1$fig_Hundreths*GEOM*TEXT SET " +this_data_str.get(0).split(",")[0]+ "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$All_LowerThirds$gfx_Counter$DataGrp"
							+ "$Units$Side2$fig_UnitOutline*GEOM*TEXT SET "+this_data_str.get(1).split(",")[2] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$All_LowerThirds$gfx_Counter$DataGrp"
							+ "$Units$Side2$fig_Unit*GEOM*TEXT SET " +this_data_str.get(1).split(",")[2]+ "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$All_LowerThirds$gfx_Counter$DataGrp"
							+ "$Tenths$Side2$fig_TenthsOutline*GEOM*TEXT SET "+this_data_str.get(1).split(",")[1] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$All_LowerThirds$gfx_Counter$DataGrp"
							+ "$Tenths$Side2$fig_Tenths*GEOM*TEXT SET "+this_data_str.get(1).split(",")[1] + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$All_LowerThirds$gfx_Counter$DataGrp"
							+ "$Hundreths$Side2$fig_HundrethsOutline*GEOM*TEXT SET "+this_data_str.get(1).split(",")[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$All_LowerThirds$gfx_Counter$DataGrp"
							+ "$Hundreths$Side2$fig_Hundreths*GEOM*TEXT SET " +this_data_str.get(1).split(",")[0]+ "\0", print_writers);
				break;
				case "Alt_p":
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$All_TossBug$In_Out$Data"
						+ "$txt_Info*GEOM*TEXT SET " + whatToProcess.split(",")[2].split("-")[0] + " WON THE TOSS & ELECTED TO " + 
						whatToProcess.split(",")[2].split("-")[1]+ "\0", print_writers);
					
				 break;
				case "Control_Shift_U": 
					if(config.getPrimaryIpAddress().equalsIgnoreCase(Constants.LOCALHOST)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$img_Image"
								+ "*TEXTURE*IMAGE SET " + Constants.BENGAL_LOCAL_PHOTO_PATH + inning.getBatting_team().getTeamName4() 
								+ "\\\\" +battingCard.getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION+ "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$img_Image"
								+ "*TEXTURE*IMAGE SET " + "\\\\" + config.getPrimaryIpAddress() + Constants.BENGAL_PHOTO_PATH + 
								inning.getBatting_team().getTeamName4() + "\\\\" + battingCard.getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION+ "\0", print_writers);
					}
					if(battingCard.getPlayer().getSurname() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$PlayerName$txt_FirstName"
								+ "*GEOM*TEXT SET " + battingCard.getPlayer().getFirstname()+ "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$PlayerName$txt_LastName"
								+ "*GEOM*TEXT SET " + battingCard.getPlayer().getSurname()+ "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$PlayerName$txt_FirstName"
								+ "*GEOM*TEXT SET " + ""+ "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$PlayerName$txt_LastName"
								+ "*GEOM*TEXT SET " + battingCard.getPlayer().getFirstname()+ "\0", print_writers);
					}
					switch (whatToProcess.split(",")[3].toUpperCase()) {
					case "SCORE":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Select_TextType"
								+ "*FUNCTION*Omo*vis_con SET " + "0" + "\0", print_writers);
						if(battingCard.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Big_Score$Text1$txt_Figure_Outline"
									+ "*GEOM*TEXT SET " + battingCard.getRuns()+"*" + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Big_Score$Text1$txt_Figure"
									+ "*GEOM*TEXT SET " + battingCard.getRuns() +"*" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Big_Score$Text1$txt_Figure_Outline"
									+ "*GEOM*TEXT SET " + battingCard.getRuns() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Big_Score$Text1$txt_Figure"
									+ "*GEOM*TEXT SET " + battingCard.getRuns() + "\0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Big_Score$Text2$txt_Figure_Outline"
								+ "*GEOM*TEXT SET " + battingCard.getBalls() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Big_Score$Text2$noname"
								+ "*GEOM*TEXT SET " + battingCard.getBalls() + "\0", print_writers);
						break;

					case "STRIKERATE":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Select_TextType"
								+ "*FUNCTION*Omo*vis_con SET " + "1" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Stat_Title$Text1$txt_Figure_Outline"
								+ "*GEOM*TEXT SET " +CricketFunctions.generateStrikeRate(battingCard.getRuns(), battingCard.getBalls(), 1)+ "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Stat_Title$Text1$txt_Figure"
								+ "*GEOM*TEXT SET " + CricketFunctions.generateStrikeRate(battingCard.getRuns(), battingCard.getBalls(), 1) + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Stat_Title$txt_Title"
								+ "*GEOM*TEXT SET " + "STRIKE RATE" + "\0", print_writers);
						break;
					}
					break;
				case "Control_Shift_V":
					if(config.getPrimaryIpAddress().equalsIgnoreCase(Constants.LOCALHOST)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$img_Image"
								+ "*TEXTURE*IMAGE SET " + Constants.BENGAL_LOCAL_PHOTO_PATH + inning.getBowling_team().getTeamName4() 
								+ "\\\\" +bowlingCard.getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION+ "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$img_Image"
								+ "*TEXTURE*IMAGE SET " + "\\\\" + config.getPrimaryIpAddress() + Constants.BENGAL_PHOTO_PATH + 
								inning.getBowling_team().getTeamName4() + "\\\\" + bowlingCard.getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION+ "\0", print_writers);
					}
					if(bowlingCard.getPlayer().getSurname() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$PlayerName$txt_FirstName"
								+ "*GEOM*TEXT SET " + bowlingCard.getPlayer().getFirstname()+ "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$PlayerName$txt_LastName"
								+ "*GEOM*TEXT SET " + bowlingCard.getPlayer().getSurname()+ "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$PlayerName$txt_FirstName"
								+ "*GEOM*TEXT SET " + ""+ "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$PlayerName$txt_LastName"
								+ "*GEOM*TEXT SET " + bowlingCard.getPlayer().getFirstname()+ "\0", print_writers);
					}
					switch (whatToProcess.split(",")[3].toUpperCase()) {
					case "FIGURE":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Select_TextType"
								+ "*FUNCTION*Omo*vis_con SET " + "0" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Big_Score$Text1$txt_Figure_Outline"
								+ "*GEOM*TEXT SET " + bowlingCard.getWickets() +"-"+ bowlingCard.getRuns() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Big_Score$Text1$txt_Figure"
								+ "*GEOM*TEXT SET " + bowlingCard.getWickets() +"-"+ bowlingCard.getRuns() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Big_Score$Text2$txt_Figure_Outline"
								+ "*GEOM*TEXT SET " + CricketFunctions.OverBalls(bowlingCard.getOvers(), bowlingCard.getBalls()) + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Big_Score$Text2$noname"
								+ "*GEOM*TEXT SET " + CricketFunctions.OverBalls(bowlingCard.getOvers(), bowlingCard.getBalls()) + "\0", print_writers);
						break;

					case "ECONOMY":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Select_TextType"
								+ "*FUNCTION*Omo*vis_con SET " + "1" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Stat_Title$Text1$txt_Figure_Outline"
								+ "*GEOM*TEXT SET " +bowlingCard.getEconomyRate()+ "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Stat_Title$Text1$txt_Figure"
								+ "*GEOM*TEXT SET " + bowlingCard.getEconomyRate() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_All$gfx_Popup$Side"+WhichSide+"$Stat_Title$txt_Title"
								+ "*GEOM*TEXT SET " + "ECONOMY" + "\0", print_writers);
						break;
					}
					break;
			}
			break;
		}
		
		return Constants.OK;
	}

	public String populateMiniScorecard(int WhichSide, String whatToProcess, MatchAllData matchAllData) {
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			status = "populateMiniScorecard match is null Or Inning is null";
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1]))
					.findAny().orElse(null);
			if(inning == null) {
				return "populateMiniScorecard Inning is null";
			}
		}
		if(populateMiniBody(WhichSide, whatToProcess.split(",")[0],matchAllData, Integer.valueOf(whatToProcess.split(",")[1])) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	public String populateMiniBowlingcard(int WhichSide, String whatToProcess, MatchAllData matchAllData) {
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			status = "populateMiniBowlingcard match is null Or Inning is null";
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1]))
					.findAny().orElse(null);
			if(inning == null) {
				return "populateMiniBowlingcard Inning is null";
			}
		}
		if(populateMiniBody(WhichSide, whatToProcess.split(",")[0],matchAllData, Integer.valueOf(whatToProcess.split(",")[1])) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	
	public String populateMiniBody(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) {
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			switch(whatToProcess) {
			case "Shift_F1":
				int battingSize=0;
				cont_name = "";
				omo_num = 0;
				rowId = 0;
				
				if(inning.getBatting_team().getTeamName4().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$img_Flag1*ACTIVE SET 0 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$img_Flag1*ACTIVE SET 1 \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Batting$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName4() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Batting$txt_FirstName*GEOM*TEXT SET " + inning.getBatting_team().getTeamName1() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				
				
				for(int i=1; i<=inning.getBattingCard().size(); i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row"+i+"*ACTIVE SET 1 \0", print_writers);	
				}
				
				Collections.sort(inning.getBattingCard());
				
				for (BattingCard bc : inning.getBattingCard()) {
					
					rowId = rowId + 1;
					
					switch (bc.getStatus().toUpperCase()) {
						case CricketUtil.OUT:
							omo_num = 0;
							cont_name = "$Players_Dehighlight";
							battingSize = battingSize + 1;
							break;
						case CricketUtil.NOT_OUT:
							omo_num = 1;
							cont_name = "$Players_Highlight";
							battingSize = battingSize + 1;
							break;
						}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$DataGrp*FUNCTION*Grid*num_row SET " + battingSize + " \0", print_writers);
					
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row"+rowId+"$Select_Row_Type*FUNCTION*Omo*vis_con SET " + String.valueOf(omo_num) + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row" + rowId + cont_name + "$txt_Name*GEOM*TEXT SET " + bc.getPlayer().getFull_name() + " \0", print_writers);
					
					if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Batting$Row" + rowId + cont_name + "$txt_Out*GEOM*TEXT SET " + "" + " \0", print_writers);
					}else if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.OUT)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Batting$Row" + rowId + cont_name + "$txt_Out*GEOM*TEXT SET " +"" + " \0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row" + rowId + cont_name + "$obj_Divider*ACTIVE SET 0  \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row" + rowId + cont_name + "$fig_Runs*GEOM*TEXT SET " + bc.getRuns() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row" + rowId + cont_name + "$fig_Out*GEOM*TEXT SET " + bc.getBalls() + " \0", print_writers);
				}
				
				break;
			case "Shift_F2":
				
				int bowling_size = 1;
				rowId = 1;
				cont_name = "";
				omo_num = 0;
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				
				if(inning.getBowling_team().getTeamName4().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$img_Flag1*ACTIVE SET 0 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$img_Flag1*ACTIVE SET 1 \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Bowling$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Bowling$txt_FirstName*GEOM*TEXT SET " + inning.getBowling_team().getTeamName1() + " \0", print_writers);
				
				for(int i=1; i<=inning.getBowlingCard().size(); i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row"+(i)+"*ACTIVE SET 1 \0", print_writers);
				}
				
				for (BowlingCard boc : inning.getBowlingCard()) {
					if(boc.getRuns() > 0 || ((boc.getOvers()*6)+boc.getBalls()) > 0) {
						bowling_size=bowling_size + 1;
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Bowling$DataGrp*FUNCTION*Grid*num_row SET " + bowling_size + " \0", print_writers);
					}
					switch (boc.getStatus().toUpperCase()) {
					case (CricketUtil.OTHER + CricketUtil.BOWLER):
						omo_num = 2;
						cont_name = "$Players_Dehighlight";
						break;
					case (CricketUtil.LAST + CricketUtil.BOWLER):
						omo_num = 2;
						cont_name = "$Players_Dehighlight";
						break;
					case (CricketUtil.CURRENT + CricketUtil.BOWLER):
						omo_num = 3;
						cont_name = "$Players_Highlight";
						break;
					}
					
					rowId = rowId + 1;
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row"+rowId+"$Select_Row_Type*FUNCTION*Omo*vis_con SET " + String.valueOf(omo_num) + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row" + rowId + cont_name + "$txt_Name*GEOM*TEXT SET " + boc.getPlayer().getTicker_name() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row" + rowId + cont_name + "$fig_Overs*GEOM*TEXT SET " + CricketFunctions.OverBalls(boc.getOvers(),boc.getBalls()) + " \0", print_writers);

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row" + rowId + cont_name + "$fig_Maidens*GEOM*TEXT SET " + boc.getMaidens() + " \0", print_writers);

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row" + rowId + cont_name + "$fig_Runs*GEOM*TEXT SET " + boc.getRuns() + " \0", print_writers);

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row" + rowId + cont_name + "$fig_Wickets*GEOM*TEXT SET " + boc.getWickets() + " \0", print_writers);
					
					if(boc.getEconomyRate() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Bowling$Row" + rowId + cont_name + "$fig_Economy*GEOM*TEXT SET " + boc.getEconomyRate() + " \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Bowling$Row" + rowId + cont_name + "$fig_Economy*GEOM*TEXT SET " + "-" + " \0", print_writers);
					}
					

				}
				break;
			}
			break;
		case Constants.NPL:
			switch(whatToProcess) {
			case "Shift_F1":
				int battingSize=0;
				cont_name = "";
				omo_num = 0;
				rowId = 0;
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Batting$img_Flag*TEXTURE*IMAGE SET " + Constants.NPL_LOGO_PATH + inning.getBatting_team().getTeamBadge() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Batting$txt_FirstName*GEOM*TEXT SET " + inning.getBatting_team().getTeamName1() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				
				
				for(int i=1; i<=inning.getBattingCard().size(); i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row"+i+"*ACTIVE SET 1 \0", print_writers);	
				}
				
				Collections.sort(inning.getBattingCard());
				
				for (BattingCard bc : inning.getBattingCard()) {
					
					rowId = rowId + 1;
					
					switch (bc.getStatus().toUpperCase()) {
						case CricketUtil.OUT:
							omo_num = 0;
							cont_name = "$Players_Dehighlight";
							battingSize = battingSize + 1;
							break;
						case CricketUtil.NOT_OUT:
							omo_num = 1;
							cont_name = "$Players_Highlight";
							battingSize = battingSize + 1;
							break;
						}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$DataGrp*FUNCTION*Grid*num_row SET " + battingSize + " \0", print_writers);
					
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row"+rowId+"$Select_Row_Type*FUNCTION*Omo*vis_con SET " + String.valueOf(omo_num) + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row" + rowId + cont_name + "$txt_Name*GEOM*TEXT SET " + bc.getPlayer().getFull_name() + " \0", print_writers);
					
					if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Batting$Row" + rowId + cont_name + "$txt_Out*GEOM*TEXT SET " + "" + " \0", print_writers);
					}else if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.OUT)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Batting$Row" + rowId + cont_name + "$txt_Out*GEOM*TEXT SET " +"" + " \0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row" + rowId + cont_name + "$obj_Divider*ACTIVE SET 0  \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row" + rowId + cont_name + "$fig_Runs*GEOM*TEXT SET " + bc.getRuns() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row" + rowId + cont_name + "$fig_Out*GEOM*TEXT SET " + bc.getBalls() + " \0", print_writers);
				}
				
				break;
				
			case "Alt_F1":
				int omo_num = 0;
				int row_id = 0, counter = 0;
				String MatchNam = "";
				boolean playerFound = false;
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Batting$img_Flag*TEXTURE*IMAGE SET " + Constants.NPL_LOGO_PATH + team.getTeamBadge() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Batting$txt_FirstName*GEOM*TEXT SET " + player.getFull_name() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				
				for(int i=1; i<=13; i++) {
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row" + i + "*ACTIVE SET 0\0", print_writers);
				}
				
				for(HeadToHead h2h : headToHead) {
					if(h2h.getPlayerId() == player.getPlayerId() && h2h.getTeam().getTeamName4().equalsIgnoreCase(team.getTeamName4())) {
						row_id++;
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Batting$DataGrp*FUNCTION*Grid*num_row SET " + row_id + " \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Batting$Row" + row_id + "*ACTIVE SET 1\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Batting$Row" + row_id + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 0\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Batting$Row" + row_id + "$Players_Dehighlight$txt_Name*GEOM*TEXT SET v " + h2h.getOpponentTeam().getTeamName3() + " \0", print_writers);
						
						MatchNam = h2h.getMatchFileName();

						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Batting$Row" + row_id + "$Players_Dehighlight$txt_Name*GEOM*TEXT SET v " + h2h.getOpponentTeam().getTeamName3() + " \0", print_writers);
						
						if(h2h.getInningStarted().contains("Y")) {
							if(h2h.getDismissed().contains("N")) {
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
										+ "$Batting$Row" + row_id + "$Players_Dehighlight$fig_Runs*GEOM*TEXT SET " + h2h.getRuns() + " \0", print_writers);
								
							}else if(h2h.getDismissed().contains("Y")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
										+ "$Batting$Row" + row_id + "$Players_Dehighlight$fig_Runs*GEOM*TEXT SET " + h2h.getRuns() + " \0", print_writers);
							}
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Batting$Row" + row_id + "$Players_Dehighlight$fig_Out*GEOM*TEXT SET " + h2h.getBallsFaced() + " \0", print_writers);
							
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Batting$Row" + row_id + "$Players_Dehighlight$fig_Out*GEOM*TEXT SET \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Batting$Row" + row_id + "$Players_Dehighlight$fig_Runs*GEOM*TEXT SET DNB\0", print_writers);
						}
						counter = 0;
					}else if(h2h.getTeam().getTeamName4().equalsIgnoreCase(team.getTeamName4())) {
						if(!MatchNam.equalsIgnoreCase(h2h.getMatchFileName()) && counter <= 11) {
							MatchNam = h2h.getMatchFileName();
							counter = 1;
						}else if(counter == 11) {
								row_id++;
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
										+ "$Batting$DataGrp*FUNCTION*Grid*num_row SET " + row_id + " \0", print_writers);
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
										+ "$Batting$Row" + row_id + "*ACTIVE SET 1\0", print_writers);
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
										+ "$Batting$Row" + row_id + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 0\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
										+ "$Batting$Row" + row_id + "$Players_Dehighlight$txt_Name*GEOM*TEXT SET v " + h2h.getOpponentTeam().getTeamName3() + " \0", print_writers);
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
										+ "$Batting$Row" + row_id + "$Players_Dehighlight$fig_Out*GEOM*TEXT SET \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
										+ "$Batting$Row" + row_id + "$Players_Dehighlight$fig_Runs*GEOM*TEXT SET DNP\0", print_writers);
								
								counter = 0;
						}else {
							counter++;
						}
					}
				}
				for(BattingCard bc : inning.getBattingCard()) {
					if(bc.getPlayerId() == player.getPlayerId()) {
						row_id++;
						playerFound = true;
						if(bc.getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Batting$DataGrp*FUNCTION*Grid*num_row SET " + row_id + " \0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Batting$Row" + row_id + "*ACTIVE SET 1\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Batting$Row" + row_id + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 1\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Batting$Row" + row_id + "$Players_Highlight$txt_Name*GEOM*TEXT SET v " + inning.getBowling_team().getTeamName3() + " \0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Batting$Row" + row_id + "$Players_Highlight$fig_Out*GEOM*TEXT SET \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Batting$Row" + row_id + "$Players_Highlight$fig_Runs*GEOM*TEXT SET DNB\0", print_writers);
							
						}else {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Batting$DataGrp*FUNCTION*Grid*num_row SET " + row_id + " \0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Batting$Row" + row_id + "*ACTIVE SET 1\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Batting$Row" + row_id + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 1\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Batting$Row" + row_id + "$Players_Highlight$txt_Name*GEOM*TEXT SET v " + inning.getBowling_team().getTeamName3() + " \0", print_writers);
							
							if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
										+ "$Batting$Row" + row_id + "$Players_Highlight$fig_Runs*GEOM*TEXT SET " + bc.getRuns() + "\0", print_writers);
								
							}else {
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
										+ "$Batting$Row" + row_id + "$Players_Highlight$fig_Runs*GEOM*TEXT SET " + bc.getRuns() + "\0", print_writers);
							}
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Batting$Row" + row_id + "$Players_Highlight$fig_Out*GEOM*TEXT SET " + bc.getBalls() + "\0", print_writers);
							
						}
					}
				}
				if(!playerFound) {
					row_id++;
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$DataGrp*FUNCTION*Grid*num_row SET " + row_id + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row" + row_id + "*ACTIVE SET 1\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row" + row_id + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 1\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row" + row_id + "$Players_Highlight$txt_Name*GEOM*TEXT SET v " + inning.getBowling_team().getTeamName3() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row" + row_id + "$Players_Highlight$fig_Out*GEOM*TEXT SET \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row" + row_id + "$Players_Highlight$fig_Runs*GEOM*TEXT SET DNP\0", print_writers);
					
				}
				break;
				
			case "Shift_F2":
				
				int bowling_size = 0;
				rowId = 0;
				
				cont_name = "";
				omo_num = 0;
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Bowling$img_Flag*TEXTURE*IMAGE SET " + Constants.NPL_LOGO_PATH + inning.getBowling_team().getTeamBadge() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Bowling$txt_FirstName*GEOM*TEXT SET " + inning.getBowling_team().getTeamName1() + " \0", print_writers);
				
				for(int i=1; i<=inning.getBowlingCard().size(); i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row"+(i)+"*ACTIVE SET 1 \0", print_writers);
				}
				
				for (BowlingCard boc : inning.getBowlingCard()) {
					if(boc.getRuns() > 0 || ((boc.getOvers()*6)+boc.getBalls()) > 0) {
						bowling_size=bowling_size + 1;
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Bowling$DataGrp*FUNCTION*Grid*num_row SET " + bowling_size + " \0", print_writers);
					}
					
					if(inning.getInningStatus().equalsIgnoreCase(CricketUtil.PAUSE)) {
						omo_num = 0;
						cont_name = "$Players_Dehighlight";
					}else {
						switch (boc.getStatus().toUpperCase()) {
						case (CricketUtil.OTHER + CricketUtil.BOWLER):
							omo_num = 0;
							cont_name = "$Players_Dehighlight";
							break;
						case (CricketUtil.LAST + CricketUtil.BOWLER):
							omo_num = 0;
							cont_name = "$Players_Dehighlight";
							break;
						case (CricketUtil.CURRENT + CricketUtil.BOWLER):
							omo_num = 1;
							cont_name = "$Players_Highlight";
							break;
						}
					}
					
					rowId = rowId + 1;
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row"+rowId+"$Select_Row_Type*FUNCTION*Omo*vis_con SET " + String.valueOf(omo_num) + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row" + rowId + cont_name + "$txt_Name*GEOM*TEXT SET " + boc.getPlayer().getTicker_name() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row" + rowId + cont_name + "$fig_Out*GEOM*TEXT SET " + CricketFunctions.OverBalls(boc.getOvers(),boc.getBalls()) + " \0", print_writers);

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row" + rowId + cont_name + "$fig_Runs*GEOM*TEXT SET " + boc.getWickets() + "-" + boc.getRuns() + " \0", print_writers);
				}
				break;
				
			case "Alt_F2":
				int row_no = 0, count = 0;
				String MatchName = "";
				rowId = 0;
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Bowling$img_Flag*TEXTURE*IMAGE SET " + Constants.NPL_LOGO_PATH + team.getTeamBadge() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Bowling$txt_FirstName*GEOM*TEXT SET " + player.getFull_name() + " \0", print_writers);
				
				
				for(HeadToHead h2h : headToHead) {
					if(h2h.getPlayerId() == player.getPlayerId() && h2h.getTeam().getTeamName4().equalsIgnoreCase(team.getTeamName4())) {
						row_no++;
						MatchName = h2h.getMatchFileName();
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Bowling$DataGrp*FUNCTION*Grid*num_row SET " + row_no + " \0", print_writers);
						
//						print_writer.println("-1 RENDERER*TREE*$Main$Select$MiniBatting_Bwling$Mini$Bowling$AllDataGrp$CardAll$Data$Data$DataGrp$Row"
//										+ row_no + "$RowAnimation$Select_Star*FUNCTION*Omo*vis_con SET 0 \0");
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Bowling$Row" + row_no + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Bowling$Row" + row_no + "$Players_Dehighlight$txt_Name*GEOM*TEXT SET v " + h2h.getOpponentTeam().getTeamName3() + " \0", print_writers);
						
						if(h2h.getBallsBowled() == 0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Bowling$Row" + row_no + "$Players_Dehighlight$fig_Out*GEOM*TEXT SET \0", print_writers);

							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Bowling$Row" + row_no + "$Players_Dehighlight$fig_Runs*GEOM*TEXT SET DNB\0", print_writers);
							
						}else {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Bowling$Row" + row_no + "$Players_Dehighlight$fig_Runs*GEOM*TEXT SET " + h2h.getWickets() +"-"+h2h.getRunsConceded() + "\0", print_writers);
							
							if(h2h.getBallsBowled()%6 == 0) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
										+ "$Bowling$Row" + row_no + "$Players_Dehighlight$fig_Out*GEOM*TEXT SET " + (h2h.getBallsBowled()/6) + "\0", print_writers);
							}else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
										+ "$Bowling$Row" + row_no + "$Players_Dehighlight$fig_Out*GEOM*TEXT SET " + (h2h.getBallsBowled()/6)+"."+h2h.getBallsBowled()%6 + "\0", print_writers);
							}
						}						
						count = 0;
					}else if(h2h.getTeam().getTeamName4().equalsIgnoreCase(team.getTeamName4())) {
						if(count == 11) {
							row_no++;
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Bowling$DataGrp*FUNCTION*Grid*num_row SET " + row_no + " \0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Bowling$Row" + row_no + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Bowling$Row" + row_no + "$Players_Dehighlight$txt_Name*GEOM*TEXT SET v " + h2h.getOpponentTeam().getTeamName3() + " \0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Bowling$Row" + row_no + "$Players_Dehighlight$fig_Out*GEOM*TEXT SET \0", print_writers);

							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Bowling$Row" + row_no + "$Players_Dehighlight$fig_Runs*GEOM*TEXT SET DNP\0", print_writers);
							
							count = 0;
						}else if(!MatchName.equalsIgnoreCase(h2h.getMatchFileName()) && count < 11) {
							MatchName = h2h.getMatchFileName();
							count = 1;
						}else {
							if(count==10) {
								count=0;
							}
							count++;
						}
					}
				}
				
				boolean playerIsInBoc = false;
				if(inning.getBowlingCard() != null) {
					for(BowlingCard boc : inning.getBowlingCard()) {
						if(boc.getPlayerId() == player.getPlayerId()) {
							playerIsInBoc = true;
							row_no++;
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Bowling$DataGrp*FUNCTION*Grid*num_row SET " + row_no + " \0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Bowling$Row" + row_no + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Bowling$Row" + row_no + "$Players_Highlight$txt_Name*GEOM*TEXT SET v " + inning.getBatting_team().getTeamName3() + " \0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Bowling$Row" + row_no + "$Players_Highlight$fig_Out*GEOM*TEXT SET " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + " \0", print_writers);

							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
									+ "$Bowling$Row" + row_no + "$Players_Highlight$fig_Runs*GEOM*TEXT SET " + boc.getWickets()+"-"+ boc.getRuns() + "\0", print_writers);
							
							break;
						}else {
							playerIsInBoc = false;
						}
					}
				}
				
				if(!playerIsInBoc) {
					row_no++;
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$DataGrp*FUNCTION*Grid*num_row SET " + row_no + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row" + row_no + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row" + row_no + "$Players_Highlight$txt_Name*GEOM*TEXT SET v " + inning.getBatting_team().getTeamName3() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row" + row_no + "$Players_Highlight$fig_Out*GEOM*TEXT SET \0", print_writers);

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row" + row_no + "$Players_Highlight$fig_Runs*GEOM*TEXT SET DNB\0", print_writers);
					
				}
				break;	
			}
			break;	
		case Constants.ISPL:
			switch(whatToProcess) {
			case "Alt_F7":
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
//						+ "$Batting$LooBase$img2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + 
//						inning.getBatting_team().getTeamName4() + " \0", print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
//						+ "$Batting$headerBnd$img1*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + 
//						inning.getBatting_team().getTeamName4() + " \0", print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
//						+ "$Batting$txt_FirstName*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + 
//						inning.getBatting_team().getTeamName4() + " \0", print_writers);
//				
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
//						+ "$Batting$img_Flag*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + inning.getBatting_team().getTeamName4() + " \0", print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
//						+ "$Batting$txt_FirstName*GEOM*TEXT SET " + inning.getBatting_team().getTeamName1() + " \0", print_writers);
				
				rowId = 1;
				for(int i=0; i<=leagueTable.getLeagueTeams().size()-1;i++) {
					rowId = rowId + 1;
					
					if(matchAllData.getSetup().getHomeTeam().getTeamName4().equalsIgnoreCase(leagueTable.getLeagueTeams().get(i).getTeamName())  
							|| matchAllData.getSetup().getAwayTeam().getTeamName4().equalsIgnoreCase(leagueTable.getLeagueTeams().get(i).getTeamName())) {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$MiniPointsTable$AllDataGrp$DataGrp"
								+ "$Row" + rowId + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
						containerName = "$Highlight";
						
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$MiniPointsTable$AllDataGrp$DataGrp"
								+ "$Row" + rowId + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
						containerName = "$Dehighlight";
					}
					
					if(leagueTable.getLeagueTeams().get(i).getQualifiedStatus().trim().equalsIgnoreCase("")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$MiniPointsTable$AllDataGrp$DataGrp"
								+ "$Row" + rowId + "$Select_Row_Type" + containerName + "$fig_Rank*GEOM*TEXT SET " + (rowId-1) + " \0", print_writers);
					}else if(leagueTable.getLeagueTeams().get(i).getQualifiedStatus().trim().equalsIgnoreCase("Q")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$MiniPointsTable$AllDataGrp$DataGrp"
								+ "$Row" + rowId + "$Select_Row_Type" + containerName + "$fig_Rank*GEOM*TEXT SET Q \0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$MiniPointsTable$AllDataGrp$DataGrp"
							+ "$Row" + rowId + "$Select_Row_Type" + containerName + "$txt_Name*GEOM*TEXT SET " + 
							leagueTable.getLeagueTeams().get(i).getTeamName() + " \0", print_writers);
					
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$MiniPointsTable$AllDataGrp$DataGrp"
							+ "$Row" + rowId + "$Select_Row_Type" + containerName + "$fig_Play*GEOM*TEXT SET " + 
							leagueTable.getLeagueTeams().get(i).getPlayed() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$MiniPointsTable$AllDataGrp$DataGrp"
							+ "$Row" + rowId + "$Select_Row_Type" + containerName + "$fig_Wins*GEOM*TEXT SET " + 
							leagueTable.getLeagueTeams().get(i).getWon() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$MiniPointsTable$AllDataGrp$DataGrp"
							+ "$Row" + rowId + "$Select_Row_Type" + containerName + "$fig_Points*GEOM*TEXT SET " + 
							leagueTable.getLeagueTeams().get(i).getPoints() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$MiniPointsTable$AllDataGrp$DataGrp"
							+ "$Row" + rowId + "$Select_Row_Type" + containerName + "$fig_NRR*GEOM*TEXT SET " + 
							String.format("%.2f", leagueTable.getLeagueTeams().get(i).getNetRunRate()) + " \0", print_writers);
					
				}
				break;
			case "Shift_F1":
				int battingSize=0;
				cont_name = "";
				omo_num = 0;
				rowId = 0;
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Batting$LooBase$img2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + 
						inning.getBatting_team().getTeamName4() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Batting$headerBnd$img1*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + 
						inning.getBatting_team().getTeamName4() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Batting$txt_FirstName*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + 
						inning.getBatting_team().getTeamName4() + " \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Batting$img_Flag*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + inning.getBatting_team().getTeamName4() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Batting$txt_FirstName*GEOM*TEXT SET " + inning.getBatting_team().getTeamName4() + " \0", print_writers);
				
				for(int i=1; i<=inning.getBattingCard().size(); i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row"+i+"*ACTIVE SET 1 \0", print_writers);
				}
				
				Collections.sort(inning.getBattingCard());
				
				for (BattingCard bc : inning.getBattingCard()) {
					
					rowId = rowId + 1;
					
					switch (bc.getStatus().toUpperCase()) {
						case CricketUtil.OUT:
							omo_num = 0;
							cont_name = "$Players_Dehighlight";
							text_name = "$Data";
							battingSize = battingSize + 1;
							break;
						case CricketUtil.NOT_OUT:
							omo_num = 1;
							cont_name = "$Players_Highlight";
							text_name = "$img_text2";
							battingSize = battingSize + 1;
							break;
						}
					
					if(CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
							bc.getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Batting$Row" + rowId + cont_name + "$Impact$Select*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}
					else if(CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
							bc.getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Batting$Row" + rowId + cont_name + "$Impact$Select*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Batting$Row" + rowId + cont_name + "$Impact$Select*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					}
					
					if(cont_name.equalsIgnoreCase("$Players_Highlight")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Batting$Row" + rowId + cont_name + "$Base$img2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + 
								inning.getBatting_team().getTeamName4() + " \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Batting$Row" + rowId + cont_name + text_name +"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + 
								inning.getBatting_team().getTeamName4() + " \0", print_writers);
					}else {
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
//								+ "$Batting$Row" + rowId + cont_name + text_name +"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT1 + 
//								inning.getBatting_team().getTeamName4() + " \0", print_writers);
					}
					
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$DataGrp*FUNCTION*Grid*num_row SET " + battingSize + " \0", print_writers);
					
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row"+rowId+"$Select_Row_Type*FUNCTION*Omo*vis_con SET " + String.valueOf(omo_num) + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row" + rowId + cont_name + "$txt_Name*GEOM*TEXT SET " + bc.getPlayer().getFull_name() + " \0", print_writers);
					
					if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Batting$Row" + rowId + cont_name + "$txt_Out*GEOM*TEXT SET " + "" + " \0", print_writers);
					}else if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.OUT)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Batting$Row" + rowId + cont_name + "$txt_Out*GEOM*TEXT SET " +"" + " \0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row" + rowId + cont_name + "$obj_Divider*ACTIVE SET 0  \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row" + rowId + cont_name + "$fig_Runs*GEOM*TEXT SET " + bc.getRuns() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Batting$Row" + rowId + cont_name + "$fig_Out*GEOM*TEXT SET " + bc.getBalls() + " \0", print_writers);
				}
				
				break;
			case "Shift_F2":
				int bowling_size = 1;
				rowId = 1;
				cont_name = "";
				omo_num = 0;
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Bowling$LooBase$img2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + 
						inning.getBowling_team().getTeamName4() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Bowling$headerBnd$img1*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + 
						inning.getBowling_team().getTeamName4() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Bowling$txt_FirstName*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + 
						inning.getBowling_team().getTeamName4() + " \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Bowling$img_Flag*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + inning.getBowling_team().getTeamName4() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Bowling$txt_FirstName*GEOM*TEXT SET " + inning.getBowling_team().getTeamName4() + " \0", print_writers);
				
				for(int i=1; i<=inning.getBowlingCard().size(); i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row"+(i+1)+"*ACTIVE SET 1 \0", print_writers);
				}
				
				for (BowlingCard boc : inning.getBowlingCard()) {
					if(boc.getRuns() > 0 || ((boc.getOvers()*6)+boc.getBalls()) > 0) {
						bowling_size=bowling_size + 1;
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Bowling$DataGrp*FUNCTION*Grid*num_row SET " + bowling_size + " \0", print_writers);
					}
					switch (boc.getStatus().toUpperCase()) {
					case (CricketUtil.OTHER + CricketUtil.BOWLER):
						omo_num = 2;
						cont_name = "$Players_Dehighlight";
						text_name = "$Data";
						break;
					case (CricketUtil.LAST + CricketUtil.BOWLER):
						omo_num = 2;
						cont_name = "$Players_Dehighlight";
						text_name = "$Data";
						break;
					case (CricketUtil.CURRENT + CricketUtil.BOWLER):
						omo_num = 3;
						cont_name = "$Players_Highlight";
						text_name = "$img_text2";
						break;
					}
					
					rowId = rowId + 1;
					
					if(CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
							boc.getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Bowling$Row" + rowId + cont_name + "$Impact$Select*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}
					else if(CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
							boc.getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Bowling$Row" + rowId + cont_name + "$Impact$Select*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Bowling$Row" + rowId + cont_name + "$Impact$Select*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					}
					
					if(cont_name.equalsIgnoreCase("$Players_Highlight")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Bowling$Row" + rowId + cont_name + "$Base$img2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + 
								inning.getBowling_team().getTeamName4() + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Bowling$Row" + rowId + cont_name + text_name +"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + 
								inning.getBowling_team().getTeamName4() + " \0", print_writers);
					}else {
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
//								+ "$Bowling$Row" + rowId + cont_name + text_name +"*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT1 + 
//								inning.getBowling_team().getTeamName4() + " \0", print_writers);
					}
					
					
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row"+rowId+"$Select_Row_Type*FUNCTION*Omo*vis_con SET " + String.valueOf(omo_num) + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row" + rowId + cont_name + "$txt_Name*GEOM*TEXT SET " + boc.getPlayer().getTicker_name() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row" + rowId + cont_name + "$fig_Overs*GEOM*TEXT SET " + CricketFunctions.OverBalls(boc.getOvers(),boc.getBalls()) + " \0", print_writers);

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row" + rowId + cont_name + "$fig_Maidens*GEOM*TEXT SET " + boc.getMaidens() + " \0", print_writers);

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row" + rowId + cont_name + "$fig_Runs*GEOM*TEXT SET " + boc.getRuns() + " \0", print_writers);

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$Row" + rowId + cont_name + "$fig_Wickets*GEOM*TEXT SET " + boc.getWickets() + " \0", print_writers);
					
					if(boc.getEconomyRate() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Bowling$Row" + rowId + cont_name + "$fig_Economy*GEOM*TEXT SET " + boc.getEconomyRate() + " \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
								+ "$Bowling$Row" + rowId + cont_name + "$fig_Economy*GEOM*TEXT SET " + "-" + " \0", print_writers);
					}
					

				}
				break;
			}
			break;
		case Constants.BENGAL_T20:
			switch(whatToProcess) {
			case "Control_Shift_E":
				rowId = 0;
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
						+ "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Title"
						+ "$txt_Name*GEOM*TEXT SET "+player.getFull_name()+" \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Title"
						+ "$txt_TeamName*GEOM*TEXT SET "+team.getTeamName1()+" \0", print_writers);
				
				for(BestStats stats : batter_data) {
					rowId++;
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
							+ "$Select_GraphicsType$Griff$Data$"+rowId+"$Select_Row_Type*FUNCTION*Omo*vis_con SET "+"0"+" \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
							+ rowId +"$Dehighlight$txt_TeamName*GEOM*TEXT SET "+"v "+stats.getPlayer().getFull_name()+" \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
							+ rowId +cont_name+"$txt_Star*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
							+ rowId +cont_name+"$txt_Runs*GEOM*TEXT SET "+stats.getRuns()+" \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
							+ rowId +cont_name+"$txt_Balls*GEOM*TEXT SET "+stats.getBalls()+" \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
						+ "$Select_GraphicsType$Griff$Select_Row*FUNCTION*Omo*vis_con SET "+batter_data.size()+" \0", print_writers);
				break;
			case "Alt_F2":
				rowId = 0;
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
						+ "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Title"
						+ "$txt_Name*GEOM*TEXT SET "+player.getFull_name()+" \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Title"
						+ "$txt_TeamName*GEOM*TEXT SET "+team.getTeamName1()+" \0", print_writers);
				
				for(BatBallGriff grif : griff) {
					rowId++;
					if(griff.get(rowId-1).getMatchNumber().equalsIgnoreCase(matchAllData.getMatch().getMatchFileName().replace(".json", ""))){
						cont_name = "$Highlight";
						omo_num= 1;
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +"$Highlight$txt_BatterName*GEOM*TEXT SET "+"v "+griff.get(rowId-1).getOpponentTeam().getTeamBadge()+" \0", print_writers);
					}else {
						cont_name = "$Dehighlight";
						omo_num= 0;
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +"$Dehighlight$txt_TeamName*GEOM*TEXT SET "+"v "+griff.get(rowId-1).getOpponentTeam().getTeamBadge()+" \0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
							+ "$Select_GraphicsType$Griff$Data$"+rowId+"$Select_Row_Type*FUNCTION*Omo*vis_con SET "+omo_num+" \0", print_writers);
					if(griff.get(rowId-1).getStatus().equalsIgnoreCase("DNB")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_Star*ACTIVE SET 0 \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_Runs*GEOM*TEXT SET "+""+" \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_Balls*GEOM*TEXT SET "+ "DNB"+" \0", print_writers);
					}else if(griff.get(rowId-1).getStatus().equalsIgnoreCase("BALL")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_Star*ACTIVE SET 0 \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_Runs*GEOM*TEXT SET "+griff.get(rowId-1).getWickets()+"-"+griff.get(rowId-1).getRunsConceded()+" \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_Balls*GEOM*TEXT SET "+ griff.get(rowId-1).getOversBowled()+" \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_Star*ACTIVE SET 0 \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_Runs*GEOM*TEXT SET "+""+" \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_Balls*GEOM*TEXT SET "+ "DNP"+" \0", print_writers);
					}
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
						+ "$Select_GraphicsType$Griff$Select_Row*FUNCTION*Omo*vis_con SET "+griff.size()+" \0", print_writers);
				
				break;
			case "Control_Shift_F":
				rowId = 0;
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
						+ "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Title"
						+ "$txt_Name*GEOM*TEXT SET "+player.getFull_name()+" \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Title"
						+ "$txt_TeamName*GEOM*TEXT SET "+team.getTeamName1()+" \0", print_writers);
				for(BestStats stats : bowler_data) {
					rowId++;
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
							+ rowId +cont_name+"$txt_Star*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
							+ rowId+"$Dehighlight$txt_TeamName*GEOM*TEXT SET "+"v "+stats.getPlayer().getFull_name()+" \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
							+ "$Select_GraphicsType$Griff$Data$"+rowId+"$Select_Row_Type*FUNCTION*Omo*vis_con SET "+"0"+" \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
							+ rowId +cont_name+"$txt_Runs*GEOM*TEXT SET "+stats.getRuns()+" \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
							+ rowId +cont_name+"$txt_Balls*GEOM*TEXT SET "+ stats.getBalls()+" \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
						+ "$Select_GraphicsType$Griff$Select_Row*FUNCTION*Omo*vis_con SET "+bowler_data.size()+" \0", print_writers);
				
				break;
			case "Alt_F1":
				rowId = 0;
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
						+ "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Title"
						+ "$txt_Name*GEOM*TEXT SET "+player.getFull_name()+" \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Title"
						+ "$txt_TeamName*GEOM*TEXT SET "+team.getTeamName1()+" \0", print_writers);
				
				for(BatBallGriff grif : griff) {
					rowId++;
					if(griff.get(rowId-1).getMatchNumber().equalsIgnoreCase(matchAllData.getMatch().getMatchFileName().replace(".json", ""))){
						cont_name = "$Highlight";
						omo_num= 1;
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_BatterName*GEOM*TEXT SET "+"v "+griff.get(rowId-1).getOpponentTeam().getTeamBadge()+" \0", print_writers);
					}else {
						cont_name = "$Dehighlight";
						omo_num= 0;
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_TeamName*GEOM*TEXT SET "+"v "+griff.get(rowId-1).getOpponentTeam().getTeamBadge()+" \0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
							+ "$Select_GraphicsType$Griff$Data$"+rowId+"$Select_Row_Type*FUNCTION*Omo*vis_con SET "+omo_num+" \0", print_writers);
					
					if(griff.get(rowId-1).getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
						if(griff.get(rowId-1).getHow_out() != null && !griff.get(rowId-1).getHow_out().trim().isEmpty() && 
								griff.get(rowId-1).getHow_out().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
									+ rowId +cont_name+"$txt_Runs*GEOM*TEXT SET "+griff.get(rowId-1).getRuns()+" \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
									+ rowId +cont_name+"$txt_Balls*GEOM*TEXT SET "+ griff.get(rowId).getBallsFaced()+" \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
									+ rowId +cont_name+"$txt_Runs*GEOM*TEXT SET "+""+" \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
									+ rowId +cont_name+"$txt_Balls*GEOM*TEXT SET "+ "DNB"+" \0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_Star*ACTIVE SET 0 \0", print_writers);
						
					}else if(griff.get(rowId-1).getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_Star*ACTIVE SET 0 \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_Runs*GEOM*TEXT SET "+griff.get(rowId-1).getRuns()+" \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_Balls*GEOM*TEXT SET "+ griff.get(rowId-1).getBallsFaced()+" \0", print_writers);
					}else if(griff.get(rowId-1).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_Star*ACTIVE SET 1 \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_Runs*GEOM*TEXT SET "+griff.get(rowId-1).getRuns()+" \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_Balls*GEOM*TEXT SET "+ griff.get(rowId-1).getBallsFaced()+" \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_Star*ACTIVE SET 0 \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_Runs*GEOM*TEXT SET "+""+" \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Griff$Rows$"
								+ rowId +cont_name+"$txt_Balls*GEOM*TEXT SET "+ "DNP"+" \0", print_writers);
					}
				}

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
						+ "$Select_GraphicsType$Griff$Select_Row*FUNCTION*Omo*vis_con SET "+griff.size()+" \0", print_writers);
				break;
			case "Alt_F7":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
						+ "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Standings$Data"
						+ "$Rows$TitleGrp$PointData$txt_Played*GEOM*TEXT SET P \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Standings$Data"
						+ "$Rows$TitleGrp$PointData$txt_Won*GEOM*TEXT SET W \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Standings$Data"
						+ "$Rows$TitleGrp$PointData$txt_Points*GEOM*TEXT SET PTS \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Standings$Data"
						+ "$Rows$TitleGrp$PointData$txt_NRR*GEOM*TEXT SET NRR \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Standings$Data"
						+ "$Title$txt_TeamName*GEOM*TEXT SET " + "STANDINGS" + " \0", print_writers);
				
				rowId = 0;
				for(int i=0; i<=leagueTable.getLeagueTeams().size()-1;i++) {
					
					rowId = rowId + 1;
					
					if(matchAllData.getSetup().getHomeTeam().getTeamName4().equalsIgnoreCase(leagueTable.getLeagueTeams().get(i).getTeamName())  
							|| matchAllData.getSetup().getAwayTeam().getTeamName4().equalsIgnoreCase(leagueTable.getLeagueTeams().get(i).getTeamName())) {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Standings$Data"
								+ "$Rows$" + rowId + "$Select_Highlight*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						containerName = "$Highlight";
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Standings$Data"
								+ "$Rows$" + rowId + "$Select_Highlight" + containerName + "$Base$img_Base2*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_BASE_PATH + "2/" + leagueTable.getLeagueTeams().get(i).getTeamName() + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Standings$Data"
								+ "$Rows$" + rowId + "$Select_Highlight" + containerName + "$Base$img_Base3*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_BASE_PATH + "3/" + leagueTable.getLeagueTeams().get(i).getTeamName() + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Standings$Data"
								+ "$Rows$" + rowId + "$Select_Highlight" + containerName + "$Base$img_Base1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_BASE_PATH + "1/" + leagueTable.getLeagueTeams().get(i).getTeamName() + " \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Standings$Data"
								+ "$Rows$" + rowId + "$Select_Highlight" + containerName + "$Data$img_Text1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_TEXT_PATH + "1/" + leagueTable.getLeagueTeams().get(i).getTeamName() + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Standings$Data"
								+ "$Rows$" + rowId + "$Select_Highlight" + containerName + "$Data$img_Text2*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_TEXT_PATH + "2/" + leagueTable.getLeagueTeams().get(i).getTeamName() + " \0", print_writers);
						
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Standings$Data"
								+ "$Rows$" + rowId + "$Select_Highlight*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						containerName = "$Dehighlight";
					}
					
					if(leagueTable.getLeagueTeams().get(i).getQualifiedStatus().trim().equalsIgnoreCase("")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Standings$Data"
								+ "$Rows$" + rowId + "$Select_Highlight" + containerName + "$Data$txt_Position*GEOM*TEXT SET " + (rowId) + " \0", print_writers);
					}else if(leagueTable.getLeagueTeams().get(i).getQualifiedStatus().trim().equalsIgnoreCase("Q")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Standings$Data"
								+ "$Rows$" + rowId + "$Select_Highlight" + containerName + "$Data$txt_Position*GEOM*TEXT SET " + "Q" + " \0", print_writers);
					}
					
					for(Team tm : Teams) {
						if(tm.getTeamName4().equalsIgnoreCase(leagueTable.getLeagueTeams().get(i).getTeamName())) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Standings$Data"
									+ "$Rows$" + rowId + "$Select_Highlight" + containerName + "$Data$txt_Name*GEOM*TEXT SET " + 
									tm.getTeamBadge() + " \0", print_writers);
						}
					}

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Standings$Data"
							+ "$Rows$" + rowId + "$Select_Highlight" + containerName + "$Data$txt_Played*GEOM*TEXT SET " + 
							leagueTable.getLeagueTeams().get(i).getPlayed() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Standings$Data"
							+ "$Rows$" + rowId + "$Select_Highlight" + containerName + "$Data$txt_Won*GEOM*TEXT SET " + 
							leagueTable.getLeagueTeams().get(i).getWon() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Standings$Data"
							+ "$Rows$" + rowId + "$Select_Highlight" + containerName + "$Data$txt_Points*GEOM*TEXT SET " + 
							leagueTable.getLeagueTeams().get(i).getPoints() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$Side" + WhichSide + "$Standings$Data"
							+ "$Rows$" + rowId + "$Select_Highlight" + containerName + "$Data$txt_NRR*GEOM*TEXT SET " + 
							String.format("%.2f", leagueTable.getLeagueTeams().get(i).getNetRunRate()) + " \0", print_writers);
				}
				break;
			case "Shift_F1":
				int battingSize=0;
				cont_name = "";
				omo_num = 0;
				rowId = 0;
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
						+ "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0", print_writers);

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
						+ "$Select_GraphicsType$Batting_Card$Data$Title$Out$In$txt_TeamName*GEOM*TEXT SET " + inning.getBatting_team().getTeamBadge() + " \0", print_writers);
				
				Collections.sort(inning.getBattingCard());
				
				for (BattingCard bc : inning.getBattingCard()) {
					
					rowId = rowId + 1;
					
					switch (bc.getStatus().toUpperCase()) {
					case CricketUtil.STILL_TO_BAT:
						if (bc.getHowOut() != null) {
							if (bc.getHowOut().toUpperCase().equalsIgnoreCase(CricketUtil.RETIRED_HURT)
									|| bc.getHowOut().toUpperCase().equalsIgnoreCase(CricketUtil.ABSENT_HURT)) {

								battingSize += 1;
								
								if(CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
										bc.getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
											+ "$Select_GraphicsType$Batting_Card$Data$Rows$Select_Row$" + rowId +"$Select_Row_Type$Out" + 
											"$Data$Name$Select_Impact*FUNCTION*Omo*vis_con SET 1\0", print_writers);
								}
								else if(CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
										bc.getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
											+ "$Select_GraphicsType$Batting_Card$Data$Rows$Select_Row$" + rowId +"$Select_Row_Type$Out" + 
											"$Data$Name$Select_Impact*FUNCTION*Omo*vis_con SET 1\0", print_writers);
								}else {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
											+ "$Select_GraphicsType$Batting_Card$Data$Rows$Select_Row$" + rowId +"$Select_Row_Type$Out" + 
											"$Data$Name$Select_Impact*FUNCTION*Omo*vis_con SET 0\0", print_writers);
								}
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
										+ "$Select_GraphicsType$Batting_Card$Data$Rows$Select_Row$" + rowId  +"$Select_Row_Type*FUNCTION*Omo*vis_con SET 0\0", print_writers);
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
										+ "$Select_GraphicsType$Batting_Card$Data$Rows$Select_Row*FUNCTION*Omo*vis_con SET " + battingSize + " \0", print_writers);
								

								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
										+ "$Select_GraphicsType$Batting_Card$Data$Rows$Select_Row$" + rowId  +"$BattingData$Select_Row_Type$" + 
										cont_name + "$Data$Name$txt_BatterName*GEOM*TEXT SET " + bc.getPlayer().getFull_name() + " \0", print_writers);
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
										+ "$Select_GraphicsType$Batting_Card$Data$Rows$Select_Row$" + rowId +"$BattingData$Select_Row_Type" + 
										cont_name + "$txt_Runs*GEOM*TEXT SET " + bc.getRuns() + " \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
										+ "$Select_GraphicsType$Batting_Card$Data$Rows$Select_Row$" + rowId +"$BattingData$Select_Row_Type" + 
										cont_name + "$txt_Balls*GEOM*TEXT SET " + bc.getBalls() + " \0", print_writers);

							}
						}
						break;
					default:
						switch (bc.getStatus().toUpperCase()) {
						case CricketUtil.OUT:
							omo_num = 0;
							cont_name = "$Out";
							text_name = "$Data";
							battingSize = battingSize + 1;
							break;
						case CricketUtil.NOT_OUT:
							omo_num = 1;
							cont_name = "$Not_Out";
							text_name = "$img_Text1";
							battingSize = battingSize + 1;
							break;
						}
						
						
						if(CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
								bc.getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
									+ "$Select_GraphicsType$Batting_Card$Data$Rows$Select_Row$" + rowId +"$Select_Row_Type" + 
									cont_name + "$Data$Name$Select_Impact*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						}
						else if(CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
								bc.getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
									+ "$Select_GraphicsType$Batting_Card$Data$Rows$Select_Row$" + rowId +"$Select_Row_Type" + 
									cont_name + "$Data$Name$Select_Impact*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
									+ "$Select_GraphicsType$Batting_Card$Data$Rows$Select_Row$" + rowId +"$Select_Row_Type" + 
									cont_name + "$Data$Name$Select_Impact*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
								+ "$Select_GraphicsType$Batting_Card$Data$Rows$Select_Row$" + rowId  +"$Select_Row_Type*FUNCTION*Omo*vis_con SET " + omo_num + " \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
								+ "$Select_GraphicsType$Batting_Card$Data$Rows$Select_Row*FUNCTION*Omo*vis_con SET " + battingSize + " \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
								+ "$Select_GraphicsType$Batting_Card$Data$Rows$Select_Row$" + rowId  +"$BattingData$Select_Row_Type$" + 
								cont_name + "$Data$Name$txt_BatterName*GEOM*TEXT SET " + bc.getPlayer().getFull_name() + " \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
								+ "$Select_GraphicsType$Batting_Card$Data$Rows$Select_Row$" + rowId +"$BattingData$Select_Row_Type" + 
								cont_name + "$txt_Runs*GEOM*TEXT SET " + bc.getRuns() + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
								+ "$Select_GraphicsType$Batting_Card$Data$Rows$Select_Row$" + rowId +"$BattingData$Select_Row_Type" + 
								cont_name + "$txt_Balls*GEOM*TEXT SET " + bc.getBalls() + " \0", print_writers);
						break;
					}
				}
				
				break;
			case "Shift_F2":
				int bowling_size = 1;
				rowId = 0;
				cont_name = "";
				omo_num = 0;
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
						+ "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
						+ "$Bowling_Card$Title$txt_TeamName*GEOM*TEXT SET " + inning.getBowling_team().getTeamBadge() + " \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
						+ "$Bowling_Card$Rows$Select_Row*FUNCTION*Omo*vis_con SET "+inning.getBowlingCard().size()+" \0", print_writers);
			
				
				
				for (BowlingCard boc : inning.getBowlingCard()) {
					rowId = rowId + 1;
					if(boc.getRuns() > 0 || ((boc.getOvers()*6)+boc.getBalls()) > 0) {
						bowling_size=bowling_size + 1;
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
//								+ "$Bowling$DataGrp*FUNCTION*Grid*num_row SET " + bowling_size + " \0", print_writers);
					}

					switch (boc.getStatus().toUpperCase()) {
					case (CricketUtil.OTHER + CricketUtil.BOWLER):case (CricketUtil.LAST + CricketUtil.BOWLER):
						omo_num = 0;
						cont_name = "$Out";
						text_name = "$Data";
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
								+ "$Select_GraphicsType$Bowling_Card$Data$Rows$Select_Row$" + rowId  +"$BowlingData$Select_Row_Type" +cont_name +text_name+"$Name$txt_Name*GEOM*TEXT SET " +  boc.getPlayer().getTicker_name() + " \0", print_writers);
		
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
								+ "$Select_GraphicsType$Bowling_Card$Data$Rows$Select_Row$" + rowId +"$BowlingData$Select_Row_Type" + cont_name +text_name+"$Overs$txt_Overs*GEOM*TEXT SET " + CricketFunctions.OverBalls(boc.getOvers(),boc.getBalls()) + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
								+ "$Select_GraphicsType$Bowling_Card$Data$Rows$Select_Row$" + rowId +"$BowlingData$Select_Row_Type"+cont_name +text_name+ "$Figures$txt_Figures*GEOM*TEXT SET " + boc.getWickets() +"-"+ boc.getRuns() + " \0", print_writers);

						break;
					
					case (CricketUtil.CURRENT + CricketUtil.BOWLER):
						System.out.println("HELLO "+rowId);
						omo_num = 1;
						cont_name = "$Not_Out";
						text_name = "$img_Text1";
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
								+ "$Select_GraphicsType$Bowling_Card$Data$Rows$Select_Row$" + rowId  +"$BowlingData$Select_Row_Type" +cont_name +"$img_Text1$Name$txt_Name*GEOM*TEXT SET " +  boc.getPlayer().getTicker_name() + " \0", print_writers);
		
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
								+ "$Select_GraphicsType$Bowling_Card$Data$Rows$Select_Row$" + rowId +"$BowlingData$Select_Row_Type" + cont_name +"$img_Text2$Overs$txt_Overs*GEOM*TEXT SET " + CricketFunctions.OverBalls(boc.getOvers(),boc.getBalls()) + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
								+ "$Select_GraphicsType$Bowling_Card$Data$Rows$Select_Row$" + rowId +"$BowlingData$Select_Row_Type"+cont_name + "$img_Text2$Figures$txt_Figures*GEOM*TEXT SET " + boc.getWickets()+"-"+  boc.getRuns() + " \0", print_writers);

						break;
					}
					
					
					if(CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
							boc.getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
								+ "$Select_GraphicsType$Bowling_Card$Data$Rows$Select_Row$" + rowId +"$BowlingData$Select_Row_Type"+ cont_name +text_name+ "$Name$Select_Impact*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}
					else if(CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
							boc.getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
								+ "$Select_GraphicsType$Bowling_Card$Data$Rows$Select_Row$" + rowId +"$BowlingData$Select_Row_Type" +cont_name+ text_name+"$Name$Select_Impact*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
								+ "$Select_GraphicsType$Bowling_Card$Data$Rows$Select_Row$" + rowId +"$BowlingData$Select_Row_Type"+cont_name +text_name+"$Name$Select_Impact*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					}
					
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Minis$All_Graphics$Side" + WhichSide 
							+ "$Select_GraphicsType$Bowling_Card$Data$Rows$Select_Row$"+rowId+"$Select_Row_Type*FUNCTION*Omo*vis_con SET " + String.valueOf(omo_num) + " \0", print_writers);

				}
				break;
			}
			break;
		}
		
			
		return Constants.OK;
	}
	public String getBowlerRunsOverbyOver(int inning,List<Event> event, MatchAllData matchAllData) {
		
		int bowlerId = 0,runs = 0,wicket = 0;
		String name = "";
		boolean bowler_found = false;
		
		if ((matchAllData.getEventFile().getEvents() != null) && (matchAllData.getEventFile().getEvents().size() > 0)) {
			for(Event evnt: matchAllData.getEventFile().getEvents()) {
				if(evnt.getEventInningNumber() == inning) {
					if(evnt.getEventExtra() != null) {
						if(evnt.getEventExtra().equalsIgnoreCase("TAPE")) {
							bowlerId = evnt.getEventBowlerNo();
							bowler_found = true;
							runs = 0;
							wicket = 0;
						}
					}
					if(bowler_found && evnt.getEventBowlerNo() == bowlerId) {
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
		                    break;										
						}
					}else if(evnt.getEventBowlerNo() != bowlerId && evnt.getEventBowlerNo() != 0) {
						bowler_found = false;
					}
				}
			}
		}
		
		for (BowlingCard boc : matchAllData.getMatch().getInning().get(inning - 1).getBowlingCard()) {
			if(boc.getPlayerId() == bowlerId) {
				name = boc.getPlayer().getTicker_name();
			}
		}
		
		return name + "," + runs + "," + wicket;
		
	}
}