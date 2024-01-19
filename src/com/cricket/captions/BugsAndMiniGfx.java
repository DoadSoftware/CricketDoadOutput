package com.cricket.captions;

import java.io.PrintWriter;
import java.util.Collections;
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
import com.cricket.model.VariousText;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;

public class BugsAndMiniGfx 
{
	String status = "";
	
	int rowId=0, omo_num=0;
	String cont_name = "";
	public List<PrintWriter> print_writers; 
	public Configuration config;
	public List<MatchAllData> tournament_matches;
	public List<Bugs> bugs;
	public List<Team> Teams;
	public List<Ground> Grounds;
	public List<VariousText> VariousText;
	
	public BattingCard battingCard;
	public BowlingCard bowlingCard;
	public Partnership partnership;
	public VariousText variousText;
	public Inning inning;
	public Player player;
	
	public Team team;
	public Bugs bug;
	
	public BugsAndMiniGfx() {
		super();
	}
	
	public BugsAndMiniGfx(List<PrintWriter> print_writers, Configuration config, List<MatchAllData> tournament_matches,
			List<Bugs> bugs, List<Team> teams, List<Ground> grounds,List<VariousText> VariousText) {
		super();
		this.print_writers = print_writers;
		this.config = config;
		this.tournament_matches = tournament_matches;
		this.bugs = bugs;
		this.Teams = teams;
		this.Grounds = grounds;
		this.VariousText = VariousText;
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
		if(PopulateBugBody(WhichSide, whatToProcess,matchAllData) == Constants.OK) {
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

	public String PopulateBugBody(int WhichSide, String whatToProcess,MatchAllData matchAllData) {
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			switch (whatToProcess.split(",")[0]) {
			case "g": case "f": case "Shift_O": case "Control_k":
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
				if(matchAllData.getMatch().getInning().get(0).getBatting_team().getTeamName4().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Toss$img_Flag1$img_Shadow*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Center_Bug$Toss$img_Flag2$img_Shadow*ACTIVE SET 1 \0", print_writers);
				}else if(matchAllData.getMatch().getInning().get(0).getBowling_team().getTeamName4().equalsIgnoreCase("NEP")){
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
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				if(inning.getBatting_team().getTeamName4().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$img_Flag1*ACTIVE SET 0 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
							+ "$Bowling$img_Flag1*ACTIVE SET 1 \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Batting$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName4() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$minis$Side" + WhichSide 
						+ "$Batting$txt_FirstName*GEOM*TEXT SET " + inning.getBatting_team().getTeamName1() + " \0", print_writers);
				
				
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
		}
		
			
		return Constants.OK;
	}
}