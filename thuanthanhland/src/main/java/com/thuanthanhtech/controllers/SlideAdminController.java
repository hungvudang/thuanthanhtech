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

import com.thuanthanhtech.entities.Slide;
import com.thuanthanhtech.entities.SlideHelper;
import com.thuanthanhtech.repositories.SlideRepository;

@Controller
@RequestMapping("/slide")
public class SlideAdminController {

	@Autowired
	private SlideRepository sRepository;

	@GetMapping
	public String slide(Model m) {
		List<Slide> slides = sRepository.findAll();

		m.addAttribute("slides", slides);
		m.addAttribute("active_slide", true);

		return "admin-pages/slide";
	}

	@GetMapping("/create")
	public String createSlide(Model m) {

		if (m.getAttribute("slide") == null) {
			Slide slide = new Slide();
			slide.setSort(0);
			slide.setPub(1);
			slide.setImage(SlideHelper.NO_IMAGE_MEDIUM);

			m.addAttribute("slide", slide);
		}

		m.addAttribute("active_slide", true);
		return "admin-pages/create-slide";
	}

	@PostMapping("/save")
	public String saveSlide(@Valid @ModelAttribute("slide") Slide slide, BindingResult br,
			@RequestParam("slide_image") MultipartFile multipartFile, RedirectAttributes ra) throws IOException {

		if (br.hasErrors() || multipartFile.isEmpty()) {
			ra.addFlashAttribute("error", "Tạo slide mới thất bại");
			if (br.hasFieldErrors("name")) {
				ra.addFlashAttribute("isNameError", true);
				ra.addFlashAttribute("nameErrorMessage", br.getFieldError("name").getDefaultMessage());
			}

			if (br.hasFieldErrors("title")) {
				ra.addFlashAttribute("isTitleError", true);
				ra.addFlashAttribute("titleErrorMessage", br.getFieldError("title").getDefaultMessage());
			}

			if (multipartFile.isEmpty()) {
				ra.addFlashAttribute("isImageError", true);
				ra.addFlashAttribute("imageErrorMessage", "Hình ảnh không được để trống");
			}

			if (br.hasFieldErrors("sort")) {
				ra.addFlashAttribute("isSortError", true);
				ra.addFlashAttribute("sortErrorMessage", br.getFieldError("sort").getDefaultMessage());
			}
			slide.setImage(SlideHelper.NO_IMAGE_MEDIUM);
			ra.addFlashAttribute("slide", slide);
			return "redirect:/slide/create";
		}
//		else
		slide.setImage("author:hungv");
		Slide saveSlide = sRepository.saveAndFlush(slide);

		// Cập nhật lại path image cho slide mới tạo
		// =========================================================================================
		String fImageSlideName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		String uploadDir = SlideHelper.ROOT_PATH_IMAGE_MEDIUM + File.separator + saveSlide.getId();
		SlideHelper.saveImage(multipartFile, uploadDir, fImageSlideName);
		saveSlide.setImage(uploadDir + File.separator + fImageSlideName);
		sRepository.save(saveSlide);
		// =========================================================================================
		ra.addFlashAttribute("success", "Slide mới đã được tạo thành công");
		return "redirect:/slide";
	}

	@GetMapping("/detail/{id}")
	public String detailSlide(@PathVariable("id") Integer id, Model m, RedirectAttributes ra) {

		Optional<Slide> opSlide = sRepository.findById(id);
		if (opSlide.isPresent()) {
			m.addAttribute("slide", opSlide.get());
			ra.addFlashAttribute("active_slide", true);
			return "admin-pages/slide-detail";
		}
//		else
		ra.addFlashAttribute("error", "Slide không tồn tại hoặc đã bị xóa");
		return "redirect:/slide";
	}

	@PostMapping("/update/{id}")
	public String updateSlide(@PathVariable("id") Integer id, @Valid @ModelAttribute("slide") Slide slide,
			BindingResult br, @RequestParam("slide_image") MultipartFile multipartFile, Model m, RedirectAttributes ra)
			throws IOException {

		if (br.hasErrors()) {

			if (br.hasFieldErrors("name")) {
				ra.addFlashAttribute("isNameError", true);
				ra.addFlashAttribute("nameErrorMessage", br.getFieldError("name").getDefaultMessage());
			}

			if (br.hasFieldErrors("title")) {
				ra.addFlashAttribute("isTitleError", true);
				ra.addFlashAttribute("titleErrorMessage", br.getFieldError("title").getDefaultMessage());
			}

			if (br.hasFieldErrors("sort")) {
				ra.addFlashAttribute("isSortError", true);
				ra.addFlashAttribute("sortErrorMessage", br.getFieldError("sort").getDefaultMessage());
			}
			ra.addFlashAttribute("error", "Cập nhật slide thất bại. Vui lòng kiểm tra lại");
			

		} else {
			Optional<Slide> opSlide = sRepository.findById(id);
			if (opSlide.isPresent()) {
				Slide updateSlide = opSlide.get();
				updateSlide.setName(slide.getName());
				updateSlide.setTitle(slide.getTitle());
				updateSlide.setPub(slide.getPub());
				updateSlide.setSort(slide.getSort());

				// Cập nhật lại path image cho slide
				// =========================================================================================
				if (!multipartFile.isEmpty()) {
					String fImageSlideName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					String uploadDir = SlideHelper.ROOT_PATH_IMAGE_MEDIUM + File.separator + updateSlide.getId();
					SlideHelper.saveImage(multipartFile, uploadDir, fImageSlideName);
					updateSlide.setImage(uploadDir + File.separator + fImageSlideName);
				}
				// =========================================================================================
				
				sRepository.save(updateSlide);
				ra.addFlashAttribute("success", "Slide đã được cập nhật thành công");
				
			} else {
				ra.addFlashAttribute("error", "Slide không tồn tại hoặc đã bị xóa. Vui lòng kiểm tra lại");
				return "redirect:/slide";
			}

		}
		
		//
		return "redirect:/slide/detail/" + id;
	}
	
	@GetMapping("/delete/{id}")
	public String deleteSlide(@PathVariable("id") Integer id, RedirectAttributes ra) {
		
		Optional<Slide> opSlide = sRepository.findById(id);
		if (opSlide.isPresent()) {
			sRepository.deleteById(id);
			deleteImageDir(id);
			
			ra.addFlashAttribute("success", "Slide đã được xóa thành công");
		} else {
			ra.addFlashAttribute("error", "Slide không tồn tại hoặc đã bị xóa. Vui lòng kiểm tra lại");
		}
		
		return "redirect:/slide";
	}

	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "admin-pages/500";
	}
	
	
	/**
	 * @param slideId
	 * Xóa thư mục lưu ảnh của slide bị xóa đi
	 */
	private void deleteImageDir(Integer slideId) {
		Path pSlideImage = Paths.get(SlideHelper.ROOT_PATH_IMAGE_MEDIUM + File.separator + slideId);

		File fUploadImageDir = pSlideImage.toFile();
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
