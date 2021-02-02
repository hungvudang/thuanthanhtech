package com.thuanthanhtech.entities;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class NewsHelper {

	public final static String NO_THUMBNAIL_MEDIUM_IMAGE = "/admin-static/images/no-thumbnail-medium.png";

	public final static String ROOT_PATH_THUMBNAIL_MEDIUM = "/public/upload/news";

	public static void saveThumbnailImage(MultipartFile multipartFile, String uploadDir, String fThumbnailImageName)
			throws IOException {
		Path pUploadDirThumbnailImage = Paths.get(uploadDir);

		if (!Files.exists(pUploadDirThumbnailImage)) {
			Files.createDirectories(pUploadDirThumbnailImage);
		}

		InputStream is = multipartFile.getInputStream();
		Path pSaveThumbnailImage = pUploadDirThumbnailImage.resolve(fThumbnailImageName);
		Files.copy(is, pSaveThumbnailImage, StandardCopyOption.REPLACE_EXISTING);
		is.close();
	}
}
