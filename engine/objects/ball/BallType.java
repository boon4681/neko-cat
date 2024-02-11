package engine.objects.ball;

import engine.World;

public enum BallType {
    BALL1(Ball1.class, Ball2.class),
    BALL2(Ball2.class, Ball3.class),
    BALL3(Ball3.class, Ball4.class),
    BALL4(Ball4.class, Ball5.class),
    BALL5(Ball5.class, Ball6.class),
    BALL6(Ball6.class, Ball7.class),
    BALL7(Ball7.class, Ball8.class),
    BALL8(Ball8.class, Ball9.class),
    BALL9(Ball9.class, Ball10.class),
    BALL10(Ball10.class, Ball11.class),
    BALL11(Ball11.class, Ball1.class),
    ;

    private Class<?> ball;
    private Class<?> to;

    <T extends BallBase, V extends BallBase> BallType(Class<T> base, Class<V> to) {
        this.ball = base;
        this.to = to;
    }

    @SuppressWarnings("unchecked")
    public <T> T createBase(World world) {
        try {
            return (T) this.ball.getConstructor(World.class).newInstance(world);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T createTo(World world) {
        try {
            return (T) this.to.getConstructor(World.class).newInstance(world);
        } catch (Exception e) {
            return null;
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

}
