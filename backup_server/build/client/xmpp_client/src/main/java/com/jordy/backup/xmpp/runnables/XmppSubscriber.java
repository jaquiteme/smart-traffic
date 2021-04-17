package com.jordy.backup.xmpp.runnables;

import java.util.List;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.jordy.backup.events.DistributedDataStoreQueue;
import com.jordy.backup.xmpp.models.Payload;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.ItemPublishEvent;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.PubSubException.NotAPubSubNodeException;
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener;
import org.json.JSONObject;
import org.json.XML;

public class XmppSubscriber implements Runnable {
    private AbstractXMPPConnection connectionInstance;
    private PubSubManager pubManager;
    LeafNode denmNode = null;
    LeafNode camNode = null;

    public XmppSubscriber(AbstractXMPPConnection connection) {
        this.connectionInstance = connection;
    }

    @Override
    public void run() {
        this.pubManager = PubSubManager.getInstance(this.connectionInstance);
        /**
         * DENM EVENTS
         */
        try {
            denmNode = this.pubManager.getNode("denm_events");
            denmNode.subscribe(this.connectionInstance.getUser().asBareJid().toString());
            denmNode.addItemEventListener(new ItemEventListener<Item>() {
                private ItemPublishEvent<Item> items;
                @Override
                public void handlePublishedItems(ItemPublishEvent<Item> items) {
                    this.items = items;
                    DistributedDataStoreQueue dataQueue = new DistributedDataStoreQueue();
                    List<Item> newItems = items.getItems();
                    for (Item i : newItems) {
                        JSONObject json =  XML.toJSONObject(i.toXML("").toString());
                        String data = json.getJSONObject("item").getJSONObject("alert").toString();
                        dataQueue.newElement(new Payload("/alert_events", data));
                    }
                }

            });
        } catch (NotAPubSubNodeException | NoResponseException | XMPPErrorException | NotConnectedException
                | InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * CAM EVENTS
         */
        try {
            camNode = this.pubManager.getNode("cam_events");
            camNode.subscribe(this.connectionInstance.getUser().asBareJid().toString());
            camNode.addItemEventListener(new ItemEventListener<Item>() {
                private ItemPublishEvent<Item> items;

                @Override
                public void handlePublishedItems(ItemPublishEvent<Item> items) {
                    this.items = items;
                    DistributedDataStoreQueue dataQueue = new DistributedDataStoreQueue();
                    List<Item> newItems = items.getItems();
                    for (Item i : newItems) {
                        JSONObject json =  XML.toJSONObject(i.toXML("").toString());
                        String data = json.getJSONObject("item").getJSONObject("road-message").toString();
                        dataQueue.newElement(new Payload("/road_messages", data));
                        // System.err.println("ITEM " + i.toXML(""));
                    }
                }
            });

        } catch (NotAPubSubNodeException | NoResponseException | XMPPErrorException | NotConnectedException
                | InterruptedException e) {
            e.printStackTrace();
        }

        // System.out.println("Waiting for topic...");
        while (this.connectionInstance.isConnected()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                this.connectionInstance.disconnect();
            }
        }

        this.connectionInstance.disconnect();
    }

}
