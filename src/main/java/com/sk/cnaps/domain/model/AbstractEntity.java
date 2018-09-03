package com.sk.cnaps.domain.model;

import java.time.LocalDateTime;

import javax.persistence.Convert;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.ClassUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sk.cnaps.domain.util.JsonUtil;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public  class AbstractEntity {
	public static final String GENERATOR = "cnapsSequenceGenerator";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	     
	@CreatedDate
	@Convert(converter=LocalDateTimeConverter.class)
	private LocalDateTime createdDateTime; 
	
	@LastModifiedDate
	@Convert(converter=LocalDateTimeConverter.class)
	private LocalDateTime lastModifiedDateTime; 

	public Long getId() {
		return id;
	}
	
	public LocalDateTime getLastModifiedDateTime() {
		return this.lastModifiedDateTime;
	}
	
	public LocalDateTime getCreatedDateTime() {
		return this.createdDateTime;
	}
	
	@Override
	public String toString() {
		return JsonUtil.toJsonStr(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (!getClass().equals(ClassUtils.getUserClass(obj))) {
			return false;
		}

		AbstractEntity that = (AbstractEntity) obj;

		return null == this.getId() ? false : this.getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		int hashCode = 17;

		hashCode += null == getId() ? 0 : getId().hashCode() * 31;

		return hashCode;
	}
}
