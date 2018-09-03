package com.sk.cnaps.service.counter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

@Service
//@EntityScan({"com.sk.cnaps.service", "springfox", "test"})
//@EnableJpaRepositories({"com.sk.cnaps.service.conter"})
public class NumberCounter {
	private static NumberCounter numberCounter;
	
	private NumberCounter() {	
	}
	
	@Bean
	public static NumberCounter getNumberDispenser() {
		if(numberCounter == null) {
			numberCounter = new NumberCounter();
		} 
		
		return numberCounter;
	}
	
	@Autowired
	private NumberDispenserRepository numberDispenserRepository;
		
	public static List<NumberDispenser> getNumberDispensers() {
		return getNumberDispenser().numberDispenserRepository.findAll();
	}
	
	public static NumberDispenser getNumberDispenser(String name) {
		NumberDispenser numberDispenser = getNumberDispenser().numberDispenserRepository.findOneByName(name);

		return numberDispenser;
	}
	
	public static void addNumberDispenser(NumberDispenser numberDispenser) {
		getNumberDispenser().numberDispenserRepository.save(numberDispenser);
	}
	
	public static Long getNumber(String name) {
		return getNumberDispenser(name).getNumber();
	}
	
	public static Long count(String name) {
		return count(name, ResetCycleType.UNLIMIT);
	}
	
	public static Long count(String name, ResetCycleType resetCycleType) {
		NumberDispenser numberDispenser = getNumberDispenser(name);
		
		if(numberDispenser != null) {
			if(numberDispenser.checkReset(resetCycleType)) {
				numberDispenser.reset();
			} 
			numberDispenser.increase();
		
			return getNumberDispenser().numberDispenserRepository.save(numberDispenser).getNumber();
		} else {
			addNumberDispenser(new NumberDispenser(name));
			return count(name, resetCycleType);
		}
	}
}
