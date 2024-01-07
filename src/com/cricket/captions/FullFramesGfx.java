package com.cricket.captions;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import com.cricket.containers.LowerThird;
import com.cricket.model.BattingCard;
import com.cricket.model.BowlingCard;
import com.cricket.model.Configuration;
import com.cricket.model.FallOfWicket;
import com.cricket.model.Fixture;
import com.cricket.model.Ground;
import com.cricket.model.Inning;
import com.cricket.model.MatchAllData;
import com.cricket.model.NameSuper;
import com.cricket.model.Partnership;
import com.cricket.model.Player;
import com.cricket.model.Statistics;
import com.cricket.model.StatsType;
import com.cricket.model.Team;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;

public class FullFramesGfx 
{
	public int FirstPlayerId;
	public int SecondPlayerId;
	public String WhichProfile, status = "", containerName = "";
	
	public List<PrintWriter> print_writers; 
	public Configuration config;
	public List<Statistics> statistics;
	public List<StatsType> statsTypes;
	public List<MatchAllData> tournament_matches;
	public List<NameSuper> nameSupers;
	public List<Fixture> fixTures;
	public List<Team> Teams;
	public List<Ground> Grounds;
	
	public List<Player> PlayingXI;
	
	public BattingCard battingCard;
	public Inning inning;
	public Player player;
	public Statistics stat;
	public StatsType statsType;
	public LowerThird lowerThird;
	
	public NameSuper namesuper;
	public Fixture fixture;
	public Team team;
	public Ground ground;
	
	public FullFramesGfx() {
		super();
	}
	
	public FullFramesGfx(List<PrintWriter> print_writers, Configuration config, List<Statistics> statistics,
			List<StatsType> statsTypes, List<MatchAllData> tournament_matches, List<NameSuper> nameSupers,List<Fixture> fixTures, 
			List<Team> Teams, List<Ground> Grounds) {
		super();
		this.print_writers = print_writers;
		this.config = config;
		this.statistics = statistics;
		this.statsTypes = statsTypes;
		this.tournament_matches = tournament_matches;
		this.nameSupers = nameSupers;
		this.fixTures = fixTures;
		this.Teams = Teams;
		this.Grounds = Grounds;
	}
		
	public String PopulateScorecardFF(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException
	{
		status = PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning);
			if(status == Constants.OK) {
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, WhichInning);
			} else {
				return status;
			}
		} else {
			return status;
		}
	}
	public String PopulateBowlingCardFF(int WhichSide,String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException
	{
		status = PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning);
			if(status == Constants.OK) {
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, WhichInning);
			} else {
				return status;
			}
		} else {
			return status;
		}
	}
	public String populateFFMatchId(int WhichSide, String whatToProcess, MatchAllData matchAllData) throws ParseException
	{
		status = PopulateFfHeader(WhichSide, whatToProcess, matchAllData, 0);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess, matchAllData, 0);
			if(status == Constants.OK) {
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, 0);
			} else {
				return status;
			}
		} else {
			return status;
		}
	}
	public String populateMatchSummary(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException
	{
		status = PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning);
			if(status == Constants.OK) {
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, WhichInning);
			} else {
				return status;
			}
		} else {
			return status;
		}
	}
	public String populateFFMatchPromo(int WhichSide, String whatToProcess, MatchAllData matchAllData) throws NumberFormatException, ParseException
	{
		fixture = fixTures.stream().filter(fix -> fix.getMatchnumber() == 
			Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
		if(fixture == null) {
			return "Fixture id [" + Integer.valueOf(whatToProcess.split(",")[2]) + "] from database is returning NULL";
		}
		status = PopulateFfHeader(WhichSide, whatToProcess.split(",")[0], matchAllData, 0);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess.split(",")[0], matchAllData, 0);
			if(status == Constants.OK) {
				return PopulateFfFooter(WhichSide, whatToProcess.split(",")[0], matchAllData, 0);
			} else {
				return status;
			}
		} else {
			return status;
		}
	}
	public String populatePlayingXI(int WhichSide, String whatToProcess,int teamId,MatchAllData matchAllData, int WhichInning) throws ParseException
	{
		if(teamId == matchAllData.getSetup().getHomeTeamId()) {
			PlayingXI = matchAllData.getSetup().getHomeSquad();
		}else {
			PlayingXI = matchAllData.getSetup().getAwaySquad();
		}
		team = Teams.stream().filter(tm -> tm.getTeamId() == teamId).findAny().orElse(null);
		if(team == null) {
			return "Team id [" + teamId + "] from database is returning NULL";
		}
		status = PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning);
			if(status == Constants.OK) {
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, WhichInning);
			} else {
				return status;
			}
		} else {
			return status;
		}
	}
	public String populatePartnership(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException 
	{
		status = PopulateFfHeader(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
			if(status == Constants.OK) {
				return PopulateFfFooter(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
			} else {
				return status;
			}
		} else {
			return status;
		}
	}
	public String populatePlayerProfile(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException 
	{
		
		player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(whatToProcess.split(",")[2]), matchAllData);
		if(player == null) {
			return "Player id [" + whatToProcess.split(",")[2] + "] from database is returning NULL";
		}
		
		stat = statistics.stream().filter(stat -> stat.getPlayer_id() == player.getPlayerId()).findAny().orElse(null);
		if(stat == null) {
			return "No stats found for player id [" + player.getPlayerId() + "] from database is returning NULL";
		}
		
		team = Teams.stream().filter(tm -> tm.getTeamId() == player.getTeamId()).findAny().orElse(null);
		if(team == null) {
			return "Team id [" + player.getTeamId() + "] from database is returning NULL";
		}

		status = PopulateFfHeader(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
			if(status == Constants.OK) {
				return PopulateFfFooter(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
			} else {
				return status;
			}
		} else {
			return status;
		}
	}
	public String PopulateDoubleTeams(int WhichSide, String whatToProcess,MatchAllData matchAllData) throws ParseException
	{
		status = PopulateFfHeader(WhichSide, whatToProcess, matchAllData, 0);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess, matchAllData, 0);
			if(status == Constants.OK) {
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, 0);
			} else {
				return status;
			}
		} else {
			return status;
		}
	}
	public String populateCurrPartnership(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException
	{
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES))
			.findAny().orElse(null);

		if(inning == null) {
			return "Current Inning NOT found in this match";
		}
		
		status = PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning);
			if(status == Constants.OK) {
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, WhichInning);
			} else {
				return status;
			}
		} else {
			return status;
		}
		
	}
	
	public String PopulateFfHeader(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) 
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			
			switch (whatToProcess) {
			case "F1": case "F2": case "F4":
				
				ground = Grounds.stream().filter(grnd -> grnd.getFullname().contains(matchAllData.getSetup().getVenueName())).findAny().orElse(null);
				if(ground == null) {
					return "PopulateFfHeader: ground name is NULL";
				}
				
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning)
					.findAny().orElse(null);
				if(inning == null) {
					return "PopulateFfHeader: current inning is NULL";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Flag*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
					"$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamGroup() + ", " 
					+ ground.getCity() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
					"$Change$Bottom*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
					"$Select_HeaderTop$Big_First$txt_Team_1*GEOM*TEXT SET " + 
					matchAllData.getMatch().getInning().get(0).getBatting_team().getTeamName1() +"\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
					"$Select_HeaderTop$Big_First$txt_Team_2*GEOM*TEXT SET " +
					matchAllData.getMatch().getInning().get(0).getBowling_team().getTeamName1() +"\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
					"$Select_HeaderTop$Small_First$txt_Team_1*GEOM*TEXT SET " + 
					matchAllData.getMatch().getInning().get(0).getBatting_team().getTeamName1() +"\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
					"$Select_HeaderTop$Small_First$txt_Team_2*GEOM*TEXT SET " + 
					matchAllData.getMatch().getInning().get(0).getBowling_team().getTeamName1() +"\0", print_writers);
				
				switch(whatToProcess) {
				case "F1": case "F4":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" 
						+ WhichSide + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName1() + "\0", print_writers);
					if(WhichInning == 1) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side"+ WhichSide + 
								"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side"+ WhichSide + 
								"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
					}
					break;
				case "F2":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" 
						+ WhichSide + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName1() + "\0", print_writers);
					
					if(WhichInning == 1) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side"+ WhichSide + 
								"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side"+ WhichSide + 
								"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}
					break;
				}
				break;
				
			case "Control_d": case "Control_e":
				
				if(WhichSide == 1) {
					containerName = "Side1_Text_Move_For_Shrink";
				}else if(WhichSide == 2) {
					containerName = "Side2_Text_For_Shrink";
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$" + containerName + "$Select_HeaderTop*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
						"$Flag$img_Flag*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
						"$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" 
						+ WhichSide + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName1() + "\0", print_writers);
				
				if(player.getFirstname() != null && player.getSurname() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$" + containerName + "$Select_HeaderTop$Player_Name$txt_FirstName*GEOM*TEXT SET " + player.getFirstname() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$" + containerName + "$Select_HeaderTop$Player_Name$txt_SecondName*GEOM*TEXT SET " + player.getSurname() + "\0", print_writers);
				}else if(player.getFirstname() != null && player.getSurname() == null){
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$" + containerName + "$Select_HeaderTop$Player_Name$txt_FirstName*GEOM*TEXT SET " + player.getFirstname() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$" + containerName + "$Select_HeaderTop$Player_Name$txt_SecondName*ACTIVE SET 0 \0", print_writers);
					
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$" + containerName + "$Change$Bottom*ACTIVE SET 0 \0", print_writers);
					
				break;

			case "Control_F8":

				ground = Grounds.stream().filter(grnd -> grnd.getFullname().contains(matchAllData.getSetup().getVenueName())).findAny().orElse(null);
				if(ground == null) {
					return "PopulateFfHeader: ground name is NULL";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Flag*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
					"$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamGroup() + ", " + ground.getCity() 
					+ "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
					"$" + containerName + "$Change$Bottom*ACTIVE SET 1 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
					"$Select_HeaderTop$Big_First$txt_Team_1*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamName1() +"\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
					"$Select_HeaderTop$Big_First$txt_Team_2*GEOM*TEXT SET " + matchAllData.getSetup().getAwayTeam().getTeamName1() +"\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
					"$Select_HeaderTop$Small_First$txt_Team_1*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamName1() +"\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
					"$Select_HeaderTop$Small_First$txt_Team_2*GEOM*TEXT SET " + matchAllData.getSetup().getAwayTeam().getTeamName1() +"\0", print_writers);
				
				if(team.getTeamId() == matchAllData.getSetup().getHomeTeamId()) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" 
						+ WhichSide + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + 
						matchAllData.getSetup().getHomeTeam().getTeamName1() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side"+ WhichSide + 
						"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" 
						+ WhichSide + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + 
						matchAllData.getSetup().getAwayTeam().getTeamName1() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side"+ WhichSide + 
						"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				}
				break;
				
			case "Shift_F11": //MATCH SUMMARY
				
				ground = Grounds.stream().filter(grnd -> grnd.getFullname().contains(matchAllData.getSetup().getVenueName())).findAny().orElse(null);
				if(ground == null) {
					return "PopulateFfHeader: ground name is NULL";
				}
				
				if(WhichSide == 1) {
					containerName = "Side1_Text_Move_For_Shrink";
				}else if(WhichSide == 2) {
					containerName = "Side2_Text_For_Shrink";
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Flag*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Shadow*ACTIVE SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$" + containerName + "$Select_HeaderTop*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$" + containerName + "$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "MATCH SUMMARY"+ "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$" + containerName + "$Change$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamGroup()
						+ ", " + ground.getCity() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$" + containerName + "$Change$Bottom*ACTIVE SET 1 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$MoveForExtraData$Overall_Scaling$Header"
						+ "$Off$Bottom_Align$Header_Extra$loop$txt_Extra*GEOM*TEXT SET " + "" +"\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$MoveForExtraData$Overall_Scaling$Header"
						+ "$Off$Bottom_Align$Header_Extra$loop$txt_Extra2*GEOM*TEXT SET " +  "" +"\0", print_writers);
				
				break;
				
			case "Shift_K":
				
				if(WhichSide == 1) {
					containerName = "Side1_Text_Move_For_Shrink";
				}else if(WhichSide == 2) {
					containerName = "Side2_Text_For_Shrink";
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
						"$Flag$img_Flag*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$" + containerName + "$Select_HeaderTop*FUNCTION*Omo*vis_con SET 3 \0", print_writers);

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$" + containerName + "$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "CURRENT PARTNERSHIP"+ "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$" + containerName + "$Change$Bottom$txt_Subheader*GEOM*TEXT SET " +  inning.getBatting_team().getTeamName1() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$" + containerName + "$Change$Bottom*ACTIVE SET 1 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName1() + "\0", print_writers);
				break;
				
			case "m": case "Control_F7": //MATCH ID

				ground = Grounds.stream().filter(grnd -> grnd.getFullname().contains(matchAllData.getSetup().getVenueName())).findAny().orElse(null);
				if(ground == null) {
					return "PopulateFfHeader: ground name is NULL";
				}
				
				if(WhichSide == 1) {
					containerName = "Side1_Text_Move_For_Shrink";
				}else if(WhichSide == 2) {
					containerName = "Side2_Text_For_Shrink";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Flag*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Shadow*ACTIVE SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$" + containerName + "$Select_HeaderTop*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
				
				switch(whatToProcess.split(",")[0]) {
				case "m":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$" + containerName + "$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + matchAllData.getSetup().getMatchIdent() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$" + containerName + "$Change$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getTournament() + "\0", print_writers);
					break;
				case "Control_F7":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$" + containerName + "$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "TEAMS" + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$" + containerName + "$Change$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamGroup() + 
							", " + ground.getCity() + "\0", print_writers);
					break;
				}

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$" + containerName + "$Change$Bottom*ACTIVE SET 1 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$MoveForExtraData$Overall_Scaling$Header"
						+ "$Off$Bottom_Align$Header_Extra$loop$txt_Extra*GEOM*TEXT SET " + "" +"\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$MoveForExtraData$Overall_Scaling$Header"
						+ "$Off$Bottom_Align$Header_Extra$loop$txt_Extra2*GEOM*TEXT SET " +  "" +"\0", print_writers);
				break;
				
			case "Control_m": //MATCH PROMO
				
				if(WhichSide == 1) {
					containerName = "Side1_Text_Move_For_Shrink";
				}else if(WhichSide == 2) {
					containerName = "Side2_Text_For_Shrink";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Flag*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Shadow*ACTIVE SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$" + containerName + "$Select_HeaderTop*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$" + containerName + "$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + fixture.getTeamgroup().toUpperCase()+ "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$" + containerName + "$Change$Bottom$txt_Subheader*GEOM*TEXT SET " +  matchAllData.getSetup().getTournament() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$" + containerName + "$Change$Bottom*ACTIVE SET 1 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$MoveForExtraData$Overall_Scaling$Header"
						+ "$Off$Bottom_Align$Header_Extra$loop$txt_Extra*GEOM*TEXT SET " + "" +"\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$MoveForExtraData$Overall_Scaling$Header"
						+ "$Off$Bottom_Align$Header_Extra$loop$txt_Extra2*GEOM*TEXT SET " +  "" +"\0", print_writers);
				break;
			}
			break;
		}
		return Constants.OK;
	}
	public String PopulateFfBody(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException 
	{
		String how_out_txt = "";
		switch (whatToProcess) {
		case "F1": // Scorecard
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:

				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning).findAny().orElse(null);
				if(inning == null) {
					return "PopulateFfBody: Inning return is NULL";
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
					+ "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
				
				for(int iRow = 1; iRow <= inning.getBattingCard().size(); iRow++) {
					switch (inning.getBattingCard().get(iRow-1).getStatus().toUpperCase()) {
					case CricketUtil.STILL_TO_BAT:
						
						if(inning.getBattingCard().get(iRow-1).getHowOut() == null) {

							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Still_To_Bat$Data$Name$txt_BatterName*GEOM*TEXT SET "
								+ inning.getBattingCard().get(iRow-1).getPlayer().getTicker_name().toUpperCase() + "\0", print_writers);
							
						} else {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$Name$txt_BatterName*GEOM*TEXT SET "
								+ inning.getBattingCard().get(iRow-1).getPlayer().getTicker_name().toUpperCase() + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$How_Out_1$txt_OutType*GEOM*TEXT SET "
								+ inning.getBattingCard().get(iRow-1).getHowOut().replace("_", " ").toLowerCase() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$How_Out_1$txt_FielderName*GEOM*TEXT SET \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$How_Out_2$txt_Bold*GEOM*TEXT SET \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$How_Out_2$txt_BowlerName*GEOM*TEXT SET \0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$Runs$txt_Runs*GEOM*TEXT SET " 
								+ inning.getBattingCard().get(iRow-1).getRuns() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$Balls$txt_Balls*GEOM*TEXT SET " 
								+ String.valueOf(inning.getBattingCard().get(iRow-1).getBalls()) + "\0", print_writers);

						}
						break;
						
					default:
						
						switch (inning.getBattingCard().get(iRow-1).getStatus().toUpperCase()) {
						case CricketUtil.OUT:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							containerName = "Out";
							break;
						case CricketUtil.NOT_OUT:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
							containerName = "Not_Out";
							break;
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + containerName + "$Data$Name$txt_BatterName*GEOM*TEXT SET "
								+ inning.getBattingCard().get(iRow-1).getPlayer().getTicker_name().toUpperCase() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + containerName + "$Data$Runs$txt_Runs*GEOM*TEXT SET " 
								+ inning.getBattingCard().get(iRow-1).getRuns() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + containerName + "$Data$Balls$txt_Balls*GEOM*TEXT SET " 
								+ String.valueOf(inning.getBattingCard().get(iRow-1).getBalls()) + "\0", print_writers);
						
						how_out_txt = CricketFunctions.processHowOutText("FOUR-PART-HOW-OUT", inning.getBattingCard().get(iRow-1));
						
						if(how_out_txt != null && how_out_txt.split("|").length >= 4) {
							switch (inning.getBattingCard().get(iRow-1).getStatus().toUpperCase()) {
							case CricketUtil.OUT:
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + containerName + 
									"$Data$How_Out_1$txt_OutType*GEOM*TEXT SET " + how_out_txt.split("\\|")[0].trim() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + containerName 
									+ "$Data$How_Out_2$txt_Bold*GEOM*TEXT SET " + how_out_txt.split("\\|")[2].trim() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + containerName 
									+ "$Data$How_Out_2$txt_BowlerName*GEOM*TEXT SET " + how_out_txt.split("\\|")[3].trim() + "\0", print_writers);
								break;
							}
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + containerName 
								+ "$Data$How_Out_1$txt_FielderName*GEOM*TEXT SET " + how_out_txt.split("\\|")[1].trim() + "\0", print_writers);
						}else {
							switch (inning.getBattingCard().get(iRow-1).getStatus().toUpperCase()) {
							case CricketUtil.OUT:
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + containerName + 
									"$Data$How_Out_1$txt_OutType*GEOM*TEXT SET " + "" + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + containerName 
									+ "$Data$How_Out_2$txt_Bold*GEOM*TEXT SET " + "" + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + containerName 
									+ "$Data$How_Out_2$txt_BowlerName*GEOM*TEXT SET " + "" + "\0", print_writers);
								break;
							}
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + containerName 
								+ "$Data$How_Out_1$txt_FielderName*GEOM*TEXT SET " + "NOT OUT" + "\0", print_writers);
						}
						break;
					}
				}
				break;
			}
			break;
		
		case "F2": //Bowling Card
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$1"
						+ "$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				
				for(int j=1;j<=10;j++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
							+ (j+1) + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$10"
							+ "$Select_Row_Type$FOW$Data$fig_" + j + "*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$11"
							+ "$Select_Row_Type$Score$Data$fig_" + j + "*GEOM*TEXT SET " + "" + "\0", print_writers);
					
				}
				
				if(inning.getBowlingCard() != null && inning.getBowlingCard().size() > 0) {
					for(int iRow = 1; iRow <= inning.getBowlingCard().size(); iRow++) {
						if(inning.getBowlingCard().get(iRow-1).getRuns() > 0 || 
								((inning.getBowlingCard().get(iRow-1).getOvers()*6)+inning.getBowlingCard().get(iRow-1).getBalls()) > 0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
								+ (iRow+1) + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
								+ (iRow+1) + "$Select_Row_Type$Players$Data$txt_Name*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getPlayer().getTicker_name() + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
									+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Overs*GEOM*TEXT SET " + CricketFunctions.
									OverBalls(inning.getBowlingCard().get(iRow-1).getOvers(), inning.getBowlingCard().get(iRow-1).getBalls()) + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
									+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Maidens*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getMaidens() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
									+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Runs*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getRuns() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
									+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Wickets*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getWickets() + "\0", print_writers);
							
							if(inning.getBowlingCard().get(iRow-1).getEconomyRate() != null) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
										+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Economy*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getEconomyRate() + "\0", print_writers);
							}else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
										+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Economy*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getPlayer().getTicker_name() + "\0", print_writers);
							}
						}
					}
				}
				
				if(inning.getBowlingCard().size() <= 8) {
					if(inning.getFallsOfWickets() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$10"
								+ "$Select_Row_Type*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$11"
								+ "$Select_Row_Type*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
						
						for(FallOfWicket fow : inning.getFallsOfWickets()) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
								+ "10$Select_Row_Type$FOW$Data$fig_" + fow.getFowNumber() + "*GEOM*TEXT SET " + fow.getFowNumber() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
								+ "11$Select_Row_Type$Score$Data$fig_" + fow.getFowNumber() + "*GEOM*TEXT SET " + fow.getFowRuns() + "\0", print_writers);
						}
					}
				}
				break;
			}
			break;
			
		case "Shift_F11": //Match Summary
			
			String teamname = "", teamFlag = ""; 
			int rowId = 0;
			
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning).findAny().orElse(null);
			if(inning == null) {
				return "PopulateFfBody: Inning return is NULL";
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 5 \0", print_writers);
			for(int i=1; i<=2; i++ ) {
				for(int j=1; j<=4; j++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_"
							+ i + "$Row_" + j + "$Batsman*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_"
							+ i + "$Row_" + j + "$Bowler*ACTIVE SET 0 \0", print_writers);
				}
			}
			
			for(int i = 1; i <= WhichInning ; i++) {
				if(i == 1) {
					rowId=0;
					if(matchAllData.getMatch().getInning().get(i-1).getBattingTeamId() == matchAllData.getSetup().getTossWinningTeam()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
								+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
								+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 0 \0", print_writers);							}
					
				} else {
					rowId=0;
					if(matchAllData.getMatch().getInning().get(i-1).getBattingTeamId() == matchAllData.getSetup().getTossWinningTeam()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
								+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
								+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					}
				}
				
				if(matchAllData.getMatch().getInning().get(i-1).getBattingTeamId() == matchAllData.getSetup().getHomeTeamId()) {
					teamname = matchAllData.getSetup().getHomeTeam().getTeamName1();
					teamFlag = matchAllData.getSetup().getHomeTeam().getTeamName1();
				} else {
					teamname = matchAllData.getSetup().getAwayTeam().getTeamName1();
					teamFlag = matchAllData.getSetup().getAwayTeam().getTeamName1();
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
						+ i +"$Tittle$Data$txt_Team*GEOM*TEXT SET " + teamname.toUpperCase() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
						+ i +"$Tittle$FlagGrp$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  teamFlag + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
						+ i +"$Tittle$Data$Overs$txt_Overs_Value*GEOM*TEXT SET " + CricketFunctions.OverBalls(matchAllData.getMatch().getInning().get(i-1).
								getTotalOvers(),matchAllData.getMatch().getInning().get(i-1).getTotalBalls()) + " \0", print_writers);
				
				if(matchAllData.getMatch().getInning().get(i-1).getTotalWickets() >= 10) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
							+ i +"$Tittle$Data$txt_Score*GEOM*TEXT SET " + matchAllData.getMatch().getInning().get(i-1).getTotalRuns() + " \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
							+ i +"$Tittle$Data$txt_Score*GEOM*TEXT SET " + matchAllData.getMatch().getInning().get(i-1).getTotalRuns() 
							+ " - " + String.valueOf(matchAllData.getMatch().getInning().get(i-1).getTotalWickets()) + " \0", print_writers);
				}
				
				if(matchAllData.getMatch().getInning().get(i-1).getBattingCard() != null) {
					Collections.sort(matchAllData.getMatch().getInning().get(i-1).getBattingCard(),new CricketFunctions.BatsmenScoreComparator());
					
					for(BattingCard bc : matchAllData.getMatch().getInning().get(i-1).getBattingCard()) {
						if(bc.getRuns() > 0) {
							// DJ to check retired/absent hurt 
							if(!bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
								rowId++;
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i +"$Row_"+rowId+"$Batsman*ACTIVE SET 1 \0", print_writers);

								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
										"$Batsman$txt_Name*GEOM*TEXT SET " + bc.getPlayer().getTicker_name() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
										"$Batsman$txt_Runs*GEOM*TEXT SET " + bc.getRuns() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
										"$Batsman$txt_Balls*GEOM*TEXT SET " + String.valueOf(bc.getBalls()) + "\0", print_writers);

								if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
											"$Batsman$txt_Not-Out*GEOM*TEXT SET " + "*" + "\0", print_writers);
								} else {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
											"$Batsman$txt_Not-Out*GEOM*TEXT SET " + "" + "\0", print_writers);
								}
								if(i == 1 && rowId >= 4) {
									break;
								}else if(i == 2 && rowId >= 4) {
									break;
								}
							}
						}
					}
				}	
				if(i == 1) {
					rowId = 0;
				}
				else {
					rowId = 0;
				}
				
				if(matchAllData.getMatch().getInning().get(i-1).getBowlingCard() != null) {
					
					Collections.sort(matchAllData.getMatch().getInning().get(i-1).getBowlingCard(),new CricketFunctions.BowlerFiguresComparator());

					for(BowlingCard boc : matchAllData.getMatch().getInning().get(i-1).getBowlingCard()) {
						
						if(boc.getWickets() > 0) {
							rowId++;
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i +"$Row_"+rowId+"$Bowler*ACTIVE SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
									"$Bowler$txt_Name*GEOM*TEXT SET " + boc.getPlayer().getTicker_name() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
									"$Bowler$txt_Figs*GEOM*TEXT SET " + boc.getWickets() + "-" + boc.getRuns() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
									"$Bowler$txt_Overs*GEOM*TEXT SET " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + "\0", print_writers);
							
							if(i == 1 && rowId >= 4) {
								break;
							}
							else if(i == 2 && rowId >= 4) {
								break;
							}
						}
					}
				}
			}
			break;
			
		case "F4":
			
			int row_id=0, omo_num=0,Top_Score = 50;
			double Mult = 160,ScaleFac1 = 0, ScaleFac2 = 0;
			String Left_Batsman="", Right_Batsman="", cont_name="";
			
			inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning)
					.findAny().orElse(null);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 6 \0", print_writers);
			
			for(int a = 1; a <= inning.getPartnerships().size(); a++){
				ScaleFac1=0;ScaleFac2=0;
				if(inning.getPartnerships().get(a-1).getFirstBatterRuns() > Top_Score) {
					Top_Score = inning.getPartnerships().get(a-1).getFirstBatterRuns();
				}
				if(inning.getPartnerships().get(a-1).getSecondBatterRuns() > Top_Score) {
					Top_Score = inning.getPartnerships().get(a-1).getSecondBatterRuns();
				}
			}
			
			for (Partnership ps : inning.getPartnerships()) {
				row_id = row_id + 1;
				Left_Batsman ="" ; Right_Batsman="";
				for (BattingCard bc : inning.getBattingCard()) {
					if(bc.getPlayerId() == ps.getFirstBatterNo()) {
						Left_Batsman = bc.getPlayer().getTicker_name().toUpperCase();
					}
					else if(bc.getPlayerId() == ps.getSecondBatterNo()) {
						Right_Batsman = bc.getPlayer().getTicker_name().toUpperCase();
					}
				}
				if(inning.getPartnerships().size() >= 10) {
					if(ps.getPartnershipNumber()<=inning.getPartnerships().size()) {
						omo_num = 2;
						cont_name = "$Out";
					}
				}
				else {
					if(ps.getPartnershipNumber() < inning.getPartnerships().size()) {
						omo_num = 2;
						cont_name = "$Out";
					}
					else if(ps.getPartnershipNumber() >= inning.getPartnerships().size()) {
						omo_num = 3;
						cont_name = "$Not_Out";
					}
				}
				
				ScaleFac1 = ((ps.getFirstBatterRuns())*(Mult/Top_Score)) ;
				ScaleFac2 = ((ps.getSecondBatterRuns())*(Mult/Top_Score)) ;
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
						+ row_id+  "$Select_Row_Type*FUNCTION*Omo*vis_con SET " + omo_num + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
						+ row_id + "$Select_Row_Type" + cont_name +"$Data$txt_Name_1*GEOM*TEXT SET " + Left_Batsman + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
						+ row_id + "$Select_Row_Type" + cont_name +"$Data$txt_Name_2*GEOM*TEXT SET " + Right_Batsman + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
						+ row_id + "$Select_Row_Type" + cont_name +"$Data$txt_Runs*GEOM*TEXT SET " + ps.getTotalRuns() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
						+ row_id + "$Select_Row_Type" + cont_name +"$Data$txt_Balls*GEOM*TEXT SET " + ps.getTotalBalls() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
						+ row_id  + "$Select_Row_Type" + cont_name + "$Geom_Bar_1*GEOM*width SET " + ScaleFac1 + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
						+ row_id  + "$Select_Row_Type" + cont_name + "$Geom_Bar_2*GEOM*width SET " + ScaleFac2 + "\0", print_writers);
			}
			
			if(inning.getPartnerships().size() >= 10) {
				row_id = row_id + 1;
				
			}else {
				for (BattingCard bc : inning.getBattingCard()) {
					if(row_id < inning.getBattingCard().size()) {
						if(row_id == inning.getPartnerships().size()) {
							row_id = row_id + 1;
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
								+ row_id+  "$Select_Row_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
							
							if(matchAllData.getMatch().getInning().get(WhichInning - 1).getTotalOvers() == matchAllData.getSetup().getMaxOvers() 
									|| matchAllData.getMatch().getInning().get(WhichInning - 1).getTotalWickets() >= 10 ) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
									+ row_id+  "$Select_Row_Type$Title$Data$txt_Name*GEOM*TEXT SET " + "DID NOT BAT" + "\0", print_writers);
							}
							else if(matchAllData.getSetup().getTargetOvers() != null && !matchAllData.getSetup().getTargetOvers().isEmpty()) {
								if( matchAllData.getMatch().getInning().get(WhichInning - 1).getTotalOvers() == Integer.valueOf(matchAllData.getSetup().getTargetOvers()) 
										|| matchAllData.getMatch().getInning().get(WhichInning - 1).getTotalWickets() >= 10) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
										+ row_id+  "$Select_Row_Type$Title$Data$txt_Name*GEOM*TEXT SET " + "DID NOT BAT" + "\0", print_writers);
								}else if(CricketFunctions.getRequiredRuns(matchAllData) <= 0 || CricketFunctions.getRequiredBalls(matchAllData) <= 0 
										|| CricketFunctions.getWicketsLeft(matchAllData) <= 0) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
										+ row_id+  "$Select_Row_Type$Title$Data$txt_Name*GEOM*TEXT SET " + "DID NOT BAT" + "\0", print_writers);
								}else {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
										+ row_id+  "$Select_Row_Type$Title$Data$txt_Name*GEOM*TEXT SET " + "STILL TO BAT" + "\0", print_writers);
								}
							}
							else if(CricketFunctions.getRequiredRuns(matchAllData) <= 0 || CricketFunctions.getRequiredBalls(matchAllData) <= 0 || 
									CricketFunctions.getWicketsLeft(matchAllData) <= 0) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
									+ row_id+  "$Select_Row_Type$Title$Data$txt_Name*GEOM*TEXT SET " + "DID NOT BAT" + "\0", print_writers);
							}
							else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
									+ row_id+  "$Select_Row_Type$Title$Data$txt_Name*GEOM*TEXT SET " + "STILL TO BAT" + "\0", print_writers);
							}
						}
						else if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
							row_id = row_id + 1;
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
								+ row_id+  "$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
								+ row_id+  "$Select_Row_Type$Still_To_Bat$Data$txt_Name*GEOM*TEXT SET " + bc.getPlayer().getTicker_name() + "\0", print_writers);
						}	
					}
					else {
						break;
					}
				}
			}
			break;
		
		case "Control_d":  
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				double average = stat.getRuns()/(stat.getMatches()-stat.getNot_out());
				double strikeRate = (stat.getRuns()/stat.getBalls_faced())*100;

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
						+ "$SubTitle$Data$txt_SubTitle*GEOM*TEXT SET " + "CAREER" + "\0", print_writers);
				if(stat.getMatches()==0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$1$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$1$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + stat.getMatches() + "\0", print_writers);
				}
				if(stat.getRuns()==0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$1$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$1$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + stat.getRuns() + "\0", print_writers);
				}
				if(stat.getFifties()==0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$2$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$2$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + stat.getFifties() + "\0", print_writers);
				}
				if(stat.getHundreds() == 0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$2$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$2$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + stat.getHundreds() + "\0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
						+ "$Stats$3$Data$Stat_1$Normal$txt_Desig*GEOM*TEXT SET " + "AVERAGE" + "\0", print_writers);
				if(average==0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$3$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$3$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + average + "\0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
						+ "$Stats$3$Data$Stat_2$Normal$txt_Desig*GEOM*TEXT SET " + "STRIKE RATE" + "\0", print_writers);
				if(strikeRate ==0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$3$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$3$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + strikeRate + "\0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
						+ "$Stats$4$Data$Stat_1$Normal$txt_Desig*GEOM*TEXT SET " + "BEST SCORE" + "\0", print_writers);
				if(stat.getBest_score()==null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_Fig*GEOM*TEXT SET " + "" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_Fig*GEOM*TEXT SET " + stat.getBest_score() + "\0", print_writers);
				}
				
				if(stat.getBest_score_against()==null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_VersusTeam*GEOM*TEXT SET " + "" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_VersusTeam*GEOM*TEXT SET " + "v "+ stat.getBest_score_against().toUpperCase() + "\0", print_writers);
				}
				if(stat.getBest_score_venue()==null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_VersusLOcation*GEOM*TEXT SET " + "" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_VersusLOcation*GEOM*TEXT SET " + "AT "+stat.getBest_score_venue() + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$PhotoPart"
						+ "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName1() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$PhotoPart"
						+ "$Photo$img_PlayerPhoto*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + team.getTeamName4() + 
						Constants.RIGHT_2048 + player.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
				break;
			}
			break;
			
		case "Control_e":
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				double economy_rate;
				double average = stat.getRuns_conceded()/stat.getWickets();
				DecimalFormat df = new DecimalFormat("0.00");

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
						+ "$SubTitle$Data$txt_SubTitle*GEOM*TEXT SET " + "CAREER" + "\0", print_writers);
				if(stat.getMatches()==0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$1$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$1$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + stat.getMatches() + "\0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
						+ "$Stats$1$Data$Stat_2$Normal$txt_Desig*GEOM*TEXT SET " + "WICKETS" + "\0", print_writers);
				if(stat.getWickets()==0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$1$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$1$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + stat.getWickets() + "\0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
						+ "$Stats$2$Data$Stat_1$Normal$txt_Desig*GEOM*TEXT SET " + "3WI" + "\0", print_writers);
				if(stat.getPlus_3()==0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$2$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$2$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + stat.getPlus_3() + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
						+ "$Stats$2$Data$Stat_2$Normal$txt_Desig*GEOM*TEXT SET " + "5WI" + "\0", print_writers);
				if(stat.getPlus_5() == 0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$2$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$2$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + stat.getPlus_5() + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
						+ "$Stats$3$Data$Stat_1$Normal$txt_Desig*GEOM*TEXT SET " + "AVERAGE" + "\0", print_writers);
				if(average==0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$3$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$3$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + df.format(average) + "\0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
						+ "$Stats$3$Data$Stat_2$Normal$txt_Desig*GEOM*TEXT SET " + "ECONOMY" + "\0", print_writers);
				if(stat.getRuns_conceded() == 0 && stat.getBalls_bowled() == 0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$3$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
				}else {
					economy_rate = stat.getRuns_conceded() / stat.getBalls_bowled();
					economy_rate = economy_rate * 6;
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$3$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + df.format(economy_rate) + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
						+ "$Stats$4$Data$Stat_1$Normal$txt_Desig*GEOM*TEXT SET " + "BEST FIGURES" + "\0", print_writers);
				
				if(stat.getBest_figures()==null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_Fig*GEOM*TEXT SET " + "" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_Fig*GEOM*TEXT SET " + stat.getBest_figures() + "\0", print_writers);
				}
				
				if(stat.getBest_figures_against()==null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_VersusTeam*GEOM*TEXT SET " + "" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_VersusTeam*GEOM*TEXT SET " + "v "+ stat.getBest_figures_against().toUpperCase() + "\0", print_writers);
				}
				
				if(stat.getBest_figures_venue()==null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_VersusLOcation*GEOM*TEXT SET " + "" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
							+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_VersusLOcation*GEOM*TEXT SET " + "AT "+stat.getBest_figures_venue() + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$PhotoPart"
						+ "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName1() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$PhotoPart"
						+ "$Photo$img_PlayerPhoto*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + team.getTeamName4() + Constants.RIGHT_2048 + 
							player.getPhoto() + CricketUtil.PNG_EXTENSION  + "\0", print_writers);
				break;
			}
			break;

		case "Control_F7": //Double Teams

			int row_no = 0;
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 9 \0", print_writers);
				
				for(int i = 1; i <= 2 ; i++) {
					if(i == 1) {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_1$Tittle"
								+ "$txt_Tittle*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamName1().toUpperCase() + "\0", print_writers);
						
						for(Player hs : matchAllData.getSetup().getHomeSquad()) {
							row_no = row_no + 1;
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_1$" + 
									row_no + "$txt_Name*GEOM*TEXT SET " + hs.getFull_name() + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_1$" + 
									row_no + "$txt_Description*GEOM*TEXT SET " + "" + "\0", print_writers);
							
							if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_1$" + 
										row_no + "$txt_Description*GEOM*TEXT SET " + "(C)" + "\0", print_writers);
							}
							else if(hs.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_1$" + 
										row_no + "$txt_Description*GEOM*TEXT SET " + "(C & WK)" + "\0", print_writers);
							}
							else if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_1$" + 
										row_no + "$txt_Description*GEOM*TEXT SET " + "(WK)" + "\0", print_writers);
							}
						}
						
						
					} else {
						row_no = 0;
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_2$Tittle"
								+ "$txt_Tittle*GEOM*TEXT SET " + matchAllData.getSetup().getAwayTeam().getTeamName1().toUpperCase() + "\0", print_writers);
						
						for(Player as : matchAllData.getSetup().getAwaySquad()) {
							row_no = row_no + 1;
							
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_2$" + 
									row_no + "$txt_Name*GEOM*TEXT SET " + as.getFull_name() + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_2$" + 
									row_no + "$txt_Description*GEOM*TEXT SET " + "" + "\0", print_writers);
							
							if(as.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_2$" + 
										row_no + "$txt_Description*GEOM*TEXT SET " + "(C)" + "\0", print_writers);
							}
							else if(as.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_2$" + 
										row_no + "$txt_Description*GEOM*TEXT SET " + "(C & WK)" + "\0", print_writers);
							}
							else if(as.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_2$" + 
										row_no + "$txt_Description*GEOM*TEXT SET " + "(WK)" + "\0", print_writers);
							}
							
						}
					}
				}
				break;
			}
			break;
			
		case "Control_F8":
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				int player_no=0;
				for(int i=1;i<=PlayingXI.size();i++) {
					switch(i) {
					case 1: case 2: case 3: case 4: case 5: case 6:
						player_no = i;
						containerName = "$Top";
						break;
					case 7: case 8: case 9: case 10: case 11:
						player_no = i - 6;
						containerName = "$Bottom";
						break;
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
							+ "$Photo_" + player_no + "$Gradient$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName1() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
							+ "$Photo_" + player_no + "$Gradient$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName1() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
							+ "$Photo_" + player_no + "$img_Photo*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + team.getTeamName4() + 
								Constants.CENTRE_512 + PlayingXI.get(i-1).getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
							+ "$Photo_" + player_no + "$Name$txt_PlayerName*GEOM*TEXT SET " + PlayingXI.get(i-1).getTicker_name() + "\0", print_writers);
					
					if(PlayingXI.get(i-1).getRole().equalsIgnoreCase(CricketUtil.BATSMAN) || PlayingXI.get(i-1).getRole().equalsIgnoreCase("BAT/KEEPER")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + player_no + "$Select_Role*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + player_no + "$Select_Role$Single_Style1$txt_Bat*GEOM*TEXT SET " + "BAT" + "\0", print_writers);
					}
					else if(PlayingXI.get(i-1).getRole().equalsIgnoreCase(CricketUtil.BOWLER)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + player_no + "$Select_Role*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + player_no + "$Select_Role$Single_Style2$txt_Bowl*GEOM*TEXT SET " + 
								CricketFunctions.getBowlerType(PlayingXI.get(i-1).getBowlingStyle()).replace("PACE", "SEAM") + "\0", print_writers);
					}
					else if(PlayingXI.get(i-1).getRole().equalsIgnoreCase("ALL-ROUNDER")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + player_no + "$Select_Role*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + player_no + "$Select_Role$Double_Style$txt_Bat*GEOM*TEXT SET " + "BAT" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + player_no + "$Select_Role$Double_Style$txt_Bowl*GEOM*TEXT SET " + 
								CricketFunctions.getBowlerType(PlayingXI.get(i-1).getBowlingStyle()).replace("PACE", "SEAM") + "\0", print_writers);
					}
					
					if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)){
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + player_no + "$Select_Captain*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}
					else if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + player_no + "$Select_Captain*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + player_no + "$Select_Role*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + player_no + "$Select_Role$Single_Style2$txt_Bowl*GEOM*TEXT SET " + "KEEPER" + "\0", print_writers);
					}
					else if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + player_no + "$Select_Captain*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + player_no + "$Select_Role*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + player_no + "$Select_Role$Single_Style2$txt_Bowl*GEOM*TEXT SET " + "KEEPER" + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + player_no + "$Select_Captain*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					}
				}
				break;
			}
			break;
		
		case "Shift_K":
			
			String first_batter_run="",first_batter_ball="",second_batter_run="",second_batter_ball="",right_bat="", left_bat="",left_bat_photo="", right_bat_photo="";
			int total_runs = 0, total_balls = 0;
			for(Inning inn : matchAllData.getMatch().getInning()) {
				if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
					for(BattingCard bc : inn.getBattingCard()) {
						if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
							if(bc.getPlayerId()==inn.getPartnerships().get(inning.getPartnerships().size() - 1).getFirstBatterNo()) {
								left_bat = bc.getPlayer().getTicker_name();
								left_bat_photo = bc.getPlayer().getPhoto();
								first_batter_run = String.valueOf(inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterRuns());
								first_batter_ball = String.valueOf(inn.getPartnerships().get(inn.getPartnerships().size() - 1).getFirstBatterBalls());
							}
							
							if(bc.getPlayerId()==inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterNo()) {
								right_bat = bc.getPlayer().getTicker_name();
								right_bat_photo = bc.getPlayer().getPhoto();
								second_batter_run = String.valueOf(inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterRuns());
								second_batter_ball = String.valueOf(inn.getPartnerships().get(inn.getPartnerships().size() - 1).getSecondBatterBalls());
							}
							
						}
					}
					total_runs = inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalRuns();
					total_balls =inn.getPartnerships().get(inn.getPartnerships().size() - 1).getTotalBalls();
				}
			}

			
			double strikeRate = (total_runs/total_balls)*100;
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 7 \0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership$1"
					+ "$Data$txt_Runs*GEOM*TEXT SET " +total_runs+ "\0", print_writers);

			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership$1"
					+ "$Data$txt_Runs*GEOM*TEXT SET " +total_balls+ "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership$2"
					+ "$Data$fig_StrikeRate*GEOM*TEXT SET " +strikeRate+ "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership$4"
					+ "$Data$txt_Name*GEOM*TEXT SET " +left_bat+ "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership$4"
					+ "$Data$txt_Runs*GEOM*TEXT SET " +first_batter_run+ "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership$4"
					+ "$Data$txt_Ball*GEOM*TEXT SET " +first_batter_ball+ "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership$5"
					+ "$Data$txt_Name*GEOM*TEXT SET " +right_bat+ "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership$5"
					+ "$Data$txt_Runs*GEOM*TEXT SET " +second_batter_run+ "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership$5"
					+ "$Data$txt_Ball*GEOM*TEXT SET " +second_batter_ball+ "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership"
					+ "$Photo$PhotoGrp1$img_PlayerShadow1*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + inning.getBatting_team().getTeamName4() + Constants.LEFT_1024 + 
						left_bat_photo + CricketUtil.PNG_EXTENSION  + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership"
					+ "$Photo$PhotoGrp1$img_PlayerPhoto1*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + inning.getBatting_team().getTeamName4() + Constants.LEFT_1024 + 
						left_bat_photo + CricketUtil.PNG_EXTENSION  + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership"
					+ "$Photo$PhotoGrp2$img_PlayerShadow1*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + inning.getBatting_team().getTeamName4() + Constants.RIGHT_1024 + 
						right_bat_photo + CricketUtil.PNG_EXTENSION  + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership"
					+ "$Photo$PhotoGrp2$img_PlayerPhoto1*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + inning.getBatting_team().getTeamName4() + Constants.RIGHT_1024 + 
						right_bat_photo + CricketUtil.PNG_EXTENSION  + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership"
					+ "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName1() + "\0", print_writers);
			break;
		
		case "m": //MATCH Ident
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
						+ "$Data$Select_DataForPromo*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
						+ "$Data$txt_Team_1*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamName1().toUpperCase() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
						+ "$Data$txt_Team_2*GEOM*TEXT SET " + matchAllData.getSetup().getAwayTeam().getTeamName1().toUpperCase() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Flag_Top$"
						+ "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  matchAllData.getSetup().getHomeTeam().getTeamName1() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Flag_Bottom$"
						+ "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  matchAllData.getSetup().getAwayTeam().getTeamName1() + "\0", print_writers);
				break;
			}
			break;
			
		case "Control_m": //MATCH PROMO
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				String newDate = "",date_data = "";

				String dayOfWeek = new SimpleDateFormat("EEEE").format(new SimpleDateFormat("dd-MM-yyyy").parse(fixture.getDate()));
				String timeDetail="";
				
				newDate = fixture.getDate().split("-")[0];
				date_data = CricketFunctions.ordinal(Integer.valueOf(newDate)) + " " + Month.of(Integer.valueOf(fixture.getDate().split("-")[1])) + " 2024";
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
						+ "$Data$Select_DataForPromo*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
						+ "$Data$Select_DataForPromo*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
							+ "$Data$Select_DataForPromo$ExtraInfo$txt_Info1*GEOM*TEXT SET " + dayOfWeek.toUpperCase() + ", " +date_data + "\0", print_writers);
				
				if(fixture.getLocalTime() != null) {
					timeDetail = fixture.getLocalTime() + " LOCAL TIME ";
					if(fixture.getGmtTime() != null) {
						timeDetail = timeDetail + "(" + fixture.getGmtTime() + " GMT)";
					}
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
						+ "$Data$Select_DataForPromo$ExtraInfo$txt_Info2*GEOM*TEXT SET " + timeDetail + "\0", print_writers);
				
				for(Team tm : Teams) {
					if(tm.getTeamId() == fixture.getHometeamid()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
								+ "$Data$txt_Team_1*GEOM*TEXT SET "+ tm.getTeamName1() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Flag_Top$"
								+ "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  tm.getTeamName1() + "\0", print_writers);
					}
					
					if(tm.getTeamId() == fixture.getAwayteamid()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
								+ "$Data$txt_Team_2*GEOM*TEXT SET " + tm.getTeamName1() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Flag_Bottom$"
								+ "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  tm.getTeamName1() + "\0", print_writers);
					}
				}
				break;
			}
		}
		return Constants.OK;
	}
	public String PopulateFfFooter(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) 
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			switch (whatToProcess) {
			case "F1": case "F2": case "F4": case "Shift_K":
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning).findAny().orElse(null);
				if(inning != null) {
					return "PopulateFfFooter: Inning return is NULL";
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Select_FooterType*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Select_FooterType$Score$Data$Side"
						+ WhichSide + "$In_Out$Extras$txt_Extras_Value*GEOM*TEXT SET " + inning.getTotalExtras() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Select_FooterType$Score$Data$Side"
						+ WhichSide + "$In_Out$Overs$txt_Overs_Value*GEOM*TEXT SET " + 
						CricketFunctions.OverBalls(inning.getTotalOvers(),inning.getTotalBalls()) + "\0", print_writers);
				
				if(matchAllData.getSetup().getTargetOvers() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Select_FooterType$Score$Data$Side"
							+ WhichSide + "$In_Out$Overs$txt_Overs_2*GEOM*TEXT SET " + matchAllData.getSetup().getTargetOvers() + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Select_FooterType$Score$Data$Side"
							+ WhichSide + "$In_Out$Overs$txt_Overs_2*GEOM*TEXT SET " + "" + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Select_FooterType$Score$Data$Side"
						+ WhichSide + "$Score$txt_Score*GEOM*TEXT SET " + CricketFunctions.getTeamScore(inning, "-", false) + "\0", print_writers);
				break;
				
			 case "Shift_F11": //MATCH SUMMARY
				 
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				// DJ use proper match summary function
				if(matchAllData.getMatch().getMatchResult() != null) {
					if(matchAllData.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
								"$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.generateMatchSummaryStatus(WhichInning, matchAllData, "MIDDLE").toUpperCase() + "\0", print_writers);
					}
					else if(matchAllData.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
								"$txt_Info_1*GEOM*TEXT SET " + "MATCH TIED" + "\0", print_writers);
					}
					else if(matchAllData.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("ABANDONED")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
								"$txt_Info_1*GEOM*TEXT SET " + matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0", print_writers);
					}
					else if(matchAllData.getMatch().getMatchResult().split(",")[2].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
								"$txt_Info_1*GEOM*TEXT SET " +"MATCH TIED - " + CricketFunctions.generateMatchSummaryStatus(WhichInning, matchAllData, "MIDDLE").toUpperCase() + "\0", print_writers);
					}
					else {

						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
								"$txt_Info_1*GEOM*TEXT SET " +CricketFunctions.generateMatchSummaryStatus(WhichInning, matchAllData, "MIDDLE").toUpperCase() + "\0", print_writers);
					}
				}
				else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
							"$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.generateMatchSummaryStatus(WhichInning, matchAllData, "MIDDLE").toUpperCase() + "\0", print_writers);
					if(matchAllData.getSetup().getTargetType() != null) {
						if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("VJD")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
									"$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.generateMatchSummaryStatus(WhichInning, matchAllData, "MIDDLE").toUpperCase() + "\0", print_writers);
						}else if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("DLS")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
									"$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.generateMatchSummaryStatus(WhichInning, matchAllData, "MIDDLE").toUpperCase() + "\0", print_writers);
						}
					}
				}
				break;

			 case "Control_d": case "Control_e":
				 
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer*ACTIVE SET 0 \0", print_writers);
				break;

			 case "Control_F7": case "Control_F8": //PlayingXI
				 
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
						"$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.generateTossResult(matchAllData, CricketUtil.FULL, CricketUtil.FIELD, CricketUtil.FULL, 
								CricketUtil.CHOSE).toUpperCase() + "\0", print_writers);
				break;
			
			case "m": //MATCH ID

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Ident$Data$Side" + WhichSide + 
						"$txt_Info_1*GEOM*TEXT SET " + "LIVE FROM " + matchAllData.getSetup().getVenueName() + "\0", print_writers);
				break;

			case "Contro_m": //MATCH PROMO

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Ident$Data$Side" + WhichSide + 
						"$txt_Info_1*GEOM*TEXT SET " + "LIVE FROM " + fixture.getVenue() + "\0", print_writers);
				break;
			}
			break;
		}
		return Constants.OK;
	}
 
}