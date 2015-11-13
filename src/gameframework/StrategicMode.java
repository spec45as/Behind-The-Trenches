package gameframework;

import gameframework.Framework.GameState;
import gameframework.entities.PlayerSpectator;
import gameframework.libs.Animation;
import gameframework.libs.RegionOffsets;
import gameframework.libs.SoundPlayer;
import gameframework.libs.other.Resources;
import gameframework.libs.other.StrategicMapSprite;
import gameframework.libs.other.GraphicsUtilities;
import strategicLogic.Game;
import strategicLogic.General;
import strategicLogic.Region;
import strategicLogic.Squad;
import strategicLogic.Team;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class StrategicMode extends Gamemode {
    private SoundPlayer soundPlayer;
    private Framework gameFramework;
    private Game strategyLogic;

    public Framework getGameFramework() {
        return this.gameFramework;
    }

    public SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }

    private ArrayList<Animation> allParticles;
    private ArrayList<StrategicMapSprite> regionsMap = new ArrayList<StrategicMapSprite>();
    private ArrayList<StrategicMapSprite> regionsMasks = new ArrayList<StrategicMapSprite>();
    private ArrayList<StrategicMapSprite> regionsMapBorder = new ArrayList<StrategicMapSprite>();
    private ArrayList<StrategicMapSprite> regionsMapBorderOutlines = new ArrayList<StrategicMapSprite>();
    private ArrayList<StrategicMapSprite> gameGenerals = new ArrayList<StrategicMapSprite>();
    private ArrayList<StrategicMapSprite> buttons = new ArrayList<StrategicMapSprite>(); // 0
                                                                                         // =
                                                                                         // end
                                                                                         // of
    // turn; 1 =
    // buy
    // squads; 2
    // = develop
    // people; 3
    // = develop
    // money; 4
    // = fortify

    private int drawnButtons = 0;
    private boolean buying = false;
    private ArrayList<Boolean> isButtonLookedFor = new ArrayList<Boolean>();

    public ArrayList<StrategicMapSprite> getButtons() {
        return buttons;
    }

    public Resources getGameResources() {
        return gameResources;
    }

    public void setGameResources(Resources gameResources) {
        this.gameResources = gameResources;
    }

    public void setButtons(ArrayList<StrategicMapSprite> buttons) {
        this.buttons = buttons;
    }

    public ArrayList<StrategicMapSprite> getGameGenerals() {
        return gameGenerals;
    }

    public void setGameGenerals(ArrayList<StrategicMapSprite> gameGenerals) {
        this.gameGenerals = gameGenerals;
    }

    public ArrayList<StrategicMapSprite> getRegionsMap() {
        return regionsMap;
    }

    private final int mapSizeX = 3200;
    private final int mapSizeY = 2284;
    private final int WINDOW_MAP_SIZE_X = 766;
    private final int WINDOW_MAP_SIZE_Y = 533;
    private final int WINDOW_OFFSET_X = 33;
    private final int WINDOW_OFFSET_Y = 70;

    private Font font;
    public Resources gameResources;

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

    private PlayerSpectator playerCamera;

    public PlayerSpectator getPlayerCamera() {
        return playerCamera;
    }

    public StrategicMode(Framework framework) {
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

    private void Initialize() {
        this.playerCamera = new PlayerSpectator(1200, 1400, getGameFramework());
        this.gameResources = new Resources();
        allParticles = new ArrayList<Animation>();
        soundPlayer = new SoundPlayer();
        strategyLogic = new Game();
        strategyLogic.getMap().loadGameInfo();

        Point pos;
        for (int i = 0; i <= 54; i++) {
            pos = RegionOffsets.getRegionOffset(i);
            StrategicMapSprite sprite = new StrategicMapSprite(pos.x, pos.y, Framework.activeGame().getGameResources()
                    .getTexture("map/" + i + ".png"), false);
            regionsMap.add(i, sprite);
            StrategicMapSprite sprite0 = new StrategicMapSprite(pos.x, pos.y, Framework.activeGame().getGameResources()
                    .getTexture("regions_masks/" + i + ".png"), false);
            regionsMasks.add(i, sprite0);
            sprite.setTexture(GraphicsUtilities.toCompatibleImage(sprite.getTexture()));
            sprite0.setTexture(GraphicsUtilities.toCompatibleImage(sprite0.getTexture()));
        }

        for (int i = 0; i < 55; i++) {
            getRegionsMap().get(i).makeColorRegion(strategyLogic.getMap().getRegions().get(i).getTeam().getColor(),
                    getRegionsMasks().get(i));
        }

        for (int i = 1, j = 0; i <= 117; i++) {
            StrategicMapSprite sprite = new StrategicMapSprite(256 * (i % 13 == 0 ? 12 : i % 13 - 1), 256 * j, Framework.activeGame()
                    .getGameResources().getTexture("map_all/map_all_" + i + ".png"), false);
            regionsMapBorder.add(sprite);
            if (i % 13 == 0) {
                j++;
            }
        }

        for (int i = 1, j = 0; i <= 117; i++) {
            StrategicMapSprite sprite = new StrategicMapSprite(256 * (i % 13 == 0 ? 12 : i % 13 - 1), 256 * j, Framework.activeGame()
                    .getGameResources().getTexture("borders_only/borders_all_" + i + ".png"), false);
            regionsMapBorderOutlines.add(sprite);
            if (i % 13 == 0) {
                j++;
            }
        }

        for (int i = 0; i < 4; i++) {
            StrategicMapSprite generalStrategicMapSprite = new StrategicMapSprite(0, 0, gameResources.getTexture("fx/general.png"), false);
            gameGenerals.add(generalStrategicMapSprite);
        }

        buttons.add(new StrategicMapSprite((int) (830 * Framework.getALPHA_X()), (int) (540 * Framework.getALPHA_Y()), gameResources
                .getTexture("fx/end_of_turn.png"), false));
        buttons.add(new StrategicMapSprite((int) (830 * Framework.getALPHA_X()), (int) (480 * Framework.getALPHA_Y()), gameResources
                .getTexture("fx/buy_squad.png"), false));
        buttons.add(new StrategicMapSprite((int) (830 * Framework.getALPHA_X()), (int) (360 * Framework.getALPHA_Y()), gameResources
                .getTexture("fx/develop_people.png"), false));
        buttons.add(new StrategicMapSprite((int) (830 * Framework.getALPHA_X()), (int) (420 * Framework.getALPHA_Y()), gameResources
                .getTexture("fx/develop_money.png"), false));
        buttons.add(new StrategicMapSprite((int) (830 * Framework.getALPHA_X()), (int) (480 * Framework.getALPHA_Y()), gameResources
                .getTexture("fx/fortify.png"), false));

        font = new Font("monospaced", Font.BOLD, 12);

        for (int i = 0; i < strategyLogic.getListOfSquadsBelongToTeam(strategyLogic.getMap().getTeams().get(strategyLogic.playerTeamID))
                .size() + 1; i++) {
            isButtonLookedFor.add(false);
        }
    }

    public void particleCreate(Animation particle) {
        this.allParticles.add(particle);
    }

    public void particleRemove(Animation particle) {
        this.allParticles.remove(particle);
    }

    private void LoadContent() {
    }

    public void RestartGame() {
        // allEnts.clear();
    }

    private long musicDelay = 0l;

    public void UpdateGame(long gameTime, Point mousePosition) {
        if (Framework.CurGameTime() >= musicDelay) {
            soundPlayer.playRandomMenuSound();
            musicDelay = 120l * 1000000000l;
        }
        playerCamera.Update(getCustomMouseCursorPos());

        for (int i = 0; i < getGameGenerals().size(); i++) {
            if (strategyLogic.getMap().getTeams().get(i).getGeneral() != null) {
                int regionGeneralID = strategyLogic.getMap().getRegionOfGeneral(strategyLogic.getMap().getTeams().get(i).getGeneral());
                StrategicMapSprite generalRegion = regionsMap.get(regionGeneralID);
                if (Framework
                        .activeGame()
                        .getPlayerCamera()
                        .canSeeStrategicMapSprite(generalRegion.getX() + getWINDOW_OFFSET_X(), generalRegion.getY() + getWINDOW_OFFSET_Y(),
                                generalRegion.getSizeX(), generalRegion.getSizeY())) {
                    gameGenerals.get(i).setX(generalRegion.getX() + strategyLogic.getMap().getRegions().get(regionGeneralID).getGeneralX());
                    gameGenerals.get(i).setY(generalRegion.getY() + strategyLogic.getMap().getRegions().get(regionGeneralID).getGeneralY());
                }
            }
        }

        isPlayerShooting(gameTime, mousePosition);
    }

    public void Draw(Graphics2D g2d, Point mousePosition, long gameTime) {
        DrawGUI(g2d, mousePosition, gameTime);
    }

    private void DrawCommon(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        BufferedImage mapCanvas = Framework.activeGame().getGameResources().getTexture("fx/menu_background.png");
        g2d.drawImage(mapCanvas, 0, 0, gameFramework.getFrameWidth(), gameFramework.getHeight(), 0, 0, mapCanvas.getWidth(),
                mapCanvas.getHeight(), null);

        drawnButtons = 0;

        g2d.setFont(new Font("monospaced", Font.BOLD, (int) (20 * Framework.getALPHA_X())));
        g2d.drawString(Integer.toString(strategyLogic.getMap().getTeams().get(strategyLogic.playerTeamID).getPeople()),
                (int) (50 * Framework.getALPHA_X()), (int) (28 * Framework.getALPHA_Y()));
        g2d.drawString(Integer.toString(strategyLogic.getMap().getTeams().get(strategyLogic.playerTeamID).getMoney()),
                (int) (210 * Framework.getALPHA_X()), (int) (28 * Framework.getALPHA_Y()));

        g2d.setFont(new Font("monospaced", Font.BOLD, (int) (28 * Framework.getALPHA_X())));
        g2d.drawString(strategyLogic.getMap().getTeams().get(strategyLogic.playerTeamID).getName(), (int) (420 * Framework.getALPHA_X()),
                (int) (28 * Framework.getALPHA_Y()));

        g2d.drawString(strategyLogic.getMonth() + ", " + strategyLogic.getYear(), (int) (750 * Framework.getALPHA_X()),
                (int) (28 * Framework.getALPHA_Y()));

        g2d.setColor(Color.BLACK);
    }

    public void DrawGUI(Graphics2D g2d, Point mousePosition, long gameTime) {
        if (mousePosition == null) {
            mousePosition = new Point(1, 1);
        }

        for (StrategicMapSprite curRegion : regionsMapBorder) {
            curRegion.DrawRegionBorders(g2d, playerCamera.getX(), playerCamera.getY());
        }

        for (int i = 0; i < 55; i++) {
            getRegionsMap().get(i).DrawRegion(g2d, playerCamera.getX(), playerCamera.getY());
        }

        for (StrategicMapSprite curFragment : regionsMapBorderOutlines) {
            curFragment.DrawRegionBorders(g2d, playerCamera.getX(), playerCamera.getY());
        }

        for (int i = 0; i < strategyLogic.getMap().getTeams().size() - 1; i++) {
            if (strategyLogic.getMap().getTeams().get(i).getGeneral() != null) {
                gameGenerals.get(i).DrawGeneral(g2d, playerCamera.getX(), playerCamera.getY());
            }
        }

        updateParticles();

        g2d.setFont(font);

        g2d.setColor(Color.WHITE);

        if (buying) {
            DrawCommon(g2d);
            Team team = strategyLogic.getMap().getTeams().get(strategyLogic.playerTeamID);
            g2d.setFont(new Font("monospaced", Font.BOLD, (int) (12 * Framework.getALPHA_X())));
            g2d.drawString("Name", (int) (830 * Framework.getALPHA_X()), (int) (60 * Framework.getALPHA_Y()));
            g2d.drawString("People", (int) (930 * Framework.getALPHA_X()), (int) (60 * Framework.getALPHA_X()));
            g2d.drawString("Money", (int) (975 * Framework.getALPHA_X()), (int) (60 * Framework.getALPHA_Y()));

            int j = 0;
            for (String k : strategyLogic.getListOfSquadsBelongToTeam(team).keySet()) {
                Squad squad = strategyLogic.getListOfSquadsBelongToTeam(team).get(k);
                String commonName = squad.getName().substring(squad.getName().indexOf(" ") + 1);
                if (isButtonLookedFor.get(j)) {
                    g2d.drawString(commonName, (int) (830 * Framework.getALPHA_X()), (int) ((90 + 30 * j + 2) * Framework.getALPHA_Y()));
                    g2d.drawString(squad.getCostPeople() + "", (int) (930 * Framework.getALPHA_X()),
                            (int) ((90 + 30 * j + 2) * Framework.getALPHA_Y()));
                    g2d.drawString(squad.getCostMoney() + "", (int) (975 * Framework.getALPHA_X()),
                            (int) ((90 + 30 * j + 2) * Framework.getALPHA_Y()));
                } else {
                    g2d.drawString(commonName, (int) (830 * Framework.getALPHA_X()), (int) ((90 + 30 * j) * Framework.getALPHA_Y()));
                    g2d.drawString(squad.getCostPeople() + "", (int) (930 * Framework.getALPHA_X()),
                            (int) ((90 + 30 * j) * Framework.getALPHA_Y()));
                    g2d.drawString(squad.getCostMoney() + "", (int) (975 * Framework.getALPHA_X()),
                            (int) ((90 + 30 * j) * Framework.getALPHA_Y()));
                }
                j++;
            }

            g2d.setFont(new Font("monospaced", Font.BOLD, (int) (32 * Framework.getALPHA_X())));

            if (isButtonLookedFor.get(j)) {
                g2d.drawString("Back", (int) (880 * Framework.getALPHA_X()), (int) ((590 + 2) * Framework.getALPHA_Y()));
            } else {
                g2d.drawString("Back", (int) (880 * Framework.getALPHA_X()), (int) (590 * Framework.getALPHA_Y()));
            }

        } else {
            Region curSelectedRegion = null;
            for (int i = 0; i < regionsMap.size(); i++) {
                if (regionsMap.get(i).isSelected()) {
                    curSelectedRegion = strategyLogic.getMap().getRegions().get(i);
                    break;
                }
            }
            if (curSelectedRegion != null) {
                DrawCommon(g2d);
                buttons.get(0).DrawButton(g2d);
                g2d.setFont(new Font("monospaced", Font.BOLD, (int) (16 * Framework.getALPHA_X())));
                g2d.drawString(curSelectedRegion.getName(), (int) (830 * Framework.getALPHA_X()), (int) (60 * Framework.getALPHA_Y()));
                g2d.drawString(curSelectedRegion.getTeam().getName(), (int) (830 * Framework.getALPHA_X()),
                        (int) (90 * Framework.getALPHA_Y()));
                g2d.drawString("People Growth: " + curSelectedRegion.getPeopleGrowth(), (int) (830 * Framework.getALPHA_X()),
                        (int) (120 * Framework.getALPHA_Y()));
                g2d.drawString("Money Growth: " + curSelectedRegion.getMoneyGrowth(), (int) (830 * Framework.getALPHA_X()),
                        (int) (150 * Framework.getALPHA_Y()));
                g2d.drawString("People Amount: " + curSelectedRegion.getPeopleAmount(), (int) (830 * Framework.getALPHA_X()),
                        (int) (180 * Framework.getALPHA_Y()));
                if (curSelectedRegion.getLevelDevelopment() == 0) {
                    g2d.drawString("Development: None", (int) (830 * Framework.getALPHA_X()), (int) (210 * Framework.getALPHA_Y()));
                } else {
                    g2d.drawString("Developed: " + curSelectedRegion.getLevelDevelopment() + " " + curSelectedRegion.getTypeDevelopment(),
                            (int) (830 * Framework.getALPHA_X()), (int) (210 * Framework.getALPHA_Y()));
                }
                g2d.drawString("Defence: " + curSelectedRegion.getLevelDefence(), (int) (830 * Framework.getALPHA_X()),
                        (int) (240 * Framework.getALPHA_Y()));
                if (curSelectedRegion.getTeam() == strategyLogic.getMap().getTeams().get(strategyLogic.playerTeamID)) {
                    if (curSelectedRegion.getTypeDevelopment().equals("people")) {
                        g2d.drawString("Cost: " + ((int) (Game.COST_DEVELOPMENT * (1 + 0.2 * curSelectedRegion.getLevelDevelopment()))),
                                (int) (830 * Framework.getALPHA_X()), (int) (417 * Framework.getALPHA_Y()));
                        g2d.drawString("Cost: " + Game.COST_DEVELOPMENT, (int) (830 * Framework.getALPHA_X()),
                                (int) (477 * Framework.getALPHA_Y()));
                    } else {
                        g2d.drawString("Cost: " + Game.COST_DEVELOPMENT, (int) (830 * Framework.getALPHA_X()),
                                (int) (417 * Framework.getALPHA_Y()));
                        g2d.drawString("Cost: " + ((int) (Game.COST_DEVELOPMENT * (1 + 0.2 * curSelectedRegion.getLevelDevelopment()))),
                                (int) (830 * Framework.getALPHA_X()), (int) (477 * Framework.getALPHA_Y()));
                    }
                    g2d.drawString("Cost: " + ((int) (Game.COST_FORTIFICATION * (1 + curSelectedRegion.getLevelDefence()))),
                            (int) (830 * Framework.getALPHA_X()), (int) (537 * Framework.getALPHA_Y()));
                    getButtons().get(2).DrawButton(g2d);
                    getButtons().get(3).DrawButton(g2d);
                    getButtons().get(4).DrawButton(g2d);
                }
                drawnButtons = 1;
            } else {
                General curSelectedGeneral = null;
                int i;
                for (i = 0; i < getGameGenerals().size(); i++) {
                    if (getGameGenerals().get(i).isSelected()) {
                        curSelectedGeneral = strategyLogic.getMap().getTeams().get(i).getGeneral();
                        break;
                    }
                }
                if (curSelectedGeneral != null) {
                    Region generalRegion = strategyLogic.getMap().getRegions()
                            .get(strategyLogic.getMap().getRegionOfGeneral(curSelectedGeneral));
                    /*
                     * StrategicMapSprite regionGeneral = getRegionsMap().get(
                     * generalRegion.getUniqID());
                     */
                    boolean isHeYourGeneral = (curSelectedGeneral.getTeam() == strategyLogic.getMap().getTeams()
                            .get(strategyLogic.playerTeamID));

                    if (!curSelectedGeneral.getTeam().getTurn() && isHeYourGeneral) {

                        for (int k : generalRegion.getUniqIDNeighbourRegions()) {
                            /*
                             * Region aimRegion = strategyLogic.getMap()
                             * .getRegions().get(k);
                             */
                            getRegionsMap().get(k).setImageAlpha(0.6f);
                        }

                    }
                    DrawCommon(g2d);
                    buttons.get(0).DrawButton(g2d);
                    g2d.setFont(new Font("monospaced", Font.BOLD, (int) (12 * Framework.getALPHA_X())));
                    g2d.drawString(curSelectedGeneral.getName(), (int) (830 * Framework.getALPHA_X()), (int) (60 * Framework.getALPHA_Y()));
                    g2d.drawString("Level " + curSelectedGeneral.getLevel(), (int) (830 * Framework.getALPHA_X()),
                            (int) (90 * Framework.getALPHA_Y()));
                    g2d.drawString("Squads: ", (int) (830 * Framework.getALPHA_X()), (int) (120 * Framework.getALPHA_Y()));
                    int j = 1;
                    for (Squad s : curSelectedGeneral.getSquads()) {
                        g2d.drawString(s.getName() + " " + s.getPeopleAmount(), (int) (830 * Framework.getALPHA_X()),
                                (int) ((120 + 30 * j) * Framework.getALPHA_Y()));
                        j++;
                    }

                    if (isHeYourGeneral) {
                        getButtons().get(1).DrawButton(g2d);
                    }
                    drawnButtons = 2;
                } else {
                    DrawCommon(g2d);
                    buttons.get(0).DrawButton(g2d);
                }
            }
        }
    }

    private long menuDelay = 0;
    private boolean lmbPressed;

    private void unselectAllRegions() {
        for (int i = 0; i < getRegionsMap().size(); i++) {
            getRegionsMap().get(i).setSelected(false);
        }
    }

    private void unselectAllGenerals() {
        for (int i = 0; i < getGameGenerals().size(); i++) {
            getGameGenerals().get(i).setSelected(false);
        }
    }

    private void unwatchAllButtons() {
        for (int i = 0; i < getButtons().size(); i++) {
            getButtons().get(i).setLookedFor(false);
        }
    }

    private void unwatchAllRegions() {
        for (int i = 0; i < getRegionsMap().size(); i++) {
            getRegionsMap().get(i).setLookedFor(false);
        }
    }

    private void unwatchAllGenerals() {
        for (int i = 0; i < getGameGenerals().size(); i++) {
            getGameGenerals().get(i).setLookedFor(false);
        }
    }

    private StrategicMapSprite getRegion() {
        for (int i = 0; i < getRegionsMap().size(); i++) {
            StrategicMapSprite sprite = getRegionsMap().get(i);
            BufferedImage image = sprite.getTexture();
            Point cursorPos = getCustomMouseCursorPos();

            int localImageX = (int) (cursorPos.x + getPlayerCamera().getX() - sprite.getX());
            int localImageY = (int) (cursorPos.y + getPlayerCamera().getY() - sprite.getY());

            if (localImageX < 0 || localImageY < 0) {
                continue;
            }

            if (localImageX > image.getWidth() || localImageY > image.getHeight()) {
                continue;
            }

            int alpha = 0;
            try {
                alpha = (image.getRGB(localImageX, localImageY) >> 24) & 0xff;
            } catch (Exception e) {
                continue;
            }

            if (alpha == 0) {
                continue;
            } else {
                return sprite;
            }

        }
        return null;
    }

    private StrategicMapSprite getButton(StrategicMapSprite button) {
        BufferedImage image = button.getTexture();
        Point cursorPos = getCustomMouseCursorPos();

        int localImageX = (int) (cursorPos.x - button.getX());
        int localImageY = (int) (cursorPos.y - button.getY());

        if (localImageX < 0 || localImageY < 0) {
            return null;
        }

        if (localImageX > image.getWidth() || localImageY > image.getHeight()) {
            return null;
        }

        int alpha = 0;
        try {
            alpha = (image.getRGB(localImageX, localImageY) >> 24) & 0xff;
        } catch (Exception e) {
            return null;
        }

        if (alpha == 0) {
            return null;
        } else {
            return button;
        }
    }

    private StrategicMapSprite getDrawnButton() {
        if (drawnButtons == 0) {
            return getButton(getButtons().get(0));
        } else {
            if (drawnButtons == 1) {
                for (int i = 2; i < getButtons().size(); i++) {
                    StrategicMapSprite button = getButton(getButtons().get(i));
                    if (button != null) {
                        return button;
                    }
                }
                return getButton(getButtons().get(0));
            } else {
                if (getButton(getButtons().get(1)) == null) {
                    return getButton(getButtons().get(0));
                } else {
                    return getButton(getButtons().get(1));
                }
            }
        }
    }

    private StrategicMapSprite getGeneral() {
        for (int i = 0; i < getGameGenerals().size(); i++) {
            StrategicMapSprite general = getGameGenerals().get(i);
            BufferedImage image = general.getTexture();
            Point cursorPos = getCustomMouseCursorPos();
            int localImageX = (int) (cursorPos.x + getPlayerCamera().getX() - general.getX());
            int localImageY = (int) (cursorPos.y + getPlayerCamera().getY() - general.getY());

            if (localImageX < 0 || localImageY < 0) {
                continue;
            }

            if (localImageX > image.getWidth() || localImageY > image.getHeight()) {
                continue;
            }

            int alpha = 0;
            try {
                alpha = (image.getRGB(localImageX, localImageY) >> 24) & 0xff;
            } catch (Exception e) {
                continue;
            }

            if (alpha == 0) {
                continue;
            } else {
                return general;
            }
        }
        return null;
    }

    public boolean guiAction(Point mousePosition) {
        unwatchAllRegions();
        unwatchAllGenerals();
        unwatchAllButtons();
        for (int j = 0; j < strategyLogic.getListOfSquadsBelongToTeam(strategyLogic.getMap().getTeams().get(strategyLogic.playerTeamID))
                .size() + 1; j++) {
            isButtonLookedFor.set(j, false);
        }
        if (buying) {
            Point cursorPos = getCustomMouseCursorPos();
            Team team = strategyLogic.getMap().getTeams().get(strategyLogic.playerTeamID);
            HashMap<String, Squad> teamSquads = strategyLogic.getListOfSquadsBelongToTeam(team);

            if (cursorPos.getX() >= (int) (830 * Framework.getALPHA_X())) {
                int i = 0;
                String key = "";
                for (String k : teamSquads.keySet()) {
                    if ((cursorPos.getY() >= (int) ((75 + 30 * i) * Framework.getALPHA_Y()))
                            && (cursorPos.getY() <= (int) ((90 + 30 * i) * Framework.getALPHA_Y()))) {
                        isButtonLookedFor.set(i, true);
                        key = k;
                        break;
                    }
                    i++;
                }
                if (i != teamSquads.size()) {
                    if (Framework.CurGameTime() < menuDelay) {
                        return false;
                    }

                    if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
                        menuDelay = Framework.CurGameTime() + 50;

                        if (lmbPressed) {
                            return false;
                        }
                        lmbPressed = true;

                        unselectAllRegions();
                        unselectAllGenerals();
                        try {
                            team.getGeneral().buySquad(key);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        buying = false;

                        return true;

                    } else {
                        lmbPressed = false;
                    }
                } else {
                    if ((cursorPos.getY() >= (int) (570 * Framework.getALPHA_Y()))
                            && (cursorPos.getY() <= (int) (590 * Framework.getALPHA_Y()))) {
                        isButtonLookedFor.set(i, true);

                        if (Framework.CurGameTime() < menuDelay) {
                            return false;
                        }

                        if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
                            menuDelay = Framework.CurGameTime() + 50;

                            if (lmbPressed) {
                                return false;
                            }
                            lmbPressed = true;

                            unselectAllRegions();
                            unselectAllGenerals();
                            buying = false;
                            return true;

                        } else {
                            lmbPressed = false;
                        }
                    }
                }
            } else {
                StrategicMapSprite sprite = getGeneral();
                if (sprite == null) {
                    sprite = getRegion();
                }

                if (sprite != null) {
                    if (!sprite.isSelected()) {
                        sprite.setLookedFor(true);
                    }
                }

                if (Framework.CurGameTime() < menuDelay) {
                    return false;
                }

                if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
                    menuDelay = Framework.CurGameTime() + 50;

                    if (lmbPressed) {
                        return false;
                    }
                    lmbPressed = true;

                    if (sprite != null) {
                        boolean isSelected = sprite.isSelected();
                        for (int i = 0; i < getGameGenerals().size(); i++) {
                            int j;
                            System.out.println(i + "  " + strategyLogic.playerTeamID);
                            if ((getGameGenerals().get(i).isSelected()) && (i == strategyLogic.playerTeamID)) {
                                for (j = 0; j < getRegionsMap().size(); j++) {
                                    if (getRegionsMap().get(j) == sprite) {
                                        break;
                                    }
                                }
                                if (j != getRegionsMap().size()) {
                                    strategyLogic.getMap().getTeams().get(i).getGeneral()
                                            .move(strategyLogic.getMap().getRegions().get(j).getUniqID());
                                    if (strategyLogic.amountLostTeams() == strategyLogic.getMap().getTeams().size() - 2) {
                                        Framework.gameState = GameState.WIN;
                                    }
                                }
                                break;
                            }
                        }
                        unselectAllRegions();
                        unselectAllGenerals();
                        sprite.setSelected(!isSelected);
                        return true;
                    }

                } else {
                    lmbPressed = false;
                }
            }
        } else {
            StrategicMapSprite sprite = getDrawnButton();
            if (sprite == null) {
                sprite = getGeneral();
                if (sprite == null) {
                    sprite = getRegion();
                }

                if (sprite != null) {
                    if (!sprite.isSelected()) {
                        sprite.setLookedFor(true);
                    }
                }

                if (Framework.CurGameTime() < menuDelay) {
                    return false;
                }

                if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
                    menuDelay = Framework.CurGameTime() + 50;

                    if (lmbPressed) {
                        return false;
                    }
                    lmbPressed = true;

                    if (sprite != null) {
                        boolean isSelected = sprite.isSelected();
                        for (int i = 0; i < getGameGenerals().size(); i++) {
                            int j;
                            if (getGameGenerals().get(i).isSelected() && (i == strategyLogic.playerTeamID)) {
                                for (j = 0; j < getRegionsMap().size(); j++) {
                                    if (getRegionsMap().get(j) == sprite) {
                                        break;
                                    }
                                }
                                if (j != getRegionsMap().size()) {
                                    strategyLogic.getMap().getTeams().get(i).getGeneral()
                                            .move(strategyLogic.getMap().getRegions().get(j).getUniqID());
                                    getRegionsMap().get(j).makeColorRegion(strategyLogic.getMap().getRegions().get(j).getTeam().getColor(),
                                            getRegionsMasks().get(j));
                                }
                                break;
                            }
                        }
                        unselectAllRegions();
                        unselectAllGenerals();
                        sprite.setSelected(!isSelected);
                        return true;
                    }

                } else {
                    lmbPressed = false;
                }
            } else {
                if (!sprite.isSelected()) {
                    sprite.setLookedFor(true);
                }

                if (Framework.CurGameTime() < menuDelay) {
                    return false;
                }

                int k;
                for (k = 0; k < getRegionsMap().size(); k++) {
                    if (getRegionsMap().get(k).isSelected()) {
                        break;
                    }
                }

                if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
                    menuDelay = Framework.CurGameTime() + 50;

                    if (lmbPressed) {
                        return false;
                    }

                    lmbPressed = true;
                    if (k != getRegionsMap().size()) {
                        for (int i = 0; i < getButtons().size(); i++) {
                            if (getButtons().get(i) == sprite) {
                                switch (i) {
                                case 0:
                                    strategyLogic.endOfTurn(strategyLogic.playerTeamID);
                                    break;
                                case 2:
                                    strategyLogic.getMap().getRegions().get(k).develop("people");
                                    break;
                                case 3:
                                    strategyLogic.getMap().getRegions().get(k).develop("money");
                                    break;
                                case 4:
                                    strategyLogic.getMap().getRegions().get(k).fortify();
                                    break;
                                }
                                break;
                            }
                        }
                    } else {
                        if (getButtons().get(0) == sprite) {
                            strategyLogic.endOfTurn(strategyLogic.playerTeamID);
                        }
                        if (getButtons().get(1) == sprite) {
                            buying = true;
                        }
                    }

                    unselectAllRegions();
                    unselectAllGenerals();
                    sprite.setSelected(false);
                    return true;

                } else {
                    lmbPressed = false;
                }
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

        } else if (Canvas.mouseButtonState(MouseEvent.BUTTON3)) { // ПКМ

        } else if (Canvas.mouseButtonState(MouseEvent.BUTTON2)) { // СКМ

        } else if (!Canvas.mouseButtonState(MouseEvent.BUTTON1)) {

        }
    }

    private void updateParticles() {
        for (int i = 0; i < allParticles.size(); i++) {
            if (!allParticles.get(i).active)
                allParticles.remove(i);
        }
    }

    public int getWINDOW_SIZE_Y() {
        return WINDOW_MAP_SIZE_Y;
    }

    public int getWINDOW_SIZE_X() {
        return WINDOW_MAP_SIZE_X;
    }

    public int getWINDOW_OFFSET_X() {
        return WINDOW_OFFSET_X;
    }

    public int getWINDOW_OFFSET_Y() {
        return WINDOW_OFFSET_Y;
    }

    public Game getStrategyLogic() {
        return strategyLogic;
    }

    public void setStrategyLogic(Game strategyLogic) {
        this.strategyLogic = strategyLogic;
    }

    public ArrayList<StrategicMapSprite> getRegionsMasks() {
        return regionsMasks;
    }

    public void setRegionsMasks(ArrayList<StrategicMapSprite> regionsMasks) {
        this.regionsMasks = regionsMasks;
    }

}
