package com.matteusmoreno.budget_buddy.login;

import com.matteusmoreno.budget_buddy.address.entity.Address;
import com.matteusmoreno.budget_buddy.customer.CustomerRepository;
import com.matteusmoreno.budget_buddy.customer.constant.Country;
import com.matteusmoreno.budget_buddy.customer.entity.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private Jwt mockJwt;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private LoginService loginService;

    private final Address address = new Address(1L, "city", "neighborhood", "state", "street", "zipcode");

    private final Customer customer = new Customer(UUID.randomUUID(), "matteusmoreno", "senha123", "Matteus Moreno",
            "matteus@email.com", "(22)999999999", "999.999.999-99", address, Country.BRAZIL, null, null, null, null, true);

    @Test
    @DisplayName("Should Login user Correctly")
    void ShouldLoginUserCorrectly() {

        LoginRequest loginRequest = new LoginRequest("matteusmoreno", "senha123");

        when(customerRepository.findByUsername(loginRequest.username())).thenReturn(customer);
        when(customerRepository.existsByUsername(loginRequest.username())).thenReturn(true);
        when(passwordEncoder.matches(loginRequest.password(), customer.getPassword())).thenReturn(true);
        when(mockJwt.getTokenValue()).thenReturn("encoderResult");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(mockJwt);

        String jwtToken = loginService.login(loginRequest);

        assertNotNull(jwtToken);
    }

    @Test
    @DisplayName("Login fails with invalid credentials")
    void loginFailsWithInvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest("matteusmoreno", "wrongPassword");

        when(customerRepository.findByUsername(loginRequest.username())).thenReturn(customer);
        when(customerRepository.existsByUsername(loginRequest.username())).thenReturn(true);
        when(passwordEncoder.matches(loginRequest.password(), customer.getPassword())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));
    }

    @Test
    @DisplayName("Login Fails With Nonexistent User")
    void loginFailsWithNonexistentUser() {
        LoginRequest loginRequest = new LoginRequest("nonexistentUser", "password");

        when(customerRepository.existsByUsername(loginRequest.username())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));
    }

}