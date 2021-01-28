package com.thuanthanhtech.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuanthanhtech.entities.NewsHelper;
import com.thuanthanhtech.entities.User;
import com.thuanthanhtech.entities.UserHelper;
import com.thuanthanhtech.repositories.UserRepository;

@Controller
@RequestMapping("/user")
public class UserAdminController {

	@Autowired
	private UserRepository uRepository;

	@GetMapping
	public String user(Model m) {
		List<User> users = uRepository.findAll();
		m.addAttribute("users", users);
		m.addAttribute("active_user", true);

		return "/admin-pages/user";
	}

	@GetMapping("/create")
	public String createUser(Model m) {
		User user = new User();
		user.setRole(0);
		user.setAvatar(NewsHelper.NO_THUMBNAIL_MEDIUM_IMAGE);
		m.addAttribute("user", user);
		m.addAttribute("active_user", true);

		return "admin-pages/create-user";
	}

	@PostMapping("/save")
	public String saveUser(@ModelAttribute("user") User user, @RequestParam("user_avatar") MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException {
		if (user.getName() == null || user.getName().isBlank() || user.getEmail() == null || user.getEmail().isBlank()
				|| user.getPhone() == null || user.getPhone().isBlank() || user.getPassword() == null
				|| user.getPassword().isBlank()) {

			ra.addFlashAttribute("error", "Tạo tài khoản mới không thành công.");
			ra.addAttribute("user", user);

			return "redirect:/user/create";
		}

		ra.addFlashAttribute("success", "Tài khoản mới đã được tạo thành công.");
		User savedUser = uRepository.saveAndFlush(user);

		// Upload ảnh đại diện (avatar)
		// ======================================================================================
		if (multipartFile != null && !multipartFile.isEmpty()) {
			String fAvatarImageName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String uploadDir = UserHelper.ROOT_PATH_AVATAR_MEDIUM + File.separator + savedUser.getId();
			UserHelper.saveAvatarImage(multipartFile, uploadDir, fAvatarImageName);

			User updateAvatarUser = uRepository.findById(savedUser.getId()).get();
			updateAvatarUser.setAvatar(uploadDir + File.separator + fAvatarImageName);
			uRepository.saveAndFlush(updateAvatarUser);
		}
		// =======================================================================================

		return "redirect:/user";
	}

	@GetMapping("/detail/{id}")
	public String detailUser(@PathVariable("id") Integer id, Model m) {

		Optional<User> opUser = uRepository.findById(id);

		if (opUser.isPresent()) {
			User user = opUser.get();

			m.addAttribute("user", user);
			m.addAttribute("active_user", true);
			return "admin-pages/user-detail";
		}

		// Nếu không tìm thấy tài khoản với id trên thì trở về trang users
		return "redirect:/user";
	}

	@PostMapping("/update/{id}")
	public String updateUser(@PathVariable("id") Integer id, @ModelAttribute("user") User user, @RequestParam("user_avatar")
			MultipartFile multipartFile, Model m, RedirectAttributes ra) throws IOException {

		if (user.getName() == null || user.getName().isBlank() || user.getEmail() == null || user.getEmail().isBlank()
				|| user.getPhone() == null || user.getPhone().isBlank()) {

			ra.addFlashAttribute("error", "Cập nhật tài khoản thất bại.");
			ra.addFlashAttribute("user", user);

			return "redirect:/user/detail/" + id;
		}
//		else
		Optional<User> opUser = uRepository.findById(id);

		if (opUser.isPresent()) {
			User nUser = opUser.get();
			nUser.setName(user.getName());
			nUser.setEmail(user.getEmail());
			nUser.setPhone(user.getPhone());
			nUser.setRole(user.getRole());

			// Cập nhật ảnh đại diện
			// ======================================================================================
			if (multipartFile != null && !multipartFile.isEmpty()) {
				String fAvatarImageName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				String uploadDir = UserHelper.ROOT_PATH_AVATAR_MEDIUM + File.separator + nUser.getId();
				UserHelper.saveAvatarImage(multipartFile, uploadDir, fAvatarImageName);
				nUser.setAvatar(UserHelper.ROOT_PATH_AVATAR_MEDIUM + File.separator + nUser.getId() + File.separator +fAvatarImageName);
			}
			// =======================================================================================

			uRepository.saveAndFlush(nUser);
			ra.addFlashAttribute("success", "Tài khoản đã được cập nhật thành công.");
			return "redirect:/user/detail/" + nUser.getId();
		} else {

			ra.addFlashAttribute("error", "Tài khoản này không tồn tại hoặc đã bị xóa.");
			return "redirect:/user";
		}
	}

	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes ra) {

		Optional<User> opUser = uRepository.findById(id);
		if (opUser.isPresent()) {
			uRepository.deleteById(id);
			
			if (opUser.get().getAvatar() != null) {
				deleteAvatarImageDir(id);
			}
			ra.addFlashAttribute("success", "Tài khoản đã được xóa thành công.");
			return "redirect:/user";
		}
//		else

		ra.addFlashAttribute("error", "Tài khoản không tồn tại hoặc đã bị xóa.");
		return "redirect:/user";
	}

	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "admin-pages/500";
	}
	
	
	
	/**
	 * @param userId
	 * 
	 * Xóa folder ảnh đại diện của người dùng khi tài khoản bị xóa
	 */
	private void deleteAvatarImageDir(Integer userId) {
		Path pUserAvatarImage = Paths.get(UserHelper.ROOT_PATH_AVATAR_MEDIUM + File.separator + userId);
		
		File fUploadAvatarImageDir = pUserAvatarImage.toFile();
		if (fUploadAvatarImageDir.exists()) {
			File[] fAvatarImages = fUploadAvatarImageDir.listFiles();
			for (File f : fAvatarImages) {
				if (f.delete()) {
					System.out.println("Deleted file: " + f.getName());
				}
			}
			if (fUploadAvatarImageDir.delete()) {
				System.out.println("Deleted folder: " + fUploadAvatarImageDir.getName());
			}
		}
	}
}
