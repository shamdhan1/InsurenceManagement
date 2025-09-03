package com.example.Restapis_ProjectPractice.controller;


import com.example.Restapis_ProjectPractice.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/customers/active")
    @Operation(summary = "Active customers report")
    public ResponseEntity<List<Map<String, Object>>> activeCustomers() {
        return ResponseEntity.ok(reportService.activeCustomers());
    }

    @GetMapping("/policies/type")
    @Operation(summary = "Count policies by type")
    public ResponseEntity<List<Map<String, Object>>> policiesByType() {
        return ResponseEntity.ok(reportService.policiesByType());
    }

    @GetMapping("/claims/status")
    @Operation(summary = "Claims report by status")
    public ResponseEntity<List<Map<String, Object>>> claimsByStatus() {
        return ResponseEntity.ok(reportService.claimsByStatus());
    }

    @GetMapping("/premiums/collected")
    @Operation(summary = "Premiums collected report")
    public ResponseEntity<Map<String, Object>> premiumsCollected(@RequestParam(defaultValue = "#{T(java.time.Year).now().value}") int year) {
        return ResponseEntity.ok(reportService.premiumsCollected(year));
    }

    @GetMapping("/loss-ratio")
    @Operation(summary = "Insurance loss ratio = claims paid / premiums")
    public ResponseEntity<Map<String, Object>> lossRatio(@RequestParam(defaultValue = "#{T(java.time.Year).now().value}") int year) {
        return ResponseEntity.ok(reportService.lossRatio(year));
    }


}
