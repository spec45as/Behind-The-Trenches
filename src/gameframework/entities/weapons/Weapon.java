package gameframework.entities.weapons;

import java.awt.Point;
import java.util.ArrayList;

import gameframework.Framework;
import gameframework.entities.Entity;
import gameframework.entities.Human;
import gameframework.libs.Animation;
import gameframework.libs.DamageInfo;
import gameframework.libs.Ents;
import gameframework.libs.Shooting;
import gameframework.libs.other.Vector;
import gameframework.libs.other.VectorLight;
import gameframework.libs.other.mapSprite;

public class Weapon implements Cloneable {

    public Weapon clone() {
        Weapon obj = null;
        try {
            obj = (Weapon) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private String name = "Lee-Enfield Mk III";
    private int ammoClipSize = 10;
    private int ammoClip = 5;

    // MILLISECONDS!!!113211
    private long fireDelay = 500;
    private long reloadTime = 5000;

    public long getLastFired() {
        return lastFired;
    }

    public long getReloadingTimer() {
        return reloadingTimer;
    }

    public int getBulletSpeed() {
        return bulletSpeed;
    }

    private long lastFired = 0;

    private long reloadingTimer = 0;

    private double damage = 40;

    // PERCENTS!!1Q121212
    private double chanceToHit = 4;

    private int maxRange = 1000;

    public int getMaxRange() {
        return this.maxRange;
    }

    private int bulletSpeed = 32;
    Animation muzzleFlash;
    private Human owner;

    private String uniqueID;

    public Weapon(String uniqueID, Human owner) {
        if (uniqueID.equals("weapon_uk_enfield")) {
            name = "Lee-Enfield Mk III";
            ammoClipSize = 10;
            ammoClip = 10;
            fireDelay = 1000;
            reloadTime = 3000;
            damage = 40;
            chanceToHit = 0.9;
            maxRange = 800;
            bulletSpeed = 32;
            this.uniqueID = uniqueID;
        } else if (uniqueID.equals("weapon_hand_mg_uk_lewis")) {
            name = "Lewis MG";
            ammoClipSize = 47;
            ammoClip = 47;
            fireDelay = 100;
            reloadTime = 9000;
            damage = 40;
            chanceToHit = 0.9;
            maxRange = 800;
            bulletSpeed = 36;
            this.uniqueID = uniqueID;
        } else if (uniqueID.equals("weapon_melee")) {
            name = "Melee attack";
            ammoClipSize = 1;
            ammoClip = 1;
            fireDelay = 750;
            reloadTime = 0;
            damage = 50;
            chanceToHit = 1;
            maxRange = 40;
            bulletSpeed = 100;
            this.uniqueID = uniqueID;
        } else if (uniqueID.equals("weapon_mortar_ger")) {
            name = "German Mortar";
            ammoClipSize = 1;
            ammoClip = 1;
            fireDelay = 4000;
            reloadTime = 0;
            damage = 0;
            chanceToHit = 1;
            maxRange = 1600;
            bulletSpeed = 100;
            this.uniqueID = uniqueID;
        } else if (uniqueID.equals("weapon_mortar_uk")) {
            name = "UK Mortar";
            ammoClipSize = 1;
            ammoClip = 1;
            fireDelay = 4000;
            reloadTime = 0;
            damage = 0;
            chanceToHit = 1;
            maxRange = 1600;
            bulletSpeed = 100;
            this.uniqueID = uniqueID;
        } else if (uniqueID.equals("weapon_stat_mg_vickers")) {
            name = "Vickers Stationary MG";
            ammoClipSize = 250;
            ammoClip = 250;
            fireDelay = 140;
            reloadTime = 20000;
            damage = 45;
            chanceToHit = 0.9;
            maxRange = 800;
            bulletSpeed = 36;
            this.uniqueID = uniqueID;
        }
        this.owner = owner;
    }

    public Animation getMuzzleFlash() {
        return muzzleFlash;
    }

    public void setMuzzleFlash(Animation muzzleFlash) {
        this.muzzleFlash = muzzleFlash;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void setAmmoClipSize(int ammoClipSize) {
        this.ammoClipSize = ammoClipSize;
    }

    public void setAmmoClip(int ammoClip) {
        this.ammoClip = ammoClip;
    }

    public void setFireDelay(long fireDelay) {
        this.fireDelay = fireDelay;
    }

    public void setReloadTime(long reloadTime) {
        this.reloadTime = reloadTime;
    }

    public void setLastFired(long lastFired) {
        this.lastFired = lastFired;
    }

    public void setReloadingTimer(long reloadingTimer) {
        this.reloadingTimer = reloadingTimer;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void setChanceToHit(double chanceToHit) {
        this.chanceToHit = chanceToHit;
    }

    public void setMaxRange(int maxRange) {
        this.maxRange = maxRange;
    }

    public void setBulletSpeed(int bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    public void update() {
    }

    public boolean tryToShoot(VectorLight shootPos, Entity victim, VectorLight victimPos) {
        if (!isReloading()) {
            if ((lastFired - Framework.CurGameTime()) < 0) {
                if (ammoClip <= 0) {
                    reload();
                    return false;
                }
                shoot(shootPos, victim, victimPos);
                return true;
            }
        }
        return false;
    }

    public boolean isHit(Entity human, Entity victim) {
        if (victim == null) {
            return false;
        }

        VectorLight victimPos = victim.GetPos();
        VectorLight humanPos = owner.GetPos();

        double chancePercentMod = 1.0;
        if (victim.getClass().getSimpleName().equals("Human")) {
            chancePercentMod = ((Human) victim).getHitChance();
        }
        chancePercentMod = chancePercentMod * owner.getShootAccuracyChance();
        double distance = Vector.distance(humanPos, victimPos);

        if (distance > getMaxRange()) {
            return false;
        }

        chancePercentMod = (1.0 + 1.5 * (victimPos.z / 100.0))
                * (chancePercentMod * getChanceToHit() * Math.max(0.5, (1.0 - (distance / getMaxRange()))));
        chancePercentMod = Math.max(Math.min(chancePercentMod, 1), 0);
        if ((Math.random() < chancePercentMod)) {
            return true;
        } else {
            return false;
        }
    }

    public double ammountOfDamage(VectorLight shootPos, Entity victim) {
        return damage + (damage * Math.random() / 10) - (damage * Math.random() / 10);
    }

    public void shoot(VectorLight shootPos, Entity victim, VectorLight victimPos2) {
        lastFired = (long) (Framework.CurGameTime() + fireDelay);
        ammoClip = ammoClip - 1;

        if (uniqueID.equals("weapon_melee")) {
            if (Vector.distance(shootPos, victimPos2) < maxRange) {
                Point victimPos = new Point((int) victimPos2.x, (int) victimPos2.y);
                if (victim == null) {
                    victim = Shooting.getShootingTargetFromAbsolutePos(victimPos);
                }
                DamageInfo damageInfo = new DamageInfo(owner, victim, ammountOfDamage(shootPos, victim));
                victim.takeDamage(damageInfo);

            }
            return;
        } else if (uniqueID.contains("mortar")) {
            double accuracyMod = Math.max(0, Math.min(1, VectorLight.distance(shootPos, victim.GetPos()) / (double) maxRange));

            VectorLight newHitPos = new VectorLight(victim.GetPos().x
                    + Framework.genRand((int) (-256 * accuracyMod), (int) (256 * accuracyMod)), victim.GetPos().y
                    + Framework.genRand((int) (-256 * accuracyMod), (int) (256 * accuracyMod)), 0);
            Animation explosion = new Animation("mortar_explosion",
                    gameframework.Framework.activeGame().getGameResources().mortarShellExplosion, 64, 64, 2,
                    (int) (160 + 10 * (Math.random() - Math.random())), false, newHitPos.x - 32, newHitPos.y - 62, 0, 1, false);
            gameframework.Framework.activeGame().particleCreate(explosion);

            mapSprite explosionRes = new mapSprite(newHitPos.x - 17, newHitPos.y - 20,
                    Framework.activeGame().getGameResources().mortarExplosionSprite, false);
            gameframework.Framework.activeGame().getMapSpritesArray().add(explosionRes);

            ArrayList<Entity> victims = Ents.getAllEntsInSphere(newHitPos, 128);
            for (Entity curVictim : victims) {
                double chancePercentMod = 1.0;
                chancePercentMod = (1.0 + 1.5 * (curVictim.GetPos().z / 100.0)) * (chancePercentMod * getChanceToHit());
                chancePercentMod = Math.max(Math.min(chancePercentMod, 1), 0);
                if ((Math.random() < chancePercentMod)) {
                    double distanceMod = 1.0 - Math.max(0,
                            Math.min(1, VectorLight.distanceApproximation2D(curVictim.GetPos(), newHitPos) / 128.0));
                    double coverMod = Math.max(0, Math.min(1, (1.0 + 1.5 * (curVictim.GetPos().z / 100.0))));
                    curVictim.takeDamage(new DamageInfo(null, victim, coverMod * distanceMod * 250.0));
                }
            }
            return;
        }

        Point victimPos = new Point((int) victimPos2.x, (int) victimPos2.y);

        if (victim == null) {
            victim = Shooting.getShootingTargetFromAbsolutePos(victimPos);
        }

        if (isHit(owner, victim)) {
            DamageInfo damageInfo = new DamageInfo(owner, victim, damage);
            victim.takeDamage(damageInfo);
        } else {
            if (Math.random() > 0.8)
                gameframework.Framework.activeGame().getSoundPlayer().playHitSmallGround();
        }

        if (uniqueID.equals("weapon_uk_enfield")) {
            if (Math.random() > 0.5)
                gameframework.Framework.activeGame().getSoundPlayer().playRandomEnfieldSound();
        } else if (uniqueID.equals("weapon_uk_mg_lewis")) {
            gameframework.Framework.activeGame().getSoundPlayer().playRandomLewisSound();
        }

    }

    public boolean isReloading() {
        if ((reloadingTimer - Framework.CurGameTime()) < 0) {
            return false;
        }
        return true;
    }

    public void reload() {
        if (!isReloading()) {
            reloadingTimer = Framework.CurGameTime() + reloadTime;
            ammoClip = ammoClipSize;

            if (uniqueID.equals("weapon_uk_enfield")) {
                gameframework.Framework.activeGame().getSoundPlayer().playRandomEnfieldReloadSound();
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmmoClipSize() {
        return ammoClipSize;
    }

    public int getAmmoClip() {
        return ammoClip;
    }

    public long getReloadTime() {
        return reloadTime;
    }

    public long getFireDelay() {
        return fireDelay;
    }

    public double getDamage() {
        return damage;
    }

    public double getChanceToHit() {
        return chanceToHit;
    }

    public Human getOwner() {
        return owner;
    }

    public void setOwner(Human owner) {
        this.owner = owner;
    }

}
