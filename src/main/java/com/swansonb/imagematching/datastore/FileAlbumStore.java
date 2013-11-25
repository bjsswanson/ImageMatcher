package com.swansonb.imagematching.datastore;

import com.swansonb.imagematching.filter.ImageFileFilter;
import com.swansonb.imagematching.filter.JsonFileFilter;
import com.swansonb.imagematching.model.Album;
import com.swansonb.imagematching.model.Image;
import com.swansonb.imagematching.utils.JsonUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FileAlbumStore implements AlbumStore {

	public static final String ENCODING = "UTF-8";
	private static final String IMAGE_EXT = ".png";
	private static final String ALBUM_EXT = ".json";

	private File dataStore;
	private File imageStore;
	private File albumStore;

	private AtomicLong idGen;
	private Map<String, Image> images;
	private Map<String, Album> albums;

	public FileAlbumStore(){
		dataStore = getFolder("data");
		imageStore = getFolder(dataStore, "images");
		albumStore = getFolder(dataStore, "albums");

		images = loadImages();
		albums = loadAlbums();

		idGen = new AtomicLong(images.size());
	}

	@Override
	public Collection<Album> getAlbums(){
		return albums.values();
	}

	private Map<String, Image> loadImages(){
		Map<String, Image> images = new HashMap<String, Image>();

		FileFilter filter = new ImageFileFilter();
		File[] files = imageStore.listFiles(filter);

		for(File file : files){
			String id = FilenameUtils.getBaseName(file.getName());
			Mat mat = Highgui.imread(file.getAbsolutePath());
			images.put(id, new Image(id, mat));
		}

		return images;
	}

	private Map<String, Album> loadAlbums(){
		Map<String, Album> albums = new HashMap<String, Album>();

		FileFilter filter = new JsonFileFilter();
		File[] files = albumStore.listFiles(filter);

		for(File file : files){
			try {
				String json = FileUtils.readFileToString(file);
				Album album = JsonUtils.fromJson(json, Album.class);
				Image image = images.get(album.getId());
				if(image != null){
					album.setImage(image.getImage());
					album.setThumb(image.getThumbnail());
					albums.put(album.getId(), album);
				}
			} catch (IOException e) {
				System.err.println("Could not load album: " + file.getPath());
			}
		}

		return albums;
	}

	@Override
	public Image storeImage(Mat mat) {
		Image image = new Image(generateId(), mat);
		boolean stored = Highgui.imwrite(getPath(imageStore, image.getId() + IMAGE_EXT), image.getImage());
		if(stored){
			images.put(image.getId(), image);
			return image;
		} else {
			System.err.println("Unable to write image to file.");
			return null;
		}
	}

	@Override
	public Album storeAlbum(String id, String albumName, String artist) {
		Image image = images.get(id);

		try {
			if(image != null){
				Album album = new Album(image, albumName, artist);
				File file = addFile(albumStore, id + ALBUM_EXT);
				if(file.exists()){
					FileUtils.forceDelete(file);
				}
				FileUtils.writeStringToFile(file, JsonUtils.toJson(album), ENCODING);
				albums.put(id, album);
				return album;
			}
		} catch (IOException e) {
			System.err.println("Unable to write album to file.");
		}

		return null;
	}

	@Override
	public Image getImage(String id) {
		return images.get(id);
	}

	@Override
	public Album getAlbum(String id) {
		return albums.get(id);
	}

	private File getFolder(String path){
		File albumStore = new File(path);
		if(!albumStore.exists()){
			try {
				FileUtils.forceMkdir(albumStore);
			} catch (IOException e) {
				System.err.println("Unable to make directory: " + path);
			}
		}
		return albumStore;
	}

	private File getFolder(File directory, String path){
		return getFolder(getPath(directory, path));
	}


	private File addFile(File directory, String fileName){
		return new File(getPath(directory, fileName));
	}

	private String getPath(File directory, String fileName){
		return directory.getAbsolutePath() + File.separator + fileName;
	}

	private String generateId() {
		long longId = idGen.incrementAndGet();
		return String.valueOf(longId);
	}

}
