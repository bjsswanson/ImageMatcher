package com.swansonb.imagematching;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Image {
	private String id;
	private transient Mat image;
	private long created;

	@SerializedName("image")
	private String thumbnail;

	public Image(String id, Mat image){
		this.id = id;
		this.image = image;
		this.thumbnail = ImageHelper.createThumbnail(image);
		this.created = System.currentTimeMillis();
	}

	public String getId() {
		return id;
	}

	public Mat getImage() {
		return image;
	}

	public long getCreated() {
		return created;
	}

	public String getThumbnail() {
		return thumbnail;
	}


}
