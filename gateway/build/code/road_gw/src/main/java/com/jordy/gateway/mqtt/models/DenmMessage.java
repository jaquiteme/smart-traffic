package com.jordy.gateway.mqtt.models;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jordy.gateway.serializers.DenmJsonDeserializer;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.util.XmlStringBuilder;

@JsonDeserialize(using = DenmJsonDeserializer.class)
public class DenmMessage implements Serializable, ExtensionElement {
    private static final long serialVersionUID = 1L;
    private String stationId;
    private int stationType;
    private int causeCode;
    private int subCauseCode;
    private Position positions;
    public static final String ELEMENT = "denm";

    public DenmMessage() {
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

    public int getCauseCode() {
        return causeCode;
    }

    public void setCauseCode(int causeCode) {
        this.causeCode = causeCode;
    }

    public int getSubCauseCode() {
        return subCauseCode;
    }

    public void setSubCauseCode(int subCauseCode) {
        this.subCauseCode = subCauseCode;
    }

    public Position getPositions() {
        return positions;
    }

    public void setPositions(Position positions) {
        this.positions = positions;
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
        xml.optElement("cause-code", causeCode);
        xml.append(positions.toXML(null));
        xml.closeElement(this);
        return xml;
    }

    @Override
    public String getNamespace() {
        return null;
    }

}
