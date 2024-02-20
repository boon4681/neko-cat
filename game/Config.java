package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.Scanner;

public class Config {
    public static int highscore = 0;
    public static int sfx = 100;
    public static int music = 70;

    public static void save() {
        String data = "";
        for (Field field : Config.class.getDeclaredFields()) {
            try {
                data += field.getName() + ":" + field.getInt(null) + "\n";
            } catch (Exception e) {
            }
        }
        try {
            FileWriter writer = new FileWriter("neko.save");
            writer.write(data);
            writer.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void load() {
        try {
            File file = new File("neko.save");
            if (file.exists()) {
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();
                    if (data.length() > 0) {
                        for (Field field : Config.class.getDeclaredFields()) {
                            try {
                                for (int i = 0; i < data.length(); i++) {
                                    if (data.charAt(i) == ':') {
                                        if (field.getName().equals(data.substring(0, i))) {
                                            field.set(null, Integer.parseInt(data.substring(i + 1, data.length())));
                                        }
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                }
                reader.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
