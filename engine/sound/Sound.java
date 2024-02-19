package engine.sound;

import java.awt.Label;
import java.io.File;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JFrame;

public class Sound {
    private boolean loop = false;
    private Clip clip;
    private File file;
    private float maxVolume;
    private int volume;

    public Sound(final File file) {
        this(file, false);
    }

    public Sound(final File file, boolean loop) {
        this.loop = loop;
        this.file = file;
    }

    public void play() {
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(inputStream);
            FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            maxVolume = control.getMinimum();
            control.setValue((1 - ((float) this.volume) / 100f) * maxVolume);
            clip.start();
            clip.loop(loop ? -1 : 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVolume(int v) {
        if (v > 100)
            v = 100;
        if (v < 0)
            v = 0;
        this.volume = v;
        if (this.clip == null)
            return;
        FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        control.setValue((1 - ((float) v) / 100f) * maxVolume);
    }

    public void stop() {
        try {
            this.loop = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getVolume() {
        return this.volume;
    }
}