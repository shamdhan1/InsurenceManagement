package com.example.Restapis_ProjectPractice.service;

import com.example.Restapis_ProjectPractice.dto.CustomerDTO;

import java.util.List;

public interface CustomberService {

    CustomerDTO createCustomer(CustomerDTO dto);
    CustomerDTO getCustomerById(Long id);
    List<CustomerDTO> getAllCustomers(int page, int size);
    CustomerDTO updateCustomer(Long id, CustomerDTO dto);
    void deleteCustomer(Long id);
    List<CustomerDTO> getPoliciesByCustomer(Long customerId); // dummy
    List<CustomerDTO> getClaimsByCustomer(Long customerId);   // dummy
    List<CustomerDTO> getPaymentsByCustomer(Long customerId); // dummy
    CustomerDTO searchCustomerByEmail(String email);
    CustomerDTO updateCustomerStatus(Long id, String status);
}
