CREATE DATABASE IF NOT EXISTS bankApp ;
USE bankApp;

CREATE TABLE user(
email varchar(50) PRIMARY KEY NOT NULL,
full_name varchar(100) NOT NULL,
password varchar(50) NOT NULL);

CREATE TABLE accounts(
account_number INT PRIMARY KEY,
full_name varchar(100) NOT NULL,
email varchar(50) NOT NULL,
balance DECIMAL(10,2) NOT NULL,
security_pin char(4) NOT NULL,
FOREIGN KEY (email) REFERENCES user (email)
ON DELETE CASCADE ON UPDATE CASCADE);




