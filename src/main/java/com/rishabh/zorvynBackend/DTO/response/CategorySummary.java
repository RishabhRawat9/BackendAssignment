package com.rishabh.zorvynBackend.DTO.response;

import java.math.BigDecimal;

import com.rishabh.zorvynBackend.Enums.Category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategorySummary {
    private Category category;
    private BigDecimal total;
}