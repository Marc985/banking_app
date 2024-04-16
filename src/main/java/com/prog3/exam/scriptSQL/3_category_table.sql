CREATE TABLE IF NOT EXISTS "category"(
    id_category serial primary key,
    category_name varchar(100),
    category_type varchar(30) CHECK (category_type='debit' OR category_type='credit')
    );

