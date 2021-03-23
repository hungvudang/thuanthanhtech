package com.thuanthanhtech.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuanthanhtech.entities.Helper;
import com.thuanthanhtech.entities.Image;
import com.thuanthanhtech.entities.Project;
import com.thuanthanhtech.entities.ProjectHelper;
import com.thuanthanhtech.repositories.ProjectRepository;

@Controller
@RequestMapping("/admin/project")
public class ProjectAdminController {

	@Autowired
	private ProjectRepository pRepository;

	@GetMapping
	public String Project(Model m) {
		List<Project> projects = pRepository.findAll();
		m.addAttribute("projects", projects);
		m.addAttribute("active_project", true);
		m.addAttribute("BASE_PATH_PROJECT_RESOURCE", ProjectHelper.BASE_PATH_PROJECT_RESOURCE);
		m.addAttribute("DIR_IMAGE_DETAILS", ProjectHelper.DIR_IMAGE_DETAILS);
		return "admin-pages/project";

	}

	@GetMapping("/create")
	public String createProject(Model m) {
		if (!m.containsAttribute("project")) {
			Project project = new Project();
			project.setPub(1);
			project.setHot(0);
			project.setThumbnail(Helper.NO_IMAGE_MEDIUM_PNG);
			m.addAttribute("project", project);
		}

		m.addAttribute("active_project", true);
		m.addAttribute("BASE_PATH_PROJECT_RESOURCE", ProjectHelper.BASE_PATH_PROJECT_RESOURCE);
		m.addAttribute("DIR_IMAGE_DETAILS", ProjectHelper.DIR_IMAGE_DETAILS);

		return "admin-pages/project-create";
	}

	@PostMapping("/save")
	public String saveProject(@Valid @ModelAttribute("project") Project project, BindingResult br,
			@RequestParam("project_thumbnail") MultipartFile multipartProjectThumbnail,
			@RequestParam(name = "project_images[]", required = false) MultipartFile[] multipartProjectImages,
			RedirectAttributes ra) throws IOException {

		if (br.hasErrors() || multipartProjectThumbnail.isEmpty()) {

			if (multipartProjectThumbnail.isEmpty()) {
				FieldError imageError = new FieldError("project", "thumbnail", "Hình ảnh không được để trống");
				br.addError(imageError);
			}

			ra.addFlashAttribute("error", "Tạo dự án mới thất bại");
			ra.addFlashAttribute("org.springframework.validation.BindingResult.project", br);
			ra.addFlashAttribute("project", project);
			return "redirect:/admin/project/create";
		}

		// Upload ảnh thumbnail của dự án lên server
		// ===========================================================================================
		if (multipartProjectThumbnail != null && !multipartProjectThumbnail.isEmpty()) {
			// Kiểm tra file upload lên có đúng định dạng không
			String contentType = multipartProjectThumbnail.getContentType();
			if (!contentType.matches("^image/.+")) {
				FieldError imageError = new FieldError("project", "thumbnail",
						"Hình ảnh không đúng định dạng. Ảnh phải có định dạnh (*.jpg, *.jpge, *.png)");
				br.addError(imageError);

				ra.addFlashAttribute("error", "Tạo dự án mới thất bại");
				ra.addFlashAttribute("org.springframework.validation.BindingResult.project", br);
				ra.addFlashAttribute("project", project);
				return "redirect:/admin/project/create";
			}

			String fThumbnailImageName = StringUtils.cleanPath(multipartProjectThumbnail.getOriginalFilename());
			project.setThumbnail(fThumbnailImageName);
		}

		// ============================================================================================
		// Lưu ảnh mô tả nếu có
		// ============================================================================================
		if (multipartProjectImages != null) {
			List<Image> images = new ArrayList<Image>();
			for (MultipartFile multipartImage : multipartProjectImages) {
				if (multipartImage != null && !multipartImage.isEmpty()) {

					if (!validTypeOfProjectImage(multipartImage, "images", br)) {

						String fImageName = StringUtils.cleanPath(multipartImage.getOriginalFilename());
						Image img = new Image();
						img.setName(fImageName);
						images.add(img);

					} else {
						ra.addFlashAttribute("project", project);
						ra.addFlashAttribute("error", "Tạo dự án mới thất bại");
						return "redirect:/admin/project/create";
					}
				}
			}

			if (!images.isEmpty()) {
				project.setImages(images);
			}
		}
		// ============================================================================================

		if ((ProjectHelper.insertProjectEntity(project, multipartProjectThumbnail, multipartProjectImages,
				pRepository))) {
			ra.addFlashAttribute("success", "Dự án mới đã được tạo thành công");

		} else {
			ra.addFlashAttribute("project", project);
			ra.addFlashAttribute("error", "Tạo dự án mới thất bại");
		}
		return "redirect:/admin/project/create";
	}

	@GetMapping("/detail/{id}")
	public String detailProject(@PathVariable("id") Integer id, Model m, RedirectAttributes ra) {
		if (!m.containsAttribute("project")) {
			Optional<Project> opProject = pRepository.findById(id);
			if (opProject.isPresent()) {
				Project project = opProject.get();
				m.addAttribute("project", project);
			} else {
				ra.addFlashAttribute("error", "Dự án không tồn tại hoặc đã bị xóa");
				return "redirect:/admin/project";
			}
		}

		m.addAttribute("active_project", true);
		m.addAttribute("BASE_PATH_PROJECT_RESOURCE", ProjectHelper.BASE_PATH_PROJECT_RESOURCE);
		m.addAttribute("DIR_IMAGE_DETAILS", ProjectHelper.DIR_IMAGE_DETAILS);
		return "/admin-pages/project-detail";

	}

	@PostMapping("update/{id}")
	public String updateProject(@PathVariable("id") Integer id, @Valid @ModelAttribute("project") Project project,
			BindingResult br, @RequestParam("project_thumbnail") MultipartFile multipartProjectThumbnail,
			@RequestParam(name = "project_images[]", required = false) MultipartFile[] multipartProjectImages,
			RedirectAttributes ra) throws IOException {

		if (br.hasErrors()) {

			ra.addFlashAttribute("org.springframework.validation.BindingResult.project", br);
			ra.addFlashAttribute("project", project);
			ra.addFlashAttribute("error", "Cập nhật dự án thất bại");
			return "redirect:/admin/project/detail/" + id;
		}

		// Cập nhật ảnh thumbnail của dự án lên server
		// =======================================================
		if (multipartProjectThumbnail != null && !multipartProjectThumbnail.isEmpty()) {

			// Kiểm tra file upload lên có đúng định dạng không
			if (validTypeOfProjectImage(multipartProjectThumbnail, "thumbnail", br)) {

				ra.addFlashAttribute("project", project);
				ra.addFlashAttribute("org.springframework.validation.BindingResult.project", br);
				ra.addFlashAttribute("error", "Cập nhật dự án thất bại");
				return "redirect:/admin/project/detail/" + id;
			}

			String fThumbnailImageName = StringUtils.cleanPath(multipartProjectThumbnail.getOriginalFilename());
			project.setThumbnail(fThumbnailImageName);
		}
		// ============================================================================================

		// Cập nhật Images Detail
		// ============================================================================================
		if (multipartProjectImages != null) {
			for (MultipartFile multipartImage : multipartProjectImages) {
				if (multipartImage != null && !multipartImage.isEmpty()) {

					if (validTypeOfProjectImage(multipartImage, "images", br)) {

						ra.addFlashAttribute("project", project);
						ra.addFlashAttribute("error", "Cập nhật dự án thất bại. Hình ảnh mô tả không đúng định dạng");
						return "redirect:/admin/project/detail/" + id;

					}
				}
			}
		}
		// ============================================================================================

		if (ProjectHelper.updateProjectById(id, project, multipartProjectThumbnail, multipartProjectImages,
				pRepository)) {

			ra.addFlashAttribute("success", "Dự án đã được cập nhật thành công");
			return "redirect:/admin/project/detail/" + id;

		} else {

			ra.addFlashAttribute("error", "Cập nhật dự án thất bại");
			return "redirect:/admin/project";
		}

	}

	@GetMapping("/delete/{id}")
	public String deleteProject(@PathVariable("id") Integer id, RedirectAttributes ra) {
		Optional<Project> opProject = pRepository.findById(id);
		if (opProject.isPresent()) {
			pRepository.deleteById(id);

			ProjectHelper.deleteProjectResourceDir(ProjectHelper.BASE_PATH_PROJECT_RESOURCE + Helper.FILE_SEPARTOR + id);
			ra.addFlashAttribute("success", "Xóa dự án thành công");

		} else {
			ra.addFlashAttribute("error", "Xóa dự án thất bại");
		}
		return "redirect:/admin/project";
	}

	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "/errors/500";
	}

	// Kiểm tra file upload lên có đúng định dạng không
	private boolean validTypeOfProjectImage(MultipartFile multipartFile, String nameFieldError, BindingResult br) {

		String contentType = multipartFile.getContentType();

		if (!contentType.matches("^image/.+")) {
			FieldError error = new FieldError("project", nameFieldError,
					"Hình ảnh không đúng định dạng. Ảnh phải có định dạnh (*.jpg, *.jpge, *.png)");
			br.addError(error);
			return true;
		}

		// else
		return false;
	}

}