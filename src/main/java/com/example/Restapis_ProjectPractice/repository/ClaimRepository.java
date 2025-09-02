package com.example.Restapis_ProjectPractice.repository;

import com.example.Restapis_ProjectPractice.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim,Long> {
    List<Claim> findByPolicyId(Long policyId);
}
