package com.thuanthanhtech.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thuanthanhtech.entities.Spotlight;


@Repository
public interface SpotlightRepository extends JpaRepository<Spotlight, Integer>{
	Optional<Spotlight> findBySort(Integer sort);
	
	List<Spotlight> findByPub(Integer pub, Sort sort);

}
