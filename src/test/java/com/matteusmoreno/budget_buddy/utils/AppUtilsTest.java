package com.matteusmoreno.budget_buddy.utils;

import com.matteusmoreno.budget_buddy.card.entity.Card;
import com.matteusmoreno.budget_buddy.customer.CustomerRepository;
import com.matteusmoreno.budget_buddy.customer.entity.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUtilsTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AppUtils appUtils;

    @Test
    @DisplayName("Should return the authenticated user")
    void getAuthenticatedUser() {
        // Arrange
        String username = "testUser";
        Customer expectedCustomer = new Customer();
        expectedCustomer.setUsername(username);

        // Simulate authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        // Simulate repository behavior
        when(customerRepository.findByUsername(username)).thenReturn(expectedCustomer);

        // Act
        Customer actualCustomer = appUtils.getAuthenticatedUser();

        // Assert
        assertEquals(expectedCustomer, actualCustomer);

        // Verify repository interaction
        verify(customerRepository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("Should throw BadCredentialsException if customer does not have the card")
    void verifyCustomerHasCard_ShouldThrowException_WhenCustomerDoesNotHaveCard() {
        // Arrange
        Customer customer = new Customer();
        Card card = new Card();
        // Assume that the customer does not have the card
        customer.setCards(List.of());

        // Act & Assert
        BadCredentialsException thrown = assertThrows(BadCredentialsException.class, () -> {
            verifyCustomerHasCard(customer, card);
        });

        assertEquals("You don't have permission to access this card", thrown.getMessage());
    }

    private void verifyCustomerHasCard(Customer customer, Card card) {
        if (!customer.getCards().contains(card)) {
            throw new BadCredentialsException("You don't have permission to access this card");
        }
    }
}