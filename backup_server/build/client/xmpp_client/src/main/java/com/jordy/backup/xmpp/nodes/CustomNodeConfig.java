package com.jordy.backup.xmpp.nodes;

import org.jivesoftware.smackx.pubsub.AccessModel;
import org.jivesoftware.smackx.pubsub.PublishModel;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.packet.DataForm;

public class CustomNodeConfig extends Form{
    private AccessModel accessModel;
    private Boolean deliverPayload;
    private Boolean notifyRetract;
    private Boolean persistence;
    private PublishModel publishModel;

    public CustomNodeConfig(DataForm.Type type, AccessModel accessModel, Boolean deliverPayload, Boolean notifyRetract,
            Boolean persistence, PublishModel publishModel) {
        super(type);
        this.accessModel = accessModel;
        this.deliverPayload = deliverPayload;
        this.notifyRetract = notifyRetract;
        this.persistence = persistence;
        this.publishModel = publishModel;
    }

    public AccessModel getAccessModel() {
        return accessModel;
    }

    public void setAccessModel(AccessModel accessModel) {
        this.accessModel = accessModel;
    }

    public Boolean getDeliverPayload() {
        return deliverPayload;
    }

    public void setDeliverPayload(Boolean deliverPayload) {
        this.deliverPayload = deliverPayload;
    }

    public Boolean getNotifyRetract() {
        return notifyRetract;
    }

    public void setNotifyRetract(Boolean notifyRetract) {
        this.notifyRetract = notifyRetract;
    }

    public Boolean getPersistence() {
        return persistence;
    }

    public void setPersistence(Boolean persistence) {
        this.persistence = persistence;
    }

    public PublishModel getPublishModel() {
        return publishModel;
    }

    public void setPublishModel(PublishModel publishModel) {
        this.publishModel = publishModel;
    }

}
