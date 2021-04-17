package com.jordy.backup.xmpp.runnables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.jordy.backup.events.DistributedDataStoreQueue;
import com.jordy.backup.events.DistributedEvent;
import com.jordy.backup.events.DistributedMessageListener;
import com.jordy.backup.xmpp.models.Payload;

public class DataStore implements Runnable {
    DistributedDataStoreQueue dataQueueInstance = new DistributedDataStoreQueue();
    static final String API_END_POINT = "http://10.5.0.21:8080/api";

    @Override
    public void run() {
        DistributedMessageListener<Payload> dataQueueListener = new DistributedMessageListener<Payload>() {
            @Override
            public void messageToDistribute(DistributedEvent<Payload> event) {
                // System.out.println(event.getMessage());
                try {
                    postData(event.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
        this.dataQueueInstance.addDistributedMessageListener(dataQueueListener);

        while (true) {

        }
    }

    public boolean postData(Payload payload) throws IOException {
        URL API_CONTEXT = new URL(API_END_POINT + payload.getUri());
        HttpURLConnection connection = (HttpURLConnection) API_CONTEXT.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        connection.setRequestProperty("Accept", "application/json");
        // PAYLOAD
        try (OutputStream os = connection.getOutputStream()) {
            byte[] _payload = payload.getData().getBytes("utf-8");
            os.write(_payload, 0, _payload.length);
        }
        // READ & SEND REQ
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }

        return true;
    }

}
