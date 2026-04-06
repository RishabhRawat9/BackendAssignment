package com.rishabh.zorvynBackend.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.rishabh.zorvynBackend.DTO.request.CreateRecordRequest;
import com.rishabh.zorvynBackend.DTO.request.PatchRecordRequest;
import com.rishabh.zorvynBackend.DTO.request.RecordFilterRequest;
import com.rishabh.zorvynBackend.DTO.request.UpdateRecordRequest;
import com.rishabh.zorvynBackend.DTO.response.RecordResponse;
import com.rishabh.zorvynBackend.Entity.FinancialRecord;
import com.rishabh.zorvynBackend.Entity.User;
import com.rishabh.zorvynBackend.Exception.EntityNotFoundException;
import com.rishabh.zorvynBackend.Repository.RecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    @Transactional(readOnly = true)
    public Page<RecordResponse> getRecords(RecordFilterRequest filters, Pageable pageable) {
        // page comes from spring data jpa, its an interface SDJ implements it bts;
        LocalDate from = filters.getFrom();
        LocalDate to = filters.getTo();

        if (from != null && to != null && from.isAfter(to)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'from' date must be on or before 'to' date");
        }

        Page<FinancialRecord> recordsPage = recordRepository.findActiveRecords(
                filters.getCategory(),
                filters.getType(),
                from,
                to,
                pageable); // pageable contains the page info like pageno., items contained;

        return recordsPage.map(record -> toResponse(record));
    }

    @Transactional
    public RecordResponse createRecord(CreateRecordRequest request) {
        User currentUser = getCurrentUser();

        FinancialRecord record = new FinancialRecord();
        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDate(request.getDate());
        record.setNotes(request.getNotes());
        record.setCreatedBy(currentUser);

        FinancialRecord savedRecord = recordRepository.save(record);
        return toResponse(savedRecord);
    }

    @Transactional
    public RecordResponse updateRecord(Long id, UpdateRecordRequest request) {
        FinancialRecord record = recordRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Record not found with id: " + id));

        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDate(request.getDate());
        record.setNotes(request.getNotes());

        FinancialRecord updatedRecord = recordRepository.save(record);
        return toResponse(updatedRecord);
    }

    @Transactional
    public RecordResponse patchRecord(Long id, PatchRecordRequest request) {
        FinancialRecord record = recordRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Record not found with id: " + id));

        if (request.getAmount() != null) {
            record.setAmount(request.getAmount());
        }
        if (request.getType() != null) {
            record.setType(request.getType());
        }
        if (request.getCategory() != null) {
            record.setCategory(request.getCategory());
        }
        if (request.getDate() != null) {
            record.setDate(request.getDate());
        }
        if (request.getNotes() != null) {
            record.setNotes(request.getNotes());
        }

        FinancialRecord patchedRecord = recordRepository.save(record);
        return toResponse(patchedRecord);
    }

    @Transactional
    public void softDeleteRecord(Long id) {
        FinancialRecord record = recordRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Record not found with id: " + id));

        record.setDeletedAt(LocalDateTime.now());
        recordRepository.save(record);
    }

    private RecordResponse toResponse(FinancialRecord record) {
        RecordResponse response = new RecordResponse();
        response.setId(record.getId());
        response.setAmount(record.getAmount());
        response.setType(record.getType());
        response.setCategory(record.getCategory());
        response.setDate(record.getDate());
        response.setNotes(record.getNotes());
        response.setCreatedByName(record.getCreatedBy() != null ? record.getCreatedBy().getName() : null);
        response.setCreatedAt(record.getCreatedAt());
        return response;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication != null ? authentication.getPrincipal() : null;
        if (!(principal instanceof User user)) {// if principal is of type User it also creates a variable user
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authentication principal");
        }
        return user;
    }
}