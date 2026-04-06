package com.rishabh.zorvynBackend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rishabh.zorvynBackend.Entity.FinancialRecord;

public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {
        List<FinancialRecord> findByDeletedAtIsNull();

        @Query("""
                            SELECT fr.type, COALESCE(SUM(fr.amount), 0)
                            FROM FinancialRecord fr
                            WHERE fr.deletedAt IS NULL
                            GROUP BY fr.type
                        """)
        List<Object[]> sumByTypeGrouped();

        @Query("""
                        SELECT COUNT(fr)
                        FROM FinancialRecord fr
                        WHERE fr.deletedAt IS NULL
                        """)
        long countActive();

        @Query("""
                        SELECT fr.category, COALESCE(SUM(fr.amount), 0) as total
                        FROM FinancialRecord fr
                        WHERE fr.deletedAt IS NULL
                        GROUP BY fr.category
                        """)
        List<Object[]> sumByCategory();

        @Query(value = """
                        SELECT DATE(fr.date) as date,
                                COALESCE(SUM(CASE WHEN fr.type = 'INCOME' THEN fr.amount ELSE 0 END), 0) as income,
                                COALESCE(SUM(CASE WHEN fr.type = 'EXPENSE' THEN fr.amount ELSE 0 END), 0) as expense
                        FROM financial_records fr
                        WHERE fr.deleted_at IS NULL
                        GROUP BY DATE(fr.date)
                        ORDER BY DATE(fr.date) DESC
                        """, nativeQuery = true)
        List<Object[]> getTrendByDay();

        @Query(value = """
                        SELECT DATE_SUB(DATE(fr.date), INTERVAL WEEKDAY(fr.date) DAY) as date,
                                COALESCE(SUM(CASE WHEN fr.type = 'INCOME' THEN fr.amount ELSE 0 END), 0) as income,
                                COALESCE(SUM(CASE WHEN fr.type = 'EXPENSE' THEN fr.amount ELSE 0 END), 0) as expense
                        FROM financial_records fr
                        WHERE fr.deleted_at IS NULL
                        GROUP BY DATE_SUB(DATE(fr.date), INTERVAL WEEKDAY(fr.date) DAY)
                        ORDER BY DATE_SUB(DATE(fr.date), INTERVAL WEEKDAY(fr.date) DAY) DESC
                        """, nativeQuery = true)
        List<Object[]> getTrendByWeek();

        @Query(value = """
                        SELECT DATE_SUB(DATE(fr.date), INTERVAL DAYOFMONTH(fr.date) - 1 DAY) as date,
                                COALESCE(SUM(CASE WHEN fr.type = 'INCOME' THEN fr.amount ELSE 0 END), 0) as income,
                                COALESCE(SUM(CASE WHEN fr.type = 'EXPENSE' THEN fr.amount ELSE 0 END), 0) as expense
                        FROM financial_records fr
                        WHERE fr.deleted_at IS NULL
                        GROUP BY DATE_SUB(DATE(fr.date), INTERVAL DAYOFMONTH(fr.date) - 1 DAY)
                        ORDER BY DATE_SUB(DATE(fr.date), INTERVAL DAYOFMONTH(fr.date) - 1 DAY) DESC
                        """, nativeQuery = true)
        List<Object[]> getTrendByMonth();
}