package com.jordy.gateway.app.events;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.jordy.gateway.mqtt.models.RoadMessage;

public class DistributedCAMQueue implements DistributedQueue<RoadMessage> {
    public static BlockingQueue<RoadMessage> queue = new LinkedBlockingDeque<>();
    public static ArrayList<DistributedMessageListener<RoadMessage>> distributedMessageListenerList = new ArrayList<>();

    public DistributedCAMQueue() {
        // DistributedCAMQueue.queue = new LinkedList<>();
    }

    public void newElement(RoadMessage message) {
        DistributedCAMQueue.queue.add(message);
        processDistributedMessageEvent(new DistributedEvent<RoadMessage>(message));
    }

    public RoadMessage getElement() throws InterruptedException {
        return DistributedCAMQueue.queue.take();
    }

    public synchronized void addDistributedMessageListener(DistributedMessageListener<RoadMessage> listener) {
        if (!distributedMessageListenerList.contains(listener)) {
            distributedMessageListenerList.add(listener);
        }
    }

    public void processDistributedMessageEvent(DistributedEvent<RoadMessage> event) {
        ArrayList<DistributedMessageListener<RoadMessage>> tmpdistributedMessageListenerList;

        synchronized (this) {
            if (distributedMessageListenerList.size() == 0)
                return;
            tmpdistributedMessageListenerList = (ArrayList<DistributedMessageListener<RoadMessage>>) distributedMessageListenerList
                    .clone();
        }

        for (DistributedMessageListener<RoadMessage> listener : tmpdistributedMessageListenerList) {
            listener.messageToDistribute(event);
        }
    }

}
