package com.rishabh.zorvynBackend.DTO.request;

import com.rishabh.zorvynBackend.Enums.Status;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateStatusRequest {
    @NotNull(message = "Status is required")
    private Status status;
}