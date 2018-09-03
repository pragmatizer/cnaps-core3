package com.sk.cnaps.domain.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.query.Param;

import com.sk.cnaps.domain.model.IdValue;

public interface IdValueRepository<T> {
	T findById(@Param("id") Long id);
	List<T> findByIdIn(@Param("ids") Set<Long> ids);
}
