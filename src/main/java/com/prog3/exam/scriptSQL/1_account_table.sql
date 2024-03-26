CREATE  TABLE IF NOT EXISTS  account(
    bigint primary key,
    client_name varchar(80),
    client_last_name varchar(80),
    birthdate date,
    monthly_net_income double precision,
    is_eligible boolean default false
    );


INSERT INTO account (account_number, client_name, client_last_name, birthdate, monthly_net_income, is_eligible)
VALUES (123456789, 'John', 'Doe', '1980-01-01', 5000.00, false)
    ON CONFLICT (account_number) DO NOTHING;
