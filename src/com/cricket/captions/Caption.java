package com.cricket.captions;

import java.io.PrintWriter;
import java.util.List;

import com.cricket.model.Configuration;
import com.cricket.util.CricketUtil;

public class Caption 
{
	public String PopulateGraphics(int whatToProcess, int WhichSide, List<PrintWriter> print_writers, 
		Configuration config) throws InterruptedException
	{
		for(PrintWriter print_writer : print_writers) {
			switch (whatToProcess) {
			case 1: // Scorecard FF
				PopulateFfHeader(WhichSide, print_writers, config);
				switch (config.getBroadcaster().toUpperCase()) {
				case "ICC-U19-2023":
					print_writer.println("-1 LAYER1*EVEREST*TREEVIEW*Main*Side_" + WhichSide + "*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*SCENE_DATA INITIALIZE \0");
					// on error return CricketUtil.NO;
					break;
				case "IPL-2023":
					print_writer.println("-1 LAYER1*EVEREST*TREEVIEW*Main*Side_" + WhichSide + "*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*SCENE_DATA INITIALIZE \0");
					break;
				}
				PopulateFfFooter(WhichSide, print_writers, config);
				break;
			case 2: // Bowling FF
				PopulateFfHeader(WhichSide, print_writers, config);
				switch (config.getBroadcaster().toUpperCase()) {
				case "ICC-U19-2023":
					print_writer.println("-1 LAYER1*EVEREST*TREEVIEW*Main*Side_" + WhichSide + "*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*SCENE_DATA INITIALIZE \0");
					break;
				case "IPL-2023":
					print_writer.println("-1 LAYER1*EVEREST*TREEVIEW*Main*Side_" + WhichSide + "*FUNCTION*TAG_CONTROL SET vSelectPowerplay " + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*SCENE_DATA INITIALIZE \0");
					break;
				}
				PopulateFfFooter(WhichSide, print_writers, config);
				break;
			}
		}

		return CricketUtil.YES;
	}	
	public void PopulateFfHeader(int WhichSide, List<PrintWriter> print_writers, Configuration config) 
	{
		for(PrintWriter print_writer : print_writers) {
			switch (config.getBroadcaster().toUpperCase()) {
			case "ICC-U19-2023":
				print_writer.println("-1 RENDERER*BACK_LAYER SET_OBJECT SCENE*" + "Full Framers scene" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*SCENE_DATA INITIALIZE \0");
				break;
			case "IPL-2023":
				print_writer.println("-1 RENDERER*BACK_LAYER SET_OBJECT SCENE*" + "Full Framers scene" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*SCENE_DATA INITIALIZE \0");
				break;
			}
		}
	}
	public void PopulateFfFooter(int WhichSide, List<PrintWriter> print_writers, Configuration config) 
	{
		for(PrintWriter print_writer : print_writers) {
			switch (config.getBroadcaster().toUpperCase()) {
			case "ICC-U19-2023":
				print_writer.println("-1 RENDERER*BACK_LAYER SET_OBJECT SCENE*" + "Full Framers scene" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*SCENE_DATA INITIALIZE \0");
				break;
			case "IPL-2023":
				print_writer.println("-1 RENDERER*BACK_LAYER SET_OBJECT SCENE*" + "Full Framers scene" + "\0");
				print_writer.println("-1 RENDERER*BACK_LAYER*SCENE_DATA INITIALIZE \0");
				break;
			}
		}
	}
 
}