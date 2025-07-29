DROP TABLE IF EXISTS infrastructure;

CREATE TABLE infrastructure (
                                id SERIAL PRIMARY KEY,
                                email VARCHAR NOT NULL,
                                json JSONB NOT NULL,
                                archived BOOLEAN NOT NULL DEFAULT false

);