package com.example.user_microservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user_microservice.exceptions.BadRequestException;
import com.example.user_microservice.exceptions.NotFoundException;
import com.example.user_microservice.model.User;
import com.example.user_microservice.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") UUID id) {
        User user = userService.findById(id);

        if (user == null) {
            throw new NotFoundException("Utilisateur non trouvé avec l'ID : " + id);
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        if (user.getEmail() == null || user.getName() == null || user.getPassword() == null) {
            throw new BadRequestException("Les informations d'utilisateur sont incomplètes");
        }

        User createdUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @Valid @RequestBody User userDetails) {
        User existingUser = userService.findById(id);

        if (existingUser == null) {
            throw new NotFoundException("Utilisateur non trouvé avec l'ID : " + id);
        }

        User updatedUser = userService.update(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        User existingUser = userService.findById(id);

        if (existingUser == null) {
            throw new NotFoundException("Utilisateur non trouvé avec l'ID : " + id);
        }

        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
