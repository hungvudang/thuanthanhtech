package com.thuanthanhtech.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.thuanthanhtech.entities.Category;
import com.thuanthanhtech.entities.CategoryHelper;
import com.thuanthanhtech.entities.News;
import com.thuanthanhtech.entities.NewsHelper;
import com.thuanthanhtech.entities.RootCategory;
import com.thuanthanhtech.entities.SlugConverter;
import com.thuanthanhtech.repositories.CategoryRepository;
import com.thuanthanhtech.repositories.NewsRepository;

@Controller
@RequestMapping("/news")
public class NewsAdminController {

	@Autowired
	private NewsRepository nRepository;
	@Autowired
	private CategoryRepository cRepository;

	@GetMapping
	public String news(Model m) {
		List<News> newses = nRepository.findAll();
		m.addAttribute("newses", newses);
		m.addAttribute("active_news", true);
		return "admin-pages/news";
	}

	@GetMapping("/create")
	public String creatNews(Model m) {
		News news = new News();
		news.setPub(1);
		news.setHot(0);
		news.setImage(NewsHelper.NO_THUMBNAIL_MEDIUM_IMAGE);

		List<Category> categories = cRepository.findAll();

		List<RootCategory> root = new ArrayList<RootCategory>();
		List<Boolean> visited = new ArrayList<Boolean>();

		for (int i = 0; i < categories.size(); i++) {
			visited.add(false);
		}
		CategoryHelper.recursive_categories(categories, visited, 0, "", root);

		m.addAttribute("news", news);
		m.addAttribute("categories", categories);
		m.addAttribute("root_categories", root);
		m.addAttribute("active_news", true);
		return "admin-pages/create-news";
	}

	@PostMapping("/save")
	public String saveNews(@ModelAttribute("news") News news,
			@RequestParam("news_thumbnail") MultipartFile fNewsThumbnail) throws IOException {

		if (news.getName().isEmpty() || news.getTitle().isEmpty() || news.getContent().isEmpty()
				|| news.getDescription().isEmpty() || news.getCategory() == null) {

			return "redirect:/news/create";
		}

		Optional<Category> opCategory = cRepository.findById(news.getCategory().getId());
		if (opCategory.isPresent()) {

			news.setCategory(opCategory.get());

			String slug = SlugConverter.convert(news.getName());
			news.setSlug(slug);

			// Upload ảnh thumbnail của tin tức lên server
			// ===========================================================================================
			if (fNewsThumbnail != null && !fNewsThumbnail.isEmpty()) {
				String fThumbnailImageName = StringUtils.cleanPath(fNewsThumbnail.getOriginalFilename());
				String uploadDir = NewsHelper.ROOT_PATH_THUMBNAIL_MEDIUM + File.separator + news.getSlug();

				NewsHelper.saveThumbnailImage(fNewsThumbnail, uploadDir, fThumbnailImageName);
				news.setImage(uploadDir + File.separator + fThumbnailImageName);

			}
			// ============================================================================================
			nRepository.save(news);
		}

		return "redirect:/news";
	}

	@GetMapping("/detail/{id}")
	public String detailNews(@PathVariable("id") Integer id, Model m) {

		m.addAttribute("active_news", true);

		Optional<News> opNews = nRepository.findById(id);
		if (opNews.isPresent()) {
			List<Category> categories = cRepository.findAll();

			List<RootCategory> root = new ArrayList<RootCategory>();
			List<Boolean> visited = new ArrayList<Boolean>();

			for (int i = 0; i < categories.size(); i++) {
				visited.add(false);
			}
			CategoryHelper.recursive_categories(categories, visited, 0, "", root);

			m.addAttribute("categories", categories);
			m.addAttribute("root_categories", root);
			m.addAttribute("news", opNews.get());

			return "admin-pages/news-detail";
		}
		return "redirect:/news";
	}

	@PostMapping("/update/{id}")
	public String updateNews(@PathVariable("id") Integer id, @ModelAttribute("news") News news,
			@RequestParam("news_thumbnail") MultipartFile fNewsThumbnail) throws IOException {

		if (news.getTitle().isEmpty() || news.getContent().isEmpty() || news.getCategory() == null) {
			return "redirect:/news/detail/" + id;

		}

		Optional<Category> opCategory = cRepository.findById(news.getCategory().getId());

		if (opCategory.isPresent()) {
			News nNews = nRepository.findById(id).get();

			nNews.setId(news.getId());
			nNews.setCategory(opCategory.get());
			nNews.setContent(news.getContent());
			nNews.setDescription(news.getDescription());
			nNews.setHot(news.getHot());
			nNews.setName(news.getName());
			nNews.setPub(news.getPub());
			nNews.setTitle(news.getTitle());

			String slug = SlugConverter.convert(nNews.getName());
			nNews.setSlug(slug);

			// Đổi tên thư mục lưu ảnh thumbnail của tin tức khi cập nhập slug
			// =========================================================================================================
			if (news.getImage() != null || !news.getImage().isEmpty()) {
				Path oldThumbnailPathDir = Path
						.of(NewsHelper.ROOT_PATH_THUMBNAIL_MEDIUM + File.separator + news.getSlug());
				Path newThumbnailPathDir = Path
						.of(NewsHelper.ROOT_PATH_THUMBNAIL_MEDIUM + File.separator + nNews.getSlug());
				Files.move(oldThumbnailPathDir, newThumbnailPathDir, StandardCopyOption.REPLACE_EXISTING);
			}
			// ==========================================================================================================

			// Cập nhật ảnh thumbnail của tin tức lên server
			// =======================================================
			if (fNewsThumbnail != null && !fNewsThumbnail.isEmpty()) {
				String fThumbnailImageName = StringUtils.cleanPath(fNewsThumbnail.getOriginalFilename());
				String uploadDir = NewsHelper.ROOT_PATH_THUMBNAIL_MEDIUM + File.separator + nNews.getSlug();

				NewsHelper.saveThumbnailImage(fNewsThumbnail, uploadDir, fThumbnailImageName);
				nNews.setImage(uploadDir + File.separator + fThumbnailImageName);
			}
			// ============================================================================================

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

	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "admin-pages/500";
	}
}
