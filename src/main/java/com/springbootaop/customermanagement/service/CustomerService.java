package com.springbootaop.customermanagement.service;

import com.springbootaop.customermanagement.model.Customer;
import com.springbootaop.customermanagement.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Transactional
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
    
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    public Optional<Customer> getCustomerById(Integer id) {
        return customerRepository.findById(id);
    }
    
    @Transactional
    public void deleteCustomer(Integer id) {
        customerRepository.deleteById(id);
    }
    
    @Transactional
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}