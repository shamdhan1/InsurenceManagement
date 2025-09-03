package com.example.Restapis_ProjectPractice.dto;

import com.example.Restapis_ProjectPractice.entity.PaymentStatus;
import com.example.Restapis_ProjectPractice.entity.PaymentType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Long id;
    private String transactionRef;
    private PaymentType type;
    private PaymentStatus status;
    private Double amount;
    private Long policyId;
    private Long customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
