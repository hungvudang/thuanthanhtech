package com.thuanthanhtech.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
import com.thuanthanhtech.entities.Item;
import com.thuanthanhtech.repositories.CategoryRepository;

@Controller
@RequestMapping("/admin/category")
public class CategoryAdminController {

	@Autowired
	private CategoryRepository cRepository;

	@GetMapping
	public String category(Model m) {
		List<Category> categories = cRepository.findAll(Sort.by(Sort.Direction.ASC, "parentId"));
		Item<Category> root = new Item<Category>();
		Category cate = new Category();
		cate.setId(0);
		cate.setName("Danh mục gốc");
		
		CategoryHelper.getCategoryTree(categories, root, cate);
		
		Map<Category, String> target = new HashMap<Category, String>();
		mapByNameParentCategory(root, target);
		
		m.addAttribute("categories", target);
		m.addAttribute("active_category", true);
		return "admin-pages/category";
	}

	@GetMapping("/create")
	public String createCategory(Model m) {

		// Tạo danh sách danh mục -> danh mục gốc
		// =================================================
		List<Category> categories = cRepository.findAll();
		Item<Category> root = new Item<Category>();
		Category cate = new Category();
		cate.setId(0);
		cate.setName("Danh mục gốc");
		
		CategoryHelper.getCategoryTree(categories, root, cate);
		
		// ===================================================================================

		if (!m.containsAttribute("category")) {
			Category c = new Category();
			c.setSort(0);
			c.setPub(1);
			c.setHot(0);
			c.setParentId(0);
			m.addAttribute("category", c);
		}

		m.addAttribute("rootCate", root);
		m.addAttribute("active_category", true);
		return "admin-pages/create-category";
	}

	@PostMapping("/save")
	public String saveCategory(@Valid @ModelAttribute("category") Category category, BindingResult br,
			RedirectAttributes ra) {

		if (br.hasErrors()) {

			ra.addFlashAttribute("error", "Tạo danh mục mới thất bại");
			ra.addFlashAttribute("org.springframework.validation.BindingResult.category", br);
			ra.addFlashAttribute("category", category);

			return "redirect:/admin/category/create";
		}

		// Kiểm tra trường sort là duy nhất
		Optional<Category> opCheckCatagory = cRepository.findBySort(category.getSort());
		if (opCheckCatagory.isPresent()) {

			FieldError sortError = new FieldError("slide", "sort", "Thứ tự hiện thị bị trùng");
			br.addError(sortError);

			ra.addFlashAttribute("org.springframework.validation.BindingResult.category", br);
			ra.addFlashAttribute("category", category);
			ra.addFlashAttribute("error", "Tạo danh mục mới thất bại");
			return "redirect:/admin/category/create";
		}

		cRepository.save(category);
		ra.addFlashAttribute("success", "Danh mục mới đã được tạo thành công");
		return "redirect:/admin/category";

	}

	@GetMapping("/detail/{id}")
	public String detailCategory(@PathVariable("id") Integer id, Model m) {

		if (!m.containsAttribute("category")) {
			Optional<Category> opCategory = cRepository.findById(id);
			if (opCategory.isPresent()) {
				Category category = opCategory.get();
				m.addAttribute("category", category);
			}

			// Tạo danh sách danh mục -> danh mục gốc
			// =================================================
			List<Category> categories = cRepository.findAll();
			Item<Category> root = new Item<Category>();
			Category cate = new Category();
			cate.setId(0);
			cate.setName("Danh mục gốc");
			
			CategoryHelper.getCategoryTree(categories, root, cate);
			// ===================================================================================

			m.addAttribute("rootCate", root);
			m.addAttribute("active_category", true);
			
			return "admin-pages/category-detail";
		}
		return "redirect:/admin/category";
	}

	@PostMapping("/update/{id}")
	public String updateCategory(@PathVariable("id") Integer id, @Valid @ModelAttribute("category") Category category,
			BindingResult br, RedirectAttributes ra) {

		if (br.hasErrors()) {

			ra.addFlashAttribute("org.springframework.validation.BindingResult.category", br);
			ra.addFlashAttribute("category", category);

			ra.addFlashAttribute("error", "Cập nhật danh mục thất bại");
			return "redirect:/admin/category/detail/" + id;
		}

		Optional<Category> opCategory = cRepository.findById(id);
		if (opCategory.isPresent()) {
			Category nCategory = opCategory.get();

			nCategory.setId(category.getId());
			nCategory.setName(category.getName());
			nCategory.setParentId(category.getId());
			nCategory.setHot(category.getHot());
			nCategory.setPub(category.getPub());
			nCategory.setSlug(category.getSlug());

			nCategory.setParentId(category.getParentId());

			// Kiểm tra trường sort là duy nhất
			Optional<Category> opCheckCatagory = cRepository.findBySort(category.getSort());
			if (opCheckCatagory.isPresent() && opCheckCatagory.get().getId() != id) {
				
				FieldError sortError = new FieldError("slide", "sort", "Thứ tự hiện thị bị trùng");
				br.addError(sortError);

				ra.addFlashAttribute("org.springframework.validation.BindingResult.category", br);
				ra.addFlashAttribute("category", category);
				ra.addFlashAttribute("error", "Cập nhật danh mục thất bại");
				
				return "redirect:/admin/category/detail/" + id;
			} else {
				nCategory.setSort(category.getSort());
			}

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
			Item<Category> target = new Item<Category>();
			
			CategoryHelper.getCategoryTree(categories, target, opCategory.get());

			if (target.getChilds().size() != 0) {
				ra.addFlashAttribute("error", "Bạn phải xóa tất cả các danh mục con của danh mục này");
				return "redirect:/admin/category";
			}

			cRepository.deleteById(id);
			ra.addFlashAttribute("success", "Danh mục đã được xóa thành công");
		} else {
			ra.addFlashAttribute("error", "Danh mục này không tồn tại hoặc đã bị xóa");
		}
		return "redirect:/admin/category";
	}
	
	private void mapByNameParentCategory(Item<Category> root, Map<Category, String> target) {
		for (Item<Category> cate : root.getChilds()) {
			target.put(cate.getSelf(), root.getSelf().getName());
			if(cate.getChilds().size() != 0) {
				mapByNameParentCategory(cate, target);
			}
		}
	}
	

	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class,
			SQLIntegrityConstraintViolationException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "admin-pages/500";
	}

}
