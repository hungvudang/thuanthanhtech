package com.thuanthanhtech.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thuanthanhtech.entities.AboutUs;

@Repository
public interface AboutUsRepository extends JpaRepository<AboutUs, Integer> {
	List<AboutUs> findByPub(int pub);
}
