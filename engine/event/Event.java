package engine.event;

public abstract class Event<T> implements IEvent<T> {
    protected final T details;
    protected final String name;

    private boolean stopPropagation = false;

    public Event(String name, T v) {
        this.name = name;
        this.details = v;
    }

    public final void setStopPropagation(boolean stop) {
        this.stopPropagation = stop;
    }

    public final boolean isStopped() {
        return this.stopPropagation;
    }

    public final String getName() {
        return this.name;
    }

    public final T getDetails() {
        return this.details;
    }
}
