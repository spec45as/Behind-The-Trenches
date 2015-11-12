package gameframework.entities;

import gameframework.Framework;
import gameframework.libs.other.VectorLight;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class DeadBody extends Entity {

    // От начала изображения до положения в центре ног
    private static final int offsetByXforXYCoordinates = 56;
    private static final int offsetByYforXYCoordinates = 90;

    private BufferedImage model;
    private boolean isFliped;

    public DeadBody(BufferedImage playerModelDeathImage) {
        this.model = playerModelDeathImage;
        Initialize();
    }

    private void Initialize() {
    }

    public VectorLight GetPos() {
        return pos;
    }

    public void setFlipped(boolean isFlipped) {
        this.isFliped = isFlipped;
    }

    public void Update() {
    }

    public void Draw(Graphics2D g2d) {
        if (Framework.activeGame().getPlayerCamera().canSeeSprite(pos.x, pos.y, 128, 128)) {

            if (!this.isFliped) {
                g2d.drawImage(model, pos.x - offsetByXforXYCoordinates, pos.y - offsetByYforXYCoordinates, pos.x
                        - offsetByXforXYCoordinates + 128, pos.y + model.getHeight() - offsetByYforXYCoordinates, 384, 0, 512, 128, null);
            } else {
                g2d.drawImage(model, pos.x - offsetByXforXYCoordinates + 128 - 13, pos.y - offsetByYforXYCoordinates, pos.x
                        - offsetByXforXYCoordinates - 13, pos.y + model.getHeight() - offsetByYforXYCoordinates, 384, 0, 512, 128, null);

            }

        }
    }
}
