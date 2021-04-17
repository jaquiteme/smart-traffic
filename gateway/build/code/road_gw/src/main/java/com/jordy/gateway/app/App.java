package com.jordy.gateway.app;

import java.io.IOException;

import com.jordy.gateway.app.events.JobQueue;
import com.jordy.gateway.mqtt.models.Topic;
import com.jordy.gateway.mqtt.runnables.MqttSubscriber;
import com.jordy.gateway.mqtt.runnables.TopicProccessing;
import com.jordy.gateway.xmpp.runnables.XmppPublisher;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;

public class App {
    public static String gatewayId = "FR51100-JAURES-GW-001";
    public static void main(String[] args) throws MqttException {
        String broker = "tcp://10.5.0.8:1883";
        MqttClient mqttClient = null;
        //
        String xmppUser = "gateway01";
        String xmppUserPwd = "Gateway0!";
        String xmppUserDomain = "example.com";
        AbstractXMPPConnection xmppConnection = null;
        /*
         * Mqtt connexion
         */
        try {
            mqttClient = new MqttClient(broker, gatewayId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(20);
            mqttClient.connect(options);
        } catch (MqttException e) {
            System.out.println("reason " + e.getReasonCode());
            System.out.println("msg " + e.getMessage());
            System.out.println("loc " + e.getLocalizedMessage());
            System.out.println("cause " + e.getCause());
            System.out.println("excep " + e);
            e.printStackTrace();
        }
        // mqttClient.disconnect();
        /**
         * XMPP connection
         */
        XMPPTCPConnectionConfiguration config = null;
        try {
            config = XMPPTCPConnectionConfiguration.builder().setUsernameAndPassword(xmppUser, xmppUserPwd)
                    .setXmppDomain(xmppUserDomain).setHost("example.com").addEnabledSaslMechanism("SCRAM-SHA-1")
                    .build();
        } catch (XmppStringprepException e1) {
            e1.printStackTrace();
        }

        xmppConnection = new XMPPTCPConnection(config);

        try {
            xmppConnection.connect();
        } catch (InterruptedException | SmackException | XMPPException | IOException e) {
            e.printStackTrace();
        }

        try {
            xmppConnection.login();
        } catch (InterruptedException | XMPPException | SmackException | IOException e) {
            e.printStackTrace();
        }

        /**
         * Queue init
         */
        // DistributedQueue camQeue = new DistributedQueue();
        // DistributedQueue denmQeue = new DistributedQueue();
        JobQueue jobQueue = new JobQueue();

        /**
         * MQTT THREADS
         */
        if (mqttClient.isConnected()) {
            MqttSubscriber cam = new MqttSubscriber(mqttClient, Topic.CAM_TOPICS, jobQueue);
            Thread camThread = new Thread(cam);
            camThread.start();
            //
            MqttSubscriber denm = new MqttSubscriber(mqttClient, Topic.DENM_TOPICS, jobQueue);
            Thread denmThread = new Thread(denm);
            denmThread.start();
        }

        /**
         * XMPP THREADS
         */
        if (xmppConnection.isConnected()) {
            XmppPublisher xmpp = new XmppPublisher(xmppConnection);
            Thread xmppThread = new Thread(xmpp);
            xmppThread.start();
        }
        /**
         * PROCESSING THREADS
         */
        TopicProccessing topicProccessing = new TopicProccessing(jobQueue);
        Thread jobThread = new Thread(topicProccessing);
        jobThread.start();
    }
}
