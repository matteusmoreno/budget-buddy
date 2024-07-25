package com.matteusmoreno.budget_buddy.login;

import com.matteusmoreno.budget_buddy.customer.CustomerRepository;
import com.matteusmoreno.budget_buddy.customer.constant.Role;
import com.matteusmoreno.budget_buddy.customer.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;

@Service
public class LoginService {

    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;

    @Autowired
    public LoginService(JwtEncoder jwtEncoder, BCryptPasswordEncoder passwordEncoder, CustomerRepository customerRepository) {
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public String login(LoginRequest request) {
        Customer customer = customerRepository.findByUsername(request.username());

        if (!customerRepository.existsByUsername(request.username()) || isLoginCorrect(request, passwordEncoder, customer)) {
            throw new BadCredentialsException("User or Password is invalid!");
        }

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("BudgetBuddy")
                .subject(customer.getUsername())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(600L))
                .claim("userId", customer.getId())
                .claim("name", customer.getName())
                .claim("scope", customer.getRole())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private boolean isLoginCorrect(LoginRequest request, PasswordEncoder passwordEncoder, Customer customer) {
        if (Objects.equals(customer.getUsername(), "admin")) return false;
        return !passwordEncoder.matches(request.password(), customer.getPassword());
    }

}
