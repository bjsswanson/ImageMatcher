package com.swansonb.imagematching.model;

import com.google.gson.annotations.SerializedName;
import com.swansonb.imagematching.utils.ImageHelper;
import org.opencv.core.Mat;

public class Image {
	private String id;
	private transient Mat image;

	@SerializedName("image")
	private String thumbnail;

	public Image(String id, Mat image) {
		this.id = id;
		this.image = image;
		this.thumbnail = ImageHelper.createThumbnail(image);
	}

	public String getId() {
		return id;
	}

	public Mat getImage() {
		return image;
	}

	public String getThumbnail() {
		return thumbnail;
	}


}
