package gameframework.libs;

import gameframework.entities.Entity;

public class DamageInfo {
    private Entity attacker;
    private Entity victim;
    private double ammount;

    // private HitBox victimHitBox;

    public DamageInfo(Entity attacker, Entity victim, double ammount) {
        this.ammount = ammount;
        this.attacker = attacker;
        this.victim = victim;
        // this.victimHitBox = victimHitBox;
    }

    // public HitBox getVictimHitBox() {
    // return this.victimHitBox;
    // }

    public DamageInfo(Entity victim, double i) {
        this.ammount = i;
        this.attacker = null;
        this.victim = victim;
    }

    public Entity getAttacker() {
        return attacker;
    }

    public double getAmmount() {
        return ammount;
    }

    public Entity getVictim() {
        return victim;
    }

}
