package com.thuanthanhtech.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thuanthanhtech.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	Optional<Category> findBySort(Integer sort);

}
