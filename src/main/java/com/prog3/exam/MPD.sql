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

