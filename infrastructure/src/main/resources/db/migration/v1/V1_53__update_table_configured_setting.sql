DROP TABLE configured_setting;

CREATE TABLE configured_setting
(
    id                          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    component_id                UUID NOT NULL,
    configuration_setting_id    UUID NOT NULL,
    value                       varchar(255) NOT NULL,
    CONSTRAINT fk__configured_setting__component_id FOREIGN KEY (component_id) REFERENCES component (id),
    CONSTRAINT fk__configured_setting__configuration_setting_id FOREIGN KEY (configuration_setting_id) REFERENCES configuration_setting (id)
);

CREATE INDEX idx__configured_setting__component_id ON configured_setting (component_id);
