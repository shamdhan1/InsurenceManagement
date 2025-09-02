package com.example.Restapis_ProjectPractice.service;

import com.example.Restapis_ProjectPractice.entity.Claim;
import com.example.Restapis_ProjectPractice.entity.Customer;
import com.example.Restapis_ProjectPractice.entity.Endorsement;
import com.example.Restapis_ProjectPractice.entity.Policy;
import com.example.Restapis_ProjectPractice.exception.ResourceNotFoundException;
import com.example.Restapis_ProjectPractice.repository.ClaimRepository;
import com.example.Restapis_ProjectPractice.repository.CustomerRepository;
import com.example.Restapis_ProjectPractice.repository.EndorsementRepository;
import com.example.Restapis_ProjectPractice.repository.PolicyRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PolicyServiceImpl implements PolicyService{

    private final PolicyRepository policyRepository;
    private final CustomerRepository customerRepository;
    private final EndorsementRepository endorsementRepository;
    private final ClaimRepository claimRepository;

    public PolicyServiceImpl(PolicyRepository policyRepository, CustomerRepository customerRepository,
                             EndorsementRepository endorsementRepository, ClaimRepository claimRepository) {
        this.policyRepository = policyRepository;
        this.customerRepository = customerRepository;
        this.endorsementRepository = endorsementRepository;
        this.claimRepository = claimRepository;
    }



    @Override
    public Policy createPolicy(Long customerId, Policy policy) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Client are not found"));
        policy.setCustomer(customer);
        policy.setStatus("ACTIVE");
        return policyRepository.save(policy);
    }

    @Override
    public Policy getPolicyById(Long id) {
        return policyRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Policy are not found"));
    }

    @Override
    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    @Override
    public Policy updatePolicy(Long id, Policy policy) {
        Policy existing = getPolicyById(id);
        existing.setType(policy.getType());
        existing.setSumAssured(policy.getSumAssured());
        existing.setEndDate(policy.getEndDate());
        return policyRepository.save(existing);
    }

    @Override
    public void deletePolicy(Long id) {
        policyRepository.deleteById(id);
    }

    @Override
    public Endorsement addEndorsement(Long policyId, Endorsement endorsement) {
        Policy policy = getPolicyById(policyId);
        endorsement.setPolicy(policy);
        return endorsementRepository.save(endorsement);
    }


    @Override
    public List<Endorsement> getEndorsements(Long policyId) {
        return List.of();
    }

    @Override
    public Policy updatePolicyStatus(Long policyId, String status) {
        return null;
    }

    @Override
    public List<Policy> searchPolicies(Long customerId, String type) {
        return List.of();
    }

    @Override
    public Policy renewPolicy(Long policyId) {
        return null;
    }

    @Override
    public List<Policy> getExpiringPolicies() {
        return List.of();
    }

    @Override
    public List<Claim> getPolicyClaims(Long policyId) {
        return List.of();
    }
}
