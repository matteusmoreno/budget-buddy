package com.matteusmoreno.budget_buddy.address;

import com.matteusmoreno.budget_buddy.address.entity.Address;
import com.matteusmoreno.budget_buddy.address.repository.AddressRepository;
import com.matteusmoreno.budget_buddy.client.ViaCepClient;
import com.matteusmoreno.budget_buddy.client.ViaCepResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final ViaCepClient viaCepClient;

    @Autowired
    public AddressService(AddressRepository addressRepository, ViaCepClient viaCepClient) {
        this.addressRepository = addressRepository;
        this.viaCepClient = viaCepClient;
    }

    @Transactional
    public Address setAddressAttributes(String zipcode) {
        if (addressRepository.existsByZipcode(zipcode)) {
            return addressRepository.findByZipcode(zipcode);
        }

        ViaCepResponse viaCepResponse = viaCepClient.getAddressByZipcode(zipcode);

        return new Address(viaCepResponse);
    }
}
