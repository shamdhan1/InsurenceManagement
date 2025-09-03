package com.example.Restapis_ProjectPractice.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyDTO {
    private Long id;
    private String policyNumber;
    private String type;      // e.g., health, life, motor
    private String status;    // active, expired, cancelled
    private Double sumAssured;
    private LocalDate startDate;
    private LocalDate endDate;

    // Only IDs instead of full entities (best practice for DTOs)
    private Long customerId;
    private Long agentId;

    // To avoid recursion, send only IDs or minimal data of endorsements/claims
    private List<Long> endorsementIds;
    private List<Long> claimIds;
}
