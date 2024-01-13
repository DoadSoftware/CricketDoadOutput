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
					+ "TRANSFORMATION*POSITION*Y SET 106.0 \0",print_writers);
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*TREE*$All_NameSupers$Overall_Position_Y*"
						+ "TRANSFORMATION*POSITION*Y SET 56.0 \0",print_writers);
			}
			break;
		}
		
	}
	
	public String getTypeOfGraphicsOnScreen(String whatToProcess)
	{
		switch (whatToProcess.split(",")[0]) {
		case "F1": case "F2": case "F4": case "Shift_F10": case "Shift_F11": case "m": case "Control_m": 
		case "Control_F8": case "Control_d": case "Control_e": case "Control_F7": case "Control_F10":
		case "Shift_K": case "Alt_F9":
			return Constants.FULL_FRAMER;
		case "F5": case "F6": case "F7": case "F8": case "F9": case "F10": case "F11":
		case "Control_F1": case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_k":
		case "Shift_F3": case "s": case "d": case "e": case "q": case "Shift_F5": case "Shift_F9": case "Alt_F12":
		case "p": case "Control_p": case "j": case "Control_F6": case "Shift_F6": case "Control_s":
			return Constants.LOWER_THIRD;
		case "Alt_1": case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_5": case "Alt_6": case "Alt_7": case "Alt_8":
			return Constants.INFO_BAR;
		case "Shift_O": case "Control_k": case "k": case "g": case "f": case "Alt_p":
			return Constants.BUGS;
		}
		return "";
	}
	
	public String AnimateIn(String whatToProcess, List<PrintWriter> print_writers, Configuration config) throws InterruptedException 
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:

			//Full framers
			switch (whatToProcess.split(",")[0]) {
			case "F1": case "F2": case "F4": case "Control_F1": case "Shift_F10": case "Shift_F11": case "m": case "Control_m": 
			case "Control_F8": case "Control_d": case "Control_e": case "Control_F7": case "Control_F10":
			case "Shift_K": case "Alt_F9":
				
				setVariousAnimationsKeys("ANIMATE-IN", print_writers, config);
				AnimateIn("ArrowDown,", print_writers, config); // Push infobar
				TimeUnit.MILLISECONDS.sleep(500);
				
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames", "START");
				
				switch (whatToProcess.split(",")[0]) {
				case "m": case "Control_m": case "Shift_F11": case "Control_F7":
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
			case "p": case "Control_p": case "Control_F6": case "Shift_F6": case "Control_s":
				
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
			case "F8": case "F10": case "j":
				
				setPositionOfLowerThirds(config, print_writers);
				AnimateIn(Constants.MIDDLE + Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Shrink infobar
				TimeUnit.MILLISECONDS.sleep(1000);
				processAnimation(Constants.FRONT, print_writers, "anim_NameSupers", "START");
				processAnimation(Constants.FRONT, print_writers, "HeaderDynamic", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
				
			case "q":
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
			case "F1": case "F2": case "F4": case "Control_F1": case "Shift_F10": case "Shift_F11": case "m": case "Control_m": 
			case "Control_F8": case "Control_d": case "Control_e": case "Control_F7": case "Control_F10":
			case "Shift_K": case "Alt_F9":
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out", "CONTINUE");
				switch (whatToProcess.split(",")[0]) {
				case "m": case "Control_m": case "Shift_F11": case "Control_F7":
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
			case "p": case "Control_p": case "Control_F6": case "Shift_F6": case "Control_s":
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
				
			case "q":
				processAnimation(Constants.FRONT, print_writers, "anim_Boundary_LT", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn(Constants.SHRUNK_INFOBAR + ",", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "Shift_O": case "Control_k": case "k": case "g": case "f":
				processAnimation(Constants.FRONT, print_writers, "Anim_Bugs$Essentials", "CONTINUE");
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
			case "F1": case "F2": case "F4": case "Shift_F11": case "Control_F8":
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
						break;
					case "Shift_F11":
						processAnimation(Constants.BACK, print_writers, "Change$Summary", "START");
						break;
					case "Control_F8":
						processAnimation(Constants.BACK, print_writers, "Change$LineUp_Image", "START");
						TimeUnit.MILLISECONDS.sleep(1000);
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
					case "Shift_F11":
						processAnimation(Constants.BACK, print_writers, "Change$Summary", "START");
						break;
					}
				}
				System.out.println("lastNumberOfRows = " + lastNumberOfRows);
				System.out.println("caption.this_fullFramesGfx.numberOfRows = " + caption.this_fullFramesGfx.numberOfRows);
				if(caption.this_fullFramesGfx.numberOfRows != lastNumberOfRows) {
					System.out.println("ConcussExtend_Y START");
					processAnimation(Constants.BACK, print_writers, "ConcussExtend_Y", "START");
					lastNumberOfRows = caption.this_fullFramesGfx.numberOfRows;
				}
				break;
			case "F5": case "F6": case "F7": case "F9": case "F11":
			case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_k":
			case "Shift_F3": case "s": case "d": case "e": case "Shift_F5": case "Shift_F9": case "Alt_F12":
			case "p": case "Control_p": case "Control_F6": case "Shift_F6": case "Control_s":
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "START");
				break;
			case "F8": case "F10": case "j":
				processAnimation(Constants.FRONT, print_writers, "Anim_NameSuperChange", "START");
				break;
			case "q":
				processAnimation(Constants.FRONT, print_writers, "Anim_Boundary_LTChange", "START");
				break;
			case "Alt_1": case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_5": case "Alt_6": case "Alt_7": case "Alt_8":
				switch(whatToProcess.split(",")[0]) {
				case "Alt_1":
					processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Bottom_Left", "START");
					break;
				case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_5": case "Alt_6":
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
			case "Alt_2": case "Alt_3": case "Alt_4": case "Alt_5": case "Alt_6":
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
			case "F5": case "F6": case "F7": case "F9": case "F11": case "p": case "Control_p":
			case "Control_F5": case "Control_F9": case "Control_a":  case "Control_F3": case "Alt_k":
			case "Control_F6": case "Shift_F6": case "Control_s":
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
					processAnimation(Constants.BACK, print_writers, "Change$Batting_Card", "SHOW 0.0");
					break;
				case "F2":  
					processAnimation(Constants.BACK, print_writers, "Change$Bowling_Card", "SHOW 0.0");
					break;
				case "F4":
					processAnimation(Constants.BACK, print_writers, "Change$Partnership_List", "SHOW 0.0");
					break;
				case "Shift_F11":
					processAnimation(Constants.BACK, print_writers, "Change$Summary", "SHOW 0.0");
					break;
				case "Control_F8":
					processAnimation(Constants.BACK, print_writers, "Change$LineUp_Image", "SHOW 0.0");
					break;
				}
				if(!whichGraphicOnScreen.split(",")[0].equalsIgnoreCase(whatToProcess.split(",")[0])) {
					switch(whatToProcess.split(",")[0]) {
					case "F1":  
						processAnimation(Constants.BACK, print_writers, "Change$Batting_Card", "SHOW 0.0");
						break;
					case "F2":
						processAnimation(Constants.BACK, print_writers, "Change$Bowling_Card", "SHOW 0.0");
						break;
					case "F4":
						processAnimation(Constants.BACK, print_writers, "Change$Partnership_List", "SHOW 0.0");
						break;
					case "Shift_F11":
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
			
			if(infobar.isInfobar_on_screen() == true) {
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
				print_writers.get(0).printf("%s","F4");
				break;
			case "6": // Stand-By
				print_writers.get(0).printf("%s","F6");
				break;
			case "7": //Animate-In
				print_writers.get(0).printf("%s","F6");
				TimeUnit.MILLISECONDS.sleep(100);
				print_writers.get(0).printf("%s","F7");
				break;
			case "8": //Animate-Out
				print_writers.get(0).printf("%s","F9");
				TimeUnit.MILLISECONDS.sleep(1000);
				print_writers.get(0).printf("%s","F4");
				break;
			case "9": //Load	
				print_writers.get(0).printf("%s","LOAD");
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
	
	public void processFullFramesPreview(String whatToProcess, List<PrintWriter> print_writer, int whichside) {
		if(whatToProcess.contains(",")) {
			if(whichside == 1) {
				switch(whatToProcess.split(",")[0]) {
				case "F1"://battingCard
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png Anim_FullFrames$In_Out$Essentials$In 2.140 "
						+ "Anim_FullFrames$In_Out$Header$In 1.800 Anim_FullFrames$In_Out$Main$Batting_Card$In 1.860 "
						+ "Anim_FullFrames$In_Out$Footer$In 1.800 \0", print_writer);
					break;
				case "F2"://bowlingCard
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png Anim_FullFrames$In_Out$Essentials$In 2.140 "
						+ "Anim_FullFrames$In_Out$Header$In 1.800 Anim_FullFrames$In_Out$Main$Bowling_Card$In 1.860 "
						+ "Anim_FullFrames$In_Out$Footer$In 1.800 \0", print_writer);
					break;
				case "F4": //All Partnership
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png Anim_FullFrames$In_Out$Essentials$In 2.140 "
						+ "Anim_FullFrames$In_Out$Header$In 1.800 Anim_FullFrames$In_Out$Main$Partnership_List$In 1.820 "
						+ "Anim_FullFrames$In_Out$Footer$In 1.800 Sponsor 1.200 \0", print_writer);
					break;
				case "Control_F7":// Double Teams
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png Anim_FullFrames$In_Out$Essentials$In 2.140 "
						+ "Anim_FullFrames$In_Out$Header$In 1.800 Anim_FullFrames$In_Out$Main$Teams$In 1.860 "
						+ "Anim_FullFrames$In_Out$Footer$In 1.800 \0", print_writer);
					break;
				case "Control_F8": //Playing XI
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png Anim_FullFrames$In_Out$Essentials$In 2.140 "
						+ "Anim_FullFrames$In_Out$Header$In 1.800 Anim_FullFrames$In_Out$Main$LineUp_Image$In 2.040 "
						+ "Anim_FullFrames$In_Out$Footer$In 1.800 \0", print_writer);
					break;
				case "Control_F1":// Photo ScoreCard
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png Anim_FullFrames$In_Out$Essentials$In 2.140 "
						+ "Anim_FullFrames$In_Out$Header$In 1.800 Anim_FullFrames$In_Out$Main$Batting_Card_Image$In 2.040 "
						+ "Anim_FullFrames$In_Out$Footer$In 1.800 \0", print_writer);
					break;
				
				case "Control_F10"://Manhattan
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png Anim_FullFrames$In_Out$Essentials$In 2.140 "
						+ "Anim_FullFrames$In_Out$Header$In 1.800 Anim_FullFrames$In_Out$Main$Manhattan$In 2.900 "
						+ "Anim_FullFrames$In_Out$Footer$In 1.800 \0", print_writer);
					break;
				case "Shift_F10"://WORMS
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png Anim_FullFrames$In_Out$Essentials$In 2.140 "
						+ "Anim_FullFrames$In_Out$Header$In 1.800 Anim_FullFrames$In_Out$Main$Worm$In 2.440 "
						+ "Anim_FullFrames$In_Out$Footer$In 1.800 \0", print_writer);
					break;
				case "Shift_F11":  //MATCH SUMMARY
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png Anim_FullFrames$In_Out$Essentials$In 2.140 "
						+ "Anim_FullFrames$In_Out$Header$In 1.800 Anim_FullFrames$In_Out$Main$Summary$In 1.820 "
						+ "Anim_FullFrames$In_Out$Footer$In 1.800 \0", print_writer);
					break;
				case "m"://Match id	
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
							+ "/Default/FullFrames" + " C:/Temp/Preview.png Anim_FullFrames$In_Out$Essentials$In 2.140 "
							+ "Anim_FullFrames$In_Out$Header$In 1.800 Anim_FullFrames$In_Out$Main$Ident$In 1.920 "
							+ "Anim_FullFrames$In_Out$Footer$In 1.800 "
							+ "Base_Gradient 0.500 \0", print_writer);
					break;
				case "Control_m": //MATCH PROMO
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png Anim_FullFrames$In_Out$Essentials$In 2.140 "
						+ "Anim_FullFrames$In_Out$Header$In 1.800 Anim_FullFrames$In_Out$Main$Ident$In 1.920 "
						+ "Anim_FullFrames$In_Out$Footer$In 1.800 \0", print_writer);
					break;
				case "Control_d": case "Control_e"://PlayerProfile
					if(Integer.valueOf(whatToProcess.split(",")[4]) > 0) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png Anim_FullFrames$In_Out$Essentials$In 2.140 "
						+ "Anim_FullFrames$In_Out$Header$In 1.800 Anim_FullFrames$In_Out$Main$Profile$In 2.300 "
						+ "Anim_FullFrames$In_Out$Footer$In 1.800 "
						+ "Profile_Highlight$Side1$"+whatToProcess.split(",")[4] +" 1.000 \0", print_writer);	
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png Anim_FullFrames$In_Out$Essentials$In 2.140 "
						+ "Anim_FullFrames$In_Out$Header$In 1.800 Anim_FullFrames$In_Out$Main$Profile$In 2.300 "
						+ "Anim_FullFrames$In_Out$Footer$In 1.800 \0", print_writer);	
					}
					break;
				case "Shift_K"://FFCurrPartnership
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png Anim_FullFrames$In_Out$Essentials$In 2.140 "
						+ "Anim_FullFrames$In_Out$Header$In 1.800 Anim_FullFrames$In_Out$Main$Partnership$In 2.300 "
						+ "Anim_FullFrames$In_Out$Footer$In 1.800 "
						+ "Base_Gradient 0.500 Sponsor 1.200 \0", print_writer);
					break;
				case "Alt_F9": // Single Teams
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png Anim_FullFrames$In_Out$Essentials$In 2.140 "
						+ "Anim_FullFrames$In_Out$Header$In 1.800 Anim_FullFrames$In_Out$Main$Team_Single$In 1.860 "
						+ "Anim_FullFrames$In_Out$Footer$In 1.800 \0", print_writer);
					break;
				}
			}
			if(whichside == 2) {
				switch(whatToProcess.split(",")[0]) {
				case "F1"://battingCard
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png "
						+ "Change$Header 1.320 Change$Header$Change_Out 0.420 "
						+ "Change$Batting_Card 1.380 Change$Batting_Card$Change_Out 1.380 "
						+ "Change$Footer 1.700 Change$Footer$Change_Out 0.580 \0", print_writer);
					break;
				case "F2"://bowlingCard
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png "
						+ "Change$Header 1.320 Change$Header$Change_Out 0.420 "
						+ "Change$Bowling_Card 1.300 Change$Bowling_Card$Change_Out 1.840 "
						+ "Change$Footer 1.700 Change$Footer$Change_Out 0.580 \0", print_writer);
					break;
				case "F4":	 //All Partnership 
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png "
						+ "Change$Header 1.320 Change$Header$Change_Out 0.420 "
						+ "Change$Partnership_List 1.360 Change$Partnership_List$Change_Out 0.880 "
						+ "Change$Footer 1.700 Change$Footer$Change_Out 0.580 Sponsor 1.200 \0", print_writer);
					break;
				case "Control_F7":// Double Teams
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png "
						+ "Change$Header 1.320 Change$Header$Change_Out 0.420 "
						+ "Change$Teams 1.440 Change$Teams$Change_Out 1.860 "
						+ "Change$Footer 1.700 Change$Footer$Change_Out 0.580 \0", print_writer);
					break;
				case "Control_F8":  //Playing XI
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png "
						+ "Change$Header 1.320 Change$Header$Change_Out 0.420 "
						+ "Change$LineUp_Image 1.560 Change$LineUp_Image$Change_Out 0.500 "
						+ "Change$Footer 1.700 Change$Footer$Change_Out 0.580 \0", print_writer);
					break;
				case "Shift_K"://FFCurrPartnership
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
							+ "/Default/FullFrames" + " C:/Temp/Preview.png "
							+ "Change$Header 1.320 Change$Header$Change_Out 0.420 "
							+ "Change$Partnership 1.900 Change$Partnership$Change_Out 0.600 "
							+ "Change$Footer 1.700 Change$Footer$Change_Out 0.580 Sponsor 1.200 \0", print_writer);
					break;
				case "Alt_F9": // Single Teams
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png "
						+ "Change$Header 1.320 Change$Header$Change_Out 0.420 "
						+ "Change$Team_Single 1.400 Change$Team_Single$Change_Out 0.860 "
						+ "Change$Footer 1.700 Change$Footer$Change_Out 0.580 \0", print_writer);
					break;
				case "Control_d": case "Control_e"://PlayerProfile
					if(Integer.valueOf(whatToProcess.split(",")[4]) > 0) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png "
						+ "Change$Header 1.320 Change$Header$Change_Out 0.420 "
						+ "Change$Profile 1.900 Change$Profile$Change_Out 0.600 "
						+ "Change$Footer 1.700 Change$Footer$Change_Out 0.580 "
						+ "Profile_Highlight$Side1$"+whatToProcess.split(",")[4] +"1.000 "+"\0", print_writer);	
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png "
						+ "Change$Header 1.320 Change$Header$Change_Out 0.420 "
						+ "Change$Profile 1.900 Change$Profile$Change_Out 0.600 "
						+ "Change$Footer 1.700 Change$Footer$Change_Out 0.580 "+"\0", print_writer);
					}
					
					break;
				case "m"://Match id	
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png "
						+ "Change$Header 1.320 Change$Header$Change_Out 0.420 "
						+ "Change$Ident 1.460 Change$Ident$Change_Out 0.600 "
						+ "Change$Footer 1.700 Change$Footer$Change_Out 0.580 "
						+ "Base_Gradient 0.500 \0", print_writer);
					break;
				case "Control_m"://Match promo
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png "
						+ "Change$Header 1.320 Change$Header$Change_Out 0.420 "
						+ "Change$Ident 1.460 Change$Ident$Change_Out 0.600 "
						+ "Change$Footer 1.700 Change$Footer$Change_Out 0.580 \0", print_writer);
					break;
				case "Shift_F10"://WORMS
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png "
						+ "Change$Header 1.320 Change$Header$Change_Out 0.420 "
						+ "Change$Worm 2.140 Change$Worm$Change_Out 1.860 "
						+ "Change$Footer 1.700 Change$Footer$Change_Out 0.580 \0", print_writer);
					break;
				case "Control_F10"://Manhattan
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png "
						+ "Change$Header 1.320 Change$Header$Change_Out 0.420 "
						+ "Change$Manhattan 2.420 Change$Manhattan$Change_Out 0.600 "
						+ "Change$Footer 1.700 Change$Footer$Change_Out 0.580 \0", print_writer);
					break;
				case "Control_F1":// Photo ScoreCard
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/FullFrames" + " C:/Temp/Preview.png "
						+ "Change$Header 1.320 Change$Header$Change_Out 0.420 "
						+ "Change$Batting_Card_Image 1.560 Change$Batting_Card_Image$Change_Out 0.500 "
						+ "Change$Footer 1.700 Change$Footer$Change_Out 0.580 \0", print_writer);
					break;
				}
			}
		}
	}

	public void processL3Preview(String whatToProcess, List<PrintWriter> print_writer, int whichside) {
		if(whatToProcess.contains(",")) {
			if(whichside == 1) {
				switch(whatToProcess.split(",")[0]) {
				case "F5": case "F6": case "F7": case "F9": case "F11":
				case "Control_F5": case "Control_F9": case "Control_a":  case "Control_c":
				case "Shift_F3": case "s": case "d": case "e": case "v": case "b": case "h":
				case "p": case "Control_p": case "Alt_k":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
						+ "/Default/Overlays" + " C:/Temp/Preview.png Anim_Infobar$Small 1.00 "
						+ "anim_Lower_Third$Essentials 2.200 "+ "anim_Lower_Third$Essentials$In 1.400 "
						+ "anim_Lower_Third$Row 2.160 anim_Lower_Third$Row$In 0.620 \0", print_writer);
					break;
				case "F8": case "F10": case "j":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
							+ "/Default/Overlays" + " C:/Temp/Preview.png Anim_Infobar$Small 1.00 "
							+"anim_NameSupers 3.600 anim_NameSupers$In 1.400 \0", print_writer);
					break;
				
				}
			}
			if(whichside == 2) {
				switch(whatToProcess.split(",")[0]) {
				case "F5": case "F6": case "F7": case "F9": case "F11":
				case "Control_F5": case "Control_F9": case "Control_a":  case "Control_c":
				case "Shift_F3": case "s": case "d": case "e": case "v": case "b": case "h":
				case "p": case "Control_p": case "Alt_k"://Anim_LtChange 1.300 
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
							+ "/Default/Overlays" + " C:/Temp/Preview.png Anim_Infobar$Small 1.00 "
							+ "Anim_LtChange$Flag 1.300 Anim_LtChange$Sublines 1.240 "
							+ "Anim_LtChange$Topline 0.900 Anim_LtChange$Lt_Position 0.940 "
							+ "Anim_LtChange$HeaderDynamic 1.220 \0", print_writer);
					break;
				case "F8": case "F10": case "j":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER PREVIEW SCENE*" 
							+ "/Default/Overlays" + " C:/Temp/Preview.png Anim_Infobar$Small 1.00 "
							+ "Anim_NameSuperChange 1.300 "
							+ "Anim_NameSuperChange$Flag 1.300 Anim_NameSuperChange$Sublines 1.240 "
							+ "Anim_NameSuperChange$Topline 0.900 "
							+ "Anim_NameSuperChange$HeaderDynamic 1.220 \0", print_writer);
					break;
				}
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
