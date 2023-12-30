package com.cricket.captions;

import java.io.PrintWriter;
import java.util.List;
import com.cricket.containers.LowerThird;
import com.cricket.model.Configuration;
import com.cricket.model.MatchAllData;
import com.cricket.model.Player;
import com.cricket.model.Statistics;
import com.cricket.model.StatsType;
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
	
	public Player player;
	public Statistics stat;
	public StatsType statsType;
	public LowerThird lowerThird;
	
	public Caption() {
		super();
	}
	
	public Caption(List<PrintWriter> print_writers, Configuration config, List<Statistics> statistics,
			List<StatsType> statsTypes, List<MatchAllData> tournament_matches) {
		super();
		this.print_writers = print_writers;
		this.config = config;
		this.statistics = statistics;
		this.statsTypes = statsTypes;
		this.tournament_matches = tournament_matches;
	}

	public String PopulateGraphics(int whatToProcess, int WhichSide, MatchAllData matchAllData, int WhichInning) 
		throws InterruptedException, JsonMappingException, JsonProcessingException
	{
		switch (whatToProcess) {
		case 1: // Scorecard FF
			return PopulateScorecardFF(WhichSide, matchAllData, WhichInning);
		case 2: // Bowling FF
			return PopulateBowlingCardFF(WhichSide, matchAllData, WhichInning);
		case 7: // L3rd Profile
			return PopulateL3rdPlayerProfile(whatToProcess,WhichSide, matchAllData, WhichInning);
		}
		return CricketUtil.YES;
	}	
	public String PopulateL3rdPlayerProfile(int whatToProcess, int WhichSide, MatchAllData matchAllData, int WhichInning) 
		throws JsonMappingException, JsonProcessingException, InterruptedException
	{
		if(FirstPlayerId <= 0 || WhichProfileId <= 0) {
			return CricketUtil.NO;
		}
		
		stat = statistics.stream().filter(
			st -> st.getPlayer_id() == FirstPlayerId).findAny().orElse(null);
		
		if(stat == null) {
			return CricketUtil.NO;
		}

		statsType = statsTypes.stream().filter(
			st -> st.getStats_id() == WhichProfileId).findAny().orElse(null);
			
		if(statsType == null) {
			return CricketUtil.NO;
		}
		
		player = CricketFunctions.getPlayerFromMatchData(stat.getPlayer_id(), matchAllData); 
		if(player == null) {
			return CricketUtil.NO;
		}
		
		stat.setStats_type(statsType);
		stat = CricketFunctions.updateTournamentDataWithStats(stat, tournament_matches, matchAllData);
		stat = CricketFunctions.updateStatisticsWithMatchData(stat, matchAllData);

		lowerThird = new LowerThird("", player.getFirstname(), player.getSurname(), 
			statsType.getStats_full_name(), "", "", 2, 
			new String[]{"MATCHES", "RUNS", "100s", "50s"},
			new String[]{String.valueOf(stat.getMatches()), String.valueOf(stat.getRuns()), 
			String.valueOf(stat.getHundreds()), String.valueOf(stat.getFifties())},null,null);
		
		if(PopulateL3rdHeader(WhichSide).equalsIgnoreCase(CricketUtil.YES)) {
			HideAndShowL3rdSubStrapContainers(WhichSide);
			return PopulateL3rdBody(WhichSide, whatToProcess);
		} else {
			return CricketUtil.NO;
		}
	}
	public String PopulateL3rdHeader(int WhichSide) 
	{
		for(PrintWriter print_writer : print_writers) {
			switch (config.getBroadcaster().toUpperCase()) {
			case "ICC-U19-2023":
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$BattingCard$Info$ExtrasGrp$Extras*GEOM*TEXT SET " 
					+ lowerThird.getFirstName()  + "\0");
				// on error return CricketUtil.NO;
				break;
//			case "IPL-2023":
//				print_writer.println("-1 RENDERER*TREE*$Main$BattingCard$Info$ExtrasGrp$Extras*GEOM*TEXT SET " 
//						+ matchAllData.getSetup().getHomeTeam().getTeamName1()  + "\0");
//				break;
			}
		}
		return CricketUtil.YES;
	}
	public void HideAndShowL3rdSubStrapContainers(int WhichSide)
	{
		for(PrintWriter print_writer : print_writers) {
			switch (config.getBroadcaster().toUpperCase()) {
			case "ICC-U19-2023":
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
				break;
			}
		}
	}
	public String PopulateL3rdBody(int WhichSide, int whatToProcess) 
	{
		for(PrintWriter print_writer : print_writers) {
			switch (config.getBroadcaster().toUpperCase()) {
			case "ICC-U19-2023":
				switch (whatToProcess) {
				case 7: // L3rd Profile
					for(int iStat = 0; iStat <= 3; iStat++) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LowerThirdGfx$SubLine" 
							+ 1 + "$TitleText" + (iStat + 1) + "*GEOM*TEXT SET " + lowerThird.getTitlesText()[iStat] + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$LowerThirdGfx$SubLine" 
							+ 2 + "$StatsText" + (iStat + 1) + "*GEOM*TEXT SET " + lowerThird.getStatsText()[iStat] + "\0");
					}
					break;
				}
				// on error return CricketUtil.NO;
				break;
//			case "IPL-2023":
//				print_writer.println("-1 RENDERER*TREE*$Main$BattingCard$Info$ExtrasGrp$Extras*GEOM*TEXT SET " 
//						+ matchAllData.getSetup().getHomeTeam().getTeamName1()  + "\0");
//				break;
			}
		}
		return CricketUtil.YES;
	}
	public String PopulateScorecardFF(int WhichSide, MatchAllData matchAllData, int WhichInning)
	{
		for(PrintWriter print_writer : print_writers) {
			if(PopulateFfHeader(WhichSide, matchAllData, WhichInning).equalsIgnoreCase(CricketUtil.YES)) {
				switch (config.getBroadcaster().toUpperCase()) {
				case "ICC-U19-2023":
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$BattingCard$Info$ExtrasGrp$Extras*GEOM*TEXT SET " 
						+ matchAllData.getSetup().getHomeTeam().getTeamName1()  + "\0");
					// on error return CricketUtil.NO;
					break;
//				case "IPL-2023":
//					print_writer.println("-1 RENDERER*TREE*$Main$BattingCard$Info$ExtrasGrp$Extras*GEOM*TEXT SET " 
//							+ matchAllData.getSetup().getHomeTeam().getTeamName1()  + "\0");
//					break;
				}
			} else {
				return CricketUtil.NO;
			}
			return PopulateFfFooter(WhichSide, matchAllData, WhichInning);
		}
		return CricketUtil.YES;
	}
	public String PopulateBowlingCardFF(int WhichSide, MatchAllData matchAllData, int WhichInning)
	{
		for(PrintWriter print_writer : print_writers) {
			PopulateFfHeader(WhichSide, matchAllData, WhichInning);
			switch (config.getBroadcaster().toUpperCase()) {
			case "ICC-U19-2023":
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$BattingCard$Info$ExtrasGrp$Extras*GEOM*TEXT SET " 
						+ matchAllData.getSetup().getHomeTeam().getTeamName1()  + "\0");
				// on error return CricketUtil.NO;
				break;
//			case "IPL-2023":
//				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$BattingCard$Info$ExtrasGrp$Extras*GEOM*TEXT SET " 
//						+ matchAllData.getSetup().getHomeTeam().getTeamName1()  + "\0");
//				break;
			}
			PopulateFfFooter(WhichSide, matchAllData, WhichInning);
		}
		return CricketUtil.YES;
	}
	public String PopulateFfHeader(int WhichSide, MatchAllData matchAllData, int WhichInning) 
	{
		for(PrintWriter print_writer : print_writers) {
			switch (config.getBroadcaster().toUpperCase()) {
			case "ICC-U19-2023":
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$BattingCard$Info$ExtrasGrp$Extras*GEOM*TEXT SET " 
						+ matchAllData.getMatch().getInning().get(WhichInning-1).getTotalRuns()  + "\0");
				break;
//			case "IPL-2023":
//				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$BattingCard$Info$ExtrasGrp$Extras*GEOM*TEXT SET " 
//						+ matchAllData.getSetup().getHomeTeam().getTeamName1()  + "\0");
//				break;
			}
		}
		return CricketUtil.YES;
	}
	public String PopulateFfFooter(int WhichSide, MatchAllData matchAllData, int WhichInning) 
	{
		for(PrintWriter print_writer : print_writers) {
			switch (config.getBroadcaster().toUpperCase()) {
			case "ICC-U19-2023":
				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$BattingCard$Info$ExtrasGrp$Extras*GEOM*TEXT SET " 
						+ matchAllData.getMatch().getInning().get(WhichInning-1).getTotalRuns()  + "\0");
				break;
//			case "IPL-2023":
//				print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$Main$BattingCard$Info$ExtrasGrp$Extras*GEOM*TEXT SET " 
//						+ matchAllData.getSetup().getHomeTeam().getTeamName1()  + "\0");
//				break;
			}
		}
		return CricketUtil.YES;
	}
 
}