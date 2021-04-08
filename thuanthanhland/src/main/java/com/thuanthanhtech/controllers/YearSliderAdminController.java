package com.thuanthanhtech.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

import com.thuanthanhtech.entities.YearSlider;
import com.thuanthanhtech.entities.YearSliderHelper;
import com.thuanthanhtech.repositories.YearSliderRepository;

@Controller
@RequestMapping("/admin/year-slider")
public class YearSliderAdminController {
	@Autowired
	YearSliderRepository yRepository;

	@GetMapping
	public String yearSlider(Model m) {
		List<YearSlider> yearsliders = yRepository.findAll();
		m.addAttribute("yearsliders", yearsliders);
		m.addAttribute("active_yearslider", true);
		return "admin-pages/yearslider";
	}

	@GetMapping("/create")
	public String createYearslider(Model m) {
		if (!m.containsAttribute("yearslider")) {
			YearSlider slider = new YearSlider();
			slider.setHot(0);
			slider.setPub(1);
			slider.setThumbnail(Helper.NO_IMAGE_MEDIUM_PNG);
			m.addAttribute("yearslider", slider);
		}
		m.addAttribute("active_yearslider", true);
		return "admin-pages/yearslider-create";
	}

	@PostMapping("/save")
	public String saveYearSlider(@Valid @ModelAttribute("yearslider") YearSlider yearslider,
			@RequestParam("yearslider_thumbnail") MultipartFile multipartFile, RedirectAttributes ra, BindingResult br)
			throws IOException {
		if (br.hasErrors() || multipartFile.isEmpty()) {
			ra.addFlashAttribute("error", "tạo slider thất bại");

			if (multipartFile.isEmpty()) {
				FieldError error = new FieldError("yearslider", "thumbnail", "Hình ảnh không được để trống");
				br.addError(error);
			}

			ra.addFlashAttribute("error", "Tạo year slider thất bại");
			ra.addFlashAttribute("yearslider", yearslider);
			yearslider.setThumbnail(Helper.NO_IMAGE_MEDIUM_PNG);
			ra.addFlashAttribute("org.springframework.validation.BindingResult.spotlight", br);
			return "redirect:/admin/year-slider/create";
		}
		// Kiểm tra file upload lên có đúng định dạng không
		String contentType = multipartFile.getContentType();
		if (!contentType.matches("^image/.+")) {
			FieldError imageError = new FieldError("slide", "image",
					"Hình ảnh không đúng định dạng. Ảnh phải có định dạnh (*.jpg, *.jpge, *.png)");
			br.addError(imageError);

			yearslider.setThumbnail(Helper.NO_IMAGE_MEDIUM_PNG);
			ra.addFlashAttribute("slide", yearslider);
			ra.addFlashAttribute("org.springframework.validation.BindingResult.slide", br);
			ra.addFlashAttribute("error", "Tạo mới slide thất bại");
			return "redirect:/admin/year-slider/create";
		}

		// Upload Thumbnail

		String fthumnail = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		String uploadDir = StringUtils.cleanPath(YearSliderHelper.ROOT_PATH_THUMBNAIL_MEDIUM + Helper.FILE_SEPARTOR);

		YearSliderHelper.saveThumbnail(multipartFile, uploadDir, fthumnail);

		yearslider.setThumbnail(YearSliderHelper.ROOT_PATH_THUMBNAIL_MEDIUM + Helper.FILE_SEPARTOR + fthumnail);

		yRepository.saveAndFlush(yearslider);
		ra.addFlashAttribute("success", "slider mới tạo thành công");
		return "redirect:/admin/year-slider";

	}

	@GetMapping("/detail/{id}")
	public String detailYearSlider(@PathVariable("id") Integer id, Model m, RedirectAttributes ra) {
		if (!m.containsAttribute("yearslider")) {
			Optional<YearSlider> opYearSlider = yRepository.findById(id);
			if (opYearSlider.isPresent()) {
				YearSlider yearslider = opYearSlider.get();
				m.addAttribute("yearslider", yearslider);
			} else {
				ra.addFlashAttribute("error", "slider không tồn tại");
				return "redirect:/admin/year-slider";
			}
		}
		m.addAttribute("active_yearslider", true);
		return "admin-pages/yearslider-detail";
	}

	@PostMapping("/update/{id}")
	public String updateYearSlider(@PathVariable("id") Integer id, @ModelAttribute("yearslider") YearSlider yearslider,
			RedirectAttributes ra, @RequestParam("yearslider_thumbnail") MultipartFile multipartFile) {

		return "";
	}

	@GetMapping("/delete/{id}")
	public String deleteYearSlider(@PathVariable("id") Integer id, RedirectAttributes ra) {
		Optional<YearSlider> opYearSlider = yRepository.findById(id);
		if (opYearSlider.isPresent()) {
			yRepository.deleteById(id);
			deleteThumbnail(id);
			ra.addFlashAttribute("success", "Year slider đã được xóa thành công");
		} else {
			ra.addFlashAttribute("error", "Slide không tồn tại. Vui lòng thử lại");
		}

		return "redirect:/admin/year-slider";
	}

	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "/errors/500";
	}

	// xóa thư mục image trong yearslider
	private void deleteThumbnail(Integer yearSliderId) {
		Path pYearSliderThumbnail = Paths
				.get(YearSliderHelper.ROOT_PATH_THUMBNAIL_MEDIUM + Helper.FILE_SEPARTOR + yearSliderId);
		File fUploadThumbnailDir = pYearSliderThumbnail.toFile();
		if (fUploadThumbnailDir.exists()) {
			File[] fThumbnail = fUploadThumbnailDir.listFiles();
			for (File file : fThumbnail) {
				if (file.delete()) {
					System.out.println("Đã xóa file" + file.getName());
				}
			}
			if (fUploadThumbnailDir.delete()) {
				System.out.println("Đã xóa folder: " + fUploadThumbnailDir.getName());
			}
		}
	}
}
