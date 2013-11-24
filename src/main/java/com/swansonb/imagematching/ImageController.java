package com.swansonb.imagematching;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.swansonb.imagematching.model.Album;
import com.swansonb.imagematching.model.Image;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

@Controller
public class ImageController {

	public static final String EMPTY_JSON = "{}";
	private VideoCapture camera;
	private Gson gson;

	@Autowired
	private AlbumStore albumStore;

	public ImageController() throws NoSuchFieldException, IllegalAccessException {
		initCamera();
		gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
	}

	private void initCamera() {
		camera = new VideoCapture(0);
		camera.open(0); //Useless

		if (!camera.isOpened()) {
			System.out.println("Camera Error");
		} else {
			System.out.println("Camera OK?");
		}
	}

	@RequestMapping(value = "/snap", method = RequestMethod.GET)
	public @ResponseBody String snap() throws IOException {
		Mat imageMat = new Mat();
		if (camera.isOpened()) {
			camera.read(imageMat);
		}

		Image temp = albumStore.storeImage(imageMat);
		return imageTag(imageMat);
	}

	@RequestMapping(value = "/identify", method = RequestMethod.GET)
	public @ResponseBody String identify() throws IOException {
		Mat image = new Mat();
		if (camera.isOpened()) {
			camera.read(image);
		}

		Album bestMatch = findBestMatch(image);

		if (bestMatch != null) {
			return imageTag(image) + imageTag(bestMatch.getThumb());
		} else {
			return imageTag(image);
		}
	}

	private Album findBestMatch(Mat image) {
		Album bestMatch = null;
		int bestMatchRating = 0;

		Collection<Album> albums = albumStore.getAlbums();
		for (Album album : albums) {
			int matchRating = ImageHelper.matchImages(image, album.getImage());
			if (bestMatch == null || matchRating > bestMatchRating) {
				bestMatch = album;
				bestMatchRating = matchRating;
			}
		}
		return bestMatch;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET,
			produces = "application/json; charset=utf-8")
	public @ResponseBody String list() throws IOException {
		return gson.toJson(albumStore.getAlbums());
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST,
			produces = "application/json; charset=utf-8")
	public @ResponseBody String update(
			@RequestParam("id") String id,
			@RequestParam("albumName") String albumName,
			@RequestParam("artist") String artist) throws IOException {

		boolean valid = isValid(id, albumName, artist);

		if (valid) {
			Album album = albumStore.storeAlbum(id, albumName, artist);
			if (album != null) {
				return constructStatus("success", "album created or updated");
			} else {
				return constructStatus("error", "error creating or updating album");
			}
		} else {
			return constructStatus("error", "POST value missing");
		}
	}

	private boolean isValid(String id, String albumName, String artist) {
		return id != null && albumName != null && artist != null;
	}

	private String constructStatus(String status, String message) {
		return "{status:\"" + status + "\",message:\"" + message + "\"}";
	}

	private String imageTag(Mat image) {
		return imageTag(ImageHelper.createThumbnail(image));
	}

	private String imageTag(String thumb) {
		return "<img src=\"" + thumb + "\"/>";
	}
}
