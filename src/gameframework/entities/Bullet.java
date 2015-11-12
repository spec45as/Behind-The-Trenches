package gameframework.entities;

import gameframework.Framework;
import gameframework.libs.other.Vector;
import gameframework.libs.other.VectorLight;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Bullet extends Entity {

    private Vector movingSpeed;
    private int bulletSpeed;
    double rotationRequired;

    public boolean isPhysicsObject = false;

    public Bullet(VectorLight shootingPos, VectorLight targetPos, int bulletSpeed) {
        super();
        this.pos = shootingPos;
        this.bulletSpeed = bulletSpeed;
        movingSpeed = new Vector();
        setDirectionAndSpeed(targetPos);
    }

    private void setDirectionAndSpeed(Vector targetPos) {
        // Unit direction vector of the bullet.
        double directionVx = targetPos.x - this.GetPos().x;
        double directionVy = targetPos.y - this.GetPos().y;

        double lengthOfVector = Math.sqrt(directionVx * directionVx + directionVy * directionVy);
        directionVx = directionVx / lengthOfVector; // Unit vector
        directionVy = directionVy / lengthOfVector; // Unit vector

        rotationRequired = Math.signum(directionVy) * Math.acos(directionVx);// Math.toRadians(45);

        // Set speed.
        this.movingSpeed.x = bulletSpeed * directionVx;
        this.movingSpeed.y = bulletSpeed * directionVy;
        this.movingSpeed.z = 0;
    }

    public boolean isItLeftScreen() {
        Vector modelPos = this.GetPos();
        if (modelPos.x > 0 && modelPos.x < Framework.activeGame().getMapSizeX() && modelPos.y > 0
                && modelPos.y < Framework.activeGame().getMapSizeY())
            return false;
        else
            return true;
    }

    public void Update() {
        if (isItLeftScreen()) {
            Remove();
        }
        pos.x += movingSpeed.x;
        pos.y += movingSpeed.y;
        pos.z += movingSpeed.z;
    }

    public void Draw(Graphics2D g2d) {
        if (Framework.activeGame().getPlayerCamera().canSeeSprite((int) GetPos().x, (int) GetPos().y, 1, 1)) {

            Vector modelPos = GetPos();
            BufferedImage bulletImg = gameframework.Framework.activeGame().gameResources.rifleBullet;
            double locationX = bulletImg.getWidth() / 2;
            double locationY = bulletImg.getHeight() / 2;
            AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

            // Drawing the rotated image at the required drawing locations
            g2d.drawImage(op.filter(bulletImg, null), (int) modelPos.x, (int) modelPos.y, null);
            g2d.setColor(new Color(40, 160, 160, 255));
        }

    }
}
