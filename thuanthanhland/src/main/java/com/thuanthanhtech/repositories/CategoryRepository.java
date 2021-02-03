package com.thuanthanhtech.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thuanthanhtech.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
