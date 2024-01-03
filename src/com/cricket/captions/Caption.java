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

public class Caption 
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
	
	public Caption() {
		super();
	}
	
	public Caption(List<PrintWriter> print_writers, Configuration config, List<Statistics> statistics,
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

	public boolean PopulateGraphics(String whatToProcess, int WhichSide, MatchAllData matchAllData) 
		throws InterruptedException, JsonMappingException, JsonProcessingException, NumberFormatException, ParseException
	{
		if(whatToProcess.contains(",")) {
			switch (Integer.valueOf(whatToProcess.split(",")[0])) {
			case 112: // Scorecard FF
				return PopulateScorecardFF(WhichSide, Integer.valueOf(whatToProcess.split(",")[0]), matchAllData, Integer.valueOf(whatToProcess.split(",")[1]));
			case 113: // Bowling FF
				return PopulateBowlingCardFF(WhichSide, Integer.valueOf(whatToProcess.split(",")[0]), matchAllData, Integer.valueOf(whatToProcess.split(",")[1]));
			case 1777: //MATCH PROMO
				return populateFFMatchPromo(WhichSide, whatToProcess,matchAllData);
			case 16122: //MATCH SUMMARY
				return populateMatchSummary(WhichSide, Integer.valueOf(whatToProcess.split(",")[0]), matchAllData, Integer.valueOf(whatToProcess.split(",")[1]));
			case 118: case 122: // L3rd BAT and BALL Profile
				return PopulateL3rdPlayerProfile(whatToProcess,WhichSide, matchAllData);
			case 117://HowOut
				return populateHowOut(whatToProcess,WhichSide,matchAllData);
			case 121: //NameSuper DB
				return populateLTNameSuper(whatToProcess,WhichSide);
			}
		}else {
			switch (Integer.valueOf(whatToProcess)) {
			case 77: //Match id
				return populateFFMatchId(WhichSide, Integer.valueOf(whatToProcess), matchAllData);
			}
		}
		return true;
	}
	
	public boolean populateLTNameSuper(String whatToProcess,int WhichSide)
	{
		namesuper = this.nameSupers.stream().filter(ns -> ns.getNamesuperId() == Integer.valueOf(whatToProcess.split(",")[1]))
				.findAny().orElse(null);
		
		lowerThird = new LowerThird("", namesuper.getFirstname(), namesuper.getSurname(),"", "", "", 1, "" ,"",
			null,null,new String[]{namesuper.getSubLine()},null);
		
		if(PopulateL3rdHeader(Integer.valueOf(whatToProcess.split(",")[0]),WhichSide) == true) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			return PopulateL3rdBody(WhichSide, Integer.valueOf(whatToProcess.split(",")[0]));
		} else {
			return false;
		}
	}
	public boolean populateHowOut(String whatToProcess,int WhichSide,MatchAllData matchAllData)
	{
		String how_out_txt = "";
		for(Inning inn : matchAllData.getMatch().getInning()) {
			if(inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1])) {
				for(BattingCard bc : inn.getBattingCard()) {
					if(bc.getPlayerId() == Integer.valueOf(whatToProcess.split(",")[2])) {
						how_out_txt = CricketFunctions.processHowOutText("FOUR-PART-HOW-OUT", bc);
						
						lowerThird = new LowerThird("", bc.getPlayer().getFirstname(), bc.getPlayer().getSurname(),"", String.valueOf(bc.getRuns()), String.valueOf(bc.getBalls()),
								2,"",inn.getBatting_team().getTeamName4(),null,null,new String[]{how_out_txt.replace("|", " "),String.valueOf(bc.getFours()),String.valueOf(bc.getSixes())},
								new String[]{bc.getStrikeRate()});
					}
				}
			}
		}
		
		if(PopulateL3rdHeader(Integer.valueOf(whatToProcess.split(",")[0]),WhichSide) == true) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			return PopulateL3rdBody(WhichSide, Integer.valueOf(whatToProcess.split(",")[0]));
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
		
		if(PopulateL3rdHeader(Integer.valueOf(whatToProcess.split(",")[0]),WhichSide) == true) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			return PopulateL3rdBody(WhichSide, Integer.valueOf(whatToProcess.split(",")[0]));
		} else {
			return false;
		}
	}
	
	public boolean PopulateL3rdHeader(int whatToProcess,int WhichSide) 
	{
		//DJ and Azaz remove unwanted Viz command containers
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			switch(whatToProcess) {
			case 121:
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
			case 117:
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
			case 118: case 122:
				
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
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_"+ WhichSide + 
						"$Select_Subline*FUNCTION*Omo*vis_con SET " + lowerThird.getNumberOfSubLines() + "\0");	
				
				for(int iSubLine = 1; iSubLine <= lowerThird.getNumberOfSubLines(); iSubLine++) {
					
					for(int iTitle = 1; iTitle <= lowerThird.getTitlesText().length; iTitle++) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$"+ iSubLine + "$Data$Title$txt_ " + iTitle + "*ACTIVE SET 1 \0");
					}
					//Hide number of Titles on each strap
					for(int iTitle = lowerThird.getTitlesText().length + 1; iTitle <= 10; iTitle++) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$"+ iSubLine + "$Data$Title$txt_ " + iTitle + "*ACTIVE SET 0 \0");
					}
					
					for(int iStats = 1; iStats <= lowerThird.getStatsText().length; iStats++) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$"+ iSubLine + "$Data$Stat$txt_ " + iStats + "*ACTIVE SET 1 \0");
					}
					//Hide number of Titles on each strap
					for(int iStats = lowerThird.getStatsText().length + 1; iStats <= 10; iStats++) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
								"$Select_Subline$"+ iSubLine + "$Data$Stat$txt_ " + iStats + "*ACTIVE SET 0 \0");
					}
					
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
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Sublines$Side_" + WhichSide + "$Set_Rows_Side_" + WhichSide + 
						"_Position_Y*TRANSFORMATION*POSITION*Y SET " + "25.0" + "\0");
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Overall_Position_Y_In_Out*ANIMATION*KEY*$In_2*VALUE SET 0.0 -50.0 0.0 \0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Overall_Position_Y_In_Out*ANIMATION*KEY*$Out_1*VALUE SET 0.0 -50.0 0.0 \0");

				break;
			}
		}
	}
	public boolean PopulateL3rdBody(int WhichSide, int whatToProcess) 
	{
		for(PrintWriter print_writer : print_writers) {
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				switch (whatToProcess) {
				case 117:
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$1$Data$Right$txt_1*GEOM*TEXT SET " + "STRIKE RATE " + lowerThird.getRightText()[0] + "\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$2$Data$Left$txt_1*GEOM*TEXT SET " + "FOURS " + lowerThird.getLeftText()[1] + "      SIXES " + 
							lowerThird.getLeftText()[2]  + "\0");
					
					break;
				case 118: case 122:
					for(int iStat = 0; iStat < 7; iStat++) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$1$Data$Title$txt_" + (iStat + 1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[iStat] + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$2$Data$Stat$txt_" + (iStat + 1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[iStat] + "\0");
					}
					break;
				case 121:				
					// DJ left text container should use left text variable of L3rd object 
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getLeftText()[0] + "\0");
					break;
				}
				break;
			}
		}
		return true;
	}
	
	public boolean PopulateScorecardFF(int WhichSide, int whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException
	{
		if(PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning) == true) {
			if(PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning) == true) {
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, WhichInning);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public boolean PopulateBowlingCardFF(int WhichSide,int whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException
	{
		if(PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning) == true) {
			if(PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning) == true) {
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, WhichInning);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public boolean populateFFMatchId(int WhichSide, int whatToProcess, MatchAllData matchAllData) throws ParseException
	{
		if(PopulateFfHeader(WhichSide, whatToProcess, matchAllData, 0) == true) {
			if(PopulateFfBody(WhichSide, whatToProcess, matchAllData, 0) == true) {
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, 0);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public boolean populateMatchSummary(int WhichSide, int whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException
	{
		if(PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning) == true) {
			if(PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning) == true) {
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, WhichInning);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public boolean populateFFMatchPromo(int WhichSide, String whatToProcess, MatchAllData matchAllData) throws NumberFormatException, ParseException
	{
		fixture = fixTures.stream().filter(fix -> fix.getMatchnumber() == Integer.valueOf(whatToProcess.split(",")[1])).findAny().orElse(null);
		
		if(PopulateFfHeader(WhichSide, Integer.valueOf(whatToProcess.split(",")[0]), matchAllData, 0) == true) {
			if(PopulateFfBody(WhichSide, Integer.valueOf(whatToProcess.split(",")[0]), matchAllData, 0) == true) {
				return PopulateFfFooter(WhichSide, Integer.valueOf(whatToProcess.split(",")[0]), matchAllData, 0);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean PopulateFfHeader(int WhichSide, int whatToProcess, MatchAllData matchAllData, int WhichInning) 
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return false;
		} else {
			String cout_name="",city_name = "";
			for(Ground ground : Grounds) {
				if(ground.getFullname().contains(matchAllData.getSetup().getVenueName())) {
					city_name = ground.getCity();
				}
			}

			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				
				switch (whatToProcess) {
				case 112: case 113: // Scorecard - BowlingCard
					inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning)
						.findAny().orElse(null);
					if(inning != null) {
						for(PrintWriter print_writer : print_writers) {
							
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
									"$Flag$img_Flag*ACTIVE SET 1 \0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
									"$Flag$img_Shadow*ACTIVE SET 1 \0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" + WhichSide + 
									"$In_Out$Change$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamGroup() + ", " + city_name + "\0");
							
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" + WhichSide + 
									"$In_Out$Change$Select_HeaderTop$Big_First$txt_Team_1*GEOM*TEXT SET " + 
									matchAllData.getMatch().getInning().get(0).getBatting_team().getTeamName1() +"\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" + WhichSide + 
									"$In_Out$Change$Select_HeaderTop$Big_First$txt_Team_2*GEOM*TEXT SET " +
									matchAllData.getMatch().getInning().get(0).getBowling_team().getTeamName1() +"\0");
							
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" + WhichSide + 
									"$In_Out$Change$Select_HeaderTop$Small_First$txt_Team_1*GEOM*TEXT SET " + 
									matchAllData.getMatch().getInning().get(0).getBatting_team().getTeamName1() +"\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" + WhichSide + 
									"$In_Out$Change$Select_HeaderTop$Small_First$txt_Team_2*GEOM*TEXT SET " + 
									matchAllData.getMatch().getInning().get(0).getBowling_team().getTeamName1() +"\0");
							
							switch(whatToProcess) {
							case 112:
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" 
									+ WhichSide + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName4() + "\0");
									
								if(WhichInning == 1) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side"+ WhichSide + 
											"$In_Out$Change$Select_HeaderTop*FUNCTION*Omo*vis_con SET 1 \0");
								}else {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side"+ WhichSide + 
											"$In_Out$Change$Select_HeaderTop*FUNCTION*Omo*vis_con SET 2 \0");
								}
								break;
							case 113:
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" 
									+ WhichSide + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() + "\0");
								
								if(WhichInning == 1) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side"+ WhichSide + 
											"$In_Out$Change$Select_HeaderTop*FUNCTION*Omo*vis_con SET 2 \0");
								}else {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side"+ WhichSide + 
											"$In_Out$Change$Select_HeaderTop*FUNCTION*Omo*vis_con SET 1 \0");
								}
								break;
							}
						}
					} else {
						return false;
					}
					break;
				case 16122: //MATCH SUMMARY
					if(WhichSide == 1) {
						cout_name = "Side1_Text_Move_For_Shrink";
					}else if(WhichSide == 2) {
						cout_name = "Side2_Text_For_Shrink";
					}
					for(PrintWriter print_writer : print_writers) {

						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
							"$Flag$img_Flag*ACTIVE SET 0 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
							"$Flag$img_Shadow*ACTIVE SET 0 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Select_HeaderTop*FUNCTION*Omo*vis_con SET 3 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "MATCH SUMMARY"+ "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Change$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamGroup()
								+ ", " + city_name + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$MoveForExtraData$Overall_Scaling$Header"
								+ "$Off$Bottom_Align$Header_Extra$loop$txt_Extra*GEOM*TEXT SET " + "" +"\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$MoveForExtraData$Overall_Scaling$Header"
								+ "$Off$Bottom_Align$Header_Extra$loop$txt_Extra2*GEOM*TEXT SET " +  "" +"\0");
					}
					break;
					
				case 77: //MATCH ID
					if(WhichSide == 1) {
						cout_name = "Side1_Text_Move_For_Shrink";
					}else if(WhichSide == 2) {
						cout_name = "Side2_Text_For_Shrink";
					}
					
					for(PrintWriter print_writer : print_writers) {

						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
							"$Flag$img_Flag*ACTIVE SET 0 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
								"$Flag$img_Shadow*ACTIVE SET 0 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Select_HeaderTop*FUNCTION*Omo*vis_con SET 3 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + matchAllData.getSetup().getMatchIdent() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Change$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getTournament() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$MoveForExtraData$Overall_Scaling$Header"
								+ "$Off$Bottom_Align$Header_Extra$loop$txt_Extra*GEOM*TEXT SET " + "" +"\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$MoveForExtraData$Overall_Scaling$Header"
								+ "$Off$Bottom_Align$Header_Extra$loop$txt_Extra2*GEOM*TEXT SET " +  "" +"\0");
					}
					break;
				case 1777: //MATCH PROMO
					if(WhichSide == 1) {
						cout_name = "Side1_Text_Move_For_Shrink";
					}else if(WhichSide == 2) {
						cout_name = "Side2_Text_For_Shrink";
					}
					
					for(PrintWriter print_writer : print_writers) {

						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
							"$Flag$img_Flag*ACTIVE SET 0 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
							"$Flag$img_Shadow*ACTIVE SET 0 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Select_HeaderTop*FUNCTION*Omo*vis_con SET 3 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + fixture.getTeamgroup().toUpperCase()+ "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Change$Bottom$txt_Subheader*GEOM*TEXT SET " +  matchAllData.getSetup().getTournament() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$MoveForExtraData$Overall_Scaling$Header"
								+ "$Off$Bottom_Align$Header_Extra$loop$txt_Extra*GEOM*TEXT SET " + "" +"\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$MoveForExtraData$Overall_Scaling$Header"
								+ "$Off$Bottom_Align$Header_Extra$loop$txt_Extra2*GEOM*TEXT SET " +  "" +"\0");
					}
					break;
				}
				break;
			}
			
		}
		return true;
	}
	public boolean PopulateFfBody(int WhichSide, int whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException 
	{
		String container_name = "", how_out_txt = "";
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return false;
		} else {
			switch (whatToProcess) {
			case 112: // Scorecard
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:

					inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning).findAny().orElse(null);
					if(inning == null) {
						return false;
					}
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 3 \0");
						for(int iRow = 1; iRow <= inning.getBattingCard().size(); iRow++) {
							switch (inning.getBattingCard().get(iRow-1).getStatus().toUpperCase()) {
							case CricketUtil.STILL_TO_BAT:
								
								if(inning.getBattingCard().get(iRow-1).getHowOut() == null) {

									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type*FUNCTION*Omo*vis_con SET 0 \0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Still_To_Bat$Data$Name$txt_BatterName*GEOM*TEXT SET "
										+ inning.getBattingCard().get(iRow-1).getPlayer().getTicker_name().toUpperCase() + "\0");
									
								} else {
									
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$Name$txt_BatterName*GEOM*TEXT SET "
										+ inning.getBattingCard().get(iRow-1).getPlayer().getTicker_name().toUpperCase() + "\0");
									
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$How_Out_1$txt_OutType*GEOM*TEXT SET "
										+ inning.getBattingCard().get(iRow-1).getHowOut().replace("_", " ").toLowerCase() + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$How_Out_1$txt_FielderName*GEOM*TEXT SET \0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$How_Out_2$txt_Bold*GEOM*TEXT SET \0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$How_Out_2$txt_BowlerName*GEOM*TEXT SET \0");
									
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$Runs$txt_Runs*GEOM*TEXT SET " 
										+ inning.getBattingCard().get(iRow-1).getRuns() + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$Balls$txt_Balls*GEOM*TEXT SET " 
										+ String.valueOf(inning.getBattingCard().get(iRow-1).getBalls()) + "\0");

								}
								break;
								
							default:
								
								switch (inning.getBattingCard().get(iRow-1).getStatus().toUpperCase()) {
								case CricketUtil.OUT:
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0");
									container_name = "Out";
									break;
								case CricketUtil.NOT_OUT:
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type*FUNCTION*Omo*vis_con SET 2 \0");
									container_name = "Not_Out";
									break;
								}
								
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name + "$Data$Name$txt_BatterName*GEOM*TEXT SET "
										+ inning.getBattingCard().get(iRow-1).getPlayer().getTicker_name().toUpperCase() + "\0");
								
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name + "$Data$Runs$txt_Runs*GEOM*TEXT SET " 
										+ inning.getBattingCard().get(iRow-1).getRuns() + "\0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name + "$Data$Balls$txt_Balls*GEOM*TEXT SET " 
										+ String.valueOf(inning.getBattingCard().get(iRow-1).getBalls()) + "\0");
								
								how_out_txt = CricketFunctions.processHowOutText("FOUR-PART-HOW-OUT", inning.getBattingCard().get(iRow-1));
								
								if(how_out_txt != null && how_out_txt.split("|").length >= 4) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name + 
										"$Data$How_Out_1$txt_OutType*GEOM*TEXT SET " + how_out_txt.split("\\|")[0] + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name 
										+ "$Data$How_Out_1$txt_FielderName*GEOM*TEXT SET " + how_out_txt.split("\\|")[1] + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name 
										+ "$Data$How_Out_2$txt_Bold*GEOM*TEXT SET " + how_out_txt.split("\\|")[2] + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name 
										+ "$Data$How_Out_2$txt_BowlerName*GEOM*TEXT SET " + how_out_txt.split("\\|")[3] + "\0");
								}else {
									// Alipto will make different container for NOT OUT
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name + 
										"$Data$How_Out_1$txt_OutType*GEOM*TEXT SET " + "" + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name 
										+ "$Data$How_Out_1$txt_FielderName*GEOM*TEXT SET " + "NOT OUT" + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name 
										+ "$Data$How_Out_2$txt_Bold*GEOM*TEXT SET " + "" + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name 
										+ "$Data$How_Out_2$txt_BowlerName*GEOM*TEXT SET " + "" + "\0");
								}
								break;
							}
						}
					}
					break;
				}
				break;
			
			case 113: //Bowling Card
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 4 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$1"
								+ "$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0");
						
						for(int j=1;j<=10;j++) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
									+ (j+1) + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 0 \0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$10"
									+ "$Select_Row_Type$FOW$Data$fig_" + j + "*GEOM*TEXT SET " + "" + "\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$11"
									+ "$Select_Row_Type$Score$Data$fig_" + j + "*GEOM*TEXT SET " + "" + "\0");
							
						}
						
						if(inning.getBowlingCard() != null && inning.getBowlingCard().size() > 0) {
							for(int iRow = 1; iRow <= inning.getBowlingCard().size(); iRow++) {
								if(inning.getBowlingCard().get(iRow-1).getRuns() > 0 || 
										((inning.getBowlingCard().get(iRow-1).getOvers()*6)+inning.getBowlingCard().get(iRow-1).getBalls()) > 0) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
										+ (iRow+1) + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 2 \0");
									
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
										+ (iRow+1) + "$Select_Row_Type$Players$Data$txt_Name*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getPlayer().getTicker_name() + "\0");
									
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
											+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Overs*GEOM*TEXT SET " + CricketFunctions.
											OverBalls(inning.getBowlingCard().get(iRow-1).getOvers(), inning.getBowlingCard().get(iRow-1).getBalls()) + "\0");
									
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
											+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Maidens*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getMaidens() + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
											+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Runs*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getRuns() + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
											+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Wickets*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getWickets() + "\0");
									
									if(inning.getBowlingCard().get(iRow-1).getEconomyRate() != null) {
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
												+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Economy*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getEconomyRate() + "\0");
									}else {
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
												+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Economy*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getPlayer().getTicker_name() + "\0");
									}
								}
							}
						}
						
						if(inning.getBowlingCard().size() <= 8) {
							if(inning.getFallsOfWickets() != null) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$10"
										+ "$Select_Row_Type*FUNCTION*Omo*vis_con SET 3 \0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$11"
										+ "$Select_Row_Type*FUNCTION*Omo*vis_con SET 4 \0");
								
								for(FallOfWicket fow : inning.getFallsOfWickets()) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
										+ "10$Select_Row_Type$FOW$Data$fig_" + fow.getFowNumber() + "*GEOM*TEXT SET " + fow.getFowNumber() + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
										+ "11$Select_Row_Type$Score$Data$fig_" + fow.getFowNumber() + "*GEOM*TEXT SET " + fow.getFowRuns() + "\0");
								}
							}
						}else {
							
						}
					}
					break;
				}
				break;
			case 16122: //Match Summary
				
				String teamname = "", teamFlag = ""; 
				int rowId = 0;
				
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning).findAny().orElse(null);
				if(inning == null) {
					return false;
				}
				
				for(PrintWriter print_writer : print_writers) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 5 \0");
					for(int i=1; i<=2; i++ ) {
						for(int j=1; j<=4; j++) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_"
									+ i + "$Row_" + j + "$Batsman*ACTIVE SET 0 \0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_"
									+ i + "$Row_" + j + "$Bowler*ACTIVE SET 0 \0");
						}
					}
					
					for(int i = 1; i <= WhichInning ; i++) {
						if(i == 1) {
							rowId=0;
							if(matchAllData.getMatch().getInning().get(i-1).getBattingTeamId() == matchAllData.getSetup().getTossWinningTeam()) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
										+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 1 \0");
							}else {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
										+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 0 \0");							}
							
						} else {
							rowId=0;
							if(matchAllData.getMatch().getInning().get(i-1).getBattingTeamId() == matchAllData.getSetup().getTossWinningTeam()) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
										+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 1 \0");
							}else {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
										+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 0 \0");
							}
						}
						
						if(matchAllData.getMatch().getInning().get(i-1).getBattingTeamId() == matchAllData.getSetup().getHomeTeamId()) {
							teamname = matchAllData.getSetup().getHomeTeam().getTeamName1();
							teamFlag = matchAllData.getSetup().getHomeTeam().getTeamName4();
						} else {
							teamname = matchAllData.getSetup().getAwayTeam().getTeamName1();
							teamFlag = matchAllData.getSetup().getAwayTeam().getTeamName4();
						}
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
								+ i +"$Tittle$Data$txt_Team*GEOM*TEXT SET " + teamname.toUpperCase() + " \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
								+ i +"$Tittle$FlagGrp$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  teamFlag + " \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
								+ i +"$Tittle$Data$Overs$txt_Overs_Value*GEOM*TEXT SET " + CricketFunctions.OverBalls(matchAllData.getMatch().getInning().get(i-1).
										getTotalOvers(),matchAllData.getMatch().getInning().get(i-1).getTotalBalls()) + " \0");
						
						if(matchAllData.getMatch().getInning().get(i-1).getTotalWickets() >= 10) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
									+ i +"$Tittle$Data$txt_Score*GEOM*TEXT SET " + matchAllData.getMatch().getInning().get(i-1).getTotalRuns() + " \0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
									+ i +"$Tittle$Data$txt_Score*GEOM*TEXT SET " + matchAllData.getMatch().getInning().get(i-1).getTotalRuns() 
									+ " - " + String.valueOf(matchAllData.getMatch().getInning().get(i-1).getTotalWickets()) + " \0");
						}
						
						if(matchAllData.getMatch().getInning().get(i-1).getBattingCard() != null) {
							Collections.sort(matchAllData.getMatch().getInning().get(i-1).getBattingCard(),new CricketFunctions.BatsmenScoreComparator());
							
							for(BattingCard bc : matchAllData.getMatch().getInning().get(i-1).getBattingCard()) {
								if(bc.getRuns() > 0) {
									// DJ to check retired/absent hurt 
									if(!bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
										rowId++;
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i +"$Row_"+rowId+"$Batsman*ACTIVE SET 1 \0");

										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
												"$Batsman$txt_Name*GEOM*TEXT SET " + bc.getPlayer().getTicker_name() + "\0");
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
												"$Batsman$txt_Runs*GEOM*TEXT SET " + bc.getRuns() + "\0");
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
												"$Batsman$txt_Balls*GEOM*TEXT SET " + String.valueOf(bc.getBalls()) + "\0");

										if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
											print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
													"$Batsman$txt_Not-Out*GEOM*TEXT SET " + "*" + "\0");
										} else {
											print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
													"$Batsman$txt_Not-Out*GEOM*TEXT SET " + "" + "\0");
										}
										if(i == 1 && rowId >= 4) {
											break;
										}else if(i == 2 && rowId >= 4) {
											break;
										}
									}
								}
							}
						}	
						if(i == 1) {
							rowId = 0;
						}
						else {
							rowId = 0;
						}
						
						if(matchAllData.getMatch().getInning().get(i-1).getBowlingCard() != null) {
							
							Collections.sort(matchAllData.getMatch().getInning().get(i-1).getBowlingCard(),new CricketFunctions.BowlerFiguresComparator());

							for(BowlingCard boc : matchAllData.getMatch().getInning().get(i-1).getBowlingCard()) {
								
								if(boc.getWickets() > 0) {
									rowId++;
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i +"$Row_"+rowId+"$Bowler*ACTIVE SET 1 \0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
											"$Bowler$txt_Name*GEOM*TEXT SET " + boc.getPlayer().getTicker_name() + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
											"$Bowler$txt_Figs*GEOM*TEXT SET " + boc.getWickets() + "-" + boc.getRuns() + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
											"$Bowler$txt_Overs*GEOM*TEXT SET " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + "\0");
									
									if(i == 1 && rowId >= 4) {
										break;
									}
									else if(i == 2 && rowId >= 4) {
										break;
									}
								}
							}
						}
					}
				}
				break;
				
			case 77: //MATCH Ident
				
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
								+ "$Data$Select_DataForPromo*FUNCTION*Omo*vis_con SET 0 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
								+ "$Data$txt_Team_1*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamName1().toUpperCase() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
								+ "$Data$txt_Team_2*GEOM*TEXT SET " + matchAllData.getSetup().getAwayTeam().getTeamName1().toUpperCase() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Flag_Top$"
								+ "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  matchAllData.getSetup().getHomeTeam().getTeamName4() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Flag_Bottom$"
								+ "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  matchAllData.getSetup().getAwayTeam().getTeamName4() + "\0");
					}
					break;
				}
				break;
				
			case 1777: //MATCH PROMO
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					String newDate = "",date_data = "";
					for(PrintWriter print_writer : print_writers) {
						
						String dayOfWeek = new SimpleDateFormat("EEEE").format(new SimpleDateFormat("dd-MM-yyyy").parse(fixture.getDate()));
						String timeDetail="";
						
						newDate = fixture.getDate().split("-")[0];
						date_data = CricketFunctions.ordinal(Integer.valueOf(newDate)) + " " + Month.of(Integer.valueOf(fixture.getDate().split("-")[1])) + " 2024";
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
								+ "$Data$Select_DataForPromo*FUNCTION*Omo*vis_con SET 0 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
								+ "$Data$Select_DataForPromo*FUNCTION*Omo*vis_con SET 1 \0");
						

						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
									+ "$Data$Select_DataForPromo$ExtraInfo$txt_Info1*GEOM*TEXT SET " + dayOfWeek.toUpperCase() + ", " +date_data + "\0");
						
						if(fixture.getLocalTime() != null) {
							timeDetail = fixture.getLocalTime() + " LOCAL TIME ";
							if(fixture.getGmtTime() != null) {
								timeDetail = timeDetail + "(" + fixture.getGmtTime() + " GMT)";
 							}
						}
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
								+ "$Data$Select_DataForPromo$ExtraInfo$txt_Info2*GEOM*TEXT SET " + timeDetail + "\0");
						
						for(Team tm : Teams) {
							if(tm.getTeamId() == fixture.getHometeamid()) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
										+ "$Data$txt_Team_1*GEOM*TEXT SET "+ tm.getTeamName1() + "\0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Flag_Top$"
										+ "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  tm.getTeamName4() + "\0");
							}
							
							if(tm.getTeamId() == fixture.getAwayteamid()) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
										+ "$Data$txt_Team_2*GEOM*TEXT SET " + tm.getTeamName1() + "\0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Flag_Bottom$"
										+ "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  tm.getTeamName4() + "\0");
							}
						}
						
					}
					break;
				}
				break;
			}
		}
		return true;
	}
	public boolean PopulateFfFooter(int WhichSide, int whatToProcess, MatchAllData matchAllData, int WhichInning) 
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return false;
		} else {

			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				switch (whatToProcess) {
				// Scorecard - BowlingCard
				case 112: case 113:
					inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning).findAny().orElse(null);
				
					if(inning != null) {
						for(PrintWriter print_writer : print_writers) {
							
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Select_FooterType*FUNCTION*Omo*vis_con SET 1 \0");
							
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Select_FooterType$Score$Data$Side"
									+ WhichSide + "$In_Out$Change$In_Out$Extras$txt_Extras_Value*GEOM*TEXT SET " + inning.getTotalExtras() + "\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Select_FooterType$Score$Data$Side"
									+ WhichSide + "$In_Out$Change$In_Out$Overs$txt_Overs_Value*GEOM*TEXT SET " + 
									CricketFunctions.OverBalls(inning.getTotalOvers(),inning.getTotalBalls()) + "\0");
							
							if(matchAllData.getSetup().getTargetOvers() != null) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Select_FooterType$Score$Data$Side"
										+ WhichSide + "$In_Out$Change$In_Out$Overs$txt_Overs_2*GEOM*TEXT SET " + matchAllData.getSetup().getTargetOvers() + "\0");
							}else {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Select_FooterType$Score$Data$Side"
										+ WhichSide + "$In_Out$Change$In_Out$Overs$txt_Overs_2*GEOM*TEXT SET " + "" + "\0");
							}
							
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Select_FooterType$Score$Data$Side"
									+ WhichSide + "$In_Out$Change$Score$txt_Score*GEOM*TEXT SET " + CricketFunctions.getTeamScore(inning, "-", false) + "\0");
						}
					} else {
						return false;
					}
					break;
				 case 16122: //MATCH SUMMARY
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType*FUNCTION*Omo*vis_con SET 2 \0");
						// DJ use proper match summary function
						if(matchAllData.getMatch().getMatchResult() != null) {
							if(matchAllData.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
										"$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.generateMatchSummaryStatus(WhichInning, matchAllData, "MIDDLE").toUpperCase() + "\0");
							}
							else if(matchAllData.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
										"$txt_Info_1*GEOM*TEXT SET " + "MATCH TIED" + "\0");
							}
							else if(matchAllData.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("ABANDONED")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
										"$txt_Info_1*GEOM*TEXT SET " + matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0");
							}
							else if(matchAllData.getMatch().getMatchResult().split(",")[2].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
										"$txt_Info_1*GEOM*TEXT SET " +"MATCH TIED - " + CricketFunctions.generateMatchSummaryStatus(WhichInning, matchAllData, "MIDDLE").toUpperCase() + "\0");
							}
							else {

								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
										"$txt_Info_1*GEOM*TEXT SET " +CricketFunctions.generateMatchSummaryStatus(WhichInning, matchAllData, "MIDDLE").toUpperCase() + "\0");
							}
						}
						else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
									"$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.generateMatchSummaryStatus(WhichInning, matchAllData, "MIDDLE").toUpperCase() + "\0");
							if(matchAllData.getSetup().getTargetType() != null) {
								if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("VJD")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
											"$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.generateMatchSummaryStatus(WhichInning, matchAllData, "MIDDLE").toUpperCase() + "\0");
								}else if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("DLS")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
											"$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.generateMatchSummaryStatus(WhichInning, matchAllData, "MIDDLE").toUpperCase() + "\0");
								}
							}
						}
					}
					break;
					
				case 77: //MATCH ID
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType*FUNCTION*Omo*vis_con SET 2 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
								"$txt_Info_1*GEOM*TEXT SET " + "LIVE FROM " + matchAllData.getSetup().getVenueName() + "\0");
					}
					break;
				case 1777: //MATCH PROMO
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType*FUNCTION*Omo*vis_con SET 2 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
								"$txt_Info_1*GEOM*TEXT SET " + "LIVE FROM " + fixture.getVenue() + "\0");
					}
					break;

				}
				break;
			}
			
		}
		return true;
	}
 
}