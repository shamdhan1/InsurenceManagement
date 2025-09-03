package com.example.Restapis_ProjectPractice.service;

import com.example.Restapis_ProjectPractice.dto.ClaimDTO;
import com.example.Restapis_ProjectPractice.dto.ClaimDocumentDTO;
import com.example.Restapis_ProjectPractice.entity.ClaimStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface ClaimService {

    ClaimDTO fileClaim(Long policyId, Double claimedAmount, String reason);
    ClaimDTO getById(Long id);
    Page<ClaimDTO> list(ClaimStatus status, LocalDate from, LocalDate to, Pageable pageable);
    ClaimDTO update(Long id, Double claimedAmount, String reason);
    void withdraw(Long id);
    ClaimDTO updateStatus(Long id, ClaimStatus status);
    Page<ClaimDTO> searchByPolicy(Long policyId, Pageable pageable);

    List<ClaimDocumentDTO> uploadDocuments(Long claimId, List<MultipartFile> files);
    List<ClaimDocumentDTO> listDocuments(Long claimId);


    List<Object[]> monthlyReport(int year);
}
