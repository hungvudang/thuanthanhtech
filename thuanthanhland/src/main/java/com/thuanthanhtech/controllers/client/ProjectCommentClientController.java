package com.thuanthanhtech.controllers.client;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuanthanhtech.entities.Item;
import com.thuanthanhtech.entities.Project;
import com.thuanthanhtech.entities.ProjectComment;
import com.thuanthanhtech.entities.ProjectCommentHelper;
import com.thuanthanhtech.entities.User;
import com.thuanthanhtech.repositories.ProjectCommentRepository;
import com.thuanthanhtech.repositories.ProjectRepository;
import com.thuanthanhtech.repositories.UserRepository;

@Controller
@RequestMapping("/du-an/binh-luan")
public class ProjectCommentClientController {

	@Autowired
	private UserRepository uRepository;

	@Autowired
	private ProjectRepository pRepository;

	@Autowired
	private ProjectCommentRepository pCmtRepository;
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public void createProjectComment(@RequestParam(name = "email", required = false) String email,
			@RequestParam(name = "parentId", required = false) Integer parentId,
			@RequestParam(name = "projectId") Integer projectId,
			@Valid @ModelAttribute("pComment") ProjectComment pComment, BindingResult br, RedirectAttributes ra,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		String referer = request.getHeader("referer");
		System.out.println(referer);

		if (email == null) {
			ra.addFlashAttribute("error", "Tài khoản người dùng không hợp lệ");
			response.sendRedirect(referer);
		} else {

			if (br.hasErrors()) {
				ra.addFlashAttribute("org.springframework.validation.BindingResult.pComment", br);
				response.sendRedirect(referer);
			} else {
				Optional<User> opUser = uRepository.findByEmail(email);
				if (!opUser.isPresent()) {
					ra.addFlashAttribute("error", "Tài khoản người dùng không tồn tại, hoặc đã bị xóa");
					response.sendRedirect(referer);
				} else {
					if (parentId != null) {
						pComment.setParentId(parentId);
					}

					Project project = pRepository.findById(projectId).get();
					pComment.setProject(project);

					User user = opUser.get();
					pComment.setUser(user);

					pComment.setPub(1);

					pCmtRepository.saveAndFlush(pComment);
					ra.addFlashAttribute("success", "Thêm bình luận thành công");

					response.sendRedirect(referer);
				}
			}
		}
	}
	
	public void projectComments(Model m, Project project) {
		List<ProjectComment> projectComments = project.getComments();
		Item<ProjectComment> root = new Item<ProjectComment>();
		ProjectCommentHelper.getProjectCommentTree(projectComments, root);
		m.addAttribute("rootComment", root);
	}

	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "/errors/500";
	}

}
