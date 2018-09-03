package com.sk.cnaps.domain.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class EnumWithTextDeserializer<T> extends StdDeserializer<Enum<? extends EnumWithText>> {
	public EnumWithTextDeserializer() {
    	this(EnumWithText.class);
    }

	public EnumWithTextDeserializer(Class<EnumWithText> clazz) {
		super(clazz);
	}

	@Override
	public Enum<? extends EnumWithText> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {	
		/*
		ValueText valueText = p.readValueAs(ValueText.class);
		
		if(p != null) {
			System.err.println("ENUM CLASS: " + valueText.getEnumClass());
		}
		
		System.err.println("TEST: " + JsonUtil.toJsonStr(valueText));
		
		return null;
		//final Class<? extends EnumWithText> tClass = Class<? extends EnumWithText>();
		
		//EnumWithText text = p.readValueAs(EnumWithText.class);
		//return Enum.valueOf(, valueText.getValue());
		
		//valueText.getValue();
		
		//return null;
		*/
		return null;
	}
}
