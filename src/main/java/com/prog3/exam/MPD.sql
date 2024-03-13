CREATE  DATABASE digital_bank;

CREATE  TABLE IF NOT EXISTS  account(
    account_number bigint primary key,
    client_name varchar(80),
    client_last_name varchar(80),
    birthdate date,
    monthly_net_income decimal
);

CREATE TABLE IF NOT EXISTS sold(
    id_sold int primary key,
    balance decimal,
    loans decimal,
    loansInterest int,
    "date" date,
    account_id bigint  REFERENCES account(account_number)

);

CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE  TABLE  IF NOT EXISTS  "transaction"(
    reference UUID DEFAULT uuid_generate_v4() primary key ,
    "type" varchar(20) check (type='debit' or type='credit'),
    amount decimal,
    "date" date,
    reason varchar(20),
    sender_account bigint  REFERENCES account(account_number),
    recipient_account bigint  REFERENCES account(account_number)

);



CREATE TABLE  IF NOT EXISTS transfert(
    reference UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    reason varchar(50),
    amount decimal,
    effective_date date,
    registration_date date,
    status varchar(30) check ( status='canceled' or status='pending' or status='success' )

);

--//CREATE EXTENSION IF NOT EXISTS "uuid-ossp"; if uuid doesn't work!
