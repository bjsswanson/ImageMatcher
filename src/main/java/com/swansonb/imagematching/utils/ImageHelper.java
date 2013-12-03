package com.swansonb.imagematching.utils;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageHelper {

	public static final int MAX_POINT_COMPARE_DIST = 200;
	public static final double DIST_THRESHOLD = 0.25;
	private static AtomicInteger counter = new AtomicInteger();

	public static double matchImages(Mat img1, Mat img2) {
		MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
		MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
		Mat descriptors1 = new Mat();
		Mat descriptors2 = new Mat();

		//Definition of ORB keypoint detector and descriptor extractors
		FeatureDetector detector = FeatureDetector.create(FeatureDetector.DYNAMIC_FAST);
		DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);

		//Detect keypoints
		detector.detect(img1, keypoints1);
		detector.detect(img2, keypoints2);
		//Extract descriptors
		extractor.compute(img1, keypoints1, descriptors1);
		extractor.compute(img2, keypoints2, descriptors2);

		//Definition of descriptor matcher
		DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);

		//Match points of two images
		MatOfDMatch matches = new MatOfDMatch();
		matcher.match(descriptors1, descriptors2, matches);

		float max = 0;
		float min = Float.MAX_VALUE;


		List<DMatch> dMatches = matches.toList();
		for (DMatch match : dMatches) {
			if (match.distance > max) {
				max = match.distance;
			}
			if (match.distance < min) {
				min = match.distance;
			}
		}

		//look whether the match is inside a defined area of the image
		//only 25% of maximum of possible distance
		double distThreshold = DIST_THRESHOLD * Math.sqrt((Math.pow(img1.size().height,2) + Math.pow(img1.size().width,2)) * 2);

		List<DMatch> goodMatches = new ArrayList<DMatch>();
		for (DMatch match : dMatches) {
			Point from =  keypoints1.toArray()[match.queryIdx].pt;
			Point to = keypoints2.toArray()[match.trainIdx].pt;
			if (match.distance < distThreshold && Math.abs(from.y - to.y) < MAX_POINT_COMPARE_DIST) {
				goodMatches.add(match);
			}
		}

		//createMatchDiagrams(img1, img2, keypoints1, keypoints2, goodMatches);


		return goodMatches.size();
	}

	private static void createMatchDiagrams(Mat img1, Mat img2, MatOfKeyPoint keypoints1, MatOfKeyPoint keypoints2, List<DMatch> goodMatches) {
		//Draw matches
		MatOfDMatch goodMat = new MatOfDMatch();
		goodMat.fromList(goodMatches);
		Mat out = new Mat();
		Features2d.drawMatches(img1, keypoints1, img2, keypoints2, goodMat, out);
		Highgui.imwrite("matches/test" + counter.incrementAndGet() + ".png", out);
		System.out.println(goodMatches.size());
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
