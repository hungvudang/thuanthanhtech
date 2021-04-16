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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
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

import com.thuanthanhtech.entities.Helper;
import com.thuanthanhtech.entities.User;
import com.thuanthanhtech.entities.UserHelper;
import com.thuanthanhtech.entities.UserValidator;
import com.thuanthanhtech.repositories.UserRepository;

@Controller
@RequestMapping("/admin/user")
public class UserAdminController {

	@Autowired
	private UserRepository uRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping
	public String user(Model m) {
		List<User> users = uRepository.findAll();
		m.addAttribute("users", users);
		m.addAttribute("active_user", true);

		return "/admin-pages/user";
	}

	@GetMapping("/create")
	public String createUser(Model m) {

		if (!m.containsAttribute("user")) {
			User user = new User();
			user.setRole(0);
			user.setAvatar(Helper.NO_IMAGE_MEDIUM_PNG);

			m.addAttribute("user", user);
		}

		m.addAttribute("active_user", true);

		return "admin-pages/user-create";
	}

	@PostMapping("/save")
	public String saveUser(@Validated(UserValidator.saveValidation.class) @ModelAttribute("user") User user,
			BindingResult br, @RequestParam("user_avatar") MultipartFile multipartFile, RedirectAttributes ra, Model m)
			throws IOException {

		if (br.hasErrors()) {

			user.setAvatar(Helper.NO_IMAGE_MEDIUM_PNG);
			ra.addFlashAttribute("error", "Tạo tài khoản mới thất bại");
			ra.addFlashAttribute("user", user);
			ra.addFlashAttribute("org.springframework.validation.BindingResult.user", br);
			return "redirect:/admin/user/create";

		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User savedUser = uRepository.saveAndFlush(user);

		// Upload ảnh đại diện (avatar)
		// ======================================================================================
		if (multipartFile != null && !multipartFile.isEmpty()) {

			// Kiểm tra file upload lên có đúng định dạng không
			String contentType = multipartFile.getContentType();
			if (!contentType.matches("^image/.+")) {

				FieldError avatarError = new FieldError("user", "avatar",
						"Hình ảnh không đúng định dạng. Ảnh phải có định dạnh (*.jpg, *.jpge, *.png)");
				br.addError(avatarError);

				ra.addFlashAttribute("error", "Tạo tài khoản mới thất bại");
				user.setAvatar(Helper.NO_IMAGE_MEDIUM_PNG);
				ra.addFlashAttribute("user", user);
				ra.addFlashAttribute("org.springframework.validation.BindingResult.user", br);
				return "redirect:/admin/user/create";
			}

			String fAvatarImageName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String uploadDir = UserHelper.ROOT_PATH_AVATAR_MEDIUM + Helper.FILE_SEPARTOR + savedUser.getId();
			UserHelper.saveAvatarImage(multipartFile, uploadDir, fAvatarImageName);

			User updateAvatarUser = uRepository.findById(savedUser.getId()).get();
			updateAvatarUser.setAvatar(uploadDir + Helper.FILE_SEPARTOR + fAvatarImageName);
			uRepository.saveAndFlush(updateAvatarUser);
		}
		// =======================================================================================
		ra.addFlashAttribute("success", "Tài khoản mới đã được tạo thành công");
		
		return "redirect:/admin/user";
	}

	@GetMapping("/detail/{id}")
	public String detailUser(@PathVariable("id") Integer id, Model m, RedirectAttributes ra) {
		if (!m.containsAttribute("user")) {
			Optional<User> opUser = uRepository.findById(id);

			if (opUser.isPresent()) {
				User user = opUser.get();
				if (user.getAvatar() == null) {
					m.addAttribute("NO_THUMBNAIL_IMAGE", Helper.NO_IMAGE_MEDIUM_PNG);
				}
				m.addAttribute("user", user);

			} else {
				// Nếu không tìm thấy tài khoản với id trên thì trở về trang users
				ra.addFlashAttribute("error", "Tài khoản không tồn tại hoặc đã bị xóa");
				return "redirect:/admin/user";
			}

		}

		m.addAttribute("active_user", true);
		return "admin-pages/user-detail";
	}

	@PostMapping("/update/{id}")
	public String updateUser(@PathVariable("id") Integer id,
			@Validated(UserValidator.updateValidation.class) @ModelAttribute("user") User user, BindingResult br,
			@RequestParam("user_avatar") MultipartFile multipartFile, Model m, RedirectAttributes ra,
			@RequestParam(name = "isChangePassword", required = false) String isChangePassword,
			@RequestParam(name = "newPassword", required = false) String newPassword) throws IOException {

		// Bỏ qua phần validate password khi cập nhật. Vì không cho phép cập nhật
		// password
		if (br.hasErrors()) {

			ra.addFlashAttribute("user", user);
			ra.addFlashAttribute("org.springframework.validation.BindingResult.user", br);
			ra.addFlashAttribute("error", "Cập nhật tài khoản thất bại");
			return "redirect:/admin/user/detail/" + id;
		}
//		else
		Optional<User> opUser = uRepository.findById(id);

		if (opUser.isPresent()) {
			User nUser = opUser.get();
			nUser.setName(user.getName());
			nUser.setEmail(user.getEmail());
			nUser.setPhone(user.getPhone());
			nUser.setAddress(user.getAddress());
			nUser.setRole(user.getRole());

			// Cập nhật mật khẩu nếu có
			if (isChangePassword != null) {
				
				if (newPassword == null || newPassword.isBlank() || newPassword.length() < 8) {

					FieldError passwordError = new FieldError("user", "password", "Mật khẩu mới không hợp lệ");
					br.addError(passwordError);

					ra.addFlashAttribute("user", user);
					ra.addFlashAttribute("org.springframework.validation.BindingResult.user", br);
					ra.addFlashAttribute("error", "Cập nhật tài khoản thất bại");
					return "redirect:/admin/user/detail/" + id;
				}

				nUser.setPassword(passwordEncoder.encode(newPassword));
			}

			// Cập nhật ảnh đại diện
			// ======================================================================================
			if (multipartFile != null && !multipartFile.isEmpty()) {

				// Kiểm tra file upload lên có đúng định dạng không
				String contentType = multipartFile.getContentType();
				if (!contentType.matches("^image/.+")) {
					FieldError imageError = new FieldError("user", "avatar",
							"Hình ảnh không đúng định dạng. Ảnh phải có định dạnh (*.jpg, *.jpge, *.png)");
					br.addError(imageError);
					ra.addFlashAttribute("user", user);
					ra.addFlashAttribute("org.springframework.validation.BindingResult.user", br);
					ra.addFlashAttribute("error", "Cập nhật tài khoản thất bại");
					return "redirect:/admin/user/detail/" + id;
				}

				// Xóa ảnh đại đại diện cũ
				deleteAvatarImageDir(id);

				String fAvatarImageName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				String uploadDir = UserHelper.ROOT_PATH_AVATAR_MEDIUM + Helper.FILE_SEPARTOR + nUser.getId();
				UserHelper.saveAvatarImage(multipartFile, uploadDir, fAvatarImageName);
				nUser.setAvatar(UserHelper.ROOT_PATH_AVATAR_MEDIUM + Helper.FILE_SEPARTOR + nUser.getId()
						+ File.separator + fAvatarImageName);
			}
			// =======================================================================================

			uRepository.saveAndFlush(nUser);
			ra.addFlashAttribute("success", "Tài khoản đã được cập nhật thành công");
			return "redirect:/admin/user/detail/" + nUser.getId();
		} else {

			ra.addFlashAttribute("error", "Tài khoản này không tồn tại hoặc đã bị xóa");
			return "redirect:/admin/user";
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
			ra.addFlashAttribute("success", "Tài khoản đã được xóa thành công");

		} else {
			ra.addFlashAttribute("error", "Tài khoản không tồn tại hoặc đã bị xóa");
		}

		return "redirect:/admin/user";
	}

	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "/errors/500";
	}

	/**
	 * @param userId
	 * 
	 *               Xóa folder ảnh đại diện của người dùng khi tài khoản bị xóa
	 */
	private void deleteAvatarImageDir(Integer userId) {
		Path pUserAvatarImage = Paths.get(UserHelper.ROOT_PATH_AVATAR_MEDIUM + Helper.FILE_SEPARTOR + userId);

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
