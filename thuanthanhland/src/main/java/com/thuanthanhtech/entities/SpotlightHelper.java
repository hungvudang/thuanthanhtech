package com.thuanthanhtech.entities;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

import com.thuanthanhtech.WebMvcConfig;

public class SpotlightHelper {
	
	public final static String ROOT_PATH_THUMBNAIL_MEDIUM = WebMvcConfig.ROOT_PATH_PUBLIC_BASE + "/upload/spotlight";
	
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
