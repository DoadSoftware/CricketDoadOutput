package com.cricket.captions;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.cricket.containers.Infobar;
import com.cricket.model.BattingCard;
import com.cricket.model.BowlingCard;
import com.cricket.model.Commentator;
import com.cricket.model.Configuration;
import com.cricket.model.DaySession;
import com.cricket.model.DuckWorthLewis;
import com.cricket.model.Event;
import com.cricket.model.Ground;
import com.cricket.model.InfobarStats;
import com.cricket.model.Inning;
import com.cricket.model.MatchAllData;
import com.cricket.model.Player;
import com.cricket.model.Statistics;
import com.cricket.model.StatsType;
import com.cricket.model.Team;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class InfobarGfx 
{
	public Configuration config;
	public String slashOrDash = "-", WhichProfile = "", containerName = "", status = "", previous_sixes = "", 
			stats_text = "", par_Overs="", Comms_Name,color = "", color2 = "", prev_score = "", new_score = "",prev_wicket = "", new_wicket = "", prevTeamScore = "", currTeamScore = "";
	
//	int runs = 0,wicket = 0;
//	List<String> allData = new ArrayList<String>();
	
	public int FirstPlayerId,lastXballs,sponsor_omo,infobarStatsId,rowId=0,challengedRuns;

	public Inning inning = new Inning();
	public Team team = new Team();
	public Infobar infobar = new Infobar();
	public Animation this_animation = new Animation();
	
	public List<Statistics> statistics;
	public List<StatsType> statsTypes;
	public List<MatchAllData> tournament_matches;
	public List<InfobarStats> infobarStats;
	public List<Ground> Grounds;
	public List<DuckWorthLewis> dls;
	public List<Commentator> Commentators;
	public List<Player> Players;

	public List<PrintWriter> print_writers; 
	public List<BattingCard> battingCardList = new ArrayList<BattingCard>();
	public BowlingCard bowlingCard = new BowlingCard();
	public List<String> this_data_str = new ArrayList<String>();
	
	public Player player;
	public Statistics stat;
	public StatsType statsType;
	public InfobarStats infoBarStats;
	public Ground ground;
	
	public InfobarGfx() {
		super();
		this.infobar.setLast_full_section(null);
	}

	public InfobarGfx(Configuration config, String slashOrDash, List<PrintWriter> print_writers, List<Statistics> statistics,
			List<StatsType> statsTypes, List<InfobarStats> infobarStats, List<Ground> Grounds, List<Commentator> commentators,
			List<MatchAllData> tournament_matches, List<DuckWorthLewis> dls, List<Player> players) {
		super();
		this.config = config;
		this.slashOrDash = slashOrDash;
		this.print_writers = print_writers;
		this.statistics = statistics;
		this.statsTypes = statsTypes;
		this.infobarStats = infobarStats;
		this.Grounds = Grounds;
		this.Commentators = commentators;
		this.tournament_matches = tournament_matches;
		this.dls = dls;
		this.Players = players;
	}

	public String populatebonus(String whatToProcess,int WhichSide,MatchAllData matchAllData) throws InterruptedException
	{
		if(matchAllData.getEventFile().getEvents().get(matchAllData.getEventFile().getEvents().size()-1).getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50)) {
			int bonus = 0;
			int challengeRuns = 0;
			challengeRuns = matchAllData.getEventFile().getEvents().get(matchAllData.getEventFile().getEvents().size()-1).getEventRuns();
			bonus = matchAllData.getEventFile().getEvents().get(matchAllData.getEventFile().getEvents().size()-1).getEventExtraRuns();
			
			if((bonus*2) >= challengeRuns) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$Bonus$BackText$img_txt2*GEOM*TEXT SET " 
						+ "+" + bonus + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$Bonus$FrontText$Bonus_Green*GEOM*TEXT SET " 
						+ "+" + bonus + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$Bonus$FrontText$Select*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$Bonus$BackText$img_txt2*GEOM*TEXT SET " 
						+ "-" + bonus + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$Bonus$FrontText$Bonus_Red*GEOM*TEXT SET " 
						+ "-" + bonus + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$Bonus$FrontText$Select*FUNCTION*Omo*vis_con SET 0 \0",print_writers);
			}
			return Constants.OK;
		}else {
			return "50-50 is not logged";
		}
		
	}
	public String updateInfobar(List<PrintWriter> print_writers,MatchAllData matchAllData) throws InterruptedException, CloneNotSupportedException, IOException {

		switch (config.getBroadcaster()) {
		case Constants.ISPL:
			inning = matchAllData.getMatch().getInning().stream().filter(
					inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				if(inning == null) {
					return "updateInfobar: Inning return is NULL";
				}
				
//				stats_text = CricketFunctions.generateMatchSummaryStatus(inning.getInningNumber(), matchAllData, 
//						CricketUtil.FULL, "|", config.getBroadcaster()).toUpperCase();
//				System.out.println("stats_text = " + stats_text);
				if(CricketFunctions.getRequiredRuns(matchAllData) <= 0 || inning.getTotalWickets() >= 10 || 
						(CricketFunctions.getRequiredRuns(matchAllData) > 0 && matchAllData.getMatch().getInning().get(1).getTotalWickets() >= 10 
						|| matchAllData.getMatch().getInning().get(1).getTotalOvers() >= matchAllData.getSetup().getMaxOvers())) {
					System.out.println("inside = " + infobar.getFull_section());
					if(infobar.isResult_on_screen() == false) {
						if(infobar.getFull_section() != null && !infobar.getFull_section().isEmpty()) {
							System.out.println("1");
							this.infobar.setFull_section(CricketUtil.RESULT);
							populateFullSection(print_writers, matchAllData, 2);
							this_animation.ChangeOn("Alt_1", print_writers, config);
							TimeUnit.MILLISECONDS.sleep(2000);
							populateFullSection(print_writers, matchAllData, 1);
							this_animation.CutBack("Alt_1", print_writers, config);
							
							infobar.setFull_section(CricketUtil.RESULT);
//							this_animation.infobar.setFull_section(CricketUtil.RESULT);
							infobar.setResult_on_screen(true);
						}else{
							System.out.println("2");
							infobar.setResult_on_screen(true);
							
							this.infobar.setFull_section(CricketUtil.RESULT);
							populateFullSection(print_writers, matchAllData, 1);
							this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage3_In", "START");
							if(infobar.getMiddle_section() != null && !infobar.getMiddle_section().isEmpty()) {
								this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage1_In", "SHOW 0.0");
							}
							if(infobar.getRight_bottom() != null && !infobar.getRight_bottom().isEmpty()) {
								this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage2_In", "SHOW 0.0");
							}
							infobar.setMiddle_section("");
							infobar.setRight_bottom("");
							
							infobar.setFull_section(CricketUtil.RESULT);
							
							System.out.println("infobar.getFull_section() = " + infobar.getFull_section());
						}
					}
				}else {
					if(infobar.getFull_section() != null && !infobar.getFull_section().isEmpty()) {
						populateFullSection(print_writers, matchAllData, 1);   
					}
					
					if(infobar.getRight_section() != null && !infobar.getRight_section().isEmpty()) {
						populateVizInfobarRightSection(true,print_writers, matchAllData, 1, 0);
					}
					infobar.setResult_on_screen(false);
				}
				
				populateInfobarTeamNameScore(true,print_writers,matchAllData,2);
				if(infobar.isChallengeRunOnScreen()) {
					populateChallengedSection(true,print_writers, matchAllData, 1);
				}
				
				
				if(infobar.getMiddle_section() != null && !infobar.getMiddle_section().isEmpty()) {
					if(infobar.getMiddle_section().equalsIgnoreCase(CricketUtil.RESULT) || infobar.getMiddle_section().equalsIgnoreCase("RESULTS")) {
					} else {
						stats_text = CricketFunctions.generateMatchSummaryStatus(inning.getInningNumber(), matchAllData, 
							CricketUtil.FULL, "|", config.getBroadcaster()).toUpperCase();
						if(stats_text.contains(" " + CricketUtil.BEAT + " ") || stats_text.contains(CricketUtil.TIED)) {
							this.infobar.setMiddle_section(CricketUtil.RESULT);
							this.infobar.setFreeText(Arrays.asList(stats_text.split("\\|")));
							populateVizInfobarMiddleSection(print_writers, matchAllData, 2);
							this_animation.ChangeOn("Alt_2,", print_writers, config);
							TimeUnit.MILLISECONDS.sleep(2000);
							populateVizInfobarMiddleSection(print_writers, matchAllData, 1);
							this_animation.CutBack("Alt_2,", print_writers, config);
						} else {
							if(!infobar.getMiddle_section().equalsIgnoreCase(CricketUtil.BATSMAN)) {
								populateVizInfobarMiddleSection(print_writers, matchAllData, 1);
								populateVizInfobarMiddleSection(print_writers, matchAllData, 2);
							}else {
								populateCurrentBatsmen(print_writers, matchAllData, 2);
								populateCurrentBatsmen(print_writers, matchAllData, 1);
							}
							
							if(infobar.getRight_bottom() != null && !infobar.getRight_bottom().isEmpty()) {
								if(!infobar.getRight_bottom().equalsIgnoreCase(CricketUtil.BOWLER)) {
									populateVizInfobarRightBottom(print_writers, matchAllData, 1, 1);
								}else {
									populateVizInfobarBowler(print_writers, matchAllData, 1);
								}
							}
						}
					}
				}
			break;
		case Constants.ICC_U19_2023: 
			
			inning = matchAllData.getMatch().getInning().stream().filter(
				inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			if(inning == null) {
				return "updateInfobar: Inning return is NULL";
			}

			populateInfobarTeamNameScore(true,print_writers,matchAllData,2);
			if(infobar.getLeft_bottom() != null && !infobar.getLeft_bottom().isEmpty()) {
				populateVizInfobarLeftBottom(print_writers, matchAllData, 1);
			}
			
			if(infobar.getMiddle_section() != null && !infobar.getMiddle_section().isEmpty()) {
				if(infobar.getMiddle_section().equalsIgnoreCase(CricketUtil.RESULT) || infobar.getMiddle_section().equalsIgnoreCase("RESULTS")) {
				} else {
					stats_text = CricketFunctions.generateMatchSummaryStatus(inning.getInningNumber(), matchAllData, 
						CricketUtil.FULL, "|", config.getBroadcaster()).toUpperCase();
					if(stats_text.contains(" " + CricketUtil.BEAT + " ") || stats_text.contains(CricketUtil.TIED)) {
						this.infobar.setMiddle_section(CricketUtil.RESULT);
						this.infobar.setFreeText(Arrays.asList(stats_text.split("\\|")));
						populateVizInfobarMiddleSection(print_writers, matchAllData, 2);
						this_animation.ChangeOn("Alt_2,", print_writers, config);
						TimeUnit.MILLISECONDS.sleep(2000);
						populateVizInfobarMiddleSection(print_writers, matchAllData, 1);
						this_animation.CutBack("Alt_2,", print_writers, config);
					} else {
						if(!infobar.getMiddle_section().equalsIgnoreCase(CricketUtil.BATSMAN)) {
							populateVizInfobarMiddleSection(print_writers, matchAllData, 1);
						}else {
							populateCurrentBatsmen(print_writers, matchAllData, 1);
						}
						if(infobar.getRight_section() != null && !infobar.getRight_section().isEmpty()) {
							if(infobar.getRight_section().equalsIgnoreCase(CricketUtil.BOWLER)) {
								populateVizInfobarBowler(print_writers, matchAllData, 1);
								populateVizInfobarRightBottom(print_writers, matchAllData, 1, 1);
							}else {
								populateVizInfobarRightSection(true,print_writers, matchAllData, 1, 1);
							}
						}
					}
				}
			}
			break;
			
		case Constants.BENGAL_T20:
			
			inning = matchAllData.getMatch().getInning().stream().filter(
				inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			if(inning == null) {
				return "updateInfobar: Inning return is NULL";
			}

			if (CricketFunctions.getRequiredRuns(matchAllData) == 0 || matchAllData.getMatch().getInning().get(1).getTotalWickets() >= 10 || 
					CricketFunctions.getRequiredBalls(matchAllData) == 0) {
				
				if(infobar.isResult_on_screen() == false) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Left$In$img_Badge*TEXTURE*IMAGE SET " + 
							Constants.BENGAL_ICONS_PATH + matchAllData.getSetup().getHomeTeam().getTeamName4() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Right$In$img_Badge*TEXTURE*IMAGE SET " + 
							Constants.BENGAL_ICONS_PATH + matchAllData.getSetup().getAwayTeam().getTeamName4() + "\0", print_writers);

					if(CricketFunctions.getRequiredRuns(matchAllData) <= 1 || CricketFunctions.getWicketsLeft(matchAllData, inning.getInningNumber()) <= 0 || 
							CricketFunctions.getRequiredBalls(matchAllData)  == 0) {
						
						if(CricketFunctions.getRequiredRuns(matchAllData) == 0 || CricketFunctions.getWicketsLeft(matchAllData, inning.getInningNumber()) <= 0 || 

								CricketFunctions.getRequiredBalls(matchAllData)  == 0) {
							
							if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Title*GEOM*TEXT SET " + 
										matchAllData.getMatch().getInning().get(1).getBowling_team().getTeamName1().toUpperCase() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Subtitle*GEOM*TEXT SET " + 
										"WIN BY SUPER OVER" + "\0", print_writers);
							}else {
								if(CricketFunctions.getRequiredRuns(matchAllData) == 0) {
									
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Title*GEOM*TEXT SET " + 
											CricketFunctions.generateMatchSummaryStatus(2, matchAllData, CricketUtil.FULL, "|", config.getBroadcaster()).toUpperCase().split(" WIN ")[0] + "\0", print_writers);
									
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Subtitle*GEOM*TEXT SET " + 
											"WIN " + CricketFunctions.generateMatchSummaryStatus(2, matchAllData, CricketUtil.FULL, "|", config.getBroadcaster()).toUpperCase().split(" WIN ")[1] + "\0", print_writers);
									
									
//									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Title*GEOM*TEXT SET " + 
//											matchAllData.getMatch().getInning().get(1).getBatting_team().getTeamName1().toUpperCase() + "\0", print_writers);
//									
//									if(matchAllData.getSetup().getTargetType() != null) {
//										if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.DLS)) {
//											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Subtitle*GEOM*TEXT SET " + 
//													"WIN BY " + CricketFunctions.getWicketsLeft(matchAllData, inning.getInningNumber()) + " WICKET" + 
//													CricketFunctions.Plural(CricketFunctions.getWicketsLeft(matchAllData, inning.getInningNumber())).toUpperCase() + " (DLS)" + "\0", print_writers);
//										}else if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.VJD)) {
//											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Subtitle*GEOM*TEXT SET " + 
//													"WIN BY " + CricketFunctions.getWicketsLeft(matchAllData, inning.getInningNumber()) + " WICKET" + 
//													CricketFunctions.Plural(CricketFunctions.getWicketsLeft(matchAllData, inning.getInningNumber())).toUpperCase() + " (VJD)" + "\0", print_writers);
//										}
//								    }else {
//								    	CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Subtitle*GEOM*TEXT SET " + 
//												"WIN BY " + CricketFunctions.getWicketsLeft(matchAllData, inning.getInningNumber()) + " WICKET" + 
//												CricketFunctions.Plural(CricketFunctions.getWicketsLeft(matchAllData, inning.getInningNumber())).toUpperCase() + "\0", print_writers);
//								    }
								}else if (CricketFunctions.getWicketsLeft(matchAllData,inning.getInningNumber()) <= 0 || 
										CricketFunctions.getRequiredBalls(matchAllData)  == 0) {
									
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Title*GEOM*TEXT SET " + 
											CricketFunctions.generateMatchSummaryStatus(2, matchAllData, CricketUtil.FULL, "|", config.getBroadcaster()).toUpperCase().split(" WIN ")[0] + "\0", print_writers);
									
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Subtitle*GEOM*TEXT SET " + 
											"WIN " + CricketFunctions.generateMatchSummaryStatus(2, matchAllData, CricketUtil.FULL, "|", config.getBroadcaster()).toUpperCase().split(" WIN ")[1] + "\0", print_writers);
									
									
//									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Title*GEOM*TEXT SET " + 
//											matchAllData.getMatch().getInning().get(1).getBowling_team().getTeamName1().toUpperCase() + "\0", print_writers);
//									
//									if(matchAllData.getSetup().getTargetType() != null) {
//										if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.DLS)) {
//											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Subtitle*GEOM*TEXT SET " + 
//													"WIN BY " + Integer.valueOf(CricketFunctions.getRequiredRuns(matchAllData) - 1) + " RUN" + 
//													CricketFunctions.Plural(Integer.valueOf(CricketFunctions.getRequiredRuns(matchAllData) - 1)).toUpperCase() + " (DLS)" + "\0", print_writers);
//										}else if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.VJD)) {
//											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Subtitle*GEOM*TEXT SET " + 
//													"WIN BY " + Integer.valueOf(CricketFunctions.getRequiredRuns(matchAllData) - 1) + " RUN" + 
//													CricketFunctions.Plural(Integer.valueOf(CricketFunctions.getRequiredRuns(matchAllData) - 1)).toUpperCase() + " (VJD)" + "\0", print_writers);
//										}
//								    }else {
//								    	CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Subtitle*GEOM*TEXT SET " + 
//												"WIN BY " + Integer.valueOf(CricketFunctions.getRequiredRuns(matchAllData) - 1) + " RUN" + 
//												CricketFunctions.Plural(Integer.valueOf(CricketFunctions.getRequiredRuns(matchAllData) - 1)).toUpperCase() + "\0", print_writers);
//								    }
								}
							}
						}
					}
					
					TimeUnit.MILLISECONDS.sleep(2000);
					
					this_animation.processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Infobar_To_Result", "START");
					this_animation.processAnimation(Constants.FRONT, print_writers, "Loop", "START");
					
					infobar.setResult_on_screen(true);
				}
			}else if (CricketFunctions.getRequiredRuns(matchAllData) == 1 && CricketFunctions.getRequiredBalls(matchAllData) <= 0 
		    		|| CricketFunctions.getRequiredRuns(matchAllData) == 1 && matchAllData.getMatch().getInning().get(1).getTotalWickets() >= 10) {
				
				if(infobar.isResult_on_screen() == false) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Left$In$img_Badge*TEXTURE*IMAGE SET " + 
							Constants.BENGAL_ICONS_PATH + matchAllData.getSetup().getHomeTeam().getTeamName4() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Right$In$img_Badge*TEXTURE*IMAGE SET " + 
							Constants.BENGAL_ICONS_PATH + matchAllData.getSetup().getAwayTeam().getTeamName4() + "\0", print_writers);

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Title*GEOM*TEXT SET " + 
							"MATCH" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Subtitle*GEOM*TEXT SET " + 
							"TIED" + "\0", print_writers);
					
					TimeUnit.MILLISECONDS.sleep(2000);
					
					this_animation.processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Infobar_To_Result", "START");
					this_animation.processAnimation(Constants.FRONT, print_writers, "Loop", "START");
					
					infobar.setResult_on_screen(true);
				}
			}else if (CricketFunctions.getRequiredRuns(matchAllData) > 0 && CricketFunctions.getRequiredBalls(matchAllData) <= 0 
		    		|| CricketFunctions.getRequiredRuns(matchAllData) > 0 && matchAllData.getMatch().getInning().get(1).getTotalWickets() >= 10 ) {
				
				if(infobar.isResult_on_screen() == false) {
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Title*GEOM*TEXT SET " + 
							CricketFunctions.generateMatchSummaryStatus(2, matchAllData, CricketUtil.FULL, "|", config.getBroadcaster()).toUpperCase().split(" WIN ")[0] + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Subtitle*GEOM*TEXT SET " + 
							"WIN " + CricketFunctions.generateMatchSummaryStatus(2, matchAllData, CricketUtil.FULL, "|", config.getBroadcaster()).toUpperCase().split(" WIN ")[1] + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Left$In$img_Badge*TEXTURE*IMAGE SET " + 
							Constants.BENGAL_ICONS_PATH + matchAllData.getSetup().getHomeTeam().getTeamName4() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Right$In$img_Badge*TEXTURE*IMAGE SET " + 
							Constants.BENGAL_ICONS_PATH + matchAllData.getSetup().getAwayTeam().getTeamName4() + "\0", print_writers);

//					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Title*GEOM*TEXT SET " + 
//							matchAllData.getMatch().getInning().get(1).getBowling_team().getTeamName1().toUpperCase() + "\0", print_writers);
//					
//					if(matchAllData.getSetup().getTargetType() != null) {
//						if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.DLS)) {
//							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Subtitle*GEOM*TEXT SET " + 
//									"WIN BY " + Integer.valueOf(CricketFunctions.getRequiredRuns(matchAllData) - 1) + " RUN" + 
//									CricketFunctions.Plural(Integer.valueOf(CricketFunctions.getRequiredRuns(matchAllData) - 1)).toUpperCase() + " (DLS)" + "\0", print_writers);
//						}else if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.VJD)) {
//							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Subtitle*GEOM*TEXT SET " + 
//									"WIN BY " + Integer.valueOf(CricketFunctions.getRequiredRuns(matchAllData) - 1) + " RUN" + 
//									CricketFunctions.Plural(Integer.valueOf(CricketFunctions.getRequiredRuns(matchAllData) - 1)).toUpperCase() + " (VJD)" + "\0", print_writers);
//						}
//				    }else {
//				    	CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Result$Middle$txt_Subtitle*GEOM*TEXT SET " + 
//								"WIN BY " + Integer.valueOf(CricketFunctions.getRequiredRuns(matchAllData) - 1) + " RUN" + 
//								CricketFunctions.Plural(Integer.valueOf(CricketFunctions.getRequiredRuns(matchAllData) - 1)).toUpperCase() + "\0", print_writers);
//				    }
					
					TimeUnit.MILLISECONDS.sleep(2000);
					
					this_animation.processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Infobar_To_Result", "START");
					this_animation.processAnimation(Constants.FRONT, print_writers, "Loop", "START");
					
					infobar.setResult_on_screen(true);
				}
				
			}else {
				populateInfobarTeamNameScore(true,print_writers,matchAllData,2);
				
				if(infobar.getLeft_bottom() != null && !infobar.getLeft_bottom().isEmpty()) {
					populateVizInfobarLeftBottom(print_writers, matchAllData, 1);
				}
				
				if(!infobar.getMiddle_section().equalsIgnoreCase(CricketUtil.BATSMAN)) {
					populateVizInfobarMiddleSection(print_writers, matchAllData, 1);
				}else {
					populateCurrentBatsmen(print_writers, matchAllData, 1);
				}
				
				if(infobar.getRight_section() != null && !infobar.getRight_section().isEmpty()) {
					System.out.println("Right = " + infobar.getRight_section());
					System.out.println("Bottom = " + infobar.getRight_bottom());
					if(infobar.getRight_section().equalsIgnoreCase(CricketUtil.BOWLER)) {
						populateVizInfobarBowler(print_writers, matchAllData, 2);
						populateVizInfobarBowler(print_writers, matchAllData, 1);
						populateVizInfobarRightBottom(print_writers, matchAllData, 1, 1);
					}else {
						populateVizInfobarRightSection(true,print_writers, matchAllData, 1, 1);
					}
				}
			}
			break;	
		}
		return Constants.OK;
	}
	public String populateTapeBall(List<PrintWriter> print_writers,MatchAllData matchAllData) {
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
		if(inning == null) {
			return "populateInfobarTeamNameScore: Inning return is NULL";
		}
//		CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Taped_Ball$Loop$img_Base1*TEXTURE*IMAGE SET " + 
//				Constants.BASE_PATH + "1/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
//		CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Taped_Ball$FrontText$img_txt2*TEXTURE*IMAGE SET " + 
//				Constants.TEXT_PATH + "2/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
//		CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Taped_Ball$Tapped_Ball_all_Grp$img_Base2*TEXTURE*IMAGE SET " + 
//				Constants.BASE_PATH + "2/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
		
		CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Taped_Ball$Loop$img_txt2*GEOM*TEXT SET " + 
				"TAPE BALL OVER" + "\0", print_writers);
		CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Taped_Ball$FrontText$img_txt2*GEOM*TEXT SET " + 
				"TAPE BALL OVER" + "\0", print_writers);
		return Constants.OK;
	}
	public String populateTarget(List<PrintWriter> print_writers,MatchAllData matchAllData) {
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
		if(inning == null) {
			return "populateInfobarTeamNameScore: Inning return is NULL";
		}
		
		CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Target$Tapped_Text_All$txt_Target*GEOM*TEXT SET " + 
				"TARGET" + "\0", print_writers);
		
//		if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.DLS)) {
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Target$Tapped_Text_All$fig_Target*GEOM*TEXT SET " + 
//					CricketFunctions.getTargetRuns(matchAllData) + " (" + CricketUtil.DLS + ")" + "\0", print_writers);
//		}
//		else if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.VJD)) {
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Target$Tapped_Text_All$fig_Target*GEOM*TEXT SET " + 
//					CricketFunctions.getTargetRuns(matchAllData) + " (" + CricketUtil.VJD + ")" + "\0", print_writers);
//		}
//		else {
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Target$Tapped_Text_All$fig_Target*GEOM*TEXT SET " + 
//					CricketFunctions.getTargetRuns(matchAllData) + "\0", print_writers);
//		}
		CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Target$Tapped_Text_All$fig_Target*GEOM*TEXT SET " + 
				CricketFunctions.getTargetRuns(matchAllData) + "\0", print_writers);
		return Constants.OK;
	}
	public String populateInfobar(List<PrintWriter> print_writers,String whatToProcess, MatchAllData matchAllData) throws InterruptedException, CloneNotSupportedException, JsonMappingException, JsonProcessingException {
		
		switch (config.getBroadcaster()) {
		case Constants.ISPL:
			infobar.setLast_full_section(null);
			status = populateInfobarTeamNameScore(false,print_writers,matchAllData,1);
			if(status == Constants.OK) {
				status = populateVizInfobarLeftBottom(print_writers, matchAllData, 1);
				if(status == Constants.OK) {
					this.infobar.setMiddle_section(whatToProcess.split(",")[2]);
					if(infobar.getMiddle_section().equalsIgnoreCase(CricketUtil.BATSMAN)) {
						this.infobar.setRight_section(CricketUtil.BOWLER);
						this.infobar.setRight_bottom(CricketUtil.BOWLER);
					}
					populateVizInfobarMiddleSection(print_writers, matchAllData, 1);
				} else {
					return status;
				}
			} else {
				return status;
			}
			break;
		case Constants.ICC_U19_2023: 
			
			this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar", "SHOW 0.0");
			infobar.setPowerplay_on_screen(false);
			infobar.setPowerplay_end(false);
			
			status = populateInfobarTeamNameScore(false,print_writers,matchAllData,1);
			if(status == Constants.OK) {
				this.infobar.setLeft_bottom(whatToProcess.split(",")[3]);
				status = populateVizInfobarLeftBottom(print_writers,matchAllData,1);
				if(status == Constants.OK) {
					this.infobar.setMiddle_section(whatToProcess.split(",")[2]);
					
					if(infobar.getMiddle_section().equalsIgnoreCase(CricketUtil.BATSMAN)) {
						this.infobar.setRight_section(CricketUtil.BOWLER);
						this.infobar.setRight_bottom("BOWLING_END");
					}
					populateVizInfobarMiddleSection(print_writers, matchAllData, 1);
				} else {
					return status;
				}
			} else {
				return status;
			}
			break;
		case Constants.BENGAL_T20: 
			
			infobar.setPowerplay_on_screen(false);
			infobar.setPowerplay_end(false);
			
			status = populateInfobarTeamNameScore(false,print_writers,matchAllData,1);
			if(status == Constants.OK) {
				this.infobar.setLeft_bottom(whatToProcess.split(",")[3]);
				status = populateVizInfobarLeftBottom(print_writers,matchAllData,1);
				if(status == Constants.OK) {
					this.infobar.setMiddle_section(whatToProcess.split(",")[2]);
					
					if(infobar.getMiddle_section().equalsIgnoreCase(CricketUtil.BATSMAN)) {
						this.infobar.setRight_section(CricketUtil.BOWLER);
						this.infobar.setRight_bottom("BOWLING_END");
					}
					populateVizInfobarMiddleSection(print_writers, matchAllData, 1);
				} else {
					return status;
				}
			} else {
				return status;
			}
			break;	
			
		}
		return Constants.OK;
	}
	public String populateChallengedSection(boolean is_this_updating, List<PrintWriter> print_writers,MatchAllData matchAllData, int whichSide) {
		switch(config.getBroadcaster()) {
		case Constants.ISPL:
			if(is_this_updating == false) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$txt_CR*GEOM*TEXT SET " + 
						"CHALLENGE RUNS "+ challengedRuns + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side" + whichSide + "$Select*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				infobar.setChallengeRunOnScreen(true);
				infobar.setTop_stage(false);
				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TopStage_Change", "SHOW 0.0");
			}
			int BonusRuns = 0;
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			if(inning == null) {
				return "populateInfobarTeamNameScore: Inning return is NULL";
			}
			
			this_data_str = new ArrayList<String>();
			this_data_str.add(CricketFunctions.getEventsText(CricketUtil.OVER,infobar.getLast_bowler().getPlayerId() ,
					",", matchAllData.getEventFile().getEvents(),0));
			
			if(this_data_str.get(this_data_str.size()-1) == null || this_data_str.get(this_data_str.size()-1).split(",").length > 24) {
				return "populateVizInfobarRightBottom: This over data returned invalid";
			}
			
			if(this_data_str.get(this_data_str.size()-1).length()>0) {
				for(int i=0; i<this_data_str.get(this_data_str.size()-1).split(",").length; i++) {
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Challenge_Info$txt_ChallengeRuns$noname"
							+ "*GEOM*TEXT SET " + CricketFunctions.processThisOverRunsCount(infobar.getLast_bowler().getPlayerId(), 
									matchAllData.getEventFile().getEvents()).split("-")[0] + "\0", print_writers);
//					System.out.println("Tis over runs = " + this_data_str);
					if(!this_data_str.get(this_data_str.size()-1).split(",")[i].contains("w") && 
							!this_data_str.get(this_data_str.size()-1).split(",")[i].contains("nb") && 
							!this_data_str.get(this_data_str.size()-1).split(",")[i].contains("lb") && 
							!this_data_str.get(this_data_str.size()-1).split(",")[i].contains("b")) {
						BonusRuns = BonusRuns +  Integer.valueOf(this_data_str.get(this_data_str.size()-1).split(",")[i]);
					}
					else {
						if(this_data_str.get(this_data_str.size()-1).split(",")[i].contains("nb") &&
								this_data_str.get(this_data_str.size()-1).split(",")[i].contains("w")) {
							if(this_data_str.get(this_data_str.size()-1).split(",")[i].contains(" ")) {
								BonusRuns = BonusRuns +  (1 + Integer.valueOf(this_data_str.get(this_data_str.size()-1).split(",")[i].replace("nb", "").replace("w", "").trim()));
							}else {
								BonusRuns = BonusRuns +  1;
							}
						}else if(this_data_str.get(this_data_str.size()-1).split(",")[i].contains("nb")) {
							if(this_data_str.get(this_data_str.size()-1).split(",")[i].contains(" ")) {
								BonusRuns = BonusRuns +  (1 + Integer.valueOf(this_data_str.get(this_data_str.size()-1).split(",")[i].replace("nb", "").trim()));
//								System.out.println("data = " + (1 + Integer.valueOf(this_data_str.get(this_data_str.size()-1).split(",")[i].replace("nb", "").trim())));
							}else {
								BonusRuns = BonusRuns +  1;
							}
						}else if(this_data_str.get(this_data_str.size()-1).split(",")[i].contains("wd")) {
							if(this_data_str.get(this_data_str.size()-1).split(",")[i].contains(" ")) {
								BonusRuns = BonusRuns +  (1 + Integer.valueOf(this_data_str.get(this_data_str.size()-1).split(",")[i].replace("wd", "").trim()));
							}else {
								BonusRuns = BonusRuns +  1;
							}
						}else if(this_data_str.get(this_data_str.size()-1).split(",")[i].contains("w")) {
							if(this_data_str.get(this_data_str.size()-1).split(",")[i].contains(" ")) {
								BonusRuns = BonusRuns +  (Integer.valueOf(this_data_str.get(this_data_str.size()-1).split(",")[i].replace("w", "").trim()));
							}
						}else if(this_data_str.get(this_data_str.size()-1).split(",")[i].contains("lb")) {
							if(!this_data_str.get(this_data_str.size()-1).split(",")[i].replace("lb", "").trim().isEmpty()) {
								BonusRuns = BonusRuns +  (1 + Integer.valueOf(this_data_str.get(this_data_str.size()-1).split(",")[i].replace("lb", "").trim()));
//								System.out.println("data = " + (1 + Integer.valueOf(this_data_str.get(this_data_str.size()-1).split(",")[i].replace("lb", "").trim())));
							}else {
								BonusRuns = BonusRuns +  1;
							}
						}else if(this_data_str.get(this_data_str.size()-1).split(",")[i].contains("b")) {
							if(!this_data_str.get(this_data_str.size()-1).split(",")[i].replace("b", "").trim().isEmpty()) {
								BonusRuns = BonusRuns +  (1 + Integer.valueOf(this_data_str.get(this_data_str.size()-1).split(",")[i].replace("b", "").trim()));
							}else {
								BonusRuns = BonusRuns +  1;
							}
						}
					}
				}
				
				for(int iBall = 0; iBall < this_data_str.get(this_data_str.size()-1).split(",").length; iBall++) {
					
					if(this_data_str.get(this_data_str.size()-1).split(",").length >= 13) {
						if(this_data_str.get(this_data_str.size()-1).split(",").length >= 13) {
							if(infobar.isTop_stage() == false) {
								this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TopStage_Change", "START");
								infobar.setTop_stage(true);
							}
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
								+ "Balls*FUNCTION*Omo*vis_con SET " + (iBall+1) + " \0", print_writers);
						switch (this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase()) {
						case CricketUtil.DOT:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
							break;
						case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FIVE: 
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type$Run$txt_Number*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).
									split(",")[iBall] + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							break;
						case CricketUtil.FOUR:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type$Four$txt_4*GEOM*TEXT SET " + CricketUtil.FOUR + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
							break;
						case CricketUtil.SIX:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type$Six$txt_6*GEOM*TEXT SET " + CricketUtil.SIX + "\0", print_writers);

							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
							break;
						case CricketUtil.NINE:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type$Six$txt_6*GEOM*TEXT SET " + CricketUtil.NINE + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
							break;
						case "W":
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type$Wicket$txt_W*GEOM*TEXT SET " + "W" + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
							break;

						default:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type$Extra$txt_Extra*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).
									split(",")[iBall] + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 5 \0", print_writers);
							
							break;
						}
						
						
						//----------------------------------------------------------//
						
						
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side2$Bounus_Runs_Grp$"
								+ "Balls*FUNCTION*Omo*vis_con SET " + ((iBall+1)-12) + " \0", print_writers);
						
						switch (this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase()) {
						case CricketUtil.DOT:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side2$Bounus_Runs_Grp$"
									+ "Balls$" + ((iBall+1)-12) + "$Choose_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
							break;
						case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FIVE: 
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side2$Bounus_Runs_Grp$"
									+ "Balls$" + ((iBall+1)-12) + "$Choose_Type$Run$txt_Number*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).
									split(",")[iBall] + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side2$Bounus_Runs_Grp$"
									+ "Balls$" + ((iBall+1)-12) + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							break;
						case CricketUtil.FOUR:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side2$Bounus_Runs_Grp$"
									+ "Balls$" + ((iBall+1)-12) + "$Choose_Type$Four$txt_4*GEOM*TEXT SET " + CricketUtil.FOUR + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side2$Bounus_Runs_Grp$"
									+ "Balls$" + ((iBall+1)-12)+ "$Choose_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
							break;
						case CricketUtil.SIX:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side2$Bounus_Runs_Grp$"
									+ "Balls$" + ((iBall+1)-12) + "$Choose_Type$Six$txt_6*GEOM*TEXT SET " + CricketUtil.SIX + "\0", print_writers);

							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side2$Bounus_Runs_Grp$"
									+ "Balls$" + ((iBall+1)-12) + "$Choose_Type*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
							break;
						case CricketUtil.NINE:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side2$Bounus_Runs_Grp$"
									+ "Balls$" + ((iBall+1)-12) + "$Choose_Type$Six$txt_6*GEOM*TEXT SET " + CricketUtil.NINE + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side2$Bounus_Runs_Grp$"
									+ "Balls$" + ((iBall+1)-12) + "$Choose_Type*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
							break;
						case "W":
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side2$Bounus_Runs_Grp$"
									+ "Balls$" + ((iBall+1)-12) + "$Choose_Type$Wicket$txt_W*GEOM*TEXT SET " + "W" + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side2$Bounus_Runs_Grp$"
									+ "Balls$" + ((iBall+1)-12) + "$Choose_Type*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
							break;

						default:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side2$Bounus_Runs_Grp$"
									+ "Balls$" + ((iBall+1)-12) + "$Choose_Type$Extra$txt_Extra*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).
									split(",")[iBall] + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side2$Bounus_Runs_Grp$"
									+ "Balls$" + ((iBall+1)-12) + "$Choose_Type*FUNCTION*Omo*vis_con SET 5 \0", print_writers);
							
							break;
						}
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side2$Bounus_Runs_Grp$"
								+ "Balls*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TopStage_Change", "SHOW 0.0");
						infobar.setTop_stage(false);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
								+ "Balls*FUNCTION*Omo*vis_con SET " + (iBall+1) + " \0", print_writers);
						switch (this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase()) {
						case CricketUtil.DOT:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
							break;
						case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FIVE: 
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type$Run$txt_Number*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).
									split(",")[iBall] + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							break;
						case CricketUtil.FOUR:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type$Four$txt_4*GEOM*TEXT SET " + CricketUtil.FOUR + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
							break;
						case CricketUtil.SIX:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type$Six$txt_6*GEOM*TEXT SET " + CricketUtil.SIX + "\0", print_writers);

							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
							break;
						case CricketUtil.NINE:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type$Six$txt_6*GEOM*TEXT SET " + CricketUtil.NINE + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
							break;
						case "W":
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type$Wicket$txt_W*GEOM*TEXT SET " + "W" + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
							break;

						default:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type$Extra$txt_Extra*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).
									split(",")[iBall] + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side1$Bounus_Runs_Grp$"
									+ "Balls$" + (iBall+1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 5 \0", print_writers);
							
							break;
						}
					}
				}
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Challenge_Info$txt_ChallengeRuns$noname"
						+ "*GEOM*TEXT SET " + "0" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side" + whichSide + "$Bounus_Runs_Grp$"
						+ "Balls*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
			}
			
			
			if(BonusRuns >= challengedRuns) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side" + whichSide + "$Bonus_Grp$Select*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side" + whichSide + "$Bonus_Grp$Select$base*TEXTURE*IMAGE SET "+ Constants.BASE_PATH +"2/ISPL" +" \0", print_writers);

			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side" + whichSide + "$Bonus_Grp$Select*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side" + whichSide + "$Bonus_Grp$Select$base*TEXTURE*IMAGE SET "+ Constants.BASE_PATH +"1/RED" +" \0", print_writers);
			}
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TopStage$Side" + whichSide + "$Bounus_Runs_Grp$txt_BonusValue*GEOM*TEXT SET "+"("+(BonusRuns/2) +")"+" \0", print_writers);
			
			infobar.setLast_this_over(this_data_str.get(this_data_str.size()-1));
			break;
		}
		return Constants.OK;
	}
	
	public String populateInfobarTeamNameScore(boolean is_this_updating,List<PrintWriter> print_writers,MatchAllData matchAllData, int whichSide) throws InterruptedException {
		
		switch(config.getBroadcaster()) {
		case Constants.ISPL:
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			if(inning == null) {
				return "populateInfobarTeamNameScore: Inning return is NULL";
			}
			if(is_this_updating == false) {
				
				if(inning.getBatting_team().getTeamName4().contains("KHILADI XI") || inning.getBatting_team().getTeamName4().contains("MASTER 11")) {
					if(inning.getBatting_team().getTeamName4().equalsIgnoreCase("KHILADI XI")) {
						color = "KHILADI_XI";
					}else if(inning.getBatting_team().getTeamName4().equalsIgnoreCase("MASTER 11")) {
						color = "MASTER_XI";
					}
				}else {
					color = inning.getBatting_team().getTeamName4();
				}
				
				if(inning.getBowling_team().getTeamName4().contains("KHILADI XI") || inning.getBowling_team().getTeamName4().contains("MASTER 11")) {
					if(inning.getBowling_team().getTeamName4().equalsIgnoreCase("KHILADI XI")) {
						color2 = "KHILADI_XI";
					}else if(inning.getBowling_team().getTeamName4().equalsIgnoreCase("MASTER 11")) {
						color2 = "MASTER_XI";
					}
				}else {
					color2 = inning.getBowling_team().getTeamName4();
				}
				
				//-----------------------------SUPER OVER---------------------------------//
				
				if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
				
					}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$BattingTeamGrp$Score_More$BattingTeamBaseGrp$img1*TEXTURE*IMAGE SET " + 
							Constants.BASE_PATH + "1/" + color + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$BattingTeamGrp$Score_More$img_txt1*TEXTURE*IMAGE SET " + 
							Constants.TEXT_PATH + "1/" + color + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$BolwerBaseGrp$img1*TEXTURE*IMAGE SET " + 
							Constants.BASE_PATH + "1/" + color2 + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$BowlerGrp_All$Side1$img_txt1*TEXTURE*IMAGE SET " + 
							Constants.TEXT_PATH + "1/" + color2 + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$BowlerGrp_All$Side2$img_txt1*TEXTURE*IMAGE SET " + 
							Constants.TEXT_PATH + "1/" + color2 + "\0", print_writers);
				}
				
				//--------------------------------------------------------------//
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Mains$HomeSideLogo$HomeTeamAll$BaseGrp$img*TEXTURE*IMAGE SET " + 
						Constants.BASE_PATH + "2/" + color + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Mains$HomeSideLogo$HomeTeamAll$Elements$img*TEXTURE*IMAGE SET " + 
						Constants.BASE_PATH + "1/" + color + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Mains$AwaySideLogo$AwayTeamAll$BaseGrp$img*TEXTURE*IMAGE SET " + 
						Constants.BASE_PATH + "2/" + color2 + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Mains$AwaySideLogo$AwayTeamAll$Elements$img*TEXTURE*IMAGE SET " + 
						Constants.BASE_PATH + "1/" + color2 + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Mains$HomeSideLogo$HomeTeamAll$HomeTeamLogo$img*TEXTURE*IMAGE SET " + 
						Constants.ISPL_LOGOS_PATH + color + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Mains$HomeSideLogo$HomeTeamAll$BaseGrp$HomeTeamLogo$img*TEXTURE*IMAGE SET " + 
						Constants.ISPL_LOGOS_BW_PATH + color + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Mains$AwaySideLogo$AwayTeamAll$AwayTeamLogo$img*TEXTURE*IMAGE SET " + 
						Constants.ISPL_LOGOS_PATH + color2 + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Mains$AwaySideLogo$AwayTeamAll$BaseGrp$AwayTeamLogo$img*TEXTURE*IMAGE SET " + 
						Constants.ISPL_LOGOS_BW_PATH + color2 + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$BattingTeamGrp$txt_TeamName*GEOM*TEXT SET " + 
						inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
			}
			
			if(matchAllData.getEventFile().getEvents().get(matchAllData.getEventFile().getEvents().size()-1).getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50)) {
				int bonus = 0;
				int challengeRuns = 0;
				challengeRuns = matchAllData.getEventFile().getEvents().get(matchAllData.getEventFile().getEvents().size()-1).getEventRuns();
				bonus = matchAllData.getEventFile().getEvents().get(matchAllData.getEventFile().getEvents().size()-1).getEventExtraRuns();
				playChallengeWipe(print_writers, bonus, challengeRuns);
			}
			
			if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
				if(infobar.isPowerplay_on_screen() == false) {
					infobar.setPowerplay_on_screen(true);
		         }
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$BattingTeamGrp$PowerPlay$txt_PP*GEOM*TEXT SET " + 
						"SUPER OVER" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Anim_InfoBar$Main$PowerPlay_In SHOW 0.700 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$BattingTeamGrp$txt_Overs*GEOM*TEXT SET " + 
						((inning.getTotalOvers()* 6) +  inning.getTotalBalls()) + "\0", print_writers);
				
				if(inning.getTotalOvers() > 0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$BattingTeamGrp$txt_DLS*GEOM*TEXT SET " + 
							"BALLS" + "\0", print_writers);
				}else if(inning.getTotalOvers() == 0) {
					if(inning.getTotalBalls() == 0 || inning.getTotalBalls() == 1) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$BattingTeamGrp$txt_DLS*GEOM*TEXT SET " + 
								"BALL" + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$BattingTeamGrp$txt_DLS*GEOM*TEXT SET " + 
								"BALLS" + "\0", print_writers);
					}
				}
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$BattingTeamGrp$PowerPlay$txt_PP*GEOM*TEXT SET " + 
						"POWERPLAY" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$BattingTeamGrp$Score_More$Score_Grp$PowerPlay$BattingTeamBase*TEXTURE*IMAGE SET " + 
						Constants.BASE_PATH + "1/" + "RED" + "\0", print_writers);
				if(((inning.getTotalOvers() * 6) + inning.getTotalBalls()) >= ((inning.getFirstPowerplayStartOver() - 1) * 6) && 
						((inning.getTotalOvers() * 6) + inning.getTotalBalls()) < (inning.getFirstPowerplayEndOver()* 6)) {
					
					if(infobar.isPowerplay_on_screen() == false) {
						 CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Anim_InfoBar$Main$PowerPlay_In START \0", print_writers);
			        	 infobar.setPowerplay_on_screen(true);
			        }
			    }
				else if(((inning.getTotalOvers() * 6) + inning.getTotalBalls()) >= ((inning.getSecondPowerplayStartOver() - 1) * 6) && 
						((inning.getTotalOvers() * 6) + inning.getTotalBalls()) < (inning.getSecondPowerplayEndOver()* 6)) {
					if(infobar.isPowerplay_on_screen() == false) {
						 CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Anim_InfoBar$Main$PowerPlay_In START \0", print_writers);
			        	 infobar.setPowerplay_on_screen(true);
			         }
			    }else {
			    	if(infobar.isPowerplay_on_screen() == true) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Anim_InfoBar$Main$PowerPlay_In CONTINUE REVERSE \0", print_writers);
						infobar.setPowerplay_on_screen(false);
			    	}
			    }
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$BattingTeamGrp$txt_Overs*GEOM*TEXT SET " + 
						CricketFunctions.OverBalls(inning.getTotalOvers(),inning.getTotalBalls()) + "\0", print_writers);
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$BattingTeamGrp$txt_Runs*GEOM*TEXT SET " + 
					CricketFunctions.getTeamScore(inning, slashOrDash, false) + "\0", print_writers);
			
			break;
		case Constants.ICC_U19_2023: 

			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			if(inning == null) {
				return "populateInfobarTeamNameScore: Inning return is NULL";
			}
			
			if(is_this_updating == false) {
				
				if(matchAllData.getSetup().getHomeTeam().getTeamName4().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Flag_Left$img_Shadow*ACTIVE SET 0 \0",print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Flag_Left$img_Shadow*ACTIVE SET 1 \0",print_writers);
				}
				
				if(matchAllData.getSetup().getAwayTeam().getTeamName4().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Flag_Right$img_Shadow*ACTIVE SET 0 \0",print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Flag_Right$img_Shadow*ACTIVE SET 1 \0",print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Wipes$Select*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Flag_Left$img_Flag*TEXTURE*IMAGE SET " + 
						Constants.ICC_U19_2023_FLAG_PATH + matchAllData.getSetup().getHomeTeam().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Flag_Right$img_Flag*TEXTURE*IMAGE SET " + 
						Constants.ICC_U19_2023_FLAG_PATH + matchAllData.getSetup().getAwayTeam().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Top$txt_Team_1*GEOM*TEXT SET " + 
						inning.getBowling_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Top$txt_v*GEOM*TEXT SET v \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Top$Scorebox$txt_Team_2*GEOM*TEXT SET " + 
						inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
			}
			
			if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
				if(infobar.isPowerplay_on_screen() == false) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Top$Powerplay$Select*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Powerplay START \0", print_writers);
					infobar.setPowerplay_on_screen(true);
		         }
			}else {
				if(!CricketFunctions.processPowerPlay(CricketUtil.MINI,matchAllData).isEmpty()) {
					if(CricketFunctions.processPowerPlayAnimation(matchAllData, inning.getInningNumber()) != null) {
						if(CricketFunctions.processPowerPlayAnimation(matchAllData, inning.getInningNumber()).equalsIgnoreCase(CricketUtil.NO) && 
								infobar.isPowerplay_end() == false) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Powerplay CONTINUE REVERSE \0", print_writers);
							infobar.setPowerplay_end(true);
						}
						else if(CricketFunctions.processPowerPlayAnimation(matchAllData, inning.getInningNumber()).equalsIgnoreCase(CricketUtil.YES)&& 
								infobar.isPowerplay_end() == true) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Powerplay START \0", print_writers);
							infobar.setPowerplay_end(false);
						}
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Top$Powerplay$Select*FUNCTION*Omo*vis_con SET 0 \0",
							print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Top$Powerplay$PP$txt_Powerplay*GEOM*TEXT SET " + 
							CricketFunctions.processPowerPlay(CricketUtil.MINI,matchAllData) + "\0", print_writers);
					
					 if(infobar.isPowerplay_on_screen() == false) {
			        	 CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Powerplay START \0", print_writers);
			        	 infobar.setPowerplay_on_screen(true);
			         }
				}
		    	else {
					if(infobar.isPowerplay_on_screen() == true) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Powerplay SHOW 0.0 \0", print_writers);
						infobar.setPowerplay_on_screen(false);
			         }
				}
			}
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Scorebox$txt_Score*GEOM*TEXT SET " + 
					CricketFunctions.getTeamScore(inning, slashOrDash, false) + "\0", print_writers);
			
			if((inning.getTotalOvers()* Integer.valueOf(matchAllData.getSetup().getBallsPerOver())+inning.getTotalBalls() <= 59)){
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Overs$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				containerName = "Overs_with_Reduced";
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Overs$Choose_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				containerName = "Overs_Only";
			}
			
			if(matchAllData.getSetup().getTargetOvers() != "") {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$" + containerName + "$txt_Reduced*GEOM*TEXT SET " + 
						matchAllData.getSetup().getTargetOvers() + "\0", print_writers);
			}else {
				if(inning.getTotalOvers() == 0 || inning.getTotalOvers() > 1) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$" + containerName + "$txt_Reduced*GEOM*TEXT SET " + 
							"OVER" + CricketFunctions.Plural(inning.getTotalOvers()).toUpperCase() + "\0", print_writers);
				}else {
					if(inning.getTotalBalls() == 1) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$" + containerName + "$txt_Reduced*GEOM*TEXT SET " + 
								"OVERS" + "\0", print_writers);
					}
					else if(inning.getTotalBalls() > 0) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$" + containerName + "$txt_Reduced*GEOM*TEXT SET " + 
								"OVER" + CricketFunctions.Plural(inning.getTotalBalls()).toUpperCase() + "\0", print_writers);
					}
					else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$" + containerName + "$txt_Reduced*GEOM*TEXT SET " + 
								"OVER" + "\0", print_writers);
					}
				}
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$" + containerName + "$txt_Overs*GEOM*TEXT SET " + 
				CricketFunctions.OverBalls(inning.getTotalOvers(),inning.getTotalBalls()) + "\0", print_writers);
			
			break;
			
		case Constants.BENGAL_T20: 

			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			if(inning == null) {
				return "populateInfobarTeamNameScore: Inning return is NULL";
			}
			
			if(is_this_updating == false) {
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$TeamGrp1$Move_To_Normal$Move_To_Result$"
						+ "img_Badge*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_ICONS_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Move_To_Normal$"
						+ "Move_To_Result$img_Badge*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_ICONS_PATH + inning.getBowling_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$TeamGrp1$In$BaseGrp$"
						+ "Mask1$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$TeamGrp1$In$BaseGrp$"
						+ "Mask2$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$TeamGrp1$In$TeamNameGrp$Move_For_Shrink$"
						+ "img_Text1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$TeamGrp1$In$TeamNameGrp$Runs_For_Shrink$"
						+ "img_Text1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$"
						+ "TeamNameGrp$img_Text1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_TEXT_PATH + "1/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$In$TeamNameGrp$txt_TeamName*GEOM*TEXT SET " + 
						inning.getBowling_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$TeamGrp1$In$TeamNameGrp$Move_For_Shrink$txt_TeamName*GEOM*TEXT SET " + 
						inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
			}
			
			if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
				if(infobar.isPowerplay_on_screen() == false) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Top$Powerplay$Select*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Powerplay START \0", print_writers);
					infobar.setPowerplay_on_screen(true);
		         }
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$MainScoreGrp$In$BottomDataGrp$Alignment$txt_Overs*GEOM*TEXT SET " + 
						(6 - ((inning.getTotalOvers()* 6) +  inning.getTotalBalls())) + " BALL" + CricketFunctions.Plural((6 - ((inning.getTotalOvers()* 6) +  inning.getTotalBalls()))).toUpperCase() + " LEFT" + "\0", print_writers);
				
			}else {
				if(!CricketFunctions.processPowerPlay(CricketUtil.MINI,matchAllData).isEmpty()) {
					if(CricketFunctions.processPowerPlayAnimation(matchAllData, inning.getInningNumber()) != null) {
						if(CricketFunctions.processPowerPlayAnimation(matchAllData, inning.getInningNumber()).equalsIgnoreCase(CricketUtil.NO) && 
								infobar.isPowerplay_end() == false) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$MainScoreGrp$In$BottomDataGrp$"
									+ "PowerplayGrp*ACTIVE SET 0 \0", print_writers);
							infobar.setPowerplay_end(true);
						}
						else if(CricketFunctions.processPowerPlayAnimation(matchAllData, inning.getInningNumber()).equalsIgnoreCase(CricketUtil.YES)&& 
								infobar.isPowerplay_end() == true) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$MainScoreGrp$In$BottomDataGrp$"
									+ "PowerplayGrp*ACTIVE SET 1 \0", print_writers);
							infobar.setPowerplay_end(false);
						}
					}
					
//					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Top$Powerplay$Select*FUNCTION*Omo*vis_con SET 0 \0",
//							print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$MainScoreGrp$In$BottomDataGrp$"
							+ "PowerplayGrp$P*GEOM*TEXT SET " + "P" + " \0", print_writers);
					
//					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Top$Powerplay$PP$txt_Powerplay*GEOM*TEXT SET " + 
//							CricketFunctions.processPowerPlay(CricketUtil.MINI,matchAllData) + "\0", print_writers);
					
					 if(infobar.isPowerplay_on_screen() == false) {
						 CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$MainScoreGrp$In$BottomDataGrp$"
									+ "PowerplayGrp*ACTIVE SET 1 \0", print_writers);
			        	 infobar.setPowerplay_on_screen(true);
			         }
				}
		    	else {
					if(infobar.isPowerplay_on_screen() == true) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$MainScoreGrp$In$BottomDataGrp$"
								+ "PowerplayGrp*ACTIVE SET 0 \0", print_writers);
						infobar.setPowerplay_on_screen(false);
			         }
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$MainScoreGrp$In$BottomDataGrp$Alignment$txt_Overs*GEOM*TEXT SET " + 
						CricketFunctions.OverBalls(inning.getTotalOvers(),inning.getTotalBalls()) + "\0", print_writers);
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$MainScoreGrp$In$txt_Runs*GEOM*TEXT SET " + 
					CricketFunctions.getTeamScore(inning, slashOrDash, false) + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$TeamGrp1$In$Runs_For_Shrink$txt_Runs*GEOM*TEXT SET " + 
					CricketFunctions.getTeamScore(inning, slashOrDash, false) + "\0", print_writers);
			
			if(matchAllData.getSetup().getTargetOvers() != "") {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$MainScoreGrp$In$BottomDataGrp$Alignment$txt_Reduced_Overs*GEOM*TEXT SET " + 
						"(" + matchAllData.getSetup().getTargetOvers() + ")" + "\0", print_writers);
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$MainScoreGrp$In$BottomDataGrp$Alignment$txt_Reduced_Overs*GEOM*TEXT SET " + 
						"" + "\0", print_writers);
			}
			break;	
		}
		return Constants.OK;
	}
	public void populateTwoBatsmenSingleBatsman(List<PrintWriter> print_writers, MatchAllData matchAllData,
			int WhichSide, int WhichSubSide, int WhichBatsman, List<BattingCard> battingCardList) throws InterruptedException {
	
		switch(config.getBroadcaster()) {
		case Constants.ISPL:
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$BatsmanGrp$Bastman" + WhichBatsman + "_All$Side1$img1*TEXTURE*IMAGE SET " + 
//					Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$BatsmanGrp$Bastman" + WhichBatsman + "_All$Side2$img1*TEXTURE*IMAGE SET " + 
//					Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
			
			if(inning.getBatting_team().getTeamName4().contains("KHILADI XI") || inning.getBatting_team().getTeamName4().contains("MASTER 11")) {
				if(inning.getBatting_team().getTeamName4().equalsIgnoreCase("KHILADI XI")) {
					color = "KHILADI_XI";
				}else if(inning.getBatting_team().getTeamName4().equalsIgnoreCase("MASTER 11")) {
					color = "MASTER_XI";
				}
			}else {
				color = inning.getBatting_team().getTeamName4();
			}
			
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$OnStrikeGrp$img1*TEXTURE*IMAGE SET " + 
//					Constants.BASE_PATH + "1/" + color + " \0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$BatsmanGrp$Bastman" + WhichBatsman + "_All$Side" + WhichSide 
				 + "$txt_PlayerName*GEOM*TEXT SET " + battingCardList.get(WhichBatsman-1).getPlayer().getTicker_name() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$BatsmanGrp$Bastman" + WhichBatsman + "_All$Side" + WhichSide 
					 + "$txt_Runs*GEOM*TEXT SET " + battingCardList.get(WhichBatsman-1).getRuns() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$BatsmanGrp$Bastman" + WhichBatsman + "_All$Side" + WhichSide 
					 + "$txt_Balls*GEOM*TEXT SET " + battingCardList.get(WhichBatsman-1).getBalls() + "\0", print_writers);
			
			if((WhichBatsman == 1 && battingCardList.get(0).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) || 
					(WhichBatsman == 2 && battingCardList.get(1).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT))) {
				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Batsman" + WhichBatsman + "_Highlight", "SHOW 0.440");
			} else {
				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Batsman" + WhichBatsman + "_Highlight", "SHOW 0.0");
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$BatsmanGrp$OnStrikeGrp$Select*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
			}
			
			if((WhichBatsman == 1 && battingCardList.get(0).getOnStrike().equalsIgnoreCase(CricketUtil.YES))
					||(WhichBatsman == 2 && battingCardList.get(1).getOnStrike().equalsIgnoreCase(CricketUtil.YES))) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$BatsmanGrp$OnStrikeGrp$Select*FUNCTION*Omo*vis_con SET " + WhichBatsman + " \0", print_writers);
//				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$StrikeIn", "START");
			} else {
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$BatsmanGrp$OnStrikeGrp$Select*FUNCTION*Omo*vis_con SET " + WhichBatsman + " \0", print_writers);
				//this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$StrikeOut", "START");
			}
			break;
		case Constants.ICC_U19_2023: 
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ infobar.getBatsmanAndBowlOrSponsor() + "$Bat_" + WhichBatsman + "$Side" + WhichSubSide + "$Batsman*GEOM*TEXT SET " 
				+ battingCardList.get(WhichBatsman-1).getPlayer().getTicker_name() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ infobar.getBatsmanAndBowlOrSponsor() + "$Bat_" + WhichBatsman + "$Side" + WhichSubSide + "$txt_Runs*GEOM*TEXT SET " 
				+ battingCardList.get(WhichBatsman-1).getRuns() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ infobar.getBatsmanAndBowlOrSponsor() + "$Bat_" + WhichBatsman + "$Side" + WhichSubSide + "$txt_Balls*GEOM*TEXT SET " 
				+ battingCardList.get(WhichBatsman-1).getBalls() + "\0", print_writers);
			
			if((WhichBatsman == 1 && battingCardList.get(0).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) || 
					(WhichBatsman == 2 && battingCardList.get(1).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT))) {
				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bat_" + WhichBatsman + "_Lowlight", "SHOW 0.0");
			} else {
				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bat_" + WhichBatsman + "_Lowlight", "SHOW 0.4");
			}
			
			if((WhichBatsman == 1 && battingCardList.get(0).getOnStrike().equalsIgnoreCase(CricketUtil.YES))
					||(WhichBatsman == 2 && battingCardList.get(1).getOnStrike().equalsIgnoreCase(CricketUtil.YES))) {
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
					+ infobar.getBatsmanAndBowlOrSponsor() + "$Bat_" + WhichBatsman + "$Side" + WhichSubSide + "$obj_Indicator*ACTIVE SET 1 \0",print_writers);
			} else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
					+ infobar.getBatsmanAndBowlOrSponsor() + "$Bat_" + WhichBatsman + "$Side" + WhichSubSide + "$obj_Indicator*ACTIVE SET 0 \0",print_writers);
			}
			break;
		case Constants.BENGAL_T20: 
			
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$BatsmanGrp$BaseGrp$"
					+ "MaskLeft$MaskRight1$mg_Base1*TEXTURE*IMAGE SET " + 
					Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$BatsmanGrp$BaseGrp$"
					+ "MaskLeft$MaskRight2$mg_Base1*TEXTURE*IMAGE SET " + 
					Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$BatsmanGrp$Batsmen$"
					+ "img_Text1*TEXTURE*IMAGE SET " + Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
			
			if(CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), battingCardList.get(WhichBatsman-1).getPlayer().getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$BatsmanGrp$"
						+ "Batsmen$Batsman_" + WhichBatsman + "$Side_" + WhichSide + "$Select_Impact*FUNCTION*Omo*vis_con SET 1\0", print_writers);
			}else if(CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), battingCardList.get(WhichBatsman-1).getPlayer().getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$BatsmanGrp$"
						+ "Batsmen$Batsman_" + WhichBatsman + "$Side_" + WhichSide + "$Select_Impact*FUNCTION*Omo*vis_con SET 1\0", print_writers);
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$BatsmanGrp$"
						+ "Batsmen$Batsman_" + WhichBatsman + "$Side_" + WhichSide + "$Select_Impact*FUNCTION*Omo*vis_con SET 0\0", print_writers);
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$BatsmanGrp$"
					+ "Batsmen$Batsman_" + WhichBatsman + "$Side_" + WhichSide + "$txt_Name*GEOM*TEXT SET " 
				+ battingCardList.get(WhichBatsman-1).getPlayer().getTicker_name() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$BatsmanGrp$"
					+ "Batsmen$Batsman_" + WhichBatsman + "$Side_" + WhichSide + "$txt_Runs*GEOM*TEXT SET " 
				+ battingCardList.get(WhichBatsman-1).getRuns() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$BatsmanGrp$"
					+ "Batsmen$Batsman_" + WhichBatsman + "$Side_" + WhichSide + "$txt_Balls*GEOM*TEXT SET " 
				+ battingCardList.get(WhichBatsman-1).getBalls() + "\0", print_writers);
			
			if((WhichBatsman == 1 && battingCardList.get(0).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) || 
					(WhichBatsman == 2 && battingCardList.get(1).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT))) {
				this_animation.processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Batsmen_Lowlight$" + WhichBatsman, "SHOW 0.0");
			} else {
				this_animation.processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Batsmen_Lowlight$" + WhichBatsman, "SHOW 0.6");
			}
			
			if((WhichBatsman == 1 && battingCardList.get(0).getOnStrike().equalsIgnoreCase(CricketUtil.YES))
					||(WhichBatsman == 2 && battingCardList.get(1).getOnStrike().equalsIgnoreCase(CricketUtil.YES))) {
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$BatsmanGrp$"
						+ "Batsmen$Batsman_" + WhichBatsman + "$Side_" + WhichSubSide + "$On_Strike*ACTIVE SET 1 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$BatsmanGrp$"
						+ "Batsmen$Batsman_" + WhichBatsman + "$Side_" + WhichSubSide + "$On_Strike$StrikeIcon*ACTIVE SET 1 \0",print_writers);
			} else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$BatsmanGrp$"
						+ "Batsmen$Batsman_" + WhichBatsman + "$Side_" + WhichSubSide + "$On_Strike*ACTIVE SET 0 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$BatsmanGrp$"
						+ "Batsmen$Batsman_" + WhichBatsman + "$Side_" + WhichSubSide + "$On_Strike$StrikeIcon*ACTIVE SET 0 \0",print_writers);
			}
			break;	
		}
	}
	public String populateCurrentBatsmen(List<PrintWriter> print_writers, MatchAllData matchAllData,int WhichSide) 
			throws InterruptedException 
	{
		if(inning.getPartnerships() != null && inning.getPartnerships().size() <= 0) {
			return "populateCurrentBatsmen: Partnership array size is zero [" + inning.getPartnerships().size() + "]";
		}
		
		battingCardList = new ArrayList<BattingCard>();
		for(int iBat = 1; iBat <= 2; iBat++) {
			if(iBat == 1) {
				battingCardList.add(inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == 
					inning.getPartnerships().get(inning.getPartnerships().size() - 1).getFirstBatterNo()).findAny().orElse(null));
			} else {
				battingCardList.add(inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == 
					inning.getPartnerships().get(inning.getPartnerships().size() - 1).getSecondBatterNo()).findAny().orElse(null));
			}
			if(battingCardList.get(battingCardList.size()-1) == null) {
				return "populateCurrentBatsmen: One or more batsmen return are NULL";
			}
		}

		switch(config.getBroadcaster()) {
		case Constants.ISPL:
			if(infobar.getLast_batsmen() != null && infobar.getLast_batsmen().size() >= 2) {
				if(infobar.getLast_batsmen().get(0).getPlayerId() != battingCardList.get(0).getPlayerId()) {
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 2, 1, battingCardList);
					this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Batsman1_Change", "START");
					TimeUnit.MILLISECONDS.sleep(800);
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, 1, 1, 1, battingCardList);
					TimeUnit.MILLISECONDS.sleep(200);
					this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Batsman1_Change", "SHOW 0.0");
				} else {
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 1, 1, battingCardList);
				}
				if(infobar.getLast_batsmen().get(1).getPlayerId() != battingCardList.get(1).getPlayerId()) {
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 2, 2, battingCardList);
					this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Batsman2_Change", "START");
					TimeUnit.MILLISECONDS.sleep(800);
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, 1, 1, 2, battingCardList);
					TimeUnit.MILLISECONDS.sleep(200);
					this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Batsman2_Change", "SHOW 0.0");
				} else {
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 1, 2, battingCardList);
				}
			} else {
				populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 1, 1, battingCardList);
				populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 1, 2, battingCardList);
//				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Batsman1_In", "START");
//				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Batsman2_In", "START");
			}
			
			infobar.setLast_batsmen(battingCardList);
			break;
		case Constants.ICC_U19_2023: 
			if(infobar.getLast_batsmen() != null && infobar.getLast_batsmen().size() >= 2) {
				if(infobar.getLast_batsmen().get(0).getPlayerId() != battingCardList.get(0).getPlayerId()) {
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 2, 1, battingCardList);
					this_animation.processAnimation(Constants.FRONT, print_writers, "Bat_1_Change", "START");
					TimeUnit.MILLISECONDS.sleep(800);
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 1, 1, battingCardList);
					this_animation.processAnimation(Constants.FRONT, print_writers, "Bat_1_Change", "SHOW 0.0");
				} else {
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 1, 1, battingCardList);
				}
				if(infobar.getLast_batsmen().get(1).getPlayerId() != battingCardList.get(1).getPlayerId()) {
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 2, 2, battingCardList);
					this_animation.processAnimation(Constants.FRONT, print_writers, "Bat_2_Change", "START");
					TimeUnit.MILLISECONDS.sleep(800);
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 1, 2, battingCardList);
					this_animation.processAnimation(Constants.FRONT, print_writers, "Bat_2_Change", "SHOW 0.0");
				} else {
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 1, 2, battingCardList);
				}
			} else {
				populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 1, 1, battingCardList);
				populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 1, 2, battingCardList);
			}
			infobar.setLast_batsmen(battingCardList);
			break;
		case Constants.BENGAL_T20:
			if(infobar.getLast_batsmen() != null && infobar.getLast_batsmen().size() >= 2) {
				if(infobar.getLast_batsmen().get(0).getPlayerId() != battingCardList.get(0).getPlayerId()) {
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 2, 1, battingCardList);
					TimeUnit.MILLISECONDS.sleep(300);
					this_animation.processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Batsmen_Change$1", "START");
					TimeUnit.MILLISECONDS.sleep(800);
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 1, 1, battingCardList);
					TimeUnit.MILLISECONDS.sleep(300);
					this_animation.processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Batsmen_Change$1", "SHOW 0.0");
				} else {
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 1, 1, battingCardList);
				}
				if(infobar.getLast_batsmen().get(1).getPlayerId() != battingCardList.get(1).getPlayerId()) {
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 2, 2, battingCardList);
					TimeUnit.MILLISECONDS.sleep(300);
					this_animation.processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Batsmen_Change$2", "START");
					TimeUnit.MILLISECONDS.sleep(800);
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 1, 2, battingCardList);
					TimeUnit.MILLISECONDS.sleep(300);
					this_animation.processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Batsmen_Change$2", "SHOW 0.0");
				} else {
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 1, 2, battingCardList);
				}
			} else {
				populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 1, 1, battingCardList);
				populateTwoBatsmenSingleBatsman(print_writers, matchAllData, WhichSide, 1, 2, battingCardList);
			}
			infobar.setLast_batsmen(battingCardList);
			break;	
		}
		return Constants.OK;
	}
	public void populateRightTopBowler(List<PrintWriter> print_writers, MatchAllData matchAllData,
			int WhichSide, int WhichSubSide) throws InterruptedException {
	
		switch(config.getBroadcaster()) {
		case Constants.ISPL:
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
			if(inning.getBowling_team().getTeamName4().contains("KHILADI XI") || inning.getBowling_team().getTeamName4().contains("MASTER 11")) {
				if(inning.getBowling_team().getTeamName4().equalsIgnoreCase("KHILADI XI")) {
					color2 = "KHILADI_XI";
				}else if(inning.getBowling_team().getTeamName4().equalsIgnoreCase("MASTER 11")) {
					color2 = "MASTER_XI";
				}
			}else {
				color2 = inning.getBowling_team().getTeamName4();
			}
			
			if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$BowlerGrp_All$Side1$img_txt1*TEXTURE*IMAGE SET " + 
						Constants.TEXT_PATH + "1/" + color2 + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$BowlerGrp_All$Side2$img_txt1*TEXTURE*IMAGE SET " + 
						Constants.TEXT_PATH + "1/" + color2 + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$BolwerBaseGrp$img1*TEXTURE*IMAGE SET " 
						+Constants.BASE_PATH + "1/" + color2 + "\0", print_writers);
			}
			
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$BowlerGrp_All$Side" + WhichSide + "$txt_PlayerName*GEOM*TEXT SET " 
					+ bowlingCard.getPlayer().getTicker_name() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$BowlerGrp_All$Side" + WhichSide + "$txt_figure*GEOM*TEXT SET " 
					+ bowlingCard.getWickets() + slashOrDash + bowlingCard.getRuns() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$BowlerGrp_All$Side" + WhichSide + "$txt_Overs*GEOM*TEXT SET " 
					+ CricketFunctions.OverBalls(bowlingCard.getOvers(), bowlingCard.getBalls()) + "\0", print_writers);
			if(bowlingCard.getStatus().equalsIgnoreCase(CricketUtil.CURRENT + CricketUtil.BOWLER)) {
				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Bowler_Grp$Bowler_Highlight", "SHOW 0.440");
			} else {
				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Bowler_Grp$Bowler_Highlight", "SHOW 0.0");
			}
			//this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Bowler_In", "START");
			break;
		case Constants.ICC_U19_2023: 
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bowl_Part$Side" + 
					WhichSubSide + "$txt_Bowler*GEOM*TEXT SET " + bowlingCard.getPlayer().getTicker_name() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bowl_Part$Side" + 
					WhichSubSide + "$txt_Wickets*GEOM*TEXT SET " + bowlingCard.getWickets() + slashOrDash + bowlingCard.getRuns() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bowl_Part$Side" + 
					WhichSubSide + "$txt_Overs*GEOM*TEXT SET " + CricketFunctions.OverBalls(bowlingCard.getOvers(), bowlingCard.getBalls()) + "\0", print_writers);
			
			if(bowlingCard.getStatus().equalsIgnoreCase(CricketUtil.CURRENT + CricketUtil.BOWLER)) {
				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bowl_Lowlight", "SHOW 0.0");
			} else {
				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bowl_Lowlight", "SHOW 0.4");
			}
	
			break;
		case Constants.BENGAL_T20:
			
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$"
					+ "BaseGrp$LeftMask$img_Base1*TEXTURE*IMAGE SET " + 
					Constants.BENGAL_BASE_PATH + "1/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$"
					+ "BaseGrp$GlossBaseGrp$img_Base1*TEXTURE*IMAGE SET " + 
					Constants.BENGAL_BASE_PATH + "1/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$"
					+ "Bowler$img_Text1*TEXTURE*IMAGE SET " + Constants.BENGAL_TEXT_PATH + "1/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
			
			
			if(CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), bowlingCard.getPlayer().getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
						+ "Fade_For_Section_2$Bowler$Side" + WhichSide + "$Select_Impact*FUNCTION*Omo*vis_con SET 1\0", print_writers);
			}else if(CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), bowlingCard.getPlayer().getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
						+ "Fade_For_Section_2$Bowler$Side" + WhichSide + "$Select_Impact*FUNCTION*Omo*vis_con SET 1\0", print_writers);
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
						+ "Fade_For_Section_2$Bowler$Side" + WhichSide + "$Select_Impact*FUNCTION*Omo*vis_con SET 0\0", print_writers);
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
					+ "Fade_For_Section_2$Bowler$Side" + WhichSide + "$txt_Name*GEOM*TEXT SET " + bowlingCard.getPlayer().getTicker_name() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
					+ "Fade_For_Section_2$Bowler$Side" + WhichSide + "$txt_Figs*GEOM*TEXT SET " + bowlingCard.getWickets() + slashOrDash + bowlingCard.getRuns() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
					+ "Fade_For_Section_2$Bowler$Side" + WhichSide + "$txt_Overs*GEOM*TEXT SET " + CricketFunctions.OverBalls(bowlingCard.getOvers(), bowlingCard.getBalls()) + "\0", print_writers);
	
			if(bowlingCard.getStatus().equalsIgnoreCase(CricketUtil.CURRENT + CricketUtil.BOWLER)) {
				this_animation.processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Bowler_Lowlight", "SHOW 0.0");
				WhichSide = 1;
			} else {
				this_animation.processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Bowler_Lowlight", "SHOW 0.4");
				WhichSide = 2;
			}
			
			break;	
		}
	}
	public String populateVizInfobarBowler(List<PrintWriter> print_writers, MatchAllData matchAllData,int WhichSide) throws InterruptedException {

		bowlingCard = inning.getBowlingCard().stream().filter(boc -> boc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.CURRENT+CricketUtil.BOWLER)
			|| boc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.LAST+CricketUtil.BOWLER)).findAny().orElse(null);
		
		if(bowlingCard == null) {
			return "populateVizInfobarBowler: no current bowler found";
		}
		
		switch(config.getBroadcaster()) {
		case Constants.ISPL:
			if(infobar.getLast_bowler() != null) {
				if(infobar.getLast_bowler().getPlayerId() != bowlingCard.getPlayerId()) {
					populateRightTopBowler(print_writers, matchAllData, 1, 2);
					populateRightTopBowler(print_writers, matchAllData, 2, 2);
					this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Bowler_Change", "START");
				} else {
					populateRightTopBowler(print_writers, matchAllData, 1, 1);
					populateRightTopBowler(print_writers, matchAllData, 2, 1);
					this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Bowler_Change", "SHOW 0.0");
				}
			} else {
				populateRightTopBowler(print_writers, matchAllData, 1, 1);
				populateRightTopBowler(print_writers, matchAllData, 2, 1);
			}
			
			infobar.setLast_bowler(bowlingCard);
			break;
		case Constants.ICC_U19_2023: 
			if(infobar.getLast_bowler() != null) {
				if(infobar.getLast_bowler().getPlayerId() != bowlingCard.getPlayerId()) {
					populateRightTopBowler(print_writers, matchAllData, WhichSide, 2);
					populateVizInfobarRightBottom(print_writers, matchAllData, WhichSide, 2);
					this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bowl_Change", "START");
					this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_RightInfo_BottomRightPart", "START");
					TimeUnit.MILLISECONDS.sleep(800);
					populateRightTopBowler(print_writers, matchAllData, WhichSide, 1);
					populateVizInfobarRightBottom(print_writers, matchAllData, WhichSide, 1);
					this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bowl_Change", "SHOW 0.0");
					this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_RightInfo_BottomRightPart", "SHOW 0.0");
				} else {
					populateRightTopBowler(print_writers, matchAllData, 1, 1);
					populateRightTopBowler(print_writers, matchAllData, 1, 2);
					populateRightTopBowler(print_writers, matchAllData, 2, 1);
					populateVizInfobarRightBottom(print_writers, matchAllData, WhichSide, 1);
				}
			} else {
				populateRightTopBowler(print_writers, matchAllData, 1, 1);
				populateRightTopBowler(print_writers, matchAllData, 1, 2);
				populateRightTopBowler(print_writers, matchAllData, 2, 1);
				populateVizInfobarRightBottom(print_writers, matchAllData, WhichSide, 1);
			}
			
			infobar.setLast_bowler(bowlingCard);
			break;
		case Constants.BENGAL_T20: 
			if(infobar.getLast_bowler() != null) {
				if(infobar.getLast_bowler().getPlayerId() != bowlingCard.getPlayerId()) {
					populateRightTopBowler(print_writers, matchAllData, WhichSide, 2);
					populateVizInfobarRightBottom(print_writers, matchAllData, WhichSide, 2);
					this_animation.processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Bowler_Change", "START");
					this_animation.processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Section1_Change", "START");
					TimeUnit.MILLISECONDS.sleep(800);
					populateRightTopBowler(print_writers, matchAllData, WhichSide, 1);
					populateVizInfobarRightBottom(print_writers, matchAllData, WhichSide, 1);
					this_animation.processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Bowler_Change", "SHOW 0.0");
					this_animation.processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Section1_Change", "SHOW 0.0");
				} else {
					populateRightTopBowler(print_writers, matchAllData, 1, 1);
					populateRightTopBowler(print_writers, matchAllData, 1, 2);
					populateRightTopBowler(print_writers, matchAllData, 2, 1);
					populateVizInfobarRightBottom(print_writers, matchAllData, WhichSide, 1);
				}
			} else {
				populateRightTopBowler(print_writers, matchAllData, 1, 1);
				populateRightTopBowler(print_writers, matchAllData, 1, 2);
				populateRightTopBowler(print_writers, matchAllData, 2, 1);
				populateVizInfobarRightBottom(print_writers, matchAllData, WhichSide, 1);
			}
			
			infobar.setLast_bowler(bowlingCard);
			break;	
		}
		
		return Constants.OK;
	}
	public String populateVizInfobarRightBottom(List<PrintWriter> print_writers, MatchAllData matchAllData,
		int WhichSide,int WhichSubSide) throws InterruptedException 
	{
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
			inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);

		if(inning == null) {
			return "populateVizInfobarRightBottom: Inning return is NULL";
		}
		
		switch(config.getBroadcaster()) {
		case Constants.ISPL:
			if(infobar.getRight_bottom() != null && !infobar.getRight_bottom().isEmpty()) {
				switch(infobar.getRight_bottom().toUpperCase()) {
				case CricketUtil.BOWLER:
					this.infobar.setRight_bottom(CricketUtil.BOWLER);
					populateVizInfobarBowler(print_writers, matchAllData, WhichSide);
					break;
				case "BOWLING_END":
					
					if(inning.getBowling_team().getTeamName4().contains("KHILADI XI") || inning.getBowling_team().getTeamName4().contains("MASTER 11")) {
						if(inning.getBowling_team().getTeamName4().equalsIgnoreCase("KHILADI XI")) {
							color2 = "KHILADI_XI";
						}else if(inning.getBowling_team().getTeamName4().equalsIgnoreCase("MASTER 11")) {
							color2 = "MASTER_XI";
						}
					}else {
						color2 = inning.getBowling_team().getTeamName4();
					}
					
					if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side" + WhichSide + "$Free_Text$img_Text1*TEXTURE*IMAGE SET " 
								+ Constants.TEXT_PATH + "1/" + color2 + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side" + WhichSide + "$Select*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					
					if(bowlingCard.getBowling_end() == 1) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side" + WhichSide + "$Free_Text$txt_Header*GEOM*TEXT SET " 
								+ matchAllData.getSetup().getGround().getFirst_bowling_end() + "\0", print_writers);
					}
					else if(bowlingCard.getBowling_end() == 2) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side" + WhichSide + "$Free_Text$txt_Header*GEOM*TEXT SET " 
								+ matchAllData.getSetup().getGround().getSecond_bowling_end() + "\0", print_writers);
					}
					break;
				case CricketUtil.OVER:
					this_data_str = new ArrayList<String>();
					this_data_str.add(CricketFunctions.getEventsText(CricketUtil.OVER,infobar.getLast_bowler().getPlayerId() ,
							",", matchAllData.getEventFile().getEvents(),0));
					
					if(this_data_str.get(this_data_str.size()-1) == null || this_data_str.get(this_data_str.size()-1).split(",").length > 11) {
						return "populateVizInfobarRightBottom: This over data returned invalid";
					}
					
					if(inning.getBowling_team().getTeamName4().contains("KHILADI XI") || inning.getBowling_team().getTeamName4().contains("MASTER 11")) {
						if(inning.getBowling_team().getTeamName4().equalsIgnoreCase("KHILADI XI")) {
							color2 = "KHILADI_XI";
						}else if(inning.getBowling_team().getTeamName4().equalsIgnoreCase("MASTER 11")) {
							color2 = "MASTER_XI";
						}
					}else {
						color2 = inning.getBowling_team().getTeamName4();
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side" + WhichSide + "$Select*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					
					for(int iBall = 0; iBall < this_data_str.get(this_data_str.size()-1).split(",").length; iBall++) {
						
						if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side" + WhichSide + "$This_Over$Balls$"+(iBall + 1)+"$img_Text1*TEXTURE*IMAGE SET " 
									+ Constants.TEXT_PATH + "1/" + color2 + "\0", print_writers);
						}
						
						switch (this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase()) {
						case CricketUtil.DOT:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side"+ WhichSide + "$This_Over"
									+ "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
							break;
						case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FIVE: 
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side"+ WhichSide + "$This_Over"
									+ "$Balls$" + (iBall + 1) + "$Run$txt_Number*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).
									split(",")[iBall] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side"+ WhichSide + "$This_Over"
									+ "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							break;
						case CricketUtil.FOUR:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side"+ WhichSide + "$This_Over"
									+ "$Balls$" + (iBall + 1) + "$Four$txt_4*GEOM*TEXT SET " + CricketUtil.FOUR + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side"+ WhichSide + "$This_Over"
									+ "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
							break;
						case CricketUtil.SIX:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side"+ WhichSide + "$This_Over"
									+ "$Balls$" + (iBall + 1) + "$Six$txt_6*GEOM*TEXT SET " + CricketUtil.SIX + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side"+ WhichSide + "$This_Over"
									+ "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
							break;
						case CricketUtil.NINE:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side"+ WhichSide + "$This_Over"
									+ "$Balls$" + (iBall + 1) + "$Six$txt_6*GEOM*TEXT SET " + CricketUtil.NINE + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side"+ WhichSide + "$This_Over"
									+ "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
							break;
						case "W":
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side"+ WhichSide + "$This_Over"
									+ "$Balls$" + (iBall + 1) + "$Wicket$txt_W*GEOM*TEXT SET " + "W" + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side"+ WhichSide + "$This_Over"
									+ "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
							break;

						default:
							
							if(this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase().contains("BOUNDARY")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side"+ WhichSide + "$This_Over"
										+ "$Balls$" + (iBall + 1) + "$Extra$txt_Extra*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).
										split(",")[iBall].toUpperCase().replace("BOUNDARY", "") + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side"+ WhichSide + "$This_Over"
										+ "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
							}else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side"+ WhichSide + "$This_Over"
										+ "$Balls$" + (iBall + 1) + "$Extra$txt_Extra*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).
										split(",")[iBall].toUpperCase() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side"+ WhichSide + "$This_Over"
										+ "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 5 \0", print_writers);
							}
							
							break;
						}
					}
					
//					if(this_data_str.get(this_data_str.size()-1).split(",").length > 6) {
//						if(infobar.getLast_this_over() != null && infobar.getLast_this_over().split(",").length > 6) {
//							if(infobar.isThisOvers_Title_Fade() == false) {
//								this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$This_Over_Title_Fade_Out", "START");
//								infobar.setThisOvers_Title_Fade(true);
//							}
//						}
//					} else {
//						if(infobar.getLast_this_over() != null && infobar.getLast_this_over().split(",").length <= 6) {
//							this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$This_Over_Title_Fade_Out", "CONTINUE REVERSE");
//							infobar.setThisOvers_Title_Fade(false);
//						}
//					}
					
					if(Integer.valueOf(CricketFunctions.processThisOverRunsCount(infobar.getLast_bowler().getPlayerId(),matchAllData.getEventFile().getEvents())
							.split(slashOrDash)[1]) > 0) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side"+ WhichSide + "$This_Over"
								 + "$Balls*FUNCTION*Omo*vis_con SET " + (this_data_str.get(this_data_str.size()-1).split(",").length) + " \0", print_writers);
					}
					else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage2$Side"+ WhichSide + "$This_Over"
								 + "$Balls*FUNCTION*Omo*vis_con SET " + "0" + " \0", print_writers);
					}
					
					infobar.setLast_this_over(this_data_str.get(this_data_str.size()-1));
					break;
				}
				
				infobar.setLast_right_bottom(infobar.getRight_bottom());
			}
			break;
		case Constants.ICC_U19_2023: 
			if(infobar.getRight_bottom() != null && !infobar.getRight_bottom().isEmpty()) {
				switch(infobar.getRight_bottom().toUpperCase()) {
				case "BOWLER_REPLACE":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bottom_Right_Part$Side_" + 
							WhichSubSide + "$Select_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
					
					int Replaced_Player_id = CricketFunctions.SecondLastBowlerId(matchAllData,bowlingCard.getPlayerId());
					
					if(Replaced_Player_id > 0) {
						player = Players.stream().filter(plyr -> plyr.getPlayerId() == Replaced_Player_id).findAny().orElse(null);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bottom_Right_Part$Side_" + 
								WhichSubSide + "$FreeText$txt_Free*GEOM*TEXT SET " + bowlingCard.getPlayer().getTicker_name() + " REPLACE " + 
								player.getTicker_name() + "\0", print_writers);
					}
					break;
				case "BOWLING_END":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bottom_Right_Part$Side_" + 
							WhichSubSide + "$Select_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
					
					if(bowlingCard.getBowling_end() == 1) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bottom_Right_Part$Side_" + 
							WhichSubSide + "$FreeText$txt_Free*GEOM*TEXT SET " + matchAllData.getSetup().getGround().getFirst_bowling_end() + "\0", print_writers);
					}
					else if(bowlingCard.getBowling_end() == 2) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bottom_Right_Part$Side_" + 
							WhichSubSide + "$FreeText$txt_Free*GEOM*TEXT SET " + matchAllData.getSetup().getGround().getSecond_bowling_end() + "\0", print_writers);
					}
					break;
				case CricketUtil.OVER:
					this_data_str = new ArrayList<String>();
					this_data_str.add(CricketFunctions.getEventsText(CricketUtil.OVER,infobar.getLast_bowler().getPlayerId() ,
							",", matchAllData.getEventFile().getEvents(),0));
					
					if(this_data_str.get(this_data_str.size()-1) == null || this_data_str.get(this_data_str.size()-1).split(",").length > 11) {
						return "populateVizInfobarRightBottom: This over data returned invalid";
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bottom_Right_Part"
							+ "$Side_" + WhichSubSide + "$Select_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					
					for(int iBall = 0; iBall < this_data_str.get(this_data_str.size()-1).split(",").length; iBall++) {
						switch (this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase()) {
						case CricketUtil.DOT:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_"+ WhichSide + "$Bottom_Right_Part$Side_" + 
									WhichSubSide + "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
							break;
						case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FIVE: 
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_"+ WhichSide + "$Bottom_Right_Part$Side_" + 
									WhichSubSide + "$Balls$" + (iBall + 1) + "$Run$txt_Number*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).
										split(",")[iBall] + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_"+ WhichSide + "$Bottom_Right_Part$Side_" + 
									WhichSubSide + "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							break;
						case CricketUtil.FOUR:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_"+ WhichSide + "$Bottom_Right_Part$Side_" + 
									WhichSubSide + "$Balls$" + (iBall + 1) + "$Four$txt_4*GEOM*TEXT SET " + CricketUtil.FOUR + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_"+ WhichSide + "$Bottom_Right_Part$Side_" + 
									WhichSubSide + "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
							break;
						case CricketUtil.SIX:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_"+ WhichSide + "$Bottom_Right_Part$Side_" + 
									WhichSubSide + "$Balls$" + (iBall + 1) + "$Six$txt_6*GEOM*TEXT SET " + CricketUtil.SIX + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_"+ WhichSide + "$Bottom_Right_Part$Side_" + 
									WhichSubSide + "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
							break;
						case "W":
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_"+ WhichSide + "$Bottom_Right_Part$Side_" + 
								WhichSubSide + "$Balls$" + (iBall + 1) + "$Run$txt_Number*GEOM*TEXT SET W \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_"+ WhichSide + "$Bottom_Right_Part$Side_" + 
								WhichSubSide + "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
							break;

						default:
							
							if(this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase().contains("BOUNDARY")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_"+ WhichSide + "$Bottom_Right_Part$Side_" + 
										WhichSubSide + "$Balls$" + (iBall + 1) + "$Extra$txt_Extra*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).
											split(",")[iBall].toUpperCase().replace("BOUNDARY", "") + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_"+ WhichSide + "$Bottom_Right_Part$Side_" + 
										WhichSubSide + "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
							}else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_"+ WhichSide + "$Bottom_Right_Part$Side_" + 
										WhichSubSide + "$Balls$" + (iBall + 1) + "$Extra$txt_Extra*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).
											split(",")[iBall].toUpperCase() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_"+ WhichSide + "$Bottom_Right_Part$Side_" + 
										WhichSubSide + "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 5 \0", print_writers);
							}
							
//							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_"+ WhichSide + "$Bottom_Right_Part$Side_" + 
//									WhichSubSide + "$Balls$" + (iBall + 1) + "$Extra$txt_Extra*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).
//										split(",")[iBall].toUpperCase() + "\0", print_writers);
//							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_"+ WhichSide + "$Bottom_Right_Part$Side_" + 
//									WhichSubSide + "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 5 \0", print_writers);
							
							break;
						}
					}
					
					if(this_data_str.get(this_data_str.size()-1).split(",").length > 6) {
						if(infobar.getLast_this_over() != null && infobar.getLast_this_over().split(",").length > 6) {
							if(infobar.isThisOvers_Title_Fade() == false) {
								this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$This_Over_Title_Fade_Out", "START");
								infobar.setThisOvers_Title_Fade(true);
							}
						}
					} else {
						if(infobar.getLast_this_over() != null && infobar.getLast_this_over().split(",").length <= 6) {
							this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$This_Over_Title_Fade_Out", "CONTINUE REVERSE");
							infobar.setThisOvers_Title_Fade(false);
						}
					}
					
					if(Integer.valueOf(CricketFunctions.processThisOverRunsCount(infobar.getLast_bowler().getPlayerId(),matchAllData.getEventFile().getEvents())
							.split(slashOrDash)[1]) > 0) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_"+ WhichSide + "$Bottom_Right_Part$Side_" + 
								WhichSubSide + "$Balls*FUNCTION*Omo*vis_con SET " + (this_data_str.get(this_data_str.size()-1).split(",").length) + "\0", print_writers);
					}
					else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_"+ WhichSide + "$Bottom_Right_Part$Side_" + 
								WhichSubSide + "$Balls*FUNCTION*Omo*vis_con SET " + "0" + "\0", print_writers);
					}
					
					infobar.setLast_this_over(this_data_str.get(this_data_str.size()-1));
					break;
				}
				
				infobar.setLast_right_bottom(infobar.getRight_bottom());
			}
			break;
			
		case Constants.BENGAL_T20: 
			if(infobar.getRight_bottom() != null && !infobar.getRight_bottom().isEmpty()) {
				switch(infobar.getRight_bottom().toUpperCase()) {
				case "BOWLER_REPLACE":
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$"
							+ "Stats$Side" + WhichSide + "$Free_Text$img_Text1*TEXTURE*IMAGE SET " + Constants.BENGAL_TEXT_PATH + "1/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bottom_Right_Part$Side_" + 
							WhichSubSide + "$Select_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
					
					int Replaced_Player_id = CricketFunctions.SecondLastBowlerId(matchAllData,bowlingCard.getPlayerId());
					
					if(Replaced_Player_id > 0) {
						player = Players.stream().filter(plyr -> plyr.getPlayerId() == Replaced_Player_id).findAny().orElse(null);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bottom_Right_Part$Side_" + 
								WhichSubSide + "$FreeText$txt_Free*GEOM*TEXT SET " + bowlingCard.getPlayer().getTicker_name() + " REPLACE " + 
								player.getTicker_name() + "\0", print_writers);
					}
					break;
				case "BOWLING_END":
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$"
							+ "Stats$Side" + WhichSide + "$Free_Text$img_Text1*TEXTURE*IMAGE SET " + Constants.BENGAL_TEXT_PATH + "1/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Section_2"
							+ "$Stats$Side" + WhichSide + "$Slect_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					
					if(bowlingCard.getBowling_end() == 1) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Section_2"
								+ "$Stats$Side" + WhichSide + "$Slect_Type$Free_Text$txt_TeamName*GEOM*TEXT SET " + 
								matchAllData.getSetup().getGround().getFirst_bowling_end().toUpperCase() + " \0", print_writers);
					}
					else if(bowlingCard.getBowling_end() == 2) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Section_2"
								+ "$Stats$Side" + WhichSide + "$Slect_Type$Free_Text$txt_TeamName*GEOM*TEXT SET " + 
								matchAllData.getSetup().getGround().getSecond_bowling_end().toUpperCase() + " \0", print_writers);
					}
					break;
				case CricketUtil.OVER:
					this_data_str = new ArrayList<String>();
					this_data_str.add(CricketFunctions.getEventsText(CricketUtil.OVER,infobar.getLast_bowler().getPlayerId() ,
							",", matchAllData.getEventFile().getEvents(),0));
					
					if(this_data_str.get(this_data_str.size()-1) == null || this_data_str.get(this_data_str.size()-1).split(",").length > 11) {
						return "populateVizInfobarRightBottom: This over data returned invalid";
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Section_2"
							+ "$Stats$Side" + WhichSide + "$Slect_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					
					for(int iBall = 0; iBall < this_data_str.get(this_data_str.size()-1).split(",").length; iBall++) {
						
						if(this_data_str.get(this_data_str.size()-1).split(",").length <= 9) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Section_2"
									+ "$Stats$Side" + WhichSide + "$Slect_Type$ThisOver$Ball_" + (iBall +1) + "*ACTIVE SET 1 \0", print_writers);
							
							if(this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase().equalsIgnoreCase(CricketUtil.FOUR) || 
									this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase().equalsIgnoreCase(CricketUtil.SIX) || 
									this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase().contains("BOUNDARY")) {
								
								if(this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase().contains("BOUNDARY")) {
									
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$"
											+ "Stats$Side" + WhichSide + "$ThisOver$Ball_" + (iBall +1) + "$3$img_Base2*TEXTURE*IMAGE SET " + Constants.BENGAL_BASE_PATH + "2/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$"
											+ "Stats$Side" + WhichSide + "$ThisOver$Ball_" + (iBall +1) + "$3$img_Text2*TEXTURE*IMAGE SET " + Constants.BENGAL_TEXT_PATH + "2/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
									
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Section_2"
											+ "$Stats$Side" + WhichSide + "$Slect_Type$ThisOver$Ball_" + (iBall + 1) + "$Select_DataType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Section_2"
											+ "$Stats$Side" + WhichSide + "$Slect_Type$ThisOver$Ball_" + (iBall + 1) + "$Select_DataType$3$txt_Figures*GEOM*TEXT SET " + 
											this_data_str.get(this_data_str.size()-1).split(",")[iBall].replace("BOUNDARY", "").trim() + "\0", print_writers);
									
								}else {
									
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$"
											+ "Stats$Side" + WhichSide + "$ThisOver$Ball_" + (iBall +1) + "$2$img_Text1*TEXTURE*IMAGE SET " + Constants.BENGAL_TEXT_PATH + "1/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$"
											+ "Stats$Side" + WhichSide + "$ThisOver$Ball_" + (iBall +1) + "$2$img_This_Over*TEXTURE*IMAGE SET " + Constants.BENGAL_OVER_PATH + inning.getBowling_team().getTeamName4() + "\0", print_writers);
									
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Section_2"
											+ "$Stats$Side" + WhichSide + "$Slect_Type$ThisOver$Ball_" + (iBall + 1) + "$Select_DataType*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Section_2"
											+ "$Stats$Side" + WhichSide + "$Slect_Type$ThisOver$Ball_" + (iBall + 1) + "$Select_DataType$2$txt_Figures*GEOM*TEXT SET " + 
											this_data_str.get(this_data_str.size()-1).split(",")[iBall].trim() + "\0", print_writers);
									
								}
							}else if(this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase().equalsIgnoreCase(CricketUtil.DOT)){
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$"
										+ "Stats$Side" + WhichSide + "$ThisOver$Ball_" + (iBall +1) + "$1$img_Text1*TEXTURE*IMAGE SET " + Constants.BENGAL_TEXT_PATH + "1/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Section_2"
										+ "$Stats$Side" + WhichSide + "$Slect_Type$ThisOver$Ball_" + (iBall + 1) + "$Select_DataType*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Section_2"
										+ "$Stats$Side" + WhichSide + "$Slect_Type$ThisOver$Ball_" + (iBall + 1) + "$Select_DataType$1$txt_Figures*GEOM*TEXT SET " + 
										"0" + "\0", print_writers);
							
							}else if(this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase().contains("+W")|| 
									this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase().equalsIgnoreCase("W")) {
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$"
										+ "Stats$Side" + WhichSide + "$ThisOver$Ball_" + (iBall +1) + "$3$img_Base2*TEXTURE*IMAGE SET " + Constants.BENGAL_BASE_PATH + "2/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$"
										+ "Stats$Side" + WhichSide + "$ThisOver$Ball_" + (iBall +1) + "$3$img_Text2*TEXTURE*IMAGE SET " + Constants.BENGAL_TEXT_PATH + "2/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Section_2"
										+ "$Stats$Side" + WhichSide + "$Slect_Type$ThisOver$Ball_" + (iBall + 1) + "$Select_DataType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Section_2"
										+ "$Stats$Side" + WhichSide + "$Slect_Type$ThisOver$Ball_" + (iBall + 1) + "$Select_DataType$3$txt_Figures*GEOM*TEXT SET " + 
										this_data_str.get(this_data_str.size()-1).split(",")[iBall].trim() + "\0", print_writers);
							}
							else if(this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase().contains("WD") || 
									this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase().contains("NB")
									 || this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase().contains("B") || 
									 this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase().contains("LB")) {
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$"
										+ "Stats$Side" + WhichSide + "$ThisOver$Ball_" + (iBall +1) + "$2$img_Text1*TEXTURE*IMAGE SET " + Constants.BENGAL_TEXT_PATH + "1/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$"
										+ "Stats$Side" + WhichSide + "$ThisOver$Ball_" + (iBall +1) + "$2$img_This_Over*TEXTURE*IMAGE SET " + Constants.BENGAL_OVER_PATH + inning.getBowling_team().getTeamName4() + "\0", print_writers);
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Section_2"
										+ "$Stats$Side" + WhichSide + "$Slect_Type$ThisOver$Ball_" + (iBall + 1) + "$Select_DataType*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Section_2"
										+ "$Stats$Side" + WhichSide + "$Slect_Type$ThisOver$Ball_" + (iBall + 1) + "$Select_DataType$2$txt_Figures*GEOM*TEXT SET " + 
										this_data_str.get(this_data_str.size()-1).split(",")[iBall].trim() + "\0", print_writers);
							}
							else if(this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase().contains("PN")) {
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$"
										+ "Stats$Side" + WhichSide + "$ThisOver$Ball_" + (iBall +1) + "$2$img_Text1*TEXTURE*IMAGE SET " + Constants.BENGAL_TEXT_PATH + "1/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$"
										+ "Stats$Side" + WhichSide + "$ThisOver$Ball_" + (iBall +1) + "$2$img_This_Over*TEXTURE*IMAGE SET " + Constants.BENGAL_OVER_PATH + inning.getBowling_team().getTeamName4() + "\0", print_writers);
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Section_2"
										+ "$Stats$Side" + WhichSide + "$Slect_Type$ThisOver$Ball_" + (iBall + 1) + "$Select_DataType*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Section_2"
										+ "$Stats$Side" + WhichSide + "$Slect_Type$ThisOver$Ball_" + (iBall + 1) + "$Select_DataType$2$txt_Figures*GEOM*TEXT SET " + 
										this_data_str.get(this_data_str.size()-1).split(",")[iBall].trim() + "\0", print_writers);
							}
							else {
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$"
										+ "Stats$Side" + WhichSide + "$ThisOver$Ball_" + (iBall +1) + "$2$img_Text1*TEXTURE*IMAGE SET " + Constants.BENGAL_TEXT_PATH + "1/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$"
										+ "Stats$Side" + WhichSide + "$ThisOver$Ball_" + (iBall +1) + "$2$img_This_Over*TEXTURE*IMAGE SET " + Constants.BENGAL_OVER_PATH + inning.getBowling_team().getTeamName4() + "\0", print_writers);
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Section_2"
										+ "$Stats$Side" + WhichSide + "$Slect_Type$ThisOver$Ball_" + (iBall + 1) + "$Select_DataType*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Section_2"
										+ "$Stats$Side" + WhichSide + "$Slect_Type$ThisOver$Ball_" + (iBall + 1) + "$Select_DataType$2$txt_Figures*GEOM*TEXT SET " + 
										this_data_str.get(this_data_str.size()-1).split(",")[iBall].trim() + "\0", print_writers);
								
							}
						}
//						else {
//							processAnimation(print_writer, "Section3$ThisOverIn", "SHOW 0.0", broadcaster);
//							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$BowlerGrp$Section3$Section3All$FreeText$img_Text$txt_Data1*GEOM*TEXT SET " + 
//									"THIS OVER : " + CricketFunctions.processThisOverRunsCount(infobar.getPlayer_id(),match.getEventFile().getEvents()) + "\0");
//							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$BowlerGrp$Section3$Section3All$FreeText$img_Text*TEXTURE*IMAGE SET " + 
//									text_path + "1/" + inn.getBowling_team().getTeamName4() +" \0");
//							processAnimation(print_writer, "Section3$FreeTextIn", "SHOW 0.480", broadcaster);
//						}
					}
					
//					if(this_data_str.get(this_data_str.size()-1).split(",").length > 9) {
//						if(infobar.getLast_this_over() != null && infobar.getLast_this_over().split(",").length > 9) {
//							if(infobar.isThisOvers_Title_Fade() == false) {
//								this_animation.processAnimation(Constants.FRONT, print_writers, "anim_Infobar$This_Over_Title_Fade_Out", "START");
//								infobar.setThisOvers_Title_Fade(true);
//							}
//						}
//					} else {
//						if(infobar.getLast_this_over() != null && infobar.getLast_this_over().split(",").length <= 9) {
//							this_animation.processAnimation(Constants.FRONT, print_writers, "anim_Infobar$This_Over_Title_Fade_Out", "CONTINUE REVERSE");
//							infobar.setThisOvers_Title_Fade(false);
//						}
//					}
					
					if(Integer.valueOf(CricketFunctions.processThisOverRunsCount(infobar.getLast_bowler().getPlayerId(),matchAllData.getEventFile().getEvents())
							.split(slashOrDash)[1]) > 0) {
						
						for(int i = 0; i < this_data_str.get(this_data_str.size()-1).split(",").length; i++ ) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$Stats$Side" + 
									WhichSide + "$Slect_Type$ThisOver$Ball_" + (i+1) + "*ACTIVE SET 1 \0", print_writers);
						}
						
						for(int i = 8; i >= this_data_str.get(this_data_str.size()-1).split(",").length; i-- ) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$Stats$Side" + 
									WhichSide + "$Slect_Type$ThisOver$Ball_" + (i+1) + "*ACTIVE SET 0 \0", print_writers);
						}
						
					}
					else {
						for(int i = 1; i <= 9; i++ ) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Fade_For_Section_2$Stats$Side" + 
									WhichSide + "$Slect_Type$ThisOver$Ball_" + (i) + "*ACTIVE SET 0 \0", print_writers);
						}
					}
					
					infobar.setLast_this_over(this_data_str.get(this_data_str.size()-1));
					break;
				}
				
				infobar.setLast_right_bottom(infobar.getRight_bottom());
			}
			break;	
		}
		
		return Constants.OK;
	}
	
	public String populateVizInfobarRightSection(boolean is_this_updating,List<PrintWriter> print_writers, MatchAllData matchAllData,
			int WhichSide,int WhichSubSide) throws InterruptedException, CloneNotSupportedException 
		{	
			switch(config.getBroadcaster()) {
			case Constants.ICC_U19_2023: 
				if(infobar.getRight_section() != null && !infobar.getRight_section().isEmpty()) {
					switch(infobar.getRight_section().toUpperCase()) {
					case CricketUtil.BOUNDARY:
						inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
						inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);

						if(inning == null) {
							return "populateVizInfobarRightSection: Inning return is NULL";
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + 
								"$Bowl_Part_All$Side" + WhichSubSide + "$Select*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + 
								"$Bowl_Part_All$Side" + WhichSubSide + "$Boundaries$Position$Boundaries$Fours$txt_Four*GEOM*TEXT SET " 
									+ inning.getTotalFours() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + 
								"$Bowl_Part_All$Side" + WhichSubSide + "$Boundaries$Position$Boundaries$txt_FourHead*GEOM*TEXT SET " 
									+ "FOUR" + CricketFunctions.Plural(inning.getTotalFours()).toUpperCase() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + 
								"$Bowl_Part_All$Side" + WhichSubSide + "$Boundaries$Position$Boundaries$Sixes$txt_Six*GEOM*TEXT SET " 
									+ inning.getTotalSixes() + "\0", print_writers);
						
						if(inning.getTotalSixes()==1) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + 
									"$Bowl_Part_All$Side" + WhichSubSide + "$Boundaries$Position$Boundaries$txt_SixHead*GEOM*TEXT SET " 
										+ "SIX" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + 
									"$Bowl_Part_All$Side" + WhichSubSide + "$Boundaries$Position$Boundaries$txt_SixHead*GEOM*TEXT SET " 
										+ "SIXES" + "\0", print_writers);
						}
						
						TimeUnit.MILLISECONDS.sleep(1000);
						break;
					case "COMPARE":
						inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)
								&& inn.getInningNumber() == 2).findAny().orElse(null);
							
							if(inning == null) {
								return "populateVizInfobarRightSection: 2nd Inning returned is NULL";
							}
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + 
									"$Bowl_Part_All$Side" + WhichSubSide + "$Select*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bowl_Part_All$Side" + 
									WhichSubSide + "$FreeTex$Position$Boundaries$txt_Head*GEOM*TEXT SET " + inning.getBowling_team().getTeamName1() + " WERE" + 
									"\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bowl_Part_All$Side" + 
									WhichSubSide + "$FreeTex$Position$Boundaries$Score$txt_ScoreFour*GEOM*TEXT SET " + CricketFunctions.
									compareInningData(matchAllData, "-", 1, matchAllData.getEventFile().getEvents()) + "\0", print_writers);
						break;
					}
					
					infobar.setLast_right_section(infobar.getRight_section());
				}
				break;
			case Constants.BENGAL_T20: 
				if(infobar.getRight_section() != null && !infobar.getRight_section().isEmpty()) {
					switch(infobar.getRight_section().toUpperCase()) {
					case "LAST_X_BALLS":
						
						inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
						if(inning == null) {
							return "populateVizInfobarMiddleSection: Inning returned is NULL";
						}
						//System.out.println(lastXballs);
						this_data_str = new ArrayList<String>();
						this_data_str.add(CricketFunctions.getlastthirtyballsdata(matchAllData, slashOrDash, matchAllData.getEventFile().getEvents(), lastXballs));
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$BaseGrp$"
								+ "LeftMask$img_Base1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$BaseGrp$"
								+ "GlossBaseGrp$img_Base1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Last_X_Overs$Figures_Grp$img_This_Over*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_OVER_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Last_X_Overs$img_Text1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Last_X_Overs$Runs_Grp$Header_Grp$img_Text1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Last_X_Overs$Runs_Grp$Figures_Grp$img_Text1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Last_X_Overs$Runs_Grp$Figures_Grp$img_This_Over*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_OVER_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Last_X_Overs$Wickets_Grp$Header_Grp$img_Text1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Last_X_Overs$Wickets_Grp$Figures_Grp$img_Text1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Last_X_Overs$Wickets_Grp$Figures_Grp$img_This_Over*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_OVER_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						if(this_data_str.get(this_data_str.size()-1) == null || this_data_str.get(this_data_str.size()-1).split(slashOrDash).length > 4) {
							return "populateVizInfobarMiddleSection: Last " + lastXballs + " Balls data returned invalid";
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
								+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType*FUNCTION*Omo*vis_con SET 6 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Last_X_Overs$img_Text1$txt_Head*GEOM*TEXT SET " + 
								 "LAST " + lastXballs + " BALLS" + "\0", print_writers);
						
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Last_X_Overs$Runs_Grp$txt_Header*GEOM*TEXT SET " + 
								"RUN" + CricketFunctions.Plural(Integer.valueOf(this_data_str.get(this_data_str.size()-1).split(slashOrDash)[0])).toUpperCase() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Last_X_Overs$Runs_Grp$txt_Figures*GEOM*TEXT SET " + 
								this_data_str.get(this_data_str.size()-1).split(slashOrDash)[0] + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Last_X_Overs$Wickets_Grp$txt_Header*GEOM*TEXT SET " + 
								"WICKET" + CricketFunctions.Plural(Integer.valueOf(this_data_str.get(this_data_str.size()-1).split(slashOrDash)[1])).toUpperCase() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Last_X_Overs$Wickets_Grp$txt_Figures*GEOM*TEXT SET " + 
								this_data_str.get(this_data_str.size()-1).split(slashOrDash)[1] + "\0", print_writers);
						
						TimeUnit.MILLISECONDS.sleep(1000);
						break;
					case "BALLS_SINCE_LAST_BOUNDARY":
						inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
							inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
					
						if(inning == null) {
							return "populateVizInfobarMiddleSection: Inning returned is NULL";
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$BaseGrp$"
								+ "LeftMask$img_Base1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$BaseGrp$"
								+ "GlossBaseGrp$img_Base1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Balls_Since$Figures_Grp$img_This_Over*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_OVER_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Balls_Since$img_Text1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Balls_Since$Figures_Grp$img_Text1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
								+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType*FUNCTION*Omo*vis_con SET 7 \0", print_writers);
						
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
								+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType$Balls_Since$txt_Head*GEOM*TEXT SET BALLS SINCE LAST BOUNDARY \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
								+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType$Balls_Since$txt_Figures*GEOM*TEXT SET " + 
								CricketFunctions.lastFewOversData(CricketUtil.BOUNDARY, matchAllData.getEventFile().getEvents(), inning.getInningNumber()) + " \0", print_writers);
						
						break;
					case "THIS_MATCH_SIXES":
						
						inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
						inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);

						if(inning == null) {
							return "populateVizInfobarRightSection: Inning return is NULL";
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$BaseGrp$"
								+ "LeftMask$img_Base1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$BaseGrp$"
								+ "GlossBaseGrp$img_Base1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Balls_Since$Figures_Grp$img_This_Over*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_OVER_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Balls_Since$img_Text1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Balls_Since$Figures_Grp$img_Text1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
								+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType*FUNCTION*Omo*vis_con SET 7 \0", print_writers);
						
						this_data_str = new ArrayList<String>();
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
								+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType$Balls_Since$txt_Head*GEOM*TEXT SET SIXES THIS INNINGS \0", print_writers);

						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
								+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType$Balls_Since$txt_Figures*GEOM*TEXT SET " + 
								CricketFunctions.extracttournamentFoursAndSixes("CURRENT_MATCH_DATA",tournament_matches, matchAllData, null).getTournament_sixes() + " \0", print_writers);
						
						break;
					case "THIS_MATCH_FOURS":
						
						inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
						inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);

						if(inning == null) {
							return "populateVizInfobarRightSection: Inning return is NULL";
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$BaseGrp$"
								+ "LeftMask$img_Base1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$BaseGrp$"
								+ "GlossBaseGrp$img_Base1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Balls_Since$Figures_Grp$img_This_Over*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_OVER_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Balls_Since$img_Text1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Balls_Since$Figures_Grp$img_Text1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
								+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType*FUNCTION*Omo*vis_con SET 7 \0", print_writers);
						
						this_data_str = new ArrayList<String>();
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
								+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType$Balls_Since$txt_Head*GEOM*TEXT SET FOURS THIS INNINGS \0", print_writers);

						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
								+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType$Balls_Since$txt_Figures*GEOM*TEXT SET " + 
								CricketFunctions.extracttournamentFoursAndSixes("CURRENT_MATCH_DATA",tournament_matches, matchAllData, null).getTournament_fours() + " \0", print_writers);
						
						break;
					case "LAST_WICKET":

						
						inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
							inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
						
						String how_out_txt = "";
						
						if(inning == null) {
							return "populateVizInfobarMiddleSection: Inning returned is NULL";
						}
						
						if(inning.getFallsOfWickets() == null && inning.getFallsOfWickets().isEmpty()) {
							return "populateVizInfobarMiddleSection: FoW returned is EMPTY";
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$BaseGrp$"
								+ "LeftMask$img_Base1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$BaseGrp$"
								+ "GlossBaseGrp$img_Base1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Last_Wicket$TopText$img_Text1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						battingCardList.add(inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == 
							inning.getFallsOfWickets().get(inning.getFallsOfWickets().size() - 1).getFowPlayerID()).findAny().orElse(null));

						if(battingCardList.get(battingCardList.size()-1) == null) {
							return "populateVizInfobarLeftBottom: Last wicket returned is invalid";
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
								+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
						
						if(battingCardList.get(battingCardList.size()-1).getHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
							if(battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute() != null && 
									battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
								how_out_txt = "run out " + "sub (" + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + ")";
							} else {
								how_out_txt = "run out (" + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + ")";
							}
						}
						else if(battingCardList.get(battingCardList.size()-1).getHowOut().equalsIgnoreCase(CricketUtil.CAUGHT)) {
							if(battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute() != null && 
									battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
								how_out_txt = "c" +  " sub (" + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + ")  b " + 
										battingCardList.get(battingCardList.size()-1).getHowOutBowler().getTicker_name();
							} else {
								how_out_txt = "c " + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + "  b " + 
										battingCardList.get(battingCardList.size()-1).getHowOutBowler().getTicker_name();
							}
						}else {
							if(!battingCardList.get(battingCardList.size()-1).getHowOutPartOne().isEmpty()) {
								how_out_txt = battingCardList.get(battingCardList.size()-1).getHowOutPartOne();
							}
							
							if(!battingCardList.get(battingCardList.size()-1).getHowOutPartTwo().isEmpty()) {
								if(!how_out_txt.trim().isEmpty()) {
									how_out_txt = how_out_txt + "  " + battingCardList.get(battingCardList.size()-1).getHowOutPartTwo();
								}else {
									how_out_txt = battingCardList.get(battingCardList.size()-1).getHowOutPartTwo();
								}
							}
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
								+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType$Last_Wicket$BottomText$txt_Name*GEOM*TEXT SET " + 
								battingCardList.get(battingCardList.size()-1).getPlayer().getFirstname() + " " + how_out_txt + " \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
								+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType$Last_Wicket$BottomText$txt_Runs*GEOM*TEXT SET " + 
								battingCardList.get(battingCardList.size()-1).getRuns()+ " \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
								+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType$Last_Wicket$BottomText$txt_Balls*GEOM*TEXT SET " +
								battingCardList.get(battingCardList.size()-1).getBalls() + " \0", print_writers);
						
				
						break;	
					case CricketUtil.BOUNDARY:
						inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
						inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);

						if(inning == null) {
							return "populateVizInfobarRightSection: Inning return is NULL";
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$BaseGrp$"
								+ "LeftMask$img_Base1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$BaseGrp$"
								+ "GlossBaseGrp$img_Base1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Boundaries$BottomText$Fours_Grp$img_This_Over*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_OVER_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Boundaries$BottomText$Six_Grp$img_This_Over*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_OVER_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Boundaries$TopText$img_Text1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Boundaries$BottomText$Fours_Grp$Header_Grp$img_Text1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Boundaries$BottomText$Fours_Grp$Figures_Grp$img_Text1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Boundaries$BottomText$Six_Grp$Header_Grp$img_Text1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
								+ "Boundaries$BottomText$Six_Grp$Figures_Grp$img_Text1*TEXTURE*IMAGE SET " + 
								Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
								+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
								+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType$Boundaries$BottomText$Fours_Grp$txt_Figures*GEOM*TEXT SET " + inning.getTotalFours() + " \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
								+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType$Boundaries$BottomText$Six_Grp$txt_Figures*GEOM*TEXT SET " + inning.getTotalSixes() + " \0", print_writers);
						
						TimeUnit.MILLISECONDS.sleep(1000);
						break;
					case "COMPARE":
						inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)
								&& inn.getInningNumber() == 2).findAny().orElse(null);
							
							if(inning == null) {
								return "populateVizInfobarRightSection: 2nd Inning returned is NULL";
							}
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$BaseGrp$"
									+ "LeftMask$img_Base1*TEXTURE*IMAGE SET " + 
									Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$BaseGrp$"
									+ "GlossBaseGrp$img_Base1*TEXTURE*IMAGE SET " + 
									Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
									+ "At_This_Stage$BottomText$Figures_Grp$img_This_Over*TEXTURE*IMAGE SET " + 
									Constants.BENGAL_OVER_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
									+ "At_This_Stage$TopText$img_Text1*TEXTURE*IMAGE SET " + 
									Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
									+ "At_This_Stage$BottomText$Details_Grp$img_Text1*TEXTURE*IMAGE SET " + 
									Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$Section_2_Grp$Side" + WhichSide + "$Select_DataType$"
									+ "At_This_Stage$BottomText$Figures_Grp$img_Text1*TEXTURE*IMAGE SET " + 
									Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
									+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
									+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType$At_This_Stage$BottomText$Details_Grp$txt_TeamName*GEOM*TEXT SET " + 
									inning.getBowling_team().getTeamName4() + " WERE" + " \0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$TeamGrp2$Fade_For_Analytics$"
									+ "Section_2_Grp$Side" + WhichSide + "$Select_DataType$At_This_Stage$BottomText$Figures_Grp$txt_Figures*GEOM*TEXT SET " + 
									CricketFunctions.compareInningData(matchAllData, "-", 1, matchAllData.getEventFile().getEvents()) + " \0", print_writers);
						break;
					}
					
					infobar.setLast_right_section(infobar.getRight_section());
				}
				break;	
			case Constants.ISPL:
				switch(infobar.getRight_section().toUpperCase()) {
				case "SUPER_OVER":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TOPRIGHT_FREETEXT$Tapped_Text_All$img_txt2*GEOM*TEXT SET " + 
							"SUPER OVER" + "\0",print_writers);
					break;
				case "TARGET":
					populateTarget(print_writers,matchAllData);
					break;
				case "TAPED_BALL":
					populateTapeBall(print_writers,matchAllData);
					break;
				case "EQUATION":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$ToWin$ToWin_Text_All$fig_Runs*GEOM*TEXT SET " + 
							CricketFunctions.getRequiredRuns(matchAllData) + "\0",print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$ToWin$ToWin_Text_All$fig_Balls*GEOM*TEXT SET " + 
							CricketFunctions.getRequiredBalls(matchAllData) + "\0",print_writers);
					break;
				case "TIMELINE":
					if(is_this_updating == false) {
						for(int i = 1; i <= 10; i++) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + i + "$Select_OverType"
									+ "*FUNCTION*Omo*vis_con SET 0\0",print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + i + "$Select_OverType$YetTo_Bowl"
									+ "$txt_Title*GEOM*TEXT SET  \0",print_writers);
						}
					}
					for(Inning inn : matchAllData.getMatch().getInning()) {
						if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
							
//							if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).size() != (inn.getTotalOvers()+1)) {
//								for (BowlingCard boc : matchAllData.getMatch().getInning().get(inn.getInningNumber() - 1).getBowlingCard()) {
//									if(boc.getStatus().equalsIgnoreCase(CricketUtil.CURRENT + CricketUtil.BOWLER)) {
//										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (inn.getTotalOvers()+1) + "$Select_OverType"
//												+ "$RunningOver$txt_OverData*GEOM*TEXT SET " + "0/0" + "\0",print_writers);
//										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (inn.getTotalOvers()+1) + "$Select_OverType"
//												+ "*FUNCTION*Omo*vis_con SET 1\0",print_writers);
//									}
//								}
//							}
							
							for(int j =0; j<= getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).size() - 1; j++) {
								
								if(inn.getFirstPowerplayEndOver() >= (j+1)) {
									//------------ first powerplay ---------------------------------//
									if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).contains("CR")) {
										
										int cr = 0;
										
										if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split(",")[1].equalsIgnoreCase("-")) {
											cr = (Integer.valueOf(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[0]) - 
												Integer.valueOf(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split(",")[2]));
										}else if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split(",")[1].equalsIgnoreCase("+")){
											cr = (Integer.valueOf(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[0]) + 
													Integer.valueOf(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split(",")[2]));
										}
										
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
												+ "*FUNCTION*Omo*vis_con SET 5\0",print_writers);
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
												+ "$ChallengeOver$txt_TapeBall*GEOM*TEXT SET 50-50 OVER/PP\0",print_writers);
										
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
												+ "$ChallengeOver$txt_OverData*GEOM*TEXT SET " + 
												cr + "-" + getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[1] + "\0",print_writers);
										
									}else if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).contains("EO")) {
										if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).contains("TO")) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "*FUNCTION*Omo*vis_con SET 4\0",print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "$TapeBallOver$txt_TapeBall*GEOM*TEXT SET TAPE BALL/PP\0",print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "$TapeBallOver$txt_OverData*GEOM*TEXT SET " + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[0] + "-" + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[1] + "\0",print_writers);
											
										}else {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "*FUNCTION*Omo*vis_con SET 3\0",print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "$PowerPlayOver$txt_OverData*GEOM*TEXT SET " + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[0] + "-" + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[1] + "\0",print_writers);
											
										}
									}else if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).contains("CO")) {
										if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).contains("TO")) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "*FUNCTION*Omo*vis_con SET 4\0",print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "$TapeBallOver$txt_TapeBall*GEOM*TEXT SET TAPE BALL/PP\0",print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "$TapeBallOver$txt_OverData*GEOM*TEXT SET " + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[0] + "-" + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[1] + "\0",print_writers);
											
										}else {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "$PowerPlayOver$txt_OverData*GEOM*TEXT SET " + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[0] + "-" + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[1] + "\0",print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "*FUNCTION*Omo*vis_con SET 3\0",print_writers);
											
										}
									}
								}else if(inn.getSecondPowerplayStartOver() == (j+1) && inn.getSecondPowerplayStartOver() != 0) {
									//--------------------------------second powerplay ----------------------------------------------//
									if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).contains("CR")) {
										
										int cr = 0;
										
										if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split(",")[1].equalsIgnoreCase("-")) {
											cr = (Integer.valueOf(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[0]) - 
												Integer.valueOf(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split(",")[2]));
											
										}else if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split(",")[1].equalsIgnoreCase("+")){
											cr = (Integer.valueOf(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[0]) + 
													Integer.valueOf(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split(",")[2]));
											
										}
										
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
												+ "*FUNCTION*Omo*vis_con SET 5\0",print_writers);
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
												+ "$ChallengeOver$txt_TapeBall*GEOM*TEXT SET 50-50 OVER/PP\0",print_writers);
										
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
												+ "$ChallengeOver$txt_OverData*GEOM*TEXT SET " + 
												cr + "-" + getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[1] + "\0",print_writers);
										
									}else if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).contains("EO")) {
										if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).contains("TO")) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "*FUNCTION*Omo*vis_con SET 4\0",print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "$TapeBallOver$txt_TapeBall*GEOM*TEXT SET TAPE BALL/PP\0",print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "$TapeBallOver$txt_OverData*GEOM*TEXT SET " + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[0] + "-" + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[1] + "\0",print_writers);
											
											
										}else {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "*FUNCTION*Omo*vis_con SET 3\0",print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "$PowerPlayOver$txt_OverData*GEOM*TEXT SET " + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[0] + "-" + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[1] + "\0",print_writers);
											
											
										}
									}else if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).contains("CO")) {
										if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).contains("TO")) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "*FUNCTION*Omo*vis_con SET 4\0",print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "$TapeBallOver$txt_TapeBall*GEOM*TEXT SET TAPE BALL/PP\0",print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "$TapeBallOver$txt_OverData*GEOM*TEXT SET " + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[0] + "-" + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[1] + "\0",print_writers);
											
											
										}else {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "$PowerPlayOver$txt_OverData*GEOM*TEXT SET " + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[0] + "-" + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[1] + "\0",print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "*FUNCTION*Omo*vis_con SET 3\0",print_writers);
											
											
										}
									}
								}else {
									if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).contains("CR")) {
										
										int cr = 0;
										
										if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split(",")[1].equalsIgnoreCase("-")) {
											cr = (Integer.valueOf(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[0]) - 
												Integer.valueOf(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split(",")[2]));
											
										}else if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split(",")[1].equalsIgnoreCase("+")){
											cr = (Integer.valueOf(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[0]) + 
													Integer.valueOf(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split(",")[2]));
											
										}
										
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
												+ "*FUNCTION*Omo*vis_con SET 5\0",print_writers);
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
												+ "$ChallengeOver$txt_OverData*GEOM*TEXT SET " + 
												cr + "-" + getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[1] + "\0",print_writers);
										
										
									}else if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).contains("EO")) {
										if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).contains("TO")) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "*FUNCTION*Omo*vis_con SET 4\0",print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "$TapeBallOver$txt_TapeBall*GEOM*TEXT SET TAPE BALL\0",print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "$TapeBallOver$txt_OverData*GEOM*TEXT SET " + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[0] + "-" + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[1] + "\0",print_writers);
										}else {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "*FUNCTION*Omo*vis_con SET 2\0",print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "$CompletedOver$txt_OverData*GEOM*TEXT SET " + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[0] + "-" + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[1] + "\0",print_writers);
										}
									}else if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).contains("CO")) {
										if(getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).contains("TO")) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "*FUNCTION*Omo*vis_con SET 4\0",print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "$TapeBallOver$txt_TapeBall*GEOM*TEXT SET TAPE BALL\0",print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "$TapeBallOver$txt_OverData*GEOM*TEXT SET " + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[0] + "-" + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[1] + "\0",print_writers);
										}else {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "$RunningOver$txt_OverData*GEOM*TEXT SET " + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[0] + "-" + 
													getOverbyOver(inn.getInningNumber(), matchAllData.getEventFile().getEvents(), matchAllData).get(j).split("-")[1] + "\0",print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$TimeLine$OversGrp$Over" + (j+1) + "$Select_OverType"
													+ "*FUNCTION*Omo*vis_con SET 1\0",print_writers);
										}
									}
								}
							}
						}
					}
					
					break;
				}
				break;
			}
			
			return Constants.OK;
		}
	
	public String populateVizInfobarLeftBottom(List<PrintWriter> print_writers, MatchAllData matchAllData,int WhichSide) 
	{
		
		switch(config.getBroadcaster()) {
		case Constants.ICC_U19_2023: 
			switch(infobar.getLeft_bottom().toUpperCase()) {
			case "CRR":
				
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
					inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				
				if(inning == null) {
					return "populateVizInfobarLeftBottom: Inning return is NULL";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" 
					+ WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide + "$Stat$txt_Title*GEOM*TEXT SET " + 
						"CURRENT RUN RATE" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide + 
					"$Stat$txt_Value*GEOM*TEXT SET " + CricketFunctions.generateRunRate(inning.getTotalRuns(),inning.getTotalOvers(), 
							inning.getTotalBalls(), 2,matchAllData) + "\0", print_writers);
				break;
				
			case CricketUtil.TOSS:
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" 
					+ WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
					+ CricketFunctions.generateTossResult(matchAllData, CricketUtil.FULL, CricketUtil.FIELD, CricketUtil.SHORT, CricketUtil.ELECTED).toUpperCase() + "\0", print_writers);
				break;
				
			case "VENUE":
				
				ground = Grounds.stream().filter(grnd -> grnd.getFullname().contains(matchAllData.getSetup().getVenueName())).findAny().orElse(null);
				if(ground == null) {
					return "populateVizInfobarLeftBottom: Ground Name [" + matchAllData.getSetup().getVenueName() + "] from database is returning NULL";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" 
					+ WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
					+ "LIVE FROM " + ground.getCity() + "\0", print_writers);
				break;
			
			case "EQUATION":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" 
					+ WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				if(matchAllData.getSetup().getTargetType() != null && !matchAllData.getSetup().getTargetType().isEmpty()) {
					if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.DLS)) {
						if(CricketFunctions.getRequiredRuns(matchAllData) <= 1) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text"
									+ "*GEOM*TEXT SET " + "NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " RUN" + CricketFunctions.Plural(
										CricketFunctions.getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.getRequiredBalls(matchAllData) 
											+ " BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + " (" + 
													CricketUtil.DLS + ")"+ "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text"
									+ "*GEOM*TEXT SET " + "NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " MORE RUN" + CricketFunctions.Plural(
										CricketFunctions.getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.getRequiredBalls(matchAllData) 
											+ " BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + " (" + 
											CricketUtil.DLS + ")" + "\0", print_writers);
						}
					}
					else if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.VJD)) {
						if(CricketFunctions.getRequiredRuns(matchAllData) <= 1) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text"
									+ "*GEOM*TEXT SET " + "NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " RUN" + CricketFunctions.Plural(
										CricketFunctions.getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.getRequiredBalls(matchAllData) 
											+ " BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + " (" + 
													CricketUtil.VJD + ")"+ "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text"
									+ "*GEOM*TEXT SET " + "NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " MORE RUN" + CricketFunctions.Plural(
										CricketFunctions.getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.getRequiredBalls(matchAllData) 
											+ " BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + " (" + 
											CricketUtil.VJD + ")" + "\0", print_writers);
						}
					}
				}else {
					if(CricketFunctions.getRequiredRuns(matchAllData) <= 1) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text"
								+ "*GEOM*TEXT SET " + "NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " RUN" + CricketFunctions.Plural(
									CricketFunctions.getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.getRequiredBalls(matchAllData) 
										+ " BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text"
								+ "*GEOM*TEXT SET " + "NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " MORE RUN" + CricketFunctions.Plural(
									CricketFunctions.getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.getRequiredBalls(matchAllData) 
										+ " BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + "\0", print_writers);
					}
				}
				break;
			
			case "CURRENT_SESSION":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" 
						+ WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
					+ "SESSION RUN RATE " + CricketFunctions.generateRunRate(matchAllData.getMatch().getDaysSessions().get(matchAllData.getMatch().
						getDaysSessions().size()-1).getTotalRuns(), 0, matchAllData.getMatch().getDaysSessions().get(matchAllData.getMatch().
						getDaysSessions().size()-1).getTotalBalls(), 2, matchAllData) + "\0", print_writers);
				
				break;	
				
			case "LOCAL-TIME":
				
				Date dt = new Date();
		        SimpleDateFormat dateFormat;
		        dateFormat = new SimpleDateFormat("hh:mm a");
			      
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" 
						+ WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
						+ "LOCAL TIME : " + dateFormat.format(dt) + "\0", print_writers);	
				break;
				
			case "BOWLING_TEAM_TARGET":
				
				inning = matchAllData.getMatch().getInning().stream().filter(in->in.getIsCurrentInning()
						.equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
						+ "$Choose_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
						+ "$Stat$txt_Title*GEOM*TEXT SET " + " " + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
							+ "$Stat$txt_Value*GEOM*TEXT SET " + inning.getBowling_team().getTeamName1()+" NEED "+(10-inning.getTotalWickets())+" WICKET"
							+CricketFunctions.Plural((10-inning.getTotalWickets())).toUpperCase()+" TO WIN "+ "\0", print_writers);
			
				break;
				
			case "FIRST_INNING_SCORE":
				
				int id = 0;
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" 
						+ WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
						+ matchAllData.getMatch().getInning().get(matchAllData.getMatch().getInning().size() - 1).getBatting_team().getTeamName1() + " -" + "\0", print_writers);
				
				for(int i = matchAllData.getMatch().getInning().size() - 1; i >= 0; i--) {
					if (matchAllData.getMatch().getInning().get(i).getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
						id = matchAllData.getMatch().getInning().get(i).getBattingTeamId();
						System.out.println("inning num    "+matchAllData.getMatch().getInning().get(i).getInningNumber()+"  id  "+id);
					}
					
					if (matchAllData.getMatch().getInning().get(i).getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.NO) && 
							(matchAllData.getMatch().getInning().get(i).getBattingTeamId() == id)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
								+ matchAllData.getMatch().getInning().get(i).getBatting_team().getTeamName1() + " " + matchAllData.getMatch().getInning().get(i).getTotalRuns() + "\0", print_writers);
					}
				}
				break;
				
			case "CURRENT_INNING_OVER":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" 
						+ WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				for(Inning inn : matchAllData.getMatch().getInning()) {
					if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
						
						if(inn.getTotalOvers() == 0 || inn.getTotalOvers() > 1) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
									+ "v "+ inn.getBowling_team().getTeamName1() + ", OVER" + CricketFunctions.Plural(inn.getTotalOvers()).toUpperCase() + " " + 
									CricketFunctions.OverBalls(inn.getTotalOvers(), inn.getTotalBalls()) + "\0", print_writers);
						}else {
							if(inn.getTotalBalls() == 1) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
										+ "v "+ inn.getBowling_team().getTeamName1() + ", OVERS " + CricketFunctions.OverBalls(inn.getTotalOvers(), inn.getTotalBalls()) + "\0", print_writers);
							}
							else if(inn.getTotalBalls() > 0) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
										+ "v "+ inn.getBowling_team().getTeamName1() + ", OVER" + CricketFunctions.Plural(inn.getTotalBalls()).toUpperCase() + " "  + 
										CricketFunctions.OverBalls(inn.getTotalOvers(), inn.getTotalBalls()) + "\0", print_writers);
							}
							else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
										+ "v "+ inn.getBowling_team().getTeamName1() + ", OVER " + CricketFunctions.OverBalls(inn.getTotalOvers(), inn.getTotalBalls()) + "\0", print_writers);
							}
						}
					}
				}
				break;
			
			case "WHICH_INNING":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" 
						+ WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				for(Inning inn : matchAllData.getMatch().getInning()) {
					if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
						if(inn.getInningNumber() == 1 || inn.getInningNumber() == 2) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
									+ "1st INNINGS" + "\0", print_writers);
						}else if(inn.getInningNumber() == 3 || inn.getInningNumber() == 4) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
									+ "2nd INNINGS" + "\0", print_writers);
						}
					}
				}
				break;
				
			case "DAY_SESSION":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" 
						+ WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				if(matchAllData.getMatch().getDaysSessions().get(matchAllData.getMatch().getDaysSessions().size()-1).getIsCurrentSession().equalsIgnoreCase(CricketUtil.YES)) {
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
							+ "DAY " + matchAllData.getMatch().getDaysSessions().get(matchAllData.getMatch().getDaysSessions().size()-1).getDayNumber() + " - SESSION " + 
							matchAllData.getMatch().getDaysSessions().get(matchAllData.getMatch().getDaysSessions().size()-1).getSessionNumber() + "\0", print_writers);
				}
				
				break;
			
			case "REMAINING_OVERS":
				int daysnumber=0;
				int over_bowled=0,remain_overs=0;

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" 
						+ WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				daysnumber = matchAllData.getMatch().getDaysSessions().get(matchAllData.getMatch().getDaysSessions().size()-1).getDayNumber();
				for(DaySession day_session : matchAllData.getMatch().getDaysSessions()) {
					if(daysnumber == day_session.getDayNumber()) {
						over_bowled = over_bowled + day_session.getTotalBalls();
					}
				}
				
				if(inning.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					remain_overs = (inning.getOversRemaining() * 6) - over_bowled;
				}
				if(remain_overs % 6 == 0) {
					remain_overs = remain_overs / 6 ;
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
							+ "OVERS LEFT TODAY : " + remain_overs + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
							+ "OVERS LEFT TODAY : " + CricketFunctions.OverBalls(0, remain_overs) + "\0", print_writers);
				}
				
				break;
				
			case "FOLLOW_ON":
				int rem_runs = 0;
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" 
						+ WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				rem_runs = ((matchAllData.getMatch().getInning().get(0).getTotalRuns() - matchAllData.getSetup().getFollowOnThreshold()) + 1 );
				rem_runs = rem_runs - matchAllData.getMatch().getInning().get(1).getTotalRuns();
				if(rem_runs > 0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
							+ "RUNS TO AVOID FOLLOW-ON : " + rem_runs + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
							+ "FOLLOW-ON AVOIDED" + "\0", print_writers);
				}
				break;
					
				
			case "GROUP":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" 
					+ WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
					+ matchAllData.getSetup().getMatchIdent() + "\0", print_writers);
				break;
				
			case "INNINGS_SCORE":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" 
						+ WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
						+ "1st INNS - " + matchAllData.getMatch().getInning().get(0).getBatting_team().getTeamName1() + " "
						+  CricketFunctions.getTeamScore(matchAllData.getMatch().getInning().get(0), "-", false) + " (" + CricketFunctions.OverBalls(
						matchAllData.getMatch().getInning().get(0).getTotalOvers(), matchAllData.getMatch().getInning().get(0).getTotalBalls()) + ")" + "\0", print_writers);
					break;
				
			case "RRR":

				if(!matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					return "populateVizInfobarLeftBottom: Required run rate available in 2nd inning only";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
					+ "$Choose_Type*FUNCTION*Omo*vis_con SET 2 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide + "$Stat$txt_Title*GEOM*TEXT SET " + 
					"REQUIRED RUN RATE" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
					+ "$Stat$txt_Value*GEOM*TEXT SET " + CricketFunctions.generateRunRate(CricketFunctions.getRequiredRuns(matchAllData),
					0,CricketFunctions.getRequiredBalls(matchAllData),2,matchAllData) + "\0", print_writers);
				break;
				
			case "TARGET":
				
				if(!matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)&&
						!matchAllData.getMatch().getInning().get(3).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					return "populateVizInfobarLeftBottom: Target available in 2nd inning only";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
					+ "$Choose_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
					+ "$Stat$txt_Title*GEOM*TEXT SET " + "TARGET" + "\0", print_writers);
				
				if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.DLS)) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
							+ "$Stat$txt_Value*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " (" + CricketUtil.DLS + ")" + "\0", print_writers);
				}
				else if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.VJD)) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
							+ "$Stat$txt_Value*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " (" + CricketUtil.VJD + ")" + "\0", print_writers);
				}
				else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
							+ "$Stat$txt_Value*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + "\0", print_writers);
				}
				
				break;
			
			case "DLSTARGET":
				
				if(!matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					return "populateVizInfobarLeftBottom: Target available in 2nd inning only";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
					+ "$Choose_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
					+ "$Stat$txt_Title*GEOM*TEXT SET " + "D/L TARGET" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
					+ "$Stat$txt_Value*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + "\0", print_writers);
				break;
				
			}
			infobar.setLast_left_bottom(infobar.getLeft_bottom());
			break;
			
		case Constants.BENGAL_T20: 
			switch(infobar.getLeft_bottom().toUpperCase()) {
			case "CRR":
				
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
					inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				
				if(inning == null) {
					return "populateVizInfobarLeftBottom: Inning return is NULL";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$Section_3_Grp$Side" 
					+ WhichSide + "$Select_Infotype*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$Section_3_Grp$Side" 
					+ WhichSide + "$Select_Infotype$RunRate$txt_Target_Head*GEOM*TEXT SET " + 
						"CRR" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$Section_3_Grp$Side" 
						+ WhichSide + "$Select_Infotype$RunRate$txt_TargetRuns*GEOM*TEXT SET " + CricketFunctions.generateRunRate(inning.getTotalRuns(),inning.getTotalOvers(), 
								inning.getTotalBalls(), 2,matchAllData) + "\0", print_writers);
				break;
				
			case CricketUtil.TOSS:
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$Section_3_Grp$Side" 
						+ WhichSide + "$Select_Infotype*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$Section_3_Grp$Side" 
					+ WhichSide + "$Select_Infotype$Toss$txt_Toss_Result*GEOM*TEXT SET " + 
					CricketFunctions.generateTossResult(matchAllData, CricketUtil.MINI, "", CricketUtil.SHORT, "").toUpperCase().split(":")[1].trim() + "\0", print_writers);
				break;
				
			case "VENUE":
				
				ground = Grounds.stream().filter(grnd -> grnd.getFullname().contains(matchAllData.getSetup().getVenueName())).findAny().orElse(null);
				if(ground == null) {
					return "populateVizInfobarLeftBottom: Ground Name [" + matchAllData.getSetup().getVenueName() + "] from database is returning NULL";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" 
					+ WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
					+ "LIVE FROM " + ground.getCity() + "\0", print_writers);
				break;
			
			case "EQUATION":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" 
					+ WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				if(matchAllData.getSetup().getTargetType() != null && !matchAllData.getSetup().getTargetType().isEmpty()) {
					if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.DLS)) {
						if(CricketFunctions.getRequiredRuns(matchAllData) <= 1) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text"
									+ "*GEOM*TEXT SET " + "NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " RUN" + CricketFunctions.Plural(
										CricketFunctions.getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.getRequiredBalls(matchAllData) 
											+ " BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + " (" + 
													CricketUtil.DLS + ")"+ "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text"
									+ "*GEOM*TEXT SET " + "NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " MORE RUN" + CricketFunctions.Plural(
										CricketFunctions.getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.getRequiredBalls(matchAllData) 
											+ " BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + " (" + 
											CricketUtil.DLS + ")" + "\0", print_writers);
						}
					}
					else if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.VJD)) {
						if(CricketFunctions.getRequiredRuns(matchAllData) <= 1) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text"
									+ "*GEOM*TEXT SET " + "NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " RUN" + CricketFunctions.Plural(
										CricketFunctions.getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.getRequiredBalls(matchAllData) 
											+ " BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + " (" + 
													CricketUtil.VJD + ")"+ "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text"
									+ "*GEOM*TEXT SET " + "NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " MORE RUN" + CricketFunctions.Plural(
										CricketFunctions.getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.getRequiredBalls(matchAllData) 
											+ " BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + " (" + 
											CricketUtil.VJD + ")" + "\0", print_writers);
						}
					}
				}else {
					if(CricketFunctions.getRequiredRuns(matchAllData) <= 1) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text"
								+ "*GEOM*TEXT SET " + "NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " RUN" + CricketFunctions.Plural(
									CricketFunctions.getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.getRequiredBalls(matchAllData) 
										+ " BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text"
								+ "*GEOM*TEXT SET " + "NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " MORE RUN" + CricketFunctions.Plural(
									CricketFunctions.getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.getRequiredBalls(matchAllData) 
										+ " BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + "\0", print_writers);
					}
				}
				break;
				
			case "RRR":

				if(!matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					return "populateVizInfobarLeftBottom: Required run rate available in 2nd inning only";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$Section_3_Grp$Side" 
					+ WhichSide + "$Select_Infotype*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$Section_3_Grp$Side" 
					+ WhichSide + "$Select_Infotype$RunRate$txt_Target_Head*GEOM*TEXT SET " + 
						"RRR" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$Section_3_Grp$Side" 
						+ WhichSide + "$Select_Infotype$RunRate$txt_TargetRuns*GEOM*TEXT SET " + CricketFunctions.generateRunRate(CricketFunctions.getRequiredRuns(matchAllData),
								0,CricketFunctions.getRequiredBalls(matchAllData),2,matchAllData) + "\0", print_writers);
				break;
				
			case "TARGET":
				
				if(!matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)&&
						!matchAllData.getMatch().getInning().get(3).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					return "populateVizInfobarLeftBottom: Target available in 2nd inning only";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$Section_3_Grp$Side" 
						+ WhichSide + "$Select_Infotype*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				
				if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.DLS)) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$Section_3_Grp$Side" + WhichSide 
							+ "$Select_Infotype$Target$txt_TargetRuns*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " (" + CricketUtil.DLS + ")" + "\0", print_writers);
				}
				else if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.VJD)) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$Section_3_Grp$Side" + WhichSide 
							+ "$Select_Infotype$Target$txt_TargetRuns*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " (" + CricketUtil.VJD + ")" + "\0", print_writers);
				}
				else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Fade_For_Analytics$Section_3_Grp$Side" + WhichSide 
							+ "$Select_Infotype$Target$txt_TargetRuns*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + "\0", print_writers);
				}
				
				break;
			
			case "DLSTARGET":
				
				if(!matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					return "populateVizInfobarLeftBottom: Target available in 2nd inning only";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
					+ "$Choose_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
					+ "$Stat$txt_Title*GEOM*TEXT SET " + "D/L TARGET" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
					+ "$Stat$txt_Value*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + "\0", print_writers);
				break;
				
			}
			infobar.setLast_left_bottom(infobar.getLeft_bottom());
			break;	
		}
		
		return Constants.OK;
	}
	public String populateVizInfobarMiddleSection(List<PrintWriter> print_writers, MatchAllData matchAllData, int WhichSide) throws InterruptedException, CloneNotSupportedException, JsonMappingException, JsonProcessingException 
	{
		switch(config.getBroadcaster()) {
		case Constants.ISPL:
			
			switch(infobar.getMiddle_section().toUpperCase()) {
			case CricketUtil.BATSMAN:
				this.infobar.setMiddle_section(CricketUtil.BATSMAN);
				
				populateCurrentBatsmen(print_writers, matchAllData, WhichSide);
				populateVizInfobarBowler(print_writers, matchAllData, WhichSide);
				break;
			case "CURR_PARTNERSHIP":

				inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
					inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
				if(inning == null) {
					return "populateVizInfobarMiddleSection: Inning returned is NULL";
				}
				
				if(inning.getPartnerships() != null && inning.getPartnerships().size() <= 0) {
					return "populateVizInfobarMiddleSection: Partnership size is NULL/Zero";
				}
				
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$CurrentPartnership$txt_Header*TEXTURE*IMAGE SET " 
//						+ Constants.BASE_PATH + "2/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$CurrentPartnership$First$txt_StatHead*TEXTURE*IMAGE SET " 
//						+ Constants.BASE_PATH + "2/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$CurrentPartnership$First$txt_Value*TEXTURE*IMAGE SET " 
//						+ Constants.BASE_PATH + "2/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide 
					+ "$Select*FUNCTION*Omo*vis_con SET 0 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$CurrentPartnership$txt_Header*GEOM*TEXT SET " + 
						"CURRENT PARTNERSHIP " + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$CurrentPartnership$txt_StatHead*GEOM*TEXT SET " + 
						inning.getPartnerships().get(inning.getPartnerships().size()-1).getTotalRuns() + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$CurrentPartnership$txt_Value*GEOM*TEXT SET " + 
						inning.getPartnerships().get(inning.getPartnerships().size()-1).getTotalBalls() + "\0",print_writers);
				break;
			case "CRR":
				
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
					inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				
				if(inning == null) {
					return "populateVizInfobarLeftBottom: Inning return is NULL";
				}
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$CurrentRunRate$txt_Header*TEXTURE*IMAGE SET " 
//						+ Constants.BASE_PATH + "2/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$CurrentRunRate$First$txt_StatValue*TEXTURE*IMAGE SET " 
//						+ Constants.BASE_PATH + "2/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide 
						+ "$Select*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$CurrentRunRate$txt_Header*GEOM*TEXT SET " + 
						"CURRENT RUN RATE " + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$CurrentRunRate$txt_StatValue*GEOM*TEXT SET " + 
						CricketFunctions.generateRunRate(inning.getTotalRuns(),inning.getTotalOvers(), inning.getTotalBalls(), 2,matchAllData) + "\0",print_writers);
				break;
			case "RRR":
				
				if(!matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					return "populateVizInfobarLeftBottom: Required run rate available in 2nd inning only";
				}
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Current_ReqRunRate$txt_Header*TEXTURE*IMAGE SET " 
//						+ Constants.BASE_PATH + "2/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Current_ReqRunRate$First$txt_StatValue*TEXTURE*IMAGE SET " 
//						+ Constants.BASE_PATH + "2/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//				
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Current_ReqRunRate$txt_Header02*TEXTURE*IMAGE SET " 
//						+ Constants.BASE_PATH + "2/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Current_ReqRunRate$Second$txt_StatValue*TEXTURE*IMAGE SET " 
//						+ Constants.BASE_PATH + "2/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide 
						+ "$Select*FUNCTION*Omo*vis_con SET 2 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Current_ReqRunRate$txt_Header*GEOM*TEXT SET " + 
						"CRR " + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Current_ReqRunRate$txt_Header02*GEOM*TEXT SET " + 
						"REQ RR " + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Current_ReqRunRate$First$txt_StatValue*GEOM*TEXT SET " + 
						CricketFunctions.generateRunRate(inning.getTotalRuns(),inning.getTotalOvers(), inning.getTotalBalls(), 2,matchAllData) + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Current_ReqRunRate$Second$txt_StatValue*GEOM*TEXT SET " + 
						CricketFunctions.generateRunRate(CricketFunctions.getRequiredRuns(matchAllData),0,CricketFunctions.getRequiredBalls(matchAllData),2,matchAllData) + "\0",print_writers);
				break;
			case "TARGET":
				
				if(!matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)&&
						!matchAllData.getMatch().getInning().get(3).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					return "populateVizInfobarMiddleSection: Target available in 2nd inning only";
				}
				inning = matchAllData.getMatch().getInning().stream().filter(inn ->inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Target$Icon*TEXTURE*IMAGE SET " 
//						+ Constants.BASE_PATH + "2/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Target$txt_Header*TEXTURE*IMAGE SET " 
//						+ Constants.BASE_PATH + "2/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Target$First$txt_StatValue*TEXTURE*IMAGE SET " 
//						+ Constants.BASE_PATH + "2/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide 
						+ "$Select*FUNCTION*Omo*vis_con SET 3 \0",print_writers);
				if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("VJD")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Target$txt_StatValue*GEOM*TEXT SET " + 
							CricketFunctions.getTargetRuns(matchAllData) +" (VJD)" + "\0",print_writers);
				}else if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("DLS")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Target$txt_StatValue*GEOM*TEXT SET " + 
							CricketFunctions.getTargetRuns(matchAllData) + "(DLS)" + "\0",print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Target$txt_StatValue*GEOM*TEXT SET " + 
							CricketFunctions.getTargetRuns(matchAllData) + "\0",print_writers);
				}
				
				break;
			case "EQUATION":
				
				if(!matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)&&
						!matchAllData.getMatch().getInning().get(3).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					return "populateVizInfobarMiddleSection: Equation available in 2nd inning only";
				}
				inning = matchAllData.getMatch().getInning().stream().filter(inn ->inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Equation$First$txt_StatHead*TEXTURE*IMAGE SET " 
//						+ Constants.BASE_PATH + "2/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Equation$First$txt_StatValue*TEXTURE*IMAGE SET " 
//						+ Constants.BASE_PATH + "2/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide 
						+ "$Select*FUNCTION*Omo*vis_con SET 4 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Equation$First$txt_Value*GEOM*TEXT SET " + 
						CricketFunctions.getRequiredRuns(matchAllData) + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Equation$Second$txt_Value*GEOM*TEXT SET " + 
						CricketFunctions.getRequiredBalls(matchAllData) + "\0",print_writers);
				
				if(CricketFunctions.getRequiredRuns(matchAllData) <= 1) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Equation$Second$txt_StatHead*GEOM*TEXT SET " + 
							"RUN FROM" + "\0",print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Equation$Second$txt_StatHead*GEOM*TEXT SET " + 
							"MORE RUNS FROM" + "\0",print_writers);
				}
				if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("VJD")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Equation$Third$txt_StatHead*GEOM*TEXT SET " + 
							"BALL"+ CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + " (VJD)" + "\0",print_writers);
				}else if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("DLS")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Equation$Third$txt_StatHead*GEOM*TEXT SET " + 
							"BALL"+ CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + " (DLS)" + "\0",print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Equation$Third$txt_StatHead*GEOM*TEXT SET " + 
							"BALL"+ CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + "\0",print_writers);
				}

				break;
			case "RESULTS":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide 
						+ "$Select*FUNCTION*Omo*vis_con SET 5 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage1$Side" + WhichSide + "$Free_Text$txt_Header*GEOM*TEXT SET " + 
						CricketFunctions.getRequiredRuns(matchAllData) + "\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Top*GEOM*TEXT SET " + 
						CricketFunctions.generateMatchSummaryStatus(2, matchAllData, CricketUtil.FULL, "|", config.getBroadcaster()).toUpperCase() + "\0", print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Bottom*GEOM*TEXT SET " + 
//						CricketFunctions.generateMatchSummaryStatus(2, matchAllData, CricketUtil.FULL, "|", config.getBroadcaster()).toUpperCase().split("\\|")[1] + "\0", print_writers);
				
				break;
			}
			break;
		case Constants.ICC_U19_2023: 

			switch(infobar.getMiddle_section().toUpperCase()) {
			
			case "OVER_RATE":
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 11 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
						"OVER RATE : " + CricketFunctions.BetterOverRate(inning.getTotalOvers(), inning.getTotalBalls() , inning.getDuration(), "", false) + " OVERS PER HOUR" + "\0", print_writers);
				break;
			case "LEAD_TRAIL_EQUATION":
				
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Right$Side_" + WhichSide 
//						+ "$Select_Type*FUNCTION*Omo*vis_con SET 11\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 11 \0",print_writers);
				
				if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
					if(matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						if(CricketFunctions.getTeamRunsAhead(2,matchAllData) > 0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
									matchAllData.getMatch().getInning().get(1).getBatting_team().getTeamName2() + " LEAD BY " + CricketFunctions.getTeamRunsAhead(2,matchAllData) + " RUN" + 
									CricketFunctions.Plural(CricketFunctions.getTeamRunsAhead(2,matchAllData)).toUpperCase() + "\0", print_writers);
						} else if(CricketFunctions.getTeamRunsAhead(2,matchAllData) == 0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
									"SCORES ARE LEVEL" + "\0", print_writers);
						} else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
									matchAllData.getMatch().getInning().get(1).getBatting_team().getTeamName2() + " TRAIL BY " + (-1 * CricketFunctions.getTeamRunsAhead(2,matchAllData)) + " RUN" + 
									CricketFunctions.Plural(-1 * CricketFunctions.getTeamRunsAhead(2,matchAllData)).toUpperCase() + "\0", print_writers);
						}
					}else if(matchAllData.getMatch().getInning().get(2).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						if(CricketFunctions.getTeamRunsAhead(3,matchAllData) > 0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
									matchAllData.getMatch().getInning().get(2).getBatting_team().getTeamName2() + " LEAD BY " + CricketFunctions.getTeamRunsAhead(3,matchAllData) + " RUN" + 
									CricketFunctions.Plural(CricketFunctions.getTeamRunsAhead(3,matchAllData)).toUpperCase() + "\0", print_writers);
						} else if(CricketFunctions.getTeamRunsAhead(3,matchAllData) == 0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
									"SCORES ARE LEVEL" + "\0", print_writers);
						} else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
									matchAllData.getMatch().getInning().get(2).getBatting_team().getTeamName2() + " TRAIL BY " + (-1 * CricketFunctions.getTeamRunsAhead(3,matchAllData)) + " RUN" + 
									CricketFunctions.Plural(-1 * CricketFunctions.getTeamRunsAhead(3,matchAllData)).toUpperCase() + "\0", print_writers);
						}
					}else if(matchAllData.getMatch().getInning().get(3).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
								matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0", print_writers);
					}
				}else {
					if(CricketFunctions.getRequiredRuns(matchAllData) == 0 || matchAllData.getMatch().getInning().get(1).getTotalWickets() >= 10 || CricketFunctions.getRequiredBalls(matchAllData) == 0) {

						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Free_Text$txt_Header*GEOM*TEXT SET " + 
								CricketFunctions.generateMatchSummaryStatus(2, matchAllData, CricketUtil.FULL, config.getBroadcaster()).toUpperCase() + "\0", print_writers);

						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
								CricketFunctions.generateMatchSummaryStatus(2, matchAllData, CricketUtil.FULL, config.getBroadcaster()).toUpperCase() + "\0", print_writers);

					}else {
						if(matchAllData.getSetup().getTargetOvers() == null || matchAllData.getSetup().getTargetOvers().trim().isEmpty() && matchAllData.getSetup().getTargetRuns() == 0) {
							if(CricketFunctions.getRequiredRuns(matchAllData) == 0) {
								if(matchAllData.getMatch().getMatchStatus() != null) {
									if(matchAllData.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
												"MATCH TIED" + "\0", print_writers);
									}
									else if(matchAllData.getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
												matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0", print_writers);
									}
									else {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
												matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0", print_writers);
									}
								}

							}else if(CricketFunctions.getRequiredRuns(matchAllData) > 0 && matchAllData.getMatch().getInning().get(1).getTotalWickets() >= 10 
									|| matchAllData.getMatch().getInning().get(1).getTotalOvers() >= matchAllData.getSetup().getMaxOvers()) 
							{
								if(matchAllData.getMatch().getMatchStatus() != null) {
									if(matchAllData.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
												"MATCH TIED" + "\0", print_writers);
									}
									else if(matchAllData.getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
												matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0", print_writers);
									}
									else {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
												matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0", print_writers);
									}
								}
							}
							else{
								
								if (CricketFunctions.getRequiredBalls(matchAllData) >= 100) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
											"NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " RUN" + CricketFunctions.Plural(CricketFunctions.
													getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.OverBalls(0,CricketFunctions.getRequiredBalls(matchAllData)) + " OVERS" + "\0", print_writers);
									
									
								}else {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
											"NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " RUN" + CricketFunctions.Plural(CricketFunctions.
													getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.getRequiredBalls(matchAllData) + " BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + "\0", print_writers);
									
								}
								
							}
						}else {
							if(Double.valueOf(matchAllData.getSetup().getTargetOvers()) == 1 && matchAllData.getSetup().getTargetRuns() == 0) {
								if(CricketFunctions.getRequiredRuns(matchAllData) == 0) {
									if(matchAllData.getMatch().getMatchStatus() != null) {
										if(matchAllData.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													"MATCH TIED" + "\0", print_writers);
										}
										else if(matchAllData.getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0", print_writers);
										}
										else {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0", print_writers);
										}
									}

								}else if(CricketFunctions.getRequiredRuns(matchAllData) > 0 && matchAllData.getMatch().getInning().get(1).getTotalWickets() >= 10 
										|| matchAllData.getMatch().getInning().get(1).getTotalOvers() >= matchAllData.getSetup().getMaxOvers()) 
								{
									if(matchAllData.getMatch().getMatchStatus() != null) {
										if(matchAllData.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													"MATCH TIED" + "\0", print_writers);
										}
										else if(matchAllData.getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0", print_writers);
										}
										else {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0", print_writers);
										}
									}
								}
								else{
									
									if (CricketFunctions.getRequiredBalls(matchAllData) >= 100) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
												"NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " RUN" + CricketFunctions.Plural(CricketFunctions.
														getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.OverBalls(0,CricketFunctions.getRequiredBalls(matchAllData)) + 
												"OVERS" + "\0", print_writers);
									}else {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
												"NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " RUN" + CricketFunctions.Plural(CricketFunctions.
														getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.getRequiredBalls(matchAllData) + 
												"BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + "\0", print_writers);
									}
								}
							}else if(Double.valueOf(matchAllData.getSetup().getTargetOvers()) != 0 && matchAllData.getSetup().getTargetRuns() == 0) {
								if(CricketFunctions.getRequiredRuns(matchAllData) == 0) {
									if(matchAllData.getMatch().getMatchStatus() != null) {
										if(matchAllData.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													"MATCH TIED" + "\0", print_writers);
										}
										else if(matchAllData.getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0", print_writers);
										}
										else {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0", print_writers);
										}
									}

								}else if(CricketFunctions.getRequiredRuns(matchAllData) > 0 && matchAllData.getMatch().getInning().get(1).getTotalWickets() >= 10 
										|| matchAllData.getMatch().getInning().get(1).getTotalOvers() >= matchAllData.getSetup().getMaxOvers()) 
								{
									if(matchAllData.getMatch().getMatchStatus() != null) {
										if(matchAllData.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													"MATCH TIED" + "\0", print_writers);
										}
										else if(matchAllData.getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0", print_writers);
										}
										else {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0", print_writers);
										}
									}
								}
								else{
									
									if (CricketFunctions.getRequiredBalls(matchAllData) >= 100) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
												"NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " RUN" + CricketFunctions.Plural(CricketFunctions.
														getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.OverBalls(0,CricketFunctions.getRequiredBalls(matchAllData)) + 
												"OVERS" + "\0", print_writers);
									}else {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
												"NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " RUN" + CricketFunctions.Plural(CricketFunctions.
														getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.getRequiredBalls(matchAllData) + 
												"BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + "\0", print_writers);
									}
								}
							}
							else {
								if((matchAllData.getSetup().getTargetRuns() - matchAllData.getMatch().getInning().get(1).getTotalRuns()) == 0) {
									if(matchAllData.getMatch().getMatchResult() != null) {
										if(matchAllData.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													"MATCH TIED" + "\0", print_writers);
										}
										else if(matchAllData.getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0", print_writers);
										}
										else {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0", print_writers);
										}
									}
									
								}else if((matchAllData.getSetup().getTargetRuns() - matchAllData.getMatch().getInning().get(1).getTotalRuns()) > 0 && matchAllData.getMatch().getInning().get(1).getTotalWickets() >= 10 
										|| Double.valueOf(CricketFunctions.OverBalls(matchAllData.getMatch().getInning().get(1).getTotalOvers(), matchAllData.getMatch().getInning().get(1).getTotalBalls())) 
										>= Double.valueOf(matchAllData.getSetup().getTargetOvers())) 
								{
									if(matchAllData.getMatch().getMatchStatus() != null) {
										if(matchAllData.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													"MATCH TIED" + "\0", print_writers);
										}
										else if(matchAllData.getMatch().getMatchResult().split(",")[1].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0", print_writers);
										}
										else {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0", print_writers);
										}
									}
								}
								else{
									if(matchAllData.getSetup().getTargetType() != null && !matchAllData.getSetup().getTargetType().isEmpty()) {
										
										if (CricketFunctions.getRequiredBalls(matchAllData) >= 100) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													"NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " RUN" + CricketFunctions.Plural(CricketFunctions.
															getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.OverBalls(0,CricketFunctions.getRequiredBalls(matchAllData)) + 
													"OVERS" + " (" + matchAllData.getSetup().getTargetType().toUpperCase() + ")" + "\0", print_writers);
										}else {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													"NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " RUN" + CricketFunctions.Plural(CricketFunctions.
															getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.getRequiredBalls(matchAllData) + 
													"BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + 
													" (" + matchAllData.getSetup().getTargetType().toUpperCase() + ")"+ "\0", print_writers);
										}
									}
									else {
										
										if (CricketFunctions.getRequiredBalls(matchAllData) >= 100) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													"NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " RUN" + CricketFunctions.Plural(CricketFunctions.
															getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.OverBalls(0,CricketFunctions.getRequiredBalls(matchAllData)) + 
													"OVERS" + "\0", print_writers);
										}else {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
													"NEED " + CricketFunctions.getRequiredRuns(matchAllData) + " RUN" + CricketFunctions.Plural(CricketFunctions.
															getRequiredRuns(matchAllData)).toUpperCase() + " FROM " + CricketFunctions.getRequiredBalls(matchAllData) + 
													"BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + "\0", print_writers);
										}
									}
								}
							}
						}
					}
				}
				break;
				
			case "BOWLING_TEAM_TARGET":
				inning = matchAllData.getMatch().getInning().stream().filter(in->in.getIsCurrentInning()
						.equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 11 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
						inning.getBowling_team().getTeamName1()+" NEED "+(10-inning.getTotalWickets())+" WICKET"
						+CricketFunctions.Plural((10-inning.getTotalWickets())).toUpperCase()+" TO WIN " + "\0", print_writers);
				
					
				break;
				
			case "CURRENT_SESSION":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 4 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type$Analytics_2_Wide$Select_Amount*FUNCTION*Omo*vis_con SET 2 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Name_1"
						+ "*GEOM*TEXT SET " + "THIS" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_V"
						+ "*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Name_2"
						+ "*GEOM*TEXT SET " + "SESSION" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Subtitle"
						+ "*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Bottom$4_Stats$Stat_1$Data$txt_Desig"
						+ "*GEOM*TEXT SET " + "OVERS" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Bottom$4_Stats$Stat_2$Data$txt_Desig"
						+ "*GEOM*TEXT SET " + "RUNS" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Bottom$4_Stats$Stat_3$Data$txt_Desig"
						+ "*GEOM*TEXT SET " + "WICKETS" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Bottom$4_Stats$Stat_4$Data$txt_Desig"
						+ "*GEOM*TEXT SET " + "RUN RATE" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Bottom$4_Stats$Stat_1$Data$txt_Fig"
						+ "*GEOM*TEXT SET " + CricketFunctions.OverBalls(0, matchAllData.getMatch().getDaysSessions().get(matchAllData.getMatch().getDaysSessions().size()-1).getTotalBalls()) + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Bottom$4_Stats$Stat_2$Data$txt_Fig"
						+ "*GEOM*TEXT SET " + matchAllData.getMatch().getDaysSessions().get(matchAllData.getMatch().getDaysSessions().size()-1).getTotalRuns()  + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Bottom$4_Stats$Stat_3$Data$txt_Fig"
						+ "*GEOM*TEXT SET " + matchAllData.getMatch().getDaysSessions().get(matchAllData.getMatch().getDaysSessions().size()-1).getTotalWickets() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Bottom$4_Stats$Stat_4$Data$txt_Fig"
						+ "*GEOM*TEXT SET " + CricketFunctions.generateRunRate(matchAllData.getMatch().getDaysSessions().get(matchAllData.getMatch().getDaysSessions().size()-1).
						getTotalRuns(), 0, matchAllData.getMatch().getDaysSessions().get(matchAllData.getMatch().getDaysSessions().size()-1).getTotalBalls(), 2, matchAllData) + "\0", print_writers);
				
				break;
			case "DAY_PLAY":
				int daynumber=0, dayruns=0, daywickets=0;
				int dayballs=0;
				
				DecimalFormat dft = new DecimalFormat("0.00");
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 4 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type$Analytics_2_Wide$Select_Amount*FUNCTION*Omo*vis_con SET 3 \0",print_writers);
				
				daynumber = matchAllData.getMatch().getDaysSessions().get(matchAllData.getMatch().getDaysSessions().size()-1).getDayNumber();
				for(DaySession day_session : matchAllData.getMatch().getDaysSessions()) {
					if(daynumber == day_session.getDayNumber()) {
						dayruns = dayruns + day_session.getTotalRuns();
						dayballs = dayballs + day_session.getTotalBalls();
						daywickets = daywickets + day_session.getTotalWickets();
					}
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Name_1"
						+ "*GEOM*TEXT SET " + "TODAY'S" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_V"
						+ "*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Name_2"
						+ "*GEOM*TEXT SET " + "PLAY" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Subtitle"
						+ "*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Bottom$5_Stats$Stat_1$Data$txt_Desig"
						+ "*GEOM*TEXT SET " + "DAY" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Bottom$5_Stats$Stat_2$Data$txt_Desig"
						+ "*GEOM*TEXT SET " + "OVERS" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Bottom$5_Stats$Stat_3$Data$txt_Desig"
						+ "*GEOM*TEXT SET " + "RUNS" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Bottom$5_Stats$Stat_4$Data$txt_Desig"
						+ "*GEOM*TEXT SET " + "WICKETS" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Bottom$5_Stats$Stat_5$Data$txt_Desig"
						+ "*GEOM*TEXT SET " + "RUN RATE" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Bottom$5_Stats$Stat_1$Data$txt_Fig"
						+ "*GEOM*TEXT SET " + daynumber + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Bottom$5_Stats$Stat_2$Data$txt_Fig"
						+ "*GEOM*TEXT SET " + CricketFunctions.OverBalls(0, dayballs)  + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Bottom$5_Stats$Stat_3$Data$txt_Fig"
						+ "*GEOM*TEXT SET " + dayruns + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Bottom$5_Stats$Stat_4$Data$txt_Fig"
						+ "*GEOM*TEXT SET " + daywickets + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Bottom$5_Stats$Stat_5$Data$txt_Fig"
						+ "*GEOM*TEXT SET " + CricketFunctions.generateRunRate(dayruns, 0, dayballs, 2, matchAllData) + "\0", print_writers);
				break;
				
			case "RESULTS":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 3 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Top*GEOM*TEXT SET " + 
						CricketFunctions.generateMatchSummaryStatus(2, matchAllData, CricketUtil.FULL, "|", config.getBroadcaster()).toUpperCase().split("\\|")[0] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Bottom*GEOM*TEXT SET " + 
						CricketFunctions.generateMatchSummaryStatus(2, matchAllData, CricketUtil.FULL, "|", config.getBroadcaster()).toUpperCase().split("\\|")[1] + "\0", print_writers);
				
				break;
			
			case "COMMENTATORS":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 3 \0",print_writers);
				
				if(Integer.valueOf(Comms_Name.split(",")[4]) > 0 && Integer.valueOf(Comms_Name.split(",")[3]) > 0 
						&& Integer.valueOf(Comms_Name.split(",")[2]) > 0) {
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Top*GEOM*TEXT SET " + 
							"COMMENTATORS" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Bottom*GEOM*TEXT SET " 
							+ Commentators.get(Integer.valueOf(Comms_Name.split(",")[2])-1).getCommentatorName() + ", " 
							+ Commentators.get(Integer.valueOf(Comms_Name.split(",")[3])-1).getCommentatorName() + " & " 
							+ Commentators.get(Integer.valueOf(Comms_Name.split(",")[4])-1).getCommentatorName() + "\0", print_writers);
					
				}else if(Integer.valueOf(Comms_Name.split(",")[4]) == 0 && Integer.valueOf(Comms_Name.split(",")[3]) > 0 
						&& Integer.valueOf(Comms_Name.split(",")[2]) > 0) {
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Top*GEOM*TEXT SET " + 
							"COMMENTATORS" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Bottom*GEOM*TEXT SET " + 
							Commentators.get(Integer.valueOf(Comms_Name.split(",")[2])-1).getCommentatorName() + " & " + 
							Commentators.get(Integer.valueOf(Comms_Name.split(",")[3])-1).getCommentatorName() + "\0", print_writers);
					
				}else if(Integer.valueOf(Comms_Name.split(",")[4]) == 0 && Integer.valueOf(Comms_Name.split(",")[3]) == 0 
						&& Integer.valueOf(Comms_Name.split(",")[2]) > 0) {
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Top*GEOM*TEXT SET " + 
							"COMMENTATOR" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Bottom*GEOM*TEXT SET " + 
							Commentators.get(Integer.valueOf(Comms_Name.split(",")[2])-1).getCommentatorName() + "\0", print_writers);
				}
				break;
				
			case "FREE_TEXT":
				infoBarStats = infobarStats.stream().filter(infostats -> infostats.getOrder() == infobarStatsId).findAny().orElse(null);
				if(infoBarStats == null) {
					return "InfoBarFreeText: Stats  not found for [" + infobarStatsId + "]";
				}
				
				if(infoBarStats.getText2() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Select_Type*FUNCTION*Omo*vis_con SET 3 \0",print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Top*GEOM*TEXT SET " + 
							infoBarStats.getText1() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Bottom*GEOM*TEXT SET " + 
							infoBarStats.getText2() + "\0", print_writers);
				}
				else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Select_Type*FUNCTION*Omo*vis_con SET 11 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
							infoBarStats.getText1() + "\0", print_writers);
				}
				break;
			case "CRICTOS":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 23 \0",print_writers);
				
				if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Crictos$Sponsor*ACTIVE SET 0 \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Crictos$Sponsor*ACTIVE SET 1 \0", print_writers);
				break;
				
			case "ARAMCO_POTD":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 24 \0",print_writers);
				
				if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Aramco$Sponsor*ACTIVE SET 0 \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Aramco$Sponsor*ACTIVE SET 1 \0", print_writers);
				
				break;
				
			case "IDENT_TEAM": case "IDENT_TOURNAMENT":
			
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
					+ "$Select_Type*FUNCTION*Omo*vis_con SET 10 \0",print_writers);
				
				switch (infobar.getMiddle_section().toUpperCase()) {
				case "IDENT_TEAM": 
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic$txt_Top*GEOM*TEXT SET " + 
							matchAllData.getSetup().getHomeTeam().getTeamName1() + " v " + matchAllData.getSetup().getAwayTeam().getTeamName1() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic$txt_Bottom*GEOM*TEXT SET " + 
							"LIVE FROM " + matchAllData.getSetup().getVenueName() + "\0", print_writers);
					break;
				case "IDENT_TOURNAMENT":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic$txt_Top*GEOM*TEXT SET " + 
							matchAllData.getSetup().getTournament() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic$txt_Bottom*GEOM*TEXT SET " + 
							matchAllData.getSetup().getMatchIdent() + "\0", print_writers);
					break;

				}
				break;
				
			case CricketUtil.BATSMAN:
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
					+ "$Select_Type*FUNCTION*Omo*vis_con SET 0 \0",print_writers);
				
				infobar.setBatsmanAndBowlOrSponsor("$Bat_and_Bowl");
				this.infobar.setRight_section(CricketUtil.BOWLER);
				this.infobar.setRight_bottom("BOWLING_END");
				
				populateCurrentBatsmen(print_writers, matchAllData, WhichSide);
				populateVizInfobarBowler(print_writers, matchAllData, WhichSide);
				break;
			
			case "BATSMAN_TOURNAMENT":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 22 \0",print_writers);
					
					infobar.setBatsmanAndBowlOrSponsor("$Bat_and_Sponsor");
					populateCurrentBatsmen(print_writers, matchAllData, WhichSide);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Bat_and_Sponsor$Select*FUNCTION*Omo*vis_con SET 0 \0",print_writers);
				
				break;
			case "BATSMAN_SPONSOR":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 22 \0",print_writers);
					
					infobar.setBatsmanAndBowlOrSponsor("$Bat_and_Sponsor");
					populateCurrentBatsmen(print_writers, matchAllData, WhichSide);
					
					
					if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
						CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
								+ "$Bat_and_Sponsor$Select*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Bat_and_Sponsor$Select*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Bat_and_Sponsor$Select$Sponsor$Select_Sponsor*FUNCTION*Omo*vis_con SET " + sponsor_omo + " \0",print_writers);
				
				break;
				
			case "DLS_PAR_SCORE":
				
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				if(inning == null) {
					return "populateVizInfobarMiddleSection: Inning is Not Found";
				}
				this_data_str = new ArrayList<String>();
				
				if(dls == null) {
					return "populateVizInfobarMiddleSection: DLS is NULL";
				}
				
				for(int i = 0; i<= dls.size() -1;i++) {
					if(dls.get(i).getOver_left().split("\\.")[0].equalsIgnoreCase(String.valueOf(inning.getTotalOvers()))) {
						for(int j=0;j<6;j++) {
							if(inning.getTotalBalls() == j) {
								this_data_str.add(CricketFunctions.populateDuckWorthLewis(matchAllData).get(i+j).getWkts_down());
								break;
							}
						}
						break;
					}
				}
				
				if(CricketFunctions.populateDls(matchAllData, CricketUtil.FULL, Integer.valueOf(this_data_str.get(0))).trim().isEmpty()) {
					return "populateVizInfobarMiddleSection: populateDls Function is Empty";
				}
				
				this_data_str.add(CricketFunctions.populateDls(matchAllData, CricketUtil.FULL, Integer.valueOf(this_data_str.get(0))));
				
				if(this_data_str == null) {
					return "populateVizInfobarMiddleSection this_data_str is null";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 3 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Top"
						+ "*GEOM*TEXT SET " + "DLS PAR SCORE AFTER " + CricketFunctions.OverBalls(inning.getTotalOvers(), inning.getTotalBalls()) + " OVERS - " 
						+ this_data_str.get(0) + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Bottom*GEOM*TEXT SET " + 
						this_data_str.get(1).toUpperCase() + "\0", print_writers);
				break;
				
			case "TARGET":
				
				if(!matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)&&
						!matchAllData.getMatch().getInning().get(3).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					return "populateVizInfobarMiddleSection: Target available in 2nd inning only";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
					+ "$Select_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Team*GEOM*TEXT SET " + 
						inning.getBatting_team().getTeamName1() + "\n NEED" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Runs_Value*GEOM*TEXT SET " + 
						CricketFunctions.getTargetRuns(matchAllData) + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_From*GEOM*TEXT SET " + 
						"MORE RUNS\nFROM" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Balls*GEOM*TEXT SET " + 
						"BALLS" + "\0", print_writers);
				
				if(matchAllData.getSetup().getTargetOvers() != null && !matchAllData.getSetup().getTargetOvers().trim().isEmpty()) {
					if(matchAllData.getSetup().getTargetOvers().contains(".")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Balls_Value"
							+ "*GEOM*TEXT SET " + ((Integer.valueOf(matchAllData.getSetup().getTargetOvers().split(".")[0]) * 6) 
								+ Integer.valueOf(matchAllData.getSetup().getTargetOvers().split(".")[1])) + "\0", print_writers);
					} else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Balls_Value*GEOM*TEXT SET " + 
								((Integer.valueOf(matchAllData.getSetup().getTargetOvers()) * 6)) + "\0", print_writers);
					}
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Balls_Value*GEOM*TEXT SET " + 
							(matchAllData.getSetup().getMaxOvers()*6) + "\0", print_writers);
				}
				break;
				
			case "EQUATION":
				
				if(!matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)&&
						!matchAllData.getMatch().getInning().get(3).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					return "populateVizInfobarMiddleSection: Equation available in 2nd inning only";
				}

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
					+ "$Select_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Team*GEOM*TEXT SET " + 
						inning.getBatting_team().getTeamName1() + "\n NEED" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Runs_Value*GEOM*TEXT SET " + 
						CricketFunctions.getRequiredRuns(matchAllData) + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Balls_Value*GEOM*TEXT SET " + 
						CricketFunctions.getRequiredBalls(matchAllData) + "\0", print_writers);
				
				if(CricketFunctions.getRequiredRuns(matchAllData) <= 1) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_From*GEOM*TEXT SET " + 
							" RUN" + CricketFunctions.Plural(CricketFunctions.getRequiredRuns(matchAllData)).toUpperCase() + "\nFROM" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_From*GEOM*TEXT SET " + 
							"MORE RUN" + CricketFunctions.Plural(CricketFunctions.getRequiredRuns(matchAllData)).toUpperCase() + "\nFROM" + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Balls*GEOM*TEXT SET " + 
						"BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + "\0", print_writers);
				break;
				
			case "FOW":
				
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				if(inning == null) {
					return "populateVizInfobarMiddleSection: Inning returned is NULL";
				}
				if(inning.getFallsOfWickets() == null) {
					return "populateVizInfobarMiddleSection : Fall Of Wickets is NULL";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 9 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stat$txt_Title"
						+ "*GEOM*TEXT SET " + "FALL \n OF WICKETS" + "\0", print_writers);
				
				if(inning.getFallsOfWickets().size() <= 8) {
					for(int i = 1; i <= inning.getFallsOfWickets().size(); i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Stats_Wide$Stats$Stat_" + i + "*ACTIVE SET 1 \0",print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_"
								+ i + "$txt_Desig*GEOM*TEXT SET " + inning.getFallsOfWickets().get(i-1).getFowNumber() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_"
								+ i + "$txt_Fig*GEOM*TEXT SET " + inning.getFallsOfWickets().get(i-1).getFowRuns() + "\0", print_writers);
					}
					
					for(int i = inning.getFallsOfWickets().size() + 1; i <= 8; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Stats_Wide$Stats$Stat_" + i + "*ACTIVE SET 0 \0",print_writers);
					}
				}
				TimeUnit.MILLISECONDS.sleep(1000);
				break;
				
			case "LAST_X_BALLS":
				
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				if(inning == null) {
					return "populateVizInfobarMiddleSection: Inning returned is NULL";
				}
				//System.out.println(lastXballs);
				this_data_str = new ArrayList<String>();
				this_data_str.add(CricketFunctions.getlastthirtyballsdata(matchAllData, slashOrDash, matchAllData.getEventFile().getEvents(), lastXballs));
				
				if(this_data_str.get(this_data_str.size()-1) == null || this_data_str.get(this_data_str.size()-1).split(slashOrDash).length > 4) {
					return "populateVizInfobarMiddleSection: Last " + lastXballs + " Balls data returned invalid";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 9 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stat$txt_Title*GEOM*TEXT SET " + 
						 "LAST\n" + lastXballs + " BALLS" + "\0", print_writers);
				
				for(int i = 1; i <= 4; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Stats_Wide$Stats$Stat_" + i + "*ACTIVE SET 1 \0",print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_1$txt_Desig*GEOM*TEXT SET " + 
						"RUN" + CricketFunctions.Plural(Integer.valueOf(this_data_str.get(this_data_str.size()-1).split(slashOrDash)[0])).toUpperCase() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_1$txt_Fig*GEOM*TEXT SET " + 
						this_data_str.get(this_data_str.size()-1).split(slashOrDash)[0] + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_2$txt_Desig*GEOM*TEXT SET " + 
						"WICKET" + CricketFunctions.Plural(Integer.valueOf(this_data_str.get(this_data_str.size()-1).split(slashOrDash)[1])).toUpperCase() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_2$txt_Fig*GEOM*TEXT SET " + 
						this_data_str.get(this_data_str.size()-1).split(slashOrDash)[1] + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_3$txt_Desig*GEOM*TEXT SET " + 
						"FOUR" + CricketFunctions.Plural(Integer.valueOf(this_data_str.get(this_data_str.size()-1).split(slashOrDash)[2])).toUpperCase() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_3$txt_Fig*GEOM*TEXT SET " + 
						this_data_str.get(this_data_str.size()-1).split(slashOrDash)[2] + "\0", print_writers);
				
				
				if(Integer.valueOf(this_data_str.get(this_data_str.size()-1).split(slashOrDash)[3]) == 1){
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_4$txt_Desig*GEOM*TEXT SET " + 
							"SIX" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_4$txt_Desig*GEOM*TEXT SET " + 
							"SIXES" + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_4$txt_Fig*GEOM*TEXT SET " + 
						this_data_str.get(this_data_str.size()-1).split(slashOrDash)[3] + "\0", print_writers);
				
				for(int i = 5; i <= 8; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Stats_Wide$Stats$Stat_" + i + "*ACTIVE SET 0 \0",print_writers);
				}
				TimeUnit.MILLISECONDS.sleep(1000);
				break;

			case CricketUtil.EXTRAS:
			
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				if(inning == null) {
					return "populateVizInfobarMiddleSection: Inning returned is NULL";
				}
				
				for(int i = 1; i <= 4; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Stats_Wide$Stats$Stat_" + i + "*ACTIVE SET 1 \0",print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
					+ "$Select_Type*FUNCTION*Omo*vis_con SET 9 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stat$txt_Title*GEOM*TEXT SET " + 
						CricketUtil.EXTRAS + "\n" + inning.getTotalExtras() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_1$txt_Desig*GEOM*TEXT SET " + 
						"WIDES" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_1$txt_Fig*GEOM*TEXT SET " + 
						inning.getTotalWides() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_2$txt_Desig*GEOM*TEXT SET " + 
						"NO BALLS" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_2$txt_Fig*GEOM*TEXT SET " + 
						inning.getTotalNoBalls() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_3$txt_Desig*GEOM*TEXT SET " + 
						"LEG BYES" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_3$txt_Fig*GEOM*TEXT SET " + 
						inning.getTotalLegByes() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_4$txt_Desig*GEOM*TEXT SET " + 
						"BYES" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_4$txt_Fig*GEOM*TEXT SET " + 
						inning.getTotalByes() + "\0", print_writers);
				
				if(inning.getTotalPenalties() > 0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Stats_Wide$Stats$Stat_5*ACTIVE SET 1 \0",print_writers);
					if(inning.getTotalPenalties() == 1) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Stats_Wide$Stats$Stat_5$txt_Desig*GEOM*TEXT SET PENALTY\0", print_writers);
					} else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Stats_Wide$Stats$Stat_5$txt_Desig*GEOM*TEXT SET PENALTIES\0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Stats_Wide$Stats$Stat_5$txt_Fig*GEOM*TEXT SET " + inning.getTotalPenalties() + "\0", print_writers);
				} else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Stats_Wide$Stats$Stat_5*ACTIVE SET 0 \0",print_writers);
				}
				
				//Hide unused stats
				for(int i = 6; i <= 8; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Stats_Wide$Stats$Stat_" + i + "*ACTIVE SET 0 \0",print_writers);
				}
				TimeUnit.MILLISECONDS.sleep(1000);
				break;
			case "BALLS_SINCE_LAST_BOUNDARY":
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
					inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
				if(inning == null) {
					return "populateVizInfobarMiddleSection: Inning returned is NULL";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 7 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$QuickScore_Wide$txt_Name*GEOM*TEXT SET " + 
						"BALLS" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$QuickScore_Wide$txt_Subtitle*GEOM*TEXT SET " + 
						"SINCE LAST BOUNDARY" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$QuickScore_Wide$txt_Stat*GEOM*TEXT SET " + 
						CricketFunctions.lastFewOversData(CricketUtil.BOUNDARY, matchAllData.getEventFile().getEvents(), inning.getInningNumber()) + "\0", print_writers);
				
				break;
				
			case "BAT_PROFILE_CAREER": case "BALL_PROFILE_CAREER":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 4 \0",print_writers);
				
				if(FirstPlayerId <= 0 || WhichProfile == null) {
					return "InfoBarPlayerProfile: Player Id NOT found [" + FirstPlayerId + "]";
				}
				
				stat = statistics.stream().filter(st -> st.getPlayer_id() == FirstPlayerId).findAny().orElse(null);
				if(stat == null) {
					return "InfoBarPlayerProfile: Stats not found for Player Id [" + FirstPlayerId + "]";
				}
				
				statsType = statsTypes.stream().filter(st -> st.getStats_short_name().equalsIgnoreCase(WhichProfile)).findAny().orElse(null);
				if(statsType == null) {
					return "InfoBarPlayerProfile: Stats Type not found for profile [" + WhichProfile + "]";
				}
				
				player = CricketFunctions.getPlayerFromMatchData(stat.getPlayer_id(), matchAllData); 
				if(player == null) {
					return "InfoBarPlayerProfile: Player Id not found [" + FirstPlayerId + "]";
				}
				
				stat.setStats_type(statsType);
				
				stat = CricketFunctions.updateTournamentDataWithStats(stat, tournament_matches, matchAllData);
				stat = CricketFunctions.updateStatisticsWithMatchData(stat, matchAllData);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Name_1"
						+ "*GEOM*TEXT SET " + player.getFirstname() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_V"
						+ "*GEOM*TEXT SET " + player.getSurname() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Name_2"
						+ "*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				if(statsType.getStats_short_name().equalsIgnoreCase(CricketUtil.DT20)) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Subtitle"
							+ "*GEOM*TEXT SET " + "T20 CAREER" + "\0", print_writers);
				}
				else if(statsType.getStats_short_name().equalsIgnoreCase(CricketUtil.IT20)) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Subtitle"
							+ "*GEOM*TEXT SET " + "T20I CAREER" + "\0", print_writers);
				}
				else if(statsType.getStats_short_name().equalsIgnoreCase("U19ODI")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Subtitle"
							+ "*GEOM*TEXT SET " + "U19 ODI CAREER" + "\0", print_writers);
				}
				else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Subtitle"
							+ "*GEOM*TEXT SET " + statsType.getStats_short_name() + "CAREER" + "\0", print_writers);
				}
				
				switch (infobar.getMiddle_section().toUpperCase()) {
				case "BAT_PROFILE_CAREER":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Select_Amount*FUNCTION*Omo*vis_con SET 2 \0",print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$4_Stats$Stat_1"
							+ "$txt_Desig*GEOM*TEXT SET " + "MATCHES" + "\0", print_writers);	
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$4_Stats$Stat_1"
							+ "$txt_Fig*GEOM*TEXT SET " + stat.getMatches() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$4_Stats$Stat_2"
							+ "$txt_Desig*GEOM*TEXT SET " + "RUNS" + "\0", print_writers);	
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$4_Stats$Stat_2"
							+ "$txt_Fig*GEOM*TEXT SET " + stat.getRuns() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$4_Stats$Stat_3"
							+ "$txt_Desig*GEOM*TEXT SET " + "AVERAGE" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$4_Stats$Stat_3"
							+ "$txt_Fig*GEOM*TEXT SET " + CricketFunctions.getAverage(stat.getMatches(), stat.getNot_out(), stat.getRuns(), 2, slashOrDash) + "\0", 
								print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$4_Stats$Stat_4"
							+ "$txt_Desig*GEOM*TEXT SET " + "50s" + "\0", print_writers);	
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$4_Stats$Stat_4"
							+ "$txt_Fig*GEOM*TEXT SET " + stat.getFifties() + "\0", print_writers);
					break;
				case "BALL_PROFILE_CAREER":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Select_Amount*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$3_Stats$Stat_1"
							+ "$txt_Desig*GEOM*TEXT SET " + "MATCHES" + "\0", print_writers);	
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$3_Stats$Stat_1"
							+ "$txt_Fig*GEOM*TEXT SET " + stat.getMatches() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$3_Stats$Stat_2"
							+ "$txt_Desig*GEOM*TEXT SET " + "WICKETS" + "\0", print_writers);	
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$3_Stats$Stat_2"
							+ "$txt_Fig*GEOM*TEXT SET " + stat.getWickets() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$3_Stats$Stat_3"
							+ "$txt_Desig*GEOM*TEXT SET " + "ECONOMY" + "\0", print_writers);	
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$3_Stats$Stat_3"
							+ "$txt_Fig*GEOM*TEXT SET " + CricketFunctions.getEconomy(stat.getRuns_conceded(), stat.getBalls_bowled(), 2, slashOrDash) + "\0", 
								print_writers);
					
					break;

				}
				TimeUnit.MILLISECONDS.sleep(1000);
				break;
				
			case "LAST_WICKET":

				inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
					inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				
				String how_out_txt = "";
				
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
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
					+ "$Select_Type*FUNCTION*Omo*vis_con SET 18 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
					+ "$Last_Wicket_Wide$Top$txt_Firstname*GEOM*TEXT SET " + battingCardList.get(battingCardList.size()-1).getPlayer().getFirstname() + "\0", print_writers);
				if(battingCardList.get(battingCardList.size()-1).getPlayer().getSurname() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Last_Wicket_Wide$Top$"
						+ "txt_SecondName*GEOM*TEXT SET " + battingCardList.get(battingCardList.size()-1).getPlayer().getSurname() + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Last_Wicket_Wide$Top$"
						+ "txt_SecondName*GEOM*TEXT SET "+ "" + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Last_Wicket_Wide$Top$"
					+ "txt_Score*GEOM*TEXT SET " + battingCardList.get(battingCardList.size()-1).getRuns() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Last_Wicket_Wide$Top$"
					+ "txt_Balls*GEOM*TEXT SET " + battingCardList.get(battingCardList.size()-1).getBalls() + "\0", print_writers);
				
				if(battingCardList.get(battingCardList.size()-1).getHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
					if(battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute() != null && 
							battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
						how_out_txt = "run out " + "sub (" + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + ")";
					} else {
						how_out_txt = "run out (" + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + ")";
					}
				}
				else if(battingCardList.get(battingCardList.size()-1).getHowOut().equalsIgnoreCase(CricketUtil.CAUGHT)) {
					if(battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute() != null && 
							battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
						how_out_txt = "c" +  " sub (" + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + ")  b " + 
								battingCardList.get(battingCardList.size()-1).getHowOutBowler().getTicker_name();
					} else {
						how_out_txt = "c " + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + "  b " + 
								battingCardList.get(battingCardList.size()-1).getHowOutBowler().getTicker_name();
					}
				}else {
					if(!battingCardList.get(battingCardList.size()-1).getHowOutPartOne().isEmpty()) {
						how_out_txt = battingCardList.get(battingCardList.size()-1).getHowOutPartOne();
					}
					
					if(!battingCardList.get(battingCardList.size()-1).getHowOutPartTwo().isEmpty()) {
						if(!how_out_txt.trim().isEmpty()) {
							how_out_txt = how_out_txt + "  " + battingCardList.get(battingCardList.size()-1).getHowOutPartTwo();
						}else {
							how_out_txt = battingCardList.get(battingCardList.size()-1).getHowOutPartTwo();
						}
					}
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
					+ "$Last_Wicket_Wide$txt_HowOut*GEOM*TEXT SET " + how_out_txt + "\0", print_writers);
		
				break;
				
			case CricketUtil.PROJECTED:
				
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == 1 &&
					inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				
				if(inning == null) {
					return "populateVizInfobarMiddleSection: 1st Inning returned is NULL";
				}
				
				this_data_str = CricketFunctions.projectedScore(matchAllData);
				if(this_data_str.size() <= 0) {
					return "populateVizInfobarMiddleSection: Projected score invalid";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
					+ "$Select_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
			    
			    CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
			    	+ "$Projected_Wide$Stat_1$txt_Desig*GEOM*TEXT SET " + "CURRENT (" + this_data_str.get(0) + ")" + "\0", print_writers);
			    CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
			    	+ "$Projected_Wide$Stat_1$txt_Fig*GEOM*TEXT SET " + this_data_str.get(1) + "\0", print_writers);
			    
			    CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
			    	+ "$Projected_Wide$Stat_2$txt_Desig*GEOM*TEXT SET " + this_data_str.get(2) + "/OVER" + "\0", print_writers);
			    CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
			    	+ "$Projected_Wide$Stat_2$txt_Fig*GEOM*TEXT SET " + this_data_str.get(3) + "\0", print_writers);
			    CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
			    	+ "$Projected_Wide$Stat_3$txt_Desig*GEOM*TEXT SET " + this_data_str.get(4) + "/OVER" + "\0", print_writers);
			    CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
			    	+ "$Projected_Wide$Stat_3$txt_Fig*GEOM*TEXT SET " + this_data_str.get(5) + "\0", print_writers);
			    CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
			    	+ "$Projected_Wide$Stat_4$txt_Desig*GEOM*TEXT SET " + this_data_str.get(6) + "/OVER" + "\0", print_writers);
			    CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
			    	+ "$Projected_Wide$Stat_4$txt_Fig*GEOM*TEXT SET " + this_data_str.get(7) + "\0", print_writers);
			    
				break;
				
			case "CURR_PARTNERSHIP":

				inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
					inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
				if(inning == null) {
					return "populateVizInfobarMiddleSection: Inning returned is NULL";
				}
				
				if(inning.getPartnerships() != null && inning.getPartnerships().size() <= 0) {
					return "populateVizInfobarMiddleSection: Partnership size is NULL/Zero";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
					+ "$Select_Type*FUNCTION*Omo*vis_con SET 6 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$txt_Title*GEOM*TEXT SET " + 
						CricketFunctions.ordinal(inning.getPartnerships().get(inning.getPartnerships().size()-1).getPartnershipNumber()) + " WICKET \n PARTNERSHIP" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$Score$txt_Runs*GEOM*TEXT SET " + 
						inning.getPartnerships().get(inning.getPartnerships().size()-1).getTotalRuns() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$Score$txt_Balls*GEOM*TEXT SET " + 
						inning.getPartnerships().get(inning.getPartnerships().size()-1).getTotalBalls() + "\0", print_writers);
				
				
				infobar.setBatsmanAndBowlOrSponsor("$Partnership_Wide");
				populateCurrentBatsmen(print_writers, matchAllData, WhichSide);
				
				TimeUnit.MILLISECONDS.sleep(1000);
				break;
			
			case "THIS_MATCH_SIXES":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 17 \0",print_writers);
				
				this_data_str = new ArrayList<String>();
				
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$txt_Title"
						+ "*GEOM*TEXT SET " + "MATCH SIXES" + "\0", print_writers);
				
				
				if(CricketFunctions.extracttournamentFoursAndSixes("CURRENT_MATCH_DATA",tournament_matches, matchAllData, null).getTournament_sixes() > 99) {
					this_data_str.add(CricketFunctions.hundredsTensUnits(String.valueOf(CricketFunctions.extracttournamentFoursAndSixes("CURRENT_MATCH_DATA", 
							tournament_matches, matchAllData, null).getTournament_sixes())));
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Tournament_Sixes$Side_1$txt_Hundread*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Tournament_Sixes$Side_1$txt_Ten*ACTIVE SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_1$txt_Hundread"
							+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_1$txt_Ten"
							+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[1] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_1$txt_Unit"
							+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[2] + "\0", print_writers);
				}else if(CricketFunctions.extracttournamentFoursAndSixes("CURRENT_MATCH_DATA",tournament_matches, matchAllData, null).getTournament_sixes() > 9) {
					this_data_str.add(CricketFunctions.hundredsTensUnits(String.valueOf(CricketFunctions.extracttournamentFoursAndSixes("CURRENT_MATCH_DATA", 
							tournament_matches, matchAllData, null).getTournament_sixes())));
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Tournament_Sixes$Side_1$txt_Hundread*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Tournament_Sixes$Side_1$txt_Ten*ACTIVE SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_1$txt_Ten"
							+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[1] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_1$txt_Unit"
							+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[2] + "\0", print_writers);
				}else {
					this_data_str.add(CricketFunctions.hundredsTensUnits(String.valueOf(CricketFunctions.extracttournamentFoursAndSixes("CURRENT_MATCH_DATA", 
							tournament_matches, matchAllData, null).getTournament_sixes())));
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Tournament_Sixes$Side_1$txt_Hundread*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Tournament_Sixes$Side_1$txt_Ten*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_1$txt_Unit"
							+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[2] + "\0", print_writers);
				}
				
				if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Tournament_Sixes$Sponsor*ACTIVE SET 0 \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Tournament_Sixes$Sponsor*ACTIVE SET 1 \0", print_writers);
				
				break;
				
			case "TOURNAMENT_SIXES":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 17 \0",print_writers);
				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Sixes", "SHOW 0.0");
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$txt_Title"
						+ "*GEOM*TEXT SET " + "TOURNAMENT SIXES" + "\0", print_writers);
				
				this_data_str = new ArrayList<String>();
				this_data_str.add(CricketFunctions.hundredsTensUnits(previous_sixes));
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_1$txt_Hundread"
						+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[0] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_1$txt_Ten"
						+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[1] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_1$txt_Unit"
						+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[2] + "\0", print_writers);
				
				if(WhichSide == 1) {
					this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_RightInfo", "SHOW 0.0");
					
					String new_sixes_value = String.valueOf(CricketFunctions.extracttournamentFoursAndSixes("COMBINED_PAST_CURRENT_MATCH_DATA", 
							tournament_matches, matchAllData, null).getTournament_sixes());
					
					this_data_str.add(CricketFunctions.hundredsTensUnits(new_sixes_value));
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_2$txt_Hundread"
							+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_2$txt_Ten"
							+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[1] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_2$txt_Unit"
							+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[2] + "\0", print_writers);
					
					if(!this_data_str.get(this_data_str.size()-2).split(",")[0].equalsIgnoreCase(this_data_str.get(this_data_str.size()-1).split(",")[0])) {
						this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Sixes$Hundreds", "START");
						this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Sixes$Tens", "START");
						this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Sixes$Units", "START");
					}
					else if(!this_data_str.get(this_data_str.size()-2).split(",")[1].equalsIgnoreCase(this_data_str.get(this_data_str.size()-1).split(",")[1])) {
						this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Sixes$Tens", "START");
						this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Sixes$Units", "START");
					}
					else if(!this_data_str.get(this_data_str.size()-2).split(",")[2].equalsIgnoreCase(this_data_str.get(this_data_str.size()-1).split(",")[2])) {
						this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Sixes$Units", "START");
					}
					//TimeUnit.MILLISECONDS.sleep(1500);
					
					previous_sixes = new_sixes_value;
					
				}
				
				if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Tournament_Sixes$Sponsor*ACTIVE SET 0 \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Tournament_Sixes$Sponsor*ACTIVE SET 1 \0", print_writers);
				
				break;
				
			case "Analytics_1_Wide": case CricketUtil.RESULT:
				
				switch(infobar.getMiddle_section().toUpperCase()) {
				case CricketUtil.RESULT:
					if(this.infobar.getFreeText() != null && this.infobar.getFreeText().size() > 2) {
						return "populateVizInfobarMiddleSection: Unable to process RESULT";
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 3 \0",print_writers);
					
					if(this.infobar.getFreeText().get(0).contains(CricketUtil.BEAT)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Analytics_1_Wide$txt_Top*GEOM*TEXT SET " + this.infobar.getFreeText().get(0) + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Analytics_1_Wide$txt_Bottom*GEOM*TEXT SET " + this.infobar.getFreeText().get(1) + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
								+ "$Analytics_1_Wide$txt_Top*GEOM*TEXT SET " + this.infobar.getFreeText().get(0) + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Analytics_1_Wide$txt_Bottom*GEOM*TEXT SET " + this.infobar.getFreeText().get(1) + "\0", print_writers);
					}
					break;
				case "Analytics_1_Wide":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Select_Type*FUNCTION*Omo*vis_con SET 3 \0",print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Top*GEOM*TEXT SET " + 
							"DOAD" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Bottom*GEOM*TEXT SET " + 
							"DOAD" + "\0", print_writers);
					break;
				}
				break;
				
			case "Analytics_2_Wide":

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 4 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Name_1*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_V*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Name_2*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Subtitle*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Bottom$5_Stats*ACTIVE SET 1 " + "\0", print_writers);
				
				String cont="$Stat_";
				
				for(int i=1;i<=5;i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$5_Stats$"+cont+i+"$txt_Desig*GEOM*TEXT SET " + 
							"DOAD" + "\0", print_writers);	
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$5_Stats$"+cont+i+"$txt_Fig*GEOM*TEXT SET " + 
							"DOAD" + "\0", print_writers);	
				}
				break;
			case "Analytics_3_Wide":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 5 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_3_Wide$Top$txt_Title*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_3_Wide$Top$txt_Subtitle*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_3_Wide$Left$txt_Desig*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_3_Wide$Left$txt_Fig*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_3_Wide$Title$txt_Desig*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_3_Wide$Right$txt_Desig*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_3_Wide$Right$txt_Fig*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				
				break;
			case "QuickScore_Wide":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 7 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$QuickScore_Wide$txt_Name*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$QuickScore_Wide$txt_Subtitle*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$QuickScore_Wide$txt_Stat*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				break;
				
			case "Basic":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 10 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic$txt_Top*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic$txt_Bottom*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				break;
				
			case "Basic_Wide":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 11 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Basic_Wide$txt_LongString*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				
				break;
				
			case "Player_Balls":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 13 \0",print_writers);
				
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Player_Balls$img_Flag*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() +"\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Player_Balls$flag_texture*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() +"\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Player_Balls$img_Flag*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() +"\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Player_Balls$txt_Firstname*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Player_Balls$txt_SecondName*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Player_Balls$txt_Score*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Player_Balls$txt_Balls*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				 
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Player_Balls$txt_Subtitle*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Player_Balls$Balls*FUNCTION*Omo*vis_con SET 9 \0",print_writers);


				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Player_Balls$Choose_Type*FUNCTION*Omo*vis_con SET 9 \0",print_writers);
				
				
				break;
			
			case "Player_Speeds":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 14 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Player_Speeds$img_Highlight*TEXTURE*IMAGE SET " + "DOAD" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Player_Speeds$Balls*FUNCTION*Omo*vis_con SET 9 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Player_Speeds$txt_Speed*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				
				break;
				
			case "Tweet_Wide":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 15 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Tweet_Wide$txt_LongString*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				break;
				
			case "Team_Tournament_Sixes":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 16 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Team_Tournament_Sixes$Base$img_Highlight*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() +"\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Team_Tournament_Sixes$shadow*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() +"\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Team_Tournament_Sixes$img_Flag*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() +"\0",print_writers);

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Team_Tournament_Sixes$txt_Title*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Team_Tournament_Sixes$Side_"+ WhichSide+"$txt_Hundread*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Team_Tournament_Sixes$Side_"+ WhichSide+"$txt_Ten*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Team_Tournament_Sixes$Side_"+ WhichSide+"$txt_Unit*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				break;
				
			case "Team_Text":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 20 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Team_Text$shadow*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() +"\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Team_Text$img_Flag*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() +"\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Team_Text$txt_LongString*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				
				break;

			}
			infobar.setLast_middle_section(infobar.getMiddle_section());
			break;
			
			
		case Constants.BENGAL_T20: 

			switch(infobar.getMiddle_section().toUpperCase()) {
			
			case CricketUtil.BATSMAN:
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$anim_Infobar$Fade_For_Analytics CONTINUE \0",print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$anim_Infobar$Analytics CONTINUE \0",print_writers);

				infobar.setBatsmanAndBowlOrSponsor("$Bat_and_Bowl");
				this.infobar.setMiddle_section(CricketUtil.BATSMAN);
				
				populateCurrentBatsmen(print_writers, matchAllData, WhichSide);
				
				if(this.infobar.getRight_section() != null && !this.infobar.getRight_section().isEmpty()) {
					populateVizInfobarBowler(print_writers, matchAllData, WhichSide);
				}else {
					this.infobar.setRight_section(CricketUtil.BOWLER);
					this.infobar.setRight_bottom("BOWLING_END");
					
					populateVizInfobarBowler(print_writers, matchAllData, WhichSide);
				}
				break;
			
				
			case "RESULTS":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask1$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask2$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$"
						+ "img_Text1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 3 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Top*GEOM*TEXT SET " + 
						CricketFunctions.generateMatchSummaryStatus(2, matchAllData, CricketUtil.FULL, "|", config.getBroadcaster()).toUpperCase().split("\\|")[0] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Bottom*GEOM*TEXT SET " + 
						CricketFunctions.generateMatchSummaryStatus(2, matchAllData, CricketUtil.FULL, "|", config.getBroadcaster()).toUpperCase().split("\\|")[1] + "\0", print_writers);
				
				break;
			
			case "COMMENTATORS":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask1$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask2$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$"
						+ "img_Text1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type$2_Line_Text$Select_Sponsor*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$Select_Type$2_Line_Text$Maxsize_Title$txt_Title*GEOM*TEXT SET " 
						+ "COMMENTATORS" + "\0", print_writers);
				
				if(Integer.valueOf(Comms_Name.split(",")[4]) > 0 && Integer.valueOf(Comms_Name.split(",")[3]) > 0 
						&& Integer.valueOf(Comms_Name.split(",")[2]) > 0) {
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$Select_Type$2_Line_Text$Maxsize_Subtitle$txt_Subtitle*GEOM*TEXT SET " 
							+ Commentators.get(Integer.valueOf(Comms_Name.split(",")[2])-1).getCommentatorName() + ", " 
							+ Commentators.get(Integer.valueOf(Comms_Name.split(",")[3])-1).getCommentatorName() + " & " 
							+ Commentators.get(Integer.valueOf(Comms_Name.split(",")[4])-1).getCommentatorName() + "\0", print_writers);
					
				}else if(Integer.valueOf(Comms_Name.split(",")[4]) == 0 && Integer.valueOf(Comms_Name.split(",")[3]) > 0 
						&& Integer.valueOf(Comms_Name.split(",")[2]) > 0) {
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$Select_Type$2_Line_Text$Maxsize_Subtitle$txt_Subtitle*GEOM*TEXT SET " 
							+ Commentators.get(Integer.valueOf(Comms_Name.split(",")[2])-1).getCommentatorName() + " & " + 
							Commentators.get(Integer.valueOf(Comms_Name.split(",")[3])-1).getCommentatorName() + "\0", print_writers);
					
				}else if(Integer.valueOf(Comms_Name.split(",")[4]) == 0 && Integer.valueOf(Comms_Name.split(",")[3]) == 0 
						&& Integer.valueOf(Comms_Name.split(",")[2]) > 0) {
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$Select_Type$2_Line_Text$Maxsize_Subtitle$txt_Subtitle*GEOM*TEXT SET " 
							+ Commentators.get(Integer.valueOf(Comms_Name.split(",")[2])-1).getCommentatorName() + "\0", print_writers);
				}
				break;
				
			case "FREE_TEXT":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask1$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask2$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$"
						+ "img_Text1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				infoBarStats = infobarStats.stream().filter(infostats -> infostats.getOrder() == infobarStatsId).findAny().orElse(null);
				if(infoBarStats == null) {
					return "InfoBarFreeText: Stats  not found for [" + infobarStatsId + "]";
				}
				
				if(infoBarStats.getText2() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
							+ "$Select_Type$2_Line_Text$Select_Sponsor*FUNCTION*Omo*vis_con SET 0 \0",print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$Select_Type$2_Line_Text$Maxsize_Title$txt_Title*GEOM*TEXT SET " 
							+ infoBarStats.getText1() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$Select_Type$2_Line_Text$Maxsize_Subtitle$txt_Subtitle*GEOM*TEXT SET " 
							+ infoBarStats.getText2() + "\0", print_writers);
				}
				else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
							+ "$Select_Type*FUNCTION*Omo*vis_con SET 0 \0",print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$Select_Type$1_Line_Text$txt_Title*GEOM*TEXT SET " 
							+ infoBarStats.getText1() + "\0", print_writers);
				}
				break;
				
			case "IDENT_TEAM": case "IDENT_TOURNAMENT":
			
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type$2_Line_Text$Select_Sponsor*FUNCTION*Omo*vis_con SET 0 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask1$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask2$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$"
						+ "img_Text1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				switch (infobar.getMiddle_section().toUpperCase()) {
				case "IDENT_TEAM": 
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$Select_Type$2_Line_Text$Maxsize_Title$txt_Title*GEOM*TEXT SET " 
							+ matchAllData.getSetup().getHomeTeam().getTeamName1() + " v " + matchAllData.getSetup().getAwayTeam().getTeamName1() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$Select_Type$2_Line_Text$Maxsize_Subtitle$txt_Subtitle*GEOM*TEXT SET " 
							+ "LIVE FROM " + matchAllData.getSetup().getVenueName() + "\0", print_writers);
					break;
				case "IDENT_TOURNAMENT":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$Select_Type$2_Line_Text$Maxsize_Title$txt_Title*GEOM*TEXT SET " 
							+ matchAllData.getSetup().getTournament() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$Select_Type$2_Line_Text$Maxsize_Subtitle$txt_Subtitle*GEOM*TEXT SET " 
							+ matchAllData.getSetup().getMatchIdent() + "\0", print_writers);
					break;

				}
				break;
			
			case "BATSMAN_TOURNAMENT":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask1$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask2$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$"
						+ "img_Text1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 22 \0",print_writers);
					
					infobar.setBatsmanAndBowlOrSponsor("$Bat_and_Sponsor");
					populateCurrentBatsmen(print_writers, matchAllData, WhichSide);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Bat_and_Sponsor$Select*FUNCTION*Omo*vis_con SET 0 \0",print_writers);
				
				break;
				
			case "TARGET":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask1$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask2$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$"
						+ "img_Text1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				if(!matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)&&
						!matchAllData.getMatch().getInning().get(3).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					return "populateVizInfobarMiddleSection: Target available in 2nd inning only";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
					+ "$Select_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Team*GEOM*TEXT SET " + 
						inning.getBatting_team().getTeamName1() + "\n NEED" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Runs_Value*GEOM*TEXT SET " + 
						CricketFunctions.getTargetRuns(matchAllData) + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_From*GEOM*TEXT SET " + 
						"MORE RUNS\nFROM" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Balls*GEOM*TEXT SET " + 
						"BALLS" + "\0", print_writers);
				
				if(matchAllData.getSetup().getTargetOvers() != null && !matchAllData.getSetup().getTargetOvers().trim().isEmpty()) {
					if(matchAllData.getSetup().getTargetOvers().contains(".")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Balls_Value"
							+ "*GEOM*TEXT SET " + ((Integer.valueOf(matchAllData.getSetup().getTargetOvers().split(".")[0]) * 6) 
								+ Integer.valueOf(matchAllData.getSetup().getTargetOvers().split(".")[1])) + "\0", print_writers);
					} else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Balls_Value*GEOM*TEXT SET " + 
								((Integer.valueOf(matchAllData.getSetup().getTargetOvers()) * 6)) + "\0", print_writers);
					}
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Balls_Value*GEOM*TEXT SET " + 
							(matchAllData.getSetup().getMaxOvers()*6) + "\0", print_writers);
				}
				break;
				
			case "EQUATION":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask1$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask2$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$img_Text1$"
						+ "Equation$Top_Line$Stat_1$img_This_Over*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_OVER_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$img_Text1$"
						+ "Equation$Top_Line$Stat_2$img_This_Over*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_OVER_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$"
						+ "img_Text1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				if(!matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)&&
						!matchAllData.getMatch().getInning().get(3).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					return "populateVizInfobarMiddleSection: Equation available in 2nd inning only";
				}

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 3 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type$Equation$Top_Line$Stat_1$txt_Runs*GEOM*TEXT SET " + 
						CricketFunctions.getRequiredRuns(matchAllData) + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type$Equation$Top_Line$Stat_2$txt_Balls*GEOM*TEXT SET " + 
						CricketFunctions.getRequiredBalls(matchAllData) + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type$Equation$Top_Line$Stat_1$txt_Title*GEOM*TEXT SET " + 
						" RUN" + CricketFunctions.Plural(CricketFunctions.getRequiredRuns(matchAllData)).toUpperCase() + "\0", print_writers);
				
				if (matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("VJD")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
							+ "$Select_Type$Equation$Top_Line$Stat_2$txt_Title*GEOM*TEXT SET " + 
							" BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + " (VJD)" + "\0", print_writers);
				} else if (matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("DLS")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
							+ "$Select_Type$Equation$Top_Line$Stat_2$txt_Title*GEOM*TEXT SET " + 
							" BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + " (DLS)" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
							+ "$Select_Type$Equation$Top_Line$Stat_2$txt_Title*GEOM*TEXT SET " + 
							" BALL" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type$Equation$Bottom_Line$txt_Title*GEOM*TEXT SET REQUIRED RUN RATE : " + CricketFunctions.generateRunRate(CricketFunctions.getRequiredRuns(matchAllData),
								0,CricketFunctions.getRequiredBalls(matchAllData),2,matchAllData) + "\0", print_writers);
				break;
				
			case "FOW":
				
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				if(inning == null) {
					return "populateVizInfobarMiddleSection: Inning returned is NULL";
				}
				if(inning.getFallsOfWickets() == null) {
					return "populateVizInfobarMiddleSection : Fall Of Wickets is NULL";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask1$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask2$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$"
						+ "img_Text1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 9 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stat$txt_Title"
						+ "*GEOM*TEXT SET " + "FALL \n OF WICKETS" + "\0", print_writers);
				
				if(inning.getFallsOfWickets().size() <= 8) {
					for(int i = 1; i <= inning.getFallsOfWickets().size(); i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Stats_Wide$Stats$Stat_" + i + "*ACTIVE SET 1 \0",print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_"
								+ i + "$txt_Desig*GEOM*TEXT SET " + inning.getFallsOfWickets().get(i-1).getFowNumber() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_"
								+ i + "$txt_Fig*GEOM*TEXT SET " + inning.getFallsOfWickets().get(i-1).getFowRuns() + "\0", print_writers);
					}
					
					for(int i = inning.getFallsOfWickets().size() + 1; i <= 8; i++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Stats_Wide$Stats$Stat_" + i + "*ACTIVE SET 0 \0",print_writers);
					}
				}
				TimeUnit.MILLISECONDS.sleep(1000);
				break;
				
			case "LAST_X_BALLS":
				
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				if(inning == null) {
					return "populateVizInfobarMiddleSection: Inning returned is NULL";
				}
				//System.out.println(lastXballs);
				this_data_str = new ArrayList<String>();
				this_data_str.add(CricketFunctions.getlastthirtyballsdata(matchAllData, slashOrDash, matchAllData.getEventFile().getEvents(), lastXballs));
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask1$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask2$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$"
						+ "img_Text1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				if(this_data_str.get(this_data_str.size()-1) == null || this_data_str.get(this_data_str.size()-1).split(slashOrDash).length > 4) {
					return "populateVizInfobarMiddleSection: Last " + lastXballs + " Balls data returned invalid";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 9 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stat$txt_Title*GEOM*TEXT SET " + 
						 "LAST\n" + lastXballs + " BALLS" + "\0", print_writers);
				
				for(int i = 1; i <= 4; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Stats_Wide$Stats$Stat_" + i + "*ACTIVE SET 1 \0",print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_1$txt_Desig*GEOM*TEXT SET " + 
						"RUN" + CricketFunctions.Plural(Integer.valueOf(this_data_str.get(this_data_str.size()-1).split(slashOrDash)[0])).toUpperCase() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_1$txt_Fig*GEOM*TEXT SET " + 
						this_data_str.get(this_data_str.size()-1).split(slashOrDash)[0] + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_2$txt_Desig*GEOM*TEXT SET " + 
						"WICKET" + CricketFunctions.Plural(Integer.valueOf(this_data_str.get(this_data_str.size()-1).split(slashOrDash)[1])).toUpperCase() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_2$txt_Fig*GEOM*TEXT SET " + 
						this_data_str.get(this_data_str.size()-1).split(slashOrDash)[1] + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_3$txt_Desig*GEOM*TEXT SET " + 
						"FOUR" + CricketFunctions.Plural(Integer.valueOf(this_data_str.get(this_data_str.size()-1).split(slashOrDash)[2])).toUpperCase() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_3$txt_Fig*GEOM*TEXT SET " + 
						this_data_str.get(this_data_str.size()-1).split(slashOrDash)[2] + "\0", print_writers);
				
				
				if(Integer.valueOf(this_data_str.get(this_data_str.size()-1).split(slashOrDash)[3]) == 1){
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_4$txt_Desig*GEOM*TEXT SET " + 
							"SIX" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_4$txt_Desig*GEOM*TEXT SET " + 
							"SIXES" + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_4$txt_Fig*GEOM*TEXT SET " + 
						this_data_str.get(this_data_str.size()-1).split(slashOrDash)[3] + "\0", print_writers);
				
				for(int i = 5; i <= 8; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Stats_Wide$Stats$Stat_" + i + "*ACTIVE SET 0 \0",print_writers);
				}
				TimeUnit.MILLISECONDS.sleep(1000);
				break;

			case CricketUtil.EXTRAS:
			
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				if(inning == null) {
					return "populateVizInfobarMiddleSection: Inning returned is NULL";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type$2_Line_Text$Select_Sponsor*FUNCTION*Omo*vis_con SET 0 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask1$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask2$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$"
						+ "img_Text1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$Select_Type$2_Line_Text$Maxsize_Title$txt_Title*GEOM*TEXT SET " 
						+ "EXTRAS : " + inning.getTotalExtras() + "\0", print_writers);
				
				if(inning.getTotalPenalties() > 0) {
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$Select_Type$2_Line_Text$Maxsize_Subtitle$txt_Subtitle*GEOM*TEXT SET " 
							+ "WD : " + inning.getTotalWides()  + ",NB : " + inning.getTotalNoBalls()  + ",LB : " + inning.getTotalLegByes() + ",B : " + 
							inning.getTotalByes() + ",PN : " + inning.getTotalPenalties() + "\0", print_writers);
				} else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$Select_Type$2_Line_Text$Maxsize_Subtitle$txt_Subtitle*GEOM*TEXT SET " 
							+ "WD : " + inning.getTotalWides()  + ",NB : " + inning.getTotalNoBalls()  + ",LB : " + inning.getTotalLegByes() + ",B : " + 
							inning.getTotalByes() + "\0", print_writers);
				}
				
				TimeUnit.MILLISECONDS.sleep(1000);
				break;
			case "BALLS_SINCE_LAST_BOUNDARY":
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
					inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
				if(inning == null) {
					return "populateVizInfobarMiddleSection: Inning returned is NULL";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 7 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$QuickScore_Wide$txt_Name*GEOM*TEXT SET " + 
						"BALLS" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$QuickScore_Wide$txt_Subtitle*GEOM*TEXT SET " + 
						"SINCE LAST BOUNDARY" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$QuickScore_Wide$txt_Stat*GEOM*TEXT SET " + 
						CricketFunctions.lastFewOversData(CricketUtil.BOUNDARY, matchAllData.getEventFile().getEvents(), inning.getInningNumber()) + "\0", print_writers);
				
				break;
				
			case "BAT_PROFILE_CAREER": case "BALL_PROFILE_CAREER":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 4 \0",print_writers);
				
				if(FirstPlayerId <= 0 || WhichProfile == null) {
					return "InfoBarPlayerProfile: Player Id NOT found [" + FirstPlayerId + "]";
				}
				
				stat = statistics.stream().filter(st -> st.getPlayer_id() == FirstPlayerId).findAny().orElse(null);
				if(stat == null) {
					return "InfoBarPlayerProfile: Stats not found for Player Id [" + FirstPlayerId + "]";
				}
				
				statsType = statsTypes.stream().filter(st -> st.getStats_short_name().equalsIgnoreCase(WhichProfile)).findAny().orElse(null);
				if(statsType == null) {
					return "InfoBarPlayerProfile: Stats Type not found for profile [" + WhichProfile + "]";
				}
				
				player = CricketFunctions.getPlayerFromMatchData(stat.getPlayer_id(), matchAllData); 
				if(player == null) {
					return "InfoBarPlayerProfile: Player Id not found [" + FirstPlayerId + "]";
				}
				
				stat.setStats_type(statsType);
				
				stat = CricketFunctions.updateTournamentDataWithStats(stat, tournament_matches, matchAllData);
				stat = CricketFunctions.updateStatisticsWithMatchData(stat, matchAllData);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Name_1"
						+ "*GEOM*TEXT SET " + player.getFirstname() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_V"
						+ "*GEOM*TEXT SET " + player.getSurname() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Name_2"
						+ "*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				if(statsType.getStats_short_name().equalsIgnoreCase(CricketUtil.DT20)) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Subtitle"
							+ "*GEOM*TEXT SET " + "T20 CAREER" + "\0", print_writers);
				}
				else if(statsType.getStats_short_name().equalsIgnoreCase(CricketUtil.IT20)) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Subtitle"
							+ "*GEOM*TEXT SET " + "T20I CAREER" + "\0", print_writers);
				}
				else if(statsType.getStats_short_name().equalsIgnoreCase("U19ODI")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Subtitle"
							+ "*GEOM*TEXT SET " + "U19 ODI CAREER" + "\0", print_writers);
				}
				else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$Top$txt_Subtitle"
							+ "*GEOM*TEXT SET " + statsType.getStats_short_name() + "CAREER" + "\0", print_writers);
				}
				
				switch (infobar.getMiddle_section().toUpperCase()) {
				case "BAT_PROFILE_CAREER":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Select_Amount*FUNCTION*Omo*vis_con SET 2 \0",print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$4_Stats$Stat_1"
							+ "$txt_Desig*GEOM*TEXT SET " + "MATCHES" + "\0", print_writers);	
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$4_Stats$Stat_1"
							+ "$txt_Fig*GEOM*TEXT SET " + stat.getMatches() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$4_Stats$Stat_2"
							+ "$txt_Desig*GEOM*TEXT SET " + "RUNS" + "\0", print_writers);	
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$4_Stats$Stat_2"
							+ "$txt_Fig*GEOM*TEXT SET " + stat.getRuns() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$4_Stats$Stat_3"
							+ "$txt_Desig*GEOM*TEXT SET " + "AVERAGE" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$4_Stats$Stat_3"
							+ "$txt_Fig*GEOM*TEXT SET " + CricketFunctions.getAverage(stat.getMatches(), stat.getNot_out(), stat.getRuns(), 2, slashOrDash) + "\0", 
								print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$4_Stats$Stat_4"
							+ "$txt_Desig*GEOM*TEXT SET " + "50s" + "\0", print_writers);	
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$4_Stats$Stat_4"
							+ "$txt_Fig*GEOM*TEXT SET " + stat.getFifties() + "\0", print_writers);
					break;
				case "BALL_PROFILE_CAREER":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Select_Amount*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$3_Stats$Stat_1"
							+ "$txt_Desig*GEOM*TEXT SET " + "MATCHES" + "\0", print_writers);	
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$3_Stats$Stat_1"
							+ "$txt_Fig*GEOM*TEXT SET " + stat.getMatches() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$3_Stats$Stat_2"
							+ "$txt_Desig*GEOM*TEXT SET " + "WICKETS" + "\0", print_writers);	
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$3_Stats$Stat_2"
							+ "$txt_Fig*GEOM*TEXT SET " + stat.getWickets() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$3_Stats$Stat_3"
							+ "$txt_Desig*GEOM*TEXT SET " + "ECONOMY" + "\0", print_writers);	
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_2_Wide$3_Stats$Stat_3"
							+ "$txt_Fig*GEOM*TEXT SET " + CricketFunctions.getEconomy(stat.getRuns_conceded(), stat.getBalls_bowled(), 2, slashOrDash) + "\0", 
								print_writers);
					
					break;

				}
				TimeUnit.MILLISECONDS.sleep(1000);
				break;
				
			case "LAST_WICKET":

				inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
					inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				
				String how_out_txt = "";
				
				if(inning == null) {
					return "populateVizInfobarMiddleSection: Inning returned is NULL";
				}
				
				if(inning.getFallsOfWickets() == null && inning.getFallsOfWickets().isEmpty()) {
					return "populateVizInfobarMiddleSection: FoW returned is EMPTY";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type$2_Line_Text$Select_Sponsor*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask1$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask2$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$"
						+ "img_Text1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				battingCardList.add(inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == 
					inning.getFallsOfWickets().get(inning.getFallsOfWickets().size() - 1).getFowPlayerID()).findAny().orElse(null));

				if(battingCardList.get(battingCardList.size()-1) == null) {
					return "populateVizInfobarLeftBottom: Last wicket returned is invalid";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$Select_Type$2_Line_Text$Maxsize_Title$txt_Title*GEOM*TEXT SET " 
						+ "LAST WICKET" + "\0", print_writers);
				
				if(battingCardList.get(battingCardList.size()-1).getHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
					if(battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute() != null && 
							battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
						how_out_txt = "run out " + "sub (" + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + ")";
					} else {
						how_out_txt = "run out (" + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + ")";
					}
				}
				else if(battingCardList.get(battingCardList.size()-1).getHowOut().equalsIgnoreCase(CricketUtil.CAUGHT)) {
					if(battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute() != null && 
							battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
						how_out_txt = "c" +  " sub (" + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + ")  b " + 
								battingCardList.get(battingCardList.size()-1).getHowOutBowler().getTicker_name();
					} else {
						how_out_txt = "c " + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + "  b " + 
								battingCardList.get(battingCardList.size()-1).getHowOutBowler().getTicker_name();
					}
				}else {
					if(!battingCardList.get(battingCardList.size()-1).getHowOutPartOne().isEmpty()) {
						how_out_txt = battingCardList.get(battingCardList.size()-1).getHowOutPartOne();
					}
					
					if(!battingCardList.get(battingCardList.size()-1).getHowOutPartTwo().isEmpty()) {
						if(!how_out_txt.trim().isEmpty()) {
							how_out_txt = how_out_txt + "  " + battingCardList.get(battingCardList.size()-1).getHowOutPartTwo();
						}else {
							how_out_txt = battingCardList.get(battingCardList.size()-1).getHowOutPartTwo();
						}
					}
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$Select_Type$2_Line_Text$Maxsize_Subtitle$txt_Subtitle*GEOM*TEXT SET " 
						+ battingCardList.get(battingCardList.size()-1).getPlayer().getTicker_name() + " " + how_out_txt + " " +
						battingCardList.get(battingCardList.size()-1).getRuns()  + " (" + battingCardList.get(battingCardList.size()-1).getBalls() + ")"+ "\0", print_writers);
				
				break;
				
			case CricketUtil.PROJECTED:
				
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == 1 &&
					inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				
				if(inning == null) {
					return "populateVizInfobarMiddleSection: 1st Inning returned is NULL";
				}
				
				this_data_str = CricketFunctions.projectedScore(matchAllData);
				if(this_data_str.size() <= 0) {
					return "populateVizInfobarMiddleSection: Projected score invalid";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask1$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask2$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$img_Text1$"
						+ "Projected$Score$img_This_Over*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_OVER_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$"
						+ "img_Text1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 2 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type$Projected$RPO$Maxsize_Title$txt_Title*GEOM*TEXT SET " + "RUNS PER OVER" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type$Projected$Score$Maxsize_Title$txt_Title*GEOM*TEXT SET " + "PROJECTED SCORES" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type$Projected$RPO$txt_Stat_1*GEOM*TEXT SET " + "CURR (" + this_data_str.get(0) + ")" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type$Projected$Score$txt_Stat_1*GEOM*TEXT SET " + this_data_str.get(1) + "\0", print_writers);
			    
			    CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type$Projected$RPO$txt_Stat_2*GEOM*TEXT SET " + this_data_str.get(2) + "\0", print_writers);
			    CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type$Projected$Score$txt_Stat_2*GEOM*TEXT SET " + this_data_str.get(3) + "\0", print_writers);
			    
			    CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type$Projected$RPO$txt_Stat_3*GEOM*TEXT SET " + this_data_str.get(4) + "\0", print_writers);
			    CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type$Projected$Score$txt_Stat_3*GEOM*TEXT SET " + this_data_str.get(5) + "\0", print_writers);
				break;
				
			case "CURR_PARTNERSHIP":

				inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
					inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
				if(inning == null) {
					return "populateVizInfobarMiddleSection: Inning returned is NULL";
				}
				
				if(inning.getPartnerships() != null && inning.getPartnerships().size() <= 0) {
					return "populateVizInfobarMiddleSection: Partnership size is NULL/Zero";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type$2_Line_Text$Select_Sponsor*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask1$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$BaseAll$"
						+ "Left_Mask2$img_Base1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$"
						+ "img_Text1*TEXTURE*IMAGE SET " + 
						Constants.BENGAL_TEXT_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$Select_Type$2_Line_Text$Maxsize_Title$txt_Title*GEOM*TEXT SET " 
						+ CricketFunctions.ordinal(inning.getPartnerships().get(inning.getPartnerships().size()-1).getPartnershipNumber()) + " WICKET PARTNERSHIP"+ "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Analytics$Side" + WhichSide + "$Select_Type$2_Line_Text$Maxsize_Subtitle$txt_Subtitle*GEOM*TEXT SET " 
						+ inning.getPartnerships().get(inning.getPartnerships().size()-1).getTotalRuns() + " (" + 
						inning.getPartnerships().get(inning.getPartnerships().size()-1).getTotalRuns() + ")" + "\0", print_writers);
				
//				infobar.setBatsmanAndBowlOrSponsor("$Partnership_Wide");
//				populateCurrentBatsmen(print_writers, matchAllData, WhichSide);
				
				TimeUnit.MILLISECONDS.sleep(1000);
				break;
			
			case "THIS_MATCH_SIXES":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 17 \0",print_writers);
				
				this_data_str = new ArrayList<String>();
				
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$txt_Title"
						+ "*GEOM*TEXT SET " + "MATCH SIXES" + "\0", print_writers);
				
				
				if(CricketFunctions.extracttournamentFoursAndSixes("CURRENT_MATCH_DATA",tournament_matches, matchAllData, null).getTournament_sixes() > 99) {
					this_data_str.add(CricketFunctions.hundredsTensUnits(String.valueOf(CricketFunctions.extracttournamentFoursAndSixes("CURRENT_MATCH_DATA", 
							tournament_matches, matchAllData, null).getTournament_sixes())));
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Tournament_Sixes$Side_1$txt_Hundread*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Tournament_Sixes$Side_1$txt_Ten*ACTIVE SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_1$txt_Hundread"
							+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_1$txt_Ten"
							+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[1] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_1$txt_Unit"
							+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[2] + "\0", print_writers);
				}else if(CricketFunctions.extracttournamentFoursAndSixes("CURRENT_MATCH_DATA",tournament_matches, matchAllData, null).getTournament_sixes() > 9) {
					this_data_str.add(CricketFunctions.hundredsTensUnits(String.valueOf(CricketFunctions.extracttournamentFoursAndSixes("CURRENT_MATCH_DATA", 
							tournament_matches, matchAllData, null).getTournament_sixes())));
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Tournament_Sixes$Side_1$txt_Hundread*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Tournament_Sixes$Side_1$txt_Ten*ACTIVE SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_1$txt_Ten"
							+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[1] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_1$txt_Unit"
							+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[2] + "\0", print_writers);
				}else {
					this_data_str.add(CricketFunctions.hundredsTensUnits(String.valueOf(CricketFunctions.extracttournamentFoursAndSixes("CURRENT_MATCH_DATA", 
							tournament_matches, matchAllData, null).getTournament_sixes())));
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Tournament_Sixes$Side_1$txt_Hundread*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Tournament_Sixes$Side_1$txt_Ten*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_1$txt_Unit"
							+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[2] + "\0", print_writers);
				}
				
				if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Tournament_Sixes$Sponsor*ACTIVE SET 0 \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Tournament_Sixes$Sponsor*ACTIVE SET 1 \0", print_writers);
				
				break;
				
			case "TOURNAMENT_SIXES":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 17 \0",print_writers);
				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Sixes", "SHOW 0.0");
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$txt_Title"
						+ "*GEOM*TEXT SET " + "TOURNAMENT SIXES" + "\0", print_writers);
				
				this_data_str = new ArrayList<String>();
				this_data_str.add(CricketFunctions.hundredsTensUnits(previous_sixes));
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_1$txt_Hundread"
						+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[0] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_1$txt_Ten"
						+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[1] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_1$txt_Unit"
						+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[2] + "\0", print_writers);
				
				if(WhichSide == 1) {
					this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_RightInfo", "SHOW 0.0");
					
					String new_sixes_value = String.valueOf(CricketFunctions.extracttournamentFoursAndSixes("COMBINED_PAST_CURRENT_MATCH_DATA", 
							tournament_matches, matchAllData, null).getTournament_sixes());
					
					this_data_str.add(CricketFunctions.hundredsTensUnits(new_sixes_value));
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_2$txt_Hundread"
							+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[0] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_2$txt_Ten"
							+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[1] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Side_2$txt_Unit"
							+ "*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[2] + "\0", print_writers);
					
					if(!this_data_str.get(this_data_str.size()-2).split(",")[0].equalsIgnoreCase(this_data_str.get(this_data_str.size()-1).split(",")[0])) {
						this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Sixes$Hundreds", "START");
						this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Sixes$Tens", "START");
						this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Sixes$Units", "START");
					}
					else if(!this_data_str.get(this_data_str.size()-2).split(",")[1].equalsIgnoreCase(this_data_str.get(this_data_str.size()-1).split(",")[1])) {
						this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Sixes$Tens", "START");
						this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Sixes$Units", "START");
					}
					else if(!this_data_str.get(this_data_str.size()-2).split(",")[2].equalsIgnoreCase(this_data_str.get(this_data_str.size()-1).split(",")[2])) {
						this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Sixes$Units", "START");
					}
					//TimeUnit.MILLISECONDS.sleep(1500);
					
					previous_sixes = new_sixes_value;
					
				}
				
				if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Tournament_Sixes$Sponsor*ACTIVE SET 0 \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToSelectedViz(1, "-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Tournament_Sixes$Sponsor*ACTIVE SET 1 \0", print_writers);
				
				break;
				
			case CricketUtil.RESULT:
				
				switch(infobar.getMiddle_section().toUpperCase()) {
				case CricketUtil.RESULT:
					if(this.infobar.getFreeText() != null && this.infobar.getFreeText().size() > 2) {
						return "populateVizInfobarMiddleSection: Unable to process RESULT";
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 3 \0",print_writers);
					
					if(this.infobar.getFreeText().get(0).contains(CricketUtil.BEAT)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Analytics_1_Wide$txt_Top*GEOM*TEXT SET " + this.infobar.getFreeText().get(0) + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Analytics_1_Wide$txt_Bottom*GEOM*TEXT SET " + this.infobar.getFreeText().get(1) + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
								+ "$Analytics_1_Wide$txt_Top*GEOM*TEXT SET " + this.infobar.getFreeText().get(0) + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
							+ "$Analytics_1_Wide$txt_Bottom*GEOM*TEXT SET " + this.infobar.getFreeText().get(1) + "\0", print_writers);
					}
					break;
				}
				break;
				
			case "Team_Tournament_Sixes":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 16 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Team_Tournament_Sixes$Base$img_Highlight*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() +"\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Team_Tournament_Sixes$shadow*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() +"\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Team_Tournament_Sixes$img_Flag*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() +"\0",print_writers);

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Team_Tournament_Sixes$txt_Title*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Team_Tournament_Sixes$Side_"+ WhichSide+"$txt_Hundread*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Team_Tournament_Sixes$Side_"+ WhichSide+"$txt_Ten*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Team_Tournament_Sixes$Side_"+ WhichSide+"$txt_Unit*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				break;
				
			case "Team_Text":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 20 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Team_Text$shadow*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() +"\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Team_Text$img_Flag*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() +"\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Team_Text$txt_LongString*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				
				break;

			}
			infobar.setLast_middle_section(infobar.getMiddle_section());
			break;	
		}
		return Constants.OK;
	}
	public String populateFullSection(List<PrintWriter> print_writers, MatchAllData matchAllData, int WhichSide) {
		if(infobar.getLast_full_section() == null) {
//			WhichSide = 3-WhichSide;
			WhichSide = 1;
			infobar.setLast_full_section(infobar.getFull_section().toUpperCase());
		}
		
		switch(infobar.getFull_section().toUpperCase()) {
		
		case "THIS_OVER":
			this_data_str = new ArrayList<String>();
			this_data_str.add(CricketFunctions.getEventsText(CricketUtil.OVER,infobar.getLast_bowler().getPlayerId() ,
					",", matchAllData.getEventFile().getEvents(),0));
			
			if(this_data_str.get(this_data_str.size()-1) == null || this_data_str.get(this_data_str.size()-1).split(",").length > 15) {
				return "populateVizInfobarRightBottom: This over data returned invalid";
			}
			
			if(inning.getBowling_team().getTeamName4().contains("KHILADI XI") || inning.getBowling_team().getTeamName4().contains("MASTER 11")) {
				if(inning.getBowling_team().getTeamName4().equalsIgnoreCase("KHILADI XI")) {
					color2 = "KHILADI_XI";
				}else if(inning.getBowling_team().getTeamName4().equalsIgnoreCase("MASTER 11")) {
					color2 = "MASTER_XI";
				}
			}else {
				color2 = inning.getBowling_team().getTeamName4();
			}
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Select*FUNCTION*Omo*vis_con SET 10 \0", print_writers);
			
			System.out.println("length = " + this_data_str.get(this_data_str.size()-1).split(",").length);
			for(int iBall = 0; iBall < this_data_str.get(this_data_str.size()-1).split(",").length; iBall++) {
				
				switch (this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase()) {
				case CricketUtil.DOT:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side"+ WhichSide + "$This_Over"
							+ "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					break;
				case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FIVE: 
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side"+ WhichSide + "$This_Over"
							+ "$Balls$" + (iBall + 1) + "$Run$txt_Number*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).
							split(",")[iBall] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side"+ WhichSide + "$This_Over"
							+ "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					break;
				case CricketUtil.FOUR:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side"+ WhichSide + "$This_Over"
							+ "$Balls$" + (iBall + 1) + "$Four$txt_4*GEOM*TEXT SET " + CricketUtil.FOUR + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side"+ WhichSide + "$This_Over"
							+ "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
					break;
				case CricketUtil.SIX:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side"+ WhichSide + "$This_Over"
							+ "$Balls$" + (iBall + 1) + "$Six$txt_6*GEOM*TEXT SET " + CricketUtil.SIX + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side"+ WhichSide + "$This_Over"
							+ "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
					break;
				case CricketUtil.NINE:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side"+ WhichSide + "$This_Over"
							+ "$Balls$" + (iBall + 1) + "$Six$txt_6*GEOM*TEXT SET " + CricketUtil.NINE + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side"+ WhichSide + "$This_Over"
							+ "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
					break;
				case "W":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side"+ WhichSide + "$This_Over"
							+ "$Balls$" + (iBall + 1) + "$Wicket$txt_W*GEOM*TEXT SET " + "W" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side"+ WhichSide + "$This_Over"
							+ "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
					break;

				default:
					
					if(this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase().contains("BOUNDARY")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side"+ WhichSide + "$This_Over"
								+ "$Balls$" + (iBall + 1) + "$Extra$txt_Extra*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).
								split(",")[iBall].toUpperCase().replace("BOUNDARY", "") + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side"+ WhichSide + "$This_Over"
								+ "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 5 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side"+ WhichSide + "$This_Over"
								+ "$Balls$" + (iBall + 1) + "$Extra$txt_Extra*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).
								split(",")[iBall].toUpperCase() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side"+ WhichSide + "$This_Over"
								+ "$Balls$" + (iBall + 1) + "$Choose_Type*FUNCTION*Omo*vis_con SET 5 \0", print_writers);
					}
					
					break;
				}
			}
			
//			if(this_data_str.get(this_data_str.size()-1).split(",").length > 6) {
//				if(infobar.getLast_this_over() != null && infobar.getLast_this_over().split(",").length > 6) {
//					if(infobar.isThisOvers_Title_Fade() == false) {
//						this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$This_Over_Title_Fade_Out", "START");
//						infobar.setThisOvers_Title_Fade(true);
//					}
//				}
//			} else {
//				if(infobar.getLast_this_over() != null && infobar.getLast_this_over().split(",").length <= 6) {
//					this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$This_Over_Title_Fade_Out", "CONTINUE REVERSE");
//					infobar.setThisOvers_Title_Fade(false);
//				}
//			}
			
			if(Integer.valueOf(CricketFunctions.processThisOverRunsCount(infobar.getLast_bowler().getPlayerId(),matchAllData.getEventFile().getEvents())
					.split(slashOrDash)[1]) > 0) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side"+ WhichSide + "$This_Over"
						 + "$Balls*FUNCTION*Omo*vis_con SET " + (this_data_str.get(this_data_str.size()-1).split(",").length) + " \0", print_writers);
			}
			else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side"+ WhichSide + "$This_Over"
						 + "$Balls*FUNCTION*Omo*vis_con SET " + "0" + " \0", print_writers);
			}
			
			infobar.setLast_this_over(this_data_str.get(this_data_str.size()-1));
			break;
		case CricketUtil.BOUNDARY:
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			if(inning == null) {
				return "populateVizInfobarMiddleSection: Inning returned is NULL";
			}
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$InningBoudaries$txt_Header*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$InningBoudaries$First$txt_StatHead*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$InningBoudaries$First$Base*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$InningBoudaries$Second$txt_StatHead*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$InningBoudaries$Second$Base*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$InningBoudaries$Third$txt_StatHead*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$InningBoudaries$Third$Base*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide 
				+ "$Select*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$InningBoudaries$First$txt_Value*GEOM*TEXT SET " + 
					inning.getTotalFours() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$InningBoudaries$Second$txt_Value*GEOM*TEXT SET " + 
					inning.getTotalSixes() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$InningBoudaries$Third$txt_Value*GEOM*TEXT SET " + 
					inning.getTotalNines() + "\0", print_writers);
			
			break;
		case "COMMENTATORS":
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide 
					+ "$Select*FUNCTION*Omo*vis_con SET 6 \0", print_writers);
			
			if(Integer.valueOf(Comms_Name.split(",")[4]) > 0 && Integer.valueOf(Comms_Name.split(",")[3]) > 0 
					&& Integer.valueOf(Comms_Name.split(",")[2]) > 0) {
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Free_Text$txt_Header*GEOM*TEXT SET " + 
						"COMMENTATORS: " + Commentators.get(Integer.valueOf(Comms_Name.split(",")[2])-1).getCommentatorName() + ", " 
						+ Commentators.get(Integer.valueOf(Comms_Name.split(",")[3])-1).getCommentatorName() + " & " 
						+ Commentators.get(Integer.valueOf(Comms_Name.split(",")[4])-1).getCommentatorName() + "\0", print_writers);
				
			}else if(Integer.valueOf(Comms_Name.split(",")[4]) == 0 && Integer.valueOf(Comms_Name.split(",")[3]) > 0 
					&& Integer.valueOf(Comms_Name.split(",")[2]) > 0) {
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Free_Text$txt_Header*GEOM*TEXT SET " + 
						"COMMENTATORS: " + Commentators.get(Integer.valueOf(Comms_Name.split(",")[2])-1).getCommentatorName() + " & " + 
						Commentators.get(Integer.valueOf(Comms_Name.split(",")[3])-1).getCommentatorName() + "\0", print_writers);
				
			}else if(Integer.valueOf(Comms_Name.split(",")[4]) == 0 && Integer.valueOf(Comms_Name.split(",")[3]) == 0 
					&& Integer.valueOf(Comms_Name.split(",")[2]) > 0) {
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Free_Text$txt_Header*GEOM*TEXT SET " + 
						"COMMENTATORS: " + Commentators.get(Integer.valueOf(Comms_Name.split(",")[2])-1).getCommentatorName() + "\0", print_writers);
			}
			break;
		
		case "FREE_TEXT":
			infoBarStats = infobarStats.stream().filter(infostats -> infostats.getOrder() == infobarStatsId).findAny().orElse(null);
			if(infoBarStats == null) {
				return "InfoBarFreeText: Stats  not found for [" + infobarStatsId + "]";
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide 
					+ "$Select*FUNCTION*Omo*vis_con SET 6 \0", print_writers);
				
			if(infoBarStats.getText1() != null && !infoBarStats.getText1().isEmpty() &&
					infoBarStats.getText2() != null && !infoBarStats.getText2().isEmpty()) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Free_Text$txt_Header*GEOM*TEXT SET " + 
						infoBarStats.getText1() + " " + infoBarStats.getText2() + "\0", print_writers);
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Free_Text$txt_Header*GEOM*TEXT SET " + 
						infoBarStats.getText1() + "\0", print_writers);
			}
			break;
			
		case CricketUtil.EXTRAS:
			String inn1CR="-",inn2CR = "-";
			
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			if(inning == null) {
				return "populateVizInfobarMiddleSection: Inning returned is NULL";
			}
			
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Extras$txt_Header*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Extras$First$txt_StatHead*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Extras$First$Base*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Extras$Second$txt_StatHead*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Extras$Second$Base*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Extras$Third$txt_StatHead*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Extras$Third$Base*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Extras$Fourth$txt_StatHead*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Extras$Fourth$Base*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Extras$Fifth$txt_StatHead*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Extras$Fifth$Base*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide 
				+ "$Select*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Extras$First$txt_Value*GEOM*TEXT SET " + 
					inning.getTotalWides() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Extras$Second$txt_Value*GEOM*TEXT SET " + 
					inning.getTotalNoBalls() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Extras$Third$txt_Value*GEOM*TEXT SET " + 
					inning.getTotalByes() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Extras$Fourth$txt_Value*GEOM*TEXT SET " + 
					inning.getTotalLegByes() + "\0", print_writers);
			
			for(int i=0; i<=matchAllData.getEventFile().getEvents().size()-1; i++){
				if(matchAllData.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50) 
						&& matchAllData.getEventFile().getEvents().get(i).getEventInningNumber() == 1) {
					inn1CR = matchAllData.getEventFile().getEvents().get(i).getEventExtra() +  matchAllData.getEventFile().getEvents().get(i).getEventExtraRuns();
				}else if(matchAllData.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50) 
						&& matchAllData.getEventFile().getEvents().get(i).getEventInningNumber() == 2) {
					inn2CR = matchAllData.getEventFile().getEvents().get(i).getEventExtra() +  matchAllData.getEventFile().getEvents().get(i).getEventExtraRuns();
				}
			}
			
			if(inning.getInningNumber() == 1) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Extras$Fifth$txt_Value*GEOM*TEXT SET " + 
						inn1CR + "\0", print_writers);
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Extras$Fifth$txt_Value*GEOM*TEXT SET " + 
						inn2CR + "\0", print_writers);
			}
			
			break;
		case "LAST_WICKET":

			inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
				inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
			String how_out_txt = "";
			
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
			
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$LastWicket$txt_Header*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$LastWicket$First$txt_StatHead*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$LastWicket$First$Base*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$LastWicket$First$txt_StatHead02*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide 
					+ "$Select*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$LastWicket$First$txt_StatHead*GEOM*TEXT SET " + 
					battingCardList.get(battingCardList.size()-1).getPlayer().getTicker_name() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$LastWicket$First$txt_Value*GEOM*TEXT SET " + 
					battingCardList.get(battingCardList.size()-1).getRuns() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$LastWicket$First$txt_Value02*GEOM*TEXT SET " + 
					battingCardList.get(battingCardList.size()-1).getBalls() + "\0", print_writers);
			
			if(battingCardList.get(battingCardList.size()-1).getHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
				if(battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute() != null && 
						battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
					how_out_txt = "run out " + "sub (" + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + ")";
				} else {
					how_out_txt = "run out (" + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + ")";
				}
			}
			else if(battingCardList.get(battingCardList.size()-1).getHowOut().equalsIgnoreCase(CricketUtil.CAUGHT)) {
				if(battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute() != null && 
						battingCardList.get(battingCardList.size()-1).getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
					how_out_txt = "c" +  " sub (" + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + ")  b " + 
							battingCardList.get(battingCardList.size()-1).getHowOutBowler().getTicker_name();
				} else {
					how_out_txt = "c " + battingCardList.get(battingCardList.size()-1).getHowOutFielder().getTicker_name() + "  b " + 
							battingCardList.get(battingCardList.size()-1).getHowOutBowler().getTicker_name();
				}
			}else {
				if(!battingCardList.get(battingCardList.size()-1).getHowOutPartOne().isEmpty()) {
					how_out_txt = battingCardList.get(battingCardList.size()-1).getHowOutPartOne();
				}
				
				if(!battingCardList.get(battingCardList.size()-1).getHowOutPartTwo().isEmpty()) {
					if(!how_out_txt.trim().isEmpty()) {
						how_out_txt = how_out_txt + "  " + battingCardList.get(battingCardList.size()-1).getHowOutPartTwo();
					}else {
						how_out_txt = battingCardList.get(battingCardList.size()-1).getHowOutPartTwo();
					}
				}
			}
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$LastWicket$First$txt_StatHead02*GEOM*TEXT SET " + 
					how_out_txt + "\0", print_writers);
	
			break;
		case CricketUtil.PROJECTED:
			
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == 1 &&
				inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
			if(inning == null) {
				return "populateVizInfobarMiddleSection: 1st Inning returned is NULL";
			}
			
			this_data_str = CricketFunctions.projectedScore(matchAllData);
			if(this_data_str.size() <= 0) {
				return "populateVizInfobarMiddleSection: Projected score invalid";
			}
			
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Projected$txt_Header*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Projected$First$txt_StatHead*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Projected$First$Base*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Projected$Second$txt_StatHead*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Projected$Second$Base*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Projected$Third$txt_StatHead*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Projected$Third$Base*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide 
					+ "$Select*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Projected$First$txt_StatHead*GEOM*TEXT SET " + 
					"@CRR (" + this_data_str.get(0) + ")" + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Projected$First$txt_Value*GEOM*TEXT SET " + 
					this_data_str.get(1) + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Projected$Second$txt_StatHead*GEOM*TEXT SET " + 
					"@"+ this_data_str.get(2) + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Projected$Second$txt_Value*GEOM*TEXT SET " + 
					this_data_str.get(3) + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Projected$Third$txt_StatHead*GEOM*TEXT SET " + 
					"@"+ this_data_str.get(4) + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Projected$Third$txt_Value*GEOM*TEXT SET " + 
					this_data_str.get(5) + "\0", print_writers);
		    
			break;
		case CricketUtil.RESULT:
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			if(inning == null) {
				return "populateVizInfobarMiddleSection: 1st Inning returned is NULL";
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide 
					+ "$Select*FUNCTION*Omo*vis_con SET 6 \0", print_writers);
			
			if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
				if(CricketFunctions.generateMatchSummaryStatus(inning.getInningNumber(), matchAllData, CricketUtil.FULL, "", 
						config.getBroadcaster()).contains("tied")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Free_Text$txt_Header*GEOM*TEXT SET " + 
							"SUPER OVER TIED - WINNER WILL BE DECIDED BY ANOTHER SUPER OVER" + "\0", print_writers);
				}else {
					if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
						if(CricketFunctions.getRequiredRuns(matchAllData) <= 0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Free_Text$txt_Header*GEOM*TEXT SET " + 
									matchAllData.getMatch().getInning().get(1).getBatting_team().getTeamName4() + " WIN THE SUPER OVER" + "\0", print_writers);
						}else if(matchAllData.getMatch().getInning().get(1).getTotalWickets() >= 10 || 
								matchAllData.getMatch().getInning().get(1).getTotalOvers() >= matchAllData.getSetup().getMaxOvers()) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Free_Text$txt_Header*GEOM*TEXT SET " + 
									matchAllData.getMatch().getInning().get(1).getBowling_team().getTeamName4() + " WIN THE SUPER OVER" + "\0", print_writers);
						}
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Free_Text$txt_Header*GEOM*TEXT SET " + 
								CricketFunctions.generateMatchSummaryStatus(inning.getInningNumber(), matchAllData, CricketUtil.FULL, "", 
										config.getBroadcaster()).toUpperCase() + "\0", print_writers);
					}
				}
			}else {
				if(CricketFunctions.generateMatchSummaryStatus(inning.getInningNumber(), matchAllData, CricketUtil.FULL, "", 
						config.getBroadcaster()).contains("tied")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Free_Text$txt_Header*GEOM*TEXT SET " + 
							"MATCH TIED - WINNER WILL BE DECIDED BY SUPER OVER" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Free_Text$txt_Header*GEOM*TEXT SET " + 
							CricketFunctions.generateMatchSummaryStatus(inning.getInningNumber(), matchAllData, CricketUtil.FULL, "", 
									config.getBroadcaster()).toUpperCase() + "\0", print_writers);
				}
			}
			break;
		case "EQUATION":
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			if(inning == null) {
				return "populateVizInfobarMiddleSection: 1st Inning returned is NULL";
			}
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide 
					+ "$Select*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
			
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$txt_Header*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$First$txt_StatHead*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$First$Base*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Second$txt_StatHead*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Second$Base*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Third$txt_StatHead*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Third$Base*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBatting_team().getTeamName4() + "\0", print_writers);
			
			if(matchAllData.getSetup().getTargetType() != null && !matchAllData.getSetup().getTargetType().isEmpty()) {
				if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.DLS)) {
					if(CricketFunctions.getRequiredRuns(matchAllData) <= 1) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$First$txt_Value*GEOM*TEXT SET " + 
								CricketFunctions.getRequiredRuns(matchAllData) + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Second$txt_StatHead*GEOM*TEXT SET " + 
								"RUN FROM" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Second$txt_Value*GEOM*TEXT SET " + 
								CricketFunctions.getRequiredBalls(matchAllData)  + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Third$txt_StatHead*GEOM*TEXT SET " + 
								"BALL"+ CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + " ("+ CricketUtil.DLS +")"  + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$First$txt_Value*GEOM*TEXT SET " + 
								CricketFunctions.getRequiredRuns(matchAllData) + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Second$txt_StatHead*GEOM*TEXT SET " + 
								"MORE RUNS FROM" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Second$txt_Value*GEOM*TEXT SET " + 
								CricketFunctions.getRequiredBalls(matchAllData)  + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Third$txt_StatHead*GEOM*TEXT SET " + 
								"BALL"+ CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + " ("+ CricketUtil.DLS +")"  + "\0", print_writers);
					}
				}
				else if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.VJD)) {
					if(CricketFunctions.getRequiredRuns(matchAllData) <= 1) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$First$txt_Value*GEOM*TEXT SET " + 
								CricketFunctions.getRequiredRuns(matchAllData) + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Second$txt_StatHead*GEOM*TEXT SET " + 
								"RUN FROM" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Second$txt_Value*GEOM*TEXT SET " + 
								CricketFunctions.getRequiredBalls(matchAllData)  + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Third$txt_StatHead*GEOM*TEXT SET " + 
								"BALL"+ CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + " ("+ CricketUtil.VJD  +")"  + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$First$txt_Value*GEOM*TEXT SET " + 
								CricketFunctions.getRequiredRuns(matchAllData) + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Second$txt_StatHead*GEOM*TEXT SET " + 
								"MORE RUNS FROM" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Second$txt_Value*GEOM*TEXT SET " + 
								CricketFunctions.getRequiredBalls(matchAllData)  + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Third$txt_StatHead*GEOM*TEXT SET " + 
								"BALL"+ CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + " ("+ CricketUtil.VJD +")"  + "\0", print_writers);
					}
				}
			}else {
				if(CricketFunctions.getRequiredRuns(matchAllData) <= 1) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$First$txt_Value*GEOM*TEXT SET " + 
							CricketFunctions.getRequiredRuns(matchAllData) + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Second$txt_StatHead*GEOM*TEXT SET " + 
							"RUN FROM" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Second$txt_Value*GEOM*TEXT SET " + 
							CricketFunctions.getRequiredBalls(matchAllData)  + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Third$txt_StatHead*GEOM*TEXT SET " + 
							"BALL"+ CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$First$txt_Value*GEOM*TEXT SET " + 
							CricketFunctions.getRequiredRuns(matchAllData) + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Second$txt_StatHead*GEOM*TEXT SET " + 
							"MORE RUNS FROM" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Second$txt_Value*GEOM*TEXT SET " + 
							CricketFunctions.getRequiredBalls(matchAllData)  + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$Equation$Third$txt_StatHead*GEOM*TEXT SET " + 
							"BALL"+ CricketFunctions.Plural(CricketFunctions.getRequiredBalls(matchAllData)).toUpperCase() + "\0", print_writers);
				}
			}
			break;
		case "COMPARE":
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)
					&& inn.getInningNumber() == 2).findAny().orElse(null);
			
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$AT_THIS_STAGE$txt_Header*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$AT_THIS_STAGE$First$txt_StatHead*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$AT_THIS_STAGE$First$Base*TEXTURE*IMAGE SET " 
//					+ Constants.BASE_PATH + "1/" + inning.getBowling_team().getTeamName4() + "\0", print_writers);
				
				if(inning == null) {
					return "populateVizInfobarRightSection: 2nd Inning returned is NULL";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide 
						+ "$Select*FUNCTION*Omo*vis_con SET 5 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$AT_THIS_STAGE$First$txt_StatHead*GEOM*TEXT SET " + 
						inning.getBowling_team().getTeamName1()+ " WERE" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Stage3$Side" + WhichSide + "$AT_THIS_STAGE$First$txt_Value*GEOM*TEXT SET " + 
						CricketFunctions.compareInningData(matchAllData, "-", 1, matchAllData.getEventFile().getEvents()) + "\0", print_writers);
			break;
		}
		return Constants.OK;
	}
	
	public String populateInfobarIdent(List<PrintWriter> print_writers,String whatToProcess, MatchAllData matchAllData,int WhichSide) {
		
		switch (config.getBroadcaster()) {
		case Constants.ISPL:
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			if(inning == null) {
				return "populateInfobarTeamNameScore: Inning return is NULL";
			}
			
			if(matchAllData.getSetup().getHomeTeam().getTeamName4().contains("KHILADI XI") || matchAllData.getSetup().getHomeTeam().getTeamName4().contains("MASTER 11")) {
				if(matchAllData.getSetup().getHomeTeam().getTeamName4().equalsIgnoreCase("KHILADI XI")) {
					color = "KHILADI_XI";
				}else if(matchAllData.getSetup().getHomeTeam().getTeamName4().equalsIgnoreCase("MASTER 11")) {
					color = "MASTER_XI";
				}
			}else {
				color = matchAllData.getSetup().getHomeTeam().getTeamName4();
			}
			
			if(matchAllData.getSetup().getAwayTeam().getTeamName4().contains("KHILADI XI") || matchAllData.getSetup().getAwayTeam().getTeamName4().contains("MASTER 11")) {
				if(matchAllData.getSetup().getAwayTeam().getTeamName4().equalsIgnoreCase("KHILADI XI")) {
					color2 = "KHILADI_XI";
				}else if(matchAllData.getSetup().getAwayTeam().getTeamName4().equalsIgnoreCase("MASTER 11")) {
					color2 = "MASTER_XI";
				}
			}else {
				color2 = matchAllData.getSetup().getAwayTeam().getTeamName4();
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Ident$HomeSideLogo$HomeTeamAll$HomeTeamLogo$img*TEXTURE*IMAGE SET " + 
					Constants.ISPL_LOGOS_PATH + color + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Ident$HomeSideLogo$HomeTeamAll$BaseGrp$HomeTeamLogo$img*TEXTURE*IMAGE SET " + 
					Constants.ISPL_LOGOS_BW_PATH + color + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Ident$AwaySideLogo$AwayTeamAll$AwayTeamLogo$img*TEXTURE*IMAGE SET " + 
					Constants.ISPL_LOGOS_PATH + color2 + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Ident$AwaySideLogo$AwayTeamAll$BaseGrp$AwayTeamLogo$img*TEXTURE*IMAGE SET " + 
					Constants.ISPL_LOGOS_BW_PATH + color2 + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Ident$HomeTeamBaseGrp$img1*TEXTURE*IMAGE SET " + 
					Constants.BASE_PATH + "1/" + color + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Ident$HomeTeamNameGrp$img_txt1*TEXTURE*IMAGE SET " + 
					Constants.TEXT_PATH + "1/" + color + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Ident$AwayTeamBaseGrp$img1*TEXTURE*IMAGE SET " + 
					Constants.BASE_PATH + "1/" + color2 + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Ident$AwayTeamNameGrp$img_txt1*TEXTURE*IMAGE SET " + 
					Constants.TEXT_PATH + "1/" + color2 + "\0", print_writers);
			
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Ident$HomeSideLogo$HomeTeamAll$BaseGrp$img*TEXTURE*IMAGE SET " + 
					Constants.BASE_PATH + "2/" + color + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Ident$HomeSideLogo$HomeTeamAll$Elements$img*TEXTURE*IMAGE SET " + 
					Constants.BASE_PATH + "1/" + color + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Ident$AwaySideLogo$AwayTeamAll$BaseGrp$img*TEXTURE*IMAGE SET " + 
					Constants.BASE_PATH + "2/" + matchAllData.getSetup().getAwayTeam().getTeamName4() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Ident$AwaySideLogo$AwayTeamAll$Elements$img*TEXTURE*IMAGE SET " + 
					Constants.BASE_PATH + "1/" + matchAllData.getSetup().getAwayTeam().getTeamName4() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Ident$HomeTeamNameGrp$txt_TeamName*GEOM*TEXT SET " + 
					matchAllData.getSetup().getHomeTeam().getTeamName1() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Ident$AwayTeamNameGrp$txt_TeamName*GEOM*TEXT SET " + 
					matchAllData.getSetup().getAwayTeam().getTeamName1() + "\0", print_writers);
			infoIdentSection(print_writers, whatToProcess, matchAllData, WhichSide);
			
			return Constants.OK;
			
		case Constants.BENGAL_T20:
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Left$In$Mask1$img_Base1*TEXTURE*IMAGE SET " + 
					Constants.BENGAL_BASE_PATH + "1/" + matchAllData.getSetup().getHomeTeam().getTeamName4() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Left$In$Mask2$img_Base1*TEXTURE*IMAGE SET " + 
					Constants.BENGAL_BASE_PATH + "1/" + matchAllData.getSetup().getHomeTeam().getTeamName4() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Left$In$TextIn$img_Text1*TEXTURE*IMAGE SET " + 
					Constants.BENGAL_TEXT_PATH + "1/" + matchAllData.getSetup().getHomeTeam().getTeamName4() + "\0", print_writers);
			
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Right$In$Mask1$img_Base1*TEXTURE*IMAGE SET " + 
					Constants.BENGAL_BASE_PATH + "1/" + matchAllData.getSetup().getAwayTeam().getTeamName4() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Right$In$Mask2$img_Base1*TEXTURE*IMAGE SET " + 
					Constants.BENGAL_BASE_PATH + "1/" + matchAllData.getSetup().getAwayTeam().getTeamName4() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Right$In$TextIn$img_Text1*TEXTURE*IMAGE SET " + 
					Constants.BENGAL_TEXT_PATH + "1/" + matchAllData.getSetup().getAwayTeam().getTeamName4() + "\0", print_writers);
			
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Left$Out$img_Badge*TEXTURE*IMAGE SET " + 
					Constants.BENGAL_ICONS_PATH + matchAllData.getSetup().getHomeTeam().getTeamName4() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Left$In$txt_TeamFirstName*GEOM*TEXT SET " + 
					matchAllData.getSetup().getHomeTeam().getTeamName2() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Left$In$txt_TeamLastName*GEOM*TEXT SET " + 
					matchAllData.getSetup().getHomeTeam().getTeamName3() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Right$Out$img_Badge*TEXTURE*IMAGE SET " + 
					Constants.BENGAL_ICONS_PATH + matchAllData.getSetup().getAwayTeam().getTeamName4() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Right$In$txt_TeamFirstName*GEOM*TEXT SET " + 
					matchAllData.getSetup().getAwayTeam().getTeamName2() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Right$In$txt_TeamLastName*GEOM*TEXT SET " + 
					matchAllData.getSetup().getAwayTeam().getTeamName3() + "\0", print_writers);
			
			infoIdentSection(print_writers, whatToProcess, matchAllData, WhichSide);
			
			return Constants.OK;	
		}
		
		return Constants.OK;	
		
	}
	public String infoIdentSection(List<PrintWriter> print_writers,String whatToProcess, MatchAllData matchAllData,int WhichSide) {
			
		switch (config.getBroadcaster()) {
		case Constants.ISPL:
			if(WhichSide==1) {
				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$IdentInfo$Change", "SHOW 0.0");
			}
			
			switch (whatToProcess.split(",")[2]) {
			case CricketUtil.TOSS:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$IdentInfo$Side" + WhichSide + "$txt_IdentInfo*GEOM*TEXT SET " 
						+ CricketFunctions.generateTossResult(matchAllData, CricketUtil.FULL, CricketUtil.FIELD, CricketUtil.SHORT, CricketUtil.ELECTED).replace("toss", "tip-top").toUpperCase() + "\0", print_writers);
				break;
			case "TOURNAMENT":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$IdentInfo$Side" + WhichSide + "$txt_IdentInfo*GEOM*TEXT SET " 
						+ "ISPL - 2024" + "\0", print_writers);
				break;
			case "SUPEROVER":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$IdentInfo$Side" + WhichSide + "$txt_IdentInfo*GEOM*TEXT SET " 
						+ "SUPER OVER" + "\0", print_writers);
				break;
			case "TARGET":
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				
				
				if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER) && matchAllData.getSetup().getMaxOvers() == 1) {
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$IdentInfo$Side" + WhichSide + "$txt_IdentInfo*GEOM*TEXT SET " 
							+ inning.getBatting_team().getTeamName4()+ " NEED "+ CricketFunctions.getTargetRuns(matchAllData) + " RUNS" + " TO WIN " + 
							String.valueOf("FROM " + matchAllData.getSetup().getMaxOvers()*6)+ " BALLS"+ "\0", print_writers);
				}else {
					if(matchAllData.getSetup().getTargetOvers() == "" || matchAllData.getSetup().getTargetOvers().trim().isEmpty() && matchAllData.getSetup().getTargetRuns() == 0) {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$IdentInfo$Side" + WhichSide + "$txt_IdentInfo*GEOM*TEXT SET " 
								+ inning.getBatting_team().getTeamName4()+ " NEED "+ CricketFunctions.getTargetRuns(matchAllData) + " RUNS" + " TO WIN " + 
								String.valueOf("FROM " + CricketFunctions.getTargetOvers(matchAllData)) + " OVERS" + "\0", print_writers);

					}else {
						
						if(matchAllData.getSetup().getTargetOvers() != "") {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$IdentInfo$Side" + WhichSide + "$txt_IdentInfo*GEOM*TEXT SET " 
									+ inning.getBatting_team().getTeamName4()+ " NEED "+ CricketFunctions.getTargetRuns(matchAllData) + " RUNS" + " TO WIN " + 
									String.valueOf("FROM " + CricketFunctions.getTargetOvers(matchAllData)) + " OVERS" + "\0", print_writers);
						}
						if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("VJD")) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$IdentInfo$Side" + WhichSide + "$txt_IdentInfo*GEOM*TEXT SET " 
									+ inning.getBatting_team().getTeamName4()+ " NEED "+ CricketFunctions.getTargetRuns(matchAllData) + " RUNS" + " TO WIN " + 
									String.valueOf("FROM " + CricketFunctions.getTargetOvers(matchAllData)) + " OVERS (VJD)" + "\0", print_writers);
							
						}else if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("DLS")) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$IdentInfo$Side" + WhichSide + "$txt_IdentInfo*GEOM*TEXT SET " 
									+ inning.getBatting_team().getTeamName4()+ " NEED "+ CricketFunctions.getTargetRuns(matchAllData) + " RUNS" + " TO WIN " + 
									String.valueOf("FROM " + CricketFunctions.getTargetOvers(matchAllData)) + " OVERS (DLS)" + "\0", print_writers);
						}
					}
				}
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$IdentInfo$Side" + WhichSide + "$txt_IdentInfo*GEOM*TEXT SET " 
//						+ inning.getBatting_team().getTeamName4()+ " NEED "+ CricketFunctions.getTargetRuns(matchAllData) + "\0", print_writers);
				break;
			case CricketUtil.RESULT:
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$IdentInfo$Side" + WhichSide + "$txt_IdentInfo*GEOM*TEXT SET " 
						+ CricketFunctions.generateMatchSummaryStatus(2, matchAllData, CricketUtil.FULL, "|", config.getBroadcaster()).toUpperCase() + "\0", print_writers);
				break;
			case "VENUE":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$IdentInfo$Side" + WhichSide + "$txt_IdentInfo*GEOM*TEXT SET " 
						+ "DADOJI KONDADEV STADIUM, THANE" + "\0", print_writers);
				break;
			}
			break;
			
		case Constants.BENGAL_T20:
			
//			if(WhichSide==1) {
//				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$IdentInfo$Change", "SHOW 0.0");
//			}
			
			switch (whatToProcess.split(",")[2]) {
			case CricketUtil.TOSS:
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Title*GEOM*TEXT SET " + 
						"TOSS" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Subtitle*GEOM*TEXT SET " + 
						CricketFunctions.generateTossResult(matchAllData, "NO_TOSS", CricketUtil.FIELD, CricketUtil.SHORT, CricketUtil.ELECTED).toUpperCase() + "\0", print_writers);
				break;
			case "TOURNAMENT":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Title*GEOM*TEXT SET " + 
						matchAllData.getSetup().getTournament() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Subtitle*GEOM*TEXT SET " + 
						"LIVE FROM " + matchAllData.getSetup().getVenueName() + "\0", print_writers);
				break;
			case "SUPEROVER":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Title*GEOM*TEXT SET " + 
						"SUPER" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Subtitle*GEOM*TEXT SET " + 
						" OVER " + "\0", print_writers);
				break;
			case "TARGET":
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				
				
				if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER) && matchAllData.getSetup().getMaxOvers() == 1) {
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Title*GEOM*TEXT SET " + 
							matchAllData.getMatch().getInning().get(1).getBatting_team().getTeamLogo() + " NEED " + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Subtitle*GEOM*TEXT SET " + 
							CricketFunctions.getTargetRuns(matchAllData) + " RUNS" + " TO WIN " + 
							String.valueOf("FROM " + matchAllData.getSetup().getMaxOvers()*6)+ " BALLS" + "\0", print_writers);
				}else {
					if(matchAllData.getSetup().getTargetOvers() == "" || matchAllData.getSetup().getTargetOvers().trim().isEmpty() && matchAllData.getSetup().getTargetRuns() == 0) {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Title*GEOM*TEXT SET " + 
								matchAllData.getMatch().getInning().get(1).getBatting_team().getTeamLogo() + " NEED " + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Subtitle*GEOM*TEXT SET " + 
								CricketFunctions.getTargetRuns(matchAllData) + " RUNS" + " TO WIN " + 
								String.valueOf("FROM " + CricketFunctions.getTargetOvers(matchAllData)) + " OVERS" + "\0", print_writers);

					}else {
						
						if(matchAllData.getSetup().getTargetOvers() != "") {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Title*GEOM*TEXT SET " + 
									matchAllData.getMatch().getInning().get(1).getBatting_team().getTeamLogo() + " NEED " + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Subtitle*GEOM*TEXT SET " + 
									CricketFunctions.getTargetRuns(matchAllData) + " RUNS" + " TO WIN " + 
									String.valueOf("FROM " + CricketFunctions.getTargetOvers(matchAllData)) + " OVERS" + "\0", print_writers);
						}
						if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("VJD")) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Title*GEOM*TEXT SET " + 
									matchAllData.getMatch().getInning().get(1).getBatting_team().getTeamLogo() + " NEED " + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Subtitle*GEOM*TEXT SET " + 
									CricketFunctions.getTargetRuns(matchAllData) + " RUNS" + " TO WIN " + 
									String.valueOf("FROM " + CricketFunctions.getTargetOvers(matchAllData)) + " OVERS (VJD)" + "\0", print_writers);
							
						}else if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("DLS")) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Title*GEOM*TEXT SET " + 
									matchAllData.getMatch().getInning().get(1).getBatting_team().getTeamLogo() + " NEED " + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Subtitle*GEOM*TEXT SET " + 
									CricketFunctions.getTargetRuns(matchAllData) + " RUNS" + " TO WIN " + 
									String.valueOf("FROM " + CricketFunctions.getTargetOvers(matchAllData)) + " OVERS (DLS)" + "\0", print_writers);
						}
					}
				}
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$IdentInfo$Side" + WhichSide + "$txt_IdentInfo*GEOM*TEXT SET " 
//						+ inning.getBatting_team().getTeamName4()+ " NEED "+ CricketFunctions.getTargetRuns(matchAllData) + "\0", print_writers);
				break;
			case CricketUtil.RESULT:
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Title*GEOM*TEXT SET " + 
						CricketFunctions.generateMatchSummaryStatus(2, matchAllData, CricketUtil.FULL, "|", config.getBroadcaster()).toUpperCase().split(" WIN ")[0] + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Subtitle*GEOM*TEXT SET " + 
						"WIN " + CricketFunctions.generateMatchSummaryStatus(2, matchAllData, CricketUtil.FULL, "|", config.getBroadcaster()).toUpperCase().split(" WIN ")[1] + "\0", print_writers);
				break;
			case "VENUE":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Title*GEOM*TEXT SET " + 
						"VENUE" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Ident$Middle$Side" + WhichSide + "$txt_Subtitle*GEOM*TEXT SET " + 
						matchAllData.getSetup().getVenueName() + "\0", print_writers);
				break;
			}
			break;	
		}
		return Constants.OK;
	}
	
	public List<String> getOverbyOver(int inning,List<Event> event, MatchAllData matchAllData) {
		
		String to = "";
		int runs = 0,wicket = 0,bowlerId = 0;
		boolean over_start = false,is_this_current_over = false;
		ArrayList<String> allData = new ArrayList<String>();
		if ((matchAllData.getEventFile().getEvents() != null) && (matchAllData.getEventFile().getEvents().size() > 0)) {
			for(Event evnt: matchAllData.getEventFile().getEvents()) {
				if(evnt.getEventInningNumber() == inning) {
					if(evnt.getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50)) {
						String lastElement = allData.get(allData.size() - 1);
				        lastElement = lastElement.concat("-CR," + evnt.getEventExtra() + "," + evnt.getEventExtraRuns());
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
//		System.out.println("allData = " + allData);
		return allData;
		
	}
	
	public void playChallengeWipe(List<PrintWriter> print_writers, int bonusRuns, int challengeRuns) {
		
		if((bonusRuns*2) >= challengeRuns) {
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$Bonus$BackText$img_txt2*GEOM*TEXT SET " 
					+ "+" + bonusRuns + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$Bonus$FrontText$Bonus_Green*GEOM*TEXT SET " 
					+ "+" + bonusRuns + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$Bonus$FrontText$Select*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
		}else {
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$Bonus$BackText$img_txt2*GEOM*TEXT SET " 
					+ "-" + bonusRuns + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$Bonus$FrontText$Bonus_Red*GEOM*TEXT SET " 
					+ "-" + bonusRuns + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$Bonus$FrontText$Select*FUNCTION*Omo*vis_con SET 0 \0",print_writers);
		}
		
//		if((bonusRuns*2) >= challengeRuns) {
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Bonus$BackText$img_txt2*GEOM*TEXT SET " 
//					+"+"+ bonusRuns + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Bonus$FrontText$Bonus*GEOM*TEXT SET " 
//					+"+"+ bonusRuns + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Bonus$Select*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
//		}else {
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Bonus$BackText$img_txt2*GEOM*TEXT SET " 
//					+"+"+ bonusRuns + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Bonus$FrontText$Bonus*GEOM*TEXT SET " 
//					+"-"+ bonusRuns + "\0", print_writers);
//			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Bonus$Select*FUNCTION*Omo*vis_con SET 0 \0",print_writers);
//		}
		
		this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Bonus_In", "START");
		
	}
}