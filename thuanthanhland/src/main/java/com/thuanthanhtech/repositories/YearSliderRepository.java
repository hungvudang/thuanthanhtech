package com.thuanthanhtech.repositories;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thuanthanhtech.entities.YearSlider;

@Repository
public interface YearSliderRepository extends JpaRepository<YearSlider, Integer> {
	List<YearSlider> findByPub(int pub);
	
}
