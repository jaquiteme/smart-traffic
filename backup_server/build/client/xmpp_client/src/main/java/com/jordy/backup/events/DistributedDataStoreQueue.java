package com.jordy.backup.events;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.jordy.backup.xmpp.models.Payload;

public class DistributedDataStoreQueue implements DistributedQueue<Payload> {
    public static BlockingQueue<Payload> queue = new LinkedBlockingDeque<>();
    public static ArrayList<DistributedMessageListener<Payload>> distributedMessageListenerList = new ArrayList<>();

    public DistributedDataStoreQueue() {
        // DistributedCAMQueue.queue = new LinkedList<>();
    }

    public void newElement(Payload message) {
        DistributedDataStoreQueue.queue.add(message);
        processDistributedMessageEvent(new DistributedEvent<Payload>(message));
    }

    public Payload getElement() throws InterruptedException {
        return DistributedDataStoreQueue.queue.take();
    }

    public synchronized void addDistributedMessageListener(DistributedMessageListener<Payload> listener) {
        if (!distributedMessageListenerList.contains(listener)) {
            distributedMessageListenerList.add(listener);
        }
    }

    public void processDistributedMessageEvent(DistributedEvent<Payload> event) {
        ArrayList<DistributedMessageListener<Payload>> tmpdistributedMessageListenerList;

        synchronized (this) {
            if (distributedMessageListenerList.size() == 0)
                return;
            tmpdistributedMessageListenerList = (ArrayList<DistributedMessageListener<Payload>>) distributedMessageListenerList
                    .clone();
        }

        for (DistributedMessageListener<Payload> listener : tmpdistributedMessageListenerList) {
            listener.messageToDistribute(event);
        }
    }

}
