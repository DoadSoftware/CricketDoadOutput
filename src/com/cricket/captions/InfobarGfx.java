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
	public Configuration config;
	public String slashOrDash, containerName = "", status = "";

	public Inning inning = new Inning();
	public Team team = new Team();
	public Infobar infobar = new Infobar();
	public Animation this_animation = new Animation();

	public List<PrintWriter> print_writers; 
	public List<BattingCard> battingCardList = new ArrayList<BattingCard>();
	public List<BowlingCard> bowlingCardList = new ArrayList<BowlingCard>();
	public List<String> this_data_str = new ArrayList<String>();
	
	public InfobarGfx() {
		super();
	}

	public InfobarGfx(Configuration config, String slashOrDash, List<PrintWriter> print_writers) {
		super();
		this.config = config;
		this.slashOrDash = slashOrDash;
		this.print_writers = print_writers;
	}

	public String updateInfobar(List<PrintWriter> print_writers,MatchAllData matchAllData) throws InterruptedException {

		switch (config.getBroadcaster()) {
		case Constants.ICC_U19_2023:
			
			populateInfobarTeamNameScore(true,print_writers,matchAllData);
			populateCurrentBatsmen(print_writers, matchAllData, 1);
			populateVizInfobarBowler(print_writers, matchAllData, 1);
			populateVizInfobarLeftBottom(print_writers, matchAllData, 1);
			populateVizInfobarMiddleSection(print_writers, matchAllData, 1);
			break;
		}
		return Constants.OK;
	}
	
	public String populateInfobar(List<PrintWriter> print_writers,String whatToProcess,
		MatchAllData matchAllData) throws InterruptedException {
		
		switch (config.getBroadcaster()) {
		case Constants.ICC_U19_2023:
			
			status = populateInfobarTeamNameScore(false,print_writers,matchAllData);
			if(status == Constants.OK) {
				this.infobar.setLeft_bottom(whatToProcess.split(",")[3]);
				status = populateVizInfobarLeftBottom(print_writers,matchAllData,1);
				if(status == Constants.OK) {
					this.infobar.setMiddle_section(whatToProcess.split(",")[2]);
					populateVizInfobarMiddleSection(print_writers, matchAllData, 1);
				} else {
					return status;
				}
			} else {
				return status;
			}
		}
		return Constants.OK;
	}
	
	public String populateInfobarTeamNameScore(boolean is_this_updating,List<PrintWriter> print_writers,MatchAllData matchAllData) {
		
		switch(config.getBroadcaster()) {
		case Constants.ICC_U19_2023:

			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
			if(inning == null) {
				return "populateInfobarTeamNameScore: Inning return is NULL";
			}
			
			if(is_this_updating == false) {
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Flag_Left$img_Flag*TEXTURE*IMAGE SET " + 
						Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Flag_Right$img_Flag*TEXTURE*IMAGE SET " + 
						Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Top$txt_Team_1*GEOM*TEXT SET " + 
						inning.getBowling_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Top$txt_v*GEOM*TEXT SET v \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Top$Scorebox$txt_Team_2*GEOM*TEXT SET " + 
						inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Flag_Right$img_Flag*TEXTURE*IMAGE SET " + 
						Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() + "\0", print_writers);
				
			}
			if(!CricketFunctions.processPowerPlay(CricketUtil.MINI,matchAllData).isEmpty()) {
				 CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Top$Powerplay$txt_Powerplay*GEOM*TEXT SET " + 
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
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Scorebox$txt_Score*GEOM*TEXT SET " + 
					CricketFunctions.getTeamScore(inning, slashOrDash, false) + "\0", print_writers);
			
			if((inning.getTotalOvers()* Integer.valueOf(matchAllData.getSetup().getBallsPerOver())+inning.getTotalBalls() <= 59)){
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Overs$Choose_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				containerName = "Overs_Only";
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Overs$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				containerName = "Overs_with_Reduced";
			}
			
			if(matchAllData.getSetup().getTargetOvers() != "") {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$" + containerName + "$txt_Reduced*GEOM*TEXT SET " + 
						matchAllData.getSetup().getTargetOvers() + "\0", print_writers);
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$" + containerName + "$txt_Reduced*GEOM*TEXT SET " + 
						"OVERS" + "\0", print_writers);
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$" + containerName + "$txt_Overs*GEOM*TEXT SET " + 
				CricketFunctions.OverBalls(inning.getTotalOvers(),inning.getTotalBalls()) + "\0", print_writers);
			
			break;
		}
		return Constants.OK;
	}
	public void populateTwoBatsmenSingleBatsman(List<PrintWriter> print_writers, MatchAllData matchAllData,
			int WhichSide, int WhichSubSide, int WhichBatsman, List<BattingCard> battingCardList) throws InterruptedException {
	
		switch(config.getBroadcaster()) {
		case Constants.ICC_U19_2023:
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ "$Bat_and_Bowl$Bat_" + WhichBatsman + "$Side" + WhichSubSide + "$Batsman*GEOM*TEXT SET " 
				+ battingCardList.get(WhichBatsman-1).getPlayer().getTicker_name() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ "$Bat_and_Bowl$Bat_" + WhichBatsman + "$Side" + WhichSubSide + "$txt_Runs*GEOM*TEXT SET " 
				+ battingCardList.get(WhichBatsman-1).getRuns() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ "$Bat_and_Bowl$Bat_" + WhichBatsman + "$Side" + WhichSubSide + "$txt_Balls*GEOM*TEXT SET " 
				+ battingCardList.get(WhichBatsman-1).getBalls() + "\0", print_writers);
			
			if(battingCardList.get(0).getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bat_" + WhichBatsman + "_Lowlight", "SHOW 0.0");
			} else {
				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bat_" + WhichBatsman + "_Lowlight", "SHOW 0.4");
			}
			
			if(battingCardList.get(0).getOnStrike().equalsIgnoreCase(CricketUtil.YES)) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
					+ "$Bat_and_Bowl$Bat_" + WhichBatsman + "$Side" + WhichSubSide + "$obj_Indicator*ACTIVE SET 1 \0",print_writers);
			} else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
					+ "$Bat_and_Bowl$Bat_" + WhichBatsman + "$Side" + WhichSubSide + "$obj_Indicator*ACTIVE SET 0 \0",print_writers);
			}
			break;
		}
	}
	public String populateCurrentBatsmen(List<PrintWriter> print_writers, MatchAllData matchAllData,int WhichSide) 
			throws InterruptedException 
	{
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
			inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
		if(inning == null) {
			return "populateCurrentBatsmen: Inning return is NULL";
		}
		
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
		case Constants.ICC_U19_2023:
			if(infobar.getLast_batsmen() != null && infobar.getLast_batsmen().size() >= 2) {
				if(infobar.getLast_batsmen().get(0).getPlayerId() != battingCardList.get(0).getPlayerId()) {
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, 1, 2, 1, battingCardList);
					this_animation.processAnimation(Constants.FRONT, print_writers, "Bat_1_Change", "START");
					TimeUnit.MILLISECONDS.sleep(800);
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, 1, 1, 1, battingCardList);
					this_animation.processAnimation(Constants.FRONT, print_writers, "Bat_1_Change", "SHOW 0.0");
				} else {
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, 1, 1, 1, battingCardList);
				}
				if(infobar.getLast_batsmen().get(1).getPlayerId() != battingCardList.get(1).getPlayerId()) {
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, 1, 2, 2, battingCardList);
					this_animation.processAnimation(Constants.FRONT, print_writers, "Bat_2_Change", "START");
					TimeUnit.MILLISECONDS.sleep(800);
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, 1, 1, 2, battingCardList);
					this_animation.processAnimation(Constants.FRONT, print_writers, "Bat_2_Change", "SHOW 0.0");
				} else {
					populateTwoBatsmenSingleBatsman(print_writers, matchAllData, 1, 1, 2, battingCardList);
				}
			} else {
				populateTwoBatsmenSingleBatsman(print_writers, matchAllData, 1, 1, 1, battingCardList);
				populateTwoBatsmenSingleBatsman(print_writers, matchAllData, 1, 1, 2, battingCardList);
			}
			infobar.setLast_batsmen(battingCardList);
			break;
		}
		return Constants.OK;
	}
	public void populateTwoBatsmenBowler(List<PrintWriter> print_writers, MatchAllData matchAllData,
			int WhichSide, int WhichSubSide, List<BowlingCard> bowlingCardList) throws InterruptedException {
	
		switch(config.getBroadcaster()) {
		case Constants.ICC_U19_2023:
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ "$Bowl_Part$Side" + WhichSubSide + "$txt_Bowler*GEOM*TEXT SET " + 
				bowlingCardList.get(bowlingCardList.size()-1).getPlayer().getTicker_name() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ "$Bowl_Part$Side" + WhichSubSide + "$txt_Wickets*GEOM*TEXT SET " + bowlingCardList.get(bowlingCardList.size()-1).getWickets() 
				+ slashOrDash + bowlingCardList.get(bowlingCardList.size()-1).getRuns() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
				+ "$Bowl_Part$Side" + WhichSubSide + "$txt_Overs*GEOM*TEXT SET " + CricketFunctions.OverBalls(
				bowlingCardList.get(bowlingCardList.size()-1).getOvers(), bowlingCardList.get(bowlingCardList.size()-1).getBalls()) + "\0", print_writers);
			
			if(bowlingCardList.get(0).getStatus().equalsIgnoreCase(CricketUtil.CURRENT + CricketUtil.BOWLER)) {
				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bowl_Lowlight", "SHOW 0.0");
			} else {
				this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bowl_Lowlight", "SHOW 0.4");
			}
	
			break;
		}
	}
	public String populateVizInfobarBowler(List<PrintWriter> print_writers, MatchAllData matchAllData,int WhichSide) throws InterruptedException {

		inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
			inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
	
		if(inning == null) {
			return "populateVizInfobarBowler: Inning return is NULL";
		}
		
		bowlingCardList = new ArrayList<BowlingCard>();
		bowlingCardList.add(inning.getBowlingCard().stream().filter(boc -> boc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.CURRENT+CricketUtil.BOWLER)
			|| boc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.LAST+CricketUtil.BOWLER)).findAny().orElse(null));
		
		if(bowlingCardList.get(bowlingCardList.size()-1) == null) {
			return "populateVizInfobarBowler: no current bowler found";
		}
		
		switch(config.getBroadcaster()) {
		case Constants.ICC_U19_2023:
			if(infobar.getLast_bowler() != null) {
				if(infobar.getLast_bowler().getPlayerId() != bowlingCardList.get(bowlingCardList.size()-1).getPlayerId()) {
					populateTwoBatsmenBowler(print_writers, matchAllData, 1, 2, bowlingCardList);
					this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bowl_Change", "START");
					TimeUnit.MILLISECONDS.sleep(800);
					populateTwoBatsmenBowler(print_writers, matchAllData, 1, 1, bowlingCardList);
					this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bowl_Change", "SHOW 0.0");
				} else {
					populateTwoBatsmenBowler(print_writers, matchAllData, 1, 1, bowlingCardList);
				}
			} else {
				populateTwoBatsmenBowler(print_writers, matchAllData, 1, 1, bowlingCardList);
			}
			
			infobar.setLast_bowler(bowlingCardList.get(bowlingCardList.size()-1));
			break;
		}
		
		return Constants.OK;
	}
	public String populateVizInfobarRightBottom(List<PrintWriter> print_writers, MatchAllData matchAllData,
		int WhichSide) throws InterruptedException 
	{
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
			inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);

		if(inning == null) {
			return "populateVizInfobarRightBottom: Inning return is NULL";
		}
		
		switch(config.getBroadcaster()) {
		case Constants.ICC_U19_2023:
			
			this_data_str = new ArrayList<String>();
			this_data_str.add(CricketFunctions.getEventsText(CricketUtil.OVER,infobar.getPlayer_id(),",", matchAllData.getEventFile().getEvents(),0));
			
			if(this_data_str.get(this_data_str.size()-1) == null || this_data_str.get(this_data_str.size()-1).split(",").length > 8) {
				return "populateVizInfobarRightBottom: This over data returned invalid";
			}
			
			for(int iBall = 0; iBall < this_data_str.get(this_data_str.size()-1).split(",").length; iBall++) {
				switch (this_data_str.get(this_data_str.size()-1).split(",")[iBall].toUpperCase()) {
				case CricketUtil.DOT:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$BowlerGrp$Section3$Section3All$ThisOver$ThisOverAll$BallGrp" + (iBall + 1) + 
							"$SelectType*FUNCTION*Omo*vis_con SET " + "1" + "\0", print_writers);
					break;
				case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR: case CricketUtil.FIVE: case CricketUtil.SIX:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$BowlerGrp$Section3$Section3All$ThisOver$ThisOverAll$BallGrp" + (iBall + 1) + 
							"$SelectType*FUNCTION*Omo*vis_con SET " + "2" + "\0", print_writers);
					
					break;
				case "W":
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
			}
			if(this_data_str.get(this_data_str.size()-1).split(",").length > 6) {
				if(infobar.getLast_this_over() != null && infobar.getLast_this_over().split(",").length <= 6) {
					this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$This_Over_Title_Fade_Out", "START");
				}
			} else {
				if(infobar.getLast_this_over() != null && infobar.getLast_this_over().split(",").length > 6) {
					this_animation.processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$This_Over_Title_Fade_Out", "CONTINUE REVERSE");
				}
			}
			infobar.setLast_this_over(this_data_str.get(this_data_str.size()-1));
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
				
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == 1 && 
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
				
			case "TOSS":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$LeftALL$Data_Left$Bottom$Side_" 
					+ WhichSide + "$Choose_Type*FUNCTION*Omo*vis_con SET 1 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Bottom$Side_" + WhichSide + "$Free_Text$txt_Text*GEOM*TEXT SET " 
					+ CricketFunctions.generateTossResult(matchAllData, CricketUtil.FULL, "", CricketUtil.SHORT, null).toUpperCase() + "\0", print_writers);
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
				
				if(!matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					return "populateVizInfobarLeftBottom: Target available in 2nd inning only";
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
			break;
		}
		
		return Constants.OK;
	}
	public String populateVizInfobarMiddleSection(List<PrintWriter> print_writers, MatchAllData matchAllData, int WhichSide) throws InterruptedException 
	{
		switch(config.getBroadcaster()) {
		case Constants.ICC_U19_2023:

			switch(infobar.getMiddle_section().toUpperCase()) {
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
				populateCurrentBatsmen(print_writers, matchAllData, WhichSide);
				populateVizInfobarBowler(print_writers, matchAllData, WhichSide);
				break;
				
			case "TARGET":
				
				if(!matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					return "populateVizInfobarLeftBottom: Target available in 2nd inning only";
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
				
				if(!matchAllData.getMatch().getInning().get(1).getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					return "populateVizInfobarLeftBottom: Target available in 2nd inning only";
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
					return "populateVizInfobarLeftBottom: Inning returned is NULL";
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
				
				break;
				
			case CricketUtil.COMPARE:
				
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)
					&& inn.getInningNumber() == 2).findAny().orElse(null);
				
				if(inning == null) {
					return "populateVizInfobarLeftBottom: 2nd Inning returned is NULL";
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
					CricketFunctions.getTeamScore(inning, slashOrDash, false) + "\0", print_writers);
				
				break;
				
			case "LAST_WICKET":

				inning = matchAllData.getMatch().getInning().stream().filter(inn -> 
					inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
				
				if(inning == null) {
					return "populateVizInfobarLeftBottom: Inning returned is NULL";
				}
				
				if(inning.getFallsOfWickets() == null && inning.getFallsOfWickets().isEmpty()) {
					return "populateVizInfobarLeftBottom: FoW returned is EMPTY";
				}
				
				battingCardList.add(inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == 
					inning.getFallsOfWickets().get(inning.getFallsOfWickets().size() - 1).getFowPlayerID()).findAny().orElse(null));

				if(battingCardList.get(battingCardList.size()-1) == null) {
					return "populateVizInfobarLeftBottom: Last wicket returned is invalid";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
					+ "$Select_Type*FUNCTION*Omo*vis_con SET 18 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Last_Wicket_Wide$Top$img_Flag*TEXTURE*IMAGE SET " + 
						Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName1() + "\0", print_writers);

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
					return "populateVizInfobarLeftBottom: 1st Inning returned is NULL";
				}
				
				this_data_str = CricketFunctions.projectedScore(matchAllData);
				if(this_data_str.size() <= 0) {
					return "populateVizInfobarLeftBottom: Projected score invalid";
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
					return "populateVizInfobarLeftBottom: Inning returned is NULL";
				}
				
				if(inning.getPartnerships() != null && inning.getPartnerships().size() <= 0) {
					return "populateVizInfobarLeftBottom: Partnership size is NULL/Zero";
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
				
			case "Analytics_1_Wide":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 3 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Top*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Analytics_1_Wide$txt_Bottom*GEOM*TEXT SET " + 
						"DOAD" + "\0", print_writers);
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
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName1() +"\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Player_Balls$flag_texture*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName1() +"\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Player_Balls$img_Flag*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName1() +"\0",print_writers);
				
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
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName1() +"\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Team_Tournament_Sixes$shadow*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName1() +"\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Team_Tournament_Sixes$img_Flag*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName1() +"\0",print_writers);

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Team_Tournament_Sixes$txt_Title*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Team_Tournament_Sixes$Side_"+ WhichSide+"$txt_Hundread*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Team_Tournament_Sixes$Side_"+ WhichSide+"$txt_Ten*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Team_Tournament_Sixes$Side_"+ WhichSide+"$txt_Unit*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				break;
				
			case "Tournament_Sixes":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 17 \0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Tournament_Sixes$Base$img_Highlight*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName1() +"\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Tournament_Sixes$txt_Title*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Tournament_Sixes$Side_"+ WhichSide+"$txt_Hundread*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Tournament_Sixes$Side_"+ WhichSide+"$txt_Ten*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Tournament_Sixes$Side_"+ WhichSide+"$txt_Unit*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				break;
				
			case "Team_Text":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide 
						+ "$Select_Type*FUNCTION*Omo*vis_con SET 20 \0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Team_Text$shadow*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName1() +"\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" + WhichSide + "$Team_Text$img_Flag*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName1() +"\0",print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Side_" 
						+ WhichSide + "$Team_Text$txt_LongString*GEOM*TEXT SET " + "DOAD" + "\0", print_writers);
				
				break;

			}
			infobar.setLast_middle_section(infobar.getMiddle_section());
			break;
		}
		return Constants.OK;
	}
}