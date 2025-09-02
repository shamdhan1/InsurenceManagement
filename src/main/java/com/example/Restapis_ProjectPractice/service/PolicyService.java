package com.example.Restapis_ProjectPractice.service;

import com.example.Restapis_ProjectPractice.entity.Claim;
import com.example.Restapis_ProjectPractice.entity.Endorsement;
import com.example.Restapis_ProjectPractice.entity.Policy;

import java.util.List;

public interface PolicyService {


    Policy createPolicy(Long customerId, Policy policy);
    Policy getPolicyById(Long id);
    List<Policy> getAllPolicies();
    Policy updatePolicy(Long id, Policy policy);
    void deletePolicy(Long id);
    Endorsement addEndorsement(Long policyId, Endorsement endorsement);

    List<Endorsement> getEndorsements(Long policyId);

    Policy updatePolicyStatus(Long policyId, String status);

    List<Policy> searchPolicies(Long customerId, String type);

    Policy renewPolicy(Long policyId);

    List<Policy> getExpiringPolicies();

    List<Claim> getPolicyClaims(Long policyId);
}
