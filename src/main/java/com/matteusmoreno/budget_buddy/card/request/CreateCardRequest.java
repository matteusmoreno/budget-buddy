package com.matteusmoreno.budget_buddy.card.request;

import com.matteusmoreno.budget_buddy.card.constant.CardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateCardRequest(
        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "Number is required")
        @Pattern(regexp = "^\\d{16}$", message = "Number must be a 16 digit number")
        String number,
        @NotBlank(message = "CVV is required")
        @Pattern(regexp = "^\\d{3}$", message = "CVV must be a 3 digit number")
        String cvv,
        @NotBlank(message = "Expiration date is required")
        @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$", message = "Expiration date must be in the format MM/YY")
        String expirationDate,
        @NotNull(message = "CardType date is required")
        CardType cardType) {
}
