package com.cricket.captions;

import java.io.PrintWriter;
import java.util.List;

import com.cricket.model.Configuration;

public class Scene 
{
	public void LoadScene(String whatToProcess, List<PrintWriter> print_writers, Configuration config) throws InterruptedException
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case "ICC-U19-2023":
			for(PrintWriter print_writer : print_writers) {
				switch (whatToProcess) {
				case "FULL-FRAMERS":
					print_writer.println("-1 RENDERER*BACK_LAYER SET_OBJECT SCENE*" + "Full Framers scene" + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*SCENE_DATA INITIALIZE \0");
					break;
				case "OVERLAYS":
					print_writer.println("-1 RENDERER*FRONT_LAYER SET_OBJECT SCENE*" + "Infobar-scene" + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*SCENE_DATA INITIALIZE \0");
					break;
				}
			}
			break;
		
		}
	}	
 
}