package gameframework.libs.other;

import gameframework.Framework;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class mapSprite {
    private int x;
    private int y;
    private int sizeX;
    private int sizeY;
    private boolean isConsequence = false;
    private float alpha = 1.0f;

    private int x1 = 0;
    private int y1 = 0;
    private int x2 = 0;
    private int y2 = 0;

    private boolean isFlipped;
    private BufferedImage texture;

    public mapSprite(int x, int y, BufferedImage texture, boolean isFlipped) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.sizeX = texture.getWidth();
        this.sizeY = texture.getHeight();
        this.setFlipped(isFlipped);
    }

    public mapSprite(int x, int y, BufferedImage texture, boolean isFlipped, float alpha) {
        this(x, y, texture, isFlipped);
        this.alpha = alpha;
    }

    public mapSprite(int x, int y, int x1, int y1, int x2, int y2, BufferedImage texture, boolean isFlipped) {
        this.x = x;
        this.y = y;

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        this.isConsequence = true;
        this.texture = texture;
        this.sizeX = x2 - x1;
        this.sizeY = y2 - y1;
        this.setFlipped(isFlipped);
    }

    public mapSprite(int x, int y, int x1, int y1, int x2, int y2, BufferedImage texture, boolean isFlipped, float alpha) {
        this(x, y, x1, y1, x2, y2, texture, isFlipped);
        this.alpha = alpha;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public void setTexture(BufferedImage texture) {
        this.texture = texture;
    }

    public void Draw(Graphics2D g2d) {
        if (Framework.activeGame().getPlayerCamera().canSeeSprite(getX(), getY(), getSizeX(), getSizeY())) {
            if (alpha != 1.0) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            }
            if (!isConsequence) {
                if (!isFlipped) {
                    g2d.drawImage(texture, getX(), getY(), getX() + getSizeX(), getY() + getSizeY(), 0, 0, getSizeX(), getSizeY(), null);
                } else {
                    g2d.drawImage(texture, getX() + getSizeX(), getY(), getX(), getY() + getSizeY(), 0, 0, getSizeX(), getSizeY(), null);
                }
            } else {
                if (!isFlipped) {
                    g2d.drawImage(texture, getX(), getY(), getX() + getSizeX(), getY() + getSizeY(), x1, y1, x2, y2, null);
                } else {
                    g2d.drawImage(texture, getX() + getSizeX(), getY(), getX(), getY() + getSizeY(), x1, y1, x2, y2, null);
                }
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

        }
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean isFlipped) {
        this.isFlipped = isFlipped;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }
}
