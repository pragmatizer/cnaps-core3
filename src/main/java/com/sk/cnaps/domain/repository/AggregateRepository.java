package com.sk.cnaps.domain.repository;

import java.util.Set;

import org.springframework.data.repository.query.Param;

public interface AggregateRepository<T> {
	Set<T> findByIdIn(@Param("ids") Set<Long> ids);
}
