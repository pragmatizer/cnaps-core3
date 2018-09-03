package com.sk.cnaps.domain.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.sk.cnaps.domain.util.EnumWithText;

public class EnumWithTextSerializer extends StdSerializer<EnumWithText> {	 
    public EnumWithTextSerializer() {
    	this(EnumWithText.class);
    }
	
	public EnumWithTextSerializer(Class<EnumWithText> e) {
        super(e); 
    }
 
	@Override
	public void serialize(EnumWithText value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeFieldName("value");
		gen.writeString(value.name());
		gen.writeFieldName("text");
		gen.writeString(value.text());
		//gen.writeFieldName("enumClass");
		//gen.writeString(value.toString());
		gen.writeEndObject();
	}
}