package strategicLogic;

import static strategicLogic.helpful.Println.*;

import java.util.*;

public class Region {
	private String name;
	private Team team;
	private int uniqID;
	private int peopleGrowth;
	private int moneyGrowth;
	private int peopleAmount;
	private int levelDevelopment;
	private String typeDevelopment;
	private int levelDefence;
	private ArrayList<Integer> neighbourRegions;
	private boolean isGeneralHere;
	private int generalX;
	private int generalY;

	Region(String name, Team team, int xGeneral, int yGeneral, int uniqID,
			int peopleGrowth, int moneyGrowth, int peopleAmount,
			ArrayList<Integer> neighbourRegions, boolean presentOfGeneral) {
		setName(name);
		setTeam(team);
		setUniqID(uniqID);
		setPeopleGrowth(peopleGrowth);
		setMoneyGrowth(moneyGrowth);
		setPeopleAmount(peopleAmount);
		levelDevelopment = 0;
		typeDevelopment = "None";
		levelDefence = 0;
		this.neighbourRegions = neighbourRegions;
		isGeneralHere = presentOfGeneral;
		setGeneralX(xGeneral);
		setGeneralY(yGeneral);
	}

	void setName(String name) {
		this.name = name;
	}

	void setTeam(Team team) {
		this.team = team;
	}

	void setUniqID(int uniqID) {
		this.uniqID = uniqID;
	}

	void setPeopleGrowth(int d) {
		this.peopleGrowth = d;
	}

	void setMoneyGrowth(int moneyGrowth) {
		this.moneyGrowth = moneyGrowth;
	}

	void setPeopleAmount(int peopleAmount) {
		this.peopleAmount = peopleAmount;
	}

	void destroyDevelopment() {
		if (typeDevelopment.equals("money"))
			setMoneyGrowth((int) (getMoneyGrowth() / (1 + 0.2 * levelDevelopment)));
		else
			setPeopleGrowth((int) (getPeopleGrowth() / (1 + 0.2 * levelDevelopment)));
		levelDevelopment = 0;
	}

	void destroyDefence() {
		if (levelDefence > 1)
			levelDefence = 1;
		else
			levelDefence = 0;
	}

	void increaseGrowth() {
		if (typeDevelopment.equals("money"))
			setMoneyGrowth((int) (((float)getMoneyGrowth())
					/ (1.0 + 0.2 * (levelDevelopment)) * (1.0 + 0.2 * (levelDevelopment+1))));
		else
			setPeopleGrowth((int)(((float)getPeopleGrowth())
					/ (1.0 + 0.2 * (levelDevelopment)) * (1.0 + 0.2 * (levelDevelopment+1))));
	}

	public void develop(String str) {
		if (levelDevelopment == 0) {
			if ((Game.COST_DEVELOPMENT * (1 + 0.2 * levelDevelopment)) <= getTeam()
					.getMoney()) {

				typeDevelopment = str;
				increaseGrowth();
				getTeam()
						.setMoney(
								getTeam().getMoney()
										- (int) (Game.COST_DEVELOPMENT * (1 + 0.2 * levelDevelopment)));
				levelDevelopment = 1;

			}
		} else if (str.equals(typeDevelopment))
			if (levelDevelopment < 5) {
				if ((Game.COST_DEVELOPMENT * (1 + 0.2 * levelDevelopment)) <= getTeam()
						.getMoney()) {

					increaseGrowth();
					getTeam()
							.setMoney(
									getTeam().getMoney()
											- (int) (Game.COST_DEVELOPMENT * (1 + 0.2 * levelDevelopment)));
					levelDevelopment++;

				}
			} else
				println("Max level of modernisation was reached");
		else {
			if (Game.COST_DEVELOPMENT <= getTeam().getMoney()) {

				typeDevelopment = str;
				if (str.equals("money")) {
					setMoneyGrowth((int) (getMoneyGrowth() * 1.2));
					setPeopleGrowth((int) (getPeopleGrowth() / (1.0 + 0.2 * (levelDevelopment))));
				} else {
					setPeopleGrowth((int) (getPeopleGrowth() * 1.2));
					setMoneyGrowth((int) (getMoneyGrowth() / (1.0 + 0.2 * (levelDevelopment))));
				}
				levelDevelopment = 1;
				getTeam()
						.setMoney(getTeam().getMoney() - Game.COST_DEVELOPMENT);
			}
		}
	}

	public void fortify() {
		if ((Game.COST_FORTIFICATION * (1 + levelDefence)) <= getTeam()
				.getMoney()) {
			if (levelDefence < 5) {
				levelDefence++;
				getTeam()
						.setMoney(
								getTeam().getMoney()
										- (int) (Game.COST_FORTIFICATION * levelDefence));
			} else
				println("Max level of defence was reached");
		}
	}

	void addNeighbourRegion(Integer neighbourRegion) {
		neighbourRegions.add(neighbourRegion);
	}

	void changePresentGeneral() {
		isGeneralHere = !isGeneralHere;
	}

	public String getName() {
		return name;
	}

	public Team getTeam() {
		return team;
	}

	public int getUniqID() {
		return uniqID;
	}

	public int getPeopleGrowth() {
		return peopleGrowth;
	}

	public int getMoneyGrowth() {
		return moneyGrowth;
	}

	public int getPeopleAmount() {
		return peopleAmount;
	}

	public int getLevelDevelopment() {
		return levelDevelopment;
	}

	public String getTypeDevelopment() {
		return typeDevelopment;
	}

	public int getLevelDefence() {
		return levelDefence;
	}

	public ArrayList<Integer> getUniqIDNeighbourRegions() {
		return neighbourRegions;
	}

	public boolean presentGeneral() {
		return isGeneralHere;
	}

	public int getGeneralX() {
		return generalX;
	}

	public void setGeneralX(int generalX) {
		this.generalX = generalX;
	}

	public int getGeneralY() {
		return generalY;
	}

	public void setGeneralY(int generalY) {
		this.generalY = generalY;
	}
}