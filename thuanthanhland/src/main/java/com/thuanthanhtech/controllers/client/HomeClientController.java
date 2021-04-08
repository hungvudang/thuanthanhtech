package com.thuanthanhtech.controllers.client;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thuanthanhtech.controllers.client.helper.CategoryClientHelper;
import com.thuanthanhtech.controllers.client.utils.Filterable;
import com.thuanthanhtech.entities.Slide;
import com.thuanthanhtech.entities.Spotlight;
import com.thuanthanhtech.repositories.SlideRepository;
import com.thuanthanhtech.repositories.SpotlightRepository;

@Controller
@RequestMapping({ "/trang-chu", "/" })
public class HomeClientController implements Filterable{

	@Autowired
	private SlideRepository sRepository;

	@Autowired
	private SpotlightRepository spotRepository;

	@Autowired
	private CategoryClientHelper cateClientHelper;

	@GetMapping
	public String home(Model m, HttpServletRequest request) {
		
		if (request.getRequestURI().equals("/")) {
			return "redirect:/trang-chu";
		}
		
		List<Slide> slides = sRepository.findByPub(1, Sort.by(Sort.Direction.ASC, "sort"));

		List<Spotlight> spotlights = spotRepository.findByPub(1, Sort.by(Sort.Direction.ASC, "sort"));

		cateClientHelper.categories(m);

		m.addAttribute("slides", slides);
		m.addAttribute("spotlights", spotlights);
		return "public-pages/index";
	}
}
