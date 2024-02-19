package engine.event;

public interface IEvent<T> {
    public String getName();

    public T getDetails();
}
