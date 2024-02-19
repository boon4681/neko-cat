package engine.event.mouse;

import engine.event.Event;

public class MouseUp extends Event<MousePosition> {

    public MouseUp(int x, int y) {
        super("mouseup", new MousePosition(x, y));
    }
}
