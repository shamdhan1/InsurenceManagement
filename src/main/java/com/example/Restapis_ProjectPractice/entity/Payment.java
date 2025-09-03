package com.example.Restapis_ProjectPractice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, updatable=false)
    private String transactionRef;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private PaymentType type; // PREMIUM or CLAIM_SETTLEMENT

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private PaymentStatus status; // PENDING, SUCCESS, FAILED

    @Column(nullable=false)
    private Double amount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id")
    private Policy policy;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Column(nullable=false, updatable=false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Version
    private Long version;

    @PrePersist
    void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt; status = status == null ? PaymentStatus.PENDING : status;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }


}
