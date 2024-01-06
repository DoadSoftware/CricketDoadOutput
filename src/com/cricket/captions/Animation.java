package com.cricket.captions;

import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.cricket.model.Configuration;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;

public class Animation 
{
	public boolean scoreBugIsShrink = false;
	
	public String AnimateIn(String whatToProcess, List<PrintWriter> print_writers, Configuration config)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case "ICC-U19-2023":
			for(PrintWriter print_writer : print_writers) {
				switch (whatToProcess.split(",")[0]) {
				//Score card ,bowling card, summary ,match id,match promo
				case "F1": case "F2": case "F4": case "Shift_F11": case "m": case "Control_m": case "Control_F8": case "Control_d": case "Control_e": case "Control_F7":
				case "Shift_K":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In_Out START \0");
					switch (whatToProcess.split(",")[0]) {
					case "m": case "Control_m": case "Shift_F11": case "Control_F7":
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header_Shrink START \0");
						break;
					}
					switch (whatToProcess.split(",")[0]) {
					case "m": case "Shift_K":
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Base_Gradient START \0");
						break;

					}
					break;
				//NameSuperDB, HOWOUT, LTBatProfile, NameSuperPlayer, LtBallProfile, BatThisMatch, BallThisMatch
				case "F5": case "F6": case "F7": case "F8": case "F9": case "F10": case "F11":
				case "Control_F5": case "Control_F9": case "Control_a": case "Alt_k":
				case "Shift_F3": case "s": case "d": case "e":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Essentials START \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Row START \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*HeaderDynamic START \0");
					break;

				case "F12": //Infobar
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*In_Out START \0");
					break;
				case "ArrowUp":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Push CONTINUE \0");
					break;
				case "ArrowDown":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Push START \0");
					break;
				case "Alt_f":
					if(scoreBugIsShrink == false) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Small START \0");
						scoreBugIsShrink = true;
					}else if(scoreBugIsShrink == true) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Small CONTINUE REVERSE \0");
						scoreBugIsShrink = false;
					}
					break;
				}
			}
			break;
		}
		return CricketUtil.YES;
	}	
	public String AnimateOut(String whatToProcess, List<PrintWriter> print_writers, Configuration config)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case "ICC-U19-2023":
			for(PrintWriter print_writer : print_writers) {
				switch (whatToProcess) {
				case "F1": case "F2": case "F4": case "Shift_F11": case "m": case "Control_m": case "Control_F8": case "Control_d": case "Control_e": case "Control_F7":
				case "Shift_K":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In_Out CONTINUE \0");
					switch (whatToProcess) {
					case "m": case "Control_m": case "Shift_F11": case "Control_F7":
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header_Shrink CONTINUE \0");
						break;
					}
					switch (whatToProcess) {
					case "m": case "Shift_K":
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Base_Gradient START \0");
						break;
					}
					break;
				//NameSuperDB, HOWOUT, LTBatProfile, NameSuperPlayer, LtBallProfile, BatThisMatch, BallThisMatch
				case "F10": case "F6": case "F7": case "F8": case "F11": case "F5": case "F9":
				case "Control_F5": case "Control_F9": case "Control_a": case "Alt_k":
				case "Shift_F3": case "s": case "d": case "e":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Essentials CONTINUE \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Row CONTINUE \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*HeaderDynamic SHOW 0.0 \0");
					break;
				case "F12": //Infobar
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*In_Out CONTINUE \0");
					break;
				}
			}
			break;
		}
		return CricketUtil.YES;
	}	
	public String ChangeOn(String whatToProcess,String whichGrapicOnScreen,List<PrintWriter> print_writers, Configuration config) throws InterruptedException
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case "ICC-U19-2023":
			for(PrintWriter print_writer : print_writers) {
				
				switch(whichGrapicOnScreen) {
				case "F1":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header START \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Batting_Card START \0");
					break;
				case "F2":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header START \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Bowling_Card START \0");
					break;
				}
				
				switch (whatToProcess.split(",")[0]) {
				case "Alt_1":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Bottom_Left START \0");
					break;
				case "Alt_2":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_RightInfo START \0");
					break;
				case "F1":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Batting_Card START \0");
					break;
				case "F2":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Bowling_Card START \0");
					break;
				}
			}
			break;
		}
		return CricketUtil.YES;
	}
	public String CutBack(String whatToProcess,String whichGrapicOnScreen,List<PrintWriter> print_writers, Configuration config)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case "ICC-U19-2023":
			for(PrintWriter print_writer : print_writers) {
				switch (whatToProcess.split(",")[0]) {
				case "Alt_1":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_Bottom_Left SHOW 0.0 \0");
					break;
				case "Alt_2":
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Change_RightInfo SHOW 0.0 \0");
					break;
				case "F1":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Batting_Card SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Bowling_Card SHOW 0.0 \0");
					break;
				case "F2":
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Batting_Card SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Bowling_Card SHOW 0.0 \0");
					break;
				}
			}
			break;
		}
		return CricketUtil.YES;
	}	
	public String ResetAnimation(String whatToProcess, List<PrintWriter> print_writers, Configuration config)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case "ICC-U19-2023":
			for(PrintWriter print_writer : print_writers) {
				switch (Integer.valueOf(whatToProcess.split(",")[0])) {
				case 1:
					print_writer.println("-1 RENDERER* ANIMATE START \0");
					break;
				case 2:
					print_writer.println("-1 RENDERER* CHANGE START \0");
					//5 second delay
					print_writer.println("-1 RENDERER* CHANGE START \0");
					break;
				case 3:
					print_writer.println("-1 RENDERER* CHANGE START \0");
					break;
				}
			}
			break;
		}
		return CricketUtil.YES;
	}
	public void processAnimation(List<PrintWriter> print_writers,String animationDirectorName, String animationCommand)
	{
		CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*"
			+ animationDirectorName + " " + animationCommand +" \0", print_writers);
	}
	public void scoreBugAnimation(List<PrintWriter> print_writers,String whichGraphicOnScreen,String whichGrapihc,Configuration config) throws InterruptedException
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case "ICC-U19-2023":
			for(PrintWriter print_writer : print_writers) {
				switch (whichGraphicOnScreen) {
				case "F12":
					switch(whichGrapihc) {
					case "F1": case "F2": case "F4": case "Shift_F11": case "m": case "Control_m": case "Control_F8": case "Control_d": case "Control_e":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Push START \0");
						TimeUnit.MILLISECONDS.sleep(1000);
						break;
					case "F5": case "F6": case "F7": case "F8": case "F9": case "F10": case "F11":case "Control_F5": case "Control_F9": case "Control_a":
					case "Alt_k": case "Shift_F3": case "s": case "d": case "e":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Small START \0");
						TimeUnit.MILLISECONDS.sleep(1000);
						break;
					}
					break;
				case "F1": case "F2": case "F4": case "Shift_F11": case "m": case "Control_m": case "Control_F8": case "Control_d": case "Control_e":
					TimeUnit.MILLISECONDS.sleep(1000);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Push CONTINUE \0");
					break;
				case "F5": case "F6": case "F7": case "F8": case "F9": case "F10": case "F11":case "Control_F5": case "Control_F9": case "Control_a":
				case "Alt_k": case "Shift_F3": case "s": case "d": case "e":
					TimeUnit.MILLISECONDS.sleep(1000);
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Small CONTINUE REVERSE \0");
					break;
					
				}
			}
			break;
		}
	}
	public boolean ClearAll(List<PrintWriter> print_writers) {
		
		for(PrintWriter print_writer : print_writers) {
			print_writer.println("-1 SCENE CLEANUP\0");
            print_writer.println("-1 IMAGE CLEANUP\0");
            print_writer.println("-1 GEOM CLEANUP\0");
            print_writer.println("-1 FONT CLEANUP\0");
            
            print_writer.println("-1 IMAGE INFO\0");
            print_writer.println("-1 RENDERER*FRONT_LAYER SET_OBJECT SCENE*/Default/Overlays\0");
	           	
            print_writer.println("-1 RENDERER*FRONT_LAYER INITIALIZE\0");
            print_writer.println("-1 RENDERER*FRONT_LAYER*SCENE_DATA INITIALIZE\0");
            print_writer.println("-1 RENDERER*FRONT_LAYER*UPDATE SET 0\0");
            print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE SHOW 0.0\0");
           //print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Reset START \0");
            
            print_writer.println("-1 RENDERER*FRONT_LAYER*UPDATE SET 1\0");
            
            print_writer.println("-1 RENDERER*BACK_LAYER SET_OBJECT SCENE*/Default/FullFrames\0");
	           	
            print_writer.println("-1 RENDERER*BACK_LAYER INITIALIZE\0");
            print_writer.println("-1 RENDERER*BACK_LAYER*SCENE_DATA INITIALIZE\0");
            print_writer.println("-1 RENDERER*BACK_LAYER*UPDATE SET 0\0");
            print_writer.println("-1 RENDERER*BACK_LAYER*STAGE SHOW 0.0\0");
            print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Reset START \0");
            
            print_writer.println("-1 RENDERER*BACK_LAYER*UPDATE SET 1\0");
            
            print_writer.println("-1 SCENE CLEANUP\0");
            print_writer.println("-1 IMAGE CLEANUP\0");
            print_writer.println("-1 GEOM CLEANUP\0");
            print_writer.println("-1 FONT CLEANUP\0");
		}
		return false;
	}
	
}
