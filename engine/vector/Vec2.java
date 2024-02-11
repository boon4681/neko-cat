package engine.vector;

public class Vec2 {
    public double x, y;

    public Vec2() {
        this(0, 0);
    }

    public Vec2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 add(Vec2 other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vec2 sub(Vec2 other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    public Vec2 mult(Vec2 other) {
        this.x *= other.x;
        this.y *= other.y;
        return this;
    }

    public Vec2 mult(double v) {
        this.x *= v;
        this.y *= v;
        return this;
    }

    public Vec2 div(Vec2 other) {
        this.x *= other.x;
        this.y *= other.y;
        return this;
    }

    public Vec2 div(double v) {
        this.x /= v;
        this.y /= v;
        return this;
    }

    public double mag() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public Vec2 normalize() {
        double mag = this.mag();
        double x = this.x / mag;
        double y = this.y / mag;
        return new Vec2(x, y);
    }

    public double dot(Vec2 v) {
        return this.x * v.x + this.y * v.y;
    }

    public double cross(Vec2 v) {
        return this.x * v.y - this.y * v.x;
    }

    public double atan() {
        return Math.atan(this.y / this.x);
    }

    public Vec2 copy() {
        return new Vec2(this.x, this.y);
    }

    @Override
    public String toString() {
        return this.x + "," + this.y;
    }
}
