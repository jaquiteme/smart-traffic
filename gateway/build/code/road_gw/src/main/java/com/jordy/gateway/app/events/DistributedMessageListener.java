package com.jordy.gateway.app.events;

public interface DistributedMessageListener<T> {
    public void messageToDistribute(DistributedEvent<T> event);
}
