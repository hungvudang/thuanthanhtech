package com.thuanthanhtech.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thuanthanhtech.entities.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
	List<News> findByPub(int pub);
	
	Optional<News> findBySlug(String slug);
}
