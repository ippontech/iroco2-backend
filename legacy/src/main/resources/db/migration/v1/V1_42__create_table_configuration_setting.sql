CREATE TABLE IF NOT EXISTS configuration_setting
(
    id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name      VARCHAR NOT NULL UNIQUE
);

