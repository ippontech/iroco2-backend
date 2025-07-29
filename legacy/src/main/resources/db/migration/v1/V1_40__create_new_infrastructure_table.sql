ALTER TABLE infrastructure
    RENAME TO infrastructure_legacy;


CREATE TABLE infrastructure
(
    id                     uuid PRIMARY KEY,
    owner                  VARCHAR(255) NOT NULL,
    name                   VARCHAR(255) NOT NULL,
    cloud_service_provider uuid    NOT NULL,
    default_region         uuid    NOT NULL
);