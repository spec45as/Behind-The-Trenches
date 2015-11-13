package gameframework.entities;

import gameframework.Framework;
import gameframework.libs.DamageInfo;
import gameframework.libs.HitBox;
import gameframework.libs.other.VectorLight;

import java.awt.Graphics2D;
import java.util.ArrayList;

abstract public class Entity {

    protected VectorLight pos = new VectorLight();

    public Entity() {
        LoadContent();
        Initialize();
    }

    private void Initialize() {
    }

    public ArrayList<HitBox> hitBoxes = new ArrayList<HitBox>();
    public boolean isPhysicsObject = false;
    public boolean isValid = true;

    public PlayerSpectator getCamera() {
        return Framework.activeGame().getPlayerCamera();
    }

    public boolean IsValid() {
        return this.isValid;
    }

    public void Remove() {
        this.isValid = false;
    }

    public VectorLight GetPos() {
        return pos;
    }

    public void SetPos(VectorLight pos) {
        this.pos = pos;
    }

    public void takeDamage(double ammount, HitBox hitBox) {
    }

    private void LoadContent() {
    }

    public void Reset(double xCoordinate, double yCoordinate, double zCoordinate) {
        pos = new VectorLight(xCoordinate, yCoordinate, zCoordinate);
    }

    public void Update() {
    }

    public void takeDamage(DamageInfo damageInfo) {
    }

    public void Draw(Graphics2D g2d) {
        // для определения центра (x,y)
        //g2d.setColor(new Color(255, 200, 90, 255));
        //g2d.drawRect((int) this.pos.x, (int) this.pos.y, 1, 1);

    }

}
