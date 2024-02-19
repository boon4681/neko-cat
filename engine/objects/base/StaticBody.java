package engine.objects.base;

import engine.World;
import engine.vector.Vec2;

public abstract class StaticBody extends Body {
    public StaticBody(World world) {
        this(world, new Vec2(), new Vec2(), new Vec2(), new Vec2(), 1);
    }

    public StaticBody(World world, Vec2 pos, Vec2 size) {
        this(world, pos, new Vec2(), new Vec2(), size, 1);
    }

    public StaticBody(World world, Vec2 pos, Vec2 vel, Vec2 size) {
        this(world, pos, vel, new Vec2(), size, 1);
    }

    public StaticBody(World world, Vec2 pos, Vec2 vel, Vec2 acc, Vec2 size) {
        this(world, pos, vel, acc, size, 1);
    }

    public StaticBody(World world, Vec2 pos, Vec2 vel, Vec2 acc, Vec2 size, double mass) {
        super(world, pos, vel, acc, size, mass);
    }

    @Override
    public void update(double dt) {
    }

    @Override
    public void updatePosition(double dt) {
    }

    @Override
    public BodyType getBodyType() {
        return BodyType.STATIC;
    }
}
