package com.example.Restapis_ProjectPractice.repository;

import com.example.Restapis_ProjectPractice.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent,Long> {
    Optional<Agent> findByEmail(String email);
}
