package com.example.banking_app.service.impl;

import com.example.banking_app.dto.AccountDto;
import com.example.banking_app.dto.TransactionDto;
import com.example.banking_app.dto.TransactionType;
import com.example.banking_app.dto.TransferFundDto;
import com.example.banking_app.entity.Account;
import com.example.banking_app.entity.Transaction;
import com.example.banking_app.exception.AccountException;
import com.example.banking_app.mapper.AccountMapper;
import com.example.banking_app.repository.AccountRepository;
import com.example.banking_app.repository.TransactionRepository;
import com.example.banking_app.service.AccountService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {


    private AccountRepository accountRepository;

    private TransactionRepository transactionRepository;


    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account does not exist"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Long id, Double amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account does not exist"));
        Double total = account.getBalance() + amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);
        Transaction transaction = new Transaction();
        transaction.setAccountId(account.getId());
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, Double amount) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account does not exist"));
        Double total = account.getBalance() - amount;
        if(total < 0) {
            throw new RuntimeException("Funds not sufficient");
        }else{
            try{
                Transaction transaction = new Transaction();
                transaction.setAccountId(id);
                transaction.setAmount(amount);
                transaction.setTransactionType(TransactionType.WITHDRAW);
                transaction.setTimestamp(LocalDateTime.now());
                transactionRepository.save(transaction);
            }catch(Exception e){
                throw new RuntimeException("Error during withdrawal: " + e.getMessage(), e);
            }
            account.setBalance(total);
            Account savedAccount = accountRepository.save(account);
            return AccountMapper.mapToAccountDto(savedAccount);
        }
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map((account) -> AccountMapper.mapToAccountDto(account)).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountException("Account does not exist"));
        accountRepository.delete(account);
    }

    @Override
    public void transferFunds(TransferFundDto transferFundDto) {
        Account fromAccount = accountRepository.findById(transferFundDto.fromAccountId()).orElseThrow(()-> new AccountException("From Account does not exist"));
        Account toAccount = accountRepository.findById(transferFundDto.toAccountId()).orElseThrow(()-> new AccountException("To Account does not exist"));
        double amountLeft = fromAccount.getBalance() - transferFundDto.amount();
        if(amountLeft < 0){
            throw new RuntimeException("Funds not sufficient");
        }
        logTransaction(fromAccount.getId(), toAccount.getId(), transferFundDto.amount());
        fromAccount.setBalance(amountLeft);
        toAccount.setBalance(toAccount.getBalance() + transferFundDto.amount());
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    @Override
    public List<TransactionDto> getAccountTransactions(Long accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);
        return transactions.stream().map(this::convertEntitytoDto).collect(Collectors.toList());
    }

    private TransactionDto convertEntitytoDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto(transaction.getId(), transaction.getAccountId(), transaction.getAmount(), transaction.getTransactionType(), transaction.getTimestamp());
        return transactionDto;
    }

    private void logTransaction(Long fromAccountId, Long toAccountId, Double amount) {
        Transaction fromTransaction = new Transaction();
        fromTransaction.setAccountId(fromAccountId);
        fromTransaction.setAmount(amount);
        fromTransaction.setTransactionType(TransactionType.TRANSFER);
        fromTransaction.setTimestamp(LocalDateTime.now());
        Transaction toTransaction = new Transaction();
        toTransaction.setAccountId(toAccountId);
        toTransaction.setAmount(amount);
        toTransaction.setTransactionType(TransactionType.TRANSFER);
        toTransaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(fromTransaction);
        transactionRepository.save(toTransaction);
    }
}
