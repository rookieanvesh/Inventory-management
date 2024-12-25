package com.inventory.exceptions;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String message;
    private String error;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }
}