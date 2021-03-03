package com.thuanthanhtech.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.thuanthanhtech.entities.News;

@Repository
public interface NewsPagingAndSortRepository extends PagingAndSortingRepository<News, Integer> {
	
	Page<News> findByPub(Integer pub, Pageable pageable);

}
