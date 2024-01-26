package com.cricket.containers;

import java.util.Arrays;

public class L3Griff 
{
	private String HeaderText;
	private String FirstName;
	private String SurName;
	private String SubTitle;
	private String WhichSponsor;
	private String WhichTeamFlag;
	
	private int numberOfSubLines;
	
	private String[] TopTitlesText;
	private String[] TopStatsText;
	private String[] BottomTitlesText;
	private String[] BottomStatsText;
	private String[] Position;
	
	public L3Griff(String headerText, String firstName, String surName, String subTitle, String whichSponsor,
			String whichTeamFlag, int numberOfSubLines, String[] topTitlesText, String[] topStatsText,
			String[] bottomTitlesText, String[] bottomStatsText, String[] position) {
		super();
		HeaderText = headerText;
		FirstName = firstName;
		SurName = surName;
		SubTitle = subTitle;
		WhichSponsor = whichSponsor;
		WhichTeamFlag = whichTeamFlag;
		this.numberOfSubLines = numberOfSubLines;
		TopTitlesText = topTitlesText;
		TopStatsText = topStatsText;
		BottomTitlesText = bottomTitlesText;
		BottomStatsText = bottomStatsText;
		Position = position;
	}

	public L3Griff() {
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

	public String getWhichSponsor() {
		return WhichSponsor;
	}

	public void setWhichSponsor(String whichSponsor) {
		WhichSponsor = whichSponsor;
	}

	public String getWhichTeamFlag() {
		return WhichTeamFlag;
	}

	public void setWhichTeamFlag(String whichTeamFlag) {
		WhichTeamFlag = whichTeamFlag;
	}

	public int getNumberOfSubLines() {
		return numberOfSubLines;
	}

	public void setNumberOfSubLines(int numberOfSubLines) {
		this.numberOfSubLines = numberOfSubLines;
	}

	public String[] getTopTitlesText() {
		return TopTitlesText;
	}

	public void setTopTitlesText(String[] topTitlesText) {
		TopTitlesText = topTitlesText;
	}

	public String[] getTopStatsText() {
		return TopStatsText;
	}

	public void setTopStatsText(String[] topStatsText) {
		TopStatsText = topStatsText;
	}

	public String[] getBottomTitlesText() {
		return BottomTitlesText;
	}

	public void setBottomTitlesText(String[] bottomTitlesText) {
		BottomTitlesText = bottomTitlesText;
	}

	public String[] getBottomStatsText() {
		return BottomStatsText;
	}

	public void setBottomStatsText(String[] bottomStatsText) {
		BottomStatsText = bottomStatsText;
	}

	public String[] getPosition() {
		return Position;
	}

	public void setPosition(String[] position) {
		Position = position;
	}

	@Override
	public String toString() {
		return "L3Griff [HeaderText=" + HeaderText + ", FirstName=" + FirstName + ", SurName=" + SurName + ", SubTitle="
				+ SubTitle + ", WhichSponsor=" + WhichSponsor + ", WhichTeamFlag=" + WhichTeamFlag
				+ ", numberOfSubLines=" + numberOfSubLines + ", TopTitlesText=" + Arrays.toString(TopTitlesText)
				+ ", TopStatsText=" + Arrays.toString(TopStatsText) + ", BottomTitlesText="
				+ Arrays.toString(BottomTitlesText) + ", BottomStatsText=" + Arrays.toString(BottomStatsText)
				+ ", Position=" + Arrays.toString(Position) + "]";
	}
	
}