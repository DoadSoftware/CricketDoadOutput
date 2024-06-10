package com.cricket.captions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.cricket.containers.Infobar;
import com.cricket.containers.LowerThird;
import com.cricket.model.Configuration;
import com.cricket.model.MatchAllData;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;

public class Animation 
{
	public String whichGraphicOnScreen = "", specialBugOnScreen = "", status = "", whichPlayer = "", 
			prevWhichPlayer = "",targetOnScreen = "",tapeballOnScreen = "";
	public Infobar infobar;
	public Caption caption;
	public int lastNumberOfRows = 0;
	public InfobarGfx this_infobarGfx;
	
	LowerThird LT = new LowerThird();
	
	BugsAndMiniGfx this_bugs = new BugsAndMiniGfx();
	
	public Animation(Infobar infobar) {
		super();
		this.infobar = infobar;
	}

	public Animation() {
		super();
	}
	public void setPositionOfLowerThirds(Configuration config, List<PrintWriter> print_writers) 
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023: 
			if(this.infobar.isInfobar_on_screen() == true) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Overall_Position_Y*"
					+ "TRANSFORMATION*POSITION*Y SET 40.0 \0",print_writers);
			}else if(this.specialBugOnScreen.equalsIgnoreCase(CricketUtil.TOSS)) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Overall_Position_Y*"
					+ "TRANSFORMATION*POSITION*Y SET 56.0 \0",print_writers);
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Overall_Position_Y*"
					+ "TRANSFORMATION*POSITION*Y SET 3.0 \0",print_writers);
			}
			break;	
		case Constants.ISPL:
			if(this.infobar.isInfobar_on_screen() == true) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Overall_Position_Y*"
					+ "TRANSFORMATION*POSITION*Y SET 40.0 \0",print_writers);
			}else if(this.specialBugOnScreen.equalsIgnoreCase(CricketUtil.TOSS)) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Overall_Position_Y*"
					+ "TRANSFORMATION*POSITION*Y SET 56.0 \0",print_writers);
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Overall_Position_Y*"
					+ "TRANSFORMATION*POSITION*Y SET 3.0 \0",print_writers);
			}
			break;
		}
	}
	
	public String getTypeOfGraphicsOnScreen(Configuration config,String whatToProcess)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			switch (whatToProcess.split(",")[0]) {
			case "F1": case "F2": case "F4": case "Shift_F10": case "Control_F11": case "Shift_F11": case "m": case "Control_m": 
			case "Shift_T": case "Control_d": case "Control_e": case "Control_F7": case "Control_F10":
			case "Shift_K": case "Alt_F9": case "Shift_D": case "p": case "Control_b": case "Alt_m": case "Alt_n":
			case "Alt_F10": case "Control_F1": case "Control_p": case "Shift_P": case "Shift_Q": 
			case "z": case "x": case "c": case "v": case "Alt_F11": case "Alt_z": case "Control_z": case "Control_x": case "r":
			case "Shift_Z": case "Shift_X":
				return Constants.FULL_FRAMER;
				
			case "F5": case "F6": case "F7": case "F8": case "F9": case "F10": case "F11": case "Alt_F8":
			case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_o":
			case "Shift_F3": case "u": case "d": case "e": case "q": case "Shift_F5": case "Shift_F9": case "Alt_F12":
			case "Control_g": case "Control_h": case "j": case "Control_F6": case "Shift_F6": case "Alt_F1": case "Alt_F2":
			case "Control_s": case "Alt_d": case "Control_f": case "Control_q": case "l": case "n": case "a": case "Control_F2":
			case "Alt_a": case "Alt_s":case "Shift_E": case "Alt_q": case "Alt_F6": case "Shift_A": case "Shift_R": case "Shift_U":
			case "Alt_w": case "Control_j": case "Alt_i": case "Alt_j": case "b": case "Control_i": case "Alt_Shift_L": case "Shift_B": 
			case "Control_Shift_F": case "Control_Shift_P":
				switch (whatToProcess.split(",")[0]) {
				case "Alt_F8": case "F8": case "F10": case "j": case "Alt_a": case "Alt_s": // Name super L3rd
					return Constants.NAME_SUPERS + Constants.LOWER_THIRD;
				case "q": case "Control_q": case "Alt_q": case "Shift_F7": // Boundary L3rd
					return Constants.BOUNDARIES + Constants.LOWER_THIRD;
				default:
					return Constants.LOWER_THIRD;
				}
			case "Alt_1": case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_5": case "Alt_6": case "Alt_7": case "Alt_8": case "Alt_9": case "Alt_0":
				return Constants.INFO_BAR;
			case "Shift_O": case "Control_k": case "k": case "g": case "y": case "Alt_p": case "o": case "t": 
			case "Control_y": case "h": case "Shift_F4": case "Shift_F": case ".": case "/":
				return Constants.BUGS;
			case "Shift_F1": case "Shift_F2": case "Alt_F7":
				return Constants.MINIS;
			}
			break;
		case Constants.BENGAL_T20:
			switch (whatToProcess.split(",")[0]) {
			case "F1": case "F2": case "F4": case "Shift_F10": case "Control_F11": case "Shift_F11": case "m": case "Control_m": 
			case "Shift_T": case "Control_d": case "Control_e": case "Control_F7": case "Control_F10":
			case "Shift_K": case "Alt_F9": case "Shift_D": case "p": case "Control_b": case "Alt_m": case "Alt_n":
			case "Alt_F10": case "Control_F1": case "Control_p": case "Shift_P": case "Shift_Q": 
			case "z": case "x": case "c": case "v": case "Alt_F11": case "Alt_z": case "Control_z": case "Control_x": case "r":
			case "Shift_Z": case "Shift_X": case "Control_Shift_F1": case "Control_Shift_D":
				return Constants.FULL_FRAMER;
				
			case "F5": case "F6": case "F7": case "F8": case "F9": case "F10": case "F11": case "Alt_F8":
			case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_o":
			case "Shift_F3": case "u": case "d": case "e": case "q": case "Shift_F5": case "Shift_F9": case "Alt_F12":
			case "Control_g": case "Control_h": case "j": case "Control_F6": case "Shift_F6": case "Alt_F1": case "Alt_F2":
			case "Control_s": case "Alt_d": case "Control_f": case "Control_q": case "l": case "n": case "a": case "Control_F2":
			case "Alt_a": case "Alt_s":case "Shift_E": case "Alt_q": case "Alt_F6": case "Shift_A": case "Shift_R": case "Shift_U":
			case "Alt_w": case "Control_j": case "Alt_i": case "Alt_j": case "b": case "Control_i": case "Alt_Shift_L": case "Shift_B": 
			case "Control_Shift_F": case "Control_Shift_P":
				switch (whatToProcess.split(",")[0]) {
				case "q": case "Control_q": case "Alt_q": case "Shift_F7": // Boundary L3rd
					return Constants.BOUNDARIES + Constants.LOWER_THIRD;
				default:
					return Constants.LOWER_THIRD;
				}
			case "Alt_1": case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_5": case "Alt_6": case "Alt_7": case "Alt_8": case "Alt_9": case "Alt_0":
			case "Control_F12": case "Shift_F12":	
				return Constants.INFO_BAR;
			case "Shift_O": case "Control_k": case "k": case "g": case "y": case "Alt_p": case "o": case "t": 
			case "Control_y": case "h": case "Shift_F4": case "Shift_F": case ".": case "/":
				return Constants.BUGS;
			case "Shift_F1": case "Shift_F2": case "Alt_F7":
				return Constants.MINIS;
			}
			break;	
		case Constants.ISPL:
			switch (whatToProcess.split(",")[0]) {
			case "F1": case "F2": case "F4": case "Shift_F10": case "Shift_F11": case "m": case "Control_m": 
			case "Shift_T": case "Shift_F8": case "Control_d": case "Control_e": case "Control_F7": case "Control_F10":
			case "Shift_K": case "Alt_F9": case "Shift_D": case "p": case "Control_b": case "Alt_m": case "Alt_n":
			case "Alt_F10": case "Control_F1": case "Control_p": case "Shift_P": case "Shift_Q": case "Control_F11":
			case "z": case "x": case "c": case "v": case "Alt_F11": case "Alt_z": case "Control_z": case "Control_x": case "r":
			case "Control_c": case "Control_v": case "Shift_V":
				return Constants.FULL_FRAMER;
			case "F5": case "F6": case "F7": case "F8": case "F9": case "F10": case "F11": case "Alt_F8":
			case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_o": case "Alt_Shift_L":
			case "Shift_F3": case "u": case "d": case "e": case "q": case "Shift_F5": case "Shift_F9": case "Alt_F12":
			case "Control_g": case "Control_h": case "j": case "Control_F6": case "Shift_F6": case "Alt_F1": case "Alt_F2":
			case "Control_s": case "Alt_d": case "Control_f": case "Control_q": case "l": case "Control_F4": case "a": case "Control_F2":
			case "Alt_a": case "Alt_s":case "Shift_E": case "Alt_q": case "Alt_F6": case "Shift_A":  case "Shift_R": case "Shift_U":
			case "Shift_F7": case "Alt_w": case "Control_j": case "Alt_i": case "Alt_j": case "b": case "Control_i":
				
				switch (whatToProcess.split(",")[0]) {
				case "q": case "Control_q": case "Alt_q": case "Shift_F7": case "Control_F4":// Boundary L3rd
					return Constants.BOUNDARIES + Constants.LOWER_THIRD;
				default:
					return Constants.LOWER_THIRD;
				}
			case "Alt_1": case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_5": case "Alt_6": case "Alt_7": case "Alt_8": case "Alt_9": 
			case "Alt_0": case "Alt_c": case "Control_F8": case "Control_F12": case "Shift_F12": case "Alt_y":
				return Constants.INFO_BAR;
			case "Shift_O": case "Control_k": case "k": case "g": case "y": case "Alt_p": case "o": case "t": 
			case "Control_y": case "h": case "Shift_F4": case "Shift_F": case ".": case "/":
				return Constants.BUGS;
			case "Shift_F1": case "Shift_F2": case "Alt_F7":
				return Constants.MINIS;
			}
			break;	
		}
		return "";
	}
	
	public String AnimateIn(String whatToProcess, List<PrintWriter> print_writers, Configuration config) throws InterruptedException, IOException 
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ISPL:
			//Full framers
//			System.out.println("whatToProcess.split(\",\")[0] = " + whatToProcess.split(",")[0]);
			switch (whatToProcess.split(",")[0]) {
			case "Control_1":
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Bonus_In", "START");
				break;
			case "m": case "Control_m":
				AnimateIn("ArrowDown,", print_writers, config); // Push infobar
				TimeUnit.MILLISECONDS.sleep(500);
				processAnimation(Constants.BACK, print_writers, "Anim_MatchId$In_Out", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Shift_D":
				AnimateIn("ArrowDown,", print_writers, config); // Push infobar
				TimeUnit.MILLISECONDS.sleep(500);
				processAnimation(Constants.BACK, print_writers, "Anim_Target$In_Out", "START");
				processAnimation(Constants.BACK, print_writers, "Anim_Target$Loop", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;	
			case "F1": case "F2": case "F4": case "Control_F7": case "Shift_T": case "Shift_F8": case "Control_F11":
			case "Shift_K":	case "Control_p": case "Shift_F11": case "z": case "x": case "c": case "v": case "Control_F10":
			case "Control_c": case "Control_v": case "Shift_V": case "Control_z": case "Control_x":
				AnimateIn("ArrowDown,", print_writers, config); // Push infobar
				TimeUnit.MILLISECONDS.sleep(500);
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Essentials", "START");
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Header", "START");
				switch (whatToProcess.split(",")[0]) {
				case "F1":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Batting_Card", "START");
					break;
				case "F2":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Bowling_Card", "START");
					break;
				case "Control_F11": case "Shift_F11":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Summary", "START");
					break;
				case "F4":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Partnership_List", "START");
					break;
				case "Control_F7":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Teams", "START");
					break;
				case "Shift_T":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Team_Single", "START");
					break;
				case "Shift_F8":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$LineUp_Image", "START");
					break;
				case "Shift_K":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Partnership", "START");
					break;
				case "Control_F10":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Manhattan", "START");
					break;
				case "Control_p":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Standings", "START");
					break;
				case "z": case "x": case "c": case "v": case "Control_z": case "Control_x": case "Control_c": case "Control_v":
				case "Shift_V":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$LeaderBoard", "START");
					break;
				}
				
				switch (whatToProcess.split(",")[0]) {
				 case "z": case "x": case "c": case "v": case "Control_z": case "Control_x": case "Control_c":
					 processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side1$Player" + whatToProcess.split(",")[2].split("_")[0], "START");
					 this.prevWhichPlayer = whatToProcess.split(",")[2].split("_")[0];
					break;
				}
				
				this.whichGraphicOnScreen = whatToProcess;
				break;
			
			//NameSuperDB, HOWOUT, LTBatProfile, NameSuperPlayer, LtBallProfile, BatThisMatch, BallThisMatch
			case "F7": case "F11": case "Control_f": case "Control_s":
				if(this.infobar.isInfobar_on_screen() == true) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 40.0 \0",print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 3.0 \0",print_writers);
				}
				
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "START");
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange$Lt_X_Position", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "F5": case "F6": case "F9": case "Control_F2":
			case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_o":
			case "Shift_F3": case "u": case "d": case "e": case "Shift_F5": case "Shift_F9": case "Alt_F12":
			case "Control_g": case "Control_h": case "Control_F6": case "Shift_F6": case "Shift_E": case "Alt_Shift_L":
			case "Alt_d": case "l": case "a":  case "Alt_F1": case "Alt_F2": case "Alt_F6": case "Shift_A":  
			case "Shift_R": case "Shift_U": case "Alt_w": case "Control_j": case "Alt_i": case "Alt_j": case "b": case "Control_i":
				
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "START");
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange$Lt_X_Position", "START");
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third$Top_Header", "SHOW 0.0");
				this.whichGraphicOnScreen = whatToProcess;
				break;	
			case "Alt_q":
				if(this.infobar.isInfobar_on_screen() == true) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_POTT_Aramco$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 40.0 \0",print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_POTT_Aramco$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 3.0 \0",print_writers);
				}
				
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "anim_POTT", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			 case "Alt_F8": case "F8": case "F10": case "j": case "Alt_a": case "Alt_s": // Name super L3rd
				 AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "START");
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange$Lt_X_Position", "CONTINUE REVERSE");
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third$Top_Header", "SHOW 0.0");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			 case "Shift_F7": case "Control_F4":
				 AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "Anim_Image_LT", "START");
				this.whichGraphicOnScreen = whatToProcess;
				 break;
			case "q": case "Control_q":// Boundary L3rd
				
				if(this.infobar.isInfobar_on_screen() == true) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 89.0 \0",print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 55.0 \0",print_writers);
				}
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "anim_Boundary_LT", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
				
			case "Alt_p":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					processAnimation(Constants.FRONT, print_writers, "anim_Toss", "START");
					break;
				default:
					processAnimation(Constants.FRONT, print_writers, "Anim_Center_Bug", "START");
					break;
				}
				this.specialBugOnScreen = CricketUtil.TOSS;
				break;
			case "o": case "t":
				processAnimation(Constants.FRONT, print_writers, "Anim_Center_Bug", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Shift_O": case "Control_k": case "k": case "g": case "y": case "Control_y": case "h": case "Shift_F4": case "Shift_F":
			case ".": case "/":	
				processAnimation(Constants.FRONT, print_writers, "Anim_Bugs$Essentials$In", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Shift_F1": case "Shift_F2":
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "Anim_Mini$In_Out", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Alt_F7":
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "Anim_MiniPoints$In_Out", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Alt_c":
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TopStage", "START");
				this.infobar.setChallengeRunOnScreen(true);
				break;
				
			case "Control_F12":
				if(this.infobar.isInfobar_on_screen() == true) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Main$Select*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					
					if(infobar.isChallengeRunOnScreen() == true) {
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TopStage", "CONTINUE");
						infobar.setChallengeRunOnScreen(false);
					}
					
					if(this.targetOnScreen.equalsIgnoreCase("TARGET")) {
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$Target_Out", "START");
						this.targetOnScreen = "";
					}
					
					
					if(this.tapeballOnScreen.equalsIgnoreCase("TAPE")) {
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$TapedBall_Out", "START");
						this.tapeballOnScreen = "";
					}
					
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage3_Out", "START");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage1_Out", "START");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Batsman1_Out", "START");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Batsman2_Out", "START");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$StrikeOut", "START");
					TimeUnit.MILLISECONDS.sleep(100);
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$Main_Out", "START");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Ident_In", "START");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$IdentInfo$In_Out", "START");
					
					infobar.setMiddle_section("");
					infobar.setFull_section("");
					infobar.setRight_bottom("");
					infobar.setRight_section("");
					this_infobarGfx.infobar.setLast_bowler(null);
					this.infobar.setInfobar_on_screen(true);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Main$Select*FUNCTION*Omo*vis_con SET 0 \0", print_writers);

					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$In", "START");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Ident_In", "START");
					TimeUnit.MILLISECONDS.sleep(1500);
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$IdentInfo$In_Out", "START");
					
					infobar.setMiddle_section("");
					infobar.setFull_section("");
					infobar.setRight_bottom("");
					infobar.setRight_section("");
					
					this.infobar.setInfobar_on_screen(true);
				}
				break;
			case "Control_F8":
				if(this.infobar.isInfobar_on_screen() == true && !this.infobar.isInfobar_pushed()) {
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TapedBall_In", "START");
					this.tapeballOnScreen = "TAPE";
				}
				break;
			
			case "Alt_y":
				if(this.infobar.isInfobar_on_screen() == true && !this.infobar.isInfobar_pushed()) {
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Target_In", "START");
					this.targetOnScreen = "TARGET";
				}
				System.out.println("Target = " + this.targetOnScreen);
				break;
				
			case "F12": //Infobar
				if(this.infobar.isInfobar_on_screen()) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Main$Select*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
//					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Small$In", "SHOW 0.0");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Ident_Out", "START");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$IdentInfo$In_Out", "CONTINUE");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$Main_In", "START");
					if(whatToProcess.split(",")[2].equalsIgnoreCase(CricketUtil.BATSMAN)) {
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Batsman1_In", "START");
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Batsman2_In", "START");
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$StrikeIn", "START");
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Bowler_In", "START");
					}
					this.infobar.setInfobar_on_screen(true);
					this.infobar.setInfobar_pushed(false);
					this.infobar.setInfobar_status(Constants.TWO_LINER_INFOBAR);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$Logos_All$Main$Select*FUNCTION*Omo*vis_con SET 1 \0", print_writers);

//					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Small$In", "SHOW 0.0");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$In", "START");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$Main_In", "START");
					if(whatToProcess.split(",")[2].equalsIgnoreCase(CricketUtil.BATSMAN)) {
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Batsman1_In", "START");
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Batsman2_In", "START");
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$StrikeIn", "START");
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Bowler_In", "START");
					}
					this.infobar.setInfobar_on_screen(true);
					this.infobar.setInfobar_pushed(false);
					this.infobar.setInfobar_status(Constants.TWO_LINER_INFOBAR);
				}
				break;
			case "ArrowUp":
				if(this.infobar.isInfobar_on_screen() == true && this.infobar.isInfobar_pushed()) {
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Push", "CONTINUE");
					this.infobar.setInfobar_pushed(false);
				}
				break;
			case "ArrowDown":
				if(this.infobar.isInfobar_on_screen() == true && !this.infobar.isInfobar_pushed()) {
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Push", "START");
					this.infobar.setInfobar_pushed(true);
					TimeUnit.MILLISECONDS.sleep(800);
				}
				break;
			case "w": case "i": case "f": case "s": case "0": case ";": case "9":
				if(whatToProcess.split(",")[0].equalsIgnoreCase("w")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$WIPES$Select*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				}else if(whatToProcess.split(",")[0].equalsIgnoreCase("i")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$WIPES$Select*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				}else if(whatToProcess.split(",")[0].equalsIgnoreCase("f")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$WIPES$Select*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
				}else if(whatToProcess.split(",")[0].equalsIgnoreCase("s")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$WIPES$Select*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
				}else if(whatToProcess.split(",")[0].equalsIgnoreCase("0")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$WIPES$Select*FUNCTION*Omo*vis_con SET 5 \0", print_writers);
				}else if(whatToProcess.split(",")[0].equalsIgnoreCase(";")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$WIPES$Select*FUNCTION*Omo*vis_con SET 6 \0", print_writers);
				}else if(whatToProcess.split(",")[0].equalsIgnoreCase("9")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$WIPES$Select*FUNCTION*Omo*vis_con SET 7 \0", print_writers);
				}
				
				if(this.infobar.isInfobar_on_screen() == true && !this.infobar.isInfobar_pushed()) {
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Wipes", "START");
				}
				break;
			case "Control_2":
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$InfoBar$CenterGRp$Main$BattingTeamGrp$PowerPlay$txt_PP*GEOM*TEXT SET " + 
//						"POWERPLAY" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Anim_InfoBar$Main$PowerPlay_In START \0", print_writers);
				break;
			case "Control_3":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Anim_InfoBar$Main$PowerPlay_In CONTINUE REVERSE \0", print_writers);
				break;	
			case "Alt_f": case "Alt_g": case Constants.SHRUNK_INFOBAR: case Constants.MIDDLE + Constants.SHRUNK_INFOBAR:
//				System.out.println("this.infobar.isInfobar_on_screen() = " + this.infobar.isInfobar_on_screen());
//				System.out.println("whatToProcess = " + whatToProcess);
//				System.out.println("this.infobar.setInfobar_status = " + this.infobar.getInfobar_status());
				if(this.infobar.isInfobar_on_screen() == true) {
					switch (whatToProcess.split(",")[0]) {
					case "Alt_f":
						if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.TWO_LINER_INFOBAR)) {
//							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Overall_Transformation*"
//								+ "ANIMATION*KEY*$Shrink_In*VALUE SET 120.0 -436.0 0.0\0",print_writers);
							processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Small$In", "START");
							this.infobar.setInfobar_status(Constants.FORCED + Constants.SHRUNK_INFOBAR);
						} else if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.FORCED + Constants.SHRUNK_INFOBAR)) {
							processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Small$Out", "START");
							this.infobar.setInfobar_status(Constants.TWO_LINER_INFOBAR);
						}
						break;
					case "Alt_g":
						if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.TWO_LINER_INFOBAR)) {
//							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Overall_Transformation*"
//								+ "ANIMATION*KEY*$Shrink_In*VALUE SET 183.0 -436.0 0.0\0",print_writers);
							processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Small$In", "START");
							this.infobar.setInfobar_status(Constants.FORCED + Constants.MIDDLE + Constants.SHRUNK_INFOBAR);
						} else if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.FORCED + Constants.MIDDLE + Constants.SHRUNK_INFOBAR)) {
							processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Small$Out", "START");
							this.infobar.setInfobar_status(Constants.TWO_LINER_INFOBAR);
						}
						break;
					case Constants.SHRUNK_INFOBAR:
						if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.TWO_LINER_INFOBAR)) {
//							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Overall_Transformation*"
//								+ "ANIMATION*KEY*$Shrink_In*VALUE SET 120.0 -436.0 0.0\0",print_writers);
							if(infobar.isChallengeRunOnScreen() == true) {
								processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TopStage", "CONTINUE");
								infobar.setChallengeRunOnScreen(false);
							}
							
							if(this.targetOnScreen.equalsIgnoreCase("TARGET")) {
								processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$Target_Out", "START");
								this.targetOnScreen = "";
							}
							
							
							if(this.tapeballOnScreen.equalsIgnoreCase("TAPE")) {
								processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$TapedBall_Out", "START");
								this.tapeballOnScreen = "";
							}
							
							processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Small$In", "START");
							this.infobar.setInfobar_status(Constants.SHRUNK_INFOBAR);
						} else if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.SHRUNK_INFOBAR)) {
							processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Small$Out", "START");
							this.infobar.setInfobar_status(Constants.TWO_LINER_INFOBAR);
						}
						break;
					case Constants.MIDDLE + Constants.SHRUNK_INFOBAR:
						if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.TWO_LINER_INFOBAR)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Overall_Transformation*"
								+ "ANIMATION*KEY*$Shrink_In*VALUE SET 183.0 -436.0 0.0\0",print_writers);
							processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Small$In", "START");
							this.infobar.setInfobar_status(Constants.MIDDLE + Constants.SHRUNK_INFOBAR);
						} else if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.MIDDLE + Constants.SHRUNK_INFOBAR)) {
							processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Small$Out", "START");
							this.infobar.setInfobar_status(Constants.TWO_LINER_INFOBAR);
						}
						break;
					}
				}
				break;
			}
			break;
			
		case Constants.ICC_U19_2023:

			//Full framers
			switch (whatToProcess.split(",")[0]) {
			case "Alt_z":
				AnimateIn("ArrowDown,", print_writers, config); // Push infobar
				TimeUnit.MILLISECONDS.sleep(500);
				processAnimation(Constants.BACK, print_writers, "Anim_Squad", "START");
				if(!caption.this_fullFramesGfx.WhichType.equalsIgnoreCase("role")) {
					processAnimation(Constants.BACK, print_writers, "Anim_SquadDataChange", "START");
				}
				processAnimation(Constants.BACK, print_writers, "SquadFlare_Loop", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Shift_D":
				AnimateIn("ArrowDown,", print_writers, config); // Push infobar
				TimeUnit.MILLISECONDS.sleep(500);
				processAnimation(Constants.BACK, print_writers, "Target", "START");
				processAnimation(Constants.BACK, print_writers, "TargetLoop", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Control_b":
				//AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Push infobar
				//TimeUnit.MILLISECONDS.sleep(500);
				processAnimation(Constants.BACK, print_writers, "In_At", "START");
				processAnimation(Constants.BACK, print_writers, "In_At_Loop", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Alt_m": case "Alt_n":
				AnimateIn("ArrowDown,", print_writers, config); // Push infobar
				TimeUnit.MILLISECONDS.sleep(500);
				processAnimation(Constants.BACK, print_writers, "Milestone", "START");
				processAnimation(Constants.BACK, print_writers, "MilestoneLoop", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "r":
				AnimateIn("ArrowDown,", print_writers, config); // Push infobar
				TimeUnit.MILLISECONDS.sleep(500);
				processAnimation(Constants.BACK, print_writers, "Anim_POTT", "START");
				processAnimation(Constants.BACK, print_writers, "POTT_Loop", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			
			case "F1": case "F2": case "F4": case "Control_F1": case "Shift_F10": case "Control_F11": case "Shift_F11": case "m": case "Control_m": 
			case "Shift_T": case "Control_d": case "Control_e": case "Control_F7": case "Control_F10":
			case "Shift_K": case "Alt_F9": case "Alt_F10": case "p": case "z": case "x": case "c": case "v": case "Control_p":
			case "Shift_P": case "Shift_Q": case "Alt_F11": case "Control_z": case "Control_x": case "Shift_Z": case "Shift_X":
				
				setVariousAnimationsKeys("ANIMATE-IN", print_writers, config);
				AnimateIn("ArrowDown,", print_writers, config); // Push infobar
				TimeUnit.MILLISECONDS.sleep(500);
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$Flare_Loop", "START");
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Essentials", "START");
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Header", "START");
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Footer", "START");
				
				switch (whatToProcess.split(",")[0]) {
				case "F1":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Batting_Card", "START");
					break;
				case "F2":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Bowling_Card", "START");
					break;
				case "F4":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Partnership_List", "START");
					break;
				case "Control_F1":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Batting_Card_Image", "START");
					break;
				case "Shift_F10":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Worm", "START");
					break;
				case "Control_F11": case "Shift_F11":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Summary", "START");
					break;
				case "m":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Ident", "START");
					break;
				case "Control_m":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Ident", "START");
					break;
				case "Shift_T":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$LineUp_Image", "START");
					break;
				case "Control_d": case "Shift_P": case "Control_e": case "Shift_Q":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Profile", "START");
					break;
				case "Control_F7":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Teams", "START");
					break;
				case "Control_F10":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Manhattan", "START");
					break;
				case "Shift_K":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Partnership", "START");
					break;
				case "Alt_F9": case "Alt_F10":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Team_Single", "START");
					break;
				case "p": 
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Group_Standings", "START");
					break;
				case "Control_p":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Standings", "START");
					break;
				case "z": case "x": case "c": case "v": case "Control_z": case "Control_x": case "Shift_Z": case "Shift_X":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Leader_Board", "START");
					break;
				case "Alt_F11":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Manhattan_Comparison", "START");
					break;
				}
				
				switch (whatToProcess.split(",")[0]) {
				case "m": case "Control_m": case "Control_F11": case "Shift_F11": case "Control_F7": 
				case "p": case "z": case "x": case "c": case "v": case "Control_p": case "Alt_F11": case "Control_z": case "Control_x":
					processAnimation(Constants.BACK, print_writers, "Header_Shrink", "START");
					break;
				case "Shift_K": case "F4":
					TimeUnit.MILLISECONDS.sleep(1500);
					processAnimation(Constants.BACK, print_writers, "Sponsor", "START");
					break;
				case "Control_d": case "Control_e": case "Shift_P": case "Shift_Q":
					if(Integer.valueOf(whatToProcess.split(",")[4]) > 0) {
						processAnimation(Constants.BACK, print_writers, "Profile_Highlight$Side1$"+whatToProcess.split(",")[4], "START");
					}
					break;
				}
				
				switch (whatToProcess.split(",")[0]) {
				 case "z": case "x": case "c": case "v": case "Control_z": case "Control_x":
					 processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side1$Player"+whatToProcess.split(",")[2].split("_")[0], "START");
					 this.prevWhichPlayer = whatToProcess.split(",")[2].split("_")[0];
					break;
				}
				
				switch (whatToProcess.split(",")[0]) {
				case "m": case "Shift_K":
					processAnimation(Constants.BACK, print_writers, "Base_Gradient", "START");
					break;
				}
				this.whichGraphicOnScreen = whatToProcess;
				lastNumberOfRows = caption.this_fullFramesGfx.numberOfRows;
				break;
			
			//NameSuperDB, HOWOUT, LTBatProfile, NameSuperPlayer, LtBallProfile, BatThisMatch, BallThisMatch
			case "F7": case "F11":
				if(this.infobar.isInfobar_on_screen() == true) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 40.0 \0",print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 3.0 \0",print_writers);
				}
				
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "F5": case "F6": case "F9": case "Control_F2":
			case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_o":
			case "Shift_F3": case "u": case "d": case "e": case "Shift_F5": case "Shift_F9": case "Alt_F12":
			case "Control_g": case "Control_h": case "Control_F6": case "Shift_F6": case "Control_s":case "Shift_E":
			case "Alt_d": case "Control_f": case "l": case "n": case "a":  case "Alt_F1": case "Alt_F2": case "Alt_F6": case "Alt_Shift_L":
			case "Shift_A":  case "Shift_R": case "Shift_U": case "Alt_w": case "Control_j": case "Alt_i": case "Alt_j": case "b": case "Control_i": 
			case "Shift_B": case "Control_Shift_F": case "Control_Shift_P":
				
				if(this.infobar.isInfobar_on_screen() == true) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 40.0 \0",print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 3.0 \0",print_writers);
				}
				
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "START");
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange$Lt_X_Position", "START");
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third$Top_Header", "SHOW 0.0");
				this.whichGraphicOnScreen = whatToProcess;
				break;	
			case "Alt_q":
				if(this.infobar.isInfobar_on_screen() == true) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_POTT_Aramco$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 40.0 \0",print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_POTT_Aramco$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 3.0 \0",print_writers);
				}
				
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "anim_POTT", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			 case "Alt_F8": case "F8": case "F10": case "j": case "Alt_a": case "Alt_s": // Name super L3rd
				 setPositionOfLowerThirds(config, print_writers);
				AnimateIn(Constants.MIDDLE + Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "anim_NameSupers", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
				
			case "q": case "Control_q":// Boundary L3rd
				
				if(this.infobar.isInfobar_on_screen() == true) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 89.0 \0",print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 55.0 \0",print_writers);
				}
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "anim_Boundary_LT", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
				
			case "Alt_p":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					processAnimation(Constants.FRONT, print_writers, "anim_Toss", "START");
					break;
				default:
					processAnimation(Constants.FRONT, print_writers, "Anim_Center_Bug", "START");
					break;
				}
				this.specialBugOnScreen = CricketUtil.TOSS;
				break;
			case "o": case "t":
				processAnimation(Constants.FRONT, print_writers, "Anim_Center_Bug", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Shift_O": case "Control_k": case "k": case "g": case "y": case "Control_y": case "h": case "Shift_F4": case "Shift_F":
				processAnimation(Constants.FRONT, print_writers, "Anim_Bugs$Essentials", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Shift_F1": case "Shift_F2":
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "Anim_Mini$In_Out", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			
//			case "Alt_2":
//				switch (config.getBroadcaster().toUpperCase()) {
//				case Constants.ISPL:
//					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage3_Out", "START");
//					break;
//				}
//				break;
				
			case "F12": //Infobar
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$In_Out", "START");
					this.infobar.setInfobar_on_screen(true);
					this.infobar.setInfobar_pushed(false);
					this.infobar.setInfobar_status(Constants.TWO_LINER_INFOBAR);
					break;
				}
				break;
			case "ArrowUp":
				if(this.infobar.isInfobar_on_screen() == true && this.infobar.isInfobar_pushed()) {
					processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Push", "CONTINUE");
					this.infobar.setInfobar_pushed(false);
				}
				break;
			case "ArrowDown":
				if(this.infobar.isInfobar_on_screen() == true && !this.infobar.isInfobar_pushed()) {
					processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Push", "START");
					this.infobar.setInfobar_pushed(true);
					TimeUnit.MILLISECONDS.sleep(800);
				}
				break;
			
			case "w": case "i": case "f": case "s":
				switch(config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					if(whatToProcess.split(",")[0].equalsIgnoreCase("w")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Wipes$Select*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
					}else if(whatToProcess.split(",")[0].equalsIgnoreCase("i")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Wipes$Select*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}else if(whatToProcess.split(",")[0].equalsIgnoreCase("f")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Wipes$Select*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
					}else if(whatToProcess.split(",")[0].equalsIgnoreCase("s")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Right$Wipes$Select*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
					}
					
					if(this.infobar.isInfobar_on_screen() == true && !this.infobar.isInfobar_pushed()) {
						processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Wipes", "START");
					}
					break;
				}
				
				break;
			case "Alt_f": case "Alt_g": case Constants.SHRUNK_INFOBAR: case Constants.MIDDLE + Constants.SHRUNK_INFOBAR:
//				System.out.println("this.infobar.isInfobar_on_screen() = " + this.infobar.isInfobar_on_screen());
//				System.out.println("whatToProcess = " + whatToProcess);
//				System.out.println("this.infobar.setInfobar_status = " + this.infobar.getInfobar_status());
				if(this.infobar.isInfobar_on_screen() == true) {
					switch (whatToProcess.split(",")[0]) {
					case "Alt_f":
						if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.TWO_LINER_INFOBAR)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Overall_Transformation*"
								+ "ANIMATION*KEY*$Shrink_In*VALUE SET 120.0 -436.0 0.0\0",print_writers);
							processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Small", "START");
							this.infobar.setInfobar_status(Constants.FORCED + Constants.SHRUNK_INFOBAR);
						} else if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.FORCED + Constants.SHRUNK_INFOBAR)) {
							processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Small", "CONTINUE REVERSE");
							this.infobar.setInfobar_status(Constants.TWO_LINER_INFOBAR);
						}
						break;
					case "Alt_g":
						if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.TWO_LINER_INFOBAR)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Overall_Transformation*"
								+ "ANIMATION*KEY*$Shrink_In*VALUE SET 183.0 -436.0 0.0\0",print_writers);
							processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Small", "START");
							this.infobar.setInfobar_status(Constants.FORCED + Constants.MIDDLE + Constants.SHRUNK_INFOBAR);
						} else if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.FORCED + Constants.MIDDLE + Constants.SHRUNK_INFOBAR)) {
							processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Small", "CONTINUE REVERSE");
							this.infobar.setInfobar_status(Constants.TWO_LINER_INFOBAR);
						}
						break;
					case Constants.SHRUNK_INFOBAR:
						if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.TWO_LINER_INFOBAR)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Overall_Transformation*"
								+ "ANIMATION*KEY*$Shrink_In*VALUE SET 120.0 -436.0 0.0\0",print_writers);
							processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Small", "START");
							this.infobar.setInfobar_status(Constants.SHRUNK_INFOBAR);
						} else if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.SHRUNK_INFOBAR)) {
							processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Small", "CONTINUE REVERSE");
							this.infobar.setInfobar_status(Constants.TWO_LINER_INFOBAR);
						}
						break;
					case Constants.MIDDLE + Constants.SHRUNK_INFOBAR:
						if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.TWO_LINER_INFOBAR)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Overall_Transformation*"
								+ "ANIMATION*KEY*$Shrink_In*VALUE SET 183.0 -436.0 0.0\0",print_writers);
							processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Small", "START");
							this.infobar.setInfobar_status(Constants.MIDDLE + Constants.SHRUNK_INFOBAR);
						} else if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.MIDDLE + Constants.SHRUNK_INFOBAR)) {
							processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Small", "CONTINUE REVERSE");
							this.infobar.setInfobar_status(Constants.TWO_LINER_INFOBAR);
						}
						break;
					}
				}
				break;
			}
			break;
		case Constants.BENGAL_T20:

			//Full framers
			switch (whatToProcess.split(",")[0]) {
			case "Alt_z":
				AnimateIn("ArrowDown,", print_writers, config); // Push infobar
				TimeUnit.MILLISECONDS.sleep(500);
				processAnimation(Constants.BACK, print_writers, "Anim_Squad", "START");
				if(!caption.this_fullFramesGfx.WhichType.equalsIgnoreCase("role")) {
					processAnimation(Constants.BACK, print_writers, "Anim_SquadDataChange", "START");
				}
				processAnimation(Constants.BACK, print_writers, "SquadFlare_Loop", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Shift_D":
				AnimateIn("ArrowDown,", print_writers, config); // Push infobar
				TimeUnit.MILLISECONDS.sleep(500);
				processAnimation(Constants.BACK, print_writers, "Anim_Target", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Control_b":
				//AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Push infobar
				//TimeUnit.MILLISECONDS.sleep(500);
				processAnimation(Constants.BACK, print_writers, "In_At", "START");
				processAnimation(Constants.BACK, print_writers, "In_At_Loop", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Alt_m": case "Alt_n":
				AnimateIn("ArrowDown,", print_writers, config); // Push infobar
				TimeUnit.MILLISECONDS.sleep(500);
				processAnimation(Constants.BACK, print_writers, "Milestone", "START");
				processAnimation(Constants.BACK, print_writers, "MilestoneLoop", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "r":
				AnimateIn("ArrowDown,", print_writers, config); // Push infobar
				TimeUnit.MILLISECONDS.sleep(500);
				processAnimation(Constants.BACK, print_writers, "Anim_POTT", "START");
				processAnimation(Constants.BACK, print_writers, "POTT_Loop", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "m": case "Control_m":
				AnimateIn("ArrowDown,", print_writers, config); // Push infobar
				TimeUnit.MILLISECONDS.sleep(500);
				processAnimation(Constants.BACK, print_writers, "Anim_Ident", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Control_Shift_D":
				AnimateIn("ArrowDown,", print_writers, config); // Push infobar
				TimeUnit.MILLISECONDS.sleep(500);
				processAnimation(Constants.BACK, print_writers, "Anim_DoubleIdent", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			
			case "Control_d": case "Control_e":
				AnimateIn("ArrowDown,", print_writers, config); // Push infobar
				processAnimation(Constants.BACK, print_writers, "anim_Profile$Essentials", "START");
				processAnimation(Constants.BACK, print_writers, "anim_Profile$Main", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			
			case "F1": case "F2": case "F4": case "Control_F1": case "Shift_F10": case "Control_F11": case "Shift_F11": 
			case "Shift_T": case "Control_F7": case "Control_F10":
			case "Shift_K": case "Alt_F9": case "Alt_F10": case "p": case "z": case "x": case "c": case "v": case "Control_p":
			case "Shift_P": case "Shift_Q": case "Alt_F11": case "Control_z": case "Control_x": case "Shift_Z": case "Shift_X":
			
			case "Control_Shift_F1":
				
				setVariousAnimationsKeys("ANIMATE-IN", print_writers, config);
				AnimateIn("ArrowDown,", print_writers, config); // Push infobar
				TimeUnit.MILLISECONDS.sleep(500);
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Essentials", "START");
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Header", "START");
				
				if(!whatToProcess.split(",")[0].equalsIgnoreCase("Shift_K")) {
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Footer", "START");
				}
				
				switch (whatToProcess.split(",")[0]) {
				case "F1":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Batting_Card", "START");
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Sponsor", "START");
					break;
				case "Control_Shift_F1":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Batting_Card", "START");
					TimeUnit.MILLISECONDS.sleep(800);
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Extra_Info", "START");
					if(caption.this_fullFramesGfx.batperformer_id > 0) {
						TimeUnit.MILLISECONDS.sleep(800);
						processAnimation(Constants.BACK, print_writers, "Anim_Highlights$Batting_Card$Side1$" + caption.this_fullFramesGfx.batperformer_id, "START");
						caption.this_fullFramesGfx.pervious_batperformer_id = caption.this_fullFramesGfx.batperformer_id;
					}
					break;
				case "F2":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Bowling_Card", "START");
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Sponsor", "START");
					break;
				case "F4":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Partnership_List", "START");
					break;
				case "Control_F1":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Batting_Card_Image", "START");
					break;
				case "Shift_F10":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Worm", "START");
					break;
				case "Control_F11": case "Shift_F11":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Summary", "START");
					processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "START");
					break;
				case "m":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Ident", "START");
					break;
				case "Control_m":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Ident", "START");
					break;
				case "Shift_T":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$LineUp_Image", "START");
					processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "START");
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Sponsor", "START");
					break;
				case "Control_d": case "Shift_P": case "Control_e": case "Shift_Q":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Profile", "START");
					break;
				case "Control_F7":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Teams", "START");
					processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "START");
					break;
				case "Control_F10":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Manhattan", "START");
					break;
				case "Shift_K":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Partnership", "START");
					break;
				case "Alt_F9": case "Alt_F10":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Team_Single", "START");
					break;
				case "p": 
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Group_Standings", "START");
					break;
				case "Control_p":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Standings", "START");
					processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "START");
					break;
				case "z": case "x": case "c": case "v": case "Control_z": case "Control_x": case "Shift_Z": case "Shift_X":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Leader_Board", "START");
					break;
				case "Alt_F11":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Manhattan_Comparison", "START");
					break;
				}
				
				switch (whatToProcess.split(",")[0]) {
				case "m": case "Control_m": case "Control_F7": 
				case "p": case "z": case "x": case "c": case "v": case "Control_p": case "Alt_F11": case "Control_z": case "Control_x":
					processAnimation(Constants.BACK, print_writers, "Header_Shrink", "START");
					break;
				case "Control_d": case "Control_e": case "Shift_P": case "Shift_Q":
					if(Integer.valueOf(whatToProcess.split(",")[4]) > 0) {
						processAnimation(Constants.BACK, print_writers, "Profile_Highlight$Side1$"+whatToProcess.split(",")[4], "START");
					}
					break;
				}
				
				switch (whatToProcess.split(",")[0]) {
				 case "z": case "x": case "c": case "v": case "Control_z": case "Control_x":
					 processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side1$Player"+whatToProcess.split(",")[2].split("_")[0], "START");
					 this.prevWhichPlayer = whatToProcess.split(",")[2].split("_")[0];
					break;
				}
				
				switch (whatToProcess.split(",")[0]) {
				case "m": case "Shift_K":
					processAnimation(Constants.BACK, print_writers, "Base_Gradient", "START");
					break;
				}
				this.whichGraphicOnScreen = whatToProcess;
				lastNumberOfRows = caption.this_fullFramesGfx.numberOfRows;
				break;
			
			//NameSuperDB, HOWOUT, LTBatProfile, NameSuperPlayer, LtBallProfile, BatThisMatch, BallThisMatch
			case "F7": case "F11":
				if(this.infobar.isInfobar_on_screen() == true) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 40.0 \0",print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 3.0 \0",print_writers);
				}
				
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "F5": 
				if(whatToProcess.split(",")[3].toUpperCase().equalsIgnoreCase("SPONSOR")) {
					AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
					TimeUnit.MILLISECONDS.sleep(1000);
					processAnimation(Constants.FRONT, print_writers, "anim_BatsmanScore_LT", "START");
					LT.setWhichSponsor("SPONSOR");
					this.whichGraphicOnScreen = whatToProcess;
				}else {
					AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
					TimeUnit.MILLISECONDS.sleep(1000);
					processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "START");
					this.whichGraphicOnScreen = whatToProcess;
					LT.setWhichSponsor("NOSPONSOR");
				}
				break;
			case "F9":
				if(whatToProcess.split(",")[3].toUpperCase().equalsIgnoreCase("SPONSOR")) {
					AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
					TimeUnit.MILLISECONDS.sleep(1000);
					processAnimation(Constants.FRONT, print_writers, "anim_BowlerFigure_LT", "START");
					LT.setWhichSponsor("SPONSOR");
					this.whichGraphicOnScreen = whatToProcess;
				}else {
					AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
					TimeUnit.MILLISECONDS.sleep(1000);
					processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "START");
					this.whichGraphicOnScreen = whatToProcess;
					LT.setWhichSponsor("NOSPONSOR");
				}
				break;
			case "Control_a":
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "anim_Projected_LT", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
				
			case "F6": case "Control_F2":
			case "Control_F5": case "Control_F9":  case "Control_F3": case "Alt_o":
			case "Shift_F3": case "u": case "d": case "e": case "Shift_F5": case "Shift_F9": case "Alt_F12":
			case "Control_g": case "Control_h": case "Control_F6": case "Shift_F6": case "Control_s":case "Shift_E":
			case "Alt_d": case "Control_f": case "l": case "n": case "a":  case "Alt_F1": case "Alt_F2": case "Alt_F6": case "Alt_Shift_L":
			case "Shift_A":  case "Shift_R": case "Shift_U": case "Alt_w": case "Control_j": case "Alt_i": case "Alt_j": case "b": case "Control_i": 
			case "Shift_B": case "Control_Shift_F": 
			case "Alt_F8": case "F8": case "F10": case "j": case "Alt_a": case "Alt_s":
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "START");
				//processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "START");
				//processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "SHOW 0.0");
				this.whichGraphicOnScreen = whatToProcess;
				break;	
			case "Alt_q":
				if(this.infobar.isInfobar_on_screen() == true) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_POTT_Aramco$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 40.0 \0",print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_POTT_Aramco$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 3.0 \0",print_writers);
				}
				
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "anim_POTT", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
				
			case "q": case "Control_q":// Boundary L3rd
				
				if(this.infobar.isInfobar_on_screen() == true) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 89.0 \0",print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$BoundaryLowerthird$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 55.0 \0",print_writers);
				}
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "anim_Boundary_LT", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
				
			case "Alt_p":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					processAnimation(Constants.FRONT, print_writers, "anim_Toss", "START");
					break;
				default:
					processAnimation(Constants.FRONT, print_writers, "Anim_Center_Bug", "START");
					break;
				}
				this.specialBugOnScreen = CricketUtil.TOSS;
				break;
			case "o": case "t":
				processAnimation(Constants.FRONT, print_writers, "Anim_Center_Bug", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Shift_O": case "Control_k": case "k": case "g": case "y": case "Control_y": case "h": case "Shift_F4": case "Shift_F":
				processAnimation(Constants.FRONT, print_writers, "Anim_Bugs$Essentials", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Shift_F1": case "Shift_F2":
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "Anim_Mini$In_Out", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			
			case "Control_F12":
				if(this.infobar.isInfobar_on_screen() == true) {
					
					processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Ident_To_Normal", "CONTINUE REVERSE");
					processAnimation(Constants.FRONT, print_writers, "Loop", "START");
					
					infobar.setMiddle_section("");
					infobar.setFull_section("");
					infobar.setRight_bottom("");
					infobar.setRight_section("");
					this_infobarGfx.infobar.setLast_bowler(null);
					this.infobar.setInfobar_on_screen(true);
				}else {
					
					processAnimation(Constants.FRONT, print_writers, "anim_Infobar$InOut", "START");
					processAnimation(Constants.FRONT, print_writers, "Loop", "START");
					
					infobar.setMiddle_section("");
					infobar.setFull_section("");
					infobar.setRight_bottom("");
					infobar.setRight_section("");
					
					this.infobar.setInfobar_on_screen(true);
				}
				break;
				
			case "F12": //Infobar
				if(this.infobar.isInfobar_on_screen()) {
					
					processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Ident_To_Normal", "START");
					processAnimation(Constants.FRONT, print_writers, "Loop", "START");
					
					this.infobar.setInfobar_on_screen(true);
					this.infobar.setInfobar_pushed(false);
					this.infobar.setInfobar_status(Constants.TWO_LINER_INFOBAR);
				}else {
					
					processAnimation(Constants.FRONT, print_writers, "anim_Infobar$InOut", "START");
					processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Ident_To_Normal", "START");
					processAnimation(Constants.FRONT, print_writers, "Loop", "START");
					
					this.infobar.setInfobar_on_screen(true);
					this.infobar.setInfobar_pushed(false);
					this.infobar.setInfobar_status(Constants.TWO_LINER_INFOBAR);
				}
				break;	
			case "ArrowUp":
				if(this.infobar.isInfobar_on_screen() == true && this.infobar.isInfobar_pushed()) {
					processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Push", "CONTINUE REVERSE");
					this.infobar.setInfobar_pushed(false);
				}
				break;
			case "ArrowDown":
				if(this.infobar.isInfobar_on_screen() == true && !this.infobar.isInfobar_pushed()) {
					processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Push", "START");
					this.infobar.setInfobar_pushed(true);
					TimeUnit.MILLISECONDS.sleep(800);
				}
				break;
			
			case "w": case "i": case "f": case "s":
				switch(config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					if(whatToProcess.split(",")[0].equalsIgnoreCase("w")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Animation$Select_Type*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
					}else if(whatToProcess.split(",")[0].equalsIgnoreCase("i")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Animation$Select_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}else if(whatToProcess.split(",")[0].equalsIgnoreCase("f")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Animation$Select_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
					}else if(whatToProcess.split(",")[0].equalsIgnoreCase("s")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$gfx_Infobar$Main$Fade_For_Shrink$Animation$Select_Type*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
					}
					
					if(this.infobar.isInfobar_on_screen() == true && !this.infobar.isInfobar_pushed()) {
						processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Animation", "START");
					}
					break;
				}
				
				break;
			case "Alt_f": case "Alt_g": case Constants.SHRUNK_INFOBAR: case Constants.MIDDLE + Constants.SHRUNK_INFOBAR:
//				System.out.println("this.infobar.isInfobar_on_screen() = " + this.infobar.isInfobar_on_screen());
//				System.out.println("whatToProcess = " + whatToProcess);
//				System.out.println("this.infobar.setInfobar_status = " + this.infobar.getInfobar_status());
				if(this.infobar.isInfobar_on_screen() == true) {
					switch (whatToProcess.split(",")[0]) {
					case "Alt_f":
						if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.TWO_LINER_INFOBAR)) {
//							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Overall_Transformation*"
//								+ "ANIMATION*KEY*$Shrink_In*VALUE SET 120.0 -436.0 0.0\0",print_writers);
							processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Shrink", "START");
							this.infobar.setInfobar_status(Constants.FORCED + Constants.SHRUNK_INFOBAR);
						} else if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.FORCED + Constants.SHRUNK_INFOBAR)) {
							processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Shrink", "CONTINUE");
							this.infobar.setInfobar_status(Constants.TWO_LINER_INFOBAR);
						}
						break;
					case "Alt_g":
						if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.TWO_LINER_INFOBAR)) {
//							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Overall_Transformation*"
//								+ "ANIMATION*KEY*$Shrink_In*VALUE SET 183.0 -436.0 0.0\0",print_writers);
							processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Shrink", "START");
							this.infobar.setInfobar_status(Constants.FORCED + Constants.MIDDLE + Constants.SHRUNK_INFOBAR);
						} else if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.FORCED + Constants.MIDDLE + Constants.SHRUNK_INFOBAR)) {
							processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Shrink", "CONTINUE");
							this.infobar.setInfobar_status(Constants.TWO_LINER_INFOBAR);
						}
						break;
					case Constants.SHRUNK_INFOBAR:
						if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.TWO_LINER_INFOBAR)) {
//							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Overall_Transformation*"
//								+ "ANIMATION*KEY*$Shrink_In*VALUE SET 120.0 -436.0 0.0\0",print_writers);
							processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Shrink", "START");
							this.infobar.setInfobar_status(Constants.SHRUNK_INFOBAR);
						} else if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.SHRUNK_INFOBAR)) {
							processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Shrink", "CONTINUE");
							this.infobar.setInfobar_status(Constants.TWO_LINER_INFOBAR);
						}
						break;
					case Constants.MIDDLE + Constants.SHRUNK_INFOBAR:
						if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.TWO_LINER_INFOBAR)) {
//							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$Infobar$Overall_Transformation*"
//								+ "ANIMATION*KEY*$Shrink_In*VALUE SET 183.0 -436.0 0.0\0",print_writers);
							processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Shrink", "START");
							this.infobar.setInfobar_status(Constants.MIDDLE + Constants.SHRUNK_INFOBAR);
						} else if(this.infobar.getInfobar_status().equalsIgnoreCase(Constants.MIDDLE + Constants.SHRUNK_INFOBAR)) {
							processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Shrink", "CONTINUE");
							this.infobar.setInfobar_status(Constants.TWO_LINER_INFOBAR);
						}
						break;
					}
				}
				break;
			}
			break;	
		}
		CricketFunctions.deletePreview();
		return CricketUtil.YES;
	}	
	public String AnimateOut(String whatToProcess, List<PrintWriter> print_writers, Configuration config) throws InterruptedException, IOException
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ISPL:
			switch (whatToProcess.split(",")[0]) {
			case "m": case "Control_m":
				processAnimation(Constants.BACK, print_writers, "Anim_MatchId$In_Out", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.BACK, print_writers, "Anim_MatchId$In_Out", "SHOW 0.0");
				AnimateIn("ArrowUp,", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "Shift_D":
				processAnimation(Constants.BACK, print_writers, "Anim_Target$In_Out", "CONTINUE");

				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.BACK, print_writers, "Anim_Target$In_Out", "SHOW 0.0");
				AnimateIn("ArrowUp,", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;	
			case "F1": case "F2": case "F4": case "Control_F7": case "Shift_T": case "Shift_F8": case "Control_F11":
			case "Shift_K":	case "Control_p": case "Shift_F11": case "z": case "x": case "c": case "v": case "Control_c": case "Control_v":
			case "Shift_V": case "Control_F10": case "Control_z": case "Control_x":
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Essentials$Out", "CONTINUE");
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Header$Out", "CONTINUE");
				
				switch (whatToProcess.split(",")[0]) {
				case "F1":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Batting_Card$Out", "CONTINUE");
					break;
				case "F2":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Bowling_Card$Out", "CONTINUE");
					break;
				case "Control_F11": case "Shift_F11":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Summary$Out", "CONTINUE");
					break;
				case "F4":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Partnership_List$Out", "CONTINUE");
					break;
				case "Control_F7":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Teams$Out", "CONTINUE");
					break;
				case "Shift_T":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Team_Single$Out", "CONTINUE");
					break;
				case "Shift_F8":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$LineUp_Image$Out", "CONTINUE");
					break;
				case "Shift_K":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Partnership$Out", "CONTINUE");
					break;
				case "Control_F10":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Manhattan$Out", "CONTINUE");
					break;
				case "Control_p":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Standings$Out", "CONTINUE");
					break;
				case "z": case "x": case "c": case "v": case "Control_c": case "Control_v": case "Shift_V": case "Control_z": case "Control_x":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$LeaderBoard$Out", "CONTINUE");
					break;
				}
				
				switch (whatToProcess.split(",")[0]) {
				 case "z": case "x": case "c": case "v": case "Control_z": case "Control_x":
					 processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side1$Player"+whatToProcess.split(",")[2].split("_")[0], "SHOW 1.574");
					processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side1$Player"+whatToProcess.split(",")[2].split("_")[0], "CONTINUE");
					this.prevWhichPlayer = "";
					this.whichPlayer = "";
					break;
				}
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn("ArrowUp,", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "F7": case "F11": case "Control_f": case "Control_s":
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "CONTINUE");
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange$Lt_X_Position", "CONTINUE REVERSE");
//				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third$Top_Header", "SHOW 2.680");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "F5": case "F6": case "F9": case "Control_F2":
			case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_o":
			case "Shift_F3": case "u": case "d": case "e": case "Shift_F5": case "Shift_F9": case "Alt_F12":
			case "Control_g": case "Control_h": case "Control_F6": case "Shift_F6": case "Shift_E": case "Alt_Shift_L":
			case "Alt_d": case "l": case "a":  case "Alt_F1": case "Alt_F2": case "Alt_F6": case "Shift_A":  
			case "Shift_R": case "Shift_U": case "Alt_w": case "Control_j": case "Alt_i": case "Alt_j": case "b": case "Control_i": 
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "CONTINUE");
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange$Lt_X_Position", "CONTINUE REVERSE");
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third$Top_Header", "SHOW 2.680");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			 case "Alt_F8": case "F8": case "F10": case "j": case "Alt_a": case "Alt_s":
				 processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "CONTINUE");
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third$Top_Header", "SHOW 2.680");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			 
			 case "Alt_q":
				 processAnimation(Constants.FRONT, print_writers, "anim_POTT", "CONTINUE");
				 TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
				
			case "Alt_p":
				if(this.specialBugOnScreen.equalsIgnoreCase(CricketUtil.TOSS)) {
					switch (config.getBroadcaster().toUpperCase()) {
					case Constants.BENGAL_T20:
						processAnimation(Constants.FRONT, print_writers, "anim_Toss", "CONTINUE");
						break;
					default:
						processAnimation(Constants.FRONT, print_writers, "Anim_Center_Bug", "CONTINUE");
						break;
					}
					this.specialBugOnScreen = "";
				}
				break;
			case "o": case "t":
				processAnimation(Constants.FRONT, print_writers, "Anim_Center_Bug", "CONTINUE");
				this.whichGraphicOnScreen = "";
				break;
			case "Shift_F7": case "Control_F4":
				processAnimation(Constants.FRONT, print_writers, "Anim_Image_LT", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;	
			case "q": case "Control_q":
				processAnimation(Constants.FRONT, print_writers, "anim_Boundary_LT", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "Shift_O": case "Control_k": case "k": case "g": case "y": case "Control_y": case "h": case "Shift_F4": case "Shift_F":
			case ".": case "/":
				processAnimation(Constants.FRONT, print_writers, "Anim_Bugs$Essentials$Out", "START");
				this.whichGraphicOnScreen = "";
				break;
			case "Shift_F1": case "Shift_F2":
				processAnimation(Constants.FRONT, print_writers, "Anim_Mini$In_Out", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "Anim_Mini$In_Out", "SHOW 0.0");
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "Alt_F7":
				processAnimation(Constants.FRONT, print_writers, "Anim_MiniPoints$In_Out", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "Anim_MiniPoints$In_Out", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_MiniPoints", "SHOW 0.0");
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;	
			case "Control_F8":
				if(infobar.getRight_section().equalsIgnoreCase("TARGET")) {
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Target_Out", "START");
				}else if(infobar.getRight_section().equalsIgnoreCase("TAPED_BALL")) {
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TapedBall_Out", "START");
				}else if(infobar.getRight_section().equalsIgnoreCase("EQUATION")) {
//					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$ToWin$In", "SHOW 0.0");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$ToWin$Out", "START");
				}else if(infobar.getRight_section().equalsIgnoreCase("TIMELINE")) {
//					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$ToWin$In", "SHOW 0.0");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TimeLine", "CONTINUE");
				}else if(infobar.getRight_section().equalsIgnoreCase("SUPER_OVER")) {
//					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$ToWin$In", "SHOW 0.0");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$TOPRIGHT_FREETEXT_Out", "CONTINUE");
				}
				
				infobar.setRight_section("");
//				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TapedBall_Out", "START");
//				this.tapeballOnScreen = "";
				break;
			case "Alt_y":
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Target_Out", "START");
				this.targetOnScreen = "";
				break;		
			case "Alt_c":
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TopStage", "CONTINUE");
				infobar.setChallengeRunOnScreen(false);
				break;
			case "Control_F12":
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Out", "START");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Ident_Out", "START");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$IdentInfo$In_Out", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(500);
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Ident_In", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$IdentInfo$In_Out", "SHOW 0.0");
				
				infobar.setMiddle_section("");
				infobar.setFull_section("");
				infobar.setRight_bottom("");
				infobar.setRight_section("");
				
				this.infobar.setInfobar_on_screen(false);
				break;
			case "F12": //Infobar
				if(infobar.isInfobar_on_screen() == true) {
					
					if(infobar.isChallengeRunOnScreen() == true) {
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TopStage", "CONTINUE");
						infobar.setChallengeRunOnScreen(false);
					}
					
					if(this.targetOnScreen.equalsIgnoreCase("TARGET")) {
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$Target_Out", "START");
						this.targetOnScreen = "";
					}
					
					if(this.tapeballOnScreen.equalsIgnoreCase("TAPE")) {
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$TapedBall_Out", "START");
						this.tapeballOnScreen = "";
					}
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Small$In", "SHOW 0.0");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Ident_In", "SHOW 0.0");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$IdentInfo", "SHOW 0.0");
					TimeUnit.MILLISECONDS.sleep(200);
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Out", "START");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$Main_Out", "START");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage3_Out", "START");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage2_Out", "START");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage1_Out", "START");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Batsman1_Out", "START");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Batsman2_Out", "START");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$StrikeOut", "START");
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Bowler_Out", "START");
					
					TimeUnit.MILLISECONDS.sleep(500);
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$Main_In", "SHOW 0.0");
					
					infobar.setMiddle_section("");
					infobar.setFull_section("");
					infobar.setRight_bottom("");
					infobar.setRight_section("");
					
					this.infobar.setInfobar_on_screen(false);
				}
				break;
			}
			break;
			
		case Constants.ICC_U19_2023:
			switch (whatToProcess.split(",")[0]) {
			case "Alt_z":
				processAnimation(Constants.BACK, print_writers, "Anim_Squad", "CONTINUE");
				if(!caption.this_fullFramesGfx.WhichType.equalsIgnoreCase("role")) {
					processAnimation(Constants.BACK, print_writers, "Anim_SquadDataChange", "CONTINUE");
				}
				processAnimation(Constants.BACK, print_writers, "SquadFlare_Loop", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn("ArrowUp,", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "Shift_D":
				processAnimation(Constants.BACK, print_writers, "Target", "CONTINUE");
				processAnimation(Constants.BACK, print_writers, "TargetLoop", "CONTINUE");

				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn("ArrowUp,", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "Control_b":
				processAnimation(Constants.BACK, print_writers, "In_At", "CONTINUE");
				processAnimation(Constants.BACK, print_writers, "In_At_Loop", "CONTINUE");
				//TimeUnit.MILLISECONDS.sleep(1000);
				//AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "Alt_m": case "Alt_n":
				processAnimation(Constants.BACK, print_writers, "Milestone", "CONTINUE");
				processAnimation(Constants.BACK, print_writers, "MilestoneLoop", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn("ArrowUp,", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "r":
				processAnimation(Constants.BACK, print_writers, "Anim_POTT", "CONTINUE");
				processAnimation(Constants.BACK, print_writers, "POTT_Loop", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn("ArrowUp,", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			
			case "F1": case "F2": case "F4": case "Control_F1": case "Shift_F10": case "Control_F11": case "Shift_F11": case "m": case "Control_m": case "p":
			case "Shift_T": case "Control_d": case "Control_e": case "Control_F7": case "Control_F10": case "Shift_K": case "Alt_F9":
			case "Alt_F10": case "Control_p": case "Shift_P": case "Shift_Q":
			case "z": case "x": case "c": case "v": case "Alt_F11": case "Control_z": case "Control_x": case "Shift_Z": case "Shift_X":
				
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$Flare_Loop", "CONTINUE");
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Essentials", "CONTINUE");
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Header", "CONTINUE");
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Footer", "CONTINUE");
				
				switch (whatToProcess.split(",")[0]) {
				case "F1":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Batting_Card", "CONTINUE");
					break;
				case "F2":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Bowling_Card", "CONTINUE");
					break;
				case "F4":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Partnership_List", "CONTINUE");
					break;
				case "Control_F1":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Batting_Card_Image", "CONTINUE");
					break;
				case "Shift_F10":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Worm", "CONTINUE");
					break;
				case "Control_F11": case "Shift_F11":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Summary", "CONTINUE");
					break;
				case "m":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Ident", "CONTINUE");
					break;
				case "Control_m":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Ident", "CONTINUE");
					break;
				case "Shift_T":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$LineUp_Image", "CONTINUE");
					break;
				
				case "Control_d": case "Shift_P": case "Control_e": case "Shift_Q":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Profile", "CONTINUE");
					break;
				case "Control_F7":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Teams", "CONTINUE");
					break;
				case "Control_F10":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Manhattan", "CONTINUE");
					break;
				case "Shift_K":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Partnership", "CONTINUE");
					break;
				case "Alt_F9": case "Alt_F10":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Team_Single", "CONTINUE");
					break;
				case "p":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Group_Standings", "CONTINUE");
					break;
				case "Control_p":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Standings", "CONTINUE");
					break;
				case "z": case "x": case "c": case "v": case "Control_z": case "Control_x": case "Shift_Z": case "Shift_X":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Leader_Board", "CONTINUE");
					break;
				case "Alt_F11":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Manhattan_Comparison", "CONTINUE");
					break;
				}
				
				switch (whatToProcess.split(",")[0]) {
				case "m": case "Control_m": case "Control_F11": case "Shift_F11": case "Control_F7": 
				case "p": case "z": case "x": case "c": case "v": case "Control_p": case "Alt_F11": case "Control_z": case "Control_x":
					processAnimation(Constants.BACK, print_writers, "Header_Shrink", "CONTINUE");
					break;
//				case "Shift_K": case "F4":
//					processAnimation(Constants.BACK, print_writers, "Sponsor", "CONTINUE");
//					break;
				case "Control_d": case "Control_e": case "Shift_P": case "Shift_Q":
					if(Integer.valueOf(whatToProcess.split(",")[4]) > 0) {
						processAnimation(Constants.BACK, print_writers, "Profile_Highlight$Side1$"+whatToProcess.split(",")[4], "CONTINUE");
					}
					break;
				}
				switch (whatToProcess.split(",")[0]) {
				 case "z": case "x": case "c": case "v": case "Control_z": case "Control_x":
					processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side1$Player"+whatToProcess.split(",")[2].split("_")[0], "CONTINUE");
					this.prevWhichPlayer = "";
					this.whichPlayer = "";
					break;
				}
				switch (whatToProcess.split(",")[0]) {
				case "m": case "Shift_K":
					processAnimation(Constants.BACK, print_writers, "Base_Gradient", "CONTINUE");
					break;
				}
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn("ArrowUp,", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			
			case "F5": case "F6": case "F7": case "F9": case "F11": case "Control_F2":
			case "Control_F5": case "Control_F9": case "Control_F3": case "Alt_o": case "Control_a":
			case "Shift_F3": case "u": case "d": case "e": case "Shift_F5": case "Shift_F9": case "Alt_F12": case "Alt_Shift_L":
			case "Control_g": case "Control_h": case "Control_F6": case "Shift_F6": case "Control_s":case "Shift_E":
			case "Alt_d": case "Control_f": case "l": case "n": case "a":  case "Alt_F1": case "Alt_F2": case "Alt_F6": 
			case "Shift_A":  case "Shift_R": case "Shift_U": case "Alt_w": case "Control_j": case "Alt_i": case "Alt_j": case "b": case "Control_i":  
			case "Shift_B": case "Control_Shift_F": case "Control_Shift_P":
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "CONTINUE");
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange$Lt_X_Position", "CONTINUE REVERSE");
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third$Top_Header", "SHOW 2.680");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			 case "Alt_F8": case "F8": case "F10": case "j": case "Alt_a": case "Alt_s":
				 processAnimation(Constants.FRONT, print_writers, "anim_NameSupers", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "anim_NameSupers", "SHOW 0.0");
				AnimateIn(Constants.MIDDLE + Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			 
			 case "Alt_q":
				 processAnimation(Constants.FRONT, print_writers, "anim_POTT", "CONTINUE");
				 TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
				
			case "Alt_p":
				if(this.specialBugOnScreen.equalsIgnoreCase(CricketUtil.TOSS)) {
					switch (config.getBroadcaster().toUpperCase()) {
					case Constants.BENGAL_T20:
						processAnimation(Constants.FRONT, print_writers, "anim_Toss", "CONTINUE");
						break;
					default:
						processAnimation(Constants.FRONT, print_writers, "Anim_Center_Bug", "CONTINUE");
						break;
					}
					this.specialBugOnScreen = "";
				}
				break;
			case "o": case "t":
				processAnimation(Constants.FRONT, print_writers, "Anim_Center_Bug", "CONTINUE");
				this.whichGraphicOnScreen = "";
				break;
			case "q": case "Control_q":
				processAnimation(Constants.FRONT, print_writers, "anim_Boundary_LT", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "Shift_O": case "Control_k": case "k": case "g": case "y": case "Control_y": case "h": case "Shift_F4": case "Shift_F":
				processAnimation(Constants.FRONT, print_writers, "Anim_Bugs$Essentials", "CONTINUE");
				this.whichGraphicOnScreen = "";
				break;
			case "Shift_F1": case "Shift_F2":
				processAnimation(Constants.FRONT, print_writers, "Anim_Mini$In_Out", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			
			case "F12": //Infobar
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					if(infobar.isInfobar_on_screen() == true) {
						processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$In_Out", "CONTINUE");
						infobar.setInfobar_on_screen(false);
					}
					break;
				case Constants.ISPL:
					if(infobar.isInfobar_on_screen() == true) {
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Small$In", "SHOW 0.0");
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Out", "START");
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$Main_OUT", "START");
						this.infobar.setInfobar_on_screen(false);
					}
					break;
				}
				
				break;
			}
			break;
		case Constants.BENGAL_T20:
			System.out.println(whatToProcess.split(",")[0]);
			switch (whatToProcess.split(",")[0]) {
			case "Alt_z":
				processAnimation(Constants.BACK, print_writers, "Anim_Squad", "CONTINUE");
				if(!caption.this_fullFramesGfx.WhichType.equalsIgnoreCase("role")) {
					processAnimation(Constants.BACK, print_writers, "Anim_SquadDataChange", "CONTINUE");
				}
				processAnimation(Constants.BACK, print_writers, "SquadFlare_Loop", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn("ArrowUp,", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "Shift_D":
				processAnimation(Constants.BACK, print_writers, "Anim_Target", "CONTINUE");

				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn("ArrowUp,", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "Control_b":
				processAnimation(Constants.BACK, print_writers, "In_At", "CONTINUE");
				processAnimation(Constants.BACK, print_writers, "In_At_Loop", "CONTINUE");
				//TimeUnit.MILLISECONDS.sleep(1000);
				//AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "Alt_m": case "Alt_n":
				processAnimation(Constants.BACK, print_writers, "Milestone", "CONTINUE");
				processAnimation(Constants.BACK, print_writers, "MilestoneLoop", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn("ArrowUp,", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "r":
				processAnimation(Constants.BACK, print_writers, "Anim_POTT", "CONTINUE");
				processAnimation(Constants.BACK, print_writers, "POTT_Loop", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn("ArrowUp,", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			
			case "m": case "Control_m":
				processAnimation(Constants.BACK, print_writers, "Anim_Ident", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn("ArrowUp,", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			
			case "Control_Shift_D":
				processAnimation(Constants.BACK, print_writers, "Anim_DoubleIdent", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn("ArrowUp,", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			
			case "Control_d": case "Control_e":
				processAnimation(Constants.BACK, print_writers, "anim_Profile$Essentials", "CONTINUE");
				processAnimation(Constants.BACK, print_writers, "anim_Profile$Main", "CONTINUE");
				AnimateIn("ArrowUp,", print_writers, config); // Push infobar
				this.whichGraphicOnScreen = "";
				break;
			
			case "F1": case "F2": case "F4": case "Control_F1": case "Shift_F10": case "Control_F11": case "Shift_F11": case "p":
			case "Shift_T": case "Control_F7": case "Control_F10": case "Shift_K": case "Alt_F9":
			case "Alt_F10": case "Control_p": case "Shift_P": case "Shift_Q":
			case "z": case "x": case "c": case "v": case "Alt_F11": case "Control_z": case "Control_x": case "Shift_Z": case "Shift_X":
				
			case "Control_Shift_F1":
				
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$Flare_Loop", "CONTINUE");
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Essentials", "CONTINUE");
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Header", "CONTINUE");
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Footer", "CONTINUE");
				
				switch (whatToProcess.split(",")[0]) {
				case "F1":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Sponsor", "CONTINUE");
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Batting_Card", "CONTINUE");
					break;
				case "Control_Shift_F1":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Batting_Card", "CONTINUE");
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Extra_Info", "CONTINUE");
					processAnimation(Constants.BACK, print_writers, "Anim_Highlights$Batting_Card$Side1$" + 
							caption.this_fullFramesGfx.pervious_batperformer_id, "CONTINUE REVERSE");
					
					caption.this_fullFramesGfx.pervious_batperformer_id = 0;
					caption.this_fullFramesGfx.batperformer_id = 0;
					break;
				case "F2":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Sponsor", "CONTINUE");
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Bowling_Card", "CONTINUE");
					break;
				case "F4":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Partnership_List", "CONTINUE");
					break;
				case "Control_F1":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Batting_Card_Image", "CONTINUE");
					break;
				case "Shift_F10":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Worm", "CONTINUE");
					break;
				case "Control_F11": case "Shift_F11":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Summary", "CONTINUE");
					processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "SHOW 0.0");
					break;
				case "Shift_T":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$LineUp_Image", "CONTINUE");
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Sponsor", "CONTINUE");
					TimeUnit.MILLISECONDS.sleep(1000);
					processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "SHOW 0.0");
					break;
				
				case "Control_d": case "Shift_P": case "Control_e": case "Shift_Q":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Profile", "CONTINUE");
					break;
				case "Control_F7":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Teams", "CONTINUE");
					processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "SHOW 0.0");
					break;
				case "Control_F10":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Manhattan", "CONTINUE");
					break;
				case "Shift_K":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Partnership", "CONTINUE");
					break;
				case "Alt_F9": case "Alt_F10":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Team_Single", "CONTINUE");
					break;
				case "p":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Group_Standings", "CONTINUE");
					break;
				case "Control_p":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Standings", "CONTINUE");
					processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "SHOW 0.0");
					break;
				case "z": case "x": case "c": case "v": case "Control_z": case "Control_x": case "Shift_Z": case "Shift_X":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Leader_Board", "CONTINUE");
					break;
				case "Alt_F11":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Manhattan_Comparison", "CONTINUE");
					break;
				}
				
				switch (whatToProcess.split(",")[0]) {
				case "Control_F11": case "Shift_F11": case "Control_F7": 
				case "p": case "z": case "x": case "c": case "v": case "Control_p": case "Alt_F11": case "Control_z": case "Control_x":
					processAnimation(Constants.BACK, print_writers, "Header_Shrink", "CONTINUE");
					break;
//				case "Shift_K": case "F4":
//					processAnimation(Constants.BACK, print_writers, "Sponsor", "CONTINUE");
//					break;
				case "Control_d": case "Control_e": case "Shift_P": case "Shift_Q":
					if(Integer.valueOf(whatToProcess.split(",")[4]) > 0) {
						processAnimation(Constants.BACK, print_writers, "Profile_Highlight$Side1$"+whatToProcess.split(",")[4], "CONTINUE");
					}
					break;
				}
				switch (whatToProcess.split(",")[0]) {
				 case "z": case "x": case "c": case "v": case "Control_z": case "Control_x":
					processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side1$Player"+whatToProcess.split(",")[2].split("_")[0], "CONTINUE");
					this.prevWhichPlayer = "";
					this.whichPlayer = "";
					break;
				}
				switch (whatToProcess.split(",")[0]) {
				case "Shift_K":
					processAnimation(Constants.BACK, print_writers, "Base_Gradient", "CONTINUE");
					break;
				}
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn("ArrowUp,", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "F5": 
				if(LT.getWhichSponsor().equalsIgnoreCase("SPONSOR")) {
					processAnimation(Constants.FRONT, print_writers, "anim_BatsmanScore_LT", "CONTINUE");
					TimeUnit.MILLISECONDS.sleep(1000);
					this.whichGraphicOnScreen = "";
					LT.setWhichSponsor("");
				}else {
					processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "CONTINUE");
					TimeUnit.MILLISECONDS.sleep(1000);
					this.whichGraphicOnScreen = "";
					LT.setWhichSponsor("");
				}
				break;
			case "F9":
				if(LT.getWhichSponsor().equalsIgnoreCase("SPONSOR")) {
					processAnimation(Constants.FRONT, print_writers, "anim_BowlerFigure_LT", "CONTINUE");
					TimeUnit.MILLISECONDS.sleep(1000);
					this.whichGraphicOnScreen = "";
					LT.setWhichSponsor("");
				}else {
					processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "CONTINUE");
					TimeUnit.MILLISECONDS.sleep(1000);
					this.whichGraphicOnScreen = "";
					LT.setWhichSponsor("");
				}
				break;
			case "Control_a":
				processAnimation(Constants.FRONT, print_writers, "anim_Projected_LT", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "F6": case "F7": case "F11": case "Control_F2":
			case "Control_F5": case "Control_F9": case "Control_F3": case "Alt_o":
			case "Shift_F3": case "u": case "d": case "e": case "Shift_F5": case "Shift_F9": case "Alt_F12": case "Alt_Shift_L":
			case "Control_g": case "Control_h": case "Control_F6": case "Shift_F6": case "Control_s":case "Shift_E":
			case "Alt_d": case "Control_f": case "l": case "n": case "a":  case "Alt_F1": case "Alt_F2": case "Alt_F6": 
			case "Shift_A":  case "Shift_R": case "Shift_U": case "Alt_w": case "Control_j": case "Alt_i": case "Alt_j": case "b": case "Control_i":  
			case "Shift_B": case "Control_Shift_F": case "Control_Shift_P":
			case "Alt_F8": case "F8": case "F10": case "j": case "Alt_a": case "Alt_s":
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "CONTINUE");
				//processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "CONTINUE REVERSE");
				//processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "SHOW 2.680");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			 
			 case "Alt_q":
				 processAnimation(Constants.FRONT, print_writers, "anim_POTT", "CONTINUE");
				 TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
				
			case "Alt_p":
				if(this.specialBugOnScreen.equalsIgnoreCase(CricketUtil.TOSS)) {
					switch (config.getBroadcaster().toUpperCase()) {
					case Constants.BENGAL_T20:
						processAnimation(Constants.FRONT, print_writers, "anim_Toss", "CONTINUE");
						break;
					default:
						processAnimation(Constants.FRONT, print_writers, "Anim_Center_Bug", "CONTINUE");
						break;
					}
					this.specialBugOnScreen = "";
				}
				break;
			case "o": case "t":
				processAnimation(Constants.FRONT, print_writers, "Anim_Center_Bug", "CONTINUE");
				this.whichGraphicOnScreen = "";
				break;
			case "q": case "Control_q":
				processAnimation(Constants.FRONT, print_writers, "anim_Boundary_LT", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "Shift_O": case "Control_k": case "k": case "g": case "y": case "Control_y": case "h": case "Shift_F4": case "Shift_F":
				processAnimation(Constants.FRONT, print_writers, "Anim_Bugs$Essentials", "CONTINUE");
				this.whichGraphicOnScreen = "";
				break;
			case "Shift_F1": case "Shift_F2":
				processAnimation(Constants.FRONT, print_writers, "Anim_Mini$In_Out", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			
			case "Control_F12":
				processAnimation(Constants.FRONT, print_writers, "anim_Infobar$InOut", "CONTINUE");
				
				TimeUnit.MILLISECONDS.sleep(1500);
				processAnimation(Constants.FRONT, print_writers, "anim_Infobar", "SHOW 0.0");
				
				infobar.setMiddle_section("");
				infobar.setFull_section("");
				infobar.setRight_bottom("");
				infobar.setRight_section("");
				
				this.infobar.setInfobar_on_screen(false);
				break;
				
			case "F12": //Infobar
				if(infobar.isInfobar_on_screen() == true) {
					
					processAnimation(Constants.FRONT, print_writers, "anim_Infobar$InOut", "CONTINUE");
					
					TimeUnit.MILLISECONDS.sleep(1500);
					processAnimation(Constants.FRONT, print_writers, "anim_Infobar", "SHOW 0.0");
					
					infobar.setMiddle_section("");
					infobar.setFull_section("");
					infobar.setRight_bottom("");
					infobar.setRight_section("");
					
					this.infobar.setInfobar_on_screen(false);
				}
				break;	
			}
			break;	
		}
		return CricketUtil.YES;
	}	
	public String ChangeOn(String whatToProcess,List<PrintWriter> print_writers,Configuration config) throws InterruptedException
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			
			if(!whatToProcess.contains(",")) {
				return CricketUtil.NO;
			}
			
			switch (whatToProcess.split(",")[0]) {
			case "Control_x": case "Control_z": case "z": case "x": case "c": case "v": 
				setVariousAnimationsKeys("CHANGE-ON", print_writers, config);
				if(!whichGraphicOnScreen.split(",")[0].equalsIgnoreCase(whatToProcess.split(",")[0])) {
					processAnimation(Constants.BACK, print_writers, "Change$Header", "START");
				}
				processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side1$Player" + prevWhichPlayer, "CONTINUE");
				processAnimation(Constants.BACK, print_writers, "Change$Leader_Board" , "START");
//				processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side2$Player" + whatToProcess.split(",")[2].split("_")[0], "START");
				processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side2$Player" + whatToProcess.split(",")[2].split("_")[0], "SHOW 0.800");
				processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side2$Player" + whatToProcess.split(",")[2].split("_")[0], "CONTINUE");
				prevWhichPlayer = whatToProcess.split(",")[2].split("_")[0];
				break;
			}
			switch(whatToProcess.split(",")[0]) {
			case "F1": case "F2": case "F4": case "Control_F11": case "Shift_T": case "p": case "Control_p":
			case "Shift_F8": 	
				setVariousAnimationsKeys("CHANGE-ON", print_writers, config);
				processAnimation(Constants.BACK, print_writers, "Change$Header", "START");
				processAnimation(Constants.BACK, print_writers, "Change$Footer", "START");
				if(whichGraphicOnScreen.contains(",")) {
					switch(whichGraphicOnScreen.split(",")[0]) {
					case "F1":  
						processAnimation(Constants.BACK, print_writers, "Change$Batting_Card", "START");
						break;
					case "F2":  
						processAnimation(Constants.BACK, print_writers, "Change$Bowling_Card", "START");
						break;
					case "F4":
						processAnimation(Constants.BACK, print_writers, "Change$Partnership_List", "START");
						if(whichGraphicOnScreen.split(",")[0].equalsIgnoreCase(whatToProcess.split(",")[0]) && 
								(caption.this_fullFramesGfx.whichSponsor != null || !caption.this_fullFramesGfx.whichSponsor.isEmpty())) {
							processAnimation(Constants.BACK, print_writers, "Sponsor", "START");
						}
						break;
					case "Control_F11":
						processAnimation(Constants.BACK, print_writers, "Change$Summary", "START");
						switch(whatToProcess.split(",")[0]) {
						case "Control_F11":
							break;
						default:
							processAnimation(Constants.BACK, print_writers, "Header_Shrink", "CONTINUE REVERSE");
							break;
						}
						break;
					case "p":
						processAnimation(Constants.BACK, print_writers, "Change$Group_Standings", "START");
						switch(whatToProcess.split(",")[0]) {
						case "p":
							break;
						default:
							processAnimation(Constants.BACK, print_writers, "Header_Shrink", "CONTINUE REVERSE");
							break;
						}
						break;
					case "Control_p":
						processAnimation(Constants.BACK, print_writers, "Change$Standings", "START");
						switch(whatToProcess.split(",")[0]) {
						case "Control_p":
							break;
						default:
							processAnimation(Constants.BACK, print_writers, "Header_Shrink", "CONTINUE REVERSE");
							break;
						}
						break;
					case "Shift_T":
						processAnimation(Constants.BACK, print_writers, "Change$LineUp_Image", "START");
						break;
					}
				}
				if(!whichGraphicOnScreen.split(",")[0].equalsIgnoreCase(whatToProcess.split(",")[0])) {
					switch(whatToProcess.split(",")[0]) {
					case "F1": 
						processAnimation(Constants.BACK, print_writers, "Change$Batting_Card", "START");
						break;
					case "F2":
						processAnimation(Constants.BACK, print_writers, "Change$Bowling_Card", "START");
						break;
					case "F4":
						processAnimation(Constants.BACK, print_writers, "Change$Partnership_List", "START");
						if(caption.this_fullFramesGfx.whichSponsor != null || !caption.this_fullFramesGfx.whichSponsor.isEmpty()) {
							processAnimation(Constants.BACK, print_writers, "Sponsor", "START");
						}
						break;
					case "Control_F11":
						processAnimation(Constants.BACK, print_writers, "Header_Shrink", "START");
						processAnimation(Constants.BACK, print_writers, "Change$Summary", "START");
						break;
					case "p":
						processAnimation(Constants.BACK, print_writers, "Header_Shrink", "START");
						processAnimation(Constants.BACK, print_writers, "Change$Group_Standings", "START");
						break;
					case "Control_p":
						processAnimation(Constants.BACK, print_writers, "Header_Shrink", "START");
						processAnimation(Constants.BACK, print_writers, "Change$Standings", "START");
						break;
					}
				}
				if(caption.this_fullFramesGfx.numberOfRows != lastNumberOfRows) {
					processAnimation(Constants.BACK, print_writers, "ConcussExtend_Y", "START");
					lastNumberOfRows = caption.this_fullFramesGfx.numberOfRows;
				}
				break;
			case "F7": case "F11": 
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "START");
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third$Top_Header", "START");
				break;
			case "F5": case "F6": case "F9": case "l": case "n": case "a": case "Control_F2":
			case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_o":  case "Alt_F1": case "Alt_F2":
			case "Shift_F3": case "u": case "d": case "e": case "Shift_F5": case "Shift_F9": case "Alt_F12":case "Shift_E":
			case "Control_g": case "Control_h": case "Control_F6": case "Shift_F6": case "Control_s": case "Control_f":
			case "Alt_F6": case "Shift_A":  case "Shift_R": case "Shift_U": case "Alt_w": case "Control_j": case "Alt_i": case "b":
			case "Alt_j": case "Control_i": case "Alt_Shift_L": case "Shift_B": case "Control_Shift_F": case "Control_Shift_P":
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "START");
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third$Top_Header", "SHOW 0.0");
				break;	
			 case "Alt_F8": case "F8": case "F10": case "j": case "Alt_a": case "Alt_s":
				 processAnimation(Constants.FRONT, print_writers, "Anim_NameSuperChange", "START");
				break;
			case "q": case "Control_q":
				processAnimation(Constants.FRONT, print_writers, "Anim_Boundary_LTChange", "START");
				break;
//			case "Alt_c":
//				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TopStage", "START");
//				break;
			case "Control_F8":
				if(this.infobar.isInfobar_on_screen() == true && !this.infobar.isInfobar_pushed()) {
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TapedBall_In", "START");
				}
				break;
			
			case "Alt_1": case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_5": case "Alt_6": case "Alt_7": case "Alt_8": case "Alt_9": case "Alt_0":
				switch(whatToProcess.split(",")[0]) {
				case "Alt_1":
					switch (config.getBroadcaster().toUpperCase()) {
					case Constants.ICC_U19_2023:
						processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Bottom_Left", "START");
						break;
					}
					
					break;
				case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_5": case "Alt_6": case "Alt_9": case "Alt_0":
					switch (config.getBroadcaster().toUpperCase()) {
					case Constants.ICC_U19_2023:
						processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_RightInfo", "START");
						if(infobar.getRight_section()!= null && !infobar.getRight_section().isEmpty()) {
							processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bowl_All$In_Out", "SHOW 0.0");
							infobar.setRight_section("");
						}
						break;
					}
					
					break;
				case "Alt_7":

					switch(config.getBroadcaster().toUpperCase()) {
					case Constants.ICC_U19_2023: 
						processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_RightInfo_BottomRightPart", "START");
						break;
					}
					
					break;
				case "Alt_8":
					if(whatToProcess.split(",")[2].equalsIgnoreCase(CricketUtil.BOWLER)) {
						processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bowl_All$In_Out", "CONTINUE");
						infobar.setRight_section("");
					}else {
						if(infobar.getRight_section()!= null && !infobar.getRight_section().isEmpty()) {
							processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bowl_All$Change", "START");
						}else {
							processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bowl_All$In_Out", "START");
						}
						infobar.setRight_section(whatToProcess.split(",")[2]);
					}
					break;
				}
				break;
			
			case "Shift_F": case "y": case "Shift_O":
				processAnimation(Constants.FRONT, print_writers, "Anim_BugsChange", "START");
				break;
			case "Shift_F1": case "Shift_F2":
				processAnimation(Constants.FRONT, print_writers, "Anim_MiniChange", "START");
				break;
			}
			break;
		
		case Constants.BENGAL_T20:
			
			if(!whatToProcess.contains(",")) {
				return CricketUtil.NO;
			}
			
			switch (whatToProcess.split(",")[0]) {
			case "Control_x": case "Control_z": case "z": case "x": case "c": case "v": 
				setVariousAnimationsKeys("CHANGE-ON", print_writers, config);
				if(!whichGraphicOnScreen.split(",")[0].equalsIgnoreCase(whatToProcess.split(",")[0])) {
					processAnimation(Constants.BACK, print_writers, "Change$Header", "START");
				}
				processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side1$Player" + prevWhichPlayer, "CONTINUE");
				processAnimation(Constants.BACK, print_writers, "Change$Leader_Board" , "START");
//				processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side2$Player" + whatToProcess.split(",")[2].split("_")[0], "START");
				processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side2$Player" + whatToProcess.split(",")[2].split("_")[0], "SHOW 0.800");
				processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side2$Player" + whatToProcess.split(",")[2].split("_")[0], "CONTINUE");
				prevWhichPlayer = whatToProcess.split(",")[2].split("_")[0];
				break;
			}
			switch(whatToProcess.split(",")[0]) {
			case "Shift_F12":
				processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Ident_Change", "START");
				break;
			case "F1": case "F2": case "F4": case "Control_F11": case "Shift_T": case "p": case "Control_p":
			case "Shift_F8": 	
				setVariousAnimationsKeys("CHANGE-ON", print_writers, config);
				processAnimation(Constants.BACK, print_writers, "Change$Header", "START");
				processAnimation(Constants.BACK, print_writers, "Change$Footer", "START");
				if(whichGraphicOnScreen.contains(",")) {
					switch(whichGraphicOnScreen.split(",")[0]) {
					case "F1":  
						processAnimation(Constants.BACK, print_writers, "Change$Batting_Card", "START");
						processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "SHOW 0.0");
						break;
					case "F2":  
						processAnimation(Constants.BACK, print_writers, "Change$Bowling_Card", "START");
						processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "SHOW 0.0");
						break;
					case "F4":
						processAnimation(Constants.BACK, print_writers, "Change$Partnership_List", "START");
						processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "SHOW 0.0");
//						if(whichGraphicOnScreen.split(",")[0].equalsIgnoreCase(whatToProcess.split(",")[0]) && 
//								(caption.this_fullFramesGfx.whichSponsor != null || !caption.this_fullFramesGfx.whichSponsor.isEmpty())) {
//							processAnimation(Constants.BACK, print_writers, "Sponsor", "START");
//						}
						break;
					case "Control_F11":
						processAnimation(Constants.BACK, print_writers, "Change$Summary", "START");
						switch(whatToProcess.split(",")[0]) {
						case "Control_F11":
							break;
						default:
							processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "CONTINUE REVERSE");
							break;
						}
						break;
					case "p":
						processAnimation(Constants.BACK, print_writers, "Change$Group_Standings", "START");
						switch(whatToProcess.split(",")[0]) {
						case "p":
							break;
						default:
							processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "CONTINUE REVERSE");
							break;
						}
						break;
					case "Control_p":
						processAnimation(Constants.BACK, print_writers, "Change$Standings", "START");
						switch(whatToProcess.split(",")[0]) {
						case "Control_p":
							break;
						default:
							processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "CONTINUE REVERSE");
							break;
						}
						break;
					case "Shift_T":
						processAnimation(Constants.BACK, print_writers, "Change$LineUp_Image", "START");
						processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "SHOW 0.500");
						processAnimation(Constants.BACK, print_writers, "Change$Sponsor", "START");
						//TimeUnit.MILLISECONDS.sleep(1000);
						break;
					}
				}
				if(!whichGraphicOnScreen.split(",")[0].equalsIgnoreCase(whatToProcess.split(",")[0])) {
					switch(whatToProcess.split(",")[0]) {
					case "F1": 
						processAnimation(Constants.BACK, print_writers, "Change$Batting_Card", "START");
						processAnimation(Constants.BACK, print_writers, "Change$Sponsor", "START");
						break;
					case "F2":
						processAnimation(Constants.BACK, print_writers, "Change$Bowling_Card", "START");
						processAnimation(Constants.BACK, print_writers, "Change$Sponsor", "START");
						break;
					case "F4":
						processAnimation(Constants.BACK, print_writers, "Change$Partnership_List", "START");
//						if(caption.this_fullFramesGfx.whichSponsor != null || !caption.this_fullFramesGfx.whichSponsor.isEmpty()) {
//							processAnimation(Constants.BACK, print_writers, "Sponsor", "START");
//						}
						break;
					case "Control_F11":
						processAnimation(Constants.BACK, print_writers, "Change$Summary", "START");
						processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "START");
						break;
					case "p":
						processAnimation(Constants.BACK, print_writers, "Change$Group_Standings", "START");
						processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "START");
						break;
					case "Control_p":
						processAnimation(Constants.BACK, print_writers, "Change$Standings", "START");
						processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "START");
						break;
					}
				}
				if(caption.this_fullFramesGfx.numberOfRows != lastNumberOfRows) {
					//processAnimation(Constants.BACK, print_writers, "ConcussExtend_Y", "START");
					lastNumberOfRows = caption.this_fullFramesGfx.numberOfRows;
				}
				break;
			case "F7": case "F11": 
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "START");
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third$Top_Header", "START");
				break;
			case "F5": case "F6": case "F9": case "l": case "n": case "a": case "Control_F2":
			case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_o":  case "Alt_F1": case "Alt_F2":
			case "Shift_F3": case "u": case "d": case "e": case "Shift_F5": case "Shift_F9": case "Alt_F12":case "Shift_E":
			case "Control_g": case "Control_h": case "Control_F6": case "Shift_F6": case "Control_s": case "Control_f":
			case "Alt_F6": case "Shift_A":  case "Shift_R": case "Shift_U": case "Alt_w": case "Control_j": case "Alt_i": case "b":
			case "Alt_j": case "Control_i": case "Alt_Shift_L": case "Shift_B": case "Control_Shift_F": case "Control_Shift_P":
			case "Alt_F8": case "F8": case "F10": case "j": case "Alt_a": case "Alt_s":
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange$Badge", "START");
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange$Sublines", "START");
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange$Topline", "START");
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange$Dynamic", "START");
				break;	
			case "q": case "Control_q":
				processAnimation(Constants.FRONT, print_writers, "Anim_Boundary_LTChange", "START");
				break;
//			case "Alt_c":
//				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TopStage", "START");
//				break;
			case "Control_F8":
				if(this.infobar.isInfobar_on_screen() == true && !this.infobar.isInfobar_pushed()) {
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TapedBall_In", "START");
				}
				break;
			
			case "Alt_1": case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_5": case "Alt_6": case "Alt_7": case "Alt_8": case "Alt_9": case "Alt_0":
				switch(whatToProcess.split(",")[0]) {
				case "Alt_1":
					switch (config.getBroadcaster().toUpperCase()) {
					case Constants.BENGAL_T20:
						processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Section3_Change", "START");
						break;
					}
					
					break;
				case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_6": case "Alt_9": case "Alt_0":
					switch (config.getBroadcaster().toUpperCase()) {
					case Constants.BENGAL_T20:
						
						if(whatToProcess.split(",")[2].equalsIgnoreCase(CricketUtil.BATSMAN)) {
							processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Fade_For_Analytics", "CONTINUE");
							processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Analytics", "CONTINUE");
//							infobar.setRight_section("");
							TimeUnit.MILLISECONDS.sleep(2000);
							processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Fade_For_Analytics", "SHOW 0.0");
							processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Analytics", "SHOW 0.0");
						}else {
							System.out.println("infobar = " + infobar.getMiddle_section());
							if(infobar.getMiddle_section()!= null && !infobar.getMiddle_section().isEmpty()) {
								
								if(infobar.getMiddle_section().equalsIgnoreCase(CricketUtil.BATSMAN)) {
									processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Fade_For_Analytics", "START");
									processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Analytics", "START");
								}else {
									processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Analytics$Change", "START");
								}
//								infobar.setRight_section("");
							}else {
								processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Fade_For_Analytics", "START");
								processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Analytics", "START");
								infobar.setMiddle_section(whatToProcess.split(",")[2]);
							}
						}
						infobar.setMiddle_section(whatToProcess.split(",")[2]);
						System.out.println("infobar.getMiddle_section() = " + infobar.getMiddle_section());
						break;
					}
					
					break;
				case "Alt_7":

					switch(config.getBroadcaster().toUpperCase()) {
					case Constants.BENGAL_T20: 
						processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Section1_Change", "START");
						break;
					}
					break;
				case "Alt_8": case "Alt_5":
					if(whatToProcess.split(",")[2].equalsIgnoreCase(CricketUtil.BOWLER)) {
						processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Fade_For_Section_2", "CONTINUE");
						processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Section_2", "CONTINUE REVERSE");
						infobar.setRight_section("");
						TimeUnit.MILLISECONDS.sleep(2000);
						processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Fade_For_Section_2", "SHOW 0.0");
					}else {
						if(infobar.getRight_section()!= null && !infobar.getRight_section().isEmpty()) {
							processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Section_2$Change", "START");
						}else {
							processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Fade_For_Section_2", "START");
							processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Section_2$InOut", "START");
						}
						infobar.setRight_section(whatToProcess.split(",")[2]);
					}
					break;
				}
				break;
			
			case "Shift_F": case "y": case "Shift_O":
				processAnimation(Constants.FRONT, print_writers, "Anim_BugsChange", "START");
				break;
			case "Shift_F1": case "Shift_F2":
				processAnimation(Constants.FRONT, print_writers, "Anim_MiniChange", "START");
				break;
			}
			break;	
			
		case Constants.ISPL:
			
			if(!whatToProcess.contains(",")) {
				return CricketUtil.NO;
			}
			switch (whatToProcess.split(",")[0]) {
			case "Control_x": case "Control_z": case "z": case "x": case "c": case "v": case "Control_c": case "Control_v":
//				setVariousAnimationsKeys("CHANGE-ON", print_writers, config);
				if(!whichGraphicOnScreen.split(",")[0].equalsIgnoreCase(whatToProcess.split(",")[0])) {
					processAnimation(Constants.BACK, print_writers, "Change$Header", "START");
				}
				processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side1$Player" + prevWhichPlayer, "SHOW 1.574");
				processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side1$Player" + prevWhichPlayer, "CONTINUE");
				processAnimation(Constants.BACK, print_writers, "Change$LeaderBoard" , "START");
//				processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side2$Player" + whatToProcess.split(",")[2].split("_")[0], "START");
				processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side2$Player" + whatToProcess.split(",")[2].split("_")[0], "SHOW 0.0");
				processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side2$Player" + whatToProcess.split(",")[2].split("_")[0], "CONTINUE");
				prevWhichPlayer = whatToProcess.split(",")[2].split("_")[0];
				break;
			}
			
			switch(whatToProcess.split(",")[0]) {
			case "F1": case "F2": case "F4": case "Shift_T":  case "Shift_F8": case "p": case "Control_p": case "Control_F11":
			case "Shift_K":
//				setVariousAnimationsKeys("CHANGE-ON", print_writers, config);
				processAnimation(Constants.BACK, print_writers, "Change$Header", "START");
//				processAnimation(Constants.BACK, print_writers, "Change$Footer", "START");
				if(whichGraphicOnScreen.contains(",")) {
					switch(whichGraphicOnScreen.split(",")[0]) {
					case "F1":  
						processAnimation(Constants.BACK, print_writers, "Change$Batting_Card", "START");
						break;
					case "F2":  
						processAnimation(Constants.BACK, print_writers, "Change$Bowling_Card", "START");
						break;
					case "F4":
						processAnimation(Constants.BACK, print_writers, "Change$Partnership_List", "START");
						break;
					case "Shift_K":
						processAnimation(Constants.BACK, print_writers, "Change$Partnership", "START");
						break;	
					case "Control_F11":
						processAnimation(Constants.BACK, print_writers, "Change$Summary", "START");
						switch(whatToProcess.split(",")[0]) {
						case "Control_F11":
							break;
						default:
							processAnimation(Constants.BACK, print_writers, "Header_Shrink", "CONTINUE REVERSE");
							break;
						}
						break;
					case "p":
						processAnimation(Constants.BACK, print_writers, "Change$Group_Standings", "START");
						switch(whatToProcess.split(",")[0]) {
						case "p":
							break;
						default:
							processAnimation(Constants.BACK, print_writers, "Header_Shrink", "CONTINUE REVERSE");
							break;
						}
						break;
					case "Control_p":
						processAnimation(Constants.BACK, print_writers, "Change$Standings", "START");
						switch(whatToProcess.split(",")[0]) {
						case "Control_p":
							break;
						default:
							processAnimation(Constants.BACK, print_writers, "Header_Shrink", "CONTINUE REVERSE");
							break;
						}
						break;
					case "Shift_T":
						processAnimation(Constants.BACK, print_writers, "Change$Team_Single", "START");
						//TimeUnit.MILLISECONDS.sleep(1000);
						break;
					case "Shift_F8":
						processAnimation(Constants.BACK, print_writers, "Change$LineUp_Image", "START");
						//TimeUnit.MILLISECONDS.sleep(1000);
						break;	
					}
				}
				if(!whichGraphicOnScreen.split(",")[0].equalsIgnoreCase(whatToProcess.split(",")[0])) {
					switch(whatToProcess.split(",")[0]) {
					case "F1": 
						processAnimation(Constants.BACK, print_writers, "Change$Batting_Card", "START");
						break;
					case "F2":
						processAnimation(Constants.BACK, print_writers, "Change$Bowling_Card", "START");
						break;
					case "F4":
						processAnimation(Constants.BACK, print_writers, "Change$Partnership_List", "START");
						break;
					case "Control_F11":
//						processAnimation(Constants.BACK, print_writers, "Header_Shrink", "START");
						processAnimation(Constants.BACK, print_writers, "Change$Summary", "START");
						break;
					case "p":
						processAnimation(Constants.BACK, print_writers, "Header_Shrink", "START");
						processAnimation(Constants.BACK, print_writers, "Change$Group_Standings", "START");
						break;
					case "Control_p":
//						processAnimation(Constants.BACK, print_writers, "Header_Shrink", "START");
						processAnimation(Constants.BACK, print_writers, "Change$Standings", "START");
						break;
					case "Shift_K":
						processAnimation(Constants.BACK, print_writers, "Change$Partnership", "START");
						break;	
					}
				}
//				if(caption.this_fullFramesGfx.numberOfRows != lastNumberOfRows) {
//					processAnimation(Constants.BACK, print_writers, "ConcussExtend_Y", "START");
//					lastNumberOfRows = caption.this_fullFramesGfx.numberOfRows;
//				}
				break;
			case "F7": case "F11": case "Control_s": case "Control_f":
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "START");
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third$Top_Header", "START");
				break;
			case "F5": case "F6": case "F9": case "l": case "a": case "Control_F2": //case "n":
			case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_o":  case "Alt_F1": case "Alt_F2":
			case "Shift_F3": case "u": case "d": case "e": case "Shift_F5": case "Shift_F9": case "Alt_F12":case "Shift_E":
			case "Control_g": case "Control_h": case "Control_F6": case "Shift_F6": case "b":
			case "Alt_F6": case "Shift_A":  case "Shift_R": case "Shift_U": case "Alt_w": case "Control_j": case "Alt_i":
			case "Alt_j": case "Control_i": case "Alt_Shift_L":
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "START");
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third$Top_Header", "SHOW 0.0");
				break;	
			 case "Alt_F8": case "F8": case "F10": case "j": case "Alt_a": case "Alt_s":
				 switch (config.getBroadcaster().toUpperCase()) {
					case Constants.ICC_U19_2023:
						processAnimation(Constants.FRONT, print_writers, "Anim_NameSuperChange", "START");
						break;
					case Constants.ISPL:
						processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "START");
						processAnimation(Constants.FRONT, print_writers, "Anim_LtChange$Lt_X_Position", "CONTINUE REVERSE");
						processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third$Top_Header", "SHOW 0.0");
						break;
				 }
				break;
			case "q": case "Control_q":
				processAnimation(Constants.FRONT, print_writers, "Anim_Boundary_LTChange", "START");
				break;
			case "Shift_F7": case "Control_F4":
				processAnimation(Constants.FRONT, print_writers, "Anim_Image_LtChange", "START");
				break;	
			case "Alt_c":
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TopStage", "START");
				break;
			case "Shift_F12":
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$IdentInfo$Change", "START");
				break;
			case "Alt_1": case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_5": case "Alt_6": case "Alt_7": case "Alt_8": case "Alt_9": case "Alt_0":
				switch(whatToProcess.split(",")[0]) {
				case "Alt_1": case "Alt_9": case "Alt_0":
					if(infobar.getFull_section() != null && !infobar.getFull_section().trim().isEmpty()) {
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage3_Change", "START");
					}else {
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage3_In", "START");
						if(infobar.getMiddle_section() != null && !infobar.getMiddle_section().isEmpty()) {
							processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage1_In", "SHOW 0.0");
						}
						if(infobar.getRight_bottom() != null && !infobar.getRight_bottom().isEmpty()) {
							processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage2_In", "SHOW 0.0");
						}
						infobar.setMiddle_section("");
						infobar.setRight_bottom("");
					}
					infobar.setFull_section(whatToProcess.split(",")[2]);
					break;
				case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_5": case "Alt_6":
					if(whatToProcess.split(",")[2].equalsIgnoreCase(CricketUtil.BATSMAN)) {
						
						if(infobar.getFull_section() != null && !infobar.getFull_section().isEmpty()) {
							processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage3_Out", "START");
							TimeUnit.MILLISECONDS.sleep(500);
							infobar.setFull_section("");
							infobar.setMiddle_section("");
						}else {
							processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage1_Out", "START");
							infobar.setMiddle_section("");
							infobar.setFull_section("");
						}
					}else {
						if(infobar.getFull_section()!= null && !infobar.getFull_section().isEmpty()) {
							processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage3_Out", "START");
							TimeUnit.MILLISECONDS.sleep(500);
							infobar.setFull_section("");
						}
						
						if(infobar.getMiddle_section() != null && !infobar.getMiddle_section().isEmpty()) {
							processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage1_Change", "START");
						}else {
							processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage1_In", "START");
						}
						infobar.setMiddle_section(whatToProcess.split(",")[2]);
					}
					
					break;
				case "Alt_7":
					if(whatToProcess.split(",")[2].equalsIgnoreCase(CricketUtil.BOWLER)) {
						if(infobar.getFull_section() != null && !infobar.getFull_section().isEmpty()) {
							processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage3_Out", "START");
							TimeUnit.MILLISECONDS.sleep(500);
							infobar.setFull_section("");
							infobar.setRight_bottom("");
						}else {
							processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage2_Out", "START");
							TimeUnit.MILLISECONDS.sleep(500);
							infobar.setFull_section("");
							infobar.setRight_bottom("");
						}
					}else {
						if(infobar.getFull_section()!= null && !infobar.getFull_section().isEmpty()) {
							processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage3_Out", "START");
							infobar.setFull_section("");
						}
						
						if(infobar.getRight_bottom() != null && !infobar.getRight_bottom().trim().isEmpty()) {
							processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage2_Change", "START");
						}else {
							processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage2_In", "START");
						}
						infobar.setRight_bottom(whatToProcess.split(",")[2]);
					}
					
					break;
				case "Alt_8":
					if(whatToProcess.split(",")[2].equalsIgnoreCase("TARGET")) {
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$Target_Out", "SHOW 0.0");
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$Target_In", "START");
//						infobar.setRight_section("");
					}else if(whatToProcess.split(",")[2].equalsIgnoreCase("TAPED_BALL")) {
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$TapedBall_In", "START");
//						infobar.setRight_section("");
					}else if(whatToProcess.split(",")[2].equalsIgnoreCase("EQUATION")) {
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$ToWin$In", "SHOW 0.0");
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$ToWin$In", "START");
					}else if(whatToProcess.split(",")[2].equalsIgnoreCase("TIMELINE")) {
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TimeLine", "SHOW 0.0");
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TimeLine", "START");
					}else if(whatToProcess.split(",")[2].equalsIgnoreCase("SUPER_OVER")) {
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$TOPRIGHT_FREETEXT_In", "SHOW 0.0");
						processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$TOPRIGHT_FREETEXT_In", "START");
					}
					infobar.setRight_section(whatToProcess.split(",")[2]);
					infobar.setLast_right_section(whatToProcess.split(",")[2]);
					break;
				}
				break;
			
			case "Shift_F": case "y": case "Shift_O":
				processAnimation(Constants.FRONT, print_writers, "Anim_BugsChange", "START");
				break;
			case "Shift_F1": case "Shift_F2":
				processAnimation(Constants.FRONT, print_writers, "Anim_MiniChange", "START");
				break;
			}
			break;	
		}
		return CricketUtil.YES;
	}
	public String CutBack(String whatToProcess,List<PrintWriter> print_writers, Configuration config) throws InterruptedException, IOException
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:

			if(!whatToProcess.contains(",")) {
				return CricketUtil.NO;
			}
			
			switch(whatToProcess.split(",")[0]) {
			case "Shift_F1": case "Shift_F2":
				processAnimation(Constants.FRONT, print_writers, "Anim_MiniChange", "SHOW 0.0");
				break;
			case "Shift_F": case "y": case "Shift_O":
				processAnimation(Constants.FRONT, print_writers, "Anim_BugsChange", "SHOW 0.0");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Shift_F12":
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$IdentInfo$Change", "SHOW 0.0");
				break;
			case "Alt_1":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Section3_Change", "SHOW 0.0");
					break;
				}
				break;
//			case "Alt_c":
//				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TopStage_Change", "SHOW 0.0");
//				break;
			case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_5": case "Alt_6": case "Alt_9": case "Alt_0":
				TimeUnit.MILLISECONDS.sleep(1000);
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_RightInfo", "SHOW 0.0");
					break;
				}
				
				break;
			case "Alt_7":
				switch (config.getBroadcaster().toUpperCase()) {

				case Constants.ICC_U19_2023:
					processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Section1_Change", "SHOW 0.0");
					break;
				}
				break;
			case "Alt_8":
				if(!whatToProcess.split(",")[2].equalsIgnoreCase(CricketUtil.BOWLER)) {
					if(infobar.getRight_section()!= null && !infobar.getRight_section().isEmpty()) {
						processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Section_2$Change", "SHOW 0.0");
					}
				}
				break;
			case "F5": case "F6": case "F7": case "F9": case "F11": case "Control_g": case "Control_h": case "Alt_F1": case "Alt_F2":
			case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_o": case "Control_F2":
			case "Control_F6": case "Shift_F6": case "Control_s": case "Control_f": case "l": case "n": case "a": 
			case "Alt_F6": case "Shift_A":  case "Shift_R": case "Shift_U": case "Alt_w": case "Control_j": case "Alt_i": case "b":
			case "Alt_j":	case "Control_Shift_F":
				switch (config.getBroadcaster().toUpperCase()) {
					case Constants.ICC_U19_2023:
						processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "SHOW 0.0");
						this.whichGraphicOnScreen = whatToProcess;
						break;
					case Constants.ISPL:
						processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "SHOW 0.0");
						processAnimation(Constants.FRONT, print_writers, "Anim_LtChange$Lt_X_Position", "SHOW 0.900");
						this.whichGraphicOnScreen = whatToProcess;
						break;
				 }
				break;
			 case "Alt_F8": case "F8": case "F10": case "j": case "Alt_a": case "Alt_s":
				 switch (config.getBroadcaster().toUpperCase()) {
					case Constants.ICC_U19_2023:
						processAnimation(Constants.FRONT, print_writers, "Anim_NameSuperChange", "SHOW 0.0");
						this.whichGraphicOnScreen = whatToProcess;
						break;
					case Constants.ISPL:
						processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "SHOW 0.0");
						this.whichGraphicOnScreen = whatToProcess;
						break;
				 }
				break;
				
			 case "Control_x": case "Control_z": case "z": case "x": case "c": case "v": case "Control_c": case "Control_v":
				 if(!whichGraphicOnScreen.split(",")[0].equalsIgnoreCase(whatToProcess.split(",")[0])) {
					 processAnimation(Constants.BACK, print_writers, "Change$Header", "SHOW 0.0");
				 }
				 processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Leader_Board", "SHOW 3.000");
				 processAnimation(Constants.BACK, print_writers, "Change$Leader_Board", "SHOW 0.0");
				
				 for(int iPlyr = 1; iPlyr <= 5; iPlyr++) {
					 if(iPlyr == Integer.valueOf(prevWhichPlayer)) {
						 processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side1$Player" + iPlyr, "SHOW 2.700");
					 } else {
						 processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side1$Player" + iPlyr, "SHOW 0.00");
					 }
				 }
				processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side2", "SHOW 0.0");
				 
				 setVariousAnimationsKeys("CUT-BACK", print_writers, config);
				 this.whichGraphicOnScreen = whatToProcess;
				 break;
				
			case "F1": case "F2": case "F4": case "Control_F11": case "Shift_T": case "p": case "Control_p":
				processAnimation(Constants.BACK, print_writers, "Change$Header", "SHOW 0.0");
				processAnimation(Constants.BACK, print_writers, "Change$Footer", "SHOW 0.0");
				switch(whichGraphicOnScreen.split(",")[0]) {
				case "F1":  
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Batting_Card", "SHOW 3.000");
					processAnimation(Constants.BACK, print_writers, "Change$Batting_Card", "SHOW 0.0");
					break;
				case "F2":  
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Bowling_Card", "SHOW 3.000");
					processAnimation(Constants.BACK, print_writers, "Change$Bowling_Card", "SHOW 0.0");
					break;
				case "F4":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Partnership_List", "SHOW 3.000");
					processAnimation(Constants.BACK, print_writers, "Change$Partnership_List", "SHOW 0.0");
					break;
				case "Control_F11":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Summary", "SHOW 3.000");
					processAnimation(Constants.BACK, print_writers, "Change$Summary", "SHOW 0.0");
					break;
				case "Shift_T":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$LineUp_Image", "SHOW 3.000");
					processAnimation(Constants.BACK, print_writers, "Change$LineUp_Image", "SHOW 0.0");
					break;
				case "p":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Group_Standings", "SHOW 3.000");
					processAnimation(Constants.BACK, print_writers, "Change$Group_Standings", "SHOW 0.0");
					break;
				case "Control_p":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Standings", "SHOW 3.000");
					processAnimation(Constants.BACK, print_writers, "Change$Standings", "SHOW 0.0");
					break;
				}
				if(!whichGraphicOnScreen.split(",")[0].equalsIgnoreCase(whatToProcess.split(",")[0])) {
					switch(whatToProcess.split(",")[0]) {
					case "F1":  
						processAnimation(Constants.BACK, print_writers, "Header_Shrink", "SHOW 0.0");
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Batting_Card", "SHOW 3.000");
						processAnimation(Constants.BACK, print_writers, "Change$Batting_Card", "SHOW 0.0");
						break;
					case "F2":
						processAnimation(Constants.BACK, print_writers, "Header_Shrink", "SHOW 0.0");
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Bowling_Card", "SHOW 3.000");
						processAnimation(Constants.BACK, print_writers, "Change$Bowling_Card", "SHOW 0.0");
						break;
					case "F4":
						processAnimation(Constants.BACK, print_writers, "Header_Shrink", "SHOW 0.0");
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Partnership_List", "SHOW 3.000");
						processAnimation(Constants.BACK, print_writers, "Change$Partnership_List", "SHOW 0.0");
						break;
					case "Control_F11":
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Summary", "SHOW 3.000");
						processAnimation(Constants.BACK, print_writers, "Change$Summary", "SHOW 0.0");
						break;
					case "p":
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Group_Standings", "SHOW 3.000");
						processAnimation(Constants.BACK, print_writers, "Change$Group_Standings", "SHOW 0.0");
						break;
					case "Control_p":
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Standings", "SHOW 3.000");
						processAnimation(Constants.BACK, print_writers, "Change$Standings", "SHOW 0.0");
						break;
					}
				}
				setVariousAnimationsKeys("CUT-BACK", print_writers, config);
				processAnimation(Constants.BACK, print_writers, "ConcussExtend_Y", "SHOW 0.0");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			}
			break;
			
		case Constants.BENGAL_T20:

			if(!whatToProcess.contains(",")) {
				return CricketUtil.NO;
			}
			
			switch(whatToProcess.split(",")[0]) {
			case "Shift_F12":
				processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Ident_Change", "SHOW 0.0");
				break;
			case "Shift_F1": case "Shift_F2":
				processAnimation(Constants.FRONT, print_writers, "Anim_MiniChange", "SHOW 0.0");
				break;
			case "Shift_F": case "y": case "Shift_O":
				processAnimation(Constants.FRONT, print_writers, "Anim_BugsChange", "SHOW 0.0");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Alt_1":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Bottom_Left", "SHOW 0.0");
					break;
				}
				break;
//			case "Alt_c":
//				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TopStage_Change", "SHOW 0.0");
//				break;
			case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_6": case "Alt_9": case "Alt_0":
				TimeUnit.MILLISECONDS.sleep(1000);
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.BENGAL_T20:
					processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Analytics$Change", "SHOW 0.0");
					break;
				}
				
				break;
			case "Alt_7":
				switch (config.getBroadcaster().toUpperCase()) {

				case Constants.BENGAL_T20:
					processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Section1_Change", "SHOW 0.0");
					break;
				}
				
				break;
			case "Alt_8": case "Alt_5":
				if(!whatToProcess.split(",")[2].equalsIgnoreCase(CricketUtil.BOWLER)) {
					if(infobar.getRight_section()!= null && !infobar.getRight_section().isEmpty()) {
						processAnimation(Constants.FRONT, print_writers, "anim_Infobar$Section_2$Change", "SHOW 0.0");
					}
				}
				break;
			case "F5": case "F6": case "F7": case "F9": case "F11": case "Control_g": case "Control_h": case "Alt_F1": case "Alt_F2":
			case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_o": case "Control_F2":
			case "Control_F6": case "Shift_F6": case "Control_s": case "Control_f": case "l": case "n": case "a": 
			case "Alt_F6": case "Shift_A":  case "Shift_R": case "Shift_U": case "Alt_w": case "Control_j": case "Alt_i": case "b":
			case "Alt_j":	case "Control_Shift_F":
			 case "Alt_F8": case "F8": case "F10": case "j": case "Alt_a": case "Alt_s":
				switch (config.getBroadcaster().toUpperCase()) {
					case Constants.BENGAL_T20:
						processAnimation(Constants.FRONT, print_writers, "Anim_LtChange$Badge", "SHOW 0.0");
						processAnimation(Constants.FRONT, print_writers, "Anim_LtChange$Sublines", "SHOW 0.0");
						processAnimation(Constants.FRONT, print_writers, "Anim_LtChange$Topline", "SHOW 0.0");
						processAnimation(Constants.FRONT, print_writers, "Anim_LtChange$Dynamic", "SHOW 0.0");
						this.whichGraphicOnScreen = whatToProcess;
						break;
				 }
				break;
				
			 case "Control_x": case "Control_z": case "z": case "x": case "c": case "v": case "Control_c": case "Control_v":
				 if(!whichGraphicOnScreen.split(",")[0].equalsIgnoreCase(whatToProcess.split(",")[0])) {
					 processAnimation(Constants.BACK, print_writers, "Change$Header", "SHOW 0.0");
				 }
				 processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Leader_Board", "SHOW 3.000");
				 processAnimation(Constants.BACK, print_writers, "Change$Leader_Board", "SHOW 0.0");
				
				 for(int iPlyr = 1; iPlyr <= 5; iPlyr++) {
					 if(iPlyr == Integer.valueOf(prevWhichPlayer)) {
						 processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side1$Player" + iPlyr, "SHOW 2.700");
					 } else {
						 processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side1$Player" + iPlyr, "SHOW 0.00");
					 }
				 }
				processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side2", "SHOW 0.0");
				 
				 setVariousAnimationsKeys("CUT-BACK", print_writers, config);
				 this.whichGraphicOnScreen = whatToProcess;
				 break;
				
			case "F1": case "F2": case "F4": case "Control_F11": case "Shift_T": case "p": case "Control_p":
				processAnimation(Constants.BACK, print_writers, "Change$Header", "SHOW 0.0");
				processAnimation(Constants.BACK, print_writers, "Change$Footer", "SHOW 0.0");
				switch(whichGraphicOnScreen.split(",")[0]) {
				case "F1":  
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Batting_Card", "SHOW 2.500");
					processAnimation(Constants.BACK, print_writers, "Change$Batting_Card", "SHOW 0.0");
					break;
				case "F2":  
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Bowling_Card", "SHOW 2.500");
					processAnimation(Constants.BACK, print_writers, "Change$Bowling_Card", "SHOW 0.0");
					break;
				case "F4":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Partnership_List", "SHOW 2.500");
					processAnimation(Constants.BACK, print_writers, "Change$Partnership_List", "SHOW 0.0");
					break;
				case "Control_F11":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Summary", "SHOW 2.500");
					processAnimation(Constants.BACK, print_writers, "Change$Summary", "SHOW 0.0");
					break;
				case "Shift_T":
					processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "SHOW 0.500");
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$LineUp_Image", "SHOW 2.500");
					processAnimation(Constants.BACK, print_writers, "Change$LineUp_Image", "SHOW 0.0");
					
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Sponsor", "SHOW 2.500");
					processAnimation(Constants.BACK, print_writers, "Change$Sponsor", "SHOW 0.0");
					break;
				case "p":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Group_Standings", "SHOW 2.500");
					processAnimation(Constants.BACK, print_writers, "Change$Group_Standings", "SHOW 0.0");
					break;
				case "Control_p":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Standings", "SHOW 2.500");
					processAnimation(Constants.BACK, print_writers, "Change$Standings", "SHOW 0.0");
					break;
				}
				if(!whichGraphicOnScreen.split(",")[0].equalsIgnoreCase(whatToProcess.split(",")[0])) {
					switch(whatToProcess.split(",")[0]) {
					case "F1":  
						processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "SHOW 0.0");
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Batting_Card", "SHOW 2.500");
						processAnimation(Constants.BACK, print_writers, "Change$Batting_Card", "SHOW 0.0");
						
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Sponsor", "SHOW 2.500");
						processAnimation(Constants.BACK, print_writers, "Change$Sponsor", "SHOW 0.0");
						break;
					case "F2":
						processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "SHOW 0.0");
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Bowling_Card", "SHOW 2.500");
						processAnimation(Constants.BACK, print_writers, "Change$Bowling_Card", "SHOW 0.0");
						
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Sponsor", "SHOW 2.500");
						processAnimation(Constants.BACK, print_writers, "Change$Sponsor", "SHOW 0.0");
						break;
					case "F4":
						processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "SHOW 0.0");
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Partnership_List", "SHOW 2.500");
						processAnimation(Constants.BACK, print_writers, "Change$Partnership_List", "SHOW 0.0");
						break;
					case "Control_F11":
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Summary", "SHOW 2.500");
						processAnimation(Constants.BACK, print_writers, "Change$Summary", "SHOW 0.0");
						processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "SHOW 0.500");
						break;
					case "p":
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Group_Standings", "SHOW 2.500");
						processAnimation(Constants.BACK, print_writers, "Change$Group_Standings", "SHOW 0.0");
						break;
					case "Control_p":
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Standings", "SHOW 2.500");
						processAnimation(Constants.BACK, print_writers, "Change$Standings", "SHOW 0.0");
						processAnimation(Constants.BACK, print_writers, "Change$Footer$Dynamic", "SHOW 0.500");
						break;
					}
				}
				setVariousAnimationsKeys("CUT-BACK", print_writers, config);
				processAnimation(Constants.BACK, print_writers, "ConcussExtend_Y", "SHOW 0.0");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			}
			break;	
			
		case Constants.ISPL:

			if(!whatToProcess.contains(",")) {
				return CricketUtil.NO;
			}
			
			switch(whatToProcess.split(",")[0]) {
			case "Shift_F12":
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$IdentInfo$Change", "SHOW 0.0");
				break;
			case "Shift_F1": case "Shift_F2":
				processAnimation(Constants.FRONT, print_writers, "Anim_MiniChange", "SHOW 0.0");
				break;
			case "Shift_F": case "y": case "Shift_O":
				processAnimation(Constants.FRONT, print_writers, "Anim_BugsChange", "SHOW 0.0");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Alt_1": case "Alt_9": case "Alt_0":
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage3_Change", "SHOW 0.0");
				break;
			case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_5": case "Alt_6": 
				TimeUnit.MILLISECONDS.sleep(1000);
				if(!whatToProcess.split(",")[2].equalsIgnoreCase(CricketUtil.BATSMAN) && 
						infobar.getMiddle_section() != null && !infobar.getMiddle_section().isEmpty()) {
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage1_Change", "SHOW 0.0");
				}
				break;
			case "Alt_7":
				if(!whatToProcess.split(",")[2].equalsIgnoreCase(CricketUtil.BOWLER) && 
						infobar.getRight_bottom() != null && !infobar.getRight_bottom().isEmpty()) {
					processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage2_Change", "SHOW 0.0");
				}
				break;
			case "Alt_8":
//				if(!whatToProcess.split(",")[2].equalsIgnoreCase(CricketUtil.BOWLER)) {
//					if(infobar.getRight_section()!= null && !infobar.getRight_section().isEmpty()) {
//						processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bowl_All$Change", "SHOW 0.0");
//					}
//				}
				break;
			case "Control_s": case "Control_f": case "F7": case "F11":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "SHOW 0.0");
					this.whichGraphicOnScreen = whatToProcess;
					break;
				case Constants.ISPL:
					processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "SHOW 0.0");
					processAnimation(Constants.FRONT, print_writers, "Anim_LtChange$Lt_X_Position", "SHOW 0.900");
					this.whichGraphicOnScreen = whatToProcess;
					break;
			 }
			break;
			case "F5": case "F6": case "F9": case "Control_g": case "Control_h": case "Alt_F1": case "Alt_F2":
			case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_o": case "Control_F2":
			case "Control_F6": case "Shift_F6": case "l": case "a": //case "n":
			case "Alt_F6": case "Shift_A":  case "Shift_R": case "Shift_U": case "Alt_w": case "Control_j": case "Alt_i": case "b":
			case "Alt_j":	
				switch (config.getBroadcaster().toUpperCase()) {
					case Constants.ICC_U19_2023:
						processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "SHOW 0.0");
						this.whichGraphicOnScreen = whatToProcess;
						break;
					case Constants.ISPL:
						processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "SHOW 0.0");
						processAnimation(Constants.FRONT, print_writers, "Anim_LtChange$Lt_X_Position", "SHOW 0.900");
						this.whichGraphicOnScreen = whatToProcess;
						break;
				 }
				break;
			 case "Alt_F8": case "F8": case "F10": case "j": case "Alt_a": case "Alt_s":
				 switch (config.getBroadcaster().toUpperCase()) {
					case Constants.ICC_U19_2023:
						processAnimation(Constants.FRONT, print_writers, "Anim_NameSuperChange", "SHOW 0.0");
						this.whichGraphicOnScreen = whatToProcess;
						break;
					case Constants.ISPL:
						processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "SHOW 0.0");
						this.whichGraphicOnScreen = whatToProcess;
						break;
				 }
				break;
			 case "Shift_F7": case "Control_F4":
				 processAnimation(Constants.FRONT, print_writers, "Anim_Image_LtChange", "SHOW 0.0");
				this.whichGraphicOnScreen = whatToProcess;
				 break;
			 case "Control_x": case "Control_z": case "z": case "x": case "c": case "v":
				 if(!whichGraphicOnScreen.split(",")[0].equalsIgnoreCase(whatToProcess.split(",")[0])) {
					 processAnimation(Constants.BACK, print_writers, "Change$Header", "SHOW 0.0");
				 }
				 processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$LeaderBoard", "SHOW 2.2400");
				 processAnimation(Constants.BACK, print_writers, "Change$LeaderBoard", "SHOW 0.0");
				
				 for(int iPlyr = 1; iPlyr <= 5; iPlyr++) {
					 if(iPlyr == Integer.valueOf(prevWhichPlayer)) {
						 processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side1$Player" + iPlyr, "SHOW 1.740");
					 } else {
						 processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side1$Player" + iPlyr, "SHOW 0.00");
					 }
				 }
				processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight$Side2", "SHOW 0.0");
				 
//				 setVariousAnimationsKeys("CUT-BACK", print_writers, config);
				 this.whichGraphicOnScreen = whatToProcess;
				 break;
				
			 case "F1": case "F2": case "F4": case "Shift_T": case "p": case "Control_p": case "Shift_F8": case "Control_F11":
			 case "Shift_K":	 
					processAnimation(Constants.BACK, print_writers, "Change$Header", "SHOW 0.0");
//					processAnimation(Constants.BACK, print_writers, "Change$Footer", "SHOW 0.0");
					switch(whichGraphicOnScreen.split(",")[0]) {
					case "F1":  
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Batting_Card", "SHOW 2.240");
						processAnimation(Constants.BACK, print_writers, "Change$Batting_Card", "SHOW 0.0");
						break;
					case "F2":  
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Bowling_Card", "SHOW 2.240");
						processAnimation(Constants.BACK, print_writers, "Change$Bowling_Card", "SHOW 0.0");
						break;
					case "F4":
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Partnership_List", "SHOW 2.240");
						processAnimation(Constants.BACK, print_writers, "Change$Partnership_List", "SHOW 0.0");
						break;
					case "Control_F11":
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Summary", "SHOW 2.240");
						processAnimation(Constants.BACK, print_writers, "Change$Summary", "SHOW 0.0");
						break;
					case "Shift_K":
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Partnership", "SHOW 2.240");
						processAnimation(Constants.BACK, print_writers, "Change$Partnership", "SHOW 0.0");
						break;	
					case "Shift_T":
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Team_Single", "SHOW 2.240");
						processAnimation(Constants.BACK, print_writers, "Change$Team_Single", "SHOW 0.0");
						break;
					case "Shift_F8":
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$LineUp_Image", "SHOW 2.240");
						processAnimation(Constants.BACK, print_writers, "Change$LineUp_Image", "SHOW 0.0");
						break;	
					case "p":
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Group_Standings", "SHOW 2.240");
						processAnimation(Constants.BACK, print_writers, "Change$Group_Standings", "SHOW 0.0");
						break;
					case "Control_p":
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Standings", "SHOW 2.240");
						processAnimation(Constants.BACK, print_writers, "Change$Standings", "SHOW 0.0");
						break;
					}
					if(!whichGraphicOnScreen.split(",")[0].equalsIgnoreCase(whatToProcess.split(",")[0])) {
						switch(whatToProcess.split(",")[0]) {
						case "F1":  
//							processAnimation(Constants.BACK, print_writers, "Header_Shrink", "SHOW 0.0");
							processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Batting_Card", "SHOW 2.240");
							processAnimation(Constants.BACK, print_writers, "Change$Batting_Card", "SHOW 0.0");
							break;
						case "F2":
//							processAnimation(Constants.BACK, print_writers, "Header_Shrink", "SHOW 0.0");
							processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Bowling_Card", "SHOW 2.240");
							processAnimation(Constants.BACK, print_writers, "Change$Bowling_Card", "SHOW 0.0");
							break;
						case "F4":
							processAnimation(Constants.BACK, print_writers, "Header_Shrink", "SHOW 0.0");
							processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Partnership_List", "SHOW 2.240");
							processAnimation(Constants.BACK, print_writers, "Change$Partnership_List", "SHOW 0.0");
							break;
						case "Control_F11":
							processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Summary", "SHOW 2.240");
							processAnimation(Constants.BACK, print_writers, "Change$Summary", "SHOW 0.0");
							break;
						case "Shift_K":
							processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Partnership", "SHOW 2.240");
							processAnimation(Constants.BACK, print_writers, "Change$Partnership", "SHOW 0.0");
							break;	
						case "p":
							processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Group_Standings", "SHOW 2.240");
							processAnimation(Constants.BACK, print_writers, "Change$Group_Standings", "SHOW 0.0");
							break;
						case "Control_p":
							processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Main$Standings", "SHOW 2.240");
							processAnimation(Constants.BACK, print_writers, "Change$Standings", "SHOW 0.0");
							break;
						}
					}
//					setVariousAnimationsKeys("CUT-BACK", print_writers, config);
//					processAnimation(Constants.BACK, print_writers, "ConcussExtend_Y", "SHOW 0.0");
				
				this.whichGraphicOnScreen = whatToProcess;
				break;
			}
			break;	
		}
		CricketFunctions.deletePreview();
		return CricketUtil.YES;
	}	
	public String ResetAnimation(String whatToProcess, List<PrintWriter> print_writers, Configuration config)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.BENGAL_T20:
			processAnimation(Constants.BACK, print_writers, "Anim_FullFrames", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Anim_Ident", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Anim_DoubleIdent", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "anim_Profile", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Change_Profile", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Change", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Anim_Highlights", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Anim_Target", "SHOW 0.0");
			break;
		case Constants.ICC_U19_2023: 

			processAnimation(Constants.BACK, print_writers, "Anim_FullFrames", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Change", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "ConcussExtend_Y", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Header_Shrink", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Profile_Highlight", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Base_Gradient", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Target", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "TargetLoop", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "In_At", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "In_At_Loop", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Milestone", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "MilestoneLoop", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Manhattan_Comparison", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Anim_Squad", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "SquadFlare_Loop", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Anim_SquadDataChange", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Anim_POTT", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "POTT_Loop", "SHOW 0.0");

			processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "anim_Boundary_LT", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_Boundary_LTChange", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_Bugs", "SHOW 0.0");
			
			processAnimation(Constants.FRONT, print_writers, "anim_POTT", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "anim_NameSupers", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_NameSuperChange", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_Center_Bug", "SHOW 0.0");
			
			processAnimation(Constants.FRONT, print_writers, "Anim_BugsChange", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_Mini", "SHOW 0.0");
//			processAnimation(Constants.FRONT, print_writers, "Anim_MiniChange", "SHOW 0.0");
			
			if(whatToProcess.contains("CLEAR-ALL-WITH-INFOBAR")) {
				processAnimation(Constants.FRONT, print_writers, "Anim_Infobar", "SHOW 0.0");
				this.infobar.setInfobar_on_screen(false);
				this.infobar.setInfobar_status("");
			}
			this.whichGraphicOnScreen = "";
			this.specialBugOnScreen = "";
			break;
		case Constants.ISPL:

			processAnimation(Constants.BACK, print_writers, "Anim_FullFrames", "SHOW 0.0");
//			processAnimation(Constants.BACK, print_writers, "Change", "SHOW 0.0");
//			processAnimation(Constants.BACK, print_writers, "ConcussExtend_Y", "SHOW 0.0");
//			processAnimation(Constants.BACK, print_writers, "Header_Shrink", "SHOW 0.0");
//			processAnimation(Constants.BACK, print_writers, "Profile_Highlight", "SHOW 0.0");
//			processAnimation(Constants.BACK, print_writers, "Base_Gradient", "SHOW 0.0");
//			processAnimation(Constants.BACK, print_writers, "Target", "SHOW 0.0");
//			processAnimation(Constants.BACK, print_writers, "TargetLoop", "SHOW 0.0");
//			processAnimation(Constants.BACK, print_writers, "In_At", "SHOW 0.0");
//			processAnimation(Constants.BACK, print_writers, "In_At_Loop", "SHOW 0.0");
//			processAnimation(Constants.BACK, print_writers, "Milestone", "SHOW 0.0");
//			processAnimation(Constants.BACK, print_writers, "MilestoneLoop", "SHOW 0.0");
//			processAnimation(Constants.BACK, print_writers, "Manhattan_Comparison", "SHOW 0.0");
//			processAnimation(Constants.BACK, print_writers, "Anim_Squad", "SHOW 0.0");
//			processAnimation(Constants.BACK, print_writers, "SquadFlare_Loop", "SHOW 0.0");
//			processAnimation(Constants.BACK, print_writers, "Anim_SquadDataChange", "SHOW 0.0");
//			processAnimation(Constants.BACK, print_writers, "LeaderBoardHighlight", "SHOW 0.0");
//			processAnimation(Constants.BACK, print_writers, "Anim_POTT", "SHOW 0.0");
//			processAnimation(Constants.BACK, print_writers, "POTT_Loop", "SHOW 0.0");

			processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_Image_LT", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_Image_LtChange", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_Bugs", "SHOW 0.0");
//			processAnimation(Constants.FRONT, print_writers, "Anim_MiniPoints$In_Out", "Show 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_MiniPoints", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_Center_Bug", "SHOW 0.0");
			
			processAnimation(Constants.FRONT, print_writers, "Anim_BugsChange", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_Mini", "SHOW 0.0");
			
			if(whatToProcess.contains("CLEAR-ALL")) {
//				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Small$In", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage3_In", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage2_In", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Stage1_In", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Batsman1_In", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Batsman2_In", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$StrikeIn", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Bowler_In", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$Target_In", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$PowerPlay_In", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$ToWin", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$TapedBall_In", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$TOPRIGHT_FREETEXT_In", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TimeLine", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Ident_In", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$IdentInfo$In_Out", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$TopStage", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Out", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$In", "SHOW 0.0");
				processAnimation(Constants.FRONT, print_writers, "Anim_InfoBar$Main$Main_In", "SHOW 0.0");
				this.infobar.setInfobar_on_screen(false);
				this.infobar.setInfobar_status("");
				infobar.setMiddle_section("");
				infobar.setFull_section("");
				infobar.setRight_bottom("");
				infobar.setRight_section("");
			}
			this.whichGraphicOnScreen = "";
			this.specialBugOnScreen = "";
			break;	
		}
		return CricketUtil.YES;
	}
	public void processAnimation(String whichLayer, List<PrintWriter> print_writers,
		String animationDirectorName, String animationCommand)
	{
		if(!whichLayer.isEmpty()) {
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*" + whichLayer + "_LAYER*STAGE*DIRECTOR*"
				+ animationDirectorName + " " + animationCommand +"\0", print_writers);
		} else {
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*STAGE*DIRECTOR*"
				+ animationDirectorName + " " + animationCommand +"\0", print_writers);
		}
	}
	
	public void processQuidichCommands(String whatToProcess, List<PrintWriter> print_writers, Configuration config) throws InterruptedException
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			switch(whatToProcess) {
			case "5": // Reset
				print_writers.get(print_writers.size()-1).printf("%s","F4");
				break;
			case "6": // Stand-By
				print_writers.get(print_writers.size()-1).printf("%s","F6");
				break;
			case "7": //Animate-In
				print_writers.get(print_writers.size()-1).printf("%s","F6");
				TimeUnit.MILLISECONDS.sleep(100);
				print_writers.get(print_writers.size()-1).printf("%s","F7");
				break;
			case "8": //Animate-Out
				print_writers.get(print_writers.size()-1).printf("%s","F9");
				TimeUnit.MILLISECONDS.sleep(1000);
				print_writers.get(print_writers.size()-1).printf("%s","F4");
				break;
			case "9": //Load	
				print_writers.get(print_writers.size()-1).printf("%s","LOAD");
				break;
			}
			break;
		}
	}
	
	public void setVariousAnimationsKeys(String whatToProcess, List<PrintWriter> print_writers, Configuration config) 
	{
		switch (config.getBroadcaster()) {
		case Constants.ICC_U19_2023: case Constants.ISPL:
			
			float MoveForExtraData, BasePositionY = 0f, obj_BiggerBase = 0f, obj__Mask_6_ = 0f, PositionY = 0f, Sponsor = 0f;
			
			switch(caption.this_fullFramesGfx.numberOfRows) {
			case 10:
				MoveForExtraData = -25f;
				BasePositionY = 25f;
				obj_BiggerBase = 690f;
				obj__Mask_6_ = 690f;
				PositionY = 50f;
				Sponsor = -330f;
				break;
			case 12:
				MoveForExtraData = 25f;
				BasePositionY = -25f;
				obj_BiggerBase = 790f;
				obj__Mask_6_ = 790f;
				PositionY = -50f;
				Sponsor = -430f;
				break;
			case 13:
				MoveForExtraData = 50f;
				BasePositionY = -50f;
				obj_BiggerBase = 840f;
				obj__Mask_6_ = 840f;
				PositionY = -100f;
				Sponsor = -480f;
				break;
			default: // 11 straps
				MoveForExtraData = 0f;
				BasePositionY = 0f;
				obj_BiggerBase = 740f;
				obj__Mask_6_ = 740f;
				PositionY = 0f;
				Sponsor = -380f;
			}
//			System.out.println("setVariousAnimationsKeys -> whatToProcess = " + whatToProcess + ": Sponsor = " + Sponsor 
//				+ ": numberOfRows = " + caption.this_fullFramesGfx.numberOfRows);
//			System.out.println("setVariousAnimationsKeys: whatToProcess = " + whatToProcess);
			switch (whatToProcess) {
			case "ANIMATE-IN":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$MoveForExtraData"
					+ "*ANIMATION*KEY*$ED_In_1*VALUE SET 0.0 " + MoveForExtraData + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$MoveForExtraData"
					+ "*ANIMATION*KEY*$ED_Out_1*VALUE SET 0.0 " + MoveForExtraData + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$BasePositionY"
					+ "*ANIMATION*KEY*$E_In_1*VALUE SET 0.0 " + BasePositionY + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$BasePositionY"
					+ "*ANIMATION*KEY*$E_Out_1*VALUE SET 0.0 " + BasePositionY + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$obj_BiggerBase"
					+ "*ANIMATION*KEY*$BB_In_1*VALUE SET " + obj_BiggerBase + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$obj_BiggerBase"
					+ "*ANIMATION*KEY*$BB_Out_1*VALUE SET " + obj_BiggerBase + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$obj__Mask_6_"
					+ "*ANIMATION*KEY*$MA_In_1*VALUE SET " + obj__Mask_6_ + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$obj__Mask_6_"
					+ "*ANIMATION*KEY*$MA_Out_1*VALUE SET " + obj__Mask_6_ + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$FooterAll$Footer$PositionY"
					+ "*ANIMATION*KEY*$F_In_1*VALUE SET 0.0 " + PositionY + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$FooterAll$Footer$PositionY"
					+ "*ANIMATION*KEY*$F_Out_1*VALUE SET 0.0 " + PositionY + " 0.0 \0", print_writers);

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor"
					+ "*ANIMATION*KEY*$S_In_1*VALUE SET 0.0 " + Sponsor + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor"
					+ "*ANIMATION*KEY*$In_1*VALUE SET 0.0 " + Sponsor + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor"
					+ "*ANIMATION*KEY*$In_2*VALUE SET 0.0 " + Sponsor + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor"
					+ "*ANIMATION*KEY*$Out_1*VALUE SET 0.0 " + Sponsor + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor"
					+ "*ANIMATION*KEY*$Out_2*VALUE SET 0.0 " + Sponsor + " 0.0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$MoveForExtraData"
					+ "*ANIMATION*KEY*$In_1*VALUE SET 0.0 " + MoveForExtraData + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$BasePositionY"
					+ "*ANIMATION*KEY*$In_1*VALUE SET 0.0 " + BasePositionY + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$obj_BiggerBase"
					+ "*ANIMATION*KEY*$In_1*VALUE SET " + obj_BiggerBase + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$obj__Mask_6_"
					+ "*ANIMATION*KEY*$In_1*VALUE SET " + obj__Mask_6_ + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$FooterAll$Footer$PositionY"
					+ "*ANIMATION*KEY*$In_1*VALUE SET 0.0 " + PositionY + " 0.0 \0", print_writers);
				
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor"
//					+ "*ANIMATION*KEY*$In_1*VALUE SET 0.0 " + Sponsor + " 0.0 \0", print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor"
//					+ "*ANIMATION*KEY*$Out_1*VALUE SET 0.0 " + Sponsor + " 0.0 \0", print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor"
//					+ "*ANIMATION*KEY*$In_2*VALUE SET 0.0 " + Sponsor + " 0.0 \0", print_writers);
			
				break;
			case "CHANGE-ON":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$MoveForExtraData"
					+ "*ANIMATION*KEY*$In_2*VALUE SET 0.0 " + MoveForExtraData + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$BasePositionY"
					+ "*ANIMATION*KEY*$In_2*VALUE SET 0.0 " + BasePositionY + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$obj_BiggerBase"
					+ "*ANIMATION*KEY*$In_2*VALUE SET " + obj_BiggerBase + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$obj__Mask_6_"
					+ "*ANIMATION*KEY*$In_2*VALUE SET " + obj__Mask_6_ + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$FooterAll$Footer$PositionY"
					+ "*ANIMATION*KEY*$In_2*VALUE SET 0.0 " + PositionY + " 0.0 \0", print_writers);
				if(caption.this_fullFramesGfx.numberOfRows != lastNumberOfRows) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor"
							+ "*ANIMATION*KEY*$S_In_1*VALUE SET 0.0 " + Sponsor + " 0.0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor"
						+ "*ANIMATION*KEY*$Out_1*VALUE SET 0.0 " + Sponsor + " 0.0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor"
						+ "*ANIMATION*KEY*$In_2*VALUE SET 0.0 " + Sponsor + " 0.0 \0", print_writers);
				}
				break;
			case "CUT-BACK":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$MoveForExtraData"
					+ "*ANIMATION*KEY*$In_1*VALUE SET 0.0 " + MoveForExtraData + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$BasePositionY"
					+ "*ANIMATION*KEY*$In_1*VALUE SET 0.0 " + BasePositionY + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$obj_BiggerBase"
					+ "*ANIMATION*KEY*$In_1*VALUE SET " + obj_BiggerBase + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$obj__Mask_6_"
					+ "*ANIMATION*KEY*$In_1*VALUE SET " + obj__Mask_6_ + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$FooterAll$Footer$PositionY"
					+ "*ANIMATION*KEY*$In_1*VALUE SET 0.0 " + PositionY + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor"
					+ "*ANIMATION*KEY*$S_In_1*VALUE SET 0.0 " + Sponsor + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor"
					+ "*ANIMATION*KEY*$In_1*VALUE SET 0.0 " + Sponsor + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor"
					+ "*ANIMATION*KEY*$In_2*VALUE SET 0.0 " + Sponsor + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor"
					+ "*ANIMATION*KEY*$Out_1*VALUE SET 0.0 " + Sponsor + " 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor"
					+ "*ANIMATION*KEY*$Out_2*VALUE SET 0.0 " + Sponsor + " 0.0 \0", print_writers);
				break;
			}
			break;
		}
	}
	
	public void processFullFramesPreview(String whatToProcess, List<PrintWriter> print_writer, int whichside, 
			Configuration config,String whichGraphicOnScreen) 
		{
		
		if(config.getPreview().equalsIgnoreCase("WITH_PREVIEW")) {
			String previewCommand = "";
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.BENGAL_T20:
				if(whichside == 1) {
					if(whatToProcess.contains(",")) {
						switch(whatToProcess.split(",")[0]) {
						case "m": case "Control_m":
							previewCommand = "Anim_Infobar$Push 0.500 Anim_Ident$In 2.000 ";
							break;
						case "Control_Shift_D":
							previewCommand = "Anim_Infobar$Push 0.500 Anim_DoubleIdent$In 2.000 ";
							break;
						
						case "Control_d": case "Control_e":
							previewCommand = "Anim_Infobar$Push 0.500 anim_Profile 1.700 anim_Profile$Essentials$In 1.140 anim_Profile$Main$In 1.140";
							break;
							
						case "F1": case "F2": case "F4": case "Control_F1": case "Shift_F10": case "Control_F11": case "Shift_F11": case "p": case "Control_p":
						case "Shift_T": case "Control_F7": case "Control_F10": case "Alt_F9": case "Shift_K":case "z": case "x": case "c": case "v": case "Alt_F10": 
						case "Shift_P": case "Shift_Q": case "Alt_F11": case "Control_z": case "Control_x": case "Control_Shift_F1":
							if(!whatToProcess.split(",")[0].equalsIgnoreCase("Shift_K")) {
								previewCommand = "Anim_Infobar$Push 0.500 Anim_FullFrames$In_Out$Essentials$In 2.200 Anim_FullFrames$In_Out$Header$In 1.900 "
										+ "Anim_FullFrames$In_Out$Footer$In 2.200";
							}else {
								previewCommand = "Anim_Infobar$Push 0.500 Anim_FullFrames$In_Out$Essentials$In 2.200 Anim_FullFrames$In_Out$Header$In 1.900";
							}
							break;
						}
						switch(whatToProcess.split(",")[0]) {
						case "F1"://battingCard
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Batting_Card$In 2.180 Anim_FullFrames$In_Out$Sponsor$In 2.420";
							break;
						case "Control_Shift_F1":
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Batting_Card$In 1.860 Anim_FullFrames$In_Out$Extra_Info$In 2.500 "
									+ "Anim_Highlights$Batting_Card$Side1$" + caption.this_fullFramesGfx.batperformer_id + " 0.500";
							break;
						case "F2"://bowlingCard
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Bowling_Card$In 2.100 Anim_FullFrames$In_Out$Sponsor$In 2.420";
							break;
						case "F4": //All Partnership
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Partnership_List$In 2.180";
							if(whichside == 1 && caption.this_fullFramesGfx.whichSponsor != null && !caption.this_fullFramesGfx.whichSponsor.isEmpty()) {
								previewCommand = previewCommand + " Sponsor 0.900 Sponsor$In 0.900";
							}
							break;
						case "Control_F7":// Double Teams
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Teams$In 2.140 Change$Footer$Dynamic 0.500";
							break;
						case "Shift_T": //Playing XI
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$LineUp_Image$In 1.960 Anim_FullFrames$In_Out$Sponsor$In 2.420 "
									+ "Change$Footer$Dynamic 0.500";
							break;
						case "Control_F1":// Photo ScoreCard
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Batting_Card_Image$In 2.040";
							break;
						case "Control_F10"://Manhattan
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Manhattan$In 2.500";
							break;
						case "Shift_F10"://WORMS
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Worm$In 2.440 Anim_FullFrames$In_Out$Main$Worm$In$Runs 2.440";
							break;
						case "Control_F11": case "Shift_F11": //MATCH SUMMARY
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Summary$In 2.200 Change$Footer$Dynamic 0.500";
							break;
						case "p": // PointsTable
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Group_Standings$In 1.540";
							break;
						case "Control_p": // PointsTable
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Standings$In 2.020 Change$Footer$Dynamic 0.500";
							break;
						case "z": case "x": case "c": case "v": case "Control_z": case "Control_x": //LeaderBoard Most - Runs,Wickets,Fours,Sixes 
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Leader_Board$In 2.300";
							previewCommand = previewCommand + " LeaderBoardHighlight$Side1$Player"+whatToProcess.split(",")[2].split("_")[0] + " 2.700";
							break;
						case "Shift_P": case "Shift_Q"://PlayerProfile
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Profile$In 2.300";
//							if(Integer.valueOf(whatToProcess.split(",")[4]) > 0) {
//								previewCommand = previewCommand + " Profile_Highlight$Side1$" + whatToProcess.split(",")[4] + " 1.000";
//							}	
							break;
						case "Shift_K"://FFCurrPartnership
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Partnership$In 3.000";
							break;
						case "Alt_F9": case "Alt_F10": // Single Teams
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Team_Single$In 3.000";
							break;
						case "Shift_D": // target
							previewCommand = previewCommand + " Anim_Target 2.000 Anim_Target$In 2.000";
							break;
						case "Control_b": // target
							previewCommand = previewCommand + " In_At 2.140 In_At$In 2.140 In_At$In$Data 2.140";
							break;
						case "Alt_m": case "Alt_n":// target
							previewCommand = previewCommand + " Milestone 2.140 Milestone$In 2.140 Milestone$In$Data 2.140";
							break;
						case "Alt_F11":
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Manhattan_Comparison$In 3.000";
							break;
						case "Alt_z":
							previewCommand = previewCommand + " Anim_Squad$In_Out 2.200 Anim_Squad$In_Out$In 2.200";
							if(!caption.this_fullFramesGfx.WhichType.equalsIgnoreCase("role")) {
								previewCommand = previewCommand + " Anim_SquadDataChange 0.500";
							}
							break;
						case "r":
							previewCommand = previewCommand + " Anim_POTT$In_Out$In 2.140";
							break;
							
						}
					}
				} else if(whichside == 2) {
					if(whatToProcess.contains(",")) {
						switch(whatToProcess.split(",")[0]) {
						case "F1": case "F2": case "F4": case "Control_F11": case "Shift_T": case "p": case "Control_p":
						case "z": case "x": case "c": case "v": case "Control_z": case "Control_x":
							previewCommand = previewCommand + " Change$Header 1.320  Change$Header$Change_In 1.320 Change$Header$Change_Out 0.420";
							if(whichGraphicOnScreen.contains(",")) {
								switch(whichGraphicOnScreen.split(",")[0]) {
								case "F1":  
									previewCommand = previewCommand + " Change$Batting_Card 1.180 Change$Batting_Card$Change_Out 0.540 Change$Batting_Card$Change_In 1.180 "
											+ "Change$Sponsor 0.500 Change$Sponsor$Change_Out 0.300 Change$Sponsor$Change_In 0.500";
									break;
								case "F2":  
									previewCommand = previewCommand + " Change$Bowling_Card 1.100 Change$Bowling_Card$Change_Out 0.500 Change$Bowling_Card$Change_In 1.100 "
											+ "Change$Sponsor 0.500 Change$Sponsor$Change_Out 0.300 Change$Sponsor$Change_In 0.500";
									break;
								case "F4":
									previewCommand = previewCommand + " Change$Partnership_List 1.180 Change$Partnership_List$Change_Out 0.540 "
										+ "Change$Partnership_List$Change_In 1.180";
									break;
								case "Control_F11":
									previewCommand = previewCommand + " Change$Summary 1.200 Change$Summary$Change_Out 0.480 Change$Summary$Change_In 1.200";
									break;
								case "Shift_T":
									previewCommand = previewCommand + " Change$LineUp_Image 0.960 Change$LineUp_Image$Change_Out 0.440 Change$LineUp_Image$Change_In 0.960 "
											+ "Change$Sponsor 0.500 Change$Sponsor$Change_Out 0.300 Change$Sponsor$Change_In 0.500";
									break;
								case "p":
									previewCommand = previewCommand + " Change$Group_Standings 1.040 Change$Group_Standings$Change_Out 0.624 Change$Group_Standings$Change_In 1.040";
									break;
								case "Control_p":
									previewCommand = previewCommand + " Change$Standings 1.020 Change$Standings$Change_Out 0.460 Change$Standings$Change_In 1.020";
									break;
								case "z": case "x": case "c": case "v": case "Control_z": case "Control_x": //LeaderBoard Most - Runs,Wickets,Fours,Sixes 
									previewCommand = previewCommand + " Change$Leader_Board 2.800 Change$Leader_Board$Change_Out 0.600 Change$Leader_Board$Change_In 2.800";
									previewCommand = previewCommand + " LeaderBoardHighlight$Side2$Player"+whatToProcess.split(",")[2].split("_")[0] + " 2.700";
									break;
								}
							}
							if(!whichGraphicOnScreen.split(",")[0].equalsIgnoreCase(whatToProcess.split(",")[0])) {
								switch(whatToProcess.split(",")[0]) {
								case "F1": case "F2": case "F4":
									previewCommand = previewCommand + " Header_Shrink 0.000 Header_Shrink$In 0.000";
									switch(whatToProcess.split(",")[0]) {
									case "F4":
										if(caption.this_fullFramesGfx.whichSponsor != null && !caption.this_fullFramesGfx.whichSponsor.isEmpty()) {
											previewCommand = previewCommand + " Change_Sponsor 1.000 Change_Sponsor$Change_Our 0.500 Change_Sponsor$Change_In 1.000";
										}
										break;
									}
									break;
								case "Control_F11": case "p": case "Control_p":
									previewCommand = previewCommand + " Header_Shrink 0.500 Header_Shrink$In 0.500";
									break;
								}
								switch(whatToProcess.split(",")[0]) {
								case "F1":  
									previewCommand = previewCommand + " Change$Batting_Card 1.180 Change$Batting_Card$Change_Out 0.540 Change$Batting_Card$Change_In 1.180"
											+ "Change$Sponsor 0.500 Change$Sponsor$Change_Out 0.300 Change$Sponsor$Change_In 0.500";
									break;
								case "F2":  
									previewCommand = previewCommand + " Change$Bowling_Card 1.100 Change$Bowling_Card$Change_Out 0.500 Change$Bowling_Card$Change_In 1.100"
											+ "Change$Sponsor 0.500 Change$Sponsor$Change_Out 0.300 Change$Sponsor$Change_In 0.500";
									break;
								case "F4":
									previewCommand = previewCommand + " Change$Partnership_List 1.180 Change$Partnership_List$Change_Out 0.540 "
										+ "Change$Partnership_List$Change_In 1.180";
									break;
								case "Control_F11":
									previewCommand = previewCommand + " Change$Summary 1.200 Change$Summary$Change_Out 0.480 Change$Summary$Change_In 1.200";
									break;
								case "Shift_T":
									previewCommand = previewCommand + " Change$LineUp_Image 0.960 Change$LineUp_Image$Change_Out 0.440 Change$LineUp_Image$Change_In 0.960"
											+ "Change$Sponsor 0.500 Change$Sponsor$Change_Out 0.300 Change$Sponsor$Change_In 0.500";
									break;
								case "p":
									previewCommand = previewCommand + " Change$Group_Standings 1.040 Change$Group_Standings$Change_Out 0.624 Change$Group_Standings$Change_In 1.040";
									break;
								case "Control_p":
									previewCommand = previewCommand + " Change$Standings 1.020 Change$Standings$Change_Out 0.460 Change$Standings$Change_In 1.020";
									break;
								}
							}
							previewCommand = previewCommand + " Change$Footer 0.700 Change$Footer$Change_In 0.700 Change$Footer$Chnage_Out 0.500";
//							System.out.println("Number of rows : " + caption.this_fullFramesGfx.numberOfRows);
//							System.out.println("L Number of rows : " + lastNumberOfRows);
//							if(caption.this_fullFramesGfx.numberOfRows != lastNumberOfRows) {
//								previewCommand = previewCommand + " ConcussExtend_Y 0.500 ConcussExtend_Y$In 0.500";
//							}
							break;
						}
					}
				}
//				System.out.println("previewCommand = " + previewCommand);
			    CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*/Default/FullFrames " + "C:/Temp/Preview.png " + previewCommand + " \0", print_writer);
				break;
			case Constants.ICC_U19_2023:
				if(whichside == 1) {
					if(whatToProcess.contains(",")) {
						switch(whatToProcess.split(",")[0]) {
						case "F1": case "F2": case "F4": case "Control_F1": case "Shift_F10": case "Control_F11": case "Shift_F11": case "m": case "Control_m": case "p": case "Control_p":
						case "Shift_T": case "Control_d": case "Control_e": case "Control_F7": case "Control_F10": case "Alt_F9": case "Shift_K":
						case "z": case "x": case "c": case "v": case "Alt_F10": case "Shift_P": case "Shift_Q": case "Alt_F11": case "Control_z": case "Control_x":
							previewCommand = "Anim_Infobar$Push 0.500 Anim_FullFrames$In_Out$Essentials$In 2.140 Anim_FullFrames$In_Out$Header$In 1.800 "
								+ "Anim_FullFrames$In_Out$Footer$In 1.800";
							break;
						}
						switch(whatToProcess.split(",")[0]) {
						case "F1"://battingCard
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Batting_Card$In 1.860";
							break;
						case "F2"://bowlingCard
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Bowling_Card$In 1.780";
							break;
						case "F4": //All Partnership
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Partnership_List$In 1.820";
							if(whichside == 1 && caption.this_fullFramesGfx.whichSponsor != null && !caption.this_fullFramesGfx.whichSponsor.isEmpty()) {
								previewCommand = previewCommand + " Sponsor 0.900 Sponsor$In 0.900";
							}
							break;
						case "Control_F7":// Double Teams
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Teams$In 1.860";
							break;
						case "Shift_T": //Playing XI
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$LineUp_Image$In 2.040";
							break;
						case "Control_F1":// Photo ScoreCard
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Batting_Card_Image$In 2.040";
							break;
						case "Control_F10"://Manhattan
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Manhattan$In 2.900";
							break;
						case "Shift_F10"://WORMS
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Worm$In 2.440 Anim_FullFrames$In_Out$Main$Worm$In$Runs 2.440";
							break;
						case "Control_F11": case "Shift_F11": //MATCH SUMMARY
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Summary$In 1.820";
							break;
						case "p": // PointsTable
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Group_Standings$In 1.540";
							break;
						case "Control_p": // PointsTable
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Standings$In 1.620";
							break;
						case "z": case "x": case "c": case "v": case "Control_z": case "Control_x": //LeaderBoard Most - Runs,Wickets,Fours,Sixes 
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Leader_Board$In 2.300";
							previewCommand = previewCommand + " LeaderBoardHighlight$Side1$Player"+whatToProcess.split(",")[2].split("_")[0] + " 2.700";
							break;
						case "m"://Match id	
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Ident$In 1.920 Base_Gradient 0.500";
							break;
						case "Control_m": //MATCH PROMO
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Ident$In 1.920";
							break;
						case "Control_d": case "Control_e": case "Shift_P": case "Shift_Q"://PlayerProfile
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Profile$In 2.300";
							if(Integer.valueOf(whatToProcess.split(",")[4]) > 0) {
								previewCommand = previewCommand + " Profile_Highlight$Side1$" + whatToProcess.split(",")[4] + " 1.000";
							}	
							break;
						case "Shift_K"://FFCurrPartnership
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Partnership$In 3.000 Base_Gradient 0.500 Sponsor 0.900 Sponsor$In 0.900 Sponsor$Out 1.200";
							break;
						case "Alt_F9": case "Alt_F10": // Single Teams
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Team_Single$In 3.000";
							break;
						case "Shift_D": // target
							previewCommand = previewCommand + " Target 2.100 Target$In 2.100 Target$In$Data 2.100";
							break;
						case "Control_b": // target
							previewCommand = previewCommand + " In_At 2.140 In_At$In 2.140 In_At$In$Data 2.140";
							break;
						case "Alt_m": case "Alt_n":// target
							previewCommand = previewCommand + " Milestone 2.140 Milestone$In 2.140 Milestone$In$Data 2.140";
							break;
						case "Alt_F11":
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Manhattan_Comparison$In 3.000";
							break;
						case "Alt_z":
							previewCommand = previewCommand + " Anim_Squad$In_Out 2.200 Anim_Squad$In_Out$In 2.200";
							if(!caption.this_fullFramesGfx.WhichType.equalsIgnoreCase("role")) {
								previewCommand = previewCommand + " Anim_SquadDataChange 0.500";
							}
							break;
						case "r":
							previewCommand = previewCommand + " Anim_POTT$In_Out$In 2.140";
							break;
							
						}
					}
				} else if(whichside == 2) {
					if(whatToProcess.contains(",")) {
						switch(whatToProcess.split(",")[0]) {
						case "F1": case "F2": case "F4": case "Control_F11": case "Shift_T": case "p": case "Control_p":
						case "z": case "x": case "c": case "v": case "Control_z": case "Control_x":
							previewCommand = previewCommand + " Change$Header 1.320  Change$Header$Change_In 1.320 Change$Header$Change_Out 0.420";
							if(whichGraphicOnScreen.contains(",")) {
								switch(whichGraphicOnScreen.split(",")[0]) {
								case "F1":  
									previewCommand = previewCommand + " Change$Batting_Card 1.380 Change$Batting_Card$Change_Out 0.880 Change$Batting_Card$Change_In 1.380";
									break;
								case "F2":  
									previewCommand = previewCommand + " Change$Bowling_Card 1.300 Change$Bowling_Card$Change_Out 0.840 Change$Bowling_Card$Change_In 1.300";
									break;
								case "F4":
									previewCommand = previewCommand + " Change$Partnership_List 1.360 Change$Partnership_List$Change_Out 0.880 "
										+ "Change$Partnership_List$Change_In 1.360";
									break;
								case "Control_F11":
									previewCommand = previewCommand + " Change$Summary 1.340 Change$Summary$Change_Out 0.720 Change$Summary$Change_In 1.340";
									break;
								case "Shift_T":
									previewCommand = previewCommand + " Change$LineUp_Image 1.560 Change$LineUp_Image$Change_Out 0.500 Change$LineUp_Image$Change_In 1.560";
									break;
								case "p":
									previewCommand = previewCommand + " Change$Group_Standings 1.040 Change$Group_Standings$Change_Out 0.624 Change$Group_Standings$Change_In 1.040";
									break;
								case "Control_p":
									previewCommand = previewCommand + " Change$Standings 1.120 Change$Standings$Change_Out 0.624 Change$Standings$Change_In 1.120";
									break;
								case "z": case "x": case "c": case "v": case "Control_z": case "Control_x": //LeaderBoard Most - Runs,Wickets,Fours,Sixes 
									previewCommand = previewCommand + " Change$Leader_Board 2.800 Change$Leader_Board$Change_Out 0.600 Change$Leader_Board$Change_In 2.800";
									previewCommand = previewCommand + " LeaderBoardHighlight$Side2$Player"+whatToProcess.split(",")[2].split("_")[0] + " 2.700";
									break;
								}
							}
							if(!whichGraphicOnScreen.split(",")[0].equalsIgnoreCase(whatToProcess.split(",")[0])) {
								switch(whatToProcess.split(",")[0]) {
								case "F1": case "F2": case "F4":
									previewCommand = previewCommand + " Header_Shrink 0.000 Header_Shrink$In 0.000";
									switch(whatToProcess.split(",")[0]) {
									case "F4":
										if(caption.this_fullFramesGfx.whichSponsor != null && !caption.this_fullFramesGfx.whichSponsor.isEmpty()) {
											previewCommand = previewCommand + " Change_Sponsor 1.000 Change_Sponsor$Change_Our 0.500 Change_Sponsor$Change_In 1.000";
										}
										break;
									}
									break;
								case "Control_F11": case "p": case "Control_p":
									previewCommand = previewCommand + " Header_Shrink 0.500 Header_Shrink$In 0.500";
									break;
								}
								switch(whatToProcess.split(",")[0]) {
								case "F1": 
									previewCommand = previewCommand + " Change$Batting_Card 1.380 Change$Batting_Card$Change_Out 0.880 Change$Batting_Card$Change_In 1.380";
									break;
								case "F2":
									previewCommand = previewCommand + " Change$Bowling_Card 1.300 Change$Bowling_Card$Change_Out 0.840 Change$Bowling_Card$Change_In 1.300";
									break;
								case "F4":
									previewCommand = previewCommand + " Change$Partnership_List 1.360 Change$Partnership_List$Change_Out 0.880 Change$Partnership_List$Change_In 1.360";
									break;
								case "Control_F11":
									previewCommand = previewCommand + " Change$Summary 1.340 Change$Summary$Change_Out 0.720 Change$Summary$Change_In 1.340";
									break;
								case "p":
									previewCommand = previewCommand + " Change$Group_Standings 1.040 Change$Group_Standings$Change_Out 0.624 Change$Group_Standings$Change_In 1.040";
									break;
								case "Control_p":
									previewCommand = previewCommand + " Change$Standings 1.120 Change$Standings$Change_Out 0.624 Change$Standings$Change_In 1.120";
									break;
								}
							}
							previewCommand = previewCommand + " Change$Footer 1.700 Change$Footer$Change_In 1.700 Change$Footer$Chnage_Out 0.580";
//							System.out.println("Number of rows : " + caption.this_fullFramesGfx.numberOfRows);
//							System.out.println("L Number of rows : " + lastNumberOfRows);
//							if(caption.this_fullFramesGfx.numberOfRows != lastNumberOfRows) {
//								previewCommand = previewCommand + " ConcussExtend_Y 0.500 ConcussExtend_Y$In 0.500";
//							}
							break;
						}
					}
				}
//				System.out.println("previewCommand = " + previewCommand);
			    CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*/Default/FullFrames "
				    	+ "C:/Temp/Preview.png " + previewCommand + " \0", print_writer);
				break;
			case Constants.ISPL:
				if(whichside == 1) {
					if(whatToProcess.contains(",")) {
						switch(whatToProcess.split(",")[0]) {
						case "F1": case "F2": case "F4": case "Control_F11": case "Control_F7": case "Shift_F8":
						case "Shift_K":	case "Control_p": case "Shift_F11": case "z": case "x": case "c": case "v": case "Control_z": case "Control_x":
						case "Control_c": case "Control_v": case "Shift_V": case "Control_F10":
							previewCommand = "Anim_Infobar$Push 0.500 Anim_FullFrames$In_Out$Essentials$In 2.140 Anim_FullFrames$In_Out$Header$In 2.100";
							break;
						case "m": case "Control_m":
							previewCommand = "Anim_MatchId$In_Out$In 1.700";
							break;
						case "Shift_D": // target
							previewCommand = "Anim_Target$In_Out$In 1.500";
							break;	
						}
						switch(whatToProcess.split(",")[0]) {
						case "F1"://battingCard
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Batting_Card$In 2.200";
							break;
						case "F2"://bowlingCard
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Bowling_Card$In 2.120";
							break;
						case "F4":
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Partnership_List$In 2.200";
							break;
						case "Control_F11": case "Shift_F11":
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Summary$In 1.880";
							break;
						case "Control_F7":
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Teams$In 2.220";
							break;
						case "Shift_T": //Playing XI
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Team_Single$In 2.240";
							break;
						case "Shift_F8":
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$LineUp_Image$In 2.240";
							break;
						case "Shift_K":
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Partnership$In 2.200";
							break;
						case "Control_F10":
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Manhattan$In 2.220";
							break;
						case "Control_p":
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Standings$In 1.843";
							break;
						case "z": case "x": case "c": case "v":	case "Control_c": case "Control_v": case "Control_z": case "Control_x":
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$LeaderBoard$In 2.220";
							previewCommand = previewCommand + " LeaderBoardHighlight$Side1$Player" + whatToProcess.split(",")[2].split("_")[0] + " 1.574";
							break;
						case "Shift_V":
							previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$LeaderBoard$In 2.220";
							break;
						}
					}
				}else if(whichside == 2) {
					if(whatToProcess.contains(",")) {
						switch(whatToProcess.split(",")[0]) {
						case "F1": case "F2": case "F4": case "Control_F11": case "Shift_T": case "Shift_F8": case "Shift_K":
						case "Control_p": case "z": case "x": case "c": case "v": case "Control_z": case "Control_x":
							previewCommand = previewCommand + "Change$Header 1.600 Change$Header$Change_In 1.600 Change$Header$Change_Out 0.420";
							if(whichGraphicOnScreen.contains(",")) {
								switch(whichGraphicOnScreen.split(",")[0]) {
								case "F1":  
									previewCommand = previewCommand + " Change$Batting_Card 1.900 Change$Batting_Card$Change_Out 0.860 Change$Batting_Card$Change_In 1.900";
									break;
								case "F2":  
									previewCommand = previewCommand + " Change$Bowling_Card 1.820 Change$Bowling_Card$Change_Out 0.760 Change$Bowling_Card$Change_In 1.820";
									break;
								case "F4":  
									previewCommand = previewCommand + " Change$Partnership_List 1.900 Change$Partnership_List$Change_Out 0.860 "
											+ "Change$Partnership_List$Change_In 1.900";
									break;
								case "Control_F11":  
									previewCommand = previewCommand + " Change$Summary 1.580 Change$Summary$Change_Out 0.760 Change$Summary$Change_In 1.580";
									break;
								case "Shift_T":
									previewCommand = previewCommand + " Change$Team_Single 1.940 Change$Team_Single$Change_Out 0.820 Change$Team_Single$Change_In 1.940";
									break;
								case "Shift_F8":
									previewCommand = previewCommand + " Change$LineUp_Image 1.940 Change$LineUp_Image$Change_Out 0.820 Change$LineUp_Image$Change_In 1.940";
									break;
								case "Shift_K":
									previewCommand = previewCommand + " Change$Partnership 1.900 Change$Partnership$Change_Out 0.860 "
											+ "Change$Partnership$Change_In 1.900";
									break;
								case "Control_p":
									previewCommand = previewCommand + " Change$Standings 1.543 Change$Standings$Change_Out 0.760 "
											+ "Change$Standings$Change_In 1.543";
									break;
								case "z": case "x": case "c": case "v":	case "Control_z": case "Control_x":
									previewCommand = previewCommand + " Change$LeaderBoard 2.200 Change$LeaderBoard$Change_Out 0.760 "
											+ "Change$LeaderBoard$Change_In 2.200";
									previewCommand = previewCommand + " LeaderBoardHighlight$Side2$Player" + whatToProcess.split(",")[2].split("_")[0] + " 1.574";
									break;	
								}
							}
							if(!whichGraphicOnScreen.split(",")[0].equalsIgnoreCase(whatToProcess.split(",")[0])) {
								switch(whatToProcess.split(",")[0]) {
								case "F1": case "F2":case "F4": case "Control_F11": case "Shift_K": case "Control_p":
								case "Shift_T":	case "Shift_F8": case "z": case "x": case "c": case "v": case "Control_z": case "Control_x":
									previewCommand = previewCommand + " Header_Shrink 0.000 Header_Shrink$In 0.000";
									break;
								}
								switch(whatToProcess.split(",")[0]) {
								case "F1":  
									previewCommand = previewCommand + " Change$Batting_Card 1.900 Change$Batting_Card$Change_Out 0.860 Change$Batting_Card$Change_In 1.900";
									break;
								case "F2":  
									previewCommand = previewCommand + " Change$Bowling_Card 1.820 Change$Bowling_Card$Change_Out 0.760 Change$Bowling_Card$Change_In 1.820";
									break;
								case "F4":
									previewCommand = previewCommand + " Change$Partnership_List 1.900 Change$Partnership_List$Change_Out 0.860 "
											+ "Change$Partnership_List$Change_In 1.900";
									break;
								case "Control_F11":  
									previewCommand = previewCommand + " Change$Summary 1.580 Change$Summary$Change_Out 0.760 Change$Summary$Change_In 1.580";
									break;
								case "Shift_T":
									previewCommand = previewCommand + " Change$Team_Single 1.940 Change$Team_Single$Change_Out 0.820 Change$Team_Single$Change_In 1.940";
									break;
								case "Shift_F8":
									previewCommand = previewCommand + " Change$LineUp_Image 1.940 Change$LineUp_Image$Change_Out 0.820 Change$LineUp_Image$Change_In 1.940";
									break;
								case "Shift_K":
									previewCommand = previewCommand + " Change$Partnership 1.900 Change$Partnership$Change_Out 0.860 "
											+ "Change$Partnership$Change_In 1.900";
									break;
								case "Control_p":
									previewCommand = previewCommand + " Change$Standings 1.543 Change$Standings$Change_Out 0.760 "
											+ "Change$Standings$Change_In 1.543";
									break;
								}
							}
//							previewCommand = previewCommand + " Change$Footer 1.700 Change$Footer$Change_In 1.700 Change$Footer$Chnage_Out 0.580";
//							System.out.println("Number of rows : " + caption.this_fullFramesGfx.numberOfRows);
//							System.out.println("L Number of rows : " + lastNumberOfRows);
//							if(caption.this_fullFramesGfx.numberOfRows != lastNumberOfRows) {
//								previewCommand = previewCommand + " ConcussExtend_Y 0.500 ConcussExtend_Y$In 0.500";
//							}
							break;
						}
					}
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*/Default/FullFrames "
				    	+ "C:/Temp/Preview.png " + previewCommand + " \0", print_writer);
				break;
			}
		}
	}

	public void processL3Preview(String whatToProcess, List<PrintWriter> print_writer, int whichside, Configuration config,MatchAllData match)
	{
		if(config.getPreview().equalsIgnoreCase("WITH_PREVIEW")) {
			String previewCommands = "";
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.BENGAL_T20:
				if(whichside == 1) {
					switch(whatToProcess.split(",")[0]) {
					case "F5": 
						if(whatToProcess.split(",")[3].toUpperCase().equalsIgnoreCase("SPONSOR")) {
							previewCommands = "anim_BatsmanScore_LT 2.200 anim_BatsmanScore_LT$In 1.500";
						}else {
							previewCommands = "anim_Lower_Third 2.200 anim_Lower_Third$Essentials 1.500 anim_Lower_Third$Essentials$In 1.500 ";
						}
						break;
					case "F9":
						if(whatToProcess.split(",")[3].toUpperCase().equalsIgnoreCase("SPONSOR")) {
							previewCommands = "anim_BowlerFigure_LT 2.200 anim_BowlerFigure_LT$In 1.200";
						}else {
							previewCommands = "anim_Lower_Third 2.200 anim_Lower_Third$Essentials 1.500 anim_Lower_Third$Essentials$In 1.500 ";
						}
						break;
					case "Control_a":
						previewCommands = "anim_Projected_LT 2.200 anim_Projected_LT$Essentials 2.200 anim_Projected_LT$Essentials$In 1.500";
						break;
					case "F6": case "Control_F6": case "Shift_F6": case "F8": case "Alt_F8": case "F10": case "Shift_F3": case "d": case "e": case "u":
					case "Shift_F5": case "Alt_o": case "Shift_F9": case "Control_F3": case "Control_F5": case "Control_F9": case "Alt_F12": case "Control_s": case "Control_f": case "F7": case "F11":
						previewCommands = "anim_Lower_Third 2.200 anim_Lower_Third$Essentials 2.200 anim_Lower_Third$Essentials$In 1.500 ";
						break;
					}
				}else if(whichside == 2){
					switch (whatToProcess.split(",")[0]) {
					case "F6": case "Control_F6": case "Shift_F6": case "F5": case "F9": case "F8": case "Alt_F8": case "F10": case "Shift_F3": case "d": case "e": case "u":
					case "Shift_F5": case "Alt_o": case "Shift_F9": case "Control_F3": case "Control_F5": case "Control_F9":  case "Alt_F12": case "Control_s": case "Control_f":case "F7": case "F11":
						previewCommands = previewCommands + " Anim_LtChange$Badge 1.00 Anim_LtChange$Sublines 1.00 "
								+ "Anim_LtChange 1.2000 Anim_LtChange$Topline 1.000 Anim_LtChange$Dynamic$Change_In 1.180 Anim_LtChange$Dynamic$Change_Out 0.560";
						break;
					}
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*/Default/Overlays "
				    	+ "C:/Temp/Preview.png " + previewCommands + " \0", print_writer);
				break;
			case Constants.ICC_U19_2023:
				if(whichside == 1) {
					switch(whatToProcess.split(",")[0]) {
					case "F5": case "F6": case "F7": case "F9": case "F11": case "Control_F2":
					case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_o":
					case "Shift_F3": case "u": case "d": case "e": case "Shift_F5": case "Shift_F9": case "Alt_F12":
					case "Control_g": case "Control_h": case "Control_p": case "Control_F6": case "Shift_F6": case "Control_s":
					case "Alt_d": case "Control_f": case "l": case "n": case "a": case "Alt_F1": case "Alt_F2":case "Shift_E":
					case "Alt_F6": case "Shift_A":  case "Shift_R": case "Shift_U": case "Alt_w": case "Control_j": case "Alt_i": case "b":
					case "Alt_j": case "Control_i": case "Alt_Shift_L": case "Shift_B": case "Control_Shift_F":
						previewCommands = "Anim_Infobar$Push 0.500 Anim_LtChange$HeaderDynamic 1.200 anim_Lower_Third$Essentials 2.200 anim_Lower_Third$Essentials$In 1.400 "
							+ "anim_Lower_Third$Row 2.160 anim_Lower_Third$Row$In 0.620";
						break;
					 case "Alt_F8": case "F8": case "F10": case "j": case "Alt_a": case "Alt_s": // Name super L3rd
						previewCommands = "Anim_Infobar$Push 0.500 anim_NameSupers$In 1.400";
						break;
					 case "Alt_q":
						 previewCommands = "Anim_Infobar$Push 0.500 anim_POTT$In 1.400";
						break;
					case "q": case "Control_q":// Boundary L3rd
						previewCommands = "Anim_Infobar$Push 0.500 anim_Boundary_LT$Essentials 2.200 anim_Boundary_LT$Essentials$In 1.400 "
							+ "anim_Boundary_LT$Row 2.160 anim_Boundary_LT$Row$In 0.620";
						break;
					case "Shift_F7":
						previewCommands = "Anim_Infobar$Push 0.500 Anim_Image_LtChange$HeaderDynamic 1.200 Anim_Image_LT$Essentials 2.200 Anim_Image_LT$Essentials$In 1.400 "
								+ "Anim_Image_LT$Row 2.160 Anim_Image_LT$Row$In 0.620";
						break;
					}
				}else if(whichside == 2) {
					switch (whatToProcess.split(",")[0]) {
					case "F5": case "F6": case "F7": case "F9": case "F11": case "l": case "n": case "a": case "Control_F2":
					case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_o": case "Alt_F1": case "Alt_F2":
					case "Shift_F3": case "u": case "d": case "e": case "Shift_F5": case "Shift_F9": case "Alt_F12":case "Shift_E":
					case "Control_g": case "Control_h": case "Control_p": case "Control_F6": case "Shift_F6": case "Control_s": case "Control_f": 
					case "Alt_F6": case "Shift_A":  case "Shift_R": case "Shift_U": case "Alt_w": case "Control_j": case "Alt_i": case "b":
					case "Alt_j": case "Control_i": case "Alt_Shift_L":  case "Shift_B": case "Control_Shift_F":
						previewCommands = previewCommands + " Anim_LtChange$Flag 1.300 Anim_LtChange$Sublines 1.240 "
							+ "Anim_LtChange$Topline 0.900 Anim_LtChange$Lt_Position 0.940 Anim_LtChange$HeaderDynamic 1.180 "
							+ "Anim_LtChange$HeaderDynamic$Change_In 1.180 Anim_LtChange$HeaderDynamic$Change_Out 0.560";
						break;
					 case "Alt_F8": case "F8": case "F10": case "j": case "Alt_a": case "Alt_s":
						previewCommands = previewCommands + " Anim_NameSuperChange$Flag 1.300 Anim_NameSuperChange$Sublines 0.700 "
							+ "Anim_NameSuperChange$Topline 0.900 Anim_NameSuperChange$HeaderDynamic 1.220 "
							+ "Anim_NameSuperChange$HeaderDynamic$Change_In 1.220 Anim_NameSuperChange$HeaderDynamic$Change_Out 0.600";
						break;
					case "q": case "Control_q":
						previewCommands = previewCommands + " Anim_Boundary_LtChange$Flag 1.300 Anim_Boundary_LtChange$Sublines 1.200 "
							+ "Anim_Boundary_LtChange$Topline 0.900 Anim_Boundary_LtChange$Lt_Position 0.940 Anim_Boundary_LtChange$HeaderDynamic 1.223 "
							+ "Anim_Boundary_LtChange$HeaderDynamic$Change_In 1.223 Anim_Boundary_LtChange$HeaderDynamic$Change_Out 0.600";
						break;
					case "Shift_F7":
						previewCommands = previewCommands + " Anim_Image_LtChange$Flag 1.300 Anim_Image_LtChange$Sublines 1.240 "
								+ "Anim_Image_LtChange$Topline 0.900 Anim_Image_LtChange$Lt_Position 0.940 Anim_Image_LtChange$HeaderDynamic 1.180 "
								+ "Anim_Image_LtChange$HeaderDynamic$Change_In 1.180 Anim_Image_LtChange$HeaderDynamic$Change_Out 0.560";
						break;	
					}
				}
			    CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*/Default/Overlays "
			    	+ "C:/Temp/Preview.png " + previewCommands + " \0", print_writer);
				break;
				
			case Constants.ISPL:
				if(whichside == 1) {
					switch(whatToProcess.split(",")[0]) {
					case "F5": case "F6": case "F7": case "F9": case "F11": case "Control_F2":
					case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_o":
					case "Shift_F3": case "u": case "d": case "e": case "Shift_F5": case "Shift_F9": case "Alt_F12":
					case "Control_g": case "Control_h": case "Control_p": case "Control_F6": case "Shift_F6": case "Control_s":
					case "Alt_d": case "Control_f": case "l": case "a": case "Alt_F1": case "Alt_F2":case "Shift_E": case "Alt_Shift_L":
					case "Alt_F6": case "Shift_A":  case "Shift_R": case "Shift_U": case "Alt_F8": case "F8": case "F10": case "j": 
					case "Alt_a": case "Alt_s":  case "Alt_w": case "Control_j": case "Alt_i": case "Alt_j": case "b": case "Control_i":
						previewCommands = "Anim_Infobar$Push 0.560 "
								+ "Anim_LtChange$Top_Header 2.680  Anim_LtChange$Top_Header$In 1.920 "
								+ "anim_Lower_Third$Essentials 3.200 anim_Lower_Third$Essentials$In 1.900 "
								+ "anim_Lower_Third$Row 3.000 anim_Lower_Third$Row$In 1.900 "
								+ "Anim_LtChange$Lt_X_Position 1.140 Anim_LtChange$Lt_X_Position$MoveForShrink 1.820 ";
						break;
					 case "Alt_q":
						 previewCommands = "Anim_Infobar$Push 0.500 anim_POTT$In 1.400";
						break;
					case "q": case "Control_q":// Boundary L3rd
						previewCommands = "Anim_Infobar$Push 0.500 anim_Boundary_LT$Essentials 2.200 anim_Boundary_LT$Essentials$In 1.400 "
							+ "anim_Boundary_LT$Row 2.160 anim_Boundary_LT$Row$In 0.620";
						break;
					case "Shift_F7": case "Control_F4":
						previewCommands = "Anim_Infobar$Push 0.560 "
								+ "Anim_Image_LT$Top_Header 2.680  Anim_Image_LT$Top_Header$In 1.920 "
								+ "Anim_Image_LT$Essentials 3.200 Anim_Image_LT$Essentials$In 1.900 "
								+ "Anim_Image_LT$Row 3.000 Anim_Image_LT$Row$In 1.900 "
								+ "Anim_Image_LtChange$Lt_X_Position 1.140 Anim_Image_LtChange$Lt_X_Position$MoveForShrink 1.820 ";
						break;	
					}
				}else if(whichside == 2) {
					switch (whatToProcess.split(",")[0]) {
					case "F5": case "F6": case "F7": case "F9": case "F11": case "l": case "a": case "Control_F2": case "F8": case "F10":
					case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_o": case "Alt_F1": case "Alt_F2":
					case "Shift_F3": case "u": case "d": case "e": case "Shift_F5": case "Shift_F9": case "Alt_F12":case "Shift_E":
					case "Control_g": case "Control_h": case "Control_p": case "Control_F6": case "Shift_F6": case "Control_s": case "Control_f": 
					case "Alt_F6": case "Shift_A":  case "Shift_R": case "Shift_U": case "Alt_F8":  case "j": case "Alt_a": case "Alt_s": case "Alt_w":
					case "Control_j": case "Alt_i": case "Alt_j":  case "b": case "Control_i": case "Alt_Shift_L":
					//case "n":	
						previewCommands = previewCommands + " Anim_LtChange$Sublines 0.780 "
								+ "Anim_LtChange$Topline 0.900 Anim_LtChange$Lt_Y_Scale 0.900 "
								+ "Anim_LtChange$Lt_X_Position 0.900";
						break;
					case "q": case "Control_q":
						previewCommands = previewCommands + " Anim_Boundary_LtChange$Flag 1.300 Anim_Boundary_LtChange$Sublines 1.200 "
							+ "Anim_Boundary_LtChange$Topline 0.900 Anim_Boundary_LtChange$Lt_Position 0.940 Anim_Boundary_LtChange$HeaderDynamic 1.223 "
							+ "Anim_Boundary_LtChange$HeaderDynamic$Change_In 1.223 Anim_Boundary_LtChange$HeaderDynamic$Change_Out 0.600";
						break;
					case "Shift_F7": case "Control_F4":
						previewCommands = previewCommands + " Anim_Image_LtChange$Flag 1.300 Anim_Image_LtChange$Sublines 1.240 "
								+ "Anim_Image_LtChange$Topline 0.900 Anim_Image_LtChange$Lt_Position 0.940 Anim_Image_LtChange$HeaderDynamic 1.180 "
								+ "Anim_Image_LtChange$HeaderDynamic$Change_In 1.180 Anim_Image_LtChange$HeaderDynamic$Change_Out 0.560";
						break;	
					}
				}
				if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*/Default/Overlays_SuperOver "
					    	+ "C:/Temp/Preview.png " + previewCommands + " \0", print_writer);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*/Default/Overlays "
					    	+ "C:/Temp/Preview.png " + previewCommands + " \0", print_writer);
				}
			    
				break;	
			}
		}
	}

	public void processBugsPreview(String whatToProcess, List<PrintWriter> print_writer, int whichside, 
		Configuration config,String whichGraphicOnScreen) 
	{
		if(config.getPreview().equalsIgnoreCase("WITH_PREVIEW")) {
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023: case Constants.ISPL:
				if(whatToProcess.contains(",")) {
					switch(whatToProcess.split(",")[0]) {
					case "Shift_O": case "Control_k": case "k": case "g": case "y": case "Control_y": case "h": case "Shift_F4": case "Shift_F":
					case ".": case "/":
						if(config.getBroadcaster().toUpperCase().equalsIgnoreCase(Constants.ICC_U19_2023)) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" + "/Default/Overlays" + " C:/Temp/Preview.png Anim_Bugs 2.200 "
									+ "Anim_Bugs$Essentials 2.200 Anim_Bugs$Essentials$In 2.200 Anim_Bugs$Essentials$In$Anim_Bugs 2.200 "
									+ "Anim_Bugs$Essentials$In$Anim_Bugs$Essentials 2.200 Anim_Bugs$Essentials$In$Anim_Bugs$Essentials$Out 2.200 \0", print_writer);
							
						}else if(config.getBroadcaster().toUpperCase().equalsIgnoreCase(Constants.ISPL)) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" + "/Default/Overlays" + " C:/Temp/Preview.png Anim_Bugs 2.940 "
									+ "Anim_Bugs$Essentials 2.940 Anim_Bugs$Essentials$In 0.960 Anim_Bugs$Essentials$In$Anim_Bugs 2.940 "
									+ "Anim_Bugs$Essentials$In$Anim_Bugs$Essentials 2.940 Anim_Bugs$Essentials$In$Anim_Bugs$Essentials$Out 2.940 \0", print_writer);	
						}
						break;	
					 case "Alt_p": case "o": case "t":
						 if(config.getBroadcaster().toUpperCase().equalsIgnoreCase(Constants.ICC_U19_2023)) {
							 CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
										+ "/Default/Overlays" + " C:/Temp/Preview.png Anim_Center_Bug$In 0.800 \0", print_writer);
							 
						 }else if(config.getBroadcaster().toUpperCase().equalsIgnoreCase(Constants.ISPL)) {
							
							 CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
										+ "/Default/Overlays" + " C:/Temp/Preview.png Anim_Center_Bug$In 0.700 \0", print_writer);
						 }else if(config.getBroadcaster().toUpperCase().equalsIgnoreCase(Constants.BENGAL_T20)) {
							
							 CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
										+ "/Default/Overlays" + " C:/Temp/Preview.png anim_Toss 1.000 \0", print_writer);
						 }
						break;
					}
					if(whichside == 2) {
						switch(whatToProcess.split(",")[0]) {
						case "Shift_O": case "Control_k": case "k": case "g": case "y": case "Control_y": case "h": case "Shift_F4": case "Shift_F":
						case ".": case "/":	
							if(config.getBroadcaster().toUpperCase().equalsIgnoreCase(Constants.ICC_U19_2023)) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
										+ "/Default/Overlays C:/Temp/Preview.png Anim_BugsChange 1.360 \0", print_writer);
								
							}else if(config.getBroadcaster().toUpperCase().equalsIgnoreCase(Constants.ISPL)) {
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
										+ "/Default/Overlays C:/Temp/Preview.png Anim_BugsChange 1.260 \0", print_writer);
							}
							break;
						}
					}
				}
				break;
			}
		}
	}
	public void processMiniPreview(String whatToProcess, List<PrintWriter> print_writer, int whichside, Configuration config, String whichGraphicOnScreen) {
		if(config.getPreview().equalsIgnoreCase("WITH_PREVIEW")) {
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023: case Constants.ISPL:
				if(whatToProcess.contains(",")) {
					switch(whatToProcess.split(",")[0]) {
					case "Shift_F1": case "Shift_F2":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
								+ "/Default/Overlays" + " C:/Temp/Preview.png Anim_Infobar$Push 0.500 Anim_Mini$In_Out 1.260 Anim_Mini$In_Out$In 1.240 \0", print_writer);
						break;
					case "Alt_F7":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
								+ "/Default/Overlays" + " C:/Temp/Preview.png Anim_Infobar$Push 0.500 Anim_MiniPoints$In_Out 0.940 Anim_MiniPoints$In_Out$In 0.940 \0", print_writer);
						break;
					}
					if(whichside == 2) {
						switch(whatToProcess.split(",")[0]) {
						case "Shift_F1": case "Shift_F2":
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
									+ "/Default/Overlays" + " C:/Temp/Preview.png Anim_MiniChange 1.860 Anim_MiniChange$Change_In 1.860 \0", print_writer);
							break;	
						}
					}
				}
				break;
			}
		}
	}
	public String getWhichGraphicOnScreen() {
		return whichGraphicOnScreen;
	}

	public void setWhichGraphicOnScreen(String whichGraphicOnScreen) {
		this.whichGraphicOnScreen = whichGraphicOnScreen;
	}

	public String getSpecialBugOnScreen() {
		return specialBugOnScreen;
	}

	public void setSpecialBugOnScreen(String specialBugOnScreen) {
		this.specialBugOnScreen = specialBugOnScreen;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Infobar getInfobar() {
		return infobar;
	}

	public void setInfobar(Infobar infobar) {
		this.infobar = infobar;
	}

	@Override
	public String toString() {
		return "Animation [whichGraphicOnScreen=" + whichGraphicOnScreen + ", specialBugOnScreen=" + specialBugOnScreen
				+ ", status=" + status + ", caption=" + caption + ", lastNumberOfRows="
				+ lastNumberOfRows + "]";
	}

}
