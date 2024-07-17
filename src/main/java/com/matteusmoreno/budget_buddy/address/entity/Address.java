package com.matteusmoreno.budget_buddy.address.entity;

import com.matteusmoreno.budget_buddy.client.ViaCepResponse;
import com.matteusmoreno.budget_buddy.customer.request.CreateCustomerRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Address {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String zipcode;
    private String street;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
    private String country;

    public Address(ViaCepResponse viaCepResponse, String number,  String country) {
        this.zipcode = viaCepResponse.cep();
        this.street = viaCepResponse.logradouro();
        this.number = number;
        this.neighborhood = viaCepResponse.bairro();
        this.city = viaCepResponse.localidade();
        this.state = viaCepResponse.uf();
        this.country = country;

    }
}
