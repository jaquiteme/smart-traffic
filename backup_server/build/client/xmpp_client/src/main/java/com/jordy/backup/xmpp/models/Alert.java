package com.jordy.backup.xmpp.models;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class Alert implements Serializable, ExtensionElement {

    private int tag;
    private List<DenmMessage> content;
    private Long created_at;
    public static final String NAMESPACE = "http://jabber.org/protocol/alert";
    public static final String ELEMENT = "alert";
    private static final Logger log = Logger.getLogger(Alert.class.getName());
    private static final long serialVersionUID = 1L;

    public Alert() {
    }

    public Alert(int tag, List<DenmMessage> content, Long created_at) {
        this.tag = tag;
        this.content = content;
        this.created_at = created_at;
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

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public List<DenmMessage> getContent() {
        return content;
    }

    public void setContent(List<DenmMessage> content) {
        this.content = content;
    }

    public CharSequence serializeContent(List<DenmMessage> list) {
        String result = "";
        for (DenmMessage item : list) {
            result += item.toXML(null);
        }
        return result;
    }

}
