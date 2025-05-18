package gameframework.libs.ai;

import gameframework.Framework;
import gameframework.entities.Entity;
import gameframework.entities.Flag;
import gameframework.entities.Human;
import gameframework.libs.DamageInfo;
import gameframework.libs.Ents;
import gameframework.libs.other.Vector;
import gameframework.libs.other.VectorLight;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class AI {

    private int destX = 0;
    private int destY = 0;
    private int team;
    private boolean running;
    private Entity shootTarget;
    private Entity meleeTarget;

    private Human human;
    private boolean isActive;
    private boolean isPlayer;

    private int meleeAimedBy = 0;

    // dont_shoot, melee_only, combined, rifle_only
    private String weaponBehavior;

    ArrayList<Integer> pressedKeys = new ArrayList<Integer>();

    private long nextAiThink = 0;

    // private long nextAimReload = 0;

    public long getLastAiThink() {
        return nextAiThink;
    }

    public void setNextAiThink(long time) {
        nextAiThink = time;
    }

    public AI(Human human) {
        super();
        this.setHuman(human);
        this.isActive = true;
        this.isPlayer = false;
        this.team = 1;
        setWeaponBehavior("combined");
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public void setPlayer(boolean isPlayer) {
        this.isPlayer = isPlayer;
    }

    private boolean lying_down = false;
    private boolean sitting_down = false;

    public void lieDown() {
        if (!human.playerModel.mainAnimation.getAnimName().contains("LYI")) {
            stop();
            pressedKeys.add(KeyEvent.VK_Z);
            human.calculateSpeed();
            human.calculateAnimation();
            lying_down = true;
        } else {
            lying_down = false;
        }

    }

    public void sit() {
        if (human.getHumanID().contains("mortar") || human.getHumanID().contains("stat_mg")) {
            lieDown();
        } else {
            if (!human.playerModel.mainAnimation.getAnimName().contains("SIT")) {
                stop();

                pressedKeys.add(KeyEvent.VK_CONTROL);
                human.calculateSpeed();
                human.calculateAnimation();
                sitting_down = true;
            } else {
                sitting_down = false;
            }
        }
    }

    public void stay() {
        stop();
        if (human.playerModel.mainAnimation.getAnimName().contains("SIT")) {
            pressedKeys.add(KeyEvent.VK_CONTROL);
            human.calculateSpeed();
            human.calculateAnimation();
        } else if (human.playerModel.mainAnimation.getAnimName().contains("LYI")) {
            pressedKeys.add(KeyEvent.VK_Z);
            human.calculateSpeed();
            human.calculateAnimation();
        }
    }

    public void stop() {
        destX = 0;
        destY = 0;
    }

    public void stopShoot() {
        shootTarget = null;
        setMeleeTarget(null);
    }

    private void move() {

        if (lying_down) {
            lieDown();
            return;
        }

        if (sitting_down) {
            sit();
            return;
        }

        if (destX != 0 && destY != 0) {
            int posX = (int) human.GetPos().x;
            int posY = (int) human.GetPos().y;

            final int MAGIC_CONST = 4;

            if ((Math.abs(posX - destX) <= MAGIC_CONST * 2) && (Math.abs(posY - destY) <= MAGIC_CONST * 2)) {
                destX = 0;
                destY = 0;
                return;
            }

            if (Math.abs(posX - destX) > MAGIC_CONST * 2) {
                if (posX < destX + MAGIC_CONST) {
                    pressedKeys.add(KeyEvent.VK_D);
                } else if (posX > destX - MAGIC_CONST) {
                    pressedKeys.add(KeyEvent.VK_A);
                }
            }

            if (Math.abs(posY - destY) > MAGIC_CONST * 2) {
                if (posY < destY + MAGIC_CONST) {
                    pressedKeys.add(KeyEvent.VK_S);
                } else if (posY > destY - MAGIC_CONST) {
                    pressedKeys.add(KeyEvent.VK_W);
                }

            }

            if (running) {
                pressedKeys.add(KeyEvent.VK_SHIFT);
            }
        }
    }

    public void shoot() {

        if (lying_down || sitting_down) {
            return;
        }

        if (shootTarget != null) {
            if ((human.getHumanID().contains("mortar")) && VectorLight.distance(human.GetPos(), shootTarget.GetPos()) < 256) {
                shootTarget = null;
                return;
            }
        }

        if (weaponBehavior.equals("dont_shoot")) {
            return;
        } else if (weaponBehavior.equals("melee_only")) {
            if (meleeTarget != null) {
                if (Vector.distance(meleeTarget.GetPos(), human.GetPos()) < human.getMeleeWeapon().getMaxRange()) {
                    human.shoot(null, meleeTarget, true);
                    return;
                }
            }
        } else if (weaponBehavior.equals("rifle_only")) {
            if (shootTarget != null) {
                if (Vector.distance(shootTarget.GetPos(), human.GetPos()) < human.getWeapon().getMaxRange()) {
                    if (Framework.activeGame().getClass().getSimpleName().equals("DemoBattle") || human.getAI().team == 1) {

                        if (human.getHumanID().contains("hand_mg")) {
                            if (Math.random() > 0.5 && !human.playerModel.mainAnimation.getAnimName().contains("LYI")) {
                                sit();
                            } else if (!human.playerModel.mainAnimation.getAnimName().contains("LYI")
                                    && !human.playerModel.mainAnimation.getAnimName().contains("SIT")) {
                                lieDown();
                            }
                        }

                        if (human.getHumanID().contains("stat_mg") || human.getHumanID().contains("mortar")) {
                            lieDown();
                        }
                        human.shoot(null, shootTarget, false);
                        return;
                    }
                }
            }
        } else {
            if (meleeTarget != null) {
                if (Vector.distance(meleeTarget.GetPos(), human.GetPos()) < human.getMeleeWeapon().getMaxRange()) {
                    human.shoot(null, meleeTarget, true);
                    return;
                }
            }
            if (shootTarget != null) {
                if (Vector.distance(shootTarget.GetPos(), human.GetPos()) < human.getWeapon().getMaxRange()) {
                    human.shoot(null, shootTarget, false);
                    return;
                }
            }
        }
    }

    public void think() {

        if (!isActive || isPlayer) {
            return;
        }

        if (human.isItLeftScreen()) {
            setDestX(destX);
            setDestY(destY);
        }

        if (meleeTarget == null && shootTarget == null && destX == 0 && destY == 0) {
            for (Flag curFlag : Framework.activeGame().getMapFlags()) {
                if (Framework.activeGame().getClass().getSimpleName().equals("DemoBattle") || team != 0) {
                    if (curFlag.team != team) {
                        if (Math.random() > 0.5) {
                            if (human.getHumanID().contains("hand_mg") || human.getHumanID().contains("stat_mg")
                                    || human.getHumanID().contains("mortar")) {
                                stay();
                            }

                            setDestX(curFlag.GetPos().x + Framework.genRand(-256, 256));
                            setDestY(curFlag.GetPos().y + Framework.genRand(-256, 256));
                        }
                    }
                }
            }
        }

        pressedKeys.clear();
        move();
        // if (Framework.CurGameTime() > nextAimReload) {
        // setMeleeAimedBy(0);
        // nextAimReload = (long) (Framework.CurGameTime() + 1500 *
        // Math.random());
        // }

        if (meleeTarget != null && !meleeTarget.IsValid()) {
            meleeTarget = null;
            nextAiThink = 250;
            stop();
            destX = (int) (human.GetPos().x + 150 * Math.random() - 150 * Math.random());
            destY = (int) (human.GetPos().y + 150 * Math.random() - 150 * Math.random());
        }

        if (shootTarget != null && !shootTarget.IsValid()) {
            nextAiThink = 250;
            shootTarget = null;
        }

        if (shootTarget != null) {
            if (shootTarget instanceof Human) {
                if (!((Human) shootTarget).IsAlive()) {
                    nextAiThink = 1000;
                    shootTarget = null;
                }
            }
        }

        if (meleeTarget != null) {
            if (meleeTarget instanceof Human) {
                if (!((Human) meleeTarget).IsAlive()) {
                    nextAiThink = 1000;
                    meleeTarget = null;
                }
            }
        }

        if (weaponBehavior.equals("rifle_only")) {
            if (Framework.CurGameTime() > nextAiThink) {
                shootTarget = getTarget(human, human.getWeapon().getMaxRange());
                nextAiThink = (long) (Framework.CurGameTime() + 1000 * Math.random());
            }
        } else if (weaponBehavior.equals("melee_only")) {
            if (meleeTarget == null) {
                meleeTarget = getTarget(human, Math.max(human.getWeapon().getMaxRange() / 2, human.getMeleeWeapon().getMaxRange()));
                if (meleeTarget != null) {
                    // if (((Human) meleeTarget).getAI().getMeleeAimedBy() < 3)
                    // {
                    stay();
                    destX = meleeTarget.GetPos().x;
                    destY = meleeTarget.GetPos().y;
                    setRunning(true);
                    // ((Human) meleeTarget).getAI().setMeleeAimedBy(((Human)
                    // meleeTarget).getAI().getMeleeAimedBy() + 1);
                    nextAiThink = (long) (Framework.CurGameTime() + 500 * Math.random());
                    // } else {
                    // meleeTarget = null;
                    // }
                }
            } else {
                stay();
                destX = meleeTarget.GetPos().x;
                destY = meleeTarget.GetPos().y;
                setRunning(true);
                nextAiThink = (long) (Framework.CurGameTime() + 1000 * Math.random());
            }
        } else if (weaponBehavior.equals("combined")) {

            if (meleeTarget == null) {
                meleeTarget = getTarget(human, human.getMeleeWeapon().getMaxRange() * 3);
                if (meleeTarget != null) {
                    stay();
                    destX = meleeTarget.GetPos().x;
                    destY = meleeTarget.GetPos().y;
                    setRunning(true);
                    nextAiThink = (long) (Framework.CurGameTime() + 500 * Math.random());
                } else if (meleeTarget == null) {
                    shootTarget = getTarget(human, human.getWeapon().getMaxRange());
                    nextAiThink = (long) (Framework.CurGameTime() + 1000 * Math.random());
                }
            } else {
                stay();
                destX = meleeTarget.GetPos().x;
                destY = meleeTarget.GetPos().y;
                setRunning(true);
                nextAiThink = (long) (Framework.CurGameTime() + 500 * Math.random());
            }

        } else if (weaponBehavior.equals("dont_shoot")) {
            nextAiThink = (long) (Framework.CurGameTime() + 1000 * Math.random());
        }

        shoot();

    }

    public void onTakeDamage(DamageInfo damageInfo) {
        if (!isActive || isPlayer) {
            return;
        }
        if (damageInfo.getAttacker() != null) {
            human.playerModel.setFlipped(gameframework.libs.ai.AI.isDirectionFliped(human.GetPos(), damageInfo.getAttacker().GetPos()));
        }
        int rand = (int) (Math.random() * 10);

        if (Framework.activeGame().getActiveSelection() == null
                || (Framework.activeGame().getActiveSelection() != null && !Framework.activeGame().getActiveSelection().getSelectedEnts()
                        .contains(human))) {
            if (human.getWeapon().getUniqueID().contains("stat_mg")) {
                lieDown();
                setShootTarget(damageInfo.getAttacker());
            }
            if (Framework.activeGame().getClass().getSimpleName().equals("DemoBattle") || !human.playerModel.mainAnimation.getAnimName().contains("RUN")) {

                if (weaponBehavior.equals("combined") || weaponBehavior.equals("rifle_only")) {
                    if (rand < 4) {
                        if (!human.playerModel.mainAnimation.getAnimName().contains("LYI")) {
                            sit();
                        }
                    }

                    if (rand > 6) {
                        lieDown();
                    }
                }
                rand = (int) (Math.random() * 10);
                if (rand > 5) {
                    if (shootTarget == null) {
                        if (damageInfo.getAttacker() != null) {
                            setShootTarget(damageInfo.getAttacker());
                        }
                    }
                }
            }
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setPressedKeys(ArrayList<Integer> pressedKeys) {
        this.pressedKeys = pressedKeys;
    }

    public ArrayList<Integer> getPressedKeys() {
        return pressedKeys;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getDestX() {
        return destX;
    }

    public void setDestX(int destX) {
        this.destX = Math.max(Math.min(destX, Framework.activeGame().getMapSizeX() - 32), 32);
    }

    public int getDestY() {
        return destY;
    }

    public void setDestY(int destY) {
        this.destY = Math.max(Math.min(destY, Framework.activeGame().getMapSizeY() - 32), 32);
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Entity getShootTarget() {
        return shootTarget;
    }

    public void setShootTarget(Entity shootTarget) {
        this.shootTarget = shootTarget;
    }

    public static Entity getTarget(Entity human, int maxDist) {

        if (((Human) human).getAI().team == 1) {
            // return null;
        }

        boolean fastSearch = false;
        boolean nearestSearch = false;// true;
        boolean advancedAi = false;
        if (fastSearch && advancedAi) {
            Entity target = Framework.activeGame().getAiTargetingMap().getOneTargetInSectors(human, maxDist);
            return target;

        } else if (advancedAi) {

            ArrayList<Entity> victims = Framework.activeGame().getAiTargetingMap().getTargetsInSectors(human, maxDist);
            if (victims.size() != 0) {
                Random randomGenerator = new Random();
                int index = randomGenerator.nextInt(victims.size());
                Entity victim = victims.get(index);
                return victim;
            } else {
                return null;
            }
        } else if (nearestSearch) {
            Entity target = Ents.getNearestTargetInSphere(human, maxDist);
            return target;
        } else if (fastSearch) {
            Entity target = Ents.getOneTargetInSphere(human, maxDist);
            return target;
        } else {

            ArrayList<Entity> targets = Ents.getAllTargetsInSphere(human, maxDist);
            ArrayList<Entity> victims = new ArrayList<Entity>();
            Random randomGenerator = new Random();

            for (Entity curTarget : targets) {
                if (curTarget.getClass().getSimpleName().equals("Human")) {
                    if (((Human) curTarget).getAI().getTeam() != ((Human) human).getAI().getTeam()) {
                        victims.add(curTarget);
                    }
                }
            }

            if (victims.size() != 0) {
                int index = randomGenerator.nextInt(victims.size());
                Entity victim = victims.get(index);
                return victim;
            } else {
                return null;
            }
        }
    }

    public static boolean isDirectionFliped(VectorLight vectorLight, VectorLight vectorLight2) {
        return (vectorLight2.x - vectorLight.x < 0);
    }

    public Human getHuman() {
        return human;
    }

    public void setHuman(Human human) {
        this.human = human;
    }

    public String getWeaponBehavior() {
        return weaponBehavior;
    }

    public void setWeaponBehavior(String weaponBehavior) {
        this.meleeTarget = null;
        this.shootTarget = null;

        if (human.getHumanID().contains("mortar")) {

            if (weaponBehavior.equals("dont_shoot")) {
                weaponBehavior = "dont_shoot";
            } else {
                weaponBehavior = "rifle_only";
            }
        }

        if (human.getHumanID().contains("stat_mg")) {

            if (weaponBehavior.equals("dont_shoot")) {
                weaponBehavior = "dont_shoot";
            } else {
                weaponBehavior = "rifle_only";
            }
        }

        if (human.getHumanID().contains("hand_mg")) {

            if (weaponBehavior.equals("melee_only") || weaponBehavior.equals("dont_shoot")) {
                weaponBehavior = "dont_shoot";
            } else {
                weaponBehavior = "rifle_only";
            }
        }

        this.weaponBehavior = weaponBehavior;
    }

    public Entity getMeleeTarget() {
        return meleeTarget;
    }

    public void setMeleeTarget(Entity meleeTarget) {
        this.meleeTarget = meleeTarget;
        this.shootTarget = meleeTarget;

    }

    public int getMeleeAimedBy() {
        return meleeAimedBy;
    }

    public void setMeleeAimedBy(int meleeAimedBy) {
        this.meleeAimedBy = meleeAimedBy;
    }
}
