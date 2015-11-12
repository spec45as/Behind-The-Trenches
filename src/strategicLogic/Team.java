package strategicLogic;

import gameframework.Framework;

import java.util.*;

public class Team {
	private String name;
	private String id;
	private int money;
	private int people;
	private General teamGeneral;
	private boolean turn;
	private String color;

	Team(String nameCountry, String id, String nameGeneral, String color) {
		setCountryName(nameCountry);
		setID(id);
		setMoney(0);
		setPeople(0);
		resetTurn();
		this.setColor(color);
		teamGeneral = new General(nameGeneral, this);
	}

	void setCountryName(String nameCountry) {
		name = nameCountry;
	}

	void setID(String id) {
		this.id = id;
	}

	void setMoney(int money) {
		this.money = money;
	}

	void setPeople(int people) {
		this.people = people;
	}
	
	void deleteGeneral() {
		teamGeneral = null;
	}

	void makeTurn() {
		turn = true;
	}

	void resetTurn() {
		turn = false;
	}

	public String getName() {
		return name;
	}

	public String getID() {
		return id;
	}

	public int getMoney() {
		return money;
	}

	public int getPeople() {
		return people;
	}

	public General getGeneral() {
		return teamGeneral;
	}

	public boolean getTurn() {
		return turn;
	}

	public void endOfTurn() {
		resetTurn();
		ArrayList<Region> teamRegions = new ArrayList<Region>();
		teamRegions = Framework.activeGame().getStrategyLogic().getMap().getRegionsBelongToTeam(this);
		boolean population = false;
		int peopleLimit = 0;
		for (int i = 0; i < teamRegions.size(); i++)
			peopleLimit += teamRegions.get(i).getPeopleAmount();
		for (int i = 0; i < teamRegions.size(); i++) {
			money += teamRegions.get(i).getMoneyGrowth();
			if (!population)
				if ((people += teamRegions.get(i).getPeopleGrowth()) >= peopleLimit) {
					population = true;
					people = peopleLimit;
				}
		}
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}