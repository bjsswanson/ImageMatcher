package com.swansonb.imagematching;

import org.opencv.core.Mat;

public class Album {
	private String id;
	private String albumName;
	private String artist;
	private transient Mat image;
	private String thumb;

	public Album(Image tempImage, String albumName, String artist) {
		this.id = tempImage.getId();
		this.thumb = tempImage.getThumbnail();
		this.image = tempImage.getImage();
		this.albumName = albumName;
		this.artist = artist;
	}

	public String getId() {
		return id;
	}

	public String getAlbumName() {
		return albumName;
	}

	public String getArtist() {
		return artist;
	}

	public Mat getImage() {
		return image;
	}

	public String getThumb() {
		return thumb;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}
}
