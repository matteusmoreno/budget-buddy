package com.matteusmoreno.budget_buddy.card.constant;

import lombok.Getter;

@Getter
public enum CardType {
    CREDIT("Credit"),
    DEBIT("Debit");

    private final String displayName;

    CardType(String displayName) {
        this.displayName = displayName;
    }
}
