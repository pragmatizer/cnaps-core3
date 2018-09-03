package com.sk.cnaps.domain.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.persistence.AttributeConverter;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sk.cnaps.domain.repository.AggregateRepository;
import com.sk.cnaps.domain.util.JsonUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@SuppressWarnings("rawtypes")
@ApiModel
public class AggregateProxy<T extends AggregateRoot> implements AttributeConverter<AggregateProxy, String> {
	@ApiModelProperty(required = true)
	private Set<Long> ids = new HashSet<>();
	
	@JsonIgnore
	@ApiModelProperty(hidden = true)
	//@Transient
	private Set<T> values = new HashSet<>();
	
	@ApiModelProperty(readOnly = true)
	//@Transient
	private Map<Long, T> idValues = new HashMap<>(); 
	
	public AggregateProxy() {	
	}
	
	public Set<Long> getIds() {
		return ids;
	}
	
	public void setIds(Set<Long> ids) {
		this.ids = ids;
	}
	
	public void add(Long id) {
		ids.add(id);
	}
	
	public void remove(Long id) {
		ids.remove(id);
	}
	
	@ApiModelProperty(hidden = true)
	public void fillValues(AggregateRepository<T> repository) {
		values = repository.findByIdIn(ids);
		
		Iterator<T> it = values.iterator();
		
		idValues.clear();
		while(it.hasNext()) {
			T item = it.next();
			idValues.put(item.getId(), item);
		}
	}

	@ApiModelProperty(hidden = true)
	public void setIdValues(Map<Long, T> idValues) {
		this.idValues = idValues;
	}
	
	@ApiModelProperty(hidden = true)
	public Set<T> getValues() {
		return this.values;
	}
	
	public Map<Long, T> getIdValues() {
		return this.idValues;
	}
	
	@Override
	public String convertToDatabaseColumn(AggregateProxy attribute) {
		String jsonStr = JsonUtil.toJsonStr(attribute);
		jsonStr = JsonUtil.removeElement(jsonStr, new String [] { "values", "idValues"} );
		return jsonStr;
	}

	@Override
	public AggregateProxy convertToEntityAttribute(String dbData) {
		dbData = JsonUtil.removeElement(dbData, new String [] { "values", "idValues"} );
		
		return JsonUtil.fromJsonStr(dbData, this.getClass());
	}
}
