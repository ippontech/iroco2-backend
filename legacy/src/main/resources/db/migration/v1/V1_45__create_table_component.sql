CREATE TABLE IF NOT EXISTS component
(
    id                     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name                   varchar(255) NOT NULL,
    last_modification_date timestamp    NOT NULL,
    infrastructure_id      UUID         NOT NULL,
    csp_region             UUID         NOT NULL,
    service_id             UUID         NOT NULL,
    CONSTRAINT component__csp_service__fk FOREIGN KEY (service_id) REFERENCES cloud_service_provider_service (id),
    CONSTRAINT component__infrastructure__fk FOREIGN KEY (infrastructure_id) REFERENCES infrastructure (id),
    CONSTRAINT component__csp_region__fk FOREIGN KEY (csp_region) REFERENCES cloud_service_provider_service (id)
);