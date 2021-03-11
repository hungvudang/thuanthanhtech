package com.thuanthanhtech.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.thuanthanhtech.entities.Category;
import com.thuanthanhtech.entities.News;

@Repository
public interface NewsPagingAndSortRepository extends PagingAndSortingRepository<News, Integer> {
	
	Page<News> findByPub(Integer pub, Pageable pageable);
	
	Page<News> findByCategoryAndPub(Category category, Integer pub, Pageable pageable);
	
	List<News> findTop5ByCategoryAndPub(Category category ,Integer pub, Sort sort);

}
