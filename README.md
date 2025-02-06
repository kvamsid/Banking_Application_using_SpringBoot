# Banking Application using SpringBoot

## Project Overview
This Spring Boot application provides a comprehensive banking platform designed to manage bank accounts and perform essential banking operations efficiently. It supports functionalities such as account creation, deletion, deposits, withdrawals, fund transfers, and transaction history retrieval.

## Features

### Account Management
- **Create Account**: Initiate a new bank account.
- **Get Account**: Retrieve details of a specific account.
- **Get All Accounts**: List all existing accounts.
- **Withdraw Amount**: Withdraw funds from an account.
- **Deposit Amount**: Deposit funds into an account.
- **Delete Account**: Remove an existing account.

### Fund Transfers
- **Transfer Funds**: Move funds between accounts securely and efficiently using the `transfer` API.

### Transaction History
- **Log Transactions**: Automatically log every deposit, withdrawal, and transfer operation.
- **Fetch Transaction History**: Retrieve a detailed transaction history for any account.

## Exception Handling
Robust exception management using `@ControllerAdvice` and `@ExceptionHandler` ensures that the API gracefully handles and responds to various error conditions.

## Technologies Used
- **Spring Boot**: Framework for creating stand-alone, production-grade Spring-based applications.
- **Java**: Primary programming language.
- **Postman**: Tool used for testing the API endpoints.

## License
Distributed under the MIT License. See `LICENSE` for more information.
