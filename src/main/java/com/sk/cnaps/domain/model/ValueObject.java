package com.sk.cnaps.domain.model;

import javax.persistence.AttributeConverter;

import com.sk.cnaps.domain.util.JsonUtil;

public abstract class ValueObject implements AttributeConverter<ValueObject, String> {
	@Override
	public String toString() {
		return JsonUtil.toJsonStr(this);
	}
	
	@Override
	public String convertToDatabaseColumn(ValueObject attribute) {
		return JsonUtil.toJsonStr(attribute);
	}
	
	@Override
	public ValueObject convertToEntityAttribute(String dbData) {
		return JsonUtil.fromJsonStr(dbData, this.getClass());
	}
}
