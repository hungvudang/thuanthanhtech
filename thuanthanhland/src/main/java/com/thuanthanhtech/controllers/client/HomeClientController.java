package com.thuanthanhtech.controllers.client;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
		
		List<Spotlight> spotlights = spotRepository.findByPub(1);
		
		if(spotlights != null) {
			Collections.sort(spotlights, (o1, o2)->{
				Spotlight s1 = (Spotlight) o1;
				Spotlight s2 = (Spotlight) o2;
				
				if(s1.getSort() > s2.getSort()) {
					return 1;
				} else if (s1.getSort() < s2.getSort()) {
					return -1;
				} else {
					return 0;
				}
			});
		}
		m.addAttribute("slides", slides);
		m.addAttribute("spotlights", spotlights);
		return "public-pages/index";
	}
}
