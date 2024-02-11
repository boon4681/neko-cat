package engine;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import engine.event.EventList;
import engine.event.EventListener;
import engine.event.MouseClicked;
import engine.event.MouseMoveEvent;
import engine.objects.BallBase;
import engine.objects.BallType;
import engine.objects.base.Body;
import engine.objects.base.Circle;
import engine.objects.base.IObject;
import engine.objects.base.BodyType;
import engine.vector.Vec2;

public class World extends JPanel implements Runnable {

    private ArrayList<IObject> objects = new ArrayList<>();

    private final int width;
    private final int height;

    private EventList events = new EventList();

    private Thread worldThread;
    private boolean loop = false;

    public World() {
        this(400, 400);
    }

    public void addRenderer() {

    }

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        setSize(width, height);
        setLocation(0, 0);
        setLayout(null);
        setBackground(Color.BLACK);

        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                events.dispatchEvent(new MouseClicked(e.getX(), e.getY()));
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

        });

        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                events.dispatchEvent(new MouseMoveEvent(e.getX(), e.getY()));
            }
        });
    }

    public void start() {
        this.loop = true;
        this.worldThread = new Thread(this);
        this.worldThread.start();
    }

    public int addEventListener(EventListener<?> e) {
        return this.events.addEventListener(e);
    }

    public void stop() {
        this.loop = false;
    }

    public UUID add(IObject object) {
        this.objects.add(object);
        return object.getID();
    }

    public void remove(IObject object) {
        this.objects.remove(object);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void update(double dt) {
        for (int i = 1; i < 64; i++) {
            for (IObject object : this.objects.toArray(new IObject[0])) {
                object.update(dt / i);
            }
            handleCollisions(dt / i);
        }
    }

    public void render(Graphics2D g) {
        for (IObject object : this.objects.toArray(new IObject[0])) {
            object.render(g);
        }
    }

    public void handleCollisions(double dt) {
        for (int i = 0; i < this.objects.size(); i++) {
            for (int l = i + 1; l < this.objects.size(); l++) {
                Body m = (Body) this.objects.get(i);
                Body g = (Body) this.objects.get(l);
                if (m.getRigibody() == BodyType.CIRCLE && g.getRigibody() == BodyType.CIRCLE) {
                    resolveCircleCollision(m, g);
                }
            }
        }
    }

    private void resolveCircleCollision(Body m, Body g) {
        BallBase a = (BallBase) m;
        BallBase b = (BallBase) g;
        Vec2 z = a.pos.copy().sub(b.pos);
        double d = z.mag();
        if (d < a.radius + b.radius) {
            Vec2 n = z.normalize();
            double delta = (a.radius + b.radius) - d;
            a.pos.add(n.copy().mult(delta * 0.5));
            b.pos.add(n.copy().mult(-delta * 0.5));
            Vec2 contact = b.pos.copy().sub(a.pos).normalize().mult(a.radius).add(b.pos);
            // Vec2 ra = contact1.copy().sub(a.pos);
            // Vec2 rb = contact2.copy().sub(b.pos);
            // Vec2 raP = new Vec2(-ra.y, ra.x);
            // Vec2 rbP = new Vec2(-rb.y, rb.x);
            // double raPN = raP.copy().dot(n);
            // double rbPN = rbP.copy().dot(n);

            // Vec2 rev_v = a.vel.copy().sub(b.vel.copy());
            // if (rev_v.copy().dot(n) > 0) {
            // return;
            // }
            for (BallType ball : BallType.values()) {
                if (ball.isBase(a) && ball.isBase(b)) {
                    this.remove(a);
                    this.remove(b);
                    BallBase to = ball.createTo(this);
                    to.pos = a.pos.copy().add(b.pos.copy()).div(2);
                }
            }
            Vec2 ra = contact.copy().sub(a.pos);
            Vec2 rb = contact.copy().sub(b.pos);
            Vec2 raP = new Vec2(-ra.y, ra.x);
            Vec2 rbP = new Vec2(-rb.y, rb.x);
            double raPN = raP.copy().dot(n.copy());
            double rbPN = rbP.copy().dot(n.copy());

            double de = (1 / a.mass) + (1 / b.mass) +
                    (raPN * raPN) * (1 / a.rotationalInertial) +
                    (rbPN * rbPN) * (1 / b.rotationalInertial);
            Vec2 ava = raP.copy().mult(a.rotationalVelocity);
            Vec2 avb = rbP.copy().mult(b.rotationalVelocity);
            Vec2 rev_v = a.vel.copy().add(ava).sub(b.vel.copy()).sub(avb);
            if (rev_v.copy().dot(n) > 0) {
                return;
            }
            System.out.println(i);
            double e = 0.5;
            double j = -(1 + e) * rev_v.copy().dot(n);
            j /= de;
            Vec2 impulse = n.copy().mult(j);
            a.applyForce(impulse.copy());
            a.applyRotationalForce(ra.copy(), impulse);
            b.applyForce(impulse.copy().mult(-1));
            b.applyRotationalForce(rb.copy().mult(-1), impulse);
            // double e = 0.5;
            // double j = (-1 + e) * rev_v.copy().dot(n);
            // double de = (1 / a.mass) + (1 / b.mass) +
            // (raPN * raPN) / a.rotationalInertial +
            // (rbPN * rbPN) / b.rotationalInertial;

            // double e = 0.5;
            // double j = -(1 + e) * rev_v.copy().dot(n);
            // j /= (1 / a.mass) + (1 / b.mass);
            // a.vel.add(n.copy().mult(j / a.mass));
            // b.vel.sub(n.copy().mult(j / b.mass));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // RenderingHints rh = new RenderingHints(
        // RenderingHints.KEY_TEXT_ANTIALIASING,
        // RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // g2d.setRenderingHints(rh);
        this.render(g2d);
    }

    @Override
    public void run() {
        int FPS = 60;
        double nano = TimeUnit.SECONDS.toNanos(1);
        double revo = nano / FPS;
        double dt = 0;
        long lastTime = System.nanoTime();
        long currentTime = System.nanoTime();
        while (loop) {
            currentTime = System.nanoTime();
            dt += ((currentTime - lastTime) / nano) * FPS;
            if (dt > 1) {
                dt--;
                this.events.flush();
                update(0.3);
                repaint();
            } else {
                try {
                    TimeUnit.NANOSECONDS.sleep((long) (revo - (currentTime - lastTime)));
                } catch (Exception e) {
                    loop = false;
                }
            }
            lastTime = currentTime;
        }
    }
}
