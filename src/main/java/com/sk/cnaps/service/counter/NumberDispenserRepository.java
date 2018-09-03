package com.sk.cnaps.service.counter;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface NumberDispenserRepository extends CrudRepository<NumberDispenser, Long> {
	NumberDispenser findOneByName(String name);
	List<NumberDispenser> findAll();
}
