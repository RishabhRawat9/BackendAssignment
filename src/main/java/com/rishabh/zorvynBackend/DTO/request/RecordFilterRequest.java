package com.rishabh.zorvynBackend.DTO.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.rishabh.zorvynBackend.Enums.Category;
import com.rishabh.zorvynBackend.Enums.RecordType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecordFilterRequest {
    private Category category;
    private RecordType type;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;

    @Min(value = 0, message = "Page must be greater than or equal to 0")
    private int page = 0;// offset : like from which page to start and each page has lets' say 10
                         // records; so 0 page, first 10 , thne page 1-> next 10;

    @Min(value = 1, message = "Size must be at least 1")
    @Max(value = 100, message = "Size must be at most 100")
    private int size = 10;
}