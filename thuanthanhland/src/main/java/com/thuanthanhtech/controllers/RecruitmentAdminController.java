package com.thuanthanhtech.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuanthanhtech.entities.Recruitment;
import com.thuanthanhtech.entities.RecruitmentHelper;
import com.thuanthanhtech.repositories.RecruitmentRepository;

@Controller
@RequestMapping("/admin/recruitment")
public class RecruitmentAdminController {

	@Autowired
	private RecruitmentRepository rRepository;

	@GetMapping
	public String recruitments(Model m) {
		List<Recruitment> recruitments = rRepository.findAll();

		m.addAttribute("active_recruitment", true);
		m.addAttribute("recruitments", recruitments);

		return "/admin-pages/recruitment";
	}

	@GetMapping("/create")
	public String create(Model m) {
		if (!m.containsAttribute("recruitment")) {
			Recruitment recruitment = new Recruitment();
			recruitment.setDeadline(LocalDate.now());
			m.addAttribute("recruitment", recruitment);
		}

		m.addAttribute("actiive_recruitment", true);
		return "/admin-pages/recruitment-create";
	}

	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("recruitment") Recruitment recruitment, BindingResult br,
			RedirectAttributes ra) {
		if (br.hasErrors()) {
			ra.addFlashAttribute("recruitment", recruitment);
			ra.addFlashAttribute("org.springframework.validation.BindingResult.recruitment", br);
			ra.addFlashAttribute("error", "Tạo tin tuyển dụng mới không thành công");

			return "redirect:/admin/recruitment/create";
		}

		rRepository.saveAndFlush(recruitment);
		ra.addFlashAttribute("success", "Tin tuyển dụng đã được tạo thành công");
		return "redirect:/admin/recruitment/create";
	}

	@GetMapping("/detail/{id}")
	public String detail(@PathVariable("id") Integer id, Model m, RedirectAttributes ra) {
		if (!m.containsAttribute("recruitment")) {
			Optional<Recruitment> opRecruitment = rRepository.findById(id);
			if (opRecruitment.isPresent()) {
				Recruitment recruitment = opRecruitment.get();
				m.addAttribute("recruitment", recruitment);

			} else {
				ra.addFlashAttribute("error", "Tin tuyển dụng này không tồn tại hoặc đã bị xóa");
				return "redirect:/admin/recruitment";
			}
		}
		m.addAttribute("active_recruitment", true);
		return "/admin-pages/recruitment-detail";
	}

	@PostMapping("/update/{id}")
	public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("recruitment") Recruitment recruitment, BindingResult br, RedirectAttributes ra) {
		if (br.hasErrors()) {
			ra.addFlashAttribute("recruitment", recruitment);
			ra.addFlashAttribute("org.springframework.validation.BindingResult.recruitment", br);
			ra.addFlashAttribute("error", "Cập nhật tin tuyển dụng thất bại");
			return "redirect:/admin/recruitment/detail/" + id ;
		}
		
		// updating
		
		if (RecruitmentHelper.updateRecruitmentEntity(id, recruitment, rRepository)) {
			ra.addFlashAttribute("success", "Cập nhật tin tuyển dụng thành công");
		} else {
			ra.addFlashAttribute("recruitment", recruitment);
			ra.addFlashAttribute("error", "Cập nhật tin tuyển dụng thất bại");
		}
		
		return "redirect:/admin/recruitment/detail/" + id ;
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes ra) {
		Optional<Recruitment> opRecruitment = rRepository.findById(id);
		if (opRecruitment.isPresent()) {
			rRepository.deleteById(id);
			ra.addFlashAttribute("success", "Xóa tin tuyển dụng thành công");
		} else {
			ra.addFlashAttribute("error", "Tin tuyển dụng không tồn tại hoặc đã bị xóa");
		}
		
		return "redirect:/admin/recruitment";
	}
	
	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "/errors/500";
	}

}
