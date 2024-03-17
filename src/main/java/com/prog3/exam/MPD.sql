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
    reference UUID DEFAULT uuid_generate_v4() primary key ,
    "type" varchar(20) check (type='debit' or type='credit'),
    amount double precision,
    "date" date,
    reason varchar(20)


);



CREATE TABLE  IF NOT EXISTS transfert(
    reference UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    reason varchar(50),
    amount double precision,
    effective_date date,
    registration_date date,
    status varchar(30) check ( status='canceled' or status='pending' or status='success' ),
    sender_account bigint  REFERENCES account(account_number),
    recipient_account bigint  REFERENCES account(account_number)
);

CREATE TABLE IF NOT EXISTS interest_rate(
     id_interest_rate serial primary key,
    first_7days float,
     after_7days float
);

CREATE TABLE IF NOT EXISTS loans(
 id_loans serial primary key,
 "value" double precision,
  loan_date date,
  id_account biginteger references account(account_number)
)



CREATE or REPLACE FUNCTION get_sold_and_loan(account_id integer)
RETURNS TABLE (
sold_balance double precision,
loan_date,
loan_value double precision) as $$
BEGIN
RETURN QUERY
SELECT s.balance as sold_balance,l.value as loan_value
from sold as s
INNER JOIN
account on account.account_number=s.account_id
INNER JOIN
loans as l on l.id_account=account.account_number
WHERE account.account_number=get_sold_and_loan.account_id
ORDER BY s.date desc,l.loan_date desc limit 1 ;
END; $$ LANGUAGE plpgsql;
