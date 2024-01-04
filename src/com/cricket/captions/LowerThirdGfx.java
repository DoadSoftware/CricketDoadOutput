package com.cricket.captions;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import com.cricket.containers.LowerThird;
import com.cricket.model.BattingCard;
import com.cricket.model.BowlingCard;
import com.cricket.model.Configuration;
import com.cricket.model.FallOfWicket;
import com.cricket.model.Fixture;
import com.cricket.model.Ground;
import com.cricket.model.Inning;
import com.cricket.model.MatchAllData;
import com.cricket.model.NameSuper;
import com.cricket.model.Player;
import com.cricket.model.Statistics;
import com.cricket.model.StatsType;
import com.cricket.model.Team;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class LowerThirdGfx 
{
	public int FirstPlayerId;
	public int SecondPlayerId;
	public String WhichProfile;
	
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
	
	public LowerThirdGfx() {
		super();
	}
	
	public LowerThirdGfx(List<PrintWriter> print_writers, Configuration config, List<Statistics> statistics,
			List<StatsType> statsTypes, List<MatchAllData> tournament_matches, List<NameSuper> nameSupers,List<Fixture> fixTures, 
			List<Team> Teams, List<Ground> Grounds) {
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
	}
	public boolean populateL3rdCurrentPartnership(String whatToProcess,int WhichSide,MatchAllData matchAllData)
	{
		String Left_Batsman = "",Right_Batsman = "",first_batter_run = "",
				first_batter_ball = "",second_batter_run = "",second_batter_ball = "";
		for(Inning inn : matchAllData.getMatch().getInning()) {
			if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
				for(BattingCard bc : inn.getBattingCard()) {
					if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						if(bc.getPlayerId()==inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterNo()) {
							Left_Batsman = bc.getPlayer().getTicker_name();
							first_batter_run = String.valueOf(inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterRuns());
							first_batter_ball = String.valueOf(inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterBalls());
						}
						
						if(bc.getPlayerId()==inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterNo()) {
							Right_Batsman = bc.getPlayer().getTicker_name();
							second_batter_run = String.valueOf(inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterRuns());
							second_batter_ball = String.valueOf(inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterBalls());
						}
					}
					
					lowerThird = new LowerThird("", "CURRENT", "PARTNERSHIP","", String.valueOf(inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalRuns())
							, String.valueOf(inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalBalls()),
							2,"",inn.getBatting_team().getTeamName4(),null,null,new String[]{Left_Batsman,first_batter_run,first_batter_ball},
									new String[]{Right_Batsman,second_batter_run,second_batter_ball});
				}
			}
		}
		
		if(PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide) == true) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 1,print_writers, config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return false;
		}
	}
	public boolean populateL3rdProjected(String whatToProcess,int WhichSide,MatchAllData matchAllData)
	{
		System.out.println("config = " + config.getBroadcaster());
		String[] proj_score_rate = new String[CricketFunctions.projectedScore(matchAllData).size()];
	    for (int i = 0; i < CricketFunctions.projectedScore(matchAllData).size(); i++) {
	    	proj_score_rate[i] = CricketFunctions.projectedScore(matchAllData).get(i);
        }
		for(Inning inn : matchAllData.getMatch().getInning()) {
			if(inn.getInningNumber() == 1 & inn.getIsCurrentInning().equalsIgnoreCase("YES")) {
				lowerThird = new LowerThird("", inn.getBatting_team().getTeamName1(), "","PROJECTED SCORE", String.valueOf(inn.getTotalRuns() + "-" + inn.getTotalWickets()), "",
						2,"",inn.getBatting_team().getTeamName4(),new String[]{"RATE","@" + proj_score_rate[0] +" (CRR)","@" + proj_score_rate[2] + " RPO","@" + proj_score_rate[4] + " RPO"}
						,new String[]{"SCORE",proj_score_rate[1],proj_score_rate[3],proj_score_rate[5]},null,null);
			}
		}
		System.out.println("lowerThird.getTitlesText() = " + lowerThird.getTitlesText());
		if(PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide) == true) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 1,print_writers, config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return false;
		}
	}
	public boolean populateBattingStyle(String whatToProcess,int WhichSide,MatchAllData matchAllData)
	{
		for(Inning inn : matchAllData.getMatch().getInning()) {
			if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
				for(BattingCard bc : inn.getBattingCard()) {
					if(bc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[1])) {
						lowerThird = new LowerThird("", bc.getPlayer().getFirstname(), bc.getPlayer().getSurname(),"", null, null,
								2,"",inn.getBatting_team().getTeamName4(),null,null,new String[]{CricketFunctions.getbattingstyle(bc.getPlayer().getBattingStyle(), CricketUtil.FULL, false, false).toUpperCase()
										,inn.getBatting_team().getTeamName1()},null);
					}
				}
			}
		}
		
		if(PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide) == true) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 1,print_writers, config);
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return false;
		}
	}
	public boolean populateBowlingStyle(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		System.out.println("inside = " + whatToProcess);
		player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(whatToProcess.split(",")[1]), matchAllData);
		
		if(matchAllData.getSetup().getHomeTeamId() == player.getTeamId()) {
			team = matchAllData.getSetup().getHomeTeam();
		} else if(matchAllData.getSetup().getAwayTeamId() == player.getTeamId()) {
			team = matchAllData.getSetup().getAwayTeam();
		}
		
		lowerThird = new LowerThird("", player.getFirstname(), player.getSurname(),"", null, null,
				2,"",team.getTeamName4(),null,null,new String[]{CricketFunctions.getbowlingstyle(player.getBowlingStyle()).toUpperCase()
						,team.getTeamName1()},null);
		
		if(PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide) == true) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 1,print_writers, config);
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return false;
		}
	}
	public boolean populateBatThisMatch(String whatToProcess,int WhichSide,MatchAllData matchAllData)
	{
		String outOrNot = "";
		for(Inning inn : matchAllData.getMatch().getInning()) {
			if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
				for(BattingCard bc : inn.getBattingCard()) {
					if(bc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[1])) {
						if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
							outOrNot = "NOT OUT";
						}else {
							outOrNot = "";
						}
						lowerThird = new LowerThird("", bc.getPlayer().getFirstname(), bc.getPlayer().getSurname(),outOrNot, String.valueOf(bc.getRuns()), "", 2, "",
							inn.getBatting_team().getTeamName4(),new String[] {"BALLS","FOURS","SIXES","STRIKE RATE"},
							new String[] {String.valueOf(bc.getBalls()), String.valueOf(bc.getFours()),String.valueOf(bc.getSixes()),bc.getStrikeRate()},null,null);
					}
				}
			}
		}
		
		if(PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide) == true) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 4,print_writers, config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return false;
		}
	}
	public boolean populateBowlThisMatch(String whatToProcess,int WhichSide,MatchAllData matchAllData)
	{
		for(Inning inn : matchAllData.getMatch().getInning()) {
			if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
				for(BowlingCard boc : inn.getBowlingCard()) {
					if(boc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[1])) {
						lowerThird = new LowerThird("", boc.getPlayer().getFirstname(), boc.getPlayer().getSurname(),"", "", "", 2, "", inn.getBowling_team().getTeamName4(),
							new String[] {"OVERS", "MAIDENS", "RUNS", "WICKETS", "ECONOMY"},new String[]{CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()), 
							String.valueOf(boc.getMaidens()),String.valueOf(boc.getRuns()),String.valueOf(boc.getWickets()), String.valueOf(boc.getEconomyRate())},null,null);
					}
				}
			}
		}
		
		if(PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide) == true) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 4,print_writers, config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return false;
		}
	}
	public boolean populateLTNameSuperPlayer(String whatToProcess,int WhichSide,MatchAllData matchAllData)
	{
		player =  CricketFunctions.getPlayerFromMatchData(Integer.valueOf(whatToProcess.split(",")[1]), matchAllData);
		team = Teams.stream().filter(tm -> tm.getTeamId() == player.getTeamId()).findAny().orElse(null);
		
		lowerThird = new LowerThird("", player.getFirstname(), player.getSurname(),"", "", "", 1, "",team.getTeamName4(),
				null,null,new String[]{whatToProcess.split(",")[2].toUpperCase()},null);
		
		if(PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide) == true) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 1,print_writers, config);
			return PopulateL3rdBody(WhichSide, whatToProcess.split(",")[0]);
		} else {
			return false;
		}
	}
	public boolean populateLTNameSuper(String whatToProcess,int WhichSide)
	{
		namesuper = this.nameSupers.stream().filter(ns -> ns.getNamesuperId() == Integer.valueOf(whatToProcess.split(",")[1]))
				.findAny().orElse(null);
		
		lowerThird = new LowerThird("", namesuper.getFirstname(), namesuper.getSurname(),"", "", "", 1, "" ,"",
			null,null,new String[]{namesuper.getSubLine()},null);
		
		if(PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide) == true) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 0,print_writers, config);
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return false;
		}
	}
	public boolean populateHowOut(String whatToProcess,int WhichSide,MatchAllData matchAllData)
	{
		for(Inning inn : matchAllData.getMatch().getInning()) {
			if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
				for(BattingCard bc : inn.getBattingCard()) {
					if(bc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[1])) {
						lowerThird = new LowerThird("", bc.getPlayer().getFirstname(), bc.getPlayer().getSurname(),"", 
								String.valueOf(bc.getRuns()), String.valueOf(bc.getBalls()),2,"",inn.getBatting_team().getTeamName4(),
								null,null,new String[]{bc.getHowOutText(),String.valueOf(bc.getFours()),String.valueOf(bc.getSixes())},
								new String[]{bc.getStrikeRate()});
					}
				}
			}
		}
		
		if(PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide) == true) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 1,print_writers, config);
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return false;
		}
	}
	public boolean PopulateL3rdPlayerProfile(String whatToProcess, int WhichSide, MatchAllData matchAllData) 
		throws JsonMappingException, JsonProcessingException, InterruptedException
	{
		if(!whatToProcess.contains(",") && whatToProcess.split(",").length >= 4) {
			return false;
		}

		FirstPlayerId = Integer.valueOf(whatToProcess.split(",")[1]);
		WhichProfile = whatToProcess.split(",")[2];
		
		if(FirstPlayerId <= 0 || WhichProfile == null) {
			return false;
		}
		
		stat = statistics.stream().filter(st -> st.getPlayer_id() == FirstPlayerId).findAny().orElse(null);
		
		if(stat == null) {
			return false;
		}
		
		statsType = statsTypes.stream().filter(st -> st.getStats_short_name().equalsIgnoreCase(WhichProfile)).findAny().orElse(null);
		
		if(statsType == null) {
			return false;
		}
		
		player = CricketFunctions.getPlayerFromMatchData(stat.getPlayer_id(), matchAllData); 
		
		if(player == null) {
			return false;
		}
		
		if(matchAllData.getSetup().getHomeTeamId() == player.getTeamId()) {
			team = matchAllData.getSetup().getHomeTeam();
		} else if(matchAllData.getSetup().getAwayTeamId() == player.getTeamId()) {
			team = matchAllData.getSetup().getAwayTeam();
		} 
		
		if(team == null) {
			return false;
		}
		
		stat.setStats_type(statsType);
		//stat = CricketFunctions.updateTournamentDataWithStats(stat, tournament_matches, matchAllData);
		//stat = CricketFunctions.updateStatisticsWithMatchData(stat, matchAllData);
		
		if(Integer.valueOf(whatToProcess.split(",")[0]) == 118) {
			//DJ runs should have thousand comma separater (10,000)
			lowerThird = new LowerThird("", player.getFirstname(), player.getSurname(),statsType.getStats_full_name(), "", "", 2,"",team.getTeamName4(),
					new String[]{"MATCHES", "RUNS", "100s", "50s", "BEST", "", ""},new String[]{String.valueOf(stat.getMatches()), String.format("%,d\n", stat.getRuns()),
					String.valueOf(stat.getHundreds()), String.valueOf(stat.getFifties()), stat.getBest_score() ,"",""},null,null);
		}
		else if(Integer.valueOf(whatToProcess.split(",")[0]) == 122) {
			lowerThird = new LowerThird("", player.getFirstname(), player.getSurname(),statsType.getStats_full_name(), "", "", 2,"",team.getTeamName4(),
					new String[]{"MATCHES", "WICKETS", "3WI", "5WI", "BEST", "", ""},new String[]{String.valueOf(stat.getMatches()), String.valueOf(stat.getWickets()), 
					String.valueOf(stat.getPlus_3()), String.valueOf(stat.getPlus_5()), stat.getBest_figures() ,"",""},null,null);
		}
		
		if(PopulateL3rdHeader(whatToProcess.split(",")[0],WhichSide) == true) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			setPositionOfLT(lowerThird.getNumberOfSubLines(), WhichSide, 1,print_writers, config);
			return PopulateL3rdBody(WhichSide,whatToProcess.split(",")[0]);
		} else {
			return false;
		}
	}
	
	public boolean PopulateL3rdHeader(String whatToProcess,int WhichSide) 
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			switch(whatToProcess) {
			case "Control a":
				if(lowerThird.getWhichTeamFlag() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag$Change_Out$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				}

				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
						"$Sponsor$Emirates_Logo*ACTIVE SET 1 \0", print_writers); // Show sponsor on primary Viz
				CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
						"$Sponsor$Emirates_Logo*ACTIVE SET 0 \0", print_writers); // Hide sponsor on slave Viz
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$txt_Designation*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name$Change_Out$Score*ACTIVE SET 1 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$Score$txt_Not_Out*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				break;	
			case "Alt+k":
				if(lowerThird.getWhichTeamFlag() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag$Change_Out$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				}

				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
						"$Sponsor$Emirates_Logo*ACTIVE SET 1 \0", print_writers); // Show sponsor on primary Viz
				CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
						"$Sponsor$Emirates_Logo*ACTIVE SET 0 \0", print_writers); // Hide sponsor on slave Viz
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name$Change_Out$Score*ACTIVE SET 0 \0", print_writers);
				break;	
			case "Control+F5": case "Control+F9":
				if(lowerThird.getWhichTeamFlag() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide + "$obj_WidthX"
							+ "*FUNCTION*Maxsize*WIDTH_X SET 1020\0", print_writers);
				}

				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
						"$Sponsor$Emirates_Logo*ACTIVE SET 1 \0", print_writers); // Show sponsor on primary Viz
				CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
						"$Sponsor$Emirates_Logo*ACTIVE SET 0 \0", print_writers); // Hide sponsor on slave Viz
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"
						+ WhichSide + "$Name$Change_Out$Score*ACTIVE SET 0 \0", print_writers);
				
				break;
			case "F8":
				if(lowerThird.getWhichTeamFlag() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag$Change_Out$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				}

				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
						"$Sponsor$Emirates_Logo*ACTIVE SET 1 \0", print_writers); // Show sponsor on primary Viz
				CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
						"$Sponsor$Emirates_Logo*ACTIVE SET 0 \0", print_writers); // Hide sponsor on slave Viz
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name$Change_Out$Score*ACTIVE SET 0 \0", print_writers);
				break;
			case "F5":
				if(lowerThird.getWhichTeamFlag() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag_Text$Change_Out$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
						"$Select_Flags$Flag_Text$Change_Out$txt_Hashtag*GEOM*TEXT SET " + "#FutureStars" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name$Change_Out$Score*ACTIVE SET 1 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$Score$txt_Not_Out*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
				break;
			case "F9":
				if(lowerThird.getWhichTeamFlag() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag_Text$Change_Out$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
						"$Select_Flags$Flag_Text$Change_Out$txt_Hashtag*GEOM*TEXT SET " + "#FutureStars" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name$Change_Out$Score*ACTIVE SET 0 \0", print_writers);
				break;
			case "F10":
				//Dj implement DoadWriteTextToSelectedViz everywhere for sponsor process
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
						"$Select_Flags*FUNCTION*Omo*vis_con SET 0 \0", print_writers);

				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
						"$Sponsor$Emirates_Logo*ACTIVE SET 1 \0", print_writers); // Show sponsor on primary Viz
				CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
						"$Sponsor$Emirates_Logo*ACTIVE SET 0 \0", print_writers); // Hide sponsor on slave Viz
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name$Change_Out$Score*ACTIVE SET 0 \0", print_writers);
				
				break;
			case "F6":
				if(lowerThird.getWhichTeamFlag() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag$Change_Out$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				}

				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
						"$Sponsor$Emirates_Logo*ACTIVE SET 1 \0", print_writers); // Show sponsor on primary Viz
				CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
						"$Sponsor$Emirates_Logo*ACTIVE SET 0 \0", print_writers); // Hide sponsor on slave Viz
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$txt_Designation*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name$Change_Out$Score*ACTIVE SET 1 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$Score$txt_Score*GEOM*TEXT SET " + lowerThird.getScoreText() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$Score$txt_Not_Out*GEOM*TEXT SET " + "(" + lowerThird.getBallsFacedText() + ")" + "\0", print_writers);
				
				break;
			case "F7": case "F11":
				
				if(lowerThird.getWhichTeamFlag() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide + 
							"$Select_Flags$Flag$Change_Out$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + lowerThird.getWhichTeamFlag() + "\0", print_writers);
				}

				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
						"$Sponsor$Emirates_Logo*ACTIVE SET 1 \0", print_writers); // Show sponsor on primary Viz
				CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_"+ WhichSide + 
						"$Sponsor$Emirates_Logo*ACTIVE SET 0 \0", print_writers); // Hide sponsor on slave Viz
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Data$Side_" + WhichSide 
						+ "$Name$Change_Out$txt_Designation*GEOM*TEXT SET " + lowerThird.getSubTitle() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"
						+ WhichSide + "$Name$Change_Out$Score*ACTIVE SET 0 \0", print_writers);
				break;
			}
			break;
		}
		return true;
	}
	public void HideAndShowL3rdSubStrapContainers(int WhichSide)
	{
		for(PrintWriter print_writer : print_writers) {
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				//Show number of sublines
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Select_Subline*FUNCTION*Omo*vis_con SET " + 
						lowerThird.getNumberOfSubLines() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_"+ WhichSide + 
						"$Select_Subline*FUNCTION*Omo*vis_con SET " + lowerThird.getNumberOfSubLines() + "\0");
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$1$Data$Stat*ACTIVE SET 0 \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
						"$Select_Subline$2$Data$Title*ACTIVE SET 0 \0");
				
				if(lowerThird.getTitlesText() != null) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1$Data$Title*ACTIVE SET 1 \0");
					for(int iTitle = 1; iTitle <= lowerThird.getTitlesText().length; iTitle++) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$1$Data$Title$txt_" + iTitle + "*ACTIVE SET 1 \0");
					}
					//Hide number of Titles on each strap
					for(int iTitle = lowerThird.getTitlesText().length + 1; iTitle <= 10; iTitle++) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$1$Data$Title$txt_" + iTitle + "*ACTIVE SET 0 \0");
					}
				}else {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1$Data$Title*ACTIVE SET 0 \0");
				}
				
				if(lowerThird.getStatsText() != null) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Stat*ACTIVE SET 1 \0");
					for(int iStats = 1; iStats <= lowerThird.getStatsText().length; iStats++) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2$Data$Stat$txt_" + iStats + "*ACTIVE SET 1 \0");
					}
					//Hide number of Titles on each strap
					for(int iStats = lowerThird.getTitlesText().length + 1; iStats <= 10; iStats++) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2$Data$Stat$txt_" + iStats + "*ACTIVE SET 0 \0");
					}
				}else {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$2$Data$Stat*ACTIVE SET 0 \0");
				}
				
				for(int iSubLine = 1; iSubLine <= lowerThird.getNumberOfSubLines(); iSubLine++) {
					//Show Left on each strap
					if(lowerThird.getLeftText() != null &&  lowerThird.getLeftText().length > 0) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$"+ iSubLine + "$Data$Left*ACTIVE SET 1 \0");
					} else {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$"+ iSubLine + "$Data$Left*ACTIVE SET 0 \0");
					}
					//Show Right on each strap
					if(lowerThird.getRightText() != null && lowerThird.getRightText().length > 0) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$"+ iSubLine + "$Data$Right*ACTIVE SET 1 \0");
					} else {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$"+ iSubLine + "$Data$Right*ACTIVE SET 0 \0");
					}
				}
				
				break;
			}
		}
	}
	public void setPositionOfLT(int subLineValue,int WhichSide,int flagAndSponsor,List<PrintWriter> print_writers, Configuration config)
	{
		String LT_Position_1 = "",LT_Position_2 = "",LT_Position_3 = "",LT_Flag_Keys="",LT_Flag_Object="";
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			switch (subLineValue) {
			case 0:
				LT_Position_1 = "0.0";
				LT_Position_2 = "0.0 -82.0 0.0";
				LT_Position_3 = "0.0 -8.0 0.0";
				break;
			case 1:
				LT_Position_1 = "25.0";
				LT_Position_2 = "0.0 -50.0 0.0";
				LT_Position_3 = "0.0 -25.0 0.0";
				break;
			case 2:
				LT_Position_1 = "50.0";
				LT_Position_2 = "0.0 0.0 0.0";
				LT_Position_3 = "0.0 -50.0 0.0";
				break;
			case 3:
				LT_Position_1 = "75.0";
				LT_Position_2 = "0.0 25.0 0.0";
				LT_Position_3 = "0.0 -75.0 0.0";
				break;
			}
			switch(flagAndSponsor) {
			case 0:
				LT_Flag_Keys = "57.0";
				LT_Flag_Object = "1110";
				break;
			case 1:
				LT_Flag_Keys = "48.0";
				LT_Flag_Object = "1020";
				break;
			case 2:
				LT_Flag_Keys = "30.0";
				LT_Flag_Object = "840";
				break;
			case 3:
				LT_Flag_Keys = "40.0";
				LT_Flag_Object = "940";
				break;
			case 4:
				LT_Flag_Keys = "26.0";
				LT_Flag_Object = "810";
				break;
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$RightPartSize*ANIMATION*KEY*$H1*VALUE SET "
					+ LT_Flag_Keys + "\0",print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$RightPartSize*ANIMATION*KEY*$H2*VALUE SET "
					+ LT_Flag_Keys + "\0",print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Top_Line$Side_" + WhichSide + "$obj_WidthX"
					+ "*FUNCTION*Maxsize*WIDTH_X SET " + LT_Flag_Object + "\0",print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_" + WhichSide + "$Set_Rows_Side_" + WhichSide + 
					"_Position_Y*TRANSFORMATION*POSITION*Y SET " + LT_Position_1 + "\0",print_writers);
			
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Overall_Position_Y_In_Out*ANIMATION*KEY*$In_2*VALUE SET " 
					+ LT_Position_2 + "\0",print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Overall_Position_Y_In_Out*ANIMATION*KEY*$Out_1*VALUE SET "
					+ LT_Position_2 + "\0",print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Sublines_Position_Y*ANIMATION*KEY*$In_2*VALUE SET "
					+ LT_Position_3 + "\0",print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Sublines_Position_Y*ANIMATION*KEY*$Out_1*VALUE SET "
					+ LT_Position_3 + "\0",print_writers);
			break;
		}
	}
	public boolean PopulateL3rdBody(int WhichSide, String whatToProcess) 
	{
		for(PrintWriter print_writer : print_writers) {
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				switch (whatToProcess) {
				case "Control a":
					for(int iStat = 0; iStat < 4; iStat++) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$1$Change_Out$Data$Title$txt_" + (iStat + 1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[iStat] + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$2$Change_Out$Data$Stat$txt_" + (iStat + 1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[iStat] + "\0");
					}
					break;
				case "Alt k":
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0]+ " " + lowerThird.getLeftText()[1] + " OFF " + 
							lowerThird.getLeftText()[2]+ "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + lowerThird.getRightText()[0] + " " + lowerThird.getRightText()[1] + " OFF " + 
							lowerThird.getRightText()[2] + "\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + "PARTNERSHIP " + lowerThird.getScoreText() + " RUNS FROM " + 
							lowerThird.getBallsFacedText() + " BALLS"  + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$2$Data$Right$txt_1*GEOM*TEXT SET " + "" + "\0");
					
					break;
				case "Control F5": case "Control F9":
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[1] + "\0");
					
					break;
				case "F6":
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + "STRIKE RATE " + lowerThird.getRightText()[0] + "\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$2$Data$Right$txt_1*GEOM*TEXT SET " + "" + "\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + "FOURS " + lowerThird.getLeftText()[1] + "      SIXES " + 
							lowerThird.getLeftText()[2]  + "\0");
					
					break;
				case "F5":
					for(int i=0; i<4; i++) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$1$Change_Out$Data$Title$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0");

						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2$Change_Out$Data$Stat$txt_" + (i+1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0");
					}
					break;
				case "F9":
					for(int i=0; i<5; i++) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$1$Change_Out$Data$Title$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getTitlesText()[i] + "\0");

						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$2$Change_Out$Data$Stat$txt_"+(i+1)+"*GEOM*TEXT SET " + lowerThird.getStatsText()[i] + "\0");
					}
					break;
				case "F7": case "F11":
					for(int iStat = 0; iStat < 7; iStat++) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$1$Data$Title$txt_" + (iStat + 1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[iStat] + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$2$Data$Stat$txt_" + (iStat + 1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[iStat] + "\0");
					}
					break;
				case "F8": case "F10":
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0");
					break;
				}
				break;
			}
		}
		return true;
	}
}