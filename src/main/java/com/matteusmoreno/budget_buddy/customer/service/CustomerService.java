package com.matteusmoreno.budget_buddy.customer.service;

import com.matteusmoreno.budget_buddy.address.AddressService;
import com.matteusmoreno.budget_buddy.address.entity.Address;
import com.matteusmoreno.budget_buddy.customer.CustomerRepository;
import com.matteusmoreno.budget_buddy.customer.entity.Customer;
import com.matteusmoreno.budget_buddy.customer.request.CreateCustomerRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Address address = addressService.setAddressAttributes(request.zipcode(), request.number(), request.country());

        customer.setAddress(address);
        customer.setActive(true);

        customerRepository.save(customer);

        return customer;
    }
}
