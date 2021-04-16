package com.thuanthanhtech.entities;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class AboutUsHelper {
	public final static String ROOT_PATH_THUMBNAIL_MEDIUM = "/public/upload/aboutus";
	public static void saveThumbnail(MultipartFile multipartFile, String uploadDir, String  fthumbnail) throws IOException {
		Path pUploadDirThumbnail = Paths.get(uploadDir);
		if(!Files.exists(pUploadDirThumbnail)) {
			Files.createDirectories(pUploadDirThumbnail);
		}
		InputStream is =multipartFile.getInputStream(); 
		Path pSaveThumbnail = pUploadDirThumbnail.resolve(fthumbnail);
		Files.copy(is, pSaveThumbnail, StandardCopyOption.REPLACE_EXISTING);
		is.close();
	}
}
