package com.example.Restapis_ProjectPractice.service;


import com.example.Restapis_ProjectPractice.entity.Payment;
import com.example.Restapis_ProjectPractice.entity.PaymentStatus;
import com.example.Restapis_ProjectPractice.entity.PaymentType;
import com.example.Restapis_ProjectPractice.entity.Policy;
import com.example.Restapis_ProjectPractice.repository.ClaimRepository;
import com.example.Restapis_ProjectPractice.repository.CustomerRepository;
import com.example.Restapis_ProjectPractice.repository.PaymentRepository;
import com.example.Restapis_ProjectPractice.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final CustomerRepository customerRepository;
    private final PolicyRepository policyRepository;
    private final ClaimRepository claimRepository;
    private final PaymentRepository paymentRepository;


    @Override
    public List<Map<String, Object>> activeCustomers() {
        // customers with at least one ACTIVE policy
        List<Policy> policies = policyRepository.findAll();
        Set<Long> activeCustomerIds = policies.stream()
                .filter(p -> "ACTIVE".equalsIgnoreCase(p.getStatus()))
                .map(p -> p.getCustomer().getId())
                .collect(Collectors.toSet());

        return customerRepository.findAll().stream()
                .filter(c -> activeCustomerIds.contains(c.getId()))
                .map(c -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("customerId", c.getId());
                    map.put("fullName", c.getFullName());
                    map.put("email", c.getEmail());
                    return map;
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<Map<String, Object>> policiesByType() {
        return policyRepository.findAll().stream()
                .collect(Collectors.groupingBy(Policy::getType, Collectors.counting()))
                .entrySet().stream()
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", e.getKey());
                    map.put("count", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> claimsByStatus() {
        return claimRepository.findAll().stream()
                .collect(Collectors.groupingBy(c -> c.getStatus().name(), Collectors.counting()))
                .entrySet().stream()
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("status", e.getKey());
                    map.put("count", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> premiumsCollected(int year) {
        double total = paymentRepository.findAll().stream()
                .filter(p -> p.getType() == PaymentType.PREMIUM && p.getStatus() == PaymentStatus.SUCCESS)
                .filter(p -> p.getCreatedAt() != null && p.getCreatedAt().getYear() == year)
                .mapToDouble(Payment::getAmount).sum();
        return Map.of("year", year, "premiumsCollected", total);
    }

    @Override
    public Map<String, Object> lossRatio(int year) {
        double premiums = (double) premiumsCollected(year).get("premiumsCollected");
        double claimsPaid = paymentRepository.findAll().stream()
                .filter(p -> p.getType() == PaymentType.CLAIM_SETTLEMENT && p.getStatus() == PaymentStatus.SUCCESS)
                .filter(p -> p.getCreatedAt() != null && p.getCreatedAt().getYear() == year)
                .mapToDouble(Payment::getAmount).sum();
        double ratio = premiums == 0 ? 0 : (claimsPaid / premiums);
        return Map.of("year", year, "premiumsCollected", premiums, "claimsPaid", claimsPaid, "lossRatio", ratio);
    }
}
