package com.rishabh.zorvynBackend.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rishabh.zorvynBackend.DTO.response.CategorySummary;
import com.rishabh.zorvynBackend.DTO.response.SummaryResponse;
import com.rishabh.zorvynBackend.DTO.response.TrendPoint;
import com.rishabh.zorvynBackend.Enums.RecordType;
import com.rishabh.zorvynBackend.Repository.FinancialRecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FinancialRecordRepository recordRepository;

    @Transactional(readOnly = true)
    public SummaryResponse getSummary() {
        List<Object[]> data = recordRepository.sumByTypeGrouped();
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (Object[] row : data) {
            RecordType type = (RecordType) row[0];
            BigDecimal total = (BigDecimal) row[1];

            if (type == RecordType.INCOME) {
                totalIncome = total;
            } else if (type == RecordType.EXPENSE) {
                totalExpense = total;
            }
        }

        long recordCount = recordRepository.countActive();

        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        SummaryResponse response = new SummaryResponse();

        response.setTotalIncome(totalIncome);
        response.setTotalExpense(totalExpense);
        response.setNetBalance(netBalance);
        response.setTotalRecords(recordCount);
        return response;
    }

    @Transactional(readOnly = true)
    public List<TrendPoint> getTrends(String period) {
        List<Object[]> results = switch (period == null ? "daily" : period.toLowerCase(Locale.ROOT)) {
            case "weekly" -> recordRepository.getTrendByWeek();
            case "monthly" -> recordRepository.getTrendByMonth();
            default -> recordRepository.getTrendByDay();
        };

        return results.stream()
                .map(row -> {
                    TrendPoint tp = new TrendPoint();
                    Date date = (Date) row[0];
                    LocalDate localDate = date.toLocalDate();
                    tp.setDate(localDate);
                    tp.setIncome((BigDecimal) row[1]);
                    tp.setExpense((BigDecimal) row[2]);
                    tp.setNet(((BigDecimal) row[1]).subtract((BigDecimal) row[2]));
                    return tp;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategorySummary> getCategoryBreakdown() {
        List<Object[]> results = recordRepository.sumByCategory();

        return results.stream()
                .map(row -> {
                    CategorySummary cs = new CategorySummary();
                    cs.setCategory((com.rishabh.zorvynBackend.Enums.Category) row[0]);
                    cs.setTotal((BigDecimal) row[1]);
                    return cs;
                })
                .collect(Collectors.toList());
    }
}