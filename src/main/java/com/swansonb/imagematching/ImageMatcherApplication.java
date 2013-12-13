package com.swansonb.imagematching;

import com.swansonb.imagematching.utils.NativeUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@ComponentScan
@EnableAutoConfiguration
public class ImageMatcherApplication {

	public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException {
		loadLibrary();
		SpringApplication.run(ImageMatcherApplication.class, args);
	}

	private static void loadLibrary() throws NoSuchFieldException, IllegalAccessException, IOException {
		NativeUtils.loadLibraryFromJar("/native/libopencv_java247.dylib");
	}
}
