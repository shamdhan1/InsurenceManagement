package com.example.Restapis_ProjectPractice.repository;

import com.example.Restapis_ProjectPractice.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy,Long> {

    List<Policy> findByCustomerId(Long customerId);
    List<Policy> findByTypeAndCustomerId(String type, Long customerId);
    List<Policy> findByEndDateBetween(LocalDate start, LocalDate end);
}
