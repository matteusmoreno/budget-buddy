package com.matteusmoreno.budget_buddy.customer.service;

import com.matteusmoreno.budget_buddy.address.service.AddressService;
import com.matteusmoreno.budget_buddy.address.entity.Address;
import com.matteusmoreno.budget_buddy.customer.CustomerRepository;
import com.matteusmoreno.budget_buddy.customer.entity.Customer;
import com.matteusmoreno.budget_buddy.customer.request.CreateCustomerRequest;
import com.matteusmoreno.budget_buddy.customer.request.UpdateCustomerRequest;
import com.matteusmoreno.budget_buddy.utils.AppUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressService addressService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AppUtils appUtils;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, AddressService addressService, BCryptPasswordEncoder passwordEncoder, AppUtils appUtils) {
        this.customerRepository = customerRepository;
        this.addressService = addressService;
        this.passwordEncoder = passwordEncoder;
        this.appUtils = appUtils;
    }

    @Transactional
    public Customer createCustomer(CreateCustomerRequest request) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(request, customer);

        Address address = addressService.setAddressAttributes(request.zipcode());

        customer.setAddress(address);
        customer.setActive(true);
        customer.setPassword(passwordEncoder.encode(request.password()));

        customerRepository.save(customer);

        return customer;
    }

    public Customer getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        appUtils.verifyAuthenticatedUser(customer);

        return customer;
    }

    @Transactional
    public Customer updateCustomer(UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(request.id()).orElseThrow();
        appUtils.verifyAuthenticatedUser(customer);

        if (request.username() != null) {
            customer.setUsername(request.username());
        }
        if (request.password() != null) {
            customer.setPassword(passwordEncoder.encode(request.password()));
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
        appUtils.verifyAuthenticatedUser(customer);

        customer.setActive(false);
        customer.setDeletedAt(LocalDateTime.now());

        customerRepository.save(customer);
    }

    @Transactional
    public Customer enableCustomer(UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        appUtils.verifyAuthenticatedUser(customer);

        customer.setActive(true);
        customer.setDeletedAt(null);

        customerRepository.save(customer);

        return customer;
    }
}
