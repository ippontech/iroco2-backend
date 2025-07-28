CREATE TABLE IF NOT EXISTS scan (
    id UUID PRIMARY KEY,
    owner VARCHAR,
    status VARCHAR
);

CREATE TABLE IF NOT EXISTS estimated_payload (
    id UUID PRIMARY KEY,
    scan_id UUID,
    carbon_gram_footprint INTEGER,
    FOREIGN KEY (scan_id) REFERENCES scan (id)
);

DROP TABLE IF EXISTS co2_estimation_from_cur;
