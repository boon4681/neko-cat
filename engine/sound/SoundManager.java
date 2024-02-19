package engine.sound;

import java.lang.reflect.Field;

public interface SoundManager {
    public static <T extends SoundManager> void setVolume(Class<T> tClass, int v) {
        for (Field field : tClass.getDeclaredFields()) {
            String type = field.getType().getName();
            if (type.equals("engine.sound.Sound")) {
                try {
                    Sound sound = (Sound) field.get(null);
                    sound.setVolume(v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
