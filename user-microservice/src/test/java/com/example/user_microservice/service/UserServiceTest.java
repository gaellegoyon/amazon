package com.example.user_microservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.example.user_microservice.exceptions.NotFoundException;
import com.example.user_microservice.model.User;
import com.example.user_microservice.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("unit-tests")
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void prepare() {
        reset(userRepository);
    }

    @Test
    void findAllUsers_success() {
        // arrange
        List<User> fixtures = List.of(
                User.builder().id(UUID.randomUUID()).name("marie").build(),
                User.builder().id(UUID.randomUUID()).name("andre").build());
        when(userRepository.findAll()).thenReturn(fixtures);
        // act
        var result = userService.findAll();
        // assert
        assertEquals(fixtures, result);
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findById_success() {
        // arrange
        User user = User.builder().name("lucie").build();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        // act
        var result = userService.findById(UUID.randomUUID());
        // assert
        assertEquals(user, result);
        verify(userRepository, times(1)).findById(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findById_notFound() {
        // arrange
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        // act
        var exception = assertThrows(NotFoundException.class, () -> userService.findById(UUID.randomUUID()));
        // assert
        assertTrue(exception.getMessage().contains("Utilisateur non trouvé"));
        verify(userRepository, times(1)).findById(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void createUser_success() {
        // Arrange
        User user = User.builder().name("John Doe").email("john@example.com").password("password").build();
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User createdUser = userService.save(user);

        // Assert
        assertEquals("John Doe", createdUser.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
void deleteUserById_success() {
    // Arrange
    UUID userId = UUID.randomUUID();
    doNothing().when(userRepository).deleteById(userId);

    // Act
    userService.deleteById(userId);

    // Assert
    verify(userRepository, times(1)).deleteById(userId);
}


}
