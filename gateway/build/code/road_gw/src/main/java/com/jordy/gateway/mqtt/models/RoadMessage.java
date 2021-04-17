package com.jordy.gateway.mqtt.models;

import java.io.Serializable;
import java.util.List;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class RoadMessage implements Serializable, ExtensionElement {
    private int tag;
    private String gatewayId;
    private List<CamMessage> content;
    public static final String NAMESPACE = "http://jabber.org/protocol/road-message";
    public static final String ELEMENT = "road-message";
    private Long created_at;
    private static final long serialVersionUID = 1L;

    public RoadMessage(int tag, String gatewayId, List<CamMessage> content, Long created_at) {
        this.tag = tag;
        this.gatewayId = gatewayId;
        this.content = content;
        this.created_at = created_at;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public List<?> getContent() {
        return content;
    }

    public void setContent(List<CamMessage> content) {
        this.content = content;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public CharSequence serializeContent(List<CamMessage> list) {
        String result = "";
        for (CamMessage item : list) {
            result += item.toXML(null);
        }
        return result;
    }

    @Override
    public String getElementName() {
        return ELEMENT;
    }

    @Override
    public CharSequence toXML(String enclosingNamespace) {
        XmlStringBuilder xml = new XmlStringBuilder(this, enclosingNamespace);
        xml.optAttribute("tag", String.valueOf(tag));
        xml.rightAngleBracket();
        xml.optElement("gateway-id", gatewayId);
        xml.openElement("content");
        xml.append(serializeContent(content));
        xml.closeElement("content");
        xml.optElement("created_at", created_at);
        xml.closeElement(this);
        return xml;
    }

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

}
