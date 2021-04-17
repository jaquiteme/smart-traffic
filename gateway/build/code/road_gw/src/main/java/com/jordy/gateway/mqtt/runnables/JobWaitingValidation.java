package com.jordy.gateway.mqtt.runnables;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jordy.gateway.app.App;
import com.jordy.gateway.app.events.DistributedDENMQueue;
import com.jordy.gateway.app.events.JobEvent;
import com.jordy.gateway.app.events.JobEventListener;
import com.jordy.gateway.app.events.JobQueue;
import com.jordy.gateway.mqtt.models.DenmMessage;
import com.jordy.gateway.mqtt.models.Job;
import com.jordy.gateway.mqtt.models.Position;
import com.jordy.gateway.xmpp.models.Alert;

public class JobWaitingValidation implements Runnable {
    private int DEFAULT_IDLE_TIME = 20;
    private int idleTime;
    private JobQueue queue;
    private int tag;
    private int condition;
    private List<DenmMessage> pendingMessages;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public JobWaitingValidation(JobQueue inputQueue, int idleTime, int condition, int cause) {
        this.queue = inputQueue;
        this.DEFAULT_IDLE_TIME = idleTime;
        this.condition = condition;
        this.tag = cause;
        this.pendingMessages = new ArrayList<>();
    }

    @Override
    public void run() {
        this.idleTime = DEFAULT_IDLE_TIME;
        // get the first pending job in queue
        Job firstJob = (Job) this.queue.peekAJob();
        // Add a DENM message to the following list
        try {
            this.pendingMessages.add(convertToDenmMessage(firstJob));
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }
        // Listen to new incomming messages
        JobEventListener listener = new JobEventListener() {
            @Override
            public void jobArrived(JobEvent jobEvent) {
                try {
                    pendingMessages.add(convertToDenmMessage(jobEvent.getJob()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                //
                if (pendingMessages.size() < condition) {
                    setIdleTime(DEFAULT_IDLE_TIME);
                } else {
                    // Call validate method
                    queue.clearJobQueue();
                    try {
                        validate(pendingMessages);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    System.out.println("QUEUE: " + queue.isQueueEmpty());
                }

            }

        };
        UUID registerId = UUID.randomUUID();
        this.queue.addJobEventListener(registerId.toString(), listener);

        while (this.idleTime > 0) {
            --this.idleTime;
            System.out.println("IDLE : " + idleTime);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.queue.clearJobQueue();
        this.pendingMessages.clear();
        this.queue.removeJobEventListener(registerId.toString());

    }

    public int getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(int idleTime) {
        this.idleTime = idleTime;
    }

    public DenmMessage convertToDenmMessage(Job job) throws JsonMappingException, JsonProcessingException {
        JsonNode denmNode = new ObjectMapper().readTree(job.getJobContent());
        DenmMessage denmMessage = new DenmMessage();
        Position position = new Position();
        denmMessage.setStationId(denmNode.get("stationId").asText());
        denmMessage.setStationType(denmNode.get("stationType").asInt());
        denmMessage.setCauseCode(tag);
        position.setLatitude(denmNode.get("positions").get("latitude").asDouble());
        position.setLongitude(denmNode.get("positions").get("longitude").asDouble());
        denmMessage.setPositions(position);
        return denmMessage;
    }

    public void validate(List<DenmMessage> content) throws JsonProcessingException {
        DistributedDENMQueue denmQueueInstance = new DistributedDENMQueue();
        Long currentTime = System.currentTimeMillis();
        Alert alert = new Alert(this.tag, App.gatewayId, content, currentTime);
        denmQueueInstance.newElement(alert);

        setIdleTime(0);
    }

    boolean isRunning() {
        return running.get();
    }
}
