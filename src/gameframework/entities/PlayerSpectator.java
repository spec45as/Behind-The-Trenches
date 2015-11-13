package gameframework.entities;

import gameframework.Framework;
import gameframework.Window;

import java.awt.Point;
import java.awt.event.KeyEvent;

public class PlayerSpectator {
    private int x;
    private int y;
    Framework framework;

    public PlayerSpectator(int x, int y, Framework framework) {
        this.x = x;
        this.y = y;
        this.framework = framework;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean canSeeStrategicMapSprite(int x, int y, int sizeX, int sizeY) {
        if ((this.getX() < x + sizeX) && ((this.getX() + framework.getFrameWidth()) > x)) {
            if ((this.getY() < y + sizeY) && ((this.getY() + framework.getFrameHeight()) > y)) {
                return true;
            }
        }

        return false;
    }

    public boolean canSeeSprite(int x, int y, int sizeX, int sizeY) {
        int mapLimitX = Framework.activeGame().getMapSizeX();
        int mapLimitY = Framework.activeGame().getMapSizeY();

        if (Framework.activeGame().isPlayerPrepearing) {
            mapLimitX = Framework.activeGame().getPlayerZoneX();
            mapLimitY = Framework.activeGame().getPlayerZoneY();
        }

        if ((this.getX() < x + sizeX) && ((this.getX() + mapLimitX) > x)) {
            if ((this.getY() < y + sizeY) && ((this.getY() + mapLimitY) > y)) {
                return true;
            }
        }

        return false;
    }

    public boolean canSeeMapSprite(int x, int y, int sizeX, int sizeY) {
        int mapLimitX = Framework.activeGame().getMapSizeX();
        int mapLimitY = Framework.activeGame().getMapSizeY();

        if (Framework.activeGame().isPlayerPrepearing) {
           // mapLimitX = Framework.activeGame().getPlayerZoneX();
            //mapLimitY = Framework.activeGame().getPlayerZoneY();
        }

        if ((this.getX() < x + sizeX) && ((this.getX() + mapLimitX) > x)) {
            if ((this.getY() < y + sizeY) && ((this.getY() + mapLimitY) > y)) {
                return true;
            }
        }

        return false;
    }

    public void Update(Point mousePosition) {
        if (Framework.activeGame().getClass().getSimpleName().equals("DemoBattle")) {
            return;
        }

        if (Framework.activeGame().getClass().getSimpleName().equals("StrategicMode")) {
            if ((mousePosition.x >= framework.getFrameWidth() - Window.getWindowBorderSizeX())
                    && (getX() + 16 <= Framework.activeGame().getMapSizeX() - Framework.activeGame().getWINDOW_OFFSET_X()
                            - Framework.activeGame().getWINDOW_SIZE_X())) {
                setX(getX() + 16);
            }
            if ((mousePosition.x <= 0) && (getX() + Framework.activeGame().getWINDOW_OFFSET_X() - 16 >= 0)) {
                setX(getX() - 16);
            }

            if ((mousePosition.y <= 0) && (getY() + Framework.activeGame().getWINDOW_OFFSET_Y() - 16 >= 0)) {
                setY(getY() - 16);
            }
            if ((mousePosition.y >= framework.getFrameHeight() - Window.getWindowBorderSizeY())
                    && (getY() + 16 <= Framework.activeGame().getMapSizeY() - Framework.activeGame().getWINDOW_OFFSET_Y()
                            - Framework.activeGame().getWINDOW_SIZE_Y())) {
                setY(getY() + 16);
            }
            return;
        }

        double mouseX;
        double mouseY;
        try {
            mouseX = mousePosition.x;
            mouseY = mousePosition.y;
        } catch (Exception e) {
            return;
        }

        if (mouseX == 0 && mouseY == 0) {
            return;
        }

        int mapLimitX = Framework.activeGame().getMapSizeX();
        int mapLimitY = Framework.activeGame().getMapSizeY();

        //if (Framework.activeGame().isPlayerPrepearing == true) {
            //mapLimitX = Framework.activeGame().getPlayerZoneX();
            //mapLimitY = Framework.activeGame().getPlayerZoneY();
        //}

        if (mouseX > framework.getFrameWidth() - 8) {
            if (getX() + framework.getFrameWidth() < mapLimitX) {
                setX(getX() + 16);
            }
        }
        if (mouseX < 1) {
            if (getX() > -16) {
                setX(getX() - 16);
            }
        }

        if (mouseY < 1) {
            if (getY() > -16) {
                setY(getY() - 16);
            }
        }
        if (mouseY >= framework.getFrameHeight() - 8) {
            if (getY() + framework.getFrameHeight() < mapLimitY) {
                setY(getY() + 16);
            }
        }

        if (Framework.keyboardKeyState(KeyEvent.VK_D) || Framework.keyboardKeyState(KeyEvent.VK_RIGHT)) {
            if (getX() + Framework.activeGame().getGameFramework().getFrameWidth() < mapLimitX) {
                setX(getX() + 16);
            }
        }
        if (Framework.keyboardKeyState(KeyEvent.VK_A) || Framework.keyboardKeyState(KeyEvent.VK_LEFT)) {
            if (getX() > -16) {
                setX(getX() - 16);
            }
        }
        if (Framework.keyboardKeyState(KeyEvent.VK_W) || Framework.keyboardKeyState(KeyEvent.VK_UP)) {
            if (getY() > -16) {
                setY(getY() - 16);
            }
        }
        if (Framework.keyboardKeyState(KeyEvent.VK_S) || Framework.keyboardKeyState(KeyEvent.VK_DOWN)) {
            if (getY() + Framework.activeGame().getGameFramework().getFrameHeight() < mapLimitY) {
                setY(getY() + 16);
            }
        }

    }

}
