package gameframework.libs;

import java.net.URL;

import javafx.scene.media.AudioClip;

public class Sound {

    private AudioClip song;

    public Sound(String filename) {

        try {
            song = new AudioClip(filename);
        } catch (Exception e) {
        }
    }

    public Sound(URL soundUrl) {
        this(soundUrl.toString());
    }

    public void playSound() {
        song.setCycleCount(AudioClip.INDEFINITE);
        song.play();
    }

    public void stopSound() {
        song.stop();
    }

    public void playSoundOnce() {
        song.setCycleCount(1);
        song.play();
    }

}
