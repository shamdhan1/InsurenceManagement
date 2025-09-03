package com.example.Restapis_ProjectPractice.repository;

import com.example.Restapis_ProjectPractice.entity.Payment;
import com.example.Restapis_ProjectPractice.entity.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Page<Payment> findByType(PaymentType type, Pageable pageable);
    Page<Payment> findByPolicy_Id(Long policyId, Pageable pageable);
    Page<Payment> findByCustomer_Id(Long customerId, Pageable pageable);
}
