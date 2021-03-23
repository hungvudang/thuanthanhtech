package com.thuanthanhtech.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thuanthanhtech.entities.Recruitment;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Integer> {

}
