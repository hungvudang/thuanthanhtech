package com.thuanthanhtech.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
import com.thuanthanhtech.entities.Helper;
import com.thuanthanhtech.entities.Item;
import com.thuanthanhtech.entities.News;
import com.thuanthanhtech.entities.NewsHelper;
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
		m.addAttribute("BASE_PATH_NEWS_RESOURCE", NewsHelper.BASE_PATH_NEWS_RESOURCE);
		return "admin-pages/news";
	}

	@GetMapping("/create")
	public String creatNews(Model m) {

		if (!m.containsAttribute("news")) {
			News news = new News();
			news.setPub(1);
			news.setHot(0);
			news.setThumbnail(Helper.NO_IMAGE_MEDIUM_PNG);
			m.addAttribute("news", news);
		}

		List<Category> categories = cRepository.findAll();

		Item<Category> root = new Item<Category>();
		Category cate = new Category();
		cate.setId(0);
		cate.setName("Danh mục gốc");

		CategoryHelper.getCategoryTree(categories, root, cate);

		m.addAttribute("categories", categories);
		m.addAttribute("rootCate", root);
		m.addAttribute("active_news", true);
		m.addAttribute("BASE_PATH_NEWS_RESOURCE", NewsHelper.BASE_PATH_NEWS_RESOURCE);
		return "admin-pages/news-create";
	}

	@PostMapping("/save")
	public String saveNews(@Valid @ModelAttribute("news") News news, BindingResult br,
			@RequestParam("news_thumbnail") MultipartFile multipartNewsThumbnail,
			@RequestParam(name = "news_images[]", required = false) MultipartFile[] multipartNewsImages,
			RedirectAttributes ra) throws IOException {

		if (br.hasErrors() || multipartNewsThumbnail.isEmpty()) {

			if (multipartNewsThumbnail.isEmpty()) {
				FieldError imageError = new FieldError("news", "thumbnail", "Hình ảnh không được để trống");
				br.addError(imageError);
			}

			ra.addFlashAttribute("error", "Tạo bài viết mới thất bại");
			ra.addFlashAttribute("news", news);
			ra.addFlashAttribute("org.springframework.validation.BindingResult.news", br);
			return "redirect:/admin/news/create";
		}

		Category category = cRepository.findById(news.getCategory().getId()).get();
		news.setCategory(category);

		// Lưu ảnh thumbnail của tin tức
		// ===========================================================================================
		if (multipartNewsThumbnail != null && !multipartNewsThumbnail.isEmpty()) {

			// Kiểm tra file upload lên có đúng định dạng không
			if (validTypeOfNewsImage(multipartNewsThumbnail, "thumbnail", br)) {

				ra.addFlashAttribute("news", news);
				ra.addFlashAttribute("org.springframework.validation.BindingResult.news", br);
				ra.addFlashAttribute("error", "Tạo bài viết mới thất bại");

				return "redirect:/admin/news/create";
			}

			String fThumbnailImageName = StringUtils.cleanPath(multipartNewsThumbnail.getOriginalFilename());
			news.setThumbnail(fThumbnailImageName);
		}
		// ============================================================================================

		if ((NewsHelper.insertNewsEntity(news, multipartNewsThumbnail, multipartNewsImages, nRepository))) {
			ra.addFlashAttribute("success", "Bài viết mới đã được tạo thành công");

		} else {
			ra.addFlashAttribute("news", news);
			ra.addFlashAttribute("error", "Tạo bài viết mới thất bại");
		}

		return "redirect:/admin/news/create";
	}

	@GetMapping("/detail/{id}")
	public String detailNews(@PathVariable("id") Integer id, Model m, RedirectAttributes ra) {
		if (!m.containsAttribute("news")) {
			Optional<News> opNews = nRepository.findById(id);
			if (opNews.isPresent()) {
				m.addAttribute("news", opNews.get());
			} else {
				ra.addFlashAttribute("error", "Bài viết không tồn tại hoặc đã bị xóa");
				return "redirect:/admin/news";
			}

		}

		List<Category> categories = cRepository.findAll();
		Item<Category> root = new Item<Category>();
		Category cate = new Category();
		cate.setId(0);
		cate.setName("Danh mục gốc");

		CategoryHelper.getCategoryTree(categories, root, cate);

		m.addAttribute("rootCate", root);

		m.addAttribute("active_news", true);
		m.addAttribute("BASE_PATH_NEWS_RESOURCE", NewsHelper.BASE_PATH_NEWS_RESOURCE);
		return "admin-pages/news-detail";

	}

	@PostMapping("/update/{id}")
	public String updateNews(@PathVariable("id") Integer id, @Valid @ModelAttribute("news") News news, BindingResult br,
			@RequestParam("news_thumbnail") MultipartFile multipartNewsThumbnail,
			@RequestParam(name = "news_images[]", required = false) MultipartFile[] multipartNewsImages,
			RedirectAttributes ra) throws IOException {
		if (br.hasErrors()) {

			ra.addFlashAttribute("news", news);
			ra.addFlashAttribute("org.springframework.validation.BindingResult.news", br);

			ra.addFlashAttribute("error", "Cập nhật bài viết thất bại");
			return "redirect:/admin/news/detail/" + id;
		}

		// Cập nhật danh mục
		Category category = cRepository.findById(news.getCategory().getId()).get();
		news.setCategory(category);

		// Cập nhật ảnh thumbnail của tin tức lên server
		// =======================================================
		if (multipartNewsThumbnail != null && !multipartNewsThumbnail.isEmpty()) {

			// Kiểm tra file upload lên có đúng định dạng không
			if (validTypeOfNewsImage(multipartNewsThumbnail, "thumbnail", br)) {

				ra.addFlashAttribute("news", news);
				ra.addFlashAttribute("org.springframework.validation.BindingResult.news", br);
				ra.addFlashAttribute("error", "Cập nhật bài viết thất bại");
				return "redirect:/admin/news/detail/" + id;
			}

			String fThumbnailImageName = StringUtils.cleanPath(multipartNewsThumbnail.getOriginalFilename());
			news.setThumbnail(fThumbnailImageName);
		}
		// ============================================================================================

		if (NewsHelper.updateNewsById(id, news, multipartNewsThumbnail, multipartNewsImages, nRepository)) {

			ra.addFlashAttribute("success", "Bài viết đã được cập nhật thành công");
			return "redirect:/admin/news/detail/" + id;

		} else {

			ra.addFlashAttribute("error", "Cập nhật bài viết thất bại");
			return "redirect:/admin/news";
		}

	}

	@GetMapping("/delete/{id}")
	public String deleteNews(@PathVariable("id") Integer id, RedirectAttributes ra) {
		Optional<News> opNews = nRepository.findById(id);
		if (opNews.isPresent()) {
			nRepository.deleteById(id);

			NewsHelper.deleteNewsResourceDir(NewsHelper.BASE_PATH_NEWS_RESOURCE + Helper.FILE_SEPARTOR + id);
			ra.addFlashAttribute("success", "Xóa bài viết thành công");

		} else {
			ra.addFlashAttribute("error", "Xóa bài viết thất bại");
		}
		return "redirect:/admin/news";
	}

	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "/errors/500";
	}

	// Kiểm tra file upload lên có đúng định dạng không
	private boolean validTypeOfNewsImage(MultipartFile multipartFile, String nameFieldError, BindingResult br) {

		String contentType = multipartFile.getContentType();

		if (!contentType.matches("^image/.+")) {
			FieldError error = new FieldError("news", nameFieldError,
					"Hình ảnh không đúng định dạng. Ảnh phải có định dạnh (*.jpg, *.jpge, *.png)");
			br.addError(error);
			return true;
		}

		// else
		return false;
	}
}
