package engine.event.mouse;

import engine.event.Event;

public class MouseMoveEvent extends Event<MousePosition> {

    public MouseMoveEvent(int x, int y) {
        super("mousemove", new MousePosition(x, y));
    }
}
