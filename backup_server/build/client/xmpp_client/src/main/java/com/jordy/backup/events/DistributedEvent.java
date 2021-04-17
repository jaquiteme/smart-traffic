package com.jordy.backup.events;

public class DistributedEvent<T> {
    private T message;

    public DistributedEvent(T message) {
        this.message = message;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }

}
