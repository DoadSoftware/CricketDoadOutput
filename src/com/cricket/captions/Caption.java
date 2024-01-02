package com.cricket.captions;

import java.io.PrintWriter;
import java.util.List;
import com.cricket.containers.LowerThird;
import com.cricket.model.BattingCard;
import com.cricket.model.Configuration;
import com.cricket.model.FallOfWicket;
import com.cricket.model.Inning;
import com.cricket.model.MatchAllData;
import com.cricket.model.NameSuper;
import com.cricket.model.Player;
import com.cricket.model.Statistics;
import com.cricket.model.StatsType;
import com.cricket.service.CricketService;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class Caption 
{
	public int FirstPlayerId;
	public int SecondPlayerId;
	public int WhichProfileId;
	
	public List<PrintWriter> print_writers; 
	public Configuration config;
	public List<Statistics> statistics;
	public List<StatsType> statsTypes;
	public List<MatchAllData> tournament_matches;
	
	public BattingCard battingCard;
	public Inning inning;
	public Player player;
	public Statistics stat;
	public StatsType statsType;
	public LowerThird lowerThird;
	public CricketService cricketService;
	
	public NameSuper namesuper;
	
	public Caption() {
		super();
	}
	
	public Caption(List<PrintWriter> print_writers, Configuration config, List<Statistics> statistics,
			List<StatsType> statsTypes, List<MatchAllData> tournament_matches,CricketService cricketService) {
		super();
		this.print_writers = print_writers;
		this.config = config;
		this.statistics = statistics;
		this.statsTypes = statsTypes;
		this.tournament_matches = tournament_matches;
		this.cricketService = cricketService;
	}

	public boolean PopulateGraphics(String whatToProcess, int WhichSide, MatchAllData matchAllData) 
		throws InterruptedException, JsonMappingException, JsonProcessingException
	{
		if(whatToProcess.contains(",")) {
			switch (Integer.valueOf(whatToProcess.split(",")[0])) {
			case 112: // Scorecard FF
				return PopulateScorecardFF(WhichSide, Integer.valueOf(whatToProcess.split(",")[0]), matchAllData, Integer.valueOf(whatToProcess.split(",")[1]));
			case 113: // Bowling FF
				return PopulateBowlingCardFF(WhichSide, Integer.valueOf(whatToProcess.split(",")[0]), matchAllData, Integer.valueOf(whatToProcess.split(",")[1]));
			
			
			case 7: // L3rd Profile
				return PopulateL3rdPlayerProfile(whatToProcess,WhichSide, matchAllData);
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
		namesuper =  cricketService.getNameSupers().stream().filter(ns -> ns.getNamesuperId() == Integer.valueOf(whatToProcess.split(",")[1]))
				.findAny().orElse(null);
		
		lowerThird = new LowerThird("", namesuper.getFirstname(), namesuper.getSurname(),"", "", "", 1, "",new String[]{namesuper.getSubLine()},null,null,null);
		
		if(PopulateL3rdHeader(Integer.valueOf(whatToProcess.split(",")[0]),WhichSide) == true) {
			//HideAndShowL3rdSubStrapContainers(WhichSide);
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
		WhichProfileId = Integer.valueOf(whatToProcess.split(",")[2]);
		
		if(FirstPlayerId <= 0 || WhichProfileId <= 0) {
			return false;
		}
		
		stat = statistics.stream().filter(
			st -> st.getPlayer_id() == FirstPlayerId).findAny().orElse(null);
		
		if(stat == null) {
			return false;
		}

		statsType = statsTypes.stream().filter(
			st -> st.getStats_id() == WhichProfileId).findAny().orElse(null);
			
		if(statsType == null) {
			return false;
		}
		
		player = CricketFunctions.getPlayerFromMatchData(stat.getPlayer_id(), matchAllData); 
		if(player == null) {
			return false;
		}
		
		stat.setStats_type(statsType);
		stat = CricketFunctions.updateTournamentDataWithStats(stat, tournament_matches, matchAllData);
		stat = CricketFunctions.updateStatisticsWithMatchData(stat, matchAllData);

		lowerThird = new LowerThird("", player.getFirstname(), player.getSurname(), 
			statsType.getStats_full_name(), "", "", 2, "FLAG",new String[]{"MATCHES", "RUNS", "100s", "50s"},
			new String[]{String.valueOf(stat.getMatches()), String.valueOf(stat.getRuns()), 
			String.valueOf(stat.getHundreds()), String.valueOf(stat.getFifties())},null,null);
		
		if(PopulateL3rdHeader(Integer.valueOf(whatToProcess.split(",")[0]),WhichSide) == true) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			return PopulateL3rdBody(WhichSide, Integer.valueOf(whatToProcess));
		} else {
			return false;
		}
	}
	public boolean PopulateL3rdHeader(int whatToProcess,int WhichSide) 
	{
		for(PrintWriter print_writer : print_writers) {
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				switch(whatToProcess) {
				case 121:
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_"+ WhichSide + 
							"$Select_Flags*FUNCTION*Omo*vis_con SET 0 \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name$Change_Out$txt_Name*GEOM*TEXT SET " + lowerThird.getFirstName() + " " + lowerThird.getSurName() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide 
							+ "$Name$Change_Out$txt_Designation*GEOM*TEXT SET " + "" + "\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Top_Line$Bottom_Align$Data$Side_" + WhichSide +
							"$Name$Change_Out$Score*ACTIVE SET 0 \0");
					
					break;
				}
				break;
			}
		}
		return true;
	}
	public void HideAndShowL3rdSubStrapContainers(int WhichSide)
	{
		for(PrintWriter print_writer : print_writers) {
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				//Show number of sublines
				for(int iSubLine = 1; iSubLine <= lowerThird.getNumberOfSubLines(); iSubLine++) {
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LowerThirdGfx$SubLine" + iSubLine + "*ACTIVE SET 1\0");
					
					//Show number of Titles on each strap
					for(int iTitle = 1; iTitle <= lowerThird.getTitlesText().length; iTitle++) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LowerThirdGfx$SubLine" 
							+ iSubLine + "$TitleText_" + iTitle + "*ACTIVE SET 1\0");
					}
					//Hide number of Titles on each strap
					for(int iTitle = lowerThird.getTitlesText().length + 1; iTitle <= 10; iTitle++) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LowerThirdGfx$SubLine" 
							+ iSubLine + "$TitleText_" + iTitle + "*ACTIVE SET 0\0");
					}
					//Show number of Stats on each strap
					for(int iStats = 1; iStats <= lowerThird.getStatsText().length; iStats++) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LowerThirdGfx$SubLine" 
							+ iSubLine + "$StatText_" + iStats + "*ACTIVE SET 1\0");
					}
					//Hide number of Stats on each strap
					for(int iStats = lowerThird.getStatsText().length + 1; iStats <= 10; iStats++) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LowerThirdGfx$SubLine" 
							+ iSubLine + "$StatText_" + iStats + "*ACTIVE SET 0\0");
					}
					//Show Left on each strap
					if(lowerThird.getLeftText().length > 0) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LowerThirdGfx$SubLine" 
								+ iSubLine + "$LeftText_" + 1 + "*ACTIVE SET 1\0");
					} else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LowerThirdGfx$SubLine" 
								+ iSubLine + "$LeftText_" + 1 + "*ACTIVE SET 0\0");
					}
					//Show Right on each strap
					if(lowerThird.getRightText().length > 0) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LowerThirdGfx$SubLine" 
								+ iSubLine + "$RightText_" + 1 + "*ACTIVE SET 1\0");
					} else {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LowerThirdGfx$SubLine" 
								+ iSubLine + "$RightText_" + 1 + "*ACTIVE SET 0\0");
					}
				}
				for(int iSubLine = lowerThird.getNumberOfSubLines() + 1; iSubLine <= 4; iSubLine++) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LowerThirdGfx$SubLine" + iSubLine + "*ACTIVE SET 0\0");
				}
				//Set position for header strap [Subline 2:  		 00.0		 00.0]
				//Set Height for L3rd [Subline 2:  		-50.0		-50.0]
				
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
				case 121:
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_"+ WhichSide + 
							"$Select_Subline*FUNCTION*Omo*vis_con SET 1 \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1$Data$Left*ACTIVE SET 1 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1$Data$Right*ACTIVE SET 0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1$Data$Title*ACTIVE SET 0 \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide +
							"$Select_Subline$1$Data$Stat*ACTIVE SET 0 \0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Position_With_Graphics$Sublines$Side_" + WhichSide 
							+ "$Select_Subline$1$Data$Left$txt_1*GEOM*TEXT SET " + lowerThird.getTitlesText()[0] + "\0");
					break;
				case 7: // L3rd Profile
					for(int iStat = 0; iStat <= 1; iStat++) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LowerThirdGfx$SubLine" 
							+ 1 + "$TitleText" + (iStat + 1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[iStat] + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LowerThirdGfx$SubLine" 
							+ 2 + "$StatsText" + (iStat + 1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[iStat] + "\0");
					}
					break;
				}
				// on error return false;
				break;
			}
		}
		return true;
	}
	
	public boolean PopulateScorecardFF(int WhichSide, int whatToProcess, MatchAllData matchAllData, int WhichInning)
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
	public boolean PopulateBowlingCardFF(int WhichSide,int whatToProcess, MatchAllData matchAllData, int WhichInning)
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
	public boolean populateFFMatchId(int WhichSide, int whatToProcess, MatchAllData matchAllData)
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
	
	public boolean PopulateFfHeader(int WhichSide, int whatToProcess, MatchAllData matchAllData, int WhichInning) 
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return false;
		} else {

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
									"$In_Out$Change$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getTournament() +"\0");
							
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
				case 77: //MATCH ID
					String cout_name="";
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
				}
				break;
			}
			
		}
		return true;
	}
	public boolean PopulateFfBody(int WhichSide, int whatToProcess, MatchAllData matchAllData, int WhichInning) 
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
						
						if(inning.getBowlingCard() != null) {
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
				
			case 77: //MATCH ID
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
					
				case 77: //MATCH ID
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType*FUNCTION*Omo*vis_con SET 2 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
								"$txt_Info_1*GEOM*TEXT SET " + "LIVE FROM " + matchAllData.getSetup().getVenueName() + "\0");
					}
					break;
				}
				break;
			}
			
		}
		return true;
	}
 
}