package com.example.user_microservice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    private String name;

    @Email
    @NotNull
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @ElementCollection
    private List<UUID> orders = new ArrayList<>();

}
