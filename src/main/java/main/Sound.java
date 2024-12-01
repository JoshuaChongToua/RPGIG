package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.InputStream;

public class Sound {

    Clip clip;
    String[] soundPaths = new String[30];

    public Sound() { //initialisation des sons
        soundPaths[0] = "/sounds/Adventure.wav";
        soundPaths[1] = "/sounds/coin.wav";
        soundPaths[2] = "/sounds/powerup.wav";
        soundPaths[3] = "/sounds/unlock.wav";
        soundPaths[4] = "/sounds/fanfare.wav";
        soundPaths[5] = "/sounds/hitmonster.wav";
        soundPaths[6] = "/sounds/receivedamage.wav";
        soundPaths[7] = "/sounds/levelup.wav";
        soundPaths[8] = "/sounds/cursor.wav";
    }

    public void setFile(int i) {
        try {
            // Charger le fichier à partir des ressources
            InputStream audioSrc = getClass().getResourceAsStream(soundPaths[i]);
            if (audioSrc == null) {
                throw new Exception("File not found: " + soundPaths[i]);
            }

            // Convertir InputStream en AudioInputStream
            AudioInputStream ais = AudioSystem.getAudioInputStream(audioSrc);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace(); //
        }
    }

    public void play() {
        if (clip != null) {
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}

