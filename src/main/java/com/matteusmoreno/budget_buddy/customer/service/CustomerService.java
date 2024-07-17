package com.matteusmoreno.budget_buddy.customer.service;

import com.matteusmoreno.budget_buddy.address.AddressService;
import com.matteusmoreno.budget_buddy.address.entity.Address;
import com.matteusmoreno.budget_buddy.address.repository.AddressRepository;
import com.matteusmoreno.budget_buddy.customer.CustomerRepository;
import com.matteusmoreno.budget_buddy.customer.entity.Customer;
import com.matteusmoreno.budget_buddy.customer.request.CreateCustomerRequest;
import com.matteusmoreno.budget_buddy.customer.request.UpdateCustomerRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressService addressService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, AddressService addressService) {
        this.customerRepository = customerRepository;
        this.addressService = addressService;
    }

    @Transactional
    public Customer createCustomer(CreateCustomerRequest request) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(request, customer);

        Address address = addressService.setAddressAttributes(request.zipcode());

        customer.setAddress(address);
        customer.setActive(true);

        customerRepository.save(customer);

        return customer;
    }

    public Customer getCustomerById(UUID id) {
        return customerRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Customer updateCustomer(UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(request.id()).orElseThrow();

        if (request.username() != null) {
            customer.setUsername(request.username());
        }
        if (request.password() != null) {
            customer.setPassword(request.password());
        }
        if (request.name() != null) {
            customer.setName(request.name());
        }
        if (request.email() != null) {
            customer.setEmail(request.email());
        }
        if (request.phone() != null) {
            customer.setPhone(request.phone());
        }
        if (request.cpf() != null) {
            customer.setCpf(request.cpf());
        }
        if (request.zipcode() != null) {
            customer.setAddress(addressService.setAddressAttributes(request.zipcode()));
        }
        if (request.country() != null) {
            customer.setCountry(request.country());
        }

        customerRepository.save(customer);

        return customer;
    }

    @Transactional
    public void disableCustomer(UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow();

        customer.setActive(false);
        customer.setDeletedAt(LocalDateTime.now());

        customerRepository.save(customer);
    }

    @Transactional
    public Customer enableCustomer(UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow();

        customer.setActive(true);
        customer.setDeletedAt(null);

        customerRepository.save(customer);

        return customer;
    }
}
