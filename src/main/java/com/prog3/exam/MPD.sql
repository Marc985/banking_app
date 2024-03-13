CREATE  DATABASE digital_bank;

CREATE TABLE account (
    account_id serial PRIMARY KEY,
    client_name varchar(150),
    client_last_name varchar(150),
    birthdate date,
    monthly_net_income decimal
);


CREATE TABLE sold (
    id_sold serial PRIMARY KEY,
    balance decimal,
    loans decimal,
    loansInterest int,
    "date" date,
    account_id integer,
    FOREIGN KEY (account_id) REFERENCES account(account_id)
);

CREATE TABLE IF NOT EXISTS "transaction" (
    id_transaction serial PRIMARY KEY,
    "type" varchar(6) CHECK ("type" IN ('debit', 'credit')),
    amount decimal,
    "date" date,
    reason varchar(20),
    sender_account_id integer REFERENCES account(account_id),
    recipient_account_id integer REFERENCES account(account_id)
);

CREATE TABLE IF NOT EXISTS transfert (
    reference UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    reason varchar(100),
    amount decimal,
    effective_date date,
    registration_date date,
    status varchar(7) CHECK (status IN ('cancel', 'pending', 'success'))
);

--//CREATE EXTENSION IF NOT EXISTS "uuid-ossp"; if uuid doesn't work!
