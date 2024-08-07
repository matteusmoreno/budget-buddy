package com.matteusmoreno.budget_buddy.customer.service;

import com.matteusmoreno.budget_buddy.address.service.AddressService;
import com.matteusmoreno.budget_buddy.address.entity.Address;
import com.matteusmoreno.budget_buddy.customer.CustomerRepository;
import com.matteusmoreno.budget_buddy.customer.constant.Role;
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

        customer.setRole(Role.CUSTOMER);
        customer.setAddress(address);
        customer.setActive(true);
        customer.setPassword(passwordEncoder.encode(request.password()));

        customerRepository.save(customer);

        return customer;
    }

    public Customer getCustomer() {
        return appUtils.getAuthenticatedUser();
    }

    @Transactional
    public Customer updateCustomer(UpdateCustomerRequest request) {
        Customer customer = appUtils.getAuthenticatedUser();

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
    public void disableCustomer() {
        Customer customer = appUtils.getAuthenticatedUser();

        customer.setActive(false);
        customer.setDeletedAt(LocalDateTime.now());

        customerRepository.save(customer);
    }

    @Transactional
    public Customer enableCustomer() {
        Customer customer = appUtils.getAuthenticatedUser();

        customer.setActive(true);
        customer.setDeletedAt(null);

        customerRepository.save(customer);

        return customer;
    }
}
