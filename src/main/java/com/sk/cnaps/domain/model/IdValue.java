package com.sk.cnaps.domain.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import com.sk.cnaps.domain.repository.IdValueRepository;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


//@SuppressWarnings("rawtypes")
@ApiModel
@Embeddable
public class IdValue<T extends AggregateRoot> /* implements AttributeConverter<IdValue, String>*/ {	
	@ApiModelProperty(required = true)
	@Column
	private Long id;
	
	@ApiModelProperty(hidden = true)
	@Transient
	private T value = null;
	
	public IdValue() {
	}
	
	public IdValue(Long id) {
		this.id = id;
	}
	
	public IdValue(T value) {
		this.id = value.getId();
		this.value = value;
	}
	
	@ApiModelProperty(hidden = true)
	public void fillValue(IdValueRepository<T> repository) {
		value = repository.findById(id);
	}
	
	
	@ApiModelProperty(hidden = true)
	public T getValue() {
		return this.value;
	}
	
	@ApiModelProperty(hidden = true)
	public void setValue(T value) {
		this.value = value;
	}
	
	@ApiModelProperty(hidden = true)
	public void setId(Long id) {
		this.id = id;
	}
	
	@ApiModelProperty(hidden = true)
	public Long getId() {
		return id;
	}
	
	/*
	@Override
	public String convertToDatabaseColumn(IdValue attribute) {
		String jsonStr = JsonUtil.toJsonStr(attribute);
		jsonStr = JsonUtil.removeElement(jsonStr, new String [] { "values", "idValues"} );
		return jsonStr;
	}

	@Override
	public IdValue convertToEntityAttribute(String dbData) {
		dbData = JsonUtil.removeElement(dbData, new String [] { "values", "idValues"} );
		
		return JsonUtil.fromJsonStr(dbData, this.getClass());
	}
	*/
}
