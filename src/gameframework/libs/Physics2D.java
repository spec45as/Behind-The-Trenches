package gameframework.libs;

import gameframework.libs.other.Vector;

//import java.awt.Rectangle;

public class Physics2D {
    public static boolean checkCollision(Vector posA, HitBox a, Vector posB, HitBox b) {
        /*
         * Rectangle first = new Rectangle((int) (posA.x + a.startPos.x), (int)
         * (posA.y + a.startPos.y), (int) (a.endPos.x - a.startPos.x), (int)
         * (a.endPos.y - a.startPos.y)); Rectangle second = new Rectangle((int)
         * (posB.x + b.startPos.x), (int) (posB.y + b.startPos.y), (int)
         * (b.endPos.x - b.startPos.x), (int) (b.endPos.y - b.startPos.y));
         * 
         * if (second.intersects(first)) {
         * 
         * return true; }
         */
        return false;
    }

}
