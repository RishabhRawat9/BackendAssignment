package com.rishabh.zorvynBackend.Service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.rishabh.zorvynBackend.DTO.request.CreateUserRequest;
import com.rishabh.zorvynBackend.DTO.request.UpdateStatusRequest;
import com.rishabh.zorvynBackend.DTO.request.UpdateUserRequest;
import com.rishabh.zorvynBackend.DTO.response.UserResponse;
import com.rishabh.zorvynBackend.Entity.User;
import com.rishabh.zorvynBackend.Enums.Role;
import com.rishabh.zorvynBackend.Enums.Status;
import com.rishabh.zorvynBackend.Exception.DuplicateEmailException;
import com.rishabh.zorvynBackend.Exception.EntityNotFoundException;
import com.rishabh.zorvynBackend.Repository.UserRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + request.getEmail());
        }
        if (request.getRole() != Role.VIEWER && request.getRole() != Role.ANALYST) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only VIEWER and ANALYST roles can be created");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setStatus(Status.ACTIVE);

        User savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == Role.VIEWER || user.getRole() == Role.ANALYST)
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(@NonNull Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        if (user.getRole() == Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot access admin user");
        }
        return toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(@NonNull Long id, UpdateUserRequest request) {

        if (request.getRole() != Role.VIEWER && request.getRole() != Role.ANALYST) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only VIEWER and ANALYST roles can be updated");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        user.setName(request.getName());
        user.setRole(request.getRole());

        User updatedUser = userRepository.save(user);
        return toResponse(updatedUser);
    }

    @Transactional
    public UserResponse updateStatus(@NonNull Long id, UpdateStatusRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        user.setStatus(request.getStatus());

        User updatedUser = userRepository.save(user);
        return toResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(@NonNull Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        if (user.getRole() != Role.VIEWER && user.getRole() != Role.ANALYST) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only VIEWER and ANALYST roles can be deleted");
        }
        userRepository.delete(user);
    }

    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}