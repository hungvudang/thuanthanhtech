package com.thuanthanhtech.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.thuanthanhtech.entities.Project;

public interface ProjectPagingAndSortRepository extends PagingAndSortingRepository<Project, Integer>{
	
	Page<Project> findByPub(Integer pub, Pageable pageable);
}
