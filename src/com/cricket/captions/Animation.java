package com.cricket.captions;

import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.cricket.containers.Infobar;
import com.cricket.model.Configuration;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;

public class Animation 
{
	public String whichGraphicOnScreen = "", specialBugOnScreen = "", status = "";
	public Infobar infobar;
	public Caption caption;
	public int lastNumberOfRows = 0;
	
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
		}
	}
	
	public String getTypeOfGraphicsOnScreen(Configuration config,String whatToProcess)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			switch (whatToProcess.split(",")[0]) {
			case "F1": case "F2": case "F4": case "Shift_F10": case "Shift_F11": case "m": case "Control_m": 
			case "Control_F8": case "Control_d": case "Control_e": case "Control_F7": case "Control_F10":
			case "Shift_K": case "Alt_F9": case "Shift_D": case "p": case "Control_b": case "Alt_m": case "Alt_n":
			case "z": case "x": case "c": case "v":
				return Constants.FULL_FRAMER;
			case "F5": case "F6": case "F7": case "F8": case "F9": case "F10": case "F11":
			case "Control_F1": case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_k":
			case "Shift_F3": case "s": case "d": case "e": case "q": case "Shift_F5": case "Shift_F9": case "Alt_F12":
			case "Control_g": case "Control_h": case "Control_p": case "j": case "Control_F6": case "Shift_F6": 
			case "Control_s": case "Alt_d": case "Control_f": case "Control_q": case "l": case "n": case "a":
				
				switch (whatToProcess.split(",")[0]) {
				case "F8": case "F10": case "j": // Name super L3rd
					return Constants.NAME_SUPERS + Constants.LOWER_THIRD;
				case "q": case "Control_q":// Boundary L3rd
					return Constants.BOUNDARIES + Constants.LOWER_THIRD;
				default:
					return Constants.LOWER_THIRD;
				}
			case "Alt_1": case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_5": case "Alt_6": case "Alt_7": case "Alt_8": case "Alt_9":
				return Constants.INFO_BAR;
			case "Shift_O": case "Control_k": case "k": case "g": case "f": case "Alt_p":
				return Constants.BUGS;
			case "Shift_F1": case "Shift_F2":
				return Constants.MINIS;
			}
			break;
		}
		return "";
	}
	
	public String AnimateIn(String whatToProcess, List<PrintWriter> print_writers, Configuration config) throws InterruptedException 
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:

			//Full framers
			switch (whatToProcess.split(",")[0]) {
			case "Shift_D":
				AnimateIn("ArrowDown,", print_writers, config); // Push infobar
				TimeUnit.MILLISECONDS.sleep(500);
				processAnimation(Constants.BACK, print_writers, "Target", "START");
				processAnimation(Constants.BACK, print_writers, "TargetLoop", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Control_b":
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Push infobar
				TimeUnit.MILLISECONDS.sleep(500);
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
			
			case "F1": case "F2": case "F4": case "Control_F1": case "Shift_F10": case "Shift_F11": case "m": case "Control_m": 
			case "Control_F8": case "Control_d": case "Control_e": case "Control_F7": case "Control_F10":
			case "Shift_K": case "Alt_F9": case "p": case "z": case "x": case "c": case "v":
				
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
				case "Shift_F11":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Summary", "START");
					break;
				case "m":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Ident", "START");
					break;
				case "Control_m":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Ident", "START");
					break;
				case "Control_F8":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$LineUp_Image", "START");
					break;
				case "Control_d":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Profile", "START");
					break;
				case "Control_e":
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
				case "Alt_F9":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Team_Single", "START");
					break;
				case "p":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Group_Standings", "START");
					break;
				case "z": case "x": case "c": case "v":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Leader_Board", "START");
					break;
				}
				
				switch (whatToProcess.split(",")[0]) {
				case "m": case "Control_m": case "Shift_F11": case "Control_F7": case "p": case "z": case "x": case "c": case "v":
					processAnimation(Constants.BACK, print_writers, "Header_Shrink", "START");
					break;
				case "Shift_K": case "F4":
					TimeUnit.MILLISECONDS.sleep(1500);
					processAnimation(Constants.BACK, print_writers, "Sponsor", "START");
					break;
				case "Control_d": case "Control_e":
					if(Integer.valueOf(whatToProcess.split(",")[4]) > 0) {
						processAnimation(Constants.BACK, print_writers, "Profile_Highlight$Side1$"+whatToProcess.split(",")[4], "START");
					}
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
			case "F5": case "F6": case "F7": case "F9": case "F11":
			case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_k":
			case "Shift_F3": case "s": case "d": case "e": case "Shift_F5": case "Shift_F9": case "Alt_F12":
			case "Control_g": case "Control_h": case "Control_p": case "Control_F6": case "Shift_F6": case "Control_s":
			case "Alt_d": case "Control_f": case "l": case "n": case "a":
				if(this.infobar.isInfobar_on_screen() == true) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 40.0 \0",print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_LowerThirds$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 3.0 \0",print_writers);
				}
				
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "Essentials", "START");
				processAnimation(Constants.FRONT, print_writers, "Row", "START");
				processAnimation(Constants.FRONT, print_writers, "HeaderDynamic", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "F8": case "F10": case "j": // Name super L3rd
				
				setPositionOfLowerThirds(config, print_writers);
				AnimateIn(Constants.MIDDLE + Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "anim_NameSupers", "START");
				processAnimation(Constants.FRONT, print_writers, "HeaderDynamic", "START");
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
				processAnimation(Constants.FRONT, print_writers, "HeaderDynamic", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
				
			case "Alt_p":
				
				processAnimation(Constants.FRONT, print_writers, "Anim_Center_Bug", "START");
				this.specialBugOnScreen = CricketUtil.TOSS;
				break;
				
			case "Shift_O": case "Control_k": case "k": case "g": case "f":
				
				processAnimation(Constants.FRONT, print_writers, "Anim_Bugs$Essentials", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Shift_F1": case "Shift_F2":
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "Anim_Mini$In_Out", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
				
			case "F12": //Infobar
				processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$In_Out", "START");
				this.infobar.setInfobar_on_screen(true);
				this.infobar.setInfobar_pushed(false);
				this.infobar.setInfobar_status(Constants.TWO_LINER_INFOBAR);
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
		}
		return CricketUtil.YES;
	}	
	public String AnimateOut(String whatToProcess, List<PrintWriter> print_writers, Configuration config) throws InterruptedException
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			switch (whatToProcess.split(",")[0]) {
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
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "Alt_m": case "Alt_n":
				processAnimation(Constants.BACK, print_writers, "Milestone", "CONTINUE");
				processAnimation(Constants.BACK, print_writers, "MilestoneLoop", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn("ArrowUp,", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			
			case "F1": case "F2": case "F4": case "Control_F1": case "Shift_F10": case "Shift_F11": case "m": case "Control_m": case "p":
			case "Control_F8": case "Control_d": case "Control_e": case "Control_F7": case "Control_F10": case "Shift_K": case "Alt_F9":
			case "z": case "x": case "c": case "v":
				
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
				case "Shift_F11":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Summary", "CONTINUE");
					break;
				case "m":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Ident", "CONTINUE");
					break;
				case "Control_m":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Ident", "CONTINUE");
					break;
				case "Control_F8":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$LineUp_Image", "CONTINUE");
					break;
				case "Control_d":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Profile", "CONTINUE");
					break;
				case "Control_e":
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
				case "Alt_F9":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Team_Single", "CONTINUE");
					break;
				case "p":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Group_Standings", "CONTINUE");
					break;
				case "z": case "x": case "c": case "v":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Leader_Board", "CONTINUE");
					break;
				}
				
				switch (whatToProcess.split(",")[0]) {
				case "m": case "Control_m": case "Shift_F11": case "Control_F7": case "p": case "z": case "x": case "c": case "v":
					processAnimation(Constants.BACK, print_writers, "Header_Shrink", "CONTINUE");
					break;
				case "Shift_K": case "F4":
					processAnimation(Constants.BACK, print_writers, "Sponsor", "CONTINUE");
					break;
				case "Control_d": case "Control_e":
					if(Integer.valueOf(whatToProcess.split(",")[4]) > 0) {
						processAnimation(Constants.BACK, print_writers, "Profile_Highlight$Side1$"+whatToProcess.split(",")[4], "CONTINUE");
					}
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
			case "F5": case "F6": case "F7": case "F9": case "F11":
			case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_k":
			case "Shift_F3": case "s": case "d": case "e": case "Shift_F5": case "Shift_F9": case "Alt_F12":
			case "Control_g": case "Control_h": case "Control_p": case "Control_F6": case "Shift_F6": case "Control_s":
			case "Alt_d": case "Control_f": case "l": case "n": case "a":
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "F8": case "F10": case "j":
				processAnimation(Constants.FRONT, print_writers, "anim_NameSupers", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.MIDDLE + Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
				
			case "Alt_p":
				if(this.specialBugOnScreen.equalsIgnoreCase(CricketUtil.TOSS)) {
					processAnimation(Constants.FRONT, print_writers, "Anim_Center_Bug", "CONTINUE");
					this.specialBugOnScreen = "";
				}
				break;
				
			case "q": case "Control_q":
				processAnimation(Constants.FRONT, print_writers, "anim_Boundary_LT", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "Shift_O": case "Control_k": case "k": case "g": case "f":
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
				if(infobar.isInfobar_on_screen() == true) {
					processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$In_Out", "CONTINUE");
					infobar.setInfobar_on_screen(false);
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
			switch(whatToProcess.split(",")[0]) {
			case "F1": case "F2": case "F4": case "Shift_F11": case "Control_F8": case "p":
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
						if(caption.this_fullFramesGfx.whichSponsor != null || !caption.this_fullFramesGfx.whichSponsor.isEmpty()) {
							processAnimation(Constants.BACK, print_writers, "Sponsor", "CONTINUE REVERSE");
						}
						break;
					case "Shift_F11":
						processAnimation(Constants.BACK, print_writers, "Change$Summary", "START");
						switch(whatToProcess.split(",")[0]) {
						case "Shift_F11":
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
					case "Control_F8":
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
						if(caption.this_fullFramesGfx.whichSponsor != null || !caption.this_fullFramesGfx.whichSponsor.isEmpty()) {
							processAnimation(Constants.BACK, print_writers, "Sponsor", "START");
						}
						break;
					case "Shift_F11":
						processAnimation(Constants.BACK, print_writers, "Header_Shrink", "START");
						processAnimation(Constants.BACK, print_writers, "Change$Summary", "START");
						break;
					case "p":
						processAnimation(Constants.BACK, print_writers, "Header_Shrink", "START");
						processAnimation(Constants.BACK, print_writers, "Change$Group_Standings", "START");
						break;
					}
				}
				if(caption.this_fullFramesGfx.numberOfRows != lastNumberOfRows) {
					processAnimation(Constants.BACK, print_writers, "ConcussExtend_Y", "START");
					lastNumberOfRows = caption.this_fullFramesGfx.numberOfRows;
				}
				break;
			case "F5": case "F6": case "F7": case "F9": case "F11": case "l": case "n": case "a":
			case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_k":
			case "Shift_F3": case "s": case "d": case "e": case "Shift_F5": case "Shift_F9": case "Alt_F12":
			case "Control_g": case "Control_h": case "Control_p": case "Control_F6": case "Shift_F6": case "Control_s": case "Control_f":
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "START");
				break;
			case "F8": case "F10": case "j":
				processAnimation(Constants.FRONT, print_writers, "Anim_NameSuperChange", "START");
				break;
			case "q": case "Control_q":
				processAnimation(Constants.FRONT, print_writers, "Anim_Boundary_LTChange", "START");
				break;
			case "Alt_1": case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_5": case "Alt_6": case "Alt_7": case "Alt_8": case "Alt_9":
				switch(whatToProcess.split(",")[0]) {
				case "Alt_1":
					processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Bottom_Left", "START");
					break;
				case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_5": case "Alt_6": case "Alt_9":
					processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_RightInfo", "START");
					break;
				case "Alt_7":
					processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_RightInfo_BottomRightPart", "START");
					break;
				case "Alt_8":
					if(whatToProcess.split(",")[2].equalsIgnoreCase(CricketUtil.BOWLER)) {
						processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bowl_All$In_Out", "CONTINUE");
						infobar.setRight_section("");
					}else {
						if(infobar.getRight_section()!= null && !infobar.getRight_section().isEmpty()) {
							processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bowl_All$Change", "START");
						}else {
							infobar.setRight_section(whatToProcess.split(",")[2]);
							processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bowl_All$In_Out", "START");
						}
					}
					break;
				}
				break;
			}
			break;
		}
		return CricketUtil.YES;
	}
	public String CutBack(String whatToProcess,List<PrintWriter> print_writers, Configuration config) throws InterruptedException
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:

			if(!whatToProcess.contains(",")) {
				return CricketUtil.NO;
			}	
			
			switch(whatToProcess.split(",")[0]) {
			case "Alt_1":
				processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Bottom_Left", "SHOW 0.0");
				break;
			case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_5": case "Alt_6": case "Alt_9":
				//TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_RightInfo", "SHOW 0.0");
				break;
			case "Alt_7":
				processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_RightInfo_BottomRightPart", "SHOW 0.0");
				break;
			case "Alt_8":
				if(!whatToProcess.split(",")[2].equalsIgnoreCase(CricketUtil.BOWLER)) {
					if(infobar.getRight_section()!= null && !infobar.getRight_section().isEmpty()) {
						processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Bowl_All$Change", "SHOW 0.0");
					}
				}
				break;
			case "F5": case "F6": case "F7": case "F9": case "F11": case "Control_g": case "Control_h": case "Control_p":
			case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_k":
			case "Control_F6": case "Shift_F6": case "Control_s": case "Control_f": case "l": case "n": case "a":
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "SHOW 0.0");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "F8": case "F10": case "j":
				processAnimation(Constants.FRONT, print_writers, "Anim_NameSuperChange", "SHOW 0.0");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "F1": case "F2": case "F4": case "Shift_F11": case "Control_F8":
				processAnimation(Constants.BACK, print_writers, "Change$Header", "SHOW 0.0");
				processAnimation(Constants.BACK, print_writers, "Change$Footer", "SHOW 0.0");
				switch(whichGraphicOnScreen.split(",")[0]) {
				case "F1":  
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Batting_Card", "SHOW 0.0");
					processAnimation(Constants.BACK, print_writers, "Change$Batting_Card", "SHOW 0.0");
					break;
				case "F2":  
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Bowling_Card", "SHOW 0.0");
					processAnimation(Constants.BACK, print_writers, "Change$Bowling_Card", "SHOW 0.0");
					break;
				case "F4":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Partnership_List", "SHOW 0.0");
					processAnimation(Constants.BACK, print_writers, "Change$Partnership_List", "SHOW 0.0");
					break;
				case "Shift_F11":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Summary", "SHOW 0.0");
					processAnimation(Constants.BACK, print_writers, "Change$Summary", "SHOW 0.0");
					break;
				case "Control_F8":
					processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$LineUp_Image", "SHOW 3.000");
					processAnimation(Constants.BACK, print_writers, "Change$LineUp_Image", "SHOW 0.0");
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
					case "Shift_F11":
						processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out$Summary", "SHOW 3.000");
						processAnimation(Constants.BACK, print_writers, "Change$Summary", "SHOW 0.0");
						break;
					}
				}
				setVariousAnimationsKeys("CUT-BACK", print_writers, config);
				processAnimation(Constants.BACK, print_writers, "ConcussExtend_Y", "SHOW 0.0");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			}
			break;
		}
		return CricketUtil.YES;
	}	
	public String ResetAnimation(String whatToProcess, List<PrintWriter> print_writers, Configuration config)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:

			processAnimation(Constants.BACK, print_writers, "Anim_FullFrames", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Change", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Sponsor_5sec", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "ConcussExtend_Y", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Header_Shrink", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Header_Extra_Loop", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Profile_Highlight", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Base_Gradient", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Target", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "TargetLoop", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "In_At", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "In_At_Loop", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "Milestone", "SHOW 0.0");
			processAnimation(Constants.BACK, print_writers, "MilestoneLoop", "SHOW 0.0");

			processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "anim_Boundary_LT", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_Boundary_LTChange", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_Bugs", "SHOW 0.0");
			
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
		}
		return CricketUtil.YES;
	}
	public void processAnimation(String whichLayer, List<PrintWriter> print_writers,
		String animationDirectorName, String animationCommand)
	{
		if(!whichLayer.isEmpty()) {
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*" + whichLayer + "_LAYER*STAGE*DIRECTOR*"
				+ animationDirectorName + " " + animationCommand +" \0", print_writers);
		} else {
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*STAGE*DIRECTOR*"
				+ animationDirectorName + " " + animationCommand +" \0", print_writers);
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
		case Constants.ICC_U19_2023:
			
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
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor"
					+ "*ANIMATION*KEY*$In_2*VALUE SET 0.0 " + Sponsor + " 0.0 \0", print_writers);				
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
					+ "*ANIMATION*KEY*$In_1*VALUE SET 0.0 " + Sponsor + " 0.0 \0", print_writers);				
				break;
			}
			break;
		}
	}
	
	public void processFullFramesPreview(String whatToProcess, List<PrintWriter> print_writer, int whichside, 
		Configuration config,String whichGraphicOnScreen) 
	{
		String previewCommand = "";
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			if(whatToProcess.contains(",")) {
				switch(whatToProcess.split(",")[0]) {
				case "F1": case "F2": case "F4": case "Control_F1": case "Shift_F10": case "Shift_F11": case "m": case "Control_m": case "p":
				case "Control_F8": case "Control_d": case "Control_e": case "Control_F7": case "Control_F10": case "Alt_F9": case "Shift_K":
				case "z": case "x": case "c": case "v":
					previewCommand = "Anim_FullFrames$In_Out$Essentials$In 2.140 Anim_FullFrames$In_Out$Header$In 1.800 "
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
					break;
				case "Control_F7":// Double Teams
					previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Teams$In 1.860";
					break;
				case "Control_F8": //Playing XI
					previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$LineUp_Image$In 2.040";
					break;
				case "Control_F1":// Photo ScoreCard
					previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Batting_Card_Image$In 2.040";
					break;
				case "Control_F10"://Manhattan
					previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Manhattan$In 2.900";
					break;
				case "Shift_F10"://WORMS
					previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Worm$In 3.000";
					break;
				case "Shift_F11":  //MATCH SUMMARY
					previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Summary$In 1.820";
					break;
				case "p": // PointsTable
					previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Group_Standings$In 1.540";
					break;
				case "z": case "x": case "c": case "v": //LeaderBoard Most - Runs,Wickets,Fours,Sixes 
					previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Leader_Board$In 2.300";
					break;
				case "m"://Match id	
					previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Ident$In 1.920 Base_Gradient 0.500";
					break;
				case "Control_m": //MATCH PROMO
					previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Ident$In 1.920";
					break;
				case "Control_d": case "Control_e"://PlayerProfile
					previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Profile$In 2.300";
					if(Integer.valueOf(whatToProcess.split(",")[4]) > 0) {
						previewCommand = previewCommand + " Profile_Highlight$Side1$" + whatToProcess.split(",")[4] + " 1.000";
					}	
					break;
				case "Shift_K"://FFCurrPartnership
					previewCommand = previewCommand + " Anim_FullFrames$In_Out$Main$Partnership$In 3.000 Base_Gradient 0.500 Sponsor 0.900";
					break;
				case "Alt_F9": // Single Teams
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
				
				}
			    if(whichside == 2) {
					switch(whatToProcess.split(",")[0]) {
					case "F1": case "F2": case "F4": case "Shift_F11": case "Control_F8": case "p":
						previewCommand = previewCommand + " Change$Header 1.320 Change$Header$Change_Out 0.420 Change$Header$Change_In 1.320";
						if(whichGraphicOnScreen.contains(",")) {
							switch(whichGraphicOnScreen.split(",")[0]) {
							case "F1":  
								previewCommand = previewCommand + " Change$Batting_Card 1.380 Change$Batting_Card$Change_Out 0.880 Change$Batting_Card$Change_In 1.380";
								break;
							case "F2":  
								previewCommand = previewCommand + " Change$Bowling_Card 1.300 Change$Bowling_Card$Change_Out 0.840 Change$Bowling_Card$Change_In 1.300";
								break;
							case "F4":
								previewCommand = previewCommand + " Change$Partnership_List 1.360 Change$Partnership_List$Change_Out 0.880 Change$Partnership_List$Change_In 1.360 Sponsor 0.000";
								break;
							case "Shift_F11":
								previewCommand = previewCommand + " Change$Summary 1.340 Change$Summary$Change_Out 0.720 Change$Summary$Change_In 1.340";
								break;
							case "Control_F8":
								previewCommand = previewCommand + " Change$LineUp_Image 1.560 Change$LineUp_Image$Change_Out 0.500 Change$LineUp_Image$Change_In 1.560";
								break;
							case "p":
								previewCommand = previewCommand + " Change$Group_Standings 1.040 Change$Group_Standings$Change_Out 0.624 Change$Group_Standings$Change_In 1.040";
								break;
							}
						}
						if(!whichGraphicOnScreen.split(",")[0].equalsIgnoreCase(whatToProcess.split(",")[0])) {
							switch(whatToProcess.split(",")[0]) {
							case "F1": case "F2": case "F4":
								previewCommand = previewCommand + " Header_Shrink 0.000 Header_Shrink$In 0.000";
								switch(whatToProcess.split(",")[0]) {
								case "F4":
									previewCommand = previewCommand + " Sponsor 0.900";
									break;
								}
								break;
							case "Shift_F11": case "p":
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
								previewCommand = previewCommand + " Change$Partnership_List 1.360 Change$Partnership_List$Change_Out 0.880 Change$Partnership_List$Change_In 1.360 Sponsor 0.000";
								break;
							case "Shift_F11":
								previewCommand = previewCommand + " Change$Summary 1.340 Change$Summary$Change_Out 0.720 Change$Summary$Change_In 1.340";
								break;
							case "p":
								previewCommand = previewCommand + " Change$Group_Standings 1.040 Change$Group_Standings$Change_Out 0.624 Change$Group_Standings$Change_In 1.040";
								break;
							}
						}
						previewCommand = previewCommand + " Change$Footer 1.700 Change$Footer$Chnage_Out 0.580 Change$Footer$Change_In 1.700";
						if(caption.this_fullFramesGfx.numberOfRows != lastNumberOfRows) {
							previewCommand = previewCommand + " ConcussExtend_Y 1.000 ConcussExtend_Y$In 0.500";
						}
						break;
				    }
			    }
			    //System.out.println("previewCommand = " + previewCommand);
			    CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*/Default/FullFrames "
			    	+ "C:/Temp/Preview.png " + previewCommand + " \0", print_writer);
			}
			break;
		}
	}

	public void processL3Preview(String whatToProcess, List<PrintWriter> print_writer, int whichside, 
		Configuration config,String whichGraphicOnScreen) 
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			if(whatToProcess.contains(",")) {
				switch(whatToProcess.split(",")[0]) {
				case "F5": case "F6": case "F7": case "F9": case "F11":
				case "Control_F5": case "Control_F9": case "Control_a":  case "Control_c":
				case "Shift_F3": case "s": case "d": case "e":case "Shift_F5": case "Shift_F9": 
				case "Alt_F12": case "Control_F3": case "Control_F6": case "Shift_F6": case "Control_s": 
				case "Control_f": case "Control_g": case "Control_h": case "a":
				case "p": case "Control_p": case "Alt_k": case "l": case "n":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/Overlays C:/Temp/Preview.png Anim_Infobar$Small 1.00 anim_Lower_Third$Essentials 2.200 "
						+ "anim_Lower_Third$Essentials$In 1.400 anim_Lower_Third$Row 2.160 anim_Lower_Third$Row$In 0.620 \0", print_writer);
					break;
				case "F8": case "F10": case "j":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/Overlays C:/Temp/Preview.png Anim_Infobar$Push 0.500 anim_NameSupers$In 1.400 \0", print_writer);
					break;
				case "q": case "Control_q":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/Overlays C:/Temp/Preview.png Anim_Infobar$Push 0.500 anim_Boundary_LT$Essentials 2.200 "
						+ "anim_Boundary_LT$Essentials$In 1.400 anim_Boundary_LT$Row 2.160 anim_Boundary_LT$Row$In 0.620 \0", print_writer);
					break;
				}
				if(whichside == 2) {
					switch(whatToProcess.split(",")[0]) {
					case "F5": case "F6": case "F7": case "F9": case "F11":
					case "Control_F5": case "Control_F9": case "Control_a":  case "Control_c":
					case "Shift_F3": case "s": case "d": case "e": 
					case "Shift_F5": case "Shift_F9": case "Alt_F12": case "Control_F3": case "Control_F6": 
					case "Shift_F6": case "Control_s": case "Control_f": case "Control_g": case "Control_h": 
					case "Control_p": case "Alt_k": case "l": case "a"://Anim_LtChange 1.300 
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
								+ "/Default/Overlays" + " C:/Temp/Preview.png Anim_LtChange$Flag 1.300 Anim_LtChange$Sublines 1.240 "
								+ "Anim_LtChange$Topline 0.900 Anim_LtChange$Lt_Position 0.940 Anim_LtChange$HeaderDynamic 1.220 \0", print_writer);
						break;
					case "F8": case "F10": case "j":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
							+ "/Default/Overlays" + " C:/Temp/Preview.png Anim_NameSuperChange$Flag 1.300 "
							+ "Anim_NameSuperChange$Sublines 0.700 Anim_NameSuperChange$Topline 0.900 Anim_NameSuperChange$HeaderDynamic 1.220 \0", print_writer);
						break;
					case "q": case "Control_q":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
							+ "/Default/Overlays" + " C:/Temp/Preview.png Anim_Boundary_LtChange$Flag 1.300 "
							+ "Anim_Boundary_LtChange$Sublines 1.200 Anim_Boundary_LtChange$Topline 0.900 Anim_Boundary_LtChange$Lt_Position 0.940 "
							+ "Anim_Boundary_LtChange$HeaderDynamic 1.220 \0", print_writer);
						break;
					}
				}
			}
			break;
		}		
	}
	public void processBugsPreview(String whatToProcess, List<PrintWriter> print_writer, int whichside, 
		Configuration config,String whichGraphicOnScreen) 
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			if(whatToProcess.contains(",")) {
				switch(whatToProcess.split(",")[0]) {
				case "Shift_O": case "Control_k": case "k": case "g": case "f":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/Overlays" + " C:/Temp/Preview.png Anim_Bugs$Essentials 2.940 Anim_Bugs$Essentials$In 2.200 \0", print_writer);
					break;
				 case "Alt_p":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/Overlays" + " C:/Temp/Preview.png Anim_Center_Bug$In 0.800 \0", print_writer);
					break;
				}
				if(whichside == 2) {
					switch(whatToProcess.split(",")[0]) {
					case "Shift_O": case "Control_k": case "k": case "g": case "f":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
							+ "/Default/Overlays C:/Temp/Preview.png Anim_BugsChange 1.360 \0", print_writer);
						break;
					}
				}
			}
			break;
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
