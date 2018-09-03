package com.sk.cnaps.application.sp.web;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sk.cnaps.application.sp.web.dto.ValueText;
import com.sk.cnaps.application.util.ClassScanner;

@RestController
@RequestMapping("/enumTypes")
public class EnumTypeRestController {
	private static Map<String, List<ValueText>> enumMap = new HashMap<>();
	
	public EnumTypeRestController() {
		this("com.skcc");
	}
	
	public EnumTypeRestController(String packageName) {
		try {
			Class[] classes = ClassScanner.getClasses(packageName);
			
			for(Class clss : classes) {
				if(clss.isEnum()) {
					enumMap.put(clss.getSimpleName(), createValueText(clss));
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private List<ValueText> createValueText(Class enumType) {
		List<ValueText> valueTexts = new ArrayList<>();
		
		for(Object object : enumType.getEnumConstants()) {
			Class objectClass = object.getClass();
			String name = object.toString(); 
			String text;
			
			try {
				Method method = objectClass.getDeclaredMethod("text", null);
				text = (String)method.invoke(object, null);
			} catch (Exception e) {
				text = object.toString();
			}
			valueTexts.add(ValueText.builder().value(name).text(text).build());
		}
		return valueTexts;
	}
	
	@GetMapping
	public Set<String> findAllEnumTypes() {
		return enumMap.keySet();
	}
	
	@GetMapping("{enumType}")
	public List<ValueText> findEnumType(@PathVariable("enumType") String enumType) {
		return enumMap.get(enumType);
	}
}
