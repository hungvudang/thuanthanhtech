package com.thuanthanhtech.entities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.web.multipart.MultipartFile;

import com.thuanthanhtech.repositories.NewsRepository;

public class NewsHelper {

	public final static String BASE_PATH_NEWS_RESOURCE = "/public/upload/news";

	@Transactional(rollbackOn = { SQLException.class, IOException.class, NullPointerException.class })
	public static boolean insertNewsEntity(News news, MultipartFile multipartNewsThumbnail,
			MultipartFile[] multipartNewsImages, NewsRepository nRepository) {

		int targetNewsId = nRepository.save(news).getId();

		String targetUploadDir = BASE_PATH_NEWS_RESOURCE + Helper.FILE_SEPARTOR + targetNewsId;

		try {

			if (multipartNewsThumbnail != null && !multipartNewsThumbnail.isEmpty()) {

				String thumbnailImageName = multipartNewsThumbnail.getOriginalFilename();
				saveImage(multipartNewsThumbnail, targetUploadDir, thumbnailImageName);

			}


		} catch (IOException e) {
			System.err.println(NewsHelper.class.getSimpleName() + ": " + e.getMessage());
			news.setThumbnail(Helper.NO_IMAGE_MEDIUM_PNG);
			deleteNewsResourceDir(targetUploadDir);
			return false;
		}

		return true;
	}

	@Transactional(rollbackOn = { SQLException.class, IOException.class , NullPointerException.class})
	public static boolean updateNewsById(Integer targetNewsId, News news, MultipartFile multipartNewsThumbnail,
			MultipartFile[] multipartNewsImages, NewsRepository nRepository) {
		Optional<News> opNews = nRepository.findById(targetNewsId);
		
		if (opNews.isPresent()) {
			News nNews = opNews.get();

			nNews.setName(news.getName());
			nNews.setTitle(news.getTitle());
			nNews.setSlug(news.getSlug());
			nNews.setDescription(news.getDescription());
			nNews.setContent(news.getContent());

			nNews.setCategory(news.getCategory());
			nNews.setPub(news.getPub());
			nNews.setHot(news.getHot());
			
			String oldNameThumbnail = nNews.getThumbnail(); 
			
			String targetUploadDir = BASE_PATH_NEWS_RESOURCE + Helper.FILE_SEPARTOR + targetNewsId;
			
			try {
				
				if (multipartNewsThumbnail != null && !multipartNewsThumbnail.isEmpty()) {
					// Xóa ảnh thumbnail cũ
					deleteNewsResourceDir(targetUploadDir + Helper.FILE_SEPARTOR + oldNameThumbnail);
					
					// Cập nhật ảnh thumbnail mới
					String thumbnailImageName = multipartNewsThumbnail.getOriginalFilename();
					saveImage(multipartNewsThumbnail, targetUploadDir, thumbnailImageName);
					
					nNews.setThumbnail(news.getThumbnail());
				}

				nRepository.save(nNews);
				
				
				return true;

			} catch (IOException e) {
				System.err.println(NewsHelper.class.getSimpleName() + ": " + e.getMessage());
				return false;
			}

			
		}
//		else
		return false;
	}

	public static void saveImage(MultipartFile multipartFile, String uploadDir, String fImageName) throws IOException {
		Path pUploadDirImage = Paths.get(uploadDir);

		if (!Files.exists(pUploadDirImage)) {
			Files.createDirectories(pUploadDirImage);
		}

		InputStream is = multipartFile.getInputStream();
		Path pSaveImage = pUploadDirImage.resolve(fImageName);
		Files.copy(is, pSaveImage, StandardCopyOption.REPLACE_EXISTING);
		is.close();
	}

	public static void deleteNewsResourceDir(String pathName) {
		File fImageDir = new File(pathName);
		if (fImageDir.exists()) {
			if (fImageDir.isDirectory()) {
				
				for (File fImage : fImageDir.listFiles()) {
					if (fImage.isFile()) {
						fImage.delete();
					} else {
						deleteNewsResourceDir(fImage.getPath());
					}
				}
			}
			fImageDir.delete();
		}
	}
}
