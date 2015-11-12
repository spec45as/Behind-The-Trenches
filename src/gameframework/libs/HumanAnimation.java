package gameframework.libs;

import gameframework.Framework;
import gameframework.libs.other.Resources;

import java.awt.image.BufferedImage;

public class HumanAnimation {
    public Animation mainAnimation;
    public BufferedImage playerModelImage;
    public BufferedImage playerRunningImage;
    public BufferedImage playerModelDeathImage;
    public BufferedImage playerModelShootImage;
    public BufferedImage playerModelRunningShootImage;

    public BufferedImage playerModelMeleeAttackImage;

    public BufferedImage playerModelLyingMoveImage;
    public BufferedImage playerModelLyingShootImage;
    public BufferedImage playerModelLieDownImage;
    public BufferedImage playerModelLieUpImage;

    public BufferedImage playerModelSitShooting;

    public int frameSize;
    private String humanID;

    public Animation GetModel() {
        return this.mainAnimation;
    }

    public void setFlipped(boolean isFlipped) {
        mainAnimation.setFlipped(isFlipped);
    }

    public void setActive(boolean isActive) {
        mainAnimation.setActive(isActive);
    }

    public void playSit() {
        if (mainAnimation.getAnimName().toString().contains("SHOOT") && mainAnimation.active) {
            return;
        }
        if (mainAnimation.getAnimName().toString().contains("LYING")) {
            playStandUpToSit();
        } else {
            if (humanID.contains("mortar") || humanID.contains("stat_mg")) {
                playLieDown();
            } else {
                mainAnimation = new Animation("SITTING_DOWN", playerModelSitShooting, frameSize, frameSize, 1, 400, false, 0, 0, 0, 0,
                        mainAnimation.isFlipped());
            }
        }
    }

    public void playLieMoving() {
        if (mainAnimation.getAnimName().toString().contains("SHOOT") && mainAnimation.active) {
            return;
        }
        if (humanID.contains("mortar")) {
            mainAnimation = new Animation("LYING", playerModelLieUpImage, frameSize, frameSize, 1, 0, true, 0, 0, 0, 0,
                    mainAnimation.isFlipped());
        } else if (humanID.contains("stat_mg")) {
            mainAnimation = new Animation("LYING", playerModelSitShooting, frameSize, frameSize, 1, 0, true, 0, 0, 0, 0,
                    mainAnimation.isFlipped());
        } else {
            mainAnimation = new Animation("LYING", playerModelLyingMoveImage, frameSize, frameSize, 4, 230, true, 0, 0, 0, 0,
                    mainAnimation.isFlipped());
        }

    }

    public void playLieDown() {
        if (mainAnimation.getAnimName().toString().contains("SHOOT") && mainAnimation.active) {
            return;
        }
        if (humanID.contains("mortar") || humanID.contains("stat_mg")) {
            mainAnimation = new Animation("LYING_DOWN", playerModelLieDownImage, frameSize, frameSize, 5, 350, false, 0, 0, 0, 5,
                    mainAnimation.isFlipped());
        } else {
            mainAnimation = new Animation("LYING_DOWN", playerModelLieDownImage, frameSize, frameSize, 3, 180, false, 0, 0, 0, 2,
                    mainAnimation.isFlipped());
        }

    }

    public void playStandUpToSit() {
        if (mainAnimation.getAnimName().toString().contains("SHOOT") && mainAnimation.active) {
            return;
        }
        if (humanID.contains("mortar") || humanID.contains("stat_mg")) {
            playLieDown();
        } else {
            mainAnimation = new Animation("STANDING_UP_TO_SIT", playerModelLieUpImage, frameSize, frameSize, 3, 180, false, 0, 0, 0, 2,
                    mainAnimation.isFlipped());
        }

    }

    public void playStandUp() {
        if (mainAnimation.getAnimName().toString().contains("SHOOT") && mainAnimation.active) {
            return;
        }
        if (humanID.contains("mortar") || humanID.contains("stat_mg")) {
            mainAnimation = new Animation("STANDING_UP", playerModelLieUpImage, frameSize, frameSize, 5, 350, false, 0, 0, 0, 5,
                    mainAnimation.isFlipped());
        } else {
            if (mainAnimation.getAnimName().toString().contains("SITTING")) {
                mainAnimation = new Animation("STANDING_UP", playerModelSitShooting, frameSize, frameSize, 1, 400, false, 0, 0, 0, 0,
                        mainAnimation.isFlipped());
            } else {
                mainAnimation = new Animation("STANDING_UP", playerModelLieUpImage, frameSize, frameSize, 3, 180, false, 0, 0, 0, 2,
                        mainAnimation.isFlipped());
            }
        }
    }

    public void playRunning() {
        if (mainAnimation.getAnimName().toString().contains("SHOOT") && mainAnimation.active) {
            return;
        }
        mainAnimation = new Animation("RUN", playerRunningImage, frameSize, frameSize, 3, 140, true, 0, 0, 0, 1, mainAnimation.isFlipped());

    }

    public void playWalking() {
        if (mainAnimation.getAnimName().toString().contains("SHOOT") && mainAnimation.active) {
            return;
        }

        mainAnimation = new Animation("WALK", playerModelImage, frameSize, frameSize, 3, 200, true, 0, 0, 0, 1, mainAnimation.isFlipped());
    }

    public void playShoot() {
        if (humanID.contains("mortar") || humanID.contains("stat_mg")) {
            if (!mainAnimation.getAnimName().toString().contains("LYING")) {
                playLieDown();
            } else {
                if (mainAnimation.getAnimName().toString().contains("LYING_DOWN")) {
                    return;
                } else {
                    mainAnimation = new Animation("LYING_SHOOT", playerModelShootImage, frameSize, frameSize, 4, 600, false, 0, 0, 0, 3,
                            mainAnimation.isFlipped());
                }
            }
        } else {
            if (mainAnimation.getAnimName().toString().contains("RUN") && mainAnimation.active) {
                mainAnimation = new Animation("RUN_SHOOT", playerModelRunningShootImage, frameSize, frameSize, 0, 140, false, 0, 0, 0, 0,
                        mainAnimation.isFlipped());
            } else if (mainAnimation.getAnimName().toString().contains("LYING")) {
                mainAnimation = new Animation("LYING_SHOOT", playerModelLyingShootImage, frameSize, frameSize, 3, 180, false, 0, 0, 0, 0,
                        mainAnimation.isFlipped());
            } else if (mainAnimation.getAnimName().toString().contains("SITTING")) {
                mainAnimation = new Animation("SITTING_SHOOT", playerModelSitShooting, frameSize, frameSize, 4, 180, false, 0, 0, 0, 0,
                        mainAnimation.isFlipped());
            } else {
                mainAnimation = new Animation("SHOOT", playerModelShootImage, frameSize, frameSize, 4, 160, false, 0, 0, 0, 0,
                        mainAnimation.isFlipped());
            }
        }

    }

    public void playMelee() {
        if (humanID.contains("mortar") || humanID.contains("stat_mg")) {
            if (!mainAnimation.getAnimName().toString().contains("LYING")) {
                playLieDown();
            } else {
                if (mainAnimation.getAnimName().toString().contains("LYING_DOWN")) {
                    return;
                } else {
                    mainAnimation = new Animation("LYING_SHOOT", playerModelShootImage, frameSize, frameSize, 4, 300, false, 0, 0, 0, 0,
                            mainAnimation.isFlipped());
                }
            }
        } else {
            if (mainAnimation.getAnimName().toString().contains("RUN") && mainAnimation.active) {
                mainAnimation = new Animation("RUN_SHOOT_MELEE", playerModelMeleeAttackImage, frameSize, frameSize, 3, 180, false, 0, 0, 0,
                        2, mainAnimation.isFlipped());
            } else if (mainAnimation.getAnimName().toString().contains("LYING")) {
                mainAnimation = new Animation("LYING_SHOOT_MELEE", playerModelMeleeAttackImage, frameSize, frameSize, 3, 180, false, 0, 0,
                        0, 2, mainAnimation.isFlipped());
            } else if (mainAnimation.getAnimName().toString().contains("SITTING")) {
                mainAnimation = new Animation("SITTING_SHOOT_MELEE", playerModelMeleeAttackImage, frameSize, frameSize, 3, 180, false, 0,
                        0, 0, 2, mainAnimation.isFlipped());
            } else {
                mainAnimation = new Animation("SHOOT_MELEE", playerModelMeleeAttackImage, frameSize, frameSize, 3, 180, false, 0, 0, 0, 2,
                        mainAnimation.isFlipped());
            }
        }
    }

    public void playDeath() {
        setUpDeathModel();
        if (humanID.contains("mortar")) {
            mainAnimation = new Animation("DEATH", playerModelDeathImage, frameSize, frameSize, 3, 260, false, 0, 0, 0, 3,
                    mainAnimation.isFlipped());
        } else if (humanID.contains("stat_mg")) {
            mainAnimation = new Animation("DEATH", playerModelDeathImage, frameSize, frameSize, 3, 260, false, 0, 0, 0, 3,
                    mainAnimation.isFlipped());
        } else {
            mainAnimation = new Animation("DEATH", playerModelDeathImage, frameSize, frameSize, 4, 260, false, 0, 0, 0, 4,
                    mainAnimation.isFlipped());
        }
    }

    public void setUpDeathModel() {
        if (humanID.contains("mortar")) {
            if (humanID.contains("ger")) {
                playerModelDeathImage = Framework.activeGame().getGameResources().getTexture("ger_mortar_death/death_up_1.png");
            } else if (humanID.contains("uk")) {
                playerModelDeathImage = Framework.activeGame().getGameResources().getTexture("uk_mortar_death/death_up_1.png");
            }
        } else {
            if (mainAnimation.getAnimName().toString().contains(("LYING")) || mainAnimation.getAnimName().toString().contains(("STANDING"))) {
                if (humanID.contains("uk")) {
                    playerModelDeathImage = Framework.activeGame().getGameResources().uk_sold_down_deaths.get(Framework.genRand(0,
                            Framework.activeGame().getGameResources().uk_sold_down_deaths.size() - 1));
                } else if (humanID.contains("ger")) {
                    playerModelDeathImage = Framework.activeGame().getGameResources().ger_sold_down_deaths.get(Framework.genRand(0,
                            Framework.activeGame().getGameResources().ger_sold_down_deaths.size() - 1));
                }
            } else {
                if (humanID.contains("uk")) {
                    playerModelDeathImage = Framework.activeGame().getGameResources().uk_sold_up_deaths.get(Framework.genRand(0,
                            Framework.activeGame().getGameResources().uk_sold_up_deaths.size() - 1));
                } else if (humanID.contains("ger")) {
                    playerModelDeathImage = Framework.activeGame().getGameResources().ger_sold_up_deaths.get(Framework.genRand(0,
                            Framework.activeGame().getGameResources().ger_sold_up_deaths.size() - 1));
                }
            }
        }
    }

    public HumanAnimation(String humanID) {
        Resources gameTextures = Framework.activeGame().getGameResources();
        this.setHumanID(humanID);

        if (humanID.equals("rifleman_uk")) {
            frameSize = 128;

            playerModelImage = gameTextures.getTexture("reg_inf/uk_sold_walking.png");
            playerModelShootImage = gameTextures.getTexture("reg_inf/uk_sold_stay_shoot.png");
            playerRunningImage = gameTextures.getTexture("reg_inf/uk_sold_running.png");
            playerModelRunningShootImage = gameTextures.getTexture("reg_inf/uk_sold_running_shoot.png");
            playerModelLyingMoveImage = gameTextures.getTexture("reg_inf/uk_sold_lying_move.png");
            playerModelLyingShootImage = gameTextures.getTexture("reg_inf/uk_sold_lying_shoot.png");
            playerModelLieDownImage = gameTextures.getTexture("reg_inf/uk_sold_liedown.png");
            playerModelLieUpImage = gameTextures.getTexture("reg_inf/uk_sold_lieup.png");
            playerModelSitShooting = gameTextures.getTexture("reg_inf/uk_sold_sit_shoot.png");
            playerModelMeleeAttackImage = gameTextures.getTexture("reg_inf/uk_sold_stay_melee.png");

            mainAnimation = new Animation("WALK", playerModelImage, frameSize, frameSize, 3, 160, false, 0, 0, 0, 1, false);

        } else if (humanID.equals("hand_mg_uk")) {
            frameSize = 128;

            playerModelImage = gameTextures.getTexture("hand_mg/uk_sold_walking.png");
            playerModelShootImage = gameTextures.getTexture("hand_mg/uk_sold_stay_shoot.png");
            playerRunningImage = gameTextures.getTexture("hand_mg/uk_sold_running.png");
            playerModelRunningShootImage = gameTextures.getTexture("hand_mg/uk_sold_running_shoot.png");
            playerModelLyingMoveImage = gameTextures.getTexture("hand_mg/uk_sold_lying_move.png");
            playerModelLyingShootImage = gameTextures.getTexture("hand_mg/uk_sold_lying_shoot.png");
            playerModelLieDownImage = gameTextures.getTexture("hand_mg/uk_sold_liedown.png");
            playerModelLieUpImage = gameTextures.getTexture("hand_mg/uk_sold_lieup.png");
            playerModelSitShooting = gameTextures.getTexture("hand_mg/uk_sold_sit_shoot.png");
            playerModelMeleeAttackImage = gameTextures.getTexture("hand_mg/uk_sold_stay_melee.png");

            mainAnimation = new Animation("WALK", playerModelImage, frameSize, frameSize, 3, 160, false, 0, 0, 0, 1, false);

        } else if (humanID.equals("hand_mg_ger")) {
            frameSize = 128;

            playerModelImage = gameTextures.getTexture("hand_mg/ger_sold_walking.png");
            playerModelShootImage = gameTextures.getTexture("hand_mg/ger_sold_stay_shoot.png");
            playerRunningImage = gameTextures.getTexture("hand_mg/ger_sold_running.png");
            playerModelRunningShootImage = gameTextures.getTexture("hand_mg/ger_sold_running_shoot.png");
            playerModelLyingMoveImage = gameTextures.getTexture("hand_mg/ger_sold_lying_move.png");
            playerModelLyingShootImage = gameTextures.getTexture("hand_mg/ger_sold_lying_shoot.png");
            playerModelLieDownImage = gameTextures.getTexture("hand_mg/ger_sold_liedown.png");
            playerModelLieUpImage = gameTextures.getTexture("hand_mg/ger_sold_lieup.png");
            playerModelSitShooting = gameTextures.getTexture("hand_mg/ger_sold_sit_shoot.png");
            playerModelMeleeAttackImage = gameTextures.getTexture("hand_mg/ger_sold_stay_melee.png");

            mainAnimation = new Animation("WALK", playerModelImage, frameSize, frameSize, 3, 160, false, 0, 0, 0, 1, false);

        } else if (humanID.equals("mortar_ger")) {
            frameSize = 128;

            playerModelImage = gameTextures.getTexture("ger_mortar/ger_mortar_walking.png");
            playerModelShootImage = gameTextures.getTexture("ger_mortar/ger_mortar_shoot.png");
            playerRunningImage = gameTextures.getTexture("ger_mortar/ger_mortar_walking.png");
            playerModelRunningShootImage = gameTextures.getTexture("ger_mortar/ger_mortar_shoot.png");
            playerModelLyingMoveImage = gameTextures.getTexture("ger_mortar/ger_mortar_walking.png");
            playerModelLyingShootImage = gameTextures.getTexture("ger_mortar/ger_mortar_shoot.png");
            playerModelLieDownImage = gameTextures.getTexture("ger_mortar/ger_mortar_deploy.png");
            playerModelLieUpImage = gameTextures.getTexture("ger_mortar/ger_mortar_undeploy.png");
            playerModelSitShooting = gameTextures.getTexture("ger_mortar/ger_mortar_shoot.png");
            playerModelMeleeAttackImage = gameTextures.getTexture("ger_mortar/ger_mortar_shoot.png");

            mainAnimation = new Animation("WALK", playerModelImage, frameSize, frameSize, 3, 160, false, 0, 0, 0, 1, false);

        } else if (humanID.equals("mortar_uk")) {
            frameSize = 128;

            playerModelImage = gameTextures.getTexture("uk_mortar/uk_mortar_walking.png");
            playerModelShootImage = gameTextures.getTexture("uk_mortar/uk_mortar_shoot.png");
            playerRunningImage = gameTextures.getTexture("uk_mortar/uk_mortar_walking.png");
            playerModelRunningShootImage = gameTextures.getTexture("uk_mortar/uk_mortar_shoot.png");
            playerModelLyingMoveImage = gameTextures.getTexture("uk_mortar/uk_mortar_walking.png");
            playerModelLyingShootImage = gameTextures.getTexture("uk_mortar/uk_mortar_shoot.png");
            playerModelLieDownImage = gameTextures.getTexture("uk_mortar/uk_mortar_deploy.png");
            playerModelLieUpImage = gameTextures.getTexture("uk_mortar/uk_mortar_undeploy.png");
            playerModelSitShooting = gameTextures.getTexture("uk_mortar/uk_mortar_shoot.png");
            playerModelMeleeAttackImage = gameTextures.getTexture("uk_mortar/uk_mortar_shoot.png");

            mainAnimation = new Animation("WALK", playerModelImage, frameSize, frameSize, 3, 160, false, 0, 0, 0, 1, false);

        } else if (humanID.equals("rifleman_ger")) {
            frameSize = 128;

            playerModelImage = gameTextures.getTexture("reg_inf/ger_sold_walking.png");
            playerModelShootImage = gameTextures.getTexture("reg_inf/ger_sold_stay_shoot.png");
            playerRunningImage = gameTextures.getTexture("reg_inf/ger_sold_running.png");
            playerModelRunningShootImage = gameTextures.getTexture("reg_inf/ger_sold_running_shoot.png");
            playerModelLyingMoveImage = gameTextures.getTexture("reg_inf/ger_sold_lying_move.png");
            playerModelLyingShootImage = gameTextures.getTexture("reg_inf/ger_sold_lying_shoot.png");
            playerModelLieDownImage = gameTextures.getTexture("reg_inf/ger_sold_liedown.png");
            playerModelLieUpImage = gameTextures.getTexture("reg_inf/ger_sold_lieup.png");
            playerModelSitShooting = gameTextures.getTexture("reg_inf/ger_sold_sit_shoot.png");
            playerModelMeleeAttackImage = gameTextures.getTexture("reg_inf/ger_sold_stay_melee.png");

            mainAnimation = new Animation("WALK", playerModelImage, frameSize, frameSize, 3, 160, false, 0, 0, 0, 1, false);

        } else if (humanID.equals("stat_mg_uk")) {
            frameSize = 128;

            playerModelImage = gameTextures.getTexture("uk_stat_mg/uk_stat_mg_walking.png");
            playerModelShootImage = gameTextures.getTexture("uk_stat_mg/uk_stat_mg_shoot.png");
            playerRunningImage = gameTextures.getTexture("uk_stat_mg/uk_stat_mg_walking.png");
            playerModelRunningShootImage = gameTextures.getTexture("uk_stat_mg/uk_stat_mg_shoot.png");
            playerModelLyingMoveImage = gameTextures.getTexture("uk_stat_mg/uk_stat_mg_walking.png");
            playerModelLyingShootImage = gameTextures.getTexture("uk_stat_mg/uk_stat_mg_shoot.png");
            playerModelLieDownImage = gameTextures.getTexture("uk_stat_mg/uk_stat_mg_deploy.png");
            playerModelLieUpImage = gameTextures.getTexture("uk_stat_mg/uk_stat_mg_undeploy.png");
            playerModelSitShooting = gameTextures.getTexture("uk_stat_mg/uk_stat_mg_shoot.png");
            playerModelMeleeAttackImage = gameTextures.getTexture("uk_stat_mg/uk_stat_mg_shoot.png");

            mainAnimation = new Animation("WALK", playerModelImage, frameSize, frameSize, 3, 160, false, 0, 0, 0, 1, false);

        } else if (humanID.equals("stat_mg_ger")) {
            frameSize = 128;

            playerModelImage = gameTextures.getTexture("ger_stat_mg/ger_stat_mg_walking.png");
            playerModelShootImage = gameTextures.getTexture("ger_stat_mg/ger_stat_mg_shoot.png");
            playerRunningImage = gameTextures.getTexture("ger_stat_mg/ger_stat_mg_walking.png");
            playerModelRunningShootImage = gameTextures.getTexture("ger_stat_mg/ger_stat_mg_shoot.png");
            playerModelLyingMoveImage = gameTextures.getTexture("ger_stat_mg/ger_stat_mg_walking.png");
            playerModelLyingShootImage = gameTextures.getTexture("ger_stat_mg/ger_stat_mg_shoot.png");
            playerModelLieDownImage = gameTextures.getTexture("ger_stat_mg/ger_stat_mg_deploy.png");
            playerModelLieUpImage = gameTextures.getTexture("ger_stat_mg/ger_stat_mg_undeploy.png");
            playerModelSitShooting = gameTextures.getTexture("ger_stat_mg/ger_stat_mg_shoot.png");
            playerModelMeleeAttackImage = gameTextures.getTexture("ger_stat_mg/ger_stat_mg_shoot.png");

            mainAnimation = new Animation("WALK", playerModelImage, frameSize, frameSize, 3, 160, false, 0, 0, 0, 1, false);

        }
    }

    public String getHumanID() {
        return humanID;
    }

    public void setHumanID(String humanID) {
        this.humanID = humanID;
    }

}