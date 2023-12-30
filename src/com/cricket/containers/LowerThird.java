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
	
	private int numberOfSubLines;
	
	private String[] TitlesText;
	private String[] StatsText;
	private String[] LeftText;
	private String[] RightText;
	
	public LowerThird(String headerText, String firstName, String surName, String subTitle, String scoreText,
			String ballsFacedText, int numberOfSubLines, String[] titlesText, String[] statsText, String[] leftText,
			String[] rightText) {
		super();
		HeaderText = headerText;
		FirstName = firstName;
		SurName = surName;
		SubTitle = subTitle;
		ScoreText = scoreText;
		BallsFacedText = ballsFacedText;
		this.numberOfSubLines = numberOfSubLines;
		TitlesText = titlesText;
		StatsText = statsText;
		LeftText = leftText;
		RightText = rightText;
	}

	public LowerThird() {
		super();
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

	@Override
	public String toString() {
		return "LowerThird [HeaderText=" + HeaderText + ", FirstName=" + FirstName + ", SurName=" + SurName
				+ ", SubTitle=" + SubTitle + ", ScoreText=" + ScoreText + ", BallsFacedText=" + BallsFacedText
				+ ", numberOfSubLines=" + numberOfSubLines + ", TitlesText=" + Arrays.toString(TitlesText)
				+ ", StatsText=" + Arrays.toString(StatsText) + ", LeftText=" + Arrays.toString(LeftText)
				+ ", RightText=" + Arrays.toString(RightText) + "]";
	}

}