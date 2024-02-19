package engine.event.window;

import java.awt.Dimension;

import engine.event.Event;

public class ResizeEvent extends Event<Dimension> {
    public ResizeEvent(Dimension v) {
        super("window.resize", v);
    }
}
