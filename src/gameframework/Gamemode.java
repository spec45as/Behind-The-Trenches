package gameframework;

import gameframework.entities.Entity;
import gameframework.entities.Flag;
import gameframework.entities.PlayerSpectator;
import gameframework.libs.Animation;
import gameframework.libs.GuiCanvasSelection;
import gameframework.libs.SoundPlayer;
import gameframework.libs.Team;
import gameframework.libs.ai.AiTargetingMap;
import gameframework.libs.other.BitMap;
import gameframework.libs.other.Resources;
import gameframework.libs.other.StrategicMapSprite;
import gameframework.libs.other.mapSprite;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import strategicLogic.Game;

public abstract class Gamemode {

    public int getMapSizeX() {
        return 0;
    }

    public int getMapSizeY() {
        return 0;
    }

    public void particleCreate(Animation particle) {
        return;
    }

    public void particleRemove(Animation particle) {
        return;
    }

    public void entCreate(Entity newEnt) {
        return;
    }

    public void entRemove(Entity ent) {
        return;
    }

    public Resources getGameResources() {
        return null;
    }

    public void setGameResources(Resources gameResources) {
        return;
    }

    public ArrayList<Entity> getEntsArray() {
        return null;
    }

    public PlayerSpectator getPlayerCamera() {
        return null;
    }

    public Gamemode(Framework framework) {
    }

    public Gamemode() {
    }

    public void UpdateGame(long gameTime, Point mousePosition) {
    }

    public void Draw(Graphics2D g2d, Point mousePosition, long gameTime) {

    }

    public Framework getGameFramework() {
        return null;
    }

    public SoundPlayer getSoundPlayer() {
        return null;
    }

    public ArrayList<mapSprite> getMapSpritesArray() {
        return null;
    }

    public static final int DIRT_SPRITE_SIZE = 32;
    public static final int SPRITE_SIZE = 32;

    public int getPlayerZoneX() {
        return 0;
    }

    public int getPlayerZoneY() {
        return 0;
    }

    public static String getCountryID(int index) {
        return null;
    }

    public BitMap gettrenchesBitMap() {
        return null;
    }

    public Point getCustomMouseCursorPos() {
        return null;
    }

    public GuiCanvasSelection getActiveSelection() {
        return null;
    }

    public void setActiveSelection(GuiCanvasSelection activeSelection) {
        return;
    }

    public Team team1;
    public Team team2;

    public ArrayList<Flag> getMapFlags() {
        return null;
    }

    public boolean isPlayerPrepearing = true;

    public void updateAiLogic() {
        return;

    }

    public void RestartGame() {
        return;
    }

    public AiTargetingMap getAiTargetingMap() {
        return null;
    }

    public void setAiTargetingMap(AiTargetingMap aiTargetingMap) {
        return;
    }

    public int getWINDOW_OFFSET_X() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getWINDOW_SIZE_X() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getWINDOW_OFFSET_Y() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getWINDOW_SIZE_Y() {
        // TODO Auto-generated method stub
        return 0;
    }

    public Game getStrategyLogic() {
        return null;
    }

    public void setStrategyLogic(Game strategyLogic) {
        return;
    }

    public ArrayList<StrategicMapSprite> getRegionsMasks() {
        return null;
    }

    public void setRegionsMasks(ArrayList<StrategicMapSprite> regionsMasks) {
        return;
    }
    
}
