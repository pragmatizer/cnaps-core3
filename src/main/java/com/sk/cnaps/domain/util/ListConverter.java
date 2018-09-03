package com.sk.cnaps.domain.util;

import java.util.List;

import javax.persistence.AttributeConverter;

import com.sk.cnaps.domain.model.ValueObject;

@SuppressWarnings("rawtypes")
public class ListConverter implements AttributeConverter<List, String> {
	@Override
	public String toString() {
		return JsonUtil.toJsonStr(this);
	}
	
	@Override
	public String convertToDatabaseColumn(List attribute) {
		return JsonUtil.toJsonStr(attribute);
	}
	
	@Override
	public List convertToEntityAttribute(String dbData) {
		return JsonUtil.fromJsonStr(dbData, List.class);
	}
}
