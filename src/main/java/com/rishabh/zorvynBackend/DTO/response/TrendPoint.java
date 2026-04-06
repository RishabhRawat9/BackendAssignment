package com.rishabh.zorvynBackend.DTO.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrendPoint {
    private LocalDate date;
    private BigDecimal income;
    private BigDecimal expense;
    private BigDecimal net;
}