package gameframework.libs.other;

public class VectorLight {

    public int x;
    public int y;
    public int z;

    public VectorLight() {
        this.x = 0;
        this.y = 0;
        this.z = 0;

    }

    public VectorLight(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public VectorLight(float x, float y, float z) {
        this.x = (int) x;
        this.y = (int) y;
        this.z = (int) z;
    }

    public VectorLight(double x, double y, double z) {
        this.x = (int) x;
        this.y = (int) y;
        this.z = (int) z;
    }

    public void reset() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public static VectorLight sum(VectorLight a, VectorLight b) {
        return new VectorLight(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    public static VectorLight sub(VectorLight a, VectorLight b) {
        return new VectorLight(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static int length(VectorLight a) {
        return (int) Math.sqrt(a.x * a.x + a.y * a.y + a.z * a.z);
    }

    public static int distance(VectorLight a, VectorLight b) {
        return (int) Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y) + (a.z - b.z) * (a.z - b.z));
    }

    public static int distance2D(VectorLight a, VectorLight b) {
        return (int) Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }

    public static int distanceApproximation2D(VectorLight a, VectorLight b) {
        // int dx = Math.abs(a.x) - Math.abs(b.x);
        // int dy = Math.abs(a.y) - Math.abs(b.y);
        int dx = b.x - a.x;
        int dy = b.y - a.y;
        int min, max;

        if (dx < 0) {
            dx = -dx;
        }
        if (dy < 0) {
            dy = -dy;
        }

        if (dx < dy) {
            min = dx;
            max = dy;
        } else {
            min = dy;
            max = dx;
        }
        return (((max << 8) + (max << 3) - (max << 4) - (max << 1) + (min << 7) - (min << 5) + (min << 3) - (min << 1)) >> 8);
    }

    public String toString() {
        return ("Vector(" + this.x + ", " + this.y + ", " + this.z + ")");
    }

    public static VectorLight sum(VectorLight a, Vector b) {
        return new VectorLight(a.x + b.x, a.y + b.y, a.z + b.z);
    }

}
