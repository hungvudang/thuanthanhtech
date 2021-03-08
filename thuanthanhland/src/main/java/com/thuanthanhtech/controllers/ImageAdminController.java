package com.thuanthanhtech.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuanthanhtech.entities.Helper;
import com.thuanthanhtech.entities.Image;
import com.thuanthanhtech.entities.NewsHelper;
import com.thuanthanhtech.repositories.ImageRepository;

@RestController
@RequestMapping("/api/images")
@CrossOrigin("*")

public class ImageAdminController {

	@Autowired
	private ImageRepository iRepository;

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> deleteImageById(@PathVariable("id") Integer id) {
		
		Optional<Image> opImage = iRepository.findById(id);
		if (opImage.isPresent()) {
			iRepository.deleteById(id);
			
			String fImageName = opImage.get().getName();
			int news_id = opImage.get().getNews().getId();
			
			String uploadImageDir = NewsHelper.BASE_PATH_NEWS_RESOURCE + Helper.FILE_SEPARTOR + news_id
					+ Helper.FILE_SEPARTOR + NewsHelper.DIR_IMAGE_DETAILS;

			NewsHelper.deleteNewsResourceDir(uploadImageDir + Helper.FILE_SEPARTOR + fImageName);
			return ResponseEntity.ok().body("Đã xóa hình ảnh mô tả thành công");
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Xóa hình ảnh mô tả thất bại");
	}

}
