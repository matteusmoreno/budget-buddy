package com.matteusmoreno.budget_buddy.customer.controller;

import com.matteusmoreno.budget_buddy.customer.entity.Customer;
import com.matteusmoreno.budget_buddy.customer.request.CreateCustomerRequest;
import com.matteusmoreno.budget_buddy.customer.response.CustomerDetailsResponse;
import com.matteusmoreno.budget_buddy.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create")
    public ResponseEntity<CustomerDetailsResponse> create(@RequestBody @Valid CreateCustomerRequest request, UriComponentsBuilder uriBuilder) {
        Customer customer = customerService.createCustomer(request);
        URI uri = uriBuilder.path("/customers/create/{id}").buildAndExpand(customer.getId()).toUri();

        return ResponseEntity.created(uri).body(new CustomerDetailsResponse(customer));
    }

}
