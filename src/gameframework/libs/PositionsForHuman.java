package gameframework.libs;

import java.awt.Point;

public class PositionsForHuman {
    public int ByXforXYCoordinates;
    public int ByYforXYCoordinates;

    public int WeaponFlashByX_RIGHT_STAY;
    public int WeaponFlashByY_RIGHT_STAY;
    public int WeaponFlashByX_LEFT_STAY;
    public int WeaponFlashByY_LEFT_STAY;

    public int WeaponFlashByX_RIGHT_SIT;
    public int WeaponFlashByY_RIGHT_SIT;
    public int WeaponFlashByX_LEFT_SIT;
    public int WeaponFlashByY_LEFT_SIT;

    public int WeaponFlashByX_RIGHT_LIE;
    public int WeaponFlashByY_RIGHT_LIE;
    public int WeaponFlashByX_LEFT_LIE;
    public int WeaponFlashByY_LEFT_LIE;

    private int hitBoxX1_STAY;
    private int hitBoxX2_STAY;
    private int hitBoxY1_STAY;
    private int hitBoxY2_STAY;
    private int hitBoxZ_STAY;

    private int hitBoxX1_SIT;
    private int hitBoxX2_SIT;
    private int hitBoxY1_SIT;
    private int hitBoxY2_SIT;
    private int hitBoxZ_SIT;

    private int hitBoxX1_LIE;
    private int hitBoxX2_LIE;
    private int hitBoxY1_LIE;
    private int hitBoxY2_LIE;
    private int hitBoxZ_LIE;

    private HitBox hitBox_STAY;
    private HitBox hitBox_SIT;
    private HitBox hitBox_LIE;

    public PositionsForHuman(String humanID) {

        if (humanID.equals("rifleman_uk")) {

            ByXforXYCoordinates = 56;
            ByYforXYCoordinates = 90;

            WeaponFlashByX_RIGHT_STAY = 34;
            WeaponFlashByY_RIGHT_STAY = -45;
            WeaponFlashByX_LEFT_STAY = -66;
            WeaponFlashByY_LEFT_STAY = -45;

            WeaponFlashByX_RIGHT_SIT = 34;
            WeaponFlashByY_RIGHT_SIT = -39;
            WeaponFlashByX_LEFT_SIT = -64;
            WeaponFlashByY_LEFT_SIT = -39;

            WeaponFlashByX_RIGHT_LIE = 36;
            WeaponFlashByY_RIGHT_LIE = -22;
            WeaponFlashByX_LEFT_LIE = -68;
            WeaponFlashByY_LEFT_LIE = -22;

            hitBoxX1_STAY = -12;
            hitBoxX2_STAY = 12;
            hitBoxY1_STAY = -36;
            hitBoxY2_STAY = 2;
            hitBoxZ_STAY = 64;

            hitBoxX1_SIT = -12;
            hitBoxX2_SIT = 12;
            hitBoxY1_SIT = -32;
            hitBoxY2_SIT = 0;
            hitBoxZ_SIT = 48;

            hitBoxX1_LIE = -20;
            hitBoxX2_LIE = 16;
            hitBoxY1_LIE = -12;
            hitBoxY2_LIE = 2;
            hitBoxZ_LIE = 32;

        } else if (humanID.equals("rifleman_ger")) {

            ByXforXYCoordinates = 56;
            ByYforXYCoordinates = 90;

            WeaponFlashByX_RIGHT_STAY = 34;
            WeaponFlashByY_RIGHT_STAY = -45;
            WeaponFlashByX_LEFT_STAY = -66;
            WeaponFlashByY_LEFT_STAY = -45;

            WeaponFlashByX_RIGHT_SIT = 34;
            WeaponFlashByY_RIGHT_SIT = -39;
            WeaponFlashByX_LEFT_SIT = -64;
            WeaponFlashByY_LEFT_SIT = -39;

            WeaponFlashByX_RIGHT_LIE = 36;
            WeaponFlashByY_RIGHT_LIE = -22;
            WeaponFlashByX_LEFT_LIE = -68;
            WeaponFlashByY_LEFT_LIE = -22;

            hitBoxX1_STAY = -12;
            hitBoxX2_STAY = 12;
            hitBoxY1_STAY = -36;
            hitBoxY2_STAY = 2;
            hitBoxZ_STAY = 64;

            hitBoxX1_SIT = -12;
            hitBoxX2_SIT = 12;
            hitBoxY1_SIT = -32;
            hitBoxY2_SIT = 0;
            hitBoxZ_SIT = 48;

            hitBoxX1_LIE = -20;
            hitBoxX2_LIE = 16;
            hitBoxY1_LIE = -12;
            hitBoxY2_LIE = 2;
            hitBoxZ_LIE = 32;

        } else if (humanID.equals("hand_mg_uk")) {

            ByXforXYCoordinates = 56;
            ByYforXYCoordinates = 90;

            WeaponFlashByX_RIGHT_STAY = 32;
            WeaponFlashByY_RIGHT_STAY = -35;
            WeaponFlashByX_LEFT_STAY = -54;
            WeaponFlashByY_LEFT_STAY = -35;

            WeaponFlashByX_RIGHT_SIT = 32;
            WeaponFlashByY_RIGHT_SIT = -31;
            WeaponFlashByX_LEFT_SIT = -64;
            WeaponFlashByY_LEFT_SIT = -31;

            WeaponFlashByX_RIGHT_LIE = 56;
            WeaponFlashByY_RIGHT_LIE = -30;
            WeaponFlashByX_LEFT_LIE = -90;
            WeaponFlashByY_LEFT_LIE = -30;

            hitBoxX1_STAY = -12;
            hitBoxX2_STAY = 12;
            hitBoxY1_STAY = -36;
            hitBoxY2_STAY = 2;
            hitBoxZ_STAY = 64;

            hitBoxX1_SIT = -12;
            hitBoxX2_SIT = 12;
            hitBoxY1_SIT = -32;
            hitBoxY2_SIT = 0;
            hitBoxZ_SIT = 48;

            hitBoxX1_LIE = -20;
            hitBoxX2_LIE = 16;
            hitBoxY1_LIE = -12;
            hitBoxY2_LIE = 2;
            hitBoxZ_LIE = 32;

        } else if (humanID.equals("hand_mg_ger")) {

            ByXforXYCoordinates = 56;
            ByYforXYCoordinates = 90;

            WeaponFlashByX_RIGHT_STAY = 37;
            WeaponFlashByY_RIGHT_STAY = -33;
            WeaponFlashByX_LEFT_STAY = -59;
            WeaponFlashByY_LEFT_STAY = -33;

            WeaponFlashByX_RIGHT_SIT = 37;
            WeaponFlashByY_RIGHT_SIT = -29;
            WeaponFlashByX_LEFT_SIT = -69;
            WeaponFlashByY_LEFT_SIT = -29;

            WeaponFlashByX_RIGHT_LIE = 61;
            WeaponFlashByY_RIGHT_LIE = -28;
            WeaponFlashByX_LEFT_LIE = -95;
            WeaponFlashByY_LEFT_LIE = -28;

            hitBoxX1_STAY = -12;
            hitBoxX2_STAY = 12;
            hitBoxY1_STAY = -36;
            hitBoxY2_STAY = 2;
            hitBoxZ_STAY = 64;

            hitBoxX1_SIT = -12;
            hitBoxX2_SIT = 12;
            hitBoxY1_SIT = -32;
            hitBoxY2_SIT = 0;
            hitBoxZ_SIT = 48;

            hitBoxX1_LIE = -20;
            hitBoxX2_LIE = 16;
            hitBoxY1_LIE = -12;
            hitBoxY2_LIE = 2;
            hitBoxZ_LIE = 32;

        } else if (humanID.equals("mortar_ger")) {

            ByXforXYCoordinates = 56;
            ByYforXYCoordinates = 90;

            WeaponFlashByX_RIGHT_STAY = 0;
            WeaponFlashByY_RIGHT_STAY = 0;
            WeaponFlashByX_LEFT_STAY = 0;
            WeaponFlashByY_LEFT_STAY = 0;

            WeaponFlashByX_RIGHT_SIT = 0;
            WeaponFlashByY_RIGHT_SIT = 0;
            WeaponFlashByX_LEFT_SIT = 0;
            WeaponFlashByY_LEFT_SIT = 0;

            WeaponFlashByX_RIGHT_LIE = -8;
            WeaponFlashByY_RIGHT_LIE = -56;
            WeaponFlashByX_LEFT_LIE = -22;
            WeaponFlashByY_LEFT_LIE = -56;

            hitBoxX1_STAY = -24;
            hitBoxX2_STAY = 18;
            hitBoxY1_STAY = -36;
            hitBoxY2_STAY = 2;
            hitBoxZ_STAY = 64;

            hitBoxX1_SIT = -32;
            hitBoxX2_SIT = 22;
            hitBoxY1_SIT = -36;
            hitBoxY2_SIT = 2;
            hitBoxZ_SIT = 48;

            hitBoxX1_LIE = -32;
            hitBoxX2_LIE = 22;
            hitBoxY1_LIE = -36;
            hitBoxY2_LIE = 2;
            hitBoxZ_LIE = 32;

        } else if (humanID.equals("mortar_uk")) {

            ByXforXYCoordinates = 56;
            ByYforXYCoordinates = 90;

            WeaponFlashByX_RIGHT_STAY = 0;
            WeaponFlashByY_RIGHT_STAY = 0;
            WeaponFlashByX_LEFT_STAY = 0;
            WeaponFlashByY_LEFT_STAY = 0;

            WeaponFlashByX_RIGHT_SIT = 0;
            WeaponFlashByY_RIGHT_SIT = 0;
            WeaponFlashByX_LEFT_SIT = 0;
            WeaponFlashByY_LEFT_SIT = 0;

            WeaponFlashByX_RIGHT_LIE = -18;
            WeaponFlashByY_RIGHT_LIE = -56;
            WeaponFlashByX_LEFT_LIE = -12;
            WeaponFlashByY_LEFT_LIE = -56;

            hitBoxX1_STAY = -24;
            hitBoxX2_STAY = 18;
            hitBoxY1_STAY = -36;
            hitBoxY2_STAY = 2;
            hitBoxZ_STAY = 64;

            hitBoxX1_SIT = -32;
            hitBoxX2_SIT = 22;
            hitBoxY1_SIT = -36;
            hitBoxY2_SIT = 2;
            hitBoxZ_SIT = 48;

            hitBoxX1_LIE = -32;
            hitBoxX2_LIE = 22;
            hitBoxY1_LIE = -36;
            hitBoxY2_LIE = 2;
            hitBoxZ_LIE = 32;

        } else if (humanID.equals("stat_mg_uk")) {
            ByXforXYCoordinates = 56;
            ByYforXYCoordinates = 90;

            WeaponFlashByX_RIGHT_STAY = 44;
            WeaponFlashByY_RIGHT_STAY = -42;
            WeaponFlashByX_LEFT_STAY = -76;
            WeaponFlashByY_LEFT_STAY = -42;

            WeaponFlashByX_RIGHT_SIT = 44;
            WeaponFlashByY_RIGHT_SIT = -36;
            WeaponFlashByX_LEFT_SIT = -76;
            WeaponFlashByY_LEFT_SIT = -36;

            WeaponFlashByX_RIGHT_LIE = 40;
            WeaponFlashByY_RIGHT_LIE = -42;
            WeaponFlashByX_LEFT_LIE = -72;
            WeaponFlashByY_LEFT_LIE = -42;

            hitBoxX1_STAY = -24;
            hitBoxX2_STAY = 18;
            hitBoxY1_STAY = -36;
            hitBoxY2_STAY = 2;
            hitBoxZ_STAY = 64;

            hitBoxX1_SIT = -32;
            hitBoxX2_SIT = 22;
            hitBoxY1_SIT = -36;
            hitBoxY2_SIT = 2;
            hitBoxZ_SIT = 48;

            hitBoxX1_LIE = -32;
            hitBoxX2_LIE = 22;
            hitBoxY1_LIE = -36;
            hitBoxY2_LIE = 2;
            hitBoxZ_LIE = 32;

        } else if (humanID.equals("stat_mg_ger")) {
            ByXforXYCoordinates = 56;
            ByYforXYCoordinates = 90;

            WeaponFlashByX_RIGHT_STAY = 44;
            WeaponFlashByY_RIGHT_STAY = -42;
            WeaponFlashByX_LEFT_STAY = -76;
            WeaponFlashByY_LEFT_STAY = -42;

            WeaponFlashByX_RIGHT_SIT = 44;
            WeaponFlashByY_RIGHT_SIT = -36;
            WeaponFlashByX_LEFT_SIT = -76;
            WeaponFlashByY_LEFT_SIT = -36;

            WeaponFlashByX_RIGHT_LIE = 40;
            WeaponFlashByY_RIGHT_LIE = -42;
            WeaponFlashByX_LEFT_LIE = -72;
            WeaponFlashByY_LEFT_LIE = -42;

            hitBoxX1_STAY = -24;
            hitBoxX2_STAY = 18;
            hitBoxY1_STAY = -36;
            hitBoxY2_STAY = 2;
            hitBoxZ_STAY = 64;

            hitBoxX1_SIT = -32;
            hitBoxX2_SIT = 22;
            hitBoxY1_SIT = -36;
            hitBoxY2_SIT = 2;
            hitBoxZ_SIT = 48;

            hitBoxX1_LIE = -32;
            hitBoxX2_LIE = 22;
            hitBoxY1_LIE = -36;
            hitBoxY2_LIE = 2;
            hitBoxZ_LIE = 32;

        }

        generateHitBoxes();

    }

    private void generateHitBoxes() {
        hitBox_LIE = new HitBox(hitBoxX1_LIE, hitBoxX2_LIE, hitBoxY1_LIE, hitBoxY2_LIE, hitBoxZ_LIE);
        hitBox_SIT = new HitBox(hitBoxX1_SIT, hitBoxX2_SIT, hitBoxY1_SIT, hitBoxY2_SIT, hitBoxZ_SIT);
        hitBox_STAY = new HitBox(hitBoxX1_STAY, hitBoxX2_STAY, hitBoxY1_STAY, hitBoxY2_STAY, hitBoxZ_STAY);

    }

    public HitBox getHitBox(String currentState) {

        if (currentState.contains("LYI")) {
            return hitBox_LIE;
        } else if (currentState.contains("SIT")) {
            return hitBox_SIT;
        } else {// if (currentState.contains("SHOOT") ||
                // currentState.contains("RUN") ||
                // currentState.contains("WALK")) {
            return hitBox_STAY;
        }

    }

    public Point getFlashOffset(boolean isFlipped, String currentState) {
        Point offset = new Point(0, 0);
        if (!isFlipped) {

            if (currentState.contains("LYI")) {
                offset = new Point(WeaponFlashByX_RIGHT_LIE, WeaponFlashByY_RIGHT_LIE);
            } else if (currentState.contains("SIT")) {
                offset = new Point(WeaponFlashByX_RIGHT_SIT, WeaponFlashByY_RIGHT_SIT);
            } else if (currentState.contains("SHOOT") || currentState.contains("RUN") || currentState.contains("WALK")) {
                offset = new Point(WeaponFlashByX_RIGHT_STAY, WeaponFlashByY_RIGHT_STAY);
            }

        } else {
            if (currentState.contains("LYI")) {
                offset = new Point(WeaponFlashByX_LEFT_LIE, WeaponFlashByY_LEFT_LIE);
            } else if (currentState.contains("SIT")) {
                offset = new Point(WeaponFlashByX_LEFT_SIT, WeaponFlashByY_LEFT_SIT);
            } else if (currentState.contains("SHOOT") || currentState.contains("RUN") || currentState.contains("WALK")) {

                offset = new Point(WeaponFlashByX_LEFT_STAY, WeaponFlashByY_LEFT_STAY);
            }
        }

        return offset;
    }
}
