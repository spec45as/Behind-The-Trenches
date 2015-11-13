package gameframework.libs;

import gameframework.Framework;
import gameframework.entities.Human;
import gameframework.libs.other.VectorLight;

public class Team {
    private int team;
    private String country;
    private int gamePoints;
    private int flags;

    private long infTimeout;

    public long getInfTimeout() {
        return infTimeout;
    }

    public void setInfTimeout(long infTimeout) {
        this.infTimeout = infTimeout;
    }

    public long getHandMgTimeout() {
        return handMgTimeout;
    }

    public void setHandMgTimeout(long handMgTimeout) {
        this.handMgTimeout = handMgTimeout;
    }

    public long getMortarTimeout() {
        return mortarTimeout;
    }

    public void setMortarTimeout(long mortarTimeout) {
        this.mortarTimeout = mortarTimeout;
    }

    public long getStatMgTimeout() {
        return statMgTimeout;
    }

    public void setStatMgTimeout(long statMgTimeout) {
        this.statMgTimeout = statMgTimeout;
    }

    private long handMgTimeout;
    private long mortarTimeout;
    private long statMgTimeout;

    private int infStarting;
    private int statMgStarting;
    private int mortarStarting;
    private int handMgStarting;

    public int getInfStarting() {
        return infStarting;
    }

    public int getStatMgStarting() {
        return statMgStarting;
    }

    public int getMortarStarting() {
        return mortarStarting;
    }

    public int getHandMgStarting() {
        return handMgStarting;
    }

    private int infantry;
    private int statMg;
    private int mortar;
    private int handMg;

    public int getInfantry() {
        return infantry;
    }

    public void setInfantry(int infantry) {
        this.infantry = infantry;
    }

    public int getStatMg() {
        return statMg;
    }

    public void setStatMg(int statMg) {
        this.statMg = statMg;
    }

    public int getMortar() {
        return mortar;
    }

    public void setMortar(int mortar) {
        this.mortar = mortar;
    }

    public int getHandMg() {
        return handMg;
    }

    public void setHandMg(int handMg) {
        this.handMg = handMg;
    }

    public int getGamePoints() {
        return gamePoints;
    }

    public void setGamePoints(int gamePoints) {
        this.gamePoints = gamePoints;
    }

    public Team(int team, String country) {
        super();
        this.team = team;
        this.country = country;
        gamePoints = 0;

        infStarting = 15;
        statMgStarting = 5;
        mortarStarting = 5;
        handMgStarting = 10;

        if (team == 1) {
            infStarting = 40;
            statMgStarting = 10;
            handMgStarting = 10;
            mortarStarting = 10;
        }

        infantry = infStarting;
        statMg = statMgStarting;
        mortar = mortarStarting;
        handMg = handMgStarting;

    }

    public void spawnInfSquad() {
        long curTime = Framework.CurGameTime() * Framework.milisecInNanosec;
        if (getInfTimeout() <= curTime && getInfantry() > 0) {
            setInfantry(getInfantry() - 1);

            if (team == 0) {
                setInfTimeout(curTime + 45 * Framework.secInNanosec);
            }

            int startingY = 0;
            if (team == 0) {
                startingY = Math.max(128, Math.min((int) (100 * Math.random()) + Framework.activeGame().getPlayerCamera().getY(), Framework
                        .activeGame().getMapSizeY() - 600));
            } else {
                startingY = Framework.genRand(128, Framework.activeGame().getMapSizeY() - 1200);
            }
            
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 25; j++) {
                    if (team == 0) {
                        Human NPC = new Human("rifleman_uk", "weapon_uk_enfield");
                        Ents.addNewEntityInGame(NPC);
                        VectorLight pos = new VectorLight(32 + i * 32, startingY + j * 22, 0);
                        NPC.SetPos(pos);
                        NPC.getAI().setTeam(team);
                        NPC.getAI().setRunning(false);
                        NPC.getAI().setDestX(1024 + i * 32);
                        NPC.getAI().setDestY((int) NPC.GetPos().y);
                    } else {
                        Human NPC = new Human("rifleman_ger", "weapon_uk_enfield");
                        Ents.addNewEntityInGame(NPC);
                        VectorLight pos = new VectorLight(Framework.activeGame().getMapSizeX() - 32 - i * 64 + Framework.genRand(-8, 8),
                                startingY + j * 48 + Framework.genRand(-8, 8), 0);
                        NPC.SetPos(pos);
                        NPC.getAI().setTeam(team);
                        NPC.getAI().setRunning(true);
                        NPC.getAI().setDestX(256 - i * 64);
                        NPC.getAI().setDestY((int) NPC.GetPos().y);
                    }
                }
            }
        }
    }

    public void spawnHandMgSquad() {
        long curTime = Framework.CurGameTime() * Framework.milisecInNanosec;
        if (getHandMgTimeout() <= curTime && getHandMg() > 0) {
            setHandMg(getHandMg() - 1);
            if (team == 0) {
                setHandMgTimeout(curTime + 60 * Framework.secInNanosec);
            }
            int startingY = 0;
            if (team == 0) {
                startingY = Math.max(128, Math.min((int) (100 * Math.random()) + Framework.activeGame().getPlayerCamera().getY(), Framework
                        .activeGame().getMapSizeY() - 600));
            } else {
                startingY = Framework.genRand(128, Framework.activeGame().getMapSizeY() - 500);

            }
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 10; j++) {
                    if (team == 0) {
                        Human NPC;
                        VectorLight pos = new VectorLight(32 + i * 40, startingY + j * 40, 0);
                        if (i == 2 && (j == 0 || j == 4 || j == 9)) {
                            NPC = new Human("hand_mg_uk", "weapon_hand_mg_uk_lewis");
                            NPC.getAI().setRunning(false);
                            pos = new VectorLight(64 + i * 40, startingY + j * 40, 0);
                        } else {
                            NPC = new Human("rifleman_uk", "weapon_uk_enfield");
                        }
                        Ents.addNewEntityInGame(NPC);
                        NPC.SetPos(pos);
                        NPC.getAI().setTeam(team);
                        NPC.getAI().setDestX(1024 + i * 40);
                        NPC.getAI().setDestY((int) NPC.GetPos().y);
                    } else {
                        Human NPC;
                        if (i == 2 && (j == 0 || j == 4 || j == 9)) {
                            NPC = new Human("hand_mg_ger", "weapon_hand_mg_uk_lewis");
                        } else {
                            NPC = new Human("rifleman_ger", "weapon_uk_enfield");
                        }
                        Ents.addNewEntityInGame(NPC);
                        VectorLight pos = new VectorLight(Framework.activeGame().getMapSizeX() - 32 - i * 40 + Framework.genRand(-8, 8),
                                startingY + j * 40 + Framework.genRand(-6, 6), 0);
                        NPC.SetPos(pos);
                        NPC.getAI().setTeam(team);
                        NPC.getAI().setRunning(true);
                        NPC.getAI().setDestX(256 - i * 40);
                        NPC.getAI().setDestY((int) NPC.GetPos().y);
                    }
                }
            }
        }
    }

    public void spawnStatMg() {
        long curTime = Framework.CurGameTime() * Framework.milisecInNanosec;
        if (getStatMgTimeout() <= curTime && getStatMg() > 0) {
            setStatMg(getStatMg() - 1);
            if (team == 0) {
                setStatMgTimeout(curTime + 70 * Framework.secInNanosec);
            }
            int startingY = 0;
            if (team == 0) {
                startingY = Math.max(
                        128,
                        Math.min(Framework.activeGame().getGameFramework().getFrameHeight() / 2
                                + Framework.activeGame().getPlayerCamera().getY(), Framework.activeGame().getMapSizeY() - 200));
            } else {
                startingY = Framework.genRand(128, Framework.activeGame().getMapSizeY() - 200);

            }

            if (team == 0) {
                Human NPC = new Human("stat_mg_uk", "weapon_stat_mg_vickers");
                Ents.addNewEntityInGame(NPC);
                VectorLight pos = new VectorLight(32, startingY, 0);
                NPC.SetPos(pos);
                NPC.getAI().setTeam(team);
                NPC.getAI().setRunning(true);
                NPC.getAI().setDestX(1024);
                NPC.getAI().setDestY((int) NPC.GetPos().y);
            } else {
                Human NPC = new Human("stat_mg_ger", "weapon_stat_mg_vickers");
                Ents.addNewEntityInGame(NPC);
                VectorLight pos = new VectorLight(Framework.activeGame().getMapSizeX() - 32, startingY, 0);
                NPC.SetPos(pos);
                NPC.getAI().setTeam(team);
                NPC.getAI().setRunning(true);
                NPC.getAI().setDestX(256);
                NPC.getAI().setDestY((int) NPC.GetPos().y);
            }
        }
    }

    public void spawnMortar() {
        long curTime = Framework.CurGameTime() * Framework.milisecInNanosec;
        if (getMortarTimeout() <= curTime && getMortar() > 0) {
            setMortar(getMortar() - 1);
            if (team == 0) {
                setMortarTimeout(curTime + 70 * Framework.secInNanosec);
            }

            int startingY = 0;
            if (team == 0) {
                startingY = Math.max(
                        128,
                        Math.min(Framework.activeGame().getGameFramework().getFrameHeight() / 2
                                + Framework.activeGame().getPlayerCamera().getY(), Framework.activeGame().getMapSizeY() - 200));
            } else {
                startingY = Framework.genRand(128, Framework.activeGame().getMapSizeY() - 200);

            }

            if (team == 0) {
                Human NPC = new Human("mortar_uk", "weapon_mortar_uk");
                Ents.addNewEntityInGame(NPC);
                VectorLight pos = new VectorLight(32, startingY, 0);
                NPC.SetPos(pos);
                NPC.getAI().setTeam(team);
                NPC.getAI().setRunning(true);
                NPC.getAI().setDestX(1024);
                NPC.getAI().setDestY((int) NPC.GetPos().y);
            } else {
                Human NPC = new Human("mortar_ger", "weapon_mortar_ger");
                Ents.addNewEntityInGame(NPC);
                VectorLight pos = new VectorLight(Framework.activeGame().getMapSizeX() - 32, startingY, 0);
                NPC.SetPos(pos);
                NPC.getAI().setTeam(team);
                NPC.getAI().setRunning(true);
                NPC.getAI().setDestX(256);
                NPC.getAI().setDestY((int) NPC.GetPos().y);
            }
        }
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }
}
