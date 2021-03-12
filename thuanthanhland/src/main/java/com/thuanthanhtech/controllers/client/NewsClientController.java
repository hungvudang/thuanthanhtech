package com.thuanthanhtech.controllers.client;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.thuanthanhtech.entities.Category;
import com.thuanthanhtech.entities.Helper;
import com.thuanthanhtech.entities.News;
import com.thuanthanhtech.entities.NewsHelper;
import com.thuanthanhtech.repositories.CategoryRepository;
import com.thuanthanhtech.repositories.NewsPagingAndSortRepository;
import com.thuanthanhtech.repositories.NewsRepository;

@Controller
@RequestMapping("/tin-tuc")
public class NewsClientController {
	@Autowired
	private NewsRepository nRepository;

	@Autowired
	private CategoryRepository cRepository;

	@Autowired
	private NewsPagingAndSortRepository nPagingAndSortRepository;

	@GetMapping("/{cate-slug}")
	public String nextPage(@PathVariable("cate-slug") String cateSlug,
			@RequestParam(name = "pageNumber", required = false) Integer pageNumber, Model m) {

		if (pageNumber == null) {
			return "redirect:/tin-tuc/" + cateSlug + "?pageNumber=1";
		}

//		else

		Optional<Category> opCate = cRepository.findBySlug(cateSlug);
		if (opCate.isPresent()) {

			Category targetCate = opCate.get();
			Pageable pageable = PageRequest.of(pageNumber - 1, 8);
			Page<News> page = nPagingAndSortRepository.findByCategoryAndPub(targetCate, 1, pageable);
			List<News> targetNewses = page.getContent();

			int currentPage = pageNumber;
			int totalPages = page.getTotalPages();
			int numOfElements = page.getNumberOfElements();

			m.addAttribute("targetNewses", targetNewses);
			m.addAttribute("currentPage", currentPage);
			m.addAttribute("totalPages", totalPages);
			m.addAttribute("numOfElements", numOfElements);

			m.addAttribute("cateSlug", cateSlug);
			m.addAttribute("BASE_PATH_NEWS_RESOURCE", NewsHelper.BASE_PATH_NEWS_RESOURCE);
			m.addAttribute("DIR_IMAGE_DETAILS", NewsHelper.DIR_IMAGE_DETAILS);

			// breadcrumb
			List<String> targetBreadCrumbs = new ArrayList<String>();
			targetBreadCrumbs.add(targetCate.getName());
			Helper.getBreadcrumb(targetCate, cRepository, targetBreadCrumbs);

			m.addAttribute("breadcrumbs", targetBreadCrumbs);
			switch (cateSlug) {
			case "bai-viet":
				return "public-pages/blog";

			case "du-an":
				return "public-pages/project";
			}
		}
		return "redirect:/tin-tuc/bai-viet";
	}

	@GetMapping("/{cate-slug}/{news-slug}")
	public String newsDetailBySlug(@PathVariable("cate-slug") String cateSlug,
			@PathVariable("news-slug") String newsSlug, Model m) {
		Optional<News> opNews = nRepository.findBySlug(newsSlug);
		Category cate = cRepository.findBySlug(cateSlug).get();

		if (opNews.isPresent()) {
			News targetNews = opNews.get();

			List<News> topNewses = nPagingAndSortRepository.findTop5ByCategoryAndPub(cate, 1,
					Sort.by(Sort.Direction.DESC, "createdAt"));

			Collections.sort(topNewses, new Comparator<News>() {

				@Override
				public int compare(News o1, News o2) {
					if (o1.getUpdatedAt() == null && o2.getUpdatedAt() == null)
						return 0;

					else if (o1.getUpdatedAt() == null && o2.getUpdatedAt() != null)
						return 1;

					else if (o1.getUpdatedAt() != null && o2.getUpdatedAt() == null)
						return -1;

					else if (o1.getUpdatedAt().isAfter(o2.getUpdatedAt()))
						return -1;

					else if (o1.getUpdatedAt().isBefore(o2.getUpdatedAt()))
						return 1;

					return 0;
				}
			});

			m.addAttribute("targetNews", targetNews);
			
			m.addAttribute("cateSlug", cateSlug);
			m.addAttribute("topNewses", topNewses);

			m.addAttribute("BASE_PATH_NEWS_RESOURCE", NewsHelper.BASE_PATH_NEWS_RESOURCE);
			m.addAttribute("DIR_IMAGE_DETAILS", NewsHelper.DIR_IMAGE_DETAILS);

			// breadcrumb
			List<String> targetBreadCrumbs = new ArrayList<String>();
			targetBreadCrumbs.add(cate.getName());
			Helper.getBreadcrumb(cate, cRepository, targetBreadCrumbs);

			m.addAttribute("breadcrumbs", targetBreadCrumbs);

			switch (cateSlug) {
			case "bai-viet":
				return "public-pages/blog-detail";

			case "du-an":
				return "public-pages/project-detail";
			}
		}

		return "redirect:/tin-tuc/bai-viet";
	}

	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "/errors/500";
	}
}
