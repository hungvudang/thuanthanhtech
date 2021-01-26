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

	public final static String ROOT_PATH_THUMBNAIL_MEDIUM = "/public/news";

	public static void saveThumbnailImage(MultipartFile multipartFile, String uploadDir, String fName)
			throws IOException {
		Path uploadPath = Paths.get(uploadDir);

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		InputStream is = multipartFile.getInputStream();
		Path fPath = uploadPath.resolve(fName);
		Files.copy(is, fPath, StandardCopyOption.REPLACE_EXISTING);
		is.close();
	}
}
