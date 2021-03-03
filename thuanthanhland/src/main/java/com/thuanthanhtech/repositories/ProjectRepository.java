package com.thuanthanhtech.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thuanthanhtech.entities.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
	List<Project> findByPub(Integer pub);
}
