package com.jordy.gateway.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.jordy.gateway.mqtt.models.DenmMessage;
import com.jordy.gateway.mqtt.models.Position;

public class DenmJsonDeserializer extends StdDeserializer<DenmMessage> {

    public DenmJsonDeserializer() {
        this(null);
    }

    protected DenmJsonDeserializer(Class<?> vc) {
        super(vc);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public DenmMessage deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode denmMessagNode = p.getCodec().readTree(p);
        DenmMessage message = new DenmMessage();
        Position position = new Position();
        message.setStationId(denmMessagNode.get("stationId").textValue());
        message.setStationType(denmMessagNode.get("stationType").asInt());
        message.setCauseCode(denmMessagNode.get("causeCode").asInt());
        message.setSubCauseCode(denmMessagNode.get("subCauseCode").asInt());
        position.setLatitude(denmMessagNode.get("positions").get("latitude").asDouble());
        position.setLongitude(denmMessagNode.get("positions").get("longitude").asDouble());
        message.setPositions(position);
        return message;
    }

}
