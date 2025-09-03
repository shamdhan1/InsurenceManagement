package com.example.Restapis_ProjectPractice.repository;

import com.example.Restapis_ProjectPractice.entity.ClaimDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClaimDocumentRepository extends JpaRepository<ClaimDocument,Long> {

    List<ClaimDocument> findByClaim_Id(Long claimId);
}
