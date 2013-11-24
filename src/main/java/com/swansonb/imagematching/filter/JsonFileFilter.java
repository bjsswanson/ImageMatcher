package com.swansonb.imagematching.filter;

import java.io.File;
import java.io.FileFilter;

public class JsonFileFilter implements FileFilter {
	private final String[] okFileExtensions = new String[] {"json"};

	public boolean accept(File file) {
		for (String extension : okFileExtensions){
			if (file.getName().toLowerCase().endsWith(extension)){
				return true;
			}
		}
		return false;
	}
}