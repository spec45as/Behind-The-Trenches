package gameframework;

import gameframework.entities.Entity;
import gameframework.entities.Flag;
import gameframework.entities.Human;
import gameframework.entities.PlayerSpectator;
import gameframework.libs.Animation;
import gameframework.libs.Ents;
import gameframework.libs.GuiCanvasSelection;
import gameframework.libs.SoundPlayer;
import gameframework.libs.Team;
import gameframework.libs.ai.AiTargetingMap;
import gameframework.libs.other.BitMap;
import gameframework.libs.other.Resources;
import gameframework.libs.other.VectorLight;
import gameframework.libs.other.mapSprite;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DemoBattle extends Gamemode {
    private SoundPlayer soundPlayer;
    private Framework gameFramework;

    public Framework getGameFramework() {
        return this.gameFramework;
    }

    public SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }

    private ArrayList<Entity> allEnts;
    private ArrayList<Animation> allParticles;
    private ArrayList<mapSprite> mapSprites;
    private AiTargetingMap aiTargetingMap;

    public ArrayList<mapSprite> getMapSpritesArray() {
        return mapSprites;
    }

    public static final int DIRT_SPRITE_SIZE = 32;
    public static final int SPRITE_SIZE = 32;
    private final int mapSizeX = 2048;
    private final int mapSizeY = 2048;

    private final int playerZoneX = mapSizeX;
    private final int playerZoneY = mapSizeY;

    public int getPlayerZoneX() {
        return playerZoneX;
    }

    public int getPlayerZoneY() {
        return playerZoneY;
    }

    private Font font;
    private Resources gameResources;

    public Resources getGameResources() {
        return gameResources;
    }

    public void setGameResources(Resources gameResources) {
        this.gameResources = gameResources;
    }

    private mapSprite[][] dirtSpriteMap = new mapSprite[mapSizeX / DIRT_SPRITE_SIZE - 1][mapSizeY / DIRT_SPRITE_SIZE - 1];
    private BitMap grassBitMap;
    private BitMap trenchesBitMap;

    public static String getCountryID(int index) {
        switch (index) {
        case 0:
            return "fr";
        case 1:
            return "ger";
        case 2:
            return "aus_hun";
        case 3:
            return "rus";
        }
        return "none";
    }

    public BitMap gettrenchesBitMap() {
        return trenchesBitMap;
    }

    public int getMapSizeX() {
        return mapSizeX;
    }

    public int getMapSizeY() {
        return mapSizeY;
    }

    public Point getCustomMouseCursorPos() {
        return new Point((int) (MouseInfo.getPointerInfo().getLocation().getX() - gameFramework.getLocationOnScreen().getX()),
                (int) (MouseInfo.getPointerInfo().getLocation().getY() - gameFramework.getLocationOnScreen().getY()));
    }

    public ArrayList<Entity> getEntsArray() {
        return allEnts;
    }

    private PlayerSpectator playerCamera;

    public PlayerSpectator getPlayerCamera() {
        return playerCamera;
    }

    public GuiCanvasSelection getActiveSelection() {
        return null;
    }

    public void setActiveSelection(GuiCanvasSelection activeSelection) {
        ;
    }

    public DemoBattle(Framework framework) {
        gameFramework = framework;
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread() {
            @Override
            public void run() {
                Initialize();
                LoadContent();
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };

        threadForInitGame.start();

    }

    private int genRand(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    public Team team1 = new Team(0, "ger");
    public Team team2 = new Team(1, "fr");

    private void Initialize() {
        playerCamera = new PlayerSpectator(1024 - getGameFramework().getFrameWidth() / 2, 384 - 10, getGameFramework());
        gameResources = new Resources();
        allEnts = new ArrayList<Entity>();
        allParticles = new ArrayList<Animation>();
        mapSprites = new ArrayList<mapSprite>();
        soundPlayer = new SoundPlayer();
        aiTargetingMap = new AiTargetingMap(512);

        grassBitMap = new BitMap(mapSizeX / SPRITE_SIZE - 1, mapSizeY / SPRITE_SIZE - 1, SPRITE_SIZE, gameResources.mapTexturesGrassCover,
                getGameFramework());
        grassBitMap.generateGrass();

        trenchesBitMap = new BitMap(mapSizeX / SPRITE_SIZE - 1, mapSizeY / SPRITE_SIZE - 1, SPRITE_SIZE, gameResources.mapTrenchesCover,
                getGameFramework());
        trenchesBitMap.generateTrenches();

        for (int i = 0; i < dirtSpriteMap.length; i++) {
            for (int j = 0; j < dirtSpriteMap[0].length; j++) {
                mapSprite sprite = new mapSprite(i * DIRT_SPRITE_SIZE, j * DIRT_SPRITE_SIZE, gameResources.mapDirt, false);
                dirtSpriteMap[i][j] = sprite;
            }
        }

        for (int j = 0; j < mapSizeY - 128; j++) {
            for (int i = 0; i < mapSizeX - 128; i++) {
                if (Math.random() > 0.999975) {
                    if (Math.random() > 0.5) {
                        mapSprite sprite = new mapSprite(i, j, gameResources.artExplosions.get(genRand(0, 2)), false);
                        mapSprites.add(sprite);
                    } else {
                        mapSprite sprite = new mapSprite(i, j, gameResources.brokenTrees.get(genRand(0, 2)), false);
                        mapSprites.add(sprite);
                    }
                }
            }
        }

        setUpFlags(0, 1);
        soundPlayer.playRandomMenuSound();

        font = new Font("monospaced", Font.BOLD, 12);
    }

    private ArrayList<Flag> mapFlags = new ArrayList<Flag>();

    public ArrayList<Flag> getMapFlags() {
        return mapFlags;
    }

    public void setMapFlags(ArrayList<Flag> mapFlags) {
        this.mapFlags = mapFlags;
    }

    public boolean isPlayerPrepearing = true;
    private long nextWave = 0;
    private long wavesDelay = 30l * 1000000000l;

    private void setUpFlags(int side1, int side2) {
        int y = getMapSizeY() / 4;
        for (int i = 1; i <= 2; i++) {
            Flag side1Flag = new Flag("none", -1);
            side1Flag.SetPos(new VectorLight(1024, i * y, 0));
            Ents.addNewEntityInGame(side1Flag);
            mapFlags.add(side1Flag);
        }
    }

    private long nextScoreUpdate = 0;

    public void updatePlayerPoints() {
        if (Framework.CurGameTime() > nextScoreUpdate) {
            team1.setFlags(0);
            team2.setFlags(0);
            for (Flag curFlag : getMapFlags()) {
                if (curFlag.team == 0) {
                    team1.setFlags(team1.getFlags() + 1);
                } else if (curFlag.team == 1) {
                    team2.setFlags(team2.getFlags() + 1);
                }
            }
            if (team1.getFlags() > team2.getFlags()) {
                team1.setGamePoints(team1.getGamePoints() + 1 * (team1.getFlags() - team2.getFlags()));
            } else if (team1.getFlags() < team2.getFlags()) {
                team2.setGamePoints(team2.getGamePoints() + 1 * (team2.getFlags() - team1.getFlags()));
            }

            nextScoreUpdate = Framework.CurGameTime() + 1000;

        }
    }

    private long nextAiLogicUpdate = 0;

    public void updateAiLogic() {
        if (team1.getGamePoints() >= 200 || team2.getGamePoints() >= 200) {
            return;
        }

        if (Framework.CurGameTime() > nextAiLogicUpdate) {
            if (Framework.CurGameTime() > nextWave) {
                if (isPlayerPrepearing) {
                    isPlayerPrepearing = false;
                }

                nextWave = wavesDelay / 1000000 + Framework.CurGameTime();
                int startingY = 0;
                startingY = Math.max(Framework.activeGame().getPlayerCamera().getY() + 64, Math.min((int) (100 * Math.random())
                        + Framework.activeGame().getPlayerCamera().getY(), Framework.activeGame().getMapSizeY() - 600));

                for (int k = 1; k <= Framework.genRand(1, 1); k++) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 15; j++) {
                            Human NPC = new Human("rifleman_ger", "weapon_uk_enfield");
                            Ents.addNewEntityInGame(NPC);
                            VectorLight pos = new VectorLight(
                                    Framework.activeGame().getMapSizeX() - 32 - i * 64 + Framework.genRand(-8, 8), startingY + j * 48
                                            + Framework.genRand(-8, 8), 0);
                            NPC.SetPos(pos);
                            NPC.getAI().setTeam(1);
                            if (Math.random() > 0.5) {
                                NPC.getAI().setWeaponBehavior("melee_only");
                            }
                            NPC.getAI().setRunning(true);
                            NPC.getAI().setDestX(256 - i * 64 + Framework.genRand(-64, 64));
                            NPC.getAI().setDestY((int) NPC.GetPos().y);
                        }
                    }
                }
                for (int k = 1; k <= Framework.genRand(0, 1); k++) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 10; j++) {
                            Human NPC;
                            if (i == 2 && (j == 0 || j == 4 || j == 9)) {
                                NPC = new Human("hand_mg_ger", "weapon_hand_mg_uk_lewis");
                            } else {
                                NPC = new Human("rifleman_ger", "weapon_uk_enfield");
                            }
                            Ents.addNewEntityInGame(NPC);
                            VectorLight pos = new VectorLight(
                                    Framework.activeGame().getMapSizeX() - 32 - i * 40 + Framework.genRand(-8, 8), startingY + j * 40
                                            + Framework.genRand(-6, 6), 0);
                            NPC.SetPos(pos);

                            NPC.getAI().setTeam(1);
                            NPC.getAI().setRunning(true);
                            NPC.getAI().setDestX(256 - i * 40 + Framework.genRand(-64, 64));
                            NPC.getAI().setDestY((int) NPC.GetPos().y);
                        }
                    }
                }
                for (int k = 1; k <= Framework.genRand(0, 2); k++) {
                    team2.spawnMortar();
                }
                for (int k = 1; k <= Framework.genRand(0, 2); k++) {
                    team2.spawnStatMg();
                }

                for (int k = 1; k <= Framework.genRand(1, 1); k++) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 15; j++) {
                            Human NPC = new Human("rifleman_uk", "weapon_uk_enfield");
                            Ents.addNewEntityInGame(NPC);
                            VectorLight pos = new VectorLight(128 + i * 48 + Framework.genRand(-8, 8), startingY + j * 40
                                    + Framework.genRand(-8, 8), 0);
                            NPC.SetPos(pos);
                            if (Math.random() > 0.5) {
                                NPC.getAI().setWeaponBehavior("melee_only");
                            }
                            NPC.getAI().setTeam(0);
                            NPC.getAI().setRunning(true);
                            NPC.getAI().setDestX(1024 + i * 32 + Framework.genRand(-64, 64));
                            NPC.getAI().setDestY((int) NPC.GetPos().y);
                        }
                    }
                }
                for (int k = 1; k <= Framework.genRand(0, 1); k++) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 10; j++) {
                            Human NPC;
                            VectorLight pos = new VectorLight(32 + i * 40, startingY + j * 40, 0);
                            if (i == 2 && (j == 0 || j == 4 || j == 9)) {
                                NPC = new Human("hand_mg_uk", "weapon_hand_mg_uk_lewis");
                                NPC.getAI().setRunning(false);
                                pos = new VectorLight(128 + i * 40, startingY + j * 40, 0);
                            } else {
                                NPC = new Human("rifleman_uk", "weapon_uk_enfield");
                            }
                            Ents.addNewEntityInGame(NPC);
                            NPC.SetPos(pos);
                            NPC.getAI().setRunning(true);
                            NPC.getAI().setTeam(0);
                            NPC.getAI().setDestX(1024 + i * 40 + Framework.genRand(-64, 64));
                            NPC.getAI().setDestY((int) NPC.GetPos().y);
                        }
                    }
                }
                for (int k = 1; k <= Framework.genRand(0, 2); k++) {
                    team1.spawnMortar();
                }
                for (int k = 1; k <= Framework.genRand(0, 2); k++) {
                    team1.spawnStatMg();
                }

            }
            nextAiLogicUpdate = Framework.CurGameTime() + 1000;
        }
    }

    public void particleCreate(Animation particle) {
        this.allParticles.add(particle);
    }

    public void particleRemove(Animation particle) {
        this.allParticles.remove(particle);
    }

    public void entCreate(Entity newEnt) {
        this.allEnts.add(newEnt);
    }

    public void entRemove(Entity ent) {
        this.allEnts.remove(ent);
    }

    private void LoadContent() {
    }

    public void RestartGame() {
        allEnts.clear();
    }

    long nextSort = 0;
    long nextAiUpdate = 0;

    public void UpdateGame(long gameTime, Point mousePosition) {

        if (Framework.CurGameTime() > nextSort) {
            sortEnts();
            nextSort = Framework.CurGameTime() + 150;
        }

        updateEnts();
        updateParticles();
        updatePlayerPoints();
        updateAiLogic();
        isPlayerShooting(gameTime, mousePosition);

    }

    private void sortEnts() {
        for (int i = 0; i < getEntsArray().size(); i++) {
            for (int j = 0; j < getEntsArray().size() - 1; j++) {
                if (getEntsArray().get(j).GetPos().y > getEntsArray().get(j + 1).GetPos().y) {
                    Entity temp = getEntsArray().get(j + 1);
                    getEntsArray().set(j + 1, getEntsArray().get(j));
                    getEntsArray().set(j, temp);
                }
            }
        }
    }

    public void Draw(Graphics2D g2d, Point mousePosition, long gameTime) {
        playerCamera.Update(getCustomMouseCursorPos());

        if (mousePosition == null) {
            mousePosition = new Point(1, 1);
        }

        g2d.translate(-playerCamera.getX(), -playerCamera.getY());

        int playerCamX = playerCamera.getX();
        int playerCamY = playerCamera.getY();
        int[] camPos = { playerCamX, playerCamY };

        int minDirtSpriteXnumber = Math.max(playerCamX / DIRT_SPRITE_SIZE, 0);
        int minDirtSpriteYnumber = Math.max(playerCamY / DIRT_SPRITE_SIZE, 0);
        int maxDirtSpriteXnumber = Math.min(DIRT_SPRITE_SIZE + (playerCamX + getGameFramework().getFrameWidth()) / DIRT_SPRITE_SIZE,
                dirtSpriteMap.length);
        int maxDirtSpriteYnumber = Math.min(DIRT_SPRITE_SIZE + (playerCamY + getGameFramework().getFrameHeight()) / DIRT_SPRITE_SIZE,
                dirtSpriteMap[0].length);

        for (int i = minDirtSpriteXnumber; i < maxDirtSpriteXnumber; i++) {
            for (int j = minDirtSpriteYnumber; j < maxDirtSpriteYnumber; j++) {
                dirtSpriteMap[i][j].Draw(g2d);
            }
        }

        grassBitMap.Draw(g2d, camPos);
        trenchesBitMap.Draw(g2d, camPos);

        for (int i = 0; i < mapSprites.size(); i++) {
            mapSprites.get(i).Draw(g2d);
        }

        for (int i = 0; i < allEnts.size(); i++) {
            allEnts.get(i).Draw(g2d);
        }

        for (int i = 0; i < allParticles.size(); i++) {
            allParticles.get(i).Draw(g2d);
        }

        g2d.setFont(font);
        g2d.setColor(Color.WHITE);

        getGameFramework().setCursor(gameResources.normalCursor);

        g2d.translate(playerCamera.getX(), playerCamera.getY());
        DrawGUI(g2d, mousePosition, gameTime);
    }

    private int selectedButton = -1;

    public void DrawGUI(Graphics2D g2d, Point mousePosition, long gameTime) {
        BufferedImage mainMenu = gameResources.getTexture("fx/main_menu.png");
        int menuX1 = getGameFramework().getWidth() / 2 - mainMenu.getWidth() / 2;
        int menuY1 = getGameFramework().getHeight() / 2 - mainMenu.getHeight() / 2;
        g2d.drawImage(mainMenu, menuX1, menuY1, mainMenu.getWidth(), mainMenu.getHeight(), null);

        BufferedImage menuButton = gameResources.getTexture("main_menu_buttons/company.png");

        for (int i = 0; i < 6; i++) {
            if (i == 0) {
                menuButton = gameResources.getTexture("main_menu_buttons/company.png");
            } else if (i == 1) {
                menuButton = gameResources.getTexture("main_menu_buttons/battle_mode.png");
            } else if (i == 2) {
                menuButton = gameResources.getTexture("main_menu_buttons/multiplayer.png");
                continue;
            } else if (i == 3) {
                menuButton = gameResources.getTexture("main_menu_buttons/tutorial.png");
                continue;
            } else if (i == 4) {
                menuButton = gameResources.getTexture("main_menu_buttons/settings.png");
                continue;
            } else if (i == 5) {
                menuButton = gameResources.getTexture("main_menu_buttons/exit.png");
            }

            if (selectedButton == i) {
                continue;
            }
            g2d.drawImage(menuButton, menuX1 + 310, menuY1 + 146 + (i * 8) + i * menuButton.getHeight(), menuButton.getWidth(),
                    menuButton.getHeight(), null);

        }

    }

    private long menuDelay = 0;

    public boolean guiAction(Point mousePosition) {
        if (mousePosition == null) {
            return false;
        }

        if (menuDelay >= Framework.CurGameTime()) {
            return true;
        }

        BufferedImage mainMenu = gameResources.getTexture("fx/main_menu.png");
        int menuX1 = getGameFramework().getWidth() / 2 - mainMenu.getWidth() / 2;
        int menuY1 = getGameFramework().getHeight() / 2 - mainMenu.getHeight() / 2;
        BufferedImage menuButton = gameResources.getTexture("main_menu_buttons/company.png");

        for (int i = 0; i < 6; i++) {

            if ((mousePosition.x > menuX1 + 310 && mousePosition.x < menuX1 + 310 + menuButton.getWidth())
                    && (mousePosition.y > (menuY1 + 146 + (i * 8) + i * menuButton.getHeight()) && (mousePosition.y < menuY1 + 146
                            + (i * 8) + i * menuButton.getHeight() + menuButton.getHeight()))) {
                if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {

                    if (i == 0) {
                        soundPlayer.stopMenuSound();
                        Framework.gameState = Framework.GameState.MAIN_MENU_STRATEGY;
                    } else if (i == 1) {
                        soundPlayer.stopMenuSound();
                        Framework.gameState = Framework.GameState.MAIN_MENU;
                    } else if (i == 2) {
                        return true;
                    } else if (i == 3) {
                        return true;
                    } else if (i == 4) {
                        return true;
                    } else if (i == 5) {
                        System.exit(0);
                    }
                }

                if (selectedButton == i) {
                    return true;
                }
                if (i != 2 && i != 3 && i != 4) {
                    soundPlayer.playMenuAction();
                }
                selectedButton = i;

                // menuDelay = Framework.CurGameTime() + 50;

                return true;
            }
        }
        selectedButton = -1;

        if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {

        }

        return false;

    }

    private void isPlayerShooting(long gameTime, Point mousePosition) {

        if (guiAction(mousePosition)) {
            return;
        }

        if (mousePosition == null) {
            return;
        }

        if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) { // ЛКМ

        } else if (Canvas.mouseButtonState(MouseEvent.BUTTON3)) { // ПКМ

        } else if (!Canvas.mouseButtonState(MouseEvent.BUTTON1)) {

        }
    }

    private void updateEnts() {
        for (int i = 0; i < allEnts.size(); i++) {
            Entity ent = allEnts.get(i);
            if (!ent.isValid) {
                entRemove(ent);
                continue;
            }
            ent.Update();
        }
    }

    private void updateParticles() {
        for (int i = 0; i < allParticles.size(); i++) {
            if (!allParticles.get(i).active)
                allParticles.remove(i);
        }
    }

    public AiTargetingMap getAiTargetingMap() {
        return aiTargetingMap;
    }

    public void setAiTargetingMap(AiTargetingMap aiTargetingMap) {
        this.aiTargetingMap = aiTargetingMap;
    }

}
