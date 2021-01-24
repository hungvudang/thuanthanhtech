package com.thuanthanhtech.controllers;

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
		news.setPub(0);
		news.setHot(0);
		List<Category> categories = cRepository.findAll();
		m.addAttribute("news", news);
		m.addAttribute("categories", categories);
		return "admin-pages/create-news";
	}

	@PostMapping("/save")
	public String saveNews(@ModelAttribute("news") News news) {

		if (news.getName().isEmpty() || news.getTitle().isEmpty() || news.getContent().isEmpty()
				|| news.getDescription().isEmpty() || news.getCategory() == null) {

			return "redirect:/news";
		}
		
		Optional<Category> opCategory = cRepository.findById(news.getCategory().getId());
		if (opCategory.isPresent()) {
			
			news.setCategory(opCategory.get());
			
			nRepository.save(news);
		}

		return "redirect:/news";
	}

	@GetMapping("/detail/{id}")
	public String detailNews(@PathVariable("id") Integer id, Model m) {
		Optional<News> opNews = nRepository.findById(id);
		if (opNews.isPresent()) {
			List<Category> categories = cRepository.findAll();
			m.addAttribute("categories", categories);
			m.addAttribute("news", opNews);
			return "admin-pages/news-detail";
		}
		return "redirect:/news";
	}

	@PostMapping("/update")
	public String updateNews(@ModelAttribute("news") News news) {
		
		

		if (news.getTitle().isEmpty() || news.getContent().isEmpty() || news.getCategory() == null) {
			return "rediect:/news/detail/" + news.getId();
		}
		
		Optional<Category> opCategory = cRepository.findById(news.getCategory().getId());
		
		if (opCategory.isPresent()) {
			News nNews = nRepository.findById(news.getId()).get();
			
			nNews.setId(news.getId());
			nNews.setCategory(opCategory.get());
			nNews.setContent(news.getContent());
			nNews.setDescription(news.getDescription());
			nNews.setHot(news.getHot());
			nNews.setName(news.getName());
			nNews.setPub(news.getPub());
			nNews.setSlug(news.getSlug());
			nNews.setTitle(news.getTitle());
			nRepository.save(nNews);
		}
		
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
