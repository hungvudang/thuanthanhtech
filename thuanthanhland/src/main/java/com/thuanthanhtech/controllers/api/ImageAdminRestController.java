package com.thuanthanhtech.controllers.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.thuanthanhtech.entities.Helper;
import com.thuanthanhtech.entities.Image;
import com.thuanthanhtech.entities.NewsHelper;
import com.thuanthanhtech.entities.ProjectHelper;
import com.thuanthanhtech.repositories.ImageRepository;

@RestController
@RequestMapping("/admin/api/images")
@CrossOrigin("*")

public class ImageAdminRestController {

	@Autowired
	private ImageRepository iRepository;

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> deleteImageById(@PathVariable("id") Integer id) {
		
		Optional<Image> opImage = iRepository.findById(id);
		if (opImage.isPresent()) {
			iRepository.deleteById(id);
			
			String fImageName = opImage.get().getName();
			int project_id = opImage.get().getProject().getId();
			
			String uploadImageDir = ProjectHelper.BASE_PATH_PROJECT_RESOURCE + Helper.FILE_SEPARTOR + project_id
					+ Helper.FILE_SEPARTOR + ProjectHelper.DIR_IMAGE_DETAILS;

			NewsHelper.deleteNewsResourceDir(uploadImageDir + Helper.FILE_SEPARTOR + fImageName);
			return ResponseEntity.ok().body("Đã xóa hình ảnh mô tả thành công");
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Xóa hình ảnh mô tả thất bại");
	}
	
	
	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "/errors/500";
	}
}
