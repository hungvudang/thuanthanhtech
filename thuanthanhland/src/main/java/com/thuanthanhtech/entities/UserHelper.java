package com.thuanthanhtech.entities;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class UserHelper {
	
	public static final String ROOT_PATH_AVATAR_MEDIUM = "/public/upload/user";
	
	
	public static void saveAvatarImage(MultipartFile multipartFile, String uploadDir, String fAvatarImageName) throws IOException {
		
		Path pUploadDirAvatarImage = Paths.get(uploadDir);
		
		if (! Files.exists(pUploadDirAvatarImage)) {
			Files.createDirectories(pUploadDirAvatarImage);
		}
		
		InputStream is = multipartFile.getInputStream();
		Path pSaveAvatarImage = pUploadDirAvatarImage.resolve(fAvatarImageName);
		Files.copy(is, pSaveAvatarImage, StandardCopyOption.REPLACE_EXISTING);
		is.close();
	}
}
