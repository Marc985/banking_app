CREATE  TABLE client(
        id_client varchar(100) primary key ,
        name varchar(200),
        email varchar(50),
        pic varchar(80)
);
CREATE TABLE IF NOT EXISTS account (
    account_number bigint PRIMARY KEY,
    account_name VARCHAR,
    is_eligible boolean DEFAULT false,
    id_client varchar(100) references client(id_client)

);







