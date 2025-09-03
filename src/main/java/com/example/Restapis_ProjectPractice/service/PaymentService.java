package com.example.Restapis_ProjectPractice.service;

import com.example.Restapis_ProjectPractice.dto.PaymentDTO;
import com.example.Restapis_ProjectPractice.entity.PaymentStatus;
import com.example.Restapis_ProjectPractice.entity.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentService {

    PaymentDTO create(Long policyId, PaymentType type, Double amount);
    PaymentDTO getById(Long id);
    Page<PaymentDTO> list(PaymentType type, Pageable pageable);
    PaymentDTO updateStatus(Long id, PaymentStatus status);
    Page<PaymentDTO> byPolicy(Long policyId, Pageable pageable);
    Page<PaymentDTO> byCustomer(Long customerId, Pageable pageable);
    List<Object[]> duePremiums();            // simplified
    List<Object[]> monthlyReport(int year);  // simplified
}
