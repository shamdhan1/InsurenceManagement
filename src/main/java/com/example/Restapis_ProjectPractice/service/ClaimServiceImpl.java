package com.example.Restapis_ProjectPractice.service;

import com.example.Restapis_ProjectPractice.dto.ClaimDTO;
import com.example.Restapis_ProjectPractice.dto.ClaimDocumentDTO;
import com.example.Restapis_ProjectPractice.entity.*;
import com.example.Restapis_ProjectPractice.exception.BadRequestException;
import com.example.Restapis_ProjectPractice.exception.ResourceNotFoundException;
import com.example.Restapis_ProjectPractice.repository.ClaimDocumentRepository;
import com.example.Restapis_ProjectPractice.repository.ClaimRepository;
import com.example.Restapis_ProjectPractice.repository.CustomerRepository;
import com.example.Restapis_ProjectPractice.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClaimServiceImpl implements ClaimService{

    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;
    private final CustomerRepository customerRepository;
    private final ClaimDocumentRepository documentRepository;

    public ClaimServiceImpl(ClaimRepository claimRepository,
                            PolicyRepository policyRepository,
                            CustomerRepository customerRepository,
                            ClaimDocumentRepository documentRepository) {
        this.claimRepository = claimRepository;
        this.policyRepository = policyRepository;
        this.customerRepository = customerRepository;
        this.documentRepository = documentRepository;
    }

    @Value("${app.upload.dir:/tmp/uploads}")
    private String uploadDir;


    private ClaimDTO toDTO(Claim c) {
        return ClaimDTO.builder()
                .id(c.getId())
                .claimNumber(c.getClaimNumber())
                .policyId(c.getPolicy().getId())
                .customerId(c.getCustomer().getId())
                .status(c.getStatus())
                .claimedAmount(c.getClaimedAmount())
                .reason(c.getReason())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }


    private ClaimDocumentDTO toDTO(ClaimDocument d) {
        return ClaimDocumentDTO.builder()
                .id(d.getId())
                .claimId(d.getClaim().getId())
                .fileName(d.getFileName())
                .contentType(d.getContentType())
                .storagePath(d.getStoragePath())
                .uploadedAt(d.getUploadedAt())
                .build();
    }



    @Override
    public ClaimDTO fileClaim(Long policyId, Double claimedAmount, String reason) {
        if (claimedAmount == null || claimedAmount <= 0)
            throw new BadRequestException("claimedAmount must be > 0");

        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

        if (!"ACTIVE".equalsIgnoreCase(policy.getStatus()))
            throw new BadRequestException("Claim allowed only for ACTIVE policy");

        Customer customer = policy.getCustomer();

        String claimNo = "CLM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Claim claim = Claim.builder()
                .claimNumber(claimNo)
                .policy(policy)
                .customer(customer)
                .status(ClaimStatus.PENDING)
                .claimedAmount(claimedAmount)
                .reason(reason)
                .build();

        return toDTO(claimRepository.save(claim));
    }

    @Override
    @Transactional(readOnly = true)
    public ClaimDTO getById(Long id) {
        return toDTO(
                claimRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Claim not found"))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClaimDTO> list(ClaimStatus status, LocalDate from, LocalDate to, Pageable pageable) {
        if (status != null) {
            return claimRepository.findByStatus(status, pageable).map(this::toDTO);
        }
        if (from != null && to != null) {
            return claimRepository
                    .findByCreatedAtBetween(from.atStartOfDay(), to.atTime(23, 59, 59), pageable)
                    .map(this::toDTO);
        }
        return claimRepository.findAll(pageable).map(this::toDTO);
    }

    @Override
    public ClaimDTO update(Long id, Double claimedAmount, String reason) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        if (claim.getStatus() != ClaimStatus.PENDING)
            throw new BadRequestException("Only PENDING claims can be updated");

        if (claimedAmount != null && claimedAmount > 0) claim.setClaimedAmount(claimedAmount);
        if (reason != null && !reason.isBlank()) claim.setReason(reason);

        return toDTO(claimRepository.save(claim));
    }

    @Override
    public void withdraw(Long id) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
        if (claim.getStatus() != ClaimStatus.PENDING)
            throw new BadRequestException("Only PENDING claims can be withdrawn");
        claim.setStatus(ClaimStatus.WITHDRAWN);
        claimRepository.save(claim);

    }

    @Override
    public ClaimDTO updateStatus(Long id, ClaimStatus status) {
        if (status == null) throw new BadRequestException("status is required");
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
        if (claim.getStatus() == ClaimStatus.WITHDRAWN)
            throw new BadRequestException("Withdrawn claim cannot change status");
        if (claim.getStatus() != ClaimStatus.PENDING)
            throw new BadRequestException("Only PENDING → APPROVED/REJECTED allowed");

        if (status == ClaimStatus.PENDING)
            throw new BadRequestException("Cannot move back to PENDING");

        claim.setStatus(status);
        return toDTO(claimRepository.save(claim));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClaimDTO> searchByPolicy(Long policyId, Pageable pageable) {
        return claimRepository.findByPolicy_Id(policyId, pageable).map(this::toDTO);
    }

    @Override
    public List<ClaimDocumentDTO> uploadDocuments(Long claimId, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) throw new BadRequestException("No files to upload");
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        try {
            Path base = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(base);

            List<ClaimDocument> toSave = new ArrayList<>();
            for (MultipartFile f : files) {
                String stored = claim.getClaimNumber() + "_" + System.currentTimeMillis() + "_" + f.getOriginalFilename();
                Path target = base.resolve(stored);
                Files.copy(f.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

                toSave.add(ClaimDocument.builder()
                        .claim(claim)
                        .fileName(f.getOriginalFilename())
                        .contentType(f.getContentType())
                        .storagePath(target.toString())
                        .build());
            }
            return documentRepository.saveAll(toSave).stream().map(this::toDTO).collect(Collectors.toList());
        } catch (Exception ex) {
            throw new BadRequestException("Failed to upload files: " + ex.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClaimDocumentDTO> listDocuments(Long claimId) {
        claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
        return documentRepository.findByClaim_Id(claimId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> monthlyReport(int year) {
        List<Claim> all = claimRepository.findAll();
        return all.stream()
                .filter(c -> c.getCreatedAt() != null && c.getCreatedAt().getYear() == year)
                .collect(Collectors.groupingBy(c -> c.getCreatedAt().getMonthValue()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    int month = e.getKey();
                    long total = e.getValue().size();
                    long approved = e.getValue().stream().filter(c -> c.getStatus() == ClaimStatus.APPROVED).count();
                    long rejected = e.getValue().stream().filter(c -> c.getStatus() == ClaimStatus.REJECTED).count();
                    double amount = e.getValue().stream().mapToDouble(Claim::getClaimedAmount).sum();
                    return new Object[]{month, total, approved, rejected, amount};
                }).collect(Collectors.toList());
    }


}
