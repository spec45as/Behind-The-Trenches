package gameframework.libs;

import gameframework.Framework;
import gameframework.entities.Entity;
import gameframework.entities.Human;

import java.awt.Point;

public class Shooting {
    public static Entity getShootingTargetFromAbsolutePos(Point Position) {
        try {
            for (Entity curTarget : Framework.activeGame().getEntsArray()) {
                if (curTarget.getClass().getSimpleName().equals("Human")) {

                    HitBox humanHitBox = ((Human) curTarget).getHitBox();

                    double x = Position.getX();
                    double y = Position.getY();

                    if ((x < curTarget.GetPos().x + humanHitBox.getHitBoxX2()) && ((x > curTarget.GetPos().x + humanHitBox.getHitBoxX1()))) {
                        if ((y < curTarget.GetPos().y + humanHitBox.getHitBoxY2())
                                && ((y > curTarget.GetPos().y + humanHitBox.getHitBoxY1()))) {
                            return curTarget;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;

    }

    public static Entity getShootingTargetFromLocalMousePos(Point mousePosition) {
        double mouseX = mousePosition.getX() + Framework.activeGame().getPlayerCamera().getX();
        double mouseY = mousePosition.getY() + Framework.activeGame().getPlayerCamera().getY();
        return getShootingTargetFromAbsolutePos(new Point((int) mouseX, (int) mouseY));
    }

}
