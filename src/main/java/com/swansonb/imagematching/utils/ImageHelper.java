package com.swansonb.imagematching.utils;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.opencv.core.*;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ImageHelper {

	public static int matchImages(Mat img1, Mat img2) {
		MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
		MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
		Mat descriptors1 = new Mat();
		Mat descriptors2 = new Mat();

		//Definition of ORB keypoint detector and descriptor extractors
		FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB);
		DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.ORB);

		//Detect keypoints
		detector.detect(img1, keypoints1);
		detector.detect(img2, keypoints2);
		//Extract descriptors
		extractor.compute(img1, keypoints1, descriptors1);
		extractor.compute(img2, keypoints2, descriptors2);

		//Definition of descriptor matcher
		DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

		//Match points of two images
		MatOfDMatch matches = new MatOfDMatch();
		matcher.match(descriptors1, descriptors2, matches);

		float max = 0;
		float min = Float.MAX_VALUE;
		double total = 0;
		List<DMatch> dMatches = matches.toList();
		for (DMatch match : dMatches) {
			if (match.distance > max) {
				max = match.distance;
			}
			if (match.distance < min) {
				min = match.distance;
			}
			total += match.distance;
		}

		double average = total / dMatches.size();

		int goodMatches = 0;

		for (DMatch match : dMatches) {
			if (match.distance >= min * 2) {
				goodMatches++;
			}
		}

		return goodMatches;
	}

	public static String createThumbnail(Mat image) {
		return createThumbnail(image, 2);
	}

	public static String createThumbnail(Mat image, int divideBy) {
		Mat thumb = new Mat();
		Imgproc.pyrDown(image, thumb, new Size(image.cols() / divideBy, image.rows() / divideBy));
		return matBase64(thumb);
	}

	private static String matBase64(Mat mat) {
		BufferedImage bi = matToBufferedImage(mat);
		if (bi != null) {
			return buffImageToBase64(bi);
		} else {
			return "";
		}
	}

	private static String buffImageToBase64(BufferedImage bi) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(bi, "PNG", out);
			byte[] bytes = out.toByteArray();
			String base64bytes = Base64.encode(bytes);
			return "data:image/png;base64," + base64bytes;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	private static BufferedImage matToBufferedImage(Mat matrix) {
		MatOfByte mb = new MatOfByte();
		Highgui.imencode(".jpg", matrix, mb);
		try {
			return ImageIO.read(new ByteArrayInputStream(mb.toArray()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
