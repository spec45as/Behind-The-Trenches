package strategicLogic;

//import static strategicLogic.helpful.Println.*;

import java.util.*;

public class Game {
    private GameStrategyMap map;
    private int month;
    private int year;
    private HashMap<String, Squad> listOfSquads;
    public int playerTeamID = 0;
    private static int lostTeams = 0;
    public final static int COST_DEVELOPMENT = 300;
    public final static int COST_FORTIFICATION = 200;

    public GameStrategyMap getMap() {
        return map;
    }

    public void increaseData() {
        if (month == 12) {
            month = 1;
            year++;
        } else
            month++;
    }

    public String getMonth() {
        switch (month) {
        case 1:
            return "January";
        case 2:
            return "February";
        case 3:
            return "March";
        case 4:
            return "April";
        case 5:
            return "May";
        case 6:
            return "June";
        case 7:
            return "July";
        case 8:
            return "August";
        case 9:
            return "September";
        case 10:
            return "October";
        case 11:
            return "November";
        case 12:
            return "December";
        default:
            return "Fault";
        }
    }

    public int getYear() {
        return year;
    }

    public void setSquadToListOfSquads(Squad squad) {
        listOfSquads.put(squad.getID(), squad);
    }

    public HashMap<String, Squad> getListOfSquads() {
        return listOfSquads;
    }

    public HashMap<String, Squad> getListOfSquadsBelongToTeam(Team team) {
        HashMap<String, Squad> listOfSquadsBelongToTeam = new HashMap<String, Squad>();
        for (String k : getListOfSquads().keySet()) {
            if (getListOfSquads().get(k).getTeamOfSquad() == team) {
                listOfSquadsBelongToTeam.put(getListOfSquads().get(k).getID(), getListOfSquads().get(k));
            }
        }
        return listOfSquadsBelongToTeam;
    }

    public void endOfTurn(int teamID) {
        if (teamID == 3) {
            playerTeamID = 0;
            increaseData();
        } else
            playerTeamID++;
        while (getMap().getTeams().get(playerTeamID).getGeneral() == null) {
            if (playerTeamID == 3) {
                playerTeamID = 0;
                increaseData();
            } else
                playerTeamID++;
        }
        

        getMap().getTeams().get(teamID).endOfTurn();
    }

    public Game() {
        month = 8;
        year = 1914;
        listOfSquads = new HashMap<String, Squad>();
        map = new GameStrategyMap();
    }

    public int amountLostTeams() {
        return lostTeams;
    }

    public static void addLostTeam() {
        lostTeams++;
    }

}