package com.jordy.gateway.xmpp.runnables;

import com.jordy.gateway.app.events.DistributedCAMQueue;
import com.jordy.gateway.app.events.DistributedDENMQueue;
import com.jordy.gateway.app.events.DistributedEvent;
import com.jordy.gateway.app.events.DistributedMessageListener;
import com.jordy.gateway.mqtt.models.RoadMessage;
import com.jordy.gateway.xmpp.models.Alert;
import com.jordy.gateway.xmpp.nodes.CustomNode;
import com.jordy.gateway.xmpp.nodes.CustomNodeConfig;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smackx.pubsub.AccessModel;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.PayloadItem;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.PublishModel;
import org.jivesoftware.smackx.pubsub.PubSubException.NotALeafNodeException;
import org.jivesoftware.smackx.pubsub.PubSubException.NotAPubSubNodeException;
import org.jivesoftware.smackx.xdata.packet.DataForm;

public class XmppPublisher implements Runnable {
    private AbstractXMPPConnection connectionInstance;
    private PubSubManager pubManager;
    private DistributedDENMQueue denmQueueInstance;
    private DistributedCAMQueue camQueueInstance;
    LeafNode denmNode = null;
    LeafNode camNode = null;

    public XmppPublisher(AbstractXMPPConnection connection) {
        this.connectionInstance = connection;
        this.denmQueueInstance = new DistributedDENMQueue();
        this.camQueueInstance = new DistributedCAMQueue();
    }

    @Override
    public void run() {
        this.pubManager = PubSubManager.getInstance(this.connectionInstance);
        /**
         * DENM EVENTS
         */
        try {
            denmNode = this.pubManager.getNode("denm_events");
        } catch (NotAPubSubNodeException | NoResponseException | XMPPErrorException | NotConnectedException
                | InterruptedException e) {
            e.printStackTrace();
            try {
                CustomNodeConfig denmNodeConfig = new CustomNodeConfig(DataForm.Type.submit, AccessModel.open, true,
                        true, true, PublishModel.open);
                CustomNode denm = new CustomNode(this.pubManager, "denm_events", denmNodeConfig);
                denm.create();
            } catch (NotALeafNodeException e1) {
                e1.printStackTrace();
            }
        }
        /**
         * CAM EVENTS
         */
        try {
            camNode = this.pubManager.getNode("cam_events");
        } catch (NotAPubSubNodeException | NoResponseException | XMPPErrorException | NotConnectedException
                | InterruptedException e) {
            e.printStackTrace();
            try {
                CustomNodeConfig camNodeConfig = new CustomNodeConfig(DataForm.Type.submit, AccessModel.open, true,
                        true, true, PublishModel.open);
                CustomNode cam = new CustomNode(this.pubManager, "cam_events", camNodeConfig);
                cam.create();
            } catch (NotALeafNodeException e1) {
                e1.printStackTrace();
            }
        }
        /**
         * Mqtt distributed message queue listener
         */
        this.camQueueInstance.addDistributedMessageListener(new DistributedMessageListener<RoadMessage>() {
            @Override
            public void messageToDistribute(DistributedEvent<RoadMessage> event) {
                try {
                    PayloadItem<RoadMessage> newCamItem = new PayloadItem<>(event.getMessage());
                    camNode.publish(newCamItem);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        this.denmQueueInstance.addDistributedMessageListener(new DistributedMessageListener<Alert>() {
            @Override
            public void messageToDistribute(DistributedEvent<Alert> event) {
                try {
                    PayloadItem<Alert> newAccidentItem = new PayloadItem<>(event.getMessage());
                    denmNode.publish(newAccidentItem);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        // System.out.println("Waiting for topic...");
        while (this.connectionInstance.isConnected()) {
        }

        this.connectionInstance.disconnect();
    }

}
