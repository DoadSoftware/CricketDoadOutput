package com.cricket.captions;

import java.io.PrintWriter;
import java.util.List;

import com.cricket.model.Configuration;
import com.cricket.util.CricketFunctions;

public class Scene 
{
	public void LoadScene(String whatToProcess, List<PrintWriter> print_writers, 
		Configuration config) throws InterruptedException
	{
		CricketFunctions.DoadWriteCommandToAllViz("-1 SCENE CLEANUP\0", print_writers);
		CricketFunctions.DoadWriteCommandToAllViz("-1 IMAGE CLEANUP\0", print_writers);
		CricketFunctions.DoadWriteCommandToAllViz("-1 GEOM CLEANUP\0", print_writers);
		CricketFunctions.DoadWriteCommandToAllViz("-1 FONT CLEANUP\0", print_writers);
        CricketFunctions.DoadWriteCommandToAllViz("-1 IMAGE INFO\0", print_writers);
		
		switch (config.getBroadcaster().toUpperCase()) {
		case "ICC-U19-2023": case Constants.NPL:

			switch (whatToProcess) {
			case "FULL-FRAMERS":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER SET_OBJECT SCENE*/Default/FullFrames \0", print_writers);
		        CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*SCENE_DATA INITIALIZE \0", print_writers);
		        CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*STAGE SHOW 0.0 \0", print_writers);
				break;
			case "OVERLAYS":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER SET_OBJECT SCENE*/Default/Overlays \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*SCENE_DATA INITIALIZE \0", print_writers);
		        CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE SHOW 0.0 \0", print_writers);
		        
		        CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Lower_Third$Change$Change_Out SHOW 50.0 \0", print_writers);
		        CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*LT_MatchID$Change$Change_Out SHOW 50.0 \0", print_writers);
		        CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*LT_Comparison$Change$Change_Out SHOW 50.0 \0", print_writers);
		        CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*LT_PlayingXI$Change$Change_Out SHOW .820 \0", print_writers);
		        CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*PopUps$Out SHOW 1.100 \0", print_writers);
		        CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*LT_NextToBat$Change$Change_Out SHOW 0.700 \0", print_writers);
				break;
			}
			break;
		case "BENGAL-T20":

			switch (whatToProcess) {
			case "FULL-FRAMERS":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER SET_OBJECT SCENE*/Default/FullFrames \0", print_writers);
		        CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*SCENE_DATA INITIALIZE \0", print_writers);
		        CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*STAGE SHOW 0.0 \0", print_writers);
				break;
			case "OVERLAYS":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER SET_OBJECT SCENE*/Default/Overlays \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*SCENE_DATA INITIALIZE \0", print_writers);
		        CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE SHOW 0.0 \0", print_writers);
				break;
			}
			break;	
		case "ISPL":

			switch (whatToProcess) {
			case "FULL-FRAMERS":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER SET_OBJECT SCENE*/Default/FullFrames \0", print_writers);
		        CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*SCENE_DATA INITIALIZE \0", print_writers);
		        CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*STAGE SHOW 0.0 \0", print_writers);
				break;
			case "OVERLAYS":
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER SET_OBJECT SCENE*/Default/ScoreAnimTest \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER SET_OBJECT SCENE*/Default/Overlays \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*SCENE_DATA INITIALIZE \0", print_writers);
		      //  CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE SHOW 0.0 \0", print_writers);
				break;
			case "SO_OVERLAYS":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER SET_OBJECT SCENE*/Default/Overlays_SuperOver \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*SCENE_DATA INITIALIZE \0", print_writers);
		      //  CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*FRONT_LAYER*STAGE SHOW 0.0 \0", print_writers);
				break;	
			}
			break;
		}
	}	
 
}