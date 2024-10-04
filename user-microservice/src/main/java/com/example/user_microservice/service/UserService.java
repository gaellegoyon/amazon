package com.example.user_microservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.user_microservice.model.User;
import com.example.user_microservice.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    public User update(UUID id, User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            return null;
        }
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setOrders(user.getOrders());
        return userRepository.save(existingUser);
    }

    @Transactional
    public void addOrderToUser(UUID userId, UUID orderId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            System.out.println("User with ID " + userId + " not found. Order cannot be added.");
            return;
        }
        user.getOrders().add(orderId);
        userRepository.save(user);
        System.out.println("Order " + orderId + " added to user " + userId);
    }

}
