package com.sk.cnaps.domain.model;

import javax.persistence.AttributeConverter;

import com.sk.cnaps.domain.util.JsonUtil;

public interface JsonValue extends AttributeConverter<JsonValue, String> {	
	default String toJsonString() {
		return JsonUtil.toJsonStr(this);
	}

	@Override
	default String convertToDatabaseColumn(JsonValue attribute) {
		//return toString(); 
		return JsonUtil.toJsonStr(attribute);
	}
	
	@Override
	default JsonValue convertToEntityAttribute(String dbData) {
		if(dbData == null) {
			dbData = "";
		}
		return JsonUtil.fromJsonStr(dbData, this.getClass());
	}
}
