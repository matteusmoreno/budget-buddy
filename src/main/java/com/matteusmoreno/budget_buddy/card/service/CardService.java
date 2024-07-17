package com.matteusmoreno.budget_buddy.card.service;

import com.matteusmoreno.budget_buddy.card.CardRepository;
import com.matteusmoreno.budget_buddy.card.entity.Card;
import com.matteusmoreno.budget_buddy.card.request.CreateCardRequest;
import com.matteusmoreno.budget_buddy.customer.CustomerRepository;
import com.matteusmoreno.budget_buddy.customer.entity.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public CardService(CardRepository cardRepository, CustomerRepository customerRepository) {
        this.cardRepository = cardRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Card createCard(CreateCardRequest request, UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow();

        if (cardRepository.existsByNumber(request.number())) {
            Card card = cardRepository.findByNumber(request.number());
            customer.getCards().add(card);
            return card;
        }

        Card card = new Card();
        BeanUtils.copyProperties(request, card);
        card.setName(request.name().toUpperCase());
        card.setActive(true);

        cardRepository.save(card);
        customerRepository.save(customer);

        return card;
    }
}
