package com.example.Restapis_ProjectPractice.controller;


import com.example.Restapis_ProjectPractice.ClientApiResponse.ApiResponse;
import com.example.Restapis_ProjectPractice.dto.CustomerDTO;
import com.example.Restapis_ProjectPractice.service.CustomberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customber")
public class CustomerController {


    private final CustomberService customberService;

    public CustomerController(CustomberService customberService) {
        this.customberService = customberService;
    }


    @PostMapping
    public ResponseEntity<CustomerDTO> create(@RequestBody CustomerDTO customerDTO){
        return new ResponseEntity<>(customberService.createCustomer(customerDTO), HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getbyId(@PathVariable long id){
        return ResponseEntity.ok(customberService.getCustomerById(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAll( @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(customberService.getAllCustomers(page, size));
    }


    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateClient(@PathVariable Long id,
                                                    @RequestBody CustomerDTO customerDTO){
        return ResponseEntity.ok(customberService.updateCustomer(id, customerDTO));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        customberService.deleteCustomer(id);
        return ResponseEntity.ok(new ApiResponse(true, "Customer deleted successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<CustomerDTO> searchByEmail(@RequestParam String email) {
        return ResponseEntity.ok(customberService.searchCustomerByEmail(email));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CustomerDTO> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(customberService.updateCustomerStatus(id, status));
    }

    @GetMapping("/{id}/policies")
    public ResponseEntity<?> getPolicies(@PathVariable Long id) {
        return ResponseEntity.ok(customberService.getPoliciesByCustomer(id));
    }

    @GetMapping("/{id}/claims")
    public ResponseEntity<?> getClaims(@PathVariable Long id) {
        return ResponseEntity.ok(customberService.getClaimsByCustomer(id));
    }

    @GetMapping("/{id}/payments")
    public ResponseEntity<?> getPayments(@PathVariable Long id) {
        return ResponseEntity.ok(customberService.getPaymentsByCustomer(id));
    }


}
