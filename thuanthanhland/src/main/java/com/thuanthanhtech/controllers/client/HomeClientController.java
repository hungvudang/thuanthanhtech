package com.thuanthanhtech.controllers.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.thuanthanhtech.entities.Slide;
import com.thuanthanhtech.entities.Spotlight;
import com.thuanthanhtech.repositories.SlideRepository;
import com.thuanthanhtech.repositories.SpotlightRepository;

@Controller
public class HomeClientController {

	@Autowired
	private SlideRepository sRepository;

	@Autowired
	private SpotlightRepository spotRepository;

	@GetMapping({ "/trang-chu", "/" })
	public String home(Model m) {
		
		List<Slide> slides = sRepository.findByPub(1, Sort.by(Sort.Direction.ASC, "sort"));

		List<Spotlight> spotlights = spotRepository.findByPub(1, Sort.by(Sort.Direction.ASC, "sort"));

		m.addAttribute("slides", slides);
		m.addAttribute("spotlights", spotlights);
		return "public-pages/index";
	}
}
