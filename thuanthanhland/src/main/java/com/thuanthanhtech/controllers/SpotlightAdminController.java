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
import com.thuanthanhtech.entities.Spotlight;
import com.thuanthanhtech.entities.SpotlightHelper;
import com.thuanthanhtech.repositories.SpotlightRepository;

@Controller
@RequestMapping("/admin/spotlight")
public class SpotlightAdminController {

	@Autowired
	private SpotlightRepository sRepository;

	@GetMapping
	public String spotLight(Model m) {
		List<Spotlight> spotlights = sRepository.findAll();

		m.addAttribute("spotlights", spotlights);
		m.addAttribute("active_spotlight", true);
		return "admin-pages/spotlight";
	}

	@GetMapping("/create")
	public String createSpotlight(Model m) {

		if (!m.containsAttribute("spotlight")) {
			Spotlight spotlight = new Spotlight();
			spotlight.setPub(1);
			spotlight.setSort(1);
			spotlight.setImage(Helper.NO_IMAGE_MEDIUM_PNG);
			m.addAttribute("spotlight", spotlight);
		}
		m.addAttribute("active_spotlight", true);
		return "admin-pages/spotlight-create";
	}

	@PostMapping("/save")
	public String saveSpotlight(@Valid @ModelAttribute("spotlight") Spotlight spotlight, BindingResult br,
			RedirectAttributes ra, @RequestParam("spotlight_image") MultipartFile multipartFile) throws IOException {
		if (br.hasErrors() || multipartFile.isEmpty() || spotlight.getSort() == null) {
			if (multipartFile.isEmpty()) {
				FieldError imageError = new FieldError("spotlight", "image", "Hình ảnh không được để trống");
				br.addError(imageError);
			}
			
			if (spotlight.getSort() == null) {
				FieldError sortError = new FieldError("spotlight", "sort", "Thứ tự hiển thị không hợp lệ");
				br.addError(sortError);
			}
			ra.addFlashAttribute("error", "Tạo spotlight mới thất bại");
			ra.addFlashAttribute("org.springframework.validation.BindingResult.spotlight", br);
			spotlight.setImage(Helper.NO_IMAGE_MEDIUM_PNG);
			ra.addFlashAttribute("spotlight", spotlight);

			return "redirect:/admin/spotlight/create";
		}

		// Kiểm tra thứ tự hiển thị có bị trùng không
		// =======================================================================================
		Optional<Spotlight> opSpotlight = sRepository.findBySort(spotlight.getSort());
		if (opSpotlight.isPresent()) {
			FieldError sortError = new FieldError("spotlight", "sort", "Thứ tự hiển thị bị trùng");
			br.addError(sortError);

			ra.addFlashAttribute("error", "Tạo spotlight mới thất bại");
			spotlight.setImage(Helper.NO_IMAGE_MEDIUM_PNG);
			ra.addFlashAttribute("org.springframework.validation.BindingResult.spotlight", br);
			ra.addFlashAttribute("spotlight", spotlight);
			return "redirect:/admin/spotlight/create";
		}
		// =======================================================================================

		// Kiểm tra file upload lên có đúng định dạng không
		String contentType = multipartFile.getContentType();
		if (!contentType.matches("^image/.+")) {
			FieldError imageError = new FieldError("spotlight", "image",
					"Hình ảnh không đúng định dạng. Ảnh phải có định dạnh (*.jpg, *.jpge, *.png)");

			br.addError(imageError);
			ra.addFlashAttribute("error", "Tạo spotlight mới thất bại");
			spotlight.setImage(Helper.NO_IMAGE_MEDIUM_PNG);
			ra.addFlashAttribute("org.springframework.validation.BindingResult.spotlight", br);
			ra.addFlashAttribute("spotlight", spotlight);
			return "redirect:/admin/spotlight/create";
		}

		// Upload Image
		// ======================================================================================================
		String fImageName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		String uploadDir = StringUtils
				.cleanPath(SpotlightHelper.ROOT_PATH_THUMBNAIL_MEDIUM + Helper.FILE_SEPARTOR + spotlight.getSort());

		SpotlightHelper.saveImage(multipartFile, uploadDir, fImageName);

		spotlight.setImage(SpotlightHelper.ROOT_PATH_THUMBNAIL_MEDIUM + Helper.FILE_SEPARTOR + spotlight.getSort()
				+ Helper.FILE_SEPARTOR + fImageName);

		// ============================================================================================================
		sRepository.saveAndFlush(spotlight);
		ra.addFlashAttribute("success", "Spotlight mới đã được tạo thành công");
		return "redirect:/admin/spotlight";
	}

	@GetMapping("/detail/{id}")
	public String detailSpotlight(@PathVariable("id") Integer id, Model m, RedirectAttributes ra) {
		if (!m.containsAttribute("spotlight")) {
			Optional<Spotlight> opSpotlight = sRepository.findById(id);
			if (opSpotlight.isPresent()) {
				Spotlight spotlight = opSpotlight.get();
				m.addAttribute("spotlight", spotlight);
			} else {
				ra.addFlashAttribute("error", "Spotligt không tồn tại hoặc đã bị xóa");
				return "redirect:/admin/spotlight";
			}
		}

		m.addAttribute("active_spotlight", true);
		return "admin-pages/spotlight-detail";

	}

	@PostMapping("/update/{id}")
	public String updateSpotlight(@PathVariable("id") Integer id,
			@Valid @ModelAttribute("spotlight") Spotlight spotlight, BindingResult br, RedirectAttributes ra,
			@RequestParam("spotlight_image") MultipartFile multipartFile) throws IOException {

		if (br.hasErrors() || spotlight.getSort() == null) {
			
			if (spotlight.getSort() == null) {
				FieldError sortError = new FieldError("spotlight", "sort", "Thứ tự hiển thị không hợp lệ");
				br.addError(sortError);
			}
			
			ra.addFlashAttribute("org.springframework.validation.BindingResult.spotlight", br);
			ra.addFlashAttribute("spotlight", spotlight);
			ra.addFlashAttribute("error", "Cập nhật spotlight thất bại");
			return "redirect:/admin/spotlight/detail/" + id;
		}

		// Kiểm tra thứ tự hiển thị có bị trùng không
		// =======================================================================================
		Optional<Spotlight> opSpotlight = sRepository.findBySort(spotlight.getSort());
		if (opSpotlight.isPresent() && opSpotlight.get().getId() != id) {
			FieldError sortError = new FieldError("spotlight", "sort", "Thứ tự hiển thị bị trùng");
			br.addError(sortError);

			ra.addFlashAttribute("error", "Cập nhật spotlight thất bại");
			ra.addFlashAttribute("org.springframework.validation.BindingResult.spotlight", br);
			ra.addFlashAttribute("spotlight", spotlight);
			return "redirect:/admin/spotlight/detail/" + id;
		}
		// =======================================================================================

		Spotlight nSpotlight = sRepository.findById(id).get();

		nSpotlight.setName(spotlight.getName());
		nSpotlight.setTitle(spotlight.getTitle());
		nSpotlight.setDescription(spotlight.getDescription());

		int oldSort = nSpotlight.getSort();

		nSpotlight.setSort(spotlight.getSort());
		nSpotlight.setPub(spotlight.getPub());

		// Đổi tên thư mục upload image
		// ===========================================================================================================
		if (oldSort != spotlight.getSort()) {
			Path oldImagePathDir = Paths.get(
					StringUtils.cleanPath(SpotlightHelper.ROOT_PATH_THUMBNAIL_MEDIUM + Helper.FILE_SEPARTOR + oldSort));
			Path newImagePathDir = Paths.get(StringUtils.cleanPath(
					SpotlightHelper.ROOT_PATH_THUMBNAIL_MEDIUM + Helper.FILE_SEPARTOR + nSpotlight.getSort()));
			Files.move(oldImagePathDir, newImagePathDir, StandardCopyOption.REPLACE_EXISTING);

			String fImageName = spotlight.getImage().substring(spotlight.getImage().lastIndexOf('/') + 1);

			spotlight.setImage(SpotlightHelper.ROOT_PATH_THUMBNAIL_MEDIUM + Helper.FILE_SEPARTOR + nSpotlight.getSort()
					+ Helper.FILE_SEPARTOR + fImageName);

		}
		// ===========================================================================================================

		// Kiểm tra file upload lên có đúng định dạng không
		if (multipartFile != null && !multipartFile.isEmpty()) {
			String contentType = multipartFile.getContentType();
			if (!contentType.matches("^image/.+")) {
				FieldError sortError = new FieldError("spotlight", "image",
						"Hình ảnh không đúng định dạng. Ảnh phải có định dạnh (*.jpg, *.jpge, *.png)");

				br.addError(sortError);
				ra.addFlashAttribute("error", "Cập nhật spotlight thất bại");
				ra.addFlashAttribute("org.springframework.validation.BindingResult.spotlight", br);
				ra.addFlashAttribute("spotlight", spotlight);
				return "redirect:/admin/spotlight/detail/" + id;
			}

			// Upload image
			String fImageName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String uploadDir = StringUtils.cleanPath(
					SpotlightHelper.ROOT_PATH_THUMBNAIL_MEDIUM + Helper.FILE_SEPARTOR + nSpotlight.getSort());
			
			// xóa hình ảnh cũ của spotlight
			deleteImageDir(spotlight.getImage());
			
			// Cập nhật hình ảnh mới
			SpotlightHelper.saveImage(multipartFile, uploadDir, fImageName);

			spotlight.setImage(SpotlightHelper.ROOT_PATH_THUMBNAIL_MEDIUM + Helper.FILE_SEPARTOR + nSpotlight.getSort()
					+ Helper.FILE_SEPARTOR + fImageName);

		}

		nSpotlight.setImage(spotlight.getImage());

		sRepository.saveAndFlush(nSpotlight);
		ra.addFlashAttribute("success", "Cập nhật spotlight thành công");
		return "redirect:/admin/spotlight/detail/" + id;

	}

	@GetMapping("/delete/{id}")
	public String deleteSpotlight(@PathVariable("id") Integer id, RedirectAttributes ra) {
		Optional<Spotlight> opSpotlight = sRepository.findById(id);
		if (opSpotlight.isPresent()) {
			sRepository.deleteById(id);
			deleteImageDir(opSpotlight.get().getImage());
			ra.addFlashAttribute("success", "Spotlight đã được xóa thành công");
		} else {
			ra.addFlashAttribute("error", "Spotlight đã bị xóa hoặc không tồn tại");
		}
		return "redirect:/admin/spotlight";
	}

	private void deleteImageDir(String fImagePath) {

		Path path = Paths.get(fImagePath);
		File fParentDirImage = path.toAbsolutePath().toFile().getParentFile();
		// xóa thư mục
		if (fParentDirImage.exists()) {
			if (fParentDirImage.isDirectory()) {
				String[] files = fParentDirImage.list();
				for (String childs : files) {
					File childDirt = new File(fParentDirImage, childs);
					childDirt.delete();
				}
			}
			fParentDirImage.delete();
		}
	}

	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "admin-pages/500";
	}

}
