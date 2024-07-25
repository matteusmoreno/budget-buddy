package com.matteusmoreno.budget_buddy.customer.service;

import com.matteusmoreno.budget_buddy.address.entity.Address;
import com.matteusmoreno.budget_buddy.address.service.AddressService;
import com.matteusmoreno.budget_buddy.card.request.UpdateCardRequest;
import com.matteusmoreno.budget_buddy.customer.CustomerRepository;
import com.matteusmoreno.budget_buddy.customer.constant.Country;
import com.matteusmoreno.budget_buddy.customer.constant.Role;
import com.matteusmoreno.budget_buddy.customer.entity.Customer;
import com.matteusmoreno.budget_buddy.customer.request.CreateCustomerRequest;
import com.matteusmoreno.budget_buddy.customer.request.UpdateCustomerRequest;
import com.matteusmoreno.budget_buddy.utils.AppUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Customer Unit Tests")
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AddressService addressService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private AppUtils appUtils;

    @InjectMocks
    private CustomerService customerService;

    private final Address address = new Address(1L, "city", "neighborhood", "state", "street", "zipcode");

    private final Customer customer = new Customer(UUID.randomUUID(), "matteusmoreno", "senha123", "Matteus Moreno",
            "matteus@email.com", "(22)999999999", "999.999.999-99", address, Country.BRAZIL, null, BigDecimal.ZERO, Role.CUSTOMER, null, null, null, true);

    @Test
    @DisplayName("Should create a new customer and save in database")
    void shouldCreateNewCustomerAndSaveInDatabase() {
        CreateCustomerRequest request = new CreateCustomerRequest("username", "password", "Name",
                "email@email.com", "(22)999999999", "999.999.999-99", "99999-999", Country.BRAZIL);

        when(addressService.setAddressAttributes(request.zipcode())).thenReturn(address);
        when(bCryptPasswordEncoder.encode(request.password())).thenReturn("passwordEncoded");

        Customer result = customerService.createCustomer(request);

        verify(addressService, times(1)).setAddressAttributes(request.zipcode());
        verify(bCryptPasswordEncoder, times(1)).encode(request.password());
        verify(customerRepository, times(1)).save(result);

        assertEquals(request.username(), result.getUsername());
        assertEquals("passwordEncoded", result.getPassword());
        assertEquals(request.name(), result.getName());
        assertEquals(request.email(), result.getEmail());
        assertEquals(request.phone(), result.getPhone());
        assertEquals(request.cpf(), result.getCpf());
        assertEquals(address, result.getAddress());
        assertEquals(request.country(), result.getCountry());
        assertArrayEquals(new String[]{}, result.getCards().toArray());
        assertTrue(result.getActive());
    }

    @Test
    @DisplayName("Should detail authenticated customer")
    void shouldDetailAuthenticatedCustomer() {

        when(appUtils.getAuthenticatedUser()).thenReturn(customer);

        Customer result = customerService.getCustomer();

        verify(appUtils, times(1)).getAuthenticatedUser();

        assertEquals(customer, result);
    }

    @Test
    @DisplayName("Should update authenticated customer")
    void shouldUpdateAuthenticatedCustomer() {
        Address newAddress = new Address();
        UpdateCustomerRequest request = new UpdateCustomerRequest("updatedusername", "updatedpassword",
                "Updated Name", "updatedemail@email.com", "(88)888888888", "000.000.000-00", "11111-111", Country.INDONESIA);

        when(appUtils.getAuthenticatedUser()).thenReturn(customer);
        when(addressService.setAddressAttributes(request.zipcode())).thenReturn(newAddress);
        when(bCryptPasswordEncoder.encode(request.password())).thenReturn("updatedEncodedPassword");

        Customer result = customerService.updateCustomer(request);

        verify(appUtils, times(1)).getAuthenticatedUser();
        verify(addressService, times(1)).setAddressAttributes(request.zipcode());
        verify(bCryptPasswordEncoder, times(1)).encode(request.password());
        verify(customerRepository, times(1)).save(result);

        assertEquals(request.username(), result.getUsername());
        assertEquals("updatedEncodedPassword", result.getPassword());
        assertEquals(request.name(), result.getName());
        assertEquals(request.email(), result.getEmail());
        assertEquals(request.phone(), result.getPhone());
        assertEquals(request.cpf(), result.getCpf());
        assertEquals(newAddress, result.getAddress());
        assertEquals(request.country(), result.getCountry());
    }

    @Test
    @DisplayName("Should disable authenticated customer")
    void shouldDisableAuthenticatedCustomer() {

        when(appUtils.getAuthenticatedUser()).thenReturn(customer);

        customerService.disableCustomer();

        verify(appUtils, times(1)).getAuthenticatedUser();
        verify(customerRepository, times(1)).save(customer);

        assertFalse(customer.getActive());
    }

    @Test
    @DisplayName("Should enable authenticated customer")
    void shouldEnableAuthenticatedCustomer() {
        customer.setActive(false);

        when(appUtils.getAuthenticatedUser()).thenReturn(customer);

        customerService.enableCustomer();

        verify(appUtils, times(1)).getAuthenticatedUser();
        verify(customerRepository, times(1)).save(customer);

        assertTrue(customer.getActive());
        assertNull(customer.getDeletedAt());
    }
}