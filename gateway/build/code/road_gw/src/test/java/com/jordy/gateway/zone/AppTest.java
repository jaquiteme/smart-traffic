package com.jordy.gateway.zone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.jordy.gateway.mqtt.models.DenmMessage;
import com.jordy.gateway.mqtt.models.Position;
import com.jordy.gateway.xmpp.models.Alert;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    private Position position;
    private DenmMessage denmMessage;
    private Alert alert;

    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void shouldReturnValidPositionXml() {
        this.position = new Position(49.282851, 4.107277);
        CharSequence resultXml = this.position.toXML(null);
        String expectedXml = "<position>" + "<latitude>49.282851</latitude>" + "<longitude>4.107277</longitude>"
                + "</position>";
        assertEquals(expectedXml, resultXml.toString());
    }

    @Test
    public void shouldReturnValidDenmEventXml() {
        position = new Position(49.282851, 4.107277);
        denmMessage = new DenmMessage();
        denmMessage.setStationId("001");
        denmMessage.setStationType(5);
        denmMessage.setCauseCode(5);
        denmMessage.setPositions(position);
        CharSequence resultXml = this.denmMessage.toXML(null);
        String expectedXml = "<denm>" + "<station-id>001</station-id>" + "<station-type>5</station-type>"
                + "<cause-code>5</cause-code>" + "<position>" + "<latitude>49.282851</latitude>"
                + "<longitude>4.107277</longitude>" + "</position>" + "</denm>";
        assertEquals(expectedXml, resultXml.toString());
    }

    @Test
    public void shouldReturnValidAlerteEventXML() {
        List<DenmMessage> content = new ArrayList<>();
        position = new Position(49.282851, 4.107277);
        denmMessage = new DenmMessage();
        denmMessage.setStationId("001");
        denmMessage.setStationType(5);
        denmMessage.setCauseCode(5);
        denmMessage.setPositions(position);
        content.add(denmMessage);
        alert = new Alert();
        alert.setTag(5);
        alert.setContent(content);
        alert.setCreated_at(1L);
        CharSequence resultXml = alert.toXML(null);
        String expectedXml = "<alert xmlns='http://jabber.org/protocol/alert' tag='5'>" + "<content>" + "<denm>"
                + "<station-id>001</station-id>" + "<station-type>5</station-type>" + "<cause-code>5</cause-code>"
                + "<position>" + "<latitude>49.282851</latitude>" + "<longitude>4.107277</longitude>" + "</position>"
                + "</denm>" + "</content>" + "<created_at>1</created_at>" + "</alert>";
        assertEquals(expectedXml, resultXml.toString());
    }

}
