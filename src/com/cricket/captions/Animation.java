package com.cricket.captions;

import java.io.PrintWriter;
import java.util.List;

import com.cricket.model.Configuration;
import com.cricket.util.CricketUtil;

public class Animation 
{
	public String AnimateIn(String whatToProcess, List<PrintWriter> print_writers, Configuration config)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case "ICC-U19-2023":
			for(PrintWriter print_writer : print_writers) {
				switch (Integer.valueOf(whatToProcess.split(",")[0])) {
				//Score card ,bowling card, summary ,match id,match promo
				case 112: case 113: case 16122: case 77: case 1777:
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In_Out START \0");
					switch (Integer.valueOf(whatToProcess.split(",")[0])) {
					case 77: case 1777: case 16122:
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header_Shrink START \0");
						break;
					}
					break;
				//NameSuperDB, HOWOUT, LTBatProfile, NameSuperPlayer, LtBallProfile, BatThisMatch, BallThisMatch
				case 121: case 117: case 118: case 119: case 122: case 116: case 120:
				case 17116: case 17120: case 1765: case 1875:
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Essentials START \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Row START \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*HeaderDynamic START \0");
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
				switch (Integer.valueOf(whatToProcess)) {
				case 112: case 113: case 16122: case 77: case 1777:
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*In_Out CONTINUE \0");
					switch (Integer.valueOf(whatToProcess)) {
					case 77: case 1777: case 16122:
						print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header_Shrink CONTINUE \0");
						break;
					}
					break;
				//NameSuperDB, HOWOUT, LTBatProfile, NameSuperPlayer, LtBallProfile, BatThisMatch, BallThisMatch
				case 121: case 117: case 118: case 119: case 122: case 116: case 120:
				case 17116: case 17120: case 1765: case 1875:
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Essentials CONTINUE \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Row CONTINUE \0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*HeaderDynamic SHOW 0.0 \0");
					break;
				}
			}
			break;
		}
		return CricketUtil.YES;
	}	
	public String ChangeOn(String whatToProcess,int whichGrapicOnScreen,List<PrintWriter> print_writers, Configuration config)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case "ICC-U19-2023":
			for(PrintWriter print_writer : print_writers) {
				
				switch(whichGrapicOnScreen) {
				case 112:
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header START \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Batting_Card START \0");
					break;
				case 113:
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header START \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Bowling_Card START \0");
					break;
				}
				
				switch (Integer.valueOf(whatToProcess.split(",")[0])) {
				case 112:
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Batting_Card START \0");
					break;
				case 113:
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Bowling_Card START \0");
					break;
				}
			}
			break;
		}
		return CricketUtil.YES;
	}
	public String CutBack(String whatToProcess,int whichGrapicOnScreen,List<PrintWriter> print_writers, Configuration config)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case "ICC-U19-2023":
			for(PrintWriter print_writer : print_writers) {
				switch (Integer.valueOf(whatToProcess.split(",")[0])) {
				case 112:
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Batting_Card SHOW 0.0 \0");
					print_writer.println("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Bowling_Card SHOW 0.0 \0");
					break;
				case 113:
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
