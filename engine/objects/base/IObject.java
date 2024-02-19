package engine.objects.base;

import java.awt.Graphics2D;
import java.util.UUID;

import engine.World;

public interface IObject {
    public boolean isVisible();
    public UUID getID();

    public World getWorld();

    public void render(Graphics2D g);

    public void update(double dt);

    public default void remove() {
        this.getWorld().remove(this);
    }
}
