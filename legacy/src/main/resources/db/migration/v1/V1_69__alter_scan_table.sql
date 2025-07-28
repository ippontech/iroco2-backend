ALTER TABLE scan
    RENAME COLUMN user_id TO owner;

ALTER TABLE scan
    RENAME COLUMN account_id TO aws_account_id;

ALTER TABLE scan
    ADD COLUMN status VARCHAR;
