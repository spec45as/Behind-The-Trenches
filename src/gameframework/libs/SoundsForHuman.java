package gameframework.libs;

import gameframework.Framework;

public class SoundsForHuman {
    private String humanID;

    public SoundsForHuman(String humanID) {
        this.humanID = humanID;
    }

    public void playAttackSound() {
        if (humanID.equals("rifleman_uk")) {
            Framework.activeGame().getSoundPlayer().humanUkAttackSound();
        } else if (humanID.equals("rifleman_ger")) {
            Framework.activeGame().getSoundPlayer().humanUkAttackSound();
        }
    }

    public void playPainSound() {
        if (humanID.equals("rifleman_uk")) {
            Framework.activeGame().getSoundPlayer().humanUkPainSound();
        } else if (humanID.equals("rifleman_ger")) {
            Framework.activeGame().getSoundPlayer().humanUkPainSound();
        }

    }

    public void playMoveSound() {
        if (humanID.equals("rifleman_uk")) {
            Framework.activeGame().getSoundPlayer().humanUkMoveSound();
        } else if (humanID.equals("rifleman_ger")) {
            Framework.activeGame().getSoundPlayer().humanUkMoveSound();
        }

    }
}
