package com.jordy.gateway.app.events;

public interface DistributedQueue<T> {

    public void newElement(T message);

    public Object getElement() throws InterruptedException;

    public void addDistributedMessageListener(DistributedMessageListener<T> listener);

    public void processDistributedMessageEvent(DistributedEvent<T> event);

}
