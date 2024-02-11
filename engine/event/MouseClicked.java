package engine.event;

public class MouseClicked extends Event<MousePosition> {

    public MouseClicked(int x, int y) {
        super("mouseclick", new MousePosition(x, y));
    }
}
