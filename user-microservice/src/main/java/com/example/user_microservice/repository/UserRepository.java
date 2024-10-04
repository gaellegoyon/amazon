package com.example.user_microservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user_microservice.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {

}