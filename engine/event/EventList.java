package engine.event;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class EventList {
    private ArrayList<EventListener<?>> list = new ArrayList<>();
    private Queue<Event<?>> events = new ArrayDeque<>();

    public int addEventListener(EventListener<?> e) {
        return list.add(e) ? this.list.size() - 1 : -1;
    }

    public <T extends Details> void dispatchEvent(Event<T> e) {
        this.events.add(e);
    }

    @SuppressWarnings("unchecked")
    private <T extends Details> void _dispatchEvent(Event<T> e) {
        for (EventListener<?> listener : list) {
            if (e.isStopped()) {
                break;
            }
            if (listener.getName().equals(e.name)) {
                EventListener<T> listen = (EventListener<T>) listener;
                listen.on(e.details);
            }
        }
    }

    public void flush() {
        while (this.events.size() > 0) {
            Event<?> e = this.events.poll();
            this._dispatchEvent(e);
        }
    }
}
