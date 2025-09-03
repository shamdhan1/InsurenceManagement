package com.example.Restapis_ProjectPractice.controller;

import com.example.Restapis_ProjectPractice.dto.AgentDTO;
import com.example.Restapis_ProjectPractice.dto.PolicyDTO;
import com.example.Restapis_ProjectPractice.service.AgentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agents")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    @PostMapping
    @Operation(summary = "Register new agent")
    public ResponseEntity<AgentDTO> create(@RequestBody AgentDTO dto) {
        return new ResponseEntity<>(agentService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgentDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(agentService.getById(id));
    }
    @GetMapping
    public ResponseEntity<Page<AgentDTO>> list(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(agentService.list(pageable));
    }

    @GetMapping("/{id}/policies")
    public ResponseEntity<List<PolicyDTO>> policies(@PathVariable Long id) {
        return ResponseEntity.ok(agentService.policies(id));
    }

    @GetMapping("/{id}/commission")
    public ResponseEntity<Object[]> commission(@PathVariable Long id) {
        return ResponseEntity.ok(agentService.commissionReport(id));
    }

}
