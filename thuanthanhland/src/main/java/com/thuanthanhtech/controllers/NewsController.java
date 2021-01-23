package com.thuanthanhtech.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thuanthanhtech.entities.Category;
import com.thuanthanhtech.entities.News;
import com.thuanthanhtech.repositories.CategoryRepository;
import com.thuanthanhtech.repositories.NewsRepository;

@Controller
@RequestMapping("/news")
public class NewsController {

	@Autowired
	private NewsRepository nRepository;
	@Autowired
	private CategoryRepository cRepository;
	
	@GetMapping
	public String news(Model m) {
		List<News> newses = nRepository.findAll();
		m.addAttribute("newses", newses);
		return "admin-pages/news";
	}
	
	@GetMapping("/create")
	public String creatNews(Model m) {
		News news = new News();
		List<Category> categories = cRepository.findAll();
		m.addAttribute("news", news);
		m.addAttribute("categories", categories);
		return "admin-pages/create-news";
	}
	
	@PostMapping("/save")
	public String saveNews(@ModelAttribute("news") News news,
			@RequestParam(name = "typeCategory") int category_id, 
			@RequestParam(name = "gridRadiosPublic") int _public,
			@RequestParam(name = "gridRadiosHot") int hot) {
		
		news.set_public(_public);
		news.setHot(hot);
		Category category = cRepository.findById(category_id).get();
		news.setCategory(category);
		category.setNewses(Collections.singleton(news));
		
		if (news.getName().isEmpty() || news.getTitle().isEmpty() || news.getContent().isEmpty() || news.getDescription().isEmpty()) {
			return "redirect:/news";
		}
		nRepository.saveAndFlush(news);
		return "redirect:/news";
	}
	
	@GetMapping("/detail/{id}")
	public String detailNews(@PathVariable("id") Integer id, Model m) {
		Optional<News> opNews = nRepository.findById(id);
		if (opNews.isPresent()) {
			m.addAttribute("news", opNews);
			return "admin-pages/news-detail";
		}
		 return "redirect:/news";
	}
	
	@PostMapping("/update")
	public String updateNews() {
		//update
		System.out.println("Update news");
		return "redirect:/news";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteNews(@PathVariable("id") Integer id) {
		Optional<News> opNews = nRepository.findById(id);
		if (opNews.isPresent()) {
			nRepository.deleteById(id);
		}
		
		return "redirect:/news";
	}
}
