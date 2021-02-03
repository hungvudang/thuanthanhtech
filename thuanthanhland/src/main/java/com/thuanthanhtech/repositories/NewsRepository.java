package com.thuanthanhtech.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thuanthanhtech.entities.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {

}
