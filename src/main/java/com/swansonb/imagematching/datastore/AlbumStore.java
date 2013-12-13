package com.swansonb.imagematching.datastore;

import com.swansonb.imagematching.model.Album;
import com.swansonb.imagematching.model.Image;
import org.opencv.core.Mat;

import java.util.Collection;

public interface AlbumStore {
	Image storeImage(Mat mat);
	Image getImage(String id);
	Album storeAlbum(String id, String uri);
	Album getAlbum(String id);

	Collection<Album> getAlbums();
}
