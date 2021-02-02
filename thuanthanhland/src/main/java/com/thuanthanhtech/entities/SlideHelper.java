package com.thuanthanhtech.entities;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class SlideHelper {
	
	public final static String NO_IMAGE_MEDIUM = "/admin-static/images/no-thumbnail-medium.png";

	public final static String ROOT_PATH_IMAGE_MEDIUM = "/public/upload/slides";

	public static void saveImage(MultipartFile multipartFile, String uploadDir, String fImageName)
			throws IOException {
		Path pUploadDirImage = Paths.get(uploadDir);

		if (!Files.exists(pUploadDirImage)) {
			Files.createDirectories(pUploadDirImage);
		}

		InputStream is = multipartFile.getInputStream();
		Path pSaveImage = pUploadDirImage.resolve(fImageName);
		Files.copy(is, pSaveImage, StandardCopyOption.REPLACE_EXISTING);
		is.close();
	}
}
