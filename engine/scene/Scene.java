package engine.scene;

import java.awt.Graphics2D;

import engine.Window;
import engine.World;

public class Scene {
    private World world;
    private IScene setup;

    public Scene(Window window) {
        world = new World(window, null, window.getWidth(), window.getHeight());
        window.add(world);
    }

    public void setup(IScene setup) {
        setup.init(world);
    }

    public void setDisplay(boolean display) {
        world.setVisible(display);
    }

    public World getWorld() {
        return this.world;
    }
}
