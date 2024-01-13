package com.cricket.containers;

import java.util.List;

import com.cricket.model.BattingCard;
import com.cricket.model.BowlingCard;

public class Infobar {
	
	private boolean infobar_on_screen = false;
	private boolean powerplay_on_screen = false;
	private boolean powerplay_end = false;
	private boolean infobar_pushed = false;
	private boolean thisOvers_Title_Fade = false;
	
	private int player_id;
	
	private String infobar_status;
	private String BatsmanAndBowlOrSponsor;
	
	private String Left_bottom;
	private String Last_left_bottom;
	
	private String Right_top;
	private String Last_right_top;
	
	private String Right_bottom;
	private String Last_right_bottom;

	private String Middle_section;
	private String Last_middle_section;
	
	private String Right_section;
	private String Last_right_section;
	
	private List<BattingCard> last_batsmen;
	private BowlingCard last_bowler;
	private String last_this_over;
	
	public boolean isInfobar_pushed() {
		return infobar_pushed;
	}
	public void setInfobar_pushed(boolean infobar_pushed) {
		this.infobar_pushed = infobar_pushed;
	}
	public String getInfobar_status() {
		return infobar_status;
	}
	public void setInfobar_status(String infobar_status) {
		this.infobar_status = infobar_status;
	}
	public String getLast_this_over() {
		return last_this_over;
	}
	public void setLast_this_over(String last_this_over) {
		this.last_this_over = last_this_over;
	}
	public boolean isInfobar_on_screen() {
		return infobar_on_screen;
	}
	public void setInfobar_on_screen(boolean infobar_on_screen) {
		this.infobar_on_screen = infobar_on_screen;
	}
	public boolean isPowerplay_on_screen() {
		return powerplay_on_screen;
	}
	public void setPowerplay_on_screen(boolean powerplay_on_screen) {
		this.powerplay_on_screen = powerplay_on_screen;
	}
	public List<BattingCard> getLast_batsmen() {
		return last_batsmen;
	}
	public void setLast_batsmen(List<BattingCard> last_batsmen) {
		this.last_batsmen = last_batsmen;
	}
	public BowlingCard getLast_bowler() {
		return last_bowler;
	}
	public void setLast_bowler(BowlingCard last_bowler) {
		this.last_bowler = last_bowler;
	}
	public String getLeft_bottom() {
		return Left_bottom;
	}
	public void setLeft_bottom(String left_bottom) {
		Left_bottom = left_bottom;
	}
	public String getLast_left_bottom() {
		return Last_left_bottom;
	}
	public void setLast_left_bottom(String last_left_bottom) {
		Last_left_bottom = last_left_bottom;
	}
	public String getRight_bottom() {
		return Right_bottom;
	}
	public void setRight_bottom(String right_bottom) {
		Right_bottom = right_bottom;
	}
	public String getLast_right_bottom() {
		return Last_right_bottom;
	}
	public void setLast_right_bottom(String last_right_bottom) {
		Last_right_bottom = last_right_bottom;
	}
	public int getPlayer_id() {
		return player_id;
	}
	public void setPlayer_id(int player_id) {
		this.player_id = player_id;
	}
	public String getMiddle_section() {
		return Middle_section;
	}
	public void setMiddle_section(String middle_section) {
		Middle_section = middle_section;
	}
	public String getLast_middle_section() {
		return Last_middle_section;
	}
	public void setLast_middle_section(String last_middle_section) {
		Last_middle_section = last_middle_section;
	}
	public String getBatsmanAndBowlOrSponsor() {
		return BatsmanAndBowlOrSponsor;
	}
	public void setBatsmanAndBowlOrSponsor(String batsmanAndBowlOrSponsor) {
		BatsmanAndBowlOrSponsor = batsmanAndBowlOrSponsor;
	}
	public String getRight_section() {
		return Right_section;
	}
	public void setRight_section(String right_section) {
		Right_section = right_section;
	}
	public String getLast_right_section() {
		return Last_right_section;
	}
	public void setLast_right_section(String last_right_section) {
		Last_right_section = last_right_section;
	}
	public String getRight_top() {
		return Right_top;
	}
	public void setRight_top(String right_top) {
		Right_top = right_top;
	}
	public String getLast_right_top() {
		return Last_right_top;
	}
	public void setLast_right_top(String last_right_top) {
		Last_right_top = last_right_top;
	}
	public boolean isThisOvers_Title_Fade() {
		return thisOvers_Title_Fade;
	}
	public void setThisOvers_Title_Fade(boolean thisOvers_Title_Fade) {
		this.thisOvers_Title_Fade = thisOvers_Title_Fade;
	}
	public boolean isPowerplay_end() {
		return powerplay_end;
	}
	public void setPowerplay_end(boolean powerplay_end) {
		this.powerplay_end = powerplay_end;
	}
	
}
