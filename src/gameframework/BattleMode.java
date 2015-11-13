package gameframework;

import gameframework.entities.Entity;
import gameframework.entities.Flag;
import gameframework.entities.Human;
import gameframework.entities.PlayerSpectator;
import gameframework.libs.Animation;
import gameframework.libs.Ents;
import gameframework.libs.GuiCanvasSelection;
import gameframework.libs.Shooting;
import gameframework.libs.SoundPlayer;
import gameframework.libs.Team;
import gameframework.libs.ai.AiTargetingMap;
import gameframework.libs.other.BitMap;
import gameframework.libs.other.Resources;
import gameframework.libs.other.Time;
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
import java.util.Random;

import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.SpatialIndex;
import com.infomatiq.jsi.rtree.RTree;

public class BattleMode extends Gamemode {
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
    private SpatialIndex siTeam1 = new RTree();
    
    public SpatialIndex getSiTeam1() {
        return siTeam1;
    }

    public SpatialIndex getSiTeam2() {
        return siTeam2;
    }

    private SpatialIndex siTeam2 = new RTree();

    public ArrayList<mapSprite> getMapSpritesArray() {
        return mapSprites;
    }

    public static final int DIRT_SPRITE_SIZE = 32;
    public static final int SPRITE_SIZE = 32;
    private final int mapSizeX = 2048;
    private final int mapSizeY = 2048;

    private final int playerZoneX = 2048;
    private final int playerZoneY = 2048;

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
        return activeSelection;
    }

    public void setActiveSelection(GuiCanvasSelection activeSelection) {
        this.activeSelection = activeSelection;
    }

    private GuiCanvasSelection activeSelection;
    private GuiCanvasSelection newSelection;

    public BattleMode(Framework framework) {
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

    public Team team1 = new Team(0, "fr");
    public Team team2 = new Team(1, "ger");

    private void Initialize() {
        playerCamera = new PlayerSpectator(0, 0, getGameFramework());
        gameResources = new Resources();
        allEnts = new ArrayList<Entity>();
        allParticles = new ArrayList<Animation>();
        mapSprites = new ArrayList<mapSprite>();
        soundPlayer = new SoundPlayer();
        aiTargetingMap = new AiTargetingMap(512);

        int minimapSizeX = 260;
        int minimapSizeY = 214;
        minimapScaleX = (float) minimapSizeX / (float) getMapSizeX();
        minimapScaleY = (float) minimapSizeY / (float) getMapSizeY();

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

        siTeam1.init(null);
        siTeam2.init(null);

        setUpFlags(0, 1);
        font = new Font("monospaced", Font.BOLD, 12);
    }

    private ArrayList<Flag> mapFlags = new ArrayList<Flag>();

    public ArrayList<Flag> getMapFlags() {
        return mapFlags;
    }

    public void setMapFlags(ArrayList<Flag> mapFlags) {
        this.mapFlags = mapFlags;
    }

    private long timeToPrepare = Framework.CurGameTime() + 35l * 1000l;
    public boolean isPlayerPrepearing = true;
    private long timeLeft;
    private int waves = 10;
    private long nextWave = timeToPrepare;
    private long wavesDelay = 0;

    private void setUpFlags(int side1, int side2) {
        this.timeLeft = 630l * 1000000000l;
        wavesDelay = timeLeft / waves;

        int y = getMapSizeY() / 4;
        for (int i = 1; i <= 3; i++) {
            Flag side1Flag = new Flag(getCountryID(side1), side1);
            side1Flag.SetPos(new VectorLight(1024, i * y, 0));
            Ents.addNewEntityInGame(side1Flag);
            mapFlags.add(side1Flag);
            Flag side2Flag = new Flag(getCountryID(side2), side2);
            side2Flag.SetPos(new VectorLight(getMapSizeX() - 1024, i * y, 0));
            Ents.addNewEntityInGame(side2Flag);
            mapFlags.add(side2Flag);
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

        if (Framework.CurGameTime() > nextAiLogicUpdate) {
            if (Framework.CurGameTime() > nextWave) {

                if (isPlayerPrepearing) {
                    if (Framework.CurGameTime() > timeToPrepare) {
                        isPlayerPrepearing = false;
                    }
                }

                nextWave = wavesDelay / 1000000 + Framework.CurGameTime();
                for (int i = 1; i <= Framework.genRand(1, 2); i++) {
                    team2.spawnInfSquad();
                }
                for (int i = 1; i <= Framework.genRand(1, 2); i++) {
                    team2.spawnHandMgSquad();
                }
                for (int i = 1; i <= Framework.genRand(1, 3); i++) {
                    team2.spawnMortar();
                }
                for (int i = 1; i <= Framework.genRand(1, 3); i++) {
                    team2.spawnStatMg();
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

    private void addRTreeObjects() {
        siTeam1 = new RTree();
        siTeam2 = new RTree();

        siTeam1.init(null);
        siTeam2.init(null);

        int i = 0;
        for (Entity curEnt : getEntsArray()) {
            if (curEnt.getClass().getSimpleName().equals("Human")) {
                if (((Human) curEnt).getAI().getTeam() == 0) {
                    siTeam1.add(new Rectangle(curEnt.GetPos().x, curEnt.GetPos().y, 2, 2), i++);
                } else {
                    siTeam2.add(new Rectangle(curEnt.GetPos().x, curEnt.GetPos().y, 2, 2), i++);
                }
            }
        }
    }

    long nextSort = 0;
    long nextAiUpdate = 0;

    public void UpdateGame(long gameTime, Point mousePosition) {
        // System.out.println(isPlayerPrepearing);
        if (Framework.CurGameTime() > nextSort) {
            sortEnts();
            nextSort = Framework.CurGameTime() + 150;
        }
        /*
         * if (Framework.CurGameTime() > nextAiUpdate) {
         * aiTargetingMap.updateSectors(); nextAiUpdate =
         * Framework.CurGameTime() + 9999; }
         */

        if (Framework.CurGameTime() > nextAiUpdate) {
            addRTreeObjects();
            nextAiUpdate = Framework.CurGameTime() + 400;
        }

        isPlayerShooting(gameTime, mousePosition);
        updateEnts();
        updateParticles();
        updatePlayerPoints();
        updateAiLogic();

        if (timeLeft - gameTime <= 0) {
            if (team1.getGamePoints() < team2.getGamePoints()) {
                Framework.gameState = Framework.GameState.GAMEOVER;
                return;
            } else if (team1.getGamePoints() > team2.getGamePoints()) {
                Framework.gameState = Framework.GameState.WIN;
                return;
            }
            if (team1.getGamePoints() == team2.getGamePoints()) {
                Framework.gameState = Framework.GameState.GAMEOVER;
                return;
            }
        }
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
        BufferedImage uiImage = gameResources.getTexture("fx/command_box.png");
        int boxX = getGameFramework().getFrameWidth() - uiImage.getWidth();
        int boxY = getGameFramework().getFrameHeight() - uiImage.getHeight();

        boolean mouseOnGui = false;
        if (((mousePosition.x > boxX && mousePosition.x < getGameFramework().getFrameWidth()) && (mousePosition.y > boxY && mousePosition.y < getGameFramework()
                .getFrameHeight()))) {
            mouseOnGui = true;
        }

        if (!buyMenuOpened) {
            if ((mousePosition.x > (boxX + 66) && mousePosition.x < (boxX + 66 + 126))
                    && (mousePosition.y > (boxY - 24) && mousePosition.y < (boxY))) {
                mouseOnGui = true;
            }
        } else {
            BufferedImage buyMenu = gameResources.getTexture("fx/buy_menu_opened.png");
            if ((mousePosition.x > (boxX + 66) && mousePosition.x < (boxX + 66 + 126))
                    && (mousePosition.y > (boxY - buyMenu.getHeight() + 120) && mousePosition.y < boxY)) {
                mouseOnGui = true;
            }
        }

        BufferedImage minimap = gameResources.getTexture("fx/minimap_canvas.png");
        int minimapBoxX = 0;
        int minimapBoxY = getGameFramework().getFrameHeight() - minimap.getHeight();
        int mapX1 = 16;
        int mapY1 = 16;
        int miniMapSizeX = 260;
        int miniMapSizeY = 214;
        if ((mousePosition.x > (minimapBoxX + mapX1) && mousePosition.x < (minimapBoxX + mapX1 + miniMapSizeX))
                && (mousePosition.y > (minimapBoxY + mapY1) && mousePosition.y < (minimapBoxY + mapY1 + miniMapSizeY))) {
            mouseOnGui = true;
        }

        if (!mouseOnGui) {
            if ((activeSelection != null && activeSelection.getSelectedEnts().size() > 0)
                    && (Shooting.getShootingTargetFromLocalMousePos(mousePosition) != null)) {
                getGameFramework().setCursor(gameResources.crosshairCursor);
            } else if ((activeSelection != null && activeSelection.getSelectedEnts().size() > 0)
                    && trenchesBitMap.calculateHeight(mousePosition.getX() + playerCamX, mousePosition.getY() + playerCamY) <= -32) {
                getGameFramework().setCursor(gameResources.noCursor);
                BufferedImage getInImage = gameResources.getTexture("fx/take_cover.png");
                g2d.drawImage(getInImage, getCustomMouseCursorPos().x + playerCamX - 14, getCustomMouseCursorPos().y + playerCamY + 2, null);
            } else {
                getGameFramework().setCursor(gameResources.normalCursor);
            }
        } else {
            getGameFramework().setCursor(gameResources.normalCursor);
        }

        if (newSelection != null) {
            newSelection.Draw(g2d);
        }

        if (activeSelection != null) {
            for (Entity curTarget : activeSelection.getSelectedEnts()) {
                if (curTarget.getClass().getSimpleName().equals("Human")) {
                    ((Human) curTarget).DrawSelection(g2d);
                }
            }
        }

        g2d.translate(playerCamera.getX(), playerCamera.getY());
        DrawGUI(g2d, mousePosition, gameTime);
    }

    boolean actionIcons[][] = new boolean[3][3];
    boolean buyMenuOpened = false;

    private long menuUpdateDelay = 0;

    private float minimapScaleX = 1;
    private float minimapScaleY = 1;

    public void DrawMiniMap(Graphics2D g2d, Point mousePosition, long gameTime) {
        BufferedImage minimap = gameResources.getTexture("fx/minimap_canvas.png");

        int boxX = 0;
        int boxY = getGameFramework().getFrameHeight() - minimap.getHeight();

        int mapX1 = 16;
        int mapY1 = 16;

        int mapSizeX = 260;
        int mapSizeY = 214;

        g2d.drawImage(minimap, boxX, boxY, minimap.getWidth(), minimap.getHeight(), null);

        g2d.setColor(Color.WHITE);
        try {
            for (Entity curEnt : getEntsArray()) {
                int sizeX = 1;
                int sizeY = 1;
                int posX = 1;
                int posY = 1;
                Color color = Color.WHITE;

                if (curEnt.getClass().getSimpleName().equals("Human")) {
                    if (((Human) curEnt).getAI().getTeam() == 0) {
                        color = Color.GREEN;
                    } else {
                        color = Color.RED;
                    }
                    if (((Human) curEnt).getHumanID().contains("mortar") || ((Human) curEnt).getHumanID().contains("stat_mg")) {
                        sizeX = 3;
                        sizeY = 2;
                    }

                } else if (curEnt.getClass().getSimpleName().equals("Flag")) {
                    if (((Flag) curEnt).team == 0) {
                        color = Color.GREEN;
                    } else {
                        color = Color.RED;
                    }
                    sizeX = 3;
                    sizeY = 3;
                }
                posX = (int) (curEnt.GetPos().x * minimapScaleX);
                posY = (int) (curEnt.GetPos().y * minimapScaleY);

                posX = Math.max(0, Math.min(posX, mapSizeX));
                posY = Math.max(0, Math.min(posY, mapSizeY));
                g2d.setColor(color);
                g2d.drawRect(boxX + mapX1 + posX, boxY + mapY1 + posY, sizeX, sizeY);
            }
        } catch (Exception e) {
        }
        g2d.setColor(Color.white);

        g2d.drawRect(boxX + mapX1 + (int) (getPlayerCamera().getX() * minimapScaleX), boxY + mapY1
                + (int) (getPlayerCamera().getY() * minimapScaleY), (int) (gameFramework.getWidth() * minimapScaleX),
                (int) (gameFramework.getHeight() * minimapScaleY));

    }

    public void DrawGUI(Graphics2D g2d, Point mousePosition, long gameTime) {
        DrawMiniMap(g2d, mousePosition, gameTime);
        BufferedImage panelTop = gameResources.getTexture("fx/panel_top.png");
        BufferedImage buyMenu = gameResources.getTexture("fx/buy_menu_closed.png");
        BufferedImage uiImage = gameResources.getTexture("fx/command_box.png");

        int frameWidth = getGameFramework().getFrameWidth();
        int frameHeight = getGameFramework().getFrameHeight();

        int boxX = frameWidth - uiImage.getWidth();
        int boxY = frameHeight - uiImage.getHeight();

        g2d.drawImage(panelTop, (frameWidth - panelTop.getWidth()) / 2, 0, panelTop.getWidth(), panelTop.getHeight(), null);
        g2d.drawImage(uiImage, boxX, frameHeight, frameWidth, boxY, 0, 0, uiImage.getWidth(), uiImage.getHeight(), null);
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Следующая атака: " + Time.formatTime(Math.abs(nextWave * 1000000l - gameTime)), 14, 14);
        g2d.drawString("Счет (за захват флагов) " + team1.getGamePoints() + ":" + team2.getGamePoints(), 14, 30);

        if (isPlayerPrepearing) {
            g2d.setColor(Color.ORANGE);
            g2d.drawString("Осталось времени на подготовку позиций: " + Time.formatTime(Math.abs(nextWave * 1000000l - gameTime)),
                    gameFramework.getFrameWidth() / 2 - 160, 70);
            g2d.setColor(Color.WHITE);

        }

        g2d.drawString(Time.formatTime(timeLeft - gameTime), gameFramework.getFrameWidth() / 2 - 25, 18);
        g2d.drawString("Франция, Флаги: " + team1.getFlags() + "x | Германская империя, Флаги: " + team2.getFlags() + "x",
                gameFramework.getFrameWidth() / 2 - 180, 34);

        if (!buyMenuOpened) {
            g2d.drawImage(buyMenu, boxX, boxY - buyMenu.getHeight(), frameWidth, boxY, 0, 0, buyMenu.getWidth(), buyMenu.getHeight(), null);
        } else {
            buyMenu = gameResources.getTexture("fx/buy_menu_opened.png");
            g2d.drawImage(buyMenu, boxX, boxY - buyMenu.getHeight(), frameWidth, boxY, 0, 0, buyMenu.getWidth(), buyMenu.getHeight(), null);

            BufferedImage buyButton = gameResources.getTexture("fx/buyicon_mortar.png");
            long timeout = 0;
            long curTime = Framework.CurGameTime() * Framework.milisecInNanosec;
            int count = 0;
            for (int i = 0; i < 4; i++) {
                if (i == 0) {
                    if (team1.getMortarTimeout() <= curTime && team1.getMortar() > 0) {
                        buyButton = gameResources.getTexture("fx/buyicon_mortar.png");
                    } else {
                        buyButton = gameResources.getTexture("fx/buyicon_empty.png");
                    }
                    timeout = Math.max(0, team1.getMortarTimeout() - Framework.CurGameTime() * Framework.milisecInNanosec);
                    count = team1.getMortar();
                } else if (i == 1) {
                    if (team1.getStatMgTimeout() <= curTime && team1.getStatMg() > 0) {
                        buyButton = gameResources.getTexture("fx/buyicon_stat_mg.png");
                    } else {
                        buyButton = gameResources.getTexture("fx/buyicon_empty.png");
                    }
                    timeout = Math.max(0, team1.getStatMgTimeout() - Framework.CurGameTime() * Framework.milisecInNanosec);
                    count = team1.getStatMg();
                } else if (i == 2) {
                    if (team1.getHandMgTimeout() <= curTime && team1.getHandMg() > 0) {
                        buyButton = gameResources.getTexture("fx/buyicon_hand_mg.png");
                    } else {
                        buyButton = gameResources.getTexture("fx/buyicon_empty.png");
                    }
                    timeout = Math.max(0, team1.getHandMgTimeout() - Framework.CurGameTime() * Framework.milisecInNanosec);
                    count = team1.getHandMg();
                } else if (i == 3) {
                    if (team1.getInfTimeout() <= curTime && team1.getInfantry() > 0) {
                        buyButton = gameResources.getTexture("fx/buyicon_inf.png");
                    } else {
                        buyButton = gameResources.getTexture("fx/buyicon_empty.png");
                    }
                    timeout = Math.max(0, team1.getInfTimeout() - Framework.CurGameTime() * Framework.milisecInNanosec);
                    count = team1.getInfantry();
                }
                g2d.drawString(Time.formatTime(timeout), boxX + 25,
                        (boxY - buyMenu.getHeight() + 145 + i * 2 + i * buyButton.getHeight()) + 22);
                g2d.drawString(count + "x", boxX + 35, (boxY - buyMenu.getHeight() + 145 + i * 2 + i * buyButton.getHeight()) + 34);

                g2d.drawImage(buyButton, boxX + 103, (boxY - buyMenu.getHeight() + 145 + i * 2 + i * buyButton.getHeight()),
                        buyButton.getWidth(), buyButton.getHeight(), null);
            }
        }

        if ((activeSelection != null) && (activeSelection.getSelectedEnts().size() != 0)) {
            uiImage = null;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (i == 0 && j == 0) {
                        uiImage = gameResources.getTexture("fx/button_attack.png");
                    } else if (i == 1 && j == 0) {
                        if (!activeSelection.isRunningMode()) {
                            uiImage = gameResources.getTexture("fx/button_run.png");
                        } else {
                            uiImage = gameResources.getTexture("fx/button_walk.png");
                        }
                    } else if (i == 2 && j == 0) {
                        uiImage = gameResources.getTexture("fx/button_stop.png");
                    } else if (i == 0 && j == 1) {
                        uiImage = gameResources.getTexture("fx/button_stand.png");
                    } else if (i == 1 && j == 1) {
                        uiImage = gameResources.getTexture("fx/button_sit.png");
                    } else if (i == 2 && j == 1) {
                        uiImage = gameResources.getTexture("fx/button_lie.png");
                    } else if (i == 0 && j == 2) {
                        uiImage = gameResources.getTexture("fx/button_melee.png");
                    } else if (i == 1 && j == 2) {
                        uiImage = gameResources.getTexture("fx/button_shoot.png");
                    } else if (i == 2 && j == 2) {
                        uiImage = gameResources.getTexture("fx/button_holdfire.png");
                    }
                    if (uiImage != null) {
                        if (!actionIcons[i][j]) {
                            g2d.drawImage(uiImage, i * 64 + boxX + 6, j * 67 + boxY + 6, i * 64 + boxX + 6 + uiImage.getWidth() / 2, j * 67
                                    + boxY + 6 + uiImage.getHeight(), uiImage.getWidth() / 2, 0, uiImage.getWidth(), uiImage.getHeight(),
                                    null);

                        } else {
                            g2d.drawImage(uiImage, i * 64 + boxX + 6, j * 67 + boxY + 6, i * 64 + boxX + 6 + uiImage.getWidth() / 2, j * 67
                                    + boxY + 6 + uiImage.getHeight(), 0, 0, uiImage.getWidth() / 2, uiImage.getHeight(), null);

                        }
                        uiImage = null;
                    }
                }
            }

            if (menuUpdateDelay < Framework.CurGameTime()) {
                menuUpdateDelay = Framework.CurGameTime() + 150;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        actionIcons[i][j] = false;
                    }
                }
            }

            if (activeSelection != null && activeSelection.isTargetingMode()) {
                actionIcons[0][0] = true;
            }

        } else {
            uiImage = gameResources.getTexture("fx/button_activated.png");
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    g2d.drawImage(uiImage, i * 64 + boxX + 6, j * 67 + boxY + 5, i * 64 + boxX + 6 + uiImage.getWidth(), j * 67 + boxY + 5
                            + uiImage.getHeight(), 0, 0, uiImage.getWidth(), uiImage.getHeight(), null);
                }
            }

        }
    }

    private long menuDelay = 0;
    private long nextSpawn;
    private long nextOrder;

    public boolean guiAction(Point mousePosition) {
        if (mousePosition == null) {
            return false;
        }

        if (activeSelection != null) {
            if (activeSelection.isEmpty()) {
                activeSelection = null;
            }
        }

        if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
            BufferedImage uiImage = gameResources.getTexture("fx/command_box.png");
            BufferedImage buyMenu = gameResources.getTexture("fx/buy_menu_closed.png");
            BufferedImage buyButton = gameResources.getTexture("fx/buyicon_mortar.png");

            int frameWidth = getGameFramework().getFrameWidth();
            int frameHeight = getGameFramework().getFrameHeight();
            int boxX = frameWidth - uiImage.getWidth();
            int boxY = frameHeight - uiImage.getHeight();

            if (menuDelay >= Framework.CurGameTime()) {
                return true;
            }

            BufferedImage minimap = gameResources.getTexture("fx/minimap_canvas.png");
            int minimapBoxX = 0;
            int minimapBoxY = getGameFramework().getFrameHeight() - minimap.getHeight();
            int mapX1 = 16;
            int mapY1 = 16;
            int miniMapSizeX = 260;
            int miniMapSizeY = 214;
            if (newSelection == null && (mousePosition.x > (minimapBoxX + mapX1) && mousePosition.x < (minimapBoxX + mapX1 + miniMapSizeX))
                    && (mousePosition.y > (minimapBoxY + mapY1) && mousePosition.y < (minimapBoxY + mapY1 + miniMapSizeY))) {

                int newCameraX = Math.min((int) ((mousePosition.x - minimapBoxX - mapX1) / minimapScaleX) - frameWidth / 2, getMapSizeX()
                        - frameWidth);
                int newCameraY = Math.min((int) ((mousePosition.y - minimapBoxY - mapY1) / minimapScaleY) - frameHeight / 2, getMapSizeY()
                        - frameHeight);
                newCameraX = Math.max(newCameraX, 0);
                newCameraY = Math.max(newCameraY, 0);

                if (isPlayerPrepearing) {
                    newCameraX = Math.min(newCameraX, playerZoneX - frameWidth);
                    newCameraY = Math.min(newCameraY, playerZoneY - frameHeight);
                }

                getPlayerCamera().setX(newCameraX);
                getPlayerCamera().setY(newCameraY);
                menuDelay = Framework.CurGameTime() + 10;

                return true;
            }
            if (newSelection == null) {
                if (!buyMenuOpened) {
                    if ((mousePosition.x > (boxX + 66) && mousePosition.x < (boxX + 66 + 126))
                            && (mousePosition.y > (boxY - 24) && mousePosition.y < (boxY))) {
                        buyMenuOpened = true;
                        menuDelay = Framework.CurGameTime() + 150;
                        return true;
                    }
                } else {
                    if ((mousePosition.x > (boxX + 66) && mousePosition.x < (boxX + 66 + 126))
                            && (mousePosition.y > (boxY - buyMenu.getHeight() + 120) && mousePosition.y < (boxY - buyMenu.getHeight() + 120 + 24))) {
                        buyMenuOpened = false;
                        menuDelay = Framework.CurGameTime() + 150;
                        return true;
                    }

                    for (int i = 0; i < 4; i++) {

                        if (newSelection == null
                                && (mousePosition.x > boxX + 103)
                                && (mousePosition.x < boxX + 103 + buyButton.getWidth())
                                && (mousePosition.y > (boxY - buyMenu.getHeight() + 145 + i * 2 + i * buyButton.getHeight()) && (mousePosition.y < (boxY
                                        - buyMenu.getHeight() + 145 + i * 2 + i * buyButton.getHeight() + buyButton.getHeight())))) {
                            long curTime = Framework.CurGameTime() * Framework.milisecInNanosec;
                            if (i == 0) {
                                if (team1.getMortar() > 0 && team1.getMortarTimeout() <= curTime) {
                                    team1.spawnMortar();
                                }
                                menuDelay = Framework.CurGameTime() + 150;
                                return true;
                            } else if (i == 1) {
                                if (team1.getStatMg() > 0 && team1.getStatMgTimeout() <= curTime) {
                                    team1.spawnStatMg();
                                }
                                menuDelay = Framework.CurGameTime() + 150;
                                return true;
                            } else if (i == 2) {
                                if (team1.getHandMg() > 0 && team1.getHandMgTimeout() <= curTime) {
                                    team1.spawnHandMgSquad();
                                }
                                menuDelay = Framework.CurGameTime() + 150;
                                return true;
                            } else if (i == 3) {
                                if (team1.getInfantry() > 0 && team1.getInfTimeout() <= curTime) {
                                    team1.spawnInfSquad();
                                }
                                menuDelay = Framework.CurGameTime() + 150;
                                return true;
                            }
                        }
                    }

                }
            }

            if (mousePosition.x > boxX && mousePosition.y > boxY && activeSelection != null && newSelection == null) {
                uiImage = gameResources.getTexture("fx/button_attack.png");

                if (menuDelay >= Framework.CurGameTime()) {
                    return true;
                }

                menuDelay = Framework.CurGameTime() + 150;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (mousePosition.x > (i * 64 + boxX + 6) && mousePosition.y > (j * 67 + boxY + 6)
                                && mousePosition.x < (i * 64 + boxX + 6 + uiImage.getWidth() / 2)
                                && mousePosition.y < (j * 67 + boxY + 6 + uiImage.getHeight())) {
                            if (i == 0 && j == 0) {
                                if (activeSelection.isTargetingMode()) {
                                    actionIcons[0][0] = false;
                                    activeSelection.setTargetingMode(false);
                                } else {
                                    actionIcons[0][0] = true;
                                    activeSelection.setTargetingMode(true);
                                }

                            } else if (i == 2 && j == 0) {
                                for (Entity curEnt : activeSelection.getSelectedEnts()) {
                                    ((Human) curEnt).getAI().stop();
                                    ((Human) curEnt).getAI().stopShoot();
                                }
                                actionIcons[2][0] = true;

                            } else if (i == 1 && j == 0) {
                                activeSelection.setRunningMode(!activeSelection.isRunningMode());
                                for (Entity curEnt : activeSelection.getSelectedEnts()) {
                                    ((Human) curEnt).getAI().setRunning(activeSelection.isRunningMode());
                                }
                                actionIcons[1][0] = true;
                            } else if (i == 0 && j == 1) {
                                for (Entity curEnt : activeSelection.getSelectedEnts()) {
                                    ((Human) curEnt).getAI().stay();
                                }
                                actionIcons[0][1] = true;
                            } else if (i == 1 && j == 1) {
                                for (Entity curEnt : activeSelection.getSelectedEnts()) {
                                    ((Human) curEnt).getAI().sit();
                                }
                                actionIcons[1][1] = true;
                            } else if (i == 2 && j == 1) {
                                for (Entity curEnt : activeSelection.getSelectedEnts()) {
                                    ((Human) curEnt).getAI().lieDown();
                                }
                                actionIcons[2][1] = true;
                            } else if (i == 0 && j == 2) {
                                for (Entity curEnt : activeSelection.getSelectedEnts()) {
                                    ((Human) curEnt).getAI().setWeaponBehavior("melee_only");
                                }
                                actionIcons[0][2] = true;
                            } else if (i == 1 && j == 2) {
                                for (Entity curEnt : activeSelection.getSelectedEnts()) {
                                    ((Human) curEnt).getAI().setWeaponBehavior("combined");
                                }
                                actionIcons[1][2] = true;
                            } else if (i == 2 && j == 2) {
                                for (Entity curEnt : activeSelection.getSelectedEnts()) {
                                    ((Human) curEnt).getAI().setWeaponBehavior("dont_shoot");
                                }
                                actionIcons[2][2] = true;
                            }
                        }
                    }
                }
                return true;
            }
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

            if (newSelection == null) {
                newSelection = new GuiCanvasSelection(playerCamera.getX() + mousePosition.x, playerCamera.getY() + mousePosition.y,
                        playerCamera.getX() + mousePosition.x + 1, playerCamera.getY() + mousePosition.y + 1);
                newSelection.selectUnits();
            } else {
                if (mousePosition.x != 0 && mousePosition.y != 0) {
                    newSelection.setX2(playerCamera.getX() + mousePosition.x);
                    newSelection.setY2(playerCamera.getY() + mousePosition.y);
                    newSelection.selectUnits();
                }
            }
        } else if (Canvas.mouseButtonState(MouseEvent.BUTTON3)) { // ПКМ
            if (nextOrder < gameTime) {
                if (activeSelection != null) {
                    if (Shooting.getShootingTargetFromLocalMousePos(mousePosition) != null) {

                        boolean attackSoundPlayed = true;
                        if (Math.random() > 0) {
                            attackSoundPlayed = false;
                        }

                        for (Entity curEnt : activeSelection.getSelectedEnts()) {
                            int num = activeSelection.getSelectedEnts().size();
                            num = Math.min(12 * num, 200);
                            ((Human) curEnt).getAI().setDestX(genRand(-num, num) + (int) (playerCamera.getX() + mousePosition.getX()));
                            ((Human) curEnt).getAI().setDestY(
                                    genRand(-num * 2, num * 2) + (int) (playerCamera.getY() + mousePosition.getY()));
                            ((Human) curEnt).getAI().setRunning(activeSelection.isRunningMode());

                            if (!attackSoundPlayed) {
                                ((Human) curEnt).playerSounds.playAttackSound();
                                attackSoundPlayed = true;
                            }
                            Animation order_mark = new Animation("target_mark", gameResources.getTexture("fx/target.png"), 107, 97, 5, 50,
                                    false, playerCamera.getX() + mousePosition.x - 54, playerCamera.getY() + mousePosition.y - 48, 0, 0,
                                    false);
                            particleCreate(order_mark);
                            ((Human) curEnt).getAI().setShootTarget(Shooting.getShootingTargetFromLocalMousePos(mousePosition));

                        }

                        if (activeSelection.isTargetingMode()) {
                            activeSelection.setTargetingMode(false);
                        }
                        nextOrder = gameTime + Framework.secInNanosec / 2;
                        return;
                    } else {
                        if (trenchesBitMap.calculateHeight(mousePosition.getX() + playerCamera.getX(),
                                mousePosition.getY() + playerCamera.getY()) <= -32) {
                            int num = activeSelection.getSelectedEnts().size();
                            num = Math.min(12 * num, 200);
                            ArrayList<Point> positions = trenchesBitMap.findTrenches((int) (mousePosition.getX() + playerCamera.getX()),
                                    (int) (mousePosition.getY() + playerCamera.getY()), num * 2);
                            if (positions.size() != 0) {
                                Random randomGenerator = new Random();
                                int index = 0;
                                for (Entity curEnt : activeSelection.getSelectedEnts()) {
                                    index = randomGenerator.nextInt(positions.size());
                                    ((Human) curEnt).getAI().setDestX(positions.get(index).x);
                                    ((Human) curEnt).getAI().setDestY(positions.get(index).y);
                                }

                                Animation order_mark = new Animation("order_mark", gameResources.getTexture("fx/get_in.png"), 107, 97, 5,
                                        50, false, playerCamera.getX() + mousePosition.x - 52, playerCamera.getY() + mousePosition.y - 48,
                                        0, 0, false);
                                particleCreate(order_mark);
                                nextOrder = gameTime + Framework.secInNanosec / 2;
                                return;
                            }
                        }
                    }

                    boolean moveSoundPlayed = true;
                    if (Math.random() > 0.5) {
                        moveSoundPlayed = false;
                    }
                    for (Entity curEnt : activeSelection.getSelectedEnts()) {
                        int num = activeSelection.getSelectedEnts().size();
                        if (!moveSoundPlayed) {
                            ((Human) curEnt).playerSounds.playMoveSound();
                            moveSoundPlayed = true;
                        }
                        ((Human) curEnt).getAI().setShootTarget(null);

                        num = Math.min(12 * num, 200);
                        ((Human) curEnt).getAI().setRunning(activeSelection.isRunningMode());
                        ((Human) curEnt).getAI().setDestX(genRand(-num / 2, num / 2) + (int) (playerCamera.getX() + mousePosition.getX()));
                        ((Human) curEnt).getAI().setDestY(genRand(-num * 2, num * 2) + (int) (playerCamera.getY() + mousePosition.getY()));
                    }

                    Animation order_mark = new Animation("order_mark", gameResources.getTexture("fx/order.png"), 107, 97, 5, 50, false,
                            playerCamera.getX() + mousePosition.x - 54, playerCamera.getY() + mousePosition.y - 48, 0, 0, false);
                    particleCreate(order_mark);

                }
                nextOrder = gameTime + Framework.secInNanosec / 2;
            }
        } else if (Canvas.mouseButtonState(MouseEvent.BUTTON2)) { // СКМ
            if (nextSpawn < gameTime) {
                // Human NPC = new Human("rifleman_uk", "weapon_uk_enfield");
                // Ents.addNewEntityInGame(NPC);
                // NPC.getAI().setTeam(2);

                // VectorLight pos = new VectorLight(playerCamera.getX() +
                // mousePosition.getX(), playerCamera.getY() +
                // mousePosition.getY(),
                // 0.0);
                // NPC.SetPos(pos);

                nextSpawn = gameTime + Framework.secInNanosec / 8;
            }
        } else if (!Canvas.mouseButtonState(MouseEvent.BUTTON1)) {

            if (newSelection != null) {
                activeSelection = newSelection;
                newSelection = null;
            }
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
