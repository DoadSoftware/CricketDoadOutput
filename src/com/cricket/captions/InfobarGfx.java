package com.cricket.captions;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.cricket.containers.Infobar;
import com.cricket.model.BattingCard;
import com.cricket.model.BowlingCard;
import com.cricket.model.Configuration;
import com.cricket.model.Inning;
import com.cricket.model.MatchAllData;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;

public class InfobarGfx 
{
	String slashOrDash = "-";
	public Infobar infobar = new Infobar();
	public Animation this_animation = new Animation();
	
	Inning inning;
	
	public boolean updateInfobar(List<PrintWriter> print_writers,Configuration config,MatchAllData matchAllData) throws InterruptedException {
		
		infobarTeamNameScore(true,print_writers,config,matchAllData);
		populateVizInfobarBatsman(print_writers, matchAllData, 1);
		populateVizInfobarBowler(print_writers, matchAllData, 1);
		
		if(infobar.getLeft_bottom() != null) {
			populateVizInfobarLeftBottom(print_writers, matchAllData, 1);
		}		
		if(infobar.getLeft_bottom() != null && !infobar.getMiddle_section().equalsIgnoreCase(CricketUtil.BATSMAN)) {
			populateVizInfobarMiddleSection(print_writers, matchAllData, 1);
		}
		return true;
	}
	
	public boolean populateInfobar(List<PrintWriter> print_writers,Configuration config,String whatToProcess,MatchAllData matchAllData) throws InterruptedException {
		if(infobarTeamNameScore(false,print_writers,config,matchAllData) != true) {
			return false;
		}else {
			infobar.setMiddle_section(whatToProcess.split(",")[1]);
			infobar.setLeft_bottom(whatToProcess.split(",")[2]);
			populateVizInfobarLeftBottom(print_writers, matchAllData, 1);
			populateVizInfobarMiddleSection(print_writers, matchAllData, 1);
		}
		return true;
	}
	
	public boolean infobarTeamNameScore(boolean is_this_updating,List<PrintWriter> print_writers,Configuration config,MatchAllData matchAllData) {
		
		String Overs_omo = "";
		switch(config.getBroadcaster()) {
		case Constants.ICC_U19_2023:
			for(PrintWriter print_writer : print_writers) {
				for(Inning inn : matchAllData.getMatch().getInning()) {
					if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						if(is_this_updating == false) {
							//Flag
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Flag_Left$img_Flag*TEXTURE*IMAGE SET " + 
									Constants.ICC_U19_2023_FLAG_PATH + inn.getBatting_team().getTeamName4() + "\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Flag_Right$img_Flag*TEXTURE*IMAGE SET " + 
									Constants.ICC_U19_2023_FLAG_PATH + inn.getBowling_team().getTeamName4() + "\0");
							
							//Team Name
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Top$Scorebox$txt_Team_2*GEOM*TEXT SET " + 
									inn.getBatting_team().getTeamName4() + "\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Top$txt_Team_1*GEOM*TEXT SET " + 
									inn.getBowling_team().getTeamName4() + "\0");
							
							if(!CricketFunctions.processPowerPlay(CricketUtil.MINI,matchAllData).isEmpty()) {
								 if(infobar.isPowerplay_on_screen() == true) {
									 break;
						         }
						         else {
						        	 infobar.setPowerplay_on_screen(true);
						        	 print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Top$Powerplay$txt_Powerplay*GEOM*TEXT SET " + 
						        			 CricketFunctions.processPowerPlay(CricketUtil.MINI,matchAllData) + "\0");
						        	 print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Powerplay START \0");
						         }
							}
					    	else {
								if(infobar.isPowerplay_on_screen() == true) {
									print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Powerplay SHOW 0.0 \0");
									infobar.setPowerplay_on_screen(false);
						         }
							}
						}
						
						//Score and Overs
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Top$Scorebox$txt_Score*GEOM*TEXT SET " + 
								CricketFunctions.getTeamScore(inn, "-", false) + "\0");
						
						if(matchAllData.getSetup().getTargetOvers() != "") {
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Top$Overs$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Top$Overs_with_Reduced$txt_Overs*GEOM*TEXT SET " + 
									matchAllData.getSetup().getTargetOvers() + "\0");
							Overs_omo = "Overs_with_Reduced";
						}else {
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Top$Overs$Choose_Type*FUNCTION*Omo*vis_con SET 0 \0");
							Overs_omo = "Overs_Only";
						}
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Top$" + Overs_omo + "$txt_Overs*GEOM*TEXT SET " + 
								CricketFunctions.OverBalls(inn.getTotalOvers(),inn.getTotalBalls()) + "\0");
					}
				}
			}
			break;
		}
		return true;
	}
	
	public void populateVizInfobarBatsman(List<PrintWriter> print_writers,MatchAllData matchAllData,int WhichSide) throws InterruptedException
	{ 
		List<BattingCard> current_batsmen = new ArrayList<BattingCard>();
		for(PrintWriter print_writer : print_writers) {
			for(Inning inn : matchAllData.getMatch().getInning()) {
				if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
					for (BattingCard bc : inn.getBattingCard()) {
						if(inn.getPartnerships() != null && inn.getPartnerships().size() > 0) {
							if(bc.getPlayerId() == inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterNo()) {
								current_batsmen.add(bc);
							} else if(bc.getPlayerId() == inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterNo()) {
								current_batsmen.add(bc);
							}
						}
					}
					
					if(infobar.getLast_batsmen() == null || infobar.getLast_batsmen().size() <= 0) {
						infobar.setLast_batsmen(current_batsmen);
					}
					populateCurrentBatsmen(print_writer, matchAllData,WhichSide,current_batsmen);
				}
			}
		}
		
	}
	public void populateCurrentBatsmen(PrintWriter print_writer, MatchAllData matchAllData,int WhichSide,List<BattingCard> current_batsmen) throws InterruptedException {
		
		for(Inning inn : matchAllData.getMatch().getInning()) {
			
			if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
				
				if(current_batsmen != null && current_batsmen.size() >= 1) {
					if(infobar.getLast_batsmen() != null && infobar.getLast_batsmen().size() >= 1) {
						if(infobar.getLast_batsmen().get(0).getPlayerId() != current_batsmen.get(0).getPlayerId()) {
							this_animation.processAnimation(print_writer, "Batsman1Out", "START");
							TimeUnit.MILLISECONDS.sleep(800);
							
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_1$Batsman*GEOM*TEXT SET " + 
									current_batsmen.get(0).getPlayer().getTicker_name() + "\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_1$txt_Runs*GEOM*TEXT SET " + 
									current_batsmen.get(0).getRuns() + "\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_1$txt_Balls*GEOM*TEXT SET " + 
									current_batsmen.get(0).getBalls() + "\0");
							
							this_animation.processAnimation(print_writer, "Batsman1In", "START");
							this_animation.processAnimation(print_writer, "Bat1DelhighlightOut", "SHOW 0.180");
						}else {
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_1$Batsman*GEOM*TEXT SET " + 
									current_batsmen.get(0).getPlayer().getTicker_name() + "\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_1$txt_Runs*GEOM*TEXT SET " + 
									current_batsmen.get(0).getRuns() + "\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_1$txt_Balls*GEOM*TEXT SET " + 
									current_batsmen.get(0).getBalls() + "\0");
							
							if(current_batsmen.get(0).getStatus().equalsIgnoreCase(CricketUtil.OUT) 
									|| current_batsmen.get(0).getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) { 
								this_animation.processAnimation(print_writer, "Bat1DelhighlightIn", "SHOW 0.220");
							}
						}
						if(infobar.getLast_batsmen().get(1).getPlayerId() != current_batsmen.get(1).getPlayerId()) {
							this_animation.processAnimation(print_writer, "Batsman2Out", "START");
							TimeUnit.MILLISECONDS.sleep(800);
							
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_2$Batsman*GEOM*TEXT SET " + 
									current_batsmen.get(1).getPlayer().getTicker_name() + "\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_2$txt_Runs*GEOM*TEXT SET " + 
									current_batsmen.get(1).getRuns() + "\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_2$txt_Balls*GEOM*TEXT SET " + 
									current_batsmen.get(1).getBalls() + "\0");
							
							this_animation.processAnimation(print_writer, "Batsman2In", "START");
							this_animation.processAnimation(print_writer, "Bat2DelhighlightOut", "SHOW 0.180");
						}else {
							
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_2$Batsman*GEOM*TEXT SET " + 
									current_batsmen.get(1).getPlayer().getTicker_name() + "\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_2$txt_Runs*GEOM*TEXT SET " + 
									current_batsmen.get(1).getRuns() + "\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_2$txt_Balls*GEOM*TEXT SET " + 
									current_batsmen.get(1).getBalls() + "\0");
							
							if(current_batsmen.get(1).getStatus().equalsIgnoreCase(CricketUtil.OUT) 
									|| current_batsmen.get(1).getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) { 
								this_animation.processAnimation(print_writer, "Bat2DelhighlightIn", "SHOW 0.220");
							}
						}
					}
					
					if(current_batsmen.get(0).getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						if(current_batsmen.get(0).getOnStrike().equalsIgnoreCase(CricketUtil.YES)) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_1$obj_Indicator*ACTIVE SET 1 \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_2$obj_Indicator*ACTIVE SET 0 \0");
						}
					}
					if(current_batsmen.get(1).getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
						if(current_batsmen.get(1).getOnStrike().equalsIgnoreCase(CricketUtil.YES)) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_2$obj_Indicator*ACTIVE SET 1 \0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_1$obj_Indicator*ACTIVE SET 0 \0");
						}	
					}
				}
			}
		}
			
		infobar.setLast_batsmen(current_batsmen);
	}
	
	public void populateVizInfobarBowler(List<PrintWriter> print_writers, MatchAllData matchAllData,int WhichSide) {
		
		for(PrintWriter print_writer : print_writers) {
			for(Inning inn : matchAllData.getMatch().getInning()) {
				if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
					for(BowlingCard boc : inn.getBowlingCard()) {
						if(boc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.CURRENT+CricketUtil.BOWLER)
								|| boc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.LAST+CricketUtil.BOWLER)) {
							if(infobar.getLast_bowler() == null || infobar.getLast_bowler().getPlayerId() != boc.getPlayerId()) {
								//Bowler Animate Out
							}
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bowl_Part$txt_Bowler*GEOM*TEXT SET " + 
									boc.getPlayer().getTicker_name() + "\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bowl_Part$txt_Wickets*GEOM*TEXT SET " + 
									boc.getWickets() + slashOrDash + boc.getRuns() + "\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bowl_Part$txt_Overs*GEOM*TEXT SET " + 
									CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + "\0");
							
							if(infobar.getLast_bowler() == null || infobar.getLast_bowler().getPlayerId() != boc.getPlayerId()) {
								//Bowler Animate In
							}
							infobar.setLast_bowler(boc);
						}
					}
				}
			}
		}
	}
	public void populateVizInfobarRightBottom(List<PrintWriter> print_writers, MatchAllData matchAllData,int WhichSide) {
		
		for(PrintWriter print_writer : print_writers) {
			switch(infobar.getRight_bottom().toUpperCase()) {
			case CricketUtil.OVER:
				for(Inning inn : matchAllData.getMatch().getInning()) {
					if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)){	
						for(BowlingCard boc : inn.getBowlingCard()) {
							if(boc.getStatus().equalsIgnoreCase(CricketUtil.CURRENT+CricketUtil.BOWLER)) {
								infobar.setPlayer_id(boc.getPlayerId());
							}
						}
						String[] this_over = CricketFunctions.getEventsText(CricketUtil.OVER,infobar.getPlayer_id(),",", matchAllData.getEventFile().getEvents(),0).split(",");
						
						for(int i=0;i < this_over.length;i++) {
							if(this_over.length <= 9) {
								switch (this_over[i]) {
								case CricketUtil.DOT:
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$BowlerGrp$Section3$Section3All$ThisOver$ThisOverAll$BallGrp" + (i+1) + 
											"$SelectType*FUNCTION*Omo*vis_con SET " + "1" + " \0");
									break;
								case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR: case CricketUtil.FIVE: case CricketUtil.SIX:
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$BowlerGrp$Section3$Section3All$ThisOver$ThisOverAll$BallGrp" + (i+1) + 
											"$SelectType*FUNCTION*Omo*vis_con SET " + "2" + " \0");
									
									break;
								case "w":
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$BowlerGrp$Section3$Section3All$ThisOver$ThisOverAll$BallGrp" + (i+1) +
											"$SelectType$Wicket$txt_Wicket*GEOM*TEXT SET " + this_over[i] + "\0");
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$BowlerGrp$Section3$Section3All$ThisOver$ThisOverAll$BallGrp" + (i+1) + 
											"$SelectType*FUNCTION*Omo*vis_con SET " + "3" + " \0");
									break;

								default:
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$BowlerGrp$Section3$Section3All$ThisOver$ThisOverAll$BallGrp" + (i+1) +
											"$SelectType$Rest$img_Base1$txt_Ball*GEOM*TEXT SET " + this_over[i] + "\0");
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$BowlerGrp$Section3$Section3All$ThisOver$ThisOverAll$BallGrp" + (i+1) + 
											"$SelectType*FUNCTION*Omo*vis_con SET " + "4" + " \0");
									
									break;
								}
							}else {
								//Animate Part for having more than 6 balls in this overs
							}
						}
					}
				}
				break;
			}
			infobar.setLast_right_bottom(infobar.getRight_bottom());
		}
	}
	
	public void populateVizInfobarLeftBottom(List<PrintWriter> print_writers, MatchAllData matchAllData,int WhichSide) {
		
		for(PrintWriter print_writer : print_writers) {
			switch(infobar.getLeft_bottom().toUpperCase()) {
			case "CRR":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 2 \0");
				for(Inning inn : matchAllData.getMatch().getInning()) {
					if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						if(inn.getRunRate() == null) {
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide + 
									"$Stat$txt_Value*GEOM*TEXT SET " + "-" + "\0");
						}else {
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide + 
									"$Stat$txt_Value*GEOM*TEXT SET " + inn.getRunRate() + "\0");
						}
					}
				}
				break;
			case "TARGET":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 0 \0");
				for(Inning inn : matchAllData.getMatch().getInning()) {
					if(inn.getInningNumber() == 2 & inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide + "$Target$txt_Runs*GEOM*TEXT SET " + 
								CricketFunctions.getTargetRuns(matchAllData) + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide + "$Target$txt_Balls*GEOM*TEXT SET " + 
								(matchAllData.getSetup().getMaxOvers()*6) + "\0");
					}
				}
				break;
			}
			infobar.setLast_left_bottom(infobar.getLeft_bottom());
		}
	}
	public void populateVizInfobarMiddleSection(List<PrintWriter> print_writers, MatchAllData matchAllData,int WhichSide) throws InterruptedException {
		
		for(PrintWriter print_writer : print_writers) {
			switch(infobar.getMiddle_section().toUpperCase()) {
			case CricketUtil.BATSMAN:
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Select_Type*FUNCTION*Omo*vis_con SET 0 \0");
				populateVizInfobarBatsman(print_writers, matchAllData, 1);
				populateVizInfobarBowler(print_writers, matchAllData, 1);
				break;
			case "TARGET":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Select_Type*FUNCTION*Omo*vis_con SET 1 \0");
				for(Inning inn : matchAllData.getMatch().getInning()) {
					if(inn.getInningNumber() == 2 & inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Team*GEOM*TEXT SET " + 
								inn.getBatting_team().getTeamName1() + "\n NEED" + "\0");
						
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Runs_Value*GEOM*TEXT SET " + 
								CricketFunctions.getTargetRuns(matchAllData) + "\0");
						print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Balls_Value*GEOM*TEXT SET " + 
								(matchAllData.getSetup().getMaxOvers()*6) + "\0");
					}
				}
				break;
			case "LAST_WICKET":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Select_Type*FUNCTION*Omo*vis_con SET 18 \0");
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Last_Wicket_Wide$Top$img_Flag*TEXTURE*IMAGE SET " + 
						Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName4() + "\0");
				
				for(BattingCard bc : inning.getBattingCard()){
					if(inning.getFallsOfWickets() != null && !inning.getFallsOfWickets().isEmpty()) {
						if(inning.getFallsOfWickets().get(inning.getFallsOfWickets().size() - 1).getFowPlayerID() == bc.getPlayerId()) {
							
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Last_Wicket_Wide$Top$txt_Firstname*GEOM*TEXT SET " + 
						    		bc.getPlayer().getFirstname() + "\0");
							if(bc.getPlayer().getSurname() != null) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Last_Wicket_Wide$Top$"
										+ "txt_SecondName*GEOM*TEXT SET " + bc.getPlayer().getSurname() + "\0");
							}else {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Last_Wicket_Wide$Top$"
										+ "txt_SecondName*GEOM*TEXT SET " + "" + "\0");
							}
							
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Last_Wicket_Wide$Top$"
									+ "txt_Score*GEOM*TEXT SET " + bc.getRuns() + "\0");
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Last_Wicket_Wide$Top$"
									+ "txt_Balls*GEOM*TEXT SET " + "(" + bc.getBalls() + ")" + "\0");
							
							print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Last_Wicket_Wide$txt_HowOut*GEOM*TEXT SET " + 
						    		bc.getHowOutText() + "\0");
						}
					}
				}
				break;
			case "PROJECTED":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Select_Type*FUNCTION*Omo*vis_con SET 2 \0");
				String[] proj_score_rate = new String[CricketFunctions.projectedScore(matchAllData).size()];
			    for (int i = 0; i < CricketFunctions.projectedScore(matchAllData).size(); i++) {
			    	proj_score_rate[i] = CricketFunctions.projectedScore(matchAllData).get(i);
		        }
			    
			    print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Projected_Wide$Stat_1$txt_Desig*GEOM*TEXT SET " + 
			    		"CURRENT (" + proj_score_rate[0] + ")" + "\0");
			    print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Projected_Wide$Stat_1$txt_Fig*GEOM*TEXT SET " + 
			    		proj_score_rate[1] + "\0");
			    
			    print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Projected_Wide$Stat_2$txt_Desig*GEOM*TEXT SET " + 
			    		proj_score_rate[2] + "/OVER" + "\0");
			    print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Projected_Wide$Stat_2$txt_Fig*GEOM*TEXT SET " + 
			    		proj_score_rate[3] + "\0");
			    print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Projected_Wide$Stat_3$txt_Desig*GEOM*TEXT SET " + 
			    		proj_score_rate[4] + "/OVER" + "\0");
			    print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Projected_Wide$Stat_3$txt_Fig*GEOM*TEXT SET " + 
			    		proj_score_rate[5] + "\0");
			    print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Projected_Wide$Stat_4$txt_Desig*GEOM*TEXT SET " + 
			    		proj_score_rate[6] + "/OVER" + "\0");
			    print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Projected_Wide$Stat_4$txt_Fig*GEOM*TEXT SET " + 
			    		proj_score_rate[7] + "\0");
			    
				break;
				
			case "CURR_PARTNERSHIP":
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Select_Type*FUNCTION*Omo*vis_con SET 6 \0");
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				
				if(inning.getPartnerships() != null && inning.getPartnerships().size() > 0) {
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$txt_Title*GEOM*TEXT SET " + 
							CricketFunctions.ordinal(inning.getPartnerships().get(inning.getPartnerships().size()-1).getPartnershipNumber()) + " WICKET \n PARTNERSHIP" + "\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$Score$txt_Runs*GEOM*TEXT SET " + 
							inning.getPartnerships().get(inning.getPartnerships().size()-1).getTotalRuns() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$Score$txt_Balls*GEOM*TEXT SET " + 
							inning.getPartnerships().get(inning.getPartnerships().size()-1).getTotalBalls() + "\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$Bat_1$Batsman*GEOM*TEXT SET " + 
							inning.getPartnerships().get(inning.getPartnerships().size()-1).getFirstPlayer().getTicker_name() + "\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$Bat_1$txt_Runs*GEOM*TEXT SET " + 
							inning.getPartnerships().get(inning.getPartnerships().size()-1).getFirstBatterRuns() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$Bat_1$txt_Balls*GEOM*TEXT SET " + 
							inning.getPartnerships().get(inning.getPartnerships().size()-1).getFirstBatterBalls() + "\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$Bat_2$Batsman*GEOM*TEXT SET " + 
							inning.getPartnerships().get(inning.getPartnerships().size()-1).getSecondPlayer().getTicker_name() + "\0");
					
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$Bat_2$txt_Runs*GEOM*TEXT SET " + 
							inning.getPartnerships().get(inning.getPartnerships().size()-1).getSecondBatterRuns() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$Bat_2$txt_Balls*GEOM*TEXT SET " + 
							inning.getPartnerships().get(inning.getPartnerships().size()-1).getSecondBatterBalls() + "\0");
					
				}
				break;
			}
			infobar.setLast_middle_section(infobar.getMiddle_section());
		}
	}

}