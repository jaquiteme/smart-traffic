package com.jordy.gateway.mqtt.runnables;

import com.jordy.gateway.app.events.EventTag;
import com.jordy.gateway.app.events.JobQueue;
import com.jordy.gateway.mqtt.models.Job;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttSubscriber implements Runnable {

    private MqttClient mqttClient;
    private String topic;
    private int qos = 1;
    private JobQueue jobQueue;

    public MqttSubscriber(MqttClient mqttClient, String topic, JobQueue jobQueue) {
        this.mqttClient = mqttClient;
        this.topic = topic;
        this.jobQueue = jobQueue;

    }

    @Override
    public void run() {
        if (this.mqttClient.isConnected()) {
            System.out.println("Connected to broker");
            try {
                this.mqttClient.subscribe(topic, qos, new IMqttMessageListener() {
                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        System.out.println("New message: " + message);
                        System.out.println("Topic: " + topic);
                        topicProccessing(topic, message.toString());
                    }

                });
            } catch (MqttException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Waiting messages... ");
        while (this.mqttClient.isConnected()) {

        }

        try {
            System.out.println("Disconnecting... ");
            this.mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void topicProccessing(String topic, String content) {
        switch (topic) {
        case "cars/data/DENM/accident":
            this.jobQueue.newTodo(new Job(EventTag.ROAD_ACCIDENT_EVENT, content));
            break;
        case "cars/data/CAM/zone/in":
            this.jobQueue.newTodo(new Job(EventTag.ZONE_IN_EVENT, content));
            break;
        case "cars/data/CAM/zone/out":
            this.jobQueue.newTodo(new Job(EventTag.ZONE_OUT_EVENT, content));
            break;
        case "cars/data/DENM/roadworks":
            this.jobQueue.newTodo(new Job(EventTag.ROAD_WORKS_EVENT, content));
            break;
        case "cars/data/DENM/roadfog":
            this.jobQueue.newTodo(new Job(EventTag.ROAD_FOG_EVENT, content));
            break;

        default:
            this.jobQueue.newTodo(new Job(EventTag.NO_EVENT, content));
            break;
        }
    }
}
