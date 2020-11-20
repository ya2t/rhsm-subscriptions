package org.candlepin.subscriptions.resteasy;

import org.candlepin.subscriptions.utilization.api.model.Granularity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

import javax.ws.rs.ext.Provider;

@Provider
public class TitleCaseEnumDeserializer extends StdDeserializer<Granularity> {

    public TitleCaseEnumDeserializer() {
        this(null);
    }

    protected TitleCaseEnumDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Granularity deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {



        System.err.println("I'm a deserializer!");

        return null;
    }
}
