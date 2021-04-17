package com.jordy.gateway.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.jordy.gateway.mqtt.models.CamMessage;
import com.jordy.gateway.mqtt.models.Position;

public class CamJsonDeserializer extends StdDeserializer<CamMessage> {

    public CamJsonDeserializer() {
        this(null);
    }

    public CamJsonDeserializer(Class<?> vc) {
        super(vc);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public CamMessage deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        JsonNode camMessagNode = p.getCodec().readTree(p);
        CamMessage message = new CamMessage();
        Position position = new Position();
        message.setStationId(camMessagNode.get("stationId").textValue());
        message.setStationType(camMessagNode.get("stationType").asInt());
        message.setSpeed(camMessagNode.get("speed").asDouble());
        message.setHeading(camMessagNode.get("heading").asDouble());
        position.setLatitude(camMessagNode.get("positions").get("latitude").asDouble());
        position.setLongitude(camMessagNode.get("positions").get("longitude").asDouble());
        message.setPositions(position);
        return message;
    }

}
