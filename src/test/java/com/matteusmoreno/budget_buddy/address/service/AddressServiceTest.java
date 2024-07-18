package com.matteusmoreno.budget_buddy.address.service;

import com.matteusmoreno.budget_buddy.address.entity.Address;
import com.matteusmoreno.budget_buddy.address.repository.AddressRepository;
import com.matteusmoreno.budget_buddy.client.ViaCepClient;
import com.matteusmoreno.budget_buddy.client.ViaCepResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Address Service Unit Tests")
class AddressServiceTest {

    @Mock
    private ViaCepClient viaCepClient;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    @Test
    @DisplayName("Should consult the zipcode API and return an address")
    void shouldConsultZipcodeAPIAndReturnAnAddress() {

        ViaCepResponse viaCepResponse = new ViaCepResponse("28994675", "Rua Alfredo Menezes", "Bacaxá", "Saquarema", "RJ");

        when(viaCepClient.getAddressByZipcode("28994675")).thenReturn(viaCepResponse);

        Address result = addressService.setAddressAttributes("28994675");

        verify(viaCepClient, times(1)).getAddressByZipcode("28994675");

        assertEquals(viaCepResponse.cep(), result.getZipcode());
        assertEquals(viaCepResponse.logradouro(), result.getStreet());
        assertEquals(viaCepResponse.bairro(), result.getNeighborhood());
        assertEquals(viaCepResponse.localidade(), result.getCity());
        assertEquals(viaCepResponse.uf(), result.getState());
    }

    @Test
    @DisplayName("Should return the existing address from the database if it exists, without creating a duplicate")
    void shouldReturnExistingAddressIfPresentInDatabase() {

        Address address = new Address(10L, "28994675", "Rua Alfredo Menezes", "Bacaxá", "Saquarema", "RJ");

        when(addressRepository.existsByZipcode("28994675")).thenReturn(true);
        when(addressRepository.findByZipcode("28994675")).thenReturn(address);

        Address result = addressService.setAddressAttributes("28994675");

        verify(addressRepository, times(1)).existsByZipcode("28994675");

        assertEquals(address.getId(), result.getId());
        assertEquals(address.getZipcode(), result.getZipcode());
        assertEquals(address.getStreet(), result.getStreet());
        assertEquals(address.getNeighborhood(), result.getNeighborhood());
        assertEquals(address.getCity(), result.getCity());
        assertEquals(address.getState(), result.getState());

    }
}