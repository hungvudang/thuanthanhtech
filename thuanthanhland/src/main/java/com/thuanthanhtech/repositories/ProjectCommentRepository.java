package com.thuanthanhtech.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thuanthanhtech.entities.ProjectComment;

@Repository
public interface ProjectCommentRepository extends JpaRepository<ProjectComment, Integer> {

}
