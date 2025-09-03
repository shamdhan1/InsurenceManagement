package com.example.Restapis_ProjectPractice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "claims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Claim {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique = true, updatable = false)
    private String claimNumber;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id")
    private Policy policy;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private ClaimStatus status; // PENDING, APPROVED, REJECTED, WITHDRAWN

    @Column(nullable=false)
    private Double claimedAmount;

    private String reason; // accident, hospitalization, etc.

    @Column(nullable=false, updatable=false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Version
    private Long version;

    @PrePersist
    void prePersist() { createdAt = LocalDateTime.now(); updatedAt = createdAt; }

    @PreUpdate
    void preUpdate() { updatedAt = LocalDateTime.now();

    }


}
