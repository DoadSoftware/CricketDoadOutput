package com.cricket.containers;

import java.util.Arrays;

public class LowerThird 
{
	private String HeaderText;
	private String FirstName;
	private String SurName;
	private String SubTitle;
	private String ScoreText;
	private String BallsFacedText;
	private String WhichSponsor;
	private String WhichTeamFlag;
	
	private int numberOfSubLines;
	
	private String[] TitlesText;
	private String[] StatsText;
	private String[] LeftText;
	private String[] RightText;
	private String[] Position;
	
	public LowerThird(String headerText, String firstName, String surName, String subTitle, String scoreText,
			String ballsFacedText, int numberOfSubLines, String WhichSponsor, String WhichTeamFlag, String[] titlesText, String[] statsText, String[] leftText,
			String[] rightText,String[] position) {
		super();
		this.HeaderText = headerText;
		this.FirstName = firstName;
		this.SurName = surName;
		this.SubTitle = subTitle;
		this.ScoreText = scoreText;
		this.BallsFacedText = ballsFacedText;
		this.numberOfSubLines = numberOfSubLines;
		this.TitlesText = titlesText;
		this.StatsText = statsText;
		this.LeftText = leftText;
		this.RightText = rightText;
		this.WhichSponsor = WhichSponsor;
		this.WhichTeamFlag = WhichTeamFlag;
		this.Position = position;
	}

	public LowerThird() {
		super();
	}

	public String getWhichSponsor() {
		return WhichSponsor;
	}

	public void setWhichSponsor(String whichSponsor) {
		WhichSponsor = whichSponsor;
	}

	public String getHeaderText() {
		return HeaderText;
	}

	public void setHeaderText(String headerText) {
		HeaderText = headerText;
	}

	public String getFirstName() {
		return FirstName;
	}

	public void setFirstName(String firstName) {
		FirstName = firstName;
	}

	public String getSurName() {
		return SurName;
	}

	public void setSurName(String surName) {
		SurName = surName;
	}

	public String getSubTitle() {
		return SubTitle;
	}

	public void setSubTitle(String subTitle) {
		SubTitle = subTitle;
	}

	public String getScoreText() {
		return ScoreText;
	}

	public void setScoreText(String scoreText) {
		ScoreText = scoreText;
	}

	public String getBallsFacedText() {
		return BallsFacedText;
	}

	public void setBallsFacedText(String ballsFacedText) {
		BallsFacedText = ballsFacedText;
	}

	public int getNumberOfSubLines() {
		return numberOfSubLines;
	}

	public void setNumberOfSubLines(int numberOfSubLines) {
		this.numberOfSubLines = numberOfSubLines;
	}

	public String[] getTitlesText() {
		return TitlesText;
	}

	public void setTitlesText(String[] titlesText) {
		TitlesText = titlesText;
	}

	public String[] getStatsText() {
		return StatsText;
	}

	public void setStatsText(String[] statsText) {
		StatsText = statsText;
	}

	public String[] getLeftText() {
		return LeftText;
	}

	public void setLeftText(String[] leftText) {
		LeftText = leftText;
	}

	public String[] getRightText() {
		return RightText;
	}

	public void setRightText(String[] rightText) {
		RightText = rightText;
	}

	public String getWhichTeamFlag() {
		return WhichTeamFlag;
	}

	public void setWhichTeamFlag(String whichTeamFlag) {
		WhichTeamFlag = whichTeamFlag;
	}

	public String[] getPosition() {
		return Position;
	}

	public void setPosition(String[] position) {
		Position = position;
	}

	@Override
	public String toString() {
		return "LowerThird [HeaderText=" + HeaderText + ", FirstName=" + FirstName + ", SurName=" + SurName
				+ ", SubTitle=" + SubTitle + ", ScoreText=" + ScoreText + ", BallsFacedText=" + BallsFacedText
				+ ", WhichSponsor=" + WhichSponsor + ", WhichTeamFlag=" + WhichTeamFlag + ", numberOfSubLines="
				+ numberOfSubLines + ", TitlesText=" + Arrays.toString(TitlesText) + ", StatsText="
				+ Arrays.toString(StatsText) + ", LeftText=" + Arrays.toString(LeftText) + ", RightText="
				+ Arrays.toString(RightText) + ", Position=" + Arrays.toString(Position) + "]";
	}

}