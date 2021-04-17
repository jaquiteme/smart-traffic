package com.jordy.gateway.xmpp.nodes;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.PubSubException.NotALeafNodeException;

public class CustomNode {
    private PubSubManager manager;
    private String nodeName;
    private CustomNodeConfig customNodeConfig;

    public CustomNode() {
    }

    public CustomNode(PubSubManager manager, String nodeName, CustomNodeConfig customNodeConfig) {
        this.manager = manager;
        this.nodeName = nodeName;
        this.customNodeConfig = customNodeConfig;
    }

    public PubSubManager getManager() {
        return manager;
    }

    public void setManager(PubSubManager manager) {
        this.manager = manager;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public CustomNodeConfig getCustomNodeConfig() {
        return customNodeConfig;
    }

    public void setCustomNodeConfig(CustomNodeConfig customNodeConfig) {
        this.customNodeConfig = customNodeConfig;
    }

    public void create() throws NotALeafNodeException {
        try {
            LeafNode leaf = this.manager.getOrCreateLeafNode(this.nodeName);
            leaf.sendConfigurationForm(customNodeConfig);
        } catch (NoResponseException | XMPPErrorException | NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
