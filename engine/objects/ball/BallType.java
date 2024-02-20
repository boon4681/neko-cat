package engine.objects.ball;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import engine.World;
import engine.vector.Vec2;

public enum BallType {
    BALL1(Ball1.class, Ball2.class, 1),
    BALL2(Ball2.class, Ball3.class, 2),
    BALL3(Ball3.class, Ball4.class, 4),
    BALL4(Ball4.class, Ball5.class, 8),
    BALL5(Ball5.class, Ball6.class, 16),
    BALL6(Ball6.class, Ball7.class, 32),
    BALL7(Ball7.class, Ball8.class, 64),
    BALL8(Ball8.class, Ball9.class, 128),
    BALL9(Ball9.class, Ball10.class, 256),
    BALL10(Ball10.class, Ball11.class, 512),
    BALL11(Ball11.class, Ball1.class, 1024),
    ;

    private Class<?> ball;
    private Class<?> to;
    private int score;

    <T extends BallBase, V extends BallBase> BallType(Class<T> base, Class<V> to, int score) {
        this.ball = base;
        this.to = to;
        this.score = score;
    }

    @SuppressWarnings("unchecked")
    public <T> T createBase(World world, Vec2 pos) {
        try {
            return (T) this.ball.getConstructor(World.class, Vec2.class).newInstance(world, pos);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T createTo(World world, Vec2 pos) {
        try {
            return (T) this.to.getConstructor(World.class, Vec2.class).newInstance(world, pos);
        } catch (Exception e) {
            return null;
        }
    }

    public BufferedImage getImage() {
        try {
            Field image = this.ball.getDeclaredField("image");
            image.setAccessible(true);
            return (BufferedImage) image.get(null);
        } catch (Exception e) {
            return null;
        }
    }

    public <T extends BallBase> int getRadius() {
        try {
            T ball = createBase(null, null);
            return ball.radius;
        } catch (Exception e) {
            e.printStackTrace();
            return 300;
        }
    }

    public boolean isBase(Object obj) {
        if (this.ball.isInstance(obj)) {
            return true;
        }
        return false;
    }

    public boolean isTo(Object obj) {
        if (this.to.isInstance(obj)) {
            return true;
        }
        return false;
    }

    public int getScore() {
        return this.score;
    }
}
