package engine.objects.base;

import java.awt.Color;
import java.awt.Graphics2D;

import engine.World;
import engine.vector.Vec2;

public class Circle extends DynamicBody {

    protected final Vec2 GRAVITY = new Vec2(0, 0.1);
    public int radius = 10;
    public final ShapeType shape = ShapeType.CIRCLE;

    public ShapeType getShapeType() {
        return this.shape;
    }

    public Circle(World world) {
        this(world, 10);
    }

    public Circle(World world, int radius) {
        this(world, radius, 10);
    }

    public Circle(World world, int radius, Vec2 pos) {
        this(world, radius, 10);
        this.pos = pos;
    }

    public Circle(World world, int radius, int mass) {
        super(world);
        this.mass = mass;
        this.radius = radius;
        this.rotationalInertial = Math.pow(radius, 4) * 0.25 * Math.PI;
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillOval((int) this.getX(), (int) this.getY(), this.radius * 2, this.radius * 2);
    }

    @Override
    public void update(double dt) {
        this.applyForce(GRAVITY.copy().mult(this.mass));
        this.updateRotation(dt);
        this.updatePosition(dt);
    }

    public void updateRotation(double dt) {
        this.rotationalVelocity = this.rotationalVelocity + this.rotationalAcc * dt;
        this.rotation = this.rotation + this.rotationalVelocity * dt + this.rotationalAcc * dt * dt * 0.5;
        this.rotationalAcc = 0;
    }

    @Override
    public void updatePosition(double dt) {
        this.edges(dt);
        this.vel = this.vel.add(this.acc.copy().mult(dt));
        if (this.vel.mag() > 3) {
            this.vel.normalize();
        }
        this.pos = this.pos.copy().add(this.vel.copy().mult(dt)).add(this.acc.copy().mult(dt * dt * 0.5));
        this.acc.x = 0;
        this.acc.y = 0;
    }

    private void edges(double dt) {
        double friction = 0.006;
        if (this.pos.y + this.radius > this.getWorld().getHeight()) {
            this.vel.y *= -0.5;
            this.vel = this.vel.copy().add(this.vel.copy().mult(friction).mult(-1));
            this.rotationalVelocity += -this.rotationalVelocity * friction;
            this.pos.y = this.getWorld().getHeight() - this.radius;
        }
        if (this.pos.x - this.radius < 0) {
            this.vel.x *= -1;
            this.vel = this.vel.copy().add(this.vel.copy().mult(friction).mult(-1));
            this.rotationalVelocity += -this.rotationalVelocity * friction;
            this.pos.x = this.radius;
        }
        if (this.pos.x + this.radius > this.getWorld().getWidth()) {
            this.vel.x *= -1;
            this.vel = this.vel.copy().add(this.vel.copy().mult(friction).mult(-1));
            this.rotationalVelocity += -this.rotationalVelocity * friction;
            this.pos.x = this.getWorld().getWidth() - this.radius;
        }
    }

    @Override
    public double getX() {
        return this.pos.x - this.radius;
    }

    @Override
    public double getY() {
        return this.pos.y - this.radius;
    }
}
