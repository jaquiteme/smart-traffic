package com.jordy.gateway.mqtt.models;

import java.io.Serializable;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class Position implements Serializable, ExtensionElement {
    private static final long serialVersionUID = 1L;
    private Double longitude;
    private Double latitude;
    public static final String ELEMENT = "position";

    public Position() {
    }

    public Position(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getElementName() {
        return ELEMENT;
    }

    @Override
    public CharSequence toXML(String enclosingNamespace) {
        XmlStringBuilder xml = new XmlStringBuilder(this);
        xml.rightAngleBracket();
        xml.optElement("latitude", latitude);
        xml.optElement("longitude", longitude);
        xml.closeElement(this);
        return xml;
    }

    @Override
    public String getNamespace() {
        return null;
    }

}
