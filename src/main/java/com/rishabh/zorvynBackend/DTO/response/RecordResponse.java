package com.rishabh.zorvynBackend.DTO.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.rishabh.zorvynBackend.Enums.Category;
import com.rishabh.zorvynBackend.Enums.RecordType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecordResponse {
    private Long id;
    private BigDecimal amount;
    private RecordType type;
    private Category category;
    private LocalDate date;
    private String notes;
    private String createdByName;
    private LocalDateTime createdAt;
}