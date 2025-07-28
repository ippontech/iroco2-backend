CREATE TABLE IF NOT EXISTS configured_setting
(
    component_id         UUID          NOT NULL,
    configuration_setting_id      UUID          NOT NULL,
    value                         VARCHAR(255)  NOT NULL,
    PRIMARY KEY (component_id, configuration_setting_id)
);

