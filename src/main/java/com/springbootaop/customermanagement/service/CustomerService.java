package com.springbootaop.customermanagement.service;

import com.springbootaop.customermanagement.model.Customer;
import com.springbootaop.customermanagement.repo.CustomerRepository;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    //ascending by default for Integer
    //ensures that all elements are unique.(doesn't allow duplicate.)
    private TreeSet<Integer> usedIds = new TreeSet<>();
    
    @PostConstruct //Specifies that the annotated method (init()) should run after the bean's properties have been initialized by Spring.
    public void init() {
        customerRepository.findAll().forEach(customer -> 
            usedIds.add(customer.getId()));
    }
    
    private Integer generateNextId() {
        int nextId = 1;
        
        // If there are existing IDs
        if (!usedIds.isEmpty()) {
            // Get the highest ID currently in use
            nextId = usedIds.last() + 1;
        }
        
        usedIds.add(nextId);
        return nextId;
    }
    
    
    @Transactional //For managing database transactions declaratively
    public Customer saveCustomer(Customer customer) {
        if (customer.getId() == null) {
            customer.setId(generateNextId());
        }
        return customerRepository.save(customer);
    }
    
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    /* Optional: container object. To reduce the risk of NullPointerException.
     * If a customer with the given ID exists, the Optional contains the Customer object.
     * If no customer is found, the Optional is empty.
     * */
    
    public Optional<Customer> getCustomerById(Integer id) {
        return customerRepository.findById(id);
    }
    
    @Transactional
    public void deleteCustomer(Integer id) {
        customerRepository.deleteById(id);
    }
    
    @Transactional
    public Customer updateCustomer(Customer customer) {
        if (customer.getId() == null) {
            throw new IllegalArgumentException("Customer ID cannot be null for update operation");
        }
        
        // Verify the customer exists
        if (!customerRepository.existsById(customer.getId())) {
            throw new IllegalArgumentException("Cannot update non-existent customer with ID: " + customer.getId());
        }
        
        return customerRepository.save(customer);
    }
}