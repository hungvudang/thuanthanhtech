package com.thuanthanhtech.controllers.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thuanthanhtech.entities.News;
import com.thuanthanhtech.repositories.NewsRepository;

@Controller
@RequestMapping("/news")
public class NewsClientController {
	@Autowired
	private NewsRepository nRepository;

	@GetMapping
	public String mainPage(Model m) {
		
		List<News> news = nRepository.findByPub(1);
		m.addAttribute("news", news);
		return "public-pages/blog";
	}
}
