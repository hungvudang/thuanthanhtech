package com.thuanthanhtech.controllers.client;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuanthanhtech.entities.Slide;
import com.thuanthanhtech.repositories.SlideRepository;

@RestController
@RequestMapping("/api/slide")
@CrossOrigin("*")
public class SlideClientController {

	@Autowired
	private SlideRepository sRepository;
	
	@GetMapping
	public ResponseEntity<?> findAllSlide(){
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
		
		return ResponseEntity.ok().body(slides);
	}
}
