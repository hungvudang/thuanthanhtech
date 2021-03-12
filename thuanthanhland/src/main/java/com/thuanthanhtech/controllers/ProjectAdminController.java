package com.thuanthanhtech.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
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
		return "/admin-pages/project";

	}

	@GetMapping("/create")
	public String createProject(Model m) {
		if (!m.containsAttribute("project")) {
			Project project = new Project();
			project.setPub(1);
			project.setHot(0);
			project.setImage(Helper.NO_IMAGE_MEDIUM_PNG);
			m.addAttribute("project", project);
		}

		m.addAttribute("active_project", true);
		return "admin-pages/project-create";
	}

	@PostMapping("/save")
	public String saveProject(@Valid @ModelAttribute("project") Project project, BindingResult br,
			RedirectAttributes ra, @RequestParam("project_thumbnail") MultipartFile multipartFile) throws IOException {

		if (br.hasErrors() || multipartFile.isEmpty()) {

			if (multipartFile.isEmpty()) {
				FieldError imageError = new FieldError("project", "image", "Hình ảnh không được để trống");
				br.addError(imageError);
			}

			ra.addFlashAttribute("error", "Tạo dự án mới thất bại");
			project.setImage(Helper.NO_IMAGE_MEDIUM_PNG);
			ra.addFlashAttribute("org.springframework.validation.BindingResult.project", br);
			ra.addFlashAttribute("project", project);
			return "redirect:/admin/project/create";
		}

		// Upload ảnh thumbnail của dự án lên server
		// ===========================================================================================
		if (multipartFile != null && !multipartFile.isEmpty()) {
			// Kiểm tra file upload lên có đúng định dạng không
			String contentType = multipartFile.getContentType();
			if (!contentType.matches("^image/.+")) {
				FieldError imageError = new FieldError("project", "image",
						"Hình ảnh không đúng định dạng. Ảnh phải có định dạnh (*.jpg, *.jpge, *.png)");
				br.addError(imageError);

				ra.addFlashAttribute("error", "Tạo dự án mới thất bại");
				project.setImage(Helper.NO_IMAGE_MEDIUM_PNG);
				ra.addFlashAttribute("org.springframework.validation.BindingResult.project", br);
				ra.addFlashAttribute("project", project);
				return "redirect:/admin/project/create";
			}

			String fThumbnailImageName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String uploadDir = StringUtils
					.cleanPath(ProjectHelper.ROOT_PATH_IMAGE_MEDIUM + Helper.FILE_SEPARTOR + project.getSlug());
			ProjectHelper.saveImage(multipartFile, uploadDir, fThumbnailImageName);
			project.setImage(StringUtils.cleanPath(uploadDir + Helper.FILE_SEPARTOR + fThumbnailImageName));
		}

		// ============================================================================================
		pRepository.saveAndFlush(project);
		ra.addFlashAttribute("success", "Tạo dự án mới thành công");
		return "redirect:/admin/project";
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
		return "/admin-pages/project-detail";

	}

	@PostMapping("update/{id}")
	public String updateProject(@PathVariable("id") Integer id, @Valid @ModelAttribute("project") Project project,
			BindingResult br, RedirectAttributes ra, @RequestParam("project_thumbnail") MultipartFile multipartFile)
			throws IOException {

		if (br.hasErrors()) {

			ra.addFlashAttribute("org.springframework.validation.BindingResult.project", br);
			ra.addFlashAttribute("project", project);
			ra.addFlashAttribute("error", "Cập nhật dự án thất bại");
			return "redirect:/admin/project/detail/" + id;
		}

		Optional<Project> opProject = pRepository.findById(id);
		if (opProject.isPresent()) {
			Project nProject = opProject.get();

			nProject.setName(project.getName());
			nProject.setTitle(project.getTitle());
			nProject.setContent(project.getContent());
			nProject.setDescription(project.getDescription());
			nProject.setHot(project.getHot());
			nProject.setPub(project.getPub());
			String oldSlug = nProject.getSlug();
			nProject.setSlug(project.getSlug());

			// Đổi tên thư mục lưu ảnh thumbnail của dư án khi cập nhập slug
			// =========================================================================================================
			if (nProject.getImage() != null) {
				Path oldThumbnailPathDir = Path.of(
						StringUtils.cleanPath(ProjectHelper.ROOT_PATH_IMAGE_MEDIUM + Helper.FILE_SEPARTOR + oldSlug));
				Path newThumbnailPathDir = Path.of(StringUtils
						.cleanPath(ProjectHelper.ROOT_PATH_IMAGE_MEDIUM + Helper.FILE_SEPARTOR + nProject.getSlug()));

				Files.move(oldThumbnailPathDir.normalize(), newThumbnailPathDir.normalize(),
						StandardCopyOption.REPLACE_EXISTING);

				String fImageName = project.getImage().substring(project.getImage().lastIndexOf('/') + 1);
				
				project.setImage(StringUtils
						.cleanPath(ProjectHelper.ROOT_PATH_IMAGE_MEDIUM + Helper.FILE_SEPARTOR + nProject.getSlug() + Helper.FILE_SEPARTOR + fImageName));
			}
			// ==========================================================================================================

			// Cập nhật ảnh thumbnail của dự án lên server
			// ===========================================================================================
			if (multipartFile != null && !multipartFile.isEmpty()) {
				// Kiểm tra file upload lên có đúng định dạng không
				String contentType = multipartFile.getContentType();
				if (!contentType.matches("^image/.+")) {
					FieldError imageError = new FieldError("project", "image",
							"Hình ảnh không đúng định dạng. Ảnh phải có định dạnh (*.jpg, *.jpge, *.png)");
					br.addError(imageError);

					ra.addFlashAttribute("org.springframework.validation.BindingResult.project", br);
					ra.addFlashAttribute("project", project);
					ra.addFlashAttribute("error", "Cập nhật dự án thất bại");
					return "redirect:/admin/project/detail/" + id;
				}

				String fThumbnailImageName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				String uploadDir = StringUtils
						.cleanPath(ProjectHelper.ROOT_PATH_IMAGE_MEDIUM + Helper.FILE_SEPARTOR + nProject.getSlug());

				// Xóa thư ảnh thumbnail cũ
				deleteImageDir(nProject.getSlug());

				ProjectHelper.saveImage(multipartFile, uploadDir, fThumbnailImageName);
				project.setImage(StringUtils.cleanPath(uploadDir + Helper.FILE_SEPARTOR + fThumbnailImageName));

			}
			nProject.setImage(project.getImage());

			pRepository.save(nProject);
			ra.addFlashAttribute("success", "Cập nhật dự án thành công");
			return "redirect:/admin/project/detail/" + nProject.getId();
		} else {
			ra.addFlashAttribute("error", "Dự án không tồn tại hoặc đã bị xóa");
		}
		return "redirect:/admin/project";
	}

	@GetMapping("/delete/{id}")
	public String deleteProject(@PathVariable("id") Integer id, RedirectAttributes ra) {
		Optional<Project> opProject = pRepository.findById(id);
		if (opProject.isPresent()) {
			pRepository.deleteById(id);

			deleteImageDir(opProject.get().getSlug());

			ra.addFlashAttribute("success", "Xóa dự án thành công");
		} else {
			ra.addFlashAttribute("error", "Xóa dự án thất bại");
		}
		return "redirect:/admin/project";
	}

	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "admin-pages/500";
	}

	/**
	 * @param projectSlug Xóa thư mục lưu ảnh của dự án bị xóa đi
	 */
	private void deleteImageDir(String projectSlug) {
		Path pProjectImage = Paths.get(ProjectHelper.ROOT_PATH_IMAGE_MEDIUM + Helper.FILE_SEPARTOR + projectSlug);

		File fUploadImageDir = pProjectImage.toFile();
		if (fUploadImageDir.exists()) {
			File[] fImages = fUploadImageDir.listFiles();
			for (File f : fImages) {
				if (f.delete()) {
					System.out.println("Deleted file: " + f.getName());
				}
			}
			if (fUploadImageDir.delete()) {
				System.out.println("Deleted folder: " + fUploadImageDir.getName());
			}
		}
	}

}
