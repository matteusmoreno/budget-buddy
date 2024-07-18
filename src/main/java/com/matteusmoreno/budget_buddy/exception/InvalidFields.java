package com.matteusmoreno.budget_buddy.exception;

import org.springframework.validation.FieldError;

public record InvalidFields(
        String field,
        String message) {
    public InvalidFields(FieldError error) {
        this(error.getField(), error.getDefaultMessage());
    }
}
