package com.cricket.captions;

import java.io.PrintWriter;
import java.util.List;
import com.cricket.model.BattingCard;
import com.cricket.model.BowlingCard;
import com.cricket.model.Bugs;
import com.cricket.model.Configuration;
import com.cricket.model.Ground;
import com.cricket.model.Inning;
import com.cricket.model.MatchAllData;
import com.cricket.model.Partnership;
import com.cricket.model.Player;
import com.cricket.model.Team;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;

public class BugsAndMiniGfx 
{
	String status = "";
	
	public List<PrintWriter> print_writers; 
	public Configuration config;
	public List<MatchAllData> tournament_matches;
	public List<Bugs> bugs;
	public List<Team> Teams;
	public List<Ground> Grounds;
	
	public BattingCard battingCard;
	public BowlingCard bowlingCard;
	public Partnership partnership;
	public Inning inning;
	public Player player;
	
	public Team team;
	public Bugs bug;
	
	public BugsAndMiniGfx() {
		super();
	}
	
	public BugsAndMiniGfx(List<PrintWriter> print_writers, Configuration config, List<MatchAllData> tournament_matches,
			List<Bugs> bugs, List<Team> teams, List<Ground> grounds) {
		super();
		this.print_writers = print_writers;
		this.config = config;
		this.tournament_matches = tournament_matches;
		this.bugs = bugs;
		this.Teams = teams;
		this.Grounds = grounds;
	}
	
	public String bugsDismissal(String whatToProcess,MatchAllData matchAllData,int WhichSide) {
		
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return "bugsDismissal match is null Or Inning is null";
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
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
		if(PopulateBugBody(WhichSide, whatToProcess.split(",")[0],matchAllData) == Constants.OK) {
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
		if(PopulateBugBody(WhichSide, whatToProcess.split(",")[0],matchAllData) == Constants.OK) {
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
		
		if(PopulateBugBody(WhichSide, whatToProcess.split(",")[0],matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	public String populateBowlScore(String whatToProcess,MatchAllData matchAllData,int WhichSide)
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			status = "populateBowlScore match is null Or Inning is null";
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
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
		if(PopulateBugBody(WhichSide, whatToProcess.split(",")[0],matchAllData) == Constants.OK) {
			status = Constants.OK;;
		}
		return status;		
	}
	public String populateBatScore(String whatToProcess,MatchAllData matchAllData,int WhichSide)
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			status = "populateBatScore match is null Or Inning is null";
		} else {
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
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
		if(PopulateBugBody(WhichSide, whatToProcess.split(",")[0],matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	
	public String bugsToss(String whatToProcess, MatchAllData matchAllData, int WhichSide) {
		if (matchAllData == null || matchAllData.getMatch() == null ||
			matchAllData.getMatch().getInning() == null|| matchAllData.getSetup()==null) {
			status = "BugsToss match is null Or Inning is null";
		} else {
			inning = matchAllData.getMatch().getInning().stream().
					filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).
					findAny().orElse(null);
		}
		if(PopulateBugBody(WhichSide, whatToProcess.split(",")[0],matchAllData) == Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}

	public String PopulateBugBody(int WhichSide, String whatToProcess,MatchAllData matchAllData) {
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			switch(whatToProcess) {
			case "Alt_p":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Select"
						+ "*FUNCTION*Omo*vis_con SET 1  \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Toss$img_Flag1*TEXTURE*IMAGE SET " 
				+ Constants.ICC_U19_2023_FLAG_PATH + matchAllData.getMatch().getInning().get(0).getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Toss$img_Flag2*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + matchAllData.getMatch().getInning().get(0).getBowling_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Toss"
						+ "$txt_Info*GEOM*TEXT SET " + CricketFunctions.generateTossResult(matchAllData, CricketUtil.FULL, CricketUtil.FIELD, 
								CricketUtil.FULL, CricketUtil.ELECTED).toUpperCase() + "\0", print_writers);
				
				break;
			case "g":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Flag*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Name*GEOM*TEXT SET " + bowlingCard.getPlayer().getTicker_name() + "\0", print_writers);
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
			case "f":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Flag*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
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
						+ "$txt_Balls*GEOM*TEXT SET " + " (" + battingCard.getBalls() + ")" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$SubText$Side" + WhichSide 
						+ "$txt_Sub*GEOM*TEXT SET " + battingCard.getFours() +" FOURS    " + battingCard.getSixes() + " SIXES"+ "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$Data$SubText$Side" + WhichSide 
						+ "$img_Sponsor*ACTIVE SET 0 \0", print_writers);
				break;
			case "Shift_O":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$img_Flag*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide + 
						"$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Name*GEOM*TEXT SET " + battingCard.getPlayer().getTicker_name() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Runs*GEOM*TEXT SET " + battingCard.getRuns() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Bugs_All$MainTxt_Grp$Side" + WhichSide 
						+ "$txt_Balls*GEOM*TEXT SET " + "(" + battingCard.getBalls() + ")" + "\0", print_writers);
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
						+ "$txt_Balls*GEOM*TEXT SET " + "(" + partnership.getTotalBalls() + ")" + "\0", print_writers);
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
		}
		
		return Constants.OK;
	}

}