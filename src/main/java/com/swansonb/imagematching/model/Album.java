package com.swansonb.imagematching.model;

import org.opencv.core.Mat;

public class Album {
	private String id;
	private String uri;
	private transient Mat image;
	private transient String thumb;

	public Album(Image tempImage, String uri) {
		this.id = tempImage.getId();
		this.thumb = tempImage.getThumbnail();
		this.image = tempImage.getImage();
		this.uri = uri;
	}

	public String getId() {
		return id;
	}

	public String getUri() {
		return uri;
	}

	public Mat getImage() {
		return image;
	}

	public String getThumb() {
		return thumb;
	}

	public void setImage(Mat image) {
		this.image = image;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
}
