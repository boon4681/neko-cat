package engine.objects.base;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

import javax.imageio.ImageIO;

import engine.World;
import engine.vector.Vec2;

public abstract class Body implements IObject {

    private UUID uuid;
    private World world;
    public Vec2 pos;
    public Vec2 vel;
    public Vec2 acc;
    public Vec2 size;
    public double mass;
    public double rotationalVelocity;
    public double rotationalInertial;
    public double rotationalAcc;
    public double rotation;
    public boolean rotateAtCenter = false;
    public boolean visible = true;
    public double age = 0;

    public boolean isVisible() {
        return this.visible;
    }

    public abstract ShapeType getShapeType();

    public abstract BodyType getBodyType();

    public Body(World world) {
        this(world, new Vec2(), new Vec2(), new Vec2(), new Vec2(), 1);
    }

    public Body(World world, Vec2 pos, Vec2 size) {
        this(world, pos, new Vec2(), new Vec2(), size, 1);
    }

    public Body(World world, Vec2 pos, Vec2 vel, Vec2 size) {
        this(world, pos, vel, new Vec2(), size, 1);
    }

    public Body(World world, Vec2 pos, Vec2 vel, Vec2 acc, Vec2 size) {
        this(world, pos, vel, acc, size, 1);
    }

    public Body(World world, Vec2 pos, Vec2 vel, Vec2 acc, Vec2 size, double mass) {
        this.uuid = UUID.randomUUID();
        this.world = world;
        this.pos = pos;
        this.vel = vel;
        this.acc = acc;
        this.mass = mass;
        this.size = size;
        if (this.world != null)
            this.world.add(this);
    }

    public UUID getID() {
        return this.uuid;
    }

    public final void remove() {
        world.remove(this);
    }

    public final void applyForce(Vec2 force) {
        this.acc.add(force.copy().div(this.mass));
    }

    public final void applyRotationalForce(Vec2 pos, Vec2 force) {
        this.rotationalAcc += pos.copy().cross(force) / this.mass;
    }

    public static final BufferedImage loadImage(File file) {
        try {
            return ImageIO.read(file);
        } catch (Exception e) {
            return null;
        }
    }

    public abstract void updatePosition(double dt);

    public abstract double getX();

    public abstract double getY();

    public double getWidth() {
        return this.size.x;
    }

    public double getHeight() {
        return this.size.y;
    }

    @Override
    public World getWorld() {
        return this.world;
    }
}
