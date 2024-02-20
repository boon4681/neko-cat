package engine;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.JPanel;

import engine.event.EventList;
import engine.event.EventListener;
import engine.event.mouse.MouseClicked;
import engine.event.mouse.MouseMoveEvent;
import engine.event.mouse.MousePosition;
import engine.event.mouse.MouseUp;
import engine.event.world.CircleMerge;
import engine.event.world.MergeScore;
import engine.objects.base.Body;
import engine.objects.base.IObject;
import engine.objects.base.ShapeType;
import engine.objects.base.UIComponent;
import engine.tick.Tickable;
import engine.vector.Vec2;
import engine.objects.ball.BallBase;
import engine.objects.ball.BallType;

public class World extends JPanel implements Tickable {

    public static interface Renderer {
        public Graphics2D render(Graphics2D g);
    }

    protected ArrayList<World> worlds = new ArrayList<>();
    protected ArrayList<IObject> objects = new ArrayList<>();
    protected ArrayList<Renderer> before_renderers = new ArrayList<>();
    protected ArrayList<Renderer> after_renderers = new ArrayList<>();
    protected EventList events = new EventList();
    protected boolean running = true;

    private final int width;
    private final int height;

    private final String name;
    private final Window window;
    public double mx = 0;
    public double my = 0;

    public World(Window window, String name) {
        this(window, name, 400, 400);
    }

    public void addBeforeRenderer(Renderer renderer) {
        this.before_renderers.add(renderer);
    }

    public void addAfterRenderer(Renderer renderer) {
        this.after_renderers.add(renderer);
    }

    public World(Window window, String name, int width, int height) {
        this.width = width;
        this.height = height;
        setSize(width, height);
        setLocation(0, 0);
        setLayout(null);
        // setBackground(null);
        setOpaque(false);
        this.name = name;
        this.window = window;
        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                events.dispatchEvent(new MouseClicked(e.getX(), e.getY()));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                events.dispatchEvent(new MouseUp(e.getX(), e.getY()));
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
                events.dispatchEvent(new MouseMoveEvent(e.getX(), e.getY()));
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                events.dispatchEvent(new MouseMoveEvent(e.getX(), e.getY()));
            }
        });
        this.addEventListener(new EventListener<MousePosition>("mousemove", (e) -> {
            this.mx = e.x;
            this.my = e.y;
            return true;
        }));
        this.addEventListener(new EventListener<MousePosition>("mouseclick", (e) -> {
            UIComponent hover = this.getHoverComponent();
            if (hover != null) {
                mx = e.x;
                my = e.y;
                if (hover.click(e) == false) {
                    return false;
                }
            }
            return true;
        }));
    }

    public int addEventListener(EventListener<?> e) {
        return this.events.addEventListener(e);
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

    private UIComponent getHoverComponent() {
        UIComponent is_hovering = null;
        for (IObject object : this.objects.toArray(new IObject[0])) {
            Body body = (Body) object;
            double x = body.getX();
            double y = body.getY();
            double w = body.getWidth();
            double h = body.getHeight();
            if (body instanceof UIComponent) {
                if (this.mx >= x && this.my >= y && this.mx <= x + w && this.my <= y + h) {
                    is_hovering = (UIComponent) body;
                } else {
                    ((UIComponent) body).hover(false);
                }
            }
        }
        return is_hovering;
    }

    public void update(double dt) {
        // mouse hover
        UIComponent hover = this.getHoverComponent();
        if (hover != null) {
            hover.hover(true);
        }
        for (int i = 1; i < 64; i++) {
            for (IObject object : this.objects.toArray(new IObject[0])) {
                object.update(dt / i);
                ((Body) object).age += dt / i;
            }
            handleCollisions(dt / i);
        }
    }

    public void render(Graphics2D g) {
        for (IObject object : this.objects.toArray(new IObject[0])) {
            if (object.isVisible()) {
                object.render(g);
            }
        }
    }

    public void handleCollisions(double dt) {
        for (int i = 0; i < this.objects.size(); i++) {
            for (int l = i + 1; l < this.objects.size(); l++) {
                Body m = (Body) this.objects.get(i);
                Body g = (Body) this.objects.get(l);
                if (m.getShapeType() == ShapeType.CIRCLE && g.getShapeType() == ShapeType.CIRCLE) {
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
            Vec2 k = z.normalize();
            double delta = (a.radius + b.radius) - d;
            a.pos.add(k.copy().mult(delta * 0.5));
            b.pos.add(k.copy().mult(-delta * 0.5));
            Vec2 n = a.pos.copy().sub(b.pos).normalize();
            Vec2 contact = b.pos.copy().sub(a.pos).normalize().mult(a.radius).add(b.pos);
            // Vec2 rev_v = a.vel.copy().sub(b.vel.copy());
            // if (rev_v.copy().dot(n) > 0) {
            // return;
            // }
            for (BallType ball : BallType.values()) {
                if (ball.isBase(a) && ball.isBase(b)) {
                    this.remove(a);
                    this.remove(b);
                    ball.createTo(this, a.pos.copy().add(b.pos.copy()).div(2));
                    this.events.dispatchEvent(new CircleMerge(new MergeScore(ball.getScore())));
                }
            }
            Vec2 ra = contact.copy().sub(a.pos);
            Vec2 rb = contact.copy().sub(b.pos);
            Vec2 raP = new Vec2(-ra.y, ra.x);
            Vec2 rbP = new Vec2(-rb.y, rb.x);
            Vec2 ava = raP.copy().mult(a.rotationalVelocity);
            Vec2 avb = rbP.copy().mult(b.rotationalVelocity);
            Vec2 rev_v = a.vel.copy().add(ava).sub(b.vel.copy()).sub(avb);
            double j = 0;
            // Freaking Impulse
            {
                double raPN = raP.copy().dot(n.copy());
                double rbPN = rbP.copy().dot(n.copy());
                double de = (1 / a.mass) + (1 / b.mass) +
                        (raPN * raPN) * (1 / a.rotationalInertial) +
                        (rbPN * rbPN) * (1 / b.rotationalInertial);
                if (rev_v.copy().dot(n) > 0) {
                    return;
                }
                double e = 0.5;
                j = -(1 + e) * rev_v.copy().dot(n);
                j /= de;
                Vec2 impulse = n.copy().mult(j);
                a.applyForce(impulse.copy());
                a.applyRotationalForce(ra.copy(), impulse);
                b.applyForce(impulse.copy().mult(-1));
                b.applyRotationalForce(rb.copy().mult(-1), impulse);
            }
            // Freaking Friction
            {
                Vec2 T = rev_v.copy().sub(n.copy().mult(rev_v.copy().dot(n.copy())));
                if (Math.abs(T.x) > 0.00001 || Math.abs(T.y) > 0.00001) {
                    T = T.normalize();
                }
                double raPT = raP.copy().dot(T.copy());
                double rbPT = rbP.copy().dot(T.copy());
                double de = (1 / a.mass) + (1 / b.mass) +
                        (raPT * raPT) * (1 / a.rotationalInertial) +
                        (rbPT * rbPT) * (1 / b.rotationalInertial);
                double jt = -rev_v.copy().dot(T.copy());
                jt /= de;
                Vec2 friction;
                if (Math.abs(jt) <= j * 0.7) {
                    friction = T.copy().mult(jt);
                } else {
                    friction = T.copy().mult(j * 0.3 * -1);
                }
                a.applyForce(friction.copy());
                // a.applyRotationalForce(ra.copy().normalize().mult(a.radius), friction);
                a.applyRotationalForce(ra.copy(), friction);
                b.applyForce(friction.copy().mult(-1));
                // b.applyRotationalForce(rb.copy().normalize().mult(-1).mult(b.radius), friction);
                b.applyRotationalForce(rb.copy().mult(-1), friction);
            }
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
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        // RenderingHints rh = new RenderingHints(
        // RenderingHints.KEY_TEXT_ANTIALIASING,
        // RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // g2d.setRenderingHints(rh);
        for (Renderer renderer : this.before_renderers) {
            g = renderer.render(g2d);
        }
        this.render(g2d);
        for (Renderer renderer : this.after_renderers) {
            g = renderer.render(g2d);
        }
        super.paintComponent(g);
    }

    public void add(World world) {
        this.worlds.add(world);
        super.add(world);
    }

    public void remove(World world) {
        this.worlds.remove(world);
        super.remove(world);
    }

    public void removeAll() {
        this.objects.clear();
    }

    @Override
    public void tick(double dt) {
        if (!this.isVisible())
            return;
        this.events.flush();
        if (!this.running)
            return;
        update(0.6);
        for (World world : worlds) {
            world.tick(dt);
        }
        repaint();
    }

    // @Override
    // public void run() {
    // double nano = TimeUnit.SECONDS.toNanos(1);
    // double dt = nano / 200;
    // long currentTime = System.nanoTime();
    // while (loop) {
    // long newTime = System.nanoTime();
    // long frameTime = newTime - currentTime;
    // currentTime = newTime;
    // this.events.flush();
    // while (frameTime > 0.0) {
    // double delta = Math.min(frameTime, dt);
    // update(dt / nano);
    // frameTime -= delta;
    // }
    // repaint();
    // }
    // }
}
