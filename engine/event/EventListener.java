package engine.event;

public class EventListener<T extends Details> {
    private final String name;
    private final OnEvent<T> listener;

    public static interface OnEvent<T> {
        public void on(T e);
    }

    public EventListener(String name, OnEvent<T> event) {
        this.name = name;
        this.listener = event;
    }

    public final String getName() {
        return this.name;
    }

    public final void on(T details) {
        this.listener.on(details);
    }
}
