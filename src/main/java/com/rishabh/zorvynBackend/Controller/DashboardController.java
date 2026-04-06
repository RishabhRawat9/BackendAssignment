package com.rishabh.zorvynBackend.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rishabh.zorvynBackend.DTO.response.CategorySummary;
import com.rishabh.zorvynBackend.DTO.response.SummaryResponse;
import com.rishabh.zorvynBackend.DTO.response.TrendPoint;
import com.rishabh.zorvynBackend.Service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SummaryResponse> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/categories")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CategorySummary>> getCategories() {
        return ResponseEntity.ok(dashboardService.getCategoryBreakdown());
    }

    @GetMapping("/trends")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TrendPoint>> getTrends(@RequestParam(defaultValue = "daily") String period) {
        return ResponseEntity.ok(dashboardService.getTrends(period));
    }
}