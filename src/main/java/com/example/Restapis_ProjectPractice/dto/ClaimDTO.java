package com.example.Restapis_ProjectPractice.dto;

import com.example.Restapis_ProjectPractice.entity.ClaimStatus;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimDTO {
    private Long id;
    private String claimNumber;
    private Long policyId;
    private Long customerId;
    private ClaimStatus status;
    private Double claimedAmount;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
