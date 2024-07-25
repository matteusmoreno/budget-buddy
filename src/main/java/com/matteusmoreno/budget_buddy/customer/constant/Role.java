package com.matteusmoreno.budget_buddy.customer.constant;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("Admin"),
    CUSTOMER("Customer");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }
}
