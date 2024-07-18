package com.matteusmoreno.budget_buddy.card.service;

import com.matteusmoreno.budget_buddy.card.CardRepository;
import com.matteusmoreno.budget_buddy.card.entity.Card;
import com.matteusmoreno.budget_buddy.card.request.CreateCardRequest;
import com.matteusmoreno.budget_buddy.card.request.UpdateCardRequest;
import com.matteusmoreno.budget_buddy.customer.CustomerRepository;
import com.matteusmoreno.budget_buddy.customer.entity.Customer;
import com.matteusmoreno.budget_buddy.utils.AppUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CustomerRepository customerRepository;
    private final AppUtils appUtils;

    @Autowired
    public CardService(CardRepository cardRepository, CustomerRepository customerRepository, AppUtils appUtils) {
        this.cardRepository = cardRepository;
        this.customerRepository = customerRepository;
        this.appUtils = appUtils;
    }

    @Transactional
    public Card createCard(CreateCardRequest request) {
        Customer customer = appUtils.getAuthenticatedUser();

        if (cardRepository.existsByNumber(request.number())) {
            Card card = cardRepository.findByNumber(request.number());
            customer.getCards().add(card);
            return card;
        }

        Card card = new Card();
        BeanUtils.copyProperties(request, card);
        card.setName(request.name().toUpperCase());
        card.setActive(true);

        customer.getCards().add(card);

        cardRepository.save(card);
        customerRepository.save(customer);

        return card;
    }

    public List<Card> getCardsByCustomer() {
        Customer customer = appUtils.getAuthenticatedUser();
        return customer.getCards();
    }

    @Transactional
    public Card updateCard(UpdateCardRequest request) {
        Customer customer = appUtils.getAuthenticatedUser();
        Card card = cardRepository.findById(request.id()).orElseThrow();

        appUtils.verifyCustomerHasCard(customer, card);

        if (request.name() != null) {
            card.setName(request.name().toUpperCase());
        }
        if (request.number() != null) {
            card.setNumber(request.number());
        }
        if (request.cvv() != null) {
            card.setCvv(request.cvv());
        }
        if (request.expirationDate() != null) {
            card.setExpirationDate(request.expirationDate());
        }
        if (request.cardType() != null) {
            card.setCardType(request.cardType());
        }

        cardRepository.save(card);

        return card;
    }

    @Transactional
    public void disableCard(Long id) {
        Customer customer = appUtils.getAuthenticatedUser();
        Card card = cardRepository.findById(id).orElseThrow();

        appUtils.verifyCustomerHasCard(customer, card);

        card.setActive(false);
        card.setDeletedAt(LocalDateTime.now());
        cardRepository.save(card);

    }

    @Transactional
    public Card enableCard(Long id) {
        Customer customer = appUtils.getAuthenticatedUser();
        Card card = cardRepository.findById(id).orElseThrow();

        appUtils.verifyCustomerHasCard(customer, card);

        card.setActive(true);
        card.setDeletedAt(null);

        cardRepository.save(card);

        return card;
    }
}
