package gameframework.libs.other;

import gameframework.Framework;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.*;

public class StrategicMapSprite {
    private int x;
    public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	private int y;
    private int sizeX;
    private int sizeY;
    private boolean isFlipped;
    private BufferedImage texture;
    private BufferedImage colorTexture;
    private boolean isSelected;
    private boolean isLookedFor;

    private float imageAlpha = 1f;

    public StrategicMapSprite(int x, int y, BufferedImage texture, boolean isFlipped) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.sizeX = texture.getWidth();
        this.sizeY = texture.getHeight();
        this.setFlipped(isFlipped);
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

    public void DrawButton (Graphics2D g2d) {

        if (!isSelected && (imageAlpha < 1)) {
            imageAlpha = Math.min(imageAlpha + 0.1f, 1f);
        }
        
        if (isSelected && (imageAlpha > 0.2)) {
            imageAlpha = Math.max(imageAlpha - 0.1f, 0.2f);
        }

        if (isLookedFor()) {
            imageAlpha = 0.75f;
        }

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, imageAlpha));

        g2d.drawImage(texture, getX(), getY(), getX() + (int)(getSizeX()*Framework.getALPHA_X()), getY() + (int)(getSizeY()*Framework.getALPHA_Y()), 0, 0, getSizeX(), getSizeY(), null);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
    }
    
    public void DrawRegionBorders(Graphics2D g2d, int cameraX, int cameraY) {

        if (!isSelected && (imageAlpha < 0.4)) {
            imageAlpha = Math.min(imageAlpha + 0.05f, 1f);
        }
        
        if (isSelected && (imageAlpha > 0.05)) {
            imageAlpha = Math.max(imageAlpha - 0.05f, 0.2f);
        }

        if (isLookedFor()) {
            imageAlpha = 0.75f;
        }

        if (Framework.activeGame().getPlayerCamera().canSeeStrategicMapSprite(x, y, sizeX, sizeY)) {

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, imageAlpha));

            g2d.drawImage(texture, getX() - cameraX, getY() - cameraY, null);
            
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        }
    }
    
    public void makeColorRegion (String color, StrategicMapSprite mask) {
    	colorTexture = null;
    	colorTexture = new BufferedImage(getSizeX(),
                getSizeY(),
                BufferedImage.TYPE_INT_ARGB);

    	Graphics2D g2 = colorTexture.createGraphics();
    	g2.setComposite(AlphaComposite.Clear);
    	g2.fillRect(0, 0, colorTexture.getWidth(), colorTexture.getHeight());

    	g2.setComposite(AlphaComposite.Src);
    	g2.drawImage(texture, 0, 0, null);
    	g2.setComposite(BlendComposite.getInstance(BlendComposite.BlendingMode.valueOf(color), imageAlpha));
    	g2.drawImage(mask.getTexture(), 0, 0, null);
    	g2.dispose();

    }
    
    public void DrawRegion(Graphics2D g2d, int cameraX, int cameraY) {

        if (!isSelected && (imageAlpha < 1)) {
            imageAlpha = Math.min(imageAlpha + 0.1f, 1f);
        }
        
        if (isSelected && (imageAlpha > 0.2)) {
            imageAlpha = Math.max(imageAlpha - 0.1f, 0.2f);
        }

        if (isLookedFor()) {
            imageAlpha = 0.75f;
        }

        if (Framework.activeGame().getPlayerCamera().canSeeStrategicMapSprite(x, y, sizeX, sizeY)) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, imageAlpha));

            //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, imageAlpha));
            
            g2d.drawImage(colorTexture, getX() - cameraX, getY() - cameraY, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        }
    }
    
    public void DrawGeneral (Graphics2D g2d, int cameraX, int cameraY) {
    	if (isLookedFor()) {
    		imageAlpha = 0.8f;
    	}
    	else {
    		imageAlpha = Math.min(imageAlpha + 0.1f, 1f);
    	}
    	
    	if (Framework.activeGame().getPlayerCamera().canSeeStrategicMapSprite(x, y, sizeX, sizeY)) {

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, imageAlpha));

            
            g2d.drawImage(texture, getX() - cameraX, getY() - cameraY, getX() - cameraX + (int)(getSizeX()*Framework.getALPHA_X()), getY() - cameraY + (int)(getSizeY()*Framework.getALPHA_Y()), 0, 0, getSizeX(), getSizeY(), null);
            
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        }
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean isFlipped) {
        this.isFlipped = isFlipped;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public float getImageAlpha() {
        return imageAlpha;
    }

    public void setImageAlpha(float imageAlpha) {
        this.imageAlpha = imageAlpha;
    }

    public boolean isLookedFor() {
        if (isSelected) {
            return false;
        }
        return isLookedFor;
    }

    public void setLookedFor(boolean isLookedFor) {
        this.isLookedFor = isLookedFor;
    }
}
