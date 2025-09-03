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

import java.time.LocalDate;
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
        Policy policyById = getPolicyById(policyId);
        return policyById.getEndorsements();
    }

    @Override
    public Policy updatePolicyStatus(Long policyId, String status) {
        Policy policyById = getPolicyById(policyId);
        policyById.setStatus(status);
        return policyRepository.save(policyById);
    }

    @Override
    public List<Policy> searchPolicies(Long customerId, String type) {
        return policyRepository.findByTypeAndCustomerId(type,customerId);
    }

    @Override
    public Policy renewPolicy(Long policyId) {
        Policy policy = getPolicyById(policyId);
        policy.setEndDate(policy.getEndDate().plusYears(1));
        policy.setStatus("ACTIVE");
        return policyRepository.save(policy);
    }

    @Override
    public List<Policy> getExpiringPolicies() {
        LocalDate today = LocalDate.now();
        LocalDate next30 = today.plusDays(30);
        return policyRepository.findByEndDateBetween(today,next30);
    }

    @Override
    public List<Claim> getPolicyClaims(Long policyId) {
        return claimRepository.findByPolicy_Id(policyId);
    }

}
