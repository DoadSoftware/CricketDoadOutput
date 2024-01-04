package com.cricket.containers;

import java.util.List;

import com.cricket.model.BattingCard;
import com.cricket.model.BowlingCard;

public class Infobar {
	
	private boolean infobar_on_screen;
	private boolean powerplay_on_screen;
	
	private int player_id;
	
	private String Left_bottom;
	private String Last_left_bottom;
	
	private String Right_bottom;
	private String Last_right_bottom;
	
	private String Middle_section;
	private String Last_middle_section;
	
	List<BattingCard> last_batsmen;
	BowlingCard last_bowler;
	
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
	
}
