package com.jordy.gateway.mqtt.models;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jordy.gateway.serializers.CamJsonDeserializer;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.util.XmlStringBuilder;

@JsonDeserialize(using = CamJsonDeserializer.class)
public class CamMessage implements Serializable, ExtensionElement {
    String stationId;
    int stationType;
    Double speed;
    Double heading;
    Position positions;
    public static final String ELEMENT = "cam";
    private static final long serialVersionUID = 1L;

    public CamMessage() {
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public int getStationType() {
        return stationType;
    }

    public void setStationType(int stationType) {
        this.stationType = stationType;
    }

    public Double getHeading() {
        return heading;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
    }

    public Position getPositions() {
        return positions;
    }

    public void setPositions(Position positions) {
        this.positions = positions;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    @Override
    public String getElementName() {
        return ELEMENT;
    }

    @Override
    public CharSequence toXML(String enclosingNamespace) {
        XmlStringBuilder xml = new XmlStringBuilder(this);
        xml.rightAngleBracket();
        xml.optElement("station-id", stationId);
        xml.optElement("station-type", stationType);
        xml.optElement("speed", speed);
        xml.optElement("heading", heading);
        xml.append(positions.toXML(null));
        xml.closeElement(this);
        return xml;
    }

    @Override
    public String getNamespace() {
        return null;
    }

}
