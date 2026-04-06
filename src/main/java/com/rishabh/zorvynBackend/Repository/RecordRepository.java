package com.rishabh.zorvynBackend.Repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rishabh.zorvynBackend.Entity.FinancialRecord;
import com.rishabh.zorvynBackend.Enums.Category;
import com.rishabh.zorvynBackend.Enums.RecordType;

public interface RecordRepository extends JpaRepository<FinancialRecord, Long> {
    @Query("""
            SELECT fr FROM FinancialRecord fr
            WHERE fr.deletedAt IS NULL
              AND (:category IS NULL OR fr.category = :category)
              AND (:type IS NULL OR fr.type = :type)
              AND (:fromDate IS NULL OR fr.date >= :fromDate)
              AND (:toDate IS NULL OR fr.date <= :toDate)
            """)
    Page<FinancialRecord> findActiveRecords(
            @Param("category") Category category,
            @Param("type") RecordType type,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable);

    Optional<FinancialRecord> findByIdAndDeletedAtIsNull(Long id);
}