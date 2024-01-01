package com.cricket.captions;

import java.io.PrintWriter;
import java.util.List;

import com.cricket.model.Configuration;
import com.cricket.util.CricketUtil;

public class Animation 
{
	public String AnimateIn(int whatToProcess, List<PrintWriter> print_writers, Configuration config)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case "ICC-U19-2023":
			for(PrintWriter print_writer : print_writers) {
				switch (whatToProcess) {
				case 1: // Scorecard
					print_writer.println("-1 RENDERER* ANIMATE START \0");
					break;
				case 2: // Bowling Card
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
	public String AnimateOut(int whatToProcess, List<PrintWriter> print_writers, Configuration config)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case "ICC-U19-2023":
			for(PrintWriter print_writer : print_writers) {
				switch (whatToProcess) {
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
	public String ChangeOn(int whatToProcess, List<PrintWriter> print_writers, Configuration config)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case "ICC-U19-2023":
			for(PrintWriter print_writer : print_writers) {
				switch (whatToProcess) {
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
	public String CutBack(int whatToProcess, List<PrintWriter> print_writers, Configuration config)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case "ICC-U19-2023":
			for(PrintWriter print_writer : print_writers) {
				switch (whatToProcess) {
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
	public String ResetAnimation(int whatToProcess, List<PrintWriter> print_writers, Configuration config)
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case "ICC-U19-2023":
			for(PrintWriter print_writer : print_writers) {
				switch (whatToProcess) {
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
}
