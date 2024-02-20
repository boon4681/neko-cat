package game;

import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

import engine.Window;
import engine.World;
import engine.objects.base.Body;
import engine.objects.base.IObject;
import game.events.GameOver;

public class PhysicWorld extends World {
    private double countdown = 0;

    public PhysicWorld(Window window, String name, int width, int height) {
        super(window, name, width, height);
        // events.dispatchEvent(new GameOver(true));
    }

    @Override
    public void tick(double dt) {
        boolean found = false;
        for (IObject iObject : objects) {
            Body body = (Body) iObject;
            if (body.pos.y < 220 && body.age > 3) {
                found = true;
            }
        }
        if (found) {
            if (countdown > 180) {
                this.running = false;
                countdown = 0;
                events.dispatchEvent(new GameOver(true));
            }
            countdown += dt;
        } else {
            countdown = 0;
        }
        super.tick(dt);
    }

    public void restart(){
        this.running = true;
        this.objects.clear();
    }
}
