package com.thuanthanhtech.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuanthanhtech.entities.Category;
import com.thuanthanhtech.entities.CategoryHelper;
import com.thuanthanhtech.entities.RootCategory;
import com.thuanthanhtech.entities.SlugConverter;
import com.thuanthanhtech.repositories.CategoryRepository;

@Controller
@RequestMapping("/admin/category")
public class CategoryAdminController {

	@Autowired
	private CategoryRepository cRepository;

	@GetMapping
	public String category(Model m) {
		List<Category> categories = cRepository.findAll();

		Map<Category, String> hm = new HashMap<Category, String>();
		categories.parallelStream().forEach((cate) -> {
			if (cate.getParent_id() == 0) {
				hm.put(cate, "Danh mục gốc");
			} else {
				Category rootCategory = cRepository.findById(cate.getParent_id()).get();
				hm.put(cate, rootCategory.getName());
			}
		});

//		m.addAttribute("categories", categories);
		m.addAttribute("categoriesEntryMap", hm.entrySet());
		m.addAttribute("active_category", true);
		return "admin-pages/category";
	}

	@GetMapping("/create")
	public String createCategory(Model m) {

		Category c = new Category();
		c.setSort(0);
		c.setPub(1);
		c.setHot(0);
		c.setParent_id(0);

		// Tạo danh sách danh mục -> danh mục gốc
		// =================================================
		List<Category> categories = cRepository.findAll();
		List<Boolean> visited = null;

		List<RootCategory> root = new ArrayList<RootCategory>();
		root.add(new RootCategory(0, "--- Danh mục gốc ---"));

		if (!categories.isEmpty()) {
			visited = new ArrayList<Boolean>();
			for (int i = 0; i < categories.size(); i++) {
				visited.add(false);
			}
			CategoryHelper.recursive_categories(categories, visited, 0, "", root);
		}
		// ===================================================================================

		m.addAttribute("root_categories", root);
		m.addAttribute("category", c);
		m.addAttribute("active_category", true);
		return "admin-pages/create-category";
	}

	@PostMapping("/save")
	public String saveCategory(@Valid @ModelAttribute("category") Category category, BindingResult br,
			RedirectAttributes ra) {

		if (br.hasErrors()) {

			if (br.hasFieldErrors("name")) {
				ra.addFlashAttribute("isNameError", true);
				ra.addFlashAttribute("nameErrorMessage", br.getFieldError("name").getDefaultMessage());
			}

			if (br.hasFieldErrors("sort")) {
				ra.addFlashAttribute("isSortError", true);
				ra.addFlashAttribute("sortErrorMessage", br.getFieldError("sort").getDefaultMessage());
			}

			ra.addFlashAttribute("error", "Tạo danh mục mới thất bại");
			ra.addFlashAttribute("category", category);

			return "redirect:/admin/category/create";
		}

		// Kiểm tra trường sort là duy nhất
		Optional<Category> opCheckCatagory = cRepository.findBySort(category.getSort());
		if (opCheckCatagory.isPresent()) {
			ra.addFlashAttribute("isSortError", true);
			ra.addFlashAttribute("sortErrorMessage", "Thứ tự hiện thị bị trùng");

			ra.addFlashAttribute("slide", category);
			ra.addFlashAttribute("error", "Tạo danh mục mới thất bại");
			return "redirect:/admin/category/create";
		}

		// Tạo slug dựa theo tên danh mục
		String slug = SlugConverter.convert(category.getName());
		category.setSlug(slug);
		// ==============================

		cRepository.save(category);
		ra.addFlashAttribute("success", "Danh mục mới đã được tạo thành công");
		return "redirect:/admin/category";

	}

	@GetMapping("/detail/{id}")
	public String detailCategory(@PathVariable("id") Integer id, Model m) {
		m.addAttribute("active_category", true);

		Optional<Category> opCategory = cRepository.findById(id);
		if (opCategory.isPresent()) {
			Category category = opCategory.get();

			// Tạo danh sách danh mục -> danh mục gốc
			// =================================================
			List<Category> categories = cRepository.findAll();
			List<Boolean> visited = null;

			List<RootCategory> root = new ArrayList<RootCategory>();
			root.add(new RootCategory(0, "--- Danh mục gốc ---"));

			if (!categories.isEmpty()) {
				visited = new ArrayList<Boolean>();
				for (int i = 0; i < categories.size(); i++) {
					visited.add(false);
				}
				CategoryHelper.recursive_categories(categories, visited, 0, "", root);
			}
			// ===================================================================================

			// Loại bỏ các danh mục là danh mục con của danh mục đang sửa
			// ====================================================================
			List<RootCategory> childsTargetCategory = new ArrayList<RootCategory>();
			visited = new ArrayList<Boolean>();
			for (int i = 0; i < categories.size(); i++) {
				visited.add(false);
			}
			CategoryHelper.recursive_categories(categories, visited, id, "", childsTargetCategory);
			root.removeAll(childsTargetCategory);
			// =====================================================================================

			m.addAttribute("root_categories", root);
			m.addAttribute("category", category);
			return "admin-pages/category-detail";
		}
		return "redirect:/admin/category";
	}

	@PostMapping("/update/{id}")
	public String updateCategory(@PathVariable("id") Integer id, @Valid @ModelAttribute("category") Category category,
			BindingResult br, RedirectAttributes ra) {

		if (br.hasErrors()) {

			if (br.hasFieldErrors("name")) {
				ra.addFlashAttribute("isNameError", true);
				ra.addFlashAttribute("nameErrorMessage", br.getFieldError("name").getDefaultMessage());
			}

			if (br.hasFieldErrors("sort")) {
				ra.addFlashAttribute("isSortError", true);
				ra.addFlashAttribute("sortErrorMessage", br.getFieldError("sort").getDefaultMessage());
			}

			ra.addFlashAttribute("error", "Cập nhật danh mục thất bại");
			return "redirect:/admin/category/detail/" + id;
		}

		Optional<Category> opCategory = cRepository.findById(id);
		if (opCategory.isPresent()) {
			Category nCategory = opCategory.get();

			nCategory.setId(category.getId());
			nCategory.setName(category.getName());
			nCategory.setParent_id(category.getId());
			nCategory.setHot(category.getHot());
			nCategory.setPub(category.getPub());

			nCategory.setParent_id(category.getParent_id());

			// Kiểm tra trường sort là duy nhất
			Optional<Category> opCheckCatagory = cRepository.findBySort(category.getSort());
			if (opCheckCatagory.isPresent() && opCheckCatagory.get().getId() != id) {
				ra.addFlashAttribute("isSortError", true);
				ra.addFlashAttribute("sortErrorMessage", "Thứ tự hiện thị bị trùng");

				ra.addFlashAttribute("error", "Cập nhật danh mục thất bại");
				return "redirect:/admin/category/detail/" + id;
			} else {
				nCategory.setSort(category.getSort());
			}

			// Tạo slug dựa theo tên danh mục
			// ====================================================
			String slug = SlugConverter.convert(category.getName());
			category.setSlug(slug);
			// =====================================================

			cRepository.save(nCategory);
			ra.addFlashAttribute("success", "Danh mục đã được cập nhật thành công");
			return "redirect:/admin/category/detail/" + nCategory.getId();
		} else {

			ra.addFlashAttribute("error", "Danh mục này không tồn tại hoặc đã bị xóa");
			return "redirect:/admin/category";
		}

	}

	/**
	 * @param id
	 * @return
	 * 
	 *         Nếu xóa một danh mục thì tất cả các danh mục con của nó cũng bị xóa
	 *         theo.
	 */
	@GetMapping("/delete/{id}")
	public String deleteCategory(@PathVariable("id") Integer id, RedirectAttributes ra) {
		Optional<Category> opCategory = cRepository.findById(id);
		if (opCategory.isPresent()) {

			if (!opCategory.get().getNewses().isEmpty()) {
				ra.addFlashAttribute("error", "Bạn phải xóa tất cả các bài viết thuộc danh mục này");
				return "redirect:/admin/category";
			}

			List<Category> categories = cRepository.findAll();
			List<RootCategory> childsTargetCategory = new ArrayList<RootCategory>();
			List<Boolean> visited = new ArrayList<Boolean>();

			categories.parallelStream().forEach((obj) -> {
				visited.add(false);
			});

			CategoryHelper.recursive_categories(categories, visited, id, "", childsTargetCategory);

			if (!childsTargetCategory.isEmpty()) {
				ra.addFlashAttribute("error", "Bạn phải xóa tất cả các danh mục con của danh mục này");
				return "redirect:/admin/category";
			}

			childsTargetCategory.parallelStream().forEach((child) -> {
				cRepository.deleteById(child.getId());
			});
			cRepository.deleteById(id);
			ra.addFlashAttribute("success", "Danh mục đã được xóa thành công");
		} else {
			ra.addFlashAttribute("error", "Danh mục này không tồn tại hoặc đã bị xóa");
		}
		return "redirect:/admin/category";
	}

	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class,
			SQLIntegrityConstraintViolationException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "admin-pages/500";
	}

}
