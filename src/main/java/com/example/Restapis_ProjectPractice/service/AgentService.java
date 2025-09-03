package com.example.Restapis_ProjectPractice.service;

import com.example.Restapis_ProjectPractice.dto.AgentDTO;
import com.example.Restapis_ProjectPractice.dto.PolicyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AgentService {

    AgentDTO create(AgentDTO dto);
    AgentDTO getById(Long id);
    Page<AgentDTO> list(Pageable pageable);
    List<PolicyDTO> policies(Long agentId);
    Object[] commissionReport(Long agentId);
}
