package gameframework.entities;

import gameframework.Canvas;
import gameframework.Framework;
import gameframework.entities.weapons.Weapon;
import gameframework.libs.Animation;
import gameframework.libs.DamageInfo;
import gameframework.libs.HitBox;
import gameframework.libs.HumanAnimation;
import gameframework.libs.PositionsForHuman;
import gameframework.libs.SoundsForHuman;
import gameframework.libs.ai.AI;
import gameframework.libs.other.Vector;
import gameframework.libs.other.VectorLight;
import gameframework.libs.other.mapSprite;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;

public class Human extends Entity {

    public PositionsForHuman playerOffsets;
    public SoundsForHuman playerSounds;
    public HumanAnimation playerModel;

    private Weapon weapon = null;
    private Weapon meleeWeapon = null;

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Weapon getMeleeWeapon() {
        return meleeWeapon;
    }

    public void setMeleeWeapon(Weapon meleeWeapon) {
        this.meleeWeapon = meleeWeapon;
    }

    private final int healthInit = 100;
    private double health;

    private Vector accuracyMovements;
    private Vector movingSpeed;
    private Vector newAcceleration = new Vector();
    private int zModificator;

    private AI ai;

    public AI getAI() {
        return ai;
    }

    public void setAI(AI newAi) {
        this.ai = newAi;
    }

    private Vector defaultAcceleratingSpeed = new Vector(1, 0.6, 0);

    private String humanID;

    public Human(String humanID, String weaponID) {
        super();
        LoadContent(humanID);
        Initialize(humanID, weaponID);
    }

    private void Initialize(String humanID, String weaponID) {
        this.movingSpeed = new Vector(0, 0, 0);
        this.accuracyMovements = new Vector(0, 0, 0);
        SetPos(new VectorLight(0, 0, 0));
        this.health = healthInit;
        this.setHumanID(humanID);
        this.setAI(new AI(this));
        this.isPhysicsObject = true;
        weapon = new Weapon(weaponID, this);
        meleeWeapon = new Weapon("weapon_melee", this);
        playerSounds = new SoundsForHuman(humanID);

        if (humanID.contains("hand_mg")) {
            health = 250;
            defaultAcceleratingSpeed = new Vector(0.8, 0.4, 0);
        } else if (humanID.contains("stat_mg")) {
            health = 350;
            defaultAcceleratingSpeed = new Vector(0.6, 0.3, 0);
        } else if (humanID.contains("mortar")) {
            health = 350;
            defaultAcceleratingSpeed = new Vector(0.6, 0.3, 0);
        }
    }

    private void LoadContent(String humanID) {
        playerOffsets = new PositionsForHuman(humanID);
        playerModel = new HumanAnimation(humanID);
        playerModel.playWalking();
        playerModel.setActive(false);
    }

    public double GetHealth() {
        return this.health;
    }

    public void SetHealth(double d) {
        this.health = d;
    }

    public boolean IsAlive() {
        return (this.GetHealth() > 0);
    }

    @Override
    public VectorLight GetPos() {
        return pos;
    }

    @Override
    public void SetPos(VectorLight pos) {
        accuracyMovements.x = pos.x;
        accuracyMovements.y = pos.y;
        accuracyMovements.z = pos.z;

        this.pos.x = (int) accuracyMovements.x;
        this.pos.y = (int) accuracyMovements.y;
        this.pos.z = (int) accuracyMovements.z;

    }

    public Point getMuzzleFlashPos() {
        Point offset = playerOffsets.getFlashOffset(playerModel.mainAnimation.isFlipped(), playerModel.mainAnimation.getAnimName());
        return new Point(pos.x + offset.x, pos.y + offset.y + zModificator);
    }

    public void createMuzzleFlash() {
        if (weapon.getUniqueID().contains("hand_mg")) {
            Animation muzzleFlash = new Animation("muzzleFlash", Framework.activeGame().getGameResources().getTexture("weapon/mg_flash_"
                    + Framework.genRand(1, 2) + ".png"), 36, 32, 3, (int) (70 + 10 * (Math.random() - Math.random())), false,
                    (int) this.getMuzzleFlashPos().x, (int) this.getMuzzleFlashPos().y, 0, 0, playerModel.mainAnimation.isFlipped());
            gameframework.Framework.activeGame().particleCreate(muzzleFlash);
        } else if (weapon.getUniqueID().contains("stat_mg")) {
            Animation muzzleFlash = new Animation("muzzleFlash", Framework.activeGame().getGameResources().getTexture("weapon/mg_flash_"
                    + Framework.genRand(1, 2) + ".png"), 36, 32, 3, (int) (80 + 10 * (Math.random() - Math.random())), false,
                    (int) this.getMuzzleFlashPos().x, (int) this.getMuzzleFlashPos().y, 0, 0, playerModel.mainAnimation.isFlipped());
            gameframework.Framework.activeGame().particleCreate(muzzleFlash);
        } else if (weapon.getUniqueID().contains("mortar")) {
            Animation muzzleFlash = new Animation("muzzleFlash",
                    Framework.activeGame().getGameResources().getTexture("weapon/mortar_flash.png"), 32, 32, 4,
                    (int) (70 + 10 * (Math.random() - Math.random())), false, (int) this.getMuzzleFlashPos().x,
                    (int) this.getMuzzleFlashPos().y, 0, 0, playerModel.mainAnimation.isFlipped());
            gameframework.Framework.activeGame().particleCreate(muzzleFlash);
        } else {
            Animation muzzleFlash = new Animation("muzzleFlash", gameframework.Framework.activeGame().getGameResources().rifleBulletFlash, 36,
                    32, 3, (int) (60 + 10 * (Math.random() - Math.random())), false, (int) this.getMuzzleFlashPos().x,
                    (int) this.getMuzzleFlashPos().y, 0, 0, playerModel.mainAnimation.isFlipped());
            gameframework.Framework.activeGame().particleCreate(muzzleFlash);
        }
    }

    public Vector getAcceleratingSpeed() {
        double mod;
        String curAnimName = playerModel.mainAnimation.getAnimName();

        if (!this.IsAlive()) {
            mod = 0.6;
        } else {
            mod = Math.max(Math.min((GetHealth() * 1.5 / healthInit), 1), 0.5);
        }

        if ((ai.isPlayer() && (Canvas.keyboardKeyState(KeyEvent.VK_SHIFT)) || ai.getPressedKeys().contains(KeyEvent.VK_SHIFT))
                && !curAnimName.toString().contains("LYING")) {
            mod = mod * 2.3;
        }

        if (GetPos().z != 0 && GetPos().z != -32) {
            mod = mod * (1 + 0.035 * GetPos().z);

        }

        if (curAnimName.equals("LYING")) {
            if (playerModel.mainAnimation.getCurrentFrame() == 2) {
                mod = mod * 1.2;
            } else {
                mod = mod * 0.65;
            }
            if (humanID.contains("hand_mg")) {
                mod = 0.001;
            } else if (humanID.contains("mortar")) {
                mod = 0.001;
            } else if (humanID.contains("stat_mg")) {
                mod = 0.001;
            }
        }

        if ((curAnimName.contains("SHOOT") && humanID.contains("hand_mg"))) {
            mod = 0.001;
        }

        if ((curAnimName.equals("SHOOT") || curAnimName.contains("MELEE") || curAnimName.equals("LYING_DOWN") || playerModel.mainAnimation
                .getAnimName().equals("STANDING_UP")) && playerModel.mainAnimation.active) {
            mod = mod * 0.2;
            if (humanID.contains("hand_mg")) {
                mod = 0.001;
            }
        }

        newAcceleration.reset();

        newAcceleration.x = defaultAcceleratingSpeed.x * mod;
        newAcceleration.y = defaultAcceleratingSpeed.y * mod;

        return newAcceleration;
    }

    public void calculateSpeed() {

        String curAnimName = playerModel.mainAnimation.getAnimName();

        if (curAnimName.contains("SIT") || (curAnimName.equals("LYING_SHOOT") && playerModel.mainAnimation.active)) {
            movingSpeed.x = 0;
            movingSpeed.y = 0;
            return;
        }

        Vector acceleratingSpeed = getAcceleratingSpeed();

        if ((ai.isPlayer() && Canvas.keyboardKeyState(KeyEvent.VK_D)) || ai.getPressedKeys().contains(KeyEvent.VK_D))
            movingSpeed.x = acceleratingSpeed.x;
        else if ((ai.isPlayer() && Canvas.keyboardKeyState(KeyEvent.VK_A)) || ai.getPressedKeys().contains(KeyEvent.VK_A))
            movingSpeed.x = -(acceleratingSpeed.x);
        else if (movingSpeed.x > 0)
            movingSpeed.x = Math.max(0, movingSpeed.x - 0.2);
        else if (movingSpeed.x < 0)
            movingSpeed.x = Math.min(0, movingSpeed.x + 0.2);

        if ((ai.isPlayer() && Canvas.keyboardKeyState(KeyEvent.VK_W)) || ai.getPressedKeys().contains(KeyEvent.VK_W))
            movingSpeed.y = -(acceleratingSpeed.y);
        else if ((ai.isPlayer() && Canvas.keyboardKeyState(KeyEvent.VK_S)) || ai.getPressedKeys().contains(KeyEvent.VK_S))
            movingSpeed.y = acceleratingSpeed.y;
        else if (movingSpeed.y > 0)
            movingSpeed.y = Math.max(0, movingSpeed.y - 0.2);
        else if (movingSpeed.y < 0)
            movingSpeed.y = Math.min(0, movingSpeed.y + 0.2);

    }

    public void playerShoot(Point mousePos, Entity victim, boolean isMelee) {
        int cameraPosX = gameframework.Framework.activeGame().getPlayerCamera().getX();
        int cameraPosY = gameframework.Framework.activeGame().getPlayerCamera().getY();
        VectorLight victimPos = new VectorLight(mousePos.getX() + cameraPosX, mousePos.getY() + cameraPosY, 0.0);
        shoot(new Point((int) victimPos.x, (int) victimPos.y), victim, isMelee);
    }

    public void shoot(Point pos, Entity victim, boolean isMelee) {
        if (!IsAlive()) {
            return;
        }

        if (humanID.contains("mortar") || humanID.contains("stat_mg")) {
            if (!(playerModel.mainAnimation.getAnimName().equals("LYING") || playerModel.mainAnimation.getAnimName().equals("LYING_SHOOT"))) {
                return;
            }
        }

        VectorLight victimPos = new VectorLight(victim.GetPos().x, victim.GetPos().y, 0);
        VectorLight shootPos = new VectorLight(GetPos().x, GetPos().y, GetPos().z);

        if (playerModel.mainAnimation.getAnimName().contains("RUN")) {
            if (gameframework.libs.ai.AI.isDirectionFliped(this.GetPos(), victimPos) != playerModel.mainAnimation.isFlipped()) {
                return;
            }
        } else if (playerModel.mainAnimation.getAnimName().contains("WALK") && (movingSpeed.x != 0 || movingSpeed.y != 0)) {
            if (gameframework.libs.ai.AI.isDirectionFliped(this.GetPos(), victimPos) != playerModel.mainAnimation.isFlipped()) {
                return;
            }
        }

        playerModel.setFlipped(gameframework.libs.ai.AI.isDirectionFliped(this.GetPos(), victimPos));

        if (!isMelee) {
            if (weapon.tryToShoot(shootPos, victim, victimPos)) {
                playerModel.playShoot();
                createMuzzleFlash();
            }
        } else {
            if (this.meleeWeapon.tryToShoot(shootPos, victim, victimPos)) {
                playerModel.playMelee();
            }
        }
    }

    public boolean isItLeftScreen() {
        VectorLight modelPos = this.GetPos();
        if (modelPos.x > 32 && modelPos.x < Framework.activeGame().getMapSizeX() - 32 && modelPos.y > 32
                && modelPos.y < Framework.activeGame().getMapSizeY())
            return false;
        else

            return true;
    }

    public void calculateAnimation() {

        if ((ai.isPlayer() && Canvas.keyboardKeyState(KeyEvent.VK_K)) || ai.getPressedKeys().contains(KeyEvent.VK_K)) {
            this.SetHealth(-1);
        }

        if (playerModel.mainAnimation.active) {
            if ((playerModel.mainAnimation.getAnimName().equals("LYING_DOWN") || playerModel.mainAnimation.getAnimName().equals(
                    "STANDING_UP"))
                    || playerModel.mainAnimation.getAnimName().equals("STANDING_UP_TO_SIT")
                    || playerModel.mainAnimation.getAnimName().equals("SITTING_DOWN")) {
                return;
            }
        } else {

            if (playerModel.mainAnimation.getAnimName().contains("MELEE")) {
                playerModel.playWalking();
            }

            if (playerModel.mainAnimation.getAnimName().equals("LYING_DOWN")) {
                playerModel.playLieMoving();
            } else if (playerModel.mainAnimation.getAnimName().equals("STANDING_UP")) {
                playerModel.playWalking();

            } else if (playerModel.mainAnimation.getAnimName().equals("STANDING_UP_TO_SIT")) {
                playerModel.playSit();

            }
        }

        if ((ai.isPlayer() && Canvas.keyboardKeyState(KeyEvent.VK_D)) || Canvas.keyboardKeyState(KeyEvent.VK_A)
                || Canvas.keyboardKeyState(KeyEvent.VK_S) || Canvas.keyboardKeyState(KeyEvent.VK_W)
                || ai.getPressedKeys().contains(KeyEvent.VK_W) || ai.getPressedKeys().contains(KeyEvent.VK_S)
                || ai.getPressedKeys().contains(KeyEvent.VK_A) || ai.getPressedKeys().contains(KeyEvent.VK_D)) {
            if (playerModel.mainAnimation.getAnimName().equals("SITTING_DOWN")
                    || (playerModel.mainAnimation.getAnimName().equals("SITTING_SHOOT") && !playerModel.mainAnimation.active)) {
                playerModel.playStandUp();
            }

        }

        if ((ai.isPlayer() && Canvas.keyboardKeyState(KeyEvent.VK_CONTROL)) || ai.getPressedKeys().contains(KeyEvent.VK_CONTROL)) {
            if (!playerModel.mainAnimation.getAnimName().contains("SIT")) {
                playerModel.playSit();
            } else {
                playerModel.playStandUp();
            }
        }

        if ((ai.isPlayer() && Canvas.keyboardKeyState(KeyEvent.VK_Z)) || ai.getPressedKeys().contains(KeyEvent.VK_Z)) {
            if (!playerModel.mainAnimation.getAnimName().contains("LYING")) {
                playerModel.playLieDown();
            } else {
                playerModel.playStandUp();
            }
        }

        if (Math.abs(movingSpeed.x) > 1.1 || Math.abs(movingSpeed.y) > 0.8) {
            if (!playerModel.mainAnimation.getAnimName().equals("RUN") && !playerModel.mainAnimation.getAnimName().equals("LYING")) {
                playerModel.playRunning();
            }
        } else {
            if (playerModel.mainAnimation.getAnimName().equals("RUN")) {
                playerModel.playWalking();
            }

            if (playerModel.mainAnimation.getAnimName().contains("LYING_SHOOT") && !playerModel.mainAnimation.active
                    && (movingSpeed.x != 0 || movingSpeed.y != 0)) {
                playerModel.playLieMoving();
            } else if (playerModel.mainAnimation.getAnimName().contains("SHOOT") && !playerModel.mainAnimation.active
                    && (movingSpeed.x != 0 || movingSpeed.y != 0)) {
                playerModel.playWalking();
            }

        }

        if (movingSpeed.x == 0 && movingSpeed.y == 0
                && (playerModel.mainAnimation.getAnimName().equals("WALK") || playerModel.mainAnimation.getAnimName().equals("LYING"))) {
            playerModel.setActive(false);
        } else if ((movingSpeed.x != 0 || movingSpeed.y != 0)
                && (playerModel.mainAnimation.getAnimName().equals("WALK") || playerModel.mainAnimation.getAnimName().equals("LYING"))) {
            playerModel.setActive(true);
        }

    }

    public void flipAnimation() {
        if (movingSpeed.x > 0 && playerModel.mainAnimation.isFlipped()) {
            playerModel.setFlipped(false);
        } else if (movingSpeed.x < 0 && !playerModel.mainAnimation.isFlipped()) {
            playerModel.setFlipped(true);
        }
    }

    public void Update() {

        if ((ai != null) && IsAlive()) {
            ai.think();
        }

        weapon.update();
        meleeWeapon.update();
        calculateSpeed();

        accuracyMovements.x += movingSpeed.x;
        accuracyMovements.y += movingSpeed.y;
        accuracyMovements.z = Framework.activeGame().gettrenchesBitMap().calculateHeight(GetPos().x, GetPos().y);

        pos.x = (int) accuracyMovements.x;
        pos.y = (int) accuracyMovements.y;
        pos.z = (int) accuracyMovements.z;
        zModificator = -pos.z / 6;

        if (!IsAlive()) {
            if (playerModel.mainAnimation.getCurrentFrame() == 3 && playerModel.mainAnimation.getAnimName().equals("DEATH")) {
                mapSprite deadBody;
                int x1 = 384, x2 = 512;
                if (humanID.contains("mortar")) {
                    x1 = 256;
                    x2 = 384;
                }

                if (humanID.contains("stat_mg")) {
                    String country = "uk";
                    if (humanID.contains("ger")) {
                        country = "ger";
                    }
                    mapSprite mgBody;
                    if (playerModel.mainAnimation.isFlipped()) {
                        mgBody = new mapSprite(pos.x - playerOffsets.ByXforXYCoordinates - 13, pos.y - playerOffsets.ByYforXYCoordinates
                                + zModificator, Framework.activeGame().getGameResources().getTexture(country+"_stat_mg/"+country+"_stat_mg_body.png"), true,
                                (1.0f + (0.01f * (float) GetPos().z)));
                        deadBody = new mapSprite((int) (32 * (Math.random() - Math.random())) + pos.x - playerOffsets.ByXforXYCoordinates
                                - 13, (int) (5 * (Math.random() - Math.random())) + pos.y - playerOffsets.ByYforXYCoordinates
                                + zModificator, x1, 0, x2, 128, playerModel.playerModelDeathImage, true,
                                (1.0f + (0.01f * (float) GetPos().z)));
                    } else {

                        mgBody = new mapSprite(pos.x - playerOffsets.ByXforXYCoordinates, pos.y - playerOffsets.ByYforXYCoordinates
                                + zModificator, Framework.activeGame().getGameResources().getTexture(country+"_stat_mg/"+country+"_stat_mg_body.png"), false,
                                (1.0f + (0.01f * (float) GetPos().z)));
                        deadBody = new mapSprite((int) (32 * (Math.random() - Math.random())) + pos.x - playerOffsets.ByXforXYCoordinates
                                - 13, (int) (5 * (Math.random() - Math.random())) + pos.y - playerOffsets.ByYforXYCoordinates
                                + zModificator, x1, 0, x2, 128, playerModel.playerModelDeathImage, false,
                                (1.0f + (0.01f * (float) GetPos().z)));
                    }
                    gameframework.Framework.activeGame().getMapSpritesArray().add(deadBody);
                    gameframework.Framework.activeGame().getMapSpritesArray().add(mgBody);
                }

                if (playerModel.mainAnimation.isFlipped()) {
                    deadBody = new mapSprite(pos.x - playerOffsets.ByXforXYCoordinates - 13, pos.y - playerOffsets.ByYforXYCoordinates
                            + zModificator, x1, 0, x2, 128, playerModel.playerModelDeathImage, true, (1.0f + (0.01f * (float) GetPos().z)));
                } else {
                    deadBody = new mapSprite(pos.x - playerOffsets.ByXforXYCoordinates, pos.y - playerOffsets.ByYforXYCoordinates
                            + zModificator, x1, 0, x2, 128, playerModel.playerModelDeathImage, false, (1.0f + (0.01f * (float) GetPos().z)));
                    if (humanID.contains("stat_mg")) {

                    }
                }

                gameframework.Framework.activeGame().getMapSpritesArray().add(deadBody);

                Remove();
            }
            if (!playerModel.mainAnimation.getAnimName().equals("DEATH")) {
                playerModel.playDeath();
            }

        } else {

            calculateAnimation();
        }
        flipAnimation();
    }

    @Override
    public void takeDamage(DamageInfo damageInfo) {
        if (IsAlive()) {
            ai.onTakeDamage(damageInfo);
            this.SetHealth(Math.max(0, this.GetHealth() - damageInfo.getAmmount()));

            if (Math.random() > 0.9) {
                playerSounds.playPainSound();
            }
            mapSprite bloodRes = new mapSprite((int) GetPos().x + Framework.genRand(-45, -20),
                    (int) GetPos().y + Framework.genRand(-26, 0), Framework.activeGame().getGameResources().getTexture("blood/blood_spread_"
                            + Framework.genRand(1, 7) + ".png"), true);
            if (damageInfo.getAttacker() != null) {
                if (gameframework.libs.ai.AI.isDirectionFliped(this.GetPos(), damageInfo.getAttacker().GetPos())) {
                    bloodRes = new mapSprite((int) GetPos().x + Framework.genRand(0, 8), (int) GetPos().y + Framework.genRand(-26, 0),
                            Framework.activeGame().getGameResources().getTexture("blood/blood_spread_" + Framework.genRand(1, 7) + ".png"),
                            false);
                }
            } else {
                bloodRes = new mapSprite((int) GetPos().x + Framework.genRand(0, 8), (int) GetPos().y + Framework.genRand(-26, 0),
                        Framework.activeGame().getGameResources().getTexture("blood/blood_spread_" + Framework.genRand(1, 7) + ".png"),
                        (Math.random() > 0.5 ? true : false));
            }
            gameframework.Framework.activeGame().getMapSpritesArray().add(bloodRes);
        }

    }

    public HitBox getHitBox() {
        return playerOffsets.getHitBox(playerModel.mainAnimation.getAnimName());
    }

    public double getHitChance() {
        if (playerModel.mainAnimation.getAnimName().contains("LYI")) {
            return 0.65;
        } else if (playerModel.mainAnimation.getAnimName().contains("SIT")) {
            return 0.7;
        } else if (playerModel.mainAnimation.getAnimName().contains("RUN")) {
            return 0.85;
        } else {
            return 1.0;
        }
    }

    public double getShootAccuracyChance() {
        if (playerModel.mainAnimation.getAnimName().contains("LYI")) {
            return 1.0;
        } else if (playerModel.mainAnimation.getAnimName().contains("SIT")) {
            return 0.9;
        } else if (playerModel.mainAnimation.getAnimName().contains("RUN")) {
            return 0.7;
        } else {
            return 0.8;
        }
    }

    public void Draw(Graphics2D g2d) {

        playerModel.mainAnimation.changeCoordinates(pos.x - playerOffsets.ByXforXYCoordinates, pos.y - playerOffsets.ByYforXYCoordinates
                + zModificator);
        float alpha = (1.0f + (0.01f * (float) GetPos().z));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        playerModel.mainAnimation.Draw(g2d);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

    }

    public void DrawSelection(Graphics2D g2d) {
        if (IsAlive()) {
            if (Framework
                    .activeGame()
                    .getPlayerCamera()
                    .canSeeSprite(pos.x - playerOffsets.ByXforXYCoordinates, pos.y - playerOffsets.ByYforXYCoordinates + zModificator, 128,
                            128)) {

                g2d.setColor(new Color(255, 240, 32, 60));
                HitBox plyHitBox = playerOffsets.getHitBox(playerModel.mainAnimation.getAnimName());

                // modificators
                if (!playerModel.mainAnimation.isFlipped()) {
                    g2d.drawRect(pos.x + plyHitBox.getHitBoxX1(), pos.y + plyHitBox.getHitBoxY1() + zModificator, plyHitBox.getHitBoxX2()
                            - plyHitBox.getHitBoxX1(), plyHitBox.getHitBoxY2() - plyHitBox.getHitBoxY1());
                } else {
                    g2d.drawRect(pos.x + plyHitBox.getHitBoxX1() + 13, pos.y + plyHitBox.getHitBoxY1() + zModificator,
                            plyHitBox.getHitBoxX2() - plyHitBox.getHitBoxX1(), plyHitBox.getHitBoxY2() - plyHitBox.getHitBoxY1());
                }

            }
        }
    }

    public String getHumanID() {
        return humanID;
    }

    public void setHumanID(String humanID) {
        this.humanID = humanID;
    }

}
