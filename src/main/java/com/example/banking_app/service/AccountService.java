package com.example.banking_app.service;


import com.example.banking_app.dto.AccountDto;
import com.example.banking_app.dto.TransactionDto;
import com.example.banking_app.dto.TransferFundDto;

import java.util.List;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);

    AccountDto getAccountById(Long id);

    AccountDto deposit(Long id, Double amount);

    AccountDto withdraw(Long id, Double amount);

    List<AccountDto> getAllAccounts();

    void delete(Long id);

    void transferFunds(TransferFundDto transferFundDto);

    List<TransactionDto> getAccountTransactions(Long accountId);
}
