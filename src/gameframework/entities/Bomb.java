package gameframework.entities;

import gameframework.Framework;
import gameframework.libs.Animation;
import gameframework.libs.DamageInfo;
import gameframework.libs.Ents;
import gameframework.libs.other.Vector;
import gameframework.libs.other.VectorLight;
import gameframework.libs.other.mapSprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Bomb extends Entity {

    private Vector movingSpeed;
    double rotationRequired;
    private VectorLight targetPos;
    private Vector accuracyMovements = new Vector(0, 0, 0);

    public Bomb(VectorLight shootPos, VectorLight targetPos) {
        super();
        this.pos = shootPos;
        movingSpeed = new Vector();
        this.targetPos = targetPos;
        setDirectionAndSpeed();
        accuracyMovements.x = shootPos.x;
        accuracyMovements.y = shootPos.y;
        accuracyMovements.z = shootPos.z;
    }

    private double MAGIC_GRAVITY = 0.1;

    private void setDirectionAndSpeed() {

        double directionVx = targetPos.x - GetPos().x;
        double directionVy = targetPos.y - GetPos().y;
        double lengthOfVector = Math.sqrt(directionVx * directionVx + directionVy * directionVy);
        directionVx = directionVx / lengthOfVector;
        directionVy = directionVy / lengthOfVector;

        rotationRequired = Math.signum(directionVy) * Math.acos(directionVx);

        double speed = 0;
        if ((targetPos.y - pos.y) < -Math.abs(pos.x - targetPos.x)) {
            movingSpeed.x = 10000.0 * directionVx / lengthOfVector;
            movingSpeed.y = 10000.0 * directionVy / lengthOfVector;
            MAGIC_GRAVITY = 0;
        } else {
            speed = Math.sqrt((MAGIC_GRAVITY / 2.0 / (Math.abs(pos.x - targetPos.x) + (targetPos.y - pos.y))))
                    * Math.abs(pos.x - targetPos.x);
            movingSpeed.x = -speed * Math.signum(pos.x - targetPos.x);
            movingSpeed.y = -Math.abs(movingSpeed.x);
        }

        movingSpeed.z = 0;
    }

    public boolean isItLeftScreen() {
        VectorLight modelPos = GetPos();
        if (modelPos.x > 0 && modelPos.x < Framework.activeGame().getMapSizeX())
            return false;
        else
            this.Remove();
        return true;
    }

    public void Update() {
        explode();
        accuracyMovements.x += movingSpeed.x;
        accuracyMovements.y += movingSpeed.y;
        accuracyMovements.z += movingSpeed.z;

        pos.x = (int) accuracyMovements.x;
        pos.y = (int) accuracyMovements.y;
        pos.z = (int) accuracyMovements.z;

        movingSpeed.y = movingSpeed.y + MAGIC_GRAVITY;

    }

    public void explode() {
        if (VectorLight.distanceApproximation2D(targetPos, GetPos()) < 256) {
            Animation explosion = new Animation("mortar_explosion",
                    gameframework.Framework.activeGame().getGameResources().mortarShellExplosion, 64, 64, 2,
                    (int) (160 + 10 * (Math.random() - Math.random())), false, pos.x - 32, pos.y - 62, 0, 1, false);
            gameframework.Framework.activeGame().particleCreate(explosion);

            mapSprite explosionRes = new mapSprite(pos.x - 17, pos.y - 20, Framework.activeGame().getGameResources().mortarExplosionSprite,
                    false);
            gameframework.Framework.activeGame().getMapSpritesArray().add(explosionRes);

            ArrayList<Entity> victims = Ents.getAllEntsInSphere(GetPos(), 100);
            for (Entity victim : victims) {
                if (victim.getClass().getName().equals("gameframework.entities.Human")) {
                    double mod = Math.max(0, Math.min(1, VectorLight.distanceApproximation2D(victim.pos, GetPos()) / 128));
                    victim.takeDamage(new DamageInfo(null, victim, mod * 500));
                }
            }

            this.Remove();
        }
    }

    public void Draw(Graphics2D g2d) {
        VectorLight modelPos = GetPos();
        BufferedImage bulletImg = gameframework.Framework.activeGame().getGameResources().mortarShell;
        double locationX = bulletImg.getWidth() / 2;
        double locationY = bulletImg.getHeight() / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        g2d.drawImage(op.filter(bulletImg, null), (int) modelPos.x, (int) modelPos.y, null);
        g2d.setColor(new Color(40, 160, 160, 255));

    }
}
