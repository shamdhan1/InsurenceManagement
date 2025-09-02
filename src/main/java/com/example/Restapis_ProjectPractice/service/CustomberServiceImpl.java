package com.example.Restapis_ProjectPractice.service;

import com.example.Restapis_ProjectPractice.dto.CustomerDTO;
import com.example.Restapis_ProjectPractice.entity.Customer;
import com.example.Restapis_ProjectPractice.exception.BadRequestException;
import com.example.Restapis_ProjectPractice.exception.ResourceNotFoundException;
import com.example.Restapis_ProjectPractice.repository.CustomerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CustomberServiceImpl implements CustomberService{

    private final CustomerRepository customerRepository;

    public CustomberServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    private CustomerDTO maptoDTO(Customer customer){
        return CustomerDTO.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .age(customer.getAge())
                .phoneNumber(customer.getPhoneNumber())
                .status(customer.getStatus())
                .build();
    }

    private Customer mapToEntity(CustomerDTO dto){
        return Customer.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .age(dto.getAge())
                .phoneNumber(dto.getPhoneNumber())
                .status(dto.getStatus())
                .build();
    }


    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        customer.setFullName(dto.getFullName());
        customer.setAge(dto.getAge());
        customer.setPhoneNumber(dto.getPhoneNumber());
        return maptoDTO(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found");
        }
        customerRepository.deleteById(id);
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO dto) {

        if (customerRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new BadRequestException("Email already exists!");
        }

        Customer customer = mapToEntity(dto);
        customer.setStatus("ACTIVE");
        return maptoDTO(customerRepository.save(customer));
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Customer not found"));
        return maptoDTO(customer);
    }

    @Override
    public List<CustomerDTO> getAllCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return customerRepository.findAll(pageable).stream()
                .map(this::maptoDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO updateCustomerStatus(Long id, String status) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        customer.setStatus(status);
        return maptoDTO(customerRepository.save(customer));
    }

    @Override
    public List<CustomerDTO> getPoliciesByCustomer(Long customerId) {
        return List.of();
    }


    @Override
    public List<CustomerDTO> getClaimsByCustomer(Long customerId) {
        return List.of();
    }

    @Override
    public List<CustomerDTO> getPaymentsByCustomer(Long customerId) {
        return List.of();
    }

    @Override
    public CustomerDTO searchCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("EMail not found"));
        return maptoDTO(customerRepository.save(customer));
    }
}
