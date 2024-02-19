package engine;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

import engine.event.EventList;
import engine.event.window.ResizeEvent;
import engine.tick.Tickable;

public class Window extends JFrame implements Tickable {
    private ArrayList<World> worlds = new ArrayList<>();
    private EventList events = new EventList();

    public Window(String name) {
        super(name);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                events.dispatchEvent(new ResizeEvent(getSize()));
            }
        });
    }

    public void add(World world) {
        this.worlds.add(world);
        super.add(world);
    }

    public void remove(World world) {
        this.worlds.remove(world);
        super.remove(world);
    }

    @Override
    public final void tick(double dt) {
        for (World world : worlds) {
            world.tick(dt);
        }
    }
}
