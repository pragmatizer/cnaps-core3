package com.sk.cnaps.service.counter;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.sk.cnaps.domain.model.AbstractEntity;
import com.sk.cnaps.domain.model.AggregateRoot;

@Table(indexes= {@Index(name="number_counter_idx", columnList="name", unique=true)})
@Entity
public class NumberDispenser extends AbstractEntity implements AggregateRoot {
	@Column(length=30, nullable=false)
	private String name;
	
	@Column(name="number_value")
	private Long number = 0L;

	public NumberDispenser() {}
	
	public NumberDispenser(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getNumber() {
		return number;
	}

	public Long increase() {
		return number++;
	}
	
	public boolean checkReset(ResetCycleType resetCycleType) {
		LocalDateTime now = LocalDateTime.now();
		
		int year = now.getYear();
		int month = now.getMonthValue();
		int day = now.getDayOfYear(); 
		int hour = now.getHour();
		int minute = now.getMinute();
		
System.err.println(this.getLastModifiedDateTime());
		if(resetCycleType == ResetCycleType.UNLIMIT) {
			return false;
		}
		
		if(year == getLastModifiedDateTime().getYear()) {
			if(resetCycleType == ResetCycleType.YEARLY) { 
				return false;
			}
		}
		
		if(month == getLastModifiedDateTime().getMonthValue()) {
			if(resetCycleType == ResetCycleType.MONTHLY) {
				return false;
			}
		}
		
		if(day == getLastModifiedDateTime().getDayOfMonth()) {
			if(resetCycleType == ResetCycleType.DAILY) {
				return false;
			} 
		}
		
		if(hour == getLastModifiedDateTime().getHour()) {
			if(resetCycleType == ResetCycleType.HOURLY) {
				return false;
			} 
		}
		
		if(minute == getLastModifiedDateTime().getMinute()) {
			if(resetCycleType == ResetCycleType.MINUTELY) {
				return false;
			} 
		}
		
		return true;
	}
	
	public Long reset() {
		this.number = 0L;
		return number;
	}	
}
