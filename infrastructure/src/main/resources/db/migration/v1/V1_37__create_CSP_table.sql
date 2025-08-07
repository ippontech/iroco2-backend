CREATE TABLE IF NOT EXISTS cloud_service_provider
(
    id   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR NOT NULL
);

INSERT INTO cloud_service_provider (name)
VALUES ('AWS');