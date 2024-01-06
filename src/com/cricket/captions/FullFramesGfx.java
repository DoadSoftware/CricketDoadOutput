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
	public String WhichProfile;
	
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
		
	public boolean PopulateScorecardFF(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException
	{
		if(PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning) == true) {
			if(PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning) == true) {
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, WhichInning);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public boolean PopulateBowlingCardFF(int WhichSide,String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException
	{
		if(PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning) == true) {
			if(PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning) == true) {
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, WhichInning);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public boolean populateFFMatchId(int WhichSide, String whatToProcess, MatchAllData matchAllData) throws ParseException
	{
		if(PopulateFfHeader(WhichSide, whatToProcess, matchAllData, 0) == true) {
			if(PopulateFfBody(WhichSide, whatToProcess, matchAllData, 0) == true) {
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, 0);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public boolean populateMatchSummary(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException
	{
		if(PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning) == true) {
			if(PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning) == true) {
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, WhichInning);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public boolean populateFFMatchPromo(int WhichSide, String whatToProcess, MatchAllData matchAllData) throws NumberFormatException, ParseException
	{
		fixture = fixTures.stream().filter(fix -> fix.getMatchnumber() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
		
		if(PopulateFfHeader(WhichSide, whatToProcess.split(",")[0], matchAllData, 0) == true) {
			if(PopulateFfBody(WhichSide, whatToProcess.split(",")[0], matchAllData, 0) == true) {
				return PopulateFfFooter(WhichSide, whatToProcess.split(",")[0], matchAllData, 0);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public boolean populatePlayingXI(int WhichSide, String whatToProcess,int teamId,MatchAllData matchAllData, int WhichInning) throws ParseException
	{
		if(teamId == matchAllData.getSetup().getHomeTeamId()) {
			PlayingXI = matchAllData.getSetup().getHomeSquad();
		}else {
			PlayingXI = matchAllData.getSetup().getAwaySquad();
		}
		team = Teams.stream().filter(tm -> tm.getTeamId() == teamId).findAny().orElse(null);
		
		if(PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning) == true) {
			if(PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning) == true) {
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, WhichInning);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public boolean populatePartnership(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException {
		
		if(PopulateFfHeader(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning) == true) {
			if(PopulateFfBody(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning) == true) {
				return PopulateFfFooter(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public boolean populatePlayerProfile(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException {
		
		player = CricketFunctions.getPlayerFromMatchData(Integer.valueOf(whatToProcess.split(",")[2]), matchAllData);
		
		if(player == null) {
			return false;
		}
		
		stat = statistics.stream().filter(stat -> stat.getPlayer_id() == player.getPlayerId()).findAny().orElse(null);
		
		if(stat == null) {
			return false;
		}
		
		team = Teams.stream().filter(tm -> tm.getTeamId() == player.getTeamId()).findAny().orElse(null);
		
		if(team == null) {
			return false;
		}
		
		if(PopulateFfHeader(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning) == true) {
			if(PopulateFfBody(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning) == true) {
				return PopulateFfFooter(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public boolean PopulateDoubleTeams(int WhichSide, String whatToProcess,MatchAllData matchAllData) throws ParseException
	{
		if(PopulateFfHeader(WhichSide, whatToProcess, matchAllData,0) == true) {
			if(PopulateFfBody(WhichSide, whatToProcess, matchAllData, 0) == true) {
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, 0);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	public boolean populateCurrPartnership(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException
	{
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES))
				.findAny().orElse(null);
		if(PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning) == true) {
			if(PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning) == true) {
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, WhichInning);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean PopulateFfHeader(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) 
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return false;
		} else {
			String cout_name="",city_name = "";
			for(Ground ground : Grounds) {
				if(ground.getFullname().contains(matchAllData.getSetup().getVenueName())) {
					city_name = ground.getCity();
				}
			}
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				
				switch (whatToProcess) {
				case "F1": case "F2": case "F4": // Scorecard - BowlingCard
					inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning)
						.findAny().orElse(null);
					if(inning != null) {
						for(PrintWriter print_writer : print_writers) {
							
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
									"$Flag$img_Flag*ACTIVE SET 1 \0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
									"$Flag$img_Shadow*ACTIVE SET 1 \0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" + WhichSide + 
									"$In_Out$Change$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamGroup() + ", " + city_name + "\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
									"$In_Out$Change$" + cout_name + "$Change$Bottom*ACTIVE SET 1 \0");
							
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" + WhichSide + 
									"$In_Out$Change$Select_HeaderTop$Big_First$txt_Team_1*GEOM*TEXT SET " + 
									matchAllData.getMatch().getInning().get(0).getBatting_team().getTeamName1() +"\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" + WhichSide + 
									"$In_Out$Change$Select_HeaderTop$Big_First$txt_Team_2*GEOM*TEXT SET " +
									matchAllData.getMatch().getInning().get(0).getBowling_team().getTeamName1() +"\0");
							
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" + WhichSide + 
									"$In_Out$Change$Select_HeaderTop$Small_First$txt_Team_1*GEOM*TEXT SET " + 
									matchAllData.getMatch().getInning().get(0).getBatting_team().getTeamName1() +"\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" + WhichSide + 
									"$In_Out$Change$Select_HeaderTop$Small_First$txt_Team_2*GEOM*TEXT SET " + 
									matchAllData.getMatch().getInning().get(0).getBowling_team().getTeamName1() +"\0");
							
							switch(whatToProcess) {
							case "F1": case "F4":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" 
									+ WhichSide + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName4() + "\0");
									
								if(WhichInning == 1) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side"+ WhichSide + 
											"$In_Out$Change$Select_HeaderTop*FUNCTION*Omo*vis_con SET 1 \0");
								}else {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side"+ WhichSide + 
											"$In_Out$Change$Select_HeaderTop*FUNCTION*Omo*vis_con SET 2 \0");
								}
								break;
							case "F2":
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" 
									+ WhichSide + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() + "\0");
								
								if(WhichInning == 1) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side"+ WhichSide + 
											"$In_Out$Change$Select_HeaderTop*FUNCTION*Omo*vis_con SET 2 \0");
								}else {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side"+ WhichSide + 
											"$In_Out$Change$Select_HeaderTop*FUNCTION*Omo*vis_con SET 1 \0");
								}
								break;
							}
						}
					} else {
						return false;
					}
					break;
				case "Control_d": case "Control_e":
					if(WhichSide == 1) {
						cout_name = "Side1_Text_Move_For_Shrink";
					}else if(WhichSide == 2) {
						cout_name = "Side2_Text_For_Shrink";
					}
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$" + cout_name + "$Select_HeaderTop*FUNCTION*Omo*vis_con SET 0 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
								"$Flag$img_Flag*ACTIVE SET 1 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
								"$Flag$img_Shadow*ACTIVE SET 1 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" 
								+ WhichSide + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0");
						
						if(player.getFirstname() != null && player.getSurname() != null) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
									"$" + cout_name + "$Select_HeaderTop$Player_Name$txt_FirstName*GEOM*TEXT SET " + player.getFirstname() + "\0");
							
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
									"$" + cout_name + "$Select_HeaderTop$Player_Name$txt_SecondName*GEOM*TEXT SET " + player.getSurname() + "\0");
						}else if(player.getFirstname() != null && player.getSurname() == null){
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
									"$" + cout_name + "$Select_HeaderTop$Player_Name$txt_FirstName*GEOM*TEXT SET " + player.getFirstname() + "\0");
							
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
									"$" + cout_name + "$Select_HeaderTop$Player_Name$txt_SecondName*ACTIVE SET 0 \0");
							
						}
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Change$Bottom*ACTIVE SET 0 \0");
						
					}
					break;
				case "Control_F8":
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
								"$Flag$img_Flag*ACTIVE SET 1 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
								"$Flag$img_Shadow*ACTIVE SET 1 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamGroup() + ", " + city_name + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Change$Bottom*ACTIVE SET 1 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$Select_HeaderTop$Big_First$txt_Team_1*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamName1() +"\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$Select_HeaderTop$Big_First$txt_Team_2*GEOM*TEXT SET " + matchAllData.getSetup().getAwayTeam().getTeamName1() +"\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$Select_HeaderTop$Small_First$txt_Team_1*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamName1() +"\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$Select_HeaderTop$Small_First$txt_Team_2*GEOM*TEXT SET " + matchAllData.getSetup().getAwayTeam().getTeamName1() +"\0");
						
						if(team.getTeamId() == matchAllData.getSetup().getHomeTeamId()) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" 
									+ WhichSide + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + 
									matchAllData.getSetup().getHomeTeam().getTeamName4() + "\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side"+ WhichSide + 
									"$In_Out$Change$Select_HeaderTop*FUNCTION*Omo*vis_con SET 1 \0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" 
									+ WhichSide + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + 
									matchAllData.getSetup().getAwayTeam().getTeamName4() + "\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side"+ WhichSide + 
									"$In_Out$Change$Select_HeaderTop*FUNCTION*Omo*vis_con SET 2 \0");
						}
					}
					break;
				case "Shift_F11": //MATCH SUMMARY
					if(WhichSide == 1) {
						cout_name = "Side1_Text_Move_For_Shrink";
					}else if(WhichSide == 2) {
						cout_name = "Side2_Text_For_Shrink";
					}
					for(PrintWriter print_writer : print_writers) {

						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
							"$Flag$img_Flag*ACTIVE SET 0 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
							"$Flag$img_Shadow*ACTIVE SET 0 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Select_HeaderTop*FUNCTION*Omo*vis_con SET 3 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "MATCH SUMMARY"+ "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Change$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamGroup()
								+ ", " + city_name + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Change$Bottom*ACTIVE SET 1 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$MoveForExtraData$Overall_Scaling$Header"
								+ "$Off$Bottom_Align$Header_Extra$loop$txt_Extra*GEOM*TEXT SET " + "" +"\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$MoveForExtraData$Overall_Scaling$Header"
								+ "$Off$Bottom_Align$Header_Extra$loop$txt_Extra2*GEOM*TEXT SET " +  "" +"\0");
					}
					break;
					
				case "Shift_K":
					if(WhichSide == 1) {
						cout_name = "Side1_Text_Move_For_Shrink";
					}else if(WhichSide == 2) {
						cout_name = "Side2_Text_For_Shrink";
					}
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
								"$Flag$img_Flag*ACTIVE SET 1 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
							"$Flag$img_Shadow*ACTIVE SET 1 \0");

						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Select_HeaderTop*FUNCTION*Omo*vis_con SET 3 \0");

						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "CURRENT PARTNERSHIP"+ "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Change$Bottom$txt_Subheader*GEOM*TEXT SET " +  inning.getBatting_team().getTeamName1() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Change$Bottom*ACTIVE SET 1 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$HeaderDataAll$Side" + WhichSide + 
								"$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName4() + "\0");
					}
					break;
					
				case "m": case "Control_F7": //MATCH ID
					System.out.println("HELLO");
					if(WhichSide == 1) {
						cout_name = "Side1_Text_Move_For_Shrink";
					}else if(WhichSide == 2) {
						cout_name = "Side2_Text_For_Shrink";
					}
					
					for(PrintWriter print_writer : print_writers) {

						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
							"$Flag$img_Flag*ACTIVE SET 0 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
							"$Flag$img_Shadow*ACTIVE SET 0 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Select_HeaderTop*FUNCTION*Omo*vis_con SET 3 \0");
						
						switch(whatToProcess.split(",")[0]) {
						case "m":
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
									"$In_Out$Change$" + cout_name + "$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + matchAllData.getSetup().getMatchIdent() + "\0");
							
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
									"$In_Out$Change$" + cout_name + "$Change$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getTournament() + "\0");
							break;
						case "Control_F7":
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
									"$In_Out$Change$" + cout_name + "$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "TEAMS" + "\0");
							
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
									"$In_Out$Change$" + cout_name + "$Change$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamGroup() + 
									", " + city_name + "\0");
							break;
						}

						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Change$Bottom*ACTIVE SET 1 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$MoveForExtraData$Overall_Scaling$Header"
								+ "$Off$Bottom_Align$Header_Extra$loop$txt_Extra*GEOM*TEXT SET " + "" +"\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$MoveForExtraData$Overall_Scaling$Header"
								+ "$Off$Bottom_Align$Header_Extra$loop$txt_Extra2*GEOM*TEXT SET " +  "" +"\0");
					}
					break;
				case "Control_m": //MATCH PROMO
					if(WhichSide == 1) {
						cout_name = "Side1_Text_Move_For_Shrink";
					}else if(WhichSide == 2) {
						cout_name = "Side2_Text_For_Shrink";
					}
					
					for(PrintWriter print_writer : print_writers) {

						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
							"$Flag$img_Flag*ACTIVE SET 0 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide +
							"$Flag$img_Shadow*ACTIVE SET 0 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Select_HeaderTop*FUNCTION*Omo*vis_con SET 3 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + fixture.getTeamgroup().toUpperCase()+ "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Change$Bottom$txt_Subheader*GEOM*TEXT SET " +  matchAllData.getSetup().getTournament() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Header$Bottom_Align$HeaderDataAll$Side" + WhichSide + 
								"$In_Out$Change$" + cout_name + "$Change$Bottom*ACTIVE SET 1 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$MoveForExtraData$Overall_Scaling$Header"
								+ "$Off$Bottom_Align$Header_Extra$loop$txt_Extra*GEOM*TEXT SET " + "" +"\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$MoveForExtraData$Overall_Scaling$Header"
								+ "$Off$Bottom_Align$Header_Extra$loop$txt_Extra2*GEOM*TEXT SET " +  "" +"\0");
					}
					break;
				}
				break;
			}
			
		}
		return true;
	}
	public boolean PopulateFfBody(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException 
	{
		String container_name = "", how_out_txt = "";
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return false;
		} else {
			switch (whatToProcess) {
			case "F1": // Scorecard
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:

					inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning).findAny().orElse(null);
					if(inning == null) {
						return false;
					}
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 3 \0");
						for(int iRow = 1; iRow <= inning.getBattingCard().size(); iRow++) {
							switch (inning.getBattingCard().get(iRow-1).getStatus().toUpperCase()) {
							case CricketUtil.STILL_TO_BAT:
								
								if(inning.getBattingCard().get(iRow-1).getHowOut() == null) {

									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type*FUNCTION*Omo*vis_con SET 0 \0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Still_To_Bat$Data$Name$txt_BatterName*GEOM*TEXT SET "
										+ inning.getBattingCard().get(iRow-1).getPlayer().getTicker_name().toUpperCase() + "\0");
									
								} else {
									
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$Name$txt_BatterName*GEOM*TEXT SET "
										+ inning.getBattingCard().get(iRow-1).getPlayer().getTicker_name().toUpperCase() + "\0");
									
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$How_Out_1$txt_OutType*GEOM*TEXT SET "
										+ inning.getBattingCard().get(iRow-1).getHowOut().replace("_", " ").toLowerCase() + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$How_Out_1$txt_FielderName*GEOM*TEXT SET \0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$How_Out_2$txt_Bold*GEOM*TEXT SET \0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$How_Out_2$txt_BowlerName*GEOM*TEXT SET \0");
									
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$Runs$txt_Runs*GEOM*TEXT SET " 
										+ inning.getBattingCard().get(iRow-1).getRuns() + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$Balls$txt_Balls*GEOM*TEXT SET " 
										+ String.valueOf(inning.getBattingCard().get(iRow-1).getBalls()) + "\0");

								}
								break;
								
							default:
								
								switch (inning.getBattingCard().get(iRow-1).getStatus().toUpperCase()) {
								case CricketUtil.OUT:
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0");
									container_name = "Out";
									break;
								case CricketUtil.NOT_OUT:
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type*FUNCTION*Omo*vis_con SET 2 \0");
									container_name = "Not_Out";
									break;
								}
								
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name + "$Data$Name$txt_BatterName*GEOM*TEXT SET "
										+ inning.getBattingCard().get(iRow-1).getPlayer().getTicker_name().toUpperCase() + "\0");
								
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name + "$Data$Runs$txt_Runs*GEOM*TEXT SET " 
										+ inning.getBattingCard().get(iRow-1).getRuns() + "\0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name + "$Data$Balls$txt_Balls*GEOM*TEXT SET " 
										+ String.valueOf(inning.getBattingCard().get(iRow-1).getBalls()) + "\0");
								
								how_out_txt = CricketFunctions.processHowOutText("FOUR-PART-HOW-OUT", inning.getBattingCard().get(iRow-1));
								
								if(how_out_txt != null && how_out_txt.split("|").length >= 4) {
									//System.out.println("how_out_txt = "+ how_out_txt);
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name + 
										"$Data$How_Out_1$txt_OutType*GEOM*TEXT SET " + how_out_txt.split("\\|")[0].trim() + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name 
										+ "$Data$How_Out_1$txt_FielderName*GEOM*TEXT SET " + how_out_txt.split("\\|")[1].trim() + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name 
										+ "$Data$How_Out_2$txt_Bold*GEOM*TEXT SET " + how_out_txt.split("\\|")[2].trim() + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name 
										+ "$Data$How_Out_2$txt_BowlerName*GEOM*TEXT SET " + how_out_txt.split("\\|")[3].trim() + "\0");
								}else {
									// Alipto will make different container for NOT OUT
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name + 
										"$Data$How_Out_1$txt_OutType*GEOM*TEXT SET " + "" + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name 
										+ "$Data$How_Out_1$txt_FielderName*GEOM*TEXT SET " + "NOT OUT" + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name 
										+ "$Data$How_Out_2$txt_Bold*GEOM*TEXT SET " + "" + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$" + container_name 
										+ "$Data$How_Out_2$txt_BowlerName*GEOM*TEXT SET " + "" + "\0");
								}
								break;
							}
						}
					}
					break;
				}
				break;
			
			case "F2": //Bowling Card
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 4 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$1"
								+ "$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0");
						
						for(int j=1;j<=10;j++) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
									+ (j+1) + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 0 \0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$10"
									+ "$Select_Row_Type$FOW$Data$fig_" + j + "*GEOM*TEXT SET " + "" + "\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$11"
									+ "$Select_Row_Type$Score$Data$fig_" + j + "*GEOM*TEXT SET " + "" + "\0");
							
						}
						
						if(inning.getBowlingCard() != null && inning.getBowlingCard().size() > 0) {
							for(int iRow = 1; iRow <= inning.getBowlingCard().size(); iRow++) {
								if(inning.getBowlingCard().get(iRow-1).getRuns() > 0 || 
										((inning.getBowlingCard().get(iRow-1).getOvers()*6)+inning.getBowlingCard().get(iRow-1).getBalls()) > 0) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
										+ (iRow+1) + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 2 \0");
									
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
										+ (iRow+1) + "$Select_Row_Type$Players$Data$txt_Name*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getPlayer().getTicker_name() + "\0");
									
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
											+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Overs*GEOM*TEXT SET " + CricketFunctions.
											OverBalls(inning.getBowlingCard().get(iRow-1).getOvers(), inning.getBowlingCard().get(iRow-1).getBalls()) + "\0");
									
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
											+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Maidens*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getMaidens() + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
											+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Runs*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getRuns() + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
											+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Wickets*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getWickets() + "\0");
									
									if(inning.getBowlingCard().get(iRow-1).getEconomyRate() != null) {
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
												+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Economy*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getEconomyRate() + "\0");
									}else {
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
												+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Economy*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getPlayer().getTicker_name() + "\0");
									}
								}
							}
						}
						
						if(inning.getBowlingCard().size() <= 8) {
							if(inning.getFallsOfWickets() != null) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$10"
										+ "$Select_Row_Type*FUNCTION*Omo*vis_con SET 3 \0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$11"
										+ "$Select_Row_Type*FUNCTION*Omo*vis_con SET 4 \0");
								
								for(FallOfWicket fow : inning.getFallsOfWickets()) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
										+ "10$Select_Row_Type$FOW$Data$fig_" + fow.getFowNumber() + "*GEOM*TEXT SET " + fow.getFowNumber() + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Bowling_Card$Rows$"
										+ "11$Select_Row_Type$Score$Data$fig_" + fow.getFowNumber() + "*GEOM*TEXT SET " + fow.getFowRuns() + "\0");
								}
							}
						}else {
							
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
					return false;
				}
				
				for(PrintWriter print_writer : print_writers) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 5 \0");
					for(int i=1; i<=2; i++ ) {
						for(int j=1; j<=4; j++) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_"
									+ i + "$Row_" + j + "$Batsman*ACTIVE SET 0 \0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_"
									+ i + "$Row_" + j + "$Bowler*ACTIVE SET 0 \0");
						}
					}
					
					for(int i = 1; i <= WhichInning ; i++) {
						if(i == 1) {
							rowId=0;
							if(matchAllData.getMatch().getInning().get(i-1).getBattingTeamId() == matchAllData.getSetup().getTossWinningTeam()) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
										+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 1 \0");
							}else {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
										+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 0 \0");							}
							
						} else {
							rowId=0;
							if(matchAllData.getMatch().getInning().get(i-1).getBattingTeamId() == matchAllData.getSetup().getTossWinningTeam()) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
										+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 1 \0");
							}else {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
										+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 0 \0");
							}
						}
						
						if(matchAllData.getMatch().getInning().get(i-1).getBattingTeamId() == matchAllData.getSetup().getHomeTeamId()) {
							teamname = matchAllData.getSetup().getHomeTeam().getTeamName1();
							teamFlag = matchAllData.getSetup().getHomeTeam().getTeamName4();
						} else {
							teamname = matchAllData.getSetup().getAwayTeam().getTeamName1();
							teamFlag = matchAllData.getSetup().getAwayTeam().getTeamName4();
						}
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
								+ i +"$Tittle$Data$txt_Team*GEOM*TEXT SET " + teamname.toUpperCase() + " \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
								+ i +"$Tittle$FlagGrp$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  teamFlag + " \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
								+ i +"$Tittle$Data$Overs$txt_Overs_Value*GEOM*TEXT SET " + CricketFunctions.OverBalls(matchAllData.getMatch().getInning().get(i-1).
										getTotalOvers(),matchAllData.getMatch().getInning().get(i-1).getTotalBalls()) + " \0");
						
						if(matchAllData.getMatch().getInning().get(i-1).getTotalWickets() >= 10) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
									+ i +"$Tittle$Data$txt_Score*GEOM*TEXT SET " + matchAllData.getMatch().getInning().get(i-1).getTotalRuns() + " \0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" 
									+ i +"$Tittle$Data$txt_Score*GEOM*TEXT SET " + matchAllData.getMatch().getInning().get(i-1).getTotalRuns() 
									+ " - " + String.valueOf(matchAllData.getMatch().getInning().get(i-1).getTotalWickets()) + " \0");
						}
						
						if(matchAllData.getMatch().getInning().get(i-1).getBattingCard() != null) {
							Collections.sort(matchAllData.getMatch().getInning().get(i-1).getBattingCard(),new CricketFunctions.BatsmenScoreComparator());
							
							for(BattingCard bc : matchAllData.getMatch().getInning().get(i-1).getBattingCard()) {
								if(bc.getRuns() > 0) {
									// DJ to check retired/absent hurt 
									if(!bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
										rowId++;
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i +"$Row_"+rowId+"$Batsman*ACTIVE SET 1 \0");

										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
												"$Batsman$txt_Name*GEOM*TEXT SET " + bc.getPlayer().getTicker_name() + "\0");
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
												"$Batsman$txt_Runs*GEOM*TEXT SET " + bc.getRuns() + "\0");
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
												"$Batsman$txt_Balls*GEOM*TEXT SET " + String.valueOf(bc.getBalls()) + "\0");

										if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
											print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
													"$Batsman$txt_Not-Out*GEOM*TEXT SET " + "*" + "\0");
										} else {
											print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
													"$Batsman$txt_Not-Out*GEOM*TEXT SET " + "" + "\0");
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
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i +"$Row_"+rowId+"$Bowler*ACTIVE SET 1 \0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
											"$Bowler$txt_Name*GEOM*TEXT SET " + boc.getPlayer().getTicker_name() + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
											"$Bowler$txt_Figs*GEOM*TEXT SET " + boc.getWickets() + "-" + boc.getRuns() + "\0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Summary$Team_" + i + "$Row_"+rowId+
											"$Bowler$txt_Overs*GEOM*TEXT SET " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + "\0");
									
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
				}
				break;
			case "F4":
				int row_id=0, omo_num=0,Top_Score = 50;
				double Mult = 160,ScaleFac1 = 0, ScaleFac2 = 0;
				String Left_Batsman="", Right_Batsman="", cont_name="";
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning)
						.findAny().orElse(null);
				for(PrintWriter print_writer : print_writers) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 6 \0");
					
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
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
								+ row_id+  "$Select_Row_Type*FUNCTION*Omo*vis_con SET " + omo_num + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
								+ row_id + "$Select_Row_Type" + cont_name +"$Data$txt_Name_1*GEOM*TEXT SET " + Left_Batsman + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
								+ row_id + "$Select_Row_Type" + cont_name +"$Data$txt_Name_2*GEOM*TEXT SET " + Right_Batsman + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
								+ row_id + "$Select_Row_Type" + cont_name +"$Data$txt_Runs*GEOM*TEXT SET " + ps.getTotalRuns() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
								+ row_id + "$Select_Row_Type" + cont_name +"$Data$txt_Balls*GEOM*TEXT SET " + ps.getTotalBalls() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
								+ row_id  + "$Select_Row_Type" + cont_name + "$Geom_Bar_1*GEOM*width SET " + ScaleFac1 + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
								+ row_id  + "$Select_Row_Type" + cont_name + "$Geom_Bar_2*GEOM*width SET " + ScaleFac2 + "\0");
					}
					
					if(inning.getPartnerships().size() >= 10) {
						row_id = row_id + 1;
						
					}else {
						for (BattingCard bc : inning.getBattingCard()) {
							if(row_id < inning.getBattingCard().size()) {
								if(row_id == inning.getPartnerships().size()) {
									row_id = row_id + 1;
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
											+ row_id+  "$Select_Row_Type*FUNCTION*Omo*vis_con SET 0 \0");
									
									if(matchAllData.getMatch().getInning().get(WhichInning - 1).getTotalOvers() == matchAllData.getSetup().getMaxOvers() 
											|| matchAllData.getMatch().getInning().get(WhichInning - 1).getTotalWickets() >= 10 ) {
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
												+ row_id+  "$Select_Row_Type$Title$Data$txt_Name*GEOM*TEXT SET " + "DID NOT BAT" + "\0");
									}
									else if(matchAllData.getSetup().getTargetOvers() != null && !matchAllData.getSetup().getTargetOvers().isEmpty()) {
										if( matchAllData.getMatch().getInning().get(WhichInning - 1).getTotalOvers() == Integer.valueOf(matchAllData.getSetup().getTargetOvers()) 
												|| matchAllData.getMatch().getInning().get(WhichInning - 1).getTotalWickets() >= 10) {
											print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
													+ row_id+  "$Select_Row_Type$Title$Data$txt_Name*GEOM*TEXT SET " + "DID NOT BAT" + "\0");
										}else if(CricketFunctions.getRequiredRuns(matchAllData) <= 0 || CricketFunctions.getRequiredBalls(matchAllData) <= 0 
												|| CricketFunctions.getWicketsLeft(matchAllData) <= 0) {
											print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
													+ row_id+  "$Select_Row_Type$Title$Data$txt_Name*GEOM*TEXT SET " + "DID NOT BAT" + "\0");
										}else {
											print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
													+ row_id+  "$Select_Row_Type$Title$Data$txt_Name*GEOM*TEXT SET " + "STILL TO BAT" + "\0");
										}
									}
									else if(CricketFunctions.getRequiredRuns(matchAllData) <= 0 || CricketFunctions.getRequiredBalls(matchAllData) <= 0 || 
											CricketFunctions.getWicketsLeft(matchAllData) <= 0) {
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
												+ row_id+  "$Select_Row_Type$Title$Data$txt_Name*GEOM*TEXT SET " + "DID NOT BAT" + "\0");
									}
									else {
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
												+ row_id+  "$Select_Row_Type$Title$Data$txt_Name*GEOM*TEXT SET " + "STILL TO BAT" + "\0");
									}
								}
								else if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
									row_id = row_id + 1;
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
											+ row_id+  "$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0");
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
											+ row_id+  "$Select_Row_Type$Still_To_Bat$Data$txt_Name*GEOM*TEXT SET " + bc.getPlayer().getTicker_name() + "\0");
								}	
							}
							else {
								break;
							}
						}
					}
				}
				break;
			case "Control_d":  
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					double average = stat.getRuns()/(stat.getMatches()-stat.getNot_out());
					double strikeRate = (stat.getRuns()/stat.getBalls_faced())*100;
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
								+ "$SubTitle$Data$txt_SubTitle*GEOM*TEXT SET " + "CAREER" + "\0");
						if(stat.getMatches()==0) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$1$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$1$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + stat.getMatches() + "\0");
						}
						if(stat.getRuns()==0) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$1$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$1$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + stat.getRuns() + "\0");
						}
						if(stat.getFifties()==0) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$2$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$2$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + stat.getFifties() + "\0");
						}
						if(stat.getHundreds() == 0) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$2$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$2$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + stat.getHundreds() + "\0");
						}
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
								+ "$Stats$3$Data$Stat_1$Normal$txt_Desig*GEOM*TEXT SET " + "AVERAGE" + "\0");
						if(average==0) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$3$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$3$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + average + "\0");
						}
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
								+ "$Stats$3$Data$Stat_2$Normal$txt_Desig*GEOM*TEXT SET " + "STRIKE RATE" + "\0");
						if(strikeRate ==0) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$3$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$3$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + strikeRate + "\0");
						}
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
								+ "$Stats$4$Data$Stat_1$Normal$txt_Desig*GEOM*TEXT SET " + "BEST SCORE" + "\0");
						if(stat.getBest_score()==null) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_Fig*GEOM*TEXT SET " + "" + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_Fig*GEOM*TEXT SET " + stat.getBest_score() + "\0");
						}
						
						if(stat.getBest_score_against()==null) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_VersusTeam*GEOM*TEXT SET " + "" + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_VersusTeam*GEOM*TEXT SET " + "v "+ stat.getBest_score_against().toUpperCase() + "\0");
						}
						if(stat.getBest_score_venue()==null) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_VersusLOcation*GEOM*TEXT SET " + "" + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_VersusLOcation*GEOM*TEXT SET " + "AT "+stat.getBest_score_venue() + "\0");
						}
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$PhotoPart"
								+ "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$PhotoPart"
								+ "$Photo$img_PlayerPhoto*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + team.getTeamName4() + 
								Constants.RIGHT_2048 + player.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0");
					}
					
					break;
				}
				break;
			case "Control_e":
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					double economy_rate;
					double average = stat.getRuns_conceded()/stat.getWickets();
					DecimalFormat df = new DecimalFormat("0.00");
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
								+ "$SubTitle$Data$txt_SubTitle*GEOM*TEXT SET " + "CAREER" + "\0");
						if(stat.getMatches()==0) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$1$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$1$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + stat.getMatches() + "\0");
						}
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
								+ "$Stats$1$Data$Stat_2$Normal$txt_Desig*GEOM*TEXT SET " + "WICKETS" + "\0");
						if(stat.getWickets()==0) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$1$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$1$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + stat.getWickets() + "\0");
						}
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
								+ "$Stats$2$Data$Stat_1$Normal$txt_Desig*GEOM*TEXT SET " + "3WI" + "\0");
						if(stat.getPlus_3()==0) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$2$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$2$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + stat.getPlus_3() + "\0");
						}
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
								+ "$Stats$2$Data$Stat_2$Normal$txt_Desig*GEOM*TEXT SET " + "5WI" + "\0");
						if(stat.getPlus_5() == 0) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$2$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$2$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + stat.getPlus_5() + "\0");
						}
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
								+ "$Stats$3$Data$Stat_1$Normal$txt_Desig*GEOM*TEXT SET " + "AVERAGE" + "\0");
						if(average==0) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$3$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$3$Data$Stat_1$Normal$txt_Fig*GEOM*TEXT SET " + df.format(average) + "\0");
						}
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
								+ "$Stats$3$Data$Stat_2$Normal$txt_Desig*GEOM*TEXT SET " + "ECONOMY" + "\0");
						if(stat.getRuns_conceded() == 0 && stat.getBalls_bowled() == 0) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$3$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + "-" + "\0");
						}else {
							economy_rate = stat.getRuns_conceded() / stat.getBalls_bowled();
							economy_rate = economy_rate * 6;
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$3$Data$Stat_2$Normal$txt_Fig*GEOM*TEXT SET " + df.format(economy_rate) + "\0");
						}
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
								+ "$Stats$4$Data$Stat_1$Normal$txt_Desig*GEOM*TEXT SET " + "BEST FIGURES" + "\0");
						
						if(stat.getBest_figures()==null) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_Fig*GEOM*TEXT SET " + "" + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_Fig*GEOM*TEXT SET " + stat.getBest_figures() + "\0");
						}
						
						if(stat.getBest_figures_against()==null) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_VersusTeam*GEOM*TEXT SET " + "" + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_VersusTeam*GEOM*TEXT SET " + "v "+ stat.getBest_figures_against().toUpperCase() + "\0");
						}
						
						if(stat.getBest_figures_venue()==null) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_VersusLOcation*GEOM*TEXT SET " + "" + "\0");
						}else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
									+ "$Stats$4$Data$Stat_1$Normal$Justify$txt_VersusLOcation*GEOM*TEXT SET " + "AT "+stat.getBest_figures_venue() + "\0");
						}
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$PhotoPart"
								+ "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$PhotoPart"
								+ "$Photo$img_PlayerPhoto*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + team.getTeamName4() + Constants.RIGHT_2048 + 
									player.getPhoto() + CricketUtil.PNG_EXTENSION  + "\0");
					}
					
					break;
				}
				break;
			case "Control_F7": //Double Teams
				//System.out.println("Hi");
				int row_no = 0;
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 9 \0");
						
						for(int i = 1; i <= 2 ; i++) {
							if(i == 1) {
								
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_1$Tittle"
										+ "$txt_Tittle*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamName1().toUpperCase() + "\0");
								
								for(Player hs : matchAllData.getSetup().getHomeSquad()) {
									row_no = row_no + 1;
									
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_1$" + 
											row_no + "$txt_Name*GEOM*TEXT SET " + hs.getFull_name() + "\0");
									
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_1$" + 
											row_no + "$txt_Description*GEOM*TEXT SET " + "" + "\0");
									
									if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)) {
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_1$" + 
												row_no + "$txt_Description*GEOM*TEXT SET " + "(C)" + "\0");
									}
									else if(hs.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_1$" + 
												row_no + "$txt_Description*GEOM*TEXT SET " + "(C & WK)" + "\0");
									}
									else if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_1$" + 
												row_no + "$txt_Description*GEOM*TEXT SET " + "(WK)" + "\0");
									}
								}
								
								
							} else {
								row_no = 0;
								
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_2$Tittle"
										+ "$txt_Tittle*GEOM*TEXT SET " + matchAllData.getSetup().getAwayTeam().getTeamName1().toUpperCase() + "\0");
								
								for(Player as : matchAllData.getSetup().getAwaySquad()) {
									row_no = row_no + 1;
									
									
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_2$" + 
											row_no + "$txt_Name*GEOM*TEXT SET " + as.getFull_name() + "\0");
									
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_2$" + 
											row_no + "$txt_Description*GEOM*TEXT SET " + "" + "\0");
									
									if(as.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)) {
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_2$" + 
												row_no + "$txt_Description*GEOM*TEXT SET " + "(C)" + "\0");
									}
									else if(as.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_2$" + 
												row_no + "$txt_Description*GEOM*TEXT SET " + "(C & WK)" + "\0");
									}
									else if(as.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
										print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Teams$Team_2$" + 
												row_no + "$txt_Description*GEOM*TEXT SET " + "(WK)" + "\0");
									}
									
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
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 1 \0");
						int player_no=0;
						for(int i=1;i<=PlayingXI.size();i++) {
							switch(i) {
							case 1: case 2: case 3: case 4: case 5: case 6:
								player_no = i;
								container_name = "$Top";
								break;
							case 7: case 8: case 9: case 10: case 11:
								player_no = i - 6;
								container_name = "$Bottom";
								break;
							}
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + container_name
									+ "$Photo_" + player_no + "$Gradient$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0");
							
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + container_name
									+ "$Photo_" + player_no + "$Gradient$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0");
							
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + container_name
									+ "$Photo_" + player_no + "$img_Photo*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + team.getTeamName4() + 
										Constants.CENTRE_512 + PlayingXI.get(i-1).getPhoto() + CricketUtil.PNG_EXTENSION + "\0");
							
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + container_name
									+ "$Photo_" + player_no + "$Name$txt_PlayerName*GEOM*TEXT SET " + PlayingXI.get(i-1).getTicker_name() + "\0");
							
							if(PlayingXI.get(i-1).getRole().equalsIgnoreCase(CricketUtil.BATSMAN) || PlayingXI.get(i-1).getRole().equalsIgnoreCase("BAT/KEEPER")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + container_name
										+ "$Photo_" + player_no + "$Select_Role*FUNCTION*Omo*vis_con SET 0 \0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + container_name
										+ "$Photo_" + player_no + "$Select_Role$Single_Style1$txt_Bat*GEOM*TEXT SET " + "BAT" + "\0");
							}
							else if(PlayingXI.get(i-1).getRole().equalsIgnoreCase(CricketUtil.BOWLER)) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + container_name
										+ "$Photo_" + player_no + "$Select_Role*FUNCTION*Omo*vis_con SET 1 \0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + container_name
										+ "$Photo_" + player_no + "$Select_Role$Single_Style2$txt_Bowl*GEOM*TEXT SET " + 
										CricketFunctions.getBowlerType(PlayingXI.get(i-1).getBowlingStyle()).replace("PACE", "SEAM") + "\0");
							}
							else if(PlayingXI.get(i-1).getRole().equalsIgnoreCase("ALL-ROUNDER")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + container_name
										+ "$Photo_" + player_no + "$Select_Role*FUNCTION*Omo*vis_con SET 2 \0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + container_name
										+ "$Photo_" + player_no + "$Select_Role$Double_Style$txt_Bat*GEOM*TEXT SET " + "BAT" + "\0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + container_name
										+ "$Photo_" + player_no + "$Select_Role$Double_Style$txt_Bowl*GEOM*TEXT SET " + 
										CricketFunctions.getBowlerType(PlayingXI.get(i-1).getBowlingStyle()).replace("PACE", "SEAM") + "\0");
							}
							
							if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)){
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + container_name
										+ "$Photo_" + player_no + "$Select_Captain*FUNCTION*Omo*vis_con SET 1 \0");
							}
							else if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + container_name
										+ "$Photo_" + player_no + "$Select_Captain*FUNCTION*Omo*vis_con SET 0 \0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + container_name
										+ "$Photo_" + player_no + "$Select_Role*FUNCTION*Omo*vis_con SET 1 \0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + container_name
										+ "$Photo_" + player_no + "$Select_Role$Single_Style2$txt_Bowl*GEOM*TEXT SET " + "KEEPER" + "\0");
							}
							else if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + container_name
										+ "$Photo_" + player_no + "$Select_Captain*FUNCTION*Omo*vis_con SET 1 \0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + container_name
										+ "$Photo_" + player_no + "$Select_Role*FUNCTION*Omo*vis_con SET 1 \0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + container_name
										+ "$Photo_" + player_no + "$Select_Role$Single_Style2$txt_Bowl*GEOM*TEXT SET " + "KEEPER" + "\0");
							}else {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$LineUp_Image" + container_name
										+ "$Photo_" + player_no + "$Select_Captain*FUNCTION*Omo*vis_con SET 0 \0");
							}
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
				
				
				for(PrintWriter print_writer : print_writers) {
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 7 \0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership$1"
							+ "$Data$txt_Runs*GEOM*TEXT SET " +total_runs+ "\0");

					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership$1"
							+ "$Data$txt_Runs*GEOM*TEXT SET " +total_balls+ "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership$2"
							+ "$Data$fig_StrikeRate*GEOM*TEXT SET " +strikeRate+ "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership$4"
							+ "$Data$txt_Name*GEOM*TEXT SET " +left_bat+ "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership$4"
							+ "$Data$txt_Runs*GEOM*TEXT SET " +first_batter_run+ "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership$4"
							+ "$Data$txt_Ball*GEOM*TEXT SET " +first_batter_ball+ "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership$5"
							+ "$Data$txt_Name*GEOM*TEXT SET " +right_bat+ "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership$5"
							+ "$Data$txt_Runs*GEOM*TEXT SET " +second_batter_run+ "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership$5"
							+ "$Data$txt_Ball*GEOM*TEXT SET " +second_batter_ball+ "\0");
					
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership"
							+ "$Photo$PhotoGrp1$img_PlayerShadow1*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + inning.getBatting_team().getTeamName4() + Constants.LEFT_1024 + 
								left_bat_photo + CricketUtil.PNG_EXTENSION  + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership"
							+ "$Photo$PhotoGrp1$img_PlayerPhoto1*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + inning.getBatting_team().getTeamName4() + Constants.LEFT_1024 + 
								left_bat_photo + CricketUtil.PNG_EXTENSION  + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership"
							+ "$Photo$PhotoGrp2$img_PlayerShadow1*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + inning.getBatting_team().getTeamName4() + Constants.RIGHT_1024 + 
								right_bat_photo + CricketUtil.PNG_EXTENSION  + "\0");
					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership"
							+ "$Photo$PhotoGrp2$img_PlayerPhoto1*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + inning.getBatting_team().getTeamName4() + Constants.RIGHT_1024 + 
								right_bat_photo + CricketUtil.PNG_EXTENSION  + "\0");
					

					print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Partnership"
							+ "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName4() + "\0");
				}
				
				break;
			
			case "m": //MATCH Ident
				
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
								+ "$Data$Select_DataForPromo*FUNCTION*Omo*vis_con SET 0 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
								+ "$Data$txt_Team_1*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamName1().toUpperCase() + "\0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
								+ "$Data$txt_Team_2*GEOM*TEXT SET " + matchAllData.getSetup().getAwayTeam().getTeamName1().toUpperCase() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Flag_Top$"
								+ "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  matchAllData.getSetup().getHomeTeam().getTeamName4() + "\0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Flag_Bottom$"
								+ "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  matchAllData.getSetup().getAwayTeam().getTeamName4() + "\0");
					}
					break;
				}
				break;
			case "Control_m": //MATCH PROMO
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					String newDate = "",date_data = "";
					for(PrintWriter print_writer : print_writers) {
						
						String dayOfWeek = new SimpleDateFormat("EEEE").format(new SimpleDateFormat("dd-MM-yyyy").parse(fixture.getDate()));
						String timeDetail="";
						
						newDate = fixture.getDate().split("-")[0];
						date_data = CricketFunctions.ordinal(Integer.valueOf(newDate)) + " " + Month.of(Integer.valueOf(fixture.getDate().split("-")[1])) + " 2024";
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
								+ "$Data$Select_DataForPromo*FUNCTION*Omo*vis_con SET 0 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
								+ "$Data$Select_DataForPromo*FUNCTION*Omo*vis_con SET 1 \0");
						

						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
									+ "$Data$Select_DataForPromo$ExtraInfo$txt_Info1*GEOM*TEXT SET " + dayOfWeek.toUpperCase() + ", " +date_data + "\0");
						
						if(fixture.getLocalTime() != null) {
							timeDetail = fixture.getLocalTime() + " LOCAL TIME ";
							if(fixture.getGmtTime() != null) {
								timeDetail = timeDetail + "(" + fixture.getGmtTime() + " GMT)";
 							}
						}
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
								+ "$Data$Select_DataForPromo$ExtraInfo$txt_Info2*GEOM*TEXT SET " + timeDetail + "\0");
						
						for(Team tm : Teams) {
							if(tm.getTeamId() == fixture.getHometeamid()) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
										+ "$Data$txt_Team_1*GEOM*TEXT SET "+ tm.getTeamName1() + "\0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Flag_Top$"
										+ "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  tm.getTeamName4() + "\0");
							}
							
							if(tm.getTeamId() == fixture.getAwayteamid()) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Text"
										+ "$Data$txt_Team_2*GEOM*TEXT SET " + tm.getTeamName1() + "\0");
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$MatchIdent$Flag_Bottom$"
										+ "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  tm.getTeamName4() + "\0");
							}
						}
						
					}
					break;
				}
				break;
			}
		}
		return true;
	}
	public boolean PopulateFfFooter(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) 
	{
		if (matchAllData == null || matchAllData.getMatch() == null || matchAllData.getMatch().getInning() == null) {
			return false;
		} else {

			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				switch (whatToProcess) {
				// Scorecard - BowlingCard
				case "F1": case "F2": case "F4": case "Shift_K":
					inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning).findAny().orElse(null);
				
					if(inning != null) {
						for(PrintWriter print_writer : print_writers) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer*ACTIVE SET 1 \0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Select_FooterType*FUNCTION*Omo*vis_con SET 1 \0");
							
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Select_FooterType$Score$Data$Side"
									+ WhichSide + "$In_Out$Change$In_Out$Extras$txt_Extras_Value*GEOM*TEXT SET " + inning.getTotalExtras() + "\0");
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Select_FooterType$Score$Data$Side"
									+ WhichSide + "$In_Out$Change$In_Out$Overs$txt_Overs_Value*GEOM*TEXT SET " + 
									CricketFunctions.OverBalls(inning.getTotalOvers(),inning.getTotalBalls()) + "\0");
							
							if(matchAllData.getSetup().getTargetOvers() != null) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Select_FooterType$Score$Data$Side"
										+ WhichSide + "$In_Out$Change$In_Out$Overs$txt_Overs_2*GEOM*TEXT SET " + matchAllData.getSetup().getTargetOvers() + "\0");
							}else {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Select_FooterType$Score$Data$Side"
										+ WhichSide + "$In_Out$Change$In_Out$Overs$txt_Overs_2*GEOM*TEXT SET " + "" + "\0");
							}
							
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Select_FooterType$Score$Data$Side"
									+ WhichSide + "$In_Out$Change$Score$txt_Score*GEOM*TEXT SET " + CricketFunctions.getTeamScore(inning, "-", false) + "\0");
						}
					} else {
						return false;
					}
					break;
				 case "Shift_F11": //MATCH SUMMARY
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer*ACTIVE SET 1 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType*FUNCTION*Omo*vis_con SET 2 \0");
						// DJ use proper match summary function
						if(matchAllData.getMatch().getMatchResult() != null) {
							if(matchAllData.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
										"$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.generateMatchSummaryStatus(WhichInning, matchAllData, "MIDDLE").toUpperCase() + "\0");
							}
							else if(matchAllData.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("DRAWN")){
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
										"$txt_Info_1*GEOM*TEXT SET " + "MATCH TIED" + "\0");
							}
							else if(matchAllData.getMatch().getMatchResult().toUpperCase().equalsIgnoreCase("ABANDONED")) {
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
										"$txt_Info_1*GEOM*TEXT SET " + matchAllData.getMatch().getMatchStatus().toUpperCase() + "\0");
							}
							else if(matchAllData.getMatch().getMatchResult().split(",")[2].toUpperCase().equalsIgnoreCase("SUPER_OVER")){
								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
										"$txt_Info_1*GEOM*TEXT SET " +"MATCH TIED - " + CricketFunctions.generateMatchSummaryStatus(WhichInning, matchAllData, "MIDDLE").toUpperCase() + "\0");
							}
							else {

								print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
										"$txt_Info_1*GEOM*TEXT SET " +CricketFunctions.generateMatchSummaryStatus(WhichInning, matchAllData, "MIDDLE").toUpperCase() + "\0");
							}
						}
						else {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
									"$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.generateMatchSummaryStatus(WhichInning, matchAllData, "MIDDLE").toUpperCase() + "\0");
							if(matchAllData.getSetup().getTargetType() != null) {
								if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("VJD")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
											"$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.generateMatchSummaryStatus(WhichInning, matchAllData, "MIDDLE").toUpperCase() + "\0");
								}else if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("DLS")) {
									print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
											"$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.generateMatchSummaryStatus(WhichInning, matchAllData, "MIDDLE").toUpperCase() + "\0");
								}
							}
						}
					}
					break;
				 case "Control_d": case "Control_e":
						for(PrintWriter print_writer : print_writers) {
							print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer*ACTIVE SET 0 \0");
						}
						break;

				 case "Control_F7": case "Control_F8": //PlayingXI
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer*ACTIVE SET 1 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType*FUNCTION*Omo*vis_con SET 2 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Info_Text$Data$Side" + WhichSide + 
								"$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.generateTossResult(matchAllData, CricketUtil.FULL, CricketUtil.FIELD, CricketUtil.FULL, 
										CricketUtil.CHOSE).toUpperCase() + "\0");
					}
					break;
				
				case "m": //MATCH ID
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer*ACTIVE SET 1 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType*FUNCTION*Omo*vis_con SET 4 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Ident$Data$Side" + WhichSide + 
								"$txt_Info_1*GEOM*TEXT SET " + "LIVE FROM " + matchAllData.getSetup().getVenueName() + "\0");
					}
					break;
				case "Contro_m": //MATCH PROMO
					for(PrintWriter print_writer : print_writers) {
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer*ACTIVE SET 1 \0");
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType*FUNCTION*Omo*vis_con SET 4 \0");
						
						print_writer.println("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$Footer$Top_Align$Select_FooterType$Ident$Data$Side" + WhichSide + 
								"$txt_Info_1*GEOM*TEXT SET " + "LIVE FROM " + fixture.getVenue() + "\0");
					}
					break;

				}
				break;
			}
			
		}
		return true;
	}
 
}