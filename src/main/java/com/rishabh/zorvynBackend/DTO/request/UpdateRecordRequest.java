package com.rishabh.zorvynBackend.DTO.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.rishabh.zorvynBackend.Enums.Category;
import com.rishabh.zorvynBackend.Enums.RecordType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateRecordRequest {
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Type is required")
    private RecordType type;

    @NotNull(message = "Category is required")
    private Category category;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate date;

    @Size(max = 2000, message = "Notes cannot exceed 2000 characters")
    private String notes;
}