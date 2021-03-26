package com.thuanthanhtech.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.thuanthanhtech.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	Optional<Category> findBySort(Integer sort);
	
	List<Category> findByPub(Integer pub, Sort sort);
	
	Optional<Category> findBySlug(String slug);

	
	@Query("SELECT c FROM Category c WHERE c.id = :parentId")
	Optional<Category> findByParentId(@Param("parentId") Integer parentId);
}
