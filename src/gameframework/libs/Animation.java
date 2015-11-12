package gameframework.libs;

import gameframework.Framework;
import gameframework.libs.other.VectorLight;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Animation {

    private BufferedImage animImage;
    private int frameWidth;
    private int frameHeight;
    private int numberOfFrames;
    // in milliseconds
    private long frameTime;
    private long startingFrameTime;
    private long timeForNextFrame;
    private int currentFrameNumber;
    private boolean loop;

    public int x;
    public int y;

    private int startingXOfFrameInImage;
    private int endingXOfFrameInImage;

    public boolean active;

    // In milliseconds
    private long showDelay;

    private long timeOfAnimationCreation;
    private int defaultFrame;
    private boolean isFlipped;
    private String animName;
    private boolean isRecentlyActive;

    /**
     * @param animImage
     * @param frameWidth
     * @param frameHeight
     * @param numberOfFrames
     * @param frameTime
     * @param loop
     * @param x
     * @param y
     * @param showDelay
     */
    public Animation(String animName, BufferedImage animImage, int frameWidth, int frameHeight, int numberOfFrames, long frameTime,
            boolean loop, int x, int y, long showDelay, int defaultFrame, boolean isFlipped) {
        this.animName = animName;
        this.animImage = animImage;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.numberOfFrames = numberOfFrames;
        this.frameTime = frameTime;
        this.loop = loop;
        this.defaultFrame = defaultFrame;
        this.isFlipped = isFlipped;

        this.x = x;
        this.y = y;

        this.showDelay = showDelay;

        timeOfAnimationCreation = System.currentTimeMillis();

        startingXOfFrameInImage = 0;
        endingXOfFrameInImage = frameWidth;

        startingFrameTime = System.currentTimeMillis() + showDelay;
        timeForNextFrame = startingFrameTime + this.frameTime;
        currentFrameNumber = 0;
        active = true;
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
    }

    public void setFlipped(boolean isFlipped) {
        this.isFlipped = isFlipped;
    }

    public int getCurrentFrame() {
        return currentFrameNumber;
    }

    public String getAnimName() {
        return animName;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public BufferedImage getImage() {
        return animImage;
    }

    public int getNumberOfFrames() {
        return numberOfFrames;
    }

    public void changeCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void changeCoordinates(VectorLight pos) {
        this.x = (int) pos.x;
        this.y = (int) pos.y;

    }

    public boolean isPreviousFrameActive() {
        return isRecentlyActive;
    }

    private void Update() {
        if (active) {
            isRecentlyActive = true;

            if (timeForNextFrame <= System.currentTimeMillis()) {
                // anim next frame
                currentFrameNumber++;

                // anim rest
                if (currentFrameNumber >= numberOfFrames) {
                    if (!loop)
                        currentFrameNumber = defaultFrame;
                    else
                        currentFrameNumber = 0;

                    if (!loop)
                        active = false;
                }

                startingXOfFrameInImage = currentFrameNumber * frameWidth;
                endingXOfFrameInImage = startingXOfFrameInImage + frameWidth;

                // next frame time
                startingFrameTime = System.currentTimeMillis();
                timeForNextFrame = startingFrameTime + frameTime;
            }
        } else {
            isRecentlyActive = false;
            currentFrameNumber = defaultFrame;
            startingXOfFrameInImage = currentFrameNumber * frameWidth;
            endingXOfFrameInImage = startingXOfFrameInImage + frameWidth;

        }
    }

    public void Draw(Graphics2D g2d) {
        this.Update();
        if (Framework.activeGame().getPlayerCamera().canSeeSprite(x, y, frameWidth, frameHeight)) {
            if (this.timeOfAnimationCreation + this.showDelay <= System.currentTimeMillis()) {
                if (!this.isFlipped()) {
                    g2d.drawImage(animImage, x, y, x + frameWidth, y + frameHeight, startingXOfFrameInImage, 0, endingXOfFrameInImage,
                            frameHeight, null);
                } else {
                    // modificators
                    if (!this.getAnimName().contains("WALK") && !this.getAnimName().contains("SIT") && !this.getAnimName().contains("RUN")
                            && !this.getAnimName().contains("SHOOT") && !this.getAnimName().contains("LYING")
                            && !this.getAnimName().contains("DEATH")) {
                        g2d.drawImage(animImage, x + frameWidth, y, x, y + frameHeight, startingXOfFrameInImage, 0, endingXOfFrameInImage,
                                frameHeight, null);
                    } else {
                        g2d.drawImage(animImage, x + frameWidth - 13, y, x - 13, y + frameHeight, startingXOfFrameInImage, 0,
                                endingXOfFrameInImage, frameHeight, null);
                    }
                }
            }
        }
    }

}