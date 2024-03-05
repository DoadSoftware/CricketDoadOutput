package com.cricket.captions;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.cricket.model.BattingCard;
import com.cricket.model.BestStats;
import com.cricket.model.BowlingCard;
import com.cricket.model.Configuration;
import com.cricket.model.FallOfWicket;
import com.cricket.model.Fixture;
import com.cricket.model.Ground;
import com.cricket.model.Inning;
import com.cricket.model.LeagueTable;
import com.cricket.model.MatchAllData;
import com.cricket.model.OverByOverData;
import com.cricket.model.POTT;
import com.cricket.model.Partnership;
import com.cricket.model.Player;
import com.cricket.model.Statistics;
import com.cricket.model.StatsType;
import com.cricket.model.Team;
import com.cricket.model.Tournament;
import com.cricket.model.VariousText;
import com.cricket.util.CricketFunctions;
import com.cricket.util.CricketUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class FullFramesGfx 
{
	public int FirstPlayerId, rowId = 0, rowId1 = 0, plyr_count=0, numberOfRows = 0,omo=0;
	public String WhichScoreCard, WhichProfile, WhichStyle, WhichType, WhichGroup,status = "", containerName = "",
			containerName_2 = "",containerName_3 = "",containerName_4 = "";
	
	public List<PrintWriter> print_writers; 
	public Configuration config;
	public List<Statistics> statistics;
	public List<StatsType> statsTypes;
	public List<Tournament> tournaments;
	public List<MatchAllData> tournament_matches;
	public List<Fixture> fixTures;
	public List<Team> Teams;
	public List<Ground> Grounds;
	public List<VariousText> VariousText;
	public List<Player> Players;
	public List<POTT> Potts;
	public List<String> TeamChanges;
	
	public List<Tournament> addPastDataToCurr = new ArrayList<Tournament>();
	public List<BestStats> top_batsman_beststats,top_bowler_beststats = new ArrayList<BestStats>();
	public List<Player> PlayingXI,otherSquad;

	public BattingCard battingCard;
	public Inning inning;
	public Player player;
	public Statistics stat;
	public StatsType statsType;
	public VariousText variousText;
	public Tournament tournament;
	
	public Fixture fixture;
	public Team team;
	public Ground ground;
	public LeagueTable leagueTable;
	public String whichSponsor;
	
	public List<BattingCard> battingCardList = new ArrayList<BattingCard>();
	public List<OverByOverData> manhattan, manhattan2 = new ArrayList<OverByOverData>();
	public List<Statistics> statisticsList = new ArrayList<Statistics>();
	public List<Tournament> tournament_stats = new ArrayList<Tournament>();
	public List<MatchAllData> previous_match = new ArrayList<MatchAllData>();
	public List<String> team_change = new ArrayList<String>();
	
	public FullFramesGfx() {
		super();
	}	
	
	public FullFramesGfx(List<PrintWriter> print_writers, Configuration config, List<Statistics> statistics, List<StatsType> statsTypes, 
			List<MatchAllData> tournament_matches,List<Fixture> fixTures, List<Team> Teams, List<Ground> Grounds,List<Tournament> tournaments, 
			List<VariousText> VariousText, List<Player> players, List<POTT> pott, List<String> teamChanges) {
		super();
		this.print_writers = print_writers;
		this.config = config;
		this.statistics = statistics;
		this.statsTypes = statsTypes;
		this.tournament_matches = tournament_matches;
		this.fixTures = fixTures;
		this.Teams = Teams;
		this.Grounds = Grounds;
		this.tournaments = tournaments;
		this.VariousText = VariousText;
		this.Players = players;
		this.Potts = pott;
		this.TeamChanges = teamChanges;
	}
	
	public String populatePOTT(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException, JsonMappingException, JsonProcessingException, InterruptedException 
	{
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
	public String populateDoubleManhattan(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException, InterruptedException
	{
		manhattan = new ArrayList<OverByOverData>();
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES) && WhichInning == 2)
				.findAny().orElse(null);
		if(inning == null) {
			return "populateDoubleManhattan: Works for inning 2 only";
		}
		ground = Grounds.stream().filter(grnd -> grnd.getFullname().contains(matchAllData.getSetup().getVenueName())).findAny().orElse(null);
		if(ground == null) {
			return "populateDoubleManhattan: Ground Name [" + matchAllData.getSetup().getVenueName() + "] from database is returning NULL";
		}
		manhattan = CricketFunctions.getOverByOverData(matchAllData, 1,"MANHATTAN" ,matchAllData.getEventFile().getEvents());
		manhattan2 = CricketFunctions.getOverByOverData(matchAllData, 2,"MANHATTAN" ,matchAllData.getEventFile().getEvents());
		if(manhattan == null) {
			return "populateDoubleManhattan is null";
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
	public String PopulateScorecardFF(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException, InterruptedException
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
				setFullFrameBaseAndTextColor(WhichSide, whatToProcess);
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
	public String PopulatePhotoScorecardFF(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException, InterruptedException
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
	public String PopulateBowlingCardFF(int WhichSide,String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException, InterruptedException
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
				setFullFrameBaseAndTextColor(WhichSide, whatToProcess);
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
	public String populateFFMatchId(int WhichSide, String whatToProcess, MatchAllData matchAllData) throws ParseException, InterruptedException, UnsupportedAudioFileException, IOException
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
	public String populateMatchSummary(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException, InterruptedException
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
	public String populatePreviousMatchSummary(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException, InterruptedException
	{
		previous_match.clear();
		fixture = fixTures.stream().filter(fix -> fix.getMatchnumber() == Integer.valueOf(whatToProcess.split(",")[2])).findAny().orElse(null);
		
		if(fixture == null) {
			return "Fixture id [" + Integer.valueOf(whatToProcess.split(",")[2]) + "] from database is returning NULL";
		}
		
		for(MatchAllData cricket_match : tournament_matches) {
			if(cricket_match.getMatch().getMatchFileName().replace(".json", "").equalsIgnoreCase(fixture.getMatchfilename()) 
					&& cricket_match.getSetup().getHomeTeam().getTeamId() == fixture.getHometeamid() 
					&& cricket_match.getSetup().getAwayTeam().getTeamId() == fixture.getAwayteamid())
			{
				previous_match.add(cricket_match);
			}
		}
		
		ground = Grounds.stream().filter(grnd -> grnd.getFullname().contains(previous_match.get(previous_match.size()-1).getSetup().getVenueName())).findAny().orElse(null);
		if(ground == null) {
			return "populatePreviousMatchSummary: Ground Name [" + previous_match.get(previous_match.size()-1).getSetup().getVenueName() + 
					"] from database is returning NULL";
		}
		
		inning = previous_match.get(previous_match.size()-1).getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == 2).findAny().orElse(null);
		if(inning == null) {
			return "populatePreviousMatchSummary: current inning is NULL";
		}
		
		status = PopulateFfHeader(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
			if(status == Constants.OK) {
				setFullFrameFooterPosition(WhichSide, 2);
				this.numberOfRows = 11;
				return PopulateFfFooter(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
			} else {
				return status;
			}
		} else {
			return status;
		}
	}
	public String populateFFMatchPromo(int WhichSide, String whatToProcess, MatchAllData matchAllData) throws NumberFormatException, ParseException, InterruptedException
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
	public String populatePlayingXI(int WhichSide, String whatToProcess,int teamId,MatchAllData matchAllData, int WhichInning) throws ParseException, InterruptedException
	{
		if(teamId == matchAllData.getSetup().getHomeTeamId()) {
			PlayingXI = matchAllData.getSetup().getHomeSquad();
			otherSquad = matchAllData.getSetup().getHomeSubstitutes();
		}else {
			PlayingXI = matchAllData.getSetup().getAwaySquad();
			otherSquad = matchAllData.getSetup().getAwaySubstitutes();
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
				setFullFrameBaseAndTextColor(WhichSide, whatToProcess);
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
	public String populatePartnership(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException, InterruptedException 
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
				setFullFrameBaseAndTextColor(WhichSide, whatToProcess);
				setFullFrameFooterPosition(WhichSide, 1);
				if(inning.getPartnerships().size() >= 10) {
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
	public String populateThisSeries(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException, InterruptedException, IOException 
	{
		
		FirstPlayerId = Integer.valueOf(whatToProcess.split(",")[2]);
		
		player = CricketFunctions.getPlayerFromMatchData(FirstPlayerId, matchAllData);
		if(player == null) {
			return "populatePlayerProfile: Player id [" + whatToProcess.split(",")[2] + "] from database is returning NULL";
		}
		
		addPastDataToCurr = CricketFunctions.extractTournamentStats("CURRENT_MATCH_DATA", false, tournament_matches, null, matchAllData, tournaments);
		
		if(addPastDataToCurr == null) {
			return "addPastDataToCurr is Null";
		}
		
		top_batsman_beststats = new ArrayList<BestStats>();
		top_bowler_beststats = new ArrayList<BestStats>();
		for(Tournament tourn : addPastDataToCurr) {
			for(BestStats bs : tourn.getBatsman_best_Stats()) {
				top_batsman_beststats.add(bs);
			}
			for(BestStats bfig : tourn.getBowler_best_Stats()) {
				top_bowler_beststats.add(bfig);
			}
		}
		
		Collections.sort(top_batsman_beststats, new CricketFunctions.PlayerBestStatsComparator());
		Collections.sort(top_bowler_beststats, new CricketFunctions.PlayerBestStatsComparator());
		
		tournament = addPastDataToCurr.stream().filter(tourn -> tourn.getPlayerId() == player.getPlayerId()).findAny().orElse(null);
		if(tournament == null) {
			return "ThisSeries : Tournament Stats is Null";
		}
		
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
	public String PopulateDoubleTeams(int WhichSide, String whatToProcess,MatchAllData matchAllData) throws ParseException, InterruptedException
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
	public String populateCurrPartnership(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException, InterruptedException
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
	public String populateManhattan(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException, InterruptedException
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
	public String populateWorms(int WhichSide, String whatToProcess, MatchAllData matchAllData,int WhichInning) throws ParseException, InterruptedException
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
	public String populateSingleTeamsCareer(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException, IOException, InterruptedException
	{	
		WhichProfile = "U19ODI";
		WhichStyle = whatToProcess.split(",")[3];
		WhichType = whatToProcess.split(",")[4];
		
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
			stat.setStats_type(statsType);
			stat = CricketFunctions.updateTournamentDataWithStats(stat, tournament_matches, matchAllData);
			stat = CricketFunctions.updateStatisticsWithMatchData(stat, matchAllData);
			statisticsList.add(stat);
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
	public String populateSingleTeamsThisSeries(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException, IOException, InterruptedException
	{	
		
		WhichProfile = "TS";
		WhichStyle = "";
		WhichType = whatToProcess.split(",")[4];
		
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

		tournament_stats = CricketFunctions.extractTournamentStats("CURRENT_MATCH_DATA", false, tournament_matches, null, matchAllData, tournaments);
		
		if(tournament_stats == null) {
			return "populateSingleTeamsThisSeries : Tournament Stats is Null";
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
	public String populateTarget(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException, InterruptedException
	{
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning)
			.findAny().orElse(null);
		if(inning == null ) {
			return "populateTarget: inning is null";
		}
		
		return PopulateFfBody(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
	}
	public String populateFFPointsTable(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException, JAXBException, InterruptedException
	{
		if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.LEAGUE_TABLE_DIRECTORY + WhichGroup + CricketUtil.XML_EXTENSION).exists()) {
			leagueTable = (LeagueTable)JAXBContext.newInstance(LeagueTable.class).createUnmarshaller().unmarshal(
					new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.LEAGUE_TABLE_DIRECTORY + WhichGroup + CricketUtil.XML_EXTENSION));
		}
		if(leagueTable == null) {
			return "populateFFPointsTable : League Table is null";
		}
		
		status = PopulateFfHeader(WhichSide, whatToProcess, matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess, matchAllData, WhichInning);
			if(status == Constants.OK) {
				setFullFrameFooterPosition(WhichSide, 4);
				this.numberOfRows = 11;
				return PopulateFfFooter(WhichSide, whatToProcess, matchAllData, WhichInning);
			} else {
				return status;
			}
		} else {
			return status;
		}
	}
	public String populateInAt(int WhichSide, String whatToProcess, int playerId, MatchAllData matchAllData) throws ParseException, InterruptedException
	{
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES))
			.findAny().orElse(null);
		if(inning == null ) {
			return "populateTarget: inning is null";
		}
		player = CricketFunctions.getPlayerFromMatchData(playerId, matchAllData);
		if(player == null) {
			return "populatePlayerProfile: Player id [" + whatToProcess.split(",")[2] + "] from database is returning NULL";
		}
		
		return PopulateFfBody(WhichSide, whatToProcess.split(",")[0], matchAllData, 0);
	}
	public String populateBatMileStone(int WhichSide, String whatToProcess, MatchAllData matchAllData) throws ParseException, InterruptedException
	{
		FirstPlayerId = Integer.valueOf(whatToProcess.split(",")[2]);
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1]))
			.findAny().orElse(null);
		if(inning == null ) {
			return "populateBatMileStone: inning is null";
		}
		player = CricketFunctions.getPlayerFromMatchData(FirstPlayerId, matchAllData);
		if(player == null) {
			return "populateBatMileStone: Player id [" + whatToProcess.split(",")[2] + "] from database is returning NULL";
		}
		team = Teams.stream().filter(tm -> tm.getTeamId() == player.getTeamId()).findAny().orElse(null);
		if(team == null) {
			return "populateBatMileStone: Team id [" + player.getTeamId() + "] from database is returning NULL";
		}
		
		return PopulateFfBody(WhichSide, whatToProcess.split(",")[0], matchAllData, 0);
	}
	public String populateBowlMileStone(int WhichSide, String whatToProcess, MatchAllData matchAllData) throws ParseException, InterruptedException
	{
		FirstPlayerId = Integer.valueOf(whatToProcess.split(",")[2]);
		inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == Integer.valueOf(whatToProcess.split(",")[1]))
			.findAny().orElse(null);
		if(inning == null ) {
			return "populateTarget: inning is null";
		}
		player = CricketFunctions.getPlayerFromMatchData(FirstPlayerId, matchAllData);
		if(player == null) {
			return "populatePlayerProfile: Player id [" + whatToProcess.split(",")[2] + "] from database is returning NULL";
		}
		team = Teams.stream().filter(tm -> tm.getTeamId() == player.getTeamId()).findAny().orElse(null);
		if(team == null) {
			return "populatePlayerProfile: Team id [" + player.getTeamId() + "] from database is returning NULL";
		}
		
		return PopulateFfBody(WhichSide, whatToProcess.split(",")[0], matchAllData, 0);
	}
	public String populateLeaderBoard(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException, JAXBException, IOException, InterruptedException
	{
		if(FirstPlayerId <= 0) {
			return "populateLeaderBoard: Player Id NOT found [" + FirstPlayerId + "]";
		}
		tournament_stats = new ArrayList<Tournament>();
		tournament_stats = CricketFunctions.extractTournamentStats("CURRENT_MATCH_DATA", false, tournament_matches, null, matchAllData, tournaments);
		if(tournament_stats == null) {
			return "populateLeaderBoard : Tournament Stats is Null";
		}
		
		switch (whatToProcess.split(",")[0]) {
		case "z": 
			Collections.sort(tournament_stats,new CricketFunctions.BatsmenMostRunComparator());
			break;
		case "x": 
			Collections.sort(tournament_stats,new CricketFunctions.BowlerWicketsComparator());
			break;
		case "c": 
			Collections.sort(tournament_stats,new CricketFunctions.BatsmanFoursComparator());
			break;
		case "v":
			Collections.sort(tournament_stats,new CricketFunctions.BatsmanSixesComparator());
			break;
		case "Control_z":
			top_batsman_beststats = new ArrayList<BestStats>();
			for(Tournament tourn : tournament_stats) {
				for(BestStats bs : tourn.getBatsman_best_Stats()) {
					top_batsman_beststats.add(CricketFunctions.getProcessedBatsmanBestStats(bs));
				}
			}
			Collections.sort(top_batsman_beststats,new CricketFunctions.BatsmanBestStatsComparator());
			break;
		 case "Control_x":
			top_bowler_beststats = new ArrayList<BestStats>();
			for(Tournament tourn : tournament_stats) {
				for(BestStats bs : tourn.getBowler_best_Stats()) {
					top_bowler_beststats.add(CricketFunctions.getProcessedBowlerBestStats(bs));
				}
			}
			Collections.sort(top_bowler_beststats,new CricketFunctions.BowlerBestStatsComparator());
			break;
		}
		
		status = PopulateFfHeader(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
		if(status == Constants.OK) {
			status = PopulateFfBody(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
			if(status == Constants.OK) {
				setFullFrameFooterPosition(WhichSide, 4);
				this.numberOfRows = 11;
				return PopulateFfFooter(WhichSide, whatToProcess.split(",")[0], matchAllData, WhichInning);
			} else {
				return status;
			}
		} else {
			return status;
		}
	}
	public String populateSquad(int WhichSide, String whatToProcess,int teamId, MatchAllData matchAllData, int WhichInning) throws ParseException, InterruptedException, IOException
	{
		team_change.clear();
		
		if(teamId == matchAllData.getSetup().getHomeTeamId()) {
			PlayingXI = matchAllData.getSetup().getHomeSquad();
			otherSquad = matchAllData.getSetup().getHomeOtherSquad();
			team = matchAllData.getSetup().getHomeTeam();
			for(int i=0;i<TeamChanges.size();i++) {
				if(TeamChanges.get(i).contains("H")) {
					team_change.add(TeamChanges.get(i));
				}
			}
		}else {
			PlayingXI = matchAllData.getSetup().getAwaySquad();
			otherSquad = matchAllData.getSetup().getAwayOtherSquad();
			team = matchAllData.getSetup().getAwayTeam();
			for(int i=0;i<TeamChanges.size();i++) {
				if(TeamChanges.get(i).contains("A")) {
					team_change.add(TeamChanges.get(i));
				}
			}
		}
		
		ground = Grounds.stream().filter(grnd -> grnd.getFullname().contains(matchAllData.getSetup().getVenueName())).findAny().orElse(null);
		if(ground == null) {
			return "populateSquad: Ground Name [" + matchAllData.getSetup().getVenueName() + "] from database is returning NULL";
		}
		
		if(WhichType.equalsIgnoreCase("matches") || WhichType.equalsIgnoreCase("runs") || WhichType.equalsIgnoreCase("wickets")) {
			tournament_stats = new ArrayList<Tournament>();
			tournament_stats = CricketFunctions.extractTournamentStats("CURRENT_MATCH_DATA", false, tournament_matches, null, matchAllData, tournaments);
			
			if(tournament_stats == null) {
				return "populateSquad : Tournament Stats is Null";
			}
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
	
	public void setFullFrameBaseAndTextColor(int WhichSide,String whatToProcess) 
	{
		switch (config.getBroadcaster()) {
		case Constants.ISPL:
			switch(whatToProcess) {
			case "F1":
				if(WhichScoreCard.equalsIgnoreCase("SPLIT")) {
					containerName_2 = "$SplitBatting";
				}else if(WhichScoreCard.equalsIgnoreCase("NORMAL")) {
					containerName_2 = "$NormalBatting";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main" + 
					"$WipeGrp$LeftWipe*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main" + 
					"$WipeGrp$RightWipe*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				for(int iRow = 1; iRow <= inning.getBattingCard().size(); iRow++) {
					switch (inning.getBattingCard().get(iRow-1).getStatus().toUpperCase()) {
					case CricketUtil.STILL_TO_BAT:
						if(inning.getBattingCard().get(iRow-1).getHowOut() == null || 
							inning.getBattingCard().get(iRow-1).getHowOut().isEmpty()) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
								"$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$Still_To_Bat$img_Base2*TEXTURE*IMAGE SET " + 
									Constants.ISPL_BASE2 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
								"$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$Still_To_Bat$img_Base1*TEXTURE*IMAGE SET " + 
									Constants.ISPL_BASE1 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
								"$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$Still_To_Bat$BatterGrp$img_Text2*TEXTURE*IMAGE SET " + 
									Constants.ISPL_TEXT2 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
								"$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$img_Base2*TEXTURE*IMAGE SET " + 
									Constants.ISPL_BASE2 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
								"$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$ScoreGrp$img_Base1*TEXTURE*IMAGE SET " + 
									Constants.ISPL_BASE1 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
								"$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$BatterGrp$img_Text2*TEXTURE*IMAGE SET " + 
									Constants.ISPL_TEXT2 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
								"$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$ScoreGrp$img_Text1*TEXTURE*IMAGE SET " + 
									Constants.ISPL_TEXT1 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
								"$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$HowOutGrp$img_Text2*TEXTURE*IMAGE SET " + 
									Constants.ISPL_TEXT2 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						}
						break;
					case CricketUtil.OUT:
						containerName = "Out";
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
							"$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$HowOutGrp$img_Text2*TEXTURE*IMAGE SET " + 
							Constants.ISPL_TEXT2 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						break;
					case CricketUtil.NOT_OUT:
						containerName = "NotOut";
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
							"$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$HowOutGrp$img_Base1*TEXTURE*IMAGE SET " + 
							Constants.ISPL_BASE1 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
						break;
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						"$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$img_Base2*TEXTURE*IMAGE SET " + 
							Constants.ISPL_BASE2 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						"$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$ScoreGrp$img_Base1*TEXTURE*IMAGE SET " + 
							Constants.ISPL_BASE1 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						"$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$BatterGrp$img_Text2*TEXTURE*IMAGE SET " + 
							Constants.ISPL_TEXT2 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						"$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$ScoreGrp$img_Text1*TEXTURE*IMAGE SET " + 
							Constants.ISPL_TEXT1 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				}
				break;
			case "F2":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main" + 
					"$WipeGrp$LeftWipe*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + inning.getBowling_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main" + 
					"$WipeGrp$RightWipe*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + inning.getBowling_team().getTeamName4() + "\0", print_writers);
					
				for(int iRow = 0; iRow <= inning.getBowlingCard().size()-1; iRow++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$BowlingDataAll$" 
							+ (iRow+1) + "$Normal$img_Base2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + inning.getBowling_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$BowlingDataAll$"
							+ (iRow+1) + "$Normal$BatterGrp$img_Text2*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + inning.getBowling_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$BowlingDataAll$"
							+ (iRow+1) + "$Normal$BowlerDataGrp$img_Text2*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + inning.getBowling_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$BowlingDataAll$"
							+ (iRow+1) + "$Normal$ScoreGrp$img_Base1*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + inning.getBowling_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$BowlingDataAll$"
							+ (iRow+1) + "$Normal$ScoreGrp$img_Text1*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT1 + inning.getBowling_team().getTeamName4() + "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$FowGrp$1$FOW"
						+ "$img_Base2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + inning.getBowling_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$FowGrp$1$FOW"
						+ "$BatterGrp$img_Text2*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + inning.getBowling_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$FowGrp$2$"
						+ "FOW$img_Base2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + inning.getBowling_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$FowGrp$2$"
						+ "FOW$img_Base1*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + inning.getBowling_team().getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$FowGrp$2$"
						+ "FOW$WicketsGrp$img_Text2*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + inning.getBowling_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$FowGrp$2$"
						+ "FOW$RunsGrp$img_Base1*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + inning.getBowling_team().getTeamName4() + "\0", print_writers);
				
				break;
			
			case "F4":
				for(int i=1;i<=13;i++) {
					//Title
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$BattingDataAll$"
							+ i + "$Select_Row_Type$Title$img_Base2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$BattingDataAll$"
							+ i + "$Select_Row_Type$Title$BatterGrp$img_Base1*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
					
					//Still To Bat
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$BattingDataAll$"
							+ i + "$Select_Row_Type$Still_To_Bat$img_Base2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$BattingDataAll$"
							+ i + "$Select_Row_Type$Still_To_Bat$BatterGrp$img_Text2*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
					
					//Out
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$BattingDataAll$"
							+ i + "$Select_Row_Type$Out$img_Base2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$BattingDataAll$"
							+ i + "$Select_Row_Type$Out$img_Text2*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$BattingDataAll$"
							+ i + "$Select_Row_Type$Out$ScoreGrp$img_Base1*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$BattingDataAll$"
							+ i + "$Select_Row_Type$Out$ScoreGrp$img_Text1*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT1 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
					
					//NotOut
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$BattingDataAll$"
							+ i + "$Select_Row_Type$NotOut$img_Base2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$BattingDataAll$"
							+ i + "$Select_Row_Type$NotOut$img_Text2*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$BattingDataAll$"
							+ i + "$Select_Row_Type$NotOut$img_Text2$Img_Base1*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$BattingDataAll$"
							+ i + "$Select_Row_Type$NotOut$ScoreGrp$img_Base1*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$BattingDataAll$"
							+ i + "$Select_Row_Type$NotOut$ScoreGrp$img_Text1*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT1 + inning.getBatting_team().getTeamName4() + "\0", print_writers);
				}
				break;
			case "Control_F8":
				for(int i=1;i<=11;i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$Team_SingleDataAll$"
							+ i + "$NameGrp$img_Base2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + team.getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$Team_SingleDataAll$"
							+ i + "$NameGrp$NameGrp$img_Text2*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + team.getTeamName4() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$Team_SingleDataAll$"
							+ i + "$IconGrp$img_Base1*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + team.getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$Team_SingleDataAll$"
							+ i + "$IconGrp$img_Base2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + team.getTeamName4() + "\0", print_writers);
					
					if(i<=4) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$Team_SingleSubDataAll$"
								+ i + "$NameGrp$img_Base2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + team.getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$Team_SingleSubDataAll$"
								+ i + "$NameGrp$NameGrp$img_Text2*TEXTURE*IMAGE SET " + Constants.ISPL_TEXT2 + team.getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$Team_SingleSubDataAll$"
								+ i + "$IconGrp$img_Base1*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + team.getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$Team_SingleSubDataAll$"
								+ i + "$IconGrp$img_Base2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + team.getTeamName4() + "\0", print_writers);
					}
				}
				break;
			}
			break;
		}
	}
	
	public String PopulateFfHeader(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) 
	{
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ISPL:
			switch (whatToProcess) {
			case "Shift_F11": case "Control_F7":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main" + 
					"$WipeGrp$LeftWipe*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + "ISPL" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main" + 
					"$WipeGrp$RightWipe*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + "ISPL" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$HeaderGrp$Side"+ WhichSide + 
					"$In_Out$Select_Header*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$HeaderGrp$Side" + WhichSide + "$Change$"
					+ "txt_Subheader*GEOM*TEXT SET " + "ISPL 2024 - " + matchAllData.getSetup().getMatchIdent() + "\0", print_writers);
				
				switch (whatToProcess) {
				case "Shift_F11":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$HeaderGrp$Side" + WhichSide + 
							"$In_Out$Select_Header$Title$txt_Title*GEOM*TEXT SET " + "MATCH SUMMARY" + "\0", print_writers);
					break;
				case "Control_F7":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$HeaderGrp$Side" + WhichSide + 
							"$In_Out$Select_Header$Title$txt_Title*GEOM*TEXT SET " + "TEAMS" + "\0", print_writers);
					break;
				}
				break;
			case "Control_F8":
				
				containerName = "$Team_Single";
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$HeaderGrp$Side"+ WhichSide + 
						"$In_Out$Select_Header*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$HeaderGrp$Side" + WhichSide + "$Change$"
						+ "txt_Subheader*GEOM*TEXT SET " + "ISPL 2024 - " + matchAllData.getSetup().getMatchIdent() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$HeaderGrp$Side" + WhichSide + 
						"$In_Out$Select_Header$Title$txt_Title*GEOM*TEXT SET " + team.getTeamName1() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main" + 
					"$WipeGrp$LeftWipe*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + team.getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main" + 
					"$WipeGrp$RightWipe*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + team.getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
					containerName + "$img_TeamLogoBW*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + "ISPL" + "\0", print_writers);
			
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
					containerName + "$TeamLogoGrp$LogoBaseGrp$img_Base2$img_TeamLogoBW*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + 
						team.getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
					containerName + "$TeamLogoGrp$LogoGrp$img_TeamLogo*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + 
						team.getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
					containerName + "$TeamLogoGrp$LogoGrp$img_TeamLogoOverlay*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + 
						team.getTeamName4() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
					containerName + "$TeamLogoGrp$LogoBaseGrp$img_Base2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + 
					team.getTeamName4()	+ "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
					containerName +	"$TeamLogoGrp$DesignElementsGrp$ThinLines$img_Base1*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + 
						team.getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
					containerName + "$TeamLogoGrp$DesignElementsGrp$ThinLines$img_Base2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + 
						team.getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
					containerName + "$TeamLogoGrp$DesignElementsGrp$img_Base1*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + 
					team.getTeamName4() + "\0", print_writers);
				
				
				
				break;
			case "F1": case "F2": case "F4":
				inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getInningNumber() == WhichInning)
				.findAny().orElse(null);
				if(inning == null) {
					return "PopulateFfHeader: current inning is NULL";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$HeaderGrp$Side" + WhichSide + "$Change$"
						+ "txt_Subheader*GEOM*TEXT SET " + "ISPL 2024 - " + matchAllData.getSetup().getMatchIdent() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$HeaderGrp$Side" + WhichSide + "$In_Out$Select_Header$Big_First$"
						+ "txt_Team_1*GEOM*TEXT SET " + matchAllData.getMatch().getInning().get(0).getBatting_team().getTeamName4() +"\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$HeaderGrp$Side" + WhichSide + "$In_Out$Select_Header$Big_First$"
						+ "txt_Team_2*GEOM*TEXT SET " + matchAllData.getMatch().getInning().get(0).getBowling_team().getTeamName4() +"\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$HeaderGrp$Side" + WhichSide + "$In_Out$Select_Header$Small_First$"
						+ "txt_Team_1*GEOM*TEXT SET " + matchAllData.getMatch().getInning().get(0).getBatting_team().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$HeaderGrp$Side" + WhichSide + "$In_Out$Select_Header$Small_First$"
						+ "txt_Team_2*GEOM*TEXT SET " + matchAllData.getMatch().getInning().get(0).getBowling_team().getTeamName4() + "\0", print_writers);
				
				switch(whatToProcess) {
				case "F1": case "F4":
					containerName = "";
					switch(whatToProcess) {
					case "F1":
						containerName = "$BattingCard";
						break;
					case "F4":
						containerName = "$Partnership_List";
						break;
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						containerName + "$img_TeamLogoBW*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + "ISPL" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						containerName + "$TeamLogoGrp$LogoBaseGrp$img_Base2$img_TeamLogoBW*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + 
							inning.getBatting_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						containerName + "$TeamLogoGrp$LogoGrp$img_TeamLogo*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + 
							inning.getBatting_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						containerName + "$TeamLogoGrp$LogoGrp$img_TeamLogoOverlay*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + 
							inning.getBatting_team().getTeamName4() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						containerName + "$TeamLogoGrp$LogoBaseGrp$img_Base2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + 
						inning.getBatting_team().getTeamName4()	+ "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						containerName +	"$TeamLogoGrp$DesignElementsGrp$ThinLines$img_Base1*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + 
							inning.getBatting_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						containerName + "$TeamLogoGrp$DesignElementsGrp$ThinLines$img_Base2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + 
							inning.getBatting_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						containerName + "$TeamLogoGrp$DesignElementsGrp$img_Base1*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + 
						inning.getBatting_team().getTeamName4() + "\0", print_writers);
					
					if(WhichInning == 1) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$HeaderGrp$Side"+ WhichSide + 
								"$In_Out$Select_Header*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$HeaderGrp$Side"+ WhichSide + 
								"$In_Out$Select_Header*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}
					break;
				case "F2":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
							"$Bowling_Card$img_TeamLogoBW*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + "ISPL" + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						"$Bowling_Card$TeamLogoGrp$LogoBaseGrp$img_Base2$img_TeamLogoBW*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + 
							inning.getBowling_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						"$Bowling_Card$TeamLogoGrp$LogoGrp$img_TeamLogo*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + 
							inning.getBowling_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						"$Bowling_Card$TeamLogoGrp$LogoGrp$img_TeamLogoOverlay*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + 
							inning.getBowling_team().getTeamName4() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
							"$Bowling_Card$TeamLogoGrp$LogoBaseGrp$img_Base2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + inning.getBowling_team().getTeamName4()
							+ "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
							"$Bowling_Card$TeamLogoGrp$DesignElementsGrp$ThinLines$img_Base1*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + 
							inning.getBowling_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
							"$Bowling_Card$TeamLogoGrp$DesignElementsGrp$ThinLines$img_Base2*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + 
							inning.getBowling_team().getTeamName4() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
							"$Bowling_Card$TeamLogoGrp$DesignElementsGrp$img_Base1*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + inning.getBowling_team().getTeamName4()
							+ "\0", print_writers);
					
					
					if(WhichInning == 1) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$HeaderGrp$Side"+ WhichSide + 
								"$In_Out$Select_Header*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$HeaderGrp$Side"+ WhichSide + 
								"$In_Out$Select_Header*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					}
					break;
				}
				
				break;	
			}
			break;
		case Constants.ICC_U19_2023:
			
			switch (whatToProcess) {
			case "r":
				CricketFunctions.DoadWriteCommandToSelectedViz(1,"-1 RENDERER*BACK_LAYER*TREE*$gfx_POTT_Aramco$tLogoMark"
						+ "*ACTIVE SET 1 \0", print_writers);
				if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
					CricketFunctions.DoadWriteCommandToSelectedViz(2,"-1 RENDERER*BACK_LAYER*TREE*$gfx_POTT_Aramco$tLogoMark"
							+ "*ACTIVE SET 0 \0", print_writers);
				}
				break;
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
						"$Flag*ACTIVE SET 1 \0", print_writers);
				
//				System.out.println(matchAllData.getSetup().getHomeTeam().getTeamGroup());
//				System.out.println(matchAllData.getSetup().getAwayTeam().getTeamGroup());
				
				if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
					if(WhichInning == 1 || WhichInning == 2) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Bottom$txt_Subheader"
								+ "*GEOM*TEXT SET " + "1st INNINGS" + "\0", print_writers);
					}else if(WhichInning == 3 || WhichInning == 4) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Bottom$txt_Subheader"
								+ "*GEOM*TEXT SET " + "2nd INNINGS" + "\0", print_writers);
					}
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Bottom$txt_Subheader"
							+ "*GEOM*TEXT SET " + matchAllData.getSetup().getMatchIdent() + " - " + ground.getCity() + "\0", print_writers);
				}
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
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
							"$Flag*ACTIVE SET 1 \0", print_writers);
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
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
							"$Flag*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side"+ WhichSide + 
							"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "COMPARISON"+ "\0", print_writers);
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Bottom$txt_Subheader"
						+ "*GEOM*TEXT SET " + matchAllData.getSetup().getMatchIdent() + " - " + ground.getCity() + "\0", print_writers);

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Change$Bottom*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				
				break;
				
			case "Alt_F9": case "Alt_F10":
				ground = Grounds.stream().filter(grnd -> grnd.getFullname().contains(matchAllData.getSetup().getVenueName())).findAny().orElse(null);
				if(ground == null) {
					return "PopulateFfHeader: ground name is NULL";
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header_Shrink SHOW 0.0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
						"$Flag*ACTIVE SET 1 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Bottom$txt_Subheader"
						+ "*GEOM*TEXT SET " + matchAllData.getSetup().getMatchIdent() + " - " + ground.getCity() + "\0", print_writers);
				
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
				
			case "Control_d": case "Control_e": case "Shift_P": case "Shift_Q":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header_Shrink SHOW 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
						"$Flag*ACTIVE SET 1 \0", print_writers);
				if(team.getTeamName4().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Flag$img_Shadow*ACTIVE SET 0 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
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
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Bottom$txt_Subheader"
						+ "*GEOM*TEXT SET " + "" + "\0", print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
//						"$Change$Bottom*ACTIVE SET 0 \0", print_writers);
					
				break;
			
			case "Alt_z":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$Select_Flag*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				if(team.getTeamName4().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$img_Shadow*ACTIVE SET 0 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$img_Shadow*ACTIVE SET 1 \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + 
					team.getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$txt_Header*GEOM*TEXT SET " +team.getTeamName1()+ " SQUAD" +"\0", print_writers);
				if(WhichType.equalsIgnoreCase("RUNS")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$txt_Subheader*GEOM*TEXT SET " +"BATTING PERFORMANCES"+ "\0", print_writers);
				}else if(WhichType.equalsIgnoreCase("WICKETS")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$txt_Subheader*GEOM*TEXT SET " +"BOWLING PERFORMANCES"+ "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getMatchIdent() + " - " + ground.getCity() + "\0", print_writers);
				}
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$txt_Subheader*GEOM*TEXT SET " +matchAllData.getSetup().getTournament()+ "\0", print_writers);
				break;

			case "Control_F8":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*STAGE*DIRECTOR*Header_Shrink SHOW 0.0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
						"$Flag*ACTIVE SET 1 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Bottom$txt_Subheader"
						+ "*GEOM*TEXT SET " + matchAllData.getSetup().getMatchIdent() + " - " + ground.getCity() + "\0", print_writers);
				
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
						"$Flag*ACTIVE SET 1 \0", print_writers);

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
				
			case "m": case "Control_m": case "Shift_F11": case "Control_F11": case "Control_F7": case "p": case "z": case "x": case "c": case "v": case "Control_p": case "Alt_F11": case "Control_z": case "Control_x":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide +
					"$Flag*ACTIVE SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Select_HeaderTop*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
				
				switch(whatToProcess.split(",")[0]) {
				case "m":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + matchAllData.getSetup().getMatchIdent() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$ICC_Wordmark*GEOM*TEXT SET " + matchAllData.getSetup().getTournament() + "\0", print_writers);
					break;
				
				case "Control_m":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + fixture.getTeamgroup()+ "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$ICC_Wordmark*GEOM*TEXT SET " + matchAllData.getSetup().getTournament() + "\0", print_writers);
					break;
					
				case "Shift_F11":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "MATCH SUMMARY"+ "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Bottom$txt_Subheader"
							+ "*GEOM*TEXT SET " + matchAllData.getSetup().getMatchIdent() + " - " + ground.getCity() + "\0", print_writers);
					break;
				case "Control_F11":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "MATCH SUMMARY"+ "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$txt_Subheader*GEOM*TEXT SET " + fixture.getTeamgroup() + " - " + ground.getCity() + "\0", print_writers);
					break;
					
				case "Control_F7":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "TEAMS" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + "$Bottom$txt_Subheader"
							+ "*GEOM*TEXT SET " + matchAllData.getSetup().getMatchIdent() + " - " + ground.getCity() + "\0", print_writers);
					break;
				case "Alt_F11":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "COMPARISON"+ "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$txt_Subheader*GEOM*TEXT SET " + matchAllData.getSetup().getMatchIdent() + " - " 
							+ ground.getCity() + "\0", print_writers);
					break;
				
				case "z": case "x": case "c": case "v": case "Control_z": case "Control_x":
					switch(whatToProcess.split(",")[0]) {
					case "z":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
								"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "MOST RUNS" + "\0", print_writers);
						break;
					case "x":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
								"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "MOST WICKETS" + "\0", print_writers);
						break;
					case "c":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
								"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "MOST FOURS" + "\0", print_writers);
						break;
					case "v":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
								"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "MOST SIXES" + "\0", print_writers);
						break;
					case "Control_z":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
								"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "HIGHEST SCORES" + "\0", print_writers);
						break;
					case "Control_x":
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
								"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "BEST FIGURES" + "\0", print_writers);
						break;
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$ICC_Wordmark*GEOM*TEXT SET " + "ICC U19 MEN'S CWC 2024" + "\0", print_writers);
					
					break;
					
				case "p": case "Control_p":
					
					if(WhichGroup.contains("A")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
								"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "GROUP A" + "\0", print_writers);
					}else if(WhichGroup.contains("B")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
								"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "GROUP B" + "\0", print_writers);
					}else if(WhichGroup.contains("C")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
								"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "GROUP C" + "\0", print_writers);
					}else if(WhichGroup.contains("D")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
								"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "GROUP D" + "\0", print_writers);
					}else if(WhichGroup.contains("1")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
								"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "SUPER SIX GROUP 1" + "\0", print_writers);
					}else if(WhichGroup.contains("2")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
								"$Select_HeaderTop$Title$txt_Title*GEOM*TEXT SET " + "SUPER SIX GROUP 2" + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$Select_SubHeaderType*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
							"$Change$Bottom$ICC_Wordmark*GEOM*TEXT SET " + matchAllData.getSetup().getTournament() + "\0", print_writers);
					break;
					
				}

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Header$Side" + WhichSide + 
						"$Change$Bottom*ACTIVE SET 1 \0", print_writers);
				break;
			}
			break;
		}
		return Constants.OK;
	}
	public String PopulateFfBody(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) throws ParseException, InterruptedException 
	{
		switch (whatToProcess) {
		case "F1": // Scorecard
			ScoreCardBody(WhichSide, whatToProcess, matchAllData, WhichInning);
			break;
		case "F2": //Bowling Card
			BowlingCardBody(WhichSide, whatToProcess, matchAllData, WhichInning);
			break;
		
		case "Shift_F11": //Match Summary
			// inning declared in summary populate section
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ISPL: case Constants.ICC_U19_2023:
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ISPL:
					omo = 2;
					numberOfRows = 3;
					containerName = "Team";
					containerName_2 = "$Title$SummaryTeamData";
					containerName_3 = "$Select_Graphics";
					break;
				case Constants.ICC_U19_2023:
					omo = 5;
					numberOfRows = 4;
					containerName = "Team_";
					containerName_2 = "$Tittle$Data";
					containerName_3 = "$Select_GraphicsType";
					break;
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + containerName_3
						+ "*FUNCTION*Omo*vis_con SET " + omo + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName + "1" 
						+ containerName_2 + "$Overs$txt_DLS_Value*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName + "2" 
						+ containerName_2 + "$Overs$txt_DLS_Value*ACTIVE SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName + "1" 
						+ containerName_2 + "$Select_ChallngeRuns*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName + "2" 
						+ containerName_2 + "$Select_ChallngeRuns*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				
				for(int i = 1; i <= WhichInning ; i++) {
					if(i == 1) {
						rowId=0;
						if(matchAllData.getSetup().getTargetOvers() != null && !matchAllData.getSetup().getTargetOvers().trim().isEmpty()) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + 
									containerName + "1" + containerName_2 + "$Overs$txt_DLS_Value*ACTIVE SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + 
									containerName + "1" + containerName_2 + "$Overs$txt_DLS_Value*GEOM*TEXT SET " + "(" + matchAllData.getSetup().getTargetOvers() + ")" + 
									" \0", print_writers);
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + 
									containerName + "2" + containerName_2 + "$Overs$txt_DLS_Value*ACTIVE SET 0 \0", print_writers);
						}
						if(matchAllData.getMatch().getInning().get(i-1).getBattingTeamId() == matchAllData.getSetup().getTossWinningTeam()) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName + 
									+ i + containerName_2 + "$Select_Toss*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName + 
									+ i + containerName_2 + "$Select_Toss*FUNCTION*Omo*vis_con SET 0 \0", print_writers);							
						}
					}else {
						rowId=0;
						if(matchAllData.getSetup().getTargetOvers() != null && !matchAllData.getSetup().getTargetOvers().trim().isEmpty()) {
							if(Integer.valueOf(matchAllData.getSetup().getTargetOvers()) != Integer.valueOf(matchAllData.getMatch().getInning().get(0).getTotalOvers())) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + 
										containerName + "2" + containerName_2 + "$Overs$txt_DLS_Value*ACTIVE SET 1 \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName +"2" 
										+ containerName_2 + "$Overs$txt_DLS_Value*GEOM*TEXT SET " + "("+matchAllData.getSetup().getTargetOvers()+")" + " \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName +"1" 
										+ containerName_2 + "$Overs$txt_DLS_Value*ACTIVE SET 0 \0", print_writers);
							}else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName +"1" 
										+ containerName_2 + "$Overs$txt_DLS_Value*ACTIVE SET 1 \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName +"1" 
										+ containerName_2 + "$Overs$txt_DLS_Value*GEOM*TEXT SET " + "("+matchAllData.getSetup().getTargetOvers()+")" + " \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName +"2" 
										+ containerName_2 + "$Overs$txt_DLS_Value*GEOM*TEXT SET " + "("+matchAllData.getSetup().getTargetOvers()+")" + " \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName +"2" 
										+ containerName_2 + "$Overs$txt_DLS_Value*ACTIVE SET 1 \0", print_writers);
							}
						}
						
						if(matchAllData.getMatch().getInning().get(i-1).getBattingTeamId() == matchAllData.getSetup().getTossWinningTeam()) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName +
									+ i + containerName_2 + "$Select_Toss*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName +
									+ i + containerName_2 + "$Select_Toss*FUNCTION*Omo*vis_con SET 0 \0", print_writers);							
						}
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName +
							+ i + containerName_2 + "$txt_Team*GEOM*TEXT SET " + matchAllData.getMatch().getInning().get(i-1).getBatting_team().getTeamName1() 
							+ " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$TeamLogoGrp"
							+ "$LogoGrp" + i + "$img_TeamLogo*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + matchAllData.getMatch().getInning().get(i-1).
							getBatting_team().getTeamName4() + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$TeamLogoGrp"
							+ "$LogoGrp" + i +"$img_TeamLogoOverlay*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + matchAllData.getMatch().getInning().get(i-1).
							getBatting_team().getTeamName4() + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName +
							+ i + containerName_2 + "$Overs$txt_Overs_Value*GEOM*TEXT SET " + CricketFunctions.OverBalls(matchAllData.getMatch().getInning().get(i-1).
							getTotalOvers(),matchAllData.getMatch().getInning().get(i-1).getTotalBalls()) + " \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName + i + 
							containerName_2 + "$ScoreGrp$img_Text1$txt_Score*GEOM*TEXT SET " + CricketFunctions.getTeamScore(matchAllData.getMatch().getInning().get(i-1), 
							"-", false) + " \0", print_writers);
					
					for(int j= matchAllData.getEventFile().getEvents().size()-1;j >= 0 ; j--) {
						if(matchAllData.getEventFile().getEvents().get(j).getEventInningNumber() == matchAllData.getMatch().getInning().get(i-1).getInningNumber()) {
							if(matchAllData.getEventFile().getEvents().get(j).getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50)) {
								if(matchAllData.getEventFile().getEvents().get(j).getEventExtra().equalsIgnoreCase("-")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName + 
											+ i + containerName_2 + "$Select_ChallngeRuns*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName +
											+ i + containerName_2 + "$Select_ChallngeRuns$NegativeChallenge$txt_ChallengeValue*GEOM*TEXT SET " + "-" + 
											matchAllData.getEventFile().getEvents().get(j).getEventExtraRuns() + "\0", print_writers);
								}
								else if(matchAllData.getEventFile().getEvents().get(j).getEventExtra().equalsIgnoreCase("+")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName +
											+ i + containerName_2 + "$Select_ChallngeRuns*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName +
											+ i + containerName_2 + "$Select_ChallngeRuns$PositiveChallenge$txt_ChallengeValue*GEOM*TEXT SET " + "+" +
											matchAllData.getEventFile().getEvents().get(j).getEventExtraRuns() + "\0", print_writers);
								}
							}
						}
					}
					
					if(i==1) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$"+ 
								containerName +"2*ACTIVE SET 0 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$"+ 
								containerName +"2*ACTIVE SET 1 \0", print_writers);
					}
					
					if(matchAllData.getMatch().getInning().get(i-1).getBattingCard() != null) {
						Collections.sort(matchAllData.getMatch().getInning().get(i-1).getBattingCard(),new CricketFunctions.BatsmenScoreComparator());
						
						for(BattingCard bc : matchAllData.getMatch().getInning().get(i-1).getBattingCard()) {
							if(bc.getRuns() > 0) {
								rowId++;
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + 
										containerName + i + "$Row_" + rowId + "*ACTIVE SET 1 \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + 
										containerName + i + "$Row_" + rowId + "$Batsman*ACTIVE SET 1 \0", print_writers);
								
								if(CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
										bc.getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
											+ "$Summary$" + containerName + i + "$Row_" + rowId + "$Batsman$Selec_tImpact*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
								}
								else if(CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
										bc.getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
											+ "$Summary$" + containerName + i + "$Row_" + rowId + "$Batsman$Selec_tImpact*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
								}else {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
											+ "$Summary$" + containerName + i + "$Row_" + rowId + "$Batsman$Selec_tImpact*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
								}
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName + i + 
										"$Row_" + rowId + "$Batsman$txt_Name*GEOM*TEXT SET " + bc.getPlayer().getTicker_name() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName + i + 
										"$Row_" + rowId + "$Batsman$txt_Runs*GEOM*TEXT SET " + bc.getRuns() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName + i + 
										"$Row_" + rowId + "$Batsman$txt_Balls*GEOM*TEXT SET " + String.valueOf(bc.getBalls()) + "\0", print_writers);

								if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName 
											+ i + "$Row_" + rowId + "$Batsman$txt_Not-Out*GEOM*TEXT SET " + "*" + "\0", print_writers);
								} else {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName
											+ i + "$Row_" + rowId + "$Batsman$txt_Not-Out*GEOM*TEXT SET " + "" + "\0", print_writers);
								}
								if(i == 1 && rowId >= numberOfRows) {
									break;
								}else if(i == 2 && rowId >= numberOfRows) {
									break;
								}
							}
						}
					}
					for(int j = rowId+1; j <= numberOfRows; j++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName + i + 
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
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName + i + 
										"$Row_" + rowId + "*ACTIVE SET 1 \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName + i + 
										"$Row_" + rowId + "$Bowler*ACTIVE SET 1 \0", print_writers);
								
								if(CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
										boc.getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
											+ "$Summary$" + containerName + i + "$Row_" + rowId + "$Bowler$Selec_tImpact*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
								}
								else if(CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
										boc.getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
											+ "$Summary$" + containerName + i + "$Row_" + rowId + "$Bowler$Selec_tImpact*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
								}else {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
											+ "$Summary$" + containerName + i + "$Row_" + rowId + "$Bowler$Selec_tImpact*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
								}
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName + i + 
										"$Row_" + rowId + "$Bowler$txt_Name*GEOM*TEXT SET " + boc.getPlayer().getTicker_name() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName + i + 
										"$Row_" + rowId + "$Bowler$txt_Figs*GEOM*TEXT SET " + boc.getWickets() + "-" + boc.getRuns() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName 
										+ i + "$Row_" + rowId + "$Bowler$txt_Not-Out*GEOM*TEXT SET " + "" + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$" + containerName + i + 
										"$Row_" + rowId + "$Bowler$txt_Overs*GEOM*TEXT SET " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()) + "\0", print_writers);
								
								if(i == 1 && rowId >= numberOfRows) {
									break;
								}
								else if(i == 2 && rowId >= numberOfRows) {
									break;
								}
							}
						}
					}
					for(int j = rowId+1; j <= numberOfRows; j++) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team" + i + 
								"$Row_" + j + "$Bowler*ACTIVE SET 0 \0", print_writers);
					}
				}
				break;
			}
			break;
			
		case "Control_F1":
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
					+ "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 13 \0", print_writers);
			rowId = 0;
			plyr_count = 0;
			Collections.sort(inning.getBattingCard());
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
				
				plyr_count = plyr_count + 1;
				
				player = PlayingXI.stream().filter(plyr -> plyr.getPlayerId() == inning.getBattingCard().get(plyr_count-1).getPlayerId()).findAny().orElse(null);
				
				if(player.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN) || 
						player.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")){
					
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
						
//						if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
//							if(!new File("\\\\"+config.getSecondaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 
//									+ inning.getBattingCard().get(iRow-1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//									return "Photo not found in "+config.getSecondaryIpAddress();
//								}
//							CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Batting_Card_Image"
//								+ containerName + "$Photo_" + rowId + "$img_Photo*TEXTURE*IMAGE SET " + "\\\\"+config.getSecondaryIpAddress()+"\\" + Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +
//								inning.getBattingCard().get(iRow-1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
//						}

						//if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 
//							+ inning.getBattingCard().get(iRow-1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//							return "Photo not found in "+config.getPrimaryIpAddress();
//						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Batting_Card_Image"
								+ containerName + "$Photo_" + rowId + "$img_Photo*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +
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
						
//						if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
//							if(!new File("\\\\"+config.getSecondaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +  inning.getBattingCard().get(iRow-1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//								return "Photo not found";
//							}
//							
//							CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Batting_Card_Image"
//								+ containerName + "$Photo_" + rowId + "$img_Photo*TEXTURE*IMAGE SET " + "\\\\"+config.getSecondaryIpAddress()+"\\" + Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +
//								inning.getBattingCard().get(iRow-1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
//						}
						
//						if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +  inning.getBattingCard().get(iRow-1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//							return "Photo not found";
//						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Batting_Card_Image"
								+ containerName + "$Photo_" + rowId + "$img_Photo*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +
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
					
//					if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
//						if(!new File("\\\\"+config.getSecondaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +  inning.getBattingCard().get(iRow-1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//							return "Photo not found";
//						}
//						CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Batting_Card_Image"
//							+ containerName + "$Photo_" + rowId + "$img_Photo*TEXTURE*IMAGE SET " + "\\\\"+config.getSecondaryIpAddress()+"\\" + Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +
//							inning.getBattingCard().get(iRow-1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
//					}
					
//					if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +  inning.getBattingCard().get(iRow-1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//						return "Photo not found";
//					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Batting_Card_Image"
							+ containerName + "$Photo_" + rowId + "$img_Photo*TEXTURE*IMAGE SET " +  Constants.ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +
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
		
		case "Control_F11":
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
					"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 5 \0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_1" 
					+ "$Tittle$Data$Overs$txt_DLS_Value*ACTIVE SET 0 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_2" 
					+ "$Tittle$Data$Overs$txt_DLS_Value*ACTIVE SET 0 \0", print_writers);
			
			for(int i=1; i<=2; i++ ) {
				for(int j=1; j<=4; j++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_"
							+ i + "$Row_" + j + "*ACTIVE SET 0 \0", print_writers);
				}
			}
			
			for(int i = 1; i <= 2 ; i++) {
				if(i == 1) {
					rowId=0;
					if(previous_match.get(previous_match.size()-1).getSetup().getTargetOvers() != null && 
							!previous_match.get(previous_match.size()-1).getSetup().getTargetOvers().isEmpty()) {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_1" 
								+ "$Tittle$Data$Overs$txt_DLS_Value*ACTIVE SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_1" 
								+ "$Tittle$Data$Overs$txt_DLS_Value*GEOM*TEXT SET " + "("+matchAllData.getSetup().getTargetOvers()+")" + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_2" 
								+ "$Tittle$Data$Overs$txt_DLS_Value*ACTIVE SET 0 \0", print_writers);
					}
					if(previous_match.get(previous_match.size()-1).getMatch().getInning().get(i-1).getBattingTeamId() == 
							previous_match.get(previous_match.size()-1).getSetup().getTossWinningTeam()) {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
								+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
								+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 0 \0", print_writers);							}
					
				} else {
					rowId=0;
					if(previous_match.get(previous_match.size()-1).getSetup().getTargetOvers() != null && 
							!previous_match.get(previous_match.size()-1).getSetup().getTargetOvers().isEmpty()) {
						if(Integer.valueOf(previous_match.get(previous_match.size()-1).getSetup().getTargetOvers()) != 
								Integer.valueOf(previous_match.get(previous_match.size()-1).getMatch().getInning().get(0).getTotalOvers())) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_2" 
									+ "$Tittle$Data$Overs$txt_DLS_Value*ACTIVE SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_2" 
									+ "$Tittle$Data$Overs$txt_DLS_Value*GEOM*TEXT SET " + "(" + previous_match.get(previous_match.size()-1).getSetup().getTargetOvers() 
									+ ")" + " \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_1" 
									+ "$Tittle$Data$Overs$txt_DLS_Value*ACTIVE SET 0 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_1" 
									+ "$Tittle$Data$Overs$txt_DLS_Value*ACTIVE SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
									+ i + "$Tittle$Data$Overs$txt_DLS_Value*GEOM*TEXT SET " + "(" + previous_match.get(previous_match.size()-1).getSetup().getTargetOvers()
									+ ")" + " \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_2" 
									+ "$Tittle$Data$Overs$txt_DLS_Value*ACTIVE SET 1 \0", print_writers);
						}
					}
					if(previous_match.get(previous_match.size()-1).getMatch().getInning().get(i-1).getBattingTeamId() == 
							previous_match.get(previous_match.size()-1).getSetup().getTossWinningTeam()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
								+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
								+ i +"$Tittle$Data$Select_Toss*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					}
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
						+ i +"$Tittle$Data$txt_Team*GEOM*TEXT SET " + previous_match.get(previous_match.size()-1).getMatch().getInning().get(i-1).
						getBatting_team().getTeamName1() + " \0", print_writers);
				
				if(previous_match.get(previous_match.size()-1).getMatch().getInning().get(i-1).getBatting_team().getTeamName4().equalsIgnoreCase("NEP")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
							+ i +"$Tittle$FlagGrp$img_Shadow*ACTIVE SET 0 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
							+ i +"$Tittle$FlagGrp$img_Shadow*ACTIVE SET 1 \0", print_writers);
				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
						+ i +"$Tittle$FlagGrp$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + previous_match.get(previous_match.size()-1).
						getMatch().getInning().get(i-1).getBatting_team().getTeamName4() + " \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
						+ i + "$Tittle$Data$Overs$txt_Overs_Value*GEOM*TEXT SET " + CricketFunctions.OverBalls(previous_match.get(previous_match.size()-1).
						getMatch().getInning().get(i-1).getTotalOvers(),previous_match.get(previous_match.size()-1).getMatch().getInning().get(i-1).
						getTotalBalls()) + " \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" 
						+ i +"$Tittle$Data$txt_Score*GEOM*TEXT SET " + CricketFunctions.getTeamScore(previous_match.get(previous_match.size()-1).getMatch().
								getInning().get(i-1), "-", false) + " \0", print_writers);
				
				if(i==1) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
							"$Summary$Team_2*ACTIVE SET 0 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
							"$Summary$Team_2*ACTIVE SET 1 \0", print_writers);
				}
				
				if(previous_match.get(previous_match.size()-1).getMatch().getInning().get(i-1).getBattingCard() != null) {
					Collections.sort(previous_match.get(previous_match.size()-1).getMatch().getInning().get(i-1).getBattingCard(),
							new CricketFunctions.BatsmenScoreComparator());
					
					for(BattingCard bc : previous_match.get(previous_match.size()-1).getMatch().getInning().get(i-1).getBattingCard()) {
						if(bc.getRuns() > 0) {
							rowId++;
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" + i +
									"$Row_" + rowId + "*ACTIVE SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" + i + 
									"$Row_" + rowId + "$Batsman*ACTIVE SET 1 \0", print_writers);
							
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
				
				if(previous_match.get(previous_match.size()-1).getMatch().getInning().get(i-1).getBowlingCard() != null) {
					Collections.sort(previous_match.get(previous_match.size()-1).getMatch().getInning().get(i-1).getBowlingCard(),
							new CricketFunctions.BowlerFiguresComparator());
					for(BowlingCard boc : previous_match.get(previous_match.size()-1).getMatch().getInning().get(i-1).getBowlingCard()) {
						
						if(boc.getWickets() > 0) {
							rowId++;
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" + i + 
									"$Row_" + rowId + "*ACTIVE SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Summary$Team_" + i + 
									"$Row_" + rowId + "$Bowler*ACTIVE SET 1 \0", print_writers);
							
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
			double Mult = 0,ScaleFac1 = 0, ScaleFac2 = 0;
			String Left_Batsman="", Right_Batsman="";
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023: case Constants.ISPL:
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023:
					omo = 6;
					Mult = 160;
					containerName_2 = "Rows";
					containerName_3 = "$Select_GraphicsType";
					break;
				case Constants.ISPL:
					omo = 5;
					Mult = 55;
					containerName_2 = "BattingDataAll";
					containerName_3 = "$Select_Graphics";
					break;
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + containerName_3 +
						"*FUNCTION*Omo*vis_con SET " + omo + "\0", print_writers);
				
				if(inning.getPartnerships().size()>=10) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$" +
							containerName_2 + "*FUNCTION*Grid*num_row SET 11 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$" +
							containerName_2 + "*FUNCTION*Grid*num_row SET " + inning.getBattingCard().size() + "\0", print_writers);
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
					if(inning.getPartnerships().size() >= 10 && inning.getTotalWickets()>=10) {
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
							switch (config.getBroadcaster().toUpperCase()) {
							case Constants.ICC_U19_2023:
								containerName = "$Not_Out";
								break;
							case Constants.ISPL:
								containerName = "$NotOut";
								break;
							}
						}
					}
					
					ScaleFac1 = ((ps.getFirstBatterRuns())*(Mult/Top_Score)) ;
					ScaleFac2 = ((ps.getSecondBatterRuns())*(Mult/Top_Score)) ;
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$" 
						+ containerName_2 + "$" + rowId +  "$Select_Row_Type*FUNCTION*Omo*vis_con SET " + omo_num + "\0", print_writers);
					
					switch (config.getBroadcaster().toUpperCase()) {
					case Constants.ICC_U19_2023:
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$"
								+ containerName_2 + "$" + rowId + "$Select_Row_Type" + containerName +"$txt_Name_1*GEOM*TEXT SET " + Left_Batsman + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$"
								+ containerName_2 + "$" + rowId + "$Select_Row_Type" + containerName +"$txt_Name_2*GEOM*TEXT SET " + Right_Batsman + "\0", print_writers);
						break;
					case Constants.ISPL:
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$"
								+ containerName_2 + "$" + rowId + "$Select_Row_Type" + containerName +"$txt_Name1*GEOM*TEXT SET " + Left_Batsman + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$"
							+ containerName_2 + "$" + rowId + "$Select_Row_Type" + containerName +"$txt_Name2*GEOM*TEXT SET " + Right_Batsman + "\0", print_writers);
						
						if(CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(),
								ps.getFirstBatterNo()).equalsIgnoreCase(CricketUtil.YES)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$"
									+ containerName_2 + "$" + rowId + "$Select_Row_Type" + containerName +"$Selec_tImpact1*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						}
						else if(CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
								ps.getFirstBatterNo()).equalsIgnoreCase(CricketUtil.YES)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$"
									+ containerName_2 + "$" + rowId + "$Select_Row_Type" + containerName +"$Selec_tImpact1*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$"
									+ containerName_2 + "$" + rowId + "$Select_Row_Type" + containerName +"$Selec_tImpact1*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						}
						
						if(CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(),
								ps.getSecondBatterNo()).equalsIgnoreCase(CricketUtil.YES)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$"
									+ containerName_2 + "$" + rowId + "$Select_Row_Type" + containerName +"$Selec_tImpact2*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						}
						else if(CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
								ps.getSecondBatterNo()).equalsIgnoreCase(CricketUtil.YES)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$"
									+ containerName_2 + "$" + rowId + "$Select_Row_Type" + containerName +"$Selec_tImpact2*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$"
									+ containerName_2 + "$" + rowId + "$Select_Row_Type" + containerName +"$Selec_tImpact2*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						}
						break;
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$"
						+ containerName_2 + "$" + rowId + "$Select_Row_Type" + containerName +"$txt_Runs*GEOM*TEXT SET " + ps.getTotalRuns() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$"
						+ containerName_2 + "$" + rowId + "$Select_Row_Type" + containerName +"$txt_Balls*GEOM*TEXT SET " + ps.getTotalBalls() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$"
						+ containerName_2 + "$" + rowId  + "$Select_Row_Type" + containerName + "$Geom_Bar_1*GEOM*width SET " + ScaleFac1 + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$"
						+ containerName_2 + "$" + rowId  + "$Select_Row_Type" + containerName + "$Geom_Bar_2*GEOM*width SET " + ScaleFac2 + "\0", print_writers);
				}
				
				if(inning.getPartnerships().size() >= 10) {
					rowId = rowId + 1;
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$" 
							+ containerName_2 + "$" + rowId + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$" 
							+ containerName_2 + "$" + rowId + "$Select_Row_Type$Title$txt_Title*GEOM*TEXT SET " + "" + "\0", print_writers);
				}else {
					for (BattingCard bc : inning.getBattingCard()) {
						if(rowId < inning.getBattingCard().size()) {
							if(rowId == inning.getPartnerships().size()) {
								rowId = rowId + 1;
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$" 
										+ containerName_2 + "$" + rowId + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
								
								if(matchAllData.getMatch().getInning().get(WhichInning - 1).getTotalOvers() == matchAllData.getSetup().getMaxOvers() 
										|| matchAllData.getMatch().getInning().get(WhichInning - 1).getTotalWickets() >= 10 ) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$" 
											+ containerName_2 + "$" + rowId + "$Select_Row_Type$Title$txt_Title*GEOM*TEXT SET " + "DID NOT BAT" + "\0", print_writers);
								}
								else if(matchAllData.getSetup().getTargetOvers() != null && !matchAllData.getSetup().getTargetOvers().trim().isEmpty()) {
									if( matchAllData.getMatch().getInning().get(WhichInning - 1).getTotalOvers() == Integer.valueOf(matchAllData.getSetup().getTargetOvers()) 
											|| matchAllData.getMatch().getInning().get(WhichInning - 1).getTotalWickets() >= 10) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$" 
												+ containerName_2 + "$" + rowId + "$Select_Row_Type$Title$txt_Title*GEOM*TEXT SET " + "DID NOT BAT" + "\0", print_writers);
									}else if(CricketFunctions.getRequiredRuns(matchAllData) <= 0 || CricketFunctions.getRequiredBalls(matchAllData) <= 0 
											|| CricketFunctions.getWicketsLeft(matchAllData) <= 0) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$" 
												+ containerName_2 + "$" + rowId + "$Select_Row_Type$Title$txt_Title*GEOM*TEXT SET " + "DID NOT BAT" + "\0", print_writers);
									}else {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$" 
												+ containerName_2 + "$" + rowId + "$Select_Row_Type$Title$txt_Title*GEOM*TEXT SET " + "STILL TO BAT" + "\0", print_writers);
									}
								}
								else if(CricketFunctions.getRequiredRuns(matchAllData) <= 0 || CricketFunctions.getRequiredBalls(matchAllData) <= 0 || 
										CricketFunctions.getWicketsLeft(matchAllData) <= 0) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$" 
											+ containerName_2 + "$" + rowId + "$Select_Row_Type$Title$txt_Title*GEOM*TEXT SET " + "DID NOT BAT" + "\0", print_writers);
								}
								else {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$" 
											+ containerName_2 + "$" + rowId + "$Select_Row_Type$Title$txt_Title*GEOM*TEXT SET " + "STILL TO BAT" + "\0", print_writers);
								}
							}
							else if(bc.getStatus().toUpperCase().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
								rowId = rowId + 1;
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$" 
										+ containerName_2 + "$" + rowId + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$" 
										+ containerName_2 + "$" + rowId + "$Select_Row_Type$Still_To_Bat$txt_Name*GEOM*TEXT SET " + 
										bc.getPlayer().getTicker_name() + "\0", print_writers);
								
								if(CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(),
										bc.getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$"
											+ containerName_2 + "$" + rowId + "$Select_Row_Type$Still_To_Bat$Selec_tImpact*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
								}
								else if(CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
										bc.getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$"
											+ containerName_2 + "$" + rowId + "$Select_Row_Type$Still_To_Bat$Selec_tImpact*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
								}else {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership_List$"
											+ containerName_2 + "$" + rowId + "$Select_Row_Type$Still_To_Bat$Selec_tImpact*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
								}
							}	
						}
						else {
							break;
						}
					}
				}
				break;
			}
			break;
			
		case "p": case "Control_p":
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023: case Constants.ISPL:
				String fourOrSix = "";
				if(whatToProcess.equalsIgnoreCase("p")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
							"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 15 \0", print_writers);
					fourOrSix = "$GroupStandings";
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
							"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 12 \0", print_writers);
					fourOrSix = "$Standings";
				}
				

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + fourOrSix +
						"$SelectSeperator*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				
				rowId = 0;
				for(int i=0; i<=leagueTable.getLeagueTeams().size()-1;i++) {
					rowId = rowId + 1;
					
					if(matchAllData.getSetup().getHomeTeam().getTeamName4().equalsIgnoreCase(leagueTable.getLeagueTeams().get(i).getTeamName())  
							|| matchAllData.getSetup().getAwayTeam().getTeamName4().equalsIgnoreCase(leagueTable.getLeagueTeams().get(i).getTeamName())) {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ fourOrSix + "$Rows$" + rowId + "$Select_Highlight*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						containerName = "$Highlight";
						
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ fourOrSix +"$Rows$" + rowId + "$Select_Highlight*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						containerName = "$Dehighlight";
					}
					
					if(whatToProcess.equalsIgnoreCase("p")) {
						if(rowId <= 3) {
							if(leagueTable.getLeagueTeams().get(i).getQualifiedStatus().trim().equalsIgnoreCase("")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + fourOrSix +"$Qualify_" 
										+ rowId + "$Select_Qualify*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
							}else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + fourOrSix +"$Qualify_" 
										+ rowId + "$Select_Qualify*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							}
						}
					}else {
						if(rowId <= 2) {
							if(leagueTable.getLeagueTeams().get(i).getQualifiedStatus().trim().equalsIgnoreCase("")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + fourOrSix +"$Qualify_" 
										+ rowId + "$Select_Qualify*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
							}else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + fourOrSix +"$Qualify_" 
										+ rowId + "$Select_Qualify*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							}
						}
					}
					for(Team team : Teams) {
						if(team.getTeamName4().equalsIgnoreCase(leagueTable.getLeagueTeams().get(i).getTeamName())) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + fourOrSix +"$Rows$"
									 + rowId + containerName + "$txt_Name*GEOM*TEXT SET " + team.getTeamName1() + "\0", print_writers);
							
							if(team.getTeamName4().equalsIgnoreCase("NEP")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + fourOrSix +"$Rows$" 
										+ rowId + "$Flag$img_Shadow*ACTIVE SET 0 \0", print_writers);
							}else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + fourOrSix + "$Rows$" 
										+ rowId + "$Flag$img_Shadow*ACTIVE SET 1 \0", print_writers);
							}
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + fourOrSix +"$Rows$"
									 + rowId + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
						}
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + fourOrSix +"$Rows$"
							 + rowId + containerName + "$txt_Played*GEOM*TEXT SET " + leagueTable.getLeagueTeams().get(i).getPlayed() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + fourOrSix +"$Rows$"
							 + rowId + containerName + "$txt_Won*GEOM*TEXT SET " + leagueTable.getLeagueTeams().get(i).getWon() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + fourOrSix +"$Rows$"
							 + rowId + containerName + "$txt_Lost*GEOM*TEXT SET " + leagueTable.getLeagueTeams().get(i).getLost() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + fourOrSix +"$Rows$"
							 + rowId + containerName + "$txt_NoResult*GEOM*TEXT SET " + leagueTable.getLeagueTeams().get(i).getNoResult() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + fourOrSix +"$Rows$"
							 + rowId + containerName + "$txt_Points*GEOM*TEXT SET " + leagueTable.getLeagueTeams().get(i).getPoints() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + fourOrSix +"$Rows$"
							 + rowId + containerName + "$txt_NRR*GEOM*TEXT SET " + String.format("%.2f", leagueTable.getLeagueTeams().get(i).getNetRunRate()) 
							 + "\0", print_writers);
					
				}
				
				break;
			}
			break;
			
		case "z": case "x": case "c": case "v": case "Control_z": case "Control_x":
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023: case Constants.ISPL:
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 14 \0", print_writers);
//				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
//						"$Leader_Board$SubTitle$txt_SubTitle*GEOM*TEXT SET " + "ICC U19 MEN'S CWC 2024" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						"$Leader_Board$SubTitle$txt_SubTitle*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				if(whatToProcess.equalsIgnoreCase("Control_z")) {
					rowId = 0;
					for(int i = 0; i <= top_batsman_beststats.size() - 1 ; i++) {
						rowId = rowId + 1;
						if(rowId <= 5) {
							if(top_batsman_beststats.get(i).getPlayerId() == this.FirstPlayerId) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$PlayerGrp"
									+ rowId + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + Teams.get(top_batsman_beststats.get(i).getPlayer().
									getTeamId() - 1).getTeamName4() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$PlayerGrp"
									+ rowId + "$Photo$img_PlayerPhoto*TEXTURE*IMAGE SET "+Constants.ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 + 
									top_batsman_beststats.get(i).getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
								
							}
							for(int j=0; j<2; j++) {
								
								if(j==0) {
									containerName ="$Highlight";
								}else {
									containerName ="$ProfileDataGrp";
								}
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$Stats$" 
										+ rowId + containerName + "$txt_Name*GEOM*TEXT SET " + top_batsman_beststats.get(i).getPlayer().getFull_name() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$Stats$" 
										+ rowId + containerName + "$txt_Country*GEOM*TEXT SET " + Teams.get(top_batsman_beststats.get(i).getPlayer().getTeamId() - 1).getTeamName1() + "\0", print_writers);
								
								if(top_batsman_beststats.get(i).getBestEquation() % 2 == 0) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$Stats$" 
											+ rowId + containerName + "$txt_NotOutStar*ACTIVE SET 0 \0", print_writers);
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$Stats$" 
											+ rowId + containerName + "$txt_Fig*GEOM*TEXT SET " + top_batsman_beststats.get(i).getBestEquation() / 2 + "\0", print_writers);
								}else {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$Stats$" 
											+ rowId + containerName + "$txt_NotOutStar*ACTIVE SET 1 \0", print_writers);
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$Stats$" 
											+ rowId + containerName + "$txt_Fig*GEOM*TEXT SET " + top_batsman_beststats.get(i).getBestEquation() / 2 + "\0", print_writers);
								}
							}
						}
					}	
				}else if(whatToProcess.equalsIgnoreCase("Control_x")){
					rowId = 0;
					for(int i = 0; i <= top_bowler_beststats.size()-1 ; i++) {
						rowId = rowId + 1;
						if(rowId <= 5) {
							if(top_bowler_beststats.get(i).getPlayerId() == FirstPlayerId) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$PlayerGrp"
										+ rowId + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + Teams.get(top_bowler_beststats.get(i).getPlayer().
												getTeamId() - 1).getTeamName4() + "\0", print_writers);
		
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$PlayerGrp"
										+ rowId + "$Photo$img_PlayerPhoto*TEXTURE*IMAGE SET "+Constants.ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 + 
										top_bowler_beststats.get(i).getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
								
							}
								
							for(int j=0; j<2; j++) {
								if(j==0) {
									containerName ="$Highlight";
								}else {
									containerName ="$ProfileDataGrp";
								}
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$Stats$" 
										+ rowId + containerName + "$txt_NotOutStar*ACTIVE SET 0 \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$Stats$" 
										+ rowId + containerName + "$txt_Name*GEOM*TEXT SET " + top_bowler_beststats.get(i).getPlayer().getFull_name() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$Stats$" 
										+ rowId + containerName + "$txt_Country*GEOM*TEXT SET " + Teams.get(top_bowler_beststats.get(i).getPlayer().getTeamId() - 1).getTeamName1() + "\0", print_writers);
								
								if(top_bowler_beststats.get(i).getBestEquation() % 1000 > 0) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$Stats$" 
											+ rowId + containerName + "$txt_Fig*GEOM*TEXT SET " + ((top_bowler_beststats.get(i).getBestEquation() / 1000) +1) 
												+ "-" + (1000 - (top_bowler_beststats.get(i).getBestEquation() % 1000)) + "\0", print_writers);
								}
								else if(top_bowler_beststats.get(i).getBestEquation() % 1000 < 0) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$Stats$" 
											+ rowId + containerName + "$txt_Fig*GEOM*TEXT SET " + (top_bowler_beststats.get(i).getBestEquation() / 1000) 
												+ "-" + Math.abs(top_batsman_beststats.get(i).getBestEquation()) + "\0", print_writers);
								}
							}	
						}
					}	
				}else{
					rowId = 0;
					for(int i = 0; i <= tournament_stats.size() - 1 ; i++) {
						rowId = rowId + 1;
						if(rowId <= 5) {
							
							if(tournament_stats.get(i).getPlayerId() == FirstPlayerId) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$PlayerGrp"
									+ rowId + "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + Teams.get(tournament_stats.get(i).getPlayer().
									getTeamId() - 1).getTeamName4() + "\0", print_writers);
		
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$PlayerGrp"
									+ rowId + "$Photo$img_PlayerPhoto*TEXTURE*IMAGE SET "+Constants.ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 + 
									tournament_stats.get(i).getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
								
							}
							for(int j=0; j<2; j++) {
								if(j==0) {
									containerName ="$Highlight";
								}else {
									containerName ="$ProfileDataGrp";
								}
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$Stats$" 
										+ rowId + containerName + "$txt_NotOutStar*ACTIVE SET 0 \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$Stats$" 
										+ rowId + containerName + "$txt_Name*GEOM*TEXT SET " + tournament_stats.get(i).getPlayer().getFull_name() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$Stats$" 
										+ rowId + containerName + "$txt_Country*GEOM*TEXT SET " + Teams.get(tournament_stats.get(i).getPlayer().getTeamId() - 1).getTeamName1() + "\0", print_writers);
								
								if(whatToProcess.equalsIgnoreCase("z")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$Stats$" 
											+ rowId + containerName + "$txt_Fig*GEOM*TEXT SET " + tournament_stats.get(i).getRuns() + "\0", print_writers);
								}else if(whatToProcess.equalsIgnoreCase("x")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$Stats$" 
											+ rowId + containerName + "$txt_Fig*GEOM*TEXT SET " + tournament_stats.get(i).getWickets() + "\0", print_writers);
								}else if(whatToProcess.equalsIgnoreCase("c")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$Stats$" 
											+ rowId + containerName + "$txt_Fig*GEOM*TEXT SET " + tournament_stats.get(i).getFours() + "\0", print_writers);
								}else if(whatToProcess.equalsIgnoreCase("v")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Leader_Board$Stats$" 
											+ rowId + containerName + "$txt_Fig*GEOM*TEXT SET " + tournament_stats.get(i).getSixes() + "\0", print_writers);
								}
							}
						}
					}
				}
				
				break;
			}
			break;
		
		case "Alt_F11":
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023: case Constants.ISPL:

				int maxRuns = 0,runsIncr = 0,powerplay_omo=0;
				double lngth = 0;
				String powerPlay = "";
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team1" 
						+ "$txt_DLS_Value*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team2" 
						+ "$txt_DLS_Value*ACTIVE SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 17 \0", print_writers);
				for(int i=0; i<2; i++) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison"
							+ "$Team"+(i+1)+ "$txt_Team*GEOM*TEXT SET " + matchAllData.getMatch().getInning().get(i).getBatting_team().getTeamName1()+"\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison"
							+ "$Team"+(i+1)+ "$txt_Overs_Value*GEOM*TEXT SET "+CricketFunctions.OverBalls(matchAllData.getMatch().getInning().get(i).getTotalOvers(),matchAllData.getMatch().getInning().get(i).getTotalBalls())+"\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$"
							+ "$Team"+(i+1)+ "$txt_Score*GEOM*TEXT SET " + CricketFunctions.getTeamScore(matchAllData.getMatch().getInning().get(i), "-", false)+"\0", print_writers);
					
					if(matchAllData.getMatch().getInning().get(i).getBatting_team().getTeamName4().equalsIgnoreCase("NEP")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison"
								+ "$Team"+(i+1)+"$img_Shadow*ACTIVE SET 0 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison"
								+ "$Team"+(i+1)+"$img_Shadow*ACTIVE SET 1 \0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison"
							+ "$Team"+(i+1)+"$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + matchAllData.getMatch().getInning().get(i).getBatting_team().getTeamName4() + "\0", print_writers);
					
					if(i == 0) {
						maxRuns = runsIncr = powerplay_omo = 0;
						lngth = 0;
						powerPlay = "";
						for (int j = 0; j < manhattan.size(); j++) {
							if(Integer.valueOf(manhattan.get(j).getOverTotalRuns()) > maxRuns){
								maxRuns = Integer.valueOf(manhattan.get(j).getOverTotalRuns()); // 33 runs came off 34th over
							}
						 	while (maxRuns % 3 != 0) {     // 3 label in y-axis
						 		maxRuns = maxRuns + 1;    // keep incrementing till max runs is divisible by 3. maxRuns = 35
							}
						}
						for(int k = 0; k < 3; k++) {
							runsIncr = maxRuns / 3; 
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
									"$Manhattan_Comparison$Team1$Runs_Axis$" + "$Runs_Data$txt_" + (k+1) + "*GEOM*TEXT SET " + runsIncr*(k+1) + "\0", print_writers);
						}
						
						for(int j = 1; j <= matchAllData.getSetup().getMaxOvers(); j++) {
							
							if(j <= matchAllData.getMatch().getInning().get(i).getFirstPowerplayEndOver()) {
								powerplay_omo = 0;
								powerPlay = "$PowerPlay_1";
							}
							else if(j >= matchAllData.getMatch().getInning().get(i).getSecondPowerplayStartOver() && 
									j <= matchAllData.getMatch().getInning().get(i).getSecondPowerplayEndOver()) {
								powerplay_omo = 1;
								powerPlay = "$PowerPlay_2";
							}
							else if(j >= matchAllData.getMatch().getInning().get(i).getThirdPowerplayStartOver() && 
									j <= matchAllData.getMatch().getInning().get(i).getThirdPowerplayEndOver()) {
								powerplay_omo = 2;
								powerPlay = "$PowerPlay_3";
							}
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team1$Bar$"
									+ j + "$Select_PowerPlay*FUNCTION*Omo*vis_con SET " + powerplay_omo + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team1$Wickets_Axis"
									+ "$Out$Wkt_" + j + "$Select_PowerPlay*FUNCTION*Omo*vis_con SET " + powerplay_omo + "\0", print_writers);
							
							if(j < manhattan.size()) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team1$Bar$"
										+ "$Position*FUNCTION*Grid*num_row SET 1\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team1$Bar$"
										+ "$Position*FUNCTION*Grid*num_col SET " + j + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team1$Wickets_Axis$"
										+ "Out$Wickets*FUNCTION*Grid*num_col SET " + j + "\0", print_writers);
								
								lngth = ((134 * Integer.valueOf(manhattan.get(j).getOverTotalRuns())) / maxRuns);
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team1$Bar$"
										+ j + powerPlay + "$Bar*GEOM*height SET " + lngth + "\0", print_writers);
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team1$Wickets_Axis"
										+ "$Out$Wkt_" + j + powerPlay + "$Select_Wickets*FUNCTION*Omo*vis_con SET " + manhattan.get(j).getOverTotalWickets() + "\0", print_writers);
							}
						}
						if(matchAllData.getSetup().getTargetOvers() != null && !matchAllData.getSetup().getTargetOvers().trim().isEmpty()) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team1" 
									+ "$txt_DLS_Value*ACTIVE SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team1" 
									+ "$txt_DLS_Value*GEOM*TEXT SET " + "("+matchAllData.getSetup().getTargetOvers()+")" + " \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team2" 
									+ "$txt_DLS_Value*ACTIVE SET 0 \0", print_writers);
						}
					} else {
						maxRuns = runsIncr = powerplay_omo = 0;
						lngth = 0;
						powerPlay = "";
						for (int j = 0; j < manhattan2.size(); j++) {
							if(Integer.valueOf(manhattan2.get(j).getOverTotalRuns()) > maxRuns){
								maxRuns = Integer.valueOf(manhattan2.get(j).getOverTotalRuns()); // 33 runs came off 34th over
							}
						 	while (maxRuns % 3 != 0) {     // 3 label in y-axis
						 		maxRuns = maxRuns + 1;    // keep incrementing till max runs is divisible by 3. maxRuns = 35
							}
						}
						for(int k = 0; k < 3; k++) {
							runsIncr = maxRuns / 3; 
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team2$Runs_Axis$"
									+ "$Runs_Data$txt_" + (k+1) + "*GEOM*TEXT SET " + runsIncr*(k+1) + "\0", print_writers);
						}
						
						for(int j = 1; j <= matchAllData.getSetup().getMaxOvers(); j++) {
							
							if(j <= matchAllData.getMatch().getInning().get(i).getFirstPowerplayEndOver()) {
								powerplay_omo = 0;
								powerPlay = "$PowerPlay_1";
							}
							else if(j >= matchAllData.getMatch().getInning().get(i).getSecondPowerplayStartOver() && 
									j <= matchAllData.getMatch().getInning().get(i).getSecondPowerplayEndOver()) {
								powerplay_omo = 1;
								powerPlay = "$PowerPlay_2";
							}
							else if(j >= matchAllData.getMatch().getInning().get(i).getThirdPowerplayStartOver() && 
									j <= matchAllData.getMatch().getInning().get(i).getThirdPowerplayEndOver()) {
								powerplay_omo = 2;
								powerPlay = "$PowerPlay_3";
							}
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
									"$Manhattan_Comparison$Team2$Bar$" + j + "$Select_PowerPlay*FUNCTION*Omo*vis_con SET " + powerplay_omo + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
									"$Manhattan_Comparison$Team2$Wickets_Axis$Out$Wkt_" + j + "$Select_PowerPlay*FUNCTION*Omo*vis_con SET " + powerplay_omo + "\0", print_writers);
							
							if(j < manhattan2.size()) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team2$Bar$"
										+ "$Position*FUNCTION*Grid*num_row SET 1\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team2$Bar$"
										+ "$Position*FUNCTION*Grid*num_col SET " + j + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team2$Wickets_Axis$"
										+ "Out$Wickets*FUNCTION*Grid*num_col SET " + j + "\0", print_writers);
								
								lngth = ((134 * Integer.valueOf(manhattan2.get(j).getOverTotalRuns())) / maxRuns);
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team2$Bar$"
										+ j + powerPlay + "$Bar*GEOM*height SET " + lngth + "\0", print_writers);
								
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team2$Wickets_Axis"
										+ "$Out$Wkt_" + j + powerPlay + "$Select_Wickets*FUNCTION*Omo*vis_con SET " + manhattan2.get(j).getOverTotalWickets() + "\0", print_writers);
							}
						}
						if(matchAllData.getSetup().getTargetOvers() != null && !matchAllData.getSetup().getTargetOvers().trim().isEmpty()) {
							if(Integer.valueOf(matchAllData.getSetup().getTargetOvers()) != Integer.valueOf(matchAllData.getMatch().getInning().get(0).getTotalOvers())) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team2" 
										+ "$txt_DLS_Value*ACTIVE SET 1 \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team2" 
										+ "$txt_DLS_Value*GEOM*TEXT SET " + "("+matchAllData.getSetup().getTargetOvers()+")" + " \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team1" 
										+ "$txt_DLS_Value*ACTIVE SET 0 \0", print_writers);
							}else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team1" 
										+ "$txt_DLS_Value*ACTIVE SET 1 \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team1" 
										+ "$txt_DLS_Value*GEOM*TEXT SET " + "("+matchAllData.getSetup().getTargetOvers()+")" + " \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team2" 
										+ "$txt_DLS_Value*GEOM*TEXT SET " + "("+matchAllData.getSetup().getTargetOvers()+")" + " \0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Manhattan_Comparison$Team2" 
										+ "$txt_DLS_Value*ACTIVE SET 1 \0", print_writers);
							}
						}
					}
					
				}
				break;
			}
			
		break;
			
		case "Control_F10":
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023: case Constants.ISPL:
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 11 \0", print_writers);
				
				int maxRuns = 0,runsIncr = 0,powerplay_omo=0;
				double lngth = 0;
				String powerPlay = "";
				System.out.println("manhattan size "+manhattan.size());
				for (int j = 0; j < manhattan.size(); j++) {
					System.out.println("Inn number : "+manhattan.get(j).getInningNumber() +" RUNS "+Integer.valueOf(manhattan.get(j).getOverTotalRuns()));
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
//						System.out.println("OVERS : " + j + " RUNS/WICKETS : " + manhattan.get(j).getOverTotalRuns() + "/" + manhattan.get(j).getOverTotalWickets() 
//								+ " Over - " + manhattan.get(j).getOverNumber());
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
		case "Shift_P":  
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023: case Constants.ISPL:
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
						"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
						+ "$SubTitle$Data$txt_SubTitle*GEOM*TEXT SET " + "ICC U19 MEN'S CWC 2024" + "\0", print_writers);
				
				for(int i=0; i<2; i++) {
					if(i==0) {
						containerName ="$Normal";
					}else {
						containerName ="$Highlight";
					}
					if(tournament.getMatches() == 0) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$1$Data$Stat_1" + containerName + "$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$1$Data$Stat_1" + containerName + "$txt_Fig*GEOM*TEXT SET " + tournament.getMatches() + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
							+ "$Stats$1$Data$Stat_2" + containerName + "$txt_Desig*GEOM*TEXT SET " + "RUNS" + "\0", print_writers);

					if(tournament.getRuns()==0) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$1$Data$Stat_2" + containerName + "$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$1$Data$Stat_2" + containerName + "$txt_Fig*GEOM*TEXT SET " + String.format("%,d\n", tournament.getRuns()) + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
							+ "$Stats$2$Data$Stat_1" + containerName + "$txt_Desig*GEOM*TEXT SET " + "FIFTIES" + "\0", print_writers);
					
					if(tournament.getFifty()==0) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$2$Data$Stat_1" + containerName + "$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$2$Data$Stat_1" + containerName + "$txt_Fig*GEOM*TEXT SET " + tournament.getFifty() + "\0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
							+ "$Stats$2$Data$Stat_2" + containerName + "$txt_Desig*GEOM*TEXT SET " + "HUNDREDS" + "\0", print_writers);
					
					if(tournament.getHundreds() == 0) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$2$Data$Stat_2" + containerName + "$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
							+ "$Stats$2$Data$Stat_2" + containerName + "$txt_Fig*GEOM*TEXT SET " + tournament.getHundreds() + "\0", print_writers);
					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
						+ "$Stats$3$Data$Stat_1" + containerName + "$txt_Desig*GEOM*TEXT SET " + "AVERAGE" + "\0", print_writers);

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Select_GraphicsType$Profile$Stats"
						+ "$Stats$3$Data$Stat_1" + containerName + "$txt_Fig*GEOM*TEXT SET " + CricketFunctions.getAverage(tournament.getInnings(), tournament.getNot_out(), tournament.getRuns(), 2, "-") + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
						+ "$Stats$3$Data$Stat_2" + containerName + "$txt_Desig*GEOM*TEXT SET " + "STRIKE RATE" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
						+ "$Stats$3$Data$Stat_2" + containerName + "$txt_Fig*GEOM*TEXT SET " + CricketFunctions.generateStrikeRate(tournament.getRuns(), tournament.getBallsFaced(), 1) + "\0", print_writers);

					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
						+ "$Stats$4$Data$Stat_1" + containerName + "$txt_Desig*GEOM*TEXT SET " + "BEST" + "\0", print_writers);
					
					for(int j=0;j<= top_batsman_beststats.size()-1;j++) {
						if(top_batsman_beststats.get(j).getPlayerId() == player.getPlayerId()) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$4$Data$Stat_1" + containerName + "$Justify$txt_VersusTeam*GEOM*TEXT SET " + "v "+ top_batsman_beststats.get(j).getOpponentTeam().getTeamName1() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$4$Data$Stat_1" + containerName + "$Justify$txt_VersusLOcation*GEOM*TEXT SET " + "AT "+ top_batsman_beststats.get(j).getWhichVenue().getCity() + "\0", print_writers);
							if(top_batsman_beststats.get(j).getBestEquation() % 2 == 0) {
								if(top_batsman_beststats.get(j).getBestEquation()/2 == 0) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
											+ "$Stats$4$Data$Stat_1" + containerName + "$Justify$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
								}else {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
											+ "$Stats$4$Data$Stat_1" + containerName + "$Justify$txt_Fig*GEOM*TEXT SET " + String.valueOf(top_batsman_beststats.get(j).getBestEquation()/2) + "\0", print_writers);
								}
							}else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
										+ "$Stats$4$Data$Stat_1" + containerName + "$Justify$txt_Fig*GEOM*TEXT SET " + (top_batsman_beststats.get(j).getBestEquation()-1) / 2 + "*" + "\0", print_writers);
							}
							break;
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$4$Data$Stat_1" + containerName + "$Justify$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
						}
					}
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$PhotoPart"
						+ "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
				
//				if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
//					if(!new File("\\\\"+config.getSecondaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 + 
//							player.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//						return "Photo not found in "+config.getSecondaryIpAddress();
//					}
//					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$PhotoPart"
//							+ "$Photo$img_PlayerPhoto*TEXTURE*IMAGE SET "+"\\\\"+config.getSecondaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 
//							+ player.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
//					
//				}
				
//				if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 + 
//						player.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//					return "Photo not found in "+config.getPrimaryIpAddress();
//				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$PhotoPart"
						+ "$Photo$img_PlayerPhoto*TEXTURE*IMAGE SET "+ Constants.ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 
						+ player.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
				break;
			}
			break;
			
			
			
			
		case "Control_d":  
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023: case Constants.ISPL:
				
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
						+ "$Stats$3$Data$Stat_1" + containerName + "$txt_Fig*GEOM*TEXT SET " + CricketFunctions.getAverage(stat.getInnings(), stat.getNot_out(), stat.getRuns(), 2, "-") + "\0", print_writers);
					
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
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$PhotoPart"
						+ "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
				
//				if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
//					if(!new File("\\\\"+config.getSecondaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 + 
//							player.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//						return "Photo not found in "+config.getSecondaryIpAddress();
//					}
//					CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$PhotoPart"
//							+ "$Photo$img_PlayerPhoto*TEXTURE*IMAGE SET "+"\\\\"+config.getSecondaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 
//							+ player.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
//					
//				}
				
//				if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 + 
//						player.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//					return "Photo not found in "+config.getPrimaryIpAddress();
//				}
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$PhotoPart"
						+ "$Photo$img_PlayerPhoto*TEXTURE*IMAGE SET "+ Constants.ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 
						+ player.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
				break;
			}
			break;
		
		case "Shift_Q":
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023: case Constants.ISPL:
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + 
							"$Select_GraphicsType*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
							+ "$SubTitle$Data$txt_SubTitle*GEOM*TEXT SET " + "ICC U19 MEN'S CWC 2024" + "\0", print_writers);
					
					for(int i=0; i<2; i++) {
						if(i==0) {
							containerName ="$Normal";
						}else {
							containerName ="$Highlight";
						}
						if(tournament.getMatches()==0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$1$Data$Stat_1" + containerName + "$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$1$Data$Stat_1"+ containerName +"$txt_Fig*GEOM*TEXT SET " + tournament.getMatches() + "\0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$1$Data$Stat_2$"+ containerName +"$txt_Desig*GEOM*TEXT SET " + "WICKETS" + "\0", print_writers);
						if(tournament.getWickets()==0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$1$Data$Stat_2"+ containerName +"$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$1$Data$Stat_2"+ containerName +"$txt_Fig*GEOM*TEXT SET " + tournament.getWickets() + "\0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$2$Data$Stat_1"+ containerName +"$txt_Desig*GEOM*TEXT SET " + "ECONOMY" + "\0", print_writers);
						if(tournament.getRunsConceded() == 0 && tournament.getBallsBowled() == 0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$2$Data$Stat_1"+ containerName +"$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$2$Data$Stat_1"+ containerName +"$txt_Fig*GEOM*TEXT SET " + CricketFunctions.getEconomy(tournament.getRunsConceded(), tournament.getBallsBowled(), 2, "-") + "\0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$2$Data$Stat_2"+ containerName +"$txt_Desig*GEOM*TEXT SET " + "AVERAGE" + "\0", print_writers);
						if(tournament.getWickets()<=0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$2$Data$Stat_2"+ containerName +"$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$2$Data$Stat_2"+ containerName +"$txt_Fig*GEOM*TEXT SET " + String.format("%.01f", (float)tournament.getRunsConceded() / 
											(float)(tournament.getWickets())) + "\0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$3$Data$Stat_1"+ containerName +"$txt_Desig*GEOM*TEXT SET " + "STRIKE RATE" + "\0", print_writers);
						if(tournament.getWickets()<=0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$3$Data$Stat_1"+ containerName +"$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$3$Data$Stat_1"+ containerName +"$txt_Fig*GEOM*TEXT SET " + String.format("%.01f", (float)tournament.getBallsBowled() / 
											(float)(tournament.getWickets())) + "\0", print_writers);
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$3$Data$Stat_2"+ containerName +"$txt_Desig*GEOM*TEXT SET " + "5WI" + "\0", print_writers);
						if(tournament.getFiveWicketHaul() == 0) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$3$Data$Stat_2"+ containerName +"$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
									+ "$Stats$3$Data$Stat_2"+ containerName +"$txt_Fig*GEOM*TEXT SET " + tournament.getFiveWicketHaul() + "\0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$4$Data$Stat_1"+ containerName +"$txt_Desig*GEOM*TEXT SET " + "BEST" + "\0", print_writers);
						
						
						for(int j=0;j<= top_bowler_beststats.size()-1;j++) {
							if(top_bowler_beststats.get(j).getPlayerId() == player.getPlayerId()) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
										+ "$Stats$4$Data$Stat_1"+ containerName +"$Justify$txt_VersusTeam*GEOM*TEXT SET " + "v "+ top_bowler_beststats.get(j).getOpponentTeam().getTeamName1() + "\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
										+ "$Stats$4$Data$Stat_1"+ containerName +"$Justify$txt_VersusLOcation*GEOM*TEXT SET " + "AT "+ top_bowler_beststats.get(j).getWhichVenue().getCity() + "\0", print_writers);
								if(top_bowler_beststats.get(j).getBestEquation() % 1000 > 0) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
											+ "$Stats$4$Data$Stat_1"+ containerName +"$Justify$txt_Fig*GEOM*TEXT SET " + ((top_bowler_beststats.get(j).getBestEquation() / 1000) +1) + "-" + (1000 - (top_bowler_beststats.get(j).getBestEquation() % 1000)) + "\0", print_writers);
									
								}
								else if(top_bowler_beststats.get(j).getBestEquation() % 1000 < 0) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
											+ "$Stats$4$Data$Stat_1"+ containerName +"$Justify$txt_Fig*GEOM*TEXT SET " + (top_bowler_beststats.get(j).getBestEquation() / 1000) + "-" + Math.abs(top_bowler_beststats.get(j).getBestEquation()) + "\0", print_writers);
								}
								break;
							}else if(top_bowler_beststats.get(j).getPlayerId() != player.getPlayerId()) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$Stats"
								+ "$Stats$4$Data$Stat_1"+ containerName +"$Justify$txt_Fig*GEOM*TEXT SET " + "-" + "\0", print_writers);
							}
						}
					}
					
//					if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
//						if(!new File("\\\\"+config.getSecondaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 +  player.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//							return "Photo not found in "+config.getSecondaryIpAddress();
//						}
//						CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$PhotoPart"
//							+ "$Photo$img_PlayerPhoto*TEXTURE*IMAGE SET "+"\\\\"+config.getSecondaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 
//							+ player.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
//					}
//					
//					if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 +  player.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//						return "Photo not found in "+config.getPrimaryIpAddress();
//					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$PhotoPart"
							+ "$Photo$img_PlayerPhoto*TEXTURE*IMAGE SET "+ Constants.ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 
							+ player.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$PhotoPart"
							+ "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
					
				break;
			}
			break;
			
			
			
			
		case "Control_e":
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023: case Constants.ISPL:
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
					
//					if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
//						if(!new File("\\\\"+config.getSecondaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 +  player.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//							return "Photo not found in "+config.getSecondaryIpAddress();
//						}
//						CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$PhotoPart"
//							+ "$Photo$img_PlayerPhoto*TEXTURE*IMAGE SET "+"\\\\"+config.getSecondaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 
//							+ player.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
//					}
//					
//					if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 +  player.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//						return "Photo not found in "+config.getPrimaryIpAddress();
//					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$PhotoPart"
							+ "$Photo$img_PlayerPhoto*TEXTURE*IMAGE SET "+ Constants.ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_2048 
							+ player.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Profile$PhotoPart"
							+ "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
					
				break;
			}
			break;

		case "Control_F7": case "Control_F8": //Double Teams-PlayingXi
			DoubleTeamsAndPlayingXiBody(WhichSide, whatToProcess, matchAllData, WhichInning);
			break;
		
		case "Alt_z":
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023: case Constants.ISPL:
				String highlight = "$Highlight";
				String dehighlight = "$Dehighlight";
				rowId=0;
				for(int i=1;i<=PlayingXI.size();i++) {
					if(PlayingXI.get(i-1).getSquad() != null) {
						if(i<=8) {
							rowId = i;
							containerName = "$Top";
						}else if(i>8) {
							rowId = i-8;
							containerName = "$Bottom";
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
								"$Select_In_Out*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						
						if(PlayingXI.get(i-1).getRole().equalsIgnoreCase(CricketUtil.BATSMAN) || PlayingXI.get(i-1).getRole().equalsIgnoreCase("BAT/KEEPER")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Role*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Role$Single_Style1$txt_Bat*GEOM*TEXT SET " + "BAT" + "\0", print_writers);
						}
						else if(PlayingXI.get(i-1).getRole().equalsIgnoreCase(CricketUtil.BOWLER)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Role*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Role$Single_Style2$txt_Bowl*GEOM*TEXT SET " + CricketFunctions.getBowlerType(PlayingXI.get(i-1).getBowlingStyle())
									.replace("PACE", "SEAM") + "\0", print_writers);
						}
						else if(PlayingXI.get(i-1).getRole().equalsIgnoreCase("ALL-ROUNDER")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Role*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Role$Double_Style$txt_Bat*GEOM*TEXT SET " + "BAT" + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName
									+ "$Photo_" + rowId + "$Select_Role$Double_Style$txt_Bowl*GEOM*TEXT SET " + 
									CricketFunctions.getBowlerType(PlayingXI.get(i-1).getBowlingStyle()).replace("PACE", "SEAM") + "\0", print_writers);
						}
						
						if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN) ||
								PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")){
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Captain*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						}else if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Captain*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Role*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Role$Single_Style2$txt_Bowl*GEOM*TEXT SET " + "KEEPER" + "\0", print_writers);
						}
						else if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Captain*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Role*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Role$Single_Style2$txt_Bowl*GEOM*TEXT SET " + "KEEPER" + "\0", print_writers);
						}
						else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Captain*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
								"$img_Photo*TEXTURE*IMAGE SET "+Constants.ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 + PlayingXI.get(i-1).getPhoto() + 
								CricketUtil.PNG_EXTENSION + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName
								+ "$Photo_" + rowId + "$Gradient$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName
								+ "$Photo_" + rowId + dehighlight  + "$txt_PlayerName*GEOM*TEXT SET " + PlayingXI.get(i-1).getTicker_name() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
								"$StatGrp$txt_StatHead*GEOM*TEXT SET " + "" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
								"$StatGrp$txt_StatValue*GEOM*TEXT SET " + "-" + "\0", print_writers);
						
						if(!WhichType.equalsIgnoreCase("role")) {
							for(int r=0;r<team_change.size();r++) {
								if(team_change.get(r).contains("IN")) {
									if(Integer.valueOf(team_change.get(r).split(" ")[1]) == PlayingXI.get(i-1).getPlayerId()) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
												"$Select_In_Out*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
									}
								}
							}
							for(Tournament ts : tournament_stats) {
								if(ts.getPlayerId() == PlayingXI.get(i-1).getPlayerId()) {
									if(WhichType.equalsIgnoreCase("runs")) {
										if(ts.getRuns() > 0) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
													"$StatGrp$txt_StatValue*GEOM*TEXT SET " + ts.getRuns() + "\0", print_writers);
										}
									}else if(WhichType.equalsIgnoreCase("wickets")) {
										if(ts.getWickets() > 0) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
													"$StatGrp$txt_StatValue*GEOM*TEXT SET " + ts.getWickets() + "\0", print_writers);
										}
									}else if(WhichType.equalsIgnoreCase("matches")) {
										if(ts.getMatches() > 0) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
													"$StatGrp$txt_StatValue*GEOM*TEXT SET " + ts.getMatches() + "\0", print_writers);
										}
									}
								}
							}
						}
					}
				}
				
				rowId=3;
				for(int j=1; j<=otherSquad.size(); j++) {
					if(otherSquad.get(j-1).getSquad() != null) {
						rowId = rowId + 1;
						containerName = "$Bottom";
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
								"$Name$Select_Dropped*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
								"$Select_In_Out*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						
						if(otherSquad.get(j-1).getRole().equalsIgnoreCase(CricketUtil.BATSMAN) || otherSquad.get(j-1).getRole().equalsIgnoreCase("BAT/KEEPER")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Role*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Role$Single_Style1$txt_Bat*GEOM*TEXT SET " + "BAT" + "\0", print_writers);
						}
						else if(otherSquad.get(j-1).getRole().equalsIgnoreCase(CricketUtil.BOWLER)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Role*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Role$Single_Style2$txt_Bowl*GEOM*TEXT SET " + CricketFunctions.getBowlerType(otherSquad.get(j-1).getBowlingStyle())
									.replace("PACE", "SEAM") + "\0", print_writers);
						}
						else if(otherSquad.get(j-1).getRole().equalsIgnoreCase("ALL-ROUNDER")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Role*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
									"$Select_Role$Double_Style$txt_Bat*GEOM*TEXT SET " + "BAT" + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName
									+ "$Photo_" + rowId + "$Select_Role$Double_Style$txt_Bowl*GEOM*TEXT SET " + 
									CricketFunctions.getBowlerType(otherSquad.get(j-1).getBowlingStyle()).replace("PACE", "SEAM") + "\0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName
								+ "$Photo_" + rowId + "$img_Photo*TEXTURE*IMAGE SET "+Constants.ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 
								+ otherSquad.get(j-1).getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName
								+ "$Photo_" + rowId + "$Gradient$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + team.getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName
								+ "$Photo_" + rowId + highlight  + "$txt_PlayerName*GEOM*TEXT SET " + otherSquad.get(j-1).getTicker_name() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
								"$StatGrp$txt_StatHead*GEOM*TEXT SET " + "" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
								"$StatGrp$txt_StatValue*GEOM*TEXT SET " + "-" + "\0", print_writers);
						
						if(!WhichType.equalsIgnoreCase("role")) {
							for(int r=0;r<team_change.size();r++) {
								if(team_change.get(r).contains("OUT")) {
									if(Integer.valueOf(team_change.get(r).split(" ")[1]) == otherSquad.get(j-1).getPlayerId()) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
												"$Select_In_Out*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
									}
								}
							}
							for(Tournament ts : tournament_stats) {
								if(ts.getPlayerId() == otherSquad.get(j-1).getPlayerId()) {
									if(WhichType.equalsIgnoreCase("runs")) {
										if(ts.getRuns() > 0) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
													"$StatGrp$txt_StatValue*GEOM*TEXT SET " + ts.getRuns() + "\0", print_writers);
										}
									}else if(WhichType.equalsIgnoreCase("wickets")) {
										if(ts.getWickets() > 0) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
													"$StatGrp$txt_StatValue*GEOM*TEXT SET " + ts.getWickets() + "\0", print_writers);
										}
									}else if(WhichType.equalsIgnoreCase("matches")) {
										if(ts.getMatches() > 0) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$AllGraphics" + containerName + "$Photo_" + rowId + 
													"$StatGrp$txt_StatValue*GEOM*TEXT SET " + ts.getMatches() + "\0", print_writers);
										}
									}
								}
							}
						}
					}
				}
				break;
			}
			break;
		case "Shift_F10":
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023: case Constants.ISPL:
				
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
//			if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 +  battingCardList.get(0).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//				return "Photo not found in "+config.getPrimaryIpAddress();
//			}
//			if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 +  battingCardList.get(1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//				return "Photo not found in "+config.getPrimaryIpAddress();
//			}
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership"
					+ "$Photo$PhotoGrp1$img_PlayerPhoto1*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 
					+ battingCardList.get(0).getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership"
					+ "$Photo$PhotoGrp2$img_PlayerPhoto1*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 
					+ battingCardList.get(1).getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);

//			if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
//				if(!new File("\\\\"+config.getSecondaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 +  battingCardList.get(0).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//					return "Photo not found in "+config.getSecondaryIpAddress();
//				}
//				if(!new File("\\\\"+config.getSecondaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 +  battingCardList.get(1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//					return "Photo not found in "+config.getSecondaryIpAddress();
//				}
//				CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership"
//						+ "$Photo$PhotoGrp1$img_PlayerPhoto1*TEXTURE*IMAGE SET " +"\\\\"+config.getSecondaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 
//						+ battingCardList.get(0).getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
//				
//				CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership"
//						+ "$Photo$PhotoGrp2$img_PlayerPhoto1*TEXTURE*IMAGE SET " +"\\\\"+config.getSecondaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 
//						+ battingCardList.get(1).getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
//			}
			
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
					+ "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 7 \0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership$1$fig_Runs"
					+ "*GEOM*TEXT SET " + inning.getPartnerships().get(inning.getPartnerships().size() - 1).getTotalRuns() + "*" + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership$1$fig_Balls"
					+ "*GEOM*TEXT SET " + inning.getPartnerships().get(inning.getPartnerships().size() - 1).getTotalBalls() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership$1$txt_Balls"
					+ "*GEOM*TEXT SET " + "BALL"+ CricketFunctions.Plural(inning.getPartnerships().get(inning.getPartnerships().size() - 1).getTotalBalls()).toUpperCase() + "\0", print_writers);
			
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
					+ "$Data$txt_Runs*GEOM*TEXT SET " + inning.getPartnerships().get(inning.getPartnerships().size() - 1).getFirstBatterRuns() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Main$AllGraphics$Side" + WhichSide + "$Partnership$4"
					+ "$Data$txt_Ball*GEOM*TEXT SET " + inning.getPartnerships().get(inning.getPartnerships().size() - 1).getFirstBatterBalls() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership$5"
					+ "$Data$txt_Name*GEOM*TEXT SET " + battingCardList.get(1).getPlayer().getTicker_name() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership$5"
					+ "$Data$txt_Runs*GEOM*TEXT SET " + inning.getPartnerships().get(inning.getPartnerships().size() - 1).getSecondBatterRuns() + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership$5"
					+ "$Data$txt_Ball*GEOM*TEXT SET " + inning.getPartnerships().get(inning.getPartnerships().size() - 1).getSecondBatterBalls() + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership"
				+ "$Photo$PhotoGrp1$img_PlayerShadow1*TEXTURE*IMAGE SET "+ Constants.ICC_U19_2023_PHOTOS_PATH + 
					Constants.LEFT_1024 + battingCardList.get(0).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION  + "\0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership"
					+ "$Photo$PhotoGrp2$img_PlayerShadow1*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + 
					battingCardList.get(1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership"
					+ "$Photo$PhotoGrp1$img_PlayerShadow1*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + 
					Constants.LEFT_1024 + battingCardList.get(0).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION  + "\0", print_writers);

			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership"
					+ "$Photo$PhotoGrp2$img_PlayerShadow1*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + 
					battingCardList.get(1).getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Partnership"
				+ "$Flag$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH + inning.getBatting_team().getTeamName4() + "\0", print_writers);
			break;
			
		case "Alt_F9": case "Alt_F10":
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023: case Constants.ISPL:
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
						+ "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 8 \0", print_writers);
				
				if(WhichStyle.equalsIgnoreCase("Age")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$TeamSingle$Tittle$txt_Tittle_1*GEOM*TEXT SET " + "AGE" + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$TeamSingle$Tittle$txt_Tittle_2*GEOM*TEXT SET " + "ROLE" + "\0", print_writers);
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
				
				switch (whatToProcess) {
				case "Alt_F9":
					for(int i=1;i<=PlayingXI.size();i++) {
						rowId = i-1;
						
						stat = statisticsList.stream().filter(stat -> stat.getPlayer_id() == PlayingXI.get(rowId).getPlayerId()).findAny().orElse(null);
						if(stat == null) {
							return "populatePlayerProfile: No stats found for player id [" + PlayingXI.get(rowId).getPlayerId() + "] from database is returning NULL";
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$TeamSingle$"+i+"$txt_Name*GEOM*TEXT SET " + PlayingXI.get(i-1).getFull_name() + "\0", print_writers);
						
						if(WhichStyle.equalsIgnoreCase("Age")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$TeamSingle$" + i + "$fig_1*GEOM*TEXT SET " + PlayingXI.get(i-1).getAge() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$TeamSingle$" + i + "$txt_Description*GEOM*TEXT SET " + "" + "\0", print_writers);
							
							if(PlayingXI.get(i-1).getRole().equalsIgnoreCase(CricketUtil.BATSMAN) || PlayingXI.get(i-1).getRole().equalsIgnoreCase("BAT/KEEPER")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$"+i+"$fig_2*GEOM*TEXT SET "+"BATTER"+"\0", print_writers);
							}
							else if(PlayingXI.get(i-1).getRole().equalsIgnoreCase(CricketUtil.BOWLER)) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$"+i+"$fig_2*GEOM*TEXT SET "+"BOWLER"+"\0", print_writers);
							}
							else if(PlayingXI.get(i-1).getRole().equalsIgnoreCase("ALL-ROUNDER")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$"+i+"$fig_2*GEOM*TEXT SET "+"ALL-ROUNDER"+"\0", print_writers);
							}
							
							if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)){
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$"+i+"$txt_Captain*GEOM*TEXT SET "+"(C)"+"\0", print_writers);
							}
							else if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$"+i+"$txt_Captain*GEOM*TEXT SET "+""+"\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$"+i+"$fig_2*GEOM*TEXT SET "+"KEEPER"+"\0", print_writers);
							}
							else if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$"+i+"$txt_Captain*GEOM*TEXT SET "+"(C)"+"\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$"+i+"$fig_2*GEOM*TEXT SET "+"KEEPER"+"\0", print_writers);
							}else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$"+i+"$txt_Captain*GEOM*TEXT SET "+""+"\0", print_writers);
							}
							
						}
						else {
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
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$TeamSingle$" + i + "$fig_1*GEOM*TEXT SET " + stat.getMatches() + "\0", print_writers);
							switch(WhichType.toUpperCase()) {
							case "RUNS":
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$" + i + "$fig_2*GEOM*TEXT SET " + stat.getRuns() + "\0", print_writers);
								break;
							case "AVERAGE":
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$" + i + "$fig_2*GEOM*TEXT SET " + CricketFunctions.getAverage(stat.getInnings(), stat.getNot_out(), 
												stat.getRuns(), 2, "-") + "\0", print_writers);
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
				case "Alt_F10":
					for(int i=1;i<=PlayingXI.size();i++) {
						rowId = i-1;
						
						tournament = tournament_stats.stream().filter(ts -> ts.getPlayerId() == PlayingXI.get(rowId).getPlayerId()).findAny().orElse(null);
						if(tournament == null) {
							return "populatePlayerProfile: No stats found for player id [" + PlayingXI.get(rowId).getPlayerId() + "] from database is returning NULL";
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$TeamSingle$"+i+"$txt_Name*GEOM*TEXT SET " + PlayingXI.get(i-1).getFull_name() + "\0", print_writers);
						
						if(WhichStyle.equalsIgnoreCase("Age")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$TeamSingle$" + i + "$fig_1*GEOM*TEXT SET " + PlayingXI.get(i-1).getAge() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$TeamSingle$" + i + "$txt_Description*GEOM*TEXT SET " + "" + "\0", print_writers);
							
							if(PlayingXI.get(i-1).getRole().equalsIgnoreCase(CricketUtil.BATSMAN) || PlayingXI.get(i-1).getRole().equalsIgnoreCase("BAT/KEEPER")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$"+i+"$fig_2*GEOM*TEXT SET "+"BATTER"+"\0", print_writers);
							}
							else if(PlayingXI.get(i-1).getRole().equalsIgnoreCase(CricketUtil.BOWLER)) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$"+i+"$fig_2*GEOM*TEXT SET "+"BOWLER"+"\0", print_writers);
							}
							else if(PlayingXI.get(i-1).getRole().equalsIgnoreCase("ALL-ROUNDER")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$"+i+"$fig_2*GEOM*TEXT SET "+"ALL-ROUNDER"+"\0", print_writers);
							}
							
							if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)){
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$"+i+"$txt_Captain*GEOM*TEXT SET "+"(C)"+"\0", print_writers);
							}
							else if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$"+i+"$txt_Captain*GEOM*TEXT SET "+""+"\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$"+i+"$fig_2*GEOM*TEXT SET "+"KEEPER"+"\0", print_writers);
							}
							else if(PlayingXI.get(i-1).getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$"+i+"$txt_Captain*GEOM*TEXT SET "+"(C)"+"\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$"+i+"$fig_2*GEOM*TEXT SET "+"KEEPER"+"\0", print_writers);
							}else {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$"+i+"$txt_Captain*GEOM*TEXT SET "+""+"\0", print_writers);
							}
						}
						else {
							
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
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$TeamSingle$" + i + "$fig_1*GEOM*TEXT SET " + tournament.getMatches() + "\0", print_writers);
							
							switch(WhichType.toUpperCase()) {
							case "RUNS":
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$" + i + "$fig_2*GEOM*TEXT SET " + tournament.getRuns() + "\0", print_writers);
								break;
							case "AVERAGE":
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$" + i + "$fig_2*GEOM*TEXT SET " + CricketFunctions.getAverage(tournament.getInnings(), 
												tournament.getNot_out(), tournament.getRuns(), 2, "-") + "\0", print_writers);
								break;
							case "STRIKERATE":
								if(tournament.getBallsFaced()<=0) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
											+ "$TeamSingle$" + i + "$fig_2*GEOM*TEXT SET " + "-" + "\0", print_writers);
								}else {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
											+ "$TeamSingle$" + i + "$fig_2*GEOM*TEXT SET " + CricketFunctions.generateStrikeRate(tournament.getRuns(), 
													tournament.getBallsFaced(), 1) + "\0", print_writers);
								}
								break;
							case "WICKETS":
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$" + i + "$fig_2*GEOM*TEXT SET " + tournament.getWickets() + "\0", print_writers);
								break;
							case "ECONOMY":
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
										+ "$TeamSingle$" + i + "$fig_2*GEOM*TEXT SET " + CricketFunctions.getEconomy(tournament.getRunsConceded(), 
											tournament.getBallsBowled(), 2, "-") + "\0", print_writers);
								break;
							}
						}
					}
					break;
				}
				break;
			}
			break;
			
		case "Shift_D":
			TargetBody(WhichSide, whatToProcess, matchAllData, WhichInning);
			break;
			
		case "Control_b":
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023: case Constants.ISPL:
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_In_At$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  inning.getBatting_team().getTeamName4() + "\0", print_writers);
				for(BattingCard bc : inning.getBattingCard()) {
					if(bc.getPlayerId() == player.getPlayerId()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_In_At$txt_Name*GEOM*TEXT SET  " + player.getFull_name() + "\0", print_writers);
//						if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
//							if(!new File("\\\\"+config.getSecondaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + player.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//								return "Photo not found for player in "+config.getSecondaryIpAddress();
//							}
//							CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*BACK_LAYER*TREE*$gfx_In_At$img_Player*TEXTURE*IMAGE SET " +"\\\\"+config.getSecondaryIpAddress()
//							+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + player.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
//							
//						}
//						
//						if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + player.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//							return "Photo not found for player "+config.getPrimaryIpAddress();
//						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_In_At$img_Player*TEXTURE*IMAGE SET "+ Constants.ICC_U19_2023_PHOTOS_PATH  + Constants.RIGHT_1024 + player.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_In_At$fig_In_At_1*GEOM*TEXT SET  " + bc.getBatterPosition() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_In_At$fig_In_At_2*GEOM*TEXT SET  " + bc.getBatterPosition() + "\0", print_writers);
						break;
					}
				}
				
				break;
			}
			break;
			
		case "Alt_m":
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023: case Constants.ISPL:
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  team.getTeamName4() + "\0", print_writers);
				Collections.sort(inning.getBattingCard());
				for(BattingCard bc : inning.getBattingCard()) {
					if(bc.getPlayerId() == FirstPlayerId) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$txt_Name*GEOM*TEXT SET  " + bc.getPlayer().getFull_name() + "\0", print_writers);
//						if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
//							if(!new File("\\\\"+config.getSecondaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + bc.getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//								return "Photo not found for player in"+config.getSecondaryIpAddress();
//							}
//							CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$img_PlayerShadow*TEXTURE*IMAGE SET " +"\\\\"+config.getSecondaryIpAddress()
//							+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + bc.getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
//							CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$img_Player*TEXTURE*IMAGE SET " +"\\\\"+config.getSecondaryIpAddress()
//							+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + bc.getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
//						}
//						
//						if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + bc.getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//							return "Photo not found for player "+config.getPrimaryIpAddress();
//						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$img_PlayerShadow*TEXTURE*IMAGE SET " 
								+ Constants.ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + bc.getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$img_Player*TEXTURE*IMAGE SET " 
								+ Constants.ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + bc.getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
						
						
						if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$txt_Runs_1*GEOM*TEXT SET  " + bc.getRuns()+"*" + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$txt_Runs_2*GEOM*TEXT SET  " + bc.getRuns() +"*" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$txt_Runs_1*GEOM*TEXT SET  " + bc.getRuns() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$txt_Runs_2*GEOM*TEXT SET  " + bc.getRuns() + "\0", print_writers);
						}
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$txt_text1*GEOM*TEXT SET  "+"OFF " + bc.getBalls() + "\0", print_writers);
						break;
					}
				}
				break;
			}
			break;
		case "Alt_n":
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023: case Constants.ISPL:
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_FLAG_PATH +  team.getTeamName4() + "\0", print_writers);
				rowId = 0;
				for(BowlingCard boc : inning.getBowlingCard()) {
					if(boc.getPlayerId() == FirstPlayerId) {
						rowId = 1;
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$txt_Name*GEOM*TEXT SET  " + boc.getPlayer().getFull_name() + "\0", print_writers);
//						if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
//							if(!new File("\\\\"+config.getSecondaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + boc.getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//								return "Photo not found for player "+config.getSecondaryIpAddress();
//							}
//							CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$img_PlayerShadow*TEXTURE*IMAGE SET " +"\\\\"+config.getSecondaryIpAddress()
//							+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + boc.getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
//							CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$img_Player*TEXTURE*IMAGE SET " +"\\\\"+config.getSecondaryIpAddress()
//							+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + boc.getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
//						}
						
//						if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + boc.getPlayer().getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//							return "Photo not found for player "+config.getPrimaryIpAddress();
//						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$img_PlayerShadow*TEXTURE*IMAGE SET " 
								+ Constants.ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + boc.getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$img_Player*TEXTURE*IMAGE SET "
								+ Constants.ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + boc.getPlayer().getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
						
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$txt_Runs_1*GEOM*TEXT SET  " + boc.getWickets() + "-" + boc.getRuns() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$txt_Runs_2*GEOM*TEXT SET  " + boc.getWickets() + "-" + boc.getRuns() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Milestone$txt_text1*GEOM*TEXT SET  " + CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls())+" OVERS" + "\0", print_writers);
						break;
					}
				}
				if(rowId == 0) {
					return player.getFull_name() + " Not In Bowling Card";
				}
				break;
			}
			break;
		
		case "r":
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023: case Constants.ISPL:
				for(Player plyr : Players) {
					if(plyr.getPlayerId() == Potts.get(0).getPlayerId1()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_POTT_Aramco$Photo1$txt_PlayerName*GEOM*TEXT SET  " + plyr.getTicker_name() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_POTT_Aramco$Photo1$img_Photo*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +
								plyr.getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
						for(Team team : Teams) {
							if(plyr.getTeamId() == team.getTeamId()) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_POTT_Aramco$Photo1$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_POTT_FLAG_PATH +
										team.getTeamName4()+"\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_POTT_Aramco$Photo1$txt_StatHead*GEOM*TEXT SET  " + team.getTeamName1() + "\0", print_writers);
							}
						}
					}
					if(plyr.getPlayerId() == Potts.get(0).getPlayerId2()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_POTT_Aramco$Photo2$txt_PlayerName*GEOM*TEXT SET  " + plyr.getTicker_name() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_POTT_Aramco$Photo2$img_Photo*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +
								plyr.getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
						for(Team team : Teams) {
							if(plyr.getTeamId() == team.getTeamId()) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_POTT_Aramco$Photo2$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_POTT_FLAG_PATH +
										team.getTeamName4()+"\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_POTT_Aramco$Photo2$txt_StatHead*GEOM*TEXT SET  " + team.getTeamName1() + "\0", print_writers);
							}
						}
					}
					if(plyr.getPlayerId() == Potts.get(0).getPlayerId3()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_POTT_Aramco$Photo3$txt_PlayerName*GEOM*TEXT SET  " + plyr.getTicker_name() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_POTT_Aramco$Photo3$img_Photo*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +
								plyr.getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
						for(Team team : Teams) {
							if(plyr.getTeamId() == team.getTeamId()) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_POTT_Aramco$Photo3$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_POTT_FLAG_PATH +
										team.getTeamName4()+"\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_POTT_Aramco$Photo3$txt_StatHead*GEOM*TEXT SET  " + team.getTeamName1() + "\0", print_writers);
							}
						}
					}
					if(plyr.getPlayerId() == Potts.get(0).getPlayerId4()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_POTT_Aramco$Photo4$txt_PlayerName*GEOM*TEXT SET  " + plyr.getTicker_name() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_POTT_Aramco$Photo4$img_Photo*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +
								plyr.getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
						for(Team team : Teams) {
							if(plyr.getTeamId() == team.getTeamId()) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_POTT_Aramco$Photo4$img_Flag*TEXTURE*IMAGE SET " + Constants.ICC_U19_2023_POTT_FLAG_PATH +
										team.getTeamName4()+"\0", print_writers);
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_POTT_Aramco$Photo4$txt_StatHead*GEOM*TEXT SET  " + team.getTeamName1() + "\0", print_writers);
							}
						}
					}
					
				}
				break;
			}
			
			break;
		
		case "m": //MATCH Ident
			
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ISPL:
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$InfoGrp$InfoTextGrp1$txt_Info1A"
						+ "*GEOM*TEXT SET " + "ISPL 2024 - " + matchAllData.getSetup().getMatchIdent() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$InfoGrp$InfoTextGrp1$txt_Info1B"
						+ "*GEOM*TEXT SET " + "ISPL 2024 - " + matchAllData.getSetup().getMatchIdent() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$InfoGrp$InfoTextGrp2$txt_Info2A"
						+ "*GEOM*TEXT SET " + "LIVE FROM " + matchAllData.getSetup().getVenueName() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$InfoGrp$InfoTextGrp2$txt_Info2B"
						+ "*GEOM*TEXT SET " + "LIVE FROM " + matchAllData.getSetup().getVenueName() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp1$LogoGrp$img_TeamLogo"
						+ "*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH +  matchAllData.getSetup().getHomeTeam().getTeamName4() 
						+ "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp1$LogoGrp$img_TeamLogoOverlay"
						+ "*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH +  matchAllData.getSetup().getHomeTeam().getTeamName4() 
						+ "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp1$BaseGrp$img_Base1"
						+ "*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 +  matchAllData.getSetup().getHomeTeam().getTeamName4() 
						+ "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp1$BaseGrp$img_Base2"
						+ "*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 +  matchAllData.getSetup().getHomeTeam().getTeamName4() 
						+ "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp1$BaseGrp$img_Base2$img_TeamLogoBW"
						+ "*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH +  matchAllData.getSetup().getHomeTeam().getTeamName4() 
						+ "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp1$DesignElementsGrp$img_Base1"
						+ "*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 +  matchAllData.getSetup().getHomeTeam().getTeamName4() 
						+ "\0", print_writers);
				
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp2$LogoGrp$img_TeamLogo"
						+ "*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH +  matchAllData.getSetup().getAwayTeam().getTeamName4() 
						+ "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp2$LogoGrp$img_TeamLogoOverlay"
						+ "*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH +  matchAllData.getSetup().getAwayTeam().getTeamName4() 
						+ "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp2$BaseGrp$img_Base1"
						+ "*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 +  matchAllData.getSetup().getAwayTeam().getTeamName4() 
						+ "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp2$BaseGrp$img_Base2"
						+ "*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 +  matchAllData.getSetup().getAwayTeam().getTeamName4() 
						+ "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp2$BaseGrp$img_Base2$img_TeamLogoBW"
						+ "*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH +  matchAllData.getSetup().getAwayTeam().getTeamName4() 
						+ "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp2$DesignElementsGrp$img_Base1"
						+ "*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 +  matchAllData.getSetup().getAwayTeam().getTeamName4() 
						+ "\0", print_writers);
				
				break;
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
			case Constants.ISPL:
				
				String match_name="",newDate = "",date_data = "";
				
				String[] dateSuffix = {
						"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
						
						"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
						
						"th", "st", "nd", "rd", "th", "th", "th", "th", "th","th",
						
						"th", "st"
				};
				
				if(fixture.getMatchnumber() > 9) {
					match_name=fixture.getMatchfilename();
				}else {
					match_name= "MATCH " + fixture.getMatchnumber();
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$InfoGrp$InfoTextGrp1$txt_Info1A"
						+ "*GEOM*TEXT SET " + "ISPL 2024 - " + match_name + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$InfoGrp$InfoTextGrp1$txt_Info1B"
						+ "*GEOM*TEXT SET " + "ISPL 2024 - " + match_name + "\0", print_writers);
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, +1);
				if(fixture.getDate().equalsIgnoreCase(new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime()))) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$InfoGrp$InfoTextGrp2$txt_Info2A"
							+ "*GEOM*TEXT SET " + "TOMORROW - " + fixture.getLocalTime() + " IST - " + fixture.getVenue() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$InfoGrp$InfoTextGrp2$txt_Info2B"
							+ "*GEOM*TEXT SET " + "TOMORROW - " + fixture.getLocalTime() + " IST - " + fixture.getVenue() + "\0", print_writers);
				}else {
					cal.add(Calendar.DATE, -1);
					if(fixture.getDate().equalsIgnoreCase(new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime()))) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$InfoGrp$InfoTextGrp2$txt_Info2A"
								+ "*GEOM*TEXT SET " + "UP NEXT - " + fixture.getVenue() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$InfoGrp$InfoTextGrp2$txt_Info2B"
								+ "*GEOM*TEXT SET " + "UP NEXT - " + fixture.getVenue() + "\0", print_writers);
					}else {
						newDate = fixture.getDate().split("-")[0];
						if(Integer.valueOf(newDate) < 10) {
							newDate = newDate.replaceFirst("0", "");
						}
						date_data = newDate + dateSuffix[Integer.valueOf(newDate)] + " " + 
								Month.of(Integer.valueOf(fixture.getDate().split("-")[1])) + fixture.getDate().split("-")[2];
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$InfoGrp$InfoTextGrp2$txt_Info2A"
								+ "*GEOM*TEXT SET " + date_data + " - " + fixture.getLocalTime() + " IST - " + fixture.getVenue() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$InfoGrp$InfoTextGrp2$txt_Info2B"
								+ "*GEOM*TEXT SET " + date_data + " - " + fixture.getLocalTime() + " IST - " + fixture.getVenue() + "\0", print_writers);
					}
				}
				
				for(Team tm : Teams) {
					if(tm.getTeamId() == fixture.getHometeamid()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp1$LogoGrp$img_TeamLogo"
								+ "*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + tm.getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp1$LogoGrp$img_TeamLogoOverlay"
								+ "*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + tm.getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp1$BaseGrp$img_Base1"
								+ "*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + tm.getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp1$BaseGrp$img_Base2"
								+ "*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + tm.getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp1$BaseGrp$img_Base2$img_TeamLogoBW"
								+ "*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + tm.getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp1$DesignElementsGrp$img_Base1"
								+ "*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + tm.getTeamName4() + "\0", print_writers);
					}
					
					if(tm.getTeamId() == fixture.getAwayteamid()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp2$LogoGrp$img_TeamLogo"
								+ "*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + tm.getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp2$LogoGrp$img_TeamLogoOverlay"
								+ "*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + tm.getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp2$BaseGrp$img_Base1"
								+ "*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + tm.getTeamName4() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp2$BaseGrp$img_Base2"
								+ "*TEXTURE*IMAGE SET " + Constants.ISPL_BASE2 + tm.getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp2$BaseGrp$img_Base2$img_TeamLogoBW"
								+ "*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_BW_PATH + tm.getTeamName4() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_MatchId$TeamGrp2$DesignElementsGrp$img_Base1"
								+ "*TEXTURE*IMAGE SET " + Constants.ISPL_BASE1 + tm.getTeamName4() + "\0", print_writers);
					}
				}
				break;
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
							" " + new SimpleDateFormat("MMMM").format(new SimpleDateFormat("dd-MM-yyyy").parse(fixture.getDate())).toUpperCase() + " " +
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
		case Constants.ISPL:
			switch (whatToProcess) {
			case "Shift_F11":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Select_Graphics"
						+ "$Summary$SummaryData$InfoGrp$txt_Info*GEOM*TEXT SET " + CricketFunctions.generateMatchSummaryStatus(WhichInning, matchAllData, 
							CricketUtil.FULL, "", config.getBroadcaster()).toUpperCase() + "\0", print_writers);
				break;
			case "Control_F7":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$InfoGrp$"
					+ "txt_Info*GEOM*TEXT SET " + CricketFunctions.generateTossResult(matchAllData, CricketUtil.SHORT, CricketUtil.FIELD, CricketUtil.SHORT,
					CricketUtil.ELECTED).replace("toss", "tip-top").toUpperCase() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$LegendGrp$"
						+ "$ZoneLegend_1$txt_Legend*GEOM*TEXT SET " + "NZ: NORTH ZONE" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$LegendGrp$"
						+ "$ZoneLegend_2$txt_Legend*GEOM*TEXT SET " + "EZ: EAST ZONE" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$LegendGrp$"
						+ "$ZoneLegend_3$txt_Legend*GEOM*TEXT SET " + "SZ: SOUTH ZONE" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$LegendGrp$"
						+ "$ZoneLegend_4$txt_Legend*GEOM*TEXT SET " + "WZ: WEST ZONE" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$LegendGrp$"
						+ "$ZoneLegend_5$txt_Legend*GEOM*TEXT SET " + "CZ: CENTRAL ZONE" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$LegendGrp$"
						+ "$ZoneLegend_6$txt_Legend*GEOM*TEXT SET " + "U19: UNDER-19" + "\0", print_writers);
				
				break;
			case "Control_F8":
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$InfoGrp$"
					+ "txt_Info*GEOM*TEXT SET " + CricketFunctions.generateTossResult(matchAllData, CricketUtil.SHORT, CricketUtil.FIELD, CricketUtil.SHORT,
					CricketUtil.ELECTED).replace("toss", "tip-top").toUpperCase() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$LegendGrp$"
						+ "$ZoneLegend_1$txt_Legend*GEOM*TEXT SET " + "NZ: NORTH ZONE" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$LegendGrp$"
						+ "$ZoneLegend_2$txt_Legend*GEOM*TEXT SET " + "EZ: EAST ZONE" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$LegendGrp$"
						+ "$ZoneLegend_3$txt_Legend*GEOM*TEXT SET " + "SZ: SOUTH ZONE" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$LegendGrp$"
						+ "$ZoneLegend_4$txt_Legend*GEOM*TEXT SET " + "WZ: WEST ZONE" + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$LegendGrp$"
						+ "$ZoneLegend_5$txt_Legend*GEOM*TEXT SET " + "CZ: CENTRAL ZONE" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$LegendGrp$"
						+ "$ZoneLegend_6$txt_Legend*GEOM*TEXT SET " + "U19: UNDER-19" + "\0", print_writers);
				
				break;
			
			case "F1": case "F4":
				containerName_2 = "";
				switch (whatToProcess) {
				case "F1":
					if(WhichScoreCard.equalsIgnoreCase("SPLIT")) {
						containerName_2 = "$SplitBatting";
					}else if(WhichScoreCard.equalsIgnoreCase("NORMAL")) {
						containerName_2 = "$NormalBatting";
					}
					break;
				case "F4":
					containerName_2 = "$PartnershipListGrp";
					break;
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + containerName_2 + "$InfoGrp"
						+ "$Select_ChallngeRuns*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				
				if(whatToProcess.equalsIgnoreCase("F1")) {
					for(int i= matchAllData.getEventFile().getEvents().size()-1;i >= 0 ; i--) {
						if(matchAllData.getEventFile().getEvents().get(i).getEventInningNumber() == inning.getInningNumber()) {
							if(matchAllData.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50)) {
								if(matchAllData.getEventFile().getEvents().get(i).getEventExtra().equalsIgnoreCase("-")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + containerName_2 +
											"$InfoGrp$Select_ChallngeRuns*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + containerName_2 + 
											"$InfoGrp$Select_ChallngeRuns$NegativeChallenge$txt_ChallengeValue*GEOM*TEXT SET " + "-" + 
											matchAllData.getEventFile().getEvents().get(i).getEventExtraRuns() + "\0", print_writers);
								}
								else if(matchAllData.getEventFile().getEvents().get(i).getEventExtra().equalsIgnoreCase("+")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + containerName_2 +
											"$InfoGrp$Select_ChallngeRuns*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + containerName_2 +
											"$InfoGrp$Select_ChallngeRuns$PositiveChallenge$txt_ChallengeValue*GEOM*TEXT SET " + "+" +
											matchAllData.getEventFile().getEvents().get(i).getEventExtraRuns() + "\0", print_writers);
								}
							}
						}
					}
				}
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + containerName_2 + "$InfoGrp"
						+ "$ExtrasGrp$txt_ExtrasValue*GEOM*TEXT SET " + inning.getTotalExtras() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + containerName_2 + "$Score_OversGrp"
						+ "$OversGrp$txt_ObversValue*GEOM*TEXT SET " + CricketFunctions.OverBalls(inning.getTotalOvers(),inning.getTotalBalls()) + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + containerName_2 + "$Score_OversGrp"
						 + "$txt_TotalScore*GEOM*TEXT SET " + CricketFunctions.getTeamScore(inning, "-", false) + "\0", print_writers);
				break;
			case "F2":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$SplitBowling$InfoGrp"
						+ "$Select_ChallngeRuns*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$SplitBowling$InfoGrp"
						+ "$ExtrasGrp$txt_ExtrasValue*GEOM*TEXT SET " + inning.getTotalExtras() + "\0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$SplitBowling$Score_OversGrp"
						+ "$OversGrp$txt_ObversValue*GEOM*TEXT SET " + CricketFunctions.OverBalls(inning.getTotalOvers(),inning.getTotalBalls()) + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$SplitBowling$Score_OversGrp"
						 + "$txt_TotalScore*GEOM*TEXT SET " + CricketFunctions.getTeamScore(inning, "-", false) + "\0", print_writers);
				break;	
			}
			break;
		case Constants.ICC_U19_2023:
			switch (whatToProcess) {
			case "Alt_z":
				if(WhichType.equalsIgnoreCase("RUNS")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$FooterAll$txt_Info_1*GEOM*TEXT SET " + "RUNS IN "+ matchAllData.getSetup().getTournament() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$FooterAll$txt_Info_2*GEOM*TEXT SET " + "RUNS IN "+ matchAllData.getSetup().getTournament() + "\0", print_writers);
				}else if(WhichType.equalsIgnoreCase("WICKETS")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$FooterAll$txt_Info_1*GEOM*TEXT SET " + "WICKETS IN "+ matchAllData.getSetup().getTournament() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$FooterAll$txt_Info_2*GEOM*TEXT SET " + "WICKETS IN "+ matchAllData.getSetup().getTournament() + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$FooterAll$txt_Info_1*GEOM*TEXT SET " + matchAllData.getSetup().getTournament() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$FooterAll$txt_Info_2*GEOM*TEXT SET " + matchAllData.getSetup().getTournament() + "\0", print_writers);
				}
//				if(!WhichType.equalsIgnoreCase("role")) {
//					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$FooterAll$txt_Info_1*GEOM*TEXT SET " + WhichType.toUpperCase() + "\0", print_writers);
//					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$FooterAll$txt_Info_2*GEOM*TEXT SET " + WhichType.toUpperCase() + "\0", print_writers);
//				}else {
//					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$FooterAll$txt_Info_1*GEOM*TEXT SET " + "" + "\0", print_writers);
//					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Squad$FooterAll$txt_Info_2*GEOM*TEXT SET " + "" + "\0", print_writers);
//				}
			break;
			case "F1": case "F2": case "F4": case "Control_F1": case "Control_F10": case "Shift_K":
				// Inning for these Gfx are declared in PopulateFfHeader

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Side" + WhichSide + "$Select_FooterType"
						+ "*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Side" + WhichSide + "$Select_FooterType$Score"
						+ "$In_Out$Extras$txt_Extras_Value*GEOM*TEXT SET " + inning.getTotalExtras() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Side"+WhichSide+"$Select_FooterType$Score"
						+ "$In_Out$Overs$txt_Overs_Value*GEOM*TEXT SET " + CricketFunctions.OverBalls(inning.getTotalOvers(),inning.getTotalBalls()) + "\0", print_writers);
				
				if(matchAllData.getSetup().getTargetOvers() != null && !matchAllData.getSetup().getTargetOvers().trim().isEmpty()) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Side"+WhichSide+"$Select_FooterType$Score"
							+ "$In_Out$Overs$txt_Overs_2*GEOM*TEXT SET " + "(" + matchAllData.getSetup().getTargetOvers() + ")" + "\0", print_writers);
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
				
			case "Shift_F11": case "Control_F11": case "Shift_F10": case "Alt_F9": case "Alt_F10": case "Alt_F11": //MATCH SUMMARY
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Side" + WhichSide + "$Select_FooterType"
						+ "*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				
				switch (whatToProcess.split(",")[0]) {
				case "Shift_F10": case "Alt_F11":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Top_Align$Side" + WhichSide + "$Select_FooterType"
							+ "$Info_Text$Data$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.
							generateMatchSummaryStatus(WhichInning, matchAllData, CricketUtil.FULL, "", config.getBroadcaster()).toUpperCase() + "\0", print_writers);
					break;
				case "Shift_F11":
					for(VariousText vt : VariousText) {
						if(vt.getVariousType().equalsIgnoreCase("MATCHSUMMARYFOOTER") && vt.getUseThis().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Top_Align$Side" + WhichSide + "$Select_FooterType"
									+ "$Info_Text$Data$txt_Info_1*GEOM*TEXT SET " + vt.getVariousText() + "\0", print_writers);
						}else if(vt.getVariousType().equalsIgnoreCase("MATCHSUMMARYFOOTER") && vt.getUseThis().toUpperCase().equalsIgnoreCase(CricketUtil.NO)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Top_Align$Side" + WhichSide + "$Select_FooterType"
								+ "$Info_Text$Data$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.
								generateMatchSummaryStatus(WhichInning, matchAllData, CricketUtil.FULL, "", config.getBroadcaster()).toUpperCase() + "\0", print_writers);
						}
					}
					break;
				case "Control_F11":
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Top_Align$Side" + WhichSide + "$Select_FooterType"
							+ "$Info_Text$Data$txt_Info_1*GEOM*TEXT SET " + CricketFunctions.generateMatchSummaryStatus(2, previous_match.get(previous_match.size()-1), 
							CricketUtil.FULL, "", config.getBroadcaster()).toUpperCase() + "\0", print_writers);
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
				case "Alt_F10":
					if(WhichStyle.equalsIgnoreCase("Age")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Top_Align$Side" + WhichSide + "$Select_FooterType"
								+ "$Info_Text$Data$txt_Info_1*GEOM*TEXT SET " + "U19 MEN'S CWC 2024" + "\0", print_writers);
					}else {
						switch (WhichType.toUpperCase()) {
						case "RUNS": case "AVERAGE": case "STRIKERATE":
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Top_Align$Side" + WhichSide + "$Select_FooterType"
									+ "$Info_Text$Data$txt_Info_1*GEOM*TEXT SET " + "ICC U19 MEN'S CWC 2024" + " - " + "BATTING" +"\0", print_writers);
							break;

						case "WICKETS": case "ECONOMY": 
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Top_Align$Side" + WhichSide + "$Select_FooterType"
									+ "$Info_Text$Data$txt_Info_1*GEOM*TEXT SET " + "ICC U19 MEN'S CWC 2024" + " - " + "BOWLING" +"\0", print_writers);
							break;
						}
						
					}
					break;
				}
				break;

			case "Control_d": case "Control_e": case "z": case "x": case "c": case "v": case "Shift_P": case "Shift_Q": case "Control_z": case "Control_x":
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
				
			case "p": case "Control_p":
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Side" + WhichSide + "$Select_FooterType"
						+ "*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				
				if(whatToProcess.equalsIgnoreCase("p")) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Top_Align$Side"+WhichSide+"$Select_FooterType$Info_Text$Data"
							+ "$txt_Info_1*GEOM*TEXT SET " + "TOP 3 TEAMS PROGRESS TO THE SUPER SIX STAGE" + "\0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$Footer$Top_Align$Side"+WhichSide+"$Select_FooterType$Info_Text$Data"
							+ "$txt_Info_1*GEOM*TEXT SET " + "TOP 2 TEAMS PROGRESS TO THE SEMI-FINALS" + "\0", print_writers);
				}
				
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
	
	public void ScoreCardBody(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) {
		
		String how_out_txt = "";
		
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ISPL:
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
					+ "$Select_Graphics*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
			
			if(WhichScoreCard.equalsIgnoreCase("SPLIT")) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
						+ "$Select_Graphics$BattingCard$Select_Style*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
				containerName_2 = "$SplitBatting";
			}else if(WhichScoreCard.equalsIgnoreCase("NORMAL")) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
						+ "$Select_Graphics$BattingCard$Select_Style*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$BattingCard$"
						+ "NormalBatting$BattingDataAll*FUNCTION*Grid*num_row SET " + inning.getBattingCard().size() + "\0", print_writers);
				containerName_2 = "$NormalBatting";
			}
			
			Collections.sort(inning.getBattingCard());
			for(int iRow = 1; iRow <= inning.getBattingCard().size(); iRow++) {
				switch (inning.getBattingCard().get(iRow-1).getStatus().toUpperCase()) {
				case CricketUtil.STILL_TO_BAT:
					if(inning.getBattingCard().get(iRow-1).getHowOut() == null || inning.getBattingCard().get(iRow-1).getHowOut().isEmpty()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$Still_To_Bat$BatterGrp$img_Text2$txt_BatterName*GEOM*TEXT SET "
							+ inning.getBattingCard().get(iRow-1).getPlayer().getTicker_name() + "\0", print_writers);
						
						if(CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
								inning.getBattingCard().get(iRow-1).getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$Still_To_Bat$BatterGrp$img_Text2$"
									+ "Selec_tImpact*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						}
						else if(CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
								inning.getBattingCard().get(iRow-1).getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$Still_To_Bat$BatterGrp$img_Text2$"
									+ "Selec_tImpact*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$BattingCard$" + containerName_2 + "$" + iRow + "$Select_Row_Type$Still_To_Bat$BatterGrp$img_Text2$"
								+ "Selec_tImpact*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						}
						
					} else {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$Out$BatterGrp$img_Text2$txt_BatterName*GEOM*TEXT SET "
							+ inning.getBattingCard().get(iRow-1).getPlayer().getTicker_name() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$Out$HowOutGrp$img_Text2$txt_OutType*GEOM*TEXT SET "
							+ inning.getBattingCard().get(iRow-1).getHowOut().replace("_", " ").toLowerCase() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$Out$HowOutGrp$img_Text2$txt_FielderName*GEOM*TEXT SET "
							+ "" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$Out$HowOutGrp$img_Text2$txt_Bold*GEOM*TEXT SET "
							+ "" + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$Out$HowOutGrp$img_Text2$txt_BowlerName*GEOM*TEXT SET "
							+ "" + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$Out$ScoreGrp$img_Text1$txt_Runs*GEOM*TEXT SET "
							+ inning.getBattingCard().get(iRow-1).getRuns() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$Out$ScoreGrp$img_Text1$txt_Balls*GEOM*TEXT SET "
							+ String.valueOf(inning.getBattingCard().get(iRow-1).getBalls()) + "\0", print_writers);
						
						if(CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
								inning.getBattingCard().get(iRow-1).getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$Still_To_Bat$BatterGrp$img_Text2$"
									+ "Selec_tImpact*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						}
						else if(CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
								inning.getBattingCard().get(iRow-1).getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
							
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$Still_To_Bat$BatterGrp$img_Text2$"
									+ "Selec_tImpact*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$BattingCard$" + containerName_2 + "$" + iRow + "$Select_Row_Type$Still_To_Bat$BatterGrp$img_Text2$"
								+ "Selec_tImpact*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						}
					}
					break;
					
				default:
					
					switch (inning.getBattingCard().get(iRow-1).getStatus().toUpperCase()) {
					case CricketUtil.OUT:
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						containerName = "Out";
						break;
					case CricketUtil.NOT_OUT:
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
						containerName = "NotOut";
						break;
					}
					
					if(CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
							inning.getBattingCard().get(iRow-1).getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$BatterGrp$img_Text2$"
								+ "Selec_tImpact*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}
					else if(CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
							inning.getBattingCard().get(iRow-1).getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$BattingCard"+ containerName_2 +"$" + iRow + "$Select_Row_Type$" + containerName + "$BatterGrp$img_Text2$"
								+ "Selec_tImpact*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$BattingCard"+ containerName_2 +"$" + iRow + "$Select_Row_Type$" + containerName + "$BatterGrp$img_Text2$"
							+ "Selec_tImpact*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					}
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
						+ "$BattingCard"+ containerName_2 +"$" + iRow + "$Select_Row_Type$" + containerName + "$BatterGrp$img_Text2$"
						+ "txt_BatterName*GEOM*TEXT SET " + inning.getBattingCard().get(iRow-1).getPlayer().getTicker_name() + "\0", print_writers);
					
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
						+ "$BattingCard"+ containerName_2 +"$" + iRow + "$Select_Row_Type$" + containerName + "$ScoreGrp$img_Text1$txt_Runs*GEOM*TEXT SET "
						+ inning.getBattingCard().get(iRow-1).getRuns() + "\0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
						+ "$BattingCard"+ containerName_2 +"$" + iRow + "$Select_Row_Type$" + containerName + "$ScoreGrp$img_Text1$txt_Balls*GEOM*TEXT SET "
						+ String.valueOf(inning.getBattingCard().get(iRow-1).getBalls()) + "\0", print_writers);
					
					how_out_txt = CricketFunctions.processHowOutText("FOUR-PART-HOW-OUT", inning.getBattingCard().get(iRow-1));
					
					if(how_out_txt != null && how_out_txt.split("|").length >= 4) {
						switch (inning.getBattingCard().get(iRow-1).getStatus().toUpperCase()) {
						case CricketUtil.OUT:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$HowOutGrp$img_Text2$"
								+ "txt_OutType*GEOM*TEXT SET " + how_out_txt.split("\\|")[0].trim() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$HowOutGrp$img_Text2$"
								+ "txt_Bold*GEOM*TEXT SET " + how_out_txt.split("\\|")[2].trim() + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$HowOutGrp$img_Text2$"
								+ "txt_BowlerName*GEOM*TEXT SET " + how_out_txt.split("\\|")[3].trim() + "\0", print_writers);
							break;
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$HowOutGrp$img_Text2$"
								+ "txt_FielderName*GEOM*TEXT SET " + how_out_txt.split("\\|")[1].trim() + "\0", print_writers);
					}else {
						switch (inning.getBattingCard().get(iRow-1).getStatus().toUpperCase()) {
						case CricketUtil.OUT:
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$HowOutGrp$txt_OutType*GEOM*TEXT SET " 
								+ "" + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$HowOutGrp$txt_Bold*GEOM*TEXT SET " 
								+ "" + "\0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
								+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$HowOutGrp$txt_BowlerName*GEOM*TEXT SET " 
								+ "" + "\0", print_writers);
							break;
						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
							+ "$BattingCard" + containerName_2 + "$" + iRow + "$Select_Row_Type$" + containerName + "$HowOutGrp$txt_FielderName*GEOM*TEXT SET " 
							+ "NOT OUT" + "\0", print_writers);
					}
					break;
				}
			}
			break;
		case Constants.ICC_U19_2023:
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
				+ "$Select_GraphicsType*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Batting_Card$Rows"
					+ "*FUNCTION*Grid*num_row SET "+inning.getBattingCard().size()+"\0", print_writers);
			Collections.sort(inning.getBattingCard());
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
	}
	public void BowlingCardBody(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) {
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
						+ (j+1) + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$"
						+ (j+1) + "$Select_Row_Type$Players$Data*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$"
						+ (j+1) + "$Select_Row_Type$Players$Base$Move_for_Batting_Card_Partnership*ACTIVE SET 0 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$10"
						+ "$Select_Row_Type$FOW$Data$fig_" + j + "*GEOM*TEXT SET " + "" + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$11"
						+ "$Select_Row_Type$Score$Data$fig_" + j + "*GEOM*TEXT SET " + "" + "\0", print_writers);
				
				
			}
			Collections.sort(inning.getBowlingCard());
			if(inning.getBowlingCard() != null && inning.getBowlingCard().size() > 0) {
				for(int iRow = 1; iRow <= inning.getBowlingCard().size(); iRow++) {
					if(inning.getBowlingCard().get(iRow-1).getRuns() > 0 || 
							((inning.getBowlingCard().get(iRow-1).getOvers()*6)
							+inning.getBowlingCard().get(iRow-1).getBalls()) > 0) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$"
								+ (iRow+1) + "$Select_Row_Type$Players$Data*ACTIVE SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$"
								+ (iRow+1) + "$Select_Row_Type$Players$Base$Move_for_Batting_Card_Partnership*ACTIVE SET 1 \0", print_writers);
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
									+ (iRow+1) + "$Select_Row_Type$Players$Data$fig_Economy*GEOM*TEXT SET " + "-" + "\0", print_writers);
						}
					}
				}
			}
			
			if(inning.getBowlingCard().size() <= 8) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$10"
						+ "$Select_Row_Type*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$11"
						+ "$Select_Row_Type*FUNCTION*Omo*vis_con SET 4 \0", print_writers);
				
				if(inning.getFallsOfWickets() != null) {
					for(FallOfWicket fow : inning.getFallsOfWickets()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$"
							+ "10$Select_Row_Type$FOW$Data$fig_" + fow.getFowNumber() + "*GEOM*TEXT SET " + fow.getFowNumber() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$Rows$"
							+ "11$Select_Row_Type$Score$Data$fig_" + fow.getFowNumber() + "*GEOM*TEXT SET " + fow.getFowRuns() + "\0", print_writers);
					}
				}
			}
			break;
		case Constants.ISPL:
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
					+ "$Select_Graphics*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
			
			for(int j=1;j<=11;j++) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$BowlingDataAll$"
						+ (j) + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
			}
			
			Collections.sort(inning.getBowlingCard());
			if(inning.getBowlingCard() != null && inning.getBowlingCard().size() > 0) {
				for(int iRow = 0; iRow <= inning.getBowlingCard().size()-1; iRow++) {
					if(inning.getBowlingCard().get(iRow).getRuns() > 0 || ((inning.getBowlingCard().get(iRow).getOvers()*6)
							+ inning.getBowlingCard().get(iRow).getBalls()) > 0) {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$BowlingDataAll"
								+ "*FUNCTION*Grid*num_row SET " + (iRow+1) + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$BowlingDataAll$"
								+ (iRow+1) + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						
						if(CricketFunctions.checkImpactPlayer(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
								inning.getBowlingCard().get(iRow).getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Bowling_Card$BowlingDataAll$" + (iRow+1) + "$Normal$BatterGrp$Selec_tImpact"
									+ "*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						}
						else if(CricketFunctions.checkImpactPlayerBowler(matchAllData.getEventFile().getEvents(), inning.getInningNumber(), 
								inning.getBowlingCard().get(iRow).getPlayerId()).equalsIgnoreCase(CricketUtil.YES)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Bowling_Card$BowlingDataAll$" + (iRow+1) + "$Normal$BatterGrp$Selec_tImpact"
									+ "*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Bowling_Card$BowlingDataAll$" + (iRow+1) + "$Normal$BatterGrp$Selec_tImpact"
									+ "*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						}
						
						if(inning.getBowlingCard().get(iRow).getBallTypeOverNo().contains("tape")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Bowling_Card$BowlingDataAll$" + (iRow+1) + "$Normal$BowlerDataGrp$Select_Tapeball"
									+ "*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide 
									+ "$Bowling_Card$BowlingDataAll$" + (iRow+1) + "$Normal$BowlerDataGrp$Select_Tapeball"
									+ "*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						}
//						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$"
//							+ (iRow+1) + "$Select_Row_Type*FUNCTION*Omo*vis_con SET 2 \0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$"
							+ (iRow+1) + "$Select_Row_Type$Normal$BatterGrp$txt_Name*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow).getPlayer().getTicker_name() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$"
								+ (iRow+1) + "$Select_Row_Type$Normal$ScoreGrp$txt_Fig*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow).getWickets() + "-" + 
								inning.getBowlingCard().get(iRow).getRuns() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$"
								+ (iRow+1) + "$Select_Row_Type$Normal$ScoreGrp$txt_Overs*GEOM*TEXT SET " + CricketFunctions.
								OverBalls(inning.getBowlingCard().get(iRow).getOvers(), inning.getBowlingCard().get(iRow).getBalls()) + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$"
								+ (iRow+1) + "$Select_Row_Type$Normal$BowlerDataGrp$txt_DotValue*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow).getDots() + "\0", print_writers);
						
						if(inning.getBowlingCard().get(iRow).getEconomyRate() != null) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$"
									+ (iRow+1) + "$Select_Row_Type$Normal$BowlerDataGrp$txt_EconomyValue*GEOM*TEXT SET " + inning.getBowlingCard().get(iRow).getEconomyRate() + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$"
									+ (iRow+1) + "$Select_Row_Type$Normal$BowlerDataGrp$txt_EconomyValue*GEOM*TEXT SET " + "-" + "\0", print_writers);
						}
					}
				}
			}
			
			if(inning.getBowlingCard().size() <= 8) {
				if(inning.getFallsOfWickets() == null || inning.getFallsOfWickets().size() <= 0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$"
							+ "FowGrp$1*ACTIVE SET 0 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$"
							+ "FowGrp$2*ACTIVE SET 0 \0", print_writers);
				}else {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$"
							+ "FowGrp$1*ACTIVE SET 1 \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$"
							+ "FowGrp$2*ACTIVE SET 1 \0", print_writers);
					for(FallOfWicket fow : inning.getFallsOfWickets()) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$FowGrp$2$FOW$WicketsGrp$img_Text2"
								+ "*FUNCTION*Grid*num_col SET " + fow.getFowNumber() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$FowGrp$2$FOW$RunsGrp$img_Base1"
								+ "*FUNCTION*Grid*num_col SET " + fow.getFowNumber() + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Bowling_Card$FowGrp$2$FOW$RunsGrp"
								+ "$fig_" + fow.getFowNumber() + "*GEOM*TEXT SET " + fow.getFowRuns() + "\0", print_writers);
					}
				}
			}
			
			break;	
		}
	}
	public void DoubleTeamsAndPlayingXiBody(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) {
		
		switch(whatToProcess) {
		case "Control_F7":
			switch (config.getBroadcaster().toUpperCase()) {
			case Constants.ICC_U19_2023: case Constants.ISPL:
				switch (config.getBroadcaster().toUpperCase()) {
				case Constants.ICC_U19_2023: 
					omo = 9;
					containerName = "Team_";
					containerName_2 = "Tittle$txt_Tittle";
					containerName_3 = "$Select_GraphicsType";
					containerName_4 = "$Tittle$txt_Tittle";
					break;
				case Constants.ISPL:
					omo = 4;
					containerName = "Team";
					containerName_3 = "$Select_Graphics";
					containerName_4 = "$Title$txt_Team";
					break;
				}

				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + containerName_3
						+ "*FUNCTION*Omo*vis_con SET " + omo + "\0", print_writers);
				
				rowId = 0; rowId1 = 0;
				for(int i = 1; i <= 2 ; i++) {
					if(i == 1) {
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
								containerName + i + containerName_4 + "*GEOM*TEXT SET " + matchAllData.getSetup().getHomeTeam().getTeamName1() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
								containerName + i + "$Title$img_TeamLogo*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + 
								matchAllData.getSetup().getHomeTeam().getTeamName4() + "\0", print_writers);
						for(Player hs : matchAllData.getSetup().getHomeSquad()) {
							rowId = rowId + 1;
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
									containerName + i + "$" + rowId + "$txt_Name*GEOM*TEXT SET " + hs.getFull_name() + "\0", print_writers);
							
							switch (config.getBroadcaster().toUpperCase()) {
							case Constants.ISPL:
								if(hs.getZone().equalsIgnoreCase("NORTH")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
											containerName + i + "$" + rowId + "$txt_Zone*GEOM*TEXT SET " + "(NZ)" + "\0", print_writers);
								}else if(hs.getZone().equalsIgnoreCase("EAST")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
											containerName + i + "$" + rowId + "$txt_Zone*GEOM*TEXT SET " + "(EZ)" + "\0", print_writers);
								}else if(hs.getZone().equalsIgnoreCase("SOUTH")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
											containerName + i + "$" + rowId + "$txt_Zone*GEOM*TEXT SET " + "(SZ)" + "\0", print_writers);
								}else if(hs.getZone().equalsIgnoreCase("WEST")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
											containerName + i + "$" + rowId + "$txt_Zone*GEOM*TEXT SET " + "(WZ)" + "\0", print_writers);
								}else if(hs.getZone().equalsIgnoreCase("CENTRAL")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
											containerName + i + "$" + rowId + "$txt_Zone*GEOM*TEXT SET " + "(CZ)" + "\0", print_writers);
								}else {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
											containerName + i + "$" + rowId + "$txt_Zone*GEOM*TEXT SET (" + hs.getZone() + ")\0", print_writers);
								}
								
								if(hs.getRole().equalsIgnoreCase("BATSMAN") || hs.getRole().equalsIgnoreCase("BAT/KEEPER")) {
									if(hs.getBattingStyle().equalsIgnoreCase("RHB")) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
												containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Batsman" + "\0", print_writers);
									}else if(hs.getBattingStyle().equalsIgnoreCase("LHB")) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
												containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Batsman_Lefthand" + "\0", print_writers);
									}
								}else if(hs.getRole().equalsIgnoreCase("BOWLER")) {
									if(hs.getBowlingStyle() == null) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
												containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowler" + "\0", print_writers);
									}else {
										switch(hs.getBowlingStyle()) {
										case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
													containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowler" + "\0", print_writers);
											break;
										case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
													containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "SpinBowlerIcon" + "\0", print_writers);
											break;
										}
									}
								}else if(hs.getRole().equalsIgnoreCase("ALL-ROUNDER")) {
									if(hs.getBowlingStyle() == null) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
												containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowlerAllrounder" + "\0", print_writers);
									}else {
										switch(hs.getBowlingStyle()) {
										case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
													containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowlerAllrounder" + "\0", print_writers);
											break;
										case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
													containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "SpinBowlerAllrounder" + "\0", print_writers);
											break;
										}
									}
								}
								
								if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
											containerName +  i + "$" + rowId + "$SelectCaptain*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
								}else if(hs.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
											containerName +  i + "$" + rowId + "$SelectCaptain*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
											containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Keeper" + "\0", print_writers);
								}else if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
											containerName +  i + "$" + rowId + "$SelectCaptain*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
											containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Keeper" + "\0", print_writers);
								}else {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
											containerName +  i + "$" + rowId + "$SelectCaptain*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
								}
								
								break;
							case Constants.ICC_U19_2023: 
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
								break;
							}
						}
						
						if(config.getBroadcaster().toUpperCase().equalsIgnoreCase(Constants.ISPL)) {
							for(Player hsub : matchAllData.getSetup().getHomeSubstitutes()) {
								rowId1 = rowId1 + 1;
								if(rowId1 <= 4) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$"
										+ "Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$txt_Name*GEOM*TEXT SET " + hsub.getFull_name() + "\0", print_writers);
									
									if(hsub.getZone().equalsIgnoreCase("NORTH")) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
												"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$txt_Zone*GEOM*TEXT SET " + "(NZ)" + "\0", print_writers);
									}else if(hsub.getZone().equalsIgnoreCase("EAST")) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
												"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$txt_Zone*GEOM*TEXT SET " + "(EZ)" + "\0", print_writers);
									}else if(hsub.getZone().equalsIgnoreCase("SOUTH")) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
												"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$txt_Zone*GEOM*TEXT SET " + "(SZ)" + "\0", print_writers);
									}else if(hsub.getZone().equalsIgnoreCase("WEST")) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
												"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$txt_Zone*GEOM*TEXT SET " + "(WZ)" + "\0", print_writers);
									}else if(hsub.getZone().equalsIgnoreCase("CENTRAL")) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
												"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$txt_Zone*GEOM*TEXT SET " + "(CZ)" + "\0", print_writers);
									}else {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
												"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$txt_Zone*GEOM*TEXT SET (" + hsub.getZone() + ")\0", print_writers);
									}
									
									if(hsub.getRole().equalsIgnoreCase("BATSMAN") || hsub.getRole().equalsIgnoreCase("BAT/KEEPER")) {
										if(hsub.getBattingStyle().equalsIgnoreCase("RHB")) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
													"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Batsman" + "\0", print_writers);
										}else if(hsub.getBattingStyle().equalsIgnoreCase("LHB")) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
													"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Batsman_Lefthand" + "\0", print_writers);
										}
									}else if(hsub.getRole().equalsIgnoreCase("BOWLER")) {
										if(hsub.getBowlingStyle() == null) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
													"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowler" + "\0", print_writers);
										}else {
											switch(hsub.getBowlingStyle()) {
											case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
												CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
														"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowler" + "\0", print_writers);
												break;
											case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
												CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
														"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "SpinBowlerIcon" + "\0", print_writers);
												break;
											}
										}
									}else if(hsub.getRole().equalsIgnoreCase("ALL-ROUNDER")) {
										if(hsub.getBowlingStyle() == null) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
													"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowlerAllrounder" + "\0", print_writers);
										}else {
											switch(hsub.getBowlingStyle()) {
											case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
												CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
														"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowlerAllrounder" + "\0", print_writers);
												break;
											case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
												CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
														"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "SpinBowlerAllrounder" + "\0", print_writers);
												break;
											}
										}
									}
									
									if(hsub.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
												"Teams_SubDataAll$Taem" +  i + "$" + rowId1 + "$SelectCaptain*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
									}else if(hsub.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
												"Teams_SubDataAll$Taem" +  i + "$" + rowId1 + "$SelectCaptain*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
												"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Keeper" + "\0", print_writers);
									}else if(hsub.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
												"Teams_SubDataAll$Taem" +  i + "$" + rowId1 + "$SelectCaptain*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
												"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Keeper" + "\0", print_writers);
									}else {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
												"Teams_SubDataAll$Taem" +  i + "$" + rowId1 + "$SelectCaptain*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
									}
								}
							}
						}
					} else {
						rowId = 0; rowId1 = 0;
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
								containerName + i + containerName_4 + "*GEOM*TEXT SET " + matchAllData.getSetup().getAwayTeam().getTeamName1() + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
								containerName + i + "$Title$img_TeamLogo*TEXTURE*IMAGE SET " + Constants.ISPL_LOGOS_PATH + 
								matchAllData.getSetup().getAwayTeam().getTeamName4() + "\0", print_writers);
						
						for(Player as : matchAllData.getSetup().getAwaySquad()) {
							rowId = rowId + 1;
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
									containerName + i + "$" + rowId + "$txt_Name*GEOM*TEXT SET " + as.getFull_name() + "\0", print_writers);
							
							switch (config.getBroadcaster().toUpperCase()) {
							case Constants.ISPL:
								if(as.getZone().equalsIgnoreCase("NORTH")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
											containerName + i + "$" + rowId + "$txt_Zone*GEOM*TEXT SET " + "(NZ)" + "\0", print_writers);
								}else if(as.getZone().equalsIgnoreCase("EAST")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
											containerName + i + "$" + rowId + "$txt_Zone*GEOM*TEXT SET " + "(EZ)" + "\0", print_writers);
								}else if(as.getZone().equalsIgnoreCase("SOUTH")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
											containerName + i + "$" + rowId + "$txt_Zone*GEOM*TEXT SET " + "(SZ)" + "\0", print_writers);
								}else if(as.getZone().equalsIgnoreCase("WEST")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
											containerName + i + "$" + rowId + "$txt_Zone*GEOM*TEXT SET " + "(WZ)" + "\0", print_writers);
								}else if(as.getZone().equalsIgnoreCase("CENTRAL")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
											containerName + i + "$" + rowId + "$txt_Zone*GEOM*TEXT SET " + "(CZ)" + "\0", print_writers);
								}else {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
											containerName + i + "$" + rowId + "$txt_Zone*GEOM*TEXT SET (" + as.getZone() + ")\0", print_writers);
								}
								
								if(as.getRole().equalsIgnoreCase("BATSMAN") || as.getRole().equalsIgnoreCase("BAT/KEEPER")) {
									if(as.getBattingStyle().equalsIgnoreCase("RHB")) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
												containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Batsman" + "\0", print_writers);
									}else if(as.getBattingStyle().equalsIgnoreCase("LHB")) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
												containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Batsman_Lefthand" + "\0", print_writers);
									}
								}else if(as.getRole().equalsIgnoreCase("BOWLER")) {
									if(as.getBowlingStyle() == null) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
												containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowler" + "\0", print_writers);
									}else {
										switch(as.getBowlingStyle()) {
										case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
													containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowler" + "\0", print_writers);
											break;
										case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
													containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "SpinBowlerIcon" + "\0", print_writers);
											break;
										}
									}
								}else if(as.getRole().equalsIgnoreCase("ALL-ROUNDER")) {
									if(as.getBowlingStyle() == null) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
												containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowlerAllrounder" + "\0", print_writers);
									}else {
										switch(as.getBowlingStyle()) {
										case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
													containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowlerAllrounder" + "\0", print_writers);
											break;
										case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
													containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "SpinBowlerAllrounder" + "\0", print_writers);
											break;
										}
									}
								}
								
								if(as.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
											containerName +  i + "$" + rowId + "$SelectCaptain*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
								}else if(as.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
											containerName +  i + "$" + rowId + "$SelectCaptain*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
											containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Keeper" + "\0", print_writers);
								}else if(as.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
											containerName +  i + "$" + rowId + "$SelectCaptain*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
											containerName + i + "$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Keeper" + "\0", print_writers);
								}else {
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
											containerName +  i + "$" + rowId + "$SelectCaptain*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
								}
								break;
							case Constants.ICC_U19_2023: 
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
								break;
							}
							if(config.getBroadcaster().toUpperCase().equalsIgnoreCase(Constants.ISPL)) {
								for(Player asub : matchAllData.getSetup().getAwaySubstitutes()) {
									rowId1 = rowId1 + 1;
									if(rowId1 <= 4) {
										CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$"
											+ "Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$txt_Name*GEOM*TEXT SET " + asub.getFull_name() + "\0", print_writers);
										
										if(asub.getZone().equalsIgnoreCase("NORTH")) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
													"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$txt_Zone*GEOM*TEXT SET " + "(NZ)" + "\0", print_writers);
										}else if(asub.getZone().equalsIgnoreCase("EAST")) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
													"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$txt_Zone*GEOM*TEXT SET " + "(EZ)" + "\0", print_writers);
										}else if(asub.getZone().equalsIgnoreCase("SOUTH")) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
													"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$txt_Zone*GEOM*TEXT SET " + "(SZ)" + "\0", print_writers);
										}else if(asub.getZone().equalsIgnoreCase("WEST")) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
													"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$txt_Zone*GEOM*TEXT SET " + "(WZ)" + "\0", print_writers);
										}else if(asub.getZone().equalsIgnoreCase("CENTRAL")) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
													"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$txt_Zone*GEOM*TEXT SET " + "(CZ)" + "\0", print_writers);
										}else {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" + 
													"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$txt_Zone*GEOM*TEXT SET (" + asub.getZone() + ")\0", print_writers);
										}
										
										if(asub.getRole().equalsIgnoreCase("BATSMAN") || asub.getRole().equalsIgnoreCase("BAT/KEEPER")) {
											if(asub.getBattingStyle().equalsIgnoreCase("RHB")) {
												CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
														"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Batsman" + "\0", print_writers);
											}else if(asub.getBattingStyle().equalsIgnoreCase("LHB")) {
												CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
														"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Batsman_Lefthand" + "\0", print_writers);
											}
										}else if(asub.getRole().equalsIgnoreCase("BOWLER")) {
											if(asub.getBowlingStyle() == null) {
												CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
														"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowler" + "\0", print_writers);
											}else {
												switch(asub.getBowlingStyle()) {
												case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
													CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
															"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowler" + "\0", print_writers);
													break;
												case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
													CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
															"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "SpinBowlerIcon" + "\0", print_writers);
													break;
												}
											}
										}else if(asub.getRole().equalsIgnoreCase("ALL-ROUNDER")) {
											if(asub.getBowlingStyle() == null) {
												CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
														"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowlerAllrounder" + "\0", print_writers);
											}else {
												switch(asub.getBowlingStyle()) {
												case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
													CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
															"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowlerAllrounder" + "\0", print_writers);
													break;
												case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
													CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
															"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "SpinBowlerAllrounder" + "\0", print_writers);
													break;
												}
											}
										}
										
										if(asub.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
													"Teams_SubDataAll$Taem" +  i + "$" + rowId1 + "$SelectCaptain*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
										}else if(asub.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
													"Teams_SubDataAll$Taem" +  i + "$" + rowId1 + "$SelectCaptain*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
													"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Keeper" + "\0", print_writers);
										}else if(asub.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
													"Teams_SubDataAll$Taem" +  i + "$" + rowId1 + "$SelectCaptain*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
													"Teams_SubDataAll$Taem" + i + "$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Keeper" + "\0", print_writers);
										}else {
											CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Teams$" +
													"Teams_SubDataAll$Taem" +  i + "$" + rowId1 + "$SelectCaptain*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
										}
									}
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
			case Constants.ISPL:
				rowId = 0; rowId1 =0;
				
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide
						+ "$Select_Graphics*FUNCTION*Omo*vis_con SET 3 \0", print_writers);
				
				for(Player plyr : PlayingXI) {
					rowId = rowId + 1;
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" + 
							"Team_SingleDataAll$" + rowId + "$txt_Name*GEOM*TEXT SET " + plyr.getFull_name() + "\0", print_writers);
					
					if(plyr.getZone().equalsIgnoreCase("NORTH")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" + 
								"Team_SingleDataAll$" + rowId + "$txt_Zone*GEOM*TEXT SET " + "(NZ)" + "\0", print_writers);
					}else if(plyr.getZone().equalsIgnoreCase("EAST")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" + 
								"Team_SingleDataAll$" + rowId + "$txt_Zone*GEOM*TEXT SET " + "(EZ)" + "\0", print_writers);
					}else if(plyr.getZone().equalsIgnoreCase("SOUTH")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" + 
								"Team_SingleDataAll$" + rowId + "$txt_Zone*GEOM*TEXT SET " + "(SZ)" + "\0", print_writers);
					}else if(plyr.getZone().equalsIgnoreCase("WEST")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" + 
								"Team_SingleDataAll$" + rowId + "$txt_Zone*GEOM*TEXT SET " + "(WZ)" + "\0", print_writers);
					}else if(plyr.getZone().equalsIgnoreCase("CENTRAL")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" + 
								"Team_SingleDataAll$" + rowId + "$txt_Zone*GEOM*TEXT SET " + "(CZ)" + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" + 
								"Team_SingleDataAll$" + rowId + "$txt_Zone*GEOM*TEXT SET (" + plyr.getZone() + ")\0", print_writers);
					}
					
					if(plyr.getRole().equalsIgnoreCase("BATSMAN") || plyr.getRole().equalsIgnoreCase("BAT/KEEPER")) {
						if(plyr.getBattingStyle().equalsIgnoreCase("RHB")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
									"Team_SingleDataAll$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Batsman" + "\0", print_writers);
						}else if(plyr.getBattingStyle().equalsIgnoreCase("LHB")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
									"Team_SingleDataAll$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Batsman_Lefthand" + "\0", print_writers);
						}
					}else if(plyr.getRole().equalsIgnoreCase("BOWLER")) {
						if(plyr.getBowlingStyle() == null) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
									"Team_SingleDataAll$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowler" + "\0", print_writers);
						}else {
							switch(plyr.getBowlingStyle()) {
							case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
										"Team_SingleDataAll$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowler" + "\0", print_writers);
								break;
							case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
										"Team_SingleDataAll$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "SpinBowlerIcon" + "\0", print_writers);
								break;
							}
						}
					}else if(plyr.getRole().equalsIgnoreCase("ALL-ROUNDER")) {
						if(plyr.getBowlingStyle() == null) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
									"Team_SingleDataAll$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowlerAllrounder" + "\0", print_writers);
						}else {
							switch(plyr.getBowlingStyle()) {
							case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
										"Team_SingleDataAll$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowlerAllrounder" + "\0", print_writers);
								break;
							case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
										"Team_SingleDataAll$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "SpinBowlerAllrounder" + "\0", print_writers);
								break;
							}
						}
					}
					
					if(plyr.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
								"Team_SingleDataAll$" + rowId + "$SelectCaptain*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
					}else if(plyr.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
								"Team_SingleDataAll$" + rowId + "$SelectCaptain*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
								"Team_SingleDataAll$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Keeper" + "\0", print_writers);
					}else if(plyr.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
								"Team_SingleDataAll$" + rowId + "$SelectCaptain*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
								"Team_SingleDataAll$" + rowId + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Keeper" + "\0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
								"Team_SingleDataAll$" + rowId + "$SelectCaptain*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
					}
				}
				
				for(Player plyr : otherSquad) {
					rowId1 = rowId1 + 1;
					if(rowId1 <= 4) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" + 
								"Team_SingleSubDataAll$" + rowId1 + "$txt_Name*GEOM*TEXT SET " + plyr.getFull_name() + "\0", print_writers);
						
						if(plyr.getZone().equalsIgnoreCase("NORTH")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" + 
									"Team_SingleSubDataAll$" + rowId1 + "$txt_Zone*GEOM*TEXT SET " + "(NZ)" + "\0", print_writers);
						}else if(plyr.getZone().equalsIgnoreCase("EAST")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" + 
									"Team_SingleSubDataAll$" + rowId1 + "$txt_Zone*GEOM*TEXT SET " + "(EZ)" + "\0", print_writers);
						}else if(plyr.getZone().equalsIgnoreCase("SOUTH")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" + 
									"Team_SingleSubDataAll$" + rowId1 + "$txt_Zone*GEOM*TEXT SET " + "(SZ)" + "\0", print_writers);
						}else if(plyr.getZone().equalsIgnoreCase("WEST")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" + 
									"Team_SingleSubDataAll$" + rowId1 + "$txt_Zone*GEOM*TEXT SET " + "(WZ)" + "\0", print_writers);
						}else if(plyr.getZone().equalsIgnoreCase("CENTRAL")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" + 
									"Team_SingleSubDataAll$" + rowId1 + "$txt_Zone*GEOM*TEXT SET " + "(CZ)" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" + 
									"Team_SingleSubDataAll$" + rowId1 + "$txt_Zone*GEOM*TEXT SET (" + plyr.getZone() + ")\0", print_writers);
						}
						
						if(plyr.getRole().equalsIgnoreCase("BATSMAN") || plyr.getRole().equalsIgnoreCase("BAT/KEEPER")) {
							if(plyr.getBattingStyle().equalsIgnoreCase("RHB")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
										"Team_SingleSubDataAll$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Batsman" + "\0", print_writers);
							}else if(plyr.getBattingStyle().equalsIgnoreCase("LHB")) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
										"Team_SingleSubDataAll$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Batsman_Lefthand" + "\0", print_writers);
							}
						}else if(plyr.getRole().equalsIgnoreCase("BOWLER")) {
							if(plyr.getBowlingStyle() == null) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
										"Team_SingleSubDataAll$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowler" + "\0", print_writers);
							}else {
								switch(plyr.getBowlingStyle()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
											"Team_SingleSubDataAll$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowler" + "\0", print_writers);
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
											"Team_SingleSubDataAll$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "SpinBowlerIcon" + "\0", print_writers);
									break;
								}
							}
						}else if(plyr.getRole().equalsIgnoreCase("ALL-ROUNDER")) {
							if(plyr.getBowlingStyle() == null) {
								CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
										"Team_SingleSubDataAll$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowlerAllrounder" + "\0", print_writers);
							}else {
								switch(plyr.getBowlingStyle()) {
								case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
											"Team_SingleSubDataAll$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "FastBowlerAllrounder" + "\0", print_writers);
									break;
								case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
									CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
											"Team_SingleSubDataAll$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "SpinBowlerAllrounder" + "\0", print_writers);
									break;
								}
							}
						}
						
						if(plyr.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
									"Team_SingleSubDataAll$" + rowId1 + "$SelectCaptain*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
						}else if(plyr.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
									"Team_SingleSubDataAll$" + rowId1 + "$SelectCaptain*FUNCTION*Omo*vis_con SET 1 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
									"Team_SingleSubDataAll$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Keeper" + "\0", print_writers);
						}else if(plyr.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)) {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
									"Team_SingleSubDataAll$" + rowId1 + "$SelectCaptain*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
									"Team_SingleSubDataAll$" + rowId1 + "$img_Icon*TEXTURE*IMAGE SET " + Constants.ICONS_PATH + "Keeper" + "\0", print_writers);
						}else {
							CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$Team_Single$" +
									"Team_SingleSubDataAll$" + rowId1 + "$SelectCaptain*FUNCTION*Omo*vis_con SET 0 \0", print_writers);
						}
					}
				}
				
				break;
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
					
//					if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
//						if(!new File("\\\\"+config.getSecondaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +  PlayingXI.get(i-1).getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//							return "Photo not found in " + config.getSecondaryIpAddress();
//						}
//						CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
//							+ "$Photo_" + rowId + "$img_Photo*TEXTURE*IMAGE SET " + "\\\\"+config.getSecondaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 
//							+ PlayingXI.get(i-1).getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
//					}
					
//					if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 +  PlayingXI.get(i-1).getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//						return "Photo not found in "+config.getPrimaryIpAddress();
//					}
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Full_Frame$AllGraphics$Side" + WhichSide + "$LineUp_Image" + containerName
							+ "$Photo_" + rowId + "$img_Photo*TEXTURE*IMAGE SET "+Constants.ICC_U19_2023_PHOTOS_PATH + Constants.CENTRE_512 
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
		}
		
		
	}
	public void TargetBody(int WhichSide, String whatToProcess, MatchAllData matchAllData, int WhichInning) {
		switch (config.getBroadcaster().toUpperCase()) {
		case Constants.ISPL:
			if(inning.getBattingTeamId() == matchAllData.getSetup().getHomeTeamId()) {
				for(Player hs : matchAllData.getSetup().getHomeSquad()) {
					if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)||hs.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerAllGrp$PlayerGrp1$"
							+ "img_Player*TEXTURE*IMAGE SET " + Constants.ISPL_PHOTO_PATH + matchAllData.getSetup().getHomeTeam().getTeamName4() 
							+ "\\\\" + hs.getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
					}
				}
				for(Player as : matchAllData.getSetup().getAwaySquad()) {
					if(as.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)||as.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerAllGrp$PlayerGrp2$"
								+ "img_Player*TEXTURE*IMAGE SET " + Constants.ISPL_PHOTO_PATH + matchAllData.getSetup().getAwayTeam().getTeamName4() 
								+ "\\\\" + as.getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
					}
				}
			}else if(inning.getBattingTeamId() == matchAllData.getSetup().getAwayTeamId()){
				for(Player hs : matchAllData.getSetup().getHomeSquad()) {
					if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)||hs.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerAllGrp$PlayerGrp2$"
								+ "img_Player*TEXTURE*IMAGE SET " + Constants.ISPL_PHOTO_PATH + matchAllData.getSetup().getHomeTeam().getTeamName4() 
								+ "\\\\" + hs.getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
					}
				}
				for(Player as : matchAllData.getSetup().getAwaySquad()) {
					if(as.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)||as.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerAllGrp$PlayerGrp1$"
								+ "img_Player*TEXTURE*IMAGE SET " + Constants.ISPL_PHOTO_PATH + matchAllData.getSetup().getAwayTeam().getTeamName4() 
								+ "\\\\" + as.getPhoto() + CricketUtil.PNG_EXTENSION + "\0", print_writers);
					}
				}
			}
			
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$Data$txt_TeamName*GEOM*TEXT SET " + 
					inning.getBatting_team().getTeamName1() + " \0", print_writers);
			
			if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER) && matchAllData.getSetup().getMaxOvers() == 1) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$Data$txt_Runs*GEOM*TEXT SET " + 
						CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$txt_Info1*GEOM*TEXT SET " + "FROM " + 
						(matchAllData.getSetup().getMaxOvers()*6) + " BALLS" + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$txt_Info2*GEOM*TEXT SET " + 
						"WINNER WILL BE DECIDED BY SUPER OVER" + " \0", print_writers);
			}else {
				if(matchAllData.getSetup().getTargetOvers() == "" || matchAllData.getSetup().getTargetOvers().trim().isEmpty() && 
						matchAllData.getSetup().getTargetRuns() == 0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$Data$txt_Runs*GEOM*TEXT SET " + 
						CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$txt_Info1*GEOM*TEXT SET " + "FROM " + 
						CricketFunctions.getTargetOvers(matchAllData) + " OVERS" + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$txt_Info2*GEOM*TEXT SET " + "AT " + CricketFunctions.
							generateRunRate(CricketFunctions.getTargetRuns(matchAllData), Integer.valueOf(CricketFunctions.getTargetOvers(matchAllData)), 0, 2, 
								matchAllData) + " RUNS PER OVER" + " \0", print_writers);

				}else {
					if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase(CricketUtil.VJD)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$Data$txt_Runs*GEOM*TEXT SET " + 
							CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$txt_Info1*GEOM*TEXT SET " + "FROM " + 
							CricketFunctions.getTargetOvers(matchAllData) + " OVERS" + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$txt_Info2*GEOM*TEXT SET " + "AT " + CricketFunctions.
								generateRunRate(CricketFunctions.getTargetRuns(matchAllData), Integer.valueOf(CricketFunctions.getTargetOvers(matchAllData)), 0, 2, 
									matchAllData) + " RUNS PER OVER" + "(" + CricketUtil.VJD + ")" + " \0", print_writers);
						
					}else if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase(CricketUtil.DLS)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$Data$txt_Runs*GEOM*TEXT SET " + 
							CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$txt_Info1*GEOM*TEXT SET " + "FROM " + 
							CricketFunctions.getTargetOvers(matchAllData) + " OVERS" + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$txt_Info2*GEOM*TEXT SET " + "AT " + CricketFunctions.
								generateRunRate(CricketFunctions.getTargetRuns(matchAllData), Integer.valueOf(CricketFunctions.getTargetOvers(matchAllData)), 0, 2, 
									matchAllData) + " RUNS PER OVER" + "(" + CricketUtil.DLS + ")" + " \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$Data$txt_Runs*GEOM*TEXT SET " + 
							CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$txt_Info1*GEOM*TEXT SET " + "FROM " + 
							CricketFunctions.getTargetOvers(matchAllData) + " OVERS" + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$txt_Info2*GEOM*TEXT SET " + "AT " + CricketFunctions.
								generateRunRate(CricketFunctions.getTargetRuns(matchAllData), Integer.valueOf(CricketFunctions.getTargetOvers(matchAllData)), 0, 2, 
									matchAllData) + " RUNS PER OVER" + " \0", print_writers);
					}
				}
			}
			
			break;
		case Constants.ICC_U19_2023:
			if(inning.getBatting_team().getTeamName4().equalsIgnoreCase("NEP")) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$FlagGrp1$img_Shadow*ACTIVE SET 0 \0", print_writers);
			}else if(inning.getBowling_team().getTeamName4().equalsIgnoreCase("NEP")) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$FlagGrp2$img_Shadow*ACTIVE SET 0 \0", print_writers);
			}else {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$FlagGrp1$img_Shadow*ACTIVE SET 1 \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$FlagGrp2$img_Shadow*ACTIVE SET 1 \0", print_writers);
			}
			
			if(inning.getBattingTeamId() == matchAllData.getSetup().getHomeTeamId()) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$FlagGrp1$img_Flag*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + matchAllData.getSetup().getHomeTeam().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$FlagGrp2$img_Flag*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH + matchAllData.getSetup().getAwayTeam().getTeamName4() + "\0", print_writers);
				for(Player hs : matchAllData.getSetup().getHomeSquad()) {
					if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)||hs.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
//						if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
//							if(!new File("\\\\"+config.getSecondaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 + hs.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//								return "Photo not found for home player in "+config.getSecondaryIpAddress();
//							}
//							CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerGrp1$img_Player*TEXTURE*IMAGE SET " 
//									+"\\\\"+config.getSecondaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 + hs.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
//						}
						
//						if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 + hs.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//							return "Photo not found for home player in "+config.getPrimaryIpAddress();
//						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerGrp1$img_Player*TEXTURE*IMAGE SET " 
								+ Constants.ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 + hs.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerGrp1$img_PlayerShadow*TEXTURE*IMAGE SET " 
								+ Constants.ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 + hs.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
						
					}
				}
				for(Player as : matchAllData.getSetup().getAwaySquad()) {
					
					if(as.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)||as.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
//						if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
//							if(!new File("\\\\"+config.getSecondaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + as.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//								return "Photo not found for away player in "+config.getSecondaryIpAddress();
//							}
//							CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerGrp2$img_Player*TEXTURE*IMAGE SET " 
//									+"\\\\"+config.getSecondaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + as.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
//						}
//						if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + as.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//							return "Photo not found for away player in "+config.getPrimaryIpAddress();
//						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerGrp2$img_Player*TEXTURE*IMAGE SET " 
								+ Constants.ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + as.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerGrp2$img_PlayerShadow*TEXTURE*IMAGE SET " 
								+ Constants.ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + as.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
						
					}
				}
			}else if(inning.getBattingTeamId() == matchAllData.getSetup().getAwayTeamId()){
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$FlagGrp1$img_Flag*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH +matchAllData.getSetup().getAwayTeam().getTeamName4() + "\0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$InfoGrp$FlagGrp2$img_Flag*TEXTURE*IMAGE SET " 
						+ Constants.ICC_U19_2023_FLAG_PATH +  matchAllData.getSetup().getHomeTeam().getTeamName4() + "\0", print_writers);
				for(Player hs : matchAllData.getSetup().getHomeSquad()) {
					if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)||hs.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
//						if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
//							if(!new File("\\\\"+config.getSecondaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + hs.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//								return "Photo not found for home player in "+config.getSecondaryIpAddress();
//							}
//							CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerGrp2$img_Player*TEXTURE*IMAGE SET " 
//									+"\\\\"+config.getSecondaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + hs.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
//						}
//						
//						if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + hs.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//							return "Photo not found for home player in "+config.getPrimaryIpAddress();
//						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerGrp2$img_Player*TEXTURE*IMAGE SET " 
								+ Constants.ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + hs.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
						
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerGrp2$img_PlayerShadow*TEXTURE*IMAGE SET " 
								+ Constants.ICC_U19_2023_PHOTOS_PATH + Constants.RIGHT_1024 + hs.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
						
						
					}
				}
				for(Player as : matchAllData.getSetup().getAwaySquad()) {
					if(as.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.CAPTAIN)||as.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
//						if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
//							if(!new File("\\\\"+config.getSecondaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 + as.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//								return "Photo not found for home player in "+config.getSecondaryIpAddress();
//							}
//							CricketFunctions.DoadWriteCommandToSelectedViz(2, "-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerGrp1$img_Player*TEXTURE*IMAGE SET " 
//								+"\\\\"+config.getSecondaryIpAddress()+"\\"+ Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 + as.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
//						}
//						
//						if(!new File("\\\\"+config.getPrimaryIpAddress()+"\\"+Constants.LOCAL_ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 + as.getPhoto() + CricketUtil.PNG_EXTENSION).exists()) {
//							return "Photo not found for home player in "+config.getPrimaryIpAddress();
//						}
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerGrp1$img_Player*TEXTURE*IMAGE SET " 
								+Constants.ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 + as.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$PlayerGrp1$img_PlayerShadow*TEXTURE*IMAGE SET " 
								+Constants.ICC_U19_2023_PHOTOS_PATH + Constants.LEFT_1024 + as.getPhoto()+ CricketUtil.PNG_EXTENSION + "\0", print_writers);
						
					}
				}
			}
			CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$gfx_Target$txt_TeamName*GEOM*TEXT SET " + inning.getBatting_team().getTeamName1() + " \0", print_writers);
			
			
			if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER) && matchAllData.getSetup().getMaxOvers() == 1) {
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs_1*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs_2*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$InfoGrp$txt_Info1*GEOM*TEXT SET " + "FROM " + (matchAllData.getSetup().getMaxOvers()*6) + 
						" BALLS" + " \0", print_writers);
				CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$InfoGrp$txt_Info2*GEOM*TEXT SET " + "WINNER WILL BE DECIDED BY SUPER OVER" + " \0", print_writers);
			}else {
				if(matchAllData.getSetup().getTargetOvers() == "" || matchAllData.getSetup().getTargetOvers().trim().isEmpty() && matchAllData.getSetup().getTargetRuns() == 0) {
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs_1*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs_2*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$InfoGrp$txt_Info1*GEOM*TEXT SET " + "FROM " + CricketFunctions.getTargetOvers(matchAllData) + " OVERS" + " \0", print_writers);
					CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$InfoGrp$txt_Info2*GEOM*TEXT SET " + "AT " + CricketFunctions.
							generateRunRate(CricketFunctions.getTargetRuns(matchAllData), Integer.valueOf(CricketFunctions.getTargetOvers(matchAllData)), 0, 2, 
								matchAllData) + " RUNS PER OVER" + " \0", print_writers);

				}else {
					if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase(CricketUtil.VJD)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs_1*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs_2*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$InfoGrp$txt_Info1*GEOM*TEXT SET " + "FROM " + 
								CricketFunctions.getTargetOvers(matchAllData) + " OVERS" + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$InfoGrp$txt_Info2*GEOM*TEXT SET " + "AT "+ CricketFunctions.
								generateRunRate(CricketFunctions.getTargetRuns(matchAllData), Integer.valueOf(CricketFunctions.getTargetOvers(matchAllData)), 0, 2, 
										matchAllData) + " RUNS PER OVER " + "(" + CricketUtil.VJD + ")"+" \0", print_writers);
						
					}else if(matchAllData.getSetup().getTargetType().toUpperCase().equalsIgnoreCase(CricketUtil.DLS)) {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs_1*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs_2*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$InfoGrp$txt_Info1*GEOM*TEXT SET " + "FROM " + 
								CricketFunctions.getTargetOvers(matchAllData) + " OVERS" + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$InfoGrp$txt_Info2*GEOM*TEXT SET " + "AT "+ CricketFunctions.
								generateRunRate(CricketFunctions.getTargetRuns(matchAllData), Integer.valueOf(CricketFunctions.getTargetOvers(matchAllData)), 0, 2, 
										matchAllData) + " RUNS PER OVER "+ "(" + CricketUtil.DLS + ")" +" \0", print_writers);
					}else {
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs_1*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$RunsGrp$txt_Runs_2*GEOM*TEXT SET " + CricketFunctions.getTargetRuns(matchAllData) + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$InfoGrp$txt_Info1*GEOM*TEXT SET " + "FROM " + 
								CricketFunctions.getTargetOvers(matchAllData) + " OVERS" + " \0", print_writers);
						CricketFunctions.DoadWriteCommandToAllViz("-1 RENDERER*BACK_LAYER*TREE*$InfoGrp$txt_Info2*GEOM*TEXT SET " + "AT "+ CricketFunctions.
								generateRunRate(CricketFunctions.getTargetRuns(matchAllData), Integer.valueOf(CricketFunctions.getTargetOvers(matchAllData)), 0, 2, 
										matchAllData) + " RUNS PER OVER "+" \0", print_writers);
					}
				}
			}
			break;
		}
	}
}