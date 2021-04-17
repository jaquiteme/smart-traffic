package com.jordy.gateway.app.events;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.jordy.gateway.xmpp.models.Alert;

public class DistributedDENMQueue implements DistributedQueue<Alert> {
    public static BlockingQueue<Alert> queue = new LinkedBlockingDeque<>();
    public static ArrayList<DistributedMessageListener<Alert>> distributedMessageListenerList = new ArrayList<>();

    public DistributedDENMQueue() {
        // DistributedDENMQueue.queue = new LinkedList<>();
    }

    public void newElement(Alert message) {
        DistributedDENMQueue.queue.add(message);
        processDistributedMessageEvent(new DistributedEvent<Alert>(message));
    }

    public Alert getElement() throws InterruptedException {
        return DistributedDENMQueue.queue.take();
    }

    public synchronized void addDistributedMessageListener(DistributedMessageListener<Alert> listener) {
        if (!distributedMessageListenerList.contains(listener)) {
            distributedMessageListenerList.add(listener);
        }
    }

    public void processDistributedMessageEvent(DistributedEvent<Alert> event) {
        ArrayList<DistributedMessageListener<Alert>> tmpdistributedMessageListenerList;

        synchronized (this) {
            if (distributedMessageListenerList.size() == 0)
                return;
            tmpdistributedMessageListenerList = (ArrayList<DistributedMessageListener<Alert>>) distributedMessageListenerList
                    .clone();
        }

        for (DistributedMessageListener<Alert> listener : tmpdistributedMessageListenerList) {
            listener.messageToDistribute(event);
        }
    }

}
