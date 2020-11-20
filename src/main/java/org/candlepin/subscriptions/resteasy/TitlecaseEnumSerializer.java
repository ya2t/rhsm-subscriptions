package org.candlepin.subscriptions.resteasy;

import org.candlepin.subscriptions.utilization.api.model.Granularity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.apache.commons.text.WordUtils;

import java.io.IOException;

import javax.ws.rs.ext.Provider;

@Provider
public class TitlecaseEnumSerializer extends StdSerializer<Granularity> {

    protected TitlecaseEnumSerializer(Class<Granularity> t) {
        super(t);
    }

    public TitlecaseEnumSerializer() {
        this(null);
    }

    @Override
    public void serialize(Granularity value, JsonGenerator gen, SerializerProvider provider)
        throws IOException {

        //TODO this is what gets put into the json

        String granularityAsTitleCase = WordUtils.capitalizeFully(value.toString());

        gen.writeString(granularityAsTitleCase);

        System.out.println(value);

    }
}
