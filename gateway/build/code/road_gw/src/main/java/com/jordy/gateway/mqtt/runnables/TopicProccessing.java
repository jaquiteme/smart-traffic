package com.jordy.gateway.mqtt.runnables;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jordy.gateway.app.App;
import com.jordy.gateway.app.events.DistributedCAMQueue;
import com.jordy.gateway.app.events.DistributedDENMQueue;
import com.jordy.gateway.app.events.EventTag;
import com.jordy.gateway.app.events.JobEvent;
import com.jordy.gateway.app.events.JobEventListener;
import com.jordy.gateway.app.events.JobQueue;
import com.jordy.gateway.mqtt.models.CamMessage;
import com.jordy.gateway.mqtt.models.DenmMessage;
import com.jordy.gateway.mqtt.models.Job;
import com.jordy.gateway.mqtt.models.RoadMessage;
import com.jordy.gateway.xmpp.models.Alert;

public class TopicProccessing implements Runnable {
    private JobQueue jobQueueInstance;
    private JobQueue pendingAccidentQueue;
    private JobQueue pendingJamQueue;
    private DistributedCAMQueue camQueueInstance;
    private DistributedDENMQueue denmQueueInstance;
    private final static Double MIN_SPEED_ALLOWED = 90.0;
    private final static int JAM_IDLE_TIME = 60;
    private final static int ACCIDENT_IDLE_TIME = 60;
    private final static int COND_JAM_VALIDATION = 3;
    private final static int COND_ACCIDENT_VALIDATION = 2;

    public TopicProccessing(JobQueue jobQueueInstance) {
        this.jobQueueInstance = jobQueueInstance;
        this.pendingJamQueue = new JobQueue();
        this.pendingAccidentQueue = new JobQueue();
        this.camQueueInstance = new DistributedCAMQueue();
        this.denmQueueInstance = new DistributedDENMQueue();
    }

    @Override
    public void run() {
        UUID registerId = UUID.randomUUID();
        this.jobQueueInstance.addJobEventListener(registerId.toString(), new JobEventListener() {
            @Override
            public void jobArrived(JobEvent jobEvent) {
                try {
                    Job newJob = jobQueueInstance.getJob();
                    jobProcessing(newJob);
                } catch (Exception e) {
                    System.out.println("SORRY JOB NOT AVAILABLE OR AN ERROR OCCURED " + e.getMessage());
                }
            }
        });

        while (true) {

        }
    }

    public void jobProcessing(Job job) {
        ObjectMapper mapper = new ObjectMapper();
        switch (job.getTag()) {
        case 0: {
            CamMessage cam = null;
            try {
                mapper = new ObjectMapper();
                cam = mapper.readValue(job.getJobContent(), CamMessage.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            Double speed = cam.getSpeed();
            /*
             * if speed under 90, Mean that traffic is slowing down = "JAM"
             */
            if (speed < MIN_SPEED_ALLOWED) {
                /*
                 * Sending the job in jam queue and waiting for validation we suppose here if 3
                 * CAM message contains sppeed lower than MIN_SPEED_ALLOWED, the system create a
                 * JAM event.
                 */
                if (this.pendingJamQueue.isQueueEmpty()) {
                    // Creating a thread to perform jam validation
                    this.pendingJamQueue.newTodo(job);
                    JobWaitingValidation waitingJamForValidation = new JobWaitingValidation(this.pendingJamQueue,
                            JAM_IDLE_TIME, COND_JAM_VALIDATION, EventTag.ROAD_JAM_EVENT);
                    Thread jamPendingValidationThread = new Thread(waitingJamForValidation);
                    if (!this.pendingJamQueue.isQueueEmpty())
                        jamPendingValidationThread.start();
                } else {
                    this.pendingJamQueue.newTodo(job);
                }
            }
            // distribute the CAM message to XMPP message processor
            List<CamMessage> camMessages = new ArrayList<>();
            camMessages.add(cam);
            RoadMessage roadMessage = new RoadMessage(EventTag.NO_EVENT, App.gatewayId, camMessages,
                    System.currentTimeMillis());
            // this.serializedMessage = new ObjectMapper().writeValueAsString(message);
            this.camQueueInstance.newElement(roadMessage);

            break;
        }
        case 1: {
            // [CAM] zone in message
            CamMessage cam = null;
            try {
                mapper = new ObjectMapper();
                cam = mapper.readValue(job.getJobContent(), CamMessage.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            List<CamMessage> camMessages = new ArrayList<>();
            camMessages.add(cam);
            RoadMessage roadMessage = new RoadMessage(EventTag.ZONE_IN_EVENT,  App.gatewayId, camMessages,
                    System.currentTimeMillis());
            this.camQueueInstance.newElement(roadMessage);
            break;
        }

        case 2: {
            // [CAM] zone out message
            CamMessage cam = null;
            try {
                mapper = new ObjectMapper();
                cam = mapper.readValue(job.getJobContent(), CamMessage.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            List<CamMessage> camMessages = new ArrayList<>();
            camMessages.add(cam);
            RoadMessage roadMessage = new RoadMessage(EventTag.ZONE_OUT_EVENT,  App.gatewayId, camMessages,
                    System.currentTimeMillis());
            this.camQueueInstance.newElement(roadMessage);
            break;
        }
        case 4: {
            // A [DENM] accident message
            if (this.pendingAccidentQueue.isQueueEmpty()) {
                this.pendingAccidentQueue.newTodo(job);
                // Creating a thread to perform accident validation
                JobWaitingValidation waitingAccidentForValidation = new JobWaitingValidation(this.pendingAccidentQueue,
                        ACCIDENT_IDLE_TIME, COND_ACCIDENT_VALIDATION, EventTag.ROAD_ACCIDENT_EVENT);
                Thread accidentPendingValidationThread = new Thread(waitingAccidentForValidation);
                if (!this.pendingAccidentQueue.isQueueEmpty())
                    accidentPendingValidationThread.start();
            } else {
                this.pendingAccidentQueue.newTodo(job);
            }
            break;
        }
        default: {
            // All other DENM messages
            DenmMessage denm = null;
            try {
                mapper = new ObjectMapper();
                denm = mapper.readValue(job.getJobContent(), DenmMessage.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            List<DenmMessage> denmMessages = new ArrayList<>();
            denmMessages.add(denm);
            Alert alert = new Alert(denm.getCauseCode(), App.gatewayId, denmMessages, System.currentTimeMillis());
            this.denmQueueInstance.newElement(alert);
            break;
        }
        }
    }
}
