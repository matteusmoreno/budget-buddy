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
    private String neighborhood;
    private String city;
    private String state;

    public Address(ViaCepResponse viaCepResponse) {
        this.zipcode = viaCepResponse.cep();
        this.street = viaCepResponse.logradouro();
        this.neighborhood = viaCepResponse.bairro();
        this.city = viaCepResponse.localidade();
        this.state = viaCepResponse.uf();

    }
}
