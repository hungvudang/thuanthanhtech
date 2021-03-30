package com.thuanthanhtech.controllers.client.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
public class SlideClientRestController {

	@Autowired
	private SlideRepository sRepository;
	
	@GetMapping
	public ResponseEntity<?> findAllSlide(){
		
		List<Slide> slides = sRepository.findByPub(1, Sort.by(Sort.Direction.ASC, "sort"));
		return ResponseEntity.ok().body(slides);
	}
}
