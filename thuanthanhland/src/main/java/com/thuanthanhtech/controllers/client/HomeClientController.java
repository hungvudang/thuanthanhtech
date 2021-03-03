package com.thuanthanhtech.controllers.client;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.thuanthanhtech.entities.Slide;
import com.thuanthanhtech.repositories.SlideRepository;

@Controller
public class HomeClientController {
	
	@Autowired
	private SlideRepository sRepository;
	
	@GetMapping({"/trang-chu", "/"})
	public String home(Model m) {
		
		List<Slide> slides = sRepository.findByPub(1);
		if (slides != null) {
			Collections.sort(slides, new Comparator<Slide>() {

				@Override
				public int compare(Slide o1, Slide o2) {
					if (o1.getSort() > o2.getSort()) return 1;
					else if (o1.getSort() < o2.getSort()) return -1;
					return 0;
				}
			});
		}
		m.addAttribute("slides", slides);
		return "public-pages/index";
	}
}
