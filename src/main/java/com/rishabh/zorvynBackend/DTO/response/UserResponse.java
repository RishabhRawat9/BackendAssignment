package com.rishabh.zorvynBackend.DTO.response;

import java.time.LocalDateTime;

import com.rishabh.zorvynBackend.Enums.Role;
import com.rishabh.zorvynBackend.Enums.Status;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private Status status;
    private LocalDateTime createdAt;
}