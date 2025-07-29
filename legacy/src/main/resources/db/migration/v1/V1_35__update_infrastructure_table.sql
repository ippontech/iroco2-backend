DROP TABLE infrastructure;

CREATE TABLE infrastructure (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                email VARCHAR NOT NULL,
                                json JSONB NOT NULL,
                                archived BOOLEAN NOT NULL DEFAULT false

);