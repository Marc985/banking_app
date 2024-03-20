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

CREATE  TABLE IF NOT EXISTS category(
    id_category serial primary key,
    category_name varchar(100) check category_name IN( CHECK (category_type IN ('nourriture et boisson', 'achat et boutique en ligne', 'logement', 'transports', 'vehicule', 'loisirs', 'multimedia Info')
)

CREATE  TABLE  IF NOT EXISTS  "transaction"(
    reference varchar(100)  primary key ,
    "type" varchar(20) check (type='debit' or type='credit'),
    amount double precision,
    "date" date,
    reason varchar(20),
    account_number bigint references account(account_number),
    int id_category REFERENCES category(id_category)

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



CREATE OR REPLACE FUNCTION account_statement(account_number INT,Date begin_date,Date end_date)
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
    t.reference,
    t."type",
    t.amount,
    t."date",
    t.reason,
 SUM(CASE WHEN t."type" = 'credit' THEN t.amount ELSE -t.amount END) OVER
(
PARTITION BY t.account_number
ORDER BY t."date"
ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
) AS balance
    FROM
    "transaction" t
    WHERE
    t."date" BETWEEN '2024-03-12' AND '2024-03-20' -- Replace '2024-03-20' and 'end_date' with your specific date range
    AND t.account_number = 1004 -- Replace 1004 with the specific account number
    ORDER BY
    t."date" DESC;
END; $$
LANGUAGE plpgsql;

