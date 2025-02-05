package com.example.banking_app.dto;

public record TransferFundDto(Long fromAccountId, Long toAccountId, Double amount) {
}
