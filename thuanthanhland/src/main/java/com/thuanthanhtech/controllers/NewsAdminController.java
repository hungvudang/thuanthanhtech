package com.thuanthanhtech.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuanthanhtech.entities.Category;
import com.thuanthanhtech.entities.CategoryHelper;
import com.thuanthanhtech.entities.News;
import com.thuanthanhtech.entities.NewsHelper;
import com.thuanthanhtech.entities.RootCategory;
import com.thuanthanhtech.entities.SlugConverter;
import com.thuanthanhtech.repositories.CategoryRepository;
import com.thuanthanhtech.repositories.NewsRepository;

@Controller
@RequestMapping("/admin/news")
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
	public String saveNews(@Valid @ModelAttribute("news") News news, BindingResult br,
			@RequestParam("news_thumbnail") MultipartFile multipartFile, RedirectAttributes ra) throws IOException {

		if (br.hasErrors()) {
			if (br.hasFieldErrors("name")) {
				ra.addFlashAttribute("isNameError", true);
				ra.addFlashAttribute("nameErrorMessage", br.getFieldError("name").getDefaultMessage());
			}

			if (br.hasFieldErrors("title")) {
				ra.addFlashAttribute("isTitleError", true);
				ra.addFlashAttribute("titleErrorMessage", br.getFieldError("title").getDefaultMessage());
			}
			
			ra.addFlashAttribute("error", "Tạo bài viết mới thất bại");
			ra.addFlashAttribute("news", news);
			return "redirect:/admin/news/create";
		}
		
		
		Optional<Category> opCategory = cRepository.findById(news.getCategory().getId());
		if (opCategory.isPresent()) {

			news.setCategory(opCategory.get());

			String slug = SlugConverter.convert(news.getName());
			news.setSlug(slug);

			// Upload ảnh thumbnail của tin tức lên server
			// ===========================================================================================
			if (multipartFile != null && !multipartFile.isEmpty()) {
				String fThumbnailImageName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				String uploadDir = StringUtils.cleanPath(NewsHelper.ROOT_PATH_THUMBNAIL_MEDIUM + File.separator + news.getSlug());

				NewsHelper.saveThumbnailImage(multipartFile, uploadDir, fThumbnailImageName);
				news.setImage(StringUtils.cleanPath(uploadDir + File.separator + fThumbnailImageName));

			}
			// ============================================================================================
			ra.addFlashAttribute("success", "Bài viết mới đã được tạo thành công");
			nRepository.save(news);
		}

		return "redirect:/admin/news";
	}

	@GetMapping("/detail/{id}")
	public String detailNews(@PathVariable("id") Integer id, Model m, RedirectAttributes ra) {

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
		ra.addFlashAttribute("error", "Bài viết không tồn tại hoặc đã bị xóa");
		return "redirect:/admin/news";
	}

	@PostMapping("/update/{id}")
	public String updateNews(@PathVariable("id") Integer id,@Valid @ModelAttribute("news") News news, BindingResult br,
			@RequestParam("news_thumbnail") MultipartFile multipartFile, RedirectAttributes ra) throws IOException {

		if (br.hasErrors()) {
			
			if (br.hasFieldErrors("name")) {
				ra.addFlashAttribute("isNameError", true);
				ra.addFlashAttribute("nameErrorMessage", br.getFieldError("name").getDefaultMessage());
			}

			if (br.hasFieldErrors("title")) {
				ra.addFlashAttribute("isTitleError", true);
				ra.addFlashAttribute("titleErrorMessage", br.getFieldError("title").getDefaultMessage());
			}
			ra.addFlashAttribute("error", "Cập nhật bài viết thất bại");
			return "redirect:/admin/news/detail/" + id;
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
			String oldSlug = nNews.getSlug(); 
			String slug = SlugConverter.convert(nNews.getName());
			nNews.setSlug(slug);

			// Đổi tên thư mục lưu ảnh thumbnail của tin tức khi cập nhập slug
			// =========================================================================================================
			if (nNews.getImage() != null) {
				Path oldThumbnailPathDir = Path
						.of(StringUtils.cleanPath(NewsHelper.ROOT_PATH_THUMBNAIL_MEDIUM + File.separator + oldSlug));
				Path newThumbnailPathDir = Path
						.of(StringUtils.cleanPath(NewsHelper.ROOT_PATH_THUMBNAIL_MEDIUM + File.separator + nNews.getSlug()));
				
				Files.move(oldThumbnailPathDir.normalize(), newThumbnailPathDir.normalize(), StandardCopyOption.REPLACE_EXISTING);
			}
			// ==========================================================================================================

			// Cập nhật ảnh thumbnail của tin tức lên server
			// =======================================================
			if (multipartFile != null && !multipartFile.isEmpty()) {
				String fThumbnailImageName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				String uploadDir = NewsHelper.ROOT_PATH_THUMBNAIL_MEDIUM + File.separator + nNews.getSlug();

				NewsHelper.saveThumbnailImage(multipartFile, uploadDir, fThumbnailImageName);
				nNews.setImage(uploadDir + File.separator + fThumbnailImageName);
			}
			// ============================================================================================

			ra.addFlashAttribute("success", "Bài viết đã được cập nhật thành công");
			nRepository.save(nNews);
			return "redirect:/admin/news/detail/" + nNews.getId();
		} else {

			ra.addFlashAttribute("error", "Bài viết không tồn tại hoặc đã bị xóa");
			return "redirect:/admin/news";
		}

	}

	@GetMapping("/delete/{id}")
	public String deleteNews(@PathVariable("id") Integer id, RedirectAttributes ra) {
		Optional<News> opNews = nRepository.findById(id);
		if (opNews.isPresent()) {
			nRepository.deleteById(id);
			
			if (opNews.get().getImage() != null) {
				deleteThumbnailImageDir(opNews.get().getImage());
			}
			ra.addFlashAttribute("success", "Xóa bài viết thành công");
			
		} else {
			ra.addFlashAttribute("error", "Xóa bài viết thất bại");
		}
		return "redirect:/admin/news";
	}

	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "admin-pages/500";
	}

	
	
	/**
	 * @param fThumbnailImagePath
	 * Xóa folder lưu ảnh thumbnail của bài viết bị xóa.
	 */
	private void deleteThumbnailImageDir(String fThumbnailImagePath) {
		
		Path path = Paths.get(fThumbnailImagePath);
		File fParentDirThumbnailImage = path.toAbsolutePath().toFile().getParentFile();
		// xóa thư mục
		if (fParentDirThumbnailImage.isDirectory()) {
			String[] files = fParentDirThumbnailImage.list();
			for (String childs : files) {
				File childDirt = new File(fParentDirThumbnailImage, childs);
				childDirt.delete();
			}
		}
		fParentDirThumbnailImage.delete();
	}
}
