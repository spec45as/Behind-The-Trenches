package strategicLogic;

import gameframework.Framework;

import java.util.*;

import static strategicLogic.helpful.Println.*;

public class General {
	private String name;
	private Team team;
	private int level = 1;
	private ArrayList<Squad> squads;

	General(String name, Team team) {
		setName(name);
		setTeam(team);
		squads = new ArrayList<Squad>();
	}
	
	public void setStartSquads () {
		switch (team.getID()) {
		case "fr":
			squads.add(Framework.activeGame().getStrategyLogic().getListOfSquads().get("fr_inf").clone());
			break;
		case "ger":
			squads.add(Framework.activeGame().getStrategyLogic().getListOfSquads().get("ger_inf").clone());
			break;
		case "aus_hun":
			squads.add(Framework.activeGame().getStrategyLogic().getListOfSquads().get("aus_inf").clone());
			break;
		case "rus":
			squads.add(Framework.activeGame().getStrategyLogic().getListOfSquads().get("rus_inf").clone());
			break;
		default:
			println("");
		}
	}

	void setName(String name) {
		this.name = name;
	}

	void setTeam(Team team) {
		this.team = team;
	}

	void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public Team getTeam() {
		return team;
	}

	public int getLevel() {
		return level;
	}

	public ArrayList<Squad> getSquads() {
		return squads;
	}

	public void buySquad(String idSquad) throws CloneNotSupportedException {
		if ((Framework.activeGame().getStrategyLogic().getListOfSquads().get(idSquad).getCostMoney() <= getTeam()
						.getMoney())
						& (Framework.activeGame().getStrategyLogic().getListOfSquads().get(idSquad).getCostPeople() <= getTeam()
								.getPeople())) {
					squads.add((Squad) Framework.activeGame().getStrategyLogic().getListOfSquads().get(idSquad).clone());
					team.setMoney(team.getMoney()-Framework.activeGame().getStrategyLogic().getListOfSquads().get(idSquad).getCostMoney());
					team.setPeople(team.getPeople()-Framework.activeGame().getStrategyLogic().getListOfSquads().get(idSquad).getCostPeople());
					println("The squad "
							+ Framework.activeGame().getStrategyLogic().getListOfSquads().get(idSquad).getName()
							+ " was succesfully bought");
				} else
					println("You don't have enough resources to buy "
							+ Framework.activeGame().getStrategyLogic().getListOfSquads().get(idSquad).getName());
	}

	public boolean move(int regionID) {
		if (!getTeam().getTurn()) {
			int currentPosition = Framework.activeGame().getStrategyLogic().getMap().getRegionOfGeneral(this);
			Region currentRegion = Framework.activeGame().getStrategyLogic().getMap().getRegions().get(currentPosition);
			ArrayList<Integer> neighbourRegions = currentRegion.getUniqIDNeighbourRegions();
			ArrayList<Region> teamRegions = Framework.activeGame().getStrategyLogic().getMap().getRegionsBelongToTeam(team);
			boolean isAimBelongToTeam = false;
			for (int i = 0; i < teamRegions.size(); i++)
				if (teamRegions.get(i).getUniqID() == regionID) {
					isAimBelongToTeam = true;
					break;
				}
			Region aimRegion = Framework.activeGame().getStrategyLogic().getMap().getRegions().get(regionID);
			for (int i = 0; i < neighbourRegions.size(); i++)
				if (neighbourRegions.get(i) == regionID) {
					team.makeTurn();
					if (!isAimBelongToTeam) {
						if (aimRegion.presentGeneral()) {
							if (getSquads().size() > (aimRegion.getTeam()
									.getGeneral().getSquads().size() + 1 + aimRegion
									.getLevelDefence())) {
								Team enemy = aimRegion.getTeam();
								currentRegion.changePresentGeneral();
								aimRegion.setTeam(team);
								aimRegion.destroyDevelopment();
								aimRegion.destroyDefence();
								ArrayList<Integer> NeighbourhoodsOfAim = aimRegion
										.getUniqIDNeighbourRegions();
								int j;
								for (j = 0; j < NeighbourhoodsOfAim.size(); j++)
									if (Framework.activeGame().getStrategyLogic().getMap().getRegions()
											.get(NeighbourhoodsOfAim.get(j))
											.getTeam() == enemy) {
										Framework.activeGame().getStrategyLogic().getMap().getRegions()
												.get(NeighbourhoodsOfAim.get(j))
												.changePresentGeneral();
										break;
									}
								if (j == NeighbourhoodsOfAim.size()) {
									ArrayList<Region> enemyTeamRegion = Framework.activeGame().getStrategyLogic().getMap().getRegionsBelongToTeam(enemy);
									int k;
									for (k = 0; k < enemyTeamRegion.size(); k++) {
										if ((enemyTeamRegion.get(k).getUniqID()-aimRegion.getUniqID() > 0) || (k == enemyTeamRegion.size())) {
											enemyTeamRegion.get(k).changePresentGeneral();
											break;
										}
									}
									if (k == enemyTeamRegion.size()) {
										enemy.deleteGeneral();
										Game.addLostTeam();
									}
								}
								println("The region " + aimRegion.getName()
										+ " was captured");
							} else
								println("You couldn't capture the region "
										+ aimRegion.getName());
						} else if (getSquads().size() > 1 + aimRegion
								.getLevelDefence()) {
							currentRegion.changePresentGeneral();
							aimRegion.setTeam(team);
							aimRegion.destroyDevelopment();
							aimRegion.destroyDefence();
							aimRegion.changePresentGeneral();
							println("The region " + aimRegion.getName()
									+ " was captured");
						} else
							println("You couldn't capture the region "
									+ aimRegion.getName());
					} else {
						aimRegion.changePresentGeneral();
						currentRegion.changePresentGeneral();
						println("Your General " + getName() + " moved to "
								+ aimRegion.getName());
					}
					return true;
				}
			println("The ID of input region was false - this is not the neighbourhood of region"
					+ currentRegion.getName());
			return false;
		}
		else {
			return false;
		}
	}
}