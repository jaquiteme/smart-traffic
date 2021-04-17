package com.jordy.backup.events;

public interface DistributedMessageListener<T> {
    public void messageToDistribute(DistributedEvent<T> event);
}
