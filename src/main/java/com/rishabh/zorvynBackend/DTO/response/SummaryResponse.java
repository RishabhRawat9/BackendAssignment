package com.rishabh.zorvynBackend.DTO.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SummaryResponse {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netBalance;
    private long totalRecords;
    private List<TrendPoint> trend;
    private List<CategorySummary> categorySummary;
}