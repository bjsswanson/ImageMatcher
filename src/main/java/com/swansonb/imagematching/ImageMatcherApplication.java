package com.swansonb.imagematching;

import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

@ComponentScan
@EnableAutoConfiguration
public class ImageMatcherApplication {

	public static void main(String[] args){
		SpringApplication.run(ImageMatcherApplication.class, args);
	}



}
