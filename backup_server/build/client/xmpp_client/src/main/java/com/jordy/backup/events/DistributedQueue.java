package com.jordy.backup.events;

public interface DistributedQueue<T> {

    public void newElement(T message);

    public T getElement() throws InterruptedException;

    public void addDistributedMessageListener(DistributedMessageListener<T> listener);

    public void processDistributedMessageEvent(DistributedEvent<T> event);

}
