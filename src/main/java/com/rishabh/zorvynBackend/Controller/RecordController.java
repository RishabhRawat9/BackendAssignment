package com.rishabh.zorvynBackend.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rishabh.zorvynBackend.DTO.request.CreateRecordRequest;
import com.rishabh.zorvynBackend.DTO.request.PatchRecordRequest;
import com.rishabh.zorvynBackend.DTO.request.RecordFilterRequest;
import com.rishabh.zorvynBackend.DTO.request.UpdateRecordRequest;
import com.rishabh.zorvynBackend.DTO.response.RecordResponse;
import com.rishabh.zorvynBackend.Service.RecordService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ResponseEntity<Page<RecordResponse>> getRecords(
            @Valid @ModelAttribute RecordFilterRequest filters) {
        Pageable pageable = PageRequest.of(filters.getPage(), filters.getSize(),
                Sort.by(Sort.Direction.DESC, "date", "createdAt"));
        return ResponseEntity.ok(recordService.getRecords(filters, pageable));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RecordResponse> createRecord(@Valid @RequestBody CreateRecordRequest request) {
        RecordResponse createdRecord = recordService.createRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecord);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RecordResponse> updateRecord(@PathVariable Long id,
            @Valid @RequestBody UpdateRecordRequest request) {
        return ResponseEntity.ok(recordService.updateRecord(id, request));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RecordResponse> patchRecord(@PathVariable Long id,
            @Valid @RequestBody PatchRecordRequest request) {
        return ResponseEntity.ok(recordService.patchRecord(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        recordService.softDeleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}