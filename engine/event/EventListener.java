package engine.event;

public class EventListener<T> {
    private final String name;
    private final OnEvent<T> listener;

    public static interface OnEvent<T> {
        public boolean on(T e);
    }

    public EventListener(String name, OnEvent<T> event) {
        this.name = name;
        this.listener = event;
    }

    public final String getName() {
        return this.name;
    }

    public final boolean on(T details) {
        return this.listener.on(details);
    }
}
