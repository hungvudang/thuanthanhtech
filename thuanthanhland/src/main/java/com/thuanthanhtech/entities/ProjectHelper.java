package com.thuanthanhtech.entities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.web.multipart.MultipartFile;

import com.thuanthanhtech.WebMvcConfig;
import com.thuanthanhtech.repositories.ProjectRepository;

public class ProjectHelper {

	public final static String BASE_PATH_PROJECT_RESOURCE = WebMvcConfig.ROOT_PATH_PUBLIC_BASE +"/upload/project";
	public final static String DIR_IMAGE_DETAILS = "/images";

	@Transactional(rollbackOn = { SQLException.class, IOException.class, NullPointerException.class })
	public static boolean insertProjectEntity(Project project, MultipartFile multipartProjectThumbnail,
			MultipartFile[] multipartProjectImages, ProjectRepository pRepository) {

		int targetProjectId = pRepository.save(project).getId();

		String targetUploadDir = BASE_PATH_PROJECT_RESOURCE + Helper.FILE_SEPARTOR + targetProjectId;
		String uploadImagesDir = targetUploadDir + Helper.FILE_SEPARTOR + DIR_IMAGE_DETAILS;

		try {

			if (multipartProjectThumbnail != null && !multipartProjectThumbnail.isEmpty()) {

				String thumbnailImageName = multipartProjectThumbnail.getOriginalFilename();
				saveImage(multipartProjectThumbnail, targetUploadDir, thumbnailImageName);

			}

			if (multipartProjectImages != null) {
				
				for (MultipartFile multipartImage : multipartProjectImages) {
					if (multipartImage != null && !multipartImage.isEmpty()) {
						String fImageName = multipartImage.getOriginalFilename();
						saveImage(multipartImage, uploadImagesDir, fImageName);

					}
				}
			}

		} catch (IOException e) {
			System.err.println(ProjectHelper.class.getSimpleName() + ": " + e.getMessage());
			project.setThumbnail(Helper.NO_IMAGE_MEDIUM_PNG);
//			project.setImages(null);
			deleteProjectResourceDir(targetUploadDir);
			return false;
		}

		return true;
	}

	@Transactional(rollbackOn = { SQLException.class, IOException.class , NullPointerException.class})
	public static boolean updateProjectById(Integer targetProjectId, Project project, MultipartFile multipartProjectThumbnail,
			MultipartFile[] multipartProjectImages, ProjectRepository pRepository) {
		Optional<Project> opProject = pRepository.findById(targetProjectId);
		
		if (opProject.isPresent()) {
			Project nProject = opProject.get();

			nProject.setName(project.getName());
			nProject.setTitle(project.getTitle());
			nProject.setSlug(project.getSlug());
			nProject.setDescription(project.getDescription());
			nProject.setContent(project.getContent());

			nProject.setPub(project.getPub());
			nProject.setHot(project.getHot());
			
			nProject.setFeatures(project.getFeatures());
			
			String oldNameThumbnail = nProject.getThumbnail(); 
			
			String targetUploadDir =  BASE_PATH_PROJECT_RESOURCE + Helper.FILE_SEPARTOR + targetProjectId;
			String uploadImagesDir = targetUploadDir + Helper.FILE_SEPARTOR + DIR_IMAGE_DETAILS;
			
			try {
				
				if (multipartProjectThumbnail != null && !multipartProjectThumbnail.isEmpty()) {
					// Xóa ảnh thumbnail cũ
					deleteProjectResourceDir(targetUploadDir + Helper.FILE_SEPARTOR + oldNameThumbnail);
					
					// Cập nhật ảnh thumbnail mới
					String thumbnailImageName = multipartProjectThumbnail.getOriginalFilename();
					saveImage(multipartProjectThumbnail, targetUploadDir, thumbnailImageName);
					
					nProject.setThumbnail(project.getThumbnail());
				}

				if (multipartProjectImages != null) {
					List<Image> projectImages = new ArrayList<Image>();
					
					for (MultipartFile multipartImage : multipartProjectImages) {
						if (multipartImage != null && !multipartImage.isEmpty()) {
							String fImageName = multipartImage.getOriginalFilename();
							saveImage(multipartImage, uploadImagesDir, fImageName);
							Image img = new Image();
							img.setName(fImageName);
							img.setProject(nProject);
							
							projectImages.add(img);
						}
					}
					
					// Cập nhật thêm hình ảnh mô tả
					if (!projectImages.isEmpty()) {
						nProject.setImages(projectImages);
					}
				}
				
				pRepository.save(nProject);
				
				
				return true;

			} catch (IOException e) {
				System.err.println(ProjectHelper.class.getSimpleName() + ": " + e.getMessage());
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

	public static void deleteProjectResourceDir(String pathName) {
		File fImageDir = new File(pathName);
		if (fImageDir.exists()) {
			if (fImageDir.isDirectory()) {
				
				for (File fImage : fImageDir.listFiles()) {
					if (fImage.isFile()) {
						fImage.delete();
					} else {
						deleteProjectResourceDir(fImage.getPath());
					}
				}
			}
			fImageDir.delete();
		}
	}
}