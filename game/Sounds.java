package game;

import java.io.File;

import engine.sound.Sound;
import engine.sound.SoundManager;

public class Sounds {
    public static interface SFX extends SoundManager {
        Sound pop = new Sound(new File("assets/sound/pop.wav"));
        Sound pop2 = new Sound(new File("assets/sound/pop2.wav"));
        Sound pop3 = new Sound(new File("assets/sound/pop3.wav"));
        Sound drop = new Sound(new File("assets/sound/drop.wav"));
        Sound drop2 = new Sound(new File("assets/sound/drop2.wav"));
        Sound clickSound = new Sound(new File("assets/sound/btn_click.wav"));
        Sound enterSound = new Sound(new File("assets/sound/btn_enter.wav"));
    }

    public static interface MUSIC extends SoundManager {
        Sound bgm = new Sound(new File("assets/sound/bgm.wav"), true);
    }
}
