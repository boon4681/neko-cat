package engine.event;

import engine.vector.Vec2;

public class MousePosition extends Vec2 implements Details {
    public MousePosition(int x, int y) {
        super(x, y);
    }
}