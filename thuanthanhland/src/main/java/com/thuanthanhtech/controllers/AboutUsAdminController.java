package com.thuanthanhtech.controllers;

import java.io.IOException;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuanthanhtech.repositories.AboutUsRepository;
import com.thuanthanhtech.entities.AboutUs;
import com.thuanthanhtech.entities.AboutUsHelper;
import com.thuanthanhtech.entities.Helper;
import com.thuanthanhtech.entities.News;

@SuppressWarnings("unused")
@Controller
@RequestMapping("/admin/aboutus")
public class AboutUsAdminController {

	@Autowired
	private AboutUsRepository aReponsitory;

	@GetMapping
	public String AboutUs(Model m) {
		List<AboutUs> aboutuses = aReponsitory.findAll();
		m.addAttribute("aboutuses", aboutuses);
		m.addAttribute("active_aboutus", true);

		return "admin-pages/aboutus";
	}

	@GetMapping("/create")
	public String createAboutUs(Model m) {
		if (!m.containsAttribute("aboutus")) {
			AboutUs aboutus = new AboutUs();
			aboutus.setPub(1);
			aboutus.setThumbnail(Helper.NO_IMAGE_MEDIUM_PNG);
			m.addAttribute("aboutUs", aboutus);
		}
		m.addAttribute("active_aboutus", true);
		return "admin-pages/aboutus-create";
	}

	@PostMapping("/save")
	public String saveAboutUs(@ModelAttribute("aboutus") AboutUs aboutus, RedirectAttributes ra,
			@RequestParam("aboutus_thumbnail") MultipartFile multipartFile, BindingResult br) throws IOException {
		if (br.hasErrors()) {
			if (multipartFile.isEmpty()) {
				FieldError error = new FieldError("aboutus", "thumbnail", "Hình ảnh không được để trống");
				br.addError(error);
			}
			ra.addFlashAttribute("error", "Tạo bài viết thất bại");
			ra.addFlashAttribute("aboutus", aboutus);
			aboutus.setThumbnail(Helper.NO_IMAGE_MEDIUM_PNG);
			ra.addFlashAttribute("org.springframework.validation.BindingResult.aboutus", br);
			return "redirect:/admin/aboutus/create";
		}
		// Upload thumbnail
		String fthumbnail = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		String uploadDir = StringUtils
				.cleanPath(AboutUsHelper.ROOT_PATH_THUMBNAIL_MEDIUM + Helper.FILE_SEPARTOR + fthumbnail);

		AboutUsHelper.saveThumbnail(multipartFile, uploadDir, fthumbnail);
		aboutus.setThumbnail(AboutUsHelper.ROOT_PATH_THUMBNAIL_MEDIUM + Helper.FILE_SEPARTOR + fthumbnail);
		
		aReponsitory.saveAndFlush(aboutus);
		ra.addFlashAttribute("success", "Bài viết được tạo thành công");
		return "redirect:/admin/aboutus";
	}

	@GetMapping("/detail/{id}")
	public String detailAboutUs(@PathVariable("id") Integer id, Model m, RedirectAttributes ra ) {

		if(!m.containsAttribute("aboutus")) {
			Optional<AboutUs> opAboutUs = aReponsitory.findById(id);
			if(opAboutUs.isPresent()) {
				AboutUs aboutUs = opAboutUs.get();
				m.addAttribute("aboutus", aboutUs);
			}else {
				ra.addFlashAttribute("error", "bài viết không tồn tại");
				return "redirect:/admin/aboutus";
			}
		}
		m.addAttribute("active_yearslider", true);
		return "admin-pages/aboutus-detail";
	}
	@PostMapping("/update/{id}")
	public String updateAboutUs() {
		return "";		
	}
	
	@GetMapping("/delete/{id}")
	public String deleteAboutUs(@PathVariable("id") Integer id, RedirectAttributes ra ) {
		Optional<AboutUs> opAboutUs = aReponsitory.findById(id);
		if(opAboutUs.isPresent()) {
			aReponsitory.deleteById(id);
			
			ra.addFlashAttribute("success", "Bài viết đã được tạo thành công");
		}
		return "";
	}
	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "/errors/500";
	}
}
