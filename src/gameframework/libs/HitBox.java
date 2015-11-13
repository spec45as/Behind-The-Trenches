package gameframework.libs;

public class HitBox {
    private int hitBoxX1;
    private int hitBoxX2;
    private int hitBoxY1;
    private int hitBoxY2;
    private int hitBoxZ;

    public HitBox(int hitBoxX1, int hitBoxX2, int hitBoxY1, int hitBoxY2, int hitBoxZ) {
        super();
        this.hitBoxX1 = hitBoxX1;
        this.hitBoxX2 = hitBoxX2;
        this.hitBoxY1 = hitBoxY1;
        this.hitBoxY2 = hitBoxY2;
        this.hitBoxZ = hitBoxZ;
    }

    public int getHitBoxX1() {
        return hitBoxX1;
    }

    public void setHitBoxX1(int hitBoxX1) {
        this.hitBoxX1 = hitBoxX1;
    }

    public int getHitBoxX2() {
        return hitBoxX2;
    }

    public void setHitBoxX2(int hitBoxX2) {
        this.hitBoxX2 = hitBoxX2;
    }

    public int getHitBoxY1() {
        return hitBoxY1;
    }

    public void setHitBoxY1(int hitBoxY1) {
        this.hitBoxY1 = hitBoxY1;
    }

    public int getHitBoxY2() {
        return hitBoxY2;
    }

    public void setHitBoxY2(int hitBoxY2) {
        this.hitBoxY2 = hitBoxY2;
    }

    public int getHitBoxZ() {
        return hitBoxZ;
    }

    public void setHitBoxZ(int hitBoxZ) {
        this.hitBoxZ = hitBoxZ;
    }

}
