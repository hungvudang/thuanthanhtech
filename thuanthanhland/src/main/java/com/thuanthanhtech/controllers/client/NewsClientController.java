package com.thuanthanhtech.controllers.client;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thuanthanhtech.entities.News;
import com.thuanthanhtech.entities.NewsHelper;
import com.thuanthanhtech.repositories.NewsPagingAndSortRepository;
import com.thuanthanhtech.repositories.NewsRepository;

@Controller
@RequestMapping("/tin-tuc")
public class NewsClientController {
	@Autowired
	private NewsRepository nRepository;
	
	@Autowired
	private NewsPagingAndSortRepository nPagingAndSortRepository;

	@GetMapping
	public String news() {
		return "redirect:/tin-tuc/page/1";
	}
	@GetMapping("/page/{index}")
	public String nextPage(@PathVariable("index") Integer index, Model m) {
		Pageable pageable = PageRequest.of(index - 1, 8);
		Page<News> page = nPagingAndSortRepository.findByPub(1, pageable);
		
		List<News> news = page.getContent();
		int currentPage = index;
		int totalPages = page.getTotalPages();
		int numOfElements = page.getNumberOfElements();
		
		m.addAttribute("news", news);
		m.addAttribute("currentPage", currentPage);
		m.addAttribute("totalPages", totalPages);
		m.addAttribute("numOfElements", numOfElements);
		
		m.addAttribute("BASE_PATH_NEWS_RESOURCE", NewsHelper.BASE_PATH_NEWS_RESOURCE);
		m.addAttribute("DIR_IMAGE_DETAILS", NewsHelper.DIR_IMAGE_DETAILS);
		
		return "public-pages/blog";
	}
	
	@GetMapping("/detail/{slug}")
	public String newsDetailBySlug(@PathVariable("slug") String slug, Model m) {
		Optional<News> opNews = nRepository.findBySlug(slug);
		if (opNews.isPresent()) {
			News news = opNews.get();
			m.addAttribute("news", news);
			
			m.addAttribute("BASE_PATH_NEWS_RESOURCE", NewsHelper.BASE_PATH_NEWS_RESOURCE);
			m.addAttribute("DIR_IMAGE_DETAILS", NewsHelper.DIR_IMAGE_DETAILS);
			
			return "public-pages/blog-detail";
		}
		
		return "redirect:/tin-tuc";
	}
}
