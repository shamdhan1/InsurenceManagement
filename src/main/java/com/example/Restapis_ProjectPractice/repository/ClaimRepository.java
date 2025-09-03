package com.example.Restapis_ProjectPractice.repository;

import com.example.Restapis_ProjectPractice.entity.Claim;
import com.example.Restapis_ProjectPractice.entity.ClaimStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim,Long> {

    Page<Claim> findByStatus(ClaimStatus status, Pageable pageable);
    Page<Claim> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);
    Page<Claim> findByPolicy_Id(Long policyId, Pageable pageable);
    boolean existsByClaimNumber(String claimNumber);

    List<Claim> findByPolicy_Id(Long policyId);
}
