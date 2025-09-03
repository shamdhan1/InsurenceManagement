package com.example.Restapis_ProjectPractice.service;

import com.example.Restapis_ProjectPractice.dto.PaymentDTO;
import com.example.Restapis_ProjectPractice.entity.Payment;
import com.example.Restapis_ProjectPractice.entity.PaymentStatus;
import com.example.Restapis_ProjectPractice.entity.PaymentType;
import com.example.Restapis_ProjectPractice.entity.Policy;
import com.example.Restapis_ProjectPractice.exception.BadRequestException;
import com.example.Restapis_ProjectPractice.exception.ResourceNotFoundException;
import com.example.Restapis_ProjectPractice.repository.PaymentRepository;
import com.example.Restapis_ProjectPractice.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PolicyRepository policyRepository;


    private PaymentDTO toDTO(Payment p) {
        return PaymentDTO.builder()
                .id(p.getId())
                .transactionRef(p.getTransactionRef())
                .type(p.getType())
                .status(p.getStatus())
                .amount(p.getAmount())
                .policyId(p.getPolicy().getId())
                .customerId(p.getCustomer().getId())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    @Override
    public PaymentDTO create(Long policyId, PaymentType type, Double amount) {

        if (amount == null || amount <= 0) throw new BadRequestException("amount must be > 0");
        if (type == null) throw new BadRequestException("type is required");

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

        Payment payment = Payment.builder()
                .transactionRef("TXN-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase())
                .type(type)
                .status(PaymentStatus.PENDING)
                .amount(amount)
                .policy(policy)
                .customer(policy.getCustomer())
                .build();

        return toDTO(paymentRepository.save(payment));
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDTO getById(Long id) {
        return toDTO(paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDTO> list(PaymentType type, Pageable pageable) {
        if (type != null) return paymentRepository.findByType(type, pageable).map(this::toDTO);
        return paymentRepository.findAll(pageable).map(this::toDTO);
    }

    @Override
    public PaymentDTO updateStatus(Long id, PaymentStatus status) {
        if (status == null) throw new BadRequestException("status is required");
        Payment p = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        p.setStatus(status);
        return toDTO(paymentRepository.save(p));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDTO> byPolicy(Long policyId, Pageable pageable) {
        return paymentRepository.findByPolicy_Id(policyId, pageable).map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentDTO> byCustomer(Long customerId, Pageable pageable) {
        return paymentRepository.findByCustomer_Id(customerId, pageable).map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> duePremiums() {
        Map<Long, List<Payment>> byPolicy = paymentRepository.findAll().stream()
                .collect(Collectors.groupingBy(p -> p.getPolicy().getId()));
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        return byPolicy.entrySet().stream()
                .filter(e -> e.getValue().stream()
                        .noneMatch(p -> p.getType() == PaymentType.PREMIUM
                                && p.getStatus() == PaymentStatus.SUCCESS
                                && p.getCreatedAt().isAfter(cutoff)))
                .map(e -> new Object[]{e.getKey(), e.getValue().get(0).getPolicy().getCustomer().getId()})
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> monthlyReport(int year) {
        List<Payment> all = paymentRepository.findAll();
        return all.stream()
                .filter(p -> p.getCreatedAt() != null && p.getCreatedAt().getYear() == year)
                .collect(Collectors.groupingBy(p -> p.getCreatedAt().getMonthValue()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    int month = e.getKey();
                    long total = e.getValue().size();
                    double sum = e.getValue().stream().mapToDouble(Payment::getAmount).sum();
                    long success = e.getValue().stream().filter(p -> p.getStatus() == PaymentStatus.SUCCESS).count();
                    return new Object[]{month, total, success, sum};
                }).collect(Collectors.toList());
    }
}
