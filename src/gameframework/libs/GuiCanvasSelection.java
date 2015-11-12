package gameframework.libs;

import gameframework.Framework;
import gameframework.entities.Entity;
import gameframework.entities.Human;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class GuiCanvasSelection {
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private boolean targetingMode;
    private boolean runningMode;
    private ArrayList<Entity> selectedEnts = new ArrayList<Entity>();

    public GuiCanvasSelection(int x1, int y1, int x2, int y2) {
        super();
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public boolean isEmpty() {
        for (Entity curEnt : selectedEnts) {
            if (curEnt.IsValid()) {
                return false;
            }
        }
        return true;
    }

    public void selectUnits() {
        selectedEnts.clear();
        int x1 = getX1(), x2 = getX2(), y1 = getY1(), y2 = getY2();

        if (getX2() - getX1() <= 0) {
            x1 = getX2();
            x2 = getX1();
        }

        if (getY2() - getY1() <= 0) {
            y1 = getY2();
            y2 = getY1();
        }

        for (Entity curTarget : Framework.activeGame().getEntsArray()) {
            if (curTarget.getClass().getSimpleName().equals("Human")) {
                if (((Human) curTarget).getAI().getTeam() == 0) {
                    if ((x1 < curTarget.GetPos().x) && ((x2 > curTarget.GetPos().x))) {
                        if ((y1 < curTarget.GetPos().y) && ((y2 > curTarget.GetPos().y))) {
                            selectedEnts.add(curTarget);
                        }
                    }
                }

            }
        }
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x) {
        this.x1 = x;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y) {
        this.y1 = y;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x) {
        this.x2 = x;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y) {
        this.y2 = y;
    }

    public ArrayList<Entity> getSelectedEnts() {
        return selectedEnts;
    }

    public void setSelectedEnts(ArrayList<Entity> selectedEnts) {
        this.selectedEnts = selectedEnts;
    }

    public void Draw(Graphics2D g2d) {

        int x1 = getX1(), x2 = getX2(), y1 = getY1(), y2 = getY2();

        if (getX2() - getX1() <= 0) {
            x1 = getX2();
            x2 = getX1();
        }

        if (getY2() - getY1() <= 0) {
            y1 = getY2();
            y2 = getY1();
        }

        g2d.setColor(new Color(255, 240, 32, 160));
        g2d.drawRect(x1, y1, x2 - x1, y2 - y1);
    }

    public boolean isTargetingMode() {
        return targetingMode;
    }

    public void setTargetingMode(boolean targetingMode) {
        this.targetingMode = targetingMode;
    }

    public boolean isRunningMode() {
        return runningMode;
    }

    public void setRunningMode(boolean runningMode) {
        this.runningMode = runningMode;
    }

}
