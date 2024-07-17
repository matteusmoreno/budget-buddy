package com.matteusmoreno.budget_buddy.card.response;

import com.matteusmoreno.budget_buddy.card.constant.CardType;
import com.matteusmoreno.budget_buddy.card.entity.Card;

import java.time.LocalDateTime;

public record CardDetailsResponse(
        Long id,
        String name,
        String number,
        String cvv,
        String expirationDate,
        String cardType,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Boolean active) {

    public CardDetailsResponse(Card card) {
        this(card.getId(), card.getName(), card.getNumber(), card.getCvv(), card.getExpirationDate(), card.getCardType().getDisplayName(),
                card.getCreatedAt(), card.getUpdatedAt(), card.getDeletedAt(), card.getActive());
    }
}
