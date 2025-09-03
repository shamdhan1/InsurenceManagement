package com.example.Restapis_ProjectPractice.controller;

import com.example.Restapis_ProjectPractice.dto.ClaimDTO;
import com.example.Restapis_ProjectPractice.dto.ClaimDocumentDTO;
import com.example.Restapis_ProjectPractice.entity.ClaimStatus;
import com.example.Restapis_ProjectPractice.service.ClaimService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/claims")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService claimService;


    @Operation(summary = "File a new claim for a policy")
    @PostMapping
    public ResponseEntity<ClaimDTO> file(@RequestParam Long policyId,
                                         @RequestParam Double claimedAmount,
                                         @RequestParam(required = false) String reason) {
        return new ResponseEntity<>(claimService.fileClaim(policyId, claimedAmount, reason), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ClaimDTO>> list(
            @RequestParam(required = false) ClaimStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(claimService.list(status, from, to, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClaimDTO> update(@PathVariable Long id,
                                           @RequestParam(required = false) Double claimedAmount,
                                           @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(claimService.update(id, claimedAmount, reason));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> withdraw(@PathVariable Long id) {
        claimService.withdraw(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ClaimDTO> updateStatus(@PathVariable Long id,
                                                 @RequestParam ClaimStatus status) {
        return ResponseEntity.ok(claimService.updateStatus(id, status));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ClaimDTO>> byPolicy(@RequestParam Long policyId,
                                                   @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(claimService.searchByPolicy(policyId, pageable));
    }

    @PostMapping("/{id}/documents")
    public ResponseEntity<List<ClaimDocumentDTO>> upload(@PathVariable Long id,
                                                         @RequestPart("files") List<MultipartFile> files) {
        return new ResponseEntity<>(claimService.uploadDocuments(id, files), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<List<ClaimDocumentDTO>> listDocs(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.listDocuments(id));
    }

    @GetMapping("/reports/monthly")
    public ResponseEntity<List<Object[]>> monthly(@RequestParam int year) {
        return ResponseEntity.ok(claimService.monthlyReport(year));
    }

}
