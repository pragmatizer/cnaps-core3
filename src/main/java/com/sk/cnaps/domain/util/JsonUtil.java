package com.sk.cnaps.domain.util;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	private JsonUtil() {
		throw new IllegalStateException("Utility class");
	}
	
	public static String removeElement(String jsonStr, String[] removeElements) {
		if(jsonStr == null || jsonStr.isEmpty()) {
			return null;
		}
		
		JSONObject object = new JSONObject(jsonStr);
		for(String key : removeElements) {
			object.remove(key);
		}
		
		return object.toString();		
	}
	
	public static String setElement(String jsonStr, String key, String value) {
		if(jsonStr == null || jsonStr.isEmpty()) {
			return null;
		}
		
		JSONObject object = new JSONObject(jsonStr);	
		object.put(key,  value);
		
		return object.toString();
	}
	
	public static String getElement(String jsonStr, String key) {
		if(jsonStr == null || jsonStr.isEmpty()) {
			return null;
		}
		
		try {
			JSONObject object = new JSONObject(jsonStr);	
			if(object == null) {
				return null;
			}
			
			return object.get(key).toString();
			//return object.toString();
			
		} catch(JSONException e) {
			return null;
		}

	}
	
	public static <T> T fromJsonStr(String jsonStr, Class<T> clazz) {
		if(jsonStr == null || jsonStr.isEmpty()) {
			return null;
		}
		
		ObjectMapper mapper = new ObjectMapper();
		try {
		
			return mapper.readValue(jsonStr, clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String toJsonStr(Object obj) {
		if(obj == null) return null;
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;	
	}
}
