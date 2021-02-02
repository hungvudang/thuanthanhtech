package com.thuanthanhtech.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thuanthanhtech.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
