package com.example.user_microservice.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.user_microservice.model.User;
import com.example.user_microservice.repository.UserRepository;
import com.example.user_microservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("unit-tests")
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void prepare() {
        reset(userService);
    }

    @Test
    void findAllUsers_success() throws Exception {
        // arrange
        when(userService.findAll()).thenReturn(List.of(new User(),
                User.builder().name("pierre").build(),
                new User()));

        // act
        var response = mockMvc.perform(get("/users"));

        // assert
        response
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].name", is("pierre")));
        verify(userService, times(1)).findAll();
        verifyNoMoreInteractions(userService);

    }

    @Test
    void findById_success() throws Exception {
        // arrange
        UUID id = UUID.randomUUID();
        when(userService.findById(id)).thenReturn(User.builder().id(id).name("pierre").build());

        // act
        var response = mockMvc.perform(get("/users/" + id));

        // assert
        response
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("pierre")));
        verify(userService, times(1)).findById(id);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void findById_BadRequest() throws Exception {
        // arrange
        // act
        var response = mockMvc.perform(get("/users/" + "gaelle"));

        // assert
        response
                .andExpect(status().isBadRequest());
        verifyNoInteractions(userService);

    }

    @Test
    void findById_NotFound() throws Exception {
        // arrange
        UUID id = UUID.randomUUID();
        when(userService.findById(id)).thenReturn(null);

        // act
        var response = mockMvc.perform(get("/users/" + id));

        // assert
        response
                .andExpect(status().isNotFound());
        verify(userService, times(1)).findById(id);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void save_success() throws Exception {
        // arrange
        User user = User.builder()
                .name("pierre")
                .email("pierre@example.com")
                .password("password123")
                .build();

        when(userService.save(any(User.class))).thenReturn(user);

        // act
        var response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        // assert
        response
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("pierre")));

        verify(userService, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    void save_BadRequest() throws Exception {
        // arrange
        User user = User.builder()
                .name("pierre")
                .email("pierre@gmail.com")
                .build();

        // act
        var response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        // assert
        response
                .andExpect(status().isBadRequest());
        verifyNoInteractions(userService);
    }

    @Test
    void deleteUserById_success() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();
        User mockUser = new User();
        mockUser.setId(userId);

        when(userService.findById(userId)).thenReturn(mockUser);

        doNothing().when(userService).deleteById(userId);

        // Act & Assert
        mockMvc.perform(delete("/users/" + userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).findById(userId);
        verify(userService, times(1)).deleteById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void deleteUserById_notFound() throws Exception {
        // Arrange
        UUID userId = UUID.randomUUID();

        when(userService.findById(userId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(delete("/users/" + userId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void createUser_passwordTooShort() throws Exception {
        // Arrange
        User user = User.builder().name("John Doe").email("john@example.com").password("123").build();

        // Act & Assert
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_missingEmail() throws Exception {
        // Arrange
        User user = User.builder().name("John Doe").password("password123").build();

        // Act & Assert
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_missingName() throws Exception {
        // Arrange
        User user = User.builder().email("test@gmail.com").password("password123").build();

        // Act & Assert
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void createUser_missingPassword() throws Exception {
        // Arrange
        User user = User.builder().name("John Doe").email("test@gmail.com").build();

        // Act & Assert
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
}
