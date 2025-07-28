CREATE TABLE IF NOT EXISTS SERVICES
(

    Name         TEXT PRIMARY KEY UNIQUE NOT NULL,
    Description  TEXT                    NOT NULL,
    Limitation   TEXT                    NOT NULL,
    Lever        TEXT                    NOT NULL,
    Availability TEXT                    NOT NULL
);