package gameframework.libs;

import gameframework.Framework;

import java.net.URL;
import java.util.ArrayList;

public class SoundPlayer {
    private ArrayList<Sound> lewisSound = new ArrayList<Sound>();
    private ArrayList<Sound> enfieldSound = new ArrayList<Sound>();
    private ArrayList<Sound> enfieldReloadSound = new ArrayList<Sound>();

    private ArrayList<Sound> hitSmallGround = new ArrayList<Sound>();
    private ArrayList<Sound> hitSmallWood = new ArrayList<Sound>();
    private ArrayList<Sound> hitSmallMetal = new ArrayList<Sound>();

    private ArrayList<Sound> mainMenuMusic = new ArrayList<Sound>();
    private Sound mainMenuAction;

    // HUMAN UK
    private ArrayList<Sound> humanUkAttackSound = new ArrayList<Sound>();
    private ArrayList<Sound> humanUkMoveSound = new ArrayList<Sound>();
    private ArrayList<Sound> humanUkPainSound = new ArrayList<Sound>();

    private int genRand(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    private boolean noSound = true;

    public SoundPlayer() {
        try {
            Sound curSound;
            for (int i = 0; i <= 2; i++) {
                curSound = new Sound(Framework.activeGame().getGameResources().mainMenuMusic.get(i));
                mainMenuMusic.add(curSound);
            }
            mainMenuAction = new Sound(Framework.activeGame().getGameResources().menu_selection);

        } catch (Exception e) {
        }

        if (noSound) {
            return;
        }

        try {
            Sound curSound;
            for (int i = 0; i <= 2; i++) {
                curSound = new Sound(Framework.activeGame().getGameResources().mainMenuMusic.get(i));
                mainMenuMusic.add(curSound);
            }

            for (int i = 1; i < 5; i++) {
                curSound = new Sound(Framework.activeGame().getGameResources().weapon_enfield_shoot);
                enfieldSound.add(curSound);
            }
            for (int i = 1; i < 5; i++) {
                curSound = new Sound(Framework.activeGame().getGameResources().weapon_enfield_reload);
                enfieldReloadSound.add(curSound);
            }

            for (int i = 1; i <= 3; i++) {
                for (URL currentURL : Framework.activeGame().getGameResources().weapon_lewis_burst) {
                    curSound = new Sound(currentURL);
                    lewisSound.add(curSound);
                }
            }

            for (URL currentURL : Framework.activeGame().getGameResources().hitSmallGround) {
                curSound = new Sound(currentURL);
                hitSmallGround.add(curSound);
            }
            for (URL currentURL : Framework.activeGame().getGameResources().hitSmallMetal) {
                curSound = new Sound(currentURL);
                hitSmallMetal.add(curSound);
            }
            for (URL currentURL : Framework.activeGame().getGameResources().hitSmallWood) {
                curSound = new Sound(currentURL);
                hitSmallWood.add(curSound);
            }

            // HUMAN UK
            for (URL currentURL : Framework.activeGame().getGameResources().humanUkAttackSound) {
                curSound = new Sound(currentURL);
                humanUkAttackSound.add(curSound);
            }
            for (URL currentURL : Framework.activeGame().getGameResources().humanUkMoveSound) {
                curSound = new Sound(currentURL);
                humanUkMoveSound.add(curSound);
            }
            for (URL currentURL : Framework.activeGame().getGameResources().humanUkPainSound) {
                curSound = new Sound(currentURL);
                humanUkPainSound.add(curSound);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playMenuAction() {
        mainMenuAction.playSoundOnce();
    }

    public void playRandomMenuSound() {
        mainMenuMusic.get(genRand(0, mainMenuMusic.size() - 1)).playSoundOnce();
    }

    public void stopMenuSound() {
        for (Sound curSound : mainMenuMusic) {
            curSound.stopSound();
        }
    }

    public void humanUkAttackSound() {
        if (noSound) {
            return;
        }
        humanUkAttackSound.get(genRand(0, enfieldSound.size() - 1)).playSoundOnce();
    }

    public void humanUkMoveSound() {
        if (noSound) {
            return;
        }
        humanUkMoveSound.get(genRand(0, enfieldSound.size() - 1)).playSoundOnce();
    }

    public void humanUkPainSound() {
        if (noSound) {
            return;
        }
        humanUkPainSound.get(genRand(0, enfieldSound.size() - 1)).playSoundOnce();
    }

    public void playRandomEnfieldSound() {
        if (noSound) {
            return;
        }
        enfieldSound.get(genRand(0, enfieldSound.size() - 1)).playSoundOnce();
    }

    public void playRandomEnfieldReloadSound() {
        if (noSound) {
            return;
        }
        enfieldReloadSound.get(genRand(0, enfieldReloadSound.size() - 1)).playSoundOnce();
    }

    public void playRandomLewisSound() {
        if (noSound) {
            return;
        }
        lewisSound.get(genRand(0, lewisSound.size() - 1)).playSoundOnce();
    }

    public void playHitSmallGround() {
        if (noSound) {
            return;
        }
        hitSmallGround.get(genRand(0, hitSmallGround.size() - 1)).playSoundOnce();
    }

    public void playHitSmallMetal() {
        if (noSound) {
            return;
        }
        hitSmallMetal.get(genRand(0, hitSmallMetal.size() - 1)).playSoundOnce();
    }

    public void playHitSmallWood() {
        if (noSound) {
            return;
        }
        hitSmallWood.get(genRand(0, hitSmallWood.size() - 1)).playSoundOnce();
    }
}
