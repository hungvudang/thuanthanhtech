package com.thuanthanhtech.controllers.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.thuanthanhtech.entities.ProjectComment;
import com.thuanthanhtech.repositories.ProjectCommentRepository;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin("*")
@RequestMapping("/admin/api/project-comment")
public class ProjectCommentRestController {
	
	@Autowired
	private ProjectCommentRepository pCmtRepository;
	
	@GetMapping
	@ResponseBody
	public ResponseEntity<?> projectComments() {
		List<ProjectComment> projectComments = pCmtRepository.findAll();
		return ResponseEntity.ok(projectComments);
	}
	
	@PutMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> updateProjectComment(@PathVariable("id") Integer id, @RequestBody ProjectComment pComment){
		Optional<ProjectComment> opPComment = pCmtRepository.findById(id);
		if (opPComment.isPresent()) {
			ProjectComment nPComment = new ProjectComment();
			nPComment.setProject(pComment.getProject());
			nPComment.setParentId(pComment.getParentId());
			nPComment.setUser(pComment.getUser());
			nPComment.setContent(pComment.getContent());
			
			nPComment =  pCmtRepository.saveAndFlush(nPComment);
			return ResponseEntity.ok(nPComment);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cập nhật bình luận thất bại");
		}
	}
	
	@DeleteMapping("{id}")
	@ResponseBody
	public ResponseEntity<?> deleteProjectComment(@PathVariable("id") Integer id){
		Optional<ProjectComment> opPComment = pCmtRepository.findById(id);
		if (opPComment.isPresent()) {
			pCmtRepository.deleteById(id);
			return ResponseEntity.ok("Bình luận đã được xóa thành công");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bình luận không tồn tại hoặc đã bị xóa");
		}
	}
	
	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "/errors/500";
	}
}
