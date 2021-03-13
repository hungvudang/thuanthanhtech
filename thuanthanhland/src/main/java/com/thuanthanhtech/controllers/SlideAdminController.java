package com.thuanthanhtech.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
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
import com.thuanthanhtech.entities.Slide;
import com.thuanthanhtech.entities.SlideHelper;
import com.thuanthanhtech.repositories.SlideRepository;

@Controller
@RequestMapping("/admin/slide")
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

		if (!m.containsAttribute("slide")) {
			Slide slide = new Slide();
			slide.setSort(0);
			slide.setPub(1);
			slide.setImage(Helper.NO_IMAGE_MEDIUM_PNG);
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

			if (multipartFile.isEmpty()) {
				FieldError imageError = new FieldError("slide", "image", "Hình ảnh không được để trống");
				br.addError(imageError);
			}

			slide.setImage(Helper.NO_IMAGE_MEDIUM_PNG);
			ra.addFlashAttribute("slide", slide);
			ra.addFlashAttribute("org.springframework.validation.BindingResult.slide", br);
			ra.addFlashAttribute("error", "Tạo mới slide thất bại");
			return "redirect:/admin/slide/create";
		}
//		else

		// Kiểm tra trường sort là duy nhất
		Optional<Slide> opCheckSlide = sRepository.findBySort(slide.getSort());
		if (opCheckSlide.isPresent()) {
			FieldError sortError = new FieldError("slide", "sort", "Thứ tự hiện thị bị trùng");
			br.addError(sortError);

			slide.setImage(Helper.NO_IMAGE_MEDIUM_PNG);
			ra.addFlashAttribute("slide", slide);
			ra.addFlashAttribute("org.springframework.validation.BindingResult.slide", br);
			ra.addFlashAttribute("error", "Tạo mới slide thất bại");
			return "redirect:/admin/slide/create";
		}

		// Kiểm tra file upload lên có đúng định dạng không
		String contentType = multipartFile.getContentType();
		if (!contentType.matches("^image/.+")) {
			FieldError imageError = new FieldError("slide", "image",
					"Hình ảnh không đúng định dạng. Ảnh phải có định dạnh (*.jpg, *.jpge, *.png)");
			br.addError(imageError);

			slide.setImage(Helper.NO_IMAGE_MEDIUM_PNG);
			ra.addFlashAttribute("slide", slide);
			ra.addFlashAttribute("org.springframework.validation.BindingResult.slide", br);
			ra.addFlashAttribute("error", "Tạo mới slide thất bại");
			return "redirect:/admin/slide/create";
		}

		slide.setImage("author:hungv");
		Slide saveSlide = sRepository.saveAndFlush(slide);

		// Cập nhật lại path image cho slide mới tạo
		// =========================================================================================
		System.out.println(SlideAdminController.class.getSimpleName() + ": " + multipartFile.getContentType());
		String fImageSlideName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		String uploadDir = SlideHelper.ROOT_PATH_IMAGE_MEDIUM + Helper.FILE_SEPARTOR + saveSlide.getId();
		SlideHelper.saveImage(multipartFile, uploadDir, fImageSlideName);
		saveSlide.setImage(uploadDir + Helper.FILE_SEPARTOR + fImageSlideName);
		sRepository.save(saveSlide);
		// =========================================================================================
		ra.addFlashAttribute("success", "Slide mới đã được tạo thành công");
		return "redirect:/admin/slide";
	}

	@GetMapping("/detail/{id}")
	public String detailSlide(@PathVariable("id") Integer id, Model m, RedirectAttributes ra) {

		if (m.containsAttribute("slide")) {
			m.addAttribute("active_slide", true);
			return "admin-pages/slide-detail";
		}
//		else 

		Optional<Slide> opSlide = sRepository.findById(id);
		if (opSlide.isPresent()) {
			m.addAttribute("slide", opSlide.get());
			m.addAttribute("active_slide", true);
			return "admin-pages/slide-detail";
		}
//		else
		ra.addFlashAttribute("error", "Slide không tồn tại hoặc đã bị xóa");
		return "redirect:/admin/slide";
	}

	@PostMapping("/update/{id}")
	public String updateSlide(@PathVariable("id") Integer id, @Valid @ModelAttribute("slide") Slide slide,
			BindingResult br, @RequestParam("slide_image") MultipartFile multipartFile, Model m, RedirectAttributes ra)
			throws IOException {

		if (br.hasErrors()) {
			ra.addFlashAttribute("slide", slide);
			ra.addFlashAttribute("org.springframework.validation.BindingResult.slide", br);
			ra.addFlashAttribute("error", "Cập nhật slide thất bại. Vui lòng kiểm tra lại");

		} else {

			Optional<Slide> opSlide = sRepository.findById(id);
			if (opSlide.isPresent()) {
				Slide updateSlide = opSlide.get();

				updateSlide.setTitle(slide.getTitle());

				// Kiểm tra trường sort là duy nhất
				Optional<Slide> opCheckSlide = sRepository.findBySort(slide.getSort());
				if (opCheckSlide.isPresent() && opCheckSlide.get().getId() != id) {

					FieldError sortError = new FieldError("slide", "sort", "Thứ tự hiện thị bị trùng");
					br.addError(sortError);
					ra.addFlashAttribute("slide", updateSlide);
					ra.addFlashAttribute("org.springframework.validation.BindingResult.slide", br);
					ra.addFlashAttribute("error", "Cập nhật slide thất bại. Vui lòng kiểm tra lại");
					return "redirect:/admin/slide/detail/" + id;
				} else {

					updateSlide.setSort(slide.getSort());
				}

				// Cập nhật lại path image cho slide
				// =========================================================================================
				if (!multipartFile.isEmpty()) {

					System.out.println(
							SlideAdminController.class.getSimpleName() + ": " + multipartFile.getContentType());

					// Kiểm tra file upload lên có đúng định dạng không
					String contentType = multipartFile.getContentType();
					if (!contentType.matches("^image/.+")) {

						FieldError imageError = new FieldError("slide", "image",
								"Hình ảnh không đúng định dạng. Ảnh phải có định dạnh (*.jpg, *.jpge, *.png)");
						br.addError(imageError);
						ra.addFlashAttribute("slide", updateSlide);
						ra.addFlashAttribute("org.springframework.validation.BindingResult.slide", br);
						ra.addFlashAttribute("error", "Cập nhật slide thất bại. Vui lòng kiểm tra lại");
						return "redirect:/admin/slide/detail/" + id;
					}

					// Xóa ảnh cũ của slide
					deleteImageDir(id);

					String fImageSlideName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					String uploadDir = SlideHelper.ROOT_PATH_IMAGE_MEDIUM + Helper.FILE_SEPARTOR + updateSlide.getId();
					SlideHelper.saveImage(multipartFile, uploadDir, fImageSlideName);
					updateSlide.setImage(uploadDir + Helper.FILE_SEPARTOR + fImageSlideName);
				}
				// =========================================================================================

				updateSlide.setDescription(slide.getDescription());
				updateSlide.setPub(slide.getPub());

				sRepository.save(updateSlide);
				ra.addFlashAttribute("success", "Slide đã được cập nhật thành công");

			} else {
				ra.addFlashAttribute("error", "Slide không tồn tại hoặc đã bị xóa. Vui lòng kiểm tra lại");
				return "redirect:/admin/slide";
			}

		}

		//
		return "redirect:/admin/slide/detail/" + id;
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

		return "redirect:/admin/slide";
	}

	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class,
			SQLIntegrityConstraintViolationException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException(HttpServletRequest req, Exception ex) {

		return "/errors/500";
	}

	/**
	 * @param slideId Xóa thư mục lưu ảnh của slide bị xóa đi
	 */
	private void deleteImageDir(Integer slideId) {
		Path pSlideImage = Paths.get(SlideHelper.ROOT_PATH_IMAGE_MEDIUM + Helper.FILE_SEPARTOR + slideId);

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
