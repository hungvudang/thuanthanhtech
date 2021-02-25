package com.thuanthanhtech.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.thuanthanhtech.entities.News;

@Repository
public interface NewsPagingAndSortRepository extends PagingAndSortingRepository<News, Integer> {

}
