package com.matteusmoreno.budget_buddy.client;

public record ViaCepResponse(
        String cep,
        String logradouro,
        String bairro,
        String localidade,
        String uf) {
}
