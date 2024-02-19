package engine.objects.base;

import engine.World;
import engine.vector.Vec2;

public abstract class DynamicBody extends Body {
    public DynamicBody(World world) {
        this(world, new Vec2(), new Vec2(), new Vec2(), new Vec2(), 1);
    }

    public DynamicBody(World world, Vec2 pos, Vec2 size) {
        this(world, pos, new Vec2(), new Vec2(), size, 1);
    }

    public DynamicBody(World world, Vec2 pos, Vec2 vel, Vec2 size) {
        this(world, pos, vel, new Vec2(), size, 1);
    }

    public DynamicBody(World world, Vec2 pos, Vec2 vel, Vec2 acc, Vec2 size) {
        this(world, pos, vel, acc, size, 1);
    }

    public DynamicBody(World world, Vec2 pos, Vec2 vel, Vec2 acc, Vec2 size, double mass) {
        super(world, pos, vel, acc, size, mass);
    }

    @Override
    public BodyType getBodyType() {
        return BodyType.DYNAMIC;
    }
}
