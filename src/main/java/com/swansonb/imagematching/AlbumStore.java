package com.swansonb.imagematching;

import org.opencv.core.Mat;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AlbumStore {

	private AtomicLong idGen;
	private Map<String, Image> tempImageStore;
	private Map<String, Album> identifiedAlbums;

	public AlbumStore(){
		idGen = new AtomicLong();
		tempImageStore = new HashMap<String, Image>();
		identifiedAlbums = new HashMap<String, Album>();
	}

	public Collection<Album> getAlbums(){
		return identifiedAlbums.values();
	}

	public Album updateOrCreateAlbum(String id, String albumName, String artist){
		Album album = identifiedAlbums.get(id);
		if(album != null){
			album.setAlbumName(albumName);
			album.setArtist(artist);
		} else {
			Image image = tempImageStore.get(id);
			album = new Album(image, albumName, artist);
			identifiedAlbums.put(id, album);
		}
		return album;
	}

	public void clearTempImages(){
		tempImageStore.clear();
	}

	public Image getTempImage(String id){
		return tempImageStore.get(id);
	}

	public Image storeTempImage(Mat imageMat){
		String id = generateId();
		Image image = new Image(id, imageMat);
		tempImageStore.put(id, image);
		return image;
	}

	private String generateId(){
		long longId = idGen.incrementAndGet();
		return String.valueOf(longId);
	}

}
