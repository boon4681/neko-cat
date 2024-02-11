package engine.event;

public interface IEvent<T extends Details> {
    public String getName();

    public T getDetails();
}
