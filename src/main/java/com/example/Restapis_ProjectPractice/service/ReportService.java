package com.example.Restapis_ProjectPractice.service;

import java.util.List;
import java.util.Map;

public interface ReportService {
    List<Map<String, Object>> activeCustomers();
    List<Map<String, Object>> policiesByType();
    List<Map<String, Object>> claimsByStatus();
    Map<String, Object> premiumsCollected(int year);
    Map<String, Object> lossRatio(int year);
}
