CREATE  DATABASE digital_bank;

CREATE  TABLE IF NOT EXISTS  account(
    account_number bigint primary key,
    client_name varchar(80),
    client_last_name varchar(80),
    birthdate date,
    monthly_net_income double precision,
    is_eligible boolean default false
);

CREATE TABLE IF NOT EXISTS sold(
    id_sold serial primary key,
    balance double precision,
    "date" date,
    account_id bigint  REFERENCES account(account_number)

);

CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE  TABLE  IF NOT EXISTS  "transaction"(
    reference varchar(100)  primary key ,
    "type" varchar(20) check (type='debit' or type='credit'),
    amount double precision,
    "date" date,
    reason varchar(20),
    account_number bigint references account(account_number)

);



CREATE TABLE  IF NOT EXISTS transfert(
    reference varchar(100) primary key,
    reason varchar(50),
    amount double precision,
    effective_date date,
    registration_date date,
    status varchar(30) check ( status='canceled' or status='pending' or status='success' ),
    account bigint  REFERENCES account(account_number)

);

--CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE IF NOT EXISTS interest_rate(
     id_interest_rate serial primary key,
    first_7days float,
     after_7days float
);



CREATE OR REPLACE FUNCTION account_statement(account_number INT)
RETURNS TABLE (
    reference varchar(100),
    type varchar(20),
    amount double precision,
    transaction_date DATE,
    reason varchar(20),
    balance double precision
) AS $$
BEGIN
RETURN QUERY
SELECT
    transaction.reference,
    transaction.type,
    transaction.amount,
    transaction.date,
    transaction.reason,
    sold.balance
FROM
    transaction
        INNER JOIN
    account ON account.account_number = transaction.account_number
        INNER JOIN
    sold ON sold.account_id = transaction.account_number
WHERE
    transaction.date = sold.date AND
    account.account_number = account_statement.account_number;
END; $$
LANGUAGE plpgsql;





__________ DUMMY DATA_____________


INSERT INTO account (account_number, client_name, client_last_name, birthdate, monthly_net_income, is_eligible)
VALUES
(123456789, 'John', 'Doe', '1990-01-01', 5000.0, true),
(987654321, 'Jane', 'Smith', '1995-05-15', 6000.0, false),
(555555555, 'Alice', 'Johnson', '1988-11-30', 7000.0, true),
(888888888, 'Bob', 'Williams', '1992-09-10', 5500.0, true);


INSERT INTO sold (balance, "date", account_id)
VALUES
(1500.0, '2024-03-15', 123456789),
(2500.0, '2024-03-16', 987654321),
(1800.0, '2024-03-17', 555555555),
(3000.0, '2024-03-18', 888888888);


INSERT INTO "transaction" (reference, "type", amount, "date", reason, account_number)
VALUES
('REF123', 'debit', 500.0, '2024-03-15', 'Purchase', 123456789),
('REF456', 'credit', 1000.0, '2024-03-16', 'Salary', 987654321),
('REF789', 'debit', 200.0, '2024-03-17', 'Utility Bill', 555555555),
('REFABC', 'credit', 1500.0, '2024-03-18', 'Refund', 888888888);



INSERT INTO transfert (reason, amount, effective_date, registration_date, status, sender_account, recipient_account)
VALUES
('Payment', 200.0, '2024-03-20', '2024-03-20', 'success', 123456789, 987654321),
('Transfer', 500.0, '2024-03-19', '2024-03-19', 'pending', 987654321, 123456789),
('Refund', 100.0, '2024-03-18', '2024-03-18', 'success', 555555555, 888888888),
('Payment', 300.0, '2024-03-17', '2024-03-17', 'canceled', 888888888, 555555555);



INSERT INTO interest_rate (first_7days, after_7days)
VALUES
(0.05, 0.07),
(0.03, 0.05),
(0.04, 0.06);


