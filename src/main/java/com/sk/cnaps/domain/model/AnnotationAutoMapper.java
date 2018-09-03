package com.sk.cnaps.domain.model;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import com.sk.cnaps.application.util.ClassScanner;
import com.sk.cnaps.application.util.DynamicAnnotations;
import com.sk.cnaps.domain.util.UtilsSupport;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AnnotationAutoMapper {
	public static void apply(String rootPackageName) {
		setupGeneratedValue();
		
		try {
			Class[] classes = ClassScanner.getClasses(rootPackageName);

			for (Class clazz : classes) {
				Class superClazz = clazz.getSuperclass();
				if (superClazz != null) {
					if (superClazz == AbstractEntity.class) {
						addSequenceGenerator(clazz);
					}
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			log.error(e.getMessage());
		}
	}
	
	public static void apply(String[] rootPackageNames) {
		for(String rootPackageName : rootPackageNames) {
			apply(rootPackageName);
		}
		
	}
	
	public static void setupGeneratedValue() {
		Field idField;
		try {
			idField = AbstractEntity.class.getDeclaredField("id");
			idField.setAccessible(true);
			GeneratedValue generatedValue = idField.getAnnotation(GeneratedValue.class);
			DynamicAnnotations.changeAnnotationValue(generatedValue, "strategy", GenerationType.SEQUENCE);
			DynamicAnnotations.changeAnnotationValue(generatedValue, "generator", "cnapsSequenceGenerator");
		} catch (NoSuchFieldException | SecurityException e) {
			log.error(e.getMessage());
		}
	}
	
	public static GeneratedValue createDefaultGeneratedValue() {
		return new GeneratedValue() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public GenerationType strategy() {
				return GenerationType.AUTO;
			}

			@Override
			public String generator() {
				return null;
			}	
		};
	}

	public static void addSequenceGenerator(Class<? extends AbstractEntity> clazz) {
		String sequenceName = UtilsSupport.camelToUnderline(clazz.getSimpleName()).toLowerCase() + "_seq";

		Map<String, Object> valuesMap = new HashMap<>();
		valuesMap.put("name", AbstractEntity.GENERATOR);
		valuesMap.put("sequenceName", sequenceName);
		valuesMap.put("initialValue", 1);
		valuesMap.put("allocationSize", 1);

		DynamicAnnotations.putAnnotation(clazz, SequenceGenerator.class, valuesMap);

		SequenceGenerator result = clazz.getAnnotation(SequenceGenerator.class);
		log.info("added SequenceGenerator: {}", result);
	}
}
