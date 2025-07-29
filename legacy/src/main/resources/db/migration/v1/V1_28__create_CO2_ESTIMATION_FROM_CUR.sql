CREATE TABLE IF NOT EXISTS CO2_ESTIMATION_FROM_CUR
(
    Id SERIAL PRIMARY KEY,
    correlation_id   VARCHAR(40),
    status VARCHAR(10),
    co2_estimation NUMERIC,
    number_of_message_processed BIGINT,
    number_of_message_expected BIGINT,
    created_date TIMESTAMP,
    modified_date TIMESTAMP
);







