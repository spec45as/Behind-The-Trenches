package gameframework.libs.other;

public class Vector {

    public double x;
    public double y;
    public double z;

    public Vector() {
        this.x = 0;
        this.y = 0;
        this.z = 0;

    }

    public Vector(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void reset() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
    
    public static Vector sum(Vector a, Vector b) {
        return new Vector(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    public static Vector sub(Vector a, Vector b) {
        return new Vector(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static double length(Vector a) {
        return Math.sqrt(a.x * a.x + a.y * a.y + a.z * a.z);
    }

    public static double distance(VectorLight vectorLight, VectorLight vectorLight2) {
        return Math.sqrt((vectorLight.x - vectorLight2.x) * (vectorLight.x - vectorLight2.x) + (vectorLight.y - vectorLight2.y) * (vectorLight.y - vectorLight2.y) + (vectorLight.z - vectorLight2.z) * (vectorLight.z - vectorLight2.z));
    }

    public String toString() {
        return ("Vector(" + this.x + ", " + this.y + ", " + this.z + ")");
    }

}
