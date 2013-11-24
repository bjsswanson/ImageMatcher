package com.swansonb.imagematching.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

public class JsonUtils {

	private static final Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create();

	public static String toJson(Object obj){
		return gson.toJson(obj);
	}

	public static <T> T fromJson(String json, Class<T> clazz){
		return gson.fromJson(json, clazz);
	}
}
