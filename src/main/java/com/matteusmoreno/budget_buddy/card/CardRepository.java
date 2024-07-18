package com.matteusmoreno.budget_buddy.card;

import com.matteusmoreno.budget_buddy.card.entity.Card;
import com.matteusmoreno.budget_buddy.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

    boolean existsByNumber(String number);

    Card findByNumber(String number);
}
