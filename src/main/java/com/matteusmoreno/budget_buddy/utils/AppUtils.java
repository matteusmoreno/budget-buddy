package com.matteusmoreno.budget_buddy.utils;

import com.matteusmoreno.budget_buddy.card.entity.Card;
import com.matteusmoreno.budget_buddy.customer.CustomerRepository;
import com.matteusmoreno.budget_buddy.customer.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AppUtils {

    private final CustomerRepository customerRepository;

    @Autowired
    public AppUtils(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return customerRepository.findByUsername(authentication.getName());
    }

    public void verifyAuthenticatedUser(Customer customer) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals(customer.getUsername())) {
            throw new BadCredentialsException("You can't access");
        }
    }

    public void verifyCustomerHasCard(Customer customer, Card card) {
        if (!customer.getCards().contains(card)) {
            throw new BadCredentialsException("You don't have permission to access this card");
        }
    }
}
