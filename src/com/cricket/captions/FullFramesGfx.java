package com.cricket.captions;

import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.cricket.model.OverByOverData;
import com.cricket.model.Partnership;
import com.cricket.model.Player;
import com.cricket.model.Statistics;
import com.cricket.model.StatsType;
import com.cricket.model.Team;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class FullFramesGfx 
{
	public int FirstPlayerId, rowId = 0, numberOfRows = 0;
	public String WhichProfile, WhichStyle, WhichType, status = "", containerName = "",containerName_2 = "";
	
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
	public String whichSponsor;
	
	public List<BattingCard> battingCardList = new ArrayList<BattingCard>();
	public List<OverByOverData> manhattan = new ArrayList<OverByOverData>();
	public List<Statistics> statisticsList = new ArrayList<Statistics>();
	
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
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning)
			.findAny().orElse(null);
		if(inning == null) {
			return "populateMatchSummary: current inning is NULL";
		}
		status = PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning);
			if(status == Constants.OK) {
				setFullFrameFooterPosition(WhichSide, 1);
				this.numberOfRows = inning.getBattingCard().size();
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, WhichInning);
			} else {
				return status;
			}
		} else {
			return status;
		}
	}
	public String PopulatePhotoScorecardFF(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException
	{
		
		if(matchAllData.getMatch().getInning().get(WhichInning-1).getBattingTeamId() == matchAllData.getSetup().getHomeTeamId()) {
			PlayingXI = matchAllData.getSetup().getHomeSquad();
		}else {
			PlayingXI = matchAllData.getSetup().getAwaySquad();
		}
		
		status = PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning);
			if(status == Constants.OK) {
				setFullFrameFooterPosition(WhichSide, 1);
				this.numberOfRows = 11;
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
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning)
				.findAny().orElse(null);
		if(inning == null) {
			return "populateMatchSummary: current inning is NULL";
		}
		status = PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning);
			if(status == Constants.OK) {
				setFullFrameFooterPosition(WhichSide, 1);
				this.numberOfRows = 11;
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
				setFullFrameFooterPosition(WhichSide, 4);
				this.numberOfRows = 11;
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
		ground = Grounds.stream().filter(grnd -> grnd.getFullname().contains(matchAllData.getSetup().getVenueName())).findAny().orElse(null);
		if(ground == null) {
			return "populateMatchSummary: Ground Name [" + matchAllData.getSetup().getVenueName() + "] from database is returning NULL";
		}
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning)
				.findAny().orElse(null);
		if(inning == null) {
			return "populateMatchSummary: current inning is NULL";
		}
		
		status = PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning);
			if(status == Constants.OK) {
				setFullFrameFooterPosition(WhichSide, 2);
				this.numberOfRows = 11;
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
				setFullFrameFooterPosition(WhichSide, 4);
				this.numberOfRows = 11;
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
		
		ground = Grounds.stream().filter(grnd -> grnd.getFullname().contains(matchAllData.getSetup().getVenueName())).findAny().orElse(null);
		if(ground == null) {
			return "populatePlayingXI: Ground Name [" + matchAllData.getSetup().getVenueName() + "] from database is returning NULL";
		}
		
		team = Teams.stream().filter(tm -> tm.getTeamId() == teamId).findAny().orElse(null);
		if(team == null) {
			return "populatePlayingXI: Team id [" + teamId + "] from database is returning NULL";
		}
		status = PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning);
			if(status == Constants.OK) {
				setFullFrameFooterPosition(WhichSide, 2);
				this.numberOfRows = 11;
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
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning)
				.findAny().orElse(null);
		if(inning == null) {
			return "populateMatchSummary: current inning is NULL";
		}
		status = PopulateFfHeader(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
			if(status == Constants.OK) {
				setFullFrameFooterPosition(WhichSide, 1);
				if(inning.getPartnerships().size()>=10) {
					this.numberOfRows = inning.getPartnerships().size();
				}else {
					this.numberOfRows = inning.getBattingCard().size();
				}
				return PopulateFfFooter(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
			} else {
				return status;
			}
		} else {
			return status;
		}
	}
	public String populatePlayerProfile(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException, JsonMappingException, JsonProcessingException, InterruptedException 
	{
		
		FirstPlayerId = Integer.valueOf(whatToProcess.split(",")[2]);
		WhichProfile = whatToProcess.split(",")[3];
		
		if(FirstPlayerId <= 0 || WhichProfile == null) {
			return "populatePlayerProfile: Player Id NOT found [" + FirstPlayerId + "]";
		}
		
		
		player = CricketFunctions.getPlayerFromMatchData(FirstPlayerId, matchAllData);
		if(player == null) {
			return "populatePlayerProfile: Player id [" + whatToProcess.split(",")[2] + "] from database is returning NULL";
		}
		
		stat = statistics.stream().filter(stat -> stat.getPlayer_id() == player.getPlayerId()).findAny().orElse(null);
		if(stat == null) {
			return "populatePlayerProfile: No stats found for player id [" + player.getPlayerId() + "] from database is returning NULL";
		}
		
		statsType = statsTypes.stream().filter(st -> st.getStats_short_name().equalsIgnoreCase(WhichProfile)).findAny().orElse(null);
		if(statsType == null) {
			return "PopulateL3rdPlayerProfile: Stats Type not found for profile [" + WhichProfile + "]";
		}
		
		stat.setStats_type(statsType);
		
		stat = CricketFunctions.updateTournamentDataWithStats(stat, tournament_matches, matchAllData);
		stat = CricketFunctions.updateStatisticsWithMatchData(stat, matchAllData);
		
		team = Teams.stream().filter(tm -> tm.getTeamId() == player.getTeamId()).findAny().orElse(null);
		if(team == null) {
			return "populatePlayerProfile: Team id [" + player.getTeamId() + "] from database is returning NULL";
		}

		status = PopulateFfHeader(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
			if(status == Constants.OK) {
				this.numberOfRows = 11;
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
		ground = Grounds.stream().filter(grnd -> grnd.getFullname().contains(matchAllData.getSetup().getVenueName())).findAny().orElse(null);
		if(ground == null) {
			return "PopulateDoubleTeams: Ground Name [" + matchAllData.getSetup().getVenueName() + "] from database is returning NULL";
		}

		status = PopulateFfHeader(WhichSide, whatToProcess, matchAllData, 0);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess, matchAllData, 0);
			if(status == Constants.OK) {
				setFullFrameFooterPosition(WhichSide, 2);
				this.numberOfRows = 12;
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
			return "populateCurrPartnership: Current Inning NOT found in this match";
		}
		
		if(inning.getPartnerships() != null && inning.getPartnerships().size() <= 0) {
			return "populateCurrPartnership: Partnership NOT found in this match";
		}
		
		battingCardList = new ArrayList<BattingCard>();
		for(int iBat = 1; iBat <= 2; iBat++) {
			if(iBat == 1) {
				battingCardList.add(inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == 
					inning.getPartnerships().get(inning.getPartnerships().size() - 1).getFirstBatterNo()).findAny().orElse(null));
			} else {
				battingCardList.add(inning.getBattingCard().stream().filter(bc -> bc.getPlayerId() == 
					inning.getPartnerships().get(inning.getPartnerships().size() - 1).getSecondBatterNo()).findAny().orElse(null));
			}
			if(battingCardList.get(battingCardList.size()-1) == null) {
				return "populateCurrPartnership: battingCardList is null";
			}
		}
		
		status = PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning);
			if(status == Constants.OK) {
				setFullFrameFooterPosition(WhichSide, 1);
				this.numberOfRows = 11;
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, WhichInning);
			} else {
				return status;
			}
		} else {
			return status;
		}
		
	}
	public String populateManhattan(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException
	{
		manhattan = new ArrayList<OverByOverData>();
		manhattan = CricketFunctions.getOverByOverData(matchAllData, WhichInning,"MANHATTAN" ,matchAllData.getEventFile().getEvents());
		if(manhattan == null) {
			return "populateManhattan is null";
		}
		
		status = PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning);
			if(status == Constants.OK) {
				setFullFrameFooterPosition(WhichSide, 1);
				this.numberOfRows = 12;
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, WhichInning);
			} else {
				return status;
			}
		} else {
			return status;
		}
	}
	public String populateWorms(int WhichSide, String whatToProcess, MatchAllData matchAllData,int WhichInning) throws ParseException
	{
		ground = Grounds.stream().filter(grnd -> grnd.getFullname().contains(matchAllData.getSetup().getVenueName())).findAny().orElse(null);
		if(ground == null) {
			return "populateWorms: Ground Name [" + matchAllData.getSetup().getVenueName() + "] from database is returning NULL";
		}
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning)
				.findAny().orElse(null);
		if(inning == null) {
			return "populateWorms: current inning is NULL";
		}
		status = PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning);
			if(status == Constants.OK) {
				setFullFrameFooterPosition(WhichSide, 2);
				this.numberOfRows = 12;
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, WhichInning);
			} else {
				return status;
			}
		} else {
			return status;
		}
	}
	public String populateSingleTeams(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException
	{	
		
		WhichProfile = whatToProcess.split(",")[3];
		WhichStyle = whatToProcess.split(",")[4];
		WhichType = whatToProcess.split(",")[5];
		
		if(Integer.valueOf(whatToProcess.split(",")[2]) == matchAllData.getSetup().getHomeTeamId()) {
			PlayingXI = matchAllData.getSetup().getHomeSquad();
		}else {
			PlayingXI = matchAllData.getSetup().getAwaySquad();
		}
		
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getBattingTeamId() == Integer.valueOf(whatToProcess.split(",")[2]))
				.findAny().orElse(null);
		if(inning == null) {
			return "populateSingleTeams current inning is NULL";
		}
		
		statsType = statsTypes.stream().filter(st -> st.getStats_short_name().equalsIgnoreCase(WhichProfile)).findAny().orElse(null);
		if(statsType == null) {
			return "populateSingleTeams: Stats Type not found for profile [" + WhichProfile + "]";
		}
		
		for(Statistics stat : statistics) {
			if(stat.getStats_type_id() == statsType.getStats_id()) {
				statisticsList.add(stat);
			}
		}
		if(statisticsList == null && statisticsList.isEmpty()) {
			return "populateSingleTeams: Stats List is null";
		}
		
		status = PopulateFfHeader(WhichSide, whatToProcess.split(",")[0], matchAllData, 0);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess.split(",")[0], matchAllData, 0);
			if(status == Constants.OK) {
				setFullFrameFooterPosition(WhichSide, 2);
				this.numberOfRows = 12;
				return PopulateFfFooter(WhichSide, whatToProcess.split(",")[0], matchAllData, 0);
			} else {
				return status;
			}
		} else {
			return status;
		}
	}
	public String populateTarget(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException
	{
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() ==  WhichInning)
			.findAny().orElse(null);
		if(inning == null) {
			return "populateTarget: current inning is NULL";
		}
		
		if(PopulateFfBody(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning)==Constants.OK) {
			status = Constants.OK;
		}
		return status;
	}
	
	public void setFullFrameFooterPosition(int whichside,int footer_omo) 
	{

		switch (config.getBroadcaster()) {
		case Constants.ICC_U19_2023:
		
			String In="",Out="",Change_In="",Change_Out="";
			
			if(whichside == 1) {
				switch(footer_omo) {
				case 1:
					In = "33.5";
					Out = "33.5";
					Change_Out = "33.5";
					break;
				case 2: case 4:
					In = "57.5";
					Out = "57.5";
					Change_Out = "57.5";
					break;
				case 3:
					In = "26.0";
					Out = "26.0";
					Change_Out = "26.0";
					break;
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$FooterDynamic$FooterDynamic_Mask"
						+ "*ANIMATION*KEY*$In*VALUE SET " + In + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$FooterDynamic$FooterDynamic_Mask"
						+ "*ANIMATION*KEY*$Out*VALUE SET " + Out + "\0",print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$FooterDynamic$FooterDynamic_Mask"
						+ "*ANIMATION*KEY*$Change_Out*VALUE SET " + Change_Out + "\0",print_writers);
				
			}
			else if(whichside == 2) {
				switch(footer_omo) {
				case 1:
					Change_In = "33.5";
					break;
				case 2: case 4:
					Change_In = "57.5";
					break;
				case 3:
					Change_In = "26.0";
					break;
				}

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$FooterDynamic$FooterDynamic_Mask"
					+ "*ANIMATION*KEY*$Change_In*VALUE SET " + Change_In + "\0",print_writers);
			}
			break;
		}
		
	}

	public String PopulateFfHeader(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) 
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ICC_U19_2023:
			
			switch (whatToProcess) {
			case "F1": case "F2": case "F4": case "Control_F1": case "Control_F10":
				ground = Grounds.stream().filter(grnd -> grnd.getFullname().contains(matchAllData.getSetup().getVenueName())).findAny().orElse(null);
				if(ground == null) {
					return "PopulateFfHeader: ground name is NULL";
				}
				
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning)
					.findAny().orElse(null);
				if(inning == null) {
					return "PopulateFfHeader: current inning is NULL";
				}
				
				//CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header_Shrink SHOW 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Flag*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
					"$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamGroup() + " - " 
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
				case "F1": case "F4": case "Control_F1": case "Control_F10":
					if(inning.getBatting_team().getTeamName4().equalsIgnoreCase("NEP")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Flag$img_Shadow*ACTIVE SET 0 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" 
						+ WhichSide + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
					if(WhichInning == 1) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side"+ WhichSide + 
								"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side"+ WhichSide + 
								"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
					}
					break;
				case "F2":
					if(inning.getBowling_team().getTeamName4().equalsIgnoreCase("NEP")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Flag$img_Shadow*ACTIVE SET 0 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" 
						+ WhichSide + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() + "\0", print_writers);
					
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
			case "Shift_F10":
				if(inning.getInningNumber() == 1) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header_Shrink SHOW 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$HeaderDataAll$Side" + WhichSide +
							"$Flag$img_Flag*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$HeaderDataAll$Side" + WhichSide +
							"$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side"+ WhichSide + 
							"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$HeaderDataAll$Side" + WhichSide + 
							"$In_Out$Change$Select_HeaderTop$Big_First$txt_Team_1*GEOM*TEXT SET " + inning.getBatting_team().getTeamName1() +"\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$HeaderDataAll$Side" + WhichSide + 
							"$In_Out$Change$Select_HeaderTop$Big_First$txt_Team_2*GEOM*TEXT SET " + inning.getBowling_team().getTeamName1() +"\0", print_writers);
					if(inning.getBatting_team().getTeamName4().equalsIgnoreCase("NEP")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Flag$img_Shadow*ACTIVE SET 0 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$HeaderDataAll$Side" + WhichSide + 
							"$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
					
				}
				else if(inning.getInningNumber()==2) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header_Shrink START \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$HeaderDataAll$Side" + WhichSide +
							"$Flag$img_Flag*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$HeaderDataAll$Side" + WhichSide +
						"$Flag$img_Shadow*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side"+ WhichSide + 
							"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "COMPARISON"+ "\0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
					"$Change$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamGroup() + " - " +
						ground.getCity() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Change$Bottom*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header"
						+ "$Header_Extra$loop$txt_Extra*GEOM*TEXT SET " + "" +"\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header"
						+ "$Header_Extra$loop$txt_Extra2*GEOM*TEXT SET " +  "" +"\0", print_writers);
				
				break;
				
			case "Alt_F9":
				ground = Grounds.stream().filter(grnd -> grnd.getFullname().contains(matchAllData.getSetup().getVenueName())).findAny().orElse(null);
				if(ground == null) {
					return "PopulateFfHeader: ground name is NULL";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header_Shrink SHOW 0.0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Flag*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
					"$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamGroup() + " - " 
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
				if(inning.getBatting_team().getTeamName4().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Flag$img_Shadow*ACTIVE SET 0 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				if(inning.getInningNumber() == 1) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side"+ WhichSide + 
							"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side"+ WhichSide + 
							"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				}
					
				break;
				
			case "Control_d": case "Control_e":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header_Shrink SHOW 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
						"$Flag$img_Flag*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
						"$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
				if(team.getTeamName4().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Flag$img_Shadow*ACTIVE SET 0 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" 
						+ WhichSide + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
				
				if(player.getFirstname() != null && player.getSurname() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Select_HeaderTop$Player_Name$txt_FirstName*GEOM*TEXT SET " + player.getFirstname() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Select_HeaderTop$Player_Name$txt_SecondName*GEOM*TEXT SET " + player.getSurname() + "\0", print_writers);
				}
				else if(player.getFirstname() != null && player.getSurname() == null){
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Select_HeaderTop$Player_Name$txt_FirstName*GEOM*TEXT SET " + player.getFirstname() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Select_HeaderTop$Player_Name$txt_SecondName*ACTIVE SET 0 \0", print_writers);
					
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Change$Bottom*ACTIVE SET 0 \0", print_writers);
					
				break;

			case "Control_F8":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header_Shrink SHOW 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Flag*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
					"$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamGroup() + " - " + ground.getCity() 
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
					if(matchAllData.getSetup().getHomeTeam().getTeamName4().equalsIgnoreCase("NEP")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Flag$img_Shadow*ACTIVE SET 0 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" 
						+ WhichSide + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + 
						matchAllData.getSetup().getHomeTeam().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side"+ WhichSide + 
						"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				}else {
					if(matchAllData.getSetup().getAwayTeam().getTeamName4().equalsIgnoreCase("NEP")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Flag$img_Shadow*ACTIVE SET 0 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" 
						+ WhichSide + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + 
						matchAllData.getSetup().getAwayTeam().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side"+ WhichSide + 
						"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				}
				break;
			case "Shift_K":
				
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES))
					.findAny().orElse(null);
				if(inning == null) {
					return "In Current Partnership PopulateFfHeader: current inning is NULL";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header_Shrink SHOW 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Flag*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 3 \0", print_writers);

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "CURRENT PARTNERSHIP"+ "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Change$Bottom$txt_Subheader*GEOM*TEXT SET " +  inning.getBatting_team().getTeamName1() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Change$Bottom*ACTIVE SET 1 \0", print_writers);
				if(inning.getBatting_team().getTeamName4().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Flag$img_Shadow*ACTIVE SET 0 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				break;
				
			case "m": case "Control_m": case "Shift_F11": case "Control_F7":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Flag*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag$img_Shadow*ACTIVE SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
				
				switch(whatToProcess.split(",")[0]) {
				case "m":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + matchAllData.getSetup().getMatchIdent() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$ICC_Wordmark*GEOM*TEXT SET " + "ICC U19 MEN’S CRICKET WORLD CUP 2024" + "\0", print_writers);
					break;
				
				case "Control_m":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + fixture.getTeamgroup().toUpperCase()+ "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$ICC_Wordmark*GEOM*TEXT SET " + "ICC U19 MEN’S CRICKET WORLD CUP 2024" + "\0", print_writers);
					break;
					
				case "Shift_F11":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "MATCH SUMMARY"+ "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamGroup() + " - " 
							+ ground.getCity() + "\0", print_writers);
					break;
					
				case "Control_F7":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "TEAMS" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamGroup() + " - " 
							+ ground.getCity() + "\0", print_writers);
					break;
				}

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Change$Bottom*ACTIVE SET 1 \0", print_writers);
				
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
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
					+ "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Batting_Card$Rows"
						+ "*FUNCTION*Grid*num_row SET "+inning.getBattingCard().size()+"\0", print_writers);
				for(int iRow = 1; iRow <= inning.getBattingCard().size(); iRow++) {
					switch (inning.getBattingCard().get(iRow-1).getStatus().toUpperCase()) {
					case CricketUtil.STILL_TO_BAT:
						
						if(inning.getBattingCard().get(iRow-1).getHowOut() == null) {

							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Still_To_Bat$Data$Name$txt_BatterName*GEOM*TEXT SET "
								+ inning.getBattingCard().get(iRow-1).getPlayer().getTicker_name() + "\0", print_writers);
							
						} else {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card$Rows$" + iRow + "$BattingData$Select_Row_Type$Out$Data$Name$txt_BatterName*GEOM*TEXT SET "
								+ inning.getBattingCard().get(iRow-1).getPlayer().getTicker_name() + "\0", print_writers);
							
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
								+ inning.getBattingCard().get(iRow-1).getPlayer().getTicker_name() + "\0", print_writers);
						
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
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						"$Bowling_Card$Rows$1$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows"
						+ "*FUNCTION*Grid*num_row SET "+inning.getBattingCard().size()+"\0", print_writers);
				
				for(int j=1;j<=10;j++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$"
							+ (j+1) + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$10"
							+ "$Select_Row_Type$FOW$Data$fig_" + j + "*GEOM*TEXT SET " + "" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$11"
							+ "$Select_Row_Type$Score$Data$fig_" + j + "*GEOM*TEXT SET " + "" + "\0", print_writers);
					
				}
				
				if(inning.getBowlingCard() != null && inning.getBowlingCard().size() > 0) {
					for(int iRow = 1; iRow <= inning.getBowlingCard().size(); iRow++) {
						if(inning.getBowlingCard().get(iRow-1).getRuns() > 0 || 
								((inning.getBowlingCard().get(iRow-1).getOvers()*6)
								+inning.getBowlingCard().get(iRow-1).getBalls()) > 0) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$"
								+ (iRow+1) + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$"
								+ (iRow+1) + "$Select_Row_Type$Players$Data$txt_Name*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getPlayer().getTicker_name() + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$"
									+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Overs*GEOM*TEXT SET " + CricketFunctions.
									OverBalls(inning.getBowlingCard().get(iRow-1).getOvers(), inning.getBowlingCard().get(iRow-1).getBalls()) + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$"
									+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Maidens*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getMaidens() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$"
									+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Runs*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getRuns() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$"
									+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Wickets*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getWickets() + "\0", print_writers);
							
							if(inning.getBowlingCard().get(iRow-1).getEconomyRate() != null) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$"
										+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Economy*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getEconomyRate() + "\0", print_writers);
							}else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$"
										+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Economy*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow-1).getPlayer().getTicker_name() + "\0", print_writers);
							}
						}
					}
				}
				
				if(inning.getBowlingCard().size() <= 8) {
					if(inning.getFallsOfWickets() != null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$10"
								+ "$Select_Row_Type*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$11"
								+ "$Select_Row_Type*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
						
						for(FallOfWicket fow : inning.getFallsOfWickets()) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$"
								+ "10$Select_Row_Type$FOW$Data$fig_" + fow.getFowNumber() + "*GEOM*TEXT SET " + fow.getFowNumber() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$"
								+ "11$Select_Row_Type$Score$Data$fig_" + fow.getFowNumber() + "*GEOM*TEXT SET " + fow.getFowRuns() + "\0", print_writers);
						}
					}
				}
				break;
			}
			break;
		
		case "Control_F1":
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
					+ "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 13 \0", print_writers);
			rowId = 0;
			for(int iRow = 1; iRow <= inning.getBattingCard().size(); iRow++) {
				switch(iRow) {
				case 1: case 2: case 3: case 4: case 5: case 6:
					rowId = iRow;
					containerName = "$Top";
					break;
				case 7: case 8: case 9: case 10: case 11:
					rowId = iRow - 6;
					containerName = "$Bottom";
					break;
				}
				
				if(PlayingXI.get(iRow-1).getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN) || 
						PlayingXI.get(iRow-1).getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")){
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Batting_Card_Image" + 
							containerName + "$Photo_" + rowId + "$Select_Captain*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				}
				else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Batting_Card_Image" + 
							containerName + "$Photo_" + rowId + "$Select_Captain*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				}
				
				switch (inning.getBattingCard().get(iRow-1).getStatus().toUpperCase()) {
				case CricketUtil.STILL_TO_BAT:
					if(inning.getBattingCard().get(iRow-1).getHowOut() == null) {

						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$Batting_Card_Image" + containerName + "$Photo_" + rowId + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Batting_Card_Image" + 
							containerName + "$Photo_" + rowId + "$Gradient$img_Flag*TEXTURE*IMAGE SET " +Constants.ICC_U19_2023_FLAG_PATH + 
							inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +  inning.getBattingCard().get(iRow-1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
							return "Photo not found";
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Batting_Card_Image"
								+ containerName + "$Photo_" + rowId + "$img_Photo*TEXTURE*IMAGE SET " + "\\\\"+config.getPrimaryIpAddress()+"\\" + Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +
								inning.getBattingCard().get(iRow-1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$Batting_Card_Image" + containerName + "$Photo_" + rowId + "$Name$txt_PlayerName*GEOM*TEXT SET "
								+ inning.getBattingCard().get(iRow-1).getPlayer().getTicker_name() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$Batting_Card_Image" + containerName + "$Photo_" + rowId + "$Still_To_Bat$txt_InAt*GEOM*TEXT SET " + "IN AT " + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$Batting_Card_Image" + containerName + "$Photo_" + rowId + "$Still_To_Bat$txt_Position*GEOM*TEXT SET " + iRow + "\0", print_writers);
						
					} else {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card_Image" + containerName + "$Photo_" + rowId + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Batting_Card_Image" + 
								containerName + "$Photo_" + rowId + "$Gradient$img_Flag*TEXTURE*IMAGE SET "+ Constants.ICC_U19_2023_FLAG_PATH + 
								inning.getBatting_team().getTeamName4() + "\0", print_writers);
						
						if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +  inning.getBattingCard().get(iRow-1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
							return "Photo not found";
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Batting_Card_Image"
								+ containerName + "$Photo_" + rowId + "$img_Photo*TEXTURE*IMAGE SET " + "\\\\"+config.getPrimaryIpAddress()+"\\" + Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +
								inning.getBattingCard().get(iRow-1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card_Image" + containerName + "$Photo_" + rowId + "$Name$txt_PlayerName*GEOM*TEXT SET "
									+ inning.getBattingCard().get(iRow-1).getPlayer().getTicker_name() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card_Image" + containerName + "$Photo_" + rowId + "$Out$txt_Runs*GEOM*TEXT SET " + 
									inning.getBattingCard().get(iRow-1).getRuns() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$Batting_Card_Image" + containerName + "$Photo_" + rowId + "$Out$txt_Balls*GEOM*TEXT SET " + 
								inning.getBattingCard().get(iRow-1).getBalls() + "\0", print_writers);

					}
					
					break;
				default:
					switch (inning.getBattingCard().get(iRow-1).getStatus().toUpperCase()) {
					case CricketUtil.OUT:
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card_Image" + containerName + "$Photo_" + rowId + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						containerName_2 = "$Out";
						break;
					case CricketUtil.NOT_OUT:
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$Batting_Card_Image" + containerName + "$Photo_" + rowId + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
						containerName_2 = "$Not_Out";
						break;
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$Batting_Card_Image" + containerName + "$Photo_" + rowId + "$Name$txt_PlayerName*GEOM*TEXT SET "
								+ inning.getBattingCard().get(iRow-1).getPlayer().getTicker_name() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Batting_Card_Image" + 
							containerName + "$Photo_" + rowId + "$Gradient$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + 
							inning.getBatting_team().getTeamName4() + "\0", print_writers);
					
					if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +  inning.getBattingCard().get(iRow-1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
						return "Photo not found";
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Batting_Card_Image"
							+ containerName + "$Photo_" + rowId + "$img_Photo*TEXTURE*IMAGE SET " + "\\\\"+config.getPrimaryIpAddress()+"\\" + Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +
							inning.getBattingCard().get(iRow-1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
					
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$Batting_Card_Image" + containerName + "$Photo_" + rowId + containerName_2 + "$txt_Runs*GEOM*TEXT SET " + 
								inning.getBattingCard().get(iRow-1).getRuns() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
						+ "$Batting_Card_Image" + containerName + "$Photo_" + rowId + containerName_2 + "$txt_Balls*GEOM*TEXT SET " + 
							inning.getBattingCard().get(iRow-1).getBalls() + "\0", print_writers);
					
					break;
				}
			}
			
			break;
			
		case "Shift_F11": //Match Summary
			// inning declared in summary populate section
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
					"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 5 \0", print_writers);
			
			for(int i=1; i<=2; i++ ) {
				for(int j=1; j<=4; j++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_"
							+ i + "$Row_" + j + "*ACTIVE SET 0 \0", print_writers);
				}
			}
			
			for(int i = 1; i <= WhichInning ; i++) {
				if(i == 1) {
					rowId=0;
					if(matchAllData.getMatch().getInning().get(i-1).getBattingTeamId() == matchAllData.getSetup().getTossWinningTeam()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
								+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
								+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 0 \0", print_writers);							}
					
				} else {
					rowId=0;
					if(matchAllData.getMatch().getInning().get(i-1).getBattingTeamId() == matchAllData.getSetup().getTossWinningTeam()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
								+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
								+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					}
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
						+ i +"$Tittle$Data$txt_Team*GEOM*TEXT SET " + matchAllData.getMatch().getInning().get(i-1).getBatting_team().getTeamName1() + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
						+ i +"$Tittle$FlagGrp$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + 
							matchAllData.getMatch().getInning().get(i-1).getBatting_team().getTeamName4() + " \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
					+ i + "$Tittle$Data$Overs$txt_DLS_Value*GEOM*TEXT SET " + "" + " \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
						+ i + "$Tittle$Data$Overs$txt_Overs_Value*GEOM*TEXT SET " + CricketFunctions.OverBalls(matchAllData.getMatch().getInning().get(i-1).
						getTotalOvers(),matchAllData.getMatch().getInning().get(i-1).getTotalBalls()) + " \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
						+ i +"$Tittle$Data$txt_Score*GEOM*TEXT SET " + CricketFunctions.getTeamScore(matchAllData.getMatch().getInning().get(i-1), 
								"-", false) + " \0", print_writers);
				
				if(i==1) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_2*ACTIVE SET 0 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_2*ACTIVE SET 1 \0", print_writers);
				}
				
				if(matchAllData.getMatch().getInning().get(i-1).getBattingCard() != null) {
					Collections.sort(matchAllData.getMatch().getInning().get(i-1).getBattingCard(),new CricketFunctions.BatsmenScoreComparator());
					
					for(BattingCard bc : matchAllData.getMatch().getInning().get(i-1).getBattingCard()) {
						if(bc.getRuns() > 0) {
							rowId++;
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" + i +
									"$Row_" + rowId + "*ACTIVE SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" + i + 
									"$Row_" + rowId + "Batsman*ACTIVE SET 1 \0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" + i + 
									"$Row_" + rowId + "$Batsman$txt_Name*GEOM*TEXT SET " + bc.getPlayer().getTicker_name() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" + i + 
									"$Row_" + rowId + "$Batsman$txt_Runs*GEOM*TEXT SET " + bc.getRuns() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" + i + 
									"$Row_" + rowId + "$Batsman$txt_Balls*GEOM*TEXT SET " + String.valueOf(bc.getBalls()) + "\0", print_writers);

							if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" + i + 
										"$Row_" + rowId + "$Batsman$txt_Not-Out*GEOM*TEXT SET " + "*" + "\0", print_writers);
							} else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" + i + 
										"$Row_" + rowId + "$Batsman$txt_Not-Out*GEOM*TEXT SET " + "" + "\0", print_writers);
							}
							if(i == 1 && rowId >= 4) {
								break;
							}else if(i == 2 && rowId >= 4) {
								break;
							}
						}
					}
				}
				
				for(int j = rowId+1; j <= 4; j++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" + i + 
							"$Row_" + j + "$Batsman*ACTIVE SET 0 \0", print_writers);
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
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" + i + 
									"$Row_" + rowId + "*ACTIVE SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" + i + 
									"$Row_" + rowId + "Bowler*ACTIVE SET 1 \0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" + i + 
									"$Row_" + rowId + "$Bowler$txt_Name*GEOM*TEXT SET " + boc.getPlayer().getTicker_name() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" + i + 
									"$Row_" + rowId + "$Bowler$txt_Figs*GEOM*TEXT SET " + boc.getWickets() + "-" + boc.getRuns() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" + i + 
									"$Row_" + rowId + "$Bowler$txt_Overs*GEOM*TEXT SET " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + "\0", print_writers);
							
							if(i == 1 && rowId >= 4) {
								break;
							}
							else if(i == 2 && rowId >= 4) {
								break;
							}
						}
					}
				}
				for(int j = rowId+1; j <= 4; j++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" + i + 
							"$Row_" + j + "$Bowler*ACTIVE SET 0 \0", print_writers);
				}
			}
			break;
			
		case "F4":
			
			int omo_num=0,Top_Score = 50;
			double Mult = 160,ScaleFac1 = 0, ScaleFac2 = 0;
			String Left_Batsman="", Right_Batsman="";
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + 
					"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 6 \0", print_writers);
			
			if(inning.getPartnerships().size()>=10) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows"
						+ "*FUNCTION*Grid*num_row SET 11 \0", print_writers);
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows"
						+ "*FUNCTION*Grid*num_row SET "+inning.getBattingCard().size()+"\0", print_writers);
			}
			
			
			
			for(int a = 1; a <= inning.getPartnerships().size(); a++){
				if(inning.getPartnerships().get(a-1).getFirstBatterRuns() > Top_Score) {
					Top_Score = inning.getPartnerships().get(a-1).getFirstBatterRuns();
				}
				if(inning.getPartnerships().get(a-1).getSecondBatterRuns() > Top_Score) {
					Top_Score = inning.getPartnerships().get(a-1).getSecondBatterRuns();
				}
			}
			
			rowId = 0;
			for (Partnership ps : inning.getPartnerships()) {
				rowId = rowId + 1;
				Left_Batsman ="" ; Right_Batsman="";
				for (BattingCard bc : inning.getBattingCard()) {
					if(bc.getPlayerId() == ps.getFirstBatterNo()) {
						Left_Batsman = bc.getPlayer().getTicker_name();
					}
					else if(bc.getPlayerId() == ps.getSecondBatterNo()) {
						Right_Batsman = bc.getPlayer().getTicker_name();
					}
				}
				if(inning.getPartnerships().size() >= 10) {
					if(ps.getPartnershipNumber()<=inning.getPartnerships().size()) {
						omo_num = 2;
						containerName = "$Out";
					}
				}
				else {
					if(ps.getPartnershipNumber() < inning.getPartnerships().size()) {
						omo_num = 2;
						containerName = "$Out";
					}
					else if(ps.getPartnershipNumber() >= inning.getPartnerships().size()) {
						omo_num = 3;
						containerName = "$Not_Out";
					}
				}
				
				ScaleFac1 = ((ps.getFirstBatterRuns())*(Mult/Top_Score)) ;
				ScaleFac2 = ((ps.getSecondBatterRuns())*(Mult/Top_Score)) ;
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
					+ rowId+  "$Select_Row_Type*FUNCTION*Omo*vis_con SET " + omo_num + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
					+ rowId + "$Select_Row_Type" + containerName +"$Data$txt_Name_1*GEOM*TEXT SET " + Left_Batsman + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
					+ rowId + "$Select_Row_Type" + containerName +"$Data$txt_Name_2*GEOM*TEXT SET " + Right_Batsman + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
					+ rowId + "$Select_Row_Type" + containerName +"$Data$txt_Runs*GEOM*TEXT SET " + ps.getTotalRuns() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
					+ rowId + "$Select_Row_Type" + containerName +"$Data$txt_Balls*GEOM*TEXT SET " + ps.getTotalBalls() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
					+ rowId  + "$Select_Row_Type" + containerName + "$Geom_Bar_1*GEOM*width SET " + ScaleFac1 + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
					+ rowId  + "$Select_Row_Type" + containerName + "$Geom_Bar_2*GEOM*width SET " + ScaleFac2 + "\0", print_writers);
			}
			
			if(inning.getPartnerships().size() >= 10) {
				rowId = rowId + 1;
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
						+ rowId+  "$Select_Row_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
//						+ rowId+  "$Select_Row_Type$Title$Data$txt_Title*GEOM*TEXT SET " + "DID NOT BAT" + "\0", print_writers);
			}else {
				for (BattingCard bc : inning.getBattingCard()) {
					if(rowId < inning.getBattingCard().size()) {
						if(rowId == inning.getPartnerships().size()) {
							rowId = rowId + 1;
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
								+ rowId+  "$Select_Row_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
							
							if(matchAllData.getMatch().getInning().get(WhichInning - 1).getTotalOvers() == matchAllData.getSetup().getMaxOvers() 
									|| matchAllData.getMatch().getInning().get(WhichInning - 1).getTotalWickets() >= 10 ) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
									+ rowId+  "$Select_Row_Type$Title$Data$txt_Title*GEOM*TEXT SET " + "DID NOT BAT" + "\0", print_writers);
							}
							else if(matchAllData.getSetup().getTargetOvers() != null && !matchAllData.getSetup().getTargetOvers().isEmpty()) {
								if( matchAllData.getMatch().getInning().get(WhichInning - 1).getTotalOvers() == Integer.valueOf(matchAllData.getSetup().getTargetOvers()) 
										|| matchAllData.getMatch().getInning().get(WhichInning - 1).getTotalWickets() >= 10) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
										+ rowId+  "$Select_Row_Type$Title$Data$txt_Title*GEOM*TEXT SET " + "DID NOT BAT" + "\0", print_writers);
								}else if(CricketFunctions.getRequiredRuns(matchAllData) <= 0 || CricketFunctions.getRequiredBalls(matchAllData) <= 0 
										|| CricketFunctions.getWicketsLeft(matchAllData) <= 0) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
										+ rowId+  "$Select_Row_Type$Title$Data$txt_Title*GEOM*TEXT SET " + "DID NOT BAT" + "\0", print_writers);
								}else {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
										+ rowId+  "$Select_Row_Type$Title$Data$txt_Title*GEOM*TEXT SET " + "STILL TO BAT" + "\0", print_writers);
								}
							}
							else if(CricketFunctions.getRequiredRuns(matchAllData) <= 0 || CricketFunctions.getRequiredBalls(matchAllData) <= 0 || 
									CricketFunctions.getWicketsLeft(matchAllData) <= 0) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
									+ rowId+  "$Select_Row_Type$Title$Data$txt_Title*GEOM*TEXT SET " + "DID NOT BAT" + "\0", print_writers);
							}
							else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
									+ rowId+  "$Select_Row_Type$Title$Data$txt_Title*GEOM*TEXT SET " + "STILL TO BAT" + "\0", print_writers);
							}
						}
						else if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
							rowId = rowId + 1;
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
								+ rowId+  "$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$Rows$"
								+ rowId+  "$Select_Row_Type$Still_To_Bat$Data$txt_Name*GEOM*TEXT SET " + bc.getPlayer().getTicker_name() + "\0", print_writers);
						}	
					}
					else {
						break;
					}
				}
			}
			break;
			
		case "Control_F10":
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 11 \0", print_writers);
				
				int maxRuns = 0,runsIncr = 0,powerplay_omo=0;
				double lngth = 0;
				String powerPlay = "";
				
				for (int j = 0; j < manhattan.size(); j++) {
					if(manhattan.get(j).getInningNumber() == WhichInning) {
						if(Integer.valueOf(manhattan.get(j).getOverTotalRuns()) > maxRuns){
							maxRuns = Integer.valueOf(manhattan.get(j).getOverTotalRuns()); // 33 runs came off 34th over
						}
					 	while (maxRuns % 5 != 0) {     // 5 label in y-axis
					 		maxRuns = maxRuns + 1;    // keep incrementing till max runs is divisible by 5. maxRuns = 35
						}
					}
				}
				for(int i = 0; i < 5;i++) {
					runsIncr = maxRuns / 5; // 35/5=7 -> Y axis will be plot like 6,12,18,24,30 & 36
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan$Runs_Axis$"
							+ "$Runs_Data$txt_" + (i+1) + "*GEOM*TEXT SET " + runsIncr*(i+1) + "\0", print_writers);
				}
				
				for(int j = 1; j <= matchAllData.getSetup().getMaxOvers(); j++) {
					
					if(j <= matchAllData.getMatch().getInning().get(WhichInning - 1).getFirstPowerplayEndOver()) {
						powerplay_omo = 0;
						powerPlay = "$PowerPlay_1";
					}
					else if(j >= matchAllData.getMatch().getInning().get(WhichInning - 1).getSecondPowerplayStartOver() && 
							j <= matchAllData.getMatch().getInning().get(WhichInning - 1).getSecondPowerplayEndOver()) {
						powerplay_omo = 1;
						powerPlay = "$PowerPlay_2";
					}
					else if(j >= matchAllData.getMatch().getInning().get(WhichInning - 1).getThirdPowerplayStartOver() && 
							j <= matchAllData.getMatch().getInning().get(WhichInning - 1).getThirdPowerplayEndOver()) {
						powerplay_omo = 2;
						powerPlay = "$PowerPlay_3";
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan$Bar$"
							+ j + "$Select_PowerPlay*FUNCTION*Omo*vis_con SET " + powerplay_omo + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan$Wickets_Axis"
							+ "$Out$Wkt_" + j + "$Select_PowerPlay*FUNCTION*Omo*vis_con SET " + powerplay_omo + "\0", print_writers);
					
					if(j < manhattan.size()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan$Bar$"
								+ "$Position*FUNCTION*Grid*num_row SET 1\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan$Bar$"
								+ "$Position*FUNCTION*Grid*num_col SET " + j + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan$Wickets_Axis$"
								+ "Out$Wickets*FUNCTION*Grid*num_col SET " + j + "\0", print_writers);
						
						lngth = ((382 * Integer.valueOf(manhattan.get(j).getOverTotalRuns())) / maxRuns);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan$Bar$"
								+ j + powerPlay + "$Bar*GEOM*height SET " + lngth + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan$Wickets_Axis"
								+ "$Out$Wkt_" + j + powerPlay + "$Select_Wickets*FUNCTION*Omo*vis_con SET " + manhattan.get(j).getOverTotalWickets() + "\0", print_writers);
					}
				}
				break;
			}
			break;
		
		case "Control_d":  
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				
				if(statsType.getStats_short_name().equalsIgnoreCase(CricketUtil.DT20)) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
							+ "$SubTitle$Data$txt_SubTitle*GEOM*TEXT SET " + "T20 CAREER" + "\0", print_writers);
				}
				else if(statsType.getStats_short_name().equalsIgnoreCase(CricketUtil.IT20)) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
							+ "$SubTitle$Data$txt_SubTitle*GEOM*TEXT SET " + "T20I CAREER" + "\0", print_writers);
				}
				else if(statsType.getStats_short_name().equalsIgnoreCase("U19ODI")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
							+ "$SubTitle$Data$txt_SubTitle*GEOM*TEXT SET " + "U19 ODI CAREER" + "\0", print_writers);				
					}
				else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
							+ "$SubTitle$Data$txt_SubTitle*GEOM*TEXT SET " + statsType.getStats_short_name() + " CAREER" + "\0", print_writers);
				}
				
				for(int i=0; i<2; i++) {
					if(i==0) {
						containerName ="$Normal";
					}else {
						containerName ="$Highlight";
					}
					if(stat.getMatches()==0) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$1$Data$Stat_1" + containerName + "$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$1$Data$Stat_1" + containerName + "$txt_Fig*GEOM*TEXT SET " + stat.getMatches() + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
							+ "$Stats$1$Data$Stat_2" + containerName + "$txt_Desig*GEOM*TEXT SET " + "RUNS" + "\0", print_writers);

					if(stat.getRuns()==0) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$1$Data$Stat_2" + containerName + "$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$1$Data$Stat_2" + containerName + "$txt_Fig*GEOM*TEXT SET " + String.format("%,d\n", stat.getRuns()) + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
							+ "$Stats$2$Data$Stat_1" + containerName + "$txt_Desig*GEOM*TEXT SET " + "FIFTIES" + "\0", print_writers);
					
					if(stat.getFifties()==0) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$2$Data$Stat_1" + containerName + "$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$2$Data$Stat_1" + containerName + "$txt_Fig*GEOM*TEXT SET " + stat.getFifties() + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
							+ "$Stats$2$Data$Stat_2" + containerName + "$txt_Desig*GEOM*TEXT SET " + "HUNDREDS" + "\0", print_writers);
					
					if(stat.getHundreds() == 0) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$2$Data$Stat_2" + containerName + "$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
							+ "$Stats$2$Data$Stat_2" + containerName + "$txt_Fig*GEOM*TEXT SET " + stat.getHundreds() + "\0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
						+ "$Stats$3$Data$Stat_1" + containerName + "$txt_Desig*GEOM*TEXT SET " + "AVERAGE" + "\0", print_writers);

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
						+ "$Stats$3$Data$Stat_1" + containerName + "$txt_Fig*GEOM*TEXT SET " + CricketFunctions.getAverage(stat.getMatches(), stat.getNot_out(), stat.getRuns(), 2, "-") + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
						+ "$Stats$3$Data$Stat_2" + containerName + "$txt_Desig*GEOM*TEXT SET " + "STRIKE RATE" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
						+ "$Stats$3$Data$Stat_2" + containerName + "$txt_Fig*GEOM*TEXT SET " + CricketFunctions.generateStrikeRate(stat.getRuns(), stat.getBalls_faced(), 1) + "\0", print_writers);

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
						+ "$Stats$4$Data$Stat_1" + containerName + "$txt_Desig*GEOM*TEXT SET " + "BEST SCORE" + "\0", print_writers);
					
					if(stat.getBest_score()==null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$4$Data$Stat_1" + containerName + "$Justify$txt_Fig*GEOM*TEXT SET " + "" + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$4$Data$Stat_1" + containerName + "$Justify$txt_Fig*GEOM*TEXT SET " + stat.getBest_score() + "\0", print_writers);
					}
					
					if(stat.getBest_score_against()==null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$4$Data$Stat_1" + containerName + "$Justify$txt_VersusTeam*GEOM*TEXT SET " + "" + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$4$Data$Stat_1" + containerName + "$Justify$txt_VersusTeam*GEOM*TEXT SET " + "v "+ stat.getBest_score_against().toUpperCase() + "\0", print_writers);
					}
					if(stat.getBest_score_venue()==null) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$4$Data$Stat_1" + containerName + "$Justify$txt_VersusLOcation*GEOM*TEXT SET " + "" + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$4$Data$Stat_1" + containerName + "$Justify$txt_VersusLOcation*GEOM*TEXT SET " + "AT "+stat.getBest_score_venue() + "\0", print_writers);
					}
				}
				
				if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 +  player.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
					return "Photo not found";
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$PhotoPart"
						+ "$Photo$img_PlayerPhoto*TEXTURE*IMAGE SET "+"\\\\"+config.getPrimaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 
						+ player.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$PhotoPart"
						+ "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
				break;
			}
			break;
			
		case "Control_e":
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				double average = (double) stat.getRuns_conceded()/stat.getWickets();
				DecimalFormat df = new DecimalFormat("0.00");

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
							"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
					
					if(statsType.getStats_short_name().equalsIgnoreCase(CricketUtil.DT20)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$SubTitle$Data$txt_SubTitle*GEOM*TEXT SET " + "T20 CAREER" + "\0", print_writers);
					}
					else if(statsType.getStats_short_name().equalsIgnoreCase(CricketUtil.IT20)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$SubTitle$Data$txt_SubTitle*GEOM*TEXT SET " + "T20I CAREER" + "\0", print_writers);
					}
					else if(statsType.getStats_short_name().equalsIgnoreCase("U19ODI")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$SubTitle$Data$txt_SubTitle*GEOM*TEXT SET " + "U19 ODI CAREER" + "\0", print_writers);				}
					else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$SubTitle$Data$txt_SubTitle*GEOM*TEXT SET " + statsType.getStats_short_name() + " CAREER" + "\0", print_writers);
					}
					
					for(int i=0; i<2; i++) {
						if(i==0) {
							containerName ="$Normal";
						}else {
							containerName ="$Highlight";
						}
						if(stat.getMatches()==0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$1$Data$Stat_1" + containerName + "$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$1$Data$Stat_1"+ containerName +"$txt_Fig*GEOM*TEXT SET " + stat.getMatches() + "\0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$1$Data$Stat_2$"+ containerName +"$txt_Desig*GEOM*TEXT SET " + "WICKETS" + "\0", print_writers);
						if(stat.getWickets()==0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$1$Data$Stat_2"+ containerName +"$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$1$Data$Stat_2"+ containerName +"$txt_Fig*GEOM*TEXT SET " + stat.getWickets() + "\0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$2$Data$Stat_1"+ containerName +"$txt_Desig*GEOM*TEXT SET " + "3WI" + "\0", print_writers);
						if(stat.getPlus_3()==0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$2$Data$Stat_1"+ containerName +"$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$2$Data$Stat_1"+ containerName +"$txt_Fig*GEOM*TEXT SET " + stat.getPlus_3() + "\0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$2$Data$Stat_2"+ containerName +"$txt_Desig*GEOM*TEXT SET " + "5WI" + "\0", print_writers);
						if(stat.getPlus_5() == 0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$2$Data$Stat_2"+ containerName +"$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$2$Data$Stat_2"+ containerName +"$txt_Fig*GEOM*TEXT SET " + stat.getPlus_5() + "\0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$3$Data$Stat_1"+ containerName +"$txt_Desig*GEOM*TEXT SET " + "AVERAGE" + "\0", print_writers);
						if(average==0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$3$Data$Stat_1"+ containerName +"$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$3$Data$Stat_1"+ containerName +"$txt_Fig*GEOM*TEXT SET " + df.format(average) + "\0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$3$Data$Stat_2"+ containerName +"$txt_Desig*GEOM*TEXT SET " + "ECONOMY" + "\0", print_writers);
						if(stat.getRuns_conceded() == 0 && stat.getBalls_bowled() == 0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$3$Data$Stat_2"+ containerName +"$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$3$Data$Stat_2"+ containerName +"$txt_Fig*GEOM*TEXT SET " + CricketFunctions.getEconomy(stat.getRuns_conceded(), stat.getBalls_bowled(), 2, "-") + "\0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$4$Data$Stat_1"+ containerName +"$txt_Desig*GEOM*TEXT SET " + "BEST FIGURES" + "\0", print_writers);
						
						if(stat.getBest_figures()==null) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$4$Data$Stat_1"+ containerName +"$Justify$txt_Fig*GEOM*TEXT SET " + "" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$4$Data$Stat_1"+ containerName +"$Justify$txt_Fig*GEOM*TEXT SET " + stat.getBest_figures() + "\0", print_writers);
						}
						
						if(stat.getBest_figures_against()==null) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$4$Data$Stat_1"+ containerName +"$Justify$txt_VersusTeam*GEOM*TEXT SET " + "" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$4$Data$Stat_1"+ containerName +"$Justify$txt_VersusTeam*GEOM*TEXT SET " + "v "+ stat.getBest_figures_against().toUpperCase() + "\0", print_writers);
						}
						
						if(stat.getBest_figures_venue()==null) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$4$Data$Stat_1"+ containerName +"$Justify$txt_VersusLOcation*GEOM*TEXT SET " + "" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$4$Data$Stat_1"+ containerName +"$Justify$txt_VersusLOcation*GEOM*TEXT SET " + "AT "+stat.getBest_figures_venue() + "\0", print_writers);
						}
					}
					if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 +  player.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
						return "Photo not found";
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$PhotoPart"
							+ "$Photo$img_PlayerPhoto*TEXTURE*IMAGE SET "+"\\\\"+config.getPrimaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 
							+ player.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
					
				break;
			}
			break;

		case "Control_F7": //Double Teams

			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 9 \0", print_writers);
				
				rowId = 0;
				for(int i = 1; i <= 2 ; i++) {
					if(i == 1) {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$Team_1$Tittle"
								+ "$txt_Tittle*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamName1() + "\0", print_writers);
						
						for(Player hs : matchAllData.getSetup().getHomeSquad()) {
							rowId = rowId + 1;
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$Team_1$" + 
									rowId + "$txt_Name*GEOM*TEXT SET " + hs.getFull_name() + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$Team_1$" + 
									rowId + "$txt_Description*GEOM*TEXT SET " + "" + "\0", print_writers);
							
							if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$Team_1$" + 
										rowId + "$txt_Description*GEOM*TEXT SET " + "(C)" + "\0", print_writers);
							}
							else if(hs.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$Team_1$" + 
										rowId + "$txt_Description*GEOM*TEXT SET " + "(C & WK)" + "\0", print_writers);
							}
							else if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$Team_1$" + 
										rowId + "$txt_Description*GEOM*TEXT SET " + "(WK)" + "\0", print_writers);
							}
						}
					} else {
						rowId = 0;
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$Team_2$Tittle"
								+ "$txt_Tittle*GEOM*TEXT SET " + matchAllData.getSetup().getAwayTeam().getTeamName1().toUpperCase() + "\0", print_writers);
						
						for(Player as : matchAllData.getSetup().getAwaySquad()) {
							rowId = rowId + 1;
							
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$Team_2$" + 
								rowId + "$txt_Name*GEOM*TEXT SET " + as.getFull_name() + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$Team_2$" + 
								rowId + "$txt_Description*GEOM*TEXT SET " + "" + "\0", print_writers);
							
							if(as.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$Team_2$" + 
									rowId + "$txt_Description*GEOM*TEXT SET " + "(C)" + "\0", print_writers);
							}
							else if(as.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$Team_2$" + 
									rowId + "$txt_Description*GEOM*TEXT SET " + "(C & WK)" + "\0", print_writers);
							}
							else if(as.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$Team_2$" + 
									rowId + "$txt_Description*GEOM*TEXT SET " + "(WK)" + "\0", print_writers);
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
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + 
						"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				
				rowId=0;
				for(int i=1;i<=PlayingXI.size();i++) {
					switch(i) {
					case 1: case 2: case 3: case 4: case 5: case 6:
						rowId = i;
						containerName = "$Top";
						break;
					case 7: case 8: case 9: case 10: case 11:
						rowId = i - 6;
						containerName = "$Bottom";
						break;
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
							+ "$Photo_" + rowId + "$Gradient$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
							+ "$Photo_" + rowId + "$Gradient$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
					
					if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +  PlayingXI.get(i-1).getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
						return "Photo not found";
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
							+ "$Photo_" + rowId + "$img_Photo*TEXTURE*IMAGE SET " + "\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 
							+ PlayingXI.get(i-1).getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
							+ "$Photo_" + rowId + "$Name$txt_PlayerName*GEOM*TEXT SET " + PlayingXI.get(i-1).getTicker_name() + "\0", print_writers);
					
					if(PlayingXI.get(i-1).getRole().equalsIgnoreCase(CricketUtil.BATSMAN) || PlayingXI.get(i-1).getRole().equalsIgnoreCase("BAT/KEEPER")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + rowId + "$Select_Role*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + rowId + "$Select_Role$Single_Style1$txt_Bat*GEOM*TEXT SET " + "BAT" + "\0", print_writers);
					}
					else if(PlayingXI.get(i-1).getRole().equalsIgnoreCase(CricketUtil.BOWLER)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + rowId + "$Select_Role*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + rowId + "$Select_Role$Single_Style2$txt_Bowl*GEOM*TEXT SET " + 
								CricketFunctions.getBowlerType(PlayingXI.get(i-1).getBowlingStyle()).replace("PACE", "SEAM") + "\0", print_writers);
					}
					else if(PlayingXI.get(i-1).getRole().equalsIgnoreCase("ALL-ROUNDER")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + rowId + "$Select_Role*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + rowId + "$Select_Role$Double_Style$txt_Bat*GEOM*TEXT SET " + "BAT" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + rowId + "$Select_Role$Double_Style$txt_Bowl*GEOM*TEXT SET " + 
								CricketFunctions.getBowlerType(PlayingXI.get(i-1).getBowlingStyle()).replace("PACE", "SEAM") + "\0", print_writers);
					}
					
					if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)){
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + rowId + "$Select_Captain*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}
					else if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + rowId + "$Select_Captain*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + rowId + "$Select_Role*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + rowId + "$Select_Role$Single_Style2$txt_Bowl*GEOM*TEXT SET " + "KEEPER" + "\0", print_writers);
					}
					else if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + rowId + "$Select_Captain*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + rowId + "$Select_Role*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + rowId + "$Select_Role$Single_Style2$txt_Bowl*GEOM*TEXT SET " + "KEEPER" + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
								+ "$Photo_" + rowId + "$Select_Captain*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					}
				}
				break;
			}
			break;
		
		case "Shift_F10":
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				
				int maxRuns = 0,runsIncr = 0,wkt_count=0;
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
						+ "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 10 \0", print_writers);
				if(inning.getInningNumber() == 1) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$Worms$Select_Team*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$Worms$Graph$Worms$Team_2*ACTIVE SET 0 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer*ACTIVE SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$1_Teams$txt_Tittle*GEOM*TEXT SET "+inning.getBatting_team().getTeamName1()+" \0", print_writers);	
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$1_Teams$txt_Runs*GEOM*TEXT SET "+CricketFunctions.getTeamScore(inning, "-", false)+"\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$1_Teams$txt_Balls*GEOM*TEXT SET "+CricketFunctions.OverBalls(inning.getTotalOvers(), inning.getTotalBalls())+"\0", print_writers);
					maxRuns = inning.getTotalRuns();
				}else if(inning.getInningNumber() == 2) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$Worms$Select_Team*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$Worms$Graph$Worms$Team_2*ACTIVE SET 1 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$2_Teams$Team_1$txt_Tittle*GEOM*TEXT SET "+matchAllData.getMatch().getInning().get(0).getBatting_team().getTeamName1()+"\0", print_writers);	
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$2_Teams$Team_1$txt_Runs*GEOM*TEXT SET "+CricketFunctions.getTeamScore(matchAllData.getMatch().getInning().get(0), "-", false)+"\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$2_Teams$Team_1$txt_Balls*GEOM*TEXT SET "+CricketFunctions.OverBalls(matchAllData.getMatch().getInning().get(0).getTotalOvers(),matchAllData.getMatch().getInning().get(0).getTotalBalls())+"\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$2_Teams$Team_2$txt_Tittle*GEOM*TEXT SET "+matchAllData.getMatch().getInning().get(1).getBatting_team().getTeamName1()+"\0", print_writers);	
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$2_Teams$Team_2$txt_Runs*GEOM*TEXT SET "+CricketFunctions.getTeamScore(matchAllData.getMatch().getInning().get(1), "-", false)+"\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$2_Teams$Team_2$txt_Balls*GEOM*TEXT SET "+CricketFunctions.OverBalls(matchAllData.getMatch().getInning().get(1).getTotalOvers(),matchAllData.getMatch().getInning().get(1).getTotalBalls())+"\0", print_writers);
					if(matchAllData.getMatch().getInning().get(0).getTotalRuns() > matchAllData.getMatch().getInning().get(1).getTotalRuns()) {
						maxRuns = matchAllData.getMatch().getInning().get(0).getTotalRuns();
					}
					else {
						maxRuns = matchAllData.getMatch().getInning().get(1).getTotalRuns();
					}
				}
				
				for(int inn_count = 1; inn_count <= WhichInning; inn_count++) {
					List<String> overByOverRuns = new ArrayList<String>();
					List<String> overByOverwicket = new ArrayList<String>();
					
						overByOverRuns.clear();
						overByOverwicket.clear();
						//wicket_which_over = "";
						for(OverByOverData Over : CricketFunctions.getOverByOverData(matchAllData,inn_count ,"WORM" ,matchAllData.getEventFile().getEvents())) {
							overByOverRuns.add(String.valueOf(Over.getOverTotalRuns()));
						}
						String cumm_runs = String.valueOf(0) + "," + String.join(",", overByOverRuns); // Store Per Overs Runs
						
						if(inn_count == 2) {
							wkt_count=0;
						}
						for(OverByOverData Wicket : CricketFunctions.getOverByOverData(matchAllData,inn_count ,"WORM" ,matchAllData.getEventFile().getEvents())) {
							wkt_count = wkt_count + 1;
							
							if(Wicket.getOverTotalWickets() > 0) {
								for(int w=1; w <= Wicket.getOverTotalWickets(); w++) {
									overByOverwicket.add(String.valueOf(wkt_count-1));
								}
							}
						}
						String cumm_wkts = String.join(",", overByOverwicket); // Store Per Overs Wickets
						
						
						if(matchAllData.getMatch().getInning().get(0).getTotalRuns() > matchAllData.getMatch().getInning().get(1).getTotalRuns()) {
							maxRuns = matchAllData.getMatch().getInning().get(0).getTotalRuns();
						}
						else {
							maxRuns = matchAllData.getMatch().getInning().get(1).getTotalRuns();
						}
						if(maxRuns % 5 == 0) {
							maxRuns = maxRuns + 1;
						}
						while (maxRuns % 5 != 0) {     // 5 label in y-axis
							maxRuns = maxRuns + 1;    // keep incrementing till max runs is divisible by 5. maxRuns = 35
						}
						
						for(int k = 1; k <= 5; k++) {           // For Y-Axis Value 
							runsIncr = maxRuns / 5; // 35/5=7 -> Y axis will be plot like 6,12,18,24,30 & 36	
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Runs_Axis$Runs_Data$txt_"+k+"*GEOM*TEXT SET " +runsIncr * k +"\0", print_writers);	
						}
						
						if(inn_count == 1) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Worms$Worms$Team_"+inn_count+"$"+inn_count+"$AnimWorm*SCRIPT*INSTANCE*strDataY SET "+ cumm_runs.replaceFirst("0,", "") +"\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Worms$Worms$Team_"+inn_count+"$"+inn_count+"$AnimWorm*SCRIPT*INSTANCE*iGraphHeightInRuns SET " + (maxRuns+5) +"\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Worms$Worms$Team_"+inn_count+"$"+inn_count+"$AnimWorm*SCRIPT*INSTANCE*iGraphWidthInOvers SET " + matchAllData.getSetup().getMaxOvers() +"\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Worms$Worms$Team_"+inn_count+"$"+inn_count+"$AnimWorm*SCRIPT*INSTANCE*iNumberOfOversForRandomData SET " + matchAllData.getSetup().getMaxOvers() +"\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Worms$Worms$Team_"+inn_count+"$"+inn_count+"$AnimWorm*SCRIPT*INSTANCE*iSetWorm INVOKE \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Worms$Worms$Team_"+inn_count+"$"+inn_count+"$AnimWorm*SCRIPT*INSTANCE*strWicketsData SET " + cumm_wkts +"\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Worms$Worms$Team_"+inn_count+"$"+inn_count+"$AnimWorm*SCRIPT*INSTANCE*iSetWorm INVOKE \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Worms$Graph$Team_2$Select_Colour*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Worms$Worms$Team_"+inn_count+"$"+inn_count+"$AnimWorm*SCRIPT*INSTANCE*strDataY SET "+ cumm_runs.replaceFirst("0,", "") +"\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Worms$Worms$Team_"+inn_count+"$"+inn_count+"$AnimWorm*SCRIPT*INSTANCE*iGraphHeightInRuns SET " + (maxRuns+5) +"\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Worms$Worms$Team_"+inn_count+"$"+inn_count+"$AnimWorm*SCRIPT*INSTANCE*iGraphWidthInOvers SET " + matchAllData.getSetup().getMaxOvers() +"\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Worms$Worms$Team_"+inn_count+"$"+inn_count+"$AnimWorm*SCRIPT*INSTANCE*iNumberOfOversForRandomData SET " + matchAllData.getSetup().getMaxOvers() +"\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Worms$Worms$Team_"+inn_count+"$"+inn_count+"$AnimWorm*SCRIPT*INSTANCE*iSetWorm INVOKE \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Worms$Worms$Team_"+inn_count+"$"+inn_count+"$AnimWorm*SCRIPT*INSTANCE*strWicketsData SET " + cumm_wkts +"\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Worms$Worms$Team_"+inn_count+"$"+inn_count+"$AnimWorm*SCRIPT*INSTANCE*iSetWorm INVOKE \0", print_writers);								
							}
				}
				
				
				break;
			}
			break;
		
		case "Shift_K":
			if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 +  battingCardList.get(0).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
				return "Photo not found";
			}
			if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 +  battingCardList.get(1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
				return "Photo not found";
			}
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership"
					+ "$Photo$PhotoGrp1$img_PlayerPhoto1*TEXTURE*IMAGE SET " +"\\\\"+config.getPrimaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 
					+ battingCardList.get(0).getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership"
					+ "$Photo$PhotoGrp2$img_PlayerPhoto1*TEXTURE*IMAGE SET " +"\\\\"+config.getPrimaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 
					+ battingCardList.get(1).getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
					+ "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 7 \0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership$1$fig_Runs"
					+ "*GEOM*TEXT SET " + inning.getPartnerships().get(inning.getPartnerships().size() - 1).getTotalRuns() + "*" + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership$1$fig_Balls"
					+ "*GEOM*TEXT SET " + inning.getPartnerships().get(inning.getPartnerships().size() - 1).getTotalBalls() + "\0", print_writers);
			
			if(CricketFunctions.generateStrikeRate(inning.getPartnerships().get(inning.getPartnerships().size() - 1).getTotalRuns(), 
					inning.getPartnerships().get(inning.getPartnerships().size() - 1).getTotalBalls(), 1).contains(".0")) {
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
						+ "$Partnership$2$fig_StrikeRate*GEOM*TEXT SET " + CricketFunctions.generateStrikeRate(inning.getPartnerships().get(inning.
							getPartnerships().size() - 1).getTotalRuns(), inning.getPartnerships().get(inning.getPartnerships().size() - 1).getTotalBalls(),
								1).replace(".0", "") + "\0", print_writers);
				
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
						+ "$Partnership$2$fig_StrikeRate*GEOM*TEXT SET " + CricketFunctions.generateStrikeRate(inning.getPartnerships().get(inning.
							getPartnerships().size() - 1).getTotalRuns(), inning.getPartnerships().get(inning.getPartnerships().size() - 1).getTotalBalls(),
								1) + "\0", print_writers);
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership$4"
					+ "$Data$txt_Name*GEOM*TEXT SET " + battingCardList.get(0).getPlayer().getTicker_name() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership$4"
					+ "$Data$txt_Runs*GEOM*TEXT SET " + battingCardList.get(0).getRuns() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Partnership$4"
					+ "$Data$txt_Ball*GEOM*TEXT SET " + battingCardList.get(0).getBalls() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership$5"
					+ "$Data$txt_Name*GEOM*TEXT SET " + battingCardList.get(1).getPlayer().getTicker_name() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership$5"
					+ "$Data$txt_Runs*GEOM*TEXT SET " + battingCardList.get(1).getRuns() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership$5"
					+ "$Data$txt_Ball*GEOM*TEXT SET " + battingCardList.get(1).getBalls() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership"
				+ "$Photo$PhotoGrp1$img_PlayerShadow1*TEXTURE*IMAGE SET " +"\\\\"+config.getPrimaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + 
					Constants.LEFT_1024 + battingCardList.get(0).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION  + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership"
				+ "$Photo$PhotoGrp2$img_PlayerShadow1*TEXTURE*IMAGE SET " +"\\\\"+config.getPrimaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + 
				battingCardList.get(1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership"
				+ "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
			break;
			
		case "Alt_F9":
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
						+ "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 8 \0", print_writers);
				
				if(WhichStyle.equalsIgnoreCase("Age")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$TeamSingle$Tittle$txt_Tittle_1*GEOM*TEXT SET " + "AGE" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$TeamSingle$Tittle$txt_Tittle_2*GEOM*TEXT SET " + "MATCHES" + "\0", print_writers);
				}
				else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$TeamSingle$Tittle$txt_Tittle_1*GEOM*TEXT SET " + "MATCHES" + "\0", print_writers);
					switch(WhichType.toUpperCase()) {
					case "RUNS":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$TeamSingle$Tittle$txt_Tittle_2*GEOM*TEXT SET " + "RUNS" + "\0", print_writers);
						break;
					case "AVERAGE":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$TeamSingle$Tittle$txt_Tittle_2*GEOM*TEXT SET " + "AVERAGE" + "\0", print_writers);
						break;
					case "STRIKERATE":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$TeamSingle$Tittle$txt_Tittle_2*GEOM*TEXT SET " + "STRIKE RATE" + "\0", print_writers);
						break;
					case "WICKETS":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$TeamSingle$Tittle$txt_Tittle_2*GEOM*TEXT SET " + "WICKETS" + "\0", print_writers);
						break;
					case "ECONOMY":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$TeamSingle$Tittle$txt_Tittle_2*GEOM*TEXT SET " + "ECONOMY" + "\0", print_writers);
						break;
					}
				}
				
				rowId=0;
				
				for(int i=1;i<=PlayingXI.size();i++) {
					rowId = i-1;
					stat = statistics.stream().filter(stat -> stat.getPlayer_id() == PlayingXI.get(rowId).getPlayerId()).findAny().orElse(null);
					if(stat == null) {
						return "populatePlayerProfile: No stats found for player id [" + player.getPlayerId() + "] from database is returning NULL";
					}
					
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
						+ "$TeamSingle$"+i+"$txt_Name*GEOM*TEXT SET " + PlayingXI.get(i-1).getFull_name() + "\0", print_writers);
				 
					if(PlayingXI.get(i-1).getRole().equalsIgnoreCase(CricketUtil.BATSMAN) || PlayingXI.get(i-1).getRole().equalsIgnoreCase("BAT/KEEPER")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$TeamSingle$"+i+"$txt_Description*GEOM*TEXT SET "+"BATTER"+"\0", print_writers);
					}
					else if(PlayingXI.get(i-1).getRole().equalsIgnoreCase(CricketUtil.BOWLER)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$TeamSingle$"+i+"$txt_Description*GEOM*TEXT SET "+"BOWLER"+"\0", print_writers);
					}
					else if(PlayingXI.get(i-1).getRole().equalsIgnoreCase("ALL-ROUNDER")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$TeamSingle$"+i+"$txt_Description*GEOM*TEXT SET "+"ALL-ROUNDER"+"\0", print_writers);
					}
					
					if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)){
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$TeamSingle$"+i+"$txt_Captain*GEOM*TEXT SET "+"(C)"+"\0", print_writers);
					}
					else if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$TeamSingle$"+i+"$txt_Captain*GEOM*TEXT SET "+""+"\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$TeamSingle$"+i+"$txt_Description*GEOM*TEXT SET "+"KEEPER"+"\0", print_writers);
					}
					else if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$TeamSingle$"+i+"$txt_Captain*GEOM*TEXT SET "+"(C)"+"\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$TeamSingle$"+i+"$txt_Description*GEOM*TEXT SET "+"KEEPER"+"\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$TeamSingle$"+i+"$txt_Captain*GEOM*TEXT SET "+""+"\0", print_writers);
					}
					
					if(WhichStyle.equalsIgnoreCase("Age")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$TeamSingle$" + i + "$fig_1*GEOM*TEXT SET " + PlayingXI.get(i-1).getAge() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$TeamSingle$" + i + "$fig_2*GEOM*TEXT SET " + stat.getMatches() + "\0", print_writers);
					}
					else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$TeamSingle$" + i + "$fig_1*GEOM*TEXT SET " + stat.getMatches() + "\0", print_writers);
						switch(WhichType.toUpperCase()) {
						case "RUNS":
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$TeamSingle$" + i + "$fig_2*GEOM*TEXT SET " + stat.getRuns() + "\0", print_writers);
							break;
						case "AVERAGE":
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$TeamSingle$" + i + "$fig_2*GEOM*TEXT SET " + CricketFunctions.getAverage(stat.getMatches(), stat.getNot_out(), 
											stat.getRuns(), 1, "-") + "\0", print_writers);
							break;
						case "STRIKERATE":
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$TeamSingle$" + i + "$fig_2*GEOM*TEXT SET " + CricketFunctions.generateStrikeRate(stat.getRuns(), stat.getBalls_faced(), 1) + 
									"\0", print_writers);
							break;
						case "WICKETS":
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$TeamSingle$" + i + "$fig_2*GEOM*TEXT SET " + stat.getWickets() + "\0", print_writers);
							break;
						case "ECONOMY":
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$TeamSingle$" + i + "$fig_2*GEOM*TEXT SET " + CricketFunctions.getEconomy(stat.getRuns_conceded(), stat.getBalls_bowled(), 2, "-") 
									+ "\0", print_writers);
							break;
						}
					}
				}
				break;
			}
			break;
			
		case "Shift_D":
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$FlagGrp1$img_Flag*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$FlagGrp2$img_Flag*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + inning.getBowling_team().getTeamName4() + "\0", print_writers);
				
				if(inning.getBattingTeamId() == matchAllData.getSetup().getHomeTeamId()) {
					for(Player hs : matchAllData.getSetup().getHomeSquad()) {
						if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)||hs.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
							if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 + hs.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
								return "Photo not found for home player in DB !";
							}
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerGrp1$img_Player*TEXTURE*IMAGE SET " 
									+"\\\\"+config.getPrimaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 + hs.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
						}
					}
					for(Player as : matchAllData.getSetup().getAwaySquad()) {
						if(as.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)||as.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
							if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 + as.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
								return "Photo not found for away player in DB !";
							}
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerGrp2$img_Player*TEXTURE*IMAGE SET " 
									+"\\\\"+config.getPrimaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 + as.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
						}
					}
				}else if(inning.getBattingTeamId() == matchAllData.getSetup().getAwayTeamId()){
					for(Player hs : matchAllData.getSetup().getHomeSquad()) {
						if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)||hs.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
							if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 + hs.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
								return "Photo not found for home player in DB !";
							}
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerGrp2$img_Player*TEXTURE*IMAGE SET " 
									+"\\\\"+config.getPrimaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 + hs.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
						}
					}
					for(Player as : matchAllData.getSetup().getAwaySquad()) {
						if(as.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)||as.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
							if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 + as.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
								return "Photo not found for home player in DB !";
							}
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerGrp1$img_Player*TEXTURE*IMAGE SET " 
									+"\\\\"+config.getPrimaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 + as.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
						}
					}
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$txt_TeamName*GEOM*TEXT SET " + inning.getBatting_team().getTeamName1() + " \0", print_writers);
				
				
				if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER) && matchAllData.getSetup().getMaxOvers() == 1) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs1*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs2*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$InfoGrp$txt_Info1*GEOM*TEXT SET " + "FROM " + (matchAllData.getSetup().getMaxOvers()*6) + " BALLS" + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$InfoGrp$txt_Info2*GEOM*TEXT SET " + "" + " \0", print_writers);
				}else {
					if(matchAllData.getSetup().getTargetOvers() == "" || matchAllData.getSetup().getTargetOvers().trim().isEmpty() && matchAllData.getSetup().getTargetRuns() == 0) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs1*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs2*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$InfoGrp$txt_Info1*GEOM*TEXT SET " + "FROM " + matchAllData.getSetup().getMaxOvers() + " OVERS" + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$InfoGrp$txt_Info2*GEOM*TEXT SET " + "AT "+ CricketFunctions.generateRunRate(CricketFunctions.getRequiredRuns(matchAllData),
								0,CricketFunctions.getRequiredBalls(matchAllData),2,matchAllData) + " RUNS PER OVER" + " \0", print_writers);

					}else {
						if(matchAllData.getSetup().getTargetOvers() != "") {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs1*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs2*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$InfoGrp$txt_Info1*GEOM*TEXT SET " + "FROM " + matchAllData.getSetup().getMaxOvers() + " OVERS" + " \0", print_writers);
						}
						if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("VJD")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs1*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs2*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$InfoGrp$txt_Info1*GEOM*TEXT SET " + "FROM " + matchAllData.getSetup().getMaxOvers() + " OVERS (VJD)" + " \0", print_writers);
							
						}else if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase("DLS")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs1*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs2*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$InfoGrp$txt_Info1*GEOM*TEXT SET " + "FROM " + matchAllData.getSetup().getMaxOvers() + " OVERS (DLS)" + " \0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$InfoGrp$txt_Info2*GEOM*TEXT SET " + "AT "+ CricketFunctions.generateRunRate(CricketFunctions.getRequiredRuns(matchAllData),
								0,CricketFunctions.getRequiredBalls(matchAllData),2,matchAllData) + " RUNS PER OVER" + " \0", print_writers);
					}
				}
				break;
			}
			
			break;
		
		case "m": //MATCH Ident
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
						+ "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
						+ "$MatchIdent$Text$Data$Select_DataForPromo*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
						+ "$MatchIdent$Text$Data$txt_Team_1*GEOM*TEXT SET " + 
						matchAllData.getSetup().getHomeTeam().getTeamName1().toUpperCase() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
						+ "$MatchIdent$Text$Data$txt_Team_2*GEOM*TEXT SET " + 
						matchAllData.getSetup().getAwayTeam().getTeamName1().toUpperCase() + "\0", print_writers);
				
				if(matchAllData.getSetup().getHomeTeam().getTeamName4().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Flag$img_Shadow*ACTIVE SET 0 \0", print_writers);
				}else if(matchAllData.getSetup().getAwayTeam().getTeamName4().equalsIgnoreCase("NEP")){
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Flag$img_Shadow*ACTIVE SET 0 \0", print_writers);
				}else{
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
				}
				if(matchAllData.getSetup().getHomeTeam().getTeamName4().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$MatchIdent$Flag_Top$"
							+ "$img_Shadow*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$MatchIdent$Flag_Bottom$"
							+ "$img_Shadow*ACTIVE SET 1 \0", print_writers);
				}else if(matchAllData.getSetup().getAwayTeam().getTeamName4().equalsIgnoreCase("NEP")){
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$MatchIdent$Flag_Top$"
							+ "$img_Shadow*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$MatchIdent$Flag_Bottom$"
							+ "$img_Shadow*ACTIVE SET 0 \0", print_writers);
				}else{
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$MatchIdent$Flag_Top$"
							+ "$img_Shadow*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$MatchIdent$Flag_Bottom$"
						+ "$img_Shadow*ACTIVE SET 1 \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
						+ "$MatchIdent$Flag_Top$img_Flag*TEXTURE*IMAGE SET " + 
						Constants.ICC_U19_2023_FLAG_PATH +  matchAllData.getSetup().getHomeTeam().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
						+ "$MatchIdent$Flag_Bottom$img_Flag*TEXTURE*IMAGE SET " + 
						Constants.ICC_U19_2023_FLAG_PATH +  matchAllData.getSetup().getAwayTeam().getTeamName4() + "\0", print_writers);
				break;
			}
			break;
			
		case "Control_m": //MATCH PROMO
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023:
				
				if(fixture.getLocalTime() == null || fixture.getGmtTime() == null) {
					return "Fixture local time and GMT time cannot be NULL";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
					"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$MatchIdent$Text"
					+ "$Data$Select_DataForPromo*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$MatchIdent$Text"
					+ "$Data$Select_DataForPromo*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$MatchIdent$Text"
					+ "$Data$Select_DataForPromo$ExtraInfo$txt_Info1*GEOM*TEXT SET " + new SimpleDateFormat("EEEE").format(new SimpleDateFormat("dd-MM-yyyy").
						parse(fixture.getDate())).toUpperCase() + ", " + CricketFunctions.ordinal(Integer.valueOf(Integer.valueOf(fixture.getDate().split("-")[0]))) + 
							" " + new SimpleDateFormat("MMM").format(new SimpleDateFormat("dd-MM-yyyy").parse(fixture.getDate())).toUpperCase() + " " +
								fixture.getDate().split("-")[2] + "\0", print_writers);

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$MatchIdent$Text"
					+ "$Data$Select_DataForPromo$ExtraInfo$txt_Info2*GEOM*TEXT SET " + fixture.getLocalTime() + " LOCAL TIME " 
					+ "(" + fixture.getGmtTime() + " GMT)" + "\0", print_writers);
				
				for(Team tm : Teams) {
					if(tm.getTeamId() == fixture.getHometeamid()) {
						if(tm.getTeamName4().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$MatchIdent$Flag_Top$"
									+ "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$MatchIdent$Flag_Top$"
								+ "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$MatchIdent$Text"
							+ "$Data$txt_Team_1*GEOM*TEXT SET "+ tm.getTeamName1() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$MatchIdent$Flag_Top$"
							+ "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  tm.getTeamName4() + "\0", print_writers);
					}
					
					if(tm.getTeamId() == fixture.getAwayteamid()) {
						if(tm.getTeamName4().equalsIgnoreCase("NEP")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$MatchIdent$Flag_Bottom$"
									+ "$img_Shadow*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$MatchIdent$Flag_Bottom$"
								+ "$img_Shadow*ACTIVE SET 1 \0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$MatchIdent$Text"
							+ "$Data$txt_Team_2*GEOM*TEXT SET " + tm.getTeamName1() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$MatchIdent$Flag_Bottom$"
							+ "$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  tm.getTeamName4() + "\0", print_writers);
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
			case "F1": case "F2": case "F4": case "Control_F1": case "Control_F10": case "Shift_K":
				// Inning for these Gfx are declared in PopulateFfHeader

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Side" + WhichSide + "$Select_FooterType"
						+ "*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Side" + WhichSide + "$Select_FooterType$Score"
						+ "$In_Out$Extras$txt_Extras_Value*GEOM*TEXT SET " + inning.getTotalExtras() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Side"+WhichSide+"$Select_FooterType$Score"
						+ "$In_Out$Overs$txt_Overs_Value*GEOM*TEXT SET " + 
						CricketFunctions.OverBalls(inning.getTotalOvers(),inning.getTotalBalls()) + "\0", print_writers);
				
				if(matchAllData.getSetup().getTargetOvers() != null) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Side"+WhichSide+"$Select_FooterType$Score"
							+ "$In_Out$Overs$txt_Overs_2*GEOM*TEXT SET " + matchAllData.getSetup().getTargetOvers() + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Side"+WhichSide+"$Select_FooterType$Score"
							+ "$In_Out$Overs$txt_Overs_2*GEOM*TEXT SET " + "" + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Side"+WhichSide+"$Select_FooterType$Score"
						 + "$In_Out$Score$txt_Score*GEOM*TEXT SET " + CricketFunctions.getTeamScore(inning, "-", false) + "\0", print_writers);

				switch (whatToProcess) {
				case "F4": case "Shift_K":
					if(whichSponsor.equalsIgnoreCase("COCACOLA")) {
						CricketFunctions.DoadWriteCommandToSelectedViz(1,"-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor$Select_Logo"
							+ "*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToSelectedViz(1,"-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor$Select_Logo"
							+ "*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					}
					if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
						CricketFunctions.DoadWriteCommandToSelectedViz(2,"-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Sponsor$Select_Logo"
							+ "*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					}
					break;
				}
				break;
				
			case "Shift_F11": case "Shift_F10": case "Alt_F9": //MATCH SUMMARY
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Side" + WhichSide + "$Select_FooterType"
						+ "*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				
				switch (whatToProcess.split(",")[0]) {
				case "Shift_F10":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Top_Align$Side" + WhichSide + "$Select_FooterType"
							+ "$Info_Text$Data$txt_Info_1*GEOM*TEXT SET " +CricketFunctions.
							generateMatchSummaryStatus(inning.getInningNumber(), matchAllData,CricketUtil.FULL).toUpperCase() + "\0", print_writers);
					break;
				case "Shift_F11":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Top_Align$Side" + WhichSide + "$Select_FooterType"
							+ "$Info_Text$Data$txt_Info_1*GEOM*TEXT SET " +CricketFunctions.
							generateMatchSummaryStatus(WhichInning, matchAllData,CricketUtil.FULL).toUpperCase() + "\0", print_writers);
					break;
				case "Alt_F9":
					if(WhichStyle.equalsIgnoreCase("Age")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Top_Align$Side" + WhichSide + "$Select_FooterType"
								+ "$Info_Text$Data$txt_Info_1*GEOM*TEXT SET " + statsType.getStats_full_name() + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Top_Align$Side" + WhichSide + "$Select_FooterType"
								+ "$Info_Text$Data$txt_Info_1*GEOM*TEXT SET " + statsType.getStats_full_name() + " - " + WhichStyle.toUpperCase() +"\0", print_writers);
					}
					break;
				}
				break;

			case "Control_d": case "Control_e":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer*ACTIVE SET 0 \0", print_writers);
				break;

			 case "Control_F7": case "Control_F8": //PlayingXI
				 
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Side" + WhichSide + "$Select_FooterType"
						+ "*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Top_Align$Side"+WhichSide+"$Select_FooterType$Info_Text$Data"
						+ "$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.generateTossResult(matchAllData, CricketUtil.FULL, CricketUtil.FIELD, CricketUtil.FULL, 
					CricketUtil.ELECTED).toUpperCase() + "\0", print_writers);
				break;
			
			case "m": case "Control_m": //MATCH ID
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Side" + WhichSide + "$Select_FooterType"
						+ "*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
				
				switch (whatToProcess) {
				case "m":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Top_Align$Side" + WhichSide + "$Select_FooterType$"
							+ "Ident$Data$txt_Info_1*GEOM*TEXT SET " + "LIVE FROM " + matchAllData.getSetup().getVenueName() + "\0", print_writers);
					break;
				case "Control_m":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Top_Align$Side" + WhichSide + "$Select_FooterType$"
							+ "Ident$Data$txt_Info_1*GEOM*TEXT SET " + "LIVE FROM " + fixture.getVenue() + "\0", print_writers);
					break;
				}
				break;
			}
			break;
		}
		return Constants.OK;
	}
 
}