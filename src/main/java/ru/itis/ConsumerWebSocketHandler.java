package ru.itis;

public interface ConsumerWebSocketHandler<T extends MessageI> {
    void setCallback(ConsumerCallback<T> callback);
}
