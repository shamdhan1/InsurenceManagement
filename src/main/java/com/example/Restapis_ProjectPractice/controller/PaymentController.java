package com.example.Restapis_ProjectPractice.controller;


import com.example.Restapis_ProjectPractice.dto.PaymentDTO;
import com.example.Restapis_ProjectPractice.entity.PaymentStatus;
import com.example.Restapis_ProjectPractice.entity.PaymentType;
import com.example.Restapis_ProjectPractice.service.PaymentService;
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
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Make a payment for premium/claim settlement")
    @PostMapping
    public ResponseEntity<PaymentDTO> create(@RequestParam Long policyId,
                                             @RequestParam PaymentType type,
                                             @RequestParam Double amount) {
        return new ResponseEntity<>(paymentService.create(policyId, type, amount), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<PaymentDTO>> list(@RequestParam(required = false) PaymentType type,
                                                 @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(paymentService.list(type, pageable));
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentDTO> updateStatus(@PathVariable Long id,
                                                   @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(paymentService.updateStatus(id, status));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PaymentDTO>> byPolicy(@RequestParam(required = false) Long policyId,
                                                     @RequestParam(required = false) Long customerId,
                                                     @ParameterObject Pageable pageable) {
        if (policyId != null) return ResponseEntity.ok(paymentService.byPolicy(policyId, pageable));
        if (customerId != null) return ResponseEntity.ok(paymentService.byCustomer(customerId, pageable));
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/due")
    public ResponseEntity<List<Object[]>> due() {
        return ResponseEntity.ok(paymentService.duePremiums());
    }

    @GetMapping("/reports/monthly")
    public ResponseEntity<List<Object[]>> monthly(@RequestParam int year) {
        return ResponseEntity.ok(paymentService.monthlyReport(year));
    }

}
