package com.swansonb.imagematching;

import org.opencv.core.Core;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.lang.reflect.Field;

@ComponentScan
@EnableAutoConfiguration
public class ImageMatcherApplication {

	public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
		loadLibrary();
		SpringApplication.run(ImageMatcherApplication.class, args);
	}

	private static void loadLibrary() throws NoSuchFieldException, IllegalAccessException {
		System.setProperty("java.library.path", "native");
		Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
		fieldSysPath.setAccessible(true);
		fieldSysPath.set(null, null);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
}
