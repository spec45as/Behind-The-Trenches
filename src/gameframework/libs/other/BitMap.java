package gameframework.libs.other;

import gameframework.Framework;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BitMap {
    private short byteMap[][];
    private mapSprite[][] spriteMap;

    private ArrayList<BufferedImage> textures;
    private int spriteSize;
    private Framework framework;

    public BitMap(int amountOfSpritesX, int amountOfSpritesY, int spriteSize, ArrayList<BufferedImage> textures, Framework framework) {
        byteMap = new short[amountOfSpritesX + 1][amountOfSpritesY + 1];
        this.spriteMap = new mapSprite[amountOfSpritesX][amountOfSpritesY];
        this.framework = framework;
        this.textures = textures;
        this.spriteSize = spriteSize;
        calculateSprites();
    }

    public void placeByte(int x, int y) {
        if (x > 0 && x < byteMap.length) {
            if (y > 0 && y < byteMap[0].length) {
                byteMap[x][y] = 1;
            }
        }
    }

    public void removeByte(int x, int y) {
        if (x > 0 && x < byteMap.length) {
            if (y > 0 && y < byteMap[0].length) {
                byteMap[x][y] = 0;
            }
        }
    }

    public void generateTrenches() {
        int generatingx = -1;
        boolean mustGenerateNext = false;
        int minY = 0, maxY = 0;
        for (int i = 1; i < byteMap.length - 1; i++) {
            if (generatingx != i) {
                if (generatingx == i - 1) {
                    if (Math.random() > 0.8 || mustGenerateNext) {
                        generatingx = i;
                        minY = Math.max(minY + Framework.genRand(-2, 2), 0);
                        maxY = Math.min(maxY + Framework.genRand(-2, 2), byteMap[0].length - 1);
                        mustGenerateNext = false;
                    }
                } else {
                    if (Math.random() > 0.9) {
                        generatingx = i;
                        minY = Framework.genRand(0, byteMap[0].length / 2);
                        maxY = Framework.genRand(byteMap[0].length / 2, byteMap[0].length - 1);
                        mustGenerateNext = true;
                    }
                }
            }

            if (generatingx == i) {
                for (int j = minY; j < maxY; j++) {
                    byteMap[i][j] = 1;
                }
            }
        }

        calculateSprites();

    }

    public void generateGrass() {

        for (int i = 0; i < byteMap.length; i++) {
            for (int j = 0; j < byteMap[0].length; j++) {
                if (Math.random() > 0.65) { // 96 norm i 55
                    byteMap[i][j] = 1;
                }
            }
        }

        for (int r = 0; r < 1; r++) {
            for (int i = 1; i < byteMap.length - 1; i++) {
                for (int j = 1; j < byteMap[0].length; j++) {

                    if (byteMap[i][j] == 1) {
                        int randomSeed = (int) (Math.random() * 10);
                        try {
                            switch (randomSeed) {
                            // case 0:
                            case 8: // up
                                byteMap[i][j - 1] = 1;
                                break;
                            // case 1:
                            case 9: // down
                                byteMap[i][j + 1] = 1;
                                break;
                            case 2: // left
                                byteMap[i - 1][j] = 1;
                                break;
                            case 3: // right
                                byteMap[i + 1][j] = 1;
                                break;
                            case 4: // U-L
                                byteMap[i - 1][j - 1] = 1;
                                break;
                            case 5: // U-R
                                byteMap[i + 1][j - 1] = 1;
                                break;
                            case 6: // D-L
                                byteMap[i - 1][j + 1] = 1;
                                break;
                            case 7: // D-R
                                byteMap[i + 1][j + 1] = 1;
                                break;
                            }
                        } catch (Exception e) {
                        }
                    }

                }
            }
        }
        calculateSprites();

    }

    public void calculateSprites() {
        for (int i = 0; i < byteMap.length - 1; i++) {
            for (int j = 0; j < byteMap[0].length - 1; j++) {
                String byteString = byteMap[i][j] + "" + byteMap[i + 1][j] + "" + byteMap[i + 1][j + 1] + "" + byteMap[i][j + 1];
                int spriteNum = Integer.parseInt(byteString, 2);
                if (spriteNum != 0)

                    if (spriteNum != 0) {
                        mapSprite sprite = new mapSprite(i * spriteSize, j * spriteSize, textures.get(spriteNum), false);
                        spriteMap[i][j] = sprite;

                    }
            }
        }
    }

    public int calculateHeight(double x, double y) {
        int minX = getMinX(x, y);
        int maxX = getMaxX(x, y);
        int minY = getMinY(x, y);
        int maxY = getMaxY(x, y);

        int corner1 = byteMap[minX][minY];
        int corner2 = byteMap[maxX][minY];
        int corner3 = byteMap[maxX][maxY];
        int corner4 = byteMap[minX][maxY];

        /*
         * x = (int) (x % spriteSize); y = 32 - (int) (y % spriteSize); int z;
         * 
         * int a = 0, b = spriteSize; // cor1 int i = spriteSize, j =
         * spriteSize; // cor2 int p = 0, q = 0; // cor4 int c = corner1, k =
         * corner2, r = corner4; z = 0;
         * 
         * if (corner1 == 0 && corner2 == 0 && corner4 == 0 && corner3 != 0) { i
         * = spriteSize; j = 0; k = corner3; // cor2 } else if (corner1 == -32
         * && corner2 == -32 && corner4 == -32 && corner3 == 0) { i =
         * spriteSize; j = 0; k = corner3; // cor2 } else if (corner1 == 0 &&
         * corner3 == 0 && corner2 != 0 && corner4 != 0) { return -32; } else if
         * (corner1 != 0 && corner3 != 0 && corner2 == 0 && corner4 == 0) {
         * return -32; }
         * 
         * z = (int) (-(c * j * p - b * k * p - c * i * q + a * k * q + b * i *
         * r - a * j * r - c * j * x + b * k * x + c * q * x - k * q * x - b * r
         * * x + j * r * x + c * i * y - a * k * y - c * p * y + k * p * y + a *
         * r * y - i * r * y) / (-b * i + a * j + b * p - j * p - a * q + i *
         * q));
         * 
         * return Math.min(z, 0);
         */

        int sum = corner1 + corner2 + corner3 + corner4;
        switch (sum) {
        case 0:
            return 0;
        case 1:
            return -8;
        case 2:
            return -16;
        case 3:
            return -24;
        case 4:
            return -32;
        default:
            return 0;
        }
    }

    public int getBitValue(int x, int y) {
        if (x >= 0 && x < byteMap.length) {
            if (y >= 0 && y < byteMap[0].length) {
                return byteMap[x][y];
            }
        }
        return -1;
    }

    public ArrayList<Point> findTrenches(int x, int y, int distance) {
        ArrayList<Point> positions = new ArrayList<Point>();
        for (int i = x - distance; i <= x + distance; i = i + spriteSize / 3) {
            for (int j = y - distance; j <= y + distance; j = j + spriteSize / 3) {
                if (calculateHeight(i, j) <= -32) {
                    positions.add(new Point(i, j));
                }
            }
        }
        return positions;
    }

    public int getMinX(double x, double y) {
        int nearestLeftX = (int) Math.min(Math.max(x / spriteSize, 0), byteMap.length - 1);
        return nearestLeftX;
    }

    public int getMinY(double x, double y) {
        int nearestUpY = (int) Math.min(Math.max(y / spriteSize, 0), byteMap[0].length - 1);
        return nearestUpY;
    }

    public int getMaxX(double x, double y) {
        int nearestRightX = (int) Math.max(Math.min(x / spriteSize + 1, byteMap.length - 1), 0);
        return nearestRightX;
    }

    public int getMaxY(double x, double y) {
        int nearestDownY = (int) Math.max(Math.min(y / spriteSize + 1, byteMap[0].length - 1), 0);
        return nearestDownY;
    }

    public void Draw(Graphics2D g2d, int playerCam[]) {

        int minGrassSpriteXnumber = Math.max(playerCam[0] / spriteSize, 0);
        int minGrassSpriteYnumber = Math.max(playerCam[1] / spriteSize, 0);
        int maxGrassSpriteXnumber = Math.min(spriteSize + (playerCam[0] + framework.getFrameWidth()) / spriteSize, spriteMap.length);
        int maxGrassSpriteYnumber = Math.min(spriteSize + (playerCam[1] + framework.getFrameHeight()) / spriteSize, spriteMap[0].length);

        for (int i = minGrassSpriteXnumber; i < maxGrassSpriteXnumber; i++) {
            for (int j = minGrassSpriteYnumber; j < maxGrassSpriteYnumber; j++) {
                if (spriteMap[i][j] != null) {
                    mapSprite curSprite = spriteMap[i][j];

                    if (Framework.activeGame().getPlayerCamera().canSeeSprite(curSprite.getX(), curSprite.getY(), spriteSize, spriteSize)) {

                        g2d.drawImage(curSprite.getTexture(), curSprite.getX(), curSprite.getY(), curSprite.getX() + spriteSize,
                                curSprite.getY() + spriteSize, 0, 0, spriteSize, spriteSize, null);
                    }
                }
            }
        }

    }
}
