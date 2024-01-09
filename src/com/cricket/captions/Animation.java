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
	public String whichGraphicOnScreen = "";
	public Infobar infobar;
	
	public Animation(Infobar infobar) {
		super();
		this.infobar = infobar;
	}
	public Animation() {
		super();
	}
	public String getTypeOfGraphicsOnScreen(String whatToProcess)
	{
		switch (whatToProcess.split(",")[0]) {
		case "F1": case "F2": case "F4": case "Shift_F10": case "Shift_F11": case "m": case "Control_m": 
		case "Control_F8": case "Control_d": case "Control_e": case "Control_F7": case "Control_F10":
		case "Shift_K":
			return Constants.FULL_FRAMER;
		case "F5": case "F6": case "F7": case "F8": case "F9": case "F10": case "F11":
		case "Control_F5": case "Control_F9": case "Control_a": case "Alt_k":
		case "Shift_F3": case "s": case "d": case "e":
			return Constants.LOWER_THIRD;
		case "Alt_1": case "Alt_2": 
			return Constants.INFO_BAR;
		case "Shift_O": case "Control_k": case "k": case "g": case "f":
			return Constants.BUGS;
		}
		return "";
	}
	
	public String AnimateIn(String whatToProcess, List<PrintWriter> print_writers, Configuration config) 
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:

			//Full framers
			switch (whatToProcess.split(",")[0]) {
			case "F1": case "F2": case "F4": case "Shift_F10": case "Shift_F11": case "m": case "Control_m": 
			case "Control_F8": case "Control_d": case "Control_e": case "Control_F7": case "Control_F10":
			case "Shift_K":
				AnimateIn("ArrowDown,", print_writers, config); // Push infobar
				processAnimation(Constants.BACK, print_writers, "In_Out", "START");
				switch (whatToProcess.split(",")[0]) {
				case "m": case "Control_m": case "Shift_F11": case "Control_F7":
					processAnimation(Constants.BACK, print_writers, "Header_Shrink", "START");
					break;
				}
				switch (whatToProcess.split(",")[0]) {
				case "m": case "Shift_K":
					processAnimation(Constants.BACK, print_writers, "Base_Gradient", "START");
					break;
				}
				this.whichGraphicOnScreen = whatToProcess;
				break;
			//NameSuperDB, HOWOUT, LTBatProfile, NameSuperPlayer, LtBallProfile, BatThisMatch, BallThisMatch
			case "F5": case "F6": case "F7": case "F9": case "F11":
			case "Control_F5": case "Control_F9": case "Control_a": case "Alt_k":
			case "Shift_F3": case "s": case "d": case "e":
				AnimateIn("Alt_f,", print_writers, config); // Shrink infobar
				processAnimation(Constants.FRONT, print_writers, "Essentials", "START");
				processAnimation(Constants.FRONT, print_writers, "Row", "START");
				processAnimation(Constants.FRONT, print_writers, "HeaderDynamic", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "F8": case "F10":
				AnimateIn("Alt_f,", print_writers, config); // Shrink infobar
				processAnimation(Constants.FRONT, print_writers, "anim_NameSupers", "START");
				processAnimation(Constants.FRONT, print_writers, "HeaderDynamic", "START");
				this.whichGraphicOnScreen = whatToProcess;
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
				}
				break;
			case "Alt_f":
				if(this.infobar.isInfobar_on_screen() == true) {
					switch (this.infobar.getInfobar_status()) {
					case Constants.SHRUNK_INFOBAR:
						processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Small", "CONTINUE REVERSE");
						this.infobar.setInfobar_status(Constants.TWO_LINER_INFOBAR);
						break;
					case Constants.TWO_LINER_INFOBAR:
						processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Small", "START");
						this.infobar.setInfobar_status(Constants.SHRUNK_INFOBAR);
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
			case "F1": case "F2": case "F4": case "Shift_F10": case "Shift_F11": case "m": case "Control_m": 
			case "Control_F8": case "Control_d": case "Control_e": case "Control_F7": case "Control_F10":
			case "Shift_K":
				processAnimation(Constants.BACK, print_writers, "Anim_FullFrames$In_Out", "CONTINUE");
				switch (whatToProcess.split(",")[0]) {
				case "m": case "Control_m": case "Shift_F11": case "Control_F7":
					processAnimation(Constants.BACK, print_writers, "Header_Shrink", "CONTINUE");
					break;
				}
				switch (whatToProcess.split(",")[0]) {
				case "m": case "Shift_K":
					processAnimation(Constants.BACK, print_writers, "Base_Gradient", "START");
					break;
				}
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn("ArrowUp,", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				break;
			case "F5": case "F6": case "F7": case "F9": case "F11":
			case "Control_F5": case "Control_F9": case "Control_a": case "Alt_k":
			case "Shift_F3": case "s": case "d": case "e":
				processAnimation(Constants.FRONT, print_writers, "anim_Lower_Third", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn("Alt_f,", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				//print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*HeaderDynamic SHOW 0.0 \0");
				break;
			case "F8": case "F10":
				processAnimation(Constants.FRONT, print_writers, "anim_NameSupers", "CONTINUE");
				TimeUnit.MILLISECONDS.sleep(1000);
				AnimateIn("Alt_f,", print_writers, config); // Restore infobar
				this.whichGraphicOnScreen = "";
				//print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*HeaderDynamic SHOW 0.0 \0");
				break;
				
			case "Shift_O": case "Control_k": case "k": case "g": case "f":
				processAnimation(Constants.FRONT, print_writers, "Anim_Bugs$Essentials", "CONTINUE");
				this.whichGraphicOnScreen = "";
				break;
			
			case "F12": //Infobar
				if(infobar.isInfobar_on_screen()) {
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
			case "F1": case "F2": case "F4":
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
					case "Shift_F10":
						processAnimation(Constants.BACK, print_writers, "Change$Summary", "START");
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
					case "Shift_F10":
						processAnimation(Constants.BACK, print_writers, "Change$Summary", "START");
						break;
					}
				}
				break;
			case "F5": case "F6": case "F7": case "F9": case "F11":
			case "Control_F5": case "Control_F9": case "Control_a": case "Alt_k":
			case "Shift_F3": case "s": case "d": case "e":
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "F8": case "F10":
				processAnimation(Constants.FRONT, print_writers, "Anim_NameSuperChange", "START");
				this.whichGraphicOnScreen = whatToProcess;
				break;
			case "Alt_1": case "Alt_2":
				switch(whatToProcess.split(",")[0]) {
				case "Alt_1":
					processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Bottom_Left", "START");
					break;
				case "Alt_2":
					processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Right_Info", "START");
					break;
				}
				break;
			}
			break;
		}
		return CricketUtil.YES;
	}
	public String CutBack(String whatToProcess,List<PrintWriter> print_writers, Configuration config)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:

			if(!whatToProcess.contains(",")) {
				return CricketUtil.NO;
			}	
			
			switch(whichGraphicOnScreen.split(",")[0]) {
			case "Alt_1":
				processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Bottom_Left", "SHOW 0.0");
				break;
			case "Alt_2":
				processAnimation(Constants.FRONT, print_writers, "Anim_Infobar$Change_Right_Info", "SHOW 0.0");
				break;
			case "F5": case "F6": case "F7": case "F9": case "F11":
			case "Control_F5": case "Control_F9": case "Control_a": case "Alt_k":
			case "Shift_F3": case "s": case "d": case "e":
				processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "SHOW 0.0");
				break;
			case "F8": case "F10":
				processAnimation(Constants.FRONT, print_writers, "Anim_NameSuperChange", "SHOW 0.0");
				break;
			case "F1": case "F2": case "F4":
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
				case "Shift_F10":
					processAnimation(Constants.BACK, print_writers, "Change$Summary", "SHOW 0.0");
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
					case "Shift_F10":
						processAnimation(Constants.BACK, print_writers, "Change$Summary", "SHOW 0.0");
						break;
					}
					this.whichGraphicOnScreen = whatToProcess;
				}
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

			processAnimation(Constants.FRONT, print_writers, "Anim_Lower_Third", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_LtChange", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "anim_Boundary_LT", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_Boundary_LTChange", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_Bugs", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_BugsChange", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_Mini", "SHOW 0.0");
			processAnimation(Constants.FRONT, print_writers, "Anim_MiniChange", "SHOW 0.0");
			if(whatToProcess.contains("INFOBAR")) {
				processAnimation(Constants.FRONT, print_writers, "Anim_Infobar", "SHOW 0.0");
				this.infobar.setInfobar_on_screen(false);
				this.infobar.setInfobar_status("");
				this.infobar.setInfobar_on_screen(false);
			}
			this.whichGraphicOnScreen = "";
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
	
	public void processQuidichCommands(String whatToProcess, List<PrintWriter> print_writers, Configuration config)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			print_writers.get(0).printf("%s",whatToProcess);
			break;
		}
	}
}
