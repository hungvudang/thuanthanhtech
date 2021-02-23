package com.thuanthanhtech.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thuanthanhtech.entities.Slide;

@Repository
public interface SlideRepository extends JpaRepository<Slide, Integer> {
	
	List<Slide> findByPub(Integer pub);
}
