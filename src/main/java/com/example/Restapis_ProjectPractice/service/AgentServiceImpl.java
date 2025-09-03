package com.example.Restapis_ProjectPractice.service;

import com.example.Restapis_ProjectPractice.dto.AgentDTO;
import com.example.Restapis_ProjectPractice.dto.PolicyDTO;
import com.example.Restapis_ProjectPractice.entity.*;
import com.example.Restapis_ProjectPractice.exception.BadRequestException;
import com.example.Restapis_ProjectPractice.exception.ResourceNotFoundException;
import com.example.Restapis_ProjectPractice.repository.AgentRepository;
import com.example.Restapis_ProjectPractice.repository.PaymentRepository;
import com.example.Restapis_ProjectPractice.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AgentServiceImpl implements AgentService{

    private final AgentRepository agentRepository;
    private final PolicyRepository policyRepository;
    private final PaymentRepository paymentRepository;

    private AgentDTO toDTO(Agent a) {
        return AgentDTO.builder()
                .id(a.getId()).fullName(a.getFullName())
                .email(a.getEmail()).phone(a.getPhone())
                .commissionRate(a.getCommissionRate())
                .build();
    }


    private PolicyDTO toDTO(Policy p) {
        return PolicyDTO.builder()
                .id(p.getId())
                .policyNumber(p.getPolicyNumber())
                .type(p.getType())
                .sumAssured(p.getSumAssured())
                .status(p.getStatus())
                .customerId(p.getCustomer().getId())
                .agentId(p.getAgent() != null ? p.getAgent().getId() : null)
                .startDate(p.getStartDate())
                .endDate(p.getEndDate())
                .build();
    }


    @Override
    public AgentDTO create(AgentDTO dto) {
        agentRepository.findByEmail(dto.getEmail()).ifPresent(a -> {
            throw new BadRequestException("Agent email already exists");
        });
        Agent a = agentRepository.save(Agent.builder()
                .fullName(dto.getFullName()).email(dto.getEmail())
                .phone(dto.getPhone())
                .commissionRate(dto.getCommissionRate() == null ? 0.05 : dto.getCommissionRate())
                .build());
        return toDTO(a);
    }

    @Override
    @Transactional(readOnly = true)
    public AgentDTO getById(Long id) {
        return toDTO(agentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Agent not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AgentDTO> list(Pageable pageable) {
        return agentRepository.findAll(pageable).map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyDTO> policies(Long agentId) {
        Agent a = agentRepository.findById(agentId).orElseThrow(() -> new ResourceNotFoundException("Agent not found"));
        return policyRepository.findByAgent_Id(a.getId()).stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Object[] commissionReport(Long agentId) {
        Agent a = agentRepository.findById(agentId).orElseThrow(() -> new ResourceNotFoundException("Agent not found"));
        List<Policy> policies = policyRepository.findByAgent_Id(agentId);
        long policyCount = policies.size();

        double premiumSum = paymentRepository.findAll().stream()
                .filter(p -> p.getPolicy().getAgent() != null && p.getPolicy().getAgent().getId().equals(agentId))
                .filter(p -> p.getType() == PaymentType.PREMIUM && p.getStatus() == PaymentStatus.SUCCESS)
                .mapToDouble(Payment::getAmount).sum();

        double estimatedCommission = premiumSum * (a.getCommissionRate() == null ? 0.05 : a.getCommissionRate());
        return new Object[]{agentId, policyCount, premiumSum, estimatedCommission};
    }
}
