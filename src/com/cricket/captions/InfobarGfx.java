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
import com.cricket.model.Team;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;

public class InfobarGfx 
{
	String slashOrDash = "-";
	public Infobar infobar = new Infobar();
	public Animation this_animation = new Animation();
	
	Inning inning = new Inning();
	Team team = new Team();
	List<BattingCard> battingCardList = new ArrayList<BattingCard>();
	List<BowlingCard> bowlingCardList = new ArrayList<BowlingCard>();
	List<String> this_data_str = new ArrayList<String>();
	
	public boolean updateInfobar(List<PrintWriter> print_writers,Configuration config,MatchAllData matchAllData) throws InterruptedException {
		
		populateInfobarTeamNameScore(true,print_writers,config,matchAllData);
		populateCurrentBatsmen(print_writers, matchAllData, 1);
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
		if(populateInfobarTeamNameScore(false,print_writers,config,matchAllData)) {
			infobar.setMiddle_section(CricketUtil.EXTRAS);
			infobar.setLeft_bottom("TOSS");
			populateVizInfobarLeftBottom(print_writers, matchAllData, 1);
			populateVizInfobarMiddleSection(print_writers, matchAllData, 1);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean populateInfobarTeamNameScore(boolean is_this_updating,List<PrintWriter> print_writers,Configuration config,MatchAllData matchAllData) {
		
		String Overs_omo = "";
		switch(config.getBroadcaster()) {
		case Constants.ICC_U19_2023:

			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
	
			if(inning == null) {
				return false;
			}
			
			if(is_this_updating == false) {
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Flag_Left$img_Flag*TEXTURE*IMAGE SET " + 
						Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Flag_Right$img_Flag*TEXTURE*IMAGE SET " + 
						Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Top$Scorebox$txt_Team_2*GEOM*TEXT SET " + 
						inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Flag_Right$img_Flag*TEXTURE*IMAGE SET " + 
						Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() + "\0", print_writers);
				
			}
			if(!CricketFunctions.processPowerPlay(CricketUtil.MINI,matchAllData).isEmpty()) {
				 CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Top$Powerplay$txt_Powerplay*GEOM*TEXT SET " + 
						 CricketFunctions.processPowerPlay(CricketUtil.MINI,matchAllData) + "\0", print_writers);
				 if(!infobar.isPowerplay_on_screen() ) {
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
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Top$Scorebox$txt_Score*GEOM*TEXT SET " + 
					CricketFunctions.getTeamScore(inning, slashOrDash, false) + "\0", print_writers);
			
			if(matchAllData.getSetup().getTargetOvers() != "") {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Top$Overs$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Top$Overs_with_Reduced$txt_Overs*GEOM*TEXT SET " + 
						matchAllData.getSetup().getTargetOvers() + "\0", print_writers);
				Overs_omo = "Overs_with_Reduced";
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Top$Overs$Choose_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				Overs_omo = "Overs_Only";
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Top$" + Overs_omo + "$txt_Overs*GEOM*TEXT SET " + 
					CricketFunctions.OverBalls(inning.getTotalOvers(),inning.getTotalBalls()) + "\0", print_writers);
			
			break;
		}
		return true;
	}
	
//	public void populateVizInfobarBatsman(List<PrintWriter> print_writers,MatchAllData matchAllData,int WhichSide) throws InterruptedException
//	{ 
//		List<BattingCard> current_batsmen = new ArrayList<BattingCard>();
//		for(PrintWriter print_writer : print_writers) {
//			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
//			for (BattingCard bc : inning.getBattingCard()) {
//				if(inning.getPartnerships() != null && inning.getPartnerships().size() > 0) {
//					if(bc.getPlayerId() == inning.getPartnerships().get(inning.getPartnerships().size() - 1).getFirstBatterNo()) {
//						current_batsmen.add(bc);
//					} else if(bc.getPlayerId() == inning.getPartnerships().get(inning.getPartnerships().size() - 1).getSecondBatterNo()) {
//						current_batsmen.add(bc);
//					}
//				}
//			}
//			
//			if(infobar.getLast_batsmen() == null || infobar.getLast_batsmen().size() <= 0) {
//				infobar.setLast_batsmen(current_batsmen);
//			}
//			populateCurrentBatsmen(print_writer, matchAllData,WhichSide,current_batsmen);
//		}
//	}
	public boolean populateCurrentBatsmen(List<PrintWriter> print_writers, MatchAllData matchAllData,int WhichSide) throws InterruptedException {
		
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
			inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
		
		if(inning == null) {
			return false;
		}
		
		if(inning.getPartnerships() != null && inning.getPartnerships().size() <= 0) {
			return false;
		}
		
		battingCardList = new ArrayList<BattingCard>();
		for(int iBat = 1; iBat <= 2; iBat++) {
			battingCardList.add(inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == 
				inning.getPartnerships().get(inning.getPartnerships().size() - 1).getFirstBatterNo()).findAny().orElse(null));
			if(battingCardList.get(battingCardList.size()-1) == null) {
				return false;
			}
		}
		
		if(infobar.getLast_batsmen() != null && infobar.getLast_batsmen().size() >= 1) {
			if(infobar.getLast_batsmen().get(0).getPlayerId() != battingCardList.get(0).getPlayerId()) {

				this_animation.processAnimation(print_writers, "Batsman1Out", "START");
				TimeUnit.MILLISECONDS.sleep(800);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_1$Batsman*GEOM*TEXT SET " + 
					battingCardList.get(0).getPlayer().getTicker_name() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_1$txt_Runs*GEOM*TEXT SET " + 
					battingCardList.get(0).getRuns() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_1$txt_Balls*GEOM*TEXT SET " + 
					battingCardList.get(0).getBalls() + "\0", print_writers);
				
				this_animation.processAnimation(print_writers, "Batsman1In", "START");
				this_animation.processAnimation(print_writers, "Bat1DelhighlightOut", "SHOW 0.180");
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_1$Batsman*GEOM*TEXT SET " + 
					battingCardList.get(0).getPlayer().getTicker_name() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_1$txt_Runs*GEOM*TEXT SET " + 
					battingCardList.get(0).getRuns() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_1$txt_Balls*GEOM*TEXT SET " + 
					battingCardList.get(0).getBalls() + "\0", print_writers);
				
				if(battingCardList.get(0).getStatus().equalsIgnoreCase(CricketUtil.OUT) 
						|| battingCardList.get(0).getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) { 
					this_animation.processAnimation(print_writers, "Bat1DelhighlightIn", "SHOW 0.220");
				}
			}
			if(infobar.getLast_batsmen().get(1).getPlayerId() != battingCardList.get(1).getPlayerId()) {
				this_animation.processAnimation(print_writers, "Batsman2Out", "START");
				TimeUnit.MILLISECONDS.sleep(800);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_2$Batsman*GEOM*TEXT SET " + 
					battingCardList.get(1).getPlayer().getTicker_name() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_2$txt_Runs*GEOM*TEXT SET " + 
					battingCardList.get(1).getRuns() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_2$txt_Balls*GEOM*TEXT SET " + 
					battingCardList.get(1).getBalls() + "\0", print_writers);
				
				this_animation.processAnimation(print_writers, "Batsman2In", "START");
				this_animation.processAnimation(print_writers, "Bat2DelhighlightOut", "SHOW 0.180");
			}else {
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_2$Batsman*GEOM*TEXT SET " + 
					battingCardList.get(1).getPlayer().getTicker_name() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_2$txt_Runs*GEOM*TEXT SET " + 
					battingCardList.get(1).getRuns() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bat_and_Bowl$Bat_2$txt_Balls*GEOM*TEXT SET " + 
					battingCardList.get(1).getBalls() + "\0", print_writers);
				
				if(battingCardList.get(1).getStatus().equalsIgnoreCase(CricketUtil.OUT) 
						|| battingCardList.get(1).getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) { 
					this_animation.processAnimation(print_writers, "Bat2DelhighlightIn", "SHOW 0.220");
				}
			}
			
			if(battingCardList.get(0).getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
				if(battingCardList.get(0).getOnStrike().equalsIgnoreCase(CricketUtil.YES)) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Bat_and_Bowl$Bat_1$obj_Indicator*ACTIVE SET 1 \0",print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Bat_and_Bowl$Bat_2$obj_Indicator*ACTIVE SET 0 \0",print_writers);
				}
			}
			if(battingCardList.get(1).getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
				if(battingCardList.get(1).getOnStrike().equalsIgnoreCase(CricketUtil.YES)) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Bat_and_Bowl$Bat_2$obj_Indicator*ACTIVE SET 1 \0",print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Bat_and_Bowl$Bat_1$obj_Indicator*ACTIVE SET 0 \0",print_writers);
				}	
			}
		}
		infobar.setLast_batsmen(battingCardList);
		return true;
	}
	
	public boolean populateVizInfobarBowler(List<PrintWriter> print_writers, MatchAllData matchAllData,int WhichSide) {

		inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
			inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
	
		if(inning == null) {
			return false;
		}
		
		bowlingCardList = new ArrayList<BowlingCard>();
		bowlingCardList.add(inning.getBowlingCard().stream().filter(boc -> boc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.CURRENT+CricketUtil.BOWLER)
				|| boc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.LAST+CricketUtil.BOWLER)).findAny().orElse(null));
		if(bowlingCardList.get(bowlingCardList.size()-1) == null) {
			return false;
		}

		if(infobar.getLast_bowler() == null || infobar.getLast_bowler().getPlayerId() != bowlingCardList.get(bowlingCardList.size()-1).getPlayerId()) {
			//Bowler Animate Out
		}
		CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bowl_Part$txt_Bowler*GEOM*TEXT SET " + 
			bowlingCardList.get(bowlingCardList.size()-1).getPlayer().getTicker_name() + "\0", print_writers);
		CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bowl_Part$txt_Wickets*GEOM*TEXT SET " + 
			bowlingCardList.get(bowlingCardList.size()-1).getWickets() + slashOrDash + bowlingCardList.get(bowlingCardList.size()-1).getRuns() + "\0", print_writers);
		CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Bowl_Part$txt_Overs*GEOM*TEXT SET " + 
			CricketFunctions.OverBalls(bowlingCardList.get(bowlingCardList.size()-1).getOvers(), bowlingCardList.get(bowlingCardList.size()-1).getBalls()) + "\0", print_writers);
		
		if(infobar.getLast_bowler() == null || infobar.getLast_bowler().getPlayerId() != bowlingCardList.get(bowlingCardList.size()-1).getPlayerId()) {
			//Bowler Animate In
		}
		infobar.setLast_bowler(bowlingCardList.get(bowlingCardList.size()-1));
		
		return true;
	}
	public boolean populateVizInfobarRightBottom(List<PrintWriter> print_writers, MatchAllData matchAllData,int WhichSide) {
		
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
		inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);

		if(inning == null) {
			return false;
		}
		
		this_data_str = new ArrayList<String>();
		this_data_str.add(CricketFunctions.getEventsText(CricketUtil.OVER,infobar.getPlayer_id(),",", matchAllData.getEventFile().getEvents(),0));
		if(this_data_str.get(this_data_str.size()-1) == null || this_data_str.get(this_data_str.size()-1).isEmpty()
			|| this_data_str.get(this_data_str.size()-1).split(",").length <= 0) {
			return false;
		}
		
		for(int iBall = 0; iBall < this_data_str.get(this_data_str.size()-1).split(",").length; iBall++) {
			if(iBall <= 8) {
				switch (this_data_str.get(this_data_str.size()-1).split(",")[iBall]) {
				case CricketUtil.DOT:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$BowlerGrp$Section3$Section3All$ThisOver$ThisOverAll$BallGrp" + (iBall + 1) + 
							"$SelectType*FUNCTION*Omo*vis_con SET " + "1" + "\0", print_writers);
					break;
				case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR: case CricketUtil.FIVE: case CricketUtil.SIX:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$BowlerGrp$Section3$Section3All$ThisOver$ThisOverAll$BallGrp" + (iBall + 1) + 
							"$SelectType*FUNCTION*Omo*vis_con SET " + "2" + "\0", print_writers);
					
					break;
				case "w":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$BowlerGrp$Section3$Section3All$ThisOver$ThisOverAll$BallGrp" + (iBall + 1) +
							"$SelectType$Wicket$txt_Wicket*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[iBall] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$BowlerGrp$Section3$Section3All$ThisOver$ThisOverAll$BallGrp" + (iBall + 1) + 
							"$SelectType*FUNCTION*Omo*vis_con SET " + "3" + "\0", print_writers);
					break;

				default:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$BowlerGrp$Section3$Section3All$ThisOver$ThisOverAll$BallGrp" + (iBall + 1) +
							"$SelectType$Rest$img_Base1$txt_Ball*GEOM*TEXT SET " + this_data_str.get(this_data_str.size()-1).split(",")[iBall] + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$BowlerGrp$Section3$Section3All$ThisOver$ThisOverAll$BallGrp" + (iBall + 1) + 
							"$SelectType*FUNCTION*Omo*vis_con SET " + "4" + "\0", print_writers);
					
					break;
				}
			}else {
				//Animate Part for having more than 6 balls in this overs
			}
		}
		return true;
	}
	
	public boolean populateVizInfobarLeftBottom(List<PrintWriter> print_writers, MatchAllData matchAllData,int WhichSide) {
		
		switch(infobar.getLeft_bottom().toUpperCase()) {
		case "CRR":
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" 
				+ WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide + "$Stat$txt_Title*GEOM*TEXT SET " + 
					"CURRENT RUN RATE" + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide + 
				"$Stat$txt_Value*GEOM*TEXT SET " + CricketFunctions.generateRunRate(CricketFunctions.getRequiredRuns(matchAllData), 
				0, CricketFunctions.getRequiredBalls(matchAllData), 2,matchAllData) + "\0", print_writers);
			
			break;
		case "TOSS":
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" 
				+ WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
			if(matchAllData.getSetup().getHomeTeamId() == matchAllData.getSetup().getTossWinningTeam()) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
					+ "$Free_Text$txt_Text*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamName4() + " WON THE TOSS" + "\0", print_writers);
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
					+ "$Free_Text$txt_Text*GEOM*TEXT SET " + matchAllData.getSetup().getAwayTeam().getTeamName4() + " WON THE TOSS" + "\0", print_writers);
			}
			break;
		case "RRR":
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
				+ "$Choose_Type*FUNCTION*Omo*vis_con SET 2 \0",print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide + "$Stat$txt_Title*GEOM*TEXT SET " + 
				"REQUIRED RUN RATE" + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
				+ "$Stat$txt_Value*GEOM*TEXT SET " + CricketFunctions.generateRunRate(CricketFunctions.getRequiredRuns(matchAllData),
				0,CricketFunctions.getRequiredBalls(matchAllData),2,matchAllData) + "\0", print_writers);
			break;
		case "TARGET":
			if(matchAllData.getMatch().getInning().size() < 2) {
				return false;
			}
			if(matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
				return false;
			}
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
				+ "$Choose_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
				+ "$Stat$txt_Title*GEOM*TEXT SET " + "TARGET" + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" + WhichSide 
				+ "$Stat$txt_Value*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + "\0", print_writers);
			break;
		}
		infobar.setLast_left_bottom(infobar.getLeft_bottom());
		return true;
	}
	public boolean populateVizInfobarMiddleSection(List<PrintWriter> print_writers, MatchAllData matchAllData,int WhichSide) throws InterruptedException {
		
		switch(infobar.getMiddle_section().toUpperCase()) {
		case CricketUtil.BATSMAN:
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ "$Select_Type*FUNCTION*Omo*vis_con SET 0 \0",print_writers);
			populateCurrentBatsmen(print_writers, matchAllData, 1);
			populateVizInfobarBowler(print_writers, matchAllData, 1);
			break;
		case "TARGET":
			if(matchAllData.getMatch().getInning().size() < 2) {
				return false;
			}
			if(matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
				return false;
			}
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ "$Select_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Team*GEOM*TEXT SET " + 
					inning.getBatting_team().getTeamName1() + "\n NEED" + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Runs_Value*GEOM*TEXT SET " + 
					CricketFunctions.getTargetRuns(matchAllData) + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Balls_Value*GEOM*TEXT SET " + 
					(matchAllData.getSetup().getMaxOvers()*6) + "\0", print_writers);
			break;
		case "EQUATION":
			if(matchAllData.getMatch().getInning().size() < 2) {
				return false;
			}
			if(matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
				return false;
			}
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ "$Select_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Team*GEOM*TEXT SET " + 
					inning.getBatting_team().getTeamName1() + "\n NEED" + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Runs_Value*GEOM*TEXT SET " + 
					CricketFunctions.getRequiredRuns(matchAllData) + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Target$txt_Balls_Value*GEOM*TEXT SET " + 
					CricketFunctions.getRequiredBalls(matchAllData) + "\0", print_writers);
			break;

		case CricketUtil.EXTRAS:
		
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			if(inning == null) {
				return false;
			}
			
			for(int i = 1; i <= 4; i++) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
					+ "$Stats_Wide$Stats$Stat_" + i + "*ACTIVE SET 1 \0",print_writers);
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ "$Select_Type*FUNCTION*Omo*vis_con SET 9 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stat$txt_Title*GEOM*TEXT SET " + 
					CricketUtil.EXTRAS + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_1$txt_Desig*GEOM*TEXT SET " + 
					"WIDE" + CricketFunctions.Plural(inning.getTotalWides()).toUpperCase() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_1$txt_Fig*GEOM*TEXT SET " + 
					inning.getTotalWides() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_2$txt_Desig*GEOM*TEXT SET " + 
					"NO BALL" + CricketFunctions.Plural(inning.getTotalNoBalls()).toUpperCase() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_2$txt_Fig*GEOM*TEXT SET " + 
					inning.getTotalNoBalls() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_3$txt_Desig*GEOM*TEXT SET " + 
					"LEG BYE" + CricketFunctions.Plural(inning.getTotalLegByes()).toUpperCase() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_3$txt_Fig*GEOM*TEXT SET " + 
					inning.getTotalLegByes() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_4$txt_Desig*GEOM*TEXT SET " + 
					"BYE" + CricketFunctions.Plural(inning.getTotalByes()).toUpperCase() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_4$txt_Fig*GEOM*TEXT SET " + 
					inning.getTotalByes() + "\0", print_writers);
			
			if(inning.getTotalPenalties() != 0) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Stats_Wide$Stats$Stat_5*ACTIVE SET 1 \0",print_writers);
				if(inning.getTotalPenalties() == 1) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Stats_Wide$Stats$Stat_5$txt_Desig*GEOM*TEXT SET PENALTY\0", print_writers);
				} else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Stats_Wide$Stats$Stat_5$txt_Desig*GEOM*TEXT SET PENALTIES\0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats_Wide$Stats$Stat_5$txt_Fig*GEOM*TEXT SET " + 
					inning.getTotalPenalties() + "\0", print_writers);
			} else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Stats_Wide$Stats$Stat_5*ACTIVE SET 1 \0",print_writers);
			}
			
			//Hide unused stats
			for(int i = 6; i <= 10; i++) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
					+ "$Stats_Wide$Stats$Stat_" + i + "*ACTIVE SET 0 \0",print_writers);
			}
			
			break;
			
		case CricketUtil.COMPARE:
			
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)
				&& inn.getInningNumber() == 2).findAny().orElse(null);
			
			if(inning == null) {
				return false;
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ "$Select_Type*FUNCTION*Omo*vis_con SET 8 \0",print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ "$Stats$Stat$txt_Title*GEOM*TEXT SET " + "AT THIS\nSTAGE" + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ "$Stats$Top$txt_Desig*GEOM*TEXT SET " + inning.getBowling_team().getTeamName1() + " WERE" + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats$Bottom$txt_Desig*GEOM*TEXT SET " + 
				inning.getBatting_team().getTeamName1() + " WERE" + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats$Top$txt_Fig*GEOM*TEXT SET " + 
					CricketFunctions.compareInningData(matchAllData, "-", 1, matchAllData.getEventFile().getEvents()) + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Stats$Bottom$txt_Fig*GEOM*TEXT SET " + 
					CricketFunctions.getTeamScore(inning, "-", false) + "\0", print_writers);
			
			break;
			
		case "LAST_WICKET":

			inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
				inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				
			if(inning == null) {
				return false;
			}

			if(inning.getFallsOfWickets() != null && !inning.getFallsOfWickets().isEmpty()) {
				return false;
			}
			
			battingCardList.add(inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == 
				inning.getFallsOfWickets().get(inning.getFallsOfWickets().size() - 1).getFowPlayerID()).findAny().orElse(null));

			if(battingCardList.get(battingCardList.size()-1) == null) {
				return false;
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ "$Select_Type*FUNCTION*Omo*vis_con SET 18 \0",print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Last_Wicket_Wide$Top$img_Flag*TEXTURE*IMAGE SET " + 
					Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);

			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ "$Last_Wicket_Wide$Top$txt_Firstname*GEOM*TEXT SET " + battingCardList.get(battingCardList.size()-1).getPlayer().getFirstname() + "\0", print_writers);
			if(battingCardList.get(battingCardList.size()-1).getPlayer().getSurname() != null) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Last_Wicket_Wide$Top$"
					+ "txt_SecondName*GEOM*TEXT SET " + battingCardList.get(battingCardList.size()-1).getPlayer().getSurname() + "\0", print_writers);
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Last_Wicket_Wide$Top$"
					+ "txt_SecondName*GEOM*TEXT SET \0", print_writers);
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Last_Wicket_Wide$Top$"
				+ "txt_Score*GEOM*TEXT SET " + battingCardList.get(battingCardList.size()-1).getRuns() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Last_Wicket_Wide$Top$"
				+ "txt_Balls*GEOM*TEXT SET " + "(" + battingCardList.get(battingCardList.size()-1).getBalls() + ")" + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ "$Last_Wicket_Wide$txt_HowOut*GEOM*TEXT SET " + battingCardList.get(battingCardList.size()-1).getHowOutText() + "\0", print_writers);
	
			break;
			
		case "PROJECTED":
			
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == 1 &&
				inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			
			if(inning == null) {
				return false;
			}
			
			this_data_str = CricketFunctions.projectedScore(matchAllData);
			if(this_data_str.size() <= 0) {
				return false;
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
				return false;
			}
			
			if(inning.getPartnerships() != null && inning.getPartnerships().size() <= 0) {
				return false;
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ "$Select_Type*FUNCTION*Omo*vis_con SET 6 \0",print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$txt_Title*GEOM*TEXT SET " + 
					CricketFunctions.ordinal(inning.getPartnerships().get(inning.getPartnerships().size()-1).getPartnershipNumber()) + " WICKET \n PARTNERSHIP" + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$Score$txt_Runs*GEOM*TEXT SET " + 
					inning.getPartnerships().get(inning.getPartnerships().size()-1).getTotalRuns() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$Score$txt_Balls*GEOM*TEXT SET " + 
					inning.getPartnerships().get(inning.getPartnerships().size()-1).getTotalBalls() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$Bat_1$Batsman*GEOM*TEXT SET " + 
					inning.getPartnerships().get(inning.getPartnerships().size()-1).getFirstPlayer().getTicker_name() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$Bat_1$txt_Runs*GEOM*TEXT SET " + 
					inning.getPartnerships().get(inning.getPartnerships().size()-1).getFirstBatterRuns() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$Bat_1$txt_Balls*GEOM*TEXT SET " + 
					inning.getPartnerships().get(inning.getPartnerships().size()-1).getFirstBatterBalls() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$Bat_2$Batsman*GEOM*TEXT SET " + 
					inning.getPartnerships().get(inning.getPartnerships().size()-1).getSecondPlayer().getTicker_name() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$Bat_2$txt_Runs*GEOM*TEXT SET " + 
					inning.getPartnerships().get(inning.getPartnerships().size()-1).getSecondBatterRuns() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Partnership_Wide$Bat_2$txt_Balls*GEOM*TEXT SET " + 
					inning.getPartnerships().get(inning.getPartnerships().size()-1).getSecondBatterBalls() + "\0", print_writers);
			break;
		}
		infobar.setLast_middle_section(infobar.getMiddle_section());
		return true;
	}
}