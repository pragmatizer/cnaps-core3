package com.sk.cnaps.domain.util;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UtilsSupport {
	public static String[] getNullPropertyNames(Object source) {
	    final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
	    return Stream.of(wrappedSource.getPropertyDescriptors())
	            .map(FeatureDescriptor::getName)
	            .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
	            .toArray(String[]::new);
	}
	
	public static String toPrettyFormat(String jsonStr) {
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(jsonStr).getAsJsonObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(json);
	}
	
	public static List<String> splitCamelCaseString(String s){
	    List<String> result = new ArrayList<>();	
	    for (String w : s.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
	    	result.add(w);
	    }    
	    return result;
	}
	
	public static String camelToUnderline(String camel) {
		List<String> camelClassnames = UtilsSupport.splitCamelCaseString(camel);
		
		String underline = "";
		for(String name : camelClassnames) {
			if(!underline.isEmpty()) {
				underline = underline + "_";
			}
			underline = underline + name;
		}
		return underline;
	}
	
}
