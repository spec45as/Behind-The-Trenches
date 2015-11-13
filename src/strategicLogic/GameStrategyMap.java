package strategicLogic;

import gameframework.Framework;

import java.io.*;
import java.util.*;

public class GameStrategyMap {
	private ArrayList<Team> teams;
	private ArrayList<Region> regions;

	public void loadGameInfo() {
		{
			regions = new ArrayList<Region>();
			teams = new ArrayList<Team>();
			boolean f = true;

			try {
				BufferedReader fileTeams = Framework.activeGame().getGameResources().getTXT("data/teams.txt");
				try {
					String team;
					while ((team = fileTeams.readLine()) != null) {
						team.trim();
						String[] parts = team.split(", ");
						Team teamToAdd;
						teamToAdd = new Team(parts[0],
								parts[1], parts[2], parts[3]);
						teams.add(teamToAdd);
					}
				} finally {
					fileTeams.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			teams.add(new Team("Neutral", "neut", "", "COLOR"));

			try {
	              BufferedReader fileSquads = Framework.activeGame().getGameResources().getTXT("data/squads.txt");
				try {
					String squad;
					while ((squad = fileSquads.readLine()) != null) {
						squad.trim();
						String[] parts = squad.split(", ");
						Squad squadToAdd = new Squad(parts[0],
								teams.get((int) Integer.parseInt(parts[1])),
								(int) Integer.parseInt(parts[2]),
								(int) Integer.parseInt(parts[3]), (int) Integer.parseInt(parts[4]), parts[5],
								parts[6]);
						Framework.activeGame().getStrategyLogic()
								.setSquadToListOfSquads(squadToAdd);
					}
				} finally {
					fileSquads.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			for (int i = 0; i < getTeams().size(); i++) {
				getTeams().get(i).getGeneral().setStartSquads();
			}

			try {
                BufferedReader fileRegions = Framework.activeGame().getGameResources().getTXT("data/regions.txt");
				try {
					String region;
					while ((region = fileRegions.readLine()) != null) {
						region.trim();
						String[] parts = region.split(", ");
						if (f) {
							parts[0] = parts[0].substring(3);
							f = false;
						}
						String[] part = parts[8].split(";");
						ArrayList<Integer> neighbourhoods = new ArrayList<Integer>();
						for (int i = 0; i < part.length; i++) {
							neighbourhoods.add(Integer.parseInt(part[i]));
						}
						Region regionToAdd = new Region(parts[0],
								teams.get((int) Integer.parseInt(parts[1])),
								(int)Integer.parseInt(parts[2]),
								(int) Integer.parseInt(parts[3]),
								(int) Integer.parseInt(parts[4]),
								(int) Integer.parseInt(parts[5]),
								(int) Integer.parseInt(parts[6]),
								(int) Integer.parseInt(parts[7]),
								neighbourhoods,
								(boolean) Boolean.parseBoolean(parts[9]));
						regions.add(regionToAdd);
					}
				} finally {
					fileRegions.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public ArrayList<Team> getTeams() {
		return teams;
	}

	public ArrayList<Region> getRegions() {
		return regions;
	}

	public int getRegionOfGeneral(General general) {
		Team generalTeam = general.getTeam();
		boolean f = true;
		int i = 0;
		while (f) {
			if (getRegionsBelongToTeam(generalTeam).get(i).presentGeneral())
				f = false;
			else
				i++;
		}
		return getRegionsBelongToTeam(generalTeam).get(i).getUniqID();
	}

	ArrayList<Region> getRegionsBelongToTeam(Team team) {
		ArrayList<Region> regionsBelongToTeam = new ArrayList<Region>();
		for (int i = 0; i < getRegions().size(); i++)
			if (regions.get(i).getTeam() == team)
				regionsBelongToTeam.add(regions.get(i));
		return regionsBelongToTeam;
	}
}