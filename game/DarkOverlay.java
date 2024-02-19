package game;

import java.awt.AlphaComposite;
import java.awt.Color;

import engine.Window;
import engine.World;

public class DarkOverlay extends World {

    public DarkOverlay(Window window, String name, int width, int height) {
        super(window, name, width, height);
        this.setBackground(new Color(118, 118, 118));
        this.setOpaque(true);
        this.addBeforeRenderer((g) -> {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.86f));
            return g;
        });
    }

}
