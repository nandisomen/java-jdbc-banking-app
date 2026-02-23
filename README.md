
# Banking Management System (JDBC)

A console-based banking management system developed using Core Java, JDBC, and MySQL.

## Features

* User registration
* User login authentication
* Create bank account
* Credit money
* Debit money (with balance validation)
* Check account balance (PIN verification)
* Money transfer with transaction management
* Commit and rollback support

## Technologies

* Java
* JDBC
* MySQL

## Key Concepts

* PreparedStatement (SQL Injection Prevention)
* ResultSet Processing
* CRUD Operations
* Transaction Management (commit & rollback)
* Exception Handling
* Menu-Driven Console Application

## Database Schema

Table: user

* email (Primary Key)
* full_name
* password

Table: accounts

* account_number (Primary Key)
* full_name
* email
* balance
* security_pin

## Future Improvements

* Add password hashing
* Add transaction history
* Improve validation and security
* Add admin panel
* Convert to GUI or Web-based system

## Author

Somen Nandi
